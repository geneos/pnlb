<!--
  - Author:  Jorg Janke
  - Version: $Id: orderDetails.jsp,v 1.1 2003/08/31 06:53:03 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Orders
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=orders.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Order Details</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<!-- Set Order ID and get Invoice		-->
<c:set target='${info}' property='id' value='${param.C_Order_ID}' />
<c:set var='order' value='${info.order}' />
<c:if test='${empty order}'>
  <c:set target='${info}' property='message' value='Order not found' />
  <c:redirect url='orders.jsp'/>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
	<h1>My Order Details <c:out value='${order.documentNo}'/></h1>
	  <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Document No</th>
          <th>Description</th>
          <th>Status</th>
          <th>Date</th>
          <th align="right">Total Lines</th>
          <th align="right">Grand Total</th>
          <th>&nbsp;</th>
        </tr>
        <tr> 
          <td><c:out value='${order.documentNo}'/></td>
          <td><c:out value='${order.description}'/>&nbsp;</td>
          <td><c:out value='${order.docStatusName}'/></td>
          <td><fmt:formatDate value='${order.dateOrdered}'/></td>
          <td align="right"><fmt:formatNumber value='${order.totalLines}' type="currency" currencySymbol=""/></td>
          <td align="right"><c:out value='${order.currencyISO}'/>&nbsp;<fmt:formatNumber value='${order.grandTotal}' type="currency" currencySymbol=""/></td>
		  <td> 
			<c:if test='${order.docStatus=="IP"}'>
			  <input name="Void" id="Void" value="Void" 
	    	    onClick="window.top.location.replace('<c:out value='https://${ctx.context}/'/>orderServlet?C_Order_ID=<c:out value='${order.c_Order_ID}'/>&DocAction=VO');" type="button">
			  <input name="Complere" id="Complete" value="Complete" 
	    	    onClick="window.top.location.replace('<c:out value='https://${ctx.context}/'/>orderServlet?C_Order_ID=<c:out value='${order.c_Order_ID}'/>&DocAction=CO');" type="button">
			</c:if>
			<c:if test='${order.docStatus=="WP"}'>
              <input type="submit" name="OrderPay" value="Pay <c:out value='${order.grandTotal}'/>" 
			    onClick="window.top.location.replace('paymentServlet?C_Invoice_ID=<c:out value='${order.c_Invoice_ID}'/>&Amt=<c:out value='${order.grandTotal}'/>');">
			</c:if>&nbsp;
          </td>
        </tr>
      </table>
	  <h3>Lines</h3>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Line</th>
          <th>Name</th>
          <th>Description</th>
          <th>Qty</th>
          <th>Price</th>
          <th>Line Net</th>
        </tr>
        <c:forEach items='${order.lines}' var='line'> 
        <tr> 
          <td><c:out value='${line.line}'/></td>
          <td><c:out value='${line.name}'/>&nbsp;</td>
          <td><c:out value='${line.descriptionText}'/>&nbsp;</td>
          <td><fmt:formatNumber value='${line.qtyInvoiced}' /></td>
          <td><fmt:formatNumber value='${line.priceActual}' type="currency" currencySymbol=""/></td>
          <td><fmt:formatNumber value='${line.lineNetAmt}' type="currency" currencySymbol=""/></td>
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
