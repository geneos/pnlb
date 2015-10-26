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

import org.compiere.util.*;
import org.compiere.model.*;

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
public class MMPCOrderBOMLine extends X_MPC_Order_BOMLine
{
	/**
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  C_OrderLine_ID  order line to load
	 */
	public MMPCOrderBOMLine(Properties ctx, int MPC_Order_BOMLine_ID,String trxName)
	{
		super (ctx, MPC_Order_BOMLine_ID,trxName);  
		if (MPC_Order_BOMLine_ID == 0)
		{
                setQtyDelivered(Env.ZERO);
                setQtyPost(Env.ZERO);
                setQtyReject(Env.ZERO);
                setQtyRequiered(Env.ZERO);
                setQtyReserved(Env.ZERO);
                setQtyScrap(Env.ZERO);
		}	
	}	//	MPC_Order_BOMLine_ID
        

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MMPCOrderBOMLine(Properties ctx, ResultSet rs,String trxName)
	{
		super (ctx, rs,trxName);
	}	//	MOrderLine

	private MMPCOrder m_parent = null;

	/**
	 * 	Set Defaults from Order.
	 * 	Does not set Parent !!
	 * 	@param order order
	 */
	public void setMMPCOrderBOM (MMPCOrderBOM bom)
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
        
        
         private MProduct 	m_product = null;
         private int			m_M_Locator_ID = 0;
         
    /**************************************************************************
     * 	after Save
     *	@param newRecord new
     *	@return save
     */
    protected boolean afterSave(boolean newRecord, boolean success) {
        
		if (!newRecord)
                {
                    
			return success;
                }
                
              
    		//Qty Ordered to Phantom                
    		BigDecimal QtyOrdered = getQtyRequiered();
                System.out.println(" Padre Product" +  getM_Product_ID() + " getQtyBatch" + getQtyBatch() + " getQtyRequiered"  + getQtyRequiered() + " QtyScrap" + getQtyScrap());
    		//Phantom
    		if(getComponentType().equals(MMPCProductBOMLine.COMPONENTTYPE_Phantom))
    		{
    			 
    			 int MPC_Product_BOM_ID = MMPCProductBOM.getBOMSearchKey(getM_Product_ID());
    			 if (MPC_Product_BOM_ID==0)
    			 	return true;
    			 
    			 MMPCProductBOM bom = new MMPCProductBOM(getCtx(),MPC_Product_BOM_ID,null);
    			 if (bom!= null)
    			 {
    			 	MMPCProductBOMLine[] MPC_Product_BOMline = bom.getLines();
    			 	
    			 	if (MPC_Product_BOMline == null)
    			 		return true;
    			 	for(int i = 0 ; i < MPC_Product_BOMline.length ; i++ )
    			 	{
    			 		MMPCOrderBOMLine MPC_Order_BOMLine = new MMPCOrderBOMLine(getCtx(),0,null);
    			 		MProduct product = new MProduct(getCtx(),MPC_Product_BOMline[i].getM_Product_ID(),null);

                                        MPC_Order_BOMLine.setDescription(MPC_Product_BOMline[i].getDescription());
                                        MPC_Order_BOMLine.setHelp(MPC_Product_BOMline[i].getHelp());
                                        MPC_Order_BOMLine.setM_ChangeNotice_ID(MPC_Product_BOMline[i].getM_ChangeNotice_ID());
                                        MPC_Order_BOMLine.setAssay(MPC_Product_BOMline[i].getAssay());
                                        MPC_Order_BOMLine.setQtyBatch(MPC_Product_BOMline[i].getQtyBatch());
                                        MPC_Order_BOMLine.setQtyBOM(MPC_Product_BOMline[i].getQtyBOM());
                                        MPC_Order_BOMLine.setIsQtyPercentage(MPC_Product_BOMline[i].isQtyPercentage());
                                        MPC_Order_BOMLine.setComponentType(MPC_Product_BOMline[i].getComponentType());          
                                        MPC_Order_BOMLine.setC_UOM_ID(MPC_Product_BOMline[i].getC_UOM_ID());
                                        MPC_Order_BOMLine.setForecast(MPC_Product_BOMline[i].getForecast());
                                        MPC_Order_BOMLine.setIsCritical(MPC_Product_BOMline[i].isCritical());
                                        MPC_Order_BOMLine.setIssueMethod(MPC_Product_BOMline[i].getIssueMethod());    		                                                  
                                        MPC_Order_BOMLine.setLine(MMPCOrder.getLines(getMPC_Order_ID(),get_TrxName()).length+10);
                                        MPC_Order_BOMLine.setLeadTimeOffset(MPC_Product_BOMline[i].getLeadTimeOffset());
                                        MPC_Order_BOMLine.setM_AttributeSetInstance_ID(MPC_Product_BOMline[i].getM_AttributeSetInstance_ID());
                                        MPC_Order_BOMLine.setMPC_Order_BOM_ID(getMPC_Order_BOM_ID());
                                        MPC_Order_BOMLine.setMPC_Order_ID(getMPC_Order_ID());
                                        MPC_Order_BOMLine.setM_Product_ID(MPC_Product_BOMline[i].getM_Product_ID());
                                        MPC_Order_BOMLine.setScrap(MPC_Product_BOMline[i].getScrap());
                                        MPC_Order_BOMLine.setValidFrom(MPC_Product_BOMline[i].getValidFrom());
                                        MPC_Order_BOMLine.setValidTo(MPC_Product_BOMline[i].getValidTo());
                                        MPC_Order_BOMLine.setM_Warehouse_ID(getM_Warehouse_ID());
    		            
                                        if (MPC_Order_BOMLine.isQtyPercentage()) 
                                        {
                                            BigDecimal qty = MPC_Order_BOMLine.getQtyBatch().multiply(QtyOrdered);                
                                            System.out.println("product:"+product.getName() +" Qty:"+qty + " QtyOrdered:"+ QtyOrdered + " MPC_Order_BOMLine.getQtyBatch():" + MPC_Order_BOMLine.getQtyBatch());
                                            if(MPC_Order_BOMLine.getComponentType().equals(MPC_Order_BOMLine.COMPONENTTYPE_Packing))
                                            MPC_Order_BOMLine.setQtyRequiered(qty.divide(new BigDecimal(100),8,qty.ROUND_UP));
                                            else if (MPC_Order_BOMLine.getComponentType().equals(MPC_Order_BOMLine.COMPONENTTYPE_Component) || MPC_Order_BOMLine.getComponentType().equals(MPC_Order_BOMLine.COMPONENTTYPE_Phantom))
                                            MPC_Order_BOMLine.setQtyRequiered(qty.divide(new BigDecimal(100),8,qty.ROUND_UP));
                                            else if (MPC_Order_BOMLine.getComponentType().equals(MPC_Order_BOMLine.COMPONENTTYPE_Tools))
                                            MPC_Order_BOMLine.setQtyRequiered(MPC_Order_BOMLine.getQtyBOM());
                                        }
                                        else 
                                        {            	
                                             //System.out.println("product: "+product.getName() + " QtyOrdered:"+ QtyOrdered + " MPC_Order_BOMLine.getQtyBOM():" + MPC_Order_BOMLine.getQtyBOM());
                                            if (MPC_Order_BOMLine.getComponentType().equals(MPC_Order_BOMLine.COMPONENTTYPE_Component) || MPC_Order_BOMLine.getComponentType().equals(MPC_Order_BOMLine.COMPONENTTYPE_Phantom))                    
                                                    MPC_Order_BOMLine.setQtyRequiered(MPC_Order_BOMLine.getQtyBOM().multiply(QtyOrdered));
                                            else if (MPC_Order_BOMLine.getComponentType().equals(MPC_Order_BOMLine.COMPONENTTYPE_Packing))                    
                                                    MPC_Order_BOMLine.setQtyRequiered(MPC_Order_BOMLine.getQtyBOM().multiply(QtyOrdered));
                                            else if (MPC_Order_BOMLine.getComponentType().equals(MPC_Order_BOMLine.COMPONENTTYPE_Tools))
                                                    MPC_Order_BOMLine.setQtyRequiered(MPC_Order_BOMLine.getQtyBOM());
                                        }                                                    

                                        // Set Scrap of Component
                                        BigDecimal Scrap = MPC_Order_BOMLine.getScrap().divide(new BigDecimal(100),8,BigDecimal.ROUND_UP);

                                        if (!Scrap.equals(Env.ZERO))
                                        {	
                                            Scrap = Scrap.divide(new BigDecimal(100),8,BigDecimal.ROUND_UP); 
                                            //System.out.println("Scrap"+Scrap);
                                            MPC_Order_BOMLine.setQtyRequiered(MPC_Order_BOMLine.getQtyRequiered().divide( Env.ONE.subtract(Scrap) , 8 ,BigDecimal.ROUND_HALF_UP ));
                                        }
    		            
//                        setQtyRequiered(Env.ZERO);	
    		            MPC_Order_BOMLine.save(get_TrxName());
    		            
    			 	}
    			 }
    			 
    		}// end Phantom    	
    		return true;
    		
    	}
	
	

	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPCProductBOMLine[")
			.append(get_ID())
			.append ("]");
		return sb.toString ();
	}

        
        
	
       
        

	/**
	 * 	Set UOM.
	 * 	make access public
	 *	@param C_UOM_ID uom
	 */
	public void setC_UOM_ID (int C_UOM_ID)
	{
		super.setC_UOM_ID (C_UOM_ID);
	}	//	setC_UOM_ID


        
       /**
	 * 	Get Product
	 *	@return product or null
	 */
	public MProduct getProduct()
	{
		if (m_product == null && getM_Product_ID() != 0)
			m_product = new MProduct (getCtx(), getM_Product_ID() ,get_TrxName());
		return m_product;
	}
        
        	/**
	 * 	Save Temp M_Locator_ID
	 *	@param M_Locator_ID id
	 */
	public void setTempM_Locator_ID (int M_Locator_ID)
	{
		m_M_Locator_ID = M_Locator_ID;
	}	//	setTempM_Locator_ID



	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MMPCOrder getParent()
	{
	if (m_parent == null)
		m_parent = new MMPCOrder(getCtx(), getMPC_Order_ID(), get_TrxName());
	return m_parent;
	}	//	getParent

    BigDecimal explodeQty(BigDecimal qtyOrdered) {
        BigDecimal requiredQty = BigDecimal.ZERO;
        if (isQtyPercentage())
            {
                BigDecimal qty =  getQtyBatch().multiply(qtyOrdered);
                if( getComponentType().equals(COMPONENTTYPE_Packing))
                    requiredQty = qty.divide(new BigDecimal(100),8,qty.ROUND_UP);
                if (getComponentType().equals(COMPONENTTYPE_Component) || getComponentType().equals(COMPONENTTYPE_Phantom))
                    requiredQty = qty.divide(new BigDecimal(100),8,qty.ROUND_UP);
                else if (getComponentType().equals(COMPONENTTYPE_Tools))
                   requiredQty = getQtyBOM();

            }
            else
            {
                    if (getComponentType().equals(COMPONENTTYPE_Component) || getComponentType().equals(COMPONENTTYPE_Phantom))
                            requiredQty = getQtyBOM().multiply(qtyOrdered);
                    else if (getComponentType().equals(COMPONENTTYPE_Packing))
                            requiredQty = getQtyBOM().multiply(qtyOrdered);
                else if (getComponentType().equals(COMPONENTTYPE_Tools))
                    requiredQty = getQtyBOM();
            }
        return requiredQty;
    }

}
