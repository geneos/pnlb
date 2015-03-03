<!--
  - Author:  Jorg Janke
  - Version: $Id: requestTypes.jsp,v 1.4 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Request Types
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=requestTypes.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title>Request Types</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
	<h1>Request Types</h1>
	  <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
	  <p><a href="request.jsp">New Request</a></p>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Type</th>
          <th>Description</th>
          <th>Open</th>
          <th>Total</th>
          <th>New last 30 days</th>
          <th>Closed last 30 days</th>
        </tr>
        <c:forEach items='${info.requestTypes}' var='type'> 
        <tr> 
          <td><a href="requestsAll.jsp?R_RequestType_ID=<c:out value='${type.r_RequestType_ID}'/>">
		  <c:out value='${type.name}'/></a></td>
          <td><c:out value='${type.description}'/>&ndash;</td>
          <td><c:out value='${type.openNo}'/></td>
          <td><c:out value='${type.totalNo}'/></td>
          <td><c:out value='${type.closed30No}'/></td>
          <td><c:out value='${type.new30No}'/></td>
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
