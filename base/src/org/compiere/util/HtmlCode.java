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

import org.apache.ecs.*;

/**
 *  ECS Component Collection.
 *
 *  @author Jorg Janke
 *  @version $Id: HtmlCode.java,v 1.3 2005/03/11 20:26:08 jjanke Exp $
 */
public class HtmlCode extends MultiPartElement
	implements Printable
{

	public HtmlCode ()
	{
		setNeedClosingTag (false);
		setTagText ("");
		setStartTagChar (' ');
		setEndTagChar (' ');
	}

	/**
		Adds an Element to the element.
		@param  hashcode name of element for hash table
		@param  element Adds an Element to the element.
		@return this
	 */
	public HtmlCode addElement (String hashcode, Element element)
	{
		addElementToRegistry (hashcode, element);
		return (this);
	}


	/**
		Adds an Element to the element.
		@param  hashcode name of element for hash table
		@param  element Adds an Element to the element.
		@return this
	 */
	public HtmlCode addElement (String hashcode, String element)
	{
		addElementToRegistry (hashcode, element);
		return (this);
	}


	/**
		Adds an Element to the element.
		@param  element Adds an Element to the element.
		@return this
	 */
	public HtmlCode addElement (Element element)
	{
		addElementToRegistry (element);
		return (this);
	}

	/**
		Adds an Element to the element.
		@param  element Adds an Element to the element.
		@return this
	 */
	public HtmlCode addElement (String element)
	{
		addElementToRegistry (element);
		return (this);
	}

	/**
		Removes an Element from the element.
		@param hashcode the name of the element to be removed.
		@return this
	 */
	public HtmlCode removeElement (String hashcode)
	{
		removeElementFromRegistry (hashcode);
		return (this);
	}

}	//	HtmlCode
