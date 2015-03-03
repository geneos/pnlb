<!--
  - Author:  Jorg Janke
  - Version: $Id: addressInfo.jsp,v 1.20 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Address Info
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Address Info</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"><h1>My Address</h1>
      <h3>Please confirm your address:</h3>
      <form action="loginServlet" method="post" enctype="application/x-www-form-urlencoded" name="addressForm" id="address"
	    onSubmit="checkForm(this, new Array ('Name','Address'));">
        <input name="Mode" type="hidden" value="Submit"/>
        <table width="100%" border="0" align="center" cellpadding="2" cellspacing="2">
          <tr> 
            <td align="right"><label id="ID_Name" for="Name">Name</label></td>
            <td align="left"><input class="Cmandatory" size="40" id="ID_Name" value='<c:out value="${webUser.name}"/>' name="Name" maxlength="60" type="text"/></td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_Company" for="Company">Company</label></td>
            <td align="left"><input size="40" id="ID_Company" value='<c:out value="${webUser.company}"/>' name="Company" maxlength="60" type="text"/> 
            </td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_Title" for="Title">Title</label></td>
            <td align="left"><input size="40" id="ID_Title" value='<c:out value="${webUser.title}"/>' name="Title" maxlength="60" type="text"/> 
            </td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_Address" for="Address">Address</label></td>
            <td align="left"><input class="Cmandatory" size="40" id="ID_Address" value='<c:out value="${webUser.address}"/>' name="Address" maxlength="60" type="text"/> 
            </td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_Address2" for="Address">Address2</label></td>
            <td align="left"><input size="40" id="ID_Address2" value='<c:out value="${webUser.address2}"/>' name="Address2" maxlength="60" type="text"/> 
            </td>
          </tr>
          <cws:location countryID="${webUser.countryID}" regionID="${webUser.regionID}" regionName="${webUser.regionName}" city="${webUser.city}" postal="${webUser.postal}" /> 
          <tr> 
            <td align="right"><label id="ID_Phone" for="Phone">Phone</label></td>
            <td align="left"><input size="20" id="ID_Phone" value='<c:out value="${webUser.phone}"/>' name="Phone" maxlength="20" type="text"/></td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_Fax" for="Fax">Fax</label></td>
            <td align="left"><input size="20" id="ID_Fax" value='<c:out value="${webUser.fax}"/>' name="Fax" maxlength="20" type="text"/></td>
          </tr>
          <tr> 
            <td>
			  <input type="reset" name="Reset" value="Reset">
			  <input name="AddressConfirm" type="hidden" id="AddressConfirm" value="Y">
			</td>
            <td align="right">
			  <font color="#FF0000"><strong> <c:out value="${webUser.saveErrorMessage}"/></strong></font> 
			  <div id="processingDiv" style="display:none"><strong>Processing ...</strong></div>
			  <div id="submitDiv" style="display:inline">
			    <input type="submit" name="Submit" id="Submit" value="Submit Info"> 
			  </div>
			</td>
          </tr>
        </table>
        <div align="center">Enter all <b class="Cmandatory">mandatory</b> data. 
        </div>
      </form>
      <p>&nbsp;</p></td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
