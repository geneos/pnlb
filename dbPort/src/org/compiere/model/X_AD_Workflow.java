/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Workflow
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:57.984
 */
public class X_AD_Workflow extends PO {
	/** Standard Constructor */
	public X_AD_Workflow(Properties ctx, int AD_Workflow_ID, String trxName) {
		super(ctx, AD_Workflow_ID, trxName);
		/**
		 * if (AD_Workflow_ID == 0) { setAD_Workflow_ID (0); setAccessLevel
		 * (null); setAuthor (null); setCost (0); setDuration (0); setEntityType
		 * (null); // U setIsDefault (false); setIsValid (false); setName
		 * (null); setPublishStatus (null); // U setValue (null); setVersion
		 * (0); setWaitingTime (0); setWorkflowType (null); // G setWorkingTime
		 * (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_Workflow(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Workflow */
	public static final String Table_Name = "AD_Workflow";

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
		StringBuffer sb = new StringBuffer("X_AD_Workflow[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	/**
	 * Set Table. Table for the Fields
	 */
	public void setAD_Table_ID(int AD_Table_ID) {
		if (AD_Table_ID <= 0)
			set_Value("AD_Table_ID", null);
		else
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
	 * Set Node. Workflow Node (activity), step or process
	 */
	public void setAD_WF_Node_ID(int AD_WF_Node_ID) {
		if (AD_WF_Node_ID <= 0)
			set_Value("AD_WF_Node_ID", null);
		else
			set_Value("AD_WF_Node_ID", new Integer(AD_WF_Node_ID));
	}

	/**
	 * Get Node. Workflow Node (activity), step or process
	 */
	public int getAD_WF_Node_ID() {
		Integer ii = (Integer) get_Value("AD_WF_Node_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Workflow Responsible. Responsible for Workflow Execution
	 */
	public void setAD_WF_Responsible_ID(int AD_WF_Responsible_ID) {
		if (AD_WF_Responsible_ID <= 0)
			set_Value("AD_WF_Responsible_ID", null);
		else
			set_Value("AD_WF_Responsible_ID", new Integer(AD_WF_Responsible_ID));
	}

	/**
	 * Get Workflow Responsible. Responsible for Workflow Execution
	 */
	public int getAD_WF_Responsible_ID() {
		Integer ii = (Integer) get_Value("AD_WF_Responsible_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Workflow Processor. Workflow Processor Server
	 */
	public void setAD_WorkflowProcessor_ID(int AD_WorkflowProcessor_ID) {
		if (AD_WorkflowProcessor_ID <= 0)
			set_Value("AD_WorkflowProcessor_ID", null);
		else
			set_Value("AD_WorkflowProcessor_ID", new Integer(
					AD_WorkflowProcessor_ID));
	}

	/**
	 * Get Workflow Processor. Workflow Processor Server
	 */
	public int getAD_WorkflowProcessor_ID() {
		Integer ii = (Integer) get_Value("AD_WorkflowProcessor_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Workflow. Workflow or combination of tasks
	 */
	public void setAD_Workflow_ID(int AD_Workflow_ID) {
		if (AD_Workflow_ID < 1)
			throw new IllegalArgumentException("AD_Workflow_ID is mandatory.");
		set_ValueNoCheck("AD_Workflow_ID", new Integer(AD_Workflow_ID));
	}

	/**
	 * Get Workflow. Workflow or combination of tasks
	 */
	public int getAD_Workflow_ID() {
		Integer ii = (Integer) get_Value("AD_Workflow_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** AccessLevel AD_Reference_ID=5 */
	public static final int ACCESSLEVEL_AD_Reference_ID = 5;

	/** Organization = 1 */
	public static final String ACCESSLEVEL_Organization = "1";

	/** Client only = 2 */
	public static final String ACCESSLEVEL_ClientOnly = "2";

	/** Client+Organization = 3 */
	public static final String ACCESSLEVEL_ClientPlusOrganization = "3";

	/** System only = 4 */
	public static final String ACCESSLEVEL_SystemOnly = "4";

	/** System+Client = 6 */
	public static final String ACCESSLEVEL_SystemPlusClient = "6";

	/** All = 7 */
	public static final String ACCESSLEVEL_All = "7";

	/**
	 * Set Data Access Level. Access Level required
	 */
	public void setAccessLevel(String AccessLevel) {
		if (AccessLevel == null)
			throw new IllegalArgumentException("AccessLevel is mandatory");
		if (AccessLevel.equals("1") || AccessLevel.equals("2")
				|| AccessLevel.equals("3") || AccessLevel.equals("4")
				|| AccessLevel.equals("6") || AccessLevel.equals("7"))
			;
		else
			throw new IllegalArgumentException("AccessLevel Invalid value - "
					+ AccessLevel + " - Reference_ID=5 - 1 - 2 - 3 - 4 - 6 - 7");
		if (AccessLevel.length() > 1) {
			log.warning("Length > 1 - truncated");
			AccessLevel = AccessLevel.substring(0, 0);
		}
		set_Value("AccessLevel", AccessLevel);
	}

	/**
	 * Get Data Access Level. Access Level required
	 */
	public String getAccessLevel() {
		return (String) get_Value("AccessLevel");
	}

	/**
	 * Set Author. Author/Creator of the Entity
	 */
	public void setAuthor(String Author) {
		if (Author == null)
			throw new IllegalArgumentException("Author is mandatory.");
		if (Author.length() > 20) {
			log.warning("Length > 20 - truncated");
			Author = Author.substring(0, 19);
		}
		set_Value("Author", Author);
	}

	/**
	 * Get Author. Author/Creator of the Entity
	 */
	public String getAuthor() {
		return (String) get_Value("Author");
	}

	/**
	 * Set Cost. Cost information
	 */
	public void setCost(int Cost) {
		set_Value("Cost", new Integer(Cost));
	}

	/**
	 * Get Cost. Cost information
	 */
	public int getCost() {
		Integer ii = (Integer) get_Value("Cost");
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
	 * Set Document Value Logic. Logic to determine Workflow Start - If true, a
	 * workflow process is started for the document
	 */
	public void setDocValueLogic(String DocValueLogic) {
		if (DocValueLogic != null && DocValueLogic.length() > 2000) {
			log.warning("Length > 2000 - truncated");
			DocValueLogic = DocValueLogic.substring(0, 1999);
		}
		set_Value("DocValueLogic", DocValueLogic);
	}

	/**
	 * Get Document Value Logic. Logic to determine Workflow Start - If true, a
	 * workflow process is started for the document
	 */
	public String getDocValueLogic() {
		return (String) get_Value("DocValueLogic");
	}

	/**
	 * Set Document No. Document sequence number of the document
	 */
	public void setDocumentNo(String DocumentNo) {
		if (DocumentNo != null && DocumentNo.length() > 30) {
			log.warning("Length > 30 - truncated");
			DocumentNo = DocumentNo.substring(0, 29);
		}
		set_Value("DocumentNo", DocumentNo);
	}

	/**
	 * Get Document No. Document sequence number of the document
	 */
	public String getDocumentNo() {
		return (String) get_Value("DocumentNo");
	}

	/**
	 * Set Duration. Normal Duration in Duration Unit
	 */
	public void setDuration(int Duration) {
		set_Value("Duration", new Integer(Duration));
	}

	/**
	 * Get Duration. Normal Duration in Duration Unit
	 */
	public int getDuration() {
		Integer ii = (Integer) get_Value("Duration");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** DurationUnit AD_Reference_ID=299 */
	public static final int DURATIONUNIT_AD_Reference_ID = 299;

	/** Day = D */
	public static final String DURATIONUNIT_Day = "D";

	/** Month = M */
	public static final String DURATIONUNIT_Month = "M";

	/** Year = Y */
	public static final String DURATIONUNIT_Year = "Y";

	/** Hour = h */
	public static final String DURATIONUNIT_Hour = "h";

	/** Minute = m */
	public static final String DURATIONUNIT_Minute = "m";

	/** Second = s */
	public static final String DURATIONUNIT_Second = "s";

	/**
	 * Set Duration Unit. Unit of Duration
	 */
	public void setDurationUnit(String DurationUnit) {
		if (DurationUnit != null && DurationUnit.length() > 1) {
			log.warning("Length > 1 - truncated");
			DurationUnit = DurationUnit.substring(0, 0);
		}
		set_Value("DurationUnit", DurationUnit);
	}

	/**
	 * Get Duration Unit. Unit of Duration
	 */
	public String getDurationUnit() {
		return (String) get_Value("DurationUnit");
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
	 * Set Valid. Element is valid
	 */
	public void setIsValid(boolean IsValid) {
		set_Value("IsValid", new Boolean(IsValid));
	}

	/**
	 * Get Valid. Element is valid
	 */
	public boolean isValid() {
		Object oo = get_Value("IsValid");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Duration Limit. Maximum Duration in Duration Unit
	 */
	public void setLimit(int Limit) {
		set_Value("Limit", new Integer(Limit));
	}

	/**
	 * Get Duration Limit. Maximum Duration in Duration Unit
	 */
	public int getLimit() {
		Integer ii = (Integer) get_Value("Limit");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Move Time. Time to move material form one operation to another
	 */
	public void setMovingTime(int MovingTime) {
		set_Value("MovingTime", new Integer(MovingTime));
	}

	/**
	 * Get Move Time. Time to move material form one operation to another
	 */
	public int getMovingTime() {
		Integer ii = (Integer) get_Value("MovingTime");
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

	/**
	 * Set Priority. Indicates if this request is of a high, medium or low
	 * priority.
	 */
	public void setPriority(int Priority) {
		set_Value("Priority", new Integer(Priority));
	}

	/**
	 * Get Priority. Indicates if this request is of a high, medium or low
	 * priority.
	 */
	public int getPriority() {
		Integer ii = (Integer) get_Value("Priority");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** ProcessType AD_Reference_ID=1000010 */
	public static final int PROCESSTYPE_AD_Reference_ID = 1000010;

	/** Batch Flow = BF */
	public static final String PROCESSTYPE_BatchFlow = "BF";

	/** Continuous Flow = CF */
	public static final String PROCESSTYPE_ContinuousFlow = "CF";

	/** Dedicate Repetititive Flow = DR */
	public static final String PROCESSTYPE_DedicateRepetititiveFlow = "DR";

	/** Job Shop = JS */
	public static final String PROCESSTYPE_JobShop = "JS";

	/** Mixed Repetitive Flow = MR */
	public static final String PROCESSTYPE_MixedRepetitiveFlow = "MR";

	/** Plant = PL */
	public static final String PROCESSTYPE_Plant = "PL";

	/** Set Process Type */
	public void setProcessType(String ProcessType) {
		if (ProcessType != null && ProcessType.length() > 2) {
			log.warning("Length > 2 - truncated");
			ProcessType = ProcessType.substring(0, 1);
		}
		set_Value("ProcessType", ProcessType);
	}

	/** Get Process Type */
	public String getProcessType() {
		return (String) get_Value("ProcessType");
	}

	/** PublishStatus AD_Reference_ID=310 */
	public static final int PUBLISHSTATUS_AD_Reference_ID = 310;

	/** Released = R */
	public static final String PUBLISHSTATUS_Released = "R";

	/** Test = T */
	public static final String PUBLISHSTATUS_Test = "T";

	/** Under Revision = U */
	public static final String PUBLISHSTATUS_UnderRevision = "U";

	/** Void = V */
	public static final String PUBLISHSTATUS_Void = "V";

	/**
	 * Set Publication Status. Status of Publication
	 */
	public void setPublishStatus(String PublishStatus) {
		if (PublishStatus == null)
			throw new IllegalArgumentException("PublishStatus is mandatory");
		if (PublishStatus.equals("R") || PublishStatus.equals("T")
				|| PublishStatus.equals("U") || PublishStatus.equals("V"))
			;
		else
			throw new IllegalArgumentException("PublishStatus Invalid value - "
					+ PublishStatus + " - Reference_ID=310 - R - T - U - V");
		if (PublishStatus.length() > 1) {
			log.warning("Length > 1 - truncated");
			PublishStatus = PublishStatus.substring(0, 0);
		}
		set_Value("PublishStatus", PublishStatus);
	}

	/**
	 * Get Publication Status. Status of Publication
	 */
	public String getPublishStatus() {
		return (String) get_Value("PublishStatus");
	}

	/** Set Qty Batch Size */
	public void setQtyBatchSize(BigDecimal QtyBatchSize) {
		set_Value("QtyBatchSize", QtyBatchSize);
	}

	/** Get Qty Batch Size */
	public BigDecimal getQtyBatchSize() {
		BigDecimal bd = (BigDecimal) get_Value("QtyBatchSize");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** Set QueuingTime */
	public void setQueuingTime(int QueuingTime) {
		set_Value("QueuingTime", new Integer(QueuingTime));
	}

	/** Get QueuingTime */
	public int getQueuingTime() {
		Integer ii = (Integer) get_Value("QueuingTime");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Resource. Resource
	 */
	public void setS_Resource_ID(int S_Resource_ID) {
		if (S_Resource_ID <= 0)
			set_Value("S_Resource_ID", null);
		else
			set_Value("S_Resource_ID", new Integer(S_Resource_ID));
	}

	/**
	 * Get Resource. Resource
	 */
	public int getS_Resource_ID() {
		Integer ii = (Integer) get_Value("S_Resource_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Setup Time. Setup time before starting Production
	 */
	public void setSetupTime(int SetupTime) {
		set_Value("SetupTime", new Integer(SetupTime));
	}

	/**
	 * Get Setup Time. Setup time before starting Production
	 */
	public int getSetupTime() {
		Integer ii = (Integer) get_Value("SetupTime");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Valid from. Valid from including this date (first day)
	 */
	public void setValidFrom(Timestamp ValidFrom) {
		set_Value("ValidFrom", ValidFrom);
	}

	/**
	 * Get Valid from. Valid from including this date (first day)
	 */
	public Timestamp getValidFrom() {
		return (Timestamp) get_Value("ValidFrom");
	}

	/**
	 * Set Valid to. Valid to including this date (last day)
	 */
	public void setValidTo(Timestamp ValidTo) {
		set_Value("ValidTo", ValidTo);
	}

	/**
	 * Get Valid to. Valid to including this date (last day)
	 */
	public Timestamp getValidTo() {
		return (Timestamp) get_Value("ValidTo");
	}

	/** Set Validate Workflow */
	public void setValidateWorkflow(String ValidateWorkflow) {
		if (ValidateWorkflow != null && ValidateWorkflow.length() > 1) {
			log.warning("Length > 1 - truncated");
			ValidateWorkflow = ValidateWorkflow.substring(0, 0);
		}
		set_Value("ValidateWorkflow", ValidateWorkflow);
	}

	/** Get Validate Workflow */
	public String getValidateWorkflow() {
		return (String) get_Value("ValidateWorkflow");
	}

	/**
	 * Set Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public void setValue(String Value) {
		if (Value == null)
			throw new IllegalArgumentException("Value is mandatory.");
		if (Value.length() > 40) {
			log.warning("Length > 40 - truncated");
			Value = Value.substring(0, 39);
		}
		set_Value("Value", Value);
	}

	/**
	 * Get Search Key. Search key for the record in the format required - must
	 * be unique
	 */
	public String getValue() {
		return (String) get_Value("Value");
	}

	/**
	 * Set Version. Version of the table definition
	 */
	public void setVersion(int Version) {
		set_Value("Version", new Integer(Version));
	}

	/**
	 * Get Version. Version of the table definition
	 */
	public int getVersion() {
		Integer ii = (Integer) get_Value("Version");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Waiting Time. Workflow Simulation Waiting time
	 */
	public void setWaitingTime(int WaitingTime) {
		set_Value("WaitingTime", new Integer(WaitingTime));
	}

	/**
	 * Get Waiting Time. Workflow Simulation Waiting time
	 */
	public int getWaitingTime() {
		Integer ii = (Integer) get_Value("WaitingTime");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** WorkflowType AD_Reference_ID=328 */
	public static final int WORKFLOWTYPE_AD_Reference_ID = 328;

	/** General = G */
	public static final String WORKFLOWTYPE_General = "G";

	/** Manufacturing = M */
	public static final String WORKFLOWTYPE_Manufacturing = "M";

	/** Document Process = P */
	public static final String WORKFLOWTYPE_DocumentProcess = "P";

	/** Quality = Q */
	public static final String WORKFLOWTYPE_Quality = "Q";

	/** Document Value = V */
	public static final String WORKFLOWTYPE_DocumentValue = "V";

	/**
	 * Set Workflow Type. Type of Worflow
	 */
	public void setWorkflowType(String WorkflowType) {
		if (WorkflowType == null)
			throw new IllegalArgumentException("WorkflowType is mandatory");
		if (WorkflowType.equals("G") || WorkflowType.equals("M")
				|| WorkflowType.equals("P") || WorkflowType.equals("Q")
				|| WorkflowType.equals("V"))
			;
		else
			throw new IllegalArgumentException("WorkflowType Invalid value - "
					+ WorkflowType + " - Reference_ID=328 - G - M - P - Q - V");
		if (WorkflowType.length() > 1) {
			log.warning("Length > 1 - truncated");
			WorkflowType = WorkflowType.substring(0, 0);
		}
		set_Value("WorkflowType", WorkflowType);
	}

	/**
	 * Get Workflow Type. Type of Worflow
	 */
	public String getWorkflowType() {
		return (String) get_Value("WorkflowType");
	}

	/**
	 * Set Working Time. Workflow Simulation Execution Time
	 */
	public void setWorkingTime(int WorkingTime) {
		set_Value("WorkingTime", new Integer(WorkingTime));
	}

	/**
	 * Get Working Time. Workflow Simulation Execution Time
	 */
	public int getWorkingTime() {
		Integer ii = (Integer) get_Value("WorkingTime");
		if (ii == null)
			return 0;
		return ii.intValue();
	}
}
