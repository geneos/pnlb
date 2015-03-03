/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_PrintTableFormat
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:56.906
 */
public class X_AD_PrintTableFormat extends PO {
	/** Standard Constructor */
	public X_AD_PrintTableFormat(Properties ctx, int AD_PrintTableFormat_ID,
			String trxName) {
		super(ctx, AD_PrintTableFormat_ID, trxName);
		/**
		 * if (AD_PrintTableFormat_ID == 0) { setAD_PrintTableFormat_ID (0);
		 * setIsDefault (false); setIsPaintBoundaryLines (false);
		 * setIsPaintHLines (false); setIsPaintHeaderLines (true); // Y
		 * setIsPaintVLines (false); setIsPrintFunctionSymbols (false); setName
		 * (null); }
		 */
	}

	/** Load Constructor */
	public X_AD_PrintTableFormat(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_PrintTableFormat */
	public static final String Table_Name = "AD_PrintTableFormat";

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
		StringBuffer sb = new StringBuffer("X_AD_PrintTableFormat[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Print Table Format. Table Format in Reports
	 */
	public void setAD_PrintTableFormat_ID(int AD_PrintTableFormat_ID) {
		if (AD_PrintTableFormat_ID < 1)
			throw new IllegalArgumentException(
					"AD_PrintTableFormat_ID is mandatory.");
		set_ValueNoCheck("AD_PrintTableFormat_ID", new Integer(
				AD_PrintTableFormat_ID));
	}

	/**
	 * Get Print Table Format. Table Format in Reports
	 */
	public int getAD_PrintTableFormat_ID() {
		Integer ii = (Integer) get_Value("AD_PrintTableFormat_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Footer Center. Content of the center portion of the footer.
	 */
	public void setFooterCenter(String FooterCenter) {
		if (FooterCenter != null && FooterCenter.length() > 255) {
			log.warning("Length > 255 - truncated");
			FooterCenter = FooterCenter.substring(0, 254);
		}
		set_Value("FooterCenter", FooterCenter);
	}

	/**
	 * Get Footer Center. Content of the center portion of the footer.
	 */
	public String getFooterCenter() {
		return (String) get_Value("FooterCenter");
	}

	/**
	 * Set Footer Left. Content of the left portion of the footer.
	 */
	public void setFooterLeft(String FooterLeft) {
		if (FooterLeft != null && FooterLeft.length() > 255) {
			log.warning("Length > 255 - truncated");
			FooterLeft = FooterLeft.substring(0, 254);
		}
		set_Value("FooterLeft", FooterLeft);
	}

	/**
	 * Get Footer Left. Content of the left portion of the footer.
	 */
	public String getFooterLeft() {
		return (String) get_Value("FooterLeft");
	}

	/**
	 * Set Footer Right. Content of the right portion of the footer.
	 */
	public void setFooterRight(String FooterRight) {
		if (FooterRight != null && FooterRight.length() > 255) {
			log.warning("Length > 255 - truncated");
			FooterRight = FooterRight.substring(0, 254);
		}
		set_Value("FooterRight", FooterRight);
	}

	/**
	 * Get Footer Right. Content of the right portion of the footer.
	 */
	public String getFooterRight() {
		return (String) get_Value("FooterRight");
	}

	/** FunctBG_PrintColor_ID AD_Reference_ID=266 */
	public static final int FUNCTBG_PRINTCOLOR_ID_AD_Reference_ID = 266;

	/**
	 * Set Function BG Color. Function Background Color
	 */
	public void setFunctBG_PrintColor_ID(int FunctBG_PrintColor_ID) {
		if (FunctBG_PrintColor_ID <= 0)
			set_Value("FunctBG_PrintColor_ID", null);
		else
			set_Value("FunctBG_PrintColor_ID", new Integer(
					FunctBG_PrintColor_ID));
	}

	/**
	 * Get Function BG Color. Function Background Color
	 */
	public int getFunctBG_PrintColor_ID() {
		Integer ii = (Integer) get_Value("FunctBG_PrintColor_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** FunctFG_PrintColor_ID AD_Reference_ID=266 */
	public static final int FUNCTFG_PRINTCOLOR_ID_AD_Reference_ID = 266;

	/**
	 * Set Function Color. Function Foreground Color
	 */
	public void setFunctFG_PrintColor_ID(int FunctFG_PrintColor_ID) {
		if (FunctFG_PrintColor_ID <= 0)
			set_Value("FunctFG_PrintColor_ID", null);
		else
			set_Value("FunctFG_PrintColor_ID", new Integer(
					FunctFG_PrintColor_ID));
	}

	/**
	 * Get Function Color. Function Foreground Color
	 */
	public int getFunctFG_PrintColor_ID() {
		Integer ii = (Integer) get_Value("FunctFG_PrintColor_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Funct_PrintFont_ID AD_Reference_ID=267 */
	public static final int FUNCT_PRINTFONT_ID_AD_Reference_ID = 267;

	/**
	 * Set Function Font. Function row Font
	 */
	public void setFunct_PrintFont_ID(int Funct_PrintFont_ID) {
		if (Funct_PrintFont_ID <= 0)
			set_Value("Funct_PrintFont_ID", null);
		else
			set_Value("Funct_PrintFont_ID", new Integer(Funct_PrintFont_ID));
	}

	/**
	 * Get Function Font. Function row Font
	 */
	public int getFunct_PrintFont_ID() {
		Integer ii = (Integer) get_Value("Funct_PrintFont_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** HdrLine_PrintColor_ID AD_Reference_ID=266 */
	public static final int HDRLINE_PRINTCOLOR_ID_AD_Reference_ID = 266;

	/**
	 * Set Header Line Color. Table header row line color
	 */
	public void setHdrLine_PrintColor_ID(int HdrLine_PrintColor_ID) {
		if (HdrLine_PrintColor_ID <= 0)
			set_Value("HdrLine_PrintColor_ID", null);
		else
			set_Value("HdrLine_PrintColor_ID", new Integer(
					HdrLine_PrintColor_ID));
	}

	/**
	 * Get Header Line Color. Table header row line color
	 */
	public int getHdrLine_PrintColor_ID() {
		Integer ii = (Integer) get_Value("HdrLine_PrintColor_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Header Stroke. Width of the Header Line Stroke
	 */
	public void setHdrStroke(BigDecimal HdrStroke) {
		set_Value("HdrStroke", HdrStroke);
	}

	/**
	 * Get Header Stroke. Width of the Header Line Stroke
	 */
	public BigDecimal getHdrStroke() {
		BigDecimal bd = (BigDecimal) get_Value("HdrStroke");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** HdrStrokeType AD_Reference_ID=312 */
	public static final int HDRSTROKETYPE_AD_Reference_ID = 312;

	/** Dash-Dotted Line = 2 */
	public static final String HDRSTROKETYPE_Dash_DottedLine = "2";

	/** Dashed Line = D */
	public static final String HDRSTROKETYPE_DashedLine = "D";

	/** Solid Line = S */
	public static final String HDRSTROKETYPE_SolidLine = "S";

	/** Dotted Line = d */
	public static final String HDRSTROKETYPE_DottedLine = "d";

	/**
	 * Set Header Stroke Type. Type of the Header Line Stroke
	 */
	public void setHdrStrokeType(String HdrStrokeType) {
		if (HdrStrokeType != null && HdrStrokeType.length() > 1) {
			log.warning("Length > 1 - truncated");
			HdrStrokeType = HdrStrokeType.substring(0, 0);
		}
		set_Value("HdrStrokeType", HdrStrokeType);
	}

	/**
	 * Get Header Stroke Type. Type of the Header Line Stroke
	 */
	public String getHdrStrokeType() {
		return (String) get_Value("HdrStrokeType");
	}

	/** HdrTextBG_PrintColor_ID AD_Reference_ID=266 */
	public static final int HDRTEXTBG_PRINTCOLOR_ID_AD_Reference_ID = 266;

	/**
	 * Set Header Row BG Color. Background color of header row
	 */
	public void setHdrTextBG_PrintColor_ID(int HdrTextBG_PrintColor_ID) {
		if (HdrTextBG_PrintColor_ID <= 0)
			set_Value("HdrTextBG_PrintColor_ID", null);
		else
			set_Value("HdrTextBG_PrintColor_ID", new Integer(
					HdrTextBG_PrintColor_ID));
	}

	/**
	 * Get Header Row BG Color. Background color of header row
	 */
	public int getHdrTextBG_PrintColor_ID() {
		Integer ii = (Integer) get_Value("HdrTextBG_PrintColor_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** HdrTextFG_PrintColor_ID AD_Reference_ID=266 */
	public static final int HDRTEXTFG_PRINTCOLOR_ID_AD_Reference_ID = 266;

	/**
	 * Set Header Row Color. Foreground color if the table header row
	 */
	public void setHdrTextFG_PrintColor_ID(int HdrTextFG_PrintColor_ID) {
		if (HdrTextFG_PrintColor_ID <= 0)
			set_Value("HdrTextFG_PrintColor_ID", null);
		else
			set_Value("HdrTextFG_PrintColor_ID", new Integer(
					HdrTextFG_PrintColor_ID));
	}

	/**
	 * Get Header Row Color. Foreground color if the table header row
	 */
	public int getHdrTextFG_PrintColor_ID() {
		Integer ii = (Integer) get_Value("HdrTextFG_PrintColor_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Hdr_PrintFont_ID AD_Reference_ID=267 */
	public static final int HDR_PRINTFONT_ID_AD_Reference_ID = 267;

	/**
	 * Set Header Row Font. Header row Font
	 */
	public void setHdr_PrintFont_ID(int Hdr_PrintFont_ID) {
		if (Hdr_PrintFont_ID <= 0)
			set_Value("Hdr_PrintFont_ID", null);
		else
			set_Value("Hdr_PrintFont_ID", new Integer(Hdr_PrintFont_ID));
	}

	/**
	 * Get Header Row Font. Header row Font
	 */
	public int getHdr_PrintFont_ID() {
		Integer ii = (Integer) get_Value("Hdr_PrintFont_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Header Center. Content of the center portion of the header.
	 */
	public void setHeaderCenter(String HeaderCenter) {
		if (HeaderCenter != null && HeaderCenter.length() > 255) {
			log.warning("Length > 255 - truncated");
			HeaderCenter = HeaderCenter.substring(0, 254);
		}
		set_Value("HeaderCenter", HeaderCenter);
	}

	/**
	 * Get Header Center. Content of the center portion of the header.
	 */
	public String getHeaderCenter() {
		return (String) get_Value("HeaderCenter");
	}

	/**
	 * Set Header Left. Content of the left portion of the header.
	 */
	public void setHeaderLeft(String HeaderLeft) {
		if (HeaderLeft != null && HeaderLeft.length() > 255) {
			log.warning("Length > 255 - truncated");
			HeaderLeft = HeaderLeft.substring(0, 254);
		}
		set_Value("HeaderLeft", HeaderLeft);
	}

	/**
	 * Get Header Left. Content of the left portion of the header.
	 */
	public String getHeaderLeft() {
		return (String) get_Value("HeaderLeft");
	}

	/**
	 * Set Header Right. Content of the right portion of the header.
	 */
	public void setHeaderRight(String HeaderRight) {
		if (HeaderRight != null && HeaderRight.length() > 255) {
			log.warning("Length > 255 - truncated");
			HeaderRight = HeaderRight.substring(0, 254);
		}
		set_Value("HeaderRight", HeaderRight);
	}

	/**
	 * Get Header Right. Content of the right portion of the header.
	 */
	public String getHeaderRight() {
		return (String) get_Value("HeaderRight");
	}

	/**
	 * Set Image attached. The image to be printed is attached to the record
	 */
	public void setImageIsAttached(boolean ImageIsAttached) {
		set_Value("ImageIsAttached", new Boolean(ImageIsAttached));
	}

	/**
	 * Get Image attached. The image to be printed is attached to the record
	 */
	public boolean isImageIsAttached() {
		Object oo = get_Value("ImageIsAttached");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Image URL. URL of image
	 */
	public void setImageURL(String ImageURL) {
		if (ImageURL != null && ImageURL.length() > 120) {
			log.warning("Length > 120 - truncated");
			ImageURL = ImageURL.substring(0, 119);
		}
		set_Value("ImageURL", ImageURL);
	}

	/**
	 * Get Image URL. URL of image
	 */
	public String getImageURL() {
		return (String) get_Value("ImageURL");
	}

	/**
	 * Set Default. Default value
	 */
	public void setIsDefault(boolean IsDefault) {
		set_Value("IsDefault", new Boolean(IsDefault));
	}

	/**
	 * Get Default. Default value
	 */
	public boolean isDefault() {
		Object oo = get_Value("IsDefault");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Paint Boundary Lines. Paint table boundary lines
	 */
	public void setIsPaintBoundaryLines(boolean IsPaintBoundaryLines) {
		set_Value("IsPaintBoundaryLines", new Boolean(IsPaintBoundaryLines));
	}

	/**
	 * Get Paint Boundary Lines. Paint table boundary lines
	 */
	public boolean isPaintBoundaryLines() {
		Object oo = get_Value("IsPaintBoundaryLines");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Paint Horizontal Lines. Paint horizontal lines
	 */
	public void setIsPaintHLines(boolean IsPaintHLines) {
		set_Value("IsPaintHLines", new Boolean(IsPaintHLines));
	}

	/**
	 * Get Paint Horizontal Lines. Paint horizontal lines
	 */
	public boolean isPaintHLines() {
		Object oo = get_Value("IsPaintHLines");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Paint Header Lines. Paint Lines over/under the Header Line
	 */
	public void setIsPaintHeaderLines(boolean IsPaintHeaderLines) {
		set_Value("IsPaintHeaderLines", new Boolean(IsPaintHeaderLines));
	}

	/**
	 * Get Paint Header Lines. Paint Lines over/under the Header Line
	 */
	public boolean isPaintHeaderLines() {
		Object oo = get_Value("IsPaintHeaderLines");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Paint Vertical Lines. Paint vertical lines
	 */
	public void setIsPaintVLines(boolean IsPaintVLines) {
		set_Value("IsPaintVLines", new Boolean(IsPaintVLines));
	}

	/**
	 * Get Paint Vertical Lines. Paint vertical lines
	 */
	public boolean isPaintVLines() {
		Object oo = get_Value("IsPaintVLines");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Print Function Symbols. Print Symbols for Functions (Sum, Average,
	 * Count)
	 */
	public void setIsPrintFunctionSymbols(boolean IsPrintFunctionSymbols) {
		set_Value("IsPrintFunctionSymbols", new Boolean(IsPrintFunctionSymbols));
	}

	/**
	 * Get Print Function Symbols. Print Symbols for Functions (Sum, Average,
	 * Count)
	 */
	public boolean isPrintFunctionSymbols() {
		Object oo = get_Value("IsPrintFunctionSymbols");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Line Stroke. Width of the Line Stroke
	 */
	public void setLineStroke(BigDecimal LineStroke) {
		set_Value("LineStroke", LineStroke);
	}

	/**
	 * Get Line Stroke. Width of the Line Stroke
	 */
	public BigDecimal getLineStroke() {
		BigDecimal bd = (BigDecimal) get_Value("LineStroke");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** LineStrokeType AD_Reference_ID=312 */
	public static final int LINESTROKETYPE_AD_Reference_ID = 312;

	/** Dash-Dotted Line = 2 */
	public static final String LINESTROKETYPE_Dash_DottedLine = "2";

	/** Dashed Line = D */
	public static final String LINESTROKETYPE_DashedLine = "D";

	/** Solid Line = S */
	public static final String LINESTROKETYPE_SolidLine = "S";

	/** Dotted Line = d */
	public static final String LINESTROKETYPE_DottedLine = "d";

	/**
	 * Set Line Stroke Type. Type of the Line Stroke
	 */
	public void setLineStrokeType(String LineStrokeType) {
		if (LineStrokeType != null && LineStrokeType.length() > 1) {
			log.warning("Length > 1 - truncated");
			LineStrokeType = LineStrokeType.substring(0, 0);
		}
		set_Value("LineStrokeType", LineStrokeType);
	}

	/**
	 * Get Line Stroke Type. Type of the Line Stroke
	 */
	public String getLineStrokeType() {
		return (String) get_Value("LineStrokeType");
	}

	/** Line_PrintColor_ID AD_Reference_ID=266 */
	public static final int LINE_PRINTCOLOR_ID_AD_Reference_ID = 266;

	/**
	 * Set Line Color. Table line color
	 */
	public void setLine_PrintColor_ID(int Line_PrintColor_ID) {
		if (Line_PrintColor_ID <= 0)
			set_Value("Line_PrintColor_ID", null);
		else
			set_Value("Line_PrintColor_ID", new Integer(Line_PrintColor_ID));
	}

	/**
	 * Get Line Color. Table line color
	 */
	public int getLine_PrintColor_ID() {
		Integer ii = (Integer) get_Value("Line_PrintColor_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getName());
	}
}
