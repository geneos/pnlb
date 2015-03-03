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
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Optimal Payment Processor Services Interface.
 *	
 *  @author Jorg Janke
 *  @version $Id: PP_Optimal.java,v 1.5 2005/11/28 03:36:03 jjanke Exp $
 */
public class PP_Optimal extends PaymentProcessor
	implements Serializable
{

	public PP_Optimal ()
	{
		super ();
	}	//	PP_Optimal

	/**	Status					*/
	private boolean		m_ok = false;
	/** Optimal Client Version	*/
	private static final String	_CLIENT_VERSION = "1.1";
	
	//	Mandatory parameters
	public static final String MERCHANT_ID          = "merchantId";
	public static final String MERCHANT_PWD         = "merchantPwd";
	public static final String ACCOUNT_ID           = "account";
	public static final String CIPHER               = "Cipher";
	public static final String PAYMENT_SERVER       = "PaymentServerURL";
	public static final String PAYMENT_SERVER_PORT  = "PaymentServerPort";
	public static final String PROXY_SERVER         = "ProxyServer";
	public static final String PROXY_PORT           = "ProxyPort";
	public static final String PROXY_USERID         = "ProxyUserId";
	public static final String PROXY_PWD            = "ProxyPwd";
	public static final String ACTIVE_RECOVERY      = "ActiveRecovery";
	public static final String HTTPVERSION          = "HTTPVersion";
	  
	//	Optional parameters
	public static final String RETRIES              = "Retries";
	public static final String TIMEOUT              = "Timeout";
	public static final String LOGLEVEL             = "LogLevel";
	public static final String LOG_BASE_PATH        = "LogBasePath";
	public static final String LOG_FILENAME         = "LogFilename";
	public static final String LOG_MAX_SIZE         = "LogMaxSize";
	public static final String LOG_MAX_BACKUP       = "LogMaxBackupFiles";

	//	some request parameters
	public static final String MERCHANT_TXN         = "merchantTxn";
	public static final String MERCHANT_DATA        = "merchantData";
	public static final String CLIENT_VERSION       = "clientVersion";
	public static final String TXN_NUMBER           = "txnNumber";
	public static final String CARD_NUMBER          = "cardNumber";
	public static final String CARD_EXPIRATION      = "cardExp";
	public static final String CARD_TYPE            = "cardType";
	public static final String STREET               = "streetAddr";
	public static final String STREET2              = "streetAddr2";
	public static final String EMAIL                = "email";
	public static final String PHONE                = "phone";
	public static final String PROVINCE             = "province";
	public static final String COUNTRY              = "country";
	public static final String CITY                 = "city";
	public static final String ZIP                  = "zip";
	public static final String CVD_INDICATOR        = "cvdIndicator";
	public static final String CVD_INDICATOR_None   = "0";
	public static final String CVD_INDICATOR_Provided = "1";
	public static final String CVD_VALUE	        = "cvdValue";
	public static final String CVD_INFO	            = "cvdInfo";
	public static final String CUST_NAME1           = "custName1";
	public static final String CUST_NAME2           = "custName2";
	  
	//	some response parameters
	public static final String STATUS               = "status";
	public static final String TXN_TYPE             = "txnType";
	public static final String AMOUNT               = "amount";
	public static final String CURRENT_AMOUNT       = "curAmount";
	public static final String ERROR_CODE           = "errCode";
	public static final String ERROR_STRING         = "errString";
	public static final String SUB_ERROR            = "subError";
	public static final String SUB_ERROR_STRING     = "subErrorString";

	public static final String AUTH_CODE            = "authCode";
	public static final String AUTH_TIME            = "authTime";
	public static final String AVS_INFO             = "avsInfo";
	  

	//	some useful constants
	public static final String QUERY_OPERATION          = "Q";
	public static final String FAILURE_LOOKUP_OPERATION = "FT";
	/** Operation p1-8					*/
	public static final String OPERATION                = "operation";
	public static final String OPERATION_Purchase       = "P";
	public static final String OPERATION_Authorization  = "A";
	public static final String OPERATION_Settlement 	= "S";
	
	
	
	//	Stratus returned by Failure Lookup Request
	public static final String STATUS_SEARCH_COMPLETED  = "status=C" ;
	//	Status returned by a Query Request
	public static final String REQUEST_PENDING          = "status=P";
	public static final String REQUEST_COMPLETE         = "status=C";
	public static final String REQUEST_FAILED           = "status=F";
	public static final String AUTHORIZATION_COMPLETE   = "status=A";
	public static final String AUTHORIZATION_FAILED     = "status=AF";
	public static final String SETTLEMENT_COMPLETE      = "status=S";
	public static final String SETTLEMENT_FAILED        = "status=SF";
	public static final String MANUAL_INTERVENTION      = "status=M";
	public static final String REQUEST_ABORTED          = "status=AB";
	public static final String REQUEST_NOT_FOUND        = "status=NF";
	public static final String UNKNOWN_TYPE             = "status=U";
	public static final String REQUEST_ERROR            = "status=E";
	/**	AVS Codes					*/
	public static Properties	AVSCodes = new Properties();
	/**	Card Types					*/
	public static Properties	CARDTypes = new Properties();
	/**	CVD Info					*/
	public static Properties	CVDInfo = new Properties();
	
	static
	{
		//	Page 1-9
		AVSCodes.put("X", "Exact. Nine digit zip and address match");
		AVSCodes.put("Y", "Yes. Five digit zip and address match");
		AVSCodes.put("A", "Address matches, Zip not");
		AVSCodes.put("W", "Nine digit zip matches, address not");
		AVSCodes.put("Z", "Five digit zip matches, address not");
		AVSCodes.put("N", "No Part matches");
		AVSCodes.put("U", "Address info unabailable");
		AVSCodes.put("R", "Retry");
		AVSCodes.put("S", "AVS not supported");
		AVSCodes.put("E", "AVS not supported for this industry");
		AVSCodes.put("B", "AVS not performed");
		AVSCodes.put("Q", "Unknown response from issuer");
		//	Page 1-14
		CARDTypes.put("AMEX", "AM");
		CARDTypes.put("DINERS", "DI");
		CARDTypes.put("VISA", "VI");
		//	CVD Info 1-20
		CVDInfo.put("M", "Match");
		CVDInfo.put("N", "No Match");
		CVDInfo.put("P", "Not Processed");
		CVDInfo.put("S", "Not Present");
		CVDInfo.put("U", "Issuer not certified");
	}
	
	/**
	 *  Get Version
	 *  @return version
	 */
	public String getVersion()
	{
		return "Optimal " + _CLIENT_VERSION;
	}   //  getVersion

	/**
	 * 	Process CC
	 *	@return true if OK
	 *	@throws IllegalArgumentException
	 */
	public boolean processCC ()
		throws IllegalArgumentException
	{
		log.fine(p_mpp.getHostAddress() + ":" + p_mpp.getHostPort() + ", Timeout=" + getTimeout()
			+ "; Proxy=" + p_mpp.getProxyAddress() + ":" + p_mpp.getProxyPort() + " " + p_mpp.getProxyLogon() + " " + p_mpp.getProxyPassword());
		setEncoded(true);

		String urlString = p_mpp.getHostAddress();
			//	"https://realtime.firepay.com/servlet/DPServlet";
			//	"https://realtime.test.firepay.com/servlet/DPServlet";
		if (p_mpp.getHostPort() != 0)
			urlString += ":" + p_mpp.getHostPort();
		
		/** General Parameters			*/
		StringBuffer param = new StringBuffer(200);
		//	 Merchant username and password.
		param.append(createPair(MERCHANT_ID, p_mpp.getUserID(), 80))
			.append(AMP).append(createPair(MERCHANT_PWD, p_mpp.getPassword(), 20))
			.append(AMP).append(createPair(ACCOUNT_ID, p_mpp.getPartnerID(), 10));
	//	param.append(AMP).append(createPair(MERCHANT_DATA, "comment", 255));

		/**	Cipher supported : 	SSL_RSA_WITH_RC4_128_MD5, SSL_RSA_WITH_RC4_128_SHA,	SSL_RSA_WITH_DES_CBC_SHA, SSL_RSA_WITH_3DES_EDE_CBC_SHA, SSL_RSA_EXPORT_WITH_RC4_40_MD5
		param.append("&Cipher=SSL_RSA_WITH_RC4_128_MD5");
		//	HTTP Version
		param.append("&HTTPVersion=1.0");
		//	Path to the keystore (cacerts) file.
		**/

		param.append(AMP).append(createPair(CARD_TYPE, "VI", 6));
		param.append(AMP).append(createPair(CARD_NUMBER, p_mp.getCreditCardNumber(), 19));
		param.append(AMP).append(createPair(CARD_EXPIRATION, p_mp.getCreditCardExp("/"), 5));
		param.append(AMP).append(createPair(AMOUNT, p_mp.getPayAmtInCents(), 10));
		param.append(AMP).append(createPair(OPERATION, OPERATION_Purchase, 1));
		param.append(AMP).append(createPair(MERCHANT_TXN, p_mp.getC_Payment_ID(), 255));
		param.append(AMP).append(createPair(CLIENT_VERSION, _CLIENT_VERSION, 4));
		param.append(AMP).append(createPair(CUST_NAME1, p_mp.getA_Name(), 255));
		param.append(AMP).append(createPair(STREET, p_mp.getA_Street(), 255));
		param.append(AMP).append(createPair(CITY, p_mp.getA_City(), 255));
		param.append(AMP).append(createPair(PROVINCE, p_mp.getA_State(), 2));
		param.append(AMP).append(createPair(ZIP, p_mp.getA_Zip(), 10));
		param.append(AMP).append(createPair(COUNTRY, p_mp.getA_Country(), 2));
	//	param.append(AMP).append(createPair("&phone", p_mp.getA_Phone(), 40));
		param.append(AMP).append(createPair("&email", p_mp.getA_EMail(), 40));
		param.append(AMP).append(createPair(CVD_INDICATOR, CVD_INDICATOR_Provided, 1));
		param.append(AMP).append(createPair(CVD_VALUE, "123", 4));
		
		try
		{
			log.fine("-> " + param.toString());
			Properties prop = getConnectPostProperties(urlString, param.toString());
			m_ok = prop != null;
			//	authCode=, authTime=1132330817, subErrorString=Card has expired: 04/04, errCode=91, clientVersion=1.1, status=E, subError=0, actionCode=CP, errString=Invalid Payment Information. Please verify request parameters.
			//	authCode=197705, authTime=1132336527, curAmount=0, avsInfo=B, clientVersion=1.1, status=SP, amount=200, cvdInfo=M, txnNumber=1000000
			if (m_ok)
			{
				String status = prop.getProperty(STATUS);
				m_ok = status != null && status.equals("SP");	//	Successful Purchase
				String authCode = prop.getProperty(AUTH_CODE);
				String authTime = prop.getProperty(AUTH_TIME);
				//
				String errCode = prop.getProperty(ERROR_CODE);
				String errString = prop.getProperty(ERROR_STRING);
				String subError = prop.getProperty(SUB_ERROR);
				String subErrorString = prop.getProperty(SUB_ERROR_STRING);
				String actionCode = prop.getProperty("actionCode");
				//
				String authorisedAmount = prop.getProperty(CURRENT_AMOUNT);
				String amount = prop.getProperty(AMOUNT);
				String avsInfo = prop.getProperty(AVS_INFO);
				String cvdInfo = prop.getProperty(CVD_INFO);
				
				log.fine("<- Status=" + status + ", AuthCode=" + authCode + ", Error=" + errString);
			}
			if (!m_ok)
				log.warning("<- " + prop);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, param.toString(), e);
			m_ok = false;
		}
		return m_ok;
	}	//	processCC

	/**
	 * 	Is Processed OK
	 *	@return true of ok
	 */
	public boolean isProcessedOK ()
	{
		return m_ok;
	}	//	isProcessedOK
	

	
	/**
	 * 	Test
	 *	@param args ifnored
	 */
	public static void main (String[] args)
	{
/**
> Test Administration Server and Login
> -----------------------------------
> The information you require to access the TEST administration server 
> is (case sensitivity matters):
> 
> Web Site: https://admin.test.firepay.com
> 
> Test Account #2
> Username: sparctwo001
> User password: abcd1234
> 
> Test Payment Server URL
> -----------------------
> To connect your web site to our TEST payment service, your technical 
> people will use the following URL:
> 
> https://realtime.test.firepay.com/servlet/DPServlet
> 
> Test Cards
> ----------
> Below are the cards you can use in the test environment. Transactions 
> done with these cards will either be successful or fail depending on 
> the amount provided with the transaction (see below).
> 
> Visa:
> 4387751111011
> 4387751111029
> 4387751111111038
> 4387751111111053
> 
> MasterCard:
> 5442981111111015
> 5442981111111023
> 5442981111111031
> 5442981111111056
> 
> The following amounts will cause either approval or various declines 
> with the cards mentioned above:
> Amount less than 20.00 = Approval
> 20.00 to 29.99 = (221, 1002) Reenter
> 30.00 to 39.99 = (221, 1003) Referral
> 40.00 to 49.99 = (221, 1004) PickUp
> 50.00 to 59.99 = (34, 1005) Decline
> 60.00 to 69.99 = (2) Timeout
> Amount greater than 69.99 = Approval
**/
		CLogMgt.initialize(true);
		CLogMgt.setLevel(Level.ALL);
		PP_Optimal pp = new PP_Optimal();
		pp.processCC();
		pp.isProcessedOK();
		
	}	//	main
	
}	//	PP_Optimal
