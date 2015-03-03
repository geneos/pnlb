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
package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import org.compiere.util.*;

/**
 *	Requisition Callouts
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutRequisition.java,v 1.6 2005/03/11 20:26:05 jjanke Exp $
 */
public class CalloutRequisition extends CalloutEngine
{
	/**
	 *	Requisition Line - Product.
	 *		- PriceStd
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String product (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer M_Product_ID = (Integer)value;
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
			return "";
	//	setCalloutActive(true);
		//
		/**	Set Attribute
		if (Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()
			&& Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID") != 0)
			mTab.setValue("M_AttributeSetInstance_ID", new Integer(Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID")));
		else
			mTab.setValue("M_AttributeSetInstance_ID", null);
		**/	
		int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, WindowNo, "C_BPartner_ID");
		BigDecimal Qty = (BigDecimal)mTab.getValue("Qty");
		boolean isSOTrx = false;
		MProductPricing pp = new MProductPricing (M_Product_ID.intValue(), 
			C_BPartner_ID, Qty, isSOTrx);
		//
		int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_ID");
		pp.setM_PriceList_ID(M_PriceList_ID);
		int M_PriceList_Version_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_Version_ID");
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
		Timestamp orderDate = (Timestamp)mTab.getValue("DateRequired");
		pp.setPriceDate(orderDate);
		//		
		mTab.setValue("PriceActual", pp.getPriceStd());
		Env.setContext(ctx, WindowNo, "EnforcePriceLimit", pp.isEnforcePriceLimit() ? "Y" : "N");	//	not used
		Env.setContext(ctx, WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");

	//	setCalloutActive(false);
		return "";
	}	//	product
	
	/**
	 *	Order Line - Amount.
	 *		- called from Qty, PriceActual
	 *		- calculates LineNetAmt
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String amt (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		//	Qty changed - recalc price
		if (mField.getColumnName().equals("Qty") 
			&& "Y".equals(Env.getContext(ctx, WindowNo, "DiscountSchema")))
		{
			int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, WindowNo, "M_Product_ID");
			int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, WindowNo, "C_BPartner_ID");
			BigDecimal Qty = (BigDecimal)value;
			boolean isSOTrx = false;
			MProductPricing pp = new MProductPricing (M_Product_ID, 
				C_BPartner_ID, Qty, isSOTrx);
			//
			int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_ID");
			pp.setM_PriceList_ID(M_PriceList_ID);
			int M_PriceList_Version_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_Version_ID");
			pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
			Timestamp orderDate = (Timestamp)mTab.getValue("DateInvoiced");
			pp.setPriceDate(orderDate);
			//
			mTab.setValue("PriceActual", pp.getPriceStd());
		}

		int StdPrecision = Env.getContextAsInt(ctx, WindowNo, "StdPrecision");
		BigDecimal Qty = (BigDecimal)mTab.getValue("Qty");
		BigDecimal PriceActual = (BigDecimal)mTab.getValue("PriceActual");

		//	get values
		log.fine("amt - Qty=" + Qty + ", Price=" + PriceActual + ", Precision=" + StdPrecision);

		//	Multiply
		BigDecimal LineNetAmt = Qty.multiply(PriceActual);
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		mTab.setValue("LineNetAmt", LineNetAmt);
		log.info("amt - LineNetAmt=" + LineNetAmt);
		//
		setCalloutActive(false);
		return "";
	}	//	amt

        
	
}	//	CalloutRequisition
