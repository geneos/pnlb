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
package org.compiere.apps;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

import org.compiere.plaf.CompierePLAF;


/**
 *  Online Help Browser & Link.
 *
 *  @author     Jorg Janke
 *  @version    $Id: OnlineHelp.java,v 1.9 2005/11/14 02:10:58 jjanke Exp $
 */
public class OnlineHelp extends JEditorPane implements HyperlinkListener
{
	/**
	 *  Default Constructor
	 */
	public OnlineHelp()
	{
		super ();
		setEditable (false);
		setContentType ("text/html");
		addHyperlinkListener (this);
	}   //  OnlineHelp

	/**
	 *  Constructor
	 *  @param url URL to load
	 */
	public OnlineHelp (String url)
	{
		this();
		try
		{
			if (url != null && url.length() > 0)
				setPage(url);
		}
		catch (Exception e)
		{
			System.err.println("OnlineHelp URL=" + url + " - " + e);
		}
	}   //  OnlineHelp

	/**
	 *  Constructor
	 *  @param loadOnline load online URL
	 */
	public OnlineHelp (boolean loadOnline)
	{
		this (loadOnline ? BASE_URL : null);
	}   //  OnlineHelp

	/** Base of Online Help System      */
	protected static final String   BASE_URL = "http://www.compiere.org/help/";

	
	/**************************************************************************
	 *	Hyperlink Listener
	 *  @param e event
	 */
	public void hyperlinkUpdate (HyperlinkEvent e)
	{
	//	System.out.println("OnlineHelp.hyperlinkUpdate - " + e.getDescription() + " " + e.getURL());
		if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
			return;

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		//
		if (e instanceof HTMLFrameHyperlinkEvent)
		{
			HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
			HTMLDocument doc = (HTMLDocument)getDocument();
			doc.processHTMLFrameHyperlinkEvent(evt);
		}
		else if (e.getURL() == null)
			//	remove # of the reference
			scrollToReference(e.getDescription().substring(1));
		else
		{
			try
			{
				setPage(e.getURL());
			}
			catch (Throwable t)
			{
				System.err.println("Help.hyperlinkUpdate - " + t.toString());
				displayError("Error", e.getURL(), t);
			}
		}
		this.setCursor(Cursor.getDefaultCursor());
	}	//	hyperlinkUpdate

	/**
	 *  Set Text
	 *  @param text text
	 */
	public void setText (String text)
	{
		setBackground (CompierePLAF.getInfoBackground());
		super.setText(text);
		setCaretPosition(0);        //  scroll to top
	}   //  setText

	/**
	 *  Load URL async
	 *  @param url url
	 */
	public void setPage (final URL url)
	{
		setBackground (Color.white);
		Runnable pgm = new Runnable()
		{
			public void run()
			{
				loadPage(url);
			}
		};
		new Thread(pgm).start();
	}   //  setPage

	/**
	 *  Load Page Async
	 *  @param url url
	 */
	private void loadPage (URL url)
	{
		try
		{
			super.setPage(url);
		}
		catch (Exception e)
		{
			displayError("Error: URL not found", url, e);
		}
	}   //  loadPage

	/**
	 *  Display Error message
	 *  @param header header
	 *  @param url url
	 *  @param exception exception
	 */
	protected void displayError (String header, Object url, Object exception)
	{
		StringBuffer msg = new StringBuffer ("<HTML><BODY>");
		msg.append("<H1>").append(header).append("</H1>")
			.append("<H3>URL=").append(url).append("</H3>")
			.append("<H3>Error=").append(exception).append("</H3>")
			.append("<p>&copy;&nbsp;Compiere &nbsp; ")
			.append("<A HREF=\"").append(BASE_URL).append ("\">Online Help</A></p>")
			.append("</BODY></HTML>");
		setText(msg.toString());
	}   //  displayError

	/*************************************************************************/

	/** Online links.
	 *  Key=AD_Window_ID (as String) - Value=URL
	 */
	private static HashMap<String,String>	s_links = new HashMap<String,String>();
	static
	{
		new Worker (BASE_URL, s_links).start();
	}

	/**
	 *  Is Online Help available.
	 *  @return true if available
	 */
	public static boolean isAvailable()
	{
		return s_links.size() != 0;
	}   //  isAvailable

}   //  OnlineHelp

/**
 *  Online Help Worker
 */
class Worker extends Thread
{
	/**
	 *  Worker Constructor
	 *  @param urlString url
	 *  @param links links
	 */
	Worker (String urlString, HashMap<String,String> links)
	{
		m_urlString = urlString;
		m_links = links;
		setPriority(Thread.MIN_PRIORITY);
	}   //  Worker

	private String      m_urlString = null;
	private HashMap<String,String>     m_links = null;

	/**
	 *  Worker: Read available Online Help Pages
	 */
	public void run()
	{
		if (m_links == null)
			return;
		URL url = null;
		try
		{
			url = new URL (m_urlString);
		}
		catch (Exception e)
		{
			System.err.println("OnlineHelp.Worker.run (url) - " + e);
		}
		if (url == null)
			return;

		//  Read Reference Page
		try
		{
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			HTMLEditorKit kit = new HTMLEditorKit();
			HTMLDocument doc = (HTMLDocument)kit.createDefaultDocument();
			doc.putProperty("IgnoreCharsetDirective", new Boolean(true));
			kit.read (new InputStreamReader(is), doc, 0);

			//  Get The Links to the Help Pages
			HTMLDocument.Iterator it = doc.getIterator(HTML.Tag.A);
			Object target = null;
			Object href = null;
			while (it != null && it.isValid())
			{
				AttributeSet as = it.getAttributes();
				//	~ href=/help/100/index.html target=Online title=My Test
			//	System.out.println("~ " + as);

				//  key keys
				if (target == null || href == null)
				{
					Enumeration en = as.getAttributeNames();
					while (en.hasMoreElements())
					{
						Object o = en.nextElement();
						if (target == null && o.toString().equals("target"))
							target = o;		//	javax.swing.text.html.HTML$Attribute
						else if (href == null && o.toString().equals("href"))
							href = o;
					}
				}

				if ("Online".equals(as.getAttribute(target)))
				{
					//  Format: /help/<AD_Window_ID>/index.html
					String hrefString = (String)as.getAttribute(href);
					if (hrefString != null)
					{
						try
						{
					//		System.err.println(hrefString);
							String AD_Window_ID = hrefString.substring(hrefString.indexOf('/',1), hrefString.lastIndexOf('/'));
							m_links.put(AD_Window_ID, hrefString);
						}
						catch (Exception e)
						{
							System.err.println("OnlineHelp.Worker.run (help) - " + e);
						}
					}
				}
				it.next();
			}
			is.close();
		}
		catch (ConnectException e)
		{
		//	System.err.println("OnlineHelp.Worker.run URL=" + url + " - " + e);
		}
		catch (UnknownHostException uhe)
		{
		//	System.err.println("OnlineHelp.Worker.run " + uhe);
		}
		catch (Exception e)
		{
			System.err.println("OnlineHelp.Worker.run (e) " + e);
		//	e.printStackTrace();
		}
		catch (Throwable t)
		{
			System.err.println("OnlineHelp.Worker.run (t) " + t);
		//	t.printStackTrace();
		}
	//	System.out.println("OnlineHelp - Links=" + m_links.size());
	}   //  run

	/**
	 * 	Diagnostics
	 * 	@param doc html document
	 * 	@param tag html tag
	 */
	private void dumpTags (HTMLDocument doc, HTML.Tag tag)
	{
		System.out.println("Doc=" + doc.getBase() + ", Tag=" + tag);
		HTMLDocument.Iterator it = doc.getIterator(tag);
		while (it != null && it.isValid())
		{
			AttributeSet as = it.getAttributes();
			System.out.println("~ " + as);
			it.next();
		}
	}	//	printTags

}   //  Worker
