/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.print.layout;





import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nadia
 */
public class TransportLayout {

    HashMap columns =null;
    Boolean active;
    String [] acum=null;//new String[15];
    
            
    public TransportLayout()
    {
            this.columns=new HashMap();
            active=false;
           
    
    }        
    
    public void addColumTrasnported(String name, int column_index)
    {
        active=true;
        columns.put(name, column_index);
        
    }    
    
    public Boolean isActive(){
        return active;
    }
    public void setActive(Boolean isActive){
            active=isActive;
    }
    
    public ArrayList<Integer> getColumns(){
        
        return new ArrayList<Integer>(columns.values());
    }
    
    
    public void setTransport(HashMap t,int size){
        active=true;
        columns=t;
        acum= new String [size];
        clean();
       
    }
    
    public void sumar(int column,String monto){
        try {
            if ( monto != null && ! monto.equals("") ){ 
            Double suma = 0.00;
            String acumulado= acum[column];
            DecimalFormat decimalFormat = new DecimalFormat("###,####,##0.00");
            suma=(decimalFormat.parse((String)acumulado )).doubleValue();
            suma = suma + (decimalFormat.parse((String) monto)).doubleValue();
            acum[column]=decimalFormat.format(suma);
            }
           
        } catch (ParseException ex) {
            Logger.getLogger(TransportLayout.class.getName()).log(Level.SEVERE, null, ex);
        }
      
   } 
    public String getAcumColumn(int col){
        return acum[col];
    
    }

    void clean() {
        for (int j=0; j< acum.length;j++)
            acum[j]="0.00";
    }
    
    
    
}  
    
    
    
    
    

