<style type="text/css">
  .tab-box {
  	margin-top: 1.5%;
  }
  .module {
  	font-weight: bold;
    color: #484A4D;
    font-size: 11px;
    vertical-align:bottom;
  }
  .testcase {
  	font-style: italic;
  	font-weight: normal;
  }
  .Datatable {
    border: none;
  }
</style>
<script type="text/javascript">
$(document).ready(function(){
	if($('#suitType').val() == 'Broken'){
		$('#baselineScheduleExecutionId').prop('disabled',true);
	}else{
		$('#baselineScheduleExecutionId').prop('disabled',false);
	}
	checkUncheckAll(true);
	<#if model.moduleList?? && model.moduleList?has_content>
		$('#baselineScheduleExecutionId').prop('disabled',true);
		//document.getElementById("baselineScheduleExecutionId").disabled = true;
	</#if>
});
function selectModulesAndTestCases(id,elemType) {
	var isMoudleValid = 0;
	if(id == 0 && elemType == 0) {
		if($("#0").prop("checked")) {
			checkUncheckAll(true);
		} else {
			checkUncheckAll(false);
		}
	} 
	if(elemType > 0 && $("#0").prop("checked")) {
		$("#0").prop("checked",false);
	}
	if(elemType == 1){
		if($("#"+id).prop("checked")) {
			$("[id^="+id+"]").each(function() {
				$(this).prop("checked",true);
			});
		} else {
			$("[id^="+id+"]").each(function() {
				$(this).prop("checked",false);
			});
		}
	}
	if(elemType == 2) {
		var moduleStr = id;
		moduleStr = moduleStr.split("_testcase_");
		var mId = moduleStr[0];
		if($("#"+id).prop("checked")) {
			$("#"+mId).prop("checked",true);
		} 
		$("[id^="+mId+"_testcase_]").each(function() {
			if($(this).prop("checked")) {
				isMoudleValid = 1;
			}
		});
		if(isMoudleValid == 0) {
			$("#"+mId).prop("checked",false);
		}
	}
}
function checkUncheckAll(isCheck) {
	$(".modules").each(function() {
		$(this).prop("checked",isCheck);
	});
	$(".testCase").each(function() {
		$(this).prop("checked",isCheck);
	});
}
function RunDemand(){
	if ( $("#suitId").val().length > 0 ){
		if($("#behindTheScene").prop("checked")) {
			if($("#dataCenter").val() == -1) {
				alert("Please select the data center to proceed");
				return false;
			}
		}
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
		
		document.getElementById("suitId").value = $("#suitId").val();
		document.getElementById("aform").setAttribute('action',"<@spring.url '/SaveRegressionTestSuits.ftl' />");
		document.getElementById("aform").setAttribute('method',"POST");
		document.getElementById("aform").submit();
	} else{
		alert('Please select a suit to run.');
	}
}
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


</script>
</head>
<body>
	<section class="drop mtlg">
		<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h3>Choose Schedule Type</h3>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding: 5">
							<form modelAttribute="RegressionTestExecutionForm"
								id="aform" name="aform" target="_top"
								method="POST" onSubmit="return validateInputValues()">
								<input type="hidden" id="TestSuitName" name="TestSuitName"
									value=""> 
								<input type="hidden" id="suitId"
									name="suitId" value="${regressionTestExecutionForm.suitId}"> 
								<input type="hidden"
									id="isPublicSite" name="isPublicSite" value=""> <input
									type="hidden" id="publicSite" name="publicSite" value=" ">
								<input type="hidden" name="scheduleType" value="">
								<input type="hidden" name="SaveDemandData" value="RunDemand">
								
								<input type="hidden" name="executeBy" value="recurrenceDate"> 
									
								<input type="hidden" id="staticUrlTesting"
									name="staticUrlTesting" value="off"> <input
									type="hidden" id="moduleAll" name="moduleAll" value="off">
									
								<input type="hidden" id="CreatedbyGRP" name="CreatedbyGRP"
									value="off"> <input type="hidden" id="CreatedbyAL"
									name="CreatedbyAL" value="off">
								<input type="hidden" id="suitType"
									name="suitType" value="${regressionTestExecutionForm.suitType!}">
								<input type="hidden" id="currentPageNo"  name="currentPageNo" value="${regressionTestExecutionForm.currentPageNo!}">
								<table>
								<tr><td>
								<table class="bd rowheight35"><br/>
									<tr class="lblFieldPair">
										<td class="lbl"><label for="lastCurrentRun">
												Last Run Date :</label></td>
										<td class="input">
											<#if lastRunDateforScheduleExecution?has_content>
												${lastRunDateforScheduleExecution!} &nbsp;&nbsp;<a href="#" onClick="viewCurrentReportsTab('${ScheduleExecutionIdforlastRunDate!}');" >View Reports</a>
											<#else>
												N/A
										 	</#if>
										</td>
									</tr>
									<tr class="lblFieldPair">
										<td class="lbl"><label for="baselineScheduleExecutionId">Compare with Baseline</label></td>
										<td class="input">
										<select id="baselineScheduleExecutionId" name="baselineScheduleExecutionId">
											<option value="0">--Do Not Compare--</option>
											<option value="-1">--Compare With previous Baseline--</option>
											<#list baselineScheduleExecutionList! as scheduleExecution>
												<option value="${scheduleExecution.scheduleExecutionId!}">${scheduleExecution.testExecutionStartTime!}</option>
											</#list>	
											
										</select></td>
									</tr>
									<tr class="lblFieldPair">
										<td class="lbl"><label for="application">Schedule
												Type</label></td>
										<td class="input"><input type="radio" id="ondemand1"
											name="ondemand" checked="true" value="OnDemand"
											onClick="getScheduleType()">&nbsp;&nbsp;OnDemand <br>
											<input type="radio" id="ondemand2" name="ondemand"
											value="Scheduled" onClick="getScheduleType()">&nbsp;&nbsp;Scheduled
										</td>
									</tr>
									<tr class="lblFieldPair">
										<td class="lbl"><label for="stopAfter">Stop
												after URL count (Test only for given number of URLs)</label></td>
										<td class="input"><input type="text" min="1"
											id="stopAfter" name="stopAfter"
											onkeypress="return isNumberKey(event)"></td>
									</tr>
									<tr class="lblFieldPair">
											<td class="lbl">
													<label for="behindTheScene">Behind The Scene</label>
											</td>
											<td class="input">
													<input type="checkbox" id="behindTheScene" name="behindTheScene" onClick="enableBts()">
											</td>
									</tr>
									<tr class="lblFieldPair">
											<td class="lbl">
													<label for="dataCenter">Data Center</label>
											</td>
											<td class="input">
											<select id="dataCenter" name="dataCenter" disabled>
												<option value="-1">--Select DataCenter--</option>
												<option value="Charlotte-A">Charlotte-A</option>
												<option value="Charlotte-B">Charlotte-B</option>
												<option value="Broomfield">Broomfield</option>
											</select>
									</td>
									</tr>
										<tr class="lblFieldPair">
											<td class="lbl">
													<label for="notes">Notes</label>
											</td>
											<td class="input">
												<input class="" type="text" id="notes" name="notes" maxlength="100" style="width: 50ex;">
											</td>
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
								</td></tr>
								</table>
								<#if model.moduleList?? && model.moduleList?has_content>
								<table class="fullwidth">
									<tr>
										<td width="3%" class="module"><hr></td>
									 	<td width="10%"class="module">
											&nbsp;&nbsp;&nbsp;<input type="checkbox" id="0" value="0" onClick="selectModulesAndTestCases(this.id,0);" name="modules" checked="true"/>&nbsp;All Transactional Modules&nbsp;
									 	</td>
									 	<td width="50%" class="module"><hr></td>
									</tr>
								</table>
								<table class="fullwidth shfTable drop2">
									<tr>
										<td style="text-align: center;">
												<center>
												<table class="styleA fullwidth" style="border: none;border-collapse: collapse;">
												<#assign moduleColumnCount = 1>
													<tr>
													<td class="module">&nbsp;</td>
												    <#list model.moduleList! as module>
												    	<#if moduleColumnCount <= 4>
												    	 	<td class="module">
												    	 		<input type="checkbox" class="modules" id="module_${module.applicationModuleXrefId!}" onClick="selectModulesAndTestCases(this.id,1);" value="${module.applicationModuleXrefId!}" name="modules">&nbsp;&nbsp;${module.moduleName!}
												    	 		<table style="border: none;border-collapse: collapse;">
												    	 			<#list module.transactionTestCaseList! as testCase>
												    	 			<tr>
												    	 				<td class="testcase">
												    	 					<input type="checkbox" class="testCase" onClick="selectModulesAndTestCases(this.id,2);" id="module_${module.applicationModuleXrefId!}_testcase_${testCase.testCaseId!}" value="${testCase.testCaseId!}" name="testCases">&nbsp;&nbsp;${testCase.transactionName!}
												    	 				</td>
												    	 			</tr>
												    	 			</#list>
												    	 		</table>
												    	 	</td>
												    	 	<#assign moduleColumnCount = moduleColumnCount+1>
												    	<#else>
												    		<#assign moduleColumnCount = 1>
												    		</tr><tr><td class="module">&nbsp;</td>
												    	</#if>
												    </#list>
												</table>
											</center>
										</td>
									</tr>
								</table>
								</#if>
							</form>
						</td>
					</tr>
					<tr class="bd">
						<td class="btnBar"><a href="#" onClick="RunDemand();" class="btn"><span>Schedule</span></a>
						</td>
					</tr>
				</table>
			</div>
	</section>
</body>
</html>
