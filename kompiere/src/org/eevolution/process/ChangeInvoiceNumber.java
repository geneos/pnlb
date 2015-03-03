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

import java.util.logging.*;
import java.math.*;
import java.sql.*;
import java.util.*;


import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
import compiere.model.*;

/**
 */
public class ChangeInvoiceNumber extends SvrProcess
{
    	private String	p_DocumentNo    = null;
    	private String	p_DocumentNoNew = null;
        
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
			else if (name.equals("DocumentNo"))
				p_DocumentNo    = ((String)para[i].getParameter());
       			else if (name.equals("DocumentNoNew"))
				p_DocumentNoNew = ((String)para[i].getParameter());
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
            String sqlInvoice = "UPDATE C_Invoice"
            + " SET DocumentNo = " + "'" + p_DocumentNoNew + "'"
            + " WHERE IsSOTrx='Y' And DocumentNo=" + "'" + p_DocumentNo    + "'";
            int noInvoice = DB.executeUpdate(sqlInvoice);
            log.info("Change Number Invoice " + noInvoice) ;
            return "Cambio hecho: del Folio " + p_DocumentNo.trim() + " al " + p_DocumentNoNew.trim();
	}	//	doIt
}	//	ChangeInvoiceNumber
