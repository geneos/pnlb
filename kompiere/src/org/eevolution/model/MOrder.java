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
package org.eevolution.model;

import java.sql.*;
import java.util.*;

import org.compiere.model.MProject;
import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.model.MInvoice;
import org.compiere.model.MInOut;


/**
 *  Order Model.
 * 	Please do not set DocStatus and C_DocType_ID directly. 
 * 	They are set in the process() method. 
 * 	Use DocAction and C_DocTypeTarget_ID instead.
 *
 *  @author Jorg Janke
 *  @version $Id: MOrder.java,v 1.101 2005/11/25 21:57:26 jjanke Exp $
 */
public class MOrder extends org.compiere.model.MOrder 
{
	
	
	
	/**************************************************************************
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  C_Order_ID    order to load, (0 create new order)
	 *  @param trxName trx name
	 */
	public MOrder(Properties ctx, int C_Order_ID, String trxName)
	{
		super (ctx, C_Order_ID, trxName);		
	}	//	MOrder
	
	/**************************************************************************
	 *  Project Constructor
	 *  @param  project Project to create Order from
	 * 	@param	DocSubTypeSO if SO DocType Target (default DocSubTypeSO_OnCredit)
	 */
	public MOrder (MProject project, boolean IsSOTrx, String DocSubTypeSO)
	{
		super(project, IsSOTrx, DocSubTypeSO);
	}
	

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MOrder (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MOrder

		
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		
		return super.beforeSave (newRecord);
	}	//	beforeSave
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success || newRecord)
			return success;
		
		
        //begin vpj-cd e-evolution 01/25/2005 CMPCS
		afterSaveSync("DocStatus");
		//end vpj-cd e-evolution 01/25/2005 CMPCS
		//
		return super.afterSave(newRecord,success);
	}	//	afterSave

	private void afterSaveSync (String columnName)
	{
		//begin vpj-cd e-evolution 01/25/2005 CMPCS
		if (is_ValueChanged("DocStatus") || is_ValueChanged("DatePromised") || is_ValueChanged("M_Warehouse_ID"))
		{	
			if(columnName.compareTo("DocStatus") == 0  || columnName.compareTo("DatePromised") == 0 || columnName.compareTo("M_Warehouse_ID") == 0)
			{	
			String sql = " UPDATE MPC_MRP m "
						+ "SET "  + columnName + " ="
						+ " (SELECT " + columnName
						+ " FROM C_Order o WHERE m.C_Order_ID=o.C_Order_ID) "
					    + " WHERE m.C_Order_ID = " + getC_Order_ID();
			int no = DB.executeUpdate(sql,get_TrxName());	
			log.fine(columnName + " MPC_MRP set DocStatus" + no);
			}
			if(columnName.compareTo("DocStatus") == 0)
				return;
		}			
		
	    // Regla para que el folio de la orden sea = a la factura
		/*String sql = "SELECT d.DocSubTypeSO , o.DocumentNo FROM C_Order o INNER JOIN C_DocType d ON (d.C_DocType_ID=o.C_DocType_ID) WHERE o.C_Order_ID=?";
		System.out.println("sql"+sql);
		try
		{
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		pstmt.setInt(1, getC_Order_ID());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
		{
			String DocSubTypeSO = rs.getString(1);
			if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS)
					|| DocSubTypeSO.equals(MOrder.DocSubTypeSO_Prepay)
					|| DocSubTypeSO.equals(MOrder.DocSubTypeSO_OnCredit))
			{
				//setDocumentNo(rs.getString(2));	
				//save(get_TrxName());
			}		
		}
		rs.close();
		pstmt.close();
		}
		catch (SQLException e)
		{
		log.log(Level.SEVERE, sql, e);
		return;
		}*/
		//end vpj-cd e-evolution 01/25/2005 CMPCS               	
	}	//	afterSaveSync
	
	/**
	 * 	Before Delete
	 *	@return true of it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		
		return super.beforeDelete();
	}	//	beforeDelete
	
	/**************************************************************************
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		
		String STATUS = super.completeIt();
		
		if (STATUS.equals(DocAction.STATUS_Completed) && isSOTrx())
		{
			try {
				/*
				 * 06-01-2011 Camarzana Mariano
				 * Cometado por el ticket 119 (Trellis) - requeria que no se imprima la OV
				 */
				//printOrder();
				
				Trx trx = Trx.get(get_TrxName(), false);
				trx.commit();
				
				printInOut();
				printInvoice();
			}
			catch (Exception e)
			{	
				e.printStackTrace();
				return DocAction.STATUS_Invalid;
			}
		}
		
		return STATUS;
	}	// completeIt
	
	public void printOrder()	{
		ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.ORDER, getC_Order_ID());
		re.print();
	}
	
	public void printInvoice()	{
		MInvoice[] invoices = getInvoices();
		for (int i =0; i<invoices.length;i++)
		{
			ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, invoices[i].getC_Invoice_ID());
			re.print();
		}
	}

	public void printInOut()	{
		MInOut[] ships = getShipments();
		for (int i =0; i<ships.length;i++)
		{
			ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.SHIPMENT, ships[i].getM_InOut_ID());
			re.print();
		}
	}
	
}	//	MOrder
