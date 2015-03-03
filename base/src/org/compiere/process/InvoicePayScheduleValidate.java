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

import java.math.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Validate Invoice Payment Schedule
 *	
 *  @author Jorg Janke
 *  @version $Id: InvoicePayScheduleValidate.java,v 1.7 2005/09/19 04:49:45 jjanke Exp $
 */
public class InvoicePayScheduleValidate extends SvrProcess
{
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
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		log.info ("C_InvoicePaySchedule_ID=" + getRecord_ID());
		MInvoicePaySchedule[] schedule = MInvoicePaySchedule.getInvoicePaySchedule
			(getCtx(), 0, getRecord_ID(), null);
		if (schedule.length == 0)
			throw new IllegalArgumentException("InvoicePayScheduleValidate - No Schedule");
		//	Get Invoice
		MInvoice invoice = new MInvoice (getCtx(), schedule[0].getC_Invoice_ID(), null);
		if (invoice.get_ID() == 0)
			throw new IllegalArgumentException("InvoicePayScheduleValidate - No Invoice");
		//
		BigDecimal total = Env.ZERO;
		for (int i = 0; i < schedule.length; i++)
		{
			BigDecimal due = schedule[i].getDueAmt();
			if (due != null)
				total = total.add(due);
		}
		boolean valid = invoice.getGrandTotal().compareTo(total) == 0;
		invoice.setIsPayScheduleValid(valid);
		invoice.save();
		//	Schedule
		for (int i = 0; i < schedule.length; i++)
		{
			if (schedule[i].isValid() != valid)
			{
				schedule[i].setIsValid(valid);
				schedule[i].save();				
			}
		}
		String msg = "@OK@";
		if (!valid)
			msg = "@GrandTotal@ = " + invoice.getGrandTotal() 
				+ " <> @Total@ = " + total 
				+ "  - @Difference@ = " + invoice.getGrandTotal().subtract(total); 
		return Msg.parseTranslation(getCtx(), msg);
	}	//	doIt

}	//	InvoicePayScheduleValidate
