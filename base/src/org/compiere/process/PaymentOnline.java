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

//import org.compiere.process.*;
import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Online Payment Process
 *
 *  @author Jorg Janke
 *  @version $Id: PaymentOnline.java,v 1.6 2005/03/11 20:25:59 jjanke Exp $
 */
public class PaymentOnline extends SvrProcess
{
	private int			m_C_Payment_ID = -1;

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
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws Exception
	{
		log.info("doIt - Record_ID=" + getRecord_ID());
		//	get Payment
		MPayment pp = new MPayment (getCtx(), getRecord_ID(), get_TrxName());
		//	Validate Number
		String msg = MPaymentValidate.validateCreditCardNumber(pp.getCreditCardNumber(), pp.getCreditCardType());
		if (msg != null && msg.length() > 0)
			throw new IllegalArgumentException(Msg.getMsg(getCtx(), msg));
		msg = MPaymentValidate.validateCreditCardExp(pp.getCreditCardExpMM(), pp.getCreditCardExpYY());
		if (msg != null && msg.length() > 0)
			throw new IllegalArgumentException(Msg.getMsg(getCtx(), msg));
		if (pp.getCreditCardVV().length() > 0)
		{
			msg = MPaymentValidate.validateCreditCardVV(pp.getCreditCardVV(), pp.getCreditCardType());
			if (msg != null && msg.length() > 0)
				throw new IllegalArgumentException(Msg.getMsg(getCtx(), msg));
		}
		
		//  Process it
		boolean ok = pp.processOnline();
		pp.save();
		if (!ok)
			throw new Exception(pp.getErrorMessage());
		return "OK";
	}	//	doIt

}	//	PaymentOnline
