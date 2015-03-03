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
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/
package org.eevolution.form;

import java.math.*;
import java.util.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.process.*;
import compiere.model.*;
import  javax.swing.*;
import  java.awt.*;

/**
 *  Copy Order Lines
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromOrder.java,v 1.2 2003/08/11 05:55:38 jjanke Exp $
 */
public class MPCProfileBOMProcess extends SvrProcess
{
	private int		m_MPC_ProfileBOM_ID = 0;
        
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
				m_MPC_ProfileBOM_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
                 int m_MPC_ProfileBOM_ID = getRecord_ID();
                 System.out.println("Valor de Profile1      " + m_MPC_ProfileBOM_ID); 
                 
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		int m_MPC_ProfileBOM_ID = getRecord_ID();
                 System.out.println("Valor de Profile2      " + m_MPC_ProfileBOM_ID);         
                 
                 
                  MPCProfileBOM frame = new MPCProfileBOM(m_MPC_ProfileBOM_ID);
                  String titulo = "";
                  String val = "";
                  String nombre = "";
                  try
                  {
                      StringBuffer sql1 = new StringBuffer("Select Value,Name From MPC_ProfileBOM Where AD_Client_ID=1000000 and MPC_ProfileBOM_ID=?");
    		PreparedStatement pstmt = DB.prepareStatement(sql1.toString());
			pstmt.setInt(1,m_MPC_ProfileBOM_ID);
                     
                        
			ResultSet rs = pstmt.executeQuery();
                         
			//
			if (rs.next())
			{
				 val = rs.getString(1);
                                 nombre = rs.getString(2);
                                 titulo=val.concat(nombre); 
				
			}
			rs.close();
			pstmt.close();
                  }
                  catch(SQLException s)
           {
           }
                  
                  
               //   frame.maximize= true;
               
          
                 frame.pack();
                  frame.setSize(1080,850); 
                  frame.maximize=true;
                  frame.setTitle("Modulo de Formulaci�n "+titulo);
                  frame.setName("Modulo de Formulaci�n");
                  
                 
                      
		//log.info("doIt - From C_File_ID=" + m_C_File_ID + " to " + To_C_File_ID);
	/**	if (To_C_Order_ID == 0)
			throw new IllegalArgumentException("Target C_Order_ID == 0");
		if (m_C_Order_ID == 0)
			throw new IllegalArgumentException("Source C_Order_ID == 0");
		MOrder from = new MOrder (getCtx(), m_C_Order_ID);
		MOrder to = new MOrder (getCtx(), To_C_Order_ID);
		//
		int no = to.copyLinesFrom (from);
	*/	// 
                log.info("doIt - MPC_ProfileBOM_ID=" + m_MPC_ProfileBOM_ID);
		/* if (m_MPC_ProfileBOM_ID == 0)
                    
			throw new IllegalArgumentException("MPC_ProfileBOM_ID == 0");
                
                MPCProfileBOM ff = new MPCProfileBOM(m_MPC_ProfileBOM_ID);
		SwingUtilities.invokeLater(m_updatePB);			//	1
		ff.openForm();
		SwingUtilities.invokeLater(m_updatePB);			//	2
		ff.pack();
		//	Center the window
		SwingUtilities.invokeLater(m_updatePB);			//	3
		AEnv.showCenterScreen(ff);
                */
              // CPanel Profile = new MPCProfileBOM(m_MPC_ProfileBOM_ID);
                //JFrame FrameAgengy = new JFrame();                              
                //FrameAgengy.getContentPane().add(Profile, java.awt.BorderLayout.CENTER);
                //FrameAgengy.show();
                
		return "MPCProfileBOM" +getRecord_ID();
	}	//	doIt

}	//	CopyFromOrder
