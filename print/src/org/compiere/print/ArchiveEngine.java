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

import java.awt.print.*;
import org.compiere.model.*;
import org.compiere.print.layout.*;
import org.compiere.util.*;
import com.qoppa.pdf.*;
import com.qoppa.pdfProcess.*;


/**
 *	Archive Engine.
 *	Based on Settings on Client Level
 *	Keys set for
 *	- Menu Reports - AD_Report_ID
 *	- Win Report - AD_Table_ID
 *	- Documents - AD_Table_ID & Record_ID & C_Customer_ID 
 *	
 *  @author Jorg Janke
 *  @version $Id: ArchiveEngine.java,v 1.4 2005/03/11 20:34:41 jjanke Exp $
 */
public class ArchiveEngine
{
	/**
	 * 	Get/Create Archive
	 * 	@return existing document or newly created if Client enabled archiving. 
	 * 	Will return NULL if archiving not enabled
	 */ 
	public PDFDocument archive (LayoutEngine layout, PrintInfo info)
	{
		//	Do we need to Archive ?
		MClient client = MClient.get(layout.getCtx());
		String aaClient = client.getAutoArchive();
		String aaRole = null; 	//	role.getAutoArchive();	//	TODO
		String aa = aaClient;
		if (aa == null)
			aa = MClient.AUTOARCHIVE_None;
		if (aaRole != null)
		{
			if (aaRole.equals(MClient.AUTOARCHIVE_AllReportsDocuments))
				aa = aaRole;
			else if (aaRole.equals(MClient.AUTOARCHIVE_Documents) && !aaClient.equals(MClient.AUTOARCHIVE_AllReportsDocuments))
				aa = aaRole;
		}
		//	Mothing to Archive
		if (aa.equals(MClient.AUTOARCHIVE_None))
			return null;
		//	Archive External only
		if (aa.equals(MClient.AUTOARCHIVE_ExternalDocuments))
		{
			if (info.isReport())
				return null;
		}
		//	Archive Documents only
		if (aa.equals(MClient.AUTOARCHIVE_Documents))
		{
			if (info.isReport())
				return null;
		}
		
		//	Create Printable
		byte[] data = Document.getPDFAsArray(layout.getPageable(false));	//	No Copy
		if (data == null)
			return null;

		//	TODO to be done async
		MArchive archive = new MArchive (layout.getCtx(),info, null);
		archive.setBinaryData(data);
		archive.save();
		
		return null;
	}	//	archive
	
	/**
	 * 	Can we archive the document?
	 *	@param layout layout
	 *	@return true if can be archived
	 */
	public static boolean isValid (LayoutEngine layout)
	{
		return (layout != null 
			&& Document.isValid((Pageable)layout)
			&& layout.getNumberOfPages() > 0);
	}	//	isValid
	
	
	/**
	 * 	Get Archive Engine
	 *	@return engine
	 */
	public static ArchiveEngine get()
	{
		if (s_engine == null)
			s_engine = new ArchiveEngine();
		return s_engine;
	}	//	get
	
	//	Create Archiver
	static {
		s_engine = new ArchiveEngine();
	}
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ArchiveEngine.class);
	/** Singleton		*/
	private static ArchiveEngine s_engine = null;
	
	
	/**************************************************************************
	 * 	ArchiveEngine
	 */
	private ArchiveEngine ()
	{
		super ();
		if (s_engine == null)
			s_engine = this;
	}	//	ArchiveEngine

	/** The base document			*/
//	private PDFDocument m_document = Document.createBlank();
	
	
	
}	//	ArchiveEngine
