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

/**
 * This RuntimeException is used to pass SQLException up the chain of calling
 * methods to determine what to do where needed.
 * 
 * @author Vincent Harcq
 * @version $Id: DBException.java,v 1.4 2005/03/11 20:28:39 jjanke Exp $
 */
public class DBException extends RuntimeException {
	
	/**
	 * Create a new DBException based on a SQLException
	 * @param e Specicy the Exception cause
	 */
	public DBException(Exception e)
	{
		super(e);
	}	//	DBException

	/**
	 * Create a new DBException
	 * @param msg Message
	 */
	public DBException(String msg) 
	{
		super(msg);
	}	//	DBException

}	//	DBException
