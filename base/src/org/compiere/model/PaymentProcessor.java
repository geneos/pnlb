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

import java.io.*;
import java.math.*;
import java.util.*;
import java.util.logging.*;
import java.net.*;
import javax.net.ssl.*;

import org.compiere.util.*;

/**
 *  Payment Processor Abstract Class
 *
 *  @author Jorg Janke
 *  @version $Id: PaymentProcessor.java,v 1.17 2005/11/20 22:39:48 jjanke Exp $
 */
public abstract class PaymentProcessor
{
	/**
	 *  Public Constructor
	 */
	public PaymentProcessor()
	{
	}   //  PaymentProcessor

	/**	Logger							*/
	protected CLogger			log = CLogger.getCLogger (getClass());
	/** Payment Processor Logger		*/
	static private CLogger		s_log = CLogger.getCLogger (PaymentProcessor.class);
	/** Encoding (ISO-8859-1 - UTF-8) 		*/
	public static final String	ENCODING = "UTF-8";
	/** Encode Parameters		*/
	private boolean 			m_encoded = false;
	/** Ampersand				*/
	public static final char	AMP = '&'; 
	/** Equals					*/
	public static final char	EQ = '='; 

	/**
	 *  Factory
	 * 	@param mpp payment processor model
	 * 	@param mp payment model
	 *  @return initialized PaymentProcessor or null
	 */
	public static PaymentProcessor create (MPaymentProcessor mpp, MPayment mp)
	{
		s_log.info("create for " + mpp);
		String className = mpp.getPayProcessorClass();
		if (className == null || className.length() == 0)
		{
			s_log.log(Level.SEVERE, "No PaymentProcessor class name in " + mpp);
			return null;
		}
		//
		PaymentProcessor myProcessor = null;
		try
		{
			Class ppClass = Class.forName(className);
			if (ppClass != null)
				myProcessor = (PaymentProcessor)ppClass.newInstance();
		}
		catch (Error e1)    //  NoClassDefFound
		{
			s_log.log(Level.SEVERE, className + " - Error=" + e1.getMessage());
			return null;
		}
		catch (Exception e2)
		{
			s_log.log(Level.SEVERE, className, e2);
			return null;
		}
		if (myProcessor == null)
		{
			s_log.log(Level.SEVERE, "no class");
			return null;
		}

		//  Initialize
		myProcessor.p_mpp = mpp;
		myProcessor.p_mp = mp;
		//
		return myProcessor;
	}   //  create

	/*************************************************************************/

	protected MPaymentProcessor p_mpp = null;
	protected MPayment			p_mp = null;
	//
	private int     m_timeout = 30;

	/*************************************************************************/

	/**
	 *  Process CreditCard (no date check)
	 *  @return true if processed successfully
	 *  @throws IllegalArgumentException
	 */
	public abstract boolean processCC () throws IllegalArgumentException;

	/**
	 *  Payment is procesed successfully
	 *  @return true if OK
	 */
	public abstract boolean isProcessedOK();

	
	/**************************************************************************
	 * 	Set Timeout
	 * 	@param newTimeout timeout
	 */
	public void setTimeout(int newTimeout)
	{
		m_timeout = newTimeout;
	}
	public int getTimeout()
	{
		return m_timeout;
	}

	
	/**************************************************************************
	 *  Check for delimiter fields &= and add length of not encoded
	 *  @param name name
	 *  @param value value
	 *  @param maxLength maximum length
	 *  @return name[5]=value or name=value
	 */
	protected String createPair(String name, BigDecimal value, int maxLength)
	{
		if (value == null)
			return createPair (name, "0", maxLength);
		else
		{
			if (value.scale() < 2)
				value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
			return createPair (name, String.valueOf(value), maxLength);
		}
	}	//	createPair

	/**
	 *  Check for delimiter fields &= and add length of not encoded
	 *  @param name name
	 *  @param value value
	 *  @param maxLength maximum length
	 *  @return name[5]=value or name=value
	 */
	protected String createPair(String name, int value, int maxLength)
	{
		if (value == 0)
			return "";
		else
			return createPair (name, String.valueOf(value), maxLength);
	}	//	createPair

	/**
	 *  Check for delimiter fields &= and add length of not encoded
	 *  @param name name
	 *  @param value value
	 *  @param maxLength maximum length
	 *  @return name[5]=value or name=value 
	 */
	protected String createPair(String name, String value, int maxLength)
	{
		//  Nothing to say
		if (name == null || name.length() == 0
			|| value == null || value.length() == 0)
			return "";
		
		if (value.length() > maxLength)
			value = value.substring(0, maxLength);
		
		StringBuffer retValue = new StringBuffer(name);
		if (m_encoded)
			try
			{
				value = URLEncoder.encode(value, ENCODING);
			}
			catch (UnsupportedEncodingException e)
			{
				log.log(Level.SEVERE, value + " - " + e.toString());
			}
		else if (value.indexOf(AMP) != -1 || value.indexOf(EQ) != -1)
			retValue.append("[").append(value.length()).append("]");
		//
		retValue.append(EQ);
		retValue.append(value);
		return retValue.toString();
	}   // createPair
	
	public void setEncoded (boolean doEncode)
	{
		m_encoded = doEncode;
	}	//	setEncode
	public boolean isEncoded()
	{
		return m_encoded;
	}	//	setEncode
	
	/**
	 * 	Get Connect Post Properties
	 *	@param urlString POST url string
	 *	@param parameter parameter
	 *	@return result as properties
	 */
	protected Properties getConnectPostProperties (String urlString, String parameter)
	{
		long start = System.currentTimeMillis();
		String result = connectPost(urlString, parameter);
		if (result == null)
			return null;
		Properties prop = new Properties();
		try
		{
			String info = URLDecoder.decode(result, ENCODING);
			StringTokenizer st = new StringTokenizer(info, "&");	//	AMP
			while (st.hasMoreTokens())
			{
				String token = st.nextToken();
				int index = token.indexOf('=');
				if (index == -1)
					prop.put(token, "");
				else
				{
					String key = token.substring(0, index);
					String value = token.substring(index+1);
					prop.put(key, value);
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, result, e);
		}
		long ms = System.currentTimeMillis() - start;
		log.fine(ms + "ms - " + prop.toString());
		return prop;
	}	//	connectPost
	
	/**
	 * 	Connect via Post
	 *	@param urlString url destination (assuming https)
	 *	@param parameter parameter
	 *	@return response or null if failure
	 */
	protected String connectPost (String urlString, String parameter)
	{
		String response = null;
		try
		{
			// open secure connection
			URL url = new URL(urlString);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		//	URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			log.fine(connection.getURL().toString());

			// POST the parameter
			DataOutputStream out = new DataOutputStream (connection.getOutputStream());
			out.write(parameter.getBytes());
			out.flush();
			out.close();

			// process and read the gateway response
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			response = in.readLine();
			in.close();	                     // no more data
			log.finest(response);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, urlString, e);
		}
		//
	    return response;
	}	//	connectPost
		
}   //  PaymentProcessor
