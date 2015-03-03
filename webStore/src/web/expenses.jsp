<!--
  - Author:  Jorg Janke
  - Version: $Id: expenses.jsp,v 1.4 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2004 Jorg Janke
  - - -
  - Web Store Expenses
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=expenses.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Expenses</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter">
      <h1>My Expenses </h1>
      <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Document No</th>
          <th>Date</th>
          <th>Description</th>
          <th>Approved</th>
        </tr>
        <c:forEach items='${info.expenses}' var='report'> 
        <tr> 
          <td><c:out value='${report.documentNo}'/></td>
          <td><fmt:formatDate value='${report.dateReport}'/></td>
          <td><c:out value='${report.description}'/>&nbsp;</td>
          <td><c:out value='${report.approved}'/>&nbsp;</td>
        </tr>
        </c:forEach> 
      </table>
    </td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
