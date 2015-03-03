/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;

public class FieldDynamicReport {

	private final int tableID;

	private final String fieldTitle;

	private final String tableName;

	private final String columName;

	private String completeColumName = "";

	private final boolean isSum;

	private final boolean isTransp;

    private final boolean isOrderby;

	private final int orderView;

    private final int fieldType;

	public FieldDynamicReport(BigDecimal tableID, String fieldTitle, String tableName, String columName, String isSum, String isTransp, String isOrderby, int orderView) {
		this.tableID = tableID.intValue();
		this.fieldTitle = fieldTitle;
		this.tableName = tableName;
		this.columName = columName;
		this.isSum = isSum.equals("Y");
		this.isTransp = isTransp.equals("Y");
		this.isOrderby = isOrderby.equals("Y");
		this.orderView = orderView;
        this.fieldType = DB.getSQLValue("txReport", "SELECT ad_reference_id FROM ad_column WHERE ad_table_id=? AND columnname=?", this.tableID, this.columName);
	}

	public FieldDynamicReport(String fieldTitle, boolean isSum, boolean isTransp, boolean isOrderby, int orderView) {
		this.tableID = 0;
		this.fieldTitle = fieldTitle;
		this.tableName = "calculado";
		this.columName = fieldTitle;
		this.isSum = isSum;
		this.isTransp = isTransp;
        this.isOrderby = isOrderby;
        this.orderView = orderView;
        this.fieldType = DisplayType.Number;
	}

	public String getColumName() {
		return columName;
	}

	public boolean isIsSum() {
		return isSum;
	}

	public boolean isIsTransp() {
		return isTransp;
	}

	public boolean isIsOrderby() {
		return isOrderby;
	}

	public String getTableName() {
		return tableName;
	}

	public int getTableID() {
		return tableID;
	}

	public int getOrderView() {
		return orderView;
	}

	public String getCompleteNameForQuery() {
		if(completeColumName.equals("")) {
			StringBuilder sqlQueryBuilder = new StringBuilder();
			sqlQueryBuilder.append(getTableName());
			sqlQueryBuilder.append(".").append(getColumName());
			completeColumName = sqlQueryBuilder.toString();
		}
		return completeColumName;
	}

	public String getFieldTitle() {
		return fieldTitle;
	}

    public int getDisplayType() {
         return fieldType;
    }

    public boolean isNumeric() {
        return DisplayType.isNumeric(fieldType);
    }

    public boolean isYesNo() {
       return DisplayType.YesNo == fieldType;
    }

    public boolean isDate() {
		return DisplayType.isDate(fieldType);
    }
}