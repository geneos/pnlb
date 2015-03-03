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

import java.text.*;
import java.util.*;

/**
 *	System Display Types.
 *  <pre>
 *	SELECT AD_Reference_ID, Name FROM AD_Reference WHERE ValidationType = 'D'
 *  </pre>
 *  @author     Jorg Janke
 *  @version    $Id: DisplayType.java,v 1.21 2005/05/14 05:32:00 jjanke Exp $
 */
public final class DisplayType
{
	/** Display Type 10	String	*/
	public static final int String     = 10;
	/** Display Type 11	Integer	*/
	public static final int Integer    = 11;
	/** Display Type 12	Amount	*/
	public static final int Amount     = 12;
	/** Display Type 13	ID	*/
	public static final int ID         = 13;
	/** Display Type 14	Text	*/
	public static final int Text       = 14;
	/** Display Type 15	Date	*/
	public static final int Date       = 15;
	/** Display Type 16	DateTime	*/
	public static final int DateTime   = 16;
	/** Display Type 17	List	*/
	public static final int List       = 17;
	/** Display Type 18	Table	*/
	public static final int Table      = 18;
	/** Display Type 19	TableDir	*/
	public static final int TableDir   = 19;
	/** Display Type 20	YN	*/
	public static final int YesNo      = 20;
	/** Display Type 21	Location	*/
	public static final int Location   = 21;
	/** Display Type 22	Number	*/
	public static final int Number     = 22;
	/** Display Type 23	BLOB	*/
	public static final int Binary     = 23;
	/** Display Type 24	Time	*/
	public static final int Time       = 24;
	/** Display Type 25	Account	*/
	public static final int Account    = 25;
	/** Display Type 26	RowID	*/
	public static final int RowID      = 26;
	/** Display Type 27	Color   */
	public static final int Color      = 27;
	/** Display Type 28	Button	*/
	public static final int Button	   = 28;
	/** Display Type 29	Quantity	*/
	public static final int Quantity   = 29;
	/** Display Type 30	Search	*/
	public static final int Search     = 30;
	/** Display Type 31	Locator	*/
	public static final int Locator    = 31;
	/** Display Type 32 BLOB	*/
	public static final int Image      = 32;
	/** Display Type 33 Assignment	*/
	public static final int Assignment = 33;
	/** Display Type 34	Memo	*/
	public static final int Memo       = 34;
	/** Display Type 35	PAttribute	*/
	public static final int PAttribute = 35;
	/** Display Type 36	CLOB	*/
	public static final int TextLong   = 36;
	/** Display Type 37	CostPrice	*/
	public static final int CostPrice  = 37;
	
	/** 
	 * 08-11-2010 Camarzana Mariano
	 * Tipo de datos agregados para manejar una precision de 3 digitos
	 * Display Type 1000098 Tipo de cambio*/
	public static final int TipoDeCambio = 1000098;
    
	/** Display Type 1000085	CostPrice	*/
	public static final int EstadoChequesRecibidos  = 1000085;
	/** Display Type 1000085	CostPrice	*/
	public static final int EstadoChequesPropios  = 1000086;
	//	Candidates: FileName, PrinterName 
	
	/**
	 *	- New Display Type
		INSERT INTO AD_REFERENCE
		(AD_REFERENCE_ID, AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,CREATED,CREATEDBY,UPDATED,UPDATEDBY,
		NAME,DESCRIPTION,HELP, VALIDATIONTYPE,VFORMAT,ENTITYTYPE)
		VALUES (35, 0,0,'Y',SysDate,0,SysDate,0,
		'PAttribute','Product Attribute',null,'D',null,'D');
	 *
	 *  - org.compiere.model.MModel (??)
	 *	- org.compiere.grid.ed.VEditor/Dialog
	 *	- org.compiere.grid.ed.VEditorFactory
	 *	- RColumn, WWindow
	 *  add/check 0_cleanupAD.sql
	 */

	//  See DBA_DisplayType.sql ----------------------------------------------

	/** Maximum number of digits    */
	private static final int    MAX_DIGITS = 28;        //  Oracle Standard Limitation 38 digits
	/** Digits of an Integer        */
	private static final int    INTEGER_DIGITS = 10;
	/** Maximum number of fractions */
	private static final int    MAX_FRACTION = 12;

	/** Default Amount Precision    */
	// 08-11-2010 Cambiada la precision por el tipo tipo de cambio en Pagos
	private static final int    CHANGE_FRACTION = 3;

	/** Default Amount Precision    */
	private static final int    AMOUNT_FRACTION = 2;
	
	/**
	 *	Returns true if (numeric) ID (Table, Search, Account, ..).
	 *  (stored as Integer)
	 *  @param displayType Display Type
	 *  @return true if ID
	 */
	public static boolean isID (int displayType)
	{
		if (displayType == ID || displayType == Table || displayType == TableDir
			|| displayType == Search || displayType == Location || displayType == Locator
			|| displayType == Account || displayType == Assignment || displayType == PAttribute)
			return true;
		return false;
	}	//	isID

	/**
	 *	Returns true, if DisplayType is numeric (Amount, Number, Quantity, Integer).
	 *  (stored as BigDecimal)
	 *  @param displayType Display Type
	 *  @return true if numeric
	 */
	public static boolean isNumeric(int displayType)
	{
		if (displayType == Amount || displayType == Number || displayType == CostPrice 
			|| displayType == Integer || displayType == Quantity || displayType == TipoDeCambio)
			return true;
		return false;
	}	//	isNumeric
	
	/**
	 * 	Get Default Precision.
	 * 	Used for databases who cannot handle dynamic number precision.
	 *	@param displayType display type
	 *	@return scale (decimal precision)
	 */
	public static int getDefaultPrecision(int displayType)
	{
		if (displayType == TipoDeCambio)
			return 3;
		if (displayType == Amount)
			return 2;
		if (displayType == Number)
			return 6;
		if (displayType == CostPrice 
			|| displayType == Quantity)
			return 4;
		return 0;
	}	//	getDefaultPrecision
	

	/**
	 *	Returns true, if DisplayType is text (String, Text, TextLong, Memo).
	 *  @param displayType Display Type
	 *  @return true if text
	 */
	public static boolean isText(int displayType)
	{
		if (displayType == String || displayType == Text 
			|| displayType == TextLong || displayType == Memo || displayType ==EstadoChequesRecibidos)
			return true;
		return false;
	}	//	isText

	/**
	 *	Returns truem if DisplayType is a Date.
	 *  (stored as Timestamp)
	 *  @param displayType Display Type
	 *  @return true if date
	 */
	public static boolean isDate (int displayType)
	{
		if (displayType == Date || displayType == DateTime || displayType == Time)
			return true;
		return false;
	}	//	isDate

	/**
	 *	Returns true if DisplayType is a VLookup (List, Table, TableDir, Search).
	 *  (stored as Integer)
	 *  @param displayType Display Type
	 *  @return true if Lookup
	 */
	public static boolean isLookup(int displayType)
	{
		if (displayType == List || displayType == Table
			|| displayType == TableDir || displayType == Search)
			return true;
		return false;
	}	//	isLookup
	
	/**
	 * 	Returns true if DisplayType is a Large Object
	 *	@param displayType Display Type
	 *	@return true if LOB
	 */
	public static boolean isLOB (int displayType)
	{
		if (displayType == Binary || displayType == TextLong
			|| displayType == Image)
			return true;
		return false;
	}

	/**************************************************************************
	 *	Return Format for numeric DisplayType
	 *  @param displayType Display Type (default Number)
	 *  @param language Language
	 *  @return number format
	 */
	public static DecimalFormat getNumberFormat(int displayType, Language language)
	{
		Language myLanguage = language;
		if (myLanguage == null)
			myLanguage = Language.getLoginLanguage();
		Locale locale = myLanguage.getLocale();
		DecimalFormat format = null;
		if (locale != null)
			format = (DecimalFormat)NumberFormat.getNumberInstance(locale);
		else
			format = (DecimalFormat)NumberFormat.getNumberInstance(Locale.US);
		//
		if (displayType == Integer)
		{
			format.setParseIntegerOnly(true);
			format.setMaximumIntegerDigits(INTEGER_DIGITS);
			format.setMaximumFractionDigits(0);
		}
		else if (displayType == Quantity)
		{
			format.setMaximumIntegerDigits(MAX_DIGITS);
			format.setMaximumFractionDigits(MAX_FRACTION);
		}
		else if (displayType == Amount)
		{
			format.setMaximumIntegerDigits(MAX_DIGITS);
			format.setMaximumFractionDigits(AMOUNT_FRACTION);
			format.setMinimumFractionDigits(AMOUNT_FRACTION);
		}
		else if (displayType == CostPrice)
		{
			format.setMaximumIntegerDigits(MAX_DIGITS);
			format.setMaximumFractionDigits(MAX_FRACTION);
			format.setMinimumFractionDigits(AMOUNT_FRACTION);
		}
		
		/**
		 * 08-11-2010 Camarzana Mariano
		 * Modificacion realizada para poder manejar 3 decimales de precision en el tipo de cambio
		 */
		else if (displayType == TipoDeCambio)
		{
			format.setMaximumIntegerDigits(MAX_DIGITS);
			format.setMaximumFractionDigits(CHANGE_FRACTION);
			format.setMinimumFractionDigits(CHANGE_FRACTION);
		}
		
		else //	if (displayType == Number)
		{
			format.setMaximumIntegerDigits(MAX_DIGITS);
			format.setMaximumFractionDigits(MAX_FRACTION);
			format.setMinimumFractionDigits(1);
		}
		return format;
	}	//	getDecimalFormat

	/**
	 *	Return Format for numeric DisplayType
	 *  @param displayType Display Type
	 *  @return number format
	 */
	public static DecimalFormat getNumberFormat(int displayType)
	{
		return getNumberFormat (displayType, null);
	}   //  getNumberFormat


	/*************************************************************************
	 *	Return Date Format
	 *  @return date format
	 */
	public static SimpleDateFormat getDateFormat()
	{
		return getDateFormat (DisplayType.Date, null);
	}   //  getDateFormat

	/**
	 *	Return Date Format
	 *  @param language Language
	 *  @return date format
	 */
	public static SimpleDateFormat getDateFormat (Language language)
	{
		return getDateFormat (DisplayType.Date, language);
	}	//	getDateFormat

	/**
	 *	Return format for date displayType
	 *  @param displayType Display Type
	 *  @return date format
	 */
	public static SimpleDateFormat getDateFormat (int displayType)
	{
		return getDateFormat (displayType, null);
	}   //  getDateFormat

	/**
	 *	Return format for date displayType
	 *  @param displayType Display Type (default Date)
	 *  @param language Language
	 *  @return date format
	 */
	public static SimpleDateFormat getDateFormat (int displayType, Language language)
	{
		Language myLanguage = language;
		if (myLanguage == null)
			myLanguage = Language.getLoginLanguage();
		//
		if (displayType == DateTime)
			return myLanguage.getDateTimeFormat();
		else if (displayType == Time)
			return myLanguage.getTimeFormat();
	//	else if (displayType == Date)
		return myLanguage.getDateFormat();		//	default
	}	//	getDateFormat

	/**
	 *	JDBC Date Format YYYY-MM-DD
	 *  @return date format
	 */
	static public SimpleDateFormat getDateFormat_JDBC()
	{
		return new SimpleDateFormat ("yyyy-MM-dd");
	}   //  getDateFormat_JDBC

	/**
	 *  Return Storage Class.
	 *  (used for MiniTable)
	 *  @param displayType Display Type
	 *  @param yesNoAsBoolean - yes or no as boolean
	 *  @return class Integer - BigDecimal - Timestamp - String - Boolean
	 */
	public static Class getClass (int displayType, boolean yesNoAsBoolean)
	{
		/** BISion - 12/01/2009 - Santiago Ibañez
                 * Modificado para que considere como texto a EstadoChequesRecibidos
                 */
        if (displayType == String || displayType == Text || displayType == TextLong 
			|| displayType == List || displayType == Memo || displayType ==EstadoChequesRecibidos || displayType ==EstadoChequesPropios)
			return String.class;
		else if (isID(displayType) || displayType == Integer)    //  note that Integer is stored as BD
			return Integer.class;
		else if (isNumeric(displayType))
			return java.math.BigDecimal.class;
		else if (isDate(displayType))
			return java.sql.Timestamp.class;
		else if (displayType == YesNo)
			return yesNoAsBoolean ? Boolean.class : String.class;
		else if (displayType == Button)
			return String.class;
		else if (isLOB(displayType))	//	CLOB is String
			return byte[].class;
		//
		return Object.class;
	}   //  getClass

}	//	DisplayType
