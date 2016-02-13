<#import "../common/spring.ftl" as spring />
<#import "../common/MacroTemplates.ftl" as mint />
<div id="Ui Testing Setup">
   <#include "../common/Header.ftl">
</div>
<style type="text/css">
  .tab-box {
  	margin-top: 1.5%;
  }
  .hd {
  	font-family: inherit;
    font-weight: bold;
  }
</style>
<script type="text/javascript" src="js/script.js"></script>
<script language="JavaScript">
function readCookie(name){
	return(document.cookie.match('(^|; )'+name+'=([^;]*)')||0)[2]
}
</script>
<head>	
<script type="text/javascript" src="js/formValidation.js"></script>		
<script type="text/javascript">  
 $(document).ready(function() { 
	 //getTaskScheduleList();
	 $("#oprasingle").prop("checked",true);
	 opraSelect();
	 $('#loginScriptFile').hide();
		$("#loginScriptButton").click(function() {
			$('#loginScriptFile').click();
	 });
  });

function onRadioClick(){
	var resetByPin = document.MVCOPRAResetToolForm.resetType[0].checked;
	if (resetByPin){
		if(document.getElementById('lblNumber')!=null)
		document.getElementById('lblNumber').innerHTML = '<font face="verdana,arial" size=-1>Pin Number</font>';
	}else{
		if(document.getElementById('lblNumber')!=null)
		document.getElementById('lblNumber').innerHTML = '<font face="verdana,arial" size=-1>User Id</font>';
	}
}

function validate()
{
	if($("#oprabulk").prop("checked")) {
		var file =  document.MVCOPRAResetToolForm.loginScriptFile.value;
		if(file == null || file.trim().length==0) {
			alert("Enter/Upload Customer details");
			return false;
		}else{
			document.getElementById("MVCOPRAResetToolForm").action="<@spring.url "/MVCBulkResetPINOrUserId.ftl" />";
		}
	}
	if($("#oprasingle").prop("checked")) {
		var textboxValue = document.MVCOPRAResetToolForm.customerPinNumber.value;
		if (textboxValue == null || textboxValue.trim().length == 0)
		{
			alert("Enter/Upload Customer details");
			return false;
		}
		document.getElementById("MVCOPRAResetToolForm").action="<@spring.url "/MVCResetPINOrUserId.ftl" />";
	}
	document.getElementById("MVCOPRAResetToolForm").submit();
	
	//document.getElementById("OPRAResetTool").action="<@spring.url "/OPERAResetTool.ftl" />";
	//document.getElementById("OPRAResetTool").submit();

}
function opraSelect() {
	 $(".oprasingle input").each(function() {
			$(this).prop("disabled",true);
	   });
	 $(".oprabulk input").each(function() {
			$(this).prop("disabled",true);
	   });
	if($("#oprasingle").prop("checked")) {
		 $(".oprasingle input").each(function() {
				$(this).prop("disabled",false);
		   });
	} else {
		 $(".oprabulk input").each(function() {
				$(this).prop("disabled",false);
		   });
	}
}
function CopyMe(oFileInput, sTargetID) {
	var arrTemp = oFileInput.value.split('\\');
	document.SetupEnvironmentForm.fileUrlPath.value = arrTemp[arrTemp.length - 1];
	document.SetupEnvironmentForm.fileUrlPath.title = arrTemp[arrTemp.length - 1];
}
function clearAll() {
	$("#loginScriptFile").val("");
	$("#customerPinNumber").val("");
	$("#fileUrlPath").val("");
	$("#oprasingle").prop("checked",true);
	opraSelect();
}
function getTaskScheduleList(){
	var request = $.ajax({  url: 
		"GetTaskScheduleList.ftl",
		type: "POST", 
		cache: false,
		dataType: "text"});
		
		request.done(function( taskScheduleList ) {
		var innerHtml = getTableHeader();
		if ($.parseJSON(taskScheduleList) != ''){
  			$.each($.parseJSON(taskScheduleList), function(idx, taskSchedule) {
     			innerHtml = innerHtml + '<tbody><tr data-sortfiltered="false">';
				innerHtml = innerHtml + '<td class="row">' + taskSchedule.taskScheduleId + '</td>';
				innerHtml = innerHtml + '<td class="row">' + taskSchedule.taskType + '</td>';
				innerHtml = innerHtml + '<td class="row">' + taskSchedule.taskStatus + '</td>';
				innerHtml = innerHtml + '<td class="row">' + taskSchedule.dateCreated + '</td>';
				innerHtml = innerHtml + '<td class="row">' + taskSchedule.userId + '</td>';
				innerHtml = innerHtml + "<td class='row'></td></tr></tbody>";
 			});
		}else{
			innerHtml = innerHtml + "<tbody><tr><td colspan='8'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr></tbody>";
		}
 			$('#opra_reset_table').empty().append(innerHtml);
		}); 
	 	request.fail(
	  		function( jqXHR, textStatus ){
	  		}); 
}

function getTableHeader(){
	var innerHtml = '<thead>';
	innerHtml += $("#tHeader").html();
	innerHtml += '</thead>';
	return innerHtml;
}
</script>
</head>
<body onLoad="window.scrollTo(0,readCookie('ypos'))">
<div style="width:80%;margin-top:18px;" class="content twoCol">
<div class="page-content" id="pagecontent" role="main" tabindex="-1">
			<#if success?exists>
			<div class="infoModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${success}
				</div>
			</div>
			</#if> 
			<#if error?exists>
			<div class="alertModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" id="errorMessage" title="Information">${error}</span>
				</div>
			</div>
			</#if>
			<#if warning?exists>
			<div class="warningModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icNotice" title="Information"></span>${warning}
				</div>
			</div>
			</#if>
			<!-- help contents -->
			<hr style="margin-top: 10px;">
			<div class="mblg">
				<div class="mtxs">
					<a id="tcId100" class="tipLink rel" name="tcId100">Help</a><b><@spring.message "mint.help.opraMVCReset"/></b><a href="<@spring.message 'mint.help.mailTo'/>"
						target="_top"> <@spring.message "mint.help.mailId"/></a>
				</div>
			</div>
			<hr style="margin-top: 0px;">
			<!--  table view one for application and category -->	

        <form modelAttribute="mvcOpraResetTool"  id="MVCOPRAResetToolForm" name="MVCOPRAResetToolForm" target="_top" method="POST">
      
		
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<input type="radio" id="oprasingle" name="bulkReset" checked="true" onClick="opraSelect();">&nbsp;&nbsp;Opra Reset
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
							<table class="bd rowheight35 oprasingle">
								<tr class="lblFieldPair">
									<td class="lbl">Reset By</td>
									<td class="input">
										<input type="radio" id="pin" name="resetType" value="1" checked="true" onClick="onRadioClick();"> PIN
									  &nbsp;&nbsp;
									  <input type="radio" id="userId" name="resetType" value="2" onClick="onRadioClick();"> User Id
										
										<font color="red" size=-1>&nbsp;*</font>
									</td>
								</tr>
								
								<tr class="lblFieldPair">
									<td class="lbl" id="lblNumber">Pin Number</td>
									<td class="input">
										<input type="text" id="customerPinNumber" name="customerPinNumber"/>	
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
							<input type="radio" id="oprabulk" name="bulkReset" checked="false" onClick="opraSelect();">&nbsp;&nbsp;
							Opra Bulk Reset
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<table class="bd rowheight35 oprabulk">
									<br />
									<!-- 	<tr class="lblFieldPair">
										<td class="lbl">Upload file</td>
										<td class="input">
											<input type="file" name="file"><font color="red" size=-1>&nbsp;*</font>
										</tr>
									</tr> -->
										<tr class="lblFieldPair fileupload">
											<td class="lbl">Upload file</td>
											<td class="input"><input class="input_small" type="text"
												id="fileUrlPath" name="fileUrlPath"
												value=""> <input
												type="file" id="loginScriptFile"
												onchange="CopyMe(this, 'txtFileName')"
												name="loginScriptFile"> <input type="button"
												class="uploadbutton" id="loginScriptButton" value="Upload"></td>
											</td>
										</tr>
										<tr class="lblFieldPair">
										<td class="lbl">Reset By</td>
										<td class="input">
											 <input type="radio" id="pin" name="bulkResetType" value="1" checked="true"> PIN
											  &nbsp;&nbsp;
											  <input type="radio" id="userId" name="bulkResetType" value="2" > User Id
											  <font color="red" size=-1>&nbsp;*</font>
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
				<tr class="bd">
				<td class="btnBar" style="border-top:none;">
					<a href="#" onClick="clearAll()" class="btn"><span>Clear</span></a>
					<a href="#" onClick="validate()" class="btn"><span>Submit</span></a>
				</td>
				</tr>
			</table>
			</div>
		</section>

		<!--  table view two for application modules -->	
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Opra Reset Status</h6>
						</td>
					</tr>
					<tr class="bd">
						<td>
						<div class="content" style="display: block;width:100%;" tabindex="0" id="opra_reset_table_content">
					  	<div class="bd">
					  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
								<table id="opra_reset_table" class="drop2 styleA fullwidth sfhtTable" summary="">
								<caption></caption>
								<thead id="tHeader">
									<tr>
										<th class="txtl header w8" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Task Schedule Id</th>
										<th class="txtl header w8" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue1" tabindex="0" data-sortpath="none">Task Type</th>
										<th class="txtl header w8" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue2" tabindex="0" data-sortpath="none">Status</th>
										<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue3" tabindex="0" data-sortpath="none">Job Submitted Time</th>
										<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue4" tabindex="0" data-sortpath="none">Submitted By</th>
										<th class="txtl header w8">Download</th>
									</tr>
								</thead>
								<tbody>
								<#if mvcOpraScheduleList?has_content>
										<#list mvcOpraScheduleList! as taskSchedule>
											<tr data-sortfiltered="false">
												<td class="row" data-sortvalue="${taskSchedule.taskScheduleId!}">${taskSchedule.taskScheduleId!}</td>
												<td class="row" data-sortvalue="${taskSchedule.taskTypeValue!}">${taskSchedule.taskTypeValue!}</td>
												<td class="row" data-sortvalue="${taskSchedule.taskStatusValue!}">${taskSchedule.taskStatusValue!}</td>
												<td class="row" data-sortvalue="${taskSchedule.dateCreated!}">${taskSchedule.dateCreated!}</td>
												<td class="row" data-sortvalue="${taskSchedule.userId!}">${taskSchedule.userId!}</td>
												<td class="row">
													<#if taskSchedule.taskStatusValue == 'SUCCESS' || taskSchedule.taskStatusValue == 'Completed with Errors/Warnings'>
														<a href="MVCOPERAReportDownload/${taskSchedule.taskScheduleId}.ftl" target="_blank">Download</a>
													<#else>
														-
													</#if>
												</td>
											</tr>
										</#list>
								<#else>
									<tr><td colspan='4'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
								</#if>
								</tbody>
							</table>
							</div>
								<@mint.tablesort id="#opra_reset_table" casesensitive="false" jsvar="opra_reset_table__js"/>
								<@mint.paginate table="#opra_reset_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</section>
	</form>
</div>
</div>
</body>
</html>
