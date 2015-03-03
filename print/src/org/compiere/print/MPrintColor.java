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
package org.compiere.print;

import java.awt.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	AD_PrintColor Print Color Model
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MPrintColor.java,v 1.21 2005/11/13 23:40:21 jjanke Exp $
 */
public class MPrintColor extends X_AD_PrintColor
{

	/**************************************************************************
	 * 	Create Color in Database and save
	 * 	@param color color
	 *  @param name name
	 * 	@return MPrintColor
	 */
	static MPrintColor create (Color color, String name)
	{
		MPrintColor pc = new MPrintColor (Env.getCtx(), 0, null);
		pc.setName(name);
		pc.setColor(color);
		pc.save();
		return pc;
	}	//	create

	/*************************************************************************/

	public static final Color	darkGreen = new Color (0, 128, 0);
	public static final Color	blackGreen = new Color (0, 64, 0);

	public static final Color	darkBlue = new Color (0, 0, 128);
	public static final Color	blackBlue = new Color (0, 0, 64);

	public static final Color	whiteGray = new Color (224, 224, 224);

	public static final Color	brown = new Color (153, 102, 51);
	public static final Color	darkBrown = new Color (102, 51, 0);

	/*************************************************************************/

	/** Cached Colors						*/
	static private CCache<Integer,MPrintColor> 	s_colors = new CCache<Integer,MPrintColor>("AD_PrintColor", 20);
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MPrintColor.class);

	/**
	 * 	Get Color.
	 * 	if id = 0, it returns a new color (black) - but do not modify/save as cached
	 * 	@param ctx context
	 * 	@param AD_PrintColor_ID id
	 * 	@return Color
	 */
	static public MPrintColor get (Properties ctx, int AD_PrintColor_ID)
	{
	//	if (AD_PrintColor_ID == 0)
	//		return new MPrintColor (ctx, 0);
		Integer key = new Integer(AD_PrintColor_ID);
		MPrintColor pc = (MPrintColor)s_colors.get(key);
		if (pc == null)
		{
			pc = new MPrintColor (ctx, AD_PrintColor_ID, null);
			s_colors.put(key, pc);
		}
		return pc;
	}	//	get

	/**
	 * 	Get Color
	 * 	@param ctx context
	 * 	@param AD_PrintColor_ID id
	 * 	@return Color or null
	 */
	static public MPrintColor get (Properties ctx, String AD_PrintColor_ID)
	{
		if (AD_PrintColor_ID == null || AD_PrintColor_ID.length() == 0)
			return null;
		try
		{
			int id = Integer.parseInt(AD_PrintColor_ID);
			return get(ctx, id);
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "AD_PrintColor_ID=" + AD_PrintColor_ID
				+ " - " + e.toString());
		}
		return null;
	}	//	get
	
	
	/**************************************************************************
	 *	Constructor
	 *  @param ctx context
	 *  @param AD_PrintColor_ID ID
	 */
	public MPrintColor(Properties ctx, int AD_PrintColor_ID, String trxName)
	{
		super (ctx, AD_PrintColor_ID, trxName);
		if (AD_PrintColor_ID == 0)
			setIsDefault(false);
	}	//	MPrintColor

	/**	Color cached				*/
	private Color	m_cacheColor = null;

	/**
	 * 	Get Color
	 * 	@return Color
	 */
	public Color getColor()
	{
		if (m_cacheColor != null)
			return m_cacheColor;
		String code = getCode();
		if (code == null || code.equals("."))
			m_cacheColor = Color.black;
		try
		{
			if (code != null && !code.equals("."))
			{
				int rgba = Integer.parseInt(code);
				m_cacheColor = new Color(rgba, false);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "MPrintColor.getColor", e);
		}
		if (code == null)
			m_cacheColor = Color.black;
	//	log.fine( "MPrintColor.getColor " + code, m_cacheColor);
		return m_cacheColor;
	}	//	getColor

	/**
	 * 	Set Color
	 * 	@param color Color
	 */
	public void setColor (Color color)
	{
		int rgba = color.getRGB();
		super.setCode(String.valueOf(rgba));
	}	//	setColor

	/**
	 * 	Get Color as RRGGBB hex string for HTML font tag
	 *	@return	rgb hex value
	 */
	public String getRRGGBB()
	{
		Color color = getColor();
		StringBuffer sb = new StringBuffer();
		sb.append(Util.toHex((byte)color.getRed()))
			.append(Util.toHex((byte)color.getGreen()))
			.append(Util.toHex((byte)color.getBlue()));
		return sb.toString();
	}	//	getRRGGBB
	
	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MPrintColor[");
		sb.append("ID=").append(get_ID())
			.append(",Name=").append(getName())
			.append(",RGB=").append(getCode())
			.append(",").append(getColor())
			.append("]");
		return sb.toString();
	}	//	toString
	
	
	/**************************************************************************
	 * 	Create Standard Colors
	 * 	@param args args
	 */
	public static void main(String[] args)
	{
		org.compiere.Compiere.startupEnvironment(true);
		Color[] colors = new Color[]
			{Color.black, Color.red, Color.green, Color.blue,
			Color.darkGray, Color.gray, Color.lightGray, Color.white,
			Color.cyan, Color.magenta, Color.orange, Color.pink, Color.yellow,
			SystemColor.textHighlight};
		String[] names = new String[]
			{"Black", "Red", "Green", "Blue",
			"Gray dark", "Gray", "Gray light", "White",
			"Cyan", "Magenta", "Orange", "Pink", "Yellow",
			"Blue dark"};
		for (int i = 0; i < colors.length; i++)
			System.out.println(names[i] + " = " + colors[i] + " RGB=" + colors[i].getRGB()
				+ " -> " + new Color(colors[i].getRGB(), false)
				+ " -> " + new Color(colors[i].getRGB(), true));
/**
		//	Create Colors
		for (int i = 0; i < colors.length; i++)
			create(colors[i], names[i]);
		create(whiteGray, "Gray white");
		create(darkGreen, "Green dark");
		create(blackGreen, "Green black");
		create(blackBlue, "Blue black");
		create(brown, "Brown");
		create(darkBrown, "Brown dark");
**/

		//	Read All Colors
		int[] IDs = PO.getAllIDs ("AD_PrintColor", null, null);
		for (int i = 0; i < IDs.length; i++)
		{
			MPrintColor pc = new MPrintColor(Env.getCtx(), IDs[i], null);
			System.out.println(IDs[i] + ": " + pc + " = " + pc.getColor() + ", RGB=" + pc.getColor().getRGB());
		}
	}	//	main
	
}	//	MPrintColor
