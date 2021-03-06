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
 *	AD_PrintFont Print Font Model
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MPrintFont.java,v 1.18 2005/11/13 23:40:21 jjanke Exp $
 */
public class MPrintFont extends X_AD_PrintFont
{
	/**
	 *	Constructor
	 *  @param ctx context
	 *  @param AD_PrintFont_ID ID
	 */
	private MPrintFont(Properties ctx, int AD_PrintFont_ID, String trxName)
	{
		super (ctx, AD_PrintFont_ID, trxName);
		if (AD_PrintFont_ID == 0)
			setIsDefault(false);
	}	//	MPrintFont

	/** Font cached					*/
	private Font 	m_cacheFont = null;

	/*************************************************************************/

	/**
	 * 	Get Font
	 * 	@return Font
	 */
	public Font getFont()
	{
		if (m_cacheFont != null)
			return m_cacheFont;
		String code = (String)get_Value("Code");
		if (code == null || code.equals("."))
			m_cacheFont = new Font (null);
		try
		{
			if (code != null && !code.equals("."))
			//	fontfamilyname-style-pointsize
				m_cacheFont = Font.decode(code);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "MPrintFont.getFont", e);
		}
		if (code == null)
			m_cacheFont = new Font (null);		//	family=dialog,name=Dialog,style=plain,size=12
	//	log.fine( "MPrintFont.getFont " + code, m_cacheFont);
		return m_cacheFont;
	}	//	getFont

	/**
	 * 	Set Font
	 * 	@param font Font
	 */
	public void setFont (Font font)
	{
		//	fontfamilyname-style-pointsize
		StringBuffer sb = new StringBuffer();
		sb.append(font.getFamily()).append("-");
		int style = font.getStyle();
		if (style == Font.PLAIN)
			sb.append("PLAIN");
		else if (style == Font.BOLD)
			sb.append("BOLD");
		else if (style == Font.ITALIC)
			sb.append("ITALIC");
		else if (style == (Font.BOLD + Font.ITALIC))
			sb.append("BOLDITALIC");
		sb.append("-").append(font.getSize());
		setCode(sb.toString());
	}	//	setFont

	/*************************************************************************/

	/**
	 * 	Create Font in Database and save
	 * 	@param font font
	 * 	@return PrintFont
	 */
	static MPrintFont create (Font font)
	{
		MPrintFont pf = new MPrintFont(Env.getCtx(), 0, null);
		StringBuffer name = new StringBuffer (font.getName());
		if (font.isBold())
			name.append(" bold");
		if (font.isItalic())
			name.append(" italic");
		name.append(" ").append(font.getSize());
		pf.setName(name.toString());
		pf.setFont(font);
		pf.save();
		return pf;
	}	//	create

	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MPrintFont[");
		sb.append("ID=").append(get_ID())
			.append(",Name=").append(getName())
			.append("PSName=").append(getFont().getPSName())
			.append(getFont())
			.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Get PostScript Level 2 definition.
	 *  e.g. /dialog 12 selectfont
	 * 	@return PostScript command
	 */
	public String toPS()
	{
		StringBuffer sb = new StringBuffer("/");
		sb.append(getFont().getPSName());
		if (getFont().isBold())
			sb.append(" Bold");
		if (getFont().isItalic())
			sb.append(" Italic");
		sb.append(" ").append(getFont().getSize())
			.append(" selectfont");
		return sb.toString();
	}	//	toPS

	/**
	 * 	Dump Font
	 * 	@param font font
	 */
	static void dump (Font font)
	{
		System.out.println("Family=" + font.getFamily());
		System.out.println("FontName=" + font.getFontName());
		System.out.println("Name=" + font.getName());
		System.out.println("PSName=" + font.getPSName());
		System.out.println("Style=" + font.getStyle());
		System.out.println("Size=" + font.getSize());
		System.out.println("Attributes:");
		Map map = font.getAttributes();
		Iterator keys = map.keySet().iterator();
		while (keys.hasNext())
		{
			Object key = keys.next();
			Object value = map.get(key);
			System.out.println(" - " + key + "=" + value);
		}
		System.out.println(font);
	}	//	dump

	/*************************************************************************/

	/** Cached Fonts						*/
	static private CCache<Integer,MPrintFont> s_fonts = new CCache<Integer,MPrintFont>("AD_PrintFont", 20);

	/**
	 * 	Get Font
	 * 	@param AD_PrintFont_ID id
	 * 	@return Font
	 */
	static public MPrintFont get (int AD_PrintFont_ID)
	{
		Integer key = new Integer(AD_PrintFont_ID);
		MPrintFont pf = (MPrintFont)s_fonts.get(key);
		if (pf == null)
		{
			pf = new MPrintFont (Env.getCtx(), AD_PrintFont_ID, null);
			s_fonts.put(key, pf);
		}
		return pf;
	}	//	get

	/*************************************************************************/

	/**
	 * 	Seed Fonts
	 * 	@param args args
	 */
	public static void main(String[] args)
	{
		System.out.println("Available Fonts:");
		String[] family = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (int i = 0; i < family.length; i++)
			System.out.println(" - " + family[i]);

		org.compiere.Compiere.startup(true);
		MPrintFont pf = new MPrintFont(Env.getCtx(), 100, null);
		dump( pf.getFont() );

		String[] systemLocical = new String[] {"Dialog", "DialogInput", "Monospaced", "Serif", "SansSerif"};
		for (int i = 0; i < systemLocical.length; i++)
		{
		//	create(new Font(systemLocical[i], Font.BOLD, 13));
		//	create(new Font(systemLocical[i], Font.PLAIN, 11));
		//	create(new Font(systemLocical[i], Font.BOLD, 11));
		//	create(new Font(systemLocical[i], Font.ITALIC, 11));
		//	create(new Font(systemLocical[i], Font.PLAIN, 10));
		//	create(new Font(systemLocical[i], Font.BOLD, 10));
		//	create(new Font(systemLocical[i], Font.ITALIC, 10));
		//	create(new Font(systemLocical[i], Font.PLAIN, 9));
		//	create(new Font(systemLocical[i], Font.BOLD, 9));
		//	create(new Font(systemLocical[i], Font.ITALIC, 9));
		//	create(new Font(systemLocical[i], Font.PLAIN, 8));
		//	create(new Font(systemLocical[i], Font.BOLD, 8));
		//	create(new Font(systemLocical[i], Font.ITALIC, 8));
		}

		//	Read All Fonts
		int[] IDs = PO.getAllIDs ("AD_PrintFont", null, null);
		for (int i = 0; i < IDs.length; i++)
		{
			pf = new MPrintFont(Env.getCtx(), IDs[i], null);
			System.out.println(IDs[i] + " = " + pf.getFont());
		}

	}	//	main
}	//	MPrintFont
