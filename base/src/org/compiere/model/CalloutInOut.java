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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;
import org.compiere.util.*;

/**
 *	Shipment/Receipt Callouts	
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutInOut.java,v 1.13 2005/11/25 21:57:26 jjanke Exp $
 */
public class CalloutInOut extends CalloutEngine {

    /**
     *	M_InOut - Order.
     */
    public String order(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        Integer C_Order_ID = (Integer) value;
        if (C_Order_ID == null || C_Order_ID.intValue() == 0) {
            return "";
        }

        //	Get Details
        MOrder order = new MOrder(ctx, C_Order_ID.intValue(), null);
        if (order.get_ID() != 0) {
            mTab.setValue("DateOrdered", order.getDateOrdered());
            mTab.setValue("POReference", order.getPOReference());
            mTab.setValue("AD_Org_ID", new Integer(order.getAD_Org_ID()));
            mTab.setValue("AD_OrgTrx_ID", new Integer(order.getAD_OrgTrx_ID()));
            mTab.setValue("C_Campaign_ID", new Integer(order.getC_Activity_ID()));
            mTab.setValue("C_Campaign_ID", new Integer(order.getC_Campaign_ID()));
            mTab.setValue("C_Project_ID", new Integer(order.getC_Project_ID()));
            mTab.setValue("User1_ID", new Integer(order.getUser1_ID()));
            mTab.setValue("User2_ID", new Integer(order.getUser2_ID()));
            mTab.setValue("M_Warehouse_ID", new Integer(order.getM_Warehouse_ID()));
            //
            mTab.setValue("DeliveryRule", order.getDeliveryRule());
            mTab.setValue("DeliveryViaRule", order.getDeliveryViaRule());
            mTab.setValue("M_Shipper_ID", new Integer(order.getM_Shipper_ID()));
            mTab.setValue("FreightCostRule", order.getFreightCostRule());
            mTab.setValue("FreightAmt", order.getFreightAmt());

            mTab.setValue("C_BPartner_ID", new Integer(order.getC_BPartner_ID()));
        }
        return "";
    }	//	order

    /**
     *	InOut - DocType.
     *			- sets MovementType
     *			- gets DocNo
     */
    public String docType(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        Integer C_DocType_ID = (Integer) value;
        if (C_DocType_ID == null || C_DocType_ID.intValue() == 0) {
            return "";
        }

        String sql = "SELECT d.DocBaseType, d.IsDocNoControlled, s.CurrentNext "
                + "FROM C_DocType d, AD_Sequence s "
                + "WHERE C_DocType_ID=?" //	1
                + " AND d.DocNoSequence_ID=s.AD_Sequence_ID(+)";
        try {
            Env.setContext(ctx, WindowNo, "C_DocTypeTarget_ID", C_DocType_ID.intValue());
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, C_DocType_ID.intValue());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                //	Set Movement Type
                String DocBaseType = rs.getString("DocBaseType");
                if (DocBaseType.equals(MDocType.DOCBASETYPE_MaterialDelivery))  {
                    boolean IsSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));
                    if (IsSOTrx) {
                        mTab.setValue("MovementType", "C-");
                    } else {
                        mTab.setValue("MovementType", "V-");
                    }

                } //end vpj-cd e-evolution  23 Feb 2006
                //	Customer Shipments
                /**
                 * BISion - 03/07/2009 - Santiago Ibañez
                 * Modificacion realizada para considerar los nuevos tipos de documento
                 */
                else if (DocBaseType.equals(MDocType.DOCBASETYPE_DevolucionMaterialesTerceros) || DocBaseType.equals(MDocType.DOCBASETYPE_RecepcionMaterialesTerceros)) //	Material Receipts
                //begin vpj-cd e-evolution 11 ENE 2005
                {
                    boolean IsSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));
                    if (IsSOTrx) {
                        mTab.setValue("MovementType", "C+");
                    } else {
                        mTab.setValue("MovementType", "V+");
                    }
                }
                //mTab.setValue("MovementType", "V+");				//	Vendor Receipts
                //end vpj-cd e-evolution 11 ENE 2005


                //	DocumentNo
                if (rs.getString("IsDocNoControlled").equals("Y")) {
                    if (C_DocType_ID != 5000046) {
                        mTab.setValue("DocumentNo", "<" + rs.getString("CurrentNext") + ">");
                    } else {
                        mTab.setValue("DocumentNo", "PENDIENTE");
                        mTab.setValue("DeliveryViaRule", "S");
                    }

                }
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql, e);
            return e.getLocalizedMessage();
        }
        return "";
    }	//	docType

    /**
     *	M_InOut - Defaults for BPartner.
     *			- Location
     *			- Contact
     */
    public String bpartner(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        Integer C_BPartner_ID = (Integer) value;
        if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0) {
            return "";
        }

        /**
         *      @autor Daniel 19/07/2010
         *      Modificación, teniendo en cuenta si es Compra o Venta
         *
         */
        String sqlIsSOTrx = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
                + "p.M_PriceList_ID,p.PaymentRule,p.POReference,"
                + "p.SO_Description,p.IsDiscountPrinted,"
                + "p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
                + "locship.C_Location_ID AS ShipLocation,c.AD_User_ID, locbill.C_Location_ID AS BillLocation, "
                + "p.DeliveryViaRule "
                + "FROM C_BPartner p "
                + " LEFT OUTER JOIN C_BPartner_Location lbill ON (p.C_BPartner_ID=lbill.C_BPartner_ID AND lbill.IsBillTo='Y' AND lbill.IsActive='Y')"
                + " LEFT OUTER JOIN C_BPartner_Location lship ON (p.C_BPartner_ID=lship.C_BPartner_ID AND lship.IsShipTo='Y' AND lship.IsActive='Y')"
                + " LEFT OUTER JOIN C_Location locbill ON (lbill.C_Location_ID=locbill.C_Location_ID)"
                + " LEFT OUTER JOIN C_Location locship ON (lship.C_Location_ID=locship.C_Location_ID)"
                + " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID) "
                + "WHERE p.isActive='Y' AND p.C_BPartner_ID=?";

        String sqlIsPOTrx = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
                + "p.M_PriceList_ID,p.PaymentRule,p.POReference,"
                + "p.SO_Description,p.IsDiscountPrinted,"
                + "p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
                + "l.C_BPartner_Location_ID,c.AD_User_ID "
                + "FROM C_BPartner p, C_BPartner_Location l, AD_User c "
                + "WHERE p.C_BPartner_ID=l.C_BPartner_ID(+)"
                + " AND p.C_BPartner_ID=c.C_BPartner_ID(+)"
                + " AND p.isActive='Y'"
                + " AND p.C_BPartner_ID=?";

        try {
            String isSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx");
            if (isSOTrx.equals("Y")) {
                PreparedStatement pstmt = DB.prepareStatement(sqlIsSOTrx, null);
                pstmt.setInt(1, C_BPartner_ID.intValue());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    //	Location
                    Integer ii = new Integer(rs.getInt("ShipLocation"));
                    if (rs.wasNull()) {
                        mTab.setValue("C_Location_ID", null);
                    } else {
                        mTab.setValue("C_Location_ID", ii);
                    }
                    //	Location
                    Integer iii = new Integer(rs.getInt("BillLocation"));
                    if (rs.wasNull()) {
                        mTab.setValue("LOCATION_BILL_ID", null);
                    } else {
                        mTab.setValue("LOCATION_BILL_ID", iii);
                    }
                    //	Contact
                    ii = new Integer(rs.getInt("AD_User_ID"));
                    if (rs.wasNull()) {
                        mTab.setValue("AD_User_ID", null);
                    } else {
                        mTab.setValue("AD_User_ID", ii);
                    }
                    //	Delivery Via Rule
                    String iv = rs.getString("DeliveryViaRule");
                    if (rs.wasNull()) {
                        mTab.setValue("DeliveryViaRule", null);
                    } else {
                        mTab.setValue("DeliveryViaRule", iv);
                    }

                    //	CreditAvailable
                    double CreditAvailable = rs.getDouble("CreditAvailable");
                    if (!rs.wasNull() && CreditAvailable < 0) {
                        mTab.fireDataStatusEEvent("CreditLimitOver",
                                DisplayType.getNumberFormat(DisplayType.Amount).format(CreditAvailable),
                                false);
                    }
                }
                rs.close();
                pstmt.close();
            } else {
                PreparedStatement pstmt = DB.prepareStatement(sqlIsPOTrx, null);
                pstmt.setInt(1, C_BPartner_ID.intValue());
                ResultSet rs = pstmt.executeQuery();
                BigDecimal bd;
                if (rs.next()) {
                    //	Location
                    Integer ii = new Integer(rs.getInt("C_BPartner_Location_ID"));
                    if (rs.wasNull()) {
                        mTab.setValue("C_BPartner_Location_ID", null);
                    } else {
                        mTab.setValue("C_BPartner_Location_ID", ii);
                    }
                    //	Contact
                    ii = new Integer(rs.getInt("AD_User_ID"));
                    if (rs.wasNull()) {
                        mTab.setValue("AD_User_ID", null);
                    } else {
                        mTab.setValue("AD_User_ID", ii);
                    }

                    //	CreditAvailable
                    double CreditAvailable = rs.getDouble("CreditAvailable");
                    if (!rs.wasNull() && CreditAvailable < 0) {
                        mTab.fireDataStatusEEvent("CreditLimitOver",
                                DisplayType.getNumberFormat(DisplayType.Amount).format(CreditAvailable),
                                false);
                    }
                }
                rs.close();
                pstmt.close();
            }
        } catch (SQLException e) {
            return e.getLocalizedMessage();
        }

        return "";
    }	//	bpartner

    /**
     *	M_InOutLine - Order Line.
     */
    public String orderLine(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        Integer C_OrderLine_ID = (Integer) value;
        if (C_OrderLine_ID == null || C_OrderLine_ID.intValue() == 0) {
            return "";
        }
        setCalloutActive(true);

        //	Get Details
        MOrderLine ol = new MOrderLine(ctx, C_OrderLine_ID.intValue(), null);
        if (ol.get_ID() != 0) {
            mTab.setValue("M_Product_ID", new Integer(ol.getM_Product_ID()));
            mTab.setValue("M_AttributeSetInstance_ID", new Integer(ol.getM_AttributeSetInstance_ID()));
            //
            mTab.setValue("C_UOM_ID", new Integer(ol.getC_UOM_ID()));
            BigDecimal MovementQty = ol.getQtyOrdered().subtract(ol.getQtyDelivered());
            /** BISion - 25/06/2009 - Santiago Ibañez
             * Modificacion realizada para que la cantidad del movimiento sea
             * igual a la cantidad ingresada
             */
            //mTab.setValue("MovementQty", MovementQty);
            mTab.setValue("MovementQty", mTab.getValue("QtyEntered"));
            BigDecimal QtyEntered = MovementQty;
            if (ol.getQtyEntered().compareTo(ol.getQtyOrdered()) != 0) {
                QtyEntered = QtyEntered.multiply(ol.getQtyEntered()).divide(ol.getQtyOrdered(), BigDecimal.ROUND_HALF_UP);
            }
            /** BISion - 25/06/2009 - Santiago Ibañez
             * Modificacion realizada para que no varie la cantidad ingresada
             */
            //mTab.setValue("QtyEntered", QtyEntered);
            mTab.setValue("QtyEntered", mTab.getValue("QtyEntered"));
        }
        setCalloutActive(false);
        return "";
    }	//	orderLine

    /**
     *	M_InOutLine - Default UOM/Locator for Product.
     */
    public String product(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        if (isCalloutActive()) {
            return "";
        }
        Integer M_Product_ID = (Integer) value;
        if (M_Product_ID == null || M_Product_ID.intValue() == 0) {
            return "";
        }
        setCalloutActive(true);

        //	Set Attribute
        if (Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()
                && Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID") != 0) {
            mTab.setValue("M_AttributeSetInstance_ID", new Integer(Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID")));
        } else {
            mTab.setValue("M_AttributeSetInstance_ID", null);
        }

        int M_Warehouse_ID = Env.getContextAsInt(ctx, WindowNo, "M_Warehouse_ID");
        boolean IsSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));
        if (IsSOTrx) {
            setCalloutActive(false);
            return "";
        }

        //	Set UOM/Locator/Qty
        MProduct product = MProduct.get(ctx, M_Product_ID.intValue());
        mTab.setValue("C_UOM_ID", new Integer(product.getC_UOM_ID()));
        BigDecimal QtyEntered = (BigDecimal) mTab.getValue("QtyEntered");
        mTab.setValue("MovementQty", QtyEntered);
        if (product.getM_Locator_ID() != 0) {
            MLocator loc = MLocator.get(ctx, product.getM_Locator_ID());
            if (M_Warehouse_ID == loc.getM_Warehouse_ID()) {
                mTab.setValue("M_Locator_ID", new Integer(product.getM_Locator_ID()));
            } else {
                log.fine("No Locator for M_Product_ID=" + M_Product_ID + " and M_Warehouse_ID=" + M_Warehouse_ID);
            }
        } else {
            log.fine("No Locator for M_Product_ID=" + M_Product_ID);
        }
        setCalloutActive(false);
        return "";
    }	//	product

    /**
     *	InOut Line - Quantity.
     *		- called from C_UOM_ID, QtyEntered, MovementQty
     *		- enforces qty UOM relationship
     *  @param ctx      Context
     *  @param WindowNo current Window No
     *  @param mTab     Model Tab
     *  @param mField   Model Field
     *  @param value    The new value
     */
    public String qty(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        if (isCalloutActive() || value == null) {
            return "";
        }
        setCalloutActive(true);

        int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
        //	log.log(Level.WARNING,"qty - init - M_Product_ID=" + M_Product_ID);
        BigDecimal MovementQty, QtyEntered;

        //	No Product
        if (M_Product_ID == 0) {
            QtyEntered = (BigDecimal) mTab.getValue("QtyEntered");
            mTab.setValue("MovementQty", (BigDecimal) mTab.getValue("QtyEntered"));
        } //	UOM Changed - convert from Entered -> Product
        else if (mField.getColumnName().equals("C_UOM_ID")) {
            int C_UOM_To_ID = ((Integer) value).intValue();
            QtyEntered = (BigDecimal) mTab.getValue("QtyEntered");
            MovementQty = MUOMConversion.convertProductFrom(ctx, M_Product_ID,
                    C_UOM_To_ID, QtyEntered);
            if (MovementQty == null) {
                MovementQty = QtyEntered;
            }
            boolean conversion = QtyEntered.compareTo(MovementQty) != 0;
            log.fine("UOM=" + C_UOM_To_ID
                    + ", QtyEntered=" + QtyEntered
                    + " -> " + conversion
                    + " MovementQty=" + MovementQty);
            Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
            mTab.setValue("MovementQty", (BigDecimal) mTab.getValue("QtyEntered"));
        } //	No UOM defined
        else if (Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID") == 0) {
            QtyEntered = (BigDecimal) mTab.getValue("QtyEntered");
            mTab.setValue("MovementQty", QtyEntered);
        } //	QtyEntered changed - calculate MovementQty
        else if (mField.getColumnName().equals("QtyEntered")) {
            int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
            QtyEntered = (BigDecimal) value;
            MovementQty = MUOMConversion.convertProductFrom(ctx, M_Product_ID,
                    C_UOM_To_ID, QtyEntered);
            if (MovementQty == null) {
                MovementQty = QtyEntered;
            }
            boolean conversion = QtyEntered.compareTo(MovementQty) != 0;
            log.fine("UOM=" + C_UOM_To_ID
                    + ", QtyEntered=" + QtyEntered
                    + " -> " + conversion
                    + " MovementQty=" + MovementQty);
            Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
            /**
             * BISion - 18/06/2009 - Santiago Ibañez
             * Modificacion realizada para evitar variaciones
             * entre la Cantidad ingresada y la cantidad del movimiento
             */
            //mTab.setValue("MovementQty", MovementQty);
            mTab.setValue("MovementQty", (BigDecimal) mTab.getValue("QtyEntered"));
        } //	MovementQty changed - calculate QtyEntered
        else if (mField.getColumnName().equals("MovementQty")) {
            int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
            MovementQty = (BigDecimal) value;
            QtyEntered = MUOMConversion.convertProductTo(ctx, M_Product_ID,
                    C_UOM_To_ID, MovementQty);
            if (QtyEntered == null) {
                QtyEntered = MovementQty;
            }
            boolean conversion = MovementQty.compareTo(QtyEntered) != 0;
            log.fine("UOM=" + C_UOM_To_ID
                    + ", MovementQty=" + MovementQty
                    + " -> " + conversion
                    + " QtyEntered=" + QtyEntered);
            Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
            /**
             * BISion - 18/06/2009 - Santiago Ibañez
             * Modificado para evitar que se modifique la cantidad ingresada
             */
            //mTab.setValue("QtyEntered", QtyEntered);
        }
        //
        setCalloutActive(false);
        return "";
    }	//	qty

    /**
     *	Vit4B Actualizar numero de Remito.
     *      19/02/2007    
     */
    public String updateRemito(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        /*
        
        if (isCalloutActive() || value == null)
        return "";
        
        setCalloutActive(true);
        
        Env.setContext(ctx,"remito",(String)mTab.getValue("DocumentNo"));
        
        String documentNo = (String)mTab.getValue("DocumentNo");
        if(documentNo == "" || documentNo == null)
        {
        setCalloutActive(false);
        return "";
        }
        
        int indexOf = documentNo.indexOf("-");
        
        if(indexOf == -1 || indexOf == 1 || indexOf == documentNo.length())
        {
        JOptionPane.showMessageDialog(null,"N�mero de Documento Inv�lido", "Info", JOptionPane.INFORMATION_MESSAGE);
        mTab.setValue("DocumentNo", "");
        setCalloutActive(false);
        return "";
        }
        
        String sucursal = documentNo.substring(0,indexOf);
        String nro = documentNo.substring(indexOf+1,documentNo.length());
        
        switch (sucursal.length()) {
        case 1:
        sucursal = "000" + sucursal;
        break;
        case 2:
        sucursal = "00" + sucursal;
        break;
        case 3:
        sucursal = "0" + sucursal;
        break;
        case 4:
        break;
        default:
        JOptionPane.showMessageDialog(null,"N�mero de Documento Inv�lido", "Info", JOptionPane.INFORMATION_MESSAGE);
        mTab.setValue("DocumentNo", "");
        setCalloutActive(false);
        return "";
        }
        
        switch (nro.length()) {
        case 1:
        nro = "0000000" + nro;
        break;
        case 2:
        nro = "000000" + nro;
        break;
        case 3:
        nro = "00000" + nro;
        break;
        case 4:
        nro = "0000" + nro;
        break;
        case 5:
        nro = "000" + nro;
        break;
        case 6:
        nro = "00" + nro;
        break;
        case 7:
        nro = "0" + nro;
        break;
        case 8:
        break;
        default:
        JOptionPane.showMessageDialog(null,"N�mero de Documento Inv�lido", "Info", JOptionPane.INFORMATION_MESSAGE);
        mTab.setValue("DocumentNo", "");
        setCalloutActive(false);
        return "";
        }
        
        mTab.setValue("DocumentNo", sucursal + "-" + nro);
        
        setCalloutActive(false);
         */

        return "";
    }	//	orderLine

    public String checkFechas(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        //Chequeo de que fecha se trata porque el callout esta asignado a ambas
        if (mField.getColumnName().equals("MovementDate")) {
            Timestamp f = (Timestamp) mField.getValue();
            Timestamp f2 = (Timestamp) mTab.getField("ShipDate").getValue();
            if (f2 != null && f != null && f.compareTo(f2) > 0) {
                JOptionPane.showMessageDialog(null, "La Fecha de Entrega debe ser posterior o igual a la Fecha de Movimiento", "Fecha invalida", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            Timestamp f = (Timestamp) mField.getValue();
            Timestamp f2 = (Timestamp) mTab.getField("MovementDate").getValue();
            if (f2 != null && f != null && f.compareTo(f2) < 0) {
                JOptionPane.showMessageDialog(null, "La Fecha de Entrega debe ser posterior o igual a la Fecha de Movimiento", "Fecha invalida", JOptionPane.ERROR_MESSAGE);
            }
        }
        return "";
    }

    public String unidadMedida(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value) {
        int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
        //	No Product
        if (M_Product_ID != 0) {
            MProduct mp = MProduct.get(Env.getCtx(), M_Product_ID);
            mTab.setValue("C_UOM_ID", mp.getC_UOM_ID());

        }
        //
        return "";
    }
}	//	CalloutInOut
