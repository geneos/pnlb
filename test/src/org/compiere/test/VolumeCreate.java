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

import java.util.*;
import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.util.*;
import org.compiere.model.*;


/**
 * 	Volume test Create
 *	
 *  @author Jorg Janke
 *  @version $Id: VolumeCreate.java,v 1.2 2005/11/28 03:34:13 jjanke Exp $
 */
public class VolumeCreate
{
	/**
	 *	VolumeCreate
	 *	@param noProducts
	 *	@param noBP
	 *	@param noBPLocation
	 *	@param noBPUser
	 *	@param noOrder
	 *	@param noOrderLine
	 */
	public VolumeCreate (int noProducts, 
		int noBP, int noBPLocation, int noBPUser, 
		int noOrder, int noOrderLine)
	{
		Compiere.startup(true);
		CLogMgt.setLevel(Level.WARNING);
		//
		Ini.setProperty(Ini.P_UID,"GardenAdmin");
		Ini.setProperty(Ini.P_PWD,"GardenAdmin");
		Ini.setProperty(Ini.P_ROLE,"GardenWorld Admin");
		Ini.setProperty(Ini.P_CLIENT, "GardenWorld");
		Ini.setProperty(Ini.P_ORG,"HQ");
		Ini.setProperty(Ini.P_WAREHOUSE,"HQ Warehouse");
		Ini.setProperty(Ini.P_LANGUAGE,"English");
	//	Ini.setProperty(Ini.P_PRINTER,"MyPrinter");
		//
		m_ctx = Env.getCtx();
		Login login = new Login(m_ctx);
		if (!login.batchLogin())
			throw new IllegalStateException("Login failed");
		//
		PREFIX = "Test";
		createEnv();
		createProduct(noProducts);
		createBP(noBP, noBPLocation, noBPUser);
		createOrder(noOrder, noOrderLine);
	}	//	VolumeCreate

	/**	Context					*/
	private Properties 		m_ctx = null;
	/**	Logger					*/
	private static CLogger	log	= CLogger.getCLogger (VolumeCreate.class);
	/** Prefix					*/
	private static String	PREFIX = "Test";

	private MClient				m_client = null;
	private MAcctSchema			m_as = null;
	private MProductCategory	m_pc = null;
	private MTaxCategory 		m_tc = null;
	private MTax 				m_tax = null;
	private MUOM				m_uom = null;
	private MPriceList			m_pl = null;
	private MDiscountSchema		m_ds = null;
	private MPriceListVersion	m_plv = null;
	private MBPGroup 			m_grp = null;

	
	/**	First BPartners			*/
	private ArrayList<MBPartner>	m_list_bp = new ArrayList<MBPartner>();
	/**	First Products			*/
	private ArrayList<MProduct>		m_list_p = new ArrayList<MProduct>();

	
	/**
	 * 	Create Environment
	 */
	private void createEnv()
	{
		m_client = MClient.get(m_ctx);
		MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(m_ctx, m_client.getAD_Client_ID());
		m_as = ass[0];
		
		m_pc = new MProductCategory(m_ctx, 0, null);
		m_pc.setValue(PREFIX);
		m_pc.setName(PREFIX);
		m_pc.save();
		//
		m_tc = new MTaxCategory (m_ctx, 0, null);
		m_tc.setName(PREFIX);
		m_tc.save();
		//
		m_tax = new MTax (m_ctx, PREFIX, new BigDecimal(5), m_tc.getC_TaxCategory_ID(), null);
		m_tax.save();
		//
		m_uom = new MUOM (m_ctx, 0, null);
		m_uom.setName(PREFIX);
		m_uom.setX12DE355(PREFIX);
		m_uom.save();
		//
		m_pl = new MPriceList(m_ctx, 0, null);
		m_pl.setName(PREFIX);
		m_pl.setC_Currency_ID(m_as.getC_Currency_ID());
		m_pl.save();
		//
		m_ds = new MDiscountSchema (m_ctx, 0, null);
		m_ds.setName(PREFIX);
		m_ds.setValidFrom(new Timestamp(System.currentTimeMillis()));
		m_ds.save();
		//
		m_plv = new MPriceListVersion(m_pl);
		m_plv.setName(PREFIX);
		m_plv.setValidFrom(new Timestamp(System.currentTimeMillis()));
		m_plv.setM_DiscountSchema_ID(m_ds.getM_DiscountSchema_ID());
		m_plv.save();
		//
		m_grp = new MBPGroup (m_ctx, 0, null);
		m_grp.setValue(PREFIX);
		m_grp.setName(PREFIX);
		m_grp.save();

	}	//	createEnv
	
	/**
	 * 	Create Business Partners
	 *	@param no no of BPartners to create
	 *	@param noUsers number of users per BP
	 *	@param noAddress number of addresses per BP
	 */
	private void createBP (int no, int noUsers, int noAddress)
	{
		log.info("No=" + no);
		long start = System.currentTimeMillis();
		int created = 0;
		
		//	Create BP
		for (int bpI = 0; bpI < no; bpI++)
		{
			MBPartner bp = new MBPartner(m_ctx, 0, null);
			bp.setValue(PREFIX + bpI);
			bp.setName(PREFIX + "_BP_" + bpI);
			bp.setC_BP_Group_ID(m_grp.getC_BP_Group_ID());
			if (!bp.save())
				break;
			if (m_list_bp.size() < 10)
				m_list_bp.add(bp);
			//	Users
			for (int uI = 0; uI < noUsers; uI++)
			{
				MUser u = new MUser(bp);
				u.setName (PREFIX + "_BP_" + bpI + "_U_" + uI);
				if (!u.save())
					break;
			}
			//	Addresses
			created++;
			for (int aI = 0; aI < noAddress; aI++)
			{
				MBPartnerLocation bpl = new MBPartnerLocation(bp);
				MLocation loc = new MLocation(m_ctx, 0, null);
				loc.setCity(PREFIX + "_BP_" + bpI + "_A_" + aI);
				if (!loc.save())
					break;
				//
				bpl.setC_Location_ID(loc.getC_Location_ID());
				bpl.setName (PREFIX + "_BP_" + bpI + "_A_" + aI);
				if (!bpl.save())
					break;
			}
		}	//	BP
		//
		long ms = System.currentTimeMillis() - start;
		long each = 0;
		if (created != 0)
			each = ms / created;
		log.warning("Created=" + created + " - " + ms + "ms - each=" + each);
	}	//	createBP

	/**
	 * 	Create Products
	 *	@param no number to create
	 */
	private void createProduct (int no)
	{
		log.info("No=" + no);
		long start = System.currentTimeMillis();
		int created = 0;
		
		//	Create Products
		for (int pI = 0; pI < no; pI++)
		{
			MProduct p = new MProduct(m_ctx, 0, null);
			p.setValue (PREFIX + pI);
			p.setName (PREFIX + "_P_" + pI);
			p.setM_Product_Category_ID(m_pc.getM_Product_Category_ID());
			p.setC_TaxCategory_ID (m_tc.getC_TaxCategory_ID());
			p.setC_UOM_ID (m_uom.getC_UOM_ID());
			if (!p.save())
				break;
			if (m_list_p.size() < 10)
				m_list_p.add(p);
			//
			MProductPrice pp = new MProductPrice (m_plv, p.getM_Product_ID(), 
				new BigDecimal(pI+2), new BigDecimal(pI+1), new BigDecimal(pI));
			if (!pp.save())
				break;
			//
			created++;
		}
		//
		long ms = System.currentTimeMillis() - start;
		long each = 0;
		if (created != 0)
			each = ms / created;
		log.warning("Created=" + created + " - " + ms + "ms - each=" + each);
		
	}	//	createProduct
	
	/**
	 * 	Create Orders
	 *	@param no number to create
	 */
	private void createOrder (int no, int noLines)
	{
		log.info("No=" + no);
		long start = System.currentTimeMillis();
		int created = 0;
		
		int indexBP = 0;
		int indexP = 0;
		//	Create
		for (int oI = 0; oI < no; oI++)
		{
			MOrder order = new MOrder (m_ctx, 0, null);
			order.setDescription(PREFIX + "_O_" + oI);
			order.setIsSOTrx(true);
			order.setC_DocTypeTarget_ID(MOrder.DocSubTypeSO_POS);
			order.setBPartner(m_list_bp.get(indexBP));
			if (++indexBP >= m_list_bp.size())
				indexBP = 0;
			order.setM_PriceList_ID(m_pl.getM_PriceList_ID());
			if (!order.save())
				break;
			for (int lI = 0; lI < noLines; lI++)
			{
				MOrderLine ol = new MOrderLine(order);
				order.setDescription(PREFIX + "_O_" + oI + "_L_" + lI);
				ol.setQty(new BigDecimal(lI+1));
				ol.setProduct(m_list_p.get(indexP));
				if (++indexP >= m_list_p.size())
					indexP = 0;
				ol.setPrice();
				if (!ol.save())
					break;
			}
			order.setDocAction(MOrder.DOCACTION_Complete);
			order.processIt(MOrder.DOCACTION_Complete);
			order.save();
			//
			created++;
		}
		//
		long ms = System.currentTimeMillis() - start;
		long each = 0;
		if (created != 0)
			each = ms / created;
		log.warning("Created=" + created + " - " + ms + "ms - each=" + each);
	}	//	createOrder
	
	/**
	 * 	Create X
	 *	@param no number to create
	 */
	private void createX (int no)
	{
		log.info("No=" + no);
		long start = System.currentTimeMillis();
		int created = 0;
		//	Create
		for (int bpI = 0; bpI < no; bpI++)
		{
			created++;
		}
		//
		long ms = System.currentTimeMillis() - start;
		long each = 0;
		if (created != 0)
			each = ms / created;
		log.warning("Created=" + created + " - " + ms + "ms - each=" + each);
		
	}	//	createX

	
	/**************************************************************************
	 * 	Test
	 *	@param args
	 */
	public static void main (String[] args)
	{
		new VolumeCreate(30000, 
			30000, 2, 2, 
			5000, 5);
	}	//	main
	
}	//	VolumeCreate
