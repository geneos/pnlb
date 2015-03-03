<!--
  - Author:  Jorg Janke
  - Version: $Id: registrations.jsp,v 1.5 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Registration
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=registrations.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Registrations</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter">
      <h1>My Registrations</h1>
      <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
	  <p><a href="registration.jsp">New Registration</a></p>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Name</th>
          <th>Description</th>
          <th>Service Date</th>
          <th>In Production</th>
          <th>Allow Publication</th>
        </tr>
        <c:forEach items='${info.registrations}' var='registration'> 
        <tr> 
          <td><a href="registration.jsp?A_Registration_ID=<c:out value='${registration.a_Registration_ID}'/>"><c:out value='${registration.name}'/></a></td>
          <td><c:out value='${registration.description}'/>&nbsp;</td>
          <td><fmt:formatDate value='${registration.assetServiceDate}'/></td>
          <td><c:out value='${registration.inProduction}'/></td>
          <td><c:out value='${registration.allowPublish}'/></td>
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
