/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;
import java.sql.SQLException;

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.process.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import org.compiere.util.*;

/**
 * Esta clase inserta tuplas en las tablas temporales T_COBRANZA_RETENCIONES,T_COBRANZA_LISTADO,T_COBRANZA_CABECERA luego de un previo filtrado por pago 
 *  y calculos posteriores.
 * @author Bision
 */
public class GenerateNroAsientos extends SvrProcess{
    
    int p_instance;
    Date fromDate;
    Date toDate;
    DateFormat df,dft;
    Calendar c;
    String FROMDATE = "";
    String TODATE = "";
    
    protected void prepare() {
        p_instance = getAD_PInstance_ID();
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (name.equals("DateAcct")) {
                fromDate = (Date)para[i].getParameter();
                toDate = (Date)para[i].getParameter_To();
            }
        }
        try {
            TODATE = new SimpleDateFormat("dd/MM/yyyy").format(toDate);
            FROMDATE = new SimpleDateFormat("dd/MM/yyyy").format(fromDate);
        } catch (Exception ex) {
            Logger.getLogger(GenerateNroAsientos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected String doIt() throws ParseException {
        /*
         *  01/02/2013 Maria Jesus Martin
         *  Validacion para que verifique que las fechas ingresadas corresponden al mismo periodo.
         */
        if (fromDate.getYear()!=toDate.getYear()){
            JOptionPane.showMessageDialog(null,"Las fechas asignadas no corresponden al mismo periodo","ERROR - Verificación Fechas", JOptionPane.ERROR_MESSAGE);
            return "Terminated.Fechas no corresponden al mismo periodo.";
        }

        /*
         *  01/02/2013 Maria jesus Martin
         *  Si la fecha de inicio esta vacia, tiene que tomar el dia 01/01 del año en curso. 
         *  Si la fecha de fin esta vacia, tiene que tomar la fecha actual del año en curso.
         */
        if (toDate == null){
            c = Calendar.getInstance();
            TODATE = c.toString();
        }
        
        if (fromDate == null){
            FROMDATE = "01/01/"+c.get(Calendar.YEAR);
        }
    	try	
        {
            /*
             *  04/02/2013 Maria Jesus Martin
             *  Si la fecha de inicio del periodo a reenumerar, es mayor a la del ultimo asiento reenumerado
             *  se cambia la fecha de inicio, para que reenumere todo el periodo que queda en el medio.
             * 
             */
            String sqlfecha = "  select distinct(dateacct+1) AS FECHAREEN"+
                              "  from fact_acct " +
                              "  where factno = ( " +
                              "                   SELECT max(factno) " +
                              "                   FROM FACT_ACCT " +
                              "                   WHERE AD_Client_ID = ? and isActive='Y' and dateacct < ?)";
            PreparedStatement pstmtQueryF = DB.prepareStatement(sqlfecha,null);
            pstmtQueryF.setInt(1, getAD_Client_ID());
            pstmtQueryF.setString(2, FROMDATE);
            ResultSet rsf = pstmtQueryF.executeQuery();
            if (rsf.next()){
                String FECHAREEN = new SimpleDateFormat("dd/MM/yyyy").format(rsf.getDate(1));
                if (!(FECHAREEN.equals(FROMDATE))){
                    int rta = JOptionPane.showConfirmDialog(null,"Se reenumerada a partir de la fecha"+FECHAREEN+" ¿Desea que se reeenumere?","Cambio de fecha de Inicio de Reenumeración", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (rta!=JOptionPane.YES_OPTION)
                    {
                         return "Terminated.No se ha reenumerado.";
                    }
                    else
                        FROMDATE = FECHAREEN;
                }
            }
            
            String sql = "	SELECT distinct factno, dateacct, record_id, ad_table_id " +
                         "	FROM FACT_ACCT " +
                         " 	WHERE AD_Client_ID = ? and isActive='Y' and dateacct between ? and ? " +
                         "	ORDER BY dateacct";

            PreparedStatement pstmtQuery = DB.prepareStatement(sql,null);
            pstmtQuery.setInt(1, getAD_Client_ID());
            pstmtQuery.setString(2, FROMDATE);
            pstmtQuery.setString(3, TODATE);
            ResultSet rs = pstmtQuery.executeQuery();

            Integer in = getNroAsientoInicial();

            if (in!=null)
            {
                    int i = in.intValue();
                    while (rs.next())
                    {
                            if (rs.getInt(1)==i)
                            {	
                                    i++;
                                    continue;
                            }
                            actualizarRegistros(i, rs.getInt(3), rs.getInt(4));
                            i++;
                    }
            }

            pstmtQuery.close();
            rs.close();

            DB.commit(false, get_TrxName());
            } catch (SQLException e) {
                    e.printStackTrace();
            }

            return "Completo.";
    }
    
    private Integer getNroAsientoInicial() throws ParseException	{
    	Integer in = 0;
        df = new SimpleDateFormat("dd/MM/yy");
        Date date = df.parse(FROMDATE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
    	if (cal.get(Calendar.DAY_OF_MONTH) == 1 && cal.get(Calendar.MONTH) == 0){
            return 1;
        }
        else
        {
            try	{
                       String sql = "	SELECT max(factno) " +
                                    "	FROM FACT_ACCT " +
                                    " 	WHERE AD_Client_ID = ? and isActive='Y' and dateacct < ?";

                       PreparedStatement pstmtQuery = DB.prepareStatement(sql,null);
                       pstmtQuery.setInt(1, getAD_Client_ID());
                       pstmtQuery.setString(2, FROMDATE);
                       ResultSet rs = pstmtQuery.executeQuery();

                       if (rs.next()){
                           if (rs == null)
                               in = 1;
                           else
                               in = 1 + rs.getInt(1); 
                       }
            }
            catch (Exception e) {
                            return null;
                    }

            return in;
        }
    }
    
    public void actualizarRegistros(int nroAsiento, int nroRegistro, int table_id){
    	
    	String sql = "	UPDATE FACT_ACCT " +
		     "	SET factno = " + nroAsiento + 
     		     " 	WHERE record_id = " + nroRegistro +
                     "  and ad_table_id = " + table_id;
    	
    	DB.executeUpdate(sql,null);
        
    }
	
}
