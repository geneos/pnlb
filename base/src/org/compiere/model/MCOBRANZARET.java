/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;

import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;

import org.compiere.util.*;
/**
 *
 * @author santiago
 *
 *	Clase creada para Verificar Nro Documento != NULL
 *
 */
public class MCOBRANZARET extends X_C_COBRANZA_RET{
        /**
	 * 	Get Action permissions from Cache
	 *	@param ctx context
	 *	@param AD_Action_Permission id
	 *	@return MActionPermission
	 */

	public static  MCOBRANZARET get (Properties ctx, int C_CobranzaRet_ID)
	{
		Integer key = new Integer (C_CobranzaRet_ID);
		MCOBRANZARET retValue = (MCOBRANZARET) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MCOBRANZARET (ctx, C_CobranzaRet_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,MCOBRANZARET>	s_cache	= new CCache<Integer,MCOBRANZARET>("C_COBRANZARET", 10);
	
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_Action_Permission_ID id
	 */
	public MCOBRANZARET (Properties ctx, int C_CobranzaRet_ID, String trxName)
	{
		super (ctx, C_CobranzaRet_ID, trxName);
	}	//	MActionPermission

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MCOBRANZARET (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MActionPermission
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return succes
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Actualiza la cobranza
		if (success)
		{
			MPayment pay = new MPayment(getCtx(),getC_Payment_ID(),get_TrxName());
			pay.updatepayment();
		}
		
		return success;
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return succes
	 */
	protected boolean afterDelete (boolean success)
	{
		//	Actualiza la cobranza
		if (success)
		{
			MPayment pay = new MPayment(getCtx(),getC_Payment_ID(),get_TrxName());
			pay.updatepayment();
		}
		
		return success;
	}	//	afterDelete
	
	public boolean beforeSave(boolean newRecord)
	{
		String documentNo = getDocumentNo();
        
        if(documentNo == null || documentNo.equals(""))
        {
        	JOptionPane.showMessageDialog(null,"No ingresó Número de Documento", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
        return true;
	}
}
