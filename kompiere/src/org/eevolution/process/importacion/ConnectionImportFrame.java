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
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.db.CConnection;
import org.compiere.swing.CPanel;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.eevolution.process.importacion.ImportPrintFormat;
import org.eevolution.process.importacion.ImportProcess;
import org.eevolution.process.importacion.ImportReportAndProcess;
import org.eevolution.process.importacion.ImportWindow;

/*import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.compiere.db.CConnection;
import org.compiere.util.DB;
import org.compiere.util.Trx;
import org.eevolution.process.ImportPrintFormat;

/**
 *
 * @author santiago
 */
public class ConnectionImportFrame extends CPanel implements FormPanel{

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

    private String datoImportar;

    /** Creates new form ImportarPrintFormat */
    public ConnectionImportFrame() {
        initComponents();
        lBarra.setForeground(this.getBackground());
        try {
            Thread.currentThread().sleep(2);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectionImportFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        String bd = DB.getDatabase().getSchema();
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
        lPrintFormat = new javax.swing.JLabel();
        cbPrintFormat = new javax.swing.JComboBox();
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
        cbPrintFormat.setPreferredSize(new java.awt.Dimension(200, 20));
        pbBarra.setPreferredSize(new java.awt.Dimension(163, 20));
        //pbBarra.setVisible(false);
        /*setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Importacion de Print Formats");
        setResizable(false);*/

        jLabel1.setText("Host");

        jLabel2.setText("Puerto");

        jLabel3.setText("Base de Datos");

        jLabel4.setText("Usuario");

        jLabel5.setText("Contraseña");

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

        lPrintFormat.setText(datoImportar);
        lPrintFormat.setEnabled(false);

        cbPrintFormat.setDoubleBuffered(true);
        cbPrintFormat.setEnabled(false);

        bImportar.setText("Importar");
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
        mainPanel.add(lPrintFormat,    new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(cbPrintFormat,    new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        mainPanel.add(bImportar,    new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        /*mainPanel.add(lBarra,    new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
                ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        /*mainPanel.add(pbBarra,    new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
                ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));*/
        //pbPanel.add(lBarra,"South");
        pbPanel.add(lBarra);
        pbPanel.add(pbBarra,"South");
        //org.compiere.print.layout
        /*org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(bTestConexion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 179, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, pbBarra, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(lBarra)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 26, Short.MAX_VALUE)
                        .add(bImportar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 127, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel3)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel2)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel4)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel5))
                        .add(13, 13, 13)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, tHost, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, tPort, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(tUsuario, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                                    .add(tBd, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                                    .add(tPassword, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)))))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(lPrintFormat)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cbPrintFormat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 237, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(tHost, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(tPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(tBd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(tUsuario, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(tPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(bTestConexion, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(42, 42, 42)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lPrintFormat)
                    .add(cbPrintFormat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(bImportar)
                    .add(lBarra))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pbBarra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-360)/2, (screenSize.height-337)/2, 360, 337);*/
    }// </editor-fold>//GEN-END:initComponents

    private void bTestConexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTestConexionActionPerformed
        // TODO add your handling code here:
        pbBarra.setVisible(true);
        jLabel5.setEnabled(false);
        try {
                    Thread.currentThread().sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConnectionImportFrame.class.getName()).log(Level.SEVERE, null, ex);
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
                lPrintFormat.setEnabled(true);
                cbPrintFormat.setEnabled(true);
                bImportar.setEnabled(true);
                tBd.setEnabled(false);
                tHost.setEnabled(false);
                tPassword.setEnabled(false);
                tPort.setEnabled(false);
                tUsuario.setEnabled(false);
                bTestConexion.setEnabled(false);
                jLabel1.setEnabled(false);
                jLabel2.setEnabled(false);
                jLabel3.setEnabled(false);
                jLabel4.setEnabled(false);
                //CConnection c2 = CConnection.get("Oracle", tHost.getText(), 1521, DB.getDatabase().getName(), DB.getDatabase().get, tPassword.getText());
                //conexionInicial = c2.getConnection(true, Connection.TRANSACTION_READ_COMMITTED);
                initCBDatosAImportar(getDatosAImportar());
                //actualizarBarra();
            }
            catch(NumberFormatException ex){
                JOptionPane.showConfirmDialog(null, "El puerto ingresado no es valido", "Puerto invalido", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception e){

            }
        }
}//GEN-LAST:event_bTestConexionActionPerformed

    private void bImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bImportarActionPerformed
        // TODO add your handling code here:
        ImportProcess proceso = null;
        
        Trx trx = Trx.get("Importar", true);
        
   		if (nombreVentana.equals(I_WINDOWS))
			proceso = new ImportWindow(conexionInicial, conexionFuente,(String) cbPrintFormat.getSelectedItem());
        else if (nombreVentana.equals(I_PRINTFORMAT))
        	proceso = new ImportPrintFormat(conexionInicial,conexionFuente,(String) cbPrintFormat.getSelectedItem());
        else if (nombreVentana.equals(I_PROCESS))
            proceso = new ImportReportAndProcess(conexionInicial,conexionFuente,(String) cbPrintFormat.getSelectedItem());
        proceso.setTrxName(trx.getTrxName());
        String rta = proceso.procesar();
        pbBarra.setValue(0);
        try {
            Thread.sleep(2);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectionImportFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rta.equals("ok")){
            JOptionPane.showMessageDialog(this, "La importaci�n se ha realizado con �xito", "Importacion exitosa", JOptionPane.INFORMATION_MESSAGE);
            trx.commit();
        }
        else{
            JOptionPane.showMessageDialog(this, rta, "Error en la importacion", JOptionPane.ERROR_MESSAGE);
            trx.rollback();
        }
        trx.close(); 
    }//GEN-LAST:event_bImportarActionPerformed

    /** BISion - 06/10/2009 - Santiago Ibañez
     * Ingresa los items del array list dado en el combo box
     * @param printFormats
     */
    private void initCBDatosAImportar(ArrayList<String> printFormats){
        Iterator it = printFormats.iterator();
        while (it.hasNext()){
            cbPrintFormat.addItem(it.next());
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
                Logger.getLogger(ConnectionImportFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
    }

    /**
     * Metodo que retorna los print formats existentes en la conexion Fuente que
     * no están en la conexion destino
     * @return
     */
    private ArrayList<String> getDatosAImportar() throws Exception{
        ArrayList<String> printFormats = new ArrayList<String>();
        try {
            String sql = "select "+columnName+" from "+ tableName + " WHERE AD_Client_ID = " + Env.getAD_Client_ID(Env.getCtx());
            PreparedStatement ps = conexionFuente.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();
            rs.last();
            int longitud = rs.getRow();
            rs.first();
            int i=1;
            int j=1;
            while (rs.next()){
                if (!existeDatoImportar(tableName,columnName,rs.getString(1)))
                    printFormats.add(rs.getString(1));
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
                    Logger.getLogger(ConnectionImportFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //tlb.setIsActive(false);
            //tlb.beforeStop();
            pbBarra.setValue(100);
            pbBarra.paintImmediately(pbBarra.getVisibleRect());
            lBarra.setForeground(this.getBackground());
            lBarra.paintImmediately(lBarra.getVisibleRect());
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionImportFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return printFormats;
    }

    /**
     * Metodo para saber si existe un print format dado un nombre
     * @param name
     * @return
     */
    private boolean existeDatoImportar(String tableName, String columnName, String name){
        boolean ret = false;
        try {
            String sql = "select * from "+ tableName +" where "+columnName+" = ? order by name asc";
            //System.out.println(sql);
            PreparedStatement ps = conexionInicial.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
              ret = true;
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionImportFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
     return ret;
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConnectionImportFrame().setVisible(true);
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
                    Logger.getLogger(ConnectionImportFrame.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JComboBox cbPrintFormat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lBarra;
    private javax.swing.JLabel lPrintFormat;
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

    private static String I_PRINTFORMAT = "Importar Print Format";
    private static String I_PROCESS = "Importar Report And Process";
    private static String I_WINDOWS = "Importar Windows Tab and Field";
    
    public void init(int WindowNo, FormFrame frame) {
        nombreVentana = Env.getContext(Env.getCtx(), WindowNo, "WindowName");
        if (nombreVentana.equals(I_PROCESS)){
            tableName = "ad_process";
            columnName = "value";
            tituloBoton = I_PROCESS;
            bTestConexion.setText(tituloBoton);
        }
        else if (nombreVentana.equals(I_PRINTFORMAT)){
            tableName = "ad_printformat";
            columnName = "name";
            tituloBoton = I_PRINTFORMAT;
            
            bTestConexion.setText(tituloBoton);
        }
        else if (nombreVentana.equals(I_WINDOWS)){
        	tableName = "ad_window";
            columnName = "name";
            tituloBoton = I_WINDOWS;    
        }
        bTestConexion.setText(tituloBoton);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.getContentPane().add(pbPanel, BorderLayout.SOUTH);
    }

}