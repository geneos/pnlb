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
 *  Set Project Type
 *
 *	@author Jorg Janke
 *	@version $Id: ProjectSetType.java,v 1.8 2005/03/11 20:25:58 jjanke Exp $
 */
public class ProjectSetType extends SvrProcess
{
	/**	Project directly from Project	*/
	private int				m_C_Project_ID = 0;
	/** Project Type Parameter			*/
	private int				m_C_ProjectType_ID = 0;

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
				continue;
			else if (name.equals("C_ProjectType_ID"))
				m_C_ProjectType_ID = ((BigDecimal)para[i].getParameter()).intValue();
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
		m_C_Project_ID = getRecord_ID();
		log.info("doIt - C_Project_ID=" + m_C_Project_ID + ", C_ProjectType_ID=" + m_C_ProjectType_ID);
		//
		MProject project = new MProject (getCtx(), m_C_Project_ID, get_TrxName());
		if (project.getC_Project_ID() == 0 || project.getC_Project_ID() != m_C_Project_ID)
			throw new IllegalArgumentException("Project not found C_Project_ID=" + m_C_Project_ID);
		if (project.getC_ProjectType_ID_Int() > 0)
			throw new IllegalArgumentException("Project already has Type (Cannot overwrite) " + project.getC_ProjectType_ID());
		//
		MProjectType type = new MProjectType (getCtx(), m_C_ProjectType_ID, get_TrxName());
		if (type.getC_ProjectType_ID() == 0 || type.getC_ProjectType_ID() != m_C_ProjectType_ID)
			throw new IllegalArgumentException("Project Type not found C_ProjectType_ID=" + m_C_ProjectType_ID);

		//	Set & Copy if Service
		project.setProjectType(type);
		if (!project.save())
			throw new Exception ("@Error@");
		//
		return "@OK@";
	}	//	doIt

}	//	ProjectSetType
