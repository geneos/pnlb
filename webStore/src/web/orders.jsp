<!--
  - Author:  Jorg Janke
  - Version: $Id: orders.jsp,v 1.13 2005/10/01 23:54:37 jjanke Exp $
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
<title><c:out value='${ctx.name}'/> - My Orders</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter">
	<h1>My Orders</h1>
	  <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Document No</th>
          <th>Desciption</th>
          <th>Status</th>
          <th>Date</th>
          <th align="right">Total Lines</th>
          <th align="right">Grand Total</th>
          <th>&nbsp;</th>
        </tr>
        <c:forEach items='${info.orders}' var='order'> 
        <tr> 
          <td><a href="orderDetails.jsp?C_Order_ID=<c:out value='${order.c_Order_ID}'/>"><c:out value='${order.documentNo}'/></a></td>
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
        </c:forEach> 
      </table>
      <p>&nbsp;</p></td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
