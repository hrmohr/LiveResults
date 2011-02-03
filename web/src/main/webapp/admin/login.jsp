<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
    <head>
        <title><s:text name="admin.login.title" /></title>
        <script type="text/javascript">
        window.onload=function() {
            document.getElementById('j_username').focus();
        }
        </script>
    </head>
    <body>
        <s:include value="/WEB-INF/decorators/menu.jsp" />

        <h1><s:text name="admin.login.title" /></h1>
        
        <s:form action="/j_spring_security_check" method="POST">
            <table id="choices" cellpadding="0" cellspacing="0" class="formTable">
	            <tr><td align="right"><s:textfield name="j_username" label="User" labelposition="left" id="j_username"/></td></tr>
	            <tr><td align="right"><s:password name="j_password" label="Password" labelposition="left" /></td></tr>
	            <tr><td align="right"><table cellpadding="0" cellspacing="0"><tr><td><div class="buttborder"><s:submit value="%{getText('admin.login.submit')}" cssClass="butt"/></div></td></tr></table></td></tr>
            </table>
        </s:form>
    </body>
</html>