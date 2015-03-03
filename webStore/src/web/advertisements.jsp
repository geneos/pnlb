<!--
  - Author:  Jorg Janke
  - Version: $Id: advertisements.jsp,v 1.2 2003/09/05 04:54:59 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Advertisements
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=advertisements.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Advertisements</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
	  <h1>My Advertisements</h1>
	  <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
      <table width="100%" border="1" cellspacing="1" cellpadding="5">
        <c:forEach items='${info.advertisements}' var='ad'> 
        <tr> 
          <td colspan="2">
		    <a href="http://www.compiere.com/wstore/click?<c:out value='${ad.clickTargetURL}'/>" target="_blank"> 
            <img src="<c:out value='${ad.imageURL}'/>" alt="<c:out value='${ad.name}'/>" border="0" align="left"></a> 
            <img src="<c:out value='${ad.webParam2}'/>" alt="<c:out value='${ad.webParam1}'/>" border="0" align="right"> 
            &nbsp; <b><c:out value='${ad.description}'/></b>
			<br>
            &nbsp; <a href="request.jsp?SalesRep_ID=<c:out value='${ad.salesRep_ID}'/>">Contact</a>
			<br>
            &nbsp; <a href="basketServlet?M_Product_ID=1000018&SalesRep_ID=<c:out value='${ad.salesRep_ID}'/>">Buy Next Step</a>
			<br>
			&nbsp; <i><c:out value='${ad.webParam3}' escapeXml='false'/></i>
			<p><c:out value='${ad.adText}' escapeXml='false'/></p>
			<p><c:out value='${ad.webParam4}' escapeXml='false'/></p>
		  </th> 
		</tr>
        <form action="advertisementServlet" method="post" enctype="application/x-www-form-urlencoded" name="advertisement" target="_top" id="advertisement">
        <tr> 
          <th>Name</th>
          <td><input name="Name" type="text" value="<c:out value='${ad.name}'/>" size="40" maxlength="40"></td>
        </tr>
        <tr> 
          <th>Description</th>
          <td><input name="Description" type="text" value="<c:out value='${ad.description}'/>" size="60" maxlength="60"></td>
        </tr>
        <tr> 
          <th>Image URL</th>
          <td><input name="ImageURL" type="text" value="<c:out value='${ad.imageURL}'/>" size="60"></td>
        </tr>
        <tr> 
          <th>Text</th>
          <td><textarea name="AdText" cols="80" rows="8" id="AdText"><c:out value='${ad.adText}'/></textarea>
          </td>
        </tr>
        <tr> 
          <th>Click Target</th>
          <td><input name="ClickTargetURL" type="text" value="<c:out value='${ad.clickTargetURL}'/>" size="60"></td>
        </tr>
        <tr> 
          <th>&nbsp;</th>
          <td>
            <input name="W_Advertisement_ID" type="hidden" id="W_Advertisement_ID" value="<c:out value='${ad.w_Advertisement_ID}'/>">
            <input type="submit" name="Submit" value="Submit">
            <input name="Reset" type="reset" id="Reset" value="Reset">
          </td>
        </tr>
        <tr> 
          <td colspan="2"><hr></td>
        </tr>
        </form>
        <c:forEach items='${pair}' var='ad.clickCountWeek'> 
        <tr> 
          <th><c:out value='${pair.key}'/></th>
          <td><c:out value='${pair.name}'/></td>
        </tr>
        </c:forEach> 
        <tr> 
          <td colspan="2">&nbsp;</td>
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