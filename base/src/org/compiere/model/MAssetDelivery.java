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
import javax.servlet.http.*;
import org.compiere.util.*;

/**
 *  Asset Delivery Model
 *
 *  @author Jorg Janke
 *  @version $Id: MAssetDelivery.java,v 1.3 2005/09/19 04:49:48 jjanke Exp $
 */
public class MAssetDelivery extends X_A_Asset_Delivery
{
	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param A_Asset_Delivery_ID id or 0
	 * 	@param trxName trx
	 */
	public MAssetDelivery (Properties ctx, int A_Asset_Delivery_ID, String trxName)
	{
		super (ctx, A_Asset_Delivery_ID, trxName);
		if (A_Asset_Delivery_ID == 0)
		{
			setMovementDate (new Timestamp (System.currentTimeMillis ()));
		}
	}	//	MAssetDelivery

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MAssetDelivery (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAssetDelivery

	/**
	 * 	Create Asset Delivery for HTTP Request
	 * 	@param asset asset
	 * 	@param request request
	 * 	@param AD_User_ID BP Contact
	 */
	public MAssetDelivery (MAsset asset, 
		HttpServletRequest request, int AD_User_ID)
	{
		super (asset.getCtx(), 0, asset.get_TrxName());
		setAD_Client_ID(asset.getAD_Client_ID());
		setAD_Org_ID(asset.getAD_Org_ID());
		//	Asset Info
		setA_Asset_ID (asset.getA_Asset_ID());
		setLot(asset.getLot());
		setSerNo(asset.getSerNo());
		setVersionNo(asset.getVersionNo());
		//
		setMovementDate (new Timestamp (System.currentTimeMillis ()));
		//	Request
		setURL(request.getRequestURL().toString());
		setReferrer(request.getHeader("Referer"));
		setRemote_Addr(request.getRemoteAddr());
		setRemote_Host(request.getRemoteHost());
		//	Who
		setAD_User_ID(AD_User_ID);
		//
		save();
	}	//	MAssetDelivery

	/**
	 * 	Create Asset Delivery for EMail
	 * 	@param asset asset
	 * 	@param email email
	 * 	@param AD_User_ID BP Contact
	 */
	public MAssetDelivery (MAsset asset, EMail email, int AD_User_ID)
	{
		super (asset.getCtx(), 0, asset.get_TrxName());
		//	Asset Info
		setA_Asset_ID (asset.getA_Asset_ID());
		setLot(asset.getLot());
		setSerNo(asset.getSerNo());
		setVersionNo(asset.getVersionNo());
		//
		setMovementDate (new Timestamp (System.currentTimeMillis ()));
		//	EMail
		setEMail(email.getTo().toString());
		setMessageID(email.getMessageID());
		//	Who
		setAD_User_ID(AD_User_ID);
		//
		save();
	}	//	MAssetDelivery

	/**
	 * 	String representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAssetDelivery[")
			.append (get_ID ())
			.append(",A_Asset_ID=").append(getA_Asset_ID())
			.append(",MovementDate=").append(getMovementDate())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MAssetDelivery

