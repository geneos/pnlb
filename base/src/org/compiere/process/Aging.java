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
import java.sql.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Invoice Aging Report.
 *	Based on RV_Aging.
 *  @author Jorg Janke
 *  @version $Id: Aging.java,v 1.12 2005/03/11 20:25:57 jjanke Exp $
 */
public class Aging extends SvrProcess
{
	private Timestamp	p_DueDate = null;
	private boolean 	p_IsSOTrx = false;
	private int			p_C_Currency_ID = 0;
	private int			p_C_BP_Group_ID = 0;
	private int			p_C_BPartner_ID = 0;
	private boolean		p_IsListInvoices = false;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			log.fine("prepare - " + para[i]);
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DueDate"))
				p_DueDate = (Timestamp)para[i].getParameter();
			else if (name.equals("IsSOTrx"))
				p_IsSOTrx = "Y".equals(para[i].getParameter());
			else if (name.equals("C_Currency_ID"))
				p_C_Currency_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("C_BP_Group_ID"))
				p_C_BP_Group_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("IsListInvoices"))
				p_IsListInvoices = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		if (p_DueDate == null)
			p_DueDate = new Timestamp (System.currentTimeMillis());
	}	//	prepare

	/**
	 * 	DoIt
	 *	@return Message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		log.info("DueDate=" + p_DueDate + ", IsSOTrx=" + p_IsSOTrx
			+ ", C_Currency_ID=" + p_C_Currency_ID
			+ ", C_BP_Group_ID=" + p_C_BP_Group_ID + ", C_BPartner_ID=" + p_C_BPartner_ID
			+ ", IsListInvoices=" + p_IsListInvoices);
		//
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT bp.C_BP_Group_ID, oi.C_BPartner_ID,oi.C_Invoice_ID,oi.C_InvoicePaySchedule_ID, " 
			+ "oi.C_Currency_ID, oi.IsSOTrx, "								//	5..6
			+ "oi.DateInvoiced, oi.NetDays,oi.DueDate,oi.DaysDue, ");		//	7..10
		if (p_C_Currency_ID == 0)
			sql.append("oi.GrandTotal, oi.PaidAmt, oi.OpenAmt ");			//	11..13
		else
		{
			String s = ",oi.C_Currency_ID," + p_C_Currency_ID + ",oi.DateInvoiced,oi.C_ConversionType_ID,oi.AD_Client_ID,oi.AD_Org_ID)";
			sql.append("currencyConvert(oi.GrandTotal").append(s)		//	11..
				.append(", currencyConvert(oi.PaidAmt").append(s)
				.append(", currencyConvert(oi.OpenAmt").append(s);
		}
		sql.append(" FROM RV_OpenItem oi"
			+ " INNER JOIN C_BPartner bp ON (oi.C_BPartner_ID=bp.C_BPartner_ID) "
			+ "WHERE oi.ISSoTrx=").append(p_IsSOTrx ? "'Y'" : "'N'");
		if (p_C_BPartner_ID > 0)
			sql.append(" AND oi.C_BPartner_ID=").append(p_C_BPartner_ID);
		else if (p_C_BP_Group_ID > 0)
			sql.append(" AND bp.C_BP_Group_ID=").append(p_C_BP_Group_ID);
		sql.append(" ORDER BY oi.C_BPartner_ID, oi.C_Currency_ID, oi.C_Invoice_ID");
		
		log.finest(sql.toString());
		String finalSql = MRole.getDefault(getCtx(), false).addAccessSQL(
			sql.toString(), "oi", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);	
		log.finer(finalSql);

		PreparedStatement pstmt = null;
		//
		MAging aging = null;
		int counter = 0;
		int rows = 0;
		int AD_PInstance_ID = getAD_PInstance_ID();
		//
		try
		{
			pstmt = DB.prepareStatement(finalSql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int C_BP_Group_ID = rs.getInt(1);
				int C_BPartner_ID = rs.getInt(2);
				int C_Invoice_ID = p_IsListInvoices ? rs.getInt(3) : 0;
				int C_InvoicePaySchedule_ID = p_IsListInvoices ? rs.getInt(4) : 0;
				int C_Currency_ID = rs.getInt(5);
				boolean IsSOTrx = "Y".equals(rs.getString(6));
				//
				Timestamp DateInvoiced = rs.getTimestamp(7);
				int NetDays = rs.getInt(8);
				Timestamp DueDate = rs.getTimestamp(9);
				int DaysDue = rs.getInt(10);
				//
				BigDecimal GrandTotal = rs.getBigDecimal(11);
				BigDecimal PaidAmt = rs.getBigDecimal(12);
				BigDecimal OpenAmt = rs.getBigDecimal(13);
				//
				rows++;
				//	New Aging Row
				if (aging == null 		//	Key
					|| AD_PInstance_ID != aging.getAD_PInstance_ID()
					|| C_BPartner_ID != aging.getC_BPartner_ID()
					|| C_Currency_ID != aging.getC_Currency_ID()
					|| C_Invoice_ID != aging.getC_Invoice_ID()
					|| C_InvoicePaySchedule_ID != aging.getC_InvoicePaySchedule_ID()
				)
				{
					if (aging != null)
					{
						if (aging.save())
							log.fine("doIt #" + ++counter + " - " + aging);
						else
						{
							log.log(Level.SEVERE, "Not saved " + aging);
							break;
						}
					}
					aging = new MAging (getCtx(), AD_PInstance_ID, C_BPartner_ID,
						C_Currency_ID, C_Invoice_ID, C_InvoicePaySchedule_ID, 
						C_BP_Group_ID, p_DueDate, IsSOTrx, get_TrxName());
				}
				//	Fill Buckets
				aging.add (DaysDue, GrandTotal, OpenAmt);
			}
			if (aging != null)
			{
				if (aging.save())
					log.fine("#" + ++counter + " - " + aging);
				else
					log.log(Level.SEVERE, "Not saved " + aging);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, finalSql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}	
		//	
		log.info("#" + counter + " - rows=" + rows);
		return "";
	}	//	doIt

}	//	Aging

