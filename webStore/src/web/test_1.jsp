<!--
  - Author:  Jorg Janke
  - Version: $Id: test_1.jsp,v 1.3 2005/10/08 02:02:44 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Interest Area
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/>- My Test Area</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr>
    <td align="left" valign="top" class="centerLeft"><%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"><h1>WebStore Links</h1>
      <p>Add Product to Web Store: <a href="/wstore/basketServlet?M_Product_ID=128">test Az</a> - <a href="/wstore/basketServlet?M_Product_ID=123">test Oak</a></p>
      <p>Create Request for Sales Rep: <a href="/wstore/request.jsp?SalesRep_ID=102">test GU </a>  - <a href="/wstore/request.jsp?SalesRep_ID=101">test GA</a></p>
      <h1>EL Test</h1>
      <p>\${1.2 + 2.3} = ${1.2 + 2.3}</p>
      <h1>Test for Each</h1>
      <c:forEach var="i" begin="1" end="10" step="1"> <c:out value='${i}'/> <br />
      </c:forEach>
      <h1>Form</h1>
      <form name="form1" method="post" action="">
        <p>
          <input name="myField" type="text" id="myField">
        </p>
        <p>
          <textarea name="myText" id="myText"></textarea>
        </p>
        <p>
          <select name="myList" size="1" id="myList">
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
          </select>
        </p>
        <p>
          <input type="submit" name="Submit" value="Submit">
        </p>
      </form>
      <h1>Table</h1>
      <table  border="1" cellspacing="2" cellpadding="2" summary="Summary">
        <caption>
        caption
        </caption>
        <tr>
          <th scope="col">c1</th>
          <th width="10" nowrap scope="col">c2</th>
          <th scope="col">c3</th>
          <th scope="col">c4</th>
        </tr>
        <tr>
          <td>c1</td>
          <td width="10" nowrap>c2</td>
          <td>c3</td>
          <td>c4</td>
        </tr>
        <tr>
          <td>c1</td>
          <td width="10" nowrap>c2</td>
          <td>c3</td>
          <td>c4</td>
        </tr>
      </table>
    <p>&nbsp;</p></td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
