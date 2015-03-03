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
 * Copyright (C) 2004 Marco LOMBARDO. lombardo@mayking.com
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/

/*package ;*/   // TODO: add package.
package org.eevolution.tools;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;
import org.compiere.db.*;


/**
 *  XML2AD Compilo tool.
 *
 *  @author Marco LOMBARDO,Victor Perez , lombardo@mayking.com , victor.perez@e-evolution.com
 */
public class XML2AD {

    /**	Logger						*/
    //private Logger log = Logger.getCLogger(getClass());
    /**	Static Logger	*/
	private static CLogger	log	= CLogger.getCLogger (XML2AD.class);
	
	
   
    /**
     * 	Uses XML2ADHandler to update AD.
     *	@param fileName xml file to read
     * 	@return status message
     */
    public String importXML (String fileName) {
	log.info("importXML:" + fileName);
	File in = new File (fileName);
	if (!in.exists()) {
	    String msg = "File does not exist: " + fileName;
	    log.log(Level.SEVERE, "importXML:" + msg);
	    return msg;
	}
	try {
	    XML2ADHandler handler = new XML2ADHandler();
	    SAXParserFactory factory = SAXParserFactory.newInstance();
	    SAXParser parser = factory.newSAXParser();
	    parser.parse(in, handler);
	    return "OK.";
	}
	catch (Exception e) {
		log.log(Level.SEVERE, "importXML:", e);	    
	    return e.toString();
	}
    }
    
    /*****************************************************
     *
     * 	@param args XMLfile host port db username password
     */
    public static void main (String[] args) {
	if (args.length < 1) {
	    System.out.println("Please give the file name to read as first parameter.");
	    System.exit(1);
	}
	
	String file = args[0];
	//org.compiere.Compiere.startupClient();
	org.compiere.Compiere.startupEnvironment(true);
	
	// Force connection if there are enough parameters. Else we work with Compiere.properties
	/*if (args.length >= 6) {
	    CConnection cc = CConnection.get(Database.DB_ORACLE, args[1], Integer.valueOf(args[2]).intValue(), args[3], args[4], args[5]);
	    System.out.println("DB UserID:"+cc.getDbUid());
	    DB.setDBTarget(cc);
	}*/

	// Adjust trace level. Or it will be taken from Compiere.properties
	// TODO: have a parameter for the trace level.
	//Log.setTraceLevel(2);
	//Ini.setProperty(Ini.P_DEBUGLEVEL, String.valueOf(2));

	XML2AD impXML = new XML2AD();
	impXML.importXML(file);
	
	System.exit(0);
    }   // main

}   // XML2AD

// Marco LOMBARDO, 2004-08-20, Italy.
// lombardo@mayking.com

