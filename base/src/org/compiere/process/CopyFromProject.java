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
import org.compiere.model.*;

/**
 *  Copy Project Details
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromProject.java,v 1.5 2005/03/11 20:25:57 jjanke Exp $
 */
public class CopyFromProject extends SvrProcess
{
	private int		m_C_Project_ID = 0;

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
			else if (name.equals("C_Project_ID"))
				m_C_Project_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		int To_C_Project_ID = getRecord_ID();
		log.info("doIt - From C_Project_ID=" + m_C_Project_ID + " to " + To_C_Project_ID);
		if (To_C_Project_ID == 0)
			throw new IllegalArgumentException("Target C_Project_ID == 0");
		if (m_C_Project_ID == 0)
			throw new IllegalArgumentException("Source C_Project_ID == 0");
		MProject from = new MProject (getCtx(), m_C_Project_ID, get_TrxName());
		MProject to = new MProject (getCtx(), To_C_Project_ID, get_TrxName());
		//
		int no = to.copyDetailsFrom (from);

		return "@Copied@=" + no;
	}	//	doIt

}	//	CopyFromProject
