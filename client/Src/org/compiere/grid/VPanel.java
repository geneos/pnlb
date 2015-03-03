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
package org.compiere.grid;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
//
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Single Row Panel.
 *  Called from GridController
 *  <pre>
 *	Structure
 *		this (CPanel - Group Bag Layout)
 *			group
 *			label - field
 *
 *  Spacing:
 *  -------------------
 *  Total Top = 10+2
 *  Total Right = 0+12
 *  Total Left = 0+12
 *  Total Bottom = 3+9
 *  -------------------
 *       2
 *  12 Label 0+5 Field 0
 *       3+2=5
 *  12 Label 0+5 Field 0
 *       3
 *
 *  </pre>
 *  @author 	Jorg Janke
 *  @version 	$Id: VPanel.java,v 1.19 2006/02/13 02:41:05 jjanke Exp $
 */
public final class VPanel extends CPanel
{
	/**
	 *	Constructor
	 */
	public VPanel()
	{
		super(new GridBagLayout());
		setName("VPanel");
		setBorder(null);

		//	Set initial values of constraint
		m_gbc.anchor = GridBagConstraints.NORTHWEST;
		m_gbc.gridy = 0;			//	line
		m_gbc.gridx = 0;
		m_gbc.gridheight = 1;
		m_gbc.gridwidth = 1;
		m_gbc.insets = m_zeroInset;
		m_gbc.fill = GridBagConstraints.HORIZONTAL;
		m_gbc.weightx = 0;
		m_gbc.weighty = 0;
		m_gbc.ipadx = 0;
		m_gbc.ipady = 0;
	}	//	VPanel
	
	/** GridBag Constraint      */
	private GridBagConstraints	m_gbc = new GridBagConstraints();

	/** Orientation             */
	private final boolean       m_leftToRight = Language.getLoginLanguage().isLeftToRight();
	/** Label Inset             */
	private final Insets 		m_labelInset =
		m_leftToRight ? new Insets(2,12,3,0) : new Insets(2,5,3,0);     // 	top,left,bottom,right
	/** Field Inset             */
	private final Insets 		m_fieldInset =
		m_leftToRight ? new Insets(2,5,3,0)  : new Insets(2,12,3,0);	// 	top,left,bottom,right
	/** Zero Inset              */
	private final Insets 		m_zeroInset = new Insets(0,0,0,0);
	//
	private int 				m_line = 0;
	private boolean 			m_hGapAdded = false;					//	only once
	/** Previous Field Group Header     */
	private String              m_oldFieldGroup = null;
        
        // begin e-evolution 08/02/2005        
        //private CPanel m_main = new CPanel(org.compiere.plaf.CompiereColor.getDefaultBackground());      
        private JTabbedPane m_tabinclude = new JTabbedPane(VTabbedPane.TOP);
        //private CPanel m_currenttab = new CPanel(org.compiere.plaf.CompiereColor.getDefaultBackground()); 
        private java.util.Hashtable m_tablist = new java.util.Hashtable();
        //private int m_mainline = 0;
        //private GridBagConstraints	m_gbc_main = new GridBagConstraints();
        // end e-evolution 08/02/2005

	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (VPanel.class);
	

	/**
	 * 	Set Field Mnemonic
	 *	@param mField field
	 */
	public void setMnemonic (MField mField)
	{
		if (mField.isCreateMnemonic())
			return;
		String text = mField.getHeader();
		int pos = text.indexOf("&");
		if (pos != -1 && text.length() > pos)	//	We have a nemonic - creates Ctrl_Shift_
		{
			char mnemonic = text.toUpperCase().charAt(pos+1);
			if (mnemonic != ' ')
			{
				if (!m_mnemonics.contains(mnemonic))
				{
					mField.setMnemonic(mnemonic);
					m_mnemonics.add(mnemonic);
				}
				else
					log.warning(mField.getColumnName() 
						+ " - Conflict - Already exists: " + mnemonic + " (" + text + ")");
			}
		}
	}	//	setMnemonic
	
	/**
	 *	Add Field and Label to Panel
	 *  @param editor editor
	 *  @param mField field model
	 */
	public void addField (VEditor editor, MField mField)
	{
		CLabel label = VEditorFactory.getLabel(mField); 
		if (label == null && editor == null)
			return;

		boolean sameLine = mField.isSameLine();
		if (addGroup(mField.getFieldGroup()))               		//	sets top
			sameLine = false;

		if (sameLine)    							//	Set line #
			m_gbc.gridy = m_line-1;
		else
			m_gbc.gridy = m_line++;
               // begin e-evolution 08/02/2005 
               if(mField.getIncluded_Tab_ID() != 0 )
               {
                     m_gbc.gridx = 0;
                     m_gbc.gridy = m_line++;
                     m_gbc.gridwidth = 4;
                     //m_gbc.insets = new Insets(10,10,10,10);                     
                     String m_name =  String.valueOf(mField.getIncluded_Tab_ID());
                     //System.out.println("-----------mField.getIncluded_Tab_ID()" + m_name);
                     m_tabinclude.setName(m_name);
                     //m_tabinclude.set                   
                     //m_tabinclude.setBackground(org.compiere.plaf.CompiereColor.getDefaultBackground());
                     m_tabinclude.setPreferredSize(new Dimension(620, 250));                     
                     this.add(m_tabinclude, m_gbc);
                     //return;
               }
                // end e-evolution 08/02/2005
                                           
		//	*** The Label ***
		if (label != null)
		{
			m_gbc.gridwidth = 1;
			m_gbc.insets = m_labelInset;
			m_gbc.fill = GridBagConstraints.HORIZONTAL;	//	required for right justified
			//	Set column #
			if (m_leftToRight)
				m_gbc.gridx = sameLine ? 2 : 0;
			else
				m_gbc.gridx = sameLine | mField.isLongField() ? 3 : 1;
			//	Weight factor for Label
			m_gbc.weightx = 0;
			//
			if (mField.isCreateMnemonic())
				setMnemonic(label, mField.getMnemonic());
			//  Add Label
			this.add(label, m_gbc);
		}

		//	*** The Field ***
		if (editor != null)
		{
			Component field = (Component)editor;
			//	Default Width
			m_gbc.gridwidth = mField.isLongField() ? 3 : 1;
			m_gbc.insets = m_fieldInset;
		//	m_gbc.fill = GridBagConstraints.NONE;
			m_gbc.fill = GridBagConstraints.HORIZONTAL;
			//	Set column #
			if (m_leftToRight)
				m_gbc.gridx = sameLine ? 3 : 1;
			else
				m_gbc.gridx = sameLine ? 2 : 0;
			//	Weight factor for Fields
			m_gbc.weightx = 1;
			//	Add Field
			this.add(field, m_gbc);
			//	Link Label to Field
			if (label != null)
				label.setLabelFor(field);
			else if (mField.isCreateMnemonic())
				setMnemonic(editor, mField.getMnemonic());
		}
	}	//	addField

	/**
	 *	Add Group
	 *  @param fieldGroup field group
	 *  @return true if group added
	 */
	private boolean addGroup(String fieldGroup)
	{
		//	First time - add top
		if (m_oldFieldGroup == null)
		{
			addTop();
			m_oldFieldGroup = "";
		}

		if (fieldGroup == null || fieldGroup.length() == 0 || fieldGroup.equals(m_oldFieldGroup))
			return false;
                
                //begin vpj-cd e-evolution 08/02/2005                                 
                if (m_tablist.get(fieldGroup) == null)
                {   
                    int IsTab = 1;
                    if (Env.getContext(Env.getCtx(),"#AD_Language").equals("en_US"))
                    {
                        IsTab = DB.getSQLValue(null,"SELECT CASE WHEN IsTab IS NULL OR IsTab = 'N' THEN 1  ELSE 0 END AS IsTab  FROM AD_FieldGroup  fg WHERE fg.Name= ? ", fieldGroup);                   
                    }
                    else
                    {
                        IsTab = DB.getSQLValue(null,"SELECT CASE WHEN IsTab IS NULL OR IsTab = 'N' THEN 1  ELSE 0 END AS IsTab  FROM AD_FieldGroup  fg INNER JOIN AD_FieldGroup_Trl fgtrl ON ( fg.AD_FieldGroup_ID =  fgtrl.AD_FieldGroup_ID) WHERE fgtrl.Name= ? ", fieldGroup);               
                    }    
                    
                    if (IsTab == 0)
                    {    
                      CPanel m_tab = (CPanel)m_tablist.get(fieldGroup);                                            
                      m_tab = new CPanel(org.compiere.plaf.CompiereColor.getDefaultBackground());
                      m_tab.setLayout(new GridBagLayout());
                      m_tab.setName(fieldGroup);                      
                      this.add(m_tab);
                      //m_currenttab.add(m_tab); 
                      m_tablist.put(fieldGroup, m_tab);
                      //m_currenttab = m_tab;                                      
                      return false;
                    }
                    /*else
                    {                       
                      m_currenttab = m_main;                      
                    }*/    
                        
                }
                else
                {                     
                      return false;
                }    
                //end vpj-cd e-evolution 08/02/2005                   
		m_oldFieldGroup = fieldGroup;

		CPanel group = new CPanel();
		group.setBorder(new VLine(fieldGroup));
		group.add(Box.createVerticalStrut(VLine.SPACE));
		m_gbc.gridx = 0;
		m_gbc.gridy = m_line++;
		m_gbc.gridwidth = 4;
		this.add(group, m_gbc);
		//	reset
		m_gbc.gridwidth = 1;
		return true;
	}	//	addGroup

	/**
	 *	Add Top (10) and right (12) gap
	 */
	private void addTop()
	{
		//	Top Gap
		m_gbc.gridy = m_line++;
		this.add(Box.createVerticalStrut(10), m_gbc);    	//	top gap
		//	Right gap
		m_gbc.gridx = 4;									//	5th column
		m_gbc.gridwidth = 1;
		m_gbc.weightx = 0;
		m_gbc.insets = m_zeroInset;
		m_gbc.fill = GridBagConstraints.NONE;
		this.add(Box.createHorizontalStrut(12), m_gbc);
	}	//	addTop

	/**
	 *	Add End (9) of Form
	 */
	public void addEnd()
	{
		m_gbc.gridx = 0;
		m_gbc.gridy = m_line;
		m_gbc.gridwidth = 1;
		m_gbc.insets = m_zeroInset;
		m_gbc.fill = GridBagConstraints.HORIZONTAL;
		m_gbc.weightx = 0;
		//
		this.add(Box.createVerticalStrut(9), m_gbc);		//	botton gap
	}	//	addEnd

	/**
	 * 	Set Mnemonic for Label CTRL_SHIFT_x
	 *	@param label label
	 *	@param predefinedMnemonic predefined Mnemonic
	 */
	private void setMnemonic (CLabel label, char predefinedMnemonic)
	{
		String text = label.getText();
		int pos = text.indexOf("&");
		if (pos != -1 && predefinedMnemonic != 0)
		{
			text = text.substring(0, pos) + text.substring(pos+1);
			label.setText(text);
			label.setSavedMnemonic(predefinedMnemonic);
			m_fields.add(label);
			log.finest(predefinedMnemonic + " - " + label.getName());
		}
		else
		{
			char mnemonic = getMnemonic(text, label);
			if (mnemonic != 0)
				label.setSavedMnemonic(mnemonic);
		//	label.setDisplayedMnemonic(mnemonic);
		}
	}	//	setMnemonic
	
	/**
	 * 	Set Mnemonic for Check Box or Button
	 *	@param editor check box or button - other ignored
	 *	@param predefinedMnemonic predefined Mnemonic
	 */
	private void setMnemonic (VEditor editor, char predefinedMnemonic)
	{
		if (editor instanceof VCheckBox)
		{
			VCheckBox cb = (VCheckBox)editor;
			String text = cb.getText();
			int pos = text.indexOf("&");
			if (pos != -1 && predefinedMnemonic != 0)
			{
				text = text.substring(0, pos) + text.substring(pos+1);
				cb.setText(text);
				cb.setSavedMnemonic(predefinedMnemonic);
				m_fields.add(cb);
				log.finest(predefinedMnemonic + " - " + cb.getName());
			}
			else
			{
				char mnemonic = getMnemonic(text, cb);
				if (mnemonic != 0)
					cb.setSavedMnemonic(mnemonic);
			//	cb.setMnemonic(mnemonic);
			}
		}
		//	Button
		else if (editor instanceof VButton)
		{
			VButton b = (VButton)editor;
			String text = b.getText();
			int pos = text.indexOf("&");
			if (pos != -1 && predefinedMnemonic != 0)
			{
				text = text.substring(0, pos) + text.substring(pos+1);
				b.setText(text);
				b.setSavedMnemonic(predefinedMnemonic);
				m_fields.add(b);
				log.finest(predefinedMnemonic + " - " + b.getName());
			}
			else if (b.getColumnName().equals("DocAction"))
			{
				b.getInputMap(WHEN_IN_FOCUSED_WINDOW)
					.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.SHIFT_MASK, false), "pressed");
				b.getInputMap(WHEN_IN_FOCUSED_WINDOW)
					.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.SHIFT_MASK, true), "released");
			//	Util.printActionInputMap(b);
			}
			else if (b.getColumnName().equals("Posted"))
			{
				b.getInputMap(WHEN_IN_FOCUSED_WINDOW)
					.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, Event.SHIFT_MASK, false), "pressed");
				b.getInputMap(WHEN_IN_FOCUSED_WINDOW)
					.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, Event.SHIFT_MASK, true), "released");
			//	Util.printActionInputMap(b);
			}
			else
			{
				char mnemonic = getMnemonic(text, b);
				if (mnemonic != 0)
					b.setSavedMnemonic(mnemonic);
			}
		}
	}	//	setMnemonic

	/**
	 * 	Get Mnemonic from text
	 *	@param text text
	 *	@return Mnemonic or 0 if not unique
	 */
	private char getMnemonic (String text, Component source)
	{
		if (text == null || text.length() == 0)
			return 0;
		String oText = text;
		text = text.trim().toUpperCase();
		char mnemonic = text.charAt(0);
		if (m_mnemonics.contains(mnemonic))
		{
			mnemonic = 0;
			//	Beginning new word
			int index = text.indexOf(' ');
			while (index != -1 && text.length() > index)
			{
				char c = text.charAt(index+1);
				if (Character.isLetterOrDigit(c) && !m_mnemonics.contains(c))
				{
					mnemonic = c;
					break;
				}
				index = text.indexOf(' ', index+1);
			}
			//	Any character
			if (mnemonic == 0)
			{
				for (int i = 1; i < text.length(); i++)
				{
					char c = text.charAt(i);
					if (Character.isLetterOrDigit(c) && !m_mnemonics.contains(c))
					{
						mnemonic = c;
						break;
					}
				}
			}
			//	Nothing found
			if (mnemonic == 0)
			{
				log.finest("None for: " + oText);
				return 0;	//	 if first char would be returned, the first occurance is invalid.
			}
		}
		m_mnemonics.add(mnemonic);
		m_fields.add(source);
		log.finest(mnemonic + " - " + source.getName());
		return mnemonic;
	}	//	getMnemonic
	
	/** Used Mnemonics		*/
	private ArrayList<Character> m_mnemonics = new ArrayList<Character>(30);
	/** Mnemonic Fields		*/
	private ArrayList<Component> m_fields = new ArrayList<Component>(30);
	
	/**
	 * 	Set Window level Mnemonics
	 *	@param set true if set otherwise unregiser
	 */
	public void setMnemonics (boolean set)
	{
		int size = m_fields.size();
		for (int i = 0; i < size; i++)
		{
			Component c = m_fields.get(i);
			if (c instanceof CLabel)
			{
				CLabel l = (CLabel)c;
				if (set)
					l.setDisplayedMnemonic(l.getSavedMnemonic());
				else
					l.setDisplayedMnemonic(0);
			}
			else if (c instanceof VCheckBox)
			{
				VCheckBox cb = (VCheckBox)c;
				if (set)
					cb.setMnemonic(cb.getSavedMnemonic());
				else
					cb.setMnemonic(0);
			}
			else if (c instanceof VButton)
			{
				VButton b = (VButton)c;
				if (set)
					b.setMnemonic(b.getSavedMnemonic());
				else
					b.setMnemonic(0);
			}
		}
	}	//	setMnemonics
	
	/**************************************************************************
	 *  Set Background to AD_Color_ID (nop)
	 *  @param AD_Color_ID Color
	 */
	public void setBackground (int AD_Color_ID)
	{
	}   //  setBackground
	
}	//	VPanel
