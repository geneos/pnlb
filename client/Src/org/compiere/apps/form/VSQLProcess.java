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
package org.compiere.apps.form;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.apps.*;
import org.compiere.grid.ed.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Process SQL Commands
 *	
 *  @author Jorg Janke
 *  @version $Id: VSQLProcess.java,v 1.6 2005/10/08 02:03:03 jjanke Exp $
 */
public class VSQLProcess extends CPanel
	implements FormPanel, ActionListener
{
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		log.info( "VSQLProcess.init");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			jbInit();
			frame.getContentPane().add(this, BorderLayout.CENTER);
		//	frame.getContentPane().add(confirmPanel, BorderLayout.SOUTH);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "VSQLProcess.init", e);
		}
	}	//	init

	/**
	 * 	Dispose - Free Resources
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	/**	Log					*/
	private static CLogger	log = CLogger.getCLogger(VSQLProcess.class);

	/** DML Statement		*/
	private static final String[] DML_KEYWORDS = new String[]{
		"SELECT", "UPDATE", "DELETE", "TRUNCATE"
	};

	private BorderLayout mainLayout = new BorderLayout();
	private CPanel northPanel = new CPanel();
	private CLabel sqlLabel = new CLabel("SQL");
	private VText sqlField = new VText("SQL", false, false, true, 3000, 9000);
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout();
	private BorderLayout northLayout = new BorderLayout();
	private CTextArea resultField = new CTextArea(20,60);
	private CButton sqlButton = ConfirmPanel.createProcessButton(true);

	/**
	 * 	Static Init
	 *	@throws Exception
	 */
	void jbInit() throws Exception
	{
		this.setLayout(mainLayout);
		mainLayout.setHgap(5);
		mainLayout.setVgap(5);
		//
		this.add(northPanel, BorderLayout.NORTH);
		northLayout.setHgap(5);
		northLayout.setVgap(5);
		northPanel.setLayout(northLayout);
		sqlLabel.setText("SQL");
		northPanel.add(sqlLabel, BorderLayout.WEST);
		//	
		northPanel.add(sqlField, BorderLayout.CENTER);
		sqlButton.addActionListener(this);	
		northPanel.add(sqlButton, BorderLayout.EAST);
		//
		this.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(centerLayout);
		centerLayout.setHgap(0);
		resultField.setReadWrite(false);		
		centerPanel.add(resultField, BorderLayout.CENTER);
	}	//	jbInit

	/**
	 *	Action Listener
	 *	@param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		resultField.setText(processStatements (sqlField.getText(), false));
	}	//	actionedPerformed
	
	/**
	 * 	Process SQL Statements
	 *	@param sqlStatements one or more statements separated by ;
	 *	@param allowDML allow DML statements
	 *	@return result
	 */
	public static String processStatements (String sqlStatements, boolean allowDML)
	{
		if (sqlStatements == null || sqlStatements.length() == 0)
			return "";
		StringBuffer result = new StringBuffer();
		//
		StringTokenizer st = new StringTokenizer(sqlStatements, ";", false);
		while (st.hasMoreTokens())
		{
			result.append(processStatement(st.nextToken(), allowDML));
			result.append(Env.NL);
		}
		//
		return result.toString();
	}	//	processStatements
	
	/**
	 * 	Process SQL Statements
	 *	@param sqlStatement one statement
	 *	@param allowDML allow DML statements
	 *	@return result
	 */
	public static String processStatement (String sqlStatement, boolean allowDML)
	{
		if (sqlStatement == null)
			return "";
		StringBuffer sb = new StringBuffer();
		char[] chars = sqlStatement.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			char c = chars[i];
			if (Character.isWhitespace(c))
				sb.append(' ');
			else
				sb.append(c);
		}
		String sql = sb.toString().trim();
		if (sql.length() == 0)
			return "";
		//
		StringBuffer result = new StringBuffer("SQL> ")
			.append(sql)
			.append(Env.NL);
		if (!allowDML)
		{
			boolean error = false;
			String SQL = sql.toUpperCase();
			for (int i = 0; i < DML_KEYWORDS.length; i++)
			{
				if (SQL.startsWith(DML_KEYWORDS[i] + " ") 
					|| SQL.indexOf(" " + DML_KEYWORDS[i] + " ") != -1
					|| SQL.indexOf("(" + DML_KEYWORDS[i] + " ") != -1)
				{
					result.append("===> ERROR: Not Allowed Keyword ")
						.append(DML_KEYWORDS[i])
						.append(Env.NL);
					error = true;	
				}
			}
			if (error)
				return result.toString();
		}	//	!allowDML
		
		//	Process
		Connection conn = DB.createConnection(true, Connection.TRANSACTION_READ_COMMITTED);
		Statement stmt = null;
		try
		{
			stmt = conn.createStatement(); 
			boolean OK = stmt.execute(sql);
			int count = stmt.getUpdateCount();
			if (count == -1)
			{
				result.append("---> ResultSet");
			}
			else
				result.append("---> Result=").append(count);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "process statement: " + sql + " - " + e.toString());
			result.append("===> ").append(e.toString());
		}
		
		//	Clean up
		try
		{
			stmt.close();
		}
		catch (SQLException e1)
		{
			log.log(Level.SEVERE, "processStatement - close statement", e1);
		}
		stmt = null;
		try
		{
			conn.close();
		}
		catch (SQLException e2)
		{
			log.log(Level.SEVERE, "processStatement - close connection", e2);
		}
		conn = null;
		//
		result.append(Env.NL);
		return result.toString();
	}	//	processStatement
	
}	//	VSQLProcess
