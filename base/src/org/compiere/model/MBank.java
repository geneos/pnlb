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
 * 	Bank Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MBank.java,v 1.1 2005/11/12 22:58:56 jjanke Exp $
 */
public class MBank extends X_C_Bank
{
	/**
	 * 	Get MBank from Cache
	 *	@param ctx context
	 *	@param C_Bank_ID id
	 *	@return MBank
	 */
	public static MBank get (Properties ctx, int C_Bank_ID)
	{
		Integer key = new Integer (C_Bank_ID);
		MBank retValue = (MBank)s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MBank (ctx, C_Bank_ID, null);
		if (retValue.get_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MBank> s_cache = 
		new CCache<Integer,MBank> ("C_Bank", 3);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Bank_ID bank
	 *	@param trxName trx
	 */
	public MBank (Properties ctx, int C_Bank_ID, String trxName)
	{
		super (ctx, C_Bank_ID, trxName);
	}	//	MBank

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MBank (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MBank

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MBank[");
		sb.append (get_ID ()).append ("-").append(getName ()).append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MBank
