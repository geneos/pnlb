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

import java.io.*;
import org.compiere.util.*;


/**
 *	Sun Java VM Configuration
 *	
 *  @author Jorg Janke
 *  @version $Id: ConfigVMSun.java,v 1.3 2005/09/07 04:47:23 jjanke Exp $
 */
public class ConfigVMSun extends Config
{
	/**
	 * 	ConfigVMSun
	 */
	public ConfigVMSun (ConfigurationData data)
	{
		super (data);
	}	//	ConfigVMSun
	
	/**
	 * 	Init
	 */
	public void init()
	{
		//	Java Home, e.g. D:\j2sdk1.4.1\jre
		String javaHome = System.getProperty("java.home");
		log.fine(javaHome);
		if (javaHome.endsWith("jre"))
			javaHome = javaHome.substring(0, javaHome.length()-4);
		p_data.setJavaHome(javaHome);
	}	//	init
	
	/**
	 * 	Test
	 *	@return error message or null of OK
	 */
	public String test()
	{
		//	Java Home
		File javaHome = new File (p_data.getJavaHome());
		boolean pass = javaHome.exists();
		String error = "Not found: Java Home";
		signalOK(getPanel().okJavaHome, "ErrorJavaHome",
			pass, true, error);
		if (!pass)
			return error;
		//	Look for tools.jar to make sure that it is not the JRE
		File tools = new File (p_data.getJavaHome() 
			+ File.separator + "lib" + File.separator + "tools.jar");
		pass = tools.exists();
		error = "Not found: Java SDK = " + tools;
		signalOK(getPanel().okJavaHome, "ErrorJavaHome",
			pass, true, error);
		if (!pass)
			return error;
		//
		if (CLogMgt.isLevelFinest())
			CLogMgt.printProperties(System.getProperties(), "System", true);
		//
		log.info("OK: JavaHome=" + javaHome.getAbsolutePath());
		setProperty(ConfigurationData.JAVA_HOME, javaHome.getAbsolutePath());
		System.setProperty(ConfigurationData.JAVA_HOME, javaHome.getAbsolutePath());
		
		
		//	Java Version
		final String VERSION = "1.5.0";
		final String VERSION2 = "1.5.0";	//	The real one
		pass = false;
		String jh = javaHome.getAbsolutePath();
		if (jh.indexOf(VERSION) != -1)	//	file name has version = assuming OK
			pass = true;
		if (!pass && jh.indexOf(VERSION2) != -1)	//
			pass = true;
		String thisJH = System.getProperty("java.home");
		if (thisJH.indexOf(jh) != -1)	//	we are running the version currently
		{
			String thisJV = System.getProperty("java.version");
			pass = thisJV.indexOf(VERSION) != -1;
			if (!pass && thisJV.indexOf(VERSION2) != -1)
				pass = true;
			if (pass)
			  log.info("OK: Version=" + thisJV);
		}
		error = "Wrong Java Version: Should be " + VERSION2;
		signalOK(getPanel().okJavaHome, "ErrorJavaHome",
				pass, true, error);
		if (!pass)
			return error;
		//
		setProperty(ConfigurationData.JAVA_TYPE, p_data.getJavaType());
		
		return null;
	}	//	test
	
}	//	ConfigVMSun
