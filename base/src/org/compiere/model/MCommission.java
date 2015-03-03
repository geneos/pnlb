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
 *	Model for Commission.
 *	(has Lines)
 *	
 *  @author Jorg Janke
 *  @version $Id: MCommission.java,v 1.8 2005/09/24 01:52:52 jjanke Exp $
 */
public class MCommission extends X_C_Commission
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Commission_ID id
	 */
	public MCommission(Properties ctx, int C_Commission_ID, String trxName)
	{
		super(ctx, C_Commission_ID, trxName);
		if (C_Commission_ID == 0)
		{
		//	setName (null);
		//	setC_BPartner_ID (0);
		//	setC_Charge_ID (0);
		//	setC_Commission_ID (0);
		//	setC_Currency_ID (0);
			//
			setDocBasisType (DOCBASISTYPE_Invoice);	// I
			setFrequencyType (FREQUENCYTYPE_Monthly);	// M
			setListDetails (false);
		}
	}	//	MCommission

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MCommission(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCommission

	/**
	 * 	Get Lines
	 *	@return array of lines
	 */
	public MCommissionLine[] getLines()
	{
		String sql = "SELECT * FROM C_CommissionLine WHERE C_Commission_ID=? ORDER BY Line";
		ArrayList<MCommissionLine> list = new ArrayList<MCommissionLine>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_Commission_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MCommissionLine(getCtx(), rs, get_TrxName()));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e); 
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
		
		//	Convert
		MCommissionLine[] retValue = new MCommissionLine[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getLines

	/**
	 * 	Set Date Last Run
	 *	@param DateLastRun date
	 */
	public void setDateLastRun (Timestamp DateLastRun)
	{
		if (DateLastRun != null)
			super.setDateLastRun(DateLastRun);
	}	//	setDateLastRun

	/**
	 * 	Copy Lines From other Commission
	 *	@param otherCom commission
	 *	@return number of lines copied
	 */
	public int copyLinesFrom (MCommission otherCom)
	{
		if (otherCom == null)
			return 0;
		MCommissionLine[] fromLines = otherCom.getLines ();
		int count = 0;
		for (int i = 0; i < fromLines.length; i++)
		{
			MCommissionLine line = new MCommissionLine (getCtx(), 0, get_TrxName());
			PO.copyValues(fromLines[i], line, getAD_Client_ID(), getAD_Org_ID());
			line.setC_Commission_ID (getC_Commission_ID());
			line.setC_CommissionLine_ID (0);	//	new
			if (line.save())
				count++;
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE, "copyLinesFrom - Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom

}	//	MCommission
