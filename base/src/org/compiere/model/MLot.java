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
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Product Lot
 *
 *	@author Jorg Janke
 *	@version $Id: MLot.java,v 1.10 2005/10/21 15:39:39 jjanke Exp $
 */
public class MLot extends X_M_Lot
{
	/**	Logger					*/
	private static CLogger		s_log = CLogger.getCLogger(MLot.class);

	/**
	 * 	Get Lots for Product
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@return Array of Lots for Product
	 */
	public static MLot[] getProductLots (Properties ctx, int M_Product_ID, String trxName)
	{
		String sql = "SELECT * FROM M_Lot WHERE M_Product_ID=?";
		ArrayList<MLot> list = new ArrayList<MLot>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MLot (ctx, rs, trxName));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		MLot[] retValue = new MLot[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getProductLots

	/**
	 * 	Get Lot for Product
	 *	@param ctx context
	 *	@param M_Product_ID product
	 *	@param lot
	 *	@return Array of Lots for Product
	 */
	public static MLot getProductLot (Properties ctx, int M_Product_ID, String lot, String trxName)
	{
		String sql = "SELECT * FROM M_Lot WHERE M_Product_ID=? AND Name=?";
		MLot retValue = null;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_ID);
			pstmt.setString(2, lot);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				retValue = new MLot (ctx, rs, trxName);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		return retValue;
	}	//	getProductLot

	/**
	 * 	Get Lot Key Name Pairs for Product
	 *	@param M_Product_ID product
	 *	@return Array of Lot Key Name Pairs for Product
	 */
	public static KeyNamePair[] getProductLotPairs (int M_Product_ID, String trxName)
	{
		String sql = "SELECT M_Lot_ID, Name FROM M_Lot WHERE M_Product_ID=?";
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Product_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new KeyNamePair (rs.getInt(1), rs.getString(2)));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		KeyNamePair[] retValue = new KeyNamePair[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getProductLotPairs

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param M_Lot_ID ID
	 */
	public MLot (Properties ctx, int M_Lot_ID, String trxName)
	{
		super (ctx, M_Lot_ID, trxName);
		/** if (M_Lot_ID == 0)
		{
			setM_Lot_ID (0);
			setM_Product_ID (0);
			setName (null);
		}
		**/
	}	//	MLot

	/**
	 * 	Load Constructor
	 * 	@param ctx context
	 * 	@param rs result set
	 */
	public MLot (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MLot

	/**
	 * 	Parent Constructor
	 * 	@param ctl lot control
	 * 	@param M_Product_ID product
	 * 	@param Name name
	 */
	public MLot (MLotCtl ctl, int M_Product_ID, String Name)
	{
		this (ctl.getCtx(), 0, ctl.get_TrxName());
		setClientOrg(ctl);
		setM_LotCtl_ID(ctl.getM_LotCtl_ID());
		setM_Product_ID (M_Product_ID);
		setName (Name);
	}	//	MLot

	/**
	 *	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		return getName();
	}	//	toString
        
        /** BISion - 14/10/2008 - Santiago Ibañez
         * Este método retorna si el lote ha sido procesado (es temporal) o no.
         * Se utilza en el contexto de borrado de los mismos.
         * @return
         */
        public boolean isProcesado(){
            Object oo = get_Value("ISPROCESADO");
            if (oo != null) 
            {
             if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
             return "Y".equals(oo);
            }
            return false;
        }
        
        /** BISion - 14/10/2008 - Santiago Ibañez
         * Método que establece un lote como procesado o no. Un lote se
         * establecerá como procesado al momento de completarse el procedimiento
         * Panalab que lo originó.
         * @param value
         */
        public void setIsProcesado(boolean value){
            set_Value("ISPROCESADO",value);
        }


        /**
	 *	@return duplicated lot
	 * 	Create a Duplicated Lot
	 */
	public MLot duplicateLot ()
	{
            MLot duplicatedLot = null;
            if (getM_Lot_ID() != 0)
            {
                duplicatedLot = new MLot(getCtx(), 0, get_TrxName());
                duplicatedLot.setM_LotCtl_ID(getM_LotCtl_ID());
                duplicatedLot.setM_Product_ID(getM_Product_ID());
                duplicatedLot.setName(getName());
                duplicatedLot.setDateFrom(getDateFrom());
                duplicatedLot.setDateTo(getDateTo());
                if (!duplicatedLot.save())
                    return null;
            }
            return duplicatedLot;
	}	//	duplicateLot

        /**
	 *	@return sub lot
	 * 	Create a sub Lot
	 */
	public MLot createSubLot ()
	{
            MLot subLot = null;
            if (getM_Lot_ID() != 0 && !isSubLot())
            {
                subLot = duplicateLot();
                if (!subLot.save())
                    return null;
             
                //Creo Vinculo entre Lote y SubLote
                MLotSubLot subLotRel = new MLotSubLot(getCtx(), 0, get_TrxName());
                subLotRel.setM_Lot_ID(getM_Lot_ID());
                subLotRel.setM_SUBLOT_ID(subLot.getM_Lot_ID());
                if (!subLotRel.save())
                    return null;

                subLot.setName(subLot.getName()+subLotRel.getSEQUENCE());
                if (!subLot.save())
                    return null;
            }
            return subLot;
	}	//	duplicateLot

        public boolean isSubLot(){
            MLot[] parentLots = MLotSubLot.getParentLots(getCtx(), getM_Lot_ID(), get_TrxName());
            if (parentLots.length > 0)
                return true;
            else
                return false;
        }
   
        public MLot[] getSubLots(){
            return MLotSubLot.getSubLots(getCtx(), getM_Lot_ID(), get_TrxName());
        }

}	//	MLot
