<!--
  - Author:  Jorg Janke
  - Version: $Id: help.jsp,v 1.7 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Help
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<cws:priceList priceList_ID="0"/> 
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - Welcome</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<script language="JavaScript" type="text/JavaScript">
;
</script>
<noscript>
Please enable Java Script to continue. 
</noscript>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft">
		<%@ include file="/WEB-INF/jspf/left.jspf" %>
	</td>
    <td class="centerCenter"> 
	<h1>Web Store Help</h1>
      <h3>New User</h3>
      <p>You create your new account when you create the order or when you click 
        on the Login button or Welcome link. After entering your email address 
        and a password displays the New User Login dialog. Enter your password 
        twice and the other mandatory and optional information.</p>
      <h3>Changing Password, Name, Address, etc.</h3>
      <p>To change your password log in with your old password. Then click on 
        the Welcome link to get the User Update screen. Enter your old and new 
        password and update the other information if required.</p>
      <h3>Payment not approved</h3>
      <p>If your payment was not successful, you can go into your Orders and void 
        the order or complete it and send us the payment information or another 
        form of payment (Check, Transfer).</p>
	</td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
