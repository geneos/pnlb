/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.test;

import java.math.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.*;
import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.*;


/**
 *	Order Test Example
 *	
 *  @author Jorg Janke
 *  @version $Id: OrderTest.java,v 1.2 2005/03/11 20:30:16 jjanke Exp $
 */
public class OrderTest implements Runnable
{
	/**
	 * 	OrderTest
	 * 	@param no thread number
	 * 	@param numberOrders number of orders to create
	 */
	public OrderTest (int no, int numberOrders, int avgLines) 
	{
		super ();
		m_no = no;
		m_numberOrders = numberOrders;
		m_maxLines = avgLines * 2;
	}	//	OrderTest
	
	int m_no = 0;
	int m_numberOrders = 0;
	int m_maxLines = 20;

	int m_errors = 0;
	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (OrderTest.class);
	
	/**
	 * 	Run
	 */
	public void run()
	{
		long time = System.currentTimeMillis();
		int count = 0;
		MBPartner bp = new MBPartner(Env.getCtx(), 117, null);
		bp.setSOCreditStatus(MBPartner.SOCREDITSTATUS_NoCreditCheck);
		bp.save();
		
		//
		for (int i = 0; i < m_numberOrders; i++)
		{
			Trx trx = Trx.get(Trx.createTrxName("Test" + m_no + "_" + i),true);
			trx.start();
			//
			MOrder order = new MOrder(Env.getCtx(),0,trx.getTrxName());
			order.setDescription("#" + m_no + "_" + i);
			order.setC_DocTypeTarget_ID(135); 	//	POS
			order.setC_BPartner_ID(117);		//	C&W
			order.setSalesRep_ID(101);			//	GardenAdmin
			order.setDeliveryRule(MOrder.DELIVERYRULE_Force);
			if (!order.save())
			{
				log.warning("#" + m_no + "_" + i + ": Not saved(1)");
				m_errors++;
				continue;
			}
			Random r = new Random();
			int linesNumber = r.nextInt(m_maxLines) + 1;
			for (int j = 0; j < linesNumber; j++)
			{
				MOrderLine line = new MOrderLine(order);
				line.setM_Product_ID(123);		//	Oak Tree
				line.setQty(new BigDecimal(5));
				if (!line.save())
				{
					log.warning("#" + m_no + "_" + i + ": Line not saved");
					m_errors++;
				}
			}
			//	Process
			order.setDocAction(DocAction.ACTION_Complete);
			if (!order.processIt(DocAction.ACTION_Complete))
			{
				log.warning("#" + m_no + "_" + i + ": Not processed");
				m_errors++;
				trx.rollback();
				trx.close();
				continue;
			}
			if (!order.save())
			{
				log.warning("#" + m_no + "_" + i + ": Not saved(2)");
				m_errors++;
			}
			else
				count++;
			trx.commit();
			trx.close();
			//
			log.info(order.toString());
		}
		time = System.currentTimeMillis() - time;
		log.warning("#" + m_no + ", Errors=" + m_errors
			+ ", Count=" + count 
			+ " " + ((float)count*100/m_numberOrders)
			+ "% - " + time + "ms - ea " + ((float)time/count) + "ms");
	}	//	run
	
	/**
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		Compiere.startup(true);
		CLogMgt.setLoggerLevel(Level.INFO, null);
		CLogMgt.setLevel(Level.INFO);
		//
		Ini.setProperty(Ini.P_UID,"SuperUser");
		Ini.setProperty(Ini.P_PWD,"System");
		Ini.setProperty(Ini.P_ROLE,"GardenWorld Admin");
		Ini.setProperty(Ini.P_CLIENT, "GardenWorld");
		Ini.setProperty(Ini.P_ORG,"HQ");
		Ini.setProperty(Ini.P_WAREHOUSE,"HQ Warehouse");
		Ini.setProperty(Ini.P_LANGUAGE,"English");
		Login login = new Login(Env.getCtx());
		if (!login.batchLogin(null))
			System.exit(1);
		//
		CLogMgt.setLoggerLevel(Level.WARNING, null);
		CLogMgt.setLevel(Level.WARNING);

		int NO_TESTS = 2;
		int NO_ORDERS = 200;
		int NO_LINES = 20;
		
		long time = System.currentTimeMillis();
		Thread[] tests = new Thread[NO_TESTS];
		for (int i = 0; i < tests.length; i++)
		{
			tests[i] = new Thread(new OrderTest(i, NO_ORDERS, NO_LINES));
			tests[i].start();
		}
		//	Wait
		for (int i = 0; i < tests.length; i++)
		{
			try
			{
				tests[i].join();
			}
			catch (InterruptedException e)
			{
			}
		}
		time = System.currentTimeMillis() - time;

		System.out.println("Time (ms)=" + time);

	}	//	main
	
}	//	OrderTest
