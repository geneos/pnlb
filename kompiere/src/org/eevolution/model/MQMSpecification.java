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
 * Created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.eevolution.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.grid.ed.VNumber;
import org.compiere.grid.ed.VString;
import org.compiere.model.*;
import org.compiere.swing.CComboBox;
import org.compiere.util.*;
import org.compiere.wf.MWFActivity;
import org.compiere.wf.MWFNextCondition;

/**
 *	Forcast Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MForcastLine.java,v 1.11 2005/05/17 05:29:52 vpj-cd Exp $
 */
public class MQMSpecification extends  X_QM_Specification
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_ForecastLine_ID id
	 */
	public MQMSpecification (Properties ctx, int QM_Specification_ID, String trxName)
	{
		super (ctx, QM_Specification_ID, trxName);
		if (QM_Specification_ID == 0)
		{		
		}
		
	}	//	MQMSpecification

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MQMSpecification (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MQMSpecification	
		
	/** Lines						*/
	private MQMSpecificationLine[]		m_lines = null;
	
	/**
	 * 	Get Lines
	 *	@return array of lines
	 */
	public MQMSpecificationLine[] getLines(String where)
	{
		if (m_lines != null)
			return m_lines;
		
		ArrayList<MQMSpecificationLine> list = new ArrayList<MQMSpecificationLine>();
		String sql = "SELECT * FROM QM_SpecificationLine WHERE QM_SpecificationLine_ID=? AND "+ where +" ORDER BY Line";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getQM_Specification_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MQMSpecificationLine(getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getLines", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		m_lines = new MQMSpecificationLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines
	
	public boolean isValid(int M_AttributeSetInstance_ID)
	{
		//MAttributeSet mas = MAttributeSet.get(getCtx(), getM_AttributeSet_ID());
		
//		Save Instance Attributes
		  
		MAttributeSetInstance asi = new MAttributeSetInstance(getCtx(),M_AttributeSetInstance_ID, get_TrxName());
		MAttributeSet 		  as = MAttributeSet.get(getCtx(),asi.getM_AttributeSet_ID());
		MAttribute[] attributes = as.getMAttributes(false);
		for (int i = 0; i < attributes.length; i++)
		{
		
			//MAttribute attribute = new MAttribute(getCtx(),0,null);
			MAttributeInstance instance = attributes[i].getMAttributeInstance (M_AttributeSetInstance_ID);			
			MQMSpecificationLine[] lines = getLines(" M_Attribute_ID="+attributes[i].getM_Attribute_ID());
			for (int s = 0; s < lines.length; i++)
			{
				MQMSpecificationLine line = lines[s];
				if (MAttribute.ATTRIBUTEVALUETYPE_Number.equals(attributes[i].getAttributeValueType()))
				{
				BigDecimal objValue = instance.getValueNumber();
				if(!line.evaluate(objValue,instance.getValue()));
				return false;
				}
				else
				{
				String	objValue = instance.getValue();
				if(!line.evaluate(objValue,instance.getValue()))
					return false;	
				}
				//if(line.evaluate(mas.getValueNumber())
			}			
		}	//	for all attributes			
		return true;
	}
	
}	//	MQMSpecification
