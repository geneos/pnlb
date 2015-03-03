package org.compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.compiere.util.DB;

public class TasaCambio {


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
	{
		//	No Conversion
		if (p_C_CurrencyFrom_ID == p_C_CurrencyTo_ID)
			return new BigDecimal(1);
		
		BigDecimal rate = new BigDecimal(1);
		try{
			//	Get Defaults
		
			Timestamp ConversionDate = p_ConversionDate; 
			if (ConversionDate == null)
				ConversionDate = new Timestamp(System.currentTimeMillis());
			//ConversionDate = Compiere.trunc(ConversionDate);
			//
			int C_ConversionType_ID = p_C_ConversionType_ID;
			if (C_ConversionType_ID == 0)
			{
				String sql = "SELECT C_ConversionType_ID "
					+ "FROM C_ConversionType "
					+ "WHERE IsDefault='Y' "
					+ " AND AD_Client_ID IN (0,?) "
					+ "ORDER BY AD_Client_ID DESC";
				
				PreparedStatement pstmt = DB.prepareStatement(sql,null);
				pstmt.setInt(1, p_AD_Client_ID);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					C_ConversionType_ID = rs.getInt(1);
				rs.close();
				pstmt.close();
			}
			
			//	Get Rate
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
				rate = rs.getBigDecimal(1);
			rs.close();
			pstmt.close();

		}
		catch (SQLException e){
			e.printStackTrace();
			return new BigDecimal(1);
		}
			
		return rate;
	}	//	rate
	
}
