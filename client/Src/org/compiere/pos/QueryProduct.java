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
package org.compiere.pos;

import java.awt.*;
import java.awt.event.*;
import java.math.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.compiere.apps.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	POS Query Product
 *	
 *  @author Jorg Janke
 *  @version $Id: QueryProduct.java,v 1.4 2005/03/11 20:28:22 jjanke Exp $
 */
public class QueryProduct extends PosSubPanel
	implements ActionListener, MouseListener, ListSelectionListener
{
	/**
	 * 	Constructor
	 */
	public QueryProduct (PosPanel posPanel)
	{
		super(posPanel);
	}	//	PosQueryProduct

	/** The Table					*/
	private MiniTable		m_table;
	
	private CPanel 			northPanel;
	private CScrollPane 	centerScroll;
	private ConfirmPanel	confirm;
	
	private CTextField		f_value;
	private CTextField		f_name;
	private CTextField		f_upc;
	private CTextField		f_sku;

	private CButton			f_up;
	private CButton			f_down;

	private int				m_M_Product_ID;
	private String			m_ProductName;
	private BigDecimal		m_Price;
	//
	private int 			m_M_PriceList_Version_ID;
	private int 			m_M_Warehouse_ID;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(QueryProduct.class);
	
	
	/**	Table Column Layout Info			*/
	private static ColumnInfo[] s_layout = new ColumnInfo[] 
	{
		new ColumnInfo(" ", "M_Product_ID", IDColumn.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "Value"), "Value", String.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "Name"), "Name", String.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "UPC"), "UPC", String.class), 
		new ColumnInfo(Msg.translate(Env.getCtx(), "SKU"), "SKU", String.class), 
		new ColumnInfo(Msg.translate(Env.getCtx(), "QtyAvailable"), "QtyAvailable", Double.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOnHand"), "QtyOnHand", Double.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "PriceStd"), "PriceStd", BigDecimal.class)
	};
	/**	From Clause							*/
	private static String s_sqlFrom = "RV_WarehousePrice";
	/** Where Clause						*/
	private static String s_sqlWhere = "IsActive='Y'"; 

	/**
	 * 	Set up Panel
	 */
	protected void init()
	{
		setLayout(new BorderLayout(5,5));
		setVisible(false);
		//	North
		northPanel = new CPanel(new GridBagLayout());
		add (northPanel, BorderLayout.NORTH);
		northPanel.setBorder(new TitledBorder(Msg.getMsg(p_ctx, "Query")));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = PosSubPanel.INSETS2;
		//
		gbc.gridy = 0;
		gbc.gridx = GridBagConstraints.RELATIVE;
		CLabel lvalue = new CLabel(Msg.translate(p_ctx, "Value"));
		gbc.anchor = GridBagConstraints.EAST;
		northPanel.add (lvalue, gbc);
		f_value = new CTextField(20);
		lvalue.setLabelFor(f_value);
		gbc.anchor = GridBagConstraints.WEST;
		northPanel.add(f_value, gbc);
		f_value.addActionListener(this);
		//
		CLabel lupc = new CLabel(Msg.translate(p_ctx, "UPC"));
		gbc.anchor = GridBagConstraints.EAST;
		northPanel.add (lupc, gbc);
		f_upc = new CTextField(15);
		lupc.setLabelFor(f_upc);
		gbc.anchor = GridBagConstraints.WEST;
		northPanel.add(f_upc, gbc);
		f_upc.addActionListener(this);
		//
		gbc.gridy = 1;
		CLabel lname = new CLabel(Msg.translate(p_ctx, "Name"));
		gbc.anchor = GridBagConstraints.EAST;
		northPanel.add (lname, gbc);
		f_name = new CTextField(20);
		lname.setLabelFor(f_name);
		gbc.anchor = GridBagConstraints.WEST;
		northPanel.add(f_name, gbc);
		f_name.addActionListener(this);
		//
		CLabel lsku = new CLabel(Msg.translate(p_ctx, "SKU"));
		gbc.anchor = GridBagConstraints.EAST;
		northPanel.add (lsku, gbc);
		f_sku = new CTextField(15);
		lsku.setLabelFor(f_sku);
		gbc.anchor = GridBagConstraints.WEST;
		northPanel.add(f_sku, gbc);
		f_sku.addActionListener(this);
		//
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = .1;
		f_up = createButtonAction("Previous", KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
		northPanel.add(f_up, gbc);
		gbc.weightx = 0;
		f_down = createButtonAction("Next", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
		northPanel.add(f_down, gbc);
		
		//	Confirm
		confirm = new ConfirmPanel (true, true, true, false, false, false, false);
		add (confirm, BorderLayout.SOUTH);
		confirm.addActionListener(this);

		//	Center
		m_table = new MiniTable();
		String sql = m_table.prepareTable (s_layout, s_sqlFrom, 
			s_sqlWhere, false, "RV_WarehousePrice")
			+ " ORDER BY Margin, QtyAvailable";
		m_table.setRowSelectionAllowed(true);
		m_table.setColumnSelectionAllowed(false);
		m_table.setMultiSelection(false);
		m_table.addMouseListener(this);
		m_table.getSelectionModel().addListSelectionListener(this);
		enableButtons();
		centerScroll = new CScrollPane(m_table);
		add (centerScroll, BorderLayout.CENTER);
	}	//	init

	
	/**
	 * 	Get GridBagConstraints
	 *	@return constraints
	 */
	protected GridBagConstraints getGridBagConstraints ()
	{
		GridBagConstraints gbc = super.getGridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.gridwidth = 2; //	GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.1;
		gbc.weighty = 0.5;
		return gbc;
	}	//	getGridBagConstraints
	
	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		removeAll();
		northPanel = null;
		centerScroll = null;
		confirm = null;
		m_table = null;
	}	//	dispose
	
	/**
	 * 	Set Visible
	 *	@param aFlag visible
	 */
	public void setVisible (boolean aFlag)
	{
		super.setVisible (aFlag);
		if (aFlag)
			f_value.requestFocus();
	}	//	setVisible

	
	/**
	 * 	Set Query Data
	 *	@param M_PriceList_Version_ID plv
	 *	@param M_Warehouse_ID wh
	 */
	public void setQueryData (int M_PriceList_Version_ID, int M_Warehouse_ID)
	{
		m_M_PriceList_Version_ID = M_PriceList_Version_ID;
		m_M_Warehouse_ID = M_Warehouse_ID;
	}	//	setQueryData
	
	/**
	 * 	Action Listener
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		log.info(e.getActionCommand());
		if ("Refresh".equals(e.getActionCommand())
			|| e.getSource() == f_value || e.getSource() == f_upc
			|| e.getSource() == f_name || e.getSource() == f_sku)
		{
			setResults(MWarehousePrice.find (p_ctx,
				m_M_PriceList_Version_ID, m_M_Warehouse_ID,
				f_value.getText(), f_name.getText(), f_upc.getText(), f_sku.getText(), null));
			return;
		}
		else if ("Reset".equals(e.getActionCommand()))
		{
			f_value.setText(null);
			f_name.setText(null);
			f_sku.setText(null);
			f_upc.setText(null);
			setResults(new MWarehousePrice[0]);
			return;
		}
		else if ("Previous".equalsIgnoreCase(e.getActionCommand()))
		{
			int rows = m_table.getRowCount();
			if (rows == 0)
				return;
			int row = m_table.getSelectedRow();
			row--;
			if (row < 0)
				row = 0;
			m_table.getSelectionModel().setSelectionInterval(row, row);
			return;
		}
		else if ("Next".equalsIgnoreCase(e.getActionCommand()))
		{
			int rows = m_table.getRowCount();
			if (rows == 0)
				return;
			int row = m_table.getSelectedRow();
			row++;
			if (row >= rows)
				row = rows - 1;
			m_table.getSelectionModel().setSelectionInterval(row, row);
			return;
		}
		//	Exit
		close();
	}	//	actionPerformed
	
	
	/**
	 * 	Set/display Results
	 *	@param results results
	 */
	public void setResults (MWarehousePrice[] results)
	{
		m_table.loadTable(results);
		enableButtons();
	}	//	setResults
	
	/**
	 * 	Table selection changed
	 *	@param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;
		enableButtons();
	}	//	valueChanged

	/**
	 * 	Enable/Set Buttons and set ID
	 */
	private void enableButtons()
	{
		m_M_Product_ID = -1;
		m_ProductName = null;
		m_Price = null;
		int row = m_table.getSelectedRow();
		boolean enabled = row != -1;
		if (enabled)
		{
			Integer ID = m_table.getSelectedRowKey();
			if (ID != null)
			{
				m_M_Product_ID = ID.intValue();
				m_ProductName = (String)m_table.getValueAt(row, 2);
				m_Price = (BigDecimal)m_table.getValueAt(row, 7);
			}
		}
		confirm.getOKButton().setEnabled(enabled);
		log.fine("M_Product_ID=" + m_M_Product_ID + " - " + m_ProductName + " - " + m_Price); 
	}	//	enableButtons

	/**
	 *  Mouse Clicked
	 *  @param e event
	 */
	public void mouseClicked(MouseEvent e)
	{
		//  Double click with selected row => exit
		if (e.getClickCount() > 1 && m_table.getSelectedRow() != -1)
		{
			enableButtons();
			close();
		}
	}   //  mouseClicked

	public void mouseEntered (MouseEvent e) {}
	public void mouseExited (MouseEvent e) {}
	public void mousePressed (MouseEvent e) {}
	public void mouseReleased (MouseEvent e) {}

	/**
	 * 	Close.
	 * 	Set Values on other panels and close
	 */
	private void close()
	{
		log.fine("M_Product_ID=" + m_M_Product_ID); 
		
		if (m_M_Product_ID > 0)
		{
			p_posPanel.f_product.setM_Product_ID(m_M_Product_ID);
			p_posPanel.f_curLine.setPrice(m_Price);
		}
		else
		{
			p_posPanel.f_product.setM_Product_ID(0);
			p_posPanel.f_curLine.setPrice(Env.ZERO);
		}
		p_posPanel.closeQuery(this);
	}	//	close
	
}	//	PosQueryProduct
