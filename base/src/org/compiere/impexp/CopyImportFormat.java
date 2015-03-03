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
package org.compiere.impexp;

import java.math.*;
import java.util.logging.*;
import org.compiere.process.*;


/**
 *	Copy Import Format (lines)
 *	
 *  @author Jorg Janke
 *  @version $Id: CopyImportFormat.java,v 1.6 2005/11/25 21:57:27 jjanke Exp $
 */
public class CopyImportFormat extends SvrProcess
{
	private int from_AD_ImpFormat_ID = 0;
	private int to_AD_ImpFormat_ID = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_ImpFormat_ID"))
				from_AD_ImpFormat_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		to_AD_ImpFormat_ID = getRecord_ID();
	}	//	prepare

	
	/**
	 * 	Process Copy
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("doIt = From=" + from_AD_ImpFormat_ID + " To=" + to_AD_ImpFormat_ID);
		MImpFormat from = new MImpFormat (getCtx(), from_AD_ImpFormat_ID, get_TrxName());
		if (from.getAD_ImpFormat_ID() != from_AD_ImpFormat_ID)
			throw new Exception ("From Format not found - " + from_AD_ImpFormat_ID);
		//
		MImpFormat to = new MImpFormat (getCtx(), to_AD_ImpFormat_ID, get_TrxName());
		if (to.getAD_ImpFormat_ID() != to_AD_ImpFormat_ID)
			throw new Exception ("To Format not found - " + from_AD_ImpFormat_ID);
		//
		if (from.getAD_Table_ID() != to.getAD_Table_ID())
			throw new Exception ("From-To do Not have same Format Table");
		//
		MImpFormatRow[] rows = from.getRows();	//	incl. inactive
		for (int i = 0; i < rows.length; i++)
		{
			MImpFormatRow row = rows[i];
			MImpFormatRow copy = new MImpFormatRow (to, row);
			if (!copy.save())
				throw new Exception ("Copy error");
		}
		
		String msg = "#" + rows.length;
		if (!from.getFormatType().equals(to.getFormatType()))
			return msg + " - Note: Format Type different!";
		return msg;
	}	//	doIt
	
}	//	CopyImportFormat
