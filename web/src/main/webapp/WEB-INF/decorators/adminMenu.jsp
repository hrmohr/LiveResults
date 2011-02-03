<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div id="pageMenuFrame">
  <div id="pageMenu">
    <table summary="This table gives other relevant links" cellpadding="0" cellspacing="0"><tbody><tr>
        <s:url id="indexUrl" action="index" namespace="/admin" />
        <c:choose>
            <c:when test="${param.selectedMenu == 'Home'}"><td><div class="item"><s:a href="%{indexUrl}" id="activePage"><s:text name="menu.home" /></s:a></div></td></c:when>
            <c:otherwise><td><div class="item"><s:a href="%{indexUrl}"><s:text name="menu.home" /></s:a></div></td></c:otherwise>
        </c:choose>
        <sec:authorize ifAllGranted="ROLE_ADMIN">
	        <s:url id="competitionsUrl" action="viewCompetitions" namespace="/admin" />
	        <c:choose>
	            <c:when test="${param.selectedMenu == 'Competitions'}"><td><div class="item"><s:a href="%{competitionsUrl}" id="activePage"><s:text name="admin.menu.competitions" /></s:a></div></td></c:when>
	            <c:otherwise><td><div class="item"><s:a href="%{competitionsUrl}"><s:text name="admin.menu.competitions" /></s:a></div></td></c:otherwise>
	        </c:choose>
	        <s:url id="scramblesUrl" action="scrambles" namespace="/admin" />
	        <c:choose>
	            <c:when test="${param.selectedMenu == 'Scrambles'}"><td><div class="item"><s:a href="%{scramblesUrl}" id="activePage"><s:text name="admin.menu.scrambles" /></s:a></div></td></c:when>
	            <c:otherwise><td><div class="item"><s:a href="%{scramblesUrl}"><s:text name="admin.menu.scrambles" /></s:a></div></td></c:otherwise>
	        </c:choose>
        </sec:authorize>
        <s:url id="emailUrl" action="email" namespace="/admin" />
        <c:choose>
            <c:when test="${param.selectedMenu == 'Email'}"><td><div class="item"><s:a href="%{emailUrl}" id="activePage"><s:text name="admin.menu.email" /></s:a></div></td></c:when>
            <c:otherwise><td><div class="item"><s:a href="%{emailUrl}"><s:text name="admin.menu.email" /></s:a></div></td></c:otherwise>
        </c:choose>
        <s:url id="scoresheetUrl" action="scoresheet" namespace="/admin" />
        <c:choose>
            <c:when test="${param.selectedMenu == 'Scoresheet'}"><td><div class="item"><s:a href="%{scoresheetUrl}" id="activePage"><s:text name="admin.menu.scoresheet" /></s:a></div></td></c:when>
            <c:otherwise><td><div class="item"><s:a href="%{scoresheetUrl}"><s:text name="admin.menu.scoresheet" /></s:a></div></td></c:otherwise>
        </c:choose>
        <s:url id="scramblesUrl" action="scrambles" namespace="/admin" />
        <c:choose>
            <c:when test="${param.selectedMenu == 'Scrambles'}"><td><div class="item"><s:a href="%{scramblesUrl}" id="activePage"><s:text name="admin.menu.scrambles" /></s:a></div></td></c:when>
            <c:otherwise><td><div class="item"><s:a href="%{scramblesUrl}"><s:text name="admin.menu.scrambles" /></s:a></div></td></c:otherwise>
        </c:choose>
        <s:url id="resultsUrl" action="uploadResults" namespace="/admin" />
        <c:choose>
            <c:when test="${param.selectedMenu == 'Upload results'}"><td><div class="item"><s:a href="%{resultsUrl}" id="activePage"><s:text name="admin.menu.results" /></s:a></div></td></c:when>
            <c:otherwise><td><div class="item"><s:a href="%{resultsUrl}"><s:text name="admin.menu.results" /></s:a></div></td></c:otherwise>
        </c:choose>
        <s:url id="diplomasUrl" action="diplomas" namespace="/admin" />
        <c:choose>
            <c:when test="${param.selectedMenu == 'Diplomas'}"><td><div class="item"><s:a href="%{diplomasUrl}" id="activePage"><s:text name="admin.menu.diplomas" /></s:a></div></td></c:when>
            <c:otherwise><td><div class="item"><s:a href="%{diplomasUrl}"><s:text name="admin.menu.diplomas" /></s:a></div></td></c:otherwise>
        </c:choose>
        <td><div class="item"><a href="<c:url value="/j_spring_security_logout"/>"><s:text name="admin.menu.logout"/></a></div></td>
    </tr></tbody></table>
  </div>
</div>
<div id="header"><s:text name="menu.header1" /><br /><s:text name="menu.header2" /></div>