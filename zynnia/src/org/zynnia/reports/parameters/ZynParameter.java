/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.parameters;

import java.awt.Component;
import java.sql.PreparedStatement;
import org.compiere.model.MZYNMODELCOLUMN;
import org.compiere.swing.CLabel;
import org.compiere.util.CLogger;

public abstract class ZynParameter {

	protected static CLogger log = CLogger.getCLogger(ZynParameter.class);

	private final int parameterID;

	private final String parameterName;

	private final String parameterTableName;

	private final ParameterType parameterType;

	public ZynParameter(MZYNMODELCOLUMN column, ParameterType type) {
		parameterID = column.getZYN_MODEL_COLUMN_ID();
		parameterName = column.getName();
		parameterTableName = column.getAD_TABLE_NAME();
		parameterType = type;
	}

	public int getParameterID() {
		return parameterID;
	}

	public String getParameterName() {
		return parameterName;
	}
	
	public String getParameterTableName() {
		return parameterTableName;
	}

	public ParameterType getParameterType() {
		return parameterType;
	}

	public abstract boolean isSingle();

	public abstract Component getFirstComponent();

	public abstract Component getLastComponent();

	public abstract String getQueryParameter();

	public abstract int assignToStatement(int idx, PreparedStatement stmt);

	public abstract Object getValueFirstComponent();

	public abstract Object getValueLastComponent();
}
