/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.parameters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import org.compiere.grid.ed.VString;
import org.compiere.model.MZYNMODELCOLUMN;
import org.compiere.util.Env;

public class ZynParameterText extends ZynParameterSingle {

	public ZynParameterText(MZYNMODELCOLUMN column) {
		super(column, ParameterType.TYPE_TEXT, new VString(column.getAD_COLUMN_NAME(), false, false, true, 10, 20, null, null));
	}

	@Override
	public String getQueryParameter() {
		Object valueFirstEditor = getValueFirstComponent();
		if (valueFirstEditor == null || valueFirstEditor.toString().length() <= 0) {
			return "";
		}
		StringBuilder sqlQueryBuilder = new StringBuilder();
		MZYNMODELCOLUMN zModelColumn = new MZYNMODELCOLUMN(Env.getCtx(), getParameterID(), null);
		sqlQueryBuilder.append("LOWER(").append(zModelColumn.getCompleteNameForQuery()).append(")");
		sqlQueryBuilder.append(" LIKE ?");
		return sqlQueryBuilder.toString();
	}

	@Override
	public int assignToStatement(int idx, PreparedStatement stmt) {
		Object valueFirstRange = getValueFirstComponent();
		try {
			if (valueFirstRange != null && valueFirstRange.toString().length() > 0) {
				stmt.setString(idx++, "%" + valueFirstRange.toString().toLowerCase() + "%");
			}
		} catch (SQLException ex) {
			log.log(Level.SEVERE, "error assign amount to statement", ex);
		}
		return idx;
	}

	@Override
	public Object getValueFirstComponent() {
            if (getInnerFirstEditor().getValue()== null) {
                return null;
            }
            return getInnerFirstEditor().getValue().toString();
	}

}
