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
package org.compiere.wstore;

import java.io.*;
import java.math.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 *	Web Store Payment Entry & Confirmation
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: PaymentServlet.java,v 1.30 2005/11/28 03:34:21 jjanke Exp $
 */
public class PaymentServlet  extends HttpServlet
{
	/**	Logging						*/
	private static CLogger			log = CLogger.getCLogger(PaymentServlet.class);

	public static final String		ATTR_PAYMENT = "payment";

	/**
	 * 	Initialize global variables
	 *  @param config servlet configuration
	 *  @throws ServletException
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		if (!WebEnv.initWeb(config))
			throw new ServletException("PaymentServlet.init");
	}	//	init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	public String getServletInfo()
	{
		return "Compiere Payment Servlet";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	public void destroy()
	{
		log.info("");
	}   //  destroy

	
	/**************************************************************************
	 *  Process the initial HTTP Get request.
	 *  Reads the Parameter Amt and optional C_Invoice_ID
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("Get from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		Properties ctx = JSPEnv.getCtx(request);
		HttpSession session = request.getSession(true);
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
	//	WEnv.dump(session);
	//	WEnv.dump(request);

		//	Non existing user or Existing Web Payment
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		MPayment p = (MPayment)session.getAttribute (ATTR_PAYMENT);
		if (wu == null)
		{
			log.info ("No User");
			String url = "/index.jsp";
			log.info ("Forward to " + url);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
			dispatcher.forward (request, response);
		}

		//	Remove any open Order
		session.removeAttribute(WebOrder.NAME);
		//	Payment Amount
		String amtParam = WebUtil.getParameter (request, "Amt");
		if (amtParam == null || amtParam.length() == 0)
		{
			log.info ("No Payment Amount (" + amtParam + ")");
			doPost (request, response);
			return;
		}
		char[] chars = amtParam.toCharArray();
		StringBuffer sb = new StringBuffer();
		boolean decimal = false;
		for (int i = chars.length-1; i >=0; i--)
		{
			char c = chars[i];
			if (c == ',' || c == '.')
			{
				if (!decimal)
				{
					sb.insert (0, '.');
					decimal = true;
				}
			}
			else if (Character.isDigit(c))
				sb.insert(0,c);
		}
		BigDecimal amt = null;
		try
		{
				if (sb.length() > 0)
				{
					amt = new BigDecimal (sb.toString ());
					amt = amt.abs (); //	make it positive
				}
		}
		catch (Exception ex)
		{
			log.warning("Parsing Amount=" + amtParam + " (" + sb + ") - " + ex.toString());
		}
		//	Need to be positive amount
		if (amt == null || amt.compareTo(Env.ZERO) < 0)
		{
			log.info("No valid Payment Amount (" + amtParam + ") - " + amt);
			doPost (request, response);
			return;
		}

		String invoiceParam = WebUtil.getParameter (request, "C_Invoice_ID");
		int C_Invoice_ID = 0;
		try
		{
			if (invoiceParam != null)
				C_Invoice_ID = Integer.parseInt (invoiceParam);
		}
		catch (NumberFormatException ex)
		{
			log.warning("Parsing C_Invoice_ID=" + invoiceParam + " - " + ex.toString());
		}
		log.info("Amt=" + amt + ", C_Invoice_ID=" + C_Invoice_ID);

		//	Create New Payment for Amt & optional Invoice
		//	see OrderServlet.createPayment
		p = new MPayment(ctx, 0, null);
	//	p.setAD_Org_ID(..);
		p.setIsSelfService(true);
		p.setAmount(0, amt);			//	for CC selection ges default from Acct Currency
		p.setIsOnline(true);

		//	Sales CC Trx
		p.setC_DocType_ID(true);
		p.setTrxType(MPayment.TRXTYPE_Sales);
		p.setTenderType(MPayment.TENDERTYPE_CreditCard);
		//	Payment Info
		p.setC_Invoice_ID(C_Invoice_ID);
		//	BP Info
		p.setBP_BankAccount(wu.getBankAccount());
		//
	//	p.save();
		session.setAttribute (ATTR_PAYMENT, p);

		String url = "/paymentInfo.jsp";
		log.info ("Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
	}   //  doGet

	/**
	 *  Process the HTTP Post request.
	 * 	The actual payment processing
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("Post from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		Properties ctx = JSPEnv.getCtx(request);
		HttpSession session = request.getSession(true);
	//	WEnv.dump(session);
	//	WEnv.dump(request);

		//	Web User/Payment
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		MPayment p = (MPayment)session.getAttribute (ATTR_PAYMENT);
		WebOrder wo = (WebOrder)session.getAttribute (WebOrder.NAME);

		String url = null;
		if (wu == null || p == null)
			url = "/index.jsp";
		else if (processPayment(request, ctx, p, wu, wo))
			url = "/confirm.jsp";
		else
			url = "/paymentInfo.jsp";

		log.info ("Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
	}   //  doPost


	/**************************************************************************
	 * 	Process Payment.
	 * 	@param request request
	 * 	@param ctx context
	 * 	@param payment payment
	 * 	@param wu web user
	 * 	@param wo web order (optional)
	 *	@return true if processed
	 */
	private boolean processPayment(HttpServletRequest request, Properties ctx, 
		MPayment payment, WebUser wu, WebOrder wo)
	{
		boolean ok = processParameter(request, ctx, payment, wu);
		if (ok)
		{
			//	if negative amount - make it positive
			if (payment.getPayAmt().compareTo(Env.ZERO) < 0)
				payment.setPayAmt(payment.getPayAmt().abs());
			ok = payment.processOnline();
			if (ok)
			{
				//	Process Web Order and Set Invoice ID
				if (wo != null)
				{
					if (!wo.isCompleted())
						wo.process(payment);
					if (!wo.isCompleted())
						log.warning("Order not processed " + wo);
				}
				else
					log.warning("No Order");
				//	
				payment.processIt(DocAction.ACTION_Complete);
				payment.save();
				sendThanksEMail (request, ctx, payment, wu, wo);
			}
			else
			{
				log.fine(payment.getErrorMessage());
				String errMsg = payment.getErrorMessage();
				payment.save ();
				payment.setErrorMessage(errMsg);
				request.getSession().setAttribute(WebSessionCtx.HDR_MESSAGE, errMsg);
				//
				sendDeclineEMail(request, payment, wu, wo);
			}
		}
		return ok;
	}	//	processPayment

	/**
	 * 	Process Parameter and check them
	 * 	@param request request
	 * 	@param ctx context
	 * 	@param p payment
	 * 	@param wu web user
	 *	@return true if processed
	 */
	private boolean processParameter (HttpServletRequest request, Properties ctx, MPayment p, WebUser wu)
	{
		StringBuffer sb = new StringBuffer();
		p.setTenderType(MPayment.TENDERTYPE_CreditCard);
		p.setTrxType(MPayment.TRXTYPE_Sales);
		p.setA_EMail(wu.getEmail());
		//	CC & Number
		String ccType = WebUtil.getParameter (request, "CreditCard");
		p.setCreditCardType(ccType);
		String ccNumber = WebUtil.getParameter (request, "CreditCardNumber");
		p.setCreditCardNumber (ccNumber);
		String AD_Message = MPaymentValidate.validateCreditCardNumber(ccNumber, ccType);
		if (AD_Message.length() > 0)
			sb.append(Msg.getMsg(ctx, AD_Message)).append(" - ");

		//	Optional Verification Code
		String ccVV = WebUtil.getParameter (request, "CreditCardVV");
		p.setCreditCardVV(ccVV);
		if (ccVV != null && ccVV.length() > 0)
		{
			AD_Message = MPaymentValidate.validateCreditCardVV (ccVV, ccType);
			if (AD_Message.length () > 0)
				sb.append (Msg.getMsg (ctx, AD_Message)).append (" - ");
		}
		//	Exp
		int mm = WebUtil.getParameterAsInt(request, "CreditCardExpMM");
		p.setCreditCardExpMM (mm);
		int yy = WebUtil.getParameterAsInt(request, "CreditCardExpYY");
		p.setCreditCardExpYY (yy);
		AD_Message = MPaymentValidate.validateCreditCardExp(mm, yy);
		if (AD_Message.length() > 0)
			sb.append(Msg.getMsg(ctx, AD_Message)).append(" - ");

		//	Account Info
		String aName = WebUtil.getParameter (request, "A_Name");
		if (aName == null || aName.length() == 0)
			sb.append("Name - ");
		else
			p.setA_Name(aName);
		String aStreet = WebUtil.getParameter (request, "A_Street");
		p.setA_Street(aStreet);
		String aCity = WebUtil.getParameter (request, "A_City");
		if (aCity == null || aCity.length() == 0)
			sb.append("City - ");
		else
			p.setA_City(aCity);
		String aState = WebUtil.getParameter (request, "A_State");
		p.setA_State(aState);
		String aZip = WebUtil.getParameter (request, "A_Zip");
		if (aZip == null || aZip.length() == 0)
			sb.append("Zip - ");
		else
			p.setA_Zip(aZip);
		String aCountry = WebUtil.getParameter (request, "A_Country");
		p.setA_Country(aCountry);

		//	Error Message
		boolean ok = sb.length() == 0;
		p.setErrorMessage(sb.toString());		//	always set

		//	Save BP Bank Account
		if (ok)
		{
			String SP = "SavePayment";
			String SavePayment = WebUtil.getParameter (request, SP);
			if (SP.equals(SavePayment))
				p.saveToBP_BankAccount(wu.getBankAccount());
		}
		//
		return ok;
	}	//	processParameter


	/**
	 * 	Send Payment EMail.
	 * 	@param request request
	 * 	@param p payment
	 * 	@param wu web user
	 * 	@param wo optional web order
	 */
	private void sendThanksEMail (HttpServletRequest request, Properties ctx,
		MPayment p, WebUser wu, WebOrder wo)
	{
		StringBuffer message = new StringBuffer()
			.append(p.getPayAmt())
			.append(" (").append(Msg.getElement(ctx, "R_PnRef"))
			.append("=").append(p.getR_PnRef()).append(") ");
		if (wo != null)
			message.append("\n").append(Msg.getElement(ctx, "C_Order_ID"))
				.append(": ").append(wo.getDocumentNo());
		
		JSPEnv.sendEMail(request, wu, MMailMsg.MAILMSGTYPE_PaymentAcknowledgement,
			new Object[]{
				p.getDocumentNo() + " (" + p.getPayAmt() + ")",
				wu.getName(),
				message.toString()});
		//	SalesRep EMail
		if (wo != null && wo.getSalesRep_ID() != 0)
		{
			MClient client = MClient.get(ctx);
			client.sendEMail(wo.getSalesRep_ID(), 
				"(CC) Payment: " + p.getDocumentNo() + " (" + p.getPayAmt() + ")", 
				"Order: " + wo.getDocumentNo() 
				+ "\nUser: " + wu.getName() + " - " + wu.getEmail(),
				null);
		}
	}	//	sendEMail

	/**
	 * 	Send Payment EMail.
	 * 	@param request request
	 * 	@param p payment
	 * 	@param wu web user
	 */
	private void sendDeclineEMail (HttpServletRequest request, 
		MPayment p, WebUser wu, WebOrder wo)
	{
		StringBuffer message = new StringBuffer(p.getErrorMessage())
			.append(" - ").append(p.getCurrencyISO()).append(" ").append(p.getPayAmt())
			.append(" (Reference=").append(p.getR_PnRef()).append(") ");
			if (wo != null)
				message.append("\nfor Order: ").append(wo.getDocumentNo());
		
		JSPEnv.sendEMail(request, wu, MMailMsg.MAILMSGTYPE_PaymentError, 
			new Object[]{
				p.getDocumentNo() + " (" + p.getCurrencyISO() + " " + p.getPayAmt() + ")",
				wu.getName(),
				message.toString()});
	}	//	sendDeclineEMail

}	//	PaymentServlet
