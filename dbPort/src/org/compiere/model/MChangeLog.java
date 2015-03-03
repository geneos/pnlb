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
import java.util.logging.*;

import org.compiere.*;
import org.compiere.util.*;

/**
 *	Change Log Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MChangeLog.java,v 1.19 2005/10/26 00:38:17 jjanke Exp $
 */
public class MChangeLog extends X_AD_ChangeLog
{
	/**
	 * 	Do we track changes for this table
	 *	@param AD_Table_ID table
	 *	@return true if changes are tracked
	 */
	public static boolean isLogged (int AD_Table_ID)
	{
		if (s_changeLog == null || s_changeLog.length == 0)
			fillChangeLog();
		//
		int index = Arrays.binarySearch(s_changeLog, AD_Table_ID);
		return index >= 0;
	}	//	trackChanges
	
	/**
	 *	Fill Log with tables to be logged 
	 */
	private static void fillChangeLog()
	{
		ArrayList<Integer> list = new ArrayList<Integer>(40);
		String sql = "SELECT t.AD_Table_ID FROM AD_Table t "
			+ "WHERE t.IsChangeLog='Y'"					//	also inactive
			+ " OR EXISTS (SELECT * FROM AD_Column c "
				+ "WHERE t.AD_Table_ID=c.AD_Table_ID AND c.ColumnName='EntityType') "
			+ "ORDER BY t.AD_Table_ID";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new Integer(rs.getInt(1)));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		//	Convert to Array
		s_changeLog = new int [list.size()];
		for (int i = 0; i < s_changeLog.length; i++)
		{
			Integer id = (Integer)list.get(i);
			s_changeLog[i] = id.intValue();
		}
		s_log.info("#" + s_changeLog.length);
	}	//	fillChangeLog

	/**	Change Log				*/
	private static int[]		s_changeLog = null;
	/**	Logger					*/
	private static CLogger		s_log = CLogger.getCLogger(MChangeLog.class);
	/** NULL Value				*/
	public static String		NULL = "NULL";
	
	
	/**************************************************************************
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MChangeLog(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MChangeLog

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_ChangeLog_ID id
	 *	@param trxName trx
	 */
	public MChangeLog (Properties ctx, int AD_ChangeLog_ID, String trxName)
	{
		super (ctx, 0, trxName);
	}	//	MChangeLog
	
	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param AD_ChangeLog_ID 0 for new change log
	 *	@param AD_Session_ID session
	 *	@param AD_Table_ID table
	 *	@param AD_Column_ID column
	 *	@param Record_ID record
	 *	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 *	@param OldValue old
	 *	@param NewValue new
	 */
	public MChangeLog (Properties ctx, 
		int AD_ChangeLog_ID, String TrxName, int AD_Session_ID, 
		int AD_Table_ID, int AD_Column_ID, int Record_ID,
		int AD_Client_ID, int AD_Org_ID,
		Object OldValue, Object NewValue)
	{
		this (ctx, 0, null);	//	 out of trx
		if (AD_ChangeLog_ID == 0)
		{
			AD_ChangeLog_ID = DB.getNextID (AD_Client_ID, Table_Name, null);
			if (AD_ChangeLog_ID <= 0)
				log.severe("No NextID (" + AD_ChangeLog_ID + ")");
		}
		setAD_ChangeLog_ID (AD_ChangeLog_ID);
		setTrxName(TrxName);
		setAD_Session_ID (AD_Session_ID);
		//
		setAD_Table_ID (AD_Table_ID);
		setAD_Column_ID (AD_Column_ID);
		setRecord_ID (Record_ID);
		//
		setClientOrg (AD_Client_ID, AD_Org_ID);
		//
		setOldValue (OldValue);
		setNewValue (NewValue);
		//	R2.5.2f_2005-09-25  2.5.2f_20050925-2201
		setDescription(Compiere.MAIN_VERSION + "_" 
			+ Compiere.DATE_VERSION + " " + Compiere.getImplementationVersion());
	}	//	MChangeLog

	
	/**
	 * 	Set Old Value
	 *	@param OldValue old
	 */
	public void setOldValue (Object OldValue)
	{
		if (OldValue == null)
			super.setOldValue (NULL);
		else
			super.setOldValue (OldValue.toString());
	}	//	setOldValue

	/**
	 * 	Is Old Value Null
	 *	@return true if null
	 */
	public boolean isOldNull()
	{
		String value = getOldValue();
		return value == null || value.equals(NULL);
	}	//	isOldNull
	
	/**
	 * 	Set New Value
	 *	@param NewValue new
	 */
	public void setNewValue (Object NewValue)
	{
		if (NewValue == null)
			super.setNewValue (NULL);
		else
			super.setNewValue (NewValue.toString());
	}	//	setNewValue
	
	/**
	 * 	Is New Value Null
	 *	@return true if null
	 */
	public boolean isNewNull()
	{
		String value = getNewValue();
		return value == null || value.equals(NULL);
	}	//	isNewNull
	
}	//	MChangeLog
