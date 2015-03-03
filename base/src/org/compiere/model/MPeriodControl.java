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
 *	Period Control Model	
 *	
 *  @author Jorg Janke
 *  @version $Id: MPeriodControl.java,v 1.12 2005/09/19 04:49:45 jjanke Exp $
 */
public class MPeriodControl extends X_C_PeriodControl
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_PeriodControl_ID 0
	 */
	public MPeriodControl (Properties ctx, int C_PeriodControl_ID, String trxName)
	{
		super(ctx, C_PeriodControl_ID, trxName);
		if (C_PeriodControl_ID == 0)
		{
		//	setC_Period_ID (0);
		//	setDocBaseType (null);
			setPeriodAction (PERIODACTION_NoAction);
			setPeriodStatus (PERIODSTATUS_NeverOpened);
		}
	}	//	MPeriodControl

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPeriodControl (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPeriodControl

	/**
	 * 	Parent Constructor
	 *	@param period parent
	 *	@param DocBaseType doc base type
	 */
	public MPeriodControl (MPeriod period, String DocBaseType)
	{
		this (period.getCtx(), period.getAD_Client_ID(), period.getC_Period_ID(),  
			DocBaseType, period.get_TrxName());
	}	//	MPeriodControl

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param C_Period_ID period
	 *	@param DocBaseType doc base type
	 */
	public MPeriodControl (Properties ctx, int AD_Client_ID, int C_Period_ID, 
		String DocBaseType, String trxName)
	{
		this (ctx, 0, trxName);
		setClientOrg(AD_Client_ID, 0);
		setC_Period_ID (C_Period_ID);
		setDocBaseType (DocBaseType);
	}	//	MPeriodControl

	/**
	 * 	Is Period Open
	 *	@return true if open
	 */
	public boolean isOpen()
	{
		return PERIODSTATUS_Open.equals(getPeriodStatus());
	}	//	isOpen

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPeriodControl[");
		sb.append(get_ID()).append(",").append(getDocBaseType())
			.append(",Status=").append(getPeriodStatus())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MPeriodControl

