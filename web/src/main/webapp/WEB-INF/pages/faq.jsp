<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title><s:text name="faq.title" /></title>
    </head>
    <body>
        <s:include value="/WEB-INF/decorators/menu.jsp">
            <s:param name="selectedMenu">FAQ</s:param>
        </s:include>
        
        <h1><s:text name="faq.title" /></h1>
        
        <dl>
            <dt>What is this?</dt>
            <dd>This is a system that provides Live results from Speedcubing competitions.</dd>
            
            <dt>What is Speedcubing?</dt>
            <dd>Speedcubing is solving twistypuzzles such as the Rubik's Cube as fast as possible. It's a lot of fun.</dd>
            
            <dt>Who made this?</dt>
            <dd><a href="mailto:hr.mohr@gmail.com" class="email"><span style="white-space: nowrap;"><img src="<c:url value='/img/email_link.png'/>" border="0" alt="" />Mads</span> Mohr Christensen</a> and the visuals was stolen from the official <a href="http://www.worldcubeassociation.org/results/" target="_blank" class="external"><span style="white-space: nowrap;"><img src="<c:url value='/img/external_link.png'/>" border="0" alt="" />WCA</span> Results</a> page.</dd>
            
            <dt>I'm organizing a competition could we use your system?</dt>
            <dd>YES! That's why I made it. (apart from having fun doing it)</dd>
            
            <dt>I have a projector available for my competition. How do I get results on that?</dt>
            <dd>Easy! Currently only supported for <a href="<c:url value='/scoreboard-win.zip'/>" target="_blank" class="external"><span style="white-space: nowrap;"><img src="<c:url value='/img/external_link.png'/>" border="0" alt="" />Windows</span></a> and <a href="<c:url value='/scoreboard-osx.zip'/>" target="_blank" class="external"><span style="white-space: nowrap;"><img src="<c:url value='/img/external_link.png'/>" border="0" alt="" />OSX</span></a>. Design based on system by Rafal Studnicki.</dd>
            
            <dt>How does it work?</dt>
            <dd>
                The system consists of 3 parts.
                <ul>
                    <li>Excel parser that uploads the results.</li>
                    <li>Webservice that saves the results.</li>
                    <li>These pages that displays the results.</li> 
                </ul>
                <br />
                Organizers only need to run the Excel parser in order to post results here.
            </dd>
            
            <dt>What are the system requirements?</dt>
            <dd>The parser needs <a href="http://java.sun.com/javase/downloads/index.jsp" target="_blank" class="external"><span style="white-space: nowrap;"><img src="<c:url value='/img/external_link.png'/>" border="0" alt="" />Java</span> 6</a> and the results database requires mysql 5, but other databases should be able to run as well.<br />Current supported platforms includes Mac OSX 10.5+ and Windows XP, but other platforms could be added upon request.</dd>
            
            <dt>Argh! Something does not work!</dt>
            <dd>Please send an <a href="mailto:hr.mohr@gmail.com" class="email"><span style="white-space: nowrap;"><img src="<c:url value='/img/email_link.png'/>" border="0" alt="" />email</span></a> and let's have a look.</dd>
            
            <dt>I found a bug, but I know how to fix it!</dt>
            <dd>Cool! The source is GPL'ed so any contributions are very welcome.</dd>
        </dl>
    </body>
</html>