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

import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Delete Data in Import Table
 *	
 *  @author Jorg Janke
 *  @version $Id: ImportDelete.java,v 1.10 2005/10/26 00:37:42 jjanke Exp $
 */
public class ImportDelete extends SvrProcess
{
	/**	Table be deleted		*/
	private int				p_AD_Table_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_Table_ID"))
				p_AD_Table_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare


	/**
	 *  Perrform process.
	 *  @return clear Message
	 *  @throws Exception
	 */
	protected String doIt() throws Exception
	{
		log.info("AD_Table_ID=" + p_AD_Table_ID);
		//	get Table Info
		M_Table table = new M_Table (getCtx(), p_AD_Table_ID, get_TrxName());
		if (table.get_ID() == 0)
			throw new IllegalArgumentException ("No AD_Table_ID=" + p_AD_Table_ID);
		String tableName = table.getTableName();
		if (!tableName.startsWith("I"))
			throw new IllegalArgumentException ("Not an import table = " + tableName);
		
		//	Delete
		String sql = "DELETE FROM " + tableName + " WHERE AD_Client_ID=" + getAD_Client_ID();
		int no = DB.executeUpdate(sql, get_TrxName());
		String msg = Msg.translate(getCtx(), tableName + "_ID") + " #" + no;
		return msg;
	}	//	ImportDelete

}	//	ImportDelete
