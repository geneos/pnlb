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

import java.math.*;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;


import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
import compiere.model.*;
import org.eevolution.model.MMPCProfileBOM;
import org.eevolution.model.MMPCProfileBOMProduct;
import org.eevolution.model.MMPCProfileBOMReal;
import org.eevolution.model.MMPCProfileBOMSelected;
import org.compiere.util.QueryDB;

/**
 *  Copy Order Lines
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromOrder.java,v 1.4 2004/05/07 05:52:14 jjanke Exp $
 */
public class CopySelected extends SvrProcess
{
	/**	The Order				*/
	private int		p_MPC_ProfileBOM_ID = 0;
        private ArrayList acalc = new ArrayList();
            private ArrayList aunico = new ArrayList();
        private MAttributeSetInstance	m_masi;
        private int cont50=0;

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
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		//new CopySelected();
		//
		return "Exito";
	}	//	doIt
public CopySelected() {
        
      //  org.compiere.Compiere.startupClient(); 
      
      //  ArrayList list = new ArrayList();
                
                QueryDB query = new QueryDB("org.compiere.model.X_MPC_ProfileBOM");
             //   String filter = "MPC_Product_BOM_ID = " + MPC_Order_BOM_ID;
                List results = query.execute("");
                Iterator select = results.iterator();
                while (select.hasNext())
                { acalc.clear();
                   aunico.clear();
                   cont50=0;
                   
                   X_MPC_ProfileBOM xbom = (X_MPC_ProfileBOM) select.next();
                   //list.add(new MMPCProfileBOMLine(getCtx(), bomline.getMPC_Order_BOM_ID()));
                   //list.add(bomline);
                   MMPCProfileBOM  bom = new  MMPCProfileBOM(Env.getCtx(), xbom.getMPC_ProfileBOM_ID(),null);
                  
                   MMPCProfileBOMSelected[] pbomsel = bom.getSel();  
                   
                   for (int i=0; i<pbomsel.length; i++)
                   {
                       MMPCProfileBOMProduct bomprod = new MMPCProfileBOMProduct(Env.getCtx(),0,null);
                    
                       bomprod.setMPC_ProfileBOM_ID(pbomsel[i].getMPC_ProfileBOM_ID());
                       bomprod.setAD_Org_ID(pbomsel[i].getAD_Org_ID());
                       bomprod.setM_Product_ID(pbomsel[i].getM_Product_ID());
                 //      calculado(pbomsel[i].getM_Product_ID(),pbomsel[i].getQty().toString());
                       bomprod.setMinimum(pbomsel[i].getQty());
                       bomprod.save();
                   }
                 //  atributos(xbom.getMPC_ProfileBOM_ID());
                  
                }
//               // MMPCOrderBOMLine[] retValue = new MMPCOrderBOMLine[list.size()];
//                list.toArray(retValue);
//                return retValue; 
                
                
    }

 public void calculado(int m_M_Product_ID, String kgsel)
         {
              BigDecimal kgbd2 = new BigDecimal(kgsel);
             int m_M_AttributeSetInstance_ID=0; 
               int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
               try
                    {
                        StringBuffer sql1 = new StringBuffer("Select M_AttributeSetInstance_ID from M_Product where AD_Client_ID=? and M_Product_ID=?");
                            PreparedStatement pstmtsql1 = DB.prepareStatement(sql1.toString());
                        pstmtsql1.setInt(1, AD_Client_ID);    
			//pstmtsql.setInt(2, m_MPC_ProfileBOM_ID);
                        pstmtsql1.setInt(2, m_M_Product_ID);
			ResultSet rssql1 = pstmtsql1.executeQuery();
                        if (rssql1.next())
                        {
                           m_M_AttributeSetInstance_ID=rssql1.getInt(1);
                                System.out.println("instancia --------------     " +m_M_AttributeSetInstance_ID);
                        }
                        rssql1.close();
                        pstmtsql1.close();
                    }
                    catch (SQLException s)
                    {
                    }
             m_masi = MAttributeSetInstance.get(Env.getCtx(), m_M_AttributeSetInstance_ID, m_M_Product_ID);
             MAttributeSet as = m_masi.getMAttributeSet();
                System.out.println("attribute set --------------     " +as.getM_AttributeSet_ID());
             //   MAttribute[] attributes = as.getMAttributes (false);
               int instancia = m_masi.getM_AttributeSetInstance_ID();
                System.out.println("attribute set --------------     " +instancia);
               int nut_id=0;
               
               String nut_idst="";
               BigDecimal rn = Env.ZERO;
                BigDecimal mil = new BigDecimal(1000.0);
                 try
                    {
                        StringBuffer sql = new StringBuffer("Select M_Attribute_ID, ValueNumber from M_AttributeInstance where AD_Client_ID=? and M_AttributeSetInstance_ID=? and ValueNumber!=0");
                            PreparedStatement pstmtsql = DB.prepareStatement(sql.toString());
                        pstmtsql.setInt(1, AD_Client_ID);    
			//pstmtsql.setInt(2, m_MPC_ProfileBOM_ID);
                        pstmtsql.setInt(2, instancia);
			ResultSet rssql = pstmtsql.executeQuery();
                        while (rssql.next())
                        {
                            nut_id = rssql.getInt(1);
                            nut_idst= nut_idst.valueOf(nut_id);
                           acalc.add(cont50, nut_idst);
                           cont50++;
                            rn = rssql.getBigDecimal(2);
                             rn=rn.multiply(kgbd2);
                             rn=rn.divide(mil,3,rn.ROUND_HALF_UP);
                             acalc.add(cont50, rn);
                             cont50++;
                                System.out.println("attributo --------------     " +nut_id + "valor" +rn);
                              
                        }
                        rssql.close();
                        pstmtsql.close();
                    }
                    catch (SQLException s)
                    {
                    }
               
                
         }
 
 public void atributos(int m_MPC_ProfileBOM_ID)
 {
       int row5=0;
           //    miniTable3.setRowCount(row5);
                   for (int r=0;r<acalc.size(); r=r+2)
             {   
                 System.out.println("real arr nut " +acalc.get(r) +"real " +acalc.get(r+1));
               
                 int m_nut_id = new Integer(acalc.get(r).toString()).intValue();
                 BigDecimal realval = new BigDecimal(acalc.get(r+1).toString());
                 MMPCProfileBOM profile = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
                 MMPCProfileBOMReal profilereal = new MMPCProfileBOMReal(Env.getCtx(),0,null);
                
            //    profileselected.setClientOrg(profile.getAD_Client_ID(),profile.getAD_Org_ID());    
           profilereal.setMPC_ProfileBOM_ID(profile.getMPC_ProfileBOM_ID());
              //  Env.setContext(Env.getCtx(),m_WindowNo,"AD_Client_ID",profile.getAD_Client_ID());
                profilereal.setM_Attribute_ID(m_nut_id);
                    //profileselected.setC_UOM_ID();
                    profilereal.setMinimum(Env.ZERO);
                    
                    profilereal.setRealnut(realval);
                    profilereal.setMaximum(Env.ZERO);
                               // row++;
                                profilereal.save();
             
                   // IDColumn id2  = new IDColumn(m_nut_id);
                    
	/* if (!yaexistenut(acalc.get(r).toString()) || miniTable3.getRowCount()==0)
               {
        
         miniTable3.setRowCount(row5+1);
				//  set values
         id2.setSelected(true);
				miniTable3.setValueAt(id2, row5, 0);   //  C_Order_ID
				//miniTable3.setValueAt(rs.getString(2), row, 1);
                                Integer attint = new Integer(acalc.get(r).toString());
                                MAttribute att = new MAttribute(Env.getCtx(),attint.intValue());
                                KeyNamePair katt = new KeyNamePair(att.getM_Attribute_ID(),att.getName());
                                miniTable3.setValueAt(katt, row5, 2);  
                               // miniTable3.setValueAt(rs.getString(4), row, 3);  
				miniTable3.setValueAt(Env.ZERO, row5, 3);
                                realnutbd = new BigDecimal(acalc.get(r+1).toString());
                                
                       //         System.out.println("REAL NUT        "+realnutbd);
                                miniTable3.setValueAt(acalc.get(r+1), row5, 4);
                                miniTable3.setValueAt(Env.ZERO, row5, 5);  
                                row5++;
             }
                    
                    m_exist3=false; */
                      }
                int nutrient_id=0;
                int cont51=0;
                BigDecimal valrealbd= Env.ZERO;
               try{
                                 StringBuffer prodid = new StringBuffer("select M_Attribute_ID, trunc(SUM(RealNut),3) from MPC_ProfileBOM_Real where AD_Client_ID=1000000 and MPC_ProfileBOM_ID=? Group By M_Attribute_ID");
                           PreparedStatement pstmtprodid = DB.prepareStatement(prodid.toString());
		//	pstmtprodid.setInt(1, AD_Client_ID);
                        pstmtprodid.setInt(1, m_MPC_ProfileBOM_ID);
			ResultSet rsprodid = pstmtprodid.executeQuery();
                         
			//
			while (rsprodid.next())
			{
                          nutrient_id = rsprodid.getInt(1);  
                          String nutst_id ="";
                          nutst_id=nutst_id.valueOf(nutrient_id);
                          aunico.add(cont51,nutst_id);         
                          cont51++;
                          valrealbd= rsprodid.getBigDecimal(2);
                          aunico.add(cont51,valrealbd);         
                          cont51++;
                        } 
                        rsprodid.close();
                        pstmtprodid.close();
          }
                    catch(SQLException s){
                    }
                
                if (aunico.size()>0)
                {                    
                StringBuffer borrars = new StringBuffer("Delete From MPC_ProfileBOM_Real where MPC_ProfileBOM_ID=" + m_MPC_ProfileBOM_ID);
                DB.executeUpdate(borrars.toString());                         
                }
                
                for (int r=0;r<aunico.size(); r=r+2)
                {   
                 //System.out.println("real arr nut " +acalc.get(r) +"real " +acalc.get(r+1));
               
                 int m_nut_id = new Integer(aunico.get(r).toString()).intValue();
                 BigDecimal realval = new BigDecimal(aunico.get(r+1).toString());
                 MMPCProfileBOM profile = new MMPCProfileBOM(Env.getCtx(),m_MPC_ProfileBOM_ID,null);
                 MMPCProfileBOMReal profilereal = new MMPCProfileBOMReal(Env.getCtx(),0,null);
                
          
                 profilereal.setMPC_ProfileBOM_ID(profile.getMPC_ProfileBOM_ID());
             
                 profilereal.setM_Attribute_ID(m_nut_id);
                 
                    profilereal.setMinimum(Env.ZERO);
                    
                    profilereal.setRealnut(realval);
                    profilereal.setMaximum(Env.ZERO);
                               // row++;
                                profilereal.save();
                   }
 }
}	//	CopyFromOrder
