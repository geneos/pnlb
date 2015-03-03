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
import org.compiere.model.*;
import org.compiere.util.*;


/**
 * 	Copy BP Group default Accounts
 *	
 *  @author Jorg Janke
 *  @version $Id: BPGroupAcctCopy.java,v 1.4 2005/10/26 00:37:42 jjanke Exp $
 */
public class BPGroupAcctCopy extends SvrProcess
{
	/** BP Group					*/
	private int			p_C_BP_Group_ID = 0;
	/**	Acct Schema					*/
	private int			p_C_AcctSchema_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_BP_Group_ID"))
				p_C_BP_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("C_AcctSchema_ID"))
				p_C_AcctSchema_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	
	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("C_AcctSchema_ID=" + p_C_AcctSchema_ID);
		if (p_C_AcctSchema_ID == 0)
			throw new CompiereSystemError("C_AcctSchema_ID=0");
		MAcctSchema as = MAcctSchema.get(getCtx(), p_C_AcctSchema_ID);
		if (as.get_ID() == 0)
			throw new CompiereSystemError("Not Found - C_AcctSchema_ID=" + p_C_AcctSchema_ID);
		//
		String sql = null;
		int updated = 0;
		int created = 0;
		int updatedTotal = 0;
		int createdTotal = 0;

		//	Update existing Customers
		sql = "UPDATE C_BP_Customer_Acct ca "
			+ "SET (C_Receivable_Acct,C_Receivable_Services_Acct,C_PrePayment_Acct)="
			 + " (SELECT C_Receivable_Acct,C_Receivable_Services_Acct,C_PrePayment_Acct "
			 + " FROM C_BP_Group_Acct"
			 + " WHERE C_BP_Group_ID=" + p_C_BP_Group_ID
			 + " AND C_AcctSchema_ID=" + p_C_AcctSchema_ID
			+ "), Updated=SysDate, UpdatedBy=0 "
			+ "WHERE ca.C_AcctSchema_ID=" + p_C_AcctSchema_ID
			+ " AND EXISTS (SELECT * FROM C_BPartner p "
				+ "WHERE p.C_BPartner_ID=ca.C_BPartner_ID"
				+ " AND p.C_BP_Group_ID=" + p_C_BP_Group_ID + ")";
		updated = DB.executeUpdate(sql, get_TrxName());
		addLog(0, null, new BigDecimal(updated), "@Updated@ @C_BPartner_ID@ @IsCustomer@");
		updatedTotal += updated;
		
		//	Insert new Customer
		sql = "INSERT INTO C_BP_Customer_Acct "
			+ "(C_BPartner_ID, C_AcctSchema_ID,"
			+ " AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,"
			+ " C_Receivable_Acct, C_Receivable_Services_Acct, C_PrePayment_Acct) "
			+ "SELECT p.C_BPartner_ID, acct.C_AcctSchema_ID,"
			+ " p.AD_Client_ID, p.AD_Org_ID, 'Y', SysDate, 0, SysDate, 0,"
			+ " acct.C_Receivable_Acct, acct.C_Receivable_Services_Acct, acct.C_PrePayment_Acct "
			+ "FROM C_BPartner p"
			+ " INNER JOIN C_BP_Group_Acct acct ON (acct.C_BP_Group_ID=p.C_BP_Group_ID)"
			+ "WHERE acct.C_AcctSchema_ID=" + p_C_AcctSchema_ID			//	#
			+ " AND p.C_BP_Group_ID=" + p_C_BP_Group_ID
			+ " AND NOT EXISTS (SELECT * FROM C_BP_Customer_Acct ca "
				+ "WHERE ca.C_BPartner_ID=p.C_BPartner_ID"
				+ " AND ca.C_AcctSchema_ID=acct.C_AcctSchema_ID)";
		created = DB.executeUpdate(sql, get_TrxName());
		addLog(0, null, new BigDecimal(created), "@Created@ @C_BPartner_ID@ @IsCustomer@");
		createdTotal += created;

		
		//	Update existing Vendors
		sql = "UPDATE C_BP_Vendor_Acct va "
			+ "SET (V_Liability_Acct,V_Liability_Services_Acct,V_PrePayment_Acct)="
			 + " (SELECT V_Liability_Acct,V_Liability_Services_Acct,V_PrePayment_Acct "
			 + " FROM C_BP_Group_Acct"
			 + " WHERE C_BP_Group_ID=" + p_C_BP_Group_ID
			 + " AND C_AcctSchema_ID=" + p_C_AcctSchema_ID
			+ "), Updated=SysDate, UpdatedBy=0 "
			+ "WHERE va.C_AcctSchema_ID=" + p_C_AcctSchema_ID
			+ " AND EXISTS (SELECT * FROM C_BPartner p "
				+ "WHERE p.C_BPartner_ID=va.C_BPartner_ID"
				+ " AND p.C_BP_Group_ID=" + p_C_BP_Group_ID + ")";
		updated = DB.executeUpdate(sql, get_TrxName());
		addLog(0, null, new BigDecimal(updated), "@Updated@ @C_BPartner_ID@ @IsVendor@");
		updatedTotal += updated;
		
		//	Insert new Vendors
		sql = "INSERT INTO C_BP_Vendor_Acct "
			+ "(C_BPartner_ID, C_AcctSchema_ID,"
			+ " AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,"
			+ " V_Liability_Acct, V_Liability_Services_Acct, V_PrePayment_Acct) "
			+ "SELECT p.C_BPartner_ID, acct.C_AcctSchema_ID,"
			+ " p.AD_Client_ID, p.AD_Org_ID, 'Y', SysDate, 0, SysDate, 0,"
			+ " acct.V_Liability_Acct, acct.V_Liability_Services_Acct, acct.V_PrePayment_Acct "
			+ "FROM C_BPartner p"
			+ " INNER JOIN C_BP_Group_Acct acct ON (acct.C_BP_Group_ID=p.C_BP_Group_ID)"
			+ "WHERE acct.C_AcctSchema_ID=" + p_C_AcctSchema_ID			//	#
			+ " AND p.C_BP_Group_ID=" + p_C_BP_Group_ID
			+ " AND NOT EXISTS (SELECT * FROM C_BP_Vendor_Acct va "
				+ "WHERE va.C_BPartner_ID=p.C_BPartner_ID AND va.C_AcctSchema_ID=acct.C_AcctSchema_ID)";
		created = DB.executeUpdate(sql, get_TrxName());
		addLog(0, null, new BigDecimal(created), "@Created@ @C_BPartner_ID@ @IsVendor@");
		createdTotal += created;

		return "@Created@=" + createdTotal + ", @Updated@=" + updatedTotal;
	}	//	doIt

}	//	BPGroupAcctCopy
