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

import java.io.*;
import junit.framework.*;

/**
 *	The class <code>EMailTest</code> contains tests for the class 
 *	EMail
 * 	<p>
 *
 *  @author Jorg Janke
 *  @version  $Id: EMailTest.java,v 1.2 2005/07/20 19:28:28 jjanke Exp $
 */
public class EMailTest extends TestCase
{
	/**
	 * Construct new test instance
	 *
	 * @param name the test name
	 */
	public EMailTest(String name)
	{
		super(name);
	}

	String host = 	"admin.compiere.org";
	String usr = 	"jjanke";
	String pwd = 	"";
	//
	String from = 	"jjanke@compiere.org";
	//
	String to = 	"jjanke@yahoo.com";
	String to2 = 	"jjanke@acm.org";
	String to3 = 	"jorg.janke@compiere.org";

	/**
	 * Perform pre-test initialization
	 * @throws Exception
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		org.compiere.Compiere.startup (true);
	}	//	setup

	/**
	 *	Test sending to internal
	 *
	public void testInternal()
	{
		EMail emailTest = new EMail(host, from, from, "TestInternal", "Test Internal Message");
		assertEquals(emailTest.send(), EMail.SENT_OK);
	}	//	testInternal

	/**
	 *	Test sending to internal authenticated
	 *
	public void testInternalAuthenticate()
	{
		EMail emailTest = new EMail(host, from, from, "TestInternalAuthenticate", "Test Internal Authenticate Message");
		emailTest.setEMailUser(usr, pwd);
		assertEquals(emailTest.send(), EMail.SENT_OK);
	}	//	testInternalAuthenticate

	/**
	 *	Test sending to external
	 *
	public void testExternal()
	{
		EMail emailTest = new EMail(host, from, to, "TestExternal", "Test External Message");
		assertNotSame(emailTest.send(), EMail.SENT_OK);
	}	//	testExternal

	/**
	 *	Test sending to external authenticated
	 *
	public void testExternalAuthenticate()
	{
		EMail emailTest = new EMail(host, from, to, "TestExternalAuthenticate", "Test External Authenticate Message");
		emailTest.setEMailUser(usr, pwd);
		assertEquals(emailTest.send(), EMail.SENT_OK);
	}	//	testExternalAuthenticate

	/**
	 *	Test sending HTML
	 *
	public void testHTML()
	{
		EMail emailTest = new EMail(host, from, to);
		emailTest.addCc(to2);
		emailTest.setMessageHTML("TestHTML", "Test HTML Message");
		emailTest.setEMailUser(usr, pwd);
		assertEquals(emailTest.send(), EMail.SENT_OK);
	}	//	testHTML

	/**
	 *	Test sending Attachment
	 *
	public void testAttachment()
	{
		EMail emailTest = new EMail(host, from, to, "TestAttachment", "Test Attachment Message");
		emailTest.addTo(to2);
		emailTest.addCc(to3);
		emailTest.addAttachment(new File("C:\\Compiere2\\RUN_Compiere2.sh"));
		emailTest.setEMailUser(usr, pwd);
		assertEquals(emailTest.send(), EMail.SENT_OK);
	}	//	testAttachmentHTML

	/**
	 *	Test sending Attachment HTML
	 */
	public void testAttachmentHTML()
	{
		EMail emailTest = new EMail(System.getProperties(), host, from, to, null, null);
		emailTest.addTo(to2);
		emailTest.addCc(to3);
		emailTest.setMessageHTML("TestAttachmentHTML", "Test Attachment HTML Message");
		emailTest.addAttachment(new File("C:\\Compiere2\\RUN_Compiere2.sh"));
		emailTest.createAuthenticator(usr, pwd);
		assertEquals(emailTest.send(), EMail.SENT_OK);
	}	//	testAttachmentHTML

	/**
	 * Launch the test.
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(EMailTest.class);
	}	//	main

}	//	EMailTest
