/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.images;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import javax.swing.*;
import javax.swing.plaf.*;

import org.compiere.plaf.CompiereLookAndFeel;

import java.util.logging.*;
import org.compiere.util.*;


/**
 *  Icon Factory based on Metal Color Schema (Themes are automatically considered)
 *
 *  @author     Jorg Janke
 *  @version    $Id: ImageFactory.java,v 1.7 2005/01/05 04:24:31 jjanke Exp $
 */
public class ImageFactory
{
	/**
	 *  Get Image Icon or null if not exists
	 *  @param name     file name in org.compiere.images
	 *  @return image
	 */
	public static ImageIcon getImageIcon (String name)
	{
		URL url = org.compiere.Compiere.class.getResource("images/" + name);
		if (url == null)
		{
			log.log(Level.SEVERE, "ImageFactory.getImageIcon - not found: " + name);
			return null;
		}
		return new ImageIcon(url);
	}   //  getImageIcon

	/** Home Icon ************************************************************/
	private static Icon s_HomeIcon = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ImageFactory.class);

	/**
	 *  Get Home Icon (little house) 18*18
	 *  @return image
	 */
	public static Icon getHomeIcon()
	{
		if (s_HomeIcon == null)
			s_HomeIcon = new HomeIcon();
		return s_HomeIcon;
	}   //  getHomeIcon

	/**
	 *  18*18 Home Icon
	 */
	private static class HomeIcon implements Icon, UIResource
	{
		public void paintIcon(Component c, Graphics g, int x, int y)
		{
			g.translate(x, y);
			// Draw outside edge of house
			g.setColor(CompiereLookAndFeel.getControlInfo());           //  black
			g.drawLine(8,1, 1,8);           // left edge of roof
			g.drawLine(8,1, 15,8);          // right edge of roof
			g.drawLine(11,2, 11,3);         // left edge of chimney
			g.drawLine(12,2, 12,4);         // right edge of chimney
			g.drawLine(3,7, 3,15);          // left edge of house
			g.drawLine(13,7, 13,15);        // right edge of house
			g.drawLine(4,15, 12,15);        // bottom edge of house
			// Draw door frame
			//     same color as edge of house
			g.drawLine( 6,9,  6,14); // left
			g.drawLine(10,9, 10,14); // right
			g.drawLine( 7,9,  9, 9); // top
			// Draw roof body
			g.setColor(CompiereLookAndFeel.getControlDarkShadow());     //  secondary1
			g.fillRect(8,2, 1,1);           //top toward bottom
			g.fillRect(7,3, 3,1);
			g.fillRect(6,4, 5,1);
			g.fillRect(5,5, 7,1);
			g.fillRect(4,6, 9,2);
			// Draw doornob
			//     same color as roof body
			g.drawLine(9,12, 9,12);
			// Paint the house
			g.setColor(CompiereLookAndFeel.getPrimaryControl());        //  primary3
			g.drawLine(4,8, 12,8);          // above door
			g.fillRect(4,9, 2,6);           // left of door
			g.fillRect(11,9, 2,6);          // right of door
			g.translate(-x, -y);
		}
		public int getIconWidth()
		{
			return 18;
		}
		public int getIconHeight()
		{
			return 18;
		}
	}   //  HomeIcon

	/** Folder Icon **********************************************************/
	private static Icon s_FolderIcon = null;
	/** 16*16 dimension     */
	private static final Dimension s_icon16Size = new Dimension(16, 16);

	/**
	 *  Folder Icon
	 *  @return icon
	 */
	public static Icon getFolderIcon()
	{
		if (s_FolderIcon == null)
			s_FolderIcon = new FolderIcon();
		return s_FolderIcon;
	}   //  getFolderIcon

	/**
	 *  FolderIcon usable for Tree (18*16) spacing
	 */
	private static class FolderIcon extends FolderIcon16
	{
		public int getShift()
		{
			return -1;
		}
		public int getAdditionalHeight()
		{
			return 2;
		}
	}   //  FolderIcon

	/**
	 *  Scaleable 16*16 Folder Icon
	 */
	public static class FolderIcon16 implements Icon
	{
		transient Image image;

		public void paintIcon (Component c, Graphics g, int x, int y)
		{
			if (image == null)
			{
				image = new BufferedImage(getIconWidth(), getIconHeight(),
					BufferedImage.TYPE_INT_ARGB);
				Graphics imageG = image.getGraphics();
				paintMe (c,imageG);
				imageG.dispose();
			}
			g.drawImage(image, x, y+getShift(), null);
		}
		private void paintMe(Component c, Graphics g)
		{
			int right = s_icon16Size.width - 1;
			int bottom = s_icon16Size.height - 1;
			// Draw tab top
			g.setColor(CompiereLookAndFeel.getPrimaryControlDarkShadow());  //  primary1
			g.drawLine(right - 5, 3, right, 3);
			g.drawLine(right - 6, 4, right, 4);
			// Draw folder front
			g.setColor(CompiereLookAndFeel.getPrimaryControl());            //  primary3
			g.fillRect(2, 7, 13, 8);
			// Draw tab bottom
			g.setColor(CompiereLookAndFeel.getPrimaryControlShadow());      //  primary2
			g.drawLine(right - 6, 5, right - 1, 5);
			// Draw outline
			g.setColor(CompiereLookAndFeel.getPrimaryControlInfo());        //  black
			g.drawLine(0, 6, 0, bottom);            // left side
			g.drawLine(1, 5, right - 7, 5);         // first part of top
			g.drawLine(right - 6, 6, right - 1, 6); // second part of top
			g.drawLine(right, 5, right, bottom);    // right side
			g.drawLine(0, bottom, right, bottom);   // bottom
			// Draw highlight
			g.setColor(CompiereLookAndFeel.getPrimaryControlHighlight());   //  white
			g.drawLine(1, 6, 1, bottom - 1);
			g.drawLine(1, 6, right - 7, 6);
			g.drawLine(right - 6, 7, right - 1, 7);
		}
		public int getShift()
		{
			return 0;
		}
		public int getAdditionalHeight()
		{
			return 0;
		}
		public int getIconWidth()
		{
			return s_icon16Size.width;
		}
		public int getIconHeight()
		{
			return s_icon16Size.height + getAdditionalHeight();
		}
	}   //  FolderIcon16

}   //  ImageFactory
