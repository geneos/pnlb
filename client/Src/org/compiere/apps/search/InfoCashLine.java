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
package org.compiere.apps.search;

import java.awt.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import org.compiere.apps.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *  Info Payment
 *
 *  @author Jorg Janke
 *  @version  $Id: InfoCashLine.java,v 1.14 2005/07/18 03:49:06 jjanke Exp $
 */
public class InfoCashLine extends Info
{
	/**
	 *  Detail Protected Contructor
	 *  @param frame parent frame
	 *  @param modal modal
	 *  @param WindowNo window no
	 *  @param value query value
	 *  @param multiSelection multiple selections
	 *  @param whereClause whwre clause
	 */
	protected InfoCashLine(Frame frame, boolean modal, int WindowNo, String value,
		boolean multiSelection, String whereClause)
	{
		super (frame, modal, WindowNo, "cl", "C_CashLine_ID", multiSelection, whereClause);
		log.info( "InfoCashLine");
		setTitle(Msg.getMsg(Env.getCtx(), "InfoCashLine"));
		//
		try
		{
			statInit();
			p_loadedOK = initInfo ();
		}
		catch (Exception e)
		{
			return;
		}
		//
		int no = p_table.getRowCount();
		setStatusLine(Integer.toString(no) + " " + Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
		setStatusDB(Integer.toString(no));
		if (value != null && value.length() > 0)
		{
			fName.setValue(value);
			executeQuery();
		}
		//
		pack();
		//	Focus
		fName.requestFocus();
	}   //  InfoCashLine

	/** list of query columns           */
	private ArrayList 	m_queryColumns = new ArrayList();
	/** Table Name              */
	private String      m_tableName;
	/** Key Column Name         */
	private String      m_keyColumn;

	//  Static Info
	private CLabel lName = new CLabel(Msg.translate(Env.getCtx(), "Name"));
	private CTextField fName = new CTextField(10);
	private CLabel lCashBook_ID = new CLabel(Msg.translate(Env.getCtx(), "C_CashBook_ID"));
	private VLookup fCashBook_ID;
//	private CLabel lOrg_ID = new CLabel(Msg.translate(Env.getCtx(), "AD_Org_ID"));
//	private VLookup fOrg_ID;
	private CLabel lInvoice_ID = new CLabel(Msg.translate(Env.getCtx(), "C_Invoice_ID"));
	private VLookup fInvoice_ID;
//	private CLabel lCharge_ID = new CLabel(Msg.translate(Env.getCtx(), "C_Charge_ID"));
//	private VLookup fCharge_ID;
	private CLabel lBankAccount_ID = new CLabel(Msg.translate(Env.getCtx(), "C_BankAccount_ID"));
	private VLookup fBankAccount_ID;
	private CCheckBox cbAbsolute = new CCheckBox (Msg.translate(Env.getCtx(), "AbsoluteAmt"));
	//
	private CLabel lDateFrom = new CLabel(Msg.translate(Env.getCtx(), "StatementDate"));
	private VDate fDateFrom = new VDate("DateFrom", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateFrom"));
	private CLabel lDateTo = new CLabel("-");
	private VDate fDateTo = new VDate("DateTo", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateTo"));
	private CLabel lAmtFrom = new CLabel(Msg.translate(Env.getCtx(), "Amount"));
	private VNumber fAmtFrom = new VNumber("AmtFrom", false, false, true, DisplayType.Amount, Msg.translate(Env.getCtx(), "AmtFrom"));
	private CLabel lAmtTo = new CLabel("-");
	private VNumber fAmtTo = new VNumber("AmtTo", false, false, true, DisplayType.Amount, Msg.translate(Env.getCtx(), "AmtTo"));

	/**  Array of Column Info    */
	private static final Info_Column[] s_cashLayout = {
		new Info_Column(" ", "cl.C_CashLine_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "C_CashBook_ID"),
			"(SELECT cb.Name FROM C_CashBook cb WHERE cb.C_CashBook_ID=c.C_CashBook_ID)", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Name"),
			"c.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "StatementDate"),
			"c.StatementDate", Timestamp.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Line"),
			"cl.Line", Integer.class),
	//	new Info_Column(Msg.translate(Env.getCtx(), "C_Currency_ID"),
	//		"(SELECT ISO_Code FROM C_Currency c WHERE c.C_Currency_ID=cl.C_Currency_ID)", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Amount"),
			"cl.Amount",  BigDecimal.class, true, true, null),
		//
		new Info_Column(Msg.translate(Env.getCtx(), "C_Invoice_ID"),
			"(SELECT i.DocumentNo||'_'||" + DB.TO_CHAR("i.DateInvoiced",DisplayType.Date,Env.getAD_Language(Env.getCtx()))
				+ "||'_'||" + DB.TO_CHAR("i.GrandTotal",DisplayType.Amount,Env.getAD_Language(Env.getCtx()))
				+ " FROM C_Invoice i WHERE i.C_Invoice_ID=cl.C_Invoice_ID)", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "C_BankAccount_ID"),
			"(SELECT b.Name||' '||ba.AccountNo FROM C_Bank b, C_BankAccount ba WHERE b.C_Bank_ID=ba.C_Bank_ID AND ba.C_BankAccount_ID=cl.C_BankAccount_ID)", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "C_Charge_ID"),
			"(SELECT ca.Name FROM C_Charge ca WHERE ca.C_Charge_ID=cl.C_Charge_ID)", String.class),
		//
		new Info_Column(Msg.translate(Env.getCtx(), "DiscountAmt"),
			"cl.DiscountAmt",  BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "WriteOffAmt"),
			"cl.WriteOffAmt",  BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Description"),
			"cl.Description", String.class)
	};

	/**
	 *	Static Setup - add fields to parameterPanel
	 *  @throws Exception if Lookups cannot be created
	 */
	private void statInit() throws Exception
	{
		lName.setLabelFor(fName);
		fName.setBackground(CompierePLAF.getInfoBackground());
		fName.addActionListener(this);
		//
	//	fOrg_ID = new VLookup("AD_Org_ID", false, false, true,
	//		MLookupFactory.create(Env.getCtx(), 3486, m_WindowNo, DisplayType.TableDir, false),
	//		DisplayType.TableDir, m_WindowNo);
	//	lOrg_ID.setLabelFor(fOrg_ID);
	//	fOrg_ID.setBackground(CompierePLAF.getInfoBackground());
		//	5249 - C_Cash.C_CashBook_ID
		fCashBook_ID = new VLookup("C_CashBook_ID", false, false, true,
			MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, 5249, DisplayType.TableDir));
		lCashBook_ID.setLabelFor(fCashBook_ID);
		fCashBook_ID.setBackground(CompierePLAF.getInfoBackground());
		//	5354 - C_CashLine.C_Invoice_ID
		fInvoice_ID = new VLookup("C_Invoice_ID", false, false, true,
			MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, 5354, DisplayType.Search));
		lInvoice_ID.setLabelFor(fInvoice_ID);
		fInvoice_ID.setBackground(CompierePLAF.getInfoBackground());
		//	5295 - C_CashLine.C_BankAccount_ID
		fBankAccount_ID = new VLookup("C_BankAccount_ID", false, false, true,
			MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, 5295, DisplayType.TableDir));
		lBankAccount_ID.setLabelFor(fBankAccount_ID);
		fBankAccount_ID.setBackground(CompierePLAF.getInfoBackground());
		//	5296 - C_CashLine.C_Charge_ID
		//	5291 - C_CashLine.C_Cash_ID
		//
		lDateFrom.setLabelFor(fDateFrom);
		fDateFrom.setBackground(CompierePLAF.getInfoBackground());
		fDateFrom.setToolTipText(Msg.translate(Env.getCtx(), "DateFrom"));
		lDateTo.setLabelFor(fDateTo);
		fDateTo.setBackground(CompierePLAF.getInfoBackground());
		fDateTo.setToolTipText(Msg.translate(Env.getCtx(), "DateTo"));
		lAmtFrom.setLabelFor(fAmtFrom);
		fAmtFrom.setBackground(CompierePLAF.getInfoBackground());
		fAmtFrom.setToolTipText(Msg.translate(Env.getCtx(), "AmtFrom"));
		lAmtTo.setLabelFor(fAmtTo);
		fAmtTo.setBackground(CompierePLAF.getInfoBackground());
		fAmtTo.setToolTipText(Msg.translate(Env.getCtx(), "AmtTo"));
		//
		parameterPanel.setLayout(new ALayout());
		//  First Row
		parameterPanel.add(lCashBook_ID, new ALayoutConstraint(0,0));
		parameterPanel.add(fCashBook_ID, null);
		parameterPanel.add(lName, null);
		parameterPanel.add(fName, null);
		parameterPanel.add(cbAbsolute, new ALayoutConstraint(0,5));
		//  2nd Row
		parameterPanel.add(lInvoice_ID, new ALayoutConstraint(1,0));
		parameterPanel.add(fInvoice_ID, null);
		parameterPanel.add(lDateFrom, null);
		parameterPanel.add(fDateFrom, null);
		parameterPanel.add(lDateTo, null);
		parameterPanel.add(fDateTo, null);
		//  3rd Row
		parameterPanel.add(lBankAccount_ID, new ALayoutConstraint(2,0));
		parameterPanel.add(fBankAccount_ID, null);
		parameterPanel.add(lAmtFrom, null);
		parameterPanel.add(fAmtFrom, null);
		parameterPanel.add(lAmtTo, null);
		parameterPanel.add(fAmtTo, null);
	//	parameterPanel.add(lOrg_ID, null);
	//	parameterPanel.add(fOrg_ID, null);
	}	//	statInit

	/**
	 *	General Init
	 *	@return true, if success
	 */
	private boolean initInfo ()
	{
		//  prepare table
		StringBuffer where = new StringBuffer("cl.IsActive='Y'");
		if (p_whereClause.length() > 0)
			where.append(" AND ").append(Util.replace(p_whereClause, "C_CashLine.", "cl."));
		prepareTable (s_cashLayout,
			"C_CashLine cl INNER JOIN C_Cash c ON (cl.C_Cash_ID=c.C_Cash_ID)",
			where.toString(),
			"2,3,cl.Line");

		return true;
	}	//	initInfo

	
	/**************************************************************************
	 *	Construct SQL Where Clause and define parameters
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return sql where clause
	 */
	String getSQLWhere()
	{
		StringBuffer sql = new StringBuffer();
		if (fName.getText().length() > 0)
			sql.append(" AND UPPER(c.Name) LIKE ?");
		//
		if (fCashBook_ID.getValue() != null)
			sql.append(" AND c.C_CashBook_ID=?");
		//
		if (fInvoice_ID.getValue() != null)
			sql.append(" AND cl.C_Invoice_ID=?");
		//
		if (fDateFrom.getValue() != null || fDateTo.getValue() != null)
		{
			Timestamp from = (Timestamp)fDateFrom.getValue();
			Timestamp to = (Timestamp)fDateTo.getValue();
			if (from == null && to != null)
				sql.append(" AND TRUNC(c.StatementDate) <= ?");
			else if (from != null && to == null)
				sql.append(" AND TRUNC(c.StatementDate) >= ?");
			else if (from != null && to != null)
				sql.append(" AND TRUNC(c.StatementDate) BETWEEN ? AND ?");
		}
		//
		if (fAmtFrom.getValue() != null || fAmtTo.getValue() != null)
		{
			BigDecimal from = (BigDecimal)fAmtFrom.getValue();
			BigDecimal to = (BigDecimal)fAmtTo.getValue();
			if (cbAbsolute.isSelected())
				sql.append(" AND ABS(cl.Amount)");
			else
				sql.append(" AND cl.Amount");
			//
			if (from == null && to != null)
				sql.append(" <=?");
			else if (from != null && to == null)
				sql.append(" >=?");
			else if (from != null && to != null)
			{
				if (from.compareTo(to) == 0)
					sql.append(" =?");
				else
					sql.append(" BETWEEN ? AND ?");
			}
		}

		log.fine(sql.toString());
		return sql.toString();
	}	//	getSQLWhere

	/**
	 *  Set Parameters for Query.
	 *  (as defined in getSQLWhere)
	 *  @param pstmt statement
	 *  @param forCount for counting records
	 *  @throws SQLException
	 */
	void setParameters(PreparedStatement pstmt, boolean forCount) throws SQLException
	{
		int index = 1;
		if (fName.getText().length() > 0)
			pstmt.setString(index++, getSQLText(fName));
		//
		if (fCashBook_ID.getValue() != null)
		{
			Integer cb = (Integer)fCashBook_ID.getValue();
			pstmt.setInt(index++, cb.intValue());
			log.fine("CashBook=" + cb);
		}
		//
		if (fInvoice_ID.getValue() != null)
		{
			Integer i = (Integer)fInvoice_ID.getValue();
			pstmt.setInt(index++, i.intValue());
			log.fine("Invoice=" + i);
		}
		//
		if (fDateFrom.getValue() != null || fDateTo.getValue() != null)
		{
			Timestamp from = (Timestamp)fDateFrom.getValue();
			Timestamp to = (Timestamp)fDateTo.getValue();
			log.fine("Date From=" + from + ", To=" + to);
			if (from == null && to != null)
				pstmt.setTimestamp(index++, to);
			else if (from != null && to == null)
				pstmt.setTimestamp(index++, from);
			else if (from != null && to != null)
			{
				pstmt.setTimestamp(index++, from);
				pstmt.setTimestamp(index++, to);
			}
		}
		//
		if (fAmtFrom.getValue() != null || fAmtTo.getValue() != null)
		{
			BigDecimal from = (BigDecimal)fAmtFrom.getValue();
			BigDecimal to = (BigDecimal)fAmtTo.getValue();
			if (cbAbsolute.isSelected())
			{
				if (from != null)
					from = from.abs();
				if (to != null)
					to = to.abs();
			}
			log.fine("Amt From=" + from + ", To=" + to + ", Absolute=" + cbAbsolute.isSelected());
			if (from == null && to != null)
				pstmt.setBigDecimal(index++, to);
			else if (from != null && to == null)
				pstmt.setBigDecimal(index++, from);
			else if (from != null && to != null)
			{
				if (from.compareTo(to) == 0)
					pstmt.setBigDecimal(index++, from);
				else
				{
					pstmt.setBigDecimal(index++, from);
					pstmt.setBigDecimal(index++, to);
				}
			}
		}
	}   //  setParameters

	/**
	 *  Get SQL WHERE parameter
	 *  @param f field
	 *  @return Upper case text with % at the end
	 */
	private String getSQLText (CTextField f)
	{
		String s = f.getText().toUpperCase();
		if (!s.endsWith("%"))
			s += "%";
		log.fine( "String=" + s);
		return s;
	}   //  getSQLText
}   //  InfoCashLine
