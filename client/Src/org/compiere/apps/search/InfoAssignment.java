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
package org.compiere.apps.search;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.apps.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *  View Assignments and optionally create Resource Assigments
 *
 *  @author     Jorg Janke
 *  @version    $Id: InfoAssignment.java,v 1.14 2005/07/18 03:49:07 jjanke Exp $
 */
public class InfoAssignment extends Info
{
	/**
	 *  Constructor
	 *  @param frame frame
	 *  @param modal modal
	 *  @param WindowNo WindowNo
	 *  @param  value   Query value Name or Value if contains numbers
	 *  @param multiSelection multiple selection
	 *  @param whereClause where clause
	 */
	public InfoAssignment (Frame frame, boolean modal, int WindowNo,
		String value, boolean multiSelection, String whereClause)
	{
		super (frame, modal, WindowNo, "ra", "S_ResourceAssigment_ID",
			multiSelection, whereClause);
		log.info(value);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoAssignment"));
		//
		if (!initLookups())
			return;
		statInit();
		initInfo (value, whereClause);
		//
		int no = p_table.getRowCount();
		setStatusLine(Integer.toString(no) + " " + Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
		setStatusDB(Integer.toString(no));
		//	AutoQuery
	//	if (value != null && value.length() > 0)
	//		executeQuery();
		p_loadedOK = true;

		AEnv.positionCenterWindow(frame, this);
	}   //  InfoAssignment

	//
	private CLabel	labelResourceType = new CLabel(Msg.translate(Env.getCtx(), "S_ResourceType_ID"));
	private VLookup	fieldResourceType;
	private CLabel	labelResource = new CLabel(Msg.translate(Env.getCtx(), "S_Resource_ID"));
	private VLookup	fieldResource;
	private CLabel	labelFrom = new CLabel(Msg.translate(Env.getCtx(), "DateFrom"));
	private VDate	fieldFrom = new VDate(DisplayType.Date);
	private CLabel	labelTo = new CLabel(Msg.translate(Env.getCtx(), "DateTo"));
	private VDate	fieldTo = new VDate(DisplayType.Date);
	private CButton bNew = new CButton();

	/**
	 * 	Initialize Lookups
	 * 	@return true if OK
	 */
	private boolean initLookups()
	{
		try
		{
			int AD_Column_ID = 6851;	//	S_Resource.S_ResourceType_ID
			Lookup lookup = MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
			fieldResourceType = new VLookup ("S_ResourceType_ID", false, false, true, lookup);
			AD_Column_ID = 6826;		//	S_ResourceAssignment.S_Resource_ID
			lookup = MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
			fieldResource = new VLookup ("S_Resource_ID", false, false, true, lookup);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "InfoAssignment.initLookup");
			return false;
		}
		//
		bNew.setIcon(Env.getImageIcon("New16.gif"));
		return true;
	}	//	initLookups

	/**
	 *	Static Setup - add fields to parameterPanel.
	 *  <pre>
	 * 		ResourceType	Resource	DateTimeFrom	DateTimeTo	New
	 *  </pre>
	 */
	private void statInit()
	{
		parameterPanel.setLayout(new ALayout());
		parameterPanel.add(labelResourceType, new ALayoutConstraint(0,0));
		parameterPanel.add(labelResource, null);
		parameterPanel.add(labelFrom, null);
		parameterPanel.add(labelTo, null);
	//	parameterPanel.add(labelPhone, null);
	//	parameterPanel.add(checkFuzzy, null);
		//
		parameterPanel.add(fieldResourceType, new ALayoutConstraint(1,0));
		parameterPanel.add(fieldResource, null);
		parameterPanel.add(fieldFrom, null);
		parameterPanel.add(fieldTo, null);
		parameterPanel.add(bNew, null);
	//	parameterPanel.add(checkCustomer, null);
	}	//	statInit

	/**
SELECT rt.Name, r.Name, ra.AssignDateFrom, ra.AssignDateTo, ra.Qty, uom.UOMSymbol, ra.IsConfirmed
FROM S_ResourceAssignment ra, S_ResourceType rt, S_Resource r, C_UOM uom
WHERE ra.IsActive='Y'
AND ra.S_Resource_ID=r.S_Resource_ID
AND r.S_ResourceType_ID=rt.S_ResourceType_ID
AND rt.C_UOM_ID=uom.C_UOM_ID
	 */

	/** From Clause             */
	private static String s_assignmentFROM =
		"S_ResourceAssignment ra, S_ResourceType rt, S_Resource r, C_UOM uom";
	private static String s_assignmentWHERE =
		"ra.IsActive='Y' AND ra.S_Resource_ID=r.S_Resource_ID "
		+ "AND r.S_ResourceType_ID=rt.S_ResourceType_ID AND rt.C_UOM_ID=uom.C_UOM_ID";

	/**  Array of Column Info    */
	private static Info_Column[] s_assignmentLayout = {
		new Info_Column(" ", "ra.S_ResourceAssignment_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "S_ResourceType_ID"), "rt.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "S_Resource_ID"), "r.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "AssignDateFrom"), "ra.AssignDateFrom", Timestamp.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Qty"), "ra.Qty", Double.class),
		new Info_Column(Msg.translate(Env.getCtx(), "C_UOM_ID"), "uom.UOMSymbol", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "AssignDateTo"), "ra.AssignDateTo", Timestamp.class),
		new Info_Column(Msg.translate(Env.getCtx(), "IsConfirmed"), "ra.IsConfirmed", Boolean.class)
	};


	/**
	 *	Dynamic Init
	 *  @param value value
	 *  @param whereClause where clause
	 */
	private void initInfo(String value, String whereClause)
	{
		//  C_BPartner bp, AD_User c, C_BPartner_Location l, C_Location a

		//	Create Grid
		StringBuffer where = new StringBuffer(s_assignmentWHERE);
		if (whereClause != null && whereClause.length() > 0)
			where.append(" AND ").append(whereClause);
		//
		prepareTable(s_assignmentLayout, s_assignmentFROM,
			where.toString(),
			"rt.Name,r.Name,ra.AssignDateFrom");
	}	//	initInfo

	/*************************************************************************/

	/**
	 *  Action Listner
	 *
	 * 	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		//  don't requery if fieldValue and fieldName are empty
		//	return;
		//
		super.actionPerformed(e);
	}   //  actionPerformed

	/*************************************************************************/

	/**
	 *  Get dynamic WHERE part of SQL
	 *	To be overwritten by concrete classes
	 *  @return WHERE clause
	 */
	String getSQLWhere()
	{
		StringBuffer sql = new StringBuffer();
		//
		Integer S_ResourceType_ID = (Integer)fieldResourceType.getValue();
		if (S_ResourceType_ID != null)
			sql.append(" AND rt.S_ResourceType_ID=").append(S_ResourceType_ID.intValue());
		//
		Integer S_Resource_ID = (Integer)fieldResource.getValue();
		if (S_Resource_ID != null)
			sql.append(" AND r.S_Resource_ID=").append(S_Resource_ID.intValue());
		//
		Timestamp ts = fieldFrom.getTimestamp();
		if (ts != null)
			sql.append(" AND TRUNC(ra.AssignDateFrom)>=").append(DB.TO_DATE(ts,false));
		//
		ts = fieldTo.getTimestamp();
		if (ts != null)
			sql.append(" AND TRUNC(ra.AssignDateTo)<=").append(DB.TO_DATE(ts,false));
		return sql.toString();
	}	//	getSQLWhere

	/**
	 *  Set Parameters for Query
	 *	To be overwritten by concrete classes
	 *  @param pstmt pstmt
	 *  @param forCount for counting records
	 *  @throws SQLException
	 */
	void setParameters (PreparedStatement pstmt, boolean forCount) throws SQLException
	{
	}

	/**
	 *  History dialog
	 *	To be overwritten by concrete classes
	 */
	void showHistory()
	{
	}

	/**
	 *  Has History (false)
	 *	To be overwritten by concrete classes
	 *  @return true if it has history (default false)
	 */
	boolean hasHistory()
	{
		return false;
	}

	/**
	 *  Customize dialog
	 *	To be overwritten by concrete classes
	 */
	void customize()
	{
	}

	/**
	 *  Has Customize (false)
	 *	To be overwritten by concrete classes
	 *  @return true if it has customize (default false)
	 */
	boolean hasCustomize()
	{
		return false;
	}

	/**
	 *  Zoom action
	 *	To be overwritten by concrete classes
	 */
	void zoom()
	{
	}

	/**
	 *  Has Zoom (false)
	 *	To be overwritten by concrete classes
	 *  @return true if it has zoom (default false)
	 */
	boolean hasZoom()
	{
		return false;
	}

	/**
	 *  Save Selection Details
	 *	To be overwritten by concrete classes
	 */
	void saveSelectionDetail()
	{
	}

}   //  InfoAssignment
