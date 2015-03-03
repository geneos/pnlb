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

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.model.MProduct;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;


/**
 *  UpdateLocation
 *
 *	@author JF
 *	@fecha 03/08/2012 Actualiza direccion en pago.
 * 
 *
 */


public class PruebaTrx {
    
    public static void main(String[] args)
    {
        
        org.compiere.Compiere.startupEnvironment(true);
        Trx trx = Trx.get("PruebaTrx", true);
        // Producto con clave 00000128-M
        MProduct producto = new MProduct(Env.getCtx(),1008843,null);
        
        producto.setDescription("prueba de trx");
        System.out.println(producto.save());
        //System.out.println(producto.save(trx.getTrxName()));
        
        
        
        boolean flag = true;
        
        if (flag == true)
            trx.rollback();
        else
            trx.commit();

        
        
    }
}