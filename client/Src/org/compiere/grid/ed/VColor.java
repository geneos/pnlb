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
package org.compiere.grid.ed;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.math.*;
import java.sql.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.model.*;
import org.compiere.plaf.CompiereColor;
import org.compiere.plaf.CompiereColorEditor;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *  Color Editor.
 *  The editor stores/gets the attributes from the tab.
 *
 *  @author     Jorg Janke
 *  @version    $Id: VColor.java,v 1.17 2005/11/14 02:10:57 jjanke Exp $
 */
public class VColor extends CButton
	implements VEditor, ActionListener
{
	/**
	 *  Constructor
	 *  @param mTab	Tab
	 *  @param mandatory mandatory
	 *  @param isReadOnly read only
	 */
	public VColor (MTab mTab, boolean mandatory, boolean isReadOnly)
	{
		m_mTab = mTab;
		setMandatory(mandatory);
		setReadWrite(!isReadOnly);
		addActionListener(this);
	}   //  VColor

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_mTab = null;
	}   //  dispose

	private MTab            m_mTab;
	private boolean         m_mandatory;
//	private int             m_AD_Color_ID = 0;
	private CompiereColor   m_cc = null;
	private Object          m_value;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VColor.class);

	/**
	 *  Set Mandatory
	 *  @param mandatory mandatory
	 */
	public void setMandatory (boolean mandatory)
	{
		m_mandatory = mandatory;
	}   //  setMandatory

	/**
	 *  Is Mandatory
	 *  @return true if Mandatory
	 */
	public boolean isMandatory()
	{
		return m_mandatory;
	}   //  isMandatory

	/**
	 *  Set Background (nop)
	 *  @param error error
	 */
	public void setBackground (boolean error)
	{
	}   //  setBackground

	/**
	 *  Set Value
	 *  @param value value
	 */
	public void setValue (Object value)
	{
		log.config("Value=" + value);
		m_value = value;
		m_cc = getCompiereColor();

		//  Display It
		setText(getDisplay());
		if (m_cc != null)
			setBackgroundColor(m_cc);
		else
		{
			setOpaque(false);
			putClientProperty(CompierePLAF.BACKGROUND, null);
		}
		repaint();
	}   //  setValue

	/**
	 *  GetValue
	 *  @return value
	 */
	public Object getValue()
	{
		return m_value;
	}   //  getValue

	/**
	 *  Get Displayed Value
	 *  @return String representation
	 */
	public String getDisplay()
	{
		if (m_cc == null)
			return "-/-";
		return " ";
	}   //  getDisplay

	/**
	 *  Property Change Listener
	 *  @param evt event
	 */
	public void propertyChange (PropertyChangeEvent evt)
	{
	//	log.config( "VColor.propertyChange", evt);
		if (evt.getPropertyName().equals(org.compiere.model.MField.PROPERTY))
		{
			setValue(evt.getNewValue());
			setBackground(false);
		}
	}   //  propertyChange

	/**
	 *  Set Field/WindowNo for ValuePreference
	 *  @param mField field
	 */
	public void setField (MField mField)
	{
		mField.setValueNoFire(false);  //  fire every time
	}   //  setField

	/*************************************************************************/

	/**
	 *  Load Color from Tab
	 *  @return true if loaded
	 *  @see org.compiere.model.MColor#getCompiereColor
	 */
	private CompiereColor getCompiereColor()
	{
		Integer AD_Color_ID = (Integer)m_mTab.getValue("AD_Color_ID");
		log.fine("AD_Color_ID=" + AD_Color_ID);
		CompiereColor cc = null;

		//  Color Type
		String ColorType = (String)m_mTab.getValue("ColorType");
		if (ColorType == null)
		{
			log.fine("No ColorType");
			return null;
		}
		//
		if (ColorType.equals(CompiereColor.TYPE_FLAT))
		{
			cc = new CompiereColor(getColor(true), true);
		}
		else if (ColorType.equals(CompiereColor.TYPE_GRADIENT))
		{
			Integer RepeatDistance = (Integer)m_mTab.getValue("RepeatDistance");
			String StartPoint = (String)m_mTab.getValue("StartPoint");
			int repeatDistance = RepeatDistance == null ? 0 : RepeatDistance.intValue();
			int startPoint = StartPoint == null ? 0 : Integer.parseInt(StartPoint);
			cc = new CompiereColor(getColor(true), getColor(false), startPoint, repeatDistance);
		}
		else if (ColorType.equals(CompiereColor.TYPE_LINES))
		{
			BigDecimal LineWidth = (BigDecimal)m_mTab.getValue("LineWidth");
			BigDecimal LineDistance = (BigDecimal)m_mTab.getValue("LineDistance");
			int lineWidth = LineWidth == null ? 0 : LineWidth.intValue();
			int lineDistance = LineDistance == null ? 0 : LineDistance.intValue();
			cc = new CompiereColor(getColor(false), getColor(true), lineWidth, lineDistance);
		}
		else if (ColorType.equals(CompiereColor.TYPE_TEXTURE))
		{
			Integer AD_Image_ID = (Integer)m_mTab.getValue("AD_Image_ID");
			String url = getURL(AD_Image_ID);
			if (url == null)
				return null;
			BigDecimal ImageAlpha = (BigDecimal)m_mTab.getValue("ImageAlpha");
			float compositeAlpha = ImageAlpha == null ? 0.7f : ImageAlpha.floatValue();
			cc = new CompiereColor(url, getColor(true), compositeAlpha);
		}
		else
			return null;

		log.fine("CompiereColor=" + cc);
		return cc;
	}   //  getCompiereColor

	/**
	 *  Get Color from Tab
	 *  @param primary true if primary false if secondary
	 *  @return Color
	 */
	private Color getColor (boolean primary)
	{
		String add = primary ? "" : "_1";
		//	is either BD or Int
		Integer Red = (Integer)m_mTab.getValue("Red" + add);
		Integer Green = (Integer)m_mTab.getValue("Green" + add);
		Integer Blue = (Integer)m_mTab.getValue("Blue" + add);
		//
		int red = Red == null ? 0 : Red.intValue();
		int green = Green == null ? 0 : Green.intValue();
		int blue = Blue == null ? 0 : Blue.intValue();
		//
		return new Color (red, green, blue);
	}   //  getColor

	/**
	 *  Get URL from Image
	 *  @param AD_Image_ID image
	 *  @return URL as String or null
	 */
	private String getURL (Integer AD_Image_ID)
	{
		if (AD_Image_ID == null || AD_Image_ID.intValue() == 0)
			return null;
		//
		String retValue = null;
		String sql = "SELECT ImageURL FROM AD_Image WHERE AD_Image_ID=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt (1, AD_Image_ID.intValue());
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

	/*************************************************************************/

	/**
	 *  Action Listener - Open Dialog
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		//  Show Dialog
		CompiereColor cc = CompiereColorEditor.showDialog((JFrame)Env.getParent(this), m_cc);
		if (cc == null)
		{
			log.info( "VColor.actionPerformed - no color");
			return;
		}
		setBackgroundColor(cc);		//	set Button
		repaint();

		//  Update Values
		m_mTab.setValue("ColorType", cc.getType());
		if (cc.isFlat())
		{
			setColor (cc.getFlatColor(), true);
		}
		else if (cc.isGradient())
		{
			setColor (cc.getGradientUpperColor(), true);
			setColor (cc.getGradientLowerColor(), false);
			m_mTab.setValue("RepeatDistance",   new BigDecimal(cc.getGradientRepeatDistance()));
			m_mTab.setValue("StartPoint",       String.valueOf(cc.getGradientStartPoint()));
		}
		else if (cc.isLine())
		{
			setColor (cc.getLineBackColor(), true);
			setColor (cc.getLineColor(), false);
			m_mTab.getValue("LineWidth");
			m_mTab.getValue("LineDistance");
		}
		else if (cc.isTexture())
		{
			setColor (cc.getTextureTaintColor(), true);
		//	URL url = cc.getTextureURL();
		//	m_mTab.setValue("AD_Image_ID");
			m_mTab.setValue("ImageAlpha", new BigDecimal(cc.getTextureCompositeAlpha()));
		}
		m_cc = cc;
	}   //  actionPerformed

	/**
	 *  Set Color in Tab
	 *  @param c Color
	 *  @param primary true if primary false if secondary
	 */
	private void setColor (Color c, boolean primary)
	{
		String add = primary ? "" : "_1";
		m_mTab.setValue("Red" + add,    new BigDecimal(c.getRed()));
		m_mTab.setValue("Green" + add,  new BigDecimal(c.getGreen()));
		m_mTab.setValue("Blue" + add,   new BigDecimal(c.getBlue()));
	}   //  setColor

}   //  VColor
