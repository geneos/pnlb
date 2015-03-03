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
package org.compiere.util;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.plaf.CompiereLookAndFeel;
import org.compiere.plaf.CompiereThemeBlueMetal;

/**
 *	Load & Save INI Settings fopm property file
 *	Initiated in Compiere.startup
 *	Settings activated in ALogin.getIni
 *
 *  @author     Jorg Janke
 *  @version    $Id: Ini.java,v 1.47 2005/12/17 19:56:58 jjanke Exp $
 */
public final class Ini implements Serializable
{
	/** Property file name				*/
	public static final String	COMPIERE_PROPERTY_FILE = "Compiere.properties";

	//	Property Constants and Default Values
	public static final String	P_UID = 			"ApplicationUserID";
	private static final String	DEFAULT_UID = 		"System";
	//
	public static final String	P_PWD = 			"ApplicationPassword";
	private static final String	DEFAULT_PWD = 		"System";
	//
	public static final String	P_STORE_PWD = 		"StorePassword";
	    
    /**
     * Change Store Pwd default value to false (no store)
     * @author Ezequiel Scott at Zynnia
     * 
     * private static final boolean DEFAULT_STORE_PWD = true;
     */
    private static final boolean DEFAULT_STORE_PWD = false;
    
    //
	public static final String	P_TRACELEVEL = 		"TraceLevel";
	
    /**
     * Change Tracelevel default value to record ALL events
     * @author Ezequiel Scott at Zynnia
     * 
     * private static final String DEFAULT_TRACELEVEL = "WARNING";
     */
    private static final String DEFAULT_TRACELEVEL = "ALL";
    
    public static final String	P_TRACEFILE = 		"TraceFile";
    
    /**
     * Change Store Tracefile default value to true (store log)
     * @author Ezequiel Scott at Zynnia
     * 
     * private static final boolean DEFAULT_TRACEFILE = false;
     */
    private static final boolean DEFAULT_TRACEFILE = true;
   
    //
	public static final String 	P_LANGUAGE = 		"Language";
	private static final String DEFAULT_LANGUAGE = 	Language.getName
		(System.getProperty("user.language") + "_" + System.getProperty("user.country"));
	//
	public static final String 	P_INI = 			"FileNameINI";
	private static final String DEFAULT_INI = 		"";
	//
	public static final String	P_CONNECTION =		"Connection";
	private static final String	DEFAULT_CONNECTION = "";
	//
	public static final String  P_CONTEXT = 		"DataSource";
	private static final String	DEFAULT_CONTEXT	= 	"java:compiereDB";
	//
	public static final String	P_UI_LOOK =			"UILookFeel";
	private static final String	DEFAULT_UI_LOOK =	CompiereLookAndFeel.NAME;
	//
	public static final String	P_UI_THEME =		"UITheme";
	private static final String	DEFAULT_UI_THEME =	CompiereThemeBlueMetal.NAME;
	public static final String	P_UI_FLAT =			"UIFlat";
	private static final boolean DEFAULT_UI_FLAT =	false;
	//
	public static final String  P_A_COMMIT =		"AutoCommit";
	private static final boolean DEFAULT_A_COMMIT =	true;
	//
	public static final String	P_A_LOGIN =			"AutoLogin";
	private static final boolean DEFAULT_A_LOGIN =	false;
	//
	public static final String	P_A_NEW =			"AutoNew";
	private static final boolean DEFAULT_A_NEW =	false;
	//
	public static final String  P_COMPIERESYS =		"CompiereSys";	//	Save system records
	private static final boolean DEFAULT_COMPIERESYS = false;
	//
	public static final String  P_SHOW_ACCT =		"ShowAcct";
	private static final boolean DEFAULT_SHOW_ACCT = true;
	//
	public static final String  P_SHOW_ADVANCED =	"ShowAdvanced";
	private static final boolean DEFAULT_SHOW_ADVANCED = true;
	//
	public static final String  P_SHOW_TRL =		"ShowTrl";
	private static final boolean DEFAULT_SHOW_TRL =	false;
	//
	public static final String  P_CACHE_WINDOW =	"CacheWindow";
	private static final boolean DEFAULT_CACHE_WINDOW = true;
	//
	public static final String  P_TEMP_DIR =    	"TempDir";
	private static final String  DEFAULT_TEMP_DIR =	"";
	//
	public static final String  P_ROLE =			"Role";
	private static final String  DEFAULT_ROLE =		"";
	//
	public static final String	P_CLIENT =			"Client";
	private static final String	DEFAULT_CLIENT =	"";
	//
	public static final String	P_ORG =				"Organization";
	private static final String	DEFAULT_ORG =		"";
	//
	public static final String  P_PRINTER =			"Printer";
	private static final String  DEFAULT_PRINTER =	"";
	//
	public static final String  P_WAREHOUSE =		"Warehouse";
	private static final String  DEFAULT_WAREHOUSE = "";
	//
	public static final String  P_TODAY =       	"Today";
	private static final Timestamp DEFAULT_TODAY =	new Timestamp(System.currentTimeMillis());
	//
	public static final String  P_PRINTPREVIEW = 	"PrintPreview";
	private static final boolean DEFAULT_PRINTPREVIEW =	false;
	//
	private static final String P_WARNING =	    	"Warning";
	private static final String DEFAULT_WARNING =	"Do_not_change_any_of_the_data_as_they_will_have_undocumented_side_effects.";
	private static final String P_WARNING_de =		"WarningD";
	private static final String DEFAULT_WARNING_de ="Einstellungen_nicht_aendern,_da_diese_undokumentierte_Nebenwirkungen_haben.";

	/** Ini Properties		*/
	private static final String[]   PROPERTIES = new String[] {
		P_UID, P_PWD, P_TRACELEVEL, P_TRACEFILE, 
		P_LANGUAGE, P_INI,
		P_CONNECTION, P_STORE_PWD,
		P_UI_LOOK, P_UI_THEME, P_UI_FLAT,
		P_A_COMMIT, P_A_LOGIN, P_A_NEW, 
		P_COMPIERESYS, P_SHOW_ACCT, P_SHOW_TRL, 
		P_SHOW_ADVANCED, P_CACHE_WINDOW,
		P_CONTEXT, P_TEMP_DIR,
		P_ROLE, P_CLIENT, P_ORG, P_PRINTER, P_WAREHOUSE, P_TODAY,
		P_PRINTPREVIEW,
		P_WARNING, P_WARNING_de
	};
	/** Ini Property Values	*/
	private static final String[]   VALUES = new String[] {
		DEFAULT_UID, DEFAULT_PWD, DEFAULT_TRACELEVEL, DEFAULT_TRACEFILE?"Y":"N",
		DEFAULT_LANGUAGE, DEFAULT_INI,
		DEFAULT_CONNECTION, DEFAULT_STORE_PWD?"Y":"N",
		DEFAULT_UI_LOOK, DEFAULT_UI_THEME, DEFAULT_UI_FLAT?"Y":"N",
		DEFAULT_A_COMMIT?"Y":"N", DEFAULT_A_LOGIN?"Y":"N", DEFAULT_A_NEW?"Y":"N",
		DEFAULT_COMPIERESYS?"Y":"N", DEFAULT_SHOW_ACCT?"Y":"N", DEFAULT_SHOW_TRL?"Y":"N", 
		DEFAULT_SHOW_ADVANCED?"Y":"N", DEFAULT_CACHE_WINDOW?"Y":"N",
		DEFAULT_CONTEXT, DEFAULT_TEMP_DIR,
		DEFAULT_ROLE, DEFAULT_CLIENT, DEFAULT_ORG, DEFAULT_PRINTER, DEFAULT_WAREHOUSE, DEFAULT_TODAY.toString(),
		DEFAULT_PRINTPREVIEW?"Y":"N",
		DEFAULT_WARNING, DEFAULT_WARNING_de
	};

	/**	Container for Properties    */
	private static Properties 		s_prop = new Properties();
	/**	Logger						*/
	private static Logger			log = Logger.getLogger(Ini.class.getName());

	/**
	 *	Save INI parameters to disk
	 *  @param tryUserHome get user home first
	 */
	public static void saveProperties (boolean tryUserHome)
	{
		String fileName = getFileName (tryUserHome);
		FileOutputStream fos = null;
		try
		{
			File f = new File(fileName);
			fos = new FileOutputStream(f);
			s_prop.store(fos, "Compiere");
			fos.flush();
			fos.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Cannot save Properties to " + fileName + " - " + e.toString());
			return;
		}
		catch (Throwable t)
		{
			log.log(Level.SEVERE, "Cannot save Properties to " + fileName + " - " + t.toString());
			return;
		}
		log.finer(fileName);
	}	//	save

	/**
	 *	Load INI parameters from disk
	 *  @param reload reload
	 */
	public static void loadProperties (boolean reload)
	{
		if (reload || s_prop.size() == 0)
			loadProperties(getFileName(s_client));
	}	//	loadProperties

	/**
	 *  Load INI parameters from filename.
	 *  Logger is on default level (INFO)
	 *	@param filename to load
	 *	@return true if first time
	 */
	public static boolean loadProperties (String filename)
	{
		boolean loadOK = true;
		boolean firstTime = false;
		s_prop = new Properties();
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(filename);
			s_prop.load(fis);
			fis.close();
		}
		catch (FileNotFoundException e)
		{
			log.warning(filename + " not found");
			loadOK = false;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, filename + " - " + e.toString());
			loadOK = false;
		}
		catch (Throwable t)
		{
			log.log(Level.SEVERE, filename + " - " + t.toString());
			loadOK = false;
		}
		if (!loadOK)
		{
			log.config(filename);
			firstTime = true;
			if (!IniDialog.accept())
				System.exit(-1);
		}

		//	Check/set properties	defaults
		for (int i = 0; i < PROPERTIES.length; i++)
		{
			if (VALUES[i].length() > 0)
				checkProperty(PROPERTIES[i], VALUES[i]);
		}

		//
		String tempDir = System.getProperty("java.io.tmpdir");
		if (tempDir == null || tempDir.length() == 1)
			tempDir = getCompiereHome();
		if (tempDir == null)
			tempDir = "";
		checkProperty(P_TEMP_DIR, tempDir);

		
		//  Save if not exist or could not be read
		if (!loadOK)
			saveProperties(true);
		s_loaded = true;
		log.info(filename + " #" + s_prop.size());
		return firstTime;
	}	//	loadProperties

	/**
	 *	Load property and set to default, if not existing
	 *
	 * 	@param key   Key
	 * 	@param defaultValue   Default Value
	 * 	@return Property
	 */
	private static String checkProperty (String key, String defaultValue)
	{
		String result = null;
		if (key.equals(P_WARNING) || key.equals(P_WARNING_de))
			result = defaultValue;
		else if (!isClient())
			result = s_prop.getProperty (key, SecureInterface.CLEARVALUE_START + defaultValue + SecureInterface.CLEARVALUE_END);
		else
			result = s_prop.getProperty (key, SecureEngine.encrypt(defaultValue));
		s_prop.setProperty (key, result);
		return result;
	}	//	checkProperty

	/**
	 *	Return File Name of INI file
	 *  <pre>
	 *  Examples:
	 *	    C:\WinNT\Profiles\jjanke\Compiere.properties
	 *      D:\Compiere2\Compiere.properties
	 *      Compiere.properties
	 *  </pre>
	 *  Can be overwritten by -DPropertyFile=myFile allowing multiple
	 *  configurations / property files.
	 *  @param tryUserHome get user home first
	 *  @return file name
	 */
	private static String getFileName (boolean tryUserHome)
	{
		if (System.getProperty("PropertyFile") != null)
			return System.getProperty("PropertyFile");
		//
		String base = null;
		if (tryUserHome && s_client)
			base = System.getProperty("user.home");
		//  Server
		if (!s_client || base == null || base.length() == 0)
		{
			String home = getCompiereHome();
			if (home != null)
				base = home;
		}
		if (base != null && !base.endsWith(File.separator))
			base += File.separator;
		if (base == null)
			base = "";
		//
		return base + COMPIERE_PROPERTY_FILE;
	}	//	getFileName

	
	/**************************************************************************
	 *	Set Property
	 *  @param key   Key
	 *  @param value Value
	 */
	public static void setProperty (String key, String value)
	{
	//	log.finer(key + "=" + value);
		if (s_prop == null)
			s_prop = new Properties();
		if (key.equals(P_WARNING) || key.equals(P_WARNING_de))
			s_prop.setProperty(key, value);
		else if (!isClient())
			s_prop.setProperty(key, SecureInterface.CLEARVALUE_START + value + SecureInterface.CLEARVALUE_END);
		else
		{
			if (value == null)
				s_prop.setProperty(key, "");
			else
			{
				String eValue = SecureEngine.encrypt(value);
				if (eValue == null)
					s_prop.setProperty(key, "");
				else
					s_prop.setProperty(key, eValue);
			}
		}
	}	//	setProperty

	/**
	 *	Set Property
	 *  @param key   Key
	 *  @param value Value
	 */
	public static void setProperty (String key, boolean value)
	{
		setProperty (key, value ? "Y" : "N");
	}   //  setProperty

	/**
	 *	Set Property
	 *  @param key   Key
	 *  @param value Value
	 */
	public static void setProperty (String key, int value)
	{
		setProperty (key, String.valueOf(value));
	}   //  setProperty

	/**
	 *	Get Propery
	 *  @param key  Key
	 *  @return     Value
	 */
	public static String getProperty (String key)
	{
		if (key == null)
			return "";
		String retStr = s_prop.getProperty(key, "");
		if (retStr == null || retStr.length() == 0)
			return "";
		//
		String value = SecureEngine.decrypt(retStr); 
	//	log.finer(key + "=" + value);
		if (value == null)
			return "";
		return value;
	}	//	getProperty

	/**
	 *	Get Propery as Boolean
	 *  @param key  Key
	 *  @return     Value
	 */
	public static boolean isPropertyBool (String key)
	{
		return getProperty (key).equals("Y");
	}	//	getProperty

	/**
	 * 	Cache Windows
	 *	@return true if windows are cached
	 */
	public static boolean isCacheWindow()
	{
		return getProperty (P_CACHE_WINDOW).equals("Y");
	}	//	isCacheWindow
	
	/**************************************************************************
	 *  Get Properties
	 *
	 * @return Ini properties
	 */
	public static Properties getProperties()
	{
		return s_prop;
	}   //  getProperties

	/**
	 *  toString
	 *  @return String representation
	 */
	public static String getAsString()
	{
		StringBuffer buf = new StringBuffer ("Ini[");
		Enumeration e = s_prop.keys();
		while (e.hasMoreElements())
		{
			String key = (String)e.nextElement();
			buf.append(key).append("=");
			buf.append(getProperty(key)).append("; ");
		}
		buf.append("]");
		return buf.toString();
	}   //  toString

	
	/*************************************************************************/

	/** System environment prefix                                       */
	public static final String  ENV_PREFIX = "env.";
	/** System Property Value of COMPIERE_HOME                          */
	public static final String  COMPIERE_HOME = "COMPIERE_HOME";

	/** IsClient Internal marker            */
	private static boolean      s_client = true;
	/** IsClient Internal marker            */
	private static boolean      s_loaded = false;

	/**
	 *  Are we in Client Mode ?
	 *  @return true if client
	 */
	public static boolean isClient()
	{
		return s_client;
	}   //  isClient

	/**
	 *  Set Client Mode
	 *  @param client client
	 */
	public static void setClient (boolean client)
	{
		s_client = client;
	}   //  setClient

	/**
	 *  Are the properties loaded?
	 *  @return true if properties loaded.
	 */
	public static boolean isLoaded()
	{
		return s_loaded;
	}   //  isLoaded

	/**
	 *  Get Compiere Home from Environment
	 *  @return CompiereHome or null
	 */
	public static String getCompiereHome()
	{
		String env = System.getProperty (ENV_PREFIX + COMPIERE_HOME);
		if (env == null)
			env = System.getProperty (COMPIERE_HOME);
		return env;
	}   //  getCompiereHome

	/**
	 *  Set Compiere Home
	 *  @param CompiereHome COMPIERE_HOME
	 */
	public static void setCompiereHome (String CompiereHome)
	{
		if (CompiereHome != null && CompiereHome.length() > 0)
			System.setProperty (COMPIERE_HOME, CompiereHome);
	}   //  setCompiereHome

	/**
	 * 	Find Compiere Home
	 *	@return compiere home or null
	 */
	public static String findCompiereHome()
	{
		String ch = getCompiereHome();
		if (ch != null)
			return ch;
		
		File[] roots = File.listRoots();
		for (int i = 0; i < roots.length; i++)
		{
			if (roots[i].getAbsolutePath().startsWith("A:"))
				continue;
			File[] subs = roots[i].listFiles();
			if (subs == null)
				continue;
			for (int j = 0; j < subs.length; j++) 
			{
				if (!subs[j].isDirectory())
					continue;
				String fileName = subs[j].getAbsolutePath();
				if (fileName.indexOf("Compiere2") != 1)
				{
					String libDir = fileName + File.separator + "lib";
					File lib = new File(libDir);
					if (lib.exists() && lib.isDirectory())
						return fileName;
				}
			}
		}
		return ch;
	}	//	findCompiereHome
	
	/**************************************************************************
	 * 	Get Window Dimension
	 *	@param AD_Window_ID window no
	 *	@return dimension or null
	 */
	public static Dimension getWindowDimension(int AD_Window_ID)
	{
		String key = "WindowDim" + AD_Window_ID;
		String value = (String)s_prop.get(key);
		if (value == null || value.length() == 0)
			return null;
		int index = value.indexOf("|");
		if (index == -1)
			return null;
		try
		{
			String w = value.substring(0, index);
			String h = value.substring(index+1);
			return new Dimension(Integer.parseInt(w),Integer.parseInt(h));
		}
		catch (Exception e)
		{
		}
		return null;
	}	//	getWindowDimension
	
	/**
	 * 	Set Window Dimension 
	 *	@param AD_Window_ID window
	 *	@param windowDimension dimension - null to remove
	 */
	public static void setWindowDimension(int AD_Window_ID, Dimension windowDimension)
	{
		String key = "WindowDim" + AD_Window_ID;
		if (windowDimension != null)
		{
			String value = windowDimension.width + "|" + windowDimension.height;
			s_prop.put(key, value);
		}
		else
			s_prop.remove(key);
	}	//	setWindowDimension
	
	/**
	 * 	Get Window Location
	 *	@param AD_Window_ID window id
	 *	@return location or null
	 */
	public static Point getWindowLocation(int AD_Window_ID)
	{
		String key = "WindowLoc" + AD_Window_ID;
		String value = (String)s_prop.get(key);
		if (value == null || value.length() == 0)
			return null;
		int index = value.indexOf("|");
		if (index == -1)
			return null;
		try
		{
			String x = value.substring(0, index);
			String y = value.substring(index+1);
			return new Point(Integer.parseInt(x),Integer.parseInt(y));
		}
		catch (Exception e)
		{
		}
		return null;
	}	//	getWindowLocation
	
	/**
	 * 	Set Window Location
	 *	@param AD_Window_ID window
	 *	@param windowLocation location - null to remove
	 */
	public static void setWindowLocation(int AD_Window_ID, Point windowLocation)
	{
		String key = "WindowLoc" + AD_Window_ID;
		if (windowLocation != null)
		{
			String value = windowLocation.x + "|" + windowLocation.y;
			s_prop.put(key, value);
		}
		else
			s_prop.remove(key);
	}	//	setWindowLocation
	
	/**
	 * 	Get Divider Location
	 *	@return location
	 */
	public static int getDividerLocation()
	{
		String key = "Divider";
		String value = (String)s_prop.get(key);
		if (value == null || value.length() == 0)
			return 0;
		try
		{
			return Integer.parseInt(value);
		}
		catch (Exception e)
		{
		}
		return 0;
	}	//	getDividerLocation
	
	/**
	 * 	Set Divider Location
	 *	@param dividerLocation location
	 */
	public static void setDividerLocation(int dividerLocation)
	{
		String key = "Divider";
		String value = String.valueOf(dividerLocation);
		s_prop.put(key, value);
	}	//	setDividerLocation

}	//	Ini
