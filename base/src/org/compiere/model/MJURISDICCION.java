/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;

import java.sql.*;
import java.util.*;

import org.compiere.util.*;
/**
 * @author daniel
 */
@SuppressWarnings("serial")
public class MJURISDICCION extends X_C_JURISDICCION{
        /**
	 * 	Get Action permissions from Cache
	 *	@param ctx context
	 *	@param AD_Action_Permission id
	 *	@return MActionPermission
	 */

	public static  MJURISDICCION get (Properties ctx, int C_Jurisdiccion_ID)
	{
		Integer key = new Integer (C_Jurisdiccion_ID);
		MJURISDICCION retValue = (MJURISDICCION) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MJURISDICCION (ctx, C_Jurisdiccion_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,MJURISDICCION>	s_cache	= new CCache<Integer,MJURISDICCION>("C_COBRANZARET", 10);
	
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_Action_Permission_ID id
	 */
	public MJURISDICCION (Properties ctx, int C_Jurisdiccion_ID, String trxName)
	{
		super (ctx, C_Jurisdiccion_ID, trxName);
	}	//	MActionPermission

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MJURISDICCION (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MActionPermission
	
	
	private static CCache<String,String>	s_jurisdicciones = null;
	private static List<String>	l_jurisdicciones = null;
	
	@SuppressWarnings("unchecked")
	public static String[] getJurisdicciones(Properties ctx)
	{
		if (s_jurisdicciones == null || s_jurisdicciones.size() == 0)
			loadAllJurisdicciones(ctx);
		String[] retValue = new String[s_jurisdicciones.size()];
		s_jurisdicciones.values().toArray(retValue);
		return retValue;
	}	//	getCountries
	
	@SuppressWarnings("unchecked")
	public static List<String> getJurisdicciones()
	{
		if (l_jurisdicciones == null || l_jurisdicciones.size() == 0)
			loadAllJurisdicciones(Env.getCtx());
		return l_jurisdicciones;
	}	//	getCountries

	/**
	 * 	Load Countries.
	 * 	Set Default Language to Client Language
	 *	@param ctx context
	 */
	private static void loadAllJurisdicciones (Properties ctx)
	{
		s_jurisdicciones = new CCache<String,String>("C_Jurisdiccion_ID", 250);
		l_jurisdicciones = new ArrayList<String>();
		String sql = "SELECT C_Jurisdiccion_ID,Name FROM C_Jurisdiccion WHERE IsActive='Y' ORDER BY Name";
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				s_jurisdicciones.put(String.valueOf(rs.getInt(1)), rs.getString(2));
				l_jurisdicciones.add(rs.getString(2));
			}
			
			rs.close();
			stmt.close();
		}
		catch (SQLException e){}
		
	}	//	loadAllJurisdicciones

}
