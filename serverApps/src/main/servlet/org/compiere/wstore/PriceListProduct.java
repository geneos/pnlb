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
import org.compiere.util.*;

/**
 *  Price List Product
 *
 *  @author Jorg Janke
 *  @version $Id: PriceListProduct.java,v 1.7 2005/03/11 20:34:46 jjanke Exp $
 */
public class PriceListProduct
{
	/**
	 * 	Price List Product.
	 * 	@param M_Product_ID product
	 * 	@param value value
	 * 	@param name name
	 * 	@param description descriprion
	 * 	@param help help
	 * 	@param documentNote document note
	 * 	@param imageURL image
	 * 	@param descriptionURL description
	 * 	@param price price
	 * 	@param uomName uom
	 * 	@param uomSymbol uom
	 */
	public PriceListProduct (int M_Product_ID, String value, String name, String description,
		String help, String documentNote, String imageURL, String descriptionURL,
		BigDecimal price, String uomName, String uomSymbol)
	{
		//
		m_Product_ID = M_Product_ID;
		m_value = value;
		m_name = name;
		m_description = description;
		//	Help, DocumentNote, ImageURL, DescriptionURL,
		m_help = help;
		m_documentNote = documentNote;
		m_imageURL = imageURL;
		m_descriptionURL = descriptionURL;
		//	PriceStd, UOMName, UOMSymbol
		m_price = price;
		m_uomName = uomName;
		m_uomSymbol = uomSymbol;
	}	//	PriceListProduct

	/**	Attribute Name				*/
	public static final String		NAME = "PriceListProduct";
	/**	Logging						*/
	private CLogger			log = CLogger.getCLogger(getClass());

	private int 			m_Product_ID;
	private String 			m_value;
	private String 			m_name;
	private String 			m_description;

	private String 			m_help;
	private String 			m_documentNote;
	private String 			m_imageURL;
	private String 			m_descriptionURL;

	private BigDecimal		m_price;
	private String			m_uomName;
	private String			m_uomSymbol;


	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("PriceListProduct[");
		sb.append(m_Product_ID).append("-").append(m_name)
			.append("-").append(m_price)
			.append("]");
		return sb.toString();
	}	//	toString

	/*************************************************************************/

	/**
	 * 	Get Product IO
	 * 	@return	M_Product_ID
	 */
	public int getId()
	{
		return m_Product_ID;
	}
	public String getValue()
	{
		return m_value;
	}
	/**
	 * 	Get Name
	 * 	@return name
	 */
	public String getName()
	{
		return m_name;
	}
	public String getDescription()
	{
		return m_description;
	}
	public String getHelp()
	{
		return m_help;
	}

	public String getDocumentNote()
	{
		return m_documentNote;
	}
	public String getImageURL()
	{
		return m_imageURL;
	}
	public String getDescriptionURL()
	{
		return m_descriptionURL;
	}

	public BigDecimal getPrice()
	{
		return m_price;
	}
	public String getUomName()
	{
		return m_uomName;
	}
	public String getUomSymbol()
	{
		return m_uomSymbol;
	}

}	//	PriceListProduct
