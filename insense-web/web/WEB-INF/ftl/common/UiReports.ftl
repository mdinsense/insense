<style type="text/css">
.tab-box {
	margin-top: 1.5%;
}
/*  .ui-widget-header  {
  background-image: -webkit-linear-gradient(#97CAE0, #E6EAED);
 } */
</style>

<script type="text/javascript">
$(document).ready(function() {
	$('#Comparison_report_table').empty();
	
	<#if regressionTestExecutionForm.scheduleExecutionId?exists>
		$("#comparisonDates").val('${regressionTestExecutionForm.scheduleExecutionId!}');
		loadComparisonReportsDetails();
	</#if>
});

function loadComparisonReportsDetails(){
	var scheduleId = $("#comparisonDates").val();
	getComparisonReportsDetails(scheduleId);	
}

function showDownloadReportPopUp(scheduleExecutionId){
	//This code to make it 100% X 70% of window size
	 var wWidth = $(window).width();
     var dWidth = wWidth * 1.0;
     var wHeight = $(window).height();
     var dHeight = wHeight * 0.7;
     
	$("#DownloadReportSummary").dialog({
		width: "auto",
	    minHeight: "auto",
	    maxHeight: dHeight,
	    show: "slide",
		title : 'Download Reports',
		resizable : false,
		position : [ 'center', 'top' ],
		create: function (event) { $(event.target).parent().css('position', 'fixed');}
	});
	addLoaderToDiv("DownloadReportSummary");
	loadDownloadReportSummary(scheduleExecutionId,'Transaction');
	$("#DownloadReportSummary").show();
	window.scrollTo(0, 1000);
}

function loadDownloadReportSummary(scheduleExecutionId){
	
	var analytics = 1;
	var analyticsError = 2;
	var brokenLink = 3;
	var brokenLinkResources = 4;
	var loadAttribute = 5;
	var comparsion = 6;
	var imageTextReport = 7;
	var allImageZipFile = 8;
	var imageTextBaslineReport =9;
	var request = $.ajax({  url: 
		"getReportSelectionStatus.ftl",
		data: {scheduleExecutionId:scheduleExecutionId  },
		type: "POST", 
		cache: false,
		dataType: "text"});
		
		request.done(function( uiReportsSummary ) {
			
			var downloadReportSummaryContent = '<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="brokenLink_summary_content">';
			downloadReportSummaryContent = downloadReportSummaryContent + '<div class="bd">';
			downloadReportSummaryContent = downloadReportSummaryContent + '<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">';
			downloadReportSummaryContent = downloadReportSummaryContent + '<table id="download_reports_summary_table" class="styleA fullwidth sfhtTable" summary=""></table></div></div></div>'
			$("#DownloadReportSummary").html(downloadReportSummaryContent);
			
			data = $.parseJSON(uiReportsSummary);
			var innerHtml = '';
			innerHtml = innerHtml + '<tbody>';
			if(data.analyticsReport || data.analyticsErrorReport || data.brokenLinkAvailable ||
					data.brokenLinkResourcesAvailable || data.loadTimeAttributeAvailable ||data.comparisonReport){
				innerHtml = innerHtml + '<tr><td scope="row" align="center"><input type="checkbox" id="selectAllReports" name="selectAllReports" onclick="selectAllBrokenLinkReport('+scheduleExecutionId+')"></td><td><b>Select ALL</b></td><td>';
				innerHtml = innerHtml + '<a href="#" class="editLink np disabled" title="Download All Broken Link Reports" id="downloadAllBrokenLink" onClick="">';
				innerHtml = innerHtml + '<span class="icon nmt plxs" ></span>Click to Download ALL Listed Reports Below</a></td></tr>';
			}else{
				innerHtml = innerHtml + '<tr><td></td><td scope="row" align="center"><b>Select ALL</b></td><td>N/A</td></tr>';
			}
			
			if(data.analyticsReport){
				innerHtml = innerHtml + '<tr><td scope="row" align="center"><input type="checkbox" id="analyticsReports" name="analyticsReports" onclick="" disabled></td><td>Analytics Report</td><td>';	
				innerHtml = innerHtml + '<a href="#" class="editLink np" title="Analytics Reports" id="analyticsReportsLink" onClick="downloadReportSelection(';
				innerHtml = innerHtml + scheduleExecutionId +','+analytics+')"><span class="icon nmt plxs" ></span>Click to Download</a></td></tr>';
			}else{
				innerHtml = innerHtml + '<tr><td></td><td scope="row" align="center">Analytics Report</td><td>N/A</td></tr>';
			}
			
			if(data.analyticsErrorReport){
				innerHtml = innerHtml + '<tr><td scope="row" align="center"><input type="checkbox" id="analyticsErrorReports" name="analyticsErrorReports" onclick="" disabled></td><td>Analytics Error Report</td><td>';	
				innerHtml = innerHtml + '<a href="#" class="editLink np" title="Analytics Error Reports" id="analyticsErrorReportsLink" onClick="downloadReportSelection(';
				innerHtml = innerHtml + scheduleExecutionId +','+analyticsError+')"><span class="icon nmt plxs" ></span>Click to Download</a></td></tr>';
			}else{
				innerHtml = innerHtml + '<tr><td></td><td scope="row" align="center">Analytics Error Report</td><td>N/A</td></tr>';
			}
			
			if(data.brokenLinkAvailable){
				innerHtml = innerHtml + '<tr><td scope="row" align="center"><input type="checkbox" id="brokenLinkReports" name="brokenLinkReports" onclick="" disabled></td><td>Broken Link</td><td>';	
				innerHtml = innerHtml + '<a href="#" class="editLink np" title="Broken Link Reports" id="brokenRepLink" onClick="downloadReportSelection(';
				innerHtml = innerHtml + scheduleExecutionId +','+brokenLink+')"><span class="icon nmt plxs" ></span>Click to Download</a></td></tr>';
			}else{
				innerHtml = innerHtml + '<tr><td></td><td scope="row" align="center">Broken Link</td><td>N/A</td></tr>';
			}
			
			if(data.brokenLinkResourcesAvailable){
				innerHtml = innerHtml + '<tr><td scope="row" align="center"><input type="checkbox" id="brokenLinkResourcesReports" name="brokenLinkResourcesReports" onclick="" disabled></td><td>Broken Link With Resources</td><td>';	
				innerHtml = innerHtml + '<a href="#" class="editLink np" title="Broken Link With Resources" id="brokenResourcesLink" onClick="downloadReportSelection(';
				innerHtml = innerHtml + scheduleExecutionId +','+brokenLinkResources+')"><span class="icon nmt plxs" ></span>Click to Download</a></td></tr>';
			}else{
				innerHtml = innerHtml + '<tr><td></td><td scope="row" align="center">Broken Link With Resources</td><td>N/A</td></tr>';
			}
			
			if(data.loadTimeAttributeAvailable){
				innerHtml = innerHtml + '<tr><td scope="row" align="center"><input type="checkbox" id="loadAttributesReports" name="loadAttributesReports" onclick="" disabled></td><td>Load Time Attributes</td><td>';	
				innerHtml = innerHtml + '<a href="#" class="editLink np" title="Load Time Attributes" id="loadAttributesLink" onClick="downloadReportSelection(';
				innerHtml = innerHtml + scheduleExecutionId +','+loadAttribute+')"><span class="icon nmt plxs" ></span>Click to Download</a></td></tr>';
			}else{
				innerHtml = innerHtml + '<tr><td></td><td scope="row" align="center">Load Time Attributes</td><td>N/A</td></tr>';
			}
			
			if(data.comparisonReport){
				innerHtml = innerHtml + '<tr><td scope="row" align="center"><input type="checkbox" id="comparisonReport" name="comparisonReport" onclick="" disabled></td><td>Comparison Report</td><td>';	
				innerHtml = innerHtml + '<a href="#" class="editLink np" title="Comparison Report" id="comparisonReportLink" onClick="downloadReportSelection(';
				innerHtml = innerHtml + scheduleExecutionId +','+comparsion+')"><span class="icon nmt plxs" ></span>Click to Download</a></td></tr>';
			}else{
				innerHtml = innerHtml + '<tr><td></td><td scope="row" align="center">Comparison Report</td><td>N/A</td></tr>';
			}
			
			if(data.imageTextReport){
				innerHtml = innerHtml + '<tr><td scope="row" align="center"><input type="checkbox" id="imageTextReport" name="imageTextReport" onclick="" disabled></td><td>ImageText Report</td><td>';	
				innerHtml = innerHtml + '<a href="#" class="editLink np" title="ImageText Report" id="imageTextReportLink" onClick="downloadReportSelection(';
				innerHtml = innerHtml + scheduleExecutionId +','+imageTextReport+')"><span class="icon nmt plxs" ></span>Click to Download</a></td></tr>';
			}else{
				innerHtml = innerHtml + '<tr><td></td><td scope="row" align="center">ImageText Report</td><td>N/A</td></tr>';
			}
			
			if(data.imageTextBaslineReport){
				innerHtml = innerHtml + '<tr><td scope="row" align="center"><input type="checkbox" id="imageTextReportWithBaseline" name="imageTextReportWithBaseline" onclick="" disabled></td><td>ImageText With Baseline Report</td><td>';	
				innerHtml = innerHtml + '<a href="#" class="editLink np" title="ImageText With Baseline Report" id="imageTextReportWithBaselineLink" onClick="downloadReportSelection(';
				innerHtml = innerHtml + scheduleExecutionId +','+imageTextBaslineReport+')"><span class="icon nmt plxs" ></span>Click to Download</a></td></tr>';
			}else{
				innerHtml = innerHtml + '<tr><td></td><td scope="row" align="center">ImageText With Baseline Report</td><td>N/A</td></tr>';
			}
			
			if(data.allImageZipFile){
				innerHtml = innerHtml + '<tr><td scope="row" align="center"><input type="checkbox" id="allImageZipFile" name="comparisonReport" onclick="" disabled></td><td>Normalized Directory</td><td>';	
				innerHtml = innerHtml + '<a href="#" class="editLink np" title="Normalized Directory" id="allImageZipFileLink" onClick="downloadReportSelection(';
				innerHtml = innerHtml + scheduleExecutionId +','+allImageZipFile+')"><span class="icon nmt plxs" ></span>Click to Download</a></td></tr>';
			}else{
				innerHtml = innerHtml + '<tr><td></td><td scope="row" align="center">Normalized Directory</td><td>N/A</td></tr>';
			}
			
			innerHtml = innerHtml + '</tbody>';
 			$('#download_reports_summary_table').empty();
	 		$('#download_reports_summary_table').html(innerHtml);
		}); 	
		
	 	request.fail(
	  		function( jqXHR, textStatus ){  
	  			$("#DownloadReportSummary").html("<div>Failed to load reports</div");
	  		}); 
}

function selectAllBrokenLinkReport(scheduleExecutionId){
	var analytics = 1;
	var analyticsError = 2;
	var brokenLink = 3;
	var brokenLinkResources = 4;
	var loadAttribute = 5;
	var comparsion = 6;
	var imageTextReport = 7;
	var allImageZipFile = 8;
	
	if($("#selectAllReports").prop("checked")) {
		$("#brokenLinkReports").prop("checked", true);
		$("#brokenLinkResourcesReports").prop("checked", true);
		$("#loadAttributesReports").prop("checked", true);
		
		$("#downloadAllBrokenLink").removeClass("disabled");
		$("#downloadAllBrokenLink").attr('onClick','downloadAllBrokenLinkReport('+scheduleExecutionId+')');
		
		$("#brokenRepLink").addClass("disabled");
		$("#brokenRepLink").removeAttr("onClick");
		
		$("#brokenResourcesLink").addClass("disabled");
		$("#brokenResourcesLink").removeAttr("onClick");
		
		$("#loadAttributesLink").addClass("disabled");
		$("#loadAttributesLink").removeAttr("onClick");
		
		$("#analyticsReportsLink").addClass("disabled");
		$("#analyticsReportsLink").removeAttr("onClick");
		
		$("#analyticsErrorReportsLink").addClass("disabled");
		$("#analyticsErrorReportsLink").removeAttr("onClick");
		
		$("#comparisonReportLink").addClass("disabled");
		$("#comparisonReportLink").removeAttr("onClick");
		
		$("#imageTextReportLink").addClass("disabled");
		$("#imageTextReportLink").removeAttr("onClick");
		
		$("#imageTextReportWithBaselineLink").addClass("disabled");
		$("#imageTextReportWithBaselineLink").removeAttr("onClick");
		
		$("#allImageZipFileLink").addClass("disabled");
		$("#allImageZipFileLink").removeAttr("onClick");
		
	}else{
		$("#brokenLinkReports").prop("checked", false);
		$("#brokenLinkResourcesReports").prop("checked", false);
		$("#loadAttributesReports").prop("checked", false);
		
		$("#downloadAllBrokenLink").addClass("disabled");
		$("#downloadAllBrokenLink").removeAttr("onClick");
		
		$("#brokenRepLink").removeClass("disabled");
		$("#brokenRepLink").attr('onClick','downloadReportSelection('+scheduleExecutionId+','+brokenLink+')');
		
		$("#brokenResourcesLink").removeClass("disabled");
		$("#brokenResourcesLink").attr('onClick','downloadReportSelection('+scheduleExecutionId+','+brokenLinkResources+')');
		
		$("#loadAttributesLink").removeClass("disabled");
		$("#loadAttributesLink").attr('onClick','downloadReportSelection('+scheduleExecutionId+','+loadAttribute+')');
		
		$("#analyticsReportsLink").removeClass("disabled");
		$("#analyticsReportsLink").attr('onClick','downloadReportSelection('+scheduleExecutionId+','+analytics+')');
		
		$("#analyticsErrorReportsLink").removeClass("disabled");
		$("#analyticsErrorReportsLink").attr('onClick','downloadReportSelection('+scheduleExecutionId+','+analyticsError+')');
		
		$("#comparisonReportLink").removeClass("disabled");
		$("#comparisonReportLink").attr('onClick','downloadReportSelection('+scheduleExecutionId+','+comparsion+')');
		
		$("#imageTextReportLink").addClass("disabled");
		$("#imageTextReportLink").attr('onClick','downloadReportSelection('+scheduleExecutionId+','+imageTextReport+')');
		
		$("#imageTextReportWithBaselineLink").addClass("disabled");
		$("#imageTextReportWithBaselineLink").attr('onClick','downloadReportSelection('+scheduleExecutionId+','+imageTextBaslineReport+')');
		
		$("#allImageZipFileLink").addClass("disabled");
		$("#allImageZipFileLink").attr('onClick','downloadReportSelection('+scheduleExecutionId+','+allImageZipFile+')');
	}
}
function getComparisonReportsDetails(scheduleId){
	var scheduleExecutionId = scheduleId;
	
	if ( scheduleExecutionId == '') {
		return;
	}
	
	var request = $.ajax({  url: 
		"getComparisonReportsDetails.ftl",
				type: "POST", 
		data: { scheduleExecutionId:scheduleExecutionId },
		type: "POST", 
		cache: false,
		dataType: "text"});
		
		request.done(function( reportStatus ) {
		data = $.parseJSON(reportStatus);
		var innerHtml = comparisonReportsTableHeader();
		if ($.parseJSON(reportStatus) != ''){
 			innerHtml = innerHtml + '<tbody><tr data-sortfiltered="false">';
 			innerHtml = innerHtml + '<td class="row">' + data.scheduleExecutionId + '</td>';
			innerHtml = innerHtml + '<td class="row">' + data.scheduleStartDate + '</td>';
			innerHtml = innerHtml + '<td class="row">' + data.noOfurls + '</td>';
			innerHtml = innerHtml + '<td class="row">' + data.totalUrlsFailed + '</td>';
			if ( data.brokenUrlReportAvailable || data.analyticsReportAvailable || data.textOrImageReportAvailable || data.reportsAvailable){
			
				innerHtml = innerHtml + '<td class="row">';
				innerHtml = innerHtml + '<a href="#" class="editLink np" title="Broken Link Reports" onClick="showDownloadReportPopUp(';
				innerHtml = innerHtml + data.scheduleExecutionId +')"><span class="icon nmt plxs" ></span>Click to View</a>';
				innerHtml = innerHtml + '</td>';
			}else{
				innerHtml = innerHtml + '<td class="row">' + 'N/A' + '</td>';
			}

			if ( data.analyticsReportAvailable ){
				innerHtml = innerHtml + '<td class="row">';
				 innerHtml = innerHtml + '<a href="#" class="editLink np" title="View Reports" onClick="showReportLinks(';
				innerHtml = innerHtml + data.scheduleExecutionId +')"><span class="icon nmt plxs" ></span>View Analytics Report</a>'; 
				//innerHtml = innerHtml + '<a href="#" class="editLink np" title="View Reports" onClick="checkAnalyticsSummaryReport"><span class="icon nmt plxs" ></span>View Analytics Report</a>'; 
				innerHtml = innerHtml + '</td>';
			}else{
				innerHtml = innerHtml + '<td class="row">' + 'N/A' + '</td>';
			}
			innerHtml = innerHtml + '</tr></tbody>';
		}else{
			innerHtml = innerHtml + "<tbody><tr><td colspan='9'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr></tbody>";
		}
 			$('#Comparison_report_table').empty().append(innerHtml);
		}); 
	 	request.fail(
	  		function( jqXHR, textStatus ){  
	  		}); 
}

function comparisonReportsTableHeader(){
	var innerHtml = '<thead><tr><th id="col1" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Schedule Id</th>';
	innerHtml = innerHtml + '<th id="col2" class="txtl header w15" scope="col"  tabindex="0" data-sortpath="none">Run Date Time</th>';
	innerHtml = innerHtml + '<th id="col2" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Total URL Count</th>';
	innerHtml = innerHtml + '<th id="col3" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Failed URL Count</th>';
	innerHtml = innerHtml + '<th id="col6" class="txtl header w15" scope="col" tabindex="0" data-sortpath="none">Download reports</th>';
	innerHtml = innerHtml + '<th id="col10" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">View Analytics Report</th></tr></thead>';
	
	return innerHtml;
}
function showReportLinks(scheduleExecutionId){
	$("#scheduleExecutionId").val(scheduleExecutionId);
	document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/viewAnalyticsSummaryReport.ftl' />");
	document.getElementById("SuitForm").setAttribute('method',"POST");
	document.getElementById("SuitForm").submit();
}


function downloadAllBrokenLinkReport(scheduleExecutionId) {
	var form = document.createElement("form"); //created dummy form for submitting.

    var element2 = document.createElement("input");
    form.method = "POST";
    form.action = "downloadAllBrokenLinkReport.ftl";
    
    element2.value=scheduleExecutionId; 
    element2.name="scheduleExecutionId";
    element2.type = 'hidden';
    
    form.appendChild(element2);

    document.body.appendChild(form);

    form.submit();
    return false;
}

function downloadAnalyticsReport(scheduleExecutionId) {
	var form = document.createElement("form"); //created dummy form for submitting.

    var element2 = document.createElement("input");
    form.method = "POST";
    form.action = "downloadAnalyticsReport.ftl";
    
    element2.value=scheduleExecutionId; 
    element2.name="scheduleExecutionId";
    element2.type = 'hidden';
    
    form.appendChild(element2);

    document.body.appendChild(form);

    form.submit();
    return false;
}

function downloadAnalyticsFailureReport(scheduleExecutionId) {
	var form = document.createElement("form"); //created dummy form for submitting.

    var element2 = document.createElement("input");
    form.method = "POST";
    form.action = "downloadAnalyticsFailureReport.ftl";
    
    element2.value=scheduleExecutionId; 
    element2.name="scheduleExecutionId";
    element2.type = 'hidden';
    
    form.appendChild(element2);

    document.body.appendChild(form);

    form.submit();
    return false;
}

function downloadReportSelection(scheduleExecutionId, reportPatternName) {
	var form = document.createElement("form"); //created dummy form for submitting.

    var element2 = document.createElement("input");
    var element3 = document.createElement("input");
    form.method = "POST";
    form.action = "downloadReportSelection.ftl";
    
    element2.value=scheduleExecutionId; 
    element2.name="scheduleExecutionId";
    element2.type = 'hidden';
    
    element3.value=reportPatternName; 
    element3.name="reportPatternName";
    element3.type = 'hidden';

    form.appendChild(element2);
    form.appendChild(element3);

    document.body.appendChild(form);

    form.submit();
    return false;
}

function downloadComparisonReport(scheduleExecutionId) {
	var form = document.createElement("form"); //created dummy form for submitting.

    var element2 = document.createElement("input");
    form.method = "POST";
    form.action = "downloadComparisonReport.ftl";
    
    element2.value=scheduleExecutionId; 
    element2.name="scheduleExecutionId";
    element2.type = 'hidden';
    
    form.appendChild(element2);

    document.body.appendChild(form);

    form.submit();
    return false;
}

</script>
</head>
<body>
	<section class="drop mtlg">
		<div class="cm">
		<form modelAttribute="RegressionTestExecutionForm"
								id="aform" name="aform" target="_top"
								method="POST" onSubmit="">
								<input type="hidden" id ="scheduleExecutionId">
			<table cellpadding='0' cellspacing='0' width="100%;">
				<tr>
					<td class="hd">
						<h3>Comparison Reports</h3>
					</td>
				</tr>
				<tr class="bd">
					<td style="padding: 5">
						<table class="bd rowheight35">
							<tr class="lblFieldPair">
								<td style="width:100"><label for="comparisonDates">Schedule
										Dates</label></td>
								<td class="input">
									<select id="comparisonDates" name="comparisonDates" onchange="loadComparisonReportsDetails()">
									<option value="0">--Choose Date--</option>
										<#list reportBaselineScheduleExecutionList! as scheduleExecution>
											<option value="${scheduleExecution.scheduleExecutionId!}">${scheduleExecution.testExecutionStartTime!}</option>
										</#list>
									</select>
								</td>
							</tr>
						</table>
				</tr>
				<tr class="bd">
					<td class="bd">
						<table id="Comparison_report_table"
							class="styleA fullwidth sfhtTable" summary="">
							<caption></caption>
							<thead>
								<tr>
									<th class="txtl header w10" scope="col" data-sortfilter="true"
										data-sortdatatype="string" data-sort="true"
										data-sortname="ftype1" tabindex="0" data-sortpath="none">Schedule Id</th>
									<th class="txtl header w15" scope="col" data-sortfilter="true"
										data-sortdatatype="string" data-sort="true"
										data-sortname="ftype1" tabindex="0" data-sortpath="none">Run
										Date Time</th>
									<th class="txtl header w10" scope="col" data-sortfilter="true"
										data-sortdatatype="number" data-sort="true"
										data-sortname="ftype2" tabindex="0" data-sortpath="none">Total URL Couns</th>
									<th class="txtl header w10" scope="col" data-sortfilter="true"
										data-sortdatatype="number" data-sort="true"
										data-sortname="ftype3" tabindex="0" data-sortpath="none">Failed Url Count</th>
									<th class="txtl header w15" scope="col" data-sortfilter="true"
										data-sortdatatype="string" data-sort="true"
										data-sortname="ftype6" tabindex="0" data-sortpath="none">Download Broken Link Report</th>
									<th class="txtl header w15" scope="col" data-sortfilter="true"
										data-sortdatatype="string" data-sort="true"
										data-sortname="ftype8" tabindex="0" data-sortpath="none">Download Comaprison Report</th>
									<th class="txtl header w15" scope="col" data-sortfilter="true"
										data-sortdatatype="string" data-sort="true"
										data-sortname="ftype7" tabindex="0" data-sortpath="none">Download Analytics Report</th>
									<th class="txtl header w10" scope="col" data-sortfilter="true"
										data-sortdatatype="string" data-sort="true"
										data-sortname="ftype9" tabindex="0" data-sortpath="none">View Analytics
										Report</th>
								</tr>
							</thead>
						</table>
					</td>
				</tr>
			</table>
			</form>
		</div>
	</section>
	<center>
		<div id="analyticsSummary" title="Analytics Summary Report">
		</div>
		<div id="DownloadReportSummary" title="Download Report Summary">
		</div>
	</center>
</body>
</html>
