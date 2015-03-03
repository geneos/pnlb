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
 * Created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.eevolution.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Forcast Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MForcastLine.java,v 1.11 2005/05/17 05:29:52 vpj-cd Exp $
 */
public class MForecastLine extends  X_M_ForecastLine
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_ForecastLine_ID id
	 */
	public MForecastLine (Properties ctx, int M_ForecastLine_ID, String trxName)
	{
		super (ctx, M_ForecastLine_ID, trxName);
		if (M_ForecastLine_ID == 0)
		{		
		}
		
	}	//	MForcastLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MForecastLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRequisitionLine

	
	
	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
            BigDecimal qty = this.getQty();
            int forecast = this.getM_ForecastLine_ID();
            Boolean success = true;
            String m_processMsg = "";
            //Cheque cantidad Positiva
            System.out.println("Chequeando cantidad positiva en linea de pronostico...");
            if (qty.signum() != 1){
                success = false;
                m_processMsg = "La cantidad de la linea debe ser positiva";
            }

            if (!success)
                JOptionPane.showMessageDialog(null,m_processMsg,"Error - Datos invalidos",JOptionPane.ERROR_MESSAGE);

            return success;
                
                
	}	//	beforeSave
	
	/**
	 * 	After Save.
	 * 	Update Total on Header
	 *	@param newRecord if new record
	 *	@param success save was success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
                //begin e-evolution vpj-cd 10/30/2004
                MMPCMRP.M_ForecastLine(this,get_TrxName(),false);
                //end e-evolution vpj-cd 10/30/2004

		return true;
	}	//	afterSave

	
	/**
	 * 	After Delete
	 *	@param success
	 *	@return true/false
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
        //begin e-evolution vpj-cd 10/30/2004
        MMPCMRP.M_ForecastLine(this,get_TrxName(),true);
        //end e-evolution vpj-cd 10/30/2004
		return true;
	}	//	afterDelete	
	
}	//	MForcastLine
