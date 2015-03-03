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
 *  Translation Texts for Look & Feel
 *
 *  @author     Jorg Janke
 *  @version    $Id: PlafRes_da.java,v 1.2 2005/07/31 14:31:50 jpedersen Exp $
 */
public class PlafRes_da extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Baggrund: Farvetype" },
	{ "BackColType_Flat",       "Fast" },
	{ "BackColType_Gradient",   "Farveforløb" },
	{ "BackColType_Lines",      "Linjer" },
	{ "BackColType_Texture",    "Struktur" },
	//
	{ "LookAndFeelEditor",      "Redigér & udseende" },
	{ "LookAndFeel",            "Udseende" },
	{ "Theme",                  "Tema" },
	{ "EditCompiereTheme",      "Redigér Compiere tema" },
	{ "SetDefault",             "Baggrund: Standard" },
	{ "SetDefaultColor",        "Baggrundsfarve" },
	{ "ColorBlind",             "Farvereduktion" },
	{ "Example",                "Eksempel" },
	{ "Reset",                  "Gendan" },
	{ "OK",                     "OK" },
	{ "Cancel",                 "Annullér" },
	//
	{ "CompiereThemeEditor",    "Compiere-tema: Redigér" },
	{ "MetalColors",            "Metal-farver" },
	{ "CompiereColors",         "Compiere-farver" },
	{ "CompiereFonts",          "Compiere-skrifttyper" },
	{ "Primary1Info",           "Skygge, Separator" },
	{ "Primary1",               "Primær 1" },
	{ "Primary2Info",           "Markeret element, Markeret menu" },
	{ "Primary2",               "Primær 2" },
	{ "Primary3Info",           "Markeret række i tabel, Markeret tekst, Værktøjstip - baggr." },
	{ "Primary3",               "Prim�r 3" },
	{ "Secondary1Info",         "Rammelinjer" },
	{ "Secondary1",             "Sekundær 1" },
	{ "Secondary2Info",         "Ikke-aktive faner, Markerede felter, Ikke-aktiv ramme + tekst" },
	{ "Secondary2",             "Sekundær 2" },
	{ "Secondary3Info",         "Baggrund" },
	{ "Secondary3",             "Sekundær 3" },
	//
	{ "ControlFontInfo",        "Skrifttype: Knapper" },
	{ "ControlFont",            "Skrifttype: Etiket" },
	{ "SystemFontInfo",         "Værktøjstip, Strukturknuder" },
	{ "SystemFont",             "Skrifttype: System" },
	{ "UserFontInfo",           "Anvend" },
	{ "UserFont",               "Skrifttype: Felt" },
//	{ "SmallFontInfo",          "Rapporter" },
	{ "SmallFont",              "Lille" },
	{ "WindowTitleFont",         "Skrifttype: Titellinje" },
	{ "MenuFont",               "Skrifttype: Menu" },
	//
	{ "MandatoryInfo",          "Tvungen feltbaggrund" },
	{ "Mandatory",              "Tvungen" },
	{ "ErrorInfo",              "Fejl: Feltbaggrund" },
	{ "Error",                  "Fejl" },
	{ "InfoInfo",               "Info: Feltbaggrund" },
	{ "Info",                   "Info" },
	{ "WhiteInfo",              "Linjer" },
	{ "White",                  "Hvid" },
	{ "BlackInfo",              "Linjer, Tekst" },
	{ "Black",                  "Sort" },
	{ "InactiveInfo",           "Inaktiv feltbaggrund" },
	{ "Inactive",               "Inaktiv" },
	{ "TextOKInfo",             "OK: Tekstforgrund" },
	{ "TextOK",                 "Tekst: OK" },
	{ "TextIssueInfo",          "Fejl: Tekstforgrund" },
	{ "TextIssue",              "Tekst: Fejl" },
	//
	{ "FontChooser",            "Skriftype" },
	{ "Fonts",                  "Skrifttyper" },
	{ "Plain",                  "Normal" },
	{ "Italic",                 "Kursiv" },
	{ "Bold",                   "Fed" },
	{ "BoldItalic",             "Fed & kursiv" },
	{ "Name",                   "Navn" },
	{ "Size",                   "Størrelse" },
	{ "Style",                  "Type" },
	{ "TestString",             "Dette er en prøve! 12.3456,78 BogstavLEn = l1 BogstavONul = O0" },
	{ "FontString",             "Skrifttype" },
	//
	{ "CompiereColorEditor",    "Compiere-farveeditor" },
	{ "CompiereType",           "Farvetype" },
	{ "GradientUpperColor",     "Farveforløb: Farve 1" },
	{ "GradientLowerColor",     "Farveforløb: Farve 2" },
	{ "GradientStart",          "Farveforløb: Start" },
	{ "GradientDistance",       "Farveforløb: Afstand" },
	{ "TextureURL",             "Struktur: URL" },
	{ "TextureAlpha",           "Struktur: Alpha" },
	{ "TextureTaintColor",      "Struktur: Pletvis" },
	{ "LineColor",              "Linjefarve" },
	{ "LineBackColor",          "Baggrundsfarve" },
	{ "LineWidth",              "Linjebredde" },
	{ "LineDistance",           "Linjeafstand" },
	{ "FlatColor",              "Fast farve" }
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
