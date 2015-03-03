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

import java.util.logging.*;
import org.compiere.model.*;

/**
 *	Create RfQ Response from RfQ Topic
 *	
 *  @author Jorg Janke
 *  @version $Id: RfQCreate.java,v 1.9 2005/09/19 04:49:45 jjanke Exp $
 */
public class RfQCreate extends SvrProcess
{
	/**	Send RfQ				*/
	private boolean	p_IsSendRfQ = false;
	/**	RfQ						*/
	private int		p_C_RfQ_ID = 0;
	
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
			else if (name.equals("IsSendRfQ"))
				p_IsSendRfQ = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_RfQ_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (translated text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		MRfQ rfq = new MRfQ (getCtx(), p_C_RfQ_ID, get_TrxName());
		log.info("doIt - " + rfq + ", Send=" + p_IsSendRfQ);
		String error = rfq.checkQuoteTotalAmtOnly();
		if (error != null && error.length() > 0)
			throw new Exception (error);

		int counter = 0;
		int sent = 0;
		int notSent = 0;
		
		//	Get all existing responses
		MRfQResponse[] responses = rfq.getResponses (false, false);
		
		//	Topic
		MRfQTopic topic = new MRfQTopic (getCtx(), rfq.getC_RfQ_Topic_ID(), get_TrxName());
		MRfQTopicSubscriber[] subscribers = topic.getSubscribers();
		for (int i = 0; i < subscribers.length; i++)
		{
			MRfQTopicSubscriber subscriber = subscribers[i];
			boolean skip = false;
			//	existing response
			for (int r = 0; r < responses.length; r++)
			{
				if (subscriber.getC_BPartner_ID() == responses[r].getC_BPartner_ID()
					&& subscriber.getC_BPartner_Location_ID() == responses[r].getC_BPartner_Location_ID())
				{
					skip = true;
					break;
				}
			}
			if (skip)
				continue;
			
			//	Create Response
			MRfQResponse response = new MRfQResponse (rfq, subscriber);
			if (response.get_ID() == 0)	//	no lines
				continue;
			
			counter++;
			if (p_IsSendRfQ)
			{
				if (response.sendRfQ())
					sent++;
				else
					notSent++;
			}
		}	//	for all subscribers

		String retValue = "@Created@ " + counter;
		if (p_IsSendRfQ)
			retValue += " - @IsSendRfQ@=" + sent + " - @Error@=" + notSent;
		return retValue;
	}	//	doIt
	
}	//	RfQCreate
