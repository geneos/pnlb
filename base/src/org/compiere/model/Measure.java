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

import java.math.BigDecimal;

/**
 *  Performance Key Indicator Interface
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: Measure.java,v 1.6 2005/12/27 06:17:56 jjanke Exp $
 */
public interface Measure
{
	/**
	 * 	Create new Measures for the Performance Goal?
	 * 	@param measure measure
	 * 	@return true if measure not created via MeasureCalc (sql)
	 */
	public boolean isCreateMeasures (MMeasure measure);

	/**
	 * 	Create new Measures for the Performance Goal.
	 * 	Called only if isCreateMeasures is true
	 * 	@param measure measure
	 * 	@return number created
	 */
	public int createMeasures (MMeasure measure);

	/**
	 * 	Calculate Parformance Goal Actual
	 * 	@param measure measure
	 * 	@return new Actual Measure
	 */
	public BigDecimal calculateMeasure (MMeasure measure);

}   //  Measure
