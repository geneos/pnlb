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
 *  Translation Texts for Look & Feel (German)
 *
 *  @author     Jorg Janke
 *  @version    $Id: PlafRes_de.java,v 1.7 2005/03/11 20:34:36 jjanke Exp $
 */
public class PlafRes_de extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Hintergrund Farbtyp" },
	{ "BackColType_Flat",       "Flach" },
	{ "BackColType_Gradient",   "Gradient" },
	{ "BackColType_Lines",      "Linie" },
	{ "BackColType_Texture",    "Textur" },
	//
	{ "LookAndFeelEditor",      "Benutzeroberfläche Editor" },
	{ "LookAndFeel",            "Oberfläche" },
	{ "Theme",                  "Thema" },
	{ "EditCompiereTheme",      "Compiere Thema bearbeiten" },
	{ "SetDefault",             "Standard Hintergrund" },
	{ "SetDefaultColor",        "Hintergrund Farbe" },
	{ "ColorBlind",             "Farbblindheit" },
	{ "Example",                "Beispiel" },
	{ "Reset",                  "Zurücksetzen" },
	{ "OK",                     "Ja" },
	{ "Cancel",                 "Abbruch" },
	//
	{ "CompiereThemeEditor",    "Compiere Thema Editor" },
	{ "MetalColors",            "Metal Farben" },
	{ "CompiereColors",         "Compiere Farben" },
	{ "CompiereFonts",          "Compiere Schriften" },
	{ "Primary1Info",           "Schatten, Separator" },
	{ "Primary1",               "Primär 1" },
	{ "Primary2Info",           "Fokuslinien, Aktives Menü" },
	{ "Primary2",               "Primär 2" },
	{ "Primary3Info",           "Tabelle Selected Row, Selected Text, Tool Tip Hintergrund" },
	{ "Primary3",               "Primär 3" },
	{ "Secondary1Info",         "Rahmen Lines" },
	{ "Secondary1",             "Sekundär 1" },
	{ "Secondary2Info",         "Inaktive Tabs, Pressed Felder, Inaktive Rahmen + Text" },
	{ "Secondary2",             "Sekundär 2" },
	{ "Secondary3Info",         "Hintergrund" },
	{ "Secundary3",             "Sekundär 3" },
	//
	{ "ControlFontInfo",        "Labels" },
	{ "ControlFont",            "Standard Schrift" },
	{ "SystemFontInfo",         "Tool Tip" },
	{ "SystemFont",             "System Schrift" },
	{ "UserFontInfo",           "Entered Data" },
	{ "UserFont",               "Nutzer Schrift" },
	{ "SmallFont",              "Kleine Schrift" },
	{ "WindowTitleFont",         "Titel Schrift" },
	{ "MenuFont",               "Menü Schrift" },
	//
	{ "MandatoryInfo",          "Erforderliches Feld Hintergrund" },
	{ "Mandatory",              "Erforderlich" },
	{ "ErrorInfo",              "Fehler Feld Hintergrund" },
	{ "Error",                  "Fehler" },
	{ "InfoInfo",               "Info Feld Hintergrund" },
	{ "Info",                   "Info" },
	{ "WhiteInfo",              "Linien" },
	{ "White",                  "Weiß" },
	{ "BlackInfo",              "Linien, Text" },
	{ "Black",                  "Schwarz" },
	{ "InactiveInfo",           "Inaktiv Feld Hintergrund" },
	{ "Inactive",               "Inaktiv" },
	{ "TextOKInfo",             "OK Text Fordergrund" },
	{ "TextOK",                 "Text - OK" },
	{ "TextIssueInfo",          "Fehler Text Fordergrund" },
	{ "TextIssue",              "Text - Fehler" },
	//
	{ "FontChooser",            "Schrift Auswahl" },
	{ "Fonts",                  "Schriften" },
	{ "Plain",                  "Normal" },
	{ "Italic",                 "Italic" },
	{ "Bold",                   "Fett" },
	{ "BoldItalic",             "Fett & Italic" },
	{ "Name",                   "Name" },
	{ "Size",                   "Größe" },
	{ "Style",                  "Stil" },
	{ "TestString",             "Dies ist nur ein Test! The quick brown Fox is doing something. 12,3456.78 BuchstabeLEins = l1 BuchstabeONull = O0" },
	{ "FontString",             "Schrift" },
	//
	{ "CompiereColorEditor",    "Compiere Farben Auswahl" },
	{ "CompiereType",           "Farbtyp" },
	{ "GradientUpperColor",     "Gradient obere Farbe" },
	{ "GradientLowerColor",     "Gradient untere Farbe" },
	{ "GradientStart",          "Gradient Start" },
	{ "GradientDistance",       "Gradient Distanz" },
	{ "TextureURL",             "Textur URL" },
	{ "TextureAlpha",           "Textur Alpha" },
	{ "TextureTaintColor",      "Textur Tönung Farbe" },
	{ "LineColor",              "Linie Farbe" },
	{ "LineBackColor",          "Hintergrund Farbe" },
	{ "LineWidth",              "Linie Breite" },
	{ "LineDistance",           "Linie Distanz" },
	{ "FlatColor",              "Flache Farbe" }
	};

	/**
	 * Get Contents
	 * @return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}
}   //  Res_de
