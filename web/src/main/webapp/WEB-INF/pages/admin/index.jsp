<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
    <head>
        <title><s:text name="competitions.title" /></title>
    </head>
    <body>
        <s:include value="/WEB-INF/decorators/adminMenu.jsp">
            <s:param name="selectedMenu">Home</s:param>
        </s:include>

        <h1><s:text name="admin.index.title" /></h1>
        
        <div style="width:85%; margin: auto; font-size: 1.3em">
            <p><s:text name="admin.index.description" /></p>
            
	        <dl>
	            <sec:authorize ifAllGranted="ROLE_ADMIN">
	                <s:url id="competitionsUrl" action="viewCompetitions" namespace="/admin" />
	                <dt><s:a href="%{competitionsUrl}"><s:text name="admin.menu.competitions" /></s:a></dt>
	                <dd><s:text name="admin.index.competitions" /></dd>
	            </sec:authorize>

	            <s:url id="scoresheetUrl" action="scoresheet" namespace="/admin" />
	            <dt><s:a href="%{scoresheetUrl}"><s:text name="admin.menu.scoresheet" /></s:a></dt>
	            <dd><s:text name="admin.index.scoresheet" /></dd>
	            
	            <s:url id="scramblesUrl" action="scrambles" namespace="/admin" />
                <dt><s:a href="%{scramblesUrl}"><s:text name="admin.menu.scrambles" /></s:a></dt>
                <dd><s:text name="admin.index.scrambles" /></dd>
                
	            <s:url id="resultsUrl" action="uploadResults" namespace="/admin" />
                <dt><s:a href="%{resultsUrl}"><s:text name="admin.index.results" /></s:a></dt>
                <dd><s:text name="admin.index.results" /></dd>

                <s:url id="exportResultsUrl" action="export" namespace="/admin" />
                <dt><s:a href="%{exportResultsUrl}"><s:text name="admin.index.export" /></s:a></dt>
                <dd><s:text name="admin.index.export" /></dd>
	            
	            <s:url id="diplomasUrl" action="diplomas" namespace="/admin" />
	            <dt><s:a href="%{diplomasUrl}"><s:text name="admin.menu.diplomas" /></s:a></dt>
	            <dd><s:text name="admin.index.diplomas" /></dd>
	            
	            <s:url id="emailUrl" action="email" namespace="/admin" />
	            <dt><s:a href="%{emailUrl}"><s:text name="admin.menu.email" /></s:a></dt>
	            <dd><s:text name="admin.index.email" /></dd>
	            
	            <dt><a href="<s:text name='admin.index.uploader.url' />"><s:text name="admin.index.uploader" /></a></dt>
	            <dd><s:text name="admin.index.uploader" /></dd>
	        </dl>
        </div>
    </body>
</html>