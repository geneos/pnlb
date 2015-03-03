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

import java.util.*;
import java.sql.*;

/**
 *	Replication Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReplication.java,v 1.4 2005/03/11 20:26:04 jjanke Exp $
 */
public class MReplication extends X_AD_Replication
{
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param AD_Replication_ID id
	 */
	public MReplication (Properties ctx, int AD_Replication_ID, String trxName)
	{
		super (ctx, AD_Replication_ID, trxName);
	}	//	MReplication

	/**
	 *	Public Access
	 * 	@param DateLastRun date last run
	 */
	public void setDateLastRun (Timestamp DateLastRun)
	{
		super.setDateLastRun (DateLastRun);
	}	//	setDateLastRun

}	//	MReplication
