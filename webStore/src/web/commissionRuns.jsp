<!--
  - Author:  Jorg Janke
  - Version: $Id: invoices.jsp,v 1.10 2003/06/20 14:49:45 jjanke Exp $
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
<title><c:out value='${ctx.name}'/> - My Commissions</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
	<h1>My Commissions</h1>
	<c:if test='${not empty info.info}'>
	  <p><b><c:out value='${info.message}'/></b></p>
	</c:if>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Document No</th>
          <th>Description</th>
          <th>Start Date</th>
          <th>Grand Total</th>
        </tr>
        <c:forEach items='${info.commissionRuns}' var='commissionRun'> 
        <tr> 
          <td><a href="comissionAmts.jsp?C_CommissionRun_ID=<c:out value='${commissionRun.c_CommissionRun_ID}'/>"><c:out value='${commissionRun.documentNo}'/></a></td>
          <td><c:out value='${commissionRun.description}'/>&nbsp;</td>
          <td><fmt:formatDate value='${commissionRun.startDate}'/></td>
          <td><fmt:formatNumber value='${commissionRun.grandTotal}' type="currency" currencySymbol=""/></td>
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
