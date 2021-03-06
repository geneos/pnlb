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
package org.compiere.report;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Report Tree Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MReportTree.java,v 1.9 2005/11/01 16:36:41 jjanke Exp $
 */
public class MReportTree
{
	/**
	 * 	Get Report Tree (cached)
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID optional hierarchy
	 *	@param ElementType Account Schema Element Type
	 *	@return tree
	 */
	public static MReportTree get (Properties ctx, int PA_Hierarchy_ID, String ElementType)
	{
		String key = PA_Hierarchy_ID + ElementType;
		MReportTree tree = (MReportTree)s_trees.get(key);
		if (tree == null)
		{
			tree = new MReportTree (ctx, PA_Hierarchy_ID, ElementType);
			s_trees.put(key, tree);
		}
		return tree;	
	}	//	get

	/**
	 * 	Get Where Clause
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID optional hierarchy
	 *	@param ElementType Account Schema Element Type
	 *	@param ID leaf element id
	 *	@return where clause
	 */
	public static String getWhereClause (Properties ctx,
		int PA_Hierarchy_ID, String ElementType, int ID)
	{
		MReportTree tree = get (ctx, PA_Hierarchy_ID, ElementType);
		return tree.getWhereClause(ID);	
	}	//	getWhereClause

	/**
	 * 	Get Child IDs
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID optional hierarchie
	 *	@param ElementType Account Schema Element Type
	 *	@return array of IDs
	 */
	public static Integer[] getChildIDs (Properties ctx,
		int PA_Hierarchy_ID, String ElementType, int ID)
	{
		MReportTree tree = get (ctx, PA_Hierarchy_ID, ElementType);
		return tree.getChildIDs(ID);	
	}	//	getChildIDs

	/**	Map with Tree				*/
	private static CCache<String,MReportTree> s_trees = new CCache<String,MReportTree>("MReportTree", 20);


	/**************************************************************************
	 * 	Report Tree
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID optional hierarchy
	 *	@param ElementType Account Schema Element Type
	 */
	public MReportTree (Properties ctx, int PA_Hierarchy_ID, String ElementType)
	{
		m_ElementType = ElementType;
		m_TreeType = m_ElementType;
		if (MAcctSchemaElement.ELEMENTTYPE_Account.equals(m_ElementType)
			|| MAcctSchemaElement.ELEMENTTYPE_UserList1.equals(m_ElementType)
			|| MAcctSchemaElement.ELEMENTTYPE_UserList2.equals(m_ElementType) )
			m_TreeType = MTree.TREETYPE_ElementValue;
		m_PA_Hierarchy_ID = PA_Hierarchy_ID;
		m_ctx = ctx;
		//
		int AD_Tree_ID = getAD_Tree_ID();
		//	Not found
		if (AD_Tree_ID == 0)
			throw new IllegalArgumentException("No AD_Tree_ID for TreeType=" + m_TreeType 
				+ ", PA_Hierarchy_ID=" + PA_Hierarchy_ID);
		//
		boolean clientTree = true;
		m_tree = new MTree (ctx, AD_Tree_ID, false, clientTree, null);
	}	//	MReportTree

	/** Optional Hierarchy		*/
	private int			m_PA_Hierarchy_ID = 0;
	/**	Element Type			*/
	private String		m_ElementType = null;
	/** Context					*/
	private Properties	m_ctx = null;
	/** Tree Type				*/
	private String		m_TreeType = null;
	/**	The Tree				*/
	private MTree 		m_tree = null;
	/**	Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());

	
	/**
	 * 	Get AD_Tree_ID 
	 *	@return tree
	 */
	protected int getAD_Tree_ID ()
	{
		if (m_PA_Hierarchy_ID == 0)
			return getDefaultAD_Tree_ID();

		MHierarchy hierarchy = MHierarchy.get(m_ctx, m_PA_Hierarchy_ID);
		int AD_Tree_ID = hierarchy.getAD_Tree_ID (m_TreeType);

		if (AD_Tree_ID == 0)
			return getDefaultAD_Tree_ID();
		
		return AD_Tree_ID;
	}	//	getAD_Tree_ID
	
	/**
	 * 	Get Default AD_Tree_ID 
	 * 	see MTree.getDefaultAD_Tree_ID
	 *	@return tree
	 */
	protected int getDefaultAD_Tree_ID()
	{
		int AD_Tree_ID = 0;
		int AD_Client_ID = Env.getAD_Client_ID(m_ctx);
		
		String sql = "SELECT AD_Tree_ID, Name FROM AD_Tree "
			+ "WHERE AD_Client_ID=? AND TreeType=? AND IsActive='Y' AND IsAllNodes='Y' "
			+ "ORDER BY IsDefault DESC, AD_Tree_ID";	//	assumes first is primary tree
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setString(2, m_TreeType);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				AD_Tree_ID = rs.getInt(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		return AD_Tree_ID;
	}	//	getDefaultAD_Tree_ID

	/**
	 * 	Get Account Schema Element Type
	 *	@return element Type
	 */
	public String getElementType()
	{
		return m_ElementType;
	}	//	getElementType
	
	/**
	 * 	Get Tree Type
	 *	@return tree type
	 */
	public String getTreeType()
	{
		return m_TreeType;
	}	//	getTreeType
	
	/**
	 * 	Get Tree
	 *	@return tree
	 */
	public MTree getTree()
	{
		return m_tree;
	}	//	getTreeType

	/**
	 * 	Get Where Clause
	 *	@param ID start node
	 *	@return ColumnName = 1 or ColumnName IN (1,2,3)
	 */	
	public String getWhereClause (int ID)
	{
		log.fine("(" + m_ElementType + ") ID=" + ID);
		String ColumnName = MAcctSchemaElement.getColumnName(m_ElementType);
		//
		MTreeNode node = m_tree.getRoot().findNode(ID);
		log.finest("Root=" + node);
		//
		StringBuffer result = null;
		if (node != null && node.isSummary())
		{
			StringBuffer sb = new StringBuffer();
			Enumeration en = node.preorderEnumeration();
			while (en.hasMoreElements())
			{
				MTreeNode nn = (MTreeNode)en.nextElement();
				if (!nn.isSummary())
				{
					if (sb.length () > 0)
						sb.append (",");
					sb.append(nn.getNode_ID());
					log.finest("- " + nn);
				}
				else
					log.finest("- skipped parent (" + nn + ")");
			}
			result = new StringBuffer (ColumnName).append(" IN (").append(sb).append(")");
		}
		else	//	not found or not summary 
			result = new StringBuffer (ColumnName).append("=").append(ID);
		//
		log.finest(result.toString());
		return result.toString();
	}	//	getWhereClause

	/**
	 * 	Get Child IDs
	 *	@param ID start node
	 *	@return array if IDs
	 */	
	public Integer[] getChildIDs (int ID)
	{
		log.fine("(" + m_ElementType + ") ID=" + ID);
		ArrayList<Integer> list = new ArrayList<Integer>(); 
		//
		MTreeNode node = m_tree.getRoot().findNode(ID);
		log.finest("Root=" + node);
		//
		if (node != null && node.isSummary())
		{
			Enumeration en = node.preorderEnumeration();
			while (en.hasMoreElements())
			{
				MTreeNode nn = (MTreeNode)en.nextElement();
				if (!nn.isSummary())
				{
					list.add(new Integer(nn.getNode_ID()));
					log.finest("- " + nn);
				}
				else
					log.finest("- skipped parent (" + nn + ")");
			}
		}
		else	//	not found or not summary 
			list.add(new Integer(ID));
		//
		Integer[] retValue = new Integer[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getWhereClause

	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MReportTree[ElementType=");
		sb.append(m_ElementType).append(",TreeType=").append(m_TreeType)
			.append(",").append(m_tree)
			.append("]");
		return sb.toString();
	}	//	toString

}	//	MReportTree
