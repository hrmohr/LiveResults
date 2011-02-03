<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title><s:text name="competitions.title" /></title>
    </head>
    <body>
        <s:include value="/WEB-INF/decorators/adminMenu.jsp">
            <s:param name="selectedMenu">Scrambles</s:param>
        </s:include>

        <h1><s:text name="admin.scrambles.title" /> - <s:property value="competition.name" /></h1>
        
        <s:if test="competition.events.size > 0">
	        <s:form method="POST" action="generateScrambles">
	           <s:token />
	           
	           <table id="choices" cellpadding="0" cellspacing="0">
				    <tbody>
				        <tr>
                           <td><table><tr><td><div class="space"><div class="buttborder"><s:submit value="%{getText('admin.scrambles.generate.submit')}" cssClass="butt"/></div></div></td></tr></table></td>
                           <td>&nbsp;</td>
                           <td><s:checkbox id="includeExtraScrambles" name="includeExtraScrambles" label="%{getText('admin.scrambles.extrascrambles')}" labelposition="right" /></td>
                       </tr>
				    </tbody>
				</table>
	        
	           <table width="100%" border="0" cellpadding="0" cellspacing="0" class="results">
	                <thead>
	                    <tr>
	                        <th><s:text name="admin.scoresheet.event" /></th>
	                        <th><s:text name="admin.scoresheet.format" /></th>
	                        <th><s:text name="admin.scoresheet.round1" /></th>
	                        <th><s:text name="admin.scoresheet.group.size" /></th>
	                        <th><s:text name="admin.scoresheet.round2" /></th>
	                        <th><s:text name="admin.scoresheet.group.size" /></th>
	                        <th><s:text name="admin.scoresheet.round3" /></th>
	                        <th><s:text name="admin.scoresheet.group.size" /></th>
	                        <th><s:text name="admin.scoresheet.round4" /></th>
	                        <th class="f"><s:text name="admin.scoresheet.group.size" /></th>
	                    </tr>
	                </thead>
	                <tbody>
			            <s:iterator value="competition.events" status="rowstatus">
                            <tr class="<s:if test="#rowstatus.even == true">e</s:if>">
			                    <td><b><s:property value="name" /></b></td>
                                <td><s:select name="formats[%{#rowstatus.index}]" value="format" list="formatTypesMap" /></td>
							    <td><s:select name="round1[%{#rowstatus.index}]" headerKey="-1" headerValue="-" list="roundTypesMap" /></td>
							    <td><s:select name="group1[%{#rowstatus.index}]" headerKey="0" headerValue="-" list="numberOfGroupsMap" /></td>
							    <td><s:select name="round2[%{#rowstatus.index}]" headerKey="-1" headerValue="-" list="roundTypesMap" /></td>
							    <td><s:select name="group2[%{#rowstatus.index}]" headerKey="0" headerValue="-" list="numberOfGroupsMap" /></td>
							    <td><s:select name="round3[%{#rowstatus.index}]" headerKey="-1" headerValue="-" list="roundTypesMap" /></td>
							    <td><s:select name="group3[%{#rowstatus.index}]" headerKey="0" headerValue="-" list="numberOfGroupsMap" /></td>
							    <td><s:select name="round4[%{#rowstatus.index}]" headerKey="-1" headerValue="-" list="roundTypesMap" /></td>
							    <td><s:select name="group4[%{#rowstatus.index}]" headerKey="0" headerValue="-" list="numberOfGroupsMap" /></td>
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