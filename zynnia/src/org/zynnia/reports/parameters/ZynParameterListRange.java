/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.parameters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import org.compiere.grid.ed.VLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MZYNMODELCOLUMN;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;

public class ZynParameterListRange extends ZynParameterRange {

	public ZynParameterListRange(int m_WindowNo, MZYNMODELCOLUMN column) {
		super(column, ParameterType.TYPE_LIST_RANGE,
				new VLookup(column.getAD_COLUMN_NAME(), false, false, true,
						MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, column.getAD_Column_ID(), DisplayType.Table)) {

					@Override
					public void setValue(Object arg0) {
						super.setValue(arg0);
					}
				},
				new VLookup(column.getAD_COLUMN_NAME(), false, false, true,
						MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, column.getAD_Column_ID(), DisplayType.List)) {

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
				stmt.setInt(idx++, Integer.parseInt(getInnerFirstEditor().getValue().toString()));
			}
			if (valueLastRange != null) {
				stmt.setInt(idx++, Integer.parseInt(getInnerLastEditor().getValue().toString()));
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
		return Integer.parseInt(getInnerFirstEditor().getValue().toString());
	}

	@Override
	public Object getValueLastComponent() {
		if (getInnerLastEditor().getValue() == null) {
			return null;
		}
		return Integer.parseInt(getInnerLastEditor().getValue().toString());
	}

}
