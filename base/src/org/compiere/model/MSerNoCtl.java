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
 *	Serial Number Control Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MSerNoCtl.java,v 1.6 2005/05/17 05:29:52 jjanke Exp $
 */
public class MSerNoCtl extends X_M_SerNoCtl
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_SerNoCtl_ID id
	 */
	public MSerNoCtl(Properties ctx, int M_SerNoCtl_ID, String trxName)
	{
		super(ctx, M_SerNoCtl_ID, trxName);
		if (M_SerNoCtl_ID == 0)
		{
		//	setM_SerNoCtl_ID (0);
			setStartNo (1);
			setCurrentNext (1);
			setIncrementNo (1);
		//	setName (null);
		}
	}	//	MSerNoCtl

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MSerNoCtl(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MSerNoCtl

	/**
	 * 	Create new Lot.
	 * 	Increments Current Next and Commits
	 *	@return saved Lot
	 */
	public String createSerNo ()
	{
		StringBuffer name = new StringBuffer();
		if (getPrefix() != null)
			name.append(getPrefix());
		int no = getCurrentNext();
		name.append(no);
		if (getSuffix() != null)
			name.append(getSuffix());
		//
		no += getIncrementNo();
		setCurrentNext(no);
		save();
		return name.toString();
	}	//	createSerNo

}	//	MSerNoCtl
