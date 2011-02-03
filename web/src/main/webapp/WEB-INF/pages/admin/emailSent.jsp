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
        
        <s:if test="sendToAccepted">
        	<s:text name="admin.email.sentAccepted">
        		<s:param value="acceptedCompetitiors.size" />
        	</s:text>
        </s:if>
        <s:if test="sendToPending">
        	<s:text name="admin.email.sentPending">
        		<s:param value="pendingCompetitiors.size" />
        	</s:text>
        </s:if>
        
        <s:a action="email"><s:text name="admin.email.back"/></s:a>
    </body>
</html>