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
package org.compiere.apps.graph;

import java.awt.*;
import java.util.*;

/**
 * 	Graphic Utilities
 *	
 *  @author Jorg Janke
 *  @version $Id: GraphUtil.java,v 1.1 2005/12/27 06:18:37 jjanke Exp $
 */
public class GraphUtil
{
	/**
	 * 	Get Foreground for back
	 *	@param background back
	 *	@return while or black
	 */
	public static Color getForeground (Color background)
	{
		if (background != null && isDark(background))
			return Color.white;
		return Color.black;
	}	//	getForeground

	/**
	 * 	Get Column Background
	 *	@param index index
	 */
	public static Color getBackground (int index)
	{
		while (s_colors.size() <= index)
		{
			int rr = (index+1) * 47;
			int gg = 100;
			while (rr > 255)
			{
				rr -= 255;
				gg += 50;
			}
			while (gg > 255)
				gg -= 255;
			s_colors.add(new Color(255-rr, gg, rr));
		}
		return s_colors.get(index);
	}	//	getBackGround

	/** List of Colors		*/
	private static ArrayList<Color> s_colors = new ArrayList<Color>();
	
	
	/**
	 *	Is the Color dark?
	 *	@param color color
	 *	@return true if dark
	 */
	public static boolean isDark (Color color)
	{
		float r = color.getRed()   / 255.0f;
		float g = color.getGreen() / 255.0f;
		float b = color.getBlue()  / 255.0f;
		return isDark (r, g, b);
	}	//	isDark
	
	/**
	 * 	Is Color more white or black?
	 *	@param r red
	 *	@param g green
	 *	@param b blue
	 *	@return true if dark
	 */
	public static boolean isDark (double r, double g, double b)
	{
		double whiteDistance = colorDistance (r, g, b, 1.0, 1.0, 1.0);
		double blackDistance = colorDistance (r, g, b, 0.0, 0.0, 0.0);
		return blackDistance < whiteDistance;
	}	//	isDark

	/**
	 * 	Simple Color Distance.
	 * 	(3d point distance)
	 *	@param r1 first red
	 *	@param g1 first green
	 *	@param b1 first blue
	 *	@param r2 second red
	 *	@param g2 second green
	 *	@param b2 second blue
	 *	@return 3d distance for relative comparison
	 */
	public static double colorDistance (double r1, double g1, double b1,
          double r2, double g2, double b2)
	{
		double a = r2 - r1;
		double b = g2 - g1;
		double c = b2 - b1;
		return Math.sqrt (a*a + b*b + c*c);
	}	//	colorDistance
	

	/**
	 * 	Get darker color
	 *	@param color color
	 *	@param factor factor 0..1 (AWT 0.7) the smaller, the darker 
	 *	@return darker color
	 */
	public static Color darker(Color color, double factor) 
	{
    	return new Color(
    		Math.max((int)(color.getRed() * factor), 0),
    		Math.max((int)(color.getGreen() * factor), 0),
    		Math.max((int)(color.getBlue() * factor), 0));
	}	//	darker

	
}	//	GraphUtil
