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

public class ZynParameterSearch extends ZynParameterSingle {
    
        private String tabla = "";
        private String columna = "";

	public ZynParameterSearch(int m_WindowNo, MZYNMODELCOLUMN column) {
		super(column, ParameterType.TYPE_SEARCH, new VLookup(column.getAD_COLUMN_NAME(), false, false, true,
				MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, column.getAD_Column_ID(), DisplayType.Search)) {

			@Override
			public void setValue(Object arg0) {
				super.setValue(arg0);
			}
		});
                tabla = column.getAD_TABLE_NAME();
                columna = column.getAD_COLUMN_NAME();
	}
	
	@Override
	public int assignToStatement(int idx, PreparedStatement stmt) {
		Object valueFirstRange = getValueFirstComponent();
		try {
			if (valueFirstRange != null) {
				stmt.setString(idx++, valueFirstRange.toString());
			}
		} catch (SQLException ex) {
			log.log(Level.SEVERE, "error assign amount to statement", ex);
		}
		return idx;
	}

	@Override
	public Object getValueFirstComponent() {
		VLookup editor = (VLookup) getInnerFirstEditor();
                if (editor.getDisplayText().length() == 0) {
			return null;
		}
                if (tabla.equals("M_Product")){
                    String cut = editor.getDisplayText().toString();
                    cut = cut.substring(cut.indexOf("_"));
                    cut = cut.substring(1);
                    return cut;
                }else
                    return editor.getDisplayText();
	}
}
