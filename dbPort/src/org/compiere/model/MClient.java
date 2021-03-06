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
package org.compiere.model;

import java.rmi.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.mail.internet.*;
import java.io.*;

import org.compiere.db.*;
import org.compiere.interfaces.*;
import org.compiere.util.*;

/**
 *  Client Model
 *
 *  @author Jorg Janke
 *  @version $Id: MClient.java,v 1.30 2005/11/12 22:59:39 jjanke Exp $
 */
public class MClient extends X_AD_Client
{
	/**
	 * 	Get client
	 *	@param ctx context
	 * 	@param AD_Client_ID id
	 *	@return client
	 */
	public static MClient get (Properties ctx, int AD_Client_ID)
	{
		Integer key = new Integer (AD_Client_ID);
		MClient client = (MClient)s_cache.get(key);
		if (client != null)
			return client;
		client = new MClient (ctx, AD_Client_ID, null);
		s_cache.put (key, client);
		return client;
	}	//	get

	/**
	 * 	Get all clients
	 *	@param ctx context
	 *	@return clients
	 */
	public static MClient[] getAll (Properties ctx)
	{
		ArrayList<MClient> list = new ArrayList<MClient>();
		String sql = "SELECT * FROM AD_Client";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MClient client = new MClient (ctx, rs, null);
				s_cache.put (new Integer (client.getAD_Client_ID()), client);
				list.add (client);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		MClient[] retValue = new MClient[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get optionally cached client
	 *	@param ctx context
	 *	@return client
	 */
	public static MClient get (Properties ctx)
	{
		return get (ctx, Env.getAD_Client_ID(ctx));
	}	//	get

	/**	Static Logger				*/
	private static CLogger	s_log	= CLogger.getCLogger (MClient.class);
	/**	Cache						*/
	private static CCache<Integer,MClient>	s_cache = new CCache<Integer,MClient>("AD_Client", 3);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param AD_Client_ID id
	 * 	@param createNew create new
	 */
	public MClient (Properties ctx, int AD_Client_ID, boolean createNew, String trxName)
	{
		super (ctx, AD_Client_ID, trxName);
		m_createNew = createNew;
		if (AD_Client_ID == 0)
		{
			if (m_createNew)
			{
			//	setValue (null);
			//	setName (null);
				setAD_Org_ID(0);
				setIsMultiLingualDocument (false);
				setIsSmtpAuthorization (false);	
				setIsUseBetaFunctions (true);
				setIsServerEMail(false);
				setAD_Language(Language.getBaseAD_Language());
				setAutoArchive(AUTOARCHIVE_None);
				setMMPolicy (MMPOLICY_FiFo);	// F
				setIsPostImmediate(false);
				setIsCostImmediate(false);
			}
			else
				load(get_TrxName());
		}
	}	//	MClient

	/**
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param AD_Client_ID id
	 */
	public MClient (Properties ctx, int AD_Client_ID, String trxName)
	{
		this (ctx, AD_Client_ID, false, trxName);
	}	//	MClient

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MClient (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MClient

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public MClient (Properties ctx, String trxName)
	{
		this (ctx, Env.getAD_Client_ID(ctx), trxName);
	}	//	MClient

	/**	Client Info					*/
	private MClientInfo 		m_info = null;
	/** Language					*/
	private Language			m_language = null;
	/** New Record					*/
	private boolean				m_createNew = false;
	/** Client Info Setup Tree for Account	*/
	private int					m_AD_Tree_Account_ID;

	/**
	 *	Get SMTP Host
	 *	@return SMTP or loaclhost
	 */
	public String getSMTPHost()
	{
		String s = super.getSMTPHost();
		if (s == null)
			s = "localhost";
		return s;
	}	//	getSMTPHost

	/**
	 *	Get Client Info
	 *	@return Client Info
	 */
	public MClientInfo getInfo()
	{
		if (m_info == null)
			m_info = MClientInfo.get (getCtx(), getAD_Client_ID(), get_TrxName());
		return m_info;
	}	//	getMClientInfo

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MClient[")
			.append(get_ID()).append("-").append(getValue())
			.append("]");
		return sb.toString();
	}	//	toString

	/**
	 *	Get Default Accounting Currency
	 *	@return currency or 0
	 */
	public int getC_Currency_ID()
	{
		if (m_info == null)
			getInfo();
		if (m_info != null)
			return m_info.getC_Currency_ID();
		return 0;
	}	//	getC_Currency_ID

	/**
	 * 	Get Language
	 *	@return client language
	 */
	public Language getLanguage()
	{
		if (m_language == null)
		{
			m_language = Language.getLanguage(getAD_Language());
			Env.verifyLanguage (getCtx(), m_language);
		}
		return m_language;
	}	//	getLanguage
	
	
	/**
	 * 	Set AD_Language
	 *	@param AD_Language new language
	 */
	public void setAD_Language (String AD_Language)
	{
		m_language = null;
		super.setAD_Language (AD_Language);
	}	//	setAD_Language
	
	/**
	 * 	Get AD_Language
	 *	@return Language
	 */
	public String getAD_Language ()
	{
		String s = super.getAD_Language ();
		if (s == null)
			return Language.getBaseAD_Language();
		return s;
	}	//	getAD_Language

	/**
	 * 	Get Locale
	 *	@return locale
	 */
	public Locale getLocale()
	{
		Language lang = getLanguage();
		if (lang != null)
			return lang.getLocale();
		return Locale.getDefault();
	}	//	getLocale
	
	
	/**************************************************************************
	 * 	Create Trees and Setup Client Info
	 */
	public boolean setupClientInfo (String language)
	{
		//	Create Trees
		String sql = null;
		if (Env.isBaseLanguage (language, "AD_Ref_List"))	//	Get TreeTypes & Name
			sql = "SELECT Value, Name FROM AD_Ref_List WHERE AD_Reference_ID=120 AND IsActive='Y'";
		else
			sql = "SELECT l.Value, t.Name FROM AD_Ref_List l, AD_Ref_List_Trl t "
				+ "WHERE l.AD_Reference_ID=120 AND l.AD_Ref_List_ID=t.AD_Ref_List_ID AND l.IsActive='Y'";

		//  Tree IDs
		int AD_Tree_Org_ID=0, AD_Tree_BPartner_ID=0, AD_Tree_Project_ID=0,
			AD_Tree_SalesRegion_ID=0, AD_Tree_Product_ID=0,
			AD_Tree_Campaign_ID=0, AD_Tree_Activity_ID=0;

		boolean success = false;
		try
		{
			PreparedStatement stmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = stmt.executeQuery();
			MTree_Base tree = null;
			while (rs.next())
			{
				String value = rs.getString(1);
				String name = getName() + " " + rs.getString(2);
				//
				if (value.equals(MTree_Base.TREETYPE_Organization))
				{
					tree = new MTree_Base (this, name, value);
					success = tree.save();
					AD_Tree_Org_ID = tree.getAD_Tree_ID();
				}
				else if (value.equals(MTree_Base.TREETYPE_BPartner))
				{
					tree = new MTree_Base (this, name, value);
					success = tree.save();
					AD_Tree_BPartner_ID = tree.getAD_Tree_ID();
				}
				else if (value.equals(MTree_Base.TREETYPE_Project))
				{
					tree = new MTree_Base (this, name, value);
					success = tree.save();
					AD_Tree_Project_ID = tree.getAD_Tree_ID();
				}
				else if (value.equals(MTree_Base.TREETYPE_SalesRegion))
				{
					tree = new MTree_Base (this, name, value);
					success = tree.save();
					AD_Tree_SalesRegion_ID = tree.getAD_Tree_ID();
				}
				else if (value.equals(MTree_Base.TREETYPE_Product))
				{
					tree = new MTree_Base (this, name, value);
					success = tree.save();
					AD_Tree_Product_ID = tree.getAD_Tree_ID();
				}
				else if (value.equals(MTree_Base.TREETYPE_ElementValue))
				{
					tree = new MTree_Base (this, name, value);
					success = tree.save();
					m_AD_Tree_Account_ID = tree.getAD_Tree_ID();
				}
				else if (value.equals(MTree_Base.TREETYPE_Campaign))
				{
					tree = new MTree_Base (this, name, value);
					success = tree.save();
					AD_Tree_Campaign_ID = tree.getAD_Tree_ID();
				}
				else if (value.equals(MTree_Base.TREETYPE_Activity))
				{
					tree = new MTree_Base (this, name, value);
					success = tree.save();
					AD_Tree_Activity_ID = tree.getAD_Tree_ID();
				}
				else if (value.equals(MTree_Base.TREETYPE_Menu))	//	No Menu
					success = true;
				else	//	PC (Product Category), BB (BOM)
				{
					tree = new MTree_Base (this, name, value);
					success = tree.save();
				}
				if (!success)
				{
					log.log(Level.SEVERE, "Tree NOT created: " + name);
					break;
				}
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e1)
		{
			log.log(Level.SEVERE, "Trees", e1);
			success = false;
		}

		if (!success)
			return false;

		//	Create ClientInfo
		MClientInfo clientInfo = new MClientInfo (this,
			AD_Tree_Org_ID, AD_Tree_BPartner_ID, AD_Tree_Project_ID, 
			AD_Tree_SalesRegion_ID, AD_Tree_Product_ID,
			AD_Tree_Campaign_ID, AD_Tree_Activity_ID, get_TrxName());
		success = clientInfo.save();
		return success;
	}	//	createTrees

	/**
	 * 	Get AD_Tree_Account_ID created in setup client info 
	 *	@return Account Tree ID
	 */
	public int getSetup_AD_Tree_Account_ID()
	{
		return m_AD_Tree_Account_ID;
	}	//	getSetup_AD_Tree_Account_ID

	/**
	 * 	Is Auto Archive on
	 *	@return true if auto archive
	 */
	public boolean isAutoArchive()
	{
		String aa = getAutoArchive();
		return aa != null && !aa.equals(AUTOARCHIVE_None);
	}	//	isAutoArchive
	
	
	/**
	 * 	Update Trl Tables automatically?
	 * 	@param TableName table name
	 *	@return true if automatically translated
	 */
	public boolean isAutoUpdateTrl (String TableName)
	{
		if (super.isMultiLingualDocument())
			return false;
		if (TableName == null)
			return false;
		//	Not Multi-Lingual Documents - only Doc Related
		if (TableName.startsWith("AD"))
			return false;
		return true;
	}	//	isMultiLingualDocument
	
	/**
	 *	Get Primary Accounting Schema
	 *	@return Acct Schema or null
	 */
	public MAcctSchema getAcctSchema()
	{
		if (m_info == null)
			m_info = MClientInfo.get (getCtx(), getAD_Client_ID(), get_TrxName());
		if (m_info != null)
		{
			int C_AcctSchema_ID = m_info.getC_AcctSchema1_ID();
			if (C_AcctSchema_ID != 0)
				return MAcctSchema.get(getCtx(), C_AcctSchema_ID);
		}
		return null;
	}	//	getMClientInfo
	
	/**
	 * 	Save
	 *	@return true if saved
	 */
	public boolean save ()
	{
		if (get_ID() == 0 && !m_createNew)
			return saveUpdate();
		return super.save ();
	}	//	save
	
	
	/**************************************************************************
	 * 	Test EMail
	 *	@return OK or error
	 */
	public String testEMail()
	{
		if (getRequestEMail() == null || getRequestEMail().length() == 0)
			return "No Request EMail for " + getName();
		//
		EMail email = createEMail (getRequestEMail(),
			"Compiere EMail Test", 
			"Compiere EMail Test: " + toString());
		if (email == null)
			return "Could not create EMail: " + getName();
		try
		{
			String msg = email.send();
			if (EMail.SENT_OK.equals (msg))
			{
				log.info("Sent Test EMail to " + getRequestEMail());
				return "OK";
			}
			else
			{
				log.warning("Could NOT send Test EMail from "
					+ getSMTPHost() + ": " + getRequestEMail()
					+ " (" + getRequestUser()
					+ ") to " + getRequestEMail() + ": " + msg);
				return msg;
			}
		}
		catch (Exception ex)
		{
			log.severe(getName() + " - " + ex.getLocalizedMessage());
			return ex.getLocalizedMessage();
		}
	}	//	testEMail
	
	/**
	 * 	Send EMail from Request User - with trace
	 *	@param AD_User_ID recipient
	 *	@param subject subject
	 *	@param message message
	 *	@param attachment optional attachment
	 *	@return true if sent
	 */
	public boolean sendEMail (int AD_User_ID, 
		String subject, String message, File attachment)
	{
		MUser to = MUser.get(getCtx(), AD_User_ID);
		String toEMail = to.getEMail(); 
		if (toEMail == null || toEMail.length() == 0)
		{
			log.warning("No EMail for recipient: " + to);
			return false;
		}
		EMail email = createEMail(null, to, subject, message);
		if (email == null)
			return false;
		if (attachment != null)
			email.addAttachment(attachment);
		try
		{
			return sendEmailNow(null, to, email);
		}
		catch (Exception ex)
		{
			log.severe(getName() + " - " + ex.getLocalizedMessage());
			return false;
		}
	}	//	sendEMail
	
	/**
	 * 	Send EMail from Request User - no trace
	 *	@param to recipient email address
	 *	@param subject subject
	 *	@param message message
	 *	@param attachment optional attachment
	 *	@return true if sent
	 */
	public boolean sendEMail (String to, 
		String subject, String message, File attachment)
	{
		EMail email = createEMail(to, subject, message);
		if (email == null)
			return false;
		if (attachment != null)
			email.addAttachment(attachment);
		try
		{
			String msg = email.send();
			if (EMail.SENT_OK.equals (msg))
			{
				log.info("Sent EMail " + subject + " to " + to);
				return true;
			}
			else
			{
				log.warning("Could NOT Send Email: " + subject 
					+ " to " + to + ": " + msg
					+ " (" + getName() + ")");
				return false;
			}
		}
		catch (Exception ex)
		{
			log.severe(getName() + " - " + ex.getLocalizedMessage());
			return false;
		}
	}	//	sendEMail

	
	/**
	 * 	Send EMail from User
	 * 	@param from sender
	 *	@param to recipient
	 *	@param subject subject
	 *	@param message message
	 *	@param attachment optional attachment
	 *	@return true if sent
	 */
	public boolean sendEMail (MUser from, MUser to, 
		String subject, String message, File attachment)
	{
		EMail email = createEMail(from, to, subject, message);
		if (email == null)
			return false;
		if (attachment != null)
			email.addAttachment(attachment);
		InternetAddress emailFrom = email.getFrom();
		try
		{
			return sendEmailNow(from, to, email);
		}
		catch (Exception ex)
		{
			log.severe(getName() + " - from " + emailFrom
				+ " to " + to + ": " + ex.getLocalizedMessage());
			return false;
		}
	}	//	sendEMail

	/**
	 * 	Send Email Now
	 *	@param from optional from user
	 *	@param to to user
	 *	@param email email
	 *	@return true if sent
	 */
	private boolean sendEmailNow(MUser from, MUser to, EMail email)
	{
		String msg = email.send();
		//
		X_AD_UserMail um = new X_AD_UserMail(getCtx(), 0, null);
		um.setClientOrg(this);
		um.setAD_User_ID(to.getAD_User_ID());
		um.setSubject(email.getSubject());
		um.setMailText(email.getMessageCRLF());
		if (email.isSentOK())
			um.setMessageID(email.getMessageID());
		else
		{
			um.setMessageID(email.getSentMsg());
			um.setIsDelivered(X_AD_UserMail.ISDELIVERED_No);
		}
		um.save();

		//
		if (email.isSentOK())
		{
			if (from != null)
				log.info("Sent Email: " + email.getSubject() 
					+ " from " + from.getEMail()
					+ " to " + to.getEMail());
			else
				log.info("Sent Email: " + email.getSubject() 
					+ " to " + to.getEMail());
			return true;
		}
		else
		{
			if (from != null)
				log.warning("Could NOT Send Email: " + email.getSubject()
					+ " from " + from.getEMail()
					+ " to " + to.getEMail() + ": " + msg
					+ " (" + getName() + ")");
			else
				log.warning("Could NOT Send Email: " + email.getSubject()
					+ " to " + to.getEMail() + ": " + msg
					+ " (" + getName() + ")");
			return false;
		}
	}	//	sendEmailNow
	
	/************
	 * 	Create EMail from Request User
	 *	@param to recipient
	 *	@param subject sunject
	 *	@param message nessage
	 *	@return EMail
	 */
	public EMail createEMail (String to, 
		String subject, String message)
	{
		if (to == null || to.length() == 0)
		{
			log.warning("No To");
			return null;
		}
		//
		EMail email = null;
		if (isServerEMail() && Ini.isClient())
		{
			Server server = CConnection.get().getServer();
			try
			{
				if (server != null)
				{	//	See ServerBean
					email = server.createEMail(getCtx(), getAD_Client_ID(), 
						to, subject, message);
				}
				else
					log.log(Level.WARNING, "No AppsServer"); 
			}
			catch (RemoteException ex)
			{
				log.log(Level.SEVERE, getName() + " - AppsServer error", ex);
			}
		}
		if (email == null)
			email = new EMail (this,
				   getRequestEMail(), to,
				   subject, message);
		if (isSmtpAuthorization())
			email.createAuthenticator (getRequestUser(), getRequestUserPW());
		return email;
	}	//	createEMail

	/**
	 * 	Create EMail from User
	 * 	@param from optional sender
	 *	@param to recipient
	 *	@param subject sunject
	 *	@param message nessage
	 *	@return EMail
	 */
	public EMail createEMail (MUser from, MUser to, 
		String subject, String message)
	{
		if (to == null)
		{
			log.warning("No To user");
			return null;
		}
		if (to.getEMail() == null || to.getEMail().length() == 0)
		{
			log.warning("No To address: " + to);
			return null;
		}
		return createEMail (from, to.getEMail(), subject, message);
	}	//	createEMail
	
	/**
	 * 	Create EMail from User
	 * 	@param from optional sender
	 *	@param to recipient
	 *	@param subject sunject
	 *	@param message nessage
	 *	@return EMail
	 */
	public EMail createEMail (MUser from, String to, 
		String subject, String message)
	{
		if (to == null || to.length() == 0)
		{
			log.warning("No To address");
			return null;
		}
		//	No From - send from Request
		if (from == null)
			return createEMail (to, subject, message);
		//	No From details - Error
		if (from.getEMail() == null 
			|| from.getEMailUser() == null || from.getEMailUserPW() == null)
		{
			log.warning("From EMail incomplete: " + from + " (" + getName() + ")");
			return null;
		}
		//
		EMail email = null;
		if (isServerEMail() && Ini.isClient())
		{
			Server server = CConnection.get().getServer();
			try
			{
				if (server != null)
				{	//	See ServerBean
					email = server.createEMail(getCtx(), getAD_Client_ID(),
						from.getAD_User_ID(),
						to, subject, message);
				}
				else
					log.log(Level.WARNING, "No AppsServer"); 
			}
			catch (RemoteException ex)
			{
				log.log(Level.SEVERE, getName() + " - AppsServer error", ex);
			}
		}
		if (email == null)
			email = new EMail (this,
				   from.getEMail(), 
				   to,
				   subject, 
				   message);
		if (isSmtpAuthorization())
			email.createAuthenticator (from.getEMailUser(), from.getEMailUserPW());
		return email;
	}	//	createEMail
	
}	//	MClient
