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
package org.compiere.print;

import java.math.*;

import org.compiere.process.*;

/**
 *	MPrintFormat Process.
 *  Performs Copy existing or Create from Table
 *  Called when pressing the Copy/Create button in Window Print Format
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MPrintFormatProcess.java,v 1.7 2005/03/11 20:34:41 jjanke Exp $
 */
public class MPrintFormatProcess extends SvrProcess
{
	/** PrintFormat             */
	private BigDecimal	m_AD_PrintFormat_ID;
	/** Table	                */
	private BigDecimal	m_AD_Table_ID;

	/**
	 *  Prepare - get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_PrintFormat_ID"))
				m_AD_PrintFormat_ID = ((BigDecimal)para[i].getParameter());
			else if (name.equals("AD_Table_ID"))
				m_AD_Table_ID = ((BigDecimal)para[i].getParameter());
			else
				log.equals("prepare - Unknown Parameter=" + para[i].getParameterName());
		}
	}   //  prepare

	/**
	 *  Perrform process.
	 *  <pre>
	 *  If AD_Table_ID is not null, create from table,
	 *  otherwise copy from AD_PrintFormat_ID
	 *  </pre>
	 * @return Message
	 * @throws Exception
	 */
	protected String doIt() throws Exception
	{
		if (m_AD_Table_ID != null && m_AD_Table_ID.intValue() > 0)
		{
			log.info("Create from AD_Table_ID=" + m_AD_Table_ID);
			MPrintFormat pf = MPrintFormat.createFromTable(getCtx(), m_AD_Table_ID.intValue(), getRecord_ID());
			addLog(m_AD_Table_ID.intValue(), null, new BigDecimal(pf.getItemCount()), pf.getName());
			return pf.getName() + " #" + pf.getItemCount();
		}
		else if (m_AD_PrintFormat_ID != null && m_AD_PrintFormat_ID.intValue() > 0)
		{
			log.info("MPrintFormatProcess - Copy from AD_PrintFormat_ID=" + m_AD_PrintFormat_ID);
			MPrintFormat pf = MPrintFormat.copy (getCtx(), m_AD_PrintFormat_ID.intValue(), getRecord_ID());
			addLog(m_AD_PrintFormat_ID.intValue(), null, new BigDecimal(pf.getItemCount()), pf.getName());
			return pf.getName() + " #" + pf.getItemCount();
		}
		else
			throw new Exception (MSG_InvalidArguments);
	}	//	doIt

}	//	MPrintFormatProcess
