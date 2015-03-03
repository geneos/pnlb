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
 * Contributor(s): ______________________________________.
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



import org.compiere.apps.*;
import org.compiere.apps.form.*;
import org.compiere.apps.search.*;



import org.compiere.minigrid.*;




import org.compiere.swing.*;


import org.compiere.grid.*;
import org.eevolution.model.MMPCProfileBOM;
import org.eevolution.model.MMPCProfileBOMCost;
import org.compiere.plaf.*;

/**
 *  Copy Order Lines
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromOrder.java,v 1.4 2004/05/07 05:52:14 jjanke Exp $
 */
public class CreateProposal extends SvrProcess
{
	/**	The Order				*/
	private int		p_MPC_ProfileBOMCost_ID = 0;
        private int		p_MPC_ProfileBOM_ID = 0;
                private int		m_MPC_ProfileBOM_ID = 0;
              //   private String		m_C_Order_ID = "";
                 private String		m1_C_Order_ID = "";
                private Integer m_C_Order_ID = new Integer(0);
        private int m_WindowNo =0;
        private int m_instance=0;
        private String salvado="";

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
                        else if (name.equals("MPC_ProfileBOMCost_ID"))
				m_MPC_ProfileBOM_ID = ((Integer)para[i].getParameter()).intValue();
                        else if (name.equals("C_Order_ID"))
			m1_C_Order_ID = para[i].getParameter().toString();
                            //m_C_Order_ID = ((Integer)para[i].getParameter()).intValue();
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
	{ int m_MPC_ProfileBOM_ID = getRecord_ID();
                  System.out.println("orden ------- " +m1_C_Order_ID);
                  if (m1_C_Order_ID!="")
                  {
                    m_C_Order_ID = new Integer(m1_C_Order_ID);
                  }
                  else
                  {
                      m_C_Order_ID = new Integer(0);
                  }
		int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
          // int m_MPC_ProfileBOM_ID= Integer.parseInt(Env.getContext(Env.getCtx(),"MPC_ProfileBOM_ID"));
            //int p_MPC_ProfileBOM_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "MPC_PrifileBOM_ID");
                      System.out.println("profilebomcost   ---------" +m_MPC_ProfileBOM_ID);
                 try
            {
              StringBuffer sql=new StringBuffer("SELECT MPC_ProfileBOM_ID FROM MPC_ProfileBOMCost WHERE IsActive='Y' AND AD_Client_ID=? and MPC_ProfileBOMCost_ID=? ");
                         PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, AD_Client_ID);
                         pstmt.setInt(2, m_MPC_ProfileBOM_ID);
			//pstmt.setInt(2, m_M_PriceList_ID);
			ResultSet rs = pstmt.executeQuery();
			//while (!m_calculated && rsplv.next())
                        if (rs.next())
			{
                            p_MPC_ProfileBOM_ID= rs.getInt(1);
                            System.out.println("profilebom ---------" +p_MPC_ProfileBOM_ID);
                        }
                        rs.close();
                        pstmt.close();
}
catch (SQLException e)
{
}
                System.out.println("profilebom --------------" +p_MPC_ProfileBOM_ID);
         MMPCProfileBOM profileorder = new MMPCProfileBOM(Env.getCtx(),p_MPC_ProfileBOM_ID,null);
        
         //   MMPCProfileBOMSelected profilebomsel = new MMPCProfileBOMSelected(Env.getCtx(),profilebom.getMPC_ProfileBOM_ID());
           //MMPCProductBOM prodbom = new MMPCProductBOM(Env.getCtx(),0);     
          
        //   MPCProfileBOM.atributos(profilebom.getM_Product_ID());
         BigDecimal LineNetAmt =Env.ZERO;
         try
            {
              StringBuffer plv=new StringBuffer("SELECT LineNetAmt FROM MPC_ProfileBOMCost WHERE IsActive='Y' AND MPC_ProfileBOM_ID=? ");
                        PreparedStatement pstmtplv = DB.prepareStatement(plv.toString());
                        pstmtplv.setInt(1, p_MPC_ProfileBOM_ID);
			//pstmt.setInt(2, m_M_PriceList_ID);
			ResultSet rsplv = pstmtplv.executeQuery();
			//while (!m_calculated && rsplv.next())
                        if (rsplv.next())
			{
                            LineNetAmt= rsplv.getBigDecimal(1);
                        }
                        rsplv.close();
                        pstmtplv.close();
            }
        catch (SQLException e)
            {
            }
          
           
          //   MMPCProfileBOM profileorder = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID);
                int m_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));    
                int m_AD_Org_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Org_ID"));                   
             //   int m_M_Warehouse_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#M_Warehouse_ID"));
    //    MProfileBOM profileorder = new MProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID);
        
        MOrder order = new MOrder(Env.getCtx(),m_C_Order_ID.intValue(),null);
   //     order.setClientOrg(m_Client_ID, m_AD_Org_ID);
//        order.setAD_Client_ID(m_Client_ID);
        if (m_C_Order_ID.intValue()==0)
        {
        order.setAD_Org_ID(1000006);
        order.setAD_OrgTrx_ID(1000026);
        int profcost =0;
          try
        {
      StringBuffer sqlcost1 = new StringBuffer("Select MPC_ProfileBOMCost_ID From MPC_ProfileBOMCost where AD_Client_ID=? and MPC_ProfileBOM_ID=?");
		//  reset table
		//  Execute
		
			PreparedStatement pstmtcost1 = DB.prepareStatement(sqlcost1.toString());
			pstmtcost1.setInt(1, m_Client_ID);
                        pstmtcost1.setInt(2, p_MPC_ProfileBOM_ID);
			ResultSet rscost1 = pstmtcost1.executeQuery();
                         
			while (rscost1.next())
			{
                            profcost= rscost1.getInt(1);
			}
			rscost1.close();
			pstmtcost1.close();        
        }
        catch(SQLException s)
        {
        } 
        MMPCProfileBOMCost profilecost1 = new MMPCProfileBOMCost(Env.getCtx(),profcost,null);
        System.out.println("Fecha orden costo  ****" +profilecost1.getDateOrdered());
        order.setDateOrdered(profilecost1.getDateOrdered());
       
        order.setC_BPartner_ID(profileorder.getC_BPartner_ID());
        int user=0;
        try
        {
      StringBuffer sql = new StringBuffer("Select AD_User_ID From AD_User where AD_Client_ID=? and C_BPartner_ID=?");
		//  reset table
		//  Execute
		
			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1,AD_Client_ID);
                        pstmt.setInt(2, profileorder.getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery();
                         
			while (rs.next())
			{
                            user= rs.getInt(1);
			}
			rs.close();
			pstmt.close();        
        }
        catch(SQLException s)
        {
        }
        order.setSalesRep_ID(user);
        //order.setSalesRep_ID(order.SALESREP_ID_AD_Reference_ID);
         //   System.out.println("Sales rep " +order.SALESREP_ID_AD_Reference_ID);
   //    order.setM_Warehouse_ID(m_M_Warehouse_ID);
         int warehouse=0;
         System.out.println("profileorder.getAD_Org_ID()    "+profileorder.getAD_Org_ID());
         System.out.println("m_AD_Org_ID    "+m_AD_Org_ID);
        try
        {
      StringBuffer sql = new StringBuffer("Select M_Warehouse_ID From M_Warehouse where AD_Client_ID=? and AD_Org_ID=1000006");
		//  reset table
		//  Execute
		
			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
                        pstmt.setInt(1,AD_Client_ID);
               //         pstmt.setInt(2, m_AD_Org_ID);
			ResultSet rs = pstmt.executeQuery();
                         
			while (rs.next())
			{
                            warehouse= rs.getInt(1);
			}
			rs.close();
			pstmt.close();        
        }
        catch(SQLException s)
        {
        }
         System.out.println("warehose------------------ " +warehouse);
        order.setM_Warehouse_ID(1000017);
        System.out.println("dateordered------------------ " +profileorder.getDateDoc());
        
         order.setC_DocTypeTarget_ID(order.DocSubTypeSO_Proposal);
        int listaprecios =0;
        try
        {
      StringBuffer sql = new StringBuffer("SELECT pl.M_PriceList_ID, BOM_PriceStd(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceStd, BOM_PriceList(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceList, BOM_PriceLimit(p.M_Product_ID,pv.M_PriceList_Version_ID) AS PriceLimit,pv.ValidFrom,pl.C_Currency_ID, pv.M_PriceList_Version_ID FROM M_Product p INNER JOIN M_ProductPrice pp ON (p.M_Product_ID=pp.M_Product_ID) INNER JOIN  M_PriceList_Version pv ON (pp.M_PriceList_Version_ID=pv.M_PriceList_Version_ID) INNER JOIN M_Pricelist pl ON (pv.M_PriceList_ID=pl.M_PriceList_ID) WHERE pv.IsActive='Y' and pl.IsDefault='Y' AND pl.IsSOPriceList='Y' and pv.AD_Client_ID=? ORDER BY pv.ValidFrom");
		//  reset table
		//  Execute
		
			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
                         
			while (rs.next())
			{
                            listaprecios= rs.getInt(1);
			}
			rs.close();
			pstmt.close();        
        }
        catch(SQLException s)
        {
        }
        
        int locationid =0;
        try
        {
      StringBuffer sql = new StringBuffer("Select C_BPartner_Location_ID From C_BPartner_Location where AD_Client_ID=? and C_BPartner_ID=?");
		//  reset table
		//  Execute
		
			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, m_Client_ID);
                        pstmt.setInt(2, profileorder.getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery();
                         
			while (rs.next())
			{
                            locationid= rs.getInt(1);
			}
			rs.close();
			pstmt.close();        
        }
        catch(SQLException s)
        {
        }  
        order.setC_BPartner_Location_ID(locationid);
       
       
       // order.setC_DocType_ID(order.C_DOCTYPE_ID_AD_Reference_ID);
       order.setM_PriceList_ID(profilecost1.getM_PriceList_ID());
       if(order.save())
       {
           salvado = "Cotizacion no. "+order.getDocumentNo();
           
         //  ADialog.info(m_WindowNo,this,salvado);
           profileorder.setC_Order_ID(order.getDocumentNo());
           profileorder.save();
          MOrderLine orderline = new MOrderLine(order);
          
          try
        {
      StringBuffer sqlcost = new StringBuffer("Select MPC_ProfileBOMCost_ID From MPC_ProfileBOMCost where AD_Client_ID=? and MPC_ProfileBOM_ID=?");
		//  reset table
		//  Execute
		
			PreparedStatement pstmtcost = DB.prepareStatement(sqlcost.toString());
			pstmtcost.setInt(1, m_Client_ID);
                        pstmtcost.setInt(2, p_MPC_ProfileBOM_ID);
			ResultSet rscost = pstmtcost.executeQuery();
                         
			while (rscost.next())
			{
                            profcost= rscost.getInt(1);
			}
			rscost.close();
			pstmtcost.close();        
        }
        catch(SQLException s)
        {
        }  
        MMPCProfileBOMCost profilecost = new MMPCProfileBOMCost(Env.getCtx(),profcost,null);
        MPriceList lp = new MPriceList(Env.getCtx(),profilecost.getM_PriceList_ID(),null);
        int lpversionid =0;
        try
        {
      StringBuffer sql = new StringBuffer("SELECT M_PriceList_Version_ID FROM M_PriceList_Version WHERE AD_Client_ID=? and M_PriceList_ID=?");
		//  reset table
		//  Execute
		
			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, m_Client_ID);
                        pstmt.setInt(2, profilecost.getM_PriceList_ID());
			ResultSet rs = pstmt.executeQuery();
                         
			while (rs.next())
			{
                            lpversionid= rs.getInt(1);
			}
			rs.close();
			pstmt.close();        
        }
        catch(SQLException s)
        {
        }  
        
      //  MProfileBOMSelected profileselected = new MProfileBOMSelected(Env.getCtx(), m_MPC_ProfileBOM_ID);
       orderline.setC_Order_ID(order.getC_Order_ID());
        orderline.setM_Product_ID(profileorder.getM_Product_ID());
//          System.out.println("costo final --- " +jTextField1.getText().toString());
//      BigDecimal costobd =new BigDecimal(profileselected.getPriceList());
//      System.out.println("costo final --- " +costobd);
      BigDecimal kgbd =new BigDecimal(1000.0);
      
      //System.out.println("kg --- " +profileorder.getQty().doubleValue());
      BigDecimal costouni = new BigDecimal(profileorder.getLineNetAmt().doubleValue()/profileorder.getQty().doubleValue());
       //System.out.println("costo --- " +profileorder.getPriceList());
        System.out.println("costo bd --- " +profilecost.getLineNetAmt().setScale(3,5));
//        orderline.setQtyOrdered(profileorder.getQty());
//        orderline.setQtyEntered(profileorder.getQty());
        orderline.setQtyOrdered(Env.ONE);
        orderline.setQtyEntered(Env.ONE);
        orderline.setC_UOM_ID(profileorder.getC_UOM_ID());
//        orderline.setPriceList(costouni.setScale(3,5));
//        orderline.setPriceEntered(costouni.setScale(3,5)); 
//        orderline.setPriceActual(costouni.setScale(3,5));
        
        
        //orderline.setDiscount(Env.ZERO);
        //BigDecimal totallinea = new BigDecimal(costouni.doubleValue()*profileorder.getQty().doubleValue());
        //orderline.setLineNetAmt(totallinea);
        orderline.setC_Tax_ID(1000001);
        orderline.setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());
        String desc = "";
        MProduct product = new MProduct(Env.getCtx(),profileorder.getM_Product_ID(),null);
        
        if (profileorder.getDescription()!=null)
            desc = profileorder.getDescription();
        if (profilecost.getDescription()!=null)
            desc = desc + " " +profilecost.getDescription();
        //if (product.getShelfDepth().compareTo(Env.ZERO) >0)
        //    desc = desc + " " + product.getShelfDepth();
            
        
        orderline.setDescription(desc);
        BigDecimal convrate = Env.ZERO;
//        if (profilecost.isUSD())
//        {
//        orderline.setC_Currency_ID(100);
//         
//                        
//                           StringBuffer conv=new StringBuffer("select C_Conversion_Rate_ID, C_Currency_ID,C_Currency_ID_To, MultiplyRate from C_Conversion_Rate where AD_Client_ID=? and C_Currency_ID=100 order by ValidFrom");
//                         PreparedStatement pstmtplv1 = DB.prepareStatement(conv.toString());
//			pstmtplv1.setInt(1, AD_Client_ID);
//                         
//			//pstmt.setInt(2, m_M_PriceList_ID);
//			ResultSet rsplv1 = pstmtplv1.executeQuery();
//                        while (rsplv1.next())
//                        {
//                            if (rsplv1.getInt(3)==130)
//                            {
//                             convrate=rsplv1.getBigDecimal(4);
//                             BigDecimal convertido = Env.ZERO;
//                             convertido = convrate.multiply(profilecost.getOfferAmt());
//                             orderline.setPriceList(convertido.setScale(3,5));
//        orderline.setPriceEntered(convertido.setScale(3,5)); 
//        orderline.setPriceActual(convertido.setScale(3,5));
////                             System.out.println("tipo de cambio" +convrate);
//                            }
//                        }
//                        rsplv1.close();
//                        pstmtplv1.close();
//        }
//        else
//        {
//       orderline.setC_Currency_ID(130);
        orderline.setPriceList(profilecost.getOfferAmt().divide(Env.ONE,3,BigDecimal.ROUND_HALF_UP));
        orderline.setPriceEntered(profilecost.getOfferAmt().divide(Env.ONE,3,BigDecimal.ROUND_HALF_UP)); 
        orderline.setPriceActual(profilecost.getOfferAmt().divide(Env.ONE,3,BigDecimal.ROUND_HALF_UP));
//        }
        boolean existelp =false;
        try
        {
      StringBuffer sql100 = new StringBuffer("Select M_Product_ID From M_ProductPrice where M_PriceList_Version_ID="+lpversionid +" and M_Product_ID=" +profileorder.getM_Product_ID());
		//  reset table
		//  Execute
		
			PreparedStatement pstmt100 = DB.prepareStatement(sql100.toString());
			//pstmt1.setInt(1, m_Client_ID);
                        //pstmt1.setInt(2, lpversionid);
                         System.out.println("si esxite este roducto en esta lista de precios "+sql100.toString());
			ResultSet rs100 = pstmt100.executeQuery();
                         
			while (rs100.next())
			{
                            existelp =true;
                            System.out.println("si esxite este roducto en esta lista de precios "+rs100.getInt(1));
			}
			rs100.close();
			pstmt100.close();        
        }
        catch(SQLException s)
        {
        } 
        
        
        
        
        if (lpversionid!=0)
        {
        if (!existelp)
        {
       
            MProductPrice mpp = new MProductPrice(Env.getCtx(),lpversionid,profileorder.getM_Product_ID(),profilecost.getOfferAmt().setScale(3,5),profilecost.getOfferAmt().setScale(3,5),profilecost.getOfferAmt().setScale(3,5),null);
            mpp.save();
        }
        
        else
        {
             StringBuffer sqlsb= new StringBuffer("Update M_ProductPrice set PriceStd="+profilecost.getOfferAmt().setScale(3,5) +", PriceList="+profilecost.getOfferAmt().setScale(3,5) +", PriceLimit="+profilecost.getOfferAmt().setScale(3,5) +" Where M_Product_ID="+profileorder.getM_Product_ID() +"and M_PriceList_Version_ID=" +lpversionid);
             DB.executeUpdate(sqlsb.toString()); 
        }
        }
        orderline.save();
        
        
       }
       else
       {
           if (profileorder.getC_BPartner_ID()== 0)
               salvado = "No se ha especificado el Socio de Negocio";
           else
               salvado = "No se pudo generar la Cotizaci�n";
         //  log.error(m_WindowNo,this,"No se pudo generar la Cotizaci�n");
       }
       
//           for (int i=0; i<miniTable2.getRowCount(); i++)
//           {
        
        }
        else
        {
            int profcost=0;
            MOrderLine orderline = new MOrderLine(order);
          
          try
        {
      StringBuffer sqlcost = new StringBuffer("Select MPC_ProfileBOMCost_ID From MPC_ProfileBOMCost where AD_Client_ID=? and MPC_ProfileBOM_ID=?");
		//  reset table
		//  Execute
		
			PreparedStatement pstmtcost = DB.prepareStatement(sqlcost.toString());
			pstmtcost.setInt(1, m_Client_ID);
                        pstmtcost.setInt(2, p_MPC_ProfileBOM_ID);
			ResultSet rscost = pstmtcost.executeQuery();
                         
			while (rscost.next())
			{
                            profcost= rscost.getInt(1);
			}
			rscost.close();
			pstmtcost.close();        
        }
        catch(SQLException s)
        {
        }  
        MMPCProfileBOMCost profilecost = new MMPCProfileBOMCost(Env.getCtx(),profcost,null);
        MPriceList lp = new MPriceList(Env.getCtx(),profilecost.getM_PriceList_ID(),null);
        int lpversionid =0;
        try
        {
      StringBuffer sql = new StringBuffer("Select M_PriceList_Version_ID From M_PriceList_Version where AD_Client_ID=? and M_PriceList_ID=?");
		//  reset table
		//  Execute
		
			PreparedStatement pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, m_Client_ID);
                        pstmt.setInt(2, profilecost.getM_PriceList_ID());
			ResultSet rs = pstmt.executeQuery();
                         
			while (rs.next())
			{
                            lpversionid= rs.getInt(1);
			}
			rs.close();
			pstmt.close();        
        }
        catch(SQLException s)
        {
        }  
        
      //  MProfileBOMSelected profileselected = new MProfileBOMSelected(Env.getCtx(), m_MPC_ProfileBOM_ID);
       orderline.setC_Order_ID(order.getC_Order_ID());
        orderline.setM_Product_ID(profileorder.getM_Product_ID());
        
//          System.out.println("costo final --- " +jTextField1.getText().toString());
//      BigDecimal costobd =new BigDecimal(profileselected.getPriceList());
//      System.out.println("costo final --- " +costobd);
      BigDecimal kgbd =new BigDecimal(1000.0);
      
      //System.out.println("kg --- " +profileorder.getQty().doubleValue());
      BigDecimal costouni = new BigDecimal(profileorder.getLineNetAmt().doubleValue()/profileorder.getQty().doubleValue());
       //System.out.println("costo --- " +profileorder.getPriceList());
        System.out.println("costo bd --- " +profilecost.getLineNetAmt().setScale(3,5));
//        orderline.setQtyOrdered(profileorder.getQty());
//        orderline.setQtyEntered(profileorder.getQty());
        orderline.setQtyOrdered(Env.ONE);
        orderline.setQtyEntered(Env.ONE);
        orderline.setC_UOM_ID(profileorder.getC_UOM_ID());
//        orderline.setPriceList(costouni.setScale(3,5));
//        orderline.setPriceEntered(costouni.setScale(3,5)); 
//        orderline.setPriceActual(costouni.setScale(3,5));
        
        
        //orderline.setDiscount(Env.ZERO);
        //BigDecimal totallinea = new BigDecimal(costouni.doubleValue()*profileorder.getQty().doubleValue());
        //orderline.setLineNetAmt(totallinea);
        orderline.setC_Tax_ID(1000001);
        orderline.setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());
          String desc = "";
        MProduct product = new MProduct(Env.getCtx(),profileorder.getM_Product_ID(),null);
        
        if (profileorder.getDescription()!=null)
            desc = profileorder.getDescription();
        if (profilecost.getDescription()!=null)
            desc = desc + " " +profilecost.getDescription();
        //if (product.getShelfDepth().compareTo(Env.ZERO) >0)            
        //    desc = desc + " " + product.getShelfDepth();
            
        
        orderline.setDescription(desc);
        BigDecimal convrate = Env.ZERO;
//        if (profilecost.isUSD())
//        {
//        orderline.setC_Currency_ID(100);
//         
//                        
//                           StringBuffer conv=new StringBuffer("select C_Conversion_Rate_ID, C_Currency_ID,C_Currency_ID_To, MultiplyRate from C_Conversion_Rate where AD_Client_ID=? and C_Currency_ID=100 order by ValidFrom");
//                         PreparedStatement pstmtplv1 = DB.prepareStatement(conv.toString());
//			pstmtplv1.setInt(1, AD_Client_ID);
//                         
//			//pstmt.setInt(2, m_M_PriceList_ID);
//			ResultSet rsplv1 = pstmtplv1.executeQuery();
//                        while (rsplv1.next())
//                        {
//                            if (rsplv1.getInt(3)==130)
//                            {
//                             convrate=rsplv1.getBigDecimal(4);
//                             BigDecimal convertido = Env.ZERO;
//                             convertido = convrate.multiply(profilecost.getOfferAmt());
//                             orderline.setPriceList(convertido.setScale(3,5));
//        orderline.setPriceEntered(convertido.setScale(3,5)); 
//        orderline.setPriceActual(convertido.setScale(3,5));
////                             System.out.println("tipo de cambio" +convrate);
//                            }
//                        }
//                        rsplv1.close();
//                        pstmtplv1.close();
//        }
//        else
//        {
//        orderline.setC_Currency_ID(130);
        
        orderline.setPriceList(profilecost.getOfferAmt().divide(Env.ONE,3,BigDecimal.ROUND_HALF_UP));
        orderline.setPriceEntered(profilecost.getOfferAmt().divide(Env.ONE,3,BigDecimal.ROUND_HALF_UP)); 
        orderline.setPriceActual(profilecost.getOfferAmt().divide(Env.ONE,3,BigDecimal.ROUND_HALF_UP));
//        }
        boolean existelp =false;
        try
        {
      StringBuffer sql100 = new StringBuffer("Select M_Product_ID From M_ProductPrice where M_PriceList_Version_ID="+lpversionid +" and M_Product_ID=" +profileorder.getM_Product_ID());
		//  reset table
		//  Execute
		
			PreparedStatement pstmt100 = DB.prepareStatement(sql100.toString());
			//pstmt1.setInt(1, m_Client_ID);
                        //pstmt1.setInt(2, lpversionid);
                         System.out.println("si esxite este roducto en esta lista de precios "+sql100.toString());
			ResultSet rs100 = pstmt100.executeQuery();
                         
			while (rs100.next())
			{
                            existelp =true;
                            System.out.println("si esxite este roducto en esta lista de precios "+rs100.getInt(1));
			}
			rs100.close();
			pstmt100.close();        
        }
        catch(SQLException s)
        {
        } 
        
        
        
        
        if (lpversionid!=0)
        {
        if (!existelp)
        {
       
            MProductPrice mpp = new MProductPrice(Env.getCtx(),lpversionid,profileorder.getM_Product_ID(),profilecost.getOfferAmt().setScale(3,5),profilecost.getOfferAmt().setScale(3,5),profilecost.getOfferAmt().setScale(3,5),null);
            mpp.save();
        }
        
        else
        {
             StringBuffer sqlsb= new StringBuffer("Update M_ProductPrice set PriceStd="+profilecost.getOfferAmt().setScale(3,5) +", PriceList="+profilecost.getOfferAmt().setScale(3,5) +", PriceLimit="+profilecost.getOfferAmt().setScale(3,5) +" Where M_Product_ID="+profileorder.getM_Product_ID() +"and M_PriceList_Version_ID=" +lpversionid);
             DB.executeUpdate(sqlsb.toString()); 
        }
        }
        orderline.save();
        }//
		return salvado;
	}	//	doIt

        
}	//	CopyFromOrder
