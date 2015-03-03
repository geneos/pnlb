/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Process_Para
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.015
 */
public class X_AD_Process_Para extends PO {
	/** Standard Constructor */
	public X_AD_Process_Para(Properties ctx, int AD_Process_Para_ID,
			String trxName) {
		super(ctx, AD_Process_Para_ID, trxName);
		/**
		 * if (AD_Process_Para_ID == 0) { setAD_Process_ID (0);
		 * setAD_Process_Para_ID (0); setAD_Reference_ID (0); setColumnName
		 * (null); setEntityType (null); // U setFieldLength (0);
		 * setIsCentrallyMaintained (true); // Y setIsMandatory (false);
		 * setIsRange (false); setName (null); setSeqNo (0); //
		 * 
		 * @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_Process_Para
		 *             WHERE AD_Process_ID=@AD_Process_ID@ }
		 */
	}

	/** Load Constructor */
	public X_AD_Process_Para(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Process_Para */
	public static final String Table_Name = "AD_Process_Para";

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
	
	protected BigDecimal accessLevel = new BigDecimal(4);

	/** AccessLevel 4 - System */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("X_AD_Process_Para[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set System Element. System Element enables the central maintenance of
	 * column description and help.
	 */
	public void setAD_Element_ID(int AD_Element_ID) {
		if (AD_Element_ID <= 0)
			set_Value("AD_Element_ID", null);
		else
			set_Value("AD_Element_ID", new Integer(AD_Element_ID));
	}

	/**
	 * Get System Element. System Element enables the central maintenance of
	 * column description and help.
	 */
	public int getAD_Element_ID() {
		Integer ii = (Integer) get_Value("AD_Element_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Process. Process or Report
	 */
	public void setAD_Process_ID(int AD_Process_ID) {
		if (AD_Process_ID < 1)
			throw new IllegalArgumentException("AD_Process_ID is mandatory.");
		set_ValueNoCheck("AD_Process_ID", new Integer(AD_Process_ID));
	}

	/**
	 * Get Process. Process or Report
	 */
	public int getAD_Process_ID() {
		Integer ii = (Integer) get_Value("AD_Process_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** Set Process Parameter */
	public void setAD_Process_Para_ID(int AD_Process_Para_ID) {
		if (AD_Process_Para_ID < 1)
			throw new IllegalArgumentException(
					"AD_Process_Para_ID is mandatory.");
		set_ValueNoCheck("AD_Process_Para_ID", new Integer(AD_Process_Para_ID));
	}

	/** Get Process Parameter */
	public int getAD_Process_Para_ID() {
		Integer ii = (Integer) get_Value("AD_Process_Para_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Reference_ID AD_Reference_ID=1 */
	public static final int AD_REFERENCE_ID_AD_Reference_ID = 1;

	/**
	 * Set Reference. System Reference (Pick List)
	 */
	public void setAD_Reference_ID(int AD_Reference_ID) {
		if (AD_Reference_ID < 1)
			throw new IllegalArgumentException("AD_Reference_ID is mandatory.");
		set_Value("AD_Reference_ID", new Integer(AD_Reference_ID));
	}

	/**
	 * Get Reference. System Reference (Pick List)
	 */
	public int getAD_Reference_ID() {
		Integer ii = (Integer) get_Value("AD_Reference_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_Reference_Value_ID AD_Reference_ID=4 */
	public static final int AD_REFERENCE_VALUE_ID_AD_Reference_ID = 4;

	/**
	 * Set Reference Key. Required to specify, if data type is Table or List
	 */
	public void setAD_Reference_Value_ID(int AD_Reference_Value_ID) {
		if (AD_Reference_Value_ID <= 0)
			set_Value("AD_Reference_Value_ID", null);
		else
			set_Value("AD_Reference_Value_ID", new Integer(
					AD_Reference_Value_ID));
	}

	/**
	 * Get Reference Key. Required to specify, if data type is Table or List
	 */
	public int getAD_Reference_Value_ID() {
		Integer ii = (Integer) get_Value("AD_Reference_Value_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Dynamic Validation. Dynamic Validation Rule
	 */
	public void setAD_Val_Rule_ID(int AD_Val_Rule_ID) {
		if (AD_Val_Rule_ID <= 0)
			set_Value("AD_Val_Rule_ID", null);
		else
			set_Value("AD_Val_Rule_ID", new Integer(AD_Val_Rule_ID));
	}

	/**
	 * Get Dynamic Validation. Dynamic Validation Rule
	 */
	public int getAD_Val_Rule_ID() {
		Integer ii = (Integer) get_Value("AD_Val_Rule_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set DB Column Name. Name of the column in the database
	 */
	public void setColumnName(String ColumnName) {
		if (ColumnName == null)
			throw new IllegalArgumentException("ColumnName is mandatory.");
		if (ColumnName.length() > 40) {
			log.warning("Length > 40 - truncated");
			ColumnName = ColumnName.substring(0, 39);
		}
		set_Value("ColumnName", ColumnName);
	}

	/**
	 * Get DB Column Name. Name of the column in the database
	 */
	public String getColumnName() {
		return (String) get_Value("ColumnName");
	}

	/**
	 * Set Default Logic. Default value hierarchy, separated by ;
	 */
	public void setDefaultValue(String DefaultValue) {
		if (DefaultValue != null && DefaultValue.length() > 255) {
			log.warning("Length > 255 - truncated");
			DefaultValue = DefaultValue.substring(0, 254);
		}
		set_Value("DefaultValue", DefaultValue);
	}

	/**
	 * Get Default Logic. Default value hierarchy, separated by ;
	 */
	public String getDefaultValue() {
		return (String) get_Value("DefaultValue");
	}

	/**
	 * Set Default Logic 2. Default value hierarchy, separated by ;
	 */
	public void setDefaultValue2(String DefaultValue2) {
		if (DefaultValue2 != null && DefaultValue2.length() > 255) {
			log.warning("Length > 255 - truncated");
			DefaultValue2 = DefaultValue2.substring(0, 254);
		}
		set_Value("DefaultValue2", DefaultValue2);
	}

	/**
	 * Get Default Logic 2. Default value hierarchy, separated by ;
	 */
	public String getDefaultValue2() {
		return (String) get_Value("DefaultValue2");
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

	/** EntityType AD_Reference_ID=245 */
	public static final int ENTITYTYPE_AD_Reference_ID = 245;

	/** Applications = A */
	public static final String ENTITYTYPE_Applications = "A";

	/** Compiere = C */
	public static final String ENTITYTYPE_Compiere = "C";

	/** Customization = CUST */
	public static final String ENTITYTYPE_Customization = "CUST";

	/** Dictionary = D */
	public static final String ENTITYTYPE_Dictionary = "D";

	/** User maintained = U */
	public static final String ENTITYTYPE_UserMaintained = "U";

	/**
	 * Set Entity Type. Dictionary Entity Type; Determines ownership and
	 * synchronization
	 */
	public void setEntityType(String EntityType) {
		if (EntityType == null)
			throw new IllegalArgumentException("EntityType is mandatory");
		if (EntityType.length() > 4) {
			log.warning("Length > 4 - truncated");
			EntityType = EntityType.substring(0, 3);
		}
		set_Value("EntityType", EntityType);
	}

	/**
	 * Get Entity Type. Dictionary Entity Type; Determines ownership and
	 * synchronization
	 */
	public String getEntityType() {
		return (String) get_Value("EntityType");
	}

	/**
	 * Set Length. Length of the column in the database
	 */
	public void setFieldLength(int FieldLength) {
		set_Value("FieldLength", new Integer(FieldLength));
	}

	/**
	 * Get Length. Length of the column in the database
	 */
	public int getFieldLength() {
		Integer ii = (Integer) get_Value("FieldLength");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Comment/Help. Comment or Hint
	 */
	public void setHelp(String Help) {
		if (Help != null && Help.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			Help = Help.substring(0, 1999);
		}
		set_Value("Help", Help);
	}

	/**
	 * Get Comment/Help. Comment or Hint
	 */
	public String getHelp() {
		return (String) get_Value("Help");
	}

	/**
	 * Set Centrally maintained. Information maintained in System Element table
	 */
	public void setIsCentrallyMaintained(boolean IsCentrallyMaintained) {
		set_Value("IsCentrallyMaintained", new Boolean(IsCentrallyMaintained));
	}

	/**
	 * Get Centrally maintained. Information maintained in System Element table
	 */
	public boolean isCentrallyMaintained() {
		Object oo = get_Value("IsCentrallyMaintained");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Mandatory. Data entry is required in this column
	 */
	public void setIsMandatory(boolean IsMandatory) {
		set_Value("IsMandatory", new Boolean(IsMandatory));
	}

	/**
	 * Get Mandatory. Data entry is required in this column
	 */
	public boolean isMandatory() {
		Object oo = get_Value("IsMandatory");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Range. The parameter is a range of values
	 */
	public void setIsRange(boolean IsRange) {
		set_Value("IsRange", new Boolean(IsRange));
	}

	/**
	 * Get Range. The parameter is a range of values
	 */
	public boolean isRange() {
		Object oo = get_Value("IsRange");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/**
	 * Set Value Format. Format of the value; Can contain fixed format elements,
	 * Variables: "_lLoOaAcCa09"
	 */
	public void setVFormat(String VFormat) {
		if (VFormat != null && VFormat.length() > 20) {
			log.warning("Length > 20 - truncated");
			VFormat = VFormat.substring(0, 19);
		}
		set_Value("VFormat", VFormat);
	}

	/**
	 * Get Value Format. Format of the value; Can contain fixed format elements,
	 * Variables: "_lLoOaAcCa09"
	 */
	public String getVFormat() {
		return (String) get_Value("VFormat");
	}

	/**
	 * Set Max. Value. Maximum Value for a field
	 */
	public void setValueMax(String ValueMax) {
		if (ValueMax != null && ValueMax.length() > 20) {
			log.warning("Length > 20 - truncated");
			ValueMax = ValueMax.substring(0, 19);
		}
		set_Value("ValueMax", ValueMax);
	}

	/**
	 * Get Max. Value. Maximum Value for a field
	 */
	public String getValueMax() {
		return (String) get_Value("ValueMax");
	}

	/**
	 * Set Min. Value. Minimum Value for a field
	 */
	public void setValueMin(String ValueMin) {
		if (ValueMin != null && ValueMin.length() > 20) {
			log.warning("Length > 20 - truncated");
			ValueMin = ValueMin.substring(0, 19);
		}
		set_Value("ValueMin", ValueMin);
	}

	/**
	 * Get Min. Value. Minimum Value for a field
	 */
	public String getValueMin() {
		return (String) get_Value("ValueMin");
	}
}
