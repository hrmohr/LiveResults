<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title><s:text name="competitions.title" /></title>
    </head>
    <body>
        <s:include value="/WEB-INF/decorators/adminMenu.jsp">
            <s:param name="selectedMenu">Competitions</s:param>
        </s:include>

        <h1><s:text name="competitions.title" /></h1>
        
        <s:if test="competition != null">
            <s:form action="saveCompetition">
                <table>
	                <tr><td><s:textfield id="competitionId" name="competition.competitionId" label="%{getText('admin.competition.id')}" required="true" /></td></tr>
	                <tr><td><s:password id="password" name="password" label="%{getText('admin.competition.password')}" required="true" /></td></tr>
	                <tr><td><s:textfield id="name" name="competition.name" label="%{getText('admin.competition.name')}" required="true" /></td></tr>
	                <tr><td><s:textfield id="city" name="competition.city" label="%{getText('admin.competition.city')}" required="true" /></td></tr>
	                <tr><td><s:textfield id="country" name="competition.country" label="%{getText('admin.competition.country')}" required="true" /></td></tr>
	                <tr><td><s:textfield id="venue" name="competition.venue" label="%{getText('admin.competition.venue')}" /></td></tr>
	                <tr><td><s:textfield id="website" name="competition.website" label="%{getText('admin.competition.website')}" /></td></tr>
	                <tr><td><s:textfield id="organiser" name="competition.organiser" label="%{getText('admin.competition.organiser')}" /></td></tr>
	                <tr><td><s:textfield id="organiserEmail" name="competition.organiserEmail" label="%{getText('admin.competition.organiser.email')}" /></td></tr>
	                <tr><td><s:textfield id="wcaDelegate" name="competition.wcaDelegate" label="%{getText('admin.competition.delegate')}" /></td></tr>
	                <tr><td><s:textfield id="wcaDelegateEmail" name="competition.wcaDelegateEmail" label="%{getText('admin.competition.delegate.email')}" /></td></tr>
	                <tr><td><s:textfield id="startDate" name="competition.startDate" label="%{getText('admin.competition.startDate')}" required="true" /></td></tr>
	                <tr><td><s:textfield id="endDate" name="competition.endDate" label="%{getText('admin.competition.endDate')}" required="true" /></td></tr>
	                <tr><td><s:checkbox id="enabled" name="enabled" label="%{getText('admin.competition.enabled')}" labelposition="right" /></td></tr>
	                <tr><td valign="bottom" align="right"><table><tr><td><div class="space"><div class="buttborder"><s:submit value="%{getText('admin.competition.submit')}" cssClass="butt"/></div></div></td></tr></table></td></tr>
                </table>
            </s:form>
        </s:if>
        <s:else>
            <p><s:text name="competitions.noactive" /></p>
        </s:else>
    </body>
</html>