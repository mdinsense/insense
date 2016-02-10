<#import "spring.ftl" as spring />
<#import "MacroTemplates.ftl" as mint />
<div id="Mint analytics summary Home">
   <#include "header.ftl">
</div>

<style type="text/css">
div.tab-box li {
	padding: 10;
}
</style> 

<head>   
    <script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	 var tabnum = "${regressionTestExecutionForm.reportTabNumber!}";
	 //$("#summaryReportValues").hide();
	 $(".tabLink").removeClass("activeLink");
	 $("#tab"+tabnum).addClass("activeLink");
});


function checkAnalyticsDetailsReport(reportTabNumber) {
	document.aform.reportTabNumber.value=reportTabNumber;
	document.getElementById("reportLink").setAttribute('action',"<@spring.url '/viewAnalyticsDetailsReport.ftl' />");
	document.getElementById("reportLink").setAttribute('method',"POST");
	document.getElementById("reportLink").submit();
}

function checkAnalyticsErrorReport(reportTabNumber) {
	document.aform.reportTabNumber.value=reportTabNumber;
	document.getElementById("reportLink").setAttribute('action',"<@spring.url '/viewAnalyticsErrorReport.ftl' />");
	document.getElementById("reportLink").setAttribute('method',"POST");
	document.getElementById("reportLink").submit();
}		

function checkAnalyticsSummaryReport(reportTabNumber) {
	document.aform.reportTabNumber.value=reportTabNumber;
	document.getElementById("reportLink").setAttribute('action',"<@spring.url '/viewAnalyticsSummaryReport.ftl' />");
	document.getElementById("reportLink").setAttribute('method',"POST");
	document.getElementById("reportLink").submit();
	
	/* var scheduleExecutionId = $("#scheduleExecutionId").val();
	 var dataToSend = {
			 'scheduleExecutionId' : scheduleExecutionId
		      }; 
	 
	var request = $.ajax({  
		url: "isAnalyticsSummaryReportExist.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		
	}); 

	request.done(function( msg ) {	
		if(msg == "FileExist"){
			$("#summaryReportValues").show();
			viewAnalyticsSummaryReport(scheduleExecutionId);
		}else{
			alert("Analytics Report : Report not found in the path");
		}
	});
	
	request.fail(function( jqXHR, textStatus ){
		
	}); */ 
}

function viewAnalyticsSummaryReport(scheduleExecutionId) {
	//$("#summaryReportValues").show();
	document.aform.scheduleExecutionId.value=scheduleExecutionId;
	document.getElementById("reportLink").setAttribute('action',"<@spring.url '/viewAnalyticsSummaryReport.ftl' />");
	document.getElementById("reportLink").setAttribute('method',"POST");
	document.getElementById("reportLink").submit();
}
	/* var request = $.ajax({  url: 
		"viewAnalyticsSummaryReport.ftl",
		data: {scheduleExecutionId:scheduleExecutionId  },
		type: "POST", 
		cache: false,
		dataType: "text"});

		request.done(function( analyticsSummaryList ) {
		
			var innerHtml = completionAnalyticsSummaryTableHeader();
			if ( $.parseJSON(analyticsSummaryList) != ''){
				innerHtml = innerHtml + '<tbody>';
				$.each($.parseJSON(analyticsSummaryList), function(idx, analytics) {
					
					innerHtml = innerHtml + '<tr data-sortfiltered="false"><td class="row" data-sortvalue="' + analytics.tagName + '">' + analytics.tagName + '</td>';
					innerHtml = innerHtml + '<td class="row" data-sortvalue=' + analytics.totalUrl + '>' + analytics.totalUrl + '</td>';
					innerHtml = innerHtml + '<td class="row" data-sortvalue=' + analytics.tagPresentUrlCount + '>' + analytics.tagPresentUrlCount + '</td>';
					innerHtml = innerHtml + '<td class="row" data-sortvalue=' + analytics.tagNotPresentUrlCount + '>' + analytics.tagNotPresentUrlCount + '</td>';
					innerHtml = innerHtml + '<td class="row" data-sortvalue=' + analytics.tagHasErrorUrlCount + '>' + analytics.tagHasErrorUrlCount + '</td></tr>';
			 	});
			 	innerHtml = innerHtml + '</tbody>';
			}else{
				
				innerHtml = innerHtml + "<tbody><tr><td colspan='9'><font face='verdana,arial' size='-1' >No Records Found.</font></td></tr></tbody>";
			}
 			$('#mint_analytics_summary_table').empty().append(innerHtml);
		});
		request.fail(
	  		function( jqXHR, textStatus ){  
  		});
} */

function loadTabData(setupTabNumberVal,currentpage) {
	$('#currentPageNo').val(currentpage);

	var form = $(document.createElement('form'));
	//$('#suitId').attr('value', '');
	$('body').append(form);
	$(form).attr("name", "UiScheduleTestcaseSetup");
	$(form).attr("modelAttribute", "RegressionTestExecutionForm");
	$(form).attr("action", "<@spring.url '/Home.ftl' />");
	$(form).attr("method", "POST");
	var input1 = $("<input>").attr("type", "hidden").attr("name", "setupTabNumber").val(setupTabNumberVal);
	var input2 = $("<input>").attr("type", "hidden").attr("name", "suitId").val('${regressionTestExecutionForm.suitId!}');
	var input3 = $("<input>").attr("type", "hidden").attr("name", "suitType").val(document.getElementById("suitType").value);
	//var input4 = $("<input>").attr("type", "hidden").attr("name", "uiSuitsCategory").val(document.getElementById("uiSuitsCategory").value);
	var input5 = $("<input>").attr("type", "hidden").attr("name", "scheduleExecutionId").val($("#scheduleExecutionId").val());
	var input6 = $("<input>").attr("type", "hidden").attr("name", "currentPageNo").val($("#currentPageNo").val());
	
	$(form).append($(input1));
	$(form).append($(input2));
	$(form).append($(input3));
	//$(form).append($(input4));
	$(form).append($(input5));
	$(form).append($(input6));
	$(form).submit();

}
function completionAnalyticsSummaryTableHeader(){
	var innerHtml = '<caption></caption><thead><tr>';
	innerHtml = innerHtml + '<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype1" tabindex="0" data-sortpath="none">Tag Name</th>';
	innerHtml = innerHtml + '<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="number" data-sort="true" data-sortname="ftype2" tabindex="0" data-sortpath="none">Total Url Count</th>';
	innerHtml = innerHtml + '<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="number" data-sort="true" data-sortname="ftype3" tabindex="0" data-sortpath="none">Tag Found URL Count</th>';
	innerHtml = innerHtml + '<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="number" data-sort="true" data-sortname="ftype4" tabindex="0" data-sortpath="none">Tag Not Found URL Count</th>';
	innerHtml = innerHtml + '<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="number" data-sort="true" data-sortname="ftype5" tabindex="0" data-sortpath="none">Tag has Error Url count</th>';
	innerHtml = innerHtml + '</tr></thead>';
	return innerHtml;
}

function getAnalyticsDetailsOrErrorList(tagName, detailOrError){
    document.getElementById("tagName").value=tagName;
    document.getElementById("detailOrError").value=detailOrError;
    
    if(document.getElementById("tagName").value == "ALL" && document.getElementById("detailOrError").value =="Error"){
    	document.getElementById("reportLink").setAttribute('action',"<@spring.url '/getAllDetailedReportLinksData.ftl' />");
    	document.getElementById("reportLink").setAttribute('method',"POST");
    	document.getElementById("reportLink").submit();
    }else{
    	document.getElementById("reportLink").setAttribute('action',"<@spring.url '/getDetailedReportLinksData.ftl' />");
    	document.getElementById("reportLink").setAttribute('method',"POST");
    	document.getElementById("reportLink").submit();	
    }
	
}		
</script>
</head>
 <body>
<div id=commonDv">

<div class="content twoCol" style="width:80%;">
<div class="page-content" id="pagecontent" role="main" tabindex="-1">
		<hr style="margin-top: 10px;">
		<div class="mblg">
			<div class="mtxs">
		 		<a id="tcId100" class="tipLink rel" name="tcId100" >Help</a><b><@spring.message "mint.help.reports"/></b><a href="<@spring.message 'mint.help.mailTo'/>" target="_top">
				<@spring.message "mint.help.mailId"/></a>
			</div>
		</div>
		<hr style="margin-top: 0px;">
</div>		
<section class="drop mtlg">
	<div class="cm">
	
	<table cellpadding='0' cellspacing='0' border='0' width='100%'>
			<tr>
			<td class="hd">
				<#if regressionTestExecutionForm.reportTabNumber == 1>
					<h3><@spring.message "mint.viewAnalytics.analyticsReportSummary"/></h3>
				<#elseif regressionTestExecutionForm.reportTabNumber == 2>
					<h3><@spring.message "mint.viewAnalytics.analyticsDetailedReport"/></h3>
				<#elseif regressionTestExecutionForm.reportTabNumber == 3>
					<h3><@spring.message "mint.viewAnalytics.analyticsErrorReport"/></h3>
				 <#else>
				 	<h3><@spring.message "mint.viewAnalytics.analyticsReportSummary"/></h3>
				 </#if>
			</td>
		</tr>
			
	</table>		
				<form modelAttribute="RegressionTestExecutionForm"
										id="reportLink" name="aform" target="_top"
										method="POST" onSubmit="">
				<input type="hidden" id="tagName" name="tagName" >
				<input type="hidden" id="detailOrError" name="detailOrError" >
				<input type="hidden" id="scheduleExecutionId" name="scheduleExecutionId" value="${regressionTestExecutionForm.scheduleExecutionId!}">
				<input type="hidden" id="suitId"  name="suitId" value="${regressionTestExecutionForm.suitId!}">
				<input type="hidden" id="suitType"  name="suitType" value="${regressionTestExecutionForm.suitType!}">
				<input type="hidden" id="selectedSuitId"  name="selectedSuitId">
				<input type="hidden" id="setupTabNumber"  name="setupTabNumber" value="${regressionTestExecutionForm.setupTabNumber!}">
				<input type="hidden" id="currentPageNo"  name="currentPageNo" value="${regressionTestExecutionForm.currentPageNo!}">
				<input type="hidden" id="reportTabNumber" name="reportTabNumber" value="${regressionTestExecutionForm.reportTabNumber!}">
				
					 
					 <div class="" style="float: left;">
							<div class="tab-box">
								<ul style="list-style-type: none">
							 		<li><a class="tabLink" href="#" id="tab1" onclick="checkAnalyticsSummaryReport(1)">View Summary Report</a></li>
							  		<li><a class="tabLink" href="#" id="tab2" onclick="checkAnalyticsDetailsReport(2)">View Detailed Report&nbsp;&nbsp;</a></li>
							 		<li><a class="tabLink" href="#" id="tab3" onclick="checkAnalyticsErrorReport(3)">View Error Report &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></li>
								</ul>
							</div> 
					</div>		
					<div class="" style="overflow: auto;">		 
							
								<#if regressionTestExecutionForm.reportTabNumber == 1>
									<#include "AnalyticsSummaryReportLink.ftl">
								<#elseif regressionTestExecutionForm.reportTabNumber == 2>
									<#include "AnalyticsDetailsReportLink.ftl">
								<#elseif regressionTestExecutionForm.reportTabNumber == 3>
									<#include "AnalyticsErrorReportLink.ftl">
								 <#else>
								 	<#include "AnalyticsSummaryReportLink.ftl">
								 </#if>
								 
					</div>	
			
			<div class="bd">
				<div class="btnBar">
					<a href="#" onClick="loadTabData(3,${regressionTestExecutionForm.currentPageNo!})" class="btn"><span>Back</span></a>
				</div>
			</div>		
	</form>
			
	</div>
	</section>
	<center>
		<div id="analyticsSummary" title="Analytics Summary Report">
		</div>
		<div id="DownloadReportSummary" title="Download Report Summary">
		</div>
	</center>

</div>
</div>
</body>

