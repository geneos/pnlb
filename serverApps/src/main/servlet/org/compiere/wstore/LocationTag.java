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

import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.apache.ecs.*;
import org.apache.ecs.xhtml.*;
import org.apache.taglibs.standard.tag.el.core.*;
import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Location City - Postal - Region - Country (Address).
 *  <pre>
 *	<cws:location country="${webUser.country}" regionID="${webUser.regionID}" regionName="${webUser.regionName}" city="${webUser.city}" postal="${webUser.postal}" />
 *	</pre>
 *
 *  @author Jorg Janke
 *  @version $Id: LocationTag.java,v 1.18 2005/10/01 23:56:02 jjanke Exp $
 */
public class LocationTag extends TagSupport
{
	/**	Logging						*/
	private CLogger			log = CLogger.getCLogger(getClass());

	private String			m_countryID_el;
	private String			m_regionID_el;
	private String			m_regionName_el;
	private String			m_city_el;
	private String			m_postal_el;

	private MCountry		m_country;

	//  CSS Classes
	private static final String C_MANDATORY = "Cmandatory";
	private static final String C_ERROR     = "Cerror";

	/**
	 * 	Set Country
	 *	@param info_el country info
	 */
	public void setCountryID (String info_el)
	{
		m_countryID_el = info_el;
	}	//	setCountry

	/**
	 * 	Set Region
	 *	@param info_el region info
	 */
	public void setRegionID (String info_el)
	{
		m_regionID_el = info_el;
	}	//	setRegion

	/**
	 * 	Set Region
	 *	@param info_el region info
	 */
	public void setRegionName (String info_el)
	{
		m_regionName_el = info_el;
	}	//	setRegion

	/**
	 * 	Set City
	 *	@param info_el city info
	 */
	public void setCity (String info_el)
	{
		m_city_el = info_el;
	}	//	setCity

	/**
	 * 	Set Postal
	 *	@param info_el postal info
	 */
	public void setPostal (String info_el)
	{
		m_postal_el = info_el;
	}	//	setPostal

	/**
	 *  Start Tag
	 *  @return SKIP_BODY
	 * 	@throws JspException
	 */
	public int doStartTag() throws JspException
	{
		Properties ctx = JSPEnv.getCtx((HttpServletRequest)pageContext.getRequest());

		//	Country		*******************************************************
		int C_Country_ID = 0;
		try
		{
			String info = (String)ExpressionUtil.evalNotNull ("location", "countryID",
				m_countryID_el, String.class, this, pageContext);
			if (info != null && info.length () != 0)
				C_Country_ID = Integer.parseInt (info);
		}
		catch (Exception e)
		{
			log.severe ("Country - " + e);
		}
		MLocation loc = new MLocation (ctx, 0, null);
		if (C_Country_ID == 0)
			C_Country_ID = loc.getC_Country_ID(); //	default
			//
		String name = "C_Country_ID";
		select sel = new select (name, getCountries (loc, C_Country_ID));
		sel.setID("ID_" + name);
		sel.setClass(C_MANDATORY);
		tr tr_country = createRow (name, Msg.translate(ctx, name), sel);

		//	Region		*******************************************************
		int C_Region_ID = 0;
		try
		{
			String info = (String)ExpressionUtil.evalNotNull ("location", "regionID",
				m_regionID_el, String.class, this, pageContext);
			if (info != null && info.length () != 0)
				C_Region_ID = Integer.parseInt (info);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "RegionID - " + e);
		}
		if (C_Region_ID == 0)
			C_Region_ID = loc.getC_Region_ID(); //	default
		//
		name = "C_Region_ID";
		tr tr_region = null;
		String regionName = (String)ExpressionUtil.evalNotNull ("location", "regionName",
			m_regionName_el, String.class, this, pageContext);
		input field = new input (input.TYPE_TEXT, "RegionName", regionName);
		field.setSize(40).setMaxlength(60).setID("ID_RegionName");
		if (m_country != null && m_country.isHasRegion())
		{
			sel = new select (name, getRegions (loc, C_Country_ID, C_Region_ID));
			sel.setID("ID_" + name);
			tr_region = createRow (name, m_country.getRegionName(), sel, field);	//	Region & Name
		}
		else
			tr_region = createRow (name, Msg.translate(ctx, name), field);	//	Name only

		//	City	***********************************************************
		name = "City";
		String city = (String)ExpressionUtil.evalNotNull ("location", "city",
			m_city_el, String.class, this, pageContext);
		field = new input (input.TYPE_TEXT, name, city);
		field.setSize(40).setMaxlength(60).setID("ID_" + name);
		field.setClass(C_MANDATORY);
		tr tr_city = createRow (name, Msg.translate(ctx, name), field);
		//
		name = "Postal";
		String postal = (String)ExpressionUtil.evalNotNull ("location", "postal",
			m_postal_el, String.class, this, pageContext);
		field = new input (input.TYPE_TEXT, name, postal);
		field.setSize(10).setMaxlength(10).setID("ID_" + name);
		field.setClass(C_MANDATORY);
		tr tr_postal = createRow (name, Msg.translate(ctx, name), field);

		log.fine("C_Country_ID=" + C_Country_ID + ", C_Region_ID=" + C_Region_ID
			+ ", RegionName=" + regionName + ", City=" + city + ", Postal=" + postal);

		//	Assemble
		HtmlCode html = new HtmlCode();
		if (m_country != null)
		{
		//	m_country.DisplaySequence;
			html.addElement(tr_city);
			html.addElement(tr_postal);
			html.addElement(tr_region);
			html.addElement(tr_country);
		}
		else
		{
			html.addElement(tr_city);
			html.addElement(tr_postal);
			html.addElement(tr_region);
			html.addElement(tr_country);
		}

		JspWriter out = pageContext.getOut();
		html.output(out);
		//
		return (SKIP_BODY);
	}   //  doStartTag

	/**
	 * 	End Tag - NOP
	 * 	@return EVAL_PAGE
	 * 	@throws JspException
	 */
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}	//	doEndTag

	/**
	 * 	Get Country Options.
	 * 	Add Regions for selected country
	 * 	Set Default
	 *	@param loc MLocation
	 * 	@param C_Country_ID default country
	 *	@return array of country options
	 */
	private option[] getCountries (MLocation loc, int C_Country_ID)
	{
		MCountry[] countries = MCountry.getCountries(loc.getCtx());
		option[] options = new option[countries.length];
		m_country = null;
		//
		for (int i = 0; i < countries.length; i++)
		{
			options[i] = new option (String.valueOf(countries[i].getC_Country_ID()));
			options[i].addElement(Util.maskHTML(countries[i].getName()));
			if (countries[i].getC_Country_ID() == C_Country_ID)
			{
				m_country = countries[i];
				options[i].setSelected (true);
			}
		}
		//
		return options;
	}	//	getCountries

	/**
	 *	Get Region Options for Country
	 * 	@param loc location
	 * 	@param C_Country_ID country
	 * 	@param C_Region_ID default region
	 * 	@return region array
	 */
	private option[] getRegions (MLocation loc, int C_Country_ID, int C_Region_ID)
	{
		MRegion[] regions = MRegion.getRegions(loc.getCtx(), C_Country_ID);
		option[] options = new option[regions.length+1];
		//
		options[0] = new option ("0");
		options[0].addElement(" ");
		//
		for (int i = 0; i < regions.length; i++)
		{
			options[i+1] = new option (String.valueOf(regions[i].getC_Region_ID()));
			options[i+1].addElement(regions[i].getName());
			if (regions[i].getC_Region_ID() == C_Region_ID)
				options[i+1].setSelected(true);
		}
		return options;
	}	//	getRegions


	/**
	 * 	Add Line to HTML
	 * 	@param name name
	 * 	@param labelText label text
	 * 	@param data data element
	 * 	@return table row
	 */
	private tr createRow (String name, String labelText, ConcreteElement data)
	{
		tr tr = new tr();

		//	Label
		td td = new td ();
		tr.addElement(td);
		td.setAlign("right");
		label label = new label();
		td.addElement(label);
		label.setID("ID_" + name);
		label.setFor(name);
		label.addElement(labelText);

		//	Data
		td = new td ();
		tr.addElement(td);
		td.setAlign("left");
		td.addElement(data);
		//
		return tr;
	}	//	addLines

	/**
	 * 	Add Line to HTML
	 * 	@param name name
	 * 	@param labelText label text
	 * 	@param data data element
	 * 	@param data2 second data element
	 * 	@return table row
	 */
	private tr createRow (String name, String labelText, ConcreteElement data, ConcreteElement data2)
	{
		tr tr = new tr();

		//	Label
		td td = new td ();
		tr.addElement(td);
		td.setAlign("right");
		label label = new label();
		td.addElement(label);
		label.setID("ID_" + name);
		label.setFor(name);
		label.addElement(labelText);

		//	Data
		td = new td ();
		tr.addElement(td);
		td.setAlign("left");
		td.addElement(data);
		td.addElement(" - ");
		td.addElement(data2);
		//
		return tr;
	}	//	addLines

}	//	LocationTag
