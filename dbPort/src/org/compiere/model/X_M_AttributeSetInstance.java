/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for M_AttributeSetInstance
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:06:02.421
 */
public class X_M_AttributeSetInstance extends PO {
	/** Standard Constructor */
	public X_M_AttributeSetInstance(Properties ctx,
			int M_AttributeSetInstance_ID, String trxName) {
		super(ctx, M_AttributeSetInstance_ID, trxName);
		/**
		 * if (M_AttributeSetInstance_ID == 0) { setM_AttributeSetInstance_ID
		 * (0); setM_AttributeSet_ID (0); }
		 */
	}

	/** Load Constructor */
	public X_M_AttributeSetInstance(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=M_AttributeSetInstance */
	public static final String Table_Name = "M_AttributeSetInstance";

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

	protected BigDecimal accessLevel = new BigDecimal(3);

	/** AccessLevel 3 - Client - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_M_AttributeSetInstance[").append(
				get_ID()).append("]");
		return sb.toString();
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
	 * Set Guarantee Date. Date when guarantee expires
	 */
	public void setGuaranteeDate(Timestamp GuaranteeDate) {
		set_Value("GuaranteeDate", GuaranteeDate);
	}

	/**
	 * Get Guarantee Date. Date when guarantee expires
	 */
	public Timestamp getGuaranteeDate() {
		return (Timestamp) get_Value("GuaranteeDate");
	}

	/**
	 * Set Lot No. Lot number (alphanumeric)
	 */
	public void setLot(String Lot) {
		if (Lot != null && Lot.length() > 40) {
			log.warning("Length > 40 - truncated");
			Lot = Lot.substring(0, 39);
		}
		set_Value("Lot", Lot);
	}

	/**
	 * Get Lot No. Lot number (alphanumeric)
	 */
	public String getLot() {
		return (String) get_Value("Lot");
	}

	/**
	 * Set Attribute Set Instance. Product Attribute Set Instance
	 */
	public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
		if (M_AttributeSetInstance_ID < 0)
			throw new IllegalArgumentException(
					"M_AttributeSetInstance_ID is mandatory.");
		set_ValueNoCheck("M_AttributeSetInstance_ID", new Integer(
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

	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), String
				.valueOf(getM_AttributeSetInstance_ID()));
	}

	/**
	 * Set Attribute Set. Product Attribute Set
	 */
	public void setM_AttributeSet_ID(int M_AttributeSet_ID) {
		if (M_AttributeSet_ID < 0)
			throw new IllegalArgumentException(
					"M_AttributeSet_ID is mandatory.");
		set_Value("M_AttributeSet_ID", new Integer(M_AttributeSet_ID));
	}

	/**
	 * Get Attribute Set. Product Attribute Set
	 */
	public int getM_AttributeSet_ID() {
		Integer ii = (Integer) get_Value("M_AttributeSet_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Lot. Product Lot Definition
	 */
	public void setM_Lot_ID(int M_Lot_ID) {
		if (M_Lot_ID <= 0)
			set_Value("M_Lot_ID", null);
		else
			set_Value("M_Lot_ID", new Integer(M_Lot_ID));
	}

	/**
	 * Get Lot. Product Lot Definition
	 */
	public int getM_Lot_ID() {
		Integer ii = (Integer) get_Value("M_Lot_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set C�digo de Producto */
	public void setProduct_Code(String Product_Code) {
		throw new IllegalArgumentException("Product_Code is virtual column");
	}

	/** Get C�digo de Producto */
	public String getProduct_Code() {
		return (String) get_Value("Product_Code");
	}

	/** Set Nombre de Producto */
	public void setProduct_Name(String Product_Name) {
		throw new IllegalArgumentException("Product_Name is virtual column");
	}

	/** Get Nombre de Producto */
	public String getProduct_Name() {
		return (String) get_Value("Product_Name");
	}

	/**
	 * Set Serial No. Product Serial Number
	 */
	public void setSerNo(String SerNo) {
		if (SerNo != null && SerNo.length() > 40) {
			log.warning("Length > 40 - truncated");
			SerNo = SerNo.substring(0, 39);
		}
		set_Value("SerNo", SerNo);
	}

	/**
	 * Get Serial No. Product Serial Number
	 */
	public String getSerNo() {
		return (String) get_Value("SerNo");
	}

	/**
	 * BISion - 15/02/2010 - Santiago Ibañez Metodo que setea la descripcion de
	 * bultos para la recepcion de terminado
	 * 
	 * @param bundle
	 */
	public void setBUNDLE(String bundle) {
		set_Value("BUNDLE", bundle);
	}

	public String getBUNDLE() {
		return (String) get_Value("BUNDLE");
	}
        
                /**
	 * Set Lote Andreani. 
	 */
	public void setLoteAndreani(String LoteAndreani) {
		if (LoteAndreani != null && LoteAndreani.length() > 40) {
			log.warning("Length > 40 - truncated");
			LoteAndreani = LoteAndreani.substring(0, 50);
		}
		set_Value("LOTE_ANDREANI", LoteAndreani);
	}

	/**
	 * Get Lote Andreani. 
	 */
	public String getLoteAndreani() {
		return (String) get_Value("LOTE_ANDREANI");
	}
}
