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
//package org.compiere.mfg.model;
package org.eevolution.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.logging.*;

import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.grid.tree.*;

/**
 *  Order Line Model.
 * 	<code>
 * 			MOrderLine ol = new MOrderLine(m_order);
			ol.setM_Product_ID(wbl.getM_Product_ID());
			ol.setQtyOrdered(wbl.getQuantity());
			ol.setPrice();
			ol.setPriceActual(wbl.getPrice());
			ol.setTax();
			ol.save();

 *	</code>
 *  @author Jorg Janke
 *  @version $Id: MOrderLine.java,v 1.22 2004/03/22 07:15:03 jjanke Exp $
 */
public class MMPCProductBOMLine extends X_MPC_Product_BOMLine
{
    
    
        /*private Vector dataBOM = new Vector();
        private Vector layout = new Vector();
        private Vector columnNames;
        private static JTree		 	m_tree;*/
        //static private int level  = 0;
        //static private int lowlevel = 0;
        //static private int comp = 0;
        //static private int parent = 0 ;
        static private int AD_Client_ID = 0;
        //static private int m_M_Product_ID = 0;
        static Hashtable tableproduct = new Hashtable();
        //static DefaultMutableTreeNode bom = new DefaultMutableTreeNode(Msg.translate(Env.getCtx(), "BOM"));
	/**
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  C_OrderLine_ID  order line to load
	 */
	public MMPCProductBOMLine(Properties ctx, int MPC_Product_BOMLine,String trxName)
	{
		super (ctx, MPC_Product_BOMLine,trxName);
	}	//	MOrderLine
        

	/**
	 *  Parent Constructor.
	 		ol.setM_Product_ID(wbl.getM_Product_ID());
			ol.setQtyOrdered(wbl.getQuantity());
			ol.setPrice();
			ol.setPriceActual(wbl.getPrice());
			ol.setTax();
			ol.save();
	 *  @param  order parent order
	 */
	public MMPCProductBOMLine(MMPCProductBOM bom)
	{
		super (bom.getCtx(), 0,null);
		if (bom.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
		setMPC_Product_BOM_ID (bom.getMPC_Product_BOM_ID());	//	parent
		//setMMPCProductBOM(bom);
		//
		//setC_Tax_ID (0);
		//setLine (0);
		//setC_UOM_ID (0);
		//
		//setIsDescription(false);
		//
		//setPriceList (Env.ZERO);
		//setPriceActual (Env.ZERO);
		//setPriceLimit (Env.ZERO);
		//setLineNetAmt (Env.ZERO);
		//
		//setQtyOrdered (Env.ZERO);
		//setQtyDelivered (Env.ZERO);
		//setQtyReserved (Env.ZERO);
		//setQtyInvoiced (Env.ZERO);
		//
		//setFreightAmt (Env.ZERO);
		//setChargeAmt (Env.ZERO);
	}	//	MOrderLine

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MMPCProductBOMLine(Properties ctx, ResultSet rs,String trxName)
	{
		super (ctx, rs,trxName);
	}	//	MOrderLine

	

	/**
	 * 	Set Defaults from Order.
	 * 	Does not set Parent !!
	 * 	@param order order
	 */
	public void setMMPCProductBOM (MMPCProductBOM bom)
	{
		setClientOrg(bom);
		//setC_BPartner_ID(order.getC_BPartner_ID());
		//setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());
		//setM_Warehouse_ID(order.getM_Warehouse_ID());
		//setDateOrdered(order.getDateOrdered());
		//setDatePromised(order.getDatePromised());
		//m_M_PriceList_ID = order.getM_PriceList_ID();
		//m_IsSOTrx = order.isSOTrx();
		//setC_Currency_ID(order.getC_Currency_ID());
	}	//	setOrder

	
	/**
	 * 	Set Defaults if not set
	 */
	private void setDefaults()
	{
		/*
                //	Get Defaults from Parent
		if (getC_BPartner_ID() == 0 || getC_BPartner_Location_ID() == 0
			|| getM_Warehouse_ID() == 0)
		{
			MOrder o = new MOrder (getCtx(), getC_Order_ID());
			setOrder (o);
		}

		//	Set Price
		if (!m_priceSet && Env.ZERO.compareTo(getPriceActual()) == 0)
			setPrice();

		//	Set Tax
		if (getC_Tax_ID() == 0)
			setTax();

		//	Get Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM C_OrderLine WHERE C_Order_ID=?";
			int ii = DB.getSQLValue (sql, getC_Order_ID());
			setLine (ii);
		}
		//	UOM
		if (getC_UOM_ID() == 0)
			setC_UOM_ID (Env.getContextAsInt(getCtx(), "#C_UOM_ID"));
		if (getC_UOM_ID() == 0)
		{
			int C_UOM_ID = MUOM.getDefault_UOM_ID(getCtx());
			if (C_UOM_ID > 0)
				setC_UOM_ID (C_UOM_ID);
		}

		//	Calculations
		setLineNetAmt(getPriceActual().multiply(getQtyOrdered()));
		setDiscount();
                 */
	}	//	setDefaults

        
		
	/**
	 * 	Save
	 * 	@return true if saved
	 */
	public boolean save ()
	{
		log.fine( "MProduct_BOMLine.save");
		setDefaults();
		return super.save ();
	}	//	save

	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MMPCProductBOMLine[")
			.append(get_ID())
			.append ("]");
		return sb.toString ();
	}
       
        

	public String getDescriptionText()
	{
		return super.getDescription();
	}	//	getDescriptionText
        
        
    public  static int getlowLevel(int M_Product_ID)
	{
                //m_M_Product_ID=M_Product_ID;
                AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
                tableproduct.clear();
                DefaultMutableTreeNode ibom = iparent(M_Product_ID,0);
                return ibom.getDepth();
	}	//	getDescriptionText
        


           
        
           // Explotion
           private static DefaultMutableTreeNode  parent(int M_Product_ID, int MPC_Product_BOM_ID)
           { 
         
                    DefaultMutableTreeNode parent = new DefaultMutableTreeNode(Integer.toString(M_Product_ID) +"|"+ Integer.toString(MPC_Product_BOM_ID));
                    
                    String sql =  new String("SELECT pbom.MPC_Product_BOM_ID FROM  MPC_Product_BOM pbom WHERE pbom.IsActive = 'Y'  AND  pbom.AD_Client_ID= ? AND pbom.M_Product_ID = ? ");
                    
                    
                    PreparedStatement pstmt = null;
                    try
                    {
                    	pstmt = DB.prepareStatement (sql);
                    	pstmt.setInt(1,  Env.getAD_Client_ID(Env.getCtx()));
                        pstmt.setInt(2,  M_Product_ID);
                        ResultSet rs = pstmt.executeQuery ();                        
                        while (rs.next())
                        {
                            
                                DefaultMutableTreeNode bom = component(rs.getInt(1), M_Product_ID , parent);
                                if (bom != null)
                                {
                                    parent.add(bom);
                                }   
                            
                            //parent.add(component(rs.getInt(1), M_Product_ID , parent));

                        }                                                
                        
                        rs.close();
                        pstmt.close();
                        
                        return parent;

                    }
                    catch (Exception e)
                    {
                        //log.error ("doIt - " + sql, e);
                        System.out.println("doIt - " + sql + e);
                    }
                     return parent;
           }
           
           

           //Explotion 
           private static DefaultMutableTreeNode  component(int M_Product_BOM_ID, int M_Product_ID , DefaultMutableTreeNode bom)
           { 
    
                    
                    //System.out.println("Level" + bom.getLevel());                               
                    
                    String sql =  new String("SELECT pboml.M_Product_ID , pbom.Value , pboml.MPC_Product_BOMLine_ID , pbom.MPC_Product_BOM_ID FROM  MPC_Product_BOM pbom INNER JOIN MPC_Product_BOMLine pboml ON (pbom.MPC_Product_BOM_ID = pboml.MPC_Product_BOM_ID) WHERE pbom.IsActive= 'Y' AND pboml.IsActive= 'Y' AND pbom.AD_Client_ID= ? AND pbom.MPC_Product_BOM_ID = ? ");
                    
                    //System.out.println("SQL Component:" + sql +  "Parametro:" + M_Product_BOM_ID);
                    PreparedStatement pstmt = null;
                    try
                    {
                    	pstmt = DB.prepareStatement (sql);
                        //pstmt.setString(1,  Value);
                    	pstmt.setInt(1,  Env.getAD_Client_ID(Env.getCtx()));
                        pstmt.setInt(2,  M_Product_BOM_ID);                        
                        ResultSet rs = pstmt.executeQuery ();                        
                        while (rs.next())
                        {
                            
                            if (M_Product_ID != rs.getInt(1))    
                            { 
                                //System.out.println("Component:"+ rs.getInt(1));                                                           
                                bom.add(parent(rs.getInt(1), rs.getInt(4)));
                                //System.out.println("Componet"+ bom.toString());

                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null,"Componet will be deactivated for BOM & Formula:" + rs.getString(2) + "(" + rs.getString(3) + ")", "Error Cycle BOM" , JOptionPane.ERROR_MESSAGE);
                                MMPCProductBOMLine MPC_Product_BOMLine = new MMPCProductBOMLine(Env.getCtx(), rs.getInt(3),null);
                                MPC_Product_BOMLine.setIsActive(false);
                                MPC_Product_BOMLine.save();
                            }    

                        }
                        
                        
                        
                       // System.out.println("Renglones Componet" + rs.getRow());
                        if (rs.getRow() == 0)
                        {
                            DefaultMutableTreeNode parent = new DefaultMutableTreeNode(Integer.toString(M_Product_ID) + "|0");
                            bom.add(parent);
                            return bom;
                        }
                        
                        rs.close();
                        pstmt.close();
                        
                        //return bom;            

                    }
                    catch (Exception e)
                    {
                        //log.error ("doIt - " + sql, e);
                        System.out.println("doIt - " + sql + e);
                    }
                    
                    return null;
           }
           
           
           
           // imp
           private static DefaultMutableTreeNode  iparent(int M_Product_ID , int MPC_Product_BOM_ID)
           { 
         
                    DefaultMutableTreeNode parent = new DefaultMutableTreeNode(Integer.toString(M_Product_ID) +"|"+ Integer.toString(MPC_Product_BOM_ID));
                    
                    String sql =  new String("SELECT pboml.MPC_Product_BOMLine_ID FROM  MPC_Product_BOMLine pboml WHERE pboml.IsActive= 'Y' AND pboml.AD_Client_ID = ? AND pboml.M_Product_ID = ? ");
                    //System.out.println("Padre" +  M_Product_ID);
                    
                    PreparedStatement pstmt = null;
                    try
                    {
                    	pstmt = DB.prepareStatement (sql);
                        pstmt.setInt(1, AD_Client_ID);
                        pstmt.setInt(2, M_Product_ID);
                        ResultSet rs = pstmt.executeQuery ();                        
                        
                        while (rs.next())
                        {
                            
                                DefaultMutableTreeNode bom = icomponent(rs.getInt(1), M_Product_ID , parent);
                                if (bom != null)
                                {
                                    parent.add(bom);
                                }                               
                                                            

                        }
                        
                        
                        rs.close();
                        pstmt.close();
                        
                        return parent;

                    }
                    catch (Exception e)
                    {
                        //log.error ("doIt - " + sql, e);
                        System.out.println("doIt - " + sql + e);
                    }
                     return parent;
           }    
           
           //Imp
           private static DefaultMutableTreeNode  icomponent(int MPC_Product_BOMLine_ID, int M_Product_ID , DefaultMutableTreeNode bom)
           { 
    
                    
                    //System.out.println("Level" + bom.getLevel());                               
                    
                    String sql =  new String("SELECT pbom.M_Product_ID , pbom.Value , pbom.MPC_Product_BOM_ID FROM  MPC_Product_BOMLine pboml INNER JOIN MPC_Product_BOM pbom ON (pbom.MPC_Product_BOM_ID = pboml.MPC_Product_BOM_ID) WHERE pbom.IsActive= 'Y' AND pboml.IsActive= 'Y' AND pboml.AD_Client_ID =? AND pboml.MPC_Product_BOMLine_ID = ? ");
                    
                    
                    PreparedStatement pstmt = null;
                    try
                    {
                    	pstmt = DB.prepareStatement (sql);
                        pstmt.setInt(1, AD_Client_ID);
                        pstmt.setInt(2,  MPC_Product_BOMLine_ID);
                        ResultSet rs = pstmt.executeQuery ();                        
                        while (rs.next())
                        {
                            //System.out.println("Componet:" + rs.getInt(1));
                            if (M_Product_ID != rs.getInt(1))    
                            { 
                                if(!tableproduct(rs.getInt(1),rs.getInt(3)))
                                bom.add(iparent(rs.getInt(1),rs.getInt(3)));
                                else
                                System.out.println("Cycle BOM & Formula:" + rs.getString(2) + "(" + rs.getString(3) + ")");    
                                //JOptionPane.showMessageDialog(null,"Cycle BOM & Formula:" + rs.getString(2) + "(" + rs.getString(3) + ")", "Error Cycle BOM" , JOptionPane.ERROR_MESSAGE);
                                    
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null,"Componet will be deactivated for BOM & Formula:" + rs.getString(2) + "(" + rs.getString(3) + ")", "Error Cycle BOM" , JOptionPane.ERROR_MESSAGE);
                                MMPCProductBOMLine MPC_Product_BOMLine = new MMPCProductBOMLine(Env.getCtx(), rs.getInt(3),null);
                                MPC_Product_BOMLine.setIsActive(false);
                                MPC_Product_BOMLine.save();
                            }    

                        }
                        rs.close();
                        pstmt.close();

                    }
                    catch (Exception e)
                    {
                        //log.error ("doIt - " + sql, e);
                        System.out.println("doIt - " + sql + e);
                    }
                    
                    return null;
           }
           
           
           private static boolean tableproduct(int M_Product_ID, int MPC_Product_BOM_ID)
           {
               Integer p = new Integer(M_Product_ID);
               Integer bom = new Integer(MPC_Product_BOM_ID);
               //String key = p.toString() ; //+ bom.toString();
               
               if (!tableproduct.containsKey(p))
               {    
               tableproduct.put(p , bom);
               return false;
               }
               else return true;
                 
           }

}	//	MOrderLine
