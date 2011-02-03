<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title><s:text name="results.title" /></title>
		
		<link rel="stylesheet" type="text/css" charset="UTF-8" href="<c:url value='/js/yui_2.8.0r4/build/datatable/assets/skins/sam/datatable.css'/>" />
		<script type="text/javascript" charset="UTF-8" src="<c:url value='/js/yui_2.8.0r4/build/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
		<script type="text/javascript" charset="UTF-8" src="<c:url value='/js/yui_2.8.0r4/build/connection/connection-min.js'/>"></script>
		<script type="text/javascript" charset="UTF-8" src="<c:url value='/js/yui_2.8.0r4/build/json/json-min.js'/>"></script>
		<script type="text/javascript" charset="UTF-8" src="<c:url value='/js/yui_2.8.0r4/build/element/element-min.js'/>"></script>
		<script type="text/javascript" charset="UTF-8" src="<c:url value='/js/yui_2.8.0r4/build/datasource/datasource-min.js'/>"></script>
		<script type="text/javascript" charset="UTF-8" src="<c:url value='/js/yui_2.8.0r4/build/datatable/datatable-min.js'/>"></script>
		
		<script type="text/javascript" charset="UTF-8" src="<c:url value='/js/results.js'/>"></script>
    </head>
    <body>
        <s:include value="/WEB-INF/decorators/menu.jsp">
            <s:param name="selectedMenu">Results</s:param>
            <s:param name="competitionId" value="competitionId" />
        </s:include>
    
        <s:if test="event.results.size > 0">
    
	        <h1><s:property value="competition.name" /></h1>
	        
	        <h2><s:property value="event.name" /></h2>
	        
	        <s:form action="results" method="get" id="eventsForm">
	            <s:hidden name="competitionId" value="%{competitionId}" />
				<table id="choices" cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
					       <td valign="bottom"><div class="space">
					           <s:select 
					               onchange="document.eventsForm.submit();"
					               name="eventId"
					               list="competition.events" 
					               listValue="formattedName" 
					               listKey="id"
					               label="%{getText('results.event')}"
					               labelposition="top"
					               cssClass="drop">
					           </s:select>
						   </div></td>
						   <td>&nbsp;</td>
						   <td valign="bottom"><div class="space"><div class="buttborder"><s:submit value="%{getText('results.submit')}" cssClass="butt"/></div></div></td>
						</tr>
					</tbody>
				</table>
	        </s:form>
	        <br />
	        
	        <center class="yui-skin-sam">
	           <div id="resultsDiv"></div>
	           <div id="resultsInfo"><img src="<c:url value='/img/loader.gif'/>" border="0" alt="" />&nbsp;<s:text name="results.reloading" /></div>
	        </center>
        
	        <s:url id="jsonUrl" action="json" escapeAmp="false">
	            <s:param name="competitionId" value="competitionId" />
	            <s:param name="eventId" value="eventId" />
	        </s:url>
			<script type="text/javascript">
			YAHOO.util.Event.addListener(window, "load", function() {
		        var myDataSource = "<s:property value='%{jsonUrl}' escape='false'/>";
		        var sortBest = function(a, b, desc) {
                    var aV = a.getData("best");
                    var bV = b.getData("best");
                    var compState = 0;
                    if (!(aV > 0)) {
                        compState = (desc)? -1 : 1;
                    } else if (!(bV > 0)) {
                        compState = (desc)? 1 : -1;
                    } else if (aV > 0 && bV > 0) {
                        compState = YAHOO.util.Sort.compare(aV, bV, desc);
                    }
                    return compState;
                };
		        var sortAverage = function(a, b, desc) {
			        var aV = a.getData("average");
			        var bV = b.getData("average");
			        var compState = 0;
			        if (!(aV > 0)) {
			        	compState = (desc)? -1 : 1;
			        } else if (!(bV > 0)) {
			        	compState = (desc)? 1 : -1;
			        } else if (aV > 0 && bV > 0) {
				        compState = YAHOO.util.Sort.compare(aV, bV, desc);
				        if (compState == 0) {
				            compState = sortBest(a, b, desc);
				        } 
			        }
			        return compState;
		        };
		        var formatName = function(elLiner, oRecord, oColumn, oData) {
		        	elLiner.innerHTML = (oRecord.getData("wcaId") != null) ? '<a target="_blank" href="http://www.worldcubeassociation.org/results/p.php?i='+oRecord.getData("wcaId")+'">'+oData+'</a>' : oData;
                };
		        <c:choose>
				    <c:when test="${event.timeFormat == 'm' || event.timeFormat == 's'}">
	                    var formatResult = function(elLiner, oRecord, oColumn, oData) {
	                        if (oData == -1) {
	                            elLiner.innerHTML = 'DNF';
	                        } else if (oData == -2) {
	                            elLiner.innerHTML = 'DNS';
                            } else if (oData > 0) {
		                        var millis = oData.toString().substring(oData.toString().length-2);
		                        oData = oData.toString().substring(0, oData.toString().length-2);
		                        var minutes = Math.floor(oData / 60);
		                        var seconds = oData - (minutes * 60);
		                        if (minutes > 0) {
		                            elLiner.innerHTML = minutes + ':' + zeroPad(seconds,2) + '.' + millis;
		                        } else {
		                        	elLiner.innerHTML = seconds + '.' + millis;
		                        }
	                        }
	                    };
	                </c:when>
	                <c:when test="${event.timeFormat == 'n'}">
	                    var formatResult = function(elLiner, oRecord, oColumn, oData) {
	                        if (oData == -1) {
	                            elLiner.innerHTML = 'DNF';
	                        } else if (oData == -2) {
	                            elLiner.innerHTML = 'DNS';
                            } else if (oData > 0) {
	                            elLiner.innerHTML = oData;
	                        }
	                    };
	                </c:when>
	                <c:when test="${event.timeFormat == 'b'}">
                    var formatResult = function(elLiner, oRecord, oColumn, oData) {
                        if (oData == -1) {
                            elLiner.innerHTML = 'DNF';
                        } else if (oData == -2) {
                            elLiner.innerHTML = 'DNS';
                        } else if (oData > 0) {
	                        var failed = Number(oData.toString().substr(-2));
	                        var time = Number(oData.toString().substring(2, 7));
	                        var solved = 99 - Number(oData.toString().substring(0, 2)) + failed;
	                        var tried = solved + failed;
	                        var minutes = Math.floor(time / 60);
	                        var seconds = time - (minutes * 60);
	                        if (minutes > 0) {
	                            if (minutes == 60) {
	                                var time = '1:00:00';
	                            } else {
	                                var time = minutes + ':' + zeroPad(seconds, 2);
	                            }
	                        } else {
	                            var time = seconds;
	                        }
	                        elLiner.innerHTML = solved + '/' + tried + ' ' + time;
                        }
                    };
                </c:when>
				</c:choose>
		        <c:choose>
	                <c:when test="${event.format == 'a'}">
	                    var myColumnDefs = [
	                        {key:"rank", label:"#", sortable:true},
	                        {key:"firstname", label:"<s:text name='results.firstname' />", formatter:formatName, sortable:true},
	                        {key:"surname", label:"<s:text name='results.surname' />", formatter:formatName, sortable:true},
	                        {key:"best", label:"<s:text name='results.best' />", formatter:formatResult, sortable:true, sortOptions:{sortFunction:sortBest}},
	                        {key:"regionalSingleRecord", label:""},
	                        {key:"average", label:"<s:text name='results.average' />", formatter:formatResult, sortable:true, sortOptions:{sortFunction:sortAverage}},
	                        {key:"regionalAverageRecord", label:""},
	                        {key:"country", label:"<s:text name='results.country' />", sortable:true},
	                        {label:"<s:text name='results.details' />", children:[
	                            {key:"result1", label:"", formatter:formatResult},
	                            {key:"result2", label:"", formatter:formatResult},
	                            {key:"result3", label:"", formatter:formatResult},
	                            {key:"result4", label:"", formatter:formatResult},
	                            {key:"result5", label:"", formatter:formatResult}
	                        ]}
	                    ];
	                </c:when>
	                <c:when test="${event.format == 'm'}">
		                var myColumnDefs = [
		                    {key:"rank", label:"#", sortable:true},
		                    {key:"firstname", label:"<s:text name='results.firstname' />", sortable:true},
		                    {key:"surname", label:"<s:text name='results.surname' />", sortable:true},
		                    {key:"best", label:"<s:text name='results.best' />", formatter:formatResult, sortable:true, sortOptions:{sortFunction:sortBest}},
		                    {key:"regionalSingleRecord", label:""},
                            {key:"average", label:"<s:text name='results.mean' />", formatter:formatResult, sortable:true, sortOptions:{sortFunction:sortAverage}},
		                    {key:"regionalAverageRecord", label:""},
		                    {key:"country", label:"<s:text name='results.country' />", sortable:true},
		                    {label:"<s:text name='results.details' />", children:[
		                        {key:"result1", label:"", formatter:formatResult},
		                        {key:"result2", label:"", formatter:formatResult},
		                        {key:"result3", label:"", formatter:formatResult}
		                    ]}
		                ];
	                </c:when>
	                <c:when test="${event.format == '1'}">
	                    var myColumnDefs = [
	                        {key:"rank", label:"#", sortable:true},
	                        {key:"firstname", label:"<s:text name='results.firstname' />", sortable:true},
	                        {key:"surname", label:"<s:text name='results.surname' />", sortable:true},
	                        {key:"best", label:"<s:text name='results.best' />", formatter:formatResult, sortable:true, sortOptions:{sortFunction:sortBest}},
	                        {key:"regionalSingleRecord", label:""},
	                        {key:"country", label:"<s:text name='results.country' />", sortable:true}
	                    ];
                    </c:when>
	                <c:when test="${event.format == '2'}">
		                var myColumnDefs = [
	                        {key:"rank", label:"#", sortable:true},
	                        {key:"firstname", label:"<s:text name='results.firstname' />", sortable:true},
	                        {key:"surname", label:"<s:text name='results.surname' />", sortable:true},
	                        {key:"best", label:"<s:text name='results.best' />", formatter:formatResult, sortOptions:{sortFunction:sortBest}},
	                        {key:"regionalSingleRecord", label:""},
	                        {key:"country", label:"<s:text name='results.country' />", sortable:true},
	                        {label:"<s:text name='results.details' />", children:[
	                            {key:"result1", label:"", formatter:formatResult},
	                            {key:"result2", label:"", formatter:formatResult}
	                        ]}
	                    ];
	                </c:when>
	                <c:when test="${event.format == '3'}">
		                var myColumnDefs = [
	                        {key:"rank", label:"#", sortable:true},
	                        {key:"firstname", label:"<s:text name='results.firstname' />", sortable:true},
	                        {key:"surname", label:"<s:text name='results.surname' />", sortable:true},
	                        {key:"best", label:"<s:text name='results.best' />", formatter:formatResult, sortable:true, sortOptions:{sortFunction:sortBest}},
	                        {key:"regionalSingleRecord", label:""},
	                        {key:"country", label:"<s:text name='results.country' />", sortable:true},
	                        {label:"<s:text name='results.details' />", children:[
	                            {key:"result1", label:"", formatter:formatResult},
	                            {key:"result2", label:"", formatter:formatResult},
	                            {key:"result3", label:"", formatter:formatResult}
	                        ]}
	                    ];
	                </c:when>
	            </c:choose>
		        return initDataTable(myDataSource, myColumnDefs);
			});
			</script>
		</s:if>
		<s:else>
		  <p><s:text name="results.noactive" /></p>
		</s:else>
    </body>
</html>