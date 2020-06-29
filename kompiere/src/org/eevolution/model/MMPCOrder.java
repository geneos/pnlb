/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/
//package org.compiere.mfg.model;
package org.eevolution.model;

import org.compiere.model.MZYNCATEGORYTOLERANCE;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.util.logging.*;
import java.io.*;

import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.wf.*;
import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.util.*;
// fjviejo e-evolution do not complete if there isnt qtyonhand begin
import javax.swing.*;
import javax.swing.tree.*;
import org.compiere.apps.ADialog;
// fjviejo e-evolution   end

/**
 *  Order Model.
 * 	Please do not set DocStatus and C_DocType_ID directly.
 * 	They are set in the process() method.
 * 	Use DocAction and C_DocTypeTarget_ID instead.
 *
 *  @author Jorg Janke
 *  @version $Id: MMPCOrder.java,v 1.3 2006/09/04 20:31:55 SIGArg-01 Exp $
 */
public class MMPCOrder extends X_MPC_Order implements DocAction {

    /**
     * 	Create new Order by copying
     * 	@param ctx context
     *	@param C_Order_ID invoice
     * 	@param dateDoc date of the document date
     * 	@param counter create counter links
     *	@return Order
     */
    public static MMPCOrder copyFrom(MMPCOrder from, Timestamp dateDoc,
            int C_DocTypeTarget_ID, boolean isSOTrx, boolean counter) {
        MMPCOrder to = new MMPCOrder(from.getCtx(), 0, "MPC_Order");
        PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
        to.setMPC_Order_ID(0);
        to.set_ValueNoCheck("DocumentNo", null);
        //
        to.setDocStatus(DOCSTATUS_Drafted);		//	Draft
        to.setDocAction(DOCACTION_Prepare);
        //
        //to.setC_DocType_ID(this.C_DOCTYPE_ID_ManufacturingOrder);
        //to.setC_DocTypeTarget_ID(C_DocTypeTarget_ID);
        to.setIsSOTrx(isSOTrx);
        //
        to.setIsSelected(false);
        /*to.setDateOrdered (dateDoc);
        to.setDateAcct (dateDoc);
        to.setDatePromised (dateDoc);
        to.setDatePrinted(null);
        to.setIsPrinted (false);
        //*/
        to.setIsApproved(false);
        /*to.setIsCreditApproved(false);
        to.setC_Payment_ID(0);
        to.setC_CashLine_ID(0);
        //	Amounts are updated  when adding lines
        to.setGrandTotal(Env.ZERO);
        to.setTotalLines(Env.ZERO);
        //
        to.setIsDelivered(false);
        to.setIsInvoiced(false);
        to.setIsSelfService(false);
        to.setIsTransferred (false);*/
        to.setPosted(false);
        to.setProcessed(false);
        /*if (counter)
        to.setRef_Order_ID(from.getC_Order_ID());
        else
        to.setRef_Order_ID(0);
        //
         */
        if (!to.save()) {
            throw new IllegalStateException("Could not create Order");
        }
        /*if (counter)
        from.setRef_Order_ID(to.getC_Order_ID());
        
        if (to.copyLinesFrom(from, counter) == 0)
        throw new IllegalStateException("Could not create Order Lines");
         */
        return to;
    }	//	copyFrom

    /**************************************************************************
     *  Default Constructor
     *  @param ctx context
     *  @param  C_Order_ID    order to load, (0 create new order)
     */
    public MMPCOrder(Properties ctx, int MPC_Order_ID, String trxName) {
        super(ctx, MPC_Order_ID, trxName);
        //  New
        if (MPC_Order_ID == 0) {
            setDocStatus(DOCSTATUS_Drafted);
            setDocAction(DOCACTION_Prepare);
            setC_DocType_ID(0);
            set_ValueNoCheck("DocumentNo", null);
            setIsSelected(false);
            setIsSOTrx(false);
            setIsApproved(false);
            setIsPrinted(false);
            setProcessed(false);
            setProcessing(false);
            setC_DocType_ID(0);
            setPosted(false);
        }
    }	//	MPC_Order

    /**************************************************************************
     *  Project Constructor
     *  @param  project Project to create Order from
     * 	@param	DocSubTypeSO if SO DocType Target (default DocSubTypeSO_OnCredit)
     */
    public MMPCOrder(MProject project, boolean IsSOTrx, String DocSubTypeSO) {
        this(project.getCtx(), 0, "MPC_Order");
        setAD_Client_ID(project.getAD_Client_ID());
        setAD_Org_ID(project.getAD_Org_ID());
        setC_Campaign_ID(project.getC_Campaign_ID());
        //setC_DocTypeTarget_ID(1000005);
        //setSalesRep_ID(project.getSalesRep_ID());
        //
        setC_Project_ID(project.getC_Project_ID());
        setDescription(project.getName());
        Timestamp ts = project.getDateContract();
        if (ts != null) {
            setDateOrdered(ts);
        }
        ts = project.getDateFinish();
        if (ts != null) {
            setDatePromised(ts);
        }
        //
        //setC_BPartner_ID(project.getC_BPartner_ID());
        //setC_BPartner_Location_ID(project.getC_BPartner_Location_ID());
        //setAD_User_ID(project.getAD_User_ID());
        //
        setM_Warehouse_ID(project.getM_Warehouse_ID());
        /*setM_PriceList_ID(project.getM_PriceList_ID());
        setC_PaymentTerm_ID(project.getC_PaymentTerm_ID());
        //*/
        setIsSOTrx(IsSOTrx);
        /*if (IsSOTrx) {
        if (DocSubTypeSO == null || DocSubTypeSO.length() == 0)
        setC_DocTypeTarget_ID(DocSubTypeSO_OnCredit);
        else
        setC_DocTypeTarget_ID(DocSubTypeSO);
        }
        else]*/
        //setC_DocTypeTarget_ID();
    }	//	MOrder

    /**
     *  Load Constructor
     *  @param ctx context
     *  @param rs result set record
     */
    public MMPCOrder(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }	//	MOrder
    /**************************************************************************
     *  Default Constructor
     *  @param ctx context
     *  @param  C_Order_ID    order to load, (0 create new order)
     */
    /*
    public MMPCOrder(Properties ctx, org.compiere.model.MOrderLine line ,int S_Resource_ID, int MPC_Product_BOM_ID, int AD_Workflow_ID , String trxName)
    
    {
    super(ctx, 0, trxName);
    setLine(line.getLine());
    
    //MProduct product = MProduct.get(getCtx(),line.getM_Product_ID());
    //int S_Resource_ID = DB.getSQLValue(trxName,"SELECT S_Resource_ID FROM S_Resource r WHERE r.ManufacturingResourceType = 'PL' AND s.IsManufacturingResource = 'Y' AND r.AD_Client_ID = ? AND r.M_Warehouse_ID = ? LIMIT 1", getAD_Client_ID(),line.getM_Warehouse_ID());
    //int MPC_Product_BOM_ID = DB.getSQLValue(trxName,"SELECT MPC_Product_BOM_ID FROM MPC_Product_BOM bom WHERE bom.AD_Client_ID = ?  AND bom.Value = ? ", getAD_Client_ID(),product.getValue());
    //int AD_Workflow_ID = DB.getSQLValue(trxName,"SELECT AD_Workflow_ID FROM AD_Workflow wf WHERE wf.AD_Client_ID = ?  AND bom.Value = ? ", getAD_Client_ID(),product.getValue());
    int SupplyPlanner_ID = 0;
    setS_Resource_ID(S_Resource_ID);
    setM_Warehouse_ID(line.getM_Warehouse_ID());
    setM_Product_ID(line.getM_Product_ID());
    setM_AttributeSetInstance_ID(line.getM_AttributeSetInstance_ID());
    setMPC_Product_BOM_ID(MPC_Product_BOM_ID);
    setAD_Workflow_ID(AD_Workflow_ID);
    setPlanner_ID(SupplyPlanner_ID);
    setQtyDelivered(Env.ZERO);
    setQtyReject(Env.ZERO);
    setQtyScrap(Env.ZERO);
    setDateOrdered(line.getDateOrdered());
    setDatePromised(line.getDatePromised());
    setDateStartSchedule(TimeUtil.addDays(line.getDatePromised(), (MMPCMRP.getDays(S_Resource_ID,AD_Workflow_ID, line.getQtyOrdered())).negate().intValue()));
    setDateFinishSchedule(line.getDatePromised());
    setQtyEntered(line.getQtyEntered());
    setQtyOrdered(line.getQtyOrdered());
    setC_UOM_ID(line.getC_UOM_ID());
    setPosted(false);
    setProcessed(false);
    //setC_DocTypeTarget_ID(C_DocType_ID);
    //setC_DocType_ID(C_DocType_ID);
    setPriorityRule(this.PRIORITYRULE_High);
    setDocStatus(DOCSTATUS_Drafted);
    setDocAction(DOCSTATUS_Completed);
    }*/
    /**	Order Lines					*/
    private MMPCOrderBOMLine[] m_order_bomlines = null;

    /**
     * 	Overwrite Client/Org if required
     * 	@param AD_Client_ID client
     * 	@param AD_Org_ID org
     */
    public void setClientOrg(int AD_Client_ID, int AD_Org_ID) {
        super.setClientOrg(AD_Client_ID, AD_Org_ID);
    }	//	setClientOrg

    /**
     * 	Set C_Resource
     *	@param C_Resource Plant
     */
    /*
    public void setC_Resource_ID (int C_Resource_ID)
    {
    super.setC_Resource_ID (C_Resource_ID);
    }	//	C_Resource Plant
     */
    /**
     * 	Set Warehouse
     *	@param M_Warehouse_ID warehouse
     */
    public void setM_Warehouse_ID(int M_Warehouse_ID) {
        super.setM_Warehouse_ID(M_Warehouse_ID);
    }	//	setM_Warehouse_ID

    /*************************************************************************/
    /*
    public static final String		DocSubTypeSO_Standard = "SO";
    public static final String		DocSubTypeSO_Quotation = "OB";
    public static final String		DocSubTypeSO_Proposal = "ON";
    public static final String		DocSubTypeSO_Prepay = "PR";
    public static final String		DocSubTypeSO_POS = "WR";
    public static final String		DocSubTypeSO_Warehouse = "WP";
    public static final String		DocSubTypeSO_OnCredit = "WI";
    public static final String		DocSubTypeSO_RMA = "RM";
     */
    /**
     * 	Set Target Sales Document Type
     * 	@param DocSubTypeSO_x SO sub type - see DocSubTypeSO_*
     */
    /*
    public void setC_DocTypeTarget_ID(String DocSubTypeSO_x) {
    String sql = "SELECT C_DocType_ID FROM C_DocType WHERE AD_Client_ID=? AND DocSubTypeSO=? ORDER BY IsDefault DESC";
    int C_DocType_ID = DB.getSQLValue(sql, getAD_Client_ID(), DocSubTypeSO_x);
    if (C_DocType_ID <= 0)
    log.log(Level.SEVERE ,("setC_DocTypeTarget_ID - Not found for AD_Client_ID=" + getAD_Client_ID() + ", SubType=" + DocSubTypeSO_x);
    else {
    log.fine("setC_DocTypeTarget_ID - " + DocSubTypeSO_x);
    setC_DocTypeTarget_ID(C_DocType_ID);
    setIsSOTrx(true);
    }
    }	//	setC_DocTypeTarget_ID
     */
    /**
     * 	Set Target Document Type.
     * 	Standard Order or PO
     */
    /*
    public void setC_DocTypeTarget_ID() {
    if (isSOTrx())		//	SO = Std Order
    {
    //setC_DocTypeTarget_ID(DocSubTypeSO_Standard);
    return;
    }
    //	PO
    String sql = "SELECT C_DocType_ID FROM C_DocType WHERE AD_Client_ID=? AND DocBaseType='POO' ORDER BY IsDefault DESC";
    int C_DocType_ID = DB.getSQLValue(sql, getAD_Client_ID());
    if (C_DocType_ID <= 0)
    log.log(Level.SEVERE ,("setC_DocTypeTarget_ID - No POO found for AD_Client_ID=" + getAD_Client_ID());
    else {
    log.fine("setC_DocTypeTarget_ID (PO) - " + C_DocType_ID);
    setC_DocTypeTarget_ID(C_DocType_ID);
    }
    }	//	setC_DocTypeTarget_ID
     */
    /**
     * 	Copy Lines From other Order
     *	@param order order
     *	@param counter set counter info
     *	@return number of lines copied
     */
    /*
    public int copyMPC_Order_BOMLinesFrom(MPC_Order MPC_Order, boolean counter) {
    if (isProcessed() || isPosted() || MPC_Order == null)
    return 0;
    MPC_Order_BOMLine[] fromLines = MPC_Order.getLines(false);
    int count = 0;
    for (int i = 0; i < fromLines.length; i++) {
    MPC_Order_BOMLine line = new MPC_Order_BOMLine(this);
    PO.copyValues(fromLines[i], line, getAD_Client_ID(), getAD_Org_ID());
    line.setMPC_Order_ID(getMPC_Order_ID());
    //line.setOrder(order);
    line.setLine(0);
    line.setM_AttributeSetInstance_ID(0);
    line.setS_ResourceAssignment_ID(0);
    //
    /*line.setQtyDelivered(Env.ZERO);
    line.setQtyInvoiced(Env.ZERO);
    line.setQtyReserved(Env.ZERO);
    line.setDateDelivered(null);
    line.setDateInvoiced(null);
    //	Tax
    if (getC_BPartner_ID() != order.getC_BPartner_ID())
    line.setTax();		//	recalculate
    //
    if (counter)
    line.setRef_OrderLine_ID(fromLines[i].getC_OrderLine_ID());
    else
    line.setRef_OrderLine_ID(0);
    
    line.setProcessed(false);
    if (line.save())
    count++;
    //	Cross Link
    if (counter) {
    fromLines[i].setRef_OrderLine_ID(line.getC_OrderLine_ID());
    fromLines[i].save();
    }
    }
    if (fromLines.length != count)
    log.log(Level.SEVERE ,("copyLinesFrom - Line difference - From=" + fromLines.length + " <> Saved=" + count);
    return count;
    }	//	copyLinesFrom
     */
    /**************************************************************************
     * 	String Representation
     *	@return info
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("MMPCOrder[").append(get_ID()).append("-").append(getDocumentNo()).append(",IsSOTrx=").append(isSOTrx()).append(",C_DocType_ID=").append(getC_DocType_ID()).append("]");
        return sb.toString();
    }	//	toString

    /**************************************************************************
     * 	Get Lines of Order
     * 	@param whereClause where clause or null (starting with AND)
     * 	@return invoices
     */
    public MMPCOrderBOMLine[] getLines(String whereClause, String orderClause) {
        ArrayList<MMPCOrderBOMLine> list = new ArrayList<MMPCOrderBOMLine>();
        StringBuffer sql = new StringBuffer("SELECT * FROM MPC_Order_BOMLine WHERE MPC_Order_ID=? ");
        if (whereClause != null) {
            sql.append(whereClause);
        }
        if (orderClause != null) {
            sql.append(" ").append(orderClause);
        }
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
            pstmt.setInt(1, getMPC_Order_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MMPCOrderBOMLine(getCtx(), rs, get_TrxName()));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getLines - " + sql, e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            pstmt = null;
        }
        //
        MMPCOrderBOMLine[] lines = new MMPCOrderBOMLine[list.size()];
        list.toArray(lines);
        return lines;
    }	//	getLines

    /**
     * 	Get Lines of Order
     * 	@param requery requery
     * 	@param orderBy optional order by column
     * 	@return lines
     */
    public MMPCOrderBOMLine[] getLines(boolean requery, String orderBy) {
        if (m_order_bomlines != null && !requery) {
            return m_order_bomlines;
        }
        //
        String orderClause = "ORDER BY ";
        if (orderBy != null && orderBy.length() > 0) {
            orderClause += orderBy;
        } else {
            orderClause += "Line";
        }
        m_order_bomlines = getLines(null, orderClause);
        return m_order_bomlines;
    }	//	getLines

    /**
     * 	Set Processed.
     * 	Propergate to Lines/Taxes
     *	@param processed processed
     */
    public void setProcessed(boolean processed) {
        super.setProcessed(processed);
        if (get_ID() == 0) {
            return;
        }
        String set = "SET Processed='"
                + (processed ? "Y" : "N")
                + "' WHERE MPC_Order_ID=" + getMPC_Order_ID();
        int noLine = DB.executeUpdate("UPDATE MPC_Order " + set, get_TrxName());
        //m_lines = null;
        //log.fine("setProcessed - " + processed + " - Lines=" + noLine + ", Tax=" + noTax);
    }	//	setProcessed

    protected boolean beforeSave(boolean newRecord) {


        //generateMPC_Cost_Order(true);
        if (newRecord) {
            //	make sure DocType set to 0
            // Bugfix by Gunther Hoppe, 10.10.2005
            // Begin
            // What!?
        	/*
            if (getC_DocType_ID() == 0)
            setC_DocType_ID(0);
             */
            // Set this values at constructor!
            //setDocStatus(DocumentEngine.STATUS_NotApproved);
            //setDocAction(DocumentEngine.ACTION_Void);
            // End
        }
        //generateMPC_Cost_Order();
        if (getAD_Client_ID() == 0) {
            m_processMsg = "AD_Client_ID = 0";
            return false;
        }
        if (getAD_Org_ID() == 0) {
            int context_AD_Org_ID = Env.getAD_Org_ID(getCtx());
            if (context_AD_Org_ID == 0) {
                m_processMsg = "AD_Org_ID = 0";
                return false;
            }
            setAD_Org_ID(context_AD_Org_ID);
            log.warning("beforeSave - Changed Org to Context=" + context_AD_Org_ID);
        }

        if (getM_Warehouse_ID() == 0) {
            int ii = Env.getContextAsInt(getCtx(), "#M_Warehouse_ID");
            if (ii != 0) {
                setM_Warehouse_ID(ii);
            }
        }
        return true;
    }

    /**************************************************************************
     * 	Before Save
     *	@param newRecord new
     *	@return save
     */
    protected boolean afterSave(boolean newRecord, boolean success) {

        if (!newRecord) {
            MMPCMRP.MPC_Order(this, get_TrxName());
            if (is_ValueChanged("QtyOrdered") && getDocStatus().equals("DR")) {
                String delBLine = "Delete From MPC_Order_BOMLine where MPC_Order_ID=" + getMPC_Order_ID();
                int noLine0 = DB.executeUpdate(delBLine, get_TrxName());
                System.out.println("***** bomlines " + noLine0);

                String delBOM = "Delete From MPC_Order_BOM where MPC_Order_ID=" + getMPC_Order_ID();
                int noLine1 = DB.executeUpdate(delBOM, get_TrxName());
                System.out.println("***** boms " + noLine1);

                String delNodeNext = "Delete From MPC_Order_NodeNext where MPC_Order_ID=" + getMPC_Order_ID();
                int noLine2 = DB.executeUpdate(delNodeNext, get_TrxName());
                System.out.println("***** nodesnext " + noLine2);

                String delNode = "Delete From MPC_Order_Node where MPC_Order_ID=" + getMPC_Order_ID();
                int noLine3 = DB.executeUpdate(delNode, get_TrxName());
                System.out.println("***** nodes " + noLine3);

                String delWF = "Delete From MPC_Order_WorkFlow where MPC_Order_ID=" + getMPC_Order_ID();
                int noLine4 = DB.executeUpdate(delWF, get_TrxName());
                System.out.println("***** wf " + noLine4);
                /**
                 * BISion - 26/08/2009 - Santiago Iba�ez
                 * Modificacion realizada por COM-PAN-BUG-002 Generar Plan de Materiales
                 */
                String MRPdelBLine = "Delete From MPC_MRP where MPC_Order_ID=" + getMPC_Order_ID() + " and MPC_Order_BomLine_ID is not null";
                int noLine5 = DB.executeUpdate(MRPdelBLine, get_TrxName());
                System.out.println("***** MRP BomLine " + noLine5);

            } //                        MMPCOrderBOMLine[] lines =   getLines(getMPC_Order_ID());
            //                         for (int l = 0 ; l < lines.length ; l ++)
            //                         {
            //                                 if (lines[l].isQtyPercentage())
            //                                {
            //                                    BigDecimal qtyup =  lines[l].getQtyBatch().multiply(getQtyOrdered());
            //                                    if( lines[l].getComponentType().equals(lines[l].COMPONENTTYPE_Packing))
            //                                        lines[l].setQtyRequiered(qtyup.divide(new BigDecimal(100),8,qtyup.ROUND_UP));
            //                                    if (lines[l].getComponentType().equals(lines[l].COMPONENTTYPE_Component) || lines[l].getComponentType().equals(lines[l].COMPONENTTYPE_Phantom))
            //                                        lines[l].setQtyRequiered(qtyup.divide(new BigDecimal(100),8,qtyup.ROUND_UP));
            //                                    else if (lines[l].getComponentType().equals(lines[l].COMPONENTTYPE_Tools))
            //                                        lines[l].setQtyRequiered(lines[l].getQtyBOM());
            //
            //                                }
            //                                else
            //                                {
            //                                        if (lines[l].getComponentType().equals(lines[l].COMPONENTTYPE_Component) || lines[l].getComponentType().equals(lines[l].COMPONENTTYPE_Phantom))
            //                                                lines[l].setQtyRequiered(lines[l].getQtyBOM().multiply(getQtyOrdered()));
            //                                        else if (lines[l].getComponentType().equals(lines[l].COMPONENTTYPE_Packing))
            //                                                lines[l].setQtyRequiered(lines[l].getQtyBOM().multiply(getQtyOrdered()));
            //                                    else if (lines[l].getComponentType().equals(lines[l].COMPONENTTYPE_Tools))
            //                                        lines[l].setQtyRequiered(lines[l].getQtyBOM());
            //                                }
            //
            //                             lines[l].save(get_TrxName());
            //
            //                         }
            else {
                //generateMPC_Cost_Order(false);
                return success;
            }
        }

        //X_MPC_Order MPC_Order = new X_MPC_Order(getCtx(),getMPC_Order_ID() , get_TrxName());
        //MMPCMRP.MPC_Order(MPC_Order ,get_TrxName());
        log.fine("afterSave - MMPCOrder Query ok");

        setC_DocType_ID(0);

        // Create BOM Head
        MMPCProductBOM MPC_Product_BOM = new MMPCProductBOM(getCtx(), getMPC_Product_BOM_ID(), get_TrxName());

        boolean ValidFromBOM = true;
        boolean ValidToBOM = true;
        if (MPC_Product_BOM.getValidFrom() != null) {
            ValidFromBOM = getDateStartSchedule().compareTo(MPC_Product_BOM.getValidFrom()) >= 0 ? true : false;
        }

        if (MPC_Product_BOM.getValidTo() != null) {
            ValidToBOM = getDateStartSchedule().compareTo(MPC_Product_BOM.getValidTo()) <= 0 ? true : false;
        }

        if (ValidFromBOM && ValidToBOM) {
            MMPCOrderBOM MPC_Order_BOM = new MMPCOrderBOM(getCtx(), 0, get_TrxName());
            MPC_Order_BOM.setMPC_Order_ID(getMPC_Order_ID());
            //MPC_Order_BOM.setMPC_Product_BOM_ID(MPC_Product_BOM.getMPC_Product_BOM_ID());
            MPC_Order_BOM.setBOMType(MPC_Product_BOM.getBOMType());
            MPC_Order_BOM.setBOMUse(MPC_Product_BOM.getBOMUse());
            MPC_Order_BOM.setM_ChangeNotice_ID(MPC_Product_BOM.getM_ChangeNotice_ID());
            MPC_Order_BOM.setHelp(MPC_Product_BOM.getHelp());
            MPC_Order_BOM.setCopyFrom(MPC_Product_BOM.getCopyFrom());
            MPC_Order_BOM.setProcessing(MPC_Product_BOM.isProcessing());
            //MPC_Order_BOM(MPC_Product_BOM.getHelp());
            MPC_Order_BOM.setDescription(MPC_Product_BOM.getDescription());
            MPC_Order_BOM.setM_AttributeSetInstance_ID(MPC_Product_BOM.getM_AttributeSetInstance_ID());
            MPC_Order_BOM.setM_Product_ID(MPC_Product_BOM.getM_Product_ID());
            MPC_Order_BOM.setName(MPC_Product_BOM.getName());
            MPC_Order_BOM.setRevision(MPC_Product_BOM.getRevision());
            MPC_Order_BOM.setValidFrom(MPC_Product_BOM.getValidFrom());
            MPC_Order_BOM.setValidTo(MPC_Product_BOM.getValidTo());
            MPC_Order_BOM.setValue(MPC_Product_BOM.getValue());
            MPC_Order_BOM.setDocumentNo(MPC_Product_BOM.getDocumentNo());
            MPC_Order_BOM.setC_UOM_ID(MPC_Product_BOM.getC_UOM_ID());
            MPC_Order_BOM.save(get_TrxName());

            //Create BOM List ---------------------------------------------------------

            MMPCProductBOMLine[] MPC_Product_BOMline = MPC_Product_BOM.getLines();

            for (int i = 0; i < MPC_Product_BOMline.length; i++) {
                boolean ValidFromBOMLine = true;
                boolean ValidToBOMLine = true;
                if (MPC_Product_BOMline[i].getValidFrom() != null) {
                    ValidFromBOMLine = getDateStartSchedule().compareTo(MPC_Product_BOMline[i].getValidFrom()) >= 0 ? true : false;
                }

                if (MPC_Product_BOMline[i].getValidTo() != null) {
                    ValidToBOMLine = getDateStartSchedule().compareTo(MPC_Product_BOMline[i].getValidTo()) <= 0 ? true : false;
                }
                //MMPCOrderBOMLine MPC_Order_BOMLine = new MMPCOrderBOMLine(getCtx(),0,trx.getTrxName());
                if (ValidFromBOMLine && ValidToBOMLine && MPC_Product_BOMline[i].isActive()) {
                    MMPCOrderBOMLine MPC_Order_BOMLine = new MMPCOrderBOMLine(getCtx(), 0, get_TrxName());
                    MPC_Order_BOMLine.setM_ChangeNotice_ID(MPC_Product_BOMline[i].getM_ChangeNotice_ID());
                    MPC_Order_BOMLine.setHelp(MPC_Product_BOMline[i].getHelp());
                    MPC_Order_BOMLine.setAssay(MPC_Product_BOMline[i].getAssay());
                    MPC_Order_BOMLine.setQtyBatch(MPC_Product_BOMline[i].getQtyBatch());
                    MPC_Order_BOMLine.setQtyBOM(MPC_Product_BOMline[i].getQtyBOM());
                    MPC_Order_BOMLine.setIsQtyPercentage(MPC_Product_BOMline[i].isQtyPercentage());
                    MPC_Order_BOMLine.setComponentType(MPC_Product_BOMline[i].getComponentType());
                    MPC_Order_BOMLine.setC_UOM_ID(MPC_Product_BOMline[i].getC_UOM_ID());
                    MPC_Order_BOMLine.setForecast(MPC_Product_BOMline[i].getForecast());
                    MPC_Order_BOMLine.setIsCritical(MPC_Product_BOMline[i].isCritical());
                    MPC_Order_BOMLine.setIssueMethod(MPC_Product_BOMline[i].getIssueMethod());
                    // Changed by Gunther Hoppe, 30.09.2005
                    // Begin
                    //MPC_Order_BOMLine.setLine(MPC_Product_BOMline[i].getLine());
                    MPC_Order_BOMLine.setLine(MMPCOrder.getLines(getMPC_Order_ID(), get_TrxName()).length + 10);
                    // End
                    MPC_Order_BOMLine.setLeadTimeOffset(MPC_Product_BOMline[i].getLeadTimeOffset());
                    MPC_Order_BOMLine.setM_AttributeSetInstance_ID(MPC_Product_BOMline[i].getM_AttributeSetInstance_ID());
                    MPC_Order_BOMLine.setMPC_Order_BOM_ID(MPC_Order_BOM.getMPC_Order_BOM_ID());
                    MPC_Order_BOMLine.setMPC_Order_ID(getMPC_Order_ID());
                    MPC_Order_BOMLine.setM_Product_ID(MPC_Product_BOMline[i].getM_Product_ID());
                    MPC_Order_BOMLine.setScrap(MPC_Product_BOMline[i].getScrap());
                    MPC_Order_BOMLine.setValidFrom(MPC_Product_BOMline[i].getValidFrom());
                    MPC_Order_BOMLine.setValidTo(MPC_Product_BOMline[i].getValidTo());
                    MPC_Order_BOMLine.setM_Warehouse_ID(getM_Warehouse_ID());


                    BigDecimal QtyOrdered = getQtyOrdered();
                    MPC_Order_BOMLine.setQtyRequiered(MPC_Order_BOMLine.explodeQty(getQtyOrdered()));

                    // Set Scrap of Component
                    BigDecimal Scrap = MPC_Order_BOMLine.getScrap();

                    if (!Scrap.equals(Env.ZERO)) {
                        Scrap = Scrap.divide(new BigDecimal(100), 8, BigDecimal.ROUND_UP);
                        MPC_Order_BOMLine.setQtyRequiered(MPC_Order_BOMLine.getQtyRequiered().divide(Env.ONE.subtract(Scrap), 8, BigDecimal.ROUND_HALF_UP));
                    }

                    MPC_Order_BOMLine.save(get_TrxName());

                    if (MPC_Order_BOMLine.getComponentType().equals(MMPCProductBOMLine.COMPONENTTYPE_Phantom)) {
                        MPC_Order_BOMLine.setQtyRequiered(Env.ZERO);
                    }

                    MPC_Order_BOMLine.save(get_TrxName());

                } // end if From / To component
                MMPCOrderBOMLine[] lines = getLines(getMPC_Order_ID(), get_TrxName());
                for (int l = 0; l < lines.length; l++) {
                    if (lines[l].getComponentType().equals(MMPCProductBOMLine.COMPONENTTYPE_Phantom)) {
                        lines[l].setQtyRequiered(Env.ZERO);
                        lines[l].save(get_TrxName());
                    }
                }

            } // end Create Order BOM

        } // end if From / To parent

        //MMPCMRP.MPC_Order(MPC_Order ,get_TrxName());

        // Create Workflow (Routing & Process

        MWorkflow AD_Workflow = new MWorkflow(getCtx(), getAD_Workflow_ID(), get_TrxName());

        boolean ValidFromWF = true;
        boolean ValidToWF = true;
        if (AD_Workflow.getValidFrom() != null) {
            ValidFromWF = getDateStartSchedule().compareTo(AD_Workflow.getValidFrom()) >= 0 ? true : false;
        }

        if (AD_Workflow.getValidTo() != null && getDateStartSchedule() != null) {
            ValidToWF = getDateStartSchedule().compareTo(AD_Workflow.getValidTo()) <= 0 ? true : false;
        }

        if (ValidFromWF && ValidToWF) {
            MMPCOrderWorkflow MPC_Order_Workflow = new MMPCOrderWorkflow(getCtx(), 0, get_TrxName());
            MPC_Order_Workflow.setValue(AD_Workflow.getValue());
            MPC_Order_Workflow.setQtyBatchSize(AD_Workflow.getQtyBatchSize());
            MPC_Order_Workflow.setName(AD_Workflow.getName());
            MPC_Order_Workflow.setAccessLevel(AD_Workflow.getAccessLevel());
            MPC_Order_Workflow.setAuthor(AD_Workflow.getAuthor());
            MPC_Order_Workflow.setDurationUnit(AD_Workflow.getDurationUnit());
            MPC_Order_Workflow.setDuration(AD_Workflow.getDuration());
            MPC_Order_Workflow.setEntityType(AD_Workflow.getEntityType());	// U
            MPC_Order_Workflow.setIsDefault(AD_Workflow.isDefault());
            MPC_Order_Workflow.setPublishStatus(AD_Workflow.getPublishStatus());	// U
            MPC_Order_Workflow.setVersion(AD_Workflow.getVersion());
            MPC_Order_Workflow.setCost(AD_Workflow.getCost());
            MPC_Order_Workflow.setWaitingTime(AD_Workflow.getWaitingTime());
            MPC_Order_Workflow.setWorkingTime(AD_Workflow.getWorkingTime());
            MPC_Order_Workflow.setAD_WF_Responsible_ID(AD_Workflow.getAD_WF_Responsible_ID());
            MPC_Order_Workflow.setAD_Workflow_ID(AD_Workflow.getAD_Workflow_ID());
            MPC_Order_Workflow.setLimit(AD_Workflow.getLimit());
            MPC_Order_Workflow.setMPC_Order_ID(getMPC_Order_ID());
            MPC_Order_Workflow.setPriority(AD_Workflow.getPriority());
            MPC_Order_Workflow.setValidateWorkflow(AD_Workflow.getValidateWorkflow());


            MPC_Order_Workflow.save(get_TrxName());


            MWFNode[] AD_WF_Node = AD_Workflow.getNodes(false, getAD_Client_ID());

            if (AD_WF_Node != null) {
                for (int g = 0; g < AD_WF_Node.length; g++) {

                    boolean ValidFromNode = true;
                    boolean ValidToNode = true;
                    if (AD_WF_Node[g].getValidFrom() != null) {
                        ValidFromNode = getDateStartSchedule().compareTo(AD_WF_Node[g].getValidFrom()) >= 0 ? true : false;
                    }

                    if (AD_WF_Node[g].getValidTo() != null) {
                        ValidToNode = getDateStartSchedule().compareTo(AD_WF_Node[g].getValidTo()) <= 0 ? true : false;
                    }

                    if (ValidFromNode && ValidToNode) {
                        MMPCOrderNode MPC_Order_Node = new MMPCOrderNode(getCtx(), 0, get_TrxName());
                        MPC_Order_Node.setAction(AD_WF_Node[g].getAction());	// N
                        MPC_Order_Node.setAD_WF_Node_ID(AD_WF_Node[g].getAD_WF_Node_ID());
                        MPC_Order_Node.setAD_WF_Responsible_ID(AD_WF_Node[g].getAD_WF_Responsible_ID());
                        MPC_Order_Node.setAD_Workflow_ID(AD_WF_Node[g].getAD_Workflow_ID());
                        MPC_Order_Node.setCost(AD_WF_Node[g].getCost());
                        MPC_Order_Node.setDuration(AD_WF_Node[g].getDuration());
                        MPC_Order_Node.setEntityType(AD_WF_Node[g].getEntityType());
                        MPC_Order_Node.setIsCentrallyMaintained(AD_WF_Node[g].isCentrallyMaintained());
                        MPC_Order_Node.setJoinElement(AD_WF_Node[g].getJoinElement());	// X
                        MPC_Order_Node.setLimit(AD_WF_Node[g].getLimit());
                        MPC_Order_Node.setMPC_Order_ID(getMPC_Order_ID());
                        MPC_Order_Node.setMPC_Order_Workflow_ID(MPC_Order_Workflow.getMPC_Order_Workflow_ID());
                        MPC_Order_Node.setName(AD_WF_Node[g].getName());
                        MPC_Order_Node.setPriority(AD_WF_Node[g].getPriority());
                        MPC_Order_Node.setSplitElement(AD_WF_Node[g].getSplitElement());	// X
                        MPC_Order_Node.setSubflowExecution(AD_WF_Node[g].getSubflowExecution());
                        MPC_Order_Node.setValue(AD_WF_Node[g].getValue());
                        MPC_Order_Node.setS_Resource_ID(AD_WF_Node[g].getS_Resource_ID());
                        MPC_Order_Node.setSetupTime(AD_WF_Node[g].getSetupTime());
                        // fjviejo e-evolution el tiempo requerido es el tiempo por lote  por el numero de lotes
                        //  MPC_Order_Node.setSetupTimeRequiered(AD_WF_Node[g].getSetupTime());
                        MPC_Order_Node.setSetupTimeRequiered(AD_WF_Node[g].getSetupTime() * getQtyBatchs().intValue());
                        // fjviejo e-evolution end
                        BigDecimal time = new BigDecimal(AD_WF_Node[g].getDuration()).multiply(getQtyOrdered());
                        MPC_Order_Node.setDurationRequiered(time.intValue());
                        MPC_Order_Node.setMovingTime(AD_WF_Node[g].getMovingTime());
                        MPC_Order_Node.setWaitingTime(AD_WF_Node[g].getWaitingTime());
                        MPC_Order_Node.setWorkingTime(AD_WF_Node[g].getWorkingTime());;
                        MPC_Order_Node.setQueuingTime(AD_WF_Node[g].getQueuingTime());
                        MPC_Order_Node.setXPosition(AD_WF_Node[g].getXPosition()); //e-evolution generatemodel
                        MPC_Order_Node.setYPosition(AD_WF_Node[g].getYPosition()); //e-evolution generatemodel
                        // fjv e-evolution Operation Activity Report begin
                        MPC_Order_Node.setIsSubcontracting(AD_WF_Node[g].isSubcontracting());
                        MPC_Order_Node.setIsBatchTime(AD_WF_Node[g].isBatchTime());

                        /*      Vit4B 10/01/2008
                         *      Agregado para que tambien actualice con el valor de BatchTime
                         *
                         */

                        //MPC_Order_Node.isBATCHTIME(AD_WF_Node[g].getb);

                        MPC_Order_Node.setM_Product_ID(AD_WF_Node[g].getM_Product_ID());
                        // fjv e-evolution Operation Activity Report end
                        MPC_Order_Node.save(get_TrxName());

                        MWFNodeNext[] AD_WF_NodeNext = AD_WF_Node[g].getTransitions(getAD_Client_ID());
                        System.out.println("AD_WF_NodeNext" + AD_WF_NodeNext.length);
                        if (AD_WF_NodeNext != null) {
                            for (int n = 0; n < AD_WF_NodeNext.length; n++) {
                                MMPCOrderNodeNext MPC_Order_NodeNext = new MMPCOrderNodeNext(getCtx(), 0, get_TrxName());
                                MPC_Order_NodeNext.setAD_WF_Node_ID(AD_WF_NodeNext[n].getAD_WF_Node_ID());
                                MPC_Order_NodeNext.setAD_WF_Next_ID(AD_WF_NodeNext[n].getAD_WF_Next_ID());
                                MPC_Order_NodeNext.setMPC_Order_Node_ID(MPC_Order_Node.getMPC_Order_Node_ID());
                                MPC_Order_NodeNext.setAD_WF_Next_ID(AD_WF_NodeNext[n].getAD_WF_Next_ID());

                                MPC_Order_NodeNext.setMPC_Order_Next_ID(n);

                                //Get MPC Order Node Next
                                /*MWFNode nodeNext = new MWFNode(getCtx(),AD_WF_NodeNext[n].getAD_WF_Next_ID(),get_TrxName());
                                MPC_Order_NodeNext.setMPC_Order_Next_ID(nodeNext.get);*/


                                MPC_Order_NodeNext.setDescription(AD_WF_NodeNext[n].getDescription());
                                MPC_Order_NodeNext.setEntityType(AD_WF_NodeNext[n].getEntityType());
                                MPC_Order_NodeNext.setIsStdUserWorkflow(AD_WF_NodeNext[n].isStdUserWorkflow());
                                MPC_Order_NodeNext.setMPC_Order_ID(getMPC_Order_ID());
                                MPC_Order_NodeNext.setSeqNo(AD_WF_NodeNext[n].getSeqNo());
                                MPC_Order_NodeNext.setTransitionCode(AD_WF_NodeNext[n].getTransitionCode());
                                System.out.println("MPC_Order_NodeNext = " + MPC_Order_NodeNext.getAD_WF_Node_ID());
                                MPC_Order_NodeNext.save(get_TrxName());
                            }// end for Node Next
                        }

                    }// end for Node

                    //set transition for order
                    MMPCOrderWorkflow OrderWorkflow = new MMPCOrderWorkflow(getCtx(), MPC_Order_Workflow.getMPC_Order_Workflow_ID(), get_TrxName());
                    MMPCOrderNode[] OrderNodes = OrderWorkflow.getNodes(false);

                    //System.out.println("-----------------------OrderNodes"+OrderNodes.length);
                    if (OrderNodes != null) {


                        /*
                         *      Agregado para que tome el nodo de inicio establecido en el WF
                         *      General para la instanciaci�n de la orden
                         *
                         */


                        for (int ind = 0; ind < OrderNodes.length; ind++) {
                            if (OrderNodes[ind].getAD_WF_Node_ID() == AD_Workflow.getAD_WF_Node_ID()) {
                                OrderWorkflow.setMPC_Order_Node_ID(OrderNodes[ind].getMPC_Order_Node_ID());
                            }

                        }

                        //OrderWorkflow.setMPC_Order_Node_ID(OrderNodes[0].getMPC_Order_Node_ID());





                        OrderWorkflow.save(get_TrxName());
                        for (int h = 0; h < OrderNodes.length; h++) {
                            MMPCOrderNodeNext[] nexts = OrderNodes[h].getTransitions();
                            //System.out.println("----------------------MPC_Order_NodeNext"+nexts.length);
                            if (nexts != null) {
                                for (int n = 0; n < nexts.length; n++) {

                                    String sql = "SELECT MPC_Order_Node_ID FROM MPC_Order_Node WHERE MPC_Order_ID = ?  AND AD_WF_Node_ID = ? AND AD_Client_ID=?";
                                    PreparedStatement pstmt = null;
                                    ResultSet rs = null;
                                    try {

                                        pstmt = DB.prepareStatement(sql, get_TrxName());
                                        pstmt.setInt(1, nexts[n].getMPC_Order_ID());
                                        pstmt.setInt(2, nexts[n].getAD_WF_Next_ID());
                                        pstmt.setInt(3, nexts[n].getAD_Client_ID());
                                        rs = pstmt.executeQuery();
                                        while (rs.next()) {
                                            nexts[n].setMPC_Order_Next_ID(rs.getInt(1));
                                            nexts[n].save(get_TrxName());
                                        }
                                        rs.close();
                                        rs = null;
                                        pstmt.close();
                                        pstmt = null;
                                    } catch (Exception e) {
                                        log.log(Level.SEVERE, "doIt - " + sql, e);
                                    }
                                    try {
                                        if (rs != null) {
                                            rs.close();
                                        }
                                        if (pstmt != null) {
                                            pstmt.close();
                                        }
                                    } catch (Exception e) {
                                        log.log(Level.SEVERE, "MMPCOrder.afterSave - No se pudieron cerrar rs ni pstmt" + sql, e);
                                    }

                                }// end for Node Next
                            }
                        }
                    }

                }// end from / to Node

            } // end from /to Workflow

        }
        //generateMPC_Cost_Order(false);
        MMPCMRP.MPC_Order(this, get_TrxName());
        return true;
    }	//	beforeSave

//fjv e-evolution fix the duplicated qtyordered begin
    /**************************************************************************
     *	Prepare Document
     * 	@return new status (In Progress or Invalid)
     */
    public String prepareItfromaprove() {
        log.info("prepareIt - " + toString());
        log.info(toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
        if (m_processMsg != null) {
            return DocAction.STATUS_Invalid;
        }

        //	Std Period open?
                /*
        if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType()))
        {
        m_processMsg = "@PeriodClosed@";
        return DocAction.STATUS_Invalid;
        }
         */


        //	Convert DocType
        if (getC_DocType_ID() != getC_DocTypeTarget_ID()) {
            //	New or in Progress/Invalid
            if (DOCSTATUS_Drafted.equals(getDocStatus())
                    || DOCSTATUS_InProgress.equals(getDocStatus())
                    || DOCSTATUS_Invalid.equals(getDocStatus())
                    || getC_DocType_ID() == 0) {
                setC_DocType_ID(getC_DocTypeTarget_ID());
            }
        }	//	convert DocType

        //	Mandatory Product Attribute Set Instance
        /*
        String mandatoryType = "='Y'";	//	IN ('Y','S')
        String sql = "SELECT COUNT(*) "
        + "FROM MPC_Order_BOMLine obl"
        + " INNER JOIN M_Product p ON (obl.M_Product_ID=p.M_Product_ID)"
        + " INNER JOIN M_AttributeSet pas ON (p.M_AttributeSet_ID=pas.M_AttributeSet_ID) "
        + " INNER JOIN MPC_Order_BOM obom ON (obl.MPC_Order_BOM_ID=obom.MPC_Order_BOM_ID) "
        + " WHERE pas.MandatoryType" + mandatoryType
        + " AND obl.M_AttributeSetInstance_ID IS NULL"
        + " AND obom.MPC_Order_ID=?";
        
        int no = DB.getSQLValue("MPC_Order",sql, getMPC_Order_ID());
        if (no != 0) {
        m_processMsg = "@LinesWithoutProductAttribute@ (" + no + ")";
        return DocAction.STATUS_Invalid;
        }*/
        MDocType doc = new MDocType(getCtx(), getC_DocType_ID(), get_TrxName());
        if (doc.getDocBaseType().equals(MDocType.DOCBASETYPE_QualityOrder)) {
            return DocAction.STATUS_InProgress;
        }

        //	Lines
        MMPCOrderBOMLine[] lines = getLines(true, "M_Product_ID");
        if (lines.length == 0) {
            m_processMsg = "@NoLines@";
            return DocAction.STATUS_Invalid;
        }



        m_justPrepared = true;
        //	if (!DOCACTION_Complete.equals(getDocAction()))		don't set for just prepare
        //		setDocAction(DOCACTION_Complete);
        return DocAction.STATUS_Approved;
    }	//	prepareItfromaprove
    //fjv e-evolution fix the duplicated qtyordered end

    protected boolean beforeDelete() {
        // OrderBOMLine

        if (Env.getContext(p_ctx, "RUN_MRP").equals("true")) {
            deleteMMPCOrderParentOrder();
            return true;
        } else {
            /*
             * Comentado por Pablo
             * Las Ordenes de Manufacturas no se pueden borrar.
             * Siempre devuelve False.
             */

            JOptionPane.showMessageDialog(null, "No se pueden borrar las Ordenes de Manufactura", "Error", JOptionPane.ERROR_MESSAGE);
            return false;



        }
        /*
        if (getDocStatus().equals(DOCSTATUS_Drafted) || getDocStatus().equals(this.DOCSTATUS_InProgress))
        {
        
        // This needs the missing MPC_Order_Node_Trl table in AD_Table
        int[] ids = null;
        PO po = null;
        boolean ok = true;
        
        ids = PO.getAllIDs("MPC_Order_Cost", "MPC_Order_ID="+get_ID()+ " AND AD_Client_ID="+ getAD_Client_ID(), get_TrxName());
        for(int i = 0; i < ids.length; i++) {
        
        po = new MMPCOrderCost(Env.getCtx(), ids[i], get_TrxName());
        ok = po.delete(true);
        if(!ok) {
        
        return ok;
        }
        }
        ids = PO.getAllIDs("MPC_Order_Node_Asset", "MPC_Order_ID="+get_ID()+ " AND AD_Client_ID="+ getAD_Client_ID(), get_TrxName());
        for(int i = 0; i < ids.length; i++) {
        
        po = new X_MPC_Order_Node_Asset(Env.getCtx(), ids[i], get_TrxName());
        ok = po.delete(true);
        if(!ok) {
        
        return ok;
        }
        }
        ids = PO.getAllIDs("MPC_Order_Node", "MPC_Order_ID="+get_ID()+ " AND AD_Client_ID="+ getAD_Client_ID(), get_TrxName());
        for(int i = 0; i < ids.length; i++) {
        
        po = new MMPCOrderNode(Env.getCtx(), ids[i], get_TrxName());
        ok = po.delete(true);
        if(!ok) {
        
        return ok;
        }
        }
        ids = PO.getAllIDs("MPC_Order_NodeNext", "MPC_Order_ID="+get_ID()+ " AND AD_Client_ID="+ getAD_Client_ID(), get_TrxName());
        for(int i = 0; i < ids.length; i++) {
        
        po = new MMPCOrderNodeNext(Env.getCtx(), ids[i], get_TrxName());
        ok = po.delete(true);
        if(!ok) {
        
        return ok;
        }
        }
        ids = PO.getAllIDs("MPC_Order_Node_Product", "MPC_Order_ID="+get_ID()+ " AND AD_Client_ID="+ getAD_Client_ID(), get_TrxName());
        for(int i = 0; i < ids.length; i++) {
        
        po = new X_MPC_Order_Node_Product(Env.getCtx(), ids[i], get_TrxName());
        ok = po.delete(true);
        if(!ok) {
        
        return ok;
        }
        }
        ids = PO.getAllIDs("MPC_Order_Workflow", "MPC_Order_ID="+get_ID()+ " AND AD_Client_ID="+ getAD_Client_ID(), get_TrxName());
        for(int i = 0; i < ids.length; i++) {
        
        po = new MMPCOrderWorkflow(Env.getCtx(), ids[i], get_TrxName());
        ok = po.delete(true);
        if(!ok) {
        
        return ok;
        }
        }
        ids = PO.getAllIDs("MPC_Order_BOMLine", "MPC_Order_ID="+get_ID()+ " AND AD_Client_ID="+ getAD_Client_ID(), get_TrxName());
        for(int i = 0; i < ids.length; i++) {
        
        po = new MMPCOrderBOMLine(Env.getCtx(), ids[i], get_TrxName());
        ok = po.delete(true);
        if(!ok) {
        
        return ok;
        }
        }
        ids = PO.getAllIDs("MPC_Order_BOM", "MPC_Order_ID="+get_ID()+ " AND AD_Client_ID="+ getAD_Client_ID(), get_TrxName());
        for(int i = 0; i < ids.length; i++) {
        
        po = new MMPCOrderBOM(Env.getCtx(), ids[i], get_TrxName());
        ok = po.delete(true);
        if(!ok) {
        
        return ok;
        }
        }
        ids = PO.getAllIDs("MPC_MRP", "MPC_Order_ID="+get_ID()+ " AND AD_Client_ID="+ getAD_Client_ID(), get_TrxName());
        for(int i = 0; i < ids.length; i++) {
        
        po = new MMPCMRP(Env.getCtx(), ids[i], get_TrxName());
        ok = po.delete(true);
        if(!ok) {
        
        return ok;
        }
        }
        }*/
// End

    }	//	beforeDelete

    /**************************************************************************
     * 	Process Order - Start Process
     *	@return true if ok
     */
    /*
    public boolean processOrder(String docAction) {
    setDocAction(docAction);
    save();
    log.fine("processOrder - " + getDocAction());
    int AD_Process_ID = 104;	//	C_Order_Post
    MProcess pp = new MProcess(getCtx(), AD_Process_ID);
    boolean ok = pp.processIt(getMPC_Order_ID()).isOK();
    load();		//	reload
    //log.fine("processOrder - ok=" + ok + " - GrandTotal=" + getGrandTotal());
    return ok;
    }	//	process
     */
    /**************************************************************************
     * 	Process document
     *	@param processAction document action
     *	@return true if performed
     */
    public boolean processIt(String processAction) {
        m_processMsg = null;
        DocumentEngine engine = new DocumentEngine(this, getDocStatus());
        return engine.processIt(processAction, getDocAction());
    }	//	processIt
    /**	Process Message 			*/
    private String m_processMsg = null;
    /**	Just Prepared Flag			*/
    private boolean m_justPrepared = false;

    /**
     * 	Unlock Document.
     * 	@return true if success
     */
    public boolean unlockIt() {
        log.info("unlockIt - " + toString());
        setProcessing(false);
        return true;
    }	//	unlockIt

    /**
     * 	Invalidate Document
     * 	@return true if success
     */
    public boolean invalidateIt() {
        log.info("invalidateIt - " + toString());
        setDocAction(DOCACTION_Prepare);
        return true;
    }	//	invalidateIt

    /**************************************************************************
     *	Prepare Document
     * 	@return new status (In Progress or Invalid)
     */
    public String prepareIt() {
        log.info("prepareIt - " + toString());
        log.info(toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
        if (m_processMsg != null) {
            return DocAction.STATUS_Invalid;
        }
        MDocType dt = MDocType.get(getCtx(), getC_DocTypeTarget_ID());

        //	Std Period open?
		/*if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType()))
        {
        m_processMsg = "@PeriodClosed@";
        return DocAction.STATUS_Invalid;
        }*/

        //	Lines
        MMPCOrderBOMLine[] lines = getLines(true, "M_Product_ID");
        if (lines.length == 0) {
            m_processMsg = "@NoLines@";
            return DocAction.STATUS_Invalid;
        }

        //	Cannot change Std to anything else if different warehouses
        if (getC_DocType_ID() != 0) {
            /*MDocType dtOld = MDocType.get(getCtx(), getC_DocType_ID());
            if (MDocType.DOCSUBTYPESO_StandardOrder.equals(dtOld.getDocSubTypeSO())		//	From SO
            && !MDocType.DOCSUBTYPESO_StandardOrder.equals(dt.getDocSubTypeSO()))	//	To !SO
            {*/
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].getM_Warehouse_ID() != getM_Warehouse_ID()) {
                    log.warning("different Warehouse " + lines[i]);
                    m_processMsg = "@CannotChangeDocType@";
                    return DocAction.STATUS_Invalid;
                }
            }
            //}
        }

        //	New or in Progress/Invalid
        if (DOCSTATUS_Drafted.equals(getDocStatus())
                || DOCSTATUS_InProgress.equals(getDocStatus())
                || DOCSTATUS_Invalid.equals(getDocStatus())
                || getC_DocType_ID() == 0) {
            setC_DocType_ID(getC_DocTypeTarget_ID());
        }


        MDocType doc = new MDocType(getCtx(), getC_DocType_ID(), get_TrxName());
        if (doc.getDocBaseType().equals(MDocType.DOCBASETYPE_QualityOrder)) {
            return DocAction.STATUS_InProgress;
        }

        if (lines.length == 0) {
            m_processMsg = "@NoLines@";
            return DocAction.STATUS_Invalid;
        }

        if (!reserveStock(lines)) {
            m_processMsg = "Cannot reserve Stock";
            return DocAction.STATUS_Invalid;
        }

        if (!orderStock()) {
            m_processMsg = "Cannot Order Stock";
            return DocAction.STATUS_Invalid;
        }

        m_justPrepared = true;
        //	if (!DOCACTION_Complete.equals(getDocAction()))		don't set for just prepare
        //		setDocAction(DOCACTION_Complete);
        return DocAction.STATUS_InProgress;
    }	//	prepareIt

    private boolean orderStock() {
        MProduct product = new MProduct(getCtx(), getM_Product_ID(), get_TrxName());
        if (product != null
                && product.isStocked()) {


            BigDecimal target = getQtyOrdered();
            BigDecimal difference = target.subtract(getQtyReserved()).subtract(getQtyDelivered());

            if (difference.signum() == 0) {
                return true;
            }
            BigDecimal ordered = difference;
            int M_Locator_ID = 0;
            //	Get Locator to reserve
            if (getM_AttributeSetInstance_ID() != 0) //	Get existing Location
            {
                M_Locator_ID = MStorage.getM_Locator_ID(getM_Warehouse_ID(),
                        getM_Product_ID(), getM_AttributeSetInstance_ID(),
                        ordered, get_TrxName());
            }
            //	Get default Location
            if (M_Locator_ID == 0) {
                MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
                M_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
            }
            //	Update Storage
            if (!MStorage.add(getCtx(), getM_Warehouse_ID(), M_Locator_ID,
                    getM_Product_ID(),
                    getM_AttributeSetInstance_ID(), getM_AttributeSetInstance_ID(),
                    Env.ZERO, Env.ZERO, ordered, get_TrxName())) {
                return false;
            }

            setQtyReserved(getQtyReserved().add(difference));
            //	update line
            if (!save(get_TrxName())) {
                return false;
            }
        }

        return true;
    }

    private boolean unorderStock() {
        MProduct product = new MProduct(getCtx(), getM_Product_ID(), get_TrxName());
        if (product != null
                && product.isStocked()) {


            BigDecimal ordered = getQtyOrdered();
            if (ordered.signum() == 0) {
                return true;
            }
            int M_Locator_ID = 0;
            //	Get Locator to reserve
            if (getM_AttributeSetInstance_ID() != 0) //	Get existing Location
            {
                M_Locator_ID = MStorage.getM_Locator_ID(getM_Warehouse_ID(),
                        getM_Product_ID(), getM_AttributeSetInstance_ID(),
                        ordered, get_TrxName());
            }
            //	Get default Location
            if (M_Locator_ID == 0) {
                MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
                M_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
            }
            //	Update Storage (negative ordered to unorder)
            if (!MStorage.addDist(getCtx(), getM_Warehouse_ID(), M_Locator_ID,
                    getM_Product_ID(),
                    getM_AttributeSetInstance_ID(), getM_AttributeSetInstance_ID(),
                    Env.ZERO, Env.ZERO, Env.ZERO.subtract(ordered), get_TrxName())) {
                return false;
            }

            setQtyReserved(Env.ZERO);
            //	update line
            if (!save(get_TrxName())) {
                return false;
            }
        }
        return true;
    }

    //fjviejo e-evolution clear ordered
    private boolean orderedStock() {
        MProduct product = new MProduct(getCtx(), getM_Product_ID(), get_TrxName());
        if (product != null && product.isStocked()) {
            BigDecimal target = getQtyOrdered();
            BigDecimal difference = Env.ZERO.subtract(getQtyOrdered());
            if (difference.signum() == 0) {
                return true;
            }

            BigDecimal ordered = difference;

            int M_Locator_ID = 0;
            //	Get Locator to order
            if (getM_AttributeSetInstance_ID() != 0) //	Get existing Location
            {
                M_Locator_ID = MStorage.getM_Locator_ID(getM_Warehouse_ID(),
                        getM_Product_ID(), getM_AttributeSetInstance_ID(),
                        ordered, get_TrxName());
            }
            //	Get default Location
            if (M_Locator_ID == 0) {
                MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
                M_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
            }
            //	Update Storage
            if (!MStorage.add(getCtx(), getM_Warehouse_ID(), M_Locator_ID,
                    getM_Product_ID(),
                    getM_AttributeSetInstance_ID(), getM_AttributeSetInstance_ID(),
                    Env.ZERO, Env.ZERO, ordered, get_TrxName())) {
                return false;
            }
            System.out.println("***** ordered  to " + getQtyOrdered().add(difference) + " reserved to " + getQtyReserved().add(difference));
            /*
             *      Una vez cerradas las �rdenes ponemos el ordenado a cero
             *      y lo mismo para el reservado.
             *
             */
            setQtyOrdered(Env.ZERO);
            setQtyReserved(Env.ZERO);



            //	update line
            if (!save(get_TrxName())) {
                return false;
            }
        }

        return true;
    }
    //fjviejo e-evolution clear reserved

    /**
     * 	Reserve Inventory.
     * 	Counterpart: MInOut.completeIt()
     * 	@param dt document type or null
     * 	@param lines order lines (ordered by M_Product_ID for deadlock prevention)
     * 	@return true if (un) reserved
     */
    private boolean reserveStock(MMPCOrderBOMLine[] lines) {
        //vpj if (dt == null)
        //vpj	dt = MDocType.get(getCtx(), getC_DocType_ID());

        //	Binding
        //vpj boolean binding = !dt.isProposal();
        //	Not binding - i.e. Target=0
        //vpj if (DOCACTION_Void.equals(getDocAction())
        //	//	Closing Binding Quotation
        //vpj	|| (MDocType.DOCSUBTYPESO_Quotation.equals(dt.getDocSubTypeSO())
        //vpj		&& DOCACTION_Close.equals(getDocAction()))
        //vpj 	|| isDropShip() )
        //vpj 	binding = false;
        boolean isSOTrx = isSOTrx();
        //vpj log.fine("Binding=" + binding + " - IsSOTrx=" + isSOTrx);
        //	Force same WH for all but SO/PO
        int header_M_Warehouse_ID = getM_Warehouse_ID();
        //vpj if (MDocType.DOCSUBTYPESO_StandardOrder.equals(dt.getDocSubTypeSO())
        //vpj 	|| MDocType.DOCBASETYPE_PurchaseOrder.equals(dt.getDocBaseType()))
        //vpj 	header_M_Warehouse_ID = 0;		//	don't enforce

        //	Always check and (un) Reserve Inventory
        for (int i = 0; i < lines.length; i++) {
            MMPCOrderBOMLine line = lines[i];
            //	Check/set WH/Org
            if (header_M_Warehouse_ID != 0) //	enforce WH
            {
                if (header_M_Warehouse_ID != line.getM_Warehouse_ID()) {
                    line.setM_Warehouse_ID(header_M_Warehouse_ID);
                }
                if (getAD_Org_ID() != line.getAD_Org_ID()) {
                    line.setAD_Org_ID(getAD_Org_ID());
                }
            }
            //	Binding
            //vpj BigDecimal target = binding ? line.getQtyOrdered() : Env.ZERO;
            BigDecimal target = line.getQtyRequiered();
            BigDecimal difference = target.subtract(line.getQtyReserved()).subtract(line.getQtyDelivered());
            if (difference.signum() == 0) {
                continue;
            }

            log.fine("Line=" + line.getLine()
                    + " - Target=" + target + ",Difference=" + difference
                    + " - Requiered=" + line.getQtyRequiered()
                    + ",Reserved=" + line.getQtyReserved() + ",Delivered=" + line.getQtyDelivered());

            //	Check Product - Stocked and Item
            MProduct product = line.getProduct();
            if (product != null) {
                if (product.isStocked()) {
                    /*
                     * GENEOS - Pablo Velazquez
                     * 26/07/2013
                     * Modificacion para reservar las cantidades en partidas especificas
                     * segun politica FEFO
                     */
                    //BigDecimal reserved = isSOTrx ? difference : Env.ZERO;
                    BigDecimal reserved = difference;
                    int M_Locator_ID = 0;
                   
                    /*
                     * Reservo en storage CERO 
                     */

                    MWarehouse wh = MWarehouse.get(getCtx(), line.getM_Warehouse_ID());
                    M_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
                    MStorage storageDefaultZero = MStorage.getCreate(getCtx(), M_Locator_ID, line.getM_Product_ID(), 0, get_TrxName());

                    if (!MStorage.addDist(getCtx(), line.getM_Warehouse_ID(), storageDefaultZero.getM_Locator_ID(),
                            line.getM_Product_ID(),
                            storageDefaultZero.getM_AttributeSetInstance_ID(), storageDefaultZero.getM_AttributeSetInstance_ID(),
                            Env.ZERO, reserved, Env.ZERO, get_TrxName())) {
                        return false;
                    }

                }	//	stockec
                //	update line
                line.setQtyReserved(line.getQtyReserved().add(difference));
                if (!line.save(get_TrxName())) {
                    return false;
                }
            }
        }	//	reverse inventory
        return true;
    }	//	reserveStock
//fjviejo e-evolution clear reserved

    /**
     * 	Reserve Inventory.
     * 	Counterpart: MInOut.completeIt()
     * 	@param dt document type or null
     * 	@param lines order lines (ordered by M_Product_ID for deadlock prevention)
     * 	@return true if (un) reserved
     */
    public boolean UnreserveStock(MMPCOrderBOMLine[] lines) {
        boolean isSOTrx = isSOTrx();
        //	Force same WH for all but SO/PO
        int header_M_Warehouse_ID = getM_Warehouse_ID();
        //	Always check and (un) Reserve Inventory

        if (lines == null) {
            lines = getLines(true, "M_Product_ID");
        }

        for (int i = 0; i < lines.length; i++) {
            MMPCOrderBOMLine line = lines[i];
            //	Check/set WH/Org
            if (header_M_Warehouse_ID != 0) //	enforce WH
            {
                if (header_M_Warehouse_ID != line.getM_Warehouse_ID()) {
                    line.setM_Warehouse_ID(header_M_Warehouse_ID);
                }
                if (getAD_Org_ID() != line.getAD_Org_ID()) {
                    line.setAD_Org_ID(getAD_Org_ID());
                }
            }
            BigDecimal target = Env.ZERO;
            log.fine("#### MODIFICACION STORAGE POR CIERRE: MMPCOrder - " + line.getMPC_Order_ID() + " MMPCOrderBOMLine - " + line.getMPC_Order_BOMLine_ID() + " Datos - " + line.getQtyReserved() + ", " + line.getQtyDelivered());
            /*
             *      Vit4B 10/08/2007
             *      Modificaci�n para el control de la actualizacion de los
             *      valores de las lineas de la orden ...
             *
             *      Lo que hago es actualizar con la cantidad pendiente que es la reservada ...
             *
             */
            BigDecimal difference = target.subtract(line.getQtyReserved());
            if (difference.signum() == 0) {
                continue;
            }
            log.fine("Line=" + line.getLine()
                    + " - Target=" + target + ",Difference=" + difference
                    + " - Requiered=" + line.getQtyRequiered()
                    + ",Reserved=" + line.getQtyReserved() + ",Delivered=" + line.getQtyDelivered());

            //	Check Product - Stocked and Item
            MProduct product = line.getProduct();
            if (product != null) {
                if (product.isStocked()) {
                    //vpj BigDecimal ordered = isSOTrx ? Env.ZERO : difference;
                    BigDecimal ordered = Env.ZERO;
                    //BigDecimal reserved = isSOTrx ? difference : Env.ZERO;
                    BigDecimal reserved = difference;


                    /*
                     * Saco el reservado de el storage CERO
                     */

                    MWarehouse wh = MWarehouse.get(getCtx(), line.getM_Warehouse_ID());
                    int defaultLocator = wh.getDefaultLocator().getM_Locator_ID();
                    //	Actualizo Storage CERO
                    if (!MStorage.addDist(getCtx(), line.getM_Warehouse_ID(), defaultLocator,
                            line.getM_Product_ID(),
                            line.getM_AttributeSetInstance_ID(), 0,
                            Env.ZERO, difference, Env.ZERO, get_TrxName())) {
                        return false;
                    }

                }	//	stockec
                //	update line
                System.out.println("***** reserved to " + line.getQtyReserved().add(difference));
                line.setQtyReserved(line.getQtyReserved().add(difference));
                if (!line.save(get_TrxName())) {
                    return false;
                }
            }
        }	//	reverse inventory
        return true;
    }	//	unreserveStock

    /**
     * 	Approve Document
     * 	@return true if success
     */
    public boolean approveIt() {
        log.info("approveIt - " + toString());
        MDocType doc = new MDocType(getCtx(), getC_DocType_ID(), get_TrxName());
        if (doc.getDocBaseType().equals(MDocType.DOCBASETYPE_QualityOrder)) {
            int QM_Specification_ID = DB.getSQLValue(get_TrxName(), "SELECT QM_Specification_ID FROM QM_Specification WHERE MPC_Product_BOM_ID=? AND AD_Workflow_ID =?", getMPC_Product_BOM_ID(), getAD_Workflow_ID());
            MQMSpecification qms = new MQMSpecification(getCtx(), QM_Specification_ID, get_TrxName());
            return qms.isValid(getM_AttributeSetInstance_ID());
        } else {
            setIsApproved(true);
        }

        return true;
    }	//	approveIt

    /**
     * 	Reject Approval
     * 	@return true if success
     */
    public boolean rejectIt() {
        log.info("rejectIt - " + toString());
        setIsApproved(false);
        return true;
    }	//	rejectIt

    /**
     * Bision - 03/07/2008 - Santiago Iba�ez
     * Funcion que dada una orden de manufactura retorna si la misma tiene
     * asociada un registro de costo (con el elemento de costo dado).
     */
    public boolean tieneRegistroCosto(int MPC_Cost_Element_ID, int MPC_Order_ID) {
        String filter = "MPC_Order_ID = " + MPC_Order_ID + " AND MPC_Cost_Element_ID = " + MPC_Cost_Element_ID;
        PreparedStatement ps = DB.prepareStatement("select MPC_Order_Cost_ID from MPC_ORDER_COST where " + filter, get_TrxName());
        ResultSet results = null;
        try {
            results = ps.executeQuery();
            while (results.next()) {
                results.close();
                ps.close();
                return true;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "tieneRegistroCosto", e);
        }
        try {
            //Si no se alcanzo a cerrar el result set lo cierro
            if (results != null) {
                results.close();
                results = null;
            }
            //Si no se alcanzo a cerrar el prepared statement lo cierro
            if (ps != null) {
                ps.close();
                ps = null;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "getRegistroCosto", e);
        }

        return false;
    }

    /**
     * Bision 03/07/2008
     * Funcion que retorna un registro de costo de la OM en cuesti�n con un
     * elemento de costo dado. En caso de no existir crea uno nuevo.
     */
    private MMPCOrderCost getRegistroCosto(int MPC_Order_ID, int MPC_Cost_Element_ID, int C_AcctSchema_ID) {
        //creo el costo de la orden y seteo la info
        String filter = "MPC_Order_ID = " + this.getMPC_Order_ID() + " AND MPC_Cost_Element_ID = " + MPC_Cost_Element_ID;
        PreparedStatement ps = DB.prepareStatement("SELECT MPC_Order_Cost_ID FROM MPC_ORDER_COST WHERE " + filter, get_TrxName());
        ResultSet results = null;
        try {
            results = ps.executeQuery();
            while (results.next()) {
                int MPC_Order_Cost_ID = results.getInt(1);
                results.close();
                ps.close();
                results = null;
                ps = null;
                MMPCOrderCost oc = new MMPCOrderCost(getCtx(), MPC_Order_Cost_ID, get_TrxName());
                return oc;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "getRegistroCosto", e);
        }
        try {
            //Si no se alcanzo a cerrar el result set lo cierro
            if (results != null) {
                results.close();
                results = null;
            }
            //Si no se alcanzo a cerrar el prepared statement lo cierro
            if (ps != null) {
                ps.close();
                ps = null;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "getRegistroCosto", e);
        }

        //si no existe un registro de costo creo uno nuevo.
        MMPCOrderCost mpc_order_cost = new MMPCOrderCost(getCtx(), 0, null);
        mpc_order_cost.setMPC_Order_ID(MPC_Order_ID);
        mpc_order_cost.setC_AcctSchema_ID(C_AcctSchema_ID);
        mpc_order_cost.setM_Product_ID(getM_Product_ID());
        mpc_order_cost.setMPC_Cost_Element_ID(MPC_Cost_Element_ID);
        return mpc_order_cost;
    }

    /* Bision - 17/07/2008 - Santiago Iba�ez
     * M�todo que crea o actualiza un registro de costo asociado para una
     * orden de manufatura, un esquema contable y un importe dados.
     * */
    private void createMPC_Cost_Order(int C_AcctSchema_ID, BigDecimal amt, int MPC_Cost_Element_ID, int MPC_Order_ID) {
        {
            //obtengo el costo real (de acuerdo a MPC_Cost_Element_ID) y actualizo o crea uno nuevo
            MMPCOrderCost mpc_order_cost = getRegistroCosto(MPC_Order_ID, MPC_Cost_Element_ID, C_AcctSchema_ID);
            mpc_order_cost.setCostCumQty(amt);
            mpc_order_cost.save(null);
        }
    }
    //**************************************************************
    //                      METODO PRINCIPAL
    //**************************************************************
     /* Bision - 17/07/2008 - Santiago Iba�ez
     * M�todo principal que calcula el costo real de recursos y el costo real de
     * materiales y genera (o actualiza) los registros de costo para la orden
     * de manufactura asociada.
     * */

    public void generateMPC_Cost_Order(boolean generarDetalleCosto) {
        //obtengo del contexto el esquema, el tipo de costo y la organizacion
        int C_AcctSchema_ID = Env.getContextAsInt(getCtx(), "$C_AcctSchema_ID");
        int M_CostType_ID = Env.getContextAsInt(getCtx(), "$M_CostType_ID");
        int AD_Org_ID = Env.getAD_Org_ID(getCtx());
        //obtengo el id correspondiente al elemento de costos de recursos
        int costoRecursosEstandar_ID = MCostElement.getCostElementByName("Costo Estandar de Recursos").getM_CostElement_ID();

        //calculo el costo de recursos
        BigDecimal resourceCost = getResourceCost(costoRecursosEstandar_ID, AD_Org_ID, M_CostType_ID, this.getM_Product_ID(), C_AcctSchema_ID, generarDetalleCosto);
        //obtengo el id correspondiente al elemento de costo real de recursos
        int costoRecursos_ID = MMPCCostElement.getCostElementByName("Costo Real de Recursos").getMPC_Cost_Element_ID();
        //Creo los registros de costo asociados a la orden de manufactura
        createMPC_Cost_Order(C_AcctSchema_ID, resourceCost, costoRecursos_ID, this.getMPC_Order_ID());

        //obtengo el id correspondiente al elemento de costo real de materiales
        int costoMateriales_ID = MMPCCostElement.getCostElementByName("Costo Real de Materiales").getMPC_Cost_Element_ID();
        BigDecimal materialsCost = getMaterialsCost(this.getMPC_Order_ID(), costoMateriales_ID, C_AcctSchema_ID, generarDetalleCosto);

        createMPC_Cost_Order(C_AcctSchema_ID, materialsCost, costoMateriales_ID, this.getMPC_Order_ID());
        //Unicamente si no tiene costos indirectos creo un registro en cero.
        int costoIndirecto_ID = MMPCCostElement.getCostElementByName("Costo Real Indirecto").getMPC_Cost_Element_ID();
        if (!tieneRegistroCosto(costoIndirecto_ID, this.getMPC_Order_ID())) {
            createMPC_Cost_Order(C_AcctSchema_ID, BigDecimal.ZERO, costoIndirecto_ID, this.getMPC_Order_ID());
        }
    }
    /* Bision - 15/07/2008 - Santiago Iba�ez
     * Funci�n que dado un producto me devuelve todos los MCost asociados
     * */
    /*private MCost[] getMCosts(int M_Product_ID,int C_AccSchema_ID)
    {
    ArrayList<MCost> list = new ArrayList<MCost>();
    QueryDB query = new QueryDB("org.compiere.model.X_M_Cost");
    String filter = "M_Product_ID = " + M_Product_ID + " AND C_ACCTSCHEMA_ID = " + C_AccSchema_ID;
    java.util.List results = query.execute(filter);
    Iterator select = results.iterator();
    while (select.hasNext())
    {
    MCost mcost = (MCost) select.next();
    list.add(mcost);
    }
    MCost[] mcosts = new MCost[list.size()];
    return list.toArray(mcosts);
    }*/

    //**************************************************************
    //METODOS ESPECIFICOS PARA EL CALCULO DEL COSTO REAL DE MATERIALES
    //**************************************************************

    /* Bision - 30/07/2008 - Santiago Iba�ez
     * Funcion que retorna el costo real de materiales de una OM dada
     * recorriendo los reportes de actividades.
     * NOTA: Las devoluciones tienen la "Cantidad del Movimiento" < 0
     * */
    private BigDecimal getMaterialsCost(int MPC_Order_ID, int MPC_Cost_Element_ID, int C_AcctSchema_ID, boolean generarDetalleCosto) {
        System.out.println("Calculando costo de materiales...");
        BigDecimal cost = BigDecimal.ZERO;
        MMPCCostCollector[] ra = getMMPCCostCollector(MPC_Order_ID);
        for (int i = 0; i < ra.length; i++) {
            //chequeo que el tipo de documento sea surtimiento o recepcion, usando el doctype asociado
            MDocType doc = MDocType.get(getCtx(), ra[i].getC_DocType_ID());
            if (doc.getDocBaseType().equalsIgnoreCase(MDocType.DOCBASETYPE_ManufacturingOrderIssue)
                    || doc.getDocBaseType().equalsIgnoreCase(MDocType.DOCBASETYPE_ManufacturingOrderReceipt)) {
                //Si el producto de la l�nea NO es el producto terminado
                if (ra[i].getM_Product_ID() != this.getM_Product_ID()) //si es materia prima...
                {
                    if (esMateriaPrima(ra[i].getM_Product_ID())) {
                        //obtengo el precio de la materia prima desde la OC
                        System.out.println(ra[i].getM_Product_ID() + " es Materia Prima");
                        BigDecimal price = getPrecioMateriaPrima(ra[i].getM_Product_ID(), ra[i].getM_AttributeSetInstance_ID());

                        //obtengo la cantidad del movimiento desde el reporte de actividades
                        BigDecimal qty = ra[i].getMovementQty();
                        //acumulo el costo
                        cost = cost.add(price.multiply(qty));
                        /** BISion - 29/12/2008 - Santiago Iba�ez
                         * Modificacion realizada para ingresar detalle de
                         * costos de materiales
                         */
                        //obtengo el producto para extraerle la unidad de medida
                        MProduct p = new MProduct(getCtx(), ra[i].getM_Product_ID(), get_TrxName());
                        if (generarDetalleCosto) {
                            actualizarDetalleCostoMaterial(getMPC_Order_ID(), ra[i].getM_Product_ID(), ra[i].getM_AttributeSetInstance_ID(), ra[i].getMovementQty(), price, price.multiply(qty), p.getC_UOM_ID());
                        }
                        /**
                         * fin de modificacion BISion
                         */
                    } //si no es materia prima
                    else {
                        System.out.println(ra[i].getM_Product_ID() + " NO es Materia Prima");
                        //obtengo la orden de manufactura para el producto de la l�nea y su partida
                        MMPCOrder order = getMMPCOrderByProduct(ra[i].getM_Product_ID(), ra[i].getM_AttributeSetInstance_ID());
                        if (order != null) {
                            System.out.println("Orden de Manufactura: " + order.getDocumentNo());
                            BigDecimal costo = BigDecimal.ZERO;
                            //si no tiene registros de costo real de material asociado
                            if (!tieneRegistroCosto(MPC_Cost_Element_ID, order.getMPC_Order_ID())) {
                                //obtengo recursivamente el costo real de materiales de la OM
                                //costo = order.getMaterialsCost(order.getMPC_Order_ID(),MPC_Cost_Element_ID,C_AcctSchema_ID);
                                //creo el registro de costo real de material para la OM order
                                //order.generateMPC_Cost_Order(generarDetalleCosto);
                                costo = getCostoOM(order.getMPC_Order_ID(), MPC_Cost_Element_ID);
                                //le agrego ademas el costo real de recursos
                                int costoRecursos_ID = MMPCCostElement.getCostElementByName("Costo Real de Recursos").getMPC_Cost_Element_ID();
                                costo = costo.add(getCostoOM(order.getMPC_Order_ID(), costoRecursos_ID));
                                int costoIndirecto_ID = MMPCCostElement.getCostElementByName("Costo Real Indirecto").getMPC_Cost_Element_ID();
                                //le agrego el costo indirecto en caso de que lo tenga
                                costo = costo.add(getCostoOM(order.getMPC_Order_ID(), costoIndirecto_ID));
                            } //si tiene registro de costo real de materiales asociado
                            else {
                                costo = getCostoOM(order.getMPC_Order_ID(), MPC_Cost_Element_ID);
                                //le agrego adem�s el costo real de recursos
                                int costoRecursos_ID = MMPCCostElement.getCostElementByName("Costo Real de Recursos").getMPC_Cost_Element_ID();
                                costo = costo.add(getCostoOM(order.getMPC_Order_ID(), costoRecursos_ID));
                                int costoIndirecto_ID = MMPCCostElement.getCostElementByName("Costo Real Indirecto").getMPC_Cost_Element_ID();
                                //le agrego en caso de que tenga el costo real indirecto
                                costo = costo.add(getCostoOM(order.getMPC_Order_ID(), costoIndirecto_ID));
                                System.out.println("Costo Real: " + costo);
                            }
                            //CALCULO EL COSTO UNITARIO
                            BigDecimal unitario = BigDecimal.ZERO;
                            //Si la cantidad entregada es diferente a 0 calculo el costo unitario
                            if (!order.getQtyDelivered().equals(BigDecimal.ZERO)) {
                                //obtengo la cantidad real que se utilizo de la OM de un producto elaborado (Ej PL..,GL... etc)
                                BigDecimal qty = ra[i].getMovementQty();
                                System.out.println("Cantidad de Movimiento: " + qty);
                                //para guardar el costo unitario
                                unitario = costo.divide(order.getQtyDelivered(), 12, BigDecimal.ROUND_HALF_UP);
                                System.out.println("Costo Unitario: $" + unitario);
                                //Si la cantidad entregada de la OM es diferente a la cantidad real que se utilizo de la OM
                                if (order.getQtyDelivered() != qty) {
                                    //al costo de la OM lo divido por la cantidad de esa OM (costo por unidad de OM)
                                    costo = costo.divide(order.getQtyDelivered(), 12, BigDecimal.ROUND_HALF_UP);
                                    //al costo le multiplico la cantidad real que se utilizo de la OM
                                    costo = costo.multiply(qty);
                                }
                                System.out.println("Costo Total: $" + costo);
                                //Considero costo = 0
                            } else {
                                System.out.println("Cantidad Entregada de la Orden en cero");
                                costo = BigDecimal.ZERO;
                            }
                            /** BISion - 29/12/2008 - Santiago Iba�ez
                             * Modificacion realizada para ingresar detalle de
                             * costos de materiales
                             */
                            /*if (generarDetalleCosto)
                            actualizarDetalleCostoMaterial(getMPC_Order_ID(),ra[i].getM_Product_ID(),ra[i].getM_AttributeSetInstance_ID(),ra[i].getMovementQty(),unitario,cost,ra[i].getC_UOM_ID());
                             */
                            /**
                             * fin de modificacion BISion
                             */
                            /** GENEOS - 07/05/2013 - Pablo Velazquez
                             * Modificacion realizada para devolver correctamente la unidad de medida del producto
                             * y el costo total ( costo )
                             */
                            //obtengo el producto para extraerle la unidad de medida
                            MProduct p = new MProduct(getCtx(), ra[i].getM_Product_ID(), get_TrxName());
                            if (generarDetalleCosto) {
                                actualizarDetalleCostoMaterial(getMPC_Order_ID(), ra[i].getM_Product_ID(), ra[i].getM_AttributeSetInstance_ID(), ra[i].getMovementQty(), unitario, costo, p.getC_UOM_ID());
                            }


                            //acumulo el costo
                            cost = cost.add(costo);
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encontro la OM para el producto elaborado: " + ra[i].getM_Product_ID(), "Mensage", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        }
        System.out.println("Costo Real de Materiales: $" + cost);
        return cost;
    }

    /**
     * BISion - 30/12/2008 - Santiago Iba�ez
     * Metodo que actualiza o crea un registro de detalle de costo de material
     * @param MPC_Order_ID orden de manufactura
     * @param M_Product_ID producto que integra la formula del producto a manufacturar
     * @param M_AttributeSetInstance_ID partida del producto
     * @param qty cantidad del producto utilizada
     * @param cost costo de material
     */
    private void actualizarDetalleCostoMaterial(int MPC_Order_ID, int M_Product_ID, int M_AttributeSetInstance_ID, BigDecimal qty, BigDecimal price, BigDecimal cost, int C_UOM_ID) {
        //Obtengo el ID del elemento de costo real de materiales
        int MPC_Cost_Element_ID = MMPCCostElement.getCostElementByName("Costo Real de Materiales").getMPC_Cost_Element_ID();
        //Obtengo (si existe) o creo un nuevo Detalle de Costo
        X_MPC_Cost_Detail matCost = MMPCCostDetail.getMaterialCostDetail(MPC_Order_ID, M_Product_ID, M_AttributeSetInstance_ID, MPC_Cost_Element_ID, Env.getCtx(), get_TrxName());
        //seteo la OM asociada
        matCost.setMPC_Order_ID(MPC_Order_ID);
        //seteo el producto del cost collector
        matCost.setM_Product_ID(M_Product_ID);
        //seteo la partida del producto
        matCost.setM_AttributeSetInstance(M_AttributeSetInstance_ID);
        //seteo la cantidad
        matCost.setCANTIDAD(qty);
        //seteo el costo unitario
        matCost.setCOSTO_UNITARIO(price);
        //seteo el costo total
        matCost.setCOSTO_TOTAL(cost);
        //guardo detalle de costo (Si era nuevo se genera un nuevo ID)
        matCost.setC_UOM_ID(C_UOM_ID);
        //seteo el elemento de costo
        matCost.setMPC_Cost_Element_ID(MPC_Cost_Element_ID);
        matCost.set_TrxName(get_TrxName());
        matCost.save();
    }

    /** BISion - 06/01/2009 - Santiago Iba�ez
     * Metodo que actualiza o crea un detalle de costo real de recursos. Es
     * utilizado para los reportes de explosion de costos.
     * @param MPC_Order_ID orden de manufactura
     * @param M_Product_ID recurso utilizado durante el nodo
     * @param MPC_Order_Node_ID nodo del flujo de trabajo
     * @param qty cantidad
     * @param price costo unitario
     * @param cost costo
     * @param C_UOM_ID unidad del recursos (hs, min, seg, etc).
     */
    private void actualizarDetalleCostoRecursos(int MPC_Order_ID, int M_Product_ID, int MPC_Order_Node_ID, BigDecimal qty, BigDecimal price, BigDecimal cost, String unidadDuracionReal, int C_UOM_ID) {
        //Obtengo el ID del elemento de costo real de materiales
        int MPC_Cost_Element_ID = MMPCCostElement.getCostElementByName("Costo Real de Recursos").getMPC_Cost_Element_ID();
        //Obtengo (si existe) o creo un nuevo Detalle de Costo
        X_MPC_Cost_Detail recCost = MMPCCostDetail.getResourceCostDetail(MPC_Order_ID, M_Product_ID, MPC_Order_Node_ID, MPC_Cost_Element_ID, getCtx(), get_TrxName());
        //seteo la OM asociada
        recCost.setMPC_Order_ID(MPC_Order_ID);
        //seteo el producto del cost collector
        recCost.setM_Product_ID(M_Product_ID);
        //seteo el nodo del worklow
        recCost.setMPC_Order_Node_ID(MPC_Order_Node_ID);
        //seteo la cantidad
        recCost.setCANTIDAD(qty);
        //seteo el costo unitario
        recCost.setCOSTO_UNITARIO(price);
        //seteo el costo total
        recCost.setCOSTO_TOTAL(cost);
        //seteo la unidad del recurso
        recCost.setC_UOM_ID(C_UOM_ID);
        //seteo el elemento de costo
        recCost.setMPC_Cost_Element_ID(MPC_Cost_Element_ID);
        //seteo la unidad de tiempo del costo unitario
        recCost.setUNIDAD_RECURSO(unidadDuracionReal);
        //guardo detalle de costo (Si era nuevo se genera un nuevo ID)
        recCost.set_TrxName(get_TrxName());
        recCost.save();
    }
    /* Bision - 17/07/2008 - Santiago Iba�ez
     * Metodo que retorna TODAS las transacciones para una orden de manufactura
     * */

    public MMPCCostCollector[] getMMPCCostCollector(int MPC_Order_ID) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<MMPCCostCollector> list = new ArrayList<MMPCCostCollector>();
        try {
            //24-11-2011 Camarzana Mariano
            //Se le agrego que ordene por producto para el reporte de Costo Detallado y tambien el tipo de aceso al metodo
            String filter = "MPC_Order_ID = " + MPC_Order_ID;
            ps = DB.prepareStatement("select MPC_Cost_Collector_ID from MPC_Cost_Collector where " + filter + " order by m_product_id", get_TrxName());
            rs = ps.executeQuery();
            while (rs.next()) {
                int MPC_Cost_Collector_ID = rs.getInt(1);
                MMPCCostCollector c = new MMPCCostCollector(getCtx(), MPC_Cost_Collector_ID, get_TrxName());
                list.add(c);
            }
            rs.close();
            ps.close();
            rs = null;
            ps = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getMMPCCostCollector", e);
        }
        try {
            //Si no se alcanzo a cerrar el result set lo cierro
            if (rs != null) {
                rs.close();
                rs = null;
            }
            //Si no se alcanzo a cerrar el prepared statement lo cierro
            if (ps != null) {
                ps.close();
                ps = null;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "getMMPCCostCollector", e);
        }
        MMPCCostCollector[] cost = new MMPCCostCollector[list.size()];
        return list.toArray(cost);
    }

    /* Bision - 30/07/2008 - Santiago Iba�ez
     * Funcion que retorna dada una OM su costo real correspondiente al
     * elemento de costo dado (materiales, recursos, indirecto)
     * Retorna -1 si no tiene registros
     * */
    public BigDecimal getCostoOM(int MPC_Order_ID, int MPC_Cost_Elmenent_ID) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String filter = "MPC_Order_ID = " + MPC_Order_ID;
            ps = DB.prepareStatement("select mpc_order_cost_id from mpc_order_cost where " + filter, get_TrxName());
            rs = ps.executeQuery();
            while (rs.next()) {
                int MPC_Order_Cost_ID = rs.getInt(1);
                //rs = null;
                //ps = null;
                MMPCOrderCost oc = new MMPCOrderCost(getCtx(), MPC_Order_Cost_ID, get_TrxName());
                /*07-04-2011 Camarzana Mariano
                 * Se agrego la condicion if (oc.getMPC_Cost_Element_ID()== MPC_Cost_Elmenent_ID) porque estaba
                 *  retornando el primero que encontraba en vez del elemento de costo que se pasa como parametro
                 */
                if (oc.getMPC_Cost_Element_ID() == MPC_Cost_Elmenent_ID) {
                    rs.close();
                    ps.close();
                    return oc.getCostCumQty();
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "getCostoOM", e);
        }
        try {
            //Si no se alcanzo a cerrar el result set lo cierro
            if (rs != null) {
                rs.close();
                rs = null;
            }
            //Si no se alcanzo a cerrar el prepared statement lo cierro
            if (ps != null) {
                ps.close();
                ps = null;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "getCostoOM", e);
        }
        return BigDecimal.ZERO;
    }

    /*
     * 03-03-2011 Camarzana Mariano
     * Metodo agregado para controlar que la linea de la factura este asociada a una 
     * factura que existe (Al eliminar las facturas no se estan eliminando las lineas)
     */
    private boolean tieneFactura(int invoice_id) {
        PreparedStatement ps = DB.prepareStatement(" select c_invoice_id from c_invoice where c_invoice_id = " + invoice_id, null);
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rs.close();
                ps.close();
                return true;
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
        }

        return false;
    }

    /* Bision - 15/07/2008 - Santiago Iba�ez
     * Metodo realizado para calcular el costo real de materiales de una materia prima dada
     * Costo Materia Prima = Precio * Cantidad del Movimiento
     * */
    public BigDecimal getPrecioMateriaPrima(int M_Product_ID, int M_AttributeSetInstance_ID) {
        //obtengo la linea del recibo de materiales dados un producto y su partida
        MInOutLine receiptLine = getMInOutLine(M_Product_ID, M_AttributeSetInstance_ID);
        BigDecimal price = BigDecimal.ZERO;
        boolean tieneFacturaAsociada = false;
        if (receiptLine != null) {
            /** BISion - 25/03/2009 - Santiago Iba�ez
             * Modificacion realizada para obtener el precio desde la factura y no desde la OC
             */
            int[] facturas = MInvoiceLine.getAllIDs("C_InvoiceLine", "M_InoutLine_ID = " + receiptLine.getM_InOutLine_ID() + " AND M_Product_ID = " + receiptLine.getM_Product_ID(), null);
            //tomo la primer factura que encuentro
            if (facturas != null && facturas.length > 0) {

                System.out.println("Tiene factura");


                MInvoiceLine linea = new MInvoiceLine(getCtx(), facturas[0], null);

                /*
                 * 03-03-2011 Camarzana Mariano
                 */
                tieneFacturaAsociada = tieneFactura(linea.getC_Invoice_ID());
                if (tieneFacturaAsociada) {
                    MInvoice invoice = new MInvoice(Env.getCtx(), linea.getC_Invoice_ID(), null);
                    BigDecimal tasaConversion = invoice.getCurrencyISO().equals("ARS") ? BigDecimal.ONE : invoice.getCotizacion();
                    if (!linea.getQtyInvoiced().equals(BigDecimal.ZERO)) {
                        price = linea.getLineNetAmt().divide(linea.getQtyInvoiced(), 8, BigDecimal.ROUND_HALF_UP).multiply(tasaConversion);
                    }
                }

                //15-02-2011 Camarzana Mariano modificacion realizada para que realice la conversion en el caso de que la moneda sea distinta
                //if (!linea.getQtyInvoiced().equals(BigDecimal.ZERO))
                //  price = linea.getLineNetAmt().divide(linea.getQtyInvoiced(),8,BigDecimal.ROUND_HALF_UP);

            }
            //Si no tiene facturas asociadas chequeo por la orden de compra
            if (facturas == null || !tieneFacturaAsociada) {
                int C_OrderLine = receiptLine.getC_OrderLine_ID();
                if (C_OrderLine != 0) {
                    //Obtengo la OC
                    MOrderLine orderLine = new MOrderLine(getCtx(), C_OrderLine, get_TrxName());
                    if (orderLine != null) {
                        //15-02-2011 Idem con las ordenes
                        MOrder order = new MOrder(Env.getCtx(), orderLine.getC_Order_ID(), null);
                        BigDecimal cotizacion = order.getCotizacion();
                        System.out.println("Precio obtenido de una Orden de Compra");
                        price = orderLine.getPriceEntered().multiply(cotizacion);
                    }
                }
            }
            //fin modificacion BISion
        } else {
            System.out.println("No Hay recepciones");
        }
        System.out.println("Precio Obtenido: $" + price);
        return price;
    }

    /* Bision - 15/07/2008 - Santiago Iba�ez
     * Metodo realizado para calcular el costo real de materiales
     * NOTA: No es necesario pasar como parametro el elemento de costo
     *
    private BigDecimal getMaterialsCost(int AD_Org_ID,int M_Product_ID,int C_AcctSchema_ID,int MPC_Order_ID){
    System.out.println("getMaterialsCost()");
    //Obtengo las l�neas de la lista de materiales (MPC_Order_BOMLine)
    MMPCOrderBOMLine[] lines = MMPCOrder.getLines(MPC_Order_ID);
    //por cada l�nea
    BigDecimal cost = Env.ZERO;
    for (int i=0;i<lines.length;i++){
    //si se trata de una materia prima
    if (esMateriaPrima(lines[i].getM_Product_ID())){
    cost = cost.add(getCostoMateriaPrima(lines[i].getM_Product_ID()));
    }
    //si no es materia prima (comienza la recursion)
    else{
    //obtengo la orden de manufactura para el producto de la l�nea y su partida
    MMPCOrder order = getMMPCOrderByProduct(lines[i].getM_Product_ID(), lines[i].getM_AttributeSetInstance_ID());
    //calculo en forma recursiva el costo
    cost = cost.add(getMaterialsCost(AD_Org_ID,lines[i].getM_Product_ID(),lines[i].getM_AttributeSetInstance_ID(),order.getMPC_Order_ID()));
    }
    }
    return cost;
    }*/

    /* Bision - 17/07/2008 - Santiago Iba�ez
     * Funcion que dado un producto indica si es una materia prima
     * */
    public boolean esMateriaPrima(int M_Product_ID) {
        //Obtengo la formula del producto
        int bom = DB.getSQLValue(null, "SELECT * FROM MPC_Order_BOM WHERE M_Product_ID = ? ", M_Product_ID);
        //Si el producto tiene una formula entonces NO es materia prima
        if (bom != -1) {
            return false;
        }
        return true;
    }

    /** BISion - 16/02/2010 - Santiago Iba�ez
     * Metodo que retorna si las ordenes de los productos fabricados que integran la formula
     * de ESTA orden estaan cerradas.
     * @return
     */
    private boolean tieneOrdenesCerradas() {
        MMPCCostCollector cc[] = getMMPCCostCollector(getMPC_Order_ID());
        for (int i = 0; i < cc.length; i++) {
            MDocType doc = new MDocType(Env.getCtx(), cc[i].getC_DocType_ID(), get_TrxName());
            if (doc.getDocBaseType().equalsIgnoreCase(MDocType.DOCBASETYPE_ManufacturingOrderIssue)) {
                MProduct p = new MProduct(getCtx(), cc[i].getM_Product_ID(), get_TrxName());
                if (p != null && p.isBOM()) {
                    MMPCOrder order = getMMPCOrderByProduct(cc[i].getM_Product_ID(), cc[i].getM_AttributeSetInstance_ID());
                    if (order != null && !order.getDocStatus().equals(DOCSTATUS_Closed)) {
                        return false;
                    }
                }
            }
        }
        return true;

    }

    /* Bision - 15/07/2008 - Santiago Iba�ez
     * M�todo realizado para calcular el costo real de materiales de una materia prima dada
     * Costo Materia Prima = Precio * Cantidad del Movimiento
     * NOTA: Las devoluciones tienen la "Cantidad del Movimiento" < 0
     *
    private BigDecimal getCostoMateriaPrima(int M_Product_ID){
    System.out.println("getCostoMateriaPrima()");
    BigDecimal cost = Env.ZERO;
    //obtengo todas las transacciones para la materia prima
    MMPCCostCollector[] ra= getMMPCCostCollectorByProduct(this.getMPC_Order_ID(),M_Product_ID);
    for (int i=0;i<ra.length;i++){
    //chequeo que el tipo de documento sea surtimiento o recepcion, usando el doctype asociado
    MDocType doc = MDocType.get(getCtx(),ra[i].getC_DocType_ID());
    if (doc.getDocBaseType().equalsIgnoreCase(MDocType.DOCBASETYPE_ManufacturingOrderIssue)
    ||doc.getName().equalsIgnoreCase(MDocType.DOCBASETYPE_ManufacturingOrderReceipt)){
    //obtengo la l�nea del recibo de materiales dados un producto y su partida
    MInOutLine receiptLine = getMInOutLine(M_Product_ID,ra[i].getM_AttributeSetInstance_ID());
    //obtengo el recibo de material en el que est� la l�nea anterior
    MInOut receipt = new MInOut(getCtx(),receiptLine.getM_InOut_ID(),get_TrxName());
    //obtengo la l�nea de la orden de compra en la que se encuentra el producto
    MOrderLine orderline = new MOrderLine(getCtx(),receiptLine.getC_OrderLine_ID(),get_TrxName());
    //obtengo el importe por unidad de la linea de orden de compra
    BigDecimal price = orderline.getPriceEntered();
    //obtengo la cantidad del movimiento
    BigDecimal qty = ra[i].getMovementQty();
    //acumulo el costo
    cost = cost.add(price.multiply(qty));
    }
    }
    return cost;
    }*/

    /* Bision - 17/07/2008 - Santiago Iba�ez
     * M�todo que retorna las transacciones para una materia prima
     *
    private MMPCCostCollector[] getMMPCCostCollectorByProduct(int MPC_Order_ID, int M_Product_ID){
    ArrayList<MMPCCostCollector> list = new ArrayList<MMPCCostCollector>();
    QueryDB query = new QueryDB("org.compiere.model.X_MPC_Cost_Collector");
    String filter = "M_Product_ID = " + M_Product_ID + "AND MPC_Order_ID = " + MPC_Order_ID;
    java.util.List results = query.execute(filter);
    Iterator select = results.iterator();
    while (select.hasNext())
    {
    X_MPC_Cost_Collector cc = (X_MPC_Cost_Collector) select.next();
    MMPCCostCollector c = new MMPCCostCollector(getCtx(),cc.getMPC_Cost_Collector_ID(),get_TrxName());
    list.add(c);
    }
    MMPCCostCollector[] cost = new MMPCCostCollector[list.size()];
    return list.toArray(cost);
    }*/

    /* Bision - 17/07/2008 - Santiago Iba�ez
     * Metodo que retorna la linea de recepcion de materiales para un
     * producto y partida dados.
     * */
    private MInOutLine getMInOutLine(int M_Product_ID, int M_AttributeSetInstance_ID) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            /*
             * 28-02-2011 Camarzana Mariano
             * Modificado para que me tome solamente la recepcion de materiales de terceros
             */
            /*String filter = "M_Product_ID = " + M_Product_ID + " AND M_AttributeSetInstance_ID = "+M_AttributeSetInstance_ID;
            ps = DB.prepareStatement("SELECT M_InOutLine_id from M_InOutLine where "+ filter, get_TrxName());
            rs = ps.executeQuery();*/

            String filter = "M_Product_ID = " + M_Product_ID + " AND M_AttributeSetInstance_ID = " + M_AttributeSetInstance_ID
                    + " and m_inout.movementtype like 'V+'";
            ps = DB.prepareStatement("SELECT M_InOutLine_id from M_InOutLine "
                    + " join m_inout on (m_inout.m_inout_id = m_inoutline.m_inout_id) where " + filter, get_TrxName());
            rs = ps.executeQuery();

            while (rs.next()) {
                int M_InOutLine_ID = rs.getInt(1);
                rs.close();
                ps.close();
                rs = null;
                ps = null;
                MInOutLine receipt = new MInOutLine(getCtx(), M_InOutLine_ID, get_TrxName());
                return receipt;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "getMInOutLine", e);
        }
        try {
            //Si no se alcanzo a cerrar el result set lo cierro
            if (rs != null) {
                rs.close();
                rs = null;
            }
            //Si no se alcanzo a cerrar el prepared statement lo cierro
            if (ps != null) {
                ps.close();
                ps = null;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "getMInOutLine", e);
        }
        return null;
    }

    //**************************************************************
    //METODOS ESPECIFICOS PARA EL CALCULO DEL COSTO REAL DE RECURSOS
    //**************************************************************

    /* Bision - 15/07/2008 - Santiago Iba�ez
     * Metodo realizado para calcular el costo real de recursos
     * */
    private BigDecimal getResourceCost(int M_CostElement_ID, int AD_Org_ID, int M_Product_ID, int M_CostType_ID, int C_AcctSchema_ID, boolean generarDetalleCosto) {
        System.out.println("Calculando Costo Real de Recursos...");
        //int mpc_order_workflow=getMPC_OrderWorkflow(this.getMPC_Order_ID());
        BigDecimal cost = Env.ZERO;
        BigDecimal time = Env.ZERO;
        //obtengo los controles de actividad para obtener los nodos.
        MMPCCostCollector[] ra = getManufacturingOperationActivity(this.getMPC_Order_ID());
        for (int e = 0; e < ra.length; e++) {
            //obtengo el nodo para poder obtener el recurso
            MMPCOrderNode node = new MMPCOrderNode(getCtx(), ra[e].getMPC_Order_Node_ID(), get_TrxName());
            //obtengo el recurso asociado al nodo
            int S_Resource_ID = node.getS_Resource_ID();
            //Hay nodos que no tienen recursos asociados
            if (S_Resource_ID != 0) {
                System.out.println("Recurso: " + S_Resource_ID);
                //obtengo la suma de todos los MCost asociados al recurso pasandole el producto asociado al recurso
                int prodResource = getM_Product_ID(S_Resource_ID);
                BigDecimal rate = sumaCostos(M_CostElement_ID, prodResource, C_AcctSchema_ID);
                System.out.println("Suma de Costos Teoricos de Recursos: $" + rate);
                //obtengo la unidad de tiempo asociada al recurso
                MUOM uom = getResourceUOM(node.getS_Resource_ID());
                System.out.println("Unidad de Medida Teorica: " + uom.getName());
                //Obtengo la unidad de duraci�n del nodo
                String durationUnit = ra[e].getDurationUnit();
                System.out.println("Unidad de Medidad Real: " + durationUnit);
                BigDecimal time_resource_seconds = BigDecimal.ONE;
                BigDecimal timeActivity = BigDecimal.ONE;
                //Con la normalizacion se puede perder precision por eso solo normalizo cuando sea necesario
                //es decir cuando sean diferentes la unidad de medida del recurso y la unidad de duracion del RA
                if (!duracionesIguales(uom, durationUnit)) {
                    System.out.println("Unidades diferentes, normalizando tiempo...");
                    //normalizo el tiempo del recurso a segundos
                    time_resource_seconds = getSeconds(S_Resource_ID);
                    System.out.println("Unidad de Tiempo Teorico: " + time_resource_seconds + " segs");
                    //Normalizo el tiempo del RA a segundos [seg]
                    timeActivity = getSeconds(durationUnit, S_Resource_ID);
                    System.out.println("Unidad de Tiempo Real: " + timeActivity + " segs");
                }
                //Calculo el costo del recurso por segundos [$/nt]
                BigDecimal costo_resource = BigDecimal.ZERO;
                if (!time_resource_seconds.equals(BigDecimal.ZERO)) //no dividir por cero
                {
                    costo_resource = rate.divide(time_resource_seconds, 12, BigDecimal.ROUND_HALF_UP);
                }
                System.out.println("Costo del Recurso por unidad de tiempo: " + costo_resource);
                //Calculo la duraci�n de la actividad [t]
                timeActivity = timeActivity.multiply(ra[e].getDurationReal());
                //Acumulo (add) el costo del recurso en este nodo multiplicandole la duracion del nodo [$]
                BigDecimal cost2 = costo_resource.multiply(timeActivity).setScale(2, BigDecimal.ROUND_HALF_UP);
                cost = cost.add(cost2);
                System.out.println("Costo Real del Recurso: $" + cost);
                System.out.println("");
                //BISion - 06/01/2009 - Santiago Iba�ez
                //Modificacion realizada para insertar el detalle de costos
                //para que sean utilizados en los informes de explosion de costos.
                //Creo el detalle de costo de recursos
                if (generarDetalleCosto) {
                    actualizarDetalleCostoRecursos(ra[e].getMPC_Order_ID(), ra[e].getM_Product_ID(), ra[e].getMPC_Order_Node_ID(), ra[e].getDurationReal(), costo_resource, costo_resource.multiply(timeActivity), ra[e].getDurationUnit(), uom.getC_UOM_ID());
                }
                System.out.println("");
            }
        }//Costo Nodo = costoRecurso[$/t]*duracionNodo[t]
        System.out.println("Costo Real de Recursos: $" + cost);
        return cost;
    }

    /* Bision - 15/07/2008 - Santiago Iba�ez
     * Funci�n que dada una unidad de medida y un string comprueba si son equivalentes
     * es decir, el String "h" es equivalente a la UOM (ej tipo hora).
     * */
    private boolean duracionesIguales(MUOM uom, String time) {
        if (uom.isYear() && time.equals("Y")) {
            return true;
        }
        if (uom.isMonth() && time.equals("M")) {
            return true;
        }
        if (uom.isHour() && time.equals("h")) {
            return true;
        } else if (uom.isDay() && time.equals("D")) {
            return true;
        } else if (uom.isMinute() && time.equals("m")) {
            return true;
        }

        return false;
    }

    /* Bision - 15/07/2008 - Santiago Iba�ez
     * Funci�n que dado un recurso retorna la unidad de medida
     * */
    private MUOM getResourceUOM(int S_Resource_ID) {
        int C_UOM_ID = DB.getSQLValue(null, "SELECT C_UOM_ID FROM M_Product WHERE S_Resource_ID = ? ", S_Resource_ID);
        MUOM uom = new MUOM(getCtx(), C_UOM_ID, null);
        return uom;
    }

    /* Bision - 15/07/2008 - Santiago Iba�ez
     * Funcion que retorna todos los cost collector para una
     * determinada orden de manufactura
     * */
    public MMPCCostCollector[] getManufacturingOperationActivity(int MPC_Order_ID) {
        ArrayList<MMPCCostCollector> list = new ArrayList<MMPCCostCollector>();
        String filter = "MPC_Order_ID = " + MPC_Order_ID;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = DB.prepareStatement("SELECT MPC_Cost_Collector_ID from MPC_Cost_Collector where " + filter, get_TrxName());
            rs = ps.executeQuery();
            while (rs.next()) {
                int MPC_Cost_Collector_ID = rs.getInt(1);
                MMPCCostCollector cc = new MMPCCostCollector(getCtx(), MPC_Cost_Collector_ID, get_TrxName());
                //chequeo que el tipo de documento sea control de actividad, usando el doctype asociado
                MDocType doc = MDocType.get(getCtx(), cc.getC_DocType_ID());
                if (doc.getDocBaseType().equalsIgnoreCase(MDocType.DOCBASETYPE_ManufacturingOperationActivity)) {
                    list.add(cc);
                }
            }
            rs.close();
            rs = null;
            ps.close();
            ps = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getManufacturingOperationActivity", e);
        }
        try {
            //Si no se alcanzo a cerrar el result set lo cierro
            if (rs != null) {
                rs.close();
                rs = null;
            }
            //Si no se alcanzo a cerrar el prepared statement lo cierro
            if (ps != null) {
                ps.close();
                ps = null;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "getManufacturingOperationActivity", e);
        }
        MMPCCostCollector[] cost = new MMPCCostCollector[list.size()];
        return list.toArray(cost);
    }

    /**
     * Bision - 12/08/08 - Santiago Iba�ez
     * Funci�n que retorna la cantidad de horas por d�a que un recurso (dado
     * su tipo) est� operativo.
     * @param type: tipo de recurso
     * @return
     */
    private BigDecimal getHorasDisponibles(MResourceType type) {
        System.out.println("Calculando las horas disponibles...");
        if (type.isTimeSlot()) {
            //Obtengo la hora en la que se inicia el recurso
            Timestamp init = type.getTimeSlotStart();
            //Obtengo la hora en la que termina de usarse el recurso
            Timestamp end = type.getTimeSlotEnd();
            //Creo un calendario para calcular la diferencia de horas
            TimeZone est = TimeZone.getTimeZone("GM-3");
            Calendar inicio = Calendar.getInstance();
            Calendar fin = Calendar.getInstance();
            //seteo los milisegundos de ambos calendarios
            inicio.setTimeInMillis(init.getTime());
            fin.setTimeInMillis(end.getTime());
            //inicio.get(Calendar.HOUR_OF_DAY);
            //hora de inicio
            System.out.println("Hora de Inicio: " + init);
            BigDecimal i = new BigDecimal(inicio.get(Calendar.HOUR_OF_DAY));
            //hora de fin
            BigDecimal f = new BigDecimal(fin.get(Calendar.HOUR_OF_DAY));
            System.out.println("Hora de Fin: " + end);
            //calculo la diferencia de horas
            f = f.subtract(i);
            return f;
        }
        return new BigDecimal(24);
    }

    /**
     * Bision - 13/08/08 - Santiago Iba�ez
     * Funci�n que retorna la cantidad de d�as disponibles que est� el recurso
     * en N semanas.
     * @param type: tipo de recurso
     * @return
     */
    private BigDecimal getDiasDisponibles(MResourceType type, int semanas) {
        if (type.isDateSlot()) {
            int cant = 0;
            if (type.isOnMonday()) {
                cant++;
            }
            if (type.isOnTuesday()) {
                cant++;
            }
            if (type.isOnWednesday()) {
                cant++;
            }
            if (type.isOnThursday()) {
                cant++;
            }
            if (type.isOnFriday()) {
                cant++;
            }
            if (type.isOnSaturday()) {
                cant++;
            }
            if (type.isOnSunday()) {
                cant++;
            }
            cant = cant * semanas;
            return new BigDecimal(cant);
        }
        if (semanas == 4) {
            return new BigDecimal(30);
        } else {
            int dias = semanas * 7;
            return new BigDecimal(dias);
        }
    }

    /* Bision - 15/07/2008 - Santiago Iba�ez
     * Funci�n que retorna dado un recurso de tiempo su equivalente en segundos
     * */
    private BigDecimal getSeconds(int S_Resource_ID) {
        System.out.println("Obteniendo segundos del recurso...");
        MResource resource = new MResource(getCtx(), S_Resource_ID, get_TrxName());
        MUOM uom = getResourceUOM(S_Resource_ID);
        //si la unidad del recurso es D�a
        if (uom.isDay()) {
            System.out.println("Unidad en dias");
            //Bision 12/08/08 Santiago Iba�ez
            //Cuando es d�a, no se consideran 24hs sino las horas en las que
            //el recurso permanece activo.
            //obtengo el tipo del recurso
            MResourceType type = resource.getResourceType();
            BigDecimal horas = new BigDecimal(3600);
            BigDecimal i = getHorasDisponibles(type);
            System.out.println("Horas disponibles por día: " + i);
            return horas.multiply(i);
        } //si la unidad del recurso es Horas
        else if (uom.isHour()) {
            return new BigDecimal(3600);
        } //si la unidad del recurso es Minutos
        else if (uom.isMinute()) {
            return new BigDecimal(60);
        } //si la unidad del recurso es Mes
        else if (uom.isMonth()) {
            //obtengo el tipo de recurso
            MResourceType type = resource.getResourceType();
            //en una hora hay 3600 segundos
            BigDecimal segundosPorHora = new BigDecimal(3600);
            //obtengo la cantidad de d�as que est� disponible en el mes
            BigDecimal i = getDiasDisponibles(type, 4);
            //multiplico los d�as por las horas disponibles por d�a
            i = i.multiply(getHorasDisponibles(type));
            //normalizo a segundos
            i = i.multiply(segundosPorHora);
            return i;
        } //si la unidad del recurso es Semanas
        else if (uom.isWeek()) {
            //obtengo el tipo de recurso
            MResourceType type = resource.getResourceType();
            return getDiasDisponibles(type, 1).multiply(getHorasDisponibles(type)).multiply(new BigDecimal(3600));
        } //si la unidad del recurso es 'd�a de trabajo' = 8 hs
        else if (uom.isWorkDay()) {
            int segs = 8 * 3600;
            return new BigDecimal(segs);
        } //si la unidad del recurso es 'mes de trabajo' = 20 d�as
        else if (uom.isWorkMonth()) {
            int segs = 8 * 3600 * 20;
            return new BigDecimal(segs);
        } else if (uom.isYear()) {
            //obtengo el tipo de recurso
            MResourceType type = resource.getResourceType();
            //en una hora hay 3600 segundos
            BigDecimal segundosPorHora = new BigDecimal(3600);
            //obtengo la cantidad de d�as que est� disponible en el mes
            BigDecimal i = getDiasDisponibles(type, 4);
            //multiplico los d�as por las horas disponibles por d�a
            i = i.multiply(getHorasDisponibles(type));
            //normalizo a segundos
            i = i.multiply(segundosPorHora);
            i = i.multiply(new BigDecimal(12));
            return i;
        }
        return new BigDecimal(0);
    }

    /** Bision - 05/08/08 - Santiago Iba�ez
     * Funci�n que dado un String me retorna el equivalente en segundos
     * @param u : unidad de tiempo
     * @return
     */
    private BigDecimal getSeconds(String u, int S_Resource_ID) {
        //Obtengo el tipo del recurso
        MResource resource = new MResource(getCtx(), S_Resource_ID, get_TrxName());
        MUOM uom = getResourceUOM(S_Resource_ID);
        MResourceType type = resource.getResourceType();
        //Normalizo el tiempo del RA a segundos [seg]
        if (u.equals("h")) {
            return new BigDecimal(3600.0);
        } else if (u.equals("m")) {
            return new BigDecimal(60.0);
        } else if (u.equals("D")) {
            BigDecimal segundosPorHora = new BigDecimal(3600);
            return getHorasDisponibles(type).multiply(segundosPorHora);
        } else if (u.equals("Y")) {
            BigDecimal segundosPorHora = new BigDecimal(3600);
            BigDecimal meses = new BigDecimal(12);
            return meses.multiply(getDiasDisponibles(type, 4).multiply(getHorasDisponibles(type)).multiply(segundosPorHora));
        } else if (u.equals("M")) {
            BigDecimal horasPorSegundo = new BigDecimal(3600);
            return getDiasDisponibles(type, 4).multiply(getHorasDisponibles(type)).multiply(horasPorSegundo);
        }
        return new BigDecimal(1.0);
    }

    /* Bision - 15/07/2008 - Santiago Iba�ez
     * M�todo agregado para obtener el costo (con un elemento de costo dado)
     * de un producto dado.
     * */
    private BigDecimal sumaCostos(int M_CostElement_ID, int M_Product_ID, int C_AcctSchema_ID) {
        //Obtengo todos su MCost
        //org.eevolution.model.MCost[]  pc = org.eevolution.model.MCost.getElements(M_Product_ID , C_AcctSchema_ID , M_CostType_ID);
        org.eevolution.model.MCost[] pc = MCost.getElements(M_Product_ID, C_AcctSchema_ID);
        if (pc != null) {
            BigDecimal rate = Env.ZERO;
            for (int e = 0; e < pc.length; e++) {
                // Chequea que se corresponda con el Elemento de costo Ej: "Costo Estandar Recursos"
                if (pc[e].getM_CostElement_ID() == M_CostElement_ID) {
                    rate = rate.add(pc[e].getCurrentCostPrice());
                }
            }
            return rate;
        }
        return Env.ZERO;
    }

    /** Bision - 15/07/2008 - Santiago Iba�ez
     * Funci�n que retorna el ID del workflow asociado a la orden de manufactura (MPC_Order_Workflow_ID)
     * dada una orden de manufactura (MPC_Order_ID)
     *
    private int getMPC_OrderWorkflow(int mpc_order_id){
    QueryDB query = new QueryDB("org.compiere.model.X_MPC_Order_Workflow");
    String filter = "mpc_order_id= " + mpc_order_id;
    java.util.List results = query.execute(filter);
    Iterator select = results.iterator();
    while (select.hasNext())
    {
    X_MPC_Order_Workflow workflow =  (X_MPC_Order_Workflow) select.next();
    return workflow.getMPC_Order_Workflow_ID();
    }
    
    return 0;
    }*/

    /* Bision - 17/07/2008 - Santiago Iba�ez
     * Metodo que retorna la orden de manufactura para un producto y
     * partida dados.
     * */
    public MMPCOrder getMMPCOrderByProduct(int M_Product_ID, int M_AttributeSetInstance_ID) {
        System.out.println("getMMPCOrderByProduct");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String filter = "M_Product_ID = " + M_Product_ID + "AND M_AttributeSetInstance_ID = " + M_AttributeSetInstance_ID;
            ps = DB.prepareStatement("select mpc_order_id from mpc_order where " + filter, get_TrxName());
            rs = ps.executeQuery();
            if (rs.next()) {
                MMPCOrder order = new MMPCOrder(getCtx(), rs.getInt(1), get_TrxName());
                rs.close();
                rs = null;
                ps.close();
                ps = null;
                return order;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "getMMPCOrderByProduct", e);
        }
        try {
            //Si no se alcanzo a cerrar el result set lo cierro
            if (rs != null) {
                rs.close();
                rs = null;
            }
            //Si no se alcanzo a cerrar el prepared statement lo cierro
            if (ps != null) {
                ps.close();
                ps = null;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "getM_Product_ID", e);
        }
        return null;
    }

    /* Bision - 15/07/2008 - Santiago Iba�ez
     * M�todo creado para obtener el ID de un Producto dado un ID de Recurso
     * */
    private int getM_Product_ID(int S_Resource_ID) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String filter = "S_Resource_ID = " + S_Resource_ID;
            ps = DB.prepareStatement("select m_product_id from m_product where " + filter, get_TrxName());
            rs = ps.executeQuery();
            if (rs.next()) {
                int M_Product_ID = rs.getInt(1);
                rs.close();
                rs = null;
                ps.close();
                ps = null;
                return M_Product_ID;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "getM_Product_ID", e);
        }
        try {
            //Si no se alcanzo a cerrar el result set lo cierro
            if (rs != null) {
                rs.close();
                rs = null;
            }
            //Si no se alcanzo a cerrar el prepared statement lo cierro
            if (ps != null) {
                ps.close();
                ps = null;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "getM_Product_ID", e);
        }
        return 0;
    }

    /**************************************************************************
     * 	Complete Document
     * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
     */
    public String completeIt() {

        //	Just prepare
        if (DOCACTION_Prepare.equals(getDocAction())) {
            setProcessed(false);
            return DocAction.STATUS_InProgress;
        }



        //	Re-Check
                /* 
         * GENEOS - Pablo Velazquez
         * 09/10/2013
         * Se comenta esta linea porque si la orden se acaba de aprobar, entonces no entra el prepareIt
         */
        /*   if (!m_justPrepared)
        {*/
        String status = prepareIt();
        if (!DocAction.STATUS_InProgress.equals(status)) {
            return status;
        }
        //   }



        //	Implicit Approval
        if (!isApproved()) {
            approveIt();
        }
        log.info("completeIt - " + toString());
        StringBuffer info = new StringBuffer();

        // fjviejo e-evolution do not complete if there isnt qtyonhand
        if (!isAvailable()) {
            JOptionPane.showMessageDialog(null, "No hay cantidad disponible", "No Available", JOptionPane.INFORMATION_MESSAGE);
            return DocAction.STATUS_InProgress;
        }
        // fjviejo e-evolution end
                /*
        //	Create SO Shipment - Force Shipment
        MInOut shipment = null;
        if (MDocType.DOCSUBTYPESO_OnCreditOrder.equals(DocSubTypeSO)		//	(W)illCall(I)nvoice
        || MDocType.DOCSUBTYPESO_WarehouseOrder.equals(DocSubTypeSO)	//	(W)illCall(P)ickup
        || MDocType.DOCSUBTYPESO_POSOrder.equals(DocSubTypeSO))			//	(W)alkIn(R)eceipt
        {
        if (!DELIVERYRULE_Force.equals(getDeliveryRule()))
        setDeliveryRule(DELIVERYRULE_Force);
        //
        shipment = createShipment (dt);
        if (shipment == null)
        return DocAction.STATUS_Invalid;
        info.append("@M_InOut_ID@: ").append(shipment.getDocumentNo());
        String msg = shipment.getProcessMsg();
        if (msg != null && msg.length() > 0)
        info.append("(").append(msg).append(")");
        }	//	Shipment
        
        
        //	Create SO Invoice - Always invoice complete Order
        if ( MDocType.DOCSUBTYPESO_POSOrder.equals(DocSubTypeSO)
        || MDocType.DOCSUBTYPESO_OnCreditOrder.equals(DocSubTypeSO) )
        {
        MInvoice invoice = createInvoice (dt, shipment);
        if (invoice == null)
        return DocAction.STATUS_Invalid;
        info.append(" - @C_Invoice_ID@: ").append(invoice.getDocumentNo());
        String msg = invoice.getProcessMsg();
        if (msg != null && msg.length() > 0)
        info.append("(").append(msg).append(")");
        }	//	Invoice
        
        //	Counter Documents
        MPC_Order_Plan counter = createCounterDoc();
        if (counter != null)
        info.append(" - @CounterDoc@: @Order@=").append(counter.getDocumentNo());
         */
        //

        /** comentado por Bision**/
        //generateMPC_Cost_Order();
                    /*int C_AcctSchema_ID = Env.getContextAsInt(getCtx(),"$C_AcctSchema_ID");
        log.info("AcctSchema_ID" + C_AcctSchema_ID);
        MAcctSchema C_AcctSchema = new MAcctSchema(getCtx(),C_AcctSchema_ID,get_TrxName());
        log.info("Cost_Group_ID" + C_AcctSchema.getM_CostType_ID());
        
        MCost[] cost =  MCost.getElements(getM_Product_ID(),C_AcctSchema_ID,C_AcctSchema.getM_CostType_ID());
        log.info("MCost" + cost.toString());
        
        if (cost != null)
        {
        log.info("Elements Total" + cost.length);
        
        for (int j = 0 ; j < cost.length ; j ++)
        {
        MMPCOrderCost MPC_Order_Cost = new  MMPCOrderCost (getCtx(), 0,"MPC_Order_Cost");
        MPC_Order_Cost.setMPC_Order_ID(getMPC_Order_ID());
        
        MPC_Order_Cost.setC_AcctSchema_ID(cost[j].getC_AcctSchema_ID());
        //                    MPC_Order_Cost.setCumulatedAmt(cost[j].getCumulatedAmt());
        //                    MPC_Order_Cost.setCumulatedQty(cost[j].getCumulatedQty());
        //                    MPC_Order_Cost.setCurrentCostPriceLL(cost[j].getCurrentCostPriceLL());
        //                    MPC_Order_Cost.setCurrentCostPrice(cost[j].getCurrentCostPrice());
        MPC_Order_Cost.setM_Product_ID(getM_Product_ID());
        //                    MPC_Order_Cost.setM_AttributeSetInstance_ID(cost[j].getM_AttributeSetInstance_ID());
        //                    MPC_Order_Cost.setM_CostElement_ID(cost[j].getM_CostElement_ID());
        MPC_Order_Cost.save(get_TrxName());
        }
        }
        
        
        MMPCOrderBOMLine[] lines = getLines(getMPC_Order_ID());
        log.info("MMPCOrderBOMLine[]" + lines.toString());
        
        for (int i = 0 ; i < lines.length ; i++ )
        {
        cost = MCost.getElements(lines[i].getM_Product_ID(), C_AcctSchema_ID , C_AcctSchema.getM_CostType_ID());
        log.info("Elements Total" + cost.length);
        if (cost != null)
        {
        
        for (int j = 0 ; j < cost.length ; j ++)
        {
        MMPCOrderCost MPC_Order_Cost = new  MMPCOrderCost (getCtx(), 0,"MPC_Order_Cost");
        MPC_Order_Cost.setMPC_Order_ID(getMPC_Order_ID());
        //                            MPC_Order_Cost.setS_Resource_ID(MPC_Product_Costing[j].getS_Resource_ID());
        MPC_Order_Cost.setC_AcctSchema_ID(cost[j].getC_AcctSchema_ID());
        //                            MPC_Order_Cost.setCumulatedAmt(cost[j].getCumulatedAmt());
        //                            MPC_Order_Cost.setCumulatedQty(cost[j].getCumulatedQty());
        //                            MPC_Order_Cost.setCurrentCostPriceLL(cost[j].getCurrentCostPriceLL());
        //                            MPC_Order_Cost.setCurrentCostPrice(cost[j].getCurrentCostPrice());
        MPC_Order_Cost.setM_Product_ID(getM_Product_ID());
        //                            MPC_Order_Cost.setM_AttributeSetInstance_ID(cost[j].getM_AttributeSetInstance_ID());
        //                            MPC_Order_Cost.setM_CostElement_ID(cost[j].getM_CostElement_ID());
        MPC_Order_Cost.save(get_TrxName());
        }
        }
        }*/
        String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
        if (valid != null) {
            if (info.length() > 0) {
                info.append(" - ");
            }
            info.append(valid);
            m_processMsg = info.toString();
            return DocAction.STATUS_Invalid;
        }

        setProcessed(true);
        m_processMsg = info.toString();

        setDocStatus(DOCSTATUS_Completed); // fjv e-evolution
        //
        setDocAction(DOCACTION_Close);
        // fjv e-evolution
        try {
            String sqlup = "UPDATE MPC_MRP set DocStatus='CO' where MPC_Order_ID=" + getMPC_Order_ID() + " and M_Product_ID=" + getM_Product_ID() + " and AD_Client_ID=" + getAD_Client_ID();
            int noLine = DB.executeUpdate(sqlup, get_TrxName());
        } catch (Exception ex) {
            return DocAction.STATUS_Invalid;
        }
        //fjv e-evolution
        return DocAction.STATUS_Completed;
    }	//	completeIt

    /**************************************************************************
     * 	Complete Document
     * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
     */
    public String uncompleteIt() {

        //	Just prepare
        if (DOCACTION_Prepare.equals(getDocAction())) {
            setProcessed(false);
            return DocAction.STATUS_InProgress;
        }


        MMPCOrderBOMLine[] lines = getLines(true, "M_Product_ID");

        //Actualizo reservados            
        if (!UnreserveStock(lines)) {
            log.severe("Cannot unreserve ordered Stock (unComplete)");
            return DOCSTATUS_Invalid;
        }

        //Actualizo ordenados                
        if (!unorderStock()) {
            log.severe("Cannot update ordered Stock (unComplete)");
            return DOCSTATUS_Invalid;
        }

        StringBuffer info = new StringBuffer();
        String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
        if (valid != null) {
            if (info.length() > 0) {
                info.append(" - ");
            }
            info.append(valid);
            m_processMsg = info.toString();
            return DocAction.STATUS_Invalid;
        }

        setProcessed(true);
        setIsApproved(false);
        //La dejo como aprovada                            
        setDocStatus(prepareItfromaprove()); // fjv e-evolution
        //
        setDocAction(DOCACTION_Complete);
        // fjv e-evolution
        try {
            String sqlup = "UPDATE MPC_MRP set DocStatus='AP' where MPC_Order_ID=" + getMPC_Order_ID() + " and M_Product_ID=" + getM_Product_ID() + " and AD_Client_ID=" + getAD_Client_ID();
            int noLine = DB.executeUpdate(sqlup, get_TrxName());
        } catch (Exception ex) {
            return DocAction.STATUS_Invalid;
        }
        //fjv e-evolution
        return getDocStatus();
    }	//	uncompleteIt

    public boolean isUnavailable() {
        /*
         * GENEOS - Pablo Velazquez
         * 01/10/2013
         * Consulta datos a la inversa que el metodo isAvailable, insertando todas las partidas que no son 
         * tenidas en cuenta para le emision (porque vencen antes o sobre la fecha de emision) 
         * de una OM en una tabla temporal (T_MPC_ORDER_UNAVAILABLE), que luego es utilizada por un 
         * informe (NOMRE INFORME) que muestra los datos
         */
        String sql = " SELECT obl.mpc_order_id,obl.mpc_order_bomline_id,str.m_locator_id,obl.m_product_id,str.m_attributesetinstance_id,str.qtyonhand FROM m_storage str"
                + " JOIN m_attributesetinstance asi ON (str.m_attributesetinstance_id = asi.m_attributesetinstance_id and str.m_attributesetinstance_id <> 0)"
                + " RIGHT JOIN mpc_order_bomline obl ON (str.m_product_id = obl.m_product_id)"
                + " JOIN mpc_order mpco ON (mpco.mpc_order_id = obl.mpc_order_id) "
                + " WHERE str.m_locator_id IN (SELECT m_locator_id FROM m_locator loc"
                + "                       JOIN m_warehouse wh ON (loc.m_warehouse_id = wh.m_warehouse_id and wh.ISRELEASE = 'Y') )"
                + //Si la fecha es nula, la considero como vencida
                " AND ( asi.guaranteedate <= current_date or asi.guaranteedate is null )"
                + " AND str.qtyonhand - str.qtyreserved > 0"
                + " AND obl.mpc_order_id = ?";
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        boolean unavailable = false;
        try {
            String sqlDelete = " DELETE T_MPC_ORDER_UNAVAILABLE WHERE MPC_ORDER_ID=" + getMPC_Order_ID();
            PreparedStatement pstmtDelete = DB.prepareStatement(sqlDelete.toString(), get_TrxName());;
            pstmtDelete.executeUpdate();
            pstmtDelete.close();

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, getMPC_Order_ID());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                unavailable = true;
                String sqlInsert = " INSERT INTO T_MPC_ORDER_UNAVAILABLE VALUES ("
                        + rs.getInt(1) + ","
                        + rs.getInt(2) + ","
                        + rs.getInt(3) + ","
                        + rs.getInt(4) + ","
                        + rs.getInt(5) + ","
                        + rs.getBigDecimal(6)
                        + ")";
                PreparedStatement pstmtInsert = null;
                pstmtInsert = DB.prepareStatement(sqlInsert.toString(), get_TrxName());
                pstmtInsert.executeUpdate();
                pstmtInsert.close();
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Insert line in T_MPC_ORDER_UNAVAILABLE - " + sql, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }

            } catch (Exception e) {
            }
            return unavailable;
        }
    }

    public boolean isSoonExpires() {
        /*
         * GENEOS - Pablo Velazquez
         * 24/12/2013
         * Consulta datos como el metodo isAvailable, pero solo tiene en cuenta las partidas
         * que vencen entre hoy y hoy mas un mes insertando todas las partidas 
         * en una tabla temporal (T_MPC_ORDER_SOONEXPIRES), que luego es utilizada por un 
         * informe (NOMRE INFORME) que muestra los datos
         */
        String sql = " SELECT obl.mpc_order_id,obl.mpc_order_bomline_id,str.m_locator_id,obl.m_product_id,str.m_attributesetinstance_id,str.qtyonhand FROM m_storage str"
                + " JOIN m_attributesetinstance asi ON (str.m_attributesetinstance_id = asi.m_attributesetinstance_id and str.m_attributesetinstance_id <> 0)"
                + " RIGHT JOIN mpc_order_bomline obl ON (str.m_product_id = obl.m_product_id)"
                + " JOIN mpc_order mpco ON (mpco.mpc_order_id = obl.mpc_order_id) "
                + " WHERE str.m_locator_id IN (SELECT m_locator_id FROM m_locator loc"
                + "                       JOIN m_warehouse wh ON (loc.m_warehouse_id = wh.m_warehouse_id and wh.ISRELEASE = 'Y') )"
                + " AND asi.guaranteedate > current_date"
                + " AND asi.guaranteedate <= add_months(current_date,1)"
                + " AND str.qtyonhand - str.qtyreserved > 0"
                + " AND obl.mpc_order_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean unavailable = false;
        try {
            String sqlDelete = " DELETE T_MPC_ORDER_SOONEXPIRES WHERE MPC_ORDER_ID=" + getMPC_Order_ID();
            PreparedStatement pstmtDelete = DB.prepareStatement(sqlDelete.toString(), get_TrxName());;
            pstmtDelete.executeUpdate();
            pstmtDelete.close();

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, getMPC_Order_ID());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                unavailable = true;
                String sqlInsert = " INSERT INTO T_MPC_ORDER_SOONEXPIRES VALUES ("
                        + rs.getInt(1) + ","
                        + rs.getInt(2) + ","
                        + rs.getInt(3) + ","
                        + rs.getInt(4) + ","
                        + rs.getInt(5) + ","
                        + rs.getBigDecimal(6)
                        + ")";
                PreparedStatement pstmtInsert = null;
                pstmtInsert = DB.prepareStatement(sqlInsert.toString(), get_TrxName());
                pstmtInsert.executeUpdate();
                pstmtInsert.close();
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Insert line in T_MPC_ORDER_SOONEXPIRES - " + sql, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }

            } catch (Exception e) {
            }
            return unavailable;
        }
    }

    public boolean isAvailable() {
        /*
         * GENEOS - Pablo Velazquez
         * 01/10/2013
         * Se cambia metodo isAvailable para que tenga en cuenta todos los almacenes de emision (ISRELEASE = 'Y')
         * y que solo se tomen las partidas que no venzan antes de la fecha de la emision de la OM
         */
        boolean hasResults = false;
        boolean returnValue = true;
        String sql = " SELECT obl.m_product_id,sum(str.qtyonhand) onhand,sum(str.qtyreserved) reserved,obl.qtyrequiered FROM m_storage str"
                + //" JOIN m_attributesetinstance asi ON (str.m_attributesetinstance_id = asi.m_attributesetinstance_id and str.m_attributesetinstance_id <> 0)" +
                //Chequeo sobre partidas en cero por ajuste con version vieja (Se reservaban en partidas 0)
                " RIGHT JOIN m_attributesetinstance asi ON (str.m_attributesetinstance_id = asi.m_attributesetinstance_id)"
                + " RIGHT JOIN mpc_order_bomline obl ON (str.m_product_id = obl.m_product_id)"
                + " WHERE str.m_locator_id IN (SELECT loc.m_locator_id FROM m_locator loc"
                + "                          JOIN m_warehouse wh ON (loc.m_warehouse_id = wh.m_warehouse_id) "
                + "                             WHERE wh.ISRELEASE = 'Y')"
                + //Si la fecha es nula, la considero como vencida
                " AND ( asi.guaranteedate > current_date )"
                + " AND obl.mpc_order_id = ?"
                + " GROUP BY obl.m_product_id,obl.qtyrequiered ";
        PreparedStatement pstmt = null;

        try {
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, getMPC_Order_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                hasResults = true;
                StringBuffer sqlcrt = new StringBuffer("SELECT IsCritical FROM MPC_Order_BOMLine WHERE  MPC_Order_ID=? and M_Product_ID=" + rs.getInt(1));
                PreparedStatement pstmtcrt = null;
                pstmtcrt = DB.prepareStatement(sqlcrt.toString(), null);
                pstmtcrt.setInt(1, getMPC_Order_ID());
                ResultSet rscrt = pstmtcrt.executeQuery();
                boolean critical = false;
                while (rscrt.next()) {
                    if (rscrt.getString(1).equals("Y")) {
                        critical = true;
                    }
                }
                rscrt.close();
                pstmtcrt.close();

                if (critical) {
                    BigDecimal qtyOnHand = rs.getBigDecimal(2);
                    BigDecimal qtyReserved = rs.getBigDecimal(3);
                    BigDecimal qtyRequired = rs.getBigDecimal(4);

                    if (qtyOnHand != null && qtyReserved != null && qtyRequired != null) {
                        if (qtyOnHand.subtract(qtyReserved).compareTo(qtyRequired) < 0) {
                            rs.close();
                            pstmt.close();
                            pstmt = null;
                            returnValue = false;
                            return false;
                        }
                    } else {
                        rs.close();
                        pstmt.close();
                        pstmt = null;
                        returnValue = false;
                        return false;
                    }
                }
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "isAvailable - " + sql, e);
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            pstmt = null;
            return hasResults && returnValue;
        }
    }

    // fjviejo e-evolution complete mpcorder cuando checa si hay cantidad disponible que no lo separe por atributo begin
    public boolean isAvailable_old() {        // fjviejo e-evolution panalab
        //StringBuffer sql = new StringBuffer("SELECT sum(Qtyonhand), sum(qtyRequiered), M_Product_ID, M_Locator_ID FROM RV_MPC_Order_Storage WHERE  MPC_Order_ID=? group by M_Product_ID, M_Locator_ID");
        StringBuffer sql = new StringBuffer("SELECT sum(Qtyonhand), sum(qtyRequiered),sum(QtyAvailable), M_Product_ID, M_Locator_ID FROM RV_MPC_Order_Storage WHERE  MPC_Order_ID=? group by M_Product_ID, M_Locator_ID");
        // end
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, getMPC_Order_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // fjviejo e-evolution panalab
//                            if (rs.getBigDecimal(1)!=null && rs.getBigDecimal(2)!=null )
//                            {
//                                if (rs.getBigDecimal(1).compareTo(rs.getBigDecimal(2))<0)
//                                    return false;
//                                else
//                                    return true;
//                            }
//                            else
//                            {
//                                return false;
//                            }
                // end
                StringBuffer sqlcrt = new StringBuffer("SELECT IsCritical FROM MPC_Order_BOMLine WHERE  MPC_Order_ID=? and M_Product_ID=" + rs.getInt(4));
                PreparedStatement pstmtcrt = null;
                pstmtcrt = DB.prepareStatement(sqlcrt.toString(), null);
                pstmtcrt.setInt(1, getMPC_Order_ID());
                ResultSet rscrt = pstmtcrt.executeQuery();
                boolean critical = false;
                while (rscrt.next()) {
                    if (rscrt.getString(1).equals("Y")) {
                        critical = true;
                    }
                }
                rscrt.close();
                pstmtcrt.close();

                /*
                 ** Vit4B modificado para verificar por todos los productos - NAtes saltaba con el ptimero en true !!!
                 ** 08/02/2007
                 */

                if (critical) {
                    if (rs.getBigDecimal(1) != null && rs.getBigDecimal(2) != null && rs.getBigDecimal(3) != null) {
                        if (rs.getBigDecimal(3).compareTo(rs.getBigDecimal(2)) < 0) {
                            rs.close();
                            pstmt.close();
                            pstmt = null;
                            return false;
                        }
                    } else {
                        rs.close();
                        pstmt.close();
                        pstmt = null;
                        return false;
                    }
                }
            }
            rs.close();
            pstmt.close();
            pstmt = null;
            return true;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getLines - " + sql, e);
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            pstmt = null;
        }
    }
    // fjviejo e-evolution complete mpcorder cuando checa si hay cantidad disponible que no lo separe por atributo begin

    public boolean isAvailable_old2() {
        StringBuffer sql = new StringBuffer("SELECT * FROM RV_MPC_Order_Storage WHERE QtyOnHand - QtyRequiered < 0 AND MPC_Order_ID=? ");

        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
            pstmt.setInt(1, getMPC_Order_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                return false;
            }
            rs.close();
            pstmt.close();
            pstmt = null;
            return true;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getLines - " + sql, e);
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            pstmt = null;
        }
    }

    /**
     * 	Create Counter Document
     */
    /*
    private MPC_Order createCounterDoc()
    {
    //	Is this a counter doc ?
    if (getRef_Order_ID() != 0)
    return null;
    
    //	Org Must be linked to BPartner
    MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
    int counterC_BPartner_ID = org.getLinkedC_BPartner_ID();
    if (counterC_BPartner_ID == 0)
    return null;
    //	Business Partner needs to be linked to Org
    MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID());
    int counterAD_Org_ID = bp.getAD_OrgBP_ID_Int();
    if (counterAD_Org_ID == 0)
    return null;
    
    MBPartner counterBP = new MBPartner (getCtx(), counterC_BPartner_ID);
    MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID);
    log.info("createCounterDoc - Counter BP=" + counterBP.getName());
    
    //	Document Type
    int C_DocTypeTarget_ID = 0;
    MDocTypeCounter counterDT = MDocTypeCounter.getCounterDocType(getCtx(), getC_DocType_ID());
    if (counterDT != null)
    {
    C_DocTypeTarget_ID = counterDT.getCounter_C_DocType_ID();
    log.fine("createCounterDoc - " + counterDT);
    }
    else	//	indirect
    {
    C_DocTypeTarget_ID = MDocTypeCounter.getCounterDocType_ID(getCtx(), getC_DocType_ID());
    log.fine("createCounterDoc - Indirect C_DocTypeTarget_ID=" + C_DocTypeTarget_ID);
    }
    //	Deep Copy
    MOrder counter = copyFrom (this, getDateOrdered(),
    C_DocTypeTarget_ID, !isSOTrx(), true);
    //
    counter.setAD_Org_ID(counterAD_Org_ID);
    counter.setM_Warehouse_ID(counterOrgInfo.getM_Warehouse_ID());
    //
    counter.setBPartner(counterBP);
    //	Refernces (Should not be required
    counter.setSalesRep_ID(getSalesRep_ID());
    counter.save();
    
    //	Update copied lines
    MOrderLine[] counterLines = counter.getLines(true);
    for (int i = 0; i < counterLines.length; i++)
    {
    MOrderLine counterLine = counterLines[i];
    counterLine.setOrder(counter);	//	copies header values (BP, etc.)
    counterLine.setPrice();
    counterLine.setTax();
    counterLine.save();
    }
    log.fine("createCounterDoc = " + counter);
    
    //	Document Action
    if (counterDT != null)
    {
    if (counterDT.getDocAction() != null)
    {
    counter.setDocAction(counterDT.getDocAction());
    counter.processIt(counterDT.getDocAction());
    counter.save();
    }
    }
    return counter;
    }	//	createCounterDoc
     */
    /**
     * 	Post Document - nothing
     * 	@return true if success
     */
    public boolean postIt() {
        log.info("postIt - " + toString());
        return false;
    }	//	postIt

    /**
     * 	Void Document.
     * 	Set Qtys to 0 - Sales: reverse all documents
     * 	@return true if success
     */
    public boolean voidIt() {
        log.info("voidIt - " + toString());
        /*
        MOrderLine[] lines = getLines(false);
        for (int i = 0; i < lines.length; i++)
        {
        MOrderLine line = lines[i];
        BigDecimal old = line.getQtyOrdered();
        if (old.compareTo(Env.ZERO) != 0)
        {
        line.setQtyOrdered(Env.ZERO);
        line.setLineNetAmt(Env.ZERO);
        line.addDescription("Void (" + old + ")");
        line.save();
        }
        }
        //	Clear Reservations
        if (!reserveStock(null))
        {
        m_processMsg = "Cannot unreserve Stock (void)";
        return false;
        }
        
        if (!createReversals())
        return false;
         */
        /*
         * GENEOS - Pablo Velazquez
         * 30/09/2013
         * Al anular o eliminar una Orden de manufactura, se deben borrar sus referencias en
         * MMPCOrderParentOrder
         */
        MMPCOrderParentOrder[] punteros = MMPCOrderParentOrder.getAllForOrder(getCtx(), getMPC_Order_ID(), get_TrxName());
        if (punteros.length > 0) {
            int aux = JOptionPane.showConfirmDialog(null, "Si cancela la orden, se eliminaran los punteros de referencia de la misma generados por el MRP. ¿Seguro que desa continuar?", "Cancelar Orden de Manufactura", JOptionPane.ERROR_MESSAGE);

            if (aux != 0) {
                m_processMsg = "Void Canceled";
                return false;
            }

            if (!deleteMMPCOrderParentOrder()) {
                m_processMsg = "Cannot delete MPCOrder References";
                return false;
            }
        }

        //fjviejo e-evolution clear reserved
        if (!orderedStock()) {
            System.out.println("Cannot unreserve ordered Stock (void)");
            return false;
        } else {
            System.out.println("unreserve ordered Stock (void)");
        }

        MMPCOrderBOMLine[] lines = getLines(true, "M_Product_ID");
        if (!UnreserveStock(lines)) {
            System.out.println("Cannot unreserve ordered Stock (void)");
            return false;
        } else {
            System.out.println("unreserve ordered Stock (void)");
        }
        //fjviejo e-evolution clear reserved


        //No se borran, se dejan para tener un control adicional aunque se hayan cancelado.
        /*
        if ( !deleteReserveCtrl(lines) ){
        log.severe("Cannot delete reserve control(unComplete)");
        return false;
        }
         */

        /*
         * GENEOS - Pablo Velazquez
         * 29/10/2013
         * Al anular una orden, se agrega el texto ANULADO al final del analisis de las partidas ( y sub partidas
         * si tiene)
         */
        /*MAttributeSetInstance[] asis = getPartidas();
        for (int i=0 ; i<asis.length ; i++) {     
        asis[i].setLot(asis[i].getLot()+"-ANULADO");
        if ( !asis[i].save() )
        return false;
        }*/
        setProcessed(true);
        setDocAction(DOCACTION_None);
        return true;
    }	//	voidIt

    /**
     * 	Create Shipment/Invoice Reversals
     * 	@param true if success
     */
    /*
    private boolean createReversals()
    {
    //	Cancel only Sales
    if (!isSOTrx())
    return true;
    
    log.fine("createReversals");
    StringBuffer info = new StringBuffer();
    
    //	Reverse All Shipments
    info.append("@M_InOut_ID@:");
    MInOut[] shipments = getShipments();
    for (int i = 0; i < shipments.length; i++)
    {
    MInOut ship = shipments[i];
    //	if closed - ignore
    if (MInOut.DOCSTATUS_Closed.equals(ship.getDocStatus())
    || MInOut.DOCSTATUS_Reversed.equals(ship.getDocStatus())
    || MInOut.DOCSTATUS_Voided.equals(ship.getDocStatus()) )
    continue;
    
    //	If not completed - void - otherwise reverse it
    if (!MInOut.DOCSTATUS_Completed.equals(ship.getDocStatus()))
    {
    if (ship.voidIt())
    ship.setDocStatus(MInOut.DOCSTATUS_Voided);
    }
    else if (ship.reverseCorrectIt())	//	completed shipment
    {
    ship.setDocStatus(MInOut.DOCSTATUS_Reversed);
    info.append(" ").append(ship.getDocumentNo());
    }
    else
    {
    m_processMsg = "Could not reverse Shipment " + ship;
    return false;
    }
    ship.setDocAction(MInOut.DOCACTION_None);
    ship.save();
    }	//	for all shipments
    
    //	Reverse All Invoices
    info.append(" - @C_Invoice_ID@:");
    MInvoice[] invoices = getInvoices();
    for (int i = 0; i < invoices.length; i++)
    {
    MInvoice invoice = invoices[i];
    //	if closed - ignore
    if (MInvoice.DOCSTATUS_Closed.equals(invoice.getDocStatus())
    || MInvoice.DOCSTATUS_Reversed.equals(invoice.getDocStatus())
    || MInvoice.DOCSTATUS_Voided.equals(invoice.getDocStatus()) )
    continue;
    
    //	If not compleded - void - otherwise reverse it
    if (!MInvoice.DOCSTATUS_Completed.equals(invoice.getDocStatus()))
    {
    if (invoice.voidIt())
    invoice.setDocStatus(MInvoice.DOCSTATUS_Voided);
    }
    else if (invoice.reverseCorrectIt())	//	completed invoice
    {
    invoice.setDocStatus(MInvoice.DOCSTATUS_Reversed);
    info.append(" ").append(invoice.getDocumentNo());
    }
    else
    {
    m_processMsg = "Could not reverse Invoice " + invoice;
    return false;
    }
    invoice.setDocAction(MInvoice.DOCACTION_None);
    invoice.save();
    }	//	for all shipments
    
    m_processMsg = info.toString();
    return true;
    }	//	createReversals
     */
    /**
     * 	Close Document.
     * 	Cancel not delivered Qunatities
     * 	@return true if success
     */
    public boolean closeIt() {

        System.out.println("closeIt()");
        log.info(toString());

        //fjviejo e-evolution clear reserved
        //	Close Not delivered Qty - SO/PO
        MMPCOrderBOMLine[] lines = getLines(true, "M_Product_ID");

        /*
         * GENEOS - Pablo Velazquez
         * 30/20/2013
         * Se valida la tolerancia (Mayor y menor) segun la categoria del producto para cada linea
         */
        boolean toleranceAccomplishedMayor = true;
        boolean toleranceAccomplishedMenor = true;
        String productsMayores = "";
        String productsMenores = "";
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].getComponentType().equals(MMPCOrderBOMLine.COMPONENTTYPE_Phantom)) {
                continue;
            }
            MProduct producto = new MProduct(getCtx(), lines[i].getM_Product_ID(), get_TrxName());
            MZYNCATEGORYTOLERANCE pct = MZYNCATEGORYTOLERANCE.getZYNCategoryTolerance(getCtx(), producto.getM_Product_Category_ID(), get_TrxName());
            if (pct != null) {
                BigDecimal tolerance = pct.getTOLERANCE().divide(new BigDecimal(100));
                BigDecimal qtyRequired = lines[i].explodeQty(getQtyDelivered());
                BigDecimal maxQtyTolerance = qtyRequired.add(qtyRequired.multiply(tolerance));
                BigDecimal minQtyTolerance = qtyRequired.subtract(qtyRequired.multiply(tolerance));
                //Sobrepasa limite superior?
                if (lines[i].getQtyDelivered().compareTo(maxQtyTolerance) == 1) {
                    toleranceAccomplishedMayor = false;
                    productsMayores += producto.getValue() + " - " + producto.getName() + "(Esperada: " + qtyRequired + ", Entregado: " + lines[i].getQtyDelivered() + ", Maximo segun tolerancia " + pct.getTOLERANCE() + ":" + maxQtyTolerance + ")\n";
                }

                //No llega a cubrir limite inferior?
                if (lines[i].getQtyDelivered().compareTo(minQtyTolerance) == -1) {
                    toleranceAccomplishedMenor = false;
                    productsMenores += producto.getValue() + " - " + producto.getName() + "(Esperada:  " + qtyRequired + ", Entregado: " + lines[i].getQtyDelivered() + ", Minimo: segun tolerancia " + pct.getTOLERANCE() + ":" + minQtyTolerance + ")\n";
                }
            }
        }
        if (!toleranceAccomplishedMayor) {
            JOptionPane.showMessageDialog(null, "No se puede cerrar la orden de manufactura, el/los siguientes productos: \n" + productsMayores + " Sobrepasan el limite superior de tolerancia", "Limite de tolerancia superior superado", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!toleranceAccomplishedMenor) {
            JOptionPane.showMessageDialog(null, "No se puede cerrar la orden de manufactura, el/los siguientes productos: \n" + productsMenores + " No llegan a cubrir el limite inferior de tolerancia", "Limite de tolerancia inferior no alcanzado", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        //	Clear Reservations
        //fjviejo e-evolution clear reserved
        if (!UnreserveStock(lines)) {
            System.out.println("Cannot unreserve ordered Stock (close)");
            return false;
        } else {
            System.out.println("unreserve ordered Stock (close)");
        }
        //fjviejo e-evolution clear reserved
        setProcessed(true);
        setDocAction(DOCACTION_None);

        /* Bision - 23/07/2008 - Santiago Iba�ez
         * Se agrego la llamada al m�todo generateMPC_Cost_Order()
         * para modelar la funcionalidad de costos dentro del closeIt()
         */
        generateMPC_Cost_Order(true); //true para que genere detalle de costo

        setDateFinish(new Timestamp(System.currentTimeMillis()));

        /** BISion - 05/03/2009 - Santiago Ibañez
         * Modificacion realizada para actualizar la cantidad ordenada
         */
        if (!orderedStock()) {
            System.out.println("Cannot update ordered Stock (void)");
            return false;
        }
        return true;
    }	//	closeIt

    /**
     * 	Reverse Correction - same void
     * 	@return true if success
     */
    public boolean reverseCorrectIt() {
        log.info("reverseCorrectIt - " + toString());
        return voidIt();
    }	//	reverseCorrectionIt

    /**
     * 	Reverse Accrual - none
     * 	@return true if success
     */
    public boolean reverseAccrualIt() {
        log.info("reverseAccrualIt - " + toString());
        return false;
    }	//	reverseAccrualIt

    /**
     * 	Re-activate.
     * 	@return true if success
     */
    public boolean reActivateIt() {
        log.info("reActivateIt - " + toString());

        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        String DocSubTypeSO = dt.getDocSubTypeSO();

        //	PO - just re-open
        if (!isSOTrx()) {
            log.fine("reActivateIt - Existing documents not modified - " + dt);
        }
        //	Reverse Direct Documents
        if (MDocType.DOCSUBTYPESO_OnCreditOrder.equals(DocSubTypeSO) //	(W)illCall(I)nvoice
                || MDocType.DOCSUBTYPESO_WarehouseOrder.equals(DocSubTypeSO) //	(W)illCall(P)ickup
                || MDocType.DOCSUBTYPESO_POSOrder.equals(DocSubTypeSO)) //	(W)alkIn(R)eceipt
        {
            //if (!createReversals())
            return false;
        } else {
            log.fine("reActivateIt - Existing documents not modified - SubType=" + DocSubTypeSO);
        }

        setDocAction(DOCACTION_Complete);
        setProcessed(false);
        return true;
    }	//	reActivateIt

    /**
     * 	Get Invoices of Order
     * 	@param C_Order_ID id
     * 	@return invoices
     */
    public static MMPCOrderBOMLine[] getLines(int MPC_Order_ID, String trxName) {
        ArrayList list = new ArrayList();

        String sql = "SELECT * FROM MPC_Order_BOMLine WHERE MPC_Order_ID=? ";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, trxName);
            pstmt.setInt(1, MPC_Order_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MMPCOrderBOMLine(Env.getCtx(), rs, trxName));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            //log.log(Level.SEVERE ,("getLines", e);
            System.out.println("getLines" + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            pstmt = null;
        }
        //
        MMPCOrderBOMLine[] retValue = new MMPCOrderBOMLine[list.size()];
        list.toArray(retValue);
        return retValue;
    }	//	getLines

    /**
    
    
    
    /*************************************************************************
     * 	Get Summary
     *	@return Summary of Document
     */
    /*
    public String getSummary()
    {
    StringBuffer sb = new StringBuffer();
    sb.append(getDocumentNo());
    //	: Grand Total = 123.00 (#1)
    sb.append(": ").
    append(Msg.translate(getCtx(),"GrandTotal")).append("=").append(getGrandTotal())
    .append(" (#").append(getLines(true).length).append(")");
    //	 - Description
    if (getDescription() != null && getDescription().length() > 0)
    sb.append(" - ").append(getDescription());
    return sb.toString();
    }	//	getSummary
     */
    /**
     * 	Get Document Owner (Responsible)
     *	@return AD_User_ID
     */
    public int getDoc_User_ID() {
        return getPlanner_ID();
    }	//	getDoc_User_ID

    /**
     * 	Get Document Approval Amount
     *	@return amount
     */
    public java.math.BigDecimal getApprovalAmt() {
        //return getGrandTotal();
        return new BigDecimal(0);
    }	//	getApprovalAmt

    public int getC_Currency_ID() {
        return 0;
    }

    public String getProcessMsg() {
        return "";
    }

    public String getSummary() {
        return "";
    }

    /**
     * 	Create PDF
     *	@return File or null
     */
    public File createPDF() {
        try {
            File temp = File.createTempFile(get_TableName() + get_ID() + "_", ".pdf");
            return createPDF(temp);
        } catch (Exception e) {
            log.severe("Could not create PDF - " + e.getMessage());
        }
        return null;
    }	//	getPDF

    /**
     * 	Create PDF file
     *	@param file output file
     *	@return file if success
     */
    public File createPDF(File file) {
        ReportEngine re = ReportEngine.get(getCtx(), ReportEngine.ORDER, getMPC_Order_ID());
        if (re == null) {
            return null;
        }
        return re.getPDF(file);
    }	//	createPDF

    /**
     * 	Get Document Info
     *	@return document info (untranslated)
     */
    public String getDocumentInfo() {
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        return dt.getName() + " " + getDocumentNo();
    }	//	getDocumentInfo

    public boolean setBOMLineQtys(MMPCOrderBOMLine obl) {
        BigDecimal QtyOrdered = getQtyOrdered();
        /*MMPCOrderBOMLine[] obl = MMPCOrder.getLines(getMPC_Order_ID());
        for (int i = 0 ; i < obl.length ; i ++)
        {
        System.out.println("Product" + obl[i].getM_Product_ID());
        if (obl[i].isQtyPercentage())
        {
        BigDecimal qty = obl[i].getQtyBatch().multiply(QtyOrdered);
        if(obl[i].getComponentType().equals(obl[i].COMPONENTTYPE_Packing))
        obl[i].setQtyRequiered(qty.divide(new BigDecimal(100),0,qty.ROUND_UP));
        if (obl[i].getComponentType().equals(obl[i].COMPONENTTYPE_Component) || obl[i].getComponentType().equals(obl[i].COMPONENTTYPE_Phantom))
        obl[i].setQtyRequiered(qty.divide(new BigDecimal(100),4,qty.ROUND_UP));
        else if (obl[i].getComponentType().equals(obl[i].COMPONENTTYPE_Tools))
        obl[i].setQtyRequiered(obl[i].getQtyBOM());
        
        }
        else
        {
        if (obl[i].getComponentType().equals(obl[i].COMPONENTTYPE_Component) || obl[i].getComponentType().equals(obl[i].COMPONENTTYPE_Phantom))
        obl[i].setQtyRequiered(obl[i].getQtyBOM().multiply(QtyOrdered));
        if (obl[i].getComponentType().equals(obl[i].COMPONENTTYPE_Packing))
        obl[i].setQtyRequiered(obl[i].getQtyBOM().multiply(QtyOrdered));
        else if (obl[i].getComponentType().equals(obl[i].COMPONENTTYPE_Tools))
        obl[i].setQtyRequiered(obl[i].getQtyBOM());
        }
        
        // Set Scrap of Component
        BigDecimal Scrap = obl[i].getScrap();
        
        if (!Scrap.equals(Env.ZERO))
        {
        Scrap = Scrap.divide(new BigDecimal(100),4,BigDecimal.ROUND_UP);
        obl[i].setQtyRequiered(obl[i].getQtyRequiered().divide( Env.ONE.subtract(Scrap) , 4 ,BigDecimal.ROUND_HALF_UP ));
        }
        
        //obl[i].save(trxName);
        if (obl[i].getComponentType().equals(obl[i].COMPONENTTYPE_Phantom))
        {
        obl[i].setQtyRequiered(Env.ZERO);
        if(!obl[i].save(get_TrxName()))
        {
        throw new IllegalStateException("Could not Set Qty Line Manufacturing Order BOM");
        }
        
        }
        }*/

        if (obl.isQtyPercentage()) {
            BigDecimal qty = obl.getQtyBatch().multiply(QtyOrdered);
            if (obl.getComponentType().equals(obl.COMPONENTTYPE_Packing)) {
                obl.setQtyRequiered(qty.divide(new BigDecimal(100), 8, qty.ROUND_UP));
            }
            if (obl.getComponentType().equals(obl.COMPONENTTYPE_Component) || obl.getComponentType().equals(obl.COMPONENTTYPE_Phantom)) {
                obl.setQtyRequiered(qty.divide(new BigDecimal(100), 8, qty.ROUND_UP));
            } else if (obl.getComponentType().equals(obl.COMPONENTTYPE_Tools)) {
                obl.setQtyRequiered(obl.getQtyBOM());
            }

        } else {
            if (obl.getComponentType().equals(obl.COMPONENTTYPE_Component) || obl.getComponentType().equals(obl.COMPONENTTYPE_Phantom)) {
                obl.setQtyRequiered(obl.getQtyBOM().multiply(QtyOrdered));
            }
            if (obl.getComponentType().equals(obl.COMPONENTTYPE_Packing)) {
                obl.setQtyRequiered(obl.getQtyBOM().multiply(QtyOrdered));
            } else if (obl.getComponentType().equals(obl.COMPONENTTYPE_Tools)) {
                obl.setQtyRequiered(obl.getQtyBOM());
            }
        }

        // Set Scrap of Component
        BigDecimal Scrap = obl.getScrap();

        if (!Scrap.equals(Env.ZERO)) {
            Scrap = Scrap.divide(new BigDecimal(100), 8, BigDecimal.ROUND_UP);
            obl.setQtyRequiered(obl.getQtyRequiered().divide(Env.ONE.subtract(Scrap), 8, BigDecimal.ROUND_HALF_UP));
        }

        //obl[i].save(trxName);
        if (obl.getComponentType().equals(obl.COMPONENTTYPE_Phantom)) {
            obl.setQtyRequiered(Env.ZERO);
        }

        return true;
    }

    /*
     * GENEOS - Pablo Velazquez
     * 20/07/2013
     * Obtiene las partidas de una orden de manufactura, compuestas por:
     *  - Su partida
     *  - Sus subpartidas, obtenidas de MPC_COST_COLLECTOR.
     */
    public MAttributeSetInstance[] getPartidas() {
        MAttributeSetInstance[] returnArray = new MAttributeSetInstance[]{};
        ArrayList<MAttributeSetInstance> list = new ArrayList<MAttributeSetInstance>();
        if (getM_AttributeSetInstance_ID() != 0) {
            //list.add(new MAttributeSetInstance(getCtx(), getM_AttributeSetInstance_ID(),get_TrxName()));
            StringBuffer sql = new StringBuffer("SELECT DISTINCT(M_AttributeSetInstance_ID) FROM MPC_COST_COLLECTOR WHERE MPC_Order_ID=? AND MOVEMENTTYPE =? AND MPC_ORDER_BOMLINE_ID IS NULL ");
            PreparedStatement pstmt = null;
            try {
                pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
                pstmt.setInt(1, getMPC_Order_ID());
                pstmt.setString(2, MMPCCostCollector.MOVEMENTTYPE_ProductionPlus);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    list.add(new MAttributeSetInstance(getCtx(), rs.getInt(1), get_TrxName()));
                }
                rs.close();
                pstmt.close();
                pstmt = null;
            } catch (Exception e) {
                log.log(Level.SEVERE, "getPartidas - " + sql, e);
            }
        }
        returnArray = new MAttributeSetInstance[list.size()];
        return list.toArray(returnArray);
    }

    public boolean tieneSurtimientos() {
        MMPCOrderBOMLine lines[] = getLines(getMPC_Order_ID(), get_TrxName());
        for (int i = 0; i > lines.length; i++) {
            MMPCOrderBOMLine aLine = lines[i];
            if (aLine.getQtyDelivered().compareTo(BigDecimal.ZERO) > 0) {
                return true;
            }
        }
        return false;
    }

    public MProduct[] getBOMProducts() {
        MProduct[] returnArray = new MProduct[]{};
        ArrayList<MProduct> list = new ArrayList<MProduct>();
        MMPCOrderBOMLine lines[] = getLines(getMPC_Order_ID(), get_TrxName());
        for (int i = 0; i < lines.length; i++) {
            MMPCOrderBOMLine aLine = lines[i];
            MProduct aProduct = new MProduct(getCtx(), aLine.getM_Product_ID(), get_TrxName());
            if (aProduct.isBOM()) {
                list.add(aProduct);
            }
        }
        returnArray = new MProduct[list.size()];
        return list.toArray(returnArray);
    }

    public MProduct[] getGRANELProducts() {
        MProduct[] returnArray = new MProduct[]{};
        ArrayList<MProduct> list = new ArrayList<MProduct>();
        MMPCOrderBOMLine lines[] = getLines(getMPC_Order_ID(), get_TrxName());
        for (int i = 0; i < lines.length; i++) {
            MMPCOrderBOMLine aLine = lines[i];
            MProduct aProduct = new MProduct(getCtx(), aLine.getM_Product_ID(), get_TrxName());
            if (aProduct.isGranel()) {
                list.add(aProduct);
            }
        }
        returnArray = new MProduct[list.size()];
        return list.toArray(returnArray);
    }

    /**
     * 	Get MPC Cost Collectors for Order
     *	@param type 0:Entrega, 1:Surtimiento, 2:Devolucion
     *	@return file if success
     */
    public MMPCCostCollector[] getMMPCCostCollectors(int type) {
        MMPCCostCollector[] returnArray = new MMPCCostCollector[]{};
        ArrayList<MMPCCostCollector> list = new ArrayList<MMPCCostCollector>();
        String sql = "SELECT * FROM MPC_Cost_Collector WHERE MPC_Order_ID=? ";

        if (type == 0) {
            sql += "WHERE MOVEMENTTYPE = 'P+' ";
        }
        if (type == 1) {
            sql += "AND MOVEMENTTYPE = 'P-' AND MOVEMENTQTY >= 0";
        }
        if (type == 2) {
            sql += "AND MOVEMENTTYPE = 'P-' AND MOVEMENTQTY < 0";
        }

        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, get_TrxName());
            pstmt.setInt(1, this.getMPC_Order_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MMPCCostCollector(Env.getCtx(), rs, get_TrxName()));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            //log.log(Level.SEVERE ,("getLines", e);
            System.out.println("getMPCCostCollectors" + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            pstmt = null;
        }
        returnArray = new MMPCCostCollector[list.size()];
        return list.toArray(returnArray);
    }

    public boolean hasDelivered() {
        if (getQtyDelivered().signum() != 0) {
            return true;
        }
        return false;
    }

    public boolean hasReturns() {
        if (getMMPCCostCollectors(2).length > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteMMPCOrderParentOrder() {

        MMPCOrderParentOrder[] punteros = MMPCOrderParentOrder.getAllForOrder(getCtx(), getMPC_Order_ID(), get_TrxName());
        //Borro Punteros
        for (int i = 0; i < punteros.length; i++) {
            if (!punteros[i].delete(true)) {
                return false;
            }
        }
        return true;

    }
}	//	MOrder