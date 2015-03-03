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

import java.math.BigInteger;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *  BP Bank Account Model
 *
 *  @author Jorg Janke
 *  @version $Id: MBPBankAccount.java,v 1.7 2005/09/19 04:49:47 jjanke Exp $
 */
public class MBPBankAccount extends X_C_BP_BankAccount
{
	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param C_BP_BankAccount_ID BP bank account
	 */
	public MBPBankAccount (Properties ctx, int C_BP_BankAccount_ID, String trxName)
	{
		super (ctx, C_BP_BankAccount_ID, trxName);
		if (C_BP_BankAccount_ID == 0)
		{
		//	setC_BPartner_ID (0);
			setIsACH (false);
		}
	}	//	MBP_BankAccount

	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MBPBankAccount (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBP_BankAccount

	/**
	 * 	Constructor
	 *	@param ctx context
	 * 	@param bp BP
	 *	@param bpc BP Contact
	 * 	@param location Location
	 */
	public MBPBankAccount (Properties ctx, MBPartner bp, MUser bpc, MLocation location)
	{
		super(ctx, 0, bp.get_TrxName());
		setIsACH (false);
		//
		setC_BPartner_ID(bp.getC_BPartner_ID());
		//
		setA_Name(bpc.getName());
		setA_EMail(bpc.getEMail());
		//
		setA_Street(location.getAddress1());
		setA_City(location.getCity());
		setA_Zip(location.getPostal());
		setA_State(location.getRegionName(true));
		setA_Country(location.getCountryName());
	}	//	MBP_BankAccount


	/*************************************************************************/

	/**
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MBP_BankAccount[")
			.append (get_ID ())
			.append(", Name=").append(getA_Name())
			.append ("]");
		return sb.toString ();
	}	//	toString

        protected boolean beforeSave (boolean newRecord){
                if(getCBU()!=null){
                    try{
                    BigInteger ii = new BigInteger(getCBU());
                    }catch(NumberFormatException e){
                        JOptionPane.showMessageDialog(null,"El CBU no debe tener caracteres ni letras.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }
                }
                return true;            
        }
}	//	MBPBankAccount
