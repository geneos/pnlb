/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/
//package org.compiere.mfg.model;
package org.eevolution.model;

import java.math.*;
import java.util.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.tree.*;

import org.compiere.util.*;
import org.compiere.util.*;
import org.compiere.apps.*;
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.print.*;
import org.compiere.swing.*;

/**
 *	Order Callouts.
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutOrder.java,v 1.11 2004/03/22 07:15:03 jjanke Exp $
 */
public class CalloutBOM extends CalloutEngine
{
	/**	Debug Steps			*/
	private boolean steps = false;

	/**
	 *	Order Header Change - DocType.
	 *		- InvoiceRuld/DeliveryRule/PaymentRule
	 *		- temporary Document
	 *  Context:
	 *  	- DocSubTypeSO
	 *		- HasCharges
	 *	- (re-sets Business Partner info of required)
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */



	/**
	 *	Parent cicle.
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 */
	public String parent (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		
		if (isCalloutActive() || value == null)
			return "";
		

		if (steps) log.warning("parent - init");
		
		setCalloutActive(true);
		//BigDecimal QtyOrdered, PriceActual, PriceLimit, Discount, PriceList;
		//int StdPrecision = Env.getContextAsInt(ctx, WindowNo, "StdPrecision");
               
                /*
		//	get values
		QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
		PriceActual = (BigDecimal)mTab.getValue("PriceActual");
	//	PriceActual = PriceActual.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		Discount = (BigDecimal)mTab.getValue("Discount");
		//
		PriceLimit = (BigDecimal)mTab.getValue("PriceLimit");
	//	PriceLimit = OriceLimit.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		PriceList = (BigDecimal)mTab.getValue("PriceList");
		log.fine("amt - Ordered=" + QtyOrdered + ", List=" + PriceList + ", Limit=" + PriceLimit + ", Precision=" + StdPrecision);
		log.fine("amt ~ Actual=" + PriceActual + ", Discount=" + Discount);
                 */

		//  calculate Actual if discount entered
		
						Integer M_Product_ID = (Integer)value;
                        int MPC_Product_BOM_ID = Env.getContextAsInt(ctx, WindowNo, "MPC_Product_BOM_ID");
                        X_MPC_Product_BOM MPC_Product_BOM = new X_MPC_Product_BOM(ctx, MPC_Product_BOM_ID,"MPC_Product_BOM");
                        if (MPC_Product_BOM.getM_Product_ID() ==  M_Product_ID.intValue())
                        {                                                                               
                            JOptionPane.showMessageDialog(null,"ValidComponent" , "Error Parent not be Componet" , JOptionPane.ERROR_MESSAGE);				
                            return ""; 
                        }
        setCalloutActive(false);
		return "";
	}	//	amt
        
    public String qty (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		if (steps) log.warning("qty - init - M_Product_ID=" + M_Product_ID + " - " );
		BigDecimal QtyOrdered, QtyEntered; //, PriceActual, PriceEntered;
		
		//	No Product
		if (M_Product_ID == 0)
		{
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			mTab.setValue("QtyOrdered", QtyEntered);
		}
		//	UOM Changed - convert from Entered -> Product
		else if (mField.getColumnName().equals("C_UOM_ID"))
		{
			int C_UOM_To_ID = ((Integer)value).intValue();
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			//PriceActual = (BigDecimal)mTab.getValue("PriceActual");
			//PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
			//	C_UOM_To_ID, PriceActual);
			//if (PriceEntered == null)
			//	PriceEntered = PriceActual; 
		//	log.fine("qty - UOM=" + C_UOM_To_ID 
		//		+ ", QtyEntered/PriceActual=" + QtyEntered + "/" + PriceActual
		//		+ " -> " + conversion 
		//		+ " QtyOrdered/PriceEntered=" + QtyOrdered + "/" + PriceEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
		//	mTab.setValue("PriceEntered", PriceEntered);
		}
		//	QtyEntered changed - calculate QtyOrdered
		else if (mField.getColumnName().equals("QtyEntered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyEntered = (BigDecimal)value;
			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			log.fine("qty - UOM=" + C_UOM_To_ID 
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion 
				+ " QtyOrdered=" + QtyOrdered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
		}
		//	QtyOrdered changed - calculate QtyEntered
		else if (mField.getColumnName().equals("QtyOrdered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyOrdered = (BigDecimal)value;
			QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyOrdered);
			if (QtyEntered == null)
				QtyEntered = QtyOrdered;
			boolean conversion = QtyOrdered.compareTo(QtyEntered) != 0;
			log.fine("qty - UOM=" + C_UOM_To_ID 
				+ ", QtyOrdered=" + QtyOrdered
				+ " -> " + conversion 
				+ " QtyEntered=" + QtyEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyEntered", QtyEntered);
		}
		//
		setCalloutActive(false);
		return "";
	}	//	qty
        
        public String qtyLine (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		setCalloutActive(true);

		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		if (steps) log.warning("qty - init - M_Product_ID=" + M_Product_ID + " - " );
		BigDecimal QtyRequiered, QtyEntered; //, PriceActual, PriceEntered;
		
		//	No Product
		if (M_Product_ID == 0)
		{
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			mTab.setValue("QtyOrdered", QtyEntered);
		}
		//	UOM Changed - convert from Entered -> Product
		else if (mField.getColumnName().equals("C_UOM_ID"))
		{
			int C_UOM_To_ID = ((Integer)value).intValue();
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			QtyRequiered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyRequiered == null)
				QtyRequiered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyRequiered) != 0;
			//PriceActual = (BigDecimal)mTab.getValue("PriceActual");
			//PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
			//	C_UOM_To_ID, PriceActual);
			//if (PriceEntered == null)
			//	PriceEntered = PriceActual; 
		//	log.fine("qty - UOM=" + C_UOM_To_ID 
		//		+ ", QtyEntered/PriceActual=" + QtyEntered + "/" + PriceActual
		//		+ " -> " + conversion 
		//		+ " QtyOrdered/PriceEntered=" + QtyOrdered + "/" + PriceEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyRequiered);
		//	mTab.setValue("PriceEntered", PriceEntered);
		}
		//	QtyEntered changed - calculate QtyOrdered
		else if (mField.getColumnName().equals("QtyEntered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyEntered = (BigDecimal)value;
			QtyRequiered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyRequiered == null)
				QtyRequiered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyRequiered) != 0;
			log.fine("qty - UOM=" + C_UOM_To_ID 
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion 
				+ " QtyOrdered=" + QtyRequiered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyRequiered);
		}
		//	QtyOrdered changed - calculate QtyEntered
		else if (mField.getColumnName().equals("QtyOrdered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyRequiered = (BigDecimal)value;
			QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyRequiered);
			if (QtyEntered == null)
				QtyEntered = QtyRequiered;
			boolean conversion = QtyRequiered.compareTo(QtyEntered) != 0;
			log.fine("qty - UOM=" + C_UOM_To_ID 
				+ ", QtyOrdered=" + QtyRequiered
				+ " -> " + conversion 
				+ " QtyEntered=" + QtyEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyEntered", QtyEntered);
		}
		//
		setCalloutActive(false);
		return "";
	}	//	qty

}	//	CalloutOrder

