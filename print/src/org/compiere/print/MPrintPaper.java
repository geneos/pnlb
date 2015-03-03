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
package org.compiere.print;

import java.util.*;

//import javax.print.attribute.Size2DSyntax;
//import javax.print.attribute.standard.*;

import org.compiere.model.*;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.MediaSize;
import org.compiere.util.MediaSizeName;
/**
 * AD_PrintPaper Print Paper Model
 * 
 * @author Jorg Janke
 * @version $Id: MPrintPaper.java,v 1.19 2005/11/13 23:40:21 jjanke Exp $
 */
public class MPrintPaper extends X_AD_PrintPaper {
	/**
	 * Constructor
	 * 
	 * @param ctx
	 *            context
	 * @param AD_PrintPaper_ID
	 *            ID if 0 A4
	 */
	private MPrintPaper(Properties ctx, int AD_PrintPaper_ID, String trxName) {
		super(ctx, AD_PrintPaper_ID, trxName);
		if (AD_PrintPaper_ID == 0) {
			setIsDefault(false);
			setIsLandscape(true);
			setCode("iso-a4");
			setMarginTop(36);
			setMarginBottom(36);
			setMarginLeft(36);
			setMarginRight(36);
		}
	} // MPrintPaper

	/** Logger */
	private static CLogger s_log = CLogger.getCLogger(MPrintPaper.class);

	/***************************************************************************
	 * Get Media Size. The search is hard coded as the javax.print.MediaSize*
	 * info is private
	 * 
	 * @return MediaSize from Code
	 */
	/*
	  public MediaSize getMediaSize() { 
	  	String nameCode = getCode(); 
	  	if(nameCode == null) return getMediaSizeDefault(); 
	  //Get Name
	  MediaSizeName nameMedia = null; 
	  if (nameCode.equals("iso-a4")) 
	  nameMedia = MediaSizeName.ISO_A4; 
	  else if (nameCode.equals("na-letter")) 
	  nameMedia = MediaSizeName.NA_LETTER; 
	  else if (nameCode.equals("na-legal")) 
	  nameMedia =  MediaSizeName.NA_LEGAL; 
	  // other media sizes come here
	   if (nameMedia == null) 
	   		return getMediaSizeDefault(); 
	   		// MediaSize
	  retValue = MediaSize.getMediaSizeForName(nameMedia); 
	  if (retValue ==  null) 
	  	retValue = getMediaSizeDefault(); 
	  	// log.fine("MPrintPaper.getMediaSize", retValue); return retValue; } // getMediaSize
	 */
	public MediaSize getMediaSize() {
		String nameCode = getCode();
		if (nameCode == null)
			return getMediaSizeDefault();
		// Get Name
		MediaSizeName nameMedia = null;
		MediaSize retValue = null;
		if (nameCode.equals("iso-a4"))
			nameMedia = MediaSizeName.ISO_A4;
		else if (nameCode.equals("na-letter"))
			nameMedia = MediaSizeName.NA_LETTER;
		else if (nameCode.equals("na-legal"))
			nameMedia = MediaSizeName.NA_LEGAL;
		
		else if (nameCode.equals("cheques-StandardBank"))
			nameMedia = MediaSizeName.CHEQUES_STANDARDBANK;
		
		else if (nameCode.equals("cheques-hsbc"))
			nameMedia = MediaSizeName.CHEQUES_HSBC;
		
		else if (nameCode.equals("cheques-galicia"))
			nameMedia = MediaSizeName.CHEQUES_GALICIA;
		// other media sizes come here

		// Agregado para incorporar medidas de impresion para cheques

		
			if (nameMedia == null)
			return getMediaSizeDefault();
			retValue = MediaSize.getMediaSizeForName(nameMedia);
		if (retValue == null)
			retValue = getMediaSizeDefault();
		// log.fine( "MPrintPaper.getMediaSize", retValue);
		return retValue;
	} // getMediaSize

	/**
	 * Get Media Size
	 * 
	 * @return Default Media Size based on Language
	 */
	public MediaSize getMediaSizeDefault() {
		MediaSize retValue = Language.getLoginLanguage().getMediaSize();
		if (retValue == null)
			retValue = MediaSize.ISO.A4;
		log.fine(retValue.toString());
		return retValue;
	} // getMediaSizeDefault

	/**
	 * Get CPaper
	 * 
	 * @return CPaper
	 */
	public CPaper getCPaper() {
		CPaper retValue = new CPaper(getMediaSize(), isLandscape(),
				getMarginLeft(), getMarginTop(), getMarginRight(),
				getMarginBottom());
		return retValue;
	} // getCPaper

	/***************************************************************************
	 * Create Paper and save
	 * 
	 * @param name
	 *            name
	 * @param landscape
	 *            landscape
	 * @return Paper
	 */
	static MPrintPaper create(String name, boolean landscape) {
		MPrintPaper pp = new MPrintPaper(Env.getCtx(), 0, null);
		pp.setName(name);
		pp.setIsLandscape(landscape);
		pp.save();
		return pp;
	} // create

	/** ********************************************************************** */

	/** Cached Fonts */
	static private CCache<Integer, MPrintPaper> s_papers = new CCache<Integer, MPrintPaper>(
			"AD_PrintPaper", 5);

	/**
	 * Get Paper
	 * 
	 * @param AD_PrintPaper_ID
	 *            id
	 * @return Paper
	 */
	static public MPrintPaper get(int AD_PrintPaper_ID) {
		Integer key = new Integer(AD_PrintPaper_ID);
		MPrintPaper pp = (MPrintPaper) s_papers.get(key);
		if (pp == null) {
			pp = new MPrintPaper(Env.getCtx(), AD_PrintPaper_ID, null);
			s_papers.put(key, pp);
		} else
			s_log.config("AD_PrintPaper_ID=" + AD_PrintPaper_ID);
		return pp;
	} // get

	/***************************************************************************
	 * Test
	 * 
	 * @param args
	 *            args
	 */
	public static void main(String[] args) {
		org.compiere.Compiere.startupEnvironment(true);

		// create ("Standard Landscape", true);
		// create ("Standard Portrait", false);

		// Read All Papers
		int[] IDs = PO.getAllIDs("AD_PrintPaper", null, null);
		for (int i = 0; i < IDs.length; i++) {
			System.out.println("--");
			MPrintPaper pp = new MPrintPaper(Env.getCtx(), IDs[i], null);
			pp.dump();
		}

	}
} // MPrintPaper
