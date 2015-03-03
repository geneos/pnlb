<!--
  - Author:  Jorg Janke
  - Version: $Id: index.jsp,v 1.25 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Index
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
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter">
	  <c:out value='${ctx.webParam2}' escapeXml='false'/>
      <form action="productServlet" method="post" enctype="application/x-www-form-urlencoded" name="search" id="search">
		<p><b>Enter Search: </b><label for="SearchString">Product </label> 
        <input name="SearchString" type="text" id="SearchString">
		<cws:productCategoryList/>
        <input type="submit" name="Submit" value="Search"></p>
		<c:if test='${priceList.notAllPrices}'>
		  <p><i>Not all Products displayed - enter Search criteria to limit selection</i></p>
		</c:if>
		<c:if test='${priceList.noLines}'>
		  <p><i>No Products found - enter Search criteria</i></p>
		</c:if>
      </form>
      <form action="basketServlet" method="post" enctype="application/x-www-form-urlencoded" name="products" id="products">
        <input name="M_PriceList_ID" type="hidden" value="<c:out value='${priceList.priceList_ID}'/>">
        <input name="M_PriceList_Version_ID" type="hidden" value="<c:out value='${priceList.priceList_Version_ID}'/>">
        <table width="100%" border="1" cellpadding="2" cellspacing="2">
          <tr> 
            <th colspan="2" align="left">Product</th>
            <th>Description</th>
            <th align="right"><c:out value='${priceList.currency}'/>&nbsp;Price</th>
            <th align="right">Quantity</th>
            <th>UOM</th>
            <th>&nbsp;</th>
          </tr>
          <c:forEach items='${priceList.prices}' var='product'> 
          <tr> 
            <td><c:if test='${not empty product.imageURL}'><img src="<c:out value='${product.imageURL}'/>"></c:if></td>
            <td> <input name="Name_<c:out value='${product.id}'/>" type="hidden" value="<c:out value='${product.name}'/>"> 
			  <c:if test='${not empty product.descriptionURL}'><a href="<c:out value='${product.descriptionURL}'/>" target="pd"></c:if>
              <c:out value='${product.name}'/> 
			  <c:if test='${not empty product.descriptionURL}'></a></c:if>
			</td>
            <td><c:out value='${product.description}'/> <c:if test="${empty product.description}">&nbsp;</c:if> 
            </td>
            <td align="right"> <input name="Price_<c:out value='${product.id}'/>" type="hidden" value="<c:out value='${product.price}'/>"> 
              <fmt:formatNumber value='${product.price}' type="currency" currencySymbol=""/> </td>
            <td align="right"> <input name="Qty_<c:out value='${product.id}'/>" type="text" id="qty_<c:out value='${product.id}'/>" value="1" size="5" maxlength="5"> 
            </td>
            <td><c:out value='${product.uomName}'/>&nbsp;</td>
            <td> <input name="Add_<c:out value='${product.id}'/>" type="submit" id="Add_<c:out value='${product.id}'/>" value="Add"> 
            </td>
          </tr>
          </c:forEach> 
        </table>
      </form>
	  <p><font size="-1">Price List: <c:out value='${priceList.name}'/>  (<c:out value='${priceList.priceCount}'/>) - <c:out value='${priceList.searchInfo}'/></font></p>
      <p>&nbsp;</p></td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
