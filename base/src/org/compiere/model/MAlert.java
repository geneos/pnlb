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
 *	Alert Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAlert.java,v 1.9 2005/11/06 01:17:27 jjanke Exp $
 */
public class MAlert extends X_AD_Alert
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Alert_ID id
	 */
	public MAlert (Properties ctx, int AD_Alert_ID, String trxName)
	{
		super (ctx, AD_Alert_ID, trxName);
		if (AD_Alert_ID == 0)
		{
		//	setAD_AlertProcessor_ID (0);
		//	setName (null);
		//	setAlertMessage (null);
		//	setAlertSubject (null);
			setEnforceClientSecurity (true);	// Y
			setEnforceRoleSecurity (true);	// Y
			setIsValid (true);	// Y
		}	
	}	//	MAlert

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAlert (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAlert

	
	/**	The Rules						*/
	private MAlertRule[]		m_rules	= null;
	/**	The Recipients					*/
	private MAlertRecipient[]	m_recipients = null;

	/**
	 * 	Get Rules
	 *	@param reload reload data
	 *	@return array of rules
	 */
	public MAlertRule[] getRules (boolean reload)
	{
		if (m_rules != null && !reload)
			return m_rules;
		String sql = "SELECT * FROM AD_AlertRule "
			+ "WHERE AD_Alert_ID=?";
		ArrayList<MAlertRule> list = new ArrayList<MAlertRule>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, getAD_Alert_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MAlertRule (getCtx(), rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		//
		m_rules = new MAlertRule[list.size ()];
		list.toArray (m_rules);
		return m_rules;
	}	//	getRules
	
	/**
	 * 	Get Recipients
	 *	@param reload reload data
	 *	@return array of recipients
	 */
	public MAlertRecipient[] getRecipients (boolean reload)
	{
		if (m_recipients != null && !reload)
			return m_recipients;
		String sql = "SELECT * FROM AD_AlertRecipient " 
			+ "WHERE AD_Alert_ID=?";
		ArrayList<MAlertRecipient> list = new ArrayList<MAlertRecipient>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, getAD_Alert_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MAlertRecipient (getCtx(), rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		//
		m_recipients = new MAlertRecipient[list.size ()];
		list.toArray (m_recipients);
		return m_recipients;
	}	//	getRecipients

	/**
	 * 	Get First Role if exist
	 *	@return AD_Role_ID or -1
	 */
	public int getFirstAD_Role_ID()
	{
		getRecipients(false);
		for (int i = 0; i < m_recipients.length; i++)
		{
			if (m_recipients[i].getAD_Role_ID() != -1)
				return m_recipients[i].getAD_Role_ID();
		}
		return -1;
	}	//	getForstAD_Role_ID
	
	/**
	 * 	Get First User Role if exist
	 *	@return AD_Role_ID or -1
	 */
	public int getFirstUserAD_Role_ID()
	{
		getRecipients(false);
		int AD_User_ID = getFirstAD_User_ID();
		if (AD_User_ID != -1)
		{
			MUserRoles[] urs = MUserRoles.getOfUser(getCtx(), AD_User_ID);
			for (int i = 0; i < urs.length; i++)
			{
				if (urs[i].isActive())
					return urs[i].getAD_Role_ID();
			}
		}
		return -1;
	}	//	getFirstUserAD_Role_ID

	/**
	 * 	Get First User if exist
	 *	@return AD_User_ID or -1
	 */
	public int getFirstAD_User_ID()
	{
		getRecipients(false);
		for (int i = 0; i < m_recipients.length; i++)
		{
			if (m_recipients[i].getAD_User_ID() != -1)
				return m_recipients[i].getAD_User_ID();
		}
		return -1;
	}	//	getFirstAD_User_ID
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAlert[");
		sb.append(get_ID())
			.append("-").append(getName())
			.append(",Valid=").append(isValid());
		if (m_rules != null)
			sb.append(",Rules=").append(m_rules.length);
		if (m_recipients != null)
			sb.append(",Recipients=").append(m_recipients.length);
		sb.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MAlert
