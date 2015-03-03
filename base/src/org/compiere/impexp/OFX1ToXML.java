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

import java.io.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Covert OFX 1XX (SQGML) into valid XML
 *
 *  SGML BASED OFX 1 compliant data is read from the BufferedReader
 *  passed to init. This class extends InputSream, allowing the
 *  XML compliant output data to be read from it.
 *
 *  @author Maarten Klinker
 * 	@version $Id: OFX1ToXML.java,v 1.3 2005/01/05 04:22:01 jjanke Exp $
 */
public final class OFX1ToXML extends InputStream implements Runnable
{
 	/**	Reader object					*/
	private PipedReader m_reader = new PipedReader();
	/**	Writer object					*/
	private BufferedWriter m_writer;
	/**	Temp String					*/
	private String m_ofx = "";

	/**	Logger			*/
	private CLogger	log = CLogger.getCLogger(getClass());
	
	/**
	 * Constructor for OFX1ToXML
	 * @param is InputStream
	 * @throws IOException
	 */
	public OFX1ToXML(InputStream is) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		init(br);
	}	//	OFX1ToXML
	
	/**
	 * Constructor for OFX1ToXML
	 * @param br BufferedReader
	 * @throws IOException
	 */
	public OFX1ToXML(BufferedReader br) throws IOException
	{
		init(br);	
	}	//	OFX1ToXML
	
	/**
	 * Method init
	 * @param br BufferedReader
	 * @throws IOException
	 */
	public void init(BufferedReader br) throws IOException
	{
		m_writer = new BufferedWriter(new PipedWriter(m_reader));
		String line = br.readLine();

		write("<?xml version=\"1.0\"?>\n");
		write("<?OFX ");
		while(line.indexOf("<") != 0)
		{
			if (line.length() > 0) 
			{
				write(line.replaceAll(":", "=\"") + "\" ");
			}
			line = br.readLine();
		}
		write("?>\n");

		while(line != null)
		{
			m_ofx += line + "\n";
			line = br.readLine();
		}
		br.close();

		new Thread(this).start();
	}	//i	nit

	/**
	 * Method run
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		boolean addCloseTag;
		int tag2Start;
		int tagStart;
		int tagEnd;
		String tag;
		String line = "";

		try
		{
			while(m_ofx != "")
			{
				addCloseTag = false;
				tagStart = m_ofx.indexOf("<");
				if (tagStart == -1) 
				{
					break;
				}
				tagEnd = m_ofx.indexOf(">");
				if (tagEnd <= tagStart + 1) 
				{
					throw new IOException("PARSE ERROR: Invalid tag");
				}
				tag = m_ofx.substring(tagStart + 1, tagEnd);
				if (tag.indexOf(" ") != -1) 
				{
					throw new IOException("PARSE ERROR: Invalid tag");
				}
				if (!tag.startsWith("/")) 
				{
					addCloseTag = (m_ofx.indexOf("</"+tag+">") == -1);
				}
				tag2Start = m_ofx.indexOf("<", tagEnd);
				if (m_ofx.indexOf("\n", tagEnd) < tag2Start) 
				{
					tag2Start = m_ofx.indexOf("\n", tagEnd);
				}
				if (tag2Start == -1) 
				{
					tag2Start = m_ofx.length();
				}
				line = m_ofx.substring(0, tag2Start);
				m_ofx = m_ofx.substring(tag2Start);
				if (addCloseTag) 
				{
					line += "</" + tag + ">";
				}
				write(line);
			}
			write(m_ofx);
			m_writer.close();
		}
		catch (IOException e)
		{
			log.log(Level.SEVERE, "Ofx1To2Convertor: IO Exception", e);
		}
	}	//	run

	/**
	 * Method write
	 * @param str String
	 * @throws IOException
	 */
	private void write(String str) throws IOException
	{
		m_writer.write(str, 0, str.length());
	}	//	write

	/**
	 * Method read
	 * @return int
	 * @throws IOException
	 */
	public int read() throws IOException
	{
		return m_reader.read();
	}	//	read

	/**
	 * Method read
	 * @param cbuf char[]
	 * @param off int
	 * @param len int
	 * @return int
	 * @throws IOException
	 */
	public int read(char[] cbuf, int off, int len) throws IOException
	{
		return m_reader.read(cbuf, off, len);
	}	//	read

}	//Ofx1To2Convertor
