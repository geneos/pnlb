/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.parameters;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import org.compiere.grid.ed.VDate;
import org.compiere.model.MZYNMODELCOLUMN;
import org.compiere.util.DisplayType;

public class ZynParameterDateRange extends ZynParameterRange {

	public ZynParameterDateRange(MZYNMODELCOLUMN column) {
		super(column, ParameterType.TYPE_DATE_RANGE,
				new VDate(column.getAD_COLUMN_NAME() + "start", false, false, true, DisplayType.Date, column.getName()) {

					// Added by Gunther Hoppe, 19.07.2005
					// To always refresh table data when value is changed
					// Begin
					@Override
					public void setValue(Object arg0) {
						super.setValue(arg0);
					}
				},
				new VDate(column.getAD_COLUMN_NAME() + "end", false, false, true, DisplayType.Date, column.getName()) {

					// Added by Gunther Hoppe, 19.07.2005
					// To always refresh table data when value is changed
					// Begin
					@Override
					public void setValue(Object arg0) {
						super.setValue(arg0);
					}
				});
	}

	@Override
	public int assignToStatement(int idx, PreparedStatement stmt) {
		Object valueFirstRange = getValueFirstComponent();
		Object valueLastRange = getValueLastComponent();
		if (valueFirstRange == null && valueLastRange == null) {
			return idx;
		}
		try {
			if (valueFirstRange != null) {
				String date1 = getInnerFirstEditor().getValue().toString().split(" ")[0];
				stmt.setDate(idx++, Date.valueOf(date1));
			}
			if (valueLastRange != null) {
				String date2 = getInnerLastEditor().getValue().toString().split(" ")[0];
				stmt.setDate(idx++, Date.valueOf(date2));
			}
		} catch (SQLException ex) {
			log.log(Level.SEVERE, "error assign amount to statement", ex);
		}
		return idx;
	}

	@Override
	public Object getValueFirstComponent() {
		if (getInnerFirstEditor().getValue() == null) {
			return null;
		}
		return getInnerFirstEditor().getValue().toString().split(" ")[0];
	}

	@Override
	public Object getValueLastComponent() {
		if (getInnerLastEditor().getValue() == null) {
			return null;
		}
		return getInnerLastEditor().getValue().toString().split(" ")[0];
	}
	
}