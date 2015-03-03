/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): PT. RFID INDONESIA (info@rfid-indonesia.com)______________.
 *****************************************************************************/
package org.compiere.plaf;

import java.util.ListResourceBundle;

/**
 *  Translation Texts for Look & Feel
 *
 *  @author     Halim Englen
 *  @version    $Id: PlafRes_in.java,v 1.3 2005/11/14 02:29:40 jjanke Exp $
 */
public class PlafRes_in extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Tipe Warna Latar Belakang" },
	{ "BackColType_Flat",       "Rata" },
	{ "BackColType_Gradient",   "Gradien" },
	{ "BackColType_Lines",      "Garis" },
	{ "BackColType_Texture",    "Textur" },
	//
	{ "LookAndFeelEditor",      "Editor Look & Feel" },
	{ "LookAndFeel",            "Look & Feel" },
	{ "Theme",                  "Tema" },
	{ "EditCompiereTheme",      "Ubah Tema Compiere" },
	{ "SetDefault",             "Latar Belakang Asal" },
	{ "SetDefaultColor",        "Warna Latar Belakang" },
	{ "ColorBlind",             "Defisiensi Warna" },
	{ "Example",                "Contoh" },
	{ "Reset",                  "Reset" },
	{ "OK",                     "OK" },
	{ "Cancel",                 "Batal" },
	//
	{ "CompiereThemeEditor",    "Compiere Tema Editor" },
	{ "MetalColors",            "Warna Metal" },
	{ "CompiereColors",         "Warna Compiere" },
	{ "CompiereFonts",          "Compiere Fonts" },
	{ "Primary1Info",           "Bayangan, Pemisah" },
	{ "Primary1",               "Utama 1" },
	{ "Primary2Info",           "Baris Fokus, Menu yang Dipilih" },
	{ "Primary2",               "Utama 2" },
	{ "Primary3Info",           "Table Selected Row, Selected Text, ToolTip Background" },
	{ "Primary3",               "Utama 3" },
	{ "Secondary1Info",         "Border Lines" },
	{ "Secondary1",             "Sekunder 1" },
	{ "Secondary2Info",         "Inactive Tabs, Pressed Fields, Inactive Border + Text" },
	{ "Secondary2",             "Sekunder 2" },
	{ "Secondary3Info",         "Background" },
	{ "Secondary3",             "Sekunder 3" },
	//
	{ "ControlFontInfo",        "Control Font" },
	{ "ControlFont",            "Label Font" },
	{ "SystemFontInfo",         "Tool Tip, Tree nodes" },
	{ "SystemFont",             "System Font" },
	{ "UserFontInfo",           "User Entered Data" },
	{ "UserFont",               "Field Font" },
//	{ "SmallFontInfo",          "Reports" },
	{ "SmallFont",              "Small Font" },
	{ "WindowTitleFont",        "Title Font" },
	{ "MenuFont",               "Menu Font" },
	//
	{ "MandatoryInfo",          "Mandatory Field Background" },
	{ "Mandatory",              "Mandatory" },
	{ "ErrorInfo",              "Error Field Background" },
	{ "Error",                  "Error" },
	{ "InfoInfo",               "Info Field Background" },
	{ "Info",                   "Info" },
	{ "WhiteInfo",              "Lines" },
	{ "White",                  "Putih" },
	{ "BlackInfo",              "Lines, Text" },
	{ "Black",                  "Hitam" },
	{ "InactiveInfo",           "Inactive Field Background" },
	{ "Inactive",               "Inactive" },
	{ "TextOKInfo",             "OK Text Foreground" },
	{ "TextOK",                 "Text - OK" },
	{ "TextIssueInfo",          "Error Text Foreground" },
	{ "TextIssue",              "Text - Error" },
	//
	{ "FontChooser",            "Font Chooser" },
	{ "Fonts",                  "Fonts" },
	{ "Plain",                  "Plain" },
	{ "Italic",                 "Italic" },
	{ "Bold",                   "Bold" },
	{ "BoldItalic",             "Bold & Italic" },
	{ "Name",                   "Nama" },
	{ "Size",                   "Ukuran" },
	{ "Style",                  "Gaya" },
	{ "TestString",             "This is just a Test! The quick brown Fox is doing something. 12,3456.78 LetterLOne = l1 LetterOZero = O0" },
	{ "FontString",             "Font" },
	//
	{ "CompiereColorEditor",    "Editor Warna Compiere" },
	{ "CompiereType",           "Tipe Warna" },
	{ "GradientUpperColor",     "Warna Atas Gradien" },
	{ "GradientLowerColor",     "Warna Bawah Gradien" },
	{ "GradientStart",          "Mulai Gradien" },
	{ "GradientDistance",       "Jarak Gradien" },
	{ "TextureURL",             "Textur URL" },
	{ "TextureAlpha",           "Textur Alpha" },
	{ "TextureTaintColor",      "Textur Warna Taint" },
	{ "LineColor",              "Warna Baris" },
	{ "LineBackColor",          "Warna Latar Belakang" },
	{ "LineWidth",              "Lebar Baris" },
	{ "LineDistance",           "Jarak Baris" },
	{ "FlatColor",              "Warna Flat" }
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
