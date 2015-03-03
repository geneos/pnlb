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
 *	POS Terminal definition
 *	
 *  @author Jorg Janke
 *  @version $Id: MPOS.java,v 1.11 2005/10/17 23:41:55 jjanke Exp $
 */
public class MPOS extends X_C_POS
{
	/**
	 * 	Get POS from Cache
	 *	@param ctx context
	 *	@param C_POS_ID id
	 *	@return MPOS
	 */
	public static MPOS get (Properties ctx, int C_POS_ID)
	{
		Integer key = new Integer (C_POS_ID);
		MPOS retValue = (MPOS) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MPOS (ctx, C_POS_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MPOS> s_cache = new CCache<Integer,MPOS>("C_POS", 20);

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_POS_ID id
	 */
	public MPOS (Properties ctx, int C_POS_ID, String trxName)
	{
		super (ctx, C_POS_ID, trxName);
		if (C_POS_ID == 0)
		{
		//	setName (null);
		//	setSalesRep_ID (0);
		//	setC_CashBook_ID (0);
		//	setM_PriceList_ID (0);
			setIsModifyPrice (false);	// N
		//	setM_Warehouse_ID (0);
		}	
	}	//	MPOS

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPOS (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPOS
	
	/**	Cash Business Partner			*/
	private MBPartner	m_template = null;
	
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Org Consistency
		if (newRecord 
			|| is_ValueChanged("C_CashBook_ID") || is_ValueChanged("M_Warehouse_ID"))
		{
			MCashBook cb = MCashBook.get(getCtx(), getC_CashBook_ID());
			if (cb.getAD_Org_ID() != getAD_Org_ID())
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@AD_Org_ID@: @C_CashBook_ID@"));
				return false;
			}
			MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
			if (wh.getAD_Org_ID() != getAD_Org_ID())
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@AD_Org_ID@: @M_Warehouse_ID@"));
				return false;
			}
		}
		return true;
	}	//	beforeSave

	
	/**
	 * 	Get default Cash BPartner
	 *	@return BPartner
	 */
	public MBPartner getBPartner()
	{
		if (m_template == null)
		{
			if (getC_BPartnerCashTrx_ID() == 0)
				m_template = MBPartner.getBPartnerCashTrx (getCtx(), getAD_Client_ID());
			else
				m_template = new MBPartner(getCtx(), getC_BPartnerCashTrx_ID(), get_TrxName());
			log.fine("getBPartner - " + m_template);
		}
		return m_template;
	}	//	getBPartner
	
}	//	MPOS
