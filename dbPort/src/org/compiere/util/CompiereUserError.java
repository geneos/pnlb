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
 *	Compiere User Error.
 *	Cuased by (lack of) user input/selection.
 * 	(No program error)
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereUserError.java,v 1.3 2005/03/11 20:28:40 jjanke Exp $
 */
public class CompiereUserError extends Exception
{
	/**
	 * 	Compiere User Error
	 *	@param message message
	 */
	public CompiereUserError (String message)
	{
		super (message);
	}	//	CompiereUserError

	/**
	 * 	Compiere User Error
	 *	@param message message
	 *	@param fixHint fix hint
	 */
	public CompiereUserError (String message, String fixHint)
	{
		super (message);
		setFixHint(fixHint);
	}	//	CompiereUserError

	/**
	 * 	CompiereUserError
	 *	@param message
	 *	@param cause
	 */
	public CompiereUserError (String message, Throwable cause)
	{
		super (message, cause);
	}	//	CompiereUserError

	/**	Additional Info how to fix	**/
	private String	m_fixHint = null;
	
	/**
	 * @return Returns the fixHint.
	 */
	public String getFixHint ()
	{
		return m_fixHint;
	}	//	getFixHint
	
	/**
	 * 	Set Fix Hint
	 *	@param fixHint fix hint
	 */
	public void setFixHint (String fixHint)
	{
		m_fixHint = fixHint;
	}	//	setFixHint
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		super.toString();
		StringBuffer sb = new StringBuffer ("UserError: ");
		sb.append(getLocalizedMessage());
		if (m_fixHint != null && m_fixHint.length() > 0)
			sb.append(" (").append(m_fixHint).append (")");
		return sb.toString ();
	}	//	toString
	
}	//	CompiereUserError
