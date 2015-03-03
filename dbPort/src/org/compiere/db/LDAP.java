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
package org.compiere.db;

import java.util.*;
import java.util.logging.*;

import javax.naming.*;
import javax.naming.ldap.*;
import javax.naming.directory.*;

import org.compiere.util.*;


/**
 *	LDAP Management Interface
 *	
 *  @author Jorg Janke
 *  @version $Id: LDAP.java,v 1.10 2005/10/26 00:38:20 jjanke Exp $
 */
public class LDAP
{
	/**
	 * 	Validate User
	 *	@param ldapURL provider url - e.g. ldap://dc.compiere.org
	 *	@param domain domain name = e.g. compiere.org
	 *	@param userName user name - e.g. jjanke
	 *	@param password password 
	 *	@return true if validated with ldap
	 */
	public static boolean validate (String ldapURL, String domain, String userName, String password)
	{
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		//	ldap://dc.compiere.org
		env.put(Context.PROVIDER_URL, ldapURL);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		//	jjanke@compiere.org
		StringBuffer principal = new StringBuffer (userName)
			.append("@").append(domain);
		env.put(Context.SECURITY_PRINCIPAL, principal.toString());
		env.put(Context.SECURITY_CREDENTIALS, password);
		//
		try
		{
			// Create the initial context
			InitialLdapContext ctx = new InitialLdapContext(env, null);
		//	DirContext ctx = new InitialDirContext(env);
			
			//	Test - Get the attributes
		    Attributes answer = ctx.getAttributes("");

		    // Print the answer
		//	dump (answer);
		}
		catch (AuthenticationException e)
		{
			log.info("Error: " + principal + " - " + e.getLocalizedMessage());
			return false;
		}
		catch (Exception e) 
		{
			log.log (Level.SEVERE, ldapURL + " - " + principal, e);
		    return false;
		}
		log.info("OK: " + principal);
		return true;
	}	//	validate
	
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (LDAP.class);
	
	
	/**
	 * 	Test NT
	 *	@throws LoginException
	 *
	private static void testNT () throws LoginException
	{
		try
		{
			System.out.println ("NT system ----------------------------");
			NTSystem ntsystem = new NTSystem ();
			System.out.println (ntsystem);
			System.out.println (ntsystem.getDomain ());
			System.out.println (ntsystem.getDomainSID ());
			System.out.println (ntsystem.getName ());
			System.out.println (ntsystem.getUserSID ());
			System.out.println ("NT login ----------------------------");
			NTLoginModule ntlogin = new NTLoginModule ();
			System.out.println (ntlogin);
			Map<String,String> map = new HashMap<String,String>();
			map.put ("debug", "true");
			ntlogin.initialize (null, null, null, map);
			System.out.println (ntlogin.login ());
		}
		catch (LoginException le)
		{
			System.err.println ("Authentication attempt failed" + le);
		}
	} //	testNT
	
	
	/**
	 * 	testKerberos
	 *	@throws LoginException
	 *
	private static void testKerberos ()
		throws LoginException
	{
		System.out.println ("Krb login ----------------------------");
		Map<String,String> map = new HashMap<String,String>();
		// map.put("debug", "true");
		// map.put("debugNative", "true");
		Krb5LoginModule klogin = new Krb5LoginModule ();
		System.out.println (klogin);
		map.put ("principal", "username@compiere.org");
		map.put ("credential", "pass");
		klogin.initialize (null, null, null, map);
		System.out.println (klogin.login ());
		/***********************************************************************
		 * ** No krb5.ini file found in entire system Debug is true storeKey
		 * false useTicketCache false useKeyTab false doNotPrompt false
		 * ticketCache is null KeyTab is null refreshKrb5Config is false
		 * principal is jjanke tryFirstPass is false useFirstPass is false
		 * storePass is false clearPass is false [Krb5LoginModule]
		 * authentication failed Could not load configuration file
		 * c:\winnt\krb5.ini (The system cannot find the file specified)
		 * javax.security.auth.login.LoginException: Could not load
		 * configuration file c:\winnt\krb5.ini (The system cannot find the file
		 * specified)
		 *
	} //	testKerbos
	/**/
	
	/**
	 * 	Print Attributes to System.out
	 *	@param attrs
	 */
	 private static void dump (Attributes attrs)
	{
		if (attrs == null)
		{
			System.out.println ("No attributes");
		}
		else
		{
			/* Print each attribute */
			try
			{
				for (NamingEnumeration ae = attrs.getAll (); ae.hasMore ();)
				{
					Attribute attr = (Attribute) ae.next ();
					System.out.println ("attribute: " + attr.getID ());
					/* print each value */
					for (NamingEnumeration e = attr.getAll(); 
						e.hasMore (); 
						System.out.println ("    value: " + e.next()))
						;
				}
			}
			catch (NamingException e)
			{
				e.printStackTrace ();
			}
		}
	}	//	dump
		
	 /**
	  * Test
	  *	@param args ignored
	  */
	public static void main (String[] args)
	{
		try
		{
			validate("ldap://dc.compiere.org", "compiere.org", "jjanke", "ikeepforgetting");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	//	main
	
}	//	LDAP

