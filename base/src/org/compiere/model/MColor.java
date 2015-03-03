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
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.plaf.CompiereColor;
import org.compiere.util.*;


/**
 *  Color Persistent Object Model
 *  (DisplayType=27)
 *
 *  @author Jorg Janke
 *  @version $Id: MColor.java,v 1.14 2005/11/06 01:17:27 jjanke Exp $
 */
public class MColor extends X_AD_Color
{
	/**
	 *  Color Model
	 *  @param ctx context
	 *  @param AD_Color_ID color
	 */
	public MColor(Properties ctx, int AD_Color_ID, String trxName)
	{
		super (ctx, AD_Color_ID, trxName);
		if (AD_Color_ID == 0)
			setName("-/-");
	}   //  MColor

	/**
	 *  String Representation
	 *  @return string
	 */
	public String toString()
	{
		return "MColor[ID=" + get_ID() + " - " + getName() + "]";
	}   //  toString

	/**
	 *  Load Special data (images, ..).
	 *  To be extended by sub-classes
	 *  @param rs result set
	 *  @param index zero based index
	 *  @return value
	 *  @throws SQLException
	 */
	protected Object loadSpecial (ResultSet rs, int index) throws SQLException
	{
		log.config(p_info.getColumnName(index));
		if (index == get_ColumnIndex("ColorType"))
			return rs.getString(index+1);
		return null;
	}   //  loadSpecial


	/**
	 *  Save Special Data.
	 *      AD_Image_ID (Background)
	 *  @param value value
	 *  @param index index
	 *  @return SQL code for INSERT VALUES clause
	 */
	protected String saveNewSpecial (Object value, int index)
	{
		String colName = p_info.getColumnName(index);
		String colValue = value == null ? "null" : value.getClass().toString();
		log.fine(colName + "=" + colValue);
		if (value == null)
			return "NULL";
		return value.toString();
	}   //  saveNewSpecial

	
	/**************************************************************************
	 *  Get CompiereColor.
	 *  see org.compiere.grid.ed.VColor#getCompiereColor
	 *  @return CompiereColor
	 */
	public CompiereColor getCompiereColor()
	{
		if (get_ID() == 0)
			return null;

		//  Color Type
		String ColorType = (String)getColorType();
		if (ColorType == null)
		{
			log.log(Level.SEVERE, "MColor.getCompiereColor - No ColorType");
			return null;
		}
		CompiereColor cc = null;
		//
		if (ColorType.equals(CompiereColor.TYPE_FLAT))
		{
			cc = new CompiereColor(getColor(true), true);
		}
		else if (ColorType.equals(CompiereColor.TYPE_GRADIENT))
		{
			int RepeatDistance = getRepeatDistance();
			String StartPoint = getStartPoint();
			int startPoint = StartPoint == null ? 0 : Integer.parseInt(StartPoint);
			cc = new CompiereColor(getColor(true), getColor(false), startPoint, RepeatDistance);
		}
		else if (ColorType.equals(CompiereColor.TYPE_LINES))
		{
			int LineWidth = getLineWidth();
			int LineDistance = getLineDistance();
			cc = new CompiereColor(getColor(false), getColor(true), LineWidth, LineDistance);
		}
		else if (ColorType.equals(CompiereColor.TYPE_TEXTURE))
		{
			int AD_Image_ID = getAD_Image_ID();
			String url = getURL(AD_Image_ID);
			if (url == null)
				return null;
			BigDecimal ImageAlpha = getImageAlpha();
			float compositeAlpha = ImageAlpha == null ? 0.7f : ImageAlpha.floatValue();
			cc = new CompiereColor(url, getColor(true), compositeAlpha);
		}
		return cc;
	}   //  getCompiereColor

	/**
	 *  Get Color
	 *  @param primary true if primary false if secondary
	 *  @return Color
	 */
	private Color getColor (boolean primary)
	{
		int red = primary ? getRed() : getRed_1();
		int green = primary ? getGreen() : getGreen_1();
		int blue = primary ? getBlue() : getBlue_1();
		//
		return new Color (red, green, blue);
	}   //  getColor

	/**
	 *  Get URL from Image
	 *  @param AD_Image_ID image
	 *  @return URL as String or null
	 */
	private String getURL (int AD_Image_ID)
	{
		if (AD_Image_ID == 0)
			return null;
		//
		String retValue = null;
		String sql = "SELECT ImageURL FROM AD_Image WHERE AD_Image_ID=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt (1, AD_Image_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = rs.getString(1);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		return retValue;
	}   //  getURL

}   //  MColor
