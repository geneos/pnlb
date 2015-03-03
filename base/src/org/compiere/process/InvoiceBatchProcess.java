/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.process;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Process Invoice Batch	
 *	
 *  @author Jorg Janke
 *  @version $Id: InvoiceBatchProcess.java,v 1.4 2005/09/19 04:49:45 jjanke Exp $
 */
public class InvoiceBatchProcess extends SvrProcess
{
	/**	Batch to process		*/
	private int		p_C_InvoiceBatch_ID = 0;
	/** Action					*/
	private String	p_DocAction = null;
	
	/** Invoice					*/
	private MInvoice	m_invoice = null;
	/** Old DocumentNo			*/
	private String		m_oldDocumentNo = null;
	/** Old BPartner			*/
	private int			m_oldC_BPartner_ID = 0;
	/** Old BPartner Loc		*/
	private int			m_oldC_BPartner_Location_ID = 0;
	
	/** Counter					*/
	private int			m_count = 0;
	
	/**
	 *  Prepare - get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DocAction"))
				p_DocAction = (String)para[i].getParameter();
		}
		p_C_InvoiceBatch_ID = getRecord_ID();
	}   //  prepare

	/**
	 * 	Process Invoice Batch
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("C_InvoiceBatch_ID=" + p_C_InvoiceBatch_ID + ", DocAction=" + p_DocAction);
		if (p_C_InvoiceBatch_ID == 0)
			throw new CompiereUserError("C_InvoiceBatch_ID = 0");
		MInvoiceBatch batch = new MInvoiceBatch(getCtx(), p_C_InvoiceBatch_ID, get_TrxName());
		if (batch.get_ID() == 0)
			throw new CompiereUserError("@NotFound@: @C_InvoiceBatch_ID@ - " + p_C_InvoiceBatch_ID);
		if (batch.isProcessed())
			throw new CompiereUserError("@Processed@");
		//
		if (batch.getControlAmt().signum() != 0
			&& batch.getControlAmt().compareTo(batch.getDocumentAmt()) != 0)
			throw new CompiereUserError("@ControlAmt@ <> @DocumentAmt@");		
		//
		MInvoiceBatchLine[] lines = batch.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceBatchLine line = lines[i];
			if (line.getC_Invoice_ID() != 0 || line.getC_InvoiceLine_ID() != 0)
				continue;
			
			if ((m_oldDocumentNo != null 
					&& !m_oldDocumentNo.equals(line.getDocumentNo()))
				|| m_oldC_BPartner_ID != line.getC_BPartner_ID()
				|| m_oldC_BPartner_Location_ID != line.getC_BPartner_Location_ID())
				completeInvoice();
			//	New Invoice
			if (m_invoice == null)
			{
				m_invoice = new MInvoice (batch, line);
				if (!m_invoice.save())
					throw new CompiereUserError("Cannot save Invoice");
				//
				m_oldDocumentNo = line.getDocumentNo();
				m_oldC_BPartner_ID = line.getC_BPartner_ID();
				m_oldC_BPartner_Location_ID = line.getC_BPartner_Location_ID();
			}
			
			if (line.isTaxIncluded() != m_invoice.isTaxIncluded())
			{
				//	rollback
				throw new CompiereUserError("Line " + line.getLine() + " TaxIncluded inconsistent");
			}
			
			//	Add Line
			MInvoiceLine invoiceLine = new MInvoiceLine (m_invoice);
			invoiceLine.setDescription(line.getDescription());
			invoiceLine.setC_Charge_ID(line.getC_Charge_ID());
			invoiceLine.setQty(line.getQtyEntered());	// Entered/Invoiced
			invoiceLine.setPrice(line.getPriceEntered());
			invoiceLine.setC_Tax_ID(line.getC_Tax_ID());
			invoiceLine.setTaxAmt(line.getTaxAmt());
			invoiceLine.setLineNetAmt(line.getLineNetAmt());
			invoiceLine.setLineTotalAmt(line.getLineTotalAmt());
			if (!invoiceLine.save())
			{
				//	rollback
				throw new CompiereUserError("Cannot save Invoice Line");
			}

			//	Update Batch Line
			line.setC_Invoice_ID(m_invoice.getC_Invoice_ID());
			line.setC_InvoiceLine_ID(invoiceLine.getC_InvoiceLine_ID());
			line.save();
			
		}	//	for all lines
		completeInvoice();
		//
		batch.setProcessed(true);
		batch.save();
		
		return "#" + m_count;
	}	//	doIt
	
	
	/**
	 * 	Complete Invoice
	 */
	private void completeInvoice()
	{
		if (m_invoice == null)
			return;
		
		m_invoice.setDocAction(p_DocAction);
		m_invoice.processIt(p_DocAction);
		m_invoice.save();
		
		addLog(0, m_invoice.getDateInvoiced(), m_invoice.getGrandTotal(), m_invoice.getDocumentNo());
		m_count++;
		
		m_invoice = null;
	}	//	completeInvoice
	
}	//	InvoiceBatchProcess
