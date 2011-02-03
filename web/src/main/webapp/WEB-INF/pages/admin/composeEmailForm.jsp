<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
    <head>
        <title><s:text name="admin.email.title" /></title>
    </head>
    <body>
        <s:include value="/WEB-INF/decorators/adminMenu.jsp">
            <s:param name="selectedMenu">Email</s:param>
        </s:include>

        <h1><s:text name="admin.email.title" /></h1>
        <s:form method="POST" action="sendEmail">
        	<s:token />
        	<table>
        		<tr>
        			<td valign="bottom"><s:checkbox id="sendToAccepted" name="sendToAccepted" label="%{getText('admin.email.sendToAccepted')}" labelposition="right" /></td>
        			<td>&nbsp;</td>
        			<td valign="bottom"><s:checkbox id="sendToPending" name="sendToPending" label="%{getText('admin.email.sendToPending')}" labelposition="right" /></td>
        		</tr>
        	</table>
        	<table>
        		<tr><td><s:textfield id="subject" name="subject" required="true" label="%{getText('admin.email.subject')}"/></td></tr>
        		<tr><td><s:textarea id="body" name="body" required="true" label="%{getText('admin.email.body')}" cols="40" rows="10"/></td></tr>
        	</table>
        	<table cellpadding="0" cellspacing="0" id="choices"><tr><td>
	            <div class="space"><div class="buttborder"><s:submit value="%{getText('admin.email.submit')}" cssClass="butt"/></div></div>
	        </td></tr></table>
        </s:form>
    </body>
</html>