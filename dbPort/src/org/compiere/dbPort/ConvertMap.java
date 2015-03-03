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
package org.compiere.dbPort;

import java.util.*;

/**
 *  Database Syntax Conversion Map.
 *
 *
 *  @author     Jorg Janke & Victor Perez
 *  @version    $Id: ConvertMap.java,v 1.18 2005/09/19 04:48:43 jjanke Exp $
 */
public class ConvertMap
{
	/**
	 *  Return Map for Sybase
	 *  @return TreeMap with pattern as key and the replacement as value
	 */
	public static TreeMap getSybaseMap()
	{
		if (s_sybase.size() == 0)
			initSybase();
		return s_sybase;
	}   //  getSybaseMap

	/**
	 *  Return Map for PostgreSQL
	 *  @return TreeMap with pattern as key and the replacement as value
	 */
	public static TreeMap getPostgeSQLMap()
	{
		if (s_pg.size() == 0)
			initPostgreSQL();
		return s_pg;
	}   //  getPostgreSQLMap

        /** Tree Map for Sybase			*/
	private static TreeMap<String,String>  s_sybase = new TreeMap<String,String>();
	/** Tree Map for PostgreSQL     */
	private static TreeMap<String,String>  s_pg = new TreeMap<String,String>();

	/**
	 *  Sybase Init
	 */
	static private void initSybase()
	{
		//      Oracle Pattern                  Replacement

		//  Data Types
		s_sybase.put("\\bNUMBER\\b",                "NUMERIC");
		s_sybase.put("\\bDATE\\b",                  "DATETIME");
		s_sybase.put("\\bVARCHAR2\\b",              "VARCHAR");
		s_sybase.put("\\bNVARCHAR2\\b",             "NVARCHAR");
		s_sybase.put("\\bNCHAR\\b",                 "NCHAR");
		s_sybase.put("\\bBLOB\\b",                  "IMAGE");
		s_sybase.put("\\bCLOB\\b",                  "TEXT");

		//  Storage
		s_sybase.put("\\bCACHE\\b",                 "");
		s_sybase.put("\\bUSING INDEX\\b",           "");
		s_sybase.put("\\bTABLESPACE\\s\\w+\\b",     "");
		s_sybase.put("\\bSTORAGE\\([\\w\\s]+\\)",   "");
		//
		s_sybase.put("\\bBITMAP INDEX\\b",          "INDEX");
		
		//	Select
		s_sybase.put("\\bFOR UPDATE\\b",			"");
		s_sybase.put("\\bTRUNC\\(",					"convert(date,");

		//  Functions
		s_sybase.put("\\bSysDate\\b",               "getdate()");
		s_sybase.put("\\bSYSDATE\\b",               "getdate()");
		s_sybase.put("\\bNVL\\b",                   "COALESCE");
		s_sybase.put("\\bTO_DATE\\b",               "TO_TIMESTAMP");
		//
		s_sybase.put("\\bDBMS_OUTPUT.PUT_LINE\\b",  "RAISE NOTICE");

		//  Temporary
		s_sybase.put("\\bGLOBAL TEMPORARY\\b",      "TEMPORARY");
		s_sybase.put("\\bON COMMIT DELETE ROWS\\b", "");
		s_sybase.put("\\bON COMMIT PRESERVE ROWS\\b",   "");


		//  DROP TABLE x CASCADE CONSTRAINTS
		s_sybase.put("\\bCASCADE CONSTRAINTS\\b",   "");

		//  Select
		s_sybase.put("\\sFROM\\s+DUAL\\b",          "");

		//  Statements
		s_sybase.put("\\bELSIF\\b",                 "ELSE IF");

		//  Sequences
		s_sybase.put("\\bSTART WITH\\b",            "START");
		s_sybase.put("\\bINCREMENT BY\\b",          "INCREMENT");

	}   //  initSybase

	/**
	 *  PostgreSQL Init
	 */
	static private void initPostgreSQL()
	{
		//      Oracle Pattern                  Replacement

		//  Data Types
		s_pg.put("\\bNUMBER\\b",                "NUMERIC");
		s_pg.put("\\bDATE\\b",                  "TIMESTAMP");
		s_pg.put("\\bVARCHAR2\\b",              "VARCHAR");
		s_pg.put("\\bNVARCHAR2\\b",             "VARCHAR");
		s_pg.put("\\bNCHAR\\b",                 "CHAR");
        //begin vpj-cd e-evolution 03/11/2005 PostgreSQL
		s_pg.put("\\bBLOB\\b",                  "BYTEA");                 //  BLOB not directly supported
		s_pg.put("\\bCLOB\\b",                  "BYTEA");                //  CLOB not directly supported
                s_pg.put("\\bLIMIT\\b","\"limit\""); 
                s_pg.put("\\bACTION\\b","\"action\""); 
		//s_pg.put("\\bBLOB\\b",                  "OID");                 //  BLOB not directly supported
		//s_pg.put("\\bCLOB\\b",                  "OID");                //  CLOB not directly supported
        //end vpj-cd e-evolution 03/11/2005 PostgreSQL
		
		//  Storage
		s_pg.put("\\bCACHE\\b",                 "");
		s_pg.put("\\bUSING INDEX\\b",           "");
		s_pg.put("\\bTABLESPACE\\s\\w+\\b",     "");
		s_pg.put("\\bSTORAGE\\([\\w\\s]+\\)",   "");
		//
		s_pg.put("\\bBITMAP INDEX\\b",          "INDEX");

		//  Functions
		s_pg.put("\\bSYSDATE\\b",               "CURRENT_TIMESTAMP");   //  alternative: NOW()
		//Bug fix, Gunther Hoppe 08.07.2005 e-evolution
        //Begin ----------------------------------------------------------------------------------------			
		s_pg.put("\\bSysDate\\b",               "CURRENT_TIMESTAMP");
		s_pg.put("SysDate",               "CURRENT_TIMESTAMP");
		//end ----------------------------------------------------------------------------------------	
        //begin vpj-cd e-evolution 03/11/2005 PostgreSQL		                                     
		s_pg.put("\\bDUMP\\b",               "MD5"); 	
		s_pg.put("END CASE",               "END");		
		s_pg.put("\\bgetDate()\\b",               "CURRENT_TIMESTAMP");   //  alternative: NOW()
		//end vpj-cd e-evolution 03/11/2005 PostgreSQL
		s_pg.put("\\bNVL\\b",                   "COALESCE");
		s_pg.put("\\bTO_DATE\\b",               "TO_TIMESTAMP");
		//
		s_pg.put("\\bDBMS_OUTPUT.PUT_LINE\\b",  "RAISE NOTICE");

		//  Temporary
		s_pg.put("\\bGLOBAL TEMPORARY\\b",      "TEMPORARY");
		s_pg.put("\\bON COMMIT DELETE ROWS\\b", "");
		s_pg.put("\\bON COMMIT PRESERVE ROWS\\b",   "");

                //DDL
                
                // begin vpj-cd e-evolution 08/02/2005 PostgreSQL
		//s_pg.put("\\bMODIFY\\b","ALTER COLUMN");						
                //s_pg.put("\\bDEFAULT\\b","SET DEFAULT");
		// end vpj-cd e-evolution 08/02/2005 PostgreSQL
                
		//  DROP TABLE x CASCADE CONSTRAINTS
		s_pg.put("\\bCASCADE CONSTRAINTS\\b",   "");

		//  Select
		s_pg.put("\\sFROM\\s+DUAL\\b",          "");

		//  Statements
		s_pg.put("\\bELSIF\\b",                 "ELSE IF");
		// begin vpj-cd e-evolution 03/11/2005 PostgreSQL
		s_pg.put("\\bREC \\b",                 "AS REC ");				
		//s_pg.put("\\bAND\\sROWNUM=\\b",                 "LIMIT ");
		// end vpj-cd e-evolution 03/11/2005 PostgreSQL

		//  Sequences
		s_pg.put("\\bSTART WITH\\b",            "START");
		s_pg.put("\\bINCREMENT BY\\b",          "INCREMENT");

	}   //  initPostgreSQL
	
	
}   //  ConvertMap
