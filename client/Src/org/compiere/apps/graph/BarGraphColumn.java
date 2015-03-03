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
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import java.math.*;
import java.sql.*;
import java.text.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Bar Graph Column
 *	
 *  @author Jorg Janke
 *  @version $Id: BarGraphColumn.java,v 1.1 2005/12/27 06:18:37 jjanke Exp $
 */
public class BarGraphColumn extends JComponent implements MouseListener
{
	/**
	 * 	Base Constructor
	 *	@param label
	 *	@param value
	 */
	public BarGraphColumn (String label, double value)
	{
		m_label = label;
		setValue(value);
		addMouseListener(this);
	}	//	BarGraphColumn
	
	/**
	 * 	Achievement Constructor
	 *	@param achievement
	 */
	public BarGraphColumn (MAchievement achievement)
	{
		this (achievement.getName(), achievement.getManualActual().doubleValue());
		m_achievement = achievement;
	}	//	BarGraphColumn

	/**
	 * 	Measure Calc Constructor
	 *	@param mc MeasureCalc
	 */
	public BarGraphColumn (MMeasureCalc mc, BigDecimal data)
	{
		this ("", data == null ? 0 : data.doubleValue());
		m_mc = mc;
	}	//	BarGraphColumn

	
	/** Optional Achievement		*/
	private MAchievement	m_achievement = null;
	/** Measure Calc				*/
	private MMeasureCalc	m_mc = null;
	private String			m_measureDisplay = null;
	private Timestamp		m_date = null;
	
	/** Column Label				*/
	private String	m_label = null;
	/** Column Data Value			*/
	private double	m_value = 0;
	/** Column Label Value			*/
	private String	m_labelValue = "";
	/** Column Data Target Value	*/
	private double	m_targetValue = 0;
	/** Column Width in pixles		*/
	private double	m_width = 0;
	/** Column Height in pixles		*/
	private double	m_height = 0;
	
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (BarGraphColumn.class);
	/** Integer Number Format		*/
	private static DecimalFormat	s_format = DisplayType.getNumberFormat(DisplayType.Integer);
	
	/**
	 * 	Get Achievement
	 *	@return achievement or null
	 */
	public MAchievement getAchievement()
	{
		return m_achievement;
	}	//	getAchievement
	
	/**
	 * 	Get MeasureCalc
	 *	@return measure
	 */
	public MMeasureCalc getMeasureCalc()
	{
		return m_mc;
	}	//	getMeasureCalc
	
	public String getMeasureDisplay()
	{
		return m_measureDisplay;
	}	//	getMeasureDisplay
	
	public Timestamp getDate()
	{
		return m_date;
	}	//	getDate
	
	/**
	 * 	Set Background and matching Foreground
	 *	@param bg background
	 */
	public void setBackground (Color bg)
	{
		super.setBackground (bg);
		setForeground(GraphUtil.getForeground(bg));
	}	//	setBackground
	
	/**
	 * @return Returns the label.
	 */
	public String getLabel ()
	{
		return m_label;
	}	//	getLabel
	
	/**
	 * @param label The label to set.
	 */
	public void setLabel (String label)
	{
		m_label = label;
		if (m_label != null)
			m_labelValue = s_format.format(m_value) + " - " + m_label;
		else
			m_labelValue = s_format.format(m_value);
		setToolTipText(m_labelValue);
		setName(m_labelValue);
	}	//	setLabel

	/**
	 *	@param date for label.
	 * 	@param MeasureDisplay measure display
	 */
	public void setLabel (Timestamp date, String MeasureDisplay)
	{
		if (date == null)
			return;
		m_date = date;
		m_measureDisplay = MeasureDisplay;
		//
		SimpleDateFormat format = DisplayType.getDateFormat(DisplayType.Date);
		String text = format.format(date);
		//	Month only
		if (MGoal.MEASUREDISPLAY_Month.equals(MeasureDisplay)
			|| MGoal.MEASUREDISPLAY_Quarter.equals(MeasureDisplay))
		{
			String pattern = format.toPattern();
			String mmText = text;
			int index = pattern.indexOf("dd");
			if (index == 0)			//	dd.MM.yyyy
				mmText = text.substring(3);
			else if (index > 0)		//	MM/dd/yyyy
			{
				mmText = text.substring(0, index-1);
				if (text.length() > index+2)
					mmText += text.substring(index+2);
			}
			setLabel(mmText);
		}
		else	//	Day
			setLabel(text);
	}	//	setLabel

	/**
	 * @return Returns the targetValue.
	 */
	public double getTargetValue ()
	{
		return m_targetValue;
	}	//	getTargetValue
	
	/**
	 * @param targetValue The targetValue to set.
	 */
	public void setTargetValue (double targetValue)
	{
		m_targetValue = targetValue;
	}	//	setTargetValue
	
	/**
	 * @return Returns the data value.
	 */
	public double getValue ()
	{
		return m_value;
	}	//	getValue
	
	/**
	 * @param value The data value to set.
	 */
	public void setValue (double value)
	{
		m_value = value;
		if (m_label != null)
			m_labelValue = s_format.format(m_value) + " - " + m_label;
		else
			m_labelValue = s_format.format(m_value);
		setToolTipText(m_labelValue);
		setName(m_labelValue);
	}	//	setValue
	
	/**
	 * @return Returns the column width in pixles.
	 */
	public double getColWidth ()
	{
		return m_width;
	}	//	getColWidth
	
	/**
	 * @param width The column width in pixles.
	 */
	public void setColWidth (double width)
	{
		m_width = width;
		if (isPreferredSizeSet())
			setPreferredSize(null);
	}	//	getColWidth

	/**
	 * @return Returns the height in pixles.
	 */
	public double getColHeight()
	{
		return m_height;
	}	//	getHeight
	
	/**
	 * @param height The hight in pixles.
	 */
	public void setColHeight (double height)
	{
		m_height = height;
		if (isPreferredSizeSet())
			setPreferredSize(null);
	}	//	setHeight
	
	/**
	 * 	Get Maximum Size
	 *	@return size
	 */
	public Dimension getMaximumSize ()
	{
		return getPreferredSize();
	}	//	getMaximumSize

	/**
	 * 	Get Minimum Size
	 *	@return size
	 */
	public Dimension getMinimumSize ()
	{
		return getPreferredSize();
	}	//	getMinimumSize

	/**
	 * 	Get Preferred Size
	 *	@return size
	 */
	public Dimension getPreferredSize ()
	{
		if (!isPreferredSizeSet()) 
		{
			Dimension size = new Dimension((int)m_width, (int)m_height);
			setPreferredSize(size);
		}
		return super.getPreferredSize ();
	}	//	getPreferredSize
	
	
	/**
	 * 	Paint Component
	 *	@param g graphics
	 */
	protected void paintComponent (Graphics g)
	{
		Graphics2D g2D = (Graphics2D)g;
		Rectangle bounds = getBounds();
		//	Background
		g2D.setColor(getBackground());
		Dimension size = getPreferredSize();
		g2D.fill3DRect(0, 0, size.width, size.height, true);
		
		//	Paint Label & Value
		Color color = getForeground();
		g2D.setPaint(color);
		//
		Font font = getFont();
		FontMetrics fm = g2D.getFontMetrics(font);
		int fontHeight = fm.getHeight();
		AffineTransform transform = AffineTransform.getRotateInstance(Math.PI*3/2);
		font = font.deriveFont(transform);
		g2D.setFont(font);
		//
		int x = (int)(size.width/2)+((fontHeight-2)/2);
		if (x < fontHeight)
			x = fontHeight-2;
		int y = (int)(size.height-3);
		g2D.drawString(m_labelValue, x, y);
		log.fine("x=" + x + ",fontHeight=" + fontHeight +  ", y=" + y + " - " + m_labelValue);
		//	Paint Target
		if (m_targetValue != 0)
		{
			
		}
	}	//	paintComponent
	
	
    /**************************************************************************
     * Adds an <code>ActionListener</code> to the indicator.
     * @param l the <code>ActionListener</code> to be added
     */
    public void addActionListener(ActionListener l) 
    {
    	if (l != null)
    		listenerList.add(ActionListener.class, l);
    }	//	addActionListener
    
    /**
     * Removes an <code>ActionListener</code> from the indicator.
     * @param l the listener to be removed
     */
    public void removeActionListener(ActionListener l) 
    {
    	if (l != null)
    		listenerList.remove(ActionListener.class, l);
    }	//	removeActionListener
    
    /**
     * Returns an array of all the <code>ActionListener</code>s added
     * to this indicator with addActionListener().
     *
     * @return all of the <code>ActionListener</code>s added or an empty
     *         array if no listeners have been added
     */
    public ActionListener[] getActionListeners() 
    {
        return (ActionListener[])(listenerList.getListeners(ActionListener.class));
    }	//	getActionListeners

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the <code>event</code> 
     * parameter.
     *
     * @param event  the <code>ActionEvent</code> object
     * @see EventListenerList
     */
    protected void fireActionPerformed(MouseEvent event) 
    {
        // Guaranteed to return a non-null array
    	ActionListener[] listeners = getActionListeners();
        ActionEvent e = null;
        // Process the listeners first to last
        for (int i = 0; i < listeners.length; i++) 
        {
        	//	Lazily create the event:
        	if (e == null) 
        		e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
        			"column"+m_label, event.getWhen(), event.getModifiers());
        	listeners[i].actionPerformed(e);
        }
    }	//	fireActionPerformed
	
    
    /**
     * 	Mouse Clicked
     *	@param e mouse event
     */
	public void mouseClicked (MouseEvent e)
	{
		if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() > 1)
			fireActionPerformed(e);
	}	//	mouseClicked

	public void mousePressed (MouseEvent e)
	{
	}

	public void mouseReleased (MouseEvent e)
	{
	}

	public void mouseEntered (MouseEvent e)
	{
	}

	public void mouseExited (MouseEvent e)
	{
	}

}	//	BarGraphColumn
