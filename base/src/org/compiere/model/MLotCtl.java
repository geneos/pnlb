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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *	Lot Control Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MLotCtl.java,v 1.2 2006/08/11 14:36:22 SIGArg-01 Exp $
 */
public class MLotCtl extends X_M_LotCtl
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_LotCtl_ID id
	 */
	public MLotCtl (Properties ctx, int M_LotCtl_ID, String trxName)
	{
		super (ctx, M_LotCtl_ID, trxName);
		if (M_LotCtl_ID == 0)
		{
		//	setM_LotCtl_ID (0);
			setStartNo (1);
			setCurrentNext (1);
			setIncrementNo (1);
		//	setName (null);
		}
	}	//	MLotCtl

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MLotCtl (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MLotCtl

	/**
	 * 	Create new Lot.
	 * 	Increments Current Next and Commits
	 *	@param M_Product_ID product
	 *	@return saved Lot
	 */
	public MLot createLot (int M_Product_ID)
	{
                System.out.println("ingresando a dar de alta el lote");
		StringBuffer name = new StringBuffer();
		if (getPrefix() != null)
			name.append(getPrefix());
		int no = getCurrentNext();
		//name.append(no);

                /*
                **  Agregado para mantener números de análisis de 3 digitos ej: 001
                **  VIT4B 24/11/2006
                */
                
                if(no < 10)
                    name.append("00" + no);
                else if(no < 100)
                    name.append("0" + no);
                else
                    name.append(no);

                /*
                **  FIN
                **  VIT4B 24/11/2006
                */
                
                /*
                java.util.Date today;
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                today = new java.util.Date();
                
		name.append(formatter.format(today));
		*/

		if (getSuffix() != null)
			name.append(getSuffix());
		//
		no += getIncrementNo();
		setCurrentNext(no);
		save();
		//
                     
		MLot retValue = new MLot (this, M_Product_ID, name.toString());
		retValue.save();
		return retValue;
	}	//	createLot

    MLot createLot(int M_Product_ID, String sufix)
    {
            System.out.println("ingresando a dar de alta el lote");
		StringBuffer name = new StringBuffer();
		if (getPrefix() != null)
			name.append(getPrefix());
		int no = getCurrentNext();
		//name.append(no);

                /*
                **  Agregado para mantener números de análisis de 3 digitos ej: 001
                **  VIT4B 24/11/2006
                */
                
                if(no < 10)
                    name.append("00" + no);
                else if(no < 100)
                    name.append("0" + no);
                else
                    name.append(no);

                /*
                **  FIN
                **  VIT4B 24/11/2006
                */
                              
		name.append(sufix);
		
		if (getSuffix() != null)
			name.append(getSuffix());
		//
		no += getIncrementNo();
		setCurrentNext(no);
		save();
		//
               

                        
		MLot retValue = new MLot (this, M_Product_ID, name.toString());
		retValue.save();
		return retValue;
    }
    
    /*
    **  Crea el lote a partir de los datos pasados en los campos de lote y sufijo
    **  VIT4B - 27/11/2006
    */


    MLot createLot(int M_Product_ID, String name, String sufix)
    {
                System.out.println("ingresando a dar de alta el lote");
		StringBuffer aux = new StringBuffer();
		if (getPrefix() != null)
			aux.append(getPrefix());

                //aux.append(name);

                /*
                **  Agregado para mantener números de análisis de 3 digitos ej: 001
                **  VIT4B 27/11/2006
                */
                
                if(name.length() == 1)
                    aux.append("00" + name);
                else if(name.length() == 2)
                    aux.append("0" + name);
                else
                    aux.append(name);

                /*
                **  FIN
                **  VIT4B 27/11/2006
                */
                              
		aux.append(sufix);
		
		if (getSuffix() != null)
			aux.append(getSuffix());

		save();
                                      
		MLot retValue = new MLot (this, M_Product_ID, aux.toString());
		retValue.save();
		return retValue;
    }

    MLot getCreateLot(int M_Product_ID)
    {
 		StringBuffer name = new StringBuffer();
		if (getPrefix() != null)
			name.append(getPrefix());
		int no = getCurrentNext();

                /*
                **  Agregado para mantener números de análisis de 3 digitos ej: 001
                **  VIT4B 24/11/2006
                */
                
                if(no < 10)
                    name.append("00" + no);
                else if(no < 100)
                    name.append("0" + no);
                else
                    name.append(no);
                
                /*
                **  FIN
                **  VIT4B 24/11/2006
                */		

		if (getSuffix() != null)
			name.append(getSuffix());
                       
                MLot retValue = new MLot (this, M_Product_ID, name.toString());
		//retValue.save();
		return retValue;
    }
    /** BISion - 07/10/2008 - Santiago Ibañez
     * Método para incrementar el próximo número de lote a asignar
     */
    private void incrementCurrentNext(){
        this.setCurrentNext(this.getCurrentNext()+this.getIncrementNo());
        save();
    }
    /** BISion - 07/10/2008 - Santiago Ibañez
     * Método que retorna el próximo número de lote a asignar, y lo incrementa
     * para la próxima vez que sea utilizado.
     */
    public StringBuffer getUpdateLot(){
        int next = getCurrentNext();
        StringBuffer name = new StringBuffer();
        if(next < 10)
            name.append("00" + next);
        else if(next < 100)
            name.append("0" + next);
        else
            name.append(next);
        incrementCurrentNext();
        saveUpdate();
        return name;
    }

}	//	MLotCtl
