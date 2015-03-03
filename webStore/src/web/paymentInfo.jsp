<!--
  - Author:  Jorg Janke
  - Version: $Id: paymentInfo.jsp,v 1.26 2005/10/01 23:54:37 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Payment Info
  - Variables: webOrder, webUser, payment
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=paymentInfo.jsp'/>
</c:if>
<c:if test='${empty payment}'>
  <c:redirect url='index.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Payment Info</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0" id="PageCenter">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
	  <c:if test='${not empty webOrder}'>
	  <h1>Thank you for your Order</h1>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Order</th>
          <th align="right">Lines</th>
          <th align="right">Shipping</th>
          <th align="right">Tax</th>
          <th align="right">Total</th>
        </tr>
        <tr> 
          <td>&nbsp;<c:out value='${webOrder.documentNo}'/></td>
          <td align="right">&nbsp;<fmt:formatNumber value='${webOrder.totalLines}' type="currency" currencySymbol=""/></td>
          <td align="right">&nbsp;<fmt:formatNumber value='${webOrder.freightAmt}' type="currency" currencySymbol=""/></td>
          <td align="right">&nbsp;<fmt:formatNumber value='${webOrder.taxAmt}' type="currency" currencySymbol=""/></td>
          <td align="right"><b><c:out value='${webOrder.currencyISO}'/>&nbsp;<fmt:formatNumber value='${webOrder.grandTotal}' type="currency" currencySymbol=""/></b></td>
        </tr>
      </table>
	  </c:if>
	  <c:if test='${empty webOrder}'>
	  <h1>Payment of  <c:out value='${payment.currencyISO}'/> <fmt:formatNumber value='${payment.payAmt}' type="currency" currencySymbol=""/></h1>
	  </c:if>
      <h2>Please enter your payment information</h2>
      <c:if test="${not empty payment.r_PnRef}">
        <p><b>Payment Info: <c:out value='${payment.r_PnRef}'/> &nbsp; 
		<font color="#FF0000"><c:out value="${payment.errorMessage}"/></font></b></p>
      </c:if>

	  <form action="paymentServlet" method="post" enctype="application/x-www-form-urlencoded" 
	  	name="payment" target="_top" id="payment"
	    onSubmit="checkForm(this, new Array ('CreditCardNumber','CreditCardExpMM','CreditCardExpYY','A_Name','A_Street','A_City','A_Zip'));">
        <table width="100%" border="0" align="center" cellpadding="2" cellspacing="2" id="CCInfo">
          <tr> 
            <td align="right"><label id="ID_CreditCard" for="Name">CreditCard</label></td>
            <td>
			  <select id="ID_CreditCard" name="CreditCard" size="1">
				<c:forEach items='${payment.creditCards}' var='cc'> 
				  <option value="<c:out value='${cc.value}'/>" <c:if test='${payment.creditCardType == cc.value}'>selected</c:if>><c:out value='${cc.name}'/></option> 
				</c:forEach>
			  </select>
			</td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_CreditCardNumber" for="CreditCardNumber">Credit Card Number</label></td>
            <td align="left">
			  <input class="Cmandatory" size="30" id="ID_CreditCardNumber" value='<c:out value="${payment.creditCardNumber}"/>' name="CreditCardNumber" maxlength="16" type="text"/> 
            </td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_Exp">Expiry Date</label></td>
            <td align="left"> <select id="ID_CreditCardExpMM" name="CreditCardExpMM" class="Cmandatory" size="1">
                <c:forEach var='mm' begin="1" end="12"> <option value="<c:out value='${mm}'/>" <c:if test='${payment.creditCardExpMM == mm}'>selected</c:if>><c:out value='${mm}'/></option> 
                </c:forEach> 
              </select>&nbsp;-&nbsp;
              <select id="ID_CreditCardExpYY" name="CreditCardExpYY" class="Cmandatory" size="1">
                <c:forEach var='yy' begin="5" end="20"> <option value="<c:out value='${yy}'/>" <c:if test='${payment.creditCardExpYY == yy}'>selected</c:if>><c:out value='${yy+2000}'/></option> 
                </c:forEach> 
              </select>&nbsp;
              <label id="ID_CreditCardVV" for="CreditCardVV">Validation 
              Code<sup>*</sup></label>&nbsp;<input class="Cmandatory" size="5" id="ID_CreditCardVV" value='<c:out value="${payment.creditCardVV}"/>' name="CreditCardVV" maxlength="4" type="text"/> 
            </td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_A_Name" for="A_Name">Name on Credit Card</label></td>
            <td align="left"><input class="Cmandatory" size="40" id="ID_A_Name" value='<c:out value="${payment.a_Name}"/>' name="A_Name" maxlength="60" type="text"/> 
            </td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_A_Street" for="A_Street">Street</label></td>
            <td align="left"><input class="Cmandatory" size="40" id="ID_A_Street" value='<c:out value="${payment.a_Street}"/>' name="A_Street" maxlength="60" type="text"/> 
            </td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_A_City" for="A_City">City</label></td>
            <td align="left"><input class="Cmandatory" size="40" id="ID_A_City" value='<c:out value="${payment.a_City}"/>' name="A_City" maxlength="40" type="text"/> 
            </td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_A_Zip" for="A_Zip">Zip</label></td>
            <td align="left"> <input class="Cmandatory" size="10" id="ID_A_Zip" value='<c:out value="${payment.a_Zip}"/>' name="A_Zip" maxlength="10" type="text"/> 
              &nbsp; <label id="ID_A_State" for="A_State">State</label> <input size="10" id="ID_A_State" value='<c:out value="${payment.a_State}"/>' name="A_State" maxlength="20" type="text"/> 
            </td>
          </tr>
          <tr> 
            <td align="right"><label id="ID_A_Country" for="A_Country">Country</label></td>
            <td align="left"><input class="Cmandatory" size="40" id="ID_A_Country" value='<c:out value="${payment.a_Country}"/>' name="A_Country" maxlength="40" type="text"/></td>
          </tr>
          <tr> 
            <td align="right">&nbsp;</td>
            <td align="left"><input name="SavePayment" type="checkbox" id="SavePayment" value="SavePayment" checked>
              Save Payment Information</td>
          </tr>
          <tr> 
            <td><input type="reset" name="Reset" value="Reset">
			</td>
            <td align="right"> 
			  <font color="#FF0000"><c:out value="${payment.errorMessage}"/></font>
			  <div id="processingDiv" style="display:none"><strong>Processing ...</strong></div>
			  <div id="submitDiv" style="display:inline">
              <input name="Submit" type="submit" id="Submit" value="Submit Payment">
		      </div> 
		    </td>
          </tr>
        </table>
        <div align="center">Please enter all <b class="Cmandatory">mandatory</b> 
          data. </div>
      </form>
      <table width="100%" border="0" align="left" cellpadding="0" cellspacing="0" id="CCValidation">
        <tr> 
          <td align="center">
		    <img src="visaCID.jpg" width="135" height="80" align="left">
            <img src="amexCID.jpg" width="135" height="80" align="right">
            <strong>Credit Card Validation Code <br>
            <font size="-1">(Card ID)<br>
            </font></strong><font size="-1">Visa and Mastercard: 3 digits - back<br>
            American Express: 4 digits - front</font>
		  </td>
        </tr>
      </table>
      <p>&nbsp;</p></td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
