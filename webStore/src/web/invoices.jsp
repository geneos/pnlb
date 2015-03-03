<!--
  - Author:  Jorg Janke
  - Version: $Id: invoices.jsp,v 1.13 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Invoices
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=invoices.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Invoices</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
	<h1>My Invoices</h1>
	<c:if test='${not empty info.info}'>
	  <p><b><c:out value='${info.message}'/></b></p>
	</c:if>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Document No</th>
          <th>Description</th>
          <th>Date</th>
          <th>Total Lines</th>
          <th>Grand Total</th>
          <th>Image</th>
          <th>Open</th>
        </tr>
        <c:forEach items='${info.invoices}' var='invoice'> 
        <tr> 
          <td><a href="invoiceLines.jsp?C_Invoice_ID=<c:out value='${invoice.c_Invoice_ID}'/>"><c:out value='${invoice.documentNo}'/></a></td>
          <td><c:out value='${invoice.description}'/>&nbsp;</td>
          <td><fmt:formatDate value='${invoice.dateInvoiced}'/></td>
          <td><fmt:formatNumber value='${invoice.totalLines}' type="currency" currencySymbol=""/></td>
          <td><c:out value='${invoice.currencyISO}'/>&nbsp;<fmt:formatNumber value='${invoice.grandTotal}' type="currency" currencySymbol=""/></td>
          <td><a href="invoiceServlet/I_<c:out value='${invoice.documentNo}'/>.pdf?Invoice_ID=<c:out value='${invoice.c_Invoice_ID}'/>" target="_blank"><img src="pdf.gif" alt="Get Invoice Image" width="30" height="30" border="0"></a></td>
          <td><c:if test='${invoice.paid}'>Paid</c:if><c:if test='${not invoice.paid}'> 
            <input type="submit" name="InvoicePay" value="Pay <c:out value='${invoice.openAmt}'/>" 
			  onClick="window.top.location.replace('paymentServlet?C_Invoice_ID=<c:out value='${invoice.c_Invoice_ID}'/>&Amt=<c:out value='${invoice.openAmt}'/>');">
            </c:if></td>
        </tr>
        </c:forEach> 
      </table>
      <p>&nbsp;</p></td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
