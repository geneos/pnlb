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

import java.util.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.util.QueryDB;
 
/**
 *  Order Model.
 * 	Please do not set DocStatus and C_DocType_ID directly. 
 * 	They are set in the process() method. 
 * 	Use DocAction and C_DocTypeTarget_ID instead.
 *
 *  @author Jorg Janke
 *  @version $Id: MOrder.java,v 1.40 2004/04/13 04:19:30 jjanke Exp $
 */
public class MMPCProductBOM extends X_MPC_Product_BOM
{
    
    
    private static CLogger log = CLogger.getCLogger(MMPCProductBOM.class);     
    
	/**
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  C_Order_ID    order to load, (0 create new order)
	 */
	public MMPCProductBOM(Properties ctx, int MPC_Product_BOM_ID,String trxName)
	{
		super (ctx,  MPC_Product_BOM_ID,trxName);
		//  New
		if ( MPC_Product_BOM_ID == 0)
		{
			//setDocStatus(DOCSTATUS_Drafted);
			//setDocAction (DOCACTION_Prepare);
			//
			//setDeliveryRule (DELIVERYRULE_Availability);
			//setFreightCostRule (FREIGHTCOSTRULE_FreightIncluded);
			//setInvoiceRule (INVOICERULE_Immediate);
			//setPaymentRule(PAYMENTRULE_OnCredit);
			//setPriorityRule (PRIORITYRULE_Medium);
			//setDeliveryViaRule (DELIVERYVIARULE_Pickup);
			//
			//setIsDiscountPrinted (false);
			//setIsSelected (false);
			//setIsTaxIncluded (false);
			//setIsSOTrx (true);
			///setIsDropShip(false);
			//setSendEMail (false);
			//
			//setIsApproved(false);
			//setIsPrinted(false);
			//setIsCreditApproved(false);
			//setIsDelivered(false);
			//setIsInvoiced(false);
			//setIsTransferred(false);
			//setIsSelfService(false);
			//
			//setProcessed(false);
			//setProcessing(false);
			//setPosted(false);

			//setDateAcct (new Timestamp(System.currentTimeMillis()));
			//setDatePromised (new Timestamp(System.currentTimeMillis()));
			//setDateOrdered (new Timestamp(System.currentTimeMillis()));

			//setFreightAmt (Env.ZERO);
			//setChargeAmt (Env.ZERO);
			//setTotalLines (Env.ZERO);
			//setGrandTotal (Env.ZERO);
		}
	}	//	MOrder

	
	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MMPCProductBOM(Properties ctx, ResultSet rs,String trxName)
	{
		super (ctx, rs,trxName);
	}	//	MOrder
        
        
        
        

	/**
	 * 	Overwrite Client/Org if required
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg


	/**
	 * 	Copy Lines From other Order
	 *	@param order order
	 *	@return number of lines copied
	 */
	public int copyLinesFrom (MMPCProductBOM bom)
	{
		if (bom == null)
			return 0;
		MMPCProductBOMLine[] fromLines = bom.getLines();
		int count = 0;
		for (int i = 0; i < fromLines.length; i++)
		{
			MMPCProductBOMLine line = new MMPCProductBOMLine (this);
			PO.copyValues(fromLines[i], line, getAD_Client_ID(), getAD_Org_ID());
			line.setMPC_Product_BOM_ID(getMPC_Product_BOM_ID());
			//line.setOrder(bom);
			line.setMPC_Product_BOMLine_ID(0);
			//
			//line.setQtyDelivered(Env.ZERO);
			//line.setQtyInvoiced(Env.ZERO);
			//line.setDateDelivered(null);
			//line.setDateInvoiced(null);
			//line.setRef_OrderLine_ID(0);
			//line.setTax();
			if (line.save(get_TrxName()))
				count++;
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE,"copyLinesFrom - Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom

	/*************************************************************************/

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MMPCProductBOM[")
			.append(get_ID()).append("-").append(getDocumentNo())
			.append ("]");
		return sb.toString ();
	}	//	toString


	
	/**************************************************************************
	 * 	Get Invoices of Order
	 * 	@return invoices
	 */
	public MMPCProductBOMLine[] getLines()
	{
		return getLines (getMPC_Product_BOM_ID());
	}	//	getLines

	/**
	 * 	Get Invoices of Order
	 * 	@param C_Order_ID id
	 * 	@return invoices
	 */
	public  MMPCProductBOMLine[] getLines (int MPC_Product_BOM_ID)
	{
		ArrayList list = new ArrayList();
                
                QueryDB query = new QueryDB("org.compiere.model.X_MPC_Product_BOMLine");
                String filter = "MPC_Product_BOM_ID = " + MPC_Product_BOM_ID;
                List results = query.execute(filter);
                Iterator select = results.iterator();
                while (select.hasNext())
                {
                	//System.out.println("linea de product bom ************ ");
                   X_MPC_Product_BOMLine bomline = (X_MPC_Product_BOMLine) select.next();
                   //System.out.println("linea de product bom ************ " + bomline.getMPC_Product_BOMLine_ID());
                   list.add(new MMPCProductBOMLine(getCtx(), bomline.getMPC_Product_BOMLine_ID(),"MPC_Product_BOM_Line"));
                   //list.add(bomline);
                }
                MMPCProductBOMLine[] retValue = new MMPCProductBOMLine[list.size()];
                list.toArray(retValue);
                return retValue; 

	}	//	getLines


	/**
	 * 	Create new Order by copying
	 * 	@param ctx context
	 *	@param C_Order_ID invoice
	 * 	@param dateDoc date of the document date
	 *	@return Order
	 */
	public static MMPCProductBOM copyFrom (Properties ctx, int MPC_Product_BOM_ID, Timestamp dateDoc)
	{
		MMPCProductBOM from = new MMPCProductBOM (ctx, MPC_Product_BOM_ID,"MPC_Product_BOM");
		if (from.getMPC_Product_BOM_ID() == 0)
			throw new IllegalArgumentException ("From Invoice not found C_Invoice_ID=" + MPC_Product_BOM_ID);
		//
		MMPCProductBOM to = new MMPCProductBOM (ctx, 0,"MPC_Product_BOM");
		PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.setMPC_Product_BOM_ID(0);
		//to.set_ValueNoCheck ("DocumentNo", null);
		//
		//to.setDocStatus (DOCSTATUS_Drafted);		//	Draft
		//to.setDocAction(DOCACTION_Prepare);
		//to.setC_DocTypeTarget_ID(to.getC_DocType_ID());
		//to.setC_DocType_ID(0);
		//to.setIsSelected (false);
		//to.setDateOrdered (dateDoc);
		//to.setDateAcct (dateDoc);
		//to.setDatePromised (dateDoc);
		//
		//to.setIsApproved (false);
		//to.setIsCreditApproved(false);
		//to.setC_Payment_ID(0);
		//to.setC_CashLine_ID(0);
		//	Amounts are updated by trigger when adding lines
		//to.setGrandTotal(Env.ZERO);
		//to.setTotalLines(Env.ZERO);
		//
		//to.setIsDelivered(false);
		//to.setIsInvoiced(false);
		//to.setIsSelfService(false);
		//to.setDatePrinted(null);
		//to.setIsPrinted (false);
		//to.setIsTransferred (false);
		//to.setPosted (false);
		//to.setProcessed (false);
		//
		if (!to.save())
			throw new IllegalStateException("Could not create Order");

		if (to.copyLinesFrom(from) == 0)
			throw new IllegalStateException("Could not create Order Lines");

		return to;
	}	//	copyFrom
        
       public static int getBOMSearchKey(int M_Product_ID)
       {
           
                int MPC_Product_BOM_ID = 0;
                int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
           
                MProduct product = new MProduct(Env.getCtx(), M_Product_ID, "M_Product");
                String sql = "SELECT pb.MPC_Product_BOM_ID FROM MPC_Product_BOM  pb WHERE pb.Value = ? AND pb.AD_Client_ID = ?";	
                
                
                
                PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setString(1, product.getValue());	
			pstmt.setInt(2, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{	
				MPC_Product_BOM_ID = rs.getInt(1);
				break;
			}	
			rs.close();
			pstmt.close();
			pstmt = null;
			return MPC_Product_BOM_ID;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"getProductPlanning", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}	
           
                return 0;
       }

}	//	MMPCProductBOM
