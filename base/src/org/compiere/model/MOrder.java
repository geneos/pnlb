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
package org.compiere.model;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.JOptionPane;

import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 *  Order Model.
 * 	Please do not set DocStatus and C_DocType_ID directly. 
 * 	They are set in the process() method. 
 * 	Use DocAction and C_DocTypeTarget_ID instead.
 *
 *  @author Jorg Janke
 *  @version $Id: MOrder.java,v 1.103 2006/01/04 05:39:20 jjanke Exp $
 */
public class MOrder extends X_C_Order implements DocAction {

    private static int MAXLines = 8;

    /**
     * 	Create new Order by copying
     * 	@param from order
     * 	@param dateDoc date of the document date
     * 	@param C_DocTypeTarget_ID target document type
     * 	@param isSOTrx sales order 
     * 	@param counter create counter links
     *	@param copyASI copy line attributes Attribute Set Instance, Resaouce Assignment
     * 	@param trxName trx
     *	@return Order
     */
    public static MOrder copyFrom(MOrder from, Timestamp dateDoc,
            int C_DocTypeTarget_ID, boolean isSOTrx, boolean counter, boolean copyASI,
            String trxName) {
        MOrder to = new MOrder(from.getCtx(), 0, trxName);
        to.set_TrxName(trxName);
        PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
        to.set_ValueNoCheck("C_Order_ID", I_ZERO);
        to.set_ValueNoCheck("DocumentNo", null);
        //
        to.setDocStatus(DOCSTATUS_Drafted);		//	Draft
        to.setDocAction(DOCACTION_Complete);
        //
        to.setC_DocType_ID(0);
        to.setC_DocTypeTarget_ID(C_DocTypeTarget_ID);
        to.setIsSOTrx(isSOTrx);
        //
        to.setIsSelected(false);
        to.setDateOrdered(dateDoc);
        to.setDateAcct(dateDoc);
        to.setDatePromised(dateDoc);	//	assumption
        to.setDatePrinted(null);
        to.setIsPrinted(false);
        //
        to.setIsApproved(false);
        to.setIsCreditApproved(false);
        to.setC_Payment_ID(0);
        to.setC_CashLine_ID(0);
        //	Amounts are updated  when adding lines
        to.setGrandTotal(Env.ZERO);
        to.setTotalLines(Env.ZERO);
        //
        to.setIsDelivered(false);
        to.setIsInvoiced(false);
        to.setIsSelfService(false);
        to.setIsTransferred(false);
        to.setPosted(false);
        to.setProcessed(false);
        if (counter) {
            to.setRef_Order_ID(from.getC_Order_ID());
        } else {
            to.setRef_Order_ID(0);
        }
        //
        if (!to.save(trxName)) {
            throw new IllegalStateException("Could not create Order");
        }
        if (counter) {
            from.setRef_Order_ID(to.getC_Order_ID());
        }

        if (to.copyLinesFrom(from, counter, copyASI) == 0) {
            throw new IllegalStateException("Could not create Order Lines");
        }

        return to;
    }	//	copyFrom

    /** BISion - 30/09/2009 - Santiago Ibañez
     * @return the MAXLines
     */
    public static int getMAXLines() {
        return MAXLines;
    }

    /**************************************************************************
     *  Default Constructor
     *  @param ctx context
     *  @param  C_Order_ID    order to load, (0 create new order)
     *  @param trxName trx name
     */
    public MOrder(Properties ctx, int C_Order_ID, String trxName) {
        super(ctx, C_Order_ID, trxName);
        //  New
        if (C_Order_ID == 0) {
            setDocStatus(DOCSTATUS_Drafted);
            setDocAction(DOCACTION_Prepare);
            //
            setDeliveryRule(DELIVERYRULE_Availability);
            setFreightCostRule(FREIGHTCOSTRULE_FreightIncluded);
            setInvoiceRule(INVOICERULE_Immediate);

            // COMENTADO DANIEL GINI - REQ-051
            //setPaymentRule(PAYMENTRULE_OnCredit);

            setPriorityRule(PRIORITYRULE_Medium);
            setDeliveryViaRule(DELIVERYVIARULE_Pickup);
            //
            setIsDiscountPrinted(false);
            setIsSelected(false);
            setIsTaxIncluded(false);
            setIsSOTrx(true);
            setIsDropShip(false);
            setSendEMail(false);
            //
            setIsApproved(false);
            setIsPrinted(false);
            setIsCreditApproved(false);
            setIsDelivered(false);
            setIsInvoiced(false);
            setIsTransferred(false);
            setIsSelfService(false);
            //
            super.setProcessed(false);
            setProcessing(false);
            setPosted(false);

            setDateAcct(new Timestamp(System.currentTimeMillis()));
            setDatePromised(new Timestamp(System.currentTimeMillis()));
            setDateOrdered(new Timestamp(System.currentTimeMillis()));

            setFreightAmt(Env.ZERO);
            setChargeAmt(Env.ZERO);
            setTotalLines(Env.ZERO);
            setGrandTotal(Env.ZERO);
        }
    }	//	MOrder

    /**************************************************************************
     *  Project Constructor
     *  @param  project Project to create Order from
     * 	@param	DocSubTypeSO if SO DocType Target (default DocSubTypeSO_OnCredit)
     */
    public MOrder(MProject project, boolean IsSOTrx, String DocSubTypeSO) {
        this(project.getCtx(), 0, project.get_TrxName());
        setAD_Client_ID(project.getAD_Client_ID());
        setAD_Org_ID(project.getAD_Org_ID());
        setC_Campaign_ID(project.getC_Campaign_ID());
        setSalesRep_ID(project.getSalesRep_ID());
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
        setC_BPartner_ID(project.getC_BPartner_ID());
        setC_BPartner_Location_ID(project.getC_BPartner_Location_ID());
        setAD_User_ID(project.getAD_User_ID());
        //
        setM_Warehouse_ID(project.getM_Warehouse_ID());
        setM_PriceList_ID(project.getM_PriceList_ID());
        setC_PaymentTerm_ID(project.getC_PaymentTerm_ID());
        //
        setIsSOTrx(IsSOTrx);
        if (IsSOTrx) {
            if (DocSubTypeSO == null || DocSubTypeSO.length() == 0) {
                setC_DocTypeTarget_ID(DocSubTypeSO_OnCredit);
            } else {
                setC_DocTypeTarget_ID(DocSubTypeSO);
            }
        } else {
            setC_DocTypeTarget_ID();
        }
    }	//	MOrder

    /**
     *  Load Constructor
     *  @param ctx context
     *  @param rs result set record
     */
    public MOrder(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }	//	MOrder
    /**	Order Lines					*/
    private MOrderLine[] m_lines = null;
    /**	Tax Lines					*/
    private MOrderTax[] m_taxes = null;
    /** Force Creation of order		*/
    private boolean m_forceCreation = false;

    /**
     * 	Overwrite Client/Org if required
     * 	@param AD_Client_ID client
     * 	@param AD_Org_ID org
     */
    public void setClientOrg(int AD_Client_ID, int AD_Org_ID) {
        super.setClientOrg(AD_Client_ID, AD_Org_ID);
    }	//	setClientOrg

    /**
     * 	Add to Description
     *	@param description text
     */
    public void addDescription(String description) {
        String desc = getDescription();
        if (desc == null) {
            setDescription(description);
        } else {
            setDescription(desc + " | " + description);
        }
    }	//	addDescription

    /**
     * 	Set Business Partner (Ship+Bill)
     *	@param C_BPartner_ID bpartner
     */
    public void setC_BPartner_ID(int C_BPartner_ID) {
        super.setC_BPartner_ID(C_BPartner_ID);
        super.setBill_BPartner_ID(C_BPartner_ID);
    }	//	setC_BPartner_ID

    /**
     * 	Set Business Partner Location (Ship+Bill)
     *	@param C_BPartner_Location_ID bp location
     */
    public void setC_BPartner_Location_ID(int C_BPartner_Location_ID) {
        super.setC_BPartner_Location_ID(C_BPartner_Location_ID);
        super.setBill_Location_ID(C_BPartner_Location_ID);
    }	//	setC_BPartner_Location_ID

    /**
     * 	Set Business Partner Contact (Ship+Bill)
     *	@param AD_User_ID user
     */
    public void setAD_User_ID(int AD_User_ID) {
        super.setAD_User_ID(AD_User_ID);
        super.setBill_User_ID(AD_User_ID);
    }	//	setAD_User_ID

    /**
     * 	Set Ship Business Partner
     *	@param C_BPartner_ID bpartner
     */
    public void setShip_BPartner_ID(int C_BPartner_ID) {
        super.setC_BPartner_ID(C_BPartner_ID);
    }	//	setShip_BPartner_ID

    /**
     * 	Set Ship Business Partner Location
     *	@param C_BPartner_Location_ID bp location
     */
    public void setShip_Location_ID(int C_BPartner_Location_ID) {
        super.setC_BPartner_Location_ID(C_BPartner_Location_ID);
    }	//	setShip_Location_ID

    /**
     * 	Set Ship Business Partner Contact
     *	@param AD_User_ID user
     */
    public void setShip_User_ID(int AD_User_ID) {
        super.setAD_User_ID(AD_User_ID);
    }	//	setShip_User_ID

    /**
     * 	Set Warehouse
     *	@param M_Warehouse_ID warehouse
     */
    public void setM_Warehouse_ID(int M_Warehouse_ID) {
        super.setM_Warehouse_ID(M_Warehouse_ID);
    }	//	setM_Warehouse_ID

    /**
     * 	Set Drop Ship
     *	@param IsDropShip drop ship
     */
    public void setIsDropShip(boolean IsDropShip) {
        super.setIsDropShip(IsDropShip);
    }	//	setIsDropShip
    /*************************************************************************/
    public static final String DocSubTypeSO_Standard = "SO";
    public static final String DocSubTypeSO_Quotation = "OB";
    public static final String DocSubTypeSO_Proposal = "ON";
    public static final String DocSubTypeSO_Prepay = "PR";
    public static final String DocSubTypeSO_POS = "WR";
    public static final String DocSubTypeSO_Warehouse = "WP";
    public static final String DocSubTypeSO_OnCredit = "WI";
    public static final String DocSubTypeSO_RMA = "RM";

    /**
     * 	Set Target Sales Document Type
     * 	@param DocSubTypeSO_x SO sub type - see DocSubTypeSO_*
     */
    public void setC_DocTypeTarget_ID(String DocSubTypeSO_x) {
        String sql = "SELECT C_DocType_ID FROM C_DocType "
                + "WHERE AD_Client_ID=? AND AD_Org_ID IN (0," + getAD_Org_ID()
                + ") AND DocSubTypeSO=? "
                + "ORDER BY AD_Org_ID DESC, IsDefault DESC";
        int C_DocType_ID = DB.getSQLValue(null, sql, getAD_Client_ID(), DocSubTypeSO_x);
        if (C_DocType_ID <= 0) {
            log.severe("Not found for AD_Client_ID=" + getAD_Client_ID() + ", SubType=" + DocSubTypeSO_x);
        } else {
            log.fine("(SO) - " + DocSubTypeSO_x);
            setC_DocTypeTarget_ID(C_DocType_ID);
            setIsSOTrx(true);
        }
    }	//	setC_DocTypeTarget_ID

    /**
     * 	Set Target Document Type.
     * 	Standard Order or PO
     */
    public void setC_DocTypeTarget_ID() {
        if (isSOTrx()) //	SO = Std Order
        {
            setC_DocTypeTarget_ID(DocSubTypeSO_Standard);
            return;
        }
        //	PO
        String sql = "SELECT C_DocType_ID FROM C_DocType "
                + "WHERE AD_Client_ID=? AND AD_Org_ID IN (0," + getAD_Org_ID()
                + ") AND DocBaseType='POO' "
                + "ORDER BY AD_Org_ID DESC, IsDefault DESC";
        int C_DocType_ID = DB.getSQLValue(null, sql, getAD_Client_ID());
        if (C_DocType_ID <= 0) {
            log.severe("No POO found for AD_Client_ID=" + getAD_Client_ID());
        } else {
            log.fine("(PO) - " + C_DocType_ID);
            setC_DocTypeTarget_ID(C_DocType_ID);
        }
    }	//	setC_DocTypeTarget_ID

    /**
     * 	Set Business Partner Defaults & Details.
     * 	SOTrx should be set.
     * 	@param bp business partner
     */
    public void setBPartner(MBPartner bp) {
        /*
         * GENEOS - Pablo Velazquez
         * Modificación para que al cargar los datos de un Socio de Negocio
         * solo setee datos en caso de que sean nulos.
         */
        if (bp == null) {
            return;
        }

        setC_BPartner_ID(bp.getC_BPartner_ID());
        //	Defaults Payment Term
        int ii = 0;
        if (isSOTrx()) {
            ii = bp.getC_PaymentTerm_ID();
        } else {
            ii = bp.getPO_PaymentTerm_ID();
        }
        if (ii != 0 && getC_PaymentTerm_ID() == 0) {
            setC_PaymentTerm_ID(ii);
        }
        //	Default Price List
        if (isSOTrx()) {
            ii = bp.getM_PriceList_ID();
        } else {
            ii = bp.getPO_PriceList_ID();
        }
        if (ii != 0 && getM_PriceList_ID() == 0) {
            setM_PriceList_ID(ii);
        }
        //	Default Delivery/Via Rule
        String ss = bp.getDeliveryRule();
        if (ss != null && getDeliveryRule() == null) {
            setDeliveryRule(ss);
        }
        ss = bp.getDeliveryViaRule();
        if (ss != null && getDeliveryViaRule() == null) {
            setDeliveryViaRule(ss);
        }
        //	Default Invoice/Payment Rule
        ss = bp.getInvoiceRule();
        if (ss != null && getInvoiceRule() == null) {
            setInvoiceRule(ss);
        }

        // COMENTADO DANIEL GINI - REQ-051

        /*
        ss = bp.getPaymentRule();
        if (ss != null)
        setPaymentRule(ss);
         */


        /*
         *21-12-2010 Camarzana Mariano
         *Modificacion realizada para que se guarde bien el agente de compania 
         */

        //	Sales Rep
		/*ii = bp.getSalesRep_ID();
        if (ii != 0)
        setSalesRep_ID(ii);*/

        //Sales Rep
        ii = getSalesRep_ID();
        if (ii != 0) {
            setSalesRep_ID(ii);
        }


        //	Set Locations
        MBPartnerLocation[] locs = bp.getLocations(false);
        if (locs != null && getC_BPartner_Location_ID() == 0) {
            for (int i = 0; i < locs.length; i++) {
                if (locs[i].isShipTo()) {
                    super.setC_BPartner_Location_ID(locs[i].getC_BPartner_Location_ID());
                }
                if (locs[i].isBillTo()) {
                    setBill_Location_ID(locs[i].getC_BPartner_Location_ID());
                }
            }
            //	set to first
            if (getC_BPartner_Location_ID() == 0 && locs.length > 0) {
                super.setC_BPartner_Location_ID(locs[0].getC_BPartner_Location_ID());
            }
            if (getBill_Location_ID() == 0 && locs.length > 0) {
                setBill_Location_ID(locs[0].getC_BPartner_Location_ID());
            }
        }
        if (getC_BPartner_Location_ID() == 0) {
            log.log(Level.SEVERE, "MOrder.setBPartner - Has no Ship To Address: " + bp);
        }
        if (getBill_Location_ID() == 0) {
            log.log(Level.SEVERE, "MOrder.setBPartner - Has no Bill To Address: " + bp);
        }

        //	Set Contact
        MUser[] contacts = bp.getContacts(false);
        if (contacts != null && contacts.length == 1) {
            setAD_User_ID(contacts[0].getAD_User_ID());
        }
    }	//	setBPartner

    /**
     * 	Copy Lines From other Order
     *	@param otherOrder order
     *	@param counter set counter info
     *	@param copyASI copy line attributes Attribute Set Instance, Resaouce Assignment
     *	@return number of lines copied
     */
    public int copyLinesFrom(MOrder otherOrder, boolean counter, boolean copyASI) {
        if (isProcessed() || isPosted() || otherOrder == null) {
            return 0;
        }
        MOrderLine[] fromLines = otherOrder.getLines(false, null);
        int count = 0;
        for (int i = 0; i < fromLines.length; i++) {
            MOrderLine line = new MOrderLine(this);
            PO.copyValues(fromLines[i], line, getAD_Client_ID(), getAD_Org_ID());
            line.setC_Order_ID(getC_Order_ID());
            line.setOrder(this);
            //begin e-evolution vpj-cd bigfix  @version $Id: MOrder.java,v 1.105 2006/01/28 01:28:28 jjanke Exp $
            line.set_ValueNoCheck("C_OrderLine_ID", I_ZERO);	//	new
            //line.setC_OrderLine_ID(0);	//	new
            //end e-evolution vpj-cd bigfix  @version $Id: MOrder.java,v 1.105 2006/01/28 01:28:28 jjanke Exp $
            //	References
            if (!copyASI) {
                line.setM_AttributeSetInstance_ID(0);
                line.setS_ResourceAssignment_ID(0);
            }
            if (counter) {
                line.setRef_OrderLine_ID(fromLines[i].getC_OrderLine_ID());
            } else {
                line.setRef_OrderLine_ID(0);
            }
            //
            line.setQtyDelivered(Env.ZERO);
            line.setQtyInvoiced(Env.ZERO);
            line.setQtyReserved(Env.ZERO);
            line.setDateDelivered(null);
            line.setDateInvoiced(null);
            //	Tax
            if (getC_BPartner_ID() != otherOrder.getC_BPartner_ID()) {
                line.setTax();		//	recalculate
            }			//
            //
            line.setProcessed(false);
            if (line.save(get_TrxName())) {
                count++;
            }
            //	Cross Link
            if (counter) {
                fromLines[i].setRef_OrderLine_ID(line.getC_OrderLine_ID());
                fromLines[i].save(get_TrxName());
            }
        }
        if (fromLines.length != count) {
            log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
        }
        return count;
    }	//	copyLinesFrom

    /**************************************************************************
     * 	String Representation
     *	@return info
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("MOrder[").append(get_ID()).append("-").append(getDocumentNo()).append(",IsSOTrx=").append(isSOTrx()).append(",C_DocType_ID=").append(getC_DocType_ID()).append("]");
        return sb.toString();
    }	//	toString

    /**
     * 	Get Document Info
     *	@return document info (untranslated)
     */
    public String getDocumentInfo() {
        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        return dt.getName() + " " + getDocumentNo();
    }	//	getDocumentInfo

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
        ReportEngine re = ReportEngine.get(getCtx(), ReportEngine.ORDER, getC_Invoice_ID());
        if (re == null) {
            return null;
        }
        return re.getPDF(file);
    }	//	createPDF

    /**
     * 	Set Price List (and Currency, TaxIncluded) when valid
     * 	@param M_PriceList_ID price list
     */
    public void setM_PriceList_ID(int M_PriceList_ID) {
        MPriceList pl = MPriceList.get(getCtx(), M_PriceList_ID, null);
        if (pl.get_ID() == M_PriceList_ID) {
            super.setM_PriceList_ID(M_PriceList_ID);
            setC_Currency_ID(pl.getC_Currency_ID());
            setIsTaxIncluded(pl.isTaxIncluded());
        }
    }	//	setM_PriceList_ID

    /**************************************************************************
     * 	Get Lines of Order
     * 	@param whereClause where clause or null (starting with AND)
     * 	@return lines
     */
    public MOrderLine[] getLines(String whereClause, String orderClause) {
        ArrayList<MOrderLine> list = new ArrayList<MOrderLine>();
        StringBuffer sql = new StringBuffer("SELECT * FROM C_OrderLine WHERE C_Order_ID=? ");
        if (whereClause != null) {
            sql.append(whereClause);
        }
        if (orderClause != null) {
            sql.append(" ").append(orderClause);
        }
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
            pstmt.setInt(1, getC_Order_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MOrderLine ol = new MOrderLine(getCtx(), rs, get_TrxName());
                ol.setHeaderInfo(this);
                list.add(ol);
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, sql.toString(), e);
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
        MOrderLine[] lines = new MOrderLine[list.size()];
        list.toArray(lines);
        return lines;
    }	//	getLines

    /**
     * 	Get Lines of Order
     * 	@param requery requery
     * 	@param orderBy optional order by column
     * 	@return lines
     */
    public MOrderLine[] getLines(boolean requery, String orderBy) {
        if (m_lines != null && !requery) {
            return m_lines;
        }
        //
        String orderClause = "ORDER BY ";
        if (orderBy != null && orderBy.length() > 0) {
            orderClause += orderBy;
        } else {
            orderClause += "Line";
        }
        m_lines = getLines(null, orderClause);
        return m_lines;
    }	//	getLines

    /**
     * 	Get Lines of Order.
     * 	(useb by web store)
     * 	@return lines
     */
    public MOrderLine[] getLines() {
        return getLines(false, null);
    }	//	getLines

    /**
     * 	Renumber Lines
     *	@param step start and step
     */
    public void renumberLines(int step) {
        int number = step;
        MOrderLine[] lines = getLines(true, null);	//	Line is default
        for (int i = 0; i < lines.length; i++) {
            MOrderLine line = lines[i];
            line.setLine(number);
            line.save(get_TrxName());
            number += step;
        }
        m_lines = null;
    }	//	renumberLines

    /**
     * 	Does the Order Line belong to this Order
     *	@param C_OrderLine_ID line
     *	@return true if part of the order
     */
    public boolean isOrderLine(int C_OrderLine_ID) {
        if (m_lines == null) {
            getLines();
        }
        for (int i = 0; i < m_lines.length; i++) {
            if (m_lines[i].getC_OrderLine_ID() == C_OrderLine_ID) {
                return true;
            }
        }
        return false;
    }	//	isOrderLine

    /**
     * 	Get Taxes of Order
     *	@param requery requery
     *	@return array of taxes
     */
    public MOrderTax[] getTaxes(boolean requery) {
        if (m_taxes != null && !requery) {
            return m_taxes;
        }
        //
        ArrayList<MOrderTax> list = new ArrayList<MOrderTax>();
        String sql = "SELECT * FROM C_OrderTax WHERE C_Order_ID=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, get_TrxName());
            pstmt.setInt(1, getC_Order_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MOrderTax(getCtx(), rs, get_TrxName()));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getTaxes", e);
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
        m_taxes = new MOrderTax[list.size()];
        list.toArray(m_taxes);
        return m_taxes;
    }	//	getTaxes

    /**
     * 	Get Invoices of Order
     * 	@return invoices
     */
    public MInvoice[] getInvoices() {
        ArrayList<MInvoice> list = new ArrayList<MInvoice>();
        String sql = "SELECT * FROM C_Invoice WHERE DocStatus in ('CO','CL') and C_Order_ID=? ORDER BY Created DESC";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, get_TrxName());
            pstmt.setInt(1, getC_Order_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MInvoice(getCtx(), rs, get_TrxName()));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getInvoices", e);
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
        MInvoice[] retValue = new MInvoice[list.size()];
        list.toArray(retValue);
        return retValue;
    }	//	getInvoices

    /**
     * 	Get latest Invoice of Order
     * 	@return invoice id or 0
     */
    public int getC_Invoice_ID() {
        int C_Invoice_ID = 0;
        ArrayList list = new ArrayList();
        String sql = "SELECT C_Invoice_ID FROM C_Invoice "
                + "WHERE C_Order_ID=? AND DocStatus IN ('CO','CL') "
                + "ORDER BY Created DESC";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, get_TrxName());
            pstmt.setInt(1, getC_Order_ID());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                C_Invoice_ID = rs.getInt(1);
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getC_Invoice_ID", e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            pstmt = null;
        }
        return C_Invoice_ID;
    }	//	getC_Invoice_ID

    /**
     * 	Get Shipments of Order
     * 	@return shipments
     */
    public MInOut[] getShipments() {
        ArrayList<MInOut> list = new ArrayList<MInOut>();
        String sql = "SELECT * FROM M_InOut WHERE DocStatus in ('CO','CL') and C_Order_ID=? ORDER BY Created DESC";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, get_TrxName());
            pstmt.setInt(1, getC_Order_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MInOut(getCtx(), rs, get_TrxName()));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getShipments", e);
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
        MInOut[] retValue = new MInOut[list.size()];
        list.toArray(retValue);
        return retValue;
    }	//	getShipments

    /**
     *	Get ISO Code of Currency
     *	@return Currency ISO
     */
    public String getCurrencyISO() {
        return MCurrency.getISO_Code(getCtx(), getC_Currency_ID());
    }	//	getCurrencyISO

    /**
     * 	Get Currency Precision
     *	@return precision
     */
    public int getPrecision() {
        return MCurrency.getStdPrecision(getCtx(), getC_Currency_ID());
    }	//	getPrecision

    /**
     * 	Get Document Status
     *	@return Document Status Clear Text
     */
    public String getDocStatusName() {
        return MRefList.getListName(getCtx(), 131, getDocStatus());
    }	//	getDocStatusName

    /**
     * 	Set DocAction
     *	@param DocAction doc action
     */
    public void setDocAction(String DocAction) {
        setDocAction(DocAction, false);
    }	//	setDocAction

    /**
     * 	Set DocAction
     *	@param DocAction doc oction
     *	@param forceCreation force creation
     */
    public void setDocAction(String DocAction, boolean forceCreation) {
        super.setDocAction(DocAction);
        m_forceCreation = forceCreation;
    }	//	setDocAction

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
                + "' WHERE C_Order_ID=" + getC_Order_ID();
        int noLine = DB.executeUpdate("UPDATE C_OrderLine " + set, get_TrxName());
        int noTax = DB.executeUpdate("UPDATE C_OrderTax " + set, get_TrxName());
        m_lines = null;
        m_taxes = null;
        log.fine("setProcessed - " + processed + " - Lines=" + noLine + ", Tax=" + noTax);
    }	//	setProcessed

    /**************************************************************************
     * 	Before Save
     *	@param newRecord new
     *	@return save
     */
    protected boolean beforeSave(boolean newRecord) {
        /*
         *  25/10/2013 Maria Jesus Martin
         *  Verificacion de la existencia de la direccion de un socio de negocio.
         */
        MBPartnerLocation bPartnerLocation = new MBPartnerLocation(getCtx(), getC_BPartner_ID(), null);
        if (bPartnerLocation == null) {
            JOptionPane.showMessageDialog(null, "El Socio de Negocio no tiene Localización. Por favor agregue estos datos en la Ventana del Socio de Negocio.", "Error - Socio de Negocio", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //	Client/Org Check
        if (isSOTrx() && getLocation_Bill_ID() == 0) {
            m_processMsg = "Dirección Facturar A - Incompleto";
            JOptionPane.showMessageDialog(null, "Debe completar el campo Dirección Facturar A.", "Error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (isSOTrx() && getC_Location_ID() == 0) {
            m_processMsg = "Direcci�n Entregar A - Incompleto";
            JOptionPane.showMessageDialog(null, "Debe completar el campo Dirección Facturar A.", "Error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (getAD_Org_ID() == 0) {
            int context_AD_Org_ID = Env.getAD_Org_ID(getCtx());
            if (context_AD_Org_ID != 0) {
                setAD_Org_ID(context_AD_Org_ID);
                log.warning("Changed Org to Context=" + context_AD_Org_ID);
            }
        }
        if (getAD_Client_ID() == 0) {
            m_processMsg = "AD_Client_ID = 0";
            return false;
        }

        //	New Record Doc Type - make sure DocType set to 0
        if (newRecord && getC_DocTypeTarget_ID() != 0 && isSOTrx()) {
            setC_DocType_ID(getC_DocTypeTarget_ID());
        }

        //	Default Warehouse
        if (getM_Warehouse_ID() == 0) {
            int ii = Env.getContextAsInt(getCtx(), "#M_Warehouse_ID");
            if (ii != 0) {
                setM_Warehouse_ID(ii);
            } else {
                log.saveError("FillMandatory", Msg.getElement(getCtx(), "M_Warehouse_ID"));
                return false;
            }
        }
        //	Warehouse Org
        if (newRecord
                || is_ValueChanged("AD_Org_ID") || is_ValueChanged("M_Warehouse_ID")) {
            MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
            if (wh.getAD_Org_ID() != getAD_Org_ID()) {
                log.saveWarning("WarehouseOrgConflict", "");
            }
        }
        //	Reservations in Warehouse
        if (!newRecord && is_ValueChanged("M_Warehouse_ID")) {
            MOrderLine[] lines = getLines(false, null);
            for (int i = 0; i < lines.length; i++) {
                if (!lines[i].canChangeWarehouse()) {
                    return false;
                }
            }
        }

        //	No Partner Info - set Template
        if (getC_BPartner_ID() == 0) {
            setBPartner(MBPartner.getTemplate(getCtx(), getAD_Client_ID()));
        }
        if (getC_BPartner_Location_ID() == 0) {
            setBPartner(new MBPartner(getCtx(), getC_BPartner_ID(), null));
        }
        //	No Bill - get from Ship
        if (getBill_BPartner_ID() == 0) {
            setBill_BPartner_ID(getC_BPartner_ID());
            setBill_Location_ID(getC_BPartner_Location_ID());
        }
        if (getBill_Location_ID() == 0) {
            setBill_Location_ID(getC_BPartner_Location_ID());
        }

        //	Default Price List
        if (getM_PriceList_ID() == 0) {
            int ii = DB.getSQLValue(null,
                    "SELECT M_PriceList_ID FROM M_PriceList "
                    + "WHERE AD_Client_ID=? AND IsSOPriceList=? "
                    + "ORDER BY IsDefault DESC", getAD_Client_ID(), isSOTrx() ? "Y" : "N");
            if (ii != 0) {
                setM_PriceList_ID(ii);
            }
        }
        //	Default Currency
        if (getC_Currency_ID() == 0) {
            String sql = "SELECT C_Currency_ID FROM M_PriceList WHERE M_PriceList_ID=?";
            int ii = DB.getSQLValue(null, sql, getM_PriceList_ID());
            if (ii != 0) {
                setC_Currency_ID(ii);
            } else {
                setC_Currency_ID(Env.getContextAsInt(getCtx(), "#C_Currency_ID"));
            }
        }

        //	Default Sales Rep
        if (getSalesRep_ID() == 0) {
            int ii = Env.getContextAsInt(getCtx(), "#SalesRep_ID");
            if (ii != 0) {
                setSalesRep_ID(ii);
            }
        }

        //	Default Document Type
        if (getC_DocTypeTarget_ID() == 0) {
            setC_DocTypeTarget_ID(DocSubTypeSO_Standard);
        }

        //	Default Payment Term
        if (getC_PaymentTerm_ID() == 0) {
            int ii = Env.getContextAsInt(getCtx(), "#C_PaymentTerm_ID");
            if (ii != 0) {
                setC_PaymentTerm_ID(ii);
            } else {
                String sql = "SELECT C_PaymentTerm_ID FROM C_PaymentTerm WHERE AD_Client_ID=? AND IsDefault='Y'";
                ii = DB.getSQLValue(null, sql, getAD_Client_ID());
                if (ii != 0) {
                    setC_PaymentTerm_ID(ii);
                }
            }
        }


        return true;
    }	//	beforeSave

    /**
     * 	After Save
     *	@param newRecord new
     *	@param success success
     */
    protected boolean afterSave(boolean newRecord, boolean success) {
        if (!success || newRecord) {
            return success;
        }

        //	Propagate Description changes
        if (is_ValueChanged("Description") || is_ValueChanged("POReference")) {
            String sql = "UPDATE C_Invoice i"
                    + " SET (Description,POReference)="
                    + "(SELECT Description,POReference "
                    + "FROM C_Order o WHERE i.C_Order_ID=o.C_Order_ID) "
                    + "WHERE DocStatus NOT IN ('RE','CL') AND C_Order_ID=" + getC_Order_ID();
            int no = DB.executeUpdate(sql, get_TrxName());
            log.fine("Description -> #" + no);
        }

        //	Propagate Changes of Payment Info to existing (not reversed/closed) invoices
        if (is_ValueChanged("PaymentRule") || is_ValueChanged("C_PaymentTerm_ID")
                || is_ValueChanged("DateAcct") || is_ValueChanged("C_Payment_ID")
                || is_ValueChanged("C_CashLine_ID")) {
            String sql = "UPDATE C_Invoice i "
                    + "SET (PaymentRule,C_PaymentTerm_ID,DateAcct,C_Payment_ID,C_CashLine_ID)="
                    + "(SELECT PaymentRule,C_PaymentTerm_ID,DateAcct,C_Payment_ID,C_CashLine_ID "
                    + "FROM C_Order o WHERE i.C_Order_ID=o.C_Order_ID)"
                    + "WHERE DocStatus NOT IN ('RE','CL') AND C_Order_ID=" + getC_Order_ID();
            //	Don't touch Closed/Reversed entries
            int no = DB.executeUpdate(sql, get_TrxName());
            log.fine("Payment -> #" + no);
        }

        //	Sync Lines
        afterSaveSync("AD_Org_ID");
        afterSaveSync("C_BPartner_ID");
        afterSaveSync("C_BPartner_Location_ID");
        afterSaveSync("DateOrdered");
        afterSaveSync("DatePromised");
        afterSaveSync("M_Warehouse_ID");
        afterSaveSync("M_Shipper_ID");
        afterSaveSync("C_Currency_ID");
        //
        return true;
    }	//	afterSave

    private void afterSaveSync(String columnName) {
        if (is_ValueChanged(columnName)) {
            String sql = "UPDATE C_OrderLine ol"
                    + " SET " + columnName + " ="
                    + "(SELECT " + columnName
                    + " FROM C_Order o WHERE ol.C_Order_ID=o.C_Order_ID) "
                    + "WHERE C_Order_ID=" + getC_Order_ID();
            int no = DB.executeUpdate(sql, get_TrxName());
            log.fine(columnName + " Lines -> #" + no);
        }
    }	//	afterSaveSync

    /**
     * 	Before Delete
     *	@return true of it can be deleted
     */
    protected boolean beforeDelete() {
        if (isProcessed()) {
            return false;
        }

        getLines();
        for (int i = 0; i < m_lines.length; i++) {
            if (!m_lines[i].beforeDelete()) {
                return false;
            }
        }
        return true;
    }	//	beforeDelete

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
        log.info(toString());
        setDocAction(DOCACTION_Prepare);
        return true;
    }	//	invalidateIt

    /**
     * 28-07-2011 Camarzana Mariano
     * Metodo empleado para verificar que no exista un flujo de Movimiento (Transfer) activo, ya que se 
     * producian deadlocks con el transfer al generar los remitos correspondientes 
     * @return 
     */
    private int get() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT record_id FROM AD_WF_Activity WHERE AD_Table_ID=  " + MOrder.getTableId(MMovement.Table_Name)
                + " AND UPDATED like getdate() "
                + " AND Processed<>'Y' ORDER BY AD_WF_Activity_ID";
        try {
            pstmt = DB.prepareStatement(sql, null);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int registro = rs.getInt(1);
                rs.close();
                rs = null;
                pstmt.close();
                pstmt = null;
                return registro;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**************************************************************************
     *	Prepare Document
     * 	@return new status (In Progress or Invalid) 
     */
    public String prepareIt() {

        /*
         *28-07-2011 Camarzana Mariano
         *Verifico que no haya un flujo de movimiento activo 
         */
        int registro = get();
        if (registro > 0) {
            JOptionPane.showMessageDialog(null, "Existe un Flujo de Movimiento (Transfer) Activo Nro. Registro =  " + registro + ", Intente nuevamente", "ERROR ", JOptionPane.ERROR_MESSAGE);
            return DocAction.STATUS_Invalid;
        }


        log.info(toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
        if (m_processMsg != null) {
            return DocAction.STATUS_Invalid;
        }
        MDocType dt = MDocType.get(getCtx(), getC_DocTypeTarget_ID());

        //	Std Period open?
        if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType())) {
            m_processMsg = "@PeriodClosed@";
            return DocAction.STATUS_Invalid;
        }

        //	Lines
        MOrderLine[] lines = getLines(true, "M_Product_ID");
        if (lines.length == 0) {
            m_processMsg = "@NoLines@";
            return DocAction.STATUS_Invalid;
        }

        //	Convert DocType to Target
        if (getC_DocType_ID() != getC_DocTypeTarget_ID()) {
            //	Cannot change Std to anything else if different warehouses
            if (getC_DocType_ID() != 0) {
                MDocType dtOld = MDocType.get(getCtx(), getC_DocType_ID());
                if (MDocType.DOCSUBTYPESO_StandardOrder.equals(dtOld.getDocSubTypeSO()) //	From SO
                        && !MDocType.DOCSUBTYPESO_StandardOrder.equals(dt.getDocSubTypeSO())) //	To !SO
                {
                    for (int i = 0; i < lines.length; i++) {
                        if (lines[i].getM_Warehouse_ID() != getM_Warehouse_ID()) {
                            log.warning("different Warehouse " + lines[i]);
                            m_processMsg = "@CannotChangeDocType@";
                            return DocAction.STATUS_Invalid;
                        }
                    }
                }
            }

            //	New or in Progress/Invalid
            if (DOCSTATUS_Drafted.equals(getDocStatus())
                    || DOCSTATUS_InProgress.equals(getDocStatus())
                    || DOCSTATUS_Invalid.equals(getDocStatus())
                    || getC_DocType_ID() == 0) {
                setC_DocType_ID(getC_DocTypeTarget_ID());
            } else //	convert only if offer
            {
                if (dt.isOffer()) {
                    setC_DocType_ID(getC_DocTypeTarget_ID());
                } else {
                    m_processMsg = "@CannotChangeDocType@";
                    return DocAction.STATUS_Invalid;
                }
            }
        }	//	convert DocType

        //	Mandatory Product Attribute Set Instance
        String mandatoryType = "='Y'";	//	IN ('Y','S')
        String sql = "SELECT COUNT(*) "
                + "FROM C_OrderLine ol"
                + " INNER JOIN M_Product p ON (ol.M_Product_ID=p.M_Product_ID)"
                + " INNER JOIN M_AttributeSet pas ON (p.M_AttributeSet_ID=pas.M_AttributeSet_ID) "
                + "WHERE pas.MandatoryType" + mandatoryType
                + " AND ol.M_AttributeSetInstance_ID IS NULL"
                + " AND ol.C_Order_ID=?";
        int no = DB.getSQLValue(get_TrxName(), sql, getC_Order_ID());
        if (no != 0) {
            m_processMsg = "@LinesWithoutProductAttribute@ (" + no + ")";
            return DocAction.STATUS_Invalid;
        }

        //	Lines
        explodeBOM();
        if (!reserveStock(dt, lines)) {
            m_processMsg = "Cannot reserve Stock";
            return DocAction.STATUS_Invalid;
        }
        if (!calculateTaxTotal()) {
            m_processMsg = "Error calculating tax";
            return DocAction.STATUS_Invalid;
        }

        //	Credit Check
        if (isSOTrx()) {
            MBPartner bp = new MBPartner(getCtx(), getC_BPartner_ID(), null);
            if (MBPartner.SOCREDITSTATUS_CreditStop.equals(bp.getSOCreditStatus())) {
                m_processMsg = "@BPartnerCreditStop@ - @TotalOpenBalance@="
                        + bp.getTotalOpenBalance()
                        + ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
                return DocAction.STATUS_Invalid;
            }
            if (MBPartner.SOCREDITSTATUS_CreditHold.equals(bp.getSOCreditStatus())) {
                m_processMsg = "@BPartnerCreditHold@ - @TotalOpenBalance@="
                        + bp.getTotalOpenBalance()
                        + ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
                return DocAction.STATUS_Invalid;
            }
            BigDecimal grandTotal = MConversionRate.convertBase(getCtx(),
                    getGrandTotal(), getC_Currency_ID(), getDateOrdered(),
                    getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());
            if (MBPartner.SOCREDITSTATUS_CreditHold.equals(bp.getSOCreditStatus(grandTotal))) {
                m_processMsg = "@BPartnerOverOCreditHold@ - @TotalOpenBalance@="
                        + bp.getTotalOpenBalance() + ", @GrandTotal@=" + grandTotal
                        + ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
                return DocAction.STATUS_Invalid;
            }
        }

        m_justPrepared = true;
        //	if (!DOCACTION_Complete.equals(getDocAction()))		don't set for just prepare 
        //		setDocAction(DOCACTION_Complete);
        return DocAction.STATUS_InProgress;
    }	//	prepareIt

    /**
     * 	Explode non stocked BOM.
     */
    private void explodeBOM() {
        String where = "AND IsActive='Y' AND EXISTS "
                + "(SELECT * FROM M_Product p WHERE C_OrderLine.M_Product_ID=p.M_Product_ID"
                + " AND	p.IsBOM='Y' AND p.IsVerified='Y' AND p.IsStocked='N')";
        //
        String sql = "SELECT COUNT(*) FROM C_OrderLine "
                + "WHERE C_Order_ID=? " + where;
        int count = DB.getSQLValue(get_TrxName(), sql, getC_Order_ID());
        while (count != 0) {
            renumberLines(1000);		//	max 999 bom items	

            //	Order Lines with non-stocked BOMs
            MOrderLine[] lines = getLines(where, "ORDER BY Line");
            for (int i = 0; i < lines.length; i++) {
                MOrderLine line = lines[i];
                MProduct product = MProduct.get(getCtx(), line.getM_Product_ID());
                log.fine(product.getName());
                //	New Lines
                int lineNo = line.getLine();
                MProductBOM[] boms = MProductBOM.getBOMLines(product);
                for (int j = 0; j < boms.length; j++) {
                    MProductBOM bom = boms[j];
                    MOrderLine newLine = new MOrderLine(this);
                    newLine.setLine(++lineNo);
                    newLine.setM_Product_ID(bom.getProduct().getM_Product_ID());
                    newLine.setC_UOM_ID(bom.getProduct().getC_UOM_ID());
                    newLine.setQty(line.getQtyOrdered().multiply(
                            bom.getBOMQty()));
                    if (bom.getDescription() != null) {
                        newLine.setDescription(bom.getDescription());
                    }
                    //
                    newLine.setPrice();
                    newLine.save(get_TrxName());
                }
                //	Convert into Comment Line
                line.setM_Product_ID(0);
                line.setM_AttributeSetInstance_ID(0);
                line.setPrice(Env.ZERO);
                line.setPriceLimit(Env.ZERO);
                line.setPriceList(Env.ZERO);
                line.setLineNetAmt(Env.ZERO);
                line.setFreightAmt(Env.ZERO);
                //
                String description = product.getName();
                if (product.getDescription() != null) {
                    description += " " + product.getDescription();
                }
                if (line.getDescription() != null) {
                    description += " " + line.getDescription();
                }
                line.setDescription(description);
                line.save(get_TrxName());
            } //	for all lines with BOM

            m_lines = null;		//	force requery
            count = DB.getSQLValue(get_TrxName(), sql, getC_Invoice_ID());
            renumberLines(10);
        }	//	while count != 0
    }	//	explodeBOM

    /**
     * 	Reserve Inventory.
     * 	Counterpart: MInOut.completeIt()
     * 	@param dt document type or null
     * 	@param lines order lines (ordered by M_Product_ID for deadlock prevention)
     * 	@return true if (un) reserved
     */
    private boolean reserveStock(MDocType dt, MOrderLine[] lines) {
        if (dt == null) {
            dt = MDocType.get(getCtx(), getC_DocType_ID());
        }

        //	Binding
        boolean binding = !dt.isProposal();
        //	Not binding - i.e. Target=0
        if (DOCACTION_Void.equals(getDocAction())
                //	Closing Binding Quotation
                || (MDocType.DOCSUBTYPESO_Quotation.equals(dt.getDocSubTypeSO())
                && DOCACTION_Close.equals(getDocAction()))
                || isDropShip()) {
            binding = false;
        }
        boolean isSOTrx = isSOTrx();
        log.fine("Binding=" + binding + " - IsSOTrx=" + isSOTrx);
        //	Force same WH for all but SO/PO
        int header_M_Warehouse_ID = getM_Warehouse_ID();
        if (MDocType.DOCSUBTYPESO_StandardOrder.equals(dt.getDocSubTypeSO())
                || MDocType.DOCBASETYPE_PurchaseOrder.equals(dt.getDocBaseType())) {
            header_M_Warehouse_ID = 0;		//	don't enforce
        }
        //	Always check and (un) Reserve Inventory		
        for (int i = 0; i < lines.length; i++) {
            MOrderLine line = lines[i];
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
            BigDecimal target = binding ? line.getQtyOrdered() : Env.ZERO;
            BigDecimal difference = target.subtract(line.getQtyReserved()).subtract(line.getQtyDelivered());
            if (difference.signum() == 0) {
                continue;
            }

            log.fine("Line=" + line.getLine()
                    + " - Target=" + target + ",Difference=" + difference
                    + " - Ordered=" + line.getQtyOrdered()
                    + ",Reserved=" + line.getQtyReserved() + ",Delivered=" + line.getQtyDelivered());

            //	Check Product - Stocked and Item
            MProduct product = line.getProduct();
            if (product != null) {
                if (product.isStocked()) {
                    /*
                     * Solo reservo si es una Orden de Venta
                     */
                    BigDecimal reserved = isSOTrx ? difference : Env.ZERO;
                    //BigDecimal ordered = isSOTrx ? Env.ZERO : difference;

                    /** BISion - 10/03/2009 - Santiago Ibañez
                     * Modificacion realizada para actualizar correctamente el ordenado al cierre
                     */
                    BigDecimal ordered;
                    BigDecimal diffOrdenado = line.getQtyEntered().subtract(line.getQtyDelivered());
                    //Si se recibio mas de lo que se pidio
                    if (diffOrdenado.compareTo(Env.ZERO) < 0) {
                        ordered = Env.ZERO;
                    } else {
                        //Aca se dan dos situaciones
                        //1- completeIt() incrementa el ordenado
                        //2- closeIt(), voidIt() y reActivateIt() decrementa el ordenado
                        if (getDocAction().equals(DocAction.ACTION_Close) || getDocAction().equals(DocAction.ACTION_Void) || getDocAction().equals(DocAction.ACTION_ReActivate)) {
                            ordered = diffOrdenado.negate(); //negado para que decremente
                        } else {
                            ordered = diffOrdenado;
                        }
                    }
                    if (isSOTrx) {
                        ordered = Env.ZERO;
                    }
                    //fin modificacion BISion

                    int M_Locator_ID = 0;
                    //	Get Locator to reserve
                    if (line.getM_AttributeSetInstance_ID() != 0) //	Get existing Location
                    {
                        M_Locator_ID = MStorage.getM_Locator_ID(line.getM_Warehouse_ID(),
                                line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
                                ordered, get_TrxName());
                    }
                    //	Get default Location
                    if (M_Locator_ID == 0) {
                        MWarehouse wh = MWarehouse.get(getCtx(), line.getM_Warehouse_ID());
                        M_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
                    }
                    //	Update Storage                                    
                    if (!MStorage.addDist(getCtx(), line.getM_Warehouse_ID(), M_Locator_ID,
                            line.getM_Product_ID(),
                            line.getM_AttributeSetInstance_ID(), line.getM_AttributeSetInstance_ID(),
                            Env.ZERO, reserved, ordered, get_TrxName())) {
                        return false;
                    }
                }	//	stockec
                //	update line
                line.setQtyReserved(line.getQtyReserved().add(difference));
                /** BISion - 19/06/2009 - Santiago Ibañez
                 * Actualizo el ordenado decrementandolo o incrementandolo
                 * Si se cierra o cancela se pone en cero y si se completa se pone de acuerdo a la cantidad ingresada
                 */
                if (getDocAction().equals(DocAction.ACTION_Complete)) {
                    line.setQtyOrdered(line.getQtyOrdered().add(BigDecimal.ZERO));
                } else if (getDocAction().equals(DOCACTION_Close) || getDocAction().equals(DOCACTION_Void)) {
                    line.setQtyOrdered(BigDecimal.ZERO);
                }
                //fin modificacion BISion
                if (!line.save(get_TrxName())) {
                    return false;
                }
            }	//	product
        }	//	reverse inventory
        return true;
    }	//	reserveStock

    /**
     * 	Calculate Tax and Total
     */
    private boolean calculateTaxTotal() {
        log.fine("");
        //	Delete Taxes
        DB.executeUpdate("DELETE C_OrderTax WHERE C_Order_ID=" + getC_Order_ID(), get_TrxName());
        m_taxes = null;

        //	Lines
        BigDecimal totalLines = Env.ZERO;
        ArrayList<Integer> taxList = new ArrayList<Integer>();
        MOrderLine[] lines = getLines();
        for (int i = 0; i < lines.length; i++) {
            MOrderLine line = lines[i];
            Integer taxID = new Integer(line.getC_Tax_ID());
            if (!taxList.contains(taxID)) {
                MOrderTax oTax = MOrderTax.get(line, getPrecision(),
                        false, get_TrxName());	//	current Tax
                oTax.setIsTaxIncluded(isTaxIncluded());
                if (!oTax.calculateTaxFromLines()) {
                    return false;
                }
                if (!oTax.save(get_TrxName())) {
                    return false;
                }
                taxList.add(taxID);
            }
            totalLines = totalLines.add(line.getLineNetAmt());
        }

        //	Taxes
        BigDecimal grandTotal = totalLines;
        MOrderTax[] taxes = getTaxes(true);
        for (int i = 0; i < taxes.length; i++) {
            MOrderTax oTax = taxes[i];
            MTax tax = oTax.getTax();
            if (tax.isSummary()) {
                MTax[] cTaxes = tax.getChildTaxes(false);
                for (int j = 0; j < cTaxes.length; j++) {
                    MTax cTax = cTaxes[j];
                    BigDecimal taxAmt = cTax.calculateTax(oTax.getTaxBaseAmt(), isTaxIncluded(), getPrecision());
                    //
                    MOrderTax newOTax = new MOrderTax(getCtx(), 0, get_TrxName());
                    newOTax.setClientOrg(this);
                    newOTax.setC_Order_ID(getC_Order_ID());
                    newOTax.setC_Tax_ID(cTax.getC_Tax_ID());
                    newOTax.setPrecision(getPrecision());
                    newOTax.setIsTaxIncluded(isTaxIncluded());
                    newOTax.setTaxBaseAmt(oTax.getTaxBaseAmt());
                    newOTax.setTaxAmt(taxAmt);
                    if (!newOTax.save(get_TrxName())) {
                        return false;
                    }
                    //
                    if (!isTaxIncluded()) {
                        grandTotal = grandTotal.add(taxAmt);
                    }
                }
                if (!oTax.delete(true, get_TrxName())) {
                    return false;
                }
            } else {
                if (!isTaxIncluded()) {
                    grandTotal = grandTotal.add(oTax.getTaxAmt());
                }
            }
        }
        //
        setTotalLines(totalLines);
        setGrandTotal(grandTotal);
        return true;
    }	//	calculateTaxTotal

    /**
     * 	Approve Document
     * 	@return true if success 
     */
    public boolean approveIt() {
        log.info("approveIt - " + toString());
        setIsApproved(true);
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

    /**************************************************************************
     * 	Complete Document
     * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
     */
    public String completeIt() {

        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        MDocType typeNC = null;
        String DocSubTypeSO = dt.getDocSubTypeSO();
        MBPartner bp = new MBPartner(getCtx(), getC_BPartner_ID(), get_TrxName());

        //	Just prepare
        if (DOCACTION_Prepare.equals(getDocAction())) {
            setProcessed(false);
            return DocAction.STATUS_InProgress;
        }
        //	Offers
        if (MDocType.DOCSUBTYPESO_Proposal.equals(DocSubTypeSO)
                || MDocType.DOCSUBTYPESO_Quotation.equals(DocSubTypeSO)) {
            //	Binding
            if (MDocType.DOCSUBTYPESO_Quotation.equals(DocSubTypeSO)) {
                reserveStock(dt, getLines(true, "M_Product_ID"));
            }
            setProcessed(true);
            return DocAction.STATUS_Completed;
        }
        //	Waiting Payment - until we have a payment
        if (!m_forceCreation
                && MDocType.DOCSUBTYPESO_PrepayOrder.equals(DocSubTypeSO)
                && getC_Payment_ID() == 0 && getC_CashLine_ID() == 0) {

            setProcessed(true);
            return DocAction.STATUS_WaitingPayment;

        }

        //	Re-Check
        if (!m_justPrepared) {
            String status = prepareIt();
            if (!DocAction.STATUS_InProgress.equals(status)) {
                return status;
            }
        }
        //	Implicit Approval
        if (!isApproved()) {
            approveIt();
        }
        getLines(true, null);
        log.info(toString());
        StringBuffer info = new StringBuffer();

        boolean realTimePOS = false;

        //	Create SO Shipment - Force Shipment
        MInOut shipment = null;
        if (MDocType.DOCSUBTYPESO_OnCreditOrder.equals(DocSubTypeSO) //	(W)illCall(I)nvoice
                || MDocType.DOCSUBTYPESO_WarehouseOrder.equals(DocSubTypeSO) //	(W)illCall(P)ickup	
                || MDocType.DOCSUBTYPESO_POSOrder.equals(DocSubTypeSO) //	(W)alkIn(R)eceipt
                || MDocType.DOCSUBTYPESO_PrepayOrder.equals(DocSubTypeSO)) {
            shipment = createShipment(dt, realTimePOS ? null : getDateOrdered());

            if (shipment == null) {
                return DocAction.STATUS_Invalid;
            }
            info.append("@M_InOut_ID@: ").append(shipment.getDocumentNo());
            String msg = shipment.getProcessMsg();
            if (msg != null && msg.length() > 0) {
                info.append(" (").append(msg).append(")");
            }
        }	//	Shipment

        MInvoice invoiceF = null;

        //	Create SO Invoice - Always invoice complete Order
        if (MDocType.DOCSUBTYPESO_POSOrder.equals(DocSubTypeSO)
                || MDocType.DOCSUBTYPESO_OnCreditOrder.equals(DocSubTypeSO)
                || MDocType.DOCSUBTYPESO_PrepayOrder.equals(DocSubTypeSO)) {


            /*
             *      Vit4B - 20/12/2007
             *      Modificado para generar el tipo de factura en función de
             *      el tipo que tiene el socio del negocio.
             *
             */

            if (bp.getCondicionIVACode().equals(MBPartner.CIVA_RESPINSCRIPTO)) {
                //FC Factura A
                dt = MDocType.get(getCtx(), 1000131);
                //NCA Nota de Crédito por Bonificación Unidades A
                typeNC = MDocType.get(getCtx(), 1000203);

                /*
                 * 10-01-2011 Camarzana Mariano
                 * Invocacion del metodo createInvoice con el remito en null para que
                 * me cree la factura a partir de la orden y no del remito
                 */

                //invoiceF = createInvoice (dt, shipment, realTimePOS ? null : getDateOrdered());

                invoiceF = createInvoice(dt, shipment, realTimePOS ? null : getDateOrdered(), true);

            } else {
                //FC Factura B
                dt = MDocType.get(getCtx(), 1000204);
                //NCB Nota de Crédito por Bonificacion Unidades B
                typeNC = MDocType.get(getCtx(), 1000171);

                /*
                 * 10-01-2011 Camarzana Mariano
                 * Invocacion del metodo createInvoice con el remito en null para que
                 * me cree la factura a partir de la orden y no del remito
                 */
                //invoiceF = createInvoice (dt, shipment, realTimePOS ? null : getDateOrdered());

                invoiceF = createInvoice(dt, shipment, realTimePOS ? null : getDateOrdered(), true);

            }

            //MInvoice invoice = createInvoice (dt, shipment, realTimePOS ? null : getDateOrdered());

            if (invoiceF == null) {
                return DocAction.STATUS_Invalid;
            }
            info.append(" - @C_Invoice_ID@: ").append(invoiceF.getDocumentNo());
            String msg = invoiceF.getProcessMsg();
            if (msg != null && msg.length() > 0) {
                info.append(" (").append(msg).append(")");
            }
        }	//	Invoice

        /*
         **      Vit4B 30/03/2007
         **      Modificacion para genarar la nota de credito correspondiente en el caso de existir
         **      bonificación.
         **      Invoice con tipo de documento AR Credit Memo
         **      ---
         */

        if (this.isADVANTAGE()) {
            MInvoice invoiceNC = createInvoiceNC(typeNC, shipment, realTimePOS ? null : getDateOrdered());
            if (invoiceNC == null) {
                return DocAction.STATUS_Invalid;
            }
            info.append(" - @C_Invoice_ID@: ").append(invoiceNC.getDocumentNo());
            String msg = invoiceNC.getProcessMsg();
            if (msg != null && msg.length() > 0) {
                info.append(" (").append(msg).append(")");
            }

            // Cargar Comprobantes Asociados
            if (invoiceF != null) {
                invoiceF.setC_Invoice_Ref(invoiceNC.getC_Invoice_ID());
                invoiceNC.setC_Invoice_Ref(invoiceF.getC_Invoice_ID());
                invoiceF.save(get_TrxName());
                invoiceNC.save(get_TrxName());
            }
        }	//	Invoice


        //	Counter Documents
        MOrder counter = createCounterDoc();
        if (counter != null) {
            info.append(" - @CounterDoc@: @Order@=").append(counter.getDocumentNo());
        }
        //	User Validation
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


        /**
         * 15-02-2011 Camarzana mariano  
         * 	PROVEEDOR/CLIENTE:
         * 			La cotización es: - Uno, si se trabaja en moneda nacional.
         * 							  - Tasa del Sistema, si no se trabaja en moneda nacional.	
         */
        MAcctSchema acct = new MAcctSchema(getCtx(), Env.getContextAsInt(getCtx(), "$C_AcctSchema_ID"), get_TrxName());
        if (acct.getC_Currency_ID() == getC_Currency_ID()) {
            setCotizacion(BigDecimal.ONE);
        } else {
            setCotizacion(TasaCambio.rate(getC_Currency_ID(), acct.getC_Currency_ID(), getDateOrdered(), getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID()));
        }

        m_processMsg = info.toString().trim();
        setProcessed(true);


        m_processMsg = info.toString();
        //
        setDocAction(DOCACTION_Close);
        return DocAction.STATUS_Completed;
    }	//	completeIt

    /**
     * 	Create Shipment
     *	@param dt order document type
     *	@param movementDate optional movement date (default today)
     *	@return shipment or null
     */
    private MInOut createShipment(MDocType dt, Timestamp movementDate) {
        log.info("For " + dt);
//		MInOut shipment = new MInOut (this, dt.getC_DocTypeShipment_ID(), movementDate);
        MInOut shipment = new MInOut(this, dt.getC_DocTypeShipment_ID(), movementDate);
        //	shipment.setDateAcct(getDateAcct());

        MDocType doc = new MDocType(getCtx(), shipment.getC_DocType_ID(), get_TrxName());
        MSequence seq = new MSequence(getCtx(), doc.getDocNoSequence_ID(), get_TrxName());

        //shipment.setDocumentNo("<" + seq.getNextID()+ ">");
        shipment.setDocumentNo("<" + seq.getCurrentNext() + ">");
        shipment.setDeliveryRule(getDeliveryRule());

        if (!shipment.save(get_TrxName())) {
            m_processMsg = "Could not create Shipment";
            return null;
        }

        //seq.save(get_TrxName());

        //
        MOrderLine[] oLines = getLines(true, null);
        for (int i = 0; i < oLines.length; i++) {
            MOrderLine oLine = oLines[i];
            //
            MInOutLine ioLine = new MInOutLine(shipment);
            //	Qty = Ordered - Delivered
            BigDecimal MovementQty = oLine.getQtyOrdered().subtract(oLine.getQtyDelivered());
            //	Location
            int M_Locator_ID = MStorage.getM_Locator_ID(oLine.getM_Warehouse_ID(),
                    oLine.getM_Product_ID(), oLine.getM_AttributeSetInstance_ID(),
                    MovementQty, get_TrxName());
            if (M_Locator_ID == 0) //	Get default Location
            {
                MWarehouse wh = MWarehouse.get(getCtx(), oLine.getM_Warehouse_ID());
                M_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
            }
            //
            ioLine.setOrderLine(oLine, M_Locator_ID, MovementQty);
            ioLine.setQty(MovementQty);
            /**
             * BISion - 25/06/2009 - Santiago Ibañez
             * Modificacion realizada para que no haya diferencias entre la
             * cantidad ingresada y la del movimiento.
             * Se comento la linea a continuacion
             */
            /*if (oLine.getQtyEntered().compareTo(oLine.getQtyOrdered()) != 0)
            ioLine.setQtyEntered(MovementQty
            .multiply(oLine.getQtyEntered())
            .divide(oLine.getQtyOrdered(), BigDecimal.ROUND_HALF_UP));*/
            //fin modificacion BISion
            if (!ioLine.save(get_TrxName())) {
                m_processMsg = "Could not create Shipment Line";
                return null;
            }
        }
        //	Manually Process Shipment
        String status = shipment.completeIt();
        shipment.setDocStatus(status);
        shipment.save(get_TrxName());
        if (!DOCSTATUS_Completed.equals(status)) {
            m_processMsg = "@M_InOut_ID@: " + shipment.getProcessMsg();
            return null;
        }
        return shipment;
    }	//	createShipment


    /*
     **      Vit4B 30/03/2007
     **      Modificacion para genarar la nota de credito correspondiente en el caso de existir
     **      bonificaci�n.
     **      Invoice con tipo de documento AR Credit Memo
     **      ---
    
     * 	Create Invoice
     *	@param dt order document type
     *	@param shipment optional shipment
     *	@return invoice or null
     */
    private MInvoice createInvoiceNC(MDocType dt, MInOut shipment, Timestamp invoiceDate) {
        log.info(dt.toString());

        MDocType dtInvoice = MDocType.get(getCtx(), dt.getC_DocTypeInvoice_ID());

        MInvoice invoice = new MInvoice(this, dtInvoice.getC_DocType_ID(), invoiceDate);
        MSequence seq = new MSequence(getCtx(), dtInvoice.getDocNoSequence_ID(), get_TrxName());

        //invoice.setDocumentNo("<" + seq.getNextID() + ">");
        invoice.setDocumentNo("<" + seq.getCurrentNext() + ">");

        if (!invoice.save(get_TrxName())) {
            m_processMsg = "Could not create Invoice";
            return null;
        }

        //seq.save(get_TrxName());

        //	If we have a Shipment - use that as a base
        if (!INVOICERULE_Immediate.equals(getInvoiceRule())) {
            setInvoiceRule(INVOICERULE_Immediate);
        }
        //
        MOrderLine[] oLines = getLines();
        for (int i = 0; i < oLines.length; i++) {
            MOrderLine oLine = oLines[i];
            //
            MInvoiceLine iLine = new MInvoiceLine(invoice);
            iLine.setOrderLine(oLine);
            //	Qty = Ordered - Invoiced	
            iLine.setQtyInvoiced(oLine.getQTYADVANTAGE());
            iLine.setQtyEntered(oLine.getQTYADVANTAGE());
            iLine.setDiscount(oLine.getDiscount());
            if (!iLine.save(get_TrxName())) {
                m_processMsg = "Could not create Invoice Line from Order Line";
                return null;
            }
        }

        //  Comentado Vit4b

        //  Manually Process Invoice
        String status = invoice.completeIt();
        invoice.setDocStatus(status);
        invoice.save(get_TrxName());
        setC_CashLine_ID(invoice.getC_CashLine_ID());

        if (!DOCSTATUS_Completed.equals(status)) {
            m_processMsg = "@C_Invoice_ID@: " + invoice.getProcessMsg();
            return null;
        }

        return invoice;

    }	//	createInvoice

    /**
     * 	Create Invoice
     *	@param dt order document type
     *	@param shipment optional shipment
     *	@return invoice or null
     */
    /*
     * 02-03-2011 Camarzana Mariano
     * Se le agrego la variable crearDesdeOV para seleccionar si se desea crear desde una OV o desde Remito
     */
    private MInvoice createInvoice(MDocType dt, MInOut shipment, Timestamp invoiceDate, boolean crearDesdeOV) {
        log.info(dt.toString());

        MDocType dtInvoice = MDocType.get(getCtx(), dt.getC_DocTypeInvoice_ID());

        MInvoice invoice = new MInvoice(this, dtInvoice.getC_DocType_ID(), invoiceDate);
        MSequence seq = new MSequence(getCtx(), dtInvoice.getDocNoSequence_ID(), get_TrxName());

        //invoice.setDocumentNo("<" + seq.getNextID() + ">");
        invoice.setDocumentNo("<" + seq.getCurrentNext() + ">");

        if (getC_PaymentTerm_ID() != 0) {
            invoice.setC_PaymentTerm_ID(getC_PaymentTerm_ID());
        }
        if (getM_PriceList_ID() != 0) {
            invoice.setM_PriceList_ID(getM_PriceList_ID());
        }

        if (!invoice.save(get_TrxName())) {
            m_processMsg = "Could not create Invoice";
            return null;
        }

        //seq.save(get_TrxName());

        //	If we have a Shipment - use that as a base
        if (shipment != null && !crearDesdeOV) {
            if (!INVOICERULE_AfterDelivery.equals(getInvoiceRule())) {
                setInvoiceRule(INVOICERULE_AfterDelivery);
            }
            //
            MInOutLine[] sLines = shipment.getLines(false);
            for (int i = 0; i < sLines.length; i++) {
                MInOutLine sLine = sLines[i];
                //
                MInvoiceLine iLine = new MInvoiceLine(invoice);
                iLine.setShipLine(sLine);
                //	Qty = Delivered	
                iLine.setQtyEntered(sLine.getQtyEntered());
                iLine.setQtyInvoiced(sLine.getMovementQty());
                if (!iLine.save(get_TrxName())) {
                    m_processMsg = "Could not create Invoice Line from Shipment Line";
                    return null;
                }
                //
                sLine.setIsInvoiced(true);
                if (!sLine.save(get_TrxName())) {
                    log.warning("Could not update Shipment line: " + sLine);
                }
            }
        } else //	Create Invoice from Order
        if (shipment != null) {
            if (!INVOICERULE_Immediate.equals(getInvoiceRule())) {
                setInvoiceRule(INVOICERULE_Immediate);
            }
            //
            MOrderLine[] oLines = getLines();

            MInOutLine[] sLines = shipment.getLines(false);

            for (int i = 0; i < oLines.length; i++) {
                MOrderLine oLine = oLines[i];
                //
                MInvoiceLine iLine = new MInvoiceLine(invoice);
                iLine.setOrderLine(oLine);
                //	Qty = Ordered - Invoiced	
                iLine.setQtyInvoiced(oLine.getQtyOrdered().subtract(oLine.getQtyInvoiced()));
                if (oLine.getQtyOrdered().compareTo(oLine.getQtyEntered()) == 0) {
                    iLine.setQtyEntered(iLine.getQtyInvoiced());
                } else {
                    iLine.setQtyEntered(iLine.getQtyInvoiced().multiply(oLine.getQtyEntered()).divide(oLine.getQtyOrdered(), BigDecimal.ROUND_HALF_UP));
                }
                iLine.setDiscount(oLine.getDiscount());

                //02-03-2011 Camarzana Mariano
                //Se le agrego M_InOutLine_ID a la factura para que cree MatchInv
                iLine.setM_InOutLine_ID(sLines[i].getM_InOutLine_ID());
                if (!iLine.save(get_TrxName())) {
                    m_processMsg = "Could not create Invoice Line from Order Line";
                    return null;
                }

            }
        }
        //	Manually Process Invoice
        String status = invoice.completeIt();
        invoice.setDocStatus(status);
        invoice.save(get_TrxName());
        setC_CashLine_ID(invoice.getC_CashLine_ID());
        if (!DOCSTATUS_Completed.equals(status)) {
            m_processMsg = "@C_Invoice_ID@: " + invoice.getProcessMsg();
            return null;
        }
        return invoice;
    }	//	createInvoice

    /**
     * 	Create Counter Document
     */
    private MOrder createCounterDoc() {
        //	Is this itself a counter doc ?
        if (getRef_Order_ID() != 0) {
            return null;
        }

        //	Org Must be linked to BPartner
        MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
        int counterC_BPartner_ID = org.getLinkedC_BPartner_ID();
        if (counterC_BPartner_ID == 0) {
            return null;
        }
        //	Business Partner needs to be linked to Org
        MBPartner bp = new MBPartner(getCtx(), getC_BPartner_ID(), null);
        int counterAD_Org_ID = bp.getAD_OrgBP_ID_Int();
        if (counterAD_Org_ID == 0) {
            return null;
        }

        MBPartner counterBP = new MBPartner(getCtx(), counterC_BPartner_ID, null);
        MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID);
        log.info("Counter BP=" + counterBP.getName());

        //	Document Type
        int C_DocTypeTarget_ID = 0;
        MDocTypeCounter counterDT = MDocTypeCounter.getCounterDocType(getCtx(), getC_DocType_ID());
        if (counterDT != null) {
            log.fine(counterDT.toString());
            if (!counterDT.isCreateCounter() || !counterDT.isValid()) {
                return null;
            }
            C_DocTypeTarget_ID = counterDT.getCounter_C_DocType_ID();
        } else //	indirect
        {
            C_DocTypeTarget_ID = MDocTypeCounter.getCounterDocType_ID(getCtx(), getC_DocType_ID());
            log.fine("Indirect C_DocTypeTarget_ID=" + C_DocTypeTarget_ID);
            if (C_DocTypeTarget_ID <= 0) {
                return null;
            }
        }
        //	Deep Copy
        MOrder counter = copyFrom(this, getDateOrdered(),
                C_DocTypeTarget_ID, !isSOTrx(), true, false, get_TrxName());
        //
        counter.setAD_Org_ID(counterAD_Org_ID);
        counter.setM_Warehouse_ID(counterOrgInfo.getM_Warehouse_ID());
        //
        counter.setBPartner(counterBP);
        counter.setDatePromised(getDatePromised());		// default is date ordered 
        //	Refernces (Should not be required
        counter.setSalesRep_ID(getSalesRep_ID());
        counter.save(get_TrxName());

        //	Update copied lines
        MOrderLine[] counterLines = counter.getLines(true, null);
        for (int i = 0; i < counterLines.length; i++) {
            MOrderLine counterLine = counterLines[i];
            counterLine.setOrder(counter);	//	copies header values (BP, etc.)
            counterLine.setPrice();
            counterLine.setTax();
            counterLine.save(get_TrxName());
        }
        log.fine(counter.toString());

        //	Document Action
        if (counterDT != null) {
            if (counterDT.getDocAction() != null) {
                counter.setDocAction(counterDT.getDocAction());
                counter.processIt(counterDT.getDocAction());
                counter.save(get_TrxName());
            }
        }
        return counter;
    }	//	createCounterDoc

    /**
     * 	Void Document.
     * 	Set Qtys to 0 - Sales: reverse all documents
     * 	@return true if success 
     */
    public boolean voidIt() {
        MOrderLine[] lines = getLines(true, "M_Product_ID");
        log.info(toString());
        /*
         *  25/10/2013 Maria Jesus
         *  Comprobación que si la OC esta facturada no se puede anular.
         */
        String sql = " select documentno "
                + " from c_invoice "
                + " where c_invoice_id in ( "
                + "                         select c_invoice_id "
                + "                         from c_invoiceline  "
                + "                         where c_orderline_id in (select c_orderline_id "
                + "                                                  from c_orderline "
                + "                                                  where c_order_id = " + this.getC_Order_ID() + ")) "
                + " and docstatus = 'CO' ";
        PreparedStatement pstmt = DB.prepareStatement(sql, null);
        try {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                m_processMsg = "La OC no se puede anular ya que se encuentra factura, en la factura Nro: " + rs.getString(1);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        //	Clear Reservations
        if (!reserveStock(null, lines)) {
            m_processMsg = "Cannot unreserve Stock (void)";
            return false;
        }

        for (int i = 0; i < lines.length; i++) {
            MOrderLine line = lines[i];
            BigDecimal old = line.getQtyOrdered();
            if (old.signum() != 0) {
                line.addDescription(Msg.getMsg(getCtx(), "Voided") + " (" + old + ")");
                line.setQty(Env.ZERO);
                line.setLineNetAmt(Env.ZERO);
                if (!line.save(get_TrxName())) {
                    m_processMsg = "Cannot set lines to 0 (void)";
                    return false;
                }
            }
        }
        addDescription(Msg.getMsg(getCtx(), "Voided"));

        if (!createReversals()) {
            return false;
        }

        setProcessed(true);
        setDocAction(DOCACTION_None);
        return true;
    }	//	voidIt

    /**
     * 	Void Shipment/Invoice 
     * 	@return true if success
     */
    private boolean createVoids() {
        //	Cancel only Sales 
        if (!isSOTrx()) {
            return true;
        }

        log.info("createVoids");
        StringBuffer info = new StringBuffer();

        //	Reverse All *Shipments*
        info.append("@M_InOut_ID@:");
        MInOut[] shipments = getShipments();
        for (int i = 0; i < shipments.length; i++) {
            MInOut ship = shipments[i];
            //	if closed - ignore
            if (MInOut.DOCSTATUS_Closed.equals(ship.getDocStatus())
                    || MInOut.DOCSTATUS_Reversed.equals(ship.getDocStatus())
                    || MInOut.DOCSTATUS_Voided.equals(ship.getDocStatus())) {
                continue;
            }
            ship.set_TrxName(get_TrxName());

            if (ship.voidIt()) {
                ship.setDocStatus(MInOut.DOCSTATUS_Voided);
            } else {
                m_processMsg = "Could not void Shipment " + ship;
                return false;
            }

            ship.setDocAction(MInOut.DOCACTION_None);
            ship.save(get_TrxName());
        }	//	for all shipments

        //	Reverse All *Invoices*
        info.append(" - @C_Invoice_ID@:");
        MInvoice[] invoices = getInvoices();
        for (int i = 0; i < invoices.length; i++) {
            MInvoice invoice = invoices[i];
            //	if closed - ignore
            if (MInvoice.DOCSTATUS_Closed.equals(invoice.getDocStatus())
                    || MInvoice.DOCSTATUS_Reversed.equals(invoice.getDocStatus())
                    || MInvoice.DOCSTATUS_Voided.equals(invoice.getDocStatus())) {
                continue;
            }
            invoice.set_TrxName(get_TrxName());

            if (invoice.voidIt()) {
                invoice.setDocStatus(MInvoice.DOCSTATUS_Voided);
            } else {
                m_processMsg = "Could not void Invoice " + invoice;
                return false;
            }

            invoice.setDocAction(MInvoice.DOCACTION_None);
            invoice.save(get_TrxName());
        }	//	for all invoices

        m_processMsg = info.toString();
        return true;
    }	//	createVoids

    /**
     * 	Create Shipment/Invoice Reversals
     * 	@return true if success
     */
    private boolean createReversals() {
        //	Cancel only Sales 
        if (!isSOTrx()) {
            return true;
        }

        log.info("createReversals");
        StringBuffer info = new StringBuffer();

        //	Reverse All *Shipments*
        info.append("@M_InOut_ID@:");
        MInOut[] shipments = getShipments();
        for (int i = 0; i < shipments.length; i++) {
            MInOut ship = shipments[i];
            //	if closed - ignore
            if (MInOut.DOCSTATUS_Closed.equals(ship.getDocStatus())
                    || MInOut.DOCSTATUS_Reversed.equals(ship.getDocStatus())
                    || MInOut.DOCSTATUS_Voided.equals(ship.getDocStatus())) {
                continue;
            }
            ship.set_TrxName(get_TrxName());

            //	If not completed - void - otherwise reverse it
            if (!MInOut.DOCSTATUS_Completed.equals(ship.getDocStatus())) {
                if (ship.voidIt()) {
                    ship.setDocStatus(MInOut.DOCSTATUS_Voided);
                }
            } else if (ship.reverseCorrectIt()) //	completed shipment
            {
                ship.setDocStatus(MInOut.DOCSTATUS_Reversed);
                info.append(" ").append(ship.getDocumentNo());
            } else {
                m_processMsg = "Could not reverse Shipment " + ship;
                return false;
            }
            ship.setDocAction(MInOut.DOCACTION_None);
            ship.save(get_TrxName());
        }	//	for all shipments

        //	Reverse All *Invoices*
        info.append(" - @C_Invoice_ID@:");
        MInvoice[] invoices = getInvoices();
        for (int i = 0; i < invoices.length; i++) {
            MInvoice invoice = invoices[i];
            //	if closed - ignore
            if (MInvoice.DOCSTATUS_Closed.equals(invoice.getDocStatus())
                    || MInvoice.DOCSTATUS_Reversed.equals(invoice.getDocStatus())
                    || MInvoice.DOCSTATUS_Voided.equals(invoice.getDocStatus())) {
                continue;
            }
            invoice.set_TrxName(get_TrxName());

            //	If not compleded - void - otherwise reverse it
            if (!MInvoice.DOCSTATUS_Completed.equals(invoice.getDocStatus())) {
                if (invoice.voidIt()) {
                    invoice.setDocStatus(MInvoice.DOCSTATUS_Voided);
                }
            } else if (invoice.reverseCorrectIt()) //	completed invoice
            {
                invoice.setDocStatus(MInvoice.DOCSTATUS_Reversed);
                info.append(" ").append(invoice.getDocumentNo());
            } else {
                m_processMsg = "Could not reverse Invoice " + invoice;
                return false;
            }

            invoice.setDocAction(MInvoice.DOCACTION_None);
            invoice.save(get_TrxName());
        }	//	for all shipments

        m_processMsg = info.toString();
        return true;
    }	//	createReversals

    /**
     * 	Close Document.
     * 	Cancel not delivered Qunatities
     * 	@return true if success 
     */
    public boolean closeIt() {
        log.info(toString());

        //	Close Not delivered Qty - SO/PO
        MOrderLine[] lines = getLines(true, "M_Product_ID");
        for (int i = 0; i < lines.length; i++) {
            MOrderLine line = lines[i];
            BigDecimal old = line.getQtyOrdered();
            if (old.compareTo(line.getQtyDelivered()) != 0) {
                line.setQtyLostSales(line.getQtyOrdered().subtract(line.getQtyDelivered()));
                line.setQtyOrdered(line.getQtyDelivered());
                //	QtyEntered unchanged
                line.addDescription("Close (" + old + ")");
                line.save(get_TrxName());
            }
        }
        //	Clear Reservations
        if (!reserveStock(null, lines)) {
            m_processMsg = "Cannot unreserve Stock (close)";
            return false;
        }
        setProcessed(true);
        setDocAction(DOCACTION_None);
        /** BISion - 15/12/2009 - Santiago Ibañez
         * Modificado porque no se seteaba el estado
         */
        setDocStatus(DOCSTATUS_Closed);
        save();
        return true;
    }	//	closeIt

    /**
     * 	Reverse Correction - same void
     * 	@return true if success 
     */
    public boolean reverseCorrectIt() {
        log.info(toString());
        return voidIt();
    }	//	reverseCorrectionIt

    /**
     * 	Reverse Accrual - none
     * 	@return false 
     */
    public boolean reverseAccrualIt() {
        log.info(toString());
        return false;
    }	//	reverseAccrualIt

    /**
     * 	Re-activate.
     * 	@return true if success 
     */
    public boolean reActivateIt() {
        log.info(toString());

        MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
        String DocSubTypeSO = dt.getDocSubTypeSO();

        //	PO - just re-open
        if (!isSOTrx()) {
            log.info("Existing documents not modified - " + dt);
        } //	Reverse Direct Documents
        else if (MDocType.DOCSUBTYPESO_OnCreditOrder.equals(DocSubTypeSO) //	(W)illCall(I)nvoice
                || MDocType.DOCSUBTYPESO_WarehouseOrder.equals(DocSubTypeSO) //	(W)illCall(P)ickup	
                || MDocType.DOCSUBTYPESO_POSOrder.equals(DocSubTypeSO)) //	(W)alkIn(R)eceipt
        {
            //if (!createReversals())
            if (!createVoids() || !reserveStock(null, getLines(true, "M_Product_ID"))) {
                return false;
            }
        } else {
            log.info("Existing documents not modified - SubType=" + DocSubTypeSO);
        }

        setDocAction(DOCACTION_Complete);
        setProcessed(false);
        return true;
    }	//	reActivateIt

    /*************************************************************************
     * 	Get Summary
     *	@return Summary of Document
     */
    public String getSummary() {
        StringBuffer sb = new StringBuffer();
        sb.append(getDocumentNo());
        //	: Grand Total = 123.00 (#1)
        sb.append(": ").
                append(Msg.translate(getCtx(), "GrandTotal")).append("=").append(getGrandTotal());
        if (m_lines != null) {
            sb.append(" (#").append(m_lines.length).append(")");
        }
        //	 - Description
        if (getDescription() != null && getDescription().length() > 0) {
            sb.append(" - ").append(getDescription());
        }
        return sb.toString();
    }	//	getSummary

    /**
     * 	Get Process Message
     *	@return clear text error message
     */
    public String getProcessMsg() {
        return m_processMsg;
    }	//	getProcessMsg

    /**
     * 	Get Document Owner (Responsible)
     *	@return AD_User_ID
     */
    public int getDoc_User_ID() {
        return getSalesRep_ID();
    }	//	getDoc_User_ID

    /**
     * 	Get Document Approval Amount
     *	@return amount
     */
    public BigDecimal getApprovalAmt() {
        return getGrandTotal();
    }	//	getApprovalAmt

    private boolean unreserveStock(MDocType dt, MOrderLine[] lines) {
        if (dt == null) {
            dt = MDocType.get(getCtx(), getC_DocType_ID());
        }

        //	Binding
        boolean binding = !dt.isProposal();
        //	Not binding - i.e. Target=0
		/*
        if (DOCACTION_Void.equals(getDocAction())
        //	Closing Binding Quotation
        || (MDocType.DOCSUBTYPESO_Quotation.equals(dt.getDocSubTypeSO())
        && DOCACTION_Close.equals(getDocAction()))
        || isDropShip() )
        binding = false;
         */
        boolean isSOTrx = isSOTrx();
        log.fine("Binding=" + binding + " - IsSOTrx=" + isSOTrx);
        //	Force same WH for all but SO/PO
        int header_M_Warehouse_ID = getM_Warehouse_ID();
        if (MDocType.DOCSUBTYPESO_StandardOrder.equals(dt.getDocSubTypeSO())
                || MDocType.DOCBASETYPE_PurchaseOrder.equals(dt.getDocBaseType())) {
            header_M_Warehouse_ID = 0;		//	don't enforce
        }
        //	Always check and (un) Reserve Inventory
        for (int i = 0; i < lines.length; i++) {
            MOrderLine line = lines[i];
            //	Check/set WH/Org
			/*if (header_M_Warehouse_ID != 0)	//	enforce WH
            {
            if (header_M_Warehouse_ID != line.getM_Warehouse_ID())
            line.setM_Warehouse_ID(header_M_Warehouse_ID);
            if (getAD_Org_ID() != line.getAD_Org_ID())
            line.setAD_Org_ID(getAD_Org_ID());
            }
            
            //	Binding
            BigDecimal target = binding ? line.getQtyOrdered() : Env.ZERO;
            BigDecimal difference = target
            .subtract(line.getQtyReserved())
            .subtract(line.getQtyDelivered());
            if (difference.signum() == 0)
            continue;
            
            log.fine("Line=" + line.getLine()
            + " - Target=" + target + ",Difference=" + difference
            + " - Ordered=" + line.getQtyOrdered()
            + ",Reserved=" + line.getQtyReserved() + ",Delivered=" + line.getQtyDelivered());
             */
            //	Check Product - Stocked and Item
            MProduct product = line.getProduct();
            if (product != null) {
                if (product.isStocked()) {
                    BigDecimal reserved = Env.ZERO;
                    BigDecimal ordered;
                    BigDecimal diffOrdenado = line.getQtyEntered().subtract(line.getQtyDelivered());
                    //Si se recibio mas de lo que se pidio
                    if (diffOrdenado.compareTo(Env.ZERO) < 0) {
                        ordered = Env.ZERO;
                    } else {
                        ordered = diffOrdenado;
                    }
                    if (isSOTrx) {
                        ordered = Env.ZERO;
                    }

                    int M_Locator_ID = 0;
                    //	Get Locator to reserve
                    if (line.getM_AttributeSetInstance_ID() != 0) //	Get existing Location
                    {
                        M_Locator_ID = MStorage.getM_Locator_ID(line.getM_Warehouse_ID(),
                                line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
                                ordered, get_TrxName());
                    }
                    //	Get default Location
                    if (M_Locator_ID == 0) {
                        MWarehouse wh = MWarehouse.get(getCtx(), line.getM_Warehouse_ID());
                        M_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
                    }
                    //	Update Storage
                    if (!MStorage.add(getCtx(), line.getM_Warehouse_ID(), M_Locator_ID,
                            line.getM_Product_ID(),
                            line.getM_AttributeSetInstance_ID(), line.getM_AttributeSetInstance_ID(),
                            Env.ZERO, reserved, ordered, get_TrxName())) {
                        return false;
                    }
                }	//	stockec
                //	update line
                //line.setQtyReserved(line.getQtyEntered().subtract(line.getQtyDelivered()));
                if (!line.save(get_TrxName())) {
                    return false;
                }
            }	//	product
        }	//	reverse inventory
        return true;
    }	//	unreserveStock

    public boolean uncloseIt() {
        log.info(toString());

        //	Close Not delivered Qty - SO/PO
        MOrderLine[] lines = getLines(true, "M_Product_ID");
        for (int i = 0; i < lines.length; i++) {
            MOrderLine line = lines[i];
            //QtyLostSales es cero en estado completo.
            line.setQtyLostSales(Env.ZERO);
            //La cantidad Ordenada es lo que antes estaba como LostSales y Ordered(Reserved en este caso)
            BigDecimal diferencia = line.getQtyEntered().subtract(line.getQtyDelivered());
            if (diferencia.signum() == 1) {
                line.setQtyReserved(line.getQtyEntered().subtract(line.getQtyDelivered()));
                line.setQtyOrdered(line.getQtyEntered().subtract(line.getQtyDelivered()));
            } else {
                line.setQtyReserved(Env.ZERO);
                line.setQtyOrdered(Env.ZERO);
            }
            line.addDescription("");
            line.save(get_TrxName());
        }
        //	Clear Reservations
        if (!unreserveStock(null, lines)) {
            m_processMsg = "Cannot unreserve Stock (unclose)";
            return false;
        }
        setDescription("");
        setDocAction(DOCACTION_Close);
        setDocStatus(DOCSTATUS_Completed);
        save();
        return true;
    }	//	closeIt

    /*
     *  18/03/2013 Maria Jesus Martin
     *  Metodo para revertir las cantidades de las OC que estaban en estado borrador
     *  y pasaron a Close por el Proceso de Cerrar Ordenes de Compra.
     *
     */
    public boolean revertirQtyLostSales() {
        log.info(toString());

        //	Close Not delivered Qty - SO/PO
        MOrderLine[] lines = getLines(true, "M_Product_ID");
        for (int i = 0; i < lines.length; i++) {
            MOrderLine line = lines[i];
            //QtyLostSales es cero en estado completo.
            line.setQtyLostSales(Env.ZERO);

            BigDecimal diferencia = line.getQtyEntered().subtract(line.getQtyDelivered());
            line.setQtyReserved(Env.ZERO);
            line.setQtyOrdered(Env.ZERO);
            line.setQtyDelivered(Env.ZERO);

            line.addDescription("");
            line.save(get_TrxName());
        }
        //	Clear Reservations
        if (!unreserveStock(null, lines)) {
            m_processMsg = "Cannot unreserve Stock (unclose)";
            return false;
        }
        setDocAction(STATUS_Completed);
        setDocStatus(STATUS_Drafted);
        save();
        return true;
    }
}	//	MOrder
