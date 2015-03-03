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
import javax.swing.JOptionPane;


import org.compiere.model.*;
import org.compiere.process.*;
import compiere.model.*;
import org.compiere.print.*;
import org.compiere.util.*;
import org.eevolution.model.MMPCOrder;
import org.eevolution.model.MMPCOrderNode;
import org.eevolution.model.MMPCOrderParentOrder;

/**
 *  CreateMMMPCParentOrder
 *
 *	@author Pablo Velazquez (GENEOS)
 *	@version $Id: CreateMMPCParentOrder.java,v 1.6 2014/03/05
 */
public class CreateMMPCParentOrder extends SvrProcess {
    /**	The Order				*/
    private int		p_MPC_Order_ID = 0;
    /**	The Parent Order                        */
    private int         p_MPC_ParentOrder_ID = 0;

    private int     p_PInstance_ID;
    /**
     *  Prepare - e.g., get Parameters.
     */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null)
                ;
            else if (name.equals("MPC_Order_ID"))
                p_MPC_Order_ID = ((BigDecimal)para[i].getParameter()).intValue();
            else if (name.equals("MPC_ParentOrder_ID"))
                p_MPC_ParentOrder_ID = ((BigDecimal)para[i].getParameter()).intValue();
            else
                log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
        }
        p_PInstance_ID = getAD_PInstance_ID();
    }	//	prepare
    
    /**
     *  Perrform process.
     *  @return Message (clear text)
     *  @throws Exception if not successful
     */
    protected String doIt() throws Exception {
        
        //Chequeo que p_MPC_Order_ID sea componente de la LDM de p_MPC_ParentOrder_ID
        MMPCOrder order = new MMPCOrder(getCtx(),p_MPC_Order_ID,get_TrxName());
        MMPCOrder parentOrder = new MMPCOrder(getCtx(),p_MPC_ParentOrder_ID,get_TrxName());
        
        MProduct[] bomProducts = parentOrder.getBOMProducts();
        boolean found = false;
        for (int i=0 ; i<bomProducts.length ; i++ ){
            if ( bomProducts[i].getM_Product_ID() == order.getM_Product_ID() )
                found = true;
        }
        if ( !found )
            throw new IllegalArgumentException("La Orden:"+ order + " no es de un producto componente de la Orden:" + parentOrder );
        
        //Chequeo que no exista un puntero
        MMPCOrder[] childs = MMPCOrderParentOrder.getChilds(getCtx(), p_MPC_ParentOrder_ID, get_TrxName());
        found = false;
        for (int i=0 ; i<childs.length ; i++ ){
            if ( childs[i].getMPC_Order_ID() == order.getMPC_Order_ID() )
                found = true;
        }
        if ( found )
            throw new IllegalArgumentException("Ya existe un puntero entre la orden:"+ order + "y la Orden:" + parentOrder );
        
        //Borro punteros existentes que tengan como padre a p_MPC_ParentOrder_ID
        MMPCOrderParentOrder[] punteros = MMPCOrderParentOrder.getAllForOrder(getCtx(), p_MPC_ParentOrder_ID, get_TrxName());
        for (int i=0 ; i<punteros.length ; i++ ){
            if ( punteros[i].getMPC_PARENTORDER_ID() == p_MPC_ParentOrder_ID )
               if ( !punteros[i].delete(true) )
                   throw new IllegalArgumentException("Error al eliminar punteros hacia niveles superiores de order:"+ parentOrder);
        }
        
        //Creo el puntero
        MMPCOrderParentOrder puntero = new MMPCOrderParentOrder(getCtx(),0,get_TrxName());
        puntero.setMPC_Order_ID(p_MPC_Order_ID);
        puntero.setMPC_PARENTORDER_ID(p_MPC_ParentOrder_ID);
        if ( !puntero.save() )
            throw new IllegalArgumentException("Error al generar el puntero" );
        
        return Msg.translate(Env.getCtx(), "Ok");
        
    }	//	doIt
        
        
}	//	CreateMMMPCParentOrder
