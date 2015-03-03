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

import java.io.*;
import java.util.*;

import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Address Loaction Lookup Model.
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MLocationLookup.java,v 1.11 2005/09/24 01:53:34 jjanke Exp $
 */
public final class MLocationLookup extends Lookup
	implements Serializable
{
	/**
	 *	Constructor
	 *  @param ctx context
	 *  @param WindowNo window no (to derive AD_Client/Org for new records)
	 */
	public MLocationLookup(Properties ctx, int WindowNo)
	{
		super (DisplayType.TableDir, WindowNo);
		m_ctx = ctx;
	}	//	MLocation

	/**	Context					*/
	private Properties 		m_ctx;

	/**
	 *	Get Display for Value (not cached)
	 *  @param value Location_ID
	 *  @return String Value
	 */
	public String getDisplay (Object value)
	{
		if (value == null)
			return null;
		MLocation loc = getLocation (value, null);
		if (loc == null)
			return "<" + value.toString() + ">";
		return loc.toString();
	}	//	getDisplay

	/**
	 *	Get Object of Key Value
	 *  @param value value
	 *  @return Object or null
	 */
	public NamePair get (Object value)
	{
		if (value == null)
			return null;
		MLocation loc = getLocation (value, null);
		if (loc == null)
			return null;
		return new KeyNamePair (loc.getC_Location_ID(), loc.toString());
	}	//	get

	/**
	 *  The Lookup contains the key 
	 *  @param key Location_ID
	 *  @return true if key known
	 */
	public boolean containsKey (Object key)
	{
		return getLocation(key, null) == null;
	}   //  containsKey

	
	/**************************************************************************
	 * 	Get Location
	 * 	@return Location
	 */
	public MLocation getLocation (Object key, String trxName)
	{
		if (key == null)
			return null;
		int C_Location_ID = 0;
		if (key instanceof Integer)
			C_Location_ID = ((Integer)key).intValue();
		else if (key != null)
			C_Location_ID = Integer.parseInt(key.toString());
		//
		return getLocation(C_Location_ID, trxName);
	}	//	getLocation
	
	/**
	 * 	Get Location
	 * 	@return Location
	 */
	public MLocation getLocation (int C_Location_ID, String trxName)
	{
		return MLocation.get(m_ctx, C_Location_ID, trxName);
	}	//	getC_Location_ID

	/**
	 *	Get underlying fully qualified Table.Column Name.
	 *	Used for VLookup.actionButton (Zoom)
	 *  @return column name
	 */
	public String getColumnName()
	{
		return "C_Location_ID";
	}   //  getColumnName

	/**
	 *	Return data as sorted Array - not implemented
	 *  @param mandatory mandatory
	 *  @param onlyValidated only validated
	 *  @param onlyActive only active
	 * 	@param temporary force load for temporary display
	 *  @return null
	 */
	public ArrayList<Object> getData (boolean mandatory, boolean onlyValidated, boolean onlyActive, boolean temporary)
	{
		log.log(Level.SEVERE, "not implemented");
		return null;
	}   //  getArray

}	//	MLocation
