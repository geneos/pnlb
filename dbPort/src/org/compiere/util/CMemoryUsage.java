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

import java.lang.management.*;
import java.text.*;

/**
 * 	Memory Usage Info
 *	
 *  @author Jorg Janke
 *  @version $Id: CMemoryUsage.java,v 1.1 2005/12/05 02:38:07 jjanke Exp $
 */
public class CMemoryUsage extends MemoryUsage
{
	/**
	 * 	Detail Constructor
	 *	@param init init
	 *	@param used used
	 *	@param committed committed
	 *	@param max max
	 */
	public CMemoryUsage (long init, long used, long committed, long max)
	{
		super (init, used, committed, max);
	}	//	CMemoryUsage
	
	/**
	 * 	Parent Constructor
	 *	@param usage usage
	 */
	public CMemoryUsage (MemoryUsage usage)
	{
		super (usage.getInit(), usage.getUsed(), usage.getCommitted(), usage.getMax());
	}	//	CMemoryUsage

	/**	Format						*/
	private static DecimalFormat s_format =	DisplayType.getNumberFormat(DisplayType.Integer);

	
	/**
	 * 	Get Free (Committed-Used) Memory
	 *	@return memory
	 */
	public long getFree()
	{
		return getCommitted() - getUsed();
	}	//	getFree
	
	/**
	 * 	Get Free (Committed-Used) Memory Percent
	 *	@return memory
	 */
	public int getFreePercent()
	{
		long base = getCommitted();
		long no = getFree() * 100;
		if (no == 0)
			return 0;
		long percent = no/base;
		return (int)percent;
	}	//	getFree

	/**
	 * 	Get Committed (Max-Committed) Memory Percent
	 *	@return memory
	 */
	public int getCommittedPercent()
	{
		long base = getMax();
		long no = getCommitted() * 100;
		if (no == 0)
			return 0;
		long percent = no/base;
		return (int)percent;
	}	//	getCommittedPercent

	/**
	 * 	Format k/M
	 *	@param info
	 *	@return string info
	 */
	private String format (long info)
	{
		long infoK = info / 1024;
		if (infoK == 0)
			return String.valueOf(info);
		long infoM = infoK / 1024;
		if (infoM == 0)
			return s_format.format(info);
		return s_format.format(infoK) + "k";		
	}	//	format
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ();
		sb.append ("Init=").append(format(getInit()))
			.append (", Used=").append(format(getUsed()))
			.append (", Free=").append(format(getFree()))
				.append(" ").append(getFreePercent())
			.append ("%, Committed=").append(format(getCommitted()))
				.append(" ").append(getCommittedPercent())
			.append ("%, Max=").append (format(getMax()));
		return sb.toString ();
	}	//	toString
	
}	//	CMemoryUsage
