/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.parameters;

import java.awt.Component;
import javax.swing.JComponent;
import org.compiere.grid.ed.VEditor;
import org.compiere.model.MZYNMODELCOLUMN;
import org.compiere.util.Env;

public abstract class ZynParameterRange extends ZynParameter {
	
	private final VEditor firstInput;
	private final VEditor lastInput;

	public ZynParameterRange(MZYNMODELCOLUMN column, ParameterType type, VEditor firstInput, VEditor lastInput) {
		super(column, type);
		this.firstInput = firstInput;
		this.lastInput = lastInput;
	}

	protected VEditor getInnerFirstEditor() {
		return firstInput;
	}

	protected VEditor getInnerLastEditor() {
		return lastInput;
	}

	@Override
	public String getQueryParameter() {
		StringBuilder sqlQueryBuilder = new StringBuilder();
		MZYNMODELCOLUMN zModelColumn = new MZYNMODELCOLUMN(Env.getCtx(), getParameterID(), null);
		Object valueFirstEditor = getValueFirstComponent();
		Object valueLastEditor = getValueLastComponent();
		
		if (valueFirstEditor != null || valueLastEditor != null) {
			sqlQueryBuilder.append(zModelColumn.getCompleteNameForQuery());
		}
		if (valueFirstEditor != null && valueLastEditor != null) {
			sqlQueryBuilder.append(" BETWEEN ? AND ?");
		} else if (valueFirstEditor != null && valueLastEditor == null) {
			sqlQueryBuilder.append(" > ?");
		} else if (valueFirstEditor == null && valueLastEditor != null) {
			sqlQueryBuilder.append(" < ?");
		}
		return sqlQueryBuilder.toString();
	}

	@Override
	public Component getFirstComponent() {
		return (JComponent) firstInput;
	}
	
	@Override
	public Component getLastComponent() {
		return (JComponent) lastInput;
	}

	public boolean isSingle() {
		return false;
	}

}