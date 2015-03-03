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

import org.compiere.model.*;
import org.compiere.process.DocAction;
import org.compiere.util.*;
import org.compiere.wf.MWFActivity;
import org.compiere.wf.MWFNextCondition;

/**
 *	Forcast Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MForcastLine.java,v 1.11 2005/05/17 05:29:52 vpj-cd Exp $
 */
public class MQMSpecificationLine extends  X_QM_SpecificationLine
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_ForecastLine_ID id
	 */
	public MQMSpecificationLine (Properties ctx, int QM_SpecificationLine_ID, String trxName)
	{
		super (ctx, QM_SpecificationLine_ID, trxName);
		if (QM_SpecificationLine_ID == 0)
		{		
		}
		
	}	//	MQMSpecification

	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MQMSpecificationLine(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MQMSpecification
	
		
	/**
	 * 	Evaluate Condition
	 *	@return true if true
	 */
	public boolean evaluate (Object valueObj,String value1)
	{
		/*if (getAD_Column_ID() == 0)
			throw new IllegalStateException("No Column defined - " + this);
			
		PO po = activity.getPO();
		if (po == null || po.get_ID() == 0)
			throw new IllegalStateException("Could not evaluate " + po + " - " + this);
		//
		Object valueObj = po.get_ValueOfColumn(getAD_Column_ID());
		if (valueObj == null)
			valueObj = "";
		String value1 = getValue();
		if (value1 == null)
			value1 = "";
		String value2 = getValue2();
		if (value2 == null)
			value2 = "";
		
		String resultStr = "PO:{" + valueObj + "} " + getOperation() + " Condition:{" + value1 + "}";
		if (getOperation().equals(OPERATION_Sql))
			throw new IllegalArgumentException("SQL Operator not implemented yet: " + resultStr);
		if (getOperation().equals(OPERATION_X))
			resultStr += "{" + value2 + "}";*/

		boolean result = false;
		if (valueObj instanceof Number)
			result = compareNumber ((Number)valueObj, value1, getValue());
		else
			result = compareString(valueObj, value1, getValue());
		//
		//log.fine(resultStr + " -> " + result 
		//	+ (m_numeric ? " (#)" : " ($)"));
		return result;
	}	//	evaluate
	
	/**
	 * 	Compare Number
	 *	@param valueObj comparator
	 *	@return true if operation
	 */
	private boolean compareNumber (Number valueObj, String value1, String value2)
	{
		BigDecimal valueObjB = null;
		BigDecimal value1B = null;
		BigDecimal value2B = null;
		try
		{
			if (valueObj instanceof BigDecimal)
				valueObjB = (BigDecimal)valueObj;
			else if (valueObj instanceof Integer)
				valueObjB = new BigDecimal (((Integer)valueObj).intValue());
			else
				valueObjB = new BigDecimal (String.valueOf(valueObj));
		}
		catch (Exception e)
		{
			log.fine("compareNumber - valueObj=" + valueObj + " - " + e.toString());
			return compareString(valueObj, value1, value2);
		}
		try
		{
			value1B = new BigDecimal (value1);
		}
		catch (Exception e)
		{
			log.fine("compareNumber - value1=" + value1 + " - " + e.toString());
			return compareString(valueObj, value1, value2);
		}
		
		String op = getOperation();
		if (OPERATION_Eq.equals(op))
			return valueObjB.compareTo(value1B) == 0;
		else if (OPERATION_Gt.equals(op))
			return valueObjB.compareTo(value1B) > 0;
		else if (OPERATION_GtEq.equals(op))
			return valueObjB.compareTo(value1B) >= 0;
		else if (OPERATION_Le.equals(op))
			return valueObjB.compareTo(value1B) < 0;
		else if (OPERATION_LeEq.equals(op))
			return valueObjB.compareTo(value1B) <= 0;
		else if (OPERATION_Like.equals(op))
			return valueObjB.compareTo(value1B) == 0;
		else if (OPERATION_NotEq.equals(op))
			return valueObjB.compareTo(value1B) != 0;
		//
		else if (OPERATION_Sql.equals(op))
			throw new IllegalArgumentException("SQL not Implemented");
		//
		else if (OPERATION_X.equals(op))
		{
			if (valueObjB.compareTo(value1B) < 0)
				return false;
			//	To
			try
			{
				value2B = new BigDecimal (String.valueOf(value2));
				return valueObjB.compareTo(value2B) <= 0;
			}
			catch (Exception e)
			{
				log.fine("compareNumber - value2=" + value2 + " - " + e.toString());
				return false;
			}
		}
		//
		throw new IllegalArgumentException("Unknown Operation=" + op);
	}	//	compareNumber
	
	/**
	 * 	Compare String
	 *	@param valueObj comparator
	 *	@return true if operation
	 */
	private boolean compareString (Object valueObj, String value1S, String value2S)
	{
		//m_numeric = false;
		String valueObjS = String.valueOf(valueObj);
		//
		String op = getOperation();
		if (OPERATION_Eq.equals(op))
			return valueObjS.compareTo(value1S) == 0;
		else if (OPERATION_Gt.equals(op))
			return valueObjS.compareTo(value1S) > 0;
		else if (OPERATION_GtEq.equals(op))
			return valueObjS.compareTo(value1S) >= 0;
		else if (OPERATION_Le.equals(op))
			return valueObjS.compareTo(value1S) < 0;
		else if (OPERATION_LeEq.equals(op))
			return valueObjS.compareTo(value1S) <= 0;
		else if (OPERATION_Like.equals(op))
			return valueObjS.compareTo(value1S) == 0;
		else if (OPERATION_NotEq.equals(op))
			return valueObjS.compareTo(value1S) != 0;
		//
		else if (OPERATION_Sql.equals(op))
			throw new IllegalArgumentException("SQL not Implemented");
		//
		else if (OPERATION_X.equals(op))
		{
			if (valueObjS.compareTo(value1S) < 0)
				return false;
			//	To
			return valueObjS.compareTo(value2S) <= 0;
		}
		//
		throw new IllegalArgumentException("Unknown Operation=" + op);
	}	//	compareString

}	//	MForcastLine
