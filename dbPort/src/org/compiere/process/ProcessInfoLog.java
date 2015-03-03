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
package org.compiere.process;

import java.io.*;
import java.sql.*;
import java.math.*;

/**
 * 	Process Info Log (VO)
 *
 *  @author Jorg Janke
 *  @version $Id: ProcessInfoLog.java,v 1.6 2005/03/11 20:29:06 jjanke Exp $
 */
public class ProcessInfoLog implements Serializable
{
	/**
	 * 	Create Process Info Log.
	 *	@param P_ID Process ID
	 *	@param P_Date Process Date
	 *	@param P_Number Process Number
	 *	@param P_Msg Process Messagre
	 */
	public ProcessInfoLog (int P_ID, Timestamp P_Date, BigDecimal P_Number, String P_Msg)
	{
		this (s_Log_ID++, P_ID, P_Date, P_Number, P_Msg);
	}	//	ProcessInfoLog

	/**
	 * 	Create Process Info Log.
	 *	@param Log_ID Log ID
	 *	@param P_ID Process ID
	 *	@param P_Date Process Date
	 *	@param P_Number Process Number
	 *	@param P_Msg Process Messagre
	 */
	public ProcessInfoLog (int Log_ID, int P_ID, Timestamp P_Date, BigDecimal P_Number, String P_Msg)
	{
		setLog_ID (Log_ID);
		setP_ID (P_ID);
		setP_Date (P_Date);
		setP_Number (P_Number);
		setP_Msg (P_Msg);
	}	//	ProcessInfoLog

	private static int	s_Log_ID = 0;

	private int 		m_Log_ID;
	private int 		m_P_ID;
	private Timestamp 	m_P_Date;
	private BigDecimal	m_P_Number;
	private String 		m_P_Msg;



	/**
	 * Method getLog_ID
	 * @return int
	 */
	public int getLog_ID()
	{
		return m_Log_ID;
	}
	public void setLog_ID (int Log_ID)
	{
		m_Log_ID = Log_ID;
	}

	/**
	 * Method getP_ID
	 * @return int
	 */
	public int getP_ID()
	{
		return m_P_ID;
	}
	/**
	 * Method setP_ID
	 * @param P_ID int
	 */
	public void setP_ID (int P_ID)
	{
		m_P_ID = P_ID;
	}

	/**
	 * Method getP_Date
	 * @return Timestamp
	 */
	public Timestamp getP_Date()
	{
		return m_P_Date;
	}
	/**
	 * Method setP_Date
	 * @param P_Date Timestamp
	 */
	public void setP_Date (Timestamp P_Date)
	{
		m_P_Date = P_Date;
	}

	/**
	 * Method getP_Number
	 * @return BigDecimal
	 */
	public BigDecimal getP_Number()
	{
		return m_P_Number;
	}
	/**
	 * Method setP_Number
	 * @param P_Number BigDecimal
	 */
	public void setP_Number (BigDecimal P_Number)
	{
		m_P_Number = P_Number;
	}

	/**
	 * Method getP_Msg
	 * @return String
	 */
	public String getP_Msg()
	{
		return m_P_Msg;
	}
	/**
	 * Method setP_Msg
	 * @param P_Msg String
	 */
	public void setP_Msg (String P_Msg)
	{
		m_P_Msg = P_Msg;
	}

}	//	ProcessInfoLog
