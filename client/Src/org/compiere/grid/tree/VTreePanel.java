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
package org.compiere.grid.tree;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.tree.*;
import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *  Tree Panel displays trees.
 *  <br>
 *	When a node is selected, a propertyChange (NODE_SELECTION) event is fired
 *  <pre>
 *		PropertyChangeListener -
 *			treePanel.addPropertyChangeListener(VTreePanel.NODE_SELECTION, this);
 *			calls: public void propertyChange(PropertyChangeEvent e)
 *  </pre>
 *  To select a specific node call
 *      setSelectedNode(NodeID);
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VTreePanel.java,v 1.38 2005/12/09 05:17:57 jjanke Exp $
 */
public final class VTreePanel extends CPanel
	implements ActionListener, DragGestureListener, DragSourceListener, DropTargetListener
{
	/**
	 *  Tree Panel for browsing and editing of a tree.
	 *  Need to call initTree
	 *  @param  WindowNo	WindowNo
	 *  @param  editable    if true you can edit it
	 *  @param  hasBar      has OutlookBar
	 */
	public VTreePanel(int WindowNo, boolean hasBar, boolean editable)
	{
		super();
		log.config("Bar=" + hasBar + ", Editable=" + editable);
		m_WindowNo = WindowNo;
		m_hasBar = hasBar;
		m_editable = editable;

		//	static init
		jbInit();
		if (!hasBar)
		{
			bar.setPreferredSize(new Dimension(0,0));
			centerSplitPane.setDividerLocation(0);
			centerSplitPane.setDividerSize(0);
			popMenuTree.remove(mBarAdd);
		}
		else
			centerSplitPane.setDividerLocation(80);
		//  base settings
		if (editable)
			tree.setDropTarget(dropTarget);
		else
		{
			popMenuTree.remove(mFrom);
			popMenuTree.remove(mTo);
		}
	}   //  VTreePanel

	/**
	 *  Tree initialization.
	 * 	May be called several times
	 *	@param	AD_Tree_ID	tree to load
	 *  @return true if loaded ok
	 */
	public boolean initTree (int AD_Tree_ID)
	{
		log.config("AD_Tree_ID=" + AD_Tree_ID);
		//
		m_AD_Tree_ID = AD_Tree_ID;

		//  Get Tree
		MTree vTree = new MTree (Env.getCtx(), AD_Tree_ID, m_editable, true, null);
		m_root = vTree.getRoot();
		log.config("root=" + m_root);
		m_nodeTableName = vTree.getNodeTableName();
		treeModel = new DefaultTreeModel(m_root, true);
		tree.setModel(treeModel);

		//  Shortcut Bar
		if (m_hasBar)
		{
			bar.removeAll();	//	remove all existing buttons
			Enumeration en = m_root.preorderEnumeration();
			while (en.hasMoreElements())
			{
				MTreeNode nd = (MTreeNode)en.nextElement();
				if (nd.isOnBar())
					addToBar(nd);
			}
		}

		return true;
	}   //  initTree

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VTreePanel.class);

	private BorderLayout mainLayout = new BorderLayout();
	private JTree tree = new JTree();
	private DefaultTreeModel treeModel;
	private DefaultTreeSelectionModel treeSelect = new DefaultTreeSelectionModel();
	private CPanel southPanel = new CPanel();
	private CCheckBox treeExpand = new CCheckBox();
	private CTextField treeSearch = new CTextField(10);
	private CLabel treeSearchLabel = new CLabel();
	private JPopupMenu popMenuTree = new JPopupMenu();
	private JPopupMenu popMenuBar = new JPopupMenu();
	private CMenuItem mFrom = new CMenuItem();
	private CMenuItem mTo = new CMenuItem();
	private CPanel bar = new CPanel();
	private CMenuItem mBarAdd = new CMenuItem();
	private CMenuItem mBarRemove = new CMenuItem();
	private BorderLayout southLayout = new BorderLayout();
	private JSplitPane centerSplitPane = new JSplitPane();
	private JScrollPane treePane = new JScrollPane();
	private MouseListener mouseListener = new VTreePanel_mouseAdapter(this);
	private KeyListener keyListener = new VTreePanel_keyAdapter(this);

	//
	private int			m_WindowNo;
	/** Tree ID                     */
	private int			m_AD_Tree_ID = 0;
	/** Table Name for TreeNode     */
	private String      m_nodeTableName = null;
	/** Tree is editable (can move nodes) - also not active shown   */
	private boolean     m_editable;
	/** Tree has a shortcut Bar     */
	private boolean     m_hasBar;
	/** The root node               */
	private MTreeNode  	m_root = null;


	private MTreeNode   m_moveNode;    	//	the node to move
	private String      m_search = "";
	private Enumeration m_nodeEn;
	private MTreeNode   m_selectedNode;	//	the selected model node
	private CButton     m_buttonSelected;

	//	Property Listener
	public static final String NODE_SELECTION = "NodeSelected";

	/**
	 *  Static Component initialization.
	 *  <pre>
	 *  - centerSplitPane
	 *      - treePane
	 *          - tree
	 *      - bar
	 *  - southPanel
	 *  </pre>
	 */
	private void jbInit()
	{
		this.setLayout(mainLayout);
		mainLayout.setVgap(5);
		//
		//  only one node to be selected
		treeSelect.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setSelectionModel(treeSelect);
		//
		tree.setEditable(false);		            //	allows to change the text
		tree.addMouseListener(mouseListener);
		tree.addKeyListener(keyListener);
		tree.setCellRenderer(new VTreeCellRenderer());
		treePane.getViewport().add(tree, null);
//		treePane.setPreferredSize(new Dimension(50,200));
//		tree.setPreferredSize(new Dimension(100,150));
		//
		treeExpand.setText(Msg.getMsg(Env.getCtx(), "ExpandTree"));
		treeExpand.setActionCommand("Expand");
		treeExpand.addMouseListener(mouseListener);
		treeExpand.addActionListener(this);
		//
		treeSearchLabel.setText(Msg.getMsg(Env.getCtx(), "TreeSearch") + " ");
		treeSearchLabel.setLabelFor(treeSearch);
		treeSearchLabel.setToolTipText(Msg.getMsg(Env.getCtx(), "TreeSearchText"));

		treeSearch.setBackground(CompierePLAF.getInfoBackground());
		treeSearch.addKeyListener(keyListener);
		southPanel.setLayout(southLayout);
		southPanel.add(treeExpand, BorderLayout.WEST);
		southPanel.add(treeSearchLabel, BorderLayout.CENTER);
		southPanel.add(treeSearch, BorderLayout.EAST);
		this.add(southPanel, BorderLayout.SOUTH);
		//
		centerSplitPane.add(treePane, JSplitPane.RIGHT);
		centerSplitPane.add(bar, JSplitPane.LEFT);
		this.add(centerSplitPane, BorderLayout.CENTER);
		//
		mFrom.setText(Msg.getMsg(Env.getCtx(), "ItemMove"));
		mFrom.setActionCommand("From");
		mFrom.addActionListener(this);
		mTo.setEnabled(false);
		mTo.setText(Msg.getMsg(Env.getCtx(), "ItemInsert"));
		mTo.setActionCommand("To");
		mTo.addActionListener(this);
		//
		bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));
		bar.setMinimumSize(new Dimension (50,50));

		mBarAdd.setText(Msg.getMsg(Env.getCtx(), "BarAdd"));
		mBarAdd.setActionCommand("BarAdd");
		mBarAdd.addActionListener(this);
		mBarRemove.setText(Msg.getMsg(Env.getCtx(), "BarRemove"));
		mBarRemove.setActionCommand("BarRemove");
		mBarRemove.addActionListener(this);
		//
		popMenuTree.setLightWeightPopupEnabled(false);
		popMenuTree.add(mBarAdd);
		popMenuTree.addSeparator();
		popMenuTree.add(mFrom);
		popMenuTree.add(mTo);
		popMenuBar.setLightWeightPopupEnabled(false);
		popMenuBar.add(mBarRemove);
	}   //  jbInit


	/**
	 * 	Set Divider Location
	 *	@param location location (80 default)
	 */
	public void setDividerLocation(int location)
	{
		centerSplitPane.setDividerLocation(location);
	}	//	setDividerLocation
	
	/**
	 * 	Get Divider Location
	 *	@return divider location
	 */
	public int getDividerLocation()
	{
		return centerSplitPane.getDividerLocation();
	}	//	getDividerLocation
	
	
	/*************************************************************************
	 *	Drag & Drop
	 */
	protected DragSource dragSource
		= DragSource.getDefaultDragSource();
	protected DropTarget dropTarget
		= new DropTarget(tree, DnDConstants.ACTION_MOVE, this, true, null);
	protected DragGestureRecognizer recognizer
		= dragSource.createDefaultDragGestureRecognizer(tree, DnDConstants.ACTION_MOVE, this);


	/**
	 *	Drag Gesture Interface	** Start **
	 *  @param e event
	 */
	public void dragGestureRecognized(DragGestureEvent e)
	{
		if (!m_editable)
			return;
		//
		try
		{
			m_moveNode = (MTreeNode)tree.getSelectionPath().getLastPathComponent();
		}
		catch (Exception ex)	//	nothing selected
		{
			return;
		}
		//	start moving
		StringSelection content = new StringSelection(m_moveNode.toString());
		e.startDrag(DragSource.DefaultMoveDrop,		//	cursor
					content,						//	Transferable
					this);
		log.fine( "Drag: " + m_moveNode.toString());
	}	//	dragGestureRecognized


	/**
	 *	DragSourceListener interface
	 *  @param e event
	 */
	public void dragDropEnd(DragSourceDropEvent e)	{}
	public void dragEnter(DragSourceDragEvent e)	{}
	public void dragExit(DragSourceEvent e)	{}
	public void dragOver(DragSourceDragEvent e)	{}
	public void dropActionChanged(DragSourceDragEvent e)	{}

	/**
	 *	DropTargetListener interface
	 *  @param e event
	 */
	public void dragEnter(DropTargetDragEvent e)
	{
		e.acceptDrag(DnDConstants.ACTION_MOVE);
	}
	public void dropActionChanged(DropTargetDragEvent e)	{}
	public void dragExit(DropTargetEvent e)	{}


	/**
	 *	Drag over 				** Between **
	 *  @param e event
	 */
	public void dragOver(DropTargetDragEvent e)
	{
		Point mouseLoc = e.getLocation(); 	//	where are we?
		TreePath path = tree.getClosestPathForLocation(mouseLoc.x, mouseLoc.y);
		tree.setSelectionPath(path);		//	show it by selecting
		MTreeNode toNode = (MTreeNode)path.getLastPathComponent();
		//
	//	log.fine( "Move: " + toNode);
		if (m_moveNode == null				//	nothing to move
			||	toNode == null)				//	nothing to drop on
			e.rejectDrag();
		else
			e.acceptDrag(DnDConstants.ACTION_MOVE);
	}	//	dragOver


	/**
	 *	Drop					** End **
	 *  @param e event
	 */
	public void drop(DropTargetDropEvent e)
	{
		Point mouseLoc = e.getLocation(); 	//	where are we?
		TreePath path = tree.getClosestPathForLocation(mouseLoc.x, mouseLoc.y);
		tree.setSelectionPath(path);		//	show it by selecting
		MTreeNode toNode = (MTreeNode)path.getLastPathComponent();
		//
		log.fine( "Drop: " + toNode);
		if (m_moveNode == null				//	nothing to move
			||	toNode == null)				//	nothing to drop on
		{
			e.rejectDrop();
			return;
		}
		//
		e.acceptDrop(DnDConstants.ACTION_MOVE);
		moveNode(m_moveNode, toNode);

		e.dropComplete(true);
		m_moveNode = null;
	}	//	drop


	/**
	 *	Move TreeNode
	 *	@param	movingNode	The node to be moved
	 *	@param	toNode		The target node
	 */
	private void moveNode(MTreeNode movingNode, MTreeNode toNode)
	{
		log.info(movingNode.toString() + " to " + toNode.toString());

		if (movingNode == toNode)
			return;

		//  remove
		MTreeNode oldParent = (MTreeNode)movingNode.getParent();
		movingNode.removeFromParent();
		treeModel.nodeStructureChanged(oldParent);

		//  insert
		MTreeNode newParent;
		int index;
		if (!toNode.isSummary())	//	drop on a child node
		{
			newParent = (MTreeNode)toNode.getParent();
			index = newParent.getIndex(toNode) + 1;	//	the next node
		}
		else									//	drop on a summary node
		{
			newParent = toNode;
			index = 0;                   			//	the first node
		}
		newParent.insert(movingNode, index);
		treeModel.nodeStructureChanged(newParent);

		//	***	Save changes to disk
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Trx trx = Trx.get ("VTreePanel", true);
		try
		{
                        //begin vpj-cd e-evolution 07/12/2005 PostgreSQL
			//Statement stmt = trx.getConnection().createStatement();
                        //end vpj-cd e-evolution 07/12/2005 PostgreSQL
			//	START TRANSACTION   **************
			for (int i = 0; i < oldParent.getChildCount(); i++)
			{
				MTreeNode nd = (MTreeNode)oldParent.getChildAt(i);
				StringBuffer sql = new StringBuffer("UPDATE ");
				sql.append(m_nodeTableName)
					.append(" SET Parent_ID=").append(oldParent.getNode_ID())
					.append(", SeqNo=").append(i)
					.append(", Updated=SysDate")
					.append(" WHERE AD_Tree_ID=").append(m_AD_Tree_ID)
					.append(" AND Node_ID=").append(nd.getNode_ID());
				log.fine(sql.toString());				
                                //begin vpj-cd e-evolution 07/12/2005 PostgreSQL       
				//stmt.executeUpdate(sql.toString());
                                DB.executeUpdate(sql.toString(),trx.getTrxName());
                                //end vpj-cd e-evolution 07/12/2005 PostgreSQL  
			}
			if (oldParent != newParent)
				for (int i = 0; i < newParent.getChildCount(); i++)
				{
					MTreeNode nd = (MTreeNode)newParent.getChildAt(i);
					StringBuffer sql = new StringBuffer("UPDATE ");
					sql.append(m_nodeTableName)
						.append(" SET Parent_ID=").append(newParent.getNode_ID())
						.append(", SeqNo=").append(i)
						.append(", Updated=SysDate")
						.append(" WHERE AD_Tree_ID=").append(m_AD_Tree_ID)
						.append(" AND Node_ID=").append(nd.getNode_ID());
                                        //begin vpj-cd e-evolution 07/12/2005 PostgreSQL     
					//stmt.executeUpdate(sql.toString());
                                        DB.executeUpdate(sql.toString(),trx.getTrxName());
                                        //end vpj-cd e-evolution 07/12/2005 PostgreSQL    
				}
			//	COMMIT          *********************
			trx.commit();
                        //begin vpj-cd e-evolution 07/12/2005 PostgreSQL     
			//stmt.close();
                        //end vpj-cd e-evolution 07/12/2005 PostgreSQL  
		}
                ///begin vpj-cd e-evolution 07/12/2005 PostgreSQL 
		//catch (SQLException e)
                catch (Exception e)
                //end vpj-cd e-evolution 07/12/2005 PostgreSQL 
		{
			trx.rollback();
			log.log(Level.SEVERE, "move", e);
			ADialog.error(m_WindowNo, this, "TreeUpdateError", e.getLocalizedMessage());
		}
		trx.close();
		trx = null;
		setCursor(Cursor.getDefaultCursor());
		log.config("complete");
	}	//	moveNode


	/*************************************************************************/

	/**
	 *  Enter Key
	 *  @param e event
	 */
	protected void keyPressed(KeyEvent e)
	{
		//  *** Tree ***
		if (e.getSource() instanceof JTree
			|| (e.getSource() == treeSearch && e.getModifiers() != 0))	//	InputEvent.CTRL_MASK
		{
			TreePath tp = tree.getSelectionPath();
			if (tp == null)
				ADialog.beep();
			else
			{
				MTreeNode tn = (MTreeNode)tp.getLastPathComponent();
				setSelectedNode(tn);
			}
		}

		//  *** treeSearch ***
		else if (e.getSource() == treeSearch)
		{
			String search = treeSearch.getText();
			boolean found = false;

			//  at the end - try from top
			if (m_nodeEn != null && !m_nodeEn.hasMoreElements())
				m_search = "";

			//  this is the first time
			if (!search.equals(m_search))
			{
				//  get enumeration of all nodes
				m_nodeEn = m_root.preorderEnumeration();
				m_search = search;
			}

			//  search the nodes
			while(!found && m_nodeEn != null && m_nodeEn.hasMoreElements())
			{
				MTreeNode nd = (MTreeNode)m_nodeEn.nextElement();
				//	compare in upper case
				if (nd.toString().toUpperCase().indexOf(search.toUpperCase()) != -1)
				{
					found = true;
					TreePath treePath = new TreePath(nd.getPath());
					tree.setSelectionPath(treePath);
					tree.makeVisible(treePath);			//	expand it
					tree.scrollPathToVisible(treePath);
				}
			}
			if (!found)
				ADialog.beep();
		}   //  treeSearch

	}   //  keyPressed


	/*************************************************************************/

	/**
	 *  Mouse clicked
	 *  @param e event
	 */
	protected void mouseClicked(MouseEvent e)
	{
		//  *** JTree ***
		if (e.getSource() instanceof JTree)
		{
			//  Left Double Click
			if (SwingUtilities.isLeftMouseButton(e)
				&& e.getClickCount() > 0)
			{
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				if(selRow != -1)
				{
					MTreeNode tn = (MTreeNode)tree.getPathForLocation
						(e.getX(), e.getY()).getLastPathComponent();
					setSelectedNode(tn);
				}
			}

			//  Right Click for PopUp
			else if ((m_editable || m_hasBar)
				&& SwingUtilities.isRightMouseButton(e)
				&& tree.getSelectionPath() != null)         //  need select first
			{
				MTreeNode nd = (MTreeNode)tree.getSelectionPath().getLastPathComponent();
			//	if (nd.isLeaf())                    //  only leaves
				{
					Rectangle r = tree.getPathBounds(tree.getSelectionPath());
					popMenuTree.show(tree, (int)r.getMaxX(), (int)r.getY());
				}
			}
		}   //  JTree

		//  *** JButton ***
		else if (e.getSource() instanceof JButton)
		{
			if (SwingUtilities.isRightMouseButton(e))
			{
				m_buttonSelected = (CButton)e.getSource();
				popMenuBar.show(m_buttonSelected, e.getX(), e.getY());
			}
		}   //  JButton

	}   //  mouseClicked


	/**
	 *  Get currently selected node
	 *  @return MTreeNode
	 */
	public MTreeNode getSelectedNode()
	{
		return m_selectedNode;
	}   //  getSelectedNode

	/**
	 *  Search Field
	 *  @return Search Field
	 */
	public JComponent getSearchField()
	{
		return treeSearch;
	}   //  getSearchField

	/**
	 *  Set Selection to Node in Event
	 *  @param nodeID Node ID
	 * 	@return true if selected
	 */
	public boolean setSelectedNode (int nodeID)
	{
		log.config("ID=" + nodeID);
		if (nodeID != -1)				//	new is -1
			return selectID(nodeID, true);     //  show selection
		return false;
	}   //  setSelectedNode

	/**
	 *  Select ID in Tree
	 *  @param nodeID	Node ID
	 *  @param show	scroll to node
	 * 	@return true if selected
	 */
	private boolean selectID (int nodeID, boolean show)
	{
		if (m_root == null)
			return false;
		log.config("NodeID=" + nodeID 
			+ ", Show=" + show + ", root=" + m_root);
		//  try to find the node
		MTreeNode node = m_root.findNode (nodeID);
		if (node != null)
		{
			TreePath treePath = new TreePath(node.getPath());
			log.config("Node=" + node 
				+ ", Path=" + treePath.toString());
			tree.setSelectionPath(treePath);
			if (show)
			{
				tree.makeVisible(treePath);       	//	expand it
				tree.scrollPathToVisible(treePath);
			}
			return true;
		}
		log.info("Node not found; ID=" + nodeID);
		return false;
	}   //  selectID


	/**
	 *  Set the selected node & initiate all listeners
	 *  @param nd node
	 */
	private void setSelectedNode (MTreeNode nd)
	{
		log.config("Node = " + nd);
		m_selectedNode = nd;
		//
		firePropertyChange(NODE_SELECTION, null, nd);
	}   //  setSelectedNode

	/*************************************************************************/

	/**
	 *  Node Changed - synchromize Node
	 *
	 *  @param  save    true the node was saved (changed/added), false if the row was deleted
	 *  @param  keyID   the ID of the row changed
	 *  @param  name	name
	 *  @param  description	description
	 *  @param  isSummary	summary node
	 *  @param  imageIndicator image indicator
	 */
	public void nodeChanged (boolean save, int keyID,
		String name, String description, boolean isSummary, String imageIndicator)
	{
		log.config("Save=" + save + ", KeyID=" + keyID
			+ ", Name=" + name + ", Description=" + description 
			+ ", IsSummary=" + isSummary + ", ImageInd=" + imageIndicator
			+ ", root=" + m_root);
		//	if ID==0=root - don't update it
		if (keyID == 0)
			return;	
			
		//  try to find the node
		MTreeNode node = m_root.findNode(keyID);

		//  Node not found and saved -> new
		if (node == null && save)
		{
			node = new MTreeNode (keyID, 0, name, description,
				m_root.getNode_ID(), isSummary, imageIndicator, false, null);
			m_root.add (node);
		}

		//  Node found and saved -> change
		else if (node != null && save)
		{
			node.setName (name);
			node.setAllowsChildren(isSummary);
		}

		//  Node found and not saved -> delete
		else if (node != null && !save)
		{
			MTreeNode parent = (MTreeNode)node.getParent();
			node.removeFromParent();
			node = parent;  //  select Parent
		}

		//  Error
		else
		{
			log.log(Level.SEVERE, "Save=" + save + ", KeyID=" + keyID + ", Node=" + node);
			node = null;
		}

		//  Nothing to display
		if (node == null)
			return;

		//  (Re) Display Node
		tree.updateUI();
		TreePath treePath = new TreePath(node.getPath());
		tree.setSelectionPath(treePath);
		tree.makeVisible(treePath);       	//	expand it
		tree.scrollPathToVisible(treePath);
	}   //  nodeChanged


	/*************************************************************************/

	/**
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		//  bar button pressed
		if (e.getSource() instanceof JButton)
		{
			//  Find Node - don't show
			selectID(Integer.parseInt(e.getActionCommand()), false);
			//  Select it
			MTreeNode tn = (MTreeNode)tree.getSelectionPath().getLastPathComponent();
			setSelectedNode(tn);
		}

		//  popup menu commands
		else if (e.getSource() instanceof JMenuItem)
		{
			if (e.getActionCommand().equals("From"))
				moveFrom();
			else if (e.getActionCommand().equals("To"))
				moveTo();
			else if (e.getActionCommand().equals("BarAdd"))
				barAdd();
			else if (e.getActionCommand().equals("BarRemove"))
				barRemove();
		}

		else if (e.getSource() instanceof JCheckBox)
		{
			if (e.getActionCommand().equals("Expand"))
				expandTree();
		}
	}   //  actionPerformed


	/*************************************************************************/

	/**
	 *  Copy Node into buffer
	 */
	private void moveFrom()
	{
		m_moveNode = (MTreeNode)tree.getSelectionPath().getLastPathComponent();
		if (m_moveNode != null)
			mTo.setEnabled(true);		//	enable menu
	}   //  mFrom_actionPerformed

	/**
	 *  Move Node
	 */
	private void moveTo()
	{
		mFrom.setEnabled(true);
		mTo.setEnabled(false);
		if (m_moveNode == null)
			return;

		MTreeNode toNode = (MTreeNode)tree.getSelectionPath().getLastPathComponent();
		moveNode(m_moveNode, toNode);
		//	cleanup
		m_moveNode = null;
	}   //  mTo_actionPerformed

	/**
	 *  Add selected TreeNode to Bar
	 */
	private void barAdd()
	{
		MTreeNode nd = (MTreeNode)tree.getSelectionPath().getLastPathComponent();
		if (barDBupdate(true, nd.getNode_ID()))
			addToBar(nd);
	}   //  barAdd

	/**
	 *  Add TreeNode to Bar
	 *  @param nd node
	 */
	private void addToBar(MTreeNode nd)
	{
		//	Only first word of Label
		String label = nd.toString().trim();
		int space = label.indexOf(" ");
	//	if (space != -1)
	//		label = label.substring(0, space);

		CButton button = new CButton(label);		//	Create the button
		button.setToolTipText(nd.getDescription());
		button.setActionCommand(String.valueOf(nd.getNode_ID()));
		//
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setIcon(nd.getIcon());
		button.setBorderPainted(false);
	//	button.setFocusPainted(false);
		button.setRequestFocusEnabled(false);
		//
		button.addActionListener(this);
		button.addMouseListener(mouseListener);
		//
		bar.add(button);
		bar.validate();
		if (centerSplitPane.getDividerLocation() == -1)
			centerSplitPane.setDividerLocation(button.getPreferredSize().width);
		bar.repaint();
	}   //  addToBar

	/**
	 *  Remove from Bar
	 */
	private void barRemove()
	{
		bar.remove(m_buttonSelected);
		bar.validate();
		bar.repaint();
		barDBupdate(false, Integer.parseInt(m_buttonSelected.getActionCommand()));
	}   //  barRemove

	/**
	 *	Make Bar add/remove persistent
	 *  @param add true if add - otherwise remove
	 *  @param Node_ID Node ID
	 */
	private boolean barDBupdate (boolean add, int Node_ID)
	{
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		int AD_Org_ID = Env.getContextAsInt(Env.getCtx(), "#AD_Org_ID");
		int AD_User_ID = Env.getContextAsInt(Env.getCtx(), "#AD_User_ID");
		StringBuffer sql = new StringBuffer();
		if (add)
			sql.append("INSERT INTO AD_TreeBar "
				+ "(AD_Tree_ID,AD_User_ID,Node_ID, "
				+ "AD_Client_ID,AD_Org_ID, "
				+ "IsActive,Created,CreatedBy,Updated,UpdatedBy)VALUES (")
				.append(m_AD_Tree_ID).append(",").append(AD_User_ID).append(",").append(Node_ID).append(",")
				.append(AD_Client_ID).append(",").append(AD_Org_ID).append(",")
				.append("'Y',SysDate,").append(AD_User_ID).append(",SysDate,").append(AD_User_ID).append(")");
			//	if already exist, will result in ORA-00001: unique constraint (COMPIERE.AD_TREEBAR_KEY)
		else
			sql.append("DELETE AD_TreeBar WHERE AD_Tree_ID=").append(m_AD_Tree_ID)
				.append(" AND AD_User_ID=").append(AD_User_ID)
				.append(" AND Node_ID=").append(Node_ID);
		int no = DB.executeUpdate(sql.toString(), true, null);
		return no == 1;
	}	//	barDBupdate


	/**
	 *  Clicked on Expand All
	 */
	private void expandTree()
	{
		if (treeExpand.isSelected())
		{
			for (int row = 0; row < tree.getRowCount(); row++)
				tree.expandRow(row);
		}
		else
		{
			for (int row = 0; row < tree.getRowCount(); row++)
				tree.collapseRow(row);
		}
	}   //  expandTree

}   //  VTreePanel

/*****************************************************************************/

/**
 *  Mouse Clicked
 */
class VTreePanel_mouseAdapter extends java.awt.event.MouseAdapter
{
	VTreePanel adaptee;

	VTreePanel_mouseAdapter(VTreePanel adaptee)
	{
		this.adaptee = adaptee;
	}

	public void mouseClicked(MouseEvent e)
	{
		adaptee.mouseClicked(e);
	}
}   //  VTreePanel_mouseAdapter

/**
 *  Key Pressed
 */
class VTreePanel_keyAdapter extends java.awt.event.KeyAdapter
{
	VTreePanel adaptee;

	VTreePanel_keyAdapter(VTreePanel adaptee)
	{
		this.adaptee = adaptee;
	}

	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			adaptee.keyPressed(e);
	}
}   //  VTreePanel_keyAdapter
