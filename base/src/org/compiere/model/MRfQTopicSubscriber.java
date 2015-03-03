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
 *	RfQ Topic Subscriber Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQTopicSubscriber.java,v 1.6 2005/11/14 02:10:53 jjanke Exp $
 */
public class MRfQTopicSubscriber extends X_C_RfQ_TopicSubscriber
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RfQ_TopicSubscriber_ID id
	 */
	public MRfQTopicSubscriber (Properties ctx, int C_RfQ_TopicSubscriber_ID, String trxName)
	{
		super (ctx, C_RfQ_TopicSubscriber_ID, trxName);
	}	//	MRfQTopic_Subscriber

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRfQTopicSubscriber (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRfQTopic_Subscriber
	
	/**	Restrictions					*/
	private MRfQTopicSubscriberOnly[] m_restrictions = null;
	
	/**
	 * 	Get Restriction Records
	 *	@param requery requery
	 *	@return arry of onlys
	 */
	public MRfQTopicSubscriberOnly[] getRestrictions (boolean requery)
	{
		if (m_restrictions != null && !requery)
			return m_restrictions;
		
		ArrayList<MRfQTopicSubscriberOnly> list = new ArrayList<MRfQTopicSubscriberOnly>();
		String sql = "SELECT * FROM C_RfQ_TopicSubscriberOnly WHERE C_RfQ_TopicSubscriber_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_RfQ_TopicSubscriber_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MRfQTopicSubscriberOnly(getCtx(), rs, get_TrxName()));
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
		
		m_restrictions = new MRfQTopicSubscriberOnly[list.size ()];
		list.toArray (m_restrictions);
		return m_restrictions;
	}	//	getRestrictions
	
	
	/**
	 * 	Is the product included?
	 *	@param M_Product_ID product
	 *	@return true if no restrictions or included in "positive" only list
	 */
	public boolean isIncluded (int M_Product_ID)
	{
		//	No restrictions
		if (getRestrictions(false).length == 0)
			return true;
		
		for (int i = 0; i < m_restrictions.length; i++)
		{
			MRfQTopicSubscriberOnly restriction = m_restrictions[i];
			if (!restriction.isActive())
				continue;
			//	Product
			if (restriction.getM_Product_ID() == M_Product_ID)
				return true;
			//	Product Category
			if (MProductCategory.isCategory(restriction.getM_Product_Category_ID(), M_Product_ID))
				return true;
		}
		//	must be on "positive" list
		return false;
	}	//	isIncluded
	
}	//	MRfQTopicSubscriber
