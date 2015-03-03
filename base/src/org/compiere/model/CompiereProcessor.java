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

import java.sql.*;
import java.util.*;

/**
 *	Processor Interface
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereProcessor.java,v 1.4 2005/03/11 20:26:03 jjanke Exp $
 */
public interface CompiereProcessor
{
	/**
	 * 	Get Client
	 *	@return AD_Client_ID
	 */
	public int getAD_Client_ID();	
	
	/**
	 * 	Get Name
	 *	@return Name
	 */
	public String getName();

	/**
	 * 	Get Description
	 *	@return Description
	 */
	public String getDescription();

	/**
	 * 	Get Context
	 *	@return context
	 */
	public Properties getCtx();
	
	/**
	 * 	Get the frequency type
	 * 	@return frequency type
	 */
	public String getFrequencyType();

	/**
	 * 	Get the frequency
	 * 	@return frequency
	 */
	public int getFrequency();

	
	/**
	 * 	Get Unique ID
	 *	@return Unique ID
	 */
	public String getServerID();

	/**
	 * 	Get the date Next run
	 * 	@param requery requery database
	 * 	@return date next run
	 */
	public Timestamp getDateNextRun (boolean requery);

	/**
	 * 	Set Date Next Run
	 *	@param dateNextWork next work
	 */
	public void setDateNextRun(Timestamp dateNextWork);

	/**
	 * 	Get the date Last run
	 * 	@return date lext run
	 */
	public Timestamp getDateLastRun ();

	/**
	 * 	Set Date Last Run
	 *	@param dateLastRun last run
	 */
	public void setDateLastRun(Timestamp dateLastRun);

	/**
	 * 	Save
	 *	@return true if saved
	 */
	public boolean save();

	
	/**
	 * 	Get Processor Logs
	 *	@return logs
	 */
	public CompiereProcessorLog[] getLogs();
	
}	//	CompiereProcessor
