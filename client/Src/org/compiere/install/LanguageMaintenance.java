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
package org.compiere.install;

import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.process.*;


/**
 *	Language Translation Maintenance Process
 *	
 *  @author Jorg Janke
 *  @version $Id: LanguageMaintenance.java,v 1.7 2005/11/14 02:10:58 jjanke Exp $
 */
public class LanguageMaintenance extends SvrProcess
{
	/**	The Language ID			*/
	private int		p_AD_Language_ID = 0;
	/** Maintenance Mode		*/
	private String	p_MaintenanceMode = null;
	
	public static String	MAINTENANCEMODE_Add = "A";
	public static String	MAINTENANCEMODE_Delete = "D";
	public static String	MAINTENANCEMODE_ReCreate = "R";
	
	/**	The Language			*/
	private MLanguage 	m_language = null;
	
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
			else if (name.equals("MaintenanceMode"))
				p_MaintenanceMode = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_AD_Language_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		m_language = new MLanguage (getCtx(), p_AD_Language_ID, get_TrxName());
		log.info("Mode=" + p_MaintenanceMode + ", ID=" + p_AD_Language_ID
			+ " - " + m_language);
		
		if (m_language.isBaseLanguage())
			throw new Exception ("Base Language has no Translations");
		
		int deleteNo = 0;
		int insertNo = 0;
		
		//	Delete
		if (MAINTENANCEMODE_Delete.equals(p_MaintenanceMode)
			|| MAINTENANCEMODE_ReCreate.equals(p_MaintenanceMode))
		{
			deleteNo = m_language.maintain(false);
		}
		//	Add
		if (MAINTENANCEMODE_Add.equals(p_MaintenanceMode)
			|| MAINTENANCEMODE_ReCreate.equals(p_MaintenanceMode))
		{
			if (m_language.isActive() && m_language.isSystemLanguage())
			{
				insertNo = m_language.maintain(true);
			}
			else
				throw new Exception ("Language not active System Language");
		}
		//	Delete
		if (MAINTENANCEMODE_Delete.equals(p_MaintenanceMode))
		{
			if (m_language.isSystemLanguage())
			{
				m_language.setIsSystemLanguage(false);
				m_language.save();
			}
		}
		
		return "@Deleted@=" + deleteNo + " - @Inserted@=" + insertNo;
	}	//	doIt

	
}	//	LanguageMaintenance
