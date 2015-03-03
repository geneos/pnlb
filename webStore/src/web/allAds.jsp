<!--
  - Author:  Jorg Janke
  - Version: $Id: allAds.jsp,v 1.2 2003/09/05 04:54:59 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Advertisements
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - All Ads</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
      <h1>Partner Info</h1>
	  <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
	  <p>Please check with our partners:</p>
	  <!-- Start Copy HERE		-->
	  <p>
        <c:forEach items='${info.allAds}' var='ad'>
		  <c:out value='${ad.description}'/>: 
		  <a href="#<c:out value='${ad.salesRep_ID}'/>">
		    <c:out value='${ad.name}'/>
	  	  </a>
		  <br>
        </c:forEach> 
	  </p>
	  <p>&nbsp;</p>
	  <!-- Start Ads HERE		-->
      <table width="100%" border="1" cellspacing="1" cellpadding="5">
        <c:forEach items='${info.allAds}' var='ad'> 
        <tr> 
          <td> 
		    <a name="<c:out value='${ad.salesRep_ID}'/>"></a>
		    <a href="http://www.compiere.com/wstore/click?<c:out value='${ad.clickTargetURL}'/>" target="_blank"> 
            <img src="<c:out value='${ad.imageURL}'/>" alt="<c:out value='${ad.name}'/>" border="0" align="left"></a> 
            <img src="<c:out value='${ad.webParam2}'/>" alt="<c:out value='${ad.webParam1}'/>" border="0" align="right"> 
            &nbsp; <b><c:out value='${ad.description}'/></b>
			<br>
            &nbsp; <a href="http://www.compiere.com/wstore/request.jsp?SalesRep_ID=<c:out value='${ad.salesRep_ID}'/>">Contact</a>
			<br>
            &nbsp; <a href="http://www.compiere.com/wstore/basketServlet?M_Product_ID=1000018&SalesRep_ID=<c:out value='${ad.salesRep_ID}'/>">Buy Next Step</a>
			<br>
			&nbsp; <i><c:out value='${ad.webParam3}' escapeXml='false'/></i>
			<p><c:out value='${ad.adText}' escapeXml='false'/></p>
			<p><c:out value='${ad.webParam4}' escapeXml='false'/></p>
          </td>
        </tr>
        </c:forEach> 
      </table>
	  <!-- End Copy HERE		-->
      <p>&nbsp;</p></td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
