/*
 * POQuery.java
 *
 * Created on 28 de abril de 2004, 05:26 PM
 */

package org.compiere.util;

import java.lang.reflect.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.util.logging.*;


/**
 *
 * @author  vpj-cd
 */
public class QueryDB {
    
    private String classname;
    private String trxName;
    private static CLogger	log	= CLogger.getCLogger (QueryDB.class);
    /** Creates a new instance of POQuery */
   
    public QueryDB(String classname)
    {
      this.classname = classname;
    }
    
    
    
     public static Object newInstance(String classname, int id, String trxName ) 
     {
      Object result = null;
      Class args;
      int begin = classname.indexOf("X_") + 2 ;
      String table = classname.substring(begin);
      Class[] intArgsClass = new Class[] {Properties.class , int.class, String.class};
      //Integer height = new Integer(12);
      Integer ID = new Integer(id);
      Object[] intArgs = new Object[] {Env.getCtx(), ID,table};
      Constructor intArgsConstructor;

      try 
      {
        
        args = Class.forName(classname);
        intArgsConstructor = 
            args.getConstructor(intArgsClass);
        result = createObject(intArgsConstructor, intArgs);
        return result;
      }
      catch (ClassNotFoundException e) 
      {
          System.out.println(e);
          return result;
      } 
      catch (NoSuchMethodException e) 
      {
          System.out.println(e);
          return result;
      }
   }

   public static Object createObject(Constructor constructor, 
                                     Object[] arguments) {

      //System.out.println ("Constructor: " + constructor.toString());
      Object object = null;

      try {
        object = constructor.newInstance(arguments);
        //System.out.println ("Object: " + object.toString());
        return object;
      } catch (InstantiationException e) {
          log.log(Level.SEVERE,"InstantiationException:" + e);
      } catch (IllegalAccessException e) {
          log.log(Level.SEVERE,"IllegalAccessException:" + e);
      } catch (IllegalArgumentException e) {
          log.log(Level.SEVERE,"IllegalArgumentExceptio:" + e);
      } catch (InvocationTargetException e) {
          log.log(Level.SEVERE,"InvocationTargetException:" + e);
      }
      return object;
   }
   
   public List execute(String filter) {
       
   //String tablename = POClass.getName();     
  //System.out.print(classname.indexOf("X_"));   
  int begin = classname.indexOf("X_") + 2 ;
  String table = classname.substring(begin);
  StringBuffer sql = new StringBuffer("SELECT ").append(table).append("_ID FROM " + table);
  if (filter.equals(""))
  System.out.println("not exist filter");
  else
  sql.append(" WHERE ").append(filter);
  
  //System.out.println("Query " + sql.toString());
  
                List results = new ArrayList();
			
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			//pstmt.setInt(1, C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt(1);
                                Object element =  newInstance(classname , id, table);
                                results.add(element);				
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"VCreateFrom.initIOS - Order\nSQL=" + sql.toString(), e);
		}
                
                return results;
   }   
   
   
   public List execute() {
       
   //String tablename = POClass.getName();     
  //System.out.print(classname.indexOf("X_"));   
  int begin = classname.indexOf("X_") + 2 ;
  String table = classname.substring(begin);
  StringBuffer sql = new StringBuffer("SELECT ").append(table).append("_ID FROM " + table);
  //if (filter.equals(""))
  //System.out.println("not exist filter");
  //else
  //sql.append(" WHERE ").append(filter);
  
  //System.out.println("Query " + sql.toString());
  
                List results = new ArrayList();
			
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			//pstmt.setInt(1, C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt(1);
                                Object element =  newInstance(classname , id, table);
                                results.add(element);				
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE,"VCreateFrom.initIOS - Order\nSQL=" + sql.toString(), e);
		}
                
                return results;
   }   
   
}
    

