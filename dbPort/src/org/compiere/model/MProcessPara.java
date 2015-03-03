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
 *  Process Parameter Model
 *
 *  @author Jorg Janke
 *  @version $Id: MProcessPara.java,v 1.7 2005/09/19 04:48:39 jjanke Exp $
 */
public class MProcessPara extends X_AD_Process_Para
{
	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param AD_Process_Para_ID id
	 */
	public MProcessPara (Properties ctx, int AD_Process_Para_ID, String trxName)
	{
		super (ctx, AD_Process_Para_ID, trxName);
	}	
	
	public MProcessPara (Properties ctx, int AD_Process_Para_ID, int AD_Process_ID, String trxName)
	{
		super (ctx, AD_Process_Para_ID, trxName);
		if (AD_Process_Para_ID == 0)
		{
		//	setAD_Process_ID (0);	Parent
		//	setName (null);
		//	setColumnName (null);
			
			setFieldLength (0);
			setSeqNo (0);
			setAD_Reference_ID (AD_Process_ID);
			setIsCentrallyMaintained (true);
			setIsRange (false);
			setIsMandatory (false);
			setEntityType (ENTITYTYPE_UserMaintained);
		}
	}	//	MProcessPara

	public static int		WINDOW_NO = 999;
	public static int		TAB_NO = 0;
	
	/**	The Lookup				*/
	private Lookup		m_lookup = null;
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MProcessPara (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProcessPara

	/**
	 *  Is this field a Lookup?.
	 *  @return true if lookup field
	 */
	public boolean isLookup()
	{
		boolean retValue = false;
		int displayType = getAD_Reference_ID(); 
		if (DisplayType.isLookup(displayType))
			retValue = true;
		else if (displayType == DisplayType.Location
			|| displayType == DisplayType.Locator
			|| displayType == DisplayType.Account
			|| displayType == DisplayType.PAttribute)
			retValue = true;
		return retValue;
	}   //  isLookup

	/**
	 *  Set Lookup for columns with lookup
	 */
	public void loadLookup()
	{
		if (!isLookup())
			return;
		log.fine("(" + getColumnName() + ")");
		int displayType = getAD_Reference_ID();
		if (DisplayType.isLookup(displayType))
		{
			MLookupInfo  lookupInfo = MLookupFactory.getLookupInfo(getCtx(), 0, 
				getAD_Process_Para_ID(), getAD_Reference_ID(), 
				Env.getLanguage(getCtx()), getColumnName(), 
				getAD_Reference_Value_ID(), false, "");
			if (lookupInfo == null)
			{
				log.log(Level.SEVERE, "(" + getColumnName() + ") - No LookupInfo");
				return;
			}
			//	Prevent loading of CreatedBy/UpdatedBy
			if (displayType == DisplayType.Table
				&& (getColumnName().equals("CreatedBy") || getColumnName().equals("UpdatedBy")) )
				lookupInfo.IsCreadedUpdatedBy = true;
			//
			MLookup ml = new MLookup (lookupInfo, TAB_NO);
			m_lookup = ml;
		}
		else if (displayType == DisplayType.Location)   //  not cached
		{
			MLocationLookup ml = new MLocationLookup (getCtx(), WINDOW_NO);
			m_lookup = ml;
		}
		else if (displayType == DisplayType.Locator)
		{
			MLocatorLookup ml = new MLocatorLookup (getCtx(), WINDOW_NO);
			m_lookup = ml;
		}
		else if (displayType == DisplayType.Account)    //  not cached
		{
			MAccountLookup ma = new MAccountLookup (getCtx(), WINDOW_NO);
			m_lookup = ma;
		}
		else if (displayType == DisplayType.PAttribute)    //  not cached
		{
			MPAttributeLookup pa = new MPAttributeLookup (getCtx(), WINDOW_NO);
			m_lookup = pa;
		}
		//
		if (m_lookup != null)
			m_lookup.loadComplete();
	}   //  loadLookup

	/**
	 * 	Get Lookup for Parameter
	 *	@return lookup or null
	 */
	public Lookup getLookup()
	{
		if (m_lookup == null && isLookup())
			loadLookup();
		return m_lookup;
	}	//	getLookup
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MProcessPara[")
			.append (get_ID ())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MProcessPara
