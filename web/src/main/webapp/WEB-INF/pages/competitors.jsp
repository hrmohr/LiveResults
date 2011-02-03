<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title><s:text name="competitors.title" /></title>
    </head>
    <body>
        <s:include value="/WEB-INF/decorators/menu.jsp">
            <s:param name="selectedMenu">Competitors</s:param>
            <s:param name="competitionId" value="competitionId" />
        </s:include>
        
        <h1><s:property value="competition.name" /></h1>
        
        <table width="100%" id="competitionDetails">
            <tr valign="top"><td style="width:70%">
                <table>  
                    <tr><td class="key"><s:text name="competitors.date" /></td>
                    <s:if test="competition.startDate != competition.endDate">
                        <td><s:date name="competition.startDate" format="MMM d" /> - <s:date name="competition.endDate" format="MMM d" />, <s:date name="competition.startDate" format="yyyy" /></td>
                    </s:if>
                    <s:else>
                        <td><s:date name="competition.startDate" format="MMM d" />, <s:date name="competition.startDate" format="yyyy" /></td>
                    </s:else></tr>
                    <tr><td class="key"><s:text name="competitors.city" /></td><td><s:property value="competition.city" />, <s:property value="getCountryUtil().getCountryByCode(competition.country)" /></td></tr>
                    <tr><td class="key"><s:text name="competitors.venue" /></td><td><s:property value="competition.venue" /></td></tr>
                    <s:if test="competition.website != null">
                        <tr><td class="key"><s:text name="competitors.website" /></td><td><a class="external" target="_blank" href="<s:property value="competition.website" />"><span style="white-space:nowrap"><img src="<c:url value='/img/external_link.png'/>" border="0" alt="" /><s:property value="competition.name" /></span> <s:text name="competitors.website" /></a></td></tr>
                    </s:if>
                    <s:if test="competition.organiser != null">
                    	<s:if test="competition.organiserEmail != null">
                    		<tr><td class="key"><s:text name="competitors.organiser" /></td><td>
                    			<s:text name="competitors.email">
                    				<s:param value="competition.organiserEmail"/>
                    				<s:param><c:url value='/img/email_link.png'/></s:param>
                    				<s:param value="competition.organiser"/>
                    			</s:text>
                    		</td></tr>
                    	</s:if>
                    	<s:else>
                        	<tr><td class="key"><s:text name="competitors.organiser" /></td><td><s:property value="competition.organiser" /></td></tr>
                        </s:else>
                    </s:if>
                    <s:if test="competition.wcaDelegate != null">
                    	<s:if test="competition.wcaDelegateEmail != null">
                    		<tr><td class="key"><s:text name="competitors.delegate" /></td><td>
                    			<s:text name="competitors.email">
                    				<s:param value="competition.wcaDelegateEmail"/>
                    				<s:param><c:url value='/img/email_link.png'/></s:param>
                    				<s:param value="competition.wcaDelegate"/>
                    			</s:text>
                    		</td></tr>
                    	</s:if>
                    	<s:else>
                        	<tr><td class="key"><s:text name="competitors.delegate" /></td><td><s:property value="competition.wcaDelegate" /></td></tr>
                        </s:else>
                    </s:if>
                </table>
            </td></tr>
        </table>
            
        <s:if test="competition.competitors.size > 0 && competition.events.size > 0">
	        <s:form action="results" method="get">
	            <s:hidden name="competitionId" value="%{competitionId}" />
	            <table cellpadding="0" cellspacing="0" id="choices"><tr>
	            	<td><div class="space"><div class="buttborder"><s:submit value="%{getText('competitors.live')}" cssClass="butt"/></div></div></td>
	            	<s:if test="records.size > 0">
			            <s:url id="recordsUrl" action="records" namespace="/">
			                <s:param name="competitionId" value="competitionId" />
			            </s:url>
		            	<td>&nbsp;</td>
		            	<td><div class="space"><div class="buttborder"><s:a href="%{recordsUrl}" cssClass="butt"><s:text name="menu.records" /></s:a></div></div></td>
	            	</s:if>
	            </tr></table><br />
	        </s:form>
        </s:if>
        
        <s:if test="competition.competitors.size > 0">
            <h1><s:text name="competitors.title" /></h1><br />
            
            <s:set name="registeredEventsCount" value="registeredEventsCount"/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="results">
	            <thead>
	               <s:include value="/WEB-INF/pages/competitorHeader.jsp" />
	            </thead>
	            <tfoot>
                    <tr>
                        <th>&nbsp;</th>
                        <th><s:text name="competitors.total" /></th>
                        <th><s:property value="numberOfCountries" /></th>
                        <s:iterator value="#registeredEventsCount" var="count">
                            <c:if test="${count > 0}"><th class="c"><s:property /></th></c:if>
                        </s:iterator>
                        <th>&nbsp;</th>
                        <th class="f">&nbsp;</th>
                    </tr>
                </tfoot>
	            <tbody>
	                <s:iterator value="competition.competitors" status="rowstatus">
	                	<s:text name="competitors.fullname" var="fullname">
	                		<s:param value="firstname" />
	                		<s:param value="surname" />
	                	</s:text>
                        <tr class="<s:if test="#rowstatus.even == true">e</s:if>">
                           <td><s:property value="#rowstatus.index+1" /></td> 
	                       <s:if test="wcaId != null">
	                           <s:url id="wcaUrl" value="http://www.worldcubeassociation.org/results/p.php">
                                   <s:param name="i" value="wcaId" />
                               </s:url>
	                           <td><s:a href="%{wcaUrl}" cssClass="p"><s:property value="fullname" /></s:a></td>
	                       </s:if>
	                       <s:else>
	                           <td><s:property value="fullname" /></td>
	                       </s:else>
	                       <td><s:property value="getCountryUtil().getCountryByCode(country)" /></td>
	                       <s:include value="/WEB-INF/pages/competitorEvents.jsp" />
	                       <td class="r"><s:property value="registeredEvents.numberOfEvents" /></td>
	                       <td class="f">&nbsp;</td>
	                    </tr>
                        <s:if test="(#rowstatus.index+1) % 20 == 0 && competition.competitors.size > 20">
	                       <s:include value="/WEB-INF/pages/competitorHeader.jsp" />
	                    </s:if>
	                </s:iterator>
	            </tbody>
	        </table>
        </s:if>
    </body>
</html>