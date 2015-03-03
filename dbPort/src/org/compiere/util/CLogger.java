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

import java.io.*;
import java.util.logging.*;


/**
 *	Compiere Logger
 *	
 *  @author Jorg Janke
 *  @version $Id: CLogger.java,v 1.7 2005/12/27 06:20:13 jjanke Exp $
 */
public class CLogger extends Logger implements Serializable
{
	/**
	 * 	Get Logger
	 *	@param className class name
	 *	@return Logger
	 */
    public static synchronized CLogger getCLogger (String className) 
    {
   	//	CLogMgt.initialize();
    	LogManager manager = LogManager.getLogManager();
    	if (className == null)
    		className = "";
    	Logger result = manager.getLogger(className);
    	if (result != null && result instanceof CLogger)
    		return (CLogger)result;
    	//
   	    CLogger newLogger = new CLogger(className, null);
   	    newLogger.setLevel(CLogMgt.getLevel());
   	    manager.addLogger(newLogger);
    	return newLogger;
    }	//	getLogger

	/**
	 * 	Get Logger
	 *	@param clazz class name
	 *	@return Logger
	 */
    public static CLogger getCLogger (Class clazz)
    {
    	if (clazz == null)
    		return get();
    	return getCLogger (clazz.getName());
    }	//	getLogger

    /**
     * 	Get default Compiere Logger.
     * 	Need to be used in serialized objects
     *	@return logger
     */
    public static CLogger get()
    {
    	if (s_logger == null)
    		s_logger = getCLogger("org.compiere.default");
    	return s_logger;
    }	//	get
    
    /**	Default Logger			*/
    private static CLogger	s_logger = null;
    
    
	/**************************************************************************
	 * 	Standard constructor
	 *	@param name logger name
	 *	@param resourceBundleName optional resource bundle (ignored)
	 */
    private CLogger (String name, String resourceBundleName)
	{
		super (name, resourceBundleName);
	//	setLevel(Level.ALL);
	}	//	CLogger

    
	/*************************************************************************/

	/** Last Error Message   */
	private static  ValueNamePair   s_lastError = null;
	/**	Last Exception		*/
	private static Exception		s_lastException = null;
	/** Last Warning Message   */
	private static  ValueNamePair   s_lastWarning = null;
	/** Last Info Message   */
	private static  ValueNamePair   s_lastInfo = null;
	
	/**
	 *  Set and issue Error and save as ValueNamePair
	 *  @param AD_Message message key
	 *  @param message clear text message
	 *  @return true (to avoid removal of method)
	 */
	public boolean saveError (String AD_Message, String message)
	{
		return saveError (AD_Message, message, true);
	}   //  saveError

	/**
	 *  Set and issue Error and save as ValueNamePair
	 *  @param AD_Message message key
	 *  @param ex exception 
	 *  @return true (to avoid removal of method)
	 */
	public boolean saveError (String AD_Message, Exception ex)
	{
		s_lastException = ex;
		return saveError (AD_Message, ex.getLocalizedMessage(), true);
	}   //  saveError

	/**
	 *  Set Error and save as ValueNamePair
	 *  @param AD_Message message key
	 *  @param message clear text message
	 *  @param issueError print error message (default true)
	 *  @return true
	 */
	public boolean saveError (String AD_Message, String message, boolean issueError)
	{
		s_lastError = new ValueNamePair (AD_Message, message);
		//  print it
		if (issueError)
			severe(AD_Message + " - " + message);
		return true;
	}   //  saveError

	/**
	 *  Get Error from Stack
	 *  @return AD_Message as Value and Message as String
	 */
	public static ValueNamePair retrieveError()
	{
		ValueNamePair vp = s_lastError;
		s_lastError = null;
		return vp;
	}   //  retrieveError

	/**
	 *  Get Error from Stack
	 *  @return last exception
	 */
	public static Exception retrieveException()
	{
		Exception ex = s_lastException;
		s_lastException = null;
		return ex;
	}   //  retrieveError

	/**
	 *  Save Warning as ValueNamePair
	 *  @param AD_Message message key
	 *  @param message clear text message
	 *  @return true
	 */
	public boolean saveWarning (String AD_Message, String message)
	{
		s_lastWarning = new ValueNamePair (AD_Message, message);
		return true;
	}   //  saveWarning

	/**
	 *  Get Warning from Stack
	 *  @return AD_Message as Value and Message as String
	 */
	public static ValueNamePair retrieveWarning()
	{
		ValueNamePair vp = s_lastWarning;
		s_lastWarning = null;
		return vp;
	}   //  retrieveWarning

	/**
	 *  Save Info as ValueNamePair
	 *  @param AD_Message message key
	 *  @param message clear text message
	 *  @return true
	 */
	public boolean saveInfo (String AD_Message, String message)
	{
		s_lastInfo = new ValueNamePair (AD_Message, message);
		return true;
	}   //  saveInfo

	/**
	 *  Get Info from Stack
	 *  @return AD_Message as Value and Message as String
	 */
	public static ValueNamePair retrieveInfo()
	{
		ValueNamePair vp = s_lastInfo;
		s_lastInfo = null;
		return vp;
	}   //  retrieveInfo

	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("CLogger[");
		sb.append (getName())
			.append (",Level=").append (getLevel()).append ("]");
		return sb.toString ();
	}	 //	toString
	
	/**
	 * 	Write Object - Serialization
	 *	@param out out
	 *	@throws IOException
	 *
	private void writeObject (ObjectOutputStream out) throws IOException
	{
		out.writeObject(getName());
		System.out.println("====writeObject:" + getName());
	}	//	writeObject
	
	private String m_className = null;
	
	private void readObject (ObjectInputStream in) throws IOException
	{
		try
		{
			m_className = (String)in.readObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("====readObject:" + m_className);
	}
	
	protected Object readResolve() throws ObjectStreamException
	{
		System.out.println("====readResolve:" + m_className);
		return getLogger(m_className);
	}
	/** **/
}	//	CLogger
