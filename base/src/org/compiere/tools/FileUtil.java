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
package org.compiere.tools;

import java.io.*;
import org.compiere.util.*;

/**
 *  File Utilities
 *
 *  @author Jorg Janke
 *  @version $Id: FileUtil.java,v 1.5 2005/04/27 17:44:36 jjanke Exp $
 */
public class FileUtil
{
	/**
	 *	File Utility
	 * 	@param file input file or directory
	 * 	@param filter filter
	 * 	@param action action
	 */
	public FileUtil (String file, String filter, String action, String p1, String p2)
	{
		this (new File(file), filter, action, p1, p2);
	}	//	FileUtil

	/**
	 *	File Utility
	 * 	@param file input file or directory
	 * 	@param filter filter
	 * 	@param action action
	 */
	public FileUtil (File file, String filter, String action, String p1, String p2)
	{
		if (action == null || action.length() == 0)
			System.err.println("FileUtil: No Action");
		else if (!validAction(action))
			System.err.println("FileUtil: Action not valid: " + action + ACTIONS);
		else if (file == null)
			System.err.println("FileUtil: No Input file");
		else if (!file.exists())
			System.err.println("FileUtil: Input file does not exist: " + file);
		else
		{
			System.out.println("FileUtil (" + file + ", Filter=" + filter + ", Action=" + action + ")");
			m_filterString = filter;
			processFile (file, p1, p2);
			System.out.println("FileUtil  Process count = " + m_count + "  actions=" + m_actions);
		}
	}	//	FileUtil

	private	String			m_filterString = null;
	private FileUtilFilter	m_filter = new FileUtilFilter();
	/** File Count			*/
	private int				m_count = 0;
	/** Action Count		*/
	private int				m_actions = 0;
	private int				m_actionIndex = -1;

	public static final String[] ACTIONS = new String[]
		{"List", "Replace", "Latex"};


	/**
	 * 	Is Action Valid
	 * 	@param action action
	 * 	@return true if supported
	 */
	private boolean validAction (String action)
	{
		for (int i = 0; i < ACTIONS.length; i++)
		{
			if (ACTIONS[i].equals (action))
			{
				m_actionIndex = i;
				return true;
			}
		}
		return false;
	}	//	validAction

	/**
	 * 	Process File
	 *	@param file file
	 */
	private void processFile (File file, String p1, String p2)
	{
		if (file == null)
			return;
		else if (!file.exists())
			return;
		else if (file.isDirectory())
		{
			File[] dirFiles = file.listFiles(m_filter);
			for (int i = 0; i < dirFiles.length; i++)
				processFile(dirFiles[i], p1, p2);
		}
		else
		{
			System.out.println(" ProcessFile=" + file.getAbsolutePath());
			m_count++;
			processFileAction(file, p1, p2);
		}
	}	//	processFile

	/**
	 * 	File Action
	 *	@param file file to be processed
	 */
	void processFileAction(File file, String p1, String p2)
	{
		try
		{
			if (m_actionIndex == 0)			//	List
				;
			else if (m_actionIndex == 1)	//	Replace
				replaceString (file, p1, p2);
			else if (m_actionIndex == 2)	//	Latex
				latex (file);
		}
		catch (Exception ex)
		{
		}
	}	//	processFileAction

	
	/**************************************************************************
	 * 	Replace String in File.
	 * 	@param file file
	 * 	@param from old String
	 * 	@param to new String
	 * 	@throws IOException
	 */
	private void replaceString (File file, String from, String to) throws IOException
	{
		String fileName = file.getAbsolutePath();
		BufferedReader in = new BufferedReader(new FileReader(file));
		//
		File tmpFile = new File(fileName + ".tmp");
		BufferedWriter out = new BufferedWriter (new FileWriter(tmpFile, false));
		boolean found = false;

		String line = null;
		int lineNo = 0;
		while ((line = in.readLine()) != null)
		{
			lineNo++;
			if (line.indexOf(from) != -1)
			{
				found = true;
				System.out.println("  " + lineNo + ": " + line);
				line = Util.replace(line, from, to);
				m_actions++;
			}
			out.write(line);
			out.newLine();
		}	//	while reading file
		//
		in.close();
		out.close();
		//
		if (found)
		{
			File oldFile = new File (fileName + ".old");
			if (file.renameTo(oldFile))
			{
				if (tmpFile.renameTo (new File (fileName)))
				{
					if (oldFile.delete ())
						System.out.println (" - File updated: " + fileName);
					else
						System.err.println (" - Old File not deleted - " + fileName);
				}
				else
					System.err.println (" - New File not renamed - " + fileName);
			}
			else
				System.err.println(" - Old File not renamed - " + fileName);
		}
		else
		{
			if (!tmpFile.delete())
				System.err.println(" - Temp file not deleted - " + tmpFile.getAbsolutePath());
		}
	}	//	replaceString

	
	
	/**************************************************************************
	 * 	Strip Latex specifics.
	 * 	 \textsl{\colorbox{yellow}{\textbf{Important:}}} For more information on the
		installation of the Compiere Server and the Compiere Client please refer to
		\href{http://www.compiere.org/support/index.html}{Compiere Support} for more details and the latest
		update.
	 * 	@param file file
	 * 	@throws IOException
	 */
	private void latex (File file) throws IOException
	{
		String fileName = file.getAbsolutePath();
		BufferedReader in = new BufferedReader(new FileReader(file));
		//
		File outFile = new File(fileName + ".txt");
		BufferedWriter out = new BufferedWriter (new FileWriter(outFile, false));

		String line = null;
		int lineNo = 0;

		while ((line = in.readLine()) != null)
		{
			lineNo++;
			boolean ignore = false;
			//
			char[] inLine = line.toCharArray();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < inLine.length; i++)
			{
				char c = inLine[i];
				if (c == '\\')
					ignore = true;
				else if (c == '{')
					ignore = false;
				else if (c == '}')
					;
				else if (!ignore)
					sb.append(c);
			}
			//
			out.write(sb.toString());
			out.newLine();
		}	//	while reading file
		//
		in.close();
		out.close();
		System.out.println("File " + fileName + " - lines=" + lineNo);
	}	//	latex



	/**************************************************************************
	 * 	File Filter.
	 * 	Accept directories and files matching filter
	 */
	class FileUtilFilter implements FilenameFilter
	{
		/**
		 * Accept directories and files matching filter.
		 *
		 * @param   dir    the directory in which the file was found.
		 * @param   name   the name of the file.
		 * @return  Accept directories and files matching filter
		 */
		public boolean accept (File dir, String name)
		{
		//	System.out.println("  Dir=" + dir + ", Name=" + name);
			File file = new File (dir, name);
			if (file.isDirectory())
				return true;
			if (m_filterString == null || m_filterString.length() == 0)
				return true;
			if (name == null)
				return false;
			//	ignore files with ~ and this file
			if (name.indexOf("~") != -1 || name.equals("FileUtil.java"))
				return false;
			//
			return name.indexOf(m_filterString) != -1;
		}	//	accept

	}	//	FileUtilFilter

	
	/**************************************************************************
	 *	Start
	 * 	@param args fileName filter action
	 */
	public static void main (String[] args)
	{
		String directory = "C:\\Compiere\\compiere-all2";
		String filter = ".sql";
		String action = "Replace";

		if (args.length == 1)
			directory = args[0];
		if (args.length == 2)
			filter = args[1];
		if (filter == null)
		   filter = "";
		//
		new FileUtil(directory, filter, action, 
			"ERP+CPM", "ERP+CRM" );
	}	//	main

}	//	FileUtil
