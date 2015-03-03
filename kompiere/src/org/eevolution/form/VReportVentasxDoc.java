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
package org.eevolution.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.VCreateFrom;
import org.compiere.grid.ed.Calculator;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.*;
import org.compiere.process.ProcessInfo;
import org.compiere.swing.CPanel;
import org.compiere.util.*;
import org.eevolution.process.GenerateVentasDocumento;

/**
 *  Create Invoice Transactions from PO Orders or AP Invoices
 *
 *  @author Jorg Janke
 *  @version  $Id: VCreateFromInvoice.java,v 1.35 2005/11/14 02:10:58 jjanke Exp $
 */
public class VReportVentasxDoc extends CPanel
implements FormPanel, ActionListener, TableModelListener
{
	private static final long serialVersionUID = 1L;

	int AD_Client_ID = 0;
	int AD_Org_ID = 0;
	/**
	 *  Protected Constructor
	 *  @param mTab MTab
	 */
	public VReportVentasxDoc()
	{
		super();
		isSoTrx = true; 
		
		try
		{
			AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
			AD_Org_ID = Env.getAD_Org_ID(Env.getCtx());
			
			if (!dynInit())
				return;
			confirmPanel.addActionListener(this);
			//  Set status
			statusBar.setStatusDB("");
			tableChanged(null);
			p_initOK = true;
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
			p_initOK = false;
		}
	}   //  VReportVentasxDoc

	/** Is Sales Transaction               */
	protected boolean 			isSoTrx = false; 
	private boolean             p_initOK = false;
	/**	Logger			*/
	protected static CLogger log = CLogger.getCLogger(VCreateFrom.class);

	private JPanel parameterPanel = new JPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private JScrollPane dataPane = new JScrollPane();
	private JPanel southPanel = new JPanel();
	private BorderLayout southLayout = new BorderLayout();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	protected StatusBar statusBar = new StatusBar();
	protected MiniTable dataTable = new MiniTable();
	private boolean m_actionActive = false;
	protected VDate fromDateField = new VDate();
	protected JLabel fromDateLabel = new JLabel();
	protected VDate toDateField = new VDate();
	protected JLabel toDateLabel = new JLabel();
	protected JCheckBox conceptoField = new JCheckBox();
	protected JLabel conceptoLabel = new JLabel();
	protected JCheckBox todosField = new JCheckBox();
	protected JLabel todosLabel = new JLabel();
	protected JLabel currencyLabel = new JLabel();
	protected JComboBox currencyField = new JComboBox();
	
	/*
	 * 18-01-2010 Camarzana Mariano
	 * 
	 */
	protected JCheckBox convertirPesosField = new JCheckBox();
	protected JLabel convertirPesosLabel = new JLabel();
	
	protected VNumber tasaConversion = new VNumber();
	protected JLabel tasaConversionLabel = new JLabel();
	
	
	
	/**
	 *	Init OK to be able to make changes?
	 *  @return on if initialized
	 */
	public boolean isInitOK()
	{
		return p_initOK;
	}	//	isInitOK
	
	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	protected boolean dynInit() throws Exception
	{
		log.config("");
		initFields();
		return true;
	}   //  dynInit

	/**
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		log.config("Action=" + e.getActionCommand());
		
		//  OK - Save
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			if (save())
				dispose();
		}
		//  Cancel
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			dispose();
		}
		
		if (m_actionActive)
			return;
		
		m_actionActive = true;
		log.config("Action=" + e.getActionCommand());
		
		if (e.getSource().equals(todosField))
		{
			loadDocType(todosField.isSelected());
		}
		/*
		 * 18-01-2011 Camarzana Mariano
		 * Si la moneda es distinta a pesos habilito el check de conversion
		 */
		if (e.getSource().equals(currencyField))
		{
			BigDecimal currency_id = (new BigDecimal(((KeyNamePair)currencyField.getSelectedItem()).getKey()));
			MCurrency mc = new MCurrency(Env.getCtx(),currency_id.intValue(),null);
			if (!mc.getISO_Code().equals("ARS")){
				convertirPesosField.setEnabled(true);
				convertirPesosLabel.setEnabled(true);
			}
			else {
					convertirPesosField.setEnabled(false);
					convertirPesosLabel.setEnabled(false);
				}
		}
		if (e.getSource().equals(convertirPesosField)){
			if (convertirPesosField.isSelected()){	
				tasaConversion.setEnabled(true);
				tasaConversionLabel.setEnabled(true);
			}
			else {
				tasaConversion.setEnabled(false);
				tasaConversionLabel.setEnabled(false);
			}
		}
		/*
		 * Fin Modificacion
		 */
		m_actionActive = false;
	}   //  actionPerformed

	/**
	 *  List number of rows selected
	 */
	protected void info()
	{
		TableModel model = dataTable.getModel();
		int rows = model.getRowCount();
		int count = 0;
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)model.getValueAt(i, 0)).booleanValue())
				count++;
		}
		statusBar.setStatusLine(String.valueOf(count));
	}   //  infoInvoice

	/**
	 *  Save - View Report
	 *  @return true if saved
	 */
	protected boolean save()
	{
		if (fromDateField.getTimestamp()!=null && toDateField.getTimestamp()!=null)
		{
			/*
			 * 18-01-2011 Camarzana Mariano
			 * Valido que si el usuario seleciono que desea convertir la moneda entonces debe ingresar un valor 
			 */
			if (convertirPesosField.isSelected()){ 
					if ((BigDecimal)tasaConversion.getValue()==null){
						JOptionPane.showMessageDialog(null, "Si quiere convertir la moneda debe ingresar la tasa correspondiente", "Datos Faltantes", JOptionPane.INFORMATION_MESSAGE);
						return false;
					}
					else 
						if (((BigDecimal)tasaConversion.getValue()).compareTo(BigDecimal.ZERO)<= 0){
							JOptionPane.showMessageDialog(null, "Si quiere convertir la moneda debe ingresar una tasa mayor a cero", "Datos Faltantes", JOptionPane.INFORMATION_MESSAGE);
							return false;
						}
			}
				
			log.config("");
			TableModel model = dataTable.getModel();
			int rows = model.getRowCount();
			if (rows == 0)
				return false;
	
			List<Integer> list = new ArrayList<Integer>();
			
			//  Lines
			for (int i = 0; i < rows; i++)
			{
				if (((Boolean)model.getValueAt(i, 0)).booleanValue())
				{
					KeyNamePair pp = (KeyNamePair)model.getValueAt(i, 1);   //  3-DocType
					list.add(new Integer(pp.getKey()));
				}   //   if selected
			}   //  for all rows
			
			GenerateVentasDocumento repVentas = new GenerateVentasDocumento();
			
			repVentas.setConvertirPesos(convertirPesosField.isSelected());
			repVentas.setTasaConversion((BigDecimal)tasaConversion.getValue());
			
			repVentas.setAD_CLIENT_ID(AD_Client_ID);
			repVentas.setAD_ORG_ID(AD_Org_ID);
			repVentas.setC_Currency_ID(new BigDecimal(((KeyNamePair)currencyField.getSelectedItem()).getKey()));
			repVentas.setConcepto(conceptoField.isSelected());
			repVentas.setDocs(list);
			repVentas.setFromDate(fromDateField.getTimestamp());
			repVentas.setToDate(toDateField.getTimestamp());
			
			ProcessInfo pi = new ProcessInfo("Reporte Ventas por Tipo de Documento", MProcess.getAllIDs(MProcess.Table_Name, "VALUE = 'ReporteVentasxDocumento'", null)[0]);
			MPInstance instance = new MPInstance(Env.getCtx(), pi.getAD_Process_ID(), 0);
			if (!instance.save())
				return false;
			pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());
			repVentas.startProcess(Env.getCtx(), pi, null);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Ingrese Fecha Desde y Fecha Hasta", "Datos Faltantes", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
	
		return true;
	}   //  saveInvoice

	/*************************************************************************/
	/**
	 *  Table Model Listener
	 *  @param e event
	 */
	public void tableChanged (TableModelEvent e)
	{
		int type = -1;
		if (e != null)
		{
			type = e.getType();
			if (type != TableModelEvent.UPDATE)
				return;
		}
		log.config("Type=" + type);
		info();
	}   //  tableChanged
	
	protected void initCurrency()
	{
		//  load Currency
		currencyField.removeAllItems();

		StringBuffer sql = new StringBuffer(" SELECT C_Currency_ID, ISO_CODE FROM C_Currency "
				+	" Where isActive='Y'"); 
		KeyNamePair first = new KeyNamePair(0, "");
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			
			ResultSet rs = pstmt.executeQuery();
			boolean prim = true;
			while (rs.next())
			{
				KeyNamePair pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				if (prim==true)
				{	first = pp;
					prim = false;
				}
				currencyField.addItem(pp);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}

		currencyField.setSelectedItem(first);
		
		/*
		 * 18-01-2011 Camarzana Mariano
		 */
		BigDecimal currency_id = (new BigDecimal(first.getKey()));
		MCurrency mc = new MCurrency(Env.getCtx(),currency_id.intValue(),null);
		if (!mc.getISO_Code().equals("ARS")){
			convertirPesosField.setEnabled(true);
			convertirPesosLabel.setEnabled(true);
			}
		else {
				convertirPesosField.setEnabled(false);
				convertirPesosLabel.setEnabled(false);
			}

		
		
	}   //  initCurrency
	
	/**
	 *  Load ComboBox Fields.
	 */
	protected void initFields()
	{
		initCurrency();
		loadDocType(false);
		
		todosField.addActionListener(this);
		/*
		 * 18-01-2010 Camarzana Mariano
		 */
		currencyField.addActionListener(this);
		convertirPesosField.addActionListener(this);
	}   //  initFields

    /**
	 *  Load Data - DocType
	 */
	protected void loadDocType(boolean check)
	{
		StringBuffer sql = new StringBuffer("");
		Vector<Vector> data = new Vector<Vector>();
		try
		{
			/**
			 *  Selected        - 0
			 *  C_DocType       - 1
			 */
			sql.append("SELECT "
				+ " C_DocType_ID, "					//	1
				+ " Name "	//	2
				+ " FROM C_DocType "
				+ " WHERE DocBaseType in ('ARI','ARC','ARF') and "
				+ " AD_Client_ID = ? AND isActive='Y'");
			//if (((KeyNamePair)orgField.getSelectedItem()).getKey() == 0)
			//	sql.append(" AND AD_org_ID = ?");
			
			log.finer(sql.toString());
			
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, AD_Client_ID);
			
			//if (((KeyNamePair)orgField.getSelectedItem()).getKey() == 0)
			//	pstmt.setInt(2, ((KeyNamePair)orgField.getSelectedItem()).getKey());
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(check));           //  0-Selection
				KeyNamePair pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				line.add(pp);                           //  1-DocType
				data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		loadTable(data, check);
	}   //  loadDocType

	/**
	 *  Load DocType data into Table
	 */
	protected void loadTable(Vector data, boolean check)
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>(7);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "C_DocType_ID"));
		
		//  Remove previous listeners
		dataTable.getModel().removeTableModelListener(this);
		//  Set Model
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		model.addTableModelListener(this);
		dataTable.setModel(model);
		//
		dataTable.setColumnClass(0, Boolean.class, check);      //  0-Selection
		dataTable.setColumnClass(1, String.class, true);        //  1-DocType
		//  Table UI
		dataTable.autoSize();
		
		if (check)
			statusBar.setStatusLine(String.valueOf(dataTable.getModel().getRowCount()));
		else
			statusBar.setStatusLine(String.valueOf(0));
		
	}   //  loadTable

	public void dispose() {
		formFrame.dispose();
	}
	
	protected FormFrame formFrame;

	public void init(int WindowNo, FormFrame frame) {
		//
		
		convertirPesosLabel.setText("Convertir a Pesos");
		tasaConversionLabel.setText("Tasa de Conversion");
		
		
		
		conceptoLabel.setText("Concepto");
		todosLabel.setText("Todos");
		currencyLabel.setText(Msg.getElement(Env.getCtx(), "C_Currency_ID"));
		fromDateLabel.setText("Fecha Desde");
		toDateLabel.setText("Fecha Hasta");
		
		Dimension d = new Dimension(150,22);
		currencyField.setPreferredSize(d);
		fromDateField.setPreferredSize(d);
		toDateField.setPreferredSize(d);
		conceptoField.setForeground(Color.WHITE);
		conceptoField.setBackground(Color.WHITE);
		todosField.setBackground(Color.WHITE);
		todosField.setForeground(Color.WHITE);
		
		formFrame = frame;
		
		parameterPanel.setLayout(parameterLayout);
		
		parameterPanel.add(currencyLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(currencyField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		
		parameterPanel.add(conceptoLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(conceptoField, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		
		parameterPanel.add(fromDateLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(fromDateField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		
		parameterPanel.add(toDateLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(toDateField, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		
		parameterPanel.add(todosLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(todosField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		
		
		
		/*
		 * 18-01-2011 Camarzana Mariano
		 * Campos necesarios para realizar la conversion a pesos
		 */
		parameterPanel.add(convertirPesosLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(convertirPesosField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		
		
		parameterPanel.add(tasaConversionLabel, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(tasaConversion, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
		
		
		/*convertirPesosField.setEnabled(false);
		convertirPesosLabel.setEnabled(false);*/
		tasaConversionLabel.setEnabled(false);
		tasaConversion.setEnabled(false);
		
		/*
		 * Fin modificacion 18-01-2011
		 */
		
		formFrame.getContentPane().add(parameterPanel, BorderLayout.NORTH);
		formFrame.getContentPane().add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(dataTable, null);
		
		formFrame.getContentPane().add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(southLayout);
		southPanel.add(confirmPanel, BorderLayout.CENTER);
		southPanel.add(statusBar, BorderLayout.SOUTH);
	}
	
}   //  VReportVentasxDoc
