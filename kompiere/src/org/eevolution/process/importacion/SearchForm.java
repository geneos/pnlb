/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process.importacion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Hashtable;

/**
 * @author daniel
 */
public class SearchForm	{

    protected static void addToHashString(Hashtable<String, Object> ht, String campo, ResultSet rsInicial, ResultSet rsFuente)	throws Exception{
    	if (rsInicial.getString(campo)!=null && rsFuente.getString(campo)==null)
    		ht.put(campo, "");
    	if ( (rsInicial.getString(campo)==null && rsFuente.getString(campo)!=null) ||
    		 (rsInicial.getString(campo)!=null && rsFuente.getString(campo)!=null &&
    				 !rsInicial.getString(campo).equals(rsFuente.getString(campo))) )
    		ht.put(campo, rsFuente.getString(campo));
    }
    
    public static Hashtable procesar(Connection cInicial, Connection cFuente, ResultSet rsInicial, ResultSet rsFuente)
    {
    	Hashtable<String, Object> ht = new Hashtable<String, Object>();
    	try	{
	    	addToHashString(ht, "ISACTIVE", rsInicial, rsFuente);
			addToHashString(ht, "DESCRIPTION", rsInicial, rsFuente);
			addToHashString(ht, "HELP", rsInicial, rsFuente);
			addToHashString(ht, "ACCESSLEVEL", rsInicial, rsFuente);
			addToHashString(ht, "CLASSNAME", rsInicial, rsFuente);
			addToHashString(ht, "ENTITYTYPE", rsInicial, rsFuente);
			addToHashString(ht, "ISBETAFUNCTIONALITY", rsInicial, rsFuente);
			addToHashString(ht, "JSPURL", rsInicial, rsFuente);
			
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	return new Hashtable<String, Object>();
	    }
	    
    	return ht;
    }
}
