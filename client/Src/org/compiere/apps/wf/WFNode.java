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
package org.compiere.apps.wf;

import java.awt.*;
import java.awt.font.*;
import java.text.*;
import javax.swing.*;
import javax.swing.border.*;
import org.compiere.util.*;
import org.compiere.wf.*;

/**
 *	Graphical Work Flow Node.
 *  Listen to PropertyChange for selection
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: WFNode.java,v 1.16 2005/07/23 04:56:32 jjanke Exp $
 */
public class WFNode extends JComponent
{
	/**
	 * 	Create WF Node
	 * 	@param node model
	 */
	public WFNode (MWFNode node)
	{
		super();
		setOpaque(true);
		m_node = node;
		setName(m_node.getName());
		m_icon = new WFIcon(node.getAction());
		m_name = m_node.getName(true);
		setBorder(s_border);
		
		//	Tool Tip
		String description = node.getDescription(true);
		if (description != null && description.length() > 0)
			setToolTipText(description);
		else
			setToolTipText(node.getName(true));
		
		//	Location
		setBounds(node.getXPosition(), node.getYPosition(), s_size.width, s_size.height);
		log.config(node.getAD_WF_Node_ID() 
			+ "," + node.getName() + " - " + getLocation());
		setSelected(false);
		setVisited(false);
	}	//	WFNode

	/**	Selected Property value			*/
	public static String	PROPERTY_SELECTED = "selected";
	/**	Standard (raised) Border		*/
	private static Border 	s_border = BorderFactory.createBevelBorder(BevelBorder.RAISED);
	/**	Selected (lowered) Border		*/
	private static Border 	s_borderSelected = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	/**	Size of the Node				*/
	private static Dimension	s_size = new Dimension (120, 50);
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(WFNode.class);
	
	/**	ID						*/
	private MWFNode 		m_node = null;
	/**	Icon					*/
	private WFIcon			m_icon = null;
	/** Name to paint			*/
	private String			m_name = null;
	/**	Selected Value			*/
	private boolean			m_selected = false;
	/**	Visited Value			*/
	private boolean			m_visited = false;
	/**	Was node moved			*/
	private boolean			m_moved = false;

	
	/**************************************************************************
	 * 	Set Selected.
	 * 	Selected: blue foreground - lowered border
	 * 	UnSelected: black foreground - raised border
	 * 	@param selected selected
	 */
	public void setSelected (boolean selected)
	{
		firePropertyChange(PROPERTY_SELECTED, m_selected, selected);
		m_selected = selected;
		if (m_selected)
		{
			setBorder (s_borderSelected);
			setForeground(Color.blue);
		}
		else
		{
			setBorder (s_border);
			setForeground (Color.black);
		}
	}	//	setSelected
	
	/**
	 * 	Set Visited.
	 * 	Visited: green background
	 * 	NotVisited: 
	 *	@param visited visited
	 */
	public void setVisited (boolean visited)
	{
		m_visited = visited;
		if (m_visited)
		{
			setBackground(Color.green);
		}
		else
		{
			setBackground(Color.lightGray);
		}
	}	//	setVisited

	/**
	 * 	Get Selected
	 * 	@return selected
	 */
	public boolean isSelected()
	{
		return m_selected;
	}	//	isSelected

	/**
	 * 	Get Client ID
	 * 	@return Client ID
	 */
	public int getAD_Client_ID()
	{
		return m_node.getAD_Client_ID();
	}	//	getAD_Client_ID

	/**
	 * 	Is the node Editable
	 *	@return yes if the Client is the same
	 */
	public boolean isEditable()
	{
		return getAD_Client_ID() == Env.getAD_Client_ID(Env.getCtx());
	}	//	isEditable
	
	
	/**
	 * 	Get Node ID
	 * 	@return Node ID
	 */
	public int getAD_WF_Node_ID()
	{
		return m_node.getAD_WF_Node_ID();
	}	//	getAD_WF_Node_ID

	/**
	 * 	Get Node Model
	 * 	@return Node Model
	 */
	public MWFNode getModel()
	{
		return m_node;
	}	//	getModel

	/**
	 * 	Set Location - also for Node.
	 *	@param x x
	 *	@param y y
	 */
	public void setLocation (int x, int y)
	{
		super.setLocation (x, y);
		m_node.setPosition(x, y);
	}
	
	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("WFNode[");
		sb.append(getAD_WF_Node_ID()).append("-").append(m_name)
			.append(",").append(getBounds())
			.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Get Font.
	 * 	Italics if not editable
	 *	@return font
	 */
	public Font getFont ()
	{
		Font base = new Font(null);
		if (!isEditable())
			return base;
		//	Return Bold Italic Font
		return new Font(base.getName(), Font.ITALIC | Font.BOLD, base.getSize());
	}	//	getFont
	
	/**************************************************************************
	 * 	Get Preferred Size
	 *	@return size
	 */
	public Dimension getPreferredSize ()
	{
		return s_size;
	}	//	getPreferredSize

	/**
	 * 	Paint Component
	 *	@param g Graphics
	 */
	protected void paintComponent (Graphics g)
	{
		Graphics2D g2D = (Graphics2D)g;
		Rectangle bounds = getBounds();
		m_icon.paintIcon(this, g2D, 0, 0);
		//	Paint Text
		Color color = getForeground();
		g2D.setPaint(color);
		Font font = getFont();
		//
		AttributedString aString = new AttributedString(m_name);
		aString.addAttribute(TextAttribute.FONT, font);
		aString.addAttribute(TextAttribute.FOREGROUND, color);
		AttributedCharacterIterator iter = aString.getIterator();
		//
		LineBreakMeasurer measurer = new LineBreakMeasurer(iter, g2D.getFontRenderContext());
		float width = s_size.width - m_icon.getIconWidth() - 2;
		TextLayout layout = measurer.nextLayout(width);
		float xPos = m_icon.getIconWidth();
		float yPos = layout.getAscent() + 2;
		//
		layout.draw(g2D, xPos, yPos);
		width = s_size.width - 4;	//	2 pt 
		while (measurer.getPosition() < iter.getEndIndex())
		{
			layout = measurer.nextLayout(width);
			yPos += layout.getAscent() + layout.getDescent() + layout.getLeading();
			layout.draw(g2D, 2, yPos);
		}
	}	//	paintComponent
	
}	//	WFNode
