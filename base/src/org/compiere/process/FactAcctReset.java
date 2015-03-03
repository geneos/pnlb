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
import java.sql.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.report.*;
import org.compiere.util.*;

/**
 *	Accounting Fact Reset
 *	
 *  @author Jorg Janke
 *  @version $Id: FactAcctReset.java,v 1.12 2005/10/26 00:37:42 jjanke Exp $
 */
public class FactAcctReset extends SvrProcess
{
	/**	Client Parameter		*/
	private int		p_AD_Client_ID = 0;
	/** Table Parameter			*/
	private int		p_AD_Table_ID = 0;
	/**	Delete Parameter		*/
	private boolean	p_DeletePosting = false;
	
	private int		m_countReset = 0;
	private int		m_countDelete = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("AD_Table_ID"))
				p_AD_Table_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("DeletePosting"))
				p_DeletePosting = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		log.info("AD_Client_ID=" + p_AD_Client_ID 
			+ ", AD_Table_ID=" + p_AD_Table_ID + ", DeletePosting=" + p_DeletePosting);
		//
		String sql = "SELECT AD_Table_ID, TableName "
			+ "FROM AD_Table t "
			+ "WHERE t.IsView='N'";
		if (p_AD_Table_ID > 0)
			sql += " AND t.AD_Table_ID=" + p_AD_Table_ID;
		sql += " AND EXISTS (SELECT * FROM AD_Column c "
				+ "WHERE t.AD_Table_ID=c.AD_Table_ID AND c.ColumnName='Posted' AND c.IsActive='Y')";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int AD_Table_ID = rs.getInt(1);
				String TableName = rs.getString(2);
				if (p_DeletePosting)
					delete (TableName, AD_Table_ID);
				else
					reset (TableName);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		return "@Updated@ = " + m_countReset + ", @Deleted@ = " + m_countDelete;
	}	//	doIt

	/**
	 * 	Reset Accounting Table and update count
	 *	@param TableName table
	 */
	private void reset (String TableName)
	{
		String sql = "UPDATE " + TableName
			+ " SET Processing='N' WHERE AD_Client_ID=" + p_AD_Client_ID
			+ " AND (Processing<>'N' OR Processing IS NULL)";
		int unlocked = DB.executeUpdate(sql, get_TrxName());
		//
		sql = "UPDATE " + TableName
			+ " SET Posted='N' WHERE AD_Client_ID=" + p_AD_Client_ID
			+ " AND (Posted NOT IN ('Y','N') OR Posted IS NULL) AND Processed='Y'";
		int invalid = DB.executeUpdate(sql, get_TrxName());
		//
		if (unlocked + invalid != 0)
			log.fine(TableName + " - Unlocked=" + unlocked + " - Invalid=" + invalid);
		m_countReset += unlocked + invalid; 
	}	//	reset

	/**
	 * 	Delete Accounting Table where period status is open and update count.
	 *	@param AD_Table_ID table
	 */
	private void delete (String TableName, int AD_Table_ID)
	{
		reset(TableName);
		m_countReset = 0;
		//
		String docBaseType = null;
		if (AD_Table_ID == MInvoice.getTableId(MInvoice.Table_Name))
			docBaseType = "IN ('" + MPeriodControl.DOCBASETYPE_APInvoice 
				+ "','" + MPeriodControl.DOCBASETYPE_ARInvoice
				+ "','" + MPeriodControl.DOCBASETYPE_APCreditMemo
				+ "','" + MPeriodControl.DOCBASETYPE_ARCreditMemo
				+ "','" + MPeriodControl.DOCBASETYPE_ARProFormaInvoice + "')";
		else if (AD_Table_ID == MInOut.getTableId(MInOut.Table_Name))
			docBaseType = "IN ('" + MPeriodControl.DOCBASETYPE_MaterialDelivery
				+ "','" + MPeriodControl.DOCBASETYPE_MaterialReceipt + "')";
		else if (AD_Table_ID == MPayment.getTableId(MPayment.Table_Name))
			docBaseType = "IN ('" + MPeriodControl.DOCBASETYPE_APPayment
				+ "','" + MPeriodControl.DOCBASETYPE_ARReceipt + "')";
		else if (AD_Table_ID == MOrder.getTableId(MOrder.Table_Name))
			docBaseType = "IN ('" + MPeriodControl.DOCBASETYPE_SalesOrder
				+ "','" + MPeriodControl.DOCBASETYPE_PurchaseOrder + "')";
		else if (AD_Table_ID == MProjectIssue.getTableId(MProjectIssue.Table_Name))
			docBaseType = "= '" + MPeriodControl.DOCBASETYPE_ProjectIssue + "'";
		else if (AD_Table_ID == MBankStatement.getTableId(MBankStatement.Table_Name))
			docBaseType = "= '" + MPeriodControl.DOCBASETYPE_BankStatement + "'";
		else if (AD_Table_ID == MCash.getTableId(MCash.Table_Name))
			docBaseType = "= '" + MPeriodControl.DOCBASETYPE_CashJournal + "'";
		else if (AD_Table_ID == MAllocationHdr.getTableId(MAllocationHdr.Table_Name))
			docBaseType = "= '" + MPeriodControl.DOCBASETYPE_PaymentAllocation + "'";
		else if (AD_Table_ID == MJournal.getTableId(MJournal.Table_Name))
			docBaseType = "= '" + MPeriodControl.DOCBASETYPE_GLJournal + "'";
	//	else if (AD_Table_ID == M.Table_ID)
	//		docBaseType = "= '" + MPeriodControl.DOCBASETYPE_GLDocument + "'";
		else if (AD_Table_ID == MMovement.getTableId(MMovement.Table_Name))
			docBaseType = "= '" + MPeriodControl.DOCBASETYPE_MaterialMovement + "'";
		else if (AD_Table_ID == MRequisition.getTableId(MRequisition.Table_Name))
			docBaseType = "= '" + MPeriodControl.DOCBASETYPE_PurchaseRequisition + "'";
		else if (AD_Table_ID == MInventory.getTableId(MInventory.Table_Name))
			docBaseType = "= '" + MPeriodControl.DOCBASETYPE_MaterialPhysicalInventory + "'";
		else if (AD_Table_ID == X_M_Production.getTableId(X_M_Production.Table_Name))
			docBaseType = "= '" + MPeriodControl.DOCBASETYPE_MaterialProduction + "'";
		else if (AD_Table_ID == MMatchInv.getTableId(MMatchInv.Table_Name))
			docBaseType = "= '" + MPeriodControl.DOCBASETYPE_MatchInvoice + "'";
		else if (AD_Table_ID == MMatchPO.getTableId(MMatchPO.Table_Name))
			docBaseType = "= '" + MPeriodControl.DOCBASETYPE_MatchPO + "'";
		//
		if (docBaseType == null)
		{
			log.severe("Unknown DocBaseType for Table=" + TableName);
			docBaseType = "";
		}
		else
			docBaseType = " AND pc.DocBaseType " + docBaseType;
		
		//	Doc
		String sql = "UPDATE " + TableName + " doc"
			+ " SET Posted='N', Processing='N' "
			+ "WHERE AD_Client_ID=" + p_AD_Client_ID
			+ " AND (Posted<>'N' OR Posted IS NULL OR Processing<>'N' OR Processing IS NULL)"
			+ " AND EXISTS (SELECT * FROM C_PeriodControl pc"
				+ " INNER JOIN Fact_Acct fact ON (fact.C_Period_ID=pc.C_Period_ID) "
				+ "WHERE pc.PeriodStatus = 'O'" + docBaseType
				+ " AND fact.AD_Table_ID=" + AD_Table_ID
				+ " AND fact.Record_ID=doc." + TableName + "_ID)";
		int reset = DB.executeUpdate(sql, get_TrxName()); 
		//	Fact
		sql = "DELETE Fact_Acct fact "
			+ "WHERE AD_Client_ID=" + p_AD_Client_ID
			+ " AND AD_Table_ID=" + AD_Table_ID
			+ " AND EXISTS (SELECT * FROM C_PeriodControl pc "
				+ "WHERE pc.PeriodStatus = 'O'" + docBaseType
				+ " AND fact.C_Period_ID=pc.C_Period_ID)";
		int deleted = DB.executeUpdate(sql, get_TrxName());
		//	Balances
		FinBalance.updateBalanceClient(getCtx(), p_AD_Client_ID, true);		//	delete
		//
		log.info(TableName + "(" + AD_Table_ID + ") - Reset=" + reset + " - Deleted=" + deleted);
		m_countReset += reset;
		m_countDelete += deleted;
	}	//	delete

}	//	FactAcctReset
