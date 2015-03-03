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
 *  CompletePrintOrder
 *
 *	@author Victor Pèrez
 *	@version $Id: CompletePrintOrder.java,v 1.4 2006/10/09 13:41:45 SIGArg-01 Exp $
 */
public class UncompletePrintOrder extends SvrProcess {
    /**	The Order				*/
    private int		p_MPC_Order_ID = 0;
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
       
        MPrintFormat format = null;
        Language language = Language.getLoginLanguage();		//	Base Language
        java.util.Date today =new java.util.Date();
        java.sql.Timestamp now =new java.sql.Timestamp(today.getTime());
        
        if (p_MPC_Order_ID == 0)
            throw new IllegalArgumentException("Manufactuing Order == 0");
            
        //Obtengo la orden
        MMPCOrder order = new MMPCOrder(getCtx(), p_MPC_Order_ID,get_TrxName());

        //Chequeo que este en estado completo
        if (! order.getDocStatus().equals(DocAction.ACTION_Complete))
            throw new RuntimeException(Msg.translate(Env.getCtx(), "NotCompleted"));
        
        if ( order.getQtyDelivered().compareTo(BigDecimal.ZERO) > 0 )
            throw new RuntimeException(Msg.translate(Env.getCtx(), "ExistDeliveries"));
        
        if ( order.tieneSurtimientos() )
            throw new RuntimeException(Msg.translate(Env.getCtx(), "ExistLineDeliveries"));

        if (!order.uncompleteIt().equals(DocAction.ACTION_Approve))
            throw new RuntimeException(Msg.translate(Env.getCtx(), "NoUncompleted"));                 
        
        order.save();
        //                             //fjv e-evolution Operation Activity Report begin
        try {
            StringBuffer sql=new StringBuffer("SELECT MPC_Order_Node_ID FROM MPC_Order_Node WHERE IsActive='Y' AND  MPC_Order_ID=? Order By Value");
            PreparedStatement pstmt = DB.prepareStatement(sql.toString(),null);
            // pstmt.setInt(1, AD_Client_ID);
            pstmt.setInt(1, p_MPC_Order_ID);
            //pstmt.setInt(2, m_M_PriceList_ID);
            ResultSet rs = pstmt.executeQuery();
            //while (!m_calculated && rsplv.next())
            while (rs.next()) {
                MMPCOrderNode onode =new MMPCOrderNode(Env.getCtx(),rs.getInt(1),null);
                
                /*  Comentado por fviejo */    
                //onode.setDocStatus("IP");
                onode.save();
            }
            rs.close();
            pstmt.close();
        } catch (SQLException enode) {
        }
        //fjv e-evolution Operation Activity Report end
        
        return Msg.translate(Env.getCtx(), "Ok");
        
    }	//	doIt
    
}	//	UncompletePrintOrder
