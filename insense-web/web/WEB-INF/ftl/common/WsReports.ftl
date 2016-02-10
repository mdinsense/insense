
<style type="text/css">
  .tab-box {
  	margin-top: 1.5%;
  }
</style>
<script type="text/javascript">
function displayXML(getValue, title){
	var xmlContent="";
	xmlContent = document.getElementById(getValue).value;
	showServiceResponsePopUp(xmlContent, title);
}

function showServiceResponsePopUp(key, title) {
	$("#ServiceResponse").dialog({
		width : 700,
		height : 650,
		title : title,
		resizable : false,
		position : [ 170, 0 ]
	});
	$("#ServiceResponse")
			.html(
					'<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="completion_summary_content">');
	$("#ServiceResponse").html('<div class="bd">');
	$("#ServiceResponse")
			.html(
					'<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">');
	$("#ServiceResponse")
			.html(
					'<table id="Service_Response" class="styleA fullwidth sfhtTable" summary=""></table></div></div></div>');
	$('#Service_Response').empty();
	//	$('#Service_Response').html(key);
	document.getElementById('Service_Response').appendChild(
			document.createTextNode(key));
	$("#ReqParameters").show();
	window.scrollTo(0, 1000);
}
$(document).ready(function(){
	 enableLoader();
	 var wsSuiteId = document.getElementById("webserviceSuiteId").value;
		$('#wsScheduleId').empty();
		
		var request = $.ajax({  url: 	
		"GetWSReportDatesFromSuiteId.ftl",  
			type: "POST",  
			data: { wsSuiteId: wsSuiteId},  
			dataType: "text"}); 
				request.done(function( reportsScheduleList ) {
				$('<option />', {value: "0", text: "Select"}).appendTo("#wsScheduleId");
					if ($.parseJSON(reportsScheduleList) != ''){
						$.each($.parseJSON(reportsScheduleList), function(idx, reports) {
							$('<option />', {value: reports.wsScheduleId, text: reports.generatedTime}).appendTo("#wsScheduleId");
						});
					 }
					
					setTimeout(setReportDate,500);
				});
		request.fail(
		 function( jqXHR, textStatus ){ 
			disableLoader();
			}); 
	
		disableLoader();	
		
}); 

function setReportDate(){
	$('#wsScheduleId').val('${regressionTestExecutionForm.wsScheduleId!}');
}
function viewReports(){

		document.getElementById("WsForm").action='<@spring.url "/WSReportsHome.ftl"/>';
		document.getElementById("WsForm").submit();	
	
}
</script>
</head>
<body>
<div id="ServiceResponse" title="Service Response">
</div>
<section class="drop mtlg">
<form id="WsForm" modelAttribute="regressionTestExecutionForm" method="POST" name="WsForm">
<input type="hidden" name="scheduleType" value="">
<input type="hidden" id="suitId" name="suitId" value="${regressionTestExecutionForm.suitId!}"> 
<input type="hidden" id="webserviceSuiteId" name="webserviceSuiteId" value="${regressionTestExecutionForm.webserviceSuiteId!}"> 
<div class="cm">

		<table cellpadding='0' cellspacing='0' border='0' width='100%'>

			<tr>
				<td class="hd">
					<h3>Reports</h3>
				</td>
			</tr>
			<tr class="bd">
				<td style="padding:5"><br>
					<table class="bd rowheight35">
									<br/>
								<tr class="lblFieldPair">
								<td class="lbl" style="width:100"><label for="comparisonDates">Report
										Dates</label></td>
								<td class="input">
									<select class="requiredSelect" id="wsScheduleId" name="wsScheduleId" tabindex="2" onchange="viewReports()" >
							  	
									</select>
								</td>
							</tr>
				
									
							
			
					</table>
					
						
					</td>
				</tr>
				
			</table>		
		</section>
		<#if wsReportsDataList?has_content>
		<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="webservice_report_table_content">
  	<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
		<table id="webservice_report_table" class="styleA fullwidth sfhtTable" summary="">
			<caption></caption>
			<thead>
				<tr>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype1" tabindex="0" data-sortpath="none">Services</th>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype2" tabindex="0" data-sortpath="none">Operation</th>
				
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype3" tabindex="0" data-sortpath="none">Current Run Time</th>
				
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype4" tabindex="0" data-sortpath="none">Baseline Time</th>
					<th class="txtl header w15">Request XML</th>
					<th class="txtl header w15">Response XML</th>
					<th class="txtl header w15">Baseline Request XML</th>
					<th class="txtl header w15">Baseline Response XML</th>
					<th class="txtl header w10">Differences</th>
					<th class="txtl header w10">Matched</th>
				</tr>
			</thead>
			<tbody>
			<#if wsReportsDataList?has_content>
			<#list wsReportsDataList! as wsreport>
				<tr>
					<td scope="row">${wsreport.service!}	</td>
					<td scope="row">${wsreport.operation!}	</td>
					<td scope="row">${wsreport.executionDate!}	</td>
					<td scope="row">${wsreport.baselineDate!}	</td>
					<td scope="row">
					<#assign req=wsreport.requestXML> 	
						<input type="hidden" id="req${wsreport_index + 1}" value="${req}">				
						<a href="javascript:displayXML('req${wsreport_index + 1}', 'Request XML');">View</a>
					</td>
					<td>
					<div id="responseXML" name="responseXML">
						<#assign resp=wsreport.responseXML> 
						<input type="hidden" id="resp${wsreport_index + 1}" value="${resp}">		
						<a href="javascript:displayXML('resp${wsreport_index + 1}','Response XML');">View</a>
								
								
					</div>
					</td>	
					<td >		
					<div id="baselineRequestXML" name="baselineRequestXML">
						<#assign breq=wsreport.baselineRequestXML!> 
						<#if breq!="-">
						<input type="hidden" id="breq${wsreport_index + 1}" value="${breq}">		
						<a href="javascript:displayXML('breq${wsreport_index + 1}', 'Baseline Request XML');">View</a>
						<#else>
								NA
						</#if>	
					</div>
					</td>
					<td >		
					<div id="baselineResponseXML" name="baselineResponseXML">
						<#assign bresp=wsreport.baselineResponseXML!> 
						<#if bresp!="-">
						<input type="hidden" id="bresp${wsreport_index + 1}" value="${bresp}">		
						<a href="javascript:displayXML('bresp${wsreport_index + 1}','Baseline Response XML');">View</a>
						<#else>
								NA
						</#if>
					</div>
					</td>
					<td >		
					<div id="differences" name="differences">
												
								<#assign diffi=wsreport.differences> 
								<#if diffi!="-">
								<input type="hidden" id="diffi${wsreport_index + 1}" value="${diffi}">					
								<a href="javascript:displayXML('diffi${wsreport_index + 1}', 'Difference');">View</a>
								<#else>
								NA
								</#if>
								
					</div>
					</td>
						<td >		
							<div id="matched" name="matched">
										${wsreport.matched!}
										
										
							</div>
							</td>
				</tr>
			</#list>
			<#else>
					<tr><td colspan='10'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
			</#if>
			</tbody>
		</table>
		</div>
		<@mint.tablesort id="#webservice_report_table" casesensitive="false" jsvar="webservice_report_table__js"/>
		<@mint.paginate table="#webservice_report_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
		
	</div>
</div>
	</#if>	
</body>
</html>
