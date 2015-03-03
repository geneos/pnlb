/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Portions created by Carlos Ruiz are Copyright (C) 2005 QSS Ltda.
 * Add e-Evolution by Perez Juarez
 * Contributor(s): Carlos Ruiz (globalqss)
 *****************************************************************************/
package org.eevolution.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.*;

import org.compiere.model.X_M_Product;
import org.compiere.util.*;
import org.compiere.process.*;

/**
 * Title:	Check BOM Structure (free of cycles)
 * Description:
 *		Tree cannot contain BOMs which are already referenced
 *	
 *  @author Carlos Ruiz (globalqss)
 *  @version $Id: M_Product_BOM_Check.java,v 1.0 2005/09/17 13:32:00 globalqss Exp $
 */
public class M_Product_BOM_Check extends SvrProcess
{

	/** The Record						*/
	private int		p_Record_ID = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_Record_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
        StringBuffer sql1 = null;
		int no = 0;

		log.info("Check BOM Structure");

		//	Record ID is M_Product_ID of product to be tested
		X_M_Product xp = new X_M_Product(Env.getCtx(), p_Record_ID, get_TrxName());
		
		if (! xp.isBOM()) {
			log.info("NOT BOM Product");
			// No BOM - should not happen, but no problem
			xp.setIsVerified(true);
			xp.save(get_TrxName());
			return "OK";
		}

		// Table to put all BOMs - duplicate will cause exception
        sql1 = new StringBuffer("DELETE FROM T_Selection2 WHERE Query_ID = 0");
        no = DB.executeUpdate(sql1.toString());
        sql1 = new StringBuffer("INSERT INTO T_Selection2 (Query_ID, T_Selection_ID) VALUES (0, " + p_Record_ID + ")");
        no = DB.executeUpdate(sql1.toString());
		// Table of root modes
        sql1 = new StringBuffer("DELETE FROM T_Selection");
        no = DB.executeUpdate(sql1.toString());
        sql1 = new StringBuffer("INSERT INTO T_Selection (T_Selection_ID) VALUES (" + p_Record_ID + ")");
        no = DB.executeUpdate(sql1.toString());
        
        while (true) {
        	
    		//	Get count remaining on t_selection
    		int countno = 0;
    		try
    		{
    			PreparedStatement pstmt = DB.prepareStatement
    				("SELECT COUNT(*) FROM T_Selection");
    			ResultSet rs = pstmt.executeQuery();
    			if (rs.next())
    				countno = rs.getInt(1);
    			rs.close();
    			pstmt.close();
    		}
    		catch (SQLException e)
    		{
    			throw new Exception ("count t_selection", e);
    		}
    		log.fine("Count T_Selection =" + countno);
    		
    		if (countno == 0)
    			break;

    		try
    		{
    			// if any command fails (no==-1) break and inform failure 
    			// Insert BOM Nodes into "All" table
    			sql1 = new StringBuffer("INSERT INTO T_Selection2 (Query_ID, T_Selection_ID) SELECT 0, p.M_Product_ID FROM M_Product p WHERE IsBOM='Y' AND EXISTS (SELECT * FROM M_Product_BOM b WHERE p.M_Product_ID=b.M_ProductBOM_ID AND b.M_Product_ID IN (SELECT T_Selection_ID FROM T_Selection))");
    			no = DB.executeUpdate(sql1.toString());
    			if (no == -1) raiseError("InsertingRoot:ERROR", sql1.toString());
    			// Insert BOM Nodes into temporary table
    			sql1 = new StringBuffer("DELETE FROM T_Selection2 WHERE Query_ID = 1");
    			no = DB.executeUpdate(sql1.toString());
    			if (no == -1) raiseError("InsertingRoot:ERROR", sql1.toString());;
    			sql1 = new StringBuffer("INSERT INTO T_Selection2 (Query_ID, T_Selection_ID) SELECT 1, p.M_Product_ID FROM M_Product p WHERE IsBOM='Y' AND EXISTS (SELECT * FROM M_Product_BOM b WHERE p.M_Product_ID=b.M_ProductBOM_ID AND b.M_Product_ID IN (SELECT T_Selection_ID FROM T_Selection))");
    			no = DB.executeUpdate(sql1.toString());
    			if (no == -1) raiseError("InsertingRoot:ERROR", sql1.toString());;
    			// Copy into root table
    			sql1 = new StringBuffer("DELETE FROM T_Selection");
    			no = DB.executeUpdate(sql1.toString());
    			if (no == -1) raiseError("InsertingRoot:ERROR", sql1.toString());;
    			sql1 = new StringBuffer("INSERT INTO T_Selection (T_Selection_ID) SELECT T_Selection_ID FROM T_Selection2 WHERE Query_ID = 1");
    			no = DB.executeUpdate(sql1.toString());
    			if (no == -1) raiseError("InsertingRoot:ERROR", sql1.toString());;
    		}
    		catch (Exception e)
    		{
    			throw new Exception ("root insert", e);
    		}
        	
        }

        // Finish process
		xp.setIsVerified(true);
		xp.save(get_TrxName());
		return "OK";
	}	//	doIt
	
	private void raiseError(String string, String sql) throws Exception {
		DB.rollback(false, get_TrxName());
		String msg = string;
		ValueNamePair pp = CLogger.retrieveError();
		if (pp != null)
			msg = pp.getName() + " - ";
		msg += sql;
		throw new CompiereUserError (msg);
	}
	
}	//	M_Product_BOM_Check
