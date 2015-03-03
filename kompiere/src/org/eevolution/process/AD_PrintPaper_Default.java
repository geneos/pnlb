/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Portions created by Carlos Ruiz are Copyright (C) 2005 QSS Ltda.
 * Add e-Evolution by Perez Juarez
 * Contributor(s): Carlos Ruiz (globalqss)
 *****************************************************************************/
package org.eevolution.process;

import java.util.logging.*;

import org.compiere.util.*;
import org.compiere.process.*;

/**
 *	Set Print Format
 *	
 *  @author Carlos Ruiz (globalqss)
 *  @version $Id: AD_PrintPaper_Default.java,v 1.0 2005/09/14 22:29:00 globalqss Exp $
 */
public class AD_PrintPaper_Default extends SvrProcess
{

	/** The Client						*/
	private int		p_AD_Client_ID = -1;
	/** The Record						*/
	private int		p_Record_ID = 0;
	
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
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_Record_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		StringBuffer sql = new StringBuffer("");
		int cnt = 0;

		log.info("Set Print Format");

		try
		{
			sql.append("UPDATE AD_PrintFormat pf "
	                + "SET AD_PrintPaper_ID = " + p_Record_ID + " "
	                + "WHERE EXISTS (SELECT * FROM AD_PrintPaper pp "
	                + "WHERE pf.AD_PrintPaper_ID=pp.AD_PrintPaper_ID "
	                + "AND IsLandscape = (SELECT IsLandscape FROM AD_PrintPaper " 
	                + "WHERE AD_PrintPaper_ID=" + p_Record_ID + "))");
			if (p_AD_Client_ID != -1) {
				sql.append(" AND AD_Client_ID = " + p_AD_Client_ID);
			}
			cnt = DB.executeUpdate(sql.toString());
			log.info("Updated " + cnt + " columns");
			log.fine("Committing ...");
			DB.commit(true, null);	//	no Trx
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "set print format", e);
		}

		return "@Copied@=" + cnt;		
	}	//	doIt
	
}	//	AD_PrintPaper_Default
