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
import java.awt.event.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.compiere.apps.*;
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Find/Search Records.
 *	Based on AD_Find for persistency, query is build to restrict info
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: Find.java,v 1.37 2005/12/09 05:17:54 jjanke Exp $
 */
public final class Find extends CDialog
		implements ActionListener, ChangeListener, DataStatusListener
{
	/**
	 *	Constructor
	 *	@param	owner		Frame Dialog Onwer
	 *  @param  targetWindowNo WindowNo of target window
	 *  @param minRecords number of minimum records
	 */
	public Find (Frame owner, int targetWindowNo, String title, 
		int AD_Table_ID, String tableName, String whereExtended,
		MField[] findFields, int minRecords)
	{
		super(owner, Msg.getMsg(Env.getCtx(), "Find") + ": " + title, true);
		log.info(title);
		//
		m_targetWindowNo = targetWindowNo;
		m_AD_Table_ID = AD_Table_ID;
		m_tableName = tableName;
		m_whereExtended = whereExtended;
		m_findFields = findFields;
		//
		m_query = new MQuery (tableName);
		m_query.addRestriction(whereExtended);
		//	Required for Column Validation
		Env.setContext(Env.getCtx(), m_targetWindowNo, "Find_Table_ID", m_AD_Table_ID);
		//  Context for Advanced Search Grid is WINDOW_FIND
		Env.setContext(Env.getCtx(), Env.WINDOW_FIND, "Find_Table_ID", m_AD_Table_ID);
		//
		try
		{
			jbInit();
			initFind();
			if (m_total < minRecords)
			{
				dispose();
				return;
			}
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "Find", e);
		}
		//
		this.getRootPane().setDefaultButton(confirmPanelS.getOKButton());
		AEnv.showCenterWindow(owner, this);
	}	//	Find

	/** Target Window No            */
	private int				m_targetWindowNo;
	/**	Table ID					*/
	private int				m_AD_Table_ID;
	/** Table Name					*/
	private String			m_tableName;
	/** Where						*/
	private String			m_whereExtended;
	/** Search Fields          		*/
	private MField[]		m_findFields;
	/** Resulting query             */
	private MQuery			m_query = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(Find.class);
	
	/** Number of records			*/
	private int				m_total;
	private PreparedStatement	m_pstmt;
	//
	private	boolean			hasValue = false;
	private boolean			hasDocNo = false;
	private	boolean			hasName = false;
	private	boolean			hasDescription = false;
	/**	Line in Simple Content		*/
	private int				m_sLine = 6;
	
	/**	List of VEditors			*/
	private ArrayList<VEditor>			m_sEditors = new ArrayList<VEditor>();
	/** Target Fields with AD_Column_ID as key  */
	private Hashtable<Integer,MField>	m_targetFields = new Hashtable<Integer,MField>();

	/**	For Grid Controller			*/
	public static final int		TABNO = 99;
	/** Length of Fields on first tab	*/
	public static final int		FIELDLENGTH = 20;
	
	//
	private CPanel southPanel = new CPanel();
	private BorderLayout southLayout = new BorderLayout();
	private StatusBar statusBar = new StatusBar();
	private CTabbedPane tabbedPane = new CTabbedPane();
	private CPanel advancedPanel = new CPanel();
	private BorderLayout advancedLayout = new BorderLayout();
	private ConfirmPanel confirmPanelA = new ConfirmPanel(true, true, false, false, false, false, true);
	private CButton bIgnore = new CButton();
	private JToolBar toolBar = new JToolBar();
	private CButton bSave = new CButton();
	private CButton bNew = new CButton();
	private CButton bDelete = new CButton();
	private GridLayout gridLayout1 = new GridLayout();
	private ConfirmPanel confirmPanelS = new ConfirmPanel(true);
	private BorderLayout simpleLayout = new BorderLayout();
	private CPanel scontentPanel = new CPanel();
	private GridBagLayout scontentLayout = new GridBagLayout();
	private CPanel simplePanel = new CPanel();
	private CLabel valueLabel = new CLabel();
	private CLabel nameLabel = new CLabel();
	private CLabel descriptionLabel = new CLabel();
	private CTextField valueField = new CTextField();
	private CTextField nameField = new CTextField();
	private CTextField descriptionField = new CTextField();
	private CLabel docNoLabel = new CLabel();
	private CTextField docNoField = new CTextField();
	private Component spaceE;
	private Component spaceN;
	private Component spaceW;
	private Component spaceS;
	private JScrollPane advancedScrollPane = new JScrollPane();
	private CTable advancedTable = new CTable();


	public static final int		INDEX_COLUMNNAME = 0;
	public static final int		INDEX_OPERATOR = 1;
	public static final int		INDEX_VALUE = 2;
	public static final int		INDEX_VALUE2 = 3;

	/**	Advanced Search Column 		*/
	public CComboBox 	columns = null;
	/**	Advanced Search Operators 	*/
	public CComboBox 	operators = null;

	/**
	 *	Static Init.
	 *  <pre>
	 *  tabbedPane
	 *      simplePanel
	 *          scontentPanel
	 *          confirmPanelS
	 *      advancedPanel
	 *          toolBar
	 *          GC
	 *          confirmPanelA
	 *  southPanel
	 *      statusBar
	 *  </pre>
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		spaceE = Box.createHorizontalStrut(8);
		spaceN = Box.createVerticalStrut(8);
		spaceW = Box.createHorizontalStrut(8);
		spaceS = Box.createVerticalStrut(8);
		bIgnore.setIcon(new ImageIcon(org.compiere.Compiere.class.getResource("images/Ignore24.gif")));
		bIgnore.setMargin(new Insets(2, 2, 2, 2));
		bIgnore.setToolTipText(Msg.getMsg(Env.getCtx(),"Ignore"));
		bIgnore.addActionListener(this);
		bSave.setIcon(new ImageIcon(org.compiere.Compiere.class.getResource("images/Save24.gif")));
		bSave.setMargin(new Insets(2, 2, 2, 2));
		bSave.setToolTipText(Msg.getMsg(Env.getCtx(),"Save"));
		bSave.addActionListener(this);
		bNew.setIcon(new ImageIcon(org.compiere.Compiere.class.getResource("images/New24.gif")));
		bNew.setMargin(new Insets(2, 2, 2, 2));
		bNew.setToolTipText(Msg.getMsg(Env.getCtx(),"New"));
		bNew.addActionListener(this);
		bDelete.setIcon(new ImageIcon(org.compiere.Compiere.class.getResource("images/Delete24.gif")));
		bDelete.setMargin(new Insets(2, 2, 2, 2));
		bDelete.setToolTipText(Msg.getMsg(Env.getCtx(),"Delete"));
		bDelete.addActionListener(this);
		//
		southPanel.setLayout(southLayout);
		valueLabel.setLabelFor(valueField);
		valueLabel.setText(Msg.translate(Env.getCtx(),"Value"));
		nameLabel.setLabelFor(nameField);
		nameLabel.setText(Msg.translate(Env.getCtx(),"Name"));
		descriptionLabel.setLabelFor(descriptionField);
		descriptionLabel.setText(Msg.translate(Env.getCtx(),"Description"));
		valueField.setText("%");
		valueField.setColumns(FIELDLENGTH);
		nameField.setText("%");
		nameField.setColumns(FIELDLENGTH);
		descriptionField.setText("%");
		descriptionField.setColumns(FIELDLENGTH);
		scontentPanel.setToolTipText(Msg.getMsg(Env.getCtx(),"FindTip"));
		docNoLabel.setLabelFor(docNoField);
		docNoLabel.setText(Msg.translate(Env.getCtx(),"DocumentNo"));
		docNoField.setText("%");
		docNoField.setColumns(FIELDLENGTH);
		advancedScrollPane.setPreferredSize(new Dimension(450, 150));
		southPanel.add(statusBar, BorderLayout.SOUTH);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		//
		scontentPanel.setLayout(scontentLayout);
		simplePanel.setLayout(simpleLayout);
		simplePanel.add(confirmPanelS, BorderLayout.SOUTH);
		simplePanel.add(scontentPanel, BorderLayout.CENTER);
		scontentPanel.add(valueLabel,      new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 0, 5), 0, 0));
		scontentPanel.add(nameLabel,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 0, 5), 0, 0));
		scontentPanel.add(descriptionLabel,    new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
		scontentPanel.add(valueField,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		scontentPanel.add(descriptionField,    new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		scontentPanel.add(docNoLabel,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 0, 5), 0, 0));
		scontentPanel.add(nameField,    new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		scontentPanel.add(docNoField,    new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		//
		scontentPanel.add(spaceE,    new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		scontentPanel.add(spaceN,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		scontentPanel.add(spaceW,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		scontentPanel.add(spaceS,  new GridBagConstraints(2, 15, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		//
	//	tabbedPane.add(simplePanel, Msg.getMsg(Env.getCtx(),"Find"));
		tabbedPane.add(simplePanel, Msg.getMsg(Env.getCtx(),"Find"));
		//
		toolBar.add(bIgnore, null);
		toolBar.addSeparator();
		toolBar.add(bNew, null);
		toolBar.add(bSave, null);
		toolBar.add(bDelete, null);
		advancedPanel.setLayout(advancedLayout);
		advancedPanel.add(toolBar, BorderLayout.NORTH);
		advancedPanel.add(confirmPanelA, BorderLayout.SOUTH);
		advancedPanel.add(advancedScrollPane, BorderLayout.CENTER);
		advancedScrollPane.getViewport().add(advancedTable, null);
	//	tabbedPane.add(advancedPanel, Msg.getMsg(Env.getCtx(),"Advanced"));
		tabbedPane.add(advancedPanel, Msg.getMsg(Env.getCtx(),"Advanced"));
		//
		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		//
		confirmPanelA.addActionListener(this);
		confirmPanelS.addActionListener(this);
		//
		JButton b = ConfirmPanel.createNewButton(true);
		confirmPanelS.addComponent (b);
		b.addActionListener(this);
	}	//	jbInit

	/**
	 *	Dynamic Init.6
	 *  Set up GridController
	 */
	private void initFind()
	{
		log.config("");

		//	Get Info from target Tab
		for (int i = 0; i < m_findFields.length; i++)
		{
			MField mField = m_findFields[i];
			String columnName = mField.getColumnName();

			if (columnName.equals("Value"))
				hasValue = true;
			else if (columnName.equals("Name"))
				hasName = true;
			else if (columnName.equals("DocumentNo"))
				hasDocNo = true;
			else if (columnName.equals("Description"))
				hasDescription = true;
			else if (mField.isSelectionColumn())
				addSelectionColumn (mField);
			else if (columnName.indexOf("Name") != -1)
				addSelectionColumn (mField);

			//  TargetFields
			m_targetFields.put (new Integer(mField.getAD_Column_ID()), mField);
		}   //  for all target tab fields

		//	Disable simple query fields
		valueLabel.setVisible(hasValue);
		valueField.setVisible(hasValue);
		if (hasValue)
			valueField.addActionListener(this);
		docNoLabel.setVisible(hasDocNo);
		docNoField.setVisible(hasDocNo);
		if (hasDocNo)
			docNoField.addActionListener(this);
		nameLabel.setVisible(hasName);
		nameField.setVisible(hasName);
		if (hasName)
			nameField.addActionListener(this);
		descriptionLabel.setVisible(hasDescription);
		descriptionField.setVisible(hasDescription);
		if (hasDescription)
			descriptionField.addActionListener(this);

		//	Get Total
		m_total = getNoOfRecords(null, false);
		setStatusDB (m_total);
		statusBar.setStatusLine("");

		tabbedPane.addChangeListener(this);

		//	Better Labels for OK/Cancel
		confirmPanelA.getOKButton().setToolTipText(Msg.getMsg(Env.getCtx(),"QueryEnter"));
		confirmPanelA.getCancelButton().setToolTipText(Msg.getMsg(Env.getCtx(),"QueryCancel"));
		confirmPanelS.getOKButton().setToolTipText(Msg.getMsg(Env.getCtx(),"QueryEnter"));
		confirmPanelS.getCancelButton().setToolTipText(Msg.getMsg(Env.getCtx(),"QueryCancel"));
	}	//	initFind

	/**
	 * 	Add Selection Column to first Tab
	 * 	@param mField field
	 */
	private void addSelectionColumn (MField mField)
	{
		log.config(mField.getHeader());
		int displayLength = mField.getDisplayLength();
		if (displayLength > FIELDLENGTH)
			mField.setDisplayLength(FIELDLENGTH);
		else
			displayLength = 0;
		
		//	Editor
		VEditor editor = null;
		if (mField.isLookup())
		{
			VLookup vl = new VLookup(mField.getColumnName(), false, false, true,
				mField.getLookup());
			vl.setName(mField.getColumnName());
			editor = vl;
		}
		else
		{
			editor = VEditorFactory.getEditor(mField, false);
			editor.setMandatory(false);
			editor.setReadWrite(true);
		}
		CLabel label = VEditorFactory.getLabel(mField);
		//
		if (displayLength > 0)		//	set it back
			mField.setDisplayLength(displayLength);
		//
		m_sLine++;
		if (label != null)	//	may be null for Y/N
			scontentPanel.add(label,   new GridBagConstraints(1, m_sLine, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(7, 5, 5, 5), 0, 0));
		scontentPanel.add((Component)editor,   new GridBagConstraints(2, m_sLine, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		m_sEditors.add(editor);
	}	//	addSelectionColumn


	/**
	 *  Init Find GridController
	 */
	private void initFindAdvanced()
	{
		log.config("");
		advancedTable.setModel(new DefaultTableModel(0, 4));
		advancedTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

		//	0 = Columns
		ArrayList<ValueNamePair> items = new ArrayList<ValueNamePair>();
		for (int c = 0; c < m_findFields.length; c++)
		{
			MField field = m_findFields[c];
			String columnName = field.getColumnName();
			String header = field.getHeader();
			if (header == null || header.length() == 0)
			{
				header = Msg.translate(Env.getCtx(), columnName);
				if (header == null || header.length() == 0)
					continue;
			}
			if (field.isKey())
				header += (" (ID)");
			ValueNamePair pp = new ValueNamePair(columnName, header);
		//	System.out.println(pp + " = " + field);
			items.add(pp);
		}
		ValueNamePair[] cols = new ValueNamePair[items.size()];
		items.toArray(cols);
		Arrays.sort(cols);		//	sort alpha
		columns = new CComboBox(cols);
		columns.addActionListener(this);
		TableColumn tc = advancedTable.getColumnModel().getColumn(INDEX_COLUMNNAME);
		tc.setPreferredWidth(150);
		tc.setCellEditor(new DefaultCellEditor(columns));
		tc.setHeaderValue(Msg.translate(Env.getCtx(), "AD_Column_ID"));

		//	1 = Operators
		operators = new CComboBox(MQuery.OPERATORS);
		tc = advancedTable.getColumnModel().getColumn(INDEX_OPERATOR);
		tc.setPreferredWidth(40);
		tc.setCellEditor(new DefaultCellEditor(operators));
		tc.setHeaderValue(Msg.getMsg(Env.getCtx(), "Operator"));

		// 	2 = QueryValue
		tc = advancedTable.getColumnModel().getColumn(INDEX_VALUE);
		tc.setCellEditor(new FindValueEditor(this, false));
		tc.setCellRenderer(new FindValueRenderer(this, false));
		tc.setHeaderValue(Msg.getMsg(Env.getCtx(), "QueryValue"));

		// 	3 = QueryValue2
		tc = advancedTable.getColumnModel().getColumn(INDEX_VALUE2);
		tc.setPreferredWidth(50);
		tc.setCellEditor(new FindValueEditor(this, true));
		tc.setCellRenderer(new FindValueRenderer(this, true));
		tc.setHeaderValue(Msg.getMsg(Env.getCtx(), "QueryValue2"));

		//	No Row - Create one
		cmd_new();
	}   //  initFindAdvanced

	/**
	 *	Dispose window
	 */
	public void dispose()
	{
		log.config("");

		//  Find SQL
		if (m_pstmt != null)
		{
			try {
				m_pstmt.close();
			} catch (SQLException e)	{}
		}
		m_pstmt = null;

		//  TargetFields
		if (m_targetFields != null)
			m_targetFields.clear();
		m_targetFields = null;
		//
		removeAll();
		super.dispose();
	}	//	dispose

	
	/**************************************************************************
	 *	Action Listener
	 *  @param e ActionEvent
	 */
	public void actionPerformed (ActionEvent e)
	{
		log.info(e.getActionCommand());
		//
		if (e.getActionCommand() == ConfirmPanel.A_CANCEL)
			cmd_cancel();
		else if (e.getActionCommand() == ConfirmPanel.A_REFRESH)
			cmd_refresh();
		//
		else if (e.getActionCommand() == ConfirmPanel.A_NEW)
		{
			m_query = MQuery.getNoRecordQuery(m_tableName, true);
			m_total = 0;
			dispose();
		}
		//
		else if (e.getSource() == bIgnore)
			cmd_ignore();
		else if (e.getSource() == bNew)
			cmd_new();
		else if (e.getSource() == bSave)
			cmd_save();
		else if (e.getSource() == bDelete)
			cmd_delete();
		//
		else if (e.getSource() == columns)
		{
			ValueNamePair column = (ValueNamePair)columns.getSelectedItem();
			if (column != null)
			{
				String columnName = column.getValue();
				log.config("Column: " + columnName);
				if (columnName.endsWith("_ID") || columnName.endsWith("_Acct"))
					operators.setModel(new DefaultComboBoxModel(MQuery.OPERATORS_ID));
				else if (columnName.startsWith("Is"))
					operators.setModel(new DefaultComboBoxModel(MQuery.OPERATORS_YN));
				else
					operators.setModel(new DefaultComboBoxModel(MQuery.OPERATORS));
			}
		}
		else    // ConfirmPanel.A_OK and enter in fields
		{
			if (e.getSource() == confirmPanelA.getOKButton())
				cmd_ok_Advanced();
			else
				cmd_ok_Simple();
		}
	}	//	actionPerformed

	/**
	 *  Change Listener (tab change)
	 *  @param e ChangeEbent
	 */
	public void stateChanged(ChangeEvent e)
	{
	//	log.info( "Find.stateChanged");
		if (tabbedPane.getSelectedIndex() == 0)
			this.getRootPane().setDefaultButton(confirmPanelS.getOKButton());
		else
		{
			initFindAdvanced();
			this.getRootPane().setDefaultButton(confirmPanelA.getOKButton());
		}
	}	//  stateChanged

	/**
	 *	Simple OK Button pressed
	 */
	private void cmd_ok_Simple()
	{
		//	Create Query String
		m_query = new MQuery(m_tableName);
		if (hasValue && !valueField.getText().equals("%") && valueField.getText().length() != 0)
		{
			String value = valueField.getText().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			m_query.addRestriction("UPPER(Value)", MQuery.LIKE, value, valueLabel.getText(), value);
		}
		//
		if (hasDocNo && !docNoField.getText().equals("%") && docNoField.getText().length() != 0)
		{
			String value = docNoField.getText().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			m_query.addRestriction("UPPER(DocumentNo)", MQuery.LIKE, value, docNoLabel.getText(), value);
		}
		//
		if ((hasName) && !nameField.getText().equals("%") && nameField.getText().length() != 0)
		{
			String value = nameField.getText().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			m_query.addRestriction("UPPER(Name)", MQuery.LIKE, value, nameLabel.getText(), value);
		}
		//
		if (hasDescription && !descriptionField.getText().equals("%") && descriptionField.getText().length() != 0)
		{
			String value = descriptionField.getText().toUpperCase();
			if (!value.endsWith("%"))
				value += "%";
			m_query.addRestriction("UPPER(Description)", MQuery.LIKE, value, descriptionLabel.getText(), value);
		}
		//	Special Editors
		for (int i = 0; i < m_sEditors.size(); i++)
		{
			VEditor ved = (VEditor)m_sEditors.get(i);
			Object value = ved.getValue();
			if (value != null && value.toString().length() > 0)
			{
				String ColumnName = ((Component)ved).getName ();
				log.fine(ColumnName + "=" + value);
				if (value.toString().indexOf("%") != -1)
					m_query.addRestriction(ColumnName, MQuery.LIKE, value, ColumnName, ved.getDisplay());
				else
					m_query.addRestriction(ColumnName, MQuery.EQUAL, value, ColumnName, ved.getDisplay());
			}
		}	//	editors


		//	Test for no records
		if (getNoOfRecords(m_query, true) != 0)
			dispose();
	}	//	cmd_ok_Simple

	
	/**
	 *	Advanced OK Button pressed
	 */
	private void cmd_ok_Advanced()
	{
		//	save pending
		if (bSave.isEnabled())
			cmd_save();
		if (getNoOfRecords(m_query, true) != 0)
			dispose();
	}	//	cmd_ok_Advanced

	/**
	 *	Cancel Button pressed
	 */
	private void cmd_cancel()
	{
		log.info("");
		m_query = null;
		m_total = 999999;
		dispose();
	}	//	cmd_ok

	/**
	 *	Ignore
	 */
	private void cmd_ignore()
	{
		log.info("");
	}	//	cmd_ignore

	/**
	 *	New record
	 */
	private void cmd_new()
	{
		log.info("");
		DefaultTableModel model = (DefaultTableModel)advancedTable.getModel();
		model.addRow(new Object[] {null, MQuery.OPERATORS[MQuery.EQUAL_INDEX], null, null});
	}	//	cmd_new

	/**
	 *	Save (Advanced)
	 */
	private void cmd_save()
	{
		log.info("");
		advancedTable.stopEditor(true);
		//
		m_query = new MQuery(m_tableName);
		for (int row = 0; row < advancedTable.getRowCount(); row++)
		{
			//	Column
			Object column = advancedTable.getValueAt(row, INDEX_COLUMNNAME);
			if (column == null)
				continue;
			String ColumnName = ((ValueNamePair)column).getValue();
			String infoName = column.toString();
			//
			MField field = getTargetMField(ColumnName);
			String ColumnSQL = field.getColumnSQL(false);
			//	Op
			Object op = advancedTable.getValueAt(row, INDEX_OPERATOR);
			if (op == null)
				continue;
			String Operator = ((ValueNamePair)op).getValue();
			
			//	Value	******
			Object value = advancedTable.getValueAt(row, INDEX_VALUE);
			if (value == null)
				continue;
			Object parsedValue = parseValue(field, value);
			if (parsedValue == null)
				continue;
			String infoDisplay = value.toString();
			if (field.isLookup())
				infoDisplay = field.getLookup().getDisplay(value);
			else if (field.getDisplayType() == DisplayType.YesNo)
				infoDisplay = Msg.getMsg(Env.getCtx(), infoDisplay);
			//	Value2	******
			if (MQuery.OPERATORS[MQuery.BETWEEN_INDEX].equals(op))
			{
				Object value2 = advancedTable.getValueAt(row, INDEX_VALUE2);
				if (value2 == null)
					continue;
				Object parsedValue2 = parseValue(field, value2);
				String infoDisplay_to = value2.toString();
				if (parsedValue2 == null)
					continue;
				m_query.addRangeRestriction(ColumnSQL, parsedValue, parsedValue2,
					infoName, infoDisplay, infoDisplay_to);
			}
			else
				m_query.addRestriction(ColumnSQL, Operator, parsedValue,
					infoName, infoDisplay);
		}
	}	//	cmd_save

	/**
	 * 	Parse Value
	 * 	@param field column
	 * 	@param in value
	 * 	@return data type corected value
	 */
	private Object parseValue (MField field, Object in)
	{
		if (in == null)
			return null;
		int dt = field.getDisplayType();
		try
		{
			//	Return Integer
			if (dt == DisplayType.Integer
				|| (DisplayType.isID(dt) && field.getColumnName().endsWith("_ID")))
			{
				if (in instanceof Integer)
					return in;
				int i = Integer.parseInt(in.toString());
				return new Integer(i);
			}
			//	Return BigDecimal
			else if (DisplayType.isNumeric(dt))
			{
				if (in instanceof BigDecimal)
					return in;
				return DisplayType.getNumberFormat(dt).parse(in.toString());
			}
			//	Return Timestamp
			else if (DisplayType.isDate(dt))
			{
				if (in instanceof Timestamp)
					return in;
				long time = 0;
				try
				{
					time = DisplayType.getDateFormat_JDBC().parse(in.toString()).getTime();
					return new Timestamp(time);
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, in + "(" + in.getClass() + ")" + e);
					time = DisplayType.getDateFormat(dt).parse(in.toString()).getTime();
				}
				return new Timestamp(time);
			}
			//	Return Y/N for Boolean
			else if (in instanceof Boolean)
				return ((Boolean)in).booleanValue() ? "Y" : "N";
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "Object=" + in, ex);
			String error = ex.getLocalizedMessage();
			if (error == null || error.length() == 0)
				error = ex.toString();
			StringBuffer errMsg = new StringBuffer();
			errMsg.append(field.getColumnName()).append(" = ").append(in).append(" - ").append(error);
			//
			ADialog.error(0, this, "ValidationError", errMsg.toString());
			return null;
		}

		return in;
	}	//	parseValue

	/**
	 *	Delete
	 */
	private void cmd_delete()
	{
		log.info("");
		DefaultTableModel model = (DefaultTableModel)advancedTable.getModel();
		int row = advancedTable.getSelectedRow();
		if (row >= 0)
			model.removeRow(row);
		cmd_refresh();
	}	//	cmd_delete

	/**
	 *	Refresh
	 */
	private void cmd_refresh()
	{
		log.info("");
		int records = getNoOfRecords(m_query, true);
		setStatusDB (records);
		statusBar.setStatusLine("");
	}	//	cmd_refresh

	
	/**************************************************************************
	 *	Get Query - Retrieve result
	 *  @return String representation of query
	 */
	public MQuery getQuery()
	{
		MRole role = MRole.getDefault();
		if (role.isQueryMax(getTotalRecords()))
		{
			m_query = MQuery.getNoRecordQuery (m_tableName, false);
			m_total = 0;
			log.warning("Query - over max");
		}
		else
			log.info("Query=" + m_query);
		return m_query;
	}	//	getQuery

	/**
	 * 	Get Total Records
	 *	@return no of records
	 */
	public int getTotalRecords()
	{
		return m_total;
	}	//	getTotalRecords
	
	/**
	 *	Get the number of records of target tab
	 *  @param query where clause for target tab
	 * 	@param alertZeroRecords show dialog if there are no records
	 *  @return number of selected records
	 */
	private int getNoOfRecords (MQuery query, boolean alertZeroRecords)
	{
		log.config("" + query);
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
		sql.append(m_tableName);
		boolean hasWhere = false;
		if (m_whereExtended != null && m_whereExtended.length() > 0)
		{
			sql.append(" WHERE ").append(m_whereExtended);
			hasWhere = true;
		}
		if (query != null && query.isActive())
		{
			if (hasWhere)
				sql.append(" AND ");
			else
				sql.append(" WHERE ");
			sql.append(query.getWhereClause());
		}
		//	Add Access
		String finalSQL = MRole.getDefault().addAccessSQL(sql.toString(), 
			m_tableName, MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		finalSQL = Env.parseContext(Env.getCtx(), m_targetWindowNo, finalSQL, false);
		Env.setContext(Env.getCtx(), m_targetWindowNo, TABNO, "FindSQL", finalSQL);

		//  Execute Qusery
		m_total = 999999;
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(finalSQL);
			if (rs.next())
				m_total = rs.getInt(1);
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, finalSQL, e);
		}
		MRole role = MRole.getDefault(); 
		//	No Records
		if (m_total == 0 && alertZeroRecords)
			ADialog.info(m_targetWindowNo, this, "FindZeroRecords");
		//	More then allowed
		else if (query != null && role.isQueryMax(m_total))
			ADialog.error(m_targetWindowNo, this, "FindOverMax", 
				m_total + " > " + role.getMaxQueryRecords());
		else
			log.config("#" + m_total);
		//
		if (query != null)
			statusBar.setStatusToolTip (query.getWhereClause());
		return m_total;
	}	//	getNoOfRecords

	/**
	 *	Display current count
	 *  @param currentCount String representation of current/total
	 */
	private void setStatusDB (int currentCount)
	{
		String text = " " + currentCount + " / " + m_total + " ";
		statusBar.setStatusDB(text);
	}	//	setDtatusDB

	
	/**************************************************************************
	 *	Grid Status Changed.
	 *  @param e DataStatueEvent
	 */
	public void dataStatusChanged (DataStatusEvent e)
	{
		log.config(e.getMessage());
		//	Action control
		boolean changed = e.isChanged();
		bIgnore.setEnabled(changed);
		bNew.setEnabled(!changed);
		bSave.setEnabled(changed);
		bDelete.setEnabled(!changed);
	}	//	statusChanged

	/**
	 * 	Get Target MField
	 * 	@param columnName column name
	 * 	@return MField
	 */
	public MField getTargetMField (String columnName)
	{
		if (columnName == null)
			return null;
		for (int c = 0; c < m_findFields.length; c++)
		{
			MField field = m_findFields[c];
			if (columnName.equals(field.getColumnName()))
				return field;
		}
		return null;
	}	//	getTargetMField

}	//	Find
