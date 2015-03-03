/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_PrintLabelLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.89
 */
public class X_AD_PrintLabelLine extends PO {
	/** Standard Constructor */
	public X_AD_PrintLabelLine(Properties ctx, int AD_PrintLabelLine_ID,
			String trxName) {
		super(ctx, AD_PrintLabelLine_ID, trxName);
		/**
		 * if (AD_PrintLabelLine_ID == 0) { setAD_LabelPrinterFunction_ID (0);
		 * setAD_PrintLabelLine_ID (0); setAD_PrintLabel_ID (0);
		 * setLabelFormatType (null); // F setName (null); setSeqNo (0);
		 * setXPosition (0); setYPosition (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_PrintLabelLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_PrintLabelLine */
	public static final String Table_Name = "AD_PrintLabelLine";

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
		StringBuffer sb = new StringBuffer("X_AD_PrintLabelLine[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Column. Column in the table
	 */
	public void setAD_Column_ID(int AD_Column_ID) {
		if (AD_Column_ID <= 0)
			set_Value("AD_Column_ID", null);
		else
			set_Value("AD_Column_ID", new Integer(AD_Column_ID));
	}

	/**
	 * Get Column. Column in the table
	 */
	public int getAD_Column_ID() {
		Integer ii = (Integer) get_Value("AD_Column_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Label printer Function. Function of Label Printer
	 */
	public void setAD_LabelPrinterFunction_ID(int AD_LabelPrinterFunction_ID) {
		if (AD_LabelPrinterFunction_ID < 1)
			throw new IllegalArgumentException(
					"AD_LabelPrinterFunction_ID is mandatory.");
		set_Value("AD_LabelPrinterFunction_ID", new Integer(
				AD_LabelPrinterFunction_ID));
	}

	/**
	 * Get Label printer Function. Function of Label Printer
	 */
	public int getAD_LabelPrinterFunction_ID() {
		Integer ii = (Integer) get_Value("AD_LabelPrinterFunction_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Print Label Line. Print Label Line Format
	 */
	public void setAD_PrintLabelLine_ID(int AD_PrintLabelLine_ID) {
		if (AD_PrintLabelLine_ID < 1)
			throw new IllegalArgumentException(
					"AD_PrintLabelLine_ID is mandatory.");
		set_ValueNoCheck("AD_PrintLabelLine_ID", new Integer(
				AD_PrintLabelLine_ID));
	}

	/**
	 * Get Print Label Line. Print Label Line Format
	 */
	public int getAD_PrintLabelLine_ID() {
		Integer ii = (Integer) get_Value("AD_PrintLabelLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Print Label. Label Format to print
	 */
	public void setAD_PrintLabel_ID(int AD_PrintLabel_ID) {
		if (AD_PrintLabel_ID < 1)
			throw new IllegalArgumentException("AD_PrintLabel_ID is mandatory.");
		set_ValueNoCheck("AD_PrintLabel_ID", new Integer(AD_PrintLabel_ID));
	}

	/**
	 * Get Print Label. Label Format to print
	 */
	public int getAD_PrintLabel_ID() {
		Integer ii = (Integer) get_Value("AD_PrintLabel_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** LabelFormatType AD_Reference_ID=280 */
	public static final int LABELFORMATTYPE_AD_Reference_ID = 280;

	/** Field = F */
	public static final String LABELFORMATTYPE_Field = "F";

	/** Text = T */
	public static final String LABELFORMATTYPE_Text = "T";

	/**
	 * Set Label Format Type. Label Format Type
	 */
	public void setLabelFormatType(String LabelFormatType) {
		if (LabelFormatType == null)
			throw new IllegalArgumentException("LabelFormatType is mandatory");
		if (LabelFormatType.equals("F") || LabelFormatType.equals("T"))
			;
		else
			throw new IllegalArgumentException(
					"LabelFormatType Invalid value - " + LabelFormatType
							+ " - Reference_ID=280 - F - T");
		if (LabelFormatType.length() > 1) {
			log.warning("Length > 1 - truncated");
			LabelFormatType = LabelFormatType.substring(0, 0);
		}
		set_Value("LabelFormatType", LabelFormatType);
	}

	/**
	 * Get Label Format Type. Label Format Type
	 */
	public String getLabelFormatType() {
		return (String) get_Value("LabelFormatType");
	}

	/**
	 * Set Name. Alphanumeric identifier of the entity
	 */
	public void setName(String Name) {
		if (Name == null)
			throw new IllegalArgumentException("Name is mandatory.");
		if (Name.length() > 60) {
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

	/**
	 * Set Print Text. The label text to be printed on a document or
	 * correspondence.
	 */
	public void setPrintName(String PrintName) {
		if (PrintName != null && PrintName.length() > 60) {
			log.warning("Length > 60 - truncated");
			PrintName = PrintName.substring(0, 59);
		}
		set_Value("PrintName", PrintName);
	}

	/**
	 * Get Print Text. The label text to be printed on a document or
	 * correspondence.
	 */
	public String getPrintName() {
		return (String) get_Value("PrintName");
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

	/**
	 * Set X Position. Absolute X (horizontal) position in 1/72 of an inch
	 */
	public void setXPosition(int XPosition) {
		set_Value("XPosition", new Integer(XPosition));
	}

	/**
	 * Get X Position. Absolute X (horizontal) position in 1/72 of an inch
	 */
	public int getXPosition() {
		Integer ii = (Integer) get_Value("XPosition");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Y Position. Absolute Y (vertical) position in 1/72 of an inch
	 */
	public void setYPosition(int YPosition) {
		set_Value("YPosition", new Integer(YPosition));
	}

	/**
	 * Get Y Position. Absolute Y (vertical) position in 1/72 of an inch
	 */
	public int getYPosition() {
		Integer ii = (Integer) get_Value("YPosition");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
