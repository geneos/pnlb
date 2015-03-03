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

import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Open/Close all Period (Control)
 *	
 *  @author Jorg Janke
 *  @version $Id: PeriodStatus.java,v 1.8 2005/09/19 04:49:45 jjanke Exp $
 */
public class PeriodStatus extends SvrProcess
{
	/** Period						*/
	private int			p_C_Period_ID = 0;
	/** Action						*/
	private String		p_PeriodAction = null;
	
	
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
			else if (name.equals("PeriodAction"))
				p_PeriodAction = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_Period_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		log.info ("C_Period_ID=" + p_C_Period_ID + ", PeriodAction=" + p_PeriodAction);
		MPeriod period = new MPeriod (getCtx(), p_C_Period_ID, get_TrxName());
		if (period.get_ID() == 0)
			throw new IllegalArgumentException("@NotFound@  @C_Period_ID@=" + p_C_Period_ID);

		StringBuffer sql = new StringBuffer ("UPDATE C_PeriodControl ");
		sql.append("SET PeriodStatus='");
		//	Open
		if (MPeriodControl.PERIODACTION_OpenPeriod.equals(p_PeriodAction))
			sql.append (MPeriodControl.PERIODSTATUS_Open);
		//	Close
		else if (MPeriodControl.PERIODACTION_ClosePeriod.equals(p_PeriodAction))
			sql.append (MPeriodControl.PERIODSTATUS_Closed);
		//	Close Permanently
		else if (MPeriodControl.PERIODACTION_PermanentlyClosePeriod.equals(p_PeriodAction))
			sql.append (MPeriodControl.PERIODSTATUS_PermanentlyClosed);
		else
			return "-";
		//
		sql.append("', PeriodAction='N', Updated=SysDate,UpdatedBy=").append(getAD_User_ID());
		//	WHERE
		sql.append(" WHERE C_Period_ID=").append(period.getC_Period_ID())
			.append(" AND PeriodStatus<>'P'")
			.append(" AND PeriodStatus<>'").append(p_PeriodAction).append("'");
			
		int no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		CacheMgt.get().reset("C_PeriodControl", 0);
		CacheMgt.get().reset("C_Period", p_C_Period_ID);
		return "@Updated@ #" + no;
	}	//	doIt

}	//	PeriodStatus
