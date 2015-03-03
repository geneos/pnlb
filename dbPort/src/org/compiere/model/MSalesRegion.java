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

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;
import org.compiere.util.*;


/**
 *	Sales Region Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MSalesRegion.java,v 1.10 2005/11/25 21:58:29 jjanke Exp $
 */
public class MSalesRegion extends X_C_SalesRegion
{
	/**
	 * 	Get SalesRegion from Cache
	 *	@param ctx context
	 *	@param C_SalesRegion_ID id
	 *	@return MSalesRegion
	 */
	public static MSalesRegion get (Properties ctx, int C_SalesRegion_ID)
	{
		Integer key = new Integer (C_SalesRegion_ID);
		MSalesRegion retValue = (MSalesRegion) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MSalesRegion (ctx, C_SalesRegion_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,MSalesRegion>	s_cache	= new CCache<Integer,MSalesRegion>("C_SalesRegion", 10);
	
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param C_SalesRegion_ID id
	 */
	public MSalesRegion (Properties ctx, int C_SalesRegion_ID, String trxName)
	{
		super (ctx, C_SalesRegion_ID, trxName);
	}	//	MSalesRegion

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MSalesRegion (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MSalesRegion

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		
                if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
                 //Modificacion hecha por Bision-Maenza,Matias para no guardar coeficientes de IB ==0
                if(get_Value("T_CODIGOJURISDICCION_ID")==null)
                    set_Value("T_CODIGOJURISDICCION_ID", new Integer(0));
                else
                    
                    if(((Integer)get_Value("T_CODIGOJURISDICCION_ID")).intValue()!=0){
                        if((((BigDecimal)get_Value("COEFICIENTEIB")).doubleValue()==0)){
                            JOptionPane.showMessageDialog(null,"El Coeficiente IB debe ser distinto de 0 (cero)", "Información", JOptionPane.ERROR_MESSAGE);
                            return false;
                         }
                        if((((BigDecimal)get_Value("LIMITEMINIMOIB")).doubleValue()==0)){
                            JOptionPane.showMessageDialog(null,"El Limite Minimo IB debe ser distinto de 0 (cero)", "Información", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                }                    
		               
		return true;
	}	//	beforeSave

	/**
	 * 	After Save.
	 * 	Insert
	 * 	- create tree
	 *	@param newRecord insert
	 *	@param success save success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		if (newRecord)
			insert_Tree(MTree_Base.TREETYPE_SalesRegion);
		//	Value/Name change
		if (!newRecord && (is_ValueChanged("Value") || is_ValueChanged("Name")))
			MAccount.updateValueDescription(getCtx(), "C_SalesRegion_ID=" + getC_SalesRegion_ID(), get_TrxName());

		return true;
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		if (success)
			delete_Tree(MTree_Base.TREETYPE_SalesRegion);
		return success;
	}	//	afterDelete
	
}	//	MSalesRegion
