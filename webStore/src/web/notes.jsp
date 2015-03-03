<!--
  - Author:  Jorg Janke
  - Version: $Id: notes.jsp,v 1.8 2005/09/11 02:27:52 jjanke Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Notes / Workflow
  -->
<%@ page session="true" contentType="text/html; charset=UTF-8" errorPage="errorPage.jsp" %>
<%@ taglib uri="webStore.tld" prefix="cws" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=notes.jsp'/>
</c:if>
<html>
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Notices</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="centerTable">
  <tr> 
    <td align="left" valign="top" class="centerLeft"> 
      <%@ include file="/WEB-INF/jspf/left.jspf" %></td>
    <td class="centerCenter">
      <h1>My Notices</h1>
      <c:if test='${not empty info.info}'>
	    <p><b><c:out value='${info.message}'/></b></p>
	  </c:if>
	  <h3>Workflow</h3>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Created<br><i>Priority</i></th>
          <th>Workflow Step<br><i>Description</i></th>
          <th>History</th>
          <th>Answer</th>
        </tr>
        <c:forEach items='${info.activities}' var='act'> 
        <tr> 
		<form action="workflowServlet" method="post" enctype="application/x-www-form-urlencoded" name="Activity">
          <input name="AD_WF_Activity_ID" type="hidden" value="<c:out value='${act.AD_WF_Activity_ID}'/>"/>
          <td><fmt:formatDate value='${act.created}'/><br><i><c:out value='${act.priority}'/></i></td>
          <td><c:out value='${act.nodeName}'/><br>
          <i><c:out value='${act.nodeDescription}'/></i>
  		  <c:if test='${not empty act.attachment}'>
		    <c:out value='${act.attachment.textMsg}'/>:&nbsp;
		  	<c:forEach items='${act.attachment.entries}' var='entry'>
		  	  <a href="workflowServlet?AD_WF_Activity_ID=<c:out value='${act.AD_WF_Activity_ID}'/>&AttachmentIndex=<c:out value='${entry.index}'/>" target="_blank">
		  	  <c:out value='${entry.name}'/>
		  	  </a>&nbsp;-&nbsp;
			</c:forEach>
	  	  </c:if>
          </td>
          <td><c:out value='${act.historyHTML}' escapeXml='false'/></td>
          <td><textarea name="textMsg" cols="30" rows="3" id="textMsg"></textarea><br>
			<cws:workflow activityID="${act.AD_WF_Activity_ID}" />
            <input type="submit" name="Submit" value="Submit">
          </td>
		</form>
		</tr>
        </c:forEach> 
      </table>
	  <br>	  
	  <h3>Notices</h3>
      <table width="100%" border="1" cellspacing="2" cellpadding="2">
        <tr> 
          <th>Created</th>
          <th>Message</th>
          <th>Reference</th>
          <th>Description</th>
          <th>Text</th>
          <th>Answer</th>
        </tr>
        <c:forEach items='${info.notes}' var='note'> 
        <tr> 
 		<form action="noteServlet" method="post" enctype="application/x-www-form-urlencoded" name="Notice">
          <input name="AD_Note_ID" type="hidden" value="<c:out value='${note.AD_Note_ID}'/>"/>
          <td><fmt:formatDate value='${note.created}'/></td>
          <td><c:out value='${note.message}'/></td>
          <td><c:out value='${note.reference}'/>&nbsp;
		  <c:if test='${not empty note.attachment}'>
		    <c:out value='${note.attachment.textMsg}'/>:&nbsp;
		  	<c:forEach items='${note.attachment.entries}' var='entry'>
		  	  <a href="noteServlet?AD_Note_ID=<c:out value='${note.AD_Note_ID}'/>&AttachmentIndex=<c:out value='${entry.index}'/>" target="_blank">
		  	  <c:out value='${entry.name}'/>
		  	  </a>&nbsp;-&nbsp;
			</c:forEach>
	  	  </c:if>
		  </td>
          <td><c:out value='${note.textMsg}'/>&nbsp;</td>
          <td><c:out value='${note.description}'/>&nbsp;</td>
          <td>
            <label>
            <input name="Processed" type="checkbox" id="Processed" value="Processed">
            Acknowledge</label>
            <input name="Update" type="submit" id="Update" value="Update">
		  </td>
		</form>
        </tr>
        </c:forEach> 
      </table>
    </td>
    <td align="right" valign="top" class="centerRight"><%@ include file="/WEB-INF/jspf/right.jspf" %></td>
  </tr>
</table>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</body>
</html>
