/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.reports;

import java.awt.Cursor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.table.TableColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.FieldDynamicReport;
import org.compiere.model.MZYNCONDITION;
import org.compiere.model.MZYNMODEL;
import org.compiere.model.MZYNMODELCOLUMN;
import org.compiere.model.MZYNMODELFK;
import org.compiere.model.MZYNREPORT;
import org.compiere.model.MZYNVIEWCALC;
import org.compiere.model.X_AD_Column;
import org.compiere.model.X_ZYN_VIEW;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.zynnia.reports.parameters.ZynParameter;
import org.zynnia.reports.table.XTableColumnModel;
import org.zynnia.reports.viewer.Exporter;
import org.zynnia.reports.viewer.PDFExporter;
import org.zynnia.reports.viewer.XLSExporter;

/**
 *
 * @author Alejandro Scott
 */
public class ZynReportsGenerator extends Thread {

    private final ZynReports reports;
    private static CLogger log = CLogger.getCLogger(ZynReportsGenerator.class);

    public enum ExportationAvailable {
        PDF_EXPORTATION,
        XLS_EXPORTATION;
    }


    /**
     * Main table for query *
     */
    private String m_sqlMainTable;
    /**
     * Main SQL Statement
     */
    private String m_sqlMain;
    /**
     * Initial JOIN Clause
     */
    protected String m_sqlJoin = "";
    /**
     * Initial WHERE Clause
     */
    protected String p_whereClause = "";
    /**
     * Initial GROUP Clause
     */
    protected String p_groupClause = "";
    /**
     * Order By Clause
     */
    private String m_sqlOrderBy;
    private int reportID;
    private Hashtable<Integer, ZynParameter> activeParams;
    private Hashtable<TableColumn, FieldDynamicReport> fieldsInReport;
    private Hashtable<String, Properties> printProperties;
    /**
     * Table
     */
    protected MiniTable p_table;
    /**
     * Column model
     */
    private XTableColumnModel columnModel = new XTableColumnModel();
    /**
     * Reports Listeners
     */
    private ArrayList<IReportsGeneratorListener> listeners = new ArrayList<IReportsGeneratorListener>();

    public ZynReportsGenerator(ZynReports reports, int zynReportID, MiniTable p_table, Hashtable<Integer, ZynParameter> activeParams, Hashtable<TableColumn, FieldDynamicReport> fieldsInReport, Hashtable<String, Properties> printProperties) {
        this.reports = reports;
        this.reportID = zynReportID;
        this.activeParams = activeParams;
        this.fieldsInReport = fieldsInReport;
        this.printProperties = printProperties;
        this.p_table = p_table;
        this.columnModel = (XTableColumnModel) p_table.getColumnModel();
    }

    protected void buildFieldsForReport(MZYNREPORT zynReport) {
        ArrayList<FieldDynamicReport> fields = zynReport.getFieldsForReport();
        StringBuilder sqlQueryBuilder = new StringBuilder("SELECT ");
        StringBuilder sqlQueryGroupBuilder = new StringBuilder(" GROUP BY ");
        boolean existsSum = false;
        for (FieldDynamicReport field : fields) {
            if (!sqlQueryBuilder.toString().equals("SELECT ")) {
                sqlQueryBuilder.append(", ");
            }
            if (field.isIsSum()) {
                existsSum = true;
                sqlQueryBuilder.append("SUM(").append(field.getCompleteNameForQuery()).append(")");
            } else {
                if (!sqlQueryGroupBuilder.toString().equals(" GROUP BY ")) {
                    sqlQueryGroupBuilder.append(", ");
                }
                sqlQueryGroupBuilder.append(field.getCompleteNameForQuery());
                sqlQueryBuilder.append(field.getCompleteNameForQuery());
            }
        }
        if (existsSum) {
            p_groupClause = sqlQueryGroupBuilder.toString();
        }
        m_sqlMain = sqlQueryBuilder.toString();
        log.log(Level.INFO, "select fields in query {0}", m_sqlMain);
    }

    protected void builTableJoinsForReport(MZYNMODEL zynModel) {
        StringBuilder sqlQueryBuilder = new StringBuilder();
        sqlQueryBuilder.append(" FROM ").append(m_sqlMainTable).append(" ");
        for (MZYNMODELFK rel : zynModel.getRelations()) {
            sqlQueryBuilder.append(rel.getRelationName()).append(" ");
            sqlQueryBuilder.append(rel.getSecondTableName()).append(" ON (");
            sqlQueryBuilder.append(rel.getFirstTableName()).append(".").append(rel.getFirstColumName());
            sqlQueryBuilder.append(" = ").append(rel.getSecondTableName()).append(".").append(rel.getSecondColumName()).append(") ");
        }
        m_sqlJoin = sqlQueryBuilder.toString();
        log.log(Level.INFO, "join for query with {0}", m_sqlJoin);
    }

    protected void buildConditionsForReport(MZYNREPORT zynReport) {
        StringBuilder sqlQueryBuilder = new StringBuilder();
        sqlQueryBuilder.append(" WHERE ");
        MZYNCONDITION condition = zynReport.getMainContidition();
        if (condition != null) {
            sqlQueryBuilder.append(condition.getSQLQuery());
            p_whereClause = sqlQueryBuilder.toString();
            if (p_whereClause.equals(" WHERE ")) {
                p_whereClause = "";
            }
            log.log(Level.INFO, "where for query with {0}", p_whereClause);
        }
    }

    protected void buildFieldsForOrder(MZYNREPORT zynReport) {
        ArrayList<FieldDynamicReport> fields = zynReport.getFieldsForOrder();
        StringBuilder sqlQueryBuilder = new StringBuilder();
        sqlQueryBuilder.append(" ORDER BY ");
        for (FieldDynamicReport field : fields) {
            if (!sqlQueryBuilder.toString().equals(" ORDER BY ")) {
                sqlQueryBuilder.append(", ");
            }
            sqlQueryBuilder.append(field.getCompleteNameForQuery());
        }
        m_sqlOrderBy = sqlQueryBuilder.toString();
        if (m_sqlOrderBy.equals(" ORDER BY ")) {
            m_sqlOrderBy = "";
        }
        log.log(Level.INFO, "order fields in query {0}", m_sqlOrderBy);
    }

    /**
     * Do Work (load data)
     */
    @Override
    public void run() {
        notifyReportsGeneratorListenerPreExecution();

        MZYNREPORT zreport = new MZYNREPORT(Env.getCtx(), reportID, null);
        MZYNMODEL zmodel = new MZYNMODEL(Env.getCtx(), zreport.getZYN_MODEL_ID(), null);
        m_sqlMainTable = zmodel.getNameOfMainTable();

        buildFieldsForReport(zreport);
        builTableJoinsForReport(zmodel);
        buildConditionsForReport(zreport);
        buildFieldsForOrder(zreport);

        log.fine("Info.Worker.run");
        reports.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //setStatusLine(Msg.getMsg(Env.getCtx(), "StartSearch"), false);

        StringBuilder sql = new StringBuilder(m_sqlMain);
        sql.append(m_sqlJoin);
        int idx = 0;
        StringBuilder sqlParameters = new StringBuilder();
        for (ZynParameter paramInput : activeParams.values()) {
            String queryByParam = paramInput.getQueryParameter();
            if (queryByParam.length() > 0) {
                if (idx != 0) {
                    sqlParameters.append(" AND ");
                }
                sqlParameters.append(paramInput.getQueryParameter());
                idx++;
            }
        }
        if (!p_whereClause.equals("") && sqlParameters.toString().length() > 0) {
            p_whereClause = p_whereClause.concat(" AND ").concat(sqlParameters.toString());
        } else if (p_whereClause.equals("") && sqlParameters.toString().length() > 0) {
            p_whereClause = p_whereClause.concat(" WHERE ").concat(sqlParameters.toString());
        }
        sql.append(p_whereClause);
        sql.append(p_groupClause).append(m_sqlOrderBy);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean hasErrors = false;
        try {
            pstmt = DB.prepareStatement(sql.toString(), null);
            idx = 1;
            for (ZynParameter paramInput : activeParams.values()) {
                idx = paramInput.assignToStatement(idx, pstmt);
            }
            rs = pstmt.executeQuery();

            ArrayList<FieldDynamicReport> fields = zreport.getFieldsForReport();
            ArrayList<MZYNVIEWCALC> fieldscalc = zreport.getColumnsCalc();

            for (FieldDynamicReport field : fields) {
                p_table.addColumn(field.getFieldTitle());
            }

            for (MZYNVIEWCALC fcalc : fieldscalc) {
                p_table.addColumn(fcalc.getName());
            }

            while (!isInterrupted() & rs.next()) {
                int row = p_table.getRowCount();

                p_table.setRowCount(row + 1);

                // Columnas fijas
                int colIndex;
                int numOffset = fields.size();
                for (int col = 0; col < fields.size(); col++) {
                    colIndex = col + 1;
                    FieldDynamicReport fdr = fields.get(col);
                    String data = "";
                    
                    /*
                     *  Cambio para aceptar nulos ya que de lo contrario las conversiones no funcionan.
                     *  Zynnia 29 01 2013
                     *  JMF
                     */
                    
                    if (fdr.getDisplayType() == DisplayType.Date || fdr.getDisplayType() == DisplayType.DateTime) {                        
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (rs.getDate(colIndex) == null)
                            data = "";
                        else
                            data = df.format(rs.getDate(colIndex));
                    } else if (fdr.isNumeric()) { // Tipo de dato numerico
                        if (rs.getBigDecimal(colIndex) == null)
                            data = "";
                        else
                            data = Double.toString(rs.getBigDecimal(colIndex).doubleValue());
                    } else { // Tipo de dato nulo
                        if (rs.getString(colIndex) == null)
                            data = "";
                        else
                            data = rs.getString(colIndex);
                    }
                    p_table.setValueAt(data, row, col);

                    TableColumn column = columnModel.getColumnByModelIndex(col);
                    fieldsInReport.put(column, fields.get(col));
                }

                //Columnas calculadas
                for (int col = 0; col < fieldscalc.size(); col++) {
                    MZYNVIEWCALC colcalc = fieldscalc.get(col);
                    colIndex = col + numOffset;
                    int view1 = colcalc.getZYN_VIEW1_ID();
                    X_ZYN_VIEW zview1 = new X_ZYN_VIEW(Env.getCtx(), view1, null);
                    MZYNMODELCOLUMN modcol1 = new MZYNMODELCOLUMN(Env.getCtx(), zview1.getZYN_MODEL_COLUMN_ID(), null);
                    X_AD_Column adcol1 = new X_AD_Column(Env.getCtx(), modcol1.getAD_Column_ID(), null);
                    float value1 = 0;
                    if (indexOfField(fields, adcol1.getColumnName()) != 0) {
                        value1 = rs.getFloat(indexOfField(fields, adcol1.getColumnName()));
                    }

                    int view2 = colcalc.getZYN_VIEW2_ID();
                    X_ZYN_VIEW zview2 = new X_ZYN_VIEW(Env.getCtx(), view2, null);
                    MZYNMODELCOLUMN modcol2 = new MZYNMODELCOLUMN(Env.getCtx(), zview2.getZYN_MODEL_COLUMN_ID(), null);
                    X_AD_Column adcol2 = new X_AD_Column(Env.getCtx(), modcol2.getAD_Column_ID(), null);
                    float value2 = 0;
                    if (indexOfField(fields, adcol2.getColumnName()) != 0) {
                        value2 = rs.getFloat(indexOfField(fields, adcol2.getColumnName()));
                    }

                    float data1 = colcalc.getVALUECALC();
                    char oper = colcalc.getOPCALC().charAt(0);
                    float resCalc = getCalculatedValue(oper, value1, value2, view2, data1);

                    p_table.setValueAt(resCalc, row, colIndex);

                    TableColumn column = columnModel.getColumnByModelIndex(colIndex);
                    FieldDynamicReport fCalc = new FieldDynamicReport(colcalc.getName(),
                                                                      colcalc.isSUM(), colcalc.isTRANSP(),
                                                                      colcalc.isOrderBy(), colcalc.getORDERVIEW());
                    fieldsInReport.put(column, fCalc);
                }
            }
            log.log(Level.FINE, "Info.Worker.run - interrupted={0}", isInterrupted());
            rs.close();
            pstmt.close();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Info.Worker.run - " + sql.toString(), ex);
            hasErrors = true;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
                pstmt = null;
            }
        }

        p_table.autoSize();
        notifyReportsGeneratorListenerPostExecution(hasErrors);
        log.log(Level.INFO, "Refresh despues del query");
    }   //  run

    private float getCalculatedValue(char operator, float value1, float value2, int view2ID, float valueCalc) {
        float resCalc = 0;

        switch (operator) {
            case '%':
                if (view2ID != 0) {
                    resCalc = value1 + (value1 * value2 / 100);
                } else {
                    resCalc = value1 + (value1 * valueCalc / 100);
                }
                break;
            case '&':
                if (view2ID != 0) {
                    resCalc = value1 - (value1 * value2 / 100);
                } else {
                    resCalc = value1 - (value1 * valueCalc / 100);
                }
                break;
            case '+':
                if (view2ID != 0) {
                    resCalc = value1 + value2;
                } else {
                    resCalc = value1 + valueCalc;
                }
                break;
            case '-':
                if (view2ID != 0) {
                    resCalc = value1 - value2;
                } else {
                    resCalc = value1 - valueCalc;
                }
                break;
            case '/':
                if (view2ID != 0) {
                    resCalc = value1 / value2;
                } else {
                    resCalc = value1 / valueCalc;
                }
                break;
            case '*':
                if (view2ID != 0) {
                    resCalc = value1 * value2;
                } else {
                    resCalc = value1 * valueCalc;
                }
                break;
        }
        return resCalc;
    }

    private int indexOfField(ArrayList<FieldDynamicReport> fields, String name) {
        for (int i = 0; i < fields.size(); i++) {
            String name2 = fields.get(i).getColumName();
            if (name2.equals(name)) {
                return i + 1;
            }
        }
        return 0;
    }

    protected void doExport(ExportationAvailable type) {
        if (reportID > 0) {
            Exporter exp = null;
            switch (type) {
                case PDF_EXPORTATION:
                    exp = new PDFExporter(reportID, activeParams, p_table, columnModel, fieldsInReport, printProperties);
                    break;
                case XLS_EXPORTATION:
                    exp = new XLSExporter(reportID, activeParams, p_table, columnModel, fieldsInReport, printProperties);
                    break;
                default:
                    log.log(Level.SEVERE, "Can't execute exportation. No type implemented" + type);
            }
            if (exp != null) {
                exp.export();
            }
        }
    }

    public void addReportsGeneratorListener(IReportsGeneratorListener listener) {
        listeners.add(listener);
    }

    private void notifyReportsGeneratorListenerPreExecution() {
        for (IReportsGeneratorListener listener : listeners) {
            listener.preExecuteReportGenerator();
        }

    }

    private void notifyReportsGeneratorListenerPostExecution(boolean hasErrors) {
        for (IReportsGeneratorListener listener : listeners) {
            listener.postExecuteReportGenerator(hasErrors);
        }

    }
}
