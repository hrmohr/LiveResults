<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<tr>
    <th>#</th>
    <th><s:text name="competitors.name" /></th>
    <th><nobr><s:text name="competitors.country" /></nobr></th>
    <s:iterator value="#registeredEventsCount" var="count" status="rowstatus">
	    <c:if test="${count > 0}">
            <th class="c"><s:text name="competitors.event%{#rowstatus.index+1}" /></th>
	    </c:if>
	</s:iterator>
	<th>&nbsp;</th>
    <th class="f">&nbsp;</th>
</tr>