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

/**
 *  Null Parameter for CPreparedStatement
 *
 *  @author Jorg Janke
 *  @version $Id: NullParameter.java,v 1.6 2005/03/11 20:28:40 jjanke Exp $
 */
public class NullParameter implements Serializable
{
	/**
	 * 	Cosntructor
	 *	@param type SQL Type java.sql.Types.*
	 */
	public NullParameter(int type)
	{
		m_type = type;
	}	//	NullParameter

	private int		m_type = -1;

	/**
	 * 	Get Type
	 *	@return type
	 */
	public int getType()
	{
		return m_type;
	}	//	getType

	/**
	 * 	String representation
	 * 	@return	info
	 */
	public String toString()
	{
		return "NullParameter Type=" + m_type;
	}	//	toString

}	//	NullParameter
