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
import java.math.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.*;
import org.compiere.db.*;
import org.compiere.model.*;


/**
 *  System Environment and static variables
 *
 *  @author     Jorg Janke
 *  @version    $Id: Env.java,v 1.58 2005/12/31 06:33:21 jjanke Exp $
 */
public final class Env
{
	/**	Logging								*/
	private static CLogger				s_log = CLogger.getCLogger(Env.class);

	/**
	 *	Exit System
	 *  @param status System exit status (usually 0 for no error)
	 */
	public static void exitEnv (int status)
	{
		//	End Session
		MSession session = MSession.get(Env.getCtx(), false);	//	finish
		if (session != null)
			session.logout();
		//
		reset(true);	// final cache reset
		s_log.info("");
		//
		CLogMgt.shutdown();
		//
		if (Ini.isClient())
			System.exit (status);
	}	//	close

	/**
	 * 	Reset Cache
	 * 	@param finalCall everything otherwise login data remains
	 */
	public static void reset (boolean finalCall)
	{
		s_log.info("finalCall=" + finalCall);

		//	Dismantle windows
		/**
		for (int i = 0; i < s_windows.size(); i++)
		{
			Container win = (Container)s_windows.get(i);
			if (win.getClass().getName().endsWith("AMenu")) // Null pointer
				;
			else if (win instanceof Window)
				((Window)win).dispose();
			else
				win.removeAll();
		}
		**/
		s_windows.clear();

		//	Clear all Context
		if (finalCall)
			s_ctx.clear();
		else	//	clear window context only
		{
			Object[] keys = s_ctx.keySet().toArray();
			for (int i = 0; i < keys.length; i++)
			{
				String tag = keys[i].toString();
				if (Character.isDigit(tag.charAt(0)))
					s_ctx.remove(keys[i]);
			}
		}

		//	Cache
		CacheMgt.get().reset();
		DB.closeTarget();
		//	Reset Role Access
		if (!finalCall)
		{
			DB.setDBTarget(CConnection.get());
			MRole defaultRole = MRole.getDefault(s_ctx, false);
			if (defaultRole != null)
				defaultRole.loadAccess(true);	//	Reload
		}
	}	//	resetAll


	/**************************************************************************
	 *  Application Context
	 */
	private static Properties   s_ctx = new Properties();
	/** WindowNo for Find           */
	public static final int     WINDOW_FIND = 1110;
	/** WinowNo for MLookup         */
	public static final int	    WINDOW_MLOOKUP = 1111;
	/** WindowNo for PrintCustomize */
	public static final int     WINDOW_CUSTOMIZE = 1112;
	/** WindowNo for PrintCustomize */
	public static final int     WINDOW_INFO = 1113;

	/** Tab for Info                */
	public static final int     TAB_INFO = 1113;

	/**
	 *  Get Context
	 *  @return Properties
	 */
	public static final Properties getCtx()
	{
		return s_ctx;
	}   //  getCtx

	/**
	 * Set Context
	 * @param ctx context
	 */
	public static void setCtx (Properties ctx)
	{
		if (ctx == null)
			throw new IllegalArgumentException ("Require Context");
		s_ctx.clear();
		s_ctx = ctx;
	}   //  setCtx

	/**
	 *	Set Global Context to Value
	 *  @param ctx context
	 *  @param context context key
	 *  @param value context value
	 */
	public static void setContext (Properties ctx, String context, String value)
	{
		if (ctx == null || context == null)
			return;
		s_log.finer("Context " + context + "==" + value);
		//
		if (value == null || value.length() == 0)
			ctx.remove(context);
		else
			ctx.setProperty(context, value);
	}	//	setContext

	/**
	 *	Set Global Context to Value
	 *  @param ctx context
	 *  @param context context key
	 *  @param value context value
	 */
	public static void setContext (Properties ctx, String context, Timestamp value)
	{
		if (ctx == null || context == null)
			return;
		if (value == null)
		{
			ctx.remove(context);
			s_log.finer("Context " + context + "==" + value);
		}
		else
		{	//	JDBC Format	2005-05-09 00:00:00.0
			String stringValue = value.toString();
			//	Chop off .0
			stringValue = stringValue.substring(0, stringValue.length()-2);
			ctx.setProperty(context, stringValue);
			s_log.finer("Context " + context + "==" + stringValue);
		}
	}	//	setContext

	/**
	 *	Set Global Context to (int) Value
	 *  @param ctx context
	 *  @param context context key
	 *  @param value context value
	 */
	public static void setContext (Properties ctx, String context, int value)
	{
		if (ctx == null || context == null)
			return;
		s_log.finer("Context " + context + "==" + value);
		//
		ctx.setProperty(context, String.valueOf(value));
	}	//	setContext

	/**
	 *	Set Global Context to Y/N Value
	 *  @param ctx context
	 *  @param context context key
	 *  @param value context value
	 */
	public static void setContext (Properties ctx, String context, boolean value)
	{
		setContext (ctx, context, value ? "Y" : "N");
	}	//	setContext

	/**
	 *	Set Context for Window to Value
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param context context key
	 *  @param value context value
	 */
	public static void setContext (Properties ctx, int WindowNo, String context, String value)
	{
		if (ctx == null || context == null)
			return;
		if (WindowNo != WINDOW_FIND && WindowNo != WINDOW_MLOOKUP)
			s_log.finer("Context("+WindowNo+") " + context + "==" + value);
		//
		if (value == null || value.equals(""))
			ctx.remove(WindowNo+"|"+context);
		else
			ctx.setProperty(WindowNo+"|"+context, value);
	}	//	setContext

	/**
	 *	Set Context for Window to Value
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param context context key
	 *  @param value context value
	 */
	public static void setContext (Properties ctx, int WindowNo, String context, Timestamp value)
	{
		if (ctx == null || context == null)
			return;
		boolean logit = WindowNo != WINDOW_FIND && WindowNo != WINDOW_MLOOKUP;
		if (value == null)
		{
			ctx.remove(WindowNo+"|"+context);
			s_log.finer("Context("+WindowNo+") " + context + "==" + value);
		}
		else
		{	//	JDBC Format	2005-05-09 00:00:00.0
			String stringValue = value.toString();
			//	Chop off .0
			stringValue = stringValue.substring(0, stringValue.length()-2);
			ctx.setProperty(WindowNo+"|"+context, stringValue);
			s_log.finer("Context("+WindowNo+") " + context + "==" + stringValue);
		}
	}	//	setContext

	/**
	 *	Set Context for Window to int Value
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param context context key
	 *  @param value context value
	 */
	public static void setContext (Properties ctx, int WindowNo, String context, int value)
	{
		if (ctx == null || context == null)
			return;
		if (WindowNo != WINDOW_FIND && WindowNo != WINDOW_MLOOKUP)
			s_log.finer("Context("+WindowNo+") " + context + "==" + value);
		//
		ctx.setProperty(WindowNo+"|"+context, String.valueOf(value));
	}	//	setContext

	/**
	 *	Set Context for Window to Y/N Value
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param context context key
	 *  @param value context value
	 */
	public static void setContext (Properties ctx, int WindowNo, String context, boolean value)
	{
		setContext (ctx, WindowNo, context, value ? "Y" : "N");
	}	//	setContext

	/**
	 *	Set Context for Window & Tab to Value
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param TabNo tab no
	 *  @param context context key
	 *  @param value context value
	 *   */
	public static void setContext (Properties ctx, int WindowNo, int TabNo, String context, String value)
	{
		if (ctx == null || context == null)
			return;
		if (WindowNo != WINDOW_FIND && WindowNo != WINDOW_MLOOKUP)
			s_log.finest("Context("+WindowNo+","+TabNo+") " + context + "==" + value);
		//
		if (value == null || value.equals(""))
			ctx.remove(WindowNo+"|"+TabNo+"|"+context);
		else
			ctx.setProperty(WindowNo+"|"+TabNo+"|"+context, value);
	}	//	setContext

	/**
	 *	Set Auto Commit
	 *  @param ctx context
	 *  @param autoCommit auto commit (save)
	 */
	public static void setAutoCommit (Properties ctx, boolean autoCommit)
	{
		if (ctx == null)
			return;
		ctx.setProperty("AutoCommit", autoCommit ? "Y" : "N");
	}	//	setAutoCommit

	/**
	 *	Set Auto Commit for Window
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param autoCommit auto commit (save)
	 */
	public static void setAutoCommit (Properties ctx, int WindowNo, boolean autoCommit)
	{
		if (ctx == null)
			return;
		ctx.setProperty(WindowNo+"|AutoCommit", autoCommit ? "Y" : "N");
	}	//	setAutoCommit

	/**
	 *	Set Auto New Record
	 *  @param ctx context
	 *  @param autoNew auto new record
	 */
	public static void setAutoNew (Properties ctx, boolean autoNew)
	{
		if (ctx == null)
			return;
		ctx.setProperty("AutoNew", autoNew ? "Y" : "N");
	}	//	setAutoNew

	/**
	 *	Set Auto New Record for Window
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param autoNew auto new record
	 */
	public static void setAutoNew (Properties ctx, int WindowNo, boolean autoNew)
	{
		if (ctx == null)
			return;
		ctx.setProperty(WindowNo+"|AutoNew", autoNew ? "Y" : "N");
	}	//	setAutoNew


	/**
	 *	Set SO Trx
	 *  @param ctx context
	 *  @param isSOTrx SO Context
	 */
	public static void setSOTrx (Properties ctx, boolean isSOTrx)
	{
		if (ctx == null)
			return;
		ctx.setProperty("IsSOTrx", isSOTrx ? "Y" : "N");
	}	//	setSOTrx

	/**
	 *	Get global Value of Context
	 *  @param ctx context
	 *  @param context context key
	 *  @return value or ""
	 */
	public static String getContext (Properties ctx, String context)
	{
		if (ctx == null || context == null)
			throw new IllegalArgumentException ("Require Context");
		return ctx.getProperty(context, "");
	}	//	getContext

	/**
	 *	Get Value of Context for Window.
	 *	if not found global context if available and enabled
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @param context context key
	 *  @param  onlyWindow  if true, no defaults are used unless explicitly asked for
	 *  @return value or ""
	 */
	public static String getContext (Properties ctx, int WindowNo, String context, boolean onlyWindow)
	{
		if (ctx == null)
			throw new IllegalArgumentException ("No Ctx");
		if (context == null)
			throw new IllegalArgumentException ("Require Context");
		String s = ctx.getProperty(WindowNo+"|"+context);
		if (s == null)
		{
			//	Explicit Base Values
			if (context.startsWith("#") || context.startsWith("$"))
				return getContext(ctx, context);
			if (onlyWindow)			//	no Default values
				return "";
			return getContext(ctx, "#" + context);
		}
		return s;
	}	//	getContext

	/**
	 *	Get Value of Context for Window.
	 *	if not found global context if available
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @param context context key
	 *  @return value or ""
	 */
	public static String getContext (Properties ctx, int WindowNo, String context)
	{
		return getContext(ctx, WindowNo, context, false);
	}	//	getContext

	/**
	 *	Get Value of Context for Window & Tab,
	 *	if not found global context if available
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param TabNo tab no
	 *  @param context context key
	 *  @return value or ""
	 */
	public static String getContext (Properties ctx, int WindowNo, int TabNo, String context)
	{
		if (ctx == null || context == null)
			throw new IllegalArgumentException ("Require Context");
		String s = ctx.getProperty(WindowNo+"|"+TabNo+"|"+context);
		if (s == null)
			return getContext(ctx, WindowNo, context, false);
		return s;
	}	//	getContext

	/**
	 *	Get Context and convert it to an integer (0 if error)
	 *  @param ctx context
	 *  @param context context key
	 *  @return value
	 */
	public static int getContextAsInt(Properties ctx, String context)
	{
		if (ctx == null || context == null)
			throw new IllegalArgumentException ("Require Context");
		String s = getContext(ctx, context);
		if (s.length() == 0)
			s = getContext(ctx, 0, context, false);		//	search 0 and defaults
		if (s.length() == 0)
			return 0;
		//
		try
		{
			return Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
			s_log.log(Level.SEVERE, "(" + context + ") = " + s, e);
		}
		return 0;
	}	//	getContextAsInt

	/**
	 *	Get Context and convert it to an integer (0 if error)
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param context context key
	 *  @return value or 0
	 */
	public static int getContextAsInt(Properties ctx, int WindowNo, String context)
	{
		String s = getContext(ctx, WindowNo, context, false);
		if (s.length() == 0)
			return 0;
		//
		try
		{
			return Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
			s_log.log(Level.SEVERE, "(" + context + ") = " + s, e);
		}
		return 0;
	}	//	getContextAsInt

	/**
	 *	Get Context and convert it to an integer (0 if error)
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param TabNo tab no
	 * 	@param context context key
	 *  @return value or 0
	 */
	public static int getContextAsInt (Properties ctx, int WindowNo, int TabNo, String context)
	{
		String s = getContext(ctx, WindowNo, TabNo, context);
		if (s.length() == 0)
			return 0;
		//
		try
		{
			return Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
			s_log.log(Level.SEVERE, "(" + context + ") = " + s, e);
		}
		return 0;
	}	//	getContextAsInt

	/**
	 *	Is AutoCommit
	 *  @param ctx context
	 *  @return true if auto commit
	 */
	public static boolean isAutoCommit (Properties ctx)
	{
		if (ctx == null)
			throw new IllegalArgumentException ("Require Context");
		String s = getContext(ctx, "AutoCommit");
		if (s != null && s.equals("Y"))
			return true;
		return false;
	}	//	isAutoCommit

	/**
	 *	Is Window AutoCommit (if not set use default)
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @return true if auto commit
	 */
	public static boolean isAutoCommit (Properties ctx, int WindowNo)
	{
		if (ctx == null)
			throw new IllegalArgumentException ("Require Context");
		String s = getContext(ctx, WindowNo, "AutoCommit", false);
		if (s != null)
		{
			if (s.equals("Y"))
				return true;
			else
				return false;
		}
		return isAutoCommit(ctx);
	}	//	isAutoCommit


	/**
	 *	Is Auto New Record
	 *  @param ctx context
	 *  @return true if auto new
	 */
	public static boolean isAutoNew (Properties ctx)
	{
		if (ctx == null)
			throw new IllegalArgumentException ("Require Context");
		String s = getContext(ctx, "AutoNew");
		if (s != null && s.equals("Y"))
			return true;
		return false;
	}	//	isAutoNew

	/**
	 *	Is Window Auto New Record (if not set use default)
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @return true if auto new record
	 */
	public static boolean isAutoNew (Properties ctx, int WindowNo)
	{
		if (ctx == null)
			throw new IllegalArgumentException ("Require Context");
		String s = getContext(ctx, WindowNo, "AutoNew", false);
		if (s != null)
		{
			if (s.equals("Y"))
				return true;
			else
				return false;
		}
		return isAutoNew(ctx);
	}	//	isAutoNew


	/**
	 *	Is Sales Order Trx
	 *  @param ctx context
	 *  @return true if SO (default)
	 */
	public static boolean isSOTrx (Properties ctx)
	{
		String s = getContext(ctx, "IsSOTrx");
		if (s != null && s.equals("N"))
			return false;
		return true;
	}	//	isSOTrx

	/**
	 *	Is Sales Order Trx
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @return true if SO (default)
	 */
	public static boolean isSOTrx (Properties ctx, int WindowNo)
	{
		String s = getContext(ctx, WindowNo, "IsSOTrx", true);
		if (s != null && s.equals("N"))
			return false;
		return true;
	}	//	isSOTrx

	/**
	 *	Get Context and convert it to a Timestamp
	 *	if error return today's date
	 *  @param ctx context
	 *  @param context context key
	 *  @return Timestamp
	 */
	public static Timestamp getContextAsDate(Properties ctx, String context)
	{
		return getContextAsDate(ctx, 0, context);
	}	//	getContextAsDate

	/**
	 *	Get Context and convert it to a Timestamp
	 *	if error return today's date
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param context context key
	 *  @return Timestamp
	 */
	public static Timestamp getContextAsDate(Properties ctx, int WindowNo, String context)
	{
		if (ctx == null || context == null)
			throw new IllegalArgumentException ("Require Context");
		String s = getContext(ctx, WindowNo, context, false);
		//	JDBC Format YYYY-MM-DD	example 2000-09-11 00:00:00.0
		if (s == null || s.equals(""))
		{
			s_log.log(Level.SEVERE, "No value for: " + context);
			return new Timestamp(System.currentTimeMillis());
		}

		//  timestamp requires time
		if (s.trim().length() == 10)
			s = s.trim() + " 00:00:00.0";
		else if (s.indexOf('.') == -1)
			s = s.trim() + ".0";

		return Timestamp.valueOf(s);
	}	//	getContextAsDate

	/**
	 * 	Get Login AD_Client_ID
	 *	@param ctx context
	 *	@return login AD_Client_ID
	 */
	public static int getAD_Client_ID (Properties ctx)
	{
		return Env.getContextAsInt(ctx, "#AD_Client_ID");
	}	//	getAD_Client_ID

	/**
	 * 	Get Login AD_Org_ID
	 *	@param ctx context
	 *	@return login AD_Org_ID
	 */
	public static int getAD_Org_ID (Properties ctx)
	{
		return Env.getContextAsInt(ctx, "#AD_Org_ID");
	}	//	getAD_Client_ID

	/**
	 * 	Get Login AD_User_ID
	 *	@param ctx context
	 *	@return login AD_User_ID
	 */
	public static int getAD_User_ID (Properties ctx)
	{
		return Env.getContextAsInt(ctx, "#AD_User_ID");
	}	//	getAD_User_ID

	/**
	 * 	Get Login AD_Role_ID
	 *	@param ctx context
	 *	@return login AD_Role_ID
	 */
	public static int getAD_Role_ID (Properties ctx)
	{
		return Env.getContextAsInt(ctx, "#AD_Role_ID");
	}	//	getAD_Role_ID

	/**************************************************************************
	 *	Get Preference.
	 *  <pre>
	 *		0)	Current Setting
	 *		1) 	Window Preference
	 *		2) 	Global Preference
	 *		3)	Login settings
	 *		4)	Accounting settings
	 *  </pre>
	 *  @param  ctx context
	 *	@param	AD_Window_ID window no
	 *	@param	context		Entity to search
	 *	@param	system		System level preferences (vs. user defined)
	 *  @return preference value
	 */
	public static String getPreference (Properties ctx, int AD_Window_ID, String context, boolean system)
	{
		if (ctx == null || context == null)
			throw new IllegalArgumentException ("Require Context");
		String retValue = null;
		//
		if (!system)	//	User Preferences
		{
			retValue = ctx.getProperty("P"+AD_Window_ID+"|"+context);//	Window Pref
			if (retValue == null)
				retValue = ctx.getProperty("P|"+context);  			//	Global Pref
		}
		else			//	System Preferences
		{
			retValue = ctx.getProperty("#"+context);   				//	Login setting
			if (retValue == null)
				retValue = ctx.getProperty("$"+context);   			//	Accounting setting
		}
		//
		return (retValue == null ? "" : retValue);
	}	//	getPreference

	/**************************************************************************
	 *  Language issues
	 */

	/** Context Language identifier */
	static public final String      LANGUAGE = "#AD_Language";

	/**
	 *  Check Base Language
	 *  @param ctx context
	 * 	@param tableName table to be translated
	 * 	@return true if base language and table not translated
	 */
	public static boolean isBaseLanguage (Properties ctx, String tableName)
	{
		/**
		if (isBaseTranslation(tableName))
			return Language.isBaseLanguage (getAD_Language(ctx));
		else	//	No AD Table
			if (!isMultiLingualDocument(ctx))
				return true;		//	access base table
		**/
		return Language.isBaseLanguage (getAD_Language(ctx));
	}	//	isBaseLanguage

	/**
	 *	Check Base Language
	 * 	@param AD_Language language
	 * 	@param tableName table to be translated
	 * 	@return true if base language and table not translated
	 */
	public static boolean isBaseLanguage (String AD_Language, String tableName)
	{
		/**
		if (isBaseTranslation(tableName))
			return Language.isBaseLanguage (AD_Language);
		else	//	No AD Table
			if (!isMultiLingualDocument(s_ctx))				//	Base Context
				return true;		//	access base table
		**/
		return Language.isBaseLanguage (AD_Language);
	}	//	isBaseLanguage

	/**
	 *	Check Base Language
	 * 	@param language language
	 * 	@param tableName table to be translated
	 * 	@return true if base language and table not translated
	 */
	public static boolean isBaseLanguage (Language language, String tableName)
	{
		/**
		if (isBaseTranslation(tableName))
			return language.isBaseLanguage();
		else	//	No AD Table
			if (!isMultiLingualDocument(s_ctx))				//	Base Context
				return true;		//	access base table
		**/
		return language.isBaseLanguage();
	}	//	isBaseLanguage

	/**
	 * 	Table is in Base Translation (AD)
	 *	@param tableName table
	 *	@return true if base trl
	 */
	public static boolean isBaseTranslation (String tableName)
	{
		if (tableName.startsWith("AD")
			|| tableName.equals("C_Country_Trl") )
			return true;
		return false;
	}	//	isBaseTranslation

	/**
	 * 	Do we have Multi-Lingual Documents.
	 *  Set in DB.loadOrgs
	 * 	@param ctx context
	 * 	@return true if multi lingual documents
	 */
	public static boolean isMultiLingualDocument (Properties ctx)
	{
		return MClient.get(ctx).isMultiLingualDocument();
	}	//	isMultiLingualDocument

	/**
	 *  Get System AD_Language
	 *  @param ctx context
	 *	@return AD_Language eg. en_US
	 */
	public static String getAD_Language (Properties ctx)
	{
		if (ctx != null)
		{
			String lang = getContext(ctx, LANGUAGE);
			if (lang != null || lang.length() > 0)
				return lang;
		}
		return Language.getBaseAD_Language();
	}	//	getAD_Language

	/**
	 *  Get System Language
	 *  @param ctx context
	 *	@return Language
	 */
	public static Language getLanguage (Properties ctx)
	{
		if (ctx != null)
		{
			String lang = getContext(ctx, LANGUAGE);
			if (lang != null || lang.length() > 0)
				return Language.getLanguage(lang);
		}
		return Language.getBaseLanguage();
	}	//	getLanguage

	/**
	 *  Get Login Language
	 *  @param ctx context
	 *	@return Language
	 */
	public static Language getLoginLanguage (Properties ctx)
	{
		return Language.getLoginLanguage();
	}	//	getLanguage

	/**
	 *  Verify Language.
	 *  Check that language is supported by the system
	 *  @param ctx might be updated with new AD_Language
	 *  @param language language
	 */
	public static void verifyLanguage (Properties ctx, Language language)
	{
		if (language.isBaseLanguage())
			return;

		boolean isSystemLanguage = false;
		ArrayList<String> AD_Languages = new ArrayList<String>();
		String sql = "SELECT DISTINCT AD_Language FROM AD_Message_Trl";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				String AD_Language = rs.getString(1);
				if (AD_Language.equals(language.getAD_Language()))
				{
					isSystemLanguage = true;
					break;
				}
				AD_Languages.add(AD_Language);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, "", e);
		}
		//	Found it
		if (isSystemLanguage)
			return;
		//	No Language - set to System
		if (AD_Languages.size() == 0)
		{
			s_log.warning ("NO System Language - Set to Base " + Language.getBaseAD_Language());
			language.setAD_Language(Language.getBaseAD_Language());
			return;
		}

		for (int i = 0; i < AD_Languages.size(); i++)
		{
			String AD_Language = (String)AD_Languages.get(i);	//	en_US
			String lang = AD_Language.substring(0, 2);			//	en
			//
			String langCompare = language.getAD_Language().substring(0, 2);
			if (lang.equals(langCompare))
			{
				s_log.fine("Found similar Language " + AD_Language);
				language.setAD_Language(AD_Language);
				return;
			}
		}

		//	We found same language
	//	if (!"0".equals(Msg.getMsg(AD_Language, "0")))

		s_log.warning ("Not System Language=" + language
			+ " - Set to Base Language " + Language.getBaseAD_Language());
		language.setAD_Language(Language.getBaseAD_Language());
	}   //  verifyLanguage

	/**************************************************************************
	 *	Get Context as String array with format: key == value
	 *  @param ctx context
	 *  @return context string
	 */
	public static String[] getEntireContext(Properties ctx)
	{
		if (ctx == null)
			throw new IllegalArgumentException ("Require Context");
		Iterator keyIterator = ctx.keySet().iterator();
		String[] sList = new String[ctx.size()];
		int i = 0;
		while (keyIterator.hasNext())
		{
			Object key = keyIterator.next();
			sList[i++] = key.toString() + " == " + ctx.get(key).toString();
		}

		return sList;
	}	//	getEntireContext

	/**
	 *	Get Header info (connection, org, user)
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @return Header String
	 */
	public static String getHeader(Properties ctx, int WindowNo)
	{
		StringBuffer sb = new StringBuffer();
		if (WindowNo > 0)
			sb.append(getContext(ctx, WindowNo, "WindowName", false)).append("  ");
		sb.append(getContext(ctx, "#AD_User_Name")).append("@")
			.append(getContext(ctx, "#AD_Client_Name")).append(".")
			.append(getContext(ctx, "#AD_Org_Name"))
			.append(" [").append(CConnection.get().toString()).append("]");
		return sb.toString();
	}	//	getHeader

	/**
	 *	Clean up context for Window (i.e. delete it)
	 *  @param ctx context
	 *  @param WindowNo window
	 */
	public static void clearWinContext(Properties ctx, int WindowNo)
	{
		if (ctx == null)
			throw new IllegalArgumentException ("Require Context");
		//
		Object[] keys = ctx.keySet().toArray();
		for (int i = 0; i < keys.length; i++)
		{
			String tag = keys[i].toString();
			if (tag.startsWith(WindowNo+"|"))
				ctx.remove(keys[i]);
		}
		//  Clear Lookup Cache
		MLookupCache.cacheReset(WindowNo);
	//	MLocator.cacheReset(WindowNo);
		//
		removeWindow(WindowNo);
	}	//	clearWinContext

	/**
	 *	Clean up all context (i.e. delete it)
	 *  @param ctx context
	 */
	public static void clearContext(Properties ctx)
	{
		if (ctx == null)
			throw new IllegalArgumentException ("Require Context");
		ctx.clear();
	}	//	clearContext


	/**
	 *	Parse Context replaces global or Window context @tag@ with actual value.
	 *
	 *  @tag@ are ignored otherwise "" is returned
	 *  @param ctx context
	 *	@param	WindowNo	Number of Window
	 *	@param	value		Message to be parsed
	 *  @param  onlyWindow  if true, no defaults are used
	 * 	@param	ignoreUnparsable if true, unsuccessful @return parsed String or "" if not successful and ignoreUnparsable
	 */
	public static String parseContext (Properties ctx, int WindowNo, String value,
		boolean onlyWindow, boolean ignoreUnparsable)
	{
		if (value == null || value.length() == 0)
			return "";

		String token;
		String inStr = new String(value);
		StringBuffer outStr = new StringBuffer();

		int i = inStr.indexOf('@');
		while (i != -1)
		{
			outStr.append(inStr.substring(0, i));			// up to @
			inStr = inStr.substring(i+1, inStr.length());	// from first @

			int j = inStr.indexOf('@');						// next @
			if (j < 0)
			{
				s_log.log(Level.SEVERE, "No second tag: " + inStr);
				return "";						//	no second tag
			}

			token = inStr.substring(0, j);

			String ctxInfo = getContext(ctx, WindowNo, token, onlyWindow);	// get context
			if (ctxInfo.length() == 0 && (token.startsWith("#") || token.startsWith("$")) )
				ctxInfo = getContext(ctx, token);	// get global context
			if (ctxInfo.length() == 0)
			{
				s_log.config("No context Win=" + WindowNo + " for: " + token);
				if (!ignoreUnparsable)
					return "";						//	token not found
			}
			else
				outStr.append(ctxInfo);				// replace context with Context

			inStr = inStr.substring(j+1, inStr.length());	// from second @
			i = inStr.indexOf('@');
		}
		outStr.append(inStr);						// add the rest of the string

		return outStr.toString();
	}	//	parseContext

	/**
	 *	Parse Context replaces global or Window context @tag@ with actual value.
	 *
	 *  @param ctx context
	 *	@param	WindowNo	Number of Window
	 *	@param	value		Message to be parsed
	 *  @param  onlyWindow  if true, no defaults are used
	 *  @return parsed String or "" if not successful
	 */
	public static String parseContext (Properties ctx, int WindowNo, String value,
		boolean onlyWindow)
	{
		return parseContext(ctx, WindowNo, value, onlyWindow, false);
	}	//	parseContext

	/*************************************************************************/

	//	Array of active Windows
	private static ArrayList<Container>	s_windows = new ArrayList<Container>(20);

	/**
	 *	Add Container and return WindowNo.
	 *  The container is a APanel, AWindow or JFrame/JDialog
	 *  @param win window
	 *  @return WindowNo used for context
	 */
	public static int createWindowNo(Container win)
	{
		int retValue = s_windows.size();
		s_windows.add(win);
		return retValue;
	}	//	createWindowNo

    public static int getLastActiveWindowNo() {
        if (!s_windows.isEmpty()) {
            return s_windows.size() - 1;
        }
        return 0;
    }

	/**
	 *	Search Window by comparing the Frames
	 *  @param container container
	 *  @return WindowNo of container or 0
	 */
	public static int getWindowNo (Container container)
	{
		if (container == null)
			return 0;
		JFrame winFrame = getFrame(container);
		if (winFrame == null)
			return 0;

		//  loop through windows
		for (int i = 0; i < s_windows.size(); i++)
		{
			Container cmp = (Container)s_windows.get(i);
			if (cmp != null)
			{
				JFrame cmpFrame = getFrame(cmp);
				if (winFrame.equals(cmpFrame))
					return i;
			}
		}
		return 0;
	}	//	getWindowNo

	/**
	 *	Return the JFrame pointer of WindowNo - or null
	 *  @param WindowNo window
	 *  @return JFrame of WindowNo
	 */
	public static JFrame getWindow (int WindowNo)
	{
		JFrame retValue = null;
		try
		{
			retValue = getFrame ((Container)s_windows.get(WindowNo));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, e.toString());
		}
		return retValue;
	}	//	getWindow

	/**
	 *	Remove window from active list
	 *  @param WindowNo window
	 */
	private static void removeWindow (int WindowNo)
	{
		if (WindowNo < s_windows.size())
			s_windows.set(WindowNo, null);
	}	//	removeWindow

	/**
	 *	Clean up context for Window (i.e. delete it)
	 *  @param WindowNo window
	 */
	public static void clearWinContext(int WindowNo)
	{
		clearWinContext (s_ctx, WindowNo);
	}	//	clearWinContext

	/**
	 *	Clean up all context (i.e. delete it)
	 */
	public static void clearContext()
	{
		s_ctx.clear();
	}	//	clearContext


	/**************************************************************************
	 *	Get Frame of Window
	 *  @param container Container
	 *  @return JFrame of container or null
	 */
	public static JFrame getFrame (Container container)
	{
		Container element = container;
		while (element != null)
		{
			if (element instanceof JFrame)
				return (JFrame)element;
			element = element.getParent();
		}
		return null;
	}	//	getFrame

	/**
	 *	Get Graphics of container or its parent.
	 *  The element may not have a Graphic if not displayed yet,
	 * 	but the parent might have.
	 *  @param container Container
	 *  @return Graphics of container or null
	 */
	public static Graphics getGraphics (Container container)
	{
		Container element = container;
		while (element != null)
		{
			Graphics g = element.getGraphics();
			if (g != null)
				return g;
			element = element.getParent();
		}
		return null;
	}	//	getFrame

	/**
	 *  Return JDialog or JFrame Parent
	 *  @param container Container
	 *  @return JDialog or JFrame of container
	 */
	public static Window getParent (Container container)
	{
		Container element = container;
		while (element != null)
		{
			if (element instanceof JDialog || element instanceof JFrame)
				return (Window)element;
			if (element instanceof Window)
				return (Window)element;
			element = element.getParent();
		}
		return null;
	}   //  getParent

	/**************************************************************************
	 *  Get Image with File name
	 *
	 *  @param fileNameInImageDir full file name in imgaes folder (e.g. Bean16.gif)
	 *  @return image
	 */
	public static Image getImage (String fileNameInImageDir)
	{
		URL url = Compiere.class.getResource("images/" + fileNameInImageDir);
		if (url == null)
		{
			s_log.log(Level.SEVERE, "Not found: " +  fileNameInImageDir);
			return null;
		}
		Toolkit tk = Toolkit.getDefaultToolkit();
		return tk.getImage(url);
	}   //  getImage

	/**
	 *  Get ImageIcon.
	 *
	 *  @param fileNameInImageDir full file name in imgaes folder (e.g. Bean16.gif)
	 *  @return image
	 */
	public static ImageIcon getImageIcon (String fileNameInImageDir)
	{
		URL url = Compiere.class.getResource("images/" + fileNameInImageDir);
		if (url == null)
		{
			s_log.log(Level.SEVERE, "Not found: " +  fileNameInImageDir);
			return null;
		}
		return new ImageIcon(url);
	}   //  getImageIcon


	/***************************************************************************
	 *  Start Browser
	 *  @param url url
	 */
	public static void startBrowser (String url)
	{
		s_log.info(url);
		//  OS command
		String cmd = "explorer ";
		if (!isWindows())
			cmd = "netscape ";
		//
		String execute = cmd + url;
		try
		{
			Runtime.getRuntime().exec(execute);
		}
		catch (Exception e)
		{
			s_log.severe(execute + " - " + e);
		}
	}   //  startBrowser

	/**
	 * 	Do we run on Apple
	 *	@return true if Mac
	 */
	public static boolean isMac()
   	{
   		String osName = System.getProperty ("os.name");
   		osName = osName.toLowerCase();
   		return osName.indexOf ("mac") != -1;
   	}	//	isMac

   	/**
   	 * 	Do we run on Windows
   	 *	@return true if windows
   	 */
   	public static boolean isWindows()
   	{
   		String osName = System.getProperty ("os.name");
   		osName = osName.toLowerCase();
   		return osName.indexOf ("windows") != -1;
   	}	//	isWindows

	/**************************************************************************
	 *  Static Variables
	 */

	/**	Big Decimal 0	 */
	static final public BigDecimal ZERO = new BigDecimal(0.0);
	/**	Big Decimal 1	 */
	static final public BigDecimal ONE = new BigDecimal(1.0);
	/**	Big Decimal 100	 */
	static final public BigDecimal ONEHUNDRED = new BigDecimal(100.0);

	/**	New Line 		 */
	public static final String	NL = System.getProperty("line.separator");


	/**
	 *  Static initializer
	 */
	static
	{
		//  Set English as default Language
		s_ctx.put(LANGUAGE, Language.getBaseAD_Language());
	}   //  static

}   //  Env
