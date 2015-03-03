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

import java.util.*;
import java.util.logging.*;
import org.compiere.model.*;
 

/**
 *	Create Checks from Payment Selection Line
 *	
 *  @author Jorg Janke
 *  @version $Id: PaySelectionCreateCheck.java,v 1.7 2005/09/29 22:01:55 jjanke Exp $
 */
public class PaySelectionCreateCheck extends SvrProcess
{
	/**	Target Payment Rule			*/
	private String		p_PaymentRule = null;
	/**	Payment Selection			*/
	private int			p_C_PaySelection_ID = 0;
	/** The checks					*/
	private ArrayList<MPaySelectionCheck>	m_list = new ArrayList<MPaySelectionCheck>();
	
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
			else if (name.equals("PaymentRule"))
				p_PaymentRule = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_C_PaySelection_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt () throws Exception
	{
		log.info ("doIt - C_PaySelection_ID=" + p_C_PaySelection_ID
			+ ", PaymentRule=" + p_PaymentRule);
		
		MPaySelection psel = new MPaySelection (getCtx(), p_C_PaySelection_ID, get_TrxName());
		if (psel.get_ID() == 0)
			throw new IllegalArgumentException("Not found C_PaySelection_ID=" + p_C_PaySelection_ID);
		if (psel.isProcessed())
			throw new IllegalArgumentException("@Processed@");
		if (p_PaymentRule == null)
			throw new IllegalArgumentException("No PaymentRule");
		//
		MPaySelectionLine[] lines = psel.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MPaySelectionLine line = lines[i];
			if (!line.isActive() || line.isProcessed())
				continue;
			createCheck (line);
		}
		//
		psel.setProcessed(true);
		psel.save();
		
		return "@C_PaySelectionCheck_ID@ - #" + m_list.size();
	}	//	doIt

	/**
	 * 	Create Check from line
	 *	@param line
	 */
	private void createCheck (MPaySelectionLine line)
	{
		//	Try to find one
		for (int i = 0; i < m_list.size(); i++)
		{
			MPaySelectionCheck check = (MPaySelectionCheck)m_list.get(i);
			//	Add to existing
			if (check.getC_BPartner_ID() == line.getInvoice().getC_BPartner_ID())
			{
				check.addLine(line);
				if (!check.save())
					throw new IllegalStateException("Cannot Save MPaySelectionCheck");
				line.setC_PaySelectionCheck_ID(check.getC_PaySelectionCheck_ID());
				line.setProcessed(true);
				if (!line.save())
					throw new IllegalStateException("Cannot Save MPaySelectionLine");
				return;
			}
		}
		//	Create new
		MPaySelectionCheck check = new MPaySelectionCheck(line, p_PaymentRule);
		if (!check.save())
			throw new IllegalStateException("Cannot Save MPaySelectionCheck");
		line.setC_PaySelectionCheck_ID(check.getC_PaySelectionCheck_ID());
		line.setProcessed(true);
		if (!line.save())
			throw new IllegalStateException("Cannot Save MPaySelectionLine");
		m_list.add(check);
	}	//	createCheck
	
}	//	PaySelectionCreateCheck
