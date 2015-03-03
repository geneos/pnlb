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

import java.sql.ResultSet;
import java.util.Properties;

/**
 *	Preference Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPreference.java,v 1.5 2005/11/10 18:15:48 jjanke Exp $
 */
public class MPreference extends X_AD_Preference
{
	/**	Null Indicator				*/
	public static String		NULL = "null";
	
	/**
	 * 	Standatrd Constructor
	 *	@param ctx ctx
	 *	@param AD_Preference_ID id
	 */
	public MPreference(Properties ctx, int AD_Preference_ID, String trxName)
	{
		super(ctx, AD_Preference_ID, trxName);
		if (AD_Preference_ID == 0)
		{
		//	setAD_Preference_ID (0);
		//	setAttribute (null);
		//	setValue (null);
		}
	}	//	MPreference

	/**
	 * 	Load Contsructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPreference(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPreference

	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param Attribute attribute
	 *	@param Value value
	 *	@param trxName trx
	 */
	public MPreference (Properties ctx, String Attribute, String Value, String trxName)
	{
		this (ctx, 0, trxName);
		setAttribute (Attribute);
		setValue (Value);
	}	//	MPreference

	/**
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		String value = getValue();
		if (value == null)
			value = "";
		if (value.equals("-1"))
			setValue("");
		return true;
	}	//	beforeSave

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPreference[");
		sb.append (get_ID()).append("-")
			.append(getAttribute()).append("-").append(getValue())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MPreference
