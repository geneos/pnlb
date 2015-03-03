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
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Accounting Fact Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MFactAcct.java,v 1.9 2005/11/01 16:36:29 jjanke Exp $
 */
public class MFactAcct extends X_Fact_Acct
{
	/**
	 * 	Delete Accounting
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@param trxName transaction
	 *	@return number of rows or -1 for error
	 */
	public static int delete (int AD_Table_ID, int Record_ID, String trxName)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE Fact_Acct WHERE AD_Table_ID=").append(AD_Table_ID)
			.append(" AND Record_ID=").append(Record_ID);
		int no = DB.executeUpdate(sb.toString(), trxName);
		if (no == -1)
			s_log.log(Level.SEVERE, "failed: AD_Table_ID=" + AD_Table_ID + ", Record_ID" + Record_ID);
		else
			s_log.fine("delete - AD_Table_ID=" + AD_Table_ID 
				+ ", Record_ID=" + Record_ID + " - #" + no);
		return no;
	}	//	delete

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MFactAcct.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param Fact_Acct_ID id
	 */
	public MFactAcct (Properties ctx, int Fact_Acct_ID, String trxName)
	{
		super (ctx, Fact_Acct_ID, trxName);
                System.out.println("Creando por X_Fact_Acct org.compiere.model");
	}	//	MFactAcct

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MFactAcct (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MFactAcct

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MFactAcct[");
		sb.append(get_ID()).append("-Acct=").append(getAccount_ID())
			.append(",Dr=").append(getAmtSourceDr()).append("|").append(getAmtAcctDr())
			.append(",Cr=").append(getAmtSourceCr()).append("|").append(getAmtAcctCr())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Derive MAccount from record
	 *	@return Valid Account Combination
	 */
	public MAccount getMAccount()
	{
		MAccount acct = MAccount.get (getCtx(), getAD_Client_ID(), getAD_Org_ID(),
			getC_AcctSchema_ID(), getAccount_ID(), getC_SubAcct_ID(),
			getM_Product_ID(), getC_BPartner_ID(), getAD_OrgTrx_ID(), 
			getC_LocFrom_ID(), getC_LocTo_ID(), getC_SalesRegion_ID(), 
			getC_Project_ID(), getC_Campaign_ID(), getC_Activity_ID(),
			getUser1_ID(), getUser2_ID(), getUserElement1_ID(), getUserElement2_ID());
		if (acct != null && acct.get_ID() == 0)
			acct.save();
		return acct;
	}	//	getMAccount
	
}	//	MFactAcct
