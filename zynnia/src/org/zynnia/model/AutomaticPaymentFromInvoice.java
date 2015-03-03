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
package org.zynnia.model;

import java.sql.SQLException;
import java.util.logging.Level;
import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.PO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.compiere.apps.ADialog;
import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.VAllocation;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.model.MPaymentAllocate;
import org.compiere.model.MQuery;
import org.compiere.model.ModelValidator;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *
 * @author alejandro
 */
public class AutomaticPaymentFromInvoice implements ModelValidator {

    /** Logger */
    protected transient CLogger log = CLogger.getCLogger(getClass());
    /** Client			*/
    private int m_AD_Client_ID = -1;

    private int getPaymentWindow(String trxName) {
        //Obtengo la ventana traducido cuyo nombre sea Pago
        String sql = "SELECT w.ad_window_id FROM ad_window_trl wt "
                + "JOIN ad_window w ON (wt.ad_window_id = w.ad_window_id) "
                + "WHERE wt.name = 'Pago' AND wt.ad_language = 'es_MX'";
        PreparedStatement ps = DB.prepareStatement(sql, trxName);
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            rs.close();
            rs = null;
            ps.close();
            ps = null;
        } catch (Exception ex) {
            System.out.println("No se pudo obtener la ventana de Remito: " + ex.getMessage());
        }
        return 0;
    }

    private int getCDocTypeID(String trxName) {
        //Obtengo la ventana traducido cuyo nombre sea Pago
        String sql = "SELECT c_doctype_id FROM c_doctype "
                + "WHERE name = 'OP Orden de Pago'";
        PreparedStatement ps = DB.prepareStatement(sql, trxName);
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            rs.close();
            rs = null;
            ps.close();
            ps = null;
        } catch (Exception ex) {
            System.out.println("No se pudo obtener la ventana de Remito: " + ex.getMessage());
        }
        return 0;
    }

    /**
     *	Initialize Validation
     *	@param engine validation engine
     *	@param client client
     */
    public void initialize(ModelValidationEngine engine, MClient client) {
        m_AD_Client_ID = client.getAD_Client_ID();

        //	We want to be informed when C_Order is created/changed
        // engine.addModelChange("C_Order", this);
        //	We want to validate Invoice before preparing
        engine.addDocValidate("C_Invoice", this);
    }

    /**
     *	Get Client to be monitored
     *	@return AD_Client_ID client
     */
    public int getAD_Client_ID() {
        return m_AD_Client_ID;
    }

    /**
     *	User Login.
     *	Called when preferences are set
     *	@param AD_Org_ID org
     *	@param AD_Role_ID role
     *	@param AD_User_ID user
     *	@return error message or null
     */
    public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
       return null;
    }

    /**
     *	Model Change of a monitored Table.
     *	Called after PO.beforeSave/PO.beforeDelete
     *	when you called addModelChange for the table
     *	@param po persistent object
     *	@param type TYPE_
     *	@return error message or null
     *	@exception Exception if the recipient wishes the change to be not accept.
     */
    public String modelChange(PO po, int type) throws Exception {
        return null;
    }

    /**
     *	Validate Document.
     *	Called as first step of DocAction.prepareIt
     *	when you called addDocValidate for the table.
     *	Note that totals, etc. may not be correct.
     *	@param po persistent object
     *	@param timing see TIMING_ constants
     *	@return error message or null
     */
    public String docValidate(PO po, int timing) {
        //	Ignore all before Complete events
        if (timing != TIMING_AFTER_COMPLETE) {
            return null;
        }
        //
        if (po.get_TableName().equals("C_Invoice")) {
            MInvoice invoice = (MInvoice) po;
            int windowsID = Env.getLastActiveWindowNo();
            if (invoice.isProcessed() && MInvoice.DOCACTION_Close.equals(invoice.getDocAction()) && ADialog.ask(windowsID, Env.getWindow(windowsID), "AutomaticPaymentMsg")) {
                String trxName = invoice.get_TrxName();
                MPayment payment = new MPayment(invoice.getCtx(), 0, trxName);
                payment.setDateTrx(invoice.getDateInvoiced());
                payment.setDateAcct(invoice.getDateAcct());
                payment.setC_BPartner_ID(invoice.getC_BPartner_ID());
                payment.setPayAmt(invoice.getGrandTotal(true));
                payment.setC_Currency_ID(invoice.getC_Currency_ID());
                payment.setC_DocType_ID(getCDocTypeID(trxName));
                payment.save(trxName);

                int payment_ID = payment.getC_Payment_ID();

                MPaymentAllocate allocate = new MPaymentAllocate(invoice.getCtx(), 0, trxName);
                allocate.setC_Payment_ID(payment_ID);
                allocate.setC_Invoice_ID(invoice.getC_Invoice_ID());
                allocate.setAmount(invoice.getGrandTotal(true));
                allocate.save(trxName);
                try {
                    DB.commit(true, trxName);
        //Fecha de la Aplicación CG: fecha de la factura.
                } catch (SQLException ex) {
                   log.log(Level.SEVERE, null, ex);
                }

                AWindow frame = new AWindow();
                MQuery query = MQuery.getEqualQuery("C_Payment_ID", payment_ID);
                int AD_Window_ID = getPaymentWindow(trxName);
                log.info("Automatic AD_Window_ID=" + AD_Window_ID + " - " + query);
                frame.initWindow(AD_Window_ID, query);
                AEnv.showCenterScreen(frame);
            }

            if (invoice.isProcessed() && MInvoice.DOCACTION_Close.equals(invoice.getDocAction())) 
            {
                // If Invoice is completed open frame for allocate it
                if (ADialog.ask(windowsID, Env.getWindow(windowsID), "AutomaticPaymentAllocationMsg"))
                {
                    initFrameInvoiceAllocation(invoice);
                }                
                
            }
        }
        return null;
    }

     /**
     * @author Alejandro Scott - 20/09/2012
     *
     * Added by open form of allocation invoice when a invoice is completed
     */
    private void initFrameInvoiceAllocation(MInvoice invoice) {
        String sql = "SELECT AD_Form_ID FROM AD_Form WHERE ClassName = 'org.compiere.apps.form.VAllocation'";
		int AD_Form_ID = DB.getSQLValue(null, sql);

        FormFrame ff = new FormFrame();
		ff.openForm(AD_Form_ID);
        VAllocation form = (VAllocation) ff.getFormPanel();
        form.setC_BPartner_ID(invoice.getC_BPartner_ID());
        form.setC_Currency_ID(invoice.getC_Currency_ID());
        form.setDate(invoice.getDateAcct(), true);
		ff.pack();
		//	Center the window
		AEnv.showCenterScreen(ff);
    }
}
