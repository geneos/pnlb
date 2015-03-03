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
import javax.sql.*;

/**
 *  Send New Data To Remote for Update
 *
 *  @author Jorg Janke
 *  @version $Id: RemoteUpdateVO.java,v 1.7 2005/03/11 20:25:58 jjanke Exp $
 */
public class RemoteUpdateVO implements Serializable
{

	public Boolean			Test = Boolean.FALSE;
//	public Integer			AD_Table_ID = null;
	public String			TableName = null;
	public String			Sql = null;
	public String[]			KeyColumns = null;
	public RowSet			CentralData = null;

	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		return "RemoteUpdateVO[test=" + Test
			+ "-" + TableName
	//		+ "," + Sql
			+ "]";
	}	//	toString


}	//	RemoteUpdateVO