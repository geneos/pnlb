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

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import org.compiere.apps.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *  Info Invoice
 *
 *  @author Jorg Janke
 *  @version  $Id: InfoInvoice.java,v 1.37 2005/12/09 05:17:54 jjanke Exp $
 */
public class InfoInvoiceNew extends Info
{
	/**
	 *  Detail Protected Contructor
	 *
	 *  @param frame parent frame
	 *  @param modal modal
	 *  @param WindowNo window no
	 *  @param value query value
	 *  @param multiSelection multiple selections
	 *  @param whereClause where clause
	 */
	protected InfoInvoiceNew(Frame frame, boolean modal, int WindowNo, String value,
		boolean multiSelection, String whereClause)
	{
		//super (frame, modal, WindowNo, "i", "C_Invoice_ID", multiSelection, whereClause);
		super (frame, modal, WindowNo, "i", "C_Invoice_ID", false, whereClause);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoInvoice"));
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
			fDocumentNo.setValue(value);
			executeQuery();
		}
		//
		pack();
		//	Focus
		fDocumentNo.requestFocus();
	}   //  InfoInvoice

	/**  String Array of Column Info    */
	private Info_Column[] m_generalLayout;
	/** list of query columns           */
	private ArrayList 	m_queryColumns = new ArrayList();
	/** Table Name              */
	private String      m_tableName;
	/** Key Column Name         */
	private String      m_keyColumn;

	//  Static Info
	private CLabel lDocumentNo = new CLabel(Msg.translate(Env.getCtx(), "DocumentNo"));
	private CTextField fDocumentNo = new CTextField(10);
	private CLabel lDescription = new CLabel(Msg.translate(Env.getCtx(), "Description"));
	private CTextField fDescription = new CTextField(10);
//	private CLabel lPOReference = new CLabel(Msg.translate(Env.getCtx(), "POReference"));
//	private CTextField fPOReference = new CTextField(10);
	//
//	private CLabel lOrg_ID = new CLabel(Msg.translate(Env.getCtx(), "AD_Org_ID"));
//	private VLookup fOrg_ID;
	private CLabel lBPartner_ID = new CLabel(Msg.translate(Env.getCtx(), "BPartner"));
	private VLookup fBPartner_ID;
	private CLabel lOrder_ID = new CLabel(Msg.translate(Env.getCtx(), "C_Order_ID"));
	private VLookup fOrder_ID;
	private VCheckBox fIsPaid = new VCheckBox ("IsPaid", false, false, true, Msg.translate(Env.getCtx(), "IsPaid"), "", false);
	private VCheckBox fIsSOTrx = new VCheckBox ("IsSOTrx", false, false, true, Msg.translate(Env.getCtx(), "IsSOTrx"), "", false);
	//
	private CLabel lDateFrom = new CLabel(Msg.translate(Env.getCtx(), "DateInvoiced"));
	private VDate fDateFrom = new VDate("DateFrom", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateFrom"));
	private CLabel lDateTo = new CLabel("-");
	private VDate fDateTo = new VDate("DateTo", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateTo"));
	private CLabel lAmtFrom = new CLabel(Msg.translate(Env.getCtx(), "GrandTotal"));
	private VNumber fAmtFrom = new VNumber("AmtFrom", false, false, true, DisplayType.Amount, Msg.translate(Env.getCtx(), "AmtFrom"));
	private CLabel lAmtTo = new CLabel("-");
	private VNumber fAmtTo = new VNumber("AmtTo", false, false, true, DisplayType.Amount, Msg.translate(Env.getCtx(), "AmtTo"));

	/**  Array of Column Info    */
	private static final Info_Column[] s_invoiceLayout = {
		new Info_Column(" ", "i.C_Invoice_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "C_BPartner_ID"), "(SELECT Name FROM C_BPartner bp WHERE bp.C_BPartner_ID=i.C_BPartner_ID)", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "DateInvoiced"), "i.DateInvoiced", Timestamp.class),
		new Info_Column(Msg.translate(Env.getCtx(), "DocumentNo"), "i.DocumentNo", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "C_Currency_ID"), "(SELECT ISO_Code FROM C_Currency c WHERE c.C_Currency_ID=i.C_Currency_ID)", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "GrandTotal"), "i.GrandTotal",  BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "ConvertedAmount"), "i.GrandTotal*i.COTIZACION", BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "OpenAmt"), "invoiceOpen(C_Invoice_ID,C_InvoicePaySchedule_ID)", BigDecimal.class, true, true, null),
		new Info_Column(Msg.translate(Env.getCtx(), "Total"), "invoiceOpen(C_Invoice_ID,C_InvoicePaySchedule_ID)", BigDecimal.class, false, true, null),
		new Info_Column(Msg.translate(Env.getCtx(), "IsPaid"), "i.IsPaid", Boolean.class),
		new Info_Column(Msg.translate(Env.getCtx(), "IsSOTrx"), "i.IsSOTrx", Boolean.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Description"), "i.Description", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "POReference"), "i.POReference", String.class),
		new Info_Column("", "''", KeyNamePair.class, "i.C_InvoicePaySchedule_ID")
	};
	private static int INDEX_PAYSCHEDULE = s_invoiceLayout.length - 1;	//	last item

	/**
	 *	Static Setup - add fields to parameterPanel
	 *	@throws Exception
	 */
	private void statInit() throws Exception
	{
		lDocumentNo.setLabelFor(fDocumentNo);
		fDocumentNo.setBackground(CompierePLAF.getInfoBackground());
		fDocumentNo.addActionListener(this);
		lDescription.setLabelFor(fDescription);
		fDescription.setBackground(CompierePLAF.getInfoBackground());
		fDescription.addActionListener(this);
	//	lPOReference.setLabelFor(lPOReference);
	//	fPOReference.setBackground(CompierePLAF.getInfoBackground());
	//	fPOReference.addActionListener(this);
		fIsPaid.setSelected(false);
		fIsPaid.addActionListener(this);
		fIsSOTrx.setSelected(!"N".equals(Env.getContext(Env.getCtx(), p_WindowNo, "IsSOTrx")));
		fIsSOTrx.addActionListener(this);
		//
	//	fOrg_ID = new VLookup("AD_Org_ID", false, false, true,
	//		MLookupFactory.create(Env.getCtx(), 3486, m_WindowNo, DisplayType.TableDir, false),
	//		DisplayType.TableDir, m_WindowNo);
	//	lOrg_ID.setLabelFor(fOrg_ID);
	//	fOrg_ID.setBackground(CompierePLAF.getInfoBackground());
		//	C_Invoice.C_BPartner_ID
		fBPartner_ID = new VLookup("C_BPartner_ID", false, false, true,
			MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, 3499, DisplayType.Search));
		lBPartner_ID.setLabelFor(fBPartner_ID);
		fBPartner_ID.setBackground(CompierePLAF.getInfoBackground());
		//	C_Invoice.C_Order_ID
		fOrder_ID = new VLookup("C_Order_ID", false, false, true,
			MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, 4247, DisplayType.Search));
		lOrder_ID.setLabelFor(fOrder_ID);
		fOrder_ID.setBackground(CompierePLAF.getInfoBackground());
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
		parameterPanel.add(lDocumentNo, new ALayoutConstraint(0,0));
		parameterPanel.add(fDocumentNo, null);
		parameterPanel.add(lBPartner_ID, null);
		parameterPanel.add(fBPartner_ID, null);
		parameterPanel.add(fIsSOTrx, new ALayoutConstraint(0,5));
		parameterPanel.add(fIsPaid, null);
		//  2nd Row
		parameterPanel.add(lDescription, new ALayoutConstraint(1,0));
		parameterPanel.add(fDescription, null);
		parameterPanel.add(lDateFrom, null);
		parameterPanel.add(fDateFrom, null);
		parameterPanel.add(lDateTo, null);
		parameterPanel.add(fDateTo, null);
		//  3rd Row
		parameterPanel.add(lOrder_ID, new ALayoutConstraint(2,0));
		parameterPanel.add(fOrder_ID, null);
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
		//  Set Defaults
		String bp = Env.getContext(Env.getCtx(), p_WindowNo, "C_BPartner_ID");
		if (bp != null && bp.length() != 0)
			fBPartner_ID.setValue(new Integer(bp));

		//  prepare table
		StringBuffer where = new StringBuffer("i.IsActive='Y'");
		if (p_whereClause.length() > 0)
			where.append(" AND ").append(Util.replace(p_whereClause, "C_Invoice.", "i."));
		prepareTable(s_invoiceLayout,
			" C_Invoice_v i",   //  corrected for CM
			where.toString(),
			"2,3,4");

		p_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		p_table.setMultiSelection(false);
		
	//	MAllocationLine.setIsPaid(Env.getCtx(), 0, null);
		return true;
	}	//	initInfo

	
	/**************************************************************************
	 *	Construct SQL Where Clause and define parameters.
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return sql
	 */
	String getSQLWhere()
	{
		StringBuffer sql = new StringBuffer();
		if (fDocumentNo.getText().length() > 0)
			sql.append(" AND UPPER(i.DocumentNo) LIKE ?");
		if (fDescription.getText().length() > 0)
			sql.append(" AND UPPER(i.Description) LIKE ?");
	//	if (fPOReference.getText().length() > 0)
	//		sql.append(" AND UPPER(i.POReference) LIKE ?");
		//
		if (fBPartner_ID.getValue() != null)
			sql.append(" AND i.C_BPartner_ID=?");
		//
		if (fOrder_ID.getValue() != null)
			sql.append(" AND i.C_Order_ID=?");
		//
		if (fDateFrom.getValue() != null || fDateTo.getValue() != null)
		{
			Timestamp from = (Timestamp)fDateFrom.getValue();
			Timestamp to = (Timestamp)fDateTo.getValue();
			if (from == null && to != null)
				sql.append(" AND TRUNC(i.DateInvoiced) <= ?");
			else if (from != null && to == null)
				sql.append(" AND TRUNC(i.DateInvoiced) >= ?");
			else if (from != null && to != null)
				sql.append(" AND TRUNC(i.DateInvoiced) BETWEEN ? AND ?");
		}
		//
		if (fAmtFrom.getValue() != null || fAmtTo.getValue() != null)
		{
			BigDecimal from = (BigDecimal)fAmtFrom.getValue();
			BigDecimal to = (BigDecimal)fAmtTo.getValue();
			if (from == null && to != null)
				sql.append(" AND i.GrandTotal <= ?");
			else if (from != null && to == null)
				sql.append(" AND i.GrandTotal >= ?");
			else if (from != null && to != null)
				sql.append(" AND i.GrandTotal BETWEEN ? AND ?");
		}
		//
		sql.append(" AND i.IsPaid=? AND i.IsSOTrx=?");

	//	log.fine( "InfoInvoice.setWhereClause", sql.toString());
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
		if (fDocumentNo.getText().length() > 0)
			pstmt.setString(index++, getSQLText(fDocumentNo));
		if (fDescription.getText().length() > 0)
			pstmt.setString(index++, getSQLText(fDescription));
	//	if (fPOReference.getText().length() > 0)
	//		pstmt.setString(index++, getSQLText(fPOReference));
		//
		if (fBPartner_ID.getValue() != null)
		{
			Integer bp = (Integer)fBPartner_ID.getValue();
			pstmt.setInt(index++, bp.intValue());
			log.fine("BPartner=" + bp);
		}
		//
		if (fOrder_ID.getValue() != null)
		{
			Integer order = (Integer)fOrder_ID.getValue();
			pstmt.setInt(index++, order.intValue());
			log.fine("Order=" + order);
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
			log.fine("Amt From=" + from + ", To=" + to);
			if (from == null && to != null)
				pstmt.setBigDecimal(index++, to);
			else if (from != null && to == null)
				pstmt.setBigDecimal(index++, from);
			else if (from != null && to != null)
			{
				pstmt.setBigDecimal(index++, from);
				pstmt.setBigDecimal(index++, to);
			}
		}
		pstmt.setString(index++, fIsPaid.isSelected() ? "Y" : "N");
		pstmt.setString(index++, fIsSOTrx.isSelected() ? "Y" : "N");
	}   //  setParameters

	/**
	 *  Get SQL WHERE parameter
	 *  @param f field
	 *  @return sql
	 */
	private String getSQLText (CTextField f)
	{
		String s = f.getText().toUpperCase();
		if (!s.endsWith("%"))
			s += "%";
		log.fine( "String=" + s);
		return s;
	}   //  getSQLText
	
	/**
	 *	Zoom
	 */
	void zoom()
	{
		log.info( "InfoInvoice.zoom");
		Integer C_Invoice_ID = getSelectedRowKey();
		if (C_Invoice_ID == null)
			return;
		MQuery query = new MQuery("C_Invoice");
		query.addRestriction("C_Invoice_ID", MQuery.EQUAL, C_Invoice_ID);
		query.setRecordCount(1);
		int AD_WindowNo = getAD_Window_ID("C_Invoice", fIsSOTrx.isSelected());
		zoom (AD_WindowNo, query);
	}	//	zoom

	/**
	 *	Has Zoom
	 *  @return true
	 */
	boolean hasZoom()
	{
		return true;
	}	//	hasZoom

	/**
	 *	Save Selection Settings
	 */
	void saveSelectionDetail()
	{
		if (checkTotals())
		{
			int[] rows = p_table.getSelectedRows();
			Trx trx = Trx.get("payAllocation",true);
			try{
				
				MPayment payment = new MPayment(Env.getCtx(),Env.getContextAsInt(Env.getCtx(),p_WindowNo, "C_Payment_ID"),trx.getTrxName());
				BigDecimal monto = payment.getPayAmt();
				BigDecimal disponible = BigDecimal.ZERO;
				
				if (rows.length>0)
				{
					disponible = getMontoDisponible(BigDecimal.ZERO,(BigDecimal)p_table.getValueAt(rows[0], 8),payment.getPayAmt(),payment.getAmountAllocate());
					if (disponible.compareTo(BigDecimal.ZERO)<1)
					{
						JOptionPane.showMessageDialog(null, "La Asignación está Completa.","Información - Asignación",JOptionPane.INFORMATION_MESSAGE);
						trx.rollback();
						return;
					}
					
					monto = disponible;
				}
					
				int i=1;
				while (i < rows.length && getMontoDisponible(monto,(BigDecimal)p_table.getValueAt(rows[i], 8),payment.getPayAmt(),payment.getAmountAllocate()).compareTo(BigDecimal.ZERO)==1)
				{
					disponible = getMontoDisponible(monto,(BigDecimal)p_table.getValueAt(rows[i], 8),payment.getPayAmt(),payment.getAmountAllocate()); 
					
					Integer idColumn = ((IDColumn)p_table.getValueAt(rows[i], 0)).getRecord_ID(); // C_Invoice_ID
					BigDecimal openAmt = (BigDecimal)p_table.getValueAt(rows[i], 7); // OpenAmt
					
					MPaymentAllocate payAll = new MPaymentAllocate(Env.getCtx(),0,trx.getTrxName());
					payAll.setC_Invoice_ID(idColumn);
					int C_Payment_ID = Env.getContextAsInt(Env.getCtx(),p_WindowNo, "C_Payment_ID");
					payAll.setC_Payment_ID(C_Payment_ID);
					payAll.setAmount(disponible);
					payAll.setInvoiceAmt(openAmt);
					payAll.save(trx.getTrxName());
					
					monto = monto.add(disponible);
					
					i++;
				}
			
				trx.commit();
				
				if (i<rows.length)
					JOptionPane.showMessageDialog(null, "Asignación Completa. No se contabilizaron todos los comprobantes seleccionados.","Información - Asignación",JOptionPane.INFORMATION_MESSAGE);
					
			}
			catch(Exception e){
				trx.rollback();
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "No se pudo realizar la asignación seleccionada.","Error - Asignación",JOptionPane.ERROR_MESSAGE);
			}
			
		} // IF
	}	//	saveSelectionDetail
	
	// Check All Totals less OpenAmt
	protected boolean checkTotals()
	{
		int[] rows = p_table.getSelectedRows();
		
		for (int i=0;i<rows.length;i++)
		{
			BigDecimal openAmt = (BigDecimal)p_table.getValueAt(rows[i], 7); // OpenAmt
			BigDecimal total = (BigDecimal)p_table.getValueAt(rows[i], 8); // Total
			
			if (openAmt.compareTo(total)==-1)
			{	
				JOptionPane.showMessageDialog(null, "Existe un registro con Total mayor al importe Abierto.","Error - Verificación de Montos",JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}
	
	//	 Obtiene la disponibilidad del monto asignado, con respecto al Total de Pago.
	protected BigDecimal getMontoDisponible(BigDecimal inicial, BigDecimal importe, BigDecimal total, BigDecimal asignado)
	{
		BigDecimal nuevoAsignado = asignado.add(importe).add(inicial);
		if (nuevoAsignado.compareTo(total)==1)
			return total.subtract(asignado.add(inicial));

		return importe;
	}
	
	
}   //  InfoInvoice
