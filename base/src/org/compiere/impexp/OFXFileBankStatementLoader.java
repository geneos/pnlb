/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Initial Developer is ActFact BV.
 * Copyright (C) 2003-2004 ActFact BV and Compiere Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.impexp;

import org.compiere.model.*;

import java.io.*;

import org.xml.sax.SAXException;

/**
 *	Loader for OFX bank statements (file based)
 *
 *  @author Eldir Tomassen
 *  @version $Id:
 */

public final class OFXFileBankStatementLoader extends OFXBankStatementHandler implements BankStatementLoaderInterface
{

	/**
	 * Method init
	 * @param controller MBankStatementLoader
	 * @return boolean
	 * @see org.compiere.impexp.BankStatementLoaderInterface#init(MBankStatementLoader)
	 */
	public boolean init(MBankStatementLoader controller)
	{
		boolean result = false;
		FileInputStream m_stream = null;
		try
		{
			//	Try to open the file specified as a process parameter
			if (controller.getLocalFileName() != null)
			{
				m_stream = new FileInputStream(controller.getLocalFileName());
			}
			//	Try to open the file specified as part of the loader configuration
			else if (controller.getFileName() != null)
			{
				m_stream = new FileInputStream(controller.getFileName());
			}
			else 
			{
				return result;
			}
			if (!super.init(controller))
			{
				return result;
			}
			if (m_stream == null)
			{
				return result;
			}
			result = attachInput(m_stream);
			}
		catch(Exception e)
		{
			m_errorMessage = "ErrorReadingData";
			m_errorDescription = "";
		}

		return result;
	}	//	init
	

	/**
	 * Method characters
	 * @param ch char[]
	 * @param start int
	 * @param length int
	 * @throws SAXException
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters (char ch[], int start, int length)
		throws SAXException
	{
		/*
		 * There are no additional things to do when importing from file.
		 * All data is handled by OFXBankStatementHandler
		 */
		super.characters(ch, start, length);
	}	//	characterS
	

}	//	OFXFileBankStatementLoader
