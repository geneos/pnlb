/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_WF_Node
 * 
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-01-22 12:09:19.656
 */
public class X_AD_WF_Node extends PO {
	/** Standard Constructor */
	public X_AD_WF_Node(Properties ctx, int AD_WF_Node_ID, String trxName) {
		super(ctx, AD_WF_Node_ID, trxName);
		/**
		 * if (AD_WF_Node_ID == 0) { setAD_WF_Node_ID (0); setAD_Workflow_ID
		 * (0); setAction (null); // N setCost (Env.ZERO); setDuration (0);
		 * setEntityType (null); // U setIsCentrallyMaintained (true); // Y
		 * setJoinElement (null); // X setLimit (0); setName (null);
		 * setSplitElement (null); // X setValue (null); setWaitingTime (0);
		 * setXPosition (0); setYPosition (0); }
		 */
	}

	/** Load Constructor */
	public X_AD_WF_Node(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_WF_Node */
	public static final String Table_Name = "AD_WF_Node";

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
		StringBuffer sb = new StringBuffer("X_AD_WF_Node[").append(get_ID())
				.append("]");
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
	 * Set Special Form. Special Form
	 */
	public void setAD_Form_ID(int AD_Form_ID) {
		if (AD_Form_ID <= 0)
			set_Value("AD_Form_ID", null);
		else
			set_Value("AD_Form_ID", new Integer(AD_Form_ID));
	}

	/**
	 * Get Special Form. Special Form
	 */
	public int getAD_Form_ID() {
		Integer ii = (Integer) get_Value("AD_Form_ID");
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
	 * Set OS Task. Operation System Task
	 */
	public void setAD_Task_ID(int AD_Task_ID) {
		if (AD_Task_ID <= 0)
			set_Value("AD_Task_ID", null);
		else
			set_Value("AD_Task_ID", new Integer(AD_Task_ID));
	}

	/**
	 * Get OS Task. Operation System Task
	 */
	public int getAD_Task_ID() {
		Integer ii = (Integer) get_Value("AD_Task_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Workflow Block. Workflow Transaction Execution Block
	 */
	public void setAD_WF_Block_ID(int AD_WF_Block_ID) {
		if (AD_WF_Block_ID <= 0)
			set_Value("AD_WF_Block_ID", null);
		else
			set_Value("AD_WF_Block_ID", new Integer(AD_WF_Block_ID));
	}

	/**
	 * Get Workflow Block. Workflow Transaction Execution Block
	 */
	public int getAD_WF_Block_ID() {
		Integer ii = (Integer) get_Value("AD_WF_Block_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Node. Workflow Node (activity), step or process
	 */
	public void setAD_WF_Node_ID(int AD_WF_Node_ID) {
		if (AD_WF_Node_ID < 1)
			throw new IllegalArgumentException("AD_WF_Node_ID is mandatory.");
		set_ValueNoCheck("AD_WF_Node_ID", new Integer(AD_WF_Node_ID));
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
	 * Set Window. Data entry or display window
	 */
	public void setAD_Window_ID(int AD_Window_ID) {
		if (AD_Window_ID <= 0)
			set_Value("AD_Window_ID", null);
		else
			set_Value("AD_Window_ID", new Integer(AD_Window_ID));
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

	/** Action AD_Reference_ID=302 */
	public static final int ACTION_AD_Reference_ID = 302;

	/** User Workbench = B */
	public static final String ACTION_UserWorkbench = "B";

	/** User Choice = C */
	public static final String ACTION_UserChoice = "C";

	/** Document Action = D */
	public static final String ACTION_DocumentAction = "D";

	/** Sub Workflow = F */
	public static final String ACTION_SubWorkflow = "F";

	/** EMail = M */
	public static final String ACTION_EMail = "M";

	/** Apps Process = P */
	public static final String ACTION_AppsProcess = "P";

	/** Apps Report = R */
	public static final String ACTION_AppsReport = "R";

	/** Apps Task = T */
	public static final String ACTION_AppsTask = "T";

	/** Set Variable = V */
	public static final String ACTION_SetVariable = "V";

	/** User Window = W */
	public static final String ACTION_UserWindow = "W";

	/** User Form = X */
	public static final String ACTION_UserForm = "X";

	/** Wait (Sleep) = Z */
	public static final String ACTION_WaitSleep = "Z";

	/**
	 * Set Action. Indicates the Action to be performed
	 */
	public void setAction(String Action) {
		if (Action == null)
			throw new IllegalArgumentException("Action is mandatory");
		if (Action.equals("B") || Action.equals("C") || Action.equals("D")
				|| Action.equals("F") || Action.equals("M")
				|| Action.equals("P") || Action.equals("R")
				|| Action.equals("T") || Action.equals("V")
				|| Action.equals("W") || Action.equals("X")
				|| Action.equals("Z"))
			;
		else
			throw new IllegalArgumentException(
					"Action Invalid value - "
							+ Action
							+ " - Reference_ID=302 - B - C - D - F - M - P - R - T - V - W - X - Z");
		if (Action.length() > 1) {
			log.warning("Length > 1 - truncated");
			Action = Action.substring(0, 0);
		}
		set_Value("Action", Action);
	}

	/**
	 * Get Action. Indicates the Action to be performed
	 */
	public String getAction() {
		return (String) get_Value("Action");
	}

	/**
	 * Set Attribute Name. Name of the Attribute
	 */
	public void setAttributeName(String AttributeName) {
		if (AttributeName != null && AttributeName.length() > 60) {
			log.warning("Length > 60 - truncated");
			AttributeName = AttributeName.substring(0, 59);
		}
		set_Value("AttributeName", AttributeName);
	}

	/**
	 * Get Attribute Name. Name of the Attribute
	 */
	public String getAttributeName() {
		return (String) get_Value("AttributeName");
	}

	/**
	 * Set Attribute Value. Value of the Attribute
	 */
	public void setAttributeValue(String AttributeValue) {
		if (AttributeValue != null && AttributeValue.length() > 60) {
			log.warning("Length > 60 - truncated");
			AttributeValue = AttributeValue.substring(0, 59);
		}
		set_Value("AttributeValue", AttributeValue);
	}

	/**
	 * Get Attribute Value. Value of the Attribute
	 */
	public String getAttributeValue() {
		return (String) get_Value("AttributeValue");
	}

	/** Set BATCHTIME */
	public void setBATCHTIME(int BATCHTIME) {
		set_Value("BATCHTIME", new Integer(BATCHTIME));
	}

	/** Get BATCHTIME */
	public int getBATCHTIME() {
		Integer ii = (Integer) get_Value("BATCHTIME");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Business Partner . Identifies a Business Partner
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		if (C_BPartner_ID <= 0)
			set_Value("C_BPartner_ID", null);
		else
			set_Value("C_BPartner_ID", new Integer(C_BPartner_ID));
	}

	/**
	 * Get Business Partner . Identifies a Business Partner
	 */
	public int getC_BPartner_ID() {
		Integer ii = (Integer) get_Value("C_BPartner_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Set Cost. Cost information
	 */
	public void setCost(BigDecimal Cost) {
		if (Cost == null)
			throw new IllegalArgumentException("Cost is mandatory.");
		set_Value("Cost", Cost);
	}

	/**
	 * Get Cost. Cost information
	 */
	public BigDecimal getCost() {
		BigDecimal bd = (BigDecimal) get_Value("Cost");
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

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID = 135;

	/** <None> = -- */
	public static final String DOCACTION_None = "--";

	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";

	/** Close = CL */
	public static final String DOCACTION_Close = "CL";

	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";

	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";

	/** Post = PO */
	public static final String DOCACTION_Post = "PO";

	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";

	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";

	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";

	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";

	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";

	/** Void = VO */
	public static final String DOCACTION_Void = "VO";

	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";

	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";

	/**
	 * Set Document Action. The targeted status of the document
	 */
	public void setDocAction(String DocAction) {
		if (DocAction != null && DocAction.length() > 2) {
			log.warning("Length > 2 - truncated");
			DocAction = DocAction.substring(0, 1);
		}
		set_Value("DocAction", DocAction);
	}

	/**
	 * Get Document Action. The targeted status of the document
	 */
	public String getDocAction() {
		return (String) get_Value("DocAction");
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

	/**
	 * Set Dynamic Priority Change. Change of priority when Activity is
	 * suspended waiting for user
	 */
	public void setDynPriorityChange(BigDecimal DynPriorityChange) {
		set_Value("DynPriorityChange", DynPriorityChange);
	}

	/**
	 * Get Dynamic Priority Change. Change of priority when Activity is
	 * suspended waiting for user
	 */
	public BigDecimal getDynPriorityChange() {
		BigDecimal bd = (BigDecimal) get_Value("DynPriorityChange");
		if (bd == null)
			return Env.ZERO;
		return bd;
	}

	/** DynPriorityUnit AD_Reference_ID=221 */
	public static final int DYNPRIORITYUNIT_AD_Reference_ID = 221;

	/** Day = D */
	public static final String DYNPRIORITYUNIT_Day = "D";

	/** Hour = H */
	public static final String DYNPRIORITYUNIT_Hour = "H";

	/** Minute = M */
	public static final String DYNPRIORITYUNIT_Minute = "M";

	/**
	 * Set Dynamic Priority Unit. Change of priority when Activity is suspended
	 * waiting for user
	 */
	public void setDynPriorityUnit(String DynPriorityUnit) {
		if (DynPriorityUnit != null && DynPriorityUnit.length() > 1) {
			log.warning("Length > 1 - truncated");
			DynPriorityUnit = DynPriorityUnit.substring(0, 0);
		}
		set_Value("DynPriorityUnit", DynPriorityUnit);
	}

	/**
	 * Get Dynamic Priority Unit. Change of priority when Activity is suspended
	 * waiting for user
	 */
	public String getDynPriorityUnit() {
		return (String) get_Value("DynPriorityUnit");
	}

	/**
	 * Set EMail Address. Electronic Mail Address
	 */
	public void setEMail(String EMail) {
		if (EMail != null && EMail.length() > 60) {
			log.warning("Length > 60 - truncated");
			EMail = EMail.substring(0, 59);
		}
		set_Value("EMail", EMail);
	}

	/**
	 * Get EMail Address. Electronic Mail Address
	 */
	public String getEMail() {
		return (String) get_Value("EMail");
	}

	/** EMailRecipient AD_Reference_ID=363 */
	public static final int EMAILRECIPIENT_AD_Reference_ID = 363;

	/** Document Business Partner = B */
	public static final String EMAILRECIPIENT_DocumentBusinessPartner = "B";

	/** Document Owner = D */
	public static final String EMAILRECIPIENT_DocumentOwner = "D";

	/** WF Responsible = R */
	public static final String EMAILRECIPIENT_WFResponsible = "R";

	/**
	 * Set EMail Recipient. Recipient of the EMail
	 */
	public void setEMailRecipient(String EMailRecipient) {
		if (EMailRecipient != null && EMailRecipient.length() > 1) {
			log.warning("Length > 1 - truncated");
			EMailRecipient = EMailRecipient.substring(0, 0);
		}
		set_Value("EMailRecipient", EMailRecipient);
	}

	/**
	 * Get EMail Recipient. Recipient of the EMail
	 */
	public String getEMailRecipient() {
		return (String) get_Value("EMailRecipient");
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

	/** FinishMode AD_Reference_ID=303 */
	public static final int FINISHMODE_AD_Reference_ID = 303;

	/** Automatic = A */
	public static final String FINISHMODE_Automatic = "A";

	/** Manual = M */
	public static final String FINISHMODE_Manual = "M";

	/**
	 * Set Finish Mode. Workflow Activity Finish Mode
	 */
	public void setFinishMode(String FinishMode) {
		if (FinishMode != null && FinishMode.length() > 1) {
			log.warning("Length > 1 - truncated");
			FinishMode = FinishMode.substring(0, 0);
		}
		set_Value("FinishMode", FinishMode);
	}

	/**
	 * Get Finish Mode. Workflow Activity Finish Mode
	 */
	public String getFinishMode() {
		return (String) get_Value("FinishMode");
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

	/** Set IsBatchTime */
	public void setIsBatchTime(boolean IsBatchTime) {
		set_Value("IsBatchTime", new Boolean(IsBatchTime));
	}

	/** Get IsBatchTime */
	public boolean isBatchTime() {
		Object oo = get_Value("IsBatchTime");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
	 * Set IsMilestone. This opertion will be used to report shop floor activity
	 * in the work order.
	 */
	public void setIsMilestone(boolean IsMilestone) {
		set_ValueE("IsMilestone", new Boolean(IsMilestone));
	}

	/**
	 * Get IsMilestone. This opertion will be used to report shop floor activity
	 * in the work order.
	 */
	public boolean isMilestone() {
		Object oo = get_ValueE("IsMilestone");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Is Subcontracting. The operation will be made in an external Work
	 * Center
	 */
	public void setIsSubcontracting(boolean IsSubcontracting) {
		set_Value("IsSubcontracting", new Boolean(IsSubcontracting));
	}

	/**
	 * Get Is Subcontracting. The operation will be made in an external Work
	 * Center
	 */
	public boolean isSubcontracting() {
		Object oo = get_Value("IsSubcontracting");
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** JoinElement AD_Reference_ID=301 */
	public static final int JOINELEMENT_AD_Reference_ID = 301;

	/** AND = A */
	public static final String JOINELEMENT_AND = "A";

	/** XOR = X */
	public static final String JOINELEMENT_XOR = "X";

	/**
	 * Set Join Element. Semantics for multiple incoming Transitions
	 */
	public void setJoinElement(String JoinElement) {
		if (JoinElement == null)
			throw new IllegalArgumentException("JoinElement is mandatory");
		if (JoinElement.equals("A") || JoinElement.equals("X"))
			;
		else
			throw new IllegalArgumentException("JoinElement Invalid value - "
					+ JoinElement + " - Reference_ID=301 - A - X");
		if (JoinElement.length() > 1) {
			log.warning("Length > 1 - truncated");
			JoinElement = JoinElement.substring(0, 0);
		}
		set_Value("JoinElement", JoinElement);
	}

	/**
	 * Get Join Element. Semantics for multiple incoming Transitions
	 */
	public String getJoinElement() {
		return (String) get_Value("JoinElement");
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
	 * Set Overlap Units. No. of prodcts you need to finish in a operation
	 * before you start the next one.
	 */
	public void setOverlapUnits(int OverlapUnits) {
		set_Value("OverlapUnits", new Integer(OverlapUnits));
	}

	/**
	 * Get Overlap Units. No. of prodcts you need to finish in a operation
	 * before you start the next one.
	 */
	public int getOverlapUnits() {
		Integer ii = (Integer) get_Value("OverlapUnits");
		if (ii == null)
			return 0;
		return ii.intValue();
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
	 * Set Mail Template. Text templates for mailings
	 */
	public void setR_MailText_ID(int R_MailText_ID) {
		if (R_MailText_ID <= 0)
			set_Value("R_MailText_ID", null);
		else
			set_Value("R_MailText_ID", new Integer(R_MailText_ID));
	}

	/**
	 * Get Mail Template. Text templates for mailings
	 */
	public int getR_MailText_ID() {
		Integer ii = (Integer) get_Value("R_MailText_ID");
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

	/** SplitElement AD_Reference_ID=301 */
	public static final int SPLITELEMENT_AD_Reference_ID = 301;

	/** AND = A */
	public static final String SPLITELEMENT_AND = "A";

	/** XOR = X */
	public static final String SPLITELEMENT_XOR = "X";

	/**
	 * Set Split Element. Semantics for multiple outgoing Transitions
	 */
	public void setSplitElement(String SplitElement) {
		if (SplitElement == null)
			throw new IllegalArgumentException("SplitElement is mandatory");
		if (SplitElement.equals("A") || SplitElement.equals("X"))
			;
		else
			throw new IllegalArgumentException("SplitElement Invalid value - "
					+ SplitElement + " - Reference_ID=301 - A - X");
		if (SplitElement.length() > 1) {
			log.warning("Length > 1 - truncated");
			SplitElement = SplitElement.substring(0, 0);
		}
		set_Value("SplitElement", SplitElement);
	}

	/**
	 * Get Split Element. Semantics for multiple outgoing Transitions
	 */
	public String getSplitElement() {
		return (String) get_Value("SplitElement");
	}

	/** StartMode AD_Reference_ID=303 */
	public static final int STARTMODE_AD_Reference_ID = 303;

	/** Automatic = A */
	public static final String STARTMODE_Automatic = "A";

	/** Manual = M */
	public static final String STARTMODE_Manual = "M";

	/**
	 * Set Start Mode. Workflow Activity Start Mode
	 */
	public void setStartMode(String StartMode) {
		if (StartMode != null && StartMode.length() > 1) {
			log.warning("Length > 1 - truncated");
			StartMode = StartMode.substring(0, 0);
		}
		set_Value("StartMode", StartMode);
	}

	/**
	 * Get Start Mode. Workflow Activity Start Mode
	 */
	public String getStartMode() {
		return (String) get_Value("StartMode");
	}

	/** SubflowExecution AD_Reference_ID=307 */
	public static final int SUBFLOWEXECUTION_AD_Reference_ID = 307;

	/** Asynchronously = A */
	public static final String SUBFLOWEXECUTION_Asynchronously = "A";

	/** Synchronously = S */
	public static final String SUBFLOWEXECUTION_Synchronously = "S";

	/**
	 * Set Subflow Execution. Mode how the sub-workflow is executed
	 */
	public void setSubflowExecution(String SubflowExecution) {
		if (SubflowExecution != null && SubflowExecution.length() > 1) {
			log.warning("Length > 1 - truncated");
			SubflowExecution = SubflowExecution.substring(0, 0);
		}
		set_Value("SubflowExecution", SubflowExecution);
	}

	/**
	 * Get Subflow Execution. Mode how the sub-workflow is executed
	 */
	public String getSubflowExecution() {
		return (String) get_Value("SubflowExecution");
	}

	/** Set Units Cycles */
	public void setUnitsCycles(BigDecimal UnitsCycles) {
		set_Value("UnitsCycles", UnitsCycles);
	}

	/** Get Units Cycles */
	public BigDecimal getUnitsCycles() {
		BigDecimal bd = (BigDecimal) get_Value("UnitsCycles");
		if (bd == null)
			return Env.ZERO;
		return bd;
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
	 * Set Wait Time. Time in minutes to wait (sleep)
	 */
	public void setWaitTime(int WaitTime) {
		set_Value("WaitTime", new Integer(WaitTime));
	}

	/**
	 * Get Wait Time. Time in minutes to wait (sleep)
	 */
	public int getWaitTime() {
		Integer ii = (Integer) get_Value("WaitTime");
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

	/** Workflow_ID AD_Reference_ID=174 */
	public static final int WORKFLOW_ID_AD_Reference_ID = 174;

	/**
	 * Set Workflow. Workflow or tasks
	 */
	public void setWorkflow_ID(int Workflow_ID) {
		if (Workflow_ID <= 0)
			set_Value("Workflow_ID", null);
		else
			set_Value("Workflow_ID", new Integer(Workflow_ID));
	}

	/**
	 * Get Workflow. Workflow or tasks
	 */
	public int getWorkflow_ID() {
		Integer ii = (Integer) get_Value("Workflow_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
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
