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

import java.awt.event.*;
import java.beans.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;
import javax.swing.table.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Create Invoice Transactions from PO Orders or AP Invoices
 *
 *  @author Jorg Janke
 *  @version  $Id: VCreateFromInvoice.java,v 1.35 2005/11/14 02:10:58 jjanke Exp $
 */
public class VCreateFromInvoice extends VCreateFrom implements VetoableChangeListener {

    /**
     *  Protected Constructor
     *  @param mTab MTab
     */
    VCreateFromInvoice(MTab mTab) {
        super(mTab);
        log.info(mTab.toString());
    }   //  VCreateFromInvoice
    private boolean m_actionActive = false;
    private MInOut m_inout = null;

    /**
     *  Dynamic Init
     *  @throws Exception if Lookups cannot be initialized
     *  @return true if initialized
     */
    protected boolean dynInit() throws Exception {
        /*
         *  Zynnia modificaciones para controlar que no se generen facturas desde ordenes de compra lo 
         *  que hace que la cantidad entregada qtydelivered se cambie a 0.   
         *  26/03/2012
         *  José Fantasia
         * 
         */

        JOptionPane.showMessageDialog(null, "Si ingresa insumos productivos, deberá obligatoriamente hacerlo por Entrega/Recibo", "Info", JOptionPane.INFORMATION_MESSAGE);
        log.config("");
        setTitle(Msg.getElement(Env.getCtx(), "C_Invoice_ID", false) + " .. " + Msg.translate(Env.getCtx(), "CreateFrom"));

        parameterBankPanel.setVisible(false);
        invoiceLabel.setVisible(false);
        invoiceField.setVisible(false);
        locatorLabel.setVisible(false);
        locatorField.setVisible(false);

        initBPartner(true);
        bPartnerField.addVetoableChangeListener(this);
        return true;
    }   //  dynInit

    /**
     *  Init Details - load receipts not invoiced
     *  @param C_BPartner_ID BPartner
     */
    protected void initBPDetails(int C_BPartner_ID) {
        log.config("C_BPartner_ID" + C_BPartner_ID);

        //  load Shipments (Receipts) - Completed, Closed
        shipmentField.removeActionListener(this);
        shipmentField.removeAllItems();
        //	None
        KeyNamePair pp = new KeyNamePair(0, "");
        shipmentField.addItem(pp);
        //	Display
        StringBuffer display = new StringBuffer("s.DocumentNo||' - '||").append(DB.TO_CHAR("s.MovementDate", DisplayType.Date, Env.getAD_Language(Env.getCtx())));
        //

        StringBuffer sql;
        if (!isSoTrx) {
            sql = new StringBuffer("SELECT s.M_InOut_ID,").append(display).append(" FROM M_InOut s "
                    + "WHERE s.C_BPartner_ID=? AND s.IsSOTrx='N' AND s.DocStatus IN ('CL','CO')"
                    + " AND s.M_InOut_ID IN "
                    + "(SELECT sl.M_InOut_ID FROM M_InOutLine sl"
                    + " LEFT OUTER JOIN M_MatchInv mi ON (sl.M_InOutLine_ID=mi.M_InOutLine_ID) "
                    + "GROUP BY sl.M_InOut_ID,mi.M_InOutLine_ID,sl.MovementQty "
                    + "HAVING (sl.MovementQty<>SUM(mi.Qty) AND mi.M_InOutLine_ID IS NOT NULL)"
                    + " OR mi.M_InOutLine_ID IS NULL) "
                    + "ORDER BY s.MovementDate");
        } else {
            sql = new StringBuffer("SELECT s.M_InOut_ID,").append(display).append(" FROM M_InOut s "
                    + "WHERE s.C_BPartner_ID=? AND s.IsSOTrx='Y' AND s.DocStatus IN ('CL','CO')"
                    + " AND s.M_InOut_ID IN "
                    + "(SELECT sl.M_InOut_ID FROM M_InOutLine sl"
                    + " LEFT OUTER JOIN M_MatchInv mi ON (sl.M_InOutLine_ID=mi.M_InOutLine_ID) "
                    + "GROUP BY sl.M_InOut_ID,mi.M_InOutLine_ID,sl.MovementQty "
                    + "HAVING (sl.MovementQty<>SUM(mi.Qty) AND mi.M_InOutLine_ID IS NOT NULL)"
                    + " OR mi.M_InOutLine_ID IS NULL) "
                    + "ORDER BY s.MovementDate");
        }
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, C_BPartner_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
                shipmentField.addItem(pp);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql.toString(), e);
        }
        shipmentField.setSelectedIndex(0);
        shipmentField.addActionListener(this);
    }   //  initDetails

    /**
     *  Action Listener
     *  @param e event
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (m_actionActive) {
            return;
        }
        m_actionActive = true;
        log.config("Action=" + e.getActionCommand());
        //  Order
        if (e.getSource().equals(orderField)) {
            KeyNamePair pp = (KeyNamePair) orderField.getSelectedItem();
            int C_Order_ID = 0;
            if (pp != null) {
                C_Order_ID = pp.getKey();
            }
            //  set Invoice and Shipment to Null
            invoiceField.setSelectedIndex(-1);
            shipmentField.setSelectedIndex(-1);
            loadOrder(C_Order_ID, true);
        } //  Shipment
        else if (e.getSource().equals(shipmentField)) {
            KeyNamePair pp = (KeyNamePair) shipmentField.getSelectedItem();
            int M_InOut_ID = 0;
            if (pp != null) {
                M_InOut_ID = pp.getKey();
            }
            //  set Order and Invoice to Null
            orderField.setSelectedIndex(-1);
            invoiceField.setSelectedIndex(-1);
            loadShipment(M_InOut_ID);
        }
        m_actionActive = false;
    }   //  actionPerformed

    /**
     *  Change Listener
     *  @param e event
     */
    public void vetoableChange(PropertyChangeEvent e) {
        log.config(e.getPropertyName() + "=" + e.getNewValue());

        //  BPartner - load Order/Invoice/Shipment
        if (e.getPropertyName() == "C_BPartner_ID") {
            int C_BPartner_ID = ((Integer) e.getNewValue()).intValue();
            initBPartnerOIS(C_BPartner_ID, true);
        }
        tableChanged(null);
    }   //  vetoableChange

    /**
     *  Load Data - Shipment not invoiced
     *  @param M_InOut_ID InOut
     */
    private void loadShipment(int M_InOut_ID) {
        log.config("M_InOut_ID=" + M_InOut_ID);
        m_inout = new MInOut(Env.getCtx(), M_InOut_ID, null);
        p_order = null;
        if (m_inout.getC_Order_ID() != 0) {
            p_order = new MOrder(Env.getCtx(), m_inout.getC_Order_ID(), null);
        }

        //
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        StringBuffer sql = new StringBuffer("SELECT " //	QtyEntered
                + "l.MovementQty-SUM(NVL(mi.Qty,0)),l.QtyEntered/l.MovementQty,"
                + " l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name)," //  3..4
                + " l.M_Product_ID,p.Name, l.M_InOutLine_ID,l.Line," //  5..8
                + " l.C_OrderLine_ID ");                             		//  9
        if (Env.isBaseLanguage(Env.getCtx(), "C_UOM")) {
            sql.append("FROM C_UOM uom, M_InOutLine l, M_Product p, M_MatchInv mi ");
            sql.append("WHERE l.C_UOM_ID=uom.C_UOM_ID");
        } else {
            sql.append("FROM C_UOM_Trl uom, M_InOutLine l, M_Product p, M_MatchInv mi ");
            sql.append("WHERE l.C_UOM_ID=uom.C_UOM_ID AND uom.AD_Language='").append(Env.getAD_Language(Env.getCtx())).append("'");
        }
        sql.append(" AND l.M_Product_ID=p.M_Product_ID") //begin vpj-cd e-evolution 03/15/2005
                //.append(" AND l.M_InOutLine_ID=mi.M_InOutLine_ID(+)")
                .append(" AND l.M_InOutLine_ID=mi.M_InOutLine_ID(+) ") //end vpj-cd e-evolution 03/15/2005
                .append("AND l.M_InOut_ID=? ") //  #1
                .append("GROUP BY l.MovementQty,l.QtyEntered/l.MovementQty,"
                + "l.C_UOM_ID,COALESCE(uom.UOMSymbol,uom.Name),"
                + "l.M_Product_ID,p.Name, l.M_InOutLine_ID,l.Line,l.C_OrderLine_ID ").append("ORDER BY l.Line");

        try {
            PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, M_InOut_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vector<Object> line = new Vector<Object>(7);
                line.add(new Boolean(false));           //  0-Selection
                BigDecimal qtyMovement = rs.getBigDecimal(1);
                BigDecimal multiplier = rs.getBigDecimal(2);
                BigDecimal qtyEntered = qtyMovement.multiply(multiplier);
                line.add(new Double(qtyEntered.doubleValue()));  //  1-Qty
                KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(4).trim());
                line.add(pp);                           //  2-UOM
                pp = new KeyNamePair(rs.getInt(5), rs.getString(6));
                line.add(pp);                           //  3-Product
                int C_OrderLine_ID = rs.getInt(9);
                if (rs.wasNull()) {
                    line.add(null);                     //  4-Order
                } else {
                    line.add(new KeyNamePair(C_OrderLine_ID, "."));
                }
                pp = new KeyNamePair(rs.getInt(7), rs.getString(8));
                line.add(pp);                           //  5-Ship
                line.add(null);                     	//  6-Invoice
                data.add(line);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql.toString(), e);
        }
        loadTableOIS(data);
    }   //  loadShipment

    /**
     *  List number of rows selected
     */
    protected void info() {
        TableModel model = dataTable.getModel();
        int rows = model.getRowCount();
        int count = 0;
        for (int i = 0; i < rows; i++) {
            if (((Boolean) model.getValueAt(i, 0)).booleanValue()) {
                count++;
            }
        }
        statusBar.setStatusLine(String.valueOf(count));
    }   //  infoInvoice

    /**
     *  Save - Create Invoice Lines
     *  @return true if saved
     */
    protected boolean save() {
        log.config("");
        TableModel model = dataTable.getModel();
        int rows = model.getRowCount();
        if (rows == 0) {
            return false;
        }

        //  Invoice
        int C_Invoice_ID = ((Integer) p_mTab.getValue("C_Invoice_ID")).intValue();
        MInvoice invoice = new MInvoice(Env.getCtx(), C_Invoice_ID, null);
        log.config(invoice.toString());


        if (!invoice.isSOTrx() && invoice.getC_Order_ID() == 0 && p_order != null) {
            invoice.setOrder(p_order);	//	overwrite header values
            invoice.save();
        }

        if (m_inout != null && m_inout.getM_InOut_ID() != 0
                && m_inout.getC_Invoice_ID() == 0) //	only first time
        {
            m_inout.setC_Invoice_ID(C_Invoice_ID);
            m_inout.save();
        }


        //  Lines
        for (int i = 0; i < rows; i++) {
            if (((Boolean) model.getValueAt(i, 0)).booleanValue()) {
                //  variable values
                Double d = (Double) model.getValueAt(i, 1);              //  1-Qty
                BigDecimal QtyEntered = new BigDecimal(d.doubleValue());
                KeyNamePair pp = (KeyNamePair) model.getValueAt(i, 2);   //  2-UOM
                int C_UOM_ID = pp.getKey();
                //
                pp = (KeyNamePair) model.getValueAt(i, 3);               //  3-Product
                int M_Product_ID = 0;
                if (pp != null) {
                    M_Product_ID = pp.getKey();
                }
                //int C_Charge_ID = 0;
                //
                int C_OrderLine_ID = 0;
                pp = (KeyNamePair) model.getValueAt(i, 4);               //  4-OrderLine
                if (pp != null) {
                    C_OrderLine_ID = pp.getKey();
                }
                int M_InOutLine_ID = 0;
                pp = (KeyNamePair) model.getValueAt(i, 5);               //  5-Shipment
                if (pp != null) {
                    M_InOutLine_ID = pp.getKey();
                }
                //
                log.fine("Line QtyEntered=" + QtyEntered
                        + ", Product_ID=" + M_Product_ID
                        + ", OrderLine_ID=" + C_OrderLine_ID + ", InOutLine_ID=" + M_InOutLine_ID);

                MInvoiceLine invoiceLine = new MInvoiceLine(invoice);
                invoiceLine.setM_Product_ID(M_Product_ID, C_UOM_ID);	//	Line UOM
                invoiceLine.setQty(QtyEntered);							//	Invoiced/Entered

                /** @author Daniel
                 *  @date 17/09/2009
                 *  @version 2.7.7.2
                 *  @description Corregir la numeraci�n de l�neas de factura
                 */
                invoiceLine.setLine(MInvoiceLine.getNextLine(invoiceLine.getC_Invoice_ID()));
                /** FIN */
                //  Info
                MOrderLine orderLine = null;
                if (C_OrderLine_ID != 0) {
                    orderLine = new MOrderLine(Env.getCtx(), C_OrderLine_ID, null);
                }
                MInOutLine inoutLine = null;
                if (M_InOutLine_ID != 0) {
                    inoutLine = new MInOutLine(Env.getCtx(), M_InOutLine_ID, null);
                    if (orderLine == null && inoutLine.getC_OrderLine_ID() != 0) {
                        C_OrderLine_ID = inoutLine.getC_OrderLine_ID();
                        orderLine = new MOrderLine(Env.getCtx(), C_OrderLine_ID, null);
                    }
                } else {
                    MInOutLine[] lines = MInOutLine.getOfOrderLine(Env.getCtx(),
                            C_OrderLine_ID, null, null);
                    log.fine("Receipt Lines with OrderLine = #" + lines.length);
                    if (lines.length > 0) {
                        for (int j = 0; j < lines.length; j++) {
                            MInOutLine line = lines[j];
                            if (line.getQtyEntered().compareTo(QtyEntered) == 0) {
                                inoutLine = line;
                                M_InOutLine_ID = inoutLine.getM_InOutLine_ID();
                                break;
                            }
                        }
                        if (inoutLine == null) {
                            inoutLine = lines[0];	//	first as default
                            M_InOutLine_ID = inoutLine.getM_InOutLine_ID();
                        }
                    }
                }	//	get Ship info

                //	Shipment Info
                if (inoutLine != null) {
                    invoiceLine.setShipLine(inoutLine);		//	overwrites
                    if (inoutLine.getQtyEntered().compareTo(inoutLine.getMovementQty()) != 0) {
                        invoiceLine.setQtyInvoiced(QtyEntered.multiply(inoutLine.getMovementQty()).divide(inoutLine.getQtyEntered(), BigDecimal.ROUND_HALF_UP));
                    }
                } else {
                    log.fine("No Receipt Line");
                }

                //	Order Info

                // El facturado es diecto no tiene que depender del ordenado

                if (orderLine != null) {
                    invoiceLine.setOrderLine(orderLine);	//	overwrites
                    invoiceLine.setDiscount(orderLine.getDiscount());
                    if (orderLine.getQtyEntered().compareTo(orderLine.getQtyOrdered()) != 0) /*invoiceLine.setQtyInvoiced(QtyEntered
                    .multiply(orderLine.getQtyOrdered())
                    .divide(orderLine.getQtyEntered(), BigDecimal.ROUND_HALF_UP));*/ {
                        invoiceLine.setQtyInvoiced(QtyEntered);
                    }
                } else {
                    log.fine("No Order Line");
                    invoiceLine.setPrice();
                    invoiceLine.setTax();
                }

                /*
                 * Si la linea la creo a partir de una recepcion, 
                 * entonces la partida de la linea de la factura
                 * debe estar asociada con la partida de la recepcion
                 */

                if (orderField.getSelectedIndex() != -1) { //From Order
                    if (orderLine != null) {
                        invoiceLine.setM_AttributeSetInstance_ID(orderLine.getM_AttributeSetInstance_ID());
                    } else {
                        log.fine("No Order Line, M_AttributeSetInstance_ID seted is:" + invoiceLine.getM_AttributeSetInstance_ID());
                    }
                } else if (shipmentField.getSelectedIndex() != -1) { //From Receipt
                    if (inoutLine != null) {
                        invoiceLine.setM_AttributeSetInstance_ID(inoutLine.getM_AttributeSetInstance_ID());
                    } else {
                        log.fine("No InOut Line, M_AttributeSetInstance_ID seted is:" + invoiceLine.getM_AttributeSetInstance_ID());
                    }
                }

                if (!invoiceLine.save()) {
                    log.log(Level.SEVERE, "Line NOT created #" + i);
                }
            }   //   if selected
        }   //  for all rows

        return true;
    }   //  saveInvoice
}   //  VCreateFromInvoice
