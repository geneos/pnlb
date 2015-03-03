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
package org.compiere.model;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.util.zip.*;
import org.compiere.util.*;


/**
 *	Archive Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MArchive.java,v 1.9 2005/11/28 03:35:24 jjanke Exp $
 */
public class MArchive extends X_AD_Archive
{
	/**
	 * 	Get Archives
	 *	@param ctx context
	 *	@param whereClause optional where clause (starting with AND)
	 *	@return archives
	 */
	public static MArchive[] get (Properties ctx, String whereClause)
	{
		ArrayList<MArchive> list = new ArrayList<MArchive>();
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM AD_Archive WHERE AD_Client_ID=?";
		if (whereClause != null && whereClause.length() > 0)
			sql += whereClause;
		sql += " ORDER BY Created";
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, Env.getAD_Client_ID(ctx));
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MArchive(ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
		if (list.size() == 0)
			s_log.fine(sql);
		else
			s_log.finer(sql);
		//
		MArchive[] retValue = new MArchive[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	get

	/**	Logger			*/
	private static CLogger s_log = CLogger.getCLogger(MArchive.class);
	
	private Integer		m_inflated = null;
	private Integer		m_deflated = null;
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Archive_ID id
	 */
	public MArchive (Properties ctx, int AD_Archive_ID, String trxName)
	{
		super (ctx, AD_Archive_ID, trxName);
	}	//	MArchive

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MArchive (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MArchive

	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param info print info
	 */
	public MArchive (Properties ctx, PrintInfo info, String trxName)
	{
		this (ctx, 0, trxName);
		setName(info.getName());
		setIsReport(info.isReport());
		//
		setAD_Process_ID(info.getAD_Process_ID());
		setAD_Table_ID(info.getAD_Table_ID());
		setRecord_ID(info.getRecord_ID());
		setC_BPartner_ID(info.getC_BPartner_ID());
	}	//	MArchive

	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MArchive[");
		sb.append(get_ID()).append(",Name=").append(getName());
		if (m_inflated != null)
			sb.append(",Inflated=" + m_inflated);
		if (m_deflated != null)
			sb.append(",Deflated=" + m_deflated);
		sb.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Get Binary Data.
	 * 	(inflate)
	 *	@return inflated data
	 */
	public byte[] getBinaryData()
	{
		byte[] deflatedData = super.getBinaryData();
		m_deflated = null;
		m_inflated = null;
		if (deflatedData == null)
			return null;
		//
		log.fine("ZipSize=" + deflatedData.length);
		m_deflated = new Integer(deflatedData.length);
		if (deflatedData.length == 0)
			return null;

		byte[] inflatedData = null;
		try
		{
			ByteArrayInputStream in = new ByteArrayInputStream(deflatedData);
			ZipInputStream zip = new ZipInputStream (in);
			ZipEntry entry = zip.getNextEntry();
			if (entry != null)	//	just one entry
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[2048];
				int length = zip.read(buffer);
				while (length != -1)
				{
					out.write(buffer, 0, length);
					length = zip.read(buffer);
				}
				//
				inflatedData = out.toByteArray();
				log.fine("Size=" + inflatedData.length + " - zip="
					+ entry.getCompressedSize() + "(" + entry.getSize() + ") "
					+ (entry.getCompressedSize()*100/entry.getSize())+ "%");
				m_inflated = new Integer(inflatedData.length);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			inflatedData = null;
		}
		return inflatedData;
	}	//	getBinaryData
	
	/**
	 * 	Get Data as Input Stream
	 *	@return input stream or null
	 */
	public InputStream getInputStream()
	{
		byte[] inflatedData = getBinaryData();
		if (inflatedData == null)
			return null;
		return new ByteArrayInputStream(inflatedData);
	}	//	getInputStream
	
	/**
	 * 	Save Binary Data.
	 * 	(deflate)
	 *	@param inflatedData inflated data 
	 */
	public void setBinaryData (byte[] inflatedData)
	{
		if (inflatedData == null || inflatedData.length == 0)
			throw new IllegalArgumentException("InflatedData is NULL");
		m_inflated = new Integer(inflatedData.length);
		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
		ZipOutputStream zip = new ZipOutputStream(out);
		zip.setMethod(ZipOutputStream.DEFLATED);
		zip.setLevel(Deflater.BEST_COMPRESSION);
		zip.setComment("compiere");
		//
		byte[] deflatedData = null;
		try
		{
			ZipEntry entry = new ZipEntry("CompiereArchive");
			entry.setTime(System.currentTimeMillis());
			entry.setMethod(ZipEntry.DEFLATED);
			zip.putNextEntry(entry);
			zip.write (inflatedData, 0, inflatedData.length);
			zip.closeEntry();
			log.fine(entry.getCompressedSize() + " (" + entry.getSize() + ") "
					+ (entry.getCompressedSize()*100/entry.getSize())+ "%");
			//
		//	zip.finish();
			zip.close();
			deflatedData = out.toByteArray();
			log.fine("Length=" +  inflatedData.length);
			m_deflated = new Integer(deflatedData.length);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "saveLOBData", e);
			deflatedData = null;
			m_deflated = null;
		}
		super.setBinaryData (deflatedData);
	}	//	setBinaryData
	
	/**
	 * 	Get Created By (User) Name
	 *	@return name
	 */
	public String getCreatedByName()
	{
		String name = "?";
		String sql = "SELECT Name FROM AD_User WHERE AD_User_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, getCreatedBy());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				name = rs.getString(1);
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
		return name;
	}	//	getCreatedByName
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@returntrue if can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Binary Data is Mandatory
		byte[] data = super.getBinaryData();
		if (data == null || data.length == 0)
			return false;
		//
		log.fine(toString());
		return true;
	}	//	beforeSave
	
}	//	MArchive
