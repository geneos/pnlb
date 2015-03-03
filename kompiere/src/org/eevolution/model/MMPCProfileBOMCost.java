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
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/
//package org.compiere.mfg.model;
package org.eevolution.model;

import java.sql.*;
import java.util.*;

import org.compiere.util.*;
import org.compiere.model.*;
/**
 * 	Project Line Model
 *
 *	@author Jorg Janke
 *	@version $Id: MProjectLine.java,v 1.5 2003/11/20 02:31:24 jjanke Exp $
 */
public class MMPCProfileBOMCost extends X_MPC_ProfileBOMCost
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_ProjectLine_ID id
	 */
	public MMPCProfileBOMCost(Properties ctx, int MPC_ProfileBOMCost_ID,String trxName)
	{
		super (ctx, MPC_ProfileBOMCost_ID,trxName);
		if (MPC_ProfileBOMCost_ID == 0)
		{
		//  setC_Project_ID (0);
		//	setC_ProjectLine_ID (0);
//			setLine (0);
//			setIsPrinted(true);
//			setProcessed(false);
//			setInvoicedAmt (Env.ZERO);
//			setInvoicedQty (Env.ZERO);
//			setPlannedAmt (Env.ZERO);
//			setPlannedMarginAmt (Env.ZERO);
//			setPlannedPrice (Env.ZERO);
//			setPlannedQty (Env.ZERO);
                //    setM_Attribute_ID();
		}
	}	//	MProjectLine

	/**
	 * 	Load Constructor
	 * 	@param ctx context
	 * 	@param rs result set
	 */
	public MMPCProfileBOMCost(Properties ctx, ResultSet rs ,String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MProjectLine

	/**
	 * 	Parent Constructor
	 *	@param project parent
	 */
	public MMPCProfileBOMCost(MMPCProfileBOM profile)
	{
		this (profile.getCtx(), 0,null);
                setAD_Client_ID(profile.getAD_Client_ID());
		setClientOrg(profile.getAD_Client_ID(), profile.getAD_Org_ID());
	//	setC_ProjectLine_ID (0);						// PK
                
		setMPC_ProfileBOM_ID (profile.getMPC_ProfileBOM_ID());	// Parent
	//	setLine (getNextLine());
	}	//	MProjectLine

	/**
	 *	Get the next Line No
	 * 	@return next line no
	 */
//	private int getNextLine()
//	{
//		return DB.getSQLValue("SELECT COALESCE(MAX(Line),0)+10 FROM MPC_ProfileBOMLine WHERE MPC_ProfileBOM_ID=?", getMPC_ProfileBOM_ID());
//	}	//	getLineFromProject

//	/**
//	 * 	Set Product, committed qty, etc.
//	 *	@param pi project issue
//	 */
//	public void setMProjectIssue (MProjectIssue pi)
//	{
//		setC_ProjectIssue_ID(pi.getC_ProjectIssue_ID());
//		setM_Product_ID(pi.getM_Product_ID());
//		setCommittedQty(pi.getMovementQty());
//		if (getDescription() != null)
//			setDescription(pi.getDescription());
//	}	//	setMProjectIssue
//
//	/**
//	 *	Set PO
//	 *	@param C_OrderPO_ID po id
//	 */
//	public void setC_OrderPO_ID (int C_OrderPO_ID)
//	{
//		super.setC_OrderPO_ID(C_OrderPO_ID);
//	}	//	setC_OrderPO_ID

}	//	MProjectLine
