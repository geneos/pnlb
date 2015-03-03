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

import java.io.*;

/**
 * 	Authorize.net Payment Processor Services Interface
 *	
 *  @author Jorg Janke
 *  @version $Id: PP_Authorize.java,v 1.2 2005/11/20 22:39:48 jjanke Exp $
 */
public class PP_Authorize extends PaymentProcessor
	implements Serializable
{
	/**	Status					*/
	private boolean		m_ok = false;

	/**
	 * 	Process CC
	 *	@return processed ok
	 *	@throws IllegalArgumentException
	 */
	public boolean processCC ()
		throws IllegalArgumentException
	{
		setEncoded(true);
		return m_ok;
	}	//	processCC

	/**
	 * 	Is Processed OK
	 *	@return true if OK
	 */
	public boolean isProcessedOK ()
	{
		return m_ok;
	}	//	isProcessedOK
	
}	//	PP_Authorize
