package org.compiere.model;

import java.math.*;
import java.lang.String;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;
import org.compiere.util.*;

public class CalloutRegimenRetPercep extends CalloutEngine 
{

    public String checkExclusionPercepcion (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){
            boolean espercepcion = (Boolean)mField.getValue();
            if(espercepcion){
                mTab.setValue("ESRETENCION", new Boolean(false));                
            }
            // por defecto la lista codigo jurisdiccion debe tener seleccionado vacio
            ((MLookup)mTab.getField("TIPO_RET").getLookup()).setSelectedItem(null);
            ((MLookup)mTab.getField("TIPO_RET").getLookup()).refresh(true);
           
             
            return "";
        }    
    
    public String checkExclusionRetencion (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){
            boolean esretencion = (Boolean)mField.getValue();
            if(esretencion){
                mTab.setValue("ESPERCEPCION", new Boolean(false));                
            }
            // por defecto la lista codigo jurisdiccion debe tener seleccionado vacio
            ((MLookup)mTab.getField("TIPO_PERCEP").getLookup()).setSelectedItem(null);
            ((MLookup)mTab.getField("TIPO_PERCEP").getLookup()).refresh(true);
         
             
            return "";
        }
    
     public String setDefaultValue (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){
            String tipo = (String)mField.getValue();
            if ( tipo != null ){
                if( tipo.equals("I") || tipo.equals("G") ||  tipo.equals("IVA") || tipo.equals("GAN")){
                mTab.getField("NRO_REGIM").setValue(0,true);                
                }
            } 
            return "";
        }

}