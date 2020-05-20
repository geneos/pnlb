/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.process;

import java.util.logging.*;
import java.sql.*;
import java.math.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Update existing Inventory Count List with current Book value
 *	
 *  @author Jorg Janke
 *  @version $Id: InventoryCountUpdate.java,v 1.7 2005/09/19 04:49:45 jjanke Exp $
 */
public class InventoryCountUpdate extends SvrProcess {

    /** Physical Inventory		*/
    private int p_M_Inventory_ID = 0;

    /**
     *  Prepare - e.g., get Parameters.
     */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null)
				; else {
                log.log(Level.SEVERE, "Unknown Parameter: " + name);
            }
        }
        p_M_Inventory_ID = getRecord_ID();
    }	//	prepare

    /**
     * 	Process
     *	@return message
     *	@throws Exception
     */
    protected String doIt() throws Exception {
        log.info("M_Inventory_ID=" + p_M_Inventory_ID);
        MInventory inventory = new MInventory(getCtx(), p_M_Inventory_ID, get_TrxName());
        if (inventory.get_ID() == 0) {
            throw new CompiereSystemError("Not found: M_Inventory_ID=" + p_M_Inventory_ID);
        }

        return inventory.inventoryCountUpdate(getAD_User_ID());
    }
    //	doIt

    
}	//	InventoryCountUpdate
