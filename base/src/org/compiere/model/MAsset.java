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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.servlet.http.*;
import org.compiere.util.*;

/**
 *  Asset Model
 *
 *  @author Jorg Janke
 *  @version $Id: MAsset.java,v 1.33 2005/12/31 06:31:53 jjanke Exp $
 */
public class MAsset extends X_A_Asset
{
	/**
	 * 	Get Asset From Shipment
	 *	@param ctx context
	 *	@param M_InOutLine_ID shipment line
	 *	@return asset or null
	 */
	public static MAsset getFromShipment (Properties ctx, int M_InOutLine_ID, String trxName)
	{
		MAsset retValue = null;
		String sql = "SELECT * FROM A_Asset WHERE M_InOutLine_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_InOutLine_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MAsset (ctx, rs, trxName);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
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
		return retValue;
	}	//	getFromShipment
	
	/**	Logger							*/
	private static CLogger	s_log = CLogger.getCLogger (MAsset.class);

	
	/**************************************************************************
	 * 	Asset Constructor
	 *	@param ctx context
	 *	@param A_Asset_ID asset
	 *	@param trxName transaction name 
	 */
	public MAsset (Properties ctx, int A_Asset_ID, String trxName)
	{
		super (ctx, A_Asset_ID, trxName);
		if (A_Asset_ID == 0)
		{
			setIsDepreciated (false);
			setIsFullyDepreciated (false);
		//	setValue (null);
		//	setName (null);
			setIsInPosession (false);
			setIsOwned (false);
		//	setA_Asset_Group_ID (0);
			setIsDisposed (false);
			setM_AttributeSetInstance_ID(0);
			setQty(Env.ONE);
		}
	}	//	MAsset

	/**
	 * 	Discontinued Asset Constructor - DO NOT USE (but don't delete either)
	 *	@param ctx context
	 *	@param A_Asset_ID asset
	 */
	public MAsset (Properties ctx, int A_Asset_ID)
	{
		this (ctx, A_Asset_ID, null);
	}	//	MAsset

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MAsset (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAsset

	/**
	 * 	Shipment Constructor
	 *	@param shipLine shipment line
	 *	@param deliveryCount 0 or number of delivery
	 */
	public MAsset (MInOut shipment, MInOutLine shipLine, int deliveryCount)
	{
		this (shipment.getCtx(), 0, shipment.get_TrxName());
		setClientOrg(shipment);
		
		setValueNameDescription(shipment, shipLine, deliveryCount);
		//	Header
		setAssetServiceDate(shipment.getMovementDate());
		setIsOwned(false);
		setC_BPartner_ID(shipment.getC_BPartner_ID());
		setC_BPartner_Location_ID(shipment.getC_BPartner_Location_ID());
		setAD_User_ID(shipment.getAD_User_ID());
		
		//	Line
		MProduct product = shipLine.getProduct();
		setM_Product_ID(product.getM_Product_ID());
		setA_Asset_Group_ID(product.getA_Asset_Group_ID());
		//	Guarantee & Version
		setGuaranteeDate(TimeUtil.addDays(shipment.getMovementDate(), product.getGuaranteeDays()));
		setVersionNo(product.getVersionNo());
		if (shipLine.getM_AttributeSetInstance_ID() != 0)
		{
			MAttributeSetInstance asi = new MAttributeSetInstance (getCtx(), shipLine.getM_AttributeSetInstance_ID(), get_TrxName()); 
			setM_AttributeSetInstance_ID(asi.getM_AttributeSetInstance_ID());
			setLot(asi.getLot());
			setSerNo(asi.getSerNo());
		}
		setHelp(shipLine.getDescription());
		if (deliveryCount != 0)
			setQty(shipLine.getMovementQty());
		setM_InOutLine_ID(shipLine.getM_InOutLine_ID());
		
		//	Activate
		MAssetGroup ag = MAssetGroup.get(getCtx(), getA_Asset_Group_ID());
		if (!ag.isCreateAsActive())
			setIsActive(false);
	}	//	MAsset
	
	
	/**	Product Info					*/
	private MProduct		m_product = null;

	/**
	 * 	Set Value Name Description
	 *	@param shipment shipment
	 *	@param line line
	 *	@param deliveryCount
	 */
	public void setValueNameDescription(MInOut shipment, MInOutLine line, 
		int deliveryCount)
	{
		MProduct product = line.getProduct(); 
		MBPartner partner = shipment.getBPartner();
		setValueNameDescription(shipment, deliveryCount, product, partner);
	}	//	setValueNameDescription
	
	/**
	 * 	Set Value, Name, Description
	 *	@param shipment shipment
	 *	@param deliveryCount count
	 *	@param product product
	 *	@param partner partner
	 */
	public void setValueNameDescription (MInOut shipment,  
		int deliveryCount, MProduct product, MBPartner partner)
	{
		String documentNo = "_" + shipment.getDocumentNo();
		if (deliveryCount > 1)
			documentNo += "_" + deliveryCount;
		//	Value
		String value = partner.getValue() + "_" + product.getValue();
		if (value.length() > 40-documentNo.length())
			value = value.substring(0,40-documentNo.length()) + documentNo;
		setValue(value);
		
		//	Name		MProduct.afterSave
		String name = partner.getName() + " - " + product.getName();
		if (name.length() > 60)
			name = name.substring(0,60);
		setName(name);
		//	Description
		String description = product.getDescription();
		setDescription(description);
	}	//	setValueNameDescription
	
	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription

	/**
	 * 	Get Qty
	 *	@return 1 or Qty
	 */
	public BigDecimal getQty ()
	{
		BigDecimal qty = super.getQty();
		if (qty == null || qty.equals(Env.ZERO))
			setQty(Env.ONE);
		return super.getQty();
	}	//	getQty
	
	/**
	 * 	String representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAsset[")
			.append (get_ID ())
			.append("-").append(getValue())
			.append ("]");
		return sb.toString ();
	}	//	toString

	
	/**************************************************************************
	 * 	Get Deliveries
	 * 	@return deliveries
	 */
	public MAssetDelivery[] getDeliveries()
	{
		ArrayList<MAssetDelivery> list = new ArrayList<MAssetDelivery>();

		String sql = "SELECT * FROM A_Asset_Delivery WHERE A_Asset_ID=? ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getA_Asset_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MAssetDelivery(getCtx(), rs, get_TrxName()));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		//
		MAssetDelivery[] retValue = new MAssetDelivery[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getDeliveries

	/**
	 * 	Get Delivery count
	 * 	@return delivery count
	 */
	public int getDeliveryCount()
	{
		String sql = "SELECT COUNT(*) FROM A_Asset_Delivery WHERE A_Asset_ID=?";
		return DB.getSQLValue(get_TrxName(),
			sql, getA_Asset_ID());
	}	//	getDeliveries

	
	/**************************************************************************
	 * 	Can we download.
	 * 	Based on guarantee date and availability of download
	 * 	@return true if downloadable
	 */
	public boolean isDownloadable()
	{
		if (!isActive())
			return false;
			
		//	Guarantee Date
		Timestamp guarantee = getGuaranteeDate();
		if (guarantee == null)
			return false;
		guarantee = TimeUtil.getDay(guarantee);
		Timestamp now = TimeUtil.getDay(System.currentTimeMillis());
		//	valid
		if (!now.after(guarantee))	//	not after guarantee date
		{
			getProduct();
			return m_product != null
				&& m_product.hasDownloads();
		}
		//
		return false;
	}	//	isDownloadable
	
	/**************************************************************************
	 * 	Get Product Version No
	 *	@return VersionNo
	 */
	public String getProductVersionNo()
	{
		return getProduct().getVersionNo();
	}	//	getProductVersionNo

	/**
	 * 	Get Product R_MailText_ID
	 *	@return R_MailText_ID
	 */
	public int getProductR_MailText_ID()
	{
		return getProduct().getR_MailText_ID();
	}	//	getProductR_MailText_ID

	/**
	 * 	Get Product Info
	 */
	private MProduct getProduct()
	{
		if (m_product == null)
			m_product = MProduct.get (getCtx(), getM_Product_ID()); 
		return m_product;
	}	//	getProductInfo

	/**
	 * 	Get Active Addl. Product Downloads
	 *	@return array of downloads
	 */
	public MProductDownload[] getProductDownloads()
	{
		if (m_product == null)
			getProduct();
		if (m_product != null)
			return m_product.getProductDownloads(false);
		return null;
	}	//	getProductDownloads
	
	/**
	 * 	Get Additional Download Names
	 *	@return names
	 */
	public String[] getDownloadNames()
	{
		MProductDownload[] dls = getProductDownloads();
		if (dls != null && dls.length > 0)
		{
			String[] retValue = new String[dls.length];
			for (int i = 0; i < retValue.length; i++)
				retValue[i] = dls[i].getName();
			log.fine("#" + dls.length);
			return retValue;
		}
		return new String[]{};
	}	//	addlDownloadNames
	
	/**
	 * 	Get Additional Download URLs
	 *	@return URLs
	 */
	public String[] getDownloadURLs()
	{
		MProductDownload[] dls = getProductDownloads();
		if (dls != null && dls.length > 0)
		{
			String[] retValue = new String[dls.length];
			for (int i = 0; i < retValue.length; i++)
			{
				String url = dls[i].getDownloadURL();
				int pos = Math.max(url.lastIndexOf('/'), url.lastIndexOf('\\'));
				if (pos != -1)
					url = url.substring(pos+1);
				retValue[i] = url;
			}
			return retValue;
		}
		return new String[]{};
	}	//	addlDownloadURLs
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		getQty();		//	set to 1
		return true;
	}	//	beforeSave
	
	
	/*************************************************************************
	 * 	Confirm Asset EMail Delivery
	 *	@param email email sent
	 * 	@param AD_User_ID recipient
	 */
	public MAssetDelivery confirmDelivery (EMail email, int AD_User_ID)
	{
		setVersionNo(getProductVersionNo());
		MAssetDelivery ad = new MAssetDelivery (this, email, AD_User_ID);
		return ad;
	}	//	confirmDelivery

	/**
	 * 	Confirm Asset Download Delivery
	 *	@param request request
	 * 	@param AD_User_ID recipient
	 */
	public MAssetDelivery confirmDelivery (HttpServletRequest request, int AD_User_ID)
	{
		setVersionNo(getProductVersionNo());
		setLifeUseUnits(getLifeUseUnits()+1);
		MAssetDelivery ad = new MAssetDelivery (this, request, AD_User_ID);
		return ad;
	}	//	confirmDelivery
	
}	//	MAsset
