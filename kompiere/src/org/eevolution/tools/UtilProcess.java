/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.tools;

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
import java.util.logging.*;
import org.compiere.model.*;

import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;
import org.compiere.process.ProcessInfo;
import org.compiere.util.*;

/**
 *
 * @author BISION
 */
public class UtilProcess {
    
    
	private static ReportEngine prepare(String printFormatName,int ad_Pinstance, ProcessInfo pi)
	throws SQLException {
		
		String process = "'" + printFormatName + "'";
        String query = "select * from AD_PRINTFORMAT where name=" + process;

        PreparedStatement pstmt = DB.prepareStatement(query, null);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        Long id = rs.getLong("ad_printformat_id");
        Long idTable = rs.getLong("ad_table_id");
        MPrintFormat format = null;
               
        Language language = Language.getLoginLanguage();
        format = MPrintFormat.get(Env.getCtx(), id.intValue(), false);
        format.setLanguage(language);
        format.setTranslationLanguage(language);

        query = "select * from AD_TABLE where ad_table_id=" + idTable.intValue();
        pstmt = DB.prepareStatement(query, null);
        rs = pstmt.executeQuery();
        rs.next();
        String name=rs.getString("name");
        MQuery mquery=MQuery.get(Env.getCtx(),ad_Pinstance,name);
      
        ReportEngine re = new ReportEngine(Env.getCtx(), format, mquery, new PrintInfo(pi));
        
        return re;
	}
	
	public static void initViewer(String printFormatName,int ad_Pinstance, ProcessInfo pi)
    {
        try {
            new Viewer(prepare(printFormatName,ad_Pinstance,pi));
        } catch (SQLException ex) {
            Logger.getLogger(UtilProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }     
    
    public static void initPrint(String printFormatName,int ad_Pinstance, ProcessInfo pi)
    {
        try {
            prepare(printFormatName,ad_Pinstance,pi).print();
        } catch (SQLException ex) {
            Logger.getLogger(UtilProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }   
}
