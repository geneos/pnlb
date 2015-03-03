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
package org.compiere.pos;

import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import org.compiere.grid.ed.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	POS Checkout Sub Panel
 *	
 *  @author Jorg Janke
 *  @version $Id: SubCheckout.java,v 1.3 2005/03/11 20:28:22 jjanke Exp $
 */
public class SubCheckout extends PosSubPanel implements ActionListener
{
	/**
	 * 	Constructor
	 *	@param posPanel POS Panel
	 */
	public SubCheckout (PosPanel posPanel)
	{
		super (posPanel);
	}	//	PosSubCheckout
	
	private CButton f_register = null;
	private CButton f_summary = null;
	private CButton f_process = null;
	private CButton f_print = null;

	private CLabel f_lcreditCardNumber = null;
	private CTextField f_creditCardNumber = null;
	private CLabel f_lcreditCardExp = null;
	private CTextField f_creditCardExp = null;
	private CLabel f_lcreditCardVV = null;
	private CTextField f_creditCardVV = null;
	private CButton f_cashPayment = null;

	private CLabel f_lcashGiven = null;
	private VNumber f_cashGiven = null;
	private CLabel f_lcashReturn = null;
	private VNumber f_cashReturn = null;
	private CButton f_creditPayment = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(SubCheckout.class);
	
	/**
	 * 	Initialize
	 */
	public void init()
	{
		//	Title
		TitledBorder border = new TitledBorder(Msg.getMsg(Env.getCtx(), "Checkout"));
		setBorder(border);
		
		//	Content
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = INSETS2;
		
		//	--	0
		gbc.gridx = 0;
		f_register = createButtonAction("Register", null);
		gbc.gridy = 0;
		add (f_register, gbc);
		//
		f_summary = createButtonAction("Summary", null);
		gbc.gridy = 1;
		add (f_summary, gbc);
		//
		f_process = createButtonAction("Process", null);
		gbc.gridy = 2;
		add (f_process, gbc);
		//
		f_print = createButtonAction("Print", null);
		gbc.gridy = 3;
		add (f_print, gbc);

		//	--	1 -- Cash 
		gbc.gridx = 1;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = .1;
		CPanel cash = new CPanel(new GridBagLayout());
		cash.setBorder(new TitledBorder(Msg.getMsg(Env.getCtx(), "Cash")));
		gbc.gridy = 0;
		add (cash, gbc);
		GridBagConstraints gbc0 = new GridBagConstraints();
		gbc0.insets = INSETS2;
		gbc0.anchor = GridBagConstraints.WEST;
		//
		f_lcashGiven = new CLabel(Msg.getMsg(Env.getCtx(),"CashGiven"));
		cash.add (f_lcashGiven, gbc0);
		f_cashGiven = new VNumber("CashGiven", false, false, true, DisplayType.Amount, 
			Msg.translate(Env.getCtx(), "CashGiven"));
		f_cashGiven.setColumns(10, 25);
		cash.add (f_cashGiven, gbc0);
		f_cashGiven.setValue(Env.ZERO);
		//
		f_lcashReturn = new CLabel(Msg.getMsg(Env.getCtx(),"CashReturn"));
		cash.add (f_lcashReturn, gbc0);
		f_cashReturn = new VNumber("CashReturn", false, true, false, DisplayType.Amount, 
			"CashReturn");
		f_cashReturn.setColumns(10, 25);
		cash.add (f_cashReturn, gbc0);
		f_cashReturn.setValue(Env.ZERO);
		//
		f_cashPayment = createButtonAction("Payment", null);
		f_cashPayment.setActionCommand("Cash");
		gbc0.anchor = GridBagConstraints.EAST;
		gbc0.weightx = 0.1;
		cash.add (f_cashPayment, gbc0);
		
		
		//	--	1 -- Creditcard 
		CPanel creditcard = new CPanel(new GridBagLayout());
		creditcard.setBorder(new TitledBorder(Msg.translate(Env.getCtx(), "CreditCardType")));
		gbc.gridy = 2;
		add (creditcard, gbc);
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.insets = INSETS2;
		gbc1.anchor = GridBagConstraints.WEST;
		
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		f_lcreditCardNumber = new CLabel(Msg.translate(Env.getCtx(), "CreditCardNumber"));
		creditcard.add (f_lcreditCardNumber, gbc1);
		gbc1.gridy = 1;
		f_creditCardNumber = new CTextField(18); 
		creditcard.add (f_creditCardNumber, gbc1);
		gbc1.gridx = 1;
		gbc1.gridy = 0;
		f_lcreditCardExp = new CLabel(Msg.translate(Env.getCtx(),"CreditCardExp"));
		creditcard.add (f_lcreditCardExp, gbc1);
		gbc1.gridy = 1;
		f_creditCardExp = new CTextField(5); 
		creditcard.add (f_creditCardExp, gbc1);
		gbc1.gridx = 2;
		gbc1.gridy = 0;
		f_lcreditCardVV = new CLabel(Msg.translate(Env.getCtx(), "CreditCardVV"));
		creditcard.add (f_lcreditCardVV, gbc1);
		gbc1.gridy = 1;
		f_creditCardVV = new CTextField(5); 
		creditcard.add (f_creditCardVV, gbc1);
		//
		gbc1.gridx = 3;
		gbc1.gridy = 0;
		gbc1.gridheight = 2;
		f_creditPayment = createButtonAction("Payment", null);
		f_creditPayment.setActionCommand("CreditCard");
		gbc1.anchor = GridBagConstraints.EAST;
		gbc1.weightx = 0.1;
		creditcard.add (f_creditPayment, gbc1);
		
	}	//	init

	/**
	 * 	Get Panel Position
	 */
	public GridBagConstraints getGridBagConstraints()
	{
		GridBagConstraints gbc = super.getGridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		return gbc;
	}	//	getGridBagConstraints
	
	/**
	 * 	Dispose - Free Resources
	 */
	public void dispose()
	{
		super.dispose();
	}	//	dispose


	/**
	 * 	Action Listener
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		String action = e.getActionCommand();
		if (action == null || action.length() == 0)
			return;
		log.info( "PosSubCheckout - actionPerformed: " + action);
		//	Register
		//	Summary
		//	Print
		//	Cash (Payment)
		//	CreditCard (Payment)
	}	//	actionPerformed
	
}	//	PosSubCheckout
