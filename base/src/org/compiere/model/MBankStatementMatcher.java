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
import org.compiere.impexp.*;
import org.compiere.util.*;

/**
 *	Bank Statement Matcher Algorithm
 *	
 *  @author Jorg Janke
 *  @version $Id: MBankStatementMatcher.java,v 1.6 2005/11/21 23:47:54 jjanke Exp $
 */
public class MBankStatementMatcher extends X_C_BankStatementMatcher
{
	/**
	 * 	Get Bank Statement Matcher Algorithms
	 *	@return matchers
	 */
	public static MBankStatementMatcher[] getMatchers (Properties ctx, String trxName)
	{
		ArrayList<MBankStatementMatcher> list = new ArrayList<MBankStatementMatcher>();
		String sql = MRole.getDefault(ctx, false).addAccessSQL(
			"SELECT * FROM C_BankStatementMatcher ORDER BY SeqNo", 
			"C_BankStatementMatcher", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add (new MBankStatementMatcher(ctx, rs, trxName));
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
		//	Convert		
		MBankStatementMatcher[] retValue = new MBankStatementMatcher[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getMatchers

	/** Static Logger					*/
	private static CLogger 	s_log = CLogger.getCLogger(MBankStatementMatcher.class);

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BankStatementMatcher_ID id
	 */
	public MBankStatementMatcher(Properties ctx, int C_BankStatementMatcher_ID, String trxName)
	{
		super(ctx, C_BankStatementMatcher_ID, trxName);
	}	//	MBankStatementMatcher

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MBankStatementMatcher(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBankStatementMatcher

	private BankStatementMatcherInterface	m_matcher = null;
	private Boolean							m_matcherValid = null;

	/**
	 * 	Is Matcher Valid
	 *	@return true if valid
	 */
	public boolean isMatcherValid()
	{
		if (m_matcherValid == null)
			getMatcher();
		return m_matcherValid.booleanValue();
	}	//	isMatcherValid

	/**
	 * 	Get Matcher 
	 *	@return Matcher Instance
	 */
	public BankStatementMatcherInterface getMatcher()
	{
		if (m_matcher != null 
			|| (m_matcherValid != null && m_matcherValid.booleanValue()))
			return m_matcher;
			
		String className = getClassname();
		if (className == null || className.length() == 0)
			return null;
		
		try
		{
			Class matcherClass = Class.forName(className);
			m_matcher = (BankStatementMatcherInterface)matcherClass.newInstance();
			m_matcherValid = Boolean.TRUE;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, className, e);
			m_matcher = null;
			m_matcherValid = Boolean.FALSE;
		}
		return m_matcher;
	}	//	getMatcher


}	//	MBankStatementMatcher
