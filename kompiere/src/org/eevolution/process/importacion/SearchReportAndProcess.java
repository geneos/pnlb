/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process.importacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import org.compiere.util.Env;

/**
 * @author daniel
 */
public class SearchReportAndProcess	{

    private static Hashtable<String, Object> compararProcess(Connection cInicial, Connection cFuente, ResultSet rsInicial, ResultSet rsFuente)
    {
    	Hashtable<String, Object> ht = new Hashtable<String, Object>();
    	try	{
    	 
	    	String sql = "Select p.NAME, trl.NAME as TRLNAME, trl.DESCRIPTION as TRLDESCTRIPTION, " +
						 "	     trl.help as TRLHELP, trl.ISTRANSLATED,wf.VALUE as WFVALUE, " +
						 "	 	 pf.NAME PFNAME, rv.NAME as RVNAME " +
						 "		 From AD_Process p " +
						 "		 LEFT JOIN AD_Process_Trl trl ON (p.ad_process_id = trl.ad_process_id) " +
						 "		 LEFT JOIN ad_workflow wf ON (p.ad_workflow_id = wf.ad_workflow_id) " +
						 "		 LEFT JOIN ad_printformat pf ON (p.ad_printformat_id = pf.ad_printformat_id) " +
						 "		 LEFT JOIN AD_ReportView rv ON (p.AD_ReportView_ID = rv.AD_ReportView_ID) " +
						 "		 Where p.AD_Process_ID = ? ";    				 
	
			PreparedStatement psFuente = cFuente.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			psFuente.setInt(1, rsFuente.getInt("AD_Process_ID"));
			ResultSet rsExtFuente = psFuente.executeQuery();
			
			PreparedStatement psInicial = cInicial.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			psInicial.setInt(1, rsInicial.getInt("AD_Process_ID"));
			ResultSet rsExtInicial = psInicial.executeQuery();
			
			rsExtFuente.next();
			rsExtInicial.next();
	    	
			addToHashString(ht, "IsActive", rsInicial, rsFuente);
			addToHashString(ht, "Name", rsInicial, rsFuente);
			addToHashString(ht, "Description", rsInicial, rsFuente);
			addToHashString(ht, "Help", rsInicial, rsFuente);
			addToHashString(ht, "AccessLevel", rsInicial, rsFuente);
			addToHashString(ht, "EntityType", rsInicial, rsFuente);
			addToHashString(ht, "ProcedureName", rsInicial, rsFuente);
			addToHashString(ht, "IsReport", rsInicial, rsFuente);
			addToHashString(ht, "IsDirectPrint", rsInicial, rsFuente);
			addToHashString(ht, "RVNAME", rsExtInicial, rsExtFuente);
			addToHashString(ht, "Classname", rsInicial, rsFuente);
			addToHashString(ht, "PFNAME", rsExtInicial, rsExtFuente);
			addToHashString(ht, "WorkflowValue", rsInicial, rsFuente);
			addToHashString(ht, "WFVALUE", rsExtInicial, rsExtFuente);
			addToHashString(ht, "IsBetaFunctionality", rsInicial, rsFuente);
			addToHashString(ht, "IsServerProcess", rsInicial, rsFuente);
			addToHashString(ht, "TRLNAME", rsExtInicial, rsExtFuente);
			addToHashString(ht, "TRLDESCTRIPTION", rsExtInicial, rsExtFuente);
			addToHashString(ht, "TRLHELP", rsExtInicial, rsExtFuente);
			addToHashString(ht, "ISTRANSLATED", rsExtInicial, rsExtFuente);
			
			rsExtFuente.close();
			rsExtInicial.close();
			psFuente.close();
			psInicial.close();
    	}catch(Exception e)	{
    		e.printStackTrace();
    		return new Hashtable<String, Object>();
    	}
    	
		return ht;
    }	
    
    protected static void addToHashString(Hashtable<String, Object> ht, String campo, ResultSet rsInicial, ResultSet rsFuente)	throws Exception{
    	if (rsInicial.getString(campo)!=null && rsFuente.getString(campo)==null)
    		ht.put(campo, "");
    	if ( (rsInicial.getString(campo)==null && rsFuente.getString(campo)!=null) ||
    		 (rsInicial.getString(campo)!=null && rsFuente.getString(campo)!=null &&
    				 !rsInicial.getString(campo).equals(rsFuente.getString(campo))) )
    		ht.put(campo, rsFuente.getString(campo));
    }
    
    protected static void addToHashInteger(Hashtable<String, Object> ht, String campo, ResultSet rsInicial, ResultSet rsFuente)	throws Exception{
    	if (rsInicial.getInt(campo)!=rsFuente.getInt(campo))
    		ht.put(campo, Integer.toString(rsFuente.getInt(campo)));
    }
    
    private static Hashtable<String, Object> CompareParameters(ResultSet rsInicial, ResultSet rsFuente)
    {
    	Hashtable<String, Object> ht = new Hashtable<String, Object>();
    	try{
    		
    		addToHashString(ht, "IsActive", rsInicial, rsFuente);
    		addToHashString(ht, "Name", rsInicial, rsFuente);
			addToHashString(ht, "Description", rsInicial, rsFuente);
			addToHashString(ht, "Help", rsInicial, rsFuente);
			addToHashString(ht, "SeqNo", rsInicial, rsFuente);
			addToHashString(ht, "RE_NAME", rsInicial, rsFuente);
			addToHashString(ht, "RVNAME", rsInicial, rsFuente);
			addToHashString(ht, "VRNAME", rsInicial, rsFuente);
			addToHashString(ht, "ColumnName", rsInicial, rsFuente);
			addToHashString(ht, "IsCentrallyMaintained", rsInicial, rsFuente);
			addToHashString(ht, "FieldLength", rsInicial, rsFuente);
			addToHashString(ht, "IsMandatory", rsInicial, rsFuente);
			addToHashString(ht, "IsRange", rsInicial, rsFuente);
			addToHashString(ht, "DefaultValue", rsInicial, rsFuente);
			addToHashString(ht, "DefaultValue2", rsInicial, rsFuente);
			addToHashString(ht, "VFormat", rsInicial, rsFuente);
			addToHashString(ht, "ValueMin", rsInicial, rsFuente);
			addToHashString(ht, "ValueMax", rsInicial, rsFuente);
	        addToHashString(ht, "ELNAME", rsInicial, rsFuente);
			addToHashString(ht, "EntityType", rsInicial, rsFuente);
			addToHashString(ht, "TRLNAME", rsInicial, rsFuente);
			addToHashString(ht, "TRLDESCTRIPTION", rsInicial, rsFuente);
			addToHashString(ht, "TRLHELP", rsInicial, rsFuente);
			addToHashString(ht, "ISTRANSLATED", rsInicial, rsFuente);
			
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		return new Hashtable<String, Object>();
	    }
    	return ht;
	}
    
    private static Hashtable<String, Object> compararParametros(Connection cInicial, Connection cFuente, ResultSet rsInicial, ResultSet rsFuente)
    {
    	Hashtable<String, Object> ht = new Hashtable<String, Object>();
    	try{
	    	String sql = "Select p.*, trl.NAME as TRLNAME, trl.DESCRIPTION as TRLDESCTRIPTION, " + 
						 " 	     trl.help as TRLHELP, trl.ISTRANSLATED,re.NAME as RE_NAME, " +
						 "	 	 rv.NAME as RVNAME, vr.NAME as VRNAME, el.NAME as ELNAME " +
						 "		 From AD_Process_Para p " +
						 "		 LEFT JOIN AD_Process_Para_Trl trl ON (p.ad_process_para_id = trl.ad_process_para_id) " + 
						 "		 LEFT JOIN AD_REFERENCE re ON (re.AD_REFERENCE_ID = p.AD_REFERENCE_ID) " +
						 "		 LEFT JOIN AD_REFERENCE rv ON (rv.AD_REFERENCE_ID = p.AD_REFERENCE_VALUE_ID) " + 
						 "		 LEFT JOIN AD_VAL_RULE vr ON (p.AD_VAL_RULE_ID = vr.AD_VAL_RULE_ID) " + 						 
                 		 "		 LEFT JOIN AD_ELEMENT el ON (p.AD_ELEMENT_ID = el.AD_ELEMENT_ID) " + 	
	    				 "		 Where p.AD_Process_ID = ? " +
	    				 "		   AND p.AD_Client_ID = " + Env.getAD_Client_ID(Env.getCtx()) +
	    				 "		 Order By p.NAME";
	
			PreparedStatement psFuente = cFuente.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			psFuente.setInt(1, rsFuente.getInt("AD_Process_ID"));
			ResultSet rsExtFuente = psFuente.executeQuery();
			
			PreparedStatement psInicial = cInicial.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			psInicial.setInt(1, rsInicial.getInt("AD_Process_ID"));
			ResultSet rsExtInicial = psInicial.executeQuery();
			
			boolean continuar = false;
			if (rsExtInicial.next())
				if (rsExtFuente.next())
	        		continuar = true;
				else
					do	{
						ht.put("P <-- " + rsExtInicial.getString("NAME"), new Hashtable<String, Object>());
					}while (rsExtInicial.next());
			else
				if (rsExtFuente.next())
					do	{
		        		ht.put("P --> " + rsExtFuente.getString("NAME"), new Hashtable<String, Object>());
					}while (rsExtFuente.next());
				
	        while (continuar)
	        {
	        	String rsI = rsExtInicial.getString("NAME");
	        	String rsF = rsExtFuente.getString("NAME");
	        	if (rsI.compareTo(rsF)==0)
	        	{
	        		Hashtable<String, Object> SUBht = CompareParameters(rsExtInicial,rsExtFuente);
	        		if (!SUBht.isEmpty())
	        			ht.put("P -- " + rsI, CompareParameters(rsExtInicial,rsExtFuente));
	        		rsExtFuente.next();
	    			rsExtInicial.next();
	            }
	        	else
	        		if (rsI.compareTo(rsF)<0)
	            	{
	        			ht.put("P <-- " + rsI, new Hashtable<String, Object>());
	        			rsExtInicial.next();
	                }
	        		else
	       			{
	        			ht.put("P --> " + rsF, new Hashtable<String, Object>());
	        			rsExtFuente.next();	        			
	                }
	        	if (rsExtInicial.isAfterLast() || rsExtFuente.isAfterLast())
	            	continuar = false;
	        }
	        try	{
		        if (!rsExtFuente.isAfterLast())
		        	do	{
		        		ht.put("P --> " + rsExtFuente.getString("NAME"), new Hashtable<String, Object>());
					}while (rsExtFuente.next());
		        		
		        else
		        	if (!rsExtInicial.isAfterLast())
		        		do	{
							ht.put("P <-- " + rsExtInicial.getString("NAME"), new Hashtable<String, Object>());
						}while (rsExtInicial.next());
	        }catch (SQLException ex) {
		    	return ht;
		    }
	        
	        rsExtFuente.close();
			rsExtInicial.close();
			psFuente.close();
			psInicial.close();
	    } catch (SQLException ex) {
	    	ex.printStackTrace();
	    	return new Hashtable<String, Object>();
	    }
    	return ht;
    }	
    
    public static Hashtable procesar(Connection cInicial, Connection cFuente, ResultSet rsInicial, ResultSet rsFuente)
    {
    	Hashtable<String, Object> ht = new Hashtable<String, Object>(); 
    	ht.putAll(compararProcess(cInicial, cFuente, rsInicial, rsFuente));
    	ht.putAll(compararParametros(cInicial, cFuente, rsInicial, rsFuente));
    	
    	return ht;
    }
}
