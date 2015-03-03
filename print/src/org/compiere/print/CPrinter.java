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
package org.compiere.print;

import java.awt.event.*;
import java.awt.print.*;
import javax.print.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Compiere Printer Selection
 *
 *  @author     Jorg Janke
 *  @version    $Id: CPrinter.java,v 1.7 2005/03/11 20:34:40 jjanke Exp $
 */
public class CPrinter extends CComboBox implements ActionListener 
{
	/**
	 *  Create PrinterJob
	 */
	public CPrinter()
	{
		super(getPrinterNames());
		//  Set Default
		setValue(Ini.getProperty(Ini.P_PRINTER));
		this.addActionListener(this);
	}   //  CPrinter

	/**
	 * 	Action Listener
	 * 	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{

	}	//	actionPerformed

	/**
	 * 	Get PrintService
	 * 	@return print service
	 */
	public PrintService getPrintService()
	{
		String currentService = (String)getSelectedItem();
		for (int i = 0; i < s_services.length; i++)
		{
			if (s_services[i].getName().equals(currentService))
				return s_services[i];
		}
		return PrintServiceLookup.lookupDefaultPrintService();
	}	//	getPrintService


	/*************************************************************************/

	/** Available Printer Services  */
//	private static PrintService[]   s_services = PrinterJob.lookupPrintServices();
	private static PrintService[]   s_services = PrintServiceLookup.lookupPrintServices(null,null);

	/**
	 *  Get Print (Services) Names
	 *  @return Printer Name array
	 */
	public static String[] getPrinterNames()
	{
		String[] retValue = new String[s_services.length];
		for (int i = 0; i < s_services.length; i++)
			retValue[i] = s_services[i].getName();
		return retValue;
	}   //  getPrintServiceNames

	/*************************************************************************/

	/**
	 *  Return default PrinterJob
	 *  @return PrinterJob
	 */
	public static PrinterJob getPrinterJob()
	{
		return getPrinterJob(Ini.getProperty(Ini.P_PRINTER));
	}   //  getPrinterJob

	/**
	 *  Return PrinterJob with selected printer name.
	 *  @param printerName if null, get default printer (Ini)
	 *  @return PrinterJob
	 */
	public static PrinterJob getPrinterJob (String printerName)
	{
		PrinterJob pj = PrinterJob.getPrinterJob();
		PrintService ps = null;

		//  find printer service
		if (printerName == null || printerName.length() == 0)
			printerName = Ini.getProperty(Ini.P_PRINTER);
		if (printerName != null && printerName.length() != 0)
		{
		//	System.out.println("CPrinter.getPrinterJob - searching " + printerName);
			for (int i = 0; i < s_services.length; i++)
			{
				String serviceName = s_services[i].getName();
				if (printerName.equals(serviceName))
				{
					ps = s_services[i];
		//			System.out.println("CPrinter.getPrinterJob - found " + printerName);
					break;
				}
		//		System.out.println("CPrinter.getPrinterJob - not: " + serviceName);
			}
		}   //  find printer service

		try
		{
			if (ps != null)
				pj.setPrintService(ps);
			else
				return null;
		}
		catch (Exception e)
		{
			System.err.println("CPrinter.getPrinterJob - " + e.toString());
			return null;
		}
		//
		String serviceName = pj.getPrintService().getName();
		if (printerName != null && !printerName.equals(serviceName))
			System.err.println("CPrinter.getPrinterJob - Not found: " + printerName + " - Used: " + serviceName);
		return pj;
	}   //  getPrinterJob


}   //  CPrinter
