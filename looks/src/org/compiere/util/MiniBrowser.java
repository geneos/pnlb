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
package org.compiere.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import org.compiere.plaf.CompierePLAF;

/**
 *  Mini Browser
 *
 *  @author     Jorg Janke
 *  @version    $Id: MiniBrowser.java,v 1.6 2005/03/11 20:34:38 jjanke Exp $
 */
public class MiniBrowser extends JDialog
{
	/**
	 *  Default Constructor
	 */
	public MiniBrowser()
	{
		this (null);
	}   //  MiniBrowser

	/**
	 *  Create MiniBrowser with URL
	 *  @param url
	 */
	public MiniBrowser(String url)
	{
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		setURL (url);
		CompierePLAF.showCenterScreen(this);
	}   //  MiniBrowser

	private JScrollPane scrollPane = new JScrollPane();
	private JEditorPane editorPane = new JEditorPane();

	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		scrollPane.setPreferredSize(new Dimension(500, 500));
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(editorPane, null);
	}   //  jbInit

	/**
	 *  Set URL
	 *  @param url
	 */
	private void setURL (String url)
	{
		String myURL = url;
		if (url == null)
			myURL = "http://www.compiere.org";
		this.setTitle(myURL);

		//  Set URL
		URL realURL = null;
		try
		{
			realURL = new URL(myURL);
		}
		catch (Exception e)
		{
			System.err.println("MiniBrowser.setURL (set) - " + e.toString());
		}
		if (realURL == null)
			return;

		//  Open
		try
		{
			editorPane.setPage(realURL);
		}
		catch (Exception e)
		{
			System.err.println("MiniBrowser.setURL (open) - " + e.toString());
		}
	}   //  setURL
}   //  MiniBrowser
