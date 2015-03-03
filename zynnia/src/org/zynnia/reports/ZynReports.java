/**
 * ****************************************************************************
 * The contents of this file are subject to the Compiere License Version 1.1 ("License"); You may not use this file
 * except in compliance with the License You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for the specific language governing rights and limitations under the License. The
 * Original Code is Compiere ERP & CRM Business Solution The Initial Developer of the Original Code is Jorg Janke and
 * ComPiere, Inc. Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts created by ComPiere are
 * Copyright (C) ComPiere, Inc.; All Rights Reserved. created by Victor Perez are Copyright (C) e-Evolution,SC. All
 * Rights Reserved. created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved. Contributor(s):
 * Victor Perez ***************************************************************************
 */
package org.zynnia.reports;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.compiere.apps.ALayout;
import org.compiere.apps.ALayoutConstraint;
import org.compiere.apps.AWindow;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.StatusBar;
import org.compiere.apps.Waiting;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.apps.search.Info_Column;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.FieldDynamicReport;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MZYNMODEL;
import org.compiere.model.MZYNMODELCOLUMN;
import org.compiere.model.MZYNMODELTABLE;
import org.compiere.model.MZYNREPORT;
import org.compiere.model.MZYNVIEWCALC;
import org.compiere.model.M_Table;
import org.compiere.plaf.CompierePLAF;
import org.compiere.process.ProcessInfo;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CScrollPane;
import org.compiere.swing.CTabbedPane;
import org.compiere.util.ASyncProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zynnia.reports.ZynReportsGenerator.ExportationAvailable;
import org.zynnia.reports.parameters.ZynParameter;
import org.zynnia.reports.parameters.ZynParameterFactory;
import org.zynnia.reports.printproperties.PrintProperty;
import org.zynnia.reports.printproperties.PropertiesTable;
import org.zynnia.reports.printproperties.PropertiesTableModel;
import org.zynnia.reports.table.FieldComboBoxItem;
import org.zynnia.reports.table.ShowHideComboBoxItem;
import org.zynnia.reports.table.XTableColumnModel;
import org.zynnia.reports.table.XTableModel;
import org.zynnia.utils.BlockingGlassPane;

/**
 * ZYNReports
 *
 * Clase que gestiona un reporte dinámico para el módulo ventanas dinámicas de Compiere tomando los parámetros
 * configurados y generando una interfaz manipulable para los resultados que luego podrán ser exportados a excel y pdf.
 *
 * @author José Fantasia - Zynnia
 * @version $Id: ZYNReports.java,v 1.0 Septiembre 2010
 * @see org.eevolution.form.VMRPDetailed
 */
public class ZynReports extends CPanel implements FormPanel, ActionListener, VetoableChangeListener, ChangeListener, ListSelectionListener, TableModelListener, ASyncProcess, IReportsGeneratorListener {

    /**
     * Creates new form ZYNReports
     */
    public ZynReports() {
        initComponents();
    }

    /**
     * Initialize Panel
     *
     * @param WindowNo window
     * @param frame    frame
     */
    public void init(int WindowNo, FormFrame frame) {
        m_WindowNo = WindowNo;
        m_frame = frame;
        Env.setContext(Env.getCtx(), m_WindowNo, "IsSOTrx", "N");

        try {
            //	UI
            statusBar.setStatusLine("");
            m_frame.setGlassPane(BlockingGlassPane.GLASS_PANE);
            m_frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
            m_frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ZYNReports.init", e);
        }
    }	//	init
    /**
     * Window No
     */
    private int m_WindowNo = 0;
    /**
     * FormFrame
     */
    private FormFrame m_frame;
    private StatusBar statusBar = new StatusBar();
    private int AD_Table_ID = 1000027;
    private M_Table table = null;
    private static CLogger log = CLogger.getCLogger(ZynReports.class);
    /**
     * Master (owning) Window
     */
    protected int p_WindowNo;
    /**
     * Table Name
     */
    private String p_tableName = getTableName();
    /**
     * Key Column Name
     */
    protected String p_keyColumn;
    /**
     * Enable more than one selection
     */
    protected boolean p_multiSelection = true;
    /**
     * Table
     */
    protected MiniTable p_table = new MiniTable();
    /**
     * Column model
     */
    private XTableColumnModel columnModel = new XTableColumnModel();
    /**
     * Layout of Grid
     */
    protected Info_Column[] p_layout;
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
    protected int row = 0;
    /**
     * Loading success indicator
     */
    protected boolean p_loadedOK = false;
    ConfirmPanel confirmPanel = new ConfirmPanel(true, true, true, true, true, true, true);
    protected CPanel parameterPanel = new CPanel();
    /**
     * Window Width
     */
    static final int INFO_WIDTH = 800;
    // Panel de pestañas que incluye la pestaña de configuración de parámetros y la pestaña de resultado
    private CTabbedPane reportPanel;
    // Panelpara seleccionar un reporte específico
    private CPanel reportInstance;
    private CPanel panelBottom;
    private CTabbedPane panelCenter;
    private CPanel panelFind;
    private CPanel results;
    private CPanel mainPanel;
    private CPanel propertiesPanel;
    private CPanel btnExportPanel;
    // Selección del reporte
    private VLookup fzynReport_ID = null;
    private String fzynReportAnt_ID = "";
    private Hashtable<Integer, Integer> indexTable = new Hashtable<Integer, Integer>();
    private Hashtable<Integer, ZynParameter> activeParams = new Hashtable<Integer, ZynParameter>();
    private Hashtable<String, Properties> printProperties = new Hashtable<String, Properties>();
    private Hashtable<TableColumn, FieldDynamicReport> fieldsInReport = new Hashtable<TableColumn, FieldDynamicReport>();
    private PropertiesTable tableEditPrintProperties = new PropertiesTable();
    private CComboBox propertiesFields;
    private JRadioButton pdfBtn;
    private JRadioButton xlsBtn;
    private Waiting m_waiting;
    /**
     * Worker
     */
    private ZynReportsGenerator m_worker = null;

    public void actionPerformed(ActionEvent e) {
        log.log(Level.INFO, "Action Performed del Panel principal");
        String cmd = e.getActionCommand();
        if (cmd.equals(ConfirmPanel.A_OK) || cmd.equals(ConfirmPanel.A_CANCEL)) {
            m_frame.dispose();
        } else {
            log.log(Level.INFO, "Refresh antes del query");
            //  ignore when running
            if (m_worker != null && m_worker.isAlive()) {
                return;
            }

            if (fzynReport_ID.getValue() != null) {
                String report_ID = fzynReport_ID.getValue().toString();
                m_worker = new ZynReportsGenerator(this, Integer.parseInt(report_ID), p_table, activeParams, fieldsInReport, printProperties);
                m_worker.addReportsGeneratorListener(this);
                m_worker.start();
            }
        }
        m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    /**
     * Inicialización de la ventana
     *
     * @Author José Fantasia
     */
    
    
    private void initComponents() {
        p_table.setColumnModel(columnModel);
        p_table.setModel(new XTableModel());
        mainPanel = new CPanel();
        Dimension minDim = new Dimension(800, 750);
        mainPanel.setSize(minDim);
        mainPanel.setMinimumSize(minDim);
        reportPanel = new CTabbedPane();

        reportInstance = new CPanel();
        panelFind = new CPanel();
        panelCenter = new CTabbedPane();
        panelBottom = new CPanel();
        btnExportPanel = new CPanel();

        results = new CPanel();
        results.setLayout(new BorderLayout());
        results.add(new CScrollPane(p_table), BorderLayout.CENTER);

        setLayout(new BorderLayout());

        mainPanel.setLayout(new BorderLayout());
        reportInstance.setLayout(new BorderLayout());
        reportInstance.add(panelFind, BorderLayout.NORTH);
        reportInstance.add(panelCenter, BorderLayout.CENTER);
        reportInstance.add(panelBottom, BorderLayout.SOUTH);

        // TODO: Sacar el 5000346 como id de la columna ZYN_REPORT_ID de la tabla ZYN_REPORTS - 5000289
        // TODO: Sacar el 5000003 como id de la ventana reportes
        int columnId = DB.getSQLValue("txReport", "SELECT ad_column_id FROM ad_column WHERE ad_table_id=? AND columnname=?", MZYNREPORT.Table_ID, "ZYN_REPORT_ID");
        MLookup lookup = null;
        int AD_Role_ID = Env.getContextAsInt(Env.getCtx(), "#AD_Role_ID");
        try {
            lookup = MLookupFactory.get(Env.getCtx(), m_WindowNo, columnId, DisplayType.TableDir,
                                        Env.getLanguage(Env.getCtx()), "ZYN_REPORT_ID", MZYNREPORT.Table_ID,
                                        false, "ZYN_REPORT.ZYN_REPORT_ID IN (SELECT ZYN_REPORT_ID FROM ZYN_REPORT_ACCESS WHERE AD_ROLE_ID = " + AD_Role_ID + ")");
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error building lookup", ex);
        }
        fzynReport_ID = new VLookup("ZYN_REPORT_ID", false, false, true, lookup) {
            @Override
            public void setValue(Object arg0) {
                super.setValue(arg0);
            }
        };

        //parameterPanel.setLayout(new GridLayout(2,2));
        parameterPanel.setLayout(new GridBagLayout());
        fzynReport_ID.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                log.log(Level.INFO, "Action performed de Reporte");
                log.log(Level.INFO, "Ejecutando evento");
                /*
                 * Función que a partir del reporte seleccionado obtiene las tablas y los campos correspondintes para
                 * crear los parámetros del reporte.
                 *
                 */
                if ((fzynReport_ID.getValue() != null) && (!(fzynReport_ID.getValue().toString().equals(fzynReportAnt_ID)))) {
                    cargarParametros(fzynReport_ID.getValue().toString());
                    fzynReportAnt_ID = fzynReport_ID.getValue().toString();
                } 
                //else {
                if (fzynReport_ID.getValue() == null){
                    panelCenter.removeAll();
                }
            }
        });
        fzynReport_ID.setBackground(CompierePLAF.getInfoBackground());

        //  1st Row
        //parameterPanel.add(lzynReport_ID);
        //parameterPanel.add(fzynReport_ID);
        JButton procesar = new JButton("Run");
        procesar.addActionListener(this);
        Insets defaultInsets = new Insets(5, 5, 5, 5);
        parameterPanel.add(procesar);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0; // El área de texto empieza en la columna cero.
        constraints.gridy = 0; // El área de texto empieza en la fila de parámetro.
        constraints.gridwidth = 1; // El área de texto ocupa 1 columna.
        constraints.gridheight = 1; // El área de texto ocupa 1 fila.
        constraints.insets = defaultInsets;

        parameterPanel.add(new CLabel("Seleccione el Modelo"), constraints);

        constraints.gridx = 1; // El área de texto empieza en la columna uno.
        constraints.gridy = 0; // El área de texto empieza en la fila de parámetro.
        constraints.gridwidth = 1; // El área de texto ocupa 1 columna.
        constraints.gridheight = 1; // El área de texto ocupa 1 fila.
        constraints.insets = defaultInsets;

        parameterPanel.add(fzynReport_ID, constraints);

        constraints.gridx = 2; // El área de texto empieza en la columna dos.
        constraints.gridy = 0; // El área de texto empieza en la fila de parámetro.
        constraints.gridwidth = 1; // El área de texto ocupa 1 columna.
        constraints.gridheight = 1; // El área de texto ocupa 1 fila.
        constraints.insets = defaultInsets;

        parameterPanel.add(procesar, constraints);

        constraints.gridx = 0; // El área de texto empieza en la columna dos.
        constraints.gridy = 1; // El área de texto empieza en la fila de parámetro.
        constraints.gridwidth = 3; // El área de texto ocupa 1 columna.
        constraints.gridheight = 1; // El área de texto ocupa 1 fila.
        constraints.insets = defaultInsets;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        parameterPanel.add(btnExportPanel, constraints);
        btnExportPanel.setVisible(false);

        propertiesPanel = new CPanel();
        propertiesPanel.setLayout(new BorderLayout());

        reportPanel.addTab("Parámetros", panelCenter);
        reportPanel.addTab("Resultado", results);
        reportPanel.addTab("Propiedades", propertiesPanel);

        mainPanel.add(parameterPanel, BorderLayout.NORTH);
        mainPanel.add(reportPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        tableEditPrintProperties.getModel().addTableModelListener(this);
    }

    

    /**
     * Lock UI & show Waiting
     */
    private void lock() {
        m_frame.getGlassPane().setVisible(true);
        JFrame frame = Env.getFrame((Container) m_frame);
        if (frame instanceof AWindow) {
            ((AWindow) frame).setBusyTimer(0);
        } else {
            m_waiting = new Waiting(frame, Msg.getMsg(Env.getCtx(), "Processing"), false, 0);
            m_waiting.setModal(true);
        }

        if (m_waiting != null) {
            m_waiting.toFront();
            m_waiting.setVisible(true);
        }
    }   //  lock

    /**
     * Unlock UI & dispose Waiting. Called from run()
     */
    private void unlock() {
        //	Remove Waiting/Processing Indicator
        if (m_waiting != null) {
            m_waiting.dispose();
        }
        m_waiting = null;
        m_frame.getGlassPane().setVisible(false);
    }   //  unlock

    public void dispose() {
        if (m_frame != null) {
            m_frame.dispose();
        }
        m_frame = null;
    }

    /**
     * Get Table name Synonym
     *
     * @return table name
     */
    String getTableName() {
        table = new M_Table(Env.getCtx(), AD_Table_ID, null);
        p_tableName = table.getTableName();

        return p_tableName;
    }   //  getTableName

    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
    }

    public void stateChanged(ChangeEvent e) {
    }

    public void valueChanged(ListSelectionEvent e) {
    }

    public void tableChanged(TableModelEvent e) {
        int editedRow = e.getFirstRow();
        int editedColumn = e.getColumn();
        TableModel model = (TableModel) e.getSource();
        Object data = model.getValueAt(editedRow, editedColumn);
        // Obtengo el elemento seleccionado como para determinar las propiedades de quien estoy editando
        FieldComboBoxItem selectedItem = (FieldComboBoxItem) propertiesFields.getSelectedItem();
        String propKey = selectedItem.getField().getCompleteNameForQuery();

        // Obtengo las propiedades para el campo que se esta editando
        Properties props = printProperties.get(propKey);
        // Obtengo la propiedad editada
        PrintProperty editedProp = ((PropertiesTableModel) tableEditPrintProperties.getModel()).getPropertyForRow(editedRow);
        if (editedProp.equals(PrintProperty.WIDTH_PROPERTY)) {
            if (data == null) {
                data = new BigDecimal(0);
            }
            BigDecimal tmp = (BigDecimal) data;
            if (tmp.doubleValue() > 100) {
                data = BigDecimal.valueOf(100);
            }
            if (tmp.doubleValue() < 0) {
                data = BigDecimal.valueOf(0);
            }
        } else if (data == null) {
            data = "";
        }
        props.put(editedProp, data);
    }

    public void lockUI(ProcessInfo pi) {
    }

    public void unlockUI(ProcessInfo pi) {
    }

    public boolean isUILocked() {
        return false;
    }

    public void executeASync(ProcessInfo pi) {
    }

    private void cargarParametros(String report_ID) {
        log.log(Level.INFO, "Procesando reporte {0}", report_ID);
        panelCenter.removeAll();
        btnExportPanel.removeAll();
        btnExportPanel.setVisible(false);
        activeParams.clear();

        MZYNREPORT x_rpt = new MZYNREPORT(Env.getCtx(), Integer.parseInt(report_ID), null);
        int model = x_rpt.getZYN_MODEL_ID();
        MZYNMODEL zmodel = new MZYNMODEL(Env.getCtx(), model, null);
        CPanel panelAux;
        int idx = 0;
        for (String tableID : zmodel.getIdTablesWithParameters()) {
            int id = Integer.parseInt(tableID);
            MZYNMODELTABLE zmodeltable = new MZYNMODELTABLE(Env.getCtx(), id, null);
            panelAux = new CPanel();
            panelAux.setLayout(new ALayout());
            panelCenter.addTab(zmodeltable.getName(), panelAux);

            // Mantengo una tabla de hash que me determine en que pestaña tengo que tabla de modo
            // que al momento de agregar los campos pueda identificar en que pestaña van.
            indexTable.put(id, idx++);
        }

        int fila = 0;
        for (String idParam : zmodel.getParameters()) {
            int id = Integer.parseInt(idParam);
            MZYNMODELCOLUMN zmodelcolumn = new MZYNMODELCOLUMN(Env.getCtx(), id, null);
            log.log(Level.INFO, "{0}-{1}", new Object[]{zmodelcolumn.getName(), zmodelcolumn.getZYN_MODEL_COLUMN_ID()});
            int tableColumn = zmodelcolumn.getZYN_MODEL_TABLE_ID();
            int indexInstanceTable = Integer.parseInt(indexTable.get(tableColumn).toString());

            panelAux = (CPanel) panelCenter.getComponentAt(indexInstanceTable);
            ZynParameter param = ZynParameterFactory.getParameterTypeInstance(m_WindowNo, zmodelcolumn);
            /*
             * Acá deberíamos discriminar si se agrega parametro simple o compuesto
             */
            addParameterToPanel(param, panelAux, fila);
            fila++;
            activeParams.put(id, param);
        }
    }

    private void addParameterToPanel(ZynParameter param, CPanel panelAux, int fila) {
        panelAux.add(new CLabel(param.getParameterName()), new ALayoutConstraint(fila, 0));
        panelAux.add(param.getFirstComponent(), new ALayoutConstraint(fila, 1));
        if (!param.isSingle()) {
            panelAux.add(param.getLastComponent(), new ALayoutConstraint(fila, 2));
        }
    }

    private void _initStructuresForReport() {
        int totalCols = columnModel.getColumnCount() - 1;
        for (int idx = totalCols; idx >= 0; idx--) {
            removeColumnAndData(p_table, idx);
        }
        p_table.setRowCount(0);
        columnModel.removeAllColumns();
        fieldsInReport.clear();
    }

    // Removes the specified column from the table and the associated
    // call data from the table model.
    public void removeColumnAndData(MiniTable table, int vColIndex) {
        XTableModel model = (XTableModel) table.getModel();
        TableColumn col = table.getColumnModel().getColumn(vColIndex);
        int columnModelIndex = col.getModelIndex();
        Vector data = model.getDataVector();
        Vector colIds = model.getColumnIdentifiers();

        // Remove the column from the table
        table.removeColumn(col);

        // Remove the column header from the table model
        colIds.removeElementAt(columnModelIndex);

        // Remove the column data
        for (int r = 0; r < data.size(); r++) {
            Vector row = (Vector) data.get(r);
            row.removeElementAt(columnModelIndex);
        }
        model.setDataVector(data, colIds);

        // Correct the model indices in the TableColumn objects
        // by decrementing those indices that follow the deleted column
        Enumeration cols = table.getColumnModel().getColumns();
        for (; cols.hasMoreElements();) {
            TableColumn c = (TableColumn) cols.nextElement();
            if (c.getModelIndex() >= columnModelIndex) {
                c.setModelIndex(c.getModelIndex() - 1);
            }
        }
        model.fireTableStructureChanged();
    }

    public void preExecuteReportGenerator() {
        lock();
        statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "Processing"));
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        _initStructuresForReport();
    }

    public void postExecuteReportGenerator(boolean hasErrors) {
        setCursor(Cursor.getDefaultCursor());
        int no = p_table.getRowCount();
        System.out.println("no de lineas de p_Table " + no);
        if (hasErrors) {
            statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "DynamicReportProcessKO"), true);
        } else {
            addButtons();
            statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "ProcessOK"));
            loadPropertiesForField(propertiesFields);
        }
        unlock();
    }

    private void addButtons() {
        MZYNREPORT zreport = new MZYNREPORT(Env.getCtx(), Integer.parseInt(fzynReport_ID.getValue().toString()), null);
        ArrayList<FieldDynamicReport> fields = zreport.getFieldsForReport();
        ArrayList<MZYNVIEWCALC> fieldsCalc = zreport.getColumnsCalc();
        ArrayList<ShowHideComboBoxItem> modelCombo = new ArrayList<ShowHideComboBoxItem>();
        ArrayList<FieldComboBoxItem> modelProp = new ArrayList<FieldComboBoxItem>();
        int column = 0;
        for (FieldDynamicReport field : fields) {
            modelCombo.add(new ShowHideComboBoxItem(column, field));
            modelProp.add(new FieldComboBoxItem(column, field));
            column++;
        }

        for (MZYNVIEWCALC fieldCalc : fieldsCalc) {
            FieldDynamicReport instField = new FieldDynamicReport(fieldCalc.getName(), fieldCalc.isSUM(), fieldCalc.isTRANSP(), fieldCalc.isOrderBy(), fieldCalc.getORDERVIEW());
            modelCombo.add(new ShowHideComboBoxItem(column, instField));
            modelProp.add(new FieldComboBoxItem(column, instField));
            column++;
        }
        CComboBox comboFieldsList = new CComboBox(modelCombo.toArray());
        propertiesFields = new CComboBox(modelProp.toArray());
        comboFieldsList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CComboBox jcmbFields = (CComboBox) e.getSource();
                ShowHideComboBoxItem selectedItem = (ShowHideComboBoxItem) jcmbFields.getSelectedItem();

                TableColumn column = columnModel.getColumnByModelIndex(selectedItem.getNumCol());
                boolean newIsVisible = !selectedItem.isVisible();
                selectedItem.setIsVisible(newIsVisible);
                columnModel.setColumnVisible(column, newIsVisible);
            }
        });
        propertiesFields.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CComboBox jcmbFields = (CComboBox) e.getSource();
                loadPropertiesForField(jcmbFields);
            }
        });
        initPropertiesForField(modelProp);
        loadPropertiesForField(propertiesFields);
        reportPanel.setSelectedIndex(1);

        GridBagConstraints constraints = new GridBagConstraints();
        Insets defaultInsets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0; // El área de texto empieza en la columna dos.
        constraints.gridy = 0; // El área de texto empieza en la fila de parámetro.
        constraints.gridwidth = 1; // El área de texto ocupa 1 columna.
        constraints.gridheight = 1; // El área de texto ocupa 1 fila.
        constraints.insets = defaultInsets;
        constraints.anchor = GridBagConstraints.EAST;

        JButton exportBtn = new JButton("Exportar Reporte");
        exportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_worker.doExport(getExportationType());
            }
        });
        btnExportPanel.add(exportBtn, constraints);

        constraints.gridx = 1; // El área de texto empieza en la columna dos.
        constraints.gridy = 0; // El área de texto empieza en la fila de parámetro.
        constraints.gridwidth = 1; // El área de texto ocupa 1 columna.
        constraints.gridheight = 1; // El área de texto ocupa 1 fila.
        constraints.insets = defaultInsets;
        constraints.anchor = GridBagConstraints.WEST;
        btnExportPanel.add(comboFieldsList, constraints);

        pdfBtn = new JRadioButton("pdf");
        pdfBtn.setActionCommand("pdf");
        pdfBtn.setSelected(true);

        xlsBtn = new JRadioButton("xls");
        xlsBtn.setActionCommand("xls");

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(pdfBtn);
        group.add(xlsBtn);
        constraints.gridx = 2; // El área de texto empieza en la columna dos.
        constraints.gridy = 0; // El área de texto empieza en la fila de parámetro.
        constraints.gridwidth = 1; // El área de texto ocupa 1 columna.
        constraints.gridheight = 1; // El área de texto ocupa 1 fila.
        constraints.insets = defaultInsets;
        constraints.anchor = GridBagConstraints.WEST;
        btnExportPanel.add(pdfBtn, constraints);

        constraints.gridx = 3; // El área de texto empieza en la columna dos.
        constraints.gridy = 0; // El área de texto empieza en la fila de parámetro.
        constraints.gridwidth = 1; // El área de texto ocupa 1 columna.
        constraints.gridheight = 1; // El área de texto ocupa 1 fila.
        constraints.insets = defaultInsets;
        constraints.anchor = GridBagConstraints.WEST;
        btnExportPanel.add(xlsBtn, constraints);

        CPanel comboPropPanel = new CPanel();
        comboPropPanel.setLayout(new GridBagLayout());

        constraints.gridx = 0; // El área de texto empieza en la columna dos.
        constraints.gridy = 0; // El área de texto empieza en la fila de parámetro.
        constraints.gridwidth = 1; // El área de texto ocupa 1 columna.
        constraints.gridheight = 1; // El área de texto ocupa 1 fila.
        constraints.insets = defaultInsets;
        constraints.anchor = GridBagConstraints.CENTER;
        comboPropPanel.add(propertiesFields, constraints);
        propertiesPanel.add(comboPropPanel, BorderLayout.NORTH);
        propertiesPanel.add(new CScrollPane(tableEditPrintProperties), BorderLayout.CENTER);

        btnExportPanel.setVisible(true);
        btnExportPanel.invalidate();
        mainPanel.invalidate();
    }

    private void initPropertiesForField(ArrayList<FieldComboBoxItem> modelProp) {
        float defaultWidth = 1;
        if (modelProp.size() > 0) {
            float size = modelProp.size();
            defaultWidth = (1 / size) * 100;
        }
        for (FieldComboBoxItem item : modelProp) {
            String propKey = item.getField().getCompleteNameForQuery();
            System.out.println("Propiedad: " + propKey);
            Properties props = printProperties.get(propKey);
            if (props == null) {
                props = initializeNewProperties(item.getField().getFieldTitle(), defaultWidth);
                printProperties.put(propKey, props);
            }
        }
    }

    private Properties initializeNewProperties(String defaultTitle, float defaultValue) {
        Properties ret = new Properties();
        for (PrintProperty prop : PrintProperty.values()) {
            Object value = null;
            switch (prop) {
                case LABEL_PROPERTY:
                    value = defaultTitle;
                    break;
                case ALIGN_PROPERTY:
                    value = "Centro";
                    break;
                case WIDTH_PROPERTY:
                    value = new Float(defaultValue);
                    break;
                default:
                    break;
            }
            ret.put(prop, value);
        }
        return ret;
    }

    private void loadPropertiesForField(CComboBox jcmbFields) {
        tableEditPrintProperties.removeAll();
        FieldComboBoxItem selectedItem = (FieldComboBoxItem) jcmbFields.getSelectedItem();

        String propKey = selectedItem.getField().getCompleteNameForQuery();
        System.out.println("Propiedad: " + propKey);
        Properties props = printProperties.get(propKey);

        PropertiesTableModel model = (PropertiesTableModel) tableEditPrintProperties.getModel();
        for (Object key : props.keySet()) {
            PrintProperty prop = (PrintProperty) key;
            Object value = props.get(key);
            model.addEditableProperty(prop, value);
        }
        tableEditPrintProperties.repaint();
        propertiesPanel.validate();
    }

    protected ExportationAvailable getExportationType() {
        ExportationAvailable type = ExportationAvailable.PDF_EXPORTATION;
        if (xlsBtn.isSelected()) {
            type = ExportationAvailable.XLS_EXPORTATION;
        }
        return type;

    }
}
