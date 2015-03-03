package org.zynnia.reports.parameters;

import java.awt.Component;
import javax.swing.JComponent;
import org.compiere.grid.ed.VEditor;
import org.compiere.model.MZYNMODELCOLUMN;
import org.compiere.util.Env;

public abstract class ZynParameterSingle extends ZynParameter {

	private final VEditor firstInput;

	public ZynParameterSingle(MZYNMODELCOLUMN column, ParameterType type, VEditor localInput) {
		super(column, type);
		this.firstInput = localInput;
	}

	protected VEditor getInnerFirstEditor() {
		return firstInput;
	}

	@Override
	public String getQueryParameter() {
		Object valueFirstEditor = getValueFirstComponent();
		if (valueFirstEditor == null) {
			return "";
		}
		StringBuilder sqlQueryBuilder = new StringBuilder();
		MZYNMODELCOLUMN zModelColumn = new MZYNMODELCOLUMN(Env.getCtx(), getParameterID(), null);
		sqlQueryBuilder.append(zModelColumn.getCompleteNameForQuery());
		sqlQueryBuilder.append(" = ?");
		return sqlQueryBuilder.toString();
	}

	@Override
	public Component getFirstComponent() {
		return (JComponent) firstInput;
	}

    // Agregado para validar su uso
    // Pepo
    @Override
	public Component getLastComponent() {
		return null;
	}

	public boolean isSingle() {
		return true;
	}

	@Override
	public Object getValueLastComponent() {
		return null;
	}
}