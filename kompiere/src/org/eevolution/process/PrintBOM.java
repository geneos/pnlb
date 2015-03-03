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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.*;
import java.math.BigDecimal;


import org.compiere.model.MQuery;
import org.compiere.model.PrintInfo;
import org.compiere.model.X_T_BOMLine;
import org.compiere.model.X_RV_MPC_Product_BOMLine;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;


/**
 *  BOM lines explosion for print
 *
 *        @author Sergio Ramazzina,Victor Perez
 *        @version $Id: PrintBOM.java,v 1.2 2005/04/19 12:54:30 srama Exp $
 */
public class PrintBOM extends SvrProcess {
	
	
    private static final Properties ctx = Env.getCtx();
    private static final String AD_Client_ID = ctx.getProperty("#AD_Client_ID");
    private static final String AD_Org_ID = ctx.getProperty("#AD_Org_ID");
    //private int p_MPC_Product_BOM_ID = 0;
    private int p_M_Product_ID = 0;
    private boolean p_implotion = false;
    private ProcessInfo info = null;
    private int LevelNo = 1;
    private int SeqNo = 0;
    private String levels  = new String("....................");
    private int AD_PInstance_ID = 0;

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
			else if (name.equals("M_Product_ID"))
            {    
				p_M_Product_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
            }
			else if (name.equals("Implotion"))
            {    
				p_implotion = ((String)para[i].getParameter()).equals("N") ? false : true;                                
            }
			
			else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
    } //        prepare

    /**
     *  Perform process.
     *  @return Message (clear text)
     *  @throws Exception if not successful
     */
    protected String doIt() throws Exception {
        info = getProcessInfo();
        AD_PInstance_ID = getAD_PInstance_ID();
        loadBOM();
        print();

        return "@ProcessOK@";
    } //        doIt
    
    void print()
    {
    	 	MPrintFormat format = null;
    	 	Language language = Language.getLoginLanguage();		//	Base Language
    	 	
    	 	format = MPrintFormat.get(getCtx(), 1000525, false);

			format.setLanguage(language);
			format.setTranslationLanguage(language);
			//	query
			MQuery query = new MQuery("RV_MPC_Product_BOMLine");
			query.addRestriction("AD_PInstance_ID", MQuery.EQUAL, new Integer(AD_PInstance_ID));
                         
			//	Engine
            PrintInfo info = new PrintInfo( "RV_MPC_Product_BOMLine",X_RV_MPC_Product_BOMLine.getTableId(X_RV_MPC_Product_BOMLine.Table_Name), getRecord_ID());               
			ReportEngine re = new ReportEngine(getCtx(), format, query, info);
            new Viewer(re);
            
            String sql = "DELETE FROM T_BomLine WHERE AD_PInstance_ID = " + AD_PInstance_ID;			
            DB.executeUpdate(sql);

                 
    }

    /*
    private int getBOMForProduct(int M_Product_ID) {
        int bomID = DB.getSQLValue(null,"SELECT MPC_PRODUCT_BOM_ID FROM MPC_PRODUCT_BOM WHERE M_PRODUCT_ID=?",
        		M_Product_ID);

        return bomID;
    }

    private int getBOMForProduct(int M_Product_ID, int M_ATTRIBUTESETINSTANCE_ID) {
        int bomID = DB.getSQLValue(null,
                "SELECT MPC_PRODUCT_BOM_ID FROM MPC_PRODUCT_BOM WHERE M_PRODUCT_ID=?" +
                " AND M_ATTRIBUTESETINSTANCE_ID=?", M_Product_ID,
				M_ATTRIBUTESETINSTANCE_ID);

        return bomID;
    }*

    private int getProductForBOM(int MPC_PRODUCT_BOM_ID) {
        int bomID = DB.getSQLValue(null,"SELECT M_PRODUCT_ID FROM MPC_PRODUCT_BOM WHERE MPC_PRODUCT_BOM_ID=?",
        		MPC_PRODUCT_BOM_ID);

        return bomID;
    }*/
    
    
    /**
	 * 	Action: Fill Tree with all nodes
	 */
	private void loadBOM()
	{

				
                if (p_M_Product_ID == 0)
                	return;

                
                X_T_BOMLine tboml = new X_T_BOMLine(ctx, 0 , null);
                tboml.setMPC_Product_BOM_ID(0);
                tboml.setMPC_Product_BOMLine_ID(0);
                tboml.setM_Product_ID(p_M_Product_ID);
                tboml.setLevelNo(0);
                tboml.setLevels("0");
                tboml.setSeqNo(0);
                tboml.setAD_PInstance_ID(AD_PInstance_ID);
                tboml.save(get_TrxName());
                
                if (p_implotion)
                {
                	PreparedStatement stmt = null;	
                    ResultSet rs = null;
                    String sql = new String("SELECT MPC_Product_BOMLine_ID FROM MPC_Product_BOMLine WHERE IsActive = 'Y' AND M_Product_ID = ? ");
                    //System.out.println("Implotion " + sql);
                    try
           		 	{
                           stmt = DB.prepareStatement(sql);
                           stmt.setInt(1, p_M_Product_ID);
                           rs = stmt.executeQuery();
                           
                           while (rs.next()) 
                           {
                           	parentImplotion(rs.getInt(1));
                           	//System.out.println("Implotion MPC_Product_BOMLine_ID:"+ rs.getInt(1));
                           }
           		 	}
                    catch (SQLException e) 
           		 	{
           	        	log.log(Level.SEVERE,"explodeBOM " , e);

           		 	}  

                }
                else
                {
                	PreparedStatement stmt = null;	
                    ResultSet rs = null;
                    String sql = new String("SELECT MPC_Product_BOM_ID FROM MPC_Product_BOM WHERE IsActive = 'Y' AND M_Product_ID = ? ");
                    System.out.println("Explotion" + sql);
                    try
           		 	{
                           stmt = DB.prepareStatement(sql);
                           stmt.setInt(1, p_M_Product_ID);
                           rs = stmt.executeQuery();                           
                           while (rs.next()) 
                           {
                           	parentExplotion(rs.getInt(1));
                           	System.out.println("Explotion MPC_Product_BOM_ID "+ rs.getInt(1));
                           }
           		 	}
                    catch (SQLException e) 
           		 	{
           	        	log.log(Level.SEVERE,"explodeBOM " , e);

           		 	}  
                    

                }               
	}	
	
	public void  parentImplotion(int MPC_Product_BOMLine_ID) 
    {

        
         X_T_BOMLine tboml = new X_T_BOMLine(ctx,0,null);
         int MPC_Product_BOM_ID = DB.getSQLValue(null,"SELECT MPC_Product_BOM_ID FROM MPC_Product_BOMLine WHERE MPC_Product_BOMLine_ID=?",MPC_Product_BOMLine_ID);
         int M_Product_ID = DB.getSQLValue(null,"SELECT M_Product_ID FROM MPC_Product_BOM WHERE MPC_Product_BOM_ID=?",MPC_Product_BOM_ID);
         tboml.setMPC_Product_BOM_ID(MPC_Product_BOM_ID);
         tboml.setMPC_Product_BOMLine_ID(MPC_Product_BOMLine_ID);
         tboml.setM_Product_ID(M_Product_ID);	 
         tboml.setLevelNo(LevelNo);
         System.out.println("LevelNo"+ LevelNo);
         if(LevelNo >= 11)
         tboml.setLevels(levels + ">" + LevelNo);	
         else if(LevelNo >= 1)	 
         tboml.setLevels(levels.substring(0,LevelNo) + LevelNo);
         
         tboml.setSeqNo(SeqNo);
         tboml.setAD_PInstance_ID(AD_PInstance_ID);
         tboml.save(get_TrxName());
         
         PreparedStatement stmt = null;	
         ResultSet rs = null;
         String sql = new String("SELECT MPC_Product_BOM_ID, M_Product_ID FROM MPC_Product_BOM WHERE IsActive = 'Y' AND M_Product_ID = ? ");
         try
		 {
                stmt = DB.prepareStatement(sql);
                stmt.setInt(1, M_Product_ID);
                rs = stmt.executeQuery();
                
                while (rs.next()) 
                {
                	System.out.println("Lines:" + rs.getInt(1) + "Lines:" + "Producto ID" +rs.getInt(2));
                	SeqNo += 1;
                	component(rs.getInt(2));
                }
		 }
         catch (SQLException e) 
		 {
	        	log.log(Level.SEVERE,"explodeBOM " , e);

	     }      

    }
         
         public void  parentExplotion(int MPC_Product_BOM_ID) 
         {
         PreparedStatement stmt = null;	
         ResultSet rs = null;
         String sql = new String("SELECT MPC_Product_BOMLine_ID, M_Product_ID FROM MPC_Product_BOMLine boml WHERE IsActive = 'Y' AND MPC_Product_BOM_ID = ? ");
         //LevelNo += 1;
         try
		 {
                stmt = DB.prepareStatement(sql);
                stmt.setInt(1, MPC_Product_BOM_ID);
                rs = stmt.executeQuery();
                
                while (rs.next()) 
                {
                	
                	System.out.println("BOM  Lines ID:" + rs.getInt(1) + "Lines:" + "Producto ID" +rs.getInt(2));
                	SeqNo += 1;
                    X_T_BOMLine tboml = new X_T_BOMLine(ctx, 0 , null);
                    tboml.setMPC_Product_BOM_ID(MPC_Product_BOM_ID);
                    tboml.setMPC_Product_BOMLine_ID(rs.getInt(1));
                    tboml.setM_Product_ID(rs.getInt(2));
                    tboml.setLevelNo(LevelNo);
                    tboml.setLevels(levels.substring(0,LevelNo) + LevelNo);
                    tboml.setSeqNo(SeqNo);
                    tboml.setAD_PInstance_ID(AD_PInstance_ID);
                    tboml.save(get_TrxName());
                    component(rs.getInt(2));
                }    
         	
		 }
         catch (SQLException e) 
		 {
	        	log.log(Level.SEVERE,"explodeBOM " , e);	           
	     } 
         
         
    }
    
   
    
    public void component(int M_Product_ID) 
    {   
        
        if (p_implotion)
        {
         
        	LevelNo += 1;
        	PreparedStatement stmt = null;	
            ResultSet rs = null;
            String sql = new String("SELECT MPC_Product_BOMLine_ID FROM MPC_Product_BOMLine  WHERE IsActive = 'Y' AND M_Product_ID = ? ");
            try
			 {
	                stmt = DB.prepareStatement(sql);	                
	                stmt.setInt(1, M_Product_ID);
	                rs = stmt.executeQuery();
	                boolean level = false;
	                while (rs.next()) 
	                {
                        //if (!level) // by hamed
                        //LevelNo += 1; //by hamed
                        //System.out.println("Nivel entra LevelNo"+ LevelNo);         
	                	//level = true;
	                	parentImplotion(rs.getInt(1));
                        //LevelNo -= 1; //by hamed
                        //System.out.println("Nivel Sale LevelNo"+ LevelNo); 
	                } 
	                rs.close();
	                stmt.close();
	                LevelNo -= 1;
	                return;	               	        	         	
			 }
	         catch (SQLException e) 
			 {
		        	log.log(Level.SEVERE,"explodeBOM " , e);
		            //return -1;
		     } 
 
        }
        else
        {
        	//            LevelNo += 1; //by hamed
        	PreparedStatement stmt = null;	
            ResultSet rs = null;
            String sql = new String("SELECT MPC_Product_BOM_ID FROM MPC_Product_BOM  WHERE IsActive = 'Y' AND Value = ? ");
            try
	    {
	                stmt = DB.prepareStatement(sql);
	                String Value = DB.getSQLValueString(null,"SELECT Value FROM M_PRODUCT WHERE M_PRODUCT_ID=?",M_Product_ID);
	                stmt.setString(1, Value);
	                rs = stmt.executeQuery();
	                boolean level = false;
	                while (rs.next()) 
	                {
                                if (!level) // by hamed
                                LevelNo += 1; //by hamed
                                 
	                	level = true;
	                	parentExplotion(rs.getInt(1));
                        LevelNo -= 1; //by hamed
	                } 
	                rs.close();
	                stmt.close();
	                return;
	        
            }	                
	         catch (SQLException e) 
			 {
		        	log.log(Level.SEVERE,"explodeBOM " , e);
		           
		     } 
        	        	
        }
    }

    /**
     * @param productBOMId
     */
    /*
    private int explodeBOM(int MPC_PRODUCT_BOM_ID) 
    {
        String sqlCmd1 = "SELECT bomlp.M_Product_ID, bomlp.IsBOM, boml.qtybom, " +
            "bomlp.c_uom_id, boml.m_attributesetinstance_id " +
            " from mpc_product_bomline boml inner join m_product " +
            "bomlp on boml.m_product_id=bomlp.m_product_id " +
            " where boml.mpc_product_bom_id=? and boml.AD_Client_ID=? order by line";
        String sqlCmd2 =
            "insert into t_ita_printbomline (ad_pinstance_id, ad_client_id, ad_org_id, " +
            "createdby, updatedby, seqno, m_product_id, mpc_product_bom_id, " +
            "lvl, c_uom_id, qtybom, m_attributesetinstance_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement prsm = null;
        PreparedStatement prsm1 = null;
        ResultSet rs = null;

        // Check If it is printable
        try {
            prsm = DB.prepareStatement(sqlCmd1);
            prsm1 = DB.prepareStatement(sqlCmd2);

            prsm.setInt(1, MPC_PRODUCT_BOM_ID);
            prsm.setString(2, AD_Client_ID);

            rs = prsm.executeQuery();

            LevelNo += 1;

            while (rs.next()) 
            {
                SeqNo += 1;
                prsm1.setInt(1, info.getAD_PInstance_ID());
                prsm1.setString(2, AD_Client_ID);
                prsm1.setString(3, AD_Org_ID);
                prsm1.setInt(4, info.getAD_User_ID().intValue());
                prsm1.setInt(5, info.getAD_User_ID().intValue());
                prsm1.setInt(6, SeqNo);
                prsm1.setInt(7, rs.getInt(1));
                prsm1.setInt(8, p_MPC_Product_BOM_ID);
                prsm1.setInt(9, LevelNo);
                prsm1.setInt(10, rs.getInt(4));
                prsm1.setInt(11, rs.getInt(3));
                prsm1.setInt(12, rs.getInt(5));

                prsm1.executeUpdate();

                if (rs.getString(2).equals("Y")) 
                {
                    // Childs are BOM as well
                	if (rs.getInt(5) != 0) 
                	{
               			explodeBOM(getBOMForProduct(rs.getInt(1), rs.getInt(5)));
               		} 
               		else 
               		{
               			explodeBOM(getBOMForProduct(rs.getInt(1)));
                	}

                    LevelNo -= 1;
                }
            }
        } 
        catch (SQLException e) 
		{
        	log.log(Level.SEVERE,"explodeBOM " , e);
            return -1;
        } 
        finally 
		{
            try 
			{
                if (rs != null) 
                {
                    rs.close();
                }

                if (prsm != null) 
                {
                    prsm.close();
                }

                if (prsm1 != null) 
                {
                    prsm1.close();
                }
            } 
            catch (SQLException e1) 
			{
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        return 0;
    }*/
}
