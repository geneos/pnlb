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

/**
 *	2D Dimesnion Implementation
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: Dimension2DImpl.java,v 1.9 2005/03/11 20:34:41 jjanke Exp $
 */
public class Dimension2DImpl extends Dimension2D
{
	/**
	 *	Constructor 0/0
	 */
	public Dimension2DImpl()
	{
	}	//	Dimension2DImpl

	/**
	 *	Constructor 0/0
	 *  @param dim dimension
	 */
	public Dimension2DImpl(Dimension dim)
	{
		setSize (dim);
	}	//	Dimension2DImpl

	/**
	 *	Constructor 0/0
	 *  @param width width
	 *  @param height height
	 */
	public Dimension2DImpl(double width, double height)
	{
		setSize (width, height);
	}	//	Dimension2DImpl

	/**	Width			*/
	public double	width = 0;
	/**	Height			*/
	public double	height = 0;

	/**
	 * 	Set Size
	 * 	@param width width
	 * 	@param height height
	 */
	public void setSize (double width, double height)
	{
		this.width = width;
		this.height = height;
	}	//	setSize

	/**
	 * 	Set Size
	 * 	@param dim dimension
	 */
	public void setSize (Dimension dim)
	{
		this.width = dim.getWidth();
		this.height = dim.getHeight();
	}	//	setSize

	/**
	 * 	Add Size below existing
	 * 	@param dWidth width to increase if below
	 * 	@param dHeight height to add
	 */
	public void addBelow (double dWidth, double dHeight)
	{
		if (this.width < dWidth)
			this.width = dWidth;
		this.height += dHeight;
	}	//	addBelow

	/**
	 * 	Add Size below existing
	 * 	@param dim add dimension
	 */
	public void addBelow (Dimension dim)
	{
		addBelow (dim.width, dim.height);
	}	//	addBelow

	/**
	 * 	Round to next Int value
	 */
	public void roundUp()
	{
		width = Math.ceil(width);
		height = Math.ceil(height);
	}	//	roundUp


	/**
	 * 	Get Width
	 * 	@return width
	 */
	public double getWidth()
	{
		return width;
	}	//	getWidth

	/**
	 * 	Get Height
	 * 	@return height
	 */
	public double getHeight()
	{
		return height;
	}	//	getHeight

	/*************************************************************************/

	/**
	 * 	Hash Code
	 * 	@return hash code
	 */
	public int hashCode()
	{
		long bits = Double.doubleToLongBits(width);
		bits ^= Double.doubleToLongBits(height) * 31;
		return (((int) bits) ^ ((int) (bits >> 32)));
	}	//	hashCode

	/**
	 * 	Equals
	 * 	@param obj object
	 * 	@return true if w/h is same
	 */
	public boolean equals (Object obj)
	{
		if (obj != null && obj instanceof Dimension2D)
		{
			Dimension2D d = (Dimension2D)obj;
			if (d.getWidth() == width && d.getHeight() == height)
				return true;
		}
		return false;
	}	//	equals

	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Dimension2D[w=").append(width).append(",h=").append(height).append("]");
		return sb.toString();
	}	//	toString

}	//	Dimension2DImpl
