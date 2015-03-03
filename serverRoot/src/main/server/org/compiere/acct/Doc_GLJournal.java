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
package org.compiere.acct;

import java.math.*;
import java.sql.*;
import java.util.*;

import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              GL_Journal (224)
 *  Document Types:     GLJ
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_GLJournal.java,v 1.7 2005/10/26 00:40:02 jjanke Exp $
 */
public class Doc_GLJournal extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@parem trxName trx
	 */
	protected Doc_GLJournal (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super(ass, MJournal.class, rs, MDocType.DOCBASETYPE_GLJournal, trxName);
	}	//	Foc_GL_Journal

	/** Posting Type				*/
	private String			m_PostingType = null;
	private int				m_C_AcctSchema_ID = 0;
	
	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		MJournal journal = (MJournal)getPO();
		m_PostingType = journal.getPostingType();
		m_C_AcctSchema_ID = journal.getC_AcctSchema_ID();
		
		//	Contained Objects
		p_lines = loadLines(journal);
		log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails


	/**
	 *	Load Invoice Line
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines(MJournal journal)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MJournalLine[] lines = journal.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MJournalLine line = lines[i];
			DocLine docLine = new DocLine (line, this); 
			//  --  Source Amounts
			docLine.setAmount (line.getAmtSourceDr(), line.getAmtSourceCr());
			//  --  Converted Amounts
			docLine.setConvertedAmt (m_C_AcctSchema_ID, line.getAmtAcctDr(), line.getAmtAcctCr());
			//  --  Account
			MAccount account = line.getAccount();
			docLine.setAccount (account);
			//	--	Organization of Line was set to Org of Account
			list.add(docLine);
		}
		//	Return Array
		int size = list.size();
		DocLine[] dls = new DocLine[size];
		list.toArray(dls);
		return dls;
	}	//	loadLines

	
	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line and tax amounts from total - no rounding
	 *  @return positive amount, if total invoice is bigger than lines
	 */
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;
		StringBuffer sb = new StringBuffer (" [");
		//  Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			retValue = retValue.add(p_lines[i].getAmtSource());
			sb.append("+").append(p_lines[i].getAmtSource());
		}
		sb.append("]");
		//
		log.fine(toString() + " Balance=" + retValue + sb.toString());
		return retValue;
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  GLJ.
	 *  (only for the accounting scheme, it was created)
	 *  <pre>
	 *      account     DR          CR
	 *  </pre>
	 *  @param as acct schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		ArrayList<Fact> facts = new ArrayList<Fact>();
		//	Other Acct Schema
		if (as.getC_AcctSchema_ID() != m_C_AcctSchema_ID)
			return facts;
		
		//  create Fact Header
		Fact fact = new Fact (this, as, m_PostingType);

		//  GLJ
		if (getDocumentType().equals(DOCTYPE_GLJournal))
		{
			//  account     DR      CR
			for (int i = 0; i < p_lines.length; i++)
			{
				if (p_lines[i].getC_AcctSchema_ID () == as.getC_AcctSchema_ID ())
				{
					fact.createLine(p_lines[i], 
									p_lines[i].getAccount(),
									getC_Currency_ID(),
									p_lines[i].getAmtSourceDr (),
									p_lines[i].getAmtSourceCr ());
				}
			}	//	for all lines
		}
		else
		{
			p_Error = "DocumentType unknown: " + getDocumentType();
			log.log(Level.SEVERE, p_Error);
			fact = null;
		}
		//
		fact = fact.reordenarFactLine();
		facts.add(fact);
		return facts;
	}   //  createFact

}   //  Doc_GLJournal
