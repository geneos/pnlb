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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	GL Journal Callout
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutGLJournal.java,v 1.6 2005/12/13 00:15:27 jjanke Exp $
 */
public class CalloutGLJournal extends CalloutEngine
{
	/**
	 *  Journal - Period.
	 *  Check that selected period is in DateAcct Range or Adjusting Period
	 *  Called when C_Period_ID or DateAcct, DateDoc changed
	 */
	public String period (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		String colName = mField.getColumnName();
		if (value == null || isCalloutActive())
			return "";
		setCalloutActive(true);

		int AD_Client_ID = Env.getContextAsInt(ctx, WindowNo, "AD_Client_ID");
		Timestamp DateAcct = null;
		if (colName.equals("DateAcct"))
			DateAcct = (Timestamp)value;
		else
			DateAcct = (Timestamp)mTab.getValue("DateAcct");
		int C_Period_ID = 0;
		if (colName.equals("C_Period_ID"))
			C_Period_ID = ((Integer)value).intValue();

		//  When DateDoc is changed, update DateAcct
		if (colName.equals("DateDoc"))
		{
			mTab.setValue("DateAcct", value);
		}

		//  When DateAcct is changed, set C_Period_ID
		else if (colName.equals("DateAcct"))
		{
			String sql = "SELECT C_Period_ID "
				+ "FROM C_Period "
				+ "WHERE C_Year_ID IN "
				+ "	(SELECT C_Year_ID FROM C_Year WHERE C_Calendar_ID ="
				+ "  (SELECT C_Calendar_ID FROM AD_ClientInfo WHERE AD_Client_ID=?))"
				+ " AND ? BETWEEN StartDate AND EndDate"
				+ " AND PeriodType='S'";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, AD_Client_ID);
				pstmt.setTimestamp(2, DateAcct);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					C_Period_ID = rs.getInt(1);
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
				setCalloutActive(false);
				return e.getLocalizedMessage();
			}
			if (C_Period_ID != 0)
				mTab.setValue("C_Period_ID", new Integer(C_Period_ID));
		}

		//  When C_Period_ID is changed, check if in DateAcct range and set to end date if not
		else
		{
			String sql = "SELECT PeriodType, StartDate, EndDate "
				+ "FROM C_Period WHERE C_Period_ID=?";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, C_Period_ID);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
					String PeriodType = rs.getString(1);
					Timestamp StartDate = rs.getTimestamp(2);
					Timestamp EndDate = rs.getTimestamp(3);
					if (PeriodType.equals("S")) //  Standard Periods
					{
						//  out of range - set to last day
						if (DateAcct == null
							|| DateAcct.before(StartDate) || DateAcct.after(EndDate))
							mTab.setValue("DateAcct", EndDate);
					}
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
				setCalloutActive(false);
				return e.getLocalizedMessage();
			}
		}
		setCalloutActive(false);
		return "";
	}   //  	Journal_Period

	/**
	 * 	Journal/Line - rate.
	 * 	Set CurrencyRate from DateAcct, C_ConversionType_ID, C_Currency_ID
	 */
	public String rate (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (value == null)
			return "";
		
		//  Source info
		Integer Currency_ID = (Integer)mTab.getValue("C_Currency_ID");
		int C_Currency_ID = Currency_ID.intValue();
		Integer ConversionType_ID = (Integer)mTab.getValue("C_ConversionType_ID");
		int C_ConversionType_ID = ConversionType_ID.intValue();
		Timestamp DateAcct = (Timestamp)mTab.getValue("DateAcct");
		if (DateAcct == null)
			DateAcct = new Timestamp(System.currentTimeMillis());
		//
		int C_AcctSchema_ID = Env.getContextAsInt(ctx, WindowNo, "C_AcctSchema_ID");
		MAcctSchema as = MAcctSchema.get (ctx, C_AcctSchema_ID);
		int AD_Client_ID = Env.getContextAsInt(ctx, WindowNo, "AD_Client_ID");
		int AD_Org_ID = Env.getContextAsInt(ctx, WindowNo, "AD_Org_ID");

		BigDecimal CurrencyRate = MConversionRate.getRate(C_Currency_ID, as.getC_Currency_ID(), 
			DateAcct, C_ConversionType_ID, AD_Client_ID, AD_Org_ID);
		log.fine("rate = " + CurrencyRate);
		if (CurrencyRate == null)
			CurrencyRate = Env.ZERO;
		mTab.setValue("CurrencyRate", CurrencyRate);

		return "";
	}	//	rate
	
	/**
	 *  JournalLine - Amt.
	 *  Convert the source amount to accounted amount (AmtAcctDr/Cr)
	 *  Called when source amount (AmtSourceCr/Dr) or rate changes
	 */
	public String amt (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		String colName = mField.getColumnName();
		if (value == null || isCalloutActive())
			return "";

		setCalloutActive(true);

		//  Get Target Currency & Precision from C_AcctSchema.C_Currency_ID
		int C_AcctSchema_ID = Env.getContextAsInt(ctx, WindowNo, "C_AcctSchema_ID");
		MAcctSchema as = MAcctSchema.get(ctx, C_AcctSchema_ID);
		int Precision = as.getStdPrecision();

		BigDecimal CurrencyRate = (BigDecimal)mTab.getValue("CurrencyRate");
		if (CurrencyRate == null)
		{
			CurrencyRate = Env.ONE;
			mTab.setValue("CurrencyRate", CurrencyRate);
		}

		//  AmtAcct = AmtSource * CurrencyRate  ==> Precision
		BigDecimal AmtSourceDr = (BigDecimal)mTab.getValue("AmtSourceDr");
		if (AmtSourceDr == null)
			AmtSourceDr = Env.ZERO;
		BigDecimal AmtSourceCr = (BigDecimal)mTab.getValue("AmtSourceCr");
		if (AmtSourceCr == null)
			AmtSourceCr = Env.ZERO;

		BigDecimal AmtAcctDr = AmtSourceDr.multiply(CurrencyRate);
		AmtAcctDr = AmtAcctDr.setScale(Precision, BigDecimal.ROUND_HALF_UP);
		mTab.setValue("AmtAcctDr", AmtAcctDr);
		BigDecimal AmtAcctCr = AmtSourceCr.multiply(CurrencyRate);
		AmtAcctCr = AmtAcctCr.setScale(Precision, BigDecimal.ROUND_HALF_UP);
		mTab.setValue("AmtAcctCr", AmtAcctCr);

		setCalloutActive(false);
		return "";
	}   //  amt
	
	/**
	 *	Journal - DocType.
	 *			- gets DocNo
	 */
	public String docType (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer C_DocType_ID = (Integer)value;
		if (C_DocType_ID == null || C_DocType_ID.intValue() == 0)
			return "";

		String sql = "SELECT d.DocBaseType, d.IsDocNoControlled, s.CurrentNext "
			+ "FROM C_DocType d, AD_Sequence s "
			+ "WHERE C_DocType_ID=?"		//	1
			+ " AND d.DocNoSequence_ID=s.AD_Sequence_ID(+)";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_DocType_ID.intValue());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	DocumentNo
				mTab.setValue("DocumentNo", "<" + rs.getString("CurrentNext") + ">");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		return "";
	}	//	docType
	
}	//	CalloutGLJournal
