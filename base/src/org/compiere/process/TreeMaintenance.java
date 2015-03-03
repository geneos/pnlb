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
 *	Tree Maintenance	
 *	
 *  @author Jorg Janke
 *  @version $Id: TreeMaintenance.java,v 1.10 2005/10/26 00:37:42 jjanke Exp $
 */
public class TreeMaintenance extends SvrProcess
{
	/**	Tree				*/
	private int		m_AD_Tree_ID;
	
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
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		m_AD_Tree_ID = getRecord_ID();		//	from Window
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		log.info("doIt - AD_Tree_ID=" + m_AD_Tree_ID);
		if (m_AD_Tree_ID == 0)
			throw new IllegalArgumentException("Tree_ID = 0");
		MTree tree = new MTree (getCtx(), m_AD_Tree_ID, get_TrxName());	
		if (tree == null || tree.getAD_Tree_ID() == 0)
			throw new IllegalArgumentException("No Tree -" + tree);
		//
		if (MTree.TREETYPE_BoM.equals(tree.getTreeType()))
			return "BOM Trees not implemented";
		return verifyTree(tree);
	}	//	doIt

	/**
	 *  Verify Tree
	 * 	@param tree tree
	 */
	private String verifyTree (MTree_Base tree)
	{
		String nodeTableName = tree.getNodeTableName();
		String sourceTableName = tree.getSourceTableName(true);
		String sourceTableKey = sourceTableName + "_ID";
		int AD_Client_ID = tree.getAD_Client_ID();
		int C_Element_ID = 0;
		if (MTree.TREETYPE_ElementValue.equals(tree.getTreeType()))
		{
			String sql = "SELECT C_Element_ID FROM C_Element "
				+ "WHERE AD_Tree_ID=" + tree.getAD_Tree_ID();
			C_Element_ID = DB.getSQLValue(null, sql);
			if (C_Element_ID <= 0)
				throw new IllegalStateException("No Account Element found");
		}
		
		//	Delete unused
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE ").append(nodeTableName)
			.append(" WHERE AD_Tree_ID=").append(tree.getAD_Tree_ID())
			.append(" AND Node_ID NOT IN (SELECT ").append(sourceTableKey)
			.append(" FROM ").append(sourceTableName)
			.append(" WHERE AD_Client_ID=").append(AD_Client_ID);
		if (C_Element_ID > 0)
			sql.append(" AND C_Element_ID=").append(C_Element_ID);
		sql.append(")");
		log.finer(sql.toString());
		//
		int deletes = DB.executeUpdate(sql.toString(), get_TrxName());
		addLog(0,null, new BigDecimal(deletes), tree.getName()+ " Deleted");
		if (!tree.isAllNodes())
			return tree.getName() + " OK";
		
		//	Insert new
		int inserts = 0;
		sql = new StringBuffer();
		sql.append("SELECT ").append(sourceTableKey)
			.append(" FROM ").append(sourceTableName)
			.append(" WHERE AD_Client_ID=").append(AD_Client_ID);
		if (C_Element_ID > 0)
			sql.append(" AND C_Element_ID=").append(C_Element_ID);
		sql.append(" AND ").append(sourceTableKey)
			.append("  NOT IN (SELECT Node_ID FROM ").append(nodeTableName)
			.append(" WHERE AD_Tree_ID=").append(tree.getAD_Tree_ID()).append(")");
		log.finer(sql.toString());
		//
		boolean ok = true;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int Node_ID = rs.getInt(1);
				PO node = null;
				if (nodeTableName.equals("AD_TreeNode"))
					node = new MTree_Node(tree, Node_ID);
				else if (nodeTableName.equals("AD_TreeNodeBP"))
					node = new MTree_NodeBP(tree, Node_ID);
				else if (nodeTableName.equals("AD_TreeNodePR"))
					node = new MTree_NodePR(tree, Node_ID);
//				else if (nodeTableName.equals("AD_TreeNodeMM"))
//					node = new MTree_NodeMM(tree, Node_ID);
				//				
				if (node == null)
					log.log(Level.SEVERE, "verifyTree - no Model for " + nodeTableName);
				else
				{
					if (node.save())
						inserts++;
					else
						log.log(Level.SEVERE, "verifyTree - Could not add to " + tree + " Node_ID=" + Node_ID);
				}
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "verifyTree", e);
			ok = false;
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
		addLog(0,null, new BigDecimal(inserts), tree.getName()+ " Inserted");
		return tree.getName() + (ok ? " OK" : " Error");
	}	//	verifyTree

}	//	TreeMaintenence
