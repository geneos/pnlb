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
import org.compiere.*;
import org.compiere.util.*;

/**
 *	Test Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MTest.java,v 1.9 2005/07/18 03:50:13 jjanke Exp $
 */
public class MTest extends X_Test
{
	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param Test_ID
	 */
	public MTest(Properties ctx, int Test_ID, String trxName)
	{
		super (ctx, Test_ID, trxName);
	}	//	MTest

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MTest(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MTest

	/**
	 * 	Test Object Constructor
	 *	@param ctx context
	 *	@param testString test string
	 */
	public MTest (Properties ctx, String testString, int testNo)
	{
		super(ctx, 0, null);
		testString = testString + "_" + testNo;
		setName(testString);
		setDescription(testString + " " + testString + " " + testString);
		setHelp (getDescription() + " - " + getDescription());
		setT_Date(new Timestamp (System.currentTimeMillis()));
		setT_DateTime(new Timestamp (System.currentTimeMillis()));
		setT_Integer(testNo);
		setT_Amount(new BigDecimal(testNo));
		setT_Number(Env.ONE.divide(new BigDecimal(testNo), BigDecimal.ROUND_HALF_UP));
		//
		setC_Currency_ID(100);		//	USD
		setC_Location_ID(109);		//	Monroe
		setC_UOM_ID(100);			//	Each
	//	setC_BPartner_ID(C_BPartner_ID);
	//	setC_Payment_ID(C_Payment_ID);
	//	setM_Locator_ID(M_Locator_ID);
	//	setM_Product_ID(M_Product_ID);
	}	//	MTest

	
	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		log.info("***");
		return true;
	}
	
	/**
	 * 	After Delete
	 *	@param success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		log.info("*** Success=" + success);
		return success;
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		log.info("New=" + newRecord + " ***");
		return true;
	}
	
	/**
	 * 	After Save
	 *	@param newRecord
	 *	@param success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		log.info("New=" + newRecord + ", Seccess=" + success + " ***");
		return success;
	}	//	afterSave

	
	/*************************************************************************
	 * 	Test
	 *	@param args
	 */
	public static void main(String[] args)
	{
		Compiere.startup(true);
		Properties ctx = Env.getCtx();
		
		/** Test CLOB	*/
		MTest t1 = new MTest (ctx, 0, null);
		t1.setName("Test1");
		System.out.println("->" + t1.getCharacterData() + "<-");
		t1.save();
		t1.setCharacterData("Long Text JJ");
		t1.save();
		int Test_ID = t1.getTest_ID();
		//
		MTest t2 = new MTest (Env.getCtx(), Test_ID, null);
		System.out.println("->" + t2.getCharacterData() + "<-");
		
		t2.delete(true);
		
		
		/**	Volume Test 
		for (int i = 1; i < 20000; i++)
		{
			new MTest (ctx, "test", i).save();
		}		
		/** */	
	}	//	main
	
}	//	MTest
