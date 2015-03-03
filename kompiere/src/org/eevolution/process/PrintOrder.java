/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import java.math.BigDecimal;
import java.util.logging.Level;
import org.compiere.model.MOrder;
import org.compiere.model.MQuery;
import org.compiere.model.PrintInfo;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.eevolution.tools.UtilProcess;

/**
 *
 * @author santiago
 */
public class PrintOrder extends SvrProcess{

    private int AD_PrintFormat_ID = 0;

    @Override
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (name.equals("AD_PrintFormat_ID"))
                AD_PrintFormat_ID = ((BigDecimal) para[i].getParameter()).intValue();
            else {
                log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
            }
        }
    }

    @Override
    protected String doIt() throws Exception {
        MPrintFormat pf = new MPrintFormat(getCtx(), AD_PrintFormat_ID, null);
        MQuery query = new MQuery(MOrder.getTableId(MOrder.Table_Name));
        //En la instancia del proceso está el ID del registro, en este caso la OC.
        query.addRestriction("C_Order_ID = "+getRecord_ID());
        ReportEngine re = new ReportEngine(getCtx(), pf, query, new PrintInfo(getProcessInfo()));
        new Viewer(re);
        //UtilProcess.initViewer(pf.getName(),getAD_PInstance_ID(),getProcessInfo());
        return "";
    }

}
