<!--
  - Author:  Jorg Janke
  - Version: $Id: basket.jsp,v 1.20 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Basket
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Basket</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %>
    </td>
    <td class="centerCenter">
	  <h1>Web Basket</h1>
      <c:if test='${empty webBasket}'> 
      <p>Empty Basket (timeout) - Please go back, refresh the page and add products 
        again.</p>
      </c:if> <c:if test='${not empty webBasket}'> 
      <form action="basketServlet" method="post" enctype="application/x-www-form-urlencoded" name="basket" id="basket" >
        <table width="100%" border="1" cellspacing="2" cellpadding="2">
          <tr> 
            <th>Product</th>
            <th align="right">Price</th>
            <th align="right">Quantity</th>
            <th align="right"><c:out value='${priceList.currency}'/> Total</th>
            <th>&nbsp;</th>
          </tr>
          <c:forEach items='${webBasket.lines}' var='line'> 
          <tr> 
            <td><c:out value='${line.name}'/></td>
            <td align="right"><fmt:formatNumber value='${line.price}' type="currency" currencySymbol=""/></td>
            <td align="right"><fmt:formatNumber value='${line.quantity}'/></td>
            <td align="right"><fmt:formatNumber value='${line.total}' type="currency" currencySymbol=""/></td>
            <td><input type="submit" name="Delete_<c:out value='${line.line}'/>" value="Delete"></td>
          </tr>
          </c:forEach> 
          <tr> 
            <th><c:out value='${webBasket.lineCount}'/></th>
            <th>&nbsp;</th>
            <th>&nbsp;</th>
            <th>
              <div align="right"><c:out value='${priceList.currency}'/> <fmt:formatNumber value='${webBasket.total}' type="currency" currencySymbol=""/></div></th>
            <th><input  type="button" name="Checkout" id="Checkout" value="Create Secure Order" 
	    	onClick="window.top.location.replace('<c:out value='https://${ctx.context}/'/>checkOutServlet');">
			<!-- removed by request of BBB
			&nbsp; <input name="CheckoutUnsecure" id="CheckoutUnsecure" value="Create Order" 
	    	onClick="window.top.location.replace('checkOutServlet');" type="button">  -->
            </th>
          </tr>
        </table>
      </form>
      </c:if> <p> 
        <input name="Back" type="button" id="Back" value="Back to Web Store" onClick="window.top.location.replace('index.jsp');">
      </p></td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
