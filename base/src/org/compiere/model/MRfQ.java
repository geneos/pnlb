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
 *	RfQ Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQ.java,v 1.10 2005/11/14 02:10:52 jjanke Exp $
 */
public class MRfQ extends X_C_RfQ
{
	/**
	 * 	Get MRfQ from Cache
	 *	@param ctx context
	 *	@param C_RfQ_ID id
	 *	@return MRfQ
	 */
	public static MRfQ get (Properties ctx, int C_RfQ_ID, String trxName)
	{
		Integer key = new Integer (C_RfQ_ID);
		MRfQ retValue = (MRfQ) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MRfQ (ctx, C_RfQ_ID, trxName);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,MRfQ>	s_cache	= new CCache<Integer,MRfQ>("C_RfQ", 10);
	
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RfQ_ID id
	 */
	public MRfQ (Properties ctx, int C_RfQ_ID, String trxName)
	{
		super (ctx, C_RfQ_ID, trxName);
		if (C_RfQ_ID == 0)
		{
		//	setC_RfQ_Topic_ID (0);
		//	setName (null);
		//	setC_Currency_ID (0);	// @$C_Currency_ID @
		//	setSalesRep_ID (0);
			//
			setDateResponse (new Timestamp(System.currentTimeMillis()));
			setDateWorkStart (new Timestamp(System.currentTimeMillis()));
			setIsInvitedVendorsOnly (false);
			setQuoteType (QUOTETYPE_QuoteSelectedLines);
			setIsQuoteAllQty (false);
			setIsQuoteTotalAmt (false);
			setIsRfQResponseAccepted (true);
			setIsSelfService (true);
			setProcessed (false);
		}
	}	//	MRfQ

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRfQ (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRfQ
	
	/**
	 * 	Get active Lines
	 *	@return array of lines
	 */
	public MRfQLine[] getLines()
	{
		ArrayList<MRfQLine> list = new ArrayList<MRfQLine>();
		String sql = "SELECT * FROM C_RfQLine "
			+ "WHERE C_RfQ_ID=? AND IsActive='Y' "
			+ "ORDER BY Line";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_RfQ_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRfQLine (getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		MRfQLine[] retValue = new MRfQLine[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getLines

	/**
	 * 	Get RfQ Responses
	 * 	@param activeOnly active responses only
	 * 	@param completedOnly complete responses only
	 *	@return array of lines
	 */
	public MRfQResponse[] getResponses (boolean activeOnly, boolean completedOnly)
	{
		ArrayList<MRfQResponse> list = new ArrayList<MRfQResponse>();
		String sql = "SELECT * FROM C_RfQResponse "
			+ "WHERE C_RfQ_ID=?";
		if (activeOnly)
			sql += " AND IsActive='Y'";
		if (completedOnly)
			sql += " AND IsComplete='Y'";
		sql += " ORDER BY Price";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_RfQ_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRfQResponse (getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		MRfQResponse[] retValue = new MRfQResponse[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getResponses
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MRfQ[");
		sb.append(get_ID()).append(",Name=").append(getName())
			.append(",QuoteType=").append(getQuoteType())
			.append("]");
		return sb.toString ();
	}	//	toString
	
	
	/**************************************************************************
	 * 	Is Quote Total Amt Only
	 *	@return true if total amout only
	 */
	public boolean isQuoteTotalAmtOnly()
	{
		return QUOTETYPE_QuoteTotalOnly.equals(getQuoteType());
	}	//	isQuoteTotalAmtOnly
	
	/**
	 * 	Is Quote Selected Lines
	 *	@return true if quote selected lines
	 */
	public boolean isQuoteSelectedLines()
	{
		return QUOTETYPE_QuoteSelectedLines.equals(getQuoteType());
	}	//	isQuoteSelectedLines

	/**
	 * 	Is Quote All Lines
	 *	@return true if quote selected lines
	 */
	public boolean isQuoteAllLines()
	{
		return QUOTETYPE_QuoteAllLines.equals(getQuoteType());
	}	//	isQuoteAllLines

	/**
	 * 	Is "Quote Total Amt Only" Valid
	 *	@return null or error message
	 */
	public String checkQuoteTotalAmtOnly()
	{
		if (!isQuoteTotalAmtOnly())
			return null;
		//	Need to check Line Qty
		MRfQLine[] lines = getLines();
		for (int i = 0; i < lines.length; i++)
		{
			MRfQLine line = lines[i];
			MRfQLineQty[] qtys = line.getQtys();
			if (qtys.length > 1)
			{
				log.warning("isQuoteTotalAmtOnlyValid - #" + qtys.length + " - " + line);
				String msg = "@Line@ " + line.getLine() 
					+ ": #@C_RfQLineQty@=" + qtys.length + " - @IsQuoteTotalAmt@";
				return msg;
			}
		}
		return null;
	}	//	checkQuoteTotalAmtOnly
		
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Calculate Complete Date (also used to verify)
		if (getDateWorkStart() != null && getDeliveryDays() != 0)
			setDateWorkComplete (TimeUtil.addDays(getDateWorkStart(), getDeliveryDays()));
		//	Calculate Delivery Days
		else if (getDateWorkStart() != null && getDeliveryDays() == 0 && getDateWorkComplete() != null)
			setDeliveryDays (TimeUtil.getDaysBetween(getDateWorkStart(), getDateWorkComplete()));
		//	Calculate Start Date
		else if (getDateWorkStart() == null && getDeliveryDays() != 0 && getDateWorkComplete() != null)
			setDateWorkStart (TimeUtil.addDays(getDateWorkComplete(), getDeliveryDays() * -1));
		
		return true;
	}	//	beforeSave
	
}	//	MRfQ
