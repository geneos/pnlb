/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.reports.printproperties;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class PropertiesTableModel extends AbstractTableModel {

	private String[] columnNames = {"Propiedad", "Valor"};
	private ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
	private ArrayList<PrintProperty> props = new ArrayList<PrintProperty>();

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int column) {
        return columnNames[column];
    }

	public Object getValueAt(int row, int col) {
		if (row >= 0 && row < getRowCount() && col >= 0 && col < getColumnCount()) {
			return data.get(row).get(col);
		}
		return null;
	}

	/*
	 * Don't need to implement this method unless your table's
	 * editable.
	 */
	public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,
		//no matter where the cell appears onscreen.
		if (col < 1) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Don't need to implement this method unless your table's
	 * data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		while (row > getRowCount()) {
			data.add(new ArrayList<Object>());
		}
		ArrayList rowData = data.get(row);
		rowData.set(col, value);
		fireTableCellUpdated(row, col);
	}

	/*
	 * Don't need to implement this method unless your table's
	 * data can change.
	 */
	public void addEditableProperty(PrintProperty prop, Object value) {
		ArrayList<Object> values = new ArrayList<Object>();
		values.add(prop.getLabel());
		values.add(value);		
		data.add(values);
		props.add(prop);
	}


	public PrintProperty getPropertyForRow(int row) {
		if (row >= 0 && row < props.size()) {
			return props.get(row);
		}
		return null;
	}

	public void removeAll() {
		data.clear();
		props.clear();
	}
}
