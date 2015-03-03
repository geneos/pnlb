/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.viewer;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.table.TableColumn;
import org.compiere.grid.ed.VEditor;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.FieldDynamicReport;
import org.compiere.model.MZYNREPORT;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.zynnia.reports.ReportUtils;
import org.zynnia.reports.parameters.ZynParameter;
import org.zynnia.reports.table.XTableColumnModel;

/**
 *
 * @author Alejandro Scott
 */
public abstract class Exporter {

    private static CLogger log = CLogger.getCLogger(Exporter.class);

    protected final int reportID;

    protected final Hashtable<Integer, ZynParameter> activeParams;

    protected final MiniTable p_table;

    protected final XTableColumnModel columnModel;

    protected final Hashtable<TableColumn, FieldDynamicReport> fieldsInReport;

    protected final Hashtable<String, Properties> printProperties;

    protected final MZYNREPORT zynReport;

     public Exporter(int reportID, Hashtable<Integer, ZynParameter> activeParams, MiniTable p_table, XTableColumnModel columnModel, Hashtable<TableColumn, FieldDynamicReport> fieldsInReport, Hashtable<String, Properties> printProperties) {
        this.reportID = reportID;
        this.activeParams = activeParams;
        this.p_table = p_table;
        this.columnModel = columnModel;
        this.fieldsInReport = fieldsInReport;
        this.printProperties = printProperties;
        this.zynReport = new MZYNREPORT(Env.getCtx(), this.reportID, null);
    }

    public abstract void export();

    protected String getComponentDisplayText(Component component) {
        if (component != null && component instanceof VEditor) {
            VEditor editor = (VEditor) component;
            return editor.getDisplay();
        }
        return null;
    }

    protected Object getComponentValue(Component component) {
        if (component != null && component instanceof VEditor) {
            VEditor editor = (VEditor) component;
            return editor.getValue();
        }
        return null;
    }

    protected void showReport(File reportFile) {
        if (reportFile != null) {
                try {
                    ReportUtils.openFile(reportFile);
                } catch (IOException ex) {
                    log.log(Level.SEVERE, "Error opening temporal file", ex);
                }
            }

    }

}
