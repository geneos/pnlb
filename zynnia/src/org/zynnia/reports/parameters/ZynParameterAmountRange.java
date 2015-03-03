/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.parameters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import org.compiere.grid.ed.VNumber;
import org.compiere.model.MZYNMODELCOLUMN;
import org.compiere.util.DisplayType;

public class ZynParameterAmountRange extends ZynParameterRange {
	
	public ZynParameterAmountRange(MZYNMODELCOLUMN column) {
		super(column, ParameterType.TYPE_AMOUNT_RANGE, 
				new VNumber(column.getAD_COLUMN_NAME(), false, false, true, DisplayType.Amount, column.getAD_COLUMN_NAME()),
				new VNumber(column.getAD_COLUMN_NAME(), false, false, true, DisplayType.Amount, column.getAD_COLUMN_NAME()));
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
				stmt.setDouble(idx++, Double.parseDouble(getInnerFirstEditor().getValue().toString()));
			}
			if (valueLastRange != null) {
				stmt.setDouble(idx++, Double.parseDouble(getInnerLastEditor().getValue().toString()));
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
		return Double.parseDouble(getInnerFirstEditor().getValue().toString());
	}

	@Override
	public Object getValueLastComponent() {
		if (getInnerLastEditor().getValue() == null) {
			return null;
		}
		return Double.parseDouble(getInnerLastEditor().getValue().toString());
	}
}