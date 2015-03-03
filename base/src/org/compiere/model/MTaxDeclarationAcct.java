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

/**
 * 	Tax Tax Declaration Accounting Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MTaxDeclarationAcct.java,v 1.1 2005/10/19 17:15:27 jjanke Exp $
 */
public class MTaxDeclarationAcct extends X_C_TaxDeclarationAcct
{
	/**
	 * 	Standard Constructor
	 *	@param ctx ctx
	 *	@param C_TaxDeclarationAcct_ID id
	 *	@param trxName trc
	 */
	public MTaxDeclarationAcct (Properties ctx, int C_TaxDeclarationAcct_ID, String trxName)
	{
		super (ctx, C_TaxDeclarationAcct_ID, trxName);
	}	//	MTaxDeclarationAcct

	/**
	 * 	Load Constructor
	 *	@param ctx ctx
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MTaxDeclarationAcct (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MTaxDeclarationAcct

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param fact fact
	 */
	public MTaxDeclarationAcct (MTaxDeclaration parent, MFactAcct fact)
	{
		super (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(fact);
		setC_TaxDeclaration_ID(parent.getC_TaxDeclaration_ID());
		//
		setFact_Acct_ID (fact.getFact_Acct_ID());
		setC_AcctSchema_ID (fact.getC_AcctSchema_ID());
	}	//	MTaxDeclarationAcct

}	//	MTaxDeclarationAcct
