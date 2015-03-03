<%@ page language="java" import="java.sql.*" %>
<!--
  - Author:  Jorg Janke
  - Version: $Id: registration.jsp,v 1.8 2005/09/11 02:27:52 jjanke Exp $
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
<title><c:out value='${ctx.name}'/> - Registration</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<!-- Set Registration ID and get Registration (may not exist) -->
<c:set target='${info}' property='id' value='0' />
<c:if test='${not empty param.A_Registration_ID}'>
  <c:set target='${info}' property='id' value='${param.A_Registration_ID}' />
</c:if>  
<c:set var='registration' value='${info.registration}' />
<c:if test='${empty registration}'>
  <c:set target='${info}' property='message' value='Registration not found' />
  <c:redirect url='registrations.jsp'/>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter">
      <h1>Registration</h1>
      <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
      <form action="registrationServlet" method="post" enctype="application/x-www-form-urlencoded" name="registration" id="registration">
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
		  <th>Name</th>
          <td><input name="Name" type="text" id="Name" value="<c:out value='${registration.name}'/>" size="60" maxlength="60"></td> 
		</tr><tr> 
		  <th>Description</th>
          <td><input name="Description" type="text" id="Description" value="<c:out value='${registration.description}'/>" size="60" maxlength="255"></td>
		</tr><tr>
          <th>Service Date</th>
          <td><input name="AssetServiceDate" type="text" id="AssetServiceDate" value="<fmt:formatDate value='${registration.assetServiceDate}'/>" size="20"></td>
		</tr><tr>
          <th>In Production</th>
          <td><input <c:if test='${registration.inProduction}'>checked</c:if> name="InProduction" type="checkbox" id="InProduction" value="IsInProduction"></td>
        </tr><tr> 
          <th>Allow Publication</th>
          <td><input <c:if test='${registration.allowPublish}'>checked</c:if> name="AllowPublish" type="checkbox" id="AllowPublish" value="IsAllowPublish"></td>
		</tr>
        <c:forEach items='${registration.values}' var='rvalue'> 
		<tr>
		  <th><c:out value='${rvalue.registrationAttributeDescription}'/></th>
		  <td><input name="<c:out value='${rvalue.registrationAttribute}'/>" type="text" id="<c:out value='${rvalue.registrationAttribute}'/>" value="<c:out value='${rvalue.name}'/>" size="30" maxlength="60"></td>
		</tr>
        </c:forEach> 
        <tr> 
          <th>&nbsp;</th>
          <td>
            <input name="A_Registration_ID" type="hidden" id="A_Registration_ID" value="<c:out value='${registration.a_Registration_ID}'/>">
            <input type="submit" name="Submit" value="Submit">
            <input name="Reset" type="reset" id="Reset" value="Reset"> 
            </td>
        </tr>
      </table>
      </form>
    </td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
