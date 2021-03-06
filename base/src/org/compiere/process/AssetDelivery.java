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

import java.net.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Deliver Assets Electronically
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: AssetDelivery.java,v 1.24 2005/07/18 03:47:43 jjanke Exp $
 */
public class AssetDelivery extends SvrProcess
{
	private MClient		m_client = null;

	private int			m_A_Asset_Group_ID = 0;
	private int			m_M_Product_ID = 0;
	private int			m_C_BPartner_ID = 0;
	private int			m_A_Asset_ID = 0;
	private Timestamp	m_GuaranteeDate = null;
	private int			m_NoGuarantee_MailText_ID = 0;
	private boolean		m_AttachAsset = false;
	//
	private MMailText	m_MailText = null;


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
			else if (name.equals("A_Asset_Group_ID"))
				m_A_Asset_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Product_ID"))
				m_M_Product_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				m_C_BPartner_ID = para[i].getParameterAsInt();
			else if (name.equals("A_Asset_ID"))
				m_A_Asset_ID = para[i].getParameterAsInt();
			else if (name.equals("GuaranteeDate"))
				m_GuaranteeDate = (Timestamp)para[i].getParameter();
			else if (name.equals("NoGuarantee_MailText_ID"))
				m_NoGuarantee_MailText_ID = para[i].getParameterAsInt();
			else if (name.equals("AttachAsset"))
				m_AttachAsset = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_GuaranteeDate == null)
			m_GuaranteeDate = new Timestamp (System.currentTimeMillis());
		//
		m_client = MClient.get(getCtx());
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message to be translated
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		log.info("");
		long start = System.currentTimeMillis();

		//	Test
		if (m_client.getSMTPHost() == null)
			throw new Exception ("No Client SMTP Info");
		if (m_client.getRequestEMail() == null)
			throw new Exception ("No Client Request User");

		//	Asset selected
		if (m_A_Asset_ID != 0)
		{
			String msg = deliverIt (m_A_Asset_ID);
			addLog (m_A_Asset_ID, null, null, msg);
			return msg;
		}
		//
		StringBuffer sql = new StringBuffer ("SELECT A_Asset_ID, GuaranteeDate "
			+ "FROM A_Asset a"
			+ " INNER JOIN M_Product p ON (a.M_Product_ID=p.M_Product_ID) "
			+ "WHERE ");
		if (m_A_Asset_Group_ID != 0)
			sql.append("a.A_Asset_Group_ID=").append(m_A_Asset_Group_ID).append(" AND ");
		if (m_M_Product_ID != 0)
			sql.append("p.M_Product_ID=").append(m_M_Product_ID).append(" AND ");
		if (m_C_BPartner_ID != 0)
			sql.append("a.C_BPartner_ID=").append(m_C_BPartner_ID).append(" AND ");
		String s = sql.toString();
		if (s.endsWith(" WHERE "))
			throw new Exception ("@RestrictSelection@");
		//	No mail to expired
		if (m_NoGuarantee_MailText_ID == 0)
		{
			sql.append("TRUNC(GuaranteeDate) >= ").append(DB.TO_DATE(m_GuaranteeDate, true));
			s = sql.toString();
		}
		//	Clean up
		if (s.endsWith(" AND "))
			s = sql.substring(0, sql.length()-5);
		//
		Statement stmt = null;
		int count = 0;
		int errors = 0;
		int reminders = 0;
		try
		{
			stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(s);
			while (rs.next())
			{
				int A_Asset_ID = rs.getInt(1);
				Timestamp GuaranteeDate = rs.getTimestamp(2);

				//	Guarantee Expired
				if (GuaranteeDate != null && GuaranteeDate.before(m_GuaranteeDate))
				{
					if (m_NoGuarantee_MailText_ID != 0)
					{
						sendNoGuaranteeMail (A_Asset_ID, m_NoGuarantee_MailText_ID, get_TrxName());
						reminders++;
					}
				}
				else	//	Guarantee valid
				{
					String msg = deliverIt (A_Asset_ID);
					addLog (A_Asset_ID, null, null, msg);
					if (msg.startsWith ("** "))
						errors++;
					else
						count++;
				}
			}
			rs.close();
			stmt.close();
			stmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, s, e);
		}
		finally
		{
			try
			{
				if (stmt != null)
					stmt.close ();
			}
			catch (Exception e)
			{}
			stmt = null;
		}
		log.info("Count=" + count + ", Errors=" + errors + ", Reminder=" + reminders
			+ " - " + (System.currentTimeMillis()-start) + "ms");
		return "@Sent@=" + count + " - @Errors@=" + errors;
	}	//	doIt


	/**
	 * 	Send No Guarantee EMail
	 * 	@param A_Asset_ID asset
	 * 	@param R_MailText_ID mail to send
	 * 	@return message - delivery errors start with **
	 */
	private String sendNoGuaranteeMail (int A_Asset_ID, int R_MailText_ID, String trxName)
	{
		MAsset asset = new MAsset (getCtx(), A_Asset_ID, trxName);
		if (asset.getAD_User_ID() == 0)
			return "** No Asset User";
		MUser user = new MUser (getCtx(), asset.getAD_User_ID(), get_TrxName());
		if (user.getEMail() == null || user.getEMail().length() == 0)
			return "** No Asset User Email";
		if (m_MailText == null || m_MailText.getR_MailText_ID() != R_MailText_ID)
			m_MailText = new MMailText (getCtx(), R_MailText_ID, get_TrxName());
		if (m_MailText.getMailHeader() == null || m_MailText.getMailHeader().length() == 0)
			return "** No Subject";

		//	Create Mail
		EMail email = m_client.createEMail(user.getEMail(), null, null);
		m_MailText.setPO(user);
		m_MailText.setPO(asset);
		String message = m_MailText.getMailText(true);
		if (m_MailText.isHtml())
			email.setMessageHTML(m_MailText.getMailHeader(), message);
		else
		{
			email.setSubject (m_MailText.getMailHeader());
			email.setMessageText (message);
		}
		String msg = email.send();
		new MUserMail(m_MailText, asset.getAD_User_ID(), email).save();
		if (!EMail.SENT_OK.equals(msg))
			return "** Not delivered: " + user.getEMail() + " - " + msg;
		//
		return user.getEMail();
	}	//	sendNoGuaranteeMail

	
	/**************************************************************************
	 * 	Deliver Asset
	 * 	@param A_Asset_ID asset
	 * 	@return message - delivery errors start with **
	 */
	private String deliverIt (int A_Asset_ID)
	{
		log.fine("A_Asset_ID=" + A_Asset_ID);
		long start = System.currentTimeMillis();
		//
		MAsset asset = new MAsset (getCtx(), A_Asset_ID, get_TrxName());
		if (asset.getAD_User_ID() == 0)
			return "** No Asset User";
		MUser user = new MUser (getCtx(), asset.getAD_User_ID(), get_TrxName());
		if (user.getEMail() == null || user.getEMail().length() == 0)
			return "** No Asset User Email";
		if (asset.getProductR_MailText_ID() == 0)
			return "** Product Mail Text";
		if (m_MailText == null || m_MailText.getR_MailText_ID() != asset.getProductR_MailText_ID())
			m_MailText = new MMailText (getCtx(), asset.getProductR_MailText_ID(), get_TrxName());
		if (m_MailText.getMailHeader() == null || m_MailText.getMailHeader().length() == 0)
			return "** No Subject";

		//	Create Mail
		EMail email = m_client.createEMail(user.getEMail(), null, null);
		if (!email.isValid())
		{
			asset.setHelp(asset.getHelp() + " - Invalid EMail");
			asset.setIsActive(false);
			return "** Invalid EMail: " + user.getEMail();
		}
		if (m_client.isSmtpAuthorization())
			email.createAuthenticator(m_client.getRequestUser(), m_client.getRequestUserPW());
		m_MailText.setUser(user);
		m_MailText.setPO(asset);
		String message = m_MailText.getMailText(true);
		if (m_MailText.isHtml() || m_AttachAsset)
			email.setMessageHTML(m_MailText.getMailHeader(), message);
		else
		{
			email.setSubject (m_MailText.getMailHeader());
			email.setMessageText (message);
		}
		if (m_AttachAsset)
		{
			MProductDownload[] pdls = asset.getProductDownloads();
			if (pdls != null)
			{
				for (int i = 0; i < pdls.length; i++)
				{
					URL url = pdls[i].getDownloadURL(m_client.getDocumentDir());
					if (url != null)
						email.addAttachment(url);
				}
			}
			else
				log.warning("No DowloadURL for A_Asset_ID=" + A_Asset_ID);
		}
		String msg = email.send();
		new MUserMail(m_MailText, asset.getAD_User_ID(), email).save();
		if (!EMail.SENT_OK.equals(msg))
			return "** Not delivered: " + user.getEMail() + " - " + msg;

		MAssetDelivery ad = asset.confirmDelivery(email, user.getAD_User_ID());
		ad.save();
		asset.save();
		//
		log.fine((System.currentTimeMillis()-start) + " ms");
		//	success
		return user.getEMail() + " - " + asset.getProductVersionNo();
	}	//	deliverIt

}	//	AssetDelivery
