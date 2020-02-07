/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.reports.viewer;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.Number;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.FieldDynamicReport;
import org.compiere.model.MOrg;
import org.compiere.model.MUser;
import org.compiere.model.MZYNREPORT;
import org.compiere.print.PrintDataElement;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.zynnia.reports.ReportUtils;
import org.zynnia.reports.parameters.ZynParameter;
import org.zynnia.reports.printproperties.PrintProperty;
import org.zynnia.reports.table.XTableColumnModel;

/**
 *
 * @author Alejandro Scott
 */
public class XLSExporter extends Exporter {

    private static CLogger log = CLogger.getCLogger(XLSExporter.class);
    private WritableCellFormat headerTableFormat;
    private WritableCellFormat commonDataFormat;
    private final DateFormat dateFormat;

    private class XLSPosition {

        private int currentRow;
        private int currentCol;

        public XLSPosition(int row, int col) {
            this.currentCol = col;
            this.currentRow = row;
        }

        public XLSPosition() {
            this(0, 0);
        }

        public int increaseRow() {
            return increaseRow(1);
        }

        public int increaseRow(int rows) {
            currentRow = currentRow + rows;
            return currentRow;
        }

        public int increaseCol() {
            return increaseCol(1);
        }

        public int increaseCol(int cols) {
            currentCol = currentCol + cols;
            return currentCol;
        }

        public int getCurrentCol() {
            return currentCol;
        }

        public int getCurrentRow() {
            return currentRow;
        }

        public void setCurrentCol(int col) {
            currentCol = col;
        }

        public void setCurrentRow(int row) {
            currentRow = row;
        }

        public void reset() {
            reset(0, 0);
        }

        public void reset(int row, int col) {
            setCurrentRow(row);
            setCurrentCol(col);
        }
    }

    public XLSExporter(int reportID, Hashtable<Integer, ZynParameter> activeParams, MiniTable p_table, XTableColumnModel columnModel, Hashtable<TableColumn, FieldDynamicReport> fieldsInReport, Hashtable<String, Properties> printProperties) {
        super(reportID, activeParams, p_table, columnModel, fieldsInReport, printProperties);
        this.dateFormat = new DateFormat("dd/MM/yyyy");
        try {
            headerTableFormat = getXLSHeaderFormat();
            commonDataFormat = getXLSDataFormat();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Can't create formats for label and header", ex);
        }

    }

    @Override
    public void export() {
         if (reportID > 0) {
            // Create temp file.
            File tempFile;
            try {
                tempFile = ReportUtils.createTemporalFile(".xls");
            } catch (IOException ex) {
                log.log(Level.SEVERE, "Can't create temporal File", ex);
                return;
            }
            try {
                WritableWorkbook workbook = Workbook.createWorkbook(tempFile);
                WritableSheet sheet = workbook.createSheet(zynReport.getName(), 0);
                XLSPosition pos = new XLSPosition();

                addTextCellToSheet(sheet, pos.getCurrentCol(), pos.getCurrentRow(), zynReport.getName());

                pos.increaseRow(2);
                if (zynReport.isPRINTDATE()) {
                    addPrintDateToReport(pos, sheet);
                    pos.increaseRow(2);
                }

                if (zynReport.isPRINTDESCRIPTION()) {
                    addTextCellToSheet(sheet, pos.getCurrentCol(), pos.getCurrentRow(), zynReport.getDescription());
                    pos.increaseRow(2);
                }

                /*
                 * Va la configuración dinamica de parámetros
                 */
                if (zynReport.isPRINTPARAM()) {
                    addParamsToReport(pos, sheet);
                }

                /*
                 * Va la configuración dinamica del contenido
                 */
                XTableColumnModel tcm = (XTableColumnModel) p_table.getTableHeader().getColumnModel();
                constructReportGridXLS(sheet, pos, (DefaultTableModel) p_table.getModel(), tcm, zynReport);

                workbook.write();
                workbook.close();

            } catch (Exception de) {
                log.log(Level.SEVERE, "Error in report generation", de);
            }
            showReport(tempFile);
        }
    }

    private void constructReportGridXLS(WritableSheet sheet, XLSPosition pos, DefaultTableModel model, XTableColumnModel tcm, MZYNREPORT zynReport) throws WriteException, Exception {
        int cantCol = columnModel.getColumnCount(true);
        int cantRow = model.getRowCount();
        float[] wCentro = new float[tcm.getColumnCount()];

        // Anerxo para llevar el acumulado de los campos que tengan función suma
        float[] acumulado = new float[tcm.getColumnCount()];
        float[][] acumuladoGrupo = new float[tcm.getColumnCount()][tcm.getColumnCount()];

        // Anerxo para llevar el valor de ordenamiento para determinar el salto de diferentes valores
        String[] ordenamiento = new String[tcm.getColumnCount()];

        /*
         * La asignación de anchos va en función de las variables de anchos recogidad desde la parametrización.
         */
        int idx = 0;
        for (Enumeration en = tcm.getColumns(true); en.hasMoreElements();) {
            TableColumn column = (TableColumn) en.nextElement();
            FieldDynamicReport field = fieldsInReport.get(column);
            if (field != null) {
                Properties prop = printProperties.get(field.getCompleteNameForQuery());
                float value = (Float) prop.get(PrintProperty.WIDTH_PROPERTY);
                if (value == -1) {
                    value = 1;
                }
                float val = (1 * value);
                val = val / 100;
                wCentro[idx] = val;

                // inicializo el acumulado en 0
                acumulado[idx] = 0;
                for (int indColAcum = 0; indColAcum < cantCol; indColAcum++) {
                    acumuladoGrupo[indColAcum][idx] = 0;
                }
                // inicializo el ordenamiento en vacio
                ordenamiento[idx] = "";
                idx = idx + 1;
            }
        }

        Vector data = model.getDataVector();

        // Obtener las columnas de la cabecera del reporte y armar dicha cabecera.
        // Recorrer el vector de datos para la fila 0 y la cantidad de columnas y
        // rellenar la cabecera de la tabla HTML.
        /*
         * TODO: ELIMINAR ESTO DE LA VERSION FINAL
         */
        /*
        for (Enumeration en = printProperties.keys(); en.hasMoreElements();) {
            // Obtenemos el objeto
            Object obj = en.nextElement();
            Properties props = printProperties.get(obj.toString());
            log.log(Level.FINE, "Celda Hash {0}: {1}", new Object[]{obj, props});
            for (PrintProperty prop : PrintProperty.values()) {
                Object value = props.get(prop);
                log.log(Level.FINE, "Propiedad: {0}, valor: {1}", new Object[]{prop, value});
            }
        }
        */
        /*
         * FIN
         */
        Enumeration<TableColumn> colHeaderItems = tcm.getColumns(true);
        int colOffset = 0;
        while (colHeaderItems.hasMoreElements()) {
            TableColumn column = colHeaderItems.nextElement();
            FieldDynamicReport field = fieldsInReport.get(column);
            Properties prop = printProperties.get(field.getCompleteNameForQuery());
            String value = (String) prop.get(PrintProperty.LABEL_PROPERTY);
            addTextCellToSheet(sheet, pos.getCurrentCol() + colOffset, pos.getCurrentRow(), value, headerTableFormat);
            colOffset++;
        }

        pos.increaseRow();

        // Implica que esta fila debe primero tratar el resumen del fin
        // del ordenamiento anterior
        for (int indRow = 0; indRow < cantRow; indRow++) {
            // Implica que esta fila debe primero tratar el resumen del fin
            // del ordenamiento anterior
            int cambioOrdenamiento = changeOrderby(indRow, cantCol, ordenamiento, data);

            for (int indCol = 0; indCol < cantCol; indCol++) {
                TableColumn column = columnModel.getVisibleColumnByModelIndex(indCol);
                FieldDynamicReport field = fieldsInReport.get(column);

                // Se debe determinar para el caso de campos de ordenamiento si cambia
                // para anexar una linea de resumen con los campos que puedan expresar funcion
                if (cambioOrdenamiento != -1) {
                    if (field.isIsSum()) {
//                         "Sum " + ((Vector) data.elementAt(indRow - 1)).elementAt(cambioOrdenamiento).toString() + "= " +
//                          Float.toString()
                        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
                                                           WritableFont.BOLD);
                        wf.setColour(Colour.RED);
                        WritableCellFormat cf = new WritableCellFormat(wf, NumberFormats.THOUSANDS_FLOAT);
                        WritableCell cell = new Number(pos.getCurrentCol() + indCol, pos.getCurrentRow(), acumuladoGrupo[cambioOrdenamiento][indCol], cf);
                        sheet.addCell(cell);
                    }
                    acumuladoGrupo[cambioOrdenamiento][indCol] = 0;
                } else {
                    Vector dataRow = (Vector) data.elementAt(indRow);
                    if(dataRow.elementAt(column.getModelIndex()) != null) {
                        String value = dataRow.elementAt(column.getModelIndex()).toString();
                        if(value != null && value.length() > 0) {
                            addCellToSheet(sheet, field, pos.getCurrentRow(), pos.getCurrentCol() + indCol, value);
                            if (field.isIsSum()) {
                                double valToAcum = Double.parseDouble(value);
                                acumulado[indCol] += valToAcum;
                                for (int indColAcum = 0; indColAcum < cantCol; indColAcum++) {
                                    acumuladoGrupo[indColAcum][indCol] += valToAcum;
                                }
                            }
                        }
                    }
                }
            }
            // Para no perder el registro de cambio (cambioOrdenamiento == 1) vuelvo a evaluarlo
            if (cambioOrdenamiento != -1) {
                indRow = indRow - 1;
            }
            pos.increaseRow();
        }

        // Finalizando el ingreso de todos los registros, debemos hacer el resumen para el caso de que tengamos columnas
        // con funciones
        log.log(Level.FINE, "Tama\u00f1o {0}", data.size());

        for (int allCol = 0; allCol < cantCol; allCol++) {
            TableColumn columnComp = columnModel.getVisibleColumnByModelIndex(allCol);
            FieldDynamicReport fieldComp = fieldsInReport.get(columnComp);

            // Se debe determinar para el caso de campos de ordenamiento si cambia
            // para anexar una linea de resumen con los campos que puedan expresar funcion
            if (fieldComp.isIsOrderby()) {
                for (int indCol = 0; indCol < cantCol; indCol++) {
                    TableColumn column = columnModel.getVisibleColumnByModelIndex(indCol);
                    FieldDynamicReport field = fieldsInReport.get(column);

                    // Se debe determinar para el caso de campos de ordenamiento si cambia
                    // para anexar una linea de resumen con los campos que puedan expresar funcion
                    if (field.isIsSum()) {
                        //                        paraParam = new Paragraph("Sum " + ((Vector) data.elementAt(cantRow - 1)).elementAt(allCol).toString() + "= " + Float.toString(acumuladoGrupo[allCol][indCol]), colContentResumeFont);
                        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
                                                           WritableFont.BOLD);
                        wf.setColour(Colour.RED);
                        WritableCellFormat cf = new WritableCellFormat(wf, NumberFormats.THOUSANDS_FLOAT);
                        WritableCell cell = new Number(pos.getCurrentCol() + allCol, pos.getCurrentRow(), acumuladoGrupo[allCol][column.getModelIndex()], cf);
                        sheet.addCell(cell);
                    }
                }
            }
        }


        /*
         * Anexar la fila de resumen antes del pìe de página
         *
         */
        for (int indColRes = 0; indColRes < cantCol; indColRes++) {
            // Se debe determinar para el caso de campos de ordenamiento si cambia
            // para anexar una linea de resumen con los campos que puedan expresar funcion
            if (acumulado[indColRes] != 0) {
                TableColumn column = columnModel.getVisibleColumnByModelIndex(indColRes);
                FieldDynamicReport field = fieldsInReport.get(column);

                WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
                                                   WritableFont.BOLD);
                wf.setColour(Colour.RED);
                WritableCellFormat cf = new WritableCellFormat(wf, NumberFormats.THOUSANDS_FLOAT);
                WritableCell cell = new Number(pos.getCurrentCol() + indColRes, pos.getCurrentRow(), acumulado[indColRes], cf);
                sheet.addCell(cell);
            }
        }
    }

    private void addParamsToReport(XLSPosition pos, WritableSheet sheet) throws WriteException, Exception {
        for (ZynParameter paramInput : activeParams.values()) {
            //  Valido el tipo de componente para hacer un casting recuperarlo
            //  y verificar si el valor no es nulo.
            String displayFirsComponent = getComponentDisplayText(paramInput.getFirstComponent());
            String displayLastComponent = getComponentDisplayText(paramInput.getLastComponent());
            Object valueFirstComp = getComponentValue(paramInput.getFirstComponent());
            Object valueLastComp = getComponentValue(paramInput.getLastComponent());
            boolean someAdded = true;
            if (paramInput.getLastComponent() != null) {
                if (valueFirstComp != null && valueLastComp != null) {
                    addRangeParamXLS(pos, sheet, paramInput.getParameterName(), displayFirsComponent, displayLastComponent);
                } else if (valueFirstComp != null) {
                    addParamXLS(pos, sheet, paramInput.getParameterName(), displayFirsComponent);
                } else if (valueLastComp != null) {
                    addToParamXLS(pos, sheet, paramInput.getParameterName(), displayLastComponent);
                } else {
                    someAdded = false;
                }

            } else {
                if (valueFirstComp != null) {
                    addParamXLS(pos, sheet, paramInput.getParameterName(), displayFirsComponent);
                } else {
                    someAdded = false;
                }
            }
            if (someAdded) {
                pos.increaseRow();
            }
        }
    }

    private void addParamXLS(XLSPosition pos, WritableSheet sheet, String parameterName, String display) throws WriteException {
        addTextCellToSheet(sheet, pos.getCurrentCol(), pos.getCurrentRow(), parameterName + ":");
        addTextCellToSheet(sheet, pos.getCurrentCol() + 1, pos.getCurrentRow(), display);
    }

    private void addRangeParamXLS(XLSPosition pos, WritableSheet sheet, String parameterName, String displayFrom, String displayTo) throws WriteException, Exception {
        addTextCellToSheet(sheet, pos.getCurrentCol(), pos.increaseRow(2), parameterName, getXLSBoldDataFormat());
        addTextCellToSheet(sheet, pos.getCurrentCol(), pos.increaseRow(1), "Desde:");
        addTextCellToSheet(sheet, pos.getCurrentCol() + 1, pos.getCurrentRow(), displayFrom);
        addTextCellToSheet(sheet, pos.getCurrentCol() + 2, pos.getCurrentRow(), "Hasta:");
        addTextCellToSheet(sheet, pos.getCurrentCol() + 3, pos.getCurrentRow(), displayTo);
    }

    private void addToParamXLS(XLSPosition pos, WritableSheet sheet, String parameterName, String displayTo) throws WriteException, Exception {
        addTextCellToSheet(sheet, pos.getCurrentCol(), pos.increaseRow(2), parameterName, getXLSBoldDataFormat());
        addTextCellToSheet(sheet, pos.getCurrentCol(), pos.increaseRow(1), "Hasta:");
        addTextCellToSheet(sheet, pos.getCurrentCol() + 1, pos.getCurrentRow(), displayTo);
    }

    private void addPrintDateToReport(XLSPosition pos, WritableSheet sheet) throws WriteException {
        WritableCellFormat cf = new WritableCellFormat(dateFormat);
        WritableCell cell = new DateTime(pos.getCurrentCol(), pos.getCurrentRow(), new Timestamp(new java.util.Date().getTime()), cf);
        sheet.addCell(cell);

        MUser us = new MUser(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()), null);
        MOrg org = new MOrg(Env.getCtx(), us.getAD_Org_ID(), null);

        addTextCellToSheet(sheet, pos.getCurrentCol(), pos.increaseRow(2), "Responsable:");
        addTextCellToSheet(sheet, pos.getCurrentCol() + 1, pos.getCurrentRow(), us.getName());
        addTextCellToSheet(sheet, pos.getCurrentCol(), pos.increaseRow(), "Organización:");
        addTextCellToSheet(sheet, pos.getCurrentCol() + 1, pos.getCurrentRow(), org.getName());
    }

    private boolean existsFieldWithTransp(XTableColumnModel tcm) {
        for (Enumeration en = tcm.getColumns(true); en.hasMoreElements();) {
            TableColumn column = (TableColumn) en.nextElement();
            FieldDynamicReport field = fieldsInReport.get(column);
            if (field.isIsTransp()) {
                return true;
            }
        }
        return false;
    }

    private int changeOrderby(int indRow, int cantCol, String[] ordenamiento, Vector data) {
        for (int indCol = 0; indCol < cantCol; indCol++) {
            TableColumn column = columnModel.getVisibleColumnByModelIndex(indCol);
            FieldDynamicReport field = fieldsInReport.get(column);

            // Se debe determinar para el caso de campos de ordenamiento si cambia
            // para anexar una linea de resumen

            // valida si ordena por el campo
            if (field.isIsOrderby()) {
                // valida si ya a sido asignado un valor inicial sobre el cual validar
                if (!ordenamiento[indCol].equals("")) {
                    String val = ((Vector) data.elementAt(indRow)).elementAt(column.getModelIndex()).toString();

                    // valida si cambia el valor del campo de ordenamiento
                    if (!ordenamiento[indCol].equals(val)) {
                        ordenamiento[indCol] = val;
                        return indCol;
                    }
                } else {
                    ordenamiento[indCol] = ((Vector) data.elementAt(indRow)).elementAt(column.getModelIndex()).toString();
                }
            }
        }

        return -1;
    }

    private void addTextCellToSheet(WritableSheet sheet, int col, int row, String content) throws WriteException {
        addTextCellToSheet(sheet, col, row, content, commonDataFormat);
    }

    private void addTextCellToSheet(WritableSheet sheet, int col, int row, String content, WritableCellFormat cellFormat) throws WriteException {
        WritableCell cell = new Label(col, row, content, cellFormat);
        sheet.addCell(cell);
    }

    private WritableCellFormat getXLSHeaderFormat() throws Exception {
        // Fuente de la cabecera de la tabla
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
                                           WritableFont.BOLD);
        wf.setColour(Colour.BLACK);
        // Creo un formato y le asigno la fuente anterior
        WritableCellFormat wcf = new WritableCellFormat(wf);
        wcf.setBackground(Colour.GREY_25_PERCENT);
        return wcf;
    }	//	getFormatoCabeceraXLS

    private WritableCellFormat getXLSDataFormat() throws Exception {
        // Fuente de la cabecera de la tabla
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10);
        wf.setColour(Colour.BLACK);
        // Creo un formato y le asigno la fuente anterior
        WritableCellFormat wcf = new WritableCellFormat(wf);
        wcf.setShrinkToFit(false);
        return wcf;
    }	//	getFormatoDatosXLS

    private WritableCellFormat getXLSBoldDataFormat() throws Exception {
        // Fuente de la cabecera de la tabla
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
                                           WritableFont.BOLD);
        wf.setColour(Colour.BLACK);
        // Creo un formato y le asigno la fuente anterior
        WritableCellFormat wcf = new WritableCellFormat(wf);
        wcf.setShrinkToFit(false);
        return wcf;
    }	//	getFormatoDatosXLS

    private WritableCellFormat getXLSResumeDataFormat() throws Exception {
        // Fuente de la cabecera de la tabla
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10,
                                           WritableFont.BOLD);
        wf.setColour(Colour.RED);
        // Creo un formato y le asigno la fuente anterior
        WritableCellFormat wcf = new WritableCellFormat(wf);
        wcf.setShrinkToFit(false);
        return wcf;
    }

    private void addCellToSheet(WritableSheet sheet, FieldDynamicReport fdr, int row, int col, String value) throws WriteException, ParseException {
        WritableCell cell;

        // Tipo de dato fecha
        if (fdr.getDisplayType() == DisplayType.Date || fdr.getDisplayType() == DisplayType.DateTime) {
            java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date parsedUtilDate = df.parse(value);
            Date date = new Date(parsedUtilDate.getTime());
            WritableCellFormat cf = new WritableCellFormat(dateFormat);
            cell = new DateTime(col, row, new Timestamp(date.getTime()), cf);
        } else if (fdr.isNumeric()) { // Tipo de dato numerico
            Double number = Double.parseDouble(value);
            WritableCellFormat cf = new WritableCellFormat(NumberFormats.THOUSANDS_FLOAT);
            cell = new jxl.write.Number(col, row, number.doubleValue(), cf);
        } else if (value == null || value.length() == 0) { // Tipo de dato nulo
            cell = new Label(col, row, "", commonDataFormat);
        } else if (fdr.isYesNo()) {
            Boolean yesNo = ( (String ) value).equals("Y");
            if (yesNo.booleanValue()) {
                cell = new Label(col, row, "SI", commonDataFormat);
            } else {
                cell = new Label(col, row, "NO", commonDataFormat);
            }
        } // Tipo de dato string (pueden haber números que son
        // representados por Strings)
        // Se los va a considerar para que se les de el formato
        // adecuado.
        // Si se puede parsear a Int se lo parsea
        else {
            // COMPARO SI ES UN ENTERO DENTRO DE UN STRING
            // Comparo con una expresion regular si el String es
            // entero
            WritableCellFormat cf = new WritableCellFormat(NumberFormats.THOUSANDS_FLOAT);
            if (value.matches("[0-9]+")) {
               // int val = Integer.valueOf(value).intValue();
                long val = Long.valueOf(value).longValue();
                BigDecimal number = new BigDecimal(val);


                cell = new Number(col, row, number.doubleValue(), cf);
            } else if (value.matches("[0-9]+,[0-9]+")
                       || value.matches("[0-9]+\\.[0-9]+")) {
                double valD = Double.valueOf(value).doubleValue();
                cell = new Number(col, row, valD, cf);
            } else {
                cell = new Label(col, row, value, commonDataFormat);
            }
        }
        sheet.addCell(cell);
    }
}
