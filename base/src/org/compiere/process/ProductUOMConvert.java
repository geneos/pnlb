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
package org.compiere.process;

import java.math.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Product UOM Conversion
 *	
 *  @author Jorg Janke
 *  @version $Id: ProductUOMConvert.java,v 1.4 2005/03/11 20:25:58 jjanke Exp $
 */
public class ProductUOMConvert extends SvrProcess
{
	/** Product From			*/
	private int			p_M_Product_ID = 0;
	/** Product To				*/
	private int			p_M_Product_To_ID = 0;
	/** Locator					*/
	private int			p_M_Locator_ID = 0;
	/** Quantity				*/
	private BigDecimal	p_Qty = null;

	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Product_To_ID"))
				p_M_Product_To_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Locator_ID"))
				p_M_Locator_ID = para[i].getParameterAsInt();
			else if (name.equals("Qty"))
				p_Qty = (BigDecimal)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		if (p_M_Product_ID == 0 || p_M_Product_To_ID == 0
			|| p_M_Locator_ID == 0 
			|| p_Qty == null || Env.ZERO.compareTo(p_Qty) == 0)
			throw new CompiereUserError("Invalid Parameter");
		//
		MProduct product = MProduct.get(getCtx(), p_M_Product_ID);
		MProduct productTo = MProduct.get(getCtx(), p_M_Product_To_ID);
		log.info("Product=" + product + ", ProductTo=" + productTo 
			+ ", M_Locator_ID=" + p_M_Locator_ID + ", Qty=" + p_Qty);
		
		MUOMConversion[] conversions = MUOMConversion.getProductConversions(getCtx(), product.getM_Product_ID());
		MUOMConversion conversion = null;
		for (int i = 0; i < conversions.length; i++)
		{
			if (conversions[i].getC_UOM_To_ID() == productTo.getC_UOM_ID())
				conversion = conversions[i];
		}
		if (conversion == null)
			throw new CompiereUserError("@NotFound@: @C_UOM_Conversion_ID@");
		
		MUOM uomTo = MUOM.get(getCtx(), productTo.getC_UOM_ID());
		BigDecimal qtyTo = p_Qty.divide(conversion.getDivideRate(), uomTo.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
		BigDecimal qtyTo6 = p_Qty.divide(conversion.getDivideRate(), 6, BigDecimal.ROUND_HALF_UP);
		if (qtyTo.compareTo(qtyTo6) != 0)
			throw new CompiereUserError("@StdPrecision@: " + qtyTo + " <> " + qtyTo6 
				+ " (" + p_Qty + "/" + conversion.getDivideRate() + ")");
		log.info(conversion + " -> " + qtyTo); 
		
		
		//	Set to Beta
		return "Not completed yet";
	}	//	doIt
	
}	//	ProductUOMConvert
