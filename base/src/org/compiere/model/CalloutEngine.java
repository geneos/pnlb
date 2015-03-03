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

import java.lang.reflect.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Callout Emgine.
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutEngine.java,v 1.6 2005/03/11 20:26:05 jjanke Exp $
 */
public class CalloutEngine implements Callout
{
	/**
	 *	Constructor
	 */
	public CalloutEngine()
	{
		super();
	}

	protected CLogger		log = CLogger.getCLogger(getClass());

	/**
	 *	Start Callout.
	 *  <p>
	 *	Callout's are used for cross field validation and setting values in other fields
	 *	when returning a non empty (error message) string, an exception is raised
	 *  <p>
	 *	When invoked, the Tab model has the new value!
	 *
	 *  @param ctx      Context
	 *  @param methodName   Method name
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @param oldValue The old value
	 *  @return Error message or ""
	 */
	public String start (Properties ctx, String methodName, int WindowNo,
		MTab mTab, MField mField, Object value, Object oldValue)
	{
		if (methodName == null || methodName.length() == 0)
			throw new IllegalArgumentException ("No Method Name");
		//
		String retValue = "";
		StringBuffer msg = new StringBuffer(methodName).append(" - ")
			.append(mField.getColumnName())
			.append("=").append(value)
			.append(" (old=").append(oldValue)
			.append(") {active=").append(isCalloutActive()).append("}");
		if (!isCalloutActive())
			log.info (msg.toString());
		
		//	Find Method
		Method method = getMethod(methodName);
		if (method == null)
			throw new IllegalArgumentException ("Method not found: " + methodName);
		int argLength = method.getParameterTypes().length;
		if (!(argLength == 5 || argLength == 6))
			throw new IllegalArgumentException ("Method " + methodName + " has invalid no of arguments: " + argLength);

		//	Call Method
		try
		{
			Object[] args = null;
			if (argLength == 6)
				args = new Object[] {ctx, new Integer(WindowNo), mTab, mField, value, oldValue};
			else
				args = new Object[] {ctx, new Integer(WindowNo), mTab, mField, value}; 
			retValue = (String)method.invoke(this, args);
		}
		catch (Exception e)
		{
			setCalloutActive(false);
			Throwable ex = e.getCause();	//	InvocationTargetException
			if (ex == null)
				ex = e;
			log.log(Level.SEVERE, "start: " + methodName, ex);
			ex.printStackTrace(System.err);
			retValue = ex.getLocalizedMessage();
		}
		return retValue;
	}	//	start
	
	/**
	 *	Conversion Rules.
	 *	Convert a String
	 *
	 *	@param methodName   method name
	 *  @param value    the value
	 *	@return converted String or Null if no method found
	 */
	public String convert (String methodName, String value)
	{
		if (methodName == null || methodName.length() == 0)
			throw new IllegalArgumentException ("No Method Name");
		//
		String retValue = null;
		StringBuffer msg = new StringBuffer(methodName).append(" - ").append(value);
		log.info (msg.toString());
		//
		//	Find Method
		Method method = getMethod(methodName);
		if (method == null)
			throw new IllegalArgumentException ("Method not found: " + methodName);
		int argLength = method.getParameterTypes().length;
		if (argLength != 1)
			throw new IllegalArgumentException ("Method " + methodName + " has invalid no of arguments: " + argLength);

		//	Call Method
		try
		{
			Object[] args = new Object[] {value};
			retValue = (String)method.invoke(this, args);
		}
		catch (Exception e)
		{
			setCalloutActive(false);
			log.log(Level.SEVERE, "convert: " + methodName, e);
			e.printStackTrace(System.err);
		}
		return retValue;
	}   //  convert
	
	/**
	 * 	Get Method
	 *	@param methodName method name
	 *	@return method or null
	 */
	private Method getMethod (String methodName)
	{
		Method[] allMethods = getClass().getMethods();
		for (int i = 0; i < allMethods.length; i++)
		{
			if (methodName.equals(allMethods[i].getName()))
				return allMethods[i];
		}
		return null;
	}	//	getMethod

	/*************************************************************************/
	
	private static boolean s_calloutActive = false;
	
	/**
	 * 	Is Callout Active
	 *	@return true if active
	 */
	protected static boolean isCalloutActive()
	{
		return s_calloutActive;
	}	//	isCalloutActive

	/**
	 * 	Set Callout (in)active
	 *	@param active active
	 */
	protected static void setCalloutActive (boolean active)
	{
		s_calloutActive = active;
	}	//	setCalloutActive
	
	/**
	 *  Set Account Date Value.
	 * 	org.compiere.model.CalloutEngine.dateAcct
	 */
	public String dateAcct (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
	//	setCalloutActive(true);
		if (value == null || !(value instanceof Timestamp))
			return "";
		mTab.setValue("DateAcct", value);
	//	setCalloutActive(false);
		return "";
	}	//	dateAcct

	/**
	 *	Rate - set Multiply Rate from Divide Rate and vice versa
	 *	org.compiere.model.CalloutEngine.rate
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @param mTab tab
	 *  @param mField field
	 *  @param value value
	 *  @return error message
	 */
	public String rate (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{
		if (isCalloutActive() || value == null)		//	assuming it is Conversion_Rate
			return "";
		setCalloutActive(true);

		BigDecimal rate1 = (BigDecimal)value;
		BigDecimal rate2 = Env.ZERO;
		BigDecimal one = new BigDecimal(1.0);

		if (rate1.doubleValue() != 0.0)	//	no divide by zero
			rate2 = one.divide(rate1, 12, BigDecimal.ROUND_HALF_UP);
		//
		if (mField.getColumnName().equals("MultiplyRate"))
			mTab.setValue("DivideRate", rate2);
		else
			mTab.setValue("MultiplyRate", rate2);
		log.info(mField.getColumnName() + "=" + rate1 + " => " + rate2);
		setCalloutActive(false);
		return "";
	}	//	rate

}	//	CalloutEngine
