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
package org.compiere.process;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Report System Issue
 *	
 *  @author Jorg Janke
 *  @version $Id: IssueReport.java,v 1.2 2005/12/31 06:33:21 jjanke Exp $
 */
public class IssueReport extends SvrProcess
{
	/**	Issue to report			*/
	private int	m_AD_Issue_ID = 0;
	
	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		m_AD_Issue_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Do It
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("AD_Issue_ID=" + m_AD_Issue_ID);
		if (!MSystem.get(getCtx()).isAutoErrorReport())
			return "NOT reported - Enable Error Reporting in Window System";
		//
		MIssue issue = new MIssue(getCtx(), m_AD_Issue_ID, get_TrxName());
		if (issue.get_ID() == 0)
			return "No Issue to report - ID=" + m_AD_Issue_ID;
		//
		String error = issue.report();
		if (error != null)
			throw new CompiereSystemError(error);
		if (issue.save())
			return "Issue Reported: " + issue.getRequestDocumentNo();
		throw new CompiereSystemError("Issue Not Saved");
	}	//	doIt
	
}	//	IssueReport
