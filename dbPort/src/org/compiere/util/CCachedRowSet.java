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
package org.compiere.util;

import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.sql.rowset.*;
import oracle.jdbc.rowset.*;
import com.sun.rowset.*;


/**
 *	Compiere Cached Row Set Implementation
 *	
 *  @author Jorg Janke
 *  @version $Id: CCachedRowSet.java,v 1.6 2005/12/13 00:16:10 jjanke Exp $
 */
public class CCachedRowSet extends CachedRowSetImpl implements CachedRowSet
{
	/**
	 * 	Get Cached Row Set.
	 * 	Required due to Java Sun bug 393865
	 *	@return Cached Row Set
	 *	@throws SQLException
	 */
	public static CCachedRowSet get() throws SQLException
	{
		CCachedRowSet crs = null;
		//	only first time call
		if (s_loc == null)
		{
			s_loc = Locale.getDefault();
			Locale.setDefault(Locale.US);
			crs = new CCachedRowSet();
			Locale.setDefault(s_loc);
		}
		else
			crs = new CCachedRowSet();
		//
		return crs;
	}	//	get
	
	/**
	 * 	Get Row Set.
	 * 	Read-Only, Scroll Insensitive
	 * 	Need to set parameters and call  execute(Commection)
	 *	@param sql sql
	 *	@return row set
	 *	@throws SQLException
	 */
	public static RowSet getRowSet (String sql) throws SQLException
	{
		CachedRowSet crs = get();
		crs.setConcurrency(ResultSet.CONCUR_READ_ONLY);
		crs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		crs.setCommand(sql);
		//	Set Parameters
	//	crs.execute(conn);
		return crs;
	}	//	get

	/**
	 * 	Get and Execute Row Set.
	 * 	No parameters, Read-Only, Scroll Insensitive
	 *	@param sql sql
	 *	@param conn connection
	 *	@return row set
	 *	@throws SQLException
	 */
	public static RowSet getRowSet (String sql, Connection conn) throws SQLException
	{
		if (DB.isOracle())
		{
			Statement stmt = conn.createStatement
				(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(sql);
			OracleCachedRowSet crs = new OracleCachedRowSet();
			crs.populate(rs);
			stmt.close();
			return crs;
		}
		CachedRowSet crs = get();
		crs.setConcurrency(ResultSet.CONCUR_READ_ONLY);
		crs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		crs.setCommand(sql);
		crs.execute(conn);
		return crs;
	}	//	get
	
	/**
	 * 	Get Cached Row Set.
	 * 	Required due to Java Sun bug 393865. 
	 * 	Also, Oracle NUMBER returns scale -127 
	 *	@return Cached Row Set
	 *	@throws SQLException
	 */
	public static RowSet getRowSet (ResultSet rs) throws SQLException
	{
		if (DB.isOracle())
		{
			OracleCachedRowSet crs = new OracleCachedRowSet();
			crs.populate(rs);
			return crs;
		}
		CachedRowSet crs = get();
		crs.populate(rs);
		return crs;
	}	//	get

	
	/**	Private Locale Marker	*/
	private static Locale s_loc = null;
	
	
	/**************************************************************************
	 * 	Compiere Cached RowSet
	 *	@throws java.sql.SQLException
	 */
	private CCachedRowSet() throws SQLException
	{
		super ();
		setSyncProvider("com.sun.rowset.providers.RIOptimisticProvider");
	}	//	CCachedRowSet

	/**
	 * 	Compiere Cached RowSet
	 *	@param arg0 hashtable
	 *	@throws java.sql.SQLException
	 */
	private CCachedRowSet (Hashtable arg0) throws SQLException
	{
		super (arg0);
		setSyncProvider("com.sun.rowset.providers.RIOptimisticProvider");
	}	//	CCachedRowSet
	
	
	/**************************************************************************
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		try
		{
			Locale.setDefault(Locale.CANADA);
			get();
			System.out.println("OK 1");
			get();
			System.out.println("OK 1a");
			new CachedRowSetImpl();
			System.out.println("OK 2");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	//	main
	
	/**
	 *	To Collection
	 *	@return a <code>Collection</code> object that contains the values in 
	 * 		each row in this <code>CachedRowSet</code> object
	 *	@throws SQLException
	 */
	public Collection<?> toCollection () throws SQLException
	{
		return super.toCollection ();
	}
	/**
	 *	To Collection
	 *	@param column an <code>int</code> indicating the column whose values
	 *	are to be represented in a <code>Collection</code> object
	 *	@return a <code>Collection</code> object that contains the values
	 *	stored in the specified column of this <code>CachedRowSet</code> object
	 *	@throws SQLException
	 */
	public Collection<?> toCollection (int column) throws SQLException
	{
		return super.toCollection (column);
	}
	/**
	 *	To Collection
	 * @param column a <code>String</code> object giving the name of the 
	 *        column whose values are to be represented in a collection
	 * @return a <code>Collection</code> object that contains the values
	 * 		stored in the specified column of this <code>CachedRowSet</code> object
	 * @throws SQLException if an error occurs generating the collection or
	 * 	an invalid column id is provided
	 */
	public Collection<?> toCollection (String column) throws SQLException
	{
		return super.toCollection (column);
	}
}	//	CCachedRowSet
