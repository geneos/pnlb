/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.reports.printproperties;

import java.util.Hashtable;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.compiere.swing.CTable;
import org.compiere.util.CLogger;

public class PropertiesTable extends CTable {

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(PropertiesTable.class);
	private Class editingClass;
	private Hashtable<PrintProperty, PropertyCellEditor> defaultEditors = new Hashtable<PrintProperty, PropertyCellEditor>();

	public PropertiesTable() {
		super();
//		setCellSelectionEnabled(false);
		setRowSelectionAllowed(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		setModel(new PropertiesTableModel());
		setupEditors();
	}

	private void setupEditors() {
		defaultEditors.put(PrintProperty.LABEL_PROPERTY, new PropertyCellEditor(PrintProperty.LABEL_PROPERTY));
		defaultEditors.put(PrintProperty.ALIGN_PROPERTY, new PropertyCellEditor(PrintProperty.ALIGN_PROPERTY));
		defaultEditors.put(PrintProperty.WIDTH_PROPERTY, new PropertyCellEditor(PrintProperty.WIDTH_PROPERTY));
	}

	public TableCellRenderer getCellRenderer(int row, int column) {
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);

		if (modelColumn == 1) {
			Object value = getModel().getValueAt(row, modelColumn);
			if (value == null) {
				value = "";
			}
			Class rowClass = value.getClass();
			return getDefaultRenderer(rowClass);
		} else {
			return super.getCellRenderer(row, column);
		}
	}

	public TableCellEditor getCellEditor(int row, int column) {
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);

		if (modelColumn == 1) {
			PropertiesTableModel model = (PropertiesTableModel) getModel();
			Object value = model.getValueAt(row, modelColumn);
			if (value == null) {
				value = "";
			}
			editingClass = value.getClass();
			return defaultEditors.get(model.getPropertyForRow(row));
			/*if (model.getPropertyForRow(row).equals(PrintProperty.ALIGN_PROPERTY)) {
				return alignEditor;
			} else {
				return getDefaultEditor( editingClass );
			}*/
		} else {
			return super.getCellEditor(row, column);
		}
	}

	//  This method is also invoked by the editor when the value in the editor
	//  component is saved in the TableModel. The class was saved when the
	//  editor was invoked so the proper class can be created.
	public Class getColumnClass(int column) {
		Class ret = editingClass != null ? editingClass : super.getColumnClass(column);
		return ret;
	}

	public void removeAll() {
		super.removeAll();
		((PropertiesTableModel) getModel()).removeAll();
	}
}
