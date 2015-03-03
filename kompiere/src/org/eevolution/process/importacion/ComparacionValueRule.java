/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process.importacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import org.compiere.util.Env;


/**
 * @author daniel
 */
public class ComparacionValueRule extends CompareProcess{


    public ComparacionValueRule(Connection cd, Connection cf){
        setConexionFuente(cf);
        setConexionDestino(cd);
        setNombreColumna("NAME");
        setNombreTabla("AD_VAL_RULE");
        setMensaje("Se van a buscar diferencias entre Validaciones Dinámicas, luego vera los resultados por pantalla.");
        setTitMensaje( "Comparación Reference");
    }

    protected boolean Comparar() throws Exception{
        try {
        	String sql = "select * from " + getNombreTabla() + " Where AD_Client_ID = " + Env.getAD_Client_ID(Env.getCtx()) + " Order by " + getNombreColumna();
            PreparedStatement psFuente = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsFuente = psFuente.executeQuery();
            
            PreparedStatement psInicial = getConexionDestino().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInicial = psInicial.executeQuery();
    
            rsInicial.next();
            rsFuente.next();
            
            boolean continuar = true;
            if (rsInicial.isAfterLast() || rsFuente.isAfterLast())
            	continuar = false;
           	int continicial=0;
           	int contfuente=0;
            while (continuar)
            {
                String rsI = rsInicial.getString(getNombreColumna());
            	String rsF = rsFuente.getString(getNombreColumna());
            	if (rsI.compareTo(rsF)==0)
            	{
            		CompararReference(rsInicial,rsFuente);
            		rsInicial.next();
                    rsFuente.next();
                    continicial++;
                    contfuente++;
                }
            	else
               		{
            			rsInicial.next();
            			continicial++;
            		}

            	if (rsInicial.isAfterLast()){
                	continuar = false;
                	JOptionPane.showMessageDialog(null,"Termino por integrado - contador integrado: " + continicial + "contador produccion: " + contfuente);
            	}
               	if (rsFuente.isAfterLast()){
            		continuar = false;
            		JOptionPane.showMessageDialog(null,"Termino por produccionpa - contador fuente: " + contfuente);
        		}
               	

            	
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionCompareFrame.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            throw ex;
        }
        return true;
    }


//  Atributos de Val_Rule
    /**
    	AD_VAL_RULE_ID     	NUMBER(10,0) NOT NULL ENABLE, 
		AD_CLIENT_ID 		NUMBER(10,0) NOT NULL ENABLE, 
		AD_ORG_ID 			NUMBER(10,0) NOT NULL ENABLE, 
		ISACTIVE 			CHAR(1 BYTE) DEFAULT 'Y' NOT NULL ENABLE, 
		CREATED 			DATE DEFAULT SYSDATE NOT NULL ENABLE, 
	   	CREATEDBY 			NUMBER(10,0) NOT NULL ENABLE, 
    	UPDATED 			DATE DEFAULT SYSDATE NOT NULL ENABLE, 
 		UPDATEDBY			NUMBER(10,0) NOT NULL ENABLE, 
 		NAME				NVARCHAR2(60) NOT NULL ENABLE, 
		DESCRIPTION			NVARCHAR2(255), 
		TYPE				CHAR(1 BYTE) DEFAULT NULL, 
		CODE				NVARCHAR2(2000), 
		ENTITYTYPE			VARCHAR2(4 BYTE) DEFAULT 'D' NOT NULL ENABLE,
	
	 **/

    private void CompararReference(ResultSet rsInicial, ResultSet rsFuente) throws Exception{
    	if ( (rsInicial.getString("CODE")!=null && rsFuente.getString("CODE")==null) ||
       		 (rsInicial.getString("CODE")==null && rsFuente.getString("CODE")!=null) ||
       		 (rsInicial.getString("CODE")!=null && rsFuente.getString("CODE")!=null &&
       				 !rsInicial.getString("CODE").equals(rsFuente.getString("CODE"))) )
     		JOptionPane.showMessageDialog(null,"La regla: " + rsInicial.getString("NAME") + " son distintas");
}

}
    

