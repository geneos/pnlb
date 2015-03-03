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

/**
 * @author daniel
 */
public class SearchPrintFormat	{

    private static Hashtable<String, Object> compararPrintFormat(Connection cInicial, Connection cFuente, ResultSet rsInicial, ResultSet rsFuente)
    {
    	Hashtable<String, Object> ht = new Hashtable<String, Object>();
    	try	{
    	 
	    	String sql = "Select p.NAME, pp.NAME as PAPERNAME, pc.NAME as COLORNAME, pf.NAME as FONTNAME, " +
	    				 "		 ptf.NAME as TFORMATNAME, tn.TABLENAME TABLENAME, rv.NAME as RVNAME " +
						 "		 From AD_PrintFormat p " +
						 "		 LEFT JOIN AD_PRINTPAPER pp ON (p.AD_PRINTPAPER_ID = pp.AD_PRINTPAPER_ID) " +
						 "		 LEFT JOIN AD_PRINTCOLOR pc ON (p.AD_PRINTCOLOR_ID = pc.AD_PRINTCOLOR_ID) " +
						 "		 LEFT JOIN AD_PRINTFONT pf ON (p.AD_PRINTFONT_ID = pf.AD_PRINTFONT_ID) " +
						 "		 LEFT JOIN AD_PRINTTABLEFORMAT ptf ON (p.AD_PRINTTABLEFORMAT_ID = ptf.AD_PRINTTABLEFORMAT_ID) " +
						 "		 LEFT JOIN AD_TABLE tn ON (p.AD_TABLE_ID = tn.AD_TABLE_ID) " +
						 "		 LEFT JOIN AD_ReportView rv ON (p.AD_ReportView_ID = rv.AD_ReportView_ID) " +
						 "		 Where p.AD_PrintFormat_ID = ? ";    				 
	    	
			PreparedStatement psFuente = cFuente.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			psFuente.setInt(1, rsFuente.getInt("AD_PrintFormat_ID"));
			ResultSet rsExtFuente = psFuente.executeQuery();
			
			PreparedStatement psInicial = cInicial.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			psInicial.setInt(1, rsInicial.getInt("AD_PrintFormat_ID"));
			ResultSet rsExtInicial = psInicial.executeQuery();
			
			rsExtFuente.next();
			rsExtInicial.next();
	    	
			addToHashString(ht, "IsActive", rsInicial, rsFuente);
			addToHashString(ht, "Name", rsInicial, rsFuente);
			addToHashString(ht, "Description", rsInicial, rsFuente);
			addToHashString(ht, "ISTABLEBASED", rsInicial, rsFuente);
			addToHashString(ht, "ISFORM", rsInicial, rsFuente);
			addToHashString(ht, "ISSTANDARDHEADERFOOTER", rsInicial, rsFuente);
			addToHashString(ht, "HEADERMARGIN", rsInicial, rsFuente);
			addToHashString(ht, "FOOTERMARGIN", rsInicial, rsFuente);
			//addToHashString(ht, "CREATECOPY", rsInicial, rsFuente);
			addToHashString(ht, "RVNAME", rsExtInicial, rsExtFuente);
			//addToHashString(ht, "PRINTERNAME", rsInicial, rsFuente);
			addToHashString(ht, "ISDEFAULT", rsInicial, rsFuente);
			//addToHashString(ht, "VIEWPARAMETERS", rsInicial, rsFuente);
			addToHashString(ht, "TABLENAME", rsExtInicial, rsExtFuente);
			addToHashString(ht, "PAPERNAME", rsExtInicial, rsExtFuente);
			addToHashString(ht, "COLORNAME", rsExtInicial, rsExtFuente);
			addToHashString(ht, "FONTNAME", rsExtInicial, rsExtFuente);
			addToHashString(ht, "TFORMATNAME", rsExtInicial, rsExtFuente);
			
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
    
    protected static void addToHashStringCERO(Hashtable<String, Object> ht, String campo, ResultSet rsInicial, ResultSet rsFuente)	throws Exception{
    	if (rsInicial.getString(campo)!=null && !rsInicial.getString(campo).equals("0") && rsFuente.getString(campo)==null)
    		ht.put(campo, rsInicial.getString(campo));
    	if ( (rsInicial.getString(campo)==null && rsFuente.getString(campo)!=null) ||
    		 (rsInicial.getString(campo)!=null && rsFuente.getString(campo)!=null &&
    				 !rsInicial.getString(campo).equals(rsFuente.getString(campo))) )
    		ht.put(campo, rsFuente.getString(campo));
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
    
    private static Hashtable<String, Object> CompareItems(ResultSet rsInicial, ResultSet rsFuente)
    {
    	Hashtable<String, Object> ht = new Hashtable<String, Object>();
    	try{
    		
    		addToHashString(ht, "ISACTIVE", rsInicial, rsFuente);
    		addToHashString(ht, "NAME", rsInicial, rsFuente);
			addToHashString(ht, "PRINTNAME", rsInicial, rsFuente);
			addToHashString(ht, "ISPRINTED", rsInicial, rsFuente);
			addToHashString(ht, "PRINTAREATYPE", rsInicial, rsFuente);
			addToHashInteger(ht, "SEQNO", rsInicial, rsFuente);
			addToHashString(ht, "PRINTFORMATTYPE", rsInicial, rsFuente);
			addToHashString(ht, "ISRELATIVEPOSITION", rsInicial, rsFuente);
			addToHashString(ht, "ISNEXTLINE", rsInicial, rsFuente);
			addToHashInteger(ht, "XSPACE", rsInicial, rsFuente);
			addToHashInteger(ht, "YSPACE", rsInicial, rsFuente);
			addToHashInteger(ht, "XPOSITION", rsInicial, rsFuente);
			addToHashInteger(ht, "YPOSITION", rsInicial, rsFuente);
			addToHashInteger(ht, "MAXWIDTH", rsInicial, rsFuente);
			addToHashString(ht, "ISHEIGHTONELINE", rsInicial, rsFuente);
			addToHashInteger(ht, "MAXHEIGHT", rsInicial, rsFuente);
    		addToHashString(ht, "FIELDALIGNMENTTYPE", rsInicial, rsFuente);
			addToHashString(ht, "LINEALIGNMENTTYPE", rsInicial, rsFuente);
			addToHashString(ht, "COLUMNNAME", rsInicial, rsFuente);
			addToHashString(ht, "COLORNAME", rsInicial, rsFuente);
			addToHashString(ht, "FONTNAME", rsInicial, rsFuente);
			addToHashString(ht, "GRAPHNAME", rsInicial, rsFuente);
			addToHashString(ht, "PRINTFORMATNAME", rsInicial, rsFuente);
			addToHashString(ht, "ISORDERBY", rsInicial, rsFuente);
			addToHashInteger(ht, "SORTNO", rsInicial, rsFuente);
			addToHashString(ht, "ISGROUPBY", rsInicial, rsFuente);
			addToHashString(ht, "ISPAGEBREAK", rsInicial, rsFuente);
			addToHashString(ht, "ISSUMMARIZED", rsInicial, rsFuente);
			addToHashString(ht, "IMAGEISATTACHED", rsInicial, rsFuente);
			addToHashString(ht, "IMAGEURL", rsInicial, rsFuente);
			addToHashString(ht, "ISAVERAGED", rsInicial, rsFuente);
			addToHashString(ht, "ISCOUNTED", rsInicial, rsFuente);
			addToHashString(ht, "ISSETNLPOSITION", rsInicial, rsFuente);
			addToHashString(ht, "ISSUPPRESSNULL", rsInicial, rsFuente);
			addToHashStringCERO(ht, "BELOWCOLUMN", rsInicial, rsFuente);
			addToHashStringCERO(ht, "RUNNINGTOTALLINES", rsInicial, rsFuente);
			addToHashString(ht, "ISFIXEDWIDTH", rsInicial, rsFuente);
			addToHashString(ht, "ISNEXTPAGE", rsInicial, rsFuente);
			addToHashString(ht, "PRINTNAMESUFFIX", rsInicial, rsFuente);
			addToHashString(ht, "ISMINCALC", rsInicial, rsFuente);
			addToHashString(ht, "ISMAXCALC", rsInicial, rsFuente);
			addToHashString(ht, "ISRUNNINGTOTAL", rsInicial, rsFuente);
			addToHashString(ht, "ISVARIANCECALC", rsInicial, rsFuente);
			addToHashString(ht, "ISDEVIATIONCALC", rsInicial, rsFuente);
			addToHashString(ht, "ARCDIAMETER", rsInicial, rsFuente);
			addToHashString(ht, "ISFILLEDRECTANGLE", rsInicial, rsFuente);
			addToHashInteger(ht, "LINEWIDTH", rsInicial, rsFuente);
			addToHashString(ht, "SHAPETYPE", rsInicial, rsFuente);
			addToHashString(ht, "ISCENTRALLYMAINTAINED", rsInicial, rsFuente);
			addToHashString(ht, "ISIMAGEFIELD", rsInicial, rsFuente);
			addToHashString(ht, "PRINTNAME", rsInicial, rsFuente);
			addToHashString(ht, "ISTRANSLATED", rsInicial, rsFuente);
			addToHashString(ht, "PRINTNAMESUFFIX", rsInicial, rsFuente);
    					
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		return new Hashtable<String, Object>();
	    }
    	return ht;
	}
    
    private static Hashtable<String, Object> compararItems(Connection cInicial, Connection cFuente, ResultSet rsInicial, ResultSet rsFuente)
    {
    	Hashtable<String, Object> ht = new Hashtable<String, Object>();
    	try{
	    	String sql = "Select pfi.*, trl.PRINTNAME, trl.PRINTNAMESUFFIX, trl.ISTRANSLATED, " +
	    				 "		 col.COLUMNNAME, pc.NAME as COLORNAME, pf.NAME as FONTNAME," +
	    				 "		 pg.NAME as GRAPHNAME, pfc.NAME as PRINTFORMATNAME	" +
						 "		 From AD_PRINTFORMATITEM pfi " +
						 "		 LEFT JOIN AD_PRINTFORMATITEM_Trl trl ON (pfi.AD_PRINTFORMATITEM_ID = trl.AD_PRINTFORMATITEM_ID) " +
						 "		 LEFT JOIN AD_COLUMN col ON (col.AD_COLUMN_ID = pfi.AD_COLUMN_ID) " +
						 "		 LEFT JOIN AD_PRINTCOLOR pc ON (pfi.AD_PRINTCOLOR_ID = pc.AD_PRINTCOLOR_ID) " +
						 "		 LEFT JOIN AD_PRINTFONT pf ON (pfi.AD_PRINTFONT_ID = pf.AD_PRINTFONT_ID) " +
						 "		 LEFT JOIN AD_PRINTGRAPH pg ON (pfi.AD_PRINTGRAPH_ID = pg.AD_PRINTGRAPH_ID) " +
						 "		 LEFT JOIN AD_PRINTFORMAT pfc ON (pfi.AD_PRINTFORMATCHILD_ID = pfc.AD_PRINTFORMAT_ID) " +
						 "		 Where pfi.AD_PRINTFORMAT_ID = ? " +
	    				 "		 Order By pfi.NAME, pfi.AD_PRINTFORMATITEM_ID";
	
			PreparedStatement psFuente = cFuente.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			psFuente.setInt(1, rsFuente.getInt("AD_PrintFormat_ID"));
			ResultSet rsExtFuente = psFuente.executeQuery();
			
			PreparedStatement psInicial = cInicial.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			psInicial.setInt(1, rsInicial.getInt("AD_PrintFormat_ID"));
			ResultSet rsExtInicial = psInicial.executeQuery();
			
			boolean continuar = false;
			if (rsExtInicial.next())
				if (rsExtFuente.next())
	        		continuar = true;
				else
					do	{
						ht.put("I <-- " + rsExtInicial.getString("NAME"), new Hashtable<String, Object>());
					}while (rsExtInicial.next());
			else
				if (rsExtFuente.next())
					do	{
		        		ht.put("I --> " + rsExtFuente.getString("NAME"), new Hashtable<String, Object>());
					}while (rsExtFuente.next());
				
	        while (continuar)
	        {
	        	String rsI = rsExtInicial.getString("NAME");
	        	String rsF = rsExtFuente.getString("NAME");
	        	if (rsI.compareTo(rsF)==0)
	        	{
	        		Hashtable<String, Object> SUBht = CompareItems(rsExtInicial,rsExtFuente);
	        		if (!SUBht.isEmpty())
	        			ht.put("I -- " + rsI, CompareItems(rsExtInicial,rsExtFuente));
	        		rsExtFuente.next();
	    			rsExtInicial.next();
	            }
	        	else
	        		if (rsI.compareTo(rsF)<0)
	            	{
	        			ht.put("I <-- " + rsI, new Hashtable<String, Object>());
	        			rsExtInicial.next();
	                }
	        		else
	       			{
	        			ht.put("I --> " + rsF, new Hashtable<String, Object>());
	        			rsExtFuente.next();	        			
	                }
	        	if (rsExtInicial.isAfterLast() || rsExtFuente.isAfterLast())
	            	continuar = false;
	        }
	        try	{
		        if (!rsExtFuente.isAfterLast())
		        	do	{
		        		ht.put("I --> " + rsExtFuente.getString("NAME"), new Hashtable<String, Object>());
					}while (rsExtFuente.next());
		        		
		        else
		        	if (!rsExtInicial.isAfterLast())
		        		do	{
							ht.put("I <-- " + rsExtInicial.getString("NAME"), new Hashtable<String, Object>());
						}while (rsExtInicial.next());
	    	} catch (SQLException ex) {
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
    	ht.putAll(compararPrintFormat(cInicial, cFuente, rsInicial, rsFuente));
    	ht.putAll(compararItems(cInicial, cFuente, rsInicial, rsFuente));
    	
    	return ht;
    }
}
