<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
   <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
    <script src="js/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script type="text/javascript" src="js/formValidation.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
    <script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
<script>

$(document).ready(function(){
	$('#sendEmail').val("false");
	$('.emailTriggerOption').attr('disabled', 'disabled');
	getScheduleType();
	$('#schDateTime').on("keypress keydown keyup", function(e) {
 		return false;
	});
	 $('#sendEmail').click(function(event) {
	    	if(this.checked) {
	    		$('#sendEmail').val("true");
	    		$('.emailTriggerOption').removeAttr('disabled');
	    	
	    	} else {
	    		$('#sendEmail').val("false");
	    		$('.emailTriggerOption').attr('disabled', 'disabled');
	    	
	    	}
	    });

	});
	
/* function getWSBaselineDates(){
	var wsSuiteId = document.getElementById("wsSuiteId").value;
		if(wsSuiteId != "0") {
	var request = $.ajax({  url: 	
	"GetWSBaselineDates.ftl",  
		type: "POST",  
		cache: false,
		data: { wsSuiteId : wsSuiteId},  
		dataType: "text"}); 
			request.done(function( baselineDatesList ) {
				var Outmsg= JSON.stringify(msg);
				Outmsg = JSON.parse(Outmsg);
				  $.each(Outmsg,function(key, val) {
				  $('<option />', {value: key, text: val}).appendTo("#baselineDate");
				  });
 		 }); 
  	request.fail(
 	 function( jqXHR, textStatus ) 
  		{ }); 
		}
} */

function getWSBaselineDates(){
	enableLoader();
	var wsSuiteId = document.getElementById("wsSuiteId").value;
		if(wsSuiteId != "0") {
	var request = $.ajax({  url: 	
	"GetWSBaselineDates.ftl",  
		type: "POST",  
		cache: false,
		data: { wsSuiteId : wsSuiteId},  
		dataType: "text"}); 
			request.done(function( baselineDatesList ) {
				
				if ($.parseJSON(baselineDatesList) != ''){
					$.each($.parseJSON(baselineDatesList), function(idx, baselineDate) {
						 $('<option />', {value: baselineDate.wsScheduleId, text: baselineDate.dateTime}).appendTo("#baselineDate");
					});
				}
				disableLoader();
 		 }); 
  	request.fail(
 	 function( jqXHR, textStatus ) 
  		{
 		disableLoader();
}); 
		}
}


	function getScheduleType(){
 		if(document.getElementById('ondemand').checked) {
 			document.DatasetForm.scheduleType.value='OnDemand';
 			$('#schDateTime').val('');
 			$('#schDateTime').attr('disabled', 'disabled');
 			//document.getElementById('datetimespan').style.display='none';
 		}
 		if(document.getElementById('scheduled').checked){
 			document.DatasetForm.scheduleType.value='Scheduled';
 			$('#schDateTime').removeAttr('disabled');
 			//document.getElementById('datetimespan').style.display='block';
 		}
 	}	
 	
 	function selectAll() {
 		if(document.getElementById("select_all").checked) {
 			$('.checkbox1').each(function(){ this.checked = true; });
 		} else {
 			$('.checkbox1').each(function(){ this.checked = false; });
 		}
 	}

function getSuiteDetails(){
	$('#buttonsTable').hide();
		getDetails();
 		getWSBaselineDates();
}

function displayValue(key) 
{
	var key = document.getElementById(key).value;
	if(key.contains("requestXML") || key.contains("ReqXML")) {
		var startString = "<td class=crop>";
		var startpos = key.indexOf(startString) + startString.length;
		var endpos = key.indexOf("</tr>",startpos); 
		newstr = key.substring(startpos,endpos-5);
		var key= key.replace(newstr, "<textarea style='width:100%; height:100%;' cols='70' rows='15'>"+newstr+"</textarea>");
	}
	showReqParametersPopUp(key);
	
	/* var w = window.open('', "", "width=600, height=400, scrollbars=yes");
	var pos = key.indexOf("ReqXML"); 
	w.document.write("<html><head></head><body><form><table border='1' width=60% style='border-collapse:collapse'><tr>")
	w.document.write(key);
	w.document.write("</tr></table></form></body></html>"); */
}

function showReqParametersPopUp(key) {
	$("#ReqParameters").dialog({
		width : 700,
		height : 650,
		title : 'Saved Request Parameters',
		resizable : false,
		position : [ 170, 0 ]
	});
	$("#ReqParameters").html('<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="completion_summary_content">');
	$("#ReqParameters").html('<div class="bd">');
	$("#ReqParameters").html('<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">');
	$("#ReqParameters").html('<table id="Saved_Request_Parameters" class="styleA fullwidth sfhtTable" summary=""></table></div></div></div>');
	$('#Saved_Request_Parameters').empty();
	$('#Saved_Request_Parameters').html(key);
	$("#ReqParameters").show();
	window.scrollTo(0, 1000);
}

function getDetails(){
	enableLoader();
	 $('#testSuiteDetailsTable').html("");
	var wsSuiteId = document.getElementById("wsSuiteId").value;
	var suites = "<tr><td class='hd'><font face='verdana,arial' ><h3>Service Name</h3></font></td>";
	suites +="<td class='hd'><font face='verdana,arial' ><h3>Operation Name</h3></font></td>";
	suites +="<td class='hd' ><font face='verdana,arial' ><h3>Parameter Name | Value</h3></font></td></tr>";
	var request = $.ajax({url: 	
		"GetSuiteDetails.ftl",  
			type: "POST",  
			cache: false,
			data: { wsSuiteId : wsSuiteId},  
			dataType: "json"}); 
				request.done(function( msg ) {
				
					var Outmsg= JSON.stringify(msg);
					Outmsg = JSON.parse(Outmsg);
					var i=1;
					 $.each(Outmsg,function(key, val) {
					
						 var idStr = "i" + i;
						 var paramset = key.split(":");
						 var oprName = paramset[1];
						 var serviceName = paramset[2];
						 var datsetName = paramset[3];
						 suites += "<tr><td>"+serviceName+"</td>";
						 suites +="<td>"+oprName+"</td>";
						 suites +="<td>";
						 suites +="<a href='#' onclick=\"displayValue('"+idStr+"')\">"+datsetName+ "</a>";
						 suites +="<input type='hidden' id="+idStr+"  value='";
						 for (var i1 in val){
						
						 		
							 	suites+= '<tr><td>' + i1 + '</td>';
							 	suites+= '<td class=' + 'crop' + '>' + val[i1] +'</td></tr>';
						 	
						 
						}
						 
						 suites+= "'></td></tr>";
						 i++;
			    	});
					 $('#testSuiteDetailsTable').append(suites);
					 disableLoader();
	 		 }); 
	  	request.fail(
	 	 function( jqXHR, textStatus ){
	 		 disableLoader();	 		 
	  		}); 

}


function submitTestsuite(){
	var schedultTime=document.DatasetForm.schDateTime.value;
	var scheduleTypes=document.DatasetForm.scheduleType.value;

	if(schedultTime== "" && scheduleTypes=="Scheduled"){
		alert('Please Select Schedule Time');
		return false;
	} else {
	document.DatasetForm.action = "WSSubmitTestsuite.ftl";
	document.DatasetForm.submit();
	}
}
</script>


<body onLoad="window.scrollTo(0,readCookie('ypos'))">
	<div id="ReqParameters" title="Request Parameters">
	</div>
	<div id="ServiceResponse" title="Service Response">
	</div>
	
<div class="page-content" id="pagecontent" role="main" tabindex="-1">
<section class="drop mtlg">
<form id="DatasetForm" modelAttribute="webserviceSetupForm" method="POST" name="DatasetForm">
<input type="hidden" name="scheduleType" value="OnDemand">
<div class="cm">

		<table cellpadding='0' cellspacing='0' border='0' width='100%'>

			<tr>
				<td class="hd">
					<h3>Schedule Webservice</h3>
				</td>
			</tr>
			<tr class="bd">
				<td style="padding:5"><br>
					<table class="bd rowheight35">
									<br />
									<tr class="lblFieldPair">
										<td class="lbl">Environment Category</td>
										<td class="input">
											<select id="environmentId" name="environmentId" onchange="getWSTestSuites()">
												<option value="-1" selected="true">Select</option>
												<#list environmentCategoryList as environment> 
													<option value="${environment.environmentCategoryId}">		 
														${environment.environmentCategory.environmentCategoryName}
													</option>
												</#list>
											</select><font color="red" size=-1>&nbsp;*</font>
										</td>
									</tr>
									<tr class="lblFieldPair">
										<td class="lbl">Webservice Suite Name</td>
										<td class="input">
											<select id="wsSuiteId" name="wsSuiteId" tabindex="1" onChange="getSuiteDetails()"> 
												<option value="0">Select</option>
											</select>
										</td>
									</tr>
								</table>	
					
						
					</td>
				</tr>
			</table>
			
		
			<table class="bd rowheight35" >
									  <tr>
									   <td>
									        <center>
									   		<div ><table style="width:90%" class="styleA drop2 sfhtTable" id="testSuiteDetailsTable"></table></div>
									   		</center>
									   </td>
									   <tr>
			</table>
			
			
									
		</section>
			
			<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Schedule Type</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<input type="hidden" id="setupTabNumber" name="setupTabNumber" value="1">
								<table class="bd rowheight35">
									<br/>
									<tr class="lblFieldPair">
										<td class="lbl">Schedule Type</td>
										<td class="input">
											<input type="radio" id="ondemand" name="onDemandOrScheduled" checked="true" value="OnDemand" onClick="getScheduleType()">&nbsp;&nbsp;OnDemand
										</td>
									</tr>
									
									
									<tr class="lblFieldPair">
										<td class="lbl">&nbsp;</td>
										<td class="input">
											<input type="radio" id="scheduled" name="onDemandOrScheduled" value="Scheduled" onClick="getScheduleType()">&nbsp;&nbsp;Scheduled
										</td>
									</tr>
									<tr class="lblFieldPair">
										<td class="lbl">
											<div id="scheduledTime">
												Scheduled Time
											</div>
										</td>
										<td class="input">
											<div id="datetimepicker3" class="input-append">
												<input name="schDateTime" class="required" id="schDateTime"
													data-format="hh:mm:ss" type="text"></input>
												<script type="text/javascript">
													$(function(){
														$('#schDateTime').datetimepicker({
															datepicker:false,
															format:'H:i',
															step:5
														});
													});
												</script>
											</div>
										</td>
									</tr>
									<tr class="lblFieldPair">
										<td class="lbl">Recurrence</td>
										<td class="input">
											<input type="checkbox" id="recurrence" name="recurrence">
										</td>
									</tr>
									
									<tr class="lblFieldPair">
										<td class="lbl">Additional Email Recepients</td>
										<td class="input">
											<input style="width:500px;" maxlength="250" type="text" id="emailRecepients" name="emailRecepients"> (COMMA Seperated)
										</td>
									</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</section>
		
		
		
	
			<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Baseline</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<input type="hidden" id="setupTabNumber" name="setupTabNumber" value="1">
			
								<table class="bd rowheight35" id="baselineTableHeading">
														<tr class="lblFieldPair">
															<td class="lbl">Baseline Dates</td>
															<td class="input"><select id="baselineDate"
																name="wsResultsId">
																	<option value="0">----------------Select------------------</option>
															</select></td>
														</tr>
								</table>
							</td>
						</tr>
							<tr class="bd">
						<td class="btnBar">
							<a href="#" class="btn" id="btnSubmit" onclick="submitTestsuite()"><span>Submit</span></a>
						</td>
					</tr>
				</table>
				</div>
				</section>
	<center>
</br></br>
</center>


</div>

</form>


</div>

</body>