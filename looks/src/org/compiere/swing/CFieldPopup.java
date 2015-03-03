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
package org.compiere.swing;

/**
 *  Compiere Field external popup Interface.
 *  The actual class must be a JDialog
 *
 *  @author     Jorg Janke
 *  @version    $Id: CFieldPopup.java,v 1.6 2005/09/16 00:50:55 jjanke Exp $
 */
public interface CFieldPopup
{
	/**
	 *  Show Popup
	 */
	public void show();

	/**
	 *  Set Value
	 *  @param value
	 */
	public void setValue (Object value);

	/**
	 *  Get Value
	 *  @return value
	 */
	public Object getValue();

	/**
	 *  Set Format
	 *  @param format
	 */
	public void setFormat (Object format);

	/**
	 *  Get Format
	 *  @return format
	 */
	public Object getFormat();

}   //  CFieldPopup
