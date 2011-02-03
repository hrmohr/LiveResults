<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title><s:text name="competitions.title" /></title>
    </head>
    <body>
        <s:include value="/WEB-INF/decorators/adminMenu.jsp">
            <s:param name="selectedMenu">Scoresheet</s:param>
        </s:include>

        <h1><s:text name="admin.scoresheet.title" /> - <s:property value="competition.name" /></h1>
        
        <s:if test="competition.events.size > 0">
	        <s:form method="POST" action="generateScoresheet">
	           <s:token />
	           
	           <table cellpadding="0" cellspacing="0" id="choices"><tr><td>
                   <div class="space"><div class="buttborder"><s:submit value="%{getText('admin.scoresheet.submit')}" cssClass="butt"/></div></div>
               </td></tr></table>
	        
	           <table width="100%" border="0" cellpadding="0" cellspacing="0" class="results">
	                <thead>
	                    <tr>
	                        <th><s:text name="admin.scoresheet.event" /></th>
	                        <th><s:text name="admin.scoresheet.format" /></th>
	                        <th><s:text name="admin.scoresheet.timeformat" /></th>
	                        <th class="f" colspan="4"><s:text name="admin.scoresheet.rounds" /></th>
	                    </tr>
	                </thead>
	                <tbody>
			            <s:iterator value="competition.events" status="rowstatus">
                            <tr class="<s:if test="#rowstatus.even == true">e</s:if>">
			                    <td><b><s:property value="name" /></b></td>
                                <td><s:select name="formats[%{#rowstatus.index}]" list="formatTypesMap" value="format" /></td>
                                <td><s:select name="timeFormats[%{#rowstatus.index}]" list="timeFormatTypesMap" value="timeFormat" /></td>
							    <td><s:select name="round1[%{#rowstatus.index}]" headerKey="-1" headerValue="-" list="roundTypesMap" /></td>
							    <td><s:select name="round2[%{#rowstatus.index}]" headerKey="-1" headerValue="-" list="roundTypesMap" /></td>
							    <td><s:select name="round3[%{#rowstatus.index}]" headerKey="-1" headerValue="-" list="roundTypesMap" /></td>
							    <td><s:select name="round4[%{#rowstatus.index}]" headerKey="-1" headerValue="-" list="roundTypesMap" /></td>
							</tr>
			            </s:iterator>
	               </tbody>
	            </table>
	        </s:form>
        </s:if>
        <s:else>
            <p><s:text name="admin.upload.cvsfile.parsererror" /></p>
        </s:else>
    </body>
</html>