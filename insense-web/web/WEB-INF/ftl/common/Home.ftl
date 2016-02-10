<#import "spring.ftl" as spring />
<#import "MacroTemplates.ftl" as mint />
<div id="Mint Home">
   <#include "header.ftl">
</div>
<head>
   <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
   <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
   
    <script src="js/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script type="text/javascript" src="js/formValidation.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
    <script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
 <script>
 $(document).ready(function(){	
	 getWSBaselineDates();
	 $(".tabLink").each(function(){
	      $(this).click(function(){
	        tabeId = $(this).attr('id');
	      
	        $(".tabLink").removeClass("activeLink");
	        $(this).addClass("activeLink");
	        return false;
	      });
	});
	$(".tabLink").removeClass("activeLink");
	$("#cont-${regressionTestExecutionForm.setupTabNumber!}").addClass("activeLink");
	$("#solutionType").val('${regressionTestExecutionForm.solutionType!}');
});
 $(document).ready(function(){
	
		$("#completed_div").show();
		$("#inprogress_div").hide();
		$("#text2").hide();
		$("#scheduled_div").hide();
		$("#text3").hide();
	
});

 $(document).ready(function(){
		 getScheduleStatus();

	
	 <#if regressionTestExecutionForm.allSched?exists && regressionTestExecutionForm.allSched == "true">
	 
			document.getElementById("allStatus").checked=true;
			document.getElementById("completedSchedule").checked=true;
			document.getElementById("currentSchedule").checked=true;
			document.getElementById("futureSchedule").checked=true;
			getScheduleStatus(true);
		<#else>
			<#if regressionTestExecutionForm.completedSched?exists && regressionTestExecutionForm.completedSched == "true" >
				document.getElementById("completedSchedule").checked=true;
				document.getElementById("allStatus").checked=false;
				getScheduleStatus(false);
			</#if>
			
			<#if regressionTestExecutionForm.currentSched?exists && regressionTestExecutionForm.currentSched == "true" >
				document.getElementById("currentSchedule").checked=true;
				document.getElementById("allStatus").checked=false;
				getScheduleStatus(false);
			</#if>
			
			<#if regressionTestExecutionForm.futureSched?exists && regressionTestExecutionForm.futureSched == "true" >
				document.getElementById("futureSchedule").checked=true;
				document.getElementById("allStatus").checked=false;
				getScheduleStatus(false);
			</#if>
			
		</#if>
		
	
});
//Status Div reloads every 10 seconds - starts 
 function reloadPage() {
	
 	 var suitId = document.getElementById("suitId").value;
 	    if(suitId >0 && $("#autoRefresh").prop("checked")){
 	    	refreshValuePage();
 	    }
 	
 }
  
 setInterval(reloadPage, 30000); 
 
 function getWSBaselineDates(){
		enableLoader();
		var wsSuiteId = '${regressionTestExecutionForm.webserviceSuiteId!}';
		
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
function listSuits(){
	
	$("#suitId").val("");
	$("#setupTabNumber").val("1");
	$(".suit").each(function() {
		  $( this ).prop('checked', false);
	});
 	document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/RetrieveSuits.ftl' />");
	document.getElementById("SuitForm").setAttribute('method',"POST");
	document.getElementById("SuitForm").submit(); 
	}

function listTestingType(){
	$(".suit").each(function() {
		  $( this ).prop('checked', false);
	});
	document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/RetrieveTestingType.ftl' />");
	document.getElementById("SuitForm").setAttribute('method',"POST");
	document.getElementById("SuitForm").submit();
}

function loadTabData(setupTabNumberVal,currentpage) {
	$('#currentPageNo').val(currentpage);
	
	var form = $(document.createElement('form'));
	//$('#suitId').attr('value', document.getElementById("selectedSuitId").value);
	$('body').append(form);
	$(form).attr("name", "UiScheduleTestcaseSetup");
	$(form).attr("modelAttribute", "RegressionTestExecutionForm");
	$(form).attr("action", "<@spring.url '/Home.ftl' />");
	$(form).attr("method", "POST");
	var input1 = $("<input>").attr("type", "hidden").attr("name", "setupTabNumber").val(setupTabNumberVal);
	var input2 = $("<input>").attr("type", "hidden").attr("name", "suitId").val(document.getElementById("suitId").value);
	var input3 = $("<input>").attr("type", "hidden").attr("name", "suitType").val(document.getElementById("suitType").value);
	var input4 = $("<input>").attr("type", "hidden").attr("name", "uiSuitsCategory").val(document.getElementById("uiSuitsCategory").value);
	var input5 = $("<input>").attr("type", "hidden").attr("name", "scheduleExecutionId").val($("#scheduleExecutionId").val());
	var input6 = $("<input>").attr("type", "hidden").attr("name", "currentPageNo").val($("#currentPageNo").val());
	$(form).append($(input1));
	$(form).append($(input2));
	$(form).append($(input3));
	$(form).append($(input4));
	$(form).append($(input5));
	$(form).append($(input6));
	$(form).submit();

}

function loadTabDataForWs(setupTabNumberVal,currentpage) {
	$('#currentPageNo').val(currentpage);
	
	var form = $(document.createElement('form'));
	//$('#suitId').attr('value', document.getElementById("selectedSuitId").value);
	$('body').append(form);
	$(form).attr("name", "UiScheduleTestcaseSetup");
	$(form).attr("modelAttribute", "RegressionTestExecutionForm");
	$(form).attr("action", "<@spring.url '/RetrieveTestingType.ftl' />");
	$(form).attr("method", "POST");
	var input1 = $("<input>").attr("type", "hidden").attr("name", "setupTabNumber").val(setupTabNumberVal);
	var input2 = $("<input>").attr("type", "hidden").attr("name", "webserviceSuiteId").val(document.getElementById("webserviceSuiteId").value);
	/* 	var input3 = $("<input>").attr("type", "hidden").attr("name", "suitType").val(document.getElementById("suitType").value);
	var input4 = $("<input>").attr("type", "hidden").attr("name", "uiSuitsCategory").val(document.getElementById("uiSuitsCategory").value); */
	var input5 = $("<input>").attr("type", "hidden").attr("name", "scheduleExecutionId").val($("#scheduleExecutionId").val());
	var input6 = $("<input>").attr("type", "hidden").attr("name", "solutionType").val("2");
	var input7 = $("<input>").attr("type", "hidden").attr("name", "currentPageNo").val($("#currentPageNo").val());
	$(form).append($(input1));
 	$(form).append($(input2));
 	/*$(form).append($(input3));
	$(form).append($(input4)); */
	$(form).append($(input5));
	$(form).append($(input6));
	$(form).append($(input7));
	$(form).submit();
	

}



	function checkSuitname(elem, suitId){
		var paginateCurrentPageNo = '${paginateCurrentPageNo!}';
		 if(paginateCurrentPageNo != null && paginateCurrentPageNo != ""){
		 	$('#currentPageNo').val(paginateCurrentPageNo);
		 }else{
			 $('#currentPageNo').val($('#pageLabeltcId0 span').text());	 
		 }
		
		$(".suit").each(function() {
			  $( this ).prop('checked', false);
		});
		$( elem ).prop('checked', true);
		if(elem.checked==true) {
			document.getElementById("suitId").value = suitId;
			$('#suitId').attr('value', suitId);
			$('#one').show();
			document.getElementById("selectedSuitId").value = suitId;
			getScheduleStatus();//loading Status Tab data
			if(document.getElementById("setupTabNumber").value == 2 || document.getElementById("setupTabNumber").value == 3){
				loadTabData(document.getElementById("setupTabNumber").value,$('#currentPageNo').val());//loading Schedule TestCase and Report Tab data
			}
				
		} 
	}
	
	function checkWsSuitname(elem, suitId){
		var paginateCurrentPageNo = '${paginateCurrentPageNo!}';
		 if(paginateCurrentPageNo != null && paginateCurrentPageNo != ""){
		 	$('#currentPageNo').val(paginateCurrentPageNo);
		 }else{
			 $('#currentPageNo').val($('#pageLabeltcId0 span').text());	 
		 }
		/* $(".suit").each(function() {
			  $( this ).prop('checked', false);
		});
		$( elem ).prop('checked', true); */
		if(elem.checked==true) {
		$('#one').show();
			//document.getElementById("webserviceSuiteId").value = suitId;
			$('#webserviceSuiteId').attr('value', suitId);
			document.getElementById("selectedSuitId").value = suitId
			loadTabDataForWs(5,$('#currentPageNo').val());
			/* if(document.getElementById("setupTabNumber").value == 5  || document.getElementById("setupTabNumber").value == 6){
				loadTabDataForWs(document.getElementById("setupTabNumber").value);//loading Schedule TestCase and Report Tab data
			
			} */

		} else {
			$('#one').hide();
		}
	}
function getSuitType(suittype){
	
	document.getElementById("suitType").value = suittype;
}
	
function isBetween(evt) {
	
    var value = document.getElementById('refreshtext').value;
    keyVal = String.fromCharCode(evt.keyCode);
    var x = value + "" +keyVal;
	if(x >= 1 && x <= 25){
		  return true;
	}else{
		  alert("Number should be between 1 to 25");
		  return false;
	}
}
function resetScheduleStatusType(){
	if(document.getElementById("allStatus")!=null)document.getElementById("allStatus").checked=false;
	if(document.getElementById("completedSchedule")!=null)document.getElementById("completedSchedule").checked=true;
	if(document.getElementById("currentSchedule")!=null)document.getElementById("currentSchedule").checked=false;
	if(document.getElementById("futureSchedule")!=null)document.getElementById("futureSchedule").checked=false;
}


</script>
    <script type="text/javascript">
    	var isScheduleDelete = 0;
    	$(document).ready(function(){
    		
    		var scheduledSuitType = '${scheduledSuitType!}';
    		if(scheduledSuitType != null && scheduledSuitType != "") {
    			$("#suitType").val(scheduledSuitType);
    		}
    	
    		var scheduledSuitId = '${scheduledSuitId!}';
    		
    		if(scheduledSuitId != null && scheduledSuitId != "") {
    			var elem = $("#"+scheduledSuitId);
    			checkSuitname(elem, scheduledSuitId);
    			$("#suitId").val(scheduledSuitId);
    			//$("#allStatus").prop('checked',true);
    			//getScheduleStatus(true);
    		}
    		var suitId = $("#suitId").val();
    		$("#"+suitId).prop('checked', true);
    	
    		var webserviceSuitId = '${regressionTestExecutionForm.webserviceSuiteId!}';
    	
	    	if(webserviceSuitId <= 0){
	    		webserviceSuitId = $("#webserviceSuiteId").val();
	    	} 
	    	$("#webserviceSuiteId").val(webserviceSuitId);
    		 $("#"+webserviceSuitId).prop('checked', true);    		
    		if(suitId > 0 || webserviceSuitId > 0) {
    			$('#one').show();
    		} else {
    			$('#one').hide();
    		}
    		
    		var checkScheduledStatus = '${checkScheduledStatus!}';
    		if(checkScheduledStatus != null && checkScheduledStatus != "" && checkScheduledStatus == "ON"){
    			document.getElementById("allStatus").checked=true;
    			document.getElementById("completedSchedule").checked=true;
    			document.getElementById("currentSchedule").checked=true;
    			document.getElementById("futureSchedule").checked=true;
    			getScheduleStatus(true);
    		}
    		
    		if(document.getElementById('scheduledTime')!=null)document.getElementById('scheduledTime').style.display='none';
    		if(document.getElementById('datetimepicker3')!=null)document.getElementById('datetimepicker3').style.display='none';
    		if(document.getElementById('recurrenceidlydweedat')!=null)document.getElementById('recurrenceidlydweedat').style.display='none';
    		if(document.getElementById('recurrencedat')!=null)document.getElementById('recurrencedat').style.display='none';
    		if(document.getElementById('recurrencewee')!=null)document.getElementById('recurrencewee').style.display='none';
    		if(document.getElementById('recurrencedat')!=null)document.getElementById('recurrencedat').style.display='none';
    		if(document.getElementById('recurrencedlyvalue')!=null)document.getElementById('recurrencedlyvalue').style.display='none';
    		if(document.getElementById('recurrenceweevalue')!=null)document.getElementById('recurrenceweevalue').style.display='none';
    		if(document.getElementById('recurrencedatvalue')!=null)document.getElementById('recurrencedatvalue').style.display='none';
    		
    		$(".weeklyCheckBox").prop("checked",false);
    		$(".weeklyCheckBox").attr("disabled",true);
    		
    		if(document.getElementById('scheduledDate')!=null)document.getElementById("scheduledDate").disabled = true;
    		if(document.getElementById('recurrence')!=null){
    			document.getElementById("recurrence").checked=false;
    			document.getElementById("recurrence").disabled = true;
    		}
    		
    		if(document.getElementById('recurrenceiddate')!=null)document.getElementById("recurrenceiddate").checked=true;
    		if(document.getElementById('scheduledDate')!=null){
    			document.getElementById("scheduledDate").disabled = false;
    			document.getElementById("scheduledDate").value="";
    		}
    		
    		if(document.getElementById('stopAfter')!=null)document.getElementById("stopAfter").value="";
    		
    		$("#completionDateRow").show();
    		$("#crawlStatusRow").hide();
    		
    		$("#startDateTimeRow").show();
    		$("#lastUpdatedtimeRow").show();
    		$("#processedUrlRow").show();
    		$("#pendingUrlCountRow").show();
    		
    	
    		$("#reportsStartTimeRow").show();
    		$("#reportsEndTimeRow").show();
    		$("#comparisonStartTimeRow").show();
    		$("#recurrenceRow").show();
    		$("#deleteRow").show();
    		
	     });

function getReccurenceType(){
	if(document.getElementById('recurrenceidweekly').checked){
		document.aform.executeBy.value='recurrenceWeekly';
		document.getElementById("recurrence").checked=false;
		document.getElementById("recurrence").disabled = false;		
		$(".weeklyCheckBox").prop("checked",false);
		$(".weeklyCheckBox").attr("disabled",false);
		document.getElementById("scheduledDate").value="";
		document.getElementById("scheduledDate").disabled = true;		
	}else if(document.getElementById('recurrenceiddate').checked){
		document.aform.executeBy.value='recurrenceDate';
		document.getElementById("recurrence").checked=false;
		document.getElementById("recurrence").disabled = true;
		$(".weeklyCheckBox").prop("checked",false);
		$(".weeklyCheckBox").attr("disabled",true);
		document.getElementById("scheduledDate").value="";
		document.getElementById("scheduledDate").disabled = false;
	}
}  	

function getScheduleType(){
		if(document.getElementById('ondemand1').checked) {
			document.aform.scheduleType.value='OnDemand';
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
			document.aform.scheduleType.value='Scheduled';
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

function getScheduleStatus(isAll) {
	disableLoader();
	$(".statusTables").hide();
	if(isAll) {
		if($("#allStatus").prop('checked')) {
			getCompletedSuitScheduleStatus();
			getInProgressSuitScheduleStatus();
			getFutureSuitScheduleStatus();
			$(".statusTables").show();
		    $(".scheduleTime").prop('checked',true);
		} else {
			 $(".scheduleTime").prop('checked',false);
		}
	} else {
		$("#allStatus").prop('checked',false);
		if($("#completedSchedule").prop('checked')){
				getCompletedSuitScheduleStatus();
				$("#completed_div").show();
				$("#text1").show();
		} 
		if($("#currentSchedule").prop('checked')){
				getInProgressSuitScheduleStatus();
				$("#inprogress_div").show();
				$("#text2").show();
		} 
		if($("#futureSchedule").prop('checked')){
				getFutureSuitScheduleStatus();
				$("#scheduled_div").show();
				$("#text3").show();
		}
	}
	 $('#refreshBtn').prop('disabled',false);
	var isChekced = 0;
	$(".scheduleTime").each(function() {
	if($( this ).prop('checked')) {
		isChekced = 1;
	}
	});
	if(isChekced == 0) {
	 $('#refreshBtn').prop('disabled',true);
	}
}

function getCompletedSuitScheduleStatus(){
	enableLoader();
	var suitId =  document.getElementById("suitId").value;
	var refreshtext = document.getElementById("refreshtext").value;
	var suitType =  document.getElementById("suitType").value;
	if ( suitId == '' || suitType == '') {
		return;
	}
	
	if ( refreshtext == '' ){
		refreshtext = 10;
	}
	
	var request = $.ajax({  url: 
		"getCompletedSuitScheduleStatus.ftl",
		data: { suitId : suitId, refreshtext:refreshtext },
		type: "POST", 
		cache: false,
		dataType: "text"});
		
		request.done(function( scheduleList ) {
		disableLoader();
		var innerHtml = completedTableHeader();
		if ($.parseJSON(scheduleList) != ''){
  			$.each($.parseJSON(scheduleList), function(idx, schedule) {
     			innerHtml = innerHtml + '<tbody><tr data-sortfiltered="false">';
				innerHtml = innerHtml + '<td class="row">' + schedule.scheduleExecutionId + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.crawlStartTime + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.crawlEndTime + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.executionStatus + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.processedUrlCount + '</td>';
				innerHtml = innerHtml + '<td class="row">';
				
				if ( schedule.transactionTesting ){
					innerHtml = innerHtml + '<a href="#" class="editLink np" title="Edit the application" onClick="showTransactionSummaryPopUp(' + schedule.scheduleExecutionId + ')">'; 
					innerHtml = innerHtml + '<span class="icon nmt plxs"></span>Reports summary</a>';
				}
				else if ( (suitType == 'Functional' || suitType == 'Analytics' || suitType == 'Broken') && schedule.executionStatus == 'Completed' ){
					innerHtml = innerHtml + '<a href="#" class="editLink np" title="Edit the application" onClick="showCompletionSummaryPopUp(' + schedule.scheduleExecutionId + ')">'; 
					innerHtml = innerHtml + '<span class="icon nmt plxs"></span>Reports summary</a>';
				} else{
					innerHtml = innerHtml + 'Not Applicable';
				}
				innerHtml = innerHtml + '</td>';
				innerHtml = innerHtml + '<td class="row">';
				
				if ( schedule.executionStatus == 'Completed' ){
					innerHtml = innerHtml + '<a href="#" class="editLink np" title="Edit the application" onClick="showExecutionSummaryPopUp(' + schedule.scheduleExecutionId + ')">'; 
					innerHtml = innerHtml + '<span class="icon nmt plxs"></span>Execution summary</a>';
				} else{
					innerHtml = innerHtml + 'Not Available';
				}
				innerHtml = innerHtml + '</td>';
				
				if ( schedule.compareConfig.compareProcessedUrlCount > 0 ){
					innerHtml = innerHtml + '<td class="row">';
					innerHtml = innerHtml + '<a href="#" class="editLink np" title="View Reports" onClick="viewReports(';
					innerHtml = innerHtml + schedule.scheduleExecutionId +')"><span class="icon nmt plxs" ></span>View Reports</a>';
					innerHtml = innerHtml + '</td>';
				}else{
					innerHtml = innerHtml + '<td class="row">' + 'N/A' + '</td>';
				}
				
				if ( schedule.analyticsReportAvailable ){
					innerHtml = innerHtml + '<td class="row">';
					innerHtml = innerHtml + '<a href="#" class="editLink np" title="View Reports" onClick="showReportLinks(';
					innerHtml = innerHtml + schedule.scheduleExecutionId +')"><span class="icon nmt plxs" ></span>Click to View</a>'; 
					innerHtml = innerHtml + '</td>';
				}else{
					innerHtml = innerHtml + '<td class="row">' + 'N/A' + '</td>';
				}
				
				innerHtml = innerHtml + '<td class="row">' + schedule.scheduledBy + '</td> </tr></tbody>';
 			});
		}else{
			innerHtml = innerHtml + "<tbody><tr><td colspan='9'><font face='verdana,arial' size='-1' >No Records Found.</font></td></tr></tbody>";
		}
 			$('#completed_table').empty().append(innerHtml);
		}); 
	 	request.fail(
	  		function( jqXHR, textStatus){
	  			disableLoader();
	  		}); 
}

function showReportLinks(scheduleExecutionId){
	$("#scheduleExecutionId").val(scheduleExecutionId);
	document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/viewAnalyticsSummaryReport.ftl' />");
	document.getElementById("SuitForm").setAttribute('method',"POST");
	document.getElementById("SuitForm").submit();
}

function getInProgressSuitScheduleStatus() {
	enableLoader();
	var suitId =  document.getElementById("suitId").value;
	var refreshtext = document.getElementById("refreshtext").value;
	
	if ( suitId == '') {
		return;
	}
	
	if ( refreshtext == '' ){
		refreshtext = 10;
	}
	
	var request = $.ajax({  url: 
		"getInProgressSuitScheduleStatus.ftl",
		data: { suitId : suitId, refreshtext:refreshtext },
		type: "POST", 
		cache: false,
		dataType: "text"});
		
		request.done(function( scheduleList ) {
		disableLoader();
		var innerHtml = inProgressTableHeader();
		if ($.parseJSON(scheduleList) != ''){
  			$.each($.parseJSON(scheduleList), function(idx, schedule) {
     			innerHtml = innerHtml + '<tbody><tr data-sortfiltered="false">';
				innerHtml = innerHtml + '<td class="row">' + schedule.scheduleExecutionId + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.crawlStartTime + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.lastUpdatedTime + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.processedUrlCount + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.pendingUrlCount + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.reportsStartTime + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.reportsEndTime + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.comparisonStartTime + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.comparisonProcessedUrlCount + '</td>';
				
				if ( schedule.compareConfig.compareProcessedUrlCount > 0 ){
					innerHtml = innerHtml + '<td class="row">';
					innerHtml = innerHtml + '<a href="#" class="editLink np" title="View Reports" onClick="viewReports(';
					innerHtml = innerHtml + schedule.scheduleExecutionId +')"><span class="icon nmt plxs" ></span>View Reports</a>';
					innerHtml = innerHtml + '</td>';
				}else{
					innerHtml = innerHtml + '<td class="row">' + 'N/A' + '</td>';
				}
				innerHtml = innerHtml + '<td class="row">' + schedule.scheduledBy + '</td> </tr></tbody>';
 			});
		}else{
			innerHtml = innerHtml + "<tbody><tr><td colspan='11'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr></tbody>";
		}
 			$('#inprogress_table').empty().append(innerHtml);
		}); 
	 	request.fail(
	  		function( jqXHR, textStatus ){  
	  			disableLoader();
	  		}); 
}
function getFutureSuitScheduleStatus(){
	enableLoader();
	var suitId =  document.getElementById("suitId").value;
	var refreshtext = document.getElementById("refreshtext").value;
	if ( suitId == '') {
		return;
	}
	
	if ( refreshtext == '' ){
		refreshtext = 10;
	}
	var request = $.ajax({  url: 
		"GetFutureSuitScheduleStatus.ftl",
		data: { suitId : suitId, refreshtext:refreshtext },
		type: "POST", 
		cache: false,
		dataType: "text"});
		
		request.done(function( scheduleList ) {
		disableLoader();
		var innerHtml = futureTableHeader();
		if ($.parseJSON(scheduleList) != ''){
  			$.each($.parseJSON(scheduleList), function(idx, schedule) {
     			innerHtml = innerHtml + '<tbody><tr data-sortfiltered="false">';
				innerHtml = innerHtml + '<td class="row">' + schedule.scheduleId + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.scheduledBy + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.startTime + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.scheduledDate + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.scheduledDay + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.reccurrence + '</td>';
				innerHtml = innerHtml + '<td class="row">' + schedule.pendingForThisWeek + '</td>';
				if(isScheduleDelete == 1) {
					innerHtml = innerHtml + "<td class='row'><a title='Remove the application' class='deleteIcon np' id="+schedule.scheduleExecutionId+" name="+schedule.scheduledDay+" href='#' onclick='deleteFutureSchedule(this);'><span class='icon nmt plxs'></span>Remove</a></td></tr></tbody>";
				} else {
					innerHtml = innerHtml + "<td class='row'><span class='icon nmt plxs'></span><font face='verdana,arial' size='-1' color='red'>No Records Found</font></td></tr></tbody>";
				}
 			});
		}else{
			innerHtml = innerHtml + "<tbody><tr><td colspan='8'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr></tbody>";
		}
 			$('#scheduled_table').empty().append(innerHtml);
		}); 
	 	request.fail(
	  		function( jqXHR, textStatus ){
	  			disableLoader();
	  		}); 
}
function viewReports(scheduleExecutionId){
	if ( scheduleExecutionId == ''){
		return false;
	}else{
		document.SuitForm.scheduleExecutionId.value=scheduleExecutionId;
		showReportsScreen();
	}
}

function showReportsScreen(){
	document.getElementById("allSched").value="false";
	document.getElementById("completedSched").value="false";
	document.getElementById("currentSched").value="false";
	document.getElementById("futureSched").value="false";
	
	
	if ($("#allStatus").prop('checked')) {
		document.getElementById("allSched").value="true";
		document.getElementById("completedSched").value="true";
		document.getElementById("currentSched").value="true";
		document.getElementById("futureSched").value="true";
	} else {
		
		if ($("#completedSchedule").prop('checked')){
			document.getElementById("completedSched").value="true";
		}
		if ($("#currentSchedule").prop('checked')){
			document.getElementById("currentSched").value="true";
		}
		if ($("#futureSchedule").prop('checked')){
			document.getElementById("futureSched").value="true";
		}
	}
	
	document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/UiViewReports.ftl' />");
	document.getElementById("SuitForm").setAttribute('method',"POST");
	document.getElementById("SuitForm").submit();
}


function deleteFutureSchedule(elem) {
		if(elem.name == null || elem.name == "") {
			elem.name = "NA";
		} 
		 var request = $.ajax({  url: 
		"DeleteFutureSchedule.ftl",
		data: {scheduleId : elem.id,scheduleDay : elem.name},
		type: "POST", 
		cache: false,
		dataType: "text"});
	
		request.done(function( msg ) {
			getFutureSuitScheduleStatus();
			if(msg == 'Success') {
				alert("Schedule reomoved successfully");
			} else {
				alert("Unable to remove schedule");
			}
		}); 
		
	 	request.fail(
	  		function( jqXHR, textStatus ){  
	  			alert( "Unable to delete the schedule");
	  		});
}

		
function inProgressTableHeader(){
	var innerHtml = '<thead><tr><th id="deleteRow" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Schedule Id</th>';
	innerHtml = innerHtml + '<th id="startDateTimeRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Start date Time</th>';
	innerHtml = innerHtml + '<th id="lastUpdatedtimeRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Last Updated time</th>';
	innerHtml = innerHtml + '<th id="processedUrlRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Processed Url count</th>';
	innerHtml = innerHtml + '<th id="processedUrlRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Pending Url Count</th>';
	innerHtml = innerHtml + '<th id="reportsStartTimeRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Reports Start Time</th>';
	innerHtml = innerHtml + '<th id="reportsEndTimeRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Reports End Time</th>';
	innerHtml = innerHtml + '<th id="comparisonStartTimeRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Comparison Start Time</th>';
	innerHtml = innerHtml + '<th id="recurrenceRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Comparison Processed Url Count</th>';
	innerHtml = innerHtml + '<th id="crawlStatusRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">View Reprots</th>';
	innerHtml = innerHtml + '<th id="deleteRow" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Scheduled By</th></tr></thead>';
	
	return innerHtml;
}

function completedTableHeader(){
	var innerHtml = '<thead><tr><th id="scheduleIdRow" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Schedule Id</th>';
	innerHtml = innerHtml + '<th id="startDateTimeRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Crawl Start Time</th>';
	innerHtml = innerHtml + '<th id="completionDateRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Crawl End Time</th>';
	innerHtml = innerHtml + '<th id="completionDateRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Execution Status</th>';
	innerHtml = innerHtml + '<th id="processedUrlRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Total Url Count</th>';
	innerHtml = innerHtml + '<th id="reportsStartTimeRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Reports Summary</th>';
	innerHtml = innerHtml + '<th id="reportsEndTimeRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Execution Summary</th>';
	innerHtml = innerHtml + '<th id="recurrenceRow" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">View Reports</th>';
	innerHtml = innerHtml + '<th id="viewAnalyticsReport" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">View Analytics Report</th>';
	innerHtml = innerHtml + '<th id="scheduledByRow" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Scheduled By</th></tr></thead>';
	return innerHtml;
}

function futureTableHeader(){
	var innerHtml = '<thead><tr><th id="deleteRow" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Schedule Id</th>';
	innerHtml = innerHtml + '<th class="txtl header w5" scope="col"  tabindex="0" data-sortpath="none">Scheduled By</th>';
	innerHtml = innerHtml + '<th class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Start Time</th>';
	innerHtml = innerHtml + '<th class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Scheduled Date</th>';
	innerHtml = innerHtml + '<th class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Scheduled Day</th>';
	innerHtml = innerHtml + '<th class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Reccurence</th>';
	innerHtml = innerHtml + '<th class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Weekly Run Status</th>';
	innerHtml = innerHtml + '<th class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Remove</th></tr></thead>';
	return innerHtml;
}

function getBaselineDates(suitId){
	var request = $.ajax({  url: 
		"getBaselineDates.ftl",  
		type: "POST",  
		data: { TestSuitId : suitId }, 
		async: false, 
		dataType: "text"}); 
		request.done(function( msg ) {
 			if(msg==""){
			 	document.getElementById("executedDates").value="";
				document.getElementById("executedDates").disabled=true;
			}else{
		 	    document.getElementById("executedDates").disabled=false;
		 	    $( "#executedDates" ).html( msg ); 
		 	}	
 		 }); 
	  request.fail(
	  function( jqXHR, textStatus ) 
	  {  
		  
	  }); 
}



function getDetailsForSchedule(dateCheckboxId){
	document.getElementById("scheduleId").value = document.getElementById(dateCheckboxId).value;
	document.getElementById('cont-2').click();
	//return checkProcessPendingDet();
}

function getScheduleDates(elem,saveId){
	var suitName = elem.id;
	var saveid = saveId;
	var request = $.ajax({  url: 
		"getScheduleDates.ftl",  
		type: "POST",  
		data: { TestSuitId : saveId }, 
		async: false, 
		dataType: "text"}); 
			request.done(function( msg ) {
	 			if(msg==""){
	 				$(".schDt").each(function() {
	 					$( this ).empty();    
	 					$( this ).prop('disabled', true);
					});
				}else{
					$(".schDt").each(function() {
						  $( this ).empty();
						  $( this ).prop('disabled', true);
					});
					document.getElementById(saveid).disabled=false;
					document.getElementById(saveid).innerHTML=msg;
			 	}	
	 		 }); 
	  request.fail(
	  function( jqXHR, textStatus ) 
	  {  
		  
	  }); 
}

function validateInputValues(){
	
}

function checkProcessPendingDet(){
	if( !isEmpty(document.getElementById("TestSuitName"))){
	var CheckSuitName = document.getElementById("TestSuitName").value;
	var CheckSuitId = document.getElementById("TestSuitId").value;
	
	var scheduleId = document.getElementById("scheduleId").value;
	
	if ( null == scheduleId || '' == scheduleId ){
		return false;
	}
	var request = $.ajax({  url: 
		"getProcessPendingDetails.ftl",  
		type: "POST",  
		data: {testScheduleId : scheduleId }, 
		async: false, 
		dataType: "text"}); 
			request.done(function( msg ) {
				 $( "#cont-2-1" ).html( msg ); 
	 		 }); 
	  request.fail(
	  function( jqXHR, textStatus ) 
	  {  
		  
	  });
	}
}

function editSuitDetails(suitId,editOrViewMode,testingType) {

		document.SuitForm.suitId.value = suitId;
		document.SuitForm.editOrViewMode.value = editOrViewMode;
		document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/ChoosingOnEditSuitDetails.ftl' />");
		document.getElementById("SuitForm").setAttribute('method',"POST");
		document.getElementById("SuitForm").submit();	
}

function editWsSuitDetails(webserviceSuiteId,editOrViewMode,testingType) {
	document.SuitForm.webserviceSuiteId.value = webserviceSuiteId;
	document.SuitForm.editOrViewMode.value = editOrViewMode;
	document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/ChoosingEditWebserviceSuitDetails.ftl' />");
	document.getElementById("SuitForm").setAttribute('method',"POST");
	document.getElementById("SuitForm").submit();	
}

function CreateWsSuit() {
	document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/CreateWebserviceSuit.ftl' />");
	document.getElementById("SuitForm").setAttribute('method',"POST");
	document.getElementById("SuitForm").submit();	
}

function removeSuite(suitId) {
	
	if(	confirm("Please Confirm to remove this suit")){
		document.SuitForm.suitId.value = suitId;
		document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/removeSuitDetails.ftl' />");
		document.getElementById("SuitForm").setAttribute('method',"POST");
		document.getElementById("SuitForm").submit();
  	}
}
function removeWsSuite(webserviceSuiteId) {
	
	if(	confirm("Please Confirm to remove this webservice suit")){
		document.SuitForm.webserviceSuiteId.value = webserviceSuiteId;
	
		document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/removeWsSuitDetails.ftl' />");
		document.getElementById("SuitForm").setAttribute('method',"POST");
		document.getElementById("SuitForm").submit();
  	}
}

function refreshValue(){
	window.location.reload(true);
}

function isNumberKey(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))
       return false;

    return true;
 }

function showSuitTypePage(){
	document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/UiChooseSuitType.ftl' />");
	document.getElementById("SuitForm").setAttribute('method',"POST");
	document.getElementById("SuitForm").submit();
}

function showMonitorJob(){
	document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/UiMonitorJob.ftl' />");
	document.getElementById("SuitForm").setAttribute('method',"POST");
	document.getElementById("SuitForm").submit();
}

function showManageSuitTypePage(){
	document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/UiManageSuitType.ftl' />");
	document.getElementById("SuitForm").setAttribute('method',"POST");
	document.getElementById("SuitForm").submit();
}

function ValidateViewReports(){
	$("#summarydiv").show();
	var reportDates=document.viewReportsForm.reportDates.value;

	if(reportDates==""){
	alert('Please Select Report Dates');
	return false;
	}
	
	document.getElementById("deleteApplicationId").action="<@spring.url "/RetrieveViewReports.ftl" />";
	document.getElementById("deleteApplicationId").submit();
}

function viewReportsTab(){
	$('#cont-4-1').show();
	$("#summarydiv").hide();
}
$(document).ready(function() {
	disableLoader();
	
});
</script>
</head>
  
 <body>
<div id=commonDv">

<div class="content twoCol" style="width:80%;">
<form modelAttribute="RegressionTestExecutionForm"  id="SuitForm" name="SuitForm" target="_top" method="POST" >
<input type="hidden" id="selectedSuitId"  name="selectedSuitId">
<input type="hidden" id="scheduleExecutionId" name="scheduleExecutionId" value="${regressionTestExecutionForm.scheduleExecutionId!}">
<input type="hidden" id="scheduleAction"  name="scheduleAction" value="0">
<input type="hidden" id="editOrViewMode"  name="editOrViewMode">
<input type="hidden" id="suitId"  name="suitId" value="${regressionTestExecutionForm.suitId!}">
<input type="hidden" id="webserviceSuiteId"  name="webserviceSuiteId" value="${regressionTestExecutionForm.suitId!}">
<input type="hidden" id="setupTabNumber"  name="setupTabNumber" value="${regressionTestExecutionForm.setupTabNumber!}">
<input type="hidden" id="allSched"  name="allSched"  value="${regressionTestExecutionForm.allSched!}">
<input type="hidden" id="completedSched"  name="completedSched" value="${regressionTestExecutionForm.completedSched!}">
<input type="hidden" id="currentSched"  name="currentSched" value="${regressionTestExecutionForm.currentSched!}">
<input type="hidden" id="futureSched"  name="futureSched" value="${regressionTestExecutionForm.futureSched!}">
<input type="hidden" id="suitType"  name="suitType" value="${regressionTestExecutionForm.suitType!}">
<input type="hidden" id="currentPageNo"  name="currentPageNo" value="${regressionTestExecutionForm.currentPageNo!}">

	<div class="page-content" id="pagecontent" role="main" tabindex="-1">
		<#if Success?exists>
			<div class="infoModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${Success}
				</div>
			</div>
			</#if> 
			<#if error?exists>
			<div class="alertModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${error}
				</div>
			</div>
		</#if>
		<hr style="margin-top: 10px;">
		<div class="mblg">
			<div class="mtxs">
		 		<a id="tcId100" class="tipLink rel" name="tcId100" >Help</a><b><@spring.message "mint.help.home"/></b><a href="<@spring.message 'mint.help.mailTo'/>" target="_top">
				<@spring.message "mint.help.mailId"/></a>
			</div>
		</div>
		<hr style="margin-top: 0px;">
			<table cellspacing="0" cellpadding="0" width="100%" >
				<tr class="bd">
					<td valign="middle" style="padding:10;width:10%;font-weight: bold;">Show Suits :</td>
					<td class="input" style="width:15%;">
						<select id="uiSuitsCategory" name="uiSuitsCategory" onchange = "listSuits()" >
						<#list uiSuitsCategoryList! as uiSuitsCategory>
							<#if model.selectedUiSuitCategory == uiSuitsCategory>
								<option value="${uiSuitsCategory}" selected>${uiSuitsCategory!}</option>
							<#else>
								<option value="${uiSuitsCategory}">${uiSuitsCategory!}</option>
							</#if>
						</#list>
						</select>
					</td>
			
					<td valign="middle" style="padding:10;width:13%;font-weight: bold;">&nbsp;&nbsp;Testing Type :</td>
					<td class="input" style="width:15%;">
						<select id="solutionType" name="solutionType" onchange = "listTestingType()">
							<#list solutionTypeList! as solutionType>
								<option value="${solutionType.solutionTypeId}">${solutionType.solutionTypeName!}</option>
							</#list>
						</select>
					</td>
					<td style="width:55%;" align="right">
					<#if model.solutionType == 1>
						<#if model.isManageSuitAccessAvailable>
							<script type="text/javascript">
	    						isScheduleDelete = 1;
	    					</script>	
							<input type="button" style="vertical-align: top;" onclick="showManageSuitTypePage()" value="Manage Suits"/>
						</#if>
						<#if model.isMonitorJobAccessAvailable>
							<input type="button" style="vertical-align: top;" onclick="showMonitorJob()" value="View Job Queue"/>
						</#if>
						<input type="button" style="vertical-align: top;" onclick="showSuitTypePage()" value="Create Suit"/>
						<#else>
						<input type="button" style="vertical-align: top;" onclick="CreateWsSuit()" value="Create Webservice Suit"/>
					</#if>
					
					</td>
					
				</tr>
			</table>
			
			<hr>
		</div>
		<div class="content" style="display: block;width:100%;" tabindex="0" id="mint_home_suits_table_content">
  			<div class="bd">
  			<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
				<table id="mint_home_suits_table" class="styleA fullwidth sfhtTable" summary="">
					<caption></caption>
					<thead>
						<tr>
							<th class="txtl header w10">Select Suit Name</th>
							<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype1" tabindex="0" data-sortpath="none">Test Suit Name</th>
							<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype2" tabindex="0" data-sortpath="none">Application Name</th>
							<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype3" tabindex="0" data-sortpath="none">Environment Category</th>
							<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype4" tabindex="0" data-sortpath="none">Created By Group</th>
							<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype5" tabindex="0" data-sortpath="none">Type</th>
							<th class="txtl header w10">Edit / View </th>
							<th class="txtl header w10">Remove</th>
						</tr>
					</thead>
					<tbody>
					<#if model.regressionTestSuit?has_content>
						<#list model.regressionTestSuit! as suit>
							<tr data-sortfiltered="false">
								<td scope="row">
								<input class="suit" type="checkbox" name="suitname" id="${suit.suitId!}"
									onClick="resetScheduleStatusType();getSuitType('${suit.type!}');checkSuitname(this,${suit.suitId!});">
								</td>
								<#if suit.privateSuit>
									<td scope="row" data-sortvalue="${suit.suitName!}">${suit.suitName!} (Private)</td>
								<#else>
									<td scope="row" data-sortvalue="${suit.suitName!}">${suit.suitName!}</td>
								</#if>
								
								<#assign applicationNameForSuit="">
								<#list applicationList! as application>
									<#if application.application.applicationId ==suit.applicationId>
										<#assign applicationNameForSuit="${application.application.applicationName!}">
									</#if>
								</#list>
								
								<td scope="row" data-sortvalue="${applicationNameForSuit!}">
									${applicationNameForSuit!}  
								</td>
								
								<#assign environmentCategoryForSuit="">
								<#list environmentCategoryList! as environmentCategory>
									<#if environmentCategory.environmentCategoryId == suit.environmentCategoryId>
										<#assign environmentCategoryForSuit="${environmentCategory.environmentCategoryName!}">
									</#if>
								</#list>
								<td scope="row" data-sortvalue="${environmentCategoryForSuit!}">
									${environmentCategoryForSuit!}
								</td>
								<td scope="row" data-sortvalue="${suit.users.group.groupName!}">
									${suit.users.group.groupName!}
								</td>
								
								<#if suit.transactionTestcase> 
									<td scope="row" data-sortvalue="${suit.type!}"> ${suit.type!} (Transactional) </td>
								<#else>
									<td scope="row" data-sortvalue="${suit.type!}"> ${suit.type!} </td>
								</#if>
								<td class="row">
									<#if currentUserId == suit.userId || GroupAdminAccess == "YES">
										<a href="#" class="editLink np" title="Edit the application" onClick="editSuitDetails(${suit.suitId!},'EditMode','Regression')"><span class="icon nmt plxs" ></span>Edit</a>
									<#else>	
										<a href="#" class="editLink np" title="Edit the application" onClick="editSuitDetails(${suit.suitId!},'ViewMode','Regression')"><span class="icon nmt plxs" ></span>View</a>
									</#if>	
								</td>
								<td class="row">
									<a title="Delete Suit" class="deleteIcon np" href="#" onClick="removeSuite(${suit.suitId!},'Regression')"><span class="icon nmt plxs" ></span>Remove</a>
								</td>
							</tr>
						</#list>
					<#if model.regressionTestSuitV4?? &&  model.regressionTestSuitV4?has_content>
						<#list model.regressionTestSuitV4! as suitDetails>
							<tr data-sortfiltered="false">
								<td scope="row">
								<input class="suit" type="checkbox" name="suitname" id="${suitDetails.testSuit.suitId!}"
									onClick="resetScheduleStatusType();getSuitType('${suitDetails.suitType!}');checkSuitname(this,${suitDetails.testSuit.suitId!});">
								</td>
								<td scope="row" data-sortvalue="${suitDetails.testSuit.suitName!}">${suitDetails.testSuit.suitName!}</td>
										
								<td scope="row" data-sortvalue="${suitDetails.testSuit.applicationName!}">
									${suitDetails.testSuit.applicationName!}
								</td>
								
								<td scope="row" data-sortvalue="${suitDetails.testEnvironment.environmentName!}">
									${suitDetails.testEnvironment.environmentName!}
								</td>
								<td scope="row" data-sortvalue="v4">  </td>
								<td scope="row" data-sortvalue="v4"> ${suitDetails.suitType!} </td>
								<td class="row">
										<a href="#" class="editLink np" title="Edit the application" onClick="editv4SuitDetails(${suitDetails.testSuit.suitId!},'ViewMode','Regression')"><span class="icon nmt plxs" ></span>View</a>
								</td>
								<td class="row">
									<a title="Delete Suit" class="deleteIcon np" href="#" onClick="removeV4Suite(${suitDetails.testSuit.suitId!},'Regression')"><span class="icon nmt plxs" ></span>Remove</a>
								</td>
							</tr>
						</#list>
					</#if>
					<#elseif model.webserviceSuits?has_content>
						<#list model.webserviceSuits! as suit>
							<tr data-sortfiltered="false">
								<td scope="row">
								<input class="suit" type="checkbox" name="suitname" id="${suit.webserviceSuiteId!}"
									onClick="getSuitType('Webservice');checkWsSuitname(this,${suit.webserviceSuiteId!});">
								</td>
								<td scope="row" data-sortvalue="${suit.suitName!}">${suit.webserviceSuiteName!}</td>
							
								
								<td scope="row" data-sortvalue="">
									NA
								</td>
								
								<#assign environmentCategoryForSuit="">
								<#list environmentCategoryList! as environmentCategory>
									<#if environmentCategory.environmentCategoryId == suit.environmentId>
										<#assign environmentCategoryForSuit="${environmentCategory.environmentCategoryName!}">
									</#if>
								</#list>
								<td scope="row" data-sortvalue="${environmentCategoryForSuit!}">
									${environmentCategoryForSuit!}
								</td>
								<td scope="row" data-sortvalue="${suit.users.group.groupName!}">
									${suit.users.group.groupName!}
								</td>
								
								<td scope="row" data-sortvalue="Webservice">
									WEBSERVICE
								</td>
								
								
								<td class="row">
									<#if currentUserId == suit.userId || GroupAdminAccess == "YES">
										<a href="#" class="editLink np" title="Edit the application" onClick="editWsSuitDetails(${suit.webserviceSuiteId!},'EditMode','Webservice')"><span class="icon nmt plxs" ></span>Edit</a>
									<#else>	
										<a href="#" class="editLink np" title="Edit the application" onClick="editWsSuitDetails(${suit.webserviceSuiteId!},'ViewMode','Webservice')"><span class="icon nmt plxs" ></span>View</a>
									</#if>	
								</td>
								
								<td class="row">
									<a title="Delete Suit" class="deleteIcon np" href="#" onClick="removeWsSuite(${suit.webserviceSuiteId!}, 'Webservice')"><span class="icon nmt plxs" ></span>Remove</a>
								</td>
							</tr>
						</#list>
					<#else>
						<tr><td colspan='8'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
					</#if>
					</tbody>
				</table>
			</div>
			<@mint.tablesort id="#mint_home_suits_table" casesensitive="false" jsvar="mint_home_suits_table__js"/>
			<@mint.paginate table="#mint_home_suits_table" style="new" autocount="true" perpage="10" startpage='${regressionTestExecutionForm.currentPageNo!}' selectpage="false" showperpagedropdown="true"/>
			
			</div>
		</div>
		</form>
		<div id="one" align="left">
			<div class="tab-box">
				<#if regressionTestExecutionForm.solutionType == 1>
				    <span>
				    	<a href="javascript:;" class="tabLink activeLink" id="cont-1"  onClick="loadTabData(1,$('#pageLabeltcId0 span').text());">Schedule Status</a>
				    </span> 
					<span>
				    	<a href="javascript:;" class="tabLink" id="cont-2" onClick="loadTabData(2,$('#pageLabeltcId0 span').text());">Schedule Testcase</a>
				    </span>
	
				     <span>
				    	<a href="javascript:;" class="tabLink " id="cont-3" onClick="loadTabData(3,$('#pageLabeltcId0 span').text());">View<b> / </b>Download Reports</a>
				    </span>
			    <#else>
				      <span>
				    	<a href="javascript:;" class="tabLink activeLink" id="cont-5"  onClick="loadTabDataForWs(5,$('#pageLabeltcId0 span').text());">Schedule Webservice Suit</a>
				    </span> 
					<span>
				    	<a href="javascript:;" class="tabLink" id="cont-6" onClick="loadTabDataForWs(6,$('#pageLabeltcId0 span').text());">Reports</a>
				    </span>
			     </#if>
			  <!--   <span>
			    	<a href="javascript:;" class="tabLink " id="cont-4" onClick="loadTabData(4);">Monitor Jobs</a>
			    </span> -->
			</div>
			<div class="tabcontent paddingAll" id="cont-2-1" />
				<#if regressionTestExecutionForm.setupTabNumber == 1>
					<#include "UiScheduleStatus.ftl">
				<#elseif regressionTestExecutionForm.setupTabNumber == 2>
					<#include "UiScheduleTestcase.ftl">
				<#elseif regressionTestExecutionForm.setupTabNumber == 3>
					<#include "UiReports.ftl">
				<#elseif regressionTestExecutionForm.setupTabNumber == 4>
					<#include "UiMonitorJobs.ftl">
				<#elseif regressionTestExecutionForm.setupTabNumber == 5>
					<#include "ScheduleWsSuite.ftl">
				<#elseif regressionTestExecutionForm.setupTabNumber == 6>
					<#include "WsReports.ftl">
				 </#if>
			</div>
			
		
 		</div>
</div>
</body>
</html>
   