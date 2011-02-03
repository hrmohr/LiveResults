<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title><s:text name="main.title" /> - <decorator:title /></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="author" content="Mads Mohr Christensen" />
        <meta name="description" content="<s:text name='main.description' />" />
        <meta name="keywords" content="<s:text name='main.keywords' />" />
        <s:text name="main.useGoogleAnalytics" var="useGoogleAnalytics" />
        <c:if test="${useGoogleAnalytics}">
        <meta name="google-site-verification" content="QzPthKgIne7wWgTUcd0x9jNpt9Rg2G9ALShaD28k1zo" />
        </c:if>    
        <link rel="shortcut icon" href="<c:url value='/img/wca.ico'/>" />
        <decorator:head />
        <link rel="stylesheet" type="text/css" charset="UTF-8" href="<c:url value='/css/results.css'/>" />
    </head>
    <body>
        <div id="main"> 
            <div id="content">
                <decorator:body />
            </div>
            <p>
                <s:text name="main.disclaimer">
                    <s:param><c:url value="/img/external_link.png"/></s:param>
                </s:text>
            </p>
        </div>
        
        <c:if test="${useGoogleAnalytics}">
		<!-- Google analytics begin -->
        <script type="text/javascript">
        var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
        document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
        </script>
        <script type="text/javascript">
        try {
            var pageTracker = _gat._getTracker("UA-11020597-2");
            pageTracker._trackPageview();
        } catch(err) {}
        </script>
		<!-- Google analytics end -->
        </c:if>    
    </body>
</html>
