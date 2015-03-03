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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.util.*;

/**
 *	Inventory Movement Callouts
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutMovement.java,v 1.3 2005/03/11 20:26:04 jjanke Exp $
 */
public class CalloutMovement extends CalloutEngine
{
	/**
	 *  Product modified
	 * 		Set Attribute Set Instance
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	public String product (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer M_Product_ID = (Integer)value;
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
			return "";
		//	Set Attribute
		if (Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()
			&& Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID") != 0)
			mTab.setValue("M_AttributeSetInstance_ID", new Integer(Env.getContextAsInt(ctx, Env.WINDOW_INFO, Env.TAB_INFO, "M_AttributeSetInstance_ID")));
		else
			mTab.setValue("M_AttributeSetInstance_ID", null);
		return "";
	}   //  product

    public String setQty(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){
        int M_Locator_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_Locator_ID");
        Integer M_AttributeSetInstance_ID = (Integer) value;
        //Si se seleccionó una partida
        if (M_AttributeSetInstance_ID!= null && M_AttributeSetInstance_ID.intValue()!=0){
            BigDecimal qty = getQtyOnHand(M_Locator_ID, M_AttributeSetInstance_ID.intValue());
            mTab.setValue("MovementQty", qty);
        }
        return "";
    }

    private BigDecimal getQtyOnHand(int M_Locator_ID, int M_AttributeSetInstance_ID){
        BigDecimal qty = BigDecimal.ZERO;
        PreparedStatement ps = DB.prepareStatement("select qtyonhand from m_storage where M_Locator_ID = ? and M_AttributeSetInstance_ID = ?", null);
        try {
            ps.setInt(1, M_Locator_ID);
            ps.setInt(2, M_AttributeSetInstance_ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                qty = rs.getBigDecimal(1);
        } catch (SQLException ex) {
            Logger.getLogger(CalloutMovement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return qty;
    }
	
}	//	CalloutMove
