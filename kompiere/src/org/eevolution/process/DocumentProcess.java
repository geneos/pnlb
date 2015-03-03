package org.eevolution.process;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.model.*;
import org.compiere.util.*;

public class DocumentProcess {
	
	/**************************************************************************
	 * 	Create DB
	 *	@param args
	 */
	public static void main (String[] args)
	{
		Compiere.startup(true);
		CLogMgt.setLevel(Level.FINE);
		CLogMgt.setLoggerLevel(Level.FINE,null);
		
		 String sql = "SELECT * FROM C_Invoice i WHERE i.AD_Client_ID = 1000001 AND DocumentNo LIKE '%R-CC%'  AND i.IsSOTrx='N'";              
			
			PreparedStatement pstmt = null;
			try
			{			
	                        pstmt = DB.prepareStatement (sql);
	                        ResultSet rs = pstmt.executeQuery ();
	                        
	                        while (rs.next())
	                        {
	                        	MInvoice invoice = new MInvoice(Env.getCtx(), rs, null);
	                        	invoice.completeIt();
	                        	invoice.save(null);
	                        }
	                        rs.close();
	                        pstmt.close();
			}
			catch (Exception e)
			{
				//log.log(Level.SEVERE,"doIt - " + sql, e);
				
				System.out.println("Compiere " + e);
			}

	}

}
