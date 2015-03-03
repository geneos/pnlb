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
package org.compiere.apps;

import java.awt.event.*;
import java.sql.*;
import java.util.logging.*;
import javax.swing.*;

import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Request Button Action.
 *	Popup Menu
 *	
 *  @author Jorg Janke
 *  @version $Id: ARequest.java,v 1.7 2005/12/09 05:17:57 jjanke Exp $
 */
public class ARequest implements ActionListener
{
	/**
	 * 	Constructor
	 *	@param invoker invoker button
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@param C_BPartner_ID optional bp
	 */
	public ARequest (JComponent invoker, int AD_Table_ID, int Record_ID,
		int C_BPartner_ID)
	{
		super ();
		log.config("AD_Table_ID=" + AD_Table_ID + ", Record_ID=" + Record_ID);
		m_AD_Table_ID = AD_Table_ID;
		m_Record_ID = Record_ID;
		m_C_BPartner_ID = C_BPartner_ID;
		getRequests(invoker);
	}	//	ARequest
	
	/**	The Table						*/
	private int			m_AD_Table_ID;
	/** The Record						*/
	private int			m_Record_ID;
	/** BPartner						*/
	private int			m_C_BPartner_ID;
	
	/**	The Popup						*/
	private JPopupMenu 	m_popup = new JPopupMenu("RequestMenu");
	private CMenuItem 	m_new = null;
	private CMenuItem 	m_active = null;
	private CMenuItem 	m_all = null;
	/** Where Clause					*/
	StringBuffer 		m_where = null;
	
	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (ARequest.class);
	
	/**
	 * 	Display Request Options - New/Existing.
	 * 	@param invoker button
	 */
	private void getRequests (JComponent invoker)
	{
		m_new = new CMenuItem(Msg.getMsg(Env.getCtx(), "RequestNew"));
		m_new.setIcon(Env.getImageIcon("New16.gif"));
		m_popup.add(m_new).addActionListener(this);
		//
		int activeCount = 0;
		int inactiveCount = 0;
		m_where = new StringBuffer();
		m_where.append("(AD_Table_ID=").append(m_AD_Table_ID)
			.append(" AND Record_ID=").append(m_Record_ID)
			.append(")");
		//
		if (m_AD_Table_ID == MUser.getTableId(MUser.Table_Name))
			m_where.append(" OR AD_User_ID=").append(m_Record_ID)
				.append(" OR SalesRep_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MBPartner.getTableId(MBPartner.Table_Name))
			m_where.append(" OR C_BPartner_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MOrder.getTableId(MOrder.Table_Name))
			m_where.append(" OR C_Order_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MInvoice.getTableId(MInvoice.Table_Name))
			m_where.append(" OR C_Invoice_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MPayment.getTableId(MPayment.Table_Name))
			m_where.append(" OR C_Payment_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MProduct.getTableId(MProduct.Table_Name))
			m_where.append(" OR M_Product_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MProject.getTableId(MProject.Table_Name))
			m_where.append(" OR C_Project_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MCampaign.getTableId(MCampaign.Table_Name))
			m_where.append(" OR C_Campaign_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MAsset.getTableId(MAsset.Table_Name))
			m_where.append(" OR A_Asset_ID=").append(m_Record_ID);
		//
		String sql = "SELECT Processed, COUNT(*) "
			+ "FROM R_Request WHERE " + m_where 
			+ " GROUP BY Processed "
			+ "ORDER BY Processed DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				if ("Y".equals(rs.getString(1)))
					inactiveCount = rs.getInt(2);
				else
					activeCount += rs.getInt(2);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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

		//
		if (activeCount > 0)
		{
			m_active = new CMenuItem(Msg.getMsg(Env.getCtx(), "RequestActive") 
				+ " (" + activeCount + ")");
			m_popup.add(m_active).addActionListener(this);
		}
		if (inactiveCount > 0)
		{
			m_all = new CMenuItem(Msg.getMsg(Env.getCtx(), "RequestAll") 
				+ " (" + (activeCount + inactiveCount) + ")");
			m_popup.add(m_all).addActionListener(this);
		}
		//
		if (invoker.isShowing())
			m_popup.show(invoker, 0, invoker.getHeight());	//	below button
	}	//	getZoomTargets
	
	/**
	 * 	Listner
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		MQuery query = null;
		if (e.getSource() == m_active)
		{
			query = new MQuery("");
			String where = "(" + m_where + ") AND Processed='N'";
			query.addRestriction(where);
		}
		else if (e.getSource() == m_all)
		{
			query = new MQuery("");
			query.addRestriction(m_where.toString());
		}
		//
		int AD_Window_ID = 232;		//	232=all - 201=my
		AWindow frame = new AWindow();
		if (!frame.initWindow(AD_Window_ID, query))
			return;
		
		//	New - set Table/Record
		if (e.getSource() == m_new)
		{
			MTab tab = frame.getAPanel().getCurrentTab();
			tab.dataNew (false);
			tab.setValue("AD_Table_ID", new Integer(m_AD_Table_ID));
			tab.setValue("Record_ID", new Integer(m_Record_ID));
			//
			if (m_C_BPartner_ID != 0)
				tab.setValue("C_BPartner_ID", new Integer(m_C_BPartner_ID));
			//
			if (m_AD_Table_ID == MBPartner.getTableId(MBPartner.Table_Name))
				tab.setValue("C_BPartner_ID", new Integer(m_Record_ID));
			else if (m_AD_Table_ID == MUser.getTableId(MUser.Table_Name))
				tab.setValue("AD_User_ID", new Integer(m_Record_ID));
			//
			else if (m_AD_Table_ID == MProject.getTableId(MProject.Table_Name))
				tab.setValue("C_Project_ID", new Integer(m_Record_ID));
			else if (m_AD_Table_ID == MAsset.getTableId(MAsset.Table_Name))
				tab.setValue("A_Asset_ID", new Integer(m_Record_ID));
			//
			else if (m_AD_Table_ID == MOrder.getTableId(MOrder.Table_Name))
				tab.setValue("C_Order_ID", new Integer(m_Record_ID));
			else if (m_AD_Table_ID == MInvoice.getTableId(MInvoice.Table_Name))
				tab.setValue("C_Invoice_ID", new Integer(m_Record_ID));
			//
			else if (m_AD_Table_ID == MProduct.getTableId(MProduct.Table_Name))
				tab.setValue("M_Product_ID", new Integer(m_Record_ID));
			else if (m_AD_Table_ID == MPayment.getTableId(MPayment.Table_Name))
				tab.setValue("C_Payment_ID", new Integer(m_Record_ID));
			//
			else if (m_AD_Table_ID == MInOut.getTableId(MInOut.Table_Name))
				tab.setValue("M_InOut_ID", new Integer(m_Record_ID));
			else if (m_AD_Table_ID == MRMA.getTableId(MRMA.Table_Name))
				tab.setValue("M_RMA_ID", new Integer(m_Record_ID));
			//
			else if (m_AD_Table_ID == MCampaign.getTableId(MCampaign.Table_Name))
				tab.setValue("C_Campaign_ID", new Integer(m_Record_ID));
		}
		AEnv.showCenterScreen(frame);
		frame = null;
	}	//	actionPerformed
	
}	//	ARequest
