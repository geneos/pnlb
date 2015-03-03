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
 *  Calendar Period Model
 *
 *	@author Jorg Janke
 *	@version $Id: MPeriod.java,v 1.24 2005/11/05 23:20:52 jjanke Exp $
 */
public class MPeriod extends X_C_Period
{
	/**
	 * 	Get Period from Cache
	 *	@param ctx context
	 *	@param C_Period_ID id
	 *	@return MPeriod
	 */
	public static MPeriod get (Properties ctx, int C_Period_ID, String trxName)
	{
		Integer key = new Integer (C_Period_ID);
		MPeriod retValue = (MPeriod) s_cache.get (key);
		if (retValue != null)
			return retValue;
		//
		retValue = new MPeriod (ctx, C_Period_ID, trxName);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} 	//	get

	/**
	 * 	Find standard Period of DateAcct based on Client Calendar
	 *	@param ctx context
	 *	@param DateAcct date
	 *	@return Period or null
	 */
	public static MPeriod get (Properties ctx, Timestamp DateAcct)
	{
		if (DateAcct == null)
			return null;
		//	Search in Cache first
		Iterator it = s_cache.values().iterator();
		while (it.hasNext())
		{
			MPeriod period = (MPeriod)it.next();
			if (period.isStandardPeriod() && period.isInPeriod(DateAcct))
				return period;
		}

		//	Get it from DB
		MPeriod retValue = null;
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		String sql = "SELECT * "
			+ "FROM C_Period "
			+ "WHERE C_Year_ID IN "
				+ "(SELECT C_Year_ID FROM C_Year WHERE C_Calendar_ID= "
					+ "(SELECT C_Calendar_ID FROM AD_ClientInfo WHERE AD_Client_ID=?))"
			+ " AND ? BETWEEN TRUNC(StartDate) AND TRUNC(EndDate)"
			+ " AND PeriodType='S'";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt (1, AD_Client_ID);
			pstmt.setTimestamp(2, DateAcct);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MPeriod period = new MPeriod(ctx, rs, null);
				Integer key = new Integer (period.getC_Period_ID());
				s_cache.put (key, period);
				if (period.isStandardPeriod())
					retValue = period;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, "(DateAcct)", e);
		}
		if (retValue == null)
			s_log.warning("No Standard Period for " + DateAcct
				+ " (AD_Client_ID=" + AD_Client_ID + ")");
		return retValue;
	}	//	get

    /**
     * Get Next open period for one date
     *	@param ctx context
	 *	@param DateAcct date
	 *	@param DocBaseType base type
	 *	@return Timestamp or null
     */
    public static Timestamp getStartDateNextOpenPeriod(Properties ctx, Timestamp DateAcct, String DocBaseType) {

        if (DateAcct == null) {
			s_log.warning("No DateAcct");
			return null;
		}

		if (DocBaseType == null) {
			s_log.warning("No DocBaseType");
			return null;
		}

        int AD_Client_ID = Env.getAD_Client_ID(ctx);
        Timestamp startDate = null;

		MAcctSchema as = MClient.get(ctx, AD_Client_ID).getAcctSchema();
		if (as != null && as.isAutoPeriodControl()) {
			//	Not in AS current period
			Timestamp today = new Timestamp (System.currentTimeMillis());
			startDate = TimeUtil.addDays(today, as.getPeriod_OpenFuture());
            startDate = TimeUtil.addDays(startDate, 1);
            return startDate;
        }

        //	Get it from DB
		String sql = "SELECT * FROM ("
                + "SELECT p.*"
                + "FROM C_Period p INNER JOIN C_PeriodControl pc ON (p.c_period_id = pc.c_period_id) "
                + "WHERE p.C_Year_ID IN (SELECT C_Year_ID FROM C_Year "
                +                       "WHERE C_Calendar_ID = (SELECT C_Calendar_ID FROM AD_ClientInfo "
                +                                              "WHERE AD_Client_ID=?)) AND "
                +       "p.PeriodType='S' AND pc.DocBaseType = ? AND pc.PeriodStatus = ? AND "
                +       "TRUNC(p.StartDate) > ? "
                + "ORDER BY p.StartDate) "
                +    "WHERE ROWNUM <= 1";

		try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt (1, AD_Client_ID);
            pstmt.setString(2, DocBaseType);
            pstmt.setString(3, MPeriodControl.PERIODSTATUS_Open);
            pstmt.setTimestamp(4, DateAcct);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				MPeriod period = new MPeriod(ctx, rs, null);
				Integer key = new Integer (period.getC_Period_ID());
				s_cache.put (key, period);
				startDate = period.getStartDate();
			}
			rs.close();
			pstmt.close();
			pstmt = null;
        } catch (SQLException e) {
			s_log.log(Level.SEVERE, "(DateAcct)", e);
		}
		if (startDate == null) {
			s_log.warning("No Next Standard Period for " + DateAcct
				+ " (AD_Client_ID=" + AD_Client_ID + ")");
        }
        return startDate;
    }

	/**
	 * 	Find valid standard Period of DateAcct based on Client Calendar
	 *	@param ctx context
	 *	@param DateAcct date
	 *	@return C_Period_ID or 0
	 */
	public static int getC_Period_ID (Properties ctx, Timestamp DateAcct)
	{
		MPeriod period = get (ctx, DateAcct);
		if (period == null)
			return 0;
		return period.getC_Period_ID();
	}	//	getC_Period_ID

	/**
	 * 	Is standard Period Open for Document Base Type
	 *	@param ctx context
	 *	@param DateAcct date
	 *	@param DocBaseType base type
	 *	@return true if open
	 */
	public static boolean isOpen (Properties ctx, Timestamp DateAcct, String DocBaseType)
	{
		if (DateAcct == null)
		{
			s_log.warning("No DateAcct");
			return false;
		}
		if (DocBaseType == null)
		{
			s_log.warning("No DocBaseType");
			return false;
		}
		MPeriod period = MPeriod.get (ctx, DateAcct);
		if (period == null)
		{
			s_log.warning("No Period for " + DateAcct + " (" + DocBaseType + ")");
			return false;
		}
		boolean open = period.isOpen(DocBaseType);
		if (!open)
			s_log.warning(period.getName()
				+ ": Not open for " + DocBaseType + " (" + DateAcct + ")");
		return open;
	}	//	isOpen

	/**
	 * 	Find first Year Period of DateAcct based on Client Calendar
	 *	@param ctx context
	 *	@param DateAcct date
	 *	@return Period
	 */
	public static MPeriod getFirstInYear (Properties ctx, Timestamp DateAcct)
	{
		MPeriod retValue = null;
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		String sql = "SELECT * "
			+ "FROM C_Period "
			+ "WHERE C_Year_ID IN "
				+ "(SELECT p.C_Year_ID "
				+ "FROM AD_ClientInfo c"
				+ " INNER JOIN C_Year y ON (c.C_Calendar_ID=y.C_Calendar_ID)"
				+ " INNER JOIN C_Period p ON (y.C_Year_ID=p.C_Year_ID) "
				+ "WHERE c.AD_Client_ID=?"
				+ "	AND ? BETWEEN StartDate AND EndDate)"
			+ " AND PeriodType='S' "
			+ "ORDER BY StartDate";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt (1, AD_Client_ID);
			pstmt.setTimestamp (2, DateAcct);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())	//	first only
				retValue = new MPeriod(ctx, rs, null);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, "getFirstinYear", e);
		}
		return retValue;
	}	//	getFirstInYear

	/**	Cache							*/
	private static CCache<Integer,MPeriod> s_cache = new CCache<Integer,MPeriod>("C_Period", 10);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (MPeriod.class);


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Period_ID id
	 */
	public MPeriod (Properties ctx, int C_Period_ID, String trxName)
	{
		super (ctx, C_Period_ID, trxName);
		if (C_Period_ID == 0)
		{
		//	setC_Period_ID (0);		//	PK
		//  setC_Year_ID (0);		//	Parent
		//  setName (null);
		//  setPeriodNo (0);
		//  setStartDate (new Timestamp(System.currentTimeMillis()));
			setPeriodType (PERIODTYPE_StandardCalendarPeriod);
		}
	}	//	MPeriod

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MPeriod (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPeriod

	/**
	 * 	Parent constructor
	 *	@param year year
	 *	@param PeriodNo no
	 *	@param name name
	 *	@param startDate start
	 *	@param endDate end
	 */
	public MPeriod (MYear year, int PeriodNo, String name,
		Timestamp startDate,Timestamp endDate)
	{
		this (year.getCtx(), 0, year.get_TrxName());
		setClientOrg(year);
		setC_Year_ID(year.getC_Year_ID());
		setPeriodNo(PeriodNo);
		setName(name);
		setStartDate(startDate);
		setEndDate(endDate);
	}	//	MPeriod


	/**	Period Controls			*/
	private MPeriodControl[] m_controls = null;

	/**
	 * 	Get Period Control
	 *	@param requery requery
	 *	@return period controls
	 */
	public MPeriodControl[] getPeriodControls (boolean requery)
	{
		if (m_controls != null && !requery)
			return m_controls;
		//
		ArrayList<MPeriodControl> list = new ArrayList<MPeriodControl>();
		String sql = "SELECT * FROM C_PeriodControl "
			+ "WHERE C_Period_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, getC_Period_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add (new MPeriodControl (getCtx(), rs, null));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		//
		m_controls = new MPeriodControl[list.size ()];
		list.toArray (m_controls);
		return m_controls;
	}	//	getPeriodControls

	/**
	 * 	Get Period Control
	 *	@param DocBaseType Document Base Type
	 *	@return period control or null
	 */
	public MPeriodControl getPeriodControl (String DocBaseType)
	{
		if (DocBaseType == null)
			return null;
                /*
                 *  Zynnia - Agregado para que ejecute la consulta de los períodos
                 *  Anteriromente el false hacía que vengan los períodos con datos erroneos
                 *  JF
                 * 
                 * 
                 */
                
		getPeriodControls(true);
		for (int i = 0; i < m_controls.length; i++)
		{
		//	log.fine("getPeriodControl - " + 1 + " - " + m_controls[i]);
			if (DocBaseType.equals(m_controls[i].getDocBaseType()))
				return m_controls[i];
		}
		return null;
	}	//	getPeriodControl

	/**
	 * 	Date In Period
	 *	@param date date
	 *	@return true if in period
	 */
	public boolean isInPeriod (Timestamp date)
	{
		if (date == null)
			return false;
		Timestamp dateOnly = TimeUtil.getDay(date);
		Timestamp from = TimeUtil.getDay(getStartDate());
		if (dateOnly.before(from))
			return false;
		Timestamp to = TimeUtil.getDay(getEndDate());
		if (dateOnly.after(to))
			return false;
		return true;
	}	//	isInPeriod

	/**
	 * 	Is Period Open for Doc Base Type
	 *	@param DocBaseType document base type
	 *	@return true if open
	 */
	public boolean isOpen (String DocBaseType)
	{
		MAcctSchema as = MClient.get(getCtx(), getAD_Client_ID()).getAcctSchema();
		if (as != null && as.isAutoPeriodControl())
		{
			if (as.getC_Period_ID() == getC_Period_ID())
				return true;
			//	Not in AS current period
			Timestamp today = new Timestamp (System.currentTimeMillis());
			Timestamp first = TimeUtil.addDays(today, - as.getPeriod_OpenHistory());
			Timestamp last = TimeUtil.addDays(today, as.getPeriod_OpenFuture());
			if (today.before(first))
			{
				log.warning ("Today before first day - " + first);
				return false;
			}
			if (today.after(last))
			{
				log.warning ("Today after last day - " + first);
				return false;
			}
			//	We are OK
			if (isInPeriod(today))
			{
				as.setC_Period_ID(getC_Period_ID());
				as.save();
			}
			return true;
		}

		//	Standard Period Control
		if (DocBaseType == null)
		{
			log.warning(getName() + " - No DocBaseType");
			return false;
		}
		MPeriodControl pc = getPeriodControl (DocBaseType);
		if (pc == null)
		{
			log.warning(getName() + " - Period Control not found for " + DocBaseType);
			return false;
		}
		log.fine(getName() + ": " + DocBaseType);
		return pc.isOpen();
	}	//	isOpen

	/**
	 * 	Standard Period
	 *	@return true if standard calendar perios
	 */
	public boolean isStandardPeriod()
	{
		return PERIODTYPE_StandardCalendarPeriod.equals(getPeriodType());
	}	//	isStandardPeriod


	/**
	 * 	Before Save.
	 * 	Truncate Dates
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Truncate Dates
		Timestamp date = getStartDate();
		if (date != null)
			setStartDate(TimeUtil.getDay(date));
		else
			return false;
		//
		date = getEndDate();
		if (date != null)
			setEndDate(TimeUtil.getDay(date));
		else
			setEndDate(TimeUtil.getMonthLastDay(getStartDate()));
		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord)
		{
		//	SELECT Value FROM AD_Ref_List WHERE AD_Reference_ID=183
			//MDocType[] types = MDocType.getOfClient(getCtx());
			String[] docBaseTypes = MDocType.getDocBase(getCtx());
			int count = 0;
			for (int i = 0; i < docBaseTypes.length; i++)
			{
				//MDocType type = types[i];
				//MPeriodControl pc = new MPeriodControl(this, type.getDocBaseType());
				MPeriodControl pc = new MPeriodControl(this, docBaseTypes[i]);
				if (pc.save())
					count++;
			}
			log.fine("PeriodControl #" + count);
		}
		return success;
	}	//	afterSave


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPeriod[");
		sb.append (get_ID())
			.append("-").append (getName())
			.append(", ").append(getStartDate()).append("-").append(getEndDate())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MPeriod
