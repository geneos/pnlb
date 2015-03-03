/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process.importacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.compiere.model.MColumn;
import org.compiere.model.M_Element;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author daniel
 */
public class ComparacionTableColumn extends CompareProcess{

    public ComparacionTableColumn(Connection cd, Connection cf){
        setConexionFuente(cf);
        setConexionDestino(cd);
        setNombreColumna("TABLENAME");
        setNombreTabla("AD_TABLE");
        setMensaje("Se van a buscar diferencias entre tablas, luego verá los resultados en el reporte.");
        setTitMensaje("Comparación Table and Column");
    }

    protected boolean Comparar() throws Exception{
        try {
        	
        	DB.executeUpdate("DELETE FROM TMP_TABLE",getTrxName());
        	DB.executeUpdate("DELETE FROM T_COLUMN",getTrxName());
        	DB.executeUpdate("DELETE FROM T_COLUMNFUENTE",getTrxName());
        	DB.executeUpdate("DELETE FROM T_COLUMNINICIAL",getTrxName());
        	
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
            
            while (continuar)
            {
            	String rsI = rsInicial.getString(getNombreColumna());
            	String rsF = rsFuente.getString(getNombreColumna());
            	if (rsI.equals(rsF))
            	{
            		CompararTablas(rsInicial,rsFuente);
            		rsInicial.next();
                    rsFuente.next();
                }
            	else
           			rsInicial.next();
            	if (rsInicial.isAfterLast() || rsFuente.isAfterLast())
                	continuar = false;
            }
            if (!rsFuente.isAfterLast())
            {	
            	setMensajeSalida("Existen datos en la Base de Datos Fuente, que no se encuentran en la Base de Datos Inicial.");
            	Exception e = new Exception();
            	throw e;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionCompareFrame.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            throw ex;
        }
        return true;
    }
    
//  Atributos de la Tabla
    /*
        AD_CLIENT_ID		NUMBER(10,0) NOT NULL ENABLE, 
       	AD_ORG_ID			NUMBER(10,0) NOT NULL ENABLE, 
       	ISACTIVE			CHAR(1 BYTE) DEFAULT 'Y' NOT NULL ENABLE,
       	T_TABLE_ID			NUMBER(10,0) NOT NULL ENABLE,
       	TABLENAME			VARCHAR2(40 BYTE) NOT NULL ENABLE,
       	 
       	NAME				NVARCHAR2(60) NOT NULL ENABLE, 
       	DESCRIPTION			NVARCHAR2(255), 
       	HELP				NVARCHAR2(2000), 
       	ISVIEW				CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE, 
       	ACCESSLEVEL			CHAR(1 BYTE) NOT NULL ENABLE, 
       	ENTITYTYPE			VARCHAR2(4 BYTE) DEFAULT 'D' NOT NULL ENABLE,
       	AD_WINDOW_ID		NUMBER(10,0), 
       	AD_VAL_RULE_ID		NUMBER(10,0), 
       	LOADSEQ				NUMBER(10,0),
       	ISSECURITYENABLED	CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE, 
       	ISDELETEABLE		CHAR(1 BYTE) DEFAULT 'Y' NOT NULL ENABLE, 
       	ISHIGHVOLUME		CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE, 
       	IMPORTTABLE			CHAR(1 BYTE), 
       	ISCHANGELOG			CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE, 
       	REPLICATIONTYPE		CHAR(1 BYTE) DEFAULT 'L' NOT NULL ENABLE, 
       	PO_WINDOW_ID		NUMBER(10,0), 
   */
    
    private void CompararTablas(ResultSet rsInicial, ResultSet rsFuente) throws Exception{
    	
    	StringBuffer SQLInto = new StringBuffer("");
    	StringBuffer SQLValue = new StringBuffer("");
    	
    	addBuffersString(SQLInto, SQLValue, "DESCRIPTION", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "HELP", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "ISVIEW", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "ACCESSLEVEL", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "ENTITYTYPE", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "HELP", rsInicial, rsFuente);
    	addBuffersInteger(SQLInto, SQLValue, "AD_WINDOW_ID", rsInicial, rsFuente);
    	addBuffersInteger(SQLInto, SQLValue, "AD_VAL_RULE_ID", rsInicial, rsFuente);
    	addBuffersInteger(SQLInto, SQLValue, "LOADSEQ", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "ISSECURITYENABLED", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "ISDELETEABLE", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "ISHIGHVOLUME", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "IMPORTTABLE", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "ISCHANGELOG", rsInicial, rsFuente);
    	addBuffersString(SQLInto, SQLValue, "REPLICATIONTYPE", rsInicial, rsFuente);
    	addBuffersInteger(SQLInto, SQLValue, "PO_WINDOW_ID", rsInicial, rsFuente);
    	
    	boolean change = CompararColumnas(rsInicial.getInt(getNombreTabla()+"_ID"),rsFuente.getInt(getNombreTabla()+"_ID"));
    	
    	if (change || !SQLInto.toString().equals(""))
    	{	String insert = "INSERT INTO TMP_Table (AD_Client_ID,AD_Org_ID,TMP_Table_ID,IsActive,TABLENAME,NAME" + SQLInto.toString() + ") VALUES ("
    			+ Env.getAD_Client_ID(Env.getCtx()) + "," + Env.getAD_Org_ID(Env.getCtx()) + ","
    			+ Integer.toString(rsInicial.getInt(getNombreTabla()+"_ID")) + ",'Y','" + rsInicial.getString(getNombreColumna()) + "','" + rsInicial.getString(getNombreColumna()) + "'" + SQLValue.toString() + ")";
    		DB.executeUpdate(insert,getTrxName());
    	}
	}
    
//  Atributos de la Tabla
   /*    
   	AD_CLIENT_ID			NUMBER(10,0) NOT NULL ENABLE, 
   	AD_ORG_ID				NUMBER(10,0) NOT NULL ENABLE, 
   	ISACTIVE				CHAR(1 BYTE) DEFAULT 'Y' NOT NULL ENABLE, 

    T_COLUMN_ID				NUMBER(10,0) NOT NULL ENABLE, 
   	NAME					NVARCHAR2(60) NOT NULL ENABLE, 
   	DESCRIPTION				NVARCHAR2(255), 
   	HELP					NVARCHAR2(2000), 
   	VERSION					NUMBER NOT NULL ENABLE, 
   	ENTITYTYPE				VARCHAR2(4 BYTE) DEFAULT 'D' NOT NULL ENABLE, 
   	COLUMNNAME				VARCHAR2(40 BYTE) NOT NULL ENABLE, 
   	T_TABLE_ID				NUMBER(10,0) NOT NULL ENABLE, 
   	AD_REFERENCE_ID			NUMBER(10,0) NOT NULL ENABLE, 
   	AD_REFERENCE_VALUE_ID	NUMBER(10,0), 
   	AD_VAL_RULE_ID			NUMBER(10,0),
   	FIELDLENGTH				NUMBER(10,0), 
   	DEFAULTVALUE			NVARCHAR2(2000),
   	ISKEY					CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE, 
   	ISPARENT				CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE, 
   	ISMANDATORY				CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE, 
   	ISUPDATEABLE			CHAR(1 BYTE) DEFAULT 'Y' NOT NULL ENABLE,
   	READONLYLOGIC			NVARCHAR2(2000),
   	ISIDENTIFIER			CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE, 
   	SEQNO					NUMBER(10,0), 
   	ISTRANSLATED			CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE,
   	ISENCRYPTED				CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE,
   	CALLOUT					NVARCHAR2(255), 
   	VFORMAT					NVARCHAR2(60), 
   	VALUEMIN				NVARCHAR2(20), 
   	VALUEMAX				NVARCHAR2(20), 
   	ISSELECTIONCOLUMN		CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE, 
   	AD_ELEMENT_ID			NUMBER(10,0), 
   	AD_PROCESS_ID			NUMBER(10,0), 
   	ISSYNCDATABASE			CHAR(1 BYTE) DEFAULT 'N', 
   	ISALWAYSUPDATEABLE		CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE, 
   	COLUMNSQL				NVARCHAR2(255), 
   */	
    
    private boolean CompararColumna(MColumn columnInicial, ResultSet columnFuente) throws Exception{
		boolean change = false;
		
    	StringBuffer SQLInto = new StringBuffer("");
    	StringBuffer SQLValue = new StringBuffer("");
    	
    	addBuffersStringColumna(SQLInto, SQLValue, "DESCRIPTION", columnInicial.getDescription(), columnFuente.getString("DESCRIPTION"));
    	addBuffersStringColumna(SQLInto, SQLValue, "HELP", columnInicial.getHelp(), columnFuente.getString("HELP"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ENTITYTYPE", columnInicial.getEntityType(), columnFuente.getString("ENTITYTYPE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "DEFAULTVALUE", columnInicial.getDefaultValue(), columnFuente.getString("DEFAULTVALUE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "READONLYLOGIC", columnInicial.getReadOnlyLogic(), columnFuente.getString("READONLYLOGIC"));
    	addBuffersStringColumna(SQLInto, SQLValue, "CALLOUT", columnInicial.getCallout(), columnFuente.getString("CALLOUT"));
    	addBuffersStringColumna(SQLInto, SQLValue, "VFORMAT", columnInicial.getVFormat(), columnFuente.getString("VFORMAT"));
    	addBuffersStringColumna(SQLInto, SQLValue, "VALUEMIN", columnInicial.getValueMin(), columnFuente.getString("VALUEMIN"));
    	addBuffersStringColumna(SQLInto, SQLValue, "VALUEMAX", columnInicial.getValueMax(), columnFuente.getString("VALUEMAX"));
    	addBuffersStringColumna(SQLInto, SQLValue, "COLUMNSQL", columnInicial.getColumnSQL(), columnFuente.getString("COLUMNSQL"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ISENCRYPTED", columnInicial.getIsEncrypted(), columnFuente.getString("ISENCRYPTED"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ISSYNCDATABASE", columnInicial.getIsSyncDatabase(), columnFuente.getString("ISSYNCDATABASE"));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISKEY", columnInicial.isKey(), (columnFuente.getString("ISKEY").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISPARENT", columnInicial.isParent(), (columnFuente.getString("ISPARENT").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISMANDATORY", columnInicial.isMandatory(), (columnFuente.getString("ISMANDATORY").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISUPDATEABLE", columnInicial.isUpdateable(), (columnFuente.getString("ISUPDATEABLE").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISIDENTIFIER", columnInicial.isIdentifier(), (columnFuente.getString("ISIDENTIFIER").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISTRANSLATED", columnInicial.isTranslated(), (columnFuente.getString("ISTRANSLATED").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISSELECTIONCOLUMN", columnInicial.isSelectionColumn(), (columnFuente.getString("ISSELECTIONCOLUMN").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISALWAYSUPDATEABLE", columnInicial.isAlwaysUpdateable(), (columnFuente.getString("ISALWAYSUPDATEABLE").equals("Y") ? true : false));
    	addBuffersBigDecimalColumna(SQLInto, SQLValue, "VERSION", columnInicial.getVersion(), columnFuente.getBigDecimal("VERSION"));
    	addBuffersIntegerColumna(SQLInto, SQLValue, "FIELDLENGTH", columnInicial.getFieldLength(), columnFuente.getInt("FIELDLENGTH"));
    	addBuffersIntegerColumna(SQLInto, SQLValue, "SEQNO", columnInicial.getSeqNo(), columnFuente.getInt("SEQNO"));
    	// TODO REVISAR CAMBIOS
    	M_Element element = new M_Element(Env.getCtx(),columnInicial.getAD_Element_ID(),null);
    	addBuffersStringColumna(SQLInto, SQLValue, "AD_ELEMENT_ID", element.getName(), columnFuente.getString("ELEMENT"));
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_REFERENCE_ID", columnInicial.getAD_Reference_ID(), columnFuente.getInt("AD_REFERENCE_ID"));
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_REFERENCE_VALUE_ID", columnInicial.getAD_Reference_Value_ID(), columnFuente.getInt("AD_REFERENCE_VALUE_ID"));
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_VAL_RULE_ID", columnInicial.getAD_Val_Rule_ID(), columnFuente.getInt("AD_VAL_RULE_ID"));
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_PROCESS_ID", columnInicial.getAD_Process_ID(), columnFuente.getInt("AD_PROCESS_ID"));
    	
    	if (!SQLInto.toString().equals(""))
    	{	String insert = "INSERT INTO T_Column (AD_Client_ID,AD_Org_ID,T_Column_ID,TMP_Table_ID,IsActive,COLUMNNAME,NAME" + SQLInto.toString() + ") VALUES ("
    			+ Env.getAD_Client_ID(Env.getCtx()) + "," + Env.getAD_Org_ID(Env.getCtx()) + ","
    			+ columnInicial.getAD_Column_ID() + "," + columnInicial.getAD_Table_ID() + ",'Y','" + columnInicial.getColumnName() + "','" + columnInicial.getColumnName() + "'" + SQLValue.toString() + ")";
    		DB.executeUpdate(insert,getTrxName());
    		return true;
    	}
    	return change;
	}
    
    private boolean InsertColumnaInicial(MColumn columnInicial) throws Exception{
    	StringBuffer SQLInto = new StringBuffer("");
    	StringBuffer SQLValue = new StringBuffer("");
    	
    	addBuffersStringColumna(SQLInto, SQLValue, "DESCRIPTION", null, columnInicial.getDescription());
    	addBuffersStringColumna(SQLInto, SQLValue, "HELP", null,columnInicial.getHelp());
    	addBuffersStringColumna(SQLInto, SQLValue, "ENTITYTYPE", null, columnInicial.getEntityType());
    	addBuffersStringColumna(SQLInto, SQLValue, "DEFAULTVALUE", null, columnInicial.getDefaultValue());
    	addBuffersStringColumna(SQLInto, SQLValue, "READONLYLOGIC", null,columnInicial.getReadOnlyLogic());
    	addBuffersStringColumna(SQLInto, SQLValue, "CALLOUT", null,columnInicial.getCallout());
    	addBuffersStringColumna(SQLInto, SQLValue, "VFORMAT", null,columnInicial.getVFormat());
    	addBuffersStringColumna(SQLInto, SQLValue, "VALUEMIN", null,columnInicial.getValueMin());
    	addBuffersStringColumna(SQLInto, SQLValue, "VALUEMAX", null,columnInicial.getValueMax());
    	addBuffersStringColumna(SQLInto, SQLValue, "COLUMNSQL", null,columnInicial.getColumnSQL());
    	addBuffersStringColumna(SQLInto, SQLValue, "ISENCRYPTED", null,columnInicial.getIsEncrypted());
    	addBuffersStringColumna(SQLInto, SQLValue, "ISSYNCDATABASE", null, columnInicial.getIsSyncDatabase());
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISKEY", false,columnInicial.isKey());
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISPARENT", false, columnInicial.isParent());
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISMANDATORY", false, columnInicial.isMandatory());
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISUPDATEABLE", false, columnInicial.isUpdateable());
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISIDENTIFIER", false, columnInicial.isIdentifier());
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISTRANSLATED", false,columnInicial.isTranslated());
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISSELECTIONCOLUMN", false,columnInicial.isSelectionColumn());
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISALWAYSUPDATEABLE", false,columnInicial.isAlwaysUpdateable());
    	addBuffersBigDecimalColumna(SQLInto, SQLValue, "VERSION", new BigDecimal(0),columnInicial.getVersion());
    	addBuffersIntegerColumna(SQLInto, SQLValue, "FIELDLENGTH", 0,columnInicial.getFieldLength());
    	addBuffersIntegerColumna(SQLInto, SQLValue, "SEQNO", 0,columnInicial.getSeqNo());
    	// TODO REVISAR CAMBIOS
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_REFERENCE_ID", 0, columnInicial.getAD_Reference_ID());
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_REFERENCE_VALUE_ID", 0, columnInicial.getAD_Reference_Value_ID());
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_VAL_RULE_ID", 0, columnInicial.getAD_Val_Rule_ID());
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_ELEMENT_ID", 0,columnInicial.getAD_Element_ID());
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_PROCESS_ID", 0,columnInicial.getAD_Process_ID());
    	
  		String insert = "INSERT INTO T_ColumnInicial (AD_Client_ID,AD_Org_ID,T_ColumnInicial_ID,TMP_Table_ID,IsActive,COLUMNNAME,NAME" + SQLInto.toString() + ") VALUES ("
    		+ Env.getAD_Client_ID(Env.getCtx()) + "," + Env.getAD_Org_ID(Env.getCtx()) + ","
    		+ columnInicial.getAD_Column_ID() + "," + columnInicial.getAD_Table_ID() + ",'Y','" + columnInicial.getColumnName() + "','" + columnInicial.getColumnName() + "'" + SQLValue.toString() + ")";
    	DB.executeUpdate(insert,getTrxName());
    	return true;
	}
    
    private boolean InsertColumnaFuente(ResultSet columnFuente, int Table_id) throws Exception{
    	StringBuffer SQLInto = new StringBuffer("");
    	StringBuffer SQLValue = new StringBuffer("");
    	
    	addBuffersStringColumna(SQLInto, SQLValue, "DESCRIPTION", null, columnFuente.getString("DESCRIPTION"));
    	addBuffersStringColumna(SQLInto, SQLValue, "HELP", null, columnFuente.getString("HELP"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ENTITYTYPE", null, columnFuente.getString("ENTITYTYPE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "DEFAULTVALUE", null, columnFuente.getString("DEFAULTVALUE"));
    	addBuffersStringColumna(SQLInto, SQLValue, "READONLYLOGIC", null, columnFuente.getString("READONLYLOGIC"));
    	addBuffersStringColumna(SQLInto, SQLValue, "CALLOUT", null, columnFuente.getString("CALLOUT"));
    	addBuffersStringColumna(SQLInto, SQLValue, "VFORMAT", null, columnFuente.getString("VFORMAT"));
    	addBuffersStringColumna(SQLInto, SQLValue, "VALUEMIN", null, columnFuente.getString("VALUEMIN"));
    	addBuffersStringColumna(SQLInto, SQLValue, "VALUEMAX", null, columnFuente.getString("VALUEMAX"));
    	addBuffersStringColumna(SQLInto, SQLValue, "COLUMNSQL", null, columnFuente.getString("COLUMNSQL"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ISENCRYPTED", null, columnFuente.getString("ISENCRYPTED"));
    	addBuffersStringColumna(SQLInto, SQLValue, "ISSYNCDATABASE", null, columnFuente.getString("ISSYNCDATABASE"));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISKEY", false, (columnFuente.getString("ISKEY").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISPARENT", false, (columnFuente.getString("ISPARENT").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISMANDATORY", false, (columnFuente.getString("ISMANDATORY").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISUPDATEABLE", false, (columnFuente.getString("ISUPDATEABLE").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISIDENTIFIER", false, (columnFuente.getString("ISIDENTIFIER").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISTRANSLATED", false, (columnFuente.getString("ISTRANSLATED").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISSELECTIONCOLUMN", false, (columnFuente.getString("ISSELECTIONCOLUMN").equals("Y") ? true : false));
    	addBuffersBooleanColumna(SQLInto, SQLValue, "ISALWAYSUPDATEABLE", false, (columnFuente.getString("ISALWAYSUPDATEABLE").equals("Y") ? true : false));
    	addBuffersBigDecimalColumna(SQLInto, SQLValue, "VERSION", new BigDecimal(0), columnFuente.getBigDecimal("VERSION"));
    	addBuffersIntegerColumna(SQLInto, SQLValue, "FIELDLENGTH", 0, columnFuente.getInt("FIELDLENGTH"));
    	addBuffersIntegerColumna(SQLInto, SQLValue, "SEQNO", 0, columnFuente.getInt("SEQNO"));
    	// TODO REVISAR CAMBIOS
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_REFERENCE_ID", 0, columnFuente.getInt("AD_REFERENCE_ID"));
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_REFERENCE_VALUE_ID", 0, columnFuente.getInt("AD_REFERENCE_VALUE_ID"));
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_VAL_RULE_ID", 0, columnFuente.getInt("AD_VAL_RULE_ID"));
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_ELEMENT_ID", 0, columnFuente.getInt("AD_ELEMENT_ID"));
    	addBuffersIntegerColumna(SQLInto, SQLValue, "AD_PROCESS_ID", 0, columnFuente.getInt("AD_PROCESS_ID"));
    	
    	String insert = "INSERT INTO T_ColumnFuente (AD_Client_ID,AD_Org_ID,T_ColumnFuente_ID,TMP_Table_ID,IsActive,COLUMNNAME,NAME" + SQLInto.toString() + ") VALUES ("
    		+ Env.getAD_Client_ID(Env.getCtx()) + "," + Env.getAD_Org_ID(Env.getCtx()) + ","
    		+ columnFuente.getInt("AD_Column_ID") + "," + Table_id + ",'Y','" + columnFuente.getString("ColumnName") + "','" + columnFuente.getString("ColumnName") + "'" + SQLValue.toString() + ")";
    	DB.executeUpdate(insert,getTrxName());
    	return true;
	}
    
    private boolean CompararColumnas(int TableInicial, int TableFuente) throws Exception{
    	boolean change = false;
    	
    	try {
        	String sql = " Select co.*, el.name as ELEMENT" +
        				 " FROM AD_Column co " +
        				 " Inner join ad_element el On (el.ad_element_id=co.ad_element_id) " +
        				 " Where co.AD_Table_ID = ? AND co.AD_Client_ID = " + Env.getAD_Client_ID(Env.getCtx()) +
        				 " Order by co.ColumnName";
        	PreparedStatement psFuente = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        	psFuente.setInt(1, TableFuente);
            //ResultSet rsFuente = psFuente.executeQuery();
            
            PreparedStatement psInicial = getConexionDestino().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psInicial.setInt(1, TableInicial);
            ResultSet rsInicial = psInicial.executeQuery();
            
            List<MColumn> columnInicial = getColumnas(rsInicial);
            
            List<String> presentes = new ArrayList<String>();
            for (int i=0;i<columnInicial.size();i++)
            {
            	boolean enc = false;
            	ResultSet rs = psFuente.executeQuery();
            	while (rs.next() && !enc)
            		if (columnInicial.get(i).getColumnName().equals(rs.getString("COLUMNNAME")))
            		{
            			change = (CompararColumna(columnInicial.get(i),rs) || change ? true : false);
                		presentes.add(columnInicial.get(i).getColumnName());
                		enc=true;
            		}
            	if (!enc)
            		change = (InsertColumnaInicial(columnInicial.get(i)) || change ? true : false);
            	rs.close();
            }
            ResultSet rs = psFuente.executeQuery();
            while (rs.next())
            	if (!presentes.contains(rs.getString("COLUMNNAME")))
            		change = (InsertColumnaFuente(rs,TableInicial) || change ? true : false);

            rsInicial.close();
            rs.close();
            psInicial.close();
            psFuente.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionCompareFrame.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            throw ex;
        }
        
        return change;
    }

	private List<MColumn> getColumnas(ResultSet rs) {
		int column_id = 0;
		List<MColumn> columns = new ArrayList<MColumn>();
		try {
			while (rs.next()){
				column_id = rs.getInt("AD_Column_ID");
				columns.add(new MColumn(Env.getCtx(),column_id,getTrxName()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			setMensajeSalida("E: " + column_id + " no existe.");
			return null;
		}

		return columns;
    }

}
