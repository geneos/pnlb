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

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.math.*;

import org.compiere.util.*;
import org.compiere.print.*;

/**
 * 	Performance Color Schema
 *	
 *  @author Jorg Janke
 *  @version $Id: MColorSchema.java,v 1.1 2005/12/27 06:17:56 jjanke Exp $
 */
public class MColorSchema extends X_PA_ColorSchema
{
	/**
	 * 	Get Color
	 *	@param ctx context
	 *	@param PA_ColorSchema_ID id
	 *	@param target target value
	 *	@param actual actual value
	 *	@return color
	 */
	public static Color getColor (Properties ctx, int PA_ColorSchema_ID, 
		BigDecimal target, BigDecimal actual)
	{
		int percent = 0;
		if (actual != null && actual.signum() != 0 
			&& target != null && target.signum() != 0)
		{
			BigDecimal pp = actual.multiply(Env.ONEHUNDRED)
				.divide(target, 0, BigDecimal.ROUND_HALF_UP);
			percent = pp.intValue();
		}
		return getColor(ctx, PA_ColorSchema_ID, percent);
	}	//	getColor

	/**
	 * 	Get Color
	 *	@param ctx context
	 *	@param PA_ColorSchema_ID id
	 *	@param percent percent
	 *	@return color
	 */
	public static Color getColor (Properties ctx, int PA_ColorSchema_ID, int percent)
	{
		MColorSchema cs = get(ctx, PA_ColorSchema_ID);
		return cs.getColor(percent);
	}	//	getColor

	
	/**
	 * 	Get MColorSchema from Cache
	 *	@param ctx context
	 *	@param PA_ColorSchema_ID id
	 *	@return MColorSchema
	 */
	public static MColorSchema get (Properties ctx, int PA_ColorSchema_ID)
	{
		if (PA_ColorSchema_ID == 0)
		{
			MColorSchema retValue = new MColorSchema(ctx, 0, null);
			retValue.setDefault();
			return retValue;
		}
		Integer key = new Integer (PA_ColorSchema_ID);
		MColorSchema retValue = (MColorSchema)s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MColorSchema (ctx, PA_ColorSchema_ID, null);
		if (retValue.get_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer, MColorSchema> s_cache 
		= new CCache<Integer, MColorSchema> ("PA_ColorSchema", 20);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_ColorSchema_ID id
	 *	@param trxName trx
	 */
	public MColorSchema (Properties ctx, int PA_ColorSchema_ID, String trxName)
	{
		super (ctx, PA_ColorSchema_ID, trxName);
		if (PA_ColorSchema_ID == 0)
		{
		//	setName (null);
		//	setMark1Percent (50);
		//	setAD_PrintColor1_ID (102);		//	red
		//	setMark2Percent (100);
		//	setAD_PrintColor2_ID (113);		//	yellow
		}
	}	//	MColorSchema

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MColorSchema (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MColorSchema

	/**
	 * 	Set Default.
	 * 	Red (50) - Yellow (100) - Green
	 */
	public void setDefault()
	{
		setName("Default");
		setMark1Percent (50);
		setAD_PrintColor1_ID (102);		//	red
		setMark2Percent (100);
		setAD_PrintColor2_ID (113);		//	yellow
		setMark3Percent (9999);
		setAD_PrintColor3_ID (103);		//	green
	}	//	setDefault
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getMark1Percent() > getMark2Percent())
			setMark1Percent(getMark2Percent());
		if (getMark2Percent() > getMark3Percent() && getMark3Percent() != 0)
			setMark2Percent(getMark3Percent());
		if (getMark3Percent() > getMark4Percent() && getMark4Percent() != 0)
			setMark4Percent(getMark4Percent());
		//
		return true;
	}	//	beforeSave
	
	/**
	 * 	Get Color
	 *	@param percent percent
	 *	@return color
	 */
	public Color getColor (int percent)
	{
		int AD_PrintColor_ID = 0;
		if (percent <= getMark1Percent() || getMark2Percent() == 0)
			AD_PrintColor_ID = getAD_PrintColor1_ID();
		else if (percent <= getMark2Percent() || getMark3Percent() == 0)
			AD_PrintColor_ID = getAD_PrintColor2_ID();
		else if (percent <= getMark3Percent() || getMark4Percent() == 0)
			AD_PrintColor_ID = getAD_PrintColor3_ID();
		else
			AD_PrintColor_ID = getAD_PrintColor4_ID();
		if (AD_PrintColor_ID == 0)
		{
			if (getAD_PrintColor3_ID() != 0)
				AD_PrintColor_ID = getAD_PrintColor3_ID();
			else if (getAD_PrintColor2_ID() != 0)
				AD_PrintColor_ID = getAD_PrintColor2_ID();
			else if (getAD_PrintColor1_ID() != 0)
				AD_PrintColor_ID = getAD_PrintColor1_ID();
		}
		if (AD_PrintColor_ID == 0)
			return Color.black;
		//
		MPrintColor pc = MPrintColor.get(getCtx(), AD_PrintColor_ID);
		if (pc != null)
			return pc.getColor();
		return Color.black;
	}	//	getColor

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MColorSchema[");
		sb.append (get_ID()).append ("-").append (getName()).append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MColorSchema
