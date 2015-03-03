/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.reports.printproperties;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.EventObject;
import java.util.logging.Level;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import org.compiere.grid.ed.VCellEditor;
import org.compiere.grid.ed.VEditor;
import org.compiere.grid.ed.VNumber;
import org.compiere.grid.ed.VString;
import org.compiere.model.MTable;
import org.compiere.plaf.CompierePLAF;
import org.compiere.util.CLogger;

public class PropertyCellEditor extends AbstractCellEditor
		implements TableCellEditor, VetoableChangeListener, ActionListener {

	/** The Table Editor        */
	private VEditor m_editor = null;
	/** Table                   */
	private JTable m_table = null;
	/** ClickCount              */
	private static int CLICK_TO_START = 2;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VCellEditor.class);
	private PrintProperty property;

	/**
	 *	Constructor for Grid
	 *  @param mField
	 */
	public PropertyCellEditor(PrintProperty prop) {
		super();
		property = prop;
		//  Click
	}	//	VCellEditor

	/**
	 *  Create Editor
	 */
	private void createEditor() {
		switch (property) {
			case ALIGN_PROPERTY:
				String[] items1 = {"Izquierda", "Centro", "Derecha"};
				m_editor = new VCombo(items1);
				break;
			case WIDTH_PROPERTY:
				m_editor = new VNumber();
				((VNumber)m_editor).setRange((double) 1, (double)100);
				break;
			default:
				m_editor = new VString();
				break;
		}
		m_editor.addVetoableChangeListener(this);
		m_editor.addActionListener(this);
	}   //  createEditor

	/**
	 *	Ask the editor if it can start editing using anEvent.
	 *	If editing can be started this method returns true.
	 *	Previously called: MTable.isCellEditable
	 *  @param anEvent event
	 *  @return true if editable
	 */
	public boolean isCellEditable(EventObject anEvent) {
		//	not enough mouse clicks
		if (anEvent instanceof MouseEvent
				&& ((MouseEvent) anEvent).getClickCount() < CLICK_TO_START) {
			return false;
		}

		if (m_editor == null) {
			createEditor();
		}
		return true;
	}	//	isCellEditable

	/**
	 *	Sets an initial value for the editor. This will cause the editor to
	 *	stopEditing and lose any partially edited value if the editor is editing
	 *	when this method is called.
	 *	Returns the component that should be added to the client's Component hierarchy.
	 *	Once installed in the client's hierarchy this component
	 *	will then be able to draw and receive user input.
	 *
	 *  @param table
	 *  @param value
	 *  @param isSelected
	 *  @param row
	 *  @param col
	 *  @return component
	 */
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
		log.fine("Value=" + value + ", row=" + row + ", col=" + col);
		table.setRowSelectionInterval(row, row);     //  force moving to new row
		if (m_editor == null) {
			createEditor();
		}

		m_table = table;

		//	Set Value
		m_editor.setValue(value);

		//	Set Background/Foreground to "normal" (unselected) colors
		m_editor.setBackground(false);
		m_editor.setForeground(CompierePLAF.getTextColor_Normal());

		//  Other UI
		m_editor.setFont(table.getFont());
		m_editor.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		//
		return (Component) m_editor;
	}	//	getTableCellEditorComponent

	/**
	 *	The editing cell should be selected or not
	 *  @param e
	 *  @return true (constant)
	 */
	public boolean shouldSelectCell(EventObject e) {
		//	log.fine( "VCellEditor.shouldSelectCell", e.toString());
		return true;
	}	//	shouldSelectCell

	/**
	 *	Returns the value contained in the editor
	 *  @return value
	 */
	public Object getCellEditorValue() {
		//	log.fine( "VCellEditor.getCellEditorValue", m_editor.getValue());
		return m_editor.getValue();
	}	//	getCellEditorValue

	/**
	 *  VEditor Change Listener (property name is columnName).
	 *  - indicate change  (for String/Text/..) <br>
	 *  When editing is complete the value is retrieved via getCellEditorValue
	 *  @param e
	 */
	public void vetoableChange(PropertyChangeEvent e) {
		if (m_table == null) {
			return;
		}
		log.log(Level.FINE, "{0}={1}", new Object[]{e.getPropertyName(), e.getNewValue()});
		//
	}   //  vetoableChange

	/**
	 *  Get Actual Editor.
	 *  Called from GridController to add ActionListener to Button
	 *  @return VEditor
	 */
	public VEditor getEditor() {
		return m_editor;
	}   //  getEditor

	/**
	 *  Action Editor - Stop Editor
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e) {
		log.fine("Value=" + m_editor.getValue());
//		super.stopCellEditing();	//	causes VLookup.Search Text not to work
	}   //  actionPerformed

	/**
	 * 	Dispose
	 */
	public void dispose() {
		m_editor = null;
		m_table = null;
	}	//	dispose
}	//	VCellEditor

