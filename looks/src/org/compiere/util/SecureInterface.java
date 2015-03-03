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

import java.math.*;
import java.sql.*;

/**
 * 	Compiere Security Interface.
 * 	To enable your own class, you need to set the property COMPIERE_SECURE 
 * 	when starting the client or server.
 *  The setting for the default class would be:
 *  -DCOMPIERE_SECURE=org.compiere.util.Secure
 *	
 *  @author Jorg Janke
 *  @version $Id: SecureInterface.java,v 1.1 2005/07/20 19:30:06 jjanke Exp $
 */
public interface SecureInterface
{
	/** Class Name implementing SecureInterface	*/
	public static final String	COMPIERE_SECURE = "COMPIERE_SECURE";
	/** Default Class Name implementing SecureInterface	*/
	public static final String	COMPIERE_SECURE_DEFAULT = "org.compiere.util.Secure";

	
	/** Clear Text Indicator xyz	*/
	public static final String		CLEARVALUE_START = "xyz";
	/** Clear Text Indicator		*/
	public static final String		CLEARVALUE_END = "";
	/** Encrypted Text Indiactor ~	*/
	public static final String		ENCRYPTEDVALUE_START = "~";
	/** Encrypted Text Indiactor ~	*/
	public static final String		ENCRYPTEDVALUE_END = "~";

	
	/**
	 *	Encryption.
	 *  @param value clear value
	 *  @return encrypted String
	 */
	public String encrypt (String value);

	/**
	 *	Decryption.
	 *  @param value encrypted value
	 *  @return decrypted String
	 */
	public String decrypt (String value);

	/**
	 *	Encryption.
	 * 	The methods must recognize clear text values
	 *  @param value clear value
	 *  @return encrypted String
	 */
	public Integer encrypt (Integer value);

	/**
	 *	Decryption.
	 * 	The methods must recognize clear text values
	 *  @param value encrypted value
	 *  @return decrypted String
	 */
	public Integer decrypt (Integer value);
	
	/**
	 *	Encryption.
	 * 	The methods must recognize clear text values
	 *  @param value clear value
	 *  @return encrypted String
	 */
	public BigDecimal encrypt (BigDecimal value);

	/**
	 *	Decryption.
	 * 	The methods must recognize clear text values
	 *  @param value encrypted value
	 *  @return decrypted String
	 */
	public BigDecimal decrypt (BigDecimal value);

	/**
	 *	Encryption.
	 * 	The methods must recognize clear text values
	 *  @param value clear value
	 *  @return encrypted String
	 */
	public Timestamp encrypt (Timestamp value);

	/**
	 *	Decryption.
	 * 	The methods must recognize clear text values
	 *  @param value encrypted value
	 *  @return decrypted String
	 */
	public Timestamp decrypt (Timestamp value);
	
	
	/**
	 *  Convert String to Digest.
	 *  JavaScript version see - http://pajhome.org.uk/crypt/md5/index.html
	 *
	 *  @param value message
	 *  @return HexString of message (length = 32 characters)
	 */
	public String getDigest (String value);

	/**
	 * 	Checks, if value is a valid digest
	 *  @param value digest string
	 *  @return true if valid digest
	 */
	public boolean isDigest (String value);
	
}	//	SecureInterface
