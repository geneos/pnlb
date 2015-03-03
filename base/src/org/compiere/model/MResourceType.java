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


/**
 *	Resource Type Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MResourceType.java,v 1.5 2005/05/17 05:29:53 jjanke Exp $
 */
public class MResourceType extends X_S_ResourceType
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param S_ResourceType_ID id
	 */
	public MResourceType (Properties ctx, int S_ResourceType_ID, String trxName)
	{
		super (ctx, S_ResourceType_ID, trxName);
	}	//	MResourceType

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MResourceType (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MResourceType
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		
		//	Update Products
		if (!newRecord)
		{
			MProduct[] products = MProduct.get(getCtx(), "S_Resource_ID IN "
				+ "(SELECT S_Resource_ID FROM S_Resource WHERE S_ResourceType_ID=" 
				+ getS_ResourceType_ID() + ")", get_TrxName());
			for (int i = 0; i < products.length; i++)
			{
				MProduct product = products[i];
				if (product.setResource(this))
					product.save(get_TrxName());
			}
		}
		
		return success;
	}	//	afterSave

    /* Bision - 15/07/2008 - Santiago Iba�ez
     * Funci�n que retorna dado un recurso de tiempo su equivalente en segundos
     * */
    public BigDecimal getSeconds(){
        MUOM uom = new MUOM(getCtx(),getC_UOM_ID(),null);
        //si la unidad del recurso es D�a
        if ( uom.isDay() ){
            //Bision 12/08/08 Santiago Iba�ez
            //Cuando es d�a, no se consideran 24hs sino las horas en las que
            //el recurso permanece activo.
            //obtengo el tipo del recurso
            BigDecimal horas = new BigDecimal(3600);
            BigDecimal i = getHorasDisponibles();
            return horas.multiply(i);
        }
        //si la unidad del recurso es Horas
        else if ( uom.isHour() )
             return new BigDecimal (3600);
         //si la unidad del recurso es Minutos
         else  if ( uom.isMinute() )
             return new BigDecimal (60);
         //si la unidad del recurso es Mes
         else if (uom.isMonth()){
             //en una hora hay 3600 segundos
             BigDecimal segundosPorHora = new BigDecimal(3600);
             //obtengo la cantidad de d�as que est� disponible en el mes
             BigDecimal i = getDiasDisponibles(4);
             //multiplico los d�as por las horas disponibles por d�a
             i = i.multiply(getHorasDisponibles());
             //normalizo a segundos
             i = i.multiply(segundosPorHora);
             return i;
         }
         //si la unidad del recurso es Semanas
         else if (uom.isWeek()){
            return getDiasDisponibles(1).multiply(getHorasDisponibles()).multiply(new BigDecimal(3600));
         }
         //si la unidad del recurso es 'd�a de trabajo' = 8 hs
         else if (uom.isWorkDay()){
            int segs = 8*3600;
            return new BigDecimal(segs);
         }
         //si la unidad del recurso es 'mes de trabajo' = 20 d�as
         else if (uom.isWorkMonth()){
            int segs = 8*3600*20;
            return new BigDecimal(segs);
         }
         else if (uom.isYear()){
             //en una hora hay 3600 segundos
             BigDecimal segundosPorHora = new BigDecimal(3600);
             //obtengo la cantidad de d�as que est� disponible en el mes
             BigDecimal i = getDiasDisponibles(4);
             //multiplico los d�as por las horas disponibles por d�a
             i = i.multiply(getHorasDisponibles());
             //normalizo a segundos
             i = i.multiply(segundosPorHora);
             i = i.multiply(new BigDecimal(12));
             return i;
         }
         return new BigDecimal(0);
    }
    /**
     * Bision - 13/08/08 - Santiago Iba�ez
     * Funci�n que retorna la cantidad de d�as disponibles que est� el recurso
     * en N semanas.
     * @param type: tipo de recurso
     * @return
     */
    private BigDecimal getDiasDisponibles(int semanas){
        if (isDateSlot()){
            int cant = 0;
            if (isOnMonday())
                cant++;
            if (isOnTuesday())
                cant++;
            if (isOnWednesday())
                cant++;
            if (isOnThursday())
                cant++;
            if (isOnFriday())
                cant++;
            if (isOnSaturday())
                cant++;
            if (isOnSunday())
                cant++;
            cant = cant*semanas;
            return new BigDecimal(cant);
        }
        if (semanas==4)
            return new BigDecimal(30);
        else{
            int dias = semanas *7;
            return new BigDecimal(dias);
        }
    }
    /**
     * Bision - 12/08/08 - Santiago Iba�ez
     * Funci�n que retorna la cantidad de horas por d�a que un recurso (dado
     * su tipo) est� operativo.
     * @param type: tipo de recurso
     * @return
     */
    private BigDecimal getHorasDisponibles(){
        if (isTimeSlot()){
            //Obtengo la hora en la que se inicia el recurso
            Timestamp init = getTimeSlotStart();
            //Obtengo la hora en la que termina de usarse el recurso
            Timestamp end = getTimeSlotEnd();
            //Creo un calendario para calcular la diferencia de horas
            Calendar inicio = Calendar.getInstance();
            Calendar fin = Calendar.getInstance();
            //seteo los milisegundos de ambos calendarios
            inicio.setTimeInMillis(init.getTime());
            fin.setTimeInMillis(end.getTime());
            //inicio.get(Calendar.HOUR_OF_DAY);
            //hora de inicio
            BigDecimal i = new BigDecimal(inicio.get(Calendar.HOUR_OF_DAY));
            //hora de fin
            BigDecimal f = new BigDecimal(fin.get(Calendar.HOUR_OF_DAY));
            //calculo la diferencia de horas
            f = f.subtract(i);
            return f;
        }
        return new BigDecimal(24);
    }
    /** Bision - 05/08/08 - Santiago Iba�ez
      * Funci�n que dado un String, proveniente del Control de Actividad,
      *  me retorna el equivalente en segundos
      * @param u : unidad de tiempo
      * @return
      */
     public BigDecimal getSeconds(String u){
         //Normalizo el tiempo del RA a segundos [seg]
        if (u.equals("h"))
            return new BigDecimal(3600.0);
        else if (u.equals("m"))
            return new BigDecimal(60.0);
        else if (u.equals("D")){
            BigDecimal segundosPorHora = new BigDecimal(3600);
            return getHorasDisponibles().multiply(segundosPorHora);
        }
        else if (u.equals("Y")){
            BigDecimal segundosPorHora = new BigDecimal(3600);
            BigDecimal meses = new BigDecimal(12);
            return meses.multiply(getDiasDisponibles(4).multiply(getHorasDisponibles()).multiply(segundosPorHora));
        }
        else if (u.equals("M")){
            BigDecimal horasPorSegundo = new BigDecimal(3600);
            return getDiasDisponibles(4).multiply(getHorasDisponibles()).multiply(horasPorSegundo);
        }
        return new BigDecimal(1.0);
     }

	
}	//	MResourceType
