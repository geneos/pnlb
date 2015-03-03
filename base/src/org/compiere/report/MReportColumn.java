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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.model.*;

/**
 *  Report Column Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReportColumn.java,v 1.22 2005/09/19 04:49:48 jjanke Exp $
 */
public class MReportColumn extends X_PA_ReportColumn
{
	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param PA_ReportColumn_ID id
	 */
	public MReportColumn (Properties ctx, int PA_ReportColumn_ID, String trxName)
	{
		super (ctx, PA_ReportColumn_ID, trxName);
		if (PA_ReportColumn_ID == 0)
		{
			setIsPrinted (true);
			setSeqNo (0);
		}
	}	//	MReportColumn

	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param rs ResultSet to load from
	 */
	public MReportColumn (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MReportColumn

	/**************************************************************************
	 * 	Get Column SQL Select Clause.
	 * 	@param withSum with SUM() function
	 * 	@return select clause - AmtAcctCR+AmtAcctDR/etc or "null" if not defined
	 */
	public String getSelectClause (boolean withSum)
	{
		//	Amount Type = Period Balance, Period Credit
		String amountType = getAmountType().substring(0,1);	//	first character
		StringBuffer sb = new StringBuffer();
		if (withSum)
			sb.append("SUM(");
		if (AmountType_Balance.equals(amountType))
		//	sb.append("AmtAcctDr-AmtAcctCr");
			sb.append("acctBalance(Account_ID,AmtAcctDr,AmtAcctCr)");
		else if (AmountType_CR.equals(amountType))
			sb.append("AmtAcctCr");
		else if (AmountType_DR.equals(amountType))
			sb.append("AmtAcctDr");
		else if (AmountType_Qty.equals(amountType))
			sb.append("Qty");
		else
		{
			log.log(Level.SEVERE, "AmountType=" + getAmountType () + ", at=" + amountType);
			return "NULL";
		}
		if (withSum)
			sb.append(")");
		return sb.toString();
	}	//	getSelectClause

	/**
	 * 	Is it Period Info ?
	 * 	@return true if Period Amount Type
	 */
	public boolean isPeriod()
	{
		String at = getAmountType();
		if (at == null)
			return false;
		return AMOUNTTYPE_PeriodBalance.equals(at)
			|| AMOUNTTYPE_PeriodCreditOnly.equals(at)
			|| AMOUNTTYPE_PeriodDebitOnly.equals(at)
			|| AMOUNTTYPE_PeriodQuantity.equals(at);
	}	//	isPeriod

	/**
	 * 	Is it Year Info ?
	 * 	@return true if Year Amount Type
	 */
	public boolean isYear()
	{
		String at = getAmountType();
		if (at == null)
			return false;
		return AMOUNTTYPE_YearBalance.equals(at)
			|| AMOUNTTYPE_YearCreditOnly.equals(at)
			|| AMOUNTTYPE_YearDebitOnly.equals(at)
			|| AMOUNTTYPE_YearQuantity.equals(at);
	}	//	isYear

	/**
	 * 	Is it Total Info ?
	 * 	@return true if Year Amount Type
	 */
	public boolean isTotal()
	{
		String at = getAmountType();
		if (at == null)
			return false;
		return AMOUNTTYPE_TotalBalance.equals(at)
			|| AMOUNTTYPE_TotalCreditOnly.equals(at)
			|| AMOUNTTYPE_TotalDebitOnly.equals(at)
			|| AMOUNTTYPE_TotalQuantity.equals(at);
	}	//	isTotalBalance


	/**
	 * 	Get String Representation
	 * 	@return	String Representation
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MReportColumn[")
			.append(get_ID()).append(" - ").append(getName()).append(" - ").append(getDescription())
			.append(", SeqNo=").append(getSeqNo()).append(", AmountType=").append(getAmountType())
			.append(", CurrencyType=").append(getCurrencyType()).append("/").append(getC_Currency_ID())
			.append(" - ColumnType=").append(getColumnType());
		if (isColumnTypeCalculation())
			sb.append(" - Calculation=").append(getCalculationType())
				.append(" - ").append(getOper_1_ID()).append(" - ").append(getOper_2_ID());
		else if (isColumnTypeRelativePeriod())
			sb.append(" - Period=").append(getRelativePeriod());
		else
			sb.append(" - SegmentValue ElementType=").append(getElementType());
		sb.append ("]");
		return sb.toString ();
	}	//	toString

	public static final String		AmountType_Balance = "B";
	public static final String		AmountType_CR = "C";
	public static final String		AmountType_DR = "D";
	public static final String		AmountType_Qty = "Q";
	//
	public static final String		AmountType_Period = "P";
	public static final String		AmountType_Year = "Y";
	public static final String		AmountType_Total = "T";


	public boolean isCalculationTypeRange()
	{
		return CALCULATIONTYPE_AddRangeOp1ToOp2.equals(getCalculationType());
	}
	public boolean isCalculationTypeAdd()
	{
		return CALCULATIONTYPE_AddOp1PlusOp2.equals(getCalculationType());
	}
	public boolean isCalculationTypeSubtract()
	{
		return CALCULATIONTYPE_SubtractOp1_Op2.equals(getCalculationType());
	}
	public boolean isCalculationTypePercent()
	{
		return CALCULATIONTYPE_PercentageOp1OfOp2.equals(getCalculationType());
	}


	public boolean isColumnTypeCalculation()
	{
		return COLUMNTYPE_Calculation.equals(getColumnType());
	}
	public boolean isColumnTypeRelativePeriod()
	{
		return COLUMNTYPE_RelativePeriod.equals(getColumnType());
	}
	public boolean isColumnTypeSegmentValue()
	{
		return COLUMNTYPE_SegmentValue.equals(getColumnType());
	}

	public int getRelativePeriodAsInt ()
	{
		BigDecimal bd = getRelativePeriod();
		if (bd == null)
			return 0;
		return bd.intValue();
	}	//	getRelativePeriodAsInt


	/**************************************************************************

	/**
	 * 	Copy
	 * 	@param ctx context
	 * 	@param AD_Client_ID parent
	 * 	@param AD_Org_ID parent
	 * 	@param PA_ReportColumnSet_ID parent
	 * 	@param source copy source
	 * 	@return Report Column
	 */
	public static MReportColumn copy (Properties ctx, int AD_Client_ID, int AD_Org_ID, int PA_ReportColumnSet_ID, MReportColumn source, String trxName)
	{
		MReportColumn retValue = new MReportColumn (ctx, 0, trxName);
		MReportColumn.copyValues(source, retValue, AD_Client_ID, AD_Org_ID);
		//
		retValue.setPA_ReportColumnSet_ID(PA_ReportColumnSet_ID);	//	parent
		retValue.setOper_1_ID(0);
		retValue.setOper_2_ID(0);
		return retValue;
	}	//	copy

}	//	MReportColumn
