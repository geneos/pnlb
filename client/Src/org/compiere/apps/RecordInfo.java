/*******************************************************************************
 * The contents of this file are subject to the Compiere License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License. The
 * Original Code is Compiere ERP & CRM Business Solution The Initial Developer
 * of the Original Code is Jorg Janke and ComPiere, Inc. Portions created by
 * Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts created by ComPiere
 * are Copyright (C) ComPiere, Inc.; All Rights Reserved. Contributor(s):
 * ______________________________________.
 ******************************************************************************/
package org.compiere.apps;

import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.table.*;
import org.compiere.grid.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 * Record Info (Who) With Change History
 * 
 * @author Jorg Janke
 * @version $Id: RecordInfo.java,v 1.5 2005/11/14 02:10:58 jjanke Exp $
 */
public class RecordInfo extends CDialog
{
	/**
	 *	Record Info
	 *	@param owner owner
	 *	@param title title
	 *	@param dse data status event
	 */
	public RecordInfo (Frame owner, String title, DataStatusEvent dse)
	{
		super (owner, title, true);
		try
		{
			jbInit ( dynInit(dse, title) );
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		AEnv.positionCenterWindow (owner, this);
	}	//	RecordInfo


	private CPanel	mainPanel	= new CPanel (new BorderLayout(0,0));
	private CPanel	northPanel	= new CPanel ();
	private CScrollPane	scrollPane = new CScrollPane ();
	private VTable table = new VTable ();
	private ConfirmPanel confirmPanel = new ConfirmPanel (false);

	/**	Logger			*/
	protected CLogger		log = CLogger.getCLogger(getClass());
	/** The Data		*/
	private Vector<Vector<String>>	m_data = new Vector<Vector<String>>();
	/** Info			*/
	private StringBuffer	m_info = new StringBuffer();

	
	
	/** Date Time Format		*/
	private SimpleDateFormat	m_dateTimeFormat = DisplayType.getDateFormat
		(DisplayType.DateTime, Env.getLanguage(Env.getCtx()));
	/** Date Format			*/
	private SimpleDateFormat	m_dateFormat = DisplayType.getDateFormat
		(DisplayType.DateTime, Env.getLanguage(Env.getCtx()));
	/** Number Format		*/
	private DecimalFormat		m_numberFormat = DisplayType.getNumberFormat
		(DisplayType.Number, Env.getLanguage(Env.getCtx()));
	/** Amount Format		*/
	private DecimalFormat		m_amtFormat = DisplayType.getNumberFormat
		(DisplayType.Amount, Env.getLanguage(Env.getCtx()));
	/** Number Format		*/
	private DecimalFormat		m_intFormat = DisplayType.getNumberFormat
		(DisplayType.Integer, Env.getLanguage(Env.getCtx()));

	/**
	 * 	Static Layout
	 *	@throws Exception
	 */
	private void jbInit (boolean showTable) throws Exception
	{
		getContentPane().add(mainPanel);
		CTextArea info = new CTextArea(m_info.toString());
		info.setReadWrite(false);
		info.setOpaque(false);	//	transparent
		info.setForeground(Color.blue);
		info.setBorder(null);
		//
		if (showTable)
		{
			mainPanel.add (info, BorderLayout.NORTH);
			mainPanel.add (scrollPane, BorderLayout.CENTER);
			scrollPane.getViewport().add(table);
			scrollPane.setPreferredSize(new Dimension(500,100));
		}
		else
		{
			info.setPreferredSize(new Dimension(400,75));
			mainPanel.add (info, BorderLayout.CENTER);
		}
		//
		mainPanel.add (confirmPanel, BorderLayout.SOUTH);
		confirmPanel.addActionListener(this);
	}	//	jbInit
	
	
	/**
	 * 	Dynamic Init
	 *	@param dse data status event
	 *	@param title title
	 *	@return true if table initialized
	 */
	private boolean dynInit(DataStatusEvent dse, String title)
	{
		if (dse.CreatedBy == null)
			return false;
		//  Info
		MUser user = MUser.get(Env.getCtx(), dse.CreatedBy.intValue());
		m_info.append(" ")
			.append(Msg.translate(Env.getCtx(), "CreatedBy"))
			.append(": ").append(user.getName())
			.append(" - ").append(m_dateTimeFormat.format(dse.Created)).append("\n");
		
		if (!dse.Created.equals(dse.Updated) 
			|| !dse.CreatedBy.equals(dse.UpdatedBy))
		{
			if (!dse.CreatedBy.equals(dse.UpdatedBy))
				user = MUser.get(Env.getCtx(), dse.UpdatedBy.intValue());
			m_info.append(" ")
				.append(Msg.translate(Env.getCtx(), "UpdatedBy"))
				.append(": ").append(user.getName())
				.append(" - ").append(m_dateTimeFormat.format(dse.Updated)).append("\n");
		}
		if (dse.Info != null && dse.Info.length() > 0)
			m_info.append("\n (").append(dse.Info).append(")");
		
		//	Title
		if (dse.AD_Table_ID != 0)
		{
			M_Table table = M_Table.get (Env.getCtx(), dse.AD_Table_ID);
			setTitle(title + " - " + table.getName());
		}

		//	Only Client Preference can view Change Log
		if (!MRole.PREFERENCETYPE_Client.equals(MRole.getDefault().getPreferenceType()))
			return false;
		
		int Record_ID = 0;
		if (dse.Record_ID instanceof Integer)
			Record_ID = ((Integer)dse.Record_ID).intValue();
		else
			log.info("dynInit - Invalid Record_ID=" + dse.Record_ID);
		if (Record_ID == 0)
			return false;
		
		//	Data
		String sql = "SELECT AD_Column_ID, Updated, UpdatedBy, OldValue, NewValue "
			+ "FROM AD_ChangeLog "
			+ "WHERE AD_Table_ID=? AND Record_ID=? "
			+ "ORDER BY Updated DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, dse.AD_Table_ID);
			pstmt.setInt (2, Record_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				addLine (rs.getInt(1), rs.getTimestamp(2), rs.getInt(3),
					rs.getString(4), rs.getString(5));
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
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(Msg.translate(Env.getCtx(), "AD_Column_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "NewValue"));
		columnNames.add(Msg.translate(Env.getCtx(), "OldValue"));
		columnNames.add(Msg.translate(Env.getCtx(), "UpdatedBy"));
		columnNames.add(Msg.translate(Env.getCtx(), "Updated"));
		DefaultTableModel model = new DefaultTableModel(m_data, columnNames);
		table.setModel(model);
		table.autoSize(false);
		return true;
	}	//	dynInit
	
	/**
	 * 	Add Line
	 *	@param AD_Column_ID column
	 *	@param Updated updated
	 *	@param UpdatedBy user
	 *	@param OldValue old
	 *	@param NewValue new
	 */
	private void addLine (int AD_Column_ID, Timestamp Updated, int UpdatedBy,
		String OldValue, String NewValue)
	{
		Vector<String> line = new Vector<String>();
		//	Column
		M_Column column = M_Column.get (Env.getCtx(), AD_Column_ID);
		line.add(column.getName());
		//
		if (OldValue != null && OldValue.equals(MChangeLog.NULL))
			OldValue = null;
		String showOldValue = OldValue;
		if (NewValue != null && NewValue.equals(MChangeLog.NULL))
			NewValue = null;
		String showNewValue = NewValue;
		//
		try
		{
			if (DisplayType.isText (column.getAD_Reference_ID ()))
				;
			else if (column.getAD_Reference_ID() == DisplayType.YesNo)
			{
				if (OldValue != null)
				{
					boolean yes = OldValue.equals("true") || OldValue.equals("Y");
					showOldValue = Msg.getMsg(Env.getCtx(), yes ? "Y" : "N");
				}
				if (NewValue != null)
				{
					boolean yes = NewValue.equals("true") || NewValue.equals("Y");
					showNewValue = Msg.getMsg(Env.getCtx(), yes ? "Y" : "N");
				}
			}
			else if (column.getAD_Reference_ID() == DisplayType.Amount)
			{
				if (OldValue != null)
					showOldValue = m_amtFormat
						.format (new BigDecimal (OldValue));
				if (NewValue != null)
					showNewValue = m_amtFormat
						.format (new BigDecimal (NewValue));
			}
			else if (column.getAD_Reference_ID() == DisplayType.Integer)
			{
				if (OldValue != null)
					showOldValue = m_intFormat.format (new Integer (OldValue));
				if (NewValue != null)
					showNewValue = m_intFormat.format (new Integer (NewValue));
			}
			else if (DisplayType.isNumeric (column.getAD_Reference_ID ()))
			{
				if (OldValue != null)
					showOldValue = m_numberFormat.format (new BigDecimal (OldValue));
				if (NewValue != null)
					showNewValue = m_numberFormat.format (new BigDecimal (NewValue));
			}
			else if (column.getAD_Reference_ID() == DisplayType.Date)
			{
				if (OldValue != null)
					showOldValue = m_dateFormat.format (Timestamp.valueOf (OldValue));
				if (NewValue != null)
					showNewValue = m_dateFormat.format (Timestamp.valueOf (NewValue));
			}
			else if (column.getAD_Reference_ID() == DisplayType.DateTime)
			{
				if (OldValue != null)
					showOldValue = m_dateTimeFormat.format (Timestamp.valueOf (OldValue));
				if (NewValue != null)
					showNewValue = m_dateTimeFormat.format (Timestamp.valueOf (NewValue));
			}
			else if (DisplayType.isLookup(column.getAD_Reference_ID ()))
			{
				MLookup lookup = MLookupFactory.get (Env.getCtx(), 0,
					AD_Column_ID, column.getAD_Reference_ID(),
					Env.getLanguage(Env.getCtx()), column.getColumnName(),
					column.getAD_Reference_Value_ID(),
					column.isParent(), null);
				if (OldValue != null)
				{
					Object key = OldValue; 
					if (column.getAD_Reference_ID() != DisplayType.List)
						key = new Integer(OldValue);
					NamePair pp = lookup.get(key);
					if (pp != null)
						showOldValue = pp.getName();
				}
				if (NewValue != null)
				{
					Object key = NewValue; 
					if (column.getAD_Reference_ID() != DisplayType.List)
						key = new Integer(NewValue);
					NamePair pp = lookup.get(key);
					if (pp != null)
						showNewValue = pp.getName();
				}
			}
			else if (DisplayType.isLOB (column.getAD_Reference_ID ()))
				;
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, OldValue + "->" + NewValue, e);
		}
		//
		line.add(showNewValue);
		line.add(showOldValue);
		//	UpdatedBy
		MUser user = MUser.get(Env.getCtx(), UpdatedBy);
		line.add(user.getName());
		//	Updated
		line.add(m_dateFormat.format(Updated));
		m_data.add(line);
	}	//	addLine
	
	
	/**
	 *	ActionListener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		dispose();
	}	//	actionPerformed

}	// RecordInfo
