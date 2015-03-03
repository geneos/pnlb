/*
 * VOrderReceiptIssue.java
 *
 * Created on 29 de julio de 2004, 05:10 PM
 */

package org.eevolution.form;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.math.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.Component;
//import java.awt.Color;
import javax.swing.JTable;

import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;

import org.compiere.util.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.apps.*;
import org.compiere.minigrid.*;
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.apps.form.*;



import javax.swing.*;
import javax.swing.tree.*;

import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.*;


import org.compiere.process.DocAction;
import org.eevolution.model.*;

/**
 *
 * @author  vpj-cd
 */

public class ActualizacionVencimientoPartidas extends CPanel
        implements FormPanel, ActionListener, VetoableChangeListener, ChangeListener, ListSelectionListener, TableModelListener, ASyncProcess {

    /** Creates new form VOrderReceiptIssue */
    /*public VOrderReceiptIssue() {
        //initComponents();
    }*/


    /**
     *	Initialize Panel
     *  @param WindowNo window
     *  @param frame frame
     */
    public void init(int WindowNo, FormFrame frame) {
        m_WindowNo = WindowNo;
        m_frame = frame;
        log.info("VOrderReceipIssue.init - WinNo=" + m_WindowNo +
                "AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID);
        //Env.setContext(Env.getCtx(), m_WindowNo, "IsSOTrx", "N");

        try {
            //	UI

            fillPicks();
            jbInit();
            //
            dynInit();
            pickcombo.addActionListener(this);
            frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
            frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
        } catch(Exception e) {
            log.log(Level.SEVERE, "VOrderReceipIssue.init", e);
        }
    }	//	init




    /**	Window No			*/
    private int         	m_WindowNo = 0;
    /**	FormFrame			*/
    private FormFrame 		m_frame;
    private StatusBar statusBar = new StatusBar();

    private int     m_AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
    private int     m_AD_Org_ID = Env.getAD_Org_ID(Env.getCtx());
    private int     m_by = Env.getAD_User_ID(Env.getCtx());
    private MMPCOrder m_mpc_order = null;
    private static CLogger log = CLogger.getCLogger(VOrderReceiptIssue.class);

    private int Cost_Collector_HEADER_ID;

    private String ordernumber = "";



    /**
     *  Static Init.
     *  <pre>
     *  mainPanel
     *      northPanel
     *      centerPanel
     *          xMatched
     *          xPanel
     *          xMathedTo
     *      southPanel
     *  </pre>
     *  @throws Exception
     */
    private void jbInit() throws Exception {
        java.awt.GridBagConstraints gridBagConstraints;

        /*TabsReceiptsIssue = new javax.swing.JTabbedPane();
        ReceiptIssueOrder = new javax.swing.JPanel();
        PanelCenter = new javax.swing.JPanel();
        issueScrollPane = new javax.swing.JScrollPane();
        issue = new javax.swing.JTable();
        PanelBottom = new javax.swing.JPanel();
        Process = new javax.swing.JButton();
        northPanel = new javax.swing.JPanel();
        orgLabel = new javax.swing.JLabel();
        org = new javax.swing.JTextField();
        resourceLabel = new javax.swing.JLabel();
        resource = new javax.swing.JTextField();
        orderLabel = new javax.swing.JLabel();
        order = new javax.swing.JTextField();
        movementDateLabel = new javax.swing.JLabel();
        movementDate = new javax.swing.JTextField();
        deliveryQtyLabel = new javax.swing.JLabel();
        deliveryQty = new javax.swing.JTextField();
        scrapQtyLabel = new javax.swing.JLabel();
        scrapQty = new javax.swing.JTextField();
        rejectQtyLabel = new javax.swing.JLabel();
        rejectQty = new javax.swing.JTextField();
        attributeLabel = new javax.swing.JLabel();
        attribute = new javax.swing.JTextField();
        issueMethodLabel = new javax.swing.JLabel();
        issueMethod = new javax.swing.JTextField();
        Generate = new javax.swing.JPanel();*/

        setLayout(new java.awt.BorderLayout());
        mainPanel.setLayout(new java.awt.BorderLayout());

        ReceiptIssueOrder.setLayout(new java.awt.BorderLayout());

        PanelCenter.setLayout(new java.awt.BorderLayout());

        issueScrollPane.setBorder(new javax.swing.border.TitledBorder(""));

        issueScrollPane.setViewportView(issue);

        PanelCenter.add(issueScrollPane, java.awt.BorderLayout.CENTER);

        ReceiptIssueOrder.add(PanelCenter, java.awt.BorderLayout.CENTER);

        Process.setText(Msg.translate(Env.getCtx(), "OK"));


        PanelBottom.add(Process);

        ReceiptIssueOrder.add(PanelBottom, java.awt.BorderLayout.SOUTH);

        northPanel.setLayout(new java.awt.GridBagLayout());


        orderLabel.setText(Msg.translate(Env.getCtx(), "MPC_Order_ID"));

        northPanel.add(orderLabel,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        northPanel.add(order,     new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


        resourceLabel.setText(Msg.translate(Env.getCtx(), "S_Resource_ID"));

        northPanel.add(resourceLabel,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        northPanel.add(resource,     new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


        warehouseLabel.setText(Msg.translate(Env.getCtx(), "M_Warehouse_ID"));

        northPanel.add(warehouseLabel,    new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        northPanel.add(warehouse,     new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


        northPanel.add(warehouseLabel,    new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        // Product

        northPanel.add(producLabel,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        northPanel.add(product,     new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


        northPanel.add(uomLabel,    new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        northPanel.add(uom,     new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


        northPanel.add(uomorderLabel,    new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        northPanel.add(uomorder,     new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


        orderedQtyLabel.setText(Msg.translate(Env.getCtx(), "QtyOrdered"));

        northPanel.add(orderedQtyLabel,    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        northPanel.add(orderedQty,     new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        deliveredQtyLabel.setText(Msg.translate(Env.getCtx(), "QtyDelivered"));

        northPanel.add(deliveredQtyLabel,    new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        northPanel.add(deliveredQty,     new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        openQtyLabel.setText(Msg.translate(Env.getCtx(), "QtyToDeliver"));

        northPanel.add(openQtyLabel,    new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        northPanel.add(openQty,     new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        /*order.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderActionPerformed(evt);
            }
        });*/



        QtyBatchsLabel.setText(Msg.translate(Env.getCtx(), "QtyBatchs"));


        northPanel.add(QtyBatchsLabel,    new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        northPanel.add(qtyBatchs,      new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        QtyBatchSizeLabel.setText(Msg.translate(Env.getCtx(), "QtyBatchSize"));

        northPanel.add(QtyBatchSizeLabel,      new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        northPanel.add(qtyBatchSize,       new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        // fjviejo e-evolution combo
        northPanel.add(labelcombo,      new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        northPanel.add(pickcombo,   new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

//        northPanel.add(IsBackflush,   new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0
//			,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
//
//
//        northPanel.add(ListDetail,   new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
//			,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        // fjviejo e-evolution combo


        toDeliverQtyLabel.setText(Msg.translate(Env.getCtx(), "QtyToDeliver"));

        northPanel.add(toDeliverQtyLabel,     new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        northPanel.add(toDeliverQty,      new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));



        scrapQtyLabel.setText(Msg.translate(Env.getCtx(), "QtyScrap"));


        northPanel.add(scrapQtyLabel,    new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        northPanel.add(scrapQty,      new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        rejectQtyLabel.setText(Msg.translate(Env.getCtx(), "QtyReject"));

        northPanel.add(rejectQtyLabel,      new GridBagConstraints(4, 6, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        northPanel.add(rejectQty,       new GridBagConstraints(5, 6, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


        movementDateLabel.setText(Msg.translate(Env.getCtx(), "MovementDate"));

        northPanel.add(movementDateLabel,     new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        northPanel.add(movementDate,       new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));



        locatorLabel.setText(Msg.translate(Env.getCtx(), "M_Locator_ID"));

        northPanel.add(locatorLabel,  new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        northPanel.add(locator,  new GridBagConstraints(3, 7, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


        attributeLabel.setText(Msg.translate(Env.getCtx(), "M_AttributeSetInstance_ID"));

        northPanel.add(attributeLabel,      new GridBagConstraints(4, 7, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        northPanel.add(attribute,       new GridBagConstraints(5, 7, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        /**
         * BISion - 19/10/2009 - Santiago Ibañez
         * COM-PAN-REQ-08.001.01
         */
        northPanel.add(labelVencimiento,     new GridBagConstraints(4, 8, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        northPanel.add(fechaVencimiento,       new GridBagConstraints(5, 8, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        //fin modificacion BISion
        /**
         * BISion - 19/10/2009 - Santiago Ibañez
         * COM-PAN-REQ-02.002.01
         */
        northPanel.add(labelBundle,     new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        northPanel.add(fieldBundle,       new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        //fin modificacion BISion

        northPanel.add(backflushGroupLabel,   new GridBagConstraints(4, 5, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        northPanel.add(backflushGroup,   new GridBagConstraints(5, 5, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        ReceiptIssueOrder.add(northPanel, java.awt.BorderLayout.NORTH);

        //TabsReceiptsIssue.addTab("Receipts & Issue Order", ReceiptIssueOrder);
        TabsReceiptsIssue.add(ReceiptIssueOrder,Msg.translate(Env.getCtx(), "IsShipConfirm"));
        //TabsReceiptsIssue.addTab(Msg.translate(Env.getCtx(), "Generate"), Generate);
        TabsReceiptsIssue.add(Generate ,Msg.translate(Env.getCtx(), "Generate"));
        //tabbedPane.add(genPanel, Msg.getMsg(Env.getCtx(), "Generate"));
        Generate.setLayout(new BorderLayout());
        Generate.add(info, BorderLayout.CENTER);
        Generate.setEnabled(false);
        info.setBackground(CompierePLAF.getFieldBackground_Inactive());
        info.setEditable(false);
        //Generate.add(confirmPanelGen, BorderLayout.SOUTH);
        //confirmPanelGen.addActionListener(this);
        TabsReceiptsIssue.addChangeListener(this);
        add(TabsReceiptsIssue, java.awt.BorderLayout.CENTER);
        mainPanel.add(TabsReceiptsIssue, java.awt.BorderLayout.CENTER);

        add(mainPanel, java.awt.BorderLayout.NORTH);
    }   //  jbInit


    /**
     *  Dynamic Init.
     *  Table Layout, Visual, Listener
     */
    private void dynInit() {
        disableToDeliver();
        issue.addColumn("MPC_OrderBOMLine_ID"); //0
        //issue.addColumn("Line");              //
        issue.addColumn("IsCritical");          //1
        issue.addColumn("Value");               //2
        issue.addColumn("M_Product_ID");        //3
        issue.addColumn("C_UOM_ID");            //4
        //issue.addColumn("BackflushGroup");
        issue.addColumn("M_AttributeStInstance_ID");           //5
        issue.addColumn("QtyRequiered");           //6
        issue.addColumn("QtyDelivered");        //7
        issue.addColumn("QtyToDeliver");        //8
        issue.addColumn("QtyOnHand");           //9
        issue.addColumn("QtyScrap");            //10
        issue.addColumn("QtyReserved");         //11
        issue.addColumn("QtyAvailable");        //12

        issue.addColumn("M_Locator_ID");        //13
        issue.addColumn("M_Warehouse_ID");      //14
        issue.addColumn("QtyBom");              //15
        issue.addColumn("IsQtyPercentage");     //16
        issue.addColumn("QtyBatch");            //17

        issue.setMultiSelection(true);
        issue.setRowSelectionAllowed(true);

        //  set details
        issue.setColumnClass( 0, IDColumn.class   , false, " ");
        issue.setColumnClass( 1, Boolean.class    , true, Msg.translate(Env.getCtx(), "IsCritical"));
        //issue.setColumnClass( 1, Integer.class    , true, Msg.translate(Env.getCtx(), "Line"));
        issue.setColumnClass( 2, String.class    , true, Msg.translate(Env.getCtx(), "Value"));
        issue.setColumnClass( 3, KeyNamePair.class, true, Msg.translate(Env.getCtx(), "M_Product_ID"));
        issue.setColumnClass( 4, KeyNamePair.class, true, Msg.translate(Env.getCtx(), "C_UOM_ID"));
        //issue.setColumnClass( 5, String.class     , true, Msg.translate(Env.getCtx(), "BackflushGroup"));
        issue.setColumnClass(5, String.class , true, Msg.translate(Env.getCtx(), "M_AttributeSetInstance_ID"));
        issue.setColumnClass( 6, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyRequiered"));
        issue.setColumnClass( 7, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyDelivered"));
        issue.setColumnClass( 8, VNumber.class , false, Msg.translate(Env.getCtx(), "QtyToDeliver"));
        issue.setColumnClass( 9, VNumber.class , false, Msg.translate(Env.getCtx(), "QtyScrap"));
        issue.setColumnClass( 10, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyOnHand"));
        issue.setColumnClass( 11, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyReserved"));
        issue.setColumnClass( 12, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyAvailable"));
        // issue.setColumnClass(10, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyOnHand"));
        // issue.setColumnClass(11, String.class , true, Msg.translate(Env.getCtx(), "M_AttributeSetInstance_ID"));
        issue.setColumnClass(13, String.class , true, Msg.translate(Env.getCtx(), "M_Locator_ID"));
        issue.setColumnClass(14, KeyNamePair.class, true, Msg.translate(Env.getCtx(), "M_Warehouse_ID"));
        issue.setColumnClass(15, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyBom"));
        issue.setColumnClass(16, Boolean.class    , true, Msg.translate(Env.getCtx(), "IsQtyPercentage"));
        issue.setColumnClass(17, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyBatch"));
//                issue.setColumnClass(11, BigDecimal.class , true, Msg.translate(Env.getCtx(), "M_Locator_ID"));
//                issue.setColumnClass(12, KeyNamePair.class, true, Msg.translate(Env.getCtx(), "M_Warehouse_ID"));
//                issue.setColumnClass(13, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyBom"));
//                issue.setColumnClass(14, Boolean.class    , true, Msg.translate(Env.getCtx(), "IsQtyPercentage"));
//                issue.setColumnClass(15, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyBatch"));

        //issue.setColumnClass(10, BigDecimal.class , true, Msg.translate(Env.getCtx(), "M_Locator_ID"));

        issue.autoSize();
        issue.getModel().addTableModelListener(this);



        m_sql = new StringBuffer("SELECT obl.MPC_Order_ID,obl.Line,obl.IsCritical,p.Name,obl.M_Product_ID,u.Name,obl.C_UOM_ID,obl.QtyBom,obl.IsQtyPercentage,obl.QtyBatch,obl.QtyRequiered,obl.M_Warehouse_ID , obl.QtyReserved,BOM_Qty_Available(obl.M_Product_ID,obl.M_Warehouse_ID) AS QtyAvailable , BOM_Qty_OnHand(p.M_Product_ID,obl.M_Warehouse_ID) AS QtyOnHand , obl.QtyReserved,obl.QtyScrap ");
        m_sql.append(" FROM MPC_Order_BOMLine obl ");
        m_sql.append(" INNER JOIN M_Product p ON (obl.M_Product_ID=p.M_Product_ID)");
        m_sql.append(" INNER JOIN C_UOM u ON (obl.C_UOM_ID=u.C_UOM_ID) ");

        CompiereColor.setBackground(this);
        issue.setCellSelectionEnabled(true);
        issue.getSelectionModel().addListSelectionListener(this);
        issue.setRowCount(0);
                /*TableColumn column = issue.getColumnModel().getColumn(0);
                column.setCellRenderer ( new CustomTableCellRenderer() );*/


    }   //  dynInit



    /**
     *	Fill Picks
     *		Column_ID from C_Order
     *  @throws Exception if Lookups cannot be initialized
     */
    private void fillPicks() throws Exception {

        Properties ctx = Env.getCtx();


                /*public static MLookup get (Properties ctx, int WindowNo, int Column_ID, int AD_Reference_ID,
                Language language, String ColumnName, int AD_Reference_Value_ID,
                boolean IsParent, String ValidationCode)*/

        Language language = Language.getLoginLanguage();//	Base Language


        //MLookup orderL = MLookupFactory.get(ctx, m_WindowNo, 1000181, DisplayType.Search, language, "MPC_Order_ID" , 0 , false , "MPC_Order.DocStatus = '" + MMPCOrder.DOCSTATUS_Completed + "'");
        //order = new VLookup("MPC_Order_ID", false, false, true, orderL);

        MLookup orderL = MLookupFactory.get(ctx, m_WindowNo, 1002528, DisplayType.Search, language, "MPC_Order_ID" , 1000045 , false , "RV_OM.Estado = '" + MMPCOrder.DOCSTATUS_Completed + "'");
        order = new VLookup("MPC_Order_ID", false, false, true, orderL);

        order.setBackground(CompierePLAF.getInfoBackground());
        //order.addActionListener(this);
        order.addVetoableChangeListener(this);


        MLookup issueMethodL = MLookupFactory.get(ctx, m_WindowNo, 0, 1000313, DisplayType.List);
        issueMethod = new VLookup("IssueMethod", false, false, true, issueMethodL);
        issueMethod.addVetoableChangeListener(this);
        issueMethod.addActionListener(this);

        MLookup resourceL = MLookupFactory.get(ctx, m_WindowNo, 0, 1000513, DisplayType.TableDir);
        resource = new VLookup("S_Resource_ID", false, false, false, resourceL);

        MLookup warehouseL = MLookupFactory.get(ctx, m_WindowNo, 0, 1000408, DisplayType.TableDir);
        warehouse = new VLookup("M_Warehouse_ID", false, false, false, warehouseL);
        //resource.addVetoableChangeListener(this);

        MLookup productL = MLookupFactory.get(ctx, m_WindowNo, 0, 1000380, DisplayType.TableDir);
        product = new VLookup("M_Product_ID", false, false, false, productL);

        MLookup uomL = MLookupFactory.get(ctx, m_WindowNo, 0, 1000187, DisplayType.TableDir);
        uom = new VLookup("C_UOM_ID", false, false, false, uomL);

        MLookup uomorderL = MLookupFactory.get(ctx, m_WindowNo, 0, 1000187, DisplayType.TableDir);
        uomorder = new VLookup("C_UOM_ID", false, false, false, uomorderL);

        MLocatorLookup locatorL = new MLocatorLookup(ctx, m_WindowNo);
        locator = new VLocator("M_Locator_ID", true, false, false, locatorL, m_WindowNo);


        MPAttributeLookup attributeL = new MPAttributeLookup(ctx,m_WindowNo);
        attribute = new VPAttribute(false, false, true, m_WindowNo, attributeL);
        attribute.setValue(new Integer(0));
        //  Tab, Window
        MFieldVO vo =  MFieldVO.createStdField(ctx , m_WindowNo , 1000031 , 1000013, false, false, false);
        // M_AttributeSetInstance_ID
        vo.AD_Column_ID = 1000183;
        MField field = new MField(vo);
        attribute.setField(field);

        pickcombo.addItem(new KeyNamePair(0, ""));
        //fjviejo e-evolution user cant aprove documents
        int AD_User_ID = Env.getAD_User_ID(Env.getCtx());
        int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
        MRole rol = new MRole(Env.getCtx(),AD_Role_ID,null);
        //MProcess process= new MProcess(Env.getCtx(),1000105, null);


//                   pickcombo.addItem(new KeyNamePair (1, Msg.translate(Env.getCtx(), "OnlyReceipt")));
//
//                    pickcombo.addItem(new KeyNamePair (2, Msg.translate(Env.getCtx(), "OnlyIssue")));
//
//                    pickcombo.addItem(new KeyNamePair (3, Msg.translate(Env.getCtx(), "IsBackflush")));

        //System.out.println("***** Acceso a proceso " +rol.);
        //Paccess.getAD_Process_ID();

        /*
         *  Referencias a RET, proceso 1000126 agregadas por VIT4B para la administraci�n del retorno
         *  de materiales de producci�n.
         *
         */

        boolean OR = true;
        boolean OI = true;
        boolean IB = true;
        boolean RET = true;


        if (rol.getProcessAccess(1000112) == null)
            OR = false;
        if (rol.getProcessAccess(1000113) == null)
            OI = false;
        if (rol.getProcessAccess(1000114) == null)
            IB = false;
        if (rol.getProcessAccess(1000126) == null)
            RET = false;

        if (OR) {
            if (rol.getProcessAccess(1000112).booleanValue()) {
                // fjviejo e-evolution end
                pickcombo.addItem(new KeyNamePair(1, Msg.translate(Env.getCtx(), "OnlyReceipt")));
            }
        }

        if (OI) {
            if (rol.getProcessAccess(1000113).booleanValue()) {
                pickcombo.addItem(new KeyNamePair(2, Msg.translate(Env.getCtx(), "OnlyIssue")));
            }
        }

        if (IB) {
            if (rol.getProcessAccess(1000114).booleanValue()) {
                pickcombo.addItem(new KeyNamePair(3, Msg.translate(Env.getCtx(), "IsBackflush")));
            }
        }

        if (RET) {
            if (rol.getProcessAccess(1000126).booleanValue()) {
                pickcombo.addItem(new KeyNamePair(4, Msg.translate(Env.getCtx(), "Return")));
            }
        }

        if (!OR && !OI && !IB) {
            pickcombo.addItem(new KeyNamePair(1, Msg.translate(Env.getCtx(), "OnlyReceipt")));
//
            pickcombo.addItem(new KeyNamePair(2, Msg.translate(Env.getCtx(), "OnlyIssue")));
//
            pickcombo.addItem(new KeyNamePair(3, Msg.translate(Env.getCtx(), "IsBackflush")));

            pickcombo.addItem(new KeyNamePair(4, Msg.translate(Env.getCtx(), "Return")));
        }

        //Boolean ok = new Boolean(true);
        // fjviejo e-evolution combo

//                OnlyReceipt.setValue(new Boolean(true));
//                IsBackflush.setValue(new Boolean(false));
//                ListDetail.setValue(new Boolean(false));
//
        pickcombo.addActionListener(this);
//                OnlyReceipt.addVetoableChangeListener(this);
//                IsBackflush.addVetoableChangeListener(this);
//                ListDetail.addVetoableChangeListener(this);
        // fjviejo e-evolution combo end
        Process.addActionListener(this);
        toDeliverQty.addActionListener(this);
        scrapQty.addActionListener(this);
    }	//	fillPicks

    /**
     *  Fill the table using m_sql
     *  @param table table
     */
    private void tableLoad(MiniTable table) {
        log.finest("tableLoad - " + m_sql + m_groupBy);
        String sql = MRole.getDefault().addAccessSQL(
                m_sql.toString(), "obl", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
                + m_groupBy;
        log.finest( "tableLoad - " + sql);
        try {
            java.sql.Statement stmt = DB.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            table.loadTable(rs);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "VOrderReceiptIssue.tableFill " + sql, e);
        }
    }   //  tableLoad

    /**
     *  Query Info
     */
    private void executeQuery() {
        //log.i("VOrderReceiptIssue.executeQuery");
        issue.removeAll();
        issue.setVisible(true);
        boolean m_OnlyReceipt = false;
        boolean m_IsBackflush = false;
        boolean m_ListDetail = false;


        int valueSelected = Integer.parseInt(pickcombo.getValue().toString());

        if (valueSelected==1)
        {
           m_OnlyReceipt = true;
        }

        if (valueSelected==2)
        {
            m_ListDetail = true;
        }

        if (valueSelected==3) {
            m_OnlyReceipt = true;
            m_IsBackflush = true;
            m_ListDetail = true;
        }

        if (valueSelected==4) {
            m_ListDetail = true;
        }




        int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
        StringBuffer sql = new StringBuffer("SELECT obl.MPC_Order_BOMLine_ID , obl.IsCritical , p.Value , obl.M_Product_ID , p.Name , p.C_UOM_ID , u.Name ,");
        sql.append(" obl.QtyRequiered  , obl.QtyReserved , bomQtyAvailable(obl.M_Product_ID,obl.M_Warehouse_ID, 0 ) AS QtyAvailable , bomQtyOnHand(obl.M_Product_ID,obl.M_Warehouse_ID,0) AS QtyOnHand  , p.M_Locator_ID , obl.M_Warehouse_ID , w.Name  , obl.QtyBom , obl.isQtyPercentage , obl.QtyBatch , obl.ComponentType , obl.QtyRequiered - QtyDelivered AS QtyOpen, obl.QtyDelivered FROM MPC_Order_BOMLine obl");
        sql.append(" INNER JOIN M_Product p ON (obl.M_Product_ID = p.M_Product_ID) ");
        sql.append(" INNER JOIN C_UOM u ON (p.C_UOM_ID = u.C_UOM_ID) ");
        sql.append(" INNER JOIN M_Warehouse w ON (w.M_Warehouse_ID = obl.M_Warehouse_ID) ");
        sql.append(" WHERE obl.MPC_Order_ID = " + order.getValue() + " ORDER BY p.Value , bomQtyOnHand(obl.M_Product_ID,obl.M_Warehouse_ID,0) " );


        log.log(Level.INFO, "VOrderReciptIssue.executeQuery - SQL", sql.toString());
        //  reset table
        int row = 0;
        issue.setRowCount(row);
        //  Execute
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql.toString(),null);
            //pstmt.setInt(1, AD_Client_ID);
            ResultSet rs = pstmt.executeQuery();
            //
            while (rs.next()) {

                //  extend table
                issue.setRowCount(row+1);
                //  set values
                //issue.
                IDColumn id = new IDColumn(rs.getInt(1));
                KeyNamePair m_productkey = new KeyNamePair(rs.getInt(4) , rs.getString(5));
                KeyNamePair m_uomkey = new KeyNamePair(rs.getInt(6),rs.getString(7));
                BigDecimal m_QtyBom = rs.getBigDecimal(15);
                Boolean m_QtyPercentage = null;
                BigDecimal m_QtyBatch = rs.getBigDecimal(17);
                String m_ComponentType = rs.getString(18);
                // fjviejo e-evolution combo
                //boolean m_OnlyReceipt= ((Boolean)OnlyReceipt.getValue()).booleanValue();
                //boolean m_IsBackflush = ((Boolean)IsBackflush.getValue()).booleanValue();
                // fjviejo e-evolution combo end
                KeyNamePair m_warehousekey = new KeyNamePair(rs.getInt(13),rs.getString(14));
                BigDecimal m_onhand = rs.getBigDecimal(10);
                BigDecimal m_toDeliverQty = (BigDecimal)toDeliverQty.getValue();
                BigDecimal m_openQty = (BigDecimal)openQty.getValue();
                BigDecimal m_scrapQty = (BigDecimal)scrapQty.getValue();
                BigDecimal m_rejectQty = (BigDecimal)rejectQty.getValue();
                BigDecimal componentToDeliverQty =Env.ZERO;
                BigDecimal componentScrapQty=Env.ZERO;
                BigDecimal componentQtyReq=Env.ZERO;
                BigDecimal componentQtytoDel=Env.ZERO;
                if (rs.getString(16).equals("Y"))
                    m_QtyPercentage = new Boolean(true);
                else
                    m_QtyPercentage = new Boolean(false);

                if(m_scrapQty == null)
                    m_scrapQty = Env.ZERO;

                if(m_rejectQty == null)
                    m_rejectQty = Env.ZERO;

                if (!m_ListDetail)
                    id.setSelected(true);


                /*
                 * Seteando el check en falso
                 *
                */

                id.setSelected(false);


                issue.setValueAt(id, row, 0);   //  MPC_OrderBOMLine_ID
                //issue.setValueAt(new Integer(rs.getInt(2)), row, 1);//  Line
                System.out.println("***** prduct_id " +rs.getInt(4));



                if (rs.getString(2).equals("Y"))
                    issue.setValueAt(new Boolean(true), row, 1);//  IsCritical
                else
                    issue.setValueAt(new Boolean(false), row, 1);//  IsCritical

                issue.setValueAt(rs.getString(3), row, 2);


                issue.setValueAt(m_productkey , row, 3);             //  Product

                issue.setValueAt(m_uomkey, row, 4);             //  UOM
                //issue.setValueAt(, row , 5);
                //issue.setValueAt(rs.getString(8), row, 5);          //  BackflushGroup

                issue.setValueAt( m_QtyBom , row, 15);          //  QtyBom




                issue.setValueAt(m_QtyPercentage, row, 16); //  isQtyPercentage

                issue.setValueAt(m_QtyBatch, row, 17);          //  QtyBatch
                //System.out.println("QtyBatch actual >>>>>>>>>>>>>>>>:" + m_QtyBatch);
                //m_QtyBatch.divide();

                //int m_mpc_order_id = ((Integer)order.getValue()).intValue();

                issue.setValueAt(m_warehousekey , row, 14);             //  Product
                //   issue.setValueAt(rs.getBigDecimal(9), row, 8);          //  QtyReserved
                issue.setValueAt(rs.getBigDecimal(9), row, 11);          //  QtyReserved
                //  issue.setValueAt(rs.getBigDecimal(10) , row, 9); // OnHand
                issue.setValueAt(m_onhand , row, 12); // OnHand
                //  issue.setValueAt(rs.getBigDecimal(10) , row, 10); // OnHand
                issue.setValueAt(rs.getBigDecimal(11) , row, 10); // Availale
                issue.setValueAt(rs.getBigDecimal(20) , row, 7); // QtyDelivered

                // Put Qty Issue

                // cambio para panalab que tome los almacenes marcados fjviejo e-evolution
                StringBuffer sqlw = new StringBuffer("SELECT sum(bomQtyAvailable(obl.M_Product_ID,w.M_Warehouse_ID, 0 )) AS QtyAvailable , sum(bomQtyOnHand(obl.M_Product_ID,w.M_Warehouse_ID,0)) AS QtyOnHand  FROM MPC_Order_BOMLine obl");
                sqlw.append(" INNER JOIN M_Warehouse w ON (w.isProductionIssue='Y' and obl.AD_Org_ID=w.AD_Org_ID) ");
                sqlw.append(" WHERE obl.MPC_Order_ID = " + order.getValue() +  " and obl.M_Product_ID=" +rs.getInt(4) +" ORDER BY M_Product_ID");
                PreparedStatement pstmtw = DB.prepareStatement(sqlw.toString(),null);
                ResultSet rsw = pstmtw.executeQuery();
                System.out.println("***** suma almacenes sql "+sqlw.toString());
                //
                while (rsw.next()) {
                    System.out.println("***** suma almacenes "+rsw.getBigDecimal(2));
                    issue.setValueAt(rsw.getBigDecimal(1), row, 12);          //  QtyAvailable

                    issue.setValueAt(rsw.getBigDecimal(2) , row, 10); // OnHand
                }
                // cambio para panalab que tome los almacenes marcados fjviejo e-evolution end




                //BigDecimal m_qtyrequiered = rs.getBigDecimal(8);
                //System.out.println("m_ComponentType:"+ m_ComponentType);

                if (m_ComponentType.equals(MMPCProductBOMLine.COMPONENTTYPE_Component) || m_ComponentType.equals(MMPCProductBOMLine.COMPONENTTYPE_Packing)) {
                    // if onhand is > 0 is select


                    /*

                    if(m_onhand.compareTo(Env.ZERO) > 0)
                        id.setSelected(true);
                    else
                        id.setSelected(false);
                    // if QtyRequired is = 0 no select
                    if(rs.getBigDecimal(8).compareTo(Env.ZERO) == 0)
                        id.setSelected(false);

                     */

                    if (m_QtyPercentage.booleanValue()) // Calculate Qty %
                    {

                        //System.out.println("// Calculate Qty %");
                        VNumber viewToDeliverQty = new VNumber();
                        viewToDeliverQty.setDisplayType(DisplayType.Number);

                        if (m_OnlyReceipt && m_IsBackflush) // Calculate Component for Receipt
                        {

                            viewToDeliverQty.setValue(m_toDeliverQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4,BigDecimal.ROUND_HALF_UP)));

                            //   viewtoDeliverQty.setDisplayType(1);
                            componentToDeliverQty = (BigDecimal)viewToDeliverQty.getValue();
                            //componentToDeliverQty = rs.getBigDecimal(19);
                            if (rs.getBigDecimal(8).compareTo(Env.ZERO) == 0)
                                componentToDeliverQty = Env.ZERO;
                            //System.out.println( " componentToDeliverQty" +componentToDeliverQty);
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq =m_toDeliverQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP));
                                componentQtytoDel =componentToDeliverQty.divide(Env.ONE,4,BigDecimal.ROUND_HALF_UP);
                                issue.setValueAt(m_toDeliverQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP)), row, 6); //  QtyRequiered
                                issue.setValueAt(componentToDeliverQty.divide(Env.ONE,8,BigDecimal.ROUND_HALF_UP), row, 8); //  QtyToDelivery

                            }
                        } else // Only Calculate Component not exist Receipt
                        {
                            //viewToDeliverQty.setValue(m_openQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4,BigDecimal.ROUND_HALF_UP)));
                            //   viewtoDeliverQty.setDisplayType(1);
                            //BigDecimal componentToDeliverQty = (BigDecimal)viewToDeliverQty.getValue();
                            componentToDeliverQty = rs.getBigDecimal(19);
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq =m_openQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4,BigDecimal.ROUND_HALF_UP));
                                componentQtytoDel=componentToDeliverQty.divide(Env.ONE,4,BigDecimal.ROUND_HALF_UP);
                                issue.setValueAt(componentToDeliverQty.divide(Env.ONE,8,BigDecimal.ROUND_HALF_UP), row, 8); //  QtyToDelivery
                                issue.setValueAt(m_openQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP)), row, 6); //  QtyRequiered
                            }
                        }



                        if(m_scrapQty.compareTo(Env.ZERO) != 0) {//issue.setValueAt(m_scrapQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4 ,BigDecimal.ROUND_HALF_UP)), row, 7); //  QtyScrap
                            //    issue.setValueAt(m_scrapQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4 ,BigDecimal.ROUND_HALF_UP)), row, 8); //  QtyScrap
                            //BigDecimal vnbd2= m_scrapQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4,BigDecimal.ROUND_HALF_UP));
                            VNumber viewScrapQty = new VNumber();
                            viewScrapQty.setDisplayType(DisplayType.Number);
                            viewScrapQty.setValue(m_scrapQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP)));

                            //   vnm.setDisplayType(1);
                            componentScrapQty = (BigDecimal)viewScrapQty.getValue();
                            if (componentScrapQty.compareTo(Env.ZERO) != 0) {
                                issue.setValueAt(componentScrapQty.divide(Env.ONE,8,BigDecimal.ROUND_HALF_UP), row, 9); //  QtyToDelivery
                            }
                        }
                    } else // Normal Calculate Qty
                    {
                        //System.out.println("Normal Calculate Qty ");
                        VNumber viewToDeliverQty = new VNumber();
                        viewToDeliverQty.setDisplayType(DisplayType.Number);

                        if (m_OnlyReceipt && m_IsBackflush) // Calculate Component for Receipt
                        {
                            viewToDeliverQty.setValue(m_toDeliverQty.multiply(m_QtyBom));

                            //   viewtoDeliverQty.setDisplayType(1);
                            componentToDeliverQty = (BigDecimal)viewToDeliverQty.getValue();
                            //System.out.println( " componentToDeliverQty" +componentToDeliverQty);
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq = m_toDeliverQty.multiply(m_QtyBom);
                                componentQtytoDel=componentToDeliverQty;
                                issue.setValueAt(m_toDeliverQty.multiply(m_QtyBom), row, 6);  //  QtyRequiered
                                issue.setValueAt(componentToDeliverQty, row, 8); //  QtyToDelivery
                            }
                        } // if (m_OnlyReceipt)
                        else {
                            //viewToDeliverQty.setValue(m_openQty.multiply(m_QtyBom));
                            //   viewtoDeliverQty.setDisplayType(1);
                            componentToDeliverQty = rs.getBigDecimal(19);
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq = m_openQty.multiply(m_QtyBom);
                                componentQtytoDel=componentToDeliverQty;
                                issue.setValueAt(m_openQty.multiply(m_QtyBom), row, 6);  //  QtyRequiered
                                issue.setValueAt(componentToDeliverQty, row, 8); //  QtyToDelivery
                            }
                        }


                        if(m_scrapQty.compareTo(Env.ZERO) != 0) {

                            VNumber viewScrapQty = new VNumber();
                            viewScrapQty.setDisplayType(DisplayType.Number);
                            viewScrapQty.setValue(m_scrapQty.multiply(m_QtyBom));

                            //    viewScrapQty.setDisplayType(1);
                            componentScrapQty = (BigDecimal)viewScrapQty.getValue();
                            if (componentScrapQty.compareTo(Env.ZERO) != 0) {
                                issue.setValueAt(componentScrapQty, row, 9); //  QtyToDelivery
                            }
                        }

                    }
                } else if (m_ComponentType.equals(MMPCProductBOMLine.COMPONENTTYPE_Tools)) {
                    System.out.println("***** componenttype " +m_ComponentType);
                    VNumber viewToDeliverQty = new VNumber();
                    viewToDeliverQty.setDisplayType(DisplayType.Number);
                    viewToDeliverQty.setValue(m_QtyBom);

                    //   viewtoDeliverQty.setDisplayType(1);
                    componentToDeliverQty = (BigDecimal)viewToDeliverQty.getValue();
                    if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                        componentQtyReq = m_QtyBom;
                        componentQtytoDel=componentToDeliverQty;
                        issue.setValueAt(m_QtyBom, row, 6);  //  QtyRequiered
                        issue.setValueAt(componentToDeliverQty, row, 8); //  QtyToDelivery
                    }
                }
                row++;

                if(m_ListDetail)
                    row=row+lotes(rs.getInt(4), row, id,rs.getInt(13),componentQtyReq, componentQtytoDel);



                //  prepare next

            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE,"VOrderReceipIssue.executeQuery", e);
        }
        //
        issue.autoSize();
        //	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
    }   //  executeQuery



    /**
     *  VIT4B - Agregado para contemplar todas las instancias de atributos
     *  para el caso de devolucion de material de producci�n
     *
     */

    private void executeQueryReturn() {
        //log.i("VOrderReceiptIssue.executeQuery");
        issue.removeAll();
        issue.setVisible(true);
        boolean m_OnlyReceipt = false;
        boolean m_IsBackflush = false;
        boolean m_ListDetail = false;

        int valueSelected = Integer.parseInt(pickcombo.getValue().toString());

        if (valueSelected==1)
        {
           m_OnlyReceipt = true;
        }

        if (valueSelected==2)
        {
            m_ListDetail = true;
        }

        if (valueSelected==3) {
            m_OnlyReceipt = true;
            m_IsBackflush = true;
            m_ListDetail = true;
        }

        if (valueSelected==4) {
            m_ListDetail = true;
        }





        int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
        StringBuffer sql = new StringBuffer("SELECT obl.MPC_Order_BOMLine_ID , obl.IsCritical , p.Value , obl.M_Product_ID , p.Name , p.C_UOM_ID , u.Name ,");
        sql.append(" obl.QtyRequiered  , obl.QtyReserved , bomQtyAvailable(obl.M_Product_ID,obl.M_Warehouse_ID, 0 ) AS QtyAvailable , bomQtyOnHand(obl.M_Product_ID,obl.M_Warehouse_ID,0) AS QtyOnHand  , p.M_Locator_ID , obl.M_Warehouse_ID , w.Name  , obl.QtyBom , obl.isQtyPercentage , obl.QtyBatch , obl.ComponentType , obl.QtyRequiered - QtyDelivered AS QtyOpen, obl.QtyDelivered FROM MPC_Order_BOMLine obl");
        sql.append(" INNER JOIN M_Product p ON (obl.M_Product_ID = p.M_Product_ID) ");
        sql.append(" INNER JOIN C_UOM u ON (p.C_UOM_ID = u.C_UOM_ID) ");
        sql.append(" INNER JOIN M_Warehouse w ON (w.M_Warehouse_ID = obl.M_Warehouse_ID) ");
        sql.append(" WHERE obl.MPC_Order_ID = " + order.getValue() + " ORDER BY bomQtyOnHand(obl.M_Product_ID,obl.M_Warehouse_ID,0) " );


        log.log(Level.INFO, "VOrderReciptIssue.executeQuery - SQL", sql.toString());
        //  reset table
        int row = 0;
        issue.setRowCount(row);
        //  Execute
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql.toString(),null);
            //pstmt.setInt(1, AD_Client_ID);
            ResultSet rs = pstmt.executeQuery();
            //
            while (rs.next()) {

                //  extend table
                issue.setRowCount(row+1);
                //  set values
                //issue.
                IDColumn id = new IDColumn(rs.getInt(1));
                KeyNamePair m_productkey = new KeyNamePair(rs.getInt(4) , rs.getString(5));
                KeyNamePair m_uomkey = new KeyNamePair(rs.getInt(6),rs.getString(7));
                BigDecimal m_QtyBom = rs.getBigDecimal(15);
                Boolean m_QtyPercentage = null;
                BigDecimal m_QtyBatch = rs.getBigDecimal(17);
                String m_ComponentType = rs.getString(18);
                // fjviejo e-evolution combo
                //boolean m_OnlyReceipt= ((Boolean)OnlyReceipt.getValue()).booleanValue();
                //boolean m_IsBackflush = ((Boolean)IsBackflush.getValue()).booleanValue();
                // fjviejo e-evolution combo end
                KeyNamePair m_warehousekey = new KeyNamePair(rs.getInt(13),rs.getString(14));
                BigDecimal m_onhand = rs.getBigDecimal(10);
                BigDecimal m_toDeliverQty = (BigDecimal)toDeliverQty.getValue();
                BigDecimal m_openQty = (BigDecimal)openQty.getValue();
                BigDecimal m_scrapQty = (BigDecimal)scrapQty.getValue();
                BigDecimal m_rejectQty = (BigDecimal)rejectQty.getValue();
                BigDecimal componentToDeliverQty =Env.ZERO;
                BigDecimal componentScrapQty=Env.ZERO;
                BigDecimal componentQtyReq=Env.ZERO;
                BigDecimal componentQtytoDel=Env.ZERO;
                if (rs.getString(16).equals("Y"))
                    m_QtyPercentage = new Boolean(true);
                else
                    m_QtyPercentage = new Boolean(false);

                if(m_scrapQty == null)
                    m_scrapQty = Env.ZERO;

                if(m_rejectQty == null)
                    m_rejectQty = Env.ZERO;

                if (!m_ListDetail)
                    id.setSelected(true);


                /*
                 * Seteando el check en falso
                 *
                */

                id.setSelected(false);


                issue.setValueAt(id, row, 0);   //  MPC_OrderBOMLine_ID
                //issue.setValueAt(new Integer(rs.getInt(2)), row, 1);//  Line
                System.out.println("***** prduct_id " +rs.getInt(4));



                if (rs.getString(2).equals("Y"))
                    issue.setValueAt(new Boolean(true), row, 1);//  IsCritical
                else
                    issue.setValueAt(new Boolean(false), row, 1);//  IsCritical

                issue.setValueAt(rs.getString(3), row, 2);


                issue.setValueAt(m_productkey , row, 3);             //  Product

                issue.setValueAt(m_uomkey, row, 4);             //  UOM
                //issue.setValueAt(, row , 5);
                //issue.setValueAt(rs.getString(8), row, 5);          //  BackflushGroup

                issue.setValueAt( m_QtyBom , row, 15);          //  QtyBom




                issue.setValueAt(m_QtyPercentage, row, 16); //  isQtyPercentage

                issue.setValueAt(m_QtyBatch, row, 17);          //  QtyBatch
                //System.out.println("QtyBatch actual >>>>>>>>>>>>>>>>:" + m_QtyBatch);
                //m_QtyBatch.divide();

                //int m_mpc_order_id = ((Integer)order.getValue()).intValue();

                issue.setValueAt(m_warehousekey , row, 14);             //  Product
                //   issue.setValueAt(rs.getBigDecimal(9), row, 8);          //  QtyReserved
                issue.setValueAt(rs.getBigDecimal(9), row, 11);          //  QtyReserved
                //  issue.setValueAt(rs.getBigDecimal(10) , row, 9); // OnHand
                issue.setValueAt(m_onhand , row, 12); // OnHand
                //  issue.setValueAt(rs.getBigDecimal(10) , row, 10); // OnHand
                issue.setValueAt(rs.getBigDecimal(11) , row, 10); // Availale
                issue.setValueAt(rs.getBigDecimal(20) , row, 7); // QtyDelivered

                // Put Qty Issue

                // cambio para panalab que tome los almacenes marcados fjviejo e-evolution
                StringBuffer sqlw = new StringBuffer("SELECT sum(bomQtyAvailable(obl.M_Product_ID,w.M_Warehouse_ID, 0 )) AS QtyAvailable , sum(bomQtyOnHand(obl.M_Product_ID,w.M_Warehouse_ID,0)) AS QtyOnHand  FROM MPC_Order_BOMLine obl");
                sqlw.append(" INNER JOIN M_Warehouse w ON (w.isProductionIssue='Y' and obl.AD_Org_ID=w.AD_Org_ID) ");
                sqlw.append(" WHERE obl.MPC_Order_ID = " + order.getValue() +  " and obl.M_Product_ID=" +rs.getInt(4) +" ORDER BY M_Product_ID");
                PreparedStatement pstmtw = DB.prepareStatement(sqlw.toString(),null);
                ResultSet rsw = pstmtw.executeQuery();
                System.out.println("***** suma almacenes sql "+sqlw.toString());
                //
                while (rsw.next()) {
                    System.out.println("***** suma almacenes "+rsw.getBigDecimal(2));
                    issue.setValueAt(rsw.getBigDecimal(1), row, 12);          //  QtyAvailable

                    issue.setValueAt(rsw.getBigDecimal(2) , row, 10); // OnHand
                }
                // cambio para panalab que tome los almacenes marcados fjviejo e-evolution end




                //BigDecimal m_qtyrequiered = rs.getBigDecimal(8);
                //System.out.println("m_ComponentType:"+ m_ComponentType);

                if (m_ComponentType.equals(MMPCProductBOMLine.COMPONENTTYPE_Component) || m_ComponentType.equals(MMPCProductBOMLine.COMPONENTTYPE_Packing)) {
                    // if onhand is > 0 is select


                    /*

                    if(m_onhand.compareTo(Env.ZERO) > 0)
                        id.setSelected(true);
                    else
                        id.setSelected(false);
                    // if QtyRequired is = 0 no select
                    if(rs.getBigDecimal(8).compareTo(Env.ZERO) == 0)
                        id.setSelected(false);

                     */

                    if (m_QtyPercentage.booleanValue()) // Calculate Qty %
                    {

                        //System.out.println("// Calculate Qty %");
                        VNumber viewToDeliverQty = new VNumber();
                        viewToDeliverQty.setDisplayType(DisplayType.Number);

                        if (m_OnlyReceipt && m_IsBackflush) // Calculate Component for Receipt
                        {

                            viewToDeliverQty.setValue(m_toDeliverQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4,BigDecimal.ROUND_HALF_UP)));

                            //   viewtoDeliverQty.setDisplayType(1);
                            componentToDeliverQty = (BigDecimal)viewToDeliverQty.getValue();
                            //componentToDeliverQty = rs.getBigDecimal(19);
                            if (rs.getBigDecimal(8).compareTo(Env.ZERO) == 0)
                                componentToDeliverQty = Env.ZERO;
                            //System.out.println( " componentToDeliverQty" +componentToDeliverQty);
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq =m_toDeliverQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP));
                                componentQtytoDel =componentToDeliverQty.divide(Env.ONE,4,BigDecimal.ROUND_HALF_UP);
                                issue.setValueAt(m_toDeliverQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP)), row, 6); //  QtyRequiered
                                issue.setValueAt(componentToDeliverQty.divide(Env.ONE,8,BigDecimal.ROUND_HALF_UP), row, 8); //  QtyToDelivery

                            }
                        } else // Only Calculate Component not exist Receipt
                        {
                            //viewToDeliverQty.setValue(m_openQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4,BigDecimal.ROUND_HALF_UP)));
                            //   viewtoDeliverQty.setDisplayType(1);
                            //BigDecimal componentToDeliverQty = (BigDecimal)viewToDeliverQty.getValue();
                            componentToDeliverQty = rs.getBigDecimal(19);
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq =m_openQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4,BigDecimal.ROUND_HALF_UP));
                                componentQtytoDel=componentToDeliverQty.divide(Env.ONE,4,BigDecimal.ROUND_HALF_UP);
                                issue.setValueAt(componentToDeliverQty.divide(Env.ONE,8,BigDecimal.ROUND_HALF_UP), row, 8); //  QtyToDelivery
                                issue.setValueAt(m_openQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP)), row, 6); //  QtyRequiered
                            }
                        }



                        if(m_scrapQty.compareTo(Env.ZERO) != 0) {//issue.setValueAt(m_scrapQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4 ,BigDecimal.ROUND_HALF_UP)), row, 7); //  QtyScrap
                            //    issue.setValueAt(m_scrapQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4 ,BigDecimal.ROUND_HALF_UP)), row, 8); //  QtyScrap
                            //BigDecimal vnbd2= m_scrapQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4,BigDecimal.ROUND_HALF_UP));
                            VNumber viewScrapQty = new VNumber();
                            viewScrapQty.setDisplayType(DisplayType.Number);
                            viewScrapQty.setValue(m_scrapQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP)));

                            //   vnm.setDisplayType(1);
                            componentScrapQty = (BigDecimal)viewScrapQty.getValue();
                            if (componentScrapQty.compareTo(Env.ZERO) != 0) {
                                issue.setValueAt(componentScrapQty.divide(Env.ONE,8,BigDecimal.ROUND_HALF_UP), row, 9); //  QtyToDelivery
                            }
                        }
                    } else // Normal Calculate Qty
                    {
                        //System.out.println("Normal Calculate Qty ");
                        VNumber viewToDeliverQty = new VNumber();
                        viewToDeliverQty.setDisplayType(DisplayType.Number);

                        if (m_OnlyReceipt && m_IsBackflush) // Calculate Component for Receipt
                        {
                            viewToDeliverQty.setValue(m_toDeliverQty.multiply(m_QtyBom));

                            //   viewtoDeliverQty.setDisplayType(1);
                            componentToDeliverQty = (BigDecimal)viewToDeliverQty.getValue();
                            //System.out.println( " componentToDeliverQty" +componentToDeliverQty);
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq = m_toDeliverQty.multiply(m_QtyBom);
                                componentQtytoDel=componentToDeliverQty;
                                issue.setValueAt(m_toDeliverQty.multiply(m_QtyBom), row, 6);  //  QtyRequiered
                                issue.setValueAt(componentToDeliverQty, row, 8); //  QtyToDelivery
                            }
                        } // if (m_OnlyReceipt)
                        else {
                            //viewToDeliverQty.setValue(m_openQty.multiply(m_QtyBom));
                            //   viewtoDeliverQty.setDisplayType(1);
                            componentToDeliverQty = rs.getBigDecimal(19);
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq = m_openQty.multiply(m_QtyBom);
                                componentQtytoDel=componentToDeliverQty;
                                issue.setValueAt(m_openQty.multiply(m_QtyBom), row, 6);  //  QtyRequiered
                                issue.setValueAt(componentToDeliverQty, row, 8); //  QtyToDelivery
                            }
                        }


                        if(m_scrapQty.compareTo(Env.ZERO) != 0) {

                            VNumber viewScrapQty = new VNumber();
                            viewScrapQty.setDisplayType(DisplayType.Number);
                            viewScrapQty.setValue(m_scrapQty.multiply(m_QtyBom));

                            //    viewScrapQty.setDisplayType(1);
                            componentScrapQty = (BigDecimal)viewScrapQty.getValue();
                            if (componentScrapQty.compareTo(Env.ZERO) != 0) {
                                issue.setValueAt(componentScrapQty, row, 9); //  QtyToDelivery
                            }
                        }

                    }
                } else if (m_ComponentType.equals(MMPCProductBOMLine.COMPONENTTYPE_Tools)) {
                    System.out.println("***** componenttype " +m_ComponentType);
                    VNumber viewToDeliverQty = new VNumber();
                    viewToDeliverQty.setDisplayType(DisplayType.Number);
                    viewToDeliverQty.setValue(m_QtyBom);

                    //   viewtoDeliverQty.setDisplayType(1);
                    componentToDeliverQty = (BigDecimal)viewToDeliverQty.getValue();
                    if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                        componentQtyReq = m_QtyBom;
                        componentQtytoDel=componentToDeliverQty;
                        issue.setValueAt(m_QtyBom, row, 6);  //  QtyRequiered
                        issue.setValueAt(componentToDeliverQty, row, 8); //  QtyToDelivery
                    }
                }
                row++;

                if(m_ListDetail)
                    row=row+lotesReturn(rs.getInt(4), row, id,rs.getInt(13),componentQtyReq, componentQtytoDel);



                //  prepare next

            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE,"VOrderReceipIssue.executeQuery", e);
        }
        //
        issue.autoSize();
        //	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
    }   //  executeQuery


        private int lotes(int M_Product_ID, int row, IDColumn id, int Warehouse_ID, BigDecimal qtyreq, BigDecimal qtytodel) {
        int haslot =0;
        BigDecimal reql = Env.ZERO;
        BigDecimal todeliveryl = Env.ZERO;
        reql=qtyreq;
        todeliveryl=qtytodel;
        StringBuffer sql = new StringBuffer("SELECT s.M_Product_ID , s.QtyOnHand, s.M_AttributeSetInstance_ID, p.Name, masi.Description, l.Value, w.Value, w.M_warehouse_ID,p.Value ");
        sql.append("  FROM M_Storage s ");
        sql.append(" INNER JOIN M_Product p ON (s.M_Product_ID = p.M_Product_ID) ");
        sql.append(" INNER JOIN C_UOM u ON (u.C_UOM_ID = p.C_UOM_ID) ");
        sql.append(" INNER JOIN M_AttributeSetInstance masi ON (masi.M_AttributeSetInstance_ID = s.M_AttributeSetInstance_ID) ");
        // sql.append(" INNER JOIN M_Warehouse w ON (w.M_Warehouse_ID = " +Warehouse_ID +") "); //cambio para que lo tome de diferentes lotes fjviejo panalab
        // sql.append(" Inner Join M_Locator l ON(l.M_Warehouse_ID=w.M_Warehouse_ID and s.M_Locator_ID=l.M_Locator_ID) "); //cambio para que lo tome de diferentes lotes fjviejo panalab
        sql.append("  Inner Join M_Locator l ON(s.M_Locator_ID=l.M_Locator_ID) ");
        sql.append(" INNER JOIN M_Warehouse w ON (w.M_Warehouse_ID = l.M_Warehouse_ID and w.isproductionissue='Y' and w.AD_Org_ID=" +m_AD_Org_ID +")");
        //cambio para que lo tome de diferentes lotes fjviejo panalab end
        sql.append(" WHERE s.M_Product_ID = " +M_Product_ID + " and s.QtyOnHand <> 0 Order by s.Created " );

        log.log(Level.INFO, "VOrderReciptIssue.executeQuery - SQL", sql.toString());
        //  reset table
        //  Execute
        try {
            PreparedStatement pstmt1 = DB.prepareStatement(sql.toString(),null);
            //pstmt.setInt(1, AD_Client_ID);
            ResultSet rs1 = pstmt1.executeQuery();
            //
            while (rs1.next()) {
                Color colour = new Color(5,5,50);

                issue.setRowCount(row+1);

                //    issue.setBackground(colour);
//                            issue.setGridColor(colour);

                IDColumn id1 = new IDColumn(rs1.getInt(3));
                id1.setSelected(false);
                issue.setRowSelectionAllowed(true);

                /*
                 * Seteando el check en falso
                 *
                */

                id1.setSelected(false);

                issue.setValueAt(id1, row, 0);   //  MPC_OrderBOMLine_ID
                KeyNamePair m_productkey = new KeyNamePair(rs1.getInt(1) , rs1.getString(4));
                issue.setValueAt(m_productkey , row, 3);
                issue.setValueAt(rs1.getBigDecimal(2) , row, 10); // Onhand

                issue.setValueAt(rs1.getString(5) , row, 5); //attribute
                issue.setValueAt(rs1.getString(6),row,13);
                KeyNamePair m_warehousekey = new KeyNamePair(rs1.getInt(8),rs1.getString(7));

                issue.setValueAt(m_warehousekey , row, 14);
                if (reql.compareTo(rs1.getBigDecimal(2)) <0) {
                    if (reql.compareTo(Env.ZERO)<=0)
                        issue.setValueAt(Env.ZERO , row, 6);
                    else
                        issue.setValueAt(reql , row, 6);
                    reql=reql.subtract(rs1.getBigDecimal(2));
                } else {

                    issue.setValueAt(rs1.getBigDecimal(2) , row, 6);
                    reql=reql.subtract(rs1.getBigDecimal(2));
                }

//                             if (todeliveryl.compareTo(rs1.getBigDecimal(2)) <0)
//                             {
//                                 if (todeliveryl.compareTo(Env.ZERO)<=0)
//                                     issue.setValueAt(Env.ZERO , row, 7);
//                                 else
//                                    issue.setValueAt(todeliveryl , row, 7);
//                                 todeliveryl=todeliveryl.subtract(rs1.getBigDecimal(2));
//                             }
//                             else
//                             {
//
//                                issue.setValueAt(rs1.getBigDecimal(2) , row, 7);
//                                 todeliveryl=todeliveryl.subtract(rs1.getBigDecimal(2));
//                             }

                haslot++;
                row++;
            }
            rs1.close();
            pstmt1.close();
        } catch (SQLException el) {
        }
        return haslot;
    }

    /**
     *  VIT4B - Agregado para contemplar todas las instancias de atributos
     *  para el caso de devolucion de material de producci�n
     *
     */

    private int lotesReturn(int M_Product_ID, int row, IDColumn id, int Warehouse_ID, BigDecimal qtyreq, BigDecimal qtytodel) {
        int haslot =0;
        BigDecimal reql = Env.ZERO;
        BigDecimal todeliveryl = Env.ZERO;
        reql=qtyreq;
        todeliveryl=qtytodel;
        StringBuffer sql = new StringBuffer("SELECT s.M_Product_ID , s.QtyOnHand, s.M_AttributeSetInstance_ID, p.Name, masi.Description, l.Value, w.Value, w.M_warehouse_ID,p.Value ");
        sql.append("  FROM M_Storage s ");
        sql.append(" INNER JOIN M_Product p ON (s.M_Product_ID = p.M_Product_ID) ");
        sql.append(" INNER JOIN C_UOM u ON (u.C_UOM_ID = p.C_UOM_ID) ");
        sql.append(" INNER JOIN M_AttributeSetInstance masi ON (masi.M_AttributeSetInstance_ID = s.M_AttributeSetInstance_ID) ");
        // sql.append(" INNER JOIN M_Warehouse w ON (w.M_Warehouse_ID = " +Warehouse_ID +") "); //cambio para que lo tome de diferentes lotes fjviejo panalab
        // sql.append(" Inner Join M_Locator l ON(l.M_Warehouse_ID=w.M_Warehouse_ID and s.M_Locator_ID=l.M_Locator_ID) "); //cambio para que lo tome de diferentes lotes fjviejo panalab
        sql.append("  Inner Join M_Locator l ON(s.M_Locator_ID=l.M_Locator_ID) ");
        sql.append(" INNER JOIN M_Warehouse w ON (w.M_Warehouse_ID = l.M_Warehouse_ID and w.isproductionissue='Y' and w.AD_Org_ID=" +m_AD_Org_ID +")");
        //cambio para que lo tome de diferentes lotes fjviejo panalab end
        sql.append(" WHERE s.M_Product_ID = " +M_Product_ID + " AND s.M_AttributeSetInstance_ID <> 0 Order by s.Created " );

        log.log(Level.INFO, "VOrderReciptIssue.executeQuery - SQL", sql.toString());
        //  reset table
        //  Execute
        try {
            PreparedStatement pstmt1 = DB.prepareStatement(sql.toString(),null);
            //pstmt.setInt(1, AD_Client_ID);
            ResultSet rs1 = pstmt1.executeQuery();
            //
            while (rs1.next()) {
                Color colour = new Color(5,5,50);

                issue.setRowCount(row+1);

                //    issue.setBackground(colour);
//                            issue.setGridColor(colour);

                IDColumn id1 = new IDColumn(rs1.getInt(3));
                id1.setSelected(false);
                issue.setRowSelectionAllowed(true);

                /*
                 * Seteando el check en falso
                 *
                */

                id1.setSelected(false);

                issue.setValueAt(id1, row, 0);   //  MPC_OrderBOMLine_ID
                KeyNamePair m_productkey = new KeyNamePair(rs1.getInt(1) , rs1.getString(4));
                issue.setValueAt(m_productkey , row, 3);
                issue.setValueAt(rs1.getBigDecimal(2) , row, 10); // Onhand

                issue.setValueAt(rs1.getString(5) , row, 5); //attribute
                issue.setValueAt(rs1.getString(6),row,13);
                KeyNamePair m_warehousekey = new KeyNamePair(rs1.getInt(8),rs1.getString(7));

                issue.setValueAt(m_warehousekey , row, 14);
                if (reql.compareTo(rs1.getBigDecimal(2)) <0) {
                    if (reql.compareTo(Env.ZERO)<=0)
                        issue.setValueAt(Env.ZERO , row, 6);
                    else
                        issue.setValueAt(reql , row, 6);
                    reql=reql.subtract(rs1.getBigDecimal(2));
                } else {

                    issue.setValueAt(rs1.getBigDecimal(2) , row, 6);
                    reql=reql.subtract(rs1.getBigDecimal(2));
                }

//                             if (todeliveryl.compareTo(rs1.getBigDecimal(2)) <0)
//                             {
//                                 if (todeliveryl.compareTo(Env.ZERO)<=0)
//                                     issue.setValueAt(Env.ZERO , row, 7);
//                                 else
//                                    issue.setValueAt(todeliveryl , row, 7);
//                                 todeliveryl=todeliveryl.subtract(rs1.getBigDecimal(2));
//                             }
//                             else
//                             {
//
//                                issue.setValueAt(rs1.getBigDecimal(2) , row, 7);
//                                 todeliveryl=todeliveryl.subtract(rs1.getBigDecimal(2));
//                             }

                haslot++;
                row++;
            }
            rs1.close();
            pstmt1.close();
        } catch (SQLException el) {
        }
        return haslot;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    /*
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        mainPanel = new javax.swing.JPanel();
        TabsReceiptsIssue = new javax.swing.JTabbedPane();
        ReceiptIssueOrder = new javax.swing.JPanel();
        PanelCenter = new javax.swing.JPanel();
        issueScrollPane = new javax.swing.JScrollPane();
        issue = new javax.swing.JTable();
        PanelBottom = new javax.swing.JPanel();
        Process = new javax.swing.JButton();
        northPanel = new javax.swing.JPanel();
        orgLabel = new javax.swing.JLabel();
        org = new javax.swing.JTextField();
        resourceLabel = new javax.swing.JLabel();
        resource = new javax.swing.JTextField();
        orderLabel = new javax.swing.JLabel();
        order = new javax.swing.JTextField();
        movementDateLabel = new javax.swing.JLabel();
        movementDate = new javax.swing.JTextField();
        deliveryQtyLabel = new javax.swing.JLabel();
        deliveryQty = new javax.swing.JTextField();
        scrapQtyLabel = new javax.swing.JLabel();
        scrapQty = new javax.swing.JTextField();
        rejectQtyLabel = new javax.swing.JLabel();
        rejectQty = new javax.swing.JTextField();
        attributeLabel = new javax.swing.JLabel();
        attribute = new javax.swing.JTextField();
        issueMethodLabel = new javax.swing.JLabel();
        issueMethod = new javax.swing.JTextField();
        Generate = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        mainPanel.setLayout(new java.awt.BorderLayout());

        ReceiptIssueOrder.setLayout(new java.awt.BorderLayout());

        PanelCenter.setLayout(new java.awt.BorderLayout());

        issueScrollPane.setBorder(new javax.swing.border.TitledBorder(""));
        issue.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        issueScrollPane.setViewportView(issue);

        PanelCenter.add(issueScrollPane, java.awt.BorderLayout.NORTH);

        ReceiptIssueOrder.add(PanelCenter, java.awt.BorderLayout.CENTER);

        Process.setText("Process");
        Process.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProcessActionPerformed(evt);
            }
        });

        PanelBottom.add(Process);

        ReceiptIssueOrder.add(PanelBottom, java.awt.BorderLayout.SOUTH);

        northPanel.setLayout(new java.awt.GridBagLayout());

        orgLabel.setText("Org");
        orgLabel.setAlignmentY(0.0F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 5, 0);
        northPanel.add(orgLabel, gridBagConstraints);

        org.setText("                                 ");
        org.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orgActionPerformed(evt);
            }
        });

        northPanel.add(org, new java.awt.GridBagConstraints());

        resourceLabel.setText("Plant");
        northPanel.add(resourceLabel, new java.awt.GridBagConstraints());

        resource.setText("                         ");
        northPanel.add(resource, new java.awt.GridBagConstraints());

        orderLabel.setText("Order");
        northPanel.add(orderLabel, new java.awt.GridBagConstraints());

        order.setText("                       ");
        order.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderActionPerformed(evt);
            }
        });

        northPanel.add(order, new java.awt.GridBagConstraints());

        movementDateLabel.setText("Movement Date");
        northPanel.add(movementDateLabel, new java.awt.GridBagConstraints());

        movementDate.setText("jTextField4");
        northPanel.add(movementDate, new java.awt.GridBagConstraints());

        deliveryQtyLabel.setText("Delivery Qty");
        northPanel.add(deliveryQtyLabel, new java.awt.GridBagConstraints());

        deliveryQty.setText("jTextField5");
        northPanel.add(deliveryQty, new java.awt.GridBagConstraints());

        scrapQtyLabel.setText("Scrap Qty");
        northPanel.add(scrapQtyLabel, new java.awt.GridBagConstraints());

        scrapQty.setText("jTextField6");
        northPanel.add(scrapQty, new java.awt.GridBagConstraints());

        rejectQtyLabel.setText("Reject");
        northPanel.add(rejectQtyLabel, new java.awt.GridBagConstraints());

        rejectQty.setText("jTextField7");
        northPanel.add(rejectQty, new java.awt.GridBagConstraints());

        attributeLabel.setText("Instance");
        northPanel.add(attributeLabel, new java.awt.GridBagConstraints());

        attribute.setText("jTextField8");
        northPanel.add(attribute, new java.awt.GridBagConstraints());

        issueMethodLabel.setText("Issue Method");
        northPanel.add(issueMethodLabel, new java.awt.GridBagConstraints());

        issueMethod.setText("jTextField9");
        northPanel.add(issueMethod, new java.awt.GridBagConstraints());

        ReceiptIssueOrder.add(northPanel, java.awt.BorderLayout.NORTH);

        TabsReceiptsIssue.addTab("Receipts & Issue Order", ReceiptIssueOrder);

        TabsReceiptsIssue.addTab("Generate", Generate);

        mainPanel.add(TabsReceiptsIssue, java.awt.BorderLayout.CENTER);

        add(mainPanel, java.awt.BorderLayout.NORTH);

    }//GEN-END:initComponents
     */
        /*
    private void orderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderActionPerformed
          // TODO add your handling code here:
    }//GEN-LAST:event_orderActionPerformed


    private void ProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProcessActionPerformed


    private void orgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_orgActionPerformed
         */
    public void actionPerformed(ActionEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //fjviejo e-evolution combo
        boolean m_OnlyReceipt = false;
        boolean m_IsBackflush = false;
        boolean m_ListDetail = false;

        boolean m_Return = false;



        /*
         *  Vit4B - Modificado para que al seleccionar entrega de
         *  materiales no muestre la instancia de atributos del
         *  producto de la orden.
         *
         */


        int valueSelected = Integer.parseInt(pickcombo.getValue().toString());

        if (valueSelected==1)
        {
            attribute.setEnabled(true);
            m_OnlyReceipt = true;
        }

        if (valueSelected==2)
        {
            attribute.setEnabled(false);
            m_ListDetail = true;
        }

        if (valueSelected==3) {
            m_OnlyReceipt = true;
            m_IsBackflush = true;
            attribute.setEnabled(true);
        }

        if (valueSelected==4) {
            m_Return = true;
            attribute.setEnabled(false);
            m_ListDetail = true;
        }

//        boolean m_IsBackflush = ((Boolean)IsBackflush.getValue()).booleanValue();
//        boolean m_ListDetail = ((Boolean)ListDetail.getValue()).booleanValue();
        //fjviejo e-evolution combo
        log.fine("VOrderReceiptIssue.actionPerformed Evet:" + e.getSource());
        if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL)) {
            dispose();
            return;
        }
        //	Product Attribute Search
        if (e.getSource().equals(Process)) {
            if(this.ordernumber.equals(""))
            {
                JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Operacion invalida por Orden no Aprobada"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                return;
            }
                    /*if (cmd_process())
                    {
                    dispose();
                    }*/
        }


        if (e.getSource().equals(issueMethod)) {
            if (issueMethod.getValue() != null ) {
                //System.out.println("issueMethod.getValue()" + issueMethod.getValue());
                cmd_search();
            }
        }

        //cmd_search();
        if (e.getSource() == issueMethod) {
            if (issueMethod.getValue() != null ) {
                cmd_search();
            }
        }


        if (e.getSource() == toDeliverQty || e.getSource() == scrapQty) {
            if (order.getValue() != null && m_IsBackflush) {
                executeQuery();
            }

        }

        if (e.getSource()== pickcombo) {
            //issue.setVisible(true);
            //     boolean m_OnlyReceipt = ((Boolean)value).booleanValue();

            if (valueSelected==1){
                m_OnlyReceipt = true;
            }
            else{
                fechaVencimiento.setVisible(false);
                labelVencimiento.setVisible(false);
                m_OnlyReceipt = false;
            }
            if (m_OnlyReceipt) {
                enableToDeliver();
                issue.setVisible(false);
            } else {
                disableToDeliver();

            }

            if (valueSelected==3)
                m_IsBackflush = true;
            else
                m_IsBackflush = false;
            if (m_IsBackflush) {
                executeQuery();
                enableToDeliver();
            }


            if (valueSelected==2)
                m_ListDetail = true;
            else
                m_ListDetail = false;

            if (m_ListDetail) {
                executeQuery();
            }


            if (valueSelected==4)
            {
                m_ListDetail = true;
                m_Return = true;
            }
            else
            {
                m_ListDetail = false;
                m_Return = false;
            }

            if (m_ListDetail && m_Return) {
                executeQueryReturn();
            }

        }


        //setCursor(Cursor.getDefaultCursor());
    }

    public void dispose() {

        if (m_frame != null)
            m_frame.dispose();
        m_frame = null;
    }

    public void executeASync(org.compiere.process.ProcessInfo processInfo) {
    }

    public boolean isUILocked() {
        return true;
    }

    public void lockUI(org.compiere.process.ProcessInfo processInfo) {
    }

    public void stateChanged(ChangeEvent e) {
    }

    public void tableChanged(TableModelEvent e) {
      /*
       int row = issue.getSelectedRow();
       int col = e.getColumn();

       //System.out.print("Prueba col:" + e.getColumn() +  " row:" +  issue.getSelectedRow());
       //System.out.print("Prueba jejejejjejejejejejjejejejeje" + e.getType());
       //System.out.println("Evento" +  e.getSource() + e.toString());
       //TableModelEvent.INSERT

       if (e.getType() == TableModelEvent.INSERT && row != -1 && col == 6)
       {
           VNumber n = new VNumber();
           //issue.setValueAt(n, row , 6 );
       }*/
    }

    public void unlockUI(org.compiere.process.ProcessInfo processInfo) {
    }

    public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {

        boolean m_OnlyReceipt = false;
        boolean m_IsBackflush = false;
        boolean m_ListDetail = false;

        int valueSelected = Integer.parseInt(pickcombo.getValue().toString());

        if (valueSelected==1)
        {
           m_OnlyReceipt = true;
        }

        if (valueSelected==2)
        {
            m_ListDetail = true;
        }

        if (valueSelected==3) {
            m_OnlyReceipt = true;
            m_IsBackflush = true;
        }

        if (valueSelected==4) {
            m_ListDetail = true;
        }



//                boolean m_OnlyReceipt = ((Boolean)OnlyReceipt.getValue()).booleanValue();
//        boolean m_IsBackflush = ((Boolean)IsBackFlush.getValue()).booleanValue();
//        boolean m_ListDetail = ((Boolean)ListDetail.getValue()).booleanValue();
        String name = e.getPropertyName();
        Object value = e.getNewValue();
        log.fine( "VOrderReceip.vetoableChange - " + name + "=" + value);
        if (value == null)
            return;

        //  MPC_Order_ID
        if (name.equals("MPC_Order_ID")) {
            order.setValue(value);

            if (order.getValue() != null) {

                        /*
                         **  ACA DEBERIA DE VERIFICAR SI LA ORDEN TIENE ESTADO COMPLETA SINO DEBERIA DE TIRAR UNA VENTANITA DE
                         **  AVISO Y NO AVANZAR.

                         **  OJO !!!!!!! 08/03/2007

                         **
                         */

                String  conultStatus = "SELECT * FROM MPC_Order WHERE MPC_Order_ID = " + order.getValue() + " AND MPC_Order.DocStatus = 'CO'";
                PreparedStatement pstmtStatus = DB.prepareStatement(conultStatus,null);
                ResultSet rsStatus = null;
                try {
                    rsStatus = pstmtStatus.executeQuery();

                    if (!rsStatus.next()) {

                        JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Operaci�n inv�lida por Orden no Aprobada"), "Info" , JOptionPane.INFORMATION_MESSAGE);

                        this.ordernumber = "";

                        Language language = Language.getLoginLanguage();
                        MLookup orderL = null;
                        try {
                            orderL = MLookupFactory.get(Env.getCtx(), m_WindowNo, 1000181, DisplayType.Search, language, "MPC_Order_ID", 0, false, "MPC_Order.DocStatus = '" + MMPCOrder.DOCSTATUS_Completed + "'");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        order = new VLookup("MPC_Order_ID", false, false, true, orderL);
                        order.repaint();
                        order.setBackground(CompierePLAF.getInfoBackground());

                        Integer MPC_Order_ID = (Integer)order.getValue();
                        m_mpc_order = new MMPCOrder(Env.getCtx(),0,null);

                        resource.setValue(new Integer(m_mpc_order.getS_Resource_ID()));	//	display value
                        warehouse.setValue(new Integer(m_mpc_order.getM_Warehouse_ID()));	//	display value
                        deliveredQty.setValue(m_mpc_order.getQtyDelivered());
                        orderedQty.setValue(m_mpc_order.getQtyEntered());
                        m_mpc_order.getQtyOrdered().subtract(m_mpc_order.getQtyDelivered());
                        qtyBatchs.setValue(m_mpc_order.getQtyBatchs());
                        qtyBatchSize.setValue(m_mpc_order.getQtyBatchSize());
                        //openQty.setValue(m_mpc_order.getQtyDelivered().subtract(m_mpc_order.getQtyOrdered()));
                        openQty.setValue(m_mpc_order.getQtyEntered().subtract(m_mpc_order.getQtyDelivered()));
                        toDeliverQty.setValue(openQty.getValue());
                        product.setValue(new Integer(m_mpc_order.getM_Product_ID()));
                        MProduct m_product =  new MProduct(Env.getCtx(),m_mpc_order.getM_Product_ID(),null);
                        uom.setValue(new Integer(m_product.getC_UOM_ID()));
                        uomorder.setValue(new Integer(m_mpc_order.getC_UOM_ID()));
                        Integer m_product_id = (Integer)product.getValue();
                        Env.setContext(Env.getCtx(), m_WindowNo ,"M_Product_ID",m_product_id.intValue());
                        attribute.setValue(new Integer(m_mpc_order.getM_AttributeSetInstance_ID()));




                        this.disableToDeliver();
                        executeQuery();


                        return;
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


                this.ordernumber = order.getValue().toString();
                Integer MPC_Order_ID = (Integer)order.getValue();
                m_mpc_order = new MMPCOrder(Env.getCtx(),MPC_Order_ID.intValue(),null);
                resource.setValue(new Integer(m_mpc_order.getS_Resource_ID()));	//	display value
                warehouse.setValue(new Integer(m_mpc_order.getM_Warehouse_ID()));	//	display value
                deliveredQty.setValue(m_mpc_order.getQtyDelivered());
                orderedQty.setValue(m_mpc_order.getQtyEntered());
                m_mpc_order.getQtyOrdered().subtract(m_mpc_order.getQtyDelivered());
                qtyBatchs.setValue(m_mpc_order.getQtyBatchs());
                qtyBatchSize.setValue(m_mpc_order.getQtyBatchSize());
                //openQty.setValue(m_mpc_order.getQtyDelivered().subtract(m_mpc_order.getQtyOrdered()));
                if (m_mpc_order.getQtyEntered().subtract(m_mpc_order.getQtyDelivered()).compareTo(BigDecimal.ZERO)>=0)
                    openQty.setValue(m_mpc_order.getQtyEntered().subtract(m_mpc_order.getQtyDelivered()));
                else
                    openQty.setValue(BigDecimal.ZERO);
                toDeliverQty.setValue(openQty.getValue());
                product.setValue(new Integer(m_mpc_order.getM_Product_ID()));
                MProduct m_product =  new MProduct(Env.getCtx(),m_mpc_order.getM_Product_ID(),null);
                uom.setValue(new Integer(m_product.getC_UOM_ID()));
                uomorder.setValue(new Integer(m_mpc_order.getC_UOM_ID()));
                Integer m_product_id = (Integer)product.getValue();
                Env.setContext(Env.getCtx(), m_WindowNo ,"M_Product_ID",m_product_id.intValue());
                attribute.setValue(new Integer(m_mpc_order.getM_AttributeSetInstance_ID()));
                enableToDeliver();

                if (m_IsBackflush) {
                    executeQuery();
                }

                if (m_ListDetail) {
                    executeQuery();
                }

            }
        }		//  MPC_Order_ID



        // yes show the field for to make receive
        if (name.equals("OnlyReceipt")) {
            //issue.setVisible(true);
            //     boolean m_OnlyReceipt = ((Boolean)value).booleanValue();

            if (valueSelected==1)
                m_OnlyReceipt = true;
            else
                m_OnlyReceipt = false;
            if (m_OnlyReceipt) {
                enableToDeliver();
            } else {
                disableToDeliver();
                if(order.getValue() != null && m_IsBackflush) {
                    executeQuery();
                }
            }
        }

        if (name.equals("toDeliverQty")) {
            if(order.getValue() != null && m_IsBackflush) {
                executeQuery();
            }

        }
        //  MPC_Order_ID
        if (name.equals("IsBackflush")) {
            // m_IsBackflush = ((Boolean)value).booleanValue();
            if (valueSelected==2)
                m_IsBackflush = true;
            else
                m_IsBackflush = false;

            if (m_IsBackflush) {
                executeQuery();
            } else {
                issue.setVisible(false);
            }

        }

        if (name.equals("ListDetail")) {
            /*

             if (valueSelected==3)
                m_ListDetail = true;
            else
                m_ListDetail = false;

             if (m_ListDetail) {
                executeQuery();
            } else {
                executeQuery();
            }

             */

            if (valueSelected==4) {
                executeQueryReturn();
            } else {
                executeQuery();
            }

        }
    }

    public void valueChanged(ListSelectionEvent e) {
    }

    /**
     *  Search Button Pressed - Fill xMatched
     */
    private void cmd_search() {
        //  ** Add Where Clause **
        //  MPC_Order_ID
        if (order.getValue() != null) {
            m_sql.append(" WHERE obl.MPC_Order_ID=").append(order.getValue());
            tableLoad(issue);
        }

    }


    /**************************************************************************
     * 	Create Line
     *	@param order order
     *	@param orderLine line
     *	@param qty qty
     */
    private void createIssue(int MPC_OrderBOMLine_ID , Timestamp movementdate, BigDecimal qty ,  BigDecimal qtyscrap , BigDecimal qtyreject, MStorage[] storages, String trxName) throws SQLException{

        //DB.executeUpdate("UPDATE MPC_Order_BOMLine SET QtyScrap = " + qtyscrap + " WHERE MPC_Order_BOMLine_ID=" + MPC_OrderBOMLine_ID);

        if (qty.compareTo(Env.ZERO) == 0)
            return;

        //	Inventory Lines
        BigDecimal toIssue = qty.add(qtyscrap);
        for (int i = 0; i < storages.length; i++) {
            MStorage storage = storages[i];
            //	TODO Selection of ASI

            /*cannot
            ** VIT4B - Comentado para dar soporte a la devoluci�n sobre instancias con stock en 0
             *

             if(storage.getQtyOnHand().compareTo(Env.ZERO) == 0)
                continue;
            */


            BigDecimal issue = toIssue;
            if (issue.compareTo(storage.getQtyOnHand()) > 0)
                issue = storage.getQtyOnHand();



            //System.out.println("***** STORAGE attribute " +storage.getM_AttributeSetInstance_ID());
            log.info("createCollector - ToIssue" + issue); //pba
            log.fine("createCollector - ToIssue" + issue);
            MMPCOrderBOMLine mpc_orderbomLine = new MMPCOrderBOMLine(Env.getCtx() , MPC_OrderBOMLine_ID, trxName);
            if ( mpc_orderbomLine.getQtyBatch().compareTo(Env.ZERO) == 0 && mpc_orderbomLine.getQtyBOM().compareTo(Env.ZERO) == 0 ) {
                // Method Variance
                int C_DocType_ID = getDocType(MDocType.DOCBASETYPE_ManufacturingOrderMethodVariation);
                //if (issue.compareTo(Env.ZERO)>0 || qtyscrap.compareTo(Env.ZERO)>0 || qtyreject.compareTo(Env.ZERO)>0) //fjviejo e-evolution no hace transacciones en 0
                log.fine ("#### MODIFICACION RECEPCION ENTREGA: product_id - " + mpc_orderbomLine.getM_Product_ID() + " MPC_OrderBOMLine_ID - " + MPC_OrderBOMLine_ID);
                createCollector(mpc_orderbomLine.getM_Product_ID(),storage.getM_Locator_ID(),storage.getM_AttributeSetInstance_ID(), movementdate , issue , qtyscrap , qtyreject , C_DocType_ID , MPC_OrderBOMLine_ID,MMPCCostCollector.MOVEMENTTYPE_Production_,trxName);

            } else {

                int C_DocType_ID = getDocType(MDocType.DOCBASETYPE_ManufacturingOrderIssue);
                //if (issue.compareTo(Env.ZERO)>0 || qtyscrap.compareTo(Env.ZERO)>0 || qtyreject.compareTo(Env.ZERO)>0) //fjviejo e-evolution no hace transacciones en 0
                log.fine ("#### MODIFICACION RECEPCION ENTREGA: product_id - " + mpc_orderbomLine.getM_Product_ID() + " MPC_OrderBOMLine_ID - " + MPC_OrderBOMLine_ID);
                createCollector(mpc_orderbomLine.getM_Product_ID(),storage.getM_Locator_ID(),storage.getM_AttributeSetInstance_ID(), movementdate , issue, qtyscrap , qtyreject, C_DocType_ID , MPC_OrderBOMLine_ID, MMPCCostCollector.MOVEMENTTYPE_Production_,trxName);
            }

                        /*if (qtyscrap.compareTo(Env.ZERO) != 0)
                        {
                            int C_DocType_ID = getDocType(MDocType.DOCBASETYPE_ManufacturingOrderUseVariation); // Use Variation
                            createCollector (mpc_orderbomLine.getM_Product_ID(),storage.getM_Locator_ID(),storage.getM_AttributeSetInstance_ID(), movementdate , qtyscrap.negate(), C_DocType_ID, MPC_OrderBOMLine_ID);
                        }*/

            toIssue = toIssue.subtract(issue);
            if (toIssue.compareTo(Env.ZERO) == 0)
                break;
            //if (toIssue.compareTo(Env.ZERO) != 0)
            //throw new IllegalStateException("Not All Issued - Remainder=" + toIssue);
        }
    }

    private void createCollector(int M_Product_ID ,int M_Locator_ID , int M_AttributeSetInstance_ID, Timestamp movementdate , BigDecimal qty , BigDecimal scrap , BigDecimal reject, int C_DocType_ID, int MPC_Order_BOMLine_ID, String MovementType, String trxName) throws SQLException {

        /** BISion - 11/02/2009 - Santiago Ibañez
         * Se crea una transaccion local ya que la tabla MPC_COST_COLLECTOR_REL
         * requiere que se cree al instante el MPC_Cost_COllector a continuación
         */
        Trx trx = Trx.get(Trx.createTrxName("Collector"), true);
        //System.out.println("***** createcollector attribute " +M_AttributeSetInstance_ID);
        MMPCCostCollector MPC_Cost_Collector=null;
        try{
            MPC_Cost_Collector = new MMPCCostCollector(Env.getCtx(), 0,trx.getTrxName());
            MPC_Cost_Collector.setMPC_Order_ID(m_mpc_order.getMPC_Order_ID());
            MPC_Cost_Collector.setMPC_Order_BOMLine_ID(MPC_Order_BOMLine_ID);
            MPC_Cost_Collector.setAD_OrgTrx_ID(m_mpc_order.getAD_OrgTrx_ID());
            MPC_Cost_Collector.setC_Activity_ID(m_mpc_order.getC_Activity_ID());
            MPC_Cost_Collector.setC_Campaign_ID(m_mpc_order.getC_Campaign_ID());
            MPC_Cost_Collector.setC_DocType_ID(C_DocType_ID);
            MPC_Cost_Collector.setC_DocTypeTarget_ID(C_DocType_ID);
            MPC_Cost_Collector.setMovementType(MovementType);
            MPC_Cost_Collector.setC_Project_ID(m_mpc_order.getC_Project_ID());
            MPC_Cost_Collector.setDescription(m_mpc_order.getDescription());
            MPC_Cost_Collector.setDocAction(MPC_Cost_Collector.ACTION_Complete);
            MPC_Cost_Collector.setDocStatus(MPC_Cost_Collector.DOCSTATUS_Drafted);
            MPC_Cost_Collector.setIsActive(true);
            MPC_Cost_Collector.setM_Warehouse_ID(m_mpc_order.getM_Warehouse_ID());
            MPC_Cost_Collector.setM_Locator_ID(M_Locator_ID);
            MPC_Cost_Collector.setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
            MPC_Cost_Collector.setS_Resource_ID(m_mpc_order.getS_Resource_ID());
            MPC_Cost_Collector.setMovementDate(movementdate);
            MPC_Cost_Collector.setDateAcct(movementdate);
            MPC_Cost_Collector.setMovementQty(qty);
            MPC_Cost_Collector.setScrappedQty(scrap);
            MPC_Cost_Collector.setQtyReject(reject);
            MPC_Cost_Collector.setPosted(false);
            MPC_Cost_Collector.setProcessed(false);
            MPC_Cost_Collector.setProcessing(false);
            MPC_Cost_Collector.setUser1_ID(m_mpc_order.getUser1_ID());
            MPC_Cost_Collector.setUser2_ID(m_mpc_order.getUser1_ID());
            MPC_Cost_Collector.setM_Locator_ID(M_Locator_ID);
            MPC_Cost_Collector.setM_Product_ID(M_Product_ID);
            //COM-PAN-CHR-02.006.01
            //MPC_Cost_Collector.setBUNDLE(fieldBundle.getText());
            if (!MPC_Cost_Collector.save()) {
                throw new IllegalStateException("Could not create Collector");
            }

            //si no se pudo completar
            if (!MPC_Cost_Collector.completeIt().equals(DocAction.STATUS_Completed))
                throw new IllegalStateException("Could not complete Collector");
            trx.commit();
            trx.close();
        }
        catch(Exception e){
          e.printStackTrace();
          trx.rollback();
          trx.close();
        }
        /*
         *** AGREGADO PARA AGRUPAR LAS ORDENESSSS
         ***
         ***
         *
         *
         *
        */

        int Cost_Collector_REL_ID = 0;
        int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
        int AD_Org_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Org_ID"));

        //Cost_Collector_REL_ID = MSequence.getNextID (AD_Client_ID, "MPC_COST_COLLECTOR_REL", "VOrderReceiptIssue");

        /** BISion - 10/02/2009 - Santiago Ibañez
         *  Modificacion realizada para que la insercion en MPC_COST_COLLECTOR_REL
         * lo haga mediante una transaccion local y no espere al final del proceso
         * ya que el informe de recepciones y entregas lo necesita antes
         */
        trx = Trx.get(Trx.createTrxName("InsCollRel"), true);

        String sqlCollector = "SELECT CURRENTNEXT FROM AD_SEQUENCE WHERE NAME = 'MPC_COST_COLLECTOR_REL'";

            try{
                PreparedStatement pstmt1 = DB.prepareStatement(sqlCollector,trx.getTrxName());
                ResultSet rs1 = pstmt1.executeQuery();
                if(rs1.next())
                {
                    Cost_Collector_REL_ID = rs1.getInt(1);
                }
                rs1.close();
                pstmt1.close();


                int NEXT = Cost_Collector_REL_ID + 1;
                sqlCollector = "UPDATE AD_SEQUENCE SET CURRENTNEXT = " + NEXT + " WHERE NAME = 'MPC_COST_COLLECTOR_REL'";

            //try {
                PreparedStatement pstmt2 = DB.prepareStatement(sqlCollector,trx.getTrxName());
                ResultSet rs2 = pstmt2.executeQuery();
                rs2.close();
                pstmt2.close();
            //} catch(SQLException obl) {
            //}

                int Cost_Collector_ID = MPC_Cost_Collector.getMPC_Cost_Collector_ID();
                int Cost_Collector_Header_ID = getHeaderID();

                sqlCollector = "INSERT INTO MPC_COST_COLLECTOR_REL (AD_CLIENT_ID,AD_ORG_ID,ISACTIVE," +
                        "CREATED,CREATEDBY,UPDATED,UPDATEDBY,MPC_COST_COLLECTOR_REL_ID,MPC_COST_COLLECTOR_ID,MPC_COST_COLLECTOR_HEADER_ID)" +
                        "VALUES (" + AD_Client_ID + "," + AD_Org_ID + ",'Y',SYSDATE,1000684," +
                        "SYSDATE,1000684," + Cost_Collector_REL_ID + "," + Cost_Collector_ID + "," + Cost_Collector_Header_ID + ")";

                System.out.println("LOG DE MPC_COST_COLLECTOR_REL ... " + sqlCollector);


            //try {
                PreparedStatement pstmt3 = DB.prepareStatement(sqlCollector,trx.getTrxName());
                ResultSet rs3 = pstmt3.executeQuery();
                rs3.close();
                pstmt3.close();
                trx.commit();
                trx.close();
            }
            catch(SQLException obl) {
                obl.printStackTrace();
                trx.rollback();
                trx.close();
                //throw obl;
            }

            //MPC_Cost_Collector.save();
    }

    private int getDocType(String DocBaseType) {
        MDocType[] Doc = MDocType.getOfDocBaseType(Env.getCtx(), DocBaseType);
        int C_DocType_ID = 0;

        if(Doc!=null)
            C_DocType_ID = Doc[0].getC_DocType_ID();

        return C_DocType_ID;
    }

    public void enableToDeliver() {
        //issue.removeAll();
        //issue.setVisible(true);
        toDeliverQtyLabel.setVisible(true);
        toDeliverQty.setVisible(true);
        scrapQtyLabel.setVisible(true);
        scrapQty.setVisible(true);
        rejectQtyLabel.setVisible(true);
        rejectQty.setVisible(true);
        movementDateLabel.setVisible(true);
        movementDate.setVisible(true);
        attributeLabel.setVisible(true);
        attribute.setVisible(true);
        locator.setValue("Cuarentena Famatina U.");
        locatorLabel.setVisible(true);
        locator.setVisible(true);
        locator.setReadWrite(false);
        labelBundle.setVisible(true);
        fieldBundle.setText("");
        fieldBundle.setVisible(true);
    }

    public void disableToDeliver() {
        toDeliverQtyLabel.setVisible(false);
        toDeliverQty.setVisible(false);
        scrapQtyLabel.setVisible(false);
        scrapQty.setVisible(false);
        rejectQtyLabel.setVisible(false);
        rejectQty.setVisible(false);
        //movementDateLabel.setVisible(false);
        //movementDate.setVisible(false);
        attributeLabel.setVisible(false);
        attribute.setVisible(false);
        //backflushGroup.setVisible(false);
        // IsBackflush.setVisible(true);
        // IsBackflush.setValue(new Boolean(true));
        //backflushGroupLabel.setVisible(false);
        //backflushGroup.setVisible(false);
        locatorLabel.setVisible(false);
        locator.setVisible(false);
        locator.setReadWrite(true);
        issue.setVisible(true);
        labelVencimiento.setVisible(false);
        fechaVencimiento.setVisible(false);
        labelBundle.setVisible(false);
        fieldBundle.setVisible(false);
        // executeQuery();
    }
      //  generateInvoices_complete

    private int getHeaderID() {
        return Cost_Collector_HEADER_ID;
    }




    /*
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Generate;
    private javax.swing.JPanel PanelBottom;
    private javax.swing.JPanel PanelCenter;
    private javax.swing.JButton Process;
    private javax.swing.JPanel ReceiptIssueOrder;
    private javax.swing.JTabbedPane TabsReceiptsIssue;
    private javax.swing.JTextField attribute;
    private javax.swing.JLabel attributeLabel;
    private javax.swing.JTextField deliveryQty;
    private javax.swing.JLabel deliveryQtyLabel;
    private javax.swing.JTable issue;
    private javax.swing.JTextField issueMethod;
    private javax.swing.JLabel issueMethodLabel;
    private javax.swing.JScrollPane issueScrollPane;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField movementDate;
    private javax.swing.JLabel movementDateLabel;
    private javax.swing.JPanel northPanel;
    private javax.swing.JTextField order;
    private javax.swing.JLabel orderLabel;
    private javax.swing.JTextField org;
    private javax.swing.JLabel orgLabel;
    private javax.swing.JTextField rejectQty;
    private javax.swing.JLabel rejectQtyLabel;
    private javax.swing.JTextField resource;
    private javax.swing.JLabel resourceLabel;
    private javax.swing.JTextField scrapQty;
    private javax.swing.JLabel scrapQtyLabel;
    // End of variables declaration//GEN-END:variables
     */


    // Variables declaration - do not modify
    private CPanel mainPanel = new CPanel();
    private CPanel Generate = new CPanel();
    private CPanel PanelBottom = new CPanel();
    private CPanel PanelCenter = new CPanel();
    private CPanel northPanel = new CPanel();
    private CButton Process = new CButton();
    private CPanel ReceiptIssueOrder = new CPanel();
    private javax.swing.JTabbedPane TabsReceiptsIssue = new JTabbedPane();
    //private MPAttributeLookup attributeL = new MPAttributeLookup (Env.getCtx(), m_WindowNo);
    private VPAttribute attribute = null;
    //private VPAttribute attribute = null;

    private CLabel attributeLabel = new CLabel();
    private VNumber orderedQty = new VNumber("QtyOrdered", false, false, false, DisplayType.Quantity, "QtyOrdered");
    private CLabel orderedQtyLabel =  new CLabel();
    private VNumber deliveredQty = new VNumber("QtyDelivered", false, false, false, DisplayType.Quantity, "QtyDelivered");
    private CLabel deliveredQtyLabel =  new CLabel();
    private VNumber openQty = new VNumber("QtyDelivered", false, false, false, DisplayType.Quantity, "QtyDelivered");
    private CLabel openQtyLabel =  new CLabel();
    private VNumber toDeliverQty = new VNumber("QtyToDeliver", true, false, true, DisplayType.Quantity, "QtyToDeliver");
    private CLabel toDeliverQtyLabel =  new CLabel();
    private VLookup issueMethod = null;
    private CLabel issueMethodLabel = new CLabel();;
    private javax.swing.JScrollPane issueScrollPane = new JScrollPane();
    private MiniTable issue = new MiniTable();
    private VDate movementDate = new VDate("MovementDate", true, false, true, DisplayType.Date, "MovementDate");
    private CLabel movementDateLabel =  new CLabel();
    private VLookup order = null;
    private CLabel orderLabel =  new CLabel();
    //private VLookup org = null;
    //private CLabel orgLabel =  new CLabel();
    private VNumber rejectQty = new VNumber("Qtyreject", false, false, true, DisplayType.Quantity, "QtyReject");
    private CLabel rejectQtyLabel =  new CLabel();
    private VLookup resource = null;
    private CLabel resourceLabel =  new CLabel();
    private VLookup warehouse = null;
    private CLabel warehouseLabel =  new CLabel();
    private VNumber scrapQty = new VNumber("Qtyscrap", false, false, true, DisplayType.Quantity, "Qtyscrap");
    private CLabel scrapQtyLabel =  new CLabel();
    private CLabel backflushGroupLabel = new CLabel(Msg.translate(Env.getCtx(), "BackflushGroup"));
    private CTextField backflushGroup = new CTextField(10);

    private CLabel producLabel = new CLabel(Msg.translate(Env.getCtx(), "M_Product_ID"));
    private VLookup product = null;
    private CLabel nameLabel = new CLabel(Msg.translate(Env.getCtx(), "Name"));
    private CTextField name = new CTextField(30);
    private CLabel uomLabel = new CLabel(Msg.translate(Env.getCtx(), "C_UOM_ID"));
    private VLookup  uom = null;
    private CLabel uomorderLabel = new CLabel(Msg.translate(Env.getCtx(), "Altert UOM"));
    private VLookup  uomorder = null;

    private CLabel locatorLabel = new CLabel(Msg.translate(Env.getCtx(), "M_Locator_ID"));
    private VLocator locator = null;

    //private VCheckBox OnlyReceipt = new VCheckBox ("OnlyReceipt", false, false, true, Msg.translate(Env.getCtx(), "OnlyReceipt"), "", false);
    //private VCheckBox IsBackflush = new VCheckBox ("IsBackflush", false, false, true, Msg.translate(Env.getCtx(), "IsBackflush"), "", false);
    //private VCheckBox ListDetail = new VCheckBox ("ListDetail", false, false, false, Msg.translate(Env.getCtx(), "ListDetail"), "", false);
    private CLabel labelcombo = new CLabel(Msg.translate(Env.getCtx(), "DeliveryRule"));
    private VComboBox pickcombo = new VComboBox();
    private CLabel QtyBatchsLabel =  new CLabel();
    private VNumber qtyBatchs = new VNumber("QtyBatchs", false, false, false, DisplayType.Quantity, "QtyBatchs");
    private CLabel QtyBatchSizeLabel =  new CLabel();
    private VNumber qtyBatchSize = new VNumber("QtyBatchSize", false, false, false, DisplayType.Quantity, "QtyBatchSize");

    private CTextPane info = new CTextPane();
    private StringBuffer    m_sql = null;
    private String          m_dateColumn = "";
    private String          m_qtyColumn = "";
    private String          m_groupBy = "";

    /**
     * BISion - 19/10/2009 - Santiago Ibañez
     * COM-PAN-REQ-08.001.01
     */
    private CLabel labelVencimiento = new CLabel("Vencimiento Partida");

    private VDate fechaVencimiento = new VDate("GuaranteeDate", true, false, true, DisplayType.Date, "GuaranteeDate");

    /**
     * BISion - 19/10/2009 - Santiago Ibañez
     * COM-PAN-REQ-02.002.01
     */
    private CLabel labelBundle = new CLabel("Descripcion de bultos");

    private CTextField fieldBundle = new CTextField(20);

}

