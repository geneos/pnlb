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
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.process;

import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Material Transaction Cross Reference
 *	
 *  @author Jorg Janke
 *  @version $Id: TransactionXRef.java,v 1.5 2005/10/26 00:37:42 jjanke Exp $
 */
public class TransactionXRef extends SvrProcess
{
	private int		p_Search_InOut_ID = 0;
	private int 	p_Search_Order_ID = 0;
	private int		p_Search_Invoice_ID = 0;
	
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
			else if (name.equals("Search_InOut_ID"))
				p_Search_InOut_ID = para[i].getParameterAsInt();
			else if (name.equals("Search_Order_ID"))
				p_Search_Order_ID = para[i].getParameterAsInt();
			else if (name.equals("Search_Invoice_ID"))
				p_Search_Invoice_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process it
	 *	@return info
	 *	@throws Exception if error
	 */
	protected String doIt () throws Exception
	{
		log.info("M_InOut_ID=" + p_Search_InOut_ID + ", C_Order_ID=" + p_Search_Order_ID
			+ ", C_Invoice_ID=" + p_Search_Invoice_ID);
		//
		if (p_Search_InOut_ID != 0)
			insertTrx(
				"SELECT NVL(ma.M_AttributeSetInstance_ID,iol.M_AttributeSetInstance_ID) "
				+ "FROM M_InOutLine iol"
				+ " LEFT OUTER JOIN M_InOutLineMA ma ON (iol.M_InOutLine_ID=ma.M_InOutLine_ID) "
				+ "WHERE M_InOut_ID=" + p_Search_InOut_ID
				);
		else if (p_Search_Order_ID != 0)
			insertTrx(
				"SELECT NVL(ma.M_AttributeSetInstance_ID,iol.M_AttributeSetInstance_ID) "
				+ "FROM M_InOutLine iol"
				+ " LEFT OUTER JOIN M_InOutLineMA ma ON (iol.M_InOutLine_ID=ma.M_InOutLine_ID) "
				+ " INNER JOIN M_InOut io ON (iol.M_InOut_ID=io.M_InOut_ID)"
				+ "WHERE io.C_Order_ID=" + p_Search_Order_ID
				);
		else if (p_Search_Invoice_ID != 0)
			insertTrx(
				"SELECT NVL(ma.M_AttributeSetInstance_ID,iol.M_AttributeSetInstance_ID) "
				+ "FROM M_InOutLine iol"
				+ " LEFT OUTER JOIN M_InOutLineMA ma ON (iol.M_InOutLine_ID=ma.M_InOutLine_ID) "
				+ " INNER JOIN C_InvoiceLine il ON (iol.M_InOutLine_ID=il.M_InOutLine_ID) "
				+ "WHERE il.C_Invoice_ID=" + p_Search_Invoice_ID
				);
		else
			throw new CompiereUserError("Select one Parameter");
		//
		return "";
	}	//	doIt
	
	/**
	 * 	Get Trx
	 *	@param sqlSubSelect sql
	 */
	private void insertTrx (String sqlSubSelect)
	{
		String sql = "INSERT INTO T_Transaction "
			+ "(AD_PInstance_ID, M_Transaction_ID,"
			+ " AD_Client_ID, AD_Org_ID, IsActive, Created,CreatedBy, Updated,UpdatedBy,"
			+ " MovementType, M_Locator_ID, M_Product_ID, M_AttributeSetInstance_ID,"
			+ " MovementDate, MovementQty,"
			+ " M_InOutLine_ID, M_InOut_ID,"
			+ " M_MovementLine_ID, M_Movement_ID,"
			+ " M_InventoryLine_ID, M_Inventory_ID, "
			+ " C_ProjectIssue_ID, C_Project_ID, "
			+ " M_ProductionLine_ID, M_Production_ID, "
			+ " Search_Order_ID, Search_Invoice_ID, Search_InOut_ID) "
			//	Data
			+ "SELECT " + getAD_PInstance_ID() + ", M_Transaction_ID,"
			+ " AD_Client_ID, AD_Org_ID, IsActive, Created,CreatedBy, Updated,UpdatedBy,"
			+ " MovementType, M_Locator_ID, M_Product_ID, M_AttributeSetInstance_ID,"
			+ " MovementDate, MovementQty,"
			+ " M_InOutLine_ID, M_InOut_ID, "
			+ " M_MovementLine_ID, M_Movement_ID,"
			+ " M_InventoryLine_ID, M_Inventory_ID, "
			+ " C_ProjectIssue_ID, C_Project_ID, "
			+ " M_ProductionLine_ID, M_Production_ID, "
			//	Parameter
			+ p_Search_Order_ID + ", " + p_Search_Invoice_ID + "," + p_Search_InOut_ID + " "
			//
			+ "FROM M_Transaction_v "
			+ "WHERE M_AttributeSetInstance_ID > 0 AND M_AttributeSetInstance_ID IN (" 
			+ sqlSubSelect
			+ ") ORDER BY M_Transaction_ID";
		//
		int no = DB.executeUpdate(sql, get_TrxName());
		log.fine(sql);
		log.config("#" + no);
		
		//	Multi-Level
		
	}	//	insertTrx
	
}	//	TransactionXRef
