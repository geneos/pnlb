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

import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Import Confirmations
 *	
 *  @author Jorg Janke
 *  @version $Id: ImportInOutConfirm.java,v 1.7 2005/12/09 05:17:38 jjanke Exp $
 */
public class ImportInOutConfirm extends SvrProcess
{
	/**	Client to be imported to		*/
	private int 			p_AD_Client_ID = 0;
	/**	Delete old Imported			*/
	private boolean			p_DeleteOldImported = false;
	/**	Import						*/
	private int				p_I_InOutLineConfirm_ID = 0;
	
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
				p_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				p_DeleteOldImported = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_I_InOutLineConfirm_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	doIt
	 *	@return info
	 */
	protected String doIt () throws Exception
	{
		log.info("");
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + p_AD_Client_ID;
		
		//	Delete Old Imported
		if (p_DeleteOldImported)
		{
			sql = new StringBuffer ("DELETE I_InOutLineConfirm "
				  + "WHERE I_IsImported='Y'").append (clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			log.fine("Delete Old Impored =" + no);
		}

		//	Set IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_InOutLineConfirm "
			+ "SET IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.info ("Reset=" + no);

		//	Set Client from Name
		sql = new StringBuffer ("UPDATE I_InOutLineConfirm i "
			+ "SET AD_Client_ID=COALESCE (AD_Client_ID,").append (p_AD_Client_ID).append (") "
			+ "WHERE (AD_Client_ID IS NULL OR AD_Client_ID=0)"
			+ " AND I_IsImported<>'Y'");
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Client from Value=" + no);

		//	Error Confirmation Line
		sql = new StringBuffer ("UPDATE I_InOutLineConfirm i "
			+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Confirmation Line, '"
			+ "WHERE (M_InOutLineConfirm_ID IS NULL OR M_InOutLineConfirm_ID=0"
			+ " OR NOT EXISTS (SELECT * FROM M_InOutLineConfirm c WHERE i.M_InOutLineConfirm_ID=c.M_InOutLineConfirm_ID))"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid InOutLineConfirm=" + no);

		//	Error Confirmation No
		sql = new StringBuffer ("UPDATE I_InOutLineConfirm i "
			+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Missing Confirmation No, '"
			+ "WHERE (ConfirmationNo IS NULL OR ConfirmationNo='')"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid ConfirmationNo=" + no);
		
		//	Qty
		sql = new StringBuffer ("UPDATE I_InOutLineConfirm i "
			+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Target<>(Confirmed+Difference+Scrapped), ' "
			+ "WHERE EXISTS (SELECT * FROM M_InOutLineConfirm c "
				+ "WHERE i.M_InOutLineConfirm_ID=c.M_InOutLineConfirm_ID"
				+ " AND c.TargetQty<>(i.ConfirmedQty+i.ScrappedQty+i.DifferenceQty))"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Qty=" + no);
		
		commit();
		
		/*********************************************************************/
		
		PreparedStatement pstmt = null;
		sql = new StringBuffer ("SELECT * FROM I_InOutLineConfirm "
			+ "WHERE I_IsImported='N'").append (clientCheck)
			.append(" ORDER BY I_InOutLineConfirm_ID");
		no = 0;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				X_I_InOutLineConfirm importLine = new X_I_InOutLineConfirm (getCtx(), rs, get_TrxName());
				MInOutLineConfirm confirmLine = new MInOutLineConfirm (getCtx(), 
					importLine.getM_InOutLineConfirm_ID(), get_TrxName());
				if (confirmLine.get_ID() == 0
					|| confirmLine.get_ID() != importLine.getM_InOutLineConfirm_ID())
				{
					importLine.setI_IsImported(false);
					importLine.setI_ErrorMsg("ID Not Found");
					importLine.save();
				}
				else
				{
					confirmLine.setConfirmationNo(importLine.getConfirmationNo());
					confirmLine.setConfirmedQty(importLine.getConfirmedQty());
					confirmLine.setDifferenceQty(importLine.getDifferenceQty());
					confirmLine.setScrappedQty(importLine.getScrappedQty());
					confirmLine.setDescription(importLine.getDescription());
					if (confirmLine.save())
					{
						//	Import
						importLine.setI_IsImported(true);
						importLine.setProcessed(true);
						if (importLine.save())
							no++;
					}
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		
		return "@Updated@ #" + no;
	}	//	doIt

}	//	ImportInOutConfirm
