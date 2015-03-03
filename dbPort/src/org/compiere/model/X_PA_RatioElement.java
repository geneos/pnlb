/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for PA_RatioElement
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:03.984
 */
public class X_PA_RatioElement extends PO {
	/** Standard Constructor */
	public X_PA_RatioElement(Properties ctx, int PA_RatioElement_ID,
			String trxName) {
		super(ctx, PA_RatioElement_ID, trxName);
		/**
		 * if (PA_RatioElement_ID == 0) { setName (null); setPA_RatioElement_ID
		 * (0); setPA_Ratio_ID (0); setRatioElementType (null); setRatioOperand
		 * (null); // P setSeqNo (0); }
		 */
	}

	/** Load Constructor */
	public X_PA_RatioElement(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=PA_RatioElement */
	public static final String Table_Name = "PA_RatioElement";

	/** AD_Table_ID */
	public int Table_ID;

	protected KeyNamePair Model;

	/** Load Meta Data */
	protected POInfo initPO(Properties ctx) {
		POInfo info = initPO(ctx, Table_Name);
		Table_ID = info.getAD_Table_ID();
		Model = new KeyNamePair(Table_ID, Table_Name);
		return info;
	}

	protected BigDecimal accessLevel = new BigDecimal(2);

	/** AccessLevel 2 - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_PA_RatioElement[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** Account_ID AD_Reference_ID=331 */
	public static final int ACCOUNT_ID_AD_Reference_ID = 331;

	/**
	 * Set Account. Account used
	 */
	public void setAccount_ID(int Account_ID) {
		if (Account_ID <= 0)
			set_Value("Account_ID", null);
		else
			set_Value("Account_ID", new Integer(Account_ID));
	}

	/**
	 * Get Account. Account used
	 */
	public int getAccount_ID() {
		Integer ii = (Integer) get_Value("Account_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Constant Value. Constant value
	 */
	public void setConstantValue(BigDecimal ConstantValue) {
		set_Value("ConstantValue", ConstantValue);
	}

	/**
	 * Get Constant Value. Constant value
	 */
	public BigDecimal getConstantValue() {
		BigDecimal bd = (BigDecimal) get_Value("ConstantValue");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Description. Optional short description of the record
	 */
	public void setDescription(String Description) {
		if (Description != null && Description.length() > 255) {
			log.warning("Length > 255 - truncated");
			Description = Description.substring(0, 254);
		}
		set_Value("Description", Description);
	}

	/**
	 * Get Description. Optional short description of the record
	 */
	public String getDescription() {
		return (String) get_Value("Description");
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 120) {
			log.warning("Length > 120 - truncated");
			Name = Name.substring(0, 119);
		}
		set_Value("Name", Name);
	}

	/**
	 * Get Name. Alphanumeric identifier of the entity
	 */
	public String getName() {
		return (String) get_Value("Name");
	}

	/**
	 * Set Measure Calculation. Calculation method for measuring performance
	 */
	public void setPA_MeasureCalc_ID(int PA_MeasureCalc_ID) {
		if (PA_MeasureCalc_ID <= 0)
			set_Value("PA_MeasureCalc_ID", null);
		else
			set_Value("PA_MeasureCalc_ID", new Integer(PA_MeasureCalc_ID));
	}

	/**
	 * Get Measure Calculation. Calculation method for measuring performance
	 */
	public int getPA_MeasureCalc_ID() {
		Integer ii = (Integer) get_Value("PA_MeasureCalc_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Ratio Element. Performance Ratio Element
	 */
	public void setPA_RatioElement_ID(int PA_RatioElement_ID) {
		if (PA_RatioElement_ID < 1)
			throw new IllegalArgumentException(
					"PA_RatioElement_ID is mandatory.");
		set_ValueNoCheck("PA_RatioElement_ID", new Integer(PA_RatioElement_ID));
	}

	/**
	 * Get Ratio Element. Performance Ratio Element
	 */
	public int getPA_RatioElement_ID() {
		Integer ii = (Integer) get_Value("PA_RatioElement_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** PA_RatioUsed_ID AD_Reference_ID=371 */
	public static final int PA_RATIOUSED_ID_AD_Reference_ID = 371;

	/**
	 * Set Ratio Used. Performace Ratio Used
	 */
	public void setPA_RatioUsed_ID(int PA_RatioUsed_ID) {
		if (PA_RatioUsed_ID <= 0)
			set_Value("PA_RatioUsed_ID", null);
		else
			set_Value("PA_RatioUsed_ID", new Integer(PA_RatioUsed_ID));
	}

	/**
	 * Get Ratio Used. Performace Ratio Used
	 */
	public int getPA_RatioUsed_ID() {
		Integer ii = (Integer) get_Value("PA_RatioUsed_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Ratio. Performace Ratio
	 */
	public void setPA_Ratio_ID(int PA_Ratio_ID) {
		if (PA_Ratio_ID < 1)
			throw new IllegalArgumentException("PA_Ratio_ID is mandatory.");
		set_ValueNoCheck("PA_Ratio_ID", new Integer(PA_Ratio_ID));
	}

	/**
	 * Get Ratio. Performace Ratio
	 */
	public int getPA_Ratio_ID() {
		Integer ii = (Integer) get_Value("PA_Ratio_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** PostingType AD_Reference_ID=125 */
	public static final int POSTINGTYPE_AD_Reference_ID = 125;

	/** Actual = A */
	public static final String POSTINGTYPE_Actual = "A";

	/** Budget = B */
	public static final String POSTINGTYPE_Budget = "B";

	/** Commitment = E */
	public static final String POSTINGTYPE_Commitment = "E";

	/** Reservation = R */
	public static final String POSTINGTYPE_Reservation = "R";

	/** Statistical = S */
	public static final String POSTINGTYPE_Statistical = "S";

	/**
	 * Set PostingType. The type of posted amount for the transaction
	 */
	public void setPostingType(String PostingType) {
		if (PostingType != null && PostingType.length() > 1) {
			log.warning("Length > 1 - truncated");
			PostingType = PostingType.substring(0, 0);
		}
		set_Value("PostingType", PostingType);
	}

	/**
	 * Get PostingType. The type of posted amount for the transaction
	 */
	public String getPostingType() {
		return (String) get_Value("PostingType");
	}

	/** RatioElementType AD_Reference_ID=372 */
	public static final int RATIOELEMENTTYPE_AD_Reference_ID = 372;

	/** Account Value = A */
	public static final String RATIOELEMENTTYPE_AccountValue = "A";

	/** Constant = C */
	public static final String RATIOELEMENTTYPE_Constant = "C";

	/** Ratio = R */
	public static final String RATIOELEMENTTYPE_Ratio = "R";

	/** Calculation = X */
	public static final String RATIOELEMENTTYPE_Calculation = "X";

	/**
	 * Set Element Type. Ratio Element Type
	 */
	public void setRatioElementType(String RatioElementType) {
		if (RatioElementType == null)
			throw new IllegalArgumentException("RatioElementType is mandatory");
		if (RatioElementType.equals("A") || RatioElementType.equals("C")
				|| RatioElementType.equals("R") || RatioElementType.equals("X"))
			;
		else
			throw new IllegalArgumentException(
					"RatioElementType Invalid value - " + RatioElementType
							+ " - Reference_ID=372 - A - C - R - X");
		if (RatioElementType.length() > 1) {
			log.warning("Length > 1 - truncated");
			RatioElementType = RatioElementType.substring(0, 0);
		}
		set_Value("RatioElementType", RatioElementType);
	}

	/**
	 * Get Element Type. Ratio Element Type
	 */
	public String getRatioElementType() {
		return (String) get_Value("RatioElementType");
	}

	/** RatioOperand AD_Reference_ID=373 */
	public static final int RATIOOPERAND_AD_Reference_ID = 373;

	/** Divide = D */
	public static final String RATIOOPERAND_Divide = "D";

	/** Multiply = M */
	public static final String RATIOOPERAND_Multiply = "M";

	/** Minus = N */
	public static final String RATIOOPERAND_Minus = "N";

	/** Plus = P */
	public static final String RATIOOPERAND_Plus = "P";

	/**
	 * Set Operand. Ratio Operand
	 */
	public void setRatioOperand(String RatioOperand) {
		if (RatioOperand == null)
			throw new IllegalArgumentException("RatioOperand is mandatory");
		if (RatioOperand.equals("D") || RatioOperand.equals("M")
				|| RatioOperand.equals("N") || RatioOperand.equals("P"))
			;
		else
			throw new IllegalArgumentException("RatioOperand Invalid value - "
					+ RatioOperand + " - Reference_ID=373 - D - M - N - P");
		if (RatioOperand.length() > 1) {
			log.warning("Length > 1 - truncated");
			RatioOperand = RatioOperand.substring(0, 0);
		}
		set_Value("RatioOperand", RatioOperand);
	}

	/**
	 * Get Operand. Ratio Operand
	 */
	public String getRatioOperand() {
		return (String) get_Value("RatioOperand");
	}

	/**
	 * Set Sequence. Method of ordering records; lowest number comes first
	 */
	public void setSeqNo(int SeqNo) {
		set_Value("SeqNo", new Integer(SeqNo));
	}

	/**
	 * Get Sequence. Method of ordering records; lowest number comes first
	 */
	public int getSeqNo() {
		Integer ii = (Integer) get_Value("SeqNo");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
	}
}
