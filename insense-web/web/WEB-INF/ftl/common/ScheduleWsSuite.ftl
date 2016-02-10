
<style type="text/css">
  .tab-box {
  	margin-top: 1.5%;
  }
</style>
<script type="text/javascript">
/* function getScheduleTypeWs(){
	if(document.getElementById('ondemand').checked) {
		$('#recurrence').prop('disabled',true);
		document.WsForm.scheduleType.value='OnDemand';
		$('#schDateTime').val('');
		$('#schDateTime').attr('disabled', 'disabled');
		//document.getElementById('datetimespan').style.display='none';
		
	}
	if(document.getElementById('scheduled').checked){
		
		$('#recurrence').prop('disabled',false);
		document.WsForm.scheduleType.value='Scheduled';
		$('#schDateTime').removeAttr('disabled');
		//document.getElementById('datetimespan').style.display='block';
		
	}
} */	


$(document).ready(function(){
	/* $('#recurrence').prop('disabled',true);
	$('#schDateTime').attr('disabled', 'disabled');
	$('#schDateTime').on("keypress keydown keyup", function(e) {
 		return false;
	}); */
	if($('#suitType').val() == 'Broken'){
		$('#baselineScheduleExecutionId').prop('disabled',true);
	}else{
		$('#baselineScheduleExecutionId').prop('disabled',false);
	}
	

});

function enableBts() {
	if($("#behindTheScene").prop("checked")) {
		$("#dataCenter").prop("disabled",false);
	} else {
		$("#dataCenter").prop("disabled",true);
	}
}
function viewCurrentReportsTab(exedcutionId) {
	$("#scheduleExecutionId").val(exedcutionId);
	loadTabData(3);
}
function submitTestsuite(){
	if($("#ondemand2").prop("checked")) {
		if(!$("#schDateTime").val()) {
			alert("Please provide schedule time to proceed");
			return false;
		}
		if($("#recurrenceiddate").prop("checked")) {
			if(!$("#scheduledDate").val()) {
				alert("Please provide schedule date to proceed");
				return false;
			}
		}
		if($("#recurrenceidweekly").prop("checked")) {
			var isChecked = 0;
			 $(".weeklyCheckBox").each(function(){
			      if($(this).prop("checked")) {
			    	  isChecked = 1;
			      }
			});
			if(isChecked == 0) {	 
				 alert("Atleast any of of the day in the week should be selected to proceed");
				 return false;
			}
		}
	}
	document.getElementById("WsForm").setAttribute('action',"<@spring.url '/SaveWebserviceSuites.ftl' />");
	document.getElementById("WsForm").setAttribute('method',"POST"); 
	document.getElementById("WsForm").submit();
}

function getScheduleTypeWs(){
	if(document.getElementById('ondemand1').checked) {
		document.WsForm.scheduleType.value='OnDemand';
		document.getElementById('scheduledTime').style.display='none';
		document.getElementById('datetimepicker3').style.display='none';
		document.getElementById('recurrenceidlydweedat').style.display='none';
		document.getElementById('recurrencewee').style.display='none';
		document.getElementById('recurrencedat').style.display='none';
		document.getElementById('recurrencedlyvalue').style.display='none';
		document.getElementById('recurrenceweevalue').style.display='none';
		document.getElementById('recurrencedatvalue').style.display='none';
	}
	if(document.getElementById('ondemand2').checked){
		document.WsForm.scheduleType.value='Scheduled';
		document.getElementById('scheduledTime').style.display='block';
		document.getElementById('datetimepicker3').style.display='block';
		document.getElementById('recurrenceidlydweedat').style.display='block';
		document.getElementById('recurrencewee').style.display='block';
		document.getElementById('recurrencedat').style.display='block';
		document.getElementById('recurrencedlyvalue').style.display='block';
		document.getElementById('recurrenceweevalue').style.display='block';
		document.getElementById('recurrencedatvalue').style.display='block';
	}
}

function getReccurenceType(){
	if(document.getElementById('recurrenceidweekly').checked){
		document.WsForm.executeBy.value='recurrenceWeekly';
		document.getElementById("recurrence").checked=false;
		document.getElementById("recurrence").disabled = false;		
		$(".weeklyCheckBox").prop("checked",false);
		$(".weeklyCheckBox").attr("disabled",false);
		document.getElementById("scheduledDate").value="";
		document.getElementById("scheduledDate").disabled = true;		
	}else if(document.getElementById('recurrenceiddate').checked){
		document.WsForm.executeBy.value='recurrenceDate';
		document.getElementById("recurrence").checked=false;
		document.getElementById("recurrence").disabled = true;
		$(".weeklyCheckBox").prop("checked",false);
		$(".weeklyCheckBox").attr("disabled",true);
		document.getElementById("scheduledDate").value="";
		document.getElementById("scheduledDate").disabled = false;
	}
} 
</script>
</head>
<body>
	<section class="drop mtlg">
<form id="WsForm" modelAttribute="regressionTestExecutionForm" method="POST" name="WsForm">
<input type="hidden" name="scheduleType">
<input type="hidden" id="suitId" name="suitId" value="${regressionTestExecutionForm.suitId!}"> 
<input type="hidden" id="webserviceSuiteId" name="webserviceSuiteId" value="${regressionTestExecutionForm.webserviceSuiteId!}"> 
<input type="hidden" name="executeBy" value="recurrenceDate"> 
<input type="hidden" id="currentPageNo"  name="currentPageNo" value="${regressionTestExecutionForm.currentPageNo!}">
<div class="cm">

		<table cellpadding='0' cellspacing='0' border='0' width='100%'>

			<tr>
				<td class="hd">
					<h3>Schedule Webservice</h3>
				</td>
			</tr>
			<tr class="bd">
				<td style="padding:5"><br>
				<table>
				<tr> <td>
					<table class="bd rowheight35">
									<br/>
									<tr class="lblFieldPair">
										<td class="lbl"><label for="application">Schedule
												Type</label></td>
										<td class="input"><input type="radio" id="ondemand1"
											name="ondemand" checked="true" value="OnDemand"
											onClick="getScheduleTypeWs()">&nbsp;&nbsp;OnDemand <br>
											<input type="radio" id="ondemand2" name="ondemand"
											value="Scheduled" onClick="getScheduleTypeWs()">&nbsp;&nbsp;Scheduled
										</td>
									</tr>
									<!-- <tr class="lblFieldPair">
										<td class="lbl">Schedule Type</td>
										<td class="input">
											<input type="radio" id="ondemand" name="onDemandOrScheduled" checked="true" value="OnDemand" onClick="getScheduleTypeWs()">&nbsp;&nbsp;OnDemand
										</td>
									</tr>
									
									
									<tr class="lblFieldPair">
										<td class="lbl">&nbsp;</td>
										<td class="input">
											<input type="radio" id="scheduled" name="onDemandOrScheduled" value="Scheduled" onClick="getScheduleTypeWs()">&nbsp;&nbsp;Scheduled
										</td>
									</tr> -->
									
							<!-- 		<tr class="lblFieldPair">
										<td class="lbl">
											<div id="scheduledTime1">
												Scheduled Time
											</div>
										</td>
										<td class="input">
											<div id="datetimepicker31" class="input-append">
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
									</tr> -->
									<tr class="lblFieldPair">
										<td class="lbl">Additional Email Recepients</td>
										<td class="input">
											<input style="width:350px;" maxlength="250" type="text" id="emailRecepients" name="emailRecepients"> (COMMA Seperated)
										</td>
									</tr>
				
									<tr class="lblFieldPair">
										<td class="lbl">Baseline Dates</td>
									<td class="input"><select id="baselineDate"
										name="wsResultsId">
											<option value="0">--Do Not Compare--</option>
									</select></td>
									</tr>
							
			
							</table>
							</td>
							<td>
							<table class="bd rowheight35"><br/>										
									<tr class="lblFieldPair">
										<td class="lbl">
											<div id="scheduledTime">
												<label for="ScheduledTime">Scheduled Time</label>
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
										<td class="lbl">
											<div id="recurrencedat">
												Date &nbsp;&nbsp;<input type="radio" id="recurrenceiddate"
													name="recurrenceid" onClick="getReccurenceType()">
											</div>
										</td>
										<td class="input">
											<div id="recurrencedatvalue">
												<input type="text" id="scheduledDate" name="scheduledDate"
													value="">
												<script type="text/javascript">
									$('#scheduledDate').datetimepicker({
										timepicker:false
									});
								</script>
											</div>
										</td>
									</tr>
									<tr class="lblFieldPair">
										<td class="lbl">
											<div id="recurrencewee">
												Weekly &nbsp;&nbsp;<input type="radio" id="recurrenceidweekly"
													name="recurrenceid" onClick="getReccurenceType()">
											</div>
										</td>
										<td class="input">
											<div id="recurrenceweevalue">
													<input type="checkbox" class="weeklyCheckBox" id="recurrenceidweekly1"
															name="recurrenceidweekly1">Monday&nbsp;&nbsp; 
													<input type="checkbox" class="weeklyCheckBox" id="recurrenceidweekly2"
														name="recurrenceidweekly2">Tuesday&nbsp;&nbsp;&nbsp;
													<input type="checkbox" class="weeklyCheckBox" id="recurrenceidweekly3"
														name="recurrenceidweekly3">Wednesday&nbsp;&nbsp;
													<input type="checkbox" class="weeklyCheckBox" id="recurrenceidweekly4"
														name="recurrenceidweekly4">Thursday<br/><br/>
													<input type="checkbox" class="weeklyCheckBox" id="recurrenceidweekly5"
														name="recurrenceidweekly5">Friday&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<input type="checkbox" class="weeklyCheckBox" id="recurrenceidweekly6"
														name="recurrenceidweekly6">Saturday&nbsp;&nbsp;
													<input type="checkbox" class="weeklyCheckBox" id="recurrenceidweekly7"
														name="recurrenceidweekly7">Sunday<br/>
											</div>
										</td>
									</tr>
									<tr class="lblFieldPair">
										<td class="lbl">
											<div id="recurrenceidlydweedat">
												<label for="stopAfter">Recurrence</label>
											</div>
										</td>
										<td class="input">
											<div id="recurrencedlyvalue">
												<input type="checkbox" id="recurrence" name="recurrence">
											</div>
										</td>
									</tr>
								</table>
							</td>
							</tr>
							</table>
					
						
					</td>
				</tr>
				<tr class="bd">
						<td class="btnBar"><a href="#" onClick="submitTestsuite();" class="btn"><span>Schedule</span></a>
						</td>
				</tr>
			</table>		
		</section>
</body>
</html>
