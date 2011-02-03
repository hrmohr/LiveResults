<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title><s:text name="records.title" /></title>
    </head>
    <body>
        <s:include value="/WEB-INF/decorators/menu.jsp">
            <s:param name="selectedMenu">Records</s:param>
            <s:param name="competitionId" value="competitionId" />
        </s:include>
    
        <s:if test="records.size > 0">
    
	        <h1><s:property value="competition.name" /></h1>

			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="results">
	            <thead>
	                <tr>
		                <th><s:text name="records.event" /></th>
		                <th><s:text name="records.competitor" /></th>
		                <th><s:text name="records.country" /></th>
		                <th><s:text name="records.single" /></th>
		                <th>&nbsp;</th>
		                <th><s:text name="records.average" /></th>
		                <th>&nbsp;</th>
		                <th class="f">&nbsp;</th>
	                </tr>
	            </thead>
	            <tbody>
					<s:iterator value="records" status="rowstatus">
						<s:text name="competitors.fullname" var="fullname">
							<s:param value="firstname" />
							<s:param value="surname" />
						</s:text>
			            <s:set value="getEventById(eventId).getName()" var="eventName" />
			            <s:set value="getEventById(eventId).getTimeFormat()" var="eventTimeFormat" />
						<tr class="<s:if test="#rowstatus.even == true">e</s:if>">
							<s:url id="resultUrl" action="results" namespace="/">
				                <s:param name="competitionId" value="competitionId" />
				                <s:param name="eventId" value="eventId" />
				            </s:url>
							<td><s:a href="%{resultUrl}"><s:property value="eventName" /></s:a></td>
							<s:if test="wcaId != null">
								<s:url id="wcaUrl"
									value="http://www.worldcubeassociation.org/results/p.php">
									<s:param name="i" value="wcaId" />
								</s:url>
								<td><s:a href="%{wcaUrl}" cssClass="p">
									<s:property value="fullname" />
								</s:a></td>
							</s:if>
							<s:else>
								<td><s:property value="fullname" /></td>
							</s:else>
							<td><s:property value="getCountryUtil().getCountryByCode(country)" /></td>
							<s:if test="regionalSingleRecord != null">
								<td align="right"><s:property value="getResultTimeFormat().format(best, #eventTimeFormat)" /></td>
							</s:if>
							<s:else>
								<td>&nbsp;</td>
							</s:else>
							<td><s:property value="regionalSingleRecord" /></td>
							<s:if test="regionalAverageRecord != null">
								<td align="right"><s:property value="getResultTimeFormat().format(average, #eventTimeFormat)" /></td>
							</s:if>
							<s:else>
								<td>&nbsp;</td>
							</s:else>
							<td><s:property value="regionalAverageRecord" /></td>
							<td class="f">&nbsp;</td>
						</tr>
					</s:iterator>
				</tbody>
            </table>
		</s:if>
		<s:else>
		  <p><s:text name="records.noactive" /></p>
		</s:else>
    </body>
</html>