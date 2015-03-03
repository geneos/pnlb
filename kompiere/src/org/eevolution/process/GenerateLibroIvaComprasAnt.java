/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import org.compiere.process.*;
import java.math.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.*;
import org.compiere.model.*;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;
/**
 *  Esta clase inserta tuplas en la tabla temporal T_LIBRO_IVA_COMPRAS luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
 *	@author BISion
 *	@version 1.0
 */
public class GenerateLibroIvaComprasAnt extends SvrProcess{

    //Máximo que permite la base de datos para los campos String
	private static int N = 30;
    
	private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    private Long num_hoja;    
    SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
    private Long CLIENT_ID;
    private Long ORG_ID;
    double iva_parcial10,iva_parcial21,iva_parcial27,exentototal,noGravado;
    double credito10,credito21,credito27,iva_percep,ib_percep,gan_percep;
    private int page;
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        
        
	    for (int i = 0; i < para.length; i++)
		{
            String name = para[i].getParameterName();
            if(name.equals("NRO_HOJA")){
                num_hoja= ((BigDecimal) para[i].getParameter()).longValue();
                Env.getCtx().put("typePrint", "LIBRO");
                Env.getCtx().put("startPage", num_hoja);
            }
            else{
                fromDate=(Timestamp)para[i].getParameter();
                toDate=(Timestamp)para[i].getParameter_To();
            }
            p_PInstance_ID = getAD_PInstance_ID();    
		}
    }

    protected String doIt() {
        try {
            try {
                page=1;
                sector1();
                setearTemp();
                tablaBase();
                sector2();
            } catch (SQLException ex) {
                Logger.getLogger(GenerateLibroIvaVentas.class.getName()).log(Level.SEVERE, null, ex);
            }
            DB.commit(true, get_TrxName());
            UtilProcess.initViewer("Libro IVA Compras",p_PInstance_ID,getProcessInfo());
            
        } catch (SQLException ex) {
            Logger.getLogger(GenerateLibroIvaVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "success";
    }

    @SuppressWarnings("deprecation")
	protected void sector1(){
        try {
            iva_parcial10=0;iva_parcial21=0;iva_parcial27=0;exentototal=0;noGravado=0;
            credito10=0;credito21=0;credito27=0;iva_percep=0;ib_percep=0;gan_percep=0;
            String sqlRemove;
            String sqlQuery = "",sqlQuery3="",sqlQuery4="";
            StringBuffer sqlQuery2 = new StringBuffer();            
            PreparedStatement pstmt=null,pstmt2=null,pstmt3=null,pstmt4=null;
            PreparedStatement pstmtInsert = null;
            String sqlInsert;
            Long facturaId,doctype;
            double grabado=0,exento=0,ivacredito=0,ivaPercepcion=0,otros=0,total=0,iva_parcial,PERCEPCIONIVA=0,PERCEPCIONIB=0,impueso_iva,impuesto_exento;
            //auxiliares
            long AD_CLIENT_ID=0,AD_ORG_ID=0;
            Date DATEINVOICED = null;
            String ISACTIVE="",COMPROBANTE="",NRO_COMPROBANTE="",RAZON_SOCIAL="",CUIT="";
                      

            log.info("Comienzo del proceso de generación de LIBRO IVA DE COMPRAS");
            log.info("Borrado de la tabla temporal T_LIBRO_IVA_COMPRAS");
            sqlRemove = "Delete from T_LIBRO_IVA_COMPRAS";
            DB.executeUpdate(sqlRemove, null);
            boolean ok=false,esPercepcion=false;
            
            sqlQuery2.append("select distinct c_invoice_id,c_doctype_id,dateinvoiced,comprobante,nro_comprobante from rv_libro_iva_compras where (dateacct between ? and ?) order by dateinvoiced,comprobante,nro_comprobante");
            pstmt2 = DB.prepareStatement(sqlQuery2.toString(), get_TrxName());
            pstmt2.setTimestamp(1, fromDate);
            pstmt2.setTimestamp(2, toDate);
            ResultSet rs2 = pstmt2.executeQuery();
            while (rs2.next()) {
                ok=true;
                esPercepcion=false;
                facturaId = rs2.getLong(1);
                doctype = rs2.getLong(2);
                impueso_iva=0;impuesto_exento=0;
                PERCEPCIONIVA=0;PERCEPCIONIB=0;
                log.info("Consulta de la vista RV_LIBRO_IVA_COMPRAS, se filtra por el rango indicado");
                sqlQuery = "select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,C_TAX_ID,DATEINVOICED,C_DOCTYPE_ID,COMPROBANTE,NRO_COMPROBANTE,RAZON_SOCIAL,CUIT,NETLINE,C_INVOICE_ID from RV_LIBRO_IVA_COMPRAS" +
                            " where ( dateacct between ? and ? ) and C_INVOICE_ID="+facturaId;
                    // Realice la consulta y guardo los resultados en la tabla
                       
                    try {
                        pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
                        pstmt.setTimestamp(1, fromDate);
                        pstmt.setTimestamp(2, toDate);
                        ResultSet rs = pstmt.executeQuery();
                        while (rs.next()) {
                            try {                                
                                esPercepcion=false;
                                AD_CLIENT_ID = rs.getLong(1);
                                // para obtener el cliente y la organizacion
                                CLIENT_ID = rs.getLong(1);
                                ORG_ID = rs.getLong(2);
                                AD_ORG_ID = rs.getLong(2);
                                ISACTIVE = rs.getString(3);
                                DATEINVOICED = rs.getDate(5);
                                COMPROBANTE = rs.getString(7);
                                NRO_COMPROBANTE = rs.getString(8);
                                RAZON_SOCIAL = rs.getString(9);
                                CUIT = rs.getString(10);
                                
                                // para iva percepcion y otros impuestos
                                String sql 
                                	= " SELECT p.Amount, r.IMPUESTO "
                                	+ " FROM C_Invoice i "
                                	+ " INNER JOIN C_INVOICEPERCEP p ON (p.C_Invoice_ID=i.C_Invoice_ID) "
                                	+ " INNER JOIN C_REGPERCEP_RECIB r ON (p.C_REGPERCEP_RECIB_ID=r.C_REGPERCEP_RECIB_ID) "
                                	+ " WHERE i.C_Invoice_ID = ? ";
                                
                                PreparedStatement pstmSQL = DB.prepareStatement(sql, null);
                                pstmSQL.setLong(1, rs.getLong(12));
                                
                                ResultSet rsSQL = pstmSQL.executeQuery();
                                
                                while (rsSQL.next())
                                {
                                	esPercepcion=true;
                                	
                                	if (rsSQL.getString(2).equals("IVA"))
                                	{
                                		PERCEPCIONIVA+=rsSQL.getDouble(1);
                                        iva_percep+=PERCEPCIONIVA;
                                	}
                                	else
                                		if (rsSQL.getString(2).equals("Ganancias"))
                                		{
                                			PERCEPCIONIB+=rsSQL.getDouble(1);
                                			gan_percep+=PERCEPCIONIB; 
                                		}
                                		else
                                		{
                                			PERCEPCIONIB+=rsSQL.getDouble(1);
                                			ib_percep+=PERCEPCIONIB;
                                		}
                                }
                                
                                rsSQL.close();
                                pstmSQL.close();
                                
                                // para no cargar exento y no gravado en caso de que sea una percepcion
                                //if(!esPercepcion){
                                    if(rs.getLong(4)==1000052 || rs.getLong(4)==1000053 || rs.getLong(4)==1000054){
                                        impueso_iva+=rs.getDouble(11);
                                        if(rs.getLong(4)==1000052) iva_parcial21+=rs.getDouble(11); else
                                        if(rs.getLong(4)==1000053) iva_parcial27+=rs.getDouble(11); else
                                        if(rs.getLong(4)==1000054) iva_parcial10+=rs.getDouble(11);
                                    }
                                    else if(rs.getLong(4)==1000055 || rs.getLong(4)==1000056){
                                        impuesto_exento+=rs.getDouble(11);
                                        if(rs.getLong(4)==1000055) exentototal+=rs.getDouble(11); else
                                        if(rs.getLong(4)==1000056) noGravado+=rs.getDouble(11);    
                                    }
                                //}
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaCompras.doIt " + exception.getMessage());
                            }
                        }    
                        rs.close();
                        pstmt.close();
                       // para la columna Iva crédito fiscal
                       iva_parcial=0;
                       sqlQuery3 ="select (currencyConvert(c_invoicetax.taxamt,c_invoice.C_CURRENCY_ID,118,c_invoice.DATEINVOICED,c_invoice.C_CONVERSIONTYPE_ID, c_invoice.AD_CLIENT_ID,c_invoice.AD_ORG_ID)) as importe,c_tax_id from c_invoicetax "+ 
                                "join c_invoice on (c_invoicetax.C_INVOICE_ID = c_invoice.C_INVOICE_ID) where c_invoice.c_invoice_id="+facturaId;
                       pstmt3 = DB.prepareStatement(sqlQuery3);
                       ResultSet rs3 = pstmt3.executeQuery();
                       while(rs3.next()){
                            if(doctype==1000134 || doctype==1000184 || doctype==1000185 || doctype==1000186 || doctype==1000189 || doctype==1000190 || doctype==1000191 || doctype==1000200){
                                if(rs3.getLong(2)==1000052 || rs3.getLong(2)==1000053 || rs3.getLong(2)==1000054){
                                iva_parcial+=rs3.getDouble(1);
                                if(rs3.getLong(2)==1000052) credito21+=rs3.getDouble(1); else
                                if(rs3.getLong(2)==1000053) credito27+=rs3.getDouble(1); else
                                credito10+=rs3.getDouble(1);
                                }
                            }
                            else
                            {
                                if(rs3.getLong(2)==1000052 || rs3.getLong(2)==1000053 || rs3.getLong(2)==1000054){
                                    iva_parcial-=rs3.getDouble(1);
                                    if(rs3.getLong(2)==1000052) credito21-=rs3.getDouble(1); else
                                    if(rs3.getLong(2)==1000053) credito27-=rs3.getDouble(1); else
                                    credito10-=rs3.getDouble(1);
                                }
                            }
                       }
                       rs3.close();
                       pstmt3.close();
                       sqlInsert = "insert into T_LIBRO_IVA_COMPRAS values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                       pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                       pstmtInsert.setLong(1, AD_CLIENT_ID);
                       pstmtInsert.setLong(2, AD_ORG_ID);
                       pstmtInsert.setString(3, ISACTIVE);
                       pstmtInsert.setLong(4, p_PInstance_ID);
                       pstmtInsert.setDate(5, new Date(fromDate.getTime() + 1000));	
                       pstmtInsert.setString(6, COMPROBANTE);
                       pstmtInsert.setString(7, NRO_COMPROBANTE);
                       if (RAZON_SOCIAL.length()>N)
                    	   pstmtInsert.setString(8, RAZON_SOCIAL.substring(0, N-1));
                       else
                    	   pstmtInsert.setString(8, RAZON_SOCIAL);
                       pstmtInsert.setString(9, CUIT);
                       pstmtInsert.setDouble(10, impueso_iva);		//	NO GRAVADO
                       pstmtInsert.setDouble(11, impuesto_exento);	//	EXCENTO
                       pstmtInsert.setDouble(12, iva_parcial);		//	IVA DEBITO FISCAL
                       pstmtInsert.setDouble(13, PERCEPCIONIVA);	//	IVA_PERCEP
                       pstmtInsert.setDouble(14, PERCEPCIONIB);		//	OTROS
                       pstmtInsert.setDouble(15, PERCEPCIONIB+PERCEPCIONIVA+iva_parcial+impuesto_exento+impueso_iva);      

                       String fecha = "";
                       //Agregar Día
                       if(DATEINVOICED.getDate()<10) fecha = "0"+DATEINVOICED.getDate();
                       else fecha += DATEINVOICED.getDate();
                       //Agregar Mes
                       if(DATEINVOICED.getMonth()+1 < 10)  fecha+= "/0"+(DATEINVOICED.getMonth()+1);
                       else fecha+= "/"+(DATEINVOICED.getMonth()+1);
                       //Agregar Año
                       fecha +="/"+(DATEINVOICED.getYear()+1900);
                       
                       pstmtInsert.setString(16, fecha);			//FECHA
                       pstmtInsert.setInt(17,page);
                       page+=1;
                       //pstmtInsert.setString(16, format.format(DATEINVOICED));
                                            
                       pstmtInsert.executeQuery();
                       pstmtInsert.close();
                       
                       grabado += impueso_iva;
                       exento += impuesto_exento; 
                       ivacredito += iva_parcial;
                       ivaPercepcion += PERCEPCIONIVA;
                       otros +=PERCEPCIONIB;
                       total +=PERCEPCIONIB+PERCEPCIONIVA+iva_parcial+impuesto_exento+impueso_iva;
                       
                       DB.commit(true, get_TrxName());
                    } catch (Exception exception) {
                        log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaCompras.doIt.Sector1 " + exception.getMessage());
                        exception.printStackTrace();
                    }                
            }
            if(ok){
            // ingreso la ultima fila de totales
                sqlInsert = "insert into T_LIBRO_IVA_COMPRAS values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                pstmtInsert.setLong(1, CLIENT_ID);
                pstmtInsert.setLong(2, ORG_ID);
                pstmtInsert.setString(3, "S");
                pstmtInsert.setLong(4, p_PInstance_ID);
                pstmtInsert.setDate(5, null);pstmtInsert.setString(6, null);pstmtInsert.setString(7, null);
                pstmtInsert.setString(8, null);pstmtInsert.setString(9, null);
                pstmtInsert.setDouble(10, grabado);
                pstmtInsert.setDouble(11, exento);
                pstmtInsert.setDouble(12, ivacredito);
                pstmtInsert.setDouble(13, ivaPercepcion);
                pstmtInsert.setDouble(14, otros);               
                pstmtInsert.setDouble(15, total);
                pstmtInsert.setString(16, "Total");
                pstmtInsert.setInt(17,page);
                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName()); 
                page+=1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenerateLibroIvaComprasAnt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    //***********************************
     protected void sector2(){
         // Definición de variables para crear los script de insert, consulta y borrado
        boolean ok=false;
        String sqlQuery="";
        String sqlInsert="";
        double neto_total=0,exento_total=0,credito_total=0,total=0;
             
        // cargo la tabla de la cual saco los resultados
        log.info("Consulta de la vista RV_LIBRO_IVA_COMPRAS, se filtra por el rango indicado");
        sqlQuery = "select distinct c_tax_id,ad_client_id,ad_org_id,isactive from c_invoicetax where c_invoice_id in(select c_invoice_id from rv_libro_iva_compras"+
                    " where dateacct between ? and ?) group by ad_client_id,ad_org_id,isactive,c_tax_id";
        
         PreparedStatement pstmt = null;
         PreparedStatement pstmtInsert = null;   
         try {
            pstmt=DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate);     
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
              try{
                ok=true;
                if(rs.getLong(1)==1000054){
                    sqlInsert="update T_LIBRO_IVA_COMPRAS2 set AD_CLIENT_ID="+rs.getLong(2)+",AD_ORG_ID="+rs.getLong(3)+",ISACTIVE='"+rs.getString(4)+"',AD_PINSTANCE_ID="+p_PInstance_ID+",NETO_GRAVADO="+iva_parcial10+",IVA_CREDITO="+credito10+",TOTAL="+(credito10+iva_parcial10)+
                            " where libro_id=1";
                    neto_total += iva_parcial10;
                    credito_total += credito10;
                    total += (credito10+iva_parcial10);
                }
                else
                if(rs.getLong(1)==1000052){
                    sqlInsert="update T_LIBRO_IVA_COMPRAS2 set AD_CLIENT_ID="+rs.getLong(2)+",AD_ORG_ID="+rs.getLong(3)+",ISACTIVE='"+rs.getString(4)+"',AD_PINSTANCE_ID="+p_PInstance_ID+",NETO_GRAVADO="+iva_parcial21+",IVA_CREDITO="+credito21+",TOTAL="+(credito21+iva_parcial21)+
                            " where libro_id=2";
                    neto_total += iva_parcial21;
                    credito_total += credito21;
                    total += credito21+iva_parcial21;
                }
                else
                if(rs.getLong(1)==1000053){
                    sqlInsert="update T_LIBRO_IVA_COMPRAS2 set AD_CLIENT_ID="+rs.getLong(2)+",AD_ORG_ID="+rs.getLong(3)+",ISACTIVE='"+rs.getString(4)+"',AD_PINSTANCE_ID="+p_PInstance_ID+",NETO_GRAVADO="+iva_parcial27+",IVA_CREDITO="+credito27+",TOTAL="+(credito27+iva_parcial27)+
                            " where libro_id=3";
                    neto_total += iva_parcial27;
                    credito_total += credito27;
                    total += credito27+iva_parcial27;
                }
                else
                if(rs.getLong(1)==1000055){
                    sqlInsert="update T_LIBRO_IVA_COMPRAS2 set AD_CLIENT_ID="+rs.getLong(2)+",AD_ORG_ID="+rs.getLong(3)+",ISACTIVE='"+rs.getString(4)+"',AD_PINSTANCE_ID="+p_PInstance_ID+",EXENTO="+exentototal+",TOTAL="+exentototal+
                            " where libro_id=4";    
                    exento_total += exentototal;
                    //credito_total += rs.getDouble(5);
                    total += exentototal;
                }
                else
                if(rs.getLong(1)==1000056){
                    sqlInsert="update T_LIBRO_IVA_COMPRAS2 set AD_CLIENT_ID="+rs.getLong(2)+",AD_ORG_ID="+rs.getLong(3)+",ISACTIVE='"+rs.getString(4)+"',AD_PINSTANCE_ID="+p_PInstance_ID+",EXENTO="+noGravado+",TOTAL="+noGravado+
                            " where libro_id=5";   
                    exento_total += noGravado;
                    //credito_total += rs.getDouble(5);
                    total += noGravado;
                }
           
                pstmtInsert=DB.prepareStatement(sqlInsert);                    
                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
                 }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaCompras.doIt.Sector2 "+exception.getMessage());
                 } 
            }
            if(ok){
            // para iva_percepcion y otros impuestos
                sqlInsert="update T_LIBRO_IVA_COMPRAS2 set IVA_PERCEPCION="+iva_percep +" where libro_id=6"; 
                pstmtInsert=DB.prepareStatement(sqlInsert);                    
                pstmtInsert.executeQuery();
                sqlInsert="update T_LIBRO_IVA_COMPRAS2 set OTROS="+ib_percep+" where libro_id=7"; 
                pstmtInsert=DB.prepareStatement(sqlInsert);                    
                pstmtInsert.executeQuery();
                sqlInsert="update T_LIBRO_IVA_COMPRAS2 set OTROS="+gan_percep+" where libro_id=8"; 
                pstmtInsert=DB.prepareStatement(sqlInsert);                    
                pstmtInsert.executeQuery();
                total += (ib_percep + gan_percep + iva_percep);
                double otros=(ib_percep+gan_percep);
                sqlInsert="update T_LIBRO_IVA_COMPRAS2 set NETO_GRAVADO="+neto_total+",EXENTO="+exento_total+",IVA_credito="+credito_total+",IVA_PERCEPCION="+iva_percep+",OTROS="+otros+",TOTAL="+total+
                        " where libro_id=9"; 
                pstmtInsert=DB.prepareStatement(sqlInsert);                    
                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());                  
            }
         }
         catch (Exception exception) {
            log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaCompras.doIt.Sector2 "+exception.getMessage());
             exception.printStackTrace();
         }
     }
     
     protected void tablaBase() throws SQLException{
         log.info("borrado de la tabla temporal T_LIBRO_IVA_COMPRAS2");
         String sqlRemove = "delete from T_LIBRO_IVA_COMPRAS2";
         DB.executeUpdate(sqlRemove, null);        
         DB.commit(true, get_TrxName());
         if(CLIENT_ID!=null){
             DB.commit(true, get_TrxName());         
             PreparedStatement pstmtInsert = null;
             String sqlInsert = "insert into T_LIBRO_IVA_COMPRAS2 values("+CLIENT_ID+","+ORG_ID+",'S',"+p_PInstance_ID+",'Tasa IVA 10.5%',null,null,null,null,null,null,1)";pstmtInsert=DB.prepareStatement(sqlInsert);pstmtInsert.executeQuery();
             sqlInsert = "insert into T_LIBRO_IVA_COMPRAS2 values("+CLIENT_ID+","+ORG_ID+",'Y',"+p_PInstance_ID+",'Tasa IVA 21%',null,null,null,null,null,null,2)";pstmtInsert=DB.prepareStatement(sqlInsert);pstmtInsert.executeQuery();
             sqlInsert = "insert into T_LIBRO_IVA_COMPRAS2 values("+CLIENT_ID+","+ORG_ID+",'Y',"+p_PInstance_ID+",'Tasa IVA 27%',null,null,null,null,null,null,3)";pstmtInsert=DB.prepareStatement(sqlInsert);pstmtInsert.executeQuery();
             sqlInsert = "insert into T_LIBRO_IVA_COMPRAS2 values("+CLIENT_ID+","+ORG_ID+",'Y',"+p_PInstance_ID+",'Exento',null,null,null,null,null,null,4)";pstmtInsert=DB.prepareStatement(sqlInsert); pstmtInsert.executeQuery();
             sqlInsert = "insert into T_LIBRO_IVA_COMPRAS2 values("+CLIENT_ID+","+ORG_ID+",'Y',"+p_PInstance_ID+",'No Gravado',null,null,null,null,null,null,5)";pstmtInsert=DB.prepareStatement(sqlInsert);pstmtInsert.executeQuery();
             sqlInsert = "insert into T_LIBRO_IVA_COMPRAS2 values("+CLIENT_ID+","+ORG_ID+",'Y',"+p_PInstance_ID+",'Percepción IVA',null,null,null,null,null,null,6)";pstmtInsert=DB.prepareStatement(sqlInsert);pstmtInsert.executeQuery();
             sqlInsert = "insert into T_LIBRO_IVA_COMPRAS2 values("+CLIENT_ID+","+ORG_ID+",'Y',"+p_PInstance_ID+",'Percepción IB',null,null,null,null,null,null,7)";pstmtInsert=DB.prepareStatement(sqlInsert);pstmtInsert.executeQuery();
             sqlInsert = "insert into T_LIBRO_IVA_COMPRAS2 values("+CLIENT_ID+","+ORG_ID+",'Y',"+p_PInstance_ID+",'Percepción Ganancias',null,null,null,null,null,null,8)";pstmtInsert=DB.prepareStatement(sqlInsert);pstmtInsert.executeQuery();
             sqlInsert = "insert into T_LIBRO_IVA_COMPRAS2 values("+CLIENT_ID+","+ORG_ID+",'Y',"+p_PInstance_ID+",'Totales',null,null,null,null,null,null,9)";pstmtInsert=DB.prepareStatement(sqlInsert);pstmtInsert.executeQuery();
             DB.commit(true, get_TrxName());         
         }
     }
     
     protected void setearTemp(){
        try{
            if(CLIENT_ID!=null){
                String sqlRemove2 = "delete from TEMP_LIBRO_IVA_COMPRAS";
                DB.executeUpdate(sqlRemove2, null);
                DB.commit(true, get_TrxName());         
                String sqlUpdate="insert into TEMP_LIBRO_IVA_COMPRAS VALUES(?,?,'Y',?,?)"; 
                PreparedStatement pstmt=DB.prepareStatement(sqlUpdate.toString(), get_TrxName());
                pstmt.setLong(1,CLIENT_ID);
                pstmt.setLong(2,ORG_ID);
                pstmt.setTimestamp(3, new Timestamp(fromDate.getTime() + 1000));
                pstmt.setLong(4, num_hoja);
                pstmt.executeQuery();
                DB.commit(true, get_TrxName());         
            }
        } catch (Exception exception) {
            log.info("Se produjo un error en org.compiere.process.GenerateLibroIvaCompras.doIt.Sector1 " + exception.getMessage());
            exception.printStackTrace();
        }        
     }
}
