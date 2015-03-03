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
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import org.compiere.util.*;

/**
 *  Image Model
 *  (DisplayType = 32)
 *
 *  @author Jorg Janke
 *  @version $Id: MImage.java,v 1.16 2005/10/14 00:44:31 jjanke Exp $
 */
public class MImage extends X_AD_Image
{
	/**
	 * 	Get MImage from Cache
	 *	@param ctx context
	 *	@param AD_Image_ID id
	 *	@return MImage
	 */
	public static MImage get (Properties ctx, int AD_Image_ID)
	{
		Integer key = new Integer (AD_Image_ID);
		MImage retValue = (MImage) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MImage (ctx, AD_Image_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MImage> s_cache = new CCache<Integer,MImage>("AD_Image", 20);
	
	/**
	 *  Constructor
	 *  @param ctx context
	 *  @param AD_Image_ID image
	 */
	public MImage(Properties ctx, int AD_Image_ID, String trxName)
	{
		super (ctx, AD_Image_ID, trxName);
		if (AD_Image_ID < 1)
			setName("-/-");
	}   //  MImage

	/**
	 * 	Load Constructor
	 *	@param ctx
	 *	@param rs
	 */
	public MImage (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MImage

	
	/** The Image                   */
	private ImageIcon       m_image = null;
	/** The Image File              */
	private File            m_file = null;

	/**
	 * 	Get Image
	 *	@return image or null
	 */
	public Image getImage ()
	{
		URL url = getURL();
		if (url == null)
			return null;
		Toolkit tk = Toolkit.getDefaultToolkit();
		return tk.getImage(url);
	}   //  getImage

	/**
	 * 	Get Icon
	 *	@return icon or null
	 */
	public Icon getIcon ()
	{
		URL url = getURL();
		if (url == null)
			return null;
		return new ImageIcon(url);
	}   //  getIcon

	/**
	 * 	Get URL
	 *	@return url or null
	 */
	private URL getURL()
	{
		String str = getImageURL();
		if (str == null || str.length() == 0)
			return null;
		
		URL url = null;
		try
		{
			//	Try URL directly
			if (str.indexOf("://") != -1)
				url = new URL(str);
			else	//	Try Resource
				url = getClass().getResource(str);
			//
			if (url == null)
				log.warning("Not found: " + str);
		}
		catch (Exception e)
		{
			log.warning("Not found: " + str + " - " + e.getMessage());
		}
		return url;
	}	//	getURL
	
	
	/**
	 *  String Representation
	 *  @return String
	 */
	public String toString()
	{
		return "MImage[ID=" + get_ID() + ",Name=" + getName() + "]";
	}   //  toString

}   //  MImage
