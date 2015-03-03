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
 *  Copy Column Set at the end of the Column Set
 *
 *  @author Jorg Janke
 *  @version $Id: ReportColumnSet_Copy.java,v 1.11 2005/03/11 20:25:58 jjanke Exp $
 */
public class ReportColumnSet_Copy extends SvrProcess
{
	/**
	 * 	Constructor
	 */
	public ReportColumnSet_Copy()
	{
		super();
	}	//	ReportColumnSet_Copy

	/**	Source Line Set					*/
	private int		m_PA_ReportColumnSet_ID = 0;

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
			else if (name.equals("PA_ReportColumnSet_ID"))
				m_PA_ReportColumnSet_ID = ((BigDecimal)para[i].getParameter()).intValue();
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
		log.info("From PA_ReportColumnSet_ID=" + m_PA_ReportColumnSet_ID + ", To=" + to_ID);
		if (to_ID < 1)
			throw new Exception(MSG_SaveErrorRowNotFound);
		//
		MReportColumnSet to = new MReportColumnSet(getCtx(), to_ID, get_TrxName());
		MReportColumnSet rcSet = new MReportColumnSet(getCtx(), m_PA_ReportColumnSet_ID, get_TrxName());
		MReportColumn[] rcs = rcSet.getColumns();
		for (int i = 0; i < rcs.length; i++)
		{
			MReportColumn rc = MReportColumn.copy (getCtx(), to.getAD_Client_ID(), to.getAD_Org_ID(), to_ID, rcs[i], get_TrxName());
			rc.save();
		}
		//	Oper 1/2 were set to Null !
		return "@Copied@=" + rcs.length;
	}	//	doIt

}	//	ReportColumnSet_Copy
