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
package org.compiere.wf;

import java.sql.*;
import java.util.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Work Flow Commitment Block
 *	
 *  @author Jorg Janke
 *  @version $Id: MWFBlock.java,v 1.7 2005/11/06 01:17:27 jjanke Exp $
 */
public class MWFBlock extends X_AD_WF_Block
{
	/**
	 * 	Get MWFBlock from Cache
	 *	@param ctx context
	 *	@param AD_WF_Block_ID id
	 *	@return MWFBlock
	 */
	public static MWFBlock get (Properties ctx, int AD_WF_Block_ID)
	{
		Integer key = new Integer (AD_WF_Block_ID);
		MWFBlock retValue = (MWFBlock) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MWFBlock (ctx, AD_WF_Block_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MWFBlock>	s_cache	= new CCache<Integer,MWFBlock>("AD_WF_Block", 20);
	
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_WF_Block_ID id
	 */
	public MWFBlock (Properties ctx, int AD_WF_Block_ID, String trxName)
	{
		super (ctx, AD_WF_Block_ID, trxName);
	}	//	MWFBlock

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MWFBlock (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MWFBlock
	
}	//	MWFBlock
