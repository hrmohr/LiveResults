<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
    <head>
        <title><s:text name="admin.export.title" /></title>
    </head>
    <body>
        <s:include value="/WEB-INF/decorators/adminMenu.jsp">
            <s:param name="selectedMenu">Submit results</s:param>
        </s:include>

        <h1><s:text name="admin.export.title" /></h1>

        <p><s:text name="admin.export.submitted" /></p>

        <s:a action="export"><s:text name="admin.export.back" /></s:a>
    </body>
</html>