    /******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/
package org.eevolution.form;

/*
 * MPCProfileBOM.java
 *
 * Created on 8 de julio de 2004, 12:03
 */

/**
 *
 * @author  Ferznado Jimenez
 */

import javax.swing.*;

import javax.swing.event.*;
import java.awt.*;
import org.eevolution.model.MMPCProductBOM;
import org.eevolution.model.MMPCProductBOMLine;
import org.eevolution.model.MMPCProfileBOM;
import org.eevolution.model.MMPCProfileBOMLine;
import org.eevolution.model.MMPCProfileBOMProduct;
import org.eevolution.model.MMPCProfileBOMReal;
import org.eevolution.model.MMPCProfileBOMSelected;

import qs.*;
import java.io.*;
import java.awt.event.*;
import java.sql.*;
import java.math.*;
import java.util.*;
import org.compiere.util.*;
import org.compiere.apps.*;
import org.compiere.apps.form.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.plaf.CompiereColor;
import org.compiere.grid.ed.VLookup;
import java.util.logging.*;

public class MPCProfileBOM extends JFrame implements TableModelListener, ActionListener

{


    public MPCProfileBOM(int MPC_ProfileBOM_ID)
    {
        super();
        addWindowListener
        (new java.awt.event.WindowAdapter()
            {
                public void windowOpened(java.awt.event.WindowEvent evt)
                {
                formWindowOpened(evt);
                }
            }
        );

	m_WindowNo = Env.createWindowNo (this);
	setGlassPane(m_glassPane);
	try
	{

                m_MPC_ProfileBOM_ID=  MPC_ProfileBOM_ID;
                log.info("CreateWO.init");

                dynInit();
                jbInit();
                // Nutirentes requeridos
                executeQuery();
                // Ingredientes a utilizar
                formular();
                // Ingredientes seleccionados
                executeQuery3();
                // query de reales
                executeQueryReal();
                // createMenu();
                this.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
                //     System.out.println("Valor de Profile0      " + m_MPC_ProfileBOM_ID);
                this.setSize(750,650);
                this.show();
                this.maximize=true;
                this.pack();
                this.setTitle("Modulo de Formulaci�n");
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE,"FormFrame", e);
		}


		this.setTitle("Modulo de Formulación");


    }


	/**
	 * 	Static Init
	 * 	@throws Exception
	 */
	private void dynInit() throws Exception
	{


		CompiereColor.setBackground(this);
		//
		this.setIconImage(org.compiere.Compiere.getImage16());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}	//	jbInit


        private int          m_WindowNo = 0;
        private String      m_nut;
        private String      m_ing;
        private String ingq="";
        public static final String	FORMATTYPE_COMMA = "C";
        public static final String	FORMATTYPE_TAB = "T";
	private FormFrame    m_frame;
        private boolean      m_isLocked = false;
        private boolean      m_solounnut = false;
        private boolean      m_nutreq=false;
        private boolean      m_validateing = true;
        private boolean      m_finished =false;
        private boolean m_exists  = false;
        private BigDecimal maximo = Env.ZERO;
        private Color fg;
        private String					m_M_AttributeSetInstanceName;
        private String       tblSql = null;
        private String       sqlWhere = "1 = 1";
        private Properties   ctx = Env.getCtx();
        private String          m_whereClause;
        private String         m_M_Attribute_ID;
        private ArrayList	m_productEditors = new ArrayList();
        private String		m_query = "";
        private String nutq ="";
	/** The GlassPane           	*/
	private AGlassPane  m_glassPane = new AGlassPane();
	/**	Description					*/
	private String		m_Description = null;
	/**	Help						*/
	private String		m_Help = null;
	/**	Menu Bar					*/
	private JMenuBar 	menuBar = new JMenuBar();
	/**	The Panel to be displayed	*/
	private FormPanel 	m_panel = null;
	public VLookup nutrientes;
        public VLookup ingredientes;
	public boolean maximize = false;
	public MLookup lookup;
        private ArrayList	m_instanceEditors = new ArrayList();
        private String productst =new String();
        private String fname;
        private int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));

        // the file format is LP (default)
        private boolean isLpFile;
        private int cont500=0;
        // use primal simplex algorithm to solve problem (default)
        private boolean solvePrimal;

        // print variable values (default optimal val only)
        private boolean printX=true;

        // print a complete solution report
        private boolean printReport;

        // turn on simplex tracing (default false)
        private boolean traceSimplex;

        // pricing strategy DANTZIG is default;
        private int price;
        private int DANTZIG = 0;
        private int STEEP = 1;
        private int DEVEX = 2;
        private   String pba = "";
        private ArrayList pbaarray= new ArrayList();
        private                String pba2="";
        private static CLogger log = CLogger.getCLogger(MPCProfileBOM.class);
                //private BorderLayout mainLayout = new BorderLayout();
private MAttributeSetInstance	m_masi;
  private CPanel       mainPanel  = new CPanel();
        private JFrame		m_frame1;

	private JLabel       dataStatus = new JLabel();
       private int cont50=0;
	private JScrollPane  dataPane   = new JScrollPane();
        private JScrollPane  dataPane2   = new JScrollPane();
        private JScrollPane  dataPane3   = new JScrollPane();
         private JScrollPane  dataPane4   = new JScrollPane();
         private JScrollPane  dataPane5   = new JScrollPane();
         private JScrollPane  dataPane6   = new JScrollPane();
         private JScrollPane  dataPane7   = new JScrollPane();
         private JScrollPane  dataPane8   = new JScrollPane();
          private JScrollPane  dataPane9   = new JScrollPane();
          private JScrollPane  dataPane10   = new JScrollPane();
	private MiniTable    miniTable  = new MiniTable();
        private MiniTable    miniTable2  = new MiniTable();
        private MiniTable    miniTable3  = new MiniTable();
          private MiniTable    miniTable4  = new MiniTable();
          private MiniTable miniTable5 = new MiniTable();
          private MiniTable miniTable6 = new MiniTable();
          private MiniTable miniTable7 = new MiniTable();
          private MiniTable miniTable8 = new MiniTable();
          private MiniTable miniTable9 = new MiniTable();
          private MiniTable miniTable10 = new MiniTable();
        private BorderLayout mainLayout = new BorderLayout();
        private     ArrayList array = new ArrayList();
        private ArrayList arregloing = new ArrayList();
        private Vector ingredients = new Vector();
        private StatusBar statusBar = new StatusBar();
        private int m_MPC_ProfileBOM_ID=0;
        private AppsAction	    aReport, aEnd, aHome, aHelp, aProduct,
							aAccount, aCalculator, aCalendar, aEditor, aPreference, aScript,
							aOnline, aMailSupport, aAbout, aPrintScr, aScrShot, aExit, aBPartner;

        private BigDecimal cero = new BigDecimal(0.0);
        private String m_value="";
        private BigDecimal nuting = new BigDecimal(0.0);
        private BigDecimal minimo = new BigDecimal(0.0);
        private String nutriente_id = "";
        private String esta ="";
        private int estan=0;
        private ArrayList cant = new ArrayList();
        private Timestamp 	m_PriceDate;

	private boolean 	m_calculated = false;
        private boolean         m_vehiculo=false;
	private BigDecimal 	m_PriceList = Env.ZERO;
	private BigDecimal 	m_PriceStd = Env.ZERO;
	private BigDecimal 	m_PriceLimit = Env.ZERO;
	private int 		m_C_Currency_ID = 0;
	private int		m_M_PriceList_Version_ID = 0;
        private int             product_id =0;
        private ArrayList preciostd = new ArrayList();
        private ArrayList cantvar = new ArrayList();
        private ArrayList validacion = new ArrayList();
        private ArrayList prodsel = new ArrayList();
        private ArrayList kilosarr = new ArrayList();
        private ArrayList nutarr = new ArrayList();
        private ArrayList ingselfinal = new ArrayList();
        private ArrayList   arringconnut = new ArrayList();
        private ArrayList   prarr =new ArrayList();
        private int cont8=0;
        private int cont9=0;
        private int cont10=0;
        private int cont11=0;
        private BigDecimal menor = Env.ZERO;
        private BigDecimal mayor = Env.ZERO;
        private BigDecimal kilos = Env.ZERO;
        private BigDecimal kg2 = Env.ZERO;
        private BigDecimal sumakilos = Env.ZERO;
        private BigDecimal precio2bd = Env.ZERO;
        private BigDecimal calc1= Env.ZERO;
        private BigDecimal calc2 = Env.ZERO;
        private BigDecimal calc3 = Env.ZERO;
        private BigDecimal prantbd = Env.ZERO;
        private BigDecimal prprodbd = Env.ZERO;
        private BigDecimal kgantbd = Env.ZERO;
        private BigDecimal prnuevo = Env.ZERO;
        private BigDecimal prprod = Env.ZERO;
        private BigDecimal ingnutbd = Env.ZERO;
        private BigDecimal nutbd =Env.ZERO;
        private BigDecimal nutkgbd = Env.ZERO;
        private BigDecimal nutprbd =Env.ZERO;
        private BigDecimal val1=Env.ZERO;
        private String ingstsel="";
        private String kgstsel="";
        private String ingq1="";
        private int m_prod_id =0;

   /**
	 *  Create Menu
	 */
	private void createMenu()
	{
		//      File

		JMenu mFile = AEnv.getMenu("File");
		menuBar.add(mFile);
		AEnv.addMenuItem("PrintScreen", null, KeyStroke.getKeyStroke(KeyEvent.VK_PRINTSCREEN, 0), mFile, this);
		AEnv.addMenuItem("ScreenShot", null, KeyStroke.getKeyStroke(KeyEvent.VK_PRINTSCREEN, Event.SHIFT_MASK), mFile, this);
		AEnv.addMenuItem("Report", null, KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.ALT_MASK), mFile, this);
		mFile.addSeparator();
		AEnv.addMenuItem("End", null, KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.ALT_MASK), mFile, this);
		AEnv.addMenuItem("Exit", null, KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.SHIFT_MASK+Event.ALT_MASK), mFile, this);

		//      View
		JMenu mView = AEnv.getMenu("View");
		menuBar.add(mView);
		AEnv.addMenuItem("InfoProduct", null, KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK), mView, this);
		AEnv.addMenuItem("InfoBPartner", null, KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.SHIFT_MASK+Event.CTRL_MASK), mView, this);
		AEnv.addMenuItem("InfoAccount", null, KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.ALT_MASK+Event.CTRL_MASK), mView, this);
		mView.addSeparator();
		AEnv.addMenuItem("InfoOrder", "Info", null, mView, this);
		AEnv.addMenuItem("InfoInvoice", "Info", null, mView, this);
		AEnv.addMenuItem("InfoInOut", "Info", null, mView, this);
		AEnv.addMenuItem("InfoPayment", "Info", null, mView, this);
		AEnv.addMenuItem("InfoSchedule", "Info", null, mView, this);

		//      Tools
		JMenu mTools = AEnv.getMenu("Tools");
		menuBar.add(mTools);
		AEnv.addMenuItem("Calculator", null, null, mTools, this);
		AEnv.addMenuItem("Calendar", null, null, mTools, this);
		AEnv.addMenuItem("Editor", null, null, mTools, this);
		AEnv.addMenuItem("Script", null, null, mTools, this);
		mTools.addSeparator();
		AEnv.addMenuItem("Preference", null, null, mTools, this);

		//      Help
		JMenu mHelp = AEnv.getMenu("Help");
		menuBar.add(mHelp);
		AEnv.addMenuItem("Help", "Help", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),	mHelp, this);
		AEnv.addMenuItem("Online", null, null, mHelp, this);
		AEnv.addMenuItem("EMailSupport", null, null, mHelp, this);
		AEnv.addMenuItem("About", null, null, mHelp, this);
	}   //  createMenu








  /* public void init (int WindowNo, FormFrame frame)

	{

		log.log(Level.INFO, "CreateWO.init");

		m_WindowNo = WindowNo;

		m_frame = frame;
                m_frame1 = new JFrame();

		try

		{
                   //  initComponents();
                     //dynInit();
                    jbInit();
                    executeQuery();
                    executeQuery2();
                        frame.setSize(650,550);
                     frame.getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
                       frame.setSize(650,550);
                     System.out.println("Valor de Profile4      " + m_MPC_ProfileBOM_ID);
               //      frame.show();
                   //  m_frame.maximize=true;
                 //    frame.pack();



		}

		catch(Exception e)

		{

			log.log(Level.SEVERE"CreateWO.init", e);

		}

	}	//	init
*/
    /* private void dynInit()

	{
             System.out.println("Entra a dynINIT      ");

    tblSql = miniTable.prepareTable(new ColumnInfo[] {

			new ColumnInfo(" ", "o.C_Order_ID", IDColumn.class, false, false, null),

			new ColumnInfo(Msg.translate(ctx, "DocumentNo"), "o.DocumentNo", String.class),

		//	new ColumnInfo(Msg.translate(ctx, "DateOrdered"), "o.DateOrdered", Timestamp.class),

			new ColumnInfo(Msg.translate(ctx, "Description"), "o.Description", String.class),

 			},

			//	FROM

			"C_Order o"

			+ " INNER JOIN C_BPartner bp ON (o.C_BPartner_ID=bp.C_BPartner_ID)"

			+ " INNER JOIN M_Warehouse w ON (o.M_Warehouse_ID=w.M_Warehouse_ID)",

			//	WHERE

			sqlWhere,

			true, "o");



   // miniTable.getModel().addTableModelListener(this);



	//	statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "GenerateWorkOrders"));

	//	statusBar.setStatusDB(" ");



	}   //  dynInit
*/
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
                   /*
    private void initComponents() {//GEN-BEGIN:initComponents
        jDesktopPane1 = new javax.swing.JDesktopPane();
        Ingredients = new javax.swing.JInternalFrame();
        jTable3 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        Selected = new javax.swing.JInternalFrame();
        jButton3 = new javax.swing.JButton();
        jTable2 = new javax.swing.JTable();
        Nutrients = new javax.swing.JInternalFrame();
        jPanel4 = new javax.swing.JPanel();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton4 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();

        Ingredients.setVisible(true);
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        Ingredients.getContentPane().add(jTable3, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jLabel2.setText("jLabel2");
        jPanel1.add(jLabel2);

        jTextField1.setText("jTextField1");
        jPanel1.add(jTextField1);

        jLabel1.setText("jLabel1");
        jPanel1.add(jLabel1);

        jTextField2.setText("jTextField2");
        jPanel1.add(jTextField2);

        Ingredients.getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jButton2.setText("jButton2");
        jPanel2.add(jButton2);

        jButton5.setText("jButton5");
        jPanel2.add(jButton5);

        Ingredients.getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        Ingredients.setBounds(20, 160, 210, 150);
        jDesktopPane1.add(Ingredients, javax.swing.JLayeredPane.DEFAULT_LAYER);

        Selected.setVisible(true);
        jButton3.setText("jButton3");
        Selected.getContentPane().add(jButton3, java.awt.BorderLayout.SOUTH);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        Selected.getContentPane().add(jTable2, java.awt.BorderLayout.NORTH);

        Selected.setBounds(270, 130, 190, 300);
        jDesktopPane1.add(Selected, javax.swing.JLayeredPane.DEFAULT_LAYER);

        Nutrients.setResizable(true);
        Nutrients.setVisible(true);
        jPanel4.setLayout(new java.awt.BorderLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jPanel4.add(jTable1, java.awt.BorderLayout.CENTER);

        jButton1.setText("jButton1");
        jPanel4.add(jButton1, java.awt.BorderLayout.SOUTH);

        Nutrients.getContentPane().add(jPanel4, java.awt.BorderLayout.WEST);

        jPanel3.setLayout(new java.awt.GridLayout(1, 0, 0, 5));

        jButton4.setText("jButton4");
        jToolBar1.add(jButton4);

        jToolBar1.add(jComboBox1);

        jPanel3.add(jToolBar1);

        Nutrients.getContentPane().add(jPanel3, java.awt.BorderLayout.NORTH);

        Nutrients.setBounds(0, 10, 520, 120);
        jDesktopPane1.add(Nutrients, javax.swing.JLayeredPane.DEFAULT_LAYER);

        getContentPane().add(jDesktopPane1, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

private void jInternalFrame1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jInternalFrame1FocusGained
// TODO add your handling code here:
}//GEN-LAST:event_jInternalFrame1FocusGained
*/


 void jbInit() throws Exception {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        Ingredients = new javax.swing.JInternalFrame();
        jButton2 = new javax.swing.JButton();
        jTable3 = new javax.swing.JTable();
        Selected = new javax.swing.JInternalFrame();
        jButton3 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jTable2 = new javax.swing.JTable();
        Nutrients = new javax.swing.JInternalFrame();
        jPanel4 = new javax.swing.JPanel();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jButtonP = new javax.swing.JButton();
        jButtonNR = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jButton47 = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        jButton58 = new javax.swing.JButton();
        jButton59 = new javax.swing.JButton();
        jButton100 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButtonN = new javax.swing.JButton();
        jButtonI = new javax.swing.JButton();
        jButtonG = new javax.swing.JButton();
        jButtonCP = new javax.swing.JButton();
        jButtonCA = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        lista = new javax.swing.JInternalFrame();
        jInternalFrame5 = new javax.swing.JInternalFrame();
        jInternalFrame6 = new javax.swing.JInternalFrame();
        jInternalFrame7 = new javax.swing.JInternalFrame();
        jInternalFrame8 = new javax.swing.JInternalFrame();
        jInternalFrame9 = new javax.swing.JInternalFrame();
        jInternalFrame10 = new javax.swing.JInternalFrame();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanelEmp = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();

        CompiereColor.setBackground(this);
        mainPanel.add(dataStatus, BorderLayout.SOUTH);
	mainPanel.add(dataPane, BorderLayout.CENTER);
        getContentPane().setLayout(mainLayout);


        MLookup nut = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 8512, DisplayType.Search);
	nutrientes = new VLookup ("M_Attribute_ID", false, false, true, nut);
        miniTable.addColumn("MPC_ProfileBOMLine_ID");
        miniTable.addColumn("I");
        miniTable.addColumn("Imx");
        miniTable.addColumn("Código");
	miniTable.addColumn("Name");
        miniTable.addColumn("UOM");
        miniTable.addColumn("Min");
        miniTable.addColumn("Max");
        miniTable.setMultiSelection(true);
	miniTable.setRowSelectionAllowed(true);
	//  set details
	miniTable.setColumnClass(0, IDColumn.class, false, " ");
	miniTable.setColumnClass(1, Boolean.class, false, Msg.translate(Env.getCtx(), "I"));
        miniTable.setColumnClass(2, Boolean.class, false, Msg.translate(Env.getCtx(), "Imx"));
	miniTable.setColumnClass(3, String.class, true, Msg.translate(Env.getCtx(), "C�digo"));
	miniTable.setColumnClass(4, String.class, true, Msg.translate(Env.getCtx(), "Name"));
        miniTable.setColumnClass(5, String.class, true, Msg.translate(Env.getCtx(), "UOM"));
        miniTable.setColumnClass(6, VNumber.class, false, Msg.translate(Env.getCtx(), "Min"));
        miniTable.setColumnClass(7, VNumber.class, false, Msg.translate(Env.getCtx(), "Max"));
	miniTable.autoSize();

	//	Info
	statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InOutGenerateSel"));//@@
	statusBar.setStatusDB(" ");
	miniTable.getModel().addTableModelListener(this);
        dataPane.getViewport().add(miniTable,null);
        miniTable.selectAll();



     //   celleditor.shouldSelectCell();
//    TableColumnModel columnModel = miniTable.getColumnModel();
// DefaultTableCellRenderer headerRenderer = new
// DefaultTableCellRenderer();
// headerRenderer.setOpaque(true);
// headerRenderer.setBackground(Color.red);
// headerRenderer.setForeground(Color.white);
// headerRenderer.setBorder(BorderFactory.createRaisedBevelBorder());
// headerRenderer.setToolTipText("Click here to sort by this column");
// headerRenderer.setHorizontalAlignment(JLabel.CENTER);
//
//
// headerRenderer.setFont(new Font("Courier", Font.ITALIC, 20));
// TableColumn aColumn = columnModel.getColumn(4);
//
//
// miniTable.getTableHeader().resizeAndRepaint();
          Nutrients.setTitle("Nutrientes requeridos");

        Nutrients.setResizable(true);
        Nutrients.setVisible(true);

        jPanel4.setLayout(new java.awt.BorderLayout());
        jPanel4.add(dataPane, java.awt.BorderLayout.CENTER);
        jButton1.setText("Guardar");
        jPanel4.add(jButton1, java.awt.BorderLayout.SOUTH);
        Nutrients.getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);
        jPanel3.setLayout(new java.awt.GridLayout(1, 3, 30, 0));
        jButton4.setText("Eliminar");
        jButton4.setSize(25,25);
        jButton4.setMaximumSize(new java.awt.Dimension(30, 25));
        jButton4.isMaximumSizeSet();
        jPanel3.add(jButton4);
        jButton5.setText("Agregar");
        jPanel3.add(jButton5);
        //
	jButtonN.setText("Nutrientes");
        jPanel3.add(jButtonN);
        Nutrients.setIconifiable(true);
        Nutrients.setMaximizable(true);
        jPanel3.add(nutrientes);
        Nutrients.getContentPane().add(jPanel3, java.awt.BorderLayout.NORTH);
        Nutrients.setBounds(0, 0, 420, 325);
        jDesktopPane1.add(Nutrients, javax.swing.JLayeredPane.DEFAULT_LAYER);

                // LISTA
                MLookup ing = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 1000008, DisplayType.Search);
		ingredientes = new VLookup ("MPC_ProfileBOM_ID", false, false, true, ing);

                miniTable4.addColumn("MPC_ProfileBOMLine_ID");
                miniTable4.addColumn("C");
                miniTable4.addColumn("G");
		miniTable4.addColumn("Value");
		miniTable4.addColumn("Name");
                miniTable4.addColumn("Min");
                miniTable4.addColumn("Max");
                miniTable4.addColumn("QtyAvailable");
                miniTable4.addColumn("PriceList");
                miniTable4.addColumn("QtyOnHand");
		miniTable4.addColumn("QtyReserved");
		miniTable4.addColumn("QtyOrdered");
	//	miniTable.addColumn("DateOrdered");
	//	miniTable.addColumn("TotalLines");
		//
		miniTable4.setMultiSelection(true);
		miniTable4.setRowSelectionAllowed(true);
		//  set details
		miniTable4.setColumnClass(0, IDColumn.class, false, " ");
		//miniTable.setColumnClass(0, String.class, true, Msg.translate(Env.getCtx(), "M_Product_ID"));
		miniTable4.setColumnClass(1, Boolean.class, false, Msg.translate(Env.getCtx(), "C"));
                miniTable4.setColumnClass(2, Boolean.class, false, Msg.translate(Env.getCtx(), "G"));
                //miniTable4.setColumnClass(3, String.class, true, Msg.translate(Env.getCtx(), "Value"));
                miniTable4.setColumnClass(3, KeyNamePair.class, true, Msg.translate(Env.getCtx(), "Value"));
		miniTable4.setColumnClass(4, String.class, true, Msg.translate(Env.getCtx(), "Name"));
                miniTable4.setColumnClass(5, VNumber.class, false, Msg.translate(Env.getCtx(), "Min"));
                miniTable4.setColumnClass(6, VNumber.class, false, Msg.translate(Env.getCtx(), "Max"));
                miniTable4.setColumnClass(7, String.class, true, Msg.translate(Env.getCtx(), "QtyAvailable"));
                miniTable4.setColumnClass(8, VNumber.class, true, Msg.translate(Env.getCtx(), "PriceList"));
                miniTable4.setColumnClass(9, String.class, true, Msg.translate(Env.getCtx(), "QtyOnHand"));
                miniTable4.setColumnClass(10, String.class, true, Msg.translate(Env.getCtx(), "QtyReserved"));
                miniTable4.setColumnClass(11, String.class, true, Msg.translate(Env.getCtx(), "QtyOrdered"));
                //               miniTable.setColumnClass(4, String.class, true, Msg.translate(Env.getCtx(), "C_BPartner_ID"));
//		miniTable.setColumnClass(5, Timestamp.class, true, Msg.translate(Env.getCtx(), "DateOrdered"));
//		miniTable.setColumnClass(6, BigDecimal.class, true, Msg.translate(Env.getCtx(), "TotalLines"));
		//
		miniTable4.autoSize();
		miniTable4.getModel().addTableModelListener(this);
		//	Info
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InOutGenerateSel"));//@@
		statusBar.setStatusDB(" ");


		miniTable4.getModel().addTableModelListener(this);
                dataPane4.getViewport().add(miniTable4,null);

                  String val = "";
                  String nombre = "";
                try
                  {
                      StringBuffer sql1 = new StringBuffer("SELECT Value,Name FROM MPC_ProfileBOM WHERE AD_Client_ID= "+ AD_Client_ID + " AND MPC_ProfileBOM_ID=?");
    		PreparedStatement pstmt = DB.prepareStatement(sql1.toString());
			pstmt.setInt(1,m_MPC_ProfileBOM_ID);


			ResultSet rs = pstmt.executeQuery();

			//
			if (rs.next())
			{
				 val = rs.getString(1);
                                 nombre = rs.getString(2);


			}
			rs.close();
			pstmt.close();
                  }
                  catch(SQLException s)
                  {
                      log.log(Level.SEVERE, "ERROR:", s);
                  }
           lista.setTitle(val +"-"+nombre);
        lista.setResizable(true);
        lista.setVisible(true);
        lista.setIconifiable(true);
        lista.setMaximizable(true);
        jPanel10.setLayout(new java.awt.BorderLayout());
        jPanel10.add(dataPane4, java.awt.BorderLayout.CENTER);
        jButton10.setText("Formular");
        jPanel10.add(jButton10, java.awt.BorderLayout.SOUTH);
        lista.getContentPane().add(jPanel10, java.awt.BorderLayout.CENTER);
        jPanel11.setLayout(new java.awt.GridLayout(1, 3, 30, 0));
        jButton11.setText("Eliminar");
        jButton11.setSize(25,25);
        jButton11.setMaximumSize(new java.awt.Dimension(30, 25));
        jButton11.isMaximumSizeSet();
        jPanel11.add(jButtonG);
        jPanel11.add(jButton11);
        jButton12.setText("Agregar");
        jPanel11.add(jButton12);
        jButtonI.setText("Ings");
        jButtonP.setText("Prods");
        jButtonNR.setText("NRs");
        jButtonG.setText("Guardar");
        jPanel11.add(jButtonI);
        jPanel11.add(jButtonP);
        jPanel11.add(jButtonNR);
        jPanel11.add(ingredientes);
        lista.getContentPane().add(jPanel11, java.awt.BorderLayout.NORTH);
        lista.setBounds(0, 325, 1000, 325);
        jDesktopPane1.add(lista, javax.swing.JLayeredPane.DEFAULT_LAYER);


//        lista.setVisible(true);
//        lista.getContentPane().add(dataPane4, java.awt.BorderLayout.CENTER);
//        lista.setTitle("Ingredientes Propuestos");
//        lista.setResizable(true);
//        jButton5.setText("Formular");
//        lista.getContentPane().add(jButton5, java.awt.BorderLayout.SOUTH);
        jButton10.addActionListener(this);
        jButtonI.addActionListener(this);
        jButtonP.addActionListener(this);
        jButtonNR.addActionListener(this);
        jButtonG.addActionListener(this);
        jButtonN.addActionListener(this);
         jButton1.addActionListener(this);

         jButton11.addActionListener(this);
        jButton4.addActionListener(this);
        jButton12.addActionListener(this);
        jButton5.addActionListener(this);




         Ingredients.setVisible(true);

         miniTable2.addColumn("M_Product_ID");
         miniTable2.addColumn("C");
         miniTable2.addColumn("G");
         miniTable2.addColumn("P");
         miniTable2.addColumn("Value");
	 miniTable2.addColumn("Name");
         miniTable2.addColumn("PriceList");
         miniTable2.addColumn("%");
         miniTable2.addColumn("Kg");
	 //miniTable.addColumn("DocumentNo");
	 //miniTable.addColumn("C_BPartner_ID");
	 //miniTable.addColumn("DateOrdered");
	 //miniTable.addColumn("TotalLines");
	 miniTable2.setMultiSelection(true);
	 miniTable2.setRowSelectionAllowed(true);
	 //  set details
	 miniTable2.setColumnClass(0, IDColumn.class, false, " ");
	 miniTable2.setColumnClass(1, Boolean.class, false, Msg.translate(Env.getCtx(), "C"));
         miniTable2.setColumnClass(2, Boolean.class, false,Msg.translate(Env.getCtx(), "G"));
         miniTable2.setColumnClass(3, Boolean.class, true, Msg.translate(Env.getCtx(), "P"));
	 miniTable2.setColumnClass(4, String.class, true, Msg.translate(Env.getCtx(), "Value"));
         miniTable2.setColumnClass(5, String.class, true, Msg.translate(Env.getCtx(), "Name"));
 	 miniTable2.setColumnClass(6, VNumber.class, true, Msg.translate(Env.getCtx(), "PriceList"));
         miniTable2.setColumnClass(7, VNumber.class, true, Msg.translate(Env.getCtx(), "%"));
         miniTable2.setColumnClass(8, VNumber.class, false, Msg.translate(Env.getCtx(), "Kg"));
         //miniTable.setColumnClass(4, String.class, true, Msg.translate(Env.getCtx(), "C_BPartner_ID"));
         //miniTable.setColumnClass(5, Timestamp.class, true, Msg.translate(Env.getCtx(), "DateOrdered"));
         //miniTable.setColumnClass(6, BigDecimal.class, true, Msg.translate(Env.getCtx(), "TotalLines"));
	 miniTable2.autoSize();
         miniTable2.getModel().addTableModelListener(this);
         miniTable2.setCellSelectionEnabled(true);
         //miniTable2.setFocusable(true);
         miniTable2.transferFocusDownCycle();
         miniTable2.addColumnSelectionInterval(0,3);
         miniTable2.getColorCode(50);
         miniTable2.getColumnSelectionAllowed();
         miniTable2.grabFocus();
	 miniTable2.getModel().addTableModelListener(this);
         dataPane2.getViewport().add(miniTable2,null);
         miniTable2.autoSize();


         miniTable3.addColumn("M_Product_ID");
	 //miniTable3.addColumn("Value");
         miniTable3.addColumn("P");
         miniTable3.addColumn("IMx");
	 miniTable3.addColumn("Name");
         //miniTable3.addColumn("Unidad");
         miniTable3.addColumn("Min");
         miniTable3.addColumn("Real");
         miniTable3.addColumn("Max");
	 //miniTable.addColumn("DocumentNo");
	 //miniTable.addColumn("C_BPartner_ID");
	 //miniTable.addColumn("DateOrdered");
	 //miniTable.addColumn("TotalLines");
	 miniTable3.setMultiSelection(true);
	 miniTable3.setRowSelectionAllowed(true);
	//  set details
	 miniTable3.setColumnClass(0, IDColumn.class, false, " ");
	 //miniTable.setColumnClass(0, String.class, true, Msg.translate(Env.getCtx(), "M_Product_ID"));
	 miniTable3.setColumnClass(1, Boolean.class, false, Msg.translate(Env.getCtx(), "I"));
         miniTable3.setColumnClass(2, Boolean.class, false, Msg.translate(Env.getCtx(), "IMx"));
	 miniTable3.setColumnClass(3, String.class, true, Msg.translate(Env.getCtx(), "Name"));
	 miniTable3.setColumnClass(3, String.class, true, Msg.translate(Env.getCtx(), "Unidad"));
         miniTable3.setColumnClass(4, VNumber.class, false, Msg.translate(Env.getCtx(), "Min"));
         miniTable3.setColumnClass(5, VNumber.class, false, Msg.translate(Env.getCtx(), "Real"));
         miniTable3.setColumnClass(6, VNumber.class, false, Msg.translate(Env.getCtx(), "Max"));
         //miniTable.setColumnClass(4, String.class, true, Msg.translate(Env.getCtx(), "C_BPartner_ID"));
         //miniTable.setColumnClass(5, Timestamp.class, true, Msg.translate(Env.getCtx(), "DateOrdered"));
         //miniTable.setColumnClass(6, BigDecimal.class, true, Msg.translate(Env.getCtx(), "TotalLines"));
         //miniTable3.transferFocusDownCycle();
         //miniTable3.addColumnSelectionInterval(0,3);
         miniTable3.getColorCode(50);
         miniTable3.getColumnSelectionAllowed();
         miniTable3.grabFocus();
	 miniTable3.getModel().addTableModelListener(this);
         dataPane3.getViewport().add(miniTable3,null);
         miniTable3.autoSize();



	 //	Info
	 statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InOutGenerateSel"));//@@
	 statusBar.setStatusDB(" ");
        //jButton2.setText("Aceptar Formula");
        jButtonCP.setText("Crear Producto");
        jButtonCA.setText("Crear Perfil");
        jButton21.setText("Guardar");
       // jPanel21.add(jButton2);
        jPanel21.add(jButtonCP);
        jPanel21.add(jButtonCA);
        jPanel21.add(jButton21);
        Ingredients.getContentPane().add(jPanel21, java.awt.BorderLayout.SOUTH);
 jButton2.addActionListener(this);
 jButtonCP.addActionListener(this);
 jButtonCA.addActionListener(this);
 jButton21.addActionListener(this);
    /*    jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));*/
        Ingredients.setTitle("An�lisis calculado");
        Ingredients.getContentPane().add(dataPane3, java.awt.BorderLayout.CENTER);
         Ingredients.setResizable(true);
         Ingredients.setIconifiable(true);
         Ingredients.setMaximizable(true);


        jDesktopPane1.add(Ingredients, javax.swing.JLayeredPane.DEFAULT_LAYER);

        Selected.setVisible(true);
        Selected.setResizable(true);
        Selected.setIconifiable(true);
         Selected.setMaximizable(true);

        jButton3.setText("Convertir Dosis");
        jButton30.setText("Redondeo");
        jButton31.setText("Recalcular");
        jButton32.setText("Guardar");
        jButton33.setText("Ing");
        jButton48.setText("Prod");
        jButton46.setText("Borrar");

        jPanel1.setLayout(new java.awt.GridLayout());
 jPanel2.setLayout(new java.awt.GridLayout());

  jPanel2.add(jButton30);
  jPanel2.add(jButton31);
  jPanel2.add(jButton32);
 jPanel2.add(jButton3);
  jPanel2.add(jButton33);
    jPanel2.add(jButton48);
  jPanel2.add(jButton46);


 jLabel1.setText("Costo");
        jPanel1.add(jLabel1);

        jTextField1.setText("");
        jPanel1.add(jTextField1);

         jLabel2.setText("Kilos");
        jPanel1.add(jLabel2);

        jTextField2.setText("");
        jPanel1.add(jTextField2);

        Selected.getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);
        Selected.getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);
        //Selected.getContentPane().add(jButton30, java.awt.BorderLayout.SOUTH);
        //Selected.getContentPane().add(jButton3, java.awt.BorderLayout.SOUTH);

 jButton3.addActionListener(this);
 jButton30.addActionListener(this);
 jButton31.addActionListener(this);
  jButton32.addActionListener(this);
   jButton33.addActionListener(this);
   jButton46.addActionListener(this);
   jButton48.addActionListener(this);
        Selected.getContentPane().add(dataPane2, java.awt.BorderLayout.CENTER);
        Selected.setResizable(true);
        Selected.setTitle("Ingredientes seleccionados");

        Ingredients.setBounds(601,200 , 400, 350);


        Selected.setBounds(0,200 , 600, 350);
        jDesktopPane1.add(Selected, javax.swing.JLayeredPane.DEFAULT_LAYER);

        miniTable6.setRowCount(0);
         miniTable6.addColumn("M_Product_ID");
		miniTable6.addColumn("Value");
		miniTable6.addColumn("Name");
      //          miniTable6.addColumn("UOM");
    		miniTable6.setMultiSelection(true);
		miniTable6.setRowSelectionAllowed(true);
		//  set details
		miniTable6.setColumnClass(0, IDColumn.class, false, " ");
		miniTable6.setColumnClass(1, String.class, true, Msg.translate(Env.getCtx(), "Value"));
               miniTable6.setColumnClass(2, String.class, true, Msg.translate(Env.getCtx(), "Name"));

		miniTable6.autoSize();

		miniTable6.getModel().addTableModelListener(this);
                dataPane6.getViewport().add(miniTable6,null);

                miniTable7.setRowCount(0);
         miniTable7.addColumn("M_Product_ID");
		miniTable7.addColumn("Value");
		miniTable7.addColumn("Name");
      //          miniTable6.addColumn("UOM");
    		miniTable7.setMultiSelection(true);
		miniTable7.setRowSelectionAllowed(true);
		//  set details
		miniTable7.setColumnClass(0, IDColumn.class, false, " ");
		miniTable7.setColumnClass(1, String.class, true, Msg.translate(Env.getCtx(), "Value"));
               miniTable7.setColumnClass(2, String.class, true, Msg.translate(Env.getCtx(), "Name"));

		miniTable7.autoSize();

		miniTable7.getModel().addTableModelListener(this);
                dataPane7.getViewport().add(miniTable7,null);

                miniTable8.setRowCount(0);
         miniTable8.addColumn("M_Product_ID");
		miniTable8.addColumn("Value");
		miniTable8.addColumn("Name");
      //          miniTable6.addColumn("UOM");
    		miniTable8.setMultiSelection(true);
		miniTable8.setRowSelectionAllowed(true);
		//  set details
		miniTable8.setColumnClass(0, IDColumn.class, false, " ");
		miniTable8.setColumnClass(1, String.class, true, Msg.translate(Env.getCtx(), "Value"));
                miniTable8.setColumnClass(2, String.class, true, Msg.translate(Env.getCtx(), "Name"));

		miniTable8.autoSize();

		miniTable8.getModel().addTableModelListener(this);
                dataPane8.getViewport().add(miniTable8,null);

                miniTable9.setRowCount(0);
         miniTable9.addColumn("M_Product_ID");
		miniTable9.addColumn("Value");
		miniTable9.addColumn("Name");
      //          miniTable6.addColumn("UOM");
    		miniTable9.setMultiSelection(true);
		miniTable9.setRowSelectionAllowed(true);
		//  set details
		miniTable9.setColumnClass(0, IDColumn.class, false, " ");
		miniTable9.setColumnClass(1, String.class, true, Msg.translate(Env.getCtx(), "Value"));
                miniTable9.setColumnClass(2, String.class, true, Msg.translate(Env.getCtx(), "Name"));

		miniTable9.autoSize();

		miniTable9.getModel().addTableModelListener(this);
                dataPane9.getViewport().add(miniTable9,null);

                miniTable10.setRowCount(0);
                miniTable10.addColumn("M_Product_ID");
		miniTable10.addColumn("Value");
		miniTable10.addColumn("Name");
      //          miniTable6.addColumn("UOM");
    		miniTable10.setMultiSelection(true);
		miniTable10.setRowSelectionAllowed(true);
		//  set details
		miniTable10.setColumnClass(0, IDColumn.class, false, " ");
		miniTable10.setColumnClass(1, String.class, true, Msg.translate(Env.getCtx(), "Value"));
                miniTable10.setColumnClass(2, String.class, true, Msg.translate(Env.getCtx(), "Name"));

		miniTable10.autoSize();

		miniTable10.getModel().addTableModelListener(this);
                dataPane10.getViewport().add(miniTable10,null);



     //jPanel6.setLayout(new java.awt.BorderLayout());
       // jPanel6.add(dataPane, java.awt.BorderLayout.CENTER);
      //  jPanel5.setLayout(new java.awt.BorderLayout());
        miniTable5.setRowCount(0);
         miniTable5.addColumn("M_Attribute_ID");
		miniTable5.addColumn("C�digo");
		miniTable5.addColumn("Name");
                miniTable5.addColumn("UOM");
    		miniTable5.setMultiSelection(true);
		miniTable5.setRowSelectionAllowed(true);
		//  set details
		miniTable5.setColumnClass(0, IDColumn.class, false, " ");
                miniTable5.setColumnClass(1, String.class, true, Msg.translate(Env.getCtx(), "C�digo"));
		miniTable5.setColumnClass(2, String.class, true, Msg.translate(Env.getCtx(), "Name"));
                miniTable5.setColumnClass(3, String.class, true, Msg.translate(Env.getCtx(), "UOM"));

		miniTable5.autoSize();

		miniTable5.getModel().addTableModelListener(this);
                dataPane5.getViewport().add(miniTable5,null);
Ingredients.setIcon(true);
            Selected.setIcon(true);
        getContentPane().add(jDesktopPane1, java.awt.BorderLayout.CENTER);

    }

//public void vetoableChange(PropertyChangeEvent e)
//	{
//		log.log(Level.INFO, "VEEAgengyGenerate.vetoableChange - "
//			+ e.getPropertyName() + "=" + e.getNewValue());
//		/**if (e.getPropertyName().equals("C_BPartner_ID"))
//			m_C_BPartner_ID = e.getNewValue();*/
//		if (e.getPropertyName().equals("M_Attribute_ID"))
//		{
//			m_M_Attribute_ID = e.getNewValue();
//			nutrientes.setValue(m_M_Attribute_ID);	//	display value
//		}
//	//	executeQuery();
//	}	//	vetoableChange


//public TableCellEditor getCellEditor(int row, int col) {
//    JTextField field = new JTextField((String);
//    super.getValueAt(row, col));
//    DefaultCellEditor editor = new DefaultCellEditor(field);
//    editor.setClickCountToStart(1);
//    field.addFocusListener(new FocusAdapter() {
//        public void focusGained(FocusEvent e) {
//            field.selectAll();
//        }
//        public void focusLost(FocusEvent e) {
//            field.select(0,0);
//        }
//    });
//    return editor;
//}

    private void executeQuery()
	{

            try
		{
            log.log(Level.FINE, "VEEAgengyGenerate.executeQuery");

               // int MPC_ProfileBOM_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#MPC_ProfileBOM_ID"));
                System.out.println("Valor de Profile4      " + m_MPC_ProfileBOM_ID);
                //  Create SQL
		StringBuffer sql = new StringBuffer(
			"SELECT pl.MPC_ProfileBOMLine_ID, ma.Description, ma.Name, ma.C_UOM_ID ,pl.Minimum,pl.Maximum,pl.IsPrinted,pl.IsPrintedMax "
			+ "FROM MPC_ProfileBOMLine pl "
                        + "INNER JOIN M_Attribute ma ON(ma.M_Attribute_ID=pl.M_Attribute_ID) "
			+ "WHERE pl.IsActive='Y' "
		        + "AND pl.AD_Client_ID=? AND pl.MPC_ProfileBOM_ID=?");

		//if (m_C_BPartner_ID != null)
                  // sql.append("p.C_BPartner_ID=").append(m_C_BPartner_ID);
		//if (m_M_Product_Category_ID != null)
		//	sql.append(" AND M_Product_Category_ID=1000000");
                        // .append(m_M_Product_Category_ID);
		//
	//	sql.append(" ORDER BY p.Name");
		log.log(Level.FINE, "VEEAgengyGenerate.executeQuery - AD_Client_ID=" + AD_Client_ID, sql.toString());

		//  reset table
		int row = 0;
		miniTable.setRowCount(row);
		//  Execute

			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, AD_Client_ID);
                        pstmt.setInt(2, m_MPC_ProfileBOM_ID);
			ResultSet rs = pstmt.executeQuery();
                         // int M_Attribute_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "M_Attribute_ID");//  Org
			//

			while (rs.next())
			{
				//  extend table
				miniTable.setRowCount(row+1);
				//  set values
                                //miniTable.isCellEditable();
				miniTable.setValueAt(new IDColumn(rs.getInt(1)), row, 0);
                                if(rs.getString(7)!=null && rs.getString(7).equals("Y"))
                                    miniTable.setValueAt(new Boolean(true), row, 1);
                                else
                                     miniTable.setValueAt(new Boolean(false), row, 1);
                                 if(rs.getString(8)!=null && rs.getString(8).equals("Y"))
                                    miniTable.setValueAt(new Boolean(true), row, 2);
                                else
                                     miniTable.setValueAt(new Boolean(false), row, 2);
                                //  C_Order_ID
				miniTable.setValueAt(rs.getString(2), row, 3);
                               // battributefield = new VLookup ("M_Attribute_ID", false, false, true, lookup);
                                //battributefield.setValue(new Integer(rs.getInt(3)));
                                miniTable.setValueAt(rs.getString(3), row, 4);
                                MUOM um = new MUOM(Env.getCtx(),rs.getInt(4),null);
                                KeyNamePair kum = new KeyNamePair(um.getC_UOM_ID(),um.getName());
                                miniTable.setValueAt(kum,row, 5);
                                 BigDecimal vnbd= Env.ZERO;
                                BigDecimal vnbdpor1= Env.ZERO;
                                BigDecimal uno = new BigDecimal(1.0);
                                 if (rs.getBigDecimal(5)==null)
                               {
                                  //  vnbd= new BigDecimal(0.0);
                                   ;
                               }
                               else
                               {
                                    vnbdpor1= new BigDecimal(rs.getBigDecimal(5).doubleValue());
                               }
                            //    BigDecimal uno = new BigDecimal(1.0);
                                BigDecimal vnbd2= vnbdpor1.divide(uno,3,vnbdpor1.ROUND_HALF_UP);


                                VNumber vnm1 = new VNumber();
                                 vnm1.setDisplayType(DisplayType.Number);
                                vnm1.setValue(vnbd2.setScale(4));

                             //   vnm.setDisplayType(1);
                                BigDecimal pbanum0 = (BigDecimal)vnm1.getValue();
                                if (pbanum0.doubleValue()!=0.0)
                                {
                                   miniTable.setValueAt(pbanum0.divide(uno,3,pbanum0.ROUND_HALF_UP), row, 6);
                                }
                               if (rs.getBigDecimal(6)==null)
                               {
                                  //  vnbd= new BigDecimal(0.0);
                                   ;
                               }
                               else
                               {
                                    vnbd= new BigDecimal(rs.getBigDecimal(6).doubleValue());
                               }

                                BigDecimal vnbd1= vnbd.divide(uno,3,vnbd.ROUND_HALF_UP);


                                VNumber vnm = new VNumber();
                                 vnm.setDisplayType(DisplayType.Number);
                                vnm.setValue(vnbd1.setScale(4));

                             //   vnm.setDisplayType(1);
                                BigDecimal pbanum = (BigDecimal)vnm.getValue();
                                 if (pbanum.doubleValue()!=0.0)
                                {
                                  miniTable.setValueAt(pbanum.divide(uno,3,pbanum.ROUND_HALF_UP), row, 7);
                                }

//				miniTable.setValueAt(rs.getBigDecimal(5), row, 4);
//                                miniTable.setValueAt(rs.getBigDecimal(6), row, 5);
                                //  Doc No
			//	miniTable.setValueAt(rs.getString(5), row, 4);              //  BPartner
			//	miniTable.setValueAt(rs.getTimestamp(6), row, 5);           //  DateOrdered
			//	miniTable.setValueAt(rs.getBigDecimal(7), row, 6);          //  TotalLines
				//  prepare next
				row++;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"ERROR" , e);
		}
		//
		miniTable.autoSize();
	//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  executeQuery

     private void executeQuery2()
	{

            try
		{
            log.log(Level.FINE, "VEEAgengyGenerate.executeQuery");
               // int MPC_ProfileBOM_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#MPC_ProfileBOM_ID"));
                //System.out.println("Valor de Profile4      " + m_MPC_ProfileBOM_ID);
                //  Create SQL
           //      StringBuffer sql= new StringBuffer("SELECT Distinct mp.MPC_ProfileBOM_Product_ID, p.Value, p.Name, BOM_Qty_Available(p.M_Product_ID,mp.MPC_ProfileBOM_Product_ID) AS QtyAvailable,  BOM_PriceList(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceList,  BOM_Qty_OnHand(p.M_Product_ID,mp.MPC_ProfileBOM_Product_ID) AS QtyOnHand, BOM_Qty_Reserved(p.M_Product_ID,mp.MPC_ProfileBOM_Product_ID) AS QtyReserved,  BOM_Qty_Ordered(p.M_Product_ID,mp.MPC_ProfileBOM_Product_ID) AS QtyOrdered, (SELECT SUM(c.TargetQty)  FROM M_InOutLineConfirm c  INNER JOIN M_InOutLine il ON (c.M_InOutLine_ID=il.M_InOutLine_ID)  INNER JOIN M_InOut i ON (il.M_InOut_ID=i.M_InOut_ID) WHERE c.Processed='N'  AND i.M_Warehouse_ID=103 AND il.M_Product_ID=p.M_Product_ID) AS Unconfirmed  FROM M_Product p LEFT OUTER JOIN M_ProductPrice pr ON (p.M_Product_ID=pr.M_Product_ID  AND pr.IsActive='Y') INNER JOIN MPC_ProfileBOM_Product mp ON(mp.M_Product_ID=p.M_Product_ID) where mp.AD_Client_ID=? and mp.MPC_ProfileBOM_ID=? and pr.M_PriceList_Version_ID=103");
		StringBuffer sql = new StringBuffer(
			"SELECT p.MPC_ProfileBOM_Product_ID, mp.Value, mp.Name, p.PriceList, p.Qty,p.Value "
			+ "FROM MPC_ProfileBOM_Product p "
                        + "INNER JOIN M_Product mp ON(mp.M_Product_ID=p.M_Product_ID) "
			+ "WHERE p.IsActive='Y' "
		//	+ " AND ic.C_BPartner_ID=bp.C_BPartner_ID"
                   //     + " AND p.M_Product_Category_ID=1000000 "
		//	+ " AND ic.C_DocType_ID=dt.C_DocType_ID"
            	        + " AND p.AD_Client_ID=? AND p.MPC_ProfileBOM_ID=?");

		//if (m_C_BPartner_ID != null)
                  // sql.append("p.C_BPartner_ID=").append(m_C_BPartner_ID);
		//if (m_M_Product_Category_ID != null)
		//	sql.append(" AND M_Product_Category_ID=1000000");
                        // .append(m_M_Product_Category_ID);
		//
	//	sql.append(" ORDER BY p.Name");
		log.log(Level.FINER, "VEEAgengyGenerate.executeQuery - AD_Client_ID=" + AD_Client_ID, sql.toString());

		//  reset table
		int row = 0;
		miniTable2.setRowCount(row);
		//  Execute

			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, AD_Client_ID);
                        pstmt.setInt(2, m_MPC_ProfileBOM_ID);
			ResultSet rs = pstmt.executeQuery();

			//
			while (rs.next())
			{
				//  extend table
				miniTable2.setRowCount(row+1);
				//  set values
				miniTable2.setValueAt(new IDColumn(rs.getInt(1)), row, 0);   //  C_Order_ID
				miniTable2.setValueAt(rs.getString(2), row, 1);              //  Org
				miniTable2.setValueAt(rs.getString(3), row, 2);
                                miniTable2.setValueAt(rs.getString(6), row, 4);
                                BigDecimal vnbd= Env.ZERO;
                                BigDecimal vnbdpor1= Env.ZERO;
                                BigDecimal uno = new BigDecimal(1.0);
                                 if (rs.getBigDecimal(4)==null)
                               {
                                  //  vnbd= new BigDecimal(0.0);
                                   ;
                               }
                               else
                               {
                                    vnbdpor1= new BigDecimal(rs.getBigDecimal(4).doubleValue());
                               }
                            //    BigDecimal uno = new BigDecimal(1.0);
                                BigDecimal vnbd2= vnbdpor1.divide(uno,3,vnbdpor1.ROUND_HALF_UP);


                                VNumber vnm1 = new VNumber();
                                 vnm1.setDisplayType(DisplayType.Number);
                                vnm1.setValue(vnbd2.setScale(3));

                             //   vnm.setDisplayType(1);
                                BigDecimal pbanum0 = (BigDecimal)vnm1.getValue();
                                if (pbanum0.doubleValue()!=0.0)
                                {
                                    miniTable2.setValueAt(pbanum0.divide(uno,3,pbanum0.ROUND_HALF_UP), row, 3);
                                }
                               if (rs.getBigDecimal(5)==null)
                               {
                                  //  vnbd= new BigDecimal(0.0);
                                   ;
                               }
                               else
                               {
                                    vnbd= new BigDecimal(rs.getBigDecimal(5).doubleValue());
                               }

                                BigDecimal vnbd1= vnbd.divide(uno,3,vnbd.ROUND_HALF_UP);


                                VNumber vnm = new VNumber();
                                 vnm.setDisplayType(DisplayType.Number);
                                vnm.setValue(vnbd1.setScale(3));

                             //   vnm.setDisplayType(1);
                                BigDecimal pbanum = (BigDecimal)vnm.getValue();
                              if (pbanum.doubleValue()!=0.0)
                                {
                                miniTable2.setValueAt(pbanum.divide(uno,3,pbanum.ROUND_HALF_UP), row, 5);
                              }
				//miniTable2.setValueAt(rs.getBigDecimal(4), row, 3);
                                //miniTable2.setValueAt(rs.getBigDecimal(5), row, 4);

                             //   miniTable.setValueAt(rs.getBigDecimal(5), row, 4);
                                //  Doc No
			//	miniTable.setValueAt(rs.getString(5), row, 4);              //  BPartner
			//	miniTable.setValueAt(rs.getTimestamp(6), row, 5);           //  DateOrdered
			//	miniTable.setValueAt(rs.getBigDecimal(7), row, 6);          //  TotalLines
				//  prepare next
				row++;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery", e);
		}
		//
		miniTable2.autoSize();
      //	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  executeQuery2

        private void executeQuery3()
	{

            try
		{
                log.log(Level.INFO, "VEEAgengyGenerate.executeQuery");
                StringBuffer sql = new StringBuffer(" SELECT s.MPC_ProfileBOM_Selected_ID,s.IsPrinted,s.IsAlias,s.IsCritical, s.M_Product_ID, s.PriceList, s.Percent, s.Qty,s.Planteamiento, p.Value FROM MPC_ProfileBOM_Selected s INNER JOIN M_Product p ON(p.M_Product_ID=s.M_Product_ID) WHERE s.AD_Client_ID=? AND s.MPC_ProfileBOM_ID=? ");
            //    System.out.println("query 3 "+ sql.toString());
		//  reset table
		int row = 0;
		miniTable2.setRowCount(row);
		//  Execute

			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
                        pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, m_MPC_ProfileBOM_ID);

			ResultSet rs = pstmt.executeQuery();

			//
			while (rs.next())
			{
				//  extend table
				miniTable2.setRowCount(row+1);
				//  set values
                                  IDColumn id  = new IDColumn(rs.getInt(1));
                                id.setSelected(true);
				miniTable2.setValueAt(id, row, 0);   //  C_Order_ID
		//		System.out.println("isprinted" +rs.getString(2));
				  if(rs.getString(2).equals("Y"))
                                    miniTable2.setValueAt(new Boolean(true), row, 1);
                                else
                                     miniTable2.setValueAt(new Boolean(false), row, 1);
                                //miniTable2.setValueAt(rs.getString(2), row, 1);
                                 if(rs.getString(3).equals("Y"))
                                    miniTable2.setValueAt(new Boolean(true), row, 2);
                                else
                                     miniTable2.setValueAt(new Boolean(false), row, 2);
                                //miniTable2.setValueAt(rs.getString(3), row, 2);
                                 if(rs.getString(4).equals("Y"))
                                 {
                                    miniTable2.setValueAt(new Boolean(true), row, 3);
                                    MMPCProfileBOM mp = new MMPCProfileBOM(Env.getCtx(),rs.getInt(9),null);
                                    KeyNamePair kmp = new KeyNamePair(mp.getMPC_ProfileBOM_ID(),mp.getName());
                                    miniTable2.setValueAt(kmp, row, 5);
                                 }
                                else
                                {
                                     miniTable2.setValueAt(new Boolean(false), row, 3);
                                     MProduct mp = new MProduct(Env.getCtx(),rs.getInt(5),null);
                                     KeyNamePair kmp = new KeyNamePair(mp.getM_Product_ID(),mp.getName());
                                     miniTable2.setValueAt(kmp, row, 5);
                                }
                                miniTable2.setValueAt(rs.getString(10), row, 4);

                                 BigDecimal vnbd= Env.ZERO;
                                BigDecimal vnbdpor1= Env.ZERO;
                                BigDecimal vnbdpor0= Env.ZERO;
                                BigDecimal uno = new BigDecimal(1.0);
                                if (rs.getBigDecimal(6)==null)
                               {
                                  //  vnbd= new BigDecimal(0.0);
                                   ;
                               }
                               else
                               {
                                    vnbdpor0= new BigDecimal(rs.getBigDecimal(6).doubleValue());
                               }
                            //    BigDecimal uno = new BigDecimal(1.0);
                                BigDecimal vnbd0= vnbdpor0.divide(uno,3,vnbdpor0.ROUND_HALF_UP);


                                VNumber vnm0 = new VNumber();
                                 vnm0.setDisplayType(DisplayType.Number);
                                vnm0.setValue(vnbd0.setScale(3));

                             //   vnm.setDisplayType(1);
                                BigDecimal pbanum1 = (BigDecimal)vnm0.getValue();

                                miniTable2.setValueAt(pbanum1.divide(uno,3,pbanum1.ROUND_HALF_UP), row, 6);

                           //     miniTable2.setValueAt(rs.getBigDecimal(6), row, 5);
                              //  miniTable2.setValueAt(rs.getBigDecimal(7), row, 6);

                                 if (rs.getBigDecimal(7)==null)
                               {
                                  //  vnbd= new BigDecimal(0.0);
                                   ;
                               }
                               else
                               {
                                    vnbdpor1= new BigDecimal(rs.getBigDecimal(7).doubleValue());
                               }
                            //    BigDecimal uno = new BigDecimal(1.0);
                                BigDecimal vnbd2= vnbdpor1.divide(uno,4,vnbdpor1.ROUND_HALF_UP);


                                VNumber vnm1 = new VNumber();
                                 vnm1.setDisplayType(DisplayType.Number);
                                vnm1.setValue(vnbd2.setScale(4));

                             //   vnm.setDisplayType(1);
                                BigDecimal pbanum0 = (BigDecimal)vnm1.getValue();

                                miniTable2.setValueAt(pbanum0.divide(uno,4,pbanum0.ROUND_HALF_UP), row, 7);

                               if (rs.getBigDecimal(8)==null)
                               {
                                  //  vnbd= new BigDecimal(0.0);
                                   ;
                               }
                               else
                               {
                                    vnbd= new BigDecimal(rs.getBigDecimal(8).doubleValue());
                               }

                                BigDecimal vnbd1= vnbd.divide(uno,3,vnbd.ROUND_HALF_UP);


                                VNumber vnm = new VNumber();
                                 vnm.setDisplayType(DisplayType.Number);
                                vnm.setValue(vnbd1.setScale(3));

                             //   vnm.setDisplayType(1);
                                BigDecimal pbanum = (BigDecimal)vnm.getValue();
                                if (pbanum.doubleValue()!=0.0)
                                {
                                miniTable2.setValueAt(pbanum.divide(uno,3,pbanum.ROUND_HALF_UP), row, 8);
                                }
                                //  Doc No
			//	miniTable.setValueAt(rs.getString(5), row, 4);              //  BPartner
			//	miniTable.setValueAt(rs.getTimestamp(6), row, 5);           //  DateOrdered
			//	miniTable.setValueAt(rs.getBigDecimal(7), row, 6);          //  TotalLines
				//  prepare next
				row++;
//                                miniTable2.getEditingColumn();
//                                miniTable2.selectAll();
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery" + e);
		}
		//
		miniTable2.autoSize();
                           MMPCProfileBOM profilebom =new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
                 profilebom.getPriceList();
                    profilebom.getQty();
                jTextField1.setText(profilebom.getPriceList().toString());
                //jTextField1.disable();
               //sumakilos = sumakilos.setScale(3,5);
                jTextField2.setText(profilebom.getQty().toString());
               //jTextField2.disable();


	//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  fin query Ing Selected

      private void executeQueryReal()
	{

            try
		{
            //log.log(Level.INFO, "VEEAgengyGenerate.executeQuery");
               // int MPC_ProfileBOM_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#MPC_ProfileBOM_ID"));
                //System.out.println("Valor de Profile4      " + m_MPC_ProfileBOM_ID);
                //  Create SQL
		StringBuffer sql = new StringBuffer("SELECT MPC_ProfileBOM_Real_ID,IsPrinted, M_Attribute_ID, Minimum, Realnut,Maximum,IsPrintedMax FROM MPC_ProfileBOM_Real pbr WHERE pbr.MPC_ProfileBOM_ID=? AND pbr.AD_Client_ID=?");
                //System.out.println("");
		//log.log(Level.FINE, "VEEAgengyGenerate.executeQuery - AD_Client_ID=" + AD_Client_ID, sql.toString());

		//  reset table
		int row = 0;
		miniTable3.setRowCount(row);
		//  Execute
                PreparedStatement pstmt = DB.prepareStatement(sql.toString());
		pstmt.setInt(1, m_MPC_ProfileBOM_ID);
                pstmt.setInt(2, AD_Client_ID);
		ResultSet rs = pstmt.executeQuery();
                         // int M_Attribute_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "M_Attribute_ID");//  Org
			//
		while (rs.next())
		{
				//  extend table
				miniTable3.setRowCount(row+1);
				//  set values
                                IDColumn id  = new IDColumn(rs.getInt(1));
                                id.setSelected(true);
				miniTable3.setValueAt(id, row, 0);   //  C_Order_ID
		//		miniTable.setValueAt(rs.getString(2), row, 1);
                               // battributefield = new VLookup ("M_Attribute_ID", false, false, true, lookup);
                                //battributefield.setValue(new Integer(rs.getInt(3)));
                                if(rs.getString(2).equals("Y"))
                                    miniTable3.setValueAt(new Boolean(true), row, 1);
                                else
                                     miniTable3.setValueAt(new Boolean(false), row, 1);
                                 if(rs.getString(7)!=null && rs.getString(7).equals("Y"))
                                    miniTable3.setValueAt(new Boolean(true), row, 2);
                                else
                                     miniTable3.setValueAt(new Boolean(false), row, 2);
                            //    miniTable3.setValueAt(rs.getString(2), row, 1);
                                   MAttribute um = new MAttribute(Env.getCtx(),rs.getInt(3),null);
                                KeyNamePair kum = new KeyNamePair(um.getM_Attribute_ID(),um.getName());
                                miniTable3.setValueAt(kum,row, 3);
                                  BigDecimal vnbd= Env.ZERO;
                                BigDecimal vnbdpor0= Env.ZERO;
                                BigDecimal vnbdpor1= Env.ZERO;
                                BigDecimal uno = new BigDecimal(1.0);
                                if (rs.getBigDecimal(4)==null)
                               {
                                  //  vnbd= new BigDecimal(0.0);
                                   ;
                               }
                               else
                               {
                                    vnbdpor0= new BigDecimal(rs.getBigDecimal(4).doubleValue());
                               }
                            //    BigDecimal uno = new BigDecimal(1.0);
                                BigDecimal vnbd0= vnbdpor0.divide(uno,3,vnbdpor0.ROUND_HALF_UP);


                                VNumber vnm0 = new VNumber();
                                 vnm0.setDisplayType(DisplayType.Number);
                                vnm0.setValue(vnbd0.setScale(3));

                             //   vnm.setDisplayType(1);
                                BigDecimal pbanum1 = (BigDecimal)vnm0.getValue();
                                miniTable3.setValueAt(pbanum1.divide(uno,3,pbanum1.ROUND_HALF_UP), row, 4);

                             //   miniTable3.setColorColumn(4); //.setForeground(fg.RED);
                                 if (rs.getBigDecimal(5)==null)
                               {
                                  //  vnbd= new BigDecimal(0.0);
                                   ;
                               }
                               else
                               {
                                    vnbdpor1= new BigDecimal(rs.getBigDecimal(5).doubleValue());
                               }
                            //    BigDecimal uno = new BigDecimal(1.0);
                                BigDecimal vnbd2= vnbdpor1.divide(uno,3,vnbdpor1.ROUND_HALF_UP);


                                VNumber vnm1 = new VNumber();
                                 vnm1.setDisplayType(DisplayType.Number);
                                vnm1.setValue(vnbd2.setScale(3));

                             //   vnm.setDisplayType(1);
                                BigDecimal pbanum0 = (BigDecimal)vnm1.getValue();
                                miniTable3.setValueAt(pbanum0.divide(uno,3,pbanum0.ROUND_HALF_UP), row, 5);
                               if (rs.getBigDecimal(6)==null)
                               {
                                  //  vnbd= new BigDecimal(0.0);
                                   ;
                               }
                               else
                               {
                                    vnbd= new BigDecimal(rs.getBigDecimal(6).doubleValue());
                               }

                                BigDecimal vnbd1= vnbd.divide(uno,3,vnbd.ROUND_HALF_UP);


                                VNumber vnm = new VNumber();
                                 vnm.setDisplayType(DisplayType.Number);
                                vnm.setValue(vnbd1.setScale(3));

                             //   vnm.setDisplayType(1);
                                BigDecimal pbanum = (BigDecimal)vnm.getValue();
                                miniTable3.setValueAt(pbanum.divide(uno,3,pbanum.ROUND_HALF_UP), row, 6);


                                //miniTable3.setValueAt(rs.getBigDecimal(4), row, 4);
                                //miniTable3.setValueAt(rs.getBigDecimal(5), row, 5);
                                //miniTable3.setValueAt(rs.getBigDecimal(6), row, 6);
				row++;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery" +e);
		}
		//
		miniTable3.autoSize();
	//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  QueryReal Analisis Calculado



      private void executeQueryN()
	{

            try
		{
            log.log(Level.INFO, "VEEAgengyGenerate.executeQuery");

               // int MPC_ProfileBOM_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#MPC_ProfileBOM_ID"));
                //System.out.println("Valor de Profile4      " + m_MPC_ProfileBOM_ID);
                //  Create SQL
		StringBuffer sql = new StringBuffer(
                        "SELECT M_Attribute_ID, Name, C_UOM_ID, Description "
			+ "FROM M_Attribute "
			+ "WHERE IsActive='Y' "
            	        + " AND AD_Client_ID=? AND AttributeValueType='N' ORDER BY Name");

		log.log(Level.FINE, "VEEAgengyGenerate.executeQuery - AD_Client_ID=" + AD_Client_ID, sql.toString());

		//  reset table
		int row = 0;
		miniTable5.setRowCount(row);
		//  Execute

			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, AD_Client_ID);

			ResultSet rs = pstmt.executeQuery();

			//
			while (rs.next())
			{
				//  extend table
				miniTable5.setRowCount(row+1);
				//  set values
				miniTable5.setValueAt(new IDColumn(rs.getInt(1)), row, 0);   //  C_Order_ID
				miniTable5.setValueAt(rs.getString(4), row, 1);              //  Org
				miniTable5.setValueAt(rs.getString(2), row, 2);
                               // miniTable3.setValueAt(rs.getString(4), row, 3);
                                     MUOM um = new MUOM(Env.getCtx(),rs.getInt(3),null);
                                KeyNamePair kum = new KeyNamePair(um.getC_UOM_ID(),um.getName());
				miniTable5.setValueAt(kum.toString(), row, 3);

				row++;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery"+ e);
		}
		//
		miniTable5.autoSize();
	//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  executeQueryN Nutrientes

      private void executeQueryI()
	{

            try
		{
            log.log(Level.INFO, "VEEAgengyGenerate.executeQuery");
               // int MPC_ProfileBOM_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#MPC_ProfileBOM_ID"));
                //System.out.println("Valor de Profile4      " + m_MPC_ProfileBOM_ID);
                //  Create SQL
              //  versionprecio();
		StringBuffer sqli = new StringBuffer("SELECT m.M_Product_ID,m.Value,m.Name From M_Product m  WHERE  m.AD_Client_ID=?  AND m.IsActive='Y' AND m.AD_Org_ID=0 AND m.IsSummary='N' AND ProductType='I' ORDER BY m.Name");
                // Left Inner Join M_ProductPrice pp ON(pp.M_Product_ID=m.M_Product_ID) where pp.M_PriceList_Version_ID=103 and m.AD_Client_ID=?");

		log.log(Level.FINE, "VEEAgengyGenerate.executeQuery - AD_Client_ID=" + AD_Client_ID, sqli.toString());

		//  reset table
		int row = 0;
		miniTable6.setRowCount(row);
		//  Execute

			PreparedStatement pstmti = DB.prepareStatement(sqli.toString());
			pstmti.setInt(1, AD_Client_ID);
                        //pstmt.setInt(2, m_M_PriceList_Version_ID);
			ResultSet rsi = pstmti.executeQuery();

			//
			while (rsi.next())
			{
				//  extend table
                                miniTable6.clearSelection();
				miniTable6.setRowCount(row+1);
				//  set values
				miniTable6.setValueAt(new IDColumn(rsi.getInt(1)), row, 0);   //  C_Order_ID
				miniTable6.setValueAt(rsi.getString(2), row, 1);              //  Org
				miniTable6.setValueAt(rsi.getString(3), row, 2);
                               // miniTable3.setValueAt(rs.getString(4), row, 3);


				row++;
			}
                        miniTable6.autoSize();
			rsi.close();
			pstmti.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery" + e);
		}
		//

	//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  executeQueryI

      // Cambio agragar ing sel
            private void executeQueryI2()
	{

            try
		{
            log.log(Level.INFO, "VEEAgengyGenerate.executeQuery");
               // int MPC_ProfileBOM_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#MPC_ProfileBOM_ID"));
                //System.out.println("Valor de Profile4      " + m_MPC_ProfileBOM_ID);
                //  Create SQL
              //  versionprecio();
		StringBuffer sqli2 = new StringBuffer("SELECT m.M_Product_ID,m.Value,m.Name FROM M_Product m  WHERE m.AD_Client_ID=?  AND m.IsActive='Y' AND m.AD_Org_ID=0 AND m.IsSummary='N' AND ProductType='I' ORDER BY m.Name");
                // Left Inner Join M_ProductPrice pp ON(pp.M_Product_ID=m.M_Product_ID) where pp.M_PriceList_Version_ID=103 and m.AD_Client_ID=?");

		log.log(Level.FINE, "VEEAgengyGenerate.executeQuery - AD_Client_ID=" + AD_Client_ID, sqli2.toString());

		//  reset table
		int row = 0;
		miniTable10.setRowCount(row);
		//  Execute

			PreparedStatement pstmti2 = DB.prepareStatement(sqli2.toString());
			pstmti2.setInt(1, AD_Client_ID);
                        //pstmt.setInt(2, m_M_PriceList_Version_ID);
			ResultSet rsi2 = pstmti2.executeQuery();

			//
			while (rsi2.next())
			{
				//  extend table
                                miniTable10.clearSelection();
				miniTable10.setRowCount(row+1);
				//  set values
				miniTable10.setValueAt(new IDColumn(rsi2.getInt(1)), row, 0);   //  C_Order_ID
				miniTable10.setValueAt(rsi2.getString(2), row, 1);              //  Org
				miniTable10.setValueAt(rsi2.getString(3), row, 2);
                               // miniTable3.setValueAt(rs.getString(4), row, 3);


				row++;
			}
			rsi2.close();
			pstmti2.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery"+ e);
		}
		//
		miniTable10.autoSize();
	//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  executeQueryI
      //

         private void executeQueryI3()
	{

            try
		{
                 log.log(Level.INFO, "VEEAgengyGenerate.executeQuery");
               // int MPC_ProfileBOM_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#MPC_ProfileBOM_ID"));
                //System.out.println("Valor de Profile4      " + m_MPC_ProfileBOM_ID);
                //  Create SQL
          //      versionprecio();
		StringBuffer sql = new StringBuffer("SELECT m.M_Product_ID,m.Value,m.Name FROM M_Product m WHERE m.AD_Client_ID=?  AND m.IsActive='Y' AND m.IsSummary='N' AND m.ProductType='I' AND m.AD_Org_ID=0 AND m.IsToFormule='Y' ORDER BY m.Name");
                // Left Inner Join M_ProductPrice pp ON(pp.M_Product_ID=m.M_Product_ID) where pp.M_PriceList_Version_ID=103 and m.AD_Client_ID=?");

		log.log(Level.FINE, "VEEAgengyGenerate.executeQuery - AD_Client_ID=" + AD_Client_ID, sql.toString());

		//  reset table
		int row = 0;
		miniTable7.setRowCount(row);
		//  Execute

			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, AD_Client_ID);
                        //pstmt.setInt(2, m_M_PriceList_Version_ID);
			ResultSet rs = pstmt.executeQuery();

			//
			while (rs.next())
			{
				//  extend table
                                miniTable7.clearSelection();
				miniTable7.setRowCount(row+1);
				//  set values
				miniTable7.setValueAt(new IDColumn(rs.getInt(1)), row, 0);   //  C_Order_ID
				miniTable7.setValueAt(rs.getString(2), row, 1);              //  Org
				miniTable7.setValueAt(rs.getString(3), row, 2);
                               // miniTable3.setValueAt(rs.getString(4), row, 3);


				row++;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery", e);
		}
		//
		miniTable7.autoSize();
	//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  executeQueryI
      //
         private void executeQueryP()
	{

            try
		{
            log.log(Level.INFO, "VEEAgengyGenerate.executeQuery");

               // int MPC_ProfileBOM_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#MPC_ProfileBOM_ID"));
                //System.out.println("Valor de Profile4      " + m_MPC_ProfileBOM_ID);
                //  Create SQL
            //   versionprecio();
		StringBuffer sqlp = new StringBuffer("SELECT m.M_Product_ID,m.Value,m.Name FROM M_Product m  WHERE m.AD_Client_ID=?  and m.IsActive='Y' AND m.IsSummary='N' AND m.ProductType='I' AND m.AD_Org_ID=0 AND m.IsToFormule='Y' ORDER BY m.Name");
                // Left Inner Join M_ProductPrice pp ON(pp.M_Product_ID=m.M_Product_ID) where pp.M_PriceList_Version_ID=103 and m.AD_Client_ID=?");

            log.log(Level.FINE, "VEEAgengyGenerate.executeQuery - AD_Client_ID=" + AD_Client_ID, sqlp.toString());

		//  reset table
		int row = 0;
		miniTable8.setRowCount(row);
		//  Execute

			PreparedStatement pstmtp = DB.prepareStatement(sqlp.toString());
			pstmtp.setInt(1, AD_Client_ID);
                        //pstmt.setInt(2, m_M_PriceList_Version_ID);
			ResultSet rsp = pstmtp.executeQuery();

			//
			while (rsp.next())
			{
				//  extend table
                                miniTable8.clearSelection();
				miniTable8.setRowCount(row+1);
				//  set values
				miniTable8.setValueAt(new IDColumn(rsp.getInt(1)), row, 0);   //  C_Order_ID
				//miniTable3.setValueAt(rs.getString(2), row, 1);              //  Org
				miniTable8.setValueAt(rsp.getString(2), row, 1);
                                miniTable8.setValueAt(rsp.getString(3), row, 2);


				row++;
			}
			rsp.close();
			pstmtp.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery", e);
		}
		//
		miniTable8.autoSize();
	//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  executeQueryI
      //

           private void executeQueryNR()
	{

            try
		{
            log.log(Level.INFO, "VEEAgengyGenerate.executeQuery");
               // int MPC_ProfileBOM_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#MPC_ProfileBOM_ID"));
                //System.out.println("Valor de Profile4      " + m_MPC_ProfileBOM_ID);
                //  Create SQL
               // versionprecio();
		StringBuffer sql = new StringBuffer("SELECT m.M_Product_ID,m.Name,m.Value FROM M_Product m  WHERE m.AD_Client_ID=?  AND m.IsActive='Y' AND m.IsSummary='N' AND m.ProductType='I' AND AD_Org_ID=0 ORDER BY m.Name");
                // Left Inner Join M_ProductPrice pp ON(pp.M_Product_ID=m.M_Product_ID) where pp.M_PriceList_Version_ID=103 and m.AD_Client_ID=?");

		log.log(Level.FINE, "VEEAgengyGenerate.executeQuery - AD_Client_ID=" + AD_Client_ID, sql.toString());

		//  reset table
		int row = 0;
		miniTable9.setRowCount(row);
		//  Execute

			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, AD_Client_ID);
                        //pstmt.setInt(2, m_M_PriceList_Version_ID);
			ResultSet rs = pstmt.executeQuery();

			//
			while (rs.next())
			{
				//  extend table
                                miniTable9.clearSelection();
				miniTable9.setRowCount(row+1);
				//  set values
				miniTable9.setValueAt(new IDColumn(rs.getInt(1)), row, 0);   //  C_Order_ID
				//miniTable3.setValueAt(rs.getString(2), row, 1);              //  Org
				miniTable9.setValueAt(rs.getString(2), row, 2);
                                miniTable9.setValueAt(rs.getString(3), row, 1);


				row++;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery"+ e);
		}
		//
		miniTable9.autoSize();
	//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  executeQueryI

     private void executeQuery4()
	{
		formular();

	//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  executeQuery4

//     private void generatesearch()
//	{
//             try
//		{
//            log.log(Level.INFO, "VEEAgengyGenerate.executeQuery");
//		int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
//               // int MPC_ProfileBOM_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#MPC_ProfileBOM_ID"));
//                System.out.println("Valor de Profile4      " + m_MPC_ProfileBOM_ID);
//                //  Create SQL
//		StringBuffer sql = new StringBuffer(
//			"SELECT pl.MPC_ProfileBOMLine_ID, pl.Line, ma.Name, ma.Description,pl.Min,pl.Min,pl.Max "
//			+ "FROM MPC_ProfileBOMLine pl "
//                        + "INNER JOIN M_Attribute ma ON(ma.M_Attribute_ID=pl.M_Attribute_ID) "
//			+ "WHERE pl.IsActive='Y' "
//            	        + " AND pl.AD_Client_ID=? AND pl.MPC_ProfileBOM_ID=?");
//
//		//if (m_C_BPartner_ID != null)
//                  // sql.append("p.C_BPartner_ID=").append(m_C_BPartner_ID);
//		//if (m_M_Product_Category_ID != null)
//		//	sql.append(" AND M_Product_Category_ID=1000000");
//                        // .append(m_M_Product_Category_ID);
//		//
//	//	sql.append(" ORDER BY p.Name");
//		log.log(Level.FINE, "VEEAgengyGenerate.executeQuery - AD_Client_ID=" + AD_Client_ID, sql.toString());
//
//		//  reset table
//		int row = 0;
//		miniTable3.setRowCount(row);
//		//  Execute
//
//			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
//			pstmt.setInt(1, AD_Client_ID);
//                        pstmt.setInt(2, m_MPC_ProfileBOM_ID);
//			ResultSet rs = pstmt.executeQuery();
//
//			//
//			while (rs.next())
//			{
//				//  extend table
//				miniTable3.setRowCount(row+1);
//				//  set values
//				miniTable3.setValueAt(new IDColumn(rs.getInt(1)), row, 0);   //  C_Order_ID
//				miniTable3.setValueAt(rs.getString(2), row, 1);              //  Org
//				miniTable3.setValueAt(rs.getString(3), row, 2);
//                                miniTable3.setValueAt(rs.getString(4), row, 3);
//				miniTable3.setValueAt(rs.getBigDecimal(5), row, 4);
//                                miniTable3.setValueAt(rs.getBigDecimal(6), row, 5);
//                                miniTable3.setValueAt(rs.getBigDecimal(7), row, 6);
//                                //  Doc No
//			//	miniTable.setValueAt(rs.getString(5), row, 4);              //  BPartner
//			//	miniTable.setValueAt(rs.getTimestamp(6), row, 5);           //  DateOrdered
//			//	miniTable.setValueAt(rs.getBigDecimal(7), row, 6);          //  TotalLines
//				//  prepare next
//				row++;
//			}
//			rs.close();
//			pstmt.close();
//		}
//		catch (SQLException e)
//		{
//			log.log(Level.SEVERE"VEEAgengyGenerate.executeQuery", e);
//		}
//		//
//		miniTable3.autoSize();
//
//
//     }

      private void executeQuerySearch()
	{
            int rows = miniTable.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rows);

                    if (id != null)
                    {
                  //      System.out.println("id" + id);
                       Integer ln=id.getRecord_ID();
                    //     System.out.println("ln" + ln);
//                        miniTable.editingStopped(new ChangeEvent(this));
//               System.out.println("minitable getvalue" + miniTable.getValueAt(i, 0));
//                //System.out.println("vlookup de nutrientes2" + nutrientes.toString());
             MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
             MMPCProfileBOMLine profilebomline = new MMPCProfileBOMLine(Env.getCtx(),ln.intValue(),null);
          //   profilebomline.setClientOrg(profilebom.getAD_Client_ID(), profilebom.getAD_Org_ID());
             //System.out.println("si compila");
           Env.setContext(Env.getCtx(),m_WindowNo,"#AD_Client_ID",profilebom.getAD_Client_ID());

          String vacio ="";
             if (miniTable.getValueAt(i,6)!=null && !miniTable.getValueAt(i,6).toString().equals(vacio))
             {
             String mn = miniTable.getValueAt(i,6).toString();

               System.out.println("string min" +mn +"-----");

             Integer mni= new Integer(0);
            // System.out.println("integer min" + mni.valueOf(mn));

             BigDecimal mnb = new BigDecimal(miniTable.getValueAt(i,6).toString());

             //System.out.println("big min" + mnb);
             profilebomline.setMinimum(mnb);

             }
             else
             {
                 profilebomline.setMinimum(cero);
             }
             if (miniTable.getValueAt(i,7)!=null && !miniTable.getValueAt(i,7).toString().equals(vacio))
             {
             String mx = miniTable.getValueAt(i,7).toString();
             //System.out.println("string max" +mx);
             Integer mxi= new Integer(0);
            // System.out.println("integer max" + mxi.valueOf(mx));
             BigDecimal mxb = new BigDecimal(miniTable.getValueAt(i,7).toString());
            // System.out.println("big max" + mxb);
             profilebomline.setMaximum(mxb);
             }
             else
             {
                 profilebomline.setMaximum(cero);
             }
           if (miniTable.getValueAt(i,1)!=null && miniTable.getValueAt(i,1).toString().matches("true"))
                 profilebomline.setIsPrinted(true);
            else
                profilebomline.setIsPrinted(false);
            if (miniTable.getValueAt(i,2)!=null && miniTable.getValueAt(i,2).toString().matches("true"))
                 profilebomline.setIsPrintedMax(true);
            else
                profilebomline.setIsPrintedMax(false);
              //    profilebomline.setLine(10);
                    profilebomline.save();
                    }
                 }

//            try
//		{
//            log.log(Level.INFO, "VEEAgengyGenerate.executeQuery");
//		int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
//               // int MPC_ProfileBOM_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#MPC_ProfileBOM_ID"));
//                //System.out.println("Valor de Profile4      " + m_MPC_ProfileBOM_ID);
//                //  Create SQL
//		StringBuffer sql = new StringBuffer(
//                "SELECT Distinct p.M_Product_ID, p.Discontinued, p.Value, p.Name, BOM_Qty_Available(p.M_Product_ID,144) AS QtyAvailable, "
//+"BOM_PriceList(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceList, "
//+"BOM_Qty_OnHand(p.M_Product_ID,144) AS QtyOnHand, BOM_Qty_Reserved(p.M_Product_ID,144) AS QtyReserved, "
//+"BOM_Qty_Ordered(p.M_Product_ID,144) AS QtyOrdered, (SELECT  SUM(c.TargetQty) "
//+" FROM M_InOutLineConfirm c INNER JOIN M_InOutLine il ON (c.M_InOutLine_ID=il.M_InOutLine_ID) "
//+" INNER JOIN M_InOut i ON (il.M_InOut_ID=i.M_InOut_ID) WHERE c.Processed='N' "
//+" AND i.M_Warehouse_ID=103 AND il.M_Product_ID=p.M_Product_ID) AS Unconfirmed, "
//+" BOM_PriceStd(p.M_Product_ID, pr.M_PriceList_Version_ID)-BOM_PriceLimit(p.M_Product_ID, pr.M_PriceList_Version_ID) AS Margin, "
//+" BOM_PriceLimit(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceLimit, pa.IsInstanceAttribute "
//+" FROM M_Product p LEFT OUTER JOIN M_ProductPrice pr ON (p.M_Product_ID=pr.M_Product_ID "
//+" AND pr.IsActive='Y') LEFT OUTER JOIN M_AttributeSet pa ON (p.M_AttributeSet_ID=pa.M_AttributeSet_ID) "
//+"Left outer JOIN MPC_ProfileBOMLine pl ON(pl.M_Attribute_ID=M_Attribute_ID) "
// +"WHERE p.IsActive='Y' AND p.IsSummary='N' AND EXISTS "
// +"(SELECT  * from M_AttributeSetInstance asi where p.M_AttributeSetInstance_ID=asi.M_AttributeSetInstance_ID "
// +" AND asi.M_AttributeSetInstance_ID IN (SELECT  M_AttributeSetInstance_ID FROM "
// +"M_AttributeInstance mai WHERE ");
//
//StringBuffer nut = new StringBuffer(
//"Select M_Attribute_ID, Min, Max From MPC_ProfileBOMLine Where MPC_ProfileBOM_ID=?");
//
//PreparedStatement pstmt1 = DB.prepareStatement(nut.toString());
//			//pstmt1.setInt(1, AD_Client_ID);
//                        pstmt1.setInt(1, m_MPC_ProfileBOM_ID);
//			ResultSet rs1 = pstmt1.executeQuery();
//
//			int row2=0;
//                     //   int row3=1;
//			while (rs1.next())
//			{
//				//  extend table
//
//                            //System.out.println("id    ---" +rs.getInt(1));
////
//
//                            array.add(row2, rs1.getString(1));
//                            row2=row2+1;
//                            array.add(row2, rs1.getBigDecimal(2));
//                            row2=row2+1;
//                            array.add(row2, rs1.getBigDecimal(3));
//
////                        miniTable4.setRowCount(row2+1);
////                                miniTable4.setValueAt(new IDColumn(rs1.getInt(1)), row2, 0);
////				miniTable4.setValueAt(rs1.getString(2), row2, 1);
////				miniTable4.setValueAt(rs1.getString(3), row2, 2);
////				miniTable4.setValueAt(rs1.getString(4), row2, 3);
////                                miniTable4.setValueAt(rs1.getBigDecimal(5), row2, 4);
////                                miniTable4.setValueAt(rs1.getBigDecimal(6), row2, 5);
////                               miniTable4.setValueAt(rs1.getBigDecimal(7), row2, 6);
////                                miniTable4.setValueAt(rs1.getBigDecimal(8), row2, 7);
//				row2++;
//			}
//			rs1.close();
//			pstmt1.close();
//
// System.out.println("no lineas minitable ----" +miniTable.getRowCount());
//    //String mat = miniTable.getValueAt(0,3).toString();
//        //   String mat1 = miniTable.getValueAt(1,3).toString();
//           System.out.println("valor atributo ----" +array.get(0));
//           System.out.println("valor atributo0 ----" +array.get(1));
//            System.out.println("valor atributo0 ----" +array.get(2));
//            System.out.println("valor atributo0 ----" +array.get(3));
//         //   System.out.println("valor atributo0 ----" +array.get(4));
//          //   System.out.println("valor atributo0 ----" +miniTable.getValueAt(1,3).toString());
//
//int row1=0;
//int contarray=0;
//contarray=array.size();
//System.out.println("contarray" +contarray);
//  StringBuffer sql1 = new StringBuffer();
// while (row1 < contarray)
//	{
//            //    String mat1 = miniTable.getValueAt(row1,3).toString();
//            //    String mat2 = miniTable.getValueAt(row1,4).toString();
//                 System.out.println("row1 ----" +row1);
//              //   System.out.println("valor atributo ----" +mat1);
//           if (sql1.length() > 0)
//                        sql1.append(" OR ");
//
//
//          // System.out.println("i ----" +i);
//
//					sql1.append("(mai.M_Attribute_ID=" +array.get(row1));
//
//                                      //  sql1.append(" OR mai.Value>" +array.get(1));
//                                        row1++;
//
//                                        if (array.get(row1).equals(cero))
//                                           System.out.println("sin valor min");
//                                        else
//                                        sql1.append(" and mai.Value >"+ array.get(row1));
//
//                                        row1++;
//
//                                           if (array.get(row1).equals(cero))
//                                               System.out.println("sin valor max");
//                                           else
//                                            sql1.append(" and mai.Value <"+ array.get(row1));
//
//                                        sql1.append(")");
//                                        row1++;
//					//	.append(" AND M_AttributeValue_ID=").append(pp.getKey()).append(")");
//
//        }
//  System.out.println("sql1 ----" +sql1.toString());
//  sql.append(sql1);
//  sql.append(")) AND p.AD_Client_ID=? AND pl.MPC_ProfileBOM_ID=? ORDER BY QtyAvailable DESC, Margin DESC");
// System.out.println("sql -----" +sql.toString());
//  log.log(Level.FINE, "VEEAgengyGenerate.executeQuery - AD_Client_ID=" + AD_Client_ID, sql.toString());
//
//		//  reset table
//		int row = 0;
//		//miniTable4.setRowCount(row);
//		//  Execute
//
//			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
//			pstmt.setInt(1, AD_Client_ID);
//                        pstmt.setInt(2, m_MPC_ProfileBOM_ID);
//			ResultSet rs = pstmt.executeQuery();
//
//
//			while (rs.next())
//			{
//				//  extend table
//
//                            System.out.println("id    ---" +rs.getInt(1));
////
//                        miniTable4.setRowCount(row+1);
//miniTable4.setValueAt(new IDColumn(rs.getInt(1)), row, 0);
//				miniTable4.setValueAt(rs.getString(3), row, 1);
//				miniTable4.setValueAt(rs.getString(4), row, 2);
//				miniTable4.setValueAt(rs.getString(5), row, 3);
//                                miniTable4.setValueAt(rs.getBigDecimal(6), row, 4);
//                                miniTable4.setValueAt(rs.getBigDecimal(7), row, 5);
//                               miniTable4.setValueAt(rs.getBigDecimal(8), row, 6);
//                                miniTable4.setValueAt(rs.getBigDecimal(9), row, 7);
//				row++;
//			}
//			rs.close();
//			pstmt.close();
//		}
//		catch (SQLException e)
//		{
//			log.log(Level.SEVERE"VEEAgengyGenerate.executeQuery", e);
//		}
//		//
//		miniTable4.autoSize();
	//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  executeQuery2

       private void executeQuerySearch2()
	{
            int rows = miniTable4.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable4.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rows);

                    if (id != null)
                    {
//                        System.out.println("id" + id);
                       Integer ln=id.getRecord_ID();
                       id.toString();
//                         System.out.println("ln" + ln);
                     String ingr ="";

                     ingr=miniTable4.getValueAt(i, 0).toString();
                      // Integer ingri= new Integer(ingr);
//                        miniTable.editingStopped(new ChangeEvent(this));
              // System.out.println("minitable getvalue " +  id.toString());
//                //System.out.println("vlookup de nutrientes2" + nutrientes.toString());
             MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
             MMPCProfileBOMProduct profilebomproduct = new MMPCProfileBOMProduct(Env.getCtx(),ln.intValue(),null);
          //   profilebomline.setClientOrg(profilebom.getAD_Client_ID(), profilebom.getAD_Org_ID());
             //System.out.println("si compila");
           Env.setContext(Env.getCtx(),m_WindowNo,"#AD_Client_ID",profilebom.getAD_Client_ID());



            if (miniTable4.getValueAt(i,1)!=null && miniTable4.getValueAt(i,1).toString().matches("true"))
                 profilebomproduct.setIsPrinted(true);
            else
                profilebomproduct.setIsPrinted(false);

           if (miniTable4.getValueAt(i,2)!=null && miniTable4.getValueAt(i,2).toString().matches("true"))
                 profilebomproduct.setIsAlias(true);
            else
                profilebomproduct.setIsAlias(false);

             String vacio="";
             if (miniTable4.getValueAt(i,5)!=null && !miniTable4.getValueAt(i,5).toString().equals(vacio))
             {
             String mn = miniTable4.getValueAt(i,5).toString();

//               System.out.println("string min" +mn);

             Integer mni= new Integer(0);
            // System.out.println("integer min" + mni.valueOf(mn));
             BigDecimal mnb = new BigDecimal(mn);

             //System.out.println("big min" + mnb);
             profilebomproduct.setMinimum(mnb);
             }
             else
             {
                 profilebomproduct.setMinimum(Env.ZERO);
             }
             if (miniTable4.getValueAt(i,6)!=null && !miniTable4.getValueAt(i,6).toString().equals(vacio))
             {
             String mx = miniTable4.getValueAt(i,6).toString();
//             System.out.println("string max" +mx);
             Integer mxi= new Integer(0);
            // System.out.println("integer max" + mxi.valueOf(mx));
             BigDecimal mxb = new BigDecimal(mx);
            // System.out.println("big max" + mxb);
             profilebomproduct.setMaximum(mxb);
             }
             else
             {
                 profilebomproduct.setMaximum(Env.ZERO);
             }
            if (miniTable4.getValueAt(i,8)!=null)
             {
             String pl = miniTable4.getValueAt(i,8).toString();
//             System.out.println("stringpl" +pl);
             Integer pli= new Integer(0);
            // System.out.println("integer max" + mxi.valueOf(mx));
             BigDecimal plb = new BigDecimal(pl);
            // System.out.println("big max" + mxb);
             profilebomproduct.setPriceList(plb);
             }
             else
             {
                 profilebomproduct.setPriceList(Env.ZERO);
             }
              //    profilebomline.setLine(10);
                    profilebomproduct.save();
                    }
                 }
       }

      private void formular()
      {
                try
		{
                    log.log(Level.INFO, "VEEAgengyGenerate.executeQuery");
                    String sql=new String("SELECT p.M_Product_ID,p.MPC_ProfileBOM_ID FROM MPC_ProfileBOM_Product p WHERE p.IsActive='Y' AND p.AD_Client_ID=? and p.MPC_ProfileBOM_ID=? ORDER BY p.M_Product_ID DESC");
                    PreparedStatement pstmt = DB.prepareStatement(sql);
                    pstmt.setInt(1, AD_Client_ID);
                    pstmt.setInt(2, m_MPC_ProfileBOM_ID);
		    //pstmt.setInt(2, m_M_PriceList_ID);
		    ResultSet rs = pstmt.executeQuery();
		    //while (!m_calculated && rsplv.next())
                    while (rs.next())
		    {
				if (rs.getString(1)!=null)
                                    m_prod_id = rs.getInt(1);
                                else
                                    m_prod_id = rs.getInt(2);


		    }
		    rs.close();
		    pstmt.close();

                    if (m_prod_id!=0)
                    {
                        //  System.out.println("profilebom id  "+m_MPC_ProfileBOM_ID);
                        MMPCProfileBOM profile = new MMPCProfileBOM(Env.getCtx(), m_MPC_ProfileBOM_ID,null);
                        // versionprecio();
                        // System.out.println("Version precio lista "+m_M_PriceList_Version_ID);
                        int M_Warehause_ID = Env.getContextAsInt(Env.getCtx(),"M_Warehouse_ID");
                        sql= new String("SELECT mp.MPC_ProfileBOM_Product_ID, p.Value,p.Name,mp.Minimum, mp.Maximum, mp.M_Product_ID, bomqtyavailable(mp.M_Product_ID," + M_Warehause_ID + ",0) AS QtyAvailable, mp.IsPrinted, bomqtyonhand(mp.M_Product_ID," + M_Warehause_ID  +",0 ) AS QtyOnHand, bomqtyreserved(mp.M_Product_ID," +  M_Warehause_ID  +" ,0) AS QtyReserved, bomqtyordered(mp.M_Product_ID,"+ M_Warehause_ID + ",0) AS QtyOrdered, mp.IsAlias FROM MPC_ProfileBOM_Product mp join M_Product p ON(p.M_Product_ID=mp.M_Product_ID)  WHERE  mp.AD_Client_ID= " + AD_Client_ID + " AND mp.MPC_ProfileBOM_ID=? AND mp.M_Product_ID IS NOT NULL UNION ALL SELECT mp.MPC_ProfileBOM_Product_ID,pf.Value,pf.Name, mp.Minimum, mp.Maximum,mp.Planteamiento,null,mp.IsPrinted, null,null,null, mp.IsAlias FROM MPC_ProfileBOM_Product mp JOIN MPC_ProfileBOM pf ON(pf.MPC_ProfileBOM_ID=mp.Planteamiento)  WHERE mp.AD_Client_ID=" + AD_Client_ID + " AND mp.MPC_ProfileBOM_ID=? AND mp.Planteamiento IS  NOT NULL");


			pstmt = DB.prepareStatement(sql);
			//pstmt.setInt(1, AD_Client_ID);
                        pstmt.setInt(1, m_MPC_ProfileBOM_ID);
                        //pstmt.setInt(3, AD_Client_ID);
                        pstmt.setInt(2, m_MPC_ProfileBOM_ID);
                        //pstmt.setInt(3,m_M_PriceList_Version_ID);
			rs = pstmt.executeQuery();

			int row = 0;
                        miniTable4.setRowCount(row);
			while (rs.next())
			{
				//  extend table
				miniTable4.setRowCount(row+1);



                                                                   //  set values
                                IDColumn id1 = new IDColumn(rs.getInt(1));
                                id1.setSelected(true);
				miniTable4.setValueAt(id1, row, 0);   //  C_Order_ID
                                 if(rs.getString(8)!=null && rs.getString(8).equals("Y"))
                                    miniTable4.setValueAt(new Boolean(true), row, 1);
                                else
                                     miniTable4.setValueAt(new Boolean(false), row, 1);
                                 if(rs.getString(12)!=null && rs.getString(12).equals("Y"))
                                    miniTable4.setValueAt(new Boolean(true), row, 2);
                                else
                                     miniTable4.setValueAt(new Boolean(false), row, 2);

                                KeyNamePair m_product_key = new KeyNamePair(rs.getInt(6),rs.getString(2));

				miniTable4.setValueAt(m_product_key, row, 3);              //  Value\
				miniTable4.setValueAt(rs.getString(3), row, 4);              //  DocType

                                  BigDecimal vnbd= Env.ZERO;
                                BigDecimal vnbdpor0= Env.ZERO;
                                BigDecimal vnbdpor1= Env.ZERO;
                                BigDecimal uno = new BigDecimal(1.0);
                                if (rs.getBigDecimal(4)==null)
                               {
                                  //  vnbd= new BigDecimal(0.0);
                                   ;
                               }
                               else
                               {
                                    vnbdpor0= new BigDecimal(rs.getBigDecimal(4).doubleValue());
                               }
                            //    BigDecimal uno = new BigDecimal(1.0);
                                BigDecimal vnbd0= vnbdpor0.divide(uno,3,vnbdpor0.ROUND_HALF_UP);


                                VNumber vnm0 = new VNumber();
                                 vnm0.setDisplayType(DisplayType.Number);
                                vnm0.setValue(vnbd0.setScale(3));

                             //   vnm.setDisplayType(1);
                                BigDecimal pbanum1 = (BigDecimal)vnm0.getValue();
                                if (pbanum1.doubleValue()!=0.0)
                                {
                                miniTable4.setValueAt(pbanum1.divide(uno,3,pbanum1.ROUND_HALF_UP), row, 5);
                                }
                                 if (rs.getBigDecimal(5)==null)
                               {
                                  //  vnbd= new BigDecimal(0.0);
                                   ;
                               }
                               else
                               {
                                    vnbdpor1= new BigDecimal(rs.getBigDecimal(5).doubleValue());
                               }
                            //    BigDecimal uno = new BigDecimal(1.0);
                                BigDecimal vnbd2= vnbdpor1.divide(uno,3,vnbdpor1.ROUND_HALF_UP);


                                VNumber vnm1 = new VNumber();
                                 vnm1.setDisplayType(DisplayType.Number);
                                vnm1.setValue(vnbd2.setScale(3));

                             //   vnm.setDisplayType(1);
                                BigDecimal pbanum0 = (BigDecimal)vnm1.getValue();
                                if (pbanum0.doubleValue()!=0.0)
                                {
                                miniTable4.setValueAt(pbanum0.divide(uno,3,pbanum0.ROUND_HALF_UP), row, 6);
                                }
                               // miniTable4.setValueAt(rs.getBigDecimal(4), row, 3);
                               // miniTable4.setValueAt(rs.getBigDecimal(5), row, 4);
                                miniTable4.setValueAt(rs.getBigDecimal(7), row, 7);
//                                if (rs.getBigDecimal(7)!=null)
//                                miniTable4.setValueAt(rs.getBigDecimal(7), row, 6);
//                                else

                                 miniTable4.setValueAt(rs.getBigDecimal(9), row, 9);
                               miniTable4.setValueAt(rs.getBigDecimal(10), row, 10);
                                miniTable4.setValueAt(rs.getBigDecimal(11), row, 11);



				row++;


			}

			rs.close();
			pstmt.close();
                  }
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery", e);
		}
		//
          //miniTable4.selectAll();
		miniTable4.autoSize();
      } // End Formular

private void formWindowOpened(java.awt.event.WindowEvent evt) {


      if(maximize == true)
      {

       //super.show();
       super.setExtendedState(JFrame.MAXIMIZED_BOTH);
      }
	   }

private String createQuery()
	{
		/** Base Query
		SELECT *
		FROM M_Product p
 		 INNER JOIN M_ProductPrice pr ON (p.M_Product_ID=pr.M_Product_ID)
 		 LEFT OUTER JOIN M_AttributeSet pa ON (p.M_AttributeSet_ID=pa.M_AttributeSet_ID)
		WHERE
		**/

		/***	Instance Attributes		*/
		StringBuffer sb = new StringBuffer();
		//	Serial No

		//	Instance Editors
		StringBuffer iAttr = new StringBuffer();
                int row = 0;
		//miniTable4.setRowCount(row);
                int cont=0;
		for (int i = 0; i < miniTable.getRowCount(); i++)
		{
			Component c = (Component)m_instanceEditors.get(i);
			int M_Attribute_ID = Integer.parseInt(c.getName());
			if (c instanceof VComboBox)
			{
				VComboBox field0 = (VComboBox)c;
				//pp = (KeyNamePair)field0.getSelectedItem();
				//if (pp != null && pp.getKey() != -1)
				//{// cambio fjv
					if (iAttr.length() > 0)
						iAttr.append(" OR ");
                                               // iAttr.append(" AND ");
                                        //cambio fjv
					iAttr.append("(M_Attribute_ID=").append(M_Attribute_ID);
		//				.append(" AND M_AttributeValue_ID=").append(pp.getKey()).append(")");
			//	}
			}
			else
			{       // Cambio vnumber fjv

				VString field = (VString)c;
                                VString field1 = (VString)c;
				String value = field.getText();
                                String value2 = field1.getText();


                              //  VNumber field= (VNumber)c;
                               // VNumber field1= (VNumber)c;
              //                  System.out.println("Valor de field      " + field.getValue());
                //                 System.out.println("Valor de field1      " + field1.getValue());
                                 //  System.out.println("Valor de getvalue      " + field.getValue());
                               Object number = field.getValue();
                                //String value = new String("");
                               if (number != null)
                               {
                                   value = number.toString();
                               }
                               Object number1 = field1.getValue();
                                //String value2 = new String("");
                               if (number1 != null)
                               {
                                   value2 = number1.toString();
                               }
                  //       System.out.println("Valor de value      " + value);
                    //      System.out.println("Valor de value2     " + value2);
                                //value =field.getValue();
                            // BigDecimal value2 = new BigDecimal(0.0);
                           // value2 =field1.getValue();

                             //    value.setScale(4);
                               //  value2.setScale(4);
                                 if ((value != null && value.length() > 0))
                                // cambio vnumber fjv
				//if ((value != null && value.intValue() != 0))
				{
                                     if (cont!=1)
                                {
                      //               System.out.println("Valor de cont      " + cont);
					// cambio fjv
					if (iAttr.length() > 0)
						iAttr.append(" OR ");
                                               // iAttr.append(" AND ");
                                        //cambio fjv
					iAttr.append("(M_Attribute_ID=").append(M_Attribute_ID)
						.append(" AND Value");

                                    //    if ((cont)==0)
                                     //   {
                                           if (value.indexOf('%') == -1 && value.indexOf('_') == 1)
						iAttr.append(">=");
					   else
                                               	iAttr.append(" >= ");
                                       //    cont++;
                                       // }
                                       // else
                                        // {
                                       //    if (value.indexOf('%') == -1 && value.indexOf('_') == 1)
					//	iAttr.append("<=");
					  // else
                                            //   	iAttr.append(" <= ");
                                          // cont=0;
                                       // }
					iAttr.append(DB.TO_STRING(value)).append(")");

                                }

                                     //   System.out.println("Valor de atributo     " + DB.TO_STRING(value));
				}
                                 // cambio vnumber fjv

                                  if ((value2 != null && value2.length() > 0))
                             //if ((value2 != null && value2.intValue() != 0))
				  {
                                      if (cont!=0)
                                {
                        //             System.out.println("Valor de cont      " + cont);
					// cambio fjv
					if (iAttr.length() > 0)
						iAttr.append(" and ");
                                               // iAttr.append(" AND ");
                                        //cambio fjv
					iAttr.append("(M_Attribute_ID=").append(M_Attribute_ID)
						.append(" AND Value");

					if (value2.indexOf('%') == -1 && value2.indexOf('_') == 1)
						iAttr.append("<=");
					else
                                        	iAttr.append(" <= ");
					iAttr.append(DB.TO_STRING(value2)).append(")");
                              //           System.out.println("Valor de atributo     " + DB.TO_STRING(value2));
				     cont = 0;
                                }
                                else
                                {cont=1;
                          //        System.out.println("Valor de cont      " + cont);
                                }
                                      }

			}
		}
		if (iAttr.length() > 0)
		{
			iAttr.insert(0, " AND asi.M_AttributeSetInstance_ID IN "
				+ "(SELECT M_AttributeSetInstance_ID FROM M_AttributeInstance "
				+ "WHERE ");
			iAttr.append(")");
		}

		//	finish Instance Attributes
		if (sb.length() > 0 || iAttr.length() > 0)
		{
			// cambio
                   /* sb.insert(0, " AND EXISTS (SELECT * FROM M_Storage s"
				+ " INNER JOIN M_AttributeSetInstance asi ON (s.M_AttributeSetInstance_ID=asi.M_AttributeSetInstance_ID) "
				+ "WHERE s.M_Product_ID=p.M_Product_ID");*/
                        sb.insert(0, " AND EXISTS (Select * from M_AttributeSetInstance asi where p.M_AttributeSetInstance_ID=asi.M_AttributeSetInstance_ID and p.M_AttributeSet_ID=asi.M_AttributeSet_ID");
                        // cambio
			sb.append(iAttr).append(")");
		}


		//	Product Attributes
		StringBuffer pAttr = new StringBuffer();
              //  String M_Attribute_ID= 101;
		for (int i = 0; i <  miniTable.getRowCount(); i++)
		{
			Component c = (Component)m_productEditors.get(i);
			int M_Attribute_ID = Integer.parseInt(c.getName());
			if (c instanceof VComboBox)
			{
				VComboBox field0 = (VComboBox)c;
//				pp = (KeyNamePair)field0.getSelectedItem();
//				if (pp != null && pp.getKey() != -1)
//				{
					if (pAttr.length() > 0)
						pAttr.append(" AND ");
					pAttr.append("(M_Attribute_ID=").append(M_Attribute_ID);
				//		.append(" AND M_AttributeValue_ID=").append(pp.getKey()).append(")");
			//	}
			}
			else
			{
				VString field = (VString)c;
                                VString field1 = (VString)c;
				String value = field.getText();
                                String value2 = field1.getText();
				if (value != null && value.length() > 0)
				{
					if (pAttr.length() > 0)
						pAttr.append(" AND ");
					pAttr.append("(M_Attribute_ID=").append(M_Attribute_ID)
						.append(" AND Value");
					if (value.indexOf('%') == -1 && value.indexOf('_') == 1)
						pAttr.append("=");
					else
						pAttr.append(" LIKE ");
					pAttr.append(DB.TO_STRING(value)).append(")");
				}
			}
		}
		if (pAttr.length() > 0)
		{
			pAttr.insert(0, " AND p.M_AttributeSetInstance_ID IN "
				+ "(SELECT M_AttributeSetInstance_ID "
				+ "FROM M_AttributeInstance WHERE ");
			pAttr.append(")");
			sb.append(pAttr);
		}
		//
		m_query = null;
		if (sb.length() > 0)
			m_query = sb.toString();
                 //System.out.println("Valor de sb     " + sb.toString());
		log.log(Level.INFO, "InfoPAttribute.createQuery", m_query);
                //System.out.println("Valor query attr      " + m_query);
		return m_query;
	}	//	createQuery

public void valueChanged(TableModelEvent e) {

        }

    public void tableChanged(javax.swing.event.TableModelEvent e) {
//        TableCellEditor celleditor = miniTable.getCellEditor();
//        String value = "";
//
//
//
//        	int type = -1;
//		if (e != null)
//		{
//			type = e.getType();
//
////                        if (type != TableModelEvent.UPDATE)
////				return;
//
//                         if (e.getType() == TableModelEvent.UPDATE || e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.DELETE)
//                         {
//                             int col=0;
//                col = miniTable.getSelectedColumn();
//                int row=0;
//                row = miniTable.getSelectedRow();
//                             log.info(Level.INFO, "VCreateFrom.tableChanged " + type);
//                log.info(Level.INFO, "VCreateFrom.row:" + miniTable.getSelectedRow());
//                log.info(Level.INFO, "VCreateFrom.Column:" +  e.getColumn());
//                  if (row>=0)
//                {
//                    String valor1 = new String(miniTable.getValueAt(row,col).toString());
//
//
//
//                        celleditor.getTableCellEditorComponent(miniTable, valor1, false, row+1,col);
//                     miniTable.changeSelection(row, col, false,false);
//
//
//
//                }
//                         }

		//}

//

//

    }
    public void actionPerformed (ActionEvent e)
	{




          if (e.getSource() == jButton30)
          {
//                    System.out.println("REDONDEO");
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    int c25=0;
                    nutarr1.clear();
                    acalc.clear();
                    aunico.clear();
                    BigDecimal acumulado=Env.ZERO;
                    BigDecimal acumulado2=Env.ZERO;
                    BigDecimal acumuladopr=Env.ZERO;
                    BigDecimal preciored = Env.ZERO;
                    int cont50=0;
                    cont500=0;
                    for (int m=0;m<miniTable2.getRowCount(); m++)
                    {
                        IDColumn id = (IDColumn)miniTable2.getValueAt(m, 0);//  ID in column 0
                        if (id != null)
                        {
                            // validar si ya estan completos los requerimientos
                            // calculo de la combinacion de ingredientes del costo menor
                            String prod2 = miniTable2.getValueAt(m,5).toString();
                            String kgreal2 = miniTable2.getValueAt(m,8).toString();
                            BigDecimal kgbd2 = new BigDecimal(kgreal2);
                            int profile_id =0;
                            try
                            {
                                    StringBuffer ingsql = new StringBuffer("SELECT M_Product_ID, Volume, Name FROM M_Product WHERE AD_Client_ID=? AND Name=? ");
                                    PreparedStatement pstmt = DB.prepareStatement(ingsql.toString());
                                    pstmt.setInt(1,AD_Client_ID);
                                    pstmt.setString(2, prod2);
                                    ResultSet rs = pstmt.executeQuery();
                                    while (rs.next())
                                    {
                                        vol = rs.getBigDecimal(2);
                                        ing_id = rs.getInt(1);
                                    }
                                    rs.close();
                                    pstmt.close();
                             }
                             catch(SQLException s)
                             {
                                 log.log(Level.SEVERE, "Error SQL" , s);
                             }
//                                System.out.println("Factor-----------" +vol);
                                BigDecimal uno = new BigDecimal(1.0);
                                if (vol.doubleValue()==0.0)
                                {
                                    int volint=0;
                                    kgbd2=kgbd2.divide(uno,volint,kgbd2.ROUND_HALF_UP);
//                                    System.out.println("kilos redondeados-----------" +kgbd2);
                                }
                                BigDecimal und=new BigDecimal(.1);
                                if (vol.doubleValue()==0.1)
                                {
                                    int volint=1;
                                    kgbd2=kgbd2.divide(uno,volint,kgbd2.ROUND_HALF_UP);
                                }
                                BigDecimal dosd=new BigDecimal(.01);
                                if (vol.doubleValue()==0.01)
                                {
                                    int volint=2;
                                    kgbd2=kgbd2.divide(uno,volint,kgbd2.ROUND_HALF_UP);
                                }
                                BigDecimal tresd=new BigDecimal(.001);
                                if (vol.doubleValue()==0.001)
                                {
                                    int volint=3;
                                    kgbd2=kgbd2.divide(uno,volint,kgbd2.ROUND_HALF_UP);
                                }
                                acumulado = new BigDecimal(acumulado.doubleValue()+kgbd2.doubleValue());
                                miniTable2.setValueAt(kgbd2,m,8);
                                String ing_idst ="";
                                ing_idst = ing_idst.valueOf(ing_id);

                                if (miniTable2.getValueAt(m,3)!=null && miniTable2.getValueAt(m,3).toString().matches("true"))
                                {
//                                    System.out.println("entra planteamiento ----------");
                                    try
                                    {
                                        StringBuffer prodid = new StringBuffer("SELECT MPC_ProfileBOM_ID FROM MPC_ProfileBOM WHERE AD_Client_ID=? AND Name=?");
                                        PreparedStatement pstmtprodid = DB.prepareStatement(prodid.toString());
                                        pstmtprodid.setInt(1, AD_Client_ID);
                                        pstmtprodid.setString(2, prod2);
                                        ResultSet rsprodid = pstmtprodid.executeQuery();
                                        while (rsprodid.next())
                                        {
                                            profile_id = rsprodid.getInt(1);
                                        }
                                        rsprodid.close();
                                        pstmtprodid.close();
                                    }
                                    catch(SQLException s)
                                    {
                                    }
                                    String mpid ="";
                                    mpid = mpid.valueOf(profile_id);
//                                    System.out.println("prod id " +mpid);
                                    //    preciodeing(mpid);
                                    //    calculado(product_id,kgreal2);
                                    BigDecimal costoplan1=Env.ZERO;
                                    try
                                    {
                                      MMPCProfileBOM prof =new MMPCProfileBOM(Env.getCtx(),profile_id,null);
                                        StringBuffer sql10= new StringBuffer("");
                                        if (prof.getQty().compareTo(Env.ZERO)>0)
                                        {
                                            sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  where mp.IsPublished='Y' and mp.Value like '");
                                        sql10.append(miniTable2.getValueAt(m,4).toString() +"%'").append("Order by mp.Value");
                                       // sql10= new StringBuffer("SELECT Trunc(mp.PriceList/mp.Qty,2) from MPC_ProfileBOM mp  where mp.AD_Client_ID=1000000 and mp.Value like '");
                                       // sql10.append(miniTable2.getValueAt(m,4).toString() +"%'");
                                        }
                                        else
                                        {
                                            sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  where mp.IsPublished='Y' and mp.Value like '");
                                        sql10.append(miniTable2.getValueAt(m,4).toString() +"%'").append("Order by mp.Value");
                                        }
                                        PreparedStatement pstmt10 = DB.prepareStatement(sql10.toString());
			               // pstmt10.setInt(1, product_id);
                                        ResultSet rs10 = pstmt10.executeQuery();

                                        if (rs10.next())
                                        {
                                            costoplan1 = rs10.getBigDecimal(1);
//                                            System.out.println("costo plan ---------------  " +costoplan1);
                                        }
                                        rs10.close();
                                        pstmt10.close();

                                         StringBuffer psql= new StringBuffer("SELECT M_Product_ID from M_Product  where AD_Client_ID=" + AD_Client_ID + " AND Value like '");
                                        psql.append(miniTable2.getValueAt(m,4).toString() +"'");

                                        PreparedStatement pstmtp10 = DB.prepareStatement(psql.toString());
			                //pstmt10.setInt(1, product_id);
                                        ResultSet rsp10 = pstmtp10.executeQuery();

                                        if (rsp10.next())
                                        {
                                            product_id = rsp10.getInt(1);
                                            System.out.println("prod" +product_id);

                                        }
                                        rsp10.close();
                                        pstmtp10.close();
                                    }
                                    catch(SQLException s)
                                    {
                                    }
                                    calculadop(profile_id,kgreal2);
                                //   System.out.println("calculado " +acalc.toString());
                                preciored = costoplan1.multiply(kgbd2);
//                                System.out.println("precio por kilos " +preciored);
                                miniTable2.setValueAt(preciored.divide(uno,3,preciored.ROUND_HALF_UP),m,6);
                                cont50++;
                                }
                                else if (miniTable2.getValueAt(m,4).toString().startsWith("P"))
                                {
//                                    System.out.println("entra planteamiento ----------");
                                    try
                                    {
                                        StringBuffer prodid = new StringBuffer("SELECT MPC_ProfileBOM_ID FROM MPC_ProfileBOM Where AD_Client_ID=? and Name=?");
                                        PreparedStatement pstmtprodid = DB.prepareStatement(prodid.toString());
                                        pstmtprodid.setInt(1, AD_Client_ID);
                                        pstmtprodid.setString(2, prod2);
                                        ResultSet rsprodid = pstmtprodid.executeQuery();
                                        while (rsprodid.next())
                                        {
                                            profile_id = rsprodid.getInt(1);
                                        }
                                        rsprodid.close();
                                        pstmtprodid.close();
                                    }
                                    catch(SQLException s)
                                    {
                                    }
                                    String mpid ="";
                                    mpid = mpid.valueOf(profile_id);
//                                    System.out.println("prod id " +mpid);
                                    //    preciodeing(mpid);
                                    //    calculado(product_id,kgreal2);
                                    BigDecimal costoplan1=Env.ZERO;
                                    try
                                    {
                                        MMPCProfileBOM prof =new MMPCProfileBOM(Env.getCtx(),profile_id,null);
                                        StringBuffer sql10= new StringBuffer("");
                                        if (prof.getQty().compareTo(Env.ZERO)>0)
                                        {
                                        sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  where mp.IsPublished='Y' and mp.Value like '");
                                        sql10.append(miniTable2.getValueAt(m,4).toString() +"%'").append("Order by mp.Value");
                                        }
                                        else
                                        {
                                           sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  where mp.IsPublished='Y' and mp.Value like '");
                                        sql10.append(miniTable2.getValueAt(m,4).toString() +"%'").append("Order by mp.Value");
                                        }
                                        PreparedStatement pstmt10 = DB.prepareStatement(sql10.toString());
			                //pstmt10.setInt(1, product_id);
                                        ResultSet rs10 = pstmt10.executeQuery();

                                        if (rs10.next())
                                        {
                                            costoplan1 = rs10.getBigDecimal(1);
//                                            System.out.println("costo plan ---------------  " +costoplan1);
                                        }
                                        rs10.close();
                                        pstmt10.close();

                                         StringBuffer psql= new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID="+AD_Client_ID + " AND Value = ?");


                                        PreparedStatement pstmtp10 = DB.prepareStatement(psql.toString());
			                pstmtp10.setString(1, miniTable2.getValueAt(m,4).toString());
                                        ResultSet rsp10 = pstmtp10.executeQuery();

                                        if (rsp10.next())
                                        {
                                            product_id = rsp10.getInt(1);
                                            System.out.println("prod" +product_id);

                                        }
                                        rsp10.close();
                                        pstmtp10.close();
                                    }
                                    catch(SQLException s)
                                    {
                                    }
                                    calculado(product_id,kgreal2);
                                //   System.out.println("calculado " +acalc.toString());
                                preciored = new BigDecimal(costoplan1.doubleValue()*kgbd2.doubleValue());
//                                System.out.println("precio por kilos " +preciored);
                                miniTable2.setValueAt(preciored.divide(uno,3,preciored.ROUND_HALF_UP),m,6);
                                cont50++;
                                }
                                else
                                {
                                      try
                                      {
                                            StringBuffer prodid = new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID=? AND Name=?");
                                            PreparedStatement pstmtprodid = DB.prepareStatement(prodid.toString());
                                            pstmtprodid.setInt(1, AD_Client_ID);
                                            pstmtprodid.setString(2, prod2);
                                            ResultSet rsprodid = pstmtprodid.executeQuery();
                                            while (rsprodid.next())
                                            {
                                                product_id = rsprodid.getInt(1);
                                            }
                                            rsprodid.close();
                                            pstmtprodid.close();
                                        }
                                        catch(SQLException s)
                                        {
                                        }
                                        preciodeing(product_id);
                                        calculado(product_id,kgreal2);

//                                        System.out.println("calculado " +acalc.toString());
                                        preciored = new BigDecimal(m_PriceStd.doubleValue()*kgbd2.doubleValue());
//                                        System.out.println("precio por kilos " +preciored);
                                        miniTable2.setValueAt(preciored.divide(uno,3,preciored.ROUND_HALF_UP),m,6);
                                        cont50++;
                                  }
                        }
                        c25++;
                     }
                    cont50=0;
                    for (int n=0;n<miniTable2.getRowCount(); n++)
                    {
                         IDColumn id = (IDColumn)miniTable2.getValueAt(n, 0);//  ID in column 0

                         if (id != null)
                         {
                                 String kgreal3 = miniTable2.getValueAt(n,8).toString();
                                // System.out.println("kg redondeo ------" +kgreal3);
                                 BigDecimal kgbd3 = new BigDecimal(kgreal3);
                                 acumulado2 = new BigDecimal((kgbd3.doubleValue()/acumulado.doubleValue())*100.0);
                                // System.out.println("% redondeo ------" +acumulado2);
                                 miniTable2.setValueAt(acumulado2.setScale(3,5),n,7);
                    }
                    }
                int c26=0;
                realarr.clear();

                nutrientesarreglo();
//                System.out.println("tamano nutrientes  " +nutarr1.size());
                if (nutarr1.size()>0)
                {
                  atributos1(m_MPC_ProfileBOM_ID);

                        for (int n=0;n<nutarr1.size();n++)
                        {

                              //System.out.println("row mt2 --------------   " +nutarr1.get(n));
                              nutacum3=Env.ZERO;
                              nutacum2=Env.ZERO;
                              for (int i=0;i<miniTable2.getRowCount(); i++)
                              {
                                IDColumn  id = (IDColumn)miniTable2.getValueAt(i, 0);//  ID in column 0

                                if (id != null && id.isSelected())
                                {
                                    // validar si ya estan completos los requerimientos
                                    // calculo de la combinacion de ingredientes del costo menor
                                    String prod = miniTable2.getValueAt(i,5).toString();
                                    String kgreal = miniTable2.getValueAt(i,8).toString();
                                    nutacum3 = new BigDecimal(nutacum3.doubleValue()+nutacum2.doubleValue());

                               }

                        }
                }
                }

          for (int i=0;i<miniTable2.getRowCount(); i++)
          {
                    IDColumn id = (IDColumn)miniTable2.getValueAt(i, 0);//  ID in column 0
                    if (id != null)
                    {
                                 String pr3 = miniTable2.getValueAt(i,6).toString();
                                 //System.out.println("kg redondeo ------" +kgreal3);
                                 BigDecimal prbd3 = new BigDecimal(pr3);
                                 acumuladopr = new BigDecimal(prbd3.doubleValue()+acumuladopr.doubleValue());
                                // System.out.println("precio total ------" +acumuladopr);

                    }
          }
          realminmax();

          jTextField1.setText(acumuladopr.setScale(2,5).toString());
          jTextField1.disable();
          jTextField2.setText(acumulado.setScale(3,5).toString());
          jTextField2.disable();

          executeQueryReal();

          setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

         } // fin redondeo

         if (e.getSource() == jButton31)
         {
//              System.out.println("RECALCULAR      ");
              setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
              int c25=0;
              nutarr1.clear();
              acalc.clear();
              aunico.clear();
              BigDecimal acumulado=Env.ZERO;
              BigDecimal acumulado2=Env.ZERO;
              BigDecimal acumuladopr=Env.ZERO;
              BigDecimal preciored = Env.ZERO;
              int  profile_id =0;
              int cont50=0;
              cont500=0;
              for (int m=0;m<miniTable2.getRowCount(); m++)
              {
                  System.out.println("valor producto ***************"+miniTable2.getValueAt(m,4).toString());
                    IDColumn id = (IDColumn)miniTable2.getValueAt(m, 0);//  ID in column 0

                    if (id != null && id.isSelected())
                    {
                        // validar si ya estan completos los requerimientos
                        // calculo de la combinacion de ingredientes del costo menor
                        String prod2 = miniTable2.getValueAt(m,5).toString();
//                        System.out.println("prod mt2 " +prod2);
                        String kgreal2 = miniTable2.getValueAt(m,8).toString();
                        BigDecimal kgbd2 = new BigDecimal(kgreal2);
                        acumulado = new BigDecimal(acumulado.doubleValue()+kgbd2.doubleValue());
                        String ing_idst ="";
                        ing_idst = ing_idst.valueOf(ing_id);
                         if (miniTable2.getValueAt(m,3)!=null && miniTable2.getValueAt(m,3).toString().matches("true"))
                            {
//                                System.out.println("entra planteamiento ----------");
                                try
                                {
                                    StringBuffer prodid = new StringBuffer("SELECT MPC_ProfileBOM_ID FROM MPC_ProfileBOM WHERE AD_Client_ID=? AND Name=?");
                                    PreparedStatement pstmtprodid = DB.prepareStatement(prodid.toString());
                                    pstmtprodid.setInt(1, AD_Client_ID);
                                    pstmtprodid.setString(2, prod2);
                                    ResultSet rsprodid = pstmtprodid.executeQuery();
                                    while (rsprodid.next())
                                    {
                                        profile_id = rsprodid.getInt(1);
                                    }
                                    rsprodid.close();
                                    pstmtprodid.close();
                                }
                                catch(SQLException s)
                                {
                                }
                                String mpid ="";
                                mpid = mpid.valueOf(profile_id);
//                                System.out.println("prod id " +mpid);
                                //    preciodeing(mpid);
                                //    calculado(product_id,kgreal2);
                                BigDecimal costoplan1=Env.ZERO;
                                try
                                {       MMPCProfileBOM prof =new MMPCProfileBOM(Env.getCtx(),profile_id,null);
                                        StringBuffer sql10= new StringBuffer("");
                                        if (prof.getQty().compareTo(Env.ZERO)>0)
                                        {
                                        sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  where mp.IsPublished='Y' and mp.Value like '");
                                        sql10.append(miniTable2.getValueAt(m,4).toString() +"%'").append("Order by mp.Value");
                                        }
                                        else
                                        {
                                           sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  where mp.IsPublished='Y' and mp.Value like '");
                                        sql10.append(miniTable2.getValueAt(m,4).toString() +"%'").append("Order by mp.Value");
                                        }
                                        PreparedStatement pstmt10 = DB.prepareStatement(sql10.toString());
			               // pstmt10.setInt(1, product_id);
                                        ResultSet rs10 = pstmt10.executeQuery();

                                        if (rs10.next())
                                        {

                                            costoplan1 = rs10.getBigDecimal(1);
//                                            System.out.println("costo plan ---------------  " +costoplan1);
                                        }
                                        rs10.close();
                                        pstmt10.close();

                                         StringBuffer psql= new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID=? AND Value LIKE '");
                                        psql.append(miniTable2.getValueAt(m,4).toString() +"'");

                                        PreparedStatement pstmtp10 = DB.prepareStatement(psql.toString());
			                pstmt10.setInt(1, AD_Client_ID);
                                        ResultSet rsp10 = pstmtp10.executeQuery();

                                        if (rsp10.next())
                                        {
                                            product_id = rsp10.getInt(1);
                                            System.out.println("prod" +product_id);

                                        }
                                        rsp10.close();
                                        pstmtp10.close();


                                }
                                catch(SQLException s)
                                {
                                }
                                calculadop(profile_id,kgreal2);
                                BigDecimal uno = new BigDecimal(1.0);
                                //   System.out.println("calculado " +acalc.toString());
                                preciored = new BigDecimal(costoplan1.doubleValue()*kgbd2.doubleValue());
//                                System.out.println("precio por kilos " +preciored);
                                miniTable2.setValueAt(preciored.divide(uno,3,preciored.ROUND_HALF_UP),m,6);
                                cont50++;
                                }
                        else if (miniTable2.getValueAt(m,4).toString().startsWith("P"))
                            {
                                System.out.println("entra planteamiento ----------");
                                try
                                {
                                    StringBuffer prodid = new StringBuffer("SELECT MPC_ProfileBOM_ID FROM MPC_ProfileBOM WHERE AD_Client_ID=? AND Value LIKE '");
                                    prodid.append(miniTable2.getValueAt(m,4).toString() +"%'");
                                    PreparedStatement pstmtprodid = DB.prepareStatement(prodid.toString());
                                    pstmtprodid.setInt(1, AD_Client_ID);
                                   // pstmtprodid.setString(2, miniTable2.getValueAt(m,4).toString());
                                    ResultSet rsprodid = pstmtprodid.executeQuery();
                                    while (rsprodid.next())
                                    {
                                        profile_id = rsprodid.getInt(1);
                                    }
                                    rsprodid.close();
                                    pstmtprodid.close();



                                }
                                catch(SQLException s)
                                {
                                }
                                String mpid ="";
                                mpid = mpid.valueOf(profile_id);
//                                System.out.println("prod id " +mpid);
                                //    preciodeing(mpid);
                                //    calculado(product_id,kgreal2);
                                BigDecimal costoplan1=Env.ZERO;
                                try
                                {
                                        MMPCProfileBOM prof =new MMPCProfileBOM(Env.getCtx(),profile_id,null);
                                        StringBuffer sql10= new StringBuffer("");
                                        if (prof.getQty().compareTo(Env.ZERO)>0)
                                        {
                                        sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  where mp.IsPublished='Y' and mp.Value like '");
                                        sql10.append(miniTable2.getValueAt(m,4).toString() +"%'").append("Order by mp.Value");
                                        }
                                        else
                                        {
                                       sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  where mp.IsPublished='Y' and mp.Value like '");
                                        sql10.append(miniTable2.getValueAt(m,4).toString() +"%'").append("Order by mp.Value");
                                        }
                                        PreparedStatement pstmt10 = DB.prepareStatement(sql10.toString());
			                //pstmt10.setInt(1, product_id);
                                        ResultSet rs10 = pstmt10.executeQuery();

                                        if (rs10.next())
                                        {

                                            costoplan1 = rs10.getBigDecimal(1);
                                            System.out.println("costo plan ---------------  " +costoplan1);
                                        }
                                        rs10.close();
                                        pstmt10.close();


                                        StringBuffer psql= new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID=? AND Value LIKE '");
                                        psql.append(miniTable2.getValueAt(m,4).toString() +"'");

                                        PreparedStatement pstmtp10 = DB.prepareStatement(psql.toString());
			                pstmt10.setInt(1, AD_Client_ID);
                                        ResultSet rsp10 = pstmtp10.executeQuery();

                                        if (rsp10.next())
                                        {
                                            product_id = rsp10.getInt(1);
                                            System.out.println("prod" +product_id);

                                        }
                                        rsp10.close();
                                        pstmtp10.close();

                                }
                                catch(SQLException s)
                                {
                                }
                                calculado(product_id,kgreal2);
                                BigDecimal uno = new BigDecimal(1.0);
                                //   System.out.println("calculado " +acalc.toString());
                                preciored = new BigDecimal(costoplan1.doubleValue()*kgbd2.doubleValue());
//                                System.out.println("precio por kilos " +preciored);
                                miniTable2.setValueAt(preciored.divide(uno,3,preciored.ROUND_HALF_UP),m,6);
                                cont50++;
                                }
                                else if(miniTable2.getValueAt(m,4).toString().startsWith("I"))
                                {
                                      try
                                      {
                                            StringBuffer prodid = new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID=? AND Name=?");
                                            PreparedStatement pstmtprodid = DB.prepareStatement(prodid.toString());
                                            pstmtprodid.setInt(1, AD_Client_ID);
                                            pstmtprodid.setString(2, prod2);
                                            ResultSet rsprodid = pstmtprodid.executeQuery();
                                            while (rsprodid.next())
                                            {
                                                product_id = rsprodid.getInt(1);
                                            }
                                            rsprodid.close();
                                            pstmtprodid.close();
                                        }
                                        catch(SQLException s)
                                        {
                                        }

                                        preciodeing(product_id);
                                        calculado(product_id,kgreal2);
                                        BigDecimal uno = new BigDecimal(1.0);
//                                        System.out.println("calculado " +acalc.toString());
                                        preciored = new BigDecimal(m_PriceStd.doubleValue()*kgbd2.doubleValue());
//                                        System.out.println("precio por kilos " +preciored);
                                        miniTable2.setValueAt(preciored.divide(uno,3,preciored.ROUND_HALF_UP),m,6);
                                        cont50++;
                                  }

                                  c25++;
                        }
                  }
                    cont50=0;

                    for (int n=0;n<miniTable2.getRowCount(); n++)
                    {
                         IDColumn id = (IDColumn)miniTable2.getValueAt(n, 0);//  ID in column 0

                         if (id != null)
                         {
                                 String kgreal3 = miniTable2.getValueAt(n,8).toString();
                                // System.out.println("kg redondeo ------" +kgreal3);
                                 BigDecimal kgbd3 = new BigDecimal(kgreal3);
                                 acumulado2 = new BigDecimal((kgbd3.doubleValue()/acumulado.doubleValue())*100.0);
                                // System.out.println("% redondeo ------" +acumulado2);
                                 miniTable2.setValueAt(acumulado2.divide(Env.ONE,4,BigDecimal.ROUND_HALF_UP),n,7);
                    }
                    }
                int c26=0;
                realarr.clear();

                nutrientesarreglo();
//                System.out.println("tamano nutrientes  " +nutarr1.size());
                if (nutarr1.size()>0)
                {
                atributos1(m_MPC_ProfileBOM_ID);

                for (int i=0;i<nutarr1.size();i++)
                {

//                   System.out.println("row mt2 --------------   " +nutarr1.get(n));
                  nutacum3=Env.ZERO;
                  nutacum2=Env.ZERO;
                  for (int c=0;c<miniTable2.getRowCount(); c++)
                  {
                    IDColumn  id = (IDColumn)miniTable2.getValueAt(c, 0);//  ID in column 0

                  if (id != null && id.isSelected())
                  {
                        // validar si ya estan completos los requerimientos
                        // calculo de la combinacion de ingredientes del costo menor
			String prod = miniTable2.getValueAt(c,5).toString();
                        String kgreal = miniTable2.getValueAt(c,8).toString();
                        nutacum3 = new BigDecimal(nutacum3.doubleValue()+nutacum2.doubleValue());

                   }

                }
                }
                }

          for (int n=0;n<miniTable2.getRowCount(); n++)
                  {
                IDColumn id = (IDColumn)miniTable2.getValueAt(n, 0);//  ID in column 0

          if (id != null)
                    {
                                 String pr3 = miniTable2.getValueAt(n,6).toString();
                                 //System.out.println("kg redondeo ------" +kgreal3);
                                 BigDecimal prbd3 = new BigDecimal(pr3);
                                 acumuladopr = new BigDecimal(prbd3.doubleValue()+acumuladopr.doubleValue());
                                // System.out.println("precio total ------" +acumuladopr);

                    }
                    }
                // COpiar palomitas a ing sel

                                try
                                {
                                    // StringBuffer sel1 = new StringBuffer("Select MPC_ProfileBOM_Selected_ID from MPC_ProfileBOM_Selected where AD_Client_ID=1000000 order by MPC_ProfileBOM_Selected_ID Desc");
                                    StringBuffer sel12 = new StringBuffer("SELECT IsPrinted, IsAlias,M_Product_ID FROM MPC_ProfileBOM_Product WHERE AD_Client_ID=? AND MPC_ProfileBOM_ID=?");
                                    PreparedStatement pstmt12 = DB.prepareStatement(sel12.toString());
                                    pstmt12.setInt(1,AD_Client_ID);
                                    pstmt12.setInt(2,m_MPC_ProfileBOM_ID);

                                    ResultSet rs12 = pstmt12.executeQuery();

                                    while(rs12.next())
                                    {

//                                         if(rs12.getString(1)!=null && rs12.getString(1).equals("Y"))
//                                         {
//                                            miniTable2.setValueAt(new Boolean(true), row11, 1);
//                                            ipr="Y";
//                                         }
//                                         else
//                                         {
//                                            miniTable2.setValueAt(new Boolean(false), row11, 1);
//                                            ipr="N";
//                                         }
//                                         if(rs12.getString(2)!=null && rs12.getString(2).equals("Y"))
//                                         {
//                                            miniTable2.setValueAt(new Boolean(true), row11, 2);
//                                            ial="Y";
//                                         }
//                                         else
//                                         {
//                                             miniTable2.setValueAt(new Boolean(false), row11, 2);
//                                             ial="N";
//                                         }

                                    }
                                    rs12.close();
                                    pstmt12.close();
                                }
                                catch (SQLException s)
                                {
                                }
                ///

              //  realminmax();
                    jTextField1.setText(acumuladopr.setScale(2,5).toString());
               jTextField1.disable();
                 jTextField2.setText(acumulado.setScale(3,5).toString());
               jTextField2.disable();
              // executeQueryReal();

               setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                } // fin recalcular

		if (e.getSource() == jCopyNutAnalisis)
                {


                }

		if (e.getSource() == jButton1)
                {
//	System.out.println("boton1      ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
               executeQuerySearch();
          //     setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
              //  cmd_InfoPAttribute();
                }
        if (e.getSource() == jButtonG)
                {
//	System.out.println("botonG ing      ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
               executeQuerySearch2();
          //     setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
              //  cmd_InfoPAttribute();
                }

		if (e.getSource() == jButton32)
                {
//	System.out.println("guardar redondeo      ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
               fillselected();
          //      setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

		if (e.getSource() == jButton21)
                {
//	System.out.println("guardar reales      ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    fillreal();
        // setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }


            	if (e.getSource() == jButtonN)
                {
//                    System.out.println("botonN utrients     ");
          //          setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    jButton40.setText("Agregar");
                    jInternalFrame5.setBackground(new java.awt.Color(102, 102, 255));
                    jInternalFrame5.setClosable(true);
                    jInternalFrame5.setResizable(true);
                    jInternalFrame5.setVisible(true);
                    try {
                    jInternalFrame5.setSelected(true);
                    } catch (java.beans.PropertyVetoException e1) {
                         e1.printStackTrace();
                    }
                    jInternalFrame5.setTitle("Nutrientes");
                    jInternalFrame5.getContentPane().add(dataPane5, java.awt.BorderLayout.CENTER);
                    jInternalFrame5.getContentPane().add(jButton40, java.awt.BorderLayout.SOUTH);

                    jInternalFrame5.show();
                    jInternalFrame5.pack();


                    jInternalFrame5.setBounds(500, 0, 320, 500);
                    jDesktopPane1.add(jInternalFrame5, javax.swing.JLayeredPane.DEFAULT_LAYER);

                    jButton40.addActionListener(this);
                    executeQueryN();
                    //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }


            if (e.getSource() == jButtonI)
                {
//                    System.out.println("botonI      ");
                    //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    jButton41.setText("Agregar");
                    jInternalFrame10.setBackground(new java.awt.Color(102, 102, 102));
                    jInternalFrame10.setClosable(true);
                    jInternalFrame10.setResizable(true);
                    try {
                            jInternalFrame10.setSelected(true);
                    } catch (java.beans.PropertyVetoException e1) {
                            e1.printStackTrace();
                    }


                    jInternalFrame10.setVisible(true);
                    jInternalFrame10.setTitle("Ingredientes");
                    jInternalFrame10.getContentPane().add(dataPane10, java.awt.BorderLayout.CENTER);
                    jInternalFrame10.getContentPane().add(jButton41, java.awt.BorderLayout.SOUTH);
                    //jPanel5.add(jInternalFrame5, java.awt.BorderLayout.CENTER);
                    jInternalFrame10.show();
                    jInternalFrame10.pack();

                    jInternalFrame10.setBounds(600, 0, 350, 500);
                    jDesktopPane1.add(jInternalFrame10, javax.swing.JLayeredPane.DEFAULT_LAYER);
                    jButton41.addActionListener(this);
                    executeQueryI2();
                    //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

  //
                //agregar productos en ingredientes a formular
                if (e.getSource() == jButtonP)
                {
//	System.out.println("botonP      ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
 jButton58.setText("Agregar");
              jInternalFrame8.setBackground(new java.awt.Color(102, 102, 102));
        jInternalFrame8.setClosable(true);
        jInternalFrame8.setResizable(true);
         try {
            jInternalFrame8.setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }


        jInternalFrame8.setVisible(true);
        jInternalFrame8.setTitle("Productos");
        jInternalFrame8.getContentPane().add(dataPane8, java.awt.BorderLayout.CENTER);
        jInternalFrame8.getContentPane().add(jButton58, java.awt.BorderLayout.SOUTH);
        //jPanel5.add(jInternalFrame5, java.awt.BorderLayout.CENTER);
         jInternalFrame8.show();
 jInternalFrame8.pack();

        jInternalFrame8.setBounds(550, 0, 400, 500);
        jDesktopPane1.add(jInternalFrame8, javax.swing.JLayeredPane.DEFAULT_LAYER);
     jButton58.addActionListener(this);
     executeQueryP();
      //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }



                //
                //agregar productos en ingredientes a formular
                if (e.getSource() == jButtonNR)
                {
//	System.out.println("botonNR      ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
 jButton59.setText("Agregar");
              jInternalFrame9.setBackground(new java.awt.Color(102, 102, 102));
        jInternalFrame9.setClosable(true);
        jInternalFrame9.setResizable(true);
         try {
            jInternalFrame9.setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }


        jInternalFrame9.setVisible(true);
        jInternalFrame9.setTitle("Productos NR");
        jInternalFrame9.getContentPane().add(dataPane9, java.awt.BorderLayout.CENTER);
        jInternalFrame9.getContentPane().add(jButton59, java.awt.BorderLayout.SOUTH);
        //jPanel5.add(jInternalFrame5, java.awt.BorderLayout.CENTER);
         jInternalFrame9.show();
 jInternalFrame9.pack();

        jInternalFrame9.setBounds(650, 0, 400, 500);
        jDesktopPane1.add(jInternalFrame9, javax.swing.JLayeredPane.DEFAULT_LAYER);
     jButton59.addActionListener(this);
     executeQueryNR();
      //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
                //
                // cambio de agregar a ing sel fjv
                 if (e.getSource() == jButton33)
                {
//	System.out.println("boton agregar ingsel      ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

 jButton45.setText("Agregar");
              jInternalFrame6.setBackground(new java.awt.Color(102, 102, 102));
        jInternalFrame6.setClosable(true);
        jInternalFrame6.setResizable(true);
         try {
            jInternalFrame6.setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }


        jInternalFrame6.setVisible(true);
        jInternalFrame6.setTitle("Ingredientes");
        jInternalFrame6.getContentPane().add(dataPane6, java.awt.BorderLayout.CENTER);
        jInternalFrame6.getContentPane().add(jButton45, java.awt.BorderLayout.SOUTH);
        //jPanel5.add(jInternalFrame5, java.awt.BorderLayout.CENTER);
         jInternalFrame6.show();
 jInternalFrame6.pack();

        jInternalFrame6.setBounds(400, 0, 400, 500);
        jDesktopPane1.add(jInternalFrame6, javax.swing.JLayeredPane.DEFAULT_LAYER);
     jButton45.addActionListener(this);
     executeQueryI();
      //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

                 if (e.getSource() == jButton48)
                {
//	System.out.println("boton agregar prods      ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

 jButton47.setText("Agregar");
              jInternalFrame7.setBackground(new java.awt.Color(50, 102, 102));
        jInternalFrame7.setClosable(true);
        jInternalFrame7.setResizable(true);
         try {
            jInternalFrame7.setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }


        jInternalFrame7.setVisible(true);
        jInternalFrame7.setTitle("Productos");
        jInternalFrame7.getContentPane().add(dataPane7, java.awt.BorderLayout.CENTER);
        jInternalFrame7.getContentPane().add(jButton47, java.awt.BorderLayout.SOUTH);
        //jPanel5.add(jInternalFrame5, java.awt.BorderLayout.CENTER);
         jInternalFrame7.show();
 jInternalFrame7.pack();

        jInternalFrame7.setBounds(600, 0, 400, 500);
        jDesktopPane1.add(jInternalFrame7, javax.swing.JLayeredPane.DEFAULT_LAYER);
     jButton47.addActionListener(this);
     executeQueryI3();
      //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }// fin cambio


                if (e.getSource() == jButton45)
                {
                    int rows = 0;
                  int m_prod_id=0;
//	System.out.println("agregar ingrediente a la tabla de ing sel     ");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        rows = miniTable6.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable6.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rows);

                    if (id != null && id.isSelected())
                    {
                       // System.out.println("id" + id);
                       Integer ln=id.getRecord_ID();
                        // System.out.println("ln" + ln);
//         miniTable.editingStopped(new ChangeEvent(this));
               //System.out.println("vlookup de nutrientes" + nutrientes);
                //System.out.println("vlookup de nutrientes2" + nutrientes.toString());
               MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
            MMPCProfileBOMSelected profilebomsel45 = new MMPCProfileBOMSelected(Env.getCtx(),0,null);
            profilebomsel45.setMPC_ProfileBOM_ID(profilebom.getMPC_ProfileBOM_ID());
                 //MAttribute um = new MAttribute(Env.getCtx(),rs.get);
                   //             KeyNamePair kum = new KeyNamePair(um.getC_UOM_ID(),um.getName());
           String attr = miniTable6.getValueAt(i,2).toString();
//           Integer attr1=new Integer(0);
//           attr1.valueOf(attr);
//           System.out.println("producto name    -----------" +attr);

           try{
               StringBuffer sql45 = new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID=? AND Name=?");
    		PreparedStatement pstmt45 = DB.prepareStatement(sql45.toString());
			pstmt45.setInt(1,AD_Client_ID);
                        pstmt45.setString(2, attr);

			ResultSet rs45 = pstmt45.executeQuery();

			//
			while (rs45.next())
			{
				m_prod_id = rs45.getInt(1);

			}
			rs45.close();
			pstmt45.close();

           }
           catch(SQLException s)
           {
           }
         //  profilebomline.setLine();
          // System.out.println("attribute  --------" +m_prod_id);
           profilebomsel45.setM_Product_ID(m_prod_id);
            profilebomsel45.save();


                    }
                 }

            executeQuery3();
//               rows = 0;
//           //       int m_attr_id=0;
//
        rows = miniTable6.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable6.getValueAt(i, 0);//  ID in column 0
                    id.setSelected(false);
                 }
         setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

                // end fjv

                 if (e.getSource() == jButton47)
                {
                    int rows = 0;
                  int m_prod_id=0;
//	System.out.println("boton47 agregar desde mini de prod     ");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        rows = miniTable7.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable7.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rows);

                    if (id != null && id.isSelected())
                    {
                       // System.out.println("id" + id);
                       Integer ln=id.getRecord_ID();
                        // System.out.println("ln" + ln);
//         miniTable.editingStopped(new ChangeEvent(this));
               //System.out.println("vlookup de nutrientes" + nutrientes);
                //System.out.println("vlookup de nutrientes2" + nutrientes.toString());
               MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
            MMPCProfileBOMSelected profilebomsel47 = new MMPCProfileBOMSelected(Env.getCtx(),0,null);
            profilebomsel47.setMPC_ProfileBOM_ID(profilebom.getMPC_ProfileBOM_ID());
                 //MAttribute um = new MAttribute(Env.getCtx(),rs.get);
                   //             KeyNamePair kum = new KeyNamePair(um.getC_UOM_ID(),um.getName());
           String attr47 = miniTable7.getValueAt(i,2).toString();
//           Integer attr1=new Integer(0);
//           attr1.valueOf(attr);
//           System.out.println("");
           try{
               StringBuffer sql47 = new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID=? AND Name=?");
    		PreparedStatement pstmt47 = DB.prepareStatement(sql47.toString());
			pstmt47.setInt(1,AD_Client_ID);
                        pstmt47.setString(2, attr47);

			ResultSet rs47 = pstmt47.executeQuery();

			//
			while (rs47.next())
			{
				m_prod_id = rs47.getInt(1);

			}
			rs47.close();
			pstmt47.close();

           }
           catch(SQLException s)
           {
           }
         //  profilebomline.setLine();
          // System.out.println("attribute  --------" +m_prod_id);
           profilebomsel47.setM_Product_ID(m_prod_id);
            profilebomsel47.save();


                    }
                 }

            executeQuery3();
//                 rows = 0;
//           //       int m_attr_id=0;
//
        rows = miniTable7.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable7.getValueAt(i, 0);//  ID in column 0
                    id.setSelected(false);
                 }
         setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

                // end fjv

                // agregar producto en ventana de ingredientes a formular
                 if (e.getSource() == jButton58)
                { int rows = 0;
                  int m_prod_id=0;
//	System.out.println("boton58 agregar desde mini de producto     ");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        rows = miniTable8.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable8.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rows);

                    if (id != null && id.isSelected())
                    {
                       // System.out.println("id" + id);
                       Integer ln=id.getRecord_ID();
                        // System.out.println("ln" + ln);
//         miniTable.editingStopped(new ChangeEvent(this));
               //System.out.println("vlookup de nutrientes" + nutrientes);
                //System.out.println("vlookup de nutrientes2" + nutrientes.toString());
               MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
            MMPCProfileBOMProduct profilebomproduct = new MMPCProfileBOMProduct(Env.getCtx(),0,null);
            profilebomproduct.setMPC_ProfileBOM_ID(profilebom.getMPC_ProfileBOM_ID());
                 //MAttribute um = new MAttribute(Env.getCtx(),rs.get);
                   //             KeyNamePair kum = new KeyNamePair(um.getC_UOM_ID(),um.getName());
           String attr = miniTable8.getValueAt(i,2).toString();
//           Integer attr1=new Integer(0);
//           attr1.valueOf(attr);
//           System.out.println("");
           try
           {
                        StringBuffer sql1 = new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE IsActive='Y' AND AD_Client_ID=? AND Name=?");
                        PreparedStatement pstmt = DB.prepareStatement(sql1.toString());
			pstmt.setInt(1,AD_Client_ID);
                        pstmt.setString(2, attr);

			ResultSet rs = pstmt.executeQuery();

			//
			while (rs.next())
			{
				m_prod_id = rs.getInt(1);

			}
			rs.close();
			pstmt.close();

           }
           catch(SQLException s)
           {
           }
         //  profilebomline.setLine();
          // System.out.println("attribute  --------" +m_prod_id);
           profilebomproduct.setM_Product_ID(m_prod_id);
            profilebomproduct.save();


                    }
                 }

            formular();
                 rows = 0;
           //       int m_attr_id=0;

        rows = miniTable8.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable8.getValueAt(i, 0);//  ID in column 0
                    id.setSelected(false);
                 }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
                //

                // agregar producto en ventana de ingredientes a formular
                 if (e.getSource() == jButton59)
                { int rows = 0;
                  int m_prod_id=0;
//	System.out.println("boton59 agregar desde mini de producto     ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        rows = miniTable9.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable9.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rows);

                    if (id != null && id.isSelected())
                    {
                       // System.out.println("id" + id);
                       Integer ln=id.getRecord_ID();
                        // System.out.println("ln" + ln);
//         miniTable.editingStopped(new ChangeEvent(this));
               //System.out.println("vlookup de nutrientes" + nutrientes);
                //System.out.println("vlookup de nutrientes2" + nutrientes.toString());
               MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
            MMPCProfileBOMProduct profilebomproduct = new MMPCProfileBOMProduct(Env.getCtx(),0,null);
            profilebomproduct.setMPC_ProfileBOM_ID(profilebom.getMPC_ProfileBOM_ID());
                 //MAttribute um = new MAttribute(Env.getCtx(),rs.get);
                   //             KeyNamePair kum = new KeyNamePair(um.getC_UOM_ID(),um.getName());
           String attr = miniTable9.getValueAt(i,2).toString();
//           Integer attr1=new Integer(0);
//           attr1.valueOf(attr);
//           System.out.println("");
           try{
               StringBuffer sql1 = new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID=? and Name=?");
    		PreparedStatement pstmt = DB.prepareStatement(sql1.toString());
			pstmt.setInt(1,AD_Client_ID);
                        pstmt.setString(2, attr);

			ResultSet rs = pstmt.executeQuery();

			//
			while (rs.next())
			{
				m_prod_id = rs.getInt(1);

			}
			rs.close();
			pstmt.close();

           }
           catch(SQLException s)
           {
           }
         //  profilebomline.setLine();
          // System.out.println("attribute  --------" +m_prod_id);
           profilebomproduct.setM_Product_ID(m_prod_id);
            profilebomproduct.save();


                    }
                 }

            formular();
                 rows = 0;
           //       int m_attr_id=0;

        rows = miniTable9.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable9.getValueAt(i, 0);//  ID in column 0
                    id.setSelected(false);
                 }
        //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
                //

//            	if (e.getSource() == jButton41)
//                { int rows = 0;
//                  int m_prod_id=0;
////	System.out.println("boton41     ");
//        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//        rows = miniTable6.getRowCount();
//
//                 for (int i = 0; i < rows; i++)
//		{
//
//                    IDColumn id = (IDColumn)miniTable6.getValueAt(i, 0);//  ID in column 0
//
//                    log.log(Level.FINE, "Row="+ rows);
//
//                    if (id != null && id.isSelected())
//                    {
//                       // System.out.println("id" + id);
//                       Integer ln=id.getRecord_ID();
//                        // System.out.println("ln" + ln);
////         miniTable.editingStopped(new ChangeEvent(this));
//               //System.out.println("vlookup de nutrientes" + nutrientes);
//                //System.out.println("vlookup de nutrientes2" + nutrientes.toString());
//               MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID);
//            MMPCProfileBOMProduct profilebomproduct = new MMPCProfileBOMProduct(Env.getCtx(),0);
//            profilebomproduct.setMPC_ProfileBOM_ID(profilebom.getMPC_ProfileBOM_ID());
//                 //MAttribute um = new MAttribute(Env.getCtx(),rs.get);
//                   //             KeyNamePair kum = new KeyNamePair(um.getC_UOM_ID(),um.getName());
//           String attr = miniTable6.getValueAt(i,2).toString();
////           Integer attr1=new Integer(0);
////           attr1.valueOf(attr);
////           System.out.println("");
//           try{
//               StringBuffer sql41 = new StringBuffer("Select M_Product_ID From M_Product Where AD_Client_ID=? and Name=?");
//    		PreparedStatement pstmt41 = DB.prepareStatement(sql1.toString());
//			pstmt.setInt(1,AD_Client_ID);
//                        pstmt.setString(2, attr);
//
//			ResultSet rs = pstmt.executeQuery();
//
//			//
//			while (rs.next())
//			{
//				m_prod_id = rs.getInt(1);
//
//			}
//			rs.close();
//			pstmt.close();
//
//           }
//           catch(SQLException s)
//           {
//           }
//         //  profilebomline.setLine();
//          // System.out.println("attribute  --------" +m_prod_id);
//           profilebomproduct.setM_Product_ID(m_prod_id);
//            profilebomproduct.save();
//
//
//                    }
//                 }
//
//            formular();
//                 rows = 0;
//           //       int m_attr_id=0;
//
//        rows = miniTable6.getRowCount();
//
//                 for (int i = 0; i < rows; i++)
//		{
//
//                    IDColumn id = (IDColumn)miniTable6.getValueAt(i, 0);//  ID in column 0
//                    id.setSelected(false);
//                 }
//         setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//                }

               if (e.getSource() == jButton41)
                {
                    int rows = 0;
                  int m_prod_id=0;
//	System.out.println("agregar ingrediente a la tabla de ing sel     ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        rows = miniTable10.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable10.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rows);

                    if (id != null && id.isSelected())
                    {
                       // System.out.println("id" + id);
                       Integer ln=id.getRecord_ID();
                        // System.out.println("ln" + ln);
//         miniTable.editingStopped(new ChangeEvent(this));
               //System.out.println("vlookup de nutrientes" + nutrientes);
                //System.out.println("vlookup de nutrientes2" + nutrientes.toString());
               MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
            MMPCProfileBOMProduct profilebomsel41 = new MMPCProfileBOMProduct(Env.getCtx(),0,null);
            profilebomsel41.setMPC_ProfileBOM_ID(profilebom.getMPC_ProfileBOM_ID());
                 //MAttribute um = new MAttribute(Env.getCtx(),rs.get);
                   //             KeyNamePair kum = new KeyNamePair(um.getC_UOM_ID(),um.getName());
           String attr = miniTable10.getValueAt(i,2).toString();
//           Integer attr1=new Integer(0);
//           attr1.valueOf(attr);
//           System.out.println("producto name    -----------" +attr);

           try{
               StringBuffer sql41 = new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID=? AND Name=?");
    		PreparedStatement pstmt41 = DB.prepareStatement(sql41.toString());
			pstmt41.setInt(1,AD_Client_ID);
                        pstmt41.setString(2, attr);

			ResultSet rs41 = pstmt41.executeQuery();

			//
			while (rs41.next())
			{
				m_prod_id = rs41.getInt(1);

			}
			rs41.close();
			pstmt41.close();

           }
           catch(SQLException s)
           {
           }
         //  profilebomline.setLine();
          // System.out.println("attribute  --------" +m_prod_id);
           profilebomsel41.setM_Product_ID(m_prod_id);
            profilebomsel41.save();


                    }
                 }

            formular();
//               rows = 0;
//           //       int m_attr_id=0;
//
        rows = miniTable10.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable6.getValueAt(i, 0);//  ID in column 0
                    id.setSelected(false);
                 }
         //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }



                if (e.getSource() == jButton12)
                {
           //System.out.println("boton12      ");
           //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
              //  generatesearch();
           miniTable4.editingStopped(new ChangeEvent(this));
               //System.out.println("vlookup de nutrientes" + nutrientes);
                //System.out.println("vlookup de nutrientes2" + nutrientes.toString());
           MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
           MMPCProfileBOMProduct profilebomproduct = new MMPCProfileBOMProduct(Env.getCtx(),0,null);
            //System.out.println("mpc_profileBom   ----" +m_MPC_ProfileBOM_ID);
            //System.out.println("profilebom.getMPC_ProfileBOM_ID()" +profilebom.getMPC_ProfileBOM_ID());
           profilebomproduct.setMPC_ProfileBOM_ID(profilebom.getMPC_ProfileBOM_ID());
           String attrp = ingredientes.getValue() == null ? "" :  ingredientes.getValue().toString();
           Integer attrp1=new Integer(0);
           attrp1.valueOf(attrp);

           //profilebomproduct.setLine(20);
           profilebomproduct.setPlanteamiento(attrp1.valueOf(attrp).intValue());
           profilebomproduct.setPriceList(cero);
            profilebomproduct.save();
            formular();
      // executeQuerySearch();
             //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
              /*
                if (e.getSource() == jButton2)
                {
//	System.out.println("Aceptar Formula      ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (ADialog.ask(m_WindowNo, this, "�Estas seguro de generar la F�rmula?"))
        {
         MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID);
           MMPCProfileBOMSelected profilebomsel = new MMPCProfileBOMSelected(profilebom);
           MMPCProductBOM prodbom = new MMPCProductBOM(Env.getCtx(),0);
           prodbom.setValue(profilebom.getValue());
           prodbom.setName(profilebom.getName());
           prodbom.setDescription(profilebom.getDescription());
        //   prodbom.setM_Product_ID(profilebom.getName());
           prodbom.setValidFrom(profilebom.getDateDoc());
           prodbom.setC_UOM_ID(profilebom.getC_UOM_ID());
           prodbom.setM_Product_ID(profilebom.getM_Product_ID());
           // comentado temporal
//           System.out.println("producto --   "+profilebom.getM_Product_ID());
           int m_MPC_ProfileBOMCost_ID=0;
            try{
               StringBuffer sql1 = new StringBuffer("SELECT MPC_ProfileBOMCost_ID FROM MPC_ProfileBOMCost WHERE IsActive='Y' AND AD_Client_ID=? and MPC_ProfileBOM_ID=?");
    		PreparedStatement pstmt = DB.prepareStatement(sql1.toString());
                        pstmt.setInt(1,AD_Client_ID);
			pstmt.setInt(2, m_MPC_ProfileBOM_ID);

			ResultSet rs = pstmt.executeQuery();

			//
			while (rs.next())
			{
				m_MPC_ProfileBOMCost_ID = rs.getInt(1);

			}
			rs.close();
			pstmt.close();

           }
           catch(SQLException s)
           {
           }
//            System.out.println("profilecost --   "+m_MPC_ProfileBOMCost_ID);
           MMPCProfileBOMCost bolsa = new MMPCProfileBOMCost(Env.getCtx(),m_MPC_ProfileBOMCost_ID);
           if (bolsa.getM_ProductE_ID()!=0)
           {
           if (profilebom.getM_Product_ID()!=0)
           {
                atributos(profilebom.getM_Product_ID());
           prodbom.setM_AttributeSetInstance_ID(m_instance);
           //profilebom.setM_AttributeSetInstance_ID(m_instance);
//          MProduct producto = new MProduct(Env.getCtx(),profilebom.getM_Product_ID());
////             producto.setValue(profilebom.getValue());
////           producto.setName(profilebom.getName());
////           producto.setIsPurchased(false);
////           producto.setM_Product_Category_ID(profilebom.getM_Product_Category_ID());
////           producto.setVersionNo(profilebom.getSpecie());
////           producto.setC_UOM_ID(profilebom.getC_UOM_ID());
////           producto.setProductType(producto.PRODUCTTYPE_Item);
////           producto.setC_TaxCategory_ID(1000001);
////           producto.setM_AttributeSet_ID(1000001);
//           producto.setM_AttributeSetInstance_ID(m_instance);
//
//           producto.save();
//           if (producto.save())
//           {
//              profilebom.setM_Product_ID(producto.getM_Product_ID());
           java.util.Date today =new java.util.Date();
java.sql.Timestamp now =new java.sql.Timestamp(today.getTime());
               profilebom.setValidFrom(now);
               profilebom.save();
//           }


        //   prodbom.save();
                if(prodbom.save())
       {
           String salvado1 = new String("F�rmula creada con �xito "+prodbom.getValue() +" como el Documento no. "+prodbom.getDocumentNo());
           ADialog.info(m_WindowNo,this,salvado1);
       }
       else
       {
           ADialog.log(Level.SEVEREm_WindowNo,this,"No se pudo generar la F�rmula");
       }
           for (int i=0;i<miniTable2.getRowCount(); i++)
           {
           String mp = "";
           mp = miniTable2.getValueAt(i,5).toString();
             try{
               StringBuffer sql1 = new StringBuffer("Select M_Product_ID From M_Product Where AD_Client_ID=? and Name=?");
    		PreparedStatement pstmt = DB.prepareStatement(sql1.toString());
                        pstmt.setInt(1,AD_Client_ID);
			pstmt.setString(2, mp);

			ResultSet rs = pstmt.executeQuery();

			//
			while (rs.next())
			{
				m_prod_id = rs.getInt(1);

			}
			rs.close();
			pstmt.close();

           }
           catch(SQLException s)
           {
           }
            MMPCProductBOMLine prodbomline = new MMPCProductBOMLine(prodbom);
           prodbomline.setM_Product_ID(m_prod_id);
           BigDecimal p = new BigDecimal(miniTable2.getValueAt(i,7).toString());
           prodbomline.setQtyBatch(p);
           prodbomline.setValidFrom(prodbom.getValidFrom());
           prodbomline.setValidTo(prodbom.getValidTo());
           prodbomline.setLine(i*10);
           prodbomline.setC_UOM_ID(profilebom.getC_UOM_ID());
           prodbomline.setIsQtyPercentage(true);
           prodbomline.setIssueMethod(prodbomline.ISSUEMETHOD_BackFlush);
           prodbomline.save();

           }
           }


//               MMPCProductBOMLine prodbomline = new MMPCProductBOMLine(prodbom);
//           prodbomline.setM_Product_ID(bolsa.getM_ProductE_ID());
//
//           prodbomline.setQtyBatch(bolsa.getQtyE());
//           prodbomline.setValidFrom(prodbom.getValidFrom());
//           prodbomline.setValidTo(prodbom.getValidTo());
//           prodbomline.setLine(5);
//           prodbomline.setC_UOM_ID(100);
//           prodbomline.setIsQtyPercentage(true);
//           prodbomline.setIssueMethod(prodbomline.ISSUEMETHOD_BackFlush);
//           prodbomline.save();
           }
           else
           {
             ADialog.log(Level.SEVEREm_WindowNo,this,"No se ha definido el empaque");
           }


        }
         //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
                */
          if (e.getSource() == jButtonCA)
                {
//	System.out.println("Aceptar Formula      ");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (ADialog.ask(m_WindowNo, this, "Este proceso genera el perfil para el planteamiento �Estas seguro?"))
        {
         MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);

                      // comentado temporal
//           System.out.println("producto --   "+profilebom.getM_Product_ID());
//           if (profilebom.getM_Product_ID()!=0)
//           {
                atributos(profilebom.getM_Product_ID());

                profilebom.setM_AttributeSetInstance_ID(m_instance);
                if (profilebom.getM_Product_ID()!=0)
                {
                    MProduct producto = new MProduct(Env.getCtx(),profilebom.getM_Product_ID(),null);
                    producto.setM_AttributeSetInstance_ID(m_instance);
                     if(producto.save())
                     {
                        ADialog.info(m_WindowNo,this,"Perfil creada con �xito para el producto "+producto.getValue());
                     }
                     else
                     {
                        ADialog.info(m_WindowNo,this,"No se pudo generar el perfilpara el producto");
                     }
                }
                 if(profilebom.save())
       {
           String salvado1 = new String("Perfil creada con �xito "+profilebom.getValue());
           ADialog.info(m_WindowNo,this,salvado1);
       }
       else
       {
           ADialog.info(0,this,"No se pudo generar el perfil");
       }

            }
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

          if (e.getSource() == jButtonCP)
                {
//	System.out.println("Aceptar Formula      ");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (ADialog.ask(m_WindowNo, this, "�Estas seguro de crear el producto?"))
        {
                 MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
                 if (profilebom.getM_Product_ID()==0)
                 {
                 MProduct producto = new MProduct (Env.getCtx(),0,null);
                 atributos(profilebom.getM_Product_ID());
                // producto.setValue(profilebom.getValue());
                   producto.setName(profilebom.getName());
                   producto.setIsPurchased(false);
                   producto.setM_Product_Category_ID(profilebom.getM_Product_Category_ID());
                   producto.setVersionNo(profilebom.getSpecie());
                   producto.setC_UOM_ID(profilebom.getC_UOM_ID());
                   producto.setProductType(producto.PRODUCTTYPE_Item);
                   producto.setC_TaxCategory_ID(1000001);
                   producto.setM_AttributeSet_ID(1000001);
                   producto.setM_AttributeSetInstance_ID(m_instance);
                    if(producto.save())
                   {
                           String salvado1 = new String("Producto creado con �xito "+producto.getValue());
                           ADialog.info(m_WindowNo,this,salvado1);
                   }
                   else
                   {
                       ADialog.info(m_WindowNo,this,"No se pudo crear el Producto");
                   }
                 }


            }
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

          //  convertir a dosis
          if (e.getSource() == jButton3)
          {
             //                    System.out.println("REDONDEO");
              MMPCProfileBOM profile =new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);

                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    int c25=0;
                    nutarr1.clear();
                    acalc.clear();
                    aunico.clear();
                    BigDecimal acumulado=Env.ZERO;
                    BigDecimal acumulado2=Env.ZERO;
                    BigDecimal acumuladopr=Env.ZERO;
                    BigDecimal preciored = Env.ZERO;
                    BigDecimal dosis =Env.ZERO;
                    BigDecimal mil = new BigDecimal(1000);
                    int cont50=0;
                    cont500=0;
                    dosis = profile.getContenidoNeto();
                    if (profile.getQty().compareTo(Env.ZERO) !=0)
                        dosis = dosis.divide(profile.getQty(),4,BigDecimal.ROUND_HALF_UP);
                    else
                        dosis =Env.ONE;
                    for (int m=0;m<miniTable2.getRowCount(); m++)
                    {
                        IDColumn id = (IDColumn)miniTable2.getValueAt(m, 0);//  ID in column 0
                        if (id != null)
                        {
                            // validar si ya estan completos los requerimientos
                            // calculo de la combinacion de ingredientes del costo menor
                            String prod2 = miniTable2.getValueAt(m,5).toString();
                            String kgreal2 = miniTable2.getValueAt(m,8).toString();
                            BigDecimal kgbd2 = new BigDecimal(kgreal2);

                            int profile_id =0;

//                                System.out.println("Factor-----------" +vol);
                                BigDecimal uno = new BigDecimal(1.0);
                                if (dosis.compareTo(Env.ZERO) != 0)
                                {


                                    kgbd2=kgbd2.multiply(dosis);
//                                    System.out.println("kilos redondeados-----------" +kgbd2);
                                }

                                acumulado = new BigDecimal(acumulado.doubleValue()+kgbd2.doubleValue());
                                miniTable2.setValueAt(kgbd2,m,8);
                                String ing_idst ="";
                                ing_idst = ing_idst.valueOf(ing_id);

                                if (miniTable2.getValueAt(m,3)!=null && miniTable2.getValueAt(m,3).toString().matches("true"))
                                {
//                                    System.out.println("entra planteamiento ----------");
                                    try
                                    {
                                        StringBuffer prodid = new StringBuffer("SELECT MPC_ProfileBOM_ID FROM MPC_ProfileBOM WHERE AD_Client_ID=? and Name=?");
                                        PreparedStatement pstmtprodid = DB.prepareStatement(prodid.toString());
                                        pstmtprodid.setInt(1, AD_Client_ID);
                                        pstmtprodid.setString(2, prod2);
                                        ResultSet rsprodid = pstmtprodid.executeQuery();
                                        while (rsprodid.next())
                                        {
                                            profile_id = rsprodid.getInt(1);
                                        }
                                        rsprodid.close();
                                        pstmtprodid.close();
                                    }
                                    catch(SQLException s)
                                    {
                                    }
                                    String mpid ="";
                                    mpid = mpid.valueOf(profile_id);
//                                    System.out.println("prod id " +mpid);
                                    //    preciodeing(mpid);
                                    //    calculado(product_id,kgreal2);
                                    BigDecimal costoplan1=Env.ZERO;
                                    try
                                    {
                                      MMPCProfileBOM prof =new MMPCProfileBOM(Env.getCtx(),profile_id,null);
                                        StringBuffer sql10= new StringBuffer("");
                                        if (prof.getQty().compareTo(Env.ZERO)>0)
                                        {
                                            sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  WHERE mp.IsPublished='Y' and mp.Value like '");
                                        sql10.append(miniTable2.getValueAt(m,4).toString() +"%'").append("ORDER BY mp.Value");
                                       // sql10= new StringBuffer("SELECT Trunc(mp.PriceList/mp.Qty,2) from MPC_ProfileBOM mp  where mp.AD_Client_ID=1000000 and mp.Value like '");
                                       // sql10.append(miniTable2.getValueAt(m,4).toString() +"%'");
                                        }
                                        else
                                        {
                                            sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  WHERE mp.IsPublished='Y' and mp.Value like '");
                                        sql10.append(miniTable2.getValueAt(m,4).toString() +"%'").append("ORDER BY mp.Value");
                                        }
                                        PreparedStatement pstmt10 = DB.prepareStatement(sql10.toString());
			               // pstmt10.setInt(1, product_id);
                                        ResultSet rs10 = pstmt10.executeQuery();

                                        if (rs10.next())
                                        {
                                            costoplan1 = rs10.getBigDecimal(1);
//                                            System.out.println("costo plan ---------------  " +costoplan1);
                                        }
                                        rs10.close();
                                        pstmt10.close();

                                         StringBuffer psql= new StringBuffer("SELECT M_Product_ID FROM M_Product  WHERE + " + AD_Client_ID + "AND Value LIKE '");
                                        psql.append(miniTable2.getValueAt(m,4).toString() +"'");

                                        PreparedStatement pstmtp10 = DB.prepareStatement(psql.toString());
			                //pstmt10.setInt(1, product_id);
                                        ResultSet rsp10 = pstmtp10.executeQuery();

                                        if (rsp10.next())
                                        {
                                            product_id = rsp10.getInt(1);
                                            System.out.println("prod" +product_id);

                                        }
                                        rsp10.close();
                                        pstmtp10.close();
                                    }
                                    catch(SQLException s)
                                    {
                                    }
                                    calculadop(profile_id,kgreal2);
                                //   System.out.println("calculado " +acalc.toString());
                                preciored = costoplan1.multiply(kgbd2);
//                                System.out.println("precio por kilos " +preciored);
                                miniTable2.setValueAt(preciored.divide(uno,3,preciored.ROUND_HALF_UP),m,6);
                                cont50++;
                                }
                                else if (miniTable2.getValueAt(m,4).toString().startsWith("P"))
                                {
//                                    System.out.println("entra planteamiento ----------");
                                    try
                                    {
                                        StringBuffer prodid = new StringBuffer("SELECT MPC_ProfileBOM_ID FROM MPC_ProfileBOM WHERE AD_Client_ID=? and Name=?");
                                        PreparedStatement pstmtprodid = DB.prepareStatement(prodid.toString());
                                        pstmtprodid.setInt(1, AD_Client_ID);
                                        pstmtprodid.setString(2, prod2);
                                        ResultSet rsprodid = pstmtprodid.executeQuery();
                                        while (rsprodid.next())
                                        {
                                            profile_id = rsprodid.getInt(1);
                                        }
                                        rsprodid.close();
                                        pstmtprodid.close();
                                    }
                                    catch(SQLException s)
                                    {
                                    }
                                    String mpid ="";
                                    mpid = mpid.valueOf(profile_id);
//                                    System.out.println("prod id " +mpid);
                                    //    preciodeing(mpid);
                                    //    calculado(product_id,kgreal2);
                                    BigDecimal costoplan1=Env.ZERO;
                                    try
                                    {
                                        MMPCProfileBOM prof =new MMPCProfileBOM(Env.getCtx(),profile_id,null);
                                        StringBuffer sql10= new StringBuffer("");
                                        if (prof.getQty().compareTo(Env.ZERO)>0)
                                        {
                                        sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  where mp.IsPublished='Y' and mp.Value like '");
                                        sql10.append(miniTable2.getValueAt(m,4).toString() +"%'").append("Order by mp.Value");
                                        }
                                        else
                                        {
                                           sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  where mp.IsPublished='Y' and mp.Value like '");
                                        sql10.append(miniTable2.getValueAt(m,4).toString() +"%'").append("Order by mp.Value");
                                        }
                                        PreparedStatement pstmt10 = DB.prepareStatement(sql10.toString());
			                //pstmt10.setInt(1, product_id);
                                        ResultSet rs10 = pstmt10.executeQuery();

                                        if (rs10.next())
                                        {
                                            costoplan1 = rs10.getBigDecimal(1);
//                                            System.out.println("costo plan ---------------  " +costoplan1);
                                        }
                                        rs10.close();
                                        pstmt10.close();

                                         StringBuffer psql= new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID=? AND Value = ?");


                                        PreparedStatement pstmtp10 = DB.prepareStatement(psql.toString());
                                        pstmtp10.setInt(1, AD_Client_ID);
			                pstmtp10.setString(2, miniTable2.getValueAt(m,4).toString());
                                        ResultSet rsp10 = pstmtp10.executeQuery();

                                        if (rsp10.next())
                                        {
                                            product_id = rsp10.getInt(1);
                                            System.out.println("prod" +product_id);

                                        }
                                        rsp10.close();
                                        pstmtp10.close();
                                    }
                                    catch(SQLException s)
                                    {
                                    }
                                    calculado(product_id,kgreal2);
                                //   System.out.println("calculado " +acalc.toString());
                                preciored = new BigDecimal(costoplan1.doubleValue()*kgbd2.doubleValue());
//                                System.out.println("precio por kilos " +preciored);
                                miniTable2.setValueAt(preciored.divide(uno,3,preciored.ROUND_HALF_UP),m,6);
                                cont50++;
                                }
                                else
                                {
                                      try
                                      {
                                            StringBuffer prodid = new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID=? AND Name=?");
                                            PreparedStatement pstmtprodid = DB.prepareStatement(prodid.toString());
                                            pstmtprodid.setInt(1, AD_Client_ID);
                                            pstmtprodid.setString(2, prod2);
                                            ResultSet rsprodid = pstmtprodid.executeQuery();
                                            while (rsprodid.next())
                                            {
                                                product_id = rsprodid.getInt(1);
                                            }
                                            rsprodid.close();
                                            pstmtprodid.close();
                                        }
                                        catch(SQLException s)
                                        {
                                        }
                                        preciodeing(product_id);
                                        calculado(product_id,kgreal2);

//                                        System.out.println("calculado " +acalc.toString());
                                        preciored = new BigDecimal(m_PriceStd.doubleValue()*kgbd2.doubleValue());
//                                        System.out.println("precio por kilos " +preciored);
                                        miniTable2.setValueAt(preciored.divide(uno,3,preciored.ROUND_HALF_UP),m,6);
                                        cont50++;
                                  }
                        }
                        c25++;
                     }
                    cont50=0;
                    for (int n=0;n<miniTable2.getRowCount(); n++)
                    {
                         IDColumn id = (IDColumn)miniTable2.getValueAt(n, 0);//  ID in column 0

                         if (id != null)
                         {
                                 String kgreal3 = miniTable2.getValueAt(n,8).toString();
                                // System.out.println("kg redondeo ------" +kgreal3);
                                 BigDecimal kgbd3 = new BigDecimal(kgreal3);
                                 acumulado2 = new BigDecimal((kgbd3.doubleValue()/acumulado.doubleValue())*100.0);
                                // System.out.println("% redondeo ------" +acumulado2);
                                 miniTable2.setValueAt(acumulado2.setScale(3,5),n,7);
                    }
                    }
                int c26=0;
                realarr.clear();

                nutrientesarreglo();
//                System.out.println("tamano nutrientes  " +nutarr1.size());
                if (nutarr1.size()>0)
                {
                atributos1(m_MPC_ProfileBOM_ID);

                for (int n=0;n<nutarr1.size();n=n+3)
             {

//                   System.out.println("row mt2 --------------   " +nutarr1.get(n));
                  nutacum3=Env.ZERO;
                  nutacum2=Env.ZERO;
                  for (int m1=0;m1<miniTable2.getRowCount(); m1++)
                  {
                    IDColumn  id = (IDColumn)miniTable2.getValueAt(m1, 0);//  ID in column 0

          if (id != null && id.isSelected())
                  {
                        // validar si ya estan completos los requerimientos
                        // calculo de la combinacion de ingredientes del costo menor
			String prod = miniTable2.getValueAt(m1,5).toString();
                        String kgreal = miniTable2.getValueAt(m1,8).toString();
                        nutacum3 = new BigDecimal(nutacum3.doubleValue()+nutacum2.doubleValue());

                   }

          }
                }
                }

          for (int n=0;n<miniTable2.getRowCount(); n++)
                  {
                IDColumn id = (IDColumn)miniTable2.getValueAt(n, 0);//  ID in column 0

          if (id != null)
                    {
                                 String pr3 = miniTable2.getValueAt(n,6).toString();
                                 //System.out.println("kg redondeo ------" +kgreal3);
                                 BigDecimal prbd3 = new BigDecimal(pr3);
                                 acumuladopr = new BigDecimal(prbd3.doubleValue()+acumuladopr.doubleValue());
                                // System.out.println("precio total ------" +acumuladopr);

                    }
                    }

                realminmax();
                    jTextField1.setText(acumuladopr.setScale(2,5).toString());
               jTextField1.disable();
                 jTextField2.setText(acumulado.setScale(3,5).toString());
               jTextField2.disable();
               executeQueryReal();


                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
          }

          /*
                   if (e.getSource() == jButton3)
                {
//	System.out.println("boton3      ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (ADialog.ask(m_WindowNo, this, "�Estas seguro de Generar la cotizaci�n para este producto?"))
        {
             MMPCProfileBOM profileorder = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID);
                int m_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
                int m_AD_Org_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Org_ID"));
             //   int m_M_Warehouse_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#M_Warehouse_ID"));
    //    MProfileBOM profileorder = new MProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID);

        MOrder order = new MOrder(Env.getCtx(),0);
   //     order.setClientOrg(m_Client_ID, m_AD_Org_ID);
//        order.setAD_Client_ID(m_Client_ID);
        order.setAD_Org_ID(1000006);
       // System.out.println("Cliente" +profileorder.getC_BPartner_ID());
        order.setC_BPartner_ID(profileorder.getC_BPartner_ID());
        int user=0;
        try
        {
      StringBuffer sql = new StringBuffer("Select AD_User_ID From AD_User where AD_Client_ID=? and C_BPartner_ID=?");
		//  reset table
		//  Execute

			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1,AD_Client_ID);
                        pstmt.setInt(2, profileorder.getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next())
			{
                            user= rs.getInt(1);
			}
			rs.close();
			pstmt.close();
        }
        catch(SQLException s)
        {
        }
        order.setSalesRep_ID(user);
        //order.setSalesRep_ID(order.SALESREP_ID_AD_Reference_ID);
         //   System.out.println("Sales rep " +order.SALESREP_ID_AD_Reference_ID);
   //    order.setM_Warehouse_ID(m_M_Warehouse_ID);
         int warehouse=0;
//         System.out.println("profileorder.getAD_Org_ID()    "+profileorder.getAD_Org_ID());
//         System.out.println("m_AD_Org_ID    "+m_AD_Org_ID);
        try
        {
      StringBuffer sql = new StringBuffer("Select M_Warehouse_ID From M_Warehouse where AD_Client_ID=? and AD_Org_ID=1000006");
		//  reset table
		//  Execute

			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
                        pstmt.setInt(1,AD_Client_ID);
               //         pstmt.setInt(2, m_AD_Org_ID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next())
			{
                            warehouse= rs.getInt(1);
			}
			rs.close();
			pstmt.close();
        }
        catch(SQLException s)
        {
        }
//         System.out.println("warehose------------------ " +warehouse);
        order.setM_Warehouse_ID(warehouse);
        int listaprecios =0;
        try
        {
      StringBuffer sql = new StringBuffer("SELECT pl.M_PriceList_ID, BOM_PriceStd(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceStd, BOM_PriceList(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceList, BOM_PriceLimit(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceLimit,pv.ValidFrom,pl.C_Currency_ID, pv.M_PriceList_Version_ID FROM M_Product p INNER JOIN M_ProductPrice pp ON (p.M_Product_ID=pp.M_Product_ID) INNER JOIN  M_PriceList_Version pv ON (pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID) INNER JOIN M_Pricelist pl ON (pv.M_PriceList_ID=pl.M_PriceList_ID) WHERE pv.IsActive='Y' and pl.IsDefault='Y' AND pl.IsSOPriceList='Y' and pv.AD_Client_ID=? ORDER BY pv.ValidFrom");
		//  reset table
		//  Execute

			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next())
			{
                            listaprecios= rs.getInt(1);
			}
			rs.close();
			pstmt.close();
        }
        catch(SQLException s)
        {
        }
        order.setM_PriceList_ID(listaprecios);
        order.setDateOrdered(profileorder.getDateDoc());
        int locationid =0;
        try
        {
      StringBuffer sql = new StringBuffer("Select C_BPartner_Location_ID From C_BPartner_Location where AD_Client_ID=? and C_BPartner_ID=?");
		//  reset table
		//  Execute

			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, m_Client_ID);
                        pstmt.setInt(2, profileorder.getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next())
			{
                            locationid= rs.getInt(1);
			}
			rs.close();
			pstmt.close();
        }
        catch(SQLException s)
        {
        }
        order.setC_BPartner_Location_ID(locationid);

        order.setC_DocTypeTarget_ID(order.DocSubTypeSO_Proposal);
       // order.setC_DocType_ID(order.C_DOCTYPE_ID_AD_Reference_ID);

       if(order.save())
       {
           String salvado = new String("Cotizaci�n no. "+order.getDocumentNo());
           ADialog.info(m_WindowNo,this,salvado);
           profileorder.setC_Order_ID(order.getDocumentNo());
           profileorder.save();
       }
       else
       {
           ADialog.log(Level.SEVEREm_WindowNo,this,"No se pudo generar la Cotizaci�n");
       }

//           for (int i=0; i<miniTable2.getRowCount(); i++)
//           {
        MOrderLine orderline = new MOrderLine(order);

      //  MProfileBOMSelected profileselected = new MProfileBOMSelected(Env.getCtx(), m_MPC_ProfileBOM_ID);

        orderline.setM_Product_ID(profileorder.getM_Product_ID());
//          System.out.println("costo final --- " +jTextField1.getText().toString());
//      BigDecimal costobd =new BigDecimal(profileselected.getPriceList());
//      System.out.println("costo final --- " +costobd);
      BigDecimal kgbd =new BigDecimal(1000.0);
      //System.out.println("kg --- " +profileorder.getQty().doubleValue());
      BigDecimal costouni = new BigDecimal(profileorder.getLineNetAmt().doubleValue()/profileorder.getQty().doubleValue());
       //System.out.println("costo --- " +profileorder.getPriceList());
      //  System.out.println("costo bd --- " +profileorder.getQty());
        orderline.setQtyOrdered(profileorder.getQty());
        orderline.setQtyEntered(profileorder.getQty());
        orderline.setC_UOM_ID(profileorder.getC_UOM_ID());
        orderline.setPriceList(costouni.setScale(3,5));
        orderline.setPriceEntered(costouni.setScale(3,5));
        orderline.setPriceActual(costouni.setScale(3,5));
        //orderline.setDiscount(Env.ZERO);
        //BigDecimal totallinea = new BigDecimal(costouni.doubleValue()*profileorder.getQty().doubleValue());
        //orderline.setLineNetAmt(totallinea);

        orderline.setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());

        orderline.setC_Currency_ID(130);

        orderline.save();
        }
       //    }
                 //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
                */
                if (e.getSource() == jButton4)
                { int row = 0;
//	System.out.println("boton4     ");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        int rows = miniTable.getRowCount();

                for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rows);

                    if (id != null && id.isSelected())
                    {
                       Integer ln=id.getRecord_ID();
                       DB.executeUpdate("DELETE FROM MPC_ProfileBOMLine WHERE MPC_ProfileBOMLine_ID=" + ln.intValue());
                    }


                }
                executeQuery();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        if (e.getSource() == jButton46)
         {
                    int rows2s = 0;
//	System.out.println("boton46 borrar sel     ");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       rows2s = miniTable2.getRowCount();

                 for (int i = 0; i<rows2s; i++)
		{

                    IDColumn id = (IDColumn)miniTable2.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rows2s);

                    if (id != null && !id.isSelected())
                    {
                        Integer ln2s=id.getRecord_ID();
                        DB.executeUpdate("DELETE FROM MPC_ProfileBOM_Selected WHERE MPC_ProfileBOM_Selected_ID="+ ln2s.intValue());
                    }


                }
                    executeQuery3();
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }



              if (e.getSource() == jButton40)
                { int rows = 0;
                  int m_attr_id=0;
//	System.out.println("boton40     ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        rows = miniTable5.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable5.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rows);

                    if (id != null && id.isSelected())
                    {
            //            System.out.println("id" + id);
                       Integer ln=id.getRecord_ID();
              //           System.out.println("ln" + ln);
//         miniTable.editingStopped(new ChangeEvent(this));
               //System.out.println("vlookup de nutrientes" + nutrientes);
                //System.out.println("vlookup de nutrientes2" + nutrientes.toString());
               MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
            MMPCProfileBOMLine profilebomline = new MMPCProfileBOMLine(Env.getCtx(),0,null);
            //System.out.println("mpc_profileBom   ----" +m_MPC_ProfileBOM_ID);
            //System.out.println("profilebom.getMPC_ProfileBOM_ID()" +profilebom.getMPC_ProfileBOM_ID());
            profilebomline.setMPC_ProfileBOM_ID(profilebom.getMPC_ProfileBOM_ID());
                 //MAttribute um = new MAttribute(Env.getCtx(),rs.get);
                   //             KeyNamePair kum = new KeyNamePair(um.getC_UOM_ID(),um.getName());
           String attr = miniTable5.getValueAt(i,2).toString();
//           Integer attr1=new Integer(0);
//           attr1.valueOf(attr);
           try{
               StringBuffer sql1 = new StringBuffer("SELECT M_Attribute_ID FROM M_Attribute WHERE AD_Client_ID=? AND Name=?");
    		PreparedStatement pstmt = DB.prepareStatement(sql1.toString());
                        pstmt.setInt(1, AD_Client_ID);
			pstmt.setString(2, attr);

			ResultSet rs = pstmt.executeQuery();

			//
			while (rs.next())
			{
				m_attr_id = rs.getInt(1);

			}
			rs.close();
			pstmt.close();

           }
           catch(SQLException s)
           {
           }
         //  profilebomline.setLine();
          // System.out.println("attribute  --------" +m_attr_id);
           profilebomline.setM_Attribute_ID(m_attr_id);
            profilebomline.save();


                    }
                 }

        executeQuery();
          rows = 0;
           //       int m_attr_id=0;

        rows = miniTable5.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable5.getValueAt(i, 0);//  ID in column 0
                    id.setSelected(false);
                 }
         //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
              }

                   if (e.getSource() == jButton11)
                { int rowsp = 0;
//	System.out.println("boton11     ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         rowsp = miniTable4.getRowCount();

                 for (int i = 0; i < rowsp; i++)
		{

                    IDColumn id = (IDColumn)miniTable4.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rowsp);

                    if (id != null && !id.isSelected())
                    {
                       Integer ln=id.getRecord_ID();
                       DB.executeUpdate("DELETE FROM MPC_ProfileBOM_Product WHERE AD_Client_ID=" +AD_Client_ID +" AND MPC_ProfileBOM_Product_ID="+id.getRecord_ID().intValue());
                    }


                }
                formular();
          //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

                if (e.getSource() == jButton6)
                {
//	System.out.println("boton6      ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//            MProfileBOM profilebom = new MProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID);
//            MProfileBOMLine profilebomline = new MProfileBOMLine(profilebom);
//          //  profilebomline.setLine(Line);
//           System.out.println("dispaly de vlookup de nutrientes" + nutrientes.getDisplay());
//           System.out.println("dispaly de vlookup de nutrientes" + nutrientes.getValue());
//           profilebomline.setM_Attribute_ID(101);
//            profilebomline.save();
//            executeQuery();
         //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

                if (e.getSource() == jButton5)
                {
//	System.out.println("boton5      ");
        //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        miniTable.editingStopped(new ChangeEvent(this));
               //System.out.println("vlookup de nutrientes" + nutrientes);
                //System.out.println("vlookup de nutrientes2" + nutrientes.toString());
               MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
            MMPCProfileBOMLine profilebomline = new MMPCProfileBOMLine(Env.getCtx(),0,null);
          //  System.out.println("mpc_profileBom   ----" +m_MPC_ProfileBOM_ID);
           // System.out.println("profilebom.getMPC_ProfileBOM_ID()" +profilebom.getMPC_ProfileBOM_ID());
            profilebomline.setMPC_ProfileBOM_ID(profilebom.getMPC_ProfileBOM_ID());
           String attr = nutrientes.getValue().toString() == null ? "" : nutrientes.getValue().toString();
           Integer attr1=new Integer(0);
           attr1.valueOf(attr);
         //  profilebomline.setLine();
           profilebomline.setM_Attribute_ID(attr1.valueOf(attr).intValue());
            profilebomline.save();
             // nutrientes.refresh();
            //nutrientes.removeAll();
            executeQuery();
           //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

                if (e.getSource() == jButton10)
                {
//	System.out.println("Formulacion      ");
       // setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        int rows = miniTable4.getRowCount();
            // System.out.println("rows __________     "+rows);
               /*  for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable4.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rows);

                    if (id != null)
                    {
              //          System.out.println("id" + id);
                       Integer ln=id.getRecord_ID();
                //         System.out.println("ln" + ln);
//                        miniTable.editingStopped(new ChangeEvent(this));
//               System.out.println("minitable getvalue" + miniTable.getValueAt(i, 0));
//                //System.out.println("vlookup de nutrientes2" + nutrientes.toString());
             MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID);
             MMPCProfileBOMProduct profilebomproduct = new MMPCProfileBOMProduct(Env.getCtx(),ln.intValue());
            // System.out.println("minitable min -------" +miniTable4.getValueAt(i,4));
             String vacio ="";
             if (miniTable4.getValueAt(i,3)!=null && !miniTable4.getValueAt(i,3).toString().equals(vacio))
             {
             String mn = miniTable4.getValueAt(i,3).toString();

              // System.out.println("string min" +mn);

             Integer mni= new Integer(0);
            // System.out.println("integer min" + mni.valueOf(mn));
             BigDecimal mnb = new BigDecimal(mn);

            // System.out.println("big min" + mnb);
             profilebomproduct.setMinimum(mnb);
             }
             else
             {
                 profilebomproduct.setMinimum(cero);
             }

             if (miniTable4.getValueAt(i,4)!=null && !miniTable4.getValueAt(i,4).toString().equals(vacio))
             {
             String mx = miniTable4.getValueAt(i,4).toString();
            // System.out.println("string max" +mx);
             Integer mxi= new Integer(0);
            // System.out.println("integer max" + mxi.valueOf(mx));
             BigDecimal mxb = new BigDecimal(mx);
            // System.out.println("big max" + mxb);
             profilebomproduct.setMaximum(mxb);
             }
             else
             {
                 profilebomproduct.setMaximum(cero);
             }

                    profilebomproduct.save();
                    }
                 }*/
        parcial=false;
         formulacion(parcial);
         //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        // generateselected();
                }
	//}	//	actionPerformed
    }
   /**
	 * 	Query per Product Attribute.
	 *  <code>
	 * 	Available synonyms:
	 *		M_Product p
	 *		M_ProductPrice pr
	 *		M_AttributeSet pa
	 *	</code>
	 */
	private void cmd_InfoPAttribute()
	{
		//InfoPAttribute_1 ia = new InfoPAttribute_1(this);
		//m_pAttributeWhere = ia.getWhereClause();
		//if (m_pAttributeWhere != null)
		//	executeQuery();
	}	//	cmdInfoAttribute
        private AppsAction addAction (String action, JMenu menu, KeyStroke accelerator, boolean toggle)
	{
		AppsAction act = new AppsAction(action, accelerator, toggle);
		if (menu != null)
			menu.add(act.getMenuItem());
		act.setDelegate(this);
		return act;
	}	//	addAction



public void formulacion(boolean parcial)
        {
//            System.out.println("entro a formulacion");
            estan=0;
            cont500=0;
            int precios=0;
            //selectedarr.clear();
             vehiculoarr.clear();
              vehiculoarr2.clear();
            cont11=0;
            preciototal=Env.ZERO;
            sumakilos=Env.ZERO;
            m_finished=false;
            arregloing.clear();
            cant.clear();
            cantvar.clear();
            validacion.clear();
            ingselfinal.clear();
            nutarr.clear();
            nutarr1.clear();
            acalc.clear();
            aunico.clear();
            arringconnut.clear();
            Ingredients.pack();
       Ingredients.setBounds(451,200 , 400, 350);

            Selected.pack();
        Selected.setBounds(0,200 , 450, 350);

            mayor=Env.ZERO;
            menor=Env.ZERO;
            MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);


		if (m_PriceDate == null)
			m_PriceDate = new Timestamp (System.currentTimeMillis());



            try
            {
                /*StringBuffer borrars = new StringBuffer("DELETE FROM MPC_ProfileBOM_Selected WHERE MPC_ProfileBOM_ID=?");
                  PreparedStatement pstmtbs = DB.prepareStatement(borrars.toString());
			pstmtbs.setInt(1, m_MPC_ProfileBOM_ID);

			ResultSet rsbs = pstmtbs.executeQuery();
                        rsbs.close();
                        pstmtbs.close();

                StringBuffer borrarr = new StringBuffer("DELETE FROM MPC_ProfileBOM_Real WHERE MPC_ProfileBOM_ID=?");
                  PreparedStatement pstmtbr = DB.prepareStatement(borrarr.toString());
			pstmtbr.setInt(1, m_MPC_ProfileBOM_ID);

			ResultSet rsbr = pstmtbr.executeQuery();
                        rsbr.close();
                        pstmtbr.close();*/

                DB.executeUpdate("DELETE FROM MPC_ProfileBOM_Selected WHERE MPC_ProfileBOM_ID="+  m_MPC_ProfileBOM_ID);
                DB.executeUpdate("DELETE FROM MPC_ProfileBOM_Real WHERE MPC_ProfileBOM_ID="+m_MPC_ProfileBOM_ID);


                int rows=miniTable4.getRowCount();
                //int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
              // System.out.println("lineas de ing "+rows);
                int cont=0;
                int atrib=0;
                int numsel=0;
                int cont2=0;
                int cont1=0;
                for (int i = 0; i < rows; i++)
		{
		   m_calculated = false;
                    IDColumn id = (IDColumn)miniTable4.getValueAt(i, 0);//  ID in column 0
                  //  String prodlinea = (String)miniTable4.getValueAt(i,2);
               //     System.out.println("id  ----------"+prodlinea);
                    log.log(Level.INFO, "Row="+ rows);

                    if (id.isSelected())
                    {
                         Integer ln=id.getRecord_ID();
                          String valuest ="";
                                valuest = miniTable4.getValueAt(i,3).toString();
                                 //System.out.println("tamano de id   " +valuest.length());
                                    if (valuest.length()<11)
                                    {
//                                        System.out.println("ingrediente");
                                        String prodlinea = (String)miniTable4.getValueAt(i,4);
                          StringBuffer prodid = new StringBuffer("SELECT M_Product_ID FROM M_Product Where AD_Client_ID=? and Name=?");
                           PreparedStatement pstmtprodid = DB.prepareStatement(prodid.toString());
			pstmtprodid.setInt(1, AD_Client_ID);
                        pstmtprodid.setString(2, prodlinea);
			ResultSet rsprodid = pstmtprodid.executeQuery();

			//
			while (rsprodid.next())
			{
                          product_id = rsprodid.getInt(1);
//                          System.out.println("ingrediente id " +product_id);
                          numsel++;
                        }
                        rsprodid.close();
                        pstmtprodid.close();


                         //System.out.println("PRODUCTO ID   "+product_id);
                  StringBuffer ing=new StringBuffer("SELECT p.M_Product_ID,mai.M_Attribute_ID, mai.Value FROM M_Product p INNER JOIN M_AttributeSetInstance masi ON(masi.M_AttributeSetInstance_ID=p.M_AttributeSetInstance_ID) INNER JOIN M_AttributeInstance mai ON(mai.M_AttributeSetInstance_ID=p.M_AttributeSetInstance_ID and (mai.Value!='0E-12')) where p.AD_Client_ID=? and p.M_Product_ID=?");

         PreparedStatement pstmting = DB.prepareStatement(ing.toString());
			pstmting.setInt(1, AD_Client_ID);
                        pstmting.setInt(2, product_id);
			ResultSet rsing = pstmting.executeQuery();

			//
			while (rsing.next())
			{
                            String m_value = rsing.getString(3);
                             nuting = new BigDecimal(m_value);
              //               System.out.println("nuting   -----" +nuting);
                            if(nuting.doubleValue() >0.0)
                            {
                           int m_prod = rsing.getInt(1);
                           arregloing.add(cont, rsing.getString(1));

                           //System.out.println("arregloing   "+cont +arregloing.get(cont));
                           cont++;
                            int m_attribute = rsing.getInt(2);
                            arregloing.add(cont, rsing.getString(2));
                  //          System.out.println("attribute_id   "+m_attribute);
                             //System.out.println("arregloing   "+cont +arregloing.get(cont));
                            cont++;

                            arregloing.add(cont, rsing.getString(3));
                            cont++;
                            }

                    //        System.out.println("valor de atributo   "+m_value);
//                             System.out.println("arregloing   "+cont +arregloing.get(cont));
                        //    nuting = new BigDecimal(m_value);

                        }
                        rsing.close();
                        pstmting.close();
                                    }
                                    else
                                    {
                                        String prodlinea = (String)miniTable4.getValueAt(i,4);
                          StringBuffer prodid = new StringBuffer("SELECT MPC_ProfileBOM_ID FROM MPC_ProfileBOM WHERE AD_Client_ID=? and Name=?");
                           PreparedStatement pstmtprodid = DB.prepareStatement(prodid.toString());
			pstmtprodid.setInt(1, AD_Client_ID);
                        pstmtprodid.setString(2, prodlinea);
			ResultSet rsprodid = pstmtprodid.executeQuery();

			//
                            while (rsprodid.next())
                            {
                              product_id = rsprodid.getInt(1);
    //                          System.out.println("plan id " +product_id);

                              numsel++;
                            }
                        rsprodid.close();
                        pstmtprodid.close();

                         StringBuffer ing=new StringBuffer("SELECT p.MPC_ProfileBOM_ID,mai.M_Attribute_ID, mai.Value FROM MPC_ProfileBOM p INNER JOIN M_AttributeSetInstance masi ON(masi.M_AttributeSetInstance_ID=p.M_AttributeSetInstance_ID) INNER JOIN M_AttributeInstance mai ON(mai.M_AttributeSetInstance_ID=p.M_AttributeSetInstance_ID and (mai.Value!='0E-12')) WHERE p.AD_Client_ID=? and p.MPC_ProfileBOM_ID=?");

                        PreparedStatement pstmting = DB.prepareStatement(ing.toString());
			pstmting.setInt(1, AD_Client_ID);
                        pstmting.setInt(2, product_id);
			ResultSet rsing = pstmting.executeQuery();

			//
                                while (rsing.next())
                                {
                                    String m_value = rsing.getString(3);
                                     nuting = new BigDecimal(m_value);
                      //               System.out.println("nuting   -----" +nuting);
                                    if(nuting.doubleValue() >0.0)
                                    {
                                   int m_prod = rsing.getInt(1);
                                   arregloing.add(cont, rsing.getString(1).toString().concat("p"));

                                   //System.out.println("arregloing   "+cont +arregloing.get(cont));
                                   cont++;
                                    int m_attribute = rsing.getInt(2);
                                    arregloing.add(cont, rsing.getString(2));
                          //          System.out.println("attribute_id   "+m_attribute);
                                     //System.out.println("arregloing   "+cont +arregloing.get(cont));
                                    cont++;

                                    arregloing.add(cont, rsing.getString(3));
                                    cont++;
                                    }

                            //        System.out.println("valor de atributo   "+m_value);
                                     //System.out.println("arregloing   "+cont +arregloing.get(cont));
                                //    nuting = new BigDecimal(m_value);

                                }
                        rsing.close();
                        pstmting.close();
                                    }

                    }

                }

               // System.out.println("arreglo de ings y plans ------------- " +arregloing.toString());
                int numnuting=arregloing.size();
                String numst="";
                numst=numst.valueOf(numnuting);
                BigDecimal numbd = new BigDecimal(numst);
                BigDecimal numtot = new BigDecimal(numbd.doubleValue()/3);

              //  System.out.println("total de nut que cubren   " +numtot);


                StringBuffer sqlnut= new StringBuffer("SELECT M_Attribute_ID, Minimum, Maximum FROM MPC_ProfileBOMLine WHERE AD_Client_ID=? AND MPC_ProfileBOM_ID=? ORDER BY M_Attribute_ID");
         PreparedStatement pstmtnut = DB.prepareStatement(sqlnut.toString());
			pstmtnut.setInt(1, AD_Client_ID);
                        pstmtnut.setInt(2, m_MPC_ProfileBOM_ID);
			ResultSet rsnut = pstmtnut.executeQuery();

			//
                        BigDecimal milnut = new BigDecimal(1000);
			while (rsnut.next())
			{
                            if (rsnut.getBigDecimal(2)==null)
                               minimo=Env.ZERO;
                           else
                           minimo = rsnut.getBigDecimal(2);
                           nutriente_id = rsnut.getString(1);
                           if (rsnut.getBigDecimal(3)==null)
                               maximo=Env.ZERO;
                           else
                           maximo = rsnut.getBigDecimal(3);
                           nutarr.add(cont1,nutriente_id);
//                           System.out.println("nut que mete --" +nutriente_id);
                           cont1++;
                           nutarr.add(cont1,minimo.multiply(milnut));
                           cont1++;
                            nutarr.add(cont1,maximo.multiply(milnut));
                           cont1++;
                           //esta = "N";
                //           System.out.println("nutriente     ?   "+nutriente_id);
//                          System.out.println("tamano arregloing size " +arregloing.size());
                            for (int j=1; j<arregloing.size(); j=j+3)
                            {
                                int contcant=0;

//                                System.out.println("nutriente: "+arregloing.get(j) +" ingrediente: " +arregloing.get(j-1));

                                esta=arregloing.get(j+1).toString();
                  //              System.out.println("esta        "+esta);
                                BigDecimal estabd = new BigDecimal(esta);
                                if (arregloing.get(j).toString().matches(nutriente_id))
                                {
                                  //  esta = "Y";
                    //                System.out.println("minimo        "+minimo);
                                    BigDecimal ckg=new BigDecimal(minimo.doubleValue()/estabd.doubleValue());
                                    cant.add(contcant,arregloing.get(j-1).toString());
                                 //   System.out.println("Producto en arreglo  " +cant.get(contcant));
                                    contcant++;
                                    cant.add(contcant,nutriente_id);

                               //System.out.println("Nutriente del producto    " +cant.get(contcant));
                                      contcant++;
                                      cant.add(contcant,ckg);
                     //                System.out.println("kilos del producto    " +cant.get(contcant));
                                    contcant++;
//                                    if (cont2<numsel)
//                                    {
//                                BigDecimal preciobd = new BigDecimal(preciostd.get(cont2).toString());
//
//                                  System.out.println("cont2   " +cont2);
                   //                    System.out.println("prod   " +arregloing.get(j-1).toString());
//                                     String valuest ="";
//                                valuest = miniTable4.getValueAt(i,1).toString();
                                    if (!arregloing.get(j-1).toString().endsWith("p"))
                                    {

                        StringBuffer sqlpro= new StringBuffer("SELECT Value FROM M_Product WHERE AD_Client_ID=? AND M_Product_ID=? ");
                        PreparedStatement pstmtpro = DB.prepareStatement(sqlpro.toString());
			pstmtpro.setInt(1, AD_Client_ID);
                        pstmtpro.setString(2, arregloing.get(j-1).toString());
			ResultSet rspro = pstmtpro.executeQuery();
                        String searchkey ="";
                        BigDecimal costoplan1 =Env.ZERO;
                                   if (rspro.next())
                                   {
                                       searchkey = rspro.getString(1);
                                     //  System.out.println("Valor del producto" +searchkey);
                                   }
                        rspro.close();
                        pstmtpro.close();
                                        if (searchkey.startsWith("P"))
                                        {
                                         //   System.out.println("producto ---------------  ");
//                                         sql10= new StringBuffer("");
//                                        if (prof.getQty().compareTo(Env.ZERO)>0)
//                                        {
//                                        sql10= new StringBuffer("SELECT Trunc(mp.PriceList/mp.Qty,2) from MPC_ProfileBOM mp  where mp.AD_Client_ID=1000000 and mp.Value like '");
//                                        sql10.append(searchkey +"%'");
//                                        }
//                                        else
//                                        {
                                           // StringBuffer sql10= new StringBuffer("SELECT Trunc(mp.PriceList/1000,2) from MPC_ProfileBOM mp  where mp.AD_Client_ID=1000000 and mp.Value like '");
                                           StringBuffer sql10= new StringBuffer("SELECT mp.PriceList FROM MPC_ProfileBOM mp  WHERE mp.AD_Client_ID=? AND mp.Value LIKE '");
                                        sql10.append(searchkey +"%'");
//                                        }
                                        PreparedStatement pstmt10 = DB.prepareStatement(sql10.toString());
			                pstmt10.setInt(1, AD_Client_ID);
                                        ResultSet rs10 = pstmt10.executeQuery();

                                        if (rs10.next())
                                        {
                                            costoplan1 = rs10.getBigDecimal(1);
                                            cant.add(contcant,costoplan1.multiply(ckg));
               //                           System.out.println("precio del producto   " +cant.get(contcant));
                                            contcant++;
                                         //   System.out.println("costo plan ---------------  " +costoplan1);
                                        }
                                        rs10.close();
                                        pstmt10.close();
                                        }
                                        else
                                        {
                                            BigDecimal mil = new BigDecimal(1000);
                                            Integer m_product_id =  new Integer(arregloing.get(j-1).toString());
                                            preciodeing(m_product_id.intValue());
                                       //   System.out.println("precio   " +m_PriceStd);
                                            // lo pultiple por mil para lo del redondeo
                                            BigDecimal costoing= m_PriceStd.multiply(ckg); //.multiply(mil);
                                            cant.add(contcant,costoing);
               //                           System.out.println("precio del producto   " +cant.get(contcant));
                                            contcant++;
                                        }
                                    }
                                    else
                                    {
//                                        System.out.println("planteamiento");
//                                      System.out.println("planteamiento2 ----------- -------------" +arregloing.get(j-1).toString());
                                     //StringBuffer sql1= new StringBuffer("SELECT Trunc(mp.PriceList/1000,2) from MPC_ProfileBOM mp  where mp.AD_Client_ID=1000000 and mp.MPC_ProfileBOM_ID=?");
                                        StringBuffer sql1= new StringBuffer("SELECT mp.PriceList FROM MPC_ProfileBOM mp  WHERE mp.MPC_ProfileBOM_ID=?");
                                        PreparedStatement pstmt1 = DB.prepareStatement(sql1.toString());
			//pstmt.setInt(1, AD_Client_ID);
                                         int ultimo=0;
                                         ultimo = arregloing.get(j-1).toString().length()-1;
//                                         System.out.println("ultimo menos uno  ------"+ultimo);
                                        Integer mpc_id = new Integer(arregloing.get(j-1).toString().substring(0,ultimo));
//                                         System.out.println("planteamiento -------------" +arregloing.get(j-1).toString().substring(0,ultimo));

                        pstmt1.setInt(1, mpc_id.intValue());
			ResultSet rs1 = pstmt1.executeQuery();
                        BigDecimal costoplan = Env.ZERO;
                        if (rs1.next())
			{
                            costoplan = rs1.getBigDecimal(1);
//                               System.out.println("costo plan   " +costoplan);
                        }
                        rs1.close();
                        pstmt1.close();
                        BigDecimal costoing= costoplan.multiply(ckg);
                                    cant.add(contcant,costoing);
//                                   System.out.println("precio del producto   " +costoing);
                                    contcant++;
                                    }


                                    //System.out.println("cantidad en kilos  " +ckg);
                            //        cont2++;

                                    estan++;

                                }


                             }
//                           if (esta=="Y")
//                               estan++;






                      //     System.out.println("minimo de nut   "+minimo);
                           atrib++;
                        }

                        rsnut.close();
                        pstmtnut.close();

                      //  System.out.println("arreglo de ings y plans cant ------------- " +cant.toString());

           //                System.out.println("numero de nut en ing     " +estan);
             //              System.out.println("numero de nut req     " +atrib);
        //         if ((atrib>numtot.intValue()) || (atrib>estan))
         //            System.out.println("NO cumple con todos lo requerimientos");

       int cont3=0;
       //System.out.println("numero de elementos en el arreglo   " +cant.size());
       int cont4=0;
       int cont5=0;
       int cont6=0;
       int cont7=0;
       int rcont=0;
           String c3 = new String();
           BigDecimal uno = Env.ONE;
           // validar si solo un ing es seleccionado y ver si el cumple con los requerimientos
           //System.out.println("numsel " +numsel);
            if (numsel==1)
                         {


                             if(validateingcubre(productst.valueOf(product_id)))
                             {
                                 nutmaspeso(productst.valueOf(product_id));
                      //           System.out.println("kilos" +nutkgbd);
                                 for (int arr2=0;arr2<cant.size();arr2=arr2+4)
                                 {
               //                      System.out.println("prod arr" +cant.get(arr2));
                 //                    System.out.println("prod de formula" +productst.valueOf(product_id));
                   //                  System.out.println("kilos arr" +cant.get(arr2+2));
                     //                System.out.println("prod kilos de nut mayor" +nutkgbd);
                               //   if (cant.get(arr2).toString().matches(productst.valueOf(product_id)) && cant.get(arr2+2).toString().matches(nutkgbd.toString()))
                                   preciodeing(product_id);
                                   BigDecimal mil = new BigDecimal(1000);
                                   nutprbd=m_PriceStd.multiply(nutkgbd).multiply(mil).divide(uno,3,BigDecimal.ROUND_HALF_UP);
//                                     if (cant.get(arr2).toString().matches(productst.valueOf(product_id)))
//                                     {
//                                   String nutpr = new String(cant.get(arr2+3).toString());
//                                   nutprbd = new BigDecimal(nutpr);
                        //           System.out.println("precio" +nutprbd);
                                   m_finished=true;
                                   ingselfinal.add(cont11,cant.get(arr2));
             //                System.out.println("ing " +ingselfinal.get(cont11));
                             cont11++;

                               ingselfinal.add(cont11,cant.get(arr2+2));
           //                    System.out.println("kg " +ingselfinal.get(cont11));
                             cont11++;
                               ingselfinal.add(cont11,cant.get(arr2+3));
         //                      System.out.println("precio " +ingselfinal.get(cont11));
                             cont11++;
//                                  }
                                 }
                             }

                         }




   //
  //comparar nutrientes y ver cuantos de un nutriente hay en los diferentes ingredientes
  //
           for (int p=1; p<cant.size(); p=p+4)
      {
          // String n1 = new String(cant.get(p).toString());
           for (int n=1; n<cant.size(); n=n+4)
           {    //System.out.println("nut1   " +cant.get(p).toString());
                 //System.out.println("nut2   " +cant.get(n).toString());
                 //comparar nutrientes y ver cuantos de un nutriente hay en los diferentes ingredientes
                if (cant.get(p).toString().matches(cant.get(n).toString()))
                {
                   cont3++;
                }

           }
    //
      // valor 0 del arreglo validacion es el numero de nutrientes que hay y el valor 1 es el nutriente
      //
           c3=c3.valueOf(cont3);
                 validacion.add(cont4,c3);
                 cont4++;
//                 System.out.println("nut  " +c3);
                 validacion.add(cont4, cant.get(p));
                 cont4++;
                 cont3=0;
            //     p=p+4;

      }

        String ln = new String("\n");
        StringBuffer buff = new StringBuffer();
        buff.append("Problem");
        buff.append(ln);
        buff.append("nombre");
        buff.append(ln);
        buff.append("Minimize");
        buff.append(ln);
        buff.append("obj: ");

      //  ingq = arregloing.get(0).toString();
        int cont21=0;
          m_exists2 = false;
       cantvar2.clear();
       cantvar2.add("obj: ");
       cont21++;

//        System.out.println("tamano cant arr   " +cant.size());
          for (int lp1=cant.size()-1; lp1>0;lp1=lp1-4)
           {
//               System.out.println("valores de arreglo cant " +cant.get(lp1-3));
               if (!yaexiste2(cant.get(lp1-3).toString()))
               {
                   if (!cant.get(lp1-3).toString().endsWith("p"))
                   {

                            StringBuffer sqlpro1= new StringBuffer("SELECT Value FROM M_Product WHERE AD_Client_ID=? AND M_Product_ID=? ");
         PreparedStatement pstmtpro1 = DB.prepareStatement(sqlpro1.toString());
			pstmtpro1.setInt(1, AD_Client_ID);
                        pstmtpro1.setString(2, cant.get(lp1-3).toString());
			ResultSet rspro1 = pstmtpro1.executeQuery();
                        String searchkey ="";
                        BigDecimal costoplan1 =Env.ZERO;
                                   if (rspro1.next())
                                   {
                                       searchkey = rspro1.getString(1);
                                      // System.out.println("Valor del producto" +searchkey);
                                   }
                         rspro1.close();
                         pstmtpro1.close();
                                        if (searchkey.startsWith("P"))
                                        {
                                            //System.out.println("producto ---------------  ");
//                                         sql10= new StringBuffer("");
//                                        if (prof.getQty().compareTo(Env.ZERO)>0)
//                                        {
//                                        sql10= new StringBuffer("SELECT Trunc(mp.PriceList/mp.Qty,2) from MPC_ProfileBOM mp  where mp.AD_Client_ID=1000000 and mp.Value like '");
//                                        sql10.append(searchkey +"%'");
//                                        }
//                                        else
//                                        {
                                            StringBuffer sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList FROM MPC_ProfileBOM mp  WHERE mp.IsPublished='Y' AND mp.Value like '");
                                        sql10.append(searchkey +"%'").append("Order by mp.Value");
//                                           StringBuffer sql10= new StringBuffer("SELECT Trunc(mp.PriceList/1000,2) from MPC_ProfileBOM mp  where mp.AD_Client_ID=1000000 and mp.Value like '");
//                                        sql10.append(searchkey +"%'");
//                                        }
                                        PreparedStatement pstmt10 = DB.prepareStatement(sql10.toString());
			                //pstmt10.setInt(1, product_id);
                                        ResultSet rs10 = pstmt10.executeQuery();

                                        if (rs10.next())
                                        {
                                            costoplan1 = rs10.getBigDecimal(1);
                                            String preciopl = "";
                                             preciopl =preciopl.valueOf(costoplan1);

                                             buff.append(" + " +preciopl  +"(" +cant.get(lp1-3).toString() +")");
                                             cantvar2.add(cont21,cant.get(lp1-3).toString());
                                             cont21++;
                                           // System.out.println("costo plan ---------------  " +costoplan1);
                                        }
                                        rs10.close();
                                        pstmt10.close();
                                        }
                                        else
                                        {

                                             Integer m_product_id = new Integer(cant.get(lp1-3).toString());
                                             preciodeing( m_product_id.intValue());
                                             String preciolp = "";
                                             preciolp =preciolp.valueOf(m_PriceStd.divide(uno,3,BigDecimal.ROUND_HALF_UP));

                                             buff.append(" + " +preciolp  +"(" +cant.get(lp1-3).toString() +")");
                                             cantvar2.add(cont21,cant.get(lp1-3).toString());
                                             cont21++;
                                        }
                   }
                   else
                   {
                       StringBuffer sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  where mp.AD_Client_ID=? and mp.MPC_ProfileBOM_ID=?");
                                        PreparedStatement pstmt10 = DB.prepareStatement(sql10.toString());

                                         int ultimo=0;
                                         ultimo = cant.get(lp1-3).toString().length()-1;
//                                         System.out.println("ultimo menos uno  ------"+ultimo);
                                        Integer mpc_id1 = new Integer(cant.get(lp1-3).toString().substring(0,ultimo));
//                                         System.out.println("planteamiento -------------" +cant.get(lp1-3).toString().substring(0,ultimo));
                        pstmt10.setInt(1, AD_Client_ID);
                        pstmt10.setInt(2, mpc_id1.intValue());
			ResultSet rs10 = pstmt10.executeQuery();
                        BigDecimal costoplan1 = Env.ZERO;
                        if (rs10.next())
			{
                            costoplan1 = rs10.getBigDecimal(1);
//                               System.out.println("costo plan   " +costoplan1);
                        }
                        rs10.close();
                        pstmt10.close();
                        buff.append(" + " +costoplan1.divide(uno,3,BigDecimal.ROUND_HALF_UP)  +"(" +cant.get(lp1-3).toString() +")");
                         cantvar2.add(cont21,cant.get(lp1-3).toString());
                         cont21++;
                   }

               }
                m_exists2 = false;
          }


                         int c19=0;
                       int lin=miniTable4.getRowCount();
                       vehiculoarr.clear();
                       for (int i=0;i<lin;i++)
                       {
                             IDColumn id = (IDColumn)miniTable4.getValueAt(i, 0);//  ID in column 0
                             m_vehiculo=true;
                             log.log(Level.INFO, "Row="+ rows);

                        if (id != null && id.isSelected())
                        {
                       // System.out.println("prod " +miniTable4.getValueAt(i,1).toString());

                            for (int a=0;a<cant.size();a=a+4)
                            {
                                if (!cant.get(a).toString().endsWith("p"))
                                {
                                     Integer m_product_id = new Integer(cant.get(a).toString());
                                     MProduct prod = new MProduct(Env.getCtx(),m_product_id.intValue(),null);
                                     KeyNamePair kprod = new KeyNamePair(prod.getM_Product_ID(),prod.getName());

                                     if (kprod.toString().matches(miniTable4.getValueAt(i,3).toString()))
                                     {
                                          m_vehiculo=false;
                                     }
                                }
                                else
                                {
                                ;
                                }
                             }
                        if (m_vehiculo)
                        {
                         vehiculoarr.add(c19,miniTable4.getValueAt(i,3).toString());
                         c19++;
                        }
                        }
                       }

       int c18=0;
       BigDecimal milbd = new BigDecimal(1000);
       //vehiculoarr.clear();
       //System.out.println("vehiculoSIZE   -------" +vehiculoarr.size());
       //int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
       for (int vh=0;vh<vehiculoarr.size(); vh++)
       {
           StringBuffer pr1 = new StringBuffer("SELECT p.M_Product_ID From M_Product p Where p.AD_Client_ID=? and p.Value=?");
            PreparedStatement pstmtpr = DB.prepareStatement(pr1.toString());
			pstmtpr.setInt(1, AD_Client_ID);
//                        System.out.println("VEHICULOOOOOOOOO " +vehiculoarr.get(vh).toString());
                        pstmtpr.setString(2, vehiculoarr.get(vh).toString());
			ResultSet rspr = pstmtpr.executeQuery();

			//
			while (rspr.next())
			{
                         prod_id = rspr.getInt(1);
                         //pbaing = pbaing.valueOf(prod_id);
                         preciodeing(prod_id);
       String preciolp1 = "";
	   //    preciolp1 =preciolp1.valueOf(m_PriceStd.multiply(milbd).divide(uno,3,BigDecimal.ROUND_HALF_UP));
           preciolp1 =preciolp1.valueOf(m_PriceStd.divide(uno,3,BigDecimal.ROUND_HALF_UP));
                         vehiculoarr2.add(c18,pbaing);
                         c18++;
                         vehiculoarr2.add(c18,preciolp1);
                         c18++;
                        }
                         rspr.close();
                        pstmtpr.close();

       }
       m_exists2=false;

    //  vehiculoarr.clear();
       for (int vh1=0;vh1<vehiculoarr2.size();vh1=vh1+2)
       {
     //  System.out.println("--------------------------ENTRA VEHICULO ---------------------- ");
         if (!yaexiste2(vehiculoarr2.get(vh1).toString()))
               {
       buff.append(" + " +vehiculoarr2.get(vh1+1) +"(" +vehiculoarr2.get(vh1)+")");
         }
       m_exists2=false;
       }

       int cont20=0;
        StringBuffer cantbuff = new StringBuffer("");

         m_exists = false;

        cantvar1.clear();
        for (int n=0; n<nutarr.size(); n=n+3)
        {


          requerimientosmin(nutarr.get(n).toString());
          if (nutbd.doubleValue()>0.0)
          {
              cantvar.clear();
         if (n!=0)
             cantbuff.append(",");
         cantvar.add("n"+nutarr.get(n).toString());
         cantbuff.append("n"+nutarr.get(n).toString()+": ");
         m_exists = false;
         cont20++;
        for (int c=cant.size()-1;c>0;c=c-4)

        {
            if (cant.get(c-2).equals(nutarr.get(n)))
            {
                if (!yaexiste(cant.get(c-3).toString()))
                {

             for (int a=0;a<arregloing.size();a=a+3)
             {
                 if (arregloing.get(a).equals(cant.get(c-3)) && arregloing.get(a+1).equals(nutarr.get(n)))
                 {

                       cantbuff.append(" + ");
                     cantvar.add(arregloing.get(a+2));
                     cantbuff.append(","+arregloing.get(a+2).toString());
                     cont20++;
                 }
             }
            cantvar.add(cant.get(c-3));
             cantbuff.append(","+"("+cant.get(c-3).toString()+")");
            cont20++;
                }
               //m_exists = false;
            }
        }



          cantbuff.append(" >= ");

                cantvar.add(nutbd.toString());
                cont20++;
                cantbuff.append("," +nutbd.toString());
                cantbuff.append("\n");

           cantvar1=cantvar;
            m_exists = false;
          }

          requerimientosmax(nutarr.get(n).toString());
          if (nutbdx.doubleValue()>0.0)
          {
               cantvar.clear();
         if (n!=0)
             cantbuff.append(",");
         cantvar.add("nx"+nutarr.get(n).toString());
         cantbuff.append("nx"+nutarr.get(n).toString()+": ");
         m_exists = false;
         cont20++;
        for (int c=cant.size()-1;c>0;c=c-4)

        {
            if (cant.get(c-2).equals(nutarr.get(n)))
            {
                if (!yaexiste(cant.get(c-3).toString()))
                {

             for (int a=0;a<arregloing.size();a=a+3)
             {
                 if (arregloing.get(a).equals(cant.get(c-3)) && arregloing.get(a+1).equals(nutarr.get(n)))
                 {

                       cantbuff.append(" + ");
                     cantvar.add(arregloing.get(a+2));
                     cantbuff.append(","+arregloing.get(a+2).toString());
                     cont20++;
                 }
             }
            cantvar.add(cant.get(c-3));
             cantbuff.append(","+"("+cant.get(c-3).toString()+")");
            cont20++;
                }
               //m_exists = false;
            }
        }



          cantbuff.append(" <= ");

                cantvar.add(nutbdx.divide(uno,3,BigDecimal.ROUND_HALF_UP).toString());
                cont20++;
                cantbuff.append("," +nutbdx.divide(uno,3,BigDecimal.ROUND_HALF_UP).toString());
                cantbuff.append("\n");
          }



           cantvar1=cantvar;
            m_exists = false;
        }
     //  System.out.println("nuevo arreglo cantvar1 "+cantvar1.toString());
     //   System.out.println("nuevo buffer buffer denuts   "+cantbuff.toString());
          String[] valores = parse(cantbuff.toString(),FORMATTYPE_COMMA,cont20);

        buff.append(ln);
        buff.append("Subject To");
        buff.append(ln);
          int vcant = (numsel*2)+2;
for (int x=0; x<valores.length;x++)
{
       //   System.out.println("valores " +valores[x]);
          buff.append(valores[x]);

}
//        int c7=0;
//       System.out.println("size nutarr "+nutarr.size());
//      for (int nutrq=0;nutrq<nutarr.size();nutrq=nutrq+2)
//      { System.out.println("nur arr "+nutarr.get(nutrq));
//        c7=0;
//        for (int lpc=(cant.size()-1)/numsel; lpc>0; lpc=lpc-4)
//        {
//
//            Integer ing2 = new Integer(cant.get(lpc-3).toString());
//               MProduct prod2 = new MProduct(Env.getCtx(),ing2.intValue());
//               KeyNamePair kprod2 = new KeyNamePair(prod2.getM_Product_ID(),prod2.getName());
//               ingconnut(cant.get(lpc-3).toString(),nutarr.get(nutrq).toString());
//               if (numsel==1 && cant.get(lpc-2).equals(nutarr.get(nutrq)))
//               {
//                   buff.append("n"+nutrq +": " +arringconnut.get(1).toString() +"(" +cant.get(lpc-3).toString() +")" +" >= " + nutarr.get(nutrq+1) +ln);
//               }
//               else
//               {
//               if (c7==0)
//               {
//               buff.append("n"+nutrq +": " +arringconnut.get(1).toString() +"(" +cant.get(lpc-3).toString() +")");
//               c7++;
//               }
//               else
//               {
//                   requerimientos(nutarr.get(nutrq).toString());
//                   int lpct = lpc -4;
//
//               if (lpct<=0)
//               buff.append(" + " +arringconnut.get(1).toString() +"(" +cant.get(lpc-3).toString() +")" +" >= " +nutbd.toString() +ln);
//               else
//               {
//
//               buff.append(" + " +arringconnut.get(1).toString()  +"(" +cant.get(lpc-3).toString() +")");
//
//               }
//               }
//            }
//
//           }
//      }
         // System.out.println("vehiculoarr  SIZE  --------" +vehiculoarr.size());
          if (!parcial)
          {
              buff.append("n: ");
              for (int cv=1; cv<cantvar2.size();cv++)
              {
               buff.append(" + " +"(" +cantvar2.get(cv) +")");
              }

             for (int vh1=0;vh1<vehiculoarr2.size();vh1=vh1+2)
       {
        if (!yaexiste2(vehiculoarr2.get(vh1).toString()))
               {
       buff.append(" + " +"(" +vehiculoarr2.get(vh1)+")");

        }
        m_exists2=false;
        }
            buff.append(" = 1000");
          }
          buff.append(ln);
//            }
          // cambios ultima hora
          minmaxing.clear();
          StringBuffer profileing = new StringBuffer("Select M_Product_ID, Minimum, Maximum, Planteamiento From MPC_ProfileBOM_Product Where IsActive='Y' and AD_Client_ID=? and MPC_ProfileBOM_ID=?");
          PreparedStatement pstmti = DB.prepareStatement(profileing.toString());
			pstmti.setInt(1, AD_Client_ID);
                       // System.out.println("VEHICULOOOOOOOOO " +vehiculoarr.get(vh).toString());
                        pstmti.setInt(2, m_MPC_ProfileBOM_ID);
			ResultSet rsi = pstmti.executeQuery();
                        int c25=0;

			//
			while (rsi.next())
			{
                         ing_id = rsi.getInt(1);
                         pbaing = pbaing.valueOf(ing_id);
                         min_id = rsi.getBigDecimal(2);
                         max_id = rsi.getBigDecimal(3);
                         plant_id = rsi.getInt(4);
                         pbaplan = pbaplan.valueOf(plant_id);
                         if (min_id!=null)
                         {
                         if (min_id.doubleValue()!=0.0)
                         {
                              if(ing_id!=0)
                             {
                                 minmaxing.add(c25,pbaing);
                                 c25++;
                             }
                             else
                             {
                                 minmaxing.add(c25,pbaplan.concat("p"));
                                 c25++;
                             }
                          minmaxing.add(c25,"m");
                          c25++;
                          BigDecimal mil = new BigDecimal(1000);
                          BigDecimal minc = min_id.divide(uno,3,BigDecimal.ROUND_HALF_UP);
                          minmaxing.add(c25,minc);
                          c25++;

                         }
                         }
                          if (max_id!=null)
                         {
                          if (max_id.doubleValue()!=0.0)
                         {
                             if(ing_id!=0)
                             {
                                minmaxing.add(c25,pbaing);
                                c25++;
                             }
                             else
                             {
                                 minmaxing.add(c25,pbaplan.concat("p"));
                                 c25++;
                             }
                          minmaxing.add(c25,"x");
                          c25++;
                          BigDecimal mil = new BigDecimal(1000);
                          BigDecimal maxc = max_id.divide(uno,3,BigDecimal.ROUND_HALF_UP);
                          minmaxing.add(c25,maxc);
                          c25++;

                         }
                          }
                        }
                         rsi.close();
                        pstmti.close();


            for (int mxi=0; mxi<minmaxing.size(); mxi=mxi+3)
              {   String mst = "";
                  mst = mst.valueOf(mxi);
                  buff.append("n" +mst +": ");
                  buff.append("+ (" +minmaxing.get(mxi).toString() +")");
                  if (minmaxing.get(mxi+1).toString().matches("m"))
                  {
                      buff.append(" >= " + minmaxing.get(mxi+2).toString() +ln);
                  }
                   if (minmaxing.get(mxi+1).toString().matches("x"))
                  {
                      buff.append(" <= " + minmaxing.get(mxi+2).toString() +ln);
                  }
              }
            // cambios de ultima hora fin

             buff.append("End");
             System.out.println("Buffer" +ln +buff.toString());
             JFileChooser fc = new JFileChooser();
             Random random = new Random();
             Integer number = new Integer(random.nextInt());
             String file = "calculate" + number.toString() + ".tmp";
             fc.setSelectedFile(new java.io.File(file));
             fc.getCurrentDirectory();
             if (fc.getSelectedFile().exists())
             {
                fc.getSelectedFile().delete();
             }

             FileWriter fw = new FileWriter(fc.getSelectedFile());
             fw.write(buff.toString());
             fw.flush();
             fw.close();
            //   System.out.println("directorio " +fc.getCurrentDirectory());
            log.log(Level.FINE,"Temporary File:" + file);
            String [] problems = new String[] {"-X", file};
            org.eevolution.form.simplesolver solver = new simplesolver(problems);
            //int fieldNo = (numsel*2)+2;
            //System.out.println("num prods sel" +numsel +"field no " +fieldNo);
            //System.out.println(solver.solveProblem().toString());
            //String[] values = parse(solver.solveProblem().toString(),FORMATTYPE_COMMA,fieldNo);
            //log.log(Level.FINE,"Array Calculate" + values);

            /*
            for (int v=0; v < values.length; v ++)
            {
                    System.out.println("Dato Num:"+v + "Key:"  +values[v].toString()) ;// + "kilos" +  values[v+1].toString());
            }*/

            solution.clear();
            solution = Solution.factory(solver.solveProblem().toString());
            ingseleccionados(solution);
            oportunidad(solution);

            }
              catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery", e);
                        m_calculated = false;
		}
           catch (QSException e) {
         System.err.println(e.toString());

         if (e.rval==1)
         {
             if(ADialog.ask(m_WindowNo,this,"El problema no tiene solucion. �Deseas probar una soluci�n parcial?"))
             {   parcial=true;
                 formulacion(parcial);
             }
         }
         else
             ADialog.info(m_WindowNo,this,"Formulation");

      }
           catch (IOException e) {
         System.err.println("No pude leer el archivo ");

         System.err.println(e);
      }
        }


       public void oportunidad(Vector s)
       {
         // ingredientes no seleccionados para precio de oportunidad
                int vcont=0;

                /*for (int v=0; v<s.size(); v++)
                {
                        Solution i = (Solution)s.get(v);
                        selectedarr.add(v,m_oport_id);
                }*/

               /* for (int v=0; v<values.length; v=v+2)
                {

                        String tv = values[v+1].toString();
                        //log.log.(Level.FINE,"Values" + tv);
                        //BigDecimal valcero = new BigDecimal(values[v+1].toString());
                        BigDecimal valcero = new BigDecimal(values[v].toString());
                        String m_oport_id="";
                        if (valcero.doubleValue()==0.0)
                        {
                            for (int tv1=0; tv1<tv.length(); tv1++)
                            {

                                    m_oport_id =tv.substring(1,tv.length()-1);


                            }
                            selectedarr.add(vcont,m_oport_id);
//                                  System.out.println("resultados ----- " +values[v].toString());
                            vcont++;
                        }
                }*/


                String instance = "";
                String att = "";
                BigDecimal val=Env.ZERO;
                /*for (int f=0;f<selectedarr.size();f++)
                {*/
                for (int f=0;f<solution.size();f++)
                {
                    Solution i = (Solution)solution.get(f);

                    if(i.getKey().equals(""))
                        continue;

                    Integer m_product_id = new Integer(i.getKey());
                    preciodeing( m_product_id.intValue());
                    //System.out.println("ing ----- " +selectedarr.get(f).toString() +" precio "+m_PriceStd);

                       try
                            {
                                 StringBuffer sql2 = new StringBuffer("SELECT M_AttributeSetInstance_ID FROM M_Product WHERE AD_Client_ID=? AND M_Product_ID=?");
                                 PreparedStatement pstmt1 = DB.prepareStatement(sql2.toString());
                                 pstmt1.setInt(1, AD_Client_ID);
                                 pstmt1.setString(2, i.getKey());
                                 ResultSet rs1 = pstmt1.executeQuery();

                            if (rs1.next())
                            {
				instance = rs1.getString(1);
//                                System.out.println("att    "+att_id);

                            }
                            rs1.close();
                            pstmt1.close();
                            for (int a=0;a<nutarr.size();a=a+2)
                            {
                            StringBuffer sql = new StringBuffer("SELECT Value, M_Attribute_ID FROM M_AttributeInstance WHERE AD_Client_ID=? AND M_AttributeSetInstance_ID=? AND M_Attribute_ID=?");
                                 PreparedStatement pstmt = DB.prepareStatement(sql.toString());
                                 pstmt.setInt(1, AD_Client_ID);
                                 pstmt.setString(2, instance);
                                 pstmt.setString(3, nutarr.get(a).toString());
                                 ResultSet rs = pstmt.executeQuery();

                            while (rs.next())
                            {
                                if (rs.getBigDecimal(1).doubleValue()>0.0)
                                {

                                    att = rs.getString(2);
                                     System.out.println("att    "+att);
                                     val =rs.getBigDecimal(1);
                                     System.out.println("val    "+val);
                                }
//

                            }
                            rs.close();
                            pstmt.close();
                            }

                       }
                            catch (SQLException e)
                            {
                                log.log(Level.INFO,"Error SQL:", e);
                            }

                }
       }



    public void generateselected()
    {
        miniTable4.editingStopped(new ChangeEvent(this));
        BigDecimal min=new BigDecimal(2.5);
        try
        {

               StringBuffer sql = new StringBuffer( "SELECT pl.MPC_ProfileBOMLine_ID, pl.Line, ma.Name, ma.Description ,pl.Minimum,pl.Maximum "
			+ "FROM MPC_ProfileBOMLine pl "
                        + "INNER JOIN M_Attribute ma ON(ma.M_Attribute_ID=pl.M_Attribute_ID) "
			+ "WHERE pl.IsActive='Y' "
		        + "AND pl.AD_Client_ID=? AND pl.MPC_ProfileBOM_ID=?");

               PreparedStatement pstmt = DB.prepareStatement(sql.toString());
               pstmt.setInt(1, AD_Client_ID);
               pstmt.setInt(2, m_MPC_ProfileBOM_ID);
               ResultSet rs = pstmt.executeQuery();

			while (rs.next())
			{
                             minimo = rs.getBigDecimal(5);
                 //          System.out.println("minimo de nut   "+minimo);
				//  extend table
			//	min =rs.getBigDecimal(4);
			}
			rs.close();
			pstmt.close();

                       //  StringBuffer ing=new StringBuffer("Select p.M_Product_ID,mai.M_Attribute_ID, mai.Value from M_Product p Inner Join M_AttributeSetInstance masi ON(masi.M_AttributeSetInstance_ID=p.M_AttributeSetInstance_ID) inner join M_AttributeInstance mai ON(mai.M_AttributeSetInstance_ID=p.M_AttributeSetInstance_ID and (mai.Value!='0')) where p.M_Product_ID=?");
                        int rows = miniTable4.getRowCount();
                int row = 0;
                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable4.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rows);

                    if (id != null && id.isSelected())
                    {
                        // validar si ya estan completos los requerimientos
                        // calculo de la combinacion de ingredientes del costo menor

                        miniTable2.setRowCount(row+1);
				//  set values
				miniTable2.setValueAt(id, row, 0);   //  C_Order_ID
                                KeyNamePair k =  (KeyNamePair)miniTable4.getValueAt(i,3);
				miniTable2.setValueAt(k.getName(), row, 5);              //  Org
			        String prod = k.getName();
                                Integer prodi = new Integer(0);
                                prodi=prodi.valueOf(prod);
                                int prodint = prodi.intValue();


//         PreparedStatement pstmting = DB.prepareStatement(ing.toString());
//			//pstmt.setInt(1, AD_Client_ID);
//                        pstmting.setInt(1, prodint);
//			ResultSet rsing = pstmting.executeQuery();
//
//			//
//			while (rsing.next())
//			{
//                            int m_prod = rsing.getInt(1);
//                            System.out.println("prod_id   "+m_prod);
//                            int m_attribute = rsing.getInt(2);
//                            System.out.println("attribute_id   "+m_attribute);
//                            String m_value = rsing.getString(3);
//                            System.out.println("valor de atributo   "+m_value);
//                             nuting = new BigDecimal(m_value);
//                        }
//                        rsing.close();
//                        pstmting.close();
//                          System.out.println("onhandqty " +rs.getBigDecimal(5).doubleValue());
//                                        BigDecimal ckg= new BigDecimal(minimo.doubleValue()/nuting.doubleValue());
//                                            System.out.println("cantidad en kilos necesitada" +ckg);
//        BigDecimal cing= new BigDecimal(ckg.doubleValue()*rs.getBigDecimal(4).doubleValue());
//        System.out.println("costo del ingrediente" +cing);
                               // BigDecimal ckg = new BigDecimal(m_value.doubleValue()/1);
                                miniTable2.setValueAt(miniTable4.getValueAt(i,4), row, 6);              //  DocType
				miniTable2.setValueAt(miniTable4.getValueAt(i,6), row, 7);
                                miniTable2.setValueAt(min, row, 8);
                             //   miniTable.setValueAt(rs.getBigDecimal(5), row, 4);
                                //  Doc No
			//	miniTable.setValueAt(rs.getString(5), row, 4);              //  BPartner
			//	miniTable.setValueAt(rs.getTimestamp(6), row, 5);           //  DateOrdered
			//	miniTable.setValueAt(rs.getBigDecimal(7), row, 6);          //  TotalLines
				//  prepare next
				row++;

                    }
                    else
                    {
                        miniTable2.remove(i);
                    }

                }
        }
        catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery"+ e);
		}

		//	Get selected entries
//		int rows = miniTable4.getRowCount();
//                int row = 0;
//                 for (int i = 0; i < rows; i++)
//		{
//
//                    IDColumn id = (IDColumn)miniTable4.getValueAt(i, 0);//  ID in column 0
//
//                    log.log(Level.FINE, "Row="+ rows);
//
//                    if (id != null && id.isSelected())
//                    {
//                        // validar si ya estan completos los requerimientos
//                        // calculo de la combinacion de ingredientes del costo menor
//
//                        miniTable2.setRowCount(row+1);
//				//  set values
//				miniTable2.setValueAt(id, row, 0);   //  C_Order_ID
//
//				miniTable2.setValueAt(miniTable4.getValueAt(i,1), row, 1);              //  Org
//			        String prod = miniTable4.getValueAt(i,1).toString();
//                                Integer prodi = new Integer(0);
//                                prodi=prodi.valueOf(prod);
//                                int prodint = prodi.intValue();
//                                StringBuffer ing=new StringBuffer("Select p.M_Product_ID,mai.M_Attribute_ID, mai.Value from M_Product p Inner Join M_AttributeSetInstance masi ON(masi.M_AttributeSetInstance_ID=p.M_AttributeSetInstance_ID) inner join M_AttributeInstance mai ON(mai.M_AttributeSetInstance_ID=p.M_AttributeSetInstance_ID and (mai.Value!='0')) where p.M_Product_ID=?");
//
//         PreparedStatement pstmting = DB.prepareStatement(ing.toString());
//			//pstmt.setInt(1, AD_Client_ID);
//                        pstmting.setInt(1, prodint);
//			ResultSet rsing = pstmting.executeQuery();
//
//			//
//			while (rsing.next())
//			{
//                            int m_prod = rsing.getInt(1);
//                            int m_attribute = rsing.getInt(2);
//                            BigDecimal m_value = rsing.getBigDecimal(3);
//                        }
//                        rsing.close();
//                        pstmting.close();
//                                BigDecimal ckg = new BigDecimal(m_value.doubleValue()/1);
//                                miniTable2.setValueAt(miniTable4.getValueAt(i,2), row, 2);              //  DocType
//				miniTable2.setValueAt(miniTable4.getValueAt(i,4), row, 3);
//                                miniTable2.setValueAt(ckg, row, 4);
//                             //   miniTable.setValueAt(rs.getBigDecimal(5), row, 4);
//                                //  Doc No
//			//	miniTable.setValueAt(rs.getString(5), row, 4);              //  BPartner
//			//	miniTable.setValueAt(rs.getTimestamp(6), row, 5);           //  DateOrdered
//			//	miniTable.setValueAt(rs.getBigDecimal(7), row, 6);          //  TotalLines
//				//  prepare next
//				row++;
//
//                    }
//                    else
//                    {
//                        miniTable2.remove(i);
//                    }
//
//                }

		    miniTable2.autoSize();
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InOutGenerateGen"));
	//	statusBar.setStatusDB(String.valueOf(no));

		//	Prepare Process
		/*ProcessInfo pi = new ProcessInfo ("", 199);  // HARDCODED    M_InOut_Create0
		pi.setAD_PInstance_ID (ProcessCtl.getInstanceID (m_WindowNo, pi.getAD_Process_ID(), pi.getRecord_ID()));
		if (pi.getAD_PInstance_ID() == 0)
		{
			info.setText(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
			return;
		}

		//	Add Parameter - Selection=Y
		String sql = "INSERT INTO AD_PInstance_Para (AD_PInstance_ID,SeqNo,ParameterName, P_STRING) "
			+ "VALUES (" + pi.getAD_PInstance_ID() + ",1,'Selection', 'Y')";
		int no = DB.executeUpdate(sql);
		if (no == 0)
		{
			String msg = "No Parameter added";  //  not translated
			info.setText(msg);
			log.log(Level.SEVERE"VEEAgengyGenerate.generateFileLines - " + msg);
			return;
		}

		//	Execute Process
		ProcessCtl worker = new ProcessCtl(this, pi);
		worker.start();  */
        //InfoProduct ip =new InfoProduct();
    }



//    void simplesolver(String av[]) throws QSException {
//    //  this();
//      getargs(av);
//   }
//
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
    }




    /**
	 *  Dispose
	 */
	public void dispose()
	{
		log.info("FormFrame.dispose");
		//	recursive calls
		if (Trace.isCalledFrom("JFrame"))	//	[x] close window pressed
			m_panel.dispose();
		m_panel = null;
		Env.clearWinContext(m_WindowNo);
		super.dispose();
	}	//  dispose

        public BigDecimal requerimientosmin(String m_nut)
        {
             for (int rnut=0; rnut<nutarr.size(); rnut=rnut+3)
             {
             // el nutriente de los requerimientos debe ser igual al nutriente que se esta analizando (del arreglo cant)
             if(nutarr.get(rnut).toString().matches(m_nut))
             {
                  String nutst = new String(nutarr.get(rnut+1).toString());
                  nutbd = new BigDecimal(nutst);
             }
             }
              return nutbd;
        }

        public BigDecimal requerimientosmax(String m_nut)
        {
             for (int rnut=0; rnut<nutarr.size(); rnut=rnut+3)
             {
             // el nutriente de los requerimientos debe ser igual al nutriente que se esta analizando (del arreglo cant)
             if(nutarr.get(rnut).toString().matches(m_nut))
             {
                  String nutst = new String(nutarr.get(rnut+2).toString());
                  nutbdx = new BigDecimal(nutst);
             }
             }
              return nutbdx;
        }
        public BigDecimal checknutdeing(String m_ing, String m_nut)
        {
               for (int aing=0;aing<arregloing.size();aing=aing+3)
               {Integer m_product_id = new Integer(arregloing.get(aing).toString());
                   MProduct prod = new MProduct(Env.getCtx(),m_product_id.intValue(),null);
               KeyNamePair kprod = new KeyNamePair(prod.getM_Product_ID(),prod.getName());
                   if (kprod.toString().matches(m_ing) && arregloing.get(aing+1).toString().matches(m_nut))
                   {
                       String ingnut = new String(arregloing.get(aing+2).toString());
                       ingnutbd = new BigDecimal(ingnut);
                   //    System.out.println("Ingrediente " +arregloing.get(aing)+" nutriente " +ingnutbd);
                   }
               }
               return ingnutbd;
        }

        public void realminmax ()
        {
//            System.out.println("realmin max entra -----------");
             int real_id=0;
                int att_id=0;
                BigDecimal mn=Env.ZERO;
                BigDecimal mx=Env.ZERO;
                boolean p = false;
                boolean px = false;
                 String ps ="";
                String pxs = "";

                for (int n=0;n<miniTable.getRowCount(); n++)
                  {
                    IDColumn id = (IDColumn)miniTable.getValueAt(n, 0);//  ID in column 0

                    if (id != null)
                        {
                            try
                            {
                                 String pr3 = miniTable.getValueAt(n,4).toString();
//                                 System.out.println("atr ------" +pr3);
                                 StringBuffer sql2 = new StringBuffer("SELECT M_Attribute_ID FROM M_Attribute WHERE AD_Client_ID=? AND Name=? ");
                                 PreparedStatement pstmt1 = DB.prepareStatement(sql2.toString());
                                 pstmt1.setInt(1,AD_Client_ID);
                                 pstmt1.setString(2, pr3);

                                 ResultSet rs1 = pstmt1.executeQuery();

                            while (rs1.next())
                            {
				att_id = rs1.getInt(1);
//                                System.out.println("att    "+att_id);

                            }
                            rs1.close();
                            pstmt1.close();



                            StringBuffer sql1 = new StringBuffer("SELECT MPC_ProfileBOM_Real_ID FROM MPC_ProfileBOM_Real WHERE AD_Client_ID=? AND MPC_ProfileBOM_ID=? AND M_Attribute_ID=? ");
                            PreparedStatement pstmt = DB.prepareStatement(sql1.toString());
                            pstmt.setInt(1,AD_Client_ID);
                            pstmt.setInt(2, m_MPC_ProfileBOM_ID);
                            pstmt.setInt(3, att_id);

                            ResultSet rs = pstmt.executeQuery();
//                         System.out.println(sql1.toString());
			//
                            while (rs.next())
                            {
				real_id = rs.getInt(1);
//                                System.out.println("real    "+real_id);

                            }
                            rs.close();
                            pstmt.close();
//                        System.out.println("attributo    "+att_id);
                            StringBuffer sql10 = new StringBuffer("SELECT Minimum, Maximum,IsPrinted,IsPrintedMax FROM MPC_ProfileBOMLine WHERE AD_Client_ID=? AND MPC_ProfileBOM_ID=? AND M_Attribute_ID=? ");
                            PreparedStatement pstmt10 = DB.prepareStatement(sql10.toString());
                            pstmt10.setInt(1,AD_Client_ID);
                            pstmt10.setInt(2, m_MPC_ProfileBOM_ID);
                            pstmt10.setInt(3, att_id);

                            ResultSet rs10 = pstmt10.executeQuery();
//                         System.out.println(sql10.toString());

			//
                            if (rs10.next())
                            {
				mn = rs10.getBigDecimal(1);
//                                 System.out.println("mn --------------" +mn);
                                mx = rs10.getBigDecimal(2);
                                if (rs10.getString(3)!=null && rs10.getString(3).equals("Y"))
                                {p=true;
                                 ps ="Y";
                                }
                                else
                                {p=false;
                                 ps = "N";
                                }
//                                    System.out.println("p --------------" +p);
                                if(rs10.getString(4)!=null && rs10.getString(4).equals("Y"))
                                {    px=true;
                                     pxs="Y";
                                }
                                else
                                {    px=false;
                                     pxs="N";
                                }


                               StringBuffer realup = new StringBuffer("update MPC_ProfileBOM_Real set Minimum=");
                                    realup.append(mn+",Maximum=" +mx +",IsPrinted='"+ps +"',IsPrintedMax='" +pxs +"'");
                                    realup.append(" Where MPC_ProfileBOM_Real_ID=" +real_id);
                           PreparedStatement pstmtup = DB.prepareStatement(realup.toString());

//                        System.out.println("update del real2  " +realup.toString());
			ResultSet rsup = pstmtup.executeQuery();

			//
			rsup.close();
                        pstmtup.close();
//                                  MMPCProfileBOMReal bomreal =new MMPCProfileBOMReal(Env.getCtx(),real_id);
//                         //bomreal.setMPC_ProfileBOM_ID(m_MPC_ProfileBOM_ID);
//                         bomreal.setMinimum(mn);
//                         bomreal.setMaximum(mx);
////                         System.out.println("p --------------" +p);
//                         bomreal.setIsPrinted(p);
////                         System.out.println("px --------------" +px);
//                         bomreal.setIsPrintedMax(px);
//                         bomreal.save();
//                                System.out.println("px --------------" +px);
                            }
                            rs10.close();
                            pstmt10.close();

                        }
                        catch(SQLException s)
                        {
                        }

//                            MMPCProfileBOM profile =new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID);



                       // MMPCProfileBOM bom =new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID);

                         }
                    }
        }

        public BigDecimal ratedeing()
        {


            convrate =Env.ZERO;
            try
            {

                        StringBuffer conv=new StringBuffer("SELECT C_Conversion_Rate_ID, C_Currency_ID,C_Currency_ID_To, MultiplyRate FROM C_Conversion_Rate WHERE AD_Client_ID=? AND C_Currency_ID=100 ORDER BY ValidFrom");
                        PreparedStatement pstmtplv1 = DB.prepareStatement(conv.toString());
			pstmtplv1.setInt(1, AD_Client_ID);
			ResultSet rsplv1 = pstmtplv1.executeQuery();
                        while (rsplv1.next())
                        {
                            if (rsplv1.getInt(3)==130)
                            {
                             convrate=rsplv1.getBigDecimal(4);
//                             System.out.println("tipo de cambio" +convrate);
                            }
                        }
                        rsplv1.close();
                        pstmtplv1.close();

                        MMPCProfileBOM pp = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
                        if (pp.getCurrencyRate().doubleValue()!=0.0)
                        {
                            convrate = pp.getCurrencyRate();
                        }







            }
           catch (SQLException e)
		{

		}

            return convrate;
        }

        public BigDecimal preciodeing(int M_Product_ID)
        {
            //Integer m_ingint = new Integer(m_ing);
            m_PriceStd=Env.ZERO;
            try
            {
                         StringBuffer plv=new StringBuffer("SELECT bompricestd(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceStd, bompricelist(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceList, bompricelimit(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceLimit,pv.ValidFrom,pl.C_Currency_ID, pv.M_PriceList_Version_ID FROM M_Product p INNER JOIN M_ProductPrice pp ON (p.M_Product_ID=pp.M_Product_ID) INNER JOIN  M_PriceList_Version pv ON (pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID) INNER JOIN M_Pricelist pl ON (pv.M_PriceList_ID=pl.M_PriceList_ID) WHERE pv.IsActive='Y' AND pv.AD_Client_ID=? and p.M_Product_ID=? AND pl.IsSOPriceList='N' and pl.IsDefault='Y'"); //ORDER BY pv.ValidFrom DESC");
                         PreparedStatement pstmtplv = DB.prepareStatement(plv.toString());
			 pstmtplv.setInt(1, AD_Client_ID);
                         pstmtplv.setInt(2, M_Product_ID);
			 ResultSet rsplv = pstmtplv.executeQuery();
                         if (rsplv.next())
			 {
                                        Timestamp plDate = rsplv.getTimestamp(4);
					m_PriceStd = rsplv.getBigDecimal (1);
					if (rsplv.wasNull ())
						m_PriceStd = Env.ZERO;
					m_PriceList = rsplv.getBigDecimal (2);
					if (rsplv.wasNull ())
						m_PriceList = Env.ZERO;
					m_PriceLimit = rsplv.getBigDecimal (3);
					if (rsplv.wasNull ())
						m_PriceLimit = Env.ZERO;

					m_C_Currency_ID = rsplv.getInt (5);
					m_M_PriceList_Version_ID = rsplv.getInt(6);
			 }
			 rsplv.close();
			 pstmtplv.close();
                         BigDecimal convrate=Env.ZERO;
                         if (m_C_Currency_ID==100)
                         {
                           StringBuffer conv=new StringBuffer("SELECT C_Conversion_Rate_ID, C_Currency_ID,C_Currency_ID_To, MultiplyRate FROM C_Conversion_Rate WHERE AD_Client_ID=? AND C_Currency_ID=? ORDER BY ValidFrom");
                           PreparedStatement pstmtplv1 = DB.prepareStatement(conv.toString());
			   pstmtplv1.setInt(1, AD_Client_ID);
                           pstmtplv1.setInt(2, m_C_Currency_ID);

			   ResultSet rsplv1 = pstmtplv1.executeQuery();
                            while (rsplv1.next())
                            {
                                if (rsplv1.getInt(3)==130)
                                {
                                 convrate=rsplv1.getBigDecimal(4);
                                }
                            }
                            rsplv1.close();
                            pstmtplv1.close();

                            MMPCProfileBOM pp = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
                            if (pp.getCurrencyRate().doubleValue()!=0.0)
                            {
                                convrate = pp.getCurrencyRate();
                            }


                            m_PriceStd=m_PriceStd.multiply(convrate);
                         }



            }
           catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery", e);
                        m_calculated = false;
		}
            //System.out.println("ing "+m_ingint +" precio funcion "+m_PriceStd);
            return m_PriceStd;
        }

        public int versionprecio()
        {
          //  Integer m_ingint = new Integer(m_ing);
            try
            {
              StringBuffer plv=new StringBuffer("SELECT bompricestd(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceStd, bompricelist(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceList, bompricelimit(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceLimit,pv.ValidFrom,pl.C_Currency_ID, pv.M_PriceList_Version_ID FROM M_Product p INNER JOIN M_ProductPrice pp ON (p.M_Product_ID=pp.M_Product_ID) INNER JOIN  M_PriceList_Version pv ON (pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID) INNER JOIN M_Pricelist pl ON (pv.M_PriceList_ID=pl.M_PriceList_ID) WHERE pv.IsActive='Y' AND pl.IsSOPriceList='N' AND pv.AD_Client_ID=? ORDER BY pv.ValidFrom");
                         PreparedStatement pstmtplv = DB.prepareStatement(plv.toString());
			pstmtplv.setInt(1, AD_Client_ID);
			//pstmt.setInt(2, m_M_PriceList_ID);
			ResultSet rsplv = pstmtplv.executeQuery();
			//while (!m_calculated && rsplv.next())
                        while (rsplv.next())
			{
				Timestamp plDate = rsplv.getTimestamp(4);
				//	we have the price list
				//	if order date is after or equal PriceList validFrom
			//	if (plDate == null)
			//	{
					//	Prices
					m_PriceStd = rsplv.getBigDecimal (1);
                                        //preciostd.add(precios, rsplv.getString(1));
                                        //System.out.println("cont precio " +precios +"precio en esa posicion " +preciostd.get(precios));
            //                            precios++;
					if (rsplv.wasNull ())
						m_PriceStd = Env.ZERO;
					m_PriceList = rsplv.getBigDecimal (2);
					if (rsplv.wasNull ())
						m_PriceList = Env.ZERO;
					m_PriceLimit = rsplv.getBigDecimal (3);
					if (rsplv.wasNull ())
						m_PriceLimit = Env.ZERO;
						//
				//	m_C_UOM_ID = rsplv.getInt (4);
					m_C_Currency_ID = rsplv.getInt (5);
					m_M_PriceList_Version_ID = rsplv.getInt(6);
					//

//					m_calculated = true;
//					break;
			//	}

			}
			rsplv.close();
			pstmtplv.close();
            }
           catch (SQLException e)
		{
			log.log(Level.SEVERE,"VEEAgengyGenerate.executeQuery", e);
                        m_calculated = false;
		}
            //System.out.println("ing "+m_ingint +" precio funcion "+m_PriceStd);
            return m_M_PriceList_Version_ID;
        }



        public void ingconnut(String m_ing,String m_nut)
        { arringconnut.clear();
          cont8=0;
            for (int aing=0;aing<arregloing.size();aing=aing+3)
               {
                  if (arregloing.get(aing).toString().matches(m_ing) && arregloing.get(aing+1).toString().matches(m_nut))
                   {
                       String prods = new String(arregloing.get(aing).toString());
                       // pone en el arreglo el producto
                       arringconnut.add(cont8,prods);
                       cont8++;
                       // pone en el arreglo el valor de nutriente de este ingrediente
                       arringconnut.add(cont8,arregloing.get(aing+2).toString());
                       cont8++;
                       Integer m_product_id = new Integer(prods);
                       preciodeing(m_product_id.intValue());
                     //  System.out.println("precio real "+m_PriceStd +" del ing " +prods);
                       arringconnut.add(cont8,m_PriceStd);
                       cont8++;
                    }
                }
            return;
        }

        public boolean vehiculo(String m_nut)
        {m_vehiculo=true;
            for (int vh=0; vh<nutarr.size(); vh=vh+3)
            {
                 //System.out.println("nut de prod   "+m_nut);
                 //System.out.println("nut de arr   "+nutarr.get(vh));
                if (nutarr.get(vh).toString().matches(m_nut))
                {
                    m_vehiculo=false;
                }


            }
         //System.out.println("m_vehiculo desde funcion   "+m_vehiculo);
            return m_vehiculo;
        }

        public boolean validateingcubre(String m_ing)
        {   cont9=0;
            for (int aing=0; aing<arregloing.size(); aing=aing+3)
            {
                  if (arregloing.get(aing).toString().matches(m_ing))
                   {

                for (int rnut1=0; rnut1<nutarr.size(); rnut1=rnut1+3)
                {

                    if (arregloing.get(aing+1).equals(nutarr.get(rnut1)))
                    {

                         cont9++;
           //              System.out.println("cont9 " +cont9);
                    }
                }
                  }
            }

            if (cont9==nutarr.size()/3)  //?
            {
                m_validateing=true;
            }
            else
            {
                m_validateing=false;
            }
           // System.out.println("validateing" +m_validateing);
            return m_validateing;
        }

        public BigDecimal nutmaspeso(String m_ing)
        { mayor=Env.ZERO;
               for (int aing=0;aing<cant.size();aing=aing+4)
               {
                   if (cant.get(aing).toString().matches(m_ing))
                   {
                       String nutkg = new String(cant.get(aing+2).toString());
                       nutkgbd = new BigDecimal(nutkg);
             //          System.out.println("mayor en func "+mayor);
               //        System.out.println("kilos de nut  " +nutkgbd);
                       if (mayor.doubleValue()<nutkgbd.doubleValue())
                       {
                           mayor = nutkgbd;
                         //  String nutpr = new String(arregloing.get(aing+3).toString());
                         //  nutprbd = new BigDecimal(nutpr);
                       }
                   }
               }
               nutkgbd=mayor;
               return nutkgbd;
             //  return nutprbd;
        }

        private int nutnumporing(String m_ing)
        { cont10=0;
            for (int acant=0;acant<cant.size();acant=acant+4)
            {
                if (cant.get(acant).toString().matches(m_ing))
                {
                    cont10++;
                }
            }
          return cont10;
        }

        private boolean ingsunnut ()
        {
             for (int acant=0;acant<cant.size();acant=acant+4)
            {
             nutnumporing(cant.get(acant).toString());
             if (cont10==1)
                 m_solounnut=true;
             else
                 m_solounnut=false;
             }
             //System.out.println("func solo un nut " +m_solounnut);
             return m_solounnut;
        }

        private boolean nutreq(String m_nut)
        {   m_nutreq=false;
            for (int rnut=0; rnut<validacion.size(); rnut=rnut+2)
            {
                //System.out.println("val nureq1 " +validacion.get(rnut+1));
                //System.out.println("val nutre2 "+m_nut);
                if (validacion.get(rnut+1).toString().matches(m_nut))
                    m_nutreq=true;
//                else
//                    m_nutreq=false;
            }
            //System.out.println("nut requerido " +m_nutreq);
            return m_nutreq;
        }

        private BigDecimal menorprecio(String m_ing1, String m_ing2)
        {
//            for (int acant=0; acant<cant.size(); acant=acant+4)
//            {
//                for (int acant2=acant; acant2<cant.size(); acant2=acant2+4)
//                {
//                    if (cant.get(acant).equals(cant.get(acant2)))
//                    {
//                     menor
//                    }
//                }
//            }

            return menor;
        }

        private String[] parse (String line, String formatType, int fieldNo)
	{

                ArrayList list = new ArrayList();
                final char QUOTE = '"';
		//  check input
		char delimiter = ' ';
               // System.out.println("line:"+ line + formatType + fieldNo);
		if (formatType.equals(FORMATTYPE_COMMA))
			delimiter = ',';
		else if (formatType.equals(FORMATTYPE_TAB))
			delimiter = '\t';
		else
			throw new IllegalArgumentException ("ImpFormat.parseFlexFormat - unknown format: " + formatType);
		if (line == null || line.length() == 0 || fieldNo < 0)
                {
                String[] retValue = new String[list.size()];
		list.toArray(retValue);
		return retValue;
                }


                for (int fields = 0; fields <= fieldNo; fields++)
		{
		//  We need to read line sequentially as the fields may be delimited
		//  with quotes (") when fields contain the delimiter
		//  Example:    "Artikel,bez","Artikel,""nr""",DEM,EUR
		//  needs to result in - Artikel,bez - Artikel,"nr" - DEM - EUR
		int pos = 0;
		int length = line.length();
		for (int field = 0; field <= fields && pos < length; field++)
		{
			StringBuffer content = new StringBuffer();

			//  two delimiter directly after each other
			if (line.charAt(pos) == delimiter)
			{
				pos++;
				continue;
			}
			//  Handle quotes
			if (line.charAt(pos) == QUOTE)
			{
				pos++;  //  move over beginning quote
				while (pos < length)
				{
					//  double quote
					if (line.charAt(pos) == QUOTE && pos+1 < length && line.charAt(pos+1) == QUOTE)
					{
						content.append(line.charAt(pos++));
						pos++;
					}
					//  end quote
					else if (line.charAt(pos) == QUOTE)
					{
						pos++;
						break;
					}
					//  normal character
					else
						content.append(line.charAt(pos++));
				}
				//  we should be at end of line or a delimiter
				if (pos < length && line.charAt(pos) != delimiter)
					log.log(Level.INFO, "ImpFormat.parseFlexFormat - Did not find delimiter at pos " + pos, line);
				pos++;  //  move over delimiter
			}
			else // plain copy
			{
				while (pos < length && line.charAt(pos) != delimiter)
					content.append(line.charAt(pos++));
				pos++;  //  move over delimiter
			}
			if (field == fields)
                            list.add(content.toString());
				//return content.toString();
		}
                }
		//  nothing found
		String[] retValue = new String[list.size()];
		list.toArray(retValue);
		return retValue;
	}   //  parseFlexFormat


          private void fillselected()
          {

                DB.executeUpdate("DELETE FROM MPC_ProfileBOM_Selected WHERE MPC_ProfileBOM_ID="+ m_MPC_ProfileBOM_ID);


           int rows = miniTable2.getRowCount();

                 for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable2.getValueAt(i, 0);//  ID in column 0

                    log.log(Level.FINE, "Row="+ rows);


//                        System.out.println("id" + id);
                       Integer ln=id.getRecord_ID();
//                         System.out.println("ln" + ln);
//                        miniTable.editingStopped(new ChangeEvent(this));
//               System.out.println("minitable getvalue" + miniTable.getValueAt(i, 0));
//                //System.out.println("vlookup de nutrientes2" + nutrientes.toString());
             MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
//             System.out.println(profilebom.getMPC_ProfileBOM_ID() +"   CABECERA    " + m_MPC_ProfileBOM_ID);
             MMPCProfileBOMSelected profilebomselected = new MMPCProfileBOMSelected(profilebom);
             profilebomselected.setMPC_ProfileBOM_ID(profilebom.getMPC_ProfileBOM_ID());
          //   profilebomselected.setClientOrg(profilebom.getAD_Client_ID(), profilebom.getAD_Org_ID());
//             System.out.println("si compila");
           //Env.setContext(Env.getCtx(),m_WindowNo,"#AD_Client_ID",profilebom.getAD_Client_ID());
         if (miniTable2.getValueAt(i,3)!=null && miniTable2.getValueAt(i,3).toString().matches("true"))
         {
             String prod = miniTable2.getValueAt(i,5).toString();
//           System.out.println("producto---------------------------" +miniTable2.getValueAt(i,0));

         int prod_id=0;

          try
            {
                StringBuffer ing = new StringBuffer("SELECT MPC_ProfileBOM_ID FROM MPC_ProfileBOM WHERE AD_Client_ID=? AND Name=?");
                  PreparedStatement pstmtbs = DB.prepareStatement(ing.toString());
			pstmtbs.setInt(1,AD_Client_ID);
                        pstmtbs.setString(2, prod);
			ResultSet rsbs = pstmtbs.executeQuery();
                        while (rsbs.next())
                        {
                             prod_id=rsbs.getInt(1);
                        }
                        rsbs.close();
                        pstmtbs.close();

              }
              catch(SQLException s)
              {
              }
         //  Integer prodb = new Integer(prod);

            profilebomselected.setPlanteamiento(prod_id);
            profilebomselected.setIsCritical(true);
         }
         else
         {
             String prod = miniTable2.getValueAt(i,5).toString();
//           System.out.println("producto" +prod);

         int prod_id=0;

          try
            {
                StringBuffer ing = new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID=? AND Name=?");
                  PreparedStatement pstmtbs = DB.prepareStatement(ing.toString());
			pstmtbs.setInt(1,AD_Client_ID);
                        pstmtbs.setString(2, prod);
			ResultSet rsbs = pstmtbs.executeQuery();
                        while (rsbs.next())
                        {
                             prod_id=rsbs.getInt(1);
                        }
                        rsbs.close();
                        pstmtbs.close();

              }
              catch(SQLException s)
              {
              }
         //  Integer prodb = new Integer(prod);

            profilebomselected.setM_Product_ID(prod_id);
             profilebomselected.setIsCritical(false);
            }
            String kg = miniTable2.getValueAt(i,8).toString();

            BigDecimal kgb = new BigDecimal(kg);

            profilebomselected.setQty(kgb);

            String por = miniTable2.getValueAt(i,7).toString();
            BigDecimal porb = new BigDecimal(por);
            profilebomselected.setPercent(porb);

            String pr = miniTable2.getValueAt(i,6).toString();
            BigDecimal prb = new BigDecimal(pr);
            profilebomselected.setPriceList(prb);



//            System.out.println("isalias" +miniTable2.getValueAt(i,2).toString());
           if (miniTable2.getValueAt(i,2)!=null && miniTable2.getValueAt(i,2).toString().matches("true"))
                 profilebomselected.setIsAlias(true);
            else
                profilebomselected.setIsAlias(false);


            if (miniTable2.getValueAt(i,1)!=null && miniTable2.getValueAt(i,1).toString().matches("true"))
                 profilebomselected.setIsPrinted(true);
            else
                profilebomselected.setIsPrinted(false);

           if (miniTable2.getValueAt(i,3)!=null && miniTable2.getValueAt(i,3).toString().matches("true"))
                 profilebomselected.setIsCritical(true);
            else
                profilebomselected.setIsCritical(false);

                    profilebomselected.save();
                    BigDecimal klbd =new BigDecimal(jTextField2.getText());
                    profilebom.setQty(klbd);
                    BigDecimal prbd =new BigDecimal(jTextField1.getText());
                    profilebom.setPriceList(prbd);
                    profilebom.save();
                    }

}

          private void fillreal()
          {
            DB.executeUpdate("DELETE FROM MPC_ProfileBOM_Real WHERE MPC_ProfileBOM_ID=" + m_MPC_ProfileBOM_ID);
            int rows = miniTable3.getRowCount();
            for (int i = 0; i < rows; i++)
		{

                    IDColumn id = (IDColumn)miniTable3.getValueAt(i, 0);//  ID in column 0
                    Integer ln=id.getRecord_ID();
                    MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
                    MMPCProfileBOMReal profilebomreal = new MMPCProfileBOMReal(profilebom,null);
                    profilebomreal.setMPC_ProfileBOM_ID(profilebom.getMPC_ProfileBOM_ID());
                    Env.setContext(Env.getCtx(),m_WindowNo,"#AD_Client_ID",profilebom.getAD_Client_ID());
                    String attr = miniTable3.getValueAt(i,3).toString();
                    int att_id=0;
                    try
                    {
                        StringBuffer ing = new StringBuffer("SELECT M_Attribute_ID FROM M_Attribute WHERE AD_Client_ID=? AND Name=?");
                        PreparedStatement pstmtbs = DB.prepareStatement(ing.toString());
			pstmtbs.setInt(1,AD_Client_ID);
                        pstmtbs.setString(2, attr);
			ResultSet rsbs = pstmtbs.executeQuery();
                        while (rsbs.next())
                        {
                             att_id=rsbs.getInt(1);
                        }
                        rsbs.close();
                        pstmtbs.close();

                    }
                    catch(SQLException s)
                   {
                        log.log(Level.SEVERE,"ERROR SQL:",s);
                   }


             profilebomreal.setM_Attribute_ID(att_id);
             if (miniTable3.getValueAt(i,4)!=null)
             {
             String mn = miniTable3.getValueAt(i,4).toString();

//               System.out.println("string min" +mn);

             Integer mni= new Integer(0);
            // System.out.println("integer min" + mni.valueOf(mn));
             BigDecimal mnb = new BigDecimal(mn);

//             System.out.println("big min" + mnb);
             profilebomreal.setMinimum(mnb);
             }
             else
             {
                 profilebomreal.setMinimum(cero);
             }
             if (miniTable3.getValueAt(i,6)!=null)
             {
             String mx = miniTable3.getValueAt(i,6).toString();
//             System.out.println("string max" +mx);
             Integer mxi= new Integer(0);
            // System.out.println("integer max" + mxi.valueOf(mx));
             BigDecimal mxb = new BigDecimal(mx);
//             System.out.println("big max" + mxb);
             profilebomreal.setMaximum(mxb);
             }
             else
             {
                 profilebomreal.setMaximum(cero);
             }
            if (miniTable3.getValueAt(i,5)!=null)
             {
               String r = miniTable3.getValueAt(i,5).toString();
//               System.out.println("minitable real  " +r);
            BigDecimal rb = new BigDecimal(r);
            profilebomreal.setRealnut(rb);
            }
            else
            {
               profilebomreal.setRealnut(Env.ZERO);
            }
            if (miniTable3.getValueAt(i,1)!=null && miniTable3.getValueAt(i,1).toString().matches("true"))
                 profilebomreal.setIsPrinted(true);
            else
                profilebomreal.setIsPrinted(false);
            if (miniTable3.getValueAt(i,2)!=null && miniTable3.getValueAt(i,2).toString().matches("true"))
                 profilebomreal.setIsPrintedMax(true);
            else
                profilebomreal.setIsPrintedMax(false);
//            String p = miniTable3.getValueAt(i,1).toString();
//             System.out.println("Printed" +p);
              //    profilebomline.setLine(10);
                    profilebomreal.save();
                    }

}

           public void atributos1(int m_MPC_ProfileBOM_ID)
           {
                //int row5=0;
                //int r2=0;
                //BigDecimal min =Env.ZERO;
                //BigDecimal max =Env.ZERO;
                Hashtable tablatemp = new Hashtable();

                 for (int i=0; i<nutarr1.size(); i++)
                 {
                     MMPCProfileBOMLine pbl =  (MMPCProfileBOMLine)nutarr1.get(i);
                    for (int r=0;r<acalc.size(); r++)
                    {


                        MAttributeInstance m =    (MAttributeInstance)acalc.get(r);
                        //int m_nut_id = new Integer(acalc.get(r).toString()).intValue();
                        //int m_nut_id2 = new Integer(nutarr1.get(n).toString()).intValue();
                        //BigDecimal uno = Env.ONE;
                        //BigDecimal realval = new BigDecimal(acalc.get(r+1).toString());
                        //min = new BigDecimal(nutarr1.get(n+1).toString());
                        //max = new BigDecimal(nutarr1.get(n+2).toString());

                        if (m.getM_Attribute_ID()==pbl.getM_Attribute_ID())
                        {
                            //int sigr = 0;
                                if (tablatemp.get(Integer.toString(m.getM_Attribute_ID())) != null)
                                {
                                     BigDecimal accum = (BigDecimal)tablatemp.get(Integer.toString(m.getM_Attribute_ID()));
                                     //tablatemp.put(acalc.get(r).toString(), new BigDecimal(acalc.get(r+1).toString()).add(sum1));//new BigDecimal(acalc.get(r+1).toString()));
                                     tablatemp.put(Integer.toString(m.getM_Attribute_ID()),accum.add(m.getValueNumber()));
                                }
                                else
                                {
                                     tablatemp.put(Integer.toString(m.getM_Attribute_ID()), m.getValueNumber());
                                }
                        }

                      }
                 }

                //int nutrient_id=0;

                //BigDecimal valrealbd= Env.ZERO;
                String sql = new String("SELECT * FROM MPC_ProfileBOMLine WHERE AD_Client_ID=? AND MPC_ProfileBOM_ID=? ");
                try{
                        //StringBuffer prodid = new StringBuffer("SELECT M_Attribute_ID,Minimum,Maximum, IsPrinted, IsPrintedMax FROM MPC_ProfileBOMLine WHERE AD_Client_ID=? AND MPC_ProfileBOM_ID=? ");
                        PreparedStatement pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, AD_Client_ID);
                        pstmt.setInt(2, m_MPC_ProfileBOM_ID);
			ResultSet rs = pstmt.executeQuery();

			//
                        int c=0;
			while (rs.next())
			{

                          MMPCProfileBOMLine pbl = new MMPCProfileBOMLine(Env.getCtx(),rs, null);
                          if(pbl!=null)
                          {
                          aunico.add(c , pbl);
                          c++;
                          }
                          /*
                          nutrient_id = rsprodid.getInt(1);
                          String nutst_id ="";
                          nutst_id=nutst_id.valueOf(nutrient_id);
                          aunico.add(cont51,nutst_id);
                          cont51++;

                          aunico.add(cont51,rsprodid.getBigDecimal(2));
                          cont51++;
                          aunico.add(cont51,rsprodid.getBigDecimal(3));
                          cont51++;
                          aunico.add(cont51,rsprodid.getString(4));
                          cont51++;
                          aunico.add(cont51,rsprodid.getString(5));
                          cont51++;*/

                        }
                        rs.close();
                        pstmt.close();
                    }
                    catch(SQLException s)
                    {
                        log.log(Level.SEVERE, "Error" , s);
                    }


          //int row3=0;

          int row = 0;
          miniTable3.setRowCount(0);
          for (int i=0;i<aunico.size(); i++)
          {
                 MMPCProfileBOMLine pbl = (MMPCProfileBOMLine)aunico.get(i);
                 if (pbl != null)
                 {
                 miniTable3.setRowCount(row + 1);
                 //IDColumn id  = new IDColumn(pbl.getM_Attribute_ID());
                 IDColumn id  = new IDColumn(1);
                 id.setSelected(true);
                 miniTable3.setValueAt(id,row,0);
                 if(pbl.isPrinted())
                     miniTable3.setValueAt(new Boolean(true), row, 1);
                 else
                     miniTable3.setValueAt(new Boolean(false),row, 1);

                 if(pbl.isPrintedMax())
                     miniTable3.setValueAt(new Boolean(true), row, 2);
                 else
                     miniTable3.setValueAt(new Boolean(false), row, 2);

                 MAttribute a = new MAttribute(Env.getCtx(),pbl.getM_Attribute_ID(),null);
                 KeyNamePair um = new KeyNamePair(a.getM_Attribute_ID(),a.getName());
                 miniTable3.setValueAt(um,row,3);
                 miniTable3.setValueAt(pbl.getMinimum(),row,4);
                 miniTable3.setValueAt((BigDecimal)tablatemp.get(Integer.toString(a.getM_Attribute_ID())),row,5);
                 miniTable3.setValueAt(pbl.getMaximum(),row,6);
                 row++;
                 }
           }
           log.log(Level.INFO,"hash table  " +tablatemp.toString());
 }


      public void nutrientesarreglo()
      {


       String sql= new String("SELECT * FROM MPC_ProfileBOMLine WHERE AD_Client_ID=? AND MPC_ProfileBOM_ID=? ORDER BY M_Attribute_ID");
       try
       {
           PreparedStatement pstmt = DB.prepareStatement(sql);
           pstmt.setInt(1, AD_Client_ID);
           pstmt.setInt(2, m_MPC_ProfileBOM_ID);
           ResultSet rs = pstmt.executeQuery();

			//
                        int c=0;
			while (rs.next())
			{
                           MMPCProfileBOMLine pbl = new MMPCProfileBOMLine(Env.getCtx(),rs, null);
                           if (pbl != null)
                           {
                           nutarr1.add(c,pbl);
                           c++;
                           }
                        }
                        rs.close();
                        pstmt.close();
       }
       catch(SQLException s)
       {
           log.log(Level.SEVERE,"ERROR SQL" + sql ,s );
       }
               //         return nutarr1;
      }

          public boolean yaexiste(String m_ing)
          {
              for (int i=0; i<cantvar.size(); i++)
              {
                  if (cantvar.get(i).toString().matches(m_ing))
                  {
                      m_exists = true;
                  }

              }
              return m_exists;
          }



           public boolean yaexistenut(String m_nut)
          {          int attribute_id =0;
                  try
                                 {
             for (int n=0;n<miniTable3.getRowCount(); n++)
                  {
                    IDColumn id = (IDColumn)miniTable3.getValueAt(n, 0);//  ID in column 0

          if (id != null)
                    {
                                 String kgreal3 = miniTable3.getValueAt(n,3).toString();

                                 StringBuffer prodid = new StringBuffer("SELECT M_Attribute_ID FROM M_Attribute WHERE AD_Client_ID=? AND Name=?");
                           PreparedStatement pstmtprodid = DB.prepareStatement(prodid.toString());
			pstmtprodid.setInt(1, AD_Client_ID);
                        pstmtprodid.setString(2, kgreal3);
			ResultSet rsprodid = pstmtprodid.executeQuery();

			//
			while (rsprodid.next())
			{
                            attribute_id = rsprodid.getInt(1);

                        }
                        rsprodid.close();
                        pstmtprodid.close();
//                System.out.println("nut arreglos" +attribute_id);
                String attrst ="";
                attrst=attrst.valueOf(attribute_id);
//                  System.out.println("nut parametro" +m_nut);
                  if (attrst.matches(m_nut))
                  {
//                      System.out.println("existe");
                      m_exist3 = true;
                  }

              }
             }
                  }
                                 catch(SQLException s){
                                 }
                                  return m_exist3;

          }

           public boolean yaexiste2(String m_ing)
          {
              for (int i=0; i<cantvar2.size(); i++)
              {
                  if (cantvar2.get(i).toString().matches(m_ing))
                  {
                      m_exists2 = true;
                  }

              }
              return m_exists2;
          }

            public boolean noentro(String m_ing)
          {
              for (int i=0; i<solution.size(); i++)
              {
                  Solution s = (Solution) solution.get(i);
                  if (s.getKey().matches(m_ing))
                  //if (selectedarr.get(i).toString().matches(m_ing))
                  {
                      m_exists4 = true;
                  }

              }
              return m_exists4;
          }

         public void ingseleccionados(Vector s)
         {
                int row11=0;
                String m_planteamiento_id="";
                int cual=0;
                String ipr="";
                String ial="";
                //String obj = valor[0].toString();
                int m_product_id=0;
                sumakilos = Env.ZERO;
                realarr.clear();
                MMPCProfileBOM profile1 = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
                MMPCProfileBOMReal profilereal1 = new MMPCProfileBOMReal(profile1,null);
                profilereal1.delete(true);

                for (int v =0 ; v < s.size(); v ++)
                {

                    Solution  i = (Solution)s.get(v);
                     System.out.println(" key:" + i.getKey()  +  " Qty:" + i.getQty());
                    sumakilos =  sumakilos.add(i.getQty()); //new BigDecimal(sumakilos.doubleValue()+kilosfinalbd.doubleValue());
                }

                /*for (int v=0; v<valor.length; v=v+2)
                {
                    BigDecimal kilosfinal = new BigDecimal(valor[v].toString());
                    //BigDecimal kilosfinalbd = new BigDecimal(kilosfinal.doubleValue()*1000.0);
                    BigDecimal kilosfinalbd = new BigDecimal(kilosfinal.doubleValue());
                    sumakilos = new BigDecimal(sumakilos.doubleValue()+kilosfinalbd.doubleValue());
                    System.out.println("Valor ing sel *********** " +valor[v].toString());
                }*/

                /*for (int v=1; v<valor.length; v=v+2)
                { */
                for (int v =0 ; v < s.size(); v ++)
                {
                    Solution  i = (Solution)s.get(v);
                    int sig = 0;
                    int sigcn=0;
                    cual =1;

                        //String tv = valor[v].toString();
                        if(i.getKey().equals(""))
                         m_product_id = 0;
                        else
                         m_product_id = new Integer(i.getKey()).intValue();
                         //System.out.println("Producto:" +  m_product_id );
                        /*
                        for (int tv1=1; tv1<key.length(); tv1++)
                        {
                                if (!key.substring(1,key.length()-1).endsWith("p"))
                                {
                                    m_product_id = new Integer(key.substring(1,key.length()-1)).intValue();
                                    //System.out.println("producto seleccionado ********** " +m_product_id);
                                    cual=1;
                                }
                                else
                                {
                                    m_planteamiento_id = key.substring(1,key.length()-1);
                                    cual=2;
                                }
                         }
                         */
                     //   selectedarr.add(v,tv.substring(1,tv.length()-1));
//                         System.out.println("si 1 = prod , si 2 =plan -------- " +cual);



                        BigDecimal kilosfinal = i.getQty(); //new BigDecimal(valor[v-1].toString());
                        //BigDecimal kilosfinalbd = new BigDecimal(kilosfinal.doubleValue()*1000.0);
                        //BigDecimal kilosfinalbd = new BigDecimal(kilosfinal.doubleValue());
                        //BigDecimal porcen = new BigDecimal(((kilosfinalbd.doubleValue())/sumakilos.doubleValue())*100.0);
                        System.out.println("Total kilos" + sumakilos);

                        BigDecimal porcen = i.getQty().divide(sumakilos,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                        String sting="";
                      //  System.out.println("cual    --" +cual);
                        if (cual==1)
                        {
                            try
                            {
                                  StringBuffer sqlpro= new StringBuffer("SELECT Value FROM M_Product WHERE AD_Client_ID=? and M_Product_ID=? ");
                                PreparedStatement pstmtpro = DB.prepareStatement(sqlpro.toString());
                                pstmtpro.setInt(1, AD_Client_ID);
                                pstmtpro.setInt(2, m_product_id);
                                ResultSet rspro = pstmtpro.executeQuery();
                                String searchkey ="";
                                BigDecimal costoplan1 =Env.ZERO;
                                   if (rspro.next())
                                   {
                                       searchkey = rspro.getString(1);
                                       System.out.println("Valor del producto" +searchkey);
                                   }
                                        rspro.close();
                                        pstmtpro.close();
                                        if (searchkey.startsWith("P"))
                                        {
                                          StringBuffer sql10= new StringBuffer("SELECT (CASE WHEN mp.Qty <> 0 THEN Trunc(mp.PriceList/mp.Qty,3) ELSE 0 END) AS PriceList from MPC_ProfileBOM mp  where mp.IsActive='Y' and mp.Value like '");
                                        sql10.append(searchkey +"%'").append("ORDER BY mp.Value");
//                                           StringBuffer sql10= new StringBuffer("SELECT Trunc(mp.PriceList/1000,2) from MPC_ProfileBOM mp  where mp.AD_Client_ID=1000000 and mp.Value like '");
//                                        sql10.append(searchkey +"%'");
//                                        }
                                        System.out.println("sql ******" +sql10.toString());
                                        PreparedStatement pstmt10 = DB.prepareStatement(sql10.toString());
			                //pstmt10.setInt(1, product_id);
                                        ResultSet rs10 = pstmt10.executeQuery();

                                        if (rs10.next())
                                        {
                                            costoplan1 = rs10.getBigDecimal(1);
                                            //BigDecimal preciofinal = new BigDecimal(costoplan1.doubleValue()*kilosfinalbd.doubleValue());
                                            BigDecimal preciofinal =costoplan1.multiply(kilosfinal);
                                            System.out.println("precio final " +preciofinal);
                                            BigDecimal preciofinalbd = new BigDecimal(preciofinal.doubleValue());
                                            preciototal = new BigDecimal(preciototal.doubleValue()+preciofinalbd.doubleValue());
                                            //System.out.println("precio total " +preciototal);
                //                            System.out.println("kilosfinal    -- " +kilosfinal);
                                            if (kilosfinal.doubleValue()>0.0)
                                            {
                                                miniTable2.setRowCount(row11 +1);
                                                MProduct prod = new MProduct(Env.getCtx(),m_product_id,null);
                                                KeyNamePair kprod = new KeyNamePair(prod.getM_Product_ID(),prod.getName());
                                                IDColumn id  = new IDColumn(m_product_id);
                                                id.setSelected(true);
                                                try
                                                {
                                                    // StringBuffer sel1 = new StringBuffer("Select MPC_ProfileBOM_Selected_ID from MPC_ProfileBOM_Selected where AD_Client_ID=1000000 order by MPC_ProfileBOM_Selected_ID Desc");
                                                    StringBuffer sel12 = new StringBuffer("SELECT IsPrinted, IsAlias FROM MPC_ProfileBOM_Product WHERE AD_Client_ID=? AND M_Product_ID=? AND MPC_ProfileBOM_ID=?");
                                                    PreparedStatement pstmt12 = DB.prepareStatement(sel12.toString());
                                                    pstmt12.setInt(1,AD_Client_ID);
                                                    pstmt12.setInt(2,m_product_id);
                                                    pstmt12.setInt(3,m_MPC_ProfileBOM_ID);
                                                    ResultSet rs12 = pstmt12.executeQuery();

                                                    if(rs12.next())
                                                    {
                                                         if(rs12.getString(1)!=null && rs12.getString(1).equals("Y"))
                                                         {
                                                            miniTable2.setValueAt(new Boolean(true), row11, 1);
                                                            ipr="Y";
                                                         }
                                                         else
                                                         {
                                                            miniTable2.setValueAt(new Boolean(false), row11, 1);
                                                             ipr="N";
                                                         }
                                                         if(rs12.getString(2)!=null && rs12.getString(2).equals("Y"))
                                                         {
                                                             miniTable2.setValueAt(new Boolean(true), row11, 2);
                                                              ial="Y";
                                                         }
                                                         else
                                                         {
                                                            miniTable2.setValueAt(new Boolean(false), row11, 2);
                                                             ial="N";
                                                         }
                                                    }
                                                    rs12.close();
                                                        pstmt12.close();
                                                }
                                                catch (SQLException e)
                                                {
                                                    log.log(Level.SEVERE, "ERROR:", e);
                                                }
                                                BigDecimal uno = new BigDecimal(1.0);
                                                miniTable2.setValueAt(id,row11,0);
                                                System.out.println("valor del producto que mete en ing sel *********" +prod.getValue());
                                                miniTable2.setValueAt(prod.getValue(), row11, 4);
                                                miniTable2.setValueAt(kprod, row11, 5);

                                                miniTable2.setValueAt(preciofinalbd.divide(uno,3,preciofinalbd.ROUND_HALF_UP), row11, 6);
                                                miniTable2.setValueAt(porcen.divide(uno,4,BigDecimal.ROUND_HALF_UP), row11, 7);
                                                //miniTable2.setValueAt(kilosfinalbd.divide(uno,3,BigDecimal.ROUND_HALF_UP), row11, 8);
                                                miniTable2.setValueAt(kilosfinal.divide(uno,3,BigDecimal.ROUND_HALF_UP), row11, 8);

                                                MMPCProfileBOM profile = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);


                                                row11++;
                                         //   System.out.println("costo plan ---------------  " +costoplan1);
                                        }

                                        }
                                        rs10.close();
                                        pstmt10.close();
                                        }
                                        else
                                        {
                            preciodeing(m_product_id);
                            //System.out.println("pricio de func " +m_PriceStd);
                            //BigDecimal preciofinal = new BigDecimal(m_PriceStd.doubleValue()*kilosfinalbd.doubleValue());
                            BigDecimal preciofinal = new BigDecimal(m_PriceStd.doubleValue()*kilosfinal.doubleValue());
                            //System.out.println("precio final " +preciofinal);
                            BigDecimal preciofinalbd = new BigDecimal(preciofinal.doubleValue());
                            preciototal = new BigDecimal(preciototal.doubleValue()+preciofinalbd.doubleValue());
                            //System.out.println("precio total " +preciototal);
//                            System.out.println("kilosfinal    -- " +kilosfinal);
                            if (kilosfinal.doubleValue()>0.0)
                            {
                                miniTable2.setRowCount(row11 +1);
                                MProduct prod = new MProduct(Env.getCtx(),m_product_id,null);
                                KeyNamePair kprod = new KeyNamePair(prod.getM_Product_ID(),prod.getName());
                                IDColumn id  = new IDColumn(m_product_id);
                                id.setSelected(true);
                                try
                                {
                                    // StringBuffer sel1 = new StringBuffer("Select MPC_ProfileBOM_Selected_ID from MPC_ProfileBOM_Selected where AD_Client_ID=1000000 order by MPC_ProfileBOM_Selected_ID Desc");
                                    StringBuffer sel12 = new StringBuffer("SELECT IsPrinted, IsAlias from MPC_ProfileBOM_Product WHERE AD_Client_ID=? AND M_Product_ID=? AND MPC_ProfileBOM_ID=?");
                                    PreparedStatement pstmt12 = DB.prepareStatement(sel12.toString());
                                    pstmt12.setInt(1,AD_Client_ID);
                                    pstmt12.setInt(2,m_product_id);
                                    pstmt12.setInt(3,m_MPC_ProfileBOM_ID);
                                    ResultSet rs12 = pstmt12.executeQuery();

                                    if(rs12.next())
                                    {
                                         if(rs12.getString(1)!=null && rs12.getString(1).equals("Y"))
                                         {
                                            miniTable2.setValueAt(new Boolean(true), row11, 1);
                                            ipr="Y";
                                         }
                                         else
                                         {
                                            miniTable2.setValueAt(new Boolean(false), row11, 1);
                                             ipr="N";
                                         }
                                         if(rs12.getString(2)!=null && rs12.getString(2).equals("Y"))
                                         {
                                             miniTable2.setValueAt(new Boolean(true), row11, 2);
                                              ial="Y";
                                         }
                                         else
                                         {
                                            miniTable2.setValueAt(new Boolean(false), row11, 2);
                                             ial="N";
                                         }
                                    }
                                    rs12.close();
                                        pstmt12.close();
                                }
                                catch (SQLException e)
                                {
                                    log.log(Level.SEVERE, "ERROR:", e);
                                }
                                BigDecimal uno = new BigDecimal(1.0);
                                miniTable2.setValueAt(id,row11,0);
                                miniTable2.setValueAt(prod.getValue(), row11, 4);
                        	miniTable2.setValueAt(kprod, row11, 5);

                                miniTable2.setValueAt(preciofinalbd.divide(uno,3,preciofinalbd.ROUND_HALF_UP), row11, 6);
                                miniTable2.setValueAt(porcen.divide(uno,4,BigDecimal.ROUND_HALF_UP), row11, 7);
                                //miniTable2.setValueAt(kilosfinalbd.divide(uno,3,BigDecimal.ROUND_HALF_UP), row11, 8);
                                miniTable2.setValueAt(kilosfinal.divide(uno,3,BigDecimal.ROUND_HALF_UP), row11, 8);


                                MMPCProfileBOM profile = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);

//
                                row11++;
//                                profileselected.save();
                            }
                            }
                                         }
                                        catch (SQLException exc)
                                        {
                                            log.log(Level.SEVERE, "ERROR:", exc);
                                        }
                        }
                        if (cual==2)
                        {
                            BigDecimal costoplan1=Env.ZERO;
                            BigDecimal QtyP=Env.ZERO;
                            BigDecimal mil = new BigDecimal(1000.0);
                            int ultimo=0;
                            ultimo = m_planteamiento_id.length()-1;
//                            System.out.println("ultimo menos uno  ------"+ultimo);
                            Integer mpc_id1 = new Integer(m_planteamiento_id.substring(0,ultimo));
//                            System.out.println("planteamiento -------------" +m_planteamiento_id.substring(0,ultimo));
                            try
                            {
                                    StringBuffer sql10= new StringBuffer("SELECT mp.LineNetAmt,mp.Qty FROM MPC_ProfileBOM mp  WHERE mp.AD_Client_ID=? AND mp.MPC_ProfileBOM_ID=? DESC");
                                    PreparedStatement pstmt10 = DB.prepareStatement(sql10.toString());
                                    pstmt10.setInt(1, AD_Client_ID);
                                    pstmt10.setInt(2, mpc_id1.intValue());
                                    ResultSet rs10 = pstmt10.executeQuery();

                                    if (rs10.next())
                                    {
                                        costoplan1 = rs10.getBigDecimal(1);
                                        QtyP = rs10.getBigDecimal(2);
//                                        if (QtyP.doubleValue()!=0.0)
//                                            costoplan1 = costoplan1.divide(QtyP,3,costoplan1.ROUND_HALF_UP);
//                                        else
//                                            costoplan1 = costoplan1.divide(mil,3,costoplan1.ROUND_HALF_UP);
//                                        System.out.println("costo plan   " +costoplan1);
                                    }
                                    rs10.close();
                                    pstmt10.close();
                            }
                            catch(SQLException e)
                            {
                                log.log(Level.SEVERE, "ERROR:", e);
                            }
                            //System.out.println("pricio de func " +m_PriceStd);
                            //BigDecimal preciofinal = new BigDecimal(costoplan1.doubleValue()*kilosfinalbd.doubleValue());
                            BigDecimal preciofinal = new BigDecimal(costoplan1.doubleValue()*kilosfinal.doubleValue());
                            //System.out.println("precio final " +preciofinal);
                            BigDecimal preciofinalbd = new BigDecimal(preciofinal.doubleValue());
                            preciototal = new BigDecimal(preciototal.doubleValue()+preciofinalbd.doubleValue());
                            //System.out.println("precio total " +preciototal);
                            if (kilosfinal.doubleValue()>0.0)
                            {
                                miniTable2.setRowCount(row11 +1);
                                MMPCProfileBOM pbom = new MMPCProfileBOM(Env.getCtx(),mpc_id1.intValue(),null);
                                KeyNamePair kpbom = new KeyNamePair(pbom.getMPC_ProfileBOM_ID(),pbom.getName());
                                IDColumn id  = new IDColumn(mpc_id1.intValue());
                                id.setSelected(true);
                                try
                                {
                                    // StringBuffer sel1 = new StringBuffer("Select MPC_ProfileBOM_Selected_ID from MPC_ProfileBOM_Selected where AD_Client_ID=1000000 order by MPC_ProfileBOM_Selected_ID Desc");
                                    StringBuffer sel12 = new StringBuffer("SELECT IsPrinted, IsAlias FROM MPC_ProfileBOM_Product WHERE AD_Client_ID=? AND Planteamiento=? AND MPC_ProfileBOM_ID=?");
                                    PreparedStatement pstmt12 = DB.prepareStatement(sel12.toString());
                                    pstmt12.setInt(1,AD_Client_ID);
                                    pstmt12.setInt(2,mpc_id1.intValue());
                                    pstmt12.setInt(3,m_MPC_ProfileBOM_ID);
                                    ResultSet rs12 = pstmt12.executeQuery();

                                    if(rs12.next())
                                    {
                                         if(rs12.getString(1)!=null && rs12.getString(1).equals("Y"))
                                         {
                                            miniTable2.setValueAt(new Boolean(true), row11, 1);
                                            ipr="Y";
                                         }
                                         else
                                         {
                                            miniTable2.setValueAt(new Boolean(false), row11, 1);
                                            ipr="N";
                                         }
                                         if(rs12.getString(2)!=null && rs12.getString(2).equals("Y"))
                                         {
                                            miniTable2.setValueAt(new Boolean(true), row11, 2);
                                            ial="Y";
                                         }
                                         else
                                         {
                                             miniTable2.setValueAt(new Boolean(false), row11, 2);
                                             ial="N";
                                         }

                                    }
                                    rs12.close();
                                    pstmt12.close();
                                }
                                catch (SQLException e)
                                {
                                    log.log(Level.SEVERE, "ERROR:", e);
                                }
                                Boolean selplan = new Boolean(true);
                                BigDecimal uno = new BigDecimal(1.0);
                                miniTable2.setValueAt(id,row11,0);
                                miniTable2.setValueAt(selplan,row11,3);
                                miniTable2.setValueAt(pbom.getValue(), row11, 4);
                       		miniTable2.setValueAt(kpbom, row11, 5);
                                miniTable2.setValueAt(preciofinalbd.divide(uno,3,preciofinalbd.ROUND_HALF_UP), row11, 6);
                                miniTable2.setValueAt(porcen.divide(uno,4,BigDecimal.ROUND_HALF_UP), row11, 7);
                                //miniTable2.setValueAt(kilosfinalbd.divide(uno,3,BigDecimal.ROUND_HALF_UP), row11, 8);
                                miniTable2.setValueAt(kilosfinal.divide(uno,3,BigDecimal.ROUND_HALF_UP), row11, 8);
                                MMPCProfileBOM profile = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
                                row11++;
                            }
                        }
             }
             miniTable2.autoSize();
             int mt2r=0;
             int c15=0;
             int c16=0;

             //pba recalculo
             nutrientesarreglo();

//             System.out.println(nutarr1.toString());

             /*for (int v=1; v<valor.length; v=v+2)
                {
                        String tv = valor[v].toString();
                        for (int tv1=0; tv1<tv.length(); tv1++)
                        {
                                if (!tv.substring(1,tv.length()-1).endsWith("p"))
                                {
                                    m_product_id = new Integer(tv.substring(1,tv.length()-1)).intValue();
                                    cual=1;
                                }
                                else
                                {
                                    m_planteamiento_id = tv.substring(1,tv.length()-1);
                                    cual=2;
                                }
                         }
             }*/

                   mt2r=miniTable2.getRowCount();
                  nutacum3=Env.ZERO;
                  nutacum2=Env.ZERO;

                  for (int m=0;m<mt2r; m++)
                  {
                    IDColumn id = (IDColumn)miniTable2.getValueAt(m, 0);//  ID in column 0

                    if (id != null)
                    {
//                     System.out.println("cual------------------"+cual);
			        String prod = miniTable2.getValueAt(m,5).toString();
//                                 System.out.println("prod mt2 " +prod);
                                 String kgreal = miniTable2.getValueAt(m,8).toString();
                                 if (cual==1)
                                 {
                                try{
                                        StringBuffer sql1 = new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID=? AND Name=?");
                                        PreparedStatement pstmt = DB.prepareStatement(sql1.toString());
                                        pstmt.setInt(1,AD_Client_ID);
                                        pstmt.setString(2, prod);
                                        ResultSet rs = pstmt.executeQuery();

                                        while (rs.next())
                                        {
                                            m_prod_id = rs.getInt(1);
                                            calculado(m_prod_id,kgreal);

                                        }
                                        rs.close();
                                        pstmt.close();

                                    }
                                    catch(SQLException e)
                                    {
                                        log.log(Level.SEVERE, "ERROR:", s);
                                    }
                                 }
                                  if (cual==2)
                                  {
                                     try{
                                            StringBuffer sql1 = new StringBuffer("SELCT MPC_ProfileBOM_ID FROM MPC_ProfileBOM WHERE AD_Client_ID=? AND Name=?");
                                            PreparedStatement pstmt = DB.prepareStatement(sql1.toString());
                                            pstmt.setInt(1,AD_Client_ID);
                                            pstmt.setString(2, prod);
                                            ResultSet rs = pstmt.executeQuery();

                                            while (rs.next())
                                            {
                                                m_prod_id = rs.getInt(1);
                                                calculadop(m_prod_id,kgreal);
                                            }
                                            rs.close();
                                            pstmt.close();

                                        }
                                        catch(SQLException e)
                                        {
                                            log.log(Level.SEVERE, "ERROR:", s);
                                        }
                                 }

                     }
                    c15++;
                }
                      atributos1(m_MPC_ProfileBOM_ID);
                     // realminmax();
                   //   executeQuery3();
                     // executeQueryReal();
                      BigDecimal uno = Env.ONE;
          //     System.out.println("preciototal ---------" +preciototal +"preciototal scale ---------- " +preciototal.setScale(2,5));
               preciototal =preciototal.divide(uno,2,BigDecimal.ROUND_HALF_UP);
              miniTable3.autoSize();
               jTextField1.setText(preciototal.toString());
               jTextField1.disable();
               sumakilos = sumakilos.divide(uno,3,BigDecimal.ROUND_HALF_UP);
                jTextField2.setText(sumakilos.toString());
                 jTextField2.disable();
                 MMPCProfileBOM profilebom =new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
                 profilebom.setPriceList(preciototal);
                 BigDecimal margen = new BigDecimal(profilebom.getMargin().doubleValue());
//                 System.out.println("margen "+margen);
                 BigDecimal total = preciototal.multiply(margen.divide(new BigDecimal(100),4,margen.ROUND_HALF_UP).add(new BigDecimal(1)));
//                 System.out.println("total * margen "+total);
                 profilebom.setQty(sumakilos);
                 ratedeing();
                 profilebom.setCurrencyRate(convrate);
                 profilebom.setLineNetAmt(total);
                 profilebom.save();
         }

         public BigDecimal real(String m_ing, String m_kg, String m_nut)
         {


//            System.out.println("arregloing "+arregloing.size());
             for (int a=0; a<arregloing.size(); a=a+3)
             {
                  Integer m_ingint = new Integer(arregloing.get(a).toString());
                  MProduct prod = new MProduct(Env.getCtx(),m_ingint.intValue(),null);
                  KeyNamePair kprod = new KeyNamePair(prod.getM_Product_ID(),prod.getName());
               //System.out.println("ing reales  " +m_ing +"= kprod  " +kprod + " nut reales " +m_nut +"= nut arregloing " +arregloing.get(a+1));
                 if (kprod.toString().matches(m_ing) && arregloing.get(a+1).toString().matches(m_nut))
                 {

                     String valoring = new String(arregloing.get(a+2).toString());
                     BigDecimal valoringbd = new BigDecimal(valoring);
               //      System.out.println("valor ing prod " +valoringbd);
                     BigDecimal kgmt = new BigDecimal(m_kg);
                 //    System.out.println("kilos  " +kgmt);
                     BigDecimal nutacum = new BigDecimal((kgmt.doubleValue()/1000.0)*valoringbd.doubleValue());
                   //  System.out.println("nut acum in1 " +nutacum);
                      nutacum2 = new BigDecimal(nutacum2.doubleValue()+nutacum.doubleValue());
                     //  System.out.println("nut acum ing " +nutacum2);
                 }
             }
           return nutacum2;
         }

         public void calculado(int m_M_Product_ID, String kgsel)
         {m_exist3 = false;
            BigDecimal kgbd2 = new BigDecimal(kgsel);
             int m_M_AttributeSetInstance_ID=0;
//             System.out.println("contador 50    "+cont500);
               try
                    {
                        StringBuffer sql1 = new StringBuffer("SELECT M_AttributeSetInstance_ID FROM M_Product WHERE AD_Client_ID=? AND M_Product_ID=?");
                        PreparedStatement pstmtsql1 = DB.prepareStatement(sql1.toString());
                        pstmtsql1.setInt(1, AD_Client_ID);
			//pstmtsql.setInt(2, m_MPC_ProfileBOM_ID);
                        pstmtsql1.setInt(2, m_M_Product_ID);
			ResultSet rssql1 = pstmtsql1.executeQuery();
                        if (rssql1.next())
                        {
                           m_M_AttributeSetInstance_ID=rssql1.getInt(1);
//                                System.out.println("instancia --------------     " +m_M_AttributeSetInstance_ID);
                        }
                        rssql1.close();
                        pstmtsql1.close();
                    }
                    catch (SQLException s)
                    {
                        log.log(Level.SEVERE, "ERROR:", s);
                    }
             m_masi = MAttributeSetInstance.get(Env.getCtx(), m_M_AttributeSetInstance_ID, m_M_Product_ID);
             MAttributeSet as = m_masi.getMAttributeSet();
//                System.out.println("attribute set --------------     " +as.getM_AttributeSet_ID());
             //   MAttribute[] attributes = as.getMAttributes (false);
               int instancia = m_masi.getM_AttributeSetInstance_ID();
//                System.out.println("attribute set --------------     " +instancia);
               int nut_id=0;

               String nut_idst="";
               BigDecimal rn = Env.ZERO;
               BigDecimal mil = new BigDecimal(1000.0);
               //String sql = new String("SELECT M_Attribute_ID, ValueNumber FROM M_AttributeInstance WHERE AD_Client_ID=? AND M_AttributeSetInstance_ID=? AND ValueNumber!=0");
               String sql = new String("SELECT * FROM M_AttributeInstance WHERE AD_Client_ID=? AND M_AttributeSetInstance_ID=? AND ValueNumber!=0");
               try
               {
                        PreparedStatement pstmt = DB.prepareStatement(sql);
                        pstmt.setInt(1, AD_Client_ID);
			//pstmt.setInt(2, m_MPC_ProfileBOM_ID);
                        pstmt.setInt(2, instancia);
			ResultSet rs = pstmt.executeQuery();
                        int c = 0;
                        while (rs.next())
                        {
                            MAttributeInstance m = new MAttributeInstance(Env.getCtx(), rs ,null);

                            //nut_id = rssql.getInt(1);
                            //nut_idst= nut_idst.valueOf(nut_id);
                           acalc.add(c,m);
                           c++;
                            //rn = rssql.getBigDecimal(2);
                            //rn=rn.multiply(kgbd2);
                            //rn=rn.divide(mil,3,rn.ROUND_HALF_UP);
                            // acalc.add(cont500, rn);
                            // cont500++;
//                                System.out.println("attributo --------------     " +nut_id + "valor" +rn);

                        }
                        rs.close();
                        pstmt.close();
                    }
                    catch (SQLException s)
                    {
                        log.log(Level.SEVERE, "ERROR:", s);
                    }


         }

         public void calculadop(int m_M_Product_ID, String kgsel)
         {m_exist3 = false;
            BigDecimal kgbd2 = new BigDecimal(kgsel);
             int m_M_AttributeSetInstance_ID=0;
//             System.out.println("contador 50    "+cont500);
               try
                    {
                        StringBuffer sql1 = new StringBuffer("SELECT M_AttributeSetInstance_ID FROM MPC_ProfileBOM WHERE AD_Client_ID=? AND MPC_ProfileBOM_ID=?");
                            PreparedStatement pstmtsql1 = DB.prepareStatement(sql1.toString());
                        pstmtsql1.setInt(1, AD_Client_ID);
			//pstmtsql.setInt(2, m_MPC_ProfileBOM_ID);
                        pstmtsql1.setInt(2, m_M_Product_ID);
			ResultSet rssql1 = pstmtsql1.executeQuery();
                        if (rssql1.next())
                        {
                           m_M_AttributeSetInstance_ID=rssql1.getInt(1);
//                                System.out.println("instancia --------------     " +m_M_AttributeSetInstance_ID);
                        }
                        rssql1.close();
                        pstmtsql1.close();
                    }
                    catch (SQLException s)
                    {
                        log.log(Level.SEVERE, "ERROR:", s);
                    }
             m_masi = MAttributeSetInstance.get(Env.getCtx(), m_M_AttributeSetInstance_ID, m_M_Product_ID);
             System.out.println("sin atributos " +m_masi);
             if (m_masi!=null)
             {
             MAttributeSet as = m_masi.getMAttributeSet();
//                System.out.println("attribute set --------------     " +as.getM_AttributeSet_ID());
             //   MAttribute[] attributes = as.getMAttributes (false);
               int instancia = m_masi.getM_AttributeSetInstance_ID();
//                System.out.println("attribute set --------------     " +instancia);
               int nut_id=0;

               String nut_idst="";
               BigDecimal rn = Env.ZERO;
               BigDecimal mil = new BigDecimal(1000.0);

                 //String sql = new String("SELECT M_Attribute_ID, ValueNumber FROM M_AttributeInstance WHERE AD_Client_ID=? AND M_AttributeSetInstance_ID=? AND ValueNumber!=0");
                 String sql = new String("SELECT * FROM M_AttributeInstance WHERE AD_Client_ID=? AND M_AttributeSetInstance_ID=? AND ValueNumber!=0");
                 try
                    {
                        PreparedStatement pstmt = DB.prepareStatement(sql);
                        pstmt.setInt(1, AD_Client_ID);
			//pstmtsql.setInt(2, m_MPC_ProfileBOM_ID);
                        pstmt.setInt(2, instancia);
			ResultSet rs = pstmt.executeQuery();
                        int c = 0;
                        while (rs.next())
                        {
                            MAttributeInstance m = new MAttributeInstance(Env.getCtx(), rs ,null);
                            //nut_id = rssql.getInt(1);
                            //nut_idst= nut_idst.valueOf(nut_id);
                            acalc.add(c, m);
                            c++;
                            //cont500++;
                            //rn = rssql.getBigDecimal(2);
                            //rn=rn.multiply(kgbd2);
                            //rn=rn.divide(mil,3,rn.ROUND_HALF_UP);
                            //acalc.add(cont500, rn);
                            // cont500++;
//                                System.out.println("attributo --------------     " +nut_id + "valor" +rn);

                        }
                        rs.close();
                        pstmt.close();
                    }
                    catch (SQLException s)
                    {
                        log.log(Level.SEVERE, "ERROR:", s);
                    }
             }

         }


         public int atributos (int m_M_Product_ID)
         {   int m_M_AttributeSetInstance_ID=0;
             m_masi = MAttributeSetInstance.get(Env.getCtx(), m_M_AttributeSetInstance_ID, m_M_Product_ID);
           //  System.out.println("attribute set instance--------------     " +m_masi.getM_AttributeSet_ID());
             Env.setContext(Env.getCtx(), m_WindowNo, "M_AttributeSet_ID", m_masi.getM_AttributeSet_ID());
          //   m_M_AttributeSetInstance_ID=m_masi.getM_AttributeSetInstance_ID();
		//	Get Attribute Set
		MAttributeSet as = m_masi.getMAttributeSet();
             //   System.out.println("attribute set --------------     " +as);
                MAttribute[] attributes = as.getMAttributes (false);
               // System.out.println("attribute --------------     " +attributes.length);
                for (int i = 0; i < attributes.length; i++)
                {
                    insatt_id =0;
                    rn = Env.ZERO;
                 //   System.out.println("attribute value --------------     " +attributes[i]);
                  //  System.out.println("attribute id--------------     " +attributes[i].getM_Attribute_ID());

                    try
                    {
                        StringBuffer sql = new StringBuffer("SELECT M_Attribute_ID, Realnut From MPC_ProfileBOM_Real WHERE AD_Client_ID=? AND MPC_ProfileBOM_ID=? AND M_Attribute_ID=?");
                        PreparedStatement pstmtsql = DB.prepareStatement(sql.toString());
                        pstmtsql.setInt(1, AD_Client_ID);
			pstmtsql.setInt(2, m_MPC_ProfileBOM_ID);
                        pstmtsql.setInt(3, attributes[i].getM_Attribute_ID());
			ResultSet rssql = pstmtsql.executeQuery();
                        while (rssql.next())
                        {
                            insatt_id = rssql.getInt(1);
                            rn = rssql.getBigDecimal(2);
                        }
                        rssql.close();
                        pstmtsql.close();
                    }
                    catch (SQLException s)
                    {
                        log.log(Level.SEVERE, "ERROR:", s);
                    }
                    if (insatt_id==0)
                    {
                        m_editor.add(i,rn);
                    }
                    else
                        m_editor.add(i,rn);


                    //System.out.println("arreglo    --------------     " +m_editor.get(i));
                }
                if (m_masi.getM_AttributeSetInstance_ID() == 0)
		{
			m_masi.save ();
			m_M_AttributeSetInstance_ID = m_masi.getM_AttributeSetInstance_ID ();
			m_M_AttributeSetInstanceName = m_masi.getDescription();
		}
                  for (int j = 0; j < attributes.length; j++)
                {
                    BigDecimal value = (BigDecimal)m_editor.get(j);
                     //System.out.println("attributesetinstance_ID    --------------     " +m_M_AttributeSetInstance_ID);
               attributes[j].setMAttributeInstance(m_M_AttributeSetInstance_ID, value);
//                 MAttributeInstance instance = attributes[j].getMAttributeInstance (m_M_AttributeSetInstance_ID);
//                          System.out.println("instance id--------------     " +instance.getM_AttributeSetInstance_ID());
//                    System.out.println("instance id--------------     " +instance.getValue());
//                     System.out.println("nuevos VALORES atributte set instance    --------------     " +attributes[j].getMAttributeValues());
                  }
                m_instance = m_M_AttributeSetInstance_ID;
                return m_instance;
             //     System.out.println("nuevos VALORES atributte set instance    --------------     " +attribu);
         }

         private void creaprod()
         {
            if (ADialog.ask(m_WindowNo, this, "�Estas seguro de generar el Producto?"))
            {
                   MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
                   MMPCProfileBOMSelected profilebomsel = new MMPCProfileBOMSelected(Env.getCtx(),profilebom.getMPC_ProfileBOM_ID(),null);
                   MMPCProductBOM prodbom = new MMPCProductBOM(Env.getCtx(),0,null);
                   prodbom.setValue(profilebom.getName());
                   prodbom.setName(profilebom.getName());
                   //prodbom.setM_Product_ID(profilebom.getName());
                   prodbom.setValidFrom(profilebom.getDateDoc());
                   prodbom.setC_UOM_ID(profilebom.getC_UOM_ID());
                   prodbom.setM_Product_ID(profilebom.getM_Product_ID());
                   atributos(profilebom.getM_Product_ID());
                   prodbom.setM_AttributeSetInstance_ID(m_instance);
                   MProduct producto = new MProduct(Env.getCtx(),0,null);
                   producto.setValue(profilebom.getValue());
                   producto.setName(profilebom.getName());
                   producto.setIsPurchased(false);
                   producto.setM_Product_Category_ID(profilebom.getM_Product_Category_ID());
                   producto.setVersionNo(profilebom.getSpecie());
                   producto.setC_UOM_ID(profilebom.getC_UOM_ID());
                   producto.setProductType(producto.PRODUCTTYPE_Item);
                   producto.setC_TaxCategory_ID(1000001);
                   producto.setM_AttributeSet_ID(1000001);
                   producto.setM_AttributeSetInstance_ID(m_instance);
                   producto.save();

                   if(prodbom.save())
                   {
                        String salvado1 = new String("F�rmula creada con �xito "+prodbom.getMPC_Product_BOM_ID());
                        ADialog.info(m_WindowNo,this,salvado1);
                   }
                   else
                   {
                        ADialog.info(m_WindowNo,this,"No se pudo generar la F�rmula");
                   }
                   for (int i=0;i<miniTable2.getRowCount(); i++)
                   {
                   String mp = "";
                   mp = miniTable2.getValueAt(i,5).toString();
                     try{
                       StringBuffer sql1 = new StringBuffer("SELECT M_Product_ID FROM M_Product WHERE AD_Client_ID=? and Name=?");
                        PreparedStatement pstmt = DB.prepareStatement(sql1.toString());
                                pstmt.setInt(1,AD_Client_ID);
                                pstmt.setString(2, mp);

                                ResultSet rs = pstmt.executeQuery();

                                //
                                while (rs.next())
                                {
                                        m_prod_id = rs.getInt(1);

                                }
                                rs.close();
                                pstmt.close();

           }
           catch(SQLException s)
           {
               log.log(Level.SEVERE, "ERROR:", s);
           }
            MMPCProductBOMLine prodbomline = new MMPCProductBOMLine(prodbom);
           prodbomline.setM_Product_ID(m_prod_id);
           BigDecimal p = new BigDecimal(miniTable2.getValueAt(i,7).toString());
           prodbomline.setQtyBatch(p);
           prodbomline.setValidFrom(prodbom.getValidFrom());
           prodbomline.setValidTo(prodbom.getValidTo());
           prodbomline.setLine(i*10);
           prodbomline.setC_UOM_ID(profilebom.getC_UOM_ID());
           prodbomline.setIsQtyPercentage(true);
           prodbomline.setIssueMethod(prodbomline.ISSUEMETHOD_BackFlush);
           prodbomline.save();

           }

        }
         }
  //  public void init(int WindowNo, FormFrame frame) {
     //    System.out.println("Valor de Profile5      " );
   // }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JInternalFrame Ingredients;
    private javax.swing.JInternalFrame Nutrients;
    private javax.swing.JInternalFrame Selected;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
      private javax.swing.JButton jButton6;
       private javax.swing.JButton jButton31;
         private javax.swing.JButton jButton33;
      private javax.swing.JInternalFrame jInternalFrame5;
      private javax.swing.JInternalFrame jInternalFrame6;
       private javax.swing.JInternalFrame jInternalFrame7;
       private javax.swing.JInternalFrame jInternalFrame8;
       private javax.swing.JInternalFrame jInternalFrame9;
         private javax.swing.JInternalFrame jInternalFrame10;
//    private javax.swing.JLabel jLabel1;
        private javax.swing.JInternalFrame lista;
 //        private javax.swing.JButton jButton5;
         private javax.swing.JButton jButton10;
         private javax.swing.JButton jButtonN;
         private javax.swing.JButton jButtonI;
         private javax.swing.JButton jButton40;
         private javax.swing.JButton jButton41;
         private javax.swing.JButton jButton45;
         private javax.swing.JButton jButton46;
         private javax.swing.JButton jButton47;
         private javax.swing.JButton jButton48;
         private javax.swing.JButton jButton58;
         private javax.swing.JButton jButton59;
         private javax.swing.JButton jButtonP;
         private javax.swing.JButton jButtonNR;
         private javax.swing.JButton jCopyNutAnalisis;
         private javax.swing.JPanel jPanel6;
         private javax.swing.JPanel jPanel7;
         private javax.swing.JPanel jPanel10;
         private javax.swing.JPanel jPanel11;
         private javax.swing.JPanel jPanel12;
         private javax.swing.JButton jButton11;
         private javax.swing.JPanel jPanel21;
         private javax.swing.JPanel jPanelEmp;
         private javax.swing.JButton jButton32;
         private javax.swing.JButton jButton21;
         private javax.swing.JButton jButton12;
         private javax.swing.JButton jButton30;
         private javax.swing.JButton jButtonG;
         private javax.swing.JButton jButtonCA;
         private javax.swing.JButton jButtonCP;
            private javax.swing.JButton jButton100;
         private javax.swing.JComboBox jComboBox2;
         private ArrayList cantvar1 = new ArrayList();
          private ArrayList cantvar2 = new ArrayList();
          private boolean m_exists2=false;
        private boolean m_exists4=false;
        private ArrayList realarr = new ArrayList();
        private ArrayList vehiculoarr = new ArrayList();
        private ArrayList vehiculoarr2 = new ArrayList();
        private BigDecimal nutacum2=Env.ZERO;
        private BigDecimal nutacum3=Env.ZERO;
        private BigDecimal nutbdx=Env.ZERO;
        private BigDecimal realnutbd=Env.ZERO;
        private  String ingnut="";
        private int prod_id=0;
        private  String pbaing="";
        private  String pbaplan="";
        private int ing_id=0;
        private int plant_id=0;
        private int insatt_id=0;
        private BigDecimal rn=Env.ZERO;
        private ArrayList m_editor= new ArrayList();
        private BigDecimal vol=Env.ZERO;
        private BigDecimal convrate=Env.ZERO;
        private BigDecimal min_id=Env.ZERO;
        private BigDecimal max_id=Env.ZERO;
        private BigDecimal preciototal=Env.ZERO;
        private ArrayList minmaxing = new ArrayList();
        private int   m_instance=0;
        private Vector acalc = new Vector();
        private Vector aunico = new Vector();
        private Vector nutarr1 = new Vector();
        //private Vector solve = new Vector();
        //private ArrayList selectedarr = new ArrayList();
        private boolean m_exist3= false;
        private boolean parcial= false;
        private Vector solution = new Vector();
        // private javax.swing.JButton jButton12;


        static class Solution
        {
            Solution (String k , BigDecimal q)
            {
                this.key =  k;
                this.qty = q;
            }

            private String key = null;
            private BigDecimal qty = Env.ZERO;

            public String getKey()
            {
                return key;
            }
            public void setKey(String k)
            {
                this.key=k;
            }

            public BigDecimal getQty()
            {
                return qty;
            }
            public void setQty(BigDecimal q)
            {
                this.qty=q;
            }

            private static  Vector factory(String s)
            {
                Vector solutions =  new Vector();
                String k = "";
                String q = "";
                String key = "";
                String solve  = s.substring(s.indexOf("("));
                boolean open = false;
                for (int i = 0 ; i < solve .length() ;i++)
                {

				char c = solve.charAt(i);
				if (c != ',')
                                {
                                    if (c == '(')
                                    {

                                        open = true;
                                        key = "";
                                        continue;
                                    }
                                    if  (c == ')')
                                    {

                                        key = k ;
                                        //System .out.println("   Key:" + key);
                                        k = "";
                                        open = false;
                                        continue;
                                    }

                                    if (open)
                                    {
                                         k = k + c;
                                         continue;
                                    }
                                    else
                                    {
                                         q = q + c;
                                         continue;

                                    }

                                }
                                else if (c == ',')
                                {
                                    if(!q.equals(""))
                                    {
                                     Solution o = new Solution(key,new BigDecimal(q));
                                     solutions.add(o);
                                    //System .out.println("---------" + q);
                                    }

                                    q = "";
                                }


                                //System.out.println("Key" + key);
                }
                Solution o = new Solution(key,new BigDecimal(q));
                solutions.add(o);
                return solutions;
            }
        }

}
