/*******************************************************************************
 * The contents of this file are subject to the Compiere License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License. The
 * Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke. All parts are Copyright (C) 1999-2005
 * ComPiere, Inc. All Rights Reserved. Contributor(s):
 * ______________________________________.
 ******************************************************************************/
package org.compiere.apps;

import java.util.*;

/**
 * Resource Bundle for Finnish language
 * 
 * @author Petteri Soininen (petteri.soininen@netorek.fi)
 * @version $Id: ALoginRes_fi.java,v 1.4 2005/12/19 01:16:52 jjanke Exp $
 */
public final class ALoginRes_fi extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content */
	static final Object[][] contents = new String[][] {
		{ "Connection", "Yhteys"},
		{ "Defaults", "Oletusarvot"},
		{ "Login", "Compiere Login"},
		{ "File", "Tiedosto"},
		{ "Exit", "Poistu"},
		{ "Help", "Ohje"},
		{ "About", "About"},
		{ "Host", "Host"},
		{ "Database", "Tietokanta"},
		{ "User", "Käyttäjätunnus"},
		{ "EnterUser", "Anna sovelluksen käyttäjätunnus"},
		{ "Password", "Salasana"},
		{ "EnterPassword", "Anna sovelluksen salasana"},
		{ "Language", "Kieli"},
		{ "SelectLanguage", "Valitse kieli"},
		{ "Role", "Rooli"},
		{ "Client", "Client"},
		{ "Organization", "Organisaatio"},
		{ "Date", "Päivämäärä"},
		{ "Warehouse", "Tietovarasto"},
		{ "Printer", "Tulostin"},
		{ "Connected", "Yhdistetty"},
		{ "NotConnected", "Ei yhteyttä"},
		{ "DatabaseNotFound", "Tietokantaa ei löydy"},
		{ "UserPwdError", "Käyttäjätunnus ja salasana eivät vastaa toisiaan"},
		{ "RoleNotFound", "Roolia ei löydy tai se ei ole täydellinen"},
		{ "Authorized", "Valtuutettu"},
		{ "Ok", "Hyväksy"},
		{ "Cancel", "Peruuta"},
		{ "VersionConflict", "Versioristiriita:"},
		{ "VersionInfo", "Server <> Client"},
		{ "PleaseUpgrade", "Ole hyvä ja aja päivitysohjelma"}};

	/**
     * Get Contents
     * 
     * @return context
     */
	public Object[][] getContents ()
	{
		return contents;
	} // getContents
	
} // ALoginRes
