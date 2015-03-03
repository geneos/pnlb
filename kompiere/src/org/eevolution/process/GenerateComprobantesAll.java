/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eevolution.process;

import org.compiere.process.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.logging.*;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *  Esta clase completa la tabla temporal que se usa para emitir el reporte de Comprobantes.
 *  27/03/2012 pedido por Edgardo Cardozo
 * 
 *	@author Zynnia
 *	@version 1.0
 */
public class GenerateComprobantesAll extends SvrProcess {

    private int p_PInstance_ID;
    private Timestamp fromDateAcct = null;
    private Timestamp toDateAcct = null;
    private Timestamp fromDate = null;
    private Timestamp toDate = null;
    private int C_BPartner_ID = 0;
    private int C_Doctype_ID = 0;
    private String posted = "";
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        p_PInstance_ID = getAD_PInstance_ID();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (name.equals("C_Doctype_ID")) {
                C_Doctype_ID = Integer.parseInt((String)para[i].getParameter());
            } else if (name.equals("DATE")) {
                fromDate = (Timestamp) para[i].getParameter();
                toDate = (Timestamp) para[i].getParameter_To();
            } else if (name.equals("DateAcct")) {
                fromDateAcct = (Timestamp) para[i].getParameter();
                toDateAcct = (Timestamp) para[i].getParameter_To();
            } else if (name.equals("C_BPartner_ID")) {
                C_BPartner_ID = Integer.parseInt((String)para[i].getParameter());
            } else if (name.equals("Posted")) {
                posted = (String)para[i].getParameter();
            }   
        }
    }

    protected String doIt() {
        loadComprobantes();
        UtilProcess.initViewer("Comprobantes All", p_PInstance_ID, getProcessInfo());
        return "";
    }    
    /*
     *  Método que carga en la tabla temporal los comprobantes
     * 
     */
    protected void loadComprobantes() {
        
        String sql = "";
        
        /*
         *  Obtengo los compronates activos de C_Invoice con los filtros de los parámetros
         */
        
        sql = "select comp.AD_CLIENT_ID, oomp.AD_ORG_ID, comp.C_DOCTYPE_ID, dtt.printname, "
                + "comp.documentno, comp.C_BPartner_ID, pa.value, pa.Name, pa.duns, "
                + "loc.C_Location_ID, loc.address1, loc.city, jur.C_Jurisdiccion_ID, jur.name, "
                + "comp.posted, line.linenetamt, line.linetotalamt, line.taxamt, comp.dateinvoiced, comp.dateacct "
                + "from C_Invoiceline line "
                + "inner join C_Invoice comp on (comp.C_Invoice_ID = line.C_Invoice_ID) "
                + "inner join C_Doctype dt on (dt.C_Doctype_ID = comp.C_Doctype_ID) "
                + "inner join C_Doctype_TRL dtt on (dt.C_Doctype_ID = dtt.C_Doctype_ID) "
                + "inner join C_BPartner pa on (comp.C_BPartner_ID = pa.C_BPartner_ID) "
                + "inner join C_Location loc on (loc.C_Location_ID = comp.C_Location_ID) "
                + "inner join C_Jurisdiccion jur on (loc.C_Jurisdiccion_ID = jur.C_Jurisdiccion_ID) "
                
                // Anexo las restricciones en función de los parámetros
                
                + "order by comp.C_BPartner_ID, comp.C_DOCTYPE_ID, ";
        
        
        PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
        try {
            ResultSet rs = pstmt.executeQuery();
            
            
            
            /*
             *  Obtengo las percepciones de los comprobantes
             */
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(GenerateComprobantesAll.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
 
}