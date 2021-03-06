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
package org.compiere.db;

/**
 *  General Database Constants and Utilities
 *
 *  @author     Jorg Janke
 *  @version    $Id: Database.java,v 1.14 2005/12/31 06:33:21 jjanke Exp $
 */
public class Database
{
	/** Oracle ID       */
	public static String        DB_ORACLE   = "Oracle";
	/** PostgreSQL ID   */
	public static String        DB_POSTGRESQL = "PostgreSQL";
        // begin vpj-c e-evolution 11/30/2005 EDB
        /** Enterprise DB   */
	public static String        DB_EDB = "EnterpriseDB";
        // end vpj-c e-evolution 11/30/2005 EDB
	/** Sybase ID		*/
	public static String        DB_SYBASE   = "Sybase";
	/** Microsoft ID	*/
	public static String        DB_MSSQLServer = "SQLServer";
	/** IBM DB/2 ID		*/
	public static String        DB_DB2 = "DB2";
	/** Derby ID		*/
	public static String        DB_DERBY = "Derby";

	/** Supported Databases     */
	public static String[]      DB_NAMES = new String[] {
		DB_ORACLE
		,DB_DB2
		,DB_SYBASE
	//	,DB_POSTGRESQL
        // begin vpj-c e-evolution 02/08/205 PostgreSQL
		,DB_POSTGRESQL 
        ,DB_EDB
        // end e-evolution 02/08/2005 PostgreSQL
	};

	/** Database Classes        */
	protected static Class[]    DB_CLASSES = new Class[] {
		DB_Oracle.class
		,DB_DB2.class
		,DB_Sybase.class
	//	,DB_PostgreSQL.class
        //begin vpj-c e-evolution 02/08/2005 PostgreSQL        
		,DB_PostgreSQL.class
        ,DB_EDB.class        
        //end e-evolution 02/08/205	PostgreSQL

	};

	/** Connection Timeout in seconds   */
	public static int           CONNECTION_TIMEOUT = 10;

}   //  Database
