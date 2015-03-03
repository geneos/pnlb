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

package org.eevolution.process;

import java.util.logging.*;
import java.math.*;
import java.sql.*;
import java.util.*;


import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
//import org.eevolution.model.MMPCCostElement;
import org.eevolution.model.MMPCMRP;
import org.eevolution.model.MMPCProductBOM;
import org.eevolution.model.MMPCProductBOMLine;
//import org.eevolution.model.MMPCProductCosting;
import org.eevolution.model.MMPCProductPlanning;
import org.compiere.util.QueryDB;

/**
 *	Rollup Bill of Material 
 *	
 *  @author Victor Perez, e-Evolution, S.C.
 *  @version $Id: CreateCost.java,v 1.1 2004/06/22 05:24:03 vpj-cd Exp $
 */
public class RollupBillOfMaterialGood extends SvrProcess
{
	/**					*/
       private int		 		   p_AD_Org_ID = 0;
       private int               p_M_Warehouse_ID = 0;
       private int               p_M_Product_ID = 0;
       private int               p_M_CostType_ID = 0;
       private int               p_C_AcctSchema_ID = 0;
       private BigDecimal        totalCost = Env.ZERO;
       private BigDecimal        totalCostIntermedio=Env.ZERO;
      
             
      
       
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
			else if (name.equals("AD_Org_ID"))
                        {    
				p_AD_Org_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
                        }
                        else if (name.equals("M_Warehouse_ID"))
                        {    
				p_M_Warehouse_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
                        }
                        else if (name.equals("M_Product_ID"))
                        {    
				p_M_Product_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
                        }
                      
                        else if (name.equals("M_CostType_ID"))
                        {    
				
                                p_M_CostType_ID = ((BigDecimal)para[i].getParameter()).intValue();
                        }
                        
                        else if (name.equals("C_AcctSchema_ID"))
                        {    
				p_C_AcctSchema_ID = ((BigDecimal)para[i].getParameter()).intValue();
                                
                        } 
                        
                        else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
                
                
                
                
	}	
/**
 * Bision- Nadia, Junio 2008
 * @return
 * @throws java.lang.Exception
 */
        
     protected String doIt() throws Exception                
     {  int AD_Client_ID =getAD_Client_ID(); 

        StringBuffer sql = new StringBuffer ("SELECT p.M_Product_ID FROM M_Product p WHERE p.ProductType = '" + MProduct.PRODUCTTYPE_Item + "' AND");

        if (p_M_Product_ID != 0)
        {    
        sql.append(" p.M_Product_ID = " + p_M_Product_ID + " AND ");
        }               
        sql.append(" p.AD_Client_ID = " + AD_Client_ID);
        sql.append(" ORDER BY p.LowLevel");


        PreparedStatement pstmt = null;
        try
        {       
                pstmt = DB.prepareStatement (sql.toString());                       						
                ResultSet rs = pstmt.executeQuery ();
                while (rs.next())
                {
                   
                   int M_Product_ID = rs.getInt("M_Product_ID");                           
                   org.eevolution.model.MCost[]  pc = org.eevolution.model.MCost.getElements( M_Product_ID , p_C_AcctSchema_ID , p_M_CostType_ID);            
                   if (pc.length!=0){
                        for (int e = 0 ; e < pc.length ; e ++ )
                        {
                            BigDecimal big = Env.ZERO; 
                            MCostElement element = new MCostElement(getCtx(), pc[e].getM_CostElement_ID(),null);
                            // check if element cost is of type Material
                            if (element.getCostElementType().equals(element.COSTELEMENTTYPE_Material))
                            {   
                                int MPC_Product_BOM_ID =  getMPC_Product_BOM_ID(p_AD_Org_ID , M_Product_ID);
                                if (MPC_Product_BOM_ID != 0)
                                {    
                                    //MMPCProductBOM mpc_product_bom= new MMPCProductBOM(getCtx(),MPC_Product_BOM_ID,null);
                                    //MMPCProductBOMLine[] lines= mpc_product_bom.getLines(); 
                                    big = SgetCost(M_Product_ID, p_M_CostType_ID , p_C_AcctSchema_ID);
                                    System.out.println("cost big: "+ big.toString());
                                    //System.out.println("cost material: "+ totalCost.toString());
                                    pc[e].setCurrentCostPrice(big);
                                    pc[e].save(get_TrxName());
                                } 
                                continue;
                            }
                        }
                   }
                   //Si M_Product_ID no tiene M_Costs asociados, creo uno nuevo
                   else{
                      //Recupero el ID de M_CostElement con nombre "Costo Estandar de Material"
                      int M_CostElement_ID = getCostElementIDByName("'Costo Estandar de Material'");
                      //Necesarios para crear el MCost
                      MProduct product = MProduct.get(getCtx(),p_M_Product_ID);
                      MAcctSchema as = MAcctSchema.get(getCtx(),p_C_AcctSchema_ID,get_TrxName());
                      //Creo el MCost
                      MCost mcost2 = MCost.get(product,M_CostElement_ID, as, p_AD_Org_ID, M_CostElement_ID);
                      mcost2.setM_AttributeSetInstance_ID(0);
                      BigDecimal big = Env.ZERO;
                      //Calculo el costo
                      big = SgetCost(M_Product_ID, p_M_CostType_ID , p_C_AcctSchema_ID);
                      mcost2.setCurrentCostPrice(big);
                      //Guardo el registro en la tabla MCost
                      mcost2.save(get_TrxName());
                   }
                }

                rs.close();
                pstmt.close();

        }
        catch (Exception e)
        {
                log.log(Level.SEVERE,"doIt - " + sql, e);
                return null;
        }


        return "ok";
         
     }
     

    /**
     * Método que agrega un MCost
     */  
    public void createMCost(){
        
        //MCost costo = new MCost()
        /*CURRENTCOSTPRICE
        AD_CLIENT_ID viene como parametro          
        AD_ORG_ID viene como parametro             
        M_PRODUCT_ID viene como parametro          
        M_COSTTYPE_ID viene como parametro         
        C_ACCTSCHEMA_ID viene como parametro       
        M_COSTELEMENT_ID*/
    }
     
     public BigDecimal updateCostProduct(int M_Product_ID, int M_CostType_ID , int  C_AcctSchema_ID){
        
     BigDecimal costResourceProduct = Env.ZERO;
    
     String costElementType="";
     BigDecimal costTotalProduct = Env.ZERO;
     try {
            // calcula el costo de recursos o indirectos
            RollupWorkflow rr = new RollupWorkflow();
            rr.setParamFromOut(p_AD_Org_ID, p_M_Warehouse_ID , M_Product_ID, M_CostType_ID, C_AcctSchema_ID,costElementType);
            rr.startProcess(getCtx(), this.getProcessInfo(),null/*Trx.get("update",true)*/);
            costResourceProduct=costResourceProduct.add(rr.getCostResource());
           
            costTotalProduct = costTotalProduct.add(costResourceProduct);
            System.out.println("producto: "+String.valueOf(M_Product_ID)+ "total resource: "+ costTotalProduct.toString());
            
           
           

      } catch (Exception ex) {
            log.log(Level.SEVERE,"RollupBillOfMaterila, updateCostProduct ", ex);
     }
       
     return costTotalProduct;
    
    }  
      
 
      
     private  BigDecimal getCostLine(int M_Product_ID , int C_AcctSchema_ID ,int M_CostType_ID)
     {
        org.eevolution.model.MCost[]  pc = org.eevolution.model.MCost.getElements(M_Product_ID , C_AcctSchema_ID , M_CostType_ID);       
        if (pc != null)
        {    
           
            BigDecimal cost = Env.ZERO;

            for (int e = 0 ; e < pc.length ; e ++ )
            {                   
                cost = cost.add(pc[e].getCurrentCostPrice());
          
            }
            return cost;
        }     
        return Env.ZERO;
     }

     private int getMPC_Product_BOM_ID(int AD_Org_ID , int M_Product_ID)
     {
         
         MMPCProductPlanning pp = MMPCProductPlanning.get(getCtx(), AD_Org_ID , M_Product_ID,p_M_Warehouse_ID);                 
         MProduct M_Product = new MProduct(getCtx(), M_Product_ID,null);
         
         int  mpc_product_bom_id = 0;       
        
         if ( pp == null )
         {
                    //System.out.println("pp.getAD_Workflow_ID() ............. " + pp.getAD_Workflow_ID());
                    QueryDB  query = new QueryDB("org.compiere.model.X_MPC_Product_BOM");
                    String filter = "Name = '" + M_Product.getName() + "'";
                    java.util.List results = query.execute(filter);
                    Iterator select = results.iterator();
                    while (select.hasNext())
                    {
                     X_MPC_Product_BOM mpc_product_bom =  (X_MPC_Product_BOM) select.next();                                          
                     return mpc_product_bom.getMPC_Product_BOM_ID();
                    }
         }
         else
         {
              mpc_product_bom_id = pp.getMPC_Product_BOM_ID();  
         }
                  
         return  mpc_product_bom_id;
        
      }
     /**
      * @author Bision 03/07/2008
      * Método que calcula el costo de materiales de un producto dado actualizando todo el árbol de
      * elaboración del mismo.
      */
     private BigDecimal SgetCost(int M_Product_ID, int M_CostType_ID , int  C_AcctSchema_ID) 
     {
           
        //Obtengo la fórmula del producto
        int MPC_Product_BOM_ID =  getMPC_Product_BOM_ID(p_AD_Org_ID , M_Product_ID);
        MMPCProductBOM mpc_product_bom= new MMPCProductBOM(getCtx(),MPC_Product_BOM_ID,null);
        //Obtengo las líneas de la fórmula
        MMPCProductBOMLine[] lines= mpc_product_bom.getLines();
        //Si el producto M_Product_ID es hoja
        if (lines.length==0){
            //Devuelvo a mi padre la suma de todos mis M_Costs
            return getCostLine(M_Product_ID ,C_AcctSchema_ID ,M_CostType_ID);
        }
        else{
            BigDecimal costMaterial=Env.ZERO;
            BigDecimal costResource=Env.ZERO;
            //obtengo y acutalizo el costo de recursos
            costResource=SupdateCostResource(M_Product_ID,M_CostType_ID ,C_AcctSchema_ID);
            //Para cada uno de mis hijos
            for (int i = 0 ; i < lines.length ; i ++ ){
                //tomo la línea de la fórmula
                MMPCProductBOMLine line= (MMPCProductBOMLine)lines[i];
                //obtengo y acutalizo el costo de recursos
                //costResource=SupdateCostResource(line.getM_Product_ID(),M_CostType_ID ,C_AcctSchema_ID);
                //obtengo el costo de Materiales
                costMaterial = costMaterial.add(SgetCost(line.getM_Product_ID(), p_M_CostType_ID , p_C_AcctSchema_ID));
            }
            //Actualizo el Costo de Materiales de M_Product_ID
            this.SupdateCostMaterial(M_Product_ID, costMaterial);
            //Devuelvo para mi padre mi costo de Materiales + mi costo de Recursos
            if (M_Product_ID != p_M_Product_ID){
                costMaterial = costMaterial.multiply(getCantCostsMaterials(M_Product_ID));
                costMaterial = costMaterial.add(costResource);
            }
            return costMaterial;
        }
      }
  /**
   * @author Bision 03/07/2008
   * Método que calcula la cantidad de Costos de Materiales que tiene asociado un producto dado
   */
  public BigDecimal getCantCostsMaterials(int M_Product_ID){
    BigDecimal cant = Env.ZERO;
    BigDecimal uno = Env.ONE;
    org.eevolution.model.MCost[]  pc = org.eevolution.model.MCost.getElements( M_Product_ID , p_C_AcctSchema_ID , p_M_CostType_ID);            
    for (int e = 0 ; e < pc.length ; e ++ ){
        MCostElement element = new MCostElement(getCtx(), pc[e].getM_CostElement_ID(),null);
        if (element.getCostElementType().equals(element.COSTELEMENTTYPE_Material))
            cant = cant.add(uno);
    }
    return cant;    
  }
     
  /**
   * @author Bision - 03/07/2008
   * Método que calcula, actualiza y retorna el costo de recursos de un producto dado
   */
  public BigDecimal SupdateCostResource(int M_Product_ID, int M_CostType_ID , int  C_AcctSchema_ID){
        
         BigDecimal costResourceProduct = Env.ZERO;
         BigDecimal costMaterialProduct = Env.ZERO;
         String costElementType="";
         BigDecimal costTotalProduct = Env.ZERO;
         try {
                // calcula el costo de recursos o indirectos
                RollupWorkflow rr = new RollupWorkflow();
                rr.setParamFromOut(p_AD_Org_ID, p_M_Warehouse_ID , M_Product_ID, M_CostType_ID, C_AcctSchema_ID,costElementType);
                rr.startProcess(getCtx(), this.getProcessInfo(),null/*Trx.get("update",true)*/);
                costResourceProduct=costResourceProduct.add(rr.getCostResource());
                System.out.println("producto: "+String.valueOf(M_Product_ID)+ "total resource: "+ costResourceProduct.toString());

          } catch (Exception ex) {
                log.log(Level.SEVERE,"RollupBillOfMaterila, updateCostProduct ", ex);
         }
         return costResourceProduct;
    }
   /**
    * @author Bision 03/07/2008
    * Método que actualiza, dado un importe, todos los costos de material asociados a un producto dado.
    */
    public void SupdateCostMaterial(int M_Product_ID,BigDecimal costMaterial){
       org.eevolution.model.MCost[]  pc = org.eevolution.model.MCost.getElements( M_Product_ID , p_C_AcctSchema_ID , p_M_CostType_ID);            
       for (int e = 0 ; e < pc.length ; e ++ )
           {
                MCostElement element = new MCostElement(getCtx(), pc[e].getM_CostElement_ID(),null);
                // check if element cost is of type Material
                if (element.getCostElementType().equals(element.COSTELEMENTTYPE_Material))
                {   
                    pc[e].setCurrentCostPrice(costMaterial);
                    pc[e].save(get_TrxName());  
                   
                }

                continue;
           }
    }
  
    /**
     * Bision - 04/06/2008
     * Este método retorna el ID del CostElement según un nombre dado
     * @param name
     * @return
     */
    private int getCostElementIDByName(String name){
        QueryDB query = new QueryDB("org.compiere.model.X_M_CostElement");
        String filter = "name = " + name;
        java.util.List results = query.execute(filter);
        Iterator select = results.iterator();
        while (select.hasNext())
        {
         X_M_CostElement element =  (X_M_CostElement) select.next();                                          
         return element.getM_CostElement_ID();
        }
        return 0;
    }
}	//	OrderOpen
        
