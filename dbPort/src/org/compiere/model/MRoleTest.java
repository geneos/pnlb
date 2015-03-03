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

import junit.framework.*;
import org.compiere.util.*;

/**
 * The class <code>MRoleTest</code> contains tests for the class MRole
 * <p>
 * @author Jorg Janke
 * @version $Id: MRoleTest.java,v 1.5 2005/05/17 05:30:16 jjanke Exp $
 */
public class MRoleTest extends TestCase
{
	/**
	 * Construct new test instance
	 * @param name the test name
	 */
	public MRoleTest(String name)
	{
		super(name);
	}

	private MRole m_role = null;

	/**
	 * Perform pre-test initialization
	 * @throws Exception
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception, Exception
	{
		org.compiere.Compiere.startupEnvironment(true);
		m_role = MRole.getDefault(Env.getCtx(), false);
		super.setUp();
	}

	/**
	 * Perform post-test clean up
	 *
	 * @throws Exception
	 *
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Run the String addAccessSQL(String, String, boolean, boolean) method
	 * test
	 */
	public void testAddAccessSQL()
	{
		// add test code here
		String sql = m_role.addAccessSQL(
			"SELECT r.a,r.b,r.c FROM AD_Role r WHERE EXISTS "
			+ "(SELECT AD_Column c WHERE c.a=c.b) ORDER BY 1",
			"r", 
			MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		System.out.println(sql);
		assertEquals(sql, "SELECT r.a,r.b,r.c FROM AD_Role r WHERE EXISTS (SELECT AD_Column c WHERE c.a=c.b) AND r.AD_Client_ID=0 AND r.AD_Org_ID=0 ORDER BY 1");
	}


	/**
	 * Launch the test.
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(MRoleTest.class);
	}

}	//	MRoleTest
