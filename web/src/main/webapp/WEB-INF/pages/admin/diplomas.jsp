<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
    <head>
        <title><s:text name="competitions.title" /></title>
    </head>
    <body>
        <s:include value="/WEB-INF/decorators/adminMenu.jsp">
            <s:param name="selectedMenu">Diplomas</s:param>
        </s:include>
        
        <h1><s:text name="admin.diplomas.title" /></h1>

        <sec:authorize ifAllGranted="ROLE_ADMIN">
	        <s:if test="competitions.size > 0">
	           <s:form action="generateDiploma">
					<table id="choices" cellpadding="0" cellspacing="0">
					    <tbody>
					        <tr>
					           <td valign="bottom"><div class="space">
					               <s:select 
					                   name="competitionId"
					                   list="competitions" 
					                   listValue="name" 
					                   listKey="competitionId"
					                   label="%{getText('competitions.title')}"
					                   labelposition="top"
					                   cssClass="drop">
					               </s:select>
					           </div></td>
					           <td>&nbsp;</td>
					           <td valign="bottom"><div class="space">
	                                <s:select 
	                                    name="template"
	                                    list="templates" 
	                                    label="%{getText('admin.diplomas.template')}"
	                                    labelposition="top"
	                                    cssClass="drop">
	                                </s:select>
	                            </div></td>
	                            <td>&nbsp;</td>
	                            <td valign="bottom"><s:checkbox id="danishDiplomas" name="danishDiplomas" label="%{getText('admin.diplomas.danishDiplomas')}" labelposition="right" /></td>
	                        </tr>
	                        <tr><td colspan="5"><table><tr><td><div class="space"><div class="buttborder"><s:submit value="%{getText('admin.diplomas.submit')}" cssClass="butt"/></div></div></td></tr></table></td></tr>
					    </tbody>
					</table>
                </s:form>
	        </s:if>
	        <s:else>
	            <p><s:text name="competitions.noactive" /></p>
	        </s:else>
        </sec:authorize>
        
        <sec:authorize ifNotGranted="ROLE_ADMIN">
            <s:form action="generateDiploma">
                <table id="choices" cellpadding="0" cellspacing="0">
                    <tbody>
                        <tr>
                            <td><div class="space"><s:textfield id="competitionId" name="competitionId" label="%{getText('admin.competition.id')}" value="%{#session.SPRING_SECURITY_LAST_USERNAME}" readonly="true" /></div></td>
                            <td>&nbsp;</td>
                            <td valign="bottom"><div class="space">
                                <s:select 
                                    name="template"
                                    list="templates" 
                                    label="%{getText('admin.diplomas.template')}"
                                    labelposition="top"
                                    cssClass="drop">
                                </s:select>
                            </div></td>
                            <td>&nbsp;</td>
                            <td valign="bottom"><div class="space">
	                            <s:select 
	                                name="eventId"
	                                list="%{getEvents(#session.SPRING_SECURITY_LAST_USERNAME)}"
	                                headerValue="%{getText('admin.diplomas.allevents')}"
	                                headerKey="-1"
	                                listKey="id"
	                                listValue="name"
	                                label="%{getText('admin.diplomas.event')}"
	                                labelposition="top"
	                                cssClass="drop">
	                            </s:select>
	                        </div></td>
                            <td>&nbsp;</td>
                            <td valign="bottom"><s:checkbox id="danishDiplomas" name="danishDiplomas" label="%{getText('admin.diplomas.danishDiplomas')}" labelposition="right" /></td>
	                    </tr>
                        <tr><td colspan="7"><table><tr><td><div class="space"><div class="buttborder"><s:submit value="%{getText('admin.diplomas.submit')}" cssClass="butt"/></div></div></td></tr></table></td></tr>
                    </tbody>
                </table>
            </s:form>
        </sec:authorize>
    </body>
</html>