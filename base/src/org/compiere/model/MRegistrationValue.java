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

import java.sql.ResultSet;
import java.util.Properties;

/**
 *	Asset Registration Attribute Value
 *	
 *  @author Jorg Janke
 *  @version $Id: MRegistrationValue.java,v 1.8 2005/03/11 20:26:03 jjanke Exp $
 */
public class MRegistrationValue extends X_A_RegistrationValue
	implements Comparable
{
	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 */
	public MRegistrationValue (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MRegistrationValue
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRegistrationValue(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRegistrationValue
	
	/**
	 * 	Parent Constructor
	 *	@param registration parent
	 *	@param A_RegistrationAttribute_ID attribute
	 *	@param Name value
	 */
	public MRegistrationValue (MRegistration registration, 
		int A_RegistrationAttribute_ID, String Name)
	{
		super(registration.getCtx(), 0, registration.get_TrxName());
		setClientOrg (registration);
		setA_Registration_ID (registration.getA_Registration_ID());
		//
		setA_RegistrationAttribute_ID (A_RegistrationAttribute_ID);
		setName (Name);
	}	//	MRegistrationValue

	/**	Cached Attribute Name				*/
	private String		m_registrationAttribute = null;
	/**	Cached Attribute Description		*/
	private String		m_registrationAttributeDescription = null;
	/**	Cached Attribute Sequence	*/
	private int			m_seqNo = -1;

	/**
	 * 	Get Registration Attribute
	 *	@return name of registration attribute
	 */
	public String getRegistrationAttribute()
	{
		if (m_registrationAttribute == null)
		{
			int A_RegistrationAttribute_ID = getA_RegistrationAttribute_ID();
			MRegistrationAttribute att = MRegistrationAttribute.get (getCtx(), A_RegistrationAttribute_ID, get_TrxName());
			m_registrationAttribute = att.getName();
			m_registrationAttributeDescription = att.getDescription();
			m_seqNo = att.getSeqNo();
		}
		return m_registrationAttribute; 
	}	//	getRegistrationAttribute

	/**
	 * 	Get Registration Attribute Description
	 *	@return Description of registration attribute 
	 */
	public String getRegistrationAttributeDescription()
	{
		if (m_registrationAttributeDescription == null)
			getRegistrationAttribute();
		return m_registrationAttributeDescription; 
	}	//	getRegistrationAttributeDescription

	/**
	 * 	Get Attribute SeqNo
	 *	@return seq no
	 */
	public int getSeqNo()
	{
		if (m_seqNo == -1)
			getRegistrationAttribute();
		return m_seqNo;
	}	//	getSeqNo

	/**
	 * 	Compare To
     *	@param   o the Object to be compared.
     *	@return  a negative integer, zero, or a positive integer as this object
     *		is less than, equal to, or greater than the specified object.
	 */
	public int compareTo (Object o)
	{
		if (o == null)
			return 0;
		MRegistrationValue oo = (MRegistrationValue)o;
		int compare = getSeqNo() - oo.getSeqNo();
		return compare;
	}	//	compareTo

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getSeqNo()).append(": ")
			.append(getRegistrationAttribute()).append("=").append(getName());
		return sb.toString();
	}	//	toString
	
}	//	MRegistrationValue
