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
 *  Swedish Translation Texts for Look & Feel
 *
 *  @author     Thomas Dilts
 *  @version    $Id: PlafRes_sv.java,v 1.5 2005/03/11 20:34:37 jjanke Exp $
 */
public class PlafRes_sv extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Bakgrundsfärgtyp" },
	{ "BackColType_Flat",       "Platt" },
	{ "BackColType_Gradient",   "Toning" },
	{ "BackColType_Lines",      "Linjer" },
	{ "BackColType_Texture",    "Struktur" },
	//
	{ "LookAndFeelEditor",      "Utseenderedigeringsprogram" },
	{ "LookAndFeel",            "Utseendet" },
	{ "Theme",                  "Tema" },
	{ "EditCompiereTheme",      "Redigera compiere tema" },
	{ "SetDefault",             "Standardbakgrund" },
	{ "SetDefaultColor",        "Bakgrundsfärg" },
	{ "ColorBlind",             "Bristfälligfärg" },
	{ "Example",                "Exempel" },
	{ "Reset",                  "Återställa" },
	{ "OK",                     "OK" },
	{ "Cancel",                 "Avbryt" },
	//
	{ "CompiereThemeEditor",    "Compiere temaredigeringsprogram" },
	{ "MetalColors",            "Metal färg" },
	{ "CompiereColors",         "Compiere färger" },
	{ "CompiereFonts",          "Compiere teckensnitt" },
	{ "Primary1Info",           "Skugga, avskiljare" },
	{ "Primary1",               "Primär 1" },
	{ "Primary2Info",           "Fokus linje, vald meny" },
	{ "Primary2",               "Primär 2" },
	{ "Primary3Info",           "Tabel vald rad, vald text, knappbeskrivning bakgrund" },
	{ "Primary3",               "Primär 3" },
	{ "Secondary1Info",         "Ram linjer" },
	{ "Secondary1",             "Sekundär 1" },
	{ "Secondary2Info",         "Inactiv tabbar, nedtrycktfält, inactive ram + text" },
	{ "Secondary2",             "Sekundär 2" },
	{ "Secondary3Info",         "Bakgrund" },
	{ "Secondary3",             "Sekundär 3" },
	//
	{ "ControlFontInfo",        "Kontrolteckensnitt" },
	{ "ControlFont",            "Textetiketter teckensnitt" },
	{ "SystemFontInfo",         "Knappbeskrivning, Träd gren" },
	{ "SystemFont",             "System teckensnitt" },
	{ "UserFontInfo",           "Användare angiven data" },
	{ "UserFont",               "Fält teckensnitt" },
//	{ "SmallFontInfo",          "Reports" },
	{ "SmallFont",              "Liten teckensnitt" },
	{ "WindowTitleFont",         "Rubrik teckensnitt" },
	{ "MenuFont",               "Meny teckensnitt" },
	//
	{ "MandatoryInfo",          "Obligatoriskt fält bakgrund" },
	{ "Mandatory",              "Obligatorisk" },
	{ "ErrorInfo",              "Fel fält bakgrund" },
	{ "Error",                  "Fel" },
	{ "InfoInfo",               "Information fält bakgrund" },
	{ "Info",                   "Information" },
	{ "WhiteInfo",              "Linjer" },
	{ "White",                  "Vit" },
	{ "BlackInfo",              "Linjer, text" },
	{ "Black",                  "Svart" },
	{ "InactiveInfo",           "Inactiv fält bakgrund" },
	{ "Inactive",               "Inactiv" },
	{ "TextOKInfo",             "OK text förgrund" },
	{ "TextOK",                 "Text - OK" },
	{ "TextIssueInfo",          "Fel text förgrund" },
	{ "TextIssue",              "Text - Fel" },
	//
	{ "FontChooser",            "Teckensnittväljare" },
	{ "Fonts",                  "Teckensnitt" },
	{ "Plain",                  "Oformaterad" },
	{ "Italic",                 "Kursiv" },
	{ "Bold",                   "Fet" },
	{ "BoldItalic",             "Fet & kursiv" },
	{ "Name",                   "Namn" },
	{ "Size",                   "Storlek" },
	{ "Style",                  "Stil" },
	{ "TestString",             "Denna är en test! ABCDEFG abcdefg ÄäÅåÖö. 12,3456.78 LetterLOne = l1 LetterOZero = O0" },
	{ "FontString",             "Teckensnitt" },
	//
	{ "CompiereColorEditor",    "Compiere färgredigeringsprogram" },
	{ "CompiereType",           "Färgtyp" },
	{ "GradientUpperColor",     "Toning överfärg" },
	{ "GradientLowerColor",     "Toning underfärg" },
	{ "GradientStart",          "Toning start" },
	{ "GradientDistance",       "Toning längd" },
	{ "TextureURL",             "Struktur URL" },
	{ "TextureAlpha",           "Struktur Alpha" },
	{ "TextureTaintColor",      "Struktur fläckfärg" },
	{ "LineColor",              "Linje färg" },
	{ "LineBackColor",          "Bakgrundsfärg" },
	{ "LineWidth",              "Linje bred" },
	{ "LineDistance",           "Linje längd" },
	{ "FlatColor",              "Mattfärg" }
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
