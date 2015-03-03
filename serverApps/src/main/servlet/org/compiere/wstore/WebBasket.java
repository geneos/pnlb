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
package org.compiere.wstore;

import java.math.*;
import java.util.*;
import org.compiere.util.*;


/**
 *  Web Basket
 *
 *  @author Jorg Janke
 *  @version $Id: WebBasket.java,v 1.9 2005/10/01 23:56:02 jjanke Exp $
 */
public class WebBasket
{
	/**
	 * 	Constructor
	 */
	public WebBasket()
	{
	}	//	WebBasket

	/**	Attribute Name - also in JSPs	*/
	public static final String		NAME = "webBasket";

	/**	Logging						*/
	private CLogger			log = CLogger.getCLogger(getClass());
	/**	Lines						*/
	private ArrayList<WebBasketLine>	m_lines = new ArrayList<WebBasketLine>();
	/** Total w/o tax				*/
	private BigDecimal		m_total;
	/**	Line (max) counter			*/
	private int				m_lineNo = 0;
	private int 			m_PriceList_Version_ID = -1;
	private int 			m_PriceList_ID = -1;
	/**	Sales Rep					*/
	private int 			m_SalesRep_ID = 0;

	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("WebBasket[Lines=");
		sb.append(m_lines.size()).append(",Total=").append(m_total)
			.append(",M_PriceList_ID=" + m_PriceList_ID)
			.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Get Total
	 *	@return	total
	 */
	public BigDecimal getTotal ()
	{
		return getTotal(false);
	}	//	getTotal

	/**
	 * 	Get (recalculated) Total
	 *	@return	total
	 */
	public BigDecimal getTotal (boolean recalc)
	{
		if (recalc)
		{
			m_total = Env.ZERO;
			for (int i = 0; i < m_lines.size(); i++)
			{
				WebBasketLine wbl = (WebBasketLine)m_lines.get(i);
				m_total = m_total.add(wbl.getTotal());
			}
		}
		if (m_total == null)
			return Env.ZERO;
		return m_total;
	}	//	getTotal

	/**
	 * 	Get Line Count
	 *	@return line count
	 */
	public int getLineCount()
	{
		return m_lines.size();
	}	//	getLineCount

	/**
	 *	Get Lines
	 * 	@return lines
	 */
	public ArrayList getLines()
	{
		return m_lines;
	}	//	getLines

	/**
	 * 	Add Line
	 *	@param wbl line
	 *	@return added line
	 */
	public WebBasketLine add (WebBasketLine wbl)
	{
		wbl.setLine (m_lineNo++);
		m_lines.add (wbl);
		getTotal (true);
		return wbl;
	}	//	add
	
	/**
	 * 	Add Line.
	 * 	Adds qty to the line, if same product 
	 * 	@param M_Product_ID product
	 * 	@param Name Name
	 *	@param Qty Qty
	 * 	@param Price Price
	 *	@return created / modified line
	 */	
	public WebBasketLine add (int M_Product_ID, String Name, BigDecimal Qty, BigDecimal Price)
	{
		//	try adding to existing line
		for (int i = 0; i < m_lines.size(); i++)
		{
			WebBasketLine wbl = (WebBasketLine)m_lines.get(i);
			if (wbl.getM_Product_ID() == M_Product_ID)
			{
				wbl.addQuantity (Qty);
				getTotal (true);
				return wbl;
			}
		}
		//	new line
		WebBasketLine wbl = new WebBasketLine (M_Product_ID, Name, Qty,	Price);
		return add (wbl);
	}	//	add


	/**
	 * 	Delete Line
	 *	@param no line no
	 */
	public void delete (int no)
	{
		for (int i = 0; i < m_lines.size(); i++)
		{
			WebBasketLine wbl = (WebBasketLine)m_lines.get(i);
			if (wbl.getLine() == no)
			{
				m_lines.remove(i);
				getTotal(true);
				break;
			}
		}
	}	//	delete


	public int getM_PriceList_Version_ID()
	{
		return m_PriceList_Version_ID;
	}
	public void setM_PriceList_Version_ID(int PriceList_Version_ID)
	{
		if (PriceList_Version_ID > 0)
			m_PriceList_Version_ID = PriceList_Version_ID;
	}
	
	
	public int getM_PriceList_ID()
	{
		return m_PriceList_ID;
	}
	public void setM_PriceList_ID(int PriceList_ID)
	{
		if (PriceList_ID > 0)
			m_PriceList_ID = PriceList_ID;
	}

	
	/**
	 * @return Returns the salesRep_ID.
	 */
	public int getSalesRep_ID ()
	{
		return m_SalesRep_ID;
	}
	/**
	 * @param salesRep_ID The salesRep_ID to set.
	 */
	public void setSalesRep_ID (int salesRep_ID)
	{
		m_SalesRep_ID = salesRep_ID;
	}
}	//	WebBasket
