/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.viewer;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.FieldDynamicReport;
import org.compiere.model.MOrg;
import org.compiere.model.MUser;
import org.compiere.model.MZYNREPORT;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.zynnia.reports.ReportUtils;
import org.zynnia.reports.ZynReports;
import org.zynnia.reports.parameters.ZynParameter;
import org.zynnia.reports.printproperties.PrintProperty;
import org.zynnia.reports.table.XTableColumnModel;

/**
 *
 * @author Alejandro Scott
 */
public class PDFExporter extends Exporter {

    private static CLogger log = CLogger.getCLogger(PDFExporter.class);

    static final int MAX_ROWS_BY_PAGE = 20;

    public PDFExporter(int reportID, Hashtable<Integer, ZynParameter> activeParams, MiniTable p_table, XTableColumnModel columnModel, Hashtable<TableColumn, FieldDynamicReport> fieldsInReport, Hashtable<String, Properties> printProperties) {
        super(reportID, activeParams, p_table, columnModel, fieldsInReport, printProperties);
    }

    @Override
    public void export() {
        if (reportID > 0) {
            // Anexo de generación del pdf con iText
            // step1
            // Dependiendo el tipo de pagina y la orientación defino document
            Document document;
            if (zynReport.getREPORTTYPE().equals("A")) {
                if (zynReport.isPAGEVIEW()) {
                    document = new Document(PageSize.A4, 36, 36, 36, 36);
                } else {
                    document = new Document(PageSize.A4_LANDSCAPE, 36, 36, 36, 36);
                }
            } else if (zynReport.isPAGEVIEW()) {
                document = new Document(PageSize.LEGAL, 36, 36, 36, 36);
            } else {
                document = new Document(PageSize.LEGAL_LANDSCAPE, 36, 36, 36, 36);
            }

            // Create temp file.
            File tempFile = null;
            try {
                tempFile = ReportUtils.createTemporalFile(".pdf");
            } catch (IOException ex) {
                log.log(Level.SEVERE, "Can't create temporal File", ex);
                return;
            }
            try {

                // step2
                PdfWriter.getInstance(document, new FileOutputStream(tempFile));

                // step3
                document.open();

                // step4
                float[] wGeneral = {1f};
                float[] wInfoCab = {1f};
                float[] wEncabezado = {0.7f, 0.3f};
                float[] wParam = {1f};

                PdfPTable tableGeneral = new PdfPTable(wGeneral);
                PdfPTable tableInfoCab = new PdfPTable(wInfoCab);
                PdfPTable tableParam = new PdfPTable(wParam);
                PdfPTable tableEncabezado = new PdfPTable(wEncabezado);

                /*
                 * Image image = Image.getInstance("panalab.jpg"); PdfPCell cellImage; cellImage = new PdfPCell(image);
                 * cellImage.setHorizontalAlignment(Element.ALIGN_CENTER);
                 * cellImage.setVerticalAlignment(Element.ALIGN_MIDDLE); cellImage.setMinimumHeight(100f);
                 */

                Font fuenteNombre = new Font();
                fuenteNombre.setFamily("ARIAL");
                fuenteNombre.setSize(18);

                PdfPCell cellNombre;
                Paragraph paraNombre = new Paragraph(zynReport.getName(), fuenteNombre);
                cellNombre = new PdfPCell(paraNombre);
                cellNombre.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellNombre.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellNombre.setMinimumHeight(40f);
                tableEncabezado.addCell(cellNombre);

                if (zynReport.isPRINTDATE()) {
                    addPrintDateToReport(tableInfoCab, tableEncabezado);
                } else {
                    tableEncabezado.addCell("");
                }

                if (zynReport.isPRINTDESCRIPTION()) {
                    addPrintDescToReport(zynReport.getDescription(), tableEncabezado);
                } else {
                    tableEncabezado.addCell("");
                }

                tableEncabezado.addCell("");

                /*
                 * Va la configuración dinamica de parámetros
                 */
                if (zynReport.isPRINTPARAM()) {
                    addParamsToReport(tableParam);
                }

                /*
                 * Va la configuración dinamica del contenido
                 */
                XTableColumnModel tcm = (XTableColumnModel) p_table.getTableHeader().getColumnModel();

                tableGeneral.addCell(tableEncabezado);
                tableGeneral.addCell(tableParam);
                document.add(tableGeneral);

                constructReportGridPDF(document, (DefaultTableModel) p_table.getModel(), tcm, zynReport);
            } catch (Exception de) {
                log.log(Level.SEVERE, "Error in report generation", de);
            }
            // step5
            document.close();

            showReport(tempFile);
        }
    }

    private void constructReportGridPDF(Document doc, DefaultTableModel model, XTableColumnModel tcm, MZYNREPORT zynReport) {
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

        PdfPTable tableCentro = new PdfPTable(wCentro);
        tableCentro.setHeaderRows(2);
        //tableCentro.setFooterRows(2);

        int pagenumber = 1;

        Font fuentePie = new Font();
        fuentePie.setFamily("ARIAL");
        fuentePie.setSize(6);

        PdfPCell cellPieIzq;
        Paragraph paraPieIzq = new Paragraph(zynReport.getTEXTFOOTER(), fuentePie);
        cellPieIzq = new PdfPCell(paraPieIzq);
        cellPieIzq.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellPieIzq.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellPieIzq.setMinimumHeight(20f);

        PdfPCell cellPieDer;
        Paragraph paraPieDer = new Paragraph("Página N° " + pagenumber, fuentePie);
        cellPieDer = new PdfPCell(paraPieDer);
        cellPieDer.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellPieDer.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellPieDer.setMinimumHeight(20f);

        float[] wPie = {0.7f, 0.3f};

        PdfPTable tablePie = new PdfPTable(wPie);

        tablePie.addCell(cellPieIzq);
        tablePie.addCell(cellPieDer);

        // Vector de los datos con dos dimensiones filas y columnas fila 1 columna 5 sería:
        // ((Vector)getDataVector().elementAt(1)).elementAt(5);

        Vector data = model.getDataVector();

        // Obtener las columnas de la cabecera del reporte y armar dicha cabecera.
        // Recorrer el vector de datos para la fila 0 y la cantidad de columnas y
        // rellenar la cabecera de la tabla HTML.

        Font colHeaderFont = new Font();
        colHeaderFont.setFamily("ARIAL");
        colHeaderFont.setSize(6);
        colHeaderFont.setColor(255, 255, 255);
        colHeaderFont.setStyle(Font.BOLD);

        Font colContentFont = new Font();
        colContentFont.setFamily("ARIAL");
        colContentFont.setSize(6);

        Font colContentResumeFont = new Font();
        colContentResumeFont.setFamily("ARIAL");
        colContentResumeFont.setSize(6);
        colContentResumeFont.setColor(255, 0, 0);

        Paragraph paraParam;
        PdfPCell cellParam;

        /*
         * ELIMINAR ESTO DE LA VERSION FINAL
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
        * */
        /*
         * FIN
         */
        Enumeration<TableColumn> colHeaderItems = tcm.getColumns(true);

        while (colHeaderItems.hasMoreElements()) {
            TableColumn column = colHeaderItems.nextElement();
            FieldDynamicReport field = fieldsInReport.get(column);
            Properties prop = printProperties.get(field.getCompleteNameForQuery());
            String value = (String) prop.get(PrintProperty.LABEL_PROPERTY);

            paraParam = new Paragraph(value, colHeaderFont);
            cellParam = new PdfPCell(paraParam);
            cellParam.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellParam.setVerticalAlignment(Element.ALIGN_TOP);
            cellParam.setBackgroundColor(BaseColor.BLACK);
            tableCentro.addCell(cellParam);
        }

        // Recorrer el vector de datos para el resto de las filas y rellenar
        // con los valores de la tabla la grilla del HTML.
        int cambioOrdenamiento = 0;

        // Implica que esta fila debe primero tratar el resumen del fin
        // del ordenamiento anterior
        //if(changeOrderby(cantRow, cantCol, ordenamiento, data))
        //    cambioOrdenamiento = 1;
        int totalRowsByPage = 0;
        for (int indRow = 0; indRow < cantRow; indRow++) {
            if (totalRowsByPage % MAX_ROWS_BY_PAGE == 0 && totalRowsByPage != 0) {
                try {

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
                            Properties prop = printProperties.get(field.getCompleteNameForQuery());
                            String value = (String) prop.get(PrintProperty.ALIGN_PROPERTY);
                            int aligProp = Element.ALIGN_CENTER;
                            if (value.equals("Izquierda")) {
                                aligProp = Element.ALIGN_LEFT;
                            } else if (value.equals("Derecha")) {
                                aligProp = Element.ALIGN_RIGHT;
                            }

                            paraParam = new Paragraph(Float.toString(acumulado[indColRes]), colContentResumeFont);
                            cellParam = new PdfPCell(paraParam);
                            cellParam.setHorizontalAlignment(aligProp);
                            cellParam.setVerticalAlignment(Element.ALIGN_TOP);
                            cellParam.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            tableCentro.addCell(cellParam);

                        } else {
                            paraParam = new Paragraph("", colContentResumeFont);
                            cellParam = new PdfPCell(paraParam);
                            cellParam.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            tableCentro.addCell(cellParam);
                        }
                    }

                    // increase amount of rows for total
                    doc.add(tableCentro);
                    doc.add(tablePie);

                    // Modifico el pie para mantener el número de página incrementado
                    pagenumber++;
                    paraPieDer = new Paragraph("Página N° " + pagenumber, fuentePie);
                    cellPieDer = new PdfPCell(paraPieDer);
                    cellPieDer.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cellPieDer.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellPieDer.setMinimumHeight(20f);

                    tablePie = new PdfPTable(wPie);
                    tablePie.addCell(cellPieIzq);
                    tablePie.addCell(cellPieDer);

                    tableCentro = new PdfPTable(wCentro);
                    tableCentro.setHeaderRows(2);
                    colHeaderItems = tcm.getColumns();

                    while (colHeaderItems.hasMoreElements()) {
                        TableColumn column = colHeaderItems.nextElement();
                        FieldDynamicReport field = fieldsInReport.get(column);
                        Properties prop = printProperties.get(field.getCompleteNameForQuery());
                        String value = (String) prop.get(PrintProperty.LABEL_PROPERTY);

                        paraParam = new Paragraph(value, colHeaderFont);
                        cellParam = new PdfPCell(paraParam);
                        cellParam.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cellParam.setVerticalAlignment(Element.ALIGN_TOP);
                        cellParam.setBackgroundColor(BaseColor.BLACK);
                        tableCentro.addCell(cellParam);
                    }

                    /*
                     * Anexar la fila de inicio por transporte
                     *
                     */
                    // TODO: Anexar function que controle que exista al menos un campo con transporte
                    totalRowsByPage = 0;
                    if (existsFieldWithTransp(tcm)) {
                        for (int indColRes = 0; indColRes < cantCol; indColRes++) {
                            // Se debe determinar para el caso de campos de ordenamiento si cambia
                            // para anexar una linea de resumen con los campos que puedan expresar funcion
                            if (acumulado[indColRes] != 0) {
                                TableColumn column = columnModel.getVisibleColumnByModelIndex(indColRes);
                                FieldDynamicReport field = fieldsInReport.get(column);
                                Properties prop = printProperties.get(field.getCompleteNameForQuery());
                                String value = (String) prop.get(PrintProperty.ALIGN_PROPERTY);
                                int aligProp = Element.ALIGN_CENTER;
                                if (value.equals("Izquierda")) {
                                    aligProp = Element.ALIGN_LEFT;
                                } else if (value.equals("Derecha")) {
                                    aligProp = Element.ALIGN_RIGHT;
                                }
                                paraParam = new Paragraph(Float.toString(acumulado[indColRes]), colContentResumeFont);
                                cellParam = new PdfPCell(paraParam);
                                cellParam.setHorizontalAlignment(aligProp);
                                cellParam.setVerticalAlignment(Element.ALIGN_TOP);
                                cellParam.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                tableCentro.addCell(cellParam);
                            } else {
                                paraParam = new Paragraph("", colContentResumeFont);
                                cellParam = new PdfPCell(paraParam);
                                cellParam.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                tableCentro.addCell(cellParam);
                            }
                        }
                        totalRowsByPage++;
                    }
                    // Fin TODO
                    doc.newPage();
                } catch (DocumentException ex) {
                    Logger.getLogger(ZynReports.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            // Implica que esta fila debe primero tratar el resumen del fin
            // del ordenamiento anterior
            cambioOrdenamiento = changeOrderby(indRow, cantCol, ordenamiento, data);

            for (int indCol = 0; indCol < cantCol; indCol++) {
                TableColumn column = columnModel.getVisibleColumnByModelIndex(indCol);
                FieldDynamicReport field = fieldsInReport.get(column);

                // Se debe determinar para el caso de campos de ordenamiento si cambia
                // para anexar una linea de resumen con los campos que puedan expresar funcion
                if (cambioOrdenamiento != -1) {
                    if (field.isIsSum()) {
                        Properties prop = printProperties.get(field.getCompleteNameForQuery());
                        String value = (String) prop.get(PrintProperty.ALIGN_PROPERTY);
                        int aligProp = Element.ALIGN_CENTER;
                        if (value.equals("Izquierda")) {
                            aligProp = Element.ALIGN_LEFT;
                        } else if (value.equals("Derecha")) {
                            aligProp = Element.ALIGN_RIGHT;
                        }
                        //TableColumn colOrd = columnModel.getColumnByModelIndex(cambioOrdenamiento);
                        //FieldDynamicReport fieldOrd = fieldsInReport.get(colOrd);
                        //Properties propOrd = printProperties.get(fieldOrd.getCompleteNameForQuery());
                        //String valueOrd = (String) propOrd.get(PrintProperty.LABEL_PROPERTY);

                        paraParam = new Paragraph("Sum " + ((Vector) data.elementAt(indRow - 1)).elementAt(cambioOrdenamiento).toString() + "= " + Float.toString(acumuladoGrupo[cambioOrdenamiento][indCol]), colContentResumeFont);
                        cellParam = new PdfPCell(paraParam);
                        cellParam.setHorizontalAlignment(aligProp);
                        cellParam.setVerticalAlignment(Element.ALIGN_TOP);
                        tableCentro.addCell(cellParam);
                        acumuladoGrupo[cambioOrdenamiento][indCol] = 0;
                    } else {
                        paraParam = new Paragraph("", colContentResumeFont);
                        cellParam = new PdfPCell(paraParam);
                        tableCentro.addCell(cellParam);
                        acumuladoGrupo[cambioOrdenamiento][indCol] = 0;
                    }
                } else {
                    Properties prop = printProperties.get(field.getCompleteNameForQuery());
                    String value = (String) prop.get(PrintProperty.ALIGN_PROPERTY);
                    int aligProp = Element.ALIGN_CENTER;
                    if (value.equals("Izquierda")) {
                        aligProp = Element.ALIGN_LEFT;
                    } else if (value.equals("Derecha")) {
                        aligProp = Element.ALIGN_RIGHT;
                    }

                    /*
                     *  Zynnia - 31/05/2012
                     *  Agregado para evitar que un valor nulo rompa la generación del pdf
                     *  JF
                     *
                     */
                    if(((Vector) data.elementAt(indRow)).elementAt(column.getModelIndex()) == null) {
                        paraParam = new Paragraph("", colContentFont);
                    } else {
                        paraParam = new Paragraph(((Vector) data.elementAt(indRow)).elementAt(column.getModelIndex()).toString(), colContentFont);
                    }
                    cellParam = new PdfPCell(paraParam);
                    cellParam.setHorizontalAlignment(aligProp);
                    cellParam.setVerticalAlignment(Element.ALIGN_TOP);
                    tableCentro.addCell(cellParam);
                    if (field.isIsSum()) {
                        acumulado[indCol] += Float.parseFloat(((Vector) data.elementAt(indRow)).elementAt(column.getModelIndex()).toString());

                        for (int indColAcum = 0; indColAcum < cantCol; indColAcum++) {
                            acumuladoGrupo[indColAcum][indCol] += Float.parseFloat(((Vector) data.elementAt(indRow)).elementAt(column.getModelIndex()).toString());
                        }
                    }
                }
            }

            // Para no perder el registro de cambio (cambioOrdenamiento == 1) vuelvo a evaluarlo
            if (cambioOrdenamiento != -1) {
                indRow = indRow - 1;
            }

            totalRowsByPage++;
            //cambioOrdenamiento = 0;
        }

        // Finalizando el ingreso de todos los registros, debemos hacer el resumen para el caso de que tengamos columnas
        // con funciones
        log.log(Level.FINE, "Tama\u00f1o {0}", data.size());

        for (int allCol = 0; allCol < cantCol; allCol++) {
            TableColumn columnComp = columnModel.getColumnByModelIndex(allCol);
            FieldDynamicReport fieldComp = fieldsInReport.get(columnComp);

            // Se debe determinar para el caso de campos de ordenamiento si cambia
            // para anexar una linea de resumen con los campos que puedan expresar funcion
            if (fieldComp.isIsOrderby()) {
                for (int indCol = 0; indCol < cantCol; indCol++) {
                    TableColumn column = columnModel.getColumnByModelIndex(indCol);
                    FieldDynamicReport field = fieldsInReport.get(column);

                    // Se debe determinar para el caso de campos de ordenamiento si cambia
                    // para anexar una linea de resumen con los campos que puedan expresar funcion
                    if (field.isIsSum()) {
                        Properties prop = printProperties.get(field.getCompleteNameForQuery());
                        String value = (String) prop.get(PrintProperty.ALIGN_PROPERTY);
                        int aligProp = Element.ALIGN_CENTER;
                        if (value.equals("Izquierda")) {
                            aligProp = Element.ALIGN_LEFT;
                        } else if (value.equals("Derecha")) {
                            aligProp = Element.ALIGN_RIGHT;
                        }
                        //TableColumn colOrd = columnModel.getColumnByModelIndex(cambioOrdenamiento);
                        //FieldDynamicReport fieldOrd = fieldsInReport.get(colOrd);
                        //Properties propOrd = printProperties.get(fieldOrd.getCompleteNameForQuery());
                        //String valueOrd = (String) propOrd.get(PrintProperty.LABEL_PROPERTY);

                        paraParam = new Paragraph("Sum " + ((Vector) data.elementAt(cantRow - 1)).elementAt(column.getModelIndex()).toString() + "= " + Float.toString(acumuladoGrupo[allCol][indCol]), colContentResumeFont);
                        cellParam = new PdfPCell(paraParam);
                        cellParam.setHorizontalAlignment(aligProp);
                        cellParam.setVerticalAlignment(Element.ALIGN_TOP);
                        tableCentro.addCell(cellParam);
                        //acumuladoGrupo[cambioOrdenamiento][indCol] = 0;
                    } else {
                        paraParam = new Paragraph("", colContentResumeFont);
                        cellParam = new PdfPCell(paraParam);
                        tableCentro.addCell(cellParam);
                        //acumuladoGrupo[cambioOrdenamiento][indCol] = 0;
                    }
                }
            }
        }

        try {

            /*
             * Anexar la fila de resumen antes del pìe de página
             *
             */
            for (int indColRes = 0; indColRes < cantCol; indColRes++) {
                // Se debe determinar para el caso de campos de ordenamiento si cambia
                // para anexar una linea de resumen con los campos que puedan expresar funcion
                if (acumulado[indColRes] != 0) {
                    TableColumn column = columnModel.getColumnByModelIndex(indColRes);
                    FieldDynamicReport field = fieldsInReport.get(column);
                    Properties prop = printProperties.get(field.getCompleteNameForQuery());
                    String value = (String) prop.get(PrintProperty.ALIGN_PROPERTY);
                    int aligProp = Element.ALIGN_CENTER;
                    if (value.equals("Izquierda")) {
                        aligProp = Element.ALIGN_LEFT;
                    } else if (value.equals("Derecha")) {
                        aligProp = Element.ALIGN_RIGHT;
                    }

                    paraParam = new Paragraph(Float.toString(acumulado[indColRes]), colContentResumeFont);
                    cellParam = new PdfPCell(paraParam);
                    cellParam.setHorizontalAlignment(aligProp);
                    cellParam.setVerticalAlignment(Element.ALIGN_TOP);
                    cellParam.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    tableCentro.addCell(cellParam);
                } else {
                    paraParam = new Paragraph("", colContentResumeFont);
                    cellParam = new PdfPCell(paraParam);
                    cellParam.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    tableCentro.addCell(cellParam);
                }
            }
            doc.add(tableCentro);
            doc.add(tablePie);
        } catch (DocumentException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    private void addParamsToReport(PdfPTable tableParam) {
        for (ZynParameter paramInput : activeParams.values()) {
            //  Valido el tipo de componente para hacer un casting recuperarlo
            //  y verificar si el valor no es nulo.
            String displayFirsComponent = getComponentDisplayText(paramInput.getFirstComponent());
            String displayLastComponent = getComponentDisplayText(paramInput.getLastComponent());
            Object valueFirstComp = getComponentValue(paramInput.getFirstComponent());
            Object valueLastComp = getComponentValue(paramInput.getLastComponent());

            if (paramInput.getLastComponent() != null) {
                if (valueFirstComp != null && valueLastComp != null) {
                    addRangeParamPDF(tableParam, paramInput.getParameterName(), displayFirsComponent, displayLastComponent);
                } else if (valueFirstComp != null) {
                    addParamPDF(tableParam, paramInput.getParameterName(), displayFirsComponent);
                } else if (valueLastComp != null) {
                    addToParamPDF(tableParam, paramInput.getParameterName(), displayLastComponent);
                }

            } else {
                if (valueFirstComp != null) {
                    addParamPDF(tableParam, paramInput.getParameterName(), displayFirsComponent);
                }
            }
        }
    }

    private void addParamPDF(PdfPTable tableParam, String parameterName, String display) {
        Font fuenteParam = new Font();
        fuenteParam.setFamily("ARIAL");
        fuenteParam.setSize(6);

        Paragraph paraParam;
        PdfPCell cellParam;
        paraParam = new Paragraph(parameterName + " = " + display, fuenteParam);
        cellParam = new PdfPCell(paraParam);
        cellParam.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellParam.setVerticalAlignment(Element.ALIGN_TOP);
        tableParam.addCell(cellParam);
    }

    private void addRangeParamPDF(PdfPTable tableParam, String parameterName, String displayFrom, String displayTo) {
        Font fuenteParam = new Font();
        fuenteParam.setFamily("ARIAL");
        fuenteParam.setSize(6);

        Paragraph paraParam;
        PdfPCell cellParam;
        paraParam = new Paragraph(parameterName + " desde " + displayFrom + " hasta " + displayTo, fuenteParam);
        cellParam = new PdfPCell(paraParam);
        cellParam.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellParam.setVerticalAlignment(Element.ALIGN_TOP);
        tableParam.addCell(cellParam);
    }

    private void addToParamPDF(PdfPTable tableParam, String parameterName, String display) {
        Font fuenteParam = new Font();
        fuenteParam.setFamily("ARIAL");
        fuenteParam.setSize(6);

        Paragraph paraParam;
        PdfPCell cellParam;
        paraParam = new Paragraph(parameterName + " hasta " + display, fuenteParam);
        cellParam = new PdfPCell(paraParam);
        cellParam.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellParam.setVerticalAlignment(Element.ALIGN_TOP);
        tableParam.addCell(cellParam);
    }

     private void addPrintDateToReport(PdfPTable tableInfoCab, PdfPTable tableEncabezado) {
        Font fuenteInfoCab = new Font();
        fuenteInfoCab.setFamily("ARIAL");
        fuenteInfoCab.setSize(8);

        Date d = new Date();
        Paragraph paraFecha = new Paragraph(d.toString(), fuenteInfoCab);

        MUser us = new MUser(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()), null);
        MOrg org = new MOrg(Env.getCtx(), us.getAD_Org_ID(), null);

        Paragraph paraResponsable = new Paragraph(us.getName(), fuenteInfoCab);
        Paragraph paraLugar = new Paragraph(org.getName(), fuenteInfoCab);

        PdfPCell cellInfoCab1;
        cellInfoCab1 = new PdfPCell(paraFecha);
        cellInfoCab1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellInfoCab1.setVerticalAlignment(Element.ALIGN_TOP);
        cellInfoCab1.setBorder(0);

        PdfPCell cellInfoCab2;
        cellInfoCab2 = new PdfPCell(paraResponsable);
        cellInfoCab2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellInfoCab2.setVerticalAlignment(Element.ALIGN_TOP);
        cellInfoCab2.setBorder(0);

        PdfPCell cellInfoCab3;
        cellInfoCab3 = new PdfPCell(paraLugar);
        cellInfoCab3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellInfoCab3.setVerticalAlignment(Element.ALIGN_TOP);
        cellInfoCab3.setBorder(0);

        tableInfoCab.addCell(cellInfoCab1);
        tableInfoCab.addCell(cellInfoCab2);
        tableInfoCab.addCell(cellInfoCab3);
        tableEncabezado.addCell(tableInfoCab);
    }

    private void addPrintDescToReport(String description, PdfPTable tableEncabezado) {
        Font fuenteDescripcion = new Font();
        fuenteDescripcion.setFamily("ARIAL");
        fuenteDescripcion.setSize(10);
        PdfPCell cellDescripcion;
        Paragraph paraDescripcion = new Paragraph(description, fuenteDescripcion);
        cellDescripcion = new PdfPCell(paraDescripcion);
        //cellDescripcion.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDescripcion.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellDescripcion.setMinimumHeight(20f);
        tableEncabezado.addCell(cellDescripcion);
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
}
