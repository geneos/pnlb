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
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *  Note Model
 *
 *  @author Jorg Janke
 *  @version $Id: MNote.java,v 1.14 2005/09/19 04:48:39 jjanke Exp $
 */
public class MNote extends X_AD_Note
{
	/**
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param AD_Note_ID id
	 */
	public MNote (Properties ctx, int AD_Note_ID, String trxName)
	{
		super (ctx, AD_Note_ID, trxName);
		if (AD_Note_ID == 0)
		{
			setProcessed (false);
			setProcessing(false);
		}
	}	//	MNote

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MNote(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MNote

	/**
	 *  New Mandatory Constructor
	 * 	@param ctx context
	 *  @param AD_Message_ID message
	 *  @param AD_User_ID targeted user
	 */
	public MNote (Properties ctx, int AD_Message_ID, int AD_User_ID, String trxName) 
	{
		this (ctx, 0, trxName);
		setAD_Message_ID (AD_Message_ID);
		setAD_User_ID(AD_User_ID);
	}	//	MNote

	/**
	 *  New Mandatory Constructor
	 * 	@param ctx context
	 *  @param AD_MessageValue message
	 *  @param AD_User_ID targeted user
	 */
	public MNote (Properties ctx, String AD_MessageValue, int AD_User_ID, String trxName) 
	{
		this (ctx, MMessage.getAD_Message_ID(ctx, AD_MessageValue), AD_User_ID, trxName);
	}	//	MNote

	/**
	 * 	Create Note
	 *	@param ctx context
	 *	@param AD_Message_ID message
	 *	@param AD_User_ID user
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@param TextMsg text message
	 *	@param Reference reference
	 */
	public MNote (Properties ctx, int AD_Message_ID, int AD_User_ID,
		int AD_Table_ID, int Record_ID, String Reference, String TextMsg, String trxName)
	{
		this (ctx, AD_Message_ID, AD_User_ID, trxName);
		setRecord(AD_Table_ID, Record_ID);
		setReference(Reference);
		setTextMsg(TextMsg);
	}	//	MNote

	/**
	 *  New Constructor
	 * 	@param ctx context
	 *  @param AD_MessageValue message
	 *  @param AD_User_ID targeted user
	 *  @param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	public MNote (Properties ctx, String AD_MessageValue, int AD_User_ID, 
		int AD_Client_ID, int AD_Org_ID, String trxName) 
	{
		this (ctx, MMessage.getAD_Message_ID(ctx, AD_MessageValue), AD_User_ID, trxName);
		setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	MNote


	/**************************************************************************
	 * 	Set Record.
	 * 	(Ss Button and defaults to String)
	 *	@param AD_Message AD_Message
	 */
	public void setAD_Message_ID (String AD_Message)
	{
		int AD_Message_ID = DB.getSQLValue(null,
			"SELECT AD_Message_ID FROM AD_Message WHERE Value=?", AD_Message);
		if (AD_Message_ID != -1)
			super.setAD_Message_ID(AD_Message_ID);
		else
		{
			super.setAD_Message_ID(240); //	Error
			log.log(Level.SEVERE, "setAD_Message_ID - ID not found for '" + AD_Message + "'");
		}
	}	//	setRecord_ID

	/**
	 * 	Set AD_Message_ID.
	 * 	Looks up No Message Found if 0
	 *	@param AD_Message_ID id
	 */
	public void setAD_Message_ID (int AD_Message_ID)
	{
		if (AD_Message_ID == 0)
			super.setAD_Message_ID(MMessage.getAD_Message_ID(getCtx(), "NoMessageFound"));
		else
			super.setAD_Message_ID(AD_Message_ID);
	}	//	setAD_Message_ID

	/**
	 * 	Get Message
	 *	@return message
	 */
	public String getMessage()
	{
		int AD_Message_ID = getAD_Message_ID();
		MMessage msg = MMessage.get(getCtx(), AD_Message_ID);
		return msg.getMsgText();
	}	//	getMessage

	/**
	 * 	Set Record
	 * 	@param AD_Table_ID table
	 * 	@param Record_ID record
	 */
	public void setRecord (int AD_Table_ID, int Record_ID)
	{
		setAD_Table_ID(AD_Table_ID);
		setRecord_ID(Record_ID);
	}	//	setRecord


	/**
	 * 	String Representation
	 *	@return	info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MNote[")
			.append(get_ID()).append(",AD_Message_ID=").append(getAD_Message_ID())
			.append(",").append(getReference())
			.append(",Processed=").append(isProcessed())
			.append("]");
		return sb.toString();
	}	//	toString

        /*
         *  11/07/2013 Maria Jesus Martin
         *  Agrega validación de Secuencia para los avisos.
         *
         */
        protected boolean beforeSave (boolean newRecord)
	{
		String maxID = "SELECT MAX(AD_Note_ID) FROM AD_Note ";
                PreparedStatement pstmt = DB.prepareStatement(maxID, null);
                ResultSet rs;
                try {
                    rs = pstmt.executeQuery();
                    if (rs.next()){
                        int numSec = rs.getInt(1)+1;
                        this.setAD_Note_ID(numSec);
                        String ad_Sequence = "SELECT Currentnext FROM AD_Sequence WHERE name like 'AD_Note'";
                        PreparedStatement pstmtSeq = DB.prepareStatement(ad_Sequence, null);
                        ResultSet rsSeq = pstmtSeq.executeQuery();
                        if (rsSeq.next() ){
                            if ( rsSeq.getInt(1) != numSec ){
                                actualizarSecuencia(numSec+1);
                                log.log(Level.SEVERE, "El ID de la Secuencia de Ad_Note se ha actualizado, ya que no era el que correspondía");
                            }
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MNote.class.getName()).log(Level.SEVERE, null, ex);
                }

		return true;
	}	//	beforeSave

        private void actualizarSecuencia(int numSec) {
            DB.executeUpdate("UPDATE AD_Sequence SET Currentnext = "+ numSec + " WHERE name like 'Ad_Note'",null);
        }

}	//	MNote
