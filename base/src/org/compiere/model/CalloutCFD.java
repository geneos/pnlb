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
package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	CFD Callouts.
 *	
 *  @author Oscar Gómez Islas
 *  @version $Id: CalloutCFD.java,v 1.31 2005/04/20 04:55:24 ogómezi Exp $
 */
public class CalloutCFD extends org.compiere.model.CalloutEngine
{
	public String End (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		Integer SeqRange, SeqBegin, sBegin;
		boolean xError      = false;
		Integer AD_Sequence = (Integer)mTab.getValue("AD_Sequence_ID");
		SeqBegin            = (Integer)mTab.getValue("SequenceBegin");
		SeqRange            = (Integer)mTab.getValue("SequenceRange");
		try
		{
			String SQL = "SELECT cs.SequenceBegin,cs.SequenceEnd " //	1,2
				+ "FROM E_CtrlSequence cs, AD_Sequence s "
				+ "WHERE cs.AD_Sequence_ID=?"	//	#1
				+ " AND cs.AD_Sequence_ID=s.AD_Sequence_ID";
			PreparedStatement pstmt = DB.prepareStatement(SQL);
			pstmt.setInt(1, AD_Sequence.intValue());
			ResultSet rs = pstmt.executeQuery();
			
			sBegin = (Integer)mTab.getValue("SequenceBegin");
			if (rs.next()) {
				int xBegin = rs.getInt(1);
				int xEnd   = rs.getInt(2);
				if (sBegin.intValue() > (xBegin-1) & sBegin.intValue() < (xEnd+1) ){
					xError = true;
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "Error", e);
			return e.getLocalizedMessage();
		}
		//
		if (xError){
			mTab.fireDataStatusEEvent("La secuencia de Folios no es Válida","El Folio " + DisplayType.getNumberFormat(DisplayType.Amount).format(sBegin) + " pertenece a otra Secuencia", true);
			mTab.setValue("SequenceBegin", Env.ZERO);
		}
		else{
	        int SeqEnd   = SeqRange.intValue() + SeqBegin.intValue() - 1;
			mTab.setValue("SequenceEnd", new Integer( SeqEnd ));
		}
		//
		return "";
	}
	
}	//	CalloutOrder