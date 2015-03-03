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
 * 	Compiere System Error.
 * 	Error caused by invalid configurations, etc.
 * 	(No program error)
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereSystemError.java,v 1.3 2005/03/11 20:28:40 jjanke Exp $
 */
public class CompiereSystemError extends Exception
{
	/**
	 * 	Compiere System Error
	 *	@param message message
	 */
	public CompiereSystemError (String message)
	{
		super (message);
	}	//	CompiereSystemError

	/**
	 * 	Compiere System Error
	 *	@param message message
	 *	@param detail detail
	 */
	public CompiereSystemError (String message, Object detail)
	{
		super (message);
		setDetail (detail);
	}	//	CompiereSystemError

	/**
	 * 	Compiere System Error
	 *	@param message
	 *	@param cause
	 */
	public CompiereSystemError (String message, Throwable cause)
	{
		super (message, cause);
	}	//	CompiereSystemError

	/**	Details					*/
	private Object	m_detail = null;
	
	/**
	 * @return Returns the detail.
	 */
	public Object getDetail ()
	{
		return m_detail;
	}
	
	/**
	 * @param detail The detail to set.
	 */
	public void setDetail (Object detail)
	{
		m_detail = detail;
	}
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		super.toString();
		StringBuffer sb = new StringBuffer ("SystemError: ");
		sb.append(getLocalizedMessage());
		if (m_detail != null)
			sb.append(" (").append(m_detail).append (")");
		return sb.toString ();
	}	//	toString

}	//	CompiereSystemError
