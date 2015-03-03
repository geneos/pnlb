/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is             Compiere  ERP & CRM Smart Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/
package org.eevolution.process;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;


import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
import compiere.model.*;
import org.eevolution.model.MMPCProfileBOM;

/**
 *  Copy Order Lines
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromOrder.java,v 1.4 2004/05/07 05:52:14 jjanke Exp $
 */
public class CopyToNutrients extends SvrProcess
{
	/**	The Order				*/
	private int		p_MPC_ProfileBOM_ID = 0;

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
			else if (name.equals("MPC_ProfileBOM_ID"))
				p_MPC_ProfileBOM_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
            int To_MPC_ProfileBOM_ID = getRecord_ID();
            
            StringBuffer borrars = new StringBuffer("Delete From MPC_ProfileBOMLine where MPC_ProfileBOM_ID=" + To_MPC_ProfileBOM_ID);
            DB.executeUpdate(borrars.toString()); 
            
		
		log.info("doIt - From MPC_ProfileBOM_ID=" + p_MPC_ProfileBOM_ID + " to " + To_MPC_ProfileBOM_ID);
		if (p_MPC_ProfileBOM_ID == 0)
			throw new IllegalArgumentException("Target MPC_ProfileBOM_ID == 0");
		if (To_MPC_ProfileBOM_ID == 0)
			throw new IllegalArgumentException("Source C_Order_ID == 0");
		MMPCProfileBOM from = new MMPCProfileBOM (Env.getCtx(), p_MPC_ProfileBOM_ID,null);
		MMPCProfileBOM to = new MMPCProfileBOM (Env.getCtx(), To_MPC_ProfileBOM_ID,null);
		//
		int no = to.copyFormatosFrom (from);
		//
		return "@Copied@=" + no;
	}	//	doIt

}	//	CopyFromOrder
