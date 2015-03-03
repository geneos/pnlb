/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_PAYMENTVALORES
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:59.906
 */
public class X_C_REGPERCEP_RECIB extends PO {
	/** Standard Constructor */
	public X_C_REGPERCEP_RECIB(Properties ctx, int C_REGPERCEP_RECIB_ID,
			String trxName) {
		super(ctx, C_REGPERCEP_RECIB_ID, trxName);

	}

	/** Load Constructor */
	public X_C_REGPERCEP_RECIB(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=C_REGPERCEP_RECIB */
	public static final String Table_Name = "C_REGPERCEP_RECIB";

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

	/** Set JURISDICCION */
	public void setJURISDICCION(Integer JUR) {
		set_Value("C_JURISDICCION_ID", JUR);
	}

	/** Get JURISDICCION */
	public Integer getJURISDICCION() {
		return (Integer) get_Value("C_JURISDICCION_ID");
	}

	/** Set COGIGO */
	public void setCODIGO(Integer CODIGO) {
		set_Value("CODIGO", CODIGO);
	}

	/** Get CODIGO */
	public Integer getCODIGO() {
		Integer bd = (Integer) get_Value("CODIGO");
		if (bd == null)
			return 0;
		return bd;
	}

	/** Set IMPUESTO */
	public void setIMPUESTO(String IMPUESTO) {
		if (IMPUESTO == null)
			throw new IllegalArgumentException("IMPUESTO is null.");
		set_Value("IMPUESTO", IMPUESTO);
	}

	/** Get IMPUESTO */
	public String getIMPUESTO() {
		String bd = (String) get_Value("IMPUESTO");
		if (bd == null)
			return null;
		return bd;
	}

	/** Set REGIMENIVA */
	public void setREGIMENIVA(String REGIMENIVA) {
		set_Value("REGIMENIVA", REGIMENIVA);
	}

	/** Get REGIMENIVA */
	public String getREGIMENIVA() {
		String bd = (String) get_Value("REGIMENIVA");
		if (bd == null)
			return null;
		return bd;
	}

	/** Set REGIMENIB */
	public void setREGIMENIB(String REGIMENIB) {
		set_Value("REGIMENIB", REGIMENIB);
	}

	/** Get REGIMENIB */
	public String getREGIMENIB() {
		String bd = (String) get_Value("REGIMENIB");
		if (bd == null)
			return null;
		return bd;
	}

	/** Set REGIMENGANANCIAS */
	public void setREGIMENGANANCIAS(String REGIMENGANANCIAS) {
		set_Value("REGIMENGANANCIAS", REGIMENGANANCIAS);
	}

	/** Get REGIMENGANANCIAS */
	public String getREGIMENGANANCIAS() {
		String bd = (String) get_Value("REGIMENGANANCIAS");
		if (bd == null)
			return null;
		return bd;
	}

	/** Set CLIENT */
	public void setAD_CLIENT_ID(Integer CLIENT) {
		if (CLIENT == null)
			throw new IllegalArgumentException("CLIENT is null.");
		set_Value("AD_CLIENT_ID", CLIENT);
	}

	/** Set ORG */
	public void setAD_ORG_ID(Integer ORG) {
		if (ORG == null)
			throw new IllegalArgumentException("ORG is null.");
		set_Value("AD_ORG_ID", ORG);
	}

	/** Set NAME */
	public void setNAME(String NAME) {
		if (NAME == null)
			throw new IllegalArgumentException("NAME is null.");
		set_Value("NAME", NAME);
	}
}
