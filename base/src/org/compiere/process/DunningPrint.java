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

import java.io.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.print.*;
import org.compiere.util.*;



/**
 *	Dunning Letter Print
 *	
 *  @author Jorg Janke
 *  @version $Id: DunningPrint.java,v 1.11 2005/11/13 22:21:21 jjanke Exp $
 */
public class DunningPrint extends SvrProcess
{
	/**	Mail PDF				*/
	private boolean		p_EMailPDF = false;
	/** Mail Template			*/
	private int			p_R_MailText_ID = 0;
	/** Dunning Run				*/
	private int			p_C_DunningRun_ID = 0;
	/** Print only Outstanding	*/
	private boolean		p_IsOnlyIfBPBalance = true;
	
	
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
			else if (name.equals("EMailPDF"))
				p_EMailPDF = "Y".equals(para[i].getParameter());
			else if (name.equals("R_MailText_ID"))
				p_R_MailText_ID = para[i].getParameterAsInt();
			else if (name.equals("C_DunningRun_ID"))
				p_C_DunningRun_ID = para[i].getParameterAsInt();
			else if (name.equals("IsOnlyIfBPBalance"))
				p_IsOnlyIfBPBalance = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Pocess
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("C_DunningRun_ID=" + p_C_DunningRun_ID + ",R_MailText_ID=" + p_R_MailText_ID 
			+ ", EmailPDF=" + p_EMailPDF + ",IsOnlyIfBPBalance=" + p_IsOnlyIfBPBalance);
		
		//	Need to have Template
		if (p_EMailPDF && p_R_MailText_ID == 0)
			throw new CompiereUserError ("@NotFound@: @R_MailText_ID@");
		String subject = "";
		MMailText mText = null;
		if (p_EMailPDF)
		{
			mText = new MMailText (getCtx(), p_R_MailText_ID, get_TrxName());
			if (p_EMailPDF && mText.get_ID() == 0)
				throw new CompiereUserError ("@NotFound@: @R_MailText_ID@ - " + p_R_MailText_ID);
			subject = mText.getMailHeader();
		}
		//
		MDunningRun run = new MDunningRun (getCtx(), p_C_DunningRun_ID, get_TrxName());
		if (run.get_ID() == 0)
			throw new CompiereUserError ("@NotFound@: @C_DunningRun_ID@ - " + p_C_DunningRun_ID);
		//	Print Format on Dunning Level
		MDunningLevel level = new MDunningLevel (getCtx(), run.getC_DunningLevel_ID(), get_TrxName());
		MPrintFormat format = MPrintFormat.get (getCtx(), level.getDunning_PrintFormat_ID(), false);
		
		MClient client = MClient.get(getCtx());
		
		int count = 0;
		int errors = 0;
		MDunningRunEntry[] entries = run.getEntries(false);
		for (int i = 0; i < entries.length; i++)
		{
			MDunningRunEntry entry = entries[i];
			if (p_IsOnlyIfBPBalance && entry.getAmt().signum() <= 0)
				continue;
			//	To BPartner
			MBPartner bp = new MBPartner (getCtx(), entry.getC_BPartner_ID(), get_TrxName());
			if (bp.get_ID() == 0)
			{
				addLog (entry.get_ID(), null, null, "@NotFound@: @C_BPartner_ID@ " + entry.getC_BPartner_ID());
				errors++;
				continue;
			}
			//	To User
			MUser to = new MUser (getCtx(), entry.getAD_User_ID(), get_TrxName());
			if (p_EMailPDF)
			{
				if (to.get_ID() == 0)
				{
					addLog (entry.get_ID(), null, null, "@NotFound@: @AD_User_ID@ - " + bp.getName());
					errors++;
					continue;
				}
				else if (to.getEMail() == null || to.getEMail().length() == 0)
				{
					addLog (entry.get_ID(), null, null, "@NotFound@: @EMail@ - " + to.getName());
					errors++;
					continue;
				}
			}
			//	BP Language
			Language language = Language.getLoginLanguage();		//	Base Language
			String tableName = "C_Dunning_Header_v";
			if (client.isMultiLingualDocument())
			{
				tableName += "t";
				String AD_Language = bp.getAD_Language();
				if (AD_Language != null)
					language = Language.getLanguage(AD_Language);
			}
			format.setLanguage(language);
			format.setTranslationLanguage(language);
			//	query
			MQuery query = new MQuery(tableName);
			query.addRestriction("C_DunningRunEntry_ID", MQuery.EQUAL, 
				new Integer(entry.getC_DunningRunEntry_ID()));

			//	Engine
			PrintInfo info = new PrintInfo(
				bp.getName(),
				X_C_DunningRunEntry.getTableId(X_C_DunningRunEntry.Table_Name),
				entry.getC_DunningRunEntry_ID(),
				entry.getC_BPartner_ID());
			info.setDescription(bp.getName() + ", Amt=" + entry.getAmt());
			ReportEngine re = new ReportEngine(getCtx(), format, query, info);
			boolean printed = false;
			if (p_EMailPDF)
			{
				EMail email = client.createEMail(to.getEMail(), null, null);
				if (!email.isValid())
				{
					addLog (entry.get_ID(), null, null, 
						"@RequestActionEMailError@ Invalid EMail: " + to);
					errors++;
					continue;
				}
				mText.setUser(to);	//	variable context
				mText.setBPartner(bp);
				mText.setPO(entry);
				String message = mText.getMailText(true);
				if (mText.isHtml())
					email.setMessageHTML(mText.getMailHeader(), message);
				else
				{
					email.setSubject (mText.getMailHeader());
					email.setMessageText (message);
				}
				//
				File attachment = re.getPDF(File.createTempFile("Dunning", ".pdf"));
				log.fine(to + " - " + attachment);
				email.addAttachment(attachment);
				//
				String msg = email.send();
				MUserMail um = new MUserMail(mText, entry.getAD_User_ID(), email);
				um.save();
				if (msg.equals(EMail.SENT_OK))
				{
					addLog (entry.get_ID(), null, null,
						bp.getName() + " @RequestActionEMailOK@");
					count++;
					printed = true;
				}
				else
				{
					addLog (entry.get_ID(), null, null,
						bp.getName() + " @RequestActionEMailError@ " + msg);
					errors++;
				}
			}
			else
			{
				re.print ();
				count++;
				printed = true;
			}

		}	//	for all dunning letters
		if (p_EMailPDF)
			return "@Sent@=" + count + " - @Errors@=" + errors;
		return "@Printed@=" + count;
	}	//	doIt
	
}	//	DunningPrint
