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
package org.compiere.apps.form;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.sql.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import org.compiere.grid.tree.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Tree Maintenance
 *	
 *  @author Jorg Janke
 *  @version $Id: VTreeMaintenance.java,v 1.9 2005/11/14 02:10:57 jjanke Exp $
 */
public class VTreeMaintenance extends CPanel
	implements FormPanel, ActionListener, ListSelectionListener, PropertyChangeListener
{
	/**	Window No				*/
	private int         	m_WindowNo = 0;
	/**	FormFrame				*/
	private FormFrame 		m_frame;
	/**	Active Tree				*/
	private MTree		 	m_tree;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VTreeMaintenance.class);
	
	
	private BorderLayout	mainLayout	= new BorderLayout ();
	private CPanel 			northPanel	= new CPanel ();
	private FlowLayout		northLayout	= new FlowLayout ();
	private CLabel			treeLabel	= new CLabel ();
	private CComboBox		treeField;
	private CButton			bAddAll		= new CButton (Env.getImageIcon("FastBack24.gif"));
	private CButton			bAdd		= new CButton (Env.getImageIcon("StepBack24.gif"));
	private CButton			bDelete		= new CButton (Env.getImageIcon("StepForward24.gif"));
	private CButton			bDeleteAll	= new CButton (Env.getImageIcon("FastForward24.gif"));
	private CCheckBox		cbAllNodes	= new CCheckBox ();
	private CLabel			treeInfo	= new CLabel ();
	//
	private JSplitPane		splitPane	= new JSplitPane ();
	private VTreePanel		centerTree;
	private JList			centerList	= new JList ();

	
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info( "VMerge.init - WinNo=" + m_WindowNo);
		try
		{
			preInit();
			jbInit ();
			frame.getContentPane().add(this, BorderLayout.CENTER);
		//	frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
			action_loadTree();
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "VTreeMaintenance.init", ex);
		}
	}	//	init
	
	/**
	 * 	Fill Tree Combo
	 */
	private void preInit()
	{
		KeyNamePair[] trees = DB.getKeyNamePairs(MRole.getDefault().addAccessSQL(
			"SELECT AD_Tree_ID, Name FROM AD_Tree WHERE TreeType NOT IN ('BB','PC') ORDER BY 2", 
			"AD_Tree", MRole.SQL_NOTQUALIFIED, MRole.SQL_RW), false);
		treeField = new CComboBox(trees);
		treeField.addActionListener(this);
		//
		centerTree = new VTreePanel (m_WindowNo, false, true);
		centerTree.addPropertyChangeListener(VTreePanel.NODE_SELECTION, this);
	}	//	preInit
	
	/**
	 * 	Static init
	 *	@throws Exception
	 */
	private void jbInit () throws Exception
	{
		this.setLayout (mainLayout);
		treeLabel.setText (Msg.translate(Env.getCtx(), "AD_Tree_ID"));
		cbAllNodes.setEnabled (false);
		cbAllNodes.setText (Msg.translate(Env.getCtx(), "IsAllNodes"));
		treeInfo.setText (" ");
		bAdd.setToolTipText("Add to Tree");
		bAddAll.setToolTipText("Add ALL to Tree");
		bDelete.setToolTipText("Delete from Tree");
		bDeleteAll.setToolTipText("Delete ALL from Tree");
		bAdd.addActionListener(this);
		bAddAll.addActionListener(this);
		bDelete.addActionListener(this);
		bDeleteAll.addActionListener(this);
		northPanel.setLayout (northLayout);
		northLayout.setAlignment (FlowLayout.LEFT);
		//
		this.add (northPanel, BorderLayout.NORTH);
		northPanel.add (treeLabel, null);
		northPanel.add (treeField, null);
		northPanel.add (cbAllNodes, null);
		northPanel.add (treeInfo, null);
		northPanel.add (bAddAll, null);
		northPanel.add (bAdd, null);
		northPanel.add (bDelete, null);
		northPanel.add (bDeleteAll, null);
		//
		this.add (splitPane, BorderLayout.CENTER);
		splitPane.add (centerTree, JSplitPane.LEFT);
		splitPane.add (new JScrollPane(centerList), JSplitPane.RIGHT);
		centerList.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
		centerList.addListSelectionListener(this);
	}	//	jbInit

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
	 * 	Action Listener
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == treeField)
			action_loadTree();
		else if (e.getSource() == bAddAll)
			action_treeAddAll();
		else if (e.getSource() == bAdd)
			action_treeAdd((ListItem)centerList.getSelectedValue());
		else if (e.getSource() == bDelete)
			action_treeDelete((ListItem)centerList.getSelectedValue());
		else if (e.getSource() == bDeleteAll)
			action_treeDeleteAll();
	}	//	actionPerformed

	
	/**
	 * 	Action: Fill Tree with all nodes
	 */
	private void action_loadTree()
	{
		KeyNamePair tree = (KeyNamePair)treeField.getSelectedItem();
		log.info("Tree=" + tree);
		if (tree.getKey() <= 0)
		{
			centerList.setModel(new DefaultListModel());
			return;
		}
		//	Tree
		m_tree = new MTree (Env.getCtx(), tree.getKey(), null);
		cbAllNodes.setSelected(m_tree.isAllNodes());
		bAddAll.setEnabled(!m_tree.isAllNodes());
		bAdd.setEnabled(!m_tree.isAllNodes());
		bDelete.setEnabled(!m_tree.isAllNodes());
		bDeleteAll.setEnabled(!m_tree.isAllNodes());
		//
		String fromClause = m_tree.getSourceTableName(false);	//	fully qualified
		String columnNameX = m_tree.getSourceTableName(true);
		String actionColor = m_tree.getActionColorName();
		//	List
		DefaultListModel model = new DefaultListModel();
		String sql = "SELECT t." + columnNameX 
			+ "_ID,t.Name,t.Description,t.IsSummary,"
			+ actionColor
			+ " FROM " + fromClause
		//	+ " WHERE t.IsActive='Y'"	//	R/O
			+ " ORDER BY 2";
		sql = MRole.getDefault().addAccessSQL(sql, 
			"t", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		log.config(sql);
		//	
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				ListItem item = new ListItem(rs.getInt(1), rs.getString(2),
					rs.getString(3), "Y".equals(rs.getString(4)), rs.getString(5));
				model.addElement(item);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		//	List
		log.config("#" + model.getSize());
		centerList.setModel(model);
		//	Tree
		centerTree.initTree(m_tree.getAD_Tree_ID());
	}	//	action_fillTree
	
	/**
	 * 	List Selection Listener
	 *	@param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;
		ListItem selected = null;
		try
		{	//	throws a ArrayIndexOutOfBoundsException if root is selected
			selected = (ListItem)centerList.getSelectedValue();
		}
		catch (Exception ex)
		{
		}
		log.info("Selected=" + selected);
		if (selected != null)	//	allow add if not in tree
			bAdd.setEnabled(!centerTree.setSelectedNode(selected.id));
	}	//	valueChanged
	
	/**
	 * 	VTreePanel Changed
	 *	@param e event
	 */
	public void propertyChange (PropertyChangeEvent e)
	{
		MTreeNode tn = (MTreeNode)e.getNewValue();
		log.info(tn.toString());
		if (tn == null)
			return;
		ListModel model = centerList.getModel();
		int size = model.getSize();
		int index = -1;
		for (index = 0; index < size; index++)
		{
			ListItem item = (ListItem)model.getElementAt(index);
			if (item.id == tn.getNode_ID())
				break;
		}
		centerList.setSelectedIndex(index);
	}	//	propertyChange

	/**
	 * 	Action: Add Node to Tree
	 */
	private void action_treeAdd(ListItem item)
	{
		log.info("Item=" + item);
		if (item != null)
		{
			centerTree.nodeChanged(true, item.id, item.name, 
				item.description, item.isSummary, item.imageIndicator);
			//	May cause Error if in tree
			if (m_tree.isProduct())
			{
				MTree_NodePR node = new MTree_NodePR (m_tree, item.id);
				node.save();
			}
			else if (m_tree.isBPartner())
			{
				MTree_NodeBP node = new MTree_NodeBP (m_tree, item.id);
				node.save();
			}
			else if (m_tree.isMenu())
			{
				MTree_NodeMM node = new MTree_NodeMM (m_tree, item.id);
				node.save();
			}
			else
			{
				MTree_Node node = new MTree_Node (m_tree, item.id);
				node.save();
			}
		}
	}	//	action_treeAdd
	
	/**
	 * 	Action: Delete Node from Tree
	 */
	private void action_treeDelete(ListItem item)
	{
		log.info("Item=" + item);
		if (item != null)
		{
			centerTree.nodeChanged(false, item.id, item.name, 
				item.description, item.isSummary, item.imageIndicator);
			//
			if (m_tree.isProduct())
			{
				MTree_NodePR node = MTree_NodePR.get (m_tree, item.id);
				if (node != null)
					node.delete(true);
			}
			else if (m_tree.isBPartner())
			{
				MTree_NodeBP node = MTree_NodeBP.get (m_tree, item.id);
				if (node != null)
					node.delete(true);
			}
			else if (m_tree.isMenu())
			{
				MTree_NodeMM node = MTree_NodeMM.get (m_tree, item.id);
				if (node != null)
					node.delete(true);
			}
			else
			{
				MTree_Node node = MTree_Node.get (m_tree, item.id);
				if (node != null)
					node.delete(true);
			}
		}
	}	//	action_treeDelete

	
	/**
	 * 	Action: Add All Nodes to Tree
	 */
	private void action_treeAddAll()
	{
		log.info("");
		ListModel model = centerList.getModel();
		int size = model.getSize();
		int index = -1;
		for (index = 0; index < size; index++)
		{
			ListItem item = (ListItem)model.getElementAt(index);
			action_treeAdd(item);
		}
	}	//	action_treeAddAll
	
	/**
	 * 	Action: Delete All Nodes from Tree
	 */
	private void action_treeDeleteAll()
	{
		log.info("");
		ListModel model = centerList.getModel();
		int size = model.getSize();
		int index = -1;
		for (index = 0; index < size; index++)
		{
			ListItem item = (ListItem)model.getElementAt(index);
			action_treeDelete(item);
		}
	}	//	action_treeDeleteAll
	
	/**************************************************************************
	 * 	Tree Maintenance List Item
	 */
	class ListItem
	{
		public ListItem (int id, String name, String description, boolean isSummary, String imageIndicator)
		{
			this.id = id;
			this.name = name;
			this.description = description;
			this.isSummary = isSummary;
			this.imageIndicator = imageIndicator;
		}	//	ListItem
		
		public int id;
		public String name;
		public String description;
		public boolean isSummary;
		public String imageIndicator;  //  Menu - Action
		
		/**
		 * 	To String
		 *	@return	String Representation
		 */
		public String toString ()
		{
			String retValue = name;
			if (description != null && description.length() > 0)
				retValue += " (" + description + ")";
			return retValue;
		}	//	toString
		
	}	//	ListItem

}	//	VTreeMaintenance
