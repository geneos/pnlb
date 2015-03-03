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
 *	Package Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPackage.java,v 1.6 2005/05/17 05:29:53 jjanke Exp $
 */
public class MPackage extends X_M_Package
{
	/**
	 * 	Create one Package for Shipment 
	 *	@param shipment shipment
	 *	@param shipper shipper
	 *	@param shipDate null for today
	 *	@return package
	 */
	public static MPackage create (MInOut shipment, MShipper shipper, Timestamp shipDate)
	{
		MPackage retValue = new MPackage (shipment, shipper);
		if (shipDate != null)
			retValue.setShipDate(shipDate);
		retValue.save();
		//	Lines
		MInOutLine[] lines = shipment.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInOutLine sLine = lines[i];
			MPackageLine pLine = new MPackageLine (retValue);
			pLine.setInOutLine(sLine);
			pLine.save();
		}	//	lines
		return retValue;
	}	//	create

	
	
	/**************************************************************************
	 * 	MPackage
	 *	@param ctx context
	 *	@param M_Package_ID od
	 */
	public MPackage (Properties ctx, int M_Package_ID, String trxName)
	{
		super (ctx, M_Package_ID, trxName);
		if (M_Package_ID == 0)
		{
		//	setM_Shipper_ID (0);
		//	setDocumentNo (null);
		//	setM_InOut_ID (0);
			setShipDate (new Timestamp(System.currentTimeMillis()));
		}
	}	//	MPackage

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPackage (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPackage
	
	/**
	 * 	Shipment Constructor
	 *	@param shipment shipment
	 *	@param shipper shipper
	 */
	public MPackage (MInOut shipment, MShipper shipper)
	{
		this (shipment.getCtx(), 0, shipment.get_TrxName());
		setClientOrg(shipment);
		setM_InOut_ID(shipment.getM_InOut_ID());
		setM_Shipper_ID(shipper.getM_Shipper_ID());
	}	//	MPackage
	
}	//	MPackage
