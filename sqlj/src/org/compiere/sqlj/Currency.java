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
package org.compiere.sqlj;

import java.math.*;
import java.sql.*;
import org.compiere.util.DB;


/**
 *	SQLJ Currency related Functions
 *	
 *  @author Jorg Janke
 *  @version $Id: Currency.java,v 1.6 2005/10/11 02:26:14 jjanke Exp $
 */
public class Currency
{
	/**
	 * 	Convert Amount to base Currency.
	 *  (default conversion type)
	 * 	Previously:  C_Base_Convert - Now: currencyBase
	 *	@param p_C_CurrencyFrom_ID from currency
	 *	@param p_ConversionDate conversion date
	 *	@param p_AD_Client_ID client
	 *	@param p_AD_Org_ID org
	 *	@return rate or null
	 *	@throws SQLException
	 */
	public static BigDecimal base (BigDecimal p_Amount,
		int p_C_CurrencyFrom_ID, Timestamp p_ConversionDate,
		int p_AD_Client_ID, int p_AD_Org_ID)
		throws SQLException
	{
		//	Return NULL
		if (p_Amount == null || p_C_CurrencyFrom_ID == 0)
			return null;
		//	Return Amount
		if (p_Amount.signum() == 0)
			return p_Amount;
		
		//	Base Currency
		String sql = "SELECT ac.C_Currency_ID "
			+ "FROM AD_ClientInfo ci"
			+ " INNER JOIN C_AcctSchema ac ON (ci.C_AcctSchema1_ID=ac.C_AcctSchema_ID) "
			+ "WHERE ci.AD_Client_ID=?";
		int C_CurrencyTo_ID = Compiere.getSQLValue(sql, p_AD_Client_ID);
		//	Return Amount
		if (p_C_CurrencyFrom_ID == C_CurrencyTo_ID)
			return p_Amount;
		//
		return convert(p_Amount, p_C_CurrencyFrom_ID, C_CurrencyTo_ID, 
			p_ConversionDate, 0, p_AD_Client_ID, p_AD_Org_ID);
	}	//	base
	
	
	/**
	 * 	Convert Amount to Currency.
	 * 	Previously:  currencyConvert - Now: currencyConvert
	 *	@param p_C_CurrencyFrom_ID from currency
	 *	@param p_C_CurrencyTo_ID to currency
	 *	@param p_ConversionDate conversion date
	 *	@param p_C_ConversionType_ID conversion type
	 *	@param p_AD_Client_ID client
	 *	@param p_AD_Org_ID org
	 *	@return rate or null
	 *	@throws SQLException
	 */
	public static BigDecimal convert (BigDecimal p_Amount,
		int p_C_CurrencyFrom_ID, int p_C_CurrencyTo_ID,
		Timestamp p_ConversionDate, int p_C_ConversionType_ID,
		int p_AD_Client_ID, int p_AD_Org_ID)
		throws SQLException
	{
		//	Return NULL
		if (p_Amount == null || p_C_CurrencyFrom_ID == 0 || p_C_CurrencyTo_ID == 0)
			return null;
		//	Return Amount
		if (p_Amount.signum() == 0 || p_C_CurrencyFrom_ID == p_C_CurrencyTo_ID)
			return p_Amount;

		//	Get Rate
		BigDecimal rate = rate (p_C_CurrencyFrom_ID, p_C_CurrencyTo_ID, 
			p_ConversionDate, p_C_ConversionType_ID, p_AD_Client_ID, p_AD_Org_ID);
		if (rate == null)
			return null;

		//	Round
		return round(p_Amount.multiply(rate), p_C_CurrencyTo_ID, null);
	}	//	convert
	
	
	/**
	 * 	Get Conversion Rate.
	 * 	Previously:  C_Currency_Rate - Now: currencyRate
	 *	@param p_C_CurrencyFrom_ID from currency
	 *	@param p_C_CurrencyTo_ID to currency
	 *	@param p_ConversionDate conversion date
	 *	@param p_C_ConversionType_ID conversion type
	 *	@param p_AD_Client_ID client
	 *	@param p_AD_Org_ID org
	 *	@return rate or null
	 *	@throws SQLException
	 */
	public static BigDecimal rate (int p_C_CurrencyFrom_ID, int p_C_CurrencyTo_ID,
		Timestamp p_ConversionDate, int p_C_ConversionType_ID,
		int p_AD_Client_ID, int p_AD_Org_ID)
		throws SQLException
	{
		//	No Conversion
		if (p_C_CurrencyFrom_ID == p_C_CurrencyTo_ID)
			return Compiere.ONE;
		
		//	Get Defaults
		Timestamp ConversionDate = p_ConversionDate; 
		if (ConversionDate == null)
			ConversionDate = new Timestamp(System.currentTimeMillis());
		ConversionDate = Compiere.trunc(ConversionDate);
		//
		int C_ConversionType_ID = p_C_ConversionType_ID;
		if (C_ConversionType_ID == 0)
		{
			String sql = "SELECT C_ConversionType_ID "
				+ "FROM C_ConversionType "
				+ "WHERE IsDefault='Y'"
				+ " AND AD_Client_ID IN (0,?) "
				+ "ORDER BY AD_Client_ID DESC";
			C_ConversionType_ID = Compiere.getSQLValue(sql, p_AD_Client_ID);
		}
		
		/**	Get Euro Currency Info
		String sql = "SELECT IsEuro, IsEMUMember, EMUEntryDate, EMURate "
			+ "FROM C_Currency "
			+ "WHERE C_Currency_ID=?";
		--	Fixed - From Euro to EMU
		IF (cf_IsEuro = 'Y' AND ct_IsEMUMember ='Y' AND v_ConvDate >= ct_EMUEntryDate) THEN
			RETURN ct_EMURate;
			END IF;
		--	Fixed - From EMU to Euro
		IF (ct_IsEuro = 'Y' AND cf_IsEMUMember ='Y' AND v_ConvDate >= cf_EMUEntryDate) THEN
			RETURN 1 / cf_EMURate;
		END IF;
		--	Fixed - From EMU to EMU
		IF (cf_IsEMUMember = 'Y' AND cf_IsEMUMember ='Y'
			AND v_ConvDate >= cf_EMUEntryDate AND v_ConvDate >= ct_EMUEntryDate) THEN
			RETURN ct_EMURate / cf_EMURate;
		END IF;
		--	Flexible Rates
		-- if EMU Member involved, replace From/To Currency
		IF ((cf_isEMUMember = 'Y' AND v_ConvDate >= cf_EMUEntryDate)
			OR (ct_isEMUMember = 'Y' AND v_ConvDate >= ct_EMUEntryDate)) THEN
			SELECT	MAX(C_Currency_ID)
				INTO	v_CurrencyEuro
			FROM		C_Currency
			WHERE	IsEuro = 'Y';
			-- Conversion Rate not Found
			IF (v_CurrencyEuro IS NULL) THEN
				DBMS_OUTPUT.PUT_LINE('Euro Not Found');
				RETURN NULL;
			END IF;
			IF (cf_isEMUMember = 'Y' AND v_ConvDate >= cf_EMUEntryDate) THEN
				v_CurrencyFrom := v_CurrencyEuro;
			ELSE
				v_CurrencyTo := v_CurrencyEuro;
			END IF;
		END IF;
		**/

		//	Get Rate
		BigDecimal rate = null;
		String sql = "SELECT MultiplyRate "
			+ "FROM C_Conversion_Rate "
			+ "WHERE C_Currency_ID=? AND C_Currency_ID_To=?"	//	from/to
			+ " AND C_ConversionType_ID=?"
			+ " AND TRUNC(ValidFrom) <= ?"
			+ " AND TRUNC(ValidTo) >= ?"
			+ " AND AD_Client_ID IN (0,?) AND AD_Org_ID IN (0,?) "
			+ "ORDER BY AD_Client_ID DESC, AD_Org_ID DESC, ValidFrom DESC";
		PreparedStatement pstmt = DB.prepareStatement(sql,null);
		pstmt.setInt(1, p_C_CurrencyFrom_ID);
		pstmt.setInt(2, p_C_CurrencyTo_ID);
		pstmt.setInt(3, C_ConversionType_ID);
		pstmt.setTimestamp(4, ConversionDate);
		pstmt.setTimestamp(5, ConversionDate);
		pstmt.setInt(6, p_AD_Client_ID);
		pstmt.setInt(7, p_AD_Org_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
		{
			rate = rs.getBigDecimal(1);
		}
		rs.close();
		pstmt.close();
		//	Not found
		if (rate == null)
			return null;
		
		/**	Currency From was EMU
		IF (cf_isEMUMember = 'Y' AND v_ConvDate >= cf_EMUEntryDate) THEN
			RETURN v_Rate / cf_EMURate;
		END IF;
		--	Currency To was EMU
		IF (ct_isEMUMember = 'Y' AND v_ConvDate >= ct_EMUEntryDate) THEN
			RETURN v_Rate * ct_EMURate;
		END IF;
		**/
		return rate;
	}	//	rate
	
	
	/**
	 * 	Round amount to Currency precision.
	 	SELECT ISO_CODE, currencyRound(111.111111, C_Currency_ID, 'N') "Std", currencyRound(111.111111, C_Currency_ID, 'Y') "Cost" FROM C_Currency WHERE C_Currency_ID IN (100,113)
	 *	@param p_Amount amount
	 *	@param p_C_Currency_ID currency
	 *	@param p_Costing Y if costing precision
	 *	@return rounded amount
	 *	@throws SQLException
	 */
	public static BigDecimal round (BigDecimal p_Amount, int p_C_Currency_ID, 
		String p_Costing)
		throws SQLException
	{
		if (p_Amount == null)
			return null;
		if (p_Amount.signum() == 0 || p_C_Currency_ID == 0) 
			return p_Amount;
		//
		boolean costing = p_Costing != null && "Y".equals(p_Costing);
		//
		BigDecimal result = p_Amount;
		String sql = "SELECT StdPrecision, CostingPrecision "
			+ "FROM C_Currency "
			+ "WHERE C_Currency_ID=?";
		PreparedStatement pstmt = DB.prepareStatement(sql,null);
		pstmt.setInt(1, p_C_Currency_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
		{
			int index = costing ? 2 : 1;
			int prec = rs.getInt(index);
			if (result.scale() > prec)
				result = result.setScale(prec, BigDecimal.ROUND_HALF_UP);
		}
		rs.close();
		pstmt.close();
		//
		return result;
	}	//	round
	
	/**
	 * 	Test
	 *	@param args ignored

SELECT p.DateTrx,p.DocumentNo,p.C_Payment_ID,c.ISO_Code,p.PayAmt,
currencyConvert(p.PayAmt,p.C_Currency_ID,100,p.DateTrx,p.C_ConversionType_ID,p.AD_Client_ID,p.AD_Org_ID) xPayAmt,
paymentAvailable(C_Payment_ID) Available,
currencyConvert(paymentAvailable(C_Payment_ID),p.C_Currency_ID,100,p.DateTrx,p.C_ConversionType_ID,p.AD_Client_ID,p.AD_Org_ID) xAvailable,
p.MultiplierAP 
FROM C_Payment_v p 
INNER JOIN C_Currency c ON (p.C_Currency_ID=c.C_Currency_ID) 
ORDER BY p.DateTrx,p.DocumentNo

	 *
	public static void main (String[] args)
	{
		try
		{
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Compiere.s_type = Compiere.TYPE_ORACLE;
			Compiere.s_url = "jdbc:oracle:thin:@//dev1:1521/dev1.compiere.org";
			Compiere.s_uid = "compiere";
			Compiere.s_pwd = "compiere";
			System.out.println(Currency.convert(new BigDecimal(102), 102, 100, null, 0, 11, 0));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}	//	main	/* */
	
}	//	Currency
