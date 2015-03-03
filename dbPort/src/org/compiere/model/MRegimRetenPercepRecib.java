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
 * @author BISION - Matías Maenza
 * @version 1.0
 */
public class MRegimRetenPercepRecib extends X_C_Regim_Reten_Percep_Recib {

/**
	 * 	Get SalesRegion from Cache
	 *	@param ctx context
	 *	@param C_Regim_Reten_Percep_Recib_ID id
	 *	@return MRegimRetenPercepRecib
	 */
    private boolean errorGuardar = false;
    
    //ADialog dialog = new ADialog();
    
	public static  MRegimRetenPercepRecib get (Properties ctx, int C_Regim_Reten_Percep_Recib_ID)
	{
		Integer key = new Integer (C_Regim_Reten_Percep_Recib_ID);
		MRegimRetenPercepRecib retValue = (MRegimRetenPercepRecib) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MRegimRetenPercepRecib (ctx, C_Regim_Reten_Percep_Recib_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,MRegimRetenPercepRecib>	s_cache	= new CCache<Integer,MRegimRetenPercepRecib>("C_Regim_Reten_Percep_Recib_ID", 10);
	
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param C_Regim_Reten_Percep_Recib_ID id
	 */
	public MRegimRetenPercepRecib (Properties ctx, int C_Regim_Reten_Percep_Recib_ID, String trxName)
	{
		super (ctx, C_Regim_Reten_Percep_Recib_ID, trxName);
	}	//	MRegimRetenPercepRecib

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRegimRetenPercepRecib (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRegimRetenPercepRecib

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//if (getAD_Org_ID() != 0)
		//	setAD_Org_ID(0);
                if((((Boolean)get_Value("ESRETENCION"))==null && ((Boolean)get_Value("ESPERCEPCION"))==null) || (((Boolean)get_Value("ESRETENCION")).booleanValue()==false && ((Boolean)get_Value("ESPERCEPCION")).booleanValue()==false)){
                    //ADialog.error(1, null, "","Debe seleccionar si es retención o percepción.");
                     JOptionPane.showMessageDialog(null,"Debe seleccionar si es retención o percepción.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                else
                    if(((Boolean)get_Value("ESRETENCION")).booleanValue() && ((Boolean)get_Value("ESPERCEPCION")).booleanValue()){
                    //ADialog.error(1, null, "","Debe seleccionar si es retención o percepción, no ambas.");
                    JOptionPane.showMessageDialog(null,"Debe seleccionar si es retención o percepción, no ambas.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                    else
                        if(((Boolean)get_Value("ESRETENCION")).booleanValue() && ((String)get_Value("TIPO_RET"))==null){
                   // ADialog.error(1, null, "","Debe seleccionar un tipo de retención.");
                     JOptionPane.showMessageDialog(null,"Debe seleccionar un tipo de retencion.", "Información", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                else
                        if(((Boolean)get_Value("ESPERCEPCION")).booleanValue() && ((String)get_Value("TIPO_PERCEP"))==null){
                    //ADialog.error(1, null, "","Debe seleccionar un tipo de percepción.");
                     JOptionPane.showMessageDialog(null,"Debe seleccionar un tipo de percepción.", "Información", JOptionPane.ERROR_MESSAGE);        
                    return false;
                }
                else
                        if(((Boolean)get_Value("ESPERCEPCION")).booleanValue() && ((String)get_Value("TIPO_PERCEP")).equals("IIB") && (get_Value("T_CODIGOJURISDICCION_ID"))==null){
                    //ADialog.error(1, null, "","Debe seleccionar un código de jurisdicción.");
                     JOptionPane.showMessageDialog(null,"Debe seleccionar un código de jurisdicción.", "Información", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                else
                        if((((Boolean)get_Value("ESRETENCION")).booleanValue()) && (((String)get_Value("TIPO_RET")).equals("B")) && ((get_Value("T_CODIGOJURISDICCION_ID"))==null)){
                     //ADialog.error(1, null, "","Debe seleccionar un código de jurisdicción.");
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un código de jurisdicción.", "Información", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                else{
                        if(((Boolean)get_Value("ESRETENCION")).booleanValue()){
                            setTIPO_PERCEP("");
                            if(!((String)get_Value("TIPO_RET")).equals("B"))
                                setT_CODIGOJURISDICCION_ID("");
                                
                                
                        }
                        else
                        if(((Boolean)get_Value("ESPERCEPCION")).booleanValue()){
                            setTIPO_RET("");
                            if(!((String)get_Value("TIPO_PERCEP")).equals("IIB"))
                                setT_CODIGOJURISDICCION_ID("");
                                
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
		return true;
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		return success;
	}	//	afterDelete
	
}	//	MRegimRetenPercepRecib

