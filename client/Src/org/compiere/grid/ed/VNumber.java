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
import java.text.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.text.*;
import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Number Control
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VNumber.java,v 1.41 2005/09/03 01:57:16 jjanke Exp $
 */
public final class VNumber extends JComponent
	implements VEditor, ActionListener, KeyListener, FocusListener
{
	/**	Number of Columns (12)		*/
	public final static int SIZE = 12;

	/**
	 *  IDE Bean Constructor
	 */
	public VNumber()
	{
		this("Number", false, false, true, DisplayType.Number, "Number");
	}   //  VNumber

	/**
	 *	Create right aligned Number field.
	 *	no popup, if WindowNo == 0 (for IDs)
	 *  @param columnName column name
	 *  @param mandatory mandatory
	 *  @param isReadOnly read only
	 *  @param isUpdateable updateable
	 *  @param displayType display type
	 *  @param title title
	 */
	public VNumber(String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		int displayType, String title)
	{
		super();
		super.setName(columnName);
		m_columnName = columnName;
		m_title = title;
		setDisplayType(displayType);
		//
		LookAndFeel.installBorder(this, "TextField.border");
		this.setLayout(new BorderLayout());
//		this.setPreferredSize(m_text.getPreferredSize());		//	causes r/o to be the same length
//		int height = m_text.getPreferredSize().height;
//		setMinimumSize(new Dimension (30,height));

		//	***	Text	***
		m_text.setBorder(null);
		m_text.setHorizontalAlignment(JTextField.TRAILING);
		m_text.addKeyListener(this);
		m_text.addFocusListener(this);
		//	Background
		setMandatory(mandatory);
		this.add(m_text, BorderLayout.CENTER);

		//	***	Button	***
		m_button.setIcon(Env.getImageIcon("Calculator10.gif"));
		m_button.setMargin(new Insets(0, 0, 0, 0));
		m_button.setFocusable(false);
		m_button.addActionListener(this);
		this.add (m_button, BorderLayout.EAST);

		//	Prefereed Size
		this.setPreferredSize(this.getPreferredSize());		//	causes r/o to be the same length

		//  Size
		setColumns(SIZE, CComboBox.FIELD_HIGHT-4);	
		//	ReadWrite
		if (isReadOnly || !isUpdateable)
			setReadWrite(false);
		else
			setReadWrite(true);
	}	//	VNumber

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_text = null;
		m_button = null;
		m_mField = null;
	}   //  dispose

	/**
	 *	Set Document
	 *  @param doc document
	 */
	protected void setDocument(Document doc)
	{
		m_text.setDocument(doc);
	}	//	getDocument

	private String			m_columnName;
	protected int			m_displayType;	//  Currency / UoM via Context
	private DecimalFormat	m_format;
	private String			m_title;
	private boolean			m_setting;
	private String			m_oldText;
	private String			m_initialText;

	private boolean			m_rangeSet = false;
	private Double			m_minValue;
	private Double			m_maxValue;
	private boolean			m_modified = false;
	
	/**  The Field                  */
	private CTextField		m_text = new CTextField(SIZE);	//	Standard
	/** The Button                  */
	private CButton		    m_button = new CButton();

	private MField          m_mField = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VNumber.class);

	/**
	 * 	Set no of Columns
	 *	@param columns columns
	 */
	public void setColumns (int columns, int height)
	{
		m_text.setPreferredSize(null);
		m_text.setColumns(columns);
		Dimension size = m_text.getPreferredSize();
		if (height > size.height)			//	default 16
			size.height = height;
		if (CComboBox.FIELD_HIGHT-4 > size.height)
			size.height = VLookup.FIELD_HIGHT-4;
		this.setPreferredSize(size);		//	causes r/o to be the same length
		this.setMinimumSize(new Dimension (columns*10, size.height));
		m_button.setPreferredSize(new Dimension(size.height, size.height));
	}	//	setColumns
	
	/**
	 *	Set Range with min & max
	 *  @param minValue min value
	 *  @param maxValue max value
	 *	@return true, if accepted
	 */
	public boolean setRange(Double minValue, Double maxValue)
	{
		m_rangeSet = true;
		m_minValue = minValue;
		m_maxValue = maxValue;
		return m_rangeSet;
	}	//	setRange

	/**
	 *	Set Range with min & max = parse US style number w/o Gouping
	 *  @param minValue min value
	 *  @param maxValue max value
	 *  @return true if accepted
	 */
	public boolean setRange(String minValue, String maxValue)
	{
		if (minValue == null || maxValue == null)
			return false;
		try
		{
			m_minValue = Double.valueOf(minValue);
			m_maxValue = Double.valueOf(maxValue);
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
		m_rangeSet = true;
		return m_rangeSet;
	}	//	setRange

	/**
	 *  Set and check DisplayType
	 *  @param displayType display type
	 */
	public void setDisplayType (int displayType)
	{
		m_displayType = displayType;
		if (!DisplayType.isNumeric(displayType))
			m_displayType = DisplayType.Number;
		m_format = DisplayType.getNumberFormat(displayType);
		m_text.setDocument (new MDocNumber(displayType, m_format, m_text, m_title));
	}   //  setDisplayType

	/**
	 *	Set ReadWrite
	 *  @param value value
	 */
	public void setReadWrite (boolean value)
	{
		if (m_text.isReadWrite() != value)
			m_text.setReadWrite(value);
		if (m_button.isReadWrite() != value)
			m_button.setReadWrite(value);
		//	Don't show button if not ReadWrite
		if (m_button.isVisible() != value)
			m_button.setVisible(value);
	}	//	setReadWrite

	/**
	 *	IsReadWrite
	 *  @return true if rw
	 */
	public boolean isReadWrite()
	{
		return m_text.isReadWrite();
	}	//	isReadWrite

	/**
	 *	Set Mandatory (and back bolor)
	 *  @param mandatory mandatory
	 */
	public void setMandatory (boolean mandatory)
	{
		m_text.setMandatory(mandatory);
	}	//	setMandatory

	/**
	 *	Is it mandatory
	 *  @return true if mandatory
	 */
	public boolean isMandatory()
	{
		return m_text.isMandatory();
	}	//	isMandatory

	/**
	 *	Set Background
	 *  @param color color
	 */
	public void setBackground(Color color)
	{
		m_text.setBackground(color);
	}	//	setBackground

	/**
	 *	Set Background
	 *  @param error error
	 */
	public void setBackground (boolean error)
	{
		m_text.setBackground(error);
	}	//	setBackground

	/**
	 *  Set Foreground
	 *  @param fg foreground
	 */
	public void setForeground(Color fg)
	{
		m_text.setForeground(fg);
	}   //  setForeground

	/**
	 *	Set Editor to value
	 *  @param value value
	 */
	public void setValue(Object value)
	{
		log.finest("Value=" + value);
		if (value == null)
			m_oldText = "";
		else
			m_oldText = m_format.format(value);
		//	only set when not updated here
		if (m_setting)
			return;
		m_text.setText (m_oldText);
		m_initialText = m_oldText;
		m_modified = false;
	}	//	setValue

	/**
	 *  Property Change Listener
	 *  @param evt event
	 */
	public void propertyChange (PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(org.compiere.model.MField.PROPERTY))
			setValue(evt.getNewValue());
	}   //  propertyChange

	/**
	 *	Return Editor value
	 *  @return value value (big decimal or integer)
	 */
	public Object getValue()
	{
		if (m_text == null || m_text.getText() == null || m_text.getText().length() == 0)
			return null;
		String value = m_text.getText();
		//	return 0 if text deleted
		if (value == null || value.length() == 0)
		{
			if (!m_modified)
				return null;
			if (m_displayType == DisplayType.Integer)
				return new Integer(0);
			return Env.ZERO;
		}
		if (value.equals(".") || value.equals(",") || value.equals("-"))
			value = "0";
		try
		{
			Number number = m_format.parse(value);
			value = number.toString();      //	converts it to US w/o thousands
			BigDecimal bd = new BigDecimal(value);
			if (m_displayType == DisplayType.Integer)
				return new Integer(bd.intValue());
			if (bd.signum() == 0)
				return bd;
			return bd.setScale(m_format.getMaximumFractionDigits(), BigDecimal.ROUND_HALF_UP);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Value=" + value, e);
		}
		if (m_displayType == DisplayType.Integer)
			return new Integer(0);
		return Env.ZERO;
	}	//	getValue

	/**
	 *  Return Display Value
	 *  @return value
	 */
	public String getDisplay()
	{
		return m_text.getText();
	}   //  getDisplay

	/**
	 * 	Get Title
	 *	@return title
	 */
	public String getTitle()
	{
		return m_title;
	}	//	getTitle

	/**
	 * 	Plus - add one.
	 * 	Also sets Value
	 *	@return new value
	 */
	public Object plus()
	{
		Object value = getValue();
		if (value == null)
		{
			if (m_displayType == DisplayType.Integer)
				value = new Integer(0);
			else
				value = Env.ZERO;
		}
		//	Add
		if (value instanceof BigDecimal)
			value = ((BigDecimal)value).add(Env.ONE);
		else
			value = new Integer(((Integer)value).intValue() + 1);
		//
		setValue(value);
		return value;
	}	//	plus
	
	/**
	 * 	Minus - subtract one, but not below minimum.
	 * 	Also sets Value
	 *	@param minimum minimum
	 *	@return new value
	 */
	public Object minus (int minimum)
	{
		Object value = getValue();
		if (value == null)
		{
			if (m_displayType == DisplayType.Integer)
				value = new Integer(minimum);
			else
				value = new BigDecimal(minimum);
			setValue(value);
			return value;
		}
		
		//	Subtract
		if (value instanceof BigDecimal)
		{
			BigDecimal bd = ((BigDecimal)value).subtract(Env.ONE);
			BigDecimal min = new BigDecimal(minimum);
			if (bd.compareTo(min) < 0)
				value = min;
			else
				value = bd;
		}
		else
		{
			int i = ((Integer)value).intValue();
			i--;
			if (i < minimum)
				i = minimum;
			value = new Integer(i);
		}
		//
		setValue(value);
		return value;
	}	//	minus
	
	/**************************************************************************
	 *	Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		log.config(e.getActionCommand());
		if (ValuePreference.NAME.equals(e.getActionCommand()))
		{
			if (MRole.getDefault().isShowPreference())
				ValuePreference.start (m_mField, getValue());
			return;
		}

		if (e.getSource() == m_button)
		{
			m_button.setEnabled(false);
			String str = startCalculator(this, m_text.getText(), m_format, m_displayType, m_title);
			m_text.setText(str);
			m_button.setEnabled(true);
			try
			{
				fireVetoableChange (m_columnName, m_oldText, getValue());
			}
			catch (PropertyVetoException pve)	{}
			m_text.requestFocus();
		}
	}	//	actionPerformed

	/**************************************************************************
	 *	Key Listener Interface
	 *  @param e event
	 */
	public void keyTyped(KeyEvent e)    {}
	public void keyPressed(KeyEvent e)  {}

	/**
	 *	Key Listener.
	 *		- Escape 		- Restore old Text
	 *		- firstChange	- signal change
	 *  @param e event
	 */
	public void keyReleased(KeyEvent e)
	{
		log.finest("Key=" + e.getKeyCode());
		//  ESC
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			m_text.setText(m_initialText);
		m_modified = true;
		m_setting = true;
		try
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER)	//	10
			{
				fireVetoableChange (m_columnName, m_oldText, getValue());
				fireActionPerformed();
			}
			else	//	indicate change
				fireVetoableChange (m_columnName, m_oldText, null);	
		}
		catch (PropertyVetoException pve)	{}
		m_setting = false;
	}	//	keyReleased

	/**
	 *	Focus Gained
	 *  @param e event
	 */
	public void focusGained (FocusEvent e)
	{
		if (m_text != null)
			m_text.selectAll();
	}	//	focusGained

	/**
	 *	Data Binding to MTable (via GridController.vetoableChange).
	 *  @param e event
	 */
	public void focusLost (FocusEvent e)
	{
		try
		{
			fireVetoableChange (m_columnName, m_initialText, getValue());
			fireActionPerformed();
		}
		catch (PropertyVetoException pve)	{}
	}   //  focusLost

	/**
	 *	Invalid Entry - Start Calculator
	 *  @param jc parent
	 *  @param value value
	 *  @param format format
	 *  @param displayType display type
	 *  @param title title
	 *  @return value
	 */
	public static String startCalculator(Container jc, String value,
		DecimalFormat format, int displayType, String title)
	{
		log.config("Value=" + value);
		BigDecimal startValue = new BigDecimal(0.0);
		try
		{
			if (value != null && value.length() > 0)
			{
				Number number = format.parse(value);
				startValue = new BigDecimal (number.toString());
			}
		}
		catch (ParseException pe)
		{
			log.info("InvalidEntry - " + pe.getMessage());
		}

		//	Find frame
		Frame frame = Env.getFrame(jc);
		//	Actual Call
		Calculator calc = new Calculator(frame, title,
			displayType, format, startValue);
		AEnv.showCenterWindow(frame, calc);
		BigDecimal result = calc.getNumber();
		log.config( "Result=" + result);
		//
		calc = null;
		if (result != null)
			return format.format(result);
		else
			return value;		//	original value
	}	//	startCalculator

	/**
	 *  Set Field/WindowNo for ValuePreference
	 *  @param mField field
	 */
	public void setField (MField mField)
	{
		m_mField = mField;
		/**
		if (m_mField != null
			&& MRole.getDefault().isShowPreference())
			ValuePreference.addMenu (this, popupMenu);
		**/
	}   //  setField

	
	/**************************************************************************
	 * 	Remove Action Listner
	 * 	@param l Action Listener
	 */
	public void removeActionListener(ActionListener l)
	{
		listenerList.remove(ActionListener.class, l);
	}	//	removeActionListener

	/**
	 * 	Add Action Listner
	 * 	@param l Action Listener
	 */
	public void addActionListener(ActionListener l)
	{
		listenerList.add(ActionListener.class, l);
	}	//	addActionListener

	/**
	 * 	Fire Action Event to listeners
	 */
	protected void fireActionPerformed()
	{
		int modifiers = 0;
		AWTEvent currentEvent = EventQueue.getCurrentEvent();
		if (currentEvent instanceof InputEvent)
			modifiers = ((InputEvent)currentEvent).getModifiers();
		else if (currentEvent instanceof ActionEvent)
			modifiers = ((ActionEvent)currentEvent).getModifiers();
		ActionEvent ae = new ActionEvent (this, ActionEvent.ACTION_PERFORMED,
			"VNumber", EventQueue.getMostRecentEventTime(), modifiers);

		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i]==ActionListener.class)
			{
				((ActionListener)listeners[i+1]).actionPerformed(ae);
			}
		}
	}	//	fireActionPerformed
	/**/
}	//	VNumber
