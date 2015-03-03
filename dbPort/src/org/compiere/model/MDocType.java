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
 *	Document Type Model
 *
 *  @author Jorg Janke
 *  @version $Id: MDocType.java,v 1.19 2005/11/25 21:58:29 jjanke Exp $
 */
public class MDocType extends X_C_DocType
{
	/**
	 * 	Get Client Document Type with DocBaseType
	 *	@param ctx context
	 *	@param DocBaseType base document type
	 *	@return array of doc types
	 */
	static public MDocType[] getOfDocBaseType (Properties ctx, String DocBaseType)
	{
		ArrayList<MDocType> list = new ArrayList<MDocType>();
		String sql = "SELECT * FROM C_DocType WHERE AD_Client_ID=? AND DocBaseType=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, Env.getAD_Client_ID(ctx));
			pstmt.setString(2, DocBaseType);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MDocType(ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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

		MDocType[] retValue = new MDocType[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfDocBaseType

	/**
	 * 	Get Client Document Types
	 *	@param ctx context
	 *	@return array of doc types
	 */
	static public MDocType[] getOfClient (Properties ctx)
	{
		ArrayList<MDocType> list = new ArrayList<MDocType>();
		String sql = "SELECT * FROM C_DocType WHERE AD_Client_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, Env.getAD_Client_ID(ctx));
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MDocType(ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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

		MDocType[] retValue = new MDocType[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfClient

	/**
	 * 	Get Client Distinct Document Base Types
	 *	@param ctx context
	 *	@return array of doc base types
	 */
	static public String[] getDocBase (Properties ctx)
	{
		ArrayList<String> list = new ArrayList<String>();
		String sql = "SELECT Distinct DOCBASETYPE FROM C_DocType WHERE AD_Client_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, Env.getAD_Client_ID(ctx));
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(rs.getString(1));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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

		String[] retValue = new String[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	getDocBase

    static public MDocType getDocTypeBy (Properties ctx, String printName)
	{
		String sql = "SELECT * FROM C_DocType WHERE PrintName = ?";
		PreparedStatement pstmt = null;
        MDocType retValue = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setString(1, printName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ()) {
				retValue = new MDocType(ctx, rs, null);
            }
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
		return retValue;
	}	//	getDocBase



	/**
	 * 	Get Document Type (cached)
	 *	@param ctx context
	 *	@param C_DocType_ID id
	 *	@return document type
	 */
	static public MDocType get (Properties ctx, int C_DocType_ID)
	{
		Integer key = new Integer(C_DocType_ID);
		MDocType retValue = (MDocType)s_cache.get(key);
		if (retValue == null)
		{
			retValue = new MDocType (ctx, C_DocType_ID, null);
			s_cache.put(key, retValue);
		}
		return retValue;
	} 	//	get

	/**	Cache					*/
	static private CCache<Integer,MDocType>	s_cache = new CCache<Integer,MDocType>("C_DocType", 20);
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MDocType.class);


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_DocType_ID id
	 */
	public MDocType(Properties ctx, int C_DocType_ID, String trxName)
	{
		super(ctx, C_DocType_ID, trxName);
		if (C_DocType_ID == 0)
		{
		//	setName (null);
		//	setPrintName (null);
		//	setDocBaseType (null);
		//	setGL_Category_ID (0);
			setDocumentCopies (0);
			setHasCharges (false);
			setIsDefault (false);
			setIsDocNoControlled (false);
			setIsSOTrx (false);
			setIsPickQAConfirm(false);
			setIsShipConfirm(false);
			setIsSplitWhenDifference(false);
			//
			setIsCreateCounter(true);
			setIsDefaultCounterDoc(false);
		}
	}	//	MDocType

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MDocType(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MDocType

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param DocBaseType document base type
	 *	@param Name name
	 */
	public MDocType (Properties ctx, String DocBaseType, String Name, String trxName)
	{
		this (ctx, 0, trxName);
		setAD_Org_ID(0);
		setDocBaseType (DocBaseType);
		setName (Name);
		setPrintName (Name);
		setGL_Category_ID ();
	}	//	MDocType

	/**
	 * 	Set Default GL Category
	 */
	public void setGL_Category_ID()
	{
		String sql = "SELECT * FROM GL_Category WHERE AD_Client_ID=? AND IsDefault='Y'";
		int GL_Category_ID = DB.getSQLValue(get_TrxName(), sql, getAD_Client_ID());
		if (GL_Category_ID == 0)
		{
			sql = "SELECT * FROM GL_Category WHERE AD_Client_ID=?";
			GL_Category_ID = DB.getSQLValue(get_TrxName(), sql, getAD_Client_ID());
		}
		setGL_Category_ID(GL_Category_ID);
	}	//	setGL_Category_ID


	/**
	 * 	Set SOTrx based on document base type
	 */
	public void setIsSOTrx ()
	{
		boolean isSOTrx = DOCBASETYPE_SalesOrder.equals(getDocBaseType())
			|| DOCBASETYPE_MaterialDelivery.equals(getDocBaseType())
			|| getDocBaseType().startsWith("AR");
		super.setIsSOTrx (isSOTrx);
	}	//	setIsSOTrx

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MDocType[");
		sb.append(get_ID()).append("-").append(getName())
			.append(",DocNoSequence_ID=").append(getDocNoSequence_ID())
			.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Is this a Quotation (Binding)
	 *	@return true if Quotation
	 */
	public boolean isQuotation()
	{
		return DOCSUBTYPESO_Quotation.equals(getDocSubTypeSO())
			&& DOCBASETYPE_SalesOrder.equals(getDocBaseType());
	}	//	isQuotation

	/**
	 * 	Is this a Proposal (Not binding)
	 *	@return true if proposal
	 */
	public boolean isProposal()
	{
		return DOCSUBTYPESO_Proposal.equals(getDocSubTypeSO())
			&& DOCBASETYPE_SalesOrder.equals(getDocBaseType());
	}	//	isProposal

	/**
	 * 	Is this a Proposal or Quotation
	 *	@return true if proposal or quotation
	 */
	public boolean isOffer()
	{
		return (DOCSUBTYPESO_Proposal.equals(getDocSubTypeSO())
				|| DOCSUBTYPESO_Quotation.equals(getDocSubTypeSO()))
			&& DOCBASETYPE_SalesOrder.equals(getDocBaseType());
	}	//	isOffer


	/**
	 * 	Get Print Name
	 *	@return print Name if available translated
	 */
	public String getPrintName (String AD_Language)
	{
		if (AD_Language == null || AD_Language.length() == 0)
			return super.getPrintName();
		String retValue = get_Translation ("PrintName", AD_Language);
		if (retValue != null)
			return retValue;
		return super.getPrintName();
	}	//	getPrintName

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		return true;
	}	//	beforeSave

}	//	MDocType
