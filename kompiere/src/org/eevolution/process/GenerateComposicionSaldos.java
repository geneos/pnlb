/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import org.compiere.process.*;
import java.math.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.*;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import org.compiere.model.*;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;
/**
 *Esta clase inserta tuplas en la tabla temporal T_COMPOSICION_SALDOS luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 * @author BISion
 */
public class GenerateComposicionSaldos extends SvrProcess{

    private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    private Long partner;    
    private String comprobantes;
    SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
    private double saldototal;
    private int page;
    boolean primero = true;
        
    private boolean soloClientesC=false;
    private boolean soloProveedoresC=false;
    private FrameSolo soloCliProv=null;
   
    
    private class FrameSolo extends JFrame{
        
        private JCheckBox soloClientes=null;
        private JCheckBox soloProveedores=null;
        private JPanel panelUp=null;
        private JPanel panelDown=null;
        private JButton  acept= null;
        private JButton cancel=null;
        private FrameSolo(){
            String curDir = System.getProperty("user.dir");
            String dir;
            Toolkit tk = Toolkit.getDefaultToolkit();
            soloClientes= new JCheckBox("Solo Clientes",true);
            soloClientesC=true;
            soloProveedoresC=false;
            soloProveedores= new JCheckBox("Solo Proveedores",false);
            soloClientes.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    
                    soloProveedores.setSelected(false);
                    soloClientesC=true;
                    soloProveedoresC=false;
                    
                }
            });
            soloProveedores.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    
                    soloClientes.setSelected(false);
                    soloProveedoresC=true;
                    soloClientesC=false;
                    
                }
            });
            panelUp= new JPanel();
            panelUp.setSize(510,170);
            panelUp.setLayout(new GridLayout(2,1));
            panelUp.setBackground(new Color(235,235,230));
            
            panelUp.add(soloClientes);
            panelUp.add(soloProveedores);
            
            panelDown=new JPanel();
            panelDown.setSize(510,10);
            FlowLayout f =new FlowLayout(FlowLayout.RIGHT);
            panelDown.setLayout(f);
            panelDown.setBackground(new Color(235,235,230));
            panelDown.setOpaque(true);
            
            acept=new JButton();
            dir=curDir+"\\client\\Src\\org\\compiere\\images\\ok24.gif";
            acept.setIcon(new ImageIcon(dir));
            
            
            cancel=new JButton();
            dir=curDir+"\\client\\Src\\org\\compiere\\images\\cancel24.gif";
            cancel.setIcon(new ImageIcon(dir));
           
           
            f.setVgap(25);
            panelDown.add(cancel);
            panelDown.add(acept);
            
            this.setTitle("Compiere - Informacion del socio del negocio");
            this.setLayout(new GridLayout(2,1));
            this.setSize(510, 180);
            this.setResizable(false);
            this.setLocation( 500,500 );
            
           
            curDir=curDir+"\\client\\Src\\org\\compiere\\images\\Process24.gif";
            
	    this.setIconImage(tk.getImage(curDir));
            
            this.add(panelUp);
            this.add(panelDown);
            this.setVisible(true);
            
            
        }
        
        protected JButton getAcept(){
            return acept;
        
        }
        
        protected JButton getCancel(){
            return cancel;
        
        }      
    
    } 

    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++)
	{
            String name = para[i].getParameterName();
            if(name.equals("COMPROBANTE"))
                comprobantes = (String) para[i].getParameter();                
            else{
                if(name.equals("C_BPARTNER_ID")){
                    partner = ((BigDecimal) para[i].getParameter()).longValue();
                   
                }else{
                    fromDate=(Timestamp)para[i].getParameter();
                    toDate=(Timestamp)para[i].getParameter_To();
                    }
            }    
                        
	}
         
        p_PInstance_ID = getAD_PInstance_ID();
    }

    protected String doIt() throws Exception {
        page = 1;
        log.info("Comienzo del proceso de composición de saldos");
        log.info("borrado de la tabla temporal T_COMPOSICION_SALDOS");
        String sqlRemove = "delete from T_COMPOSICION_SALDOS";
        DB.executeUpdate(sqlRemove, null);
        log.info("borrado de la tabla temporal T_COMPOSICION_SALDO_TMP");
        sqlRemove = "delete from T_COMPOSICION_SALDO_TMP";
        DB.executeUpdate(sqlRemove, null);
        if ( partner == null ){
            soloCliProv=new FrameSolo();
            soloCliProv.getCancel().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                  soloCliProv.dispose();
                }
            });
            soloCliProv.getAcept().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    try {
                        if ( soloClientesC || soloProveedoresC ){    
                            String sql;
                            saldototal = 0;
                            if (soloClientesC)  sql = "select c_bpartner_id,name from c_bpartner where iscustomer='Y' and isactive='Y' order by name";
                            else sql = "select c_bpartner_id,name from c_bpartner where isvendor='Y' and isactive='Y' order by name";
                            
                            PreparedStatement pstmt = DB.prepareStatement(sql);
                            ResultSet rs = pstmt.executeQuery();
                            while (rs.next()) {
                                saldototal = 0;
                                saldoInicial(rs.getLong(1));
                                facturas(rs.getLong(1));
                                pagos(rs.getLong(1));
                                composicion(rs.getLong(1),rs.getString(2));
                            }
                            UtilProcess.initViewer("Composición de saldos",p_PInstance_ID,getProcessInfo());
                        }
                        } catch (SQLException ex) {
                            Logger.getLogger(GenerateComposicionSaldos.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }                 
              });              
        }
        else{
            String name ="";
            try{
                String sql = "select name from c_bpartner where c_bpartner_id="+partner.longValue();
                PreparedStatement pstmt = DB.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()){name = rs.getString(1);}
            } catch (SQLException ex) {Logger.getLogger(GenerateComposicionSaldos.class.getName()).log(Level.SEVERE, null, ex);}
            saldototal = 0;
            saldoInicial(partner);
            facturas(partner);
            pagos(partner);            
            composicion(partner,name);
            UtilProcess.initViewer("Composición de saldos",p_PInstance_ID,getProcessInfo());
        }       
        return "sucess";
    }
    
    //***************************
    protected void facturas(long patnerID){
        try {
          PreparedStatement pstmt = null,pstmtInsert;
          String sqlQuery="",sqlInsert="";
          ResultSet rs;
          double importe=0;
          if(comprobantes.equals("Y")){
              log.info("Consulta de la vista RV_COMP_SALDO_FACTURAS, se filtra por el rango indicado y socio del negocio");
              sqlQuery = "select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,DATEINVOICED,C_DOCTYPE_ID,C_INVOICE_ID,C_BPARTNER_ID,C_PAYMENT_ID,TIPO,DOCUMENTNO,NETDAYS,MONEDA,IMPORTE from RV_COMP_SALDO_FACTURAS"+
                         " where ( dateinvoiced between ? and ? ) and C_BPARTNER_ID=" + patnerID + " order by DATEINVOICED,TIPO,DOCUMENTNO";
              // Realice la consulta y guardo los resultados en la tabla
              pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
              pstmt.setTimestamp(1, fromDate);
              pstmt.setTimestamp(2, toDate);
              rs = pstmt.executeQuery();
              while (rs.next()) {
                        if(rs.getLong(5)==1000131 || rs.getLong(5)==1000204 || rs.getLong(5)==1000205 || rs.getLong(5)==1000170 || rs.getLong(5)==1000174 || rs.getLong(5)==1000175 || rs.getLong(5)==1000197 ||
                                 rs.getLong(5)==1000177 || rs.getLong(5)==1000135 || rs.getLong(5)==1000187 || rs.getLong(5)==1000188 || rs.getLong(5)==1000199)
                            importe=rs.getDouble(13);                            
                        else                            
                            importe = -rs.getDouble(13);
                        sqlInsert = "insert into T_COMPOSICION_SALDO_TMP values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                        pstmtInsert.setLong(1, rs.getLong(1));
                        pstmtInsert.setLong(2, rs.getLong(2));
                        pstmtInsert.setString(3, rs.getString(3));
                        pstmtInsert.setLong(4, p_PInstance_ID);
                        pstmtInsert.setDate(5, new Date(rs.getDate(4).getTime() + 1000));
                        pstmtInsert.setString(6, rs.getString(9));
                        pstmtInsert.setString(7, rs.getString(10));
                        // Para el vencimiento
                        Calendar timeRestante = Calendar.getInstance();
                        timeRestante.setTimeInMillis(rs.getDate(4).getTime());
                        timeRestante.add(Calendar.DATE, rs.getInt(11));
                        Date date = new Date(timeRestante.getTimeInMillis());
                        pstmtInsert.setDate(8, date);
                        // fin calculo dias
                        pstmtInsert.setString(9, rs.getString(12));
                        pstmtInsert.setDouble(10, rs.getDouble(13));
                        // calculo de la mora
                        Long resta =  toDate.getTime() - timeRestante.getTimeInMillis();
                        double dias = Math.floor(resta / (1000 * 60 * 60 * 24));
                        pstmtInsert.setInt(11, (int)dias);                
                        // fin calculo de mora
                        
                        pstmtInsert.setDouble(12, importe);
                        pstmtInsert.setLong(13, patnerID);
                        pstmtInsert.setString(14, comprobantes);
                        
                        String fecha = "";
                        if(rs.getDate(4).getDate()<10) fecha = "0"+rs.getDate(4).getDate(); else fecha += rs.getDate(4).getDate();
                        if(rs.getDate(4).getMonth()+1 < 10)  fecha+= "/0"+(rs.getDate(4).getMonth()+1); else fecha+= "/"+(rs.getDate(4).getMonth()+1);
                        fecha +="/"+(rs.getDate(4).getYear()+1900);
                        pstmtInsert.setString(15, fecha);
                        pstmtInsert.executeQuery();
                        DB.commit(true, get_TrxName());
                        pstmtInsert.close();                        
                }
              pstmt.close();rs.close();
          }
          else{
              sqlQuery = "select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,DATEINVOICED,C_DOCTYPE_ID,C_INVOICE_ID,C_BPARTNER_ID,C_PAYMENT_ID,TIPO,DOCUMENTNO,NETDAYS,MONEDA,IMPORTE from RV_COMP_SALDO_FACTURAS RV "+
                    "where (dateinvoiced between ? and ?) and RV.C_BPARTNER_ID=? and c_invoice_id not in "+ 
                    "(select CA.c_invoice_id from C_ALLOCATIONLINE CA,C_PAYMENT CP where RV.C_INVOICE_ID=CA.C_INVOICE_ID and CP.C_PAYMENT_ID=CA.C_PAYMENT_ID)"+
                    " order by DATEINVOICED,TIPO,DOCUMENTNO";
              pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
              pstmt.setTimestamp(1, fromDate);
              pstmt.setTimestamp(2, toDate);
              pstmt.setLong(3, patnerID);
              rs = pstmt.executeQuery();
              while(rs.next()){ 
                        if(rs.getLong(5)==1000131 || rs.getLong(5)==1000204 || rs.getLong(5)==1000205 || rs.getLong(5)==1000170 || rs.getLong(5)==1000174 || rs.getLong(5)==1000175 || rs.getLong(5)==1000197 ||
                                 rs.getLong(5)==1000177 || rs.getLong(5)==1000135 || rs.getLong(5)==1000187 || rs.getLong(5)==1000188 || rs.getLong(5)==1000199)
                            importe=rs.getDouble(13);
                         else
                            importe = -rs.getDouble(13); 
                        
                        sqlInsert = "insert into T_COMPOSICION_SALDO_TMP values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                        pstmtInsert.setLong(1, rs.getLong(1));
                        pstmtInsert.setLong(2, rs.getLong(2));
                        pstmtInsert.setString(3, rs.getString(3));
                        pstmtInsert.setLong(4, p_PInstance_ID);
                        pstmtInsert.setDate(5, new Date(rs.getDate(4).getTime() + 1000));
                        pstmtInsert.setString(6, rs.getString(9));
                        pstmtInsert.setString(7, rs.getString(10));
                        // Para el vencimiento
                        Calendar timeRestante = Calendar.getInstance();
                        timeRestante.setTimeInMillis(rs.getDate(4).getTime());
                        timeRestante.add(Calendar.DATE, rs.getInt(11));
                        Date date = new Date(timeRestante.getTimeInMillis());
                        pstmtInsert.setDate(8, date);
                        // fin calculo dias
                        pstmtInsert.setString(9, rs.getString(12));
                        pstmtInsert.setDouble(10, rs.getDouble(13));
                        // calculo de la mora
                        Long resta =  toDate.getTime() - timeRestante.getTimeInMillis();
                        double dias = Math.floor(resta / (1000 * 60 * 60 * 24));
                        pstmtInsert.setInt(11, (int)dias);                
                        // fin calculo de mora
                        pstmtInsert.setDouble(12, importe);
                        pstmtInsert.setLong(13, patnerID);
                        pstmtInsert.setString(14, comprobantes);
                        
                        String fecha = "";
                        if(rs.getDate(4).getDate()<10) fecha = "0"+rs.getDate(4).getDate(); else fecha += rs.getDate(4).getDate();
                        if(rs.getDate(4).getMonth()+1 < 10)  fecha+= "/0"+(rs.getDate(4).getMonth()+1); else fecha+= "/"+(rs.getDate(4).getMonth()+1);
                        fecha +="/"+(rs.getDate(4).getYear()+1900);
                        pstmtInsert.setString(15, fecha);
                        pstmtInsert.executeQuery();
                        DB.commit(true, get_TrxName());
                        pstmtInsert.close();
                }
              pstmt.close();rs.close();
          }
        } catch (SQLException ex) {
            Logger.getLogger(GenerateComposicionSaldos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //***************************
    protected void pagos(long patnerID){
        try {
            String sqlInsert = "",sqlQuery="";
            PreparedStatement pstmtInsert,pstmt;
            ResultSet rs;
            double importe=0;
            if(comprobantes.equals("Y")){
            log.info("Consulta de la vista RV_COMP_SALDO_PAGOS, se filtra por el rango indicado y socio del negocio");
            sqlQuery = "select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,DATETRX,C_DOCTYPE_ID,C_BPARTNER_ID,C_PAYMENT_ID,TIPO,DOCUMENTNO,MONEDA,IMPORTE from RV_COMP_SALDO_PAGOS" +
                                " where ( DATETRX between ? and ? ) and C_BPARTNER_ID=" + patnerID + " order by DATETRX,TIPO,DOCUMENTNO";
            // Realice la consulta y guardo los resultados en la tabla
            pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if(rs.getLong(5)==1000138)
                    importe = rs.getDouble(11);                    
                else
                    importe = -rs.getDouble(11);
                    
                sqlInsert = "insert into T_COMPOSICION_SALDO_TMP values(?,?,?,?,?,?,?,null,?,?,null,?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                pstmtInsert.setLong(1, rs.getLong(1));
                pstmtInsert.setLong(2, rs.getLong(2));
                pstmtInsert.setString(3, rs.getString(3));
                pstmtInsert.setLong(4, p_PInstance_ID);
                pstmtInsert.setDate(5, new Date(rs.getDate(4).getTime() + 1000));
                pstmtInsert.setString(6, rs.getString(8));
                pstmtInsert.setString(7, rs.getString(9));                
                pstmtInsert.setString(8, rs.getString(10));
                pstmtInsert.setDouble(9, rs.getDouble(11));                
                
                pstmtInsert.setDouble(10, importe);
                pstmtInsert.setLong(11, patnerID);
                pstmtInsert.setString(12, comprobantes);
                
                String fecha = "";
                if(rs.getDate(4).getDate()<10) fecha = "0"+rs.getDate(4).getDate(); else fecha += rs.getDate(4).getDate();
                if(rs.getDate(4).getMonth()+1 < 10)  fecha+= "/0"+(rs.getDate(4).getMonth()+1); else fecha+= "/"+(rs.getDate(4).getMonth()+1);
                fecha +="/"+(rs.getDate(4).getYear()+1900);
                pstmtInsert.setString(13, fecha);
                pstmtInsert.executeQuery();                
                DB.commit(true, get_TrxName());
                pstmtInsert.close();
            }
            pstmt.close();rs.close();
            }else{
                sqlQuery = "select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,DATETRX,C_DOCTYPE_ID,C_BPARTNER_ID,C_PAYMENT_ID,TIPO,DOCUMENTNO,MONEDA,IMPORTE from RV_COMP_SALDO_PAGOS RV"+
                            " where ( DATETRX between ? and ? ) and C_BPARTNER_ID=? and c_payment_id not in "+
                            "(select CA.c_payment_id from C_ALLOCATIONLINE CA,C_INVOICE CP where CP.C_INVOICE_ID=CA.C_INVOICE_ID and RV.C_PAYMENT_ID=CA.C_PAYMENT_ID)"+
                            " order by DATETRX,TIPO,DOCUMENTNO";
                pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
                pstmt.setTimestamp(1, fromDate);
                pstmt.setTimestamp(2, toDate);
                pstmt.setLong(3, patnerID);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if(rs.getLong(5)==1000138)
                        importe = rs.getDouble(11);
                        //saldototal+=rs.getDouble(11);
                    else
                        importe = -rs.getDouble(11);
                    sqlInsert = "insert into T_COMPOSICION_SALDO_TMP values(?,?,?,?,?,?,?,null,?,?,null,?,?,?,?)";
                    pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                    pstmtInsert.setLong(1, rs.getLong(1));
                    pstmtInsert.setLong(2, rs.getLong(2));
                    pstmtInsert.setString(3, rs.getString(3));
                    pstmtInsert.setLong(4, p_PInstance_ID);
                    pstmtInsert.setDate(5, new Date(rs.getDate(4).getTime() + 1000));
                    pstmtInsert.setString(6, rs.getString(8));
                    pstmtInsert.setString(7, rs.getString(9));                
                    pstmtInsert.setString(8, rs.getString(10));
                    pstmtInsert.setDouble(9, rs.getDouble(11));                
                    pstmtInsert.setDouble(10, importe);
                    pstmtInsert.setLong(11, patnerID);
                    pstmtInsert.setString(12, comprobantes);
                    
                    String fecha = "";
                    if(rs.getDate(4).getDate()<10) fecha = "0"+rs.getDate(4).getDate(); else fecha += rs.getDate(4).getDate();
                    if(rs.getDate(4).getMonth()+1 < 10)  fecha+= "/0"+(rs.getDate(4).getMonth()+1); else fecha+= "/"+(rs.getDate(4).getMonth()+1);
                    fecha +="/"+(rs.getDate(4).getYear()+1900);
                    pstmtInsert.setString(13, fecha);
                    pstmtInsert.executeQuery();
                    DB.commit(true, get_TrxName());
                    pstmtInsert.close();
                }
                pstmt.close();rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenerateComposicionSaldos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //*****************************
    protected void saldoInicial(long patnerID){
        try {
            //if(comprobantes.equals("Y")){
            // saldo anterior para las facturas de cliente y proveedor
                if(patnerID == 1002118)
                    System.out.println();
                String cliente,proveedor,name="";
                if(soloClientesC) {cliente="Y";proveedor="N";} else {cliente="N";proveedor="Y";}
                String sqlQuery = "select C_DOCTYPE_ID,IMPORTE,SOCIO from RV_COMP_SALDO_FACTURAS" + " where ( dateinvoiced < ? ) and C_BPARTNER_ID=" + patnerID;
                PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
                pstmt.setTimestamp(1, fromDate);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (rs.getLong(1) == 1000131 || rs.getLong(1) == 1000204 || rs.getLong(1) == 1000205 || rs.getLong(1) == 1000170 || rs.getLong(1) == 1000174 || rs.getLong(1) == 1000175 || rs.getLong(1) == 1000197 || rs.getLong(1) == 1000177 || rs.getLong(1) == 1000135 || rs.getLong(1) == 1000187 || rs.getLong(1) == 1000188 || rs.getLong(1) == 1000199) {
                        saldototal += rs.getDouble(2);
                    } else if (rs.getLong(1) == 1000133 || rs.getLong(1) == 1000206 || rs.getLong(1) == 1000172 || rs.getLong(1) == 1000207 || rs.getLong(1) == 1000203 || rs.getLong(1) == 1000171 || rs.getLong(1) == 1000208 || rs.getLong(1) == 1000173 || rs.getLong(1) == 1000178 || rs.getLong(1) == 1000198 || rs.getLong(1) == 1000176 || rs.getLong(1) == 1000134 || rs.getLong(1) == 1000184 || rs.getLong(1) == 1000185 || rs.getLong(1) == 1000186 || rs.getLong(1) == 1000189 || rs.getLong(1) == 1000190 || rs.getLong(1) == 1000191 || rs.getLong(1) == 1000200) {
                        saldototal -= rs.getDouble(2);
                    }                    
                }
                pstmt.close();rs.close();
                // saldo anterior para los pagos
                String sqlQuery2 = "select C_DOCTYPE_ID,IMPORTE from RV_COMP_SALDO_PAGOS" +
                                    " where ( DATETRX < ? ) and C_BPARTNER_ID=" + patnerID;
                PreparedStatement pstmt2 = DB.prepareStatement(sqlQuery2, get_TrxName());
                pstmt2.setTimestamp(1, fromDate);
                ResultSet rs2 = pstmt2.executeQuery();
                while (rs2.next()) {
                    if(rs2.getLong(1)==1000138)
                        saldototal+=rs2.getDouble(2);
                    else
                        saldototal-=rs2.getDouble(2);            
                }
                rs2.close();pstmt2.close();
                // para el nombre del socio en caso de que las consultas anteriores den vacio
                String sqlQuery3 = "select name from  C_BPARTNER where C_BPARTNER_ID=" + patnerID;
                PreparedStatement pstmt3 = DB.prepareStatement(sqlQuery3);
                ResultSet rs3 = pstmt3.executeQuery();
                if(rs3.next()){name = rs3.getString(1);}
                String sqlInsert = "insert into T_COMPOSICION_SALDOS values("+getAD_Client_ID()+",1000033,'Y',"+p_PInstance_ID+",?,null,null,null,null,null,null,"+saldototal+","+patnerID+",'"+comprobantes+"',"+page+",null,'"+cliente+"','"+proveedor+"',?)";
                PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert,get_TrxName());
                pstmtInsert.setDate(1, new Date(fromDate.getTime() + 1000));
                pstmtInsert.setString(2, name);
                pstmtInsert.executeQuery();DB.commit(true, get_TrxName());
                page+=1;                
                pstmtInsert.close();rs3.close();pstmt3.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateComposicionSaldos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //****************************************
    protected void composicion(long patnerID, String name){
        try {
            String sqlInsert="",cliente,proveedor;
            String sql = "select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,AD_PINSTANCE_ID,TEMPDATE,TIPO_COMT,NRO_COMP,VENCIMIENTO,MONEDA,IMPORTE,MORA,SALDO,C_BPARTNER_ID,COMPROBANTE,FECHA from T_COMPOSICION_SALDO_TMP " + 
                    "WHERE C_BPARTNER_ID="+ patnerID +"ORDER BY TEMPDATE,TIPO_COMT,NRO_COMP";
            PreparedStatement pstmt = DB.prepareStatement(sql);
            PreparedStatement pstmtInsert=null;
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                        sqlInsert = "insert into T_COMPOSICION_SALDOS values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());        
                        saldototal+=rs.getDouble(12);
                        pstmtInsert.setLong(1, rs.getLong(1));
                        pstmtInsert.setLong(2, rs.getLong(2));
                        pstmtInsert.setString(3, rs.getString(3));
                        pstmtInsert.setLong(4, rs.getLong(4));
                        pstmtInsert.setDate(5, rs.getDate(5));
                        pstmtInsert.setString(6, rs.getString(6));
                        pstmtInsert.setString(7, rs.getString(7));
                        pstmtInsert.setDate(8, rs.getDate(8));
                        pstmtInsert.setString(9, rs.getString(9));
                        pstmtInsert.setDouble(10, rs.getDouble(10));
                        pstmtInsert.setInt(11, rs.getInt(11));                
                        pstmtInsert.setDouble(12, saldototal);                        
                        pstmtInsert.setLong(13, rs.getLong(13));
                        pstmtInsert.setString(14, rs.getString(14));
                        pstmtInsert.setInt(15, page);
                        pstmtInsert.setString(16, rs.getString(15));
                        if(soloClientesC) {cliente="Y";proveedor="N";} else {cliente="N";proveedor="Y";}
                        pstmtInsert.setString(17, cliente);
                        pstmtInsert.setString(18, proveedor);
                        pstmtInsert.setString(19, name);
                        pstmtInsert.executeQuery();
                        DB.commit(true, get_TrxName());
                        page+=1;
                        pstmtInsert.close();
            }
            pstmt.close();rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateComposicionSaldos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
