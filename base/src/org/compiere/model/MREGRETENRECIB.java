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
 *	R�gime de Percepciones Recibidas
 *	
 *  @author Daniel Gini
 */
public class MREGRETENRECIB extends X_C_REGRETEN_RECIB
{

	public static MREGRETENRECIB[] get (Properties ctx, int C_REGRETEN_RECIB_ID, String trxName)
	{
		ArrayList<MREGRETENRECIB> list = new ArrayList<MREGRETENRECIB>();
		String sql = "SELECT * FROM C_REGPERCEP_RECIB WHERE C_REGRETEN_RECIB_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, C_REGRETEN_RECIB_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				list.add (new MREGRETENRECIB (ctx, rs, trxName));
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
		
		MREGRETENRECIB[] retValue = new MREGRETENRECIB[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	/**	Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MREGRETENRECIB.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_MovementLineMA_ID ignored
	 *	@param trxName trx
	 */
	public MREGRETENRECIB (Properties ctx, int C_REGRETEN_RECIB_ID,
		String trxName)
	{
		super (ctx, C_REGRETEN_RECIB_ID, trxName);
	}	//	MPercepRecib

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result ser
	 *	@param trxName trx
	 */
	public MREGRETENRECIB (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MPercepRecib
	
	protected boolean beforeSave(boolean newRecord)
	{		
		if (getIMPUESTO().equals(RETEN_IIBB))
		{	if ((getJURISDICCION()==null) || (getJURISDICCION().equals(0)))
			{
				JOptionPane.showMessageDialog(null,"Ingrese Jurisdicci�n","Error - Falta Par�metro", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			else
				if ((getCODIGO().equals(0)) || (getCODIGO()==null))
				{
					JOptionPane.showMessageDialog(null,"Ingrese C�digo de Jurisdicci�n","Error - Falta Par�metro", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				else
				{
					setREGIMENGANANCIAS(null);
					setREGIMENIVA(null);
					setREGIMENSUSS(null);
				}
		}
		else
		{	if (getIMPUESTO().equals(RETEN_IVA))
				if (getREGIMENIVA()==null || getREGIMENIVA().equals(""))
				{
					JOptionPane.showMessageDialog(null,"Ingrese R�gimen para IVA","Error - Falta Par�metro", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				else
				{
					setREGIMENGANANCIAS(null);
					setREGIMENSUSS(null);
					setJURISDICCION(null);
					setCODIGO(null);
				}
			else
				if (getIMPUESTO().equals(RETEN_GAN))
					if (getREGIMENGANANCIAS()==null || getREGIMENGANANCIAS().equals(""))
					{	
						JOptionPane.showMessageDialog(null,"Ingrese R�gimen para Ganancias","Error - Falta Par�metro", JOptionPane.ERROR_MESSAGE);
						return false;
					}
					else
					{
						setREGIMENIVA(null);
						setREGIMENSUSS(null);
						setJURISDICCION(null);
						setCODIGO(null);
					}
				else
				{	if (getIMPUESTO().equals(RETEN_SUSS))
						if (getREGIMENSUSS()==null || getREGIMENSUSS().equals(""))
						{	
							JOptionPane.showMessageDialog(null,"Ingrese R�gimen para SUSS","Error - Falta Par�metro", JOptionPane.ERROR_MESSAGE);
							return false;
						}
						else
						{
							setREGIMENGANANCIAS(null);
							setREGIMENIVA(null);
							setJURISDICCION(null);
							setCODIGO(null);
						}
					else
					{	
						setREGIMENGANANCIAS(null);
						setREGIMENIVA(null);
						setREGIMENSUSS(null);
						setJURISDICCION(null);
						setCODIGO(null);
					}
				}
		}
		
		String query = " Select C_REGRETEN_RECIB_Id FROM C_REGRETEN_RECIB" +
					   " WHERE NAME = ? AND ISACTIVE='Y' " +
					   "   AND AD_Org_ID = ? AND AD_Client_ID = ?";
	
		PreparedStatement pstmt = DB.prepareStatement(query, null);
		try {
			pstmt.setString(1, getNAME());
			pstmt.setInt(2, getAD_Org_ID());
			pstmt.setInt(3, getAD_Client_ID());
			ResultSet rs = pstmt.executeQuery();
		
			if (rs.next() && (getREGRETEN_RECIB() != rs.getLong(1)))
			{
				JOptionPane.showMessageDialog(null,"El Regimen ingresado ya existe en la Base de Datos.","Error - Regimen duplicado", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}	//	beforeSave

}	//	MPercepRecib
