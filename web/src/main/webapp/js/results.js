function zeroPad(n, digits) {
    n = n.toString();
    while (n.length < digits) {
        n = '0' + n;
    }
    return n;
}
function initDataTable(dataSource, myColumnDefs) {
	var myDataFields = [{key:"rank", parser:"number"},"wcaId","firstname","surname","country",
	                    {key:"best", parser:"number"},"regionalSingleRecord",{key:"average", parser:"number"},"regionalAverageRecord",
	                    {key:"result1", parser:"number"},{key:"result2", parser:"number"},{key:"result3", parser:"number"},{key:"result4", parser:"number"},{key:"result5", parser:"number"}];
	var myDataSource = new YAHOO.util.DataSource(dataSource);
    myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
    myDataSource.connXhrMode = "queueRequests";
    myDataSource.responseSchema = {
        resultsList: "results",
        fields: myDataFields
    };

    var myDataTable = new YAHOO.widget.DataTable("resultsDiv", myColumnDefs, myDataSource, {sortedBy:{key:"rank", dir:YAHOO.widget.DataTable.CLASS_ASC}});
    myDataTable.subscribe("rowMouseoverEvent", myDataTable.onEventHighlightRow);
    myDataTable.subscribe("rowMouseoutEvent", myDataTable.onEventUnhighlightRow);

    var myCallback = {
		success: myDataTable.onDataReturnSetRows, 
		failure: function() { 
			YAHOO.log("Polling failure", "error"); 
		}, 
		scope: myDataTable,
		argument: myDataTable.getState()
    };
    
    myDataSource.setInterval(1000*30, null, myCallback);
    
    return {
        oDS: myDataSource,
        oDT: myDataTable
    };
}