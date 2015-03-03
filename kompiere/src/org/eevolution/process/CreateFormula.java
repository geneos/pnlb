/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is             Compiere  ERP & CRM Smart Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/
package org.eevolution.process;

import java.util.logging.*;
import java.math.*;
import java.sql.*;
import java.util.*;


import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
import compiere.model.*;

import org.eevolution.form.*;

import org.compiere.grid.ed.*;
import org.eevolution.model.MMPCProductBOM;
import org.eevolution.model.MMPCProductBOMLine;
import org.eevolution.model.MMPCProductPlanning;
import org.eevolution.model.MMPCProfileBOM;
import org.eevolution.model.MMPCProfileBOMCost;
import org.eevolution.model.MMPCProfileBOMSelected;
import org.compiere.util.QueryDB;

/**
 *  Copy Order Lines
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromOrder.java,v 1.4 2004/05/07 05:52:14 jjanke Exp $
 */
public class CreateFormula extends SvrProcess
{
	/**	The Order				*/
	private int		p_MPC_ProfileBOM_ID = 0;
        private int		m_MPC_ProfileBOM_ID = 0;
        private int m_WindowNo =0;
        private int m_instance=0;
        private boolean bandera=false;
        private String salvado1="";
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
//                      else if (name.equals("MPC_ProfileBOM_ID"))
//				p_MPC_ProfileBOM_ID = ((BigDecimal)para[i].getParameter()).intValue();
                        
			else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{       int m_MPC_ProfileBOM_ID = getRecord_ID();
                int bomp=0;
		 int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
//           int m_MPC_ProfileBOM_ID= Integer.parseInt(Env.getContext(Env.getCtx(),"#MPC_ProfileBOM_ID"));
//           System.out.println("profilebom " +m_MPC_ProfileBOM_ID);
                 MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
                
                  int m_MPC_ProfileBOMCost_ID=0;
            try{
               StringBuffer sql1 = new StringBuffer("SELECT MPC_ProfileBOMCost_ID FROM MPC_ProfileBOMCost WHERE IsActive='Y' AND AD_Client_ID=? and MPC_ProfileBOM_ID=?");
    		PreparedStatement pstmt = DB.prepareStatement(sql1.toString());
                        pstmt.setInt(1,AD_Client_ID);
			pstmt.setInt(2, m_MPC_ProfileBOM_ID);
                        
			ResultSet rs = pstmt.executeQuery();
                         
			//
			while (rs.next())
			{
				m_MPC_ProfileBOMCost_ID = rs.getInt(1);
				
			}
			rs.close();
			pstmt.close();
	           
           }
           catch(SQLException s)
           {
           }
//            System.out.println("profilecost --   "+m_MPC_ProfileBOMCost_ID);
           MMPCProfileBOMCost bolsa = new MMPCProfileBOMCost(Env.getCtx(),m_MPC_ProfileBOMCost_ID,null);
           if (bolsa.getM_ProductE_ID()!=0)
           {
                 QueryDB query2 = new QueryDB("org.compiere.model.X_MPC_Product_BOM");
                String filter2 = "Value = '" + profilebom.getValue()+"'";
                List results2 = query2.execute(filter2);
                Iterator select2 = results2.iterator();
                while (select2.hasNext())
                { 
                   
                   X_MPC_Product_BOM xpbom = (X_MPC_Product_BOM) select2.next();
                   //list.add(new MMPCProfileBOMLine(getCtx(), bomline.getMPC_Order_BOM_ID()));
                   //list.add(bomline);
                   MMPCProductBOM  prodbom = new  MMPCProductBOM(Env.getCtx(), xpbom.getMPC_Product_BOM_ID(),null);
                   bomp = prodbom.getMPC_Product_BOM_ID();
                   bandera = true;
                }
                   
                 
                 
        
          // MMPCProfileBOMSelected profilebomsel = new MMPCProfileBOMSelected(profilebom);
        if (!profilebom.isPublished() && !bandera)
        {
           MMPCProductBOM prodbom = new MMPCProductBOM(Env.getCtx(),0,null);    
           MProduct mprod = new MProduct(Env.getCtx(),profilebom.getM_Product_ID(),null);
           prodbom.setValue(profilebom.getValue());
           prodbom.setName(profilebom.getName());
           String desc ="";
           if (profilebom.getOldValue()!=null && profilebom.getDescription()!=null)
                desc = profilebom.getOldValue() +"---"+profilebom.getDescription();
           else if (profilebom.getOldValue()!=null)
                desc = profilebom.getOldValue();
           else if (profilebom.getDescription()!=null)
                desc = profilebom.getDescription();
          
           prodbom.setDescription(desc);
        //   prodbom.setM_Product_ID(profilebom.getName());
           
           prodbom.setC_UOM_ID(profilebom.getC_UOM_ID());
           prodbom.setM_Product_ID(profilebom.getM_Product_ID());
           java.util.Date today =new java.util.Date();
java.sql.Timestamp now =new java.sql.Timestamp(today.getTime());
            
               prodbom.setValidFrom(profilebom.getValidFrom());
                 if(prodbom.save())
       {
              profilebom.setValidFrom(now);
               profilebom.setIsPublished(true);
                profilebom.setMPC_Product_BOM_ID(prodbom.getMPC_Product_BOM_ID());
               profilebom.save();
           try
           {
               StringBuffer sqlpp = new StringBuffer("SELECT MPC_Product_Plannning_ID FROM MPC_Product_Planning WHERE IsActive='Y' AND AD_Client_ID=? and M_Product_ID=?");
    		PreparedStatement pstmtpp = DB.prepareStatement(sqlpp.toString());
                        pstmtpp.setInt(1,AD_Client_ID);
			pstmtpp.setInt(2, profilebom.getM_Product_ID());
                         System.out.println("query del plannning " +sqlpp.toString());
			ResultSet rspp = pstmtpp.executeQuery();
                         
			//
			while (rspp.next())
			{
                            System.out.println("Planning " +rspp.getInt(1) + " ProductBom " +prodbom.getMPC_Product_BOM_ID());
				MMPCProductPlanning mpplanning = new MMPCProductPlanning(Env.getCtx(),rspp.getInt(1),null);
                                mpplanning.setMPC_Product_BOM_ID(prodbom.getMPC_Product_BOM_ID());
                                mpplanning.save();
				
			}
			rspp.close();
			pstmtpp.close();
           }
           catch (SQLException s)
           {
           }
           mprod.setIsBOM(true);
           mprod.save();
           
            salvado1 = new String("Formula creada con exito "+prodbom.getValue() +" como el Documento no. "+prodbom.getDocumentNo());
           
                QueryDB query = new QueryDB("org.compiere.model.X_MPC_ProfileBOM");
                String filter = "MPC_ProfileBOM_ID = " + m_MPC_ProfileBOM_ID;
                List results = query.execute(filter);
                Iterator select = results.iterator();
                while (select.hasNext())
                { 
                   
                   X_MPC_ProfileBOM xbom = (X_MPC_ProfileBOM) select.next();
                   //list.add(new MMPCProfileBOMLine(getCtx(), bomline.getMPC_Order_BOM_ID()));
                   //list.add(bomline);
                   MMPCProfileBOM  bom = new  MMPCProfileBOM(Env.getCtx(), xbom.getMPC_ProfileBOM_ID(),null);
                   
                   MMPCProfileBOMSelected[] pbomsel = bom.getSel();  
                   
                   for (int i=0; i<pbomsel.length; i++)
                   {
//                       MMPCProfileBOMProduct bomprod = new MMPCProfileBOMProduct(Env.getCtx(),0);
//                    
//                       bomprod.setMPC_ProfileBOM_ID(pbomsel[i].getMPC_ProfileBOM_ID());
//                       bomprod.setAD_Org_ID(pbomsel[i].getAD_Org_ID());
//                       bomprod.setM_Product_ID(pbomsel[i].getM_Product_ID());
//                 //      calculado(pbomsel[i].getM_Product_ID(),pbomsel[i].getQty().toString());
//                       bomprod.setMinimum(pbomsel[i].getQty());
//                       bomprod.save();
                        MMPCProductBOMLine prodbomline = new MMPCProductBOMLine(prodbom);
                        MProduct producto = new MProduct(Env.getCtx(),pbomsel[i].getM_Product_ID(),null);
           prodbomline.setM_Product_ID(pbomsel[i].getM_Product_ID());
           if (producto.getDescriptionURL()!=null)
                prodbomline.setDescription(producto.getDescriptionURL());
           prodbomline.setQtyBatch(pbomsel[i].getPercent());
           prodbomline.setValidFrom(prodbom.getValidFrom());
           prodbomline.setValidTo(prodbom.getValidTo());
           prodbomline.setLine(i*10);
           prodbomline.setC_UOM_ID(profilebom.getC_UOM_ID());
           prodbomline.setIsQtyPercentage(true);
           prodbomline.setIsCritical(true);
           prodbomline.setIssueMethod(prodbomline.ISSUEMETHOD_BackFlush);
           prodbomline.save();
                   }
                 //  atributos(xbom.getMPC_ProfileBOM_ID());
                  
                }
           BigDecimal one =Env.ONE;
            MMPCProductBOMLine prodbomlineb = new MMPCProductBOMLine(prodbom);
           prodbomlineb.setM_Product_ID(bolsa.getM_ProductE_ID());
          
           prodbomlineb.setQtyBatch(bolsa.getQtyE());
           prodbomlineb.setQtyBOM(Env.ZERO);
           prodbomlineb.setComponentType(prodbomlineb.COMPONENTTYPE_Packing);
           prodbomlineb.setValidFrom(prodbom.getValidFrom());
           prodbomlineb.setValidTo(prodbom.getValidTo());
           prodbomlineb.setLine(5);
           prodbomlineb.setC_UOM_ID(100);
           prodbomlineb.setIsQtyPercentage(true);
           prodbomlineb.setIssueMethod(prodbomlineb.ISSUEMETHOD_BackFlush);
           prodbomlineb.save();
           
           MMPCProductBOMLine prodbomlineet = new MMPCProductBOMLine(prodbom);
           prodbomlineet.setM_Product_ID(1002823);
          
           prodbomlineet.setQtyBatch(bolsa.getQtyE());
           prodbomlineet.setQtyBOM(Env.ZERO);
           prodbomlineet.setComponentType(prodbomlineb.COMPONENTTYPE_Packing);
           prodbomlineet.setValidFrom(prodbom.getValidFrom());
           prodbomlineet.setValidTo(prodbom.getValidTo());
           prodbomlineet.setLine(7);
           prodbomlineet.setC_UOM_ID(100);
           prodbomlineet.setIsQtyPercentage(true);
           prodbomlineet.setIssueMethod(prodbomlineb.ISSUEMETHOD_BackFlush);
           prodbomlineet.save();
         //  ADialog.info(m_WindowNo,this,salvado1);
       }
       else
       {
           salvado1 = "No se pudo generar la Formula";
           //ADialog.error(m_WindowNo,this,"No se pudo generar la F�rmula");
       }
        //   prodbom.save();
        }
        else if (bandera && profilebom.isPublished())
        {
                System.out.println("formula bom seleccionada" +bomp);
                MMPCProductBOM prodbomp = new MMPCProductBOM(Env.getCtx(),bomp,null);
                 MProduct mprod = new MProduct(Env.getCtx(),prodbomp.getM_Product_ID(),null);
                java.util.Date today =new java.util.Date();
                java.sql.Timestamp now =new java.sql.Timestamp(today.getTime());
                prodbomp.setValidTo(now);
                profilebom.setValidFrom(now);
                prodbomp.setIsActive(false);
                if (prodbomp.save())
                {
                      try
                        {
                           StringBuffer sql2 = new StringBuffer("SELECT MPC_Product_Plannning_ID FROM MPC_Product_Planning WHERE IsActive='Y' AND AD_Client_ID=? and M_Product_ID=?");
                            PreparedStatement pstmt2 = DB.prepareStatement(sql2.toString());
                                    pstmt2.setInt(1,AD_Client_ID);
                                    pstmt2.setInt(2, profilebom.getM_Product_ID());
                                    System.out.println("query del plannning " +sql2.toString());
                                    ResultSet rs2 = pstmt2.executeQuery();

                                    //
                                    while (rs2.next())
                                    {
                                        System.out.println("Planning " +rs2.getInt(1) + " ProductBom " +prodbomp.getMPC_Product_BOM_ID());
                                            MMPCProductPlanning mpplanning = new MMPCProductPlanning(Env.getCtx(),rs2.getInt(1),null);
                                            mpplanning.setMPC_Product_BOM_ID(prodbomp.getMPC_Product_BOM_ID());
                                            mpplanning.save();

                                    }
                                    rs2.close();
                                    pstmt2.close();
                       }
                       catch (SQLException s)
                       {
                       }
                      profilebom.setIsPublished(false);
                       profilebom.setMPC_Product_BOM_ID(prodbomp.getMPC_Product_BOM_ID());
                      profilebom.save();
                }
                
                mprod.setIsBOM(false);
                mprod.save();
        }
              else if (bandera && !profilebom.isPublished())
              {
                  
                  MMPCProductBOM prodbomp = new MMPCProductBOM(Env.getCtx(),bomp,null);
                  MProduct mprod = new MProduct(Env.getCtx(),prodbomp.getM_Product_ID(),null);
                         StringBuffer borrars = new StringBuffer("Delete from MPC_Product_BOMLine WHERE IsActive='Y' AND MPC_Product_BOM_ID=" + bomp);
                         DB.executeUpdate(borrars.toString());
                         
                        java.util.Date today =new java.util.Date();
                        java.sql.Timestamp now =new java.sql.Timestamp(today.getTime());
                        String desc="";
                         if (profilebom.getOldValue()!=null && profilebom.getDescription()!=null)
                             desc = profilebom.getOldValue() +"---"+profilebom.getDescription();
                         else if (profilebom.getOldValue()!=null)
                              desc = profilebom.getOldValue();
                         else if (profilebom.getDescription()!=null)
                                desc = profilebom.getDescription();
          
                        prodbomp.setDescription(desc);
                        prodbomp.setValidTo(profilebom.getValidTo());
                        prodbomp.setValidFrom(now);
                        prodbomp.setIsActive(true);
                        prodbomp.save();
                        mprod.setIsBOM(true);
                        mprod.save();
                        // borrar y volver a poner datos al reliberar
                        QueryDB query = new QueryDB("org.compiere.model.X_MPC_ProfileBOM");
                        String filter = "MPC_ProfileBOM_ID = " + m_MPC_ProfileBOM_ID;
                        List results = query.execute(filter);
                        Iterator select = results.iterator();
                        while (select.hasNext())
                        { 
                   
                        X_MPC_ProfileBOM xbom = (X_MPC_ProfileBOM) select.next();                  
                        MMPCProfileBOM  bom = new  MMPCProfileBOM(Env.getCtx(), xbom.getMPC_ProfileBOM_ID(),null);                  
                        MMPCProfileBOMSelected[] pbomsel = bom.getSel();                      
                            for (int i=0; i<pbomsel.length; i++)
                            {
                                MMPCProductBOMLine prodbomline = new MMPCProductBOMLine(prodbomp);
                                MProduct producto = new MProduct(Env.getCtx(),pbomsel[i].getM_Product_ID(),null);
                                prodbomline.setM_Product_ID(pbomsel[i].getM_Product_ID());
                                if (producto.getDescriptionURL()!=null)
                                    prodbomline.setDescription(producto.getDescriptionURL());
                                prodbomline.setQtyBatch(pbomsel[i].getPercent());
                                prodbomline.setValidFrom(prodbomp.getValidFrom());
                                prodbomline.setLine(i*10);
                                prodbomline.setC_UOM_ID(profilebom.getC_UOM_ID());
                                prodbomline.setIsQtyPercentage(true);
                                prodbomline.setIsCritical(true);
                                prodbomline.setIssueMethod(prodbomline.ISSUEMETHOD_BackFlush);
                                prodbomline.save();
                                
                            }                
                  
                        }
           BigDecimal one =Env.ONE;
           MMPCProductBOMLine prodbomlineb = new MMPCProductBOMLine(prodbomp);
           prodbomlineb.setM_Product_ID(bolsa.getM_ProductE_ID());
          
           prodbomlineb.setQtyBatch(bolsa.getQtyE());
           prodbomlineb.setQtyBOM(Env.ZERO);
           prodbomlineb.setComponentType(prodbomlineb.COMPONENTTYPE_Packing);
           prodbomlineb.setValidFrom(prodbomp.getValidFrom());
           prodbomlineb.setValidTo(prodbomp.getValidTo());
           prodbomlineb.setLine(5);
           prodbomlineb.setC_UOM_ID(100);
           prodbomlineb.setIsQtyPercentage(true);
           prodbomlineb.setIssueMethod(prodbomlineb.ISSUEMETHOD_BackFlush);
           prodbomlineb.save();
           
           MMPCProductBOMLine prodbomlineet = new MMPCProductBOMLine(prodbomp);
           prodbomlineet.setM_Product_ID(1002823);
           BigDecimal dos = new BigDecimal(2);
          if (bolsa.isPacking())
          {
             prodbomlineet.setQtyBatch(bolsa.getQtyE().multiply(dos)); 
          }
          else
          {
           prodbomlineet.setQtyBatch(bolsa.getQtyE());
          }
           prodbomlineet.setQtyBOM(Env.ZERO);
           prodbomlineet.setComponentType(prodbomlineet.COMPONENTTYPE_Packing);
           prodbomlineet.setValidFrom(prodbomp.getValidFrom());
           prodbomlineet.setValidTo(prodbomp.getValidTo());
           prodbomlineet.setLine(7);
           prodbomlineet.setC_UOM_ID(100);
           prodbomlineet.setIsQtyPercentage(true);
           prodbomlineet.setIssueMethod(prodbomlineb.ISSUEMETHOD_BackFlush);
           prodbomlineet.save();
                  // termina reliberar
                  profilebom.setIsPublished(true);
                  profilebom.setMPC_Product_BOM_ID(prodbomp.getMPC_Product_BOM_ID());
                  profilebom.save();
                 
              }
           }
           else
           {
               salvado1 = "Falta especificar el empaque";
           }
           
		//
		return salvado1;
	}	//	doIt

         
}	//	CopyFromOrder
