<!--
  - Author:  Jorg Janke
  - Version: $Id: payments.jsp,v 1.14 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Payments
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=payments.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Payments</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
	<h1>My Payments</h1>
	  <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Document No</th>
          <th>Doc Status</th>
          <th>Credit Card</th>
          <th>Date</th>
          <th align="right">Amount</th>
          <th>Details</th>
        </tr>
        <c:forEach items='${info.payments}' var='payment'> 
        <tr> 
          <td><c:out value='${payment.documentNo}'/></td>
          <td><c:out value='${payment.docStatusName}'/></td>
          <td><c:out value='${payment.r_RespMsg}'/>&nbsp;</td>
          <td><fmt:formatDate value='${payment.dateTrx}'/></td>
          <td align="right"><c:out value='${payment.currencyISO}'/>&nbsp;<fmt:formatNumber value='${payment.payAmt}' type="currency" currencySymbol=""/></td>
          <td>&nbsp;<c:out value='${payment.r_AuthCode}'/></td>
        </tr>
        </c:forEach> 
      </table>
      <form action="paymentServlet" method="get" enctype="application/x-www-form-urlencoded" name="MakePayment" id="MakePayment">
        Make a Payment: &nbsp; Amount 
        <input name="Amt" type="text" id="Amt" value="120.00" size="10">
        <input type="submit" name="Submit" value="Submit">
      </form></td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
