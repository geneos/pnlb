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
import org.compiere.model.X_ZYN_CATEGORY_TOLERANCE;

import org.compiere.util.*;

/**
*	Bank Statement Model
*
*	@author Eldir Tomassen/Jorg Janke
*	@version $Id: MBankStatement.java,v 1.20 2005/11/12 22:58:56 jjanke Exp $
*/

public class MZYNCATEGORYTOLERANCE extends X_ZYN_CATEGORY_TOLERANCE
{


        /**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MZYNCATEGORYTOLERANCE.class);

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param MPC_OrderParentOrder_ID id
	 */
	public MZYNCATEGORYTOLERANCE (Properties ctx, int MPC_OrderParentOrder_ID, String trxName)
	{
		super (ctx, MPC_OrderParentOrder_ID, trxName);
		if (MPC_OrderParentOrder_ID == 0)
		{
		//	setC_BankAccount_ID (0);	//	parent

		}
	}	//	MMPCOrderParentOrder

	/**
	 * 	Load Constructor
	 * 	@param ctx Current context
	 * 	@param rs result set
	 */
	public MZYNCATEGORYTOLERANCE(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//

        public boolean beforeSave(boolean newRecord){
            
            /*
             * Chequeo que no existan 2 tolerancias para la misma categoria
             */
            String sql = "SELECT count(*) FROM ZYN_CATEGORY_TOLERANCE WHERE m_product_category_id=? and zyn_category_tolerance_id <> ? ";
            PreparedStatement pstmt = null;
            MZYNCATEGORYTOLERANCE returnValue = null;
            int count = 0;
            try
            {
                    pstmt = DB.prepareStatement (sql, get_TrxName());
                    pstmt.setInt (1, getM_Product_Category_ID());
                    pstmt.setInt (2, getZYN_CATEGORY_TOLERANCE_ID());
                    ResultSet rs = pstmt.executeQuery ();
                    if (rs.next ())
                            count = rs.getInt(1);
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
            if (count >= 1){
                JOptionPane.showMessageDialog(null,"Ya existe una tolerancia cargada para la categoria seleccionada", "Categoria duplicada", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }
        
        public static MZYNCATEGORYTOLERANCE getZYNCategoryTolerance( Properties ctx, int m_product_category_id, String trxName ){
            String sql = "SELECT ZYN_CATEGORY_TOLERANCE_ID FROM ZYN_CATEGORY_TOLERANCE WHERE m_product_category_id=? and isActive = 'Y' ";
            PreparedStatement pstmt = null;
            MZYNCATEGORYTOLERANCE returnValue = null;
            try
            {
                    pstmt = DB.prepareStatement (sql, trxName);
                    pstmt.setInt (1, m_product_category_id);
                    ResultSet rs = pstmt.executeQuery ();
                    if (rs.next ())
                            returnValue = new MZYNCATEGORYTOLERANCE(ctx, rs.getInt(1), trxName);
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
            return returnValue;
        }
 }		
