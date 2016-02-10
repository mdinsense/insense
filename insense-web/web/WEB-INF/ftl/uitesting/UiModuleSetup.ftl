<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css"
	href="css/jquery.datetimepicker.css" />
<script src="js/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript" src="js/formValidation.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
</head>
<style>
.dicon
	{
	background: url('../images/idownload.png') no-repeat
}
input[type="file"] {
	width : 150px;
}
</style>
<script type="text/javascript">

var javafiles = [];var htmlfiles = [];
/* $(document)
        .on(
                "change",
                "#testCaseFile1",
                function(event) {
                 javafiles=event.target.files;
                })

                
                
  $(document)
        .on(
                "change",
                "#testHtmlFile1",
                function(event) {
                	htmlfiles=event.target.files;
                }) */

$(document).ready(function() {
	$(".staticUrlList").hide();
	$(".editScript").hide();
	$('.file').hide();
	$("#staticUrlButton").click(function() {
		$('#staticUrlFile').click();
	});
	$("#environmentId").attr('disabled','disabled');
	$(".byurlpattern").hide();
	$(".startUrl").hide();
	$(".testUrl").hide();
	showUrlPatternOrStaticUrl();
	if($("#startUrlPattern").val()) {
		$("#startUrl").prop("checked",true);
		setStartOrTestUrl();
	}else if($("#testUrlPattern").val()) {
		$("#testUrl").prop("checked",true);
		setStartOrTestUrl();
	}
});
function addNewTestCaseRow(addRow) {
	removeElements(addRow);
    $('#containertbl').append("<tr class='lblFieldPair transactional remove'>\
			<td class='lbl'>Test Case name</td>\
			<td class='input'>\
				<input class='input_small required testCaseName' type='text' id='testCaseName' name='testCaseName' value=''>\
				<font color='red' size=-1>*&nbsp;&nbsp;</font>\
				Java <font color='red' size=-1>*&nbsp;</font><input type='file' class='testCaseFile' name='testCaseFile'>\
				Html <input type='file' class='testHtmlFile' name='testHtmlFile'>\
				<a class='editLink np' name='addbtn' onClick='addUploadRow()' width='20' height='20'><span class='icon nmt plxs' ></span>Add\
			</td>\
		</tr>");
 }
function addUploadRow() {
	var test
    $('#containertbl').append("<tr class='lblFieldPair transactional remove'>\
			<td class='lbl'>Test Case name</td>\
			<td class='input'>\
				<input class='input_small required testCaseName' type='text' id='testCaseName' name='testCaseName' value=''>\
				<font color='red' size=-1>*&nbsp;&nbsp;</font>\
				Java <font color='red' size=-1>*&nbsp;</font><input type='file' class='testCaseFile' name='testCaseFile'>\
				Html <input type='file' class='testHtmlFile' name='testHtmlFile'>\
				<a class='editLink np' name='addbtn' onClick='removeElements(this)' width='20' height='20'><span class='icon nmt plxs' ></span>Remove\
			</td>\
		</tr>");
 }
function removeElements(btnelem)
{
	 $(btnelem).closest('tr').remove();  
} 
function deleteRow(testLoginId) {
	if(confirm("Confirm delete of application configs"))
	{
		document.getElementById("testLoginId").value = testLoginId;
		document.getElementById("setupenvloginid").setAttribute('action',"<@spring.url '/deleteEnvLoginUser.ftl' />");
		document.getElementById("setupenvloginid").setAttribute('method',"POST");
		document.getElementById("setupenvloginid").submit();
	}
}
function deleteForm(applicationModuleXrefId){
	if(	confirm("Please Confirm to Delete Application module from the system")){
		document.getElementById("applicationModuleXrefId").value=applicationModuleXrefId;
		clearFields();
		document.getElementById("ApplicationModuleFormId").action="<@spring.url "/DeleteApplicationModule.ftl" />";
		document.getElementById("ApplicationModuleFormId").submit();
  	}
}
function editFormSubmit(applicationModuleXrefId){
	document.getElementById("applicationModuleXrefId").value=applicationModuleXrefId;
	document.getElementById("ApplicationModuleFormId").action="<@spring.url "/EditApplicationModuleDetails.ftl" />";
	document.getElementById("ApplicationModuleFormId").submit();
	$("#applicationId").attr('disabled','disabled');
	$("#environmentCategoryId").attr('disabled','disabled');
	$("#environmentId").attr('disabled','disabled');
}	
function disable_Env(){
	if(document.getElementById("environmentId").disabled){
		document.getElementById("environmentId").disabled = false;		
	}else{
		document.getElementById("environmentId").disabled = true;
	}
}
function getEnvironmentCategoryDetails() {
	var applicationId = $("#applicationId").val();
	if ($('#environmentCategoryId option').length > 0){
		document.getElementById("environmentCategoryId").options.length=0;
	}
	loadEnvironmentCategoryDropdown(applicationId,"environmentCategoryId");
	//loadEnvironmentCategoryDropdownAppendALL(applicationId,"environmentCategoryId");
	
}
function getEnvironmentDetails() {
	var environmentCategoryId =$("#environmentCategoryId").val();
	var applicationId = $("#applicationId").val();
	if ($('#environmentId option').length > 0){
		document.getElementById("environmentId").options.length=0;
	}
		loadEnvironmentDropdown(applicationId, environmentCategoryId,"environmentId");	
}

function processUpload(oldTestCaseId,target) {
	var action = "UpdateTestCaseJavaOnly.ftl";
	var oMyForm = new FormData();
	var javaFile = []; 
	javaFile = document.getElementById(oldTestCaseId+"_java").files;
	var htmlFile = [] 
	htmlFile = document.getElementById(oldTestCaseId+"_html").files;
	if(typeof javaFile !== 'undefined' && javaFile.length > 0) {
    	oMyForm.append("javafile", javaFile[0]);
	} else {
		alert("Please select testcase file to continue");
		return false;
	}
    if(typeof htmlFile !== 'undefined' && htmlFile.length > 0) {
    	oMyForm.append("htmlfile", htmlFile[0]);
    	action = "UpdateTestCase.ftl";
    }
    oMyForm.append("oldTestcaseId", oldTestCaseId);
    oMyForm.append("testCaseName", $("#"+oldTestCaseId+"_testId").val());
	
	var request = $.ajax({  url: 	
		action,  
		type: "POST",  
		data: oMyForm,
		enctype: 'multipart/form-data',
        processData: false,
        contentType: false}); 
		request.done(function( msg ) {
		 if(msg == "Success") {
			 alert("Testcase Updated successfully");
			 removeElements(target);
		 } else {
			 alert("Unable to update the testcase");
		 }
	 	}); 
	request.fail(
			 	 function( jqXHR, textStatus ){  
			 		disableLoader();
			 		alert("Testcase Update failed");
						if(jqXHR.status == '403' || jqXHR.status == '500'){
				            // inactive session so redirect to login page
			            	window.location = '/mint/login.ftl';
					    }
			 	 }); 
}

function validateAndsave(){
	
	if($("#moduleTypeId").val() < 1) {
		alert("Please choose any module type");
		return false;
	}
	
	if($("#moduleName").val() == "") {
		alert('Please provdide module name');
		document.aform.moduleName.focus();
		return false;
	}
	
	if($("#applicationId").val() == "" || $("#applicationId").val() <=0) {
		alert('Please Select Application');
		document.aform.application.focus();
		return false;
	}
	
	$("#environmentId").attr('disabled',false);
	if($("#environmentId").val() == "" || $("#environmentId").val() <0) {
		alert('Please Select Environment');
		$("#environmentId").attr('disabled','disabled');
		return false;
	}
	
	$("#environmentId").attr('disabled','disabled');
	if($("#environmentCategoryId").val() == "" || $("#environmentId").val() <0) {
		alert('Please Select environmentCategoryId');
		document.aform.environmentCategoryId.focus();
		return false;
	}
	
	if($("#environmentCategoryId").val() == "" || $("#environmentId").val() <0) {
		alert('Please Select environmentCategoryId');
		document.aform.environmentCategoryId.focus();
		return false;
	}
	
	// module type is static url
	if($("#moduleTypeId").val() == 2) {
		if($("#fileUrlPath").val() =="" || $("#columnPosition").val() <=0) {
			alert('Please upload static url list file and provide column position');
			return false;
		}
		
	}
	// module type is url pattern
	if($("#moduleTypeId").val() == 1) {
			// start url pattern selected
			if($("#startUrl").prop("checked")) {
				if(isEmpty(document.aform.startUrlPattern)){
					alert('Please provide start Url proceed');								
					return false;
				}
				if(!isEmpty(document.aform.startUrlPattern) && !isInNumericRange(document.aform.startUrlPattern.value.length,1,250)){
					alert('start Url pattern length should be below 250 characters.');
					edocument.aform.startUrlPattern.focus();
					return false;
				}
				if(!isEmpty(document.aform.startUrlPattern) && !isInNumericRange(document.aform.startUrlPattern.value.length,1,250)){
					alert('start Url Pattern length should be below 250 characters.');
					document.aform.startUrlPattern.focus();
					return false;
				}
			}
			// test url pattern selected
			else if($("#testUrl").prop("checked")){
				if(isEmpty(document.aform.testUrlPattern)){
					alert('Please provide Test Url Pattern to proceed');								
					return false;
				}
				if(!isEmpty(document.aform.testUrlPattern) && !isInNumericRange(document.aform.testUrlPattern.value.length,1,250)){
					alert('Test Url pattern length should be below 250 characters.');
					edocument.aform.testUrlPattern.focus();
					return false;
				}
				if(!isEmpty(document.aform.testUrlPattern) && !isInNumericRange(document.aform.testUrlPattern.value.length,1,250)){
					alert('Test Url Pattern length should be below 250 characters.');
					document.aform.includeUrlPattern.focus();
					return false;
				}
			} else {
				alert("Please choose any of the option test url and start url")
				return false;
			}
	} 
	// Transaction module type
	if($("#moduleTypeId").val() == 3){
		var isValid = 1;
		$(".testCaseName").each(function() {
			if($(this).val() == null || $(this).val() == "") {
				alert('Testcase name missing');
				isValid = 0;
			}
		});
		$(".testCaseFile").each(function() {
			if($(this).val() == null || $(this).val() == "") {
				alert('Testcase file missing');
				isValid = 0;
			}
		});
		if(isValid == 0) {
			return false;
		}
	} 	
	
	if( document.aform.applicationModuleXrefId.value != "" && document.aform.applicationModuleXrefId.value > 0
			|| document.aform.applicationModuleXrefId.value != "" && document.aform.applicationModuleXrefId.value > 0	) {
			document.getElementById("ApplicationModuleFormId").setAttribute('action',"<@spring.url '/UpdateApplicationModule.ftl' />");
	} else {
		
			document.getElementById("ApplicationModuleFormId").setAttribute('action',"<@spring.url '/SaveApplicationModule.ftl' />");
	}
	$("#environmentId").attr('disabled',false);
	document.getElementById("ApplicationModuleFormId").submit();
}

function clearFields(){
	document.aform.applicationId.value = "";
	document.aform.environmentId.value = "";
	document.aform.environmentCategoryId.value = "";
	document.aform.includeUrlPattern.value = "";
	document.aform.testUrlPattern.value = "";
}

function clearIncludeTestUrlPattern(){
	document.aform.includeUrlPattern.value = "";
	document.aform.testUrlPattern.value = "";
}

function clearFieldsApplication(){
	document.aform.environmentId.value = "";
	document.aform.environment.value = "";
	document.aform.environmentCategoryId.value = "";
	document.aform.includeUrlPattern.value = "";
	document.aform.testUrlPattern.value = "";
}
function showUrlPatternOrStaticUrl() {
	
	$("#testUrl").prop("checked",true);
	$(".byurlpattern").hide();
	$(".staticUrlList").hide();
	$(".urlpattern").hide();
	$(".startUrl").hide();
	$(".testUrl").hide();
	$(".transactional").hide();
	$(".edit").hide(); 
	
	if($("#moduleTypeId").val() == 1) {
		$(".byurlpattern").show();
		$(".staticUrlList").hide();
		$(".urlpattern").show();
		$(".startUrl").hide();
		$(".testUrl").show();
		$(".transactional").hide();
		$(".edit").hide(); 
		setStartOrTestUrl();
	} 
	
	if($("#moduleTypeId").val() == 2) { 
			$(".staticUrlList").show();
			$(".byurlpattern").hide();
			$(".urlpattern").hide();
			$(".transactional").hide();
			$(".edit").hide();
	}
	
	if($("#moduleTypeId").val() == 3) { 
		$(".transactional").show();
		$(".byurlpattern").hide();
		$(".staticUrlList").hide();
		$(".urlpattern").hide();
		$(".startUrl").hide();
		$(".testUrl").hide();
	}
}
function setStartOrTestUrl() {
	if($("#startUrl").prop("checked")) {
		$(".startUrl").show();
		$(".testUrl").hide();
	} 
	if($("#testUrl").prop("checked")) { 
		$(".startUrl").hide();
		$(".testUrl").show();
	}
}
function CopyMe(oFileInput, sTargetID) {
	var arrTemp = oFileInput.value.split('\\');
	$("#"+sTargetID).val(arrTemp[arrTemp.length - 1]);
}
function clearAll() {
	$("#applicationId").val(-1);
	$("#environmentId").val(-1);
	$("#environmentCategoryId").val(-1);
	$("#applicationModuleXrefId").val("");
	$("#moduleTypeId").val("");
	$("#moduleName").val("");
	$("#startUrl").prop('checked', false);
	$("#testUrl").prop('checked', false);
	$("#startUrlPattern").val("");
	$("#testUrlPattern").val("");
	$("#includeUrlPattern").val("");
	$(".byurlpattern").hide();
	$(".startUrl").hide();
	$(".testUrl").hide();
	$(".staticUrlList").hide();
	$('#staticUrlFile').hide();
	$(".testCaseName").each(function() {
		$(this).val(null);
	});
	$(".testCaseFile").each(function() {
		$(this).val(null);
	});
	$(".remove").each(function() {
		removeElements($(this));
	});
	$(".transactional").hide();
}
function removeEditRows() {
	$(".editTest").each(function() {
		removeElements($(this));
	});
}
function clearAndReset() {
	var id = $("#moduleTypeId").val();
	clearAll();
	$("#moduleTypeId").val(id);
	showUrlPatternOrStaticUrl();
}
function deleteScript(testCaseId,target){
	if(confirm("Please Confirm to Delete the selected test case file")){	
		enableLoader();
		var moduleId = $("#applicationModuleXrefId").val();
		var dataToSend = {
				 'testCaseId' : testCaseId, 'moduleId' : moduleId
			      }; 
		var request = $.ajax({  
			url: "DeleteTestCase.ftl",
			type: "POST", 
			data: dataToSend,
			dataType: "text"
		}); 
		request.done(function( msg ) {
			disableLoader();
			if(msg == 'Success') {
				alert("Test case deleted successfully");
				removeElements(target);
			}
		});
		request.fail(function( jqXHR, textStatus ){ 
			disableLoader();
			alert("Unable to delete testcase");
		}); 
	}
} 
function getTestCases(moduleId) {
	    var testCaseId = 0;
		var request = $.ajax({  url: 
			"getTestCases.ftl",
			data: {'moduleId':moduleId},
			type: "POST",
			dataType: "text"});
			request.done(function( testCaseList ) {
			if ($.parseJSON(testCaseList) != ''){
				var innerHtml = '';
				innerHtml = innerHtml + '<tbody>';
				innerHtml = innerHtml + '<tr class="hd"><th scope="row" align="right">Testcase Name</th><th>Java File</th><th>Html File</th></tr>';
				$.each($.parseJSON(testCaseList), function(idx, script) {
					innerHtml = innerHtml + '<tr><td scope="row" align="center">' + script.testCase.transactionName + '</td>';
					if(script.testCaseAvailable) {
						innerHtml = innerHtml + '<td><div><a class="editLink np" href="#" onClick="downloadTestCase('+script.testCase.testCaseId+',1)"><span class="icon nmt plxs" ></span>download</a></div></td>';
						if(script.htmlAvailable) {
							innerHtml = innerHtml + '<td><div><a class="editLink np" href="#" onClick="downloadTestCase('+script.testCase.testCaseId+',2)"><span class="icon nmt plxs"></span>download</a></div></td>';
						} else {
							innerHtml = innerHtml + "<td>Not available</td>"
						}
					} else {
						innerHtml = innerHtml + "<td>Not available</td><td>Not available</td>"
					}
					innerHtml = innerHtml + "</tr></tbody>";
					testCaseId = script.testCase.testCaseId;
				 });
				if(testCaseId > 0) {
					innerHtml = innerHtml + '<tr colspan="3"><td scope="row" colspan="3" align="center"><div><a class="editLink np" href="#" onClick="downloadTestCase('+testCaseId+',3)"><span class="icon nmt plxs"></span>Download All</a></div></td></tr>';
				}
				innerHtml = innerHtml + '</tbody>';
	 			$('#completion_summary_table').empty();
		 		$('#completion_summary_table').html(innerHtml);
			}
	 	});
	 	request.fail(
	  		function( jqXHR, textStatus ){  
  		}); 
}
function downloadTestCase(testCaseId,typeId) {  
	var form = $(document.createElement('form'));
	$('body').append(form);
	$(form).attr("name", "aform1");
	$(form).attr("modelAttribute", "UiTestingSetupForm");
	$(form).attr("action", "<@spring.url '/downloadTestCase.ftl' />");
	$(form).attr("method", "POST");
	var input1 = $("<input>").attr("type", "hidden").attr("name", "testCaseId").val(testCaseId);
	var input2 = $("<input>").attr("type", "hidden").attr("name", "typeId").val(typeId);
	$(form).append($(input1));
	$(form).append($(input2));
	$(form).submit();
} 

function showTestasePopup(moduleId,moduleName){
	enableLoader();
	 var wWidth = $(window).width();
     var dWidth = wWidth * 1.0;
     var wHeight = $(window).height();
     var dHeight = wHeight * 0.7;
     
	$("#testCaseListView").dialog({
		width: "auto",
	    minHeight: "auto",
	    maxHeight: dHeight,
	    show: "slide",
		title : 'TestCases for '+moduleName,
		resizable : false,
		position : [ 'center', 'center' ],
		create: function (event) { $(event.target).parent().css('position', 'fixed');}
	});
	
	var testCaseListViewContent = '<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="completion_summary_content">';
	testCaseListViewContent = testCaseListViewContent + '<div class="bd">';
	testCaseListViewContent = testCaseListViewContent + '<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">';
	testCaseListViewContent = testCaseListViewContent + '<table id="completion_summary_table" class="styleA fullwidth sfhtTable" summary=""></table></div></div>'
	$("#testCaseListView").html(testCaseListViewContent);
	getTestCases(moduleId);
	$("#testCaseListView").show();
	window.scrollTo(0, 1000);
	disableLoader();
}
</script>



 <!-- Hero Banner Start -->

<section class="drop mtlg">
<div class="cm">

<table cellpadding='0' cellspacing='0' border='0' width='100%'>
		<tr>
			<td class="hd">
				<h3>Add New Application Module</h3>
			</td>
		</tr>
		<tr class="bd">
			<td style="padding: 5">
			<body>
				<form modelAttribute="UiTestingSetupForm" enctype="multipart/form-data" id="ApplicationModuleFormId" name="aform" target="_top" method="POST" onLoad=disable_Env();>
					<input type="hidden" id="applicationModuleXrefId" name="applicationModuleXrefId" value="${uiTestingSetupForm.applicationModuleXrefId!}" />
					<input type="hidden" id="setupTabNumber" name="setupTabNumber" value="7">
						<table class="bd rowheight35 sfhtTable" id="containertbl">
							<tr class="lblFieldPair">
								<td class="lbl">Module Type</td>
								<td class="input">
									<select id="moduleTypeId" class="" name="moduleTypeId" onChange="clearAndReset();">
									<option value="-1">---------Select----------</option> 
										<#list moduleTypeList! as moduleType> 
											<#if uiTestingSetupForm.moduleTypeId?exists && uiTestingSetupForm.moduleTypeId == moduleType.moduleTypeId>
												<option value="${moduleType.moduleTypeId}" selected="true">${moduleType.moduleType}</option>
											<#else>
												<option value="${moduleType.moduleTypeId}">${moduleType.moduleType}</option>
											</#if> 
										</#list>
									</select><font color="red" size=-1>&nbsp;*</font>
								</td>
							</tr>
							<tr class="lblFieldPair">
								<td class="lbl">Module Name</td>
								<td class="input">
									<input type="text" value="${uiTestingSetupForm.moduleName!}" name="moduleName" id="moduleName" maxlength="250">
									<font color="red" size=-1>&nbsp;*</font>
								</td>
							</tr>
							<tr class="lblFieldPair">
								<td class="lbl">Application Name</td>
								<td class="input">
									<select id="applicationId" class="" name="applicationId" onchange="getEnvironmentCategoryDetails()"> 
										<option value="-1">---------Select----------</option>
										<#list applications! as application> 
											<#if uiTestingSetupForm.applicationId?exists && uiTestingSetupForm.applicationId == application.applicationId>
												<option value="${application.applicationId}" selected="true">${application.applicationName}</option>
											<#else>
												<option value="${application.applicationId}">${application.applicationName}</option>
											</#if> 
										</#list>
									</select><font color="red" size=-1>&nbsp;*</font>
							</td>
							</tr>
							<tr class="lblFieldPair">
								<td class="lbl">Environment Category</td>
								<td class="input">
									<select id="environmentCategoryId" class="" name="environmentCategoryId" onchange="getEnvironmentDetails()">
											<option value="-1" selected="true">---------Select----------</option>
											<#list environmentCategoryList! as environmentCategory>
												<#if uiTestingSetupForm.environmentCategoryId?exists && uiTestingSetupForm.environmentCategoryId == environmentCategory.environmentCategoryId>
													<option value="${environmentCategory.environmentCategoryId}" selected="true">${environmentCategory.environmentCategory.environmentCategoryName!}</option>
												<#else>
													<option value="${environmentCategory.environmentCategoryId}">${environmentCategory.environmentCategory.environmentCategoryName!}</option>
												</#if>
											</#list>
									</select><font color="red" size=-1>&nbsp;*</font>
								</td>
							</tr>
							<tr class="lblFieldPair">
								<td class="lbl">Environment Name</td>
								<td class="input">
									<select id="environmentId" name="environmentId"	class="" onchange="clearIncludeExclude()"> 
										<option value="-1" selected="true">---------Select----------</option>
										<#list environmentList! as environment>
											<#if uiTestingSetupForm.environmentId?exists && uiTestingSetupForm.environmentId == environment.environmentId>
												<option value="${environment.environmentId!}" selected="true">${environment.environmentName!}</option>
											<#else>
												<option value="${environment.environmentId!}" >${environment.environmentName!}</option>
											</#if> 
										</#list>
									</select>
								</td>
							</tr></br>
							<tr class="lblFieldPair byurlpattern">
								<td class="lbl">
									<input type="radio" name="startOrTestUrl" id="startUrl" value="Start Url" onchange="setStartOrTestUrl()">
									&nbsp;&nbsp;&nbsp;Provide Start Url&nbsp;&nbsp;&nbsp;
								</td>
								<td class="input">
									<input type="radio" name="startOrTestUrl" id="testUrl" value="Test Url" onchange="setStartOrTestUrl()">Test Url Pattern
								</td>
							</tr>
							<tr class="lblFieldPair startUrl urlpattern">
								<td class="lbl">Start URL</td>
								<td class="input">
									<input type="text" name="startUrlPattern" class="required" id="startUrlPattern" value="${uiTestingSetupForm.startUrlPattern!}">
								</td>
							</tr>	
							<tr class="lblFieldPair urlpattern testUrl">
								<td class="lbl">Test URL Pattern</td>
								<td class="input">
									<input type="text" name="testUrlPattern" class="required" id="testUrlPattern" value="${uiTestingSetupForm.testUrlPattern!}">
								</td>
							</tr>	
							<tr class="lblFieldPair urlpattern incUrl">
								<td class="lbl">Include URL Pattern</td>
								<td class="input">
									<input type="text" name="includeUrlPattern" class="" id="includeUrlPattern" value="${uiTestingSetupForm.includeUrlPattern!}">
								</td>
							</tr>
							<tr class="lblFieldPair staticUrlList">
								<td class="lbl">Upload Static Url List</td>
								<td class="input">
									<input class="input_small" type="text" class="required" id="fileUrlPath" name="fileUrlPath" value="${uiTestingSetupForm.fileUrlPath!}""> 
									<input type="file" class="file" id="staticUrlFile" onchange="CopyMe(this, 'fileUrlPath')" name="staticUrlFile"> 
									<input type="button" class="uploadbutton" id="staticUrlButton" value="Upload"></td>
								</td> 
							</tr>
							<tr class="lblFieldPair staticUrlList">
								<td class="lbl">Url Column Position</td>
								<td class="input">
									<input class="input_small" type="text" class="required" name="columnPosition" id="columnPosition" value="${uiTestingSetupForm.columnPosition!}">
								</td>
							</tr>
							<#if testCaseList?? && testCaseList?has_content>
								<#list testCaseList! as testCase>
								<tr class="lblFieldPair transactional editTest">
									<td class="lbl">Test Case name</td>
									<td class="input">
										<input type="hidden" name="oldTestCaseId" value="${testCase.testCaseId!}">
										<input class="input_small testCaseName" type="text" id="${testCase.testCaseId!}_testId" name="testCaseName" value="${testCase.transactionName!}">
										<font color='red' size=-1>*&nbsp;&nbsp;</font>
										Java <font color="red" size=-1>*&nbsp;</font>
										<input type="file" class="testCaseFile" id="${testCase.testCaseId!}_java" name="testCaseFile"> 
										Html 
										<input type="file" class="testHtmlFile" id="${testCase.testCaseId!}_html" name="testHtmlFile"> 
										<input type='button' name='addbtn' id='updatebtn' onclick="processUpload('${testCase.testCaseId!}',this)" value='Update' width='20' height='20'>&nbsp;
										<input type='button' name='addbtn' onClick='deleteScript(${testCase.testCaseId!},this)' value='Delete' width='20' height='20'>
									</td>
								</tr>
								</#list>
								<tr class="lblFieldPair transactional">
								 	<td colspan="2"><div style="float:right"><a class="editLink np" name="addbtn" onClick="removeEditRows();addNewTestCaseRow(this);" width="20" height="20"><span class="icon nmt plxs" ></span>Add New</a></div></td>
								</tr>
							<#else>
							<tr class="lblFieldPair transactional" style="display:none;">
								<td class="lbl">Test Case name</td>
								<td class="input">
									<input class="input_small testCaseName" type="text" id="testCaseName1" name="testCaseName" value="">
									<font color='red' size=-1>*&nbsp;&nbsp;</font>
									Java <font color="red" size=-1>*&nbsp;</font>
									<input type="file" class="testCaseFile" id="testCaseFile1" name="testCaseFile" width="20px"> 
									Html 
									<input type="file" class="testHtmlFile" id="testHtmlFile1" name="testHtmlFile" width="20px"> 
									<a class="editLink np" name="addbtn" onClick="addUploadRow()" width="20" height="20"><span class="icon nmt plxs" ></span>Add
								</td>
							</tr>
							</#if>
						</table>
						</form>
						</body>
						<center><font color="red" size=-1>&nbsp;* mandatory &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></center>
			</td>
		</tr>
		<tr class="bd">
			<td class="btnBar">
				<a href="#" onClick="validateAndsave()" class="btn"><span>Save</span></a>
				<a href="#" onClick="clearAll()" class="btn"><span>Clear</span></a>
				<a href="#" onClick="loadTabData(6)" class="btn"><span>Previous</span></a> 
			</td>
		</tr>
		</table>
		</div>
		</section>						
		<div id="testCaseListView" title="Reports summary"></div>
		<div class="content" tabindex="0" style="display: block;width:100%;" id="mint_home_table_content">
  		<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
		<table id="mint_home_table" class="styleA fullwidth sfhtTable" summary="">
			<caption></caption>
			<thead>
				<tr>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Module Name</th>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Application Name</th>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Environment Name</th>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Environment Category</th>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Include URL</th>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Test URL</th>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Start URL</th>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Static URL Count</th>
					<th class="txtl header w10">Download</th>
					<th class="txtl header w10">edit</th>
					<th class="txtl header w10">Remove</th>
				</tr>
			</thead>
			<tbody>
				<#list applicationEnvironmentCategoryList as appEnvDetails>
					<#list appEnvDetails.applicationModuleXrefList as applicationModule>
							<tr data-sortfiltered="false">
								<td scope="row" data-sortvalue="${applicationModule.moduleName!}">
								${applicationModule.moduleName}
								</td>
								
								<td scope="row" data-sortvalue="${appEnvDetails.application.applicationName!} ">
							 	${appEnvDetails.application.applicationName!} 
								</td>
								<td scope="row" data-sortvalue="${appEnvDetails.environment.environmentName!}">
								 ${appEnvDetails.environment.environmentName!} 
								</td>
								<td scope="row" data-sortvalue="${appEnvDetails.environment.environmentCategory.environmentCategoryName!}">
								 ${appEnvDetails.environment.environmentCategory.environmentCategoryName!}
								</td>
								<td scope="row" data-sortvalue="${applicationModule.includeUrlPattern!}">
								 ${applicationModule.includeUrlPattern!} 
								</td>
								<td scope="row" data-sortvalue="${applicationModule.testUrlPattern!}">
								 ${applicationModule.testUrlPattern!} 
								</td>
								<td scope="row" data-sortvalue="${applicationModule.startUrl!}">
								 ${applicationModule.startUrl!} 
								</td>
								<td scope="row" data-sortvalue="${applicationModule.staticUrlCount!}">
								 ${applicationModule.staticUrlCount!} 
								</td>
								<td class="row">
								<#if applicationModule.moduleTypeId ==3>
									<a href="#" class="editLink np" title="Edit the application" onClick="showTestasePopup(${applicationModule.applicationModuleXrefId},'${applicationModule.moduleName}')">
										<span class="icon nmt plxs"></span>Testcases
									</a>
								<#else>
								    NA	
								</#if>	
								</td>
								<td class="row">
									<a href="#" class="editLink np" title="Edit the application" onClick="editFormSubmit(${applicationModule.applicationModuleXrefId})">
									<span class="icon nmt plxs" ></span>Edit
									</a>
								</td>
								<td class="row">
									<a class="deleteIcon np" href="#" onClick="deleteForm(${applicationModule.applicationModuleXrefId})">
										<span class="icon nmt plxs" ></span>Remove
									</a>
								</td>
							</tr>
						</#list>
				</#list>
			</tbody>
</table>
</div>
<@mint.tablesort id="#mint_home_table" casesensitive="false" jsvar="mint_home_table__js"/>
<@mint.paginate table="#mint_home_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
</div>
</div>
</body>
</html>
