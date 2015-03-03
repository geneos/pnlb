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
 * 	Actual Click
 *
 *  @author Jorg Janke
 *  @version $Id: MClick.java,v 1.9 2005/11/06 01:17:27 jjanke Exp $
 */
public class MClick extends X_W_Click
{
	/**
	 * 	Get Unprocessed Clicks
	 *	@param ctx context
	 *	@return array of unprocessed clicks
	 */
	public static MClick[] getUnprocessed(Properties ctx)
	{
		ArrayList<MClick> list = new ArrayList<MClick> ();
		String sql = "SELECT * FROM W_Click WHERE AD_Client_ID=? AND Processed = 'N'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, Env.getAD_Client_ID(ctx));
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MClick (ctx, rs, null));
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
		//
		MClick[] retValue = new MClick[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getUnprocessed
	
	/**	Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MClick.class);
	
	
	/**************************************************************************
	 * 	Actual Click
	 *	@param ctx context
	 *	@param W_Click_ID ID
	 */
	public MClick (Properties ctx, int W_Click_ID, String trxName)
	{
		super (ctx, W_Click_ID, trxName);
		if (W_Click_ID == 0)
			setProcessed (false);
	}	//	MClick

	/**
	 * 	Actual Click
	 *	@param ctx context
	 *	@param TargetURL url
	 */
	public MClick (Properties ctx, String TargetURL, String trxName)
	{
		this (ctx, 0, trxName);
		setTargetURL(TargetURL);
	}	//	MClick

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MClick (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MClick
	
	/**
	 * 	Set Target URL. Reset Click Count
	 *	@param TargetURL
	 */
	public void setTargetURL(String TargetURL)
	{
		super.setTargetURL(TargetURL);
		super.setW_ClickCount_ID(0);
	}	//	setTargetURL

	/**
	 * 	Find Click Count.
	 */
	public void setW_ClickCount_ID ()
	{
		//	clean up url
		String url = getTargetURL();
		if (url == null || url.length() == 0)
			return;
		String exactURL = url;
		//	remove everything before first / .
		if (url.startsWith("http://"))
			url = url.substring(7);
		int dot = url.indexOf('.');
		int slash = url.indexOf('/');
		while (dot > slash && slash != -1)
		{
			url = url.substring(slash+1);
			dot = url.indexOf('.');
			slash = url.indexOf('/');
		}
		//	remove everything after /
		if (slash != -1)
			url = url.substring(0, slash);
		log.fine(exactURL + " -> " + url);
		int W_ClickCount_ID = search (url, exactURL);
		//	try minumum
		if (W_ClickCount_ID == 0)
		{
			int lastDot = url.lastIndexOf('.');
			int firstDot = url.indexOf('.');
			while (lastDot != firstDot)
			{
				url = url.substring(firstDot+1);
				lastDot = url.lastIndexOf('.');
				firstDot = url.indexOf('.');
			}
			log.fine(exactURL + " -> " + url);
			W_ClickCount_ID = search (url, exactURL);
		}
		//	Not found
		if (W_ClickCount_ID == 0)
		{
			log.warning ("Not found: " + url 
				+ " (" + exactURL + ") Referrer=" + getReferrer());
			return;
		}
		//	OK
		setProcessed(true);
		super.setW_ClickCount_ID (W_ClickCount_ID);
	}	//	setW_ClickCount_ID

	/**
	 * 	Search for Click Count
	 *	@param url url
	 *	@param exactURL original url
	 *	@return W_ClickCount_ID
	 */
	private int search (String url, String exactURL)
	{
		String sql = "SELECT W_ClickCount_ID, TargetURL FROM W_ClickCount WHERE TargetURL LIKE ?";
		int W_ClickCount_ID = 0;
		int exactW_ClickCount_ID = 0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, "%" + url + "%");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				W_ClickCount_ID = rs.getInt(1);
				if (exactURL.equals(rs.getString(2)))
				{
					exactW_ClickCount_ID = W_ClickCount_ID;
					break;
				}
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//	Set Click Count
		if (exactW_ClickCount_ID != 0)
			W_ClickCount_ID = exactW_ClickCount_ID;
		return W_ClickCount_ID;
	}	//	search
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getW_ClickCount_ID() == 0)
			setW_ClickCount_ID();
		return true;
	}	//	beforeSave
	
	/**************************************************************************
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		Compiere.startup(true);
		Env.setContext(Env.getCtx(), "#AD_Client_ID", 1000000);
		MClick[] clicks = getUnprocessed(Env.getCtx());
		int counter = 0;
		for (int i = 0; i < clicks.length; i++)
		{
			MClick click = clicks[i];
			if (click.getW_ClickCount_ID() == 0)
			{
				click.setW_ClickCount_ID();
				if (click.getW_ClickCount_ID() != 0)
				{
					click.save();
					counter++;
				}
			}
		}
		System.out.println("#" + counter);
	}	//	main
	
}	//	MClick
