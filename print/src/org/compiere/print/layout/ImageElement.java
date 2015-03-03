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
package org.compiere.print.layout;

import java.awt.*;
import java.awt.geom.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.print.*;
import org.compiere.util.*;

/**
 *	Image Element
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImageElement.java,v 1.16 2005/11/13 23:40:21 jjanke Exp $
 */
public class ImageElement extends PrintElement
{
	/**
	 *	Create Image from URL
	 *	@param imageURLString image url
	 */
	public static ImageElement get (String imageURLString)
	{
		Object key = imageURLString;
		ImageElement image = (ImageElement)s_cache.get(key);
		if (image == null)
		{
			image = new ImageElement(imageURLString);
			s_cache.put(key, image);
		}
		return new ImageElement(image.getImage());
	}	//	get
	
	/**
	 *	Create Image from URL
	 *  @param imageURL image url
	 */
	public static ImageElement get (URL imageURL)
	{
		Object key = imageURL;
		ImageElement image = (ImageElement)s_cache.get(key);
		if (image == null)
		{
			image = new ImageElement(imageURL);
			s_cache.put(key, image);
		}
		return new ImageElement(image.getImage());
	}	//	get
	
	/**
	 *	Create Image from Attachment
	 * 	@param AD_PrintFormatItem_ID record id
	 */
	public static ImageElement get (int AD_PrintFormatItem_ID)
	{
		Object key = new Integer(AD_PrintFormatItem_ID);
		ImageElement image = (ImageElement)s_cache.get(key);
		if (image == null)
		{
			image = new ImageElement(AD_PrintFormatItem_ID);
			s_cache.put(key, image);
		}
		return new ImageElement(image.getImage());
	}	//	get

	/**	60 minute Cache						*/
	private static CCache<Object,ImageElement>	s_cache 
		= new CCache<Object,ImageElement>("ImageElement", 10, 60);
	
	/**************************************************************************
	 *	Create from existing Image
	 *  @param image image
	 */
	public ImageElement(Image image)
	{
		m_image = image;
		if (m_image != null)
			log.fine("Image=" + image);
		else
			log.log(Level.SEVERE, "Image is NULL");
	}	//	ImageElement

	/**
	 *	Create Image from URL
	 *	@param imageURLstring image url
	 */
	private ImageElement(String imageURLstring)
	{
		URL imageURL = getURL(imageURLstring);
		if (imageURL != null)
		{
			m_image = Toolkit.getDefaultToolkit().getImage(imageURL);
			if (m_image != null)
				log.fine("URL=" + imageURL);
			else
				log.log(Level.SEVERE, "Not loaded - URL=" + imageURL);
		}
		else
			log.log(Level.SEVERE, "Invalid URL=" + imageURLstring);
	}	//	ImageElement

	/**
	 *	Create Image from URL
	 *  @param imageURL image url
	 */
	private ImageElement(URL imageURL)
	{
		if (imageURL != null)
		{
			m_image = Toolkit.getDefaultToolkit().getImage(imageURL);
			if (m_image != null)
				log.fine("URL=" + imageURL);
			else
				log.log(Level.SEVERE, "Not loaded - URL=" + imageURL);
		}
		else
			log.severe ("ImageURL is NULL");
	}	//	ImageElement

	/**
	 *	Create Image from Attachment
	 * 	@param AD_PrintFormatItem_ID record id
	 */
	private ImageElement(int AD_PrintFormatItem_ID)
	{
		loadAttachment(AD_PrintFormatItem_ID);
	}	//	ImageElement

	/**	The Image			*/
	private Image	m_image = null;

	/**************************************************************************
	 * 	Get URL from String
	 *  @param urlString url or resource
	 *  @return URL or null
	 */
	private URL getURL (String urlString)
	{
		URL url = null;
		//	not a URL - may be a resource
		if (urlString.indexOf("://") == -1)
		{
			ClassLoader cl = getClass().getClassLoader();
			url = cl.getResource(urlString);
			if (url != null)
				return url;
			log.log(Level.SEVERE, "Not found - " + urlString);
			return null;
		}
		//	load URL
		try
		{
			url = new URL (urlString);
		}
		catch (MalformedURLException ex)
		{
			log.log(Level.SEVERE, "getURL", ex);
		}
		return url;
	}	//	getURL;

	/**
	 * 	Load Attachment
	 * 	@param AD_PrintFormatItem_ID record id
	 */
	private void loadAttachment(int AD_PrintFormatItem_ID)
	{
		MAttachment attachment = MAttachment.get(Env.getCtx(), 
			MPrintFormatItem.getTableId(MPrintFormatItem.Table_Name), AD_PrintFormatItem_ID);
		if (attachment == null)
		{
			log.log(Level.SEVERE, "No Attachment - AD_PrintFormatItem_ID=" + AD_PrintFormatItem_ID);
			return;
		}
		if (attachment.getEntryCount() != 1)
		{
			log.log(Level.SEVERE, "Need just 1 Attachment Entry = " + attachment.getEntryCount());
			return;
		}
		byte[] imageData = attachment.getEntryData(0);
		if (imageData != null)
			m_image = Toolkit.getDefaultToolkit().createImage(imageData);
		if (m_image != null)
			log.fine(attachment.getEntryName(0) 
				+ " - Size=" + imageData.length);
		else
			log.log(Level.SEVERE, attachment.getEntryName(0)
				+ " - not loaded (must be gif or jpg) - AD_PrintFormatItem_ID=" + AD_PrintFormatItem_ID);
	}	//	loadAttachment

	
	/**************************************************************************
	 * 	Calculate Image Size.
	 * 	Set p_width & p_height
	 * 	@return true if calculated
	 */
	protected boolean calculateSize()
	{
		p_width = 0;
		p_height = 0;
		if (m_image == null)
			return true;
		//	we have an image
		waitForLoad(m_image);
		if (m_image != null)
		{
			p_width = m_image.getWidth(this);
			p_height = m_image.getHeight(this);
		}
		return true;
	}	//	calculateSize

	/**
	 * 	Get the Image
	 *	@return image
	 */
	public Image getImage()
	{
		return m_image;
	}	//	getImage
	
	/**
	 * 	Paint Image
	 * 	@param g2D Graphics
	 *  @param pageStart top left Location of page
	 *  @param pageNo page number for multi page support (0 = header/footer) - ignored
	 *  @param ctx print context
	 *  @param isView true if online view (IDs are links)
	 */
	public void paint(Graphics2D g2D, int pageNo, Point2D pageStart, Properties ctx, boolean isView)
	{
		if (m_image == null)
			return;

		//	Position
		Point2D.Double location = getAbsoluteLocation(pageStart);
		int x = (int)location.x;
		if (MPrintFormatItem.FIELDALIGNMENTTYPE_TrailingRight.equals(p_FieldAlignmentType))
			x += p_maxWidth - p_width;
		else if (MPrintFormatItem.FIELDALIGNMENTTYPE_Center.equals(p_FieldAlignmentType))
			x += (p_maxWidth - p_width) / 2;
		int y = (int)location.y;
		//
		g2D.drawImage(m_image, x, y, this);
	}	//	paint
	
}	//	ImageElement
