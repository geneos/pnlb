<!--
  - Author:  Jorg Janke
  - Version: $Id: requestDetails.jsp,v 1.13 2005/10/08 02:02:44 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Request Details
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=requests.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - Request Details</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<!-- Set Request ID and get Request		-->
<c:set target='${info}' property='id' value='${param.R_Request_ID}' />
<c:set var='request' value='${info.request}' />
<c:if test='${empty request}'>
  <c:set target='${info}' property='message' value='Request not found' />
  <c:redirect url='requests.jsp'/>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter"> 
	  <p><a href="request.jsp">New Request</a></p>
	  <h1>Request <c:out value='${request.documentNo}'/></h1>
	  <c:if test='${not empty info.info}'>
	    <p><c:out value='${info.message}'/></p>
	  </c:if>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> <th>Summary</th>
          <td colspan="3"><c:out value='${request.summary}'/></td>
		</tr>
        <tr> <th>Type</th>
          <td><c:out value='${request.requestTypeName}'/></td>
          <th>Group:</th>
          <td><c:out value='${request.groupName}'/>&nbsp;</td>
		</tr>
        <tr> <th>Category</th>
          <td><c:out value='${request.categoryName}'/>&nbsp;</td>
          <th>Importance:</th>
          <td><c:out value='${request.priorityUserText}'/></td>
		</tr>
        <tr> <th>Status</th>
          <td><c:out value='${request.statusName}'/></td>
          <th>Priority:</th>
          <td><c:out value='${request.priorityText}'/></td>
		</tr>
		 <tr> <th>Resolution</th>
          <td><c:out value='${request.resolutionName}'/>&nbsp;</td>
          <th><c:out value='${request.dueTypeText}'/></th>
          <td><fmt:formatDate value='${request.dateNextAction}'/>&nbsp;</td>
		</tr>
        <tr> <th>Created</th>
          <td><fmt:formatDate value='${request.created}'/>&nbsp;</td>
          <td><c:out value='${request.createdByName}'/></td>
          <td><c:out value='${request.confidentialText}'/></td>
		</tr>
		 <tr> <th>Result</th>
          <td colspan="3"><c:out value='${request.result}'/>&nbsp;</td>
		</tr>
        <tr>
        <th>Attachments</th>
        <td colspan="3">
		<c:if test='${not empty request.attachment}'>
	    <c:out value='${request.attachment.textMsg}'/>:&nbsp;
	  	<c:forEach items='${request.attachment.entries}' var='entry'>
		  <a href="requestServlet?R_Request_ID=<c:out value='${request.r_Request_ID}'/>&AttachmentIndex=<c:out value='${entry.index}'/>" target="_blank">
		  <c:out value='${entry.name}'/>
		  </a>&nbsp;-&nbsp;
		</c:forEach>
	  </c:if>&nbsp;
      </td>
      </tr>
      </table>
	  <c:if test='${request.webCanUpdate}'>
	  <h3>Response</h3>
      <form method="post" name="Request" action="requestServlet" enctype="application/x-www-form-urlencoded"
			onSubmit="checkForm(this, new Array ('Summary'));">
        <input name="Source" type="hidden" value=""/>
        <input name="Info" type="hidden" value=""/>
        <script language="Javascript">
		  document.Request.Source.value=document.referrer;
		  document.Request.Info.value=document.lastModified;
	    </script>
	    <input name="ForwardTo" type="hidden" value="<c:out value='${param.ForwardTo}'/>"/>
        <input name="SalesRep_ID" type="hidden" value="<c:out value='${webUser.salesRep_ID}'/>"/>
        <input name="R_Request_ID" type="hidden" id="R_Request_ID" value="<c:out value='${request.r_Request_ID}'/>">
      <table border="0" align="center" cellpadding="2" cellspacing="2">
        <tr> 
          <td align="right">From <c:out value='${webUser.name}'/>:</td>
          <td align="left"><c:out value='${webUser.email}'/>&nbsp;</td>
        </tr>
        <tr> 
          <td>&nbsp;</td>
          <td><input name="Close" type="checkbox" id="Close" value="Close">Close request&nbsp; - &nbsp;
		  <input name="Escalate" type="checkbox" id="Escalate" value="Escalate">Escalate request
		  </td>
        </tr>
        <tr> 
          <td><label id="ID_Summary" for="Summary">Response - Follow-Up:</label></td>
          <td><input name="Confidential" type="checkbox" id="Confidential" value="Confidential">Confidential Information</td>
        </tr>
        <tr> 
          <td colspan="2" align="left"> <textarea name="Summary" cols="80" rows="8" id="ID_Summary"></textarea> 
        </td>
        </tr>
          <tr> 
            <td colspan="2" align="center"><font size="-1" color="#666666">1500 characters max - Longer text add as Attachment</font>
            </td>
          </tr>
        <tr> 
          <td> <input name="Reset" type="reset" value="Reset"/></td>
          <td align="right"> <input name="Submit" type="submit" value="Submit"> 
          </td>
        </tr>
		</table>
      </form>
	  <form action="requestServlet" method="post" enctype="multipart/form-data" name="fileLoad" id="fileLoad">
        <input name="R_Request_ID" type="hidden" id="R_Request_ID" value="<c:out value='${request.r_Request_ID}'/>">
      <table border="0" align="center" cellpadding="2" cellspacing="2">
	  <tr>
	  <td align="right">
	    <label for="file">Attach File: </label></td>
	  <td align="left">
	    <input name="file" type="file" id="file" size="40"></td>
	  <td align="right">
        <input type="submit" name="Submit" value="Upload"></td>
	  </tr>
      </table>
      </form>
	  </c:if>
	  <p>&nbsp;</p>
	  <h3>History</h3>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Created</th>
          <th>By</th>
          <th>Result</th>
        </tr>
        <c:forEach items='${request.updatesCustomer}' var='update'> 
        <tr> 
          <td><fmt:formatDate value='${update.created}'/></td>
          <td><c:out value='${update.createdByName}'/></td>
          <td><c:out value='${update.result}'/>&nbsp;</td>
        </tr>
        </c:forEach> 
      </table>
	  <br>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Updated</th>
          <th>By</th>
          <th>Old Values</th>
        </tr>
        <c:forEach items='${request.actions}' var='action'> 
        <tr> 
          <td valign="top"><fmt:formatDate value='${action.created}'/></td>
          <td valign="top"><c:out value='${action.createdByName}'/></td>
          <td valign="top"><c:out value='${action.changesHTML}' escapeXml='false'/>&nbsp;</td>
        </tr>
        </c:forEach> 
      </table>
      <p>&nbsp;</p>
    </td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
