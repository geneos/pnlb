/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.beans.*;
import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Field Model.
 *  <p>
 *  Fields are a combination of AD_Field (the display attributes)
 *  and AD_Column (the storage attributes).
 *  <p>
 *  The Field maintains the current edited value. If the value is changed,
 *  it fire PropertyChange "FieldValue".
 *  If the background is changed the PropertyChange "FieldAttribute" is fired.
 *  <br>
 *  Usually editors listen to their fields.
 *
 *  @author Jorg Janke
 *  @version $Id: MField.java,v 1.89 2006/02/13 02:40:42 jjanke Exp $
 */
public final class MField 
	implements Serializable, Evaluatee
{
	/**
	 *  Field Constructor.
	 *  requires initField for complete instatanciation
	 *  @param vo ValueObjecy
	 */
	public MField (MFieldVO vo)
	{
		m_vo = vo;
		//  Set Attributes
		loadLookup();
		setError(false);
	}   //  MField

	/** Value Object                */
	private MFieldVO        m_vo;
	/** The Mnemonic				*/
	private char			m_mnemonic = 0;
	
	/**
	 *  Dispose
	 */
	protected void dispose()
	{
	//	log.fine( "MField.dispose = " + m_vo.ColumnName);
		m_propertyChangeListeners = null;
		if (m_lookup != null)
			m_lookup.dispose();
		m_lookup = null;
		m_vo.lookupInfo = null;
		m_vo = null;
	}   //  dispose


	/** Lookup for this field       */
	private Lookup			m_lookup = null;
	/** New Row / inserting         */
	private boolean			m_inserting = false;


	/** Max Display Length = 60		*/
	public static final int MAXDISPLAY_LENGTH = 60;

	/** The current value                   */
	private Object          m_value = null;
	/** The old to force Property Change    */
	private static Object   s_oldValue = new Object();
	/** The old/previous value              */
	private Object          m_oldValue = s_oldValue;
	/** Only fire Property Change if old value really changed   */
	private boolean         m_valueNoFire = true;
	/** Error Status                        */
	private boolean         m_error = false;
	/** Parent Check						*/
	private boolean			m_parentChecked = false;

	/** Property Change         			*/
	private PropertyChangeSupport m_propertyChangeListeners = new PropertyChangeSupport(this);
	/** PropertyChange Name 				*/
	public static final String  PROPERTY = "FieldValue";
	/** Indicator for new Value				*/
	public static final String  INSERTING = "FieldValueInserting";

	/** Error Value for HTML interface          */
	private String			m_errorValue = null;
	/** Error Value indicator for HTML interface    */
	private boolean			m_errorValueFlag = false;

	/**	Logger			*/
	private static CLogger	log = CLogger.getCLogger(MField.class);
	
	/**************************************************************************
	 *  Set Lookup for columns with lookup
	 */
	public void loadLookup()
	{
		if (!isLookup())
			return;
		log.config("(" + m_vo.ColumnName + ")");

		if (DisplayType.isLookup(m_vo.displayType))
		{
			if (m_vo.lookupInfo == null)
			{
				log.log(Level.SEVERE, "(" + m_vo.ColumnName + ") - No LookupInfo");
				return;
			}
			//	Prevent loading of CreatedBy/UpdatedBy
			if (m_vo.displayType == DisplayType.Table
				&& (m_vo.ColumnName.equals("CreatedBy") || m_vo.ColumnName.equals("UpdatedBy")) )
				m_vo.lookupInfo.IsCreadedUpdatedBy = true;
			//
			m_vo.lookupInfo.IsKey = isKey();
			MLookup ml = new MLookup (m_vo.lookupInfo, m_vo.TabNo);
			m_lookup = ml;
		}
		else if (m_vo.displayType == DisplayType.Location)   //  not cached
		{
			MLocationLookup ml = new MLocationLookup (m_vo.ctx, m_vo.WindowNo);
			m_lookup = ml;
		}
		else if (m_vo.displayType == DisplayType.Locator)
		{
			MLocatorLookup ml = new MLocatorLookup (m_vo.ctx, m_vo.WindowNo);
			m_lookup = ml;
		}
		else if (m_vo.displayType == DisplayType.Account)    //  not cached
		{
			MAccountLookup ma = new MAccountLookup (m_vo.ctx, m_vo.WindowNo);
			m_lookup = ma;
		}
		else if (m_vo.displayType == DisplayType.PAttribute)    //  not cached
		{
			MPAttributeLookup pa = new MPAttributeLookup (m_vo.ctx, m_vo.WindowNo);
			m_lookup = pa;
		}
	}   //  m_lookup

	/**
	 *  Wait until Load is complete
	 */
	public void lookupLoadComplete()
	{
		if (m_lookup == null)
			return;
		m_lookup.loadComplete();
	}   //  loadCompete

	/**
	 *  Get Lookup, may return null
	 *  @return lookup
	 */
	public Lookup getLookup()
	{
		return m_lookup;
	}   //  getLookup

	/**
	 *  Is this field a Lookup?.
	 *  @return true if lookup field
	 */
	public boolean isLookup()
	{
		boolean retValue = false;
		if (m_vo.IsKey)
			retValue = false;
	//	else if (m_vo.ColumnName.equals("CreatedBy") || m_vo.ColumnName.equals("UpdatedBy"))
	//		retValue = false;
		else if (DisplayType.isLookup(m_vo.displayType))
			retValue = true;
		else if (m_vo.displayType == DisplayType.Location
			|| m_vo.displayType == DisplayType.Locator
			|| m_vo.displayType == DisplayType.Account
			|| m_vo.displayType == DisplayType.PAttribute)
			retValue = true;

		return retValue;
	}   //  isLookup

	/**
	 *  Refresh Lookup if the lookup is unstable
	 *  @return true if lookup is validated
	 */
	public boolean refreshLookup()
	{
		//  if there is a validation string, the lookup is unstable
		if (m_lookup == null || m_lookup.getValidation().length() == 0)
			return true;
		//
		log.fine("(" + m_vo.ColumnName + ")");
		m_lookup.refresh();
		return m_lookup.isValidated();
	}   //  refreshLookup

	/**
	 *  Get a list of fields, this field is dependent on.
	 *  - for display purposes or
	 *  - for lookup purposes
	 *  @return ArrayList
	 */
	public ArrayList<String> getDependentOn()
	{
		ArrayList<String> list = new ArrayList<String>();
		//  Display
		parseDepends(list, m_vo.DisplayLogic);
		parseDepends(list, m_vo.ReadOnlyLogic);
		//  Lookup
		if (m_lookup != null)
			parseDepends(list, m_lookup.getValidation());
		//
		if (list.size() > 0 && CLogMgt.isLevelFiner())
		{
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < list.size(); i++)
				sb.append(list.get(i)).append(" ");
			log.finer("(" + m_vo.ColumnName + ") " + sb.toString());
		}
		return list;
	}   //  getDependentOn

	/**
	 *  Parse String and add variables with @ to the list.
	 *  @param list list to be added to
	 *  @param parseString string to parse for variables
	 */
	private void parseDepends (ArrayList<String> list, String parseString)
	{
		if (parseString == null || parseString.length() == 0)
			return;
	//	log.fine( "MField.parseDepends", parseString);
		String s = parseString;
		//  while we have variables
		while (s.indexOf("@") != -1)
		{
			int pos = s.indexOf("@");
			s = s.substring(pos+1);
			pos = s.indexOf("@");
			if (pos == -1)
				continue;	//	error number of @@ not correct
			String variable = s.substring(0, pos);
			s = s.substring(pos+1);
		//	log.fine( variable);
			list.add(variable);
		}
	}   //  parseDepends

	
	/**************************************************************************
	 *	Set Error.
	 *  Used by editors to set the color
	 *  @param error true if error
	 */
	public void setError (boolean error)
	{
		m_error = error;
	}	//	setBackground

	/**
	 *	Get Background Error.
	 *  @return error
	 */
	public boolean isError()
	{
		return m_error;
	}	//	isError


	/**
	 *	Is it Mandatory to enter for user?
	 *  Mandatory checking is dome in MTable.getMandatory
	 *  @param checkContext - check environment (requires correct row position)
	 *  @return true if mandatory
	 */
	public boolean isMandatory (boolean checkContext)
	{
		//  Not mandatory
		if (!m_vo.IsMandatory || isVirtualColumn())
			return false;

		//  Numeric Keys and Created/Updated as well as 
		//	DocumentNo/Value/ASI ars not mandatory (persistency layer manages them)
		if ((m_vo.IsKey && m_vo.ColumnName.endsWith("_ID"))
				|| m_vo.ColumnName.startsWith("Created") || m_vo.ColumnName.startsWith("Updated")
				|| m_vo.ColumnName.equals("Value") 
				|| m_vo.ColumnName.equals("DocumentNo")
				|| m_vo.ColumnName.equals("M_AttributeSetInstance_ID"))	//	0 is valid
			return false;

		//  Mandatory if displayed
		return isDisplayed (checkContext);
	}	//	isMandatory

	/**
	 *	Is it Editable - checks IsActive, IsUpdateable, and isDisplayed
	 *  @param checkContext if true checks Context for Active, IsProcessed, LinkColumn
	 *  @return true, if editable
	 */
	public boolean isEditable (boolean checkContext)
	{
		if (isVirtualColumn())
			return false;
		//  Fields always enabled (are usually not updateable)
		if (m_vo.ColumnName.equals("Posted")
			|| (m_vo.ColumnName.equals("Record_ID") && m_vo.displayType == DisplayType.Button))	//  Zoom
			return true;

		//  Fields always updareable
		if (m_vo.IsAlwaysUpdateable)      //  Zoom
			return true;
		
		//  Tab or field is R/O
		if (m_vo.tabReadOnly || m_vo.IsReadOnly)
		{
			log.finest(m_vo.ColumnName + " NO - TabRO=" + m_vo.tabReadOnly + ", FieldRO=" + m_vo.IsReadOnly);
			return false;
		}

		//	Not Updateable - only editable if new updateable row
		if (!m_vo.IsUpdateable && !m_inserting)
		{
			log.finest(m_vo.ColumnName + " NO - FieldUpdateable=" + m_vo.IsUpdateable);
			return false;
		}

		//	Field is the Link Column of the tab
		if (m_vo.ColumnName.equals(Env.getContext(m_vo.ctx, m_vo.WindowNo, m_vo.TabNo, "LinkColumnName")))
		{
			log.finest(m_vo.ColumnName + " NO - LinkColumn");
			return false;
		}

		//	Role Access & Column Access			
		if (checkContext)
		{
			int AD_Client_ID = Env.getContextAsInt(m_vo.ctx, m_vo.WindowNo, m_vo.TabNo, "AD_Client_ID");
			int AD_Org_ID = Env.getContextAsInt(m_vo.ctx, m_vo.WindowNo, m_vo.TabNo, "AD_Org_ID");
			String keyColumn = Env.getContext(m_vo.ctx, m_vo.WindowNo, m_vo.TabNo, "KeyColumnName");
			int Record_ID = Env.getContextAsInt(m_vo.ctx, m_vo.WindowNo, m_vo.TabNo, keyColumn);
			int AD_Table_ID = m_vo.AD_Table_ID;
			if (!MRole.getDefault(m_vo.ctx, false).canUpdate(
				AD_Client_ID, AD_Org_ID, AD_Table_ID, Record_ID, false))
				return false;
			if (!MRole.getDefault(m_vo.ctx, false).isColumnAccess(AD_Table_ID, m_vo.AD_Column_ID, false))
				return false;
		}
			
		//  Do we have a readonly rule
		if (checkContext && m_vo.ReadOnlyLogic.length() > 0)
		{
			boolean retValue = !Evaluator.evaluateLogic(this, m_vo.ReadOnlyLogic);
			log.finest(m_vo.ColumnName + " R/O(" + m_vo.ReadOnlyLogic + ") => R/W-" + retValue);
			if (!retValue)
				return false;
		}

		//  Always editable if Active
		if (m_vo.ColumnName.equals("Processing")
				|| m_vo.ColumnName.equals("PaymentRule")
				|| m_vo.ColumnName.equals("DocAction")
				|| m_vo.ColumnName.equals("GenerateTo"))
			return true;

		//  Record is Processed	***	
		if (checkContext && Env.getContext(m_vo.ctx, m_vo.WindowNo, "Processed").equals("Y"))
			return false;

		//  IsActive field is editable, if record not processed
		if (m_vo.ColumnName.equals("IsActive"))
			return true;

		//  Record is not Active
		if (checkContext && !Env.getContext(m_vo.ctx, m_vo.WindowNo, "IsActive").equals("Y"))
			return false;

		//  ultimately visibily decides
		return isDisplayed (checkContext);
	}	//	isEditable

	/**
	 *  Set Inserting (allows to enter not updateable fields).
	 *  Reset when setting the Field Value
	 *  @param inserting true if inserting
	 */
	public void setInserting (boolean inserting)
	{
		m_inserting = inserting;
	}   //  setInserting

	
	/**************************************************************************
	 *	Create default value.
	 *  <pre>
	 *		(a) Key/Parent/IsActive/SystemAccess
	 *      (b) SQL Default
	 *		(c) Column Default		//	system integrity
	 *      (d) User Preference
	 *		(e) System Preference
	 *		(f) DataType Defaults
	 *
	 *  Don't default from Context => use explicit defaultValue
	 *  (would otherwise copy previous record)
	 *  </pre>
	 *  @return default value or null
	 */
	public Object getDefault()
	{
		/**
		 *  (a) Key/Parent/IsActive/SystemAccess
		 */

		//	No defaults for these fields
		if (m_vo.IsKey || m_vo.displayType == DisplayType.RowID || m_vo.displayType == DisplayType.Binary)
			return null;
		//	Set Parent to context if not explitly set
		if (isParentValue()
			&& (m_vo.DefaultValue == null || m_vo.DefaultValue.length() == 0))
		{
			String parent = Env.getContext(m_vo.ctx, m_vo.WindowNo, m_vo.ColumnName);
			log.fine("[Parent] " + m_vo.ColumnName + "=" + parent);
			return createDefault(parent);
		}
		//	Always Active
		if (m_vo.ColumnName.equals("IsActive"))
		{
			log.fine("[IsActive] " + m_vo.ColumnName + "=Y");
			return "Y";
		}
		
		//	Set Client & Org to System, if System access
		if (X_AD_Table.ACCESSLEVEL_SystemOnly.equals(Env.getContext(m_vo.ctx, m_vo.WindowNo, m_vo.TabNo, "AccessLevel"))
			&& (m_vo.ColumnName.equals("AD_Client_ID") || m_vo.ColumnName.equals("AD_Org_ID")))
		{
			log.fine("[SystemAccess] " + m_vo.ColumnName + "=0");
			return new Integer(0);
		}
		//	Set Org to System, if Client access
		else if (X_AD_Table.ACCESSLEVEL_SystemPlusClient.equals(Env.getContext(m_vo.ctx, m_vo.WindowNo, m_vo.TabNo, "AccessLevel"))
			&& m_vo.ColumnName.equals("AD_Org_ID"))
		{
			log.fine("[ClientAccess] " + m_vo.ColumnName + "=0");
			return new Integer(0);
		}

		/**
		 *  (b) SQL Statement (for data integity & consistency)
		 */
		String	defStr = "";
		if (m_vo.DefaultValue.startsWith("@SQL="))
		{
			String sql = m_vo.DefaultValue.substring(5);			//	w/o tag
			sql = Env.parseContext(m_vo.ctx, m_vo.WindowNo, sql, false, true);	//	replace variables
			if (sql.equals(""))
			{
				log.log(Level.SEVERE, "(" + m_vo.ColumnName + ") - Default SQL variable parse failed: "
					+ m_vo.DefaultValue);
			}
			else
			{
				try
				{
					PreparedStatement stmt = DB.prepareStatement(sql, null);
					ResultSet rs = stmt.executeQuery();
					if (rs.next())
						defStr = rs.getString(1);
					else
						log.log(Level.SEVERE, "(" + m_vo.ColumnName + ") - no Result: " + sql);
					rs.close();
					stmt.close();
				}
				catch (SQLException e)
				{
					log.log(Level.SEVERE, "(" + m_vo.ColumnName + ") " + sql, e);
				}
			}
			if (!defStr.equals(""))
			{
				log.fine("[SQL] " + m_vo.ColumnName + "=" + defStr);
				return createDefault(defStr);
			}
		}	//	SQL Statement

		/**
		 * 	(c) Field DefaultValue		=== similar code in AStartRPDialog.getDefault ===
		 */
		if (!m_vo.DefaultValue.equals("") && !m_vo.DefaultValue.startsWith("@SQL="))
		{
			defStr = "";
			//	It is one or more variables/constants
			StringTokenizer st = new StringTokenizer(m_vo.DefaultValue, ",;", false);
			while (st.hasMoreTokens())
			{
				defStr = st.nextToken().trim();
				if (defStr.equals("@SysDate@"))				//	System Time
					return new Timestamp (System.currentTimeMillis());
				else if (defStr.indexOf('@') != -1)			//	it is a variable
					defStr = Env.getContext(m_vo.ctx, m_vo.WindowNo, defStr.replace('@',' ').trim());
				else if (defStr.indexOf("'") != -1)			//	it is a 'String'
					defStr = defStr.replace('\'', ' ').trim();

				if (!defStr.equals(""))
				{
					log.fine("[DefaultValue] " + m_vo.ColumnName + "=" + defStr);
					return createDefault(defStr);
				 }
			}	//	while more Tokens
		}	//	Default value

		/**
		 *	(d) Preference (user) - P|
		 */
		defStr = Env.getPreference (m_vo.ctx, m_vo.AD_Window_ID, m_vo.ColumnName, false);
		if (!defStr.equals(""))
		{
			log.fine("[UserPreference] " + m_vo.ColumnName + "=" + defStr);
			return createDefault(defStr);
		}

		/**
		 *	(e) Preference (System) - # $
		 */
		defStr = Env.getPreference (m_vo.ctx, m_vo.AD_Window_ID, m_vo.ColumnName, true);
		if (!defStr.equals(""))
		{
			log.fine("[SystemPreference] " + m_vo.ColumnName + "=" + defStr);
			return createDefault(defStr);
		}

		/**
		 *	(f) DataType defaults
		 */

		//	Button to N
		if (m_vo.displayType == DisplayType.Button && !m_vo.ColumnName.endsWith("_ID"))
		{
			log.fine("[Button=N] " + m_vo.ColumnName);
			return "N";
		}
		//	CheckBoxes default to No
		if (m_vo.displayType == DisplayType.YesNo)
		{
			log.fine("[YesNo=N] " + m_vo.ColumnName);
			return "N";
		}
		//  lookups with one value
	//	if (DisplayType.isLookup(m_vo.displayType) && m_lookup.getSize() == 1)
	//	{
	//		/** @todo default if only one lookup value */
	//	}
		//  IDs remain null
		if (m_vo.ColumnName.endsWith("_ID"))
		{
			log.fine("[ID=null] "  + m_vo.ColumnName);
			return null;
		}
		//  actual Numbers default to zero
		if (DisplayType.isNumeric(m_vo.displayType))
		{
			log.fine("[Number=0] " + m_vo.ColumnName);
			return createDefault("0");
		}

		/**
		 *  No resolution
		 */
		log.fine("[NONE] " + m_vo.ColumnName);
		return null;
	}	//	getDefault

	/**
	 *	Create Default Object type.
	 *  <pre>
	 *		Integer 	(IDs, Integer)
	 *		BigDecimal 	(Numbers)
	 *		Timestamp	(Dates)
	 *		Boolean		(YesNo)
	 *		default: String
	 *  </pre>
	 *  @param value string
	 *  @return type dependent converted object
	 */
	private Object createDefault (String value)
	{
		//	true NULL
		if (value == null || value.toString().length() == 0)
			return null;
		//	see also MTable.readData
		try
		{
			//	IDs & Integer & CreatedBy/UpdatedBy
			if (m_vo.ColumnName.endsWith("atedBy")
					|| m_vo.ColumnName.endsWith("_ID"))
			{
				try	//	defaults -1 => null
				{
					int ii = Integer.parseInt(value);
					if (ii < 0)
						return null;
					return new Integer(ii);
				}
				catch (Exception e)
				{
					log.warning("Cannot parse: " + value + " - " + e.getMessage());
				}
				return new Integer(0);
			}
			//	Integer
			if (m_vo.displayType == DisplayType.Integer)
				return new Integer(value);
			
			//	Number
			if (DisplayType.isNumeric(m_vo.displayType))
				return new BigDecimal(value);
			
			//	Timestamps
			if (DisplayType.isDate(m_vo.displayType))
			{
				java.util.Date date =  DisplayType.getDateFormat_JDBC().parse (value);
				return new Timestamp (date.getTime());
			}
			
			//	Boolean
			if (m_vo.displayType == DisplayType.YesNo)
				return new Boolean ("Y".equals(value));
			
			//	Default
			return value;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, m_vo.ColumnName + " - " + e.getMessage());
		}
		return null;
	}	//	createDefault

	/**
	 *  Validate initial Field Value.
	 * 	Called from MTab.dataNew and MTab.setCurrentRow when inserting
	 *  @return true if valid
	 */
	public boolean validateValue()
	{
		//  null
		if (m_value == null || m_value.toString().length() == 0)
		{
			if (isMandatory(true))
			{
				m_error = true;
				return false;
			}
			else
				return true;
		}
		
		//	Search not cached
		if (getDisplayType() == DisplayType.Search && m_lookup != null)
		{
			// need to re-set invalid values - OK BPartner in PO Line - not OK SalesRep in Invoice
			if (m_lookup.getDirect(m_value, false, true) == null)
			{
				log.finest(m_vo.ColumnName + " Serach not valid - set to null");
				setValue(null, m_inserting);
				m_error = true;
				return false;
			}
			return true;
		} 

		//  cannot be validated
		if (!isLookup()
			|| m_lookup.containsKey(m_value))
			return true;
		//	it's not null, a lookup and does not have the key
		if (isKey() || isParentValue())		//	parents/ket are not validated
			return true;	
			
		log.finest(m_vo.ColumnName + " - set to null");
		setValue(null, m_inserting);
		m_error = true;
		return false;
	}   //  validateValue


	/**************************************************************************
	 *	Is the Column Visible ?
	 *  @param checkContext - check environment (requires correct row position)
	 *  @return true, if visible
	 */
	public boolean isDisplayed (boolean checkContext)
	{
		//  ** static content **
		//  not displayed
		if (!m_vo.IsDisplayed)
			return false;
		//  no restrictions
		if (m_vo.DisplayLogic.equals(""))
			return true;

		//  ** dynamic content **
		if (checkContext)
		{
			boolean retValue = Evaluator.evaluateLogic(this, m_vo.DisplayLogic);
			log.finest(m_vo.ColumnName 
				+ " (" + m_vo.DisplayLogic + ") => " + retValue);
			return retValue;
		}
		return true;
	}	//	isDisplayed

	/**
	 * 	Get Variable Value (Evaluatee)
	 *	@param variableName name
	 *	@return value
	 */
	public String get_ValueAsString (String variableName)
	{
		return Env.getContext (m_vo.ctx, m_vo.WindowNo, variableName, true);
	}	//	get_ValueAsString


	/**
	 *	Add Display Dependencies to given List.
	 *  Source: DisplayLogic
	 *  @param list list to be added to
	 */
	public void addDependencies (ArrayList<String> list)
	{
		//	nothing to parse
		if (!m_vo.IsDisplayed || m_vo.DisplayLogic.equals(""))
			return;

		StringTokenizer logic = new StringTokenizer(m_vo.DisplayLogic.trim(), "&|", false);

		while (logic.hasMoreTokens())
		{
			StringTokenizer st = new StringTokenizer(logic.nextToken().trim(), "!=^", false);
			while (st.hasMoreTokens())
			{
				String tag = st.nextToken().trim();					//	get '@tag@'
				//	Do we have a @variable@ ?
				if (tag.indexOf('@') != -1)
				{
					tag = tag.replace('@', ' ').trim();				//	strip 'tag'
					//	Add columns (they might not be a column, but then it is static)
					if (!list.contains(tag))
						list.add(tag);
				}
			}
		}
	}	//	addDependencies

	
	/**************************************************************************
	 *  Get Column Name
	 *  @return column name
	 */
	public String getColumnName()
	{
		return m_vo.ColumnName;
	}	//	getColumnName
	
	/**
	 *  Get Column Name or SQL .. with/without AS
	 *  @param withAS include AS ColumnName for virtual columns in select statements
	 *  @return column name
	 */
	public String getColumnSQL(boolean withAS)
	{
		if (m_vo.ColumnSQL != null && m_vo.ColumnSQL.length() > 0)
		{
			if (withAS)
				return m_vo.ColumnSQL + " AS " + m_vo.ColumnName;
			else
				return m_vo.ColumnSQL;
		}
		return m_vo.ColumnName;
	}	//	getColumnSQL

	/**
	 *  Is Virtual Column
	 *  @return column is virtual
	 */
	public boolean isVirtualColumn()
	{
		if (m_vo.ColumnSQL != null && m_vo.ColumnSQL.length() > 0)
			return true; 
		return false;
	}	//	isColumnVirtual
	
	public String getHeader()
	{
		return m_vo.Header;
	}
	public int getDisplayType()
	{
		return m_vo.displayType;
	}
	public int getAD_Reference_Value_ID()
	{
		return m_vo.AD_Reference_Value_ID;
	}
	public int getAD_Window_ID()
	{
		return m_vo.AD_Window_ID;
	}
	public int getWindowNo()
	{
		return m_vo.WindowNo;
	}
	public int getAD_Column_ID()
	{
		return m_vo.AD_Column_ID;
	}
	public int getDisplayLength()
	{
		return m_vo.DisplayLength;
	}
	public boolean isSameLine()
	{
		return m_vo.IsSameLine;
	}
	public boolean isDisplayed()
	{
		return m_vo.IsDisplayed;
	}
	public String getDisplayLogic()
	{
		return m_vo.DisplayLogic;
	}
	public String getDefaultValue()
	{
		return m_vo.DefaultValue;
	}
	public boolean isReadOnly()
	{
		if (isVirtualColumn())
			return true;
		return m_vo.IsReadOnly;
	}
	public boolean isUpdateable()
	{
		if (isVirtualColumn())
			return false;
		return m_vo.IsUpdateable;
	}
	public boolean isAlwaysUpdateable()
	{
		if (isVirtualColumn())
			return false;
		return m_vo.IsAlwaysUpdateable;
	}
	public boolean isHeading()
	{
		return m_vo.IsHeading;
	}
	public boolean isFieldOnly()
	{
		return m_vo.IsFieldOnly;
	}
	public boolean isEncryptedField()
	{
		return m_vo.IsEncryptedField;
	}
	public boolean isEncryptedColumn()
	{
		return m_vo.IsEncryptedColumn;
	}
	public boolean isSelectionColumn()
	{
		return m_vo.IsSelectionColumn;
	}
	public String getObscureType()
	{
		return m_vo.ObscureType;
	}
	public int getSortNo()
	{
		return m_vo.SortNo;
	}
	public int getFieldLength()
	{
		return m_vo.FieldLength;
	}
	public String getVFormat()
	{
		return m_vo.VFormat;
	}
	public String getValueMin()
	{
		return m_vo.ValueMin;
	}
	public String getValueMax()
	{
		return m_vo.ValueMax;
	}
	public String getFieldGroup()
	{
		return m_vo.FieldGroup;
	}
	public boolean isKey()
	{
		return m_vo.IsKey;
	}
	public boolean isParentColumn()
	{
			return m_vo.IsParent;
	}
	public boolean isParentValue()
	{
		if (m_parentChecked)
			return m_vo.IsParent;
		if (!DisplayType.isID(m_vo.displayType) || m_vo.TabNo == 0)
			m_vo.IsParent = false;
		else 
		{
			String LinkColumnName = Env.getContext(m_vo.ctx, m_vo.WindowNo, m_vo.TabNo, "LinkColumnName");
			if (LinkColumnName.length() == 0)
				;
			else 
				m_vo.IsParent = m_vo.ColumnName.equals(LinkColumnName);
			if (m_vo.IsParent)
				log.config(m_vo.IsParent
					+ " - Link(" + LinkColumnName + ", W=" + m_vo.WindowNo + ",T=" + m_vo.TabNo
					+ ") = " + m_vo.ColumnName);
		}
		m_parentChecked = true;
		return m_vo.IsParent;
	}
	public String getCallout()
	{
		return m_vo.Callout;
	}
	public int getAD_Process_ID()
	{
		return m_vo.AD_Process_ID;
	}
	public String getDescription()
	{
		return m_vo.Description;
	}
	public String getHelp()
	{
		return m_vo.Help;
	}
	public MFieldVO getVO()
	{
		return m_vo;
	}

	/**
	 *  Is this a long (string/text) field (over 60/2=30 characters)
	 *  @return true if long field
	 */
	public boolean isLongField()
	{
	//	if (m_vo.displayType == DisplayType.String 
	//		|| m_vo.displayType == DisplayType.Text 
	//		|| m_vo.displayType == DisplayType.Memo
	//		|| m_vo.displayType == DisplayType.TextLong
	//		|| m_vo.displayType == DisplayType.Image)
		return (m_vo.DisplayLength >= MAXDISPLAY_LENGTH/2);
	//	return false;
	}   //  isLongField

	/**
	 *  Set Value to null.
	 *  <p>
	 *  Do not update context - called from MTab.setCurrentRow
	 *  Send Bean PropertyChange if there is a change
	 */
	public void setValue ()
	{
	//	log.fine( "MField.setValue - " + ColumnName + "=" + newValue);
		if (m_valueNoFire)      //  set the old value
			m_oldValue = m_value;
		m_value = null;
		m_inserting = false;
		m_error = false;        //  reset error

		//  Does not fire, if same value
		m_propertyChangeListeners.firePropertyChange(PROPERTY, m_oldValue, m_value);
	//	m_propertyChangeListeners.firePropertyChange(PROPERTY, s_oldValue, null);
	}   //  setValue


	/**
	 *  Set Value.
	 *  <p>
	 *  Update context, if not text or RowID;
	 *  Send Bean PropertyChange if there is a change
	 *  @param newValue new value
	 *  @param inserting true if inserting
	 */
	public void setValue (Object newValue, boolean inserting)
	{
	//	log.fine( "MField.setValue - " + ColumnName + "=" + newValue);
		if (m_valueNoFire)      //  set the old value
			m_oldValue = m_value;
		m_value = newValue;
		m_inserting = inserting;
		m_error = false;        //  reset error

		//	Set Context
		if (m_vo.displayType == DisplayType.Text 
			|| m_vo.displayType == DisplayType.Memo
			|| m_vo.displayType == DisplayType.TextLong
			|| m_vo.displayType == DisplayType.Binary
			|| m_vo.displayType == DisplayType.RowID)
			;	//	ignore
		else if (newValue instanceof Boolean)
			Env.setContext(m_vo.ctx, m_vo.WindowNo, m_vo.ColumnName, 
				((Boolean)newValue).booleanValue());
		else if (newValue instanceof Timestamp)
			Env.setContext(m_vo.ctx, m_vo.WindowNo, m_vo.ColumnName, (Timestamp)m_value);
		else
			Env.setContext(m_vo.ctx, m_vo.WindowNo, m_vo.ColumnName, 
				m_value==null ? null : m_value.toString());
		
		//  Does not fire, if same value
		Object oldValue = m_oldValue;
		if (inserting)
			oldValue = INSERTING;
		m_propertyChangeListeners.firePropertyChange(PROPERTY, oldValue, m_value);
	}   //  setValue

	/**
	 *  Get Value
	 *  @return current value
	 */
	public Object getValue()
	{
		return m_value;
	}   //  getValue

	/**
	 *  Set old/previous Value.
	 *  (i.e. don't fire Property change)
	 *  Used by VColor.setField
	 *  @param value if false property change will always be fires
	 */
	public void setValueNoFire (boolean value)
	{
		m_valueNoFire = value;
	}   //  setOldValue

	/**
	 *  Get old/previous Value.
	 * 	Called from MTab.processCallout
	 *  @return old value
	 */
	public Object getOldValue()
	{
		return m_oldValue;
	}   //  getOldValue

	/**
	 *  Set Error Value (the value, which cuased some Error)
	 *  @param errorValue error message
	 */
	public void setErrorValue (String errorValue)
	{
		m_errorValue = errorValue;
		m_errorValueFlag = true;
	}   //  setErrorValue

	/**
	 *  Get Error Value (the value, which cuased some Error) <b>AND</b> reset it to null
	 *  @return error value
	 */
	public String getErrorValue ()
	{
		String s = m_errorValue;
		m_errorValue = null;
		m_errorValueFlag = false;
		return s;
	}   //  getErrorValue

	/**
	 *  Return true, if value has Error (for HTML interface) <b>AND</b> reset it to false
	 *  @return has error
	 */
	public boolean isErrorValue()
	{
		boolean b = m_errorValueFlag;
		m_errorValueFlag = false;
		return b;
	}   //  isErrorValue

	/**
	 *  Overwrite default DisplayLength
	 *  @param length new length
	 */
	public void setDisplayLength (int length)
	{
		m_vo.DisplayLength = length;
	}   //  setDisplayLength

	/**
	 *  Overwrite Displayed
	 *  @param displayed trie if displayed
	 */
	public void setDisplayed (boolean displayed)
	{
		m_vo.IsDisplayed = displayed;
	}   //  setDisplayed

	
	/**
	 * 	Create Mnemonic for field
	 *	@return no for r/o, client, org, document no
	 */
	public boolean isCreateMnemonic()
	{
		if (isReadOnly() 
			|| m_vo.ColumnName.equals("AD_Client_ID")
			|| m_vo.ColumnName.equals("AD_Org_ID")
			|| m_vo.ColumnName.equals("DocumentNo"))
			return false;
		return true;
	}
	
	/**
	 * 	Get Label Mnemonic
	 *	@return Mnemonic
	 */
	public char getMnemonic()
	{
		return m_mnemonic;
	}	//	getMnemonic
	
	/**
	 * 	Set Label Mnemonic
	 *	@param mnemonic Mnemonic
	 */
	public void setMnemonic (char mnemonic)
	{
		m_mnemonic = mnemonic;
	}	//	setMnemonic

	
	/**
	 *  String representation
	 *  @return string representation
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MField[");
		sb.append(m_vo.ColumnName).append("=").append(m_value).append("]");
		return sb.toString();
	}   //  toString

	/**
	 *  Extended String representation
	 *  @return string representation
	 */
	public String toStringX()
	{
		StringBuffer sb = new StringBuffer("MField[");
		sb.append(m_vo.ColumnName).append("=").append(m_value)
			.append(",DisplayType=").append(getDisplayType())
			.append("]");
		return sb.toString();
	}   //  toStringX


	/*************************************************************************
	 *  Remove Property Change Listener
	 *  @param l listener
	 */
	public synchronized void removePropertyChangeListener(PropertyChangeListener l)
	{
		m_propertyChangeListeners.removePropertyChangeListener(l);
	}

	/**
	 *  Add Property Change Listener
	 *  @param l listener
	 */
	public synchronized void addPropertyChangeListener(PropertyChangeListener l)
	{
		m_propertyChangeListeners.addPropertyChangeListener(l);
	}
	
	
	/**************************************************************************
	 * 	Create Fields.
	 * 	Used by APanel.cmd_find  and  Viewer.cmd_find
	 * 	@param AD_Tab_ID tab
	 * 	@return array of all fields in display order
	 */
	public static MField[] createFields (Properties ctx, int WindowNo, int TabNo,
		 int AD_Tab_ID)
	{
		ArrayList<MFieldVO> listVO = new ArrayList<MFieldVO>();
		int AD_Window_ID = 0;
		boolean readOnly = false;
		
		String sql = MFieldVO.getSQL(ctx);
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Tab_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MFieldVO vo = MFieldVO.create(ctx, WindowNo, TabNo, AD_Window_ID, readOnly, rs);
				listVO.add(vo);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		
		//
		MField[] retValue = new MField[listVO.size()];
		for (int i = 0; i < listVO.size(); i++)
			retValue[i] = new MField ((MFieldVO)listVO.get(i));
		return retValue;
	}	//	createFields
	
        //begin vpj-cd e-evolution 08/09/2005
        /**
         *  Get the id tab include 
         *  @return id Tab
	 */
	public int getIncluded_Tab_ID ()
	{
		
		return m_vo.Included_Tab_ID;
	} 
        //end vpj-cd e-evolution 08/09/2005
				
}   //  MField
