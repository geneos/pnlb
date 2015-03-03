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

import java.math.*;
import java.util.logging.*;
import org.compiere.report.*;

/**
 *	Copy Line Set at the end of the Line Set
 *
 *  @author Jorg Janke
 *  @version $Id: ReportLineSet_Copy.java,v 1.12 2005/09/19 04:49:45 jjanke Exp $
 */
public class ReportLineSet_Copy extends SvrProcess
{
	/**
	 * 	Constructor
	 */
	public ReportLineSet_Copy()
	{
		super();
	}	//	ReportLineSet_Copy

	/**	Source Line Set					*/
	private int		m_PA_ReportLineSet_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("PA_ReportLineSet_ID"))
				m_PA_ReportLineSet_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws Exception
	{
		int to_ID = super.getRecord_ID();
		log.info("From PA_ReportLineSet_ID=" + m_PA_ReportLineSet_ID + ", To=" + to_ID);
		if (to_ID < 1)
			throw new Exception(MSG_SaveErrorRowNotFound);
		//
		MReportLineSet to = new MReportLineSet(getCtx(), to_ID, get_TrxName());
		MReportLineSet rlSet = new MReportLineSet(getCtx(), m_PA_ReportLineSet_ID, get_TrxName());
		MReportLine[] rls = rlSet.getLiness();
		for (int i = 0; i < rls.length; i++)
		{
			MReportLine rl = MReportLine.copy (getCtx(), to.getAD_Client_ID(), to.getAD_Org_ID(), to_ID, rls[i], get_TrxName());
			rl.save();
			MReportSource[] rss = rls[i].getSources();
			if (rss != null)
			{
				for (int ii = 0; ii < rss.length; ii++)
				{
					MReportSource rs = MReportSource.copy (getCtx(), to.getAD_Client_ID(), to.getAD_Org_ID(), rl.get_ID(), rss[ii], get_TrxName());
					rs.save();
				}
			}
			//	Oper 1/2 were set to Null ! 
		}
		return "@Copied@=" + rls.length;
	}	//	doIt

}	//	ReportLineSet_Copy
