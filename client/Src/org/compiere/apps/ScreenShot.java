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
package org.compiere.apps;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import javax.swing.*;
import org.compiere.util.*;

/**
 *	JPEG File Utility
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ScreenShot.java,v 1.6 2005/03/11 20:27:58 jjanke Exp $
 */
public class ScreenShot
{
	/**
	 * 	Create JPEG file from window
	 * 	@param window window
	 * 	@param fileName optional file name
	 * 	@return true if created
	 */
	public static boolean createJPEG (Window window, String fileName)
	{
		if (window == null || fileName == null)
			new IllegalArgumentException("ScreenShot.createJPEG Window os NULL");

		//	Get File
		File file = getJPGFile (window);
		if (file == null)
			return false;
		log.config("File=" + file);
		if (file.exists())
			file.delete();

		//	Get Writer
		Iterator writers = ImageIO.getImageWritersByFormatName("jpg");
		ImageWriter writer = (ImageWriter)writers.next();
		if (writer == null)
		{
			log.log(Level.SEVERE, "no ImageWriter");
			return false;
		}

		//	Get Image
		BufferedImage bi = getImage(window);

		//	Write Image
		try
		{
			ImageOutputStream ios = ImageIO.createImageOutputStream (file);
			writer.setOutput(ios);
			writer.write(bi);
			ios.flush();
			ios.close();

		}
		catch (IOException ex)
		{
			log.log(Level.SEVERE, "ex", ex);
			return false;
		}
		return true;
	}	//	createJPEG


	/**
	 * 	Get JPEG File
	 * 	@param parent parent
	 * 	@return file
	 */
	protected static File getJPGFile (Component parent)
	{
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new ExtensionFileFilter("jpg", Msg.getMsg(Env.getCtx(), "FileJPEG")));
		if (fc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION)
			return null;
		File file = fc.getSelectedFile();
		if (file == null)
			return null;
		String fileName = file.getAbsolutePath();
		if (!(fileName.toUpperCase().equals(".JPG") || fileName.toUpperCase().equals(".JPEG")))
			fileName += ".jpg";
		return new File (fileName);
	}	//	getFile

	/**
	 * 	Get Image of Window
	 * 	@param window window
	 * 	@return image
	 */
	protected static BufferedImage getImage (Window window)
	{
		BufferedImage bi = new BufferedImage (window.getWidth(), window.getHeight(),
			BufferedImage.TYPE_INT_RGB);	//	TYPE_INT_ARGB is tinted red
		window.paintAll(bi.createGraphics());
		return bi;
	}	//	getImage

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ScreenShot.class);
}	//	ScreenShot
