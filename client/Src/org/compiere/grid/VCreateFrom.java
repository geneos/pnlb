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
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.math.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.compiere.apps.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  CreateFrom (Called from GridController.startProcess)
 *
 *  @author  Jorg Janke
 *  @version $Id: VCreateFrom.java,v 1.40 2005/12/27 06:18:36 jjanke Exp $
 */
public abstract class VCreateFrom extends CDialog
	implements ActionListener, TableModelListener
{
	/**
	 *  Factory - called from APanel
	 *  @param  mTab        Model Tab for the trx
	 *  @return JDialog
	 */
	public static VCreateFrom create (MTab mTab)
	{
		//	dynamic init preparation
		int AD_Table_ID = Env.getContextAsInt(Env.getCtx(), mTab.getWindowNo(), "BaseTable_ID");

		VCreateFrom retValue = null;
		if (mTab.getTableName().equals("C_BankStatement"))		//  C_BankStatement
			retValue = new VCreateFromStatement (mTab);
		else if (mTab.getTableName().equals("C_Invoice"))		//  C_Invoice
			retValue = new VCreateFromInvoice (mTab);
		else if (mTab.getTableName().equals("M_InOut"))			//  M_InOut
			retValue = new VCreateFromShipment (mTab);
		else if (mTab.getTableName().equals("C_PaySelection"))	//	C_PaySelection
			return null;	//	ignore - will call process C_PaySelection_CreateFrom
		else if (mTab.getTableName().equals("C_CONCILIACIONBANCARIA"))	//	C_ConciliacionBancaria
			retValue = new VCreateFromConciliacion (mTab);
		else if (mTab.getTableName().equals("C_Payment"))		//	C_Payment
			retValue = new VCreateFromPayment (mTab);
		else    //  Not supported CreateFrom
		{
			log.info("Unsupported AD_Table_ID=" + AD_Table_ID);
			return null;
		}
		return retValue;
	}   //  create

	
	/**************************************************************************
	 *  Protected super class Constructor
	 *  @param mTab MTab
	 */
	VCreateFrom (MTab mTab)
	{
		super(Env.getWindow(mTab.getWindowNo()), true);
		log.info(mTab.toString());
		p_WindowNo = mTab.getWindowNo();
		p_mTab = mTab;
		isSoTrx = Env.getContext(Env.getCtx(), p_WindowNo, "IsSOTrx").equals("Y"); 
		
		try
		{
			if (!dynInit())
				return;
			jbInit();
			confirmPanel.addActionListener(this);
			//  Set status
			statusBar.setStatusDB("");
			tableChanged(null);
			p_initOK = true;
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
			p_initOK = false;
		}

		//AEnv.positionCenterScreen(this);
		Dimension d = new Dimension(900,480);
		setSize(d);
	}   //  VCreateFrom

	/** Window No               */
	protected int               p_WindowNo;
	/** Model Tab               */
	protected MTab         		p_mTab;
	/** Is Sales Transaction               */
	protected boolean 			isSoTrx = false; 

	private boolean             p_initOK = false;

	/** Loaded Order            */
	protected MOrder 			p_order = null;
	/**	Logger			*/
	protected static CLogger log = CLogger.getCLogger(VCreateFrom.class);

	//
	private CPanel parameterPanel = new CPanel();
	protected CPanel parameterBankPanel = new CPanel();
	private BorderLayout parameterLayout = new BorderLayout();
	private JLabel bankAccountLabel = new JLabel();
	protected CPanel parameterStdPanel = new CPanel();
	private JLabel bPartnerLabel = new JLabel();
	protected VLookup bankAccountField;
	private GridBagLayout parameterStdLayout = new GridBagLayout();
	private GridBagLayout parameterBankLayout = new GridBagLayout();
	protected VLookup bPartnerField;
	private JLabel orderLabel = new JLabel();
	protected JComboBox orderField = new JComboBox();
	protected JLabel invoiceLabel = new JLabel();
	protected JComboBox invoiceField = new JComboBox();
	protected JLabel shipmentLabel = new JLabel();
	protected JComboBox shipmentField = new JComboBox();
	private JScrollPane dataPane = new JScrollPane();
	private CPanel southPanel = new CPanel();
	private BorderLayout southLayout = new BorderLayout();
	protected ConfirmPanel confirmPanel = new ConfirmPanel(true);
	protected StatusBar statusBar = new StatusBar();
	protected MiniTable dataTable = new MiniTable();
	protected JLabel locatorLabel = new JLabel();
	protected VLocator locatorField = new VLocator();

	/**
	 *  Static Init.
	 *  <pre>
	 *  parameterPanel
	 *      parameterBankPanel
	 *      parameterStdPanel
	 *          bPartner/order/invoice/shopment/licator Label/Field
	 *  dataPane
	 *  southPanel
	 *      confirmPanel
	 *      statusBar
	 *  </pre>
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		parameterPanel.setLayout(parameterLayout);
		parameterStdPanel.setLayout(parameterStdLayout);
		parameterBankPanel.setLayout(parameterBankLayout);
		//
		bankAccountLabel.setText(Msg.translate(Env.getCtx(), "C_BankAccount_ID"));
		bPartnerLabel.setText(Msg.getElement(Env.getCtx(), "C_BPartner_ID"));
		
		/**
		 * Modificado por @bision Daniel Gini
		 */
		orderLabel.setText(Msg.getElement(Env.getCtx(), "C_Order_ID", isSoTrx));
		invoiceLabel.setText(Msg.getElement(Env.getCtx(), "C_Invoice_ID", isSoTrx));
		shipmentLabel.setText(Msg.getElement(Env.getCtx(), "M_InOut_ID", isSoTrx));
		/**	 */
		
		locatorLabel.setText(Msg.translate(Env.getCtx(), "M_Locator_ID"));
		//
		this.getContentPane().add(parameterPanel, BorderLayout.NORTH);
		parameterPanel.add(parameterBankPanel, BorderLayout.NORTH);
		parameterBankPanel.add(bankAccountLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		if (bankAccountField != null)
			parameterBankPanel.add(bankAccountField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(parameterStdPanel, BorderLayout.CENTER);
		parameterStdPanel.add(bPartnerLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		if (bPartnerField != null)
			parameterStdPanel.add(bPartnerField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		
		// Daniel GINI - Se crean facturas a partir de ordenes, solo en compras - 17/06/2009
		// REQ-048 Modificaci�n
		if (!isSoTrx)
		{
			parameterStdPanel.add(orderLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterStdPanel.add(orderField,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
			parameterStdPanel.add(shipmentLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterStdPanel.add(shipmentField,  new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		}
		else
		{
			parameterStdPanel.add(shipmentLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterStdPanel.add(shipmentField,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		}

		parameterStdPanel.add(invoiceLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterStdPanel.add(invoiceField,  new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		parameterStdPanel.add(locatorLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterStdPanel.add(locatorField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		this.getContentPane().add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(dataTable, null);
		//
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(southLayout);
		southPanel.add(confirmPanel, BorderLayout.CENTER);
		southPanel.add(statusBar, BorderLayout.SOUTH);
	}   //  jbInit

	/**
	 *	Init OK to be able to make changes?
	 *  @return on if initialized
	 */
	public boolean isInitOK()
	{
		return p_initOK;
	}	//	isInitOK

	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	abstract boolean dynInit() throws Exception;

	/**
	 *  Init Business Partner Details
	 *  @param C_BPartner_ID BPartner
	 */
	abstract void initBPDetails(int C_BPartner_ID);

	/**
	 *  Add Info
	 */
	abstract void info();

	/**
	 *  Save & Insert Data
	 *  @return true if saved
	 */
	abstract boolean save();

	/*************************************************************************/

	/**
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		log.config("Action=" + e.getActionCommand());
	//	if (m_action)
	//		return;
	//	m_action = true;

		//  OK - Save
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			if (save())
				dispose();
		}
		//  Cancel
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			dispose();
		}
	//	m_action = false;
	}   //  actionPerformed

	/**
	 *  Table Model Listener
	 *  @param e event
	 */
	public void tableChanged (TableModelEvent e)
	{
		int type = -1;
		if (e != null)
		{
			type = e.getType();
			if (type != TableModelEvent.UPDATE)
				return;
		}
		log.config("Type=" + type);
		info();
	}   //  tableChanged

	
	/**************************************************************************
	 *  Load BPartner Field
	 *  @param forInvoice true if Invoices are to be created, false receipts
	 *  @throws Exception if Lookups cannot be initialized
	 */
	protected void initBPartner (boolean forInvoice) throws Exception
	{
		//  load BPartner
		int AD_Column_ID = 3499;        //  C_Invoice.C_BPartner_ID
		MLookup lookup = MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		bPartnerField = new VLookup ("C_BPartner_ID", true, false, true, lookup);
		//
		int C_BPartner_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_BPartner_ID");
		bPartnerField.setValue(new Integer(C_BPartner_ID));

		//  initial loading
		initBPartnerOIS(C_BPartner_ID, forInvoice);
	}   //  initBPartner

	/**
	 *  Load PBartner dependent Order/Invoice/Shipment Field.
	 *  @param C_BPartner_ID BPartner
	 *  @param forInvoice for invoice
	 */
	protected void initBPartnerOIS (int C_BPartner_ID, boolean forInvoice)
	{
		log.config("C_BPartner_ID=" + C_BPartner_ID);
		KeyNamePair pp = new KeyNamePair(0,"");

		//  load PO Orders - Closed, Completed
		orderField.removeActionListener(this);
		orderField.removeAllItems();
		orderField.addItem(pp);
		//	Display
		StringBuffer display = new StringBuffer("o.DocumentNo||' - ' ||")
			.append(DB.TO_CHAR("o.DateOrdered", DisplayType.Date, Env.getAD_Language(Env.getCtx())))
			.append("||' - '||")
			.append(DB.TO_CHAR("o.GrandTotal", DisplayType.Amount, Env.getAD_Language(Env.getCtx())));
		//
		String column = "m.M_InOutLine_ID";
		if (forInvoice)
			column = "m.C_InvoiceLine_ID";

		int C_DocType_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_DocType_ID");
		
                /*
                ** Vit4B - Modificado para que liste todas las OC del sociom de negocio sin restricciones de modo 
                ** que pueda devolver siempre
                ** 15/02/2007
                */
                StringBuffer sql;

		if (!isSoTrx)
		{	
			if(C_DocType_ID == 1000143)
                        {
                            sql = new StringBuffer("SELECT o.C_Order_ID,").append(display)
				.append(" FROM C_Order o "
				//begin vpj-cd e-evolutio 11 ENE 2006
				//+ "WHERE o.C_BPartner_ID=? AND o.IsSOTrx='N' AND o.DocStatus IN ('CL','CO')"
				+ "WHERE o.C_BPartner_ID=? AND o.IsSOTrx='N' "
		 		//end vpj-cd e-evolutio 11 ENE 2006
				+ " AND o.C_Order_ID IN "
				+ "(SELECT ol.C_Order_ID FROM C_OrderLine ol"
				+ " LEFT OUTER JOIN M_MatchPO m ON (ol.C_OrderLine_ID=m.C_OrderLine_ID) "
				+ "GROUP BY ol.C_Order_ID,ol.C_OrderLine_ID, ol.QtyOrdered,").append(column)
				.append(" HAVING (").append(column)
				.append(" IS NOT NULL) OR ").append(column).append(" IS NULL) "
				+ "ORDER BY o.DateOrdered");
                            try
                            {
                                PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
                                pstmt.setInt(1, C_BPartner_ID);
                                ResultSet rs = pstmt.executeQuery();
                                while (rs.next())
                                {
                                        pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
                                        orderField.addItem(pp);
                                }
                                rs.close();
                                pstmt.close();
                            }
                            catch (SQLException e)
                            {
                                    log.log(Level.SEVERE, sql.toString(), e);
                            }

                        }
                        else
                            if (C_DocType_ID == 1000142)
                            {
                                  /**
                                   * BISion - 30/01/2009 - Santiago Iba�ez
                                   * Modificacion realizada para obtener desde la BD el
                                   * umbral permitido de excedente
                                   */
                                  int umbral = 0;
                                  try {
                                      //Selecciono el Umbral especificado
                                      int AD_Workflow_ID = Env.getContextAsInt(Env.getCtx(), "AD_Workflow_ID");
                                      String sqlUmbral = "SELECT valor FROM M_UMBRAL WHERE m_umbral_id = 1000000";
                                      PreparedStatement pstmt = DB.prepareStatement(sqlUmbral, null);
                                      ResultSet rs = pstmt.executeQuery();
                                      if (rs.next())
                                          umbral = rs.getInt(1);
                                      rs.close();
                                      //fin modificacion BISion
                                      //Obtengo las OC completas asociadas al Socio del negocio
                                      int orders[]=MOrder.getAllIDs("C_Order", "C_BPartner_ID = "+C_BPartner_ID+" AND DocStatus = 'CO'", "Y");
                                      //Por cada orden las agrego al ComboBox si cumple la condicion
                                      for (int i=0; i<orders.length;i++){
                                          int C_Order_ID = orders[i];
                                          //Obtengo la orden de compra desde C_Order_ID
                                          MOrder order = new MOrder(Env.getCtx(),C_Order_ID,null);
                                          //Recepcion completa?
                                          if (!recepcionCompleta(order,umbral)){
                                              //agrego al CB la OC de compra
                                              pp = new KeyNamePair(order.getC_Order_ID(),order.getDocumentNo()+" - "+order.getDateOrdered()+" - "+order.getGrandTotal());
                                              orderField.addItem(pp);
                                          }
                                      }
                                      sql = new StringBuffer("SELECT o.C_Order_ID,").append(display).append(" FROM C_Order o "
                                            + "WHERE o.C_BPartner_ID=? AND o.IsSOTrx='N' AND o.DocStatus IN ('CO')"
                                            + " AND o.C_Order_ID IN "
                                            + "(SELECT ol.C_Order_ID FROM C_OrderLine ol"
                                            + " LEFT OUTER JOIN M_MatchPO m ON (ol.C_OrderLine_ID=m.C_OrderLine_ID) "
                                            /*
                                             *  28/05/2013 Maria Jesus Martin
                                             *  Agregado para que en las Recepciones de Material de Terceros
                                             *  no se puedan elegir cargos.
                                             */
                                            + " WHERE ol.m_product_id IS NOT null "
                                            + "GROUP BY ol.C_Order_ID,ol.C_OrderLine_ID, ol.QtyOrdered,").append(column).append(" HAVING (ol.QtyOrdered <> SUM(m.Qty) AND ").append(column).append(" IS NOT NULL) OR ").append(column).append(" IS NULL) " + "ORDER BY o.DateOrdered");
                                        } catch (SQLException ex) {
                                      Logger.getLogger(VCreateFrom.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                            }
                            else
                            {
                                    sql = new StringBuffer("SELECT o.C_Order_ID, o.DateOrdered, ").append(display)
                                    .append(" FROM C_Order o "
                                 //begin vpj-cd e-evolutio 11 ENE 2006
                                    + "WHERE o.C_BPartner_ID=? AND o.IsSOTrx='N' AND o.DocStatus IN ('CO','CL')"
                                    //end vpj-cd e-evolutio 11 ENE 2006
                                    + " AND o.C_Order_ID IN "
                                    + "(SELECT ol.C_Order_ID FROM C_OrderLine ol"
                                    + " LEFT OUTER JOIN M_MatchPO m ON (ol.C_OrderLine_ID=m.C_OrderLine_ID) "
                                    + " WHERE ol.M_Product_ID IS NOT NULL "
                                    + "GROUP BY ol.C_Order_ID,ol.C_OrderLine_ID, ol.QtyOrdered,").append(column)
                                    .append(" HAVING (ol.QtyOrdered <> SUM(m.Qty) AND ").append(column)
                                    .append(" IS NOT NULL) OR ").append(column).append(" IS NULL) ");

                                    StringBuffer sqlCargos = new StringBuffer(" UNION SELECT o.C_Order_ID,o.DateOrdered, ").append(display)
                                    .append(" FROM C_Order o "
                                    + "WHERE o.C_BPartner_ID=? AND o.IsSOTrx='N' AND o.DocStatus IN ('CO','CL')"
                                    + " AND o.C_Order_ID IN "
                                    + "(SELECT ol.C_Order_ID FROM C_OrderLine ol"
                                    + " LEFT OUTER JOIN M_MatchPO m ON (ol.C_OrderLine_ID=m.C_OrderLine_ID) "
                                    + " WHERE ol.C_Charge_ID IS NOT NULL "
                                    + "GROUP BY ol.C_Order_ID,ol.C_OrderLine_ID, ol.QtyOrdered,").append(column)
                                    .append(" HAVING (").append(column)
                                    .append(" IS NOT NULL) OR ").append(column).append(" IS NULL) "
                                    + "ORDER BY DateOrdered ");

                                    sql = sql.append(sqlCargos);
                                    try
                                    {
                                            PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
                                            pstmt.setInt(1, C_BPartner_ID);
                                            pstmt.setInt(2, C_BPartner_ID);
                                            ResultSet rs = pstmt.executeQuery();
                                            while (rs.next())
                                            {
                                                    pp = new KeyNamePair(rs.getInt(1), rs.getString(3));
                                                    orderField.addItem(pp);
                                            }
                                            rs.close();
                                            pstmt.close();
                                    }
                                    catch (SQLException e)
                                    {
                                            log.log(Level.SEVERE, sql.toString(), e);
                                    }
                            }
                        } //	Orden de Compra
                        else	//Orden de Venta
                        {
                            sql = new StringBuffer("SELECT o.C_Order_ID,").append(display)
                            .append(" FROM C_Order o "
                            //begin vpj-cd e-evolutio 11 ENE 2006
                            //+ "WHERE o.C_BPartner_ID=? AND o.IsSOTrx='N' AND o.DocStatus IN ('CL','CO')"
                            + "WHERE o.C_BPartner_ID=? AND o.IsSOTrx='Y' AND o.DocStatus IN ('CO')"
                            //end vpj-cd e-evolutio 11 ENE 2006
                            + " AND o.C_Order_ID IN "
                            + "(SELECT ol.C_Order_ID FROM C_OrderLine ol"
                            + " LEFT OUTER JOIN M_MatchPO m ON (ol.C_OrderLine_ID=m.C_OrderLine_ID) "
                            + "GROUP BY ol.C_Order_ID,ol.C_OrderLine_ID, ol.QtyOrdered,").append(column)
                            .append(" HAVING (ol.QtyOrdered <> SUM(m.Qty) AND ").append(column)
                            .append(" IS NOT NULL) OR ").append(column).append(" IS NULL) "
                            + "ORDER BY o.DateOrdered");
                            try
                            {
                                PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
                                pstmt.setInt(1, C_BPartner_ID);
                                ResultSet rs = pstmt.executeQuery();
                                while (rs.next())
                                {
                                        pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
                                        orderField.addItem(pp);
                                }
                                rs.close();
                                pstmt.close();
                            }
                            catch (SQLException e)
                            {
                                    log.log(Level.SEVERE, sql.toString(), e);
                            }
                        }

		orderField.setSelectedIndex(0);
		orderField.addActionListener(this);

		initBPDetails(C_BPartner_ID);
	}   //  initBPartnerOIS

    /**
     * BISion - 03/02/2009 - Santiago Ibañez
     * Modificacion realizada para comprobar si la OC dada ya no puede recibir
     * mas nada.
     * @param C_Order_ID OC
     * @param umbral umbral permitido
     */
    private boolean recepcionCompleta(MOrder order,int umbral){
        //Obtengo las lineas de la OC
        MOrderLine[] lines = order.getLines();
        //Por cada linea chequeo que la cantidad ordenada + umbral permitido
        //sea = a la suma de todas las recepciones realizadas sobre dicha linea
        for (int i=0;i<lines.length;i++){
            //obtengo la suma de las recepciones realizadas sobre esta linea OC
            MMatchPO[] matchs = MMatchPO.getByOrderLine(Env.getCtx(),lines[i].getC_OrderLine_ID(),null);
            //Cantidad recibida para la línea
            BigDecimal qty = BigDecimal.ZERO;
            //sumo todas las cantidades recibidas
            for (int j=0;j<matchs.length;j++){
                qty = qty.add(matchs[j].getQty());
            }
            BigDecimal u = new BigDecimal(umbral);
            u = u.divide(new BigDecimal(100));
            //Si una linea de la OC no esta completa entonces la OC tampoco
            //BISion - 22/06/2009 - Santiago Iba�ez
            //Si la cantidad recibida para esta OC es menor a la cantidad pedida mas el umbral
            if (qty.compareTo(lines[i].getQtyEntered().add(lines[i].getQtyEntered().multiply(u)))<0)
            //if (qty.compareTo(lines[i].getQtyOrdered().add(lines[i].getQtyOrdered().multiply(u)))<0)
                return false;
        }
        return true;
    }
    /**
	 *  Load Data - Order
	 *  @param C_Order_ID Order
	 *  @param forInvoice true if for invoice vs. delivery qty
	 */
	protected void loadOrder (int C_Order_ID, boolean forInvoice)
	{
		/**
		 *  Selected        - 0
		 *  Qty             - 1
		 *  C_UOM_ID        - 2
		 *  M_Product_ID    - 3
		 *  OrderLine       - 4
		 *  ShipmentLine    - 5
		 *  InvoiceLine     - 6
		 */
		log.config("C_Order_ID=" + C_Order_ID);
		p_order = new MOrder (Env.getCtx(), C_Order_ID, null);      //  save
		/** BISion - 22/06/2009 - Santiago Ibañez
         * Se modifico el valor de la primer columna de la consulta (en //1)
         * antes: l.QtyOrdered-SUM(COALESCE(m.Qty,0))
         * ahora: l.QtyEntered-SUM(COALESCE(m.Qty,0))
         * Y tambien el primer criterio de agupamiento en el group by
         * antes: l.QtyOrdered
         * ahora: l.QtyEntered
         */
		Vector<Vector> data = new Vector<Vector>();
		StringBuffer sql = new StringBuffer("SELECT "
			+ "l.QtyEntered-SUM(COALESCE(m.Qty,0)),"					//	1
			+ "CASE WHEN l.QtyOrdered=0 THEN 0 ELSE l.QtyEntered/l.QtyOrdered END,"	//	2
			+ " l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name),"			//	3..4
			+ " COALESCE(l.M_Product_ID,0),COALESCE(p.Name,c.Name),"	//	5..6
			+ " l.C_OrderLine_ID,l.Line "								//	7..8
//			begin vpj-cd e-evolution 11 Ene 2006 add Value to Description
			+ " ,p.Value "												// 9 Value M_Product
//			end vpj-cd e-evolution 11 Ene 2006 add Value to Description
			+ "FROM C_OrderLine l"
			+ " LEFT OUTER JOIN M_MatchPO m ON (l.C_OrderLine_ID=m.C_OrderLine_ID AND ");
		sql.append(forInvoice ? "m.C_InvoiceLine_ID" : "m.M_InOutLine_ID");
		sql.append(" IS NOT NULL)")
			.append(" LEFT OUTER JOIN M_Product p ON (l.M_Product_ID=p.M_Product_ID)"
			+ " LEFT OUTER JOIN C_Charge c ON (l.C_Charge_ID=c.C_Charge_ID)");
		if (Env.isBaseLanguage(Env.getCtx(), "C_UOM"))
			sql.append(" LEFT OUTER JOIN C_UOM uom ON (l.C_UOM_ID=uom.C_UOM_ID)");
		else
			sql.append(" LEFT OUTER JOIN C_UOM_Trl uom ON (l.C_UOM_ID=uom.C_UOM_ID AND uom.AD_Language='")
				.append(Env.getAD_Language(Env.getCtx())).append("')");
		//
		sql.append(" WHERE l.C_Order_ID=?");
                if (!forInvoice)
                     sql.append(" AND c.C_charge_ID is null");
                sql.append(" GROUP BY l.QtyEntered,CASE WHEN l.QtyOrdered=0 THEN 0 ELSE l.QtyEntered/l.QtyOrdered END, "
			+ "l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name), "
				+ "l.M_Product_ID,COALESCE(p.Name,c.Name), l.Line,l.C_OrderLine_ID "
//				begin vpj-cd e-evolution 11 Ene 2006 add Value to Description
				+ " ,p.Value "												// 9 Value M_Product
//				end vpj-cd e-evolution 11 Ene 2006 add Value to Description
			+ "ORDER BY l.Line");
		//
		log.finer(sql.toString());
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, C_Order_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));           //  0-Selection
				BigDecimal qtyOrdered = rs.getBigDecimal(1);
				BigDecimal multiplier = rs.getBigDecimal(2);
				//BigDecimal qtyEntered = qtyOrdered.multiply(multiplier);
				/** BISion - 22/06/2009 - Santiago Ibañez
                 * Modificado para que traiga la cantidad ordenada que es la pendiente para completar la OC
                 */
				line.add(new Double(qtyOrdered.doubleValue()));  //  1-Qty
				//begin vpj-cd e-evolution 11 Ene 2006 add Value to Description
				//KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(4).trim());
				KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(9) + "-" +rs.getString(4).trim());
                //end vpj-cd e-evolution 11 Ene 2006
				line.add(pp);                           //  2-UOM
				pp = new KeyNamePair(rs.getInt(5), rs.getString(6));
				line.add(pp);                           //  3-Product
				pp = new KeyNamePair(rs.getInt(7), rs.getString(8));
				line.add(pp);                           //  4-OrderLine
				line.add(null);                         //  5-Ship
				line.add(null);                         //  6-Invoice
				data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		loadTableOIS (data);
	}   //  LoadOrder


	/**
	 *  Load Order/Invoice/Shipment data into Table
	 *  @param data data
	 */
	protected void loadTableOIS (Vector data)
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>(7);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "Quantity"));
		columnNames.add(Msg.translate(Env.getCtx(), "C_UOM_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "M_Product_ID"));
		
		
		columnNames.add(Msg.getElement(Env.getCtx(), "C_Order_ID", isSoTrx));
		columnNames.add(Msg.getElement(Env.getCtx(), "M_InOut_ID", isSoTrx));
		columnNames.add(Msg.getElement(Env.getCtx(), "C_Invoice_ID", isSoTrx));

		//  Remove previous listeners
		dataTable.getModel().removeTableModelListener(this);
		//  Set Model
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		model.addTableModelListener(this);
		dataTable.setModel(model);
		//
		dataTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		dataTable.setColumnClass(1, Double.class, true);        //  1-Qty
		dataTable.setColumnClass(2, String.class, true);        //  2-UOM
		dataTable.setColumnClass(3, String.class, true);        //  3-Product
		dataTable.setColumnClass(4, String.class, true);        //  4-Order
		dataTable.setColumnClass(5, String.class, true);        //  5-Ship
		dataTable.setColumnClass(6, String.class, true);        //  6-Invoice
		//  Table UI
		dataTable.autoSize();
	}   //  loadOrder

}   //  VCreateFrom
