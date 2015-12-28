/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;
import java.math.BigDecimal;
import java.sql.SQLException;

import org.compiere.model.MBankAccount;
import org.compiere.model.MCurrency;
import org.compiere.model.MDocType;
import org.compiere.model.MPAYMENTVALORES;
import org.compiere.model.MPAYMENTRET;
import org.compiere.model.MPayment;
import org.compiere.model.MVALORPAGO;
import org.compiere.process.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.*;
import org.compiere.apps.ADialog;
import org.compiere.model.*;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;

import org.compiere.util.*;
import org.eevolution.tools.DateTimeUtil;
import org.eevolution.tools.Numero2Letras;
import org.eevolution.tools.UtilProcess;
/**
 *
 * @author Bision
 */
public class GenerateRemito extends SvrProcess{

	int p_instance;        
        int m_print_format_id;
        int m_table_id;

     protected String doIt() throws Exception{
        
        /*
         *  Se imprime informe estandar o especial segun si es devolucion o no
         * 
         */

        MPrintFormat format = null;
        Language language = Language.getLoginLanguage();
        PrintInfo info = null;
        ReportEngine re = null;

        format = MPrintFormat.get(Env.getCtx(), m_print_format_id, false);
        format.setLanguage(language);
        format.setTranslationLanguage(language);

        MQuery query = new MQuery("RV_M_INOUT_HEADER_CO");
        query.addRestriction("M_InOut_ID", MQuery.EQUAL, getRecord_ID());
        info = new PrintInfo("RV_M_INOUT_HEADER_CO",m_table_id, getRecord_ID());
        re = new ReportEngine(Env.getCtx(), format, query, info);

        new Viewer(re);                

        return "success";
        
    }

    
    protected void prepare() {
        p_instance = getAD_PInstance_ID();
                 
        m_print_format_id = 1000967;
        m_table_id = 1000262;
        
        MInOut inout = new MInOut(Env.getCtx(),getRecord_ID(),null);
        if (inout.getC_DocType_ID() == 5000046){
            m_print_format_id = 5000156;
            m_table_id = 5000067;
        }
    }

}
