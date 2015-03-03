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

import java.util.*;

import org.compiere.util.Trx;

/**
 *  Interface for user started processes.
 *
 *  ProcessCtrl.startClass creates the Object and calls startProcess
 *  before executing the optional SQL procedure and Report.
 *
 *  see ProcessCtl#startClass
 *  @author     Jorg Janke
 *  @version    $Id: ProcessCall.java,v 1.6 2005/10/20 04:52:11 jjanke Exp $
 */
public interface ProcessCall
{
	/**
	 *  Start the process.
	 *  Called when pressing the ... button in ...
	 *  It should only return false, if the function could not be performed
	 *  as this causes the process to abort.
	 *
	 *  @param ctx              Context
	 *  @param pi				Process Info
	 *  @return true if the next process should be performed
	 */
	public boolean startProcess (Properties ctx, ProcessInfo pi, Trx trx);

}   //  ProcessCall
