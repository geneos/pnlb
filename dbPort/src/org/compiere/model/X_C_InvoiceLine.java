/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;

import org.compiere.util.*;

/**
 * Generated Model for C_InvoiceLine
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.562
 */
public class X_C_InvoiceLine extends PO {
	/** Standard Constructor */
	public X_C_InvoiceLine(Properties ctx, int C_InvoiceLine_ID, String trxName) {
		super(ctx, C_InvoiceLine_ID, trxName);
		/**
		 * if (C_InvoiceLine_ID == 0) { setC_InvoiceLine_ID (0); setC_Invoice_ID
		 * (0); setC_Tax_ID (0); setIsDescription (false); // N setIsPrinted
		 * (true); // Y setLine (0); //
		 * 
		 * @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_InvoiceLine
		 *             WHERE C_Invoice_ID=@C_Invoice_ID@ setLineNetAmt
		 *             (Env.ZERO); setM_AttributeSetInstance_ID (0);
		 *             setPriceActual (Env.ZERO); setPriceEntered (Env.ZERO);
		 *             setPriceLimit (Env.ZERO); setPriceList (Env.ZERO);
		 *             setProcessed (false); setQtyEntered (Env.ZERO); // 1
		 *             setQtyInvoiced (Env.ZERO); // 1 }
		 */
	}

	/** Load Constructor */
	public X_C_InvoiceLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_InvoiceLine */
	public static final String Table_Name = "C_InvoiceLine";

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

	protected BigDecimal accessLevel = new BigDecimal(1);

	/** AccessLevel 1 - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_C_InvoiceLine[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Asset. Asset used internally or by customers
	 */
	public void setA_Asset_ID(int A_Asset_ID) {
		if (A_Asset_ID <= 0)
			set_Value("A_Asset_ID", null);
		else
			set_Value("A_Asset_ID", new Integer(A_Asset_ID));
	}

	/**
	 * Get Asset. Asset used internally or by customers
	 */
	public int getA_Asset_ID() {
		Integer ii = (Integer) get_Value("A_Asset_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Charge. Additional document charges
	 */
	public void setC_Charge_ID(int C_Charge_ID) {
		if (C_Charge_ID <= 0)
			set_Value("C_Charge_ID", null);
		else
			set_Value("C_Charge_ID", new Integer(C_Charge_ID));
	}

	/**
	 * Get Charge. Additional document charges
	 */
	public int getC_Charge_ID() {
		Integer ii = (Integer) get_Value("C_Charge_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Invoice Line. Invoice Detail Line
	 */
	public void setC_InvoiceLine_ID(int C_InvoiceLine_ID) {
		if (C_InvoiceLine_ID < 1)
			throw new IllegalArgumentException("C_InvoiceLine_ID is mandatory.");
		set_ValueNoCheck("C_InvoiceLine_ID", new Integer(C_InvoiceLine_ID));
	}

	/**
	 * Get Invoice Line. Invoice Detail Line
	 */
	public int getC_InvoiceLine_ID() {
		Integer ii = (Integer) get_Value("C_InvoiceLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Invoice. Invoice Identifier
	 */
	public void setC_Invoice_ID(int C_Invoice_ID) {
		if (C_Invoice_ID < 1)
			throw new IllegalArgumentException("C_Invoice_ID is mandatory.");
		set_ValueNoCheck("C_Invoice_ID", new Integer(C_Invoice_ID));
	}

	/**
	 * Get Invoice. Invoice Identifier
	 */
	public int getC_Invoice_ID() {
		Integer ii = (Integer) get_Value("C_Invoice_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String.valueOf(getC_Invoice_ID()));
	}

	/**
	 * Set Sales Order Line. Sales Order Line
	 */
	public void setC_OrderLine_ID(int C_OrderLine_ID) {
		if (C_OrderLine_ID <= 0)
			set_ValueNoCheck("C_OrderLine_ID", null);
		else
			set_ValueNoCheck("C_OrderLine_ID", new Integer(C_OrderLine_ID));
	}

	/**
	 * Get Sales Order Line. Sales Order Line
	 */
	public int getC_OrderLine_ID() {
		Integer ii = (Integer) get_Value("C_OrderLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Project. Financial Project
	 */
	public void setC_Project_ID(int C_Project_ID) {
		if (C_Project_ID <= 0)
			set_Value("C_Project_ID", null);
		else
			set_Value("C_Project_ID", new Integer(C_Project_ID));
	}

	/**
	 * Get Project. Financial Project
	 */
	public int getC_Project_ID() {
		Integer ii = (Integer) get_Value("C_Project_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Tax. Tax identifier
	 */
	public void setC_Tax_ID(int C_Tax_ID) {
		if (C_Tax_ID < 1)
			throw new IllegalArgumentException("C_Tax_ID is mandatory.");
		set_Value("C_Tax_ID", new Integer(C_Tax_ID));
	}

	/**
	 * Get Tax. Tax identifier
	 */
	public int getC_Tax_ID() {
		Integer ii = (Integer) get_Value("C_Tax_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set UOM. Unit of Measure
	 */
	public void setC_UOM_ID(int C_UOM_ID) {
		if (C_UOM_ID <= 0)
			set_ValueNoCheck("C_UOM_ID", null);
		else
			set_ValueNoCheck("C_UOM_ID", new Integer(C_UOM_ID));
	}

	/**
	 * Get UOM. Unit of Measure
	 */
	public int getC_UOM_ID() {
		Integer ii = (Integer) get_Value("C_UOM_ID");
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
	 * Set Description Only. if true, the line is just description and no
	 * transaction
	 */
	public void setIsDescription(boolean IsDescription) {
		set_Value("IsDescription", new Boolean(IsDescription));
	}

	/**
	 * Get Description Only. if true, the line is just description and no
	 * transaction
	 */
	public boolean isDescription() {
		Object oo = get_Value("IsDescription");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
	 * Set Line No. Unique line for this document
	 */
	public void setLine(int Line) {
		set_Value("Line", new Integer(Line));
	}

	/**
	 * Get Line No. Unique line for this document
	 */
	public int getLine() {
		Integer ii = (Integer) get_Value("Line");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Line Amount. Line Extended Amount (Quantity * Actual Price) without
	 * Freight and Charges
	 */
	public void setLineNetAmt(BigDecimal LineNetAmt) {
		if (LineNetAmt == null)
			throw new IllegalArgumentException("LineNetAmt is mandatory.");
		set_ValueNoCheck("LineNetAmt", LineNetAmt);
	}

	/**
	 * Get Line Amount. Line Extended Amount (Quantity * Actual Price) without
	 * Freight and Charges
	 */
	public BigDecimal getLineNetAmt() {
		BigDecimal bd = (BigDecimal) get_Value("LineNetAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Line Total. Total line amount incl. Tax
	 */
	public void setLineTotalAmt(BigDecimal LineTotalAmt) {
		set_Value("LineTotalAmt", LineTotalAmt);
	}

	/**
	 * Get Line Total. Total line amount incl. Tax
	 */
	public BigDecimal getLineTotalAmt() {
		BigDecimal bd = (BigDecimal) get_Value("LineTotalAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Attribute Set Instance. Product Attribute Set Instance
	 */
	public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
		if (M_AttributeSetInstance_ID < 0)
			throw new IllegalArgumentException(
					"M_AttributeSetInstance_ID is mandatory.");
		set_Value("M_AttributeSetInstance_ID", new Integer(
				M_AttributeSetInstance_ID));
	}

	/**
	 * Get Attribute Set Instance. Product Attribute Set Instance
	 */
	public int getM_AttributeSetInstance_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSetInstance_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Shipment/Receipt Line. Line on Shipment or Receipt document
	 */
	public void setM_InOutLine_ID(int M_InOutLine_ID) {
		if (M_InOutLine_ID <= 0)
			set_ValueNoCheck("M_InOutLine_ID", null);
		else
			set_ValueNoCheck("M_InOutLine_ID", new Integer(M_InOutLine_ID));
	}

	/**
	 * Get Shipment/Receipt Line. Line on Shipment or Receipt document
	 */
	public int getM_InOutLine_ID() {
		Integer ii = (Integer) get_Value("M_InOutLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Product. Product, Service, Item
	 */
	public void setM_Product_ID(int M_Product_ID) {
		if (M_Product_ID <= 0)
			set_Value("M_Product_ID", null);
		else
			set_Value("M_Product_ID", new Integer(M_Product_ID));
	}

	/**
	 * Get Product. Product, Service, Item
	 */
	public int getM_Product_ID() {
		Integer ii = (Integer) get_Value("M_Product_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Unit Price. Actual Price
	 */
	public void setPriceActual(BigDecimal PriceActual) {
		if (PriceActual == null)
			throw new IllegalArgumentException("PriceActual is mandatory.");
		set_ValueNoCheck("PriceActual", PriceActual);
	}

	/**
	 * Get Unit Price. Actual Price
	 */
	public BigDecimal getPriceActual() {
		BigDecimal bd = (BigDecimal) get_Value("PriceActual");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Price. Price Entered - the price based on the selected/base UoM
	 */
	public void setPriceEntered(BigDecimal PriceEntered) {
		if (PriceEntered == null)
			throw new IllegalArgumentException("PriceEntered is mandatory.");
		set_Value("PriceEntered", PriceEntered);
	}

	/**
	 * Get Price. Price Entered - the price based on the selected/base UoM
	 */
	public BigDecimal getPriceEntered() {
		BigDecimal bd = (BigDecimal) get_Value("PriceEntered");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Limit Price. Lowest price for a product
	 */
	public void setPriceLimit(BigDecimal PriceLimit) {
		if (PriceLimit == null)
			throw new IllegalArgumentException("PriceLimit is mandatory.");
		set_Value("PriceLimit", PriceLimit);
	}

	/**
	 * Get Limit Price. Lowest price for a product
	 */
	public BigDecimal getPriceLimit() {
		BigDecimal bd = (BigDecimal) get_Value("PriceLimit");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set List Price. List Price
	 */
	public void setPriceList(BigDecimal PriceList) {
		if (PriceList == null)
			throw new IllegalArgumentException("PriceList is mandatory.");
		set_Value("PriceList", PriceList);
	}

	/**
	 * Get List Price. List Price
	 */
	public BigDecimal getPriceList() {
		BigDecimal bd = (BigDecimal) get_Value("PriceList");
		if (bd == null)
			return Env.ZERO;
		return bd;
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

	/**
	 * Set Quantity. The Quantity Entered is based on the selected UoM
	 */
	public void setQtyEntered(BigDecimal QtyEntered) {
		if (QtyEntered == null)
			throw new IllegalArgumentException("QtyEntered is mandatory.");
		set_Value("QtyEntered", QtyEntered);
	}

	/**
	 * Get Quantity. The Quantity Entered is based on the selected UoM
	 */
	public BigDecimal getQtyEntered() {
		BigDecimal bd = (BigDecimal) get_Value("QtyEntered");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/**
	 * Set Quantity Invoiced. Invoiced Quantity
	 */
	public void setQtyInvoiced(BigDecimal QtyInvoiced) {
		if (QtyInvoiced == null)
			throw new IllegalArgumentException("QtyInvoiced is mandatory.");
		set_Value("QtyInvoiced", QtyInvoiced);
	}

	/**
	 * Get Quantity Invoiced. Invoiced Quantity
	 */
	public BigDecimal getQtyInvoiced() {
		BigDecimal bd = (BigDecimal) get_Value("QtyInvoiced");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set Referenced Invoice Line */
	public void setRef_InvoiceLine_ID(int Ref_InvoiceLine_ID) {
		if (Ref_InvoiceLine_ID <= 0)
			set_Value("Ref_InvoiceLine_ID", null);
		else
			set_Value("Ref_InvoiceLine_ID", new Integer(Ref_InvoiceLine_ID));
	}

	/** Get Referenced Invoice Line */
	public int getRef_InvoiceLine_ID() {
		Integer ii = (Integer) get_Value("Ref_InvoiceLine_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Resource Assignment. Resource Assignment
	 */
	public void setS_ResourceAssignment_ID(int S_ResourceAssignment_ID) {
		if (S_ResourceAssignment_ID <= 0)
			set_ValueNoCheck("S_ResourceAssignment_ID", null);
		else
			set_ValueNoCheck("S_ResourceAssignment_ID", new Integer(
					S_ResourceAssignment_ID));
	}

	/**
	 * Get Resource Assignment. Resource Assignment
	 */
	public int getS_ResourceAssignment_ID() {
		Integer ii = (Integer) get_Value("S_ResourceAssignment_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Tax Amount. Tax Amount for a document
	 */
	public void setTaxAmt(BigDecimal TaxAmt) {
		set_Value("TaxAmt", TaxAmt);
	}

	/**
	 * Get Tax Amount. Tax Amount for a document
	 */
	public BigDecimal getTaxAmt() {
		BigDecimal bd = (BigDecimal) get_Value("TaxAmt");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set C_REGIM_RETEN_PERCEP_RECIB_ID */
	public void setC_REGIM_RETEN_PERCEP_RECIB_ID(
			int C_REGIM_RETEN_PERCEP_RECIB_ID) {
		if (C_REGIM_RETEN_PERCEP_RECIB_ID <= 0)
			set_ValueNoCheck("C_REGIM_RETEN_PERCEP_RECIB_ID", null);
		else
			set_ValueNoCheck("C_REGIM_RETEN_PERCEP_RECIB_ID", new Integer(
					C_REGIM_RETEN_PERCEP_RECIB_ID));
	}

	/** Get C_REGIM_RETEN_PERCEP_RECIB_ID */
	public int getC_REGIM_RETEN_PERCEP_RECIB_ID() {
		Integer ii = (Integer) get_Value("C_REGIM_RETEN_PERCEP_RECIB_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** T_CODIGOJURISDICCION_ID AD_Reference_ID=1000064 */
	public static final int CODIGO_JURISDICCION_AD_Reference_ID = 1000064;

	/** 901 = 901 */
	public static final String CODIGO_JURISDICCION_901 = "901";

	/** 902 = 902 */
	public static final String CODIGO_JURISDICCION_902 = "902";

	/** 903 = 903 */
	public static final String CODIGOJURISDICCION_903 = "903";

	/** 904 = 904 */
	public static final String CODIGO_JURISDICCION_904 = "904";

	/** 905 = 905 */
	public static final String CODIGO_JURISDICCION_905 = "905";

	/** 906 = 906 */
	public static final String CODIGO_JURISDICCION_906 = "906";

	/** 907 = 907 */
	public static final String CODIGO_JURISDICCION_907 = "907";

	/** 908 = 908 */
	public static final String CODIGO_JURISDICCION_908 = "908";

	/** 909 = 909 */
	public static final String CODIGO_JURISDICCION_909 = "909";

	/** 910 = 910 */
	public static final String CODIGO_JURISDICCION_910 = "910";

	/** 911 = 911 */
	public static final String CODIGO_JURISDICCION_911 = "911";

	/** 912 = 912 */
	public static final String CODIGO_JURISDICCION_912 = "912";

	/** 913 = 913 */
	public static final String CODIGO_JURISDICCION_913 = "913";

	/** 914 = 914 */
	public static final String CODIGO_JURISDICCION_914 = "914";

	/** 915 = 915 */
	public static final String CODIGO_JURISDICCION_915 = "915";

	/** 916 = 916 */
	public static final String CODIGO_JURISDICCION_916 = "916";

	/** 917 = 917 */
	public static final String CODIGO_JURISDICCION_917 = "917";

	/** 918 = 918 */
	public static final String CODIGO_JURISDICCION_918 = "918";

	/** 919 = 919 */
	public static final String CODIGO_JURISDICCION_919 = "919";

	/** 920 = 920 */
	public static final String CODIGO_JURISDICCION_920 = "920";

	/** 921 = 921 */
	public static final String CODIGO_JURISDICCION_921 = "921";

	/** 922 = 922 */
	public static final String CODIGO_JURISDICCION_922 = "922";

	/** 923 = 923 */
	public static final String CODIGO_JURISDICCION_923 = "923";

	/** 924 = 924 */
	public static final String CODIGO_JURISDICCION_924 = "924";

	/** Set T_CODIGOJURISDICCION_ID */
	public void set_CODIGO_JURISDICCION(String CODIGO_JURISDICCION) {
		if (CODIGO_JURISDICCION != null && CODIGO_JURISDICCION.length() > 4) {
			log.warning("Length > 4 - truncated");
			CODIGO_JURISDICCION = CODIGO_JURISDICCION.substring(0, 3);
		}
		set_Value("CODIGO_JURISDICCION", CODIGO_JURISDICCION);
	}

	/** Get CODIGOJURISDICCION_ID */
	public int get_CODIGO_JURISDICCION() {
		if (get_Value("CODIGO_JURISDICCION").getClass().getName().equals(
				"Integer")) {
			Integer i = (Integer) get_Value("CODIGO_JURISDICCION");
			if (i != null)
				return i.intValue();
			else
				return 0;
		} else {
			String i = (String) get_Value("CODIGO_JURISDICCION");
			if (i != null)
				return (new Integer(i).intValue());
			else
				return 0;
		}
	}

	public static final int YEAR_AD_Reference_ID = 1000070;

	public static final String YEAR_2002 = "2002";

	public static final String YEAR_2003 = "2003";

	public static final String YEAR_2004 = "2004";

	public static final String YEAR_2005 = "2005";

	public static final String YEAR_2006 = "2006";

	public static final String YEAR_2007 = "2007";

	public static final String YEAR_2008 = "2008";

	public static final String YEAR_2009 = "2009";

	public static final String YEAR_2010 = "2010";

	public static final String YEAR_2011 = "2011";

	public static final String YEAR_2012 = "2012";

	public static final String YEAR_2013 = "2013";

	public static final String YEAR_2014 = "2014";

	public static final String YEAR_2015 = "2015";

	public static final String YEAR_2016 = "2016";

	public static final String YEAR_2017 = "2017";

	public static final String YEAR_2018 = "2018";

	public static final String YEAR_2019 = "2019";

	public static final String YEAR_2020 = "2020";

	// Set Year
	public void setYEAR(String YEAR) {
		if (YEAR == null)
			set_ValueNoCheck("Year", null);
		else
			set_ValueNoCheck("Year", new Integer(YEAR));
	}

	// Get Year
	public int getYEAR() {
		String i = (String) get_Value("Year");
		if (i != null)
			return (new Integer(i).intValue());
		else
			return 0;
	}

	public static final int MONTH_AD_Reference_ID = 1000069;

	public static final String MONTH_1 = "Enero";

	public static final String MONTH_2 = "Febrero";

	public static final String MONTH_3 = "Marzo";

	public static final String MONTH_4 = "Abril";

	public static final String MONTH_5 = "Mayo";

	public static final String MONTH_6 = "Junio";

	public static final String MONTH_7 = "Julio";

	public static final String MONTH_8 = "Agosto";

	public static final String MONTH_9 = "Septiembre";

	public static final String MONTH_90 = "Octubre";

	public static final String MONTH_91 = "Noviembre";

	public static final String MONTH_92 = "Diciembre";

	// Set MONTH
	public void setMONTH(String MONTH) {
		if (MONTH == null)
			set_ValueNoCheck("MONTH", null);
		else
			set_ValueNoCheck("MONTH", new Integer(MONTH));
	}

	// Get MONTH
	public int getMONTH() {
		String i = (String) get_Value("MONTH");
		if (i != null)
			return (new Integer(i).intValue());
		else
			return 0;
	}

	/**
	 * Set ESPERCEPADUANERA. Si es true, habilita el combo codigo de
	 * jurisdiccion
	 */
	public void setESPERCEPADUANERA(boolean ESPERCEPADUANERA) {
		set_Value("ESPERCEPADUANERA", new Boolean(ESPERCEPADUANERA));
	}

	/**
	 * Get ESPERCEPADUANERA.
	 */
	public boolean getESPERCEPADUANERA() {
		Object oo = get_Value("ESPERCEPADUANERA");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set ESRETBANCARIA. Si es true, habilita el combo codigo de
	 * jurisdiccion,mes y a�o
	 */
	public void setESRETBANCARIA(boolean ESRETBANCARIA) {
		set_Value("ESRETBANCARIA", new Boolean(ESRETBANCARIA));
	}

	/**
	 * Get ESRETBANCARIA.
	 */
	public boolean getESRETBANCARIA() {
		Object oo = get_Value("ESRETBANCARIA");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set ISTAX. Si es true, habilita el combo codigo de regimen
	 */
	public void setISTAX(boolean ISTAX) {
		set_Value("ISTAX", new Boolean(ISTAX));
	}

	/**
	 * Get ISTAX.
	 */
	public boolean getISTAX() {
		Object oo = get_Value("ISTAX");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Discount %. Discount in percent
	 */
	public void setDiscount(BigDecimal Discount) {
		set_Value("Discount", Discount);
	}

	/**
	 * Get Discount %. Discount in percent
	 */
	public BigDecimal getDiscount() {
		BigDecimal bd = (BigDecimal) get_Value("Discount");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}
}
