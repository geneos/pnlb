<!--
  - Author:  Jorg Janke
  - Version: $Id: requestsAll.jsp,v 1.4 2005/10/08 02:02:44 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Type all Requests (Summary)
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=requestAlls.jsp'/>
</c:if>
<!-- Set Request Type ID and get Request Type		-->
<c:if test='${empty param.R_RequestType_ID}'>
  <c:set target='${info}' property='message' value='RequestType not found' />
  <c:redirect url='requestTypes.jsp'/>
</c:if>
<c:set target='${info}' property='id' value='${param.R_RequestType_ID}' />
<c:set var='requestType' value='${info.requestType}' />
<c:if test='${empty requestType}'>
  <c:set target='${info}' property='message' value='RequestType not found' />
  <c:redirect url='requestTypes.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title>- Requests: <c:out value='${requestType.name}'/></title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
	<h1>Requests: <c:out value='${requestType.name}'/></h1>
	  <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
	  <p><a href="requestTypes.jsp">Request Types</a> - <a href="request.jsp">New Request</a></p>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Document No</th>
          <th>Summary</th>
          <th>Status</th>
          <th>Assigned</th>
          <th>Created</th>
        </tr>
        <c:forEach items='${requestType.requests}' var='request'> 
        <tr> 
          <td><a href="requestDetails.jsp?R_Request_ID=<c:out value='${request.r_Request_ID}'/>"><c:out value='${request.documentNo}'/></a></td>
          <td><c:out value='${request.summary}'/></td>
          <td><c:out value='${request.statusName}'/></td>
          <td><c:out value='${request.salesRepName}'/></td>
          <td><fmt:formatDate value='${request.created}'/> <c:out value='${request.createdByName}'/></td>
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
