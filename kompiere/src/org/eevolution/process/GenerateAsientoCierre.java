/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;
import java.sql.SQLException;
import org.compiere.process.*;

import java.math.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MJournal;
import org.compiere.model.MJournalBatch;
import org.compiere.model.MJournalLine;
import org.compiere.model.MConversionType;

import org.compiere.util.*;

/**
 * Esta clase inserta tuplas en las tablas temporales T_COBRANZA_RETENCIONES,T_COBRANZA_LISTADO,T_COBRANZA_CABECERA luego de un previo filtrado por pago 
 *  y calculos posteriores.
 * @author Bision
 */
public class GenerateAsientoCierre extends SvrProcess{
    
    int p_instance;
    
    Timestamp fromDate = null;
    Timestamp toDate = null;
    BigDecimal C_Currency_ID = null;
    BigDecimal schema = null;
    int org = 0;
    
    protected void prepare() {
        p_instance = getAD_PInstance_ID(); 
        ProcessInfoParameter[] para = getParameter();
        
        for (int i = 0; i < para.length; i++)
    	{
        	String name = para[i].getParameterName();
        	if(name.equals("C_AcctSchema_ID"))
        		schema= (BigDecimal) para[i].getParameter();
            else
                toDate=(Timestamp)para[i].getParameter();
        }
        fromDate = new Timestamp(toDate.getYear(),0,1,0,0,0,0);
        
        MAcctSchema account = new MAcctSchema(Env.getCtx(),schema.intValue(),null);
        C_Currency_ID = new BigDecimal(account.getC_Currency_ID());
    }

    protected String doIt() {
        
    	try	{
	    	
	    	MJournalBatch jBatch = new MJournalBatch(Env.getCtx(),0,get_TrxName());
	    	jBatch.setC_Currency_ID(C_Currency_ID.intValue());
	    	jBatch.setDateAcct(toDate);
	    	jBatch.setDateDoc(toDate);
	    	jBatch.setDescription("Cierre de Cuentas Patrimoniales");
	    	jBatch.save(get_TrxName());
	    	
	    	MJournal journal = new MJournal(jBatch);
	    	journal.setC_Currency_ID(C_Currency_ID.intValue());
	    	journal.setDateAcct(toDate);
	    	journal.setDateDoc(toDate);
	    	journal.setDescription("Cierre de Cuentas Patrimoniales");
	    	journal.setC_AcctSchema_ID(schema.intValue());
	    	journal.setC_ConversionType_ID(MConversionType.getDefault(getAD_Client_ID()));
	    	journal.save(get_TrxName());
	    	
	    	String sqlQuery = 
	        	" SELECT fa.account_id, ev.value, ev.name, -( sum(fa.amtacctdr)-sum(fa.amtacctcr) ) " +
	        	" FROM fact_acct fa " +
	        	" INNER JOIN c_elementvalue ev ON (ev.c_elementvalue_id=fa.account_id) " +
	        	//" LEFT JOIN FACT_ACCT_RESUMEN ON fa.RECORD_ID = FACT_ACCT_RESUMEN.RECORD_FACT_ID " +
	        	" WHERE account_id IN (select c_elementvalue_id from c_elementvalue where (value like '1%' or value like '2%' or value like '3%') and isActive='Y')" +
                        //" WHERE account_id IN (select c_elementvalue_id from c_elementvalue where (accounttype = 'A' or accounttype = 'L' or accounttype = 'O') and isActive='Y') " +
	        	"   AND fa.c_acctschema_id = ? AND fa.AD_Client_ID=? AND fa.dateacct between ? and ? " +
	        	//"   AND fa.isactive = 'Y' AND FACT_ACCT_RESUMEN.RECORD_FACT_ID is null " +
                        "   AND fa.isactive = 'Y' " +
	        	" GROUP BY fa.account_id,ev.value, ev.name " +
	        	" HAVING (sum(fa.amtsourcecr)- sum(fa.amtsourcedr)) <> 0 " +
	        	" ORDER BY ev.value " ;
	    	
	    	PreparedStatement pstmtQuery = DB.prepareStatement(sqlQuery,null);
	    	pstmtQuery.setInt(1, schema.intValue());
	    	pstmtQuery.setInt(2, getAD_Client_ID());
	    	pstmtQuery.setTimestamp(3, fromDate);
	    	pstmtQuery.setTimestamp(4, toDate);
	    	
	    	ResultSet rs = pstmtQuery.executeQuery();
	    	
	    	int i = 1;
	    	while (rs.next())
	    	{
	    		MJournalLine jLine = new MJournalLine(journal);
	    		jLine.setC_Currency_ID(C_Currency_ID.intValue());
	    		jLine.setDescription(rs.getString(2) + "_" + rs.getString(3));
	    		BigDecimal amount = rs.getBigDecimal(4);
	    		jLine.setPrecision(amount.scale());
                        if (amount.compareTo(BigDecimal.ZERO)>0)
	    			jLine.setAmtSourceDr(amount);
	    		else
	    			jLine.setAmtSourceCr(amount.negate());
	    		jLine.setC_ElementValue_ID(rs.getInt(1));
	    		jLine.setLine(i);
	    		jLine.save(get_TrxName());
	    		i++;
	    	}
	    	
	    	pstmtQuery.close();
	    	rs.close();
	    			
	    	DB.commit(false, get_TrxName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
		return "Completo.";
    }
	
}
