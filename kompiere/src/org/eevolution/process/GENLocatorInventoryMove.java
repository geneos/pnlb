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

import org.compiere.model.MLocator;
import org.compiere.model.MMovementLine;
import org.compiere.model.MProduct;
import org.compiere.model.MStorage;
import org.compiere.process.*;
import org.compiere.util.*;
import org.eevolution.model.MMovement;

/**
 *  CreateMMMPCParentOrder
 *
 *	@author Pablo Velazquez (GENEOS)
 *	@version $Id: GENLocatorInventoryMove.java,v 1.6 2016/03/05
 */
public class GENLocatorInventoryMove extends SvrProcess {
    /**	The Origin Locator                              */
    private int		p_M_Locator_ID = 0;
    /**	The Destination Locator                         */
    private int         p_M_Locator_ID_To = 0;

    /**
     *  Prepare - e.g., get Parameters.
     */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null)
                ;
            else if (name.equals("M_Locator_ID"))
                p_M_Locator_ID = para[i].getParameterAsInt();
            else if (name.equals("M_Locator_ID_To"))
                p_M_Locator_ID_To = para[i].getParameterAsInt();
            else
                log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
        }
    }
    
    /**
     *  Perrform process.
     *  @return Message (clear text)
     *  @throws Exception if not successful
     */
    @Override
    protected String doIt() throws Exception {

        MLocator locFrom = new MLocator(getCtx(), p_M_Locator_ID, get_TrxName());
        MLocator locTo = new MLocator(getCtx(), p_M_Locator_ID_To, get_TrxName());
        // Obtengo todos los MStorage para esta ubicacion
        MStorage[] storages = MStorage.getAll(getCtx(), p_M_Locator_ID, get_TrxName());
        MMovement newMov = new MMovement(getCtx(), 0, get_TrxName());
        newMov.setDescription("Movimiento de existencias desde:"+locFrom.getValue()+" hacia: "+locTo.getValue());
        newMov.save();
        for (int i= 0 ; i < storages.length ; i++){
            BigDecimal qtyAvailable = storages[i].getQtyAvailable();
            MProduct aux = new MProduct(getCtx(),storages[i].getM_Product_ID(),get_TrxName());
            if (aux.get_ID() != 0) {
                if (qtyAvailable.signum() == 1){
                    MMovementLine aLine = new MMovementLine(newMov);
                    aLine.setM_Product_ID(storages[i].getM_Product_ID());
                    aLine.setM_Locator_ID(p_M_Locator_ID);
                    aLine.setM_LocatorTo_ID(p_M_Locator_ID_To);
                    aLine.setM_AttributeSetInstance_ID(storages[i].getM_AttributeSetInstance_ID());
                    aLine.setMovementQty(qtyAvailable);
                    if (!aLine.save())
                        log.warning("Error al guardar la linea :"+aLine);
                }
            }
                
        }
        if (!newMov.processIt(DocAction.ACTION_Complete))
            throw new Exception("Error al completar el movimiento: "+newMov.getProcessMsg());
        
        if (!newMov.save()){
            throw new Exception("Error al guardar el movimiento, por favor revisar los logs: "+newMov.getProcessMsg());
        }
        return Msg.translate(Env.getCtx(), "Se creo movimiento: "+newMov.getDocumentInfo());
        
    }	//	doIt
        
        
}	//	GENLocatorInventoryMove
