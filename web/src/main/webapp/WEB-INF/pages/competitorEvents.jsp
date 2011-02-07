<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<s:iterator value="#registeredEventsCount" var="count" status="rowstatus">
    <c:if test="${count > 0}">
        <s:if test="#rowstatus.index == 0">
            <td class="c"><s:if test="registeredEvents.signedUpFor2x2">X</s:if><s:else>-</s:else></td>
        </s:if>
        <s:elseif test="#rowstatus.index == 1">
            <td class="c"><s:if test="registeredEvents.signedUpFor3x3">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 2">
            <td class="c"><s:if test="registeredEvents.signedUpFor4x4">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 3">
            <td class="c"><s:if test="registeredEvents.signedUpFor5x5">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 4">
            <td class="c"><s:if test="registeredEvents.signedUpFor6x6">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 5">
            <td class="c"><s:if test="registeredEvents.signedUpFor7x7">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 6">
            <td class="c"><s:if test="registeredEvents.signedUpForFm">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 7">
            <td class="c"><s:if test="registeredEvents.signedUpForOh">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 8">
            <td class="c"><s:if test="registeredEvents.signedUpForBf">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 9">
            <td class="c"><s:if test="registeredEvents.signedUpForBf4">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 10">
            <td class="c"><s:if test="registeredEvents.signedUpForBf5">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 11">
            <td class="c"><s:if test="registeredEvents.signedUpForFeet">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 12">
            <td class="c"><s:if test="registeredEvents.signedUpForClk">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 13">
            <td class="c"><s:if test="registeredEvents.signedUpForMgc">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 14">
            <td class="c"><s:if test="registeredEvents.signedUpForMmgc">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 15">
            <td class="c"><s:if test="registeredEvents.signedUpForMinx">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 16">
            <td class="c"><s:if test="registeredEvents.signedUpForSq1">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 17">
            <td class="c"><s:if test="registeredEvents.signedUpForPyr">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 18">
            <td class="c"><s:if test="registeredEvents.signedUpForMbf">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 19">
        	<td class="c"><s:if test="registeredEvents.signedUpFor333ni">X</s:if><s:else>-</s:else></td>
        </s:elseif>
        <s:elseif test="#rowstatus.index == 20">
			<td class="c"><s:if test="registeredEvents.signedUpFor333sbf">X</s:if><s:else>-</s:else></td>
		</s:elseif>
		<s:elseif test="#rowstatus.index == 21">
			<td class="c"><s:if test="registeredEvents.signedUpFor333r3">X</s:if><s:else>-</s:else></td>
		</s:elseif>
		<s:elseif test="#rowstatus.index == 22">
			<td class="c"><s:if test="registeredEvents.signedUpFor333ts">X</s:if><s:else>-</s:else></td>
		</s:elseif>
		<s:elseif test="#rowstatus.index == 23">
			<td class="c"><s:if test="registeredEvents.signedUpFor333bts">X</s:if><s:else>-</s:else></td>
		</s:elseif>
		<s:elseif test="#rowstatus.index == 24">
			<td class="c"><s:if test="registeredEvents.signedUpFor222bf">X</s:if><s:else>-</s:else></td>
		</s:elseif>
		<s:elseif test="#rowstatus.index == 25">
			<td class="c"><s:if test="registeredEvents.signedUpFor333si">X</s:if><s:else>-</s:else></td>
		</s:elseif>
		<s:elseif test="#rowstatus.index == 26">
			<td class="c"><s:if test="registeredEvents.signedUpForRainb">X</s:if><s:else>-</s:else></td>
		</s:elseif>
		<s:elseif test="#rowstatus.index == 27">
			<td class="c"><s:if test="registeredEvents.signedUpForSnake">X</s:if><s:else>-</s:else></td>
		</s:elseif>
		<s:elseif test="#rowstatus.index == 28">
			<td class="c"><s:if test="registeredEvents.signedUpForSkewb">X</s:if><s:else>-</s:else></td>
		</s:elseif>
		<s:elseif test="#rowstatus.index == 29">
			<td class="c"><s:if test="registeredEvents.signedUpForMirbl">X</s:if><s:else>-</s:else></td>
		</s:elseif>
		<s:elseif test="#rowstatus.index == 30">
			<td class="c"><s:if test="registeredEvents.signedUpFor222oh">X</s:if><s:else>-</s:else></td>
		</s:elseif>
		<s:elseif test="#rowstatus.index == 31">
			<td class="c"><s:if test="registeredEvents.signedUpForMagico">X</s:if><s:else>-</s:else></td>
		</s:elseif>
		<s:elseif test="#rowstatus.index == 32">
			<td class="c"><s:if test="registeredEvents.signedUpFor360">X</s:if><s:else>-</s:else></td>
		</s:elseif>
    </c:if>
</s:iterator>