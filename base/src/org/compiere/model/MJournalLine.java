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
import org.compiere.util.*;

/**
 *  Journal Line Model
 *
 *	@author Jorg Janke
 *	@version $Id: MJournalLine.java,v 1.16 2005/12/19 01:16:46 jjanke Exp $
 */
public class MJournalLine extends X_GL_JournalLine
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param GL_JournalLine_ID id
	 */
	public MJournalLine (Properties ctx, int GL_JournalLine_ID, String trxName)
	{
		super (ctx, GL_JournalLine_ID, trxName);
		if (GL_JournalLine_ID == 0)
		{
		//	setGL_JournalLine_ID (0);		//	PK
		//	setGL_Journal_ID (0);			//	Parent
		//	setC_Currency_ID (0);
		//	setC_ValidCombination_ID (0);
			setLine (0);
			setAmtAcctCr (Env.ZERO);
			setAmtAcctDr (Env.ZERO);
			setAmtSourceCr (Env.ZERO);
			setAmtSourceDr (Env.ZERO);
			setCurrencyRate (Env.ONE);
		//	setC_ConversionType_ID (0);
			setDateAcct (new Timestamp(System.currentTimeMillis()));
			setIsGenerated (true);
		}
	}	//	MJournalLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MJournalLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MJournalLine

	/**
	 * 	Parent Constructor
	 *	@param parent journal
	 */
	public MJournalLine (MJournal parent)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setGL_Journal_ID(parent.getGL_Journal_ID());
		setC_Currency_ID(parent.getC_Currency_ID());
		setC_ConversionType_ID(parent.getC_ConversionType_ID());
		setDateAcct(parent.getDateAcct());
		
	}	//	MJournalLine

	/**	Currency Precision		*/
	private int					m_precision = 6;
	/**	Account Combination		*/
	private MAccount		 	m_account = null;
	/** Account Element			*/
	private MElementValue		m_accountElement = null;
	
	/**
	 * 	Set Currency Info
	 *	@param C_Currency_ID currenct
	 *	@param C_ConversionType_ID type
	 *	@param CurrencyRate rate
	 */
	public void setCurrency (int C_Currency_ID, int C_ConversionType_ID, BigDecimal CurrencyRate)
	{
		setC_Currency_ID(C_Currency_ID);
		if (C_ConversionType_ID != 0)
			setC_ConversionType_ID(C_ConversionType_ID);
		if (CurrencyRate != null && CurrencyRate.signum() == 0)
			setCurrencyRate(CurrencyRate);
	}	//	setCurrency

	/**
	 * 	Set C_Currency_ID and precision
	 *	@param C_Currency_ID currency
	 */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID == 0)
			return;
		super.setC_Currency_ID (C_Currency_ID);
		m_precision = MCurrency.getStdPrecision(getCtx(), C_Currency_ID);
	}	//	setC_Currency_ID
	
	/**
	 * 	Get Currency Precision
	 *	@return precision
	 */
	public int getPrecision()
	{
		return m_precision;
	}	//


        /**
	 * 	Set Currency Precision
	 *	@return precision
	 */
	public void setPrecision(int precision)
	{
            m_precision = precision;
	}
	
	/**
	 * 	Set Currency Rate
	 *	@param CurrencyRate check for null (->one)
	 */
	public void setCurrencyRate (BigDecimal CurrencyRate)
	{
		if (CurrencyRate == null)
		{
			log.warning("was NULL - set to 1");
			super.setCurrencyRate (Env.ONE);
		}
		else if (CurrencyRate.signum() < 0)
		{
			log.warning("negative - " + CurrencyRate + " - set to 1");
			super.setCurrencyRate (Env.ONE);
		}
		else
			super.setCurrencyRate (CurrencyRate);
	}	//	setCurrencyRate
	
	/**
	 * 	Set Accounted Amounts only if not 0.
	 * 	Amounts overwritten in beforeSave - set conversion rate
	 *	@param AmtAcctDr Dr
	 *	@param AmtAcctCr Cr
	 */
	public void setAmtAcct (BigDecimal AmtAcctDr, BigDecimal AmtAcctCr)
	{
		//	setConversion
		double rateDR = 0;
		if (AmtAcctDr != null && AmtAcctDr.signum() != 0)
		{
			rateDR = AmtAcctDr.doubleValue() / getAmtSourceDr().doubleValue();
			super.setAmtAcctDr(AmtAcctDr);
		}
		double rateCR = 0;
		if (AmtAcctCr != null && AmtAcctCr.signum() != 0)
		{
			rateCR = AmtAcctCr.doubleValue() / getAmtSourceCr().doubleValue();
			super.setAmtAcctCr(AmtAcctCr);
		}
		if (rateDR != 0 && rateCR != 0 && rateDR != rateCR)
		{
			log.warning("Rates Different DR=" + rateDR + "(used) <> CR=" + rateCR + "(ignored)");
			rateCR = 0;
		}
		if (rateDR < 0 || Double.isInfinite(rateDR) || Double.isNaN(rateDR))
		{
			log.warning("DR Rate ignored - " + rateDR);
			return;
		}
		if (rateCR < 0 || Double.isInfinite(rateCR) || Double.isNaN(rateCR))
		{
			log.warning("CR Rate ignored - " + rateCR);
			return;
		}
		
		if (rateDR != 0)
			setCurrencyRate(new BigDecimal(rateDR));
		if (rateCR != 0)
			setCurrencyRate(new BigDecimal(rateCR));
	}	//	setAmtAcct

	
	/**
	 * 	Set C_ValidCombination_ID
	 *	@param C_ValidCombination_ID id
	 */
	public void setC_ElementValue_ID (int C_ElementValue_ID)
	{
		super.setC_ElementValue_ID (C_ElementValue_ID);
		m_account = null;
		m_accountElement = null;
	}	//	setC_ValidCombination_ID
	
	/**
	 * 	Set C_ValidCombination_ID
	 *	@param acct account
	 */
	public void setC_ValidCombination_ID (MAccount acct)
	{
		if (acct == null)
			throw new IllegalArgumentException("Account is null");
		
		//super.setC_ValidCombination_ID (acct.getC_ValidCombination_ID());
		
		m_account = acct;
		m_accountElement = null;
	}	//	setC_ValidCombination_ID

	/**
	 * 	Get Account (Valid Combination)
	 *	@return combination or null
	 */
	public MAccount getAccount()
	{
		if (m_account == null && getC_ElementValue_ID() != 0)
		{
			int validCombination_ID = 0;
			
			try	{
				String sql = " SELECT cb.C_VALIDCOMBINATION_ID " +
							 " FROM C_VALIDCOMBINATION cb, GL_JournalLine jl " +
				  			 " WHERE cb.account_ID=jl.C_ElementValue_ID AND jl.C_ElementValue_ID=? " +
				  			 "   AND cb.C_AcctSchema_ID=?";
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt (1, getC_ElementValue_ID());
				MJournal journal = new MJournal(getCtx(), getGL_Journal_ID(), get_TrxName());
				pstmt.setInt (2, journal.getC_AcctSchema_ID());
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					validCombination_ID = rs.getInt(1);
				
				rs.close();
				pstmt.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			m_account = new MAccount (getCtx(), validCombination_ID, get_TrxName());
		}
			
		return m_account;
	}	//	getValidCombination
	
	/**
	 * 	Get Natural Account Element Value
	 *	@return account
	 */
	public MElementValue getAccountElementValue()
	{
		if (m_accountElement == null)
		{
			MAccount vc = getAccount();
			if (vc != null && vc.getAccount_ID() != 0)
				m_accountElement = new MElementValue (getCtx(), vc.getAccount_ID(), get_TrxName()); 
		}
		return m_accountElement;
	}	//	getAccountElement
	
	/**
	 * 	Is it posting to a Control Acct
	 *	@return true if control acct
	 */
	public boolean isDocControlled()
	{
		MElementValue acct = getAccountElementValue();
		if (acct == null)
		{
			log.warning ("Account not found for C_ElementValue_ID=" + getC_ElementValue_ID());
			return false;
		}
		return acct.isDocControlled();
	}	//	isDocControlled
	
	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true 
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Acct Amts
		BigDecimal rate = getCurrencyRate();
		BigDecimal amt = rate.multiply(getAmtSourceDr());
		if (amt.scale() > getPrecision())
			amt = amt.setScale(getPrecision(), BigDecimal.ROUND_HALF_UP);
		setAmtAcctDr(amt);
		amt = rate.multiply(getAmtSourceCr());
		if (amt.scale() > getPrecision())
			amt = amt.setScale(getPrecision(), BigDecimal.ROUND_HALF_UP);
		setAmtAcctCr(amt);
		//	Set Line Org to Acct Org
		if (newRecord 
			|| is_ValueChanged("C_ValidCombination_ID")
			|| is_ValueChanged("AD_Org_ID"))
			setAD_Org_ID(getAccount().getAD_Org_ID());
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save.
	 * 	Update Journal/Batch Total
	 *	@param newRecord true if new record
	 *	@param success true if success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		return updateJournalTotal();
	}	//	afterSave
	
	
	/**
	 * 	After Delete
	 *	@param success true if deleted
	 *	@return true if success
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		return updateJournalTotal();
	}	//	afterDelete

	
	/**
	 * 	Update Journal and Batch Total
	 *	@return true if success
	 */
	private boolean updateJournalTotal()
	{
		//	Update Journal Total
		String sql = "UPDATE GL_Journal j"
			+ " SET (TotalDr, TotalCr) = (SELECT COALESCE(SUM(AmtSourceDr),0), COALESCE(SUM(AmtSourceCr),0)"
				+ " FROM GL_JournalLine jl WHERE jl.IsActive='Y' AND j.GL_Journal_ID=jl.GL_Journal_ID) "
			+ "WHERE GL_Journal_ID=" + getGL_Journal_ID();
		int no = DB.executeUpdate(sql, get_TrxName());
		if (no != 1)
			log.warning("afterSave - Update Journal #" + no);
		
		//	Update Batch Total
		sql = "UPDATE GL_JournalBatch jb"
			+ " SET (TotalDr, TotalCr) = (SELECT COALESCE(SUM(TotalDr),0), COALESCE(SUM(TotalCr),0)"
				+ " FROM GL_Journal j WHERE jb.GL_JournalBatch_ID=j.GL_JournalBatch_ID) "
			+ "WHERE GL_JournalBatch_ID="
				+ "(SELECT DISTINCT GL_JournalBatch_ID FROM GL_Journal WHERE GL_Journal_ID=" 
				+ getGL_Journal_ID() + ")";
		no = DB.executeUpdate(sql, get_TrxName());
		if (no != 1)
			log.warning("Update Batch #" + no);
		return no == 1;
	}	//	updateJournalTotal
	
	
	public void setC_ValidCombination_ID(int C_ValidCombination_ID)
	{
		try{
			String query = "Select Account_ID from C_ValidCombination WHERE C_ValidCombination_ID = " + C_ValidCombination_ID;
		
			PreparedStatement pst = DB.prepareStatement(query, null);
			ResultSet rst = pst.executeQuery();
			if (rst.next())
				setC_ElementValue_ID(rst.getInt(1));
			else
				setC_ElementValue_ID(0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}	//	MJournalLine
