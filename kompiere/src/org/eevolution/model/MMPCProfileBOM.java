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
//package org.compiere.mfg.model;
package org.eevolution.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;
import org.compiere.model.*;


/**
 * 	Project Model
 *
 *	@author Jorg Janke
 *	@version $Id: MProject.java,v 1.11 2004/05/11 03:38:45 jjanke Exp $
 */
public class MMPCProfileBOM extends X_MPC_ProfileBOM
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Project_ID id
	 */
	public MMPCProfileBOM(Properties ctx, int MPC_ProfileBOM_ID,String trxName)
	{
		super (ctx, MPC_ProfileBOM_ID , trxName);
		if (MPC_ProfileBOM_ID == 0)
		{
		//	setC_Project_ID(0);
		//	setValue (null);
		//	setC_Currency_ID (0);
//			setCommittedAmt (Env.ZERO);
//			setCommittedQty (Env.ZERO);
//			setInvoicedAmt (Env.ZERO);
//			setInvoicedQty (Env.ZERO);
//			setPlannedAmt (Env.ZERO);
//			setPlannedMarginAmt (Env.ZERO);
//			setPlannedQty (Env.ZERO);
//			setProjectBalanceAmt (Env.ZERO);
//		//	setProjectCategory(PROJECTCATEGORY_General);
//			setIsCommitCeiling (false);
//			setIsCommitment (false);
//			setIsSummary (false);
//			setProcessed (false);
                    
		}
	}	//	MProject

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MMPCProfileBOM(Properties ctx, ResultSet rs,String trxName)
	{
		super (ctx, rs ,trxName);
	}	//	MProject

	/**	Cached PL			*/
//	private int		m_M_PriceList_ID = 0;
//
//	/**
//	 * 	Get Project Type as Int (is Button).
//	 *	@return C_ProjectType_ID id
//	 */
//	public int getC_ProjectType_ID_Int()
//	{
//		String pj = super.getC_ProjectType_ID();
//		if (pj == null)
//			return 0;
//		int C_ProjectType_ID = 0;
//		try
//		{
//			C_ProjectType_ID = Integer.parseInt (pj);
//		}
//		catch (Exception ex)
//		{
//			log.error("getC_ProjectType_ID_Int - " + pj, ex);
//		}
//		return C_ProjectType_ID;
//	}	//	getC_ProjectType_ID_Int
//
//	/**
//	 * 	Set Project Type (overwrite r/o)
//	 *	@param C_ProjectType_ID id
//	 */
//	public void setC_ProjectType_ID (int C_ProjectType_ID)
//	{
//		if (C_ProjectType_ID == 0)
//			super.setC_ProjectType_ID (null);
//		else
//			super.setC_ProjectType_ID (String.valueOf(C_ProjectType_ID));
//	}	//	setC_ProjectType_ID

	/**
	 *	String Representation
	 * 	@return info
	 */
//	public String toString()
//	{
//		StringBuffer sb = new StringBuffer ("MProject[").append(getID())
//			.append("-").append(getValue()).append(",ProjectCategory=").append(getProjectCategory())
//			.append("]");
//		return sb.toString();
//	}	//	toString

	/**
	 * 	Get Price List from Price List Version
	 *	@return price list or 0
	 */
//	public int getM_PriceList_ID()
//	{
//		if (getM_PriceList_Version_ID() == 0)
//			return 0;
//		if (m_M_PriceList_ID > 0)
//			return m_M_PriceList_ID;
//		//
//		String sql = "SELECT M_PriceList_ID FROM M_PriceList_Version WHERE M_PriceList_Version_ID=?";
//		m_M_PriceList_ID = DB.getSQLValue(sql, getM_PriceList_Version_ID());
//		return m_M_PriceList_ID;
//	}	//	getM_PriceList_ID
//
//	/**
//	 * 	Set PL Version
//	 *	@param M_PriceList_Version_ID id
//	 */
//	public void setM_PriceList_Version_ID (int M_PriceList_Version_ID)
//	{
//		super.setM_PriceList_Version_ID(M_PriceList_Version_ID);
//		m_M_PriceList_ID = 0;	//	reset
//	}	//	setM_PriceList_Version_ID


	/*************************************************************************/

	/**
	 * 	Get Project Lines
	 *	@return Array of lines
	 */
	public MMPCProfileBOMLine[] getLines()
	{
		ArrayList list = new ArrayList();
		String sql = "SELECT * FROM MPC_ProfileBOMLine WHERE MPC_ProfileBOM_ID=? ORDER BY Value ";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, getMPC_ProfileBOM_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MMPCProfileBOMLine (getCtx(), rs , null));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE,"getLines" + ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		MMPCProfileBOMLine[] retValue = new MMPCProfileBOMLine[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getLines

	/**
	 * 	Get Project Issues
	 *	@return Array of issues
	 */
	public MMPCProfileBOMProduct[] getIngs()
	{
		ArrayList list = new ArrayList();
		String sql = "SELECT * FROM MPC_ProfileBOM_Product WHERE MPC_ProfileBOM_ID=? AND" + getAD_Client_ID();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, getMPC_ProfileBOM_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MMPCProfileBOMProduct (getCtx(), rs,null));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE,"getIssues" +ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		MMPCProfileBOMProduct[] retValue = new MMPCProfileBOMProduct[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getIssues

        public MMPCProfileBOMSelected[] getSel()
	{
		ArrayList list = new ArrayList();
		String sql = "SELECT * FROM MPC_ProfileBOM_Selected WHERE MPC_ProfileBOM_ID=? AND " + getAD_Client_ID();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, getMPC_ProfileBOM_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MMPCProfileBOMSelected (getCtx(), rs,null));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE,"getIssues" + ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		MMPCProfileBOMSelected[] retValue = new MMPCProfileBOMSelected[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getIssues
        
         public MMPCProfileBOMReal[] getReal()
	{
		ArrayList list = new ArrayList();
		String sql = "SELECT * FROM MPC_ProfileBOM_Real WHERE MPC_ProfileBOM_ID=? AND " + getAD_Client_ID();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, getMPC_ProfileBOM_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MMPCProfileBOMReal (getCtx(), rs,null));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE,"getIssues", ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		MMPCProfileBOMReal[] retValue = new MMPCProfileBOMReal[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getIssues

        

	/**
	 * 	Copy Lines/Phase/Task from other Project
	 *	@param project project
	 *	@return number of total lines copied
	 */
	public int copyDetailsFrom (MMPCProfileBOM profile)
	{
//		if (isProcessed() || project == null)
//			return 0;
		int count = copyLinesFrom(profile)
			 +copyPhasesFrom(profile) 
                         +copySelFrom(profile);
		return count;
	}	//	copyDetailsFrom
//
        /**
	 * 	Copy Lines/Phase/Task from other Project
	 *	@param project project
	 *	@return number of total lines copied
	 */
	public int copyFormatosFrom (MMPCProfileBOM profile)
	{
//		if (isProcessed() || project == null)
//			return 0;
		int count = copyLinesFrom(profile)
			 +copyPhasesFrom(profile);
                         
		return count;
	}	//	copyDetailsFrom
        
        
//	/**
//	 * 	Copy Lines From other Project
//	 *	@param project project
//	 *	@return number of lines copied
//	 */
	public int copyLinesFrom (MMPCProfileBOM profile)
	{
//		if (isProcessed() || project == null)
//			return 0;
		int count = 0;
		MMPCProfileBOMLine[] fromLines = profile.getLines();
		for (int i = 0; i < fromLines.length; i++)
		{
			MMPCProfileBOMLine line = new MMPCProfileBOMLine (getCtx(), 0,null);
			PO.copyValues(fromLines[i], line, getAD_Client_ID(), getAD_Org_ID());
			line.setMPC_ProfileBOM_ID(getMPC_ProfileBOM_ID());
//			
                     //   line.setM_Attribute_ID(0);
//			line.setInvoicedQty(Env.ZERO);
//			line.setC_OrderPO_ID(0);
//			line.setC_Order_ID(0);
//			line.setProcessed(false);
			if (line.save())
				count++;
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE,"copyLinesFrom - Lines difference - Project=" + fromLines.length + " <> Saved=" + count);

		return count;
	}	//	copyLinesFrom
        
        public int copyCalculadoFrom (MMPCProfileBOM profile)
	{
//		if (isProcessed() || project == null)
//			return 0;
		int count = 0;
		MMPCProfileBOMReal[] fromReal = profile.getReal();
		for (int i = 0; i < fromReal.length; i++)
		{
			MMPCProfileBOMReal real = new MMPCProfileBOMReal (getCtx(), 0, null);
			PO.copyValues(fromReal[i], real, getAD_Client_ID(), getAD_Org_ID());
			real.setMPC_ProfileBOM_ID(getMPC_ProfileBOM_ID());
//			
                     //   line.setM_Attribute_ID(0);
//			line.setInvoicedQty(Env.ZERO);
//			line.setC_OrderPO_ID(0);
//			line.setC_Order_ID(0);
//			line.setProcessed(false);
			if (real.save())
				count++;
		}
		if (fromReal.length != count)
			log.log(Level.SEVERE,"copyLinesFrom - Lines difference - Project=" + fromReal.length + " <> Saved=" + count);

		return count;
	}	//	copyLinesFrom

//	/**
//	 * 	Copy Phases/Tasks from other Project
//	 *	@param fromProject project
//	 *	@return number of items copied
//	 */
	public int copyPhasesFrom (MMPCProfileBOM profile)
	{
		
		int count = 0;
		int taskCount = 0;
		//	Get Phases
		MMPCProfileBOMProduct[] myPhases = getIngs();
		MMPCProfileBOMProduct[] fromPhases = profile.getIngs();
		//	Copy Phases
		for (int i = 0; i < fromPhases.length; i++)
		{
			//	Check if Phase already exists
			int MPC_ProfileBOM_Product_ID = fromPhases[i].getMPC_ProfileBOM_Product_ID();
			boolean exists = false;
			if (MPC_ProfileBOM_Product_ID == 0)
				exists = false;
			else
			{
				for (int ii = 0; ii < myPhases.length; ii++)
				{
					if (myPhases[ii].getMPC_ProfileBOM_Product_ID() == MPC_ProfileBOM_Product_ID)
					{
						exists = true;
						break;
					}
				}
			}
			//	Phase exist
			if (exists)
				log.info("copyPhasesFrom - Phase already exists here, ignored - " + fromPhases[i]);
			else
			{
				MMPCProfileBOMProduct toPhase = new MMPCProfileBOMProduct (getCtx (), 0,null);
				PO.copyValues (fromPhases[i], toPhase, getAD_Client_ID (), getAD_Org_ID ());
				toPhase.setMPC_ProfileBOM_ID (getMPC_ProfileBOM_ID ());
//				toPhase.setC_Order_ID (0);
//				toPhase.setIsComplete (false);
				if (toPhase.save ())
				{
					count++;
				//	taskCount += toPhase.copyTasksFrom (fromPhases[i]);
				}
			}
		}
		if (fromPhases.length != count)
			log.log(Level.WARNING,"copyPhasesFrom - Count difference - Project=" + fromPhases.length + " <> Saved=" + count);

		return count;
	}	//	copyPhasesFrom
        
        public int copySelFrom (MMPCProfileBOM profile)
	{
		
		int count = 0;
		int taskCount = 0;
		//	Get Phases
		MMPCProfileBOMSelected[] myPhases = getSel();
		MMPCProfileBOMSelected[] fromPhases = profile.getSel();
		//	Copy Phases
		for (int i = 0; i < fromPhases.length; i++)
		{
			//	Check if Phase already exists
			int MPC_ProfileBOM_Selected_ID = fromPhases[i].getMPC_ProfileBOM_Selected_ID();
			boolean exists = false;
			if (MPC_ProfileBOM_Selected_ID == 0)
				exists = false;
			else
			{
				for (int ii = 0; ii < myPhases.length; ii++)
				{
					if (myPhases[ii].getMPC_ProfileBOM_Selected_ID() == MPC_ProfileBOM_Selected_ID)
					{
						exists = true;
						break;
					}
				}
			}
			//	Phase exist
			if (exists)
				log.info("copyPhasesFrom - Phase already exists here, ignored - " + fromPhases[i]);
			else
			{
				MMPCProfileBOMSelected toPhase = new MMPCProfileBOMSelected (getCtx (), 0,null);
				PO.copyValues (fromPhases[i], toPhase, getAD_Client_ID (), getAD_Org_ID ());
				toPhase.setMPC_ProfileBOM_ID (getMPC_ProfileBOM_ID ());
//				toPhase.setC_Order_ID (0);
//				toPhase.setIsComplete (false);
				if (toPhase.save ())
				{
					count++;
				//	taskCount += toPhase.copyTasksFrom (fromPhases[i]);
				}
			}
		}
		if (fromPhases.length != count)
			log.log(Level.SEVERE,"copyPhasesFrom - Count difference - Project=" + fromPhases.length + " <> Saved=" + count);

		return count;
	}	//	copyPhasesFrom

	/**
	 * 	Create new Project by copying
	 * 	@param ctx context
	 *	@param C_Project_ID project
	 * 	@param dateDoc date of the document date
	 *	@return Project
	 */
	public static MMPCProfileBOM copyFrom (Properties ctx, int MPC_ProfileBOM_ID, Timestamp dateDoc)
	{
		MMPCProfileBOM from = new MMPCProfileBOM (ctx, MPC_ProfileBOM_ID,null);
		if (from.getMPC_ProfileBOM_ID() == 0)
			throw new IllegalArgumentException ("From Project not found C_Project_ID=" + MPC_ProfileBOM_ID);
		//
		MMPCProfileBOM to = new MMPCProfileBOM (ctx, 0, null);
		PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.setMPC_ProfileBOM_ID(0);
		//	Set Value with Time
		String Value = to.getValue() + " ";
		String Time = dateDoc.toString();
		int length = Value.length() + Time.length();
		if (length <= 40)
			Value += Time;
		else
			Value += Time.substring (length-40);
		to.setValue(Value);
		//to.setInvoicedAmt(Env.ZERO);
		//to.setProjectBalanceAmt(Env.ZERO);
		//to.setProcessed(false);
		//
		if (!to.save())
			throw new IllegalStateException("Could not create Project");

		if (to.copyDetailsFrom(from) == 0)
			throw new IllegalStateException("Could not create Project Details");

		return to;
	}	//	copyFrom
        
        private boolean flag=false;
        private int AD_Sequence_ID=0;

}	//	MProject
