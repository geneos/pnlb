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
package org.compiere.process;

import java.io.*;
import java.math.BigDecimal;
import javax.sql.RowSet;

/**
 * 	Remote Setup VO
 *
 *  @author Jorg Janke
 *  @version $Id: RemoteSetupVO.java,v 1.5 2005/03/11 20:25:58 jjanke Exp $
 */
public class RemoteSetupVO implements Serializable
{

	public Boolean			Test = Boolean.FALSE;
	public RowSet			ReplicationTable = null;
	public BigDecimal		IDRangeStart = null;
	public BigDecimal		IDRangeEnd = null;
	public int				AD_Client_ID = -1;
	public int				AD_Org_ID = -1;
	public String			Prefix = null;
	public String			Suffix = null;

	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		return "RemoteSetupVO[test=" + Test
			+ ",IDRange=" + IDRangeStart + "-" + IDRangeEnd
			+ ",AD_Client_ID=" + AD_Client_ID + ",AD_Org_ID=" + AD_Org_ID
			+ ",Prefix=" + Prefix + ",Suffix=" + Suffix
			+ "]";
	}	//	toString

}	//	RemoteSetupVO
