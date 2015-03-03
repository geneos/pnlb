package org.zynnia.reports.table;

import org.compiere.model.FieldDynamicReport;

public class FieldComboBoxItem {

	private FieldDynamicReport field;

	private int numCol;

	public FieldComboBoxItem(int column, FieldDynamicReport field) {
		this.numCol = column;
		this.field = field;
	}

	public FieldDynamicReport getField() {
		return field;
	}	

	public int getNumCol() {
		return numCol;
	}

	@Override
	public String toString() {
		return getField().getFieldTitle();
	}

}
