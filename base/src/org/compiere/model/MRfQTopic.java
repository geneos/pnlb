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
 *	RfQ Topic Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQTopic.java,v 1.7 2005/11/14 02:10:52 jjanke Exp $
 */
public class MRfQTopic extends X_C_RfQ_Topic
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RfQ_Topic_ID id
	 */
	public MRfQTopic (Properties ctx, int C_RfQ_Topic_ID, String trxName)
	{
		super (ctx, C_RfQ_Topic_ID, trxName);
	}	//	MRfQTopic

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRfQTopic (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRfQTopic

	/**
	 * 	Get Current Topic Subscribers
	 *	@return array subscribers
	 */
	public MRfQTopicSubscriber[] getSubscribers()
	{
		ArrayList<MRfQTopicSubscriber> list = new ArrayList<MRfQTopicSubscriber>();
		String sql = "SELECT * FROM C_RfQ_TopicSubscriber "
			+ "WHERE C_RfQ_Topic_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_RfQ_Topic_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRfQTopicSubscriber (getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getSubscribers", e);
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
		
		MRfQTopicSubscriber[] retValue = new MRfQTopicSubscriber[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getSubscribers
	
}	//	MRfQTopic

