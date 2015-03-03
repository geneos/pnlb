/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import org.compiere.util.*;

/**
 *	Persistent Column Model
 *	
 *  @author Jorg Janke
 *  @version $Id: M_Column.java,v 1.19 2005/10/20 04:51:56 jjanke Exp $
 */
public class M_Column extends X_AD_Column
{
	/**
	 * 	Get M_Column from Cache
	 *	@param ctx context
	 * 	@param AD_Column_ID id
	 *	@return M_Column
	 */
	public static M_Column get (Properties ctx, int AD_Column_ID)
	{
		Integer key = new Integer (AD_Column_ID);
		M_Column retValue = (M_Column) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new M_Column (ctx, AD_Column_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Column Name
	 *	@param ctx context
	 *	@param AD_Column_ID id
	 *	@return Column Name or null
	 */
	public static String getColumnName (Properties ctx, int AD_Column_ID)
	{
		M_Column col = M_Column.get(ctx, AD_Column_ID);
		if (col.get_ID() == 0)
			return null;
		return col.getColumnName();
	}	//	getColumnName
	
	/**	Cache						*/
	private static CCache<Integer,M_Column>	s_cache	= new CCache<Integer,M_Column>("AD_Column", 20);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Column_ID
	 */
	public M_Column (Properties ctx, int AD_Column_ID, String trxName)
	{
		super (ctx, AD_Column_ID, trxName);
		if (AD_Column_ID == 0)
		{
		//	setAD_Element_ID (0);
		//	setAD_Reference_ID (0);
		//	setColumnName (null);
		//	setName (null);
		//	setEntityType (null);	// U
			setIsAlwaysUpdateable (false);	// N
			setIsEncrypted (false);
			setIsIdentifier (false);
			setIsKey (false);
			setIsMandatory (false);
			setIsParent (false);
			setIsSelectionColumn (false);
			setIsTranslated (false);
			setIsUpdateable (true);	// Y
			setVersion (Env.ZERO);
		}
	}	//	M_Column

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public M_Column (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	M_Column
	
	/**
	 * 	Parent Constructor
	 *	@param parent table
	 */
	public M_Column (M_Table parent)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setAD_Table_ID (parent.getAD_Table_ID());
		setEntityType(parent.getEntityType());
	}	//	M_Column
	
	
	/**
	 * 	Is Standard Column
	 *	@return true for AD_Client_ID, etc.
	 */
	public boolean isStandardColumn()
	{
		String columnName = getColumnName();
		if (columnName.equals("AD_Client_ID") 
			|| columnName.equals("AD_Org_ID")
			|| columnName.equals("IsActive")
			|| columnName.startsWith("Created")
			|| columnName.startsWith("Updated") )
			return true;
		
		return false;
	}	//	isStandardColumn
	
	/**
	 * 	Is Virtual Column
	 *	@return true if virtual column
	 */
	public boolean isVirtualColumn()
	{
		String s = getColumnSQL();
		return s != null && s.length() > 0;
	}	//	isVirtualColumn
	
	/**
	 * 	Is the Column Encrypted?
	 *	@return true if encrypted
	 */
	public boolean isEncrypted()
	{
		String s = getIsEncrypted();
		return "Y".equals(s);
	}	//	isEncrypted
	
	/**
	 * 	Set Encrypted
	 *	@param IsEncrypted encrypted
	 */
	public void setIsEncrypted (boolean IsEncrypted)
	{
		setIsEncrypted (IsEncrypted ? "Y" : "N");
	}	//	setIsEncrypted
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Length
		if (getFieldLength() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "FieldLength"));
			return false;
		}
		
		/** Views are not updateable
		UPDATE AD_Column c
		SET IsUpdateable='N', IsAlwaysUpdateable='N'
		WHERE AD_Table_ID IN (SELECT AD_Table_ID FROM AD_Table WHERE IsView='Y')
		**/
		
		//	Virtual Column
		if (isVirtualColumn())
		{
			if (isMandatory())
				setIsMandatory(false);
			if (isUpdateable())
				setIsUpdateable(false);
		}
		//	Updateable
		if (isAlwaysUpdateable() && !isUpdateable())
			setIsAlwaysUpdateable(false);
		//	Encrypted
		if (isEncrypted()) 
		{
			int dt = getAD_Reference_ID();
			if (isKey() || isParent() || isStandardColumn()
				|| isVirtualColumn() || isIdentifier() || isTranslated()
				|| DisplayType.isLookup(dt) || DisplayType.isLOB(dt)
				|| "DocumentNo".equalsIgnoreCase(getColumnName())
				|| "Value".equalsIgnoreCase(getColumnName())
				|| "Name".equalsIgnoreCase(getColumnName()))
			{
				log.warning("Encryption not sensible - " + getColumnName());
				setIsEncrypted(false);
			}
		}	
		return true;
	}	//	beforeSave

	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Update Fields
		if (!newRecord)
		{
			StringBuffer sql = new StringBuffer("UPDATE AD_Field SET Name=")
				.append(DB.TO_STRING(getName()))
				.append(", Description=").append(DB.TO_STRING(getDescription()))
				.append(", Help=").append(DB.TO_STRING(getHelp()))
				.append(" WHERE AD_Column_ID=").append(get_ID())
				.append(" AND IsCentrallyMaintained='Y'");
			int no = DB.executeUpdate(sql.toString(), get_TrxName());
			log.fine("afterSave - Fields updated #" + no);
		}
		return success;
	}	//	afterSave
	
	/**
	 * 	Get SQL Add command
	 *	@param table table
	 *	@return sql
	 */
	public String getSQLAdd (M_Table table)
	{
		StringBuffer sql = new StringBuffer ("ALTER TABLE ")
			.append(table.getTableName())
			.append(" ADD ").append(getSQLDDL());
		return sql.toString();
	}	//	getSQLAdd

	/**
	 * 	Get SQL DDL
	 *	@return columnName datataype ..
	 */
	public String getSQLDDL()
	{
		StringBuffer sql = new StringBuffer (getColumnName())
			.append(" ").append(getSQLDataType());
		//	Default
		if (getDefaultValue() != null && getDefaultValue().length() > 0)
		{
			sql.append(" DEFAULT ");
			if (DisplayType.isText(getAD_Reference_ID()))
				sql.append(DB.TO_STRING(getDefaultValue()));
			else
				sql.append(getDefaultValue());
		}
		//	Inline Constraint
		if (getAD_Reference_ID() == DisplayType.YesNo)
			sql.append(" CHECK (").append(getColumnName()).append(" IN ('Y','N'))");

		//	Null
		if (isMandatory())
			sql.append(" NOT NULL");
		return sql.toString();
	}	//	getSQLDDL	
	
	/**
	 * 	Get SQL Modify command
	 *	@param table table
	 *	@param setNullOption generate null / not null statement
	 *	@return sql separated by ;
	 */
	public String getSQLModify (M_Table table, boolean setNullOption)
	{
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlBase = new StringBuffer ("ALTER TABLE ")
			.append(table.getTableName())
			.append(" MODIFY ").append(getColumnName());
		
		//	Default
		StringBuffer sqlDefault = new StringBuffer(sqlBase)
			.append(" ").append(getSQLDataType())
			.append(" DEFAULT ");
		String defaultValue = getDefaultValue();
		if (defaultValue != null 
			&& defaultValue.length() > 0
			&& defaultValue.indexOf("@") == -1)		//	no variables
		{
			if (DisplayType.isText(getAD_Reference_ID()) 
				|| getAD_Reference_ID() == DisplayType.List
				|| getAD_Reference_ID() == DisplayType.YesNo)
			{
				if (!defaultValue.startsWith("'") && !defaultValue.endsWith("'"))
					defaultValue = DB.TO_STRING(defaultValue);
			}
			sqlDefault.append(defaultValue);
		}
		else
		{
			sqlDefault.append(" NULL ");
			defaultValue = null;
		}
		sql.append(sqlDefault);
		
		//	Constraint

		//	Null Values
		if (isMandatory() && defaultValue != null && defaultValue.length() > 0)
		{
			StringBuffer sqlSet = new StringBuffer("UPDATE ")
				.append(table.getTableName())
				.append(" SET ").append(getColumnName())
				.append("=").append(defaultValue)
				.append(" WHERE ").append(getColumnName()).append(" IS NULL");
			sql.append(DB.SQLSTATEMENT_SEPARATOR).append(sqlSet);
		}
		
		//	Null
		if (setNullOption)
		{
			StringBuffer sqlNull = new StringBuffer(sqlBase);
			if (isMandatory())
				sqlNull.append(" NOT NULL");
			else
				sqlNull.append(" NULL");
			sql.append(DB.SQLSTATEMENT_SEPARATOR).append(sqlNull);
		}
		//
		return sql.toString();
	}	//	getSQLModify

	/**
	 * 	Get SQL Data Type
	 *	@return e.g. NVARCHAR2(60)
	 */
	private String getSQLDataType()
	{
		int dt = getAD_Reference_ID();
		if (DisplayType.isID(dt) || dt == DisplayType.Integer)
			return "NUMBER(10)";
		if (DisplayType.isDate(dt))
			return "DATE";
		if (DisplayType.isNumeric(dt))
			return "NUMBER";
		if (dt == DisplayType.Binary || dt == DisplayType.Image)
			return "BLOB";
		if (dt == DisplayType.TextLong)
			return "CLOB";
		if (dt == DisplayType.YesNo)
			return "CHAR(1)";
		if (dt == DisplayType.List)
			return "CHAR(" + getFieldLength() + ")";
		else if (!DisplayType.isText(dt))
			log.severe("Unhandled Data Type = " + dt);
			
		return "NVARCHAR2(" + getFieldLength() + ")";
	}	//	getSQLDataType
	
	/**
	 * 	Get Table Constraint
	 *	@param tableName table name
	 *	@return table constraint
	 */
	public String getConstraint(String tableName)
	{
		if (isKey())
			return "CONSTRAINT " + tableName + "_Key PRIMARY KEY (" + getColumnName() + ")";
		/**
		if (getAD_Reference_ID() == DisplayType.TableDir 
			|| getAD_Reference_ID() == DisplayType.Search)
			return "CONSTRAINT " ADTable_ADTableTrl
				+ " FOREIGN KEY (" + getColumnName() + ") REFERENCES "
				+ AD_Table(AD_Table_ID) ON DELETE CASCADE
		**/
		
		return "";
	}	//	getConstraint
	
}	//	M_Column
