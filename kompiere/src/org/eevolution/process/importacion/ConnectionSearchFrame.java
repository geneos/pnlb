/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ImportarPrintFormat.java
 *
 * Created on 05/10/2009, 10:22:47
 */

package org.eevolution.process.importacion;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;

import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.db.CConnection;
import org.compiere.swing.CFrame;
import org.compiere.swing.CPanel;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author daniel
 */
public class ConnectionSearchFrame extends CPanel implements FormPanel{

    //Conexión donde se van a insertar los datos (Destino)
    private Connection conexionInicial;

    //Coenxion de la BD desde donde se van a obtener los datos (Origen)
    private Connection conexionFuente;

    //Tabla desde la cual se va a importar datos
    private String tableName;

    //Columna por la cual se verifica existencia en BD destino
    private String columnName;

    //Titulo del boton
    private String tituloBoton;

    private String datoBuscar;
    
    private Hashtable<String, Hashtable> datosHash = new Hashtable<String, Hashtable>(); 

    /** Creates new form ImportarPrintFormat */
    public ConnectionSearchFrame() {
        initComponents();
        lBarra.setForeground(this.getBackground());
        try {
            Thread.currentThread().sleep(2);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectionSearchFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        //String bd = DB.getDatabase().getSchema();
        conexionInicial = DB.getConnectionRW();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    //@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tHost = new javax.swing.JTextField();
        tPort = new javax.swing.JTextField();
        tBd = new javax.swing.JTextField();
        tUsuario = new javax.swing.JTextField();
        bTestConexion = new javax.swing.JButton();
        lReportAProcess = new javax.swing.JLabel();
        cbReportAProcess = new javax.swing.JComboBox();
        bImportar = new javax.swing.JButton();
        tPassword = new javax.swing.JPasswordField();
        pbBarra = new javax.swing.JProgressBar();
        lBarra = new javax.swing.JLabel();

        tPort.setMaximumSize(new java.awt.Dimension(163, 20));
        tPort.setMinimumSize(new java.awt.Dimension(163, 20));
        tPort.setPreferredSize(new java.awt.Dimension(163, 20));
        tBd.setPreferredSize(new java.awt.Dimension(163, 20));
        tUsuario.setPreferredSize(new java.awt.Dimension(163, 20));
        tPassword.setPreferredSize(new java.awt.Dimension(163, 20));
        tHost.setPreferredSize(new java.awt.Dimension(163, 20));
        cbReportAProcess.setPreferredSize(new java.awt.Dimension(200, 20));
        pbBarra.setPreferredSize(new java.awt.Dimension(163, 20));
        
        jLabel1.setText("Host");
        jLabel2.setText("Puerto");
        jLabel3.setText("Base de Datos");
        jLabel4.setText("Usuario");
        jLabel5.setText("Contrase�a");

        tHost.setText("192.168.1.5");
        tPort.setText("1521");
        tPort.setDragEnabled(true);
        tBd.setText("produccionpa");
        tUsuario.setText("compiere");

        bTestConexion.setText(tituloBoton);
        bTestConexion.setIconTextGap(0);
        bTestConexion.setMaximumSize(new java.awt.Dimension(200, 25));
        bTestConexion.setMinimumSize(new java.awt.Dimension(200, 25));
        bTestConexion.setPreferredSize(new java.awt.Dimension(200, 25));
        bTestConexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTestConexionActionPerformed(evt);
            }
        });

        lReportAProcess.setText(datoBuscar);
        lReportAProcess.setEnabled(false);

        cbReportAProcess.setDoubleBuffered(true);
        cbReportAProcess.setEnabled(false);

        bImportar.setText("Mostrar");
        bImportar.setEnabled(false);
        bImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bImportarActionPerformed(evt);
            }
        });

        tPassword.setText("compiere");
        

        pbBarra.setStringPainted(true);

        lBarra.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lBarra.setText("Seleccionando...");

        mainPanel = new JPanel();
        mainPanel.setLayout(new java.awt.GridBagLayout());

        pbPanel = new JPanel();
        pbPanel.setLayout(new java.awt.BorderLayout());

        add(mainPanel, java.awt.BorderLayout.CENTER);
        mainPanel.add(jLabel1,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(tHost,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(jLabel2,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(tPort,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(jLabel3,    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(tBd,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(jLabel4,    new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(tUsuario,    new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(jLabel5,    new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(tPassword,    new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(bTestConexion,    new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(lReportAProcess,    new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(cbReportAProcess,    new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(bImportar,    new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        pbPanel.add(lBarra);
        pbPanel.add(pbBarra,"South");
    }// </editor-fold>//GEN-END:initComponents

    private void bTestConexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTestConexionActionPerformed
        // TODO add your handling code here:
        pbBarra.setVisible(true);
        jLabel5.setEnabled(false);
        try {
                Thread.currentThread().sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConnectionSearchFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
        //Compruebo que todos los campos tengan datos
        if (tBd.getText().equals("")||tHost.getText().equals("")||tPassword.getText().equals("")||tPort.getText().equals("")||tUsuario.getText().equals(""))
            JOptionPane.showConfirmDialog(null, "Algunos campos no tienen valores", "Error Parametros", JOptionPane.INFORMATION_MESSAGE);
        else{
            //Obtengo la conexion desde donde voy a extraer las OC.
            try{
                Integer port = new Integer(tPort.getText());
                CConnection c=CConnection.get("Oracle", tHost.getText(), port, tBd.getText(), tUsuario.getText(), tPassword.getText());
                conexionFuente = c.getConnection(true, Connection.TRANSACTION_READ_COMMITTED);
                lReportAProcess.setEnabled(true);
                cbReportAProcess.setEnabled(true);
                bImportar.setEnabled(true);
                tBd.setEnabled(false);
                tHost.setEnabled(false);
                tPassword.setEnabled(false);
                tPort.setEnabled(false);
                tUsuario.setEnabled(false);
                //bTestConexion.setEnabled(false);
                jLabel1.setEnabled(false);
                jLabel2.setEnabled(false);
                jLabel3.setEnabled(false);
                jLabel4.setEnabled(false);
                initCBDatosABuscar(getDatosABuscar());
            }
            catch(NumberFormatException ex){
                JOptionPane.showConfirmDialog(null, "El puerto ingresado no es valido", "Puerto invalido", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception e){
            }
        }
    }//GEN-LAST:event_bTestConexionActionPerformed
    private CFrame frm;
    private void bImportarActionPerformed(java.awt.event.ActionEvent evt)
    {	
        String itemName = (String) cbReportAProcess.getSelectedItem();
    	Hashtable ht = datosHash.get(itemName);
        pbBarra.setValue(0);
        try {
            Thread.sleep(2);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectionSearchFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        String diferencias = formatHashtable(ht,"");
        //JOptionPane.showMessageDialog(null, "Diferencias: \n" + diferencias , nombreVentana + ": " + itemName, JOptionPane.INFORMATION_MESSAGE);

        frm = new CFrame(nombreVentana + ": " + itemName);
        frm.setSize(700,500);
        //Calculate the frame location  
        Toolkit toolkit = Toolkit.getDefaultToolkit();  
        Dimension screenSize = toolkit.getScreenSize();  
        int x = (screenSize.width - frm.getWidth()) / 2;  
        int y = (screenSize.height - frm.getHeight()) / 2;  
         //Set the new frame location  
        frm.setLocation(x, y);  
        
        frm.setResizable(true);
        javax.swing.JButton submit = new javax.swing.JButton("Aceptar");
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	frm.dispose();
            }
        });
        
        javax.swing.JTextArea textArea = new javax.swing.JTextArea(25, 50);
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textArea.append("Diferencias: \n" + diferencias);
        //Lay out the content pane.
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setPreferredSize(new Dimension(700, 500));
        contentPane.add(scrollPane, BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.add(submit);
        contentPane.add(panel, BorderLayout.SOUTH);
        frm.setContentPane(contentPane);
        frm.pack();
        frm.setVisible(true);
    }
    
    private String formatHashtable(Hashtable ht, String level)
    {
    	String toString = "";
    	
    	for (Enumeration e = ht.keys(); e.hasMoreElements();)
    	{
    		String key = (String)e.nextElement();
    		Object obj = ht.get(key);
    		if (obj instanceof String)
    			toString += level + key + "= " + (String)obj + ";\n";
    		else
    			toString += level + key + ": \n" + formatHashtable((Hashtable)obj, level + "   -   ");
    	}
    	return toString;
    }

    /** BISion - 06/10/2009 - Santiago Iba�ez
     * Ingresa los items del array list dado en el combo box
     * @param printFormats
     */
    private void initCBDatosABuscar(ArrayList<String> datosToSearch){
    	//cbReportAProcess.removeAll();
    	Iterator it = datosToSearch.iterator();
        while (it.hasNext()){
            cbReportAProcess.addItem(it.next());
        }
    }

    private void actualizarBarra(){
      for (int i = 1; i <= 100; i++){
      final int percent = i;
            pbBarra.setValue(percent);
            pbBarra.paintImmediately(pbBarra.getVisibleRect());

            try {
                Thread.currentThread().sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectionSearchFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
    }

    /**
     * Metodo que retorna los print formats existentes en la conexion Fuente que
     * no están en la conexion destino
     * @return
     */
    private ArrayList<String> getDatosABuscar() throws Exception{
        ArrayList<String> datosToSearch = new ArrayList<String>();
        try {
        	String sql = "select * from " + tableName + " Where AD_Client_ID = " + Env.getAD_Client_ID(Env.getCtx()) + " Order by " + columnName;
            PreparedStatement psFuente = conexionFuente.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsFuente = psFuente.executeQuery();
            PreparedStatement psInicial = conexionInicial.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInicial = psInicial.executeQuery();
            rsInicial.last();
            int longitud = rsInicial.getRow();
            rsInicial.first();
            int i=1;
            int j=1;
            
            rsInicial.next();
            rsFuente.next();
            boolean continuar = true;
            if (rsInicial.isAfterLast() || rsFuente.isAfterLast())
            	continuar = false;
            while (continuar)
            {
                String rsI = rsInicial.getString(columnName);
                String rsF = rsFuente.getString(columnName);
            	int res = rsI.compareTo(rsF);
            	if (res==0)
            	{
            		Hashtable ht = null;
            		if (nombreVentana.equals(S_PROCESS))
            			ht = SearchReportAndProcess.procesar(conexionInicial,conexionFuente,rsInicial,rsFuente);
            		if (nombreVentana.equals(S_PRINTFORMAT))
            			ht = SearchPrintFormat.procesar(conexionInicial,conexionFuente,rsInicial,rsFuente);
            		if (nombreVentana.equals(S_WINDOWS))
            			ht = SearchWindowsTabAndField.procesar(conexionInicial,conexionFuente,rsInicial,rsFuente);
            		if (nombreVentana.equals(S_FORMS))
            			ht = SearchForm.procesar(conexionInicial,conexionFuente,rsInicial,rsFuente);
            		
            		if (ht!=null && !ht.isEmpty())
        			{	datosHash.put(rsInicial.getString(columnName), ht);
        				datosToSearch.add(rsInicial.getString(columnName));
        			}
            		rsInicial.next();
                    rsFuente.next();
                }
            	else
            		if (res<0)
                		rsInicial.next();
            		else
                   		rsFuente.next();
                	
                i = j*100/longitud;
                j++;
                final int k=i;
                pbBarra.setVisible(true);
                pbBarra.setValue(k);
                pbBarra.paintImmediately(pbBarra.getVisibleRect());
                if ((i/8) % 2 == 0){
                    Color color = new Color(51, 51, 51);
                    lBarra.setForeground(color);
                    lBarra.paintImmediately(lBarra.getVisibleRect());
                }
                else{
                    lBarra.setForeground(this.getBackground());
                    lBarra.paintImmediately(lBarra.getVisibleRect());
                }
                try {
                    Thread.currentThread().sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConnectionSearchFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            
                if (rsInicial.isAfterLast() || rsFuente.isAfterLast())
                	continuar = false;
            }
            
            pbBarra.setValue(100);
            pbBarra.paintImmediately(pbBarra.getVisibleRect());
            lBarra.setForeground(this.getBackground());
            lBarra.paintImmediately(lBarra.getVisibleRect());
            rsInicial.close();
            rsFuente.close();
            psInicial.close();
            psFuente.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionSearchFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return datosToSearch;
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConnectionSearchFrame().setVisible(true);
            }
        });
    }

    public void dispose() {
    }

    public class ThreadLabelBarra extends Thread{
        private boolean isActive = true;

        public ThreadLabelBarra(){
            lBarra.setVisible(true);
            lBarra.paintImmediately(lBarra.getVisibleRect());
        }

        public void run(){
            while (isActive){
                lBarra.setVisible(!lBarra.isVisible());
                lBarra.paintImmediately(lBarra.getVisibleRect());
                try {
                    Thread.sleep(800);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConnectionSearchFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        public void beforeStop(){
            lBarra.setVisible(true);
            lBarra.paintImmediately(lBarra.getVisibleRect());
        }
        public void setIsActive(boolean active){
            isActive = active;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bImportar;
    private javax.swing.JButton bTestConexion;
    private javax.swing.JComboBox cbReportAProcess;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lBarra;
    private javax.swing.JLabel lReportAProcess;
    private javax.swing.JProgressBar pbBarra;
    private javax.swing.JTextField tBd;
    private javax.swing.JTextField tHost;
    private javax.swing.JPasswordField tPassword;
    private javax.swing.JTextField tPort;
    private javax.swing.JTextField tUsuario;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel pbPanel;
    // End of variables declaration//GEN-END:variables

    private String nombreVentana;
    
    private static String S_PRINTFORMAT = "Buscar Print Format";
    private static String S_PROCESS = "Buscar Report And Process";
    private static String S_WINDOWS = "Buscar Windows Tab and Field";
    private static String S_FORMS = "Buscar Forms";

    public void init(int WindowNo, FormFrame frame) {
        nombreVentana = Env.getContext(Env.getCtx(), WindowNo, "WindowName");
        if (nombreVentana.equals(S_PRINTFORMAT)){
        	tableName = "ad_printformat";
            columnName = "name";
            tituloBoton = S_PRINTFORMAT;
        }
        else if (nombreVentana.equals(S_PROCESS)){
            tableName = "ad_process";
            columnName = "value";
            tituloBoton = S_PROCESS;
        }
        else if (nombreVentana.equals(S_WINDOWS)){
            tableName = "ad_window";
            columnName = "name";
            tituloBoton = S_WINDOWS;
        }
        else if (nombreVentana.equals(S_FORMS)){
            tableName = "AD_FORM";
            columnName = "name";
            tituloBoton = S_FORMS;
        }
        bTestConexion.setText(tituloBoton);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.getContentPane().add(pbPanel, BorderLayout.SOUTH);
    }

}