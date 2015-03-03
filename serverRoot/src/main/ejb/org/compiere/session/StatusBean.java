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
package org.compiere.session;

import java.util.logging.*;
import javax.ejb.*;

import org.compiere.*;
import org.compiere.db.*;
import org.compiere.util.*;


/**
 * 	Compiere Status Bean
 *
 *  @ejb.bean name="compiere/Status"
 *		display-name="Compiere Status Session Bean"
 *		type="Stateless"
 *		view-type="both"
 *		transaction-type="Bean"
 *		jndi-name="compiere/Status"
 *      local-jndi-name="compiere/StatusLocal"
 *
 *  @ejb.ejb-ref ejb-name="compiere/Status"
 *  	view-type="both"
 *		ref-name="compiere/Status"
 *  @ejb.ejb-ref ejb-name="compiere/Status"
 *  	view-type="local"
 *		ref-name="compiere/StatusLocal"
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: StatusBean.java,v 1.16 2005/09/12 02:24:43 jjanke Exp $
 */
public class StatusBean implements SessionBean
{
	/**	Context				*/
	private SessionContext 	m_Context;
	/**	Logging				*/
	private static CLogger	log = CLogger.getCLogger(StatusBean.class);

	private static int		s_no = 0;
	private int				m_no = 0;
	//
	private int				m_versionCount = 0;
	private int				m_databaseCount = 0;


	/**
	 * 	Get Version (Date)
	 *  @ejb.interface-method view-type="both"
	 *  @return version e.g. 2002-09-02
	 */
	public String getDateVersion()
	{
		m_versionCount++;
		log.info ("getDateVersion " + m_versionCount);
		return Compiere.DATE_VERSION;
	}	//	getDateVersion

	/**
	 * 	Get Main Version
	 *  @ejb.interface-method view-type="both"
	 *  @return main version - e.g. Version 2.4.3b
	 */
	public String getMainVersion()
	{
		return Compiere.MAIN_VERSION;
	}	//	getMainVersion

	/**
	 *  Get Database Type
	 *  @ejb.interface-method view-type="both"
	 *  @return Database Type
	 */
	public String getDbType()
	{
		return CConnection.get().getType();
	}   //  getDbType

	/**
	 *  Get Database Host
	 *  @ejb.interface-method view-type="both"
	 *  @return Database Host Name
	 */
	public String getDbHost()
	{
		m_databaseCount++;
		log.info ("getDbHost " + m_databaseCount);
		return CConnection.get().getDbHost();
	}   //  getDbHost

	/**
	 *  Get Database Port
	 *  @ejb.interface-method view-type="both"
	 *  @return Database Port
	 */
	public int getDbPort()
	{
		return CConnection.get().getDbPort();
	}   //  getDbPort

	/**
	 *  Get Database SID
	 *  @ejb.interface-method view-type="both"
	 *  @return Database SID
	 */
	public String getDbName()
	{
		return CConnection.get().getDbName();
	}   //  getDbSID

	/**
	 *  Get Database URL
	 *  @ejb.interface-method view-type="both"
	 *  @return Database URL
	 */
	public String getConnectionURL()
	{
		return CConnection.get().getConnectionURL();
	}   //  getConnectionURL
	
	/**
	 *  Get Database UID
	 *  @ejb.interface-method view-type="both"
	 *  @return Database User Name
	 */
	public String getDbUid()
	{
		return CConnection.get().getDbUid();
	}   //  getDbUID

	/**
	 *  Get Database PWD
	 *  @ejb.interface-method view-type="both"
	 *  @return Database User Password
	 */
	public String getDbPwd()
	{
		return CConnection.get().getDbPwd();
	}   //  getDbPWD

	/**
	 *  Get Connection Manager Host
	 *  @ejb.interface-method view-type="both"
	 *  @return Connection Manager Host
	 */
	public String getFwHost()
	{
		return CConnection.get().getFwHost();
	}   //  getCMHost

	/**
	 *  Get Connection Manager Port
	 *  @ejb.interface-method view-type="both"
	 *  @return Connection Manager Port
	 */
	public int getFwPort()
	{
		return CConnection.get().getFwPort();
	}   //  getCMPort

	
	/**************************************************************************
	 * 	Get Version Count
	 *  @ejb.interface-method view-type="both"
	 * 	@return number of version inquiries
	 */
	public int getVersionCount()
	{
		return m_versionCount;
	}	//	getVersionCount

	/**
	 * 	Get Database Count
	 *  @ejb.interface-method view-type="both"
	 * 	@return number of database inquiries
	 */
	public int getDatabaseCount()
	{
		return m_databaseCount;
	}	//	getVersionCount

	/**
	 * 	Describes the instance and its content for debugging purpose
	 *  @ejb.interface-method view-type="both"
	 * 	@return Debugging information about the instance and its content
	 */
	public String getStatus()
	{
		StringBuffer sb = new StringBuffer("StatusBean[No=");
		sb.append(m_no)
			.append(",VersionCount=").append(m_versionCount)
			.append(",DatabaseCount=").append(m_versionCount)
			.append("]");
		return sb.toString();
	}	//	getStatus


	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		return getStatus();
	}	//	toString

	
	/**************************************************************************
	 * 	Create the Session Bean
	 * 	@throws EJBException, CreateException
	 *  @ejb.create-method view-type="both"
	 */
	public void ejbCreate() throws EJBException, CreateException
	{
		m_no = ++s_no;
		try
		{
			org.compiere.Compiere.startup(false);
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "", ex);
		//	throw new CreateException ();
		}
		log.info("#" + m_no + " - " + getStatus());
	}	//	ejbCreate


	// -------------------------------------------------------------------------
	// Framework Callbacks
	// -------------------------------------------------------------------------

	public void setSessionContext (SessionContext aContext) throws EJBException
	{
		m_Context = aContext;
	}

	public void ejbActivate() throws EJBException
	{
		if (log == null)
			log = CLogger.getCLogger(getClass());
		log.fine("ejbActivate");
	}

	public void ejbPassivate() throws EJBException
	{
		log.fine("ejbPassivate");
	}

	public void ejbRemove() throws EJBException
	{
		log.fine("ejbRemove");
	}

}	//	StatusBean
