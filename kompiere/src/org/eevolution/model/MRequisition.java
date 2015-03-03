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
package org.eevolution.model;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.eevolution.model.MMPCMRP;

/**
 *	Requisition Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRequisition.java,v 1.20 2005/11/12 22:58:56 jjanke Exp $
 */
public class MRequisition extends org.compiere.model.MRequisition 
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Requisition_ID id
	 */
	public MRequisition (Properties ctx, int M_Requisition_ID, String trxName)
	{
		super (ctx, M_Requisition_ID, trxName);
	}	//	MRequisition
        	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRequisition (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRequisition

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		return super.beforeSave (newRecord);
	}	//	beforeSave
        
        /*begin vpj-cd e-evolution 01/25/2005
        /**
         *      After Save
         *      @param newRecord new
         *      @param success success
         */
        protected boolean afterSave (boolean newRecord, boolean success)
        {
                if (!success || newRecord)
                        return success;

                MMPCMRP.M_Requisition(this,get_TrxName());
                /** 23/09/2009 
                 * Modificacion relizada por BISion para que actualicen las líneas
                 * ya que Ej al modificar la fecha de la requisicion tiene que modificar los
                 * registros MPC_MRP asociados a cada una de las líneas de la requisición.
                 */

                 MRequisitionLine lineas[] = getLines();
                 for (int i=0;i<lineas.length;i++)
                     MMPCMRP.M_RequisitionLine(lineas[i], get_TrxName(), false);
                 //fin modificacion BISion
                return super.afterSave (newRecord, success);
        }
        
        /**
    	 * 	Before Delete
    	 *	@return true of it can be deleted
    	 */
    	protected boolean beforeDelete ()
    	{
    		if (isProcessed())
    			return false;
    		
    		org.compiere.model.MRequisitionLine[] m_lines  = getLines();
    		for (int i = 0; i < m_lines.length; i++)
    		{
    			if (!m_lines[i].delete(true))
    			return false;
    		}
    		return super.beforeDelete();
    	}	//	beforeDelete
        //end vpj-cd e-evolution 01/25/2005
        	/** Lines						*/
	private org.eevolution.model.MRequisitionLine[]		m_lines = null;
        /**
	 * 	Get Lines
	 *	@return array of lines
	 */
	public org.eevolution.model.MRequisitionLine[] getLines()
	{
		if (m_lines != null)
			return m_lines;
		
		ArrayList<org.eevolution.model.MRequisitionLine> list = new ArrayList<org.eevolution.model.MRequisitionLine>();
		String sql = "SELECT * FROM M_RequisitionLine WHERE M_Requisition_ID=? ORDER BY Line";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getM_Requisition_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new org.eevolution.model.MRequisitionLine (getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getLines", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		m_lines = new org.eevolution.model.MRequisitionLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines
	
}	//	MRequisition
