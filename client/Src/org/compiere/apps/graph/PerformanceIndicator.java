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
import java.awt.font.*;
import java.text.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 * 	Performance Indicator
 *	
 *  @author Jorg Janke
 *  @version $Id: PerformanceIndicator.java,v 1.3 2006/01/04 03:59:31 jjanke Exp $
 */
public class PerformanceIndicator extends JComponent 
	implements MouseListener, ActionListener
{
	/**
	 * 	Constructor
	 *	@param goal goal model
	 */
	public PerformanceIndicator(MGoal goal)
	{
		super();
		m_goal = goal;
		setName(m_goal.getName());
		getPreferredSize();		//	calculate size
		setOpaque(true);
		updateDisplay();
		//
		mRefresh.addActionListener(this);
		popupMenu.add(mRefresh);
		//
		addMouseListener(this);
	}	//	PerformanceIndicator

	private MGoal				m_goal = null;
	/**	The Performance Name		*/
	private String				m_text = null;
	/** Performance Line			*/ 
	private double				m_line = 0;
	
	/**	Height						*/
	private static double		s_height = 50;
	/**	100% width					*/
	private static double		s_width100 = 150;
	/**	Max width					*/
	private static double		s_widthMax = 250;
	/** Integer Number Format		*/
	private static DecimalFormat	s_format = DisplayType.getNumberFormat(DisplayType.Integer);

	JPopupMenu 					popupMenu = new JPopupMenu();
	private CMenuItem 			mRefresh = new CMenuItem(Msg.getMsg(Env.getCtx(), "Refresh"), Env.getImageIcon("Refresh16.gif"));

	/**
	 * 	Get Goal
	 *	@return goal
	 */
	public MGoal getGoal()
	{
		return m_goal;
	}	//	getGoal

	/**
	 * 	Update Display Data
	 */
	protected void updateDisplay()
	{
		//	Set Text
		StringBuffer text = new StringBuffer(m_goal.getName());
		if (m_goal.isTarget())
			text.append(": ").append(m_goal.getPercent()).append("%");
		else
			text.append(": ").append(s_format.format(m_goal.getMeasureActual()));
		m_text = text.toString();
		//	ToolTip
		text = new StringBuffer();
		if (m_goal.getDescription() != null)
			text.append(m_goal.getDescription()).append(": ");
		text.append(s_format.format(m_goal.getMeasureActual()));
		if (m_goal.isTarget())
			text.append(" ").append(Msg.getMsg(Env.getCtx(), "of")).append(" ")
				.append(s_format.format(m_goal.getMeasureTarget()));
		setToolTipText(text.toString());
		//
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		setBackground(m_goal.getColor());
		setForeground(GraphUtil.getForeground(getBackground()));
		//	Performance Line
		int percent = m_goal.getPercent();
		if (percent > 100)			//	draw 100% line
			m_line = s_width100;
		else						//	draw Performance Line
			m_line = s_width100 * m_goal.getGoalPerformanceDouble();
		setPreferredSize(null);		//	resets
		getPreferredSize();
	}	//	updateData
	
	
	/**************************************************************************
	 * 	Get Preferred Size
	 *	@return size
	 */
	public Dimension getPreferredSize()
	{
		if (!isPreferredSizeSet())
		{
			double width = s_width100;
			if (m_goal.getPercent() > 100)
			{
				width = width * m_goal.getGoalPerformanceDouble();
				width = Math.min(width, s_widthMax);
			}
			Dimension size = new Dimension();
			size.setSize(width, s_height);
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
		}
		return super.getPreferredSize();
	}	//	getPreferredSize
	
	/**
	 * 	Paint Component
	 *	@param g Graphics
	 */
	protected void paintComponent (Graphics g)
	{
		Graphics2D g2D = (Graphics2D)g;
		Rectangle bounds = getBounds();
		Insets insets = getInsets();
		//	Background
		g2D.setColor(GraphUtil.darker(getBackground(), 0.85));
		Dimension size = getPreferredSize();
		g2D.fill3DRect(0+insets.right, 0+insets.top, 
			size.width-insets.right-insets.left, 
			size.height-insets.top-insets.bottom, true);
		
		//	Paint Text
		Color color = getForeground();
		g2D.setPaint(color);
		Font font = getFont();	//	Bold +1
		font = new Font(font.getName(), Font.BOLD, font.getSize()+1);
		//
		AttributedString aString = new AttributedString(m_text);
		aString.addAttribute(TextAttribute.FONT, font);
		aString.addAttribute(TextAttribute.FOREGROUND, color);
		AttributedCharacterIterator iter = aString.getIterator();
		//
		LineBreakMeasurer measurer = new LineBreakMeasurer(iter, g2D.getFontRenderContext());
		float width = getPreferredSize().width - 8;	//	4 pt ;
		float xPos = 4;
		float yPos = 4;
		while (measurer.getPosition() < iter.getEndIndex())
		{
			TextLayout layout = measurer.nextLayout(width);
			yPos += layout.getAscent() + layout.getDescent() + layout.getLeading();
			layout.draw(g2D, xPos, yPos);
		}
		
		//	Paint Performance Line
		int x = (int)(m_line);
		int y = (int)(size.height-insets.bottom);
		g2D.setPaint(Color.black);
		g2D.drawLine(x, insets.top, x, y);
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
        			"pi", event.getWhen(), event.getModifiers());
        	listeners[i].actionPerformed(e);
        }
    }	//	fireActionPerformed
	
    
    /**************************************************************************
     * 	Mouse Clicked
     *	@param e mouse event
     */
	public void mouseClicked (MouseEvent e)
	{
		if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() > 1)
			fireActionPerformed(e);
		if (SwingUtilities.isRightMouseButton(e))
			popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
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

	/**
	 * 	Action Listener.
	 * 	Update Display
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == mRefresh)
		{
			if (m_goal.updateGoal(true))
				m_goal.save();
			updateDisplay();
			invalidate();
		}
	}	//	actionPerformed
	
}	//	PerformanceIndicator
