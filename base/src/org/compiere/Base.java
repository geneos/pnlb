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
package org.compiere;

import java.math.*;
import java.util.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Base Library Test Classes mainly for Optimize it
 *
 *  @author Jorg Janke
 *  @version $Id: Base.java,v 1.22 2005/03/11 20:26:12 jjanke Exp $
 */
class Base
{
	/**
	 *  Base Test
	 */
	public static void test()
	{
		System.out.println("** Before Init **"); //$NON-NLS-1$
		getMemoryUsed();
		Properties ctx = Login.initTest(false);
	//	Log.printProperties(System.getProperties(), "System", false);
		//
		System.gc();    //  cleanup Init
		//
		System.out.println("** Before Creation **");
		long start = getMemoryUsed();

		//  *******************************************************************

		//  Table=100, Shipper=142, Window=102, Reference=101
		int AD_Window_ID = 102;
		long startTime = System.currentTimeMillis();
		MWindowVO vo = MWindowVO.create(Env.getCtx(), 1, AD_Window_ID);
		MWindow w = new MWindow(vo);
		long endDef = System.currentTimeMillis();
		System.out.println("Load Definition Time in ms = " + String.valueOf(endDef-startTime));
		if (1==2)   //  optional step
		{
			w.loadCompete();
			long endDefComplete = System.currentTimeMillis();
			System.out.println("Load Definition Complete Time in ms = " + String.valueOf(endDefComplete-startTime));
		}
		w.query();
		long endData = System.currentTimeMillis();
		System.out.println("Load Data Time in ms = " + String.valueOf(endData-startTime));
		w.loadCompete();
		long endDataComplete = System.currentTimeMillis();
		System.out.println("Load Data Complete Time in ms = " + String.valueOf(endDataComplete-startTime));
		w.getTab(0).navigate(0);

		//  *******************************************************************
//		sleep();

		System.out.println("** Before Dispose **");
		getMemoryUsed();
		w.dispose();
//		sleep();
		//
		System.out.println("** Before GC **");
		getMemoryUsed();
		w = null;
		System.gc();
		System.out.println("** After GC **");
		getMemoryUsed();
		System.gc();

		System.out.println("** Final **");
		long complete = System.currentTimeMillis();
		System.out.println("Complete Time in ms = " + String.valueOf(complete-startTime));
		long end = getMemoryUsed();
		System.out.println("Memory increase in kB = End-Start=" + String.valueOf((end-start)/1024));
		listThreads();
		//
		System.out.println("API Test");
		System.out.println("64.72=" + MConversionRate.convert(ctx, new BigDecimal(100.0), 116, 100,0,0));
		System.out.println("0.647169=" + MConversionRate.getRate(116, 100, null, 0,0,0));
		System.out.println("12.5=" + MUOMConversion.convert(101, 102, new BigDecimal(100.0), true));

	}   //  Base

	/**
	 *  Get Used Memory in bytes
	 */
	private static long getMemoryUsed()
	{
		long free = Runtime.getRuntime().freeMemory();
		long total = Runtime.getRuntime().totalMemory();
		long used = total - free;
		//
		System.out.println("Memory used in kB = Total("
			+ String.valueOf(total/1024) + ")-Free("
			+ String.valueOf(free/1024) + ") = " + String.valueOf(used/1024));
		System.out.println("Active Threads=" + Thread.activeCount());
		return used;
	}   //  getMemoryUsed

	/**
	 *  Sleep for a second
	 */
	private static void sleep()
	{
		System.out.println(".. sleeping-ini .. -> " + Thread.activeCount());
		Thread.yield();
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException ie)
		{}
		System.out.println(".. sleeping-end .. -> " + Thread.activeCount());
	}   //  sleep

	/**
	 *  List Threads
	 */
	private static void listThreads()
	{
		Thread[] list = new Thread[Thread.activeCount()];
	//	int no = Thread.currentThread().enumerate(list);
		for (int i = 0; i < list.length; i++)
		{
			if (list[i] != null)
				System.out.println("Thread " + i + " - " + list[i].toString());
		}
	}   //  listThreads

	/**
	 *  Start
	 */
	public static void main(String[] args)
	{
		Base.test();
		Env.exitEnv(0);
	}   //  main
}   //  Base
