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

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.*;

/**
 *	Request History Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRequestAction.java,v 1.15 2005/07/18 03:47:42 jjanke Exp $
 */
public class MRequestAction extends X_R_RequestAction
{
	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param R_RequestAction_ID id
	 */
	public MRequestAction (Properties ctx, int R_RequestAction_ID, String trxName)
	{
		super (ctx, R_RequestAction_ID, trxName);
	}	//	MRequestAction

	/**
	 * 	Load Construtor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRequestAction(Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MRequestAction

	/**
	 * 	Parent Action Constructor
	 *	@param request parent
	 *	@param newRecord new (copy all)
	 */
	public MRequestAction (MRequest request, boolean newRecord)
	{
		this (request.getCtx(), 0, request.get_TrxName());
		setClientOrg(request);
		setR_Request_ID (request.getR_Request_ID());
	}	//	MRequestAction

	/**
	 * 	Add Null Column
	 *	@param columnName
	 */
	public void addNullColumn (String columnName)
	{
		String nc = getNullColumns();
		if (nc == null)
			setNullColumns(columnName);
		else
			setNullColumns(nc + ";" + columnName);
	}	//	addNullColumn
	

	/**
	 * 	Get Name of creator
	 *	@return name
	 */
	public String getCreatedByName()
	{
		MUser user = MUser.get(getCtx(), getCreatedBy());
		return user.getName();
	}	//	getCreatedByName

	/**
	 * 	Get Changes as HTML string
	 *	@return changes
	 */
	public String getChangesHTML()
	{
		StringBuffer sb = new StringBuffer();
		getChangeHTML(sb, "Priority");
		getChangeHTML(sb, "PriorityUser");
		getChangeHTML(sb, "R_Category_ID");
		getChangeHTML(sb, "R_Group_ID");
		getChangeHTML(sb, "R_RequestType_ID");
		getChangeHTML(sb, "R_Resolution_ID");
		getChangeHTML(sb, "R_Status_ID");
		getChangeHTML(sb, "SalesRep_ID");
		getChangeHTML(sb, "Summary");
		//
	//	getChangeHTML(sb, "AD_Org_ID");		//	always stored
		getChangeHTML(sb, "AD_Role_ID");
		getChangeHTML(sb, "AD_User_ID");
		getChangeHTML(sb, "C_Activity_ID");
		getChangeHTML(sb, "C_BPartner_ID");
		getChangeHTML(sb, "C_Invoice_ID");
		getChangeHTML(sb, "C_Order_ID");
		getChangeHTML(sb, "C_Payment_ID");
		getChangeHTML(sb, "C_Project_ID");
		getChangeHTML(sb, "DateNextAction");
		getChangeHTML(sb, "IsEscalated");
		getChangeHTML(sb, "IsInvoiced");
		getChangeHTML(sb, "IsSelfService");
		getChangeHTML(sb, "M_InOut_ID");
		getChangeHTML(sb, "M_Product_ID");
		getChangeHTML(sb, "M_RMA_ID");
		getChangeHTML(sb, "A_Asset_ID");
		
		if (sb.length() == 0)
			sb.append("./.");
		//	Unicode check
		char[] chars = sb.toString().toCharArray();
		sb = new StringBuffer(chars.length);
		for (int i = 0; i < chars.length; i++)
		{
			char c = chars[i];
			int ii = (int)c;
			if (ii > 255)
				sb.append("&#").append(ii).append(";");
			else
				sb.append(c);
		}
		return sb.toString();
	}	//	getChangesHTML
	
	/**
	 * 	Get individual Change HTML
	 *	@param sb string to append to
	 *	@param columnName column name
	 */
	private void getChangeHTML (StringBuffer sb, String columnName)
	{
		if (get_Value(columnName) != null)
		{
			if (sb.length() > 0)
				sb.append("<br>");
			sb.append(Msg.getElement(getCtx(), columnName))
				.append(": ").append(get_DisplayValue(columnName, true));
		}
		else
		{
			String nc = getNullColumns();
			if (nc != null && nc.indexOf(columnName) != -1)
			{
				if (sb.length() > 0)
					sb.append("<br>");
				sb.append("(")
					.append(Msg.getElement(getCtx(), columnName))
					.append(")");
			}
		}
	}	//	getChangeHTML
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		return true;
	}	//	beforeSave
}	//	MRequestAction
