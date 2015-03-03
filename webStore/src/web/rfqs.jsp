<!--
  - Author:  Jorg Janke
  - Version: $Id: rfqs.jsp,v 1.6 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2004 Jorg Janke
  - - -
  - Web Store RfQs
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=rfqs.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My RfQs</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
	<h1>My RfQs</h1>
	  <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Name</th>
          <th>Desciption</th>
          <th>Details</th>
          <th>Response by</th>
          <th>Work Start</th>
          <th>Delivery</th>
        </tr>
        <c:forEach items='${info.rfQs}' var='rfq'> 
        <tr> 
          <td><a href="rfqDetails.jsp?C_RfQ_ID=<c:out value='${rfq.c_RfQ_ID}'/>"><c:out value='${rfq.name}'/></a></td>
          <td><c:out value='${rfq.description}'/>&nbsp;</td>
          <td><c:out value='${rfq.help}'/>&nbsp;</td>
          <td><fmt:formatDate value='${rfq.dateResponse}'/></td>
          <td><fmt:formatDate value='${rfq.dateWorkStart}'/></td>
          <td><fmt:formatDate value='${rfq.dateWorkComplete}'/> - <c:out value='${rfq.deliveryDays}'/> days</td>
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
