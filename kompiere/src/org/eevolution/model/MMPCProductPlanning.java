/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/
package org.eevolution.model;
//package compiere.model;


import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;

import org.compiere.util.*;
import org.compiere.swing.*;
import org.compiere.apps.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.apps.form.*;
import compiere.model.*;
import org.compiere.apps.search.*;
import org.compiere.grid.*;
import org.compiere.grid.ed.*;
import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.swing.*;
import java.awt.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.apps.*;
import org.compiere.minigrid.*;
import org.compiere.swing.*;
import org.compiere.util.*;
/**
 *	Product Data Planning
 *
 *  @author Jorg Janke
 *  @version $Id: MCProductPlannning.java,v 1.4 2004/05/13 06:05:22 jjanke Exp $
 */
public class MMPCProductPlanning extends X_MPC_Product_Planning
{
	/**
	 * 	Get from Cache
	 *	@param ctx context
	 *	@param M_Product_Costing_ID id
	 *	@return
	 */
        /*
	public static MMPCProductCosting get (Properties ctx, int MPC_Product_Costing_ID)
	{
		Integer ii = new Integer (MPC_Product_Costing_ID);
		MMPCProductCosting pc = (MMPCProductCosting)s_cache.get(ii);
		if (pc == null)
			pc = new MMPCProductCosting (ctx, MPC_Product_Costing_ID);
		return pc;
	}	//	get
	*/

	/**	Cache						*/
	//private static CCache	s_cache = new CCache ("M_Product_Costing", 20);

        /** Log									*/

        private static CLogger log = CLogger.getCLogger(MMPCProductPlanning.class);


	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_Product_Costing_ID id
	 */
	public MMPCProductPlanning(Properties ctx, int MPC_Product_Planning_ID,String trxName)
	{
		super(ctx, MPC_Product_Planning_ID,trxName);
		if (MPC_Product_Planning_ID == 0)
		{
                 setIsDemand(false);
                 setIsSupply(false);

                /*
		setC_AcctSchema_ID(0);
                setCostCumAmt();
                setCostCumQty();
                setCostLLAmt();
                setCostTLAmt();
                setM_Product_ID();
                setM_Warehouse_ID();
                setMPC_Cost_Element_ID();
                stS_Resource_ID();*/

		}
	}	//	MPCCostElement

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MMPCProductPlanning(Properties ctx, ResultSet rs,String trxName)
	{
		super(ctx, rs,trxName);
	}

        public static MMPCProductPlanning get(Properties ctx, int AD_Org_ID , int M_Product_ID, int M_Warehouse_ID, int S_Resource_ID)
	{
		//int AD_Org_ID = Env.getContextAsInt(ctx, "AD_Org_ID");
                System.out.println("Ad_Org_ID" + AD_Org_ID + "M_Product_ID" + M_Product_ID + "M_Warehouse_ID" + M_Warehouse_ID + "S_Resource_ID" + S_Resource_ID );
                String sql = "SELECT * FROM MPC_Product_Planning  pp WHERE pp.AD_Org_ID = ? AND pp.M_Product_ID = ? AND pp.M_Warehouse_ID = ? AND pp.S_Resource_ID = ? ";

                PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, AD_Org_ID);
			pstmt.setInt(2, M_Product_ID);
			pstmt.setInt(3, M_Warehouse_ID);
                        pstmt.setInt(4, S_Resource_ID);
			ResultSet rs = pstmt.executeQuery();

            /** BISion 07/05/2009 - Santiago Ibañez
             * Modificacion realizada para cerrar adecuadamente los rs y ps
             */
            while (rs.next()){
				int MPC_Product_Planning_ID = rs.getInt("MPC_Product_Planning_ID");
                rs.close();
                pstmt.close();
                return new MMPCProductPlanning(ctx, MPC_Product_Planning_ID,null);
            }
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"getProductPlanning", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return null;
	}


        /**
         * Get the MMPCProductPlanning, Bision-Nadia 27/06/2008
         * @param ctx
         * @param AD_Org_ID
         * @param M_Product_ID
         * @param M_Warehouse_ID
         * @return
         */


        public static MMPCProductPlanning get(Properties ctx, int AD_Org_ID , int M_Product_ID,int M_Warehouse_ID)
	{
		//int AD_Org_ID = Env.getContextAsInt(ctx, "AD_Org_ID");
                //System.out.println("Ad_Org_ID" + AD_Org_ID + "M_Product_ID" + M_Product_ID + "M_Warehouse_ID" + M_Warehouse_ID + "S_Resource_ID" + S_Resource_ID );
                //MOrgInfo.get(Env.getCtx(), AD_Org_ID);
                String sql = "SELECT * FROM MPC_Product_Planning  pp WHERE pp.AD_Org_ID = ? AND pp.M_Product_ID = ? ";
                if  (M_Warehouse_ID!= -1)
                    sql= sql + "AND pp.M_Warehouse_ID = ? ";

                PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, AD_Org_ID);
			pstmt.setInt(2, M_Product_ID);
                        if  (M_Warehouse_ID != -1)
                            pstmt.setInt(3, M_Warehouse_ID );//MOrgInfo.get(Env.getCtx(), AD_Org_ID).getM_Warehouse_ID()); Bision 19/06/2008
                        //pstmt.setInt(4, S_Resource_ID);
			ResultSet rs = pstmt.executeQuery();
			/** BISion 07/05/2009 - Santiago Iba�ez
             * Modificacion realizada para cerrar adecuadamente los rs y ps
             */
            while (rs.next()){
				int MPC_Product_Planning_ID = rs.getInt("MPC_Product_Planning_ID");
                rs.close();
                pstmt.close();
                return new MMPCProductPlanning(ctx, MPC_Product_Planning_ID,null);
            }
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"getProductPlanning", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return null;
	}


        public static MMPCProductPlanning get(Properties ctx, int AD_Org_ID , int M_Product_ID)
	{
		//int AD_Org_ID = Env.getContextAsInt(ctx, "AD_Org_ID");
                //System.out.println("Ad_Org_ID" + AD_Org_ID + "M_Product_ID" + M_Product_ID + "M_Warehouse_ID" + M_Warehouse_ID + "S_Resource_ID" + S_Resource_ID );
                //MOrgInfo.get(Env.getCtx(), AD_Org_ID);
                String sql = "SELECT * FROM MPC_Product_Planning  pp WHERE pp.AD_Org_ID = ? AND pp.M_Product_ID = ? AND pp.isactive='Y' ";


                PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, AD_Org_ID);
			pstmt.setInt(2, M_Product_ID);

                        //Spstmt.setInt(3, MOrgInfo.get(Env.getCtx(), AD_Org_ID).getM_Warehouse_ID());
                        //pstmt.setInt(4, S_Resource_ID);
			ResultSet rs = pstmt.executeQuery();
            /** BISion 07/05/2009 - Santiago Ibañez
             * Modificacion realizada para cerrar adecuadamente los rs y ps
             */
			while (rs.next()){
                int MPC_Product_Planning_ID = rs.getInt("MPC_Product_Planning_ID");
                rs.close();
                pstmt.close();
				return new MMPCProductPlanning(ctx, MPC_Product_Planning_ID,null);
            }
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"getProductPlanning", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return null;
	}
        public static MMPCProductPlanning getDemandWarehouse(Properties ctx , int AD_Org_ID , int M_Product_ID, int M_Warehouse_ID, String trxName)
	{
		//int AD_Org_ID = Env.getContextAsInt(ctx, "AD_Org_ID");
                System.out.println("***** Ad_Org_ID" + AD_Org_ID + "M_Product_ID" + M_Product_ID + "M_Warehouse_ID" + M_Warehouse_ID );
                String sql = "SELECT * FROM MPC_Product_Planning  pp WHERE pp.AD_Org_ID = " +AD_Org_ID +" AND pp.M_Product_ID =" +M_Product_ID +" AND pp.M_Warehouse_ID = " +M_Warehouse_ID +" AND pp.IsDemand =  'Y' AND pp.IsActive = 'Y'";

                System.out.println("***** SQL " +sql);

                PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql,trxName);
		//	pstmt.setInt(1, AD_Org_ID);
		//	pstmt.setInt(2, M_Product_ID);
		//	pstmt.setInt(3, M_Warehouse_ID);
                        //pstmt.setInt(4, S_Resource_ID);
			ResultSet rs = pstmt.executeQuery();

            /** BISion - 29/04/2009 - Santiago Ibañez
             * Modificacion realizada porque no se cerraban los resultSet ni
             * los prepared Statement y arrojaba la excepcion de maximum open cursors exceeded
             */

            while (rs.next()){
				int MPC_Product_Planning_ID = rs.getInt("MPC_Product_Planning_ID");
                rs.close();
                pstmt.close();
                pstmt = null;
                return new MMPCProductPlanning(ctx, MPC_Product_Planning_ID,trxName);
            }
            //fin modificacion BISion
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"getProductPlanning", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return null;
	}

         public static MMPCProductPlanning getDemandSupplyResource(Properties ctx , int  AD_Org_ID , int M_Product_ID, int S_Resource_ID)
	{
		//int AD_Org_ID = Env.getContextAsInt(ctx, "AD_Org_ID");
                System.out.println("Ad_Org_ID" + AD_Org_ID + "M_Product_ID" + M_Product_ID + "S_Resource_ID" + S_Resource_ID );
                String sql = "SELECT * FROM MPC_Product_Planning  pp WHERE pp.AD_Org_ID = ? AND pp.M_Product_ID = ? AND pp.S_Resource_ID = ? AND  pp.IsSupply =  'Y' AND pp.IsDemand = 'Y' AND pp.IsActive = 'Y'";

                PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, AD_Org_ID);
			pstmt.setInt(2, M_Product_ID);
			pstmt.setInt(3, S_Resource_ID);
                        //pstmt.setInt(4, S_Resource_ID);
			ResultSet rs = pstmt.executeQuery();
			/** BISion 07/05/2009 - Santiago Ibañez
             * Modificacion realizada para cerrar adecuadamente los rs y ps
             */
            while (rs.next()){
				int MPC_Product_Planning_ID = rs.getInt("MPC_Product_Planning_ID");
                rs.close();
                pstmt.close();
                pstmt = null;
                return new MMPCProductPlanning(ctx, MPC_Product_Planning_ID,null);
            }
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"getProductPlanning", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return null;
	}

          public static MMPCProductPlanning getDemandSupplyResource(Properties ctx , int  AD_Org_ID , int M_Product_ID)
	{
		//int AD_Org_ID = Env.getContextAsInt(ctx, "AD_Org_ID");
                System.out.println("Ad_Org_ID" + AD_Org_ID + "M_Product_ID" + M_Product_ID);
                String sql = "SELECT * FROM MPC_Product_Planning  pp WHERE pp.AD_Org_ID = ? AND pp.M_Product_ID = ? AND  pp.IsSupply =  'Y' AND pp.IsDemand = 'Y'";

                PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, AD_Org_ID);
			pstmt.setInt(2, M_Product_ID);
                        //pstmt.setInt(4, S_Resource_ID);
			ResultSet rs = pstmt.executeQuery();
			/** BISion 07/05/2009 - Santiago Ibañez
             * Modificacion realizada para cerrar adecuadamente los rs y ps
             */
            while (rs.next()){
				int MPC_Product_Planning_ID = rs.getInt("MPC_Product_Planning_ID");
                rs.close();
                pstmt.close();
                pstmt = null;
                return new MMPCProductPlanning(ctx, MPC_Product_Planning_ID,null);
            }
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"getProductPlanning", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return null;
	}



}	//	Product Data Planning

