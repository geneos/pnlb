/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Initial Developer is ActFact BV.
 * Copyright (C) 2003-2004 ActFact BV and Compiere Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.process;

import java.math.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Process for loading Bank Statements into I_BankStatement
 *
 *	@author Maarten Klinker, Eldir Tomassen
 *	@version $Id: LoadBankStatement.java,v 1.7 2005/09/19 04:49:45 jjanke Exp $
 */
public class LoadBankStatement extends SvrProcess
{
	public LoadBankStatement()
	{
		super();
		log.info("LoadBankStatement");
	}	//	LoadBankStatement

	/**	Client to be imported to			*/
	private int				m_AD_Client_ID = 0;

	/** Organization to be imported to			*/
	private int				m_AD_Org_ID = 0;
	
	/** Ban Statement Loader				*/
	private int m_C_BankStmtLoader_ID = 0;

	/** File to be imported					*/
	private String fileName = "";
	
	/** Current context					*/
	private Properties m_ctx;
	
	/** Current context					*/
	private MBankStatementLoader m_controller = null;
	

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		log.info("");
		m_ctx = Env.getCtx();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("C_BankStatementLoader_ID"))
				m_C_BankStmtLoader_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("FileName"))
				fileName = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		m_AD_Client_ID = Env.getAD_Client_ID(m_ctx);
		log.info("AD_Client_ID=" + m_AD_Client_ID);
		m_AD_Org_ID = Env.getAD_Org_ID(m_ctx);
		log.info("AD_Org_ID=" + m_AD_Org_ID);
		log.info("C_BankStatementLoader_ID=" + m_C_BankStmtLoader_ID);
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		log.info("LoadBankStatement.doIt");
		String message = "@Error@";
		
		m_controller = new MBankStatementLoader(m_ctx, m_C_BankStmtLoader_ID, fileName, get_TrxName());
		log.info(m_controller.toString());
		
		if (m_controller == null || m_controller.get_ID() == 0)
			log.log(Level.SEVERE, "Invalid Loader");

		// Start loading bank statement lines
		else if (!m_controller.loadLines())
			log.log(Level.SEVERE, m_controller.getErrorMessage() + " - " + m_controller.getErrorDescription());
		
		else
		{
			log.info("Imported=" + m_controller.getLoadCount());
			addLog (0, null, new BigDecimal (m_controller.getLoadCount()), "@Loaded@");
			message = "@OK@";
		}

		return message;
	}	//	doIt

}	//	LoadBankStatement
