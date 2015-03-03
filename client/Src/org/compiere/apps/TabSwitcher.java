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
 * All parts are Copyright (C) 1999-2005 e-Evolution, S.C.  All Rights Reserved.
 * Contributor(s): Gunther Hoppe & Victor Perez, 21.08.2005 e-evolution
 *****************************************************************************/


package org.compiere.apps;

import java.awt.Component;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.*;

import javax.swing.AbstractButton;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;


import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import org.compiere.grid.GridController;
import org.compiere.grid.ed.VEditor;
import org.compiere.model.MField;
import org.compiere.swing.CPanel;

/**
 * 
 * @author Gunther Hoppe, 21.08.2005
 *
 */
public class TabSwitcher extends FocusAdapter implements ActionListener, ListSelectionListener{
	
	private APanel panel;
	private GridController gc;
	
	public TabSwitcher(GridController g, APanel p) {
	
		panel = p;
		gc = g;
	}

	public void valueChanged(ListSelectionEvent e) {
		System.out.println("ListSelectionEvent Objeto:" + e.getSource());
		System.out.println("Gridcontroller ListSelectionEvent:" + gc.getName());
		if(!e.getValueIsAdjusting()) {
		
			performSwitch();
		}
	};		

	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed Objeto:" + e.getSource());
		System.out.println("Gridcontroller actionPerformed:" + gc.getName());
		
		
		/*JComponent c = (JComponent)e.getSource(); 
		if(c instanceof org.compiere.grid.ed.VComboBox) 
		{					
					org.compiere.grid.ed.VComboBox l = ((org.compiere.grid.ed.VComboBox)c);
		              l.removeActionListener(this);
		              performSwitch();
					  l.addActionListener(this);
		}*/	
		if(!(e.getSource() instanceof JTextComponent)) {
			if(gc.getMTab().getRecord_ID() != -1)
				performSwitch();
		}
	}

	public void focusGained(FocusEvent e) {
		System.out.println("FocusGained Objeto:" + e.getSource());
		System.out.println("Gridcontroller FocusEvent Objeto:" + gc.getName());
		/*System.out.println(" e.isTemporary()" + e.isTemporary());*/
		performSwitch();
	}
	
	/*public void focusLost(FocusEvent e) {
		System.out.println("Focus Lost Objeto:" + e.getSource());		
		//panel.transferFocus();
	}*/
								

	private void performSwitch() {
	
		//System.out.println("performSwitch GridControler:" + gc.getName());
		//gc.transferFocus();
		panel.dispatchTabSwitch(gc);
	}
	
	public void addTabSwitchingSupport(JComponent c) {

		if(c instanceof JTable) {
			
			((JTable)c).getSelectionModel().addListSelectionListener(this);
			return;
		}
		else if(  //c instanceof org.compiere.grid.ed.VEditor ||  				  
				   c instanceof JTextComponent ||
				   //c instanceof ItemSelectable ||
				   c instanceof org.compiere.grid.ed.VCheckBox ||
				   //c instanceof org.compiere.grid.ed.VLookup ||
				   //c instanceof org.compiere.swing.CLabel ||
				   c instanceof AbstractButton) 
		{
						
					//System.out.println("Component encontrado VEditor : "+ c.toString());
					c.addFocusListener(this);
					//c.addKeyListener(new MovementAdapter());
					return;
		}
		else if(c instanceof org.compiere.grid.ed.VDate) 
		{
					//System.out.println("Component encontrado VLookup : "+ c.toString());
					org.compiere.grid.ed.VDate d = ((org.compiere.grid.ed.VDate)c);
					//d.addFocusListener(this);
					d.addActionListener(this);
					//d.addKeyListener(new MovementAdapter());
					return;
		}
		else if(c instanceof org.compiere.grid.ed.VLookup) 
		{
					//System.out.println("Component encontrado VLookup : "+ c.toString());
					org.compiere.grid.ed.VLookup l = ((org.compiere.grid.ed.VLookup)c);
					//l.addFocusListener(this);
					l.addActionListener(this);
					//l.addKeyListener(new MovementAdapter());
					return;
		}
		/*else if (c instanceof ItemSelectable || c instanceof JTextComponent)
		{
					System.out.println("Component encontrado  ItemSelectable : "+ c.toString());
					c.addFocusListener(this);
			return;	    
		}
		else
					System.out.println("Component no  encontrado:"+ c.toString());*/
	}
	
	class MovementAdapter extends KeyAdapter 
	{
         public void keyPressed(KeyEvent event) 
         {        	  	
             // look for tab keys
             if(event.getKeyCode() == KeyEvent.VK_TAB
             || event.getKeyCode() == KeyEvent.VK_ENTER) 
             {
            	 //System.out.println("key"+ event.getKeyCode());    
            	 //System.out.println("component"+ ((JComponent)event.getSource()).getName());               	
            	 ((JComponent)event.getSource()).transferFocus();
             }
             else
            	 System.out.println("key"+ event.getKeyCode()); 	 
         }
	}     
}
