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
 * Contributor(s): Santiago Iba�ez
 *****************************************************************************/

package org.eevolution.process;

import java.util.logging.*;
import java.math.*;
import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;


import org.compiere.model.*;
import org.compiere.wf.*;
import org.compiere.util.*;
import org.compiere.process.*;
import compiere.model.*;
import org.eevolution.model.*;
import org.eevolution.model.MCost;
import org.compiere.util.QueryDB;

/**
 *  Clase que implementa el proceso de "Asignar Costo Real Indirecto" para una
 *  orden de manufactura dada.	
 *  @author Santiago Iba�ez, Bision
 */
public class GenerateStandarIndirectCosts extends SvrProcess
{

       private BigDecimal       p_Amount = BigDecimal.ZERO;
       private BigDecimal		M_Product_Category_ID = BigDecimal.ZERO;
       private BigDecimal		C_AcctSchema_ID = BigDecimal.ZERO;
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
            else if (name.equals("C_AcctSchema_ID"))
            	C_AcctSchema_ID = (BigDecimal) (para[i].getParameter());
            else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare
	
    /** BISion - 20/02/2009 - Santiago Ibañez
     * Metodo que retorna todos los ID de productos correspondientes a la categoria dada
     * @return
     */
	private int[] getProductosCategoria(){
    	int productos[] = MProduct.getAllIDs("M_Product", "M_Product_Category_ID = "+M_Product_Category_ID, null);
    	return productos;
    }
	
	/**
	 * Metodo que obtiene (si existe) o crea un registro de costo cuyo elemento de costo
	 * sea 'Costo Estandar Indirecto'.
	 * @param M_Product_ID
	 * @param M_Cost_Element_ID
	 */
	private void actualizarRegistroCosto(int M_Product_ID,int M_CostElement_ID){
		//Obtengo los registros de costos asociados al producto
		MCost costos[] = MCost.getElements(M_Product_ID, C_AcctSchema_ID.intValue());
		for (int i=0; i<costos.length;i++){
			//Considero unicamente 'Costo Estandar Indirecto'
			if (costos[i].getM_CostElement_ID()==M_CostElement_ID){
				costos[i].set_TrxName(null);
				costos[i].setCurrentCostPrice(p_Amount);
				costos[i].save();
				return;
			}
		}
		MProduct product = new MProduct(Env.getCtx(),M_Product_ID,null);
		MAcctSchema acct = new MAcctSchema(Env.getCtx(),C_AcctSchema_ID.intValue(),null);
		//Creo un nuevo registro de costo ya que no existia
		MCost cost = new MCost(product ,0,acct,Env.getAD_Org_ID(Env.getCtx()),M_CostElement_ID);
		cost.setCurrentCostPrice(p_Amount);
		cost.setM_CostElement_ID(M_CostElement_ID);
		cost.save();
	}
	
	/** BISion - 20/02/2009 - Santiago Ibañez
	 * Metodo que actualiza o crea los registros de costos de todos los productos
	 * asociados a una categoria dada
	 */
	private void actualizarCostoEstandarIndirecto(){
    	//Existe elemento de costo 'Costo Estandar Indirecto'
    	MCostElement cost = MCostElement.getCostElementByName("Costo Estandar Indirecto");
    	if (cost==null)
    		JOptionPane.showMessageDialog(null, "No existe el elemento de costo: Costo Estandar Indirecto", "ERROR: Elemento de costo inexistente", JOptionPane.ERROR_MESSAGE);
    	else{
    		//obtengo los productos de la categoria
    		int productos[] = getProductosCategoria();
    		//para cada uno de los productos
    		for (int i=0;i<productos.length;i++)
    			actualizarRegistroCosto(productos[i],cost.getM_CostElement_ID());
    	}
    } 
	
	protected String doIt() throws Exception                
     {
    	 actualizarCostoEstandarIndirecto();
    	 return "ok";
     }
                                                                
}
