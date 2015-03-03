<!--
  - Author:  Jorg Janke
  - Version: $Id: info.jsp,v 1.1 2003/05/27 04:31:55 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Interest Area
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=info.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Interest Areas</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
	  <h1>Info - Interest Areas</h1>
	  <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
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
