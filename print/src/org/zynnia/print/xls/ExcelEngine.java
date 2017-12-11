/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.print.xls;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.regex.Pattern;
import jxl.format.Colour;
import jxl.write.DateFormat;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.compiere.model.MLocation;
import org.compiere.model.MQuery;
import org.compiere.print.DataEngine;
import org.compiere.print.MPrintFormat;
import org.compiere.print.MPrintFormatItem;
import org.compiere.print.PrintData;
import org.compiere.print.PrintDataElement;
import org.compiere.print.layout.HTMLElement;
import org.compiere.print.layout.LayoutEngine;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.compiere.util.NamePair;
import org.compiere.util.StringUtil;
import org.compiere.util.Util;
import org.compiere.util.ValueNamePair;

/**
 *
 * @author Alejandro Scott
 */
public class ExcelEngine {

    /**
     * Logger
     */
    private static CLogger log = CLogger.getCLogger(LayoutEngine.class);
    /**
     * Existing Layout
     */
    private boolean m_hasLayout = false;
    /**
     * The Workbook
     */
    private WritableWorkbook m_workbook;
    /**
     * The Format
     */
    private MPrintFormat m_format;
    /**
     * Print Context
     */
    private Properties m_printCtx;
    /**
     * The Data
     */
    private PrintData m_data;
    /**
     * The Query (parameter
     */
    private MQuery m_query;
    /**
     * Printed Rows Count
     */
    private int m_rowCount = -1;
    /**
     * Excel's Sheet to be writed
     */
    private WritableSheet sheet;

    private Language language;

    private static final long MAX_ROWS_BY_SHEET = 65536;

    private int m_numSheet = 0;

    private WritableCellFormat headerFormat;

    private WritableCellFormat simpleFormat;

    private WritableCellFormat numberFormat;

    private WritableCellFormat dateFormat;


    public ExcelEngine(WritableWorkbook workbook, MPrintFormat format, PrintData data, MQuery query, WritableSheet sheet, Language language) throws WriteException, Exception {
        log.log(Level.INFO, "{0} - {1} - {2}", new Object[]{format, data, query});
        this.sheet = sheet;
        this.language = language;
        this.m_workbook = workbook;
        DateFormat df = new DateFormat("dd/MM/yyyy");
        this.dateFormat =  new WritableCellFormat(df);
        this.numberFormat = new WritableCellFormat(NumberFormats.THOUSANDS_FLOAT);
        this.headerFormat = getHeaderFormatXLS();
        this.simpleFormat = getSimpleFormatXLS();


        setPrintFormat(format, false);
        setPrintData(data, query, false);
    }

    /**
     * ************************************************************************
     * Set Print Format Optionally re-calculate layout
     *
     * @param doLayout if layout exists, redo it
     * @param format print Format
     */
    public final void setPrintFormat(MPrintFormat format, boolean doLayout) throws WriteException, Exception {
        m_format = format;
        //	Initial & Default Settings
        m_printCtx = new Properties(format.getCtx());
        if (m_hasLayout && doLayout) {
            layout();			//	re-calculate
        }
    }	//	setPrintFormat

    /**
     * Set PrintData. Optionally re-calculate layout
     *
     * @param data data
     * @param doLayout if layout exists, redo it
     * @param query query for parameter
     */
    public final void setPrintData(PrintData data, MQuery query, boolean doLayout) throws WriteException, Exception {
        m_data = data;
        m_query = query;
        if (m_hasLayout && doLayout) {
            layout();			//	re-calculate
        }
    }	//	setPrintData

    /**
     * ************************************************************************
     * Create Layout
     */
    public void layout() throws WriteException, Exception {
        //	Parameter
        if (m_format.isVIEWPARAMETERS()) {
            layoutParameter();
        }

        if (m_format.isForm()) {
            m_rowCount = 0;
            layoutForm();
        } else if (m_data != null) { //	Table
            m_rowCount = 0;
            layoutTable(m_format, m_data);
        }
        m_hasLayout = true;
    }	//	layout

    /**
     * Get Format Model
     *
     * @return model
     */
    public MPrintFormat getFormat() {
        return m_format;
    }	//	getFormat

    private int getRowForCell() {
         if (m_rowCount + 1 >= MAX_ROWS_BY_SHEET) {
             m_numSheet++;
             sheet = m_workbook.createSheet(sheet.getName() + " " + m_numSheet, m_numSheet);
             m_rowCount = 0;
         }
         return m_rowCount;
    }

    /**
     * Layout Parameter based on MQuery
     *
     * @return PrintElement
     */
    private void layoutParameter() throws WriteException {
        if (m_query == null || !m_query.isActive()) {
            return;
        }

        Label label = new Label(0, 0, Msg.getMsg(m_printCtx, "Parameter") + ":");
        sheet.addCell(label);
        for (int r = 0; r < m_query.getRestrictionCount(); r++) {
            Label col1 = new Label(r, 1, m_query.getInfoName(r));
            sheet.addCell(col1);
            Label col2 = new Label(r, 2, m_query.getInfoOperator(r));
            sheet.addCell(col2);
            Label col3 = new Label(r, 3, m_query.getInfoDisplayAll(r));
            sheet.addCell(col3);
        }
    }	//	layoutParameter

    /**
     * ************************************************************************
     * Layout Form. For every Row, loop through the Format and calculate element size and position.
     */
    private void layoutForm() throws Exception {
        if (m_data == null) {
            return;
        }
        //	for every row
        for (int row = 0; row < m_data.getRowCount(); row++) {
            log.log(Level.INFO, "Row={0}", row);
            m_data.setRowIndex(row);
            //	for every item
            for (int i = 0; i < m_format.getItemCount(); i++) {
                MPrintFormatItem item = m_format.getItem(i);
                //	log.fine("layoutForm - Row=" + row + " - #" + i + " - " + item);
                if (!item.isPrinted()) {
                    continue;
                }
                //	Read Header/Footer just once
                if (row > 0 && (item.isHeader() || item.isFooter())) {
                    continue;
                }

                //	Type
                if (item.isTypePrintFormat()) { //** included PrintFormat
                    includeFormat(item, m_data);
                    if (m_rowCount > 0) {
                        m_rowCount++;
                    }
                } else if (item.isTypeField()) { //**	Field
                    createFieldElement(item, m_format.isForm(), simpleFormat);
                } else { //	(item.isTypeText())		//**	Text
                    createStringElement(item.getPrintName(m_format.getLanguage()),
                                        true, simpleFormat);
                }
            }	//	for every item
        }	//	for every row
    }	//	layoutForm

    /**
     * Include Table Format
     *
     * @param item print format item
     * @return Print Element
     */
    private void includeFormat(MPrintFormatItem item, PrintData data) throws Exception {
        //
        MPrintFormat format = MPrintFormat.get(getCtx(), item.getAD_PrintFormatChild_ID(), false);
        format.setLanguage(m_format.getLanguage());
        if (m_format.isTranslationView()) {
            format.setTranslationLanguage(m_format.getLanguage());
        }

        int AD_Column_ID = item.getAD_Column_ID();
        log.log(Level.INFO, "{0} - Item={1} ({2})", new Object[]{format, item.getName(), AD_Column_ID});

        //
        Object obj = data.getNode(new Integer(AD_Column_ID));
        if (obj == null) {
            data.dumpHeader();
            data.dumpCurrentRow();
            log.log(Level.SEVERE, "No Node - AD_Column_ID={0} - {1} - {2}", new Object[]{AD_Column_ID, item, data});
            return;
        }

        PrintDataElement dataElement = (PrintDataElement) obj;
        String recordString = dataElement.getValueKey();
        if (recordString == null || recordString.length() == 0) {
            data.dumpHeader();
            data.dumpCurrentRow();
            log.log(Level.SEVERE, "No Record Key - {0} - AD_Column_ID={1} - {2}", new Object[]{dataElement, AD_Column_ID, item});
            return;
        }

        int Record_ID;
        try {
            Record_ID = Integer.parseInt(recordString);
        } catch (Exception e) {
            data.dumpCurrentRow();
            log.log(Level.SEVERE, "Invalid Record Key - {0} ({1}) - AD_Column_ID={2} - {3}", new Object[]{recordString, e.getMessage(), AD_Column_ID, item});
            return;
        }

        MQuery query = new MQuery(format.getAD_Table_ID());
        query.addRestriction(item.getColumnName(), MQuery.EQUAL, new Integer(Record_ID));
        format.setTranslationViewQuery(query);
        log.fine(query.toString());
        //
        DataEngine de = new DataEngine(format.getLanguage());
        PrintData includedData = de.getPrintData(data.getCtx(), format, query);
        log.fine(includedData.toString());
        if (includedData == null) {
            return;
        }

        //
        layoutTable(format, includedData);
    }	//	includeFormat

    /**
     * ************************************************************************
     * Layout Table. Convert PrintData into TableElement
     *
     * @param format format to use
     * @param printData data to use
     */
    private void layoutTable(MPrintFormat format, PrintData printData) throws Exception {
        log.log(Level.INFO, "{0} - {1}", new Object[]{format.getName(), printData.getName()});

        //	Column count
        int columnCount = 0;
        for (int c = 0; c < format.getItemCount(); c++) {
            if (format.getItem(c).isPrinted()) {
                columnCount++;
            }
        }

        //	Header & Column Setup
        HashMap<Integer, Integer> additionalLines = new HashMap<Integer, Integer>();
        int col = 0;
        boolean somePrinted = false;
        int fixedRow = getRowForCell();
        for (int c = 0; c < format.getItemCount(); c++) {
            MPrintFormatItem item = format.getItem(c);
            if (item.isPrinted()) {
                if (item.isNextLine() && item.getBelowColumn() != 0) {
                    additionalLines.put(new Integer(col), new Integer(item.getBelowColumn() - 1));
                    if (!item.isSuppressNull()) {
                        item.setIsSuppressNull(true);	//	display size will be set to 0 in TableElement
                        item.save();
                    }
                }
                Label label = new Label(col, fixedRow, item.getPrintName(format.getLanguage()),
                                        headerFormat);
                sheet.addCell(label);
                somePrinted = true;
                //
                col++;
            }
        }

        if (somePrinted) {
            m_rowCount++;
        }
        //	The Data
        int rows = printData.getRowCount();
        //	System.out.println("Rows=" + rows);
        KeyNamePair[] pk = new KeyNamePair[rows];
        String pkColumnName = null;

        //	for all rows
        for (int row = 0; row < rows; row++) {
            //	System.out.println("row=" + row);
            printData.setRowIndex(row);
            //	for all columns
            col = 0;
            fixedRow = getRowForCell();
            for (int c = 0; c < format.getItemCount(); c++) {
                MPrintFormatItem item = format.getItem(c);
                if (item.isPrinted() && item.getAD_Column_ID() > 0) { //	Text Columns
                    if (item.isTypePrintFormat() || item.isTypeImage()) {
                    } else {
                        Object obj = printData.getNode(new Integer(item.getAD_Column_ID()));
                        if (obj == null) {
                        } else if (obj instanceof PrintDataElement) {
                            PrintDataElement pde = (PrintDataElement) obj;
                            addCellToSheet(pde, fixedRow, col, dateFormat, simpleFormat, numberFormat);
                        } else {
                            log.log(Level.SEVERE, "Element not PrintDataElement {0}", obj.getClass());
                        }
                    }
                    col++;
                }	//	printed
            }	//	for all columns
            m_rowCount++;
            PrintDataElement pde = printData.getPKey();
            if (pde != null) { //	for FunctionRows
                pk[row] = (KeyNamePair) pde.getValue();
                if (pkColumnName == null) {
                    pkColumnName = pde.getColumnName();
                }
            }
        }	//	for all rows
    }	//	layoutTable

    /**
     * *************************************************************************
     * Get PrintLayout (Report) Context
     *
     * @return context
     */
    public Properties getCtx() {
        return m_printCtx;
    }	//	getCtx

    private WritableCellFormat getHeaderFormatXLS() throws Exception {
        // Fuente de la cabecera de la tabla
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
                                           WritableFont.BOLD);
        wf.setColour(Colour.BLACK);
        // Creo un formato y le asigno la fuente anterior
        WritableCellFormat wcf = new WritableCellFormat(wf);
        wcf.setBackground(Colour.GREY_25_PERCENT);
        return wcf;
    }	//	getHeaderFormatXLS

    private WritableCellFormat getSimpleFormatXLS() throws Exception {
        // Fuente de la cabecera de la tabla
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10);
        wf.setColour(Colour.BLACK);
        // Creo un formato y le asigno la fuente anterior
        WritableCellFormat wcf = new WritableCellFormat(wf);
        wcf.setShrinkToFit(false);
        return wcf;
    }	//	getSimpleFormatXLS

    private void addTextCellToSheet(String content, int row, int col, WritableCellFormat cellFormat) throws WriteException {
        WritableCell cell = new jxl.write.Label(col, row, content, cellFormat);
        sheet.addCell(cell);
    }

    private void addCellToSheet(PrintDataElement pde, int row, int col, WritableCellFormat dateDf, WritableCellFormat cellFormat, WritableCellFormat numberFormat) throws WriteException {
        WritableCell cell;

        // Tipo de dato fecha
        if (pde.getDisplayType() == DisplayType.Date) {
            java.sql.Date date = (java.sql.Date) pde.getValue();
            cell = new jxl.write.DateTime(col, row, new Timestamp(date.getTime()), dateDf);
        } else if (pde.getDisplayType() == DisplayType.DateTime) {
            if (pde.getValue() instanceof java.sql.Date) {
                java.sql.Date time = (java.sql.Date) pde.getValue();
                cell = new jxl.write.DateTime(col, row, new Timestamp(time.getTime()));
            } else {
                Timestamp time = (Timestamp) pde.getValue();
                //i+1 porque en la primer fila van los labels
                cell = new jxl.write.DateTime(col, row, time);
            }
            cell.setCellFormat(dateDf);
        } // Tipo de dato numerico
        else if (pde.isNumeric()) {
            BigDecimal number = (BigDecimal) pde.getValue();
            cell = new jxl.write.Number(col, row, number.doubleValue(), numberFormat);
        } // Tipo de dato nulo
        else if (pde.isNull()) {
            cell = new jxl.write.Label(col, row, "", cellFormat);
        } else if (pde.isYesNo()) {
            java.lang.Boolean yesNo = (java.lang.Boolean) pde.getValue();
            if (yesNo.booleanValue()) {
                cell = new jxl.write.Label(col, row, "SI", cellFormat);
            } else {
                cell = new jxl.write.Label(col, row, "NO", cellFormat);
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
            if (valueStr.matches("[0-9]+")) {
                try {
                    int val = Integer.valueOf(valueStr).intValue();
                    BigDecimal number = new BigDecimal(val);
                    cell = new jxl.write.Number(col, row, number.doubleValue(), cellFormat);
                }
                //Capturo error al transformar a integer, entonces lo guado como string
                catch (NumberFormatException e) {
                   log.log(Level.SEVERE, "Error parseando valor "+valueStr+", se parsea entonces como texto", e);
                    cell = new jxl.write.Label(col, row, valueStr, cellFormat);
                }
            } else if (valueStr.matches("[0-9]+,[0-9]+")
                       || valueStr.matches("[0-9]+\\.[0-9]+")) {
                double valD = Double.valueOf(valueStr).doubleValue();
                cell = new jxl.write.Number(col, row, valD, cellFormat);
            } else {
                cell = new jxl.write.Label(col, row, valueStr, cellFormat);
            }
        }
        sheet.addCell(cell);
    }

    /**
     * Create Field Element
     *
     * @param item Format Item
     * @param isForm true if document
     * @return Print Element or null if nothing to print
     */
    private void createFieldElement(MPrintFormatItem item, boolean isForm, WritableCellFormat cellFormat) throws WriteException {
        //	Get Data
        Object obj = m_data.getNode(new Integer(item.getAD_Column_ID()));
        if (obj == null) {
            return;
        } else if (!(obj instanceof PrintDataElement)) {
            log.log(Level.SEVERE, "Element not PrintDataElement {0}", obj.getClass());
            return;
        }

        //	Convert DataElement to String
        PrintDataElement data = (PrintDataElement) obj;
        if (data.isNull() && item.isSuppressNull()) {
            return;
        }
        String stringContent = data.getValueDisplay(m_format.getLanguage());
        if ((stringContent == null || stringContent.length() == 0) && item.isSuppressNull()) {
            return;
        }
        //	non-string
        if (data.getValue() instanceof Boolean) {
            stringContent = ((Boolean) data.getValue()).booleanValue() ? "SI" : "NO";
        }

        //	Convert AmtInWords Content to alpha
        if (item.getColumnName().equals("AmtInWords")) {
            log.log(Level.FINE, "AmtInWords: {0}", stringContent);
            stringContent = Msg.getAmtInWords(m_format.getLanguage(), stringContent);
        }
        //	Label
        String label = item.getPrintName(m_format.getLanguage());
        String labelSuffix = item.getPrintNameSuffix(m_format.getLanguage());

        //	ID Type
        NamePair ID = null;
        if (data.isID()) {	//	Record_ID/ColumnName
            Object value = data.getValue();
            if (value instanceof KeyNamePair) {
                ID = new KeyNamePair(((KeyNamePair) value).getKey(), item.getColumnName());
            } else if (value instanceof ValueNamePair) {
                ID = new ValueNamePair(((ValueNamePair) value).getValue(), item.getColumnName());
            }
        }

        int fixedRow = getRowForCell();
        //	Create String, HTML or Location
        if (data.getDisplayType() == DisplayType.Location) {
            addLocationItemToSheet(((KeyNamePair) ID).getKey(), cellFormat);
        } else {
            if (HTMLElement.isHTML(stringContent)) {
                stringContent = StringUtil.extractText(stringContent);
            }
            int col = 0;
            if (isForm) {
                if (labelSuffix != null) {
                    label = label.concat(labelSuffix);
                }
                addTextCellToSheet(label, fixedRow, col, cellFormat);
                col++;
            }
            addTextCellToSheet(stringContent, fixedRow, col, cellFormat);
            m_rowCount++;
        }
    }	//	createFieldElement

    private void addLocationItemToSheet(int C_Location_ID, WritableCellFormat cellFormat) throws WriteException {
        MLocation ml = MLocation.get(m_printCtx, C_Location_ID, null);
        if (ml != null) {
            if (ml.isAddressLinesReverse()) {
                addTextCellToSheet(ml.getCountry(true), getRowForCell(), 0, cellFormat);
                String[] lines = Pattern.compile("$", Pattern.MULTILINE).split(ml.getCityRegionPostal());
                for (int i = 0; i < lines.length; i++) {
                    addTextCellToSheet(lines[i], getRowForCell(), 0, cellFormat);
                    m_rowCount++;
                }
                if (ml.getAddress4() != null && ml.getAddress4().length() > 0) {
                    addTextCellToSheet(ml.getAddress4(), getRowForCell(), 0, cellFormat);
                    m_rowCount++;
                }
                if (ml.getAddress3() != null && ml.getAddress3().length() > 0) {
                    addTextCellToSheet(ml.getAddress3(), getRowForCell(), 0, cellFormat);
                    m_rowCount++;
                }
                if (ml.getAddress2() != null && ml.getAddress2().length() > 0) {
                    addTextCellToSheet(ml.getAddress2(), getRowForCell(), 0, cellFormat);
                    m_rowCount++;
                }
                if (ml.getAddress1() != null && ml.getAddress1().length() > 0) {
                    addTextCellToSheet(ml.getAddress1(), getRowForCell(), 0, cellFormat);
                    m_rowCount++;
                }
            } else {
                if (ml.getAddress1() != null && ml.getAddress1().length() > 0) {
                    addTextCellToSheet(ml.getAddress1(), getRowForCell(), 0, cellFormat);
                    m_rowCount++;
                }
                if (ml.getAddress2() != null && ml.getAddress2().length() > 0) {
                    addTextCellToSheet(ml.getAddress2(), getRowForCell(), 0, cellFormat);
                    m_rowCount++;
                }
                if (ml.getAddress3() != null && ml.getAddress3().length() > 0) {
                    addTextCellToSheet(ml.getAddress3(), getRowForCell(), 0, cellFormat);
                    m_rowCount++;
                }
                if (ml.getAddress4() != null && ml.getAddress4().length() > 0) {
                    addTextCellToSheet(ml.getAddress4(), getRowForCell(), 0, cellFormat);
                    m_rowCount++;
                }
                String[] lines = Pattern.compile("$", Pattern.MULTILINE).split(ml.getCityRegionPostal());
                for (int i = 0; i < lines.length; i++) {
                    addTextCellToSheet(lines[i], getRowForCell(), 0, cellFormat);
                    m_rowCount++;
                }
                addTextCellToSheet(ml.getCountry(true), getRowForCell(), 0, cellFormat);
                m_rowCount++;
            }
        }
    }

    /**
     * Create String Element
     *
     * @param content string to be printed
     * @param isTranslated if true and content contaiins @variable@, it is dynamically translated during print
     * @return Print Element
     */
    private void createStringElement(String content,
                                     boolean isTranslated, WritableCellFormat cellFormat) throws WriteException {
        if (content == null || content.length() == 0) {
            return;
        }

        if (isTranslated) {
            int count = Util.getCount(content, '@');
            if (count > 0 && count % 2 == 0) {
                String m_originalString = content;
                //	Translate it to get rough space (not correct context) = may be too small
                content = Msg.parseTranslation(Env.getCtx(), m_originalString);
            }
        }
        String[] lines = Pattern.compile("$", Pattern.MULTILINE).split(content);
        for (int i = 0; i < lines.length; i++) {
            String line = Util.removeCRLF(lines[i]);
            if (line.length() == 0) {
                continue;
            }
            log.log(Level.FINEST, " - line={0} - {1}", new Object[]{i, line});
            addTextCellToSheet(line, getRowForCell(), 0, cellFormat);
        }
    }	//	createStringElement
}
