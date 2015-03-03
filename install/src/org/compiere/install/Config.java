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
package org.compiere.install;

import java.sql.*;

import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Configuration Setup and Test
 *	
 *  @author Jorg Janke
 *  @version $Id: Config.java,v 1.3 2005/09/08 21:54:12 jjanke Exp $
 */
public abstract class Config
{
	/**
	 * 	Configuration
	 */
	public Config (ConfigurationData data)
	{
		super ();
		p_data = data;
	}	//	Config

	/**	Configuration Data			*/
	protected ConfigurationData 	p_data = null;
	/**	Logger	*/
	static CLogger	log	= CLogger.getCLogger (Config.class);
	
	
	/**
	 * 	Initialize
	 */
	abstract void init();
	
	/**
	 * 	Test
	 *	@return error message or null of OK
	 */
	abstract String test();

	/**
	 * 	Discover Databases.
	 * 	To be overwritten by database configs
	 *	@param selected selected database
	 *	@return array of databases
	 */
	public String[] discoverDatabases(String selected)
	{
		return new String[]{};
	}	//	discoverDatabases
	
	/**
	 * 	Get Panel
	 *	@return panel
	 */
	protected ConfigurationPanel getPanel()
	{
		return p_data.p_panel;
	}	//	getPanel
	
	/**
	 * 	Set Configuration Property
	 *	@param key key
	 *	@param value value
	 */
	protected void setProperty(String key, String value)
	{
		p_data.p_properties.setProperty(key, value);
	}	//	setProperty

	
	/**
	 * 	UI Signal OK
	 *	@param cb ckeck box
	 *	@param resString resource string key
	 *	@param pass true if test passed
	 *	@param critical true if critial
	 *	@param errorMsg error Message
	 */
	void signalOK (CCheckBox cb, String resString, 
		boolean pass, boolean critical, String errorMsg)
	{
		p_data.p_panel.signalOK(cb, resString, pass, critical, errorMsg);
	}	//	signalOK

	/**
	 * 	Get Web Store Context Names separared by ,
	 *	@param con connection
	 *	@return String of Web Store Names - e.g. /wstore
	 */
	protected String getWebStores(Connection con)
	{
		String sql = "SELECT WebContext FROM W_Store WHERE IsActive='Y'";
		Statement stmt = null;
		StringBuffer result = new StringBuffer();
		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next ())
			{
				if (result.length() > 0)
					result.append(",");
				result.append(rs.getString(1));
			}
			rs.close ();
			stmt.close ();
			stmt = null;
		}
		catch (Exception e)
		{
			log.severe(e.toString());
		}
		try
		{
			if (stmt != null)
				stmt.close ();
			stmt = null;
		}
		catch (Exception e)
		{
			stmt = null;
		}
		return result.toString();
	}	//	getWebStores	

}	//	Config
