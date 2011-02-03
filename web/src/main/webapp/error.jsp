<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!--  
Copyright (C) 2009 Mads Mohr Christensen, <hr.mohr@gmail.com>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>An unexpected error has occurred</title>
    </head>
    <body>
    <s:include value="/WEB-INF/decorators/menu.jsp">
    	<s:param name="selectedMenu">none</s:param>
    </s:include>
	<h2>An unexpected error has occurred</h2>
	<p>Please report this error to your system administrator or
	appropriate technical support personnel. Thank you for your cooperation.
	</p>
	<hr />
	<h3>Error Message</h3>
	<s:actionerror />
	<s:if test="%{exception != null">
		<p><s:property value="%{exception.message}" /></p>
	</s:if>
	<s:if test="%{exceptionStack != null}">
		<hr />
		<h3>Technical Details</h3>
		<p><s:property value="%{exceptionStack}" /></p>
	</s:if>
</body>
</html>