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
package org.compiere.apps;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.compiere.plaf.CompierePLAF;

import java.util.logging.*;
import org.compiere.util.*;


/**
 *  Glass Pane to display "waiting" and capture events while processing.
 *
 *  @author     Jorg Janke
 *  @version    $Id: AGlassPane.java,v 1.8 2005/03/11 20:27:59 jjanke Exp $
 */
public class AGlassPane extends JPanel implements MouseListener, ActionListener
{
	/**
	 *  Constructor
	 */
	public AGlassPane()
	{
		this.setOpaque(false);
		this.setVisible(false);
		this.addMouseListener(this);
	}   //  AGlassPane

	/** The Image               */
	public static Image         s_image = Env.getImage("C10030.gif");
	/** The Message Font        */
	public static Font          s_font = new Font("Dialog", 3, 14);
	/** The Message Color       */
	public static Color         s_color = CompierePLAF.getTextColor_OK();

	/** Gap between components  */
	private static final int    GAP = 4;

	/** The Message             */
	private String              m_message = Msg.getMsg(Env.getCtx(), "Processing");

	/** Timer                   */
	private Timer               m_timer;
	/** Value of timer          */
	private int                 m_timervalue = 0;
	private int                 m_timermax = 0;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(AGlassPane.class);
	
	/**
	 *  Set Message
	 *  @param AD_Message to be translated - null resets to default message
	 */
	public void setMessage(String AD_Message)
	{
		if (AD_Message == null)
			m_message = Msg.getMsg(Env.getCtx(), "Processing");
		else if (AD_Message.length() == 0)
			m_message = AD_Message;		//	nothing
		else
			m_message = Msg.getMsg(Env.getCtx(), AD_Message);
		if (isVisible())
			repaint();
	}   //  setMessage

	/**
	 *  Get Message
	 *  @return displayed message
	 */
	public String getMessage()
	{
		return m_message;
	}   //  getMessage

	
	/**************************************************************************
	 *  Set and start Busy Counter if over 2 seconds
	 *  @param time in seconds
	 */
	public void setBusyTimer (int time)
	{
		log.config( "AGlassPane.setBusyTimer - " + time);
		//  should we display a progress bar?
		if (time < 2)
		{
			m_timermax = 0;
			if (isVisible())
				repaint();
			return;
		}

		m_timermax = time;
		m_timervalue = 0;

		//  Start Timer
		m_timer = new Timer (1000, this);     //  every second
		m_timer.start();

		if (!isVisible())
			setVisible(true);
		repaint();
	}   //  setBusyTimer

	/**
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (m_timermax > 0)
		{
			m_timervalue++;
			if (m_timervalue > m_timermax)
				m_timervalue = 0;
			repaint();
		}
	}   //  actionPerformed

	
	/**************************************************************************
	 *  Paint Component.
	 *  <pre>
	 *      image
	 *      message
	 *      progressBar
	 *  </pre>
	 *  @param g
	 */
	public void paintComponent (Graphics g)
	{
		Dimension panelSize = getSize();
		g.setColor(new Color(1f,1f,1f, 0.4f));      //  .5 is a bit too light
		g.fillRect(0,0, panelSize.width, panelSize.height);
		//
		g.setFont(s_font);
		g.setColor(s_color);
		FontMetrics fm = g.getFontMetrics();
		Dimension messageSize = new Dimension (fm.stringWidth(m_message), fm.getAscent() + fm.getDescent());
		Dimension imageSize = new Dimension (s_image.getWidth(this), s_image.getHeight(this));
		Dimension progressSize = new Dimension(150, 15);
	//	System.out.println("Panel=" + panelSize + " - Message=" + messageSize + " - Image=" + imageSize + " - Progress=" + progressSize);

		//  Horizontal layout
		int height = imageSize.height + GAP + messageSize.height + GAP + progressSize.height;
		if (height > panelSize.height)
		{
			log.log(Level.SEVERE, "AGlassPane.paintComponent - Panel too small - height=" + panelSize.height);
			return;
		}
		int yImage = (panelSize.height/2) - (height/2);
		int yMessage = yImage + imageSize.height + GAP + fm.getAscent();
		int yProgress = yMessage + fm.getDescent() + GAP;
	//	System.out.println("yImage=" + yImage + " - yMessage=" + yMessage);

		//  Vertical layout
		if (imageSize.width > panelSize.width || messageSize.width > panelSize.width)
		{
			log.log(Level.SEVERE, "AGlassPane.paintComponent - Panel too small - width=" + panelSize.width);
			return;
		}
		int xImage = (panelSize.width/2) - (imageSize.width/2);
		int xMessage = (panelSize.width/2) - (messageSize.width/2);
		int xProgress = (panelSize.width/2) - (progressSize.width/2);

		g.drawImage(s_image, xImage, yImage, this);
		g.drawString(m_message, xMessage, yMessage);
		if (m_timermax > 0)
		{
			int pWidth = progressSize.width / m_timermax * m_timervalue;
			g.setColor(CompierePLAF.getPrimary3());
			g.fill3DRect(xProgress, yProgress, pWidth, progressSize.height, true);
			g.setColor(CompierePLAF.getPrimary2());
			g.draw3DRect(xProgress, yProgress, progressSize.width, progressSize.height, true);
		}
	}   //  paintComponent


	/**************************************************************************
	 *  Mouse Listener
	 *  @param e
	 */
	public void mouseClicked(MouseEvent e)
	{
		if (isVisible())
			e.consume();
	}
	/**
	 *  Mouse Listener
	 *  @param e
	 */
	public void mousePressed(MouseEvent e)
	{
		if (isVisible())
			e.consume();
	}
	/**
	 *  Mouse Listener
	 *  @param e
	 */
	public void mouseReleased(MouseEvent e)
	{
		if (isVisible())
			e.consume();
	}
	/**
	 *  Mouse Listener
	 *  @param e
	 */
	public void mouseEntered(MouseEvent e)
	{
		if (isVisible())
			e.consume();
	}
	/**
	 *  Mouse Listener
	 *  @param e
	 */
	public void mouseExited(MouseEvent e)
	{
		if (isVisible())
			e.consume();
	}

}   //  AGlassPane