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
import java.util.logging.*;

import javax.swing.*;

import org.compiere.apps.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Product Attribute Set Product/Instance Dialog Editor.
 * 	Called from VPAttribute.actionPerformed
 *
 *  @author Jorg Janke
 *  @version $Id: VPAttributeDialog.java,v 1.8 2006/10/09 15:29:34 SIGArg-01 Exp $
 */
public class CPEstadoDialog extends CDialog
	implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Estado de Cheques Recibidos Dialog
	 *	@param frame parent frame
	 * 	@param AD_Column_ID column
	 */
	public CPEstadoDialog (Frame frame, int WindowNo, String state)
	{
		super (frame, "Proximo estado cheques" , true);
		m_WindowNo = Env.createWindowNo (this);
		m_WindowNoParent = WindowNo;
		m_State = state;
		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "CPEstadoDialog" + ex);
		}
		//	Dynamic Init
		if (!initStates ())
		{
			dispose();
			return;
		}
		AEnv.showCenterWindow(frame, this);
	}	//	VPAttributeDialog

	private int						m_WindowNo;
	private int						m_WindowNoParent;
    private String					m_State;
    private String					m_newState;
    private CLogger					log = CLogger.getCLogger(getClass());
	/** Row Counter					*/
	private int                     m_row = 0;       
	JPopupMenu 					popupMenu = new JPopupMenu();
    private boolean changed;

	//COMPONENTES GRAFICOS
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel centerPanel = new CPanel();
	private ALayout centerLayout = new ALayout(15,8, true);
	private ConfirmPanel confirmPanel = new ConfirmPanel (true);
    private CComboBox proxEstados;
        
        
	private void jbInit () throws Exception
	{
		this.getContentPane().setLayout(mainLayout);
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
		this.getContentPane().add(confirmPanel, BorderLayout.SOUTH);
		centerPanel.setLayout(centerLayout);
		//
		confirmPanel.addActionListener(this);
	}	//	jbInit

	/**
	 *	Dyanmic Init.
	 *  @return true if initialized
	 */
     private boolean initStates ()
	 {
    	 	changed = false;
    	 	CLabel label1 = new CLabel("Estado actual:");
    	 	CLabel actual = new CLabel(CPEstado.getNameState(m_State.charAt(0)));
    	 	actual.setAlignmentX(0);
    	 	label1.setLabelFor(actual);
    	 	
    	 	//Se inicializan el combo box y el panel
            proxEstados = new CComboBox();
            proxEstados.setMandatory(true);
        	       		
        	if (m_State.equals("E"))
       		{
        		proxEstados.addItem(new ValueNamePair("P",CPEstado.getNameState('P')));
                proxEstados.addItem(new ValueNamePair("D",CPEstado.getNameState('D')));
			}
                
                // Agregado para manejar la impresión de cheques
                // José Fantasia
                // Zynnia
                        	if (m_State.equals("I"))
       		{
        		proxEstados.addItem(new ValueNamePair("P",CPEstado.getNameState('P')));
                proxEstados.addItem(new ValueNamePair("D",CPEstado.getNameState('D')));
			}
                                
			if (m_State.equals("P"))
			{
				proxEstados.addItem(new ValueNamePair("D",CPEstado.getNameState('D')));
			}
			
			CLabel label2 = new CLabel ("Proximo estado:"); 
            label2.setLabelFor (proxEstados);
            
            //se ubica el combo box y el label en el panel
            centerPanel.add(label1, new ALayoutConstraint(m_row++,0));
            centerPanel.add(actual, null);
            centerPanel.add(label2, new ALayoutConstraint(m_row++,0));
            centerPanel.add(proxEstados, null);
            
            //agregarComponentesEstados();   
            return true; 
	 }	//	initStates
        
	/**
	 *	dispose
	 */
	public void dispose()
	{
		removeAll();
		Env.clearWinContext(m_WindowNo);
		super.dispose();
	}	//	dispose
	/**
	 *	ActionListener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{   
            ValueNamePair pp = (ValueNamePair)proxEstados.getSelectedItem();
            m_newState = pp.getValue();
            changed = true;
            dispose();
        }	//	OK
        else	//	CANCEL
        	if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
        		dispose();
            else
            	log.log(Level.SEVERE, "not found - " + e);
	}	//	actionPerformed

        public int getWindowNoParent()
        {
            return m_WindowNoParent;
        }


        /** BISion - 09/01/2009 - Santiago Iba�ez
         * Retorna true en caso de haber cambiado el estado
         * @return
         */
        public boolean changed(){
            return changed;
        }
        
        public String getState(){
            return m_newState;
        }
        
} //	CPEStadoDialog

/*****************************************************************************
 *	Mouse Listener for Popup Menu
 */
final class CPEstadoDialog_mouseAdapter extends java.awt.event.MouseAdapter
{
	/**
	 *	Constructor
	 *  @param adaptee adaptee
	 */
	CPEstadoDialog_mouseAdapter(CPEstadoDialog adaptee)
	{
		this.adaptee = adaptee;
	}	//	VPAttributeDialog_mouseAdapter

	private CPEstadoDialog adaptee;

	/**
	 *	Mouse Listener
	 *  @param e MouseEvent
	 */
	public void mouseClicked(MouseEvent e)
	{
	//	System.out.println("mouseClicked " + e.getID() + " " + e.getSource().getClass().toString());
		//	popup menu
		if (SwingUtilities.isRightMouseButton(e))
			adaptee.popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
	}	//	mouse Clicked

}	//	VPAttributeDialog_mouseAdapter
