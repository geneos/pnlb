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

import javax.swing.filechooser.*;
import java.io.File;
import java.io.Serializable;


/**
 *	Extension File Chooser
 *
 *  @author Jorg Janke
 *  @version  $Id: ExtensionFileFilter.java,v 1.6 2005/03/11 20:26:08 jjanke Exp $
 */
public class ExtensionFileFilter extends FileFilter
	implements Serializable
{
	/**
	 *	Constructor
	 */
	public ExtensionFileFilter()
	{
		this ("","");
	}	//	ExtensionFileFilter

	/**
	 *	Constructor
	 * 	@param extension extension
	 * 	@param description description
	 */
	public ExtensionFileFilter(String extension, String description)
	{
		setDescription (description);
		setExtension (extension);
	}	//	ExtensionFileFilter

	/**	Extension			*/
	private String 		m_extension;
	//
	private String 		m_description;


	/**
	 *	Description
	 *  @return description
	 */
	public String getDescription()
	{
		return m_description;
	}
	public void setDescription(String newDescription)
	{
		m_description = newDescription;
	}

	/**
	 *	Extension
	 *  @param newExtension ext
	 */
	public void setExtension(String newExtension)
	{
		m_extension = newExtension;
	}
	public String getExtension()
	{
		return m_extension;
	}

	/**
	 *	Accept File
	 *  @param file file to be tested
	 *  @return true if OK
	 */
	public boolean accept(File file)
	{
		//	Need to accept directories
		if (file.isDirectory())
			return true;

		String ext = file.getName();
		int pos = ext.lastIndexOf('.');

		//	No extension
		if (pos == -1)
			return false;

		ext = ext.substring(pos+1);

		if (m_extension.equalsIgnoreCase(ext))
			return true;

		return false;
	}	//	accept


	/**
	 *	Verify file name with filer
	 *  @param file file
	 *  @param filter filter
	 *  @return file name
	 */
	public static String getFileName(File file, FileFilter filter)
	{
		return getFile(file, filter).getAbsolutePath();
	}	//	getFileName

	/**
	 *	Verify file with filter
	 *  @param file file
	 *  @param filter filter
	 *  @return file
	 */
	public static File getFile(File file, FileFilter filter)
	{
		String fName = file.getAbsolutePath();
		if (fName == null || fName.equals(""))
			fName = "Compiere";
		//
		ExtensionFileFilter eff = null;
		if (filter instanceof ExtensionFileFilter)
			eff = (ExtensionFileFilter)filter;
		else
			return file;
		//
		int pos = fName.lastIndexOf('.');

		//	No extension
		if (pos == -1)
		{
			fName += '.' + eff.getExtension();
			return new File(fName);
		}

		String ext = fName.substring(pos+1);

		//	correct extension
		if (ext.equalsIgnoreCase(eff.getExtension()))
			return file;

		fName += '.' + eff.getExtension();
		return new File(fName);
	}	//	getFile

}	//	ExtensionFileFilter
