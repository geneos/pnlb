<!--
  - Author:  Jorg Janke
  - Version: $Id: request.jsp,v 1.20 2005/10/01 23:54:37 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Request
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=request.jsp&SalesRep_ID=${param.SalesRep_ID}'/> 
</c:if> 
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - Request</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
	  <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter">
	<h1>New Request</h1>
      <form method="post" name="Request" action="requestServlet" enctype="application/x-www-form-urlencoded"
	  	onSubmit="checkForm(this, new Array ('Summary'));">
        <input name="Source" type="hidden" value=""/>
        <input name="Info" type="hidden" value=""/>
        <script language="Javascript">
		  document.Request.Source.value=document.referrer;
		  document.Request.Info.value=document.lastModified;
	    </script>
	    <input name="ForwardTo" type="hidden" value="<c:out value='${param.ForwardTo}'/>"/>
	    <c:if test='${not empty param.SalesRep_ID}'>
          <input name="SalesRep_ID" type="hidden" value="<c:out value='${param.SalesRep_ID}'/>"/>
	    </c:if>
	    <c:if test='${empty param.SalesRep_ID}'>
          <input name="SalesRep_ID" type="hidden" value="<c:out value='${webUser.salesRep_ID}'/>"/>
	    </c:if>
        <table border="0" align="center" cellpadding="2" cellspacing="2">
          <tr> 
            <td align="right">From <c:out value='${webUser.name}'/>:</td>
            <td align="left"><c:out value='${webUser.email}'/>&nbsp;</td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_RequestType" for="RequestType">Request Type</label></td>
            <td align="left"><cws:requestType/></td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_RequestType" for="RequestType">Optional Order Reference</label></td>
            <td align="left"><cws:requestOrder bpartnerID="${webUser.bpartnerID}"/></td>
          </tr>
          <tr> 
            <td>
              <label id="ID_Summary" for="Summary">Question - Issue - Request:</label></td>
          <td><input name="Confidential" type="checkbox" id="Confidential" value="Confidential">
            Confidential Information</td>
          </tr>
          <tr> 
            <td colspan="2" align="left"> <textarea name="Summary" cols="80" rows="8" id="ID_Summary"></textarea> 
            </td>
          </tr>
          <tr> 
            <td colspan="2" align="center"><font size="-1" color="#666666">Summary 1500 characters max - Details: add as Attachment after Submit</font></td>
          </tr>
          <tr> 
            <td> <input name="Reset" type="reset" value="Reset"/></td>
            <td align="right"> <input name="Submit" type="submit" value="Submit"> 
            </td>
          </tr>
        </table>
      </form>
      <p>&nbsp;</p></td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
