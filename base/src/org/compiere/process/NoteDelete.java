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
import org.compiere.util.*;

/**
 *	Delere Notes (Notice)
 *	
 *  @author Jorg Janke
 *  @version $Id: NoteDelete.java,v 1.4 2005/03/11 20:25:57 jjanke Exp $
 */
public class NoteDelete extends SvrProcess
{
	private int		p_AD_User_ID = -1;
	
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
			else if (name.equals("AD_User_ID"))
				p_AD_User_ID = ((BigDecimal)para[i].getParameter()).intValue();
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
		log.info("doIt - AD_User_ID=" + p_AD_User_ID);
		
		String sql = "DELETE FROM AD_Note WHERE AD_Client_ID=" + getAD_Client_ID();
		
		/*
		 * 16-05-2011 Camarzana Mariano
		 * Modificado para que solo borre los avisos del usuario que esta logueado
		 */
		/*if (p_AD_User_ID > 0)
			sql += " AND AD_User_ID=" + p_AD_User_ID;*/
		//
		
		sql += " AND AD_User_ID= " + Env.getContext(Env.getCtx(),"#AD_User_ID");
		
		int no = DB.executeUpdate(sql, get_TrxName());
		return "@Deleted@ = " + no;
	}	//	doIt

}	//	NoteDelete
