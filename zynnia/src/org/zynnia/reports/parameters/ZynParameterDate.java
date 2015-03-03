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

public class ZynParameterDate extends ZynParameterSingle {

	public ZynParameterDate(MZYNMODELCOLUMN column) {
		super(column, ParameterType.TYPE_DATE, new VDate(column.getAD_COLUMN_NAME(), false, false, true, DisplayType.Date, column.getName()) {

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
		try {
			if (valueFirstRange != null) {
				String date = getInnerFirstEditor().getValue().toString().split(" ")[0];
				stmt.setDate(idx++, Date.valueOf(date));
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
}
