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

public class VOrderReceiptIssue extends CPanel
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
        
        
        northPanel.add(labelAutoGenerateLot, new GridBagConstraints(4, 8, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        northPanel.add(autoGenerateLot, new GridBagConstraints(5, 8, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        /**
         * BISion - 19/10/2009 - Santiago Ibañez
         * COM-PAN-REQ-08.001.01
         */
        northPanel.add(labelVencimiento,     new GridBagConstraints(4, 9, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        northPanel.add(fechaVencimiento,       new GridBagConstraints(5, 9, 1, 1, 0.0, 0.0
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
        issue.addColumn("MPC_Order_BOMLine");            //18

        issue.setMultiSelection(true);
        issue.setRowSelectionAllowed(true);

        //  set details
        issue.setColumnClass( 0, IDColumn.class   , false, " ");
        issue.setColumnClass( 1, Boolean.class    , true, Msg.translate(Env.getCtx(), "IsCritical"));
        issue.setColumnClass( 2, String.class    , true, Msg.translate(Env.getCtx(), "Value"));
        issue.setColumnClass( 3, KeyNamePair.class, true, Msg.translate(Env.getCtx(), "M_Product_ID"));
        issue.setColumnClass( 4, KeyNamePair.class, true, Msg.translate(Env.getCtx(), "C_UOM_ID"));
        issue.setColumnClass(5, String.class , true, Msg.translate(Env.getCtx(), "M_AttributeSetInstance_ID"));
        issue.setColumnClass( 6, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyRequiered"));
        issue.setColumnClass( 7, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyDelivered"));
        issue.setColumnClass( 8, VNumber.class , false, Msg.translate(Env.getCtx(), "QtyToDeliver"));
        issue.setColumnClass( 9, VNumber.class , false, Msg.translate(Env.getCtx(), "QtyScrap"));
        issue.setColumnClass( 10, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyOnHand"));
        issue.setColumnClass( 11, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyReserved"));
        issue.setColumnClass( 12, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyAvailable"));
        issue.setColumnClass(13, String.class , true, Msg.translate(Env.getCtx(), "M_Locator_ID"));
        issue.setColumnClass(14, KeyNamePair.class, true, Msg.translate(Env.getCtx(), "M_Warehouse_ID"));
        issue.setColumnClass(15, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyBom"));
        issue.setColumnClass(16, Boolean.class    , true, Msg.translate(Env.getCtx(), "IsQtyPercentage"));
        issue.setColumnClass(17, BigDecimal.class , true, Msg.translate(Env.getCtx(), "QtyBatch"));
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
        locator = new VLocator("M_Locator_ID", true, false, true, locatorL, m_WindowNo);


        MPAttributeLookup attributeL = new MPAttributeLookup(ctx,m_WindowNo);
        attribute = new VPAttribute(false, false, true, m_WindowNo, attributeL);
        attribute.setValue(new Integer(0));
        //  Tab, Window
        MFieldVO vo =  MFieldVO.createStdField(ctx , m_WindowNo , 1000031 , 1000013, false, false, false);
        // M_AttributeSetInstance_ID
        vo.AD_Column_ID = 1000183;
        MField field = new MField(vo);
        attribute.setField(field);
        
        fillCombo();
        
        pickcombo.addActionListener(this);
        Process.addActionListener(this);
        toDeliverQty.addActionListener(this);
        scrapQty.addActionListener(this);

        //order.addActionListener(this); 
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

        int valueSelected = 0;
        if ( pickcombo.getValue() != null )
            valueSelected = Integer.parseInt(pickcombo.getValue().toString());

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
                issue.setValueAt(id, row, 0);
                System.out.println("***** prduct_id " +rs.getInt(4));
                if (rs.getString(2).equals("Y"))
                    issue.setValueAt(new Boolean(true), row, 1);//  IsCritical
                else
                    issue.setValueAt(new Boolean(false), row, 1);//  IsCritical

                issue.setValueAt(rs.getString(3), row, 2);


                issue.setValueAt(m_productkey , row, 3);             //  Product

                issue.setValueAt(m_uomkey, row, 4);             //  UOM

                issue.setValueAt( m_QtyBom , row, 15);          //  QtyBom

                issue.setValueAt(m_QtyPercentage, row, 16); //  isQtyPercentage

                issue.setValueAt(m_QtyBatch, row, 17);          //  QtyBatch

                issue.setValueAt(m_warehousekey , row, 14);             //  Product

                issue.setValueAt(rs.getBigDecimal(9), row, 11);          //  QtyReserved

                issue.setValueAt(m_onhand , row, 12); // OnHand

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
                while (rsw.next()) {
                    System.out.println("***** suma almacenes "+rsw.getBigDecimal(2));
                    issue.setValueAt(rsw.getBigDecimal(1), row, 12);          //  QtyAvailable

                    issue.setValueAt(rsw.getBigDecimal(2) , row, 10); // OnHand
                }
                // cambio para panalab que tome los almacenes marcados fjviejo e-evolution end

                if (m_ComponentType.equals(MMPCProductBOMLine.COMPONENTTYPE_Component) || m_ComponentType.equals(MMPCProductBOMLine.COMPONENTTYPE_Packing)) {
                    if (m_QtyPercentage.booleanValue()) // Calculate Qty %
                    {
                        VNumber viewToDeliverQty = new VNumber();
                        viewToDeliverQty.setDisplayType(DisplayType.Number);

                        if (m_OnlyReceipt && m_IsBackflush) // Calculate Component for Receipt
                        {
                            viewToDeliverQty.setValue(m_toDeliverQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4,BigDecimal.ROUND_HALF_UP)));
                            componentToDeliverQty = (BigDecimal)viewToDeliverQty.getValue();
                            if (rs.getBigDecimal(8).compareTo(Env.ZERO) == 0)
                                componentToDeliverQty = Env.ZERO;
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq =m_toDeliverQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP));
                                componentQtytoDel =componentToDeliverQty.divide(Env.ONE,4,BigDecimal.ROUND_HALF_UP);
                                issue.setValueAt(m_toDeliverQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP)), row, 6); //  QtyRequiered
                                issue.setValueAt(componentToDeliverQty.divide(Env.ONE,8,BigDecimal.ROUND_HALF_UP), row, 8); //  QtyToDelivery
                            }
                        }
                        else // Only Calculate Component not exist Receipt
                        {
                            componentToDeliverQty = rs.getBigDecimal(19);
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq =m_openQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4,BigDecimal.ROUND_HALF_UP));
                                componentQtytoDel=componentToDeliverQty.divide(Env.ONE,4,BigDecimal.ROUND_HALF_UP);
                                issue.setValueAt(componentToDeliverQty.divide(Env.ONE,8,BigDecimal.ROUND_HALF_UP), row, 8); //  QtyToDelivery
                                issue.setValueAt(m_openQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP)), row, 6); //  QtyRequiered
                            }
                        }
                        if(m_scrapQty.compareTo(Env.ZERO) != 0) {
                            VNumber viewScrapQty = new VNumber();
                            viewScrapQty.setDisplayType(DisplayType.Number);
                            viewScrapQty.setValue(m_scrapQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP)));
                            componentScrapQty = (BigDecimal)viewScrapQty.getValue();
                            if (componentScrapQty.compareTo(Env.ZERO) != 0) {
                                issue.setValueAt(componentScrapQty.divide(Env.ONE,8,BigDecimal.ROUND_HALF_UP), row, 9); //  QtyToDelivery
                            }
                        }
                    } else // Normal Calculate Qty
                    {
                        VNumber viewToDeliverQty = new VNumber();
                        viewToDeliverQty.setDisplayType(DisplayType.Number);

                        if (m_OnlyReceipt && m_IsBackflush) // Calculate Component for Receipt
                        {
                            viewToDeliverQty.setValue(m_toDeliverQty.multiply(m_QtyBom));
                            componentToDeliverQty = (BigDecimal)viewToDeliverQty.getValue();
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq = m_toDeliverQty.multiply(m_QtyBom);
                                componentQtytoDel=componentToDeliverQty;
                                issue.setValueAt(m_toDeliverQty.multiply(m_QtyBom), row, 6);  //  QtyRequiered
                                issue.setValueAt(componentToDeliverQty, row, 8); //  QtyToDelivery
                            }
                        } // if (m_OnlyReceipt)
                        else {
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
                    row=row+lotes(rs.getInt(4), row, id,rs.getInt(13),componentQtyReq, componentQtytoDel,id.getRecord_ID());
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE,"VOrderReceipIssue.executeQuery", e);
        }
        issue.autoSize();
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
        
        int valueSelected = 0;
        if ( pickcombo.getValue() != null )
            valueSelected = Integer.parseInt(pickcombo.getValue().toString());

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
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                issue.setRowCount(row+1);
                IDColumn id = new IDColumn(rs.getInt(1));
                KeyNamePair m_productkey = new KeyNamePair(rs.getInt(4) , rs.getString(5));
                KeyNamePair m_uomkey = new KeyNamePair(rs.getInt(6),rs.getString(7));
                BigDecimal m_QtyBom = rs.getBigDecimal(15);
                Boolean m_QtyPercentage = null;
                BigDecimal m_QtyBatch = rs.getBigDecimal(17);
                String m_ComponentType = rs.getString(18);
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
                System.out.println("***** prduct_id " +rs.getInt(4));
                if (rs.getString(2).equals("Y"))
                    issue.setValueAt(new Boolean(true), row, 1);//  IsCritical
                else
                    issue.setValueAt(new Boolean(false), row, 1);//  IsCritical
                issue.setValueAt(rs.getString(3), row, 2);
                issue.setValueAt(m_productkey , row, 3);             //  Product
                issue.setValueAt(m_uomkey, row, 4);             //  UOM
                issue.setValueAt( m_QtyBom , row, 15);          //  QtyBom
                issue.setValueAt(m_QtyPercentage, row, 16); //  isQtyPercentage
                issue.setValueAt(m_QtyBatch, row, 17);          //  QtyBatch
                issue.setValueAt(m_warehousekey , row, 14);             //  Product
                issue.setValueAt(rs.getBigDecimal(9), row, 11);          //  QtyReserved
                issue.setValueAt(m_onhand , row, 12); // OnHand
                issue.setValueAt(rs.getBigDecimal(11) , row, 10); // Availale
                issue.setValueAt(rs.getBigDecimal(20) , row, 7); // QtyDelivered
                // Put Qty Issue
                
                /*
                * GENEOS - Pablo Velazquez
                * 09/08/2013
                * Modificacion para que se tenga control de a que linea se esta surtiendo
                */
                issue.setValueAt(id.getRecord_ID() , row, 18);
                
                // cambio para panalab que tome los almacenes marcados fjviejo e-evolution
                StringBuffer sqlw = new StringBuffer("SELECT sum(bomQtyAvailable(obl.M_Product_ID,w.M_Warehouse_ID, 0 )) AS QtyAvailable , sum(bomQtyOnHand(obl.M_Product_ID,w.M_Warehouse_ID,0)) AS QtyOnHand  FROM MPC_Order_BOMLine obl");
                sqlw.append(" INNER JOIN M_Warehouse w ON (w.isProductionIssue='Y' and obl.AD_Org_ID=w.AD_Org_ID) ");
                sqlw.append(" WHERE obl.MPC_Order_ID = " + order.getValue() +  " and obl.M_Product_ID=" +rs.getInt(4) +" ORDER BY M_Product_ID");
                PreparedStatement pstmtw = DB.prepareStatement(sqlw.toString(),null);
                ResultSet rsw = pstmtw.executeQuery();
                System.out.println("***** suma almacenes sql "+sqlw.toString());
                while (rsw.next()) {
                    System.out.println("***** suma almacenes "+rsw.getBigDecimal(2));
                    issue.setValueAt(rsw.getBigDecimal(1), row, 12);          //  QtyAvailable

                    issue.setValueAt(rsw.getBigDecimal(2) , row, 10); // OnHand
                }
                if (m_ComponentType.equals(MMPCProductBOMLine.COMPONENTTYPE_Component) || m_ComponentType.equals(MMPCProductBOMLine.COMPONENTTYPE_Packing)) {
                    if (m_QtyPercentage.booleanValue()) // Calculate Qty %
                    {
                        VNumber viewToDeliverQty = new VNumber();
                        viewToDeliverQty.setDisplayType(DisplayType.Number);
                        if (m_OnlyReceipt && m_IsBackflush) // Calculate Component for Receipt
                        {
                            viewToDeliverQty.setValue(m_toDeliverQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4,BigDecimal.ROUND_HALF_UP)));
                            componentToDeliverQty = (BigDecimal)viewToDeliverQty.getValue();
                            if (rs.getBigDecimal(8).compareTo(Env.ZERO) == 0)
                                componentToDeliverQty = Env.ZERO;
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq =m_toDeliverQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP));
                                componentQtytoDel =componentToDeliverQty.divide(Env.ONE,4,BigDecimal.ROUND_HALF_UP);
                                issue.setValueAt(m_toDeliverQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP)), row, 6); //  QtyRequiered
                                issue.setValueAt(componentToDeliverQty.divide(Env.ONE,8,BigDecimal.ROUND_HALF_UP), row, 8); //  QtyToDelivery
                            }
                        } else // Only Calculate Component not exist Receipt
                        {
                            componentToDeliverQty = rs.getBigDecimal(19);
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq =m_openQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,4,BigDecimal.ROUND_HALF_UP));
                                componentQtytoDel=componentToDeliverQty.divide(Env.ONE,4,BigDecimal.ROUND_HALF_UP);
                                issue.setValueAt(componentToDeliverQty.divide(Env.ONE,8,BigDecimal.ROUND_HALF_UP), row, 8); //  QtyToDelivery
                                issue.setValueAt(m_openQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP)), row, 6); //  QtyRequiered
                            }
                        }
                        if(m_scrapQty.compareTo(Env.ZERO) != 0) {
                            VNumber viewScrapQty = new VNumber();
                            viewScrapQty.setDisplayType(DisplayType.Number);
                            viewScrapQty.setValue(m_scrapQty.multiply(m_QtyBatch.divide(new BigDecimal(100.00) ,8,BigDecimal.ROUND_HALF_UP)));
                            componentScrapQty = (BigDecimal)viewScrapQty.getValue();
                            if (componentScrapQty.compareTo(Env.ZERO) != 0) {
                                issue.setValueAt(componentScrapQty.divide(Env.ONE,8,BigDecimal.ROUND_HALF_UP), row, 9); //  QtyToDelivery
                            }
                        }
                    } else // Normal Calculate Qty
                    {
                        VNumber viewToDeliverQty = new VNumber();
                        viewToDeliverQty.setDisplayType(DisplayType.Number);

                        if (m_OnlyReceipt && m_IsBackflush) // Calculate Component for Receipt
                        {
                            viewToDeliverQty.setValue(m_toDeliverQty.multiply(m_QtyBom));
                            componentToDeliverQty = (BigDecimal)viewToDeliverQty.getValue();
                            if (componentToDeliverQty.compareTo(Env.ZERO) != 0) {
                                componentQtyReq = m_toDeliverQty.multiply(m_QtyBom);
                                componentQtytoDel=componentToDeliverQty;
                                issue.setValueAt(m_toDeliverQty.multiply(m_QtyBom), row, 6);  //  QtyRequiered
                                issue.setValueAt(componentToDeliverQty, row, 8); //  QtyToDelivery
                            }
                        } // if (m_OnlyReceipt)
                        else {
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
                    row=row+lotesReturn(rs.getInt(4), row, id,rs.getInt(13),componentQtyReq, componentQtytoDel, id.getRecord_ID());
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE,"VOrderReceipIssue.executeQuery", e);
        }
        //
        issue.autoSize();
    }   //  executeQuery


    private int lotes(int M_Product_ID, int row, IDColumn id, int Warehouse_ID, BigDecimal qtyreq, BigDecimal qtytodel,int mpc_order_bomline_id) {

        int haslot =0;
        BigDecimal reql = Env.ZERO;
        BigDecimal todeliveryl = Env.ZERO;
        reql=qtyreq;
        /*
         * GENEOS - Pablo Velazquez
         * 18/08/2013
         * Modificacion para que calcule lo requerido de la linea segun lo que falta entregar y no lo requerido TOTAL.
         */
        reql=qtytodel;
        
        todeliveryl=qtytodel;
        StringBuffer sql = new StringBuffer("SELECT s.M_Product_ID , s.QtyOnHand, s.M_AttributeSetInstance_ID, p.Name, masi.Description, l.Value, w.Value, w.M_warehouse_ID,p.Value, qr.remainingqty, CASE WHEN qr.remainingqty is null THEN s.QtyOnHand - s.QtyReserved ELSE s.QtyOnHand - s.QtyReserved + qr.remainingqty END as qtyAvailable ");
        sql.append("  FROM M_Storage s ");
        sql.append(" INNER JOIN M_Product p ON (s.M_Product_ID = p.M_Product_ID) ");
        sql.append(" INNER JOIN C_UOM u ON (u.C_UOM_ID = p.C_UOM_ID) ");
        sql.append(" INNER JOIN M_AttributeSetInstance masi ON (masi.M_AttributeSetInstance_ID = s.M_AttributeSetInstance_ID) ");
        sql.append("  Inner Join M_Locator l ON(s.M_Locator_ID=l.M_Locator_ID) ");
        sql.append(" INNER JOIN M_Warehouse w ON (w.M_Warehouse_ID = l.M_Warehouse_ID and w.isproductionissue='Y' and w.AD_Org_ID=" +m_AD_Org_ID +")");
        sql.append(" LEFT JOIN  mpc_order_qtyreserved qr ON (s.M_product_id = qr.M_Product_ID " +
"                                        and s.M_AttributeSetInstance_ID = qr.M_AttributeSetInstance_ID " +
"                                        and s.M_Locator_ID=qr.M_Locator_ID and qr.mpc_order_bomline_id="+mpc_order_bomline_id + ")");
        //cambio para que lo tome de diferentes lotes fjviejo panalab end
        sql.append(" WHERE s.M_Product_ID = " +M_Product_ID + " and ( qr.mpc_order_bomline_id="+mpc_order_bomline_id + " or qr.mpc_order_bomline_id is null ) and s.QtyOnHand <> 0 and s.m_attributesetinstance_id <> 0 AND ( masi.guaranteedate > current_date ) Order by qr.USEORDER, masi.guaranteedate " );

        log.log(Level.INFO, "VOrderReciptIssue.executeQuery - SQL", sql.toString());
        System.out.println( sql.toString() );
        //  reset table
        //  Execute
        try {
            PreparedStatement pstmt1 = DB.prepareStatement(sql.toString(),null);
            ResultSet rs1 = pstmt1.executeQuery();
            while (rs1.next()) {
                Color colour = new Color(5,5,50);
                issue.setRowCount(row+1);
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
                /*
                * GENEOS - Pablo Velazquez
                * 09/08/2013
                * Modificacion para que se tenga control de a que linea se esta surtiendo
                */
                issue.setValueAt(mpc_order_bomline_id , row, 18);
                
                BigDecimal cantDisponible = rs1.getBigDecimal(11);
                if (cantDisponible == null)
                        cantDisponible = BigDecimal.ZERO.setScale(2);
                issue.setValueAt(cantDisponible , row, 12);
                
                BigDecimal cantReservada = rs1.getBigDecimal(10);
                if (cantReservada == null)
                        cantReservada = BigDecimal.ZERO.setScale(2);
                issue.setValueAt(cantReservada , row, 11);
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

    private int lotesReturn(int M_Product_ID, int row, IDColumn id, int Warehouse_ID, BigDecimal qtyreq, BigDecimal qtytodel, int mpc_order_bomline_id) {
        int haslot =0;
        BigDecimal reql = Env.ZERO;
        BigDecimal todeliveryl = Env.ZERO;
        reql=qtyreq;
        /*
         * GENEOS - Pablo Velazquez
         * 98/08/2013
         * Modificacion para que calcule lo requerido de la linea segun lo que falta entregar y no lo requerido TOTAL.
         */
        reql=qtytodel;
        
        todeliveryl=qtytodel;
        StringBuffer sql = new StringBuffer("SELECT s.M_Product_ID , s.QtyOnHand, s.M_AttributeSetInstance_ID, p.Name, masi.Description, l.Value, w.Value, w.M_warehouse_ID,p.Value ");
        sql.append("  FROM M_Storage s ");
        sql.append(" INNER JOIN M_Product p ON (s.M_Product_ID = p.M_Product_ID) ");
        sql.append(" INNER JOIN C_UOM u ON (u.C_UOM_ID = p.C_UOM_ID) ");
        sql.append(" INNER JOIN M_AttributeSetInstance masi ON (masi.M_AttributeSetInstance_ID = s.M_AttributeSetInstance_ID) ");
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
                /*
                * GENEOS - Pablo Velazquez
                * 09/08/2013
                * Modificacion para que se tenga control de a que linea se esta surtiendo
                */
                issue.setValueAt(mpc_order_bomline_id , row, 18);
                haslot++;
                row++;
            }
            rs1.close();
            pstmt1.close();
        } catch (SQLException el) {
        }
        return haslot;
    }
    
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
        int valueSelected = 0;
        if ( pickcombo.getValue() != null )
            valueSelected = Integer.parseInt(pickcombo.getValue().toString());

        //Recepcion de terminado
        if (valueSelected==1)
        {
            attribute.setEnabled(true);
            m_OnlyReceipt = true;
        }

        //Entrega de Material para Produccion
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

        //Devolucion
        if (valueSelected==4) {
            m_Return = true;
            attribute.setEnabled(false);
            m_ListDetail = true;
        }
        
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
            
            ReceiptIssue();
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

            // Recepcion de Producto Terminado
            if (valueSelected==1){
                m_OnlyReceipt = true;
                mostrarVencimiento();
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
    
    }

    public void unlockUI(org.compiere.process.ProcessInfo processInfo) {
    }

    public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {

        boolean m_OnlyReceipt = false;
        boolean m_IsBackflush = false;
        boolean m_ListDetail = false;

        int valueSelected = 0;
        if ( pickcombo.getValue() != null )
            valueSelected = Integer.parseInt(pickcombo.getValue().toString());

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
            fillCombo();
        }		//  MPC_Order_ID
        // yes show the field for to make receive
        if (name.equals("OnlyReceipt")) {
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

    
    public int recuperarLote(String nombreLote){
        String sql = "select m_locator_id from m_locator where value like '" + nombreLote + "'";
        int id = 0;
        PreparedStatement ps = DB.prepareStatement(sql, null);
        try{
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                id = rs.getInt(1);
        }
        catch(SQLException ex){
        }
        return id;
    }	
    /**
     *  Search Button Pressed - Fill xMatched
     */
    private boolean cmd_process(String trxName) throws SQLException{
        boolean m_OnlyReceipt = false;
        boolean m_IsBackflush = false;
        boolean m_ListDetail = false;

        m_mpc_order.set_TrxName(trxName);

        int valueSelected = 0;
        if ( pickcombo.getValue() != null )
            valueSelected = Integer.parseInt(pickcombo.getValue().toString());
        
        //Recepcion de Terminado
        if (valueSelected==1)
        {
           m_OnlyReceipt = true;
        }
        //Entrega de Material para produccion (Surtimiento)
        if (valueSelected==2)
        {
            m_ListDetail = true;
            m_IsBackflush = true;
        }
        
        
        if (valueSelected==3) {
            m_OnlyReceipt = true;
            m_IsBackflush = true;
        }
        
        //Devolucion
        if (valueSelected==4) {
            m_ListDetail = true;
            m_IsBackflush = true;
        }
        
        if (m_OnlyReceipt) {
            if (locator.getValue() == null)
                JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"NoLocator"), "Info" , JOptionPane.INFORMATION_MESSAGE);
        }


        //if (m_mpc_order != null && movementDate.getValue() != null && locator.getValue()!=null && m_OnlyReceipt)
        if (m_mpc_order != null && movementDate.getValue() != null ) {
            Timestamp m_movementDate = (Timestamp)movementDate.getValue();
            BigDecimal m_toDeliverQty = (BigDecimal)toDeliverQty.getValue();
            int m_M_Location_ID = 0;
            int m_M_Location_IDissue = 0;
            String seguro="";
            int m_M_Product_ID = 0;
            
            
            
            int m2_M_Warehouse_ID = 0;

            Timestamp minGuaranteeDate = m_movementDate;
            BigDecimal m_qtyToDeliver = Env.ZERO;
            BigDecimal m_deliver = Env.ZERO;
            BigDecimal m_scrapQtyComponet = Env.ZERO;

            boolean iscompleteqtydeliver = false;

            int m_C_UOM_ID = 0;

            /*
             *  Entrega de Material (Surtimiento)
             */
            
            /*
             * 
             */
            
            if ((!m_OnlyReceipt && m_IsBackflush) || (m_IsBackflush && m_OnlyReceipt)) {
                // cheak available onhand
                for (int i = 0 ; i < issue.getRowCount(); i++) {
                    IDColumn id = (IDColumn) issue.getValueAt(i, 0);
                    if (id != null && id.isSelected()) {

                        KeyNamePair m_productkey = (KeyNamePair)issue.getValueAt(i,3);
                        KeyNamePair m_uomkey = (KeyNamePair)issue.getValueAt(i,4);

                        m_M_Product_ID = m_productkey.getKey();

                        //System.out.println("Product" + m_M_Product_ID + "Almacen" + m_mpc_order.getM_Warehouse_ID());
                        if (issue.getValueAt(i,4)!=null)
                            m_C_UOM_ID = m_uomkey.getKey();

                        String vacio =""; //Cantidad a surtir
                        if (issue.getValueAt(i,8)!=null && !issue.getValueAt(i,8).toString().equals(vacio)) {
                            String mn = issue.getValueAt(i,8).toString();
                            Integer mni= new Integer(0);
                            BigDecimal mnb = new BigDecimal(issue.getValueAt(i,8).toString());
                            m_qtyToDeliver = mnb; //(BigDecimal)issue.getValueAt(i,6);
                        } // Desperdicio
                        if (issue.getValueAt(i,9)!=null && !issue.getValueAt(i,9).toString().equals(vacio)) {
                            String mns = issue.getValueAt(i,9).toString();
                            Integer mnis= new Integer(0);
                            BigDecimal mnbs = new BigDecimal(issue.getValueAt(i,9).toString());
                            // m_qtyToDeliver = mnbs; //(BigDecimal)issue.getValueAt(i,6);
                            m_scrapQtyComponet =  mnbs;//(BigDecimal)issue.getValueAt(i,8);
                        }
                        if(!m_IsBackflush) {
                            m_qtyToDeliver=Env.ZERO;
                            m_scrapQtyComponet=Env.ZERO;
                        }

                        if (m_qtyToDeliver == null)
                            m_qtyToDeliver = Env.ZERO;

                        if(m_scrapQtyComponet == null)
                            m_scrapQtyComponet = Env.ZERO;

                        Properties ctx = Env.getCtx();
                        MStorage[] storages = null;
                        MProduct product = new MProduct(ctx,m_M_Product_ID,trxName);
                        if (product != null && product.get_ID() != 0 && product.isStocked()) { //Validacion sobre el producto ¿¿ QUE PASA SI FALLA ????
                            MProductCategory pc = MProductCategory.get(ctx, product.getM_Product_Category_ID());
                            String MMPolicy = pc.getMMPolicy();
                            if (MMPolicy == null || MMPolicy.length() == 0) {
                                MClient client = MClient.get(ctx);
                                MMPolicy = client.getMMPolicy();
                            }
                            System.out.println("***** producto:"+product.get_ID() +" instance " +m_mpc_order.getM_AttributeSetInstance_ID());
                            storages = MStorage.getWarehouse(Env.getCtx(), m_mpc_order.getM_Warehouse_ID(), m_M_Product_ID, m_mpc_order.getM_AttributeSetInstance_ID(),product.getM_AttributeSet_ID(), true, minGuaranteeDate,MClient.MMPOLICY_FiFo.equals(MMPolicy) ,trxName);
                        }
                        //fjviejo e-evolution cambio para que te muestre una ventana con todo lo que va a hacer
                        int M_AttributeSetInstance_ID=1;

                        System.out.println("***** value:" +issue.getValueAt(i,2) +".");
                        if(issue.getValueAt(i,2)== null && id.isSelected()) {
                            M_AttributeSetInstance_ID=((Integer)id.getRecord_ID()).intValue();

                        }
                        MAttributeSetInstance minst=null;
                        String desc="";
                        if (M_AttributeSetInstance_ID==1) { //En que caso entraria ACA ¿?
                            System.out.println("***** producto:"+product.get_ID() +" instance " +M_AttributeSetInstance_ID);
                            BigDecimal toIssue = m_qtyToDeliver.add(m_scrapQtyComponet);
                            for (int k = 0; k < storages.length; k++) {
                                MStorage storage = storages[k];
                                //	TODO Selection of ASI
                                if(storage.getQtyOnHand().compareTo(Env.ZERO) == 0)
                                    continue;

                                BigDecimal issueact = toIssue;
                                if (issueact.compareTo(storage.getQtyOnHand()) > 0)
                                    issueact = storage.getQtyOnHand();

                                toIssue = toIssue.subtract(issueact);


                                minst = new MAttributeSetInstance(Env.getCtx(), storage.getM_AttributeSetInstance_ID(), null);
                                desc=minst.getDescription();

                                seguro=seguro +product.getValue() +"\t- " +desc +"\t - " +issueact.setScale(4,BigDecimal.ROUND_HALF_UP) +"\n";// +" " +issue.getValueAt(i,5).toString();

                                System.out.println("***** seguro:"+seguro);

                                if (toIssue.compareTo(Env.ZERO) <= 0)
                                    break;
                            }

                        } else {
                            minst = new MAttributeSetInstance(Env.getCtx(), M_AttributeSetInstance_ID, null);
                            desc=minst.getDescription();
                            BigDecimal qtydelivered = m_qtyToDeliver;
                            seguro=seguro +product.getValue() +"\t- " +desc +"\t - " +qtydelivered.setScale(4,BigDecimal.ROUND_HALF_UP) +"\n";// +" " +issue.getValueAt(i,5).toString();
                            qtydelivered=Env.ZERO;
                        }
                        BigDecimal onHand = Env.ZERO;
                        for (int j = 0; j < storages.length; j++) {
                            MStorage storage = storages[j];
                            onHand = onHand.add(storage.getQtyOnHand());
                        }

                        iscompleteqtydeliver = onHand.compareTo(m_qtyToDeliver.add(m_scrapQtyComponet)) >= 0;
                        if(!iscompleteqtydeliver)
                            break;
                    }
                }

                /*if (!iscompleteqtydeliver) {
                    ADialog.error(m_WindowNo,this,"NoQtyAvailable");
                    return false;
                }*/
                            
                /*
                 *  Aqu� asigno la fecha de inicio de la orden en caso que no est� ya asignada
                 *  Autor: VIT4B
                 *  Fecha: 15/09/2006
                 *
                 */

                if(m_mpc_order.getDateStart() == null || m_mpc_order.getDateStart().compareTo(m_movementDate) > 0)
                {
                    m_mpc_order.setDateStart(m_movementDate);
                    //Comentado por BISion - 13/10/2009 - Santiago Ibañez
                    //m_mpc_order.save();
                }

                /*
                 *  Aqu� genero la cabecera MPC_COST_COLLECTOR_HEADER para que englobe a todos los movimientos
                 *  Autor: VIT4B
                 *  Fecha: 18/08/2006
                 *
                 */

                int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
                int AD_Org_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Org_ID"));

                /**
                 * BISion - 17/11/2008 - Santiago Iba�ez
                 * Se comentaron todos los bloques try-catch dado que no se
                 * daba ning�n tratamiento adecuado a la excepci�n. Cualquier
                 * excepci�n que ocurra ser� tratada por ReceiptIssue() quien
                 * deshar� los cambios realizados sobre la base de datos.
                 */

                String sqlCollector = "SELECT CURRENTNEXT FROM AD_SEQUENCE WHERE NAME = 'MPC_COST_COLLECTOR_HEADER'";

                PreparedStatement pstmt1 = DB.prepareStatement(sqlCollector,trxName);
                ResultSet rs1 = pstmt1.executeQuery();
                if(rs1.next())
                {
                    Cost_Collector_HEADER_ID = rs1.getInt(1);
                }
                rs1.close();
                pstmt1.close();

                int NEXT = Cost_Collector_HEADER_ID + 1;
                sqlCollector = "UPDATE AD_SEQUENCE SET CURRENTNEXT = " + NEXT + " WHERE NAME = 'MPC_COST_COLLECTOR_HEADER'";

                PreparedStatement pstmt2 = DB.prepareStatement(sqlCollector,trxName);
                ResultSet rs2 = pstmt2.executeQuery();
                rs2.close();

                String MOVEMENTTYPE = "P-";
                if(m_OnlyReceipt)
                    MOVEMENTTYPE = "P+";


                sqlCollector = "INSERT INTO MPC_COST_COLLECTOR_HEADER (AD_CLIENT_ID,AD_ORG_ID,ISACTIVE," +
                "CREATED,CREATEDBY,UPDATED,UPDATEDBY,MPC_COST_COLLECTOR_HEADER_ID,MOVEMENTDATE,DESCRIPTION,MPC_ORDER_ID,MOVEMENTTYPE,M_LOCATOR_ID)" +
                "VALUES (" + AD_Client_ID + "," + AD_Org_ID + ",'Y',SYSDATE,1000684," +
                "SYSDATE,1000684," + Cost_Collector_HEADER_ID + ",SYSDATE,''," + order.getValue() + ",'" + MOVEMENTTYPE + "',"+locator.getValue()+")";
                System.out.println("LOG DE MPC_COST_COLLECTOR _REL ... " + sqlCollector);

                PreparedStatement pstmt3 = DB.prepareStatement(sqlCollector,trxName);
                ResultSet rs3 = pstmt3.executeQuery();
                rs3.close();
                pstmt3.close();
                
                /* GENEOS - Pablo Velazquez
                * 18-10-2013
                * Desreservo lo que todavia no fue surtido, ya que ya no habra mas surtimientos para la orden
                * luego de efectuada una devolucion
                */         
               if (valueSelected==4){ //Devolucion
                   if (!m_mpc_order.UnreserveStock(null))
                        return false;
               }
                
                //Do Issue if ( m_IsBackflush =  YES AND iscompleteqtydeliver = YES)
               //Fix
                iscompleteqtydeliver = true;
                if (m_IsBackflush  && iscompleteqtydeliver) {
                    
                    // Variable para controlar cantidad acumulada sin reservar
                    BigDecimal cantidadAgrupada = Env.ZERO;
                    
                    int MPC_Order_BOMLine_ID = 0;      
                    
                    // Variable para controlar cambio de producto (Para desreservar en cascada en Surtimiento)
                    int old_m_M_Product_ID = 0;
                    // Variable para controlar linea del producto (Para desreservar en cascada en Surtimiento)
                    int old_MPC_Order_BOMLine_ID=0;
                    
                    m_M_Product_ID = 0;
                    
                    
                    for (int ok = 0 ; ok < issue.getRowCount(); ok++) {                   
                        
                        IDColumn id = (IDColumn) issue.getValueAt(ok, 0);
                        if (id != null && id.isSelected()) {
                                             
                            KeyNamePair m_productkey = (KeyNamePair)issue.getValueAt(ok,3);
                            KeyNamePair m_uomkey = (KeyNamePair)issue.getValueAt(ok,4);

                            // cambio para sacar de un lote de otra organizacion fjviejo e-evolution panalab
                            if (issue.getValueAt(ok,14)!=null && !issue.getValueAt(ok,14).toString().equals("")) {
                                KeyNamePair m_warkey = (KeyNamePair)issue.getValueAt(ok,14);
                                m2_M_Warehouse_ID = m_warkey.getKey();
                                System.out.println("***** Warehouse del lote seleccionado " +m2_M_Warehouse_ID);
                            }
                            // cambio para sacar de un lote de otra organizacion fjviejo e-evolution panalab end

                            m_M_Product_ID = m_productkey.getKey();

                            MPC_Order_BOMLine_ID=0;
                            int M_AttributeSetInstance_ID=1;

                            System.out.println("***** value:" +issue.getValueAt(ok,2) +".");
                            if(issue.getValueAt(ok,2)== null && id.isSelected()) {
                                M_AttributeSetInstance_ID=((Integer)id.getRecord_ID()).intValue();

                                StringBuffer sql = new StringBuffer("SELECT obl.MPC_Order_BOMLine_ID");
                                sql.append("  FROM MPC_Order_BOMLine obl ");

                                sql.append(" WHERE obl.M_Product_ID = " +m_M_Product_ID +" and obl.MPC_Order_ID="+ order.getValue());

                                log.log(Level.INFO, "VOrderReciptIssue.executeQuery - SQL Orderbom of instances ", sql.toString());
                               
                                PreparedStatement pstmt = DB.prepareStatement(sql.toString(),trxName);
                                //pstmt.setInt(1, AD_Client_ID);
                                ResultSet rs = pstmt.executeQuery();
                                //
                                while (rs.next()) {
                                    MPC_Order_BOMLine_ID = rs.getInt(1);
                                }
                                MPC_Order_BOMLine_ID = (Integer)issue.getValueAt(ok,18);
                                rs.close();
                                pstmt.close();

                                //MPC_Order_BOMLine_ID = ((Integer)id.getRecord_ID()).intValue();
                            } else if(id.isSelected() && issue.getValueAt(ok,2)!= null) {
                                MPC_Order_BOMLine_ID = ((Integer)id.getRecord_ID()).intValue();
                            }

                            if(issue.getValueAt(ok,4)!=null)
                                m_C_UOM_ID = m_uomkey.getKey();
                            m_qtyToDeliver = Env.ZERO;
                            m_scrapQtyComponet = Env.ZERO;
                            String vacio ="";
                            if (issue.getValueAt(ok,8)!=null && !issue.getValueAt(ok,8).toString().equals(vacio)) {
                                String mn1 = issue.getValueAt(ok,8).toString();
                                Integer mni1= new Integer(0);
                                BigDecimal mnb1 = new BigDecimal(issue.getValueAt(ok,8).toString());
                                m_qtyToDeliver = mnb1; //(BigDecimal)issue.getValueAt(i,6);
                                // m_qtyToDeliver = (BigDecimal)issue.getValueAt(ok,6);
                            }
                            if (issue.getValueAt(ok,9)!=null && !issue.getValueAt(ok,9).toString().equals(vacio)) {
                                String mns1 = issue.getValueAt(ok,9).toString();
                                Integer mnis1= new Integer(0);
                                BigDecimal mnbs1 = new BigDecimal(issue.getValueAt(ok,9).toString());
                                // m_qtyToDeliver = mnbs; //(BigDecimal)issue.getValueAt(i,6);
                                m_scrapQtyComponet =  mnbs1;//(BigDecimal)issue.getValueAt(i,8);
                            }
                            //m_scrapQtyComponet =  (BigDecimal)issue.getValueAt(ok,8);
                            if (m_qtyToDeliver == null)
                                m_qtyToDeliver = Env.ZERO;

                            if(m_scrapQtyComponet == null)
                                m_scrapQtyComponet = Env.ZERO;

                            BigDecimal onHand = Env.ZERO;

                            Properties ctx = Env.getCtx();
                            MStorage[] storages = null;
                            int Loteselected=0;
                            MProduct product = new MProduct(ctx,m_M_Product_ID,trxName);
                            if (product != null && product.get_ID() != 0 && product.isStocked()) {

                                MProductCategory pc = MProductCategory.get(ctx, product.getM_Product_Category_ID());
                                String MMPolicy = pc.getMMPolicy();
                                if (MMPolicy == null || MMPolicy.length() == 0) {
                                    MClient client = MClient.get(ctx);
                                    MMPolicy = client.getMMPolicy();
                                }

                                if(M_AttributeSetInstance_ID==1) {
                                    System.out.println("***** producto:"+product.get_ID() +" instance " +M_AttributeSetInstance_ID);
                                    storages = MStorage.getWarehouse(Env.getCtx(), m_mpc_order.getM_Warehouse_ID(), m_M_Product_ID, m_mpc_order.getM_AttributeSetInstance_ID(), product.getM_AttributeSet_ID(), true , minGuaranteeDate,MClient.MMPOLICY_FiFo.equals(MMPolicy),trxName);
                                    Loteselected=m_mpc_order.getM_AttributeSetInstance_ID();
                                } else {   // cambio para sacar de un lote de otra organizacion fjviejo e-evolution panalab
                                    //storages = MStorage.getWarehouse(Env.getCtx(), m_mpc_order.getM_Warehouse_ID(), m_M_Product_ID, M_AttributeSetInstance_ID,product.getM_AttributeSet_ID() , false , minGuaranteeDate,MClient.MMPOLICY_FiFo.equals(MMPolicy),null);
                                    storages = MStorage.getWarehouse(Env.getCtx(), m2_M_Warehouse_ID, m_M_Product_ID, M_AttributeSetInstance_ID,product.getM_AttributeSet_ID() , false , minGuaranteeDate,MClient.MMPOLICY_FiFo.equals(MMPolicy),trxName);
                                    // cambio para sacar de un lote de otra organizacion fjviejo e-evolution panalab end
                                    System.out.println("***** else:"+product.get_ID() +" instance " +M_AttributeSetInstance_ID);
                                    Loteselected=M_AttributeSetInstance_ID;
                                }
                            }
                            for (int j = 0; j < storages.length; j++) {
                                MStorage storage = storages[j];
                                onHand = onHand.add(storage.getQtyOnHand());
                                System.out.println("***** storages:"+onHand +" storage " +storage.toString());
                            }

                            System.out.println("***** bomline " +MPC_Order_BOMLine_ID + " Existencia " + onHand);
                            /*
                             * 06-07-2011 Camarzana Mariano
                             * Anteriormente se pasaban todas las ubicaciones correspondientes a una determinada
                             * partida - producto - Almacen ya que se empleaba una sola ubicacion (M_Locator), en cambio
                             * ahora se comenzo a utilizar varias ubicaciones de un determinado almacen para surtir una orden. Por lo
                             * tanto se debe pasar como parametro al metodo createIsuue un unico almacen del cual se debe decrementar el stock.
                             */
                            
                            //Recupero la ubicacion seleccionada
                            
                            MLocator loc = new MLocator(Env.getCtx(),recuperarLote(issue.getValueAt(ok,13).toString()),null);
                            MStorage storageSeleccionado = MStorage.getCreate(Env.getCtx(), loc.getM_Locator_ID(), m_M_Product_ID, M_AttributeSetInstance_ID, null);
                            ArrayList<MStorage> list = new ArrayList<MStorage>();
                            list.add(storageSeleccionado);
                            MStorage[] storagesSel = new MStorage[list.size()];
                            storagesSel[0] = storageSeleccionado;
                            list.toArray(storagesSel);
                            
                            
                            BigDecimal diffReserved = BigDecimal.ZERO;
                            //Instancio control de antes de createIssue para calcular
                            //cuanto se va a desreservar!
                            MMPCOrderQtyReserved qtyRes = MMPCOrderQtyReserved.get(Env.getCtx(),storageSeleccionado,MPC_Order_BOMLine_ID,trxName);
                            
                            
                            
                            if ( qtyRes != null ) {
                                BigDecimal storageReservedBefore = qtyRes.getRemainingQty();
                                BigDecimal storageReservedAfter = storageReservedBefore.subtract(m_qtyToDeliver);
                                //Si entrego mas de lo que tenia disponible del reservado para la partida, entonces mi reservado pasa a CERO
                                if ( storageReservedAfter.signum() == -1 )
                                    storageReservedAfter=BigDecimal.ZERO;

                                //Si lo que devuelvo + lo que tenia, es mayor a lo que habia reservado al liberarla orden, entonces el reservado
                                //pasa a ser ese total
                                if ( storageReservedAfter.compareTo(qtyRes.getTotalQty()) > 0 )
                                    storageReservedAfter=qtyRes.getTotalQty();

                                //Cantidad que se desreserva realmente
                                diffReserved = storageReservedBefore.subtract(storageReservedAfter);
                        }
                            
                            
                            createIssue( MPC_Order_BOMLine_ID  , m_movementDate , m_qtyToDeliver , m_scrapQtyComponet , Env.ZERO, storagesSel,trxName);

                            /*
                             *  Aqui asigno la fecha de inicio de la orden en caso que no estuviera ya asignada
                             *  Autor: VIT4B
                             *  Fecha: 15/09/2006
                             *
                             */

                            if(m_mpc_order.getDateStart() == null || m_mpc_order.getDateStart().compareTo(m_movementDate) > 0)                            {
                                m_mpc_order.setDateStart(m_movementDate);
                                m_mpc_order.save();
                            }                           
                            
                            
                             /*
                            * GENEOS - Pablo Velazquez
                            * 10/10/2013
                            * Si cambie de BomLine entonces desreservo lo acumulado que no fue desreservado por los
                            * CostCollector del producto anterior
                            */

                           if (MPC_Order_BOMLine_ID != old_MPC_Order_BOMLine_ID) {
                               if ( old_MPC_Order_BOMLine_ID != 0 && old_m_M_Product_ID!=0 && cantidadAgrupada.signum() == 1){
                                   if ( !unreserveFeFo(m_mpc_order.getMPC_Order_ID(),old_m_M_Product_ID,old_MPC_Order_BOMLine_ID,cantidadAgrupada,trxName) ){
                                       log.severe("No se pudo desreservar para producto: "+m_M_Product_ID);
                                       return false;
                                   }
                                   cantidadAgrupada=Env.ZERO;
                               }
                               old_MPC_Order_BOMLine_ID = MPC_Order_BOMLine_ID;
                               old_m_M_Product_ID = m_M_Product_ID;
                           }
                            
                            
                            
                            //Agrupo cantidad que no se desreservo
                            cantidadAgrupada = cantidadAgrupada.add(m_qtyToDeliver.subtract(diffReserved));                           
                        }
                        
                    }
                    /*
                    * GENEOS - Pablo Velazquez
                    * 11/10/2013
                    * Si termine de iterar y tengo cantidadAgrupada -> tengo que desreservar del ultimo producto
                    */
                   if ( cantidadAgrupada.signum() == 1){
                       if ( !unreserveFeFo(m_mpc_order.getMPC_Order_ID(),m_M_Product_ID,MPC_Order_BOMLine_ID,cantidadAgrupada,trxName) ){
                           log.severe("No se pudo desreservar para producto: "+m_M_Product_ID);
                           return false;
                       }
                       cantidadAgrupada=Env.ZERO;
                   }
 
                }                             
                m_mpc_order.setDescription(m_mpc_order.getDescription());
                m_mpc_order.save();
            } //Fin entrega de Material
   
            /*
             *  Recepcion de Producto Terminado (Producto manufacturado)
             */

            // Do Receipt if (m_OnlyReceipt = YES AND m_IsBackflush = NO) OR (m_IsBackflush = YES  AND m_OnlyReceipt = YES AND iscompleteqtydeliver)
            if ( (m_OnlyReceipt && !m_IsBackflush) || (m_IsBackflush && m_OnlyReceipt && iscompleteqtydeliver) ) {
                m_M_Location_ID = ((Integer)locator.getValue()).intValue();
                Integer m_M_AttributeSetInstance_ID = (Integer)attribute.getValue();
                int AttributeSetInstance =0;


                if (m_M_AttributeSetInstance_ID!=null)
                    AttributeSetInstance =m_M_AttributeSetInstance_ID.intValue();
                MAttributeSetInstance asi = new MAttributeSetInstance(Env.getCtx(), m_M_AttributeSetInstance_ID, trxName);

                /*
                 * GENEOS - Pablo Velazquez
                 * 19/07/2013
                 * Si ya existia una recepcion para esta orden de manufactura, y en la recepcion se
                 * indica la opcion "Generar Sublotes Automaticamente" entonces creo una nueva partida
                 * con el sublote correspondiente.
                 */
                if (autoGenerateLot.isSelected()) {
                    if ( m_mpc_order.getPartidas().length >= 1 ) {
                        // Creo nueva Partida
                        MAttributeSetInstance subAsi = MAttributeSetInstance.get(Env.getCtx(),
                                        0, m_mpc_order.getM_Product_ID());
                        subAsi.set_TrxName(trxName);
                        if (subAsi == null){
                            //Error
                            log.severe("No se pudo crear la nueva partida");
                            return false;
                        }
                        //Obtengo lote principal.
                        MLot mainLot = new MLot(Env.getCtx(),asi.getM_Lot_ID(),trxName);

                        //Creo sub lote
                        MLot subLot = subAsi.createSubLot(mainLot);
                        if (subLot == null){
                            //Error
                            log.severe("No se pudo crear el sublote");
                            return false;
                        }
                        //Reasigno partida
                        asi = subAsi;
                    }
                }

                BigDecimal m_scrapQty = (BigDecimal)scrapQty.getValue();
                BigDecimal m_rejectQty = (BigDecimal)rejectQty.getValue();

                if(m_scrapQty == null)
                    m_scrapQty = Env.ZERO;

                if(m_rejectQty == null)
                    m_rejectQty = Env.ZERO;

                /*
                 *  Aqu� genero la cabecera MPC_COST_COLLECTOR_HEADER para que englobe a todos los movimientos
                 *  Autor: VIT4B
                 *  Fecha: 21/09/2006
                 *
                 */

                int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
                int AD_Org_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Org_ID"));

                /**
                 * BISion - 21/10/2009 - Santiago Iba�ez
                 * COM-PAN-REQ-08.001.01
                 */
                try{
                    asi.setGuaranteeDate(fechaVencimiento.getTimestamp());
                    asi.setBUNDLE(fieldBundle.getText());
                    asi.setDescription();
                    if ( !asi.save() ){
                        log.severe("Fallo al guardar los datos de la partida");
                        return false;
                    }
                }
                catch(Exception e){
                    System.out.println("No se actualizo la fecha de garantia de la partida por ser nula");
                }

                String sqlCollector = "SELECT CURRENTNEXT FROM AD_SEQUENCE WHERE NAME = 'MPC_COST_COLLECTOR_HEADER'";

                try {

                    PreparedStatement pstmt1 = DB.prepareStatement(sqlCollector,trxName);

                    ResultSet rs1 = pstmt1.executeQuery();
                    if(rs1.next())
                    {
                        Cost_Collector_HEADER_ID = rs1.getInt(1);
                    }
                    rs1.close();
                    pstmt1.close();

                    int NEXT = Cost_Collector_HEADER_ID + 1;
                    sqlCollector = "UPDATE AD_SEQUENCE SET CURRENTNEXT = " + NEXT + " WHERE NAME = 'MPC_COST_COLLECTOR_HEADER'";

                    pstmt1 = DB.prepareStatement(sqlCollector,trxName);
                    rs1 = pstmt1.executeQuery();
                    rs1.close();
                    pstmt1.close();

                    String MOVEMENTTYPE = "P-";
                    if(m_OnlyReceipt)
                        MOVEMENTTYPE = "P+";

                    sqlCollector = "INSERT INTO MPC_COST_COLLECTOR_HEADER (AD_CLIENT_ID,AD_ORG_ID,ISACTIVE," +
                    "CREATED,CREATEDBY,UPDATED,UPDATEDBY,MPC_COST_COLLECTOR_HEADER_ID,MOVEMENTDATE,DESCRIPTION,MPC_ORDER_ID,MOVEMENTTYPE,M_LOCATOR_ID) " +
                    "VALUES (" + AD_Client_ID + "," + AD_Org_ID + ",'Y',SYSDATE,1000684," +
                    "SYSDATE,1000684," + Cost_Collector_HEADER_ID + ",SYSDATE,''," + order.getValue() + ",'" + MOVEMENTTYPE + "',"+locator.getValue()+")";

                    System.out.println("LOG DE MPC_COST_COLLECTOR _REL ... " + sqlCollector);
                    pstmt1 = DB.prepareStatement(sqlCollector,trxName);
                    rs1 = pstmt1.executeQuery();
                    rs1.close();
                    pstmt1.close();
                } catch(SQLException obl) {
                    log.severe("Error al crear MPC_COST_COLLECTOR_HEADER: "+obl.getMessage());
                    return false;
                }
                int C_DocType_ID = getDocType(MDocType.DOCBASETYPE_ManufacturingOrderReceipt);
                if (m_toDeliverQty.compareTo(Env.ZERO)>0 || m_scrapQty.compareTo(Env.ZERO)>0 || m_rejectQty.compareTo(Env.ZERO)>0) //fjviejo e-evolution no hace transacciones en 0
                    createCollector(m_mpc_order.getM_Product_ID(), m_M_Location_ID , asi.getM_AttributeSetInstance_ID() , m_movementDate , m_toDeliverQty , m_scrapQty , m_rejectQty, C_DocType_ID , 0, MMPCCostCollector.MOVEMENTTYPE_ProductionPlus,trxName);

                m_mpc_order = new MMPCOrder(Env.getCtx(), m_mpc_order.getMPC_Order_ID(), trxName);

                BigDecimal qty = (BigDecimal) toDeliverQty.getValue();
                qty = qty.setScale(0);
                enviarMensajeRecepcionTerminado(m_mpc_order.getM_Product_ID(), m_M_AttributeSetInstance_ID, qty, m_M_Location_ID);
        
                /*
                 *  Por favor optimizar este c�digo ....
                 *  Verifica si el rol tiene permiso sobre los procesos de recepci�n y entrega ...
                 *  Autor: VIT4B
                 *  Fecha: 21/09/2006
                 *
                 */
                int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
                MRole rol = new MRole(Env.getCtx(),AD_Role_ID,trxName);
                boolean OR = true;
                boolean OI = true;
                boolean IB = true;
                if (rol.getProcessAccess(1000112) == null)
                    OR = false;
                if (rol.getProcessAccess(1000113) == null)
                    OI = false;
                if (rol.getProcessAccess(1000114) == null)
                    IB = false;
                if (OR) {
                    if(rol.getProcessAccess(1000112) != null)
                    {
                        if(rol.getProcessAccess(1000112).booleanValue()) {
                            if (ADialog.ask(m_WindowNo,this,Msg.translate(Env.getCtx(), "IsCloseDocument") , Msg.translate(Env.getCtx(), "DocumentNo") + m_mpc_order.getDocumentNo())) {
                                m_mpc_order.setDateFinish(m_movementDate);
                                m_mpc_order.closeIt();
                                m_mpc_order.save();
                            }

                        }
                    }

                    if(rol.getProcessAccess(1000114) != null)
                    {
                        if (rol.getProcessAccess(1000114).booleanValue()){
                            if (ADialog.ask(m_WindowNo,this,Msg.translate(Env.getCtx(), "IsCloseDocument") , Msg.translate(Env.getCtx(), "DocumentNo") + m_mpc_order.getDocumentNo())) {
                                m_mpc_order.setDateFinish(m_movementDate);
                                m_mpc_order.closeIt();
                                m_mpc_order.save();
                            }
                        }
                    }
                }
                if (!OR && !OI && !IB) {
                    if (ADialog.ask(m_WindowNo,this,Msg.translate(Env.getCtx(), "IsCloseDocument") , Msg.translate(Env.getCtx(), "DocumentNo") + m_mpc_order.getDocumentNo())) {
                        m_mpc_order.setDateFinish(m_movementDate);
                        m_mpc_order.closeIt();
                        m_mpc_order.save();
                    }
                }

                m_mpc_order.setDateDelivered(m_movementDate);
                if(m_mpc_order.getDateStart() == null || m_mpc_order.getDateStart().compareTo(m_movementDate) > 0)
                    m_mpc_order.setDateStart(m_movementDate);

                BigDecimal DQ = Env.ZERO;
                BigDecimal SQ = Env.ZERO;
                BigDecimal OQ = Env.ZERO;
                if (deliveredQty.getValue() != null)
                    DQ = (BigDecimal)deliveredQty.getValue();
                if (scrapQty.getValue() != null)
                    SQ = (BigDecimal)scrapQty.getValue();
                if (toDeliverQty.getValue() != null)
                    OQ = (BigDecimal)toDeliverQty.getValue();

                System.out.println("***** DQ "+DQ +" SQ " +SQ +" OQ "+OQ);
                if(DQ.add(SQ).compareTo(OQ) >= 0)
                    m_mpc_order.setDateFinish(m_movementDate);

                /**
                 * BISion - 22/10/2009 - Santiago Ibañez
                 * COM-PAN-REQ-08.002.01
                 */
                if (fieldBundle.getText()!=null || !fieldBundle.getText().equals("")){
                    if (m_mpc_order.getBUNDLE()!=null&&!m_mpc_order.getBUNDLE().equals(""))
                        m_mpc_order.setBUNDLE(m_mpc_order.getBUNDLE()+" - "+fieldBundle.getText());
                    else
                        m_mpc_order.setBUNDLE(fieldBundle.getText());
                }
                //fin modificacion BISion

                m_mpc_order.save();
                ADialog.info(m_WindowNo,this, Msg.translate(Env.getCtx(), "OnlyReceipt") ,Msg.translate(Env.getCtx(), "DocumentNo") + m_mpc_order.getDocumentNo());

                 /*
                 *  Vit4B - Solo imprime si actualizo
                 *
                 */

                MPrintFormat format = null;
                Language language = Language.getLoginLanguage();		//	Base Language
     
                /*
                 * GENEOS - Pablo Velazquez
                 * 23/07/2013
                 * Se realiza un commit de los cambios antes de imprimir el documento
                 */

                Trx trxAux = Trx.get(trxName, false);

                if ( trxAux == null ){
                    log.severe("No existe la transaccion: "+trxAux.getTrxName());
                    return false;
                }

                if ( !trxAux.commit() ){
                    log.severe("No se pudieron aplicar cambios en transaccion: "+trxAux.getTrxName());
                    return false;
                }

                if (ADialog.ask(m_WindowNo, this, "Print Document")) {
                    int AD_PrintFormat_ID = getPrintFormat();
                    format = MPrintFormat.get(Env.getCtx(), AD_PrintFormat_ID, false);
                    format.setLanguage(language);
                    format.setTranslationLanguage(language);

                    //	query
                    MQuery query = new MQuery("RV_SURTIMIENTO_RECEP_TERMINADO");
                    query.addRestriction("RV_RECEPCIONENTREGA_HEAD_ID", MQuery.EQUAL, Cost_Collector_HEADER_ID);

                    //	Engine
                    PrintInfo info = new PrintInfo("RV_SURTIMIENTO_RECEP_TERMINADO",getTable(), Cost_Collector_HEADER_ID);
                    ReportEngine re = new ReportEngine(Env.getCtx(), format, query, info);
                    new Viewer(re);

                }
                return true;
            } //Fin Recepcion de Producto Terminado
            else {
                ADialog.info(m_WindowNo,this, Msg.translate(Env.getCtx(), "OnlyReceipt") ,Msg.translate(Env.getCtx(), "DocumentNo") + m_mpc_order.getDocumentNo());
                 /*
                 *  Vit4B - Solo imprime si actualizo
                 *
                 */

                MPrintFormat format = null;
                Language language = Language.getLoginLanguage();		//	Base Language
                
                /*
                 * GENEOS - Pablo Velazquez
                 * 23/07/2013
                 * Se realiza un commit de los cambios antes de imprimir el documento
                 */

                Trx trxAux = Trx.get(trxName, false);

                if ( trxAux == null ){
                    log.severe("No existe la transaccion: "+trxAux.getTrxName());
                    return false;
                }

                if ( !trxAux.commit() ){
                    log.severe("No se pudieron aplicar cambios en transaccion: "+trxAux.getTrxName());
                    return false;
                }

                if (ADialog.ask(m_WindowNo, this, "Print Document")) {
                    format = MPrintFormat.get(Env.getCtx(), 5000189, false);
                    format.setLanguage(language);
                    format.setTranslationLanguage(language);

                    //	query
                    MQuery query = new MQuery("RV_RECEPCIONENTREGA_HEAD");
                    query.addRestriction("RV_RECEPCIONENTREGA_HEAD_ID", MQuery.EQUAL, Cost_Collector_HEADER_ID);

                    //	Engine
                    PrintInfo info = new PrintInfo("RV_RECEPCIONENTREGA_HEAD",1000107, Cost_Collector_HEADER_ID);
                    ReportEngine re = new ReportEngine(Env.getCtx(), format, query, info);
                    new Viewer(re);
                }
                return true;
            }
        }
        return false;
    }

    /**************************************************************************
     * 	Create Line
     *	@param order order
     *	@param orderLine line
     *	@param qty qty
     */
    private void createIssue(int MPC_OrderBOMLine_ID , Timestamp movementdate, BigDecimal qty ,  BigDecimal qtyscrap , BigDecimal qtyreject, MStorage[] storages, String trxName) throws SQLException{
        if (qty.compareTo(Env.ZERO) == 0)
            return;

        //	Inventory Lines
        BigDecimal toIssue = qty.add(qtyscrap);
        for (int i = 0; i < storages.length; i++) {
            MStorage storage = storages[i];
            BigDecimal issue = toIssue;
            if (issue.compareTo(storage.getQtyOnHand()) > 0)
                issue = storage.getQtyOnHand();
            log.info("createCollector - ToIssue" + issue); //pba
            log.fine("createCollector - ToIssue" + issue);
            MMPCOrderBOMLine mpc_orderbomLine = new MMPCOrderBOMLine(Env.getCtx() , MPC_OrderBOMLine_ID, trxName);
            if ( mpc_orderbomLine.getQtyBatch().compareTo(Env.ZERO) == 0 && mpc_orderbomLine.getQtyBOM().compareTo(Env.ZERO) == 0 ) {
                // Method Variance
                int C_DocType_ID = getDocType(MDocType.DOCBASETYPE_ManufacturingOrderMethodVariation);
                log.fine ("#### MODIFICACION RECEPCION ENTREGA: product_id - " + mpc_orderbomLine.getM_Product_ID() + " MPC_OrderBOMLine_ID - " + MPC_OrderBOMLine_ID);
                createCollector(mpc_orderbomLine.getM_Product_ID(),storage.getM_Locator_ID(),storage.getM_AttributeSetInstance_ID(), movementdate , issue , qtyscrap , qtyreject , C_DocType_ID , MPC_OrderBOMLine_ID,MMPCCostCollector.MOVEMENTTYPE_Production_,trxName);
            } 
            else
            {
                int C_DocType_ID = getDocType(MDocType.DOCBASETYPE_ManufacturingOrderIssue);
                log.fine ("#### MODIFICACION RECEPCION ENTREGA: product_id - " + mpc_orderbomLine.getM_Product_ID() + " MPC_OrderBOMLine_ID - " + MPC_OrderBOMLine_ID);
                createCollector(mpc_orderbomLine.getM_Product_ID(),storage.getM_Locator_ID(),storage.getM_AttributeSetInstance_ID(), movementdate , issue, qtyscrap , qtyreject, C_DocType_ID , MPC_OrderBOMLine_ID, MMPCCostCollector.MOVEMENTTYPE_Production_,trxName);
            }
            toIssue = toIssue.subtract(issue);
            if (toIssue.compareTo(Env.ZERO) == 0)
                break;
        }
    }

    private void createCollector(int M_Product_ID ,int M_Locator_ID , int M_AttributeSetInstance_ID, Timestamp movementdate , BigDecimal qty , BigDecimal scrap , BigDecimal reject, int C_DocType_ID, int MPC_Order_BOMLine_ID, String MovementType, String trxName) throws SQLException {

        MMPCCostCollector MPC_Cost_Collector=null;
        MPC_Cost_Collector = new MMPCCostCollector(Env.getCtx(), 0,trxName);
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
           
        /*
         * AGREGADO PARA AGRUPAR LAS ORDENESSSS
        */

        int Cost_Collector_REL_ID = 0;
        int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
        int AD_Org_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Org_ID"));

        String sqlCollector = "SELECT CURRENTNEXT FROM AD_SEQUENCE WHERE NAME = 'MPC_COST_COLLECTOR_REL'";

        PreparedStatement pstmt1 = DB.prepareStatement(sqlCollector,trxName);
        ResultSet rs1 = pstmt1.executeQuery();
        if(rs1.next())
        {
            Cost_Collector_REL_ID = rs1.getInt(1);
        }
        rs1.close();
        pstmt1.close();


        int NEXT = Cost_Collector_REL_ID + 1;
        sqlCollector = "UPDATE AD_SEQUENCE SET CURRENTNEXT = " + NEXT + " WHERE NAME = 'MPC_COST_COLLECTOR_REL'";
        PreparedStatement pstmt2 = DB.prepareStatement(sqlCollector,trxName);
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();

        int Cost_Collector_ID = MPC_Cost_Collector.getMPC_Cost_Collector_ID();
        int Cost_Collector_Header_ID = getHeaderID();

        sqlCollector = "INSERT INTO MPC_COST_COLLECTOR_REL (AD_CLIENT_ID,AD_ORG_ID,ISACTIVE," +
                "CREATED,CREATEDBY,UPDATED,UPDATEDBY,MPC_COST_COLLECTOR_REL_ID,MPC_COST_COLLECTOR_ID,MPC_COST_COLLECTOR_HEADER_ID)" +
                "VALUES (" + AD_Client_ID + "," + AD_Org_ID + ",'Y',SYSDATE,1000684," +
                "SYSDATE,1000684," + Cost_Collector_REL_ID + "," + Cost_Collector_ID + "," + Cost_Collector_Header_ID + ")";

        System.out.println("LOG DE MPC_COST_COLLECTOR_REL ... " + sqlCollector);

        PreparedStatement pstmt3 = DB.prepareStatement(sqlCollector,trxName);
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
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
        //IsBackflush.setVisible(true);
        //IsBackflush.setValue(new Boolean(false));
        //locator.setValue(getLocation("Cuarentena Famatina U."));
        locatorLabel.setVisible(true);
        locator.setVisible(true);
        //locator.setReadWrite(false);
        mostrarVencimiento();
        labelBundle.setVisible(true);
        fieldBundle.setText("");
        fieldBundle.setVisible(true);
        autoGenerateLot.setVisible(true);
        labelAutoGenerateLot.setVisible(true);
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
        //locator.setReadWrite(true);
        issue.setVisible(true);
        labelVencimiento.setVisible(false);
        fechaVencimiento.setVisible(false);
        autoGenerateLot.setVisible(false);
        labelAutoGenerateLot.setVisible(false);
        labelBundle.setVisible(false);
        fieldBundle.setVisible(false);
        // executeQuery();
    }
    /**
     *  Complete generating invoices.
     *  Called from Unlock UI
     *  @param pi process info
     */
    private void ReceiptIssue() {

        boolean m_OnlyReceipt = false;
        boolean m_IsBackflush = false;
        boolean m_ListDetail = false;

        boolean entrega = false;

        int valueSelected = Integer.parseInt(pickcombo.getValue().toString());


        if ( pickcombo.getSelectedIndex() == 0 ){
            JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"NoRule"), "Info" , JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (valueSelected==1)
        {
           m_OnlyReceipt = true;
        }

        if (valueSelected==2)
        {
            m_ListDetail = true;
            entrega = true;
        }

        if (valueSelected==3) {
            m_OnlyReceipt = true;
            m_IsBackflush = true;
        }

        if (valueSelected==4) {
            m_ListDetail = true;
            entrega = false;
        }



        if (movementDate.getValue() == null) {
            JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"NoDate"), "Info" , JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (m_OnlyReceipt) {
            if (locator.getValue() == null) {
                JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"NoLocator"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        /** BISion - 16/11/2009 - Santiago Ibañez
         * COM-PAN-CHR-08.002.01
         */
         if (valueSelected == 1 && fechaVencimiento.isMandatory()) {
            if (fechaVencimiento.getValue() == null){
                JOptionPane.showMessageDialog(null,"Ingrese una fecha de vencimiento para la partida", "Info" , JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        /** BISion - 03/12/2009 - Santiago Ibañez
         *  COM-PAN-REQ-08.005.01
         */
         if (valueSelected == 1 && !tieneSurtimientosMateriales()) {
                JOptionPane.showMessageDialog(null,"Avisar a Produccion: Faltan surtir componentes de la formula", "Info" , JOptionPane.INFORMATION_MESSAGE);
                return;
        }
         
        /*
        * GENEOS - Pablo Velazquez
        * 21/10/2013
        * Se agregan carteles de informacion/confirmacion
        */
       
        if (order.getValue() != null) {

            //instantiate order (No TRX Needed)
            MMPCOrder auxOrder = new MMPCOrder(Env.getCtx(),(Integer)order.getValue(),null);        
            
            //Producto Terminado y no tiene recepcion
            /*if ( valueSelected== 1 && !auxOrder.hasDelivered()) {
                int aux = JOptionPane.showConfirmDialog(null,"Si efectua una Recepción de Producto terminado para esta orden, no podra efectuar mas surtimientos sobre la misma. ¿Seguro que desa continuar?", "Producto Terminado", JOptionPane.ERROR_MESSAGE);
                if ( aux!=0 ) {
                    return;
                }   
            }*/

            //Devolucion y no existe devolucion
            if ( valueSelected== 4 && !auxOrder.hasReturns() ) {
                int aux = JOptionPane.showConfirmDialog(null,"Si efectua una Devolucion de Materiales para esta orden no podra efectuar mas Recepciones de Producto Terminado, ni surtimientos sobre la misma. ¿Seguro que desea continuar?", "Devolución", JOptionPane.ERROR_MESSAGE);

                if ( aux!=0 ) {
                    return;
                }   
            }
        }
        
       /*
        * GENEOS - Pablo Velazquez
        * 21/10/2013
        * Se Agrega cartel de confirmacion si se surte de partida que no tiene reservado
        */
       //Si es surtimiento
       if ( valueSelected== 2 ) {
           boolean sobrepasaReservado = false;
           for (int i = 0 ; i < issue.getRowCount(); i++) {
               IDColumn id = (IDColumn) issue.getValueAt(i, 0);
               if (id != null && id.isSelected()) {

                   BigDecimal cantidadAEntregar = new BigDecimal((String)issue.getValueAt(i,8));
                   BigDecimal cantidadAReservadaDisponible = (BigDecimal)issue.getValueAt(i,11);
                   if ( cantidadAEntregar != null && cantidadAEntregar.compareTo(cantidadAReservadaDisponible)>0 ){
                       sobrepasaReservado = true;
                       break;
                   }
               }
           }
           if (sobrepasaReservado) {
               int aux = JOptionPane.showConfirmDialog(null,"Esta surtiendo de partidas que no fueron reservadas para esta orden, o esta surtiendo mas de lo resevado. ¿Seguro que desa continuar?", "Cantidades no reservadas", JOptionPane.ERROR_MESSAGE);

               if ( aux!=0 ) {
                   return;
               }   
           }
       }
       
       /*
        * GENEOS - Pablo Velazquez
        * 25/10/2013
        * Se Agrega cartel de confirmacion si se surte por encima del 20% alguna de las lineas
        */
       //Si es surtimiento
       if ( valueSelected== 2 ) {
           int m_product_id = 0;
           int old_m_product_id = 0;
           int mpc_order_bomlie_id = 0;
           int old_mpc_order_bomline_id = 0;
           boolean sobrepasaSurtimiento = false;
           String sobrepasan = "";
           BigDecimal cantidadAgrupada = Env.ZERO;
            KeyNamePair m_productkey = null;
           for (int i = 0 ; i < issue.getRowCount(); i++) {
               IDColumn id = (IDColumn) issue.getValueAt(i, 0);
               if (id != null && id.isSelected()) {
                   
                    mpc_order_bomlie_id = (Integer)issue.getValueAt(i,18);

                    m_productkey = (KeyNamePair)issue.getValueAt(i,3);
                    m_product_id = m_productkey.getKey();
                    if (mpc_order_bomlie_id != old_mpc_order_bomline_id) {
                        if ( old_mpc_order_bomline_id != 0 && old_m_product_id!=0 ){
                            MMPCOrderBOMLine bomLINE =  new MMPCOrderBOMLine(Env.getCtx(),old_mpc_order_bomline_id,null);
                            BigDecimal limiteSuperior = bomLINE.getQtyRequiered().multiply(new BigDecimal(1.2));
                            
                            if ( cantidadAgrupada.add(bomLINE.getQtyDelivered()).compareTo(limiteSuperior) > 0 ) {
                                sobrepasaSurtimiento= true;
                                sobrepasan += m_productkey.getName() + " ";                
                            }
                        }
                        old_mpc_order_bomline_id = mpc_order_bomlie_id;
                        m_product_id = old_m_product_id;
                        cantidadAgrupada = Env.ZERO;
                    }
                    BigDecimal movementQty = new BigDecimal(issue.getValueAt(i,8).toString());
                    cantidadAgrupada = cantidadAgrupada.add( movementQty );
               }
           }
           //Recheck (last product)
           if ( cantidadAgrupada.signum() == 1 ){
                MMPCOrderBOMLine bomLINE =  new MMPCOrderBOMLine(Env.getCtx(),old_mpc_order_bomline_id,null);
                BigDecimal limiteSuperior = bomLINE.getQtyRequiered().multiply(new BigDecimal(1.2));

                if ( cantidadAgrupada.add(bomLINE.getQtyDelivered()).compareTo(limiteSuperior) > 0 ) {
                    sobrepasaSurtimiento= true;
                    sobrepasan += m_productkey.getName() + "\n";    
                }
            }
           
           if (sobrepasaSurtimiento) {
               int aux = JOptionPane.showConfirmDialog(null,"Esta surtiendo por encima del 20% los siguientes productos:\n"+sobrepasan+"¿Seguro que desa continuar?", "Surtimiento sobrepasa 20%", JOptionPane.ERROR_MESSAGE);

               if ( aux!=0 ) {
                   return;
               }   
           }
       }
    
        //  Switch Tabs
        TabsReceiptsIssue.setSelectedIndex(1);
        //
        StringBuffer iText = new StringBuffer();
        iText.append("<b>").append(Msg.translate(Env.getCtx(), "IsShipConfirm")).append("</b>");
        iText.append("<br>").append(" ").append("</br>");

        if(m_OnlyReceipt) {
            iText.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">");
            iText.append("<tr>");
                                 /*iText.append("<td>");
                                 iText.append(Msg.translate(Env.getCtx(), "Value"));
                                 iText.append("</td>");*/
            iText.append("<td>");
            iText.append(Msg.translate(Env.getCtx(), "Name"));
            iText.append("</td>");
            iText.append("<td>");
            iText.append(Msg.translate(Env.getCtx(), "C_UOM_ID"));
            iText.append("</td>");
            iText.append("<td>");
            iText.append(Msg.translate(Env.getCtx(), "M_AttributeSetInstance_ID"));
            iText.append("</td>");
            iText.append("<td>");
            iText.append(Msg.translate(Env.getCtx(), "QtyToDeliver"));
            iText.append("</td>");
            iText.append("<td>");
            iText.append(Msg.translate(Env.getCtx(), "QtyDelivered"));
            iText.append("</td>");
            iText.append("<td>");
            iText.append(Msg.translate(Env.getCtx(), "QtyScrap"));
            iText.append("</td>");
            iText.append("</tr>");
            iText.append("<tr>");
                              /*iText.append("<td>");
                                 iText.append(product.getDisplay());
                                 iText.append("</td>");*/
            iText.append("<td>");
            iText.append(product.getDisplay());
            iText.append("</td>");
            iText.append("<td>");
            iText.append(uom.getDisplay());
            iText.append("</td>");
            iText.append("<td>");
            /*
            * GENEOS - Pablo Velazquez
            * 05/08/2013
            * Cambio para que si se va a generar un sublote
            * Muestre el sublote apropiado
            */
           String desc = attribute.getDisplay();        
           if ( autoGenerateLot.isSelected() && m_OnlyReceipt ) {
                if ( m_mpc_order.getPartidas().length >= 1 ) {
                    int m_M_AttributeSetInstance_ID = (Integer)attribute.getValue();
                    MAttributeSetInstance minst= new MAttributeSetInstance(Env.getCtx(),m_M_AttributeSetInstance_ID,null);
                    MLotSubLot subLot = new MLotSubLot(Env.getCtx(), 0, null);
                    MLot lot = new MLot(Env.getCtx(),minst.getM_Lot_ID(), null);
                    subLot.setM_Lot_ID(minst.getM_Lot_ID());
                    minst.setLot(lot.getName()+subLot.getNextSequence());
                    minst.setDescription();
                    desc=minst.getDescription();
                }
           }
            
            
            //iText.append(attribute.getDisplay());
            iText.append(desc);
            iText.append("</td>");
            iText.append("<td>");
            iText.append(toDeliverQty.getDisplay());
            iText.append("</td>");
            iText.append("<td>");
            iText.append(deliveredQty.getDisplay());
            iText.append("</td>");
            iText.append("<td>");
            iText.append(scrapQty.getDisplay());
            iText.append("</td>");
            iText.append("</tr>");
            iText.append("</table>");
        }

        if(m_IsBackflush || ! m_OnlyReceipt ) {
            iText.append("<br>").append(" ").append("</br>");
            iText.append("<br>").append(" ").append("</br>");
            iText.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">");
            iText.append("<tr>");
            iText.append("<td>");
            iText.append(Msg.translate(Env.getCtx(), "Value"));
            iText.append("</td>");
            iText.append("<td>");
            iText.append(Msg.translate(Env.getCtx(), "Name"));
            iText.append("</td>");
            iText.append("<td>");
            iText.append(Msg.translate(Env.getCtx(), "C_UOM_ID"));
            iText.append("</td>");
            iText.append("<td>");
            iText.append(Msg.translate(Env.getCtx(), "M_AttributeSetInstance_ID"));
            iText.append("</td>");
            iText.append("<td>");
            iText.append(Msg.translate(Env.getCtx(), "QtyToDeliver"));
            iText.append("</td>");
            iText.append("<td>");
            iText.append(Msg.translate(Env.getCtx(), "QtyDelivered"));
            iText.append("</td>");
            iText.append("<td>");
            iText.append(Msg.translate(Env.getCtx(), "QtyScrap"));
            iText.append("</td>");
            iText.append("</tr>");

            // cheak available onhand
            for (int i = 0 ; i < issue.getRowCount(); i++) {


                IDColumn id = (IDColumn) issue.getValueAt(i, 0);
                if (id != null && id.isSelected()) {


                    KeyNamePair m_productkey = (KeyNamePair)issue.getValueAt(i,3);
                    int m_M_Product_ID = m_productkey.getKey();
                    KeyNamePair m_uomkey = (KeyNamePair)issue.getValueAt(i,4);


                    if (issue.getValueAt(i,5) == null) {

                        Timestamp m_movementDate = (Timestamp)movementDate.getValue();
                        Timestamp minGuaranteeDate = m_movementDate;
                        Properties ctx = Env.getCtx();
                        MStorage[] storages = null;
                        MProduct product = new MProduct(ctx,m_M_Product_ID,null);
                        if (product != null && product.get_ID() != 0 && product.isStocked()) {
                            MProductCategory pc = MProductCategory.get(ctx, product.getM_Product_Category_ID());
                            String MMPolicy = pc.getMMPolicy();
                            if (MMPolicy == null || MMPolicy.length() == 0) {
                                MClient client = MClient.get(ctx);
                                MMPolicy = client.getMMPolicy();
                            }
                            System.out.println("***** producto:"+product.get_ID() +" instance " +m_mpc_order.getM_AttributeSetInstance_ID());
                            storages = MStorage.getWarehouse(Env.getCtx(), m_mpc_order.getM_Warehouse_ID(), m_M_Product_ID, m_mpc_order.getM_AttributeSetInstance_ID(),product.getM_AttributeSet_ID(), true, minGuaranteeDate,MClient.MMPOLICY_FiFo.equals(MMPolicy) ,null);
                        }
                        //System.out.println("***** producto:"+product.get_ID() +" instance " +M_AttributeSetInstance_ID);
                        BigDecimal todelivery=Env.ZERO;
                        BigDecimal scrap=Env.ZERO;
                        if (issue.getValueAt(i,8)!= null) {
                            todelivery=new BigDecimal(issue.getValueAt(i,8).toString());
                        }
                        if (issue.getValueAt(i,9)!= null) {
                            scrap=new BigDecimal(issue.getValueAt(i,9).toString());
                        }
                        BigDecimal toIssue = todelivery.add(scrap);

                        /*
                        ** Vit4B: Aca se puede controlar que la cantidad a entregar sea positiva en surtimiento y negativa en caso
                        ** de devolucion
                        ** 09/02/2007
                        */

                        if(entrega && toIssue.intValue() < 0)
                        {
                            TabsReceiptsIssue.setSelectedIndex(0);
                            JOptionPane.showMessageDialog(null,"La cantidad a entregar debe ser positiva", "Info" , JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }

                        if(!entrega && toIssue.intValue() > 0)
                        {
                            TabsReceiptsIssue.setSelectedIndex(0);
                            JOptionPane.showMessageDialog(null,"La cantidad a entregar debe ser negativa", "Info" , JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }



                        for (int k = 0; k < storages.length; k++) {
                            MStorage storage = storages[k];
                            //	TODO Selection of ASI

                            if(storage.getQtyOnHand().compareTo(Env.ZERO) == 0)
                                continue;

                            iText.append("<tr>");
                            iText.append("<td>");
                            if (issue.getValueAt(i,2)!=null)
                                iText.append(issue.getValueAt(i,2));
                            else
                                iText.append("");
                            // iText.append(issue.getValueAt(i,2));
                            iText.append("</td>");
                            iText.append("<td>");
                            iText.append(m_productkey);
                            iText.append("</td>");
                            iText.append("<td>");
                            if (m_uomkey!=null)
                                iText.append(m_uomkey);
                            else
                                iText.append("");
                            iText.append("</td>");
                            iText.append("<td>");
                            
                            
                            BigDecimal issueact = toIssue;
                            if (issueact.compareTo(storage.getQtyOnHand()) > 0)
                                issueact = storage.getQtyOnHand();

                            toIssue = toIssue.subtract(issueact);

                            
                            MAttributeSetInstance minst = new MAttributeSetInstance(Env.getCtx(), storage.getM_AttributeSetInstance_ID(), null);
                            String desc=minst.getDescription();
                                                      
                            //seguro=seguro +product.getValue() +"\t- " +desc +"\t - " +issueact.setScale(4,BigDecimal.ROUND_HALF_UP) +"\n";// +" " +issue.getValueAt(i,5).toString();
                            if (desc != null)
                                iText.append(desc);
                            else
                                iText.append("");
                            iText.append("</td>");
                            String vacio ="";
                            if (issue.getValueAt(i,8)!=null && !issue.getValueAt(i,8).toString().equals(vacio)) {
                                iText.append("<td>");
                                // iText.append(issue.getValueAt(i,8).toString());
                                iText.append(issueact);
                                iText.append("</td>");
                            } else {
                                iText.append("<td>");
                                iText.append(" 0.00");
                                iText.append("</td>");
                            }

                            if (issue.getValueAt(i,7)!=null && !issue.getValueAt(i,7).toString().equals(vacio)) {
                                iText.append("<td>");
                                // iText.append(issue.getValueAt(i,8).toString());
                                iText.append(issue.getValueAt(i,7).toString());
                                iText.append("</td>");
                            } else {
                                iText.append("<td>");
                                iText.append(" 0.00");
                                iText.append("</td>");
                            }

                            if (issue.getValueAt(i,9)!=null && !issue.getValueAt(i,9).toString().equals(vacio)) {
                                iText.append("<td>");
                                iText.append(issue.getValueAt(i,9).toString());
                                iText.append("</td>");
                            } else {
                                iText.append("<td>");
                                iText.append(" 0.00");
                                iText.append("</td>");
                            }
                            iText.append("</tr>");

                            if (toIssue.compareTo(Env.ZERO) <= 0)
                                break;
                        }

                    } else {
                        
                        BigDecimal toIssue = BigDecimal.ZERO;
                        if (issue.getValueAt(i,8)!= null) {
                            toIssue=new BigDecimal(issue.getValueAt(i,8).toString());
                        }
                        
                        if(!entrega && toIssue.intValue() > 0)
                        {
                            TabsReceiptsIssue.setSelectedIndex(0);
                            JOptionPane.showMessageDialog(null,"La cantidad a entregar debe ser negativa", "Info" , JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        
                        
                        iText.append("<tr>");
                        iText.append("<td>");
                        if (issue.getValueAt(i,2)!=null)
                            iText.append(issue.getValueAt(i,2));
                        else
                            iText.append("");
                        iText.append("</td>");
                        iText.append("<td>");
                        iText.append(m_productkey);
                        iText.append("</td>");
                        iText.append("<td>");
                        if (m_uomkey!=null)
                            iText.append(m_uomkey);
                        else
                            iText.append("");
                        // iText.append(m_uomkey);
                        iText.append("</td>");
                        iText.append("<td>");
                        if (issue.getValueAt(i,5)!=null)
                            iText.append(issue.getValueAt(i,5));
                        else
                            iText.append("");
                        iText.append("</td>");
                        String vacio ="";
                        if (issue.getValueAt(i,8)!=null && !issue.getValueAt(i,8).toString().equals(vacio)) {
                            iText.append("<td>");
                            iText.append(issue.getValueAt(i,8).toString());
                            iText.append("</td>");
                        } else {
                            iText.append("<td>");
                            iText.append(" 0.00");
                            iText.append("</td>");
                        }
                        if (issue.getValueAt(i,7)!=null && !issue.getValueAt(i,7).toString().equals(vacio)) {
                            iText.append("<td>");
                            // iText.append(issue.getValueAt(i,8).toString());
                            iText.append(issue.getValueAt(i,7).toString());
                            iText.append("</td>");
                        } else {
                            iText.append("<td>");
                            iText.append(" 0.00");
                            iText.append("</td>");
                        }
                        if (issue.getValueAt(i,9)!=null && !issue.getValueAt(i,9).toString().equals(vacio)) {
                            iText.append("<td>");
                            iText.append(issue.getValueAt(i,9).toString());
                            iText.append("</td>");
                        } else {
                            iText.append("<td>");
                            iText.append(" 0.00");
                            iText.append("</td>");
                        }
                        iText.append("</tr>");
                    }

                }

            }
            iText.append("</table>");
        }
        info.setText(iText.toString());

        if (ADialog.ask(m_WindowNo, this, "Update")) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            /** BISion - 14/11/2008 - Santiago Iba�ez
             * Se crea una transaccion global para efectivizar los cambios hechos
             * en la base de datos unicamente cuando termina el proceso.
             */
            
            /*
             * GENEOS - Pablo Velazquez
             * 23/07/2013
             * Se agrega nombre unico para la transaccion
             */
            java.util.Date date=new java.util.Date();
            Trx trx = Trx.get(Trx.createTrxName("ReceiptIssue-"+new Timestamp(date.getTime()).toString()), true);
            try{
                if(cmd_process(trx.getTrxName())){
                    trx.commit();
                    trx.close();
                    dispose();
                }
                else {
                    trx.rollback();
                    trx.close();
                    TabsReceiptsIssue.setSelectedIndex(0);
                    setCursor(Cursor.getDefaultCursor());
                }
            }
            catch(Exception e){
                e.printStackTrace();
                trx.rollback();
                trx.close();
                log.log(Level.SEVERE, "Error en VOrderReciptIssue", e);
                TabsReceiptsIssue.setSelectedIndex(0);
                setCursor(Cursor.getDefaultCursor());
            }
        }	//	OK to print invoices
        else {
            TabsReceiptsIssue.setSelectedIndex(0);
        }
    }   //  generateInvoices_complete

    private int getHeaderID() {
        return Cost_Collector_HEADER_ID;
    }

    // Variables declaration - do not modify
    private CPanel mainPanel = new CPanel();
    private CPanel Generate = new CPanel();
    private CPanel PanelBottom = new CPanel();
    private CPanel PanelCenter = new CPanel();
    private CPanel northPanel = new CPanel();
    private CButton Process = new CButton();
    private CPanel ReceiptIssueOrder = new CPanel();
    private javax.swing.JTabbedPane TabsReceiptsIssue = new JTabbedPane();
    private VPAttribute attribute = null;

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
    private CLabel labelcombo = new CLabel(Msg.translate(Env.getCtx(), "DeliveryRule"));
    private VComboBox pickcombo = new VComboBox();
    private CLabel QtyBatchsLabel =  new CLabel();
    private VNumber qtyBatchs = new VNumber("QtyBatchs", false, false, false, DisplayType.Quantity, "QtyBatchs");
    private CLabel QtyBatchSizeLabel =  new CLabel();
    private VNumber qtyBatchSize = new VNumber("QtyBatchSize", false, false, false, DisplayType.Quantity, "QtyBatchSize");
    private CCheckBox autoGenerateLot = new CCheckBox(Msg.translate(Env.getCtx(), "YesNo"), false);

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

    private CLabel labelAutoGenerateLot = new CLabel("Generar SubLotes");

    private VDate fechaVencimiento = new VDate("GuaranteeDate", true, false, true, DisplayType.Date, "GuaranteeDate");
    
    /**
     * BISion - 19/10/2009 - Santiago Ibañez
     * COM-PAN-REQ-02.002.01
     */
    private CLabel labelBundle = new CLabel("Descripcion de bultos");
 
    private CTextField fieldBundle = new CTextField(20);


    /** BISion - 19/10/2009 - Santiago Ibañez
     * COM-PAN-REQ-08.001.01
     * Metodo que retorna si el campo fecha de Vencimiento debe ser de solo lectura o no.
     * @return
     */
    private boolean esSoloLecturaVencimiento(){
        //Obtengo el producto recibido
        MProduct producto = new MProduct(Env.getCtx(), (Integer) product.getValue(), null);
        if (producto.getLowLevel()==1||producto.getLowLevel()==2)
            return false;
        else
            return true;

    }

    private Timestamp getFechaVencimientoLevel0(){
        //Obtener cost collector de la OM
        MMPCOrder orden = new MMPCOrder(Env.getCtx(), (Integer) order.getValue(), null);
        //Los surtimientos los saco de los cost collector
        MMPCCostCollector[] cc = orden.getMMPCCostCollector((Integer) order.getValue());
        //fecha de hoy para que sea mayor
        Timestamp fecha = new Timestamp(System.currentTimeMillis()*100);
        boolean cambioFecha = false;
        for (int i=0;i<cc.length;i++){
            //descarto todos los collector de tipo diferente P-
            if (cc[i].getMovementType()!=null && !cc[i].getMovementType().equals(MMPCCostCollector.MOVEMENTTYPE_Production_))
                continue;
            //descarto todas las devoluciones
            if (cc[i].getMovementQty().signum()==-1)
                continue;
            //Obtengo el producto suministrado
            MProduct p = new MProduct(Env.getCtx(), cc[i].getM_Product_ID(), null);
            if (p.getLowLevel()==1&&p.isBOM()||p.getLowLevel()==0&&p.isBOM()){
                //Obtengo la partida asociada
                MAttributeSetInstance asi = new MAttributeSetInstance(Env.getCtx(), cc[i].getM_AttributeSetInstance_ID(), null);
                //Obtengo la menor fecha
                if (asi.getGuaranteeDate()!=null&&fecha.compareTo(asi.getGuaranteeDate())>=0){
                    fecha = asi.getGuaranteeDate();
                    cambioFecha = true;
                }
            }
        }
        if (cambioFecha)
            return fecha;
        else
            return null;
        
    }
    private void mostrarVencimiento(){
        
        //fechaVencimiento.set
        if (product==null||product.getValue()==null)
            return;
        /**
         * BISion - 19/10/2009 - Santiago Ibañez
         * COM-PAN-REQ-08.001.01
         */
        boolean readOnly = esSoloLecturaVencimiento();
        //Obtengo el producto recibido
        MProduct producto = new MProduct(Env.getCtx(), (Integer) product.getValue(), null);
        Timestamp fv = getVencimientoOrden((Integer) order.getValue());
        if (fv==null){
            if (producto.getLowLevel()==0){
                fv = getFechaVencimientoLevel0();

                if (fv!=null)
                    fechaVencimiento.setValue(fv);
                else
                    fechaVencimiento.setValue(null);
            }
            else
                fechaVencimiento.setValue(null);
            fechaVencimiento.setReadWrite(!readOnly);
            fechaVencimiento.setMandatory(!readOnly);
        }
        //Aca va a entrar si ya recibio una parte de terminado
        else{
            fechaVencimiento.setValue(fv);
            fechaVencimiento.setReadWrite(false);
            fechaVencimiento.setMandatory(false);
        }
        
        labelVencimiento.setVisible(true);
        fechaVencimiento.setVisible(true);
    }

    /** BISion - 02/12/2009 - Santiago Ibañez
     * Método que retorna una fecha de vencimiento (si es que existe)
     * para una Orden.
     * @param MPC_Order_ID
     * @return
     */
    private Timestamp getVencimientoOrden(int MPC_Order_ID){
        MMPCOrder o = new MMPCOrder(Env.getCtx(), MPC_Order_ID, null);
        //Obtengo todos los Cost Collector
        MMPCCostCollector[] cc = o.getMMPCCostCollector(MPC_Order_ID);
        MAttributeSetInstance asi = null;
        for (int i=0;i<cc.length;i++){
            //Obtengo Recepciones de terminado
            if (cc[i].getMovementType()!=null && cc[i].getMovementType().equals(MMPCCostCollector.MOVEMENTTYPE_ProductionPlus)){
                //Obtengo la partida del terminado
                asi = new MAttributeSetInstance(Env.getCtx(), o.getM_AttributeSetInstance_ID(), null);
                return asi.getGuaranteeDate();
            }
        }
        if ( asi == null && attribute.getValue() != null){
            asi = new MAttributeSetInstance(Env.getCtx(), (Integer)attribute.getValue(), null);
            return asi.getGuaranteeDate();
        }
        return null;
    }

    /** BISion - 13/11/2009 - Santiago Ibañez
     * Metodo que retorna el id del printformat para surtimientos y devoluciones
     * @return
     */
    private int getPrintFormat(){
        String sql = "select ad_printformat_id from ad_printformat where lower(name) = 'recepcion de terminado/surtimiento materiales'";
        int id = 0;
        PreparedStatement ps = DB.prepareStatement(sql, null);
        try{
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                id = rs.getInt(1);
        }
        catch(SQLException ex){
        }
        return id;
    }

    /** BISion - 13/11/2009 - Santiago Ibañez
     * Metodo que retorna el id de la vista RV_SURTIMIENTO_RECEP_TERMINADO
     * @return
     */
    private int getTable(){
        String sql = "select ad_table_id from ad_table where upper(tablename) = 'RV_SURTIMIENTO_RECEP_TERMINADO'";
        int id = 0;
        PreparedStatement ps = DB.prepareStatement(sql, null);
        try{
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                id = rs.getInt(1);
        }
        catch(SQLException ex){
        }
        return id;
    }
    /**
     * BISion - 16/11/2009 - Santiago Ibañez
     * Metodo que retorna el id de una ubicacion de deposito, dado su nombre
     * @param name
     * @return
     */
    private int getLocation(String name){
        int id = 0;
        try {
            String sql = "select m_locator_id from m_locator where value like '" + name + "%'";
            PreparedStatement ps = DB.prepareStatement(sql, null);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(CalloutMovementLineLocations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /** BISion - 03/12/2009 - Santiago Ibañez
     * Metodo que retorna si la orden tiene al menos un surtimiento
     * de cada producto que integra su formula
     * @return
     */
    private boolean tieneSurtimientosMateriales(){
        boolean tiene = true;
        //Obtengo cada uno de los productos que integran la formula del producto
        //del cual se quere recibir terminado.
        MMPCOrderBOMLine[] lines = MMPCOrder.getLines((Integer) order.getValue(), null);
        for (int i=0;i<lines.length&&tiene;i++){
            /** BISion - 25/03/2010 - Santiago Ibañez
             * COM-PAN-REQ-08.005.02
             */
            MProduct p = MProduct.get(Env.getCtx(), lines[i].getM_Product_ID()) ;
            boolean categNOSumin = esCategoriaNOSuministrable(p.getM_Product_Category_ID());
             //fin modificacion BISion
            if (!categNOSumin && !tieneSurtimientos(lines[i].getM_Product_ID()))
                tiene = false;
        }
        return tiene;
    }

    private boolean esCategoriaNOSuministrable(int M_Product_Category_ID){
        boolean ret = false;
        try {
            //La Tabla T_CATEGORIAS_NO_SUMINISTRABLES guarda todas aquellas categorias que no requieran
            //algun surtimiento para recibir terminado. Es en el contexto de COM-PAN-REQ-08.005.02
            String sql = "select * from T_CATEGORIAS_NO_SUMINISTRABLES where m_product_category_id = ?";
            PreparedStatement ps = DB.prepareStatement(sql, null);
            ps.setInt(1, M_Product_Category_ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ret = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(VOrderReceiptIssue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    /** BISion - 03/12/2009 - Santiago Ibañez
     * Metodo que retorna si la orden tiene al menos un surtimiento del producto dado
     * @param M_Product_ID
     * @return
     */
    private boolean tieneSurtimientos(int M_Product_ID){
        boolean tiene = false;
        BigDecimal qtySurtida = BigDecimal.ZERO;
        //Obtengo la orden en cuestion
        MMPCOrder o = new MMPCOrder(Env.getCtx(),(Integer) order.getValue(),null);
        //Obtengo todos los surtimientos de la orden
        MMPCCostCollector[] cc = o.getMMPCCostCollector((Integer) order.getValue());
        for (int i=0;i<cc.length;i++){
            //Busco que tienga un surtimiento del producto dado...
            if (cc[i].getMovementType().equals(MMPCCostCollector.MOVEMENTTYPE_Production_)&&cc[i].getM_Product_ID()==M_Product_ID){
                tiene = true;
                qtySurtida = qtySurtida.add(cc[i].getMovementQty());
            }
        }
        if (qtySurtida.compareTo(BigDecimal.ZERO)>0)
            tiene = true;
        else
            tiene = false;
        return tiene;
    }

    protected void enviarMensajeRecepcionTerminado(int M_Product_ID, int instanceId, BigDecimal qty, int M_LocatorTo_ID) throws SQLException{

            //Esta lista es porque un usuario puede tener N roles y evitar que reciba N avisos identicos
            ArrayList lista_usuario = new ArrayList();

            //  Cargo los usuarios de acuerdo a los roles
                //Gestion de control de procesos.
                ArrayList usuarios = getUsuarios(1000062);
                //Gestion de planeamiento
                usuarios.addAll(getUsuarios(1000063));
                //Gestion de Inventarios
                usuarios.addAll(getUsuarios(1000068));
                //Gestion de Manufactura
                usuarios.addAll(getUsuarios(1000069));
                //Gestion de abastecimientos
                usuarios.addAll(getUsuarios(1000060));
                //Gestion de Costos
                usuarios.addAll(getUsuarios(1000079));
                //Pago a proveedores
                usuarios.addAll(getUsuarios(1000075));
                //Supervision de la gestion
                usuarios.addAll(getUsuarios(5000006));
                //Supervision de la Gestion de Planeamiento
                usuarios.addAll(getUsuarios(5000005));
                //Supervision de la Gestion de abastecimientos
                usuarios.addAll(getUsuarios(1000055));
                //Supervision de la Gestion de Inventarios
                usuarios.addAll(getUsuarios(1000065));
                //Supervision de la Gestion de Manufactura
                usuarios.addAll(getUsuarios(1000070));
                //Supervision de la Gestion de Control de Procesos
                usuarios.addAll(getUsuarios(1000073));

            
            Integer[] users = new Integer[usuarios.size()];
            usuarios.toArray(users);
            for (int i=0;i<users.length;i++){
                if (!lista_usuario.contains(users[i])){
                    lista_usuario.add(new Integer(users[i]));
                    MProduct p = new MProduct(Env.getCtx(),M_Product_ID,null);
                    MNote note = new MNote(Env.getCtx(), "Recepcion de Terminado" , users[i], null);
                    MAttributeSetInstance asi = new MAttributeSetInstance(Env.getCtx(), instanceId, null);
                    MLocator locTo = new MLocator(Env.getCtx(), M_LocatorTo_ID, null);
                    note.setTextMsg("Se solicita la aprobacion del producto "+p.getValue()+" "+p.getName()+" su partida "+asi.getDescription()+" con una cantidad de "+qty+" en el deposito "+locTo.getValue());
                    /*
                     *  03/06/2013 Maria Jesus
                     *  Se agrega para que guarde en el log en caso de que no pueda guardar la nota.
                     */
                    if (!note.save()){
                        log.log(Level.SEVERE, "No se pudo guardar la Nota: "+ "Se solicita la aprobación del producto "+p.getValue()+" "+p.getName()+" su partida "+asi.getDescription()+" con una cantidad de "+qty+" en el deposito "+locTo.getValue());
                    }
                }
            }

        }

        /**
         * BISion - 03/05/2010 - Santiago Ibañez
         * Método que retorna un mensaje dada una categoria de producto
         * @param M_ProductCategory_ID
         * @return Search key del mensaje
         */
        private String getMensajeByCategoria(int M_ProductCategory_ID){
            MProductCategory pc = new MProductCategory(Env.getCtx(), M_ProductCategory_ID, null);
            if (pc.getName().equals("Excipientes")||
                pc.getName().equals("FOLIA")||
                pc.getName().equals("Material de Llenado")||
                pc.getName().equals("Temporal")||
                pc.getName().equals("Material de Acondicionamiento"))
                return "Aprobacion de Insumos";
            else if (pc.getName().equals("Granel")||pc.getName().equals("Producto Envasado"))
                return "Aprobacion de Graneles y Semielaborados";
            if (pc.getName().equals("Original")||pc.getName().equals("Muestra"))
                return "Aprobacion de Producto Terminado";
            //
            else
                return "";
        }

        /** BISion - 07/04/2010 - Santiago Ibañez
         * Metodo que retorna el id de un rol determinado
         * @param name
         * @return
         * @throws java.sql.SQLException
         */
        private int getAD_Role_IDByName(String name) throws SQLException{
            String sql = "select ad_role_id from ad_role where name = ?";
            PreparedStatement ps = DB.prepareStatement(sql, null);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            int id = -1;
            if (rs.next()){
                id = rs.getInt(1);
            }
            rs.close();
            ps.close();
            return id;
        }

        /** BISion - 07/04/2010 - Santiago Ibañez
         * Obtengo todos los usuarios de acuerdo al rol
         * @param AD_Role_ID
         * @return
         * @throws java.sql.SQLException
         */
        private ArrayList getUsuarios(int AD_Role_ID) throws SQLException{
            String sql = "select ad_user_id from ad_user_roles where ad_role_id = ?";
            PreparedStatement ps = DB.prepareStatement(sql, null);
            ArrayList users = new ArrayList();
            ps.setInt(1, AD_Role_ID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                users.add(rs.getInt(1));
            }
            return users;
            //Integer[] usuarios = new Integer[users.size()];
            //return (Integer[]) users.toArray(usuarios);
        }
        
        public boolean unreserveFeFo(int MPC_Order_ID,int m_M_Product_ID,int MPC_Order_BOMLine_ID,BigDecimal cantidadAgrupada, String trxName){
                /*
                 * Desreservo de los que vencen mas adelante primero, dejando comprometidas las que vencen primero
                 * a no ser que se cancelen todos los reservados.
                 */
                String sql = "SELECT * FROM MPC_ORDER_QTYRESERVED"
                        + " WHERE MPC_ORDER_ID=?"
                        + " AND MPC_ORDER_BOMLINE_ID=?"
                        + " AND M_PRODUCT_ID=? "
                        + " AND REMAININGQTY > 0"
                        + " ORDER BY USEORDER DESC";
                PreparedStatement ps = DB.prepareStatement(sql,trxName);
                try {
                    ps.setInt(1, MPC_Order_ID);
                    ps.setInt(2, MPC_Order_BOMLine_ID);
                    ps.setInt(3, m_M_Product_ID);
                    ResultSet rs = ps.executeQuery();
                    while ( rs.next() && (cantidadAgrupada.compareTo(Env.ZERO) != 0) ){
                        MMPCOrderQtyReserved qtyRes = new MMPCOrderQtyReserved(Env.getCtx(),rs,trxName);
                        BigDecimal storageReservedBefore = qtyRes.getRemainingQty();
                        BigDecimal toUnreserve = BigDecimal.ZERO;
                        //Si lo que me queda supera a lo que tengo que desreservar
                        if (storageReservedBefore.compareTo(cantidadAgrupada) >= 0){
                            toUnreserve = cantidadAgrupada;
                        }
                        //Si lo que me queda no cubre lo que tengo que desreservar
                        else {
                            toUnreserve = storageReservedBefore;
                        }
                        
                        //Desacumulo lo desreservado
                        cantidadAgrupada = cantidadAgrupada.subtract(toUnreserve);
                        
                        //Actualizo control de reservados
                        qtyRes.setRemainingQty(storageReservedBefore.subtract(toUnreserve));                      
                        if ( !qtyRes.save() ){
                            log.severe("Error al actualizar control de reservados: "+qtyRes);
                            return false;
                        }
                        //Actualizo MStorage
                        if (!MStorage.addDist(Env.getCtx(), 0,
                            qtyRes.getM_Locator_ID(),
                            qtyRes.getM_Product_ID(),
                            qtyRes.getM_AttributeSetInstance_ID(), qtyRes.getM_AttributeSetInstance_ID(),
                            BigDecimal.ZERO, toUnreserve.negate(), BigDecimal.ZERO, trxName) ) {
                            log.severe("Error al actualizar mstorage para locator: "+qtyRes.getM_Locator_ID()+
                                        " product_id: "+qtyRes.getM_Product_ID()+" m_attributesetintance_id: "+
                                        qtyRes.getM_AttributeSetInstance_ID());
                            return false;
                        }
                    }
                    //Solo si se hace esto para surtimiento!
                    if (cantidadAgrupada.compareTo(Env.ZERO) < 0){
                        //Error
                        return false;
                    }
                }
                catch( Exception e){ 
                    return false;
                }            
            return true;
        }
        
        private void fillCombo(){
            
            pickcombo.removeActionListener(this);
            pickcombo.removeAllItems();
            
            boolean delivered = false;               
            boolean backflush = false;
            
            if (order.getValue() != null) {
                
                //instantiate order (No TRX Needed)
                MMPCOrder auxOrder = new MMPCOrder(Env.getCtx(),(Integer)order.getValue(),null);
                if ( auxOrder.hasDelivered() )
                    delivered = true;
                if ( auxOrder.hasReturns() )
                    backflush = true;

            }
            
            pickcombo.addItem(new KeyNamePair(0, ""));
            //fjviejo e-evolution user cant aprove documents
            int AD_User_ID = Env.getAD_User_ID(Env.getCtx());
            int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
            MRole rol = new MRole(Env.getCtx(),AD_Role_ID,null);

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
                    
                    //Recepcion solo si no existe devolucion
                    //if (!backflush)
                        pickcombo.addItem(new KeyNamePair(1, Msg.translate(Env.getCtx(), "OnlyReceipt")));
                }
            }

            if (OI) {
                if (rol.getProcessAccess(1000113).booleanValue()) {
                    
                    //Surtimiento solo si no hubo devolucion
                    //if ( !backflush )
                        pickcombo.addItem(new KeyNamePair(2, Msg.translate(Env.getCtx(), "OnlyIssue")));
                }
            }

            if (IB) {
                if (rol.getProcessAccess(1000114).booleanValue()) 
                   //Para hacer devolucion debe existir recepcion
                   if (delivered)
                        pickcombo.addItem(new KeyNamePair(3, Msg.translate(Env.getCtx(), "IsBackflush")));
            }

            if (RET) 
                
                if (rol.getProcessAccess(1000126).booleanValue()) {
                    
                    //Para hacer devolucion debe existir recepcion
                    pickcombo.addItem(new KeyNamePair(4, Msg.translate(Env.getCtx(), "Return")));
                }

            if (!OR && !OI && !IB) {
                
                //Recepcion solo si no existe devolucion
                //if (!backflush)
                    pickcombo.addItem(new KeyNamePair(1, Msg.translate(Env.getCtx(), "OnlyReceipt")));
                
                //Surtimiento solo si no hubo devolucion
                //if ( !backflush)
                    pickcombo.addItem(new KeyNamePair(2, Msg.translate(Env.getCtx(), "OnlyIssue")));
                
                if (delivered)
                    pickcombo.addItem(new KeyNamePair(3, Msg.translate(Env.getCtx(), "IsBackflush")));
                
                //Para hacer devolucion debe existir recepcion
                pickcombo.addItem(new KeyNamePair(4, Msg.translate(Env.getCtx(), "Return")));
            }
            pickcombo.addActionListener(this);
        }

}