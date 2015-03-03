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
import org.compiere.util.*;

/**
 *	Dunning Run Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDunningRun.java,v 1.6 2005/09/24 01:52:52 jjanke Exp $
 */
public class MDunningRun extends X_C_DunningRun
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_DunningRun_ID id
	 */
	public MDunningRun (Properties ctx, int C_DunningRun_ID, String trxName)
	{
		super (ctx, C_DunningRun_ID, trxName);
		if (C_DunningRun_ID == 0)
		{
		//	setC_DunningLevel_ID (0);
			setDunningDate (new Timestamp(System.currentTimeMillis()));
			setProcessed (false);
		}	
	}	//	MDunningRun

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MDunningRun (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MDunningRun

	private MDunningLevel		m_level = null;
	private MDunningRunEntry[]	m_entries = null;
	
	/**
	 * 	Get Dunning Level
	 *	@return level
	 */
	public MDunningLevel getLevel()
	{
		if (m_level == null)
			m_level = new MDunningLevel (getCtx(), getC_DunningLevel_ID(), get_TrxName());
		return m_level;
	}	//	getLevel
	
	/**
	 * 	Get Entries
	 * 	@param requery requery
	 *	@return entries
	 */
	public MDunningRunEntry[] getEntries (boolean requery)
	{
		if (m_entries != null && !requery)
			return m_entries;
		
		String sql = "SELECT * FROM C_DunningRunEntry WHERE C_DunningRun_ID=?";
		ArrayList<MDunningRunEntry> list = new ArrayList<MDunningRunEntry>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_DunningRun_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MDunningRunEntry(getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getEntries", e);
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
		m_entries = new MDunningRunEntry[list.size ()];
		list.toArray (m_entries);
		return m_entries;
	}	//	getEntries
	
	/**
	 * 	Delete all Entries
	 * 	@param force delete also processed records
	 *	@return true if deleted
	 */
	public boolean deleteEntries(boolean force)
	{
		getEntries(true);
		for (int i = 0; i < m_entries.length; i++)
		{
			MDunningRunEntry entry = m_entries[i];
			entry.delete(force);
		}
		boolean ok = getEntries(true).length == 0;
		if (ok)
			m_entries = null;
		return ok;
	}	//	deleteEntries
	
	/**
	 * 	Get/Create Entry for BPartner
	 *	@param C_BPartner_ID id
	 *	@return entry
	 */
	public MDunningRunEntry getEntry (int C_BPartner_ID, int C_Currency_ID, int SalesRep_ID)
	{
		// TODO: Related BP
		int C_BPartnerRelated_ID = C_BPartner_ID;
		//
		getEntries(false);
		for (int i = 0; i < m_entries.length; i++)
		{
			MDunningRunEntry entry = m_entries[i];
			if (entry.getC_BPartner_ID() == C_BPartnerRelated_ID)
				return entry;
		}
		//	New Entry
		MDunningRunEntry entry = new MDunningRunEntry (this);
		MBPartner bp = new MBPartner (getCtx(), C_BPartnerRelated_ID, get_TrxName());
		entry.setBPartner(bp, true);	//	AR hardcoded
		//
		if (entry.getSalesRep_ID() == 0)
			entry.setSalesRep_ID (SalesRep_ID);
		entry.setC_Currency_ID (C_Currency_ID);
		//
		m_entries = null;
		return entry;
	}	//	getEntry
	
}	//	MDunningRun
