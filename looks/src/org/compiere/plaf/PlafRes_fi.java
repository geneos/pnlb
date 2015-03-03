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
package org.compiere.plaf;

import java.util.ListResourceBundle;

/**
 *  Translation Texts for Look & Feel for Finnish Language
 *
 * 	@author 	Petteri Soininen (petteri.soininen@netorek.fi)
 * 	@version 	$Id: PlafRes_fi.java,v 1.5 2005/03/11 20:34:37 jjanke Exp $
 */
public class PlafRes_fi extends ListResourceBundle
{
	/** 
    * Data 
    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Taustan värityyppi" },
	{ "BackColType_Flat",       "Yksiväri" },
	{ "BackColType_Gradient",   "Gradientti" },
	{ "BackColType_Lines",      "Viiva" },
	{ "BackColType_Texture",    "Kuviointi" },
	//
	{ "LookAndFeelEditor",      "Käyttötuntuman muokkaus" },
	{ "LookAndFeel",            "Käyttötuntuma" },
	{ "Theme",                  "Teema" },
	{ "EditCompiereTheme",      "Muokkaa Compiere-teemaa" },
	{ "SetDefault",             "Oletustaustaväri" },
	{ "SetDefaultColor",        "Taustaväri" },
	{ "ColorBlind",             "Värisokeus" },
	{ "Example",                "Esimerkki" },
	{ "Reset",                  "Nollaa" },
	{ "OK",                     "Hyväksy" },
	{ "Cancel",                 "Peruuta" },
	//
	{ "CompiereThemeEditor",    "Compiere-teeman muokkaus" },
	{ "MetalColors",            "Metallivärit" },
	{ "CompiereColors",         "Compiere-värit" },
	{ "CompiereFonts",          "Compiere-kirjasimet" },
	{ "Primary1Info",           "Varjostin, Erotin" },
	{ "Primary1",               "Ensisijainen 1" },
	{ "Primary2Info",           "Focus-viiva, Valitty Valikko" },
	{ "Primary2",               "Ensisijainen 2" },
	{ "Primary3Info",           "Taulun Valittu Rivi, Valittu Teksti, ToolTip Tausta" },
	{ "Primary3",               "Ensisijainen 3" },
	{ "Secondary1Info",         "Reunaviivat" },
	{ "Secondary1",             "Toissijainen 1" },
	{ "Secondary2Info",         "Ei-aktiiviset Tabulaattorit, Painetut Kentät, Ei-aktiivinen Reuna + Teksti" },
	{ "Secondary2",             "Toissijainen 2" },
	{ "Secondary3Info",         "Tausta" },
	{ "Secondary3",             "Toissijainen 3" },
	//
	{ "ControlFontInfo",        "Kontrollikirjasin" },
	{ "ControlFont",            "Nimikekirjasin" },
	{ "SystemFontInfo",         "Tool Tip, Puun Solmut" },
	{ "SystemFont",             "Järjestelmäkirjasin" },
	{ "UserFontInfo",           "Käyttäjän Syöttämä Tieto" },
	{ "UserFont",               "Kenttäkirjasin" },
//	{ "SmallFontInfo",          "Raportit" },
	{ "SmallFont",              "Pieni Kirjasin" },
	{ "WindowTitleFont",         "Otsikkokirjasin" },
	{ "MenuFont",               "Valikkokirjasin" },
	//
	{ "MandatoryInfo",          "Pakollinen Kenttätausta" },
	{ "Mandatory",              "Pakollinen" },
	{ "ErrorInfo",              "Virhekentän Tausta" },
	{ "Error",                  "Virhe" },
	{ "InfoInfo",               "Tietokentän Tausta" },
	{ "Info",                   "Tieto" },
	{ "WhiteInfo",              "Viivat" },
	{ "White",                  "Valkoinen" },
	{ "BlackInfo",              "Viivat, Teksti" },
	{ "Black",                  "Musta" },
	{ "InactiveInfo",           "Ei-aktiivinen Kenttätausta" },
	{ "Inactive",               "Ei-aktiivinen" },
	{ "TextOKInfo",             "Hyväksy Teksti Edusta" },
	{ "TextOK",                 "Teksti - Hyväksy" },
	{ "TextIssueInfo",          "Virhetekstin Edusta" },
	{ "TextIssue",              "Teksti - Virhe" },
	//
	{ "FontChooser",            "Kirjasimen Valitsin" },
	{ "Fonts",                  "Kirjasimet" },
	{ "Plain",                  "Tavallinen" },
	{ "Italic",                 "Kursiivi" },
	{ "Bold",                   "Lihavoitu" },
	{ "BoldItalic",             "Lihavoitu ja Kursiivi" },
	{ "Name",                   "Nimi" },
	{ "Size",                   "Koko" },
	{ "Style",                  "Tyyli" },
	{ "TestString",             "Tämä on vain Testi! Nopea ruskea Kettu suorittaa jotain. 12,3456.78 LetterLOne = l1 LetterOZero = O0" },
	{ "FontString",             "Kirjasin" },
	//
	{ "CompiereColorEditor",    "Compiere-värimuokkaus" },
	{ "CompiereType",           "Värityyppi" },
	{ "GradientUpperColor",     "Gradientin Ylempi Väri" },
	{ "GradientLowerColor",     "Gradientin Alempi Väri" },
	{ "GradientStart",          "Gradientin Alku" },
	{ "GradientDistance",       "Gradientin Etäisyys" },
	{ "TextureURL",             "Kuvioinnin URL" },
	{ "TextureAlpha",           "Kuvioinnin Alpha" },
	{ "TextureTaintColor",      "Kuvioinnin Korvausväri" },
	{ "LineColor",              "Viivan Väri" },
	{ "LineBackColor",          "Taustan Väri" },
	{ "LineWidth",              "Viivan Paksuus" },
	{ "LineDistance",           "Viivan Etäisyys" },
	{ "FlatColor",              "Tavallinen Väri" }
	};

	/**
	 * Get Contents
	 * @return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}
}   //  Res

