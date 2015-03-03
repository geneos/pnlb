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
import javax.activation.*;

/**
 *	A DataSource based on the Java Mail Example.
 *  This class implements a DataSource from:
 * 		an InputStream
 *		a byte array
 * 		a String
 * 	@author John Mani
 * 	@author Bill Shannon
 * 	@author Max Spivak
 */
public class ByteArrayDataSource
	implements DataSource
{
	/**
	 *  Create a DataSource from an input stream
	 * 	@param is stream
	 * 	@param type MIME type e.g. text/html
	 */
	public ByteArrayDataSource (InputStream is, String type)
	{
		m_type = type;
		try
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream ();
			int ch;

			while ((ch = is.read ()) != -1)
			{
				// XXX - must be made more efficient by
				// doing buffered reads, rather than one byte reads
				os.write (ch);
			}
			m_data = os.toByteArray ();
		}
		catch (IOException ioex)
		{
			System.err.println("ByteArrayDataSource - " + ioex);
		}
	}	//	ByteArrayDataSource

	/**
	 * 	Create a DataSource from a byte array
	 * 	@param data	data
	 * 	@param type type e.g. text/html
	 */
	public ByteArrayDataSource (byte[] data, String type)
	{
		m_data = data;
		m_type = type;
	}	//	ByteArrayDataSource

	/**
	 * Create a DataSource from a String
	 * @param asciiData data
	 *	Assumption that the string contains only ASCII characters!
	 * 	Otherwise just pass a charset into this constructor and use it in getBytes()
	 * @param type MIME type e.g. text/html
	 */
	public ByteArrayDataSource (String asciiData, String type)
	{
		try		//	WebEnv.ENCODING
		{
			m_data = asciiData.getBytes ("UTF-8");	//	iso-8859-1
		}
		catch (UnsupportedEncodingException uex)
		{
			System.err.println("ByteArrayDataSource - " + uex);
		}
		m_type = type;
	}	//	ByteArrayDataSource

	/**	Data			**/
	private byte[] 		m_data = null;
	/** Content Type	**/
	private String 		m_type = "text/plain";
	/**	Name			**/
	private String		m_name = null;

	/**
	 * 	Return an InputStream for the data.
	 * 	@return inputstream
	 * 	@throws IOException
	 */
	public InputStream getInputStream ()
		throws IOException
	{
		if (m_data == null)
			throw new IOException ("no data");
		//	a new stream must be returned each time.
		return new ByteArrayInputStream (m_data);
	}	//	getInputStream

	/**
	 * Throws exception
	 * @return null
	 * @throws IOException
	 */
	public OutputStream getOutputStream ()
		throws IOException
	{
		throw new IOException ("cannot do this");
	}	//	getOutputStream

	/**
	 * 	Get Content Type
	 * 	@return MIME type e.g. text/html
	 */
	public String getContentType ()
	{
		return m_type;
	}	//	getContentType

	/**
	 * 	Set Name
	 * 	@param name name
	 * 	@return this
	 */
	public ByteArrayDataSource setName(String name)
	{
		m_name = name;
		return this;
	}	//	setName

	/**
	 * 	Return Name or Class Name & Content Type
	 * 	@return dummy
	 */
	public String getName ()
	{
		if (m_name != null)
			return m_name;
		return "ByteArrayDataStream " + m_type;
	}	//	getName

}	//	ByteArrayDataStream
