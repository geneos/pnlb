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
 *	Copy all Tabs of a Window
 *	
 *  @author Jorg Janke
 *  @version $Id: WindowCopy.java,v 1.5 2005/09/19 04:49:45 jjanke Exp $
 */
public class WindowCopy extends SvrProcess
{
	/**	Window To					*/
	private int			p_AD_WindowTo_ID = 0;
	/**	Window From					*/
	private int			p_AD_WindowFrom_ID = 0;
	
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
			else if (name.equals("AD_Window_ID"))
				p_AD_WindowFrom_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_AD_WindowTo_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		log.info("doIt - To AD_Window_ID=" + p_AD_WindowTo_ID + ", From=" + p_AD_WindowFrom_ID);
		M_Window from = new M_Window (getCtx(), p_AD_WindowFrom_ID, get_TrxName());
		if (from.get_ID() == 0)
			throw new CompiereUserError("@NotFound@ (from->) @AD_Window_ID@");
		M_Window to = new M_Window (getCtx(), p_AD_WindowTo_ID, get_TrxName());
		if (to.get_ID() == 0)
			throw new CompiereUserError("@NotFound@ (to<-) @AD_Window_ID@");
		
		int tabCount = 0;
		int fieldCount = 0;
		M_Tab[] oldTabs = from.getTabs(false, get_TrxName());
		for (int i = 0; i < oldTabs.length; i++)
		{
			M_Tab oldTab = oldTabs[i];
			M_Tab newTab = new M_Tab (to, oldTab);
			if (newTab.save())
			{
				tabCount++;
				//	Copy Fields
				M_Field[] oldFields = oldTab.getFields(false, get_TrxName());
				for (int j = 0; j < oldFields.length; j++)
				{
					M_Field oldField = oldFields[j];
					M_Field newField = new M_Field (newTab, oldField);
					if (newField.save())
						fieldCount++;
					else
						throw new CompiereUserError("@Error@ @AD_Field_ID@");
				}
			}
			else
				throw new CompiereUserError("@Error@ @AD_Tab_ID@");
		}
		
		return "@Copied@ #" + tabCount + "/" + fieldCount;
	}	//	doIt

}	//	WindowCopy
