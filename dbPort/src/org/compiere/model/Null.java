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

/**
 * 	Database Null Indicator 
 *	
 *  @author Jorg Janke
 *  @version $Id: Null.java,v 1.1 2005/11/25 21:58:30 jjanke Exp $
 */
public class Null
{
	/** Singleton				*/
	public static final Null	NULL = new Null();
	
	/**
	 * 	NULL Constructor
	 */
	private Null ()
	{
	}	//	Null
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		return "NULL";
	} //	toString
	
}	//	Null