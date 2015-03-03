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
 *	Validate Business Partner
 *	
 *  @author Jorg Janke
 *  @version $Id: BPartnerValidate.java,v 1.7 2005/11/28 03:36:02 jjanke Exp $
 */
public class BPartnerValidate extends SvrProcess
{
	/**	BPartner ID			*/
	int p_C_BPartner_ID = 0;
	/** BPartner Group		*/
	int p_C_BP_Group_ID = 0;	

	/**
	 *	Prepare
	 */
	protected void prepare ()
	{
		p_C_BPartner_ID = getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BP_Group_ID"))
				p_C_BP_Group_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("C_BPartner_ID=" + p_C_BPartner_ID + ", C_BP_Group_ID=" + p_C_BP_Group_ID); 
		if (p_C_BPartner_ID == 0 && p_C_BP_Group_ID == 0)
			throw new CompiereUserError ("No Business Partner/Group selected");
		
		if (p_C_BP_Group_ID == 0)
		{
			MBPartner bp = new MBPartner (getCtx(), p_C_BPartner_ID, get_TrxName());
			if (bp.get_ID() == 0)
				throw new CompiereUserError ("Business Partner not found - C_BPartner_ID=" + p_C_BPartner_ID);
			checkBP (bp);
		}
		else
		{
			String sql = "SELECT * FROM C_BPartner WHERE C_BP_Group_ID=? AND IsActive='Y'";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement (sql, get_TrxName());
				pstmt.setInt (1, p_C_BP_Group_ID);
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					MBPartner bp = new MBPartner (getCtx(), rs, get_TrxName());
					checkBP (bp);
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql, e);
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
		}
		//
		return "OK";
	}	//	doIt

	/**
	 * 	Check BP
	 *	@param bp bp
	 */
	private void checkBP (MBPartner bp)
	{
		addLog(0, null, null, bp.getName() + ":");
		//	See also VMerge.postMerge
		checkPayments(bp);
		checkInvoices(bp);
		//	
		bp.setTotalOpenBalance();
		bp.setActualLifeTimeValue();
		bp.save();
		//
	//	if (bp.getSO_CreditUsed().signum() != 0)
		addLog(0, null, bp.getSO_CreditUsed(), Msg.getElement(getCtx(), "SO_CreditUsed"));
		addLog(0, null, bp.getTotalOpenBalance(), Msg.getElement(getCtx(), "TotalOpenBalance"));
		addLog(0, null, bp.getActualLifeTimeValue(), Msg.getElement(getCtx(), "ActualLifeTimeValue"));
		//
		commit();
	}	//	checkBP
	
	
	/**
	 * 	Check Payments
	 *	@param bp business partner
	 */
	private void checkPayments (MBPartner bp)
	{
		//	See also VMerge.postMerge
		int changed = 0;
		MPayment[] payments = MPayment.getOfBPartner(getCtx(), bp.getC_BPartner_ID(), get_TrxName());
		for (int i = 0; i < payments.length; i++) 
		{
			MPayment payment = payments[i];
			if (payment.testAllocation())
			{
				payment.save();
				changed++;
			}
		}
		if (changed != 0)
			addLog(0, null, new BigDecimal(payments.length), 
				Msg.getElement(getCtx(), "C_Payment_ID") + " - #" + changed);
	}	//	checkPayments

	/**
	 * 	Check Invoices
	 *	@param bp business partner
	 */
	private void checkInvoices (MBPartner bp)
	{
		//	See also VMerge.postMerge
		int changed = 0;
		MInvoice[] invoices = MInvoice.getOfBPartner(getCtx(), bp.getC_BPartner_ID(), get_TrxName());
		for (int i = 0; i < invoices.length; i++) 
		{
			MInvoice invoice = invoices[i];
			if (invoice.testAllocation())
			{
				invoice.save();
				changed++;
			}
		}
		if (changed != 0)
			addLog(0, null, new BigDecimal(invoices.length), 
				Msg.getElement(getCtx(), "C_Invoice_ID") + " - #" + changed);
	}	//	checkInvoices
	
}	//	BPartnerValidate
