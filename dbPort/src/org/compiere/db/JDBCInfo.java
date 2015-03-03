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
package org.compiere.db;

import java.sql.*;
import java.util.logging.*;
import org.compiere.*;
import org.compiere.util.*;

/**
 *  JDBC Meta Info
 *
 *  @author     Jorg Janke
 *  @version    $Id: JDBCInfo.java,v 1.4 2005/03/11 20:29:00 jjanke Exp $
 */
public class JDBCInfo
{
	/**
	 *  Constructor
	 */
	public JDBCInfo(Connection conn) throws SQLException
	{
		m_md = conn.getMetaData(); 
		log.info(m_md.getDatabaseProductName());
		log.config(m_md.getDatabaseProductVersion());
	//	log.config(m_md.getDatabaseMajorVersion() + "/" + m_md.getDatabaseMinorVersion());
		//
		log.info(m_md.getDriverName());
		log.config(m_md.getDriverVersion());
		log.config(m_md.getDriverMajorVersion() + "/" + m_md.getDriverMinorVersion());
		//
	//	log.info("JDBC = " + m_md.getJDBCMajorVersion() + "/" + m_md.getJDBCMinorVersion());
	}   //  JDBCInfo

	/**	Mata Data				*/
	private DatabaseMetaData	m_md = null;

	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (JDBCInfo.class);
	
	/**
	 * 	List Catalogs
	 */
	public void listCatalogs() throws SQLException
	{
		log.info(m_md.getCatalogTerm() + " -> " + m_md.getCatalogSeparator());
		ResultSet rs = m_md.getCatalogs();
		while (rs.next())
		{
			dump(rs);
		}
	}	//	listCatalogs
	
	/**
	 * 	List Schemas
	 */
	public void listSchemas() throws SQLException
	{
		log.info(m_md.getSchemaTerm());
		ResultSet rs = m_md.getSchemas();
		while (rs.next())
		{
			dump(rs);
		}
	}	//	listSchemas
	
	/**
	 * 	List Types
	 */
	public void listTypes() throws SQLException
	{
		ResultSet rs = m_md.getTypeInfo();
		while (rs.next())
		{
			log.info("");
			dump(rs);
		}
	}	//	listTypes
	
	/**
	 * 	Dump the current row of a Result Set
	 *	@param rs result set
	 */
	public static void dump(ResultSet rs) throws SQLException
	{
		ResultSetMetaData md = rs.getMetaData();
		for (int i = 0; i < md.getColumnCount(); i++)
		{
			int index = i + 1;
			String info = md.getColumnLabel(index);
			String name = md.getColumnName(index);
			if (info == null)
				info = name;
			else if (name != null && !name.equals(info))
				info += " (" + name + ")";
			info += " = " 
				+ rs.getString(index);
			info += " [" + md.getColumnTypeName(index) 
				+ "(" + md.getPrecision(index);
			if (md.getScale(index) != 0)
				info += "," + md.getScale(index);
			info += ")]"; 
			log.fine(info);
		}
	}	//	dump
	
	/**************************************************************************
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		Compiere.startup(true);
		CLogMgt.setLevel(Level.ALL);
		//
		try
		{
			JDBCInfo info = new JDBCInfo(DB.createConnection(true, Connection.TRANSACTION_READ_COMMITTED));
			info.listCatalogs();
			info.listSchemas();
			info.listTypes();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	main

}   //  JDBCInfo
