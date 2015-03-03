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
import org.compiere.util.*;

/**
*	Bank Statement Model
*
*	@author Eldir Tomassen/Jorg Janke
*	@version $Id: MBankStatement.java,v 1.20 2005/11/12 22:58:56 jjanke Exp $
*/
@SuppressWarnings("serial")
public class MLotSubLot extends X_M_LOT_SUBLOT
{


        /**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MLotSubLot.class);

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BankStatement_ID id
	 */
	public MLotSubLot (Properties ctx, int M_Lot_SubLot_ID, String trxName)
	{
		super (ctx, M_Lot_SubLot_ID, trxName);
		if (M_Lot_SubLot_ID == 0)
		{
		//	setC_BankAccount_ID (0);	//	parent

		}
	}	//

        public boolean beforeSave(boolean newRecord){
                if (newRecord) {
                    if (getSEQUENCE() == null)
                        setSEQUENCE( getNextSequence() );
                    else
                        if( !getSEQUENCE().equals(getNextSequence() ))
                            setSEQUENCE( getNextSequence() );
                }
            return true;
        }

	/**
	 * 	Load Constructor
	 * 	@param ctx Current context
	 * 	@param rs result set
	 */
	public MLotSubLot(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//

        /**
	 * 	Get MLot sublots
	 *	@param ctx context
	 *	@param M_Lot_ID lot
         *      @param traxName transaction Name
	 *	@return array of matches
	 */
	public static MLot[] getSubLots (Properties ctx,
		int M_Lot_ID, String trxName)
	{
		if (M_Lot_ID == 0)
			return new MLot[]{};
		//
		String sql = "SELECT M_SUBLOT_ID FROM M_LOT_SUBLOT WHERE M_LOT_ID=?";
		ArrayList<MLot> list = new ArrayList<MLot>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Lot_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MLot(ctx, rs.getInt(1), trxName));
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
		MLot[] retValue = new MLot[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	getSubLots

        /**
	 * 	Get MLot parent lots
	 *	@param ctx context
	 *	@param M_Lot_ID lot
         *      @param traxName transaction Name
	 *	@return array of matches (Must be one)
	 */
	public static MLot[] getParentLots (Properties ctx,
		int M_Lot_ID, String trxName)
	{
		if (M_Lot_ID == 0)
			return new MLot[]{};
		//
		String sql = "SELECT M_LOT_ID FROM M_LOT_SUBLOT WHERE M_SUBLOT_ID=?";
		ArrayList<MLot> list = new ArrayList<MLot>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Lot_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MLot(ctx, rs.getInt(1), trxName));
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
		MLot[] retValue = new MLot[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	getParentLots

	
        /**
	 * 	Get Next Sublot Sequence for MLot
	 *	@param ctx context
	 *	@param M_Lot_ID order
         *      @param traxName transaction Name
	 *	@return array of matches
	 */
	public String getNextSequence ()
	{
            String returnValue = "A";
            String sql = "SELECT MAX(SEQUENCE) FROM M_LOT_SUBLOT WHERE M_LOT_ID=?";
            ArrayList<MLot> list = new ArrayList<MLot>();
            PreparedStatement pstmt = null;
            try
            {
                    pstmt = DB.prepareStatement (sql, get_TrxName());
                    pstmt.setInt (1, getM_Lot_ID());
                    ResultSet rs = pstmt.executeQuery ();
                    String result;
                    if (rs.next()){
                        result = rs.getString(1);
                        if ( result != null ){
                            char sequence = result.charAt(0);
                            sequence++;
                            returnValue = String.valueOf(sequence);
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
	return returnValue;
	}	//	getNextSequence


 }		
