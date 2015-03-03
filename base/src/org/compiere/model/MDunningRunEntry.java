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
import org.compiere.util.*;

/**
 *	Dunning Run Entry Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDunningRunEntry.java,v 1.4 2005/03/11 20:26:05 jjanke Exp $
 */
public class MDunningRunEntry extends X_C_DunningRunEntry
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_DunningRunEntry_ID id
	 */
	public MDunningRunEntry (Properties ctx, int C_DunningRunEntry_ID, String trxName)
	{
		super (ctx, C_DunningRunEntry_ID, trxName);
		if (C_DunningRunEntry_ID == 0)
		{
		//	setC_BPartner_ID (0);
		//	setC_BPartner_Location_ID (0);
		//	setAD_User_ID (0);
			
		//	setSalesRep_ID (0);
		//	setC_Currency_ID (0);
			setAmt (Env.ZERO);
			setQty (Env.ZERO);
			setProcessed (false);
		}
	}	//	MDunningRunEntry

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MDunningRunEntry (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MDunningRunEntry
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent 
	 */
	public MDunningRunEntry (MDunningRun parent)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setC_DunningRun_ID(parent.getC_DunningRun_ID());
		m_parent = parent;
	}	//	MDunningRunEntry

	private MDunningRun		m_parent = null;
	
	/**
	 * 	Set BPartner
	 *	@param bp partner
	 */
	public void setBPartner (MBPartner bp, boolean isSOTrx)
	{
		setC_BPartner_ID(bp.getC_BPartner_ID());
		MBPartnerLocation[] locations = bp.getLocations(false);
		//	Location
		if (locations.length == 1)
			setC_BPartner_Location_ID (locations[0].getC_BPartner_Location_ID());
		else
		{
			for (int i = 0; i < locations.length; i++)
			{
				MBPartnerLocation location = locations[i];
				if ((location.isPayFrom() && isSOTrx)
					|| (location.isRemitTo() && !isSOTrx))
				{
					setC_BPartner_Location_ID (location.getC_BPartner_Location_ID());
					break;
				}
			}
		}
		if (getC_BPartner_Location_ID() == 0)
		{
			String msg = "@C_BPartner_ID@ " + bp.getName();
			if (isSOTrx)
				msg += " @No@ @IsPayFrom@";
			else
				msg += " @No@ @IsRemitTo@";
			throw new IllegalArgumentException (msg);
		}
		//	User with location
		MUser[] users = MUser.getOfBPartner(getCtx(), bp.getC_BPartner_ID());
		if (users.length == 1)
			setAD_User_ID (users[0].getAD_User_ID());
		else
		{
			for (int i = 0; i < users.length; i++)
			{
				MUser user = users[i];
				if (user.getC_BPartner_Location_ID() == getC_BPartner_Location_ID())
				{
					setAD_User_ID (users[i].getAD_User_ID());
					break;
				}
			}
		}
		//
		setSalesRep_ID (bp.getSalesRep_ID());
	}	//	setBPartner
	
}	//	MDunningRunEntry
