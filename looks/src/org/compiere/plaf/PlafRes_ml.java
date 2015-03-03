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
 *  @author     Robin Hoo
 *  @version    $Id: PlafRes_ml.java,v 1.6 2005/03/11 20:34:36 jjanke Exp $
 */
public class PlafRes_ml extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Jenis Warna Latar Belakang" },
	{ "BackColType_Flat",       "Flat" },
	{ "BackColType_Gradient",   "Gradient" },
	{ "BackColType_Lines",      "Lines" },
	{ "BackColType_Texture",    "Texture" },
	//
	{ "LookAndFeelEditor",      "Look & Feel Editor" },
	{ "LookAndFeel",            "Look & Feel" },
	{ "Theme",                  "Theme" },
	{ "EditCompiereTheme",      "Edit Compiere Theme" },
	{ "SetDefault",             "Default Background" },
	{ "SetDefaultColor",        "Background Color" },
	{ "ColorBlind",             "Color Deficiency" },
	{ "Example",                "Example" },
	{ "Reset",                  "Reset" },
	{ "OK",                     "OK" },
	{ "Cancel",                 "Batal" },
	//
	{ "CompiereThemeEditor",    "Compiere Theme Editor" },
	{ "MetalColors",            "Metal Colors" },
	{ "CompiereColors",         "Compiere Colors" },
	{ "CompiereFonts",          "Compiere Fonts" },
	{ "Primary1Info",           "Shadow, Separator" },
	{ "Primary1",               "Primary 1" },
	{ "Primary2Info",           "Focus Line, Selected Menu" },
	{ "Primary2",               "Primary 2" },
	{ "Primary3Info",           "Table Selected Row, Selected Text, ToolTip Background" },
	{ "Primary3",               "Primary 3" },
	{ "Secondary1Info",         "Border Lines" },
	{ "Secondary1",             "Secondary 1" },
	{ "Secondary2Info",         "Inactive Tabs, Pressed Fields, Inactive Border + Text" },
	{ "Secondary2",             "Secondary 2" },
	{ "Secondary3Info",         "Background" },
	{ "Secondary3",             "Secondary 3" },
	//
	{ "ControlFontInfo",        "Control Font" },
	{ "ControlFont",            "Label Font" },
	{ "SystemFontInfo",         "Tool Tip, Tree nodes" },
	{ "SystemFont",             "System Font" },
	{ "UserFontInfo",           "User Entered Data" },
	{ "UserFont",               "Field Font" },
//	{ "SmallFontInfo",          "Reports" },
	{ "SmallFont",              "Small Font" },
	{ "WindowTitleFont",         "Title Font" },
	{ "MenuFont",               "Menu Font" },
	//
	{ "MandatoryInfo",          "Mandatory Field Background" },
	{ "Mandatory",              "Mandatory" },
	{ "ErrorInfo",              "Error Field Background" },
	{ "Error",                  "Error" },
	{ "InfoInfo",               "Info Field Background" },
	{ "Info",                   "Info" },
	{ "WhiteInfo",              "Lines" },
	{ "White",                  "White" },
	{ "BlackInfo",              "Lines, Text" },
	{ "Black",                  "Black" },
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
	{ "Name",                   "Name" },
	{ "Size",                   "Size" },
	{ "Style",                  "Style" },
	{ "TestString",             "This is just a Test! The quick brown Fox is doing something. 12,3456.78 LetterLOne = l1 LetterOZero = O0" },
	{ "FontString",             "Font" },
	//
	{ "CompiereColorEditor",    "Compiere Color Editor" },
	{ "CompiereType",           "Color Type" },
	{ "GradientUpperColor",     "Gradient Upper Color" },
	{ "GradientLowerColor",     "Gradient Lower Color" },
	{ "GradientStart",          "Gradient Start" },
	{ "GradientDistance",       "Gradient Distance" },
	{ "TextureURL",             "Texture URL" },
	{ "TextureAlpha",           "Texture Alpha" },
	{ "TextureTaintColor",      "Texture Taint Color" },
	{ "LineColor",              "Line Color" },
	{ "LineBackColor",          "Background Color" },
	{ "LineWidth",              "Line Width" },
	{ "LineDistance",           "Line Distance" },
	{ "FlatColor",              "Flat Color" }
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
