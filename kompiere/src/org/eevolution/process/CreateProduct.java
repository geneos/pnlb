/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is             Compiere  ERP & CRM Smart Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.eevolution.process;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;


import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.process.*;
import compiere.model.*;

import org.eevolution.form.*;

import org.compiere.grid.ed.*;
import org.eevolution.model.MMPCProfileBOM;

/**
 *  Copy Order Lines
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromOrder.java,v 1.4 2004/05/07 05:52:14 jjanke Exp $
 */
public class CreateProduct extends SvrProcess
{
	/**	The Order				*/
	private int		p_MPC_ProfileBOM_ID = 0;
        private int		m_MPC_ProfileBOM_ID = 0;
        private int m_WindowNo =0;
        private int m_instance=0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{ 
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
                      else if (name.equals("MPC_ProfileBOM_ID"))
				p_MPC_ProfileBOM_ID = ((BigDecimal)para[i].getParameter()).intValue();
                        
			else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		creaprod();
		//
		return "@Copied@@";
	}	//	doIt

         private void creaprod()
         { int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
//           int m_MPC_ProfileBOM_ID= Integer.parseInt(Env.getContext(Env.getCtx(),"#MPC_ProfileBOM_ID"));
//           System.out.println("profilebom " +m_MPC_ProfileBOM_ID);
         MMPCProfileBOM profilebom = new MMPCProfileBOM(Env.getCtx(),p_MPC_ProfileBOM_ID,null);
         //   MMPCProfileBOMSelected profilebomsel = new MMPCProfileBOMSelected(Env.getCtx(),profilebom.getMPC_ProfileBOM_ID());
           //MMPCProductBOM prodbom = new MMPCProductBOM(Env.getCtx(),0);     
          
        //   MPCProfileBOM.atributos(profilebom.getM_Product_ID());
         
           MProduct producto = new MProduct(Env.getCtx(),0,null);
           producto.setValue(profilebom.getValue());
           producto.setName(profilebom.getName());
           producto.setIsPurchased(false);
           producto.setM_Product_Category_ID(profilebom.getM_Product_Category_ID());
           producto.setVersionNo(profilebom.getSpecie());
           producto.setC_UOM_ID(profilebom.getC_UOM_ID());
           producto.setProductType(producto.PRODUCTTYPE_Item);
           producto.setC_TaxCategory_ID(1000001);
           producto.setM_AttributeSet_ID(1000001);
       //    producto.setM_AttributeSetInstance_ID(m_instance);
           producto.save();
        //   prodbom.save();
         }
}	//	CopyFromOrder
