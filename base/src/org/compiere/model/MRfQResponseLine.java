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
 *	RfQ Response Line Model	
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQResponseLine.java,v 1.14 2005/11/14 02:10:53 jjanke Exp $
 */
public class MRfQResponseLine extends X_C_RfQResponseLine
{
	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MRfQResponseLine (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MRfQResponseLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRfQResponseLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRfQResponseLine
	
	/**
	 * 	Parent Constructor.
	 * 	Also creates qtys if RfQ Qty
	 * 	Is saved if there are qtys(!)
	 *	@param response response
	 *	@param line line
	 */
	public MRfQResponseLine (MRfQResponse response, MRfQLine line)
	{
		super (response.getCtx(), 0, response.get_TrxName());
		setClientOrg(response);
		setC_RfQResponse_ID (response.getC_RfQResponse_ID());
		//
		setC_RfQLine_ID (line.getC_RfQLine_ID());
		//
		setIsSelectedWinner (false);
		setIsSelfService (false);
		//
		MRfQLineQty[] qtys = line.getQtys();
		for (int i = 0; i < qtys.length; i++)
		{
			if (qtys[i].isActive() && qtys[i].isRfQQty())
			{
				if (get_ID() == 0)	//	save this line
					save();
				MRfQResponseLineQty qty = new MRfQResponseLineQty (this, qtys[i]);
				qty.save();
			}
		}
	}	//	MRfQResponseLine
	
	/**	RfQ Line				*/
	private MRfQLine				m_rfqLine = null;
	/**	Quantities				*/
	private MRfQResponseLineQty[] 	m_qtys = null;
	
	/**
	 * 	Get Quantities
	 *	@return array of quantities
	 */
	public MRfQResponseLineQty[] getQtys ()
	{
		return getQtys (false);
	}	//	getQtys

	/**
	 * 	Get Quantities
	 * 	@param requery requery
	 *	@return array of quantities
	 */
	public MRfQResponseLineQty[] getQtys (boolean requery)
	{
		if (m_qtys != null && !requery)
			return m_qtys;
		
		ArrayList<MRfQResponseLineQty> list = new ArrayList<MRfQResponseLineQty>();
		String sql = "SELECT * FROM C_RfQResponseLineQty "
			+ "WHERE C_RfQResponseLine_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_RfQResponseLine_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRfQResponseLineQty(getCtx(), rs, get_TrxName()));
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
		
		m_qtys = new MRfQResponseLineQty[list.size ()];
		list.toArray (m_qtys);
		return m_qtys;
	}	//	getQtys
	
	/**
	 * 	Get RfQ Line
	 *	@return rfq line
	 */
	public MRfQLine getRfQLine()
	{
		if (m_rfqLine == null)
			m_rfqLine = MRfQLine.get(getCtx(), getC_RfQLine_ID(), get_TrxName());
		return m_rfqLine;
	}	//	getRfQLine
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MRfQResponseLine[");
		sb.append(get_ID()).append(",Winner=").append(isSelectedWinner())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	
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

		if (!isActive())
			setIsSelectedWinner(false);
		return true;
	}	//	beforeSave
	

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!isActive())
		{
			getQtys (false);
			for (int i = 0; i < m_qtys.length; i++)
			{
				MRfQResponseLineQty qty = m_qtys[i];
				if (qty.isActive())
				{
					qty.setIsActive(false);
					qty.save();
				}
			}
		}
		return success;
	}	//	success
	
}	//	MRfQResponseLine
