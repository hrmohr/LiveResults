<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title><s:text name="competitions.title" /></title>
    </head>
    <body>
        <s:include value="/WEB-INF/decorators/menu.jsp">
            <s:param name="selectedMenu">Home</s:param>
        </s:include>

        <h1><s:text name="competitions.title" /></h1>
        
        <s:if test="competitions.size > 0">
	        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="results">
	            <thead>
	                <tr>
		                <th><s:text name="competitions.year" /></th>
		                <th><s:text name="competitions.date" /></th>
		                <th><s:text name="competitions.name" /></th>
		                <th><s:text name="competitions.country" />,&nbsp;<s:text name="competitions.city" /></th>
		                <th class="f"><s:text name="competitions.venue" /></th>
	                </tr>
	            </thead>
	            <tbody>
			        <s:iterator value="competitions" status="rowstatus">
			          <s:url id="competitorsUrl" action="competitors">
			              <s:param name="competitionId" value="competitionId" />
			          </s:url>
			          <tr class="<s:if test="#rowstatus.even == true">e</s:if>">
			              <td><s:date name="startDate" format="yyyy" /></td>
			              <s:if test="startDate != endDate">
	                          <td><s:date name="startDate" format="MMM d" /> - <s:date name="endDate" format="MMM d" /></td>
			              </s:if>
			              <s:else>
			                  <td><s:date name="startDate" format="MMM d" /></td>
			              </s:else>
	                      <td><s:a href="%{competitorsUrl}" cssClass="c"><s:property value="name" /></s:a></td>
	                      <td><b><s:property value="getCountryUtil().getCountryByCode(country)" /></b>, <s:property value="city" /></td>
	                      <s:if test="website != null"><td><a href="<s:property value="website" />" target="_blank" class="external"><span style="white-space: nowrap;"><img src="<c:url value='/img/external_link.png'/>" border="0" alt="" /><s:property value="venue" /></span></a></td></s:if>
	                      <s:else><td><s:property value="venue" /></td></s:else>
			          </tr>
			        </s:iterator>
	            </tbody>
	        </table>
        </s:if>
        <s:else>
            <p><s:text name="competitions.noactive" /></p>
        </s:else>
    </body>
</html>