<!--
  - Author:  Jorg Janke
  - Version: $Id: emailVerify.jsp,v 1.3 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Assets
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=emailVerify.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - Verify EMail</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter">
	  <h1>Verify Your EMail Address</h1>
      <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
	  <c:if test='${not webUser.EMailVerified}'>
        <form action="emailServlet" method="post" enctype="application/x-www-form-urlencoded" 
	  	  name="EMailVerification" target="_top">
          	<input name="AD_Client_ID" type="hidden" value='<c:out value="${initParam.#AD_Client_ID}" default="0"/>'/>
          	<input name="Source" type="hidden" value=""/>
          	<input name="Info" type="hidden" value=""/>
            <input name="ForwardTo" type="hidden" value="emailVerify.jsp">         	
         	<script language="Javascript">
				document.EMailVerification.Source.value=document.referrer;
				document.EMailVerification.Info.value=document.lastModified;
  			</script>
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
	  <c:if test='${webUser.EMailVerified}'>
	    <p>
	  	Thank you - your email address <b><c:out value='${webUser.email}'/></b> was verified.
		</p>
	  </c:if>
      <c:if test='${webUser.creditCritical}'>
	      <p><strong><font color="#990000">! <c:out value='${webUser.SOCreditStatus}'/> !</font></strong></p>
	  </c:if>
    </td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
