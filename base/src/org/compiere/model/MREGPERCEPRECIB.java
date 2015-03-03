/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.JOptionPane;

import org.compiere.util.*;


/**
 *	Régime de Percepciones Recibidas
 *	
 *  @author Daniel Gini
 */
public class MREGPERCEPRECIB extends X_C_REGPERCEP_RECIB
{

	public static MREGPERCEPRECIB[] get (Properties ctx, int C_REGPERCEP_RECIB_ID, String trxName)
	{
		ArrayList<MREGPERCEPRECIB> list = new ArrayList<MREGPERCEPRECIB>();
		String sql = "SELECT * FROM C_REGPERCEP_RECIB WHERE C_REGPERCEP_RECIB_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, C_REGPERCEP_RECIB_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				list.add (new MREGPERCEPRECIB (ctx, rs, trxName));
			}
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
		
		MREGPERCEPRECIB[] retValue = new MREGPERCEPRECIB[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	/**	Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MREGPERCEPRECIB.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_MovementLineMA_ID ignored
	 *	@param trxName trx
	 */
	public MREGPERCEPRECIB (Properties ctx, int C_REGPERCEP_RECIB_ID,
		String trxName)
	{
		super (ctx, C_REGPERCEP_RECIB_ID, trxName);
	}	//	MPercepRecib

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result ser
	 *	@param trxName trx
	 */
	public MREGPERCEPRECIB (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MPercepRecib
	
	protected boolean beforeSave(boolean newRecord)
	{
		if (getIMPUESTO().equals("Ingresos Brutos"))
		{	if ((getJURISDICCION()==null) || (getJURISDICCION().equals(0)))
			{
				JOptionPane.showMessageDialog(null,"Ingrese Jurisdicción","Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			else
				if ((getCODIGO().equals(0)) || (getCODIGO()==null))
				{
					JOptionPane.showMessageDialog(null,"Ingrese Código de Jurisdicción","Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				else
				{
					setREGIMENGANANCIAS(null);
					setREGIMENIVA(null);
				}
		}
		else
		{	if (getIMPUESTO().equals("IVA"))		//TODO PASAR A CONSTANTE
				if (getREGIMENIVA()==null || getREGIMENIVA().equals(""))
				{
					JOptionPane.showMessageDialog(null,"Ingrese Régimen para IVA","Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				else
				{
					setREGIMENGANANCIAS(null);
					setJURISDICCION(null);
					setCODIGO(null);
				}
			else
				if (getIMPUESTO().equals("Ganancias"))		//TODO PASAR A CONSTANTE
					if (getREGIMENGANANCIAS()==null || getREGIMENGANANCIAS().equals(""))
					{	
						JOptionPane.showMessageDialog(null,"Ingrese Régimen para Ganancias","Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					else
					{
						setREGIMENIVA(null);
						setJURISDICCION(null);
						setCODIGO(null);
					}
				else
				{
					setREGIMENGANANCIAS(null);
					setREGIMENIVA(null);
					setJURISDICCION(null);
					setCODIGO(null);
				}
		}
		
		return true;
	}	//	beforeSave

}	//	MPercepRecib
