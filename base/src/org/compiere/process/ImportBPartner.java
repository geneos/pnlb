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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Import BPartners from I_BPartner
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImportBPartner.java,v 1.18 2005/10/26 00:37:42 jjanke Exp $
 */
public class ImportBPartner extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Organization to be imported to	*/
	private int				m_AD_Org_ID = 0;
	/** Effective						*/
	private Timestamp		m_DateValue = null;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_Client_ID"))
				m_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
	}	//	prepare


	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + m_AD_Client_ID;

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE I_BPartner "
				+ "WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_BPartner "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(m_AD_Client_ID).append("),"
			+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Reset=" + no);

		//	Set BP_Group
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET GroupValue=(SELECT Value FROM C_BP_Group g WHERE g.IsDefault='Y'"
			+ " AND g.AD_Client_ID=i.AD_Client_ID AND ROWNUM=1) "
			+ "WHERE GroupValue IS NULL AND C_BP_Group_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Group Default=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET C_BP_Group_ID=(SELECT C_BP_Group_ID FROM C_BP_Group g"
			+ " WHERE i.GroupValue=g.Value AND g.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE C_BP_Group_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Group=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner "
			+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Group, ' "
			+ "WHERE C_BP_Group_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.config("Invalid Group=" + no);

		//	Set Country
		/**
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET CountryCode=(SELECT CountryCode FROM C_Country c WHERE c.IsDefault='Y'"
			+ " AND c.AD_Client_ID IN (0, i.AD_Client_ID) AND ROWNUM=1) "
			+ "WHERE CountryCode IS NULL AND C_Country_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Country Default=" + no);
		**/
		//
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET C_Country_ID=(SELECT C_Country_ID FROM C_Country c"
			+ " WHERE i.CountryCode=c.CountryCode AND c.AD_Client_ID IN (0, i.AD_Client_ID)) "
			+ "WHERE C_Country_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Country=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner "
			+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Country, ' "
			+ "WHERE C_Country_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.config("Invalid Country=" + no);

		//	Set Region
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "Set RegionName=(SELECT Name FROM C_Region r"
			+ " WHERE r.IsDefault='Y' AND r.C_Country_ID=i.C_Country_ID"
			+ " AND r.AD_Client_ID IN (0, i.AD_Client_ID) AND ROWNUM=1) "
			+ "WHERE RegionName IS NULL AND C_Region_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Region Default=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "Set C_Region_ID=(SELECT C_Region_ID FROM C_Region r"
			+ " WHERE r.Name=i.RegionName AND r.C_Country_ID=i.C_Country_ID"
			+ " AND r.AD_Client_ID IN (0, i.AD_Client_ID)) "
			+ "WHERE C_Region_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Region=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Region, ' "
			+ "WHERE C_Region_ID IS NULL "
			+ " AND EXISTS (SELECT * FROM C_Country c"
			+ " WHERE c.C_Country_ID=i.C_Country_ID AND c.HasRegion='Y')"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.config("Invalid Region=" + no);

		//	Set Greeting
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET C_Greeting_ID=(SELECT C_Greeting_ID FROM C_Greeting g"
			+ " WHERE i.BPContactGreeting=g.Name AND g.AD_Client_ID IN (0, i.AD_Client_ID)) "
			+ "WHERE C_Greeting_ID IS NULL AND BPContactGreeting IS NOT NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Greeting=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Greeting, ' "
			+ "WHERE C_Greeting_ID IS NULL AND BPContactGreeting IS NOT NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.config("Invalid Greeting=" + no);


		//	Existing BPartner ? Match Value
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner p"
			+ " WHERE i.Value=p.Value AND p.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE C_BPartner_ID IS NULL AND Value IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Found BPartner=" + no);

		//	Existing Contact ? Match Name
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET AD_User_ID=(SELECT AD_User_ID FROM AD_User c"
			+ " WHERE i.ContactName=c.Name AND i.C_BPartner_ID=c.C_BPartner_ID AND c.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE C_BPartner_ID IS NOT NULL AND AD_User_ID IS NULL AND ContactName IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Found Contact=" + no);

		//	Existing Location ? Exact Match
		sql = new StringBuffer ("UPDATE I_BPartner i "
			+ "SET C_BPartner_Location_ID=(SELECT C_BPartner_Location_ID"
			+ " FROM C_BPartner_Location bpl INNER JOIN C_Location l ON (bpl.C_Location_ID=l.C_Location_ID)"
			+ " WHERE i.C_BPartner_ID=bpl.C_BPartner_ID AND bpl.AD_Client_ID=i.AD_Client_ID"
			+ " AND DUMP(i.Address1)=DUMP(l.Address1) AND DUMP(i.Address2)=DUMP(l.Address2)"
			+ " AND DUMP(i.City)=DUMP(l.City) AND DUMP(i.Postal)=DUMP(l.Postal) AND DUMP(i.Postal_Add)=DUMP(l.Postal_Add)"
			+ " AND DUMP(i.C_Region_ID)=DUMP(l.C_Region_ID) AND DUMP(i.C_Country_ID)=DUMP(l.C_Country_ID)) "
			+ "WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Found Location=" + no);

		commit();
		//	-------------------------------------------------------------------
		int noInsert = 0;
		int noUpdate = 0;

		//	Go through Records
		sql = new StringBuffer ("SELECT I_BPartner_ID, C_BPartner_ID,"
			+ "C_BPartner_Location_ID,COALESCE (Address1,Address2,City),"
			+ "AD_User_ID,ContactName "
			+ "FROM I_BPartner "
			+ "WHERE I_IsImported='N'").append(clientCheck);
		try
		{
			/**	Insert BPartner
			PreparedStatement pstmt_insertBPartner = conn.prepareStatement
				("INSERT INTO C_BPartner (C_BPartner_ID,"
				+ "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,"
				+ "Value,Name,Name2,Description,DUNS,TaxID,NAICS,C_BP_Group_ID,IsSummary) "
				+ "SELECT ?,"
				+ "AD_Client_ID,AD_Org_ID,'Y',SysDate,CreatedBy,SysDate,UpdatedBy,"
				+ "Value,Name,Name2,Description,DUNS,TaxID,NAICS,C_BP_Group_ID,'N' "
				+ "FROM I_BPartner "
				+ "WHERE I_BPartner_ID=?");
			*/	
			//	Update BPartner
			PreparedStatement pstmt_updateBPartner = DB.prepareStatement
				("UPDATE C_BPartner "
				+ "SET (Value,Name,Name2,Description,DUNS,TaxID,NAICS,C_BP_Group_ID,Updated,UpdatedBy)="
				+ "(SELECT Value,Name,Name2,Description,DUNS,TaxID,NAICS,C_BP_Group_ID,SysDate,UpdatedBy"
				+ " FROM I_BPartner"
				+ " WHERE I_BPartner_ID=?) "
				+ "WHERE C_BPartner_ID=?", get_TrxName());

			//	Insert Location
			PreparedStatement pstmt_insertLocation = DB.prepareStatement
				("INSERT INTO C_Location (C_Location_ID,"
				+ "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,"
				+ "Address1,Address2,City,Postal,Postal_Add,C_Country_ID,C_Region_ID) "
				+ "SELECT ?,"
				+ "AD_Client_ID,AD_Org_ID,'Y',SysDate,CreatedBy,SysDate,UpdatedBy,"
				+ "Address1,Address2,City,Postal,Postal_Add,C_Country_ID,C_Region_ID "
				+ "FROM I_BPartner "
				+ "WHERE I_BPartner_ID=?", get_TrxName());

		//	PreparedStatement pstmt_updateLocation = conn.prepareStatement
		//		("");

			//	Insert BP Location
			PreparedStatement pstmt_insertBPLocation = DB.prepareStatement
				("INSERT INTO C_BPartner_Location (C_BPartner_Location_ID,"
				+ "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,"
				+ "Name,IsBillTo,IsShipTo,IsPayFrom,IsRemitTo,"
				+ "Phone,Phone2,Fax, C_BPartner_ID,C_Location_ID) "
				+ "SELECT ?,"
				+ "AD_Client_ID,AD_Org_ID,'Y',SysDate,CreatedBy,SysDate,UpdatedBy,"
				+ "City,'Y','Y','Y','Y',"
				+ "Phone,Phone2,Fax, ?,? "
				+ "FROM I_BPartner "
				+ "WHERE I_BPartner_ID=?", get_TrxName());

		//	PreparedStatement pstmt_updateBPLocation = conn.prepareStatement
		//		("");

			//	Insert Contact
			PreparedStatement pstmt_insertBPContact = DB.prepareStatement
				("INSERT INTO AD_User (AD_User_ID,"
				+ "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,"
				+ "C_BPartner_ID,C_BPartner_Location_ID,C_Greeting_ID,"
				+ "Name,Title,Description,Comments,Phone,Phone2,Fax,EMail,Birthday) "
				+ "SELECT ?,"
				+ "AD_Client_ID,AD_Org_ID,'Y',SysDate,CreatedBy,SysDate,UpdatedBy,"
				+ "?,?,C_Greeting_ID,"
				+ "ContactName,Title,ContactDescription,Comments,Phone,Phone2,Fax,EMail,Birthday "
				+ "FROM I_BPartner "
				+ "WHERE I_BPartner_ID=?", get_TrxName());

			//	Update Contact
			PreparedStatement pstmt_updateBPContact = DB.prepareStatement
				("UPDATE AD_User "
				+ "SET (C_Greeting_ID,"
				+ "Name,Title,Description,Comments,Phone,Phone2,Fax,EMail,Birthday,Updated,UpdatedBy)="
				+ "(SELECT C_Greeting_ID,"
				+ "ContactName,Title,ContactDescription,Comments,Phone,Phone2,Fax,EMail,Birthday,SysDate,UpdatedBy"
				+ " FROM I_BPartner WHERE I_BPartner_ID=?) "
				+ "WHERE AD_User_ID=?", get_TrxName());

			//	Set Imported = Y
			PreparedStatement pstmt_setImported = DB.prepareStatement
				("UPDATE I_BPartner SET I_IsImported='Y',"
				+ " C_BPartner_ID=?, C_BPartner_Location_ID=?, AD_User_ID=?, "
				+ " Updated=SysDate, Processed='Y' WHERE I_BPartner_ID=?", get_TrxName());
			//
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int I_BPartner_ID = rs.getInt(1);
				int C_BPartner_ID = rs.getInt(2);
				boolean newBPartner = C_BPartner_ID == 0;
				int C_BPartner_Location_ID = rs.getInt(3);
				boolean newLocation = rs.getString(4) != null;
				int AD_User_ID = rs.getInt(5);
				boolean newContact =  rs.getString(6) != null;
				log.fine( "I_BPartner_ID=" + I_BPartner_ID
					+ ", C_BPartner_ID=" + C_BPartner_ID
					+ ", C_BPartner_Location_ID=" + C_BPartner_Location_ID + " create=" + newLocation
					+ ", AD_User_ID=" + AD_User_ID + " create=" + newContact);


				//	****	Create/Update BPartner
				if (newBPartner)	//	Insert new BPartner
				{
					X_I_BPartner iBP = new X_I_BPartner(getCtx(), I_BPartner_ID, get_TrxName());
					MBPartner bp = new MBPartner(iBP);
					if (bp.save())
					{
						C_BPartner_ID = bp.getC_BPartner_ID();
						log.finest("Insert BPartner");
						noInsert++;
					}
					else
					{
						sql = new StringBuffer ("UPDATE I_BPartner i "
							+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Insert BPartner failed"))
							.append("WHERE I_BPartner_ID=").append(I_BPartner_ID);
						DB.executeUpdate(sql.toString(), get_TrxName());
						continue;
					}
				}
				else				//	Update existing BPartner
				{
					pstmt_updateBPartner.setInt(1, I_BPartner_ID);
					pstmt_updateBPartner.setInt(2, C_BPartner_ID);
					try
					{
						no = pstmt_updateBPartner.executeUpdate();
						log.finest("Update BPartner = " + no);
						noUpdate++;
					}
					catch (SQLException ex)
					{
						log.finest("Update BPartner - " + ex.toString());
						sql = new StringBuffer ("UPDATE I_BPartner i "
							+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Update BPartner: " + ex.toString()))
							.append("WHERE I_BPartner_ID=").append(I_BPartner_ID);
						DB.executeUpdate(sql.toString(), get_TrxName());
						continue;
					}
				}

				//	****	Create/Update BPartner Location
				if (C_BPartner_Location_ID != 0)		//	Update Location
				{
				}
				else if (newLocation)					//	New Location
				{
					int C_Location_ID = 0;
					try
					{
						C_Location_ID = DB.getNextID(m_AD_Client_ID, "C_Location", null);
						if (C_Location_ID <= 0)
							throw new DBException("No NextID (" + C_Location_ID + ")");
						pstmt_insertLocation.setInt(1, C_Location_ID);
						pstmt_insertLocation.setInt(2, I_BPartner_ID);
						//
						no = pstmt_insertLocation.executeUpdate();
						log.finest( "Insert Location = " + no);
					}
					catch (SQLException ex)
					{
						log.finest("Insert Location - " + ex.toString());
						rollback();
						noInsert--;
						sql = new StringBuffer ("UPDATE I_BPartner i "
							+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Insert Location: " + ex.toString()))
							.append("WHERE I_BPartner_ID=").append(I_BPartner_ID);
						DB.executeUpdate(sql.toString(), get_TrxName());
						continue;
					}
					//
					try
					{
						C_BPartner_Location_ID = DB.getNextID(m_AD_Client_ID, "C_BPartner_Location", null);
						if (C_BPartner_Location_ID <= 0)
							throw new DBException("No NextID (" + C_BPartner_Location_ID + ")");
						pstmt_insertBPLocation.setInt(1, C_BPartner_Location_ID);
						pstmt_insertBPLocation.setInt(2, C_BPartner_ID);
						pstmt_insertBPLocation.setInt(3, C_Location_ID);
						pstmt_insertBPLocation.setInt(4, I_BPartner_ID);
						//
						no = pstmt_insertBPLocation.executeUpdate();
						log.finest( "Insert BP Location = " + no);
					}
					catch (Exception ex)
					{
						log.finest("Insert BPLocation - " + ex.toString());
						rollback();
						noInsert--;
						sql = new StringBuffer ("UPDATE I_BPartner i "
							+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Insert BPLocation: " + ex.toString()))
							.append("WHERE I_BPartner_ID=").append(I_BPartner_ID);
						DB.executeUpdate(sql.toString(), get_TrxName());
						continue;
					}
				}

				//	****	Create/Update Contact
				if (AD_User_ID != 0)
				{
					pstmt_updateBPContact.setInt(1, I_BPartner_ID);
					pstmt_updateBPContact.setInt(2, AD_User_ID);
					try
					{
						no = pstmt_updateBPContact.executeUpdate();
						log.finest( "Update BP Contact = " + no);
					}
					catch (SQLException ex)
					{
						log.finest( "Update BP Contact - " + ex.toString());
						rollback();
						noInsert--;
						sql = new StringBuffer ("UPDATE I_BPartner i "
							+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Update BP Contact: " + ex.toString()))
							.append("WHERE I_BPartner_ID=").append(I_BPartner_ID);
						DB.executeUpdate(sql.toString(), get_TrxName());
						continue;
					}
				}
				else if (newContact)					//	New Contact
				{
					try
					{
						AD_User_ID = DB.getNextID(m_AD_Client_ID, "AD_User", null);
						if (AD_User_ID <= 0)
							throw new DBException("No NextID (" + AD_User_ID + ")");
						pstmt_insertBPContact.setInt(1, AD_User_ID);
						pstmt_insertBPContact.setInt(2, C_BPartner_ID);
						if (C_BPartner_Location_ID == 0)
							pstmt_insertBPContact.setNull(3, Types.NUMERIC);
						else
							pstmt_insertBPContact.setInt(3, C_BPartner_Location_ID);
						pstmt_insertBPContact.setInt(4, I_BPartner_ID);
						//
						no = pstmt_insertBPContact.executeUpdate();
						log.finest( "Insert BP Contact = " + no);
					}
					catch (Exception ex)
					{
						log.finest( "Insert BPContact - " + ex.toString());
						rollback();
						noInsert--;
						sql = new StringBuffer ("UPDATE I_BPartner i "
							+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||").append(DB.TO_STRING("Insert BPContact: " + ex.toString()))
							.append("WHERE I_BPartner_ID=").append(I_BPartner_ID);
						DB.executeUpdate(sql.toString(), get_TrxName());
						continue;
					}
				}

				//	Update I_Product
				pstmt_setImported.setInt(1, C_BPartner_ID);
				if (C_BPartner_Location_ID == 0)
					pstmt_setImported.setNull(2, Types.NUMERIC);
				else
					pstmt_setImported.setInt(2, C_BPartner_Location_ID);
				if (AD_User_ID == 0)
					pstmt_setImported.setNull(3, Types.NUMERIC);
				else
					pstmt_setImported.setInt(3, AD_User_ID);
				pstmt_setImported.setInt(4, I_BPartner_ID);
				no = pstmt_setImported.executeUpdate();
				//
				commit();
			}	//	for all I_Product
			rs.close();
			pstmt.close();
			//
		//	pstmt_insertBPartner.close();
			pstmt_updateBPartner.close();
			pstmt_insertLocation.close();
		//	pstmt_updateLocation.close();
			pstmt_insertBPLocation.close();
		//	pstmt_updateBPLocation.close();
			pstmt_insertBPContact.close();
			pstmt_updateBPContact.close();
			pstmt_setImported.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "", e);
			rollback();
		}

		//	Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE I_BPartner "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@C_BPartner_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdate), "@C_BPartner_ID@: @Updated@");
		return "";
	}	//	doIt

}	//	ImportBPartner
