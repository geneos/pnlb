<!--
  - Author:  Jorg Janke
  - Version: $Id: rfqDetails.jsp,v 1.9 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store RfQ Dateils
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=rfqs.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My RfQ Details</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<!-- Set RfQ ID and get RfQ		-->
<c:set target='${info}' property='id' value='${param.C_RfQ_ID}' />
<c:set var='rfqResponse' value='${info.rfQResponse}' />
<c:if test='${empty rfqResponse}'>
  <c:set target='${info}' property='message' value='No RfQ Response' />
  <c:redirect url='rfqs.jsp'/>
</c:if>
<c:set var='rfq' value='${rfqResponse.rfQ}' />
<c:if test='${empty rfq}'>
  <c:set target='${info}' property='message' value='RfQ not found' />
  <c:redirect url='rfqs.jsp'/>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
	<h1>My RfQ Details: <c:out value='${rfq.name}'/></h1>
	  <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
      <form action="rfqServlet" method="post" enctype="application/x-www-form-urlencoded" name="RfQResponse">
	  <input name="C_RfQ_ID" type="hidden" value="<c:out value='${rfqResponse.c_RfQ_ID}'/>"/>
	  <input name="C_RfQResponse_ID" type="hidden" value="<c:out value='${rfqResponse.c_RfQResponse_ID}'/>"/>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Name</th>
          <th>Desciption</th>
          <th>Details</th>
          <th>Response</th>
          <th>Work Start</th>
          <th>Delivery</th>
        </tr>
        <tr> 
          <td><c:out value='${rfq.name}'/></td>
          <td><c:out value='${rfq.description}'/>&nbsp;</td>
          <td><c:out value='${rfq.help}'/>&nbsp;
		  <c:if test='${rfq.pdfAttachment}'><br>
		  <a href="rfqServlet/RfQ_<c:out value='${rfq.c_RfQ_ID}'/>.pdf?C_RfQ_ID=<c:out value='${rfq.c_RfQ_ID}'/>" target="_blank"><img src="pdf.gif" alt="Get Report" width="30" height="30" border="0"></a>
		  </c:if>
		  </td>
          <td>By <fmt:formatDate value='${rfq.dateResponse}' dateStyle='short'/></td>
          <td><fmt:formatDate value='${rfq.dateWorkStart}' dateStyle='short'/></td>
          <td><fmt:formatDate value='${rfq.dateWorkComplete}' dateStyle='short'/> - <c:out value='${rfq.deliveryDays}'/> days</td>
        </tr>
        <tr> 
          <td><input name="Name" type="text" id="Name" value="<c:out value='${rfqResponse.name}'/>"></td>
          <td><textarea name="Description" rows="3" id="Description"><c:out value='${rfqResponse.description}'/></textarea></td>
          <td><textarea name="Help" rows="3" id="Help"><c:out value='${rfqResponse.help}'/></textarea></td>
          <td><fmt:formatDate value='${rfqResponse.dateResponse}' dateStyle='short'/>
            <br>Total: 
            <input name="Price" type="text" id="Price" value="<fmt:formatNumber value='${rfqResponse.price}' type='currency' currencySymbol=''/>" size="15">
          </td>
          <td><input name="DateWorkStart" type="text" id="DateWorkStart" value="<fmt:formatDate value='${rfqResponse.dateWorkStart}' dateStyle='short'/>" size="12">
          </td>
          <td><input name="DateWorkComplete" type="text" id="DateWorkComplete" value="<fmt:formatDate value='${rfqResponse.dateWorkComplete}' dateStyle='short'/>" size="12"> 
          - <input name="DeliveryDays" type="text" id="DeliveryDays" value="<c:out value='${rfqResponse.deliveryDays}'/>" size="4" maxlength="4"> 
		  days
		  </td>
        </tr>
      </table>
	  <h3>Lines</h3>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>#</th>
          <th>Product</th>
          <th>Description</th>
          <th>Details</th>
          <th>Work Start</th>
          <th>Delivery</th>
        </tr>
        <c:forEach items='${rfqResponse.lines}' var='line'> 
		<c:set var='rfqLine' value='${line.rfQLine}' />
        <tr> 
          <td><c:out value='${rfqLine.line}'/></td>
          <td><c:out value='${rfqLine.productDetailHTML}' escapeXml='false'/>&nbsp;</td>
          <td><c:out value='${rfqLine.description}'/><br>
		  <textarea name="Description_<c:out value='${line.c_RfQResponseLine_ID}'/>" rows="3" id="Description"><c:out value='${line.description}'/></textarea></td>
          <td><c:out value='${rfqLine.help}'/><br>
		  <textarea name="Help_<c:out value='${line.c_RfQResponseLine_ID}'/>" rows="3" id="Help"><c:out value='${line.help}'/></textarea></td>
          <td><fmt:formatDate value='${rfqLine.dateWorkStart}' dateStyle='short'/><br>
		  <input name="DateWorkStart_<c:out value='${line.c_RfQResponseLine_ID}'/>" type="text" id="DateWorkStart" value="<fmt:formatDate value='${line.dateWorkStart}' dateStyle='short'/>" size="12"></td>
          <td><fmt:formatDate value='${rfqLine.dateWorkComplete}' dateStyle='short'/> - <c:out value='${rfqLine.deliveryDays}'/> days<br>
		  <input name="DateWorkComplete_<c:out value='${line.c_RfQResponseLine_ID}'/>" type="text" id="DateWorkComplete" value="<fmt:formatDate value='${line.dateWorkComplete}' dateStyle='short'/>" size="12"> 
          - <input name="DeliveryDays_<c:out value='${line.c_RfQResponseLine_ID}'/>" type="text" id="DeliveryDays" value="<c:out value='${line.deliveryDays}'/>" size="4" maxlength="4"> 
		  days</td>
        </tr>
        <tr> 
		  <td colspan="2">&nbsp;</td>
		  <td colspan="4">
		  <table width="100%" border="1" cellspacing="2" cellpadding="2">
		  <tr>
          	<th>UOM</th>
          	<th>Quantity</th>
			<c:if test='${not rfq.quoteTotalAmtOnly}'>
          	<th>Price</th>
          	<th>Discount</th>
			</c:if>
          </tr>
          <c:forEach items='${line.qtys}' var='qty'> 
		  <c:set var='rfqQty' value='${qty.rfQLineQty}'/>
		  <tr>
		    <td><c:out value='${rfqQty.uomName}'/></td>
		    <td><c:out value='${rfqQty.qty}'/></td>
			<c:if test='${not rfq.quoteTotalAmtOnly}'>
		    <td><input name="Price_<c:out value='${qty.c_RfQResponseLineQty_ID}'/>" type="text" id="Price" value="<fmt:formatNumber value='${qty.price}' type='currency' currencySymbol=''/>" size="15"></td>
		    <td><input name="Discount_<c:out value='${qty.c_RfQResponseLineQty_ID}'/>" type="text" id="Discount" value="<c:out value='${qty.discount}'/>" size="15"></td>
			</c:if>
		  </tr>
		  </c:forEach>
		  </table>
		  </td>
		</tr>
        </c:forEach> 
      </table>
      <p><input type="checkbox" name="IsComplete" value="IsComplete" id="IsComplete"><label for="IsComplete">Complete</label>
	  &nbsp;<input name="Submit" type="submit" id="Submit" value="Submit"></p>
	  </form></td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
