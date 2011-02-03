<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="pageMenuFrame">
  <div id="pageMenu">
    <table summary="This table gives other relevant links" cellpadding="0" cellspacing="0"><tbody><tr>
        <s:url id="indexUrl" action="index" namespace="/"/>
        <c:choose>
            <c:when test="${param.selectedMenu == 'Home'}"><td><div class="item"><s:a href="%{indexUrl}" id="activePage"><s:text name="menu.home" /></s:a></div></td></c:when>
            <c:otherwise><td><div class="item"><s:a href="%{indexUrl}"><s:text name="menu.home" /></s:a></div></td></c:otherwise>
        </c:choose>
        <c:choose><c:when test="${param.competitionId != null}">
            <s:url id="competitorsUrl" action="competitors" namespace="/">
                <s:param name="competitionId" value="competitionId" />
            </s:url>
	        <c:choose>
	            <c:when test="${param.selectedMenu == 'Competitors'}"><td><div class="item"><s:a href="%{competitorsUrl}" id="activePage"><s:text name="menu.competitors" /></s:a></div></td></c:when>
	            <c:otherwise><td><div class="item"><s:a href="%{competitorsUrl}"><s:text name="menu.competitors" /></s:a></div></td></c:otherwise>
	        </c:choose>
	        <s:if test="competition.competitors.size > 0 && competition.events.size > 0">
		        <s:url id="resultsUrl" action="results" namespace="/">
	                <s:param name="competitionId" value="competitionId" />
	            </s:url>
		        <c:choose>
		            <c:when test="${param.selectedMenu == 'Results'}"><td><div class="item"><s:a href="%{resultsUrl}" id="activePage"><s:text name="menu.results" /></s:a></div></td></c:when>
		            <c:otherwise><td><div class="item"><s:a href="%{resultsUrl}"><s:text name="menu.results" /></s:a></div></td></c:otherwise>
		        </c:choose>
		        <s:if test="records.size > 0">
			        <s:url id="recordsUrl" action="records" namespace="/">
		                <s:param name="competitionId" value="competitionId" />
		            </s:url>
			        <c:choose>
			            <c:when test="${param.selectedMenu == 'Records'}"><td><div class="item"><s:a href="%{recordsUrl}" id="activePage"><s:text name="menu.records" /></s:a></div></td></c:when>
			            <c:otherwise><td><div class="item"><s:a href="%{recordsUrl}"><s:text name="menu.records" /></s:a></div></td></c:otherwise>
			        </c:choose>
		        </s:if>
	        </s:if>
        </c:when></c:choose>
        <s:url id="faqUrl" action="faq" namespace="/"/>
        <c:choose>
            <c:when test="${param.selectedMenu == 'FAQ'}"><td><div class="item"><s:a href="%{faqUrl}" id="activePage"><s:text name="menu.faq" /></s:a></div></td></c:when>
            <c:otherwise><td><div class="item"><s:a href="%{faqUrl}"><s:text name="menu.faq" /></s:a></div></td></c:otherwise>
        </c:choose>
    </tr></tbody></table>
  </div>
</div>
<div id="header"><s:text name="menu.header1" /><br /><s:text name="menu.header2" /></div>