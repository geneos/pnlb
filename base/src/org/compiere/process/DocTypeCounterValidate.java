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
package org.compiere.process;

import org.compiere.model.*;

/**
 *	Validate Counter Document
 *	
 *  @author Jorg Janke
 *  @version $Id: DocTypeCounterValidate.java,v 1.8 2005/09/19 04:49:45 jjanke Exp $
 */
public class DocTypeCounterValidate extends SvrProcess
{
	/**	Counter Document		*/
	private int					p_C_DocTypeCounter_ID = 0;
	/**	Document Relationship	*/
	private MDocTypeCounter		m_counter = null;
	
	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		p_C_DocTypeCounter_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Do It
	 *	@return message
	 */
	protected String doIt () throws Exception
	{
		log.info("C_DocTypeCounter_ID=" + p_C_DocTypeCounter_ID);
		m_counter = new MDocTypeCounter (getCtx(), p_C_DocTypeCounter_ID, get_TrxName());
		if (m_counter == null || m_counter.get_ID() == 0)
			throw new IllegalArgumentException("Not found C_DocTypeCounter_ID=" + p_C_DocTypeCounter_ID);
		//
		String error = m_counter.validate();
		m_counter.save();
		if (error != null)
			throw new Exception(error);
		
		return "OK";
	}	//	doIt
	
}	//	DocTypeCounterValidate
