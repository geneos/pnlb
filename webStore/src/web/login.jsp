<!--
  - Author:  Jorg Janke
  - Version: $Id: login.jsp,v 1.28 2005/09/13 04:26:26 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Login
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - Login</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
	<h1>User Information</h1>
      <form action="loginServlet" method="post" enctype="application/x-www-form-urlencoded" 
	  	name="Login" target="_top" onSubmit="checkForm(this, new Array ('EMail','Password'));">
        <input name="AD_Client_ID" type="hidden" value='<c:out value="${initParam.#AD_Client_ID}" default="0"/>'/>
        <input name="Source" type="hidden" value=""/>
        <input name="Info" type="hidden" value=""/>
        <input name="Mode" type="hidden" value=""/>
        <script language="Javascript">
		  document.Login.Source.value=document.referrer;
		  document.Login.Info.value=document.lastModified;
  		</script>
        <table border="0" align="center" cellpadding="2" cellspacing="2" id="loginForm">
          <tr> 
            <td align="right"><label id="ID_EMail" for="EMail" title="Must be a valid EMail Address!!"><cws:message txt="EMail"/></label></td>
            <td align="left"><input class="Cmandatory" size="40" id="ID_EMail" value='<c:out value="${webUser.email}"/>' name="EMail" maxlength="60" type="text"/>
              <input name="validated" type="checkbox" id="validated" value="validated" disabled <c:if test='${webUser.EMailVerified}'>checked</c:if>> Address validated
            </td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_Password" for="Password"><cws:message txt="Password"/></label></td>
            <td align="left"> <input class="Cmandatory" size="20" type="password" id="ID_Password" value="" name="Password" maxlength="40"/> 
              &nbsp;<font color="#FF0000"><c:out value="${webUser.passwordMessage}"/></font> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>
			 <input type="submit" name="Login" id="Login" value="Login existing" 
		  		onClick="document.Login.Mode.value='Login';"> 
			 <input type="submit" name="LoginNew" id="LoginNew" value="New user" 
		  		onClick="document.Login.Mode.value='LoginNew';"> 
			  <c:if test="${not empty webUser.passwordMessage}"> 
                &nbsp; 
                <input type="submit" name="SendEMail" id="SendEMail" value="Send Password to EMail" 
				  onClick="document.Login.Mode.value='SendEMail';">
              </c:if>
			</td>
          </tr>
          <c:if test="${empty webUser || webUser.contactID == 0 || webUser.loggedIn}"> 
          <tr> 
            <td><h3> 
			  <c:choose>
			    <c:when test="${not empty webUser && webUser.contactID != 0}">Update Existing</c:when>
				<c:otherwise>Create New</c:otherwise>
			  </c:choose> 
            </h3></td>
            <td>&nbsp;</td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_PasswordNew" for="PasswordNew"> <c:choose> 
              <c:when test="${webUser.contactID != 0}">New/Confirm Password</c:when> 
			  <c:otherwise>Confirm Password</c:otherwise> </c:choose></label></td>
            <td align="left"><input class="Cmandatory" size="20" id="ID_PasswordNew" value="" name="PasswordNew" maxlength="40" type="password"/></td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_Name" for="Name">Name</label></td>
            <td align="left"><input class="Cmandatory" size="40" id="ID_Name" value="<c:out value='${webUser.name}'/>" name="Name" maxlength="60" type="text"/></td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_Company" for="Company">Company</label></td>
            <td align="left"><input size="40" id="ID_Company" value="<c:out value='${webUser.company}'/>" name="Company" maxlength="60" type="text"/> 
            </td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_Title" for="Title">Title</label></td>
            <td align="left"><input size="40" id="ID_Title" value='<c:out value="${webUser.title}"/>' name="Title" maxlength="60" type="text"/> 
            </td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_Address" for="Address">Address</label></td>
            <td align="left"><input class="Cmandatory"  size="40" id="ID_Address" value="<c:out value='${webUser.address}'/>" name="Address" maxlength="60" type="text"/> 
            </td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_Address2" for="Address">Address2</label></td>
            <td align="left"><input size="40" id="ID_Address2" value='<c:out value="${webUser.address2}"/>' name="Address2" maxlength="60" type="text"/> 
            </td>
          </tr>
          <cws:location countryID='${webUser.countryID}' regionID='${webUser.regionID}' regionName='${webUser.regionName}' city='${webUser.city}' postal='${webUser.postal}' /> 
          <tr> 
            <td align="right"><label id="ID_Phone" for="Phone">Phone</label></td>
            <td align="left"><input size="20" id="ID_Phone" value='<c:out value="${webUser.phone}"/>' name="Phone" maxlength="20" type="text"/></td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_Fax" for="Fax">Fax</label></td>
            <td align="left"><input size="20" id="ID_Fax" value='<c:out value="${webUser.fax}"/>' name="Fax" maxlength="20" type="text"/></td>
          </tr>
          <tr> 
            <td><input type="reset" name="Reset" value="Reset"></td>
            <td align="right"> 
              <input name="AddressConfirm" type="hidden" id="AddressConfirm" value="N">
              <font color="#FF0000"><c:out value="${webUser.saveErrorMessage}"/></font>&nbsp;
			  <input type="submit" name="Submit" id="Submit" value="Submit Info" 
		  		onClick="document.Login.Mode.value='Submit';checkForm(this, new Array ('EMail','Password','PasswordNew','Name','Address'));"> 
            </td>
          </tr>
          </c:if> 
        </table>
        <div align="center">Please enter all <b class="Cmandatory">mandatory</b> 
          data. </div>
      </form>
      <p>&nbsp;</p></td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
