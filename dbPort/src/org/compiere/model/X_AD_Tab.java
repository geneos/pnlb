/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Tab
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.375
 */
public class X_AD_Tab extends PO {
	/** Standard Constructor */
	public X_AD_Tab(Properties ctx, int AD_Tab_ID, String trxName) {
		super(ctx, AD_Tab_ID, trxName);
		/**
		 * if (AD_Tab_ID == 0) { setAD_Tab_ID (0); setAD_Table_ID (0);
		 * setAD_Window_ID (0); setEntityType (null); // U setHasTree (false);
		 * setIsAdvancedTab (false); // N setIsInsertRecord (true); // Y
		 * setIsReadOnly (false); setIsSingleRow (false); setIsSortTab (false); //
		 * N setIsTranslationTab (false); setName (null); setSeqNo (0); //
		 * 
		 * @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_Tab
		 *             WHERE AD_Window_ID=@AD_Window_ID@ setTabLevel (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_Tab(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Tab */
	public static final String Table_Name = "AD_Tab";

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
		StringBuffer sb = new StringBuffer("X_AD_Tab[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/** AD_ColumnSortOrder_ID AD_Reference_ID=257 */
	public static final int AD_COLUMNSORTORDER_ID_AD_Reference_ID = 257;

	/**
	 * Set Order Column. Column determining the order
	 */
	public void setAD_ColumnSortOrder_ID(int AD_ColumnSortOrder_ID) {
		if (AD_ColumnSortOrder_ID <= 0)
			set_Value("AD_ColumnSortOrder_ID", null);
		else
			set_Value("AD_ColumnSortOrder_ID", new Integer(
					AD_ColumnSortOrder_ID));
	}

	/**
	 * Get Order Column. Column determining the order
	 */
	public int getAD_ColumnSortOrder_ID() {
		Integer ii = (Integer) get_Value("AD_ColumnSortOrder_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AD_ColumnSortYesNo_ID AD_Reference_ID=258 */
	public static final int AD_COLUMNSORTYESNO_ID_AD_Reference_ID = 258;

	/**
	 * Set Included Column. Column determining if a Table Column is included in
	 * Ordering
	 */
	public void setAD_ColumnSortYesNo_ID(int AD_ColumnSortYesNo_ID) {
		if (AD_ColumnSortYesNo_ID <= 0)
			set_Value("AD_ColumnSortYesNo_ID", null);
		else
			set_Value("AD_ColumnSortYesNo_ID", new Integer(
					AD_ColumnSortYesNo_ID));
	}

	/**
	 * Get Included Column. Column determining if a Table Column is included in
	 * Ordering
	 */
	public int getAD_ColumnSortYesNo_ID() {
		Integer ii = (Integer) get_Value("AD_ColumnSortYesNo_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Image. System Image or Icon
	 */
	public void setAD_Image_ID(int AD_Image_ID) {
		if (AD_Image_ID <= 0)
			set_Value("AD_Image_ID", null);
		else
			set_Value("AD_Image_ID", new Integer(AD_Image_ID));
	}

	/**
	 * Get Image. System Image or Icon
	 */
	public int getAD_Image_ID() {
		Integer ii = (Integer) get_Value("AD_Image_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Process. Process or Report
	 */
	public void setAD_Process_ID(int AD_Process_ID) {
		if (AD_Process_ID <= 0)
			set_Value("AD_Process_ID", null);
		else
			set_Value("AD_Process_ID", new Integer(AD_Process_ID));
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

	/**
	 * Set Tab. Tab within a Window
	 */
	public void setAD_Tab_ID(int AD_Tab_ID) {
		if (AD_Tab_ID < 1)
			throw new IllegalArgumentException("AD_Tab_ID is mandatory.");
		set_ValueNoCheck("AD_Tab_ID", new Integer(AD_Tab_ID));
	}

	/**
	 * Get Tab. Tab within a Window
	 */
	public int getAD_Tab_ID() {
		Integer ii = (Integer) get_Value("AD_Tab_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID < 1)
			throw new IllegalArgumentException("AD_Table_ID is mandatory.");
		set_Value("AD_Table_ID", new Integer(AD_Table_ID));
	}

	/**
	 * Get Table. Table for the Fields
	 */
	public int getAD_Table_ID() {
		Integer ii = (Integer) get_Value("AD_Table_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Window. Data entry or display window
	 */
	public void setAD_Window_ID(int AD_Window_ID) {
		if (AD_Window_ID < 1)
			throw new IllegalArgumentException("AD_Window_ID is mandatory.");
		set_ValueNoCheck("AD_Window_ID", new Integer(AD_Window_ID));
	}

	/**
	 * Get Window. Data entry or display window
	 */
	public int getAD_Window_ID() {
		Integer ii = (Integer) get_Value("AD_Window_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Commit Warning. Warning displayed when saving
	 */
	public void setCommitWarning(String CommitWarning) {
		if (CommitWarning != null && CommitWarning.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			CommitWarning = CommitWarning.substring(0, 1999);
		}
		set_Value("CommitWarning", CommitWarning);
	}

	/**
	 * Get Commit Warning. Warning displayed when saving
	 */
	public String getCommitWarning() {
		return (String) get_Value("CommitWarning");
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
	 * Set Display Logic. If the Field is displayed, the result determines if
	 * the field is actually displayed
	 */
	public void setDisplayLogic(String DisplayLogic) {
		if (DisplayLogic != null && DisplayLogic.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			DisplayLogic = DisplayLogic.substring(0, 1999);
		}
		set_Value("DisplayLogic", DisplayLogic);
	}

	/**
	 * Get Display Logic. If the Field is displayed, the result determines if
	 * the field is actually displayed
	 */
	public String getDisplayLogic() {
		return (String) get_Value("DisplayLogic");
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
	 * Set Has Tree. Window has Tree Graph
	 */
	public void setHasTree(boolean HasTree) {
		set_Value("HasTree", new Boolean(HasTree));
	}

	/**
	 * Get Has Tree. Window has Tree Graph
	 */
	public boolean isHasTree() {
		Object oo = get_Value("HasTree");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
	 * Set Import Fields. Create Fields from Table Columns
	 */
	public void setImportFields(String ImportFields) {
		if (ImportFields != null && ImportFields.length() > 1) {
			log.warning("Length > 1 - truncated");
			ImportFields = ImportFields.substring(0, 0);
		}
		set_Value("ImportFields", ImportFields);
	}

	/**
	 * Get Import Fields. Create Fields from Table Columns
	 */
	public String getImportFields() {
		return (String) get_Value("ImportFields");
	}

	/** Included_Tab_ID AD_Reference_ID=278 */
	public static final int INCLUDED_TAB_ID_AD_Reference_ID = 278;

	/**
	 * Set Included Tab. Included Tab in this Tab (Master Dateail)
	 */
	public void setIncluded_Tab_ID(int Included_Tab_ID) {
		if (Included_Tab_ID <= 0)
			set_Value("Included_Tab_ID", null);
		else
			set_Value("Included_Tab_ID", new Integer(Included_Tab_ID));
	}

	/**
	 * Get Included Tab. Included Tab in this Tab (Master Dateail)
	 */
	public int getIncluded_Tab_ID() {
		Integer ii = (Integer) get_Value("Included_Tab_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Advanced Tab. This Tab contains advanced Functionality
	 */
	public void setIsAdvancedTab(boolean IsAdvancedTab) {
		set_Value("IsAdvancedTab", new Boolean(IsAdvancedTab));
	}

	/**
	 * Get Advanced Tab. This Tab contains advanced Functionality
	 */
	public boolean isAdvancedTab() {
		Object oo = get_Value("IsAdvancedTab");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Accounting Tab. This Tab contains accounting information
	 */
	public void setIsInfoTab(boolean IsInfoTab) {
		set_Value("IsInfoTab", new Boolean(IsInfoTab));
	}

	/**
	 * Get Accounting Tab. This Tab contains accounting information
	 */
	public boolean isInfoTab() {
		Object oo = get_Value("IsInfoTab");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Insert Record. The user can insert a new Record
	 */
	public void setIsInsertRecord(boolean IsInsertRecord) {
		set_Value("IsInsertRecord", new Boolean(IsInsertRecord));
	}

	/**
	 * Get Insert Record. The user can insert a new Record
	 */
	public boolean isInsertRecord() {
		Object oo = get_Value("IsInsertRecord");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Read Only. Field is read only
	 */
	public void setIsReadOnly(boolean IsReadOnly) {
		set_Value("IsReadOnly", new Boolean(IsReadOnly));
	}

	/**
	 * Get Read Only. Field is read only
	 */
	public boolean isReadOnly() {
		Object oo = get_Value("IsReadOnly");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Single Row Layout. Default for toggle between Single- and Multi-Row
	 * (Grid) Layout
	 */
	public void setIsSingleRow(boolean IsSingleRow) {
		set_Value("IsSingleRow", new Boolean(IsSingleRow));
	}

	/**
	 * Get Single Row Layout. Default for toggle between Single- and Multi-Row
	 * (Grid) Layout
	 */
	public boolean isSingleRow() {
		Object oo = get_Value("IsSingleRow");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Order Tab. The Tab determines the Order
	 */
	public void setIsSortTab(boolean IsSortTab) {
		set_Value("IsSortTab", new Boolean(IsSortTab));
	}

	/**
	 * Get Order Tab. The Tab determines the Order
	 */
	public boolean isSortTab() {
		Object oo = get_Value("IsSortTab");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set TranslationTab. This Tab contains translation information
	 */
	public void setIsTranslationTab(boolean IsTranslationTab) {
		set_Value("IsTranslationTab", new Boolean(IsTranslationTab));
	}

	/**
	 * Get TranslationTab. This Tab contains translation information
	 */
	public boolean isTranslationTab() {
		Object oo = get_Value("IsTranslationTab");
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
	 * Set Sql ORDER BY. Fully qualified ORDER BY clause
	 */
	public void setOrderByClause(String OrderByClause) {
		if (OrderByClause != null && OrderByClause.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			OrderByClause = OrderByClause.substring(0, 1999);
		}
		set_Value("OrderByClause", OrderByClause);
	}

	/**
	 * Get Sql ORDER BY. Fully qualified ORDER BY clause
	 */
	public String getOrderByClause() {
		return (String) get_Value("OrderByClause");
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
	 * Set Read Only Logic. Logic to determine if field is read only (applies
	 * only when field is read-write)
	 */
	public void setReadOnlyLogic(String ReadOnlyLogic) {
		if (ReadOnlyLogic != null && ReadOnlyLogic.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			ReadOnlyLogic = ReadOnlyLogic.substring(0, 1999);
		}
		set_Value("ReadOnlyLogic", ReadOnlyLogic);
	}

	/**
	 * Get Read Only Logic. Logic to determine if field is read only (applies
	 * only when field is read-write)
	 */
	public String getReadOnlyLogic() {
		return (String) get_Value("ReadOnlyLogic");
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
	 * Set Tab Level. Hierarchical Tab Level (0 = top)
	 */
	public void setTabLevel(int TabLevel) {
		set_Value("TabLevel", new Integer(TabLevel));
	}

	/**
	 * Get Tab Level. Hierarchical Tab Level (0 = top)
	 */
	public int getTabLevel() {
		Integer ii = (Integer) get_Value("TabLevel");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Sql WHERE. Fully qualified SQL WHERE clause
	 */
	public void setWhereClause(String WhereClause) {
		if (WhereClause != null && WhereClause.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			WhereClause = WhereClause.substring(0, 1999);
		}
		set_Value("WhereClause", WhereClause);
	}

	/**
	 * Get Sql WHERE. Fully qualified SQL WHERE clause
	 */
	public String getWhereClause() {
		return (String) get_Value("WhereClause");
	}
}
