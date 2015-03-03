/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import org.compiere.process.*;
import org.eevolution.tools.UtilProcess;

/**
 *
 * @author Daniel Gini Bision
 */
public class CambioFPago extends SvrProcess{
    
    int p_instance;
    long cheque;
    long org;
        
     protected String doIt() throws Exception{
    	
    	org = 1000033;
    	
    	UtilProcess.initViewer("Cheques con Cambio en Fecha de Pago",p_instance,getProcessInfo());
        
    	return "success"; 
    }

    protected void prepare() {
    	p_instance = getAD_PInstance_ID();
    }
    
    public ProcessInfo setParamFromOut(long AD_Org_ID,String nroCheque){
    	org = AD_Org_ID;
    	
    	ProcessInfoParameter[] pip = new ProcessInfoParameter[1];
    	pip[0] = new ProcessInfoParameter ("NROCHEQUE", nroCheque, null, null, null);
    	    
    	ProcessInfo pi = new ProcessInfo("Cambio de Fecha de Pago",1000223);
    	pi.setParameter(pip);
    	
    	return pi;
    }   

}
