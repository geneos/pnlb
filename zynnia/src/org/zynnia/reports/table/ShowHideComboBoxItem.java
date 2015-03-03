package org.zynnia.reports.table;

import org.compiere.model.FieldDynamicReport;

public class ShowHideComboBoxItem extends FieldComboBoxItem {

	private boolean isVisible;

	public ShowHideComboBoxItem(int column, FieldDynamicReport field) {
		this(column, field, true);
	}

	public ShowHideComboBoxItem(int column, FieldDynamicReport field, boolean visible) {
		super(column, field);
		this.isVisible = visible;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	@Override
	public String toString() {
		String result = !isVisible() ? "Show " : "Hide ";
		result = result + getField().getFieldTitle();
		return result;
	}

}
