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

import java.math.*;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;


import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
import org.eevolution.model.*;
import org.eevolution.model.MMPCProfileBOM;

/**
 *  Copy Order Lines
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromOrder.java,v 1.4 2004/05/07 05:52:14 jjanke Exp $
 */
public class QualityReporting extends SvrProcess
{
	/**	The Order				*/
        
        private String		p_DocType = "";
        private boolean         p_NextOperation = false;
        private boolean         p_CompletePrevious = false;
        private String		p_docAction = DocAction.ACTION_Complete;
	private int		p_MPC_ProfileBOM_ID = 0;
        private int		p_PInstance_ID = 0;
        private int		p_Record_ID = 0;
        private MMPCCostCollector ccoll = null;
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
                        else if (name.equals("IsComplete"))
				p_DocType = (String)para[i].getParameter();
			else if (name.equals("NextOperation"))
				p_NextOperation = "Y".equals(para[i].getParameter()); 
                        else if (name.equals("CompletePrevious"))
				p_CompletePrevious = "Y".equals(para[i].getParameter());
                        else if (name.equals("Processing"))
				p_docAction = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
                p_PInstance_ID = getAD_PInstance_ID();
                p_Record_ID=getRecord_ID();
                ccoll = new MMPCCostCollector(Env.getCtx(),p_Record_ID, get_TrxName());
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		int MPC_CostCollector_ID = getRecord_ID();
		
		if (MPC_CostCollector_ID == 0)
			throw new IllegalArgumentException("Not saved Activity Report");
		
                
                ArrayList list = new ArrayList();
                int count =0;
                
                  try
                                             {
                                                    StringBuffer sql=new StringBuffer("SELECT MPC_Order_Node_ID FROM MPC_Order_Node WHERE IsActive='Y' AND  MPC_Order_ID=? Order By Value");
                                                     PreparedStatement pstmt = DB.prepareStatement(sql.toString(),null);
                                                    // pstmt.setInt(1, AD_Client_ID);
                                                     pstmt.setInt(1, ccoll.getMPC_Order_ID());
                                                    //pstmt.setInt(2, m_M_PriceList_ID);
                                                    ResultSet rs = pstmt.executeQuery();
                                                    //while (!m_calculated && rsplv.next())
                                                    while (rs.next())
                                                    {

                                                        Integer nodeid = new Integer(rs.getInt(1));
                                                        list.add(count,nodeid.toString());

                                                        count++;

                                                    }
                                                    rs.close();
                                                    pstmt.close();
                                              }
                                                catch (SQLException enode)
                                                {
                                                }
                
                                    // Complete previous nodes
                                    
                                    boolean ultimonodo = false;  
                                    if (p_CompletePrevious)
                                    {
                                        for (int v =0 ; v < list.size(); v++)
                                        {
                                            if (list.get(v).equals(new Integer(ccoll.getMPC_Order_Node_ID()).toString()))
                                            {
                                                //String nextnode = new String(list.get(v+1));
                                                 try
                                                 {
                                                        StringBuffer sqlnn=new StringBuffer("SELECT MPC_Order_Node_ID FROM MPC_Order_NodeNext WHERE IsActive='Y' AND  MPC_Order_ID=? and MPC_Order_Node_ID=?");
                                                         PreparedStatement pstmtnn = DB.prepareStatement(sqlnn.toString(),null);
                                                        // pstmt.setInt(1, AD_Client_ID);
                                                         pstmtnn.setInt(1, ccoll.getMPC_Order_ID());
                                                        pstmtnn.setInt(2, ccoll.getMPC_Order_Node_ID());
                                                        ResultSet rsnn = pstmtnn.executeQuery();
                                                        //while (!m_calculated && rsplv.next())
                                                        System.out.println("***** SQL ultm nodo " +sqlnn.toString());
                                                        if (rsnn.next())
                                                        {

                                                           ultimonodo=false;



                                                        }
                                                        else
                                                        {
                                                            ultimonodo=true;
                                                        }
                                                        rsnn.close();
                                                        pstmtnn.close();
                                                  }
                                                    catch (SQLException enodenn)
                                                    {
                                                    }
                                                if (!ultimonodo)
                                                {
                                                    System.out.println("***** No ES EL ULTIMO NODO");
                                                }
                                                else
                                                {
                                                        try
                                                 {
                                                        StringBuffer sql1=new StringBuffer("SELECT OperationStatus,MPC_Order_Node_ID,DurationRequiered FROM MPC_Order_Node WHERE IsActive='Y' AND  MPC_Order_ID=? and MPC_Order_Node_ID!=?");
                                                         PreparedStatement pstmt1 = DB.prepareStatement(sql1.toString(),null);
                                                        // pstmt.setInt(1, AD_Client_ID);
                                                         pstmt1.setInt(1, ccoll.getMPC_Order_ID());
                                                        pstmt1.setInt(2, ccoll.getMPC_Order_Node_ID());
                                                        ResultSet rs1 = pstmt1.executeQuery();
                                                        //while (!m_calculated && rsplv.next())
                                                        System.out.println("***** SQL1 " +sql1 + " variable " +ccoll.getMPC_Order_ID());
                                                        while (rs1.next())
                                                        {
                                                            System.out.println("***** Nodo " +rs1.getInt(2) +" status " +rs1.getString(1));
    //                                                        if(!rs1.getString(1).equals("CL"))
    //                                                        {
    //                                                            
    //                                                            MMPCOrderNode onodenext =new MMPCOrderNode(Env.getCtx(),rs1.getInt(2),get_TrxName());
    //                                                        onodenext.setDocStatus("CL");
    //                                                        onodenext.save();
    //                                                        }
                                                             createnewnode(rs1.getInt(2),rs1.getBigDecimal(3));  

                                                        }
                                                        rs1.close();
                                                        pstmt1.close();


                                                  }
                                                    catch (SQLException enode)
                                                    {
                                                    }
                                                }
                                            }

                                        } 
                                    } // complete previous nodes
                                    
                                                 // crear orden de compra al completar
                                    int p_MPC_Order_Node_ID=0;
                                    BigDecimal m_MovementQty=Env.ZERO;

                                      try
                                    {
                                        StringBuffer sql=new StringBuffer("SELECT MPC_Order_Node_ID,MovementQty FROM MPC_Cost_Collector WHERE IsActive='Y' AND AD_Client_ID=? and MPC_Cost_Collector_ID=? ");
                                         PreparedStatement pstmt = DB.prepareStatement(sql.toString(),null);
                                         pstmt.setInt(1, getAD_Client_ID());
                                         pstmt.setInt(2, ccoll.getMPC_Cost_Collector_ID());
                                        //pstmt.setInt(2, m_M_PriceList_ID);
                                        ResultSet rs = pstmt.executeQuery();
                                        //while (!m_calculated && rsplv.next())
                                        while (rs.next())
                                        {
                                            p_MPC_Order_Node_ID= rs.getInt(1);
                                            m_MovementQty=rs.getBigDecimal(2);
                                        }
                                        rs.close();
                                        pstmt.close();
                                    }
                                    catch (SQLException e)
                                    {
                                    }


//                StringBuffer borrars = new StringBuffer("Delete From MPC_ProfileBOMLine where MPC_ProfileBOM_ID= "+ To_MPC_ProfileBOM_ID);
//                DB.executeUpdate(borrars.toString());
                if (p_docAction.equals("CO"))
                    completeit();
                else if(p_docAction.equals("CL"))
                    closeit();
                else if(p_docAction.equals("VO"))                   
                    voidit();
		//
		return "Ok";
	}	//	doIt

        
        	  protected void createnewnode(int node, BigDecimal duration)
          {
	
    	
                   //fjv e-evolution Operation Activity Report begin
                                try
                                {
                                         String sqlar="SELECT MPC_Cost_Collector_ID FROM MPC_Cost_Collector WHERE IsActive='Y' AND  MPC_Order_ID="+ccoll.getMPC_Order_ID() +"  and MPC_Order_Node_ID="+node;
                                                             PreparedStatement pstmtar = DB.prepareStatement(sqlar,null);
                                                            // pstmt.setInt(1, AD_Client_ID);
                                                           //  pstmtar.setInt(1, getMPC_Order_ID());
                                                            //pstmtar.setInt(2, rs1.getInt(2));
                                                              System.out.println("***** SQLar " +sqlar + " variables " +ccoll.getMPC_Order_ID() +" nodo "+node);
                                                            ResultSet rsar = pstmtar.executeQuery();
                                                            
                                                            //while (!m_calculated && rsplv.next())
                                                            if(rsar.next())
                                                            {
                                                                System.out.println("***** NODO Ya Existe");
                                                            }
                                                            else
                                                            {
                                                                 System.out.println("***** ENTRA AL eLSE ");
                                                                MMPCCostCollector costnew = new MMPCCostCollector(Env.getCtx(),0,get_TrxName());
                                                                costnew.setMPC_Order_ID(ccoll.getMPC_Order_ID());
                                                                costnew.setC_DocTypeTarget_ID(ccoll.getC_DocTypeTarget_ID());
                                                                costnew.setC_DocType_ID(ccoll.getC_DocType_ID());
                                                                costnew.setS_Resource_ID(ccoll.getS_Resource_ID());
                                                                costnew.setM_Warehouse_ID(ccoll.getM_Warehouse_ID());
                                                                costnew.setM_Locator_ID(ccoll.getM_Locator_ID());
                                                                costnew.setM_Product_ID(ccoll.getM_Product_ID());
                                                                costnew.setM_AttributeSetInstance_ID(ccoll.getM_AttributeSetInstance_ID());
                                                                costnew.setMPC_Order_Workflow_ID(ccoll.getMPC_Order_Workflow_ID());
                                                                costnew.setAD_User_ID(ccoll.getAD_User_ID());
                                                                costnew.setMovementDate(ccoll.getMovementDate());
                                                                costnew.setDateAcct(ccoll.getDateAcct());
                                                                costnew.setMPC_Order_Node_ID(node);
                                                                costnew.setMovementQty(ccoll.getMovementQty());
                                                                costnew.setDurationReal(duration);
                                                      // temp         costnew.setDurationUnit(getDurationUnit());
                                                                costnew.setMovementType(ccoll.getMovementType());
                                                                costnew.save();
                                                                //    costnew.completeIt();
                                                                
                                                            }
                                                            
                                                            rsar.close();
                                                            pstmtar.close();
                                                            
                            }
                            catch (SQLException exnode)
                            {
                            }
                          //completenew(getMPC_Order_ID(),node);
                             //fjv e-evolution Operation Activity Report end
       
    	
        } // migration end
	
       
           protected void closenew(int order, int node)
          {
                     try
                                {
                                         String sqlcom="SELECT MPC_Cost_Collector_ID FROM MPC_Cost_Collector WHERE IsActive='Y' AND  MPC_Order_ID="+order;
                                                             PreparedStatement pstmtcom = DB.prepareStatement(sqlcom,null);
                                                            // pstmt.setInt(1, AD_Client_ID);
                                                           //  pstmtar.setInt(1, getMPC_Order_ID());
                                                            //pstmtar.setInt(2, rs1.getInt(2));
                                                              System.out.println("***** SQLar " +sqlcom + " variables " +order +" nodo "+node);
                                                            ResultSet rscom = pstmtcom.executeQuery();
                                                            while(rscom.next())
                                                            {
                                                                MDocType doc = new MDocType(Env.getCtx(),ccoll.getC_DocType_ID(),get_TrxName());
                                                                String doct ="";
                                                                doct=doc.getDocBaseType();
                                                                if(doct.equals("MOA"))
                                                                {
                                                                    MMPCCostCollector costcoll = new MMPCCostCollector(Env.getCtx(),rscom.getInt(1),get_TrxName());
                                                               //     costcoll.setDocnStatus("CL");
                                                                    costcoll.setDocAction(ccoll.DOCACTION_None);
                                                                    costcoll.save();
                                                                }
                                                            }
                                }
                     catch (SQLException excom)
                     {
                     }
            }
           
            protected void completenew(int order, int node)
          {
                     try
                                {
                                         String sqlcom="SELECT MPC_Cost_Collector_ID,OperationStatus FROM MPC_Cost_Collector WHERE IsActive='Y' AND  MPC_Order_ID="+order;
                                                             PreparedStatement pstmtcom = DB.prepareStatement(sqlcom,null);
                                                            // pstmt.setInt(1, AD_Client_ID);
                                                           //  pstmtar.setInt(1, getMPC_Order_ID());
                                                            //pstmtar.setInt(2, rs1.getInt(2));
                                                              System.out.println("***** SQLar " +sqlcom + " variables " +order +" nodo "+node);
                                                            ResultSet rscom = pstmtcom.executeQuery();
                                                            while(rscom.next())
                                                            {
                                                                        MDocType doc = new MDocType(Env.getCtx(),ccoll.getC_DocType_ID(),get_TrxName());
                                                                String doct ="";
                                                                doct=doc.getDocBaseType();
                                                                if(doct.equals("MOA"))
                                                                   {
                                                                        if(!rscom.getString(2).equals("C0") && !rscom.getString(2).equals("CL"))
                                                                        {
                                                                            MMPCCostCollector costcoll = new MMPCCostCollector(Env.getCtx(),rscom.getInt(1),get_TrxName());
                                                                            costcoll.completeIt();
                                                                        }
                                                                    }
                                                            }
                                }
                     catch (SQLException excom)
                     {
                     }
            }
            
             protected void completeit()
          {
                     
                                          MMPCCostCollector costcoll = new MMPCCostCollector(Env.getCtx(),p_Record_ID,get_TrxName());
                                          costcoll.completeIt();
                                                           
            }
                protected void closeit()
          {
                     
                                          MMPCCostCollector costcoll = new MMPCCostCollector(Env.getCtx(),p_Record_ID,get_TrxName());
                                          costcoll.closeIt();
                                                           
            }
                
                   protected void voidit()
          {
                     
                                          MMPCCostCollector costcoll = new MMPCCostCollector(Env.getCtx(),p_Record_ID,get_TrxName());
                                          costcoll.voidIt();
                                                           
            }
}	//	CopyFromOrder
