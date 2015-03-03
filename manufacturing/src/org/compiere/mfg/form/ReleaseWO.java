/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2001 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc. and Colorado
 * Correctional Industries;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.mfg.form;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.math.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import org.compiere.apps.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.swing.*;
import org.compiere.util.*;
import org.compiere.apps.form.*;

/**
 *	Manual Invoice Selection
 *
 *  @author Jorg Janke and Scott Klakken
 *  @version $Id: ReleaseWO.java,v 1.1 2004/02/24 23:33:43 sklakken Exp $
 */
public class ReleaseWO extends CPanel implements FormPanel, ActionListener, VetoableChangeListener, ChangeListener, TableModelListener, ASyncProcess
{
	/**
	 *	Standard Constructor
	 */
	public ReleaseWO()
	{
	}	//	ReleaseWO

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		Log.trace(Log.l1_User, "ReleaseWO.init");
		m_WindowNo = WindowNo;
		m_frame = frame;
		Env.setContext(Env.getCtx(), m_WindowNo, "IsSOTrx", "Y");
		try
		{
			fillPicks();
			jbInit();
			dynInit();
			frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		}
		catch(Exception ex)
		{
			Log.error("ReleaseWO,init", ex);
		}
	}	//	init

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	private boolean			m_selectionActive = true;
	private String      m_whereClause;
	private Object 			m_AD_Org_ID = null;
	private Object 			m_C_BPartner_ID = null;
	//
	private CTabbedPane tabbedPane = new CTabbedPane();
	private CPanel selPanel = new CPanel();
	private CPanel selNorthPanel = new CPanel();
	private BorderLayout selPanelLayout = new BorderLayout();
	private CLabel lOrg = new CLabel();
	private VLookup fOrg;
	private CLabel lBPartner = new CLabel();
	private VLookup fBPartner;
	private FlowLayout northPanelLayout = new FlowLayout();
	private ConfirmPanel confirmPanelSel = new ConfirmPanel(true);
	private ConfirmPanel confirmPanelGen = new ConfirmPanel(false, true, false, false, false, false, true);
	private StatusBar statusBar = new StatusBar();
	private CPanel genPanel = new CPanel();
	private BorderLayout genLayout = new BorderLayout();
	private CTextArea info = new CTextArea();
	private JScrollPane scrollPane = new JScrollPane();
	private MiniTable miniTable = new MiniTable();

	/**
	 *	Static Init.
	 *  <pre>
	 *  selPanel (tabbed)
	 *      fOrg, fBPartner
	 *      scrollPane & miniTable
	 *  genPanel
	 *      info
	 *  </pre>
	 *  @throws Exception
	 */
	void jbInit() throws Exception
	{
		CompiereColor.setBackground(this);
		//
		selPanel.setLayout(selPanelLayout);
		lOrg.setLabelFor(fOrg);
		lOrg.setText("Org");
		lBPartner.setLabelFor(fBPartner);
		lBPartner.setText("BPartner");
		selNorthPanel.setLayout(northPanelLayout);
		northPanelLayout.setAlignment(FlowLayout.LEFT);
		tabbedPane.add(selPanel, Msg.getMsg(Env.getCtx(), "Select"));
		selPanel.add(selNorthPanel, BorderLayout.NORTH);
		selNorthPanel.add(lOrg, null);
		selNorthPanel.add(fOrg, null);
		selNorthPanel.add(lBPartner, null);
		selNorthPanel.add(fBPartner, null);
		selPanel.setName("selPanel");
		selPanel.add(confirmPanelSel, BorderLayout.SOUTH);
		selPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(miniTable, null);
		confirmPanelSel.addActionListener(this);
		//
		tabbedPane.add(genPanel, Msg.getMsg(Env.getCtx(), "Generate"));
		genPanel.setLayout(genLayout);
		genPanel.add(info, BorderLayout.CENTER);
		genPanel.setEnabled(false);
		info.setWrapStyleWord(true);
		info.setLineWrap(true);
		info.setBackground(CompierePLAF.getFieldBackground_Inactive());
		info.setEditable(false);
		genPanel.add(confirmPanelGen, BorderLayout.SOUTH);
		confirmPanelGen.addActionListener(this);
	}	//	jbInit

	/**
	 *	Fill Picks
	 *		Column_ID from C_Order
	 *  @throws Exception if Lookups cannot be initialized
	 */
	private void fillPicks() throws Exception
	{
		MLookup orgL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2163, DisplayType.TableDir);
		fOrg = new VLookup ("AD_Org_ID", false, false, true, orgL);
		lOrg.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		fOrg.addVetoableChangeListener(this);
		//
		MLookup bpL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2762, DisplayType.Search);
		fBPartner = new VLookup ("C_BPartner_ID", false, false, true, bpL);
		lBPartner.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		fBPartner.addVetoableChangeListener(this);
	}	//	fillPicks

	/**
	 *	Dynamic Init.
	 *	- Create GridController & Panel
	 *	- AD_Column_ID from C_Order
	 */
	private void dynInit()
	{
		//  create Columns
		miniTable.addColumn("C_Order_ID");
		miniTable.addColumn("AD_Org_ID");
		miniTable.addColumn("C_DocType_ID");
		miniTable.addColumn("DocumentNo");
		miniTable.addColumn("C_BPartner_ID");
		miniTable.addColumn("DateOrdered");
		miniTable.addColumn("TotalLines");
		//
		miniTable.setMultiSelection(true);
		miniTable.setRowSelectionAllowed(true);
		//  set details
		miniTable.setColumnClass(0, IDColumn.class, false, " ");
		miniTable.setColumnClass(1, String.class, true, Msg.translate(Env.getCtx(), "AD_Org_ID"));
		miniTable.setColumnClass(2, String.class, true, Msg.translate(Env.getCtx(), "C_DocType_ID"));
		miniTable.setColumnClass(3, String.class, true, Msg.translate(Env.getCtx(), "DocumentNo"));
		miniTable.setColumnClass(4, String.class, true, Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		miniTable.setColumnClass(5, Timestamp.class, true, Msg.translate(Env.getCtx(), "DateOrdered"));
		miniTable.setColumnClass(6, BigDecimal.class, true, Msg.translate(Env.getCtx(), "TotalLines"));
		//
		miniTable.autoSize();
		miniTable.getModel().addTableModelListener(this);
		//	Info
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InvGenerateSel"));
		statusBar.setStatusDB(" ");
		//	Tabbed Pane Listener
		tabbedPane.addChangeListener(this);
	}	//	dynInit

	/**
	 *  Query Info
	 */
	private void executeQuery()
	{
		Log.trace(Log.l1_User, "ReleaseWO.executeQuery");
		int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
		//  Create SQL
		StringBuffer sql = new StringBuffer(
			"SELECT C_Order_ID, o.Name, dt.Name, DocumentNo, bp.Name, DateOrdered, TotalLines "
			+ "FROM C_Invoice_Candidate_v ic, AD_Org o, C_BPartner bp, C_DocType dt "
			+ "WHERE ic.AD_Org_ID=o.AD_Org_ID"
			+ " AND ic.C_BPartner_ID=bp.C_BPartner_ID"
			+ " AND ic.C_DocType_ID=dt.C_DocType_ID"
			+ " AND ic.AD_Client_ID=?");

		if (m_AD_Org_ID != null)
			sql.append(" AND ic.AD_Org_ID=").append(m_AD_Org_ID);
		if (m_C_BPartner_ID != null)
			sql.append(" AND ic.C_BPartner_ID=").append(m_C_BPartner_ID);
		//
		sql.append(" ORDER BY o.Name,bp.Name,DateOrdered");
	//	Log.trace(Log.l5_DData, "ReleaseWO.executeQuery - AD_Client_ID=" + AD_Client_ID, sql.toString());

		//  reset table
		int row = 0;
		miniTable.setRowCount(row);
		//  Execute
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			//
			while (rs.next())
			{
				//  extend table
				miniTable.setRowCount(row+1);
				//  set values
				miniTable.setValueAt(new IDColumn(rs.getInt(1)), row, 0);   //  C_Order_ID
				miniTable.setValueAt(rs.getString(2), row, 1);              //  Org
				miniTable.setValueAt(rs.getString(3), row, 2);              //  DocType
				miniTable.setValueAt(rs.getString(4), row, 3);              //  Doc No
				miniTable.setValueAt(rs.getString(5), row, 4);              //  BPartner
				miniTable.setValueAt(rs.getTimestamp(6), row, 5);           //  DateOrdered
				miniTable.setValueAt(rs.getBigDecimal(7), row, 6);          //  TotalLines
				//  prepare next
				row++;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			Log.error("ReleaseWO.executeQuery", e);
		}
		//
		miniTable.autoSize();
	//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  executeQuery

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	/**
	 *	Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		Log.trace(Log.l1_User, "ReleaseWO.actionPerformed - " + e.getActionCommand());
		//
		if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			dispose();
			return;
		}
		//
		m_whereClause = saveSelection();
		if (m_whereClause.length() > 0 && m_selectionActive)
			generateInvoices ();
		else
			dispose();
	}	//	actionPerformed

	/**
	 *	Vetoable Change Listener - requery
	 *  @param e event
	 */
	public void vetoableChange(PropertyChangeEvent e)
	{
		Log.trace(Log.l1_User, "ReleaseWO.vetoableChange - "
			+ e.getPropertyName() + "=" + e.getNewValue());
		if (e.getPropertyName().equals("AD_Org_ID"))
			m_AD_Org_ID = e.getNewValue();
		if (e.getPropertyName().equals("C_BPartner_ID"))
		{
			m_C_BPartner_ID = e.getNewValue();
			fBPartner.setValue(m_C_BPartner_ID);	//	display value
		}
		executeQuery();
	}	//	vetoableChange

	/**
	 *	Change Listener (Tab changed)
	 *  @param e event
	 */
	public void stateChanged (ChangeEvent e)
	{
		int index = tabbedPane.getSelectedIndex();
		m_selectionActive = (index == 0);
	}	//	stateChanged

	/**
	 *  Table Model Listener
	 *  @param e event
	 */
	public void tableChanged(TableModelEvent e)
	{
		int rowsSelected = 0;
		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getValueAt(i, 0);     //  ID in column 0
			if (id != null && id.isSelected())
				rowsSelected++;
		}
		statusBar.setStatusDB(" " + rowsSelected + " ");
	}   //  tableChanged

	/**
	 *	Save Selection & return selecion Query or ""
	 *  @return where clause like C_Order_ID IN (...)
	 */
	private String saveSelection()
	{
		Log.trace(Log.l1_User, "ReleaseWO.saveSelection");
		//  ID selection may be pending
		miniTable.editingStopped(new ChangeEvent(this));
		//  Array of Integers
		ArrayList results = new ArrayList();

		//	Get selected entries
		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getValueAt(i, 0);     //  ID in column 0
		//	Log.trace(Log.l6_Database, "Row=" + i + " - " + id);
			if (id != null && id.isSelected())
				results.add(id.getRecord_ID());
		}

		if (results.size() == 0)
			return "";

		//	Query String
		String keyColumn = "C_Order_ID";
		StringBuffer sb = new StringBuffer(keyColumn);
		if (results.size() > 1)
			sb.append(" IN (");
		else
			sb.append("=");
		//	Add elements
		for (int i = 0; i < results.size(); i++)
		{
			if (i > 0)
				sb.append(",");
			if (keyColumn.endsWith("_ID"))
				sb.append(results.get(i).toString());
			else
				sb.append("'").append(results.get(i).toString());
		}

		if (results.size() > 1)
			sb.append(")");
		//
		Log.trace(Log.l4_Data, "ReleaseWO.saveSelection", sb.toString());
		return sb.toString();
	}	//	saveSelection

	/*************************************************************************/

	/**
	 *	Generate Invoices
	 */
	private void generateInvoices ()
	{
		//	Reset Selection
		String sql = "UPDATE C_Order SET IsSelected = 'N' WHERE IsSelected='Y'"
			+ " AND AD_Client_ID=" + Env.getAD_Client_ID(Env.getCtx())
			+ " AND AD_Org_ID=" + Env.getAD_Org_ID(Env.getCtx());
		int no = DB.executeUpdate(sql);
		Log.trace(Log.l3_Util, "ReleaseWO.generateInvoices - Reset=" + no);

		//	Set Selection
		sql = "UPDATE C_Order SET IsSelected = 'Y' WHERE " + m_whereClause;
		no = DB.executeUpdate(sql);
		if (no == 0)
		{
			String msg = "No Invoices";     //  not translated!
			Log.trace(Log.l2_Sub, "ReleaseWO.generateInvoice", msg);
			info.setText(msg);
			return;
		}
		Log.trace(Log.l3_Util, "ReleaseWO.generateInvoices - Set=" + no);

		m_selectionActive = false;  //  prevents from being called twice
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InvGenerateGen"));
		statusBar.setStatusDB(String.valueOf(no));

		//	Prepare Process
		ProcessInfo pi = new ProcessInfo ("", 134);  // HARDCODED    C_Invoice_Create0
		pi.setAD_PInstance_ID (ProcessCtl.getInstanceID (m_WindowNo, pi.getAD_Process_ID(), pi.getRecord_ID()));
		if (pi.getAD_PInstance_ID() == 0)
		{
			info.setText(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
			return;
		}

		//	Add Parameter
		sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_STRING) "
			+ "VALUES (" + pi.getAD_PInstance_ID() + ",1,'Selection', 'Y')";
		no = DB.executeUpdate(sql);
		if (no == 0)
		{
			String msg = "No Parameter added";  //  not translated
			info.setText(msg);
			Log.error("ReleaseWO.generateInvoice - " + msg);
			return;
		}

		//	Execute Process
		ProcessCtl worker = new ProcessCtl(this, pi);
		worker.start();     //  complete tasks in unlockUI / generateInvoice_complete
	}	//	generateInvoices

	/**
	 *  Complete generating invoices.
	 *  Called from Unlock UI
	 *  @param pi process info
	 */
	private void generateInvoice_complete (ProcessInfo pi)
	{
		//  Switch Tabs
		tabbedPane.setSelectedIndex(1);
		//
		ProcessInfoUtil.setLogFromDB(pi);
		info.setText(pi.getSummary());
		info.append("\n(");
		info.append(Msg.getMsg(Env.getCtx(), "InvGenerateInfo"));
		//  Invoices are generated depending on the Invoicing Rule selection in the Order
		info.append(")");

		//	Reset Selection
		String sql = "UPDATE C_Order SET IsSelected = 'N' WHERE " + m_whereClause;
		int no = DB.executeUpdate(sql);
		Log.trace(Log.l3_Util, "ReleaseWO.generateInvoices_complete - Reset=" + no);

		//	Get results
		int[] ids = pi.getIDs();
		if (ids == null || ids.length == 0)
			return;

		confirmPanelGen.getOKButton().setEnabled(false);
		//	OK to print invoices
		if (ADialog.ask(m_WindowNo, this, "PrintInvoices"))
		{
			info.append("\n\n" + Msg.getMsg(Env.getCtx(), "PrintInvoices"));
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			int retValue = ADialogDialog.A_CANCEL;
			do
			{
				//	Loop through all items
				for (int i = 0; i < ids.length; i++)
				{
					int AD_Invoice_ID = ids[i];
					ReportCtl.startDocumentPrint(ReportCtl.INVOICE, AD_Invoice_ID, true);
				}
				ADialogDialog d = new ADialogDialog (m_frame,
					Env.getHeader(Env.getCtx(), m_WindowNo),
					Msg.getMsg(Env.getCtx(), "PrintoutOK?"),
					JOptionPane.QUESTION_MESSAGE);
				retValue = d.getReturnCode();
			}
			while (retValue == ADialogDialog.A_CANCEL);
			setCursor(Cursor.getDefaultCursor());
		}	//	OK to print invoices

		//
		confirmPanelGen.getOKButton().setEnabled(true);
	}   //  generateInvoices_complete

	/*************************************************************************/

	/**
	 *  Lock User Interface.
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi)
	{
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.setEnabled(false);
	}   //  lockUI

	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi result of execute ASync call
	 */
	public void unlockUI (ProcessInfo pi)
	{
		this.setEnabled(true);
		this.setCursor(Cursor.getDefaultCursor());
		//
		generateInvoice_complete(pi);
	}   //  unlockUI

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return this.isEnabled();
	}   //  isUILocked

	/**
	 *  Method to be executed async.
	 *  Called from the Worker
	 *  @param pi ProcessInfo
	 */
	public void executeASync (ProcessInfo pi)
	{
	}
  

  
   //  executeASync

}	//	ReleaseWO
