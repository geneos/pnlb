/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ActFact BV
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import javax.swing.JOptionPane;

import org.compiere.util.*;

/**
*	Bank Statement Model
*
*	@author Eldir Tomassen/Jorg Janke
*	@version $Id: MBankStatement.java,v 1.20 2005/11/12 22:58:56 jjanke Exp $
*/

public class MZYNMovementWarehouse extends X_ZYN_MOVEMENT_WAREHOUSE
{


        /**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MZYNMovementWarehouse.class);

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param ZYN_ZYN_MOVEMENT_WAREHOUSE_ID id
	 */
	public MZYNMovementWarehouse (Properties ctx, int ZYN_ZYN_MOVEMENT_WAREHOUSE_ID, String trxName)
	{
		super (ctx, ZYN_ZYN_MOVEMENT_WAREHOUSE_ID, trxName);
		if (ZYN_ZYN_MOVEMENT_WAREHOUSE_ID == 0)
		{
		//	setC_BankAccount_ID (0);	//	parent

		}
	}	//	MMPCOrderParentOrder

	/**
	 * 	Load Constructor
	 * 	@param ctx Current context
	 * 	@param rs result set
	 */
	public MZYNMovementWarehouse(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//

        public boolean beforeSave(boolean newRecord){    
            return true;
        }
        
        
        /**
	 * 	Check if a movement has permission to move from a warehouse,
         *      if no one restriction exist, then has permisson
	 * 	@param ctx Current context
         *      @param C_DocType_ID DocType
         *      @param M_Warehouse_ID Warehouse from
	 * 	@param trxName transaction, can be null
	 */
        public static boolean isWarehouseFrom(Properties ctx,int C_DocType_ID, int M_Warehouse_ID, String trxName){
            boolean hasPermission = true;
            String sql = "SELECT * FROM ZYN_MOVEMENT_WAREHOUSE WHERE C_DocType_ID=? and isActive = 'Y' ";
            PreparedStatement pstmt = null;
            try
            {
                    pstmt = DB.prepareStatement (sql, trxName);
                    pstmt.setInt (1, C_DocType_ID);
                    ResultSet rs = pstmt.executeQuery ();
                    while (rs.next ()) {
                        hasPermission = false;
                        MZYNMovementWarehouse movementWarehouse = new MZYNMovementWarehouse(ctx, rs, trxName);
                        if ( movementWarehouse.getM_WAREHOUSEFROM_ID() == M_Warehouse_ID){
                            hasPermission = true;
                            return hasPermission;
                        }
                    }
                    rs.close ();
                    pstmt.close ();
                    pstmt = null;
            }
            catch (Exception e)
            {
                    s_log.log(Level.SEVERE, sql, e);
            }
            try
            {
                    if (pstmt != null)
                            pstmt.close ();
                    pstmt = null;
            }
            catch (Exception e)
            {
                    pstmt = null;
            }
            return hasPermission;
        }
        
        /**
	 * 	Check if a movement has permission to move from a warehouse,
         *      if no one restriction exist, then has permisson
	 * 	@param ctx Current context
         *      @param C_DocType_ID DocType
         *      @param M_Warehouse_ID Warehouse from
	 * 	@param trxName transaction, can be null
	 */
        public static boolean isWarehouseTo(Properties ctx,int C_DocType_ID, int M_Warehouse_ID, String trxName){
            boolean hasPermission = true;
            String sql = "SELECT * FROM ZYN_MOVEMENT_WAREHOUSE WHERE C_DocType_ID=? and isActive = 'Y' ";
            PreparedStatement pstmt = null;
            try
            {
                    pstmt = DB.prepareStatement (sql, trxName);
                    pstmt.setInt (1, C_DocType_ID);
                    ResultSet rs = pstmt.executeQuery ();
                    while (rs.next ()) {
                        hasPermission = false;
                        MZYNMovementWarehouse movementWarehouse = new MZYNMovementWarehouse(ctx, rs, trxName);
                        if ( movementWarehouse.getM_WAREHOUSETO_ID() == M_Warehouse_ID){
                            hasPermission = true;
                            return hasPermission;
                        }
                    }
                    rs.close ();
                    pstmt.close ();
                    pstmt = null;
            }
            catch (Exception e)
            {
                    s_log.log(Level.SEVERE, sql, e);
            }
            try
            {
                    if (pstmt != null)
                            pstmt.close ();
                    pstmt = null;
            }
            catch (Exception e)
            {
                    pstmt = null;
            }
            return hasPermission;
        }
        
        
        /**
	 * 	Check if movement has warehouse from restriction
	 * 	@param ctx Current context
         *      @param C_DocType_ID DocType
	 * 	@param trxName transaction, can be null
	 */
        public static boolean hasWarehouseFromRestriction(Properties ctx,int C_DocType_ID, String trxName){
            boolean hasRestriction = false;
            String sql = "SELECT * FROM ZYN_MOVEMENT_WAREHOUSE WHERE C_DocType_ID=? and isActive = 'Y' ";
            PreparedStatement pstmt = null;

            try
            {
                    pstmt = DB.prepareStatement (sql, trxName);
                    pstmt.setInt (1, C_DocType_ID);
                    ResultSet rs = pstmt.executeQuery ();
                    while (rs.next ()) {
                        MZYNMovementWarehouse movementWarehouse = new MZYNMovementWarehouse(ctx, rs, trxName);
                        if ( movementWarehouse.getM_WAREHOUSEFROM_ID() != 0){
                            hasRestriction = true;
                            return hasRestriction;
                        }
                    }
                    rs.close ();
                    pstmt.close ();
                    pstmt = null;
            }
            catch (Exception e)
            {
                    s_log.log(Level.SEVERE, sql, e);
            }
            try
            {
                    if (pstmt != null)
                            pstmt.close ();
                    pstmt = null;
            }
            catch (Exception e)
            {
                    pstmt = null;
            }
            return hasRestriction;
        }
        
        /**
	 * 	Check if movement has warehouse from restriction
	 * 	@param ctx Current context
         *      @param C_DocType_ID DocType
	 * 	@param trxName transaction, can be null
	 */
        public static boolean hasWarehouseToRestriction(Properties ctx,int C_DocType_ID, String trxName){
            boolean hasRestriction = false;
            String sql = "SELECT * FROM ZYN_MOVEMENT_WAREHOUSE WHERE C_DocType_ID=? and isActive = 'Y' ";
            PreparedStatement pstmt = null;

            try
            {
                    pstmt = DB.prepareStatement (sql, trxName);
                    pstmt.setInt (1, C_DocType_ID);
                    ResultSet rs = pstmt.executeQuery ();
                    while (rs.next ()) {
                        MZYNMovementWarehouse movementWarehouse = new MZYNMovementWarehouse(ctx, rs, trxName);
                        if ( movementWarehouse.getM_WAREHOUSETO_ID() != 0){
                            hasRestriction = true;
                            return hasRestriction;
                        }
                    }
                    rs.close ();
                    pstmt.close ();
                    pstmt = null;
            }
            catch (Exception e)
            {
                    s_log.log(Level.SEVERE, sql, e);
            }
            try
            {
                    if (pstmt != null)
                            pstmt.close ();
                    pstmt = null;
            }
            catch (Exception e)
            {
                    pstmt = null;
            }
            return hasRestriction;
        }
 }		
