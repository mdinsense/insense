<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
   <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
    <script src="js/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script type="text/javascript" src="js/formValidation.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
    <script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	 enableLoader();
		if('${webserviceSetupForm.environmentId!}' > 0){
		 $('#environmentId').val('${webserviceSetupForm.environmentId!}');
		}
		getWSTestSuites();
		setTimeout(getWSReportDates, 1000);
		
}); 
	 


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
					'<table id="Service_Response" class="styleA fullwidth sfhtTable" summary=""><textarea row="100" cols="150" style="width:100%; height:100%">'+key+'</textarea></table></div></div></div>');
	$('#Service_Response').empty();
	//	$('#Service_Response').html(key);
	/* document.getElementById('Service_Response').appendChild(
			document.createTextNode(key)); */
	$("#ReqParameters").show();
	window.scrollTo(0, 1000);
}

function getWSReportDates(){
	 enableLoader();
	var wsSuiteId = document.getElementById("wsSuiteId").value;
	$('#wsScheduleId').empty();
	
	var request = $.ajax({  url: 	
	"GetWSReportDatesFromSuiteId.ftl",  
		type: "POST",  
		data: { wsSuiteId: wsSuiteId},  
		dataType: "text"}); 
			request.done(function( reportsScheduleList ) {

				if ($.parseJSON(reportsScheduleList) != ''){
  					$.each($.parseJSON(reportsScheduleList), function(idx, reports) {
  						$('<option />', {value: reports.wsScheduleId, text: reports.generatedTime}).appendTo("#wsScheduleId");
  					});
				 }
				
				 if('${webserviceSetupForm.wsScheduleId!}' > 0){
						$('#wsScheduleId').val('${webserviceSetupForm.wsScheduleId!}');
				}
			});
  	request.fail(
 	 function( jqXHR, textStatus ){ 
 		disableLoader();
  		}); 
  	disableLoader();
}

function validateCommonInputFields(){
	var environment=document.getElementById("environmentId").value;
	var suitName=document.getElementById("wsSuiteId").value;
	var reportDate=document.getElementById("wsScheduleId").value;
	if(environment == "0"){
		alert('Please Select the Environment');
		document.WSReportsForm.environmentId.focus();
		return false;
	}else if(suitName == "0"){
		alert('Please Select the Suit Name');
		document.WSReportsForm.wsSuiteId.focus();
		return false;
	}else if(reportDate == "0"){
		alert('Please Select the report Date');
		document.WSReportsForm.wsScheduleId.focus();
		return false;
	}
	return true;
}

function viewReports(){
	if(validateCommonInputFields()){
		document.getElementById("WSReportsForm").action='<@spring.url "/WSReportsRetrieve.ftl"/>';
		document.getElementById("WSReportsForm").submit();	
	}
	
}
</script>

<body onLoad="window.scrollTo(0,readCookie('ypos'))">
<div id="ServiceResponse" title="Service Response">
</div>
<div class="page-content" id="pagecontent" role="main" tabindex="-1">
<section class="drop mtlg">
<form id="WSReportsForm" modelAttribute="webserviceSetupForm" method="POST" name="WSReportsForm">
<input type="hidden" id="elementsArray" name="elementsArray" value="">
<div class="cm">

		<table cellpadding='0' cellspacing='0' border='0' width='100%'>

			<tr>
				<td class="hd">
					<h3>Webservice Reports</h3>
				</td>
			</tr>
			<tr class="bd">
				<td style="padding:5"><br>
					
						<table class="dropdownTable" width="100%" style="table-layout:fixed;margin-left:100px;">

						  	<tr>
								<td>	
									<font face="verdana,arial" size="-1">Environment</font>
								</td>
						  		<td>
						  			<font face="verdana,arial" size="-1">Suite Name</font>
								</td>
								<td>
									<font face="verdana,arial" size="-1">Report Dates</font>
								</td> 
						  	</tr>
						  	<tr>
							  	<td>
							  	<select class="requiredSelect" id="environmentId" name="environmentId" onChange="getWSTestSuites()">
										<option value="0">Select</option>
										<#list environmentCategoryList as environment> 
												<option value="${environment.environmentCategoryId}">		 
												${environment.environmentCategory.environmentCategoryName}</option>
										</#list>
									</select>
							  	</td>
							  	<td>
							  		<select class="requiredSelect" id="wsSuiteId" name="wsSuiteId" tabindex="1" onChange="getWSReportDates()"> 
										<option value="0">Select</option>
									</select>
								</td>
							  	<td>
								  	<select class="requiredSelect" id="wsScheduleId" name="wsScheduleId" tabindex="2" >
								  	<option value="0">Select</option>
								  	
									</select>
								</td>
						  	</tr>
						</table>
						<center><font color="red" size=-1>&nbsp;* mandatory &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></center>	
					</td>
				</tr>
			</table>
			
	<table  width='100%'
	<tr class="bd">
		<td class="btnBar" >
			<a href="#" class="btn" id="SubmitSelectedServices" onclick="viewReports()"><span>View Reports</span></a>
		</td>
	</tr>
	</table>
</div>
</form>
</section>
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




</div>

</body>