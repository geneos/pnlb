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

import java.math.*;
import java.sql.*;
import java.util.*;
import org.compiere.util.DB;

/**
 *  Product Attribute Set
 *
 *	@author Jorg Janke
 *	@version $Id: MAttributeInstance.java,v 1.8 2005/09/19 04:49:48 jjanke Exp $
 */
public class MAttributeInstance extends X_M_AttributeInstance
{
	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MAttributeInstance (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MAttributeInstance

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAttributeInstance (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAttributeInstance

	/**
	 * 	String Value Constructior
	 *	@param ctx context
	 *	@param M_Attribute_ID attribute
	 *	@param M_AttributeSetInstance_ID instance
	 *	@param Value string value
	 */
	public MAttributeInstance (Properties ctx, int M_Attribute_ID, 
		int M_AttributeSetInstance_ID, String Value, String trxName)
	{
		super(ctx, 0, trxName);
		setM_Attribute_ID (M_Attribute_ID);
		setM_AttributeSetInstance_ID (M_AttributeSetInstance_ID);
		setValue (Value);
	}	//	MAttributeInstance
	
	/**
	 * 	Number Value Constructior
	 *	@param ctx context
	 *	@param M_Attribute_ID attribute
	 *	@param M_AttributeSetInstance_ID instance
	 *	@param BDValue number value
	 */
	public MAttributeInstance (Properties ctx, int M_Attribute_ID, 
		int M_AttributeSetInstance_ID, BigDecimal BDValue, String trxName)
	{
		super(ctx, 0, trxName);
			setM_Attribute_ID (M_Attribute_ID);
		setM_AttributeSetInstance_ID (M_AttributeSetInstance_ID);
		setValueNumber(BDValue);
	}	//	MAttributeInstance

	/**
	 * 	Selection Value Constructior
	 *	@param ctx context
	 *	@param M_Attribute_ID attribute
	 *	@param M_AttributeSetInstance_ID instance
	 *	@param M_AttributeValue_ID selection
	 * 	@param Value String representation for fast display
	 */
	public MAttributeInstance (Properties ctx, int M_Attribute_ID, 
		int M_AttributeSetInstance_ID, int M_AttributeValue_ID, String Value, String trxName)
	{
		super(ctx, 0, trxName);
		setM_Attribute_ID (M_Attribute_ID);
		setM_AttributeSetInstance_ID (M_AttributeSetInstance_ID);
		setM_AttributeValue_ID (M_AttributeValue_ID);
		setValue (Value);
	}	//	MAttributeInstance

	
	/**
	 * 	Set ValueNumber
	 *	@param ValueNumber number
	 */
	public void setValueNumber (BigDecimal ValueNumber)
	{
		super.setValueNumber (ValueNumber);
		if (ValueNumber == null)
		{
			setValue(null);
			return;
		}
		if (ValueNumber.signum() == 0)
		{
			setValue("0");
			return;
		}
		//	Display number w/o decimal 0
		char[] chars = ValueNumber.toString().toCharArray();
		StringBuffer display = new StringBuffer();
		boolean add = false;
		for (int i = chars.length-1; i >= 0; i--)
		{
			char c = chars[i];
			if (add)
				display.insert(0, c);
			else
			{
				if (c == '0')
					continue;
				else if (c == '.')	//	decimal point
					add = true;
				else
				{
					display.insert(0, c);
					add = true;
				}
			}
		}			
		setValue(display.toString());
	}	//	setValueNumber
	
	
	/**
	 *	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		return getValue();
	}	//	toString
        
        /** BISion - 06/10/2008 - Santiago Ibañez
         * Método para comprobar si una partida tiene el análisis creado pero en
         * cero, para que luego se le asigne un número en el momento de
         * completar el procedimiento.
         * @param M_AttributeSetInstance_ID 
         * @param M_Attribute_ID
         * @return
         */
        public static boolean tieneAnalisisCero(int M_AttributeSetInstance_ID,int M_Attribute_ID){
              String sql = "SELECT VALUENUMBER FROM M_ATTRIBUTEINSTANCE ai"+
                           " WHERE ai.M_ATTRIBUTE_ID = ? AND ai.M_ATTRIBUTESETINSTANCE_ID = ?";
              BigDecimal analisis = new BigDecimal(DB.getSQLValue(null, sql, M_Attribute_ID,M_AttributeSetInstance_ID));
              return analisis.equals(BigDecimal.ZERO);
        }
        
        /** Bision - 29/09/2008 - Santiago Ibaéez
         * Este método retorna el próximo número de análisis que se debe asignar
         * al producto dado.
         * @param M_Product_ID
         * @param M_Attribute_ID
         * @return
         */
        public static BigDecimal getProximoAnalisis(int M_Product_ID,int M_Attribute_ID){
            //Obtengo el maximo analisis que tiene el producto almcenado
            String sql = "SELECT MAX(VALUENUMBER) FROM M_ATTRIBUTEINSTANCE ai"+
                         " JOIN M_ATTRIBUTESETINSTANCE asi ON (asi.M_ATTRIBUTESETINSTANCE_ID = ai.M_ATTRIBUTESETINSTANCE_ID)"+
                         " JOIN M_STORAGE s ON (s.M_ATTRIBUTESETINSTANCE_ID = asi.M_ATTRIBUTESETINSTANCE_ID)"+
                         " WHERE s.M_Product_ID = ? AND ai.M_ATTRIBUTE_ID = ? AND ai.ISACTIVE = 'Y' AND ai.VALUENUMBER <> 0";
            BigDecimal analisis = new BigDecimal(DB.getSQLValue(null, sql, M_Product_ID,M_Attribute_ID));
            //si es el primer analisis a asignar, comienza con el 1
            if (analisis.intValue() ==-1 || analisis.equals(BigDecimal.ZERO))
                analisis = BigDecimal.ZERO;
            //retorno el próximo análisis
            return analisis.add(BigDecimal.ONE);
        }

}	//	MAttributeInstance
