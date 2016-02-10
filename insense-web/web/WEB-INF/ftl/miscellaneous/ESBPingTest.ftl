  <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>

    <script src="js/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script type="text/javascript" src="js/formValidation.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
    <script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
<style type="text/css">
  .header {
  	font-family: inherit;
    font-weight: bold;
    line-height: 20px;
    color: inherit;
    text-rendering: optimizelegibility;
  }
</style>	
<script type="text/javascript">
var isChooseService = 1;
 	$(document).ready(function(){
 		if(isChooseService == 1) {
 			showAddService();
 		} else {
 			showChooseService();
 		}
 		$('#sendEmail').val("false");
 		$('#file').hide();
		$("#loginScriptButton").click(function() {
			$('#file').click();
		});
		document.getElementById("uploadSubmit").style.display='block';
		document.getElementById("uploadRefresh").style.display='block';
		var ref = document.getElementById("uploadSubmit").value;
		var add = document.getElementById("uploadRefresh").value;
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
 	
 	
 	function getScheduleType(){
 		if(document.getElementById('ondemand').checked) {
 			document.esbFileUploadForm.scheduleType.value='OnDemand';
 			$('#schDateTime').val('');
 			$('#schDateTime').attr('disabled', 'disabled');
 			//document.getElementById('datetimespan').style.display='none';
 		}
 		if(document.getElementById('scheduled').checked){
 			document.esbFileUploadForm.scheduleType.value='Scheduled';
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
</script>
<script type="text/javascript">
function validateInputValues(flag){
	document.getElementById("buttonType").value = flag;
	
	var fileupload = document.getElementById("file").value;
	 if(fileupload == ""){
		alert('Please Select file for Upload');
		return false;
	} else {
		submitPage();
	}
}

function submitPage(){
	document.getElementById("esbFileUploadForm").setAttribute("action","<@spring.url '/SaveESBPingTestDetails.ftl' />");
	document.getElementById("esbFileUploadForm").setAttribute('method',"POST");
	document.getElementById("esbFileUploadForm").submit();

}


function testPingOperation(){
	document.getElementById("esbFileUploadForm").setAttribute("action","<@spring.url '/SubmitESBPingTestDetails.ftl' />");
	document.getElementById("esbFileUploadForm").submit();
}

function submitEsbPingPage() {
	if(document.getElementById("environment").value == "-1"){
		alert('Please Select the environment');
		return false;
	} else if ( validateESBPingInput() ) {
		//Validate schedule Time, if schedule is selected.
		 if ($("#scheduled").is(":checked")) {
   			if( $("#schDateTime").val() == "" ){
   				alert('Please select schedule time.');
   				return false;
   			}
		}
		testPingOperation();
	} else {
		alert('Please select at least one Service.');
		return false;
	}
}

function validateESBPingInput(){
	var selected=0;
 	$(':checkbox').each(function() {
        if ( $(this).is('[id!=recurrence]') && $(this).prop('checked') ) {
        	selected = 1;
             return true;
          }                                    
    });
	
	if (  selected == 0 ) {
		return false;
	} else {
		return true;
	}
}
function CopyMe(oFileInput, sTargetID) {
	var arrTemp = oFileInput.value.split('\\');
	$("#fileUrlPath").val(arrTemp[arrTemp.length - 1]);
	$("#fileUrlPath").val(arrTemp[arrTemp.length - 1]);
}
function showAddService() {
	$("#uploadServices").show();
	$(".pingTestData").hide();
}
function showChooseService() {
	$("#uploadServices").hide();
	$(".pingTestData").show();
}
</script>

<body onLoad="window.scrollTo(0,readCookie('ypos'))">
<#if success?exists>
<div class="infoModule visible" id="tcId1">
		<div class="messageBodyContent"><span class="icon" title="Information"></span>${success}</div>
</div>
</#if><form modelAttribute="webserviceSetupForm" method="POST"  name="esbFileUploadForm" id="esbFileUploadForm"
								 enctype="multipart/form-data">

		
		
		<section class="mtlg pingTestData">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Choose Environment</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<input type="hidden" id="setupTabNumber" name="setupTabNumber" value="1">
								<table class="bd rowheight35">
									<br/>
									<tr class="lblFieldPair">
										<td class="lbl">Environment</td>
										<td class="input">
											<select id="environment" name="environment" onchange="getEnvironmentDetails();loadLoginUserDetails();loadApplicationModules();">
												<option value="-1" selected="true">----------Select-------------</option>
												<option value="PROD-A">PROD-A</option>
												<option value="PROD-B">PROD-B</option>
												<option value="DR">DR</option>
											</select><font color="red" size=-1>&nbsp;*</font>
										</td>
									</tr>
									
								</table>
						</td>
					</tr>
				</table>
			</div>
		</section>
		
		<section class="mtlg" id="uploadServices">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd header">
							Add Service<!-- <input style="float:right;" type="button" value="Back" onClick="showChooseService();"> -->
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<input type="hidden" name="scheduleType" >
								<input type="hidden" name="buttonType" id="buttonType" value="uploadSubmit" />
								<table width="60%">
									<br/>
									<tr class="lblFieldPair">
										<td class="lbl">Upload File</td>
										<td class="input">
												<input class="input_small" type="text" id="fileUrlPath" name="fileUrlPath" value=""> 
												<input type="file" id="file" onchange="CopyMe(this, 'txtFileName')" name="file[0]"> 
												<input type="button" class="uploadbutton" id="loginScriptButton" value="Upload">
										</td>
										<td>
										
											
										
										
										
										</td>
									</tr>
								</table>
						</td>
					</tr>
				</table>
				<table  width='100%'>
											
												<tr class="bd">
													<td class="btnBar" >
															<div style="float: right;">
														<a href="#" class="btn btnSmall" id="SubmitSelectedServices" onclick="validateInputValues('uploadSubmit')"><span>Add</span></a>
														&nbsp;&nbsp;<a href="#" class="btn btnSmall" id="SubmitSelectedServices" onclick="validateInputValues('uploadRefresh')"><span>Refresh</span></a>
												</div>
													</td>
												</tr>
												
											<tr>
											</tr>
				</table>	
			</div>
		</section>
	
		<section class="mtlg pingTestData" id="showServices">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd header">
							Choose Service
							<#if isAddServiceAccessAvailable>
							<input style="float:right;" type="button" value="Add Services" onClick="showAddService();">
							</#if>
							
							
						</td>
					</tr>
					<tr class="bd">
					<td style="padding:15">
						<table cellpadding='0' cellspacing='0' id = "servicesTobeAdded">
 	 						<tr>        	
						   		<td class="hd" style='width:400px;padding:5;' >
									<font face="verdana,arial" size=-1><b>Service</b>&nbsp;&nbsp;&nbsp;</font>
								</td> 		
   								<td class="hd" style="padding:5">
   									<input type="checkbox" name="select-all" id="select_all" onclick='selectAll(this)'> 
   								</td>
							</tr>
							<#list webservicesPingTestList! as service> 
							<script type="text/javascript">
								isChooseService = 2;
							</script>	
  							<tr class="bd">
								<td style='width:400px;padding:5;' >
							      	<div id="service" name="service"> 
												${service.serviceName}	
							 		</div>
								</td>
								<td style="padding:5">		
								<div id="checkboxColumn" name="checkboxColumn">
										<input type="checkbox" name="serviceNames" id="serviceNames" class="checkbox1" value='${service.serviceName}'/>
								</div>
								</td>
							</tr>
 							</#list>
 						
						</table>
						
				
					</td>
					</tr>
					
					</table>
				</div>
			</section>
			<section class="mtlg pingTestData">
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
							</table>
						</td>
					</tr>
				</table>
			</div>
		</section>
		
		
		
		<section class="mtlg pingTestData">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Send Email</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<input type="hidden" id="setupTabNumber" name="setupTabNumber" value="1">
								<table class="bd rowheight35">
									<br/>
									
									<!-- <tr class="lblFieldPair">
										<td class="lbl">Send Email</td>
										<td class="input">
												<input type="checkbox" id="sendEmail" name="sendEmail" value="false">
										</td>
									</tr>
									<tr class="lblFieldPair">
										<input type="hidden" value="${emailAddr!}" name="emailAddrTxt"/>
										<td class="lbl"></td>
										<td class="input">
												<input type="checkbox" class="emailTriggerOption" name="emailTriggerOption" id="emailTriggerOption" value="completion" onClick="getScheduleType()">&nbsp;&nbsp;Email on Completion
										</td>
									</tr>
									
									<tr class="lblFieldPair">
										<td class="lbl"></td>
										<td class="input">
											<input type="checkbox" class="emailTriggerOption" name="emailTriggerOption" id="emailTriggerOption" value="failure">&nbsp;&nbsp;Email on Failure
										</td>
									</tr> -->
									<tr class="lblFieldPair">
										<td class="lbl">Additional Email Recepients</td>
										<td class="input">
												<input style="width:350px;" maxlength="250" type="text" id="emailRecepients" name="emailRecepients"> (COMMA Seperated)
										</td>
									</tr>
								</table>
						</td>
					</tr>
				</table>
			</div>
		</section>
		<section class="mtlg pingTestData">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
				<tr class="bd">
				<td class="btnBar">
					<a href="#" onClick="submitEsbPingPage()"
											class="btn"><span>Test the Service</span></a> 
				</td>
				</tr>
			</table>
			</div>
		</section>
				
				
	</body>