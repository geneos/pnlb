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
import java.lang.annotation.Target;
import java.math.*;
import java.sql.*;
import java.util.*;


import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
//import org.eevolution.model.MMPCCostElement;
import org.eevolution.model.MInvoice;
import org.eevolution.model.MMPCMRP;
import org.eevolution.model.MMPCProductBOM;
import org.eevolution.model.MMPCProductBOMLine;
import org.eevolution.model.MOrderLine;
//import org.eevolution.model.MMPCProductCosting;
import org.eevolution.model.MMPCProductPlanning;
import org.compiere.util.QueryDB;

/**
 *	Rollup Bill of Material
 *
 *  @author Victor Perez, e-Evolution, S.C.
 *  @version $Id: CreateCost.java,v 1.1 2004/06/22 05:24:03 vpj-cd Exp $
 */
public class RollupBillOfMaterial extends SvrProcess
{
	/**					*/
       private int		 		   p_AD_Org_ID = 0;
       private int               p_M_Warehouse_ID = 0;
       private int               p_M_Product_ID = 0;
       private int               p_M_Product_Category_ID = 0;
       private int               p_M_CostType_ID = 0;
       private int               p_C_AcctSchema_ID = 0;
       private BigDecimal        totalCost = Env.ZERO;
       private BigDecimal        totalCostIntermedio=Env.ZERO;
       private int               precision = 3;



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

                        //24-02-2011 Camarzana Mariano
						//Recupero el parametro agregado
                        else if (name.equals("M_Product_Category_ID"))
                        {
                        	p_M_Product_Category_ID = ((BigDecimal)para[i].getParameter()).intValue();

                        }
                        else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}




	}
	
	
/**
 * 24-02-2011 Camarzana Mariano
 * Metodo empleado para retornar el producto o la lista de productos dependiendo 
 * de los parametros del proceso
 * 
 */
	public ResultSet getProductos() throws Exception{
		
		int AD_Client_ID =getAD_Client_ID();
		StringBuffer sql = null;
		if (p_M_Product_ID != 0 && p_M_Product_Category_ID != 0)
			return null;
		if (p_M_Product_ID != 0)
        	{
 
        		sql = new StringBuffer ("SELECT p.M_Product_ID FROM M_Product p WHERE p.ProductType = '" + MProduct.PRODUCTTYPE_Item + "' AND");
        		sql.append(" p.M_Product_ID = " + p_M_Product_ID + " AND p.isactive like 'Y' AND ");
        		sql.append(" p.AD_Client_ID = " + AD_Client_ID);
        		sql.append(" ORDER BY p.LowLevel");
			}
		else if (p_M_Product_Category_ID != 0)
			{
        		sql = new StringBuffer ("select m_product_id from m_product p " +
        								" where p.m_product_category_id =  " + p_M_Product_Category_ID  + " AND " +
        								" p.ProductType = '" + MProduct.PRODUCTTYPE_Item + "' AND" +	
        								" p.isactive like 'Y' and "); 
        		sql.append(" p.AD_Client_ID = " + AD_Client_ID + " order by p.m_product_id");
			}
        
        if (sql != null){
        	PreparedStatement pstmt = null;
            pstmt = DB.prepareStatement (sql.toString(),null);
            return	pstmt.executeQuery ();
        }
        return null;
		} 
 
			
			
	
/**
 * Bision- Nadia, Junio 2008
 * @return
 * @throws java.lang.Exception
 */
/**
 * 24-02-2011 Camarzana Mariano
 * Modificado para permitir correr el proceso Integración de Costos de LDM y Fórmulas por categoria
 */
     protected String doIt() throws Exception
     {  
        ResultSet rs = getProductos();
        try
        	{
	        	if (rs != null){
	                while (rs.next())
	                {
	                  int M_Product_ID = rs.getInt("M_Product_ID");
	                  if (p_M_Product_Category_ID != 0)
	                	  p_M_Product_ID = M_Product_ID;
	                  BigDecimal big = Env.ZERO;
	                  //Calculo el costo de materiales
	                  System.out.println("Producto" + M_Product_ID);
	                  System.out.println("Calculando Costo de Materiales");
	                  big = getCostMaterials(M_Product_ID, p_M_CostType_ID , p_C_AcctSchema_ID);
	                  //Creo o actualizo el costo
	                  System.out.println("Creando Costo de Materiales");
	                  createCostMaterials(big,M_Product_ID);
	                  //Guardo el registro en la tabla MCost
	                }
	                rs.close();
	        	}
	        else return "Debe seleccionar un Producto o Grupo de Producto";
        	}
        	catch (Exception e)
        	{
        		e.printStackTrace();
                return null;
        	}


        return "ok";
        }

     /**
      * Metodo que crea o actualiza el registro de 'Costo Estandar de Materiales'
      * para el producto M_Product_ID
      * @author Bision - 06/08/2008 - Santiago Ibañez
      * @param cost : importe a actualizar
      * @param M_Product_ID : producto cuyo costo hay que actualizar
      */
     private void createCostMaterials(BigDecimal cost, int M_Product_ID){
          int M_CostElement_ID = getCostElementIDByName("'Costo Estandar de Materiales'");
          //Necesarios para crear el MCost
          MProduct product = MProduct.get(getCtx(),M_Product_ID);
          MAcctSchema as = MAcctSchema.get(getCtx(),p_C_AcctSchema_ID,get_TrxName());
          //Creo el MCost
          MCost mcost = MCost.get(product,0, as, p_AD_Org_ID, M_CostElement_ID);
          mcost.setM_AttributeSetInstance_ID(0);
          //si no tiene el mcost, creo uno nuevo
          if (mcost == null){
              mcost = new MCost(product,0,as,p_AD_Org_ID,M_CostElement_ID);
          }
          mcost.setCurrentCostPrice(cost.setScale(precision,BigDecimal.ROUND_HALF_UP));
          //Guardo el registro en la tabla MCost
          mcost.save(get_TrxName());
     }

    /** Metodo que dado un producto de la organizacion retorna la formula de
     * la planificacion que tiene asociada.
     * @author Bision - 08/08/08 - Santiago Ibañez
     * @param AD_Org_ID
     * @param M_Product_ID
     * @return
     */
    private int getMPC_Product_BOM_ID(int AD_Org_ID , int M_Product_ID)
     {

         MMPCProductPlanning pp = MMPCProductPlanning.get(getCtx(), AD_Org_ID , M_Product_ID,p_M_Warehouse_ID);
         MProduct M_Product = new MProduct(getCtx(), M_Product_ID,null);

         int  mpc_product_bom_id = 0;

         if ( pp == null )
         {
                   	
        	 		/*//System.out.println("pp.getAD_Workflow_ID() ............. " + pp.getAD_Workflow_ID());
                    QueryDB  query = new QueryDB("org.compiere.model.X_MPC_Product_BOM");
                    String filter = "Name = '" + M_Product.getName() + "'";
                    java.util.List results = query.execute(filter);
                    Iterator select = results.iterator();
                    while (select.hasNext())*/
        	 
        	 		 /*
        	 		  * 25-02-2011 Camarzana Mariano modificado para que realice la consulta por id y no por nombre
        	 		  * Daba problemas con el producto GL284107-1 que contiene ELAB0074-1
        	 		  * Al buscar ELAB0074-1 por nombre me daba la formula del GL284107-1 
        	 		  */
		        	 
        	 		 //System.out.println("pp.getAD_Workflow_ID() ............. " + pp.getAD_Workflow_ID());
		             QueryDB  query = new QueryDB("org.compiere.model.X_MPC_Product_BOM");
		             String filter = "m_product_id = " + M_Product.getM_Product_ID();
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

   private BigDecimal getIndirectCost(int M_Product_ID,int C_AcctSchema_ID,int M_CostType_ID){
        //Obtengo todos los costos asociados al producto
        org.eevolution.model.MCost[]  pc = org.eevolution.model.MCost.getElements(M_Product_ID , C_AcctSchema_ID , M_CostType_ID);
        if (pc != null){
            BigDecimal rate = Env.ZERO;
            //Busco el costo 'Costo Estandar Indirecto'
            for (int e = 0 ; e < pc.length ; e ++ )
            {
                MCostElement element = new MCostElement(getCtx(), pc[e].getM_CostElement_ID(),null);
                if (element.getM_CostElement_ID()== getCostElementIDByName("'Costo Estandar Indirecto'")){
                    //obtengo el importe del 'Costo Estandar Indirecto'
                    rate = rate.add(pc[e].getCurrentCostPrice());
                }
            }
            return rate;
        }
        return Env.ZERO;

   }

    //private BigDecimal


     /** Metodo que retorna todas las lineas de ordenes de compra para un
      * producto dado
      * @author Bision - 06/08/08 - Santiago Ibañez
      * @param M_Product_ID
      * @return arreglo de lineas de O.C.
      */
     private BigDecimal getPrecioUltimaCompra (int M_Product_ID){
        //ESTA FUNCION POR AHORA BUSCA LAS ORDENES DE COMPRA PARA UN PRODUCTO
        //ORDENADAS POR FECHA Y A LA PRIMERA QUE OBTIENE SE LE SACA EL PRECIO.
        //TOMAR EN CUENTA QUE ESTE PRECIO PUEDE LLEGAR A SER OBTENIDO DESDE LOS
        //REGISTROS DE COSTOS (MCosts) CUYO ELEMENTO DE COSTO SEA ("ULTIMA O.C.)
        MInvoiceLine il;
        try {
       	il = getUltimaLineaFacturaCompra(M_Product_ID);
            if (il!=null){
            	MInvoice invoice = new MInvoice(Env.getCtx(), il.getC_Invoice_ID(),null);
            	return il.getPriceEntered().multiply(invoice.getCotizacion());
            }
        } catch (Exception ex) {
            Logger.getLogger(RollupBillOfMaterial.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        /* 28-02-2011 Camarzana Mariano
         * Modificado para que considere la OC en estado "CO" y "CL"
         * 
         */
        
        try {
        	 String sql = "SELECT c_orderline.C_OrderLine_ID" +
        	 			  " FROM c_order join  c_orderline on (c_order.c_order_id = c_orderline.c_order_id)" +
        	 			  " WHERE c_orderline.M_Product_ID = " + M_Product_ID + " and c_order.docstatus in ('CO','CL')" +
        	 			  "ORDER BY c_orderline.DATEORDERED desc";
        	 PreparedStatement pst = DB.prepareStatement(sql,null);
        	 ResultSet rs = pst.executeQuery();
        	 if (rs.next()){
    	         MOrderLine ol = new MOrderLine(getCtx(),rs.getInt(1),get_TrxName());
    	         
    	         //16-02-2011 Camarzana Mariano 
    	         //Modificacion para tomar la cotizacion del importe de la orden
    	         MOrder order = new MOrder(getCtx(),ol.getC_Order_ID(), null);
    	         //devuelvo la primera que encuentro porque estaban ordenadas
    	         
    	         return ol.getPriceEntered().multiply(order.getCotizacion());
        	 }
        	}
        	 catch (Exception ex) {
                 Logger.getLogger(RollupBillOfMaterial.class.getName()).log(Level.SEVERE, null, ex);
             }
        	 
       
        /* ArrayList<MOrderLine> list = new ArrayList<MOrderLine>();
        QueryDB query = new QueryDB("org.compiere.model.X_C_OrderLine");
        String filter = "M_Product_ID = " + M_Product_ID + " ORDER BY DATEORDERED desc";
        java.util.List results = query.execute(filter);
        Iterator select = results.iterator();
        while (select.hasNext())
          {
         X_C_OrderLine xol = (X_C_OrderLine) select.next();
         MOrderLine ol = new MOrderLine(getCtx(),xol.getC_OrderLine_ID(),get_TrxName());
         
        
         
         //16-02-2011 Camarzana Mariano 
         //Modificacion para tomar la cotizacion del importe de la orden
         MOrder order = new MOrder(getCtx(),ol.getC_Order_ID(), null);
         //devuelvo la primera que encuentro porque estaban ordenadas
        
         
         
         return ol.getPriceEntered().multiply(order.getCotizacion());
        }*/
	// = list.toArray(orderlines);
        //retorno el precio de la primer orden de compra encontrada (ordenadas)
        return BigDecimal.ZERO;
     }

     /**
      * @author Bision 03/07/2008
      * Metodo que calcula el costo de materiales de un producto dado, actualizando todo el arbol de
      * elaboracion del mismo.
      */
     private BigDecimal getCostMaterials(int M_Product_ID, int M_CostType_ID , int  C_AcctSchema_ID)
     {
        //Obtengo la formula del producto
        int MPC_Product_BOM_ID =  getMPC_Product_BOM_ID(p_AD_Org_ID , M_Product_ID);
        MMPCProductBOM mpc_product_bom= new MMPCProductBOM(getCtx(),MPC_Product_BOM_ID,null);
        //Obtengo las lineas de la formula
        MMPCProductBOMLine[] lines= mpc_product_bom.getLines();
        if (lines.length==0){
            MProduct p = new MProduct(getCtx(), M_Product_ID, null);
            //Devuelvo a mi padre el precio de la ultima orden de compra
            System.out.println("Insumo: "+p.getValue());
            return getPrecioUltimaCompra(M_Product_ID);
        }
        //si el producto no es materia prima y el producto no es el parametro
        else {
            MProduct p = new MProduct(getCtx(), M_Product_ID, null);
            System.out.println("Elaborado: "+p.getValue());
            BigDecimal costMaterial=Env.ZERO;
            BigDecimal costResource=Env.ZERO;
            BigDecimal costoMaterialHijo = Env.ZERO;
            BigDecimal costoIndirecto = Env.ZERO;
            if (M_Product_ID!=p_M_Product_ID){
                //obtengo y actualizo el costo de recursos solo si NO es el producto en cuestion
                costResource=SupdateCostResource(M_Product_ID,M_CostType_ID ,C_AcctSchema_ID);
                System.out.println("Costo de Recursos: $"+costResource);
                costoIndirecto = getIndirectCost(M_Product_ID,C_AcctSchema_ID,M_CostType_ID);
                System.out.println("Costo Indirecto: $"+costoIndirecto);
            }
            //Para cada uno de mis hijos obtengo su costo de materiales
            for (int i = 0 ; i < lines.length ; i ++ ){
                //obtengo el costo de uno de sus hijos
                costoMaterialHijo = getCostMaterials(lines[i].getM_Product_ID(), p_M_CostType_ID , p_C_AcctSchema_ID);
                System.out.println("Costo Unitario: "+costoMaterialHijo);
                //multiplico el costo de uno de sus hijos por la cantidad
                
                /*
                 *23-03-2011 Camarzana Mariano
                 *Modificado ya que no tenia en cuenta si la cantidad estaba expresada en porcentaje 
                 */
                if (lines[i].isQtyPercentage())
                	costoMaterialHijo = costoMaterialHijo.multiply(lines[i].getQtyBatch().divide(new BigDecimal(100)));
                else
                	costoMaterialHijo = costoMaterialHijo.multiply(lines[i].getQtyBOM());
                
                
                System.out.println("Costo: "+costoMaterialHijo);
                //obtengo el costo de Materiales
                costMaterial = costMaterial.add(costoMaterialHijo);
            }
            System.out.println("Costo de Materiales: $"+costMaterial);
            if (M_Product_ID != p_M_Product_ID){
                //Actualizo el Costo de Materiales de M_Product_ID
                this.createCostMaterials(costMaterial, M_Product_ID);
                //Devuelvo para mi padre mi costo de Materiales + mi costo de Recursos + mi costo indirecto
                costMaterial = costMaterial.add(costoIndirecto);
                costMaterial = costMaterial.add(costResource);
            }
            return costMaterial;
        }
     }

  /**
   * @author Bision - 03/07/2008
   * Metodo que calcula, actualiza y retorna el costo de recursos de un producto dado
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
    * Metodo que actualiza, dado un importe, todos los costos de material asociados a un producto dado.
    */
    /*public void SupdateCostMaterial(int M_Product_ID,BigDecimal costMaterial){
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
    }*/

    /**
     * Bision - 04/06/2008
     * Este metodo retorna el ID del CostElement segun un nombre dado
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

        private MInvoiceLine getUltimaLineaFacturaCompra(int M_Product_ID) throws Exception{
			MInvoiceLine invoiceLine = null;
			//Como actualmente no se pueden completar Facturas por un problema en Panalab se quito la consideracion de facturas en estado 'CO'
            //PreparedStatement ps = DB.prepareStatement("SELECT il.C_InvoiceLine_ID FROM C_Invoiceline il join C_Invoice i on (i.C_Invoice_ID = il.C_Invoice_ID) WHERE i.docstatus = 'CO' AND il.M_Product_ID = "+M_Product_ID+"AND i.issotrx = 'N' ORDER BY i.dateinvoiced DESC", null);
            //PreparedStatement ps = DB.prepareStatement("SELECT il.C_InvoiceLine_ID FROM C_Invoiceline il join C_Invoice i on (i.C_Invoice_ID = il.C_Invoice_ID) WHERE il.M_Product_ID = "+M_Product_ID+"AND i.issotrx = 'N' ORDER BY i.dateinvoiced DESC", null);
			
			//28-02-2011 Camarzana Mariano
			//Modificado para que tome las OC
			PreparedStatement ps = DB.prepareStatement("SELECT il.C_InvoiceLine_ID FROM C_Invoiceline il join C_Invoice i on (i.C_Invoice_ID = il.C_Invoice_ID) WHERE i.docstatus = 'CO' AND il.M_Product_ID = "+M_Product_ID+"AND i.issotrx = 'N' ORDER BY i.dateinvoiced DESC", null);
			//try{
				ResultSet rs = ps.executeQuery();
				if (rs.next())//{
					invoiceLine = new MInvoiceLine(getCtx(),rs.getInt(1),null);
			//	}
                rs.close();
                ps.close();
			/*}
			catch(Exception e){
                e.printStackTrace();
			}*/
			return invoiceLine;
		}
}	//	OrderOpen

