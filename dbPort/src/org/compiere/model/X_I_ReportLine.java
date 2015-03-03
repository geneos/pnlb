/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for I_ReportLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:01.562
 */
public class X_I_ReportLine extends PO {
	/** Standard Constructor */
	public X_I_ReportLine(Properties ctx, int I_ReportLine_ID, String trxName) {
		super(ctx, I_ReportLine_ID, trxName);
		/**
		 * if (I_ReportLine_ID == 0) { setI_IsImported (false);
		 * setI_ReportLine_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_I_ReportLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=I_ReportLine */
	public static final String Table_Name = "I_ReportLine";

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

	protected BigDecimal accessLevel = new BigDecimal(6);

	/** AccessLevel 6 - System - Client */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_I_ReportLine[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AmountType AD_Reference_ID=235 */
	public static final int AMOUNTTYPE_AD_Reference_ID = 235;

	/** Period Balance = BP */
	public static final String AMOUNTTYPE_PeriodBalance = "BP";

	/** Total Balance = BT */
	public static final String AMOUNTTYPE_TotalBalance = "BT";

	/** Year Balance = BY */
	public static final String AMOUNTTYPE_YearBalance = "BY";

	/** Period Credit Only = CP */
	public static final String AMOUNTTYPE_PeriodCreditOnly = "CP";

	/** Total Credit Only = CT */
	public static final String AMOUNTTYPE_TotalCreditOnly = "CT";

	/** Year Credit Only = CY */
	public static final String AMOUNTTYPE_YearCreditOnly = "CY";

	/** Period Debit Only = DP */
	public static final String AMOUNTTYPE_PeriodDebitOnly = "DP";

	/** Total Debit Only = DT */
	public static final String AMOUNTTYPE_TotalDebitOnly = "DT";

	/** Year Debit Only = DY */
	public static final String AMOUNTTYPE_YearDebitOnly = "DY";

	/** Period Quantity = QP */
	public static final String AMOUNTTYPE_PeriodQuantity = "QP";

	/** Total Quantity = QT */
	public static final String AMOUNTTYPE_TotalQuantity = "QT";

	/** Year Quantity = QY */
	public static final String AMOUNTTYPE_YearQuantity = "QY";

	/**
	 * Set Amount Type. Type of amount to report
	 */
	public void setAmountType(String AmountType) {
		if (AmountType != null && AmountType.length() > 2) {
			log.warning("Length > 2 - truncated");
			AmountType = AmountType.substring(0, 1);
		}
		set_Value("AmountType", AmountType);
	}

	/**
	 * Get Amount Type. Type of amount to report
	 */
	public String getAmountType() {
		return (String) get_Value("AmountType");
	}

	/**
	 * Set Account Element. Account Element
	 */
	public void setC_ElementValue_ID(int C_ElementValue_ID) {
		if (C_ElementValue_ID <= 0)
			set_Value("C_ElementValue_ID", null);
		else
			set_Value("C_ElementValue_ID", new Integer(C_ElementValue_ID));
	}

	/**
	 * Get Account Element. Account Element
	 */
	public int getC_ElementValue_ID() {
		Integer ii = (Integer) get_Value("C_ElementValue_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** CalculationType AD_Reference_ID=236 */
	public static final int CALCULATIONTYPE_AD_Reference_ID = 236;

	/** Add (Op1+Op2) = A */
	public static final String CALCULATIONTYPE_AddOp1PlusOp2 = "A";

	/** Percentage (Op1 of Op2) = P */
	public static final String CALCULATIONTYPE_PercentageOp1OfOp2 = "P";

	/** Add Range (Op1 to Op2) = R */
	public static final String CALCULATIONTYPE_AddRangeOp1ToOp2 = "R";

	/** Subtract (Op1-Op2) = S */
	public static final String CALCULATIONTYPE_SubtractOp1_Op2 = "S";

	/** Set Calculation */
	public void setCalculationType(String CalculationType) {
		if (CalculationType != null && CalculationType.length() > 1) {
			log.warning("Length > 1 - truncated");
			CalculationType = CalculationType.substring(0, 0);
		}
		set_Value("CalculationType", CalculationType);
	}

	/** Get Calculation */
	public String getCalculationType() {
		return (String) get_Value("CalculationType");
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
	 * Set Element Key. Key of the element
	 */
	public void setElementValue(String ElementValue) {
		if (ElementValue != null && ElementValue.length() > 40) {
			log.warning("Length > 40 - truncated");
			ElementValue = ElementValue.substring(0, 39);
		}
		set_Value("ElementValue", ElementValue);
	}

	/**
	 * Get Element Key. Key of the element
	 */
	public String getElementValue() {
		return (String) get_Value("ElementValue");
	}

	/**
	 * Set Import Error Message. Messages generated from import process
	 */
	public void setI_ErrorMsg(String I_ErrorMsg) {
		if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			I_ErrorMsg = I_ErrorMsg.substring(0, 1999);
		}
		set_Value("I_ErrorMsg", I_ErrorMsg);
	}

	/**
	 * Get Import Error Message. Messages generated from import process
	 */
	public String getI_ErrorMsg() {
		return (String) get_Value("I_ErrorMsg");
	}

	/**
	 * Set Imported. Has this import been processed
	 */
	public void setI_IsImported(boolean I_IsImported) {
		set_Value("I_IsImported", new Boolean(I_IsImported));
	}

	/**
	 * Get Imported. Has this import been processed
	 */
	public boolean isI_IsImported() {
		Object oo = get_Value("I_IsImported");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Import Report Line Set. Import Report Line Set values
	 */
	public void setI_ReportLine_ID(int I_ReportLine_ID) {
		if (I_ReportLine_ID < 1)
			throw new IllegalArgumentException("I_ReportLine_ID is mandatory.");
		set_ValueNoCheck("I_ReportLine_ID", new Integer(I_ReportLine_ID));
	}

	/**
	 * Get Import Report Line Set. Import Report Line Set values
	 */
	public int getI_ReportLine_ID() {
		Integer ii = (Integer) get_Value("I_ReportLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Printed. Indicates if this document / line is printed
	 */
	public void setIsPrinted(boolean IsPrinted) {
		set_Value("IsPrinted", new Boolean(IsPrinted));
	}

	/**
	 * Get Printed. Indicates if this document / line is printed
	 */
	public boolean isPrinted() {
		Object oo = get_Value("IsPrinted");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Summary Level. This is a summary entity
	 */
	public void setIsSummary(boolean IsSummary) {
		set_Value("IsSummary", new Boolean(IsSummary));
	}

	/**
	 * Get Summary Level. This is a summary entity
	 */
	public boolean isSummary() {
		Object oo = get_Value("IsSummary");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** LineType AD_Reference_ID=241 */
	public static final int LINETYPE_AD_Reference_ID = 241;

	/** Calculation = C */
	public static final String LINETYPE_Calculation = "C";

	/** Segment Value = S */
	public static final String LINETYPE_SegmentValue = "S";

	/** Set Line Type */
	public void setLineType(String LineType) {
		if (LineType != null && LineType.length() > 1) {
			log.warning("Length > 1 - truncated");
			LineType = LineType.substring(0, 0);
		}
		set_Value("LineType", LineType);
	}

	/** Get Line Type */
	public String getLineType() {
		return (String) get_Value("LineType");
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name != null && Name.length() > 60) {
			log.warning("Length > 60 - truncated");
			Name = Name.substring(0, 59);
		}
		set_Value("Name", Name);
	}

	/**
	 * Get Name. Alphanumeric identifier of the entity
	 */
	public String getName() {
		return (String) get_Value("Name");
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
	}

	/** Set Report Line Set */
	public void setPA_ReportLineSet_ID(int PA_ReportLineSet_ID) {
		if (PA_ReportLineSet_ID <= 0)
			set_Value("PA_ReportLineSet_ID", null);
		else
			set_Value("PA_ReportLineSet_ID", new Integer(PA_ReportLineSet_ID));
	}

	/** Get Report Line Set */
	public int getPA_ReportLineSet_ID() {
		Integer ii = (Integer) get_Value("PA_ReportLineSet_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Report Line */
	public void setPA_ReportLine_ID(int PA_ReportLine_ID) {
		if (PA_ReportLine_ID <= 0)
			set_Value("PA_ReportLine_ID", null);
		else
			set_Value("PA_ReportLine_ID", new Integer(PA_ReportLine_ID));
	}

	/** Get Report Line */
	public int getPA_ReportLine_ID() {
		Integer ii = (Integer) get_Value("PA_ReportLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Report Source. Restriction of what will be shown in Report Line
	 */
	public void setPA_ReportSource_ID(int PA_ReportSource_ID) {
		if (PA_ReportSource_ID <= 0)
			set_Value("PA_ReportSource_ID", null);
		else
			set_Value("PA_ReportSource_ID", new Integer(PA_ReportSource_ID));
	}

	/**
	 * Get Report Source. Restriction of what will be shown in Report Line
	 */
	public int getPA_ReportSource_ID() {
		Integer ii = (Integer) get_Value("PA_ReportSource_ID");
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

	/**
	 * Set Processed. The document has been processed
	 */
	public void setProcessed(boolean Processed) {
		set_Value("Processed", new Boolean(Processed));
	}

	/**
	 * Get Processed. The document has been processed
	 */
	public boolean isProcessed() {
		Object oo = get_Value("Processed");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now */
	public void setProcessing(boolean Processing) {
		set_Value("Processing", new Boolean(Processing));
	}

	/** Get Process Now */
	public boolean isProcessing() {
		Object oo = get_Value("Processing");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Report Line Set Name. Name of the Report Line Set
	 */
	public void setReportLineSetName(String ReportLineSetName) {
		if (ReportLineSetName != null && ReportLineSetName.length() > 60) {
			log.warning("Length > 60 - truncated");
			ReportLineSetName = ReportLineSetName.substring(0, 59);
		}
		set_Value("ReportLineSetName", ReportLineSetName);
	}

	/**
	 * Get Report Line Set Name. Name of the Report Line Set
	 */
	public String getReportLineSetName() {
		return (String) get_Value("ReportLineSetName");
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
}
