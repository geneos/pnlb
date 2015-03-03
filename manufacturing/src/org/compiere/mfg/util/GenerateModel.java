/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is             Compiere  ERP & CRM Smart Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.mfg.util;

import java.sql.*;
import java.io.*;
import java.math.*;
import org.compiere.util.*;

/**
 *  Generate Model Classes extending PO.
 *  Base class for CMP interface - will be extended to create byte code directly
 *
 *  @author Jorg Janke
 *  @version $Id: GenerateModel.java,v 1.2 2004/02/27 19:49:34 sklakken Exp $
 */
public class GenerateModel
{
	/**
	 * 	Generate PO Class
	 * 	@param AD_Table_ID table id
	 * 	@param directory directory with \ or / at the end.
	 */
	public GenerateModel (int AD_Table_ID, String directory)
	{
		//	create column access methods
		StringBuffer mandatory = new StringBuffer();
		StringBuffer sb = createColumns(AD_Table_ID, mandatory);
		// add header stuff
		String tableName = createHeader(AD_Table_ID, sb, mandatory);
		//	Save it
		writeToFile (sb, directory + tableName + ".java");
	}	//	GenerateModel

	/**
	 * 	Add Header info to buffer
	 * 	@param AD_Table_ID table
	 * 	@param sb buffer
	 * 	@param mandatory init call for mandatory columns
	 * 	@return class name
	 */
	private String createHeader (int AD_Table_ID, StringBuffer sb, StringBuffer mandatory)
	{
		String tableName = "";
		String sql = "SELECT TableName FROM AD_Table WHERE AD_Table_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, AD_Table_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				tableName = rs.getString(1);
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			Log.error("GenerateModel.createHeader", e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		if (tableName == null)
			throw new RuntimeException ("TableName not found for ID=" + AD_Table_ID);
		//
		String keyColumn = tableName + "_ID";
		String className = "X_" + tableName;
		//
		StringBuffer start = new StringBuffer ()
			.append (
			"/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2003 Jorg Janke */\n"
			+ "package org.compiere.model;"
			+ "import java.util.*;"
			+ "import java.sql.*;"
			+ "import java.math.*;"
			+ "import java.io.Serializable;"
			+ "import org.compiere.util.*;"
			//	Constructor
			+ "/** Generated Model for ").append(tableName).append("\n"
			+ " ** @version $Id: GenerateModel.java,v 1.2 2004/02/27 19:49:34 sklakken Exp $ */\n"
			+ "public class ").append(className).append(" extends PO"
			+ "{"
			//
			+ "/** Standard Constructor */\n"
			+ "public ").append(className).append(" (Properties ctx, int ").append(keyColumn).append(")"
			+ "{"
			+ "super (ctx, ").append(keyColumn).append(");"
			+ "/** if (").append(keyColumn).append(" == 0)"
			+ "{").append(mandatory).append("} */\n"
			+ "}"	//	Constructor End
			//	Load Constructor
			+ "/** Load Constructor */\n"
			+ "public ").append(className).append(" (Properties ctx, ResultSet rs)"
			+ "{"
			+ "super (ctx, rs);"
			+ "}"	//	Load Constructor End
			//
			+ "public static final int Table_ID=").append(AD_Table_ID).append(";\n"
			+ "/** Load Meta Data */\n"
			+ "protected POInfo initPO (Properties ctx)"
			+ "{"
			+ "POInfo poi = POInfo.getPOInfo (ctx, Table_ID);"
			+ "return poi;"
			+ "}"		//	initPO
			//
			+ "public String toString()"
			+ "{"
			+ "StringBuffer sb = new StringBuffer (\"").append(className).append("[\")"
			+ ".append(getID()).append(\"]\");"
			+ "return sb.toString();"
			+ "}");

		StringBuffer end = new StringBuffer ("}");
		//
		sb.insert(0, start);
		sb.append(end);

		return className;
	}	//	createHeader


	/**
	 * 	Create Column access methods
	 * 	@param AD_Table_ID table
	 * 	@param mandatory init call for mandatory columns
	 * 	@return set/get method
	 */
	private StringBuffer createColumns (int AD_Table_ID, StringBuffer mandatory)
	{
		StringBuffer sb = new StringBuffer();
		String sql = "SELECT c.ColumnName, c.IsUpdateable, c.IsMandatory,"		//	1..3
			+ " c.AD_Reference_ID, c.AD_Reference_Value_ID, DefaultValue, SeqNo, "	//	4..7
			+ " c.FieldLength, c.ValueMin, c.ValueMax, c.VFormat, c.Callout, "	//	8..12
			+ " c.Name, c.Description "
			+ "FROM AD_Column c "
			+ "WHERE c.AD_Table_ID=?"
			+ " AND c.ColumnName <> 'AD_Client_ID'"
			+ " AND c.ColumnName <> 'AD_Org_ID'"
			+ " AND c.ColumnName <> 'IsActive'"
			+ " AND c.ColumnName NOT LIKE 'Created%'"
			+ " AND c.ColumnName NOT LIKE 'Updated%' "
			+ "ORDER BY c.ColumnName";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, AD_Table_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				String columnName = rs.getString(1);
				boolean isUpdateable = "Y".equals(rs.getString(2));
				boolean isMandatory = "Y".equals(rs.getString(3));
				int displayType = rs.getInt(4);
				int AD_Reference_Value_ID = rs.getInt(5);
				String defaultValue = rs.getString(6);
				int seqNo = rs.getInt(7);
				int fieldLength = rs.getInt(8);
				String ValueMin = rs.getString(9);
				String ValueMax = rs.getString(10);
				String VFormat = rs.getString(11);
				String Callout = rs.getString(12);
				String Name = rs.getString(13);
				String Description = rs.getString(14);
				//
				sb.append(createColumnMethods (mandatory,
					columnName, isUpdateable, isMandatory, 
					displayType, AD_Reference_Value_ID, fieldLength, 
					defaultValue, ValueMin, ValueMax, VFormat,
					Callout, Name, Description));
				//	
				if (seqNo == 1)
					sb.append(createKeyNamePair(columnName, displayType));
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			Log.error("GenerateModel.createColumns", e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		return sb;
	}	//	createColumns

	/**
	 *	Create set/get methods for column
	 * 	@param mandatory init call for mandatory columns
	 * 	@param columnName column name
	 * 	@param isUpdateable updateable
	 * 	@param isMandatory mandatory
	 * 	@param displayType display type
	 * 	@param AD_Reference_ID validation reference
	 * 	@param defaultValue default value
	 * 	@return set/get method
	 */
	private String createColumnMethods (StringBuffer mandatory,
		String columnName, boolean isUpdateable, boolean isMandatory,
		int displayType, int AD_Reference_ID, int fieldLength, 
		String defaultValue, String ValueMin, String ValueMax, String VFormat,
		String Callout, String Name, String Description)
	{
		Class clazz = DisplayType.getClass(displayType, true);
		if (defaultValue == null)
			defaultValue = "";
		//	Handle Posted
		if (columnName.equalsIgnoreCase("Posted") 
			|| columnName.equalsIgnoreCase("Processed")
			|| columnName.equalsIgnoreCase("Processing"))
		{
			clazz = Boolean.class;
			AD_Reference_ID = 0;
		}
		//	Record_ID
		else if (columnName.equalsIgnoreCase("Record_ID"))
		{
			clazz = Integer.class;
			AD_Reference_ID = 0;
		}
		//	String Key
		else if (columnName.equalsIgnoreCase("AD_Language"))
		{
			clazz = String.class;
		}	
		//	Data Type
		String dataType = clazz.getName();
		dataType = dataType.substring(dataType.lastIndexOf('.')+1);
		if (dataType.equals("Boolean"))
			dataType = "boolean";
		else if (dataType.equals("Integer"))
			dataType = "int";
		else if (displayType == DisplayType.Binary || displayType == DisplayType.Image)
			dataType = "byte[]";


		StringBuffer sb = new StringBuffer();
		//	****** Set Comment ******
		sb.append("/** Set ").append(Name);
		if (Description != null && Description.length() > 0)
			sb.append(".\n").append(Description);
		sb.append(" */\n");

		//	Set	********
		String setValue = "setValue";
		//	public void setColumn (xxx variable)
		if (isUpdateable)
			sb.append("public ");
		else
			setValue = "setValueNoCheck";
		sb.append("void set").append(columnName).append(" (").append(dataType).append(" ").append(columnName).append(")"
			+ "{");
		//	List Validation
		if (AD_Reference_ID != 0)
		{
			String staticVar = addListValidation (sb, AD_Reference_ID, columnName, !isMandatory);
			sb.insert(0, staticVar);
		}
		//	setValue ("ColumnName", xx);
		if (clazz.equals(Integer.class))
		{
			if (columnName.endsWith("_ID") && !isMandatory)	//	set uptional _ID to null if 0
				sb.append("if (").append (columnName).append (" == 0) ")
					.append(setValue).append(" (\"").append(columnName).append("\", null); else \n");
			sb.append(setValue).append(" (\"").append(columnName).append("\", new Integer(").append(columnName).append("));");
		}
		else if (clazz.equals(Boolean.class))
			sb.append(setValue).append(" (\"").append(columnName).append("\", new Boolean(").append(columnName).append("));");
		else
		{
			if (isMandatory)	//	does not apply to int/boolean
				sb.append ("if (").append (columnName).append (" == null)"
				  + " throw new IllegalArgumentException (\"").append(columnName).append(" is mandatory\");");
			// String length check
			if (clazz.equals(String.class) && fieldLength > 0)
			{
				sb.append ("if (");
				if (!isMandatory)
					sb.append(columnName).append(" != null && ");
				sb.append(columnName).append(".length() > ").append(fieldLength)
					.append("){log.warn(\"set").append(columnName)
					.append(" - length > ").append(fieldLength).append(" - truncated\");")
					.append(columnName).append(" = ")
					.append(columnName).append(".substring(0,").append(fieldLength-1).append(");}");
			}
					  
			//
			sb.append (setValue).append(" (\"").append (columnName).append ("\", ")
				.append (columnName).append (");");
		}
		sb.append("}");

		//	Mandatory call in constructor
		if (isMandatory)
		{
			mandatory.append("set").append(columnName).append(" (");
			if (clazz.equals(Integer.class))
				mandatory.append("0");
			else if (clazz.equals(Boolean.class))
			{
				if (defaultValue.indexOf('Y') != -1)
					mandatory.append(true);
				else
					mandatory.append("false");
			}
			else if (clazz.equals(BigDecimal.class))
				mandatory.append("Env.ZERO");
			else if (clazz.equals(Timestamp.class))
				mandatory.append("new Timestamp(System.currentTimeMillis())");
			else
				mandatory.append("null");
			mandatory.append(");");
			if (defaultValue.length() > 0)
				mandatory.append("// ").append(defaultValue).append(Env.NL);
		}


		//	****** Get Comment ****** 
		sb.append("/** Get ").append(Name);
		if (Description != null && Description.length() > 0)
			sb.append(".\n").append(Description);
		sb.append(" */\n");
		
		//	Get	********
		sb.append("public ").append(dataType);
		if (clazz.equals(Boolean.class))
		{
			sb.append(" is");
			if (columnName.toLowerCase().startsWith("is"))
				sb.append(columnName.substring(2));
			else
				sb.append(columnName);
		}
		else
			sb.append(" get").append(columnName);
		sb.append("() {");
		if (clazz.equals(Integer.class))
			sb.append("Integer ii = (Integer)getValue(\"").append(columnName).append("\");"
				+ "if (ii == null)"
				+ " return 0;"
				+ "return ii.intValue();");
		else if (clazz.equals(BigDecimal.class))
			sb.append("BigDecimal bd = (BigDecimal)getValue(\"").append(columnName).append("\");"
				+ "if (bd == null)"
				+ " return Env.ZERO;"
				+ "return bd;");
		else if (clazz.equals(Boolean.class))
			sb.append("Object oo = getValue(\"").append(columnName).append("\");"
				+ "if (oo != null) { if (oo instanceof Boolean) return ((Boolean)oo).booleanValue(); return \"Y\".equals(oo);}"
				+ "return false;");
		else if (dataType.equals("Object"))
			sb.append("return getValue(\"").append(columnName).append("\");");
		else
			sb.append("return (").append(dataType).append(")getValue(\"").append(columnName).append("\");");
		sb.append("}");
		//
		return sb.toString();
	}	//	createColumnMethods


	/**
	 * 	Add List Validation
	 * 	@param sb buffer - example:
		if (NextAction.equals("N") || NextAction.equals("F"));
		else throw new IllegalArgumentException ("NextAction Invalid value - Reference_ID=219 - N - F");
	 * 	@param AD_Reference_ID reference
	 * 	@param columnName column
	 * 	@param nullable the validation must allow null values
	 * 	@return static parameter - Example:
		public static final int NEXTACTION_AD_Reference_ID=219;
		public static final String NEXTACTION_None = "N";
		public static final String NEXTACTION_FollowUp = "F";
	 */
	private String addListValidation (StringBuffer sb, int AD_Reference_ID, 
		String columnName, boolean nullable)
	{
		StringBuffer retValue = new StringBuffer();
		retValue.append("public static final int ").append(columnName.toUpperCase())
			.append("_AD_Reference_ID=").append(AD_Reference_ID).append(";");
		//
		boolean found = false;
		StringBuffer values = new StringBuffer("Reference_ID=")
			.append(AD_Reference_ID);
		StringBuffer statement = new StringBuffer();
		if (nullable)
			statement.append("if (").append(columnName).append(" == null");
		
		String sql = "SELECT Value, Name FROM AD_Ref_List WHERE AD_Reference_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, AD_Reference_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				String value = rs.getString(1);
				values.append(" - ").append(value);
				if (statement.length() == 0)
					statement.append("if (").append(columnName)
						.append(".equals(\"").append(value).append("\")");
				else
					statement.append(" || ").append(columnName)
						.append(".equals(\"").append(value).append("\")");
				found = true;
				//	Name (SmallTalkNotation)
				String name = rs.getString(2);
				char[] nameArray = name.toCharArray();
				StringBuffer nameClean = new StringBuffer();
				boolean initCap = true;
				for (int i = 0; i < nameArray.length; i++)
				{
					char c = nameArray[i];
					if (Character.isJavaIdentifierPart(c))
					{
						if (initCap)
							nameClean.append(Character.toUpperCase(c));
						else
							nameClean.append(c);
						initCap = false;
					}
					else
					{
						if (c == '+')
							nameClean.append("Plus");
						else if (c == '-')
							nameClean.append("_");
						else if (c == '>')
						{
							if (name.indexOf('<') == -1)	//	ignore <xx>
								nameClean.append("Gt");
						}
						else if (c == '<')
						{
							if (name.indexOf('>') == -1)	//	ignore <xx>
								nameClean.append("Le");
						}
						else if (c == '=')
							nameClean.append("Eq");
						else if (c == '~')
							nameClean.append("Like");
						initCap = true;
					}
				}
				retValue.append("/** ").append(name).append(" = ").append(value).append(" */\n");
				retValue.append("public static final String ").append(columnName.toUpperCase())
					.append("_").append(nameClean)
					.append(" = \"").append(value).append("\";");
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			Log.error("GenerateMethod.addListValidation", e);
			found = false;
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		statement.append(")"
			+ "; "
			+ "else "
			+ "throw new IllegalArgumentException (\"").append(columnName)
			.append(" Invalid value - ").append(values).append("\");");
		//
		if (found)
			sb.append (statement);
		return retValue.toString();
	}	//	addListValidation

	/**
	 * 	Create getKeyNamePair() method with first identifier
	 *	@param columnName name
	 *	@return method code
	 */
	private StringBuffer createKeyNamePair (String columnName, int displayType)
	{
		String method = "get" + columnName + "()";
		if (displayType != DisplayType.String)
			method = "String.valueOf(" + method + ")";
		StringBuffer sb = new StringBuffer("public KeyNamePair getKeyNamePair() "
			+ "{return new KeyNamePair(getID(), ").append(method).append(");}");
		return sb;
	}	//	createKeyNamePair


	/**************************************************************************
	 * 	Write to file
	 * 	@param sb string buffer
	 * 	@param fileName file name
	 */
	private void writeToFile (StringBuffer sb, String fileName)
	{
		try
		{
			File out = new File (fileName);
			FileWriter fw = new FileWriter (out);
			for (int i = 0; i < sb.length(); i++)
			{
				char c = sb.charAt(i);
				//	after
				if (c == ';' || c == '}')
				{
					fw.write (c);
					if (sb.substring(i+1).startsWith("//"))
						fw.write('\t');
					else
						fw.write(Env.NL);
				}
				//	before & after
				else if (c == '{')
				{
					fw.write(Env.NL);
					fw.write (c);
					fw.write(Env.NL);
				}
				else
					fw.write (c);
			}
			fw.flush ();
			fw.close ();
			float size = out.length();
			size /= 1024;
			Log.trace(Log.l1_User, out.getAbsolutePath() + " - " + size + " kB");
		}
		catch (Exception ex)
		{
			Log.error("GenerateModel.writeToFile", ex);
		}
	}	//	writeToFile

	/**
	 * 	String representation
	 * 	@return string representation
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("GenerateModel[")
			.append("]");
		return sb.toString();
	}	//	toString



	/**************************************************************************
	 * 	Generate PO Model Class.
	 * 	@param args directory entityType
	 * 	Example java GenerateModel.class mydirectory UA
	 * 	would generate entity type User and Application classes into mydirectory 
	 */
	public static void main (String[] args)
	{
		//	first parameter
		String directory = "C:\\Work\\compiere-mfg\\manufacturing\\src\\org\\compiere\\mfg\\model\\";
		if (args.length > 0)
			directory = args[0];
		if (directory == null || directory.length() == 0)
		{
			System.err.println("No Directory");
			System.exit(1);
		}
		
		//	second parameter
		String entityType = "A";	//	Directory & Compiere
		if (args.length > 1)
			entityType = args[1]; 
		if (entityType == null || entityType.length() == 0)
		{
			System.err.println("No EntityType");
			System.exit(1);
		}
		StringBuffer sql = new StringBuffer("EntityType IN (");
		for (int i = 0; i < entityType.length(); i++)
		{
			if (i > 0)
				sql.append(",");
			sql.append("'").append(entityType.charAt(i)).append("'");
		}
		sql.append(")");	//	close IN
		System.out.println("Generate Model to " + directory + " for " + sql);
		System.out.println("-----------------------------------------------");
		
		//	complete sql
		sql.insert(0, "SELECT AD_Table_ID "
			+ "FROM AD_Table "
			+ "WHERE (TableName IN ('RV_WarehousePrice')"
			+ " OR IsView='N')"
			+ " AND TableName NOT LIKE '%_Trl' AND ");
		sql.append(" ORDER BY TableName");
		
		//
		org.compiere.Compiere.startupClient();
		int count = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				new GenerateModel(rs.getInt(1), directory);
				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("GenerateModel.main - " + e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		System.out.println("Generated = " + count);

	}	//	main

}	//	GenerateModel
