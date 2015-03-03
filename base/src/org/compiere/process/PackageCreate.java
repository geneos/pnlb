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

import java.util.logging.*;
import org.compiere.model.*;
 
/**
 *	Create Package from Shipment for Shipper
 *	
 *  @author Jorg Janke
 *  @version $Id: PackageCreate.java,v 1.7 2005/09/19 04:49:45 jjanke Exp $
 */
public class PackageCreate extends SvrProcess
{
	/**	Shipper				*/
	private int		p_M_Shipper_ID = 0;
	/** Parent				*/
	private int		p_M_InOut_ID = 0;
	

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
			else if (name.equals("M_Shipper_ID"))
				p_M_Shipper_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Invoice_ID"))
				p_M_InOut_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		if (p_M_InOut_ID == 0)
			p_M_InOut_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("doIt - M_InOut_ID=" + p_M_InOut_ID + ", M_Shipper_ID=" + p_M_Shipper_ID);
		if (p_M_InOut_ID == 0)
			throw new IllegalArgumentException("No Shipment");
		if (p_M_Shipper_ID == 0)
			throw new IllegalArgumentException("No Shipper");
		MInOut shipment = new MInOut (getCtx(), p_M_InOut_ID, null);
		if (shipment.get_ID() != p_M_InOut_ID)
			throw new IllegalArgumentException("Cannot find Shipment ID=" + p_M_InOut_ID);
		MShipper shipper = new MShipper (getCtx(), p_M_Shipper_ID, get_TrxName());
		if (shipper.get_ID() != p_M_Shipper_ID)
			throw new IllegalArgumentException("Cannot find Shipper ID=" + p_M_InOut_ID);
		//
		
		MPackage pack = MPackage.create (shipment, shipper, null);
		
		return pack.getDocumentNo();
	}	//	doIt
	
}	//	PackageCreate
