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
 * Contributor(s): Victor Perez s.i.
 *****************************************************************************/

package org.eevolution.process;

import java.util.logging.*;
import java.math.*;
import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


import org.compiere.model.*;
import org.compiere.wf.*;
import org.compiere.util.*;
import org.compiere.process.*;
import compiere.model.*;
//import org.eevolution.model.MMPCCostElement;
//import org.eevolution.model.MMPCProductCosting;
import org.eevolution.model.MMPCProductPlanning;
import org.compiere.util.QueryDB;

/**
 *	Rollup of Rouning
 *
 *  @author Victor Perez, e-Evolution, S.C.
 *  @version $Id: CreateCost.java,v 1.1 2004/06/22 05:24:03 vpj-cd Exp $
 */
public class RollupWorkflow extends SvrProcess
{

       private int		 p_AD_Org_ID = 0;
       private int               p_C_AcctSchema_ID = 0;
       private int               p_M_Warehouse_ID = 0;
       private int               p_M_Product_Category_ID = 0;
       private int               p_M_Product_ID = 0;
       private int               p_M_CostType_ID = 0;
       private BigDecimal        costResource=Env.ZERO;
       private int               precision = 3;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{

              if ( p_AD_Org_ID == 0 ) {
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
                        /*else if (name.equals("S_Resource_ID"))
                        {
				p_S_Resource_ID = ((BigDecimal)para[i].getParameter()).intValue();

                        }
                        else if (name.equals("MPC_Cost_Group_ID"))
                        {

                                p_MPC_Cost_Group_ID = ((BigDecimal)para[i].getParameter()).intValue();
                        }*/
                        else if (name.equals("M_CostType_ID"))
                        {

                                p_M_CostType_ID = ((BigDecimal)para[i].getParameter()).intValue();
                        }
                        //else if (name.equals("ElementType"))
                        //{
			//	p_ElementType = (String)para[i].getParameter();
                        //
                        //}
                        else if (name.equals("C_AcctSchema_ID"))
                        {
				p_C_AcctSchema_ID = ((BigDecimal)para[i].getParameter()).intValue();

                        }
                        /*else if (name.equals("M_Produc_Category_ID"))
                        {
				p_M_Product_Category_ID = ((BigDecimal)para[i].getParameter()).intValue();

                        }*/
	            //03-03-2011 Camarzana Mariano
				//Recupero el parametro agregado
	            else if (name.equals("M_Product_Category_ID"))
	            {
	            	p_M_Product_Category_ID = ((BigDecimal)para[i].getParameter()).intValue();
	
	            }
                        else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}}
	}	//	prepare


    /* Bision - 04/08/2008 - Santiago Ibañez
     * Funcion que dado un elemento de costo, comprueba si es de tipo
     * 'Estandar de Recursos'
     * */
     private boolean esCostoRecursos(MCostElement e){
        return e.getM_CostElement_ID() == getCostElementIDByName("'Costo Estandar de Recursos'");
     }

     
     /**
      * 03-03-2011 Camarzana Mariano
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
     				sql = new StringBuffer ("SELECT p.M_Product_ID FROM M_Product p  " +
     										" WHERE p.ProductType = '" + MProduct.PRODUCTTYPE_Item + "' AND");
             		sql.append(" p.M_Product_ID = " + p_M_Product_ID + " AND p.isactive like 'Y' AND ");
             		sql.append(" p.AD_Client_ID = " + AD_Client_ID);
             		sql.append(" ORDER BY p.LowLevel");
     			}
     		else if (p_M_Product_Category_ID != 0)
     			{
             		sql = new StringBuffer ("select m_product_id from m_product p where " +
             								" p.m_product_category_id =  " + p_M_Product_Category_ID  + " AND " +
             								"p.ProductType = '" + MProduct.PRODUCTTYPE_Item + "' AND" +
             								" p.isactive like 'Y' and "); 
             		
             		//"p.ProductType = '" + MProduct.PRODUCTTYPE_Item + "'" + "and p.isactive like 'Y' and ");
             				
             		sql.append(" p.AD_Client_ID = " + AD_Client_ID + " order by p.m_product_id");
     			}
             
             if (sql != null){
             	PreparedStatement pstmt = null;
                 pstmt = DB.prepareStatement (sql.toString(),null);
                 return	pstmt.executeQuery ();
             }
             return null;
     		} 

     	
     protected String doIt() throws Exception
     {
         ResultSet rs = getProductos();
         try
         	{
 	        	if (rs != null){
 	                while (rs.next())
 	                {
 	                  //obtengo el id del producto en cuestion
 	                  int M_Product_ID = rs.getInt("M_Product_ID");
 	                  if (p_M_Product_Category_ID != 0)
 	                	  p_M_Product_ID = M_Product_ID;
 	                  BigDecimal resource = Env.ZERO;
 	                  //obtengo el costo de recursos
 	                  resource=getCost(p_AD_Org_ID , M_Product_ID , p_M_CostType_ID , p_C_AcctSchema_ID);
                      costResource = resource;
                      //creo o actualizo el registro de costo estandar de recursos
                      createCostResource(resource,M_Product_ID);
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
     * Bision - 04/07/2008
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
     /**
     * Bision - 08/07/2008
     * Crea el costo estandar de recursos para el producto especificado,
     * con el monto del cost. Si no existe un registro de costo, lo crea.
     * @param cost
     * @param M_Product_ID
     * @return
     */
     private void createCostResource(BigDecimal cost, int M_Product_ID){
          int M_CostElement_ID = getCostElementIDByName("'Costo Estandar de Recursos'");
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
          mcost.setCurrentCostPrice(cost);
          //Guardo el registro en la tabla MCost
          mcost.save(get_TrxName());
     }

     /* Bision - 15/07/2008 - Santiago Ibañez
     * Funcion que dado un recurso retorna la unidad de medida
     * */
    private MUOM getResourceUOM(int S_Resource_ID){
        int C_UOM_ID = DB.getSQLValue(null,"SELECT C_UOM_ID FROM M_Product WHERE S_Resource_ID = ? " , S_Resource_ID);
        MUOM uom = new MUOM(getCtx(),C_UOM_ID,null);
        return uom;
    }

     /* Bision - 04/08/2008 - Santiago Ibañez
      * Funcion que se encarga de calcular el costo estandar de recursos
      * recorriendo los nodos del workflow asociado al producto en cuestion.
      * */
     private BigDecimal getCost(int AD_Org_ID , int M_Product_ID , int M_CostType_ID , int  C_AcctSchema_ID)
     {
         BigDecimal cost = Env.ZERO;
         BigDecimal time=Env.ZERO;
         //tomo la planeacion del producto
         MMPCProductPlanning pp=get_MMPCProductPlanning(AD_Org_ID , M_Product_ID);
         int AD_Workflow_ID =0;
         if ( pp != null )
             AD_Workflow_ID=pp.getAD_Workflow_ID();
         if (AD_Workflow_ID != 0&&pp!=null)
         {
            //tomo el flujo de trabajo para el producto M_Product_ID
            MWorkflow Workflow = new MWorkflow(getCtx(),AD_Workflow_ID,null);
            //tomo los nodos del flujo de trabajo para el producto M_Product_ID
            MWFNode[] nodes = Workflow.getNodes(false,getAD_Client_ID());
            for (int i = 0 ; i < nodes.length ; i ++ )
            {
                MWFNode node = (MWFNode) nodes[i];
                BigDecimal rate = getRate(node.getS_Resource_ID() , AD_Org_ID , C_AcctSchema_ID , M_CostType_ID);
                //obtengo la unidad de medida del recurso asociado al nodo
                MUOM uom = getResourceUOM(node.getS_Resource_ID());
                //obtengo la unidad de duracion del nodo
                String wfDuration = Workflow.getDurationUnit();
                BigDecimal time_resource = BigDecimal.ONE;
                BigDecimal costo_resource = BigDecimal.ZERO;
                time = BigDecimal.ONE;
                //Normalizo a segundos si no tienen la misma unidad de tiempo
                if (!igualUnidadTiempo(wfDuration,uom)){
                    //convierto la unidad de medida del recurso a segundos
                    time_resource=getSeconds(node.getS_Resource_ID());
                    //convierto la unidad de tiempo del nodo a segundos
                    time = getSeconds(wfDuration,node.getS_Resource_ID());
                }
                //calculo el costo por unidad de tiempo para el recurso [$/t]
                if (!time_resource.equals(BigDecimal.ZERO))
                    costo_resource=rate.divide(time_resource,precision,BigDecimal.ROUND_HALF_UP);

                //ï¿½es tiempo por lote?
                if ( node.isBatchTime() ){
                    if ( ! pp.getOrder_Pack().equals(Env.ZERO) ){
                        BigDecimal size_lote=(BigDecimal)pp.getOrder_Pack();
                        //obtengo la duracion del nodo
                        BigDecimal duracionNodo = new BigDecimal(node.getBATCHTIME());
                        time = time.multiply(duracionNodo);
                        costo_resource=costo_resource.multiply(time);
                        cost = cost.add(costo_resource.divide(size_lote,precision,BigDecimal.ROUND_HALF_UP));
                    }

                }else{
                    //obtengo la duracion del nodo
                    BigDecimal duracionNodo = new BigDecimal(node.getDuration());
                    time = time.multiply(duracionNodo);
                    //acumulo el costo de todos los recursos, multiplicando por la duraciï¿½n de este nodo [$]
                    cost = cost.add(costo_resource.multiply(time));
                    //Limito la precisiï¿½n del costo a dos decimales
                    //cost = cost.setScale(2,BigDecimal.ROUND_HALF_UP);
                }
            } //fin recorrido de nodos
            return cost;
         }
         return cost;
     }

     /** Bision - 05/08/08 - Santiago Ibaï¿½ez
     * Funcion que comprueba si unit y uom se corresponden con la misma unidad
     * de tiempo
     * @param unit : El workflow utiliza Strings para la unidad de tiempo
     * @param uom : El recurso utiliza un objeto MUOM para la unidad de tiempo
     */
     private boolean igualUnidadTiempo(String unit,MUOM uom){
      if (unit.equals("h")&&uom.isHour())
        return true;
      else if (unit.equals("m")&&uom.isMinute())
        return true;
      else if (unit.equals("D")&&uom.isDay())
        return true;
      else if (unit.equals("M")&&uom.isMonth())
        return true;
      else if (unit.equals("Y")&&uom.isYear())
        return true;
      return false;
    }

     /** Bision - 05/08/08 - Santiago Ibañez
      * Funcion que dado un String me retorna el equivalente en segundos
      * @param u : unidad de tiempo
      * @return
      */
     private BigDecimal getSeconds(String u,int S_Resource_ID){
        //Obtengo el tipo del recurso

         //03-03-2011 Camarzana Mariano
         // Verifico que contenga el recurso

    	 if (S_Resource_ID == 0)
         	return BigDecimal.ZERO;
    	 MResource resource = new MResource(getCtx(),S_Resource_ID,get_TrxName());
        MUOM uom = getResourceUOM(S_Resource_ID);
        MResourceType type = resource.getResourceType();
         //Normalizo el tiempo del RA a segundos [seg]
        if (u.equals("h"))
            return new BigDecimal(3600.0);
        else if (u.equals("m"))
            return new BigDecimal(60.0);
        else if (u.equals("D")){
            BigDecimal segundosPorHora = new BigDecimal(3600);
            return getHorasDisponibles(type).multiply(segundosPorHora);
        }
        else if (u.equals("Y")){
            BigDecimal segundosPorHora = new BigDecimal(3600);
            BigDecimal meses = new BigDecimal(12);
            return meses.multiply(getDiasDisponibles(type,4).multiply(getHorasDisponibles(type)).multiply(segundosPorHora));
        }
        else if (u.equals("M")){
            BigDecimal horasPorSegundo = new BigDecimal(3600);
            return getDiasDisponibles(type,4).multiply(getHorasDisponibles(type)).multiply(horasPorSegundo);
        }
        return new BigDecimal(1.0);
     }

     /**
     * Bision - 13/08/08 - Santiago Ibaï¿½ez
     * Funciï¿½n que retorna la cantidad de dï¿½as disponibles que estï¿½ el recurso
     * en N semanas.
     * @param type: tipo de recurso
     * @return
     */
    private BigDecimal getDiasDisponibles(MResourceType type,int semanas){
        if (type.isDateSlot()){
            int cant = 0;
            if (type.isOnMonday())
                cant++;
            if (type.isOnTuesday())
                cant++;
            if (type.isOnWednesday())
                cant++;
            if (type.isOnThursday())
                cant++;
            if (type.isOnFriday())
                cant++;
            if (type.isOnSaturday())
                cant++;
            if (type.isOnSunday())
                cant++;
            cant = cant*semanas;
            return new BigDecimal(cant);
        }
        if (semanas==4)
            return new BigDecimal(30);
        else{
            int dias = semanas *7;
            return new BigDecimal(dias);
        }
    }

     /**
     * Bision - 12/08/08 - Santiago Ibaï¿½ez
     * Funciï¿½n que retorna la cantidad de horas por dï¿½a que un recurso (dado
     * su tipo) estï¿½ operativo.
     * @param type: tipo de recurso
     * @return
     */
    private BigDecimal getHorasDisponibles(MResourceType type){
        if (type.isTimeSlot()){
            //Obtengo la hora en la que se inicia el recurso
            Timestamp init = type.getTimeSlotStart();
            //Obtengo la hora en la que termina de usarse el recurso
            Timestamp end = type.getTimeSlotEnd();
            //Creo un calendario para calcular la diferencia de horas
            TimeZone est = TimeZone.getTimeZone("GM-3");
            Calendar inicio = Calendar.getInstance();
            Calendar fin = Calendar.getInstance();
            //seteo los milisegundos de ambos calendarios
            inicio.setTimeInMillis(init.getTime());
            fin.setTimeInMillis(end.getTime());
            //inicio.get(Calendar.HOUR_OF_DAY);
            //hora de inicio
            BigDecimal i = new BigDecimal(inicio.get(Calendar.HOUR_OF_DAY));
            //hora de fin
            BigDecimal f = new BigDecimal(fin.get(Calendar.HOUR_OF_DAY));
            //calculo la diferencia de horas
            f = f.subtract(i);
            return f;
        }
        return new BigDecimal(24);
    }

     /* Bision - 15/07/2008 - Santiago Ibaï¿½ez
     * Funciï¿½n que retorna dada una unidad de tiempo su equivalente en segundos
     * */
     private BigDecimal getSeconds(int S_Resource_ID){
        MResource resource = new MResource(getCtx(),S_Resource_ID,get_TrxName());
        MUOM uom = getResourceUOM(S_Resource_ID);
        //si la unidad del recurso es Dï¿½a

        
        //03-03-2011 Camarzana Mariano
        // Verifico que contenga el recurso
        if (S_Resource_ID == 0)
        	return BigDecimal.ZERO;
        if ( uom.isDay() ){
            //Bision 12/08/08 Santiago Ibaï¿½ez
            //Cuando es dï¿½a, no se consideran 24hs sino las horas en las que
            //el recurso permanece activo.
            //obtengo el tipo del recurso
            MResourceType type = resource.getResourceType();
            BigDecimal horas = new BigDecimal(3600);
            BigDecimal i = getHorasDisponibles(type);
            return horas.multiply(i);
        }
        //si la unidad del recurso es Horas
        else if ( uom.isHour() )
             return new BigDecimal (3600);
         //si la unidad del recurso es Minutos
         else  if ( uom.isMinute() )
             return new BigDecimal (60);
         //si la unidad del recurso es Mes
         else if (uom.isMonth()){
             //obtengo el tipo de recurso
             MResourceType type = resource.getResourceType();
             //en una hora hay 3600 segundos
             BigDecimal segundosPorHora = new BigDecimal(3600);
             //obtengo la cantidad de dï¿½as que estï¿½ disponible en el mes
             BigDecimal i = getDiasDisponibles(type,4);
             //multiplico los dï¿½as por las horas disponibles por dï¿½a
             i = i.multiply(getHorasDisponibles(type));
             //normalizo a segundos
             i = i.multiply(segundosPorHora);
             return i;
         }
         //si la unidad del recurso es Semanas
         else if (uom.isWeek()){
            //obtengo el tipo de recurso
            MResourceType type = resource.getResourceType();
            return getDiasDisponibles(type,1).multiply(getHorasDisponibles(type)).multiply(new BigDecimal(3600));
         }
         //si la unidad del recurso es 'dï¿½a de trabajo' = 8 hs
         else if (uom.isWorkDay()){
            int segs = 8*3600;
            return new BigDecimal(segs);
         }
         //si la unidad del recurso es 'mes de trabajo' = 20 dï¿½as
         else if (uom.isWorkMonth()){
            int segs = 8*3600*20;
            return new BigDecimal(segs);
         }
         else if (uom.isYear()){
             //obtengo el tipo de recurso
             MResourceType type = resource.getResourceType();
             //en una hora hay 3600 segundos
             BigDecimal segundosPorHora = new BigDecimal(3600);
             //obtengo la cantidad de dï¿½as que estï¿½ disponible en el mes
             BigDecimal i = getDiasDisponibles(type,4);
             //multiplico los dï¿½as por las horas disponibles por dï¿½a
             i = i.multiply(getHorasDisponibles(type));
             //normalizo a segundos
             i = i.multiply(segundosPorHora);
             i = i.multiply(new BigDecimal(12));
             return i;
         }
         return new BigDecimal(0);
    }

     /* Bision - 05/08/2008 - Santiago Ibañez
      * Funcion que retorna el importe del costo asociado a un recurso
      * */
     private  BigDecimal getRate(int S_Resource_ID , int AD_Org_ID , int C_AcctSchema_ID ,int M_CostType_ID)
     {
        //obtengo el producto asociado al recurso
        int M_Product_ID = getM_Product_ID(S_Resource_ID);
        //Obtengo todos los costos asociados al producto
        org.eevolution.model.MCost[]  pc = org.eevolution.model.MCost.getElements(M_Product_ID , C_AcctSchema_ID , M_CostType_ID);
        if (pc != null){
            BigDecimal rate = Env.ZERO;
            //Busco el costo 'Costo Estandar de Recursos'
            for (int e = 0 ; e < pc.length ; e ++ )
            {
                MCostElement element = new MCostElement(getCtx(), pc[e].getM_CostElement_ID(),null);
                if (element.getM_CostElement_ID()== getCostElementIDByName("'Costo Estandar de Recursos'")){
                    //obtengo el importe del 'Costo Estandar de Recursos'
                    rate = rate.add(pc[e].getCurrentCostPrice());
                }
            }
            return rate;
        }
        return Env.ZERO;
     }

     private int getM_Product_ID(int S_Resource_ID)
     {
        QueryDB query = new QueryDB("org.compiere.model.X_M_Product");
        String filter = "S_Resource_ID = " + S_Resource_ID;
        java.util.List results = query.execute(filter);
        Iterator select = results.iterator();
        while (select.hasNext())
        {
         X_M_Product M_Product =  (X_M_Product) select.next();
         return M_Product.getM_Product_ID();
        }

        return 0;
     }


     private MMPCProductPlanning get_MMPCProductPlanning(int AD_Org_ID , int M_Product_ID)
     {

         MMPCProductPlanning pp = MMPCProductPlanning.get(getCtx(), AD_Org_ID , M_Product_ID,p_M_Warehouse_ID);
         MProduct M_Product = new MProduct(getCtx(), M_Product_ID,null);



         if ( pp == null )
         {
                  /*  
                   *20-02-201 Camarzana Mariano
                   *Comentado porque siempre debe tener el deposito ya que la planificacion
                   *se calcula en base al mismo 
                   //System.out.println("pp.getAD_Workflow_ID() ............. " + pp.getAD_Workflow_ID());
                    QueryDB  query = new QueryDB("org.compiere.model.X_AD_Workflow");
                    String filter = "Name = '" + M_Product.getName() + "'";
                    java.util.List results = query.execute(filter);
                    Iterator select = results.iterator();
                    while (select.hasNext())
                    {
                     X_AD_Workflow AD_Workflow =  (X_AD_Workflow) select.next();
                     //return AD_Workflow.getAD_Workflow_ID();
                    }*/
        	 
        	 //JOptionPane.showMessageDialog(null,"Debe ingresar una Ubicacion", "Info" , JOptionPane.INFORMATION_MESSAGE);
         }
         else
         {
              return pp;
         }

         return null;

         }

      public void setParamFromOut(int AD_Org_ID,int M_Warehouse_ID,int M_Product_ID,int M_CostType_ID,int C_AcctSchema_ID, String costElementType){


        p_AD_Org_ID = AD_Org_ID;
        p_M_Warehouse_ID =M_Warehouse_ID;
        p_M_Product_ID = M_Product_ID;
        p_M_CostType_ID =M_CostType_ID;
        p_C_AcctSchema_ID = C_AcctSchema_ID;
        //costResource=getCost(costElementType,AD_Org_ID, M_Product_ID, M_CostType_ID, C_AcctSchema_ID);


    }
     public BigDecimal getCostResource(){
        return costResource;
     }


}	//	OrderOpen
