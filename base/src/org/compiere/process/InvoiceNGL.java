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

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.math.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Invoice Not realized Gain & Loss.
 * 	The actual data shown is T_InvoiceGL_v
 *  @author Jorg Janke
 *  @version $Id: InvoiceNGL.java,v 1.6 2005/12/27 06:18:08 jjanke Exp $
 */
public class InvoiceNGL extends SvrProcess
{
	/**	Mandatory Acct Schema			*/
	private int				p_C_AcctSchema_ID = 0;
	/** Mandatory Conversion Type		*/
	private int				p_C_ConversionTypeReval_ID = 0;
	/** Revaluation Date				*/
	private Timestamp		p_DateReval = null;
	/** Only AP/AR Transactions			*/
	private String			p_APAR = "A";
	private static String	ONLY_AP = "P";
	private static String	ONLY_AR = "R";
	/** Report all Currencies			*/
	private boolean			p_IsAllCurrencies = false;
	/** Optional Invoice Currency		*/
	private int				p_C_Currency_ID = 0;
	/** GL Document Type				*/
	private int				p_C_DocTypeReval_ID = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_AcctSchema_ID"))
				p_C_AcctSchema_ID = para[i].getParameterAsInt();
			else if (name.equals("C_ConversionTypeReval_ID"))
				p_C_ConversionTypeReval_ID = para[i].getParameterAsInt();
			else if (name.equals("DateReval"))
				p_DateReval = (Timestamp)para[i].getParameter();
			else if (name.equals("APAR"))
				p_APAR = (String)para[i].getParameter();
			else if (name.equals("IsAllCurrencies"))
				p_IsAllCurrencies = "Y".equals((String)para[i].getParameter());
			else if (name.equals("C_Currency_ID"))
				p_C_Currency_ID = para[i].getParameterAsInt();
			else if (name.equals("C_DocTypeReval_ID"))
				p_C_DocTypeReval_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		if (p_IsAllCurrencies)
			p_C_Currency_ID = 0;
		log.info("C_AcctSchema_ID=" + p_C_AcctSchema_ID 
			+ ",C_ConversionTypeReval_ID=" + p_C_ConversionTypeReval_ID
			+ ",DateReval=" + p_DateReval 
			+ ", APAR=" + p_APAR
			+ ", IsAllCurrencies=" + p_IsAllCurrencies
			+ ",C_Currency_ID=" + p_C_Currency_ID
			+ ", C_DocType_ID=" + p_C_DocTypeReval_ID);
		
		//	Parameter
		if (p_DateReval == null)
			p_DateReval = new Timestamp(System.currentTimeMillis());
		
		//	Delete - just to be sure
		String sql = "DELETE T_InvoiceGL WHERE AD_PInstance_ID=" + getAD_PInstance_ID();
		int no = DB.executeUpdate(sql, get_TrxName());
		if (no > 0)
			log.info("Deleted #" + no);
		
		//	Insert Trx
		String dateStr = DB.TO_DATE(p_DateReval, true);
		sql = "INSERT INTO T_InvoiceGL (AD_Client_ID, AD_Org_ID, IsActive, Created,CreatedBy, Updated,UpdatedBy,"
			+ " AD_PInstance_ID, C_Invoice_ID, GrandTotal, OpenAmt, "
			+ " Fact_Acct_ID, AmtSourceBalance, AmtAcctBalance, "
			+ " AmtRevalDr, AmtRevalCr, C_DocTypeReval_ID, IsAllCurrencies, "
			+ " DateReval, C_ConversionTypeReval_ID, AmtRevalDrDiff, AmtRevalCrDiff, APAR) "
			//	--
			+ "SELECT i.AD_Client_ID, i.AD_Org_ID, i.IsActive, i.Created,i.CreatedBy, i.Updated,i.UpdatedBy,"
			+  getAD_PInstance_ID() + ", i.C_Invoice_ID, i.GrandTotal, invoiceOpen(i.C_Invoice_ID, 0), "
			+ " fa.Fact_Acct_ID, fa.AmtSourceDr-fa.AmtSourceCr, fa.AmtAcctDr-fa.AmtAcctCr, " 
			+ " currencyConvert(fa.AmtSourceDr, i.C_Currency_ID, a.C_Currency_ID, " + dateStr + ", " + p_C_ConversionTypeReval_ID + ", i.AD_Client_ID, i.AD_Org_ID),"
		    + " currencyConvert(fa.AmtSourceCr, i.C_Currency_ID, a.C_Currency_ID, " + dateStr + ", " + p_C_ConversionTypeReval_ID + ", i.AD_Client_ID, i.AD_Org_ID),"
		    + (p_C_DocTypeReval_ID==0 ? "NULL" : p_C_DocTypeReval_ID) + ", "
		    + (p_IsAllCurrencies ? "'Y'," : "'N',")
		    + dateStr + ", " + p_C_ConversionTypeReval_ID + ", 0, 0, '" + p_APAR + "' "
		    //
		    + "FROM C_Invoice i"
		    + " INNER JOIN Fact_Acct fa ON (fa.AD_Table_ID=318 AND fa.Record_ID=i.C_Invoice_ID"
		    	+ " AND (i.GrandTotal=fa.AmtSourceDr OR i.GrandTotal=fa.AmtSourceCr))"
		    + " INNER JOIN C_AcctSchema a ON (fa.C_AcctSchema_ID=a.C_AcctSchema_ID) "
		    + "WHERE i.IsPaid='N'"
		    + " AND EXISTS (SELECT * FROM C_ElementValue ev "
		    	+ "WHERE ev.C_ElementValue_ID=fa.Account_ID AND (ev.AccountType='A' OR ev.AccountType='L'))"
		    + " AND fa.C_AcctSchema_ID=" + p_C_AcctSchema_ID;
		if (!p_IsAllCurrencies)
			sql += " AND i.C_Currency_ID<>a.C_Currency_ID";
		if (ONLY_AR.equals(p_APAR))
			sql += " AND i.IsSOTrx='Y'";
		else if (ONLY_AP.equals(p_APAR))
			sql += " AND i.IsSOTrx='N'";
		if (!p_IsAllCurrencies && p_C_Currency_ID != 0)
			sql += " AND i.C_Currency_ID=" + p_C_Currency_ID;
		
		no = DB.executeUpdate(sql, get_TrxName());
		if (no != 0)
			log.info("Inserted #" + no);
		else if (CLogMgt.isLevelFiner())
			log.warning("Inserted #" + no + " - " + sql);
		else 
			log.warning("Inserted #" + no);

		//	Calculate Difference
		sql = "UPDATE T_InvoiceGL gl "
			+ "SET (AmtRevalDrDiff,AmtRevalCrDiff)="
				+ "(SELECT gl.AmtRevalDr-fa.AmtAcctDr, gl.AmtRevalCr-fa.AmtAcctCr "
				+ "FROM Fact_Acct fa "
				+ "WHERE gl.Fact_Acct_ID=fa.Fact_Acct_ID) "
			+ "WHERE AD_PInstance_ID=" + getAD_PInstance_ID();
		int noT = DB.executeUpdate(sql, get_TrxName());
		if (noT > 0)
			log.config("Difference #" + noT);
		
		//	Percentage
		sql = "UPDATE T_InvoiceGL SET Percent = 100 "
			+ "WHERE GrandTotal=OpenAmt AND AD_PInstance_ID=" + getAD_PInstance_ID();
		no = DB.executeUpdate(sql, get_TrxName());
		if (no > 0)
			log.info("Not Paid #" + no);

		sql = "UPDATE T_InvoiceGL SET Percent = ROUND(OpenAmt/GrandTotal*100,0) "
			+ "WHERE GrandTotal<>OpenAmt AND GrandTotal <> 0 AND AD_PInstance_ID=" + getAD_PInstance_ID();
		no = DB.executeUpdate(sql, get_TrxName());
		if (no > 0)
			log.info("Partial Paid #" + no);

		sql = "UPDATE T_InvoiceGL SET AmtRevalDr = AmtRevalDr * Percent,"
			+ " AmtRevalCr = AmtRevalCr * Percent/100,"
			+ " AmtRevalDrDiff = AmtRevalDrDiff * Percent/100,"
			+ " AmtRevalCrDiff = AmtRevalCrDiff * Percent/100 "
			+ "WHERE Percent <> 100 AND AD_PInstance_ID=" + getAD_PInstance_ID();
		no = DB.executeUpdate(sql, get_TrxName());
		if (no > 0)
			log.config("Partial Calc #" + no);
		
		//	Create Document
		String info = "";
		if (p_C_DocTypeReval_ID != 0)
		{
			if (p_C_Currency_ID != 0)
				log.warning("Can create Journal only for all currencies");
			else
				info = createGLJournal();
		}
		return "#" + noT + info;
	}	//	doIt

	/**
	 * 	Create GL Journal
	 * 	@return document info
	 */
	private String createGLJournal()
	{
		ArrayList<X_T_InvoiceGL> list = new ArrayList<X_T_InvoiceGL>();
		String sql = "SELECT * FROM T_InvoiceGL "
			+ "WHERE AD_PInstance_ID=" + getAD_PInstance_ID()
			+ " ORDER BY AD_Org_ID";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new X_T_InvoiceGL (getCtx(), rs, get_TrxName()));
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
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
		if (list.size() == 0)
			return " - No Records found";
		
		//
		MAcctSchema as = MAcctSchema.get(getCtx(), p_C_AcctSchema_ID);
		MAcctSchemaDefault asDefaultAccts = MAcctSchemaDefault.get(getCtx(), p_C_AcctSchema_ID);
		MGLCategory cat = MGLCategory.getDefaultSystem(getCtx());
		if (cat == null)
		{
			MDocType docType = MDocType.get(getCtx(), p_C_DocTypeReval_ID);
			cat = MGLCategory.get(getCtx(), docType.getGL_Category_ID());
		}
		//
		MJournalBatch batch = new MJournalBatch(getCtx(), 0, get_TrxName());
		batch.setDescription (getName());
		batch.setC_DocType_ID(p_C_DocTypeReval_ID);
		batch.setDateDoc(new Timestamp(System.currentTimeMillis()));
		batch.setDateAcct(p_DateReval);
		batch.setC_Currency_ID(as.getC_Currency_ID());
		if (!batch.save())
			return " - Could not create Batch";
		//
		MJournal journal = null;
		BigDecimal drTotal = Env.ZERO;
		BigDecimal crTotal = Env.ZERO;
		int AD_Org_ID = 0;
		for (int i = 0; i < list.size(); i++)
		{
			X_T_InvoiceGL gl = list.get(i);
			if (gl.getAmtRevalDrDiff().signum() == 0 && gl.getAmtRevalCrDiff().signum() == 0)
				continue;
			MInvoice invoice = new MInvoice(getCtx(), gl.getC_Invoice_ID(), null);
			if (invoice.getC_Currency_ID() == as.getC_Currency_ID())
				continue;
			//
			if (journal == null)
			{
				journal = new MJournal (batch);
				journal.setC_AcctSchema_ID (as.getC_AcctSchema_ID());
				journal.setC_Currency_ID(as.getC_Currency_ID());
				journal.setC_ConversionType_ID(p_C_ConversionTypeReval_ID);
				MOrg org = MOrg.get(getCtx(), gl.getAD_Org_ID());
				journal.setDescription (getName() + " - " + org.getName());
				journal.setGL_Category_ID (cat.getGL_Category_ID());
				if (!journal.save())
					return " - Could not create Journal";
			}
			//
			MJournalLine line = new MJournalLine(journal);
			line.setLine((i+1) * 10);
			line.setDescription(invoice.getSummary());
			//
			MFactAcct fa = new MFactAcct (getCtx(), gl.getFact_Acct_ID(), null);
			line.setC_ValidCombination_ID(MAccount.get(fa));
			BigDecimal dr = gl.getAmtRevalDrDiff();
			BigDecimal cr = gl.getAmtRevalCrDiff();
			drTotal = drTotal.add(dr);
			crTotal = crTotal.add(cr);
			line.setAmtSourceDr (dr);
			line.setAmtAcctDr (dr);
			line.setAmtSourceCr (cr);
			line.setAmtAcctCr (cr);
			line.save();
			//
			if (AD_Org_ID == 0)		//	invoice org id
				AD_Org_ID = gl.getAD_Org_ID();
			//	Change in Org
			if (AD_Org_ID != gl.getAD_Org_ID())
			{
				createBalancing (asDefaultAccts, journal, drTotal, crTotal, AD_Org_ID, (i+1) * 10);
				//
				AD_Org_ID = gl.getAD_Org_ID();
				drTotal = Env.ZERO;
				crTotal = Env.ZERO;
				journal = null;
			}
		}
		createBalancing (asDefaultAccts, journal, drTotal, crTotal, AD_Org_ID, (list.size()+1) * 10);
		
		return " - " + batch.getDocumentNo() + " #" + list.size();
	}	//	createGLJournal

	/**
	 * 	Create Balancing Entry
	 *	@param asDefaultAccts acct schema default accounts
	 *	@param journal journal
	 *	@param drTotal dr
	 *	@param crTotal cr
	 *	@param AD_Org_ID org
	 *	@param lineNo base line no
	 */
	private void createBalancing (MAcctSchemaDefault asDefaultAccts, MJournal journal, 
		BigDecimal drTotal, BigDecimal crTotal, int AD_Org_ID, int lineNo)
	{
		if (journal == null)
			throw new IllegalArgumentException("Jornal is null");
		//		CR Entry = Gain
		if (drTotal.signum() != 0)
		{
			MJournalLine line = new MJournalLine(journal);
			line.setLine(lineNo+1);
			MAccount base = MAccount.get(getCtx(), asDefaultAccts.getUnrealizedGain_Acct());
			MAccount acct = MAccount.get(getCtx(), asDefaultAccts.getAD_Client_ID(), AD_Org_ID, 
				asDefaultAccts.getC_AcctSchema_ID(), base.getAccount_ID(), base.getC_SubAcct_ID(),
				base.getM_Product_ID(), base.getC_BPartner_ID(), base.getAD_OrgTrx_ID(), 
				base.getC_LocFrom_ID(), base.getC_LocTo_ID(), base.getC_SalesRegion_ID(), 
				base.getC_Project_ID(), base.getC_Campaign_ID(), base.getC_Activity_ID(),
				base.getUser1_ID(), base.getUser2_ID(), base.getUserElement1_ID(), base.getUserElement2_ID());
			line.setDescription(Msg.getElement(getCtx(), "UnrealizedGain_Acct"));
			line.setC_ValidCombination_ID(acct.getC_ValidCombination_ID());
			line.setAmtSourceCr (drTotal);
			line.setAmtAcctCr (drTotal);
			line.save();
		}
		//	DR Entry = Loss
		if (crTotal.signum() != 0)
		{
			MJournalLine line = new MJournalLine(journal);
			line.setLine(lineNo+2);
			MAccount base = MAccount.get(getCtx(), asDefaultAccts.getUnrealizedLoss_Acct());
			MAccount acct = MAccount.get(getCtx(), asDefaultAccts.getAD_Client_ID(), AD_Org_ID, 
				asDefaultAccts.getC_AcctSchema_ID(), base.getAccount_ID(), base.getC_SubAcct_ID(),
				base.getM_Product_ID(), base.getC_BPartner_ID(), base.getAD_OrgTrx_ID(), 
				base.getC_LocFrom_ID(), base.getC_LocTo_ID(), base.getC_SalesRegion_ID(), 
				base.getC_Project_ID(), base.getC_Campaign_ID(), base.getC_Activity_ID(),
				base.getUser1_ID(), base.getUser2_ID(), base.getUserElement1_ID(), base.getUserElement2_ID());
			line.setDescription(Msg.getElement(getCtx(), "UnrealizedLoss_Acct"));
			line.setC_ValidCombination_ID(acct.getC_ValidCombination_ID());
			line.setAmtSourceDr (crTotal);
			line.setAmtAcctDr (crTotal);
			line.save();
		}
	}	//	createBalancing

}	//	InvoiceNGL
