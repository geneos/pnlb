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
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.eevolution.process;

import java.util.logging.*;
import java.math.*;
import java.sql.*;
import java.util.*;


import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
import org.eevolution.model.*;

/**
 *  Copy Order Lines
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromOrder.java,v 1.4 2004/05/07 05:52:14 jjanke Exp $
 */
public class generateXML extends SvrProcess
{
	/**	The Order				*/
	
private int cont=0;
private int	p_C_Invoice_ID = 0;

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
			else if (name.equals("C_Invoice_ID"))
				p_C_Invoice_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				System.out.println("prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT i.C_Invoice_ID From C_Invoice i, C_DocType dt, AD_Sequence sq, Ad_Attachment a" +
                   " WHERE i.DocStatus='CO' AND i.IsSOTrx ='Y' AND Not Exists(Select * From AD_Attachment att WHERE i.C_Invoice_ID=att.Record_ID)");
        if (p_C_Invoice_ID > 0)
		   	sql.append(" AND i.C_Invoice_ID =" +p_C_Invoice_ID);
        sql.append("   AND i.DocStatus != 'CL' AND i.DocAction != '--'" +
                   "   AND dt.C_DocType_ID=i.C_DocType_ID" +
                   "   AND sq.AD_Sequence_ID=dt.DocNoSequence_ID" +
                   "   AND a.Record_ID=sq.AD_Sequence_ID AND a.AD_Table_ID=115" +
				   "   AND INSTR(i.DocumentNo,TRIM(sq.Prefix))>0");
		String finalSql = MRole.getDefault(Env.getCtx(), false).addAccessSQL(sql.toString(), "i", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
        //System.out.println("==================================================== SQL: " + finalSql);
        //
        PreparedStatement pstmt = null;
        int counter = 0;
        //
        try
		{
			pstmt = DB.prepareStatement(finalSql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int C_Invoice_ID = rs.getInt(1);
	            org.eevolution.model.MInvoice eXML = new  org.eevolution.model.MInvoice(Env.getCtx(), C_Invoice_ID, null);
    			System.out.println("================ Estas Facturas cumplen con la condición, Documento " + eXML.getDocumentNo());
	            eXML.setDocAction("CO");
	          	eXML.createCFD();
	            eXML.setDocAction("CL");
	          	cont++;
			}
		} catch (Exception e){
				System.out.println("ERROR: " + e);
		}
		return "Genero XML para: " +cont+ " factura(s)";
	}	//	doIt
}	//	generateXML
