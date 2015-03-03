<!--
  - Author:  Jorg Janke
  - Version: $Id: assets.jsp,v 1.17 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Assets
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=assets.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Assets</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter">
	  <h1>My Assets</h1>
      <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
	  <c:if test='${not webUser.EMailVerified}'>
        <form action="emailServlet" method="post" enctype="application/x-www-form-urlencoded" 
	  	  name="EMailVerification" target="_top">
          	<input name="AD_Client_ID" type="hidden" value='<c:out value="${initParam.#AD_Client_ID}" default="0"/>'/>
          	<input name="Source" type="hidden" value=""/>
          	<input name="Info" type="hidden" value=""/>
            <input name="ForwardTo" type="hidden" value="assets.jsp">         	
            <script language="Javascript">
				document.EMailVerification.Source.value=document.referrer;
				document.EMailVerification.Info.value=document.lastModified;
  			</script>
			<p><b>To access your Assets:</b></p>
			<p>Enter Verification Code
      	      <input name="VerifyCode" type="text" id="VerifyCode" value="<Check EMail>">
              <input type="submit" name="Submit" value="Submit">
			</p>
			<p>The Verification Code will be sent to <b><c:out value='${webUser.email}'/></b> &nbsp;
              <input type="submit" name="ReSend" id="ReSend" value="Send Verification Code">
			</p>
			<p><c:out value="${webUser.passwordMessage}"/></p>
	    </form>
	  </c:if>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Name</th>
          <th>Description</th>
          <th>Guarantee Date, Qty </th>
          <th>Your Version, Serial</th>
          <th>Delivery</th>
          <th>Download</th>
        </tr>
        <c:forEach items='${info.assets}' var='asset'> 
        <tr> 
          <td><c:out value='${asset.name}'/></td>
          <td><c:out value='${asset.description}'/>&nbsp;</td>
          <td><fmt:formatDate value='${asset.guaranteeDate}'/><br><c:out value='${asset.qty}'/></td>
          <td><c:out value='${asset.versionNo}'/><br><c:out value='${asset.serNo}'/></td>
          <td><c:forEach items='${asset.downloadNames}' var='addlDL'>
		    <c:out value='${addlDL}'/><br>
		  </c:forEach>&nbsp;
		  </td>
          <td>
		    <c:if test='${asset.downloadable}'>
		  	  <c:forEach items='${asset.downloadURLs}' var='addlDL'>
		    	<a href="<c:out value='http://${ctx.context}/'/>assetServlet/<c:out value='${addlDL}'/>.zip?Asset_ID=<c:out value='${asset.a_Asset_ID}'/>&PD=<c:out value='${addlDL}'/>" target="_blank">
		  	  	<img src="assetDownload.gif" alt="Download <c:out value='${addlDL}'/>" width="24" height="24" border="0"></a><br>
			  </c:forEach>
			</c:if>
		    <c:if test='${not asset.downloadable}'>
		  	  n/a
			</c:if>
			&nbsp;
		  </td>
        </tr>
        </c:forEach> 
      </table>
      <c:if test='${webUser.creditCritical}'>
	      <p><strong><font color="#990000">! <c:out value='${webUser.SOCreditStatus}'/> !</font></strong></p>
	  </c:if>
      <p>To download, click on the link - or right-click and &quot;Save Target As
        ...&quot;</p>
    </td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
