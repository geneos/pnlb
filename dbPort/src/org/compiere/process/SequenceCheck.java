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
package org.compiere.process;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	System + Document Sequence Check
 *	
 *  @author Jorg Janke
 *  @version $Id: SequenceCheck.java,v 1.11 2005/10/28 01:00:20 jjanke Exp $
 */
public class SequenceCheck extends SvrProcess
{
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (SequenceCheck.class);
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
	}	//	prepare
	
	/**
	 *  Perform process.
	 *  (see also MSequenve.validate)
	 *  @return Message to be translated
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		log.info("");
		//
		checkTableSequences (Env.getCtx(), this);
		checkTableID (Env.getCtx(), this);
		checkClientSequences (Env.getCtx(), this);
		return "Sequence Check";
	}	//	doIt
	
	/**
	 *	Validate Sequences
	 *	@param ctx context
	 */
	public static void validate(Properties ctx)
	{
		try
		{
			checkTableSequences (ctx, null);
			checkTableID (ctx, null);
			checkClientSequences (ctx, null);
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "validate", e);
		}
	}	//	validate
	
	
	
	/**************************************************************************
	 * 	Check existence of Table Sequences.
	 *	@param ctx comtext
	 *	@param sp server process or null
	 */
	private static void checkTableSequences (Properties ctx, SvrProcess sp)
	{
		String trxName = null;
		if (sp != null)
			trxName = sp.get_TrxName();
		String sql = "SELECT TableName "
			+ "FROM AD_Table t "
			+ "WHERE IsActive='Y' AND IsView='N'"
			+ " AND NOT EXISTS (SELECT * FROM AD_Sequence s "
			+ "WHERE UPPER(s.Name)=UPPER(t.TableName) AND s.IsTableID='Y')";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				String tableName = rs.getString(1);
				if (MSequence.createTableSequence (ctx, tableName, trxName))
				{
					if (sp != null)
						sp.addLog(0, null, null, tableName);
					else
						s_log.fine(tableName);
				}
				else
				{
					rs.close();
					throw new Exception ("Error creating Table Sequence for " + tableName);
				}
			}
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
		
		//	Sync Table Name case
		sql = "UPDATE AD_Sequence s "
			+ "SET Name = (SELECT TableName FROM AD_Table t "
				+ "WHERE t.IsView='N' AND UPPER(s.Name)=UPPER(t.TableName)) "
			+ "WHERE s.IsTableID='Y'"
			+ " AND EXISTS (SELECT * FROM AD_Table t "
				+ "WHERE t.IsActive='Y' AND t.IsView='N'"
				+ " AND UPPER(s.Name)=UPPER(t.TableName) AND s.Name<>t.TableName)";
		int no = DB.executeUpdate(sql, trxName);
		if (no > 0)
		{
			if (sp != null)
				sp.addLog(0, null, null, "SyncName #" + no);
			else
				s_log.fine("Sync #" + no);
		}
		if (no >= 0)
			return;
		
		/** Find Duplicates 		 */
		sql = "SELECT TableName, s.Name "
			+ "FROM AD_Table t, AD_Sequence s "
			+ "WHERE t.IsActive='Y' AND t.IsView='N'"
			+ " AND UPPER(s.Name)=UPPER(t.TableName) AND s.Name<>t.TableName";
		//
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				String TableName = rs.getString(1);
				String SeqName = rs.getString(2);
				sp.addLog(0, null, null, "ERROR: TableName=" + TableName + " - Sequence=" + SeqName);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
	}	//	checkTableSequences
	

	/**
	 * 	Check Table Sequence ID values
	 *	@param ctx comtext
	 *	@param sp server process or null
	 */
	private static void checkTableID (Properties ctx, SvrProcess sp)
	{
		int IDRangeEnd = DB.getSQLValue(null,
			"SELECT IDRangeEnd FROM AD_System");
		if (IDRangeEnd <= 0)
			IDRangeEnd = DB.getSQLValue(null,
				"SELECT MIN(IDRangeStart)-1 FROM AD_Replication");
		s_log.info("IDRangeEnd = " + IDRangeEnd);
		//
		String sql = "SELECT * FROM AD_Sequence "
			+ "WHERE IsTableID='Y' "
			+ "ORDER BY Name";
		int counter = 0;
		PreparedStatement pstmt = null;
		String trxName = null;
		if (sp != null)
			trxName = sp.get_TrxName();
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MSequence seq = new MSequence (ctx, rs, trxName);
				int old = seq.getCurrentNext();
				int oldSys = seq.getCurrentNextSys();
				if (seq.validateTableIDValue())
				{
					if (seq.getCurrentNext() != old)
					{
						String msg = seq.getName() + " ID  " 
							+ old + " -> " + seq.getCurrentNext();
						if (sp != null)
							sp.addLog(0, null, null, msg);
						else
							s_log.fine(msg);
					}
					if (seq.getCurrentNextSys() != oldSys)
					{
						String msg = seq.getName() + " Sys " 
							+ oldSys + " -> " + seq.getCurrentNextSys();
						if (sp != null)
							sp.addLog(0, null, null, msg);
						else
							s_log.fine(msg);
					}
					if (seq.save())
						counter++;
					else
						s_log.severe("Not updated: " + seq);
				}
			//	else if (CLogMgt.isLevel(6)) 
			//		log.fine("checkTableID - skipped " + tableName);
			}
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
		s_log.fine("#" + counter);
	}	//	checkTableID

	
	/**
	 *	Check/Initialize DocumentNo/Value Sequences for all Clients 	
	 *	@param ctx comtext
	 *	@param sp server process or null
	 */
	private static void checkClientSequences (Properties ctx, SvrProcess sp)
	{
		String trxName = null;
		if (sp != null)
			trxName = sp.get_TrxName();
		//	Sequence for DocumentNo/Value
		MClient[] clients = MClient.getAll(ctx);
		for (int i = 0; i < clients.length; i++)
		{
			MClient client = clients[i];
			if (!client.isActive())
				continue;
			MSequence.checkClientSequences (ctx, client.getAD_Client_ID(), trxName);
		}	//	for all clients
		
	}	//	checkClientSequences
	
}	//	SequenceCheck
