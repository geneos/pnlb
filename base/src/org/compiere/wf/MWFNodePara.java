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
package org.compiere.wf;

import java.sql.*;
import java.util.*;

import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Workflow Node Process Parameter Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MWFNodePara.java,v 1.7 2005/11/06 01:17:27 jjanke Exp $
 */
public class MWFNodePara extends X_AD_WF_Node_Para
{
	/**
	 * 	Get Parameters for a node
	 *	@param ctx context
	 *	@param AD_WF_Node_ID node
	 *	@return array of parameters
	 */
	public static MWFNodePara[] getParameters (Properties ctx, int AD_WF_Node_ID)
	{
		ArrayList<MWFNodePara> list = new ArrayList<MWFNodePara>();
		String sql = "SELECT * FROM AD_WF_Node_Para "
			+ "WHERE AD_WF_Node_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_WF_Node_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MWFNodePara (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getParameters", e);
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
		MWFNodePara[] retValue = new MWFNodePara[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getParameters
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MWFNodePara.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param id id
	 */
	public MWFNodePara (Properties ctx, int id, String trxName)
	{
		super (ctx, id, trxName);
	}	//	MWFNodePara

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MWFNodePara (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MWFNodePara
	
	
	/** Linked Process Parameter			*/
	private MProcessPara 	m_processPara = null;
	
	/**
	 * 	Get Process Parameter
	 *	@return process parameter
	 */
	public MProcessPara getProcessPara()
	{
		if (m_processPara == null)
			m_processPara = new MProcessPara (getCtx(), getAD_Process_Para_ID(), get_TrxName());
		return m_processPara;
	}	//	getProcessPara
	
	/**
	 * 	Get Attribute Name.
	 * 	If not set - retrieve it
	 *	@return attribute name
	 */
	public String getAttributeName ()
	{
		String an = super.getAttributeName ();
		if (an == null || an.length() == 0 && getAD_Process_Para_ID() != 0)
		{
			an = getProcessPara().getColumnName();
			setAttributeName(an);
			save();
		}
		return an;
	}	//	getAttributeName
	
	/**
	 * 	Get Display Type
	 *	@return display type
	 */
	public int getDisplayType()
	{
		return getProcessPara().getAD_Reference_ID();
	}	//	getDisplayType

	/**
	 * 	Is Mandatory
	 *	@return true if mandatory
	 */
	public boolean isMandatory()
	{
		return getProcessPara().isMandatory();
	}	//	isMandatory
	
	/**
	 * 	Set AD_Process_Para_ID
	 *	@param AD_Process_Para_ID id
	 */
	public void setAD_Process_Para_ID (int AD_Process_Para_ID)
	{
		super.setAD_Process_Para_ID (AD_Process_Para_ID);
		setAttributeName(null);
	}
	
}	//	MWFNodePara
