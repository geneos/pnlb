/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.eevolution.process;

import java.math.*;
import java.sql.*;
import java.io.*;
import javax.swing.JFileChooser;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.print.*;
import org.compiere.process.*;
import org.eevolution.tools.*;
import org.compiere.print.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.apps.*;
import org.compiere.minigrid.*;
import org.compiere.grid.ed.*;
import java.util.logging.*;
/**
 *	Invoice Aging Report.
 *	Based on RV_Aging.
 *  @author Jorg Janke
 *  @version $Id: Aging.java,v 1.5 2004/07/05 01:26:34 jjanke Exp $
 */
public class CFDReport extends SvrProcess
{
	private int    p_C_Period_ID  = 0;
	private String p_DocBaseType  = "";
	
	protected void prepare()
	{
		ProcessInfoParameter[] parac = getParameter();
		for (int i = 0; i < parac.length; i++)
		{
			//log.debug("prepare - " + parac[i]);
			String name = parac[i].getParameterName();
			if (parac[i].getParameter() == null)
				;
			else if (name.equals("C_Period_ID"))
				p_C_Period_ID = ((BigDecimal)parac[i].getParameter()).intValue();
			else if (name.equals("DocBaseType"))
				p_DocBaseType = ((String)parac[i].getParameter()).toString();
			else
				log.log(Level.SEVERE,"Parameter Error");
		}
	}	//	prepare

	 /*
	 * 	DoIt
	 *	@return Message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT(SELECT bp.TaxID FROM C_BPartner bp WHERE i.C_BPartner_ID=bp.C_BPartner_ID) AS TaxID, " +  		// 1.RFC 
           "(SELECT s.Prefix FROM C_DocType dt, AD_Sequence s WHERE  i.C_DocTypeTarget_ID=dt.C_DocType_ID " +
           "	AND dt.DocNoSequence_ID=s.AD_Sequence_ID) AS Prefix, " +										  			// 2.Folio
	       "TO_NUMBER( CASE TO_CHAR(SUBSTR(i.DocumentNo,LENGTH(i.DocumentNo))) WHEN CHR(39)" +
	       "	THEN SUBSTR(i.DocumentNo,INSTR(i.DocumentNo,(SELECT s.Prefix FROM C_DocType dt,AD_Sequence s WHERE " +
		   "	i.C_DocTypeTarget_ID=dt.C_DocType_ID AND dt.DocNoSequence_ID=s.AD_Sequence_ID))+1,LENGTH(i.DocumentNo)-2)" +
		   "	ELSE SUBSTR(i.DocumentNo,INSTR(i.DocumentNo,(SELECT s.Prefix FROM C_DocType dt, AD_Sequence s WHERE " +
		   "	i.C_DocTypeTarget_ID=dt.C_DocType_ID AND dt.DocNoSequence_ID=s.AD_Sequence_ID))+1,LENGTH(i.DocumentNo)) " +
		   "	END) AS DocumentNo," + 																						// 3.DocumentNo			   
	       "(SELECT cs.AutorizationNumber FROM C_DocType dt, AD_Sequence s, E_CtrlSequence cs WHERE i.C_DocTypeTarget_ID=" +
	       " 	dt.C_DocType_ID AND dt.DocNoSequence_ID=s.AD_Sequence_ID AND s.AD_Sequence_ID=cs.AD_Sequence_ID AND " +
	       "	TO_NUMBER(SUBSTR(i.DocumentNo,INSTR(i.DocumentNo,(SELECT s.Prefix FROM C_DocType dt, AD_Sequence s WHERE" +
	       " 	i.C_DocTypeTarget_ID=dt.C_DocType_ID AND dt.DocNoSequence_ID=s.AD_Sequence_ID))+1,length (i.DocumentNo)))" +
           " 	> SequenceBegin-1 AND TO_NUMBER(SUBSTR(i.DocumentNo,INSTR(i.DocumentNo,(SELECT s.Prefix FROM C_DocType dt," +
           " 	AD_Sequence s WHERE i.C_DocTypeTarget_ID=dt.C_DocType_ID AND dt.DocNoSequence_ID=s.AD_Sequence_ID))+1,length" +
           "	(i.DocumentNo))) < SequenceEnd+1) AS AutorizationNumber, " +									      		// 4. NoAutorización
           "(SELECT TO_CHAR(cs.DateRequest,'YYYY') FROM C_DocType dt, AD_Sequence s, E_CtrlSequence cs WHERE " +
           "	i.C_DocTypeTarget_ID=dt.C_DocType_ID AND dt.DocNoSequence_ID=s.AD_Sequence_ID AND s.AD_Sequence_ID=cs.AD_Sequence_ID" +
           " 	AND TO_NUMBER(SUBSTR(i.DocumentNo,INSTR(i.DocumentNo,(SELECT s.Prefix FROM C_DocType dt, AD_Sequence s" +
           "	WHERE i.C_DocTypeTarget_ID=dt.C_DocType_ID AND dt.DocNoSequence_ID=s.AD_Sequence_ID))+1,LENGTH(i.DocumentNo))) >" +
           " 	SequenceBegin-1 AND TO_NUMBER(SUBSTR(i.DocumentNo,INSTR(i.DocumentNo,(SELECT s.Prefix FROM C_DocType dt," +
           "	AD_Sequence s WHERE i.C_DocTypeTarget_ID=dt.C_DocType_ID AND dt.DocNoSequence_ID=s.AD_Sequence_ID))+1,length" +
           "	(i.DocumentNo))) < SequenceEnd+1) AS AutorizationDate, " +									      			// 5. Año de Autorización
           " 	TO_CHAR(i.DateInvoiced,'dd/mm/yyyy hh:mm:ss'), i.GrandTotal, (i.GrandTotal-i.TotalLines) As SubTotal, " +	// 6.7.8
	       "(CASE TO_CHAR(SubStr(i.DocumentNo,length(i.DocumentNo))) WHEN CHR(39)" +
	       "	THEN TO_CHAR('0') ELSE TO_CHAR('1') END) AS Type," +													// 9. Normal/Cancelado			   
           " 	i.DocStatus, i.Created, p.StartDate, p.EndDate,i.C_DocTypeTarget_ID,i.DocumentNo");						// 10.11.12.13        
		sql.append(" FROM C_Invoice i"); //, C_DocType dt, AD_Sequence sq, Ad_Attachment a ");
		sql.append(" INNER JOIN C_Period p     ON(p.C_Period_ID=").append(p_C_Period_ID).append(")");
		sql.append(" INNER JOIN C_DocType dt   ON(dt.C_DocType_ID=i.C_DocTypeTarget_ID)");
		sql.append(" INNER JOIN AD_Sequence sq ON(sq.AD_Sequence_ID=dt.DocNoSequence_ID)");
		sql.append(" WHERE i.DateInvoiced BETWEEN p.StartDate AND p.EndDate" + " AND INSTR(i.DocumentNo,TRIM(sq.Prefix))>0");
		sql.append(" AND EXISTS( SELECT * FROM AD_Attachment a WHERE a.Record_ID=sq.AD_Sequence_ID)");
		if (p_DocBaseType != null)
			sql.append(" AND dt.DocBaseType == p_DocBaseType ");
		//
		sql.append(" ORDER BY 2,3,4");  // Folio y Número de Documento, Número Autorización.
		String finalSql = MRole.getDefault(getCtx(), false).addAccessSQL(sql.toString(), "i", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		//
		System.err.println("-------------------->" + finalSql);
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setDialogTitle(Msg.getMsg(Env.getCtx(), "Guardar ReporteCFD SAT"));
		File fileName = new File("ReporteMensualCFD.txt");
		chooser.setSelectedFile(fileName);
		//	Show dialog
		int returnVal = chooser.showSaveDialog(null);
		if (returnVal != JFileChooser.APPROVE_OPTION)	return "";
		File saveFile = chooser.getSelectedFile();
		if (saveFile == null) return "";
		FileOutputStream fileOut = new FileOutputStream( saveFile.toString());
		FileOutputStream fileOtro = new FileOutputStream( saveFile.toString()+"_"); // OGI
		OutputStreamWriter out = new OutputStreamWriter(fileOtro,"UTF8"); //OGI
		// File to Error
	    String filee = System.getProperty("user.dir") + System.getProperty("file.separator") + "logError.txt";
   	    FileOutputStream fileError = new FileOutputStream( filee.toString());;
		// Statement
		PreparedStatement pstmt = null;
		//
		try	{
			pstmt = DB.prepareStatement(finalSql);
			ResultSet rs = pstmt.executeQuery();
			int count = 0, documentNo=0, beforeDocNo=0, i=1, AD_PInstance_ID=1;
			BigDecimal grandTotal, subTotal;
	        String taxID,prefix,autorizaSeq,docStatus,dateInvoiced,eReport,eError="",beforePrefix="",beforeSeq="",status="0";
			while (rs.next()){
				taxID        = rs.getString(1) == null ? "" : rs.getString(1).trim();
				prefix       = rs.getString(2) == null ? "" : rs.getString(2);
				documentNo   = rs.getInt(3);
				autorizaSeq  = rs.getString(5)+rs.getString(4);
				dateInvoiced = rs.getString(6);
				docStatus	 = rs.getString(9);
				grandTotal   = docStatus.equals("0") ? rs.getBigDecimal(7).abs() : rs.getBigDecimal(7);
				subTotal     = docStatus.equals("0") ? rs.getBigDecimal(8).abs() : rs.getBigDecimal(8);
				
				// Crea cadena de impresión y guarda en archivo de reporte
				eReport = "|"+ taxID +"|"+ prefix +"|"+ documentNo +"|"+ autorizaSeq +"|"+ dateInvoiced +"|"+ grandTotal +"|"+ 
									subTotal +"|" + docStatus +"|";
				System.err.println("...............: "+eReport);
				fileOut.write(eReport.getBytes());
				fileOut.write("\r\n".getBytes());
				out.write(eReport); //OGI
				out.write("\r"); //OGI
				
				// Verifica el consecutivo de folios
				if(count > 1 & docStatus.equals("1")){ // A partir del Segundo registro y que no sea un cancelado
					//System.err.println("---> Checando: " + beforePrefix +"=" + prefix +" <> " + beforeDocNo + "<>" +documentNo + " >>>>>" +	(beforePrefix.equals(prefix))  +"&"+ (beforeDocNo == documentNo ));
					if (beforePrefix.equals(prefix) & beforeDocNo != documentNo){ // secuencia incorrecta(1+1) del mismo folio y No. de Autorización
						eError = "Error en la secuencia del comprobante " + prefix + beforeDocNo + " de Fecha " + dateInvoiced + 
								 "  $" + grandTotal + "" + rs.getString(10);
						//System.err.println("---->>>" + eError);
						fileError.write(eError.getBytes());
						fileError.write("\n".getBytes());
					}
				}
				// Verifica que el TAXId no sea Nulo o el folio
				if(taxID.equals("") ||  prefix.equals("")){
					eError = "Error en RFC || Folio del Comproante " + prefix + beforeDocNo + " de Fecha " + dateInvoiced + 
					 "  $" + grandTotal + "" + rs.getString(10);
					fileError.write(eError.getBytes());
					fileError.write("\n".getBytes());
				}
					
				if(docStatus.equals("1")){ // Solo Verifica los Documentos no Cancelados
					beforeSeq    = rs.getString(4); // Anterior No. Secuencia
					beforePrefix = rs.getString(2); // Anterior Prefix
					beforeDocNo  = documentNo+1;	// Anterior DocumentNo					
				}
				count ++;
			}
			fileOut.close();
			fileError.close();
			out.close();
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("doIt - " + finalSql + e); //log.error("doIt - " + finalSql, e);
		}
		
		return "Terminate... " ;
	}	//	doIt
}	//	Terminate CFDReport