/*****************************************************************************
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
 * Contributor(s): Santiago Ibaï¿½ez
 *****************************************************************************/

package org.eevolution.process;

import java.util.logging.*;
import java.math.*;
import java.util.*;

import javax.swing.JOptionPane;


import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
import org.eevolution.model.*;
import org.compiere.util.QueryDB;

/**
 *  Clase que implementa el proceso de "Asignar Costo Real Indirecto" para una
 *  orden de manufactura dada.
 *  @author Santiago Ibañez, Bision
 */
public class GenerateIndirectCosts extends SvrProcess
{

       private BigDecimal       p_Amount = BigDecimal.ZERO;
       private int              p_MPC_Order_ID = 0;
       private BigDecimal 		C_AcctSchema_ID = BigDecimal.ZERO;
       private BigDecimal		M_Product_Category_ID = BigDecimal.ZERO;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("Amount")){
				p_Amount = ((BigDecimal)para[i].getParameter());
			}
            else if (name.equals("M_Product_Category_ID")){
            	M_Product_Category_ID = (BigDecimal) (para[i].getParameter());
            }
            else if (name.equals("C_AcctSchema_ID")){
            	C_AcctSchema_ID = (BigDecimal) (para[i].getParameter());
            }
            else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare


     /**
     * Bision 19/08/2008
     * Funcion que retorna un registro de costo de la OM en cuestión con un
     * elemento de costo dado. En caso de no existir crea uno nuevo.
     */
    private MMPCOrderCost getRegistroCosto(int MPC_Order_ID,int MPC_Cost_Element_ID,BigDecimal C_AcctSchema_ID,int M_Product_ID){
       //creo el costo de la orden y seteo la info
       QueryDB query = new QueryDB("org.compiere.model.X_MPC_Order_Cost");
       String filter = "MPC_Order_ID = " + MPC_Order_ID + " AND MPC_Cost_Element_ID = " + MPC_Cost_Element_ID;
       java.util.List results = query.execute(filter);
       Iterator select = results.iterator();
       while (select.hasNext()){
         X_MPC_Order_Cost xoc = (X_MPC_Order_Cost) select.next();
         if (xoc.getMPC_Cost_Element_ID()==MPC_Cost_Element_ID){
             MMPCOrderCost oc = new MMPCOrderCost(getCtx(),xoc.getMPC_Order_Cost_ID(),get_TrxName());
             return oc;
         }
       }
       //si no existe un registro de costo creo uno nuevo.
       MMPCOrderCost mpc_order_cost = new MMPCOrderCost(getCtx(), 0,get_TrxName());
       mpc_order_cost.setMPC_Order_ID(MPC_Order_ID);
       mpc_order_cost.setC_AcctSchema_ID(C_AcctSchema_ID.intValue());
       mpc_order_cost.setM_Product_ID(M_Product_ID);
       mpc_order_cost.setMPC_Cost_Element_ID(MPC_Cost_Element_ID);
       return mpc_order_cost;
    }

    /* Bision - 19/02/2009 - Santiago Ibañez
     * Metodo que actualiza los registros de costos indirectos para cada una
     * de las OM de manufacturas completas involucradas en el proceso, es decir
     * que tengan asociado un producto correspondiente a la categoria dada.
     * NOTA: Las OM deben tener al menos un control de actividad completo
     * */
    private boolean actualizarCostosIndirectos(){
       //Obtengo todos los ID de productos de la categoria seleccionada
       int[] products = MProduct.getAllIDs("M_Product", "M_Product_Category_ID = "+M_Product_Category_ID, null);
       //Obtengo el ID del elemento de costo 'Real Indirecto'
       int MPC_CostElement_ID = MMPCCostElement.getCostElementByName("Costo Real Indirecto").getMPC_Cost_Element_ID();
       MMPCCostElement element = MMPCCostElement.getCostElementByName("Costo Real Indirecto");
       //Existe el elemento de costo: 'Costo Real Indirecto' ?
       if (element==null){
    	   JOptionPane.showMessageDialog(null, "No existe el elemento de costo: Costo Real Indirecto", "ERROR: Elemento de costo inexistente", JOptionPane.ERROR_MESSAGE);
    	   return false;
       }
       MPC_CostElement_ID = element.getMPC_Cost_Element_ID();
       //Obtengo las OM a las cuales se les va a actualizar su costo
       MMPCOrder orders[] = getOMCompletas(products);
       BigDecimal costoUnitario = BigDecimal.ZERO;
       if (orders.length!=0)
    	   //Obtengo el costo unitario indirecto
    	   costoUnitario = getCostoUnitario(orders);
       //Actualizo cada uno de los registros de costos indirectos de OM
       if (orders.length!=0)
    	   actualizarCostosOM(orders, costoUnitario, MPC_CostElement_ID);
       return true;
    }

    /** BISion - 19/02/2009 - Santiago Ibañez
     * Metodo que retorna el costo unitario que se calcula de la sig manera:
     * CU = importe ingresado / sumatoria de todas las cantidades de OM
     * NOTA: No toma en cuenta OM sin al menos 1 control de actividad completo
     * @param products
     * @return
     */
    private BigDecimal getCostoUnitario(MMPCOrder orders[]){
    	if (p_Amount.equals(BigDecimal.ZERO))
    		return BigDecimal.ZERO;
    	//cantidad total acumulada de unidades a producir (suma todas las OM)
 	    BigDecimal cantidadProducir = BigDecimal.ZERO;
    	for (int i=0;i<orders.length;i++){
    		cantidadProducir = cantidadProducir.add(orders[i].getQtyDelivered());
    	}
        //Para cada Producto obtengo cada una de las OM completas asociadas
    	if (!cantidadProducir.equals(BigDecimal.ZERO))
            return p_Amount.divide(cantidadProducir,8,p_Amount.ROUND_UP);
        else
            return BigDecimal.ZERO;
    }

    /** BISion 19/02/2009 - Santiago IbaÃ±ez
     * Metodo que actualiza o crea los registros de costos indirectos para las OM dadas
     * @param orders
     * @param costoUnitario
     * @param MPC_CostElement_ID
     */
    private void actualizarCostosOM(MMPCOrder orders[], BigDecimal costoUnitario, int MPC_CostElement_ID){
    	for(int j=0;j<orders.length;j++){
    	   System.out.println("Actualizando Costo Indirecto Real de la Orden nro: "+orders[j].getDocumentNo());
           //obtengo el costo real (de acuerdo a MPC_Cost_Element_ID) y actualizo o crea uno nuevo
  	       MMPCOrderCost mpc_order_cost = getRegistroCosto(orders[j].getMPC_Order_ID(),MPC_CostElement_ID,C_AcctSchema_ID,orders[j].getM_Product_ID());
  	       mpc_order_cost.setCostCumQty(costoUnitario.multiply(orders[j].getQtyDelivered()));
  	       mpc_order_cost.save(get_TrxName());
  	   }
    }

    /** BISion - 19/02/2009 - Santiago Ibañez
     * Metodo que retorna todas las OM completas asociadas a la categoria dada
     * con al menos un registro de control de actividad completo.
     * @param products
     * @return
     */
    private MMPCOrder[] getOMCompletas(int products[]){
    	ArrayList<MMPCOrder> list = new ArrayList<MMPCOrder>();
    	for (int i=0;i<products.length;i++){
			int[] orders = MMPCOrder.getAllIDs("MPC_Order", "M_Product_ID = "+products[i]+" AND Docstatus = 'CO' ", null);
			//Para cada orden obtenida obtengo los controles de actividad
	  	    
			/*
			 * 06-04-2011 Camarzana Mariano
			 * Comentado debido a que se pidio como cambio 
			 * que calcule los CRI con la sola restriccion de que las OM esten en estado CO sin importar el registro de actividad
			 */
			
			/*for(int j=0;j<orders.length;j++){                
	  	    	MMPCOrder order = new MMPCOrder(Env.getCtx(),orders[j],null);
	  	    	//Obtengo solo los controles de actividad excluyendo los surtimientos, etc.
                MMPCCostCollector col[] = order.getManufacturingOperationActivity(orders[j]);
	  	    	//busco al menos un cost collector en estado completo
	  	    	for (int k=0;k<col.length;k++){
	  	    		if (col[k].getDocStatus().equals("CO")&&!order.getQtyDelivered().equals(BigDecimal.ZERO)){
	  	    			list.add(order);
	  	    			break;
	  	    		}
	  	    	}
	    	}*/
			for(int j=0;j<orders.length;j++){         
				MMPCOrder order = new MMPCOrder(Env.getCtx(),orders[j],null);
				if (!order.getQtyDelivered().equals(BigDecimal.ZERO)){
	  	    			list.add(order);
	  	    		}
  	    	}
			
    	}
    	MMPCOrder oms[] = new MMPCOrder[list.size()];
    	return list.toArray(oms);
  }
     protected String doIt() throws Exception
     {
        if (actualizarCostosIndirectos())
    		return "ok";
    	else
    		return "";
     }
     
}
