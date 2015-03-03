/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process.importacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.compiere.model.X_AD_Reference;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author daniel
 */
public class ComparacionReference extends CompareProcess{

    public ComparacionReference(Connection cd, Connection cf){
        setConexionFuente(cf);
        setConexionDestino(cd);
        setNombreColumna("NAME");
        setNombreTabla("AD_REFERENCE");
        setMensaje("Se van a buscar diferencias entre References, luego verá los resultados en el reporte");
        setTitMensaje("Comparación Reference");
    }

    protected boolean Comparar() throws Exception{
        try {
        	DB.executeUpdate("DELETE FROM TMP_REFERENCE",getTrxName());
        	DB.executeUpdate("DELETE FROM TMP_REF_LIST",getTrxName());
        	
        	String sql = "select * from " + getNombreTabla() + " Where AD_Client_ID = " + Env.getAD_Client_ID(Env.getCtx()) + " Order by " + getNombreColumna();
            PreparedStatement psFuente = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsFuente = psFuente.executeQuery();
            
            PreparedStatement psInicial = getConexionDestino().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInicial = psInicial.executeQuery();
            
            rsInicial.next();
            rsFuente.next();
            
            sql = "SELECT MAX(AD_REFERENCE_ID) FROM AD_REFERENCE";
            PreparedStatement ps = getConexionDestino().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();
            int ref = 0;
            if (rs.next())
            	ref = rs.getInt(1) + 1;
            
            boolean continuar = false;
			if (rsInicial.next())
				if (rsFuente.next())
	        		continuar = true;
				else
					do	{
						InsertarReferenceInicial(rsInicial);
					}while (rsInicial.next());
			else
				if (rsFuente.next())
					do	{
						ref = InsertarReferenceFuente(rsFuente, ref);
					}while (rsFuente.next());
            
            while (continuar)
            {
            	String rsI = rsInicial.getString(getNombreColumna());
            	String rsF = rsFuente.getString(getNombreColumna());
            	int res = rsI.compareTo(rsF);
            	if (res==0)
            	{
            		CompararReference(rsInicial,rsFuente);
            		rsInicial.next();
                    rsFuente.next();
                }
            	else
            		if (res<0)
                	{
            			InsertarReferenceInicial(rsInicial);
                		rsInicial.next();
                    }
            		else
           			{
            			ref = InsertarReferenceFuente(rsFuente, ref);
                   		rsFuente.next();
                    }
            	if (rsInicial.isAfterLast() || rsFuente.isAfterLast())
                	continuar = false;
            }
            
            try	{
            	if (!rsFuente.isAfterLast())
                	do	{
    					ref = InsertarReferenceFuente(rsFuente, ref);
    				}while (rsFuente.next());
                else
                	if (!rsInicial.isAfterLast())
                		do	{
    						InsertarReferenceInicial(rsInicial);
    					}while (rsInicial.next());	
	    	} catch (SQLException ex) {
				return true;
			}
            
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionCompareFrame.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            setMensajeSalida("Error en la Importación");
            throw ex;
        }
        return true;
    }

//  Atributos de Reference
    /**
		AD_REFERENCE_ID		NUMBER(10,0)		NOT NULL,
		AD_CLIENT_ID		NUMBER(10,0)		NOT NULL,
		AD_ORG_ID			NUMBER(10,0)		NOT NULL,
		CREATED				DATE				NOT NULL,
		CREATEDBY			NUMBER(10,0)		NOT NULL,
		UPDATED				DATE				NOT NULL,
		UPDATEDBY			NUMBER(10,0)		NOT NULL,
		ISACTIVE			CHAR(1 BYTE)		NOT NULL,
		NAME				NVARCHAR2(60 CHAR)	NOT NULL,
		DESCRIPTION			NVARCHAR2(255 CHAR)			,	
		HELP				NVARCHAR2(2000 CHAR)		,
		VALIDATIONTYPE		CHAR(1 BYTE)		NOT NULL,
		VFORMAT				NVARCHAR2(40 CHAR)			,
		VFORMAT				VARCHAR2(4 BYTE)	NOT NULL,
   */

    private void CompararReference(ResultSet rsInicial, ResultSet rsFuente) throws Exception{
    	
    	StringBuffer SQLInto = new StringBuffer("");
    	StringBuffer SQLValue = new StringBuffer("");
    	
    	boolean change = false;
    	addBuffersString(SQLInto, SQLValue, "VALIDATIONTYPE", rsInicial, rsFuente);
    	if (SQLInto.toString().equals(""))
    		if (rsInicial.getString("VALIDATIONTYPE").equals(X_AD_Reference.VALIDATIONTYPE_ListValidation))
    			change = CompararList(rsInicial.getInt(getNombreTabla()+"_ID"),rsFuente.getInt(getNombreTabla()+"_ID"));
    		else
    			if (rsInicial.getString("VALIDATIONTYPE").equals(X_AD_Reference.VALIDATIONTYPE_TableValidation))
    				change = CompararTable(rsInicial.getInt(getNombreTabla()+"_ID"),rsFuente.getInt(getNombreTabla()+"_ID"),SQLInto,SQLValue);
    	
    	addBuffersString(SQLInto, SQLValue, "ISACTIVE", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "DESCRIPTION", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "HELP", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "VFORMAT", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "ENTITYTYPE", rsInicial, rsFuente);
    	
    	if (change || !SQLInto.toString().equals(""))
    	{	String insert = "INSERT INTO TMP_REFERENCE (TMP_REFERENCE_ID,AD_Client_ID,AD_Org_ID,NAME" + SQLInto.toString() + ") VALUES ("
    			+ Integer.toString(rsInicial.getInt(getNombreTabla()+"_ID")) + "," + Env.getAD_Client_ID(Env.getCtx()) + ","
    			+ Env.getAD_Org_ID(Env.getCtx()) + ",'" + rsInicial.getString(getNombreColumna()) + "'" + SQLValue.toString() + ")";
    		DB.executeUpdate(insert,getTrxName());
    	}
	}
    
    private void InsertarReferenceInicial(ResultSet rs) throws Exception{
    	
    	StringBuffer SQLInto = new StringBuffer("");
    	StringBuffer SQLValue = new StringBuffer("");
    	
		addBuffersStringColumna(SQLInto, SQLValue, "ISACTIVE", null, rs.getString("ISACTIVE"));
		addBuffersStringColumna(SQLInto, SQLValue, "DESCRIPTION", null, rs.getString("DESCRIPTION"));
		addBuffersStringColumna(SQLInto, SQLValue, "HELP", null, rs.getString("HELP"));
		addBuffersStringColumna(SQLInto, SQLValue, "VALIDATIONTYPE", null, rs.getString("VALIDATIONTYPE"));
		addBuffersStringColumna(SQLInto, SQLValue, "VFORMAT", null, rs.getString("VFORMAT"));
		addBuffersStringColumna(SQLInto, SQLValue, "ENTITYTYPE", null, rs.getString("ENTITYTYPE"));
	
		String insert = "INSERT INTO TMP_REFERENCE (TMP_REFERENCE_ID,AD_Client_ID,AD_Org_ID,NAME" + SQLInto.toString() + ") VALUES ("
			+ Integer.toString(rs.getInt(getNombreTabla()+"_ID")) + "," + Env.getAD_Client_ID(Env.getCtx()) + ","
			+ Env.getAD_Org_ID(Env.getCtx()) + ",'<-- " + rs.getString(getNombreColumna()) + "'" + SQLValue.toString() + ")";
	
		DB.executeUpdate(insert,getTrxName());
	}

    private int InsertarReferenceFuente(ResultSet rs, int ref) throws Exception{
    	
    	StringBuffer SQLInto = new StringBuffer("");
    	StringBuffer SQLValue = new StringBuffer("");
    	
		addBuffersStringColumna(SQLInto, SQLValue, "ISACTIVE", null, rs.getString("ISACTIVE"));
		addBuffersStringColumna(SQLInto, SQLValue, "DESCRIPTION", null, rs.getString("DESCRIPTION"));
		addBuffersStringColumna(SQLInto, SQLValue, "HELP", null, rs.getString("HELP"));
		addBuffersStringColumna(SQLInto, SQLValue, "VALIDATIONTYPE", null, rs.getString("VALIDATIONTYPE"));
		addBuffersStringColumna(SQLInto, SQLValue, "VFORMAT", null, rs.getString("VFORMAT"));
		addBuffersStringColumna(SQLInto, SQLValue, "ENTITYTYPE", null, rs.getString("ENTITYTYPE"));
	
		String insert = "INSERT INTO TMP_REFERENCE (TMP_REFERENCE_ID,AD_Client_ID,AD_Org_ID,NAME" + SQLInto.toString() + ") VALUES ("
			+ ref + "," + Env.getAD_Client_ID(Env.getCtx()) + ","
			+ Env.getAD_Org_ID(Env.getCtx()) + ",'--> " + rs.getString(getNombreColumna()) + "'" + SQLValue.toString() + ")";
	
		DB.executeUpdate(insert,getTrxName());
		return ref+1;
	}

//  Atributos de Lista
   /**    
	   	TMP_REF_LIST_ID 	NUMBER(10,0) NOT NULL ENABLE, 
		AD_CLIENT_ID 		NUMBER(10,0) NOT NULL ENABLE, 
		AD_ORG_ID 			NUMBER(10,0) NOT NULL ENABLE,
		 
		ISACTIVE 			CHAR(1 BYTE), 
		VALUE 				NVARCHAR2(60),
		NAME 				NVARCHAR2(100),
		DESCRIPTION 		NVARCHAR2(255),
		TMP_REFERENCE_ID 	NUMBER(10,0) NOT NULL ENABLE,
		VALIDFROM 			DATE,
		VALIDTO 			DATE,
		ENTITYTYPE 			VARCHAR2(4 BYTE),
	  	AD_LANGUAGE 		VARCHAR2(6 BYTE), 
		TRLNAME 			NVARCHAR2(100), 
		TLRDESCRIPTION 		NVARCHAR2(255), 
		ISTRANSLATED 		CHAR(1 BYTE), 
   */	
    
    private boolean CompararListItem(ResultSet rsInicial, ResultSet rsFuente) throws Exception{
		
    	StringBuffer SQLInto = new StringBuffer("");
    	StringBuffer SQLValue = new StringBuffer("");
    	
    	addBuffersStringColumna(SQLInto, SQLValue, "ISACTIVE", rsInicial.getString("ISACTIVE"), rsFuente.getString("ISACTIVE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "NAME", rsInicial.getString("NAME"), rsFuente.getString("NAME"));
    	addBuffersStringColumna(SQLInto, SQLValue, "DESCRIPTION", rsInicial.getString("DESCRIPTION"), rsFuente.getString("DESCRIPTION"));
    	addBuffersTimestampColumna(SQLInto, SQLValue, "VALIDFROM", rsInicial.getTimestamp("VALIDFROM"), rsFuente.getTimestamp("VALIDFROM"));
    	addBuffersTimestampColumna(SQLInto, SQLValue, "VALIDTO", rsInicial.getTimestamp("VALIDTO"), rsFuente.getTimestamp("VALIDTO"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ENTITYTYPE", rsInicial.getString("ENTITYTYPE"), rsFuente.getString("ENTITYTYPE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "AD_LANGUAGE", rsInicial.getString("AD_LANGUAGE"), rsFuente.getString("AD_LANGUAGE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "TRLNAME", rsInicial.getString("TRLNAME"), rsFuente.getString("TRLNAME"));
    	addBuffersStringColumna(SQLInto, SQLValue, "TRLDESCRIPTION", rsInicial.getString("TRLDESCRIPTION"), rsFuente.getString("TRLDESCRIPTION"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ISTRANSLATED", rsInicial.getString("ISTRANSLATED"), rsFuente.getString("ISTRANSLATED"));
    	
    	if (!SQLInto.toString().equals(""))
    	{	String insert = "INSERT INTO TMP_REF_LIST (AD_Client_ID,AD_Org_ID,TMP_REF_LIST_ID,TMP_REFERENCE_ID, VALUE" + SQLInto.toString() + ") VALUES ("
    			+ Env.getAD_Client_ID(Env.getCtx()) + "," + Env.getAD_Org_ID(Env.getCtx()) + ","
    			+ rsInicial.getInt("AD_REF_LIST_ID") + "," + rsInicial.getInt("AD_REFERENCE_ID") + ",'" + rsInicial.getString("VALUE") + "'" + SQLValue.toString() + ")";
    		DB.executeUpdate(insert,getTrxName());
    		return true;
    	}
    	return false;
	}
    
    private boolean InsertRefList(String ref, ResultSet rs, int Reference_ID) throws Exception{

    	StringBuffer SQLInto = new StringBuffer("");
    	StringBuffer SQLValue = new StringBuffer("");
    	
    	addBuffersStringColumna(SQLInto, SQLValue, "ISACTIVE", null, rs.getString("ISACTIVE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "VALUE", null, rs.getString("VALUE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "DESCRIPTION", null, rs.getString("DESCRIPTION"));
    	addBuffersTimestampColumna(SQLInto, SQLValue, "VALIDFROM", null, rs.getTimestamp("VALIDFROM"));
    	addBuffersTimestampColumna(SQLInto, SQLValue, "VALIDTO", null, rs.getTimestamp("VALIDTO"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ENTITYTYPE", null, rs.getString("ENTITYTYPE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "AD_LANGUAGE", null, rs.getString("AD_LANGUAGE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "TRLNAME", null, rs.getString("TRLNAME"));
    	addBuffersStringColumna(SQLInto, SQLValue, "TRLDESCRIPTION", null, rs.getString("TRLDESCRIPTION"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ISTRANSLATED", null, rs.getString("ISTRANSLATED"));
    	
    	String insert = "INSERT INTO TMP_REF_LIST (AD_Client_ID,AD_Org_ID,TMP_REF_LIST_ID,TMP_REFERENCE_ID,NAME" + SQLInto.toString() + ") VALUES ("
    		+ Env.getAD_Client_ID(Env.getCtx()) + "," + Env.getAD_Org_ID(Env.getCtx()) + ","
    		+ rs.getInt("AD_REF_LIST_ID") + "," + Reference_ID + ",'" + ref + rs.getString("NAME") + "'" + SQLValue.toString() + ")";
    	DB.executeUpdate(insert,getTrxName());
    	return true;
	}
    
    private boolean CompararList(int ReferenceInicial, int ReferenceFuente) throws Exception{
    	boolean change = false;
    	
    	try {
        	String sql = " SELECT rl.*,rlt.AD_LaNGUAGE,rlt.name as TRLNAME, rlt.description as TRLDESCRIPTION, rlt.istranslated " +
        				 " FROM ad_ref_list rl " +
        				 " LEFT JOIN ad_ref_list_trl rlt ON(rl.ad_ref_list_id=rlt.ad_ref_list_id) " +
        				 " Where rl.AD_Client_ID = " + Env.getAD_Client_ID(Env.getCtx()) + " AND rl.AD_Reference_ID = ? " +
        				 " Order by rl.NAME";
        	PreparedStatement psFuente = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        	psFuente.setInt(1, ReferenceFuente);
            ResultSet rsFuente = psFuente.executeQuery();
            
            PreparedStatement psInicial = getConexionDestino().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psInicial.setInt(1, ReferenceInicial);
            ResultSet rsInicial = psInicial.executeQuery();
            
            List<String> presentes = new ArrayList<String>();
            while (rsInicial.next())
            {
            	boolean enc = false;
            	ResultSet rs = psFuente.executeQuery();
            	while (rs.next() && !enc)
            		if (rsInicial.getString("VALUE").equals(rs.getString("VALUE")))
            		{
            			change = (CompararListItem(rsInicial,rs) || change ? true : false);
                		presentes.add(rsInicial.getString("VALUE"));
                		enc=true;
            		}
            	if (!enc)
            		change = (InsertRefList("<-- ",rsInicial,ReferenceInicial) || change ? true : false);
            	rs.close();
            }
            
            ResultSet rs = psFuente.executeQuery();
            while (rs.next())
            	if (!presentes.contains(rs.getString("VALUE")))
            		change = (InsertRefList("--> ",rs,ReferenceInicial) || change ? true : false);
            
            rsInicial.close();
            rsFuente.close();
            psInicial.close();
            psFuente.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionCompareFrame.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            setMensajeSalida("Error en la Importación");
            throw ex;
        }
        
        return change;
    }
    
/**
    AD_REFERENCE_ID                NOT NULL NUMBER(10)                                                                                                                                                                                    
   	AD_CLIENT_ID                   NOT NULL NUMBER(10)                                                                                                                                                                                    
   	AD_ORG_ID                      NOT NULL NUMBER(10)                                                                                                                                                                                    
   	ISACTIVE                       NOT NULL CHAR(1)                                                                                                                                                                                       
   	CREATED                        NOT NULL DATE                                                                                                                                                                                          
   	CREATEDBY                      NOT NULL NUMBER(10)                                                                                                                                                                                    
   	UPDATED                        NOT NULL DATE                                                                                                                                                                                          
   	UPDATEDBY                      NOT NULL NUMBER(10)                                                                                                                                                                                    
   	AD_TABLE_ID                    NOT NULL NUMBER(10)                                                                                                                                                                                    
   	AD_KEY                         NOT NULL NUMBER(10)                                                                                                                                                                                    
   	AD_DISPLAY                     NOT NULL NUMBER(10)                                                                                                                                                                                    
   	ISVALUEDISPLAYED               NOT NULL CHAR(1)                                                                                                                                                                                       
   	WHERECLAUSE                    NVARCHAR2(2000)                                                                                                                                                                               
   	ORDERBYCLAUSE                  NVARCHAR2(2000)                                                                                                                                                                               
   	ENTITYTYPE                     NOT NULL VARCHAR2(4)                                                                                                                                                                                   
*/
    private boolean CompararTableItem(ResultSet rsInicial, ResultSet rsFuente, StringBuffer SQLInto, StringBuffer SQLValue) throws Exception{
    	
    	addBuffersStringColumna(SQLInto, SQLValue, "ISACTIVE_T", rsInicial.getString("ISACTIVE"), rsFuente.getString("ISACTIVE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "TableName", rsInicial.getString("TableName"), rsFuente.getString("TableName"));
    	addBuffersStringColumna(SQLInto, SQLValue, "KeyName", rsInicial.getString("KeyName"), rsFuente.getString("KeyName"));
    	addBuffersStringColumna(SQLInto, SQLValue, "DisplayName", rsInicial.getString("DisplayName"), rsFuente.getString("DisplayName"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ENTITYTYPE_T", rsInicial.getString("ENTITYTYPE"), rsFuente.getString("ENTITYTYPE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "WHERECLAUSE", rsInicial.getString("WHERECLAUSE"), rsFuente.getString("WHERECLAUSE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ORDERBYCLAUSE", rsInicial.getString("ORDERBYCLAUSE"), rsFuente.getString("ORDERBYCLAUSE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ISVALUEDISPLAYED", rsInicial.getString("ISVALUEDISPLAYED"), rsFuente.getString("ISVALUEDISPLAYED"));
    	
    	return !SQLInto.toString().equals("");
	}
    
    private boolean InsertarTableItem(String ref, ResultSet rs, StringBuffer SQLInto, StringBuffer SQLValue) throws Exception{

    	addBuffersStringColumna(SQLInto, SQLValue, "ISACTIVE_T", null, rs.getString("ISACTIVE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "TableName", null, ref + rs.getString("TableName"));
    	addBuffersStringColumna(SQLInto, SQLValue, "KeyName", null, rs.getString("KeyName"));
    	addBuffersStringColumna(SQLInto, SQLValue, "DisplayName", null, rs.getString("DisplayName"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ENTITYTYPE_T", null, rs.getString("ENTITYTYPE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "WHERECLAUSE", null, rs.getString("WHERECLAUSE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ORDERBYCLAUSE", null, rs.getString("ORDERBYCLAUSE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ISVALUEDISPLAYED", null, rs.getString("ISVALUEDISPLAYED"));
    	
    	return !SQLInto.toString().equals("");
	}
    
/**
 * @param ReferenceInicial
 * @param ReferenceFuente
 * @return
 * @throws Exception
 */    
    
    private boolean CompararTable(int ReferenceInicial, int ReferenceFuente, StringBuffer SQLInto, StringBuffer SQLValue) throws Exception{
    	boolean change = false;
    	
    	try {
        	String sql = " SELECT t.name as TableName, ck.name as KeyName, cd.name as DisplayName, rt.* " +
        				 " FROM ad_ref_table rt " +
        				 " INNER JOIN ad_table t ON (rt.ad_table_id=t.ad_table_id) " +
        				 " INNER JOIN ad_column ck ON (ck.ad_column_id= rt.ad_key) " +
        				 " INNER JOIN ad_column cd ON (cd.ad_column_id= rt.ad_display) " +
        				 " Where AD_Client_ID = " + Env.getAD_Client_ID(Env.getCtx()) + " AND AD_Reference_ID = ? "; 
        	PreparedStatement psFuente = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        	psFuente.setInt(1, ReferenceFuente);
            ResultSet rsFuente = psFuente.executeQuery();
            
            PreparedStatement psInicial = getConexionDestino().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psInicial.setInt(1, ReferenceInicial);
            ResultSet rsInicial = psInicial.executeQuery();
            
            if (rsInicial.next())
            	if (rsFuente.next())
            		change = CompararTableItem(rsInicial,rsFuente,SQLInto,SQLValue);
            	else
            		change = InsertarTableItem("<-- ",rsInicial,SQLInto,SQLValue);
            else
            	if (rsFuente.next())
            		change = InsertarTableItem("--> ",rsFuente,SQLInto,SQLValue);
            		
            rsInicial.close();
            rsFuente.close();
            psInicial.close();
            psFuente.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionCompareFrame.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            setMensajeSalida("Error en la Importación");
            throw ex;
        }
        
        return change;
    }
}
