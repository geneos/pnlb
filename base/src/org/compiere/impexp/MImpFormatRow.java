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
package org.compiere.impexp;

import java.sql.*;
import java.util.*;

import org.compiere.model.*;


/**
 *	Import Format Row Model 
 *	
 *  @author Jorg Janke
 *  @version $Id: MImpFormatRow.java,v 1.5 2005/05/17 05:29:53 jjanke Exp $
 */
public class MImpFormatRow extends X_AD_ImpFormat_Row
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_ImpFormat_Row_ID id
	 */
	public MImpFormatRow (Properties ctx, int AD_ImpFormat_Row_ID, String trxName)
	{
		super (ctx, AD_ImpFormat_Row_ID, trxName);
		if (AD_ImpFormat_Row_ID == 0)
		{
		//	setAD_ImpFormat_ID (0);		Parent
		//	setAD_Column_ID (0);
		//	setDataType (null);
		//	setName (null);
		//	setSeqNo (10);
			setDecimalPoint (".");
			setDivideBy100 (false);
		}
	}	//	MImpFormatRow

	/**
	 * 	Load Construcor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MImpFormatRow (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MImpFormatRow
	
	/**
	 * 	Parent Construcor
	 *	@param format format parent
	 */
	public MImpFormatRow (MImpFormat format)
	{
		this (format.getCtx(), 0, format.get_TrxName());
		setAD_ImpFormat_ID(format.getAD_ImpFormat_ID());
	}	//	MImpFormatRow
	
	/**
	 * 	Parent/Copy Construcor
	 *	@param parent format parent
	 *	@param original to copy
	 */
	public MImpFormatRow (MImpFormat parent, MImpFormatRow original)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		copyValues(original, this);
		setClientOrg(parent);
		setAD_ImpFormat_ID(parent.getAD_ImpFormat_ID());
	}	//	MImpFormatRow
	
}	//	MImpFormatRow
