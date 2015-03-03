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

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Loaction (Address)
 *	
 *  @author Jorg Janke
 *  @version $Id: MLocation.java,v 1.36 2005/11/28 03:35:24 jjanke Exp $
 */
public class MLocation extends X_C_Location implements Comparator
{
	/**
	 * 	Get Location from Cache
	 *	@param ctx context
	 *	@param C_Location_ID id
	 *	@return MLocation
	 */
	public static MLocation get (Properties ctx, int C_Location_ID, String trxName)
	{
		//	New
		if (C_Location_ID == 0)
			return new MLocation(ctx, C_Location_ID, trxName);
		//
		Integer key = new Integer (C_Location_ID);
		MLocation retValue = (MLocation) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MLocation (ctx, C_Location_ID, trxName);
		if (retValue.get_ID () != 0)		//	found
		{
			s_cache.put (key, retValue);
			return retValue;
		}
		return null;					//	not found
	}	//	get

	/**
	 *	Load Location with ID if Business Partner Location
	 *  @param C_BPartner_Location_ID Business Partner Location
	 *  @return loaction or null
	 */
	public static MLocation getBPLocation (Properties ctx, int C_BPartner_Location_ID, String trxName)
	{
		if (C_BPartner_Location_ID == 0)					//	load default
			return null;

		MLocation loc = null;
		String sql = "SELECT * FROM C_Location l "
			+ "WHERE C_Location_ID=(SELECT C_Location_ID FROM C_BPartner_Location WHERE C_BPartner_Location_ID=?)";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, C_BPartner_Location_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				loc = new MLocation (ctx, rs, trxName);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql + " - " + C_BPartner_Location_ID, e);
			loc = null;
		}
		return loc;
	}	//	getBPLocation

	/**	Cache						*/
	private static CCache<Integer,MLocation> s_cache = new CCache<Integer,MLocation>("C_Location", 100, 30);
	/**	Static Logger				*/
	private static CLogger	s_log = CLogger.getCLogger(MLocation.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Location_ID id
	 */
	public MLocation (Properties ctx, int C_Location_ID, String trxName)
	{
		super (ctx, C_Location_ID, trxName);
		if (C_Location_ID == 0)
		{
			MCountry defaultCountry = MCountry.getDefault(getCtx()); 
			setCountry(defaultCountry);
			MRegion defaultRegion = MRegion.getDefault(getCtx());
			if (defaultRegion != null 
				&& defaultRegion.getC_Country_ID() == defaultCountry.getC_Country_ID())
				setRegion(defaultRegion);
		}
	}	//	MLocation

	/**
	 * 	Parent Constructor
	 *	@param country mandatory country
	 *	@param region optional region
	 */
	public MLocation (MCountry country, MRegion region)
	{
		super (country.getCtx(), 0, country.get_TrxName());
		setCountry (country);
		setRegion (region);
	}	//	MLocation

	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param C_Country_ID country
	 *	@param C_Region_ID region
	 *	@param city city
	 */
	public MLocation (Properties ctx, int C_Country_ID, int C_Region_ID, String city, String trxName)
	{
		super(ctx, 0, trxName);
		setC_Country_ID(C_Country_ID);
		setC_Region_ID(C_Region_ID);
		setCity(city);
	}	//	MLocation

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MLocation (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MLocation

	private 	MCountry		m_c = null;
	private 	MRegion			m_r = null;
	
	/**
	 * 	Set Country
	 *	@param country
	 */
	public void setCountry (MCountry country)
	{
		if (country != null)
			m_c = country;
		else
			m_c = MCountry.getDefault(getCtx());
		super.setC_Country_ID (m_c.getC_Country_ID());
	}	//	setCountry

	/**
	 * 	Set C_Country_ID
	 *	@param C_Country_ID id
	 */
	public void setC_Country_ID (int C_Country_ID)
	{
		if (getC_Country_ID() != C_Country_ID)
			setRegion(null);
		setCountry (MCountry.get(getCtx(), C_Country_ID));
	}	//	setCountry

	/**
	 * 	Get Country
	 *	@return country
	 */
	public MCountry getCountry()
	{
		if (m_c == null)
		{
			if (getC_Country_ID() != 0)
				m_c = MCountry.get(getCtx(), getC_Country_ID());
			else
				m_c = MCountry.getDefault(getCtx());
		}
		return m_c;
	}	//	getCountry
	
	/**
	 * 	Get Country Name
	 *	@return	Country Name
	 */
	public String getCountryName()
	{
		return getCountry().getName();
	}	//	getCountryName
	
	/**
	 * 	Get Country Line
	 * 	@param local if true only foreign country is returned
	 * 	@return country or null
	 */
	public String getCountry (boolean local)
	{
		if (local 
			&& getC_Country_ID() == MCountry.getDefault(getCtx()).getC_Country_ID())
			return null;
		return getCountryName();
	}	//	getCountry

	
	/**
	 * 	Set Region
	 *	@param region
	 */
	public void setRegion (MRegion region)
	{
		m_r = region;
		if (region == null)
			super.setC_Region_ID(0);
		else
		{
			super.setC_Region_ID(m_r.getC_Region_ID());
			if (m_r.getC_Country_ID() != getC_Country_ID())
			{
				log.info("Region(" + region + ") C_Country_ID=" + region.getC_Country_ID()
				  + " - From  C_Country_ID=" + getC_Country_ID());
				setC_Country_ID(region.getC_Country_ID());
			}
		}
	}	//	setRegion

	/**
	 * 	Set C_Region_ID
	 *	@param C_Region_ID region
	 */
	public void setC_Region_ID (int C_Region_ID)
	{
		if (C_Region_ID == 0)
			setRegion(null);
		//	Country defined
		else if (getC_Country_ID() != 0)
		{
			MCountry cc = getCountry();
			if (cc.isValidRegion(C_Region_ID))
				super.setC_Region_ID(C_Region_ID);
			else
				setRegion(null);
		}
		else
			setRegion (MRegion.get(getCtx(), C_Region_ID));
	}	//	setC_Region_ID
	
	/**
	 * 	Get Region
	 *	@return region
	 */
	public MRegion getRegion()
	{
		if (m_r == null && getC_Region_ID() != 0)
			m_r = MRegion.get(getCtx(), getC_Region_ID());
		return m_r;
	}	//	getRegion
	
	/**
	 * 	Get (local) Region Name
	 *	@return	region Name or ""
	 */
	public String getRegionName()
	{
		return getRegionName(false);
	}	//	getRegionName

	/**
	 * 	Get Region Name
	 * 	@param getFromRegion get from region (not locally)
	 *	@return	region Name or ""
	 */
	public String getRegionName (boolean getFromRegion)
	{
		if (getFromRegion && getCountry().isHasRegion() 
			&& getRegion() != null)
		{
			super.setRegionName("");	//	avoid duplicates
			return getRegion().getName();
		}
		//
		String regionName = super.getRegionName();
		if (regionName == null)
			regionName = "";
		return regionName;
	}	//	getRegionName

	
	/**
	 * 	Compares to current record
	 *	@param C_Country_ID if 0 ignored
	 *	@param C_Region_ID if 0 ignored
	 *	@param Postal match postal
	 *	@param Postal_Add match postal add
	 *	@param City match city
	 *	@param Address1 match address 1
	 *	@param Address2 match addtess 2
	 *	@return true if equals
	 */
	public boolean equals (int C_Country_ID, int C_Region_ID, 
		String Postal, String Postal_Add, String City, String Address1, String Address2)
	{
		if (C_Country_ID != 0 && getC_Country_ID() != C_Country_ID)
			return false;
		if (C_Region_ID != 0 && getC_Region_ID() != C_Region_ID)
			return false;
		//	must match
		if (!equalsNull(Postal, getPostal()))
			return false;
		if (!equalsNull(Postal_Add, getPostal_Add()))
			return false;
		if (!equalsNull(City, getCity()))
			return false;
		if (!equalsNull(Address1, getAddress1()))
			return false;
		if (!equalsNull(Address2, getAddress2()))
			return false;
		return true;
	}	//	equals
	
	/**
	 * 	Equals if "" or Null
	 *	@param c1 c1
	 *	@param c2 c2
	 *	@return true if equal (ignore case)
	 */
	private boolean equalsNull (String c1, String c2)
	{
		if (c1 == null)
			c1 = "";
		if (c2 == null)
			c2 = "";
		return c1.equalsIgnoreCase(c2);
	}	//	equalsNull
	
	/**
	 * 	Equals
	 * 	@param cmp comperator
	 * 	@return true if ID the same
	 */
	public boolean equals (Object cmp)
	{
		if (cmp == null)
			return false;
		if (cmp.getClass().equals(this.getClass()))
			return ((PO)cmp).get_ID() == get_ID();
		return equals(cmp);
	}	//	equals

	/**
	 * 	Print Address Reverse Order
	 *	@return true if reverse depending on country
	 */
	public boolean isAddressLinesReverse()
	{
		//	Local
		if (getC_Country_ID() == MCountry.getDefault(getCtx()).getC_Country_ID())
			return getCountry().isAddressLinesLocalReverse();
		return getCountry().isAddressLinesReverse();
	}	//	isAddressLinesReverse

	
	/**
	 * 	Get formatted City Region Postal line
	 * 	@return City, Region Postal
	 */
	public String getCityRegionPostal()
	{
		return parseCRP (getCountry());
	}	//	getCityRegionPostal
	
	/**
	 *	Parse according Ctiy/Postal/Region according to displaySequence.
	 *	@C@ - City		@R@ - Region	@P@ - Postal  @A@ - PostalAdd
	 *  @param c country
	 *  @return parsed String
	 */
	private String parseCRP (MCountry c)
	{
		if (c == null)
			return "CountryNotFound";

		boolean local = getC_Country_ID() == MCountry.getDefault(getCtx()).getC_Country_ID();
		String inStr = local ? c.getDisplaySequenceLocal() : c.getDisplaySequence();
		StringBuffer outStr = new StringBuffer();

		String token;
		int i = inStr.indexOf("@");
		while (i != -1)
		{
			outStr.append (inStr.substring(0, i));			// up to @
			inStr = inStr.substring(i+1, inStr.length());	// from first @

			int j = inStr.indexOf("@");						// next @
			if (j < 0)
			{
				token = "";									//	no second tag
				j = i+1;
			}
			else
				token = inStr.substring(0, j);
			//	Tokens
			if (token.equals("C"))
			{
				if (getCity() != null)
					outStr.append(getCity());
			}
			else if (token.equals("R"))
			{
				if (getRegion() != null)					//	we have a region
					outStr.append(getRegion().getName());
				else if (super.getRegionName() != null && super.getRegionName().length() > 0)
					outStr.append(super.getRegionName());	//	local region name
			}
			else if (token.equals("P"))
			{
				if (getPostal() != null)
					outStr.append(getPostal());
			}
			else if (token.equals("A"))
			{
				String add = getPostal_Add();
				if (add != null && add.length() > 0)
					outStr.append("-").append(add);
			}
			else
				outStr.append("@").append(token).append("@");

			inStr = inStr.substring(j+1, inStr.length());	// from second @
			i = inStr.indexOf("@");
		}
		outStr.append(inStr);						// add the rest of the string

		//	Print Region Name if entered and not part of pattern
		if (c.getDisplaySequence().indexOf("@R@") == -1
			&& super.getRegionName() != null && super.getRegionName().length() > 0)
			outStr.append(" ").append(super.getRegionName());

		String retValue = Util.replace(outStr.toString(), "\\n", "\n");
		log.finest("parseCRP - " + c.getDisplaySequence() + " -> " +  retValue);
		return retValue;
	}	//	parseContext

	
	/**************************************************************************
	 *	Return printable String representation
	 *  @return String
	 */
	public String toString()
	{
		StringBuffer retStr = new StringBuffer();
		if (getAddress1() != null)
			retStr.append(getAddress1());
		else
			if (getAddress2() != null && getAddress2().length() > 0)
				retStr.append(getAddress2());
			else
				if (getAddress3() != null && getAddress3().length() > 0)
					retStr.append(getAddress3());
				else
					if (getAddress4() != null && getAddress4().length() > 0)
						retStr.append(getAddress4());
		
		//	City, Region, Postal
		getCity();
		if (retStr.length() > 0 && getCity() != null)
			retStr.append(", ").append(getCity());
		
		//retStr.append(", ").append(parseCRP (getCountry()));
		
		return retStr.toString();
	}	//	toString

	/**
	 *	Return String representation with CR at line end
	 *  @return String
	 */
	public String toStringCR()
	{
		StringBuffer retStr = new StringBuffer();
		if (getAddress1() != null)
			retStr.append(getAddress1());
		if (getAddress2() != null && getAddress2().length() > 0)
			retStr.append("\n").append(getAddress2());
		if (getAddress3() != null && getAddress3().length() > 0)
			retStr.append("\n").append(getAddress3());
		if (getAddress4() != null && getAddress4().length() > 0)
			retStr.append("\n").append(getAddress4());
		//	City, Region, Postal
		retStr.append("\n").append(parseCRP (getCountry()));
		//	Add Country would come here
		return retStr.toString();
	}	//	toStringCR

	/**
	 *	Return detailed String representation
	 *  @return String
	 */
	public String toStringX()
	{
		StringBuffer sb = new StringBuffer("MLocation=[");
		sb.append(get_ID())
			.append(",C_Country_ID=").append(getC_Country_ID())
			.append(",C_Region_ID=").append(getC_Region_ID())
			.append(",Postal=").append(getPostal())
			.append ("]");
		return sb.toString();
	}   //  toStringX

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		//	Region Check
		if (getC_Region_ID() != 0)
		{
			if (m_c == null || m_c.getC_Country_ID() != getC_Country_ID())
				getCountry();
			if (!m_c.isHasRegion())
				setC_Region_ID(0);
		}
		
		return true;
	}	//	geforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Value/Name change in Account
		if (!newRecord
			&& ("Y".equals(Env.getContext(getCtx(), "$Element_LF")) 
				|| "Y".equals(Env.getContext(getCtx(), "$Element_LT")))
			&& (is_ValueChanged("Postal") || is_ValueChanged("City"))
			)
			MAccount.updateValueDescription(getCtx(), 
				"(C_LocFrom_ID=" + getC_Location_ID() 
				+ " OR C_LocTo_ID=" + getC_Location_ID() + ")", get_TrxName());
		return success;
	}	//	afterSave

}	//	MLocation
