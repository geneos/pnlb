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
package org.compiere.www;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.ecs.*;
import org.apache.ecs.xhtml.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  WAccount Servlet.
 *  <p>
 *  The servlet is invoked by a parent window via
 *  <code>
 *  WAccount?FormName=formName%ColumnName=columnName
 *  </code>
 *  and assumes that in the opening window/form there are two fields
 *  <code>
 *  opener.document.formName.columnName - The (hidden) field for the ID
 *  opener.document.formName.columnName_D - The display field for the value
 *  </code>
 *  When selecting an entry, the window is closed and the value of the two fields set.
 *
 *  @author Jorg Janke
 *  @version  $Id: WAccount.java,v 1.8 2005/03/11 20:34:48 jjanke Exp $
 */
public class WAccount extends HttpServlet
{
	/**
	 * Initialize global variables
	 *
	 * @param config
	 * @throws ServletException
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		if (!WebEnv.initWeb(config))
			throw new ServletException("WAccount.init");
	}   //  init
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(WAccount.class);

	/**
	 * Process the HTTP Get request - initial Start
	 * Needs to have parameters FormName and ColumnName
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.config( "WAccount.doGet");
		WebSessionCtx wsc = WebSessionCtx.get(request); 
		WWindowStatus ws = WWindowStatus.get(request);
		if (wsc == null || ws == null)
		{
			WebUtil.createTimeoutPage(request, response, this, null);
			return;
		}
		//  Get Mandatory Parameters
		String formName = WebUtil.getParameter (request, "FormName");
		String columnName = WebUtil.getParameter (request, "ColumnName");
		//
		MField mField = ws.curTab.getField(columnName);
		log.config("FormName=" + formName + ", ColumnName=" + columnName + ", MField=" + mField.toString());
		if (mField == null || formName == null || columnName == null || formName.equals("") || columnName.equals(""))
		{
			WebUtil.createTimeoutPage(request, response, this,  
				Msg.getMsg(wsc.ctx, "ParameterMissing"));
			return;
		}
	//	Object value = ws.curTab.getValue(columnName);
		String target = "opener.document." + formName + "." + columnName;

		//  Create Document
		WebDoc doc = WebDoc.create (mField.getHeader());
		body body = doc.getBody();
		body.setOnBlur("self.focus();");
		body.addElement(fillTable(ws, mField, target));

		//  Reset, Cancel
		button reset = new button();
		reset.addElement("Reset");                      //  translate
		reset.setOnClick(target + ".value='';" + target + "_D.value='';window.close();");
		button cancel = new button();
		cancel.addElement("Cancel");                    //  translate
		cancel.setOnClick("window.close();");
		body.addElement(new p(AlignType.RIGHT)
			.addElement(reset)
			.addElement("&nbsp")
			.addElement(cancel));
		//
	//	log.fine( doc.toString());
		WebUtil.createResponse (request, response, this, null, doc, false);
	}   //  doGet


	/**
	 *  Process the HTTP Post request - perform doGet
	 *  @param request
	 *  @param response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		log.config( "WAccount.doPost");
		doGet(request, response);
	}   //  doPost


	/*************************************************************************/

	/**
	 *  Fill Table (Generic)
	 *
	 * @param ws        WindowStatus
	 * @param mField    the Field
	 * @param target    target field string
	 * @return  Table with selection
	 */
	private table fillTable (WWindowStatus ws, MField mField, String target)
	{
		table table = new table("1");
		tr line = new tr();
		line.addElement(new th("&nbsp")).addElement(new th(Msg.translate(ws.ctx, "Name")));
		table.addElement(line);

		//  Fill & list options
		Lookup lookup = mField.getLookup();
		lookup.fillComboBox(mField.isMandatory(false), true, true, true);   //  no context check
		int size = lookup.getSize();
		for (int i = 0; i < size; i++)
		{
			Object lValue = lookup.getElementAt(i);
			if (!(lValue != null && lValue instanceof KeyNamePair))
				continue;
			//
		//	log.fine( lValue.toString());
			KeyNamePair np = (KeyNamePair)lValue;
			button button = new button();
			button.addElement("&gt;");
			StringBuffer script = new StringBuffer(target);
			script.append(".value='").append(np.getKey()).append("';")
				.append(target).append("_D.value='").append(np.getName()).append("';window.close();");
			button.setOnClick(script.toString());
			//
			line = new tr();
			line.addElement(new td(button));
			String name = np.getName();
			if (name == null || name.length() == 0)
				name = "&nbsp";
			line.addElement(new td(name));
			table.addElement(line);
		}
		//  Restore
		lookup.fillComboBox(true);
		return table;
	}   //  fillTable

}   //  WAccount
