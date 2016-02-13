<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

<style type="text/css">
 .tab-box {
  	margin-top: 1.5%;
 }
 .module {
  	font-weight: normal;
    color: #458FA9;
    font-size: 12px;
    vertical-align:bottom;
  }
</style>
<script type="text/javascript">
	$(document).ready(function(){
		$("#autoRefresh").prop("checked",false);
	});
	function refreshValuePage() {
		if ($("#false").prop('checked')) {
			getScheduleStatus(true);
		} else {
			getScheduleStatus(false);
		}
	}
	
	function viewPageImage(imagePathElement, thisValue){
		var imagePath = $('#'+imagePathElement).val();
		var wWidth = $(window).width();
	     var dWidth = wWidth * 1.0;
	     var wHeight = $(window).height();
	     var dHeight = wHeight * 0.23;
	     
		/*$("#divAll").dialog({
			width: dWidth,
		    minHeight: "auto",
		    maxHeight: 600,
		    show: "slide",
		    title : 'Comparison Details',
		    resizable: false,
		    position: ['left', 'top'],
		    create: function (event) { $(event.target).parent().css('position', 'fixed');}
		});*/
		
		$( ".viewPageHighlight" ).each(function() {
			  $( this ).removeClass("activeLink");
			});
		$("#"+thisValue).addClass("activeLink");
		
		$("#divAll").show();
		loadImages(imagePath,
				"showImagePopup");
				
	}
	
	function loadImages(imagePath, target) {
		enableLoader();
		var dataToSend = {
			'imagePath' : imagePath
		};

		var request = $.ajax({
			url : "getImage.ftl",
			type : "POST",
			cache : false,
			data : dataToSend,
			dataType : "json"
		});

		request.done(function(msg) {
			disableLoader();
			var Outmsg = JSON.stringify(msg);
			Outmsg = JSON.parse(Outmsg);

			$.each(Outmsg, function(key, val) {
					document.getElementById(target).src = "data:image/png;base64,"
							+ val;
			});
		});
		request.fail(function(jqXHR, textStatus) {
			disableLoader();
			document.getElementById(target).src = "<@spring.url '' />" + "/images/notavailable.jpg";
			$("#"+target).css("height","1000px");
		});
	}
	
	function showScreens(scheduleExecutionId, scheduleScriptId){
		
		var request = $.ajax({  url: 
			"getTransactionScreens.ftl",
			data: {scheduleExecutionId:scheduleExecutionId,  scheduleScriptId:scheduleScriptId },
			type: "POST", 
			cache: false,
			dataType: "text"});
		
		request.done(function( transactionSummaryReport ) {
			if ($.parseJSON(transactionSummaryReport) != ''){
				var innerHtml = '';//completionSummaryTableHeader();
				innerHtml = innerHtml + '<tbody>';
				innerHtml = innerHtml + '<tr><td class="tab-box module">';
				
				$.each($.parseJSON(transactionSummaryReport).screenList, function(idx, screenLink) {
					innerHtml = innerHtml + '<span><a href="#" id="viewScreen'+screenLink.urlNo+'" class="editLink tabLink np viewPageHighlight" title="Show Image" onClick=viewPageImage("'+ 'image'+screenLink.urlNo +'",this.id)>' + 'View Page'+screenLink.urlNo + '&nbsp;&nbsp;&nbsp;';
					innerHtml = innerHtml + '<input type="hidden" id="' + 'image' + screenLink.urlNo + '" value ="' +screenLink.imageFullPath+'"></span>';
					
				 });
				innerHtml = innerHtml + '</td></tr></tbody>';
		 		$('#view_screen_shots_table').html(innerHtml);
			}
	 	});
	 	
	 	request.fail(
	  		function( jqXHR, textStatus ){  
  		}); 
	}
	
	function showTransactionSummaryPopUp(scheduleExecutionId){
		enableLoader();
		//This code to make it 100% X 70% of window size
		 var wWidth = $(window).width();
	     var dWidth = wWidth * 1.0;
	     var wHeight = $(window).height();
	     var dHeight = wHeight * 0.7;
	     
		$("#completionSummary").dialog({
			width: "auto",
		    minHeight: "auto",
		    maxHeight: dHeight,
		    show: "slide",
			title : 'Reports summary',
			resizable : false,
			position : [ 'center', 'top' ],
			create: function (event) { $(event.target).parent().css('position', 'fixed');}
		});
		
		var completionSummaryContent = '<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="completion_summary_content">';
		completionSummaryContent = completionSummaryContent + '<div class="bd">';
		completionSummaryContent = completionSummaryContent + '<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">';
		completionSummaryContent = completionSummaryContent + '<table id="completion_summary_table" class="styleA fullwidth sfhtTable" summary=""></table></div></div>'
		completionSummaryContent = completionSummaryContent + '<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="view_screen_shots"></div>';
		completionSummaryContent = completionSummaryContent + '<div id="divAll"><img id="showImagePopup" src="" style="overflow-x: scroll;overflow-y: scroll;"/></div></div>';
		$("#completionSummary").html(completionSummaryContent);

		loadTransactionSummary(scheduleExecutionId,'Transaction');
		$("#completionSummary").show();
		window.scrollTo(0, 1000);
		disableLoader();
	}
	function showCompletionSummaryPopUp(scheduleExecutionId) {
		enableLoader();
		//This code to make it 100% X 70% of window size
		 var wWidth = $(window).width();
	     var dWidth = wWidth * 1.0;
	     var wHeight = $(window).height();
	     var dHeight = wHeight * 0.7;
	     
		$("#completionSummary").dialog({
			width: "auto",
		    minHeight: "auto",
		    maxHeight: dHeight,
		    show: "slide",
			title : 'Reports summary',
			resizable : false,
			position : [ 'center', 'top' ],
			create: function (event) { $(event.target).parent().css('position', 'fixed');}
		});
		$("#completionSummary").html('<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="completion_summary_content">');
		$("#completionSummary").html('<div class="bd">');
		$("#completionSummary").html('<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">');
		$("#completionSummary").html('<table id="completion_summary_table" class="styleA fullwidth sfhtTable" summary=""></table></div></div></div>');
		loadCompletionSummary(scheduleExecutionId,'Completion');
		$("#completionSummary").show();
		window.scrollTo(0, 1000);
		disableLoader();
	}
	
	function showExecutionSummaryPopUp(scheduleExecutionId) {
		//This code to make it 100% X 70% of window size
		 var wWidth = $(window).width();
	     var dWidth = wWidth * 1.0;
	     var wHeight = $(window).height();
	     var dHeight = wHeight * 0.7;
		$("#executionSummary").dialog({
			width: "auto",
		    minHeight: "auto",
		    maxHeight: dHeight,
		    show: "slide",
			title : 'Execution summary',
			resizable : false,
			position : [ 'center', 'top' ],
			create: function (event) { $(event.target).parent().css('position', 'fixed');}
		});
		$("#executionSummary").html('<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="execution_summary_content">');
		$("#executionSummary").html('<div class="bd">');
		$("#executionSummary").html('<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">');
		$("#executionSummary").html("<table id='execution_summary_table' class='styleA fullwidth sfhtTable'></table></div></div></div>");
		loadCompletionSummary(scheduleExecutionId,'Execution');
		$("#executionSummary").show();
		window.scrollTo(0, 1000);
	}
	
	function executionSummaryTableHeader(){
		var innerHtml = '<thead><tr><th id="rowId1" class="txtl header w100" scope="col" tabindex="0" data-sortpath="none">Execution Summary Details</th><th></th></tr></thead>';
		return innerHtml;
	}
	function completionSummaryTableHeader(){
		/*var innerHtml = '<tbody><tr><td class="row">Total Urls </td><td></td></tr>';
		innerHtml = innerHtml + '<tr><td class="row">Current Run Total Url :</td><td class="row">'+uiReportsSummary.currentRunTotalUrls+'</td></tr>';
		innerHtml = innerHtml + '<tr><td class="row">URL Not Matched with Baseline</td><td class="row">5</td></tr>';
		innerHtml = innerHtml + '<tr><td class="row">Content matched 90% and above </td><td class="row">125</td></tr>';
		innerHtml = innerHtml + '<tr><td class="row">Content matched between 60% to 89% </td><td class="row">10</td></tr>';
		innerHtml = innerHtml + '<tr><td class="row">Content matched between 20% to 59% </td><td class="row">5</td></tr>';
		innerHtml = innerHtml + '<tr><td class="row">Content matched between 0% to 20% </td><td class="row">10</td></tr>';
		innerHtml = innerHtml + '<tr><td class="row">Baseline Run Date</td><td></td></tr>';
		innerHtml = innerHtml + '<tr><td class="row">Current Run Date</td><td></td></tr>';
		*/
		var innerHtml = '<thead><tr><th id="rowId1" class="txtl header w100" scope="col" tabindex="0" data-sortpath="none">Reports Summary</th><th></th></tr></thead>';
		return innerHtml;
	}
	
	function loadTransactionSummary(scheduleExecutionId, type){
		var request = $.ajax({  url: 
			"getTransactionSummaryReport.ftl",
			data: {scheduleExecutionId:scheduleExecutionId},
			type: "POST", 
			cache: false,
			dataType: "text"});
		
		request.done(function( transactionSummaryReportList ) {
			if ($.parseJSON(transactionSummaryReportList) != ''){
				var innerHtml = '';//completionSummaryTableHeader();
				innerHtml = innerHtml + '<tbody>';
				innerHtml = innerHtml + '<tr><td scope="row" align="right">Module Name</td><td scope="row" align="right">Testcase Name</td><td scope="row" align="right">Execution Start time</td><td>Execution Status</td>';
				innerHtml = innerHtml + '<td>View Screens</td><td>Error Details</td></tr>';
				
				$.each($.parseJSON(transactionSummaryReportList), function(idx, transactionSummaryReport) {
					innerHtml = innerHtml + '<tr><td scope="row" align="center">' + transactionSummaryReport.moduleName + '</td><td scope="row" align="center">' + transactionSummaryReport.testcaseName + '</td>';
					innerHtml = innerHtml + '<td scope="row" align="center">' + transactionSummaryReport.executionStartTime + '</td><td scope="row" align="center">' + transactionSummaryReport.executionStatus + '</td>';
					innerHtml = innerHtml + '<td scope="row" align="center"><input type="button" id="viewScreensBtn" value="View Screen" class="btn" ';
					innerHtml = innerHtml + 'onClick="showScreens('+transactionSummaryReport.scheduleExecutionId+','+transactionSummaryReport.scheduleScriptId + ');">';
					innerHtml = innerHtml + '</td><td scope="row" align="center"></td></tr>';
				 });
				innerHtml = innerHtml + '</tbody>';
				
				var viewScreens = '';
				
				viewScreens = viewScreens + '<div class="bd">';
				viewScreens = viewScreens + '<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">';
				viewScreens = viewScreens + '<table id="view_screen_shots_table" class="styleA fullwidth sfhtTable" summary=""></table></div></div>';
		
				innerHtml = innerHtml;
				
	 			$('#completion_summary_table').empty();
		 		$('#completion_summary_table').html(innerHtml);
		 		
		 		$('#view_screen_shots').html(viewScreens);
			}
	 	});
	 	
	 	request.fail(
	  		function( jqXHR, textStatus ){  
  		}); 
	}
	
	function loadCompletionSummary(scheduleExecutionId,summaryType){
		
		var request = $.ajax({  url: 
			"getCompletionSummaryForScheduleStatus.ftl",
			data: {scheduleExecutionId:scheduleExecutionId  },
			type: "POST", 
			cache: false,
			dataType: "text"});
			
			if(summaryType == 'Completion'){
				request.done(function( uiReportsSummary ) {
					data = $.parseJSON(uiReportsSummary);
					var innerHtml = '';//completionSummaryTableHeader();
					innerHtml = innerHtml + '<tbody>';
					innerHtml = innerHtml + '<tr><td scope="row" align="right">Current Run Testing Start Date</td><td>' + data.currentCrawlRunStartDateTime + '</td></tr>';
					innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Run Testing Start Date</td><td>' + data.baselineCrawlRunStartDateTime + '</td></tr>';
					innerHtml = innerHtml + '<tr><td scope="row" align="right">Current Run Total URL Count</td><td>' + data.currentRunTotalUrls + '</td></tr>';
					innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Run Total URL Count</td><td>' + data.baselineTotalUrlCount + '</td></tr>';
					
					innerHtml = innerHtml + '<tr><td scope="row" align="right">Total Urls Not Found in Current Run</td><td>' + data.urlNotMatchedWithBaselineCount + '</td></tr>';
					innerHtml = innerHtml + '<tr><td scope="row" align="right">Total Urls Newly Added in Current Run</td><td>' + data.newUrlFoundInCurrentRunCount + '</td></tr>';
					
					if(data.comparisonStatusRefId){
						
						innerHtml = innerHtml + getComparisonDetailsForReportSummary(data);
					}else if(data.brokenUrlReportStatusId){
						
						innerHtml = innerHtml + getBrokenDetailsForReportSummary(data);
					}else if(data.analyticsReportStatusId){
						
						innerHtml = innerHtml + getAnalyticsDetailsForReportSummary(data);
					}
					
					innerHtml = innerHtml + '</tbody>';
		 			$('#completion_summary_table').empty();
			 		$('#completion_summary_table').html(innerHtml);
				}); 	
			}else if(summaryType == 'Execution'){
				request.done(function( uiReportsSummary ) {
					data = $.parseJSON(uiReportsSummary);
					var innerHtml = '';//executionSummaryTableHeader();
					innerHtml = innerHtml + '<tbody>';
					if(data.comparisonStatusRefId){
						innerHtml = innerHtml + getRunningDetailsForExecutionSummary(data);
						if(data.textComparison){
							innerHtml = innerHtml + getTextDetailsForExecutionSummary(data);	
						}
						if(data.htmlComparison){
							innerHtml = innerHtml + getHTMLDetailsForExecutionSummary(data);	
						}
						if(data.imageComparison){
							innerHtml = innerHtml + getImageDetailsForExecutionSummary(data);	
						}
					}else if(data.brokenUrlReportStatusId || data.analyticsReportStatusId){
						
						innerHtml = innerHtml + getRunningDetailsForExecutionSummary(data);
					}

					innerHtml = innerHtml + '</tbody>';
		 			$('#execution_summary_table').empty();
			 		$('#execution_summary_table').html(innerHtml);
		 			
				}); 
			}
			
		 	request.fail(
		  		function( jqXHR, textStatus ){  
		  		}); 
	}
	
	function getComparisonDetailsForReportSummary(data){
		var innerHtml = '<tr><td scope="row" align="right">Total Passed</td><td>' + data.matchedCount + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Total Failed</td><td>' + data.failedCount + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Current Text Comparison Start Date</td><td>' + data.currentTextCompareRunStartDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Text Comparison Start Date</td><td>' + data.baselineTextCompareRunStartDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Current Text Comparison End Date</td><td>' + data.currentTextCompareRunEndDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Text Comparison End Date</td><td>' + data.baselineTextCompareRunEndDateTime + '</td></tr>';
		
		if(data.imageComparison){
			innerHtml = innerHtml + '<tr><td scope="row" align="right">Current Image Comparison Start Date</td><td>' + data.currentImageCompareRunStartDateTime + '</td></tr>';
			innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Image Comparison Start Date</td><td>' + data.baselineImageCompareRunStartDateTime + '</td></tr>';
			innerHtml = innerHtml + '<tr><td scope="row" align="right">Current Image Comparison End Date</td><td>' + data.currentImageCompareRunEndDateTime + '</td></tr>';
			innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Image Comparison End Date</td><td>' + data.baselineImageCompareRunEndDateTime + '</td></tr>';	
		}
		
		if(data.htmlComparison){
			innerHtml = innerHtml + '<tr><td scope="row" align="right">Current HTML Comparison Start Date</td><td>' + data.currentHtmlCompareRunStartDateTime + '</td></tr>';
			innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline HTML Comparison Start Date</td><td>' + data.baselineHtmlCompareRunStartDateTime + '</td></tr>';
			innerHtml = innerHtml + '<tr><td scope="row" align="right">Current HTML Comparison End Date</td><td>' + data.currentHtmlCompareRunEndDateTime + '</td></tr>';
			innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline HTML Comparison End Date</td><td>' + data.baselineHtmlCompareRunEndDateTime + '</td></tr>';	
		}
		
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Total Text Comparison Matched(100%)</td><td>' + data.textComparisonPassedCount + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Total Text Comparison 61% - 99% Matched</td><td>' + data.sixtyToNintyNinePercentageMatchCount + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Total Text Comparison 21% - 60% Matched</td><td>' + data.twentyToSixtyPercentageMatchCount + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Total Text Comparison 0% - 20% Matched</td><td>' + data.zeroTotwentyPercentageMatchCount + '</td></tr>';
		
		return innerHtml;
	}
	
	function getBrokenDetailsForReportSummary(data){
		var innerHtml = '<tr><td scope="row" align="right">Current Broken Report Start Date</td><td>' + data.currentBrokenUrlReportStartDateTime + '</td></tr>';
		//innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Broken Report Start Date</td><td>' + data.baselineBrokenUrlReportStartDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Current Broken Report End Date</td><td>' + data.currentBrokenUrlReportEndDateTime + '</td></tr>';
		//innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Broken Report End Date</td><td>' + data.baselineBrokenUrlReportEndDateTime + '</td></tr>';
		return innerHtml;
	}
	
	function getAnalyticsDetailsForReportSummary(data){
		var innerHtml = '<tr><td scope="row" align="right">Current Analytics Report Start Date</td><td>' + data.currentAnalyticsReportStartDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Analytics Report Start Date</td><td>' + data.baselineAnalyticsReportStartDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Current Analytics Report End Date</td><td>' + data.currentAnalyticsReportEndDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Analytics Report End Date</td><td>' + data.baselineAnalyticsReportEndDateTime + '</td></tr>';
		return innerHtml;
	}
	
	function getRunningDetailsForExecutionSummary(data){
		var innerHtml = '<tr><td scope="row" align="right">Current run testing Start time</td><td>' + data.currentCrawlRunStartDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Current run testing End time</td><td>' + data.currentCrawlRunEndDateTime + '</td></tr>';
		if((data.analyticsReportStatusId) && (data.baselineCrawlRunStartDateTime!='Not Applicable' || data.baselineCrawlRunEndDateTime !='Not Applicable')){
			innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Run Testing Start Date</td><td>' + data.baselineCrawlRunStartDateTime + '</td></tr>';
			innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Run Testing End Date</td><td>' + data.baselineCrawlRunEndDateTime + '</td></tr>';
		}
				
		return innerHtml;
	}
	
	function getTextDetailsForExecutionSummary(data){
		var innerHtml = '<tr><td scope="row" align="right">Current run Text Compare Start Time</td><td>' + data.currentTextCompareRunStartDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Current run Text Compare End time</td><td>' + data.currentTextCompareRunEndDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Text Compare Start Time</td><td>' + data.baselineTextCompareRunStartDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Text Compare End time</td><td>' + data.baselineTextCompareRunEndDateTime + '</td></tr>';
		return innerHtml;
	}	
	function getHTMLDetailsForExecutionSummary(data){
		var innerHtml = '<tr><td scope="row" align="right">Current run Html Compare Start Time</td><td>' + data.currentHtmlCompareRunStartDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Current run Html Compare End Time</td><td>' + data.currentHtmlCompareRunEndDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Html Compare Start Time</td><td>' + data.baselineHtmlCompareRunStartDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Html Compare End Time</td><td>' + data.baselineHtmlCompareRunEndDateTime + '</td></tr>';
		
		return innerHtml;
	}	
	function getImageDetailsForExecutionSummary(data){
		var innerHtml = '<tr><td scope="row" align="right">Current run Image Compare Start Time</td><td>' + data.currentImageCompareRunStartDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Current run Image Compare End Time</td><td>' + data.currentImageCompareRunEndDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Image Compare Start Time</td><td>' + data.baselineImageCompareRunStartDateTime + '</td></tr>';
		innerHtml = innerHtml + '<tr><td scope="row" align="right">Baseline Image Compare End Time</td><td>' + data.baselineImageCompareRunEndDateTime + '</td></tr>';
		return innerHtml;
	}
</script>
</head>
<body>
	<section class="drop mtlg">
		<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h3>Choose Status Type</h3>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding: 5">
							<table class="bd rowheight35">
								<tr class="lblFieldPair">
									<td class="input">
										<input type="checkbox" id="allStatus" class="scheduleTime" 
											value="completedSchedule" onClick="getScheduleStatus(true)">All&nbsp;&nbsp;&nbsp;&nbsp;
											
										<input type="checkbox" id="completedSchedule" class="scheduleTime" checked="true" 
											value="completedSchedule" onClick="getScheduleStatus(false)">Completed&nbsp;&nbsp;&nbsp;&nbsp;
									
										<input type="checkbox" id="currentSchedule" class="scheduleTime"
											value="currentSchedule" onClick="getScheduleStatus(false)">In-Progress&nbsp;&nbsp;&nbsp;&nbsp;
											
										<input type="checkbox" id="futureSchedule" class="scheduleTime"
											value="futureSchedule" onClick="getScheduleStatus(false)">Scheduled In Future&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											Display Max No. Of Schedules 
										<input type="text" min="1" size="2" style="width: 60px;" id="refreshtext" value="${regressionTestExecutionForm.refreshtext!}" onkeypress="return isBetween(event);" name="refreshtext" >
										<input type="button" id="refreshBtn" value="Refresh" class="btn" onClick="refreshValuePage();">
										<div style="float:right">Auto Refresh&nbsp;&nbsp;<input type="checkbox" id="autoRefresh" value="autoRefresh" checked="false"></div>
									</td>
								</tr>
								<tr>
								<td colspan="3">
								<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="completed_table_content">
									<div class="bd statusTables" id="completed_div">
	  								<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
										<span id="text1" class="statusTables" style="font-weight: bolder;margin: 10px">Completed</span>
										<table id="completed_table" class="styleA fullwidth sfhtTable" style="margin-top: 10px;margin-bottom: 10px;" summary="">
											<caption></caption>
											<thead>
												<tr>
												<th id="scheduleIdRow" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Schedule Id</th>
												<th id="startDateTimeRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Crawl Start Time</th>
												<th id="completionDateRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Crawl End Time</th>
												<th id="completionDateRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Execution Status</th>
												<th id="processedUrlRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Total Url Count</th>
												<th id="reportsStartTimeRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Reports Start Time</th>
												<th id="reportsEndTimeRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Reports End Time</th>
												<th id="comparisonStartTimeRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Comparison Start Time</th>
												<th id="recurrenceRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Comparison End Time</th>
												<th id="viewReportsRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">View Reports</th>
												<th id="viewAnalyticsReportsRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">View Analytics Report</th>
												<th id="scheduledByRow" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Scheduled By</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
									</div>
									</div>
								</div>
								<div class="bd statusTables" id="inprogress_div">
  								<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
								<span id="text2" class="statusTables" style="font-weight: bolder;margin: 10px">In-Progress</span>
								<table id="inprogress_table" class="styleA fullwidth sfhtTable" style="margin-top: 10px;margin-bottom: 10px;" summary=""><br/>
									<caption></caption>
									<thead>
										<tr>
											<th id="scheduleIdRow" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Schedule Id</th>
											<th id="startDateTimeRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Start date Time</th>
											<th id="lastUpdatedtimeRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Last Updated time</th>
											<th id="processedUrlRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Processed Url Count</th>
											<th id="pendingUrlCountRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Pending Url Count</th>
											<th id="reportsStartTimeRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Reports Start Time</th>
											<th id="reportsEndTimeRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Reports End Time</th>
											<th id="comparisonStartTimeRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Comparison Start Time</th>
											<th id="recurrenceRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Comparison Processed Url Count</th>
											<th id="deleteRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Comparison Pending Url Count</th>
											<th id="crawlStatusRow" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">view Reports</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
								</div>
								
								</div>
								<div class="bd statusTables" id="scheduled_div">
  								<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
								<span id="text3" class="statusTables" style="font-weight: bolder;margin: 10px">Scheduled In Future</span>
								<table id="scheduled_table" class="styleA fullwidth sfhtTable" style="margin-top: 10px;margin-bottom: 10px;" summary=""><br/>
									<caption></caption>
									<thead>
										<tr>
											<th id="startDateTimeRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Start date Time</th>
											<th id="recurrenceRow" class="txtl header w15" scope="col" tabindex="0" data-sortpath="none">Recurrence</th>
											<th id="deleteRow" class="txtl header w15" scope="col" tabindex="0" data-sortpath="none">Delete</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
								</td>
							</tr>
							</table>
							</div>
							
							</div>
						</td>
					</tr
				</table>				
		</div>
	</section>
	<center>
	<div id="completionSummary" title="Reports summary">
	</div>
	<div id="executionSummary" title="Execution summary">
	</div>
	</center>
</body>
</html>
