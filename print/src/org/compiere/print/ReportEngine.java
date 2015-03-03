/**
 * ****************************************************************************
 * The contents of this file are subject to the Compiere License Version 1.1 ("License"); You may not use this file
 * except in compliance with the License You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for the specific language governing rights and limitations under the License. The
 * Original Code is Compiere ERP & CRM Smart Business Solution. The Initial Developer of the Original Code is Jorg
 * Janke. Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke. All parts are Copyright (C) 1999-2005
 * ComPiere, Inc. All Rights Reserved. Contributor(s): ______________________________________.
 ****************************************************************************
 */
package org.compiere.print;

import com.qoppa.pdf.*;
import java.awt.print.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.*;
import javax.xml.transform.stream.*;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.*;
import org.apache.ecs.*;
import org.apache.ecs.xhtml.*;
import org.compiere.model.*;
import org.compiere.print.layout.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.zynnia.print.xls.ExcelEngine;

/**
 * Report Engine. For a given PrintFormat, create a Report
 *
 * @author Jorg Janke
 * @version $Id: ReportEngine.java,v 1.64 2005/12/09 05:20:12 jjanke Exp $
 */
public class ReportEngine implements PrintServiceAttributeListener {

    /**
     * Constructor
     *
     * @param ctx context
     * @param pf Print Format
     * @param query Optional Query
     */
    public ReportEngine(Properties ctx, MPrintFormat pf, MQuery query,
                        PrintInfo info) {
        if (pf == null) {
            throw new IllegalArgumentException("ReportEngine - no PrintFormat");
        }
        log.log(Level.INFO, "{0} -- {1}", new Object[]{pf, query});
        m_ctx = ctx;
        //
        m_printFormat = pf;
        m_info = info;
        setQuery(query); // loads Data
    } // ReportEngine
    /**
     * Static Logger
     */
    private static CLogger log = CLogger.getCLogger(ReportEngine.class);
    /**
     * Context
     */
    private Properties m_ctx;
    /**
     * Print Format
     */
    private MPrintFormat m_printFormat;
    /**
     * Print Info
     */
    private PrintInfo m_info;
    /**
     * Query
     */
    private MQuery m_query;
    /**
     * Query Data
     */
    private PrintData m_printData;
    /**
     * Layout
     */
    private LayoutEngine m_layout = null;
    /**
     * Printer
     */
    private String m_printerName = Ini.getProperty(Ini.P_PRINTER);
    /**
     * View
     */
    private View m_view = null;

    /**
     * Set PrintFormat. If Layout was created, re-create layout
     *
     * @param pf print format
     */
    protected void setPrintFormat(MPrintFormat pf) {
        m_printFormat = pf;
        if (m_layout != null) {
            setPrintData();
            m_layout.setPrintFormat(pf, false);
            m_layout.setPrintData(m_printData, m_query, true); // format
            // changes data
        }
        if (m_view != null) {
            m_view.revalidate();
        }
    } // setPrintFormat

    /**
     * Set Query and generate PrintData. If Layout was created, re-create layout
     *
     * @param query query
     */
    protected void setQuery(MQuery query) {
        m_query = query;
        if (query == null) {
            return;
        }
        //
        setPrintData();
        if (m_layout != null) {
            m_layout.setPrintData(m_printData, m_query, true);
        }
        if (m_view != null) {
            m_view.revalidate();
        }
    } // setQuery

    /**
     * Get Query
     *
     * @return query
     */
    public MQuery getQuery() {
        return m_query;
    } // getQuery

    /**
     * Set PrintData for Format restricted by Query. Nothing set if there is no query Sets m_printData
     */
    private void setPrintData() {
        if (m_query == null) {
            return;
        }
        DataEngine de = new DataEngine(m_printFormat.getLanguage());
        setPrintData(de.getPrintData(m_ctx, m_printFormat, m_query));
        // m_printData.dump();
    } // setPrintData

    /**
     * Get PrintData
     *
     * @return print data
     */
    public PrintData getPrintData() {
        return m_printData;
    } // getPrintData

    /**
     * Set PrintData
     *
     * @param printData printData
     */
    public void setPrintData(PrintData printData) {
        if (printData == null) {
            return;
        }
        m_printData = printData;
    } // setPrintData

    /**
     * *************************************************************************
     * Layout
     */
    private void layout() {
        if (m_printFormat == null) {
            throw new IllegalStateException("No print format");
        }
        if (m_printData == null) {
            throw new IllegalStateException(
                    "No print data (Delete Print Format and restart)");
        }
        m_layout = new LayoutEngine(m_printFormat, m_printData, m_query);
    } // layout

    /**
     * Get Layout
     *
     * @return Layout
     */
    protected LayoutEngine getLayout() {
        if (m_layout == null) {
            layout();
        }
        return m_layout;
    } // getLayout

    /**
     * Get PrintFormat (Report) Name
     *
     * @return name
     */
    public String getName() {
        return m_printFormat.getName();
    } // getName

    /**
     * Get PrintFormat
     *
     * @return print format
     */
    public MPrintFormat getPrintFormat() {
        return m_printFormat;
    } // getPrintFormat

    /**
     * Get Print Info
     *
     * @return info
     */
    public PrintInfo getPrintInfo() {
        return m_info;
    } // getPrintInfo

    /**
     * Get PrintLayout (Report) Context
     *
     * @return context
     */
    public Properties getCtx() {
        return m_layout.getCtx();
    } // getCtx

    /**
     * Get Row Count
     *
     * @return row count
     */
    public int getRowCount() {
        return m_printData.getRowCount();
    } // getRowCount

    /**
     * Get Column Count
     *
     * @return column count
     */
    public int getColumnCount() {
        if (m_layout != null) {
            return m_layout.getColumnCount();
        }
        return 0;
    } // getColumnCount

    /**
     * *************************************************************************
     * Get View Panel
     *
     * @return view panel
     */
    public View getView() {
        if (m_layout == null) {
            layout();
        }
        if (m_view == null) {
            m_view = new View(m_layout);
        }
        return m_view;
    } // getView

    /**
     * *************************************************************************
     * Print Report
     */
    public void print() {
        log.info(m_info.toString());
        if (m_layout == null) {
            layout();
        }

        // Paper Attributes: media-printable-area, orientation-requested, media
        PrintRequestAttributeSet prats = m_layout.getPaper().getPrintRequestAttributeSet();
        // add: copies, job-name, priority
        if (m_info.isDocumentCopy() || m_info.getCopies() < 1) {
            prats.add(new Copies(1));
        } else {
            prats.add(new Copies(m_info.getCopies()));
        }
        Locale locale = Language.getLoginLanguage().getLocale();
        prats.add(new JobName(m_printFormat.getName(), locale));
        prats.add(PrintUtil.getJobPriority(m_layout.getNumberOfPages(), m_info.getCopies(), true));

        try {
            // PrinterJob
            PrinterJob job = getPrinterJob(m_info.getPrinterName());
            // job.getPrintService().addPrintServiceAttributeListener(this);
            job.setPageable(m_layout.getPageable(false)); // no copy
            // Dialog
            try {
                if (m_info.isWithDialog() && !job.printDialog(prats)) {
                    return;
                }
            } catch (Exception e) {
                log.log(Level.WARNING,
                        "Operating System Print Issue, check & try again", e);
                return;
            }

            // submit
            boolean printCopy = m_info.isDocumentCopy()
                                && m_info.getCopies() > 1;
            ArchiveEngine.get().archive(m_layout, m_info);
            PrintUtil.print(job, prats, false, printCopy);

            // Document: Print Copies
            if (printCopy) {
                log.log(Level.INFO, "Copy {0}", (m_info.getCopies() - 1));
                prats.add(new Copies(m_info.getCopies() - 1));
                job = getPrinterJob(m_info.getPrinterName());
                // job.getPrintService().addPrintServiceAttributeListener(this);
                job.setPageable(m_layout.getPageable(true)); // Copy
                PrintUtil.print(job, prats, false, false);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "", e);
        }
    } // print

    /**
     * Print Service Attribute Listener.
     *
     * @param psae event
     */
    public void attributeUpdate(PrintServiceAttributeEvent psae) {
        /**
         * PrintEvent on Win32 Printer : \\MAIN\HP LaserJet 5L PrintServiceAttributeSet - length=2 queued-job-count = 0
         * (class javax.print.attribute.standard.QueuedJobCount) printer-is-accepting-jobs = accepting-jobs (class
         * javax.print.attribute.standard.PrinterIsAcceptingJobs) PrintEvent on Win32 Printer : \\MAIN\HP LaserJet 5L
         * PrintServiceAttributeSet - length=1 queued-job-count = 1 (class
         * javax.print.attribute.standard.QueuedJobCount) PrintEvent on Win32 Printer : \\MAIN\HP LaserJet 5L
         * PrintServiceAttributeSet - length=1 queued-job-count = 0 (class
         * javax.print.attribute.standard.QueuedJobCount)
         */
        log.log(Level.FINE, "attributeUpdate - {0}", psae);
        // PrintUtil.dump (psae.getAttributes());
    } // attributeUpdate

    /**
     * Get PrinterJob based on PrinterName
     *
     * @param printerName optional Printer Name
     * @return PrinterJob
     */
    private PrinterJob getPrinterJob(String printerName) {
        PrinterJob pj;
        if (printerName != null && printerName.length() > 0) {
            pj = CPrinter.getPrinterJob(printerName);
            if (pj != null) {
                return pj;
            }
        }
        return CPrinter.getPrinterJob(m_printerName);
    } // getPrinterJob

    /**
     * Show Dialog and Set Paper Optionally re-calculate layout
     */
    public void pageSetupDialog() {
        if (m_layout == null) {
            layout();
        }
        m_layout.pageSetupDialog(getPrinterJob(m_printerName));
        if (m_view != null) {
            m_view.revalidate();
        }
    } // pageSetupDialog

    /**
     * Set Printer (name)
     *
     * @param printerName valid printer name
     */
    public void setPrinterName(String printerName) {
        if (printerName == null) {
            m_printerName = Ini.getProperty(Ini.P_PRINTER);
        } else {
            m_printerName = printerName;
        }
    } // setPrinterName

    /**
     * Get Printer (name)
     *
     * @return printer name
     */
    public String getPrinterName() {
        return m_printerName;
    } // getPrinterName

    /**
     * *************************************************************************
     * Create HTML File
     *
     * @param file file
     * @param onlyTable if false create complete HTML document
     * @param language optional language - if null the default language is used to format nubers/dates
     * @return true if success
     */
    public boolean createHTML(File file, boolean onlyTable, Language language) {
        try {
            Language lang = language;
            if (lang == null) {
                lang = Language.getLoginLanguage();
            }
            FileWriter fw = new FileWriter(file, false);
            return createHTML(new BufferedWriter(fw), onlyTable, lang);
        } catch (FileNotFoundException fnfe) {
            log.log(Level.SEVERE, "(f) - {0}", fnfe.toString());
        } catch (Exception e) {
            log.log(Level.SEVERE, "(f)", e);
        }
        return false;
    } // createHTML

    /**
     * Write HTML to writer
     *
     * @param writer writer
     * @param onlyTable if false create complete HTML document
     * @param language optional language - if null nubers/dates are not formatted
     * @return true if success
     */
    public boolean createHTML(Writer writer, boolean onlyTable,
                              Language language) {
        try {
            table table = new table();
            //
            // for all rows (-1 = header row)
            for (int row = -1; row < m_printData.getRowCount(); row++) {
                tr tr = new tr();
                table.addElement(tr);
                if (row != -1) {
                    m_printData.setRowIndex(row);
                }
                // for all columns
                for (int col = 0; col < m_printFormat.getItemCount(); col++) {
                    MPrintFormatItem item = m_printFormat.getItem(col);
                    if (item.isPrinted()) {
                        // header row
                        if (row == -1) {
                            th th = new th();
                            tr.addElement(th);
                            th.addElement(Util.maskHTML(item.getPrintName(language)));
                        } else {
                            td td = new td();
                            tr.addElement(td);
                            Object obj = m_printData.getNode(new Integer(item.getAD_Column_ID()));
                            if (obj == null) {
                                td.addElement("&nbsp;");
                            } else if (obj instanceof PrintDataElement) {
                                String value = ((PrintDataElement) obj).getValueDisplay(language); // formatted
                                td.addElement(Util.maskHTML(value));
                            } else if (obj instanceof PrintData) {
                                // ignore contained Data
                            } else {
                                log.log(Level.SEVERE, "Element not PrintData(Element) {0}", obj.getClass());
                            }
                        }
                    } // printed
                } // for all columns
            } // for all rows

            //
            PrintWriter w = new PrintWriter(writer);
            if (onlyTable) {
                table.output(w);
            } else {
                XhtmlDocument doc = new XhtmlDocument();
                doc.appendBody(table);
                doc.output(w);
            }
            w.flush();
            w.close();
        } catch (Exception e) {
            log.log(Level.SEVERE, "(w)", e);
        }
        return false;
    } // createHTML

    /**
     * *************************************************************************
     * Create CSV File
     *
     * @param file file
     * @param delimiter delimiter, e.g. comma, tab
     * @param language translation language
     * @return true if success
     */
    public boolean createCSV(File file, char delimiter, Language language) {
        try {
            FileWriter fw = new FileWriter(file, false);
            return createCSV(new BufferedWriter(fw), delimiter, language);
        } catch (FileNotFoundException fnfe) {
            log.log(Level.SEVERE, "(f) - {0}", fnfe.toString());
        } catch (Exception e) {
            log.log(Level.SEVERE, "(f)", e);
        }
        return false;
    } // createCSV

    /**
     * Write CSV to writer
     *
     * @param writer writer
     * @param delimiter delimiter, e.g. comma, tab
     * @param language translation language
     * @return true if success
     */
    public boolean createCSV(Writer writer, char delimiter, Language language) {
        if (delimiter == 0) {
            delimiter = '\t';
        }
        try {
            // for all rows (-1 = header row)
            for (int row = -1; row < m_printData.getRowCount(); row++) {
                StringBuffer sb = new StringBuffer();
                if (row != -1) {
                    m_printData.setRowIndex(row);
                }

                // for all columns
                boolean first = true; // first column to print
                for (int col = 0; col < m_printFormat.getItemCount(); col++) {
                    MPrintFormatItem item = m_printFormat.getItem(col);
                    if (item.isPrinted()) {
                        // column delimiter (comma or tab)
                        if (first) {
                            first = false;
                        } else {
                            sb.append(delimiter);
                        }
                        // header row
                        if (row == -1) {
                            createCSVvalue(sb, delimiter, m_printFormat.getItem(col).getPrintName(language));
                        } else {
                            Object obj = m_printData.getNode(new Integer(item.getAD_Column_ID()));
                            String data = "";
                            if (obj == null)
								; else if (obj instanceof PrintDataElement) {
                                PrintDataElement pde = (PrintDataElement) obj;
                                if (pde.isPKey()) {
                                    data = pde.getValueAsString();
                                } else {
                                    data = pde.getValueDisplay(language); // formatted
                                }
                            } else if (obj instanceof PrintData) {
                            } else {
                                log.log(Level.SEVERE, "Element not PrintData(Element) {0}", obj.getClass());
                            }
                            createCSVvalue(sb, delimiter, data);
                        }
                    } // printed
                } // for all columns
                writer.write(sb.toString());
                writer.write(Env.NL);
            } // for all rows
            //
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.log(Level.SEVERE, "(w)", e);
        }
        return false;
    } // createCSV

    /**
     * Add Content to CSV string. Encapsulate/mask content in " if required
     *
     * @param sb StringBuffer to add to
     * @param delimiter delimiter
     * @param content column value
     */
    private void createCSVvalue(StringBuffer sb, char delimiter, String content) {
        // nothing to add
        if (content == null || content.length() == 0) {
            return;
        }
        //
        boolean needMask = false;
        StringBuffer buff = new StringBuffer();
        char chars[] = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '"') {
                needMask = true;
                buff.append(c); // repeat twice
            } // mask if any control character
            else if (!needMask
                     && (c == delimiter || !Character.isLetterOrDigit(c))) {
                needMask = true;
            }
            buff.append(c);
        }

        // Optionally mask value
        if (needMask) {
            sb.append('"').append(buff).append('"');
        } else {
            sb.append(buff);
        }
    } // addCSVColumnValue

    /**
     * *************************************************************************
     * Create XML File
     *
     * @param file file
     * @return true if success
     */
    public boolean createXML(File file) {
        try {
            FileWriter fw = new FileWriter(file, false);
            return createXML(new BufferedWriter(fw));
        } catch (FileNotFoundException fnfe) {
            log.log(Level.SEVERE, "(f) - {0}", fnfe.toString());
        } catch (Exception e) {
            log.log(Level.SEVERE, "(f)", e);
        }
        return false;
    } // createXML

    /**
     * Write XML to writer
     *
     * @param writer writer
     * @return true if success
     */
    public boolean createXML(Writer writer) {
        try {
            m_printData.createXML(new StreamResult(writer));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.log(Level.SEVERE, "(w)", e);
        }
        return false;
    } // createXML

    public boolean createXLS(File file, Language language) {
        try {
            WorkbookSettings settings = new WorkbookSettings();
            settings.setUseTemporaryFileDuringWrite(true);
            WritableWorkbook workbook = Workbook.createWorkbook(file, settings);
            //WritableSheet sheet = workbook.createSheet(m_printFormat.getName(),0);
            WritableSheet sheet;

            /*
             * Modificaciones para contemplar los reportes múltiples Zynnia - 02/02/2012
             *
             */

            int indsheet = 0;

            DataEngine de = new DataEngine(m_printFormat.getLanguage());

            PrintData printData;


            // printData tiene el resultado de la consulta a nivel general dependiendo de los registros
            // que de son la cantidad de reportes en el reporte múltiple que voy a tener por lo tanto
            // debería de llamar a lo que ahora esta con un query diferenciado por ejemplo que determine cada
            // uno de los reportes del múltiple.

            if (m_printFormat.getName().equals("Libro Mayor")) {

                printData = de.getPrintData2(m_ctx, m_printFormat, m_query);

                PrintDataElement pde = null;
                Object obj;

                for (int fil = 0; fil < printData.getRowCount(); fil++) {

                    sheet = workbook.createSheet(m_printFormat.getName(), indsheet);
                    indsheet++;
                    createXLSLabels(sheet, language, m_printFormat, 0);

                    MQuery m_query2 = new MQuery();
                    m_query2.setTableName(m_query.getTableName());

                    obj = printData.getNode(3 * fil);

                    if (obj == null)
                            ; else if (obj instanceof PrintDataElement) {
                        pde = (PrintDataElement) obj;
                    }

                    String valueStr = pde.getValueDisplay(language);
                    m_query2.addRestriction("VALUE", "=", valueStr);


                    createRecursiveXLSData(workbook, sheet, language, m_printFormat, m_query2, 0);
                    ajustarAnchoColumnas(sheet);

                }

            } else {

                sheet = workbook.createSheet(m_printFormat.getName(), 0);
//                createXLSLabels(sheet, language, m_printFormat, 0);
//                createRecursiveXLSData(workbook, sheet, language, m_printFormat, m_query, 0);
//                ajustarAnchoColumnas(sheet);
                ExcelEngine engine = new ExcelEngine(workbook, m_printFormat, m_printData, m_query, sheet, language);
                engine.layout();

            }


            workbook.write();
            workbook.close();

        } catch (Exception e) {
            log.log(Level.SEVERE, "(f)", e);
        }
        return false;
    }

    private void createXLSHeader() {
    }

    private void createXLSLabels(WritableSheet sheet, Language language, MPrintFormat printFormat, int rowIdx)
            throws Exception {
        int colExcel = 0;
        for (int col = 0; col < printFormat.getItemCount(); col++) {
            MPrintFormatItem item = printFormat.getItem(col);
            if (item.isPrinted()
                && item.isTypeField()) {
                String titulo = printFormat.getItem(col).getPrintName(
                        language);
                if (titulo == null) {
                    titulo = item.getName();
                }
                Label label = new Label(colExcel, rowIdx, titulo,
                                        getFormatoCabeceraXLS());
                sheet.addCell(label);
                colExcel++;
            }
        }
    }

    private WritableCellFormat getFormatoCabeceraXLS() throws Exception {
        // Fuente de la cabecera de la tabla
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
                                           WritableFont.BOLD);
        wf.setColour(Colour.BLACK);
        // Creo un formato y le asigno la fuente anterior
        WritableCellFormat wcf = new WritableCellFormat(wf);
        wcf.setBackground(Colour.GREY_25_PERCENT);
        return wcf;
    }

    private WritableCellFormat getFormatoDatosXLS() throws Exception {
        // Fuente de la cabecera de la tabla
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10);
        wf.setColour(Colour.BLACK);
        // Creo un formato y le asigno la fuente anterior
        WritableCellFormat wcf = new WritableCellFormat(wf);
        wcf.setShrinkToFit(false);
        return wcf;
    }

    private int createRecursiveXLSData(WritableWorkbook workbook, WritableSheet sheet, Language language, MPrintFormat printFormat, MQuery query, int rowIndex) throws WriteException, Exception {
        DateFormat df = new DateFormat("dd/MM/yyyy");
        DataEngine de = new DataEngine(printFormat.getLanguage());
        PrintData printData = de.getPrintData(m_ctx, printFormat, query);
        // Las inserciones se realizan por columna
        boolean esTexto;
        int printedRows = 0, actualCol = 0;
        int actualRow;

        // Para cada una de las filas de datos del informe
        for (int col = 0; col < printFormat.getItemCount(); col++) {
            esTexto = false;
            MPrintFormatItem item = printFormat.getItem(col);
            // Considero aquellos format items te tipo field y que sean
            // impresos.
            if (item.isTypeField() && item.isPrinted()) {
                // Para cada una de las filas de datos del informe
                boolean rowPrinted = false;
                for (int fil = 0; fil < printData.getRowCount(); fil++) {
                    actualRow = rowIndex + fil;
                    printData.setRowIndex(fil);

                    // Obtengo el valor del String
                    Object obj = printData.getNode(new Integer(item.getAD_Column_ID()));
                    if (obj == null)
                            ; else if (obj instanceof PrintDataElement) {
                        rowPrinted = true;
                        PrintDataElement pde = (PrintDataElement) obj;
                        WritableCell cell;
                        // Tipo de dato fecha
                        if (pde.getDisplayType() == DisplayType.Date) {
                            java.sql.Date date = (java.sql.Date) pde.getValue();
                            WritableCellFormat cf = new WritableCellFormat(df);
                            cell = new jxl.write.DateTime(actualCol, actualRow + 1, new Timestamp(date.getTime()), cf, DateTime.GMT);
                        } else if (pde.getDisplayType() == DisplayType.DateTime) {
                            System.out.println(pde.getValue());
                            if (pde.getValue() instanceof java.sql.Date) {
                                java.sql.Date time = (java.sql.Date) pde.getValue();
                                WritableCellFormat cf = new WritableCellFormat(df);
                                cell = new jxl.write.DateTime(actualCol, actualRow + 1, new Timestamp(time.getTime()), cf, DateTime.GMT);
                            } else {
                                Timestamp time = (Timestamp) pde.getValue();
                                //i+1 porque en la primer fila van los labels
                                WritableCellFormat cf = new WritableCellFormat(df);
                                cell = new jxl.write.DateTime(actualCol, actualRow + 1, time, cf, DateTime.GMT);
                            }
                            WritableCellFormat cf = new WritableCellFormat(df);
                            cell.setCellFormat(cf);
                        } // Tipo de dato numerico
                        else if (pde.isNumeric()) {
                            BigDecimal number = (BigDecimal) pde.getValue();
                            cell = new jxl.write.Number(actualCol, actualRow + 1, number.doubleValue());
                        } // Tipo de dato nulo
                        else if (pde.isNull()) {
                            cell = new jxl.write.Label(actualCol, actualRow + 1, "");
                        } else if (pde.isYesNo()) {
                            java.lang.Boolean yesNo = (java.lang.Boolean) pde.getValue();
                            if (yesNo.booleanValue()) {
                                cell = new jxl.write.Label(actualCol, actualRow + 1, "SI");
                            } else {
                                cell = new jxl.write.Label(actualCol, actualRow + 1, "NO");
                            }
                        } // Tipo de dato string (pueden haber números que son
                        // representados por Strings)
                        // Se los va a considerar para que se les de el formato
                        // adecuado.
                        // Si se puede parsear a Int se lo parsea
                        else {
                            // COMPARO SI ES UN ENTERO DENTRO DE UN STRING
                            String valueStr = pde.getValueDisplay(language);
                            // Comparo con una expresion regular si el String es
                            // entero
                            if (valueStr.matches("[0-9]+") && !esTexto) {
                                int val = Integer.valueOf(valueStr).intValue();
                                BigDecimal number = new BigDecimal(val);
                                cell = new jxl.write.Number(actualCol, actualRow + 1, number.doubleValue());
                            } else if (valueStr.matches("[0-9]+,[0-9]+")
                                       || valueStr.matches("[0-9]+\\.[0-9]+")
                                          && !esTexto) {
                                double valD = Double.valueOf(valueStr).doubleValue();
                                cell = new jxl.write.Number(actualCol, actualRow + 1, valD);
                            } else {
                                cell = new jxl.write.Label(actualCol, actualRow + 1, valueStr);
                                esTexto = true;
                            }
                        }
                        sheet.addCell(cell);
                        if (actualCol == 0) {
                            printedRows++;
                        }
                    }
                }// fin for rows
                if (rowPrinted) {
                    actualCol++;
                }

            } else if (item.isTypePrintFormat()) { // fin if tipo field and isprinted
                MPrintFormat childFormat = MPrintFormat.get(getCtx(), item.getAD_PrintFormatChild_ID(), true);
                if (childFormat != null) {
                    createXLSLabels(sheet, language, childFormat, rowIndex + 2);
                    MQuery childQuery = getQueryFor2(item, childFormat, printData);
                    rowIndex = createRecursiveXLSData(workbook, sheet, language, childFormat, childQuery, rowIndex + 3);
                }
            }
        }// for fin col
        return rowIndex + printedRows;
    }

    private MQuery getQueryFor2(MPrintFormatItem item, MPrintFormat format, PrintData printData) {
        int AD_Column_ID = item.getAD_Column_ID();

        Object obj = printData.getNode(new Integer(AD_Column_ID));
        //	Object obj = data.getNode(item.getColumnName());	//	slower
        if (obj == null) {
            printData.dumpHeader();
            printData.dumpCurrentRow();
            return null;
        }
        PrintDataElement dataElement = (PrintDataElement) obj;
        String recordString = dataElement.getValueKey();
        if (recordString == null || recordString.length() == 0) {
            printData.dumpHeader();
            printData.dumpCurrentRow();
            return null;
        }
        int Record_ID;
        try {
            Record_ID = Integer.parseInt(recordString);
        } catch (Exception e) {
            printData.dumpCurrentRow();
            log.log(Level.SEVERE, "Invalid Record Key - {0} ({1}) - AD_Column_ID={2} - {3}", new Object[]{recordString, e.getMessage(), AD_Column_ID, item});
            return null;
        }
        MQuery query = new MQuery(format.getAD_Table_ID());
        query.addRestriction(item.getColumnName(), MQuery.EQUAL, new Integer(Record_ID));
        return query;
    }

    private MQuery getQueryFor(MPrintFormatItem item, MPrintFormat format) {
        int AD_Column_ID = item.getAD_Column_ID();

        Object obj = m_printData.getNode(new Integer(AD_Column_ID));
        //	Object obj = data.getNode(item.getColumnName());	//	slower
        if (obj == null) {
            m_printData.dumpHeader();
            m_printData.dumpCurrentRow();
            return null;
        }
        PrintDataElement dataElement = (PrintDataElement) obj;
        String recordString = dataElement.getValueKey();
        if (recordString == null || recordString.length() == 0) {
            m_printData.dumpHeader();
            m_printData.dumpCurrentRow();
            return null;
        }
        int Record_ID;
        try {
            Record_ID = Integer.parseInt(recordString);
        } catch (Exception e) {
            m_printData.dumpCurrentRow();
            log.log(Level.SEVERE, "Invalid Record Key - {0} ({1}) - AD_Column_ID={2} - {3}", new Object[]{recordString, e.getMessage(), AD_Column_ID, item});
            return null;
        }
        MQuery query = new MQuery(format.getAD_Table_ID());
        query.addRestriction(item.getColumnName(), MQuery.EQUAL, new Integer(Record_ID));
        return query;
    }

    private void createXLSData(WritableSheet sheet, Language language)
            throws Exception {
        int col = 0;
        DateFormat df = new DateFormat("dd/MM/yyyy");
        // Las inserciones se realizan por columna
        boolean esTexto;
        for (int j = 0; j < m_printFormat.getItemCount(); j++) {
            esTexto = false;
            MPrintFormatItem item = m_printFormat.getItem(j);
            // Considero aquellos format items te tipo field y que sean
            // impresos.
            if (item.getPrintFormatType().equals(
                    MPrintFormatItem.PRINTFORMATTYPE_Field)
                && item.isPrinted()) {
                // Para cada una de las filas de datos del informe
                for (int i = 0; i < m_printData.getRowCount(); i++) {
                    m_printData.setRowIndex(i);
                    // Obtengo el valor del String
                    Object obj = m_printData.getNode(new Integer(item.getAD_Column_ID()));
                    String data = "";
                    if (obj == null)
						; else if (obj instanceof PrintDataElement) {
                        PrintDataElement pde = (PrintDataElement) obj;
                        if (pde.isPKey()) {
                            data = pde.getValueAsString();
                        } else {
                            data = pde.getValueDisplay(language); // formatted
                        }
                        WritableCell cell;
                        // Tipo de dato fecha
                        if (pde.getDisplayType() == DisplayType.Date) {
                            java.sql.Date date = (java.sql.Date) pde.getValue();
                            cell = new jxl.write.DateTime(col, i + 1, new Timestamp(date.getTime()));
                            WritableCellFormat cf = new WritableCellFormat(df);
                            cell.setCellFormat(cf);
                        } else if (pde.getDisplayType() == DisplayType.DateTime) {
                            System.out.println(pde.getValue());
                            if (pde.getValue() instanceof java.sql.Date) {
                                java.sql.Date time = (java.sql.Date) pde.getValue();
                                cell = new jxl.write.DateTime(col, i + 1, new Timestamp(time.getTime()));
                            } else {
                                Timestamp time = (Timestamp) pde.getValue();
                                //i+1 porque en la primer fila van los labels
                                cell = new jxl.write.DateTime(col, i + 1, time);
                            }
                            WritableCellFormat cf = new WritableCellFormat(df);
                            cell.setCellFormat(cf);
                        } // Tipo de dato numerico
                        else if (pde.isNumeric()) {
                            BigDecimal number = (BigDecimal) pde.getValue();
                            cell = new jxl.write.Number(col, i + 1, number.doubleValue());
                        } // Tipo de dato nulo
                        else if (pde.isNull()) {
                            cell = new jxl.write.Label(col, i + 1, "");
                        } else if (pde.isYesNo()) {
                            java.lang.Boolean yesNo = (java.lang.Boolean) pde.getValue();
                            if (yesNo.booleanValue()) {
                                cell = new jxl.write.Label(col, i + 1, "SI");
                            } else {
                                cell = new jxl.write.Label(col, i + 1, "NO");
                            }
                        } // Tipo de dato string (pueden haber números que son
                        // representados por Strings)
                        // Se los va a considerar para que se les de el formato
                        // adecuado.
                        // Si se puede parsear a Int se lo parsea
                        else {
                            // COMPARO SI ES UN ENTERO DENTRO DE UN STRING
                            String valueStr = pde.getValueDisplay(language);
                            // Comparo con una expresion regular si el String es
                            // entero
                            if (valueStr.matches("[0-9]+") && !esTexto) {
                                int val = Integer.valueOf(valueStr).intValue();
                                BigDecimal number = new BigDecimal(val);
                                cell = new jxl.write.Number(col, i + 1, number.doubleValue());
                            } else if (valueStr.matches("[0-9]+,[0-9]+")
                                       || valueStr.matches("[0-9]+\\.[0-9]+")
                                          && !esTexto) {
                                double valD = Double.valueOf(valueStr).doubleValue();
                                cell = new jxl.write.Number(col, i + 1, valD);
                            } else {
                                cell = new jxl.write.Label(col, i + 1, valueStr);
                                esTexto = true;
                            }
                        }
                        sheet.addCell(cell);
                    }
                }// fin for rows
                col++;
            }// fin if tipo field and isprinted
        }// for fin col
    }

    private void ajustarAnchoColumnas(WritableSheet sheet) throws Exception {
        // A cada columna le pongo autosize en true
        int cols = sheet.getColumns();
        for (int c = 0; c < cols; c++) {
            CellView cv = new CellView();
            int width0 = cv.getSize();
            // El tamaño se autoajusta pero muchas veces no alcanza (hay que
            // ampliar aun mas el ancho). Para calcular el ancho máximo habría
            // que usar métricas de fuentes y setear asi columnview.
            cv.setAutosize(true);
            int width = cv.getSize();
            // Al ancho automatico lo aumento porque siempre se queda corto para
            // el dato mas "ancho"
            cv.setSize(width + 30);
            WritableCellFormat wcf = getFormatoDatosXLS();
            cv.setFormat(wcf);
            sheet.setColumnView(c, cv);
        }
    }

    /**
     * *************************************************************************
     * Create PDF file. (created in temporary storage)
     *
     * @return PDF file
     */
    public File getPDF() {
        return getPDF(null);
    } // getPDF

    /**
     * Create PDF file.
     *
     * @param file file
     * @return PDF file
     */
    public File getPDF(File file) {
        try {
            if (file == null) {
                file = File.createTempFile("ReportEngine", ".pdf");
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "", e);
        }
        if (createPDF(file)) {
            return file;
        }
        return null;
    } // getPDF

    /**
     * Create PDF File
     *
     * @param file file
     * @return true if success
     */
    public boolean createPDF(File file) {
        String fileName;
        URI uri;

        try {
            if (file == null) {
                file = File.createTempFile("ReportEngine", ".pdf");
            }
            fileName = file.getAbsolutePath();
            uri = file.toURI();
            if (file.exists()) {
                file.delete();
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "file", e);
            return false;
        }

        log.fine(uri.toString());

        try {
            if (m_layout == null) {
                layout();
            }
            ArchiveEngine.get().archive(m_layout, m_info);
            Document.getPDFAsFile(fileName, m_layout.getPageable(false));
        } catch (Exception e) {
            log.log(Level.SEVERE, "PDF", e);
            return false;
        }

        File file2 = new File(fileName);
        log.log(Level.INFO, "{0} - {1}", new Object[]{file2.getAbsolutePath(), file2.length()});
        return file2.exists();
    } // createPDF

    /**
     * Create PDF as Data array
     *
     * @return pdf data
     */
    public byte[] createPDFData() {
        try {
            if (m_layout == null) {
                layout();
            }
            return Document.getPDFAsArray(m_layout.getPageable(false));
        } catch (Exception e) {
            log.log(Level.SEVERE, "PDF", e);
        }
        return null;
    } // createPDFData

    /**
     * *************************************************************************
     * Create PostScript File
     *
     * @param file file
     * @return true if success
     */
    public boolean createPS(File file) {
        try {
            return createPS(new FileOutputStream(file));
        } catch (FileNotFoundException fnfe) {
            log.log(Level.SEVERE, "(f) - {0}", fnfe.toString());
        } catch (Exception e) {
            log.log(Level.SEVERE, "(f)", e);
        }
        return false;
    } // createPS

    /**
     * Write PostScript to writer
     *
     * @param fos file output stream
     * @return true if success
     */
    public boolean createPS(FileOutputStream fos) {
        try {
            String outputMimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
            DocFlavor docFlavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
            StreamPrintServiceFactory[] spsfactories = StreamPrintServiceFactory.lookupStreamPrintServiceFactories(docFlavor,
                                                                                                                   outputMimeType);
            if (spsfactories.length == 0) {
                log.log(Level.SEVERE, "(fos) - No StreamPrintService");
                return false;
            }
            // just use first one - sun.print.PSStreamPrinterFactory
            // System.out.println("- " + spsfactories[0]);
            StreamPrintService sps = spsfactories[0].getPrintService(fos);
            // get format
            if (m_layout == null) {
                layout();
            }
            // print it
            sps.createPrintJob().print(m_layout.getPageable(false),
                                       new HashPrintRequestAttributeSet());
            //
            fos.flush();
            fos.close();
        } catch (Exception e) {
            log.log(Level.SEVERE, "(fos)", e);
        }
        return false;
    } // createPS

    /**
     * *************************************************************************
     * Get Report Engine for process info
     *
     * @param ctx context
     * @param pi process info with AD_PInstance_ID
     * @return report engine or null
     */
    static public ReportEngine get(Properties ctx, ProcessInfo pi) {
        int AD_Client_ID = Env.getAD_Client_ID(ctx);
        //
        int AD_Table_ID = 0;
        int AD_ReportView_ID = 0;
        String TableName = null;
        String whereClause = "";
        int AD_PrintFormat_ID = 0;
        boolean IsForm = false;
        int Client_ID = -1;

        // Get AD_Table_ID and TableName
        String sql = "SELECT rv.AD_ReportView_ID,rv.WhereClause,"
                     + " t.AD_Table_ID,t.TableName, pf.AD_PrintFormat_ID, pf.IsForm, pf.AD_Client_ID "
                     + "FROM AD_PInstance pi"
                     + " INNER JOIN AD_Process p ON (pi.AD_Process_ID=p.AD_Process_ID)"
                     + " INNER JOIN AD_ReportView rv ON (p.AD_ReportView_ID=rv.AD_ReportView_ID)"
                     + " INNER JOIN AD_Table t ON (rv.AD_Table_ID=t.AD_Table_ID)"
                     + " LEFT OUTER JOIN AD_PrintFormat pf ON (p.AD_ReportView_ID=pf.AD_ReportView_ID AND pf.AD_Client_ID IN (0,?)) "
                     + "WHERE pi.AD_PInstance_ID=? " // #2
                     + "ORDER BY pf.AD_Client_ID DESC, pf.IsDefault DESC"; // own
        // first
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, AD_Client_ID);
            pstmt.setInt(2, pi.getAD_PInstance_ID());
            ResultSet rs = pstmt.executeQuery();
            // Just get first
            if (rs.next()) {
                AD_ReportView_ID = rs.getInt(1); // required
                whereClause = rs.getString(2);
                if (rs.wasNull()) {
                    whereClause = "";
                }
                //
                AD_Table_ID = rs.getInt(3);
                TableName = rs.getString(4); // required for query
                AD_PrintFormat_ID = rs.getInt(5); // required
                IsForm = "Y".equals(rs.getString(6)); // required
                Client_ID = rs.getInt(7);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e1) {
            log.log(Level.SEVERE, "(1) - " + sql, e1);
        }
        // Nothing found
        if (AD_ReportView_ID == 0) {
            // Check Print format in Report Directly
            sql = "SELECT t.AD_Table_ID,t.TableName, pf.AD_PrintFormat_ID, pf.IsForm "
                  + "FROM AD_PInstance pi"
                  + " INNER JOIN AD_Process p ON (pi.AD_Process_ID=p.AD_Process_ID)"
                  + " INNER JOIN AD_PrintFormat pf ON (p.AD_PrintFormat_ID=pf.AD_PrintFormat_ID)"
                  + " INNER JOIN AD_Table t ON (pf.AD_Table_ID=t.AD_Table_ID) "
                  + "WHERE pi.AD_PInstance_ID=?";
            try {
                PreparedStatement pstmt = DB.prepareStatement(sql, null);
                pstmt.setInt(1, pi.getAD_PInstance_ID());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    whereClause = "";
                    AD_Table_ID = rs.getInt(1);
                    TableName = rs.getString(2); // required for query
                    AD_PrintFormat_ID = rs.getInt(3); // required
                    IsForm = "Y".equals(rs.getString(4)); // required
                    Client_ID = AD_Client_ID;
                }
                rs.close();
                pstmt.close();
            } catch (SQLException e1) {
                log.log(Level.SEVERE, "(2) - " + sql, e1);
            }
            if (AD_PrintFormat_ID == 0) {
                log.log(Level.SEVERE, "Report Info NOT found AD_PInstance_ID={0},AD_Client_ID={1}", new Object[]{pi.getAD_PInstance_ID(), AD_Client_ID});
                return null;
            }
        }

        // Create Query from Parameters
        MQuery query;
        if (IsForm && pi.getRecord_ID() != 0) // Form = one record
        {
            query = MQuery.getEqualQuery(TableName + "_ID", pi.getRecord_ID());
        } else {
            query = MQuery.get(ctx, pi.getAD_PInstance_ID(), TableName);
        }

        // Add to static where clause from ReportView
        if (whereClause.length() != 0) {
            query.addRestriction(whereClause);
        }

        // Get PrintFormat
        MPrintFormat format = null;
        if (AD_PrintFormat_ID != 0) {
            // We have a PrintFormat with the correct Client
            if (Client_ID == AD_Client_ID) {
                format = MPrintFormat.get(ctx, AD_PrintFormat_ID, false);
            } else {
                format = MPrintFormat.copyToClient(ctx, AD_PrintFormat_ID,
                                                   AD_Client_ID);
            }
        }
        if (format != null && format.getItemCount() == 0) {
            log.log(Level.INFO, "No Items - recreating:  {0}", format);
            format.delete(true);
            format = null;
        }
        // Create it
        if (format == null && AD_ReportView_ID != 0) {
            format = MPrintFormat.createFromReportView(ctx, AD_ReportView_ID,
                                                       pi.getTitle());
        }
        if (format == null) {
            return null;
        }
        //
        PrintInfo info = new PrintInfo(pi);
        info.setAD_Table_ID(AD_Table_ID);
        info.setPrinterName(format.getPrinterName());

        return new ReportEngine(ctx, format, query, info);
    } // get
    /**
     * **********************************************************************
     */
    public static final int ORDER = 0;
    public static final int SHIPMENT = 1;
    public static final int INVOICE = 2;
    public static final int PROJECT = 3;
    public static final int RFQ = 4;
    //
    public static final int REMITTANCE = 5;
    public static final int CHECK = 6;
    public static final int DUNNING = 7;
    private static final String[] DOC_TABLES = new String[]{
        "C_Order_Header_v", "M_InOut_Header_v", "C_Invoice_Header_v",
        "C_Project_Header_v", "C_RfQResponse_v", "C_PaySelection_Check_v",
        "C_PaySelection_Check_v", "C_DunningRunEntry_v"};
    private static final String[] DOC_BASETABLES = new String[]{"C_Order",
        "M_InOut", "C_Invoice", "C_Project", "C_RfQResponse",
        "C_PaySelectionCheck", "C_PaySelectionCheck", "C_DunningRunEntry"};
    private static final String[] DOC_IDS = new String[]{"C_Order_ID",
        "M_InOut_ID", "C_Invoice_ID", "C_Project_ID", "C_RfQResponse_ID",
        "C_PaySelectionCheck_ID", "C_PaySelectionCheck_ID",
        "C_DunningRunEntry_ID"};
    private static final int[] DOC_TABLE_ID = new int[]{
        X_C_Order.getTableId(X_C_Order.Table_Name),
        X_M_InOut.getTableId(X_M_InOut.Table_Name),
        X_C_Invoice.getTableId(X_C_Invoice.Table_Name),
        X_C_Project.getTableId(X_C_Project.Table_Name),
        X_C_RfQResponse.getTableId(X_C_RfQResponse.Table_Name),
        X_C_PaySelectionCheck.getTableId(X_C_PaySelectionCheck.Table_Name),
        X_C_PaySelectionCheck.getTableId(X_C_PaySelectionCheck.Table_Name),
        X_C_DunningRunEntry.getTableId(X_C_DunningRunEntry.Table_Name)};

    /**
     * *************************************************************************
     * Get Document Print Engine for Document Type.
     *
     * @param ctx context
     * @param type document type
     * @param Record_ID id
     * @return Report Engine or null
     */
    public static ReportEngine get(Properties ctx, int type, int Record_ID) {
        return get(ctx, type, Record_ID, null);
    }

    public static ReportEngine get(Properties ctx, int type, int Record_ID,
                                   String trxName) {
        // Order - Print Shipment or Invoice
        if (type == ORDER) {
            int[] what = getDocumentWhat(Record_ID);
            if (what != null) {
                type = what[0];
                Record_ID = what[1];
            }
        } // Order
        //
        String JobName = DOC_BASETABLES[type] + "_Print";
        int AD_PrintFormat_ID = 0;
        int C_BPartner_ID = 0;
        String DocumentNo = null;
        int copies = 1;

        // Language
        MClient client = MClient.get(ctx);
        Language language = client.getLanguage();
        // Get Document Info
        String sql;
        if (type == CHECK) {
            sql = "SELECT bad.Check_PrintFormat_ID," // 1
                  + "	c.IsMultiLingualDocument,bp.AD_Language,bp.C_BPartner_ID,d.DocumentNo " // 2..5
                  + "FROM C_PaySelectionCheck d"
                  + " INNER JOIN C_PaySelection ps ON (d.C_PaySelection_ID=ps.C_PaySelection_ID)"
                  + " INNER JOIN C_BankAccountDoc bad ON (ps.C_BankAccount_ID=bad.C_BankAccount_ID AND d.PaymentRule=bad.PaymentRule)"
                  + " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
                  + " INNER JOIN C_BPartner bp ON (d.C_BPartner_ID=bp.C_BPartner_ID) "
                  + "WHERE d.C_PaySelectionCheck_ID=?"; // info from
        } // BankAccount
        else if (type == DUNNING) {
            sql = "SELECT dl.Dunning_PrintFormat_ID,"
                  + " c.IsMultiLingualDocument,bp.AD_Language,bp.C_BPartner_ID,dr.DunningDate "
                  + "FROM C_DunningRunEntry d"
                  + " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
                  + " INNER JOIN C_BPartner bp ON (d.C_BPartner_ID=bp.C_BPartner_ID)"
                  + " INNER JOIN C_DunningRun dr ON (d.C_DunningRun_ID=dr.C_DunningRun_ID)"
                  + " INNER JOIN C_DunningLevel dl ON (dr.C_DunningLevel_ID=dr.C_DunningLevel_ID) "
                  + "WHERE d.C_DunningRunEntry_ID=?"; // info from Dunning
        } else if (type == REMITTANCE) {
            sql = "SELECT pf.Remittance_PrintFormat_ID,"
                  + " c.IsMultiLingualDocument,bp.AD_Language,bp.C_BPartner_ID,d.DocumentNo "
                  + "FROM C_PaySelectionCheck d"
                  + " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
                  + " INNER JOIN AD_PrintForm pf ON (c.AD_Client_ID=pf.AD_Client_ID)"
                  + " INNER JOIN C_BPartner bp ON (d.C_BPartner_ID=bp.C_BPartner_ID) "
                  + "WHERE d.C_PaySelectionCheck_ID=?" // info from
                  // PrintForm
                  + " AND pf.AD_Org_ID IN (0,d.AD_Org_ID) ORDER BY pf.AD_Org_ID DESC";
        } else if (type == PROJECT) {
            sql = "SELECT pf.Project_PrintFormat_ID,"
                  + " c.IsMultiLingualDocument,bp.AD_Language,bp.C_BPartner_ID,d.Value "
                  + "FROM C_Project d"
                  + " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
                  + " INNER JOIN AD_PrintForm pf ON (c.AD_Client_ID=pf.AD_Client_ID)"
                  + " LEFT OUTER JOIN C_BPartner bp ON (d.C_BPartner_ID=bp.C_BPartner_ID) "
                  + "WHERE d.C_Project_ID=?" // info from PrintForm
                  + " AND pf.AD_Org_ID IN (0,d.AD_Org_ID) ORDER BY pf.AD_Org_ID DESC";
        } else if (type == RFQ) {
            sql = "SELECT COALESCE(t.AD_PrintFormat_ID, pf.AD_PrintFormat_ID),"
                  + " c.IsMultiLingualDocument,bp.AD_Language,bp.C_BPartner_ID,rr.Name "
                  + "FROM C_RfQResponse rr"
                  + " INNER JOIN C_RfQ r ON (rr.C_RfQ_ID=r.C_RfQ_ID)"
                  + " INNER JOIN C_RfQ_Topic t ON (r.C_RfQ_Topic_ID=t.C_RfQ_Topic_ID)"
                  + " INNER JOIN AD_Client c ON (rr.AD_Client_ID=c.AD_Client_ID)"
                  + " INNER JOIN C_BPartner bp ON (rr.C_BPartner_ID=bp.C_BPartner_ID),"
                  + " AD_PrintFormat pf "
                  + "WHERE pf.AD_Client_ID IN (0,rr.AD_Client_ID)"
                  + " AND pf.AD_Table_ID=725 AND pf.IsTableBased='N'" // from
                  // RfQ
                  // PrintFormat
                  + " AND rr.C_RfQResponse_ID=? " // Info from RfQTopic
                  + "ORDER BY t.AD_PrintFormat_ID, pf.AD_Client_ID DESC, pf.AD_Org_ID DESC";
        } else // Get PrintFormat from Org or 0 of document client
        {
            sql = "SELECT pf.Order_PrintFormat_ID,pf.Shipment_PrintFormat_ID," // 1..2
                  // Prio: 1. BPartner 2. DocType, 3. PrintFormat (Org) // see
                  // InvoicePrint
                  + " COALESCE (bp.Invoice_PrintFormat_ID,dt.AD_PrintFormat_ID,pf.Invoice_PrintFormat_ID)," // 3
                  + " pf.Project_PrintFormat_ID, pf.Remittance_PrintFormat_ID," // 4..5
                  + " c.IsMultiLingualDocument, bp.AD_Language," // 6..7
                  + " COALESCE(dt.DocumentCopies,0)+COALESCE(bp.DocumentCopies,1), " // 8
                  + " dt.AD_PrintFormat_ID,bp.C_BPartner_ID,d.DocumentNo " // 9..11
                  + "FROM "
                  + DOC_BASETABLES[type]
                  + " d"
                  + " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
                  + " INNER JOIN AD_PrintForm pf ON (c.AD_Client_ID=pf.AD_Client_ID)"
                  + " INNER JOIN C_BPartner bp ON (d.C_BPartner_ID=bp.C_BPartner_ID)"
                  + " LEFT OUTER JOIN C_DocType dt ON (d.C_DocType_ID=dt.C_DocType_ID) "
                  + "WHERE d."
                  + DOC_IDS[type]
                  + "=?" // info from PrintForm
                  + " AND pf.AD_Org_ID IN (0,d.AD_Org_ID) "
                  + "ORDER BY pf.AD_Org_ID DESC";
        }
        //
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
            pstmt.setInt(1, Record_ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) // first record only
            {
                if (type == CHECK || type == DUNNING || type == REMITTANCE
                    || type == PROJECT || type == RFQ) {
                    AD_PrintFormat_ID = rs.getInt(1);
                    copies = 1;
                    // Set Language when enabled
                    String AD_Language = rs.getString(3);
                    if (AD_Language != null)// && "Y".equals(rs.getString(2)))
                    // // IsMultiLingualDocument
                    {
                        language = Language.getLanguage(AD_Language);
                    }
                    C_BPartner_ID = rs.getInt(4);
                    if (type == DUNNING) {
                        Timestamp ts = rs.getTimestamp(5);
                        DocumentNo = ts.toString();
                    } else {
                        DocumentNo = rs.getString(5);
                    }
                } else {
                    // Set PrintFormat
                    AD_PrintFormat_ID = rs.getInt(type + 1);
                    if (rs.getInt(9) != 0) // C_DocType.AD_PrintFormat_ID
                    {
                        AD_PrintFormat_ID = rs.getInt(9);
                    }
                    copies = rs.getInt(8);
                    // Set Language when enabled
                    String AD_Language = rs.getString(7);
                    if (AD_Language != null) // &&
                    // "Y".equals(rs.getString(6)))
                    // // IsMultiLingualDocument
                    {
                        language = Language.getLanguage(AD_Language);
                    }
                    C_BPartner_ID = rs.getInt(10);
                    DocumentNo = rs.getString(11);
                }
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Record_ID=" + Record_ID + ", SQL=" + sql, e);
        }
        if (AD_PrintFormat_ID == 0) {
            log.log(Level.SEVERE, "No PrintFormat found for Type={0}, Record_ID={1}", new Object[]{type, Record_ID});
            return null;
        }

        // Get Format & Data
        MPrintFormat format = MPrintFormat.get(ctx, AD_PrintFormat_ID, false);
        format.setLanguage(language); // BP Language if Multi-Lingual
        // if (!Env.isBaseLanguage(language, DOC_TABLES[type]))
        format.setTranslationLanguage(language);
        // query
        MQuery query = new MQuery(DOC_TABLES[type]);
        query.addRestriction(DOC_IDS[type], MQuery.EQUAL,
                             new Integer(Record_ID));
        // log.config( "ReportCtrl.startDocumentPrint - " + format, query + " -
        // " + language.getAD_Language());
        //
        if (DocumentNo == null || DocumentNo.length() == 0) {
            DocumentNo = "DocPrint";
        }
        PrintInfo info = new PrintInfo(DocumentNo, DOC_TABLE_ID[type],
                                       Record_ID, C_BPartner_ID);
        info.setCopies(copies);
        info.setDocumentCopy(false); // true prints "Copy" on second
        info.setPrinterName(format.getPrinterName());

        // Engine
        ReportEngine re = new ReportEngine(ctx, format, query, info);
        return re;
    } // get

    /**
     * Determine what Order document to print.
     *
     * @param C_Order_ID id
     * @return int Array with [printWhat, ID]
     */
    private static int[] getDocumentWhat(int C_Order_ID) {
        int[] what = new int[2];
        what[0] = ORDER;
        what[1] = C_Order_ID;
        //
        String sql = "SELECT dt.DocSubTypeSO "
                     + "FROM C_DocType dt, C_Order o "
                     + "WHERE o.C_DocType_ID=dt.C_DocType_ID"
                     + " AND o.C_Order_ID=?";
        String DocSubTypeSO = null;
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, C_Order_ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                DocSubTypeSO = rs.getString(1);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e1) {
            log.log(Level.SEVERE, "(1) - " + sql, e1);
            return null; // error
        }
        if (DocSubTypeSO == null) {
            DocSubTypeSO = "";
        }
        // WalkIn Receipt, WalkIn Invoice,
        if (DocSubTypeSO.equals("WR") || DocSubTypeSO.equals("WI")) {
            what[0] = INVOICE;
        } // WalkIn Pickup,
        else if (DocSubTypeSO.equals("WP")) {
            what[0] = SHIPMENT;
        } // Offer Binding, Offer Nonbinding, Standard Order
        else {
            return what;
        }

        // Get Record_ID of Invoice/Receipt
        if (what[0] == INVOICE) {
            sql = "SELECT C_Invoice_ID REC FROM C_Invoice WHERE C_Order_ID=?" // 1
                  + " ORDER BY C_Invoice_ID DESC";
        } else {
            sql = "SELECT M_InOut_ID REC FROM M_InOut WHERE C_Order_ID=?" // 1
                  + " ORDER BY M_InOut_ID DESC";
        }
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, C_Order_ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // if (i == 1 && ADialog.ask(0, null, what[0] == INVOICE ?
                // "PrintOnlyRecentInvoice?" : "PrintOnlyRecentShipment?"))
                // break;
                what[1] = rs.getInt(1);
            } else // No Document Found
            {
                what[0] = ORDER;
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e2) {
            log.log(Level.SEVERE, "(2) - " + sql, e2);
            return null;
        }
        log.log(Level.FINE, "Order => {0} ID={1}", new Object[]{what[0], what[1]});
        return what;
    } // getDocumentWhat

    /**
     * Print Confirm. Update Date Printed
     *
     * @param type document type
     * @param Record_ID record id
     */
    public static void printConfirm(int type, int Record_ID) {
        StringBuilder sql = new StringBuilder();
        if (type == ORDER || type == SHIPMENT || type == INVOICE) {
            sql.append("UPDATE ").append(DOC_BASETABLES[type]).append(
                    " SET DatePrinted=SysDate, IsPrinted='Y' WHERE ").append(
                    DOC_IDS[type]).append("=").append(Record_ID);
        }
        //
        if (sql.length() > 0) {
            int no = DB.executeUpdate(sql.toString(), null);
            if (no != 1) {
                log.log(Level.SEVERE, "Updated records={0} - should be just one", no);
            }
        }
    } // printConfirm

    /**
     * *************************************************************************
     * Test
     *
     * @param args args
     */
    public static void main(String[] args) {
        org.compiere.Compiere.startupEnvironment(true);
        //
        int AD_Table_ID = 100;
        MQuery q = new MQuery("AD_Table");
        q.addRestriction("AD_Table_ID", "<", 108);
        //
        MPrintFormat f = MPrintFormat.createFromTable(Env.getCtx(), AD_Table_ID);
        PrintInfo i = new PrintInfo("test", AD_Table_ID, 108, 0);
        i.setAD_Table_ID(AD_Table_ID);
        ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
        re.layout();
        /**
         * *********************************************************************
         * re.createCSV(new File("C:\\Temp\\test.csv"), ',', Language.getLanguage()); re.createHTML(new
         * File("C:\\Temp\\test.html"), false, Language.getLanguage()); re.createXML(new File("C:\\Temp\\test.xml"));
         * re.createPS(new File ("C:\\Temp\\test.ps")); re.createPDF(new File("C:\\Temp\\test.pdf")); /
         *********************************************************************
         */
        re.print();
        // re.print(true, 1, false, "Epson Stylus COLOR 900 ESC/P 2"); // Dialog
        // re.print(true, 1, false, "HP LaserJet 3300 Series PCL 6"); // Dialog
        // re.print(false, 1, false, "Epson Stylus COLOR 900 ESC/P 2"); //
        // Dialog
        System.exit(0);
    } // main
} // ReportEngine
