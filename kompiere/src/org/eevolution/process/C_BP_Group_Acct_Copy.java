/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Portions created by Carlos Ruiz are Copyright (C) 2005 QSS Ltda.
 * Add e-Evolution by Perez Juarez
 * Contributor(s): Carlos Ruiz (globalqss)
 *****************************************************************************/
package org.eevolution.process;

import java.sql.*;
import java.util.logging.*;

import org.compiere.util.*;
import org.compiere.process.*;

/**
 * Title:	Copy Acct Info
 * Description:
 *		Copy Accounting Info to all Business Partner Account Info elements
 *		(existing entries are overwritten)
 *	
 *  @author Carlos Ruiz (globalqss)
 *  @version $Id: M_Product_Category_Acct_Copy.java,v 1.0 2005/09/19 23:13:00 globalqss Exp $
 */
public class C_BP_Group_Acct_Copy extends SvrProcess
{

	/*
	 * NOTE: The original oracle procedure C_BP_Group_Acct_Copy had a
	 *       BP_Group_ID parameter for Direct Call not implemented in this class
	 */
	
	/** The Business Partner Group		*/
	private int		c_BP_Group_ID = -1;
	/** The Record						*/
	private int		p_Record_ID = 0;
	
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
			else if (name.equals("C_BP_Group_ID"))
				c_BP_Group_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_Record_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		String sql;
		String sqlupd;
		String sqlins;
		int cntu = 0;
		int cnti = 0;
		int totu = 0;
		int toti = 0;

		log.info("Copy and overwrite Accounts to products of this category");

		sql = "SELECT C_RECEIVABLE_ACCT, C_PREPAYMENT_ACCT, C_ACCTSCHEMA_ID, C_BP_GROUP_ID, "
			+ "V_PREPAYMENT_ACCT, V_LIABILITY_ACCT, V_LIABILITY_SERVICES_ACCT "
			+ "FROM C_BP_Group_Acct "
			+ "WHERE C_BP_Group_ID = " + c_BP_Group_ID;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				// Update existing Customers
				sqlupd = "UPDATE C_BP_Customer_Acct "
					+ "SET C_Receivable_Acct=" + rs.getInt("C_RECEIVABLE_ACCT") + ", "
					+ "C_PrePayment_Acct=" + rs.getInt("C_PREPAYMENT_ACCT") + ", "
					+ "Updated=SysDate, "
					+ "UpdatedBy=0 "
					+ "WHERE C_BP_Customer_Acct.C_AcctSchema_ID=" + rs.getInt("C_ACCTSCHEMA_ID") + " " 
					+ "AND EXISTS (SELECT * FROM C_BPartner p "
					+ "WHERE p.C_BPartner_ID=C_BP_Customer_Acct.C_BPartner_ID AND p.C_BP_Group_ID=" + rs.getInt("C_BP_GROUP_ID") + ")";
				cntu = DB.executeUpdate(sqlupd);
				totu += cntu;
				// Insert new Customer
				sqlins = "INSERT INTO C_BP_Customer_Acct "
					+ "(C_BPartner_ID, C_AcctSchema_ID, "
					+ "AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy, "
					+ "C_Receivable_Acct, C_PrePayment_Acct) "
					+ "SELECT p.C_BPartner_ID, " + rs.getInt("C_ACCTSCHEMA_ID") + ", "
					+ "p.AD_Client_ID, p.AD_Org_ID, 'Y', SysDate, 0, SysDate, 0, "
					+ rs.getInt("C_RECEIVABLE_ACCT") + ", " + rs.getInt("C_PREPAYMENT_ACCT") + " "
					+ "FROM C_BPartner p "
					+ "WHERE p.C_BP_Group_ID=" + rs.getInt("C_BP_GROUP_ID") + " "
					+ "AND NOT EXISTS (SELECT * FROM C_BP_Customer_Acct ca " 
					+ "WHERE ca.C_BPartner_ID=p.C_BPartner_ID AND ca.C_AcctSchema_ID=" + rs.getInt("C_ACCTSCHEMA_ID") + ")";
				cnti = DB.executeUpdate(sqlins);
				toti += cnti;
				log.info("Customers = " + cntu + " / " + cnti);

				// Update existing Vendors
				sqlupd = "UPDATE C_BP_Vendor_Acct "
					+ "SET V_Liability_Acct=" + rs.getInt("V_LIABILITY_ACCT") + ", "
					+ "V_Liability_Services_Acct=" + rs.getInt("V_LIABILITY_SERVICES_ACCT") + ", "
					+ "V_PrePayment_Acct=" + rs.getInt("V_PREPAYMENT_ACCT") + ", "
					+ "Updated=SysDate, "
					+ "UpdatedBy=0 "
					+ "WHERE C_BP_Vendor_Acct.C_AcctSchema_ID=" + rs.getInt("C_ACCTSCHEMA_ID") + " " 
					+ "AND EXISTS (SELECT * FROM C_BPartner p "
					+ "WHERE p.C_BPartner_ID=C_BP_Vendor_Acct.C_BPartner_ID AND p.C_BP_Group_ID=" + rs.getInt("C_BP_GROUP_ID") + ")";
				cntu = DB.executeUpdate(sqlupd);
				totu += cntu;
				// Insert new Vendors
				sqlins = "INSERT INTO C_BP_Vendor_Acct "
					+ "(C_BPartner_ID, C_AcctSchema_ID, "
					+ "AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy, "
					+ "V_Liability_Acct, V_Liability_Services_Acct, V_PrePayment_Acct) "
					+ "SELECT p.C_BPartner_ID, " + rs.getInt("C_ACCTSCHEMA_ID") + ", "
					+ "p.AD_Client_ID, p.AD_Org_ID, 'Y', SysDate, 0, SysDate, 0, "
					+ rs.getInt("V_LIABILITY_ACCT") + ", " + rs.getInt("V_LIABILITY_SERVICES_ACCT") 
					+ ", " + rs.getInt("V_PREPAYMENT_ACCT") + " "
					+ "FROM C_BPartner p "
					+ "WHERE p.C_BP_Group_ID=" + rs.getInt("C_BP_GROUP_ID") + " "
					+ "AND NOT EXISTS (SELECT * FROM C_BP_Vendor_Acct va " 
					+ "WHERE va.C_BPartner_ID=p.C_BPartner_ID AND va.C_AcctSchema_ID=" + rs.getInt("C_ACCTSCHEMA_ID") + ")";
				cnti = DB.executeUpdate(sqlins);
				toti += cnti;
				log.info("Vendors = " + cntu + " / " + cnti);

			}
			rs.close();
			pstmt.close();
			pstmt = null;
			log.fine("Committing ...");
			DB.commit(true, get_TrxName());
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "adding missing elements", e);
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

		return "@Created@=" + cnti + ":2, @Updated@=" + cntu + ":2";		
	}	//	doIt
	
}	//	M_Product_Category_Acct_Copy
