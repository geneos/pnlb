/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.model;

import java.util.Iterator;
import org.compiere.model.X_MPC_Cost_Detail;
import java.util.*;
import org.compiere.util.QueryDB;

/**
 * BISion - 29/12/2008
 * @author santiago
 * Clase que modela la tabla MPC_Material_Cost_Detail
 */
public class MMPCCostDetail extends X_MPC_Cost_Detail{
    
    /**************************************************************************
     * 	Default Constructor
     *	@param ctx context
     *	@param MPC_Material_Cost_Detail_ID id
     */
    public MMPCCostDetail (Properties ctx, int MPC_Material_Cost_Detail_ID, String trxName)
    {
            super (ctx, MPC_Material_Cost_Detail_ID, trxName);
    }
    
    /** BISion - 29/12/2008 - Santiago Ibañez
     * Metodo que retorna si existe un detalle de costo de material para la
     * partida del producto de la manufactura dados.
     * @param MPC_Order_ID orden de manufactura
     * @param M_Product_ID producto
     * @param M_AttributeSetInstance_ID partida
     * @param ctx contexto
     * @param trxName transaccion
     * @return
     */
    public static X_MPC_Cost_Detail getMaterialCostDetail(int MPC_Order_ID,int M_Product_ID, int M_AttributeSetInstance_ID,int MPC_Cost_Element_ID,Properties ctx,String trxName){
        QueryDB query = new QueryDB("org.compiere.model.X_MPC_Cost_Detail");
        String filter = "M_Product_ID = "+M_Product_ID+
                        " AND M_AttributeSetInstance_ID = "+M_AttributeSetInstance_ID+
                        " AND MPC_Order_ID = "+ MPC_Order_ID+
                        " AND MPC_Cost_Element_ID = "+MPC_Cost_Element_ID;
        String sql = "SELECT * FROM MPC_COST_DETAIL WHERE "+filter;
        
        java.util.List results = query.execute(filter);
        Iterator select = results.iterator();
        if (results.size()>0){
           X_MPC_Cost_Detail x = (X_MPC_Cost_Detail)results.get(0);
           X_MPC_Cost_Detail x2 = (X_MPC_Cost_Detail)select.next();
           return x ;
        }
        return new X_MPC_Cost_Detail(ctx,0,trxName);
    }
     /** BISion - 06/01/2009 - Santiago Ibañez
     * Metodo que retorna si existe un detalle de costo de recursos el producto 
     * de la manufactura dados.
     * @param MPC_Order_ID orden de manufactura
     * @param M_Product_ID producto
     * @param M_AttributeSetInstance_ID partida
     * @param ctx contexto
     * @param trxName transaccion
     * @return
     */
    public static X_MPC_Cost_Detail getResourceCostDetail(int MPC_Order_ID,int M_Product_ID, int MPC_Order_Node_ID,int MPC_Cost_Element_ID,Properties ctx,String trxName){
        QueryDB query = new QueryDB("org.compiere.model.X_MPC_Cost_Detail");
        String filter = "M_Product_ID = "+M_Product_ID+
                        " AND MPC_Order_Node_ID = "+MPC_Order_Node_ID+
                        " AND MPC_Order_ID = "+ MPC_Order_ID+
                        " AND MPC_Cost_Element_ID = "+MPC_Cost_Element_ID;
        java.util.List results = query.execute(filter);
        Iterator select = results.iterator();
        if (results.size()>0){
            X_MPC_Cost_Detail x = (X_MPC_Cost_Detail)select.next();
            return x ;
        }
        return new X_MPC_Cost_Detail(ctx,0,trxName);
    }
}
