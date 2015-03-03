/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.eevolution.process; 


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.*;
import org.compiere.db.CompiereDatabase;
import org.compiere.db.DB_Oracle;
import org.compiere.model.*;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;


/**
 *  Esta clase inserta tuplas en la tabla temporal T_RANKING_SALE luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
 *	@author BISion
 *	@version 1.0
 */
public class CompiereExportData extends SvrProcess
{
    

       private CompiereDatabase m_dbTarget;
       String table=""; 
       String process="";
       String destino="";
       StringBuffer result=new StringBuffer();
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
            m_dbTarget=(DB_Oracle) DB.getDatabase();
            ProcessInfoParameter[] para = getParameter();
            for (int i = 0; i < para.length; i++)
	    {
			String name = para[i].getParameterName();
                        if (name.equals("table"))
                            table="'"+(String) para[i].getParameter()+"'";
                        if (name.equals("process"))
                            process="'"+(String) para[i].getParameter()+"'";
                        else
                            destino=(String) para[i].getParameter();
                       
            }
		
	}	

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
                // Definición de variables para crear los script de insert, consulta y borrado
                
                
                
                 

                String tableId;
                String reportViewId;              
                String printFormatId;
                String processId;
                
                
                
                
                //ad_table
                PreparedStatement pstm=DB.prepareStatement("Select * from ad_table where name="+table);
                ResultSet rs=pstm.executeQuery();
                rs.next();
                M_Table ad_table=M_Table.get(new Properties(),"AD_TABLE");
                tableId=getDataRow(rs, ad_table);
                
                
                //ad_table_tr
                String query="Select * from ad_table_trl where ad_table_id="+tableId.toString();
                pstm=DB.prepareStatement(query);
                rs=pstm.executeQuery();
                rs.next();
                M_Table ad_table_tr=M_Table.get(new Properties(),"AD_TABLE_TRL");
                getDataRow(rs, ad_table_tr);
                
                // ad_sequence
                
                query="Select * from ad_sequence where name="+table;
                pstm=DB.prepareStatement(query);
                rs=pstm.executeQuery();
                rs.next();
                M_Table ad_sequence=M_Table.get(new Properties(),"AD_SEQUENCE");
                getDataRow(rs, ad_sequence);

                
                // ad_element
                
                query="select * from ad_element where name in (select name from ad_column where ad_table_id="+tableId+")";
                pstm=DB.prepareStatement(query);
                rs=pstm.executeQuery();
                while (rs.next())
                {
                    M_Table ad_element=M_Table.get(new Properties(),"AD_ELEMENT");
                    String adElementId=getDataRow(rs, ad_element);                    
                    
                    query="select * from ad_element_trl where ad_element_id="+adElementId;
                    pstm=DB.prepareStatement(query);
                    ResultSet rsInt=pstm.executeQuery();
                    while (rsInt.next())
                    {
                        M_Table ad_element_trl=M_Table.get(new Properties(),"AD_ELEMENT_TRL");
                        getDataRow(rsInt, ad_element_trl);
                    }    
                    
                    
                } 
                
                //ad_table_column
                query="Select * from ad_column where ad_table_id="+tableId.toString();
                pstm=DB.prepareStatement(query);
                rs=pstm.executeQuery();
                while (rs.next())
                {
                    M_Table ad_column=M_Table.get(new Properties(),"AD_COLUMN");
                    getDataRow(rs, ad_column);
                }     
                
                
                //ad_table_column_trl
                query="select * from ad_column_trl where ad_column_id in (select ad_column_id from ad_column where ad_table_id="+tableId.toString()+")";
                pstm=DB.prepareStatement(query);
                rs=pstm.executeQuery();
                while (rs.next())
                {
                    M_Table ad_column_tr=M_Table.get(new Properties(),"AD_COLUMN_TRL");
                    getDataRow(rs, ad_column_tr);
                }    
                
                

                
                // report view
                
                query="Select * from ad_reportview where ad_table_id="+tableId.toString();
                pstm=DB.prepareStatement(query);
                rs=pstm.executeQuery();
                M_Table ad_reportview=M_Table.get(new Properties(),"AD_REPORTVIEW");
                rs.next();
                reportViewId=getDataRow(rs, ad_reportview);                
                
                // print format

                query="Select * from ad_printformat where ad_table_id="+tableId.toString();
                pstm=DB.prepareStatement(query);
                rs=pstm.executeQuery();
                M_Table ad_printformat=M_Table.get(new Properties(),"AD_PRINTFORMAT");
                while (rs.next())
                {
                        printFormatId=getDataRow(rs, ad_printformat);                     
                    
                        //print foprmat item
                        query="Select * from ad_printformatitem where ad_printformat_id="+printFormatId.toString();
                        pstm=DB.prepareStatement(query);
                        ResultSet rsInt=pstm.executeQuery();
                        while (rsInt.next())
                        {
                            M_Table ad_printformat_item=M_Table.get(new Properties(),"AD_PRINTFORMATITEM");
                            getDataRow(rsInt, ad_printformat_item);
                        }     
                        
                        //print format item trl
                        query="select * from ad_printformatitem_trl where ad_printformatitem_id in(Select ad_printformatitem_id from ad_printformatitem where ad_printformat_id="+printFormatId.toString()+")";
                        pstm=DB.prepareStatement(query);
                        rsInt=pstm.executeQuery();
                        while (rsInt.next())
                        {
                            M_Table ad_printformat_item_trl=M_Table.get(new Properties(),"AD_PRINTFORMATITEM_TRL");
                            getDataRow(rsInt, ad_printformat_item_trl);
                        }     
               }

                
                //AD_PROCESS
                pstm=DB.prepareStatement("Select * from ad_process where name="+process);
                rs=pstm.executeQuery();
                rs.next();
                M_Table ad_process=M_Table.get(new Properties(),"AD_PROCESS");
                processId=getDataRow(rs, ad_process);
                
                //AD_PROCESS
                pstm=DB.prepareStatement("Select * from ad_process_trl where ad_process_id="+processId);
                rs=pstm.executeQuery();
                rs.next();
                M_Table ad_process_trl=M_Table.get(new Properties(),"AD_PROCESS_TRL");
                getDataRow(rs, ad_process_trl);                


                //AD_PROCESS_PARA 
                pstm=DB.prepareStatement("Select * from ad_process_para where ad_process_id="+processId);
                rs=pstm.executeQuery();
                while (rs.next())
                {    
                    M_Table ad_process_para=M_Table.get(new Properties(),"AD_PROCESS_PARA");
                    String processParaId=getDataRow(rs, ad_process_para);                
                    
                    //AD_PROCESS_PARA_TRL
                    pstm=DB.prepareStatement("Select * from ad_process_para_trl where ad_process_para_id="+processParaId);
                    ResultSet rsInt=pstm.executeQuery();
                    rsInt.next();
                    M_Table ad_process_para_trl=M_Table.get(new Properties(),"AD_PROCESS_PARA_TRL");
                    getDataRow(rsInt, ad_process_para_trl);
                 }   

                
                //AD_PROCESS_ACCESS 
                pstm=DB.prepareStatement("Select * from ad_process_access where ad_process_id="+processId);
                rs=pstm.executeQuery();
                while (rs.next())
                {    
                    M_Table ad_process_access=M_Table.get(new Properties(),"AD_PROCESS_ACCESS");
                    getDataRow(rs, ad_process_access); 
                }   
                


                //AD_MENU
                pstm=DB.prepareStatement("Select * from ad_menu where ad_process_id="+processId);
                rs=pstm.executeQuery();
                while (rs.next())
                {    
                    M_Table ad_menu=M_Table.get(new Properties(),"AD_MENU");
                    String menuId=getDataRow(rs, ad_menu); 
                  
                    //AD_MENU_TRL
                    pstm=DB.prepareStatement("Select * from ad_menu_trl where ad_menu_id="+menuId);
                    ResultSet rsInt=pstm.executeQuery();
                    rsInt.next();
                    M_Table ad_menu_trl=M_Table.get(new Properties(),"AD_MENU_TRL");
                    getDataRow(rsInt, ad_menu_trl);
                    
                    
                    pstm=DB.prepareStatement("Select * from ad_treenodemm where node_id="+menuId);
                    rsInt=pstm.executeQuery();
                    rsInt.next();
                    M_Table ad_treenodemm=M_Table.get(new Properties(),"AD_TREENODEMM");
                    getDataRow(rsInt, ad_treenodemm);
     
                }   
             
                try{
                    FileWriter fileW = new FileWriter(destino);
                    BufferedWriter bw = new BufferedWriter(fileW);
                    PrintWriter salida = new PrintWriter(bw);
                    salida.println(result.toString());
                    salida.flush();
                    salida.close();
                    fileW.close();
                    bw.close();
                    }
                    catch (IOException e)
                    {
                        System.err.println ("No se puede escribir el archivo"); 
                        
                    }
                
                  String[] tables={"AD_TABLE","AD_TABLE_TRL","AD_ELEMENT","ad_column","AD_COLUMN_TRL","AD_REPORTVIEW","AD_PRINTFORMAT",
                "AD_PRINTFORMATITEM","AD_PRINTFORMATITEM_TRL","AD_PROCESS","AD_PROCESS_TRL","AD_PROCESS_PARA",
                  "AD_PROCESS_PARA_TRL","AD_PROCESS_ACCESS","AD_MENU","AD_MENU_TRL"};                  
                resetSequence(tables);
                return "success";

	}	//	doIt
        
       
        
    public String getDataRow(ResultSet rs, M_Table mTable)
    {        
                      
                      StringBuffer insert = new StringBuffer ("INSERT INTO ").append(mTable.getTableName()).append(" (");
                      StringBuffer values = new StringBuffer ();
                      String id="";
        
                      M_Column[] columns = mTable.getColumns(false);
                      for (int i = 0; i < columns.length; i++)
                      {
                           if (i != 0)
                           {
                                insert.append(",");
                                values.append(",");
                           }
                           M_Column column = columns[i];
                           String columnName = column.getColumnName();
                           insert.append(columnName);
       
                           int dt = column.getAD_Reference_ID();
                           try
                                   
                           {
                                 
                                   Object value = rs.getObject(columnName);
                                   
                                   if (columnName.toUpperCase().equals(mTable.getTableName().toUpperCase()+"_ID"))
                                   {    
                                       BigDecimal bd = rs.getBigDecimal(columnName);
                                       id=m_dbTarget.TO_NUMBER(bd, dt);
                                            
                                   }
                                   if (rs.wasNull())
                                   {
                                           values.append("NULL");
                                    }
                                   else if (columnName.endsWith("_ID")	|| DisplayType.isNumeric(dt) 
                                        || (DisplayType.isID(dt) && !columnName.equals("AD_Language"))) 
                                        {
                                              BigDecimal bd = rs.getBigDecimal(columnName);
                                              String s = m_dbTarget.TO_NUMBER(bd, dt);
                                                                    values.append(s);
                                                            }
                                                            else if (DisplayType.isDate(dt))
                                                            {
                                                                    Timestamp ts = rs.getTimestamp(columnName);
                                                                    String tsString = m_dbTarget.TO_DATE(ts, dt == DisplayType.Date);
                                                                    values.append(tsString);
                                                            }
                                                            else if (DisplayType.isLOB(dt))
                                                            {
                                                                    // ignored
                                                                    values.append("NULL");
                                                            }
                                                            else if (DisplayType.isText(dt) || dt == DisplayType.YesNo 
                                                                    || dt == DisplayType.List || dt == DisplayType.Button
                                                                    || columnName.equals("AD_Language"))
                                                            {
                                                                    String s = rs.getString(columnName);
                                                                    values.append(DB.TO_STRING(s));
                                                            }
                                                            else
                                                            {
                                                                    
                                                                    values.append("NuLl");
                                                            }
                                                    }
                                                    catch (Exception e)
                                                    {
                                                            System.out.println(" columna invalida "+columnName); 
                                                            e.printStackTrace();
                                                    }
                               }	//	for all columns

                                            //
                      
                               insert.append(") VALUES (").append(values).append(")");
                               
                              
                               
                               
                               
                               this.result.append(insert.toString()+";\n"); 
                            //   System.out.println(insert.toString()+",");
                               return id;
    
      }
        
    
    public void resetSequence(String[] tables)
    {        
        for (int i=0;i<tables.length;i++)
        try {
            PreparedStatement pstm = null;
            M_Table mTable=M_Table.get(new Properties(),tables[i]);
            String nameId;
            if (mTable.getTableName().toUpperCase().endsWith("TRL"))
              nameId=mTable.getTableName().substring(0,mTable.getTableName().length()-4)+"_ID";
            else          
                if (mTable.getTableName().toUpperCase().endsWith("ACCESS"))
                        nameId=mTable.getTableName().substring(0,mTable.getTableName().length()-7)+"_ID";
                else            
                      nameId= mTable.getTableName()+"_ID";
            
              
                
                
             pstm = DB.prepareStatement("Select * from " + mTable.getTableName() + " order by " + nameId+ " desc");
                
            
            ResultSet rs = pstm.executeQuery();
            rs.next();
            Long id=rs.getLong(nameId);
            pstm = DB.prepareStatement("update AD_SEQUENCE set CURRENTNEXT="+id+" where name='"+mTable.getTableName()+"'" );
            pstm.executeQuery();
            System.out.println("update AD_SEQUENCE set CURRENTNEXT="+id+" where name='"+mTable.getTableName()+"';");
            
        } catch (SQLException ex) {
            Logger.getLogger(CompiereExportData.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         
        
    }    
    
  
        
}	
