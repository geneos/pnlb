/*
 * NewClass.java
 *
 * Created on 8 de enero de 2008, 11:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.compiere.model;

/**
 *
 * @author Pepo
 */
public class NewClass {
    
    /** Creates a new instance of NewClass */
    public NewClass() {
        
        String documentNo = "<234>";
        if(documentNo.indexOf("<") != -1)
        {
            documentNo = documentNo.substring(1,documentNo.length()-1);
        }
        
        String doc = "0001-24";
        int i = doc.length();
        
        String nro = doc.substring(5,doc.length());
        String nro2 = doc.substring(0,4);               
    }
    
	public static void main(String[] args)
	{
            
                    String documentNo = "<234>";
        if(documentNo.indexOf("<") != -1)
        {
            documentNo = documentNo.substring(1,documentNo.length()-1);
        }
		NewClass clase = new NewClass();
        }   //  main
    
}
