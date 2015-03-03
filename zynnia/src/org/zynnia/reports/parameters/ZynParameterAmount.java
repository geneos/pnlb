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

public class ZynParameterAmount extends ZynParameterSingle {
	
	public ZynParameterAmount(MZYNMODELCOLUMN column) {
		super(column, ParameterType.TYPE_AMOUNT, new VNumber(column.getAD_COLUMN_NAME(), false, false, true,
								  DisplayType.Amount, column.getAD_COLUMN_NAME()));
	}

	@Override
	public int assignToStatement(int idx, PreparedStatement stmt) {
		Object valueFirstRange = getValueFirstComponent();
		try {
			if (valueFirstRange != null) {
				stmt.setInt(idx++, Integer.parseInt(getInnerFirstEditor().getValue().toString()));
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
}