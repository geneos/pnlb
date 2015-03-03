<!--
  - Author:  Jorg Janke
  - Version: $Id: confirm.jsp,v 1.13 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Confirmation 
  - webOrder, webUser, payment
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - Payment Confirmation</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter">
	<h1>Thanks - Payment Confirmation</h1>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Payment</th>
          <th>Invoice</th>
          <th>Details</th>
          <th>Total</th>
        </tr>
        <tr> 
          <td><c:out value='${payment.documentNo}'/></td>
          <td>
		    <c:choose>
		    <c:when test='${not empty webOrder}'>
			  <a href="invoiceServlet/I_<c:out value='${webOrder.invoiceInfo}'/>.pdf?Invoice_ID=<c:out value='${webOrder.invoice_ID}'/>" target="_blank"><img src="pdf.gif" alt="Get Invoice PDF" width="30" height="30" border="0" align="right"></a>
			  <c:out value='${webOrder.invoiceInfo}'/>
			</c:when>  
		    <c:otherwise>
			  &nbsp;
			</c:otherwise>  
			</c:choose>
		  </td>
          <td><c:out value='${payment.r_AuthCode}'/></td>
          <td><c:out value='${payment.currencyISO}'/>&nbsp;<fmt:formatNumber value='${payment.payAmt}' type="currency" currencySymbol=""/></td>
        </tr>
      </table>
      <form action="assets.jsp" method="post" enctype="application/x-www-form-urlencoded" name="confirm" id="confirm">
        If applicable, please check your &nbsp; 
        <input type="submit" name="Submit" value="My Assets">
        &nbsp; for download information. 
      </form>
	  <!-- Remove Info			-->
	  <c:remove var='payment' />
	  <c:remove var='webOrder' />
	  
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Interest Area</th>
          <th>Description</th>
          <th align="right">Subscription</th>
        </tr>
        <c:forEach items='${info.interests}' var='interest'> 
        <tr> 
          <td><c:out value='${interest.name}'/></td>
          <td><c:out value='${interest.description}'/>&nbsp;</td>
          <td>
		    <c:choose>
		    <c:when test='${interest.subscribed}'>
			<fmt:formatDate value='${interest.subscribeDate}'/>&nbsp;
			<input type="button" name="UnSubscribe_<c:out value='${interest.r_InterestArea_ID}'/>" value="Un-Subscribe" 
		      onClick="window.top.location.replace('infoServlet?mode=unsubscribe&area=<c:out value='${interest.r_InterestArea_ID}'/>&contact=<c:out value='${info.user_ID}'/>');" >
			</c:when>
			<c:otherwise>  
			<input type="button" name="Subscribe_<c:out value='${interest.r_InterestArea_ID}'/>" value="Subscribe" 
		      onClick="window.top.location.replace('infoServlet?mode=subscribe&area=<c:out value='${interest.r_InterestArea_ID}'/>&contact=<c:out value='${info.user_ID}'/>');" >
			</c:otherwise>
			</c:choose>  
		  </td>
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
