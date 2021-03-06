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
package org.compiere.grid;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.compiere.apps.*;
import org.compiere.grid.ed.*;
import org.compiere.grid.tree.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  The Grid Controller is the panel for single and multi-row presentation
 *  and links to the Model Tab.
 *
 *  <pre>
 *  UI Structure:
 *  this    (BorderLayout)
 *      splitPane (JSplitPane)
 *          left
 *              graphicPanel
 *          right
 *              cardPanel   JPanel  (CardLayout)
 *                  srPane  JSplitPane
 * 						vPane	JScrollPane
 *              	        vPanel  VPanel (GridBagLayout)
 * 						vIncludedGC	GridController
 *                  mrPane  JScrollPane
 *                      vTable  VTable
 *
 *  <B>DataBinding:<B>
 *  - MultiRow - is automatic between VTable and MTable
 *  - SingleRow
 *		- from VEditors via fireVetoableChange(m_columnName, null, getText());
 *			(vetoableChange)
 *		- to VEditors via updateSingleRow -> Editor.setValue(object)
 *
 *  Event Chains
 *  -- Navigation --
 *  (VTable selection -> GridController.valueChanged)
 *  (APanel selection)
 *      + MTab.navivate
 *          + MTab.setCurrentRow
 *              + Update all MFields
 *                  + MField.setValue
 *                      + setContext
 *                      + fire PropertyChange "Value"
 *                          + VEditor.propertyChange
 *                              + VEditor.setValue
 *              + MTab.fireProperyChange "CurrentRow"
 *                  + VTable.propertyChange (setRowSelectionInterval)
 *                      + GridController.valueChange
 *                          + GridController.dynamicDisplay(complete)
 *              + MTab.fireDataStatusChanged
 *                  + APanel.statusChanged
 *
 *  -- ValueChanges --
 *  VEditor.fireVetoableChange
 *      + (VCellEditor.vetoableChange/getCellEditorValue)   -- multi-row source
 *      + (GridController.vetoableChange)                   -- single-row source
 *          + MTable.setValueAt
 *              + MField.setValue
 *                  + setContext
 *                  + fire PropertyChange "Value"
 *                      + VEditor.setValue
 *              + MTable.fireDataStatusChanged
 *                  + MTab.dataStatusChanged
 *                      + MTab.fireDataStatusChanged
 *                          + APanel.statusChanged
 *                  + GridController.dataStatusChanged
 *                      + GridController.dynamicDisplay(selective)
 *  </pre>
 * @author  Jorg Janke
 * @version $Id: GridController.java,v 1.73 2006/02/13 02:41:05 jjanke Exp $
 */
public class GridController extends CPanel
	implements DataStatusListener, ListSelectionListener, 
		VetoableChangeListener,	PropertyChangeListener, MouseListener
{
	/**
	 *  Constructor - you need to call initGrid for instanciation
	 */
	public GridController()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}   //  GridController

	/**
	 *  toString
	 *  @return string representation
	 */
	public String toString()
	{
		return "GridController for " + m_mTab;
	}   //  toString

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(GridController.class);
	
	/**
	 *  The Layout
	 */
	private BorderLayout mainLayout = new BorderLayout();
	private JSplitPane splitPane = new JSplitPane();
	private CPanel graphPanel = new CPanel();
	private BorderLayout graphLayout = new BorderLayout();
	private CPanel cardPanel = new CPanel();
	private CardLayout cardLayout = new CardLayout();
	private JSplitPane srPane = new JSplitPane();
	private JScrollPane vPane = new JScrollPane();
	private GridController vIncludedGC = null;
	private CScrollPane mrPane = new CScrollPane();
	private CPanel xPanel = new CPanel();
	private FlowLayout xLayout = new FlowLayout();
	private VTable vTable = new VTable();
	private VPanel vPanel = new VPanel();

	/**
	 *  Static Layout init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setLayout(mainLayout);
		this.add(splitPane, BorderLayout.CENTER);
		splitPane.setOpaque(false);
		graphPanel.setLayout(graphLayout);
		//
		splitPane.add(graphPanel, JSplitPane.LEFT);
		splitPane.add(cardPanel, JSplitPane.RIGHT);
		splitPane.setBorder(null);
		splitPane.setName("gc_splitPane");
		//
		cardPanel.setLayout(cardLayout);
		cardPanel.add(srPane, "srPane");	//	Sequence Important!
		cardPanel.add(mrPane, "mrPane");
		cardPanel.setBorder(null);
		cardPanel.setName("gc_cardPanel");
		//  single row (w/o xPane it would be centered)
		srPane.setBorder(null);
		srPane.setName("gc_srPane");
		srPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		srPane.add(vPane, JSplitPane.TOP);
		srPane.setTopComponent(vPane);
		srPane.setBottomComponent(null);	//	otherwise a button is created/displayed
		//
		vPane.getViewport().add(xPanel, null);
		xPanel.add(vPanel);
		vPane.setBorder(null);
		xPanel.setLayout(xLayout);
		xPanel.setName("gc_xPanel");
		xLayout.setAlignment(FlowLayout.LEFT);
		xLayout.setHgap(0);
		xLayout.setVgap(0);
		//  multi-row
		mrPane.setBorder(null);
		mrPane.getViewport().add(vTable, null);
		mrPane.setName("gc_mrPane");
		//
		graphPanel.setBorder(null);
		graphPanel.setName("gc_graphPanel");
		srPane.setDividerLocation(200);
	}   //  jbInit

	/**
	 *  Displose
	 */
	public void dispose()
	{
		log.config( "(" + m_mTab.toString() + ")");
		//  clear info
		stopEditor(false);
		if (m_mTab.needSave(true, false))
			m_mTab.dataIgnore();
		vIncludedGC = null;

		//  Listeners
		m_mTab.getTableModel().removeDataStatusListener(this);
		m_mTab.getTableModel().removeVetoableChangeListener(this);
		vTable.getSelectionModel().removeListSelectionListener(this);
		m_mTab.removePropertyChangeListener(vTable);

		//  editors
		Component[] comp = vPanel.getComponents();
		for (int i = 0; i < comp.length; i++)
		{
			if (comp[i] instanceof VEditor)
			{
				VEditor vEditor = (VEditor)comp[i];
				vEditor.removeVetoableChangeListener(this);
				String columnName = comp[i].getName();
				MField mField = m_mTab.getField(columnName);
				if (mField != null)
					mField.removePropertyChangeListener(vEditor);
				vEditor.dispose();
			}
		}
		/** @todo Remove APanel Button listeners */

		vTable.removeAll();
		vTable.setModel(new DefaultTableModel());   //  remove reference
		vTable = null;
		vPanel.removeAll();
		vPanel = null;
		srPane.removeAll();
		srPane = null;
		splitPane.removeAll();
		splitPane = null;
		m_mTab = null;
		m_tree = null;
		this.removeAll();
	}   //  dispose

	/** Model Tab                   */
	private MTab        m_mTab = null;
	/** Window                      */
	private int         m_WindowNo;
	/** Only Multi-Row exist        */
	private boolean     m_onlyMultiRow = false;
	/** Single/Multi Row indicator  */
	private boolean     m_singleRow = true;
	/** Veto Active                 */
	private boolean     m_vetoActive = false;
	/** Tree Panel (optional)       */
	private VTreePanel  m_tree;

	
	/**************************************************************************
	 *  Init Grid.
	 *  <pre>
	 *  - Map table to model
	 *  - Update (multi-row) table info with renderers/editors
	 *  - build single-row panel
	 *  - initialize display
	 *  </pre>
	 *  @param mTab tab
	 *  @param onlyMultiRow only table
	 *  @param WindowNo window no
	 *  @param aPanel optional Application Panel for adding button listeners
	 * 	@param mWindow parent Window Model
	 *  @return true if initialized
	 */
	public boolean initGrid (MTab mTab, boolean onlyMultiRow, 
		int WindowNo, APanel aPanel, MWindow mWindow)
	{
		log.config( "(" + mTab.toString() + ")");
		m_mTab = mTab;
		m_WindowNo = WindowNo;
		m_onlyMultiRow = onlyMultiRow;
		setName("GC-" + mTab);

		//  Set up Multi Row Table
		vTable.setModel(m_mTab.getTableModel());

		//  Update Table Info -------------------------------------------------
		int size = setupVTable (aPanel, m_mTab, vTable);

		//  Set Color on Tab Level
		//  this.setBackgroundColor (mTab.getColor());

		//  Single Row  -------------------------------------------------------
		if (!m_onlyMultiRow)
		{
			//	Set Softcoded Mnemonic &x
			for (int i = 0; i < size; i++)
			{
				MField mField = m_mTab.getField(i);
				if (mField.isDisplayed())
					vPanel.setMnemonic(mField);
			}   //  for all fields
			
			//	Add Fields
			for (int i = 0; i < size; i++)
			{
				MField mField = m_mTab.getField(i);
				if (mField.isDisplayed())
				{
					VEditor vEditor = VEditorFactory.getEditor(m_mTab, mField, false);
					if (vEditor == null)
					{
						log.severe("Editor not created for " + mField.getColumnName());
						continue;
					}
					//  MField => VEditor - New Field value to be updated to editor
					mField.addPropertyChangeListener(vEditor);
					//  VEditor => this - New Editor value to be updated here (MTable)
					vEditor.addVetoableChangeListener(this);
					//  Add to VPanel
					vPanel.addField(vEditor, mField);
					//  APanel Listen to buttons
					if (mField.getDisplayType() == DisplayType.Button && aPanel != null)
						((JButton)vEditor).addActionListener (aPanel);
				}
			}   //  for all fields

			//	No Included Grid Controller
			srPane.setResizeWeight(1);	//	top part gets all
			srPane.setDividerSize (0);
			srPane.setDividerLocation (9999);

			//  Use SR to size MR
			mrPane.setPreferredSize(vPanel.getPreferredSize());
		}   //  Single-Row

		//  Tree Graphics Layout
		int AD_Tree_ID = 0;
		if (m_mTab.isTreeTab())
			AD_Tree_ID = MTree.getDefaultAD_Tree_ID (
				Env.getAD_Client_ID(Env.getCtx()), m_mTab.getKeyColumnName());
		if (m_mTab.isTreeTab() && AD_Tree_ID != 0)
		{
			m_tree = new VTreePanel(m_WindowNo, false, true);
			if (m_mTab.getTabNo() == 0)	//	initialize other tabs later
				m_tree.initTree(AD_Tree_ID);
			m_tree.addPropertyChangeListener(VTreePanel.NODE_SELECTION, this);
			graphPanel.add(m_tree, BorderLayout.CENTER);
			splitPane.setDividerLocation(250);
		//	splitPane.resetToPreferredSizes();
		}
		else    //  No Graphics - hide
		{
			graphPanel.setPreferredSize(new Dimension(0,0));
			splitPane.setDividerSize(0);
			splitPane.setDividerLocation(0);
		}

		//  Receive DataStatusChanged info from MTab
		m_mTab.addDataStatusListener(this);
		//  Receive vetoableChange info from MTable when saving
		m_mTab.getTableModel().addVetoableChangeListener(this);
		//	Selection Listener -> valueChanged
		vTable.getSelectionModel().addListSelectionListener(this);
		//  Navigation (RowChanged)
		m_mTab.addPropertyChangeListener(vTable);

		//  Update UI
		vTable.autoSize(true);

		setTabLevel(m_mTab.getTabLevel());

		//  Set initial presentation
		if (onlyMultiRow || !m_mTab.isSingleRow())
			switchMultiRow();
		else
			switchSingleRow();
	//	log.config( "GridController.dynInit (" + mTab.toString() + ") - fini");
		return true;
	}   //  initGrid

	/**
	 * 	Include Tab
	 * 	@param gc grod controller to add
	 * 	@return true if included
	 */
        // begin vpj-cd e-evolution 
        public boolean includeTab (GridController gc , APanel aPanel)	
        //public boolean includeTab (GridController gc)
        //end vpj-cd e-evolution 
        {
         MTab includedMTab = gc.getMTab();
         includedMTab.setIncluded (true);
	     //gc.getvPanel().setIncludeTab(window);
		//if (m_mTab.getIncluded_Tab_ID () != imcludedMTab.getAD_Tab_ID())
		//	return false;		
		 vIncludedGC = gc;
		 vIncludedGC.switchMultiRow();
         //begin vpj-cd e-evolution 08/10/2005				 
		 int c = VTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
		 vTable.getInputMap(c).put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), aPanel.aSave.getName());
	     vTable.getActionMap().put(aPanel.aSave.getName(), aPanel.aSave);
         vIncludedGC.setPreferredSize(new Dimension(650, 250)); 
         vIncludedGC.setName(includedMTab.getName()); 
         vIncludedGC.setBackground(getBackground());
         //vIncludedGC.setBackground(org.compiere.plaf.CompiereColor.getDefaultBackground());                
         //org.compiere.apps.TabSwitcher ts = new org.compiere.apps.TabSwitcher(vIncludedGC, aPanel);       
      
         Component[] comp = vPanel.getComponents();                                
	     for (int i = 0; i < comp.length; i++)
	     {
                   if  (comp[i] == null)
                   continue;
                   	
                   
		   if (comp[i] instanceof JTabbedPane)
		   {                                
                       
                       
                       final JTabbedPane m_tabinclude = (JTabbedPane)comp[i];                                                      
                       String m_name = m_tabinclude.getName();
                       
                       if (m_name ==null)
                           continue;
                       String m_tab_id =  String.valueOf(includedMTab.getAD_Tab_ID());
                       //System.out.println("id tab include " + m_tab_id);
                       if (m_name.equals(m_tab_id))
                       {
                           m_tabinclude.add(vIncludedGC);                      
                       }    
                   }
                 }  
         
		/*Dimension size = getPreferredSize();
		srPane.setResizeWeight(.75);	//	top part gets 75%
		srPane.add(vIncludedGC, JSplitPane.BOTTOM);
		srPane.setBottomComponent(vIncludedGC);
		srPane.setDividerSize (5);
		//
		int height = 150;
		vIncludedGC.setPreferredSize(new Dimension(600, height));
		setPreferredSize(new Dimension(size.width, size.height+height));
		srPane.setDividerLocation (size.height);*/                
        //end vpj-cd e-evolution 08/10/2005  
                
		//
		//includedMTab.setIncluded (true);		
		includedMTab.query (false, 0);
		JRootPane rt = SwingUtilities.getRootPane(this);
		if (rt == null)
			System.out.println("Root pane null");
		else
		{
			System.out.println("Root=" + rt);
			rt.addMouseListener(vIncludedGC);
			Component gp = rt.getGlassPane();
			if (gp == null)
				System.out.println("No Glass Pane");
			else
			{
				System.out.println("Glass=" + gp);
				gp.addMouseListener(vIncludedGC);
			}

		}



		vIncludedGC.addMouseListener(vIncludedGC);
		vIncludedGC.enableEvents(AWTEvent.HIERARCHY_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK);
		/**
		vIncludedGC.splitPane.addMouseListener(vIncludedGC);
		vIncludedGC.cardPanel.addMouseListener(vIncludedGC);
		vIncludedGC.mrPane.addMouseListener(vIncludedGC);
		vIncludedGC.vTable.addMouseListener(vIncludedGC);
		**/
		return true;
	}	//	IncludeTab

	/**
	 * 	Get Title
	 *	@return title
	 */
	public String getTitle ()
	{
		return m_mTab.getName();
	}	//	getTitle
	
	/**
	 *	Setup Multi-Row Table (add fields)
	 * 	@param aPanel Panel
	 * 	@param mTab Model Tab
	 * 	@param table JTable
	 * 	@return size
	 */
	private int setupVTable (APanel aPanel, MTab mTab, VTable table)
	{
		if (!mTab.isDisplayed())
			return 0;
		int size = mTab.getFieldCount ();
		TableColumnModel tcm = table.getColumnModel();
		if (size != tcm.getColumnCount())
			throw new IllegalStateException("TableColumn Size <> TableModel");

		for (int i = 0; i < size; i++)
		{
			MField mField = mTab.getField (i);
			TableColumn tc = tcm.getColumn(i);
			tc.setMinWidth(30);
			//
			if (mField.getColumnName().equals(tc.getIdentifier().toString()))
			{
				if (mField.getDisplayType () == DisplayType.RowID)
				{
					tc.setCellRenderer (new VRowIDRenderer (false));
					tc.setCellEditor (new VRowIDEditor (false));
					tc.setHeaderValue ("");
					tc.setMaxWidth (2);
				}
				else
				{
					//  need to set CellEditor explicitly as default editor based on class causes problem (YesNo-> Boolean)
					if (mField.isDisplayed ())
					{
						tc.setCellRenderer (new VCellRenderer (mField));
						VCellEditor ce = new VCellEditor (mField);
						tc.setCellEditor (ce);
						//
						tc.setHeaderValue (mField.getHeader ());
						tc.setPreferredWidth (Math.max (mField.getDisplayLength (), 30));
						tc.setHeaderRenderer (new VHeaderRenderer (mField.getDisplayType ()));

						//  Enable Button actions in grid
						if (mField.getDisplayType () == DisplayType.Button)
						{
							VEditor button = ce.getEditor ();
							if (button != null && aPanel != null)
								((JButton)button).addActionListener (aPanel);
						}
					}
					else //  column not displayed
					{
						TableCellNone tcn = new TableCellNone(mField.getColumnName());
						tc.setCellRenderer (tcn);
						tc.setCellEditor (tcn);
						tc.setHeaderValue (null);
						tc.setMinWidth (0);
						tc.setMaxWidth (0);
						tc.setPreferredWidth (0);
					}
				}
			//	System.out.println ("TableColumnID " + tc.getIdentifier ()
			//	  + "  Renderer=" + tc.getCellRenderer ()
			//	  + mField.getHeader ());

			}	//	found field
			else
				log.log(Level.SEVERE, "TableColumn " + tc.getIdentifier ()
				  + " <> MField " + mField.getColumnName() + mField.getHeader());
		} 	//  for all fields
		return size;
	}	//	setupVTable

	/**
	 * 	Activate Grid Controller.
	 * 	Called by APanel when GridController is displayed (foreground)
	 */
	public void activate ()
	{
		//	Tree to be initiated on second/.. tab
		if (m_mTab.isTreeTab() && m_mTab.getTabNo() != 0)
		{
			int AD_Tree_ID = Env.getContextAsInt (Env.getCtx(), m_WindowNo, "AD_Tree_ID");
			if (AD_Tree_ID == 0)
				AD_Tree_ID = MTree.getDefaultAD_Tree_ID (
					Env.getAD_Client_ID(Env.getCtx()), m_mTab.getKeyColumnName());
			m_tree.initTree (AD_Tree_ID);
		}
	}	//	activate


	/**
	 *  Register ESC Actions
	 *  - overwrite VTable's Keystroks assigment for ESC
	 *  @param aIgnore ignore
	 */
	public void registerESCAction (AppsAction aIgnore)
	{
		int c = VTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
		vTable.getInputMap(c).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), aIgnore.getName());
		vTable.getActionMap().put(aIgnore.getName(), aIgnore);

	//	AEnv.printActionInputMap(vTable);
	}   //  registerESCAction

	/**
	 *  Query Tab and resize Table
	 *  (called from APanel)
	 *  @param onlyCurrentRows only current rows
	 *  @param onlyCurrentDays how many days back
	 */
	public void query (boolean onlyCurrentRows, int onlyCurrentDays)
	{
		//  start loading while building screen
		m_mTab.query(onlyCurrentRows, onlyCurrentDays);
		//  Update UI
		if (!isSingleRow())
			vTable.autoSize(true);
	}   //  query

	
	/**************************************************************************
	 *  Switch from single to multi & vice versa
	 */
	public void switchRowPresentation()
	{
		stopEditor(true);
		if (m_singleRow)
			switchMultiRow();
		else
			switchSingleRow();
	}   //  switchRowPresentation

	/**
	 *  Switch to SingleRow Presentation
	 */
	public void switchSingleRow()
	{
		if (m_onlyMultiRow)
			return;
		cardLayout.first(cardPanel);
		m_singleRow = true;
		dynamicDisplay(0);
	//	vPanel.requestFocus();
	}   //  switchSingleRow

	/**
	 *  Switch to MultiRow Presentation
	 */
	public void switchMultiRow()
	{
		cardLayout.last(cardPanel);
		m_singleRow = false;
		vTable.autoSize(true);	//	resizes
	//	vTable.requestFocus();
	}   //  switchSingleRow

	/**
	 *  Is Single Row presentation
	 *  @return true if Single Row is displayed
	 */
	public boolean isSingleRow()
	{
		return m_singleRow;
	}   //  isSingleRow

	
	/**************************************************************************
	 *  Remove Listener - pass on to MTab
	 *  @param l listener
	 */
	public synchronized void removeDataStatusListener(DataStatusListener l)
	{
		m_mTab.removeDataStatusListener(l);
	}   //  removeDataStatusListener

	/**
	 *  Add Data Status Listener - pass on to MTab
	 *  @param l listener
	 */
	public synchronized void addDataStatusListener(DataStatusListener l)
	{
		m_mTab.addDataStatusListener(l);
	}

	/**
	 *  Data Status Listener - for MTab events.
	 *  <p>
	 *  Callouts are processed here for GUI changes
	 *  - same as in MTab.setValue for batch changes
	 *  <p>
	 *  calls dynamicDisplay
	 *  @param e event
	 */
	public void dataStatusChanged(DataStatusEvent e)
	{
	//	if (e.getChangedColumn() == 0)
	//		return;
		int col = e.getChangedColumn();
		log.config("(" + m_mTab + ") Col=" + col + ": " + e.toString());

		//  Process Callout only for specific columns
		if (col != 0)
		{
			MField mField = m_mTab.getField(col);
			if (mField != null && mField.getCallout().length() > 0)
			{
				String msg = m_mTab.processFieldChange(mField);     //  Dependencies & Callout
				if (msg.length() > 0)
					ADialog.error(m_WindowNo, this, msg);
			}
		}
		dynamicDisplay(col);
	}   //  dataStatusChanged

	
	/**************************************************************************
	 *  List Selection Listener (VTable) - row changed
	 *  @param e event
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		//	nothing or initiated by mouse (wait for "real" one)
		if (e.getValueIsAdjusting())
			return;
		//  no rows
		if (m_mTab.getRowCount() == 0)
			return;

	//	vTable.stopEditor(graphPanel);
		int rowTable = vTable.getSelectedRow();
		int rowCurrent = m_mTab.getCurrentRow();
		log.config("(" + m_mTab.toString() + ") Row in Table=" + rowTable + ", in Model=" + rowCurrent);
		
		//begin vpj-cd e-evolution 30 Ene 2005
		if(rowCurrent + 1 == vTable.getRowCount()   
				&& !isSingleRow() && Env.isAutoNew(Env.getCtx()) && m_mTab.getRecord_ID() != -1)
		{
			//stopEditor(true);
			vTable.getSelectionModel().removeListSelectionListener(this);
			m_mTab.dataNew(false);
			dynamicDisplay(0);
			vTable.getSelectionModel().addListSelectionListener(this);
			//vTable.setRowSelectionInterval(rowCurrent + 1, rowCurrent + 1); //  causes this method to be called again
			return;
		}
		//end   vpj-cd e-evolution 30 Ene 2006

		if (rowTable == -1)  //  nothing selected
		{
			if (rowCurrent >= 0)
			{
				vTable.setRowSelectionInterval(rowCurrent, rowCurrent); //  causes this method to be called again
				return;
			}
		}
		else
		{
			if (rowTable != rowCurrent)
				m_mTab.navigate(rowTable);
			dynamicDisplay(0);
		}

		//	TreeNavigation - Synchronize 	-- select node in tree
		if (m_tree != null)
			m_tree.setSelectedNode (m_mTab.getRecord_ID());	//	ignores new (-1)

	//	log.config( "GridController.valueChanged (" + m_mTab.toString() + ") - fini",
	//		"Row in Table=" + rowTable + ", in Model=" + rowCurrent);

		//	Query Included Tab
		if (vIncludedGC != null)
			vIncludedGC.getMTab().query(false, 0);
	}   //  valueChanged

	/**
	 *  PropertyChange Listener - Tree Panel - node selection
	 *  @param e event
	 */
	public void propertyChange(PropertyChangeEvent e)
	{
	//	System.out.println("propertyChange");
	//	System.out.println(e);
		if (e == null)
			return;
		Object value = e.getNewValue();
		if (value == null)
			return;
		log.config(e.getPropertyName() + "=" + value
			+ " - " + value.getClass().toString());
		if (!(value instanceof MTreeNode))
			return;

		//  We Have a TreeNode
		int nodeID = ((MTreeNode)value).getNode_ID();
		//  root of tree selected - ignore
		if (nodeID == 0)
			return;

		//  Search all rows for mode id
		int size = m_mTab.getRowCount();
		int row = -1;
		for (int i = 0; i < size; i++)
		{
			if (m_mTab.getKeyID(i) == nodeID)
			{
				row = i;
				break;
			}
		}
		if (row == -1)
		{
			log.log(Level.SEVERE, "Tab does not have ID with Node_ID=" + nodeID);
			return;
		}

		//  Navigate to node row
		m_mTab.navigate(row);
	}   //  propertyChange

	/**
	 *  Dynamic Display.
	 *  - Single Row Screen layout and update of dynamic Lookups
	 *  <p>
	 *  Single Row layout:
	 *  the components's name is the ColumnName; if it matches, the
	 *  MField.isDisplayed(true) is used to determine if it is visible
	 *  if the component is a VEditor, setEnabled is set from the MField
	 *  <p>
	 *  Multi Row layout is not changed:
	 *  VCellRenderer calls JTable.isCellEditable -> checks MField.isEditable (Active, isDisplayed)
	 *  VCellEditor.isCellEditable calls MField.isEditable(true) <br>
	 *  If a column is not displayed, the width is set to 0 in dynInit
	 *  <p>
	 *  Dynamic update of data is handeled in VLookup.focusGained/Lost.
	 *  When focus is gained the model is temporarily updated with the
	 *  specific validated data, if lost, it is switched back to the
	 *  unvalidated data (i.e. everything). This allows that the display
	 *  methods have a lookup to display. <br>
	 *  Here: if the changed field has dependents and the dependent
	 *  is a Lookup and this lookup has a dynamic dependence of the changed field,
	 *  the value of that field is set to null (in MTab.processDependencies -
	 *  otherwise it would show an invalid value).
	 *  As Editors listen for value changed of their MField, the display is updated.
	 *  <p>
	 *  Called from GridController.valueChanged/dataStatusChanged, APane;.stateChanged/unlock/cmd_...
	 *  @param col selective column number or 0 if all
	 */
	public void dynamicDisplay (int col)
	{
	//	log.config( "GridController.dynamicDisplay (" + m_mTab.toString() + ") SingleRow=" + isSingleRow() + ", OnlyMultiRow=" + m_onlyMultiRow);
		//	Don't update if multi-row
		if (!isSingleRow() || m_onlyMultiRow)
			return;
		if (!m_mTab.isOpen())
			return;
		//  Selective
		if (col != 0)
		{
			MField changedField = m_mTab.getField(col);
			String columnName = changedField.getColumnName();
			ArrayList dependants = m_mTab.getDependantList(columnName);
			log.config("(" + m_mTab.toString() + ") "
				+ columnName + " - Dependents=" + dependants.size());
			//	No Dependents and no Callout - Set just Background
			if (dependants.size() == 0 && changedField.getCallout().length() > 0)
			{
				Component[] comp = vPanel.getComponents();
				for (int i = 0; i < comp.length; i++)
				{
					if (columnName.equals(comp[i].getName ()) && comp[i] instanceof VEditor)
					{
						VEditor ve = (VEditor)comp[i];
						boolean manMissing = false;
						boolean noValue = changedField.getValue() == null || changedField.getValue().toString().length() == 0;
						if (noValue && changedField.isEditable(true) && changedField.isMandatory(true))    //  check context
							manMissing = true;
						ve.setBackground(manMissing || changedField.isError());
						break;
					}
				}
				return;
			}
		}   //  selective


		//  complete single row re-display
		boolean noData = m_mTab.getRowCount() == 0;
		log.config(m_mTab.toString() + " - Rows=" + m_mTab.getRowCount());
		//  All Components in vPanel (Single Row)
		Component[] comp = vPanel.getComponents();
		for (int i = 0; i < comp.length; i++)
		{
			String columnName = comp[i].getName();
			if (columnName != null)
			{
				MField mField = m_mTab.getField(columnName);
				if (mField != null)
				{
					if (mField.isDisplayed(true))       //  check context
					{
						if (!comp[i].isVisible())
							comp[i].setVisible(true);                   //  visibility
						if (comp[i] instanceof VEditor)
						{
							VEditor ve = (VEditor)comp[i];
							if (noData)
								ve.setReadWrite(false);
							else
							{
								boolean rw = mField.isEditable(true);       //  r/w - check Context
								ve.setReadWrite(rw);
							//	Log.trace(7, "GridController.dynamicDisplay RW=" + rw, mField);
								boolean manMissing = false;
							//  least expensive operations first        //  missing mandatory
								if (rw && mField.getValue() == null && mField.isMandatory(true))    //  check context
									manMissing = true;
								ve.setBackground(manMissing || mField.isError());
							}
						}
					}
					else if (comp[i].isVisible())
						comp[i].setVisible(false);
				}
			}
		}   //  all components
		log.config(m_mTab.toString() + " - fini - " + (col==0 ? "complete" : "seletive"));
	}   //  dynamicDisplay

	/**
	 *  Row Changed - synchronize with Tree
	 *
	 *  @param  save    true the row was saved (changed/added), false if the row was deleted
	 *  @param  keyID   the ID of the row changed
	 */
	public void rowChanged (boolean save, int keyID)
	{
		if (m_tree == null || keyID <= 0)
			return;
		String name = (String)m_mTab.getValue("Name");
		String description = (String)m_mTab.getValue("Description");
		Boolean IsSummary = (Boolean)m_mTab.getValue("IsSummary");
		String imageIndicator = (String)m_mTab.getValue("Action");  //  Menu - Action
		//
		m_tree.nodeChanged(save, keyID, name, description, 
			IsSummary.booleanValue(), imageIndicator);
	}   //  rowChanged


	
	/**************************************************************************
	 *  Vetoable Change Listener.
	 * 	Called from VEditor
	 *  <pre>
	 *  - for Save Confirmation dialog
	 *  - for Single Row from VEditor: Update MTable
	 *  </pre>
	 *  @param e event
	 *  @throws PropertyVetoException
	 */
	public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException
	{
		if (m_mTab.isProcessed())		//	only active records
		{
			Object source = e.getSource();
			if (source instanceof VEditor)
			{
				if (!((VEditor)source).isReadWrite())
				{
					log.config("(" + m_mTab.toString() + ") " + e.getPropertyName());
					return;
				}
			}
			else
			{
				log.config("(" + m_mTab.toString() + ") " + e.getPropertyName());
				return;
			}
		}
		log.config("(" + m_mTab.toString() + ") "
			+ e.getPropertyName() + "=" + e.getNewValue() + " (" + e.getOldValue() + ") "
			+ (e.getOldValue() == null ? "" : e.getOldValue().getClass().getName()));
		

		//  Save Confirmation dialog    MTable-RowSave
		if (e.getPropertyName().equals(MTable.PROPERTY))
		{
			//  throw new PropertyVetoException calls this method (??) again
			if (m_vetoActive)
			{
				m_vetoActive = false;
				return;
			}
			if (!Env.isAutoCommit(Env.getCtx(), m_WindowNo) || m_mTab.getCommitWarning().length() > 0)
			{
				if (!ADialog.ask(m_WindowNo, this, "SaveChanges?", m_mTab.getCommitWarning()))
				{
					m_vetoActive = true;
					throw new PropertyVetoException ("UserDeniedSave", e);
				}
			}
			return;
		}   //  saveConfirmation


		//  Get Row/Col Info
		MTable mTable = m_mTab.getTableModel();
		int row = m_mTab.getCurrentRow();
		int col = mTable.findColumn(e.getPropertyName());
		//
		if (e.getNewValue() == null && e.getOldValue() != null 
			&& e.getOldValue().toString().length() > 0)		//	some editors return "" instead of null
			mTable.setChanged (true);
		else
		{
		//	mTable.setValueAt (e.getNewValue(), row, col, true);
			mTable.setValueAt (e.getNewValue(), row, col);	//	-> dataStatusChanged -> dynamicDisplay
			//	Force Callout
			if (e.getPropertyName().equals("S_ResourceAssignment_ID"))
			{
				MField mField = m_mTab.getField(col);
				if (mField != null && mField.getCallout().length() > 0)
					m_mTab.processFieldChange(mField);     //  Dependencies & Callout
			}
		}

	//	log.config( "GridController.vetoableChange (" + m_mTab.toString() + ") - fini", e.getPropertyName() + "=" + e.getNewValue());
	}   //  vetoableChange

	
	/**************************************************************************
	 *  Get Model Tab
	 *  @return Model Tab
	 */
	public MTab getMTab()
	{
		return m_mTab;
	}   //  getMTab

	/**
	 *  Get VTable
	 *  @return VTable
	 */
	public VTable getTable()
	{
		return vTable;
	}   //  getTable

	
	/**
	 * 	Set Window level Mnemonics
	 *	@param set true if set otherwise unregiser
	 */
	public void setMnemonics (boolean set)
	{
		if (vPanel != null)
			vPanel.setMnemonics(set);
	}	//	setMnemonics
	
	/**
	 *  Stop Table & SR Editors and move focus to graphPanel
	 *  @param saveValue save value
	 */
	public void stopEditor (boolean saveValue)
	{
		log.config("(" + m_mTab.toString() + ") TableEditing=" + vTable.isEditing());

		//  MultiRow - remove editors
		vTable.stopEditor(saveValue);

		//  SingleRow - stop editors by changing focus
		if (m_singleRow)
			vPanel.transferFocus();
		//	graphPanel.requestFocus();
		//
	//	log.config( "GridController.stopEditor (" + m_mTab.toString() + ") - fini",
	//		"Editing=" + vTable.isEditing());
	}   //  stopEditors


	public void mouseClicked(MouseEvent e)
	{
		log.finest("" + this + " - " + e);
	}
	public void mousePressed(MouseEvent e)
	{
		log.finest("" + this + " - " + e);
	}
	public void mouseReleased(MouseEvent e)
	{
		log.finest("" + this + " - " + e);
	}
	public void mouseEntered(MouseEvent e)
	{
		log.finest("" + this + " - " + e);
	}
	public void mouseExited(MouseEvent e)
	{
		log.finest("" + this + " - " + e);
	}
        
    //begin add Victor Perez 28.08.2005 e-evolution
        public VPanel getvPanel()
	{
		return vPanel;
	}
 
    //end

}   //  GridController
