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
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Copy Tab Fields
 *	
 *  @author Jorg Janke
 *  @version $Id: TabCopy.java,v 1.6 2005/09/19 04:49:45 jjanke Exp $
 */
public class TabCopy extends SvrProcess
{
	/**	Tab	To					*/
	private int			p_AD_TabTo_ID = 0;
	/**	Tab	From				*/
	private int			p_AD_TabFrom_ID = 0;

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
			else if (name.equals("AD_Tab_ID"))
				p_AD_TabFrom_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_AD_TabTo_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		log.info("To AD_Tab_ID=" + p_AD_TabTo_ID + ", From=" + p_AD_TabFrom_ID);
		M_Tab from = new M_Tab (getCtx(), p_AD_TabFrom_ID, get_TrxName());
		if (from.get_ID() == 0)
			throw new CompiereUserError("@NotFound@ (from->) @AD_Tab_ID@");
		M_Tab to = new M_Tab (getCtx(), p_AD_TabTo_ID, get_TrxName());
		if (to.get_ID() == 0)
			throw new CompiereUserError("@NotFound@ (to<-) @AD_Tab_ID@");
		if (from.getAD_Table_ID() != to.getAD_Table_ID())
			throw new CompiereUserError("@Error@ @AD_Table_ID@");
		
		int count = 0;
		M_Field[] oldFields = from.getFields(false, get_TrxName());
		for (int i = 0; i < oldFields.length; i++)
		{
			M_Field oldField = oldFields[i];
			M_Field newField = new M_Field (to, oldField);
			if (newField.save())
				count++;
			else
				throw new CompiereUserError("@Error@ @AD_Field_ID@");
		}
		
		return "@Copied@ #" + count;
	}	//	doIt

}	//	TabCopy
