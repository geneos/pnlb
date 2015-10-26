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

import java.io.File;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.compiere.db.CConnection;
import org.compiere.interfaces.Server;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessCall;
import org.compiere.process.ProcessInfo;
import org.compiere.util.*;

/**
 *  Payment Model.
 *  - retrieve and create payments for invoice
 *  <pre>
 *  Event chain
 *  - Payment inserted
 *      C_Payment_Trg fires
 *          update DocumentNo with payment summary
 *  - Payment posted (C_Payment_Post)
 *      create allocation line
 *          C_Allocation_Trg fires
 *              Update C_BPartner Open Item Amount
 *      update invoice (IsPaid)
 *      link invoice-payment if batch
 *
 *  Lifeline:
 *  -   Created by VPayment or directly
 *  -   When changed in VPayment
 *      - old payment is reversed
 *      - new payment created
 *
 *  When Payment is posed, the Allocation is made
 *  </pre>
 *  @author 	Jorg Janke
 *  @version 	$Id: MPayment.java,v 1.87 2005/12/27 06:17:56 jjanke Exp $
 */

// Begin e-Evolution 20Abril2006
    /*public final class MPayment extends X_C_Payment
	implements DocAction, ProcessCall*/
public class MPayment extends X_C_Payment
	implements DocAction, ProcessCall
// End e-Evolution 20Abril2006
{
	/**
	 * 	Get Payments Of BPartner
	 *	@param ctx context
	 *	@param C_BPartner_ID id
	 *	@return array
	 */
	public static MPayment[] getOfBPartner (Properties ctx, int C_BPartner_ID, String trxName)
	{
		ArrayList<MPayment> list = new ArrayList<MPayment>();
		String sql = "SELECT * FROM C_Payment WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MPayment(ctx,rs, trxName));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}

		//
		MPayment[] retValue = new MPayment[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfBPartner


	/**************************************************************************
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  C_Payment_ID    payment to load, (0 create new payment)
	 *  @param trxName trx name
	 */
	public MPayment (Properties ctx, int C_Payment_ID, String trxName)
	{
		super (ctx, C_Payment_ID, trxName);
		//  New
		if (C_Payment_ID == 0)
		{
			setDocAction(DOCACTION_Complete);
			setDocStatus(DOCSTATUS_Drafted);
			setTrxType(TRXTYPE_Sales);
			//
			setR_AvsAddr (R_AVSZIP_Unavailable);
			setR_AvsZip (R_AVSZIP_Unavailable);
			//
			setIsReceipt (true);
			setIsApproved (false);
			setIsReconciled (false);
			setIsAllocated(false);
			setIsOnline (false);
			setIsSelfService(false);
			setIsDelayedCapture (false);
			setIsPrepayment(false);
			setProcessed(false);
			setProcessing(false);
			setPosted (false);
			//
			setPayAmt(Env.ZERO);
			setDiscountAmt(Env.ZERO);
			setTaxAmt(Env.ZERO);
			setWriteOffAmt(Env.ZERO);
			setIsOverUnderPayment (false);
			setOverUnderAmt(Env.ZERO);
			//
			setDateTrx (new Timestamp(System.currentTimeMillis()));
			setDateAcct (getDateTrx());
			setTenderType(TENDERTYPE_Check);
		}
	}   //  MPayment

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MPayment (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPayment

	/**	Temporary	Payment Processors		*/
	private MPaymentProcessor[]	m_mPaymentProcessors = null;
	/**	Temporary	Payment Processor		*/
	private MPaymentProcessor	m_mPaymentProcessor = null;
	/** Logger								*/
	private static CLogger		s_log = CLogger.getCLogger (MPayment.class);
	/** Error Message						*/
	private String				m_errorMessage = null;

	/** Reversal Indicator			*/
	public static String	REVERSE_INDICATOR = "^";
	//TODO PASAR A CONSTANTE
	/**	DocBaseType		*/
	public static String	COBRO = "ARR";
	public static String	PAGO = "APP";
	public static String	CERTIFICADO = "CRR";
	private static String	NOAPLICA = "No Aplica";
	//private static String	PAGO = "APP";

	public boolean retencion = true;


        // VIT4B ... agregado para manejar los save de retenciones

        public boolean flagSave = false;

	/**
	 *  Reset Payment to new status
	 */
	public void resetNew()
	{
		setC_Payment_ID(0);		//	forces new Record
		set_ValueNoCheck ("DocumentNo", null);
		setDocAction(DOCACTION_Prepare);
		setDocStatus(DOCSTATUS_Drafted);
		setProcessed(false);
		setPosted (false);
		setIsReconciled (false);
		setIsAllocated(false);
		setIsOnline(false);
		setIsDelayedCapture (false);
	//	setC_BPartner_ID(0);
		setC_Invoice_ID(0);
		setC_Order_ID(0);
		setC_Charge_ID(0);
		setC_Project_ID(0);
		setIsPrepayment(false);
	}	//	resetNew

	/**
	 * 	Is Cashbook Transfer Trx
	 *	@return true if Cash Trx
	 */
	public boolean isCashTrx()
	{
		return "X".equals(getTenderType());
	}	//	isCashTrx

	/**************************************************************************
	 *  Set Credit Card.
	 *  Need to set PatmentProcessor after Amount/Currency Set
	 *
	 *  @param TrxType Transaction Type see TRX_
	 *  @param creditCardType CC type
	 *  @param creditCardNumber CC number
	 *  @param creditCardVV CC verification
	 *  @param creditCardExpMM CC Exp MM
	 *  @param creditCardExpYY CC Exp YY
	 *  @return true if valid
	 */
	public boolean setCreditCard (String TrxType, String creditCardType, String creditCardNumber,
		String creditCardVV, int creditCardExpMM, int creditCardExpYY)
	{
		setTenderType(TENDERTYPE_CreditCard);
		setTrxType(TrxType);
		//
		setCreditCardType (creditCardType);
		setCreditCardNumber (creditCardNumber);
		setCreditCardVV (creditCardVV);
		setCreditCardExpMM (creditCardExpMM);
		setCreditCardExpYY (creditCardExpYY);
		//
		int check = MPaymentValidate.validateCreditCardNumber(creditCardNumber, creditCardType).length()
			+ MPaymentValidate.validateCreditCardExp(creditCardExpMM, creditCardExpYY).length();
		if (creditCardVV.length() > 0)
			check += MPaymentValidate.validateCreditCardVV(creditCardVV, creditCardType).length();
		return check == 0;
	}   //  setCreditCard

	/**
	 *  Set Credit Card - Exp.
	 *  Need to set PatmentProcessor after Amount/Currency Set
	 *
	 *  @param TrxType Transaction Type see TRX_
	 *  @param creditCardType CC type
	 *  @param creditCardNumber CC number
	 *  @param creditCardVV CC verification
	 *  @param creditCardExp CC Exp
	 *  @return true if valid
	 */
	public boolean setCreditCard (String TrxType, String creditCardType, String creditCardNumber,
		String creditCardVV, String creditCardExp)
	{
		return setCreditCard(TrxType, creditCardType, creditCardNumber,
			creditCardVV, MPaymentValidate.getCreditCardExpMM(creditCardExp),
			MPaymentValidate.getCreditCardExpYY(creditCardExp));
	}   //  setCreditCard

	/**
	 *  Set ACH BankAccount Info
	 *
	 *  @param C_BankAccount_ID bank account
	 *  @param isReceipt true if receipt
	 *  @return true if valid
	 */
	public boolean setBankACH (int C_BankAccount_ID, boolean isReceipt)
	{
		setBankAccountDetails(C_BankAccount_ID);
		setIsReceipt (isReceipt);
		//
		int check = MPaymentValidate.validateRoutingNo(getRoutingNo()).length()
			+ MPaymentValidate.validateAccountNo(getAccountNo()).length();
		return check == 0;
	}	//	setBankACH

	/**
	 *  Set ACH BankAccount Info
	 *
	 *  @param C_BankAccount_ID bank account
	 *  @param isReceipt true if receipt
	 * 	@param tenderType - Direct Debit or Direct Deposit
	 *  @param routingNo routing
	 *  @param accountNo account
	 *  @return true if valid
	 */
	public boolean setBankACH (int C_BankAccount_ID, boolean isReceipt, String tenderType,
		String routingNo, String accountNo)
	{
		setTenderType (tenderType);
		setIsReceipt (isReceipt);
		//
		if (C_BankAccount_ID > 0
			&& (routingNo == null || routingNo.length() == 0 || accountNo == null || accountNo.length() == 0))
			setBankAccountDetails(C_BankAccount_ID);
		else
		{
			setC_BankAccount_ID(C_BankAccount_ID);
			setRoutingNo (routingNo);
			setAccountNo (accountNo);
		}
		setCheckNo ("");
		//
		int check = MPaymentValidate.validateRoutingNo(routingNo).length()
			+ MPaymentValidate.validateAccountNo(accountNo).length();
		return check == 0;
	}   //  setBankACH

	/**
	 *  Set Check BankAccount Info
	 *
	 *  @param C_BankAccount_ID bank account
	 *  @param isReceipt true if receipt
	 *  @param checkNo chack no
	 *  @return true if valid
	 */
	public boolean setBankCheck (int C_BankAccount_ID, boolean isReceipt, String checkNo)
	{
		return setBankCheck (C_BankAccount_ID, isReceipt, null, null, checkNo);
	}	//	setBankCheck

	/**
	 *  Set Check BankAccount Info
	 *
	 *  @param C_BankAccount_ID bank account
	 *  @param isReceipt true if receipt
	 *  @param routingNo routing no
	 *  @param accountNo account no
	 *  @param checkNo chack no
	 *  @return true if valid
	 */

	public boolean setBankCheck (int C_BankAccount_ID, boolean isReceipt,
		String routingNo, String accountNo, String checkNo)
	{
		setTenderType (TENDERTYPE_Check);
		setIsReceipt (isReceipt);
		//
		if (C_BankAccount_ID > 0
			&& (routingNo == null || routingNo.length() == 0
				|| accountNo == null || accountNo.length() == 0))
			setBankAccountDetails(C_BankAccount_ID);
		else
		{
			setC_BankAccount_ID(C_BankAccount_ID);
			setRoutingNo (routingNo);
			setAccountNo (accountNo);
		}
		setCheckNo (checkNo);
		//
		int check = MPaymentValidate.validateRoutingNo(routingNo).length()
			+ MPaymentValidate.validateAccountNo(accountNo).length()
			+ MPaymentValidate.validateCheckNo(checkNo).length();
		return check == 0;       //  no error message
	}   //  setBankCheck

	/**
	 * 	Set Bank Account Details.
	 * 	Look up Routing No & Bank Acct No
	 * 	@param C_BankAccount_ID bank account
	 */
	public void setBankAccountDetails (int C_BankAccount_ID)
	{
		if (C_BankAccount_ID == 0)
			return;
		setC_BankAccount_ID(C_BankAccount_ID);
		//
		String sql = "SELECT b.RoutingNo, ba.AccountNo "
			+ "FROM C_BankAccount ba"
			+ " INNER JOIN C_Bank b ON (ba.C_Bank_ID=b.C_Bank_ID) "
			+ "WHERE C_BankAccount_ID=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, C_BankAccount_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				setRoutingNo (rs.getString(1));
				setAccountNo (rs.getString(2));
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "setsetBankAccountDetails", e);
		}
	}	//	setBankAccountDetails

	/**
	 *  Set Account Address
	 *
	 *  @param name name
	 *  @param street street
	 *  @param city city
	 *  @param state state
	 *  @param zip zip
	 * 	@param country country
	 */
	public void setAccountAddress (String name, String street,
		String city, String state, String zip, String country)
	{
		setA_Name (name);
		setA_Street (street);
		setA_City (city);
		setA_State (state);
		setA_Zip (zip);
		setA_Country(country);
	}   //  setAccountAddress


	/**************************************************************************
	 *  Process Payment
	 *  @return true if approved
	 */
	public boolean processOnline()
	{
		log.info ("Amt=" + getPayAmt());
		//
		setIsOnline(true);
		setErrorMessage(null);
		//	prevent charging twice
		if (isApproved())
		{
			log.info("Already processed - " + getR_Result() + " - " + getR_RespMsg());
			setErrorMessage("Payment already Processed");
			return true;
		}

		if (m_mPaymentProcessor == null)
			setPaymentProcessor();
		if (m_mPaymentProcessor == null)
		{
			log.log(Level.SEVERE, "No Payment Processor Model");
			setErrorMessage("No Payment Processor Model");
			return false;
		}

		boolean approved = false;
		/**	Process Payment on Server	*/
		if (DB.isRemoteObjects())
		{
			Server server = CConnection.get().getServer();
			try
			{
				if (server != null)
				{	//	See ServerBean
					String trxName = null;	//	unconditionally save
					save(trxName);	//	server reads from disk
					approved = server.paymentOnline (getCtx(), getC_Payment_ID(),
						m_mPaymentProcessor.getC_PaymentProcessor_ID(), trxName);
					if (CLogMgt.isLevelFinest())
						s_log.fine("processOnline - server => " + approved);
					load(trxName);	//	server saves to disk
					setIsApproved(approved);
					return approved;
				}
				log.log(Level.SEVERE, "processOnline - AppsServer not found");
			}
			catch (RemoteException ex)
			{
				log.log(Level.SEVERE, "processOnline - AppsServer error", ex);
			}
		}
		/** **/

		//	Try locally
		try
		{
			PaymentProcessor pp = PaymentProcessor.create(m_mPaymentProcessor, this);
			if (pp == null)
				setErrorMessage("No Payment Processor");
			else
			{
				approved = pp.processCC ();
				if (approved)
					setErrorMessage(null);
				else
					setErrorMessage("From " +  getCreditCardName() + ": " + getR_RespMsg());
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "processOnline", e);
			setErrorMessage("Payment Processor Error");
		}
		setIsApproved(approved);
		return approved;
	}   //  processOnline

	/**
	 *  Process Online Payment.
	 *  implements ProcessCall after standard constructor
	 *  Called when pressing the Process_Online button in C_Payment
	 *
	 *  @param ctx          Context
	 *  @param pi			Process Info
	 *  @return true if the next process should be performed
	 */
	public boolean startProcess (Properties ctx, ProcessInfo pi, Trx trx)
	{
		log.info("startProcess - " + pi.getRecord_ID());
		boolean retValue = false;
		//
		if (pi.getRecord_ID() != get_ID())
		{
			log.log(Level.SEVERE, "startProcess - Not same Payment - " + pi.getRecord_ID());
			return false;
		}
		//  Process it
		retValue = processOnline();
		save();
		return retValue;    //  Payment processed
	}   //  startProcess

	/*
        **  Agregado para controlar las retenciones
        **  VIT4B - 13/12/2006
        */


        protected boolean afterSave (boolean newRecord, boolean success)
	{
            /**
            *  26/07/2013 Maria Jesus Martin
            *  Modificación realizada para que si la OP esta en borrador o en proceso se calculen nuevamente
            *  las retenciones.
            */
            
            if(!this.isReceipt()&& (super.isRetenciones()) && ((DOCSTATUS_Drafted.equals(getDocStatus())) || (DOCSTATUS_InProgress.equals(getDocStatus())))) {
                System.out.println("Entrando a evaluar el calcular retenciones");
                
                if(this.flagSave == false && this.retencion == true){
                    System.out.println("Entrando a calcular retenciones");
                    deleteRetenciones();
                    Retenciones.inicio(this.getC_Payment_ID(),this, get_TrxName());
                }  
            }
            return true;
  
	}	//	afterSave

        public void recalcularRetenciones(){        
            deleteRetenciones();
            Retenciones.inicio(this.getC_Payment_ID(),this, get_TrxName());
        }
        /*
         *  12/04/2013 
         *  Modificacion para que el pago y la cobranza no se pueda eliminar si no
         *  esta en estado borrador.
         * 
         */
        protected boolean beforeDelete ()
	{
            if (DOCSTATUS_Drafted.equals(getDocStatus()))
                return true;
            else
            {
                JOptionPane.showMessageDialog(null,"El documento no se puede eliminar ya que no esta en Estado Borrador.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
        }

    /**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord)
	{

            if (newRecord)
            {
                MDocType dt = new MDocType(getCtx(),getC_DocType_ID(),get_TrxName());

                if (getC_DocType_ID()!=0)
                {
                    if (dt.getDocBaseType().equals(COBRO) || dt.getDocBaseType().equals(CERTIFICADO))
                        setIsReceipt(true);
                    else
                        setIsReceipt(false);
                }
            }

            if (DOCSTATUS_Drafted.equals(getDocStatus())) 
            {
                if (isReceipt() && getC_DocType_ID()!=0)
                {
                    String documentNo = this.getDocumentNo();

                    if(documentNo != null && !documentNo.equals(""))
                    {
                        /*
                         *      Si el numero es definido por el sistema automaticamente le asigna <>
                         *      en este caso se debe reformatear para anexarle los ceros y aumentar
                         *      el numerador a mano ya que el sistema pierde la referencia automática.
                         */

                        if(documentNo.indexOf("<") != -1)
                        {
                            documentNo = documentNo.substring(1,documentNo.length()-1);
                            MDocType docType = MDocType.get(getCtx(), this.getC_DocType_ID());
                            MSequence seq = new MSequence(getCtx(),docType.getDocNoSequence_ID(), null);
                            int next = seq.getCurrentNext();
                            seq.setCurrentNext(next + 1);
                            seq.save(get_TrxName());

                        }

                        int indexOf = documentNo.indexOf("-");

                        String prefijo = "";
                        String nro = "";

                        if(indexOf == -1)
                        {
                            MDocType docType = MDocType.get(getCtx(), this.getC_DocType_ID());
                            MSequence seq = new MSequence(getCtx(),docType.getDocNoSequence_ID(), null);
                            prefijo = seq.getPrefix();
                            nro = documentNo;

                        }
                        else
                        {
                            prefijo = this.getDocumentNo().substring(0,indexOf);
                            nro = this.getDocumentNo().substring(indexOf+1,this.getDocumentNo().length());
                        }

                        if(nro == "" || nro == null)
                        {
                            JOptionPane.showMessageDialog(null,"Número de Documento Inválido", "Error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                        if(prefijo == "" || prefijo == null)
                        {
                            JOptionPane.showMessageDialog(null,"Número de Documento Inválido", "Error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        
                        switch (prefijo.length()) 
                        {
                          case 1:
                                prefijo = "000" + prefijo;
                                break;
                          case 2:
                                prefijo = "00" + prefijo;
                                break;
                          case 3:
                                prefijo = "0" + prefijo;
                                break;
                          case 4:
                                break;

                          default:
                                JOptionPane.showMessageDialog(null,"Número de Documento Inválido", "Error", JOptionPane.ERROR_MESSAGE);
                                return false;
                        }

                        switch (nro.length()) 
                        {
                          case 1:
                                nro = "0000000" + nro;
                                break;
                          case 2:
                                nro = "000000" + nro;
                                break;
                          case 3:
                                nro = "00000" + nro;
                                break;
                          case 4:
                                nro = "0000" + nro;
                                break;
                          case 5:
                                nro = "000" + nro;
                                break;
                          case 6:
                                nro = "00" + nro;
                                break;
                          case 7:
                                nro = "0" + nro;
                                break;
                          case 8:
                                break;
                          default:
                                JOptionPane.showMessageDialog(null,"Número de Documento Inválido", "Error", JOptionPane.ERROR_MESSAGE);
                                return false;
                        }

                        String val = prefijo + "-" + nro;

                        if (ValueFormat.validFormat(val,"0000-00000000"))
                            this.setDocumentNo(val);
                        else
                        {
                            JOptionPane.showMessageDialog(null,"Número de Documento Inválido", "Error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null,"No ingresó Número de Documento", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }
                }

                //	We have a charge
                if (getC_Charge_ID() != 0)
                {
                    if (newRecord || is_ValueChanged("C_Charge_ID"))
                    {
                        setC_Order_ID(0);
                        setC_Invoice_ID(0);
                        setWriteOffAmt(Env.ZERO);
                        setDiscountAmt(Env.ZERO);
                        setIsOverUnderPayment(false);
                        setOverUnderAmt(Env.ZERO);
                        setIsPrepayment(false);
                    }
                }
                //	We need a BPartner
                else if (getC_BPartner_ID() == 0 && !isCashTrx())
                {
                    if (getC_Invoice_ID() != 0)
                        ;
                    else if (getC_Order_ID() != 0)
                        ;
                    else
                    {
                        log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@: @C_BPartner_ID@"));
                        return false;
                    }
                }
            }
            //	Prepayment: No charge and order or project (not as acct dimension)
            if (newRecord
                    || is_ValueChanged("C_Charge_ID") || is_ValueChanged("C_Invoice_ID")
                    || is_ValueChanged("C_Order_ID") || is_ValueChanged("C_Project_ID"))
                setIsPrepayment (getC_Charge_ID() == 0
                            && getC_BPartner_ID() != 0
                            && (getC_Order_ID() != 0
                                    || (getC_Project_ID() != 0 && getC_Invoice_ID() == 0)));
            if (isPrepayment())
            {
                if (newRecord || is_ValueChanged("C_Order_ID") || is_ValueChanged("C_Project_ID"))
                {
                    setWriteOffAmt(Env.ZERO);
                    setDiscountAmt(Env.ZERO);
                    setIsOverUnderPayment(false);
                    setOverUnderAmt(Env.ZERO);
                }
            }

            //	Document Type/Receipt
            if (getC_DocType_ID() == 0)
                setC_DocType_ID();
            else
            {
                MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
                setIsReceipt(dt.isSOTrx());
            }
            setDocumentNo();
            //
            if (getDateAcct() == null)
                setDateAcct(getDateTrx());

            if (!isOverUnderPayment())
                    setOverUnderAmt(Env.ZERO);

            /* 02/05/2013 -  GENEOS - Pablo Velazquez.
             * Se agrega que algunas validaciones se hagan solo cuando el documento
             * esta en BORRADOR.
             */


            if (DOCSTATUS_Drafted.equals(getDocStatus()))
            {
                if (getC_BPartner_ID()!=0 && !isReceipt())
                {
                    MBPartner bPartner = new MBPartner(getCtx(),getC_BPartner_ID(),null);

                    if (!bPartner.isEXENCIONGANANCIAS()) {
                        if ((getC_REGIMENGANANCIAS_V_ID() == null) || (getC_REGIMENGANANCIAS_V_ID().equals("No Aplica"))){
                            JOptionPane.showMessageDialog(null,"Debe ingresar un RÃ©gimen, el cual se aplicarÃ¡ a las retenciones de Ganancias","Error - Falta ParÃ¡metro", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                }

                // Si Las monedas son distintas, requiere cotización
                if (isForeingCurrency())
                {	
                    if (getCotizacion().equals(Env.ZERO))
                    {
                        JOptionPane.showMessageDialog(null,"Debe ingresar Cotización, se encuentra trabajando con Moneda Extranjera","Error - Falta Parámetro", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                else
                // Si las monedas son iguales, la cotización es 1
                    setCotizacion(Env.ONE);
            }
            /*
             *  28/08/2013 Maria Jesus
             *  Verificacion de la existencia de la direccion de un socio de negocio.
             */
            MBPartnerLocation bPartnerLocation = new MBPartnerLocation(getCtx(),getC_BPartner_ID(),null);
            if (bPartnerLocation == null){
                JOptionPane.showMessageDialog(null,"El Socio de Negocio no tiene Localización. Por favor agregue estos datos en la Ventana del Socio de Negocio.","Error - Socio de Negocio", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
	}	//	beforeSave

	/**
	 * 	Get Allocated Amt in Payment Currency
	 *	@return amount or null
	 */
	public BigDecimal getAllocatedAmt ()
	{
		BigDecimal retValue = BigDecimal.ZERO;
		if (getC_Charge_ID() != 0)
			return getPayAmt();
		//
		String sql = "SELECT SUM(currencyConvert(al.Amount,"
				+ "ah.C_Currency_ID, p.C_Currency_ID,ah.DateTrx,p.C_ConversionType_ID, al.AD_Client_ID,al.AD_Org_ID)) "
			+ "FROM C_AllocationLine al"
			+ " INNER JOIN C_AllocationHdr ah ON (al.C_AllocationHdr_ID=ah.C_AllocationHdr_ID) "
			+ " INNER JOIN C_Payment p ON (al.C_Payment_ID=p.C_Payment_ID) "
			+ "WHERE al.C_Payment_ID=?"
			+ " AND ah.IsActive='Y' AND al.IsActive='Y'";
		//	+ " AND al.C_Invoice_ID IS NOT NULL";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_Payment_ID());

			ResultSet rs = pstmt.executeQuery();
			if (rs.next() && rs.getBigDecimal(1)!=null)
				retValue = rs.getBigDecimal(1);
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getAllocatedAmt", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
	//	log.fine("getAllocatedAmt - " + retValue);
		//	? ROUND(NVL(v_AllocatedAmt,0), 2);
		return retValue;
	}	//	getAllocatedAmt

	/**
	 * 	Test Allocation (and set allocated flag)
	 *	@return true if updated
	 */
	public boolean testAllocation()
	{
		//	Cash Trx always allocated
		if (isCashTrx())
		{
			if (!isAllocated())
			{
				setIsAllocated(true);
				return true;
			}
			return false;
		}
		//
		BigDecimal alloc = getAllocatedAmt();

		if (alloc == null)
			alloc = Env.ZERO;
		BigDecimal total = getPayAmt();

		if (!isReceipt())
			total = total.negate();
		boolean test = total.compareTo(alloc) == 0;
		boolean change = test != isAllocated();
		if (change)
			setIsAllocated(test);
		log.fine("Allocated=" + test
			+ " (" + alloc + "=" + total + ")");
		return change;
	}	//	testAllocation

	/**
	 * 	Set Allocated Flag for payments
	 * 	@param ctx context
	 *	@param C_BPartner_ID if 0 all
	 *	@param trxName trx
	 */
	public static void setIsAllocated (Properties ctx, int C_BPartner_ID, String trxName)
	{
		int counter = 0;
		String sql = "SELECT * FROM C_Payment "
			+ "WHERE IsAllocated='N' AND DocStatus IN ('CO','CL')";
		if (C_BPartner_ID > 1)
			sql += " AND C_BPartner_ID=?";
		else
			sql += " AND AD_Client_ID=" + Env.getAD_Client_ID(ctx);
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			if (C_BPartner_ID > 1)
				pstmt.setInt (1, C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MPayment pay = new MPayment (ctx, rs, trxName);
				if (pay.testAllocation())
					if (pay.save())
						counter++;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
		s_log.config("#" + counter);
	}	//	setIsAllocated

	/**************************************************************************
	 * 	Set Error Message
	 *	@param errorMessage error message
	 */
	public void setErrorMessage(String errorMessage)
	{
		m_errorMessage = errorMessage;
	}	//	setErrorMessage

	/**
	 * 	Get Error Message
	 *	@return error message
	 */
	public String getErrorMessage()
	{
		return m_errorMessage;
	}	//	getErrorMessage


	/**
	 *  Set Bank Account for Payment.
	 *  @param C_BankAccount_ID C_BankAccount_ID
	 */
	public void setC_BankAccount_ID (int C_BankAccount_ID)
	{
		if (C_BankAccount_ID == 0)
		{
			setPaymentProcessor();
			if (getC_BankAccount_ID() == 0)
				throw new IllegalArgumentException("Can't find Bank Account");
		}
		else
			super.setC_BankAccount_ID(C_BankAccount_ID);
	}	//	setC_BankAccount_ID

	/**
	 *  Set BankAccount and PaymentProcessor
	 *  @return true if found
	 */
	public boolean setPaymentProcessor ()
	{
		return setPaymentProcessor (getTenderType(), getCreditCardType());
	}	//	setPaymentProcessor

	/**
	 *  Set BankAccount and PaymentProcessor
	 *  @param tender TenderType see TENDER_
	 *  @param CCType CC Type see CC_
	 *  @return true if found
	 */
	public boolean setPaymentProcessor (String tender, String CCType)
	{
		m_mPaymentProcessor = null;
		//	Get Processor List
		if (m_mPaymentProcessors == null || m_mPaymentProcessors.length == 0)
			m_mPaymentProcessors = MPaymentProcessor.find (getCtx(), tender, CCType, getAD_Client_ID(),
				getC_Currency_ID(), getPayAmt(), get_TrxName());
		//	Relax Amount
		if (m_mPaymentProcessors == null || m_mPaymentProcessors.length == 0)
			m_mPaymentProcessors = MPaymentProcessor.find (getCtx(), tender, CCType, getAD_Client_ID(),
				getC_Currency_ID(), Env.ZERO, get_TrxName());
		if (m_mPaymentProcessors == null || m_mPaymentProcessors.length == 0)
			return false;

		//	Find the first right one
		for (int i = 0; i < m_mPaymentProcessors.length; i++)
		{
			if (m_mPaymentProcessors[i].accepts (tender, CCType))
			{
				m_mPaymentProcessor = m_mPaymentProcessors[i];
			}
		}
		if (m_mPaymentProcessor != null)
			setC_BankAccount_ID (m_mPaymentProcessor.getC_BankAccount_ID());
		//
		return m_mPaymentProcessor != null;
	}   //  setPaymentProcessor


	/**
	 * 	Get Accepted Credit Cards for PayAmt (default 0)
	 *	@return credit cards
	 */
	public ValueNamePair[] getCreditCards ()
	{
		return getCreditCards(getPayAmt());
	}	//	getCreditCards


	/**
	 * 	Get Accepted Credit Cards for amount
	 *	@param amt trx amount
	 *	@return credit cards
	 */
	public ValueNamePair[] getCreditCards (BigDecimal amt)
	{
		try
		{
			if (m_mPaymentProcessors == null || m_mPaymentProcessors.length == 0)
				m_mPaymentProcessors = MPaymentProcessor.find (getCtx (), null, null,
					getAD_Client_ID (), getC_Currency_ID (), amt, get_TrxName());
			//
			HashMap<String,ValueNamePair> map = new HashMap<String,ValueNamePair>(); //	to eliminate duplicates
			for (int i = 0; i < m_mPaymentProcessors.length; i++)
			{
				if (m_mPaymentProcessors[i].isAcceptAMEX ())
					map.put (CREDITCARDTYPE_Amex, getCreditCardPair (CREDITCARDTYPE_Amex));
				if (m_mPaymentProcessors[i].isAcceptDiners ())
					map.put (CREDITCARDTYPE_Diners, getCreditCardPair (CREDITCARDTYPE_Diners));
				if (m_mPaymentProcessors[i].isAcceptDiscover ())
					map.put (CREDITCARDTYPE_Discover, getCreditCardPair (CREDITCARDTYPE_Discover));
				if (m_mPaymentProcessors[i].isAcceptMC ())
					map.put (CREDITCARDTYPE_MasterCard, getCreditCardPair (CREDITCARDTYPE_MasterCard));
				if (m_mPaymentProcessors[i].isAcceptCorporate ())
					map.put (CREDITCARDTYPE_PurchaseCard, getCreditCardPair (CREDITCARDTYPE_PurchaseCard));
				if (m_mPaymentProcessors[i].isAcceptVisa ())
					map.put (CREDITCARDTYPE_Visa, getCreditCardPair (CREDITCARDTYPE_Visa));
			} //	for all payment processors
			//
			ValueNamePair[] retValue = new ValueNamePair[map.size ()];
			map.values ().toArray (retValue);
			log.fine("getCreditCards - #" + retValue.length + " - Processors=" + m_mPaymentProcessors.length);
			return retValue;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}	//	getCreditCards

	/**
	 * 	Get Type and name pair
	 *	@param CreditCardType credit card Type
	 *	@return pair
	 */
	private ValueNamePair getCreditCardPair (String CreditCardType)
	{
		return new ValueNamePair (CreditCardType, getCreditCardName(CreditCardType));
	}	//	getCreditCardPair


	/**************************************************************************
	 *  Credit Card Number
	 *  @param CreditCardNumber CreditCard Number
	 */
	public void setCreditCardNumber (String CreditCardNumber)
	{
		super.setCreditCardNumber (MPaymentValidate.checkNumeric(CreditCardNumber));
	}	//	setCreditCardNumber

	/**
	 *  Verification Code
	 *  @param newCreditCardVV CC verification
	 */
	public void setCreditCardVV(String newCreditCardVV)
	{
		super.setCreditCardVV (MPaymentValidate.checkNumeric(newCreditCardVV));
	}	//	setCreditCardVV

	/**
	 *  Two Digit CreditCard MM
	 *  @param CreditCardExpMM Exp month
	 */
	public void setCreditCardExpMM (int CreditCardExpMM)
	{
		if (CreditCardExpMM < 1 || CreditCardExpMM > 12)
			;
		else
			super.setCreditCardExpMM (CreditCardExpMM);
	}	//	setCreditCardExpMM

	/**
	 *  Two digit CreditCard YY (til 2020)
	 *  @param newCreditCardExpYY 2 or 4 digit year
	 */
	public void setCreditCardExpYY (int newCreditCardExpYY)
	{
		int CreditCardExpYY = newCreditCardExpYY;
		if (newCreditCardExpYY > 1999)
			CreditCardExpYY = newCreditCardExpYY-2000;
		super.setCreditCardExpYY(CreditCardExpYY);
	}	//	setCreditCardExpYY

	/**
	 *  CreditCard Exp  MMYY
	 *  @param mmyy Exp in form of mmyy
	 *  @return true if valid
	 */
	public boolean setCreditCardExp (String mmyy)
	{
		if (MPaymentValidate.validateCreditCardExp(mmyy).length() != 0)
			return false;
		//
		String exp = MPaymentValidate.checkNumeric(mmyy);
		String mmStr = exp.substring(0,2);
		String yyStr = exp.substring(2,4);
		setCreditCardExpMM (Integer.parseInt(mmStr));
		setCreditCardExpYY (Integer.parseInt(yyStr));
		return true;
	}   //  setCreditCardExp


	/**
	 *  CreditCard Exp  MMYY
	 *  @param delimiter / - or null
	 *  @return Exp
	 */
	public String getCreditCardExp(String delimiter)
	{
		String mm = String.valueOf(getCreditCardExpMM());
		String yy = String.valueOf(getCreditCardExpYY());

		StringBuffer retValue = new StringBuffer();
		if (mm.length() == 1)
			retValue.append("0");
		retValue.append(mm);
		//
		if (delimiter != null)
			retValue.append(delimiter);
		//
		if (yy.length() == 1)
			retValue.append("0");
		retValue.append(yy);
		//
		return (retValue.toString());
	}   //  getCreditCardExp

	/**
	 *  MICR
	 *  @param MICR MICR
	 */
	public void setMicr (String MICR)
	{
		super.setMicr (MPaymentValidate.checkNumeric(MICR));
	}	//	setBankMICR

	/**
	 *  Routing No
	 *  @param RoutingNo Routing No
	 */
	public void setRoutingNo(String RoutingNo)
	{
		super.setRoutingNo (MPaymentValidate.checkNumeric(RoutingNo));
	}	//	setBankRoutingNo


	/**
	 *  Bank Account No
	 *  @param AccountNo AccountNo
	 */
	public void setAccountNo (String AccountNo)
	{
		super.setAccountNo (MPaymentValidate.checkNumeric(AccountNo));
	}	//	setBankAccountNo


	/**
	 *  Check No
	 *  @param CheckNo Check No
	 */
	public void setCheckNo(String CheckNo)
	{
		super.setCheckNo(MPaymentValidate.checkNumeric(CheckNo));
	}	//	setBankCheckNo


	/**
	 *  Set DocumentNo to Payment info.
	 * 	If there is a R_PnRef that is set automatically
	 */
	private void setDocumentNo()
	{
		//	Cash Transfer
		if ("X".equals(getTenderType()))
			return;
		//	Current Document No
		String documentNo = getDocumentNo();
		//	Existing reversal
		if (documentNo != null
			&& documentNo.indexOf(REVERSE_INDICATOR) >= 0)
			return;

		//	If external number exists - enforce it
		if (getR_PnRef() != null && getR_PnRef().length() > 0)
		{
			if (!getR_PnRef().equals(documentNo))
				setDocumentNo(getR_PnRef());
			return;
		}

		documentNo = "";

                /* Begin e-Evolution
		//	Credit Card
		if (TENDERTYPE_CreditCard.equals(getTenderType()))
		{
			documentNo = getCreditCardType()
				+ " " + Obscure.obscure(getCreditCardNumber())
				+ " " + getCreditCardExpMM()
				+ "/" + getCreditCardExpYY();
		}
		//	Own Check No
		else
                // End e-Evolution */

                        if (TENDERTYPE_Check.equals(getTenderType())
			&& !isReceipt()
			&& getCheckNo() != null && getCheckNo().length() > 0)
		{
			documentNo = getCheckNo();
		}
		//	Customer Check: Routing: Account #Check
                //begin e-evolution  vpj-cd 11 MAy 2006
		/*else if (TENDERTYPE_Check.equals(getTenderType())
			&& isReceipt())
		{
			if (getRoutingNo() != null)
				documentNo = getRoutingNo() + ": ";
			if (getAccountNo() != null)
				documentNo += getAccountNo();
			if (getCheckNo() != null)
			{
				if (documentNo.length() > 0)
					documentNo += " ";
				documentNo += "#" + getCheckNo();
			}
		}
                 // End e-Evolution */
		//	Set Document No
		documentNo = documentNo.trim();
		if (documentNo.length() > 0)
			setDocumentNo(documentNo);
	}	//	setDocumentNo

	/**
	 * 	Set Refernce No (and Document No)
	 *	@param R_PnRef reference
	 */
	public void setR_PnRef (String R_PnRef)
	{
		super.setR_PnRef (R_PnRef);
		if (R_PnRef != null)
			setDocumentNo (R_PnRef);
	}	//	setR_PnRef

	//	---------------

	/**
	 *  Set Payment Amount
	 *  @param PayAmt Pay Amt
	 */
	public void setPayAmt (BigDecimal PayAmt)
	{
		super.setPayAmt(PayAmt == null ? Env.ZERO : PayAmt);
	}	//	setPayAmt

	/**
	 *  Set Payment Amount
	 *
	 * @param C_Currency_ID currency
	 * @param payAmt amount
	 */
	public void setAmount (int C_Currency_ID, BigDecimal payAmt)
	{
		if (C_Currency_ID == 0)
			C_Currency_ID = MClient.get(getCtx()).getC_Currency_ID();
		setC_Currency_ID(C_Currency_ID);
		setPayAmt(payAmt);
	}   //  setAmount

	/**
	 *  Discount Amt
	 *  @param DiscountAmt Discount
	 */
	public void setDiscountAmt (BigDecimal DiscountAmt)
	{
		super.setDiscountAmt (DiscountAmt == null ? Env.ZERO : DiscountAmt);
	}	//	setDiscountAmt

	/**
	 *  WriteOff Amt
	 *  @param WriteOffAmt WriteOff
	 */
	public void setWriteOffAmt (BigDecimal WriteOffAmt)
	{
		super.setWriteOffAmt (WriteOffAmt == null ? Env.ZERO : WriteOffAmt);
	}	//	setWriteOffAmt

	/**
	 *  OverUnder Amt
	 *  @param OverUnderAmt OverUnder
	 */
	public void setOverUnderAmt (BigDecimal OverUnderAmt)
	{
		super.setOverUnderAmt (OverUnderAmt == null ? Env.ZERO : OverUnderAmt);
		setIsOverUnderPayment(getOverUnderAmt().compareTo(Env.ZERO) != 0);
	}	//	setOverUnderAmt

	/**
	 *  Tax Amt
	 *  @param TaxAmt Tax
	 */
	public void setTaxAmt (BigDecimal TaxAmt)
	{
		super.setTaxAmt (TaxAmt == null ? Env.ZERO : TaxAmt);
	}	//	setTaxAmt

	/**
	 * 	Set Info from BP Bank Account
	 *	@param ba BP bank account
	 */
	public void setBP_BankAccount (MBPBankAccount ba)
	{
		log.fine("" + ba);
		if (ba == null)
			return;
		setC_BPartner_ID(ba.getC_BPartner_ID());
		setAccountAddress(ba.getA_Name(), ba.getA_Street(), ba.getA_City(),
			ba.getA_State(), ba.getA_Zip(), ba.getA_Country());
		setA_EMail(ba.getA_EMail());
		setA_Ident_DL(ba.getA_Ident_DL());
		setA_Ident_SSN(ba.getA_Ident_SSN());
		//	CC
		if (ba.getCreditCardType() != null)
			setCreditCardType(ba.getCreditCardType());
		if (ba.getCreditCardNumber() != null)
			setCreditCardNumber(ba.getCreditCardNumber());
		if (ba.getCreditCardExpMM() != 0)
			setCreditCardExpMM(ba.getCreditCardExpMM());
		if (ba.getCreditCardExpYY() != 0)
			setCreditCardExpYY(ba.getCreditCardExpYY());
		if (ba.getCreditCardVV() != null)
			setCreditCardVV(ba.getCreditCardVV());
		//	Bank
		if (ba.getAccountNo() != null)
			setAccountNo(ba.getAccountNo());
		if (ba.getRoutingNo() != null)
			setRoutingNo(ba.getRoutingNo());
	}	//	setBP_BankAccount

	/**
	 * 	Save Info from BP Bank Account
	 *	@param ba BP bank account
	 * 	@return true if saved
	 */
	public boolean saveToBP_BankAccount (MBPBankAccount ba)
	{
		if (ba == null)
			return false;
		ba.setA_Name(getA_Name());
		ba.setA_Street(getA_Street());
		ba.setA_City(getA_City());
		ba.setA_State(getA_State());
		ba.setA_Zip(getA_Zip());
		ba.setA_Country(getA_Country());
		ba.setA_EMail(getA_EMail());
		ba.setA_Ident_DL(getA_Ident_DL());
		ba.setA_Ident_SSN(getA_Ident_SSN());
		//	CC
		ba.setCreditCardType(getCreditCardType());
		ba.setCreditCardNumber(getCreditCardNumber());
		ba.setCreditCardExpMM(getCreditCardExpMM());
		ba.setCreditCardExpYY(getCreditCardExpYY());
		ba.setCreditCardVV(getCreditCardVV());
		//	Bank
		if (getAccountNo() != null)
			ba.setAccountNo(getAccountNo());
		if (getRoutingNo() != null)
			ba.setRoutingNo(getRoutingNo());
		//	Trx
		ba.setR_AvsAddr(getR_AvsAddr());
		ba.setR_AvsZip(getR_AvsZip());
		//
		boolean ok = ba.save(get_TrxName());
		log.fine("saveToBP_BankAccount - " + ba);
		return ok;
	}	//	setBP_BankAccount

	/**
	 * 	Set Doc Type bases on IsReceipt
	 */
	private void setC_DocType_ID ()
	{
		setC_DocType_ID(isReceipt());
	}	//	setC_DocType_ID

	/**
	 * 	Set Doc Type
	 * 	@param isReceipt is receipt
	 */
	public void setC_DocType_ID (boolean isReceipt)
	{
		setIsReceipt(isReceipt);
		String sql = "SELECT C_DocType_ID FROM C_DocType WHERE AD_Client_ID=? AND DocBaseType=? ORDER BY IsDefault DESC";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getAD_Client_ID());
			if (isReceipt)
				pstmt.setString(2, MDocType.DOCBASETYPE_ARReceipt);
			else
				pstmt.setString(2, MDocType.DOCBASETYPE_APPayment);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				setC_DocType_ID(rs.getInt(1));
			else
				log.warning ("setDocType - NOT found - isReceipt=" + isReceipt);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}	//	setC_DocType_ID


	/**
	 * 	Set Document Type
	 *	@param C_DocType_ID doc type
	 */
	public void setC_DocType_ID (int C_DocType_ID)
	{
	//	if (getDocumentNo() != null && getC_DocType_ID() != C_DocType_ID)
	//		setDocumentNo(null);
		super.setC_DocType_ID(C_DocType_ID);
	}	//	setC_DocType_ID

	/**
	 * 	Verify Document Type with Invoice
	 *	@return true if ok
	 */
	private boolean verifyDocType()
	{
		if (getC_DocType_ID() == 0)
			return false;
		//
		Boolean invoiceSO = null;
		//	Check Invoice First
		if (getC_Invoice_ID() > 0)
		{
			String sql = "SELECT idt.IsSOTrx "
				+ "FROM C_Invoice i"
				+ " INNER JOIN C_DocType idt ON (i.C_DocType_ID=idt.C_DocType_ID) "
				+ "WHERE i.C_Invoice_ID=?";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, getC_Invoice_ID());
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					invoiceSO = new Boolean ("Y".equals(rs.getString(1)));
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			try
			{
				if (pstmt != null)
					pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				pstmt = null;
			}
		}	//	Invoice

		//	DocumentType
		Boolean paymentSO = null;
		PreparedStatement pstmt = null;
		String sql = "SELECT IsSOTrx "
			+ "FROM C_DocType "
			+ "WHERE C_DocType_ID=?";
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_DocType_ID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				paymentSO = new Boolean ("Y".equals(rs.getString(1)));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		//	No Payment info
		if (paymentSO == null)
			return false;
		setIsReceipt(paymentSO.booleanValue());

		//	We have an Invoice .. and it does not match
		if (invoiceSO != null
				&& invoiceSO.booleanValue() != paymentSO.booleanValue())
			return false;
		//	OK
		return true;
	}	//	verifyDocType


	/**
	 *	Get ISO Code of Currency
	 *	@return Currency ISO
	 */
	public String getCurrencyISO()
	{
		return MCurrency.getISO_Code (getCtx(), getC_Currency_ID());
	}	//	getCurrencyISO

	/**
	 * 	Get Document Status
	 *	@return Document Status Clear Text
	 */
	public String getDocStatusName()
	{
		return MRefList.getListName(getCtx(), 131, getDocStatus());
	}	//	getDocStatusName

	/**
	 *	Get Name of Credit Card
	 *	@return Name
	 */
	public String getCreditCardName()
	{
		return getCreditCardName(getCreditCardType());
	}	//	getCreditCardName

	/**
	 *	Get Name of Credit Card
	 * 	@param CreditCardType credit card type
	 *	@return Name
	 */
	public String getCreditCardName(String CreditCardType)
	{
		if (CreditCardType == null)
			return "--";
		else if (CREDITCARDTYPE_MasterCard.equals(CreditCardType))
			return "MasterCard";
		else if (CREDITCARDTYPE_Visa.equals(CreditCardType))
			return "Visa";
		else if (CREDITCARDTYPE_Amex.equals(CreditCardType))
			return "Amex";
		else if (CREDITCARDTYPE_ATM.equals(CreditCardType))
			return "ATM";
		else if (CREDITCARDTYPE_Diners.equals(CreditCardType))
			return "Diners";
		else if (CREDITCARDTYPE_Discover.equals(CreditCardType))
			return "Discover";
		else if (CREDITCARDTYPE_PurchaseCard.equals(CreditCardType))
			return "PurchaseCard";
		return "?" + CreditCardType + "?";
	}	//	getCreditCardName

	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription


	/**
	 * 	Get Pay Amt
	 * 	@param absolute if true the absolute amount (i.e. negative if payment)
	 *	@return amount
	 */
	public BigDecimal getPayAmt (boolean absolute)
	{
		if (isReceipt())
			return super.getPayAmt();
		return super.getPayAmt().negate();
	}	//	getPayAmt

	/**
	 * 	Get Pay Amt in cents
	 *	@return amount in cents
	 */
	public int getPayAmtInCents ()
	{
		BigDecimal bd = super.getPayAmt().multiply(Env.ONEHUNDRED);
		return bd.intValue();
	}	//	getPayAmtInCents

	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	process

	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**
	 * 	Unlock Document.
	 * 	@return true if success
	 */
	public boolean unlockIt()
	{
		log.info("unlockIt - " + toString());
		setProcessing(false);
		return true;
	}	//	unlockIt

	/**
	 * 	Invalidate Document
	 * 	@return true if success
	 */
	public boolean invalidateIt()
	{
		log.info("invalidateIt - " + toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt


	/**************************************************************************
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt()
	{
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
                
                /*
		 *      Zyynia
                 *      10-05-2012 Martin Maria Jesus
		 *      Modificado para que verifique que se le ha asignado un valor a la OP, sino que no se deje completar.
		*/
                
                if (getPayAmt().equals(Env.ZERO)){
                        JOptionPane.showMessageDialog(null,"Es necesario asignar un valor.","ERROR - No cuenta con valor asignado", JOptionPane.ERROR_MESSAGE);
			return DocAction.STATUS_Drafted;
                }                

		//	Std Period open?
		if (!MPeriod.isOpen(getCtx(), getDateAcct(),
			isReceipt() ? MDocType.DOCBASETYPE_ARReceipt : MDocType.DOCBASETYPE_APPayment))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Drafted;
		}

		//	Unsuccessful Online Payment
		if (isOnline() && !isApproved())
		{
			if (getR_Result() != null)
				m_processMsg = "@OnlinePaymentFailed@";
			else
				m_processMsg = "@PaymentNotProcessed@";
			return DocAction.STATUS_Drafted;
		}

		//	Waiting Payment - Need to create Invoice & Shipment
		if (getC_Order_ID() != 0 && getC_Invoice_ID() == 0)
		{	//	see WebOrder.process
			MOrder order = new MOrder (getCtx(), getC_Order_ID(), get_TrxName());
			if (DOCSTATUS_WaitingPayment.equals(order.getDocStatus()))
			{
				order.setC_Payment_ID(getC_Payment_ID());
				order.setDocAction(MOrder.DOCACTION_WaitComplete);
				order.set_TrxName(get_TrxName());
				boolean ok = order.processIt (MOrder.DOCACTION_WaitComplete);
				m_processMsg = order.getProcessMsg();
				order.save(get_TrxName());
				//	Set Invoice
				MInvoice[] invoices = order.getInvoices();
				int length = invoices.length;
				if (length > 0)		//	get last invoice
					setC_Invoice_ID (invoices[length-1].getC_Invoice_ID());
				//
				if (getC_Invoice_ID() == 0)
				{
					m_processMsg = "@NotFound@ @C_Invoice_ID@";
					return DocAction.STATUS_Invalid;
				}
			}	//	WaitingPayment
		}

		//	Consistency of Invoice / Document Type and IsReceipt
		if (!verifyDocType())
		{
			m_processMsg = "@PaymentDocTypeInvoiceInconsistent@";
			return DocAction.STATUS_Drafted;
		}

		//	Do not pay when Credit Stop/Hold
		if (!isReceipt())
		{
			MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
			if (MBPartner.SOCREDITSTATUS_CreditStop.equals(bp.getSOCreditStatus()))
			{
				m_processMsg = "@BPartnerCreditStop@ - @TotalOpenBalance@="
					+ bp.getTotalOpenBalance()
					+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
				return DocAction.STATUS_Drafted;
			}
			if (MBPartner.SOCREDITSTATUS_CreditHold.equals(bp.getSOCreditStatus()))
			{
				m_processMsg = "@BPartnerCreditHold@ - @TotalOpenBalance@="
					+ bp.getTotalOpenBalance()
					+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
				return DocAction.STATUS_Drafted;
			}
		}

		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt

	/**
	 * 	Approve Document
	 * 	@return true if success
	 */
	public boolean  approveIt()
	{
		log.info(toString());
		setIsApproved(true);
		return true;
	}	//	approveIt

	/**
	 * 	Reject Approval
	 * 	@return true if success
	 */
	public boolean rejectIt()
	{
		log.info(toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt

	/**************************************************************************
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{

		/**
		 * 		Creado REQ-021 - DANIEL 31/10/2008
		 *
		 *		Modificación REQ-034 - DANIEL 15/12/2008 (Parte 3).
	 	 * 			VERIFICACIÓN DE MONTOS
	 	 *
	 	 *		Modificación REQ-065 - DANIEL 25/06/2009
	 	 *			Nueva manera de verificar los montos.
		 */
           
		if (isReceipt())
		{
			// Verificar que existan Retenciones o Valores.

			if (getRetenciones()==null && getValoresCobranza()==null)
			{
				JOptionPane.showMessageDialog(null,"Es necesario ingresar Datos en la Pestaña Valores o Retenciones","ERROR - Verificación Datos", JOptionPane.ERROR_MESSAGE);
				return DocAction.STATUS_Drafted;
			}
			if (getAmountAllocate().compareTo(getPayAmt())==1)
			{
				JOptionPane.showMessageDialog(null,"El importe asignado supera el total de valores recibidos","ERROR - Verificación Asignación", JOptionPane.ERROR_MESSAGE);
				return DocAction.STATUS_Drafted;
			}

			List<MPaymentAllocate> payAll = getAllocate();

			int i = 0;

			if (payAll != null)
			{
				while (i<payAll.size())
				{
					MInvoice inv = new MInvoice(getCtx(),payAll.get(i).getC_Invoice_ID(),get_TrxName());
					if (inv.getC_Currency_ID()!=getC_Currency_ID())
					{
						JOptionPane.showMessageDialog(null,"La moneda seleccionada en la cobranza no coincide con la moneda de emisión de los comprobantes a cancelar.","ERROR - Verificación Asignación", JOptionPane.ERROR_MESSAGE);
						return DocAction.STATUS_Drafted;
					}
					i++;
				}
			}

			setIsAllocated(true);
		}
		else
		{
			if (getValoresPago()==null || getValoresPago().size() == 0)
			{
				JOptionPane.showMessageDialog(null,"Es necesario ingresar Datos en la Pestaña Valores","ERROR - Verificación Datos", JOptionPane.ERROR_MESSAGE);
				return DocAction.STATUS_Drafted;
			}
			else
			{
				try {
                                    
                                    
                                    /*
                                     *  Zynnia anexo para controlar que no pueda asignar por dos pagos más monto que el
                                     *  del total de la factura
                                     * 
                                     *  22/11/2012
                                     */
                                    
                                        String sql = "SELECT C_Invoice_Id " +
					  			 "FROM C_PaymentAllocate " +
					  			 "WHERE C_Payment_ID = ? AND IsActive = 'Y'";

					PreparedStatement pstmt = DB.prepareStatement(sql, null);
					pstmt.setLong(1, getC_Payment_ID());

					ResultSet rs = pstmt.executeQuery();

					if (rs.next())
					{
							/*
							 * Sólo si hay facturas asignadas
							 * en la pestaña Asignación.
							 */
						    sql = "SELECT MAX(p.PayAmt)-COALESCE(SUM(a.Amount),0) " +
							  "FROM C_Payment p " +
							  "LEFT OUTER JOIN C_PaymentAllocate a ON (p.C_Payment_ID=a.C_Payment_ID) " +
							  "WHERE p.C_Payment_ID = ? AND a.IsActive = 'Y'";

							pstmt = DB.prepareStatement(sql, null);
							pstmt.setLong(1, getC_Payment_ID());

							rs = pstmt.executeQuery();

							if (rs.next())
							{
								float remanente = rs.getFloat(1);

								if (remanente < 0)	{
									JOptionPane.showMessageDialog(null,"El monto asignado supera el Total del Pago","ERROR - Verificación Montos", JOptionPane.ERROR_MESSAGE);
									return DocAction.STATUS_Drafted;
								}
							}
					}
					else
						if (getC_Invoice_ID() == 0)
							setIsPrepayment(true);
                                    
                                    
                                    
					sql = "SELECT C_VALORPAGO_ID " +
			  			  "FROM C_VALORPAGO " +
			  			  "WHERE C_Payment_ID = ? AND IsActive = 'Y'";



					/*pstmt = DB.prepareStatement(sql, null);
					pstmt.setLong(1, getC_Payment_ID());

					rs = pstmt.executeQuery();*/

                                        PreparedStatement pstmtValor = DB.prepareStatement(sql, null);
					pstmtValor.setLong(1, getC_Payment_ID());

					ResultSet rsValor = pstmtValor.executeQuery();

					if (rsValor.next())
					{
/*						sql = "SELECT MAX(p.PayNet),COALESCE(SUM(a.Importe),0), " +
						  "FROM C_Payment p " +
						  "LEFT OUTER JOIN C_VALORPAGO a ON (p.C_Payment_ID=a.C_Payment_ID) " +
						  //"LEFT OUTER JOIN C_PAYMENTRET r ON (p.C_Payment_ID=r.C_Payment_ID) " +
						  "WHERE p.C_Payment_ID = ? AND a.IsActive = 'Y' ";//AND r.IsActive = 'Y' ";*/

						/*
						 * 17-12-2010 Camarzana Mariano
						 * Comentado y modificado para que tome el valor de la moneda original
						 */
						String sql2 = "SELECT MAX(p.PayNet),COALESCE(SUM(a.Importe),0),COALESCE(SUM(a.mextranjera),0) " +
						  "FROM C_Payment p " +
						  "LEFT OUTER JOIN C_VALORPAGO a ON (p.C_Payment_ID=a.C_Payment_ID) " +
						  //"LEFT OUTER JOIN C_PAYMENTRET r ON (p.C_Payment_ID=r.C_Payment_ID) " +
						  "WHERE p.C_Payment_ID = ? AND a.IsActive = 'Y' ";//AND r.IsActive = 'Y' ";

                                                PreparedStatement pstmt2;
                                                ResultSet rs2;
						pstmt2 = DB.prepareStatement(sql2, null);
						pstmt2.setLong(1, getC_Payment_ID());
						rs2 = pstmt2.executeQuery();

						if (rs2.next())
						{
							float remanente = (rs2.getFloat(3)==0 ? rs2.getFloat(1) - rs2.getFloat(2) : rs2.getFloat(1) - rs2.getFloat(3));
							if (remanente != 0)	{
								JOptionPane.showMessageDialog(null,"Los valores asignados no coinciden con el Neto del Pago","ERROR - Verificación Montos", JOptionPane.ERROR_MESSAGE);
								/*
                                                                 *  Modificado para que el pago siga quedando en borrador de no coincidir los montos.
                                                                 *  Zynnia - 19/03/2012
                                                                 *  José Fantasia
                                                                 * 
                                                                 */
                                                                pstmt2.close();
                                                                rs2.close();
                                                                return DocAction.STATUS_Drafted;
                                                                //return DocAction.STATUS_Invalid;
                                                                
							}
						}
                                                pstmt2.close();
                                                rs2.close();
					}
                                        
                                        pstmt.close();
                                        rs.close();
                                        
                                        
                                        /*
                                         *  31/08/2012 Zynnia
                                         *  Se agrega que se valide que el monto total del pago - el neto sea igual a las
                                         *  retenciones del pago.
                                         * 
                                         */
                                        
                                        BigDecimal bd = this.getPayAmt();
                                        bd = bd.subtract(this.getPAYNET());
                                        
                                        /*
                                         *  28/11/2012 Zynnia
                                         *  Se agrega que se valide la moneda del pago. Si no es $ debe ser
                                         *  convertidas las retenciones que siempre están en $ (C_Currency_ID=118)
                                         * 
                                         */
                                        
                                        if(this.getC_Currency_ID() != 118) {
                                            MCurrency moneda = MCurrency.get (Env.getCtx(), getC_Currency_ID());                
                                            BigDecimal zeroScale = Env.ZERO;

                                            if (moneda != null) {                    
                                                zeroScale = Env.ZERO.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                                                /*bd = bd.subtract(this.getRetencionIB().divide(this.getCotizacion())).setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                                                bd = bd.subtract(this.getRetencionGanancias().divide(this.getCotizacion())).setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                                                bd = bd.subtract(this.getRetencionSUSS().divide(this.getCotizacion())).setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                                                bd = bd.subtract(this.getRetencionIVA().divide(this.getCotizacion())).setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);*/
                                                bd = bd.subtract(this.getRetencionIB().divide(this.getCotizacion(),moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP)).setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                                                bd = bd.subtract(this.getRetencionGanancias().divide(this.getCotizacion(),moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP)).setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                                                bd = bd.subtract(this.getRetencionSUSS().divide(this.getCotizacion(),moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP)).setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                                                bd = bd.subtract(this.getRetencionIVA().divide(this.getCotizacion(),moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP)).setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                                            }
                                        } else {
                                            bd = bd.subtract(this.getRetencionIB());
                                            bd = bd.subtract(this.getRetencionGanancias());
                                            bd = bd.subtract(this.getRetencionSUSS());
                                            bd = bd.subtract(this.getRetencionIVA());
                                        }
                                        
                                        MCurrency moneda = MCurrency.get (Env.getCtx(), getC_Currency_ID());                
                                        BigDecimal zeroScale = Env.ZERO;
                                        
                                        if (moneda != null) {                    
                                            zeroScale = Env.ZERO.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                                            bd = bd.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                                        }
                                        
                                        if (!bd.equals(zeroScale)){
                                            JOptionPane.showMessageDialog(null,"El valor neto mas las retenciones no coinciden con el total del pago.","ERROR - Verificación Montos", JOptionPane.ERROR_MESSAGE);
                                            return DocAction.STATUS_Drafted;
                                        }                                        
                                        
                                        setFlagSave(true);
                                        
                                        
				}
				catch (SQLException esql){
				}
			}
		}

        /**
         * 		FIN VERIFICACIÓN MONTOS
         */

		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}
		//	Implicit Approval
		if (!isApproved())
			approveIt();
		log.info(toString());

		//	Charge Handling
		if (getC_Charge_ID() != 0)
		{
			setIsAllocated(true);
		}
		else
		{
                    /*
                     * GENEOS -Pablo Velazquez
                     * 01/07/2013
                     * Si falla al crear las asignaciones, se informa el error y se interrumple el proceso.
                     * Ademas solos se llama a allocateIt() si hay lineas para asignar.
                     */
                    MPaymentAllocate[] pAllocs = MPaymentAllocate.get(this);
                    if (pAllocs.length > 0){
			if ( !allocateIt() ) { //Create Allocation Records
                            JOptionPane.showMessageDialog(null,"Error al intentar completar las asignaciones. Por favor intentelo nuevamente.","ERROR - Asignaciones", JOptionPane.ERROR_MESSAGE);
                            m_processMsg += "Error al intentar completar las asignaciones.";
                            return DocAction.STATUS_Drafted;
                        }
			testAllocation();
                    }
		}

		//	Project update
		if (getC_Project_ID() != 0)
		{
		//	MProject project = new MProject(getCtx(), getC_Project_ID());
		}

		/**
		 * 	@author Daniel - 28/05/2009 - REQ-055
		 *
		 *  - PAGO ASIGNADO => SALDO ACTUAL Actualizado.
		 *  - PAGO PARCIALMENTE ASIGNADO => SALDO ACTUAL Agrega el monto No Asignado.
		 *	- PAGO NO ASIGNADO => SALDO ACTUAL Agregar monto del pago.
		 */

		if (!isReceipt() && getC_BPartner_ID() != 0 && !isAllocated())
		{
			MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
			BigDecimal previo = bp.getTotalOpenBalance();
                        BigDecimal actual = Env.ZERO;

                        BigDecimal monto = getAllocatedAmt();
                        if (monto==null)
                            monto = Env.ZERO;

                        //	Suma el monto no asignado del pago, al saldo del proveedor
                        actual = previo.add(getPayAmt().add(monto));

                                    bp.setTotalOpenBalance(actual);
                        bp.save();
		}

		//	Counter Doc
		MPayment counter = createCounterDoc();
		if (counter != null)
			m_processMsg += " @CounterDoc@: @C_Payment_ID@=" + counter.getDocumentNo();

		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
                                 
                /*
                *  18/12/2012 Zynnia
                *  Se Obtiene numero de cheque o transaccion, segun corresponda y se calcula el siguiente.
                *  Esto antes se hacia con un callout CalloutValorPago + metodo beforeSave() de MVALORPAGO
                * 
                */              
                List<MVALORPAGO> valoresPago = getValoresPago();
                
                //MVALORPAGO = new MVALORPAGO(Env.getCtx(), 0,trx.getTrxName());
                for (int i=0;i<valoresPago.size();i++){
                    //MVALORPAGO nuevoValorPago = new MVALORPAGO(getCtx(),valoresPago.get(i).getC_VALORPAGO_ID(),get_TrxName());//
                    //Trx trx = Trx.get(Trx.createTrxName("ValorPago"), true);
                    MVALORPAGO nuevoValorPago = new MVALORPAGO(getCtx(),valoresPago.get(i).getC_VALORPAGO_ID(),null);
                    if (nuevoValorPago.getTIPO().equals(MVALORPAGO.PROPIO) 
                               || nuevoValorPago.getTIPO().equals(MVALORPAGO.PCBANKING)) {
                        
                        //Se Obtiene numero de cheque del pago
                        String unNumeroCheque = nuevoValorPago.getNroCheque();
                        int key = nuevoValorPago.getC_BankAccount_ID();
                        //Si el numero de cheque todavia no se genero, se genera.
                        if (unNumeroCheque.equals("00000000")){
                            unNumeroCheque = generateCheck(nuevoValorPago.getC_BankAccount_ID());
                            if (unNumeroCheque.equals("00000000")){
                             JOptionPane.showMessageDialog(null,"Actualice Documento de Cuenta Bancaria, cuenta: "+key, "Chequera Completa", JOptionPane.INFORMATION_MESSAGE);   
                             System.out.println("No se pudo generar numero de cheque para la cuenta: "+key+" id valorpago: "+nuevoValorPago.getC_VALORPAGO_ID());
                             return DocAction.STATUS_Drafted; 
                            }
                            if (unNumeroCheque == null){
                                JOptionPane.showMessageDialog(null,"No se pudo generar el numero de cheque, cuenta: "+key, "Número de Cheque Incorrecto", JOptionPane.INFORMATION_MESSAGE);
                                System.out.println("No se pudo generar numero de cheque para la cuenta: "+key+" id valorpago: "+nuevoValorPago.getC_VALORPAGO_ID());
                                return DocAction.STATUS_Drafted; 
                            }
                            nuevoValorPago.setNroCheque(unNumeroCheque);
                            if (nuevoValorPago.save()){
                                //Incremento Cheque
                                int nextNroCheque = Integer.valueOf(unNumeroCheque).intValue() + 1;
                                try
                                {
                                    DB.executeUpdate("UPDATE C_BankAccountDoc SET CURRENTNEXT = " + nextNroCheque + " WHERE C_BankAccount_ID = " + key, null);


                                }
                                catch (Exception e) {
                                    JOptionPane.showMessageDialog(null,"No se pudo generar el siguiente numero de cheque para: "+ nextNroCheque +", cuenta: "+key, "Error al Actualizar", JOptionPane.INFORMATION_MESSAGE);
                                    System.out.println("No se pudo generar el siguiente numero de cheque para: "+ nextNroCheque +", cuenta: "+key);
                                }
                            }
                            else {
                               JOptionPane.showMessageDialog(null,"No se pudo actualizar Nro. de Cheque", "Error al Actualizar", JOptionPane.INFORMATION_MESSAGE);
                               System.out.println("No se pudo actualizar Nro. de Cheque. id valorpago: "+nuevoValorPago.getC_VALORPAGO_ID());                                 
                               return DocAction.STATUS_Drafted;  
                            }
                            
                        }        
                    }
                   else                      
                        if (nuevoValorPago.getTIPO().equals(MVALORPAGO.TRANSFERENCIA)) {
                            //Se Obtiene numero de transferencia
                             String unNumeroTransferencia = nuevoValorPago.getNroTransferencia();
                            //Si el numero de transferencia todavia no se genero, se genera.
                            if (unNumeroTransferencia.equals("00000000")){
                                unNumeroTransferencia = generateTransferencia();
                                if (unNumeroTransferencia == null){
                                    m_processMsg = "No se puede generar automáticamente el Nro. de Transferencia.";
                                    System.out.println("No se puede generar automáticamente el Nro. de Transferencia. id valorpago: "+nuevoValorPago.getC_VALORPAGO_ID());                                 
                                    return DocAction.STATUS_Drafted; 
                                }
                                nuevoValorPago.setNroTransferencia(unNumeroTransferencia);
                                if (nuevoValorPago.save()){
                                    //ID For C_VALORPAGO_TRANSFERENCIA = 5000048
                                    //Incremento Transferencia
                                    
                                    MSequence seq = new MSequence(getCtx(), 5000048, get_TrxName());

                                    // Sequence increment and update
                                    seq.setCurrentNext(seq.getCurrentNext() + seq.getIncrementNo());
                                    seq.save();
                                    
                                }
                                else {
                                    m_processMsg = "No se pudo actualizar Nro. de Transferencia.";
                                    System.out.println("No se pudo actualizar Nro. de Transferencia. id valorpago: "+nuevoValorPago.getC_VALORPAGO_ID());                                 
                                    return DocAction.STATUS_Drafted;
                                }
                            }
                        }                         
                }
                

                // Procesar los valores

                
                
                try {

                    /*
                    *  Zynnia 17/01/2013
                    *  Modificacion para que procese tambien los valores en las cobranzas
                    *  cuando la misma se completa.
                    *  Pablo Velazquez
                    */
                    
                    if ( isReceipt() ) {
                        DB.executeUpdate("UPDATE C_PAYMENTVALORES SET Processed='Y'" +
                                    " WHERE C_Payment_ID = " + getC_Payment_ID(),get_TrxName());
                    }
                    else {
                        /*
                        *  Zynnia 08/01/2013
                        *  Modificación para que procese todos los valores independientemente del tipo
                        *  de esta manera los valores pasan a no ser pausibles de modificaciones luego de que
                        *  el pago se completa.
                        *  Jose Fantasia
                        */

                        DB.executeUpdate("UPDATE C_VALORPAGO SET Processed='Y'" +
                                        " WHERE C_Payment_ID = " + getC_Payment_ID(),get_TrxName());
                    }
                } catch (Exception e) {
                    // Que pasa si falla ver !!
                };
                
                
                // Procesar las retenciones

                try {

                    /*
                    *  Zynnia 14/01/2013
                    *  Modificación para que procese las retenciones
                    *  de esta manera las retenciones pasan a no ser pausibles de modificaciones luego de que
                    *  el pago se completa.
                    *  Pablo Velazquez
                    */

                    DB.executeUpdate("UPDATE C_paymentret SET Processed='Y'" +
                                    " WHERE C_Payment_ID = " + getC_Payment_ID(),get_TrxName());

                } catch (Exception e) {
                    // Que pasa si falla ver !!
                };

		/*
		 * 	COBRANZA:
		 * 			La cotización se ingresa en la ventana.
		 * 	PAGO:
		 * 			La cotización es: - Uno, si se trabaja en moneda nacional.
		 * 							  - Tasa del Sistema, si no se trabaja en moneda nacional.
		 */
		if (!isReceipt())
		{
			MAcctSchema acct = new MAcctSchema(getCtx(),Env.getContextAsInt(getCtx(), "$C_AcctSchema_ID"),get_TrxName());
			if (acct.getC_Currency_ID()==getC_Currency_ID())
				setCotizacion(BigDecimal.ONE);
			else
				setCotizacion(TasaCambio.rate(getC_Currency_ID(), acct.getC_Currency_ID(), getDateTrx(), getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID()));
		}
                  
                
		setProcessed(true);
		setDocAction(DOCACTION_Close);
                
		return DocAction.STATUS_Completed;
	}	//	completeIt

	/**
	 * 	Create Counter Document
	 */
	private MPayment createCounterDoc()
	{
		//	Is this a counter doc ?
		if (getRef_Payment_ID() != 0)
			return null;

		//	Org Must be linked to BPartner
		MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
		int counterC_BPartner_ID = org.getLinkedC_BPartner_ID();
		if (counterC_BPartner_ID == 0)
			return null;
		//	Business Partner needs to be linked to Org
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), null);
		int counterAD_Org_ID = bp.getAD_OrgBP_ID_Int();
		if (counterAD_Org_ID == 0)
			return null;

		MBPartner counterBP = new MBPartner (getCtx(), counterC_BPartner_ID, null);
		MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID);
		log.info("Counter BP=" + counterBP.getName());

		//	Document Type
		int C_DocTypeTarget_ID = 0;
		MDocTypeCounter counterDT = MDocTypeCounter.getCounterDocType(getCtx(), getC_DocType_ID());
		if (counterDT != null)
		{
			log.fine(counterDT.toString());
			if (!counterDT.isCreateCounter() || !counterDT.isValid())
				return null;
			C_DocTypeTarget_ID = counterDT.getCounter_C_DocType_ID();
		}
		else	//	indirect
		{
			C_DocTypeTarget_ID = MDocTypeCounter.getCounterDocType_ID(getCtx(), getC_DocType_ID());
			log.fine("Indirect C_DocTypeTarget_ID=" + C_DocTypeTarget_ID);
			if (C_DocTypeTarget_ID <= 0)
				return null;
		}

		//	Deep Copy
		MPayment counter = new MPayment (getCtx(), 0, get_TrxName());
		counter.setAD_Org_ID(counterAD_Org_ID);
		counter.setC_BPartner_ID(counterBP.getC_BPartner_ID());
		counter.setIsReceipt(!isReceipt());
		counter.setC_DocType_ID(C_DocTypeTarget_ID);
		counter.setTrxType(getTrxType());
		counter.setTenderType(getTenderType());
		//
		counter.setPayAmt(getPayAmt());
		counter.setDiscountAmt(getDiscountAmt());
		counter.setTaxAmt(getTaxAmt());
		counter.setWriteOffAmt(getWriteOffAmt());
		counter.setIsOverUnderPayment (isOverUnderPayment());
		counter.setOverUnderAmt(getOverUnderAmt());
		counter.setC_Currency_ID(getC_Currency_ID());
		counter.setC_ConversionType_ID(getC_ConversionType_ID());
		//
		counter.setDateTrx (getDateTrx());
		counter.setDateAcct (getDateAcct());
		counter.setRef_Payment_ID(getC_Payment_ID());
		//
		String sql = "SELECT C_BankAccount_ID FROM C_BankAccount "
			+ "WHERE C_Currency_ID=? AND AD_Org_ID IN (0,?) AND IsActive='Y' "
			+ "ORDER BY IsDefault DESC";
		int C_BankAccount_ID = DB.getSQLValue(get_TrxName(), sql, getC_Currency_ID(), counterAD_Org_ID);
		counter.setC_BankAccount_ID(C_BankAccount_ID);

		//	Refernces
		counter.setC_Activity_ID(getC_Activity_ID());
		counter.setC_Campaign_ID(getC_Campaign_ID());
		counter.setC_Project_ID(getC_Project_ID());
		counter.setUser1_ID(getUser1_ID());
		counter.setUser2_ID(getUser2_ID());
		counter.save(get_TrxName());
		log.fine(counter.toString());
		setRef_Payment_ID(counter.getC_Payment_ID());

		//	Document Action
		if (counterDT != null)
		{
			if (counterDT.getDocAction() != null)
			{
				counter.setDocAction(counterDT.getDocAction());
				counter.processIt(counterDT.getDocAction());
				counter.save(get_TrxName());
			}
		}
		return counter;
	}	//	createCounterDoc

	/**
	 * 	Allocate It.
	 * 	Only call when there is NO allocation as it will create duplicates.
	 * 	If an invoice exists, it allocates that
	 * 	otherwise it allocates Payment Selection.
	 *	@return true if allocated
	 */
	public boolean allocateIt()
	{

		//	Create invoice Allocation -	See also MCash.completeIt
		if (getC_Invoice_ID() != 0)
			return allocateInvoice();
		//	Invoices of a AP Payment Selection
		if (allocatePaySelection())
			return true;

		if (getC_Order_ID() != 0)
			return false;

		//	Allocate to multiple Payments based on entry
		MPaymentAllocate[] pAllocs = MPaymentAllocate.get(this);
		if (pAllocs.length == 0)
			return false;

		/**
		 *
		 * 		Modificación para diferenciar
		 *	cobros/pagos en Consulta de Asignación
		 *
		 */

		MAllocationHdr alloc;

		if (isReceipt())
		{
			alloc = new MAllocationHdr(getCtx(), false, getDateTrx(), getC_Currency_ID(),
					Msg.translate(getCtx(), "IsReceipt")	+ ": " + getDocumentNo(), get_TrxName());
		}
		else
		{
			alloc = new MAllocationHdr(getCtx(), false, getDateTrx(), getC_Currency_ID(),
				Msg.translate(getCtx(), "C_Payment_ID")	+ ": " + getDocumentNo(), get_TrxName());
		}
		alloc.setAD_Org_ID(getAD_Org_ID());

		/**
		 * 25/11/2010 Camarzana Mariano
		 * Estaba tomando mal el nombre de la transaccion
		 * usaba getTrxType() en vez de get_TrxName()
		 *
		 */

		if (!alloc.save(get_TrxName()))
		{
			log.severe("P.Allocations not created");
			return false;
		}
		//	Lines
		for (int i = 0; i < pAllocs.length; i++)
		{
			MPaymentAllocate pa = pAllocs[i];
			MAllocationLine aLine = null;
                        
                        /*
                         *  30/05/2012
                         *  Zynnia - agregado para que actualice el campo pagado
                         *  JF
                         * 
                         */

                        String sql = "SELECT invoiceOpen(C_Invoice_ID, 0) "
					+ "FROM C_Invoice WHERE C_Invoice_ID=?";
                        BigDecimal open = DB.getSQLValueBD(null, sql, pa.getC_Invoice_ID());
                        
                        if (open.compareTo(Env.ZERO) == 0){
                            MInvoice invoice = new MInvoice(getCtx(),pa.getC_Invoice_ID(),get_TrxName());
                            invoice.setIsPaid(true);
                            if (!invoice.save(get_TrxName())){
                                log.severe("No se pudo cambiar a estado Pagado la factura "+ invoice.getDocumentNo());
                                return false;
                            }
                        }     
                        
                        
			if (isReceipt())
				aLine = new MAllocationLine (alloc, pa.getAmount(),
					pa.getDiscountAmt(), pa.getWriteOffAmt(), pa.getOverUnderAmt());
			else
				aLine = new MAllocationLine (alloc, pa.getAmount().negate(),
					pa.getDiscountAmt().negate(), pa.getWriteOffAmt().negate(), pa.getOverUnderAmt().negate());
			aLine.setDocInfo(pa.getC_BPartner_ID(), 0, pa.getC_Invoice_ID());
			aLine.setPaymentInfo(getC_Payment_ID(), 0);
			if (!aLine.save(get_TrxName()))
				log.warning("P.Allocations - line not saved");
			else
			{
				pa.setC_AllocationLine_ID(aLine.getC_AllocationLine_ID());
				pa.save(get_TrxName());
			}
		}
		//	Should start WF
		alloc.processIt(DocAction.ACTION_Complete);
		m_processMsg = "@C_AllocationHdr_ID@: " + alloc.getDocumentNo();
		return alloc.save(get_TrxName());
	}	//	allocateIt

	/**
	 * 	Allocate single AP/AR Invoice
	 */
	private boolean allocateInvoice()
	{
		//	calculate actual allocation
		BigDecimal allocationAmt = getPayAmt();			//	underpayment

		//		DANIEL -- 2do.
		float pay = getPayAmt().floatValue();
		//

		if (getOverUnderAmt().signum() < 0 && getPayAmt().signum() > 0)
			allocationAmt = allocationAmt.add(getOverUnderAmt());	//	overpayment (negative)

		/**
		 *
		 * 		Modificación para diferenciar
		 *	cobros/pagos en Consulta de Asignación
		 *
		 */

		MAllocationHdr alloc;

		if (isReceipt())
		{
			alloc = new MAllocationHdr(getCtx(), false,	getDateTrx(), getC_Currency_ID(),
					Msg.translate(getCtx(), "IsReceipt") + ": " + getDocumentNo() + " [1]", get_TrxName());
		}
		else
		{
			alloc = new MAllocationHdr(getCtx(), false,	getDateTrx(), getC_Currency_ID(),
			Msg.translate(getCtx(), "C_Payment_ID") + ": " + getDocumentNo() + " [1]", get_TrxName());
		}

		alloc.setAD_Org_ID(getAD_Org_ID());
		if (!alloc.save())
		{
			log.log(Level.SEVERE, "Could not create Allocation Hdr");
			return false;
		}
		MAllocationLine aLine = null;
		if (isReceipt())
			aLine = new MAllocationLine (alloc, allocationAmt,
				getDiscountAmt(), getWriteOffAmt(), getOverUnderAmt());
		else
			aLine = new MAllocationLine (alloc, allocationAmt.negate(),
				getDiscountAmt().negate(), getWriteOffAmt().negate(), getOverUnderAmt().negate());
		aLine.setDocInfo(getC_BPartner_ID(), 0, getC_Invoice_ID());
		aLine.setC_Payment_ID(getC_Payment_ID());
		if (!aLine.save(get_TrxName()))
		{
			log.log(Level.SEVERE, "Could not create Allocation Line");
			return false;
		}
		//	Should start WF
		alloc.processIt(DocAction.ACTION_Complete);
		alloc.save(get_TrxName());
		m_processMsg = "@C_AllocationHdr_ID@: " + alloc.getDocumentNo();

		//	Get Project from Invoice
		int C_Project_ID = DB.getSQLValue(get_TrxName(),
			"SELECT MAX(C_Project_ID) FROM C_Invoice WHERE C_Invoice_ID=?", getC_Invoice_ID());
		if (C_Project_ID > 0 && getC_Project_ID() == 0)
			setC_Project_ID(C_Project_ID);
		else if (C_Project_ID > 0 && getC_Project_ID() > 0 && C_Project_ID != getC_Project_ID())
			log.warning("Invoice C_Project_ID=" + C_Project_ID
				+ " <> Payment C_Project_ID=" + getC_Project_ID());
		return true;
	}	//	allocateInvoice

	/**
	 * 	Allocate Payment Selection
	 */
	private boolean allocatePaySelection()
	{
		/**
		 *
		 * 		Modificación para diferenciar
		 *	cobros/pagos en Consulta de Asignación
		 *
		 */

		MAllocationHdr alloc;

		if (isReceipt())
		{
			alloc = new MAllocationHdr(getCtx(), false, getDateTrx(), getC_Currency_ID(),
					Msg.translate(getCtx(), "IsReceipt")	+ ": " + getDocumentNo() + " [n]", get_TrxName());
		}
		else
		{
			alloc = new MAllocationHdr(getCtx(), false, getDateTrx(), getC_Currency_ID(),
					Msg.translate(getCtx(), "C_Payment_ID")	+ ": " + getDocumentNo() + " [n]", get_TrxName());
		}
		alloc.setAD_Org_ID(getAD_Org_ID());

		String sql = "SELECT psc.C_BPartner_ID, psl.C_Invoice_ID, psl.IsSOTrx, "	//	1..3
			+ " psl.PayAmt, psl.DiscountAmt, psl.DifferenceAmt, psl.OpenAmt "
			+ "FROM C_PaySelectionLine psl"
			+ " INNER JOIN C_PaySelectionCheck psc ON (psl.C_PaySelectionCheck_ID=psc.C_PaySelectionCheck_ID) "
			+ "WHERE psc.C_Payment_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_Payment_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int C_BPartner_ID = rs.getInt(1);
				int C_Invoice_ID = rs.getInt(2);
				if (C_BPartner_ID == 0 && C_Invoice_ID == 0)
					continue;
				boolean isSOTrx = "Y".equals(rs.getString(3));
				BigDecimal PayAmt = rs.getBigDecimal(4);
				BigDecimal DiscountAmt = rs.getBigDecimal(5);
				BigDecimal WriteOffAmt = rs.getBigDecimal(6);
				BigDecimal OpenAmt = rs.getBigDecimal(7);
				BigDecimal OverUnderAmt = OpenAmt.subtract(PayAmt)
					.subtract(DiscountAmt).subtract(WriteOffAmt);
				//
				if (alloc.get_ID() == 0 && !alloc.save(get_TrxName()))
				{
					log.log(Level.SEVERE, "Could not create Allocation Hdr");
					rs.close();
					pstmt.close();
					return false;
				}
				MAllocationLine aLine = null;
				if (isSOTrx)
					aLine = new MAllocationLine (alloc, PayAmt,
						DiscountAmt, WriteOffAmt, OverUnderAmt);
				else
					aLine = new MAllocationLine (alloc, PayAmt.negate(),
						DiscountAmt.negate(), WriteOffAmt.negate(), OverUnderAmt.negate());
				aLine.setDocInfo(C_BPartner_ID, 0, C_Invoice_ID);
				aLine.setC_Payment_ID(getC_Payment_ID());
				if (!aLine.save(get_TrxName()))
					log.log(Level.SEVERE, "Could not create Allocation Line");
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "allocatePaySelection", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}

		//	Should start WF
		boolean ok = true;
		if (alloc.get_ID() == 0)
		{
			log.fine("No Allocation created - C_Payment_ID="
				+ getC_Payment_ID());
			ok = false;
		}
		else
		{
			alloc.processIt(DocAction.ACTION_Complete);
			ok = alloc.save(get_TrxName());
			m_processMsg = "@C_AllocationHdr_ID@: " + alloc.getDocumentNo();
		}
		return ok;
	}	//	allocatePaySelection

	/**
	 * 	De-allocate Payment.
	 * 	Unkink Invoices and Orders and delete Allocations
	 */
	private void deAllocate()
	{
		if (getC_Order_ID() != 0)
			setC_Order_ID(0);
	//	if (getC_Invoice_ID() == 0)
	//		return;
		//	De-Allocate all
		MAllocationHdr[] allocations = MAllocationHdr.getOfPayment(getCtx(),
			getC_Payment_ID(), get_TrxName());
		log.fine("#" + allocations.length);
		for (int i = 0; i < allocations.length; i++)
		{
			allocations[i].set_TrxName(get_TrxName());
			allocations[i].setDocAction(DocAction.ACTION_Reverse_Correct);
			allocations[i].processIt(DocAction.ACTION_Reverse_Correct);
			allocations[i].save();
		}

		// 	Unlink (in case allocation did not get it)
		if (getC_Invoice_ID() != 0)
		{
			//	Invoice
			String sql = "UPDATE C_Invoice "
				+ "SET C_Payment_ID = NULL, IsPaid='N' "
				+ "WHERE C_Invoice_ID=" + getC_Invoice_ID()
				+ " AND C_Payment_ID=" + getC_Payment_ID();
			int no = DB.executeUpdate(sql, get_TrxName());
			if (no != 0)
				log.fine("Unlink Invoice #" + no);
			//	Order
			sql = "UPDATE C_Order o "
				+ "SET C_Payment_ID = NULL "
				+ "WHERE EXISTS (SELECT * FROM C_Invoice i "
					+ "WHERE o.C_Order_ID=i.C_Order_ID AND i.C_Invoice_ID=" + getC_Invoice_ID() + ")"
				+ " AND C_Payment_ID=" + getC_Payment_ID();
			no = DB.executeUpdate(sql, get_TrxName());
			if (no != 0)
				log.fine("Unlink Order #" + no);
		}
		//
		setC_Invoice_ID(0);
		setIsAllocated(false);
	}	//	deallocate

	/**
	 * 	Void Document.
	 * 	@return true if success
	 */
	public boolean voidIt()
	{
		log.info(toString());
		if (DOCSTATUS_Closed.equals(getDocStatus())
			|| DOCSTATUS_Reversed.equals(getDocStatus())
			|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
			setDocAction(DOCACTION_None);
			return false;
		}
		//	If on Bank Statement, don't void it - reverse it
		if (getC_BankStatementLine_ID() > 0)
			return reverseCorrectIt();

		//	Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus())
			|| DOCSTATUS_Invalid.equals(getDocStatus())
			|| DOCSTATUS_InProgress.equals(getDocStatus())
			|| DOCSTATUS_Approved.equals(getDocStatus())
			|| DOCSTATUS_NotApproved.equals(getDocStatus()) )
		{
			addDescription(Msg.getMsg(getCtx(), "Voided") + " (" + getPayAmt() + ")");
			setPayAmt(Env.ZERO);
			setDiscountAmt(Env.ZERO);
			setWriteOffAmt(Env.ZERO);
			setOverUnderAmt(Env.ZERO);
			setIsAllocated(false);
			//	Unlink & De-Allocate
			deAllocate();
		}
		else
		{
			/*
			 * 01-03-2011 Camarzana Mariano
			 * Modificacion realizada para que no me cambie el estado en caso de que falle una de las verficacioes
			 */
			if (isReceipt()){
				if (verificarAnularCobranza())
					{
						// Eliminar Valores
						deleteValoresCobranza();
						// Eliminar Retenciones
						deleteRetenciones();
						// Eliminar Asignaciones
						deleteAllocations();
						// Eliminar la contabilidad generada para el comprobante
						anularContabilidad();
                                               /*
                                                 *  09/04/2013 María Jesús Martín 
                                                 *  Eliminar movimientos de conciliación
                                                 */
                                                eliminarMovimientosConciliacion(getC_Payment_ID());

						// Actualizar saldo del socio de negocio en una anulación
						if (!actualizarBPartnerVoid(false))
						{
							m_processMsg = "@No se puede Actualizar el Saldo del Socio de Negocio@";
							setDocAction(DOCACTION_Close);
							return false;
						}
						setProcessed(true);
						setIsAllocated(false);
						//setPosted(false);
						setIsApproved(false);
						setDocStatus(DOCSTATUS_Voided);
						setDocAction(DOCACTION_None);
					}
				else
					{ 
					  setDocAction(DOCACTION_Close);
					  return false;
					}
			}
			else
				if (!isReceipt()){
					if (verificarAnularPago())
						{
							// Eliminar Valores
							deleteValoresPago();

							// Eliminar Retenciones
							deleteRetenciones();
							// Eliminar Asignaciones
							deleteAllocations();
							// Eliminar la contabilidad generada para el comprobante
							anularContabilidad();
                                                        
                                                        /*
                                                         *  Zynnia 24/05/2012
                                                         *  Eliminar movimientos de conciliación
                                                         * 
                                                         * 
                                                         */
                                                        
                                                        
							eliminarMovimientosConciliacion(getC_Payment_ID());

							// Actualizar saldo del socio de negocio en una anulación
							if (!actualizarBPartnerVoid(true))
							{
								m_processMsg = "@No se puede Actualizar el Saldo del Socio de Negocio@";
								setDocAction(DOCACTION_Close);
								return false;
							}
							setProcessed(true);
							setIsAllocated(false);
							//setPosted(false);
							setIsApproved(false);
							setDocStatus(DOCSTATUS_Voided);
							setDocAction(DOCACTION_None);
						}
					else
						{ 
						  setDocAction(DOCACTION_Close);
						  return false;
						}
				}
		}
		return true;
	}	//	voidIt

	/**
	 * 	Close Document.
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		log.info(toString());
		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correction
	 * 	@return true if success
	 */
	public boolean reverseCorrectIt()
	{
		log.info(toString());

		//	Std Period open?
		Timestamp dateAcct = getDateAcct();
		if (!MPeriod.isOpen(getCtx(), dateAcct,
			isReceipt() ? MDocType.DOCBASETYPE_ARReceipt : MDocType.DOCBASETYPE_APPayment))
			dateAcct = new Timestamp(System.currentTimeMillis());

		//	Auto Reconcile if not on Bank Statement
		boolean reconciled = false; //	getC_BankStatementLine_ID() == 0;

		//	Create Reversal
		MPayment reversal = new MPayment (getCtx(), 0, get_TrxName());
		copyValues(this, reversal);
		reversal.setClientOrg(this);
		reversal.setC_Order_ID(0);
		reversal.setC_Invoice_ID(0);
		reversal.setDateAcct(dateAcct);
		//
		reversal.setDocumentNo(getDocumentNo() + REVERSE_INDICATOR);	//	indicate reversals
		reversal.setDocStatus(DOCSTATUS_Drafted);
		reversal.setDocAction(DOCACTION_Complete);
		//
		reversal.setPayAmt(getPayAmt().negate());
		reversal.setDiscountAmt(getDiscountAmt().negate());
		reversal.setWriteOffAmt(getWriteOffAmt().negate());
		reversal.setOverUnderAmt(getOverUnderAmt().negate());
		//
		reversal.setIsAllocated(true);
		reversal.setIsReconciled(reconciled);	//	to put on bank statement
		reversal.setIsOnline(false);
		reversal.setIsApproved(true);
		reversal.setR_PnRef(null);
		reversal.setR_Result(null);
		reversal.setR_RespMsg(null);
		reversal.setR_AuthCode(null);
		reversal.setR_Info(null);
		reversal.setProcessing(false);
		reversal.setOProcessing("N");
		reversal.setProcessed(false);
		reversal.setPosted(false);
		reversal.setDescription(getDescription());
		reversal.addDescription("{->" + getDocumentNo() + ")");
		reversal.save(get_TrxName());
		//	Post Reversal
		if (!reversal.processIt(DocAction.ACTION_Complete))
		{
			m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
			return false;
		}
		reversal.closeIt();
		reversal.setDocStatus(DOCSTATUS_Reversed);
		reversal.setDocAction(DOCACTION_None);
		reversal.save(get_TrxName());

		//	Unlink & De-Allocate
		deAllocate();
		setIsReconciled (reconciled);
		setIsAllocated (true);	//	the allocation below is overwritten
		//	Set Status
		addDescription("(" + reversal.getDocumentNo() + "<-)");
		setDocStatus(DOCSTATUS_Reversed);
		setDocAction(DOCACTION_None);
		setProcessed(true);

		//	Create automatic Allocation
		MAllocationHdr alloc = new MAllocationHdr (getCtx(), false,
			getDateTrx(), getC_Currency_ID(),
			Msg.translate(getCtx(), "C_Payment_ID")	+ ": " + reversal.getDocumentNo(), get_TrxName());
		alloc.setAD_Org_ID(getAD_Org_ID());
		if (!alloc.save())
			log.warning("Automatic allocation - hdr not saved");
		else
		{
			//	Original Allocation
			MAllocationLine aLine = new MAllocationLine (alloc, getPayAmt(true),
				Env.ZERO, Env.ZERO, Env.ZERO);
			aLine.setDocInfo(getC_BPartner_ID(), 0, 0);
			aLine.setPaymentInfo(getC_Payment_ID(), 0);
			if (!aLine.save(get_TrxName()))
				log.warning("Automatic allocation - line not saved");
			//	Reversal Allocation
			aLine = new MAllocationLine (alloc, reversal.getPayAmt(true),
				Env.ZERO, Env.ZERO, Env.ZERO);
			aLine.setDocInfo(reversal.getC_BPartner_ID(), 0, 0);
			aLine.setPaymentInfo(reversal.getC_Payment_ID(), 0);
			if (!aLine.save(get_TrxName()))
				log.warning("Automatic allocation - reversal line not saved");
		}
		alloc.processIt(DocAction.ACTION_Complete);
		alloc.save(get_TrxName());
		//
		StringBuffer info = new StringBuffer (reversal.getDocumentNo());
		info.append(" - @C_AllocationHdr_ID@: ").append(alloc.getDocumentNo());

		//	Update BPartner
		if (getC_BPartner_ID() != 0)
		{
			MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
			bp.setTotalOpenBalance();
			bp.save(get_TrxName());
		}

                //begin e-evolution vpj-cd
                if(!isReceipt())
                {
                    MPaySelectionCheck psc = MPaySelectionCheck.getOfPayment(getCtx(), this.getC_Payment_ID(),null);
                    // Begin Add ogi-cd 2/Ago/2006
                    	if (psc == null)
                    		return true;
                    // End Add ogi-cd 2/Ago/2006

                    if(!psc.delete(true))
                    {
                    	m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
                    	return false;
                    }


                    MPaySelectionLine[] psls = psc.getPaySelectionLines(true);
                    for (int i = 0; i < psls.length; i++)
                    {
                         if(!psls[i].delete(true))
                         {
                           m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
                            return false;
                         }
                    }



                }
                //end e-evolution vpj-cd

		m_processMsg = info.toString();
		return true;
	}	//	reverseCorrectionIt


	/**
	 * 	REQ-038
	 *
	 * 		Cambio de Estado de Cheques
	 *
	 */
	private void CQChangeState(String origen, String destino)
	{
		if (!isReceipt())
	    {
			String sql = "SELECT C_VALORPAGO_ID FROM C_VALORPAGO WHERE TIPO='P' AND STATE=? AND C_PAYMENT_ID=?";

			try{

				PreparedStatement pstm = DB.prepareStatement(sql, get_TrxName());
				pstm.setString(1, origen);
				pstm.setInt(2, getC_Payment_ID());
				ResultSet rs = pstm.executeQuery();

				int C_VALORPAGO_ID;

				while (rs.next())
				{
					C_VALORPAGO_ID = rs.getInt(1);
					MVALORPAGO payval = new MVALORPAGO(getCtx(), C_VALORPAGO_ID, get_TrxName());
					payval.setEstado(destino);
					payval.save(get_TrxName());
				}
			}	catch(Exception e){}
	    }
	}


	/**
	 * 	Get Bank Statement Line of payment or 0
	 *	@return id or 0
	 */
	private int getC_BankStatementLine_ID()
	{
		String sql = "SELECT C_BankStatementLine_ID FROM C_BankStatementLine WHERE C_Payment_ID=?";
		int id = DB.getSQLValue(get_TrxName(), sql, getC_Payment_ID());
		if (id < 0)
			return 0;
		return id;
	}	//	getC_BankStatementLine_ID

	/**
	 * 	Reverse Accrual - none
	 * 	@return true if success
	 */
	public boolean reverseAccrualIt()
	{
		log.info(toString());

		if (!isReceipt())
                {
                	CQChangeState(MVALORPAGO.EMITIDO,MVALORPAGO.REVERTIDO);
                        CQChangeState(MVALORPAGO.IMPRESO,MVALORPAGO.REVERTIDO);

                }

		return false;
	}	//	reverseAccrualIt

	private void deleteValoresPago()
	{
		// Cambiar Valores del Pago
		CQChangeState(MVALORPAGO.EMITIDO,MVALORPAGO.ANULADO);
		CQChangeState(MVALORPAGO.IMPRESO,MVALORPAGO.ANULADO);
	}

	private void deleteValoresCobranza()
	{
		// Eliminar Valores de la Cobranza
		/*List<MPAYMENTVALORES> lVal = getValoresCobranza();
		if (lVal!=null)
			for (int i=0; i<lVal.size(); i++)
	    	{
				MPAYMENTVALORES payval = (MPAYMENTVALORES)lVal.get(i);
	    		payval.delete(true);
	    	}*/
	}

/*	private void deleteRetencionesCobranza()
	{
		//	 Eliminar Retenciones
		List<MCOBRANZARET> lRet = getRetenciones();
		if (lRet!=null)
			for (int i=0; i<lRet.size(); i++)
	    	{
				MCOBRANZARET cobret = (MCOBRANZARET)lRet.get(i);
				cobret.delete(true);
	    	}
	}
*/
	private void deleteRetenciones()
	{
		//	 Eliminar Retenciones
		List<PO> lRet = getRetenciones();
		PO ret;

		if (lRet!=null)
			for (int i=0; i<lRet.size(); i++)
	    	{
				if (isReceipt())
					ret = (MCOBRANZARET)lRet.get(i);
				else
					ret = (MPAYMENTRET)lRet.get(i);
				ret.delete(true);
	    	}
	}

	/**
	 * 	Re-activate
	 * 	@return true if success
	 */
	public boolean reActivateIt(){
            
		log.info(toString());

		if (isReceipt()){
                    
                    /*
                    * GENEOS - Pablo Velazquez
                    * 14/03/2014
                    * Solo se le permite reactivar cobranzas al rol Panalab Admin
                    */
                    int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
                    int[] roleid = MRole.getAllIDs(MRole.Table_Name, " name = 'Panalab Admin' ", get_TrxName());
                    
                    
                    if ( roleid.length>0 && AD_Role_ID != roleid[0]) {
                        /*              
                        *  22/11/2013 Maria Jesus
                        *  No se permite reactivar las cobranzas.
                        */
                       m_processMsg = "Solo al rol Panalab Admin se le permite reactivar una cobranza.";
                       return false;
                    }
                    
                    /*
                    *  Zynnia - Agregado por bug que no anula lo que se pasa a la conciliación.
                    *  JF - 15/08/2012
                    */ 
                    
                    try {
                        if(MMOVIMIENTOCONCILIACION.isConcCompleteForPayment(getC_Payment_ID())){
                            m_processMsg = "@La cobranza ya fue conciliada@";
                            setDocAction(DOCACTION_Close);
                            return false;
                        }
                            
                        MMOVIMIENTOCONCILIACION.deleteMov(getC_Payment_ID());

                    } catch (SQLException ex) {
                        Logger.getLogger(MPayment.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    }

                    // Eliminar Asignaciones
                    deleteAllocations();

                    // Eliminar la contabilidad generada para el comprobante
                    anularContabilidad();

                    // El cheque deja de estar procesado
                    reactivarChequesCobranza();


                    // Actualizar saldo del socio de negocio en una anulación
                    if (!actualizarBPartnerVoid(false))
                    {
                            m_processMsg = "@No se puede Actualizar el Saldo del Socio de Negocio@";
                            setDocAction(DOCACTION_Close);
                            return false;
                    }

                    setDocStatus(DOCSTATUS_Drafted);
                    setDocAction(DOCACTION_Complete);
                    setIsAllocated(false);
                    setProcessed(false);
                    setPosted(false);
                    setIsApproved(false);

                    return true;
		
                } else if (!isReceipt() && verificarAnularPago()){
                            
                    /*
                    *  Zynnia - Agregado por bug que no anula lo que se pasa a la conciliación.
                    *  JF - 15/08/2012
                    */

                    try {

                        MMOVIMIENTOCONCILIACION.deleteMov(getC_Payment_ID());

                    } catch (SQLException ex) {
                        Logger.getLogger(MPayment.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    }
                    //Eliminar Asignaciones
                    deleteAllocations();
                    // Eliminar Retenciones
                    deleteRetenciones();
                    // Eliminar la contabilidad generada para el comprobante
                    anularContabilidad();
                    // El cheque deja de estar procesado
                    reactivarChequesPago();

                    // Actualizar saldo del socio de negocio en una anulación
                    if (!actualizarBPartnerVoid(true))
                    {
                            m_processMsg = "@No se puede Actualizar el Saldo del Socio de Negocio@";
                            setDocAction(DOCACTION_Close);
                            return false;
                    }

                    setDocStatus(DOCSTATUS_Drafted);
                    setDocAction(DOCACTION_Complete);
                    setIsAllocated(false);
                    setProcessed(false);
                    setPosted(false);
                    setIsApproved(false);
                    
                    return true;
                }
		
                return false;

        }	//	reActivateIt

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPayment[");
		sb.append(get_ID()).append("-").append(getDocumentNo())
			.append(",Receipt=").append(isReceipt())
			.append(",PayAmt=").append(getPayAmt())
			.append(",Discount=").append(getDiscountAmt())
			.append(",WriteOff=").append(getWriteOffAmt())
			.append(",OverUnder=").append(getOverUnderAmt());
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.PAYMENT, getC_Payment_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF


	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		//	: Total Lines = 123.00 (#1)
		sb.append(": ")
			.append(Msg.translate(getCtx(),"PayAmt")).append("=").append(getPayAmt())
			.append(",").append(Msg.translate(getCtx(),"WriteOffAmt")).append("=").append(getWriteOffAmt());
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg

	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getCreatedBy();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return amount payment(AP) or write-off(AR)
	 */
	public BigDecimal getApprovalAmt()
	{
		if (isReceipt())
			return getWriteOffAmt();
		return getPayAmt();
	}	//	getApprovalAmt

        public void setFlagSave(boolean val) {
            this.flagSave = val;
        }

    /**
	 * 	Modificación REQ-034 (Parte 2).
	 *     	Calcular el Neto de cobranza.
	 *
	 *  Modificación con REQ-039
	 *  	Conversion de Moneda Extranjera
	 *
	 *  Modificación con REQ-065
	 *  	Re-Cálculo NETO y BRUTO
	 *
	 */
    public boolean updatepayment()
	{
		if (isReceipt())
		{
			try {

				BigDecimal montoVal = new BigDecimal(0);
				BigDecimal montoRet = new BigDecimal(0);

				/*
				 * 	El monto se saca de Convertido, no Importe.
				 * 	Se utilizan inversos las columnas y los campos para no tener que cambiar
				 * 	donde almacenar el importe en cheques de terceros y Movimiento de Fondos.
				 */
				String sql;

				if (isForeingCurrency())
					sql = "SELECT COALESCE(SUM(CONVERTIDO),0) " +
						 "FROM C_PaymentValores " +
			  			 "WHERE C_Payment_ID = ? AND IsActive = 'Y'";
				else
					sql = "SELECT COALESCE(SUM(IMPORTE),0) " +
					 "FROM C_PaymentValores " +
		  			 "WHERE C_Payment_ID = ? AND IsActive = 'Y'";

				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				pstmt.setLong(1, getC_Payment_ID());

				ResultSet rs = pstmt.executeQuery();

				if (rs.next())
					montoVal = rs.getBigDecimal(1);

				sql = "SELECT COALESCE(SUM(Importe),0) " +
	  			 	  "FROM C_COBRANZA_RET " +
	  			 	  "WHERE C_Payment_ID = ? AND IsActive = 'Y'";

				pstmt = DB.prepareStatement(sql, null);
				pstmt.setLong(1, getC_Payment_ID());

				rs = pstmt.executeQuery();

				if (rs.next())
					montoRet = rs.getBigDecimal(1);

                setPAYNET(montoVal);
                setPayAmt(montoVal.add(montoRet));

                save();
			}
			catch (Exception e){return false;}
		}
		return true;
	}	    //updatepayment

    public List<PO> getRetenciones()
	{
    	List<PO> listRet = new ArrayList<PO>();
		try	{

			if (isReceipt())
			{
				String sql = "SELECT C_COBRANZA_RET_ID " +
							 "FROM C_COBRANZA_RET " +
						     "WHERE C_Payment_ID = ? AND IsActive = 'Y'";

				PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setLong(1, getC_Payment_ID());

				ResultSet rs = pstmt.executeQuery();

				while (rs.next())
					listRet.add(new MCOBRANZARET(getCtx(),rs.getInt(1),get_TrxName()));
				rs.close();
				pstmt.close();
			}
			else
			{
				String sql = "SELECT C_PAYMENTRET_ID " +
							 "FROM C_PAYMENTRET " +
						     "WHERE C_Payment_ID = ? AND IsActive = 'Y'";

				PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setLong(1, getC_Payment_ID());

				ResultSet rs = pstmt.executeQuery();

				while (rs.next())
				{
					MPAYMENTRET ret = new MPAYMENTRET(getCtx(),rs.getInt(1),get_TrxName());
					if (ret.getC_PAYMENTRET_ID()!=0)
						listRet.add(ret);
				}
				rs.close();
				pstmt.close();
			}

		}
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}

		return listRet;
	}	// getRetenciones

	public List<MPAYMENTVALORES> getValoresCobranza()
	{
		List<MPAYMENTVALORES> listVal = new ArrayList<MPAYMENTVALORES>();
		try	{
			String sql = "SELECT C_PAYMENTVALORES_ID " +
						"FROM C_PAYMENTVALORES " +
						"WHERE C_Payment_ID = ? AND IsActive = 'Y'";

			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setLong(1, getC_Payment_ID());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next())
			{
				int C_PaymentValores_ID = rs.getInt(1);
				MPAYMENTVALORES payVal = new MPAYMENTVALORES(getCtx(),C_PaymentValores_ID,get_TrxName());
				listVal.add(payVal);
			}

		}
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}

		if (listVal.size()>0)
			return listVal;

		return null;
	}	// getValoresCobranza

	public List<MVALORPAGO> getValoresPago()
	{
		List<MVALORPAGO> listVal = new ArrayList<MVALORPAGO>();
		try	{
			String sql = "SELECT C_VALORPAGO_ID " +
						"FROM C_VALORPAGO " +
						"WHERE C_Payment_ID = ? AND IsActive = 'Y'";

			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setLong(1, getC_Payment_ID());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next())
			{
				int C_ValorPago_ID = rs.getInt(1);
				MVALORPAGO payVal = new MVALORPAGO(getCtx(),C_ValorPago_ID,get_TrxName());
				listVal.add(payVal);
			}

		}
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}

		return listVal;

                    
	}	// getValoresPago

	public List<MPaymentAllocate> getAllocate()
	{
		List<MPaymentAllocate> listAll = new ArrayList<MPaymentAllocate>();
		try	{
			String sql = "SELECT C_PaymentAllocate_ID " +
						"FROM C_PaymentAllocate " +
						"WHERE C_Payment_ID = ? AND IsActive = 'Y'";

			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setLong(1, getC_Payment_ID());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next())
				listAll.add(new MPaymentAllocate(getCtx(),rs.getInt(1),get_TrxName()));

		}
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}

		if (listAll.size()>0)
			return listAll;

		return null;
	}	// getAllocate

	public BigDecimal getAmountAllocate()
	{
		BigDecimal amount = Env.ZERO;

		List<MPaymentAllocate> payAll = getAllocate();
		if (payAll!=null)
		{	for (int i=0; i<payAll.size(); i++)
				amount = amount.add(payAll.get(i).getAmount());
		}

		return amount;
	}	// getAmountAllocate

	protected boolean verificarAnularCobranza() {
            
		log.info(toString());
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType()))
		{
			m_processMsg = "@PeriodClosed@";
			return false;
		}

		List chRecib = getChequesRecibidos();

		for (int i=0; i<chRecib.size(); i++)
                {
                        MPAYMENTVALORES payval = (MPAYMENTVALORES)chRecib.get(i);
                        if (!payval.getEstado().equals(MPAYMENTVALORES.CARTERA)){
                            m_processMsg = "@Existe un cheque ("+payval.getNroCheque()+") que no se encuentra En Cartera@";    
                            return false;
                        }
                }
                
                
                
                /*
                *  Anexo para verificar que la cobranza no sea parte de una conciliación cerrada
                *  JF - 15/08/2012
                */
                
                try {
                    
                    if(MMOVIMIENTOCONCILIACION.isConcCompleteForPayment(getC_Payment_ID())){
                            m_processMsg = "@La Cobranza ya fue conciliada@";
                            return false;
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(MPayment.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return true;
                
    }

	public boolean isRetencion() {
		return retencion;
	}

	public void setRetencion(boolean retencion) {
		this.retencion = retencion;
	}

	protected boolean verificarAnularPago()	{
		log.info(toString());
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType()))
		{
			m_processMsg = "@PeriodClosed@";
			return false;
		}

		List chProp = getChequesPropios();

		for (int i=0; i<chProp.size(); i++)
                {
                        MVALORPAGO payval = (MVALORPAGO)chProp.get(i);
                        if (!payval.getEstado().equals(MVALORPAGO.EMITIDO)&!payval.getEstado().equals(MVALORPAGO.IMPRESO)){
                            m_processMsg = "@Existe un cheque ("+payval.getNroCheque()+") que no se encuentra Emitido@";    
                            return false;
                        }
                }

                /*
                *  Anexo para verificar que el pago no sea parte de una conciliación cerrada
                *  JF - 15/08/2012
                */
                
                try {
                    
                    if(MMOVIMIENTOCONCILIACION.isConcCompleteForPayment(getC_Payment_ID())){
                            m_processMsg = "@El Pago ya fue conciliado@";    
                            return false;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MPayment.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return true;
                
    }

	public List getChequesRecibidos()
    {
    	List<MPAYMENTVALORES> list = getValoresCobranza();
    	List<MPAYMENTVALORES> result = new ArrayList<MPAYMENTVALORES>();

    	for (int i=0; i<list.size(); i++)
    	{
    		MPAYMENTVALORES payval = list.get(i);
    		if (payval.getTIPO().equals(MPAYMENTVALORES.CHEQUE))
    			result.add(payval);
    	}

	    return result;
    }

	public List getChequesPropios()
    {
    	List<MVALORPAGO> list = getValoresPago();
    	List<MVALORPAGO> result = new ArrayList<MVALORPAGO>();

    	if (list!=null)
	    	for (int i=0; i<list.size(); i++)
	    	{
	    		MVALORPAGO payval = list.get(i);
	    		if (payval.getTIPO().equals(MVALORPAGO.CHEQUEPROPIO))
	    			result.add(payval);
	    	}

	    return result;
    }

	public List getContabilidad(){
            
            List<MFactAcct> list = new ArrayList<MFactAcct>();

            String sql = "SELECT Fact_Acct_ID "
                            + " FROM Fact_Acct "
                            + " WHERE Record_ID=? AND AD_Table_ID=? ";

	    try
	    {
	    	PreparedStatement pstm = DB.prepareStatement(sql, get_TrxName());
	    	pstm.setInt(1,getC_Payment_ID());
	    	pstm.setInt(2,get_Table_ID());
	    	ResultSet rs = pstm.executeQuery();

		    while (rs.next())
		    {
		    	MFactAcct cont = new MFactAcct(getCtx(),rs.getInt(1),get_TrxName());
		    	list.add(cont);
		    }
	    }
	    catch (SQLException e){}

	    return list;
        }

	protected void anularContabilidad()	{
    	//	Acct Reversed (this)
		List fAcct = getContabilidad();
		for (int i = 0; i < fAcct.size(); i++)
			((MFactAcct)fAcct.get(i)).delete(true);
        }

	protected boolean actualizarBPartnerVoid(boolean add)	{

		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());

	    BigDecimal previo;
	    if (getC_BPartner_ID() != 0)
		{
			previo = bp.getTotalOpenBalance();
	        BigDecimal actual = Env.ZERO;

	        if (add)
	        	actual = previo.add(getPayAmt());
	        else
	        	actual = previo.subtract(getPayAmt());

	        bp.setTotalOpenBalance(actual);
	        bp.setSO_CreditUsed(actual);
		}

    	if (!bp.save())
    		return false;

    	return true;
	}

	protected void deleteAllocations()	{
	    //	 Eliminar Asignaciones

		MAllocationHdr[] allocations = MAllocationHdr.getOfPayment(getCtx(), getC_Payment_ID(), get_TrxName());
		if (allocations!=null)
		{	for (int i=0; i<allocations.length; i++)
			{
				MAllocationHdr allocation = allocations[i];

				// Eliminar Lineas de la Asignacion
				MAllocationLine[] allocationLines = allocation.getLines(true);
				for (int j=0; j<allocationLines.length; j++)
		    	{
					MAllocationLine allocationLine = allocationLines[j];

					//	Establecer la Factura como no paga
					MInvoice invoice = new MInvoice(getCtx(),allocationLine.getC_Invoice_ID(),get_TrxName());
					allocationLine.delete(false);
					invoice.setIsPaid(false);
					invoice.save(get_TrxName());
		    	}
				allocation.anularContabilidad();
				allocation.delete(false);
			}
		}

		MPaymentAllocate[] payall = MPaymentAllocate.get(this);
		if (payall!=null)
		{	for (int i=0; i<payall.length; i++)
			{
				payall[i].delete(false);
			}
		}
	}

	private void reactivarChequesCobranza()
	{
		List list = getChequesRecibidos();
		for (int i=0;i<list.size();i++)
		{
			((MPAYMENTVALORES)list.get(i)).setProcessed(false);
			((MPAYMENTVALORES)list.get(i)).save(get_TrxName());
		}
	}

	private void reactivarChequesPago()
	{
		List list = getChequesPropios();
		for (int i=0;i<list.size();i++)
		{
			((MVALORPAGO)list.get(i)).setProcessed(false);
			((MVALORPAGO)list.get(i)).save(get_TrxName());
		}
	}

        private void eliminarMovimientosConciliacion(int c_Payment_ID) {
            /*
             *  15/03/2013 Maria Jesus
             *  Si al momento de eliminar el registro de la conciliación este arroja un error
             *  debe avisar en el log y no continuar. 
             * 
             */
            
            String sql = "DELETE FROM C_MOVIMIENTOCONCILIACION where C_MOVIMIENTOCONCILIACION_ID in "
                    + "(SELECT C_MOVIMIENTOCONCILIACION_ID FROM C_MOVIMIENTOCONCILIACION mc "
                    + "INNER JOIN C_CONCILIACIONBANCARIA c on (mc.C_CONCILIACIONBANCARIA_ID = c.C_CONCILIACIONBANCARIA_ID) "
                    + "WHERE mc.C_PAYMENT_ID = " + c_Payment_ID + " AND c.DOCSTATUS = 'DR')";
            /*
             *  15/03/2013 Maria Jesus
             *  Si al momento de eliminar el registro de la conciliación este arroja un error
             *  debe avisar en el log y no continuar. 
             * 
             */
            
            try{
                DB.executeUpdate(sql, null);    
            }
            catch (Exception ex) {
                    Logger.getLogger(MPayment.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        /* Devuelve el numero de cheque correspondiente para una cuenta bancaria.
         * "00000000" si la chequera esta completa
         *  null en caso de error
        */
        public String generateCheck (int bankAccountID)
	{
            //	Get Info from DB
            int key = bankAccountID;

            String sql = "SELECT CURRENTNEXT,HASTA FROM C_BankAccountDoc WHERE C_BankAccount_ID = ? and IsActive='Y'";

            try	{

                    PreparedStatement pstm = DB.prepareStatement(sql, null);
                    pstm.setInt(1, key);

                    ResultSet rs = pstm.executeQuery();

                    //	Set Info to Tab
                    if (rs.next())
                    {
                        int next = rs.getInt(1);
                        int to = rs.getInt(2);

                        if (next<=to)
                        {
                            String prefix = rs.getString(1);

                            switch (prefix.length()) {
                              case 1:
                                    prefix = "0000000" + prefix;
                                    break;
                              case 2:
                                    prefix = "000000" + prefix;
                                    break;
                              case 3:
                                    prefix = "00000" + prefix;
                                    break;
                              case 4:
                                    prefix = "0000" + prefix;
                                    break;
                              case 5:
                                    prefix = "000" + prefix;
                                    break;
                              case 6:
                                    prefix = "00" + prefix;
                                    break;
                              case 7:
                                    prefix = "0" + prefix;
                                    break;
                              case 8:
                                    break;
                              default:
                                    prefix = null;
                                    //JOptionPane.showMessageDialog(null,"Número de Cheque Incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            return prefix;
                           // mTab.setValue("NROCHEQUE", prefix);
                        }
                        else
                        {
                                return "00000000";
                                /*JOptionPane.showMessageDialog(null,"Actualice Documento de Cuenta Bancaria", "Chequera Completa", JOptionPane.INFORMATION_MESSAGE);
                                mTab.setValue("NROCHEQUE", "00000000");*/
                        }
                    }
                    else
                    {
                        return "00000000";
                        /*JOptionPane.showMessageDialog(null,"Actualice Documento de Cuenta Bancaria", "Chequera Completa", JOptionPane.INFORMATION_MESSAGE);					
                        mTab.setValue("NROCHEQUE", "00000000");*/
                    }
            }
            catch (Exception e) {
            return null;
            }
               
	}
        
        public String generateTransferencia(){
            //ID For C_VALORPAGO_TRANSFERENCIA = 5000048

            MSequence seq = new MSequence(getCtx(), 5000048, get_TrxName());
            
            return Integer.toString(seq.getCurrentNext());
        }
        
        

}   //  MPayment
