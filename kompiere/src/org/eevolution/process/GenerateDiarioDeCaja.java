package org.eevolution.process;

import java.sql.SQLException;
import org.compiere.process.*;
import java.math.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.*;
import org.compiere.model.*;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;

/**
 *Esta clase inserta tuplas en la tabla temporal T_DIARIO_CAJA luego de un previo filtrado por fecha, tipo de efectivo
 *y organizacion.
 * 
 * @author BISION - Matías Maenza
 * @version 1.0
 */
public class GenerateDiarioDeCaja extends SvrProcess{
    
    private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    private long cashBook=0,org=0;
    int page;
    private String docStatus="";
    
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++)
	{
            String name = para[i].getParameterName();
            if(name.equals("C_CashBook_ID"))
                cashBook = ((BigDecimal) para[i].getParameter()).longValue();
            else
                if(name.equals("AD_Org_ID"))
                org = ((BigDecimal) para[i].getParameter()).longValue();
                else
                    if(name.equals("DOCSTATUS"))
                        docStatus = (String)para[i].getParameter();
                    else{
                        fromDate=(Timestamp)para[i].getParameter();
                        toDate=(Timestamp)para[i].getParameter_To();
                    }
        }
        if(docStatus==null) docStatus="";
        p_PInstance_ID = getAD_PInstance_ID(); 
        page = 1;            
    }

    protected String doIt() throws Exception {
        String sqlQuery,sqlInsert="",sql;
        PreparedStatement pstmtInsert = null;
        log.info("Comienzo del proceso del detalle de diario de caja");
        log.info("borrado de la tabla temporal T_DIARIO_CAJA");
        String sqlRemove = "delete from T_DIARIO_CAJA";
        DB.executeUpdate(sqlRemove, null);
        /*
         * para el saldo inicial y final
         * */
        
        sql="select distinct C_Cash_ID,AD_CLIENT_ID,DATEACCT,LIBRO_CAJA,DIARIO_CAJA,C_CASHBOOK_ID,SALDOINICIAL,SALDOFINAL,DOCSTATUS from RV_DIARIO_DE_CAJA where ";
        sql=getConsulta(sql);sql+=" order by DATEACCT,LIBRO_CAJA,DIARIO_CAJA";
        PreparedStatement pstmt2 = DB.prepareStatement(sql);
        if(fromDate!=null){
        pstmt2.setTimestamp(1, fromDate);
        pstmt2.setTimestamp(2, toDate);}
        ResultSet rs2 = pstmt2.executeQuery();
        while(rs2.next()){
            
            saldos(rs2.getLong(2),new Date(rs2.getDate(3).getTime() + 1000),rs2.getString(4),rs2.getString(5),"Saldo Inicial",rs2.getLong(6),rs2.getDouble(7),rs2.getString(9));
                       
            /*
             * para las lineas de caja
             * */
            sqlQuery="select AD_CLIENT_ID,AD_ORG_ID,DATEACCT,LIBRO_CAJA,DIARIO_CAJA,TIPOEFECTIVO,DETALLE,MONEDA,IMPORTE,LINEA,SALDOINICIAL,SALDOFINAL,DOCSTATUS "+
                    "from RV_DIARIO_DE_CAJA where C_Cash_ID=? and ";
            sqlQuery=getConsulta(sqlQuery); sqlQuery+=" order by DATEACCT,LIBRO_CAJA,DIARIO_CAJA,linea"; 
            PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setLong(1, rs2.getLong(1));
            if(fromDate!=null){
            pstmt.setTimestamp(2, fromDate);
            pstmt.setTimestamp(3, toDate);}
            
            ResultSet rs = pstmt.executeQuery();
            try {
                while (rs.next()) {
                sqlInsert = "insert into T_DIARIO_CAJA values(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?)";
                pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                pstmtInsert.setLong(1, rs.getLong(1));
                pstmtInsert.setLong(2, rs.getLong(2));
                pstmtInsert.setLong(3, p_PInstance_ID);
                pstmtInsert.setDate(4, new Date(rs.getDate(3).getTime() + 1000));
                pstmtInsert.setString(5, rs.getString(4));
                pstmtInsert.setString(6, rs.getString(5));
                pstmtInsert.setString(7, rs.getString(6));
                pstmtInsert.setString(8, rs.getString(7));
                pstmtInsert.setString(9, rs.getString(8));
                pstmtInsert.setDouble(10, rs.getDouble(9));
                pstmtInsert.setLong(11, cashBook);
                pstmtInsert.setInt(12, page);
                pstmtInsert.setString(13, rs.getString(13));
                pstmtInsert.setString(14, getEstado(rs.getString(13)));
                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
                page+=1;
            }
            } catch (Exception exception) {
               log.info("Se produjo un error en org.compiere.process.GenerateDiarioDeCaja " + exception.getMessage());
               exception.printStackTrace();
            }
            pstmt.close();
            pstmtInsert.close();
            rs.close();
            saldos(rs2.getLong(2),new Date(rs2.getDate(3).getTime() + 1000),rs2.getString(4),rs2.getString(5),"Saldo final",rs2.getLong(6),rs2.getDouble(8),rs2.getString(9));
        }
        
        UtilProcess.initViewer("Detalle de Diario de Caja",p_PInstance_ID,getProcessInfo());
        return "success";
    }
    
    private void saldos(long cliente,Date date,String st1,String st2,String st3,long bookId,double importe,String st5){
        try {
            String sql="select cur.ISO_CODE from C_CashBook libro "+
                "left join c_currency cur on (cur.C_CURRENCY_ID = libro.C_CURRENCY_ID) "+
                "where libro.C_CASHBOOK_ID="+bookId;
            PreparedStatement pstmt = DB.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                String sqlInsert = "insert into T_DIARIO_CAJA values(?,?,'Y',?,?,?,?,null,?,?,?,?,?,?,?)";
                PreparedStatement pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
                pstmtInsert.setLong(1, cliente);
                pstmtInsert.setLong(2, org);
                pstmtInsert.setLong(3, p_PInstance_ID);
                pstmtInsert.setDate(4, date);
                pstmtInsert.setString(5, st1);
                pstmtInsert.setString(6, st2);

                pstmtInsert.setString(7, st3);
                pstmtInsert.setString(8, rs.getString(1));
                pstmtInsert.setDouble(9, importe);
                pstmtInsert.setLong(10, cashBook);
                pstmtInsert.setInt(11, page);
                pstmtInsert.setString(12, st5);
                pstmtInsert.setString(13, getEstado(st5));
                pstmtInsert.executeQuery();
                DB.commit(true, get_TrxName());
                pstmtInsert.close();
                page+=1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenerateDiarioDeCaja.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    private String getConsulta(String sql){
        if(org!=0) sql+="ad_org_id="+org+" and ";
        if(cashBook!=0) sql+="C_CashBook_id="+cashBook+" and ";
        if(!docStatus.equals("")) sql+="docstatus='"+docStatus+"'  and ";
        if(fromDate!=null) sql +="(DATEACCT between ? and ?) and ";
        sql=sql.substring(0, sql.length()-4);
        return sql;
    }
    
    private String getEstado(String status){
        if(status==null) return "";
        else if(status.equals("??")) return "Desconocido";
        else if(status.equals("AP")) return "Aprobado";
        else if(status.equals("CL")) return "Cerrado";
        else if(status.equals("CO")) return "Completo";
        else if(status.equals("DR")) return "Borrador";
        else if(status.equals("IN")) return "Inactivo";
        else if(status.equals("IP")) return "En Proceso";        
        else if(status.equals("NA")) return "No Aprobado";
        else if(status.equals("RE")) return "Revertido";
        else if(status.equals("VO")) return "Anulado";
        else if(status.equals("WC")) return "Esperando Confirmación";
        else return "Pagos en Espera";       
    }
}
    
    