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
import java.util.logging.*;
import org.compiere.util.*;
import org.compiere.util.QueryDB;

/**
 *  Product Attribute
 *
 *	@author Jorg Janke
 *	@version $Id: MAttribute.java,v 1.11 2005/11/06 01:17:27 jjanke Exp $
 */
public class MAttribute extends X_M_Attribute
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Attribute_ID id
	 */
	public MAttribute (Properties ctx, int M_Attribute_ID, String trxName)
	{
		super (ctx, M_Attribute_ID, trxName);
		if (M_Attribute_ID == 0)
		{
			setAttributeValueType(ATTRIBUTEVALUETYPE_StringMax40);
			setIsInstanceAttribute (false);
			setIsMandatory (false);
		}
	}	//	MAttribute

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MAttribute (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAttribute

	/**	Values						*/
	private MAttributeValue[]		m_values = null;

	/**
	 *	Get Values if List
	 *	@return Values or null if not list
	 */
	public MAttributeValue[] getMAttributeValues()
	{
		if (m_values == null && ATTRIBUTEVALUETYPE_List.equals(getAttributeValueType()))
		{
			ArrayList<MAttributeValue> list = new ArrayList<MAttributeValue>();
			if (!isMandatory())
				list.add (null);
			//
			String sql = "SELECT * FROM M_AttributeValue "
				+ "WHERE M_Attribute_ID=? "
				+ "ORDER BY Value";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, getM_Attribute_ID());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
					list.add(new MAttributeValue (getCtx(), rs, null));
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (SQLException ex)
			{
				log.log(Level.SEVERE, sql, ex);
			}
			try
			{
				if (pstmt != null)
					pstmt.close();
			}
			catch (SQLException ex1)
			{
			}
			pstmt = null;
			m_values = new MAttributeValue[list.size()];
			list.toArray(m_values);
		}
		return m_values;
	}	//	getValues

	
	/**************************************************************************
	 * 	Get Attribute Instance
	 *	@param M_AttributeSetInstance_ID attribute set instance
	 *	@return Attribute Instance or null
	 */
	public MAttributeInstance getMAttributeInstance (int M_AttributeSetInstance_ID)
	{
		MAttributeInstance retValue = null;
		String sql = "SELECT * "
			+ "FROM M_AttributeInstance "
			+ "WHERE M_Attribute_ID=? AND M_AttributeSetInstance_ID=?";
 		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getM_Attribute_ID());
			pstmt.setInt(2, M_AttributeSetInstance_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MAttributeInstance (getCtx(), rs, get_TrxName());
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;

		return retValue;
	}	//	getAttributeInstance

	/**
	 * 	Set Attribute Instance
	 * 	@param value value
	 * 	@param M_AttributeSetInstance_ID id
	 */
	public void setMAttributeInstance (int M_AttributeSetInstance_ID, MAttributeValue value)
	{
		MAttributeInstance instance = getMAttributeInstance(M_AttributeSetInstance_ID);
		if (instance == null)
		{
			if (value != null)
				instance = new MAttributeInstance (getCtx (), getM_Attribute_ID (),
					M_AttributeSetInstance_ID, value.getM_AttributeValue_ID (),
					value.getName (), get_TrxName()); 					//	Cached !!
			else
				instance = new MAttributeInstance (getCtx(), getM_Attribute_ID(),
					M_AttributeSetInstance_ID, 0, null, get_TrxName());
		}
		else
		{
			if (value != null)
			{
				instance.setM_AttributeValue_ID (value.getM_AttributeValue_ID ());
				instance.setValue (value.getName()); 	//	Cached !!
			}
			else
			{
				instance.setM_AttributeValue_ID (0);
				instance.setValue (null);
			}
		}
		instance.save();
	}	//	setAttributeInstance

	/**
	 * 	Set Attribute Instance
	 * 	@param value string value
	 * 	@param M_AttributeSetInstance_ID id
	 */
	public void setMAttributeInstance (int M_AttributeSetInstance_ID, String value)
	{
		MAttributeInstance instance = getMAttributeInstance(M_AttributeSetInstance_ID);
		if (instance == null)
			instance = new MAttributeInstance (getCtx(), getM_Attribute_ID(), 
				M_AttributeSetInstance_ID, value, get_TrxName());
		else
			instance.setValue(value);
		instance.save();
	}	//	setAttributeInstance

	/**
	 * 	Set Attribute Instance
	 * 	@param value number value
	 * 	@param M_AttributeSetInstance_ID id
	 */
	public void setMAttributeInstance (int M_AttributeSetInstance_ID, BigDecimal value)
	{
		MAttributeInstance instance = getMAttributeInstance(M_AttributeSetInstance_ID);
		if (instance == null)
			instance = new MAttributeInstance (getCtx(), getM_Attribute_ID(), 
				M_AttributeSetInstance_ID, value, get_TrxName());
		else
			instance.setValueNumber(value);
		instance.save();
	}	//	setAttributeInstance
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MAttribute[");
		sb.append (get_ID()).append ("-").append (getName())
			.append(",Type=").append(getAttributeValueType())
			.append(",Instance=").append(isInstanceAttribute())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	AfterSave
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Changed to Instance Attribute
		if (!newRecord && is_ValueChanged("IsInstanceAttribute") && isInstanceAttribute())
		{
			String sql = "UPDATE M_AttributeSet mas "
				+ "SET IsInstanceAttribute='Y' "
				+ "WHERE IsInstanceAttribute='N'"
				+ " AND EXISTS (SELECT * FROM M_AttributeUse mau "
					+ "WHERE mas.M_AttributeSet_ID=mau.M_AttributeSet_ID"
					+ " AND mau.M_Attribute_ID=" + getM_Attribute_ID() + ")";
			int no = DB.executeUpdate(sql, get_TrxName());
			log.fine("AttributeSet Instance set #" + no);
		}
		return success;
	}	//	afterSave
        /**
         * Bision - 25/09/2008 - Santiago Ibañez
         * Método que retorna el ID para el atributo número de análisis
         * @return Id correspondiente
         */
        public static int getAtributoAnalisisID(){
            QueryDB query = new QueryDB("org.compiere.model.X_M_Attribute");
            String filter = "name = 'Nro. de Análisis'";
            java.util.List results = query.execute(filter);
            Iterator select = results.iterator();
            while (select.hasNext())
            {
             X_M_Attribute M_Attribute =  (X_M_Attribute) select.next();                                          
             return M_Attribute.getM_Attribute_ID();
            }
            return 0;
        }
	
}	//	MAttribute
