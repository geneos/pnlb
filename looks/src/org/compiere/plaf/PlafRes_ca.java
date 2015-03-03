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
 *  @author Jaume Teixi
 *  @version    $Id: PlafRes_ca.java,v 1.6 2005/03/11 20:34:36 jjanke Exp $
 */
public class PlafRes_ca extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Tipus Color Fons" },
	{ "BackColType_Flat",       "Pla" },
	{ "BackColType_Gradient",   "Gradient" },
	{ "BackColType_Lines",      "Línes" },
	{ "BackColType_Texture",    "Textura" },
	//
	{ "LookAndFeelEditor",      "Editor Aparença i Comportament" },
	{ "LookAndFeel",            "Aparença i Comportament" },
	{ "Theme",                  "Tema" },
	{ "EditCompiereTheme",      "Editar Tema Compiere" },
	{ "SetDefault",             "Fons Per Defecte" },
	{ "SetDefaultColor",        "Color Fons" },
	{ "ColorBlind",             "Deficiència Color" },
	{ "Example",                "Exemple" },
	{ "Reset",                  "Reiniciar" },
	{ "OK",                     "D'Acord" },
	{ "Cancel",                 "Cancel.lar" },
	//
	{ "CompiereThemeEditor",    "Editor Tema Compiere" },
	{ "MetalColors",            "Colors Metal" },
	{ "CompiereColors",         "Colors Compiere" },
	{ "CompiereFonts",          "Fonts Compiere" },
	{ "Primary1Info",           "Ombra, Separador" },
	{ "Primary1",               "Primari 1" },
	{ "Primary2Info",           "Línia Focus, Menú Seleccionat" },
	{ "Primary2",               "Primari 2" },
	{ "Primary3Info",           "Taula Fila Seleccionada, Texte Seleccionat, Indicador Fons" },
	{ "Primary3",               "Primari 3" },
	{ "Secondary1Info",         "Línies Marc" },
	{ "Secondary1",             "Secondari 1" },
	{ "Secondary2Info",         "Pestanyes Innactives, Camps Premuts, Texte + Marc Innactius" },
	{ "Secondary2",             "Secondari 2" },
	{ "Secondary3Info",         "Fons" },
	{ "Secondary3",             "Secondari 3" },
	//
	{ "ControlFontInfo",        "Font Control" },
	{ "ControlFont",            "Font Etiqueta" },
	{ "SystemFontInfo",         "Indicador, Nodes Arbre" },
	{ "SystemFont",             "Font Sistema" },
	{ "UserFontInfo",           "Dades Entrades Per l'Usuari" },
	{ "UserFont",               "Font Camp" },
//	{ "SmallFontInfo",          "Informes" },
	{ "SmallFont",              "Font Petita" },
	{ "WindowTitleFont",         "Font Títol" },
	{ "MenuFont",               "Font Menú" },
	//
	{ "MandatoryInfo",          "Camp de Fons Obligatori" },
	{ "Mandatory",              "Obligatori" },
	{ "ErrorInfo",              "Camp de Fons Error" },
	{ "Error",                  "Error" },
	{ "InfoInfo",               "Camp de Fons Informació" },
	{ "Info",                   "Informació" },
	{ "WhiteInfo",              "Línies" },
	{ "White",                  "Blanc" },
	{ "BlackInfo",              "Línies, Text" },
	{ "Black",                  "Negre" },
	{ "InactiveInfo",           "Camp de Fons Innactiu" },
	{ "Inactive",               "Innactiu" },
	{ "TextOKInfo",             "Texte Superior OK" },
	{ "TextOK",                 "Texte - OK" },
	{ "TextIssueInfo",          "Texte Superior Error" },
	{ "TextIssue",              "Texte - Error" },
	//
	{ "FontChooser",            "Escollidor Font" },
	{ "Fonts",                  "Fonts" },
	{ "Plain",                  "Plana" },
	{ "Italic",                 "Itàlica" },
	{ "Bold",                   "Negreta" },
	{ "BoldItalic",             "Negreta & Itàlica" },
	{ "Name",                   "Nom" },
	{ "Size",                   "Tamany" },
	{ "Style",                  "Estil" },
	{ "TestString",             "Això és només una Prova! La Guineu marró ràpida éstà fent quelcom. 12,3456.78 LetterLOne = l1 LetterOZero = O0" },
	{ "FontString",             "Font" },
	//
	{ "CompiereColorEditor",    "Editor Color Compiere" },
	{ "CompiereType",           "Tipus Color" },
	{ "GradientUpperColor",     "Color Dalt Degradat" },
	{ "GradientLowerColor",     "Color Baix Degradat" },
	{ "GradientStart",          "Inici Degradat" },
	{ "GradientDistance",       "Distància Degradat" },
	{ "TextureURL",             "Textura URL" },
	{ "TextureAlpha",           "Textura Alfa" },
	{ "TextureTaintColor",      "Textura Color Corrupció" },
	{ "LineColor",              "Color Línia" },
	{ "LineBackColor",          "Color Fons" },
	{ "LineWidth",              "Ampla Línia" },
	{ "LineDistance",           "Distància Línia" },
	{ "FlatColor",              "Color Pla" }
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
