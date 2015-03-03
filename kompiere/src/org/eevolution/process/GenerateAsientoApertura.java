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
public class GenerateAsientoApertura extends SvrProcess{
    
    int p_instance;
    
    Timestamp toDate = null;
    BigDecimal GL_JournalBatch_ID = null;
    int org = 0;
    
    protected void prepare() {
        p_instance = getAD_PInstance_ID(); 
        ProcessInfoParameter[] para = getParameter();
        
        for (int i = 0; i < para.length; i++)
    	{
        	String name = para[i].getParameterName();
    	    if(name.equals("GL_JournalBatch_ID"))
            	GL_JournalBatch_ID= (BigDecimal) para[i].getParameter();
            else{
            	toDate=(Timestamp)para[i].getParameter();
            }
        }
    }

    protected String doIt() {
        
    	try	{
	    	
    		MJournalBatch jBatch = new MJournalBatch(Env.getCtx(),GL_JournalBatch_ID.intValue(),get_TrxName());
    		
    		MJournalBatch newBatch = new MJournalBatch(Env.getCtx(),0,get_TrxName());
	    	newBatch.setC_Currency_ID(jBatch.getC_Currency_ID());
	    	newBatch.setDateAcct(toDate);
	    	newBatch.setDateDoc(toDate);
	    	newBatch.setDescription("Apertura de Cuentas Patrimoniales");
	    	newBatch.save(get_TrxName());
	    	
	    	MJournal[] journals = jBatch.getJournals(true);
	    	for (int i=0; i<journals.length; i++)
	    	{
	    		MJournal newJournal = new MJournal(newBatch);
	    		newJournal.setC_Currency_ID(journals[i].getC_Currency_ID());
		    	newJournal.setDateAcct(toDate);
		    	newJournal.setDateDoc(toDate);
		    	newJournal.setDescription("Apertura de Cuentas Patrimoniales");
		    	newJournal.setC_AcctSchema_ID(journals[i].getC_AcctSchema_ID());
		    	newJournal.setC_ConversionType_ID(journals[i].getC_ConversionType_ID());
		    	newJournal.save(get_TrxName());
		    	
		    	MJournalLine[] lines = journals[i].getLines(true);
		    	for (int j=0; j<lines.length; j++)
		    	{
		    		MJournalLine jLine = new MJournalLine(newJournal);
		    		jLine.setC_Currency_ID(lines[j].getC_Currency_ID());
		    		jLine.setDescription(lines[j].getDescription());
		    		jLine.setAmtSourceDr(lines[j].getAmtSourceCr());
		    		jLine.setAmtSourceCr(lines[j].getAmtSourceDr());
		    		jLine.setC_ElementValue_ID(lines[j].getC_ElementValue_ID());
		    		jLine.setLine(lines[j].getLine());
		    		jLine.save(get_TrxName());
		    	}
		    }
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
		return "Completo.";
    }
	
}
