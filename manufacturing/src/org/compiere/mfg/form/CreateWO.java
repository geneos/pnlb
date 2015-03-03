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
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.mfg.form;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.math.*;
import java.text.*;
import java.util.*;

import org.compiere.util.*;
import org.compiere.apps.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.plaf.*;
import org.compiere.process.*;
import org.compiere.apps.form.*;

/**
 *  Create Manual Payments From (AP) Invoices or (AR) Credit Memos.
 *  Allows user to select Invoices for payment.
 *  When Processed, PaySelection is created
 *  and optionally posted/generated and printed
 *
 *  @author Jorg Janke
 *  @version $Id: CreateWO.java,v 1.2 2004/02/27 19:36:44 sklakken Exp $
 */
public class CreateWO extends CPanel
	implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{
  private int          m_WindowNo = 0;   
	private FormFrame    m_frame;      
  private boolean      m_isLocked = false;
  private String       tblSql = null;
  private String       sqlWhere = "1 = 1";
  private Properties   ctx = Env.getCtx();
  
  /* Parameter Section Elements */
  
  private GridBagLayout parameterLayout = new GridBagLayout();  
	private CPanel        parameterPanel  = new CPanel();
	private CLabel        labelWarehouse  = new CLabel();
	private CComboBox     fieldWarehouse  = new CComboBox();
	private CLabel        labelBPartner   = new CLabel();
	private CComboBox     fieldBPartner   = new CComboBox();
	private JButton       bRefresh        = ConfirmPanel.createRefreshButton(true);
  

  /* Main Section Elements */

  private CTabbedPane tabbedPane = new CTabbedPane();
  
  private BorderLayout mainLayout = new BorderLayout();
  private CPanel       mainPanel  = new CPanel();
	private JLabel       dataStatus = new JLabel();  
	private JScrollPane  dataPane   = new JScrollPane();
	private MiniTable    miniTable  = new MiniTable();

  /* Command and Status Bar Elements - Combined to create Bottom Pane */
  
	private FlowLayout   commandLayout = new FlowLayout();  
	private CPanel       commandPanel  = new CPanel();
	private JButton      bCancel       = ConfirmPanel.createCancelButton(true);
	private JButton      bGenerate     = ConfirmPanel.createProcessButton(true);

	private BorderLayout statusLayout  = new BorderLayout();  
  private CPanel       statusPanel   = new CPanel();  
  private StatusBar    statusBar     = new StatusBar();
  
	private BorderLayout bottomLayout  = new BorderLayout();  
  private CPanel       bottomPanel   = new CPanel();    
  /**
	 *  CreateWO Constructor
	 */
	public CreateWO()
	{
	}   //  CreateWO

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		Log.trace(Log.l1_User, "CreateWO.init");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			jbInit();
			dynInit();
			frame.getContentPane().add(parameterPanel, BorderLayout.NORTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		}
		catch(Exception e)
		{
			Log.error("CreateWO.init", e);
		}
	}	//	init
	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
    CompiereColor.setBackground(this);
    /*
     * Parameter Panel Initializations
     */
    parameterPanel.setLayout(parameterLayout);
		labelWarehouse.setText(Msg.translate(Env.getCtx(), "M_Warehouse_ID"));
		labelBPartner.setText(Msg.translate(Env.getCtx() , "C_BPartner_ID"));

    fieldWarehouse = new CComboBox(getWarehouses());
    fieldBPartner = new CComboBox(getBPartners());

    /* Line 1 */
    parameterPanel.add(labelWarehouse, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldWarehouse, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 15), 0, 0));    
    
    /* Line 2 */
		parameterPanel.add(labelBPartner, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldBPartner, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 15), 0, 0));
		parameterPanel.add(bRefresh,    new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    
    fieldWarehouse.addActionListener(this);
    fieldBPartner.addActionListener(this);
    bRefresh.addActionListener(this);
    /*
     * Main Panel Initializations
     */
		mainPanel.setLayout(mainLayout);
		mainPanel.add(dataStatus, BorderLayout.SOUTH);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(miniTable, null);
		miniTable.getModel().addTableModelListener(this);    
    /*
     * Command Panel Initializations - add to main panel
     */
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandPanel.add(bCancel, null);
		commandPanel.add(bGenerate,null);    
    /*
     * Status Panel Initializations
     */
    statusPanel.setLayout(statusLayout);
    statusPanel.add(statusBar, null);
    /*
     * Bottom Panel Initializations - includes command and status panels
     */
		bottomPanel.setLayout(bottomLayout);    
		bottomPanel.add(commandPanel, BorderLayout.NORTH);
		bottomPanel.add(statusPanel, BorderLayout.SOUTH);
    
		bGenerate.addActionListener(this);
		bCancel.addActionListener(this);
	}   //  jbInit
	/**
	 * 	Get Array of Warehouses
	 *	@return warehouses
	 */  
	private KeyNamePair[] getWarehouses()
  {
    String sql = 
      "SELECT DISTINCT w.M_Warehouse_ID, w.Name "           
			+ "FROM C_Order o"
			+ " INNER JOIN M_Warehouse w ON (o.M_Warehouse_ID=w.M_Warehouse_ID)"
      + " WHERE " + sqlWhere
      + " ORDER BY 2";
      
      return DB.getKeyNamePairs(sql, true);
  }
	/**
	 * 	Get Array of Business Partners
	 *	@return bpartners
	 */
	private KeyNamePair[] getBPartners()
  {
    String sql = 
      "SELECT DISTINCT bp.C_BPartner_ID, bp.Name "           
			+ "FROM C_Order o"
			+ " INNER JOIN C_BPartner bp ON (o.C_BPartner_ID=bp.C_BPartner_ID)"
      + " WHERE " + sqlWhere
      + " ORDER BY 2";
      
      return DB.getKeyNamePairs(sql, true);
  } 
	/**
	 *  Dynamic Init.
	 *  - Load Bank Info
	 *  - Load BPartner
	 *  - Init Table
	 */
	private void dynInit()
	{
    tblSql = miniTable.prepareTable(new ColumnInfo[] {
			new ColumnInfo(" ", "o.C_Order_ID", IDColumn.class, false, false, null),
			new ColumnInfo(Msg.translate(ctx, "DocumentNo"), "o.DocumentNo", String.class),
			new ColumnInfo(Msg.translate(ctx, "DateOrdered"), "o.DateOrdered", Timestamp.class),
			new ColumnInfo(Msg.translate(ctx, "Description"), "o.Description", String.class),
      new ColumnInfo(Msg.translate(ctx, "C_BPartner_ID"), "bp.Name", KeyNamePair.class, true, false, "o.C_BPartner_ID"),
      new ColumnInfo(Msg.translate(ctx, "M_Warehouse_ID"), "w.Name", KeyNamePair.class, true, false, "o.M_Warehouse_ID"),
			},
			//	FROM
			"C_Order o"
			+ " INNER JOIN C_BPartner bp ON (o.C_BPartner_ID=bp.C_BPartner_ID)"
			+ " INNER JOIN M_Warehouse w ON (o.M_Warehouse_ID=w.M_Warehouse_ID)",
			//	WHERE
			sqlWhere,	
			true, "o");
      
    miniTable.getModel().addTableModelListener(this);      
      
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "GenerateWorkOrders"));
		statusBar.setStatusDB(" ");      
      
	}   //  dynInit
	/**
	 *  Query and create TableInfo
	 */
	private void loadTableInfo()
	{
		Log.trace(Log.l3_Util, "CreateWO.loadTableInfo");
		//  not yet initialized
		if (tblSql == null)
			return;

		String sql = tblSql;
    
		//  Parameters

    KeyNamePair bp = (KeyNamePair) fieldBPartner.getSelectedItem();
    KeyNamePair wh = (KeyNamePair) fieldWarehouse.getSelectedItem();
    
		int C_BPartner_ID  = bp.getKey();
		int W_Warehouse_ID = wh.getKey();
    
	  sql += (C_BPartner_ID > 0)  ? " AND o.C_BPartner_ID=? "  : "";
		sql += (W_Warehouse_ID > 0) ? " AND o.M_Warehouse_ID=? " : "";
    sql += " ORDER BY 2 ";

		try
		{
			int index = 1;
			PreparedStatement pstmt = DB.prepareStatement(sql);
      
      if (C_BPartner_ID > 0)
			  pstmt.setInt(index++, C_BPartner_ID);
      if (W_Warehouse_ID > 0)
			  pstmt.setInt(index++, W_Warehouse_ID);
		
			ResultSet rs = pstmt.executeQuery();
			miniTable.loadTable(rs);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			Log.error ("CreateWO.loadTableInfo", e);
		}
		statusBar.setStatusDB("     ");    
	}   //  loadTableInfo
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
		Log.trace(Log.l1_User, "CreateWO.saveSelection");
		//  ID selection may be pending
		miniTable.editingStopped(new ChangeEvent(this));
		//  Array of Integers
		ArrayList results = new ArrayList();

		//	Get selected entries
		int rows = miniTable.getRowCount();
    
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getValueAt(i, 0);     //  ID in column 0

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
		Log.trace(Log.l4_Data, "CreateWO.saveSelection", sb.toString());
		return sb.toString();
	}	//	saveSelection  
	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose
	/*************************************************************************/

	/**
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == bCancel) 
    {
			dispose();
    }
    
		if (e.getSource() == bGenerate)
    {  
      String sqlOrders = saveSelection();
      
      if (saveSelection().length() > 0)
      {
        if(ADialog.ask(m_WindowNo, this, "Confirm Work Order Generation of Selected Sales Orders")) {
			    generateWorkOrders(sqlOrders);
        }
      }
		  else
        ADialog.error(m_WindowNo, this, "No Factory Sales Order Selections Made");       
    }
    if (e.getSource() == fieldWarehouse || 
        e.getSource() == fieldBPartner  || 
        e.getSource() == bRefresh)
    {
			loadTableInfo();
    }
	}   //  actionPerformed  
  /**
	 *  Generate the Work Orders
	 *  @param e event
	 */
	public void generateWorkOrders(String sqlOrders)
	{
    ADialog.info(m_WindowNo, this, "Work Orders Generated");
	}
	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return m_isLocked;
	}   //  isLoacked
	/**
	 *  Lock User Interface
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi)
	{
		this.setEnabled(false);
		m_isLocked = true;
	}   //  lockUI  
	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi process info
	 */   
	public void unlockUI (ProcessInfo pi)
	{

	}   //  unlockUI
	/**
	 *  Method to be executed async.
	 *  Called from the ASyncProcess worker
	 *  @param pi process info
	 */  
	public void executeASync (ProcessInfo pi)
	{
		Log.trace(Log.l3_Util, "CreateWO.executeASync");
	}   //  executeASync  


}   //  CreateWO