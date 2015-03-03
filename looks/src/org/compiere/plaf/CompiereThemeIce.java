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
package org.compiere.plaf;

import java.awt.*;
import javax.swing.plaf.*;

/**
 * 	Compiere Theme Ice
 *	
 *  @author Jorg Janke, Adam Michau
 *  @version $Id: CompiereThemeIce.java,v 1.1 2005/10/21 15:35:46 jjanke Exp $
 */
public class CompiereThemeIce extends CompiereTheme
{
	/**
	 * 	Compiere Theme: Ice
	 */
	public CompiereThemeIce ()
	{
		setDefault();
		s_theme = this;
		s_name = NAME;
	}	//	CompiereThemeBlueMetal
	
	/**	Name			*/
	public static final String	NAME = "Compiere Ice";
	
	/**
	 *  Set Defaults
	 */
	protected void setDefault()
	{
		/** Blue 102, 102, 153      */
		primary1 =      new ColorUIResource(210, 226, 239);
		/** Blue 153, 153, 204      */
		primary2 =      new ColorUIResource(233, 240, 248);
		/** Blue 204, 204, 255      */
		primary3 =      new ColorUIResource(167, 198, 227);
		/** Gray 102, 102, 102      */
		secondary1 =    new ColorUIResource(102, 102, 102);
		/** Gray 153, 153, 153      */
		secondary2 =    new ColorUIResource(153, 153, 153);
		/** BlueGray 214, 224, 234 - background */
		secondary3 =    new ColorUIResource(210, 226, 239);

		/** Black                   */
		black =         new ColorUIResource(Color.black);
		/** White                   */
		white =         new ColorUIResource(Color.white);

		/** Background for mandatory fields */
		mandatory =     new ColorUIResource(224, 224, 255); //  blue-isch
		/** Background for fields in error  */
		error =         new ColorUIResource(255, 204, 204); //  red-isch
		/** Background for inactive fields  */
		inactive =      new ColorUIResource(234, 234, 234);	//	light gray
		/** Background for info fields      */
		info =          new ColorUIResource(253, 237, 207);	//	light yellow

		/** Foreground Text OK      */
		txt_ok =        new ColorUIResource(51, 51, 102);   //  dark blue
		/** Foreground Text Error   */
		txt_error =     new ColorUIResource(204, 0, 0);     //  dark red

		/** Control font            */
		controlFont = null;
		_getControlTextFont();
		/** System font             */
		systemFont = null;
		_getSystemTextFont();
		/** User font               */
		userFont = null;
		_getUserTextFont();
		/** Small font              */
		smallFont = null;
		_getSubTextFont();
		/** Window Title font       */
		windowFont = null;
		_getWindowTitleFont();
		/** Menu font               */
		menuFont = null;
		_getMenuTextFont();
	}   //  setDefault

}	//	CompiereThemeIce
