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
            <s:param name="selectedMenu">Upload results</s:param>
        </s:include>

        <h1><s:text name="admin.results.title" /></h1>
        
        <s:form method="POST" enctype="multipart/form-data" action="uploadXls">
            <s:token />
            
	        <table class="formTable" id="choices">
	            <sec:authorize ifAllGranted="ROLE_ADMIN">
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
                </tr>
                </sec:authorize>
		        <sec:authorize ifNotGranted="ROLE_ADMIN">
		        <tr>
                    <td><div class="space"><s:textfield id="competitionId" name="competitionId" label="%{getText('admin.competition.id')}" value="%{#session.SPRING_SECURITY_LAST_USERNAME}" readonly="true" required="true" /></div></td>
                </tr>
		        </sec:authorize>
	            <tr>
	                <td><div class="space"><s:file name="xls" label="%{getText('admin.upload.xlsfile')}" required="true" /></div></td>
	            </tr>
	            <tr>
	                <td align="right"><table><tr><td><div class="space"><div class="buttborder"><s:submit value="%{getText('admin.upload.submit')}" cssClass="butt" /></div></div></td></tr></table></td>
	            </tr>
	        </table>
	    </s:form>
	    
	    <s:if test="competition != null">
	       <p><s:text name="admin.results.uploaded"><s:param value="competition.competitionId" /></s:text></p>
	    </s:if>
    </body>
</html>