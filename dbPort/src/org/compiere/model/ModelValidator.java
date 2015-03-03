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

/**
 *	Model Validator
 *	
 *  @author Jorg Janke
 *  @version $Id: ModelValidator.java,v 1.3 2005/03/11 20:28:38 jjanke Exp $
 */
public interface ModelValidator
{
	/** Model Change Type New		*/
	public static final int	TYPE_NEW = 1;
	/** Model Change Type Change	*/
	public static final int	TYPE_CHANGE = 2;
	/** Model Change Type Delete	*/
	public static final int	TYPE_DELETE = 3;
	
	
	/**
	 * 	Initialize Validation
	 * 	@param engine validation engine 
	 *	@param client client
	 */
	public void initialize (ModelValidationEngine engine, MClient client);

	/**
	 * 	Get Client to be monitored
	 *	@return AD_Client_ID
	 */
	public int getAD_Client_ID();
	
	/**
	 * 	User logged in 
	 * 	Called before preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID);

	
    /**
     * 	Model Change of a monitored Table.
     * 	Called after PO.beforeSave/PO.beforeDelete 
     * 	when you called addModelChange for the table
     * 	@param po persistent object
     * 	@param type TYPE_
     *	@return error message or null
     *	@exception Exception if the recipient wishes the change to be not accept.
     */
	public String modelChange (PO po, int type) throws Exception;

	
	/**
	 * 	Validate Document.
	 * 	Called as first step of DocAction.prepareIt 
	 * 	or at the end of DocAction.completeIt
     * 	when you called addDocValidate for the table.
     * 	Note that totals, etc. may not be correct before the prepare stage.
	 *	@param po persistent object
	 *	@param timing see TIMING_ constants
     *	@return error message or null - 
     *	if not null, the pocument will be marked as Invalid.
	 */
	public String docValidate (PO po, int timing);
	
	
	/** Called before document is prepared		*/
	public static final int		TIMING_BEFORE_PREPARE = 1;
	/** Called after document is processed		*/
	public static final int		TIMING_AFTER_COMPLETE = 9;
	
}	//	ModelValidator
