<#import "../common/spring.ftl" as spring />
<#import "../common/MacroTemplates.ftl" as mint />
<div id="Ui BrokenTesting Setup">
   <#include "../common/Header.ftl">
</div>
<style type="text/css">
  .tab-box {
  	margin-top: 1.5%;
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
    	enableDisableModules();
    	
    	if($("#environmentCategoryId").val() != null && $("#applicationId").val() != null) {
    		getEnvironmentDetails();
    		if($("#loginId").val() == null){
    			$('#checkSecureLbl').hide();
	   			$('#checkSecureInput').hide();
	   			$("#secureSite").val("OFF");
    		}
    		if($("#transactionOrCrawlerType").val() == "1"){
    			$("#transactional").prop("checked", true);
    		}
    		//loadApplicationModules();
    		setModuleType();
    	}
    	
    	<#if testingForm?exists> 
			<#if testingForm.brokenLinks?exists && testingForm.brokenLinks>
				$("#brokenLinks").prop("checked", true);
			</#if>
			
			<#if testingForm.brokenLinksResources?exists && testingForm.brokenLinksResources>
				$("#brokenLinksResources").prop("checked", true);
			</#if>
			
			
			<#if testingForm.loadAttributes?exists && testingForm.loadAttributes>
				$("#loadAttributes").prop("checked", true);
			</#if>
		</#if>	
		
  });
  
  $(document).ready(function(){
		$('#0').change(function () {
			enableDisableModules();
		});
	});

  
function isNumberKey(evt) {
	    var charCode = (evt.which) ? evt.which : event.keyCode
	    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))
	       return false;

	    return true;
}
  
function enableDisableModules() {
    if($('#0').prop("checked")) {
    	$(".modules").prop("checked",false);
    	$(".modules").attr("disabled",true);
    	$(".entireCrawlerModules").prop("checked",false);
    	$(".entireTransactionModules").prop("checked",false);
    	$(".entireCrawlerModules").attr("disabled",true);
    	$(".entireTransactionModules").attr("disabled",true);
		
	} else {
		$(".modules").attr("disabled",false);
		$(".entireCrawlerModules").attr("disabled",false);
    	$(".entireTransactionModules").attr("disabled",false);
		
	}
}
function loadEnvironmentCategoryDropdown(applicationId,environment){
	 var dataToSend = {
			 'applicationId' : applicationId
		      }; 
	 
	var request = $.ajax({  
		url: "GetEnvironmentCategoryDropdown.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 

	request.done(function( msg ) {	
		var Outmsg= JSON.stringify(msg);
		Outmsg = JSON.parse(Outmsg);
		$('<option />', {value: "-1", text: "-----Select-------"}).appendTo("#"+environment);
        $.each(Outmsg,function(key, val) {
        	  $('<option />', {value: key, text: val}).appendTo("#"+environment);
       });
	});
	
	request.fail(function( jqXHR, textStatus ){  
	}); 
}
function loadLoginUserDetails(){
	 var applicationId = $("#applicationId").val();
	 var categoryId = $("#environmentCategoryId").val();
	 var dataToSend = {
			 'applicationId' : applicationId,
			 'categoryId' : categoryId
		      }; 
	 
	var request = $.ajax({  
		url: "CheckSecureEnvironmentDropdown.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 

	request.done(function( msg ) {
		$('#checkSecureLbl').show();
		$('#checkSecureInput').show();
		$("#secureSite").val("ON");
		var Outmsg= JSON.stringify(msg);
		Outmsg = JSON.parse(Outmsg);
       $.each(Outmsg,function(key, val) {
    	   if(val == false){
    		   $('#checkSecureLbl').hide();
   			   $('#checkSecureInput').hide();
   				$("#secureSite").val("OFF");
    	   }else{
    		   var request = $.ajax({  
					url: "GetLoginUserDetails.ftl",
					type: "POST", 
					cache: false,
					data: dataToSend,
					dataType: "json"
				}); 
			
				request.done(function( msg ) {
					$('#loginId').html('');
					var Outmsg= JSON.stringify(msg);
					Outmsg = JSON.parse(Outmsg);
					$('<option />', {value: -1, text: "------Select-------"}).appendTo("#loginId");
			       $.each(Outmsg,function(key, val) {
			       	  $('<option />', {value: key, text: val}).appendTo("#loginId");
			      });
				});
				
				request.fail(function( jqXHR, textStatus ){
				}); 
    	   }
       	  
      });
	});
	
	request.fail(function( jqXHR, textStatus ){  
	}); 
}
function loadApplicationModules(){
	 var applicationId = $("#applicationId").val();
	 var categoryId = $("#environmentCategoryId").val();
	 var transactionOrCrawlerType = $("#transactionOrCrawlerType").val();
	 var moduleIdHtml = '';
	 $('#moduleId').html('');
	 
	 var dataToSend = {
			 'applicationId' : applicationId,
			 'categoryId' : categoryId
		      }; 
	 
	var request = $.ajax({  
		url: "GetApplicationModules.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "text"
	}); 

	request.done(function( moduleList ) {
		var test=[];
		var transactionArray=[];
		moduleIdHtml = moduleIdHtml + "<tr class='lblFieldPair'>";
		if ($.parseJSON(moduleList) != ''){
			if(transactionOrCrawlerType == 0){
				moduleIdHtml = moduleIdHtml +getCrawlerSection(moduleList,categoryId,test);
				//moduleIdHtml = moduleIdHtml +getTransactionSection(moduleList,test);
			}else{
				moduleIdHtml = moduleIdHtml +getTransactionSection(moduleList,test);
			}
		}else{
			moduleIdHtml = moduleIdHtml + "<td colspan=''><font face='verdana,arial' size='-1' >Modules Not Available.</font></td>";
		}
		moduleIdHtml = moduleIdHtml + "</tr>";
		
		$('#moduleId').append(moduleIdHtml);
		enableDisableModules();
	});
	
	request.fail(function( jqXHR, textStatus ){  
	});
}

function getCrawlerSection(moduleList,categoryId,test){
	var columnCount = 1;
	var moduleIdHtml = "<tr><td colspan='50'><div style='float:left;'><input type='checkbox' class='entireCrawlerModules' name='entireCrawlerModules' id='entireCrawlerModules' onclick='enableDisableCrawlerModules()'><b>Select All</b></div><div style='float:left; width: 40%;'><hr/></div><div style='float:right; width: 40%;'><hr/></div><b>Crawler Section</b></td></tr>";
	$.each($.parseJSON(moduleList), function(idx, module) {
			if(module.moduleTypeId!=3) {
				if(columnCount == 1) {
					moduleIdHtml = moduleIdHtml + "<tr>";
				}
				if(categoryId == 1 ){
					if(module.applicationModuleXrefId == 0){
						<#if testingForm?exists>
 						<#list testingForm.modules! as mod>
 							test.push(${mod});			
 						</#list>
 					
 						if(isContains(test,module.applicationModuleXrefId)){
 							moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox' name='entireApplication' id='"+module.applicationModuleXrefId+"' value='"+ module.applicationModuleXrefId + "' onclick='enableDisableModules()' checked disabled>" + module.moduleName + " </td>";
 						}else{
 							moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox' name='entireApplication' id='"+module.applicationModuleXrefId+"' value='"+ module.applicationModuleXrefId + "' onclick='enableDisableModules()' checked disabled>" + module.moduleName + " </td>";
 						}
					<#else>
							moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox' class='entireApplication' name='modules' id='"+module.applicationModuleXrefId+"' value='" + module.applicationModuleXrefId +"' onclick='enableDisableModules()' checked disabled>" + module.moduleName + " </td>";
						</#if>		
					}else{
						<#if testingForm?exists>
 						<#list testingForm.modules! as mod>
 							test.push(${mod});			
 						</#list>
 						
 						if(isContains(test,module.applicationModuleXrefId)){
 							moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox' class='modules CrawlerModules' name='modules' id='"+module.applicationModuleXrefId+"' value='" + module.applicationModuleXrefId +"' checked" + module.moduleName + " </td>";
 						}else{
 							moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox' class='modules CrawlerModules' name='modules' id='"+module.applicationModuleXrefId+"' value='" + module.applicationModuleXrefId +"'>" + module.moduleName + " </td>";
 						}
					<#else>
							moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox' class='modules CrawlerModules' name='modules' id='"+module.applicationModuleXrefId+"' value='" + module.applicationModuleXrefId +"' disabled>" + module.moduleName +  " </td>";
						</#if>	
					}
				}else{
					if(module.applicationModuleXrefId == 0){
						<#if testingForm?exists>
 						<#list testingForm.modules! as mod>
 							test.push(${mod});			
 						</#list>
 					
 						if(isContains(test,module.applicationModuleXrefId)){
 							moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox' name='entireApplication' id='"+module.applicationModuleXrefId+"' value='"+ module.applicationModuleXrefId + "' onclick='enableDisableModules()' checked>" + module.moduleName +  " </td>";
 						}else{
 							moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox' name='entireApplication' id='"+module.applicationModuleXrefId+"' value='"+ module.applicationModuleXrefId + "' onclick='enableDisableModules()'>" + module.moduleName +  " </td>";
 						}
						<#else>
							moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox' name='entireApplication' id='"+module.applicationModuleXrefId+"' value='"+ module.applicationModuleXrefId + "' onclick='enableDisableModules()'>" + module.moduleName + " </td>"; 
						</#if>	
					}else{
						<#if testingForm?exists>
 						<#list testingForm.modules! as mod>
 							test.push(${mod});			
 						</#list>
 						
 						if(isContains(test,module.applicationModuleXrefId)){
 							moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox' class='modules CrawlerModules' name='modules' id='"+module.applicationModuleXrefId+"' value='" + module.applicationModuleXrefId +"' checked>" + module.moduleName + " </td>";
 						}else{
 							moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox' class='modules CrawlerModules' name='modules' id='"+module.applicationModuleXrefId+"' value='" + module.applicationModuleXrefId +"'>" + module.moduleName + " </td>";
 						}
						<#else>
							moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox'  class='modules CrawlerModules' name='modules' id='"+module.applicationModuleXrefId+"' value='" + module.applicationModuleXrefId +"'>" + module.moduleName + " </td>"; 
						</#if> 	
					}	
				}
				if(columnCount == 5) {
					moduleIdHtml = moduleIdHtml + "</tr>"
				}
			}
		});
	return moduleIdHtml;
}

function getTransactionSection(moduleList,test){
	var columnCount = 1;
	var moduleIdHtml = "<tr><td colspan='50'><div style='float:left;'><input type='checkbox' class ='entireTransactionModules' name='entireTransactionModules' id='entireTransactionModules' onclick='enableDisableTransactionModules()'><b>Select All</b></div><div style='float:left; width: 40%;'><hr/></div><div style='float:right; width: 40%;'><hr/></div><b>Transaction Section</b></td></tr>";
		$.each($.parseJSON(moduleList), function(idx, module) {
			if(module.moduleTypeId==3) {
				if(columnCount == 1) {
					moduleIdHtml = moduleIdHtml + "<tr>";
				}
				<#if testingForm?exists>
					<#list testingForm.modules! as mod>
						test.push(${mod});			
					</#list>
					if(isContains(test,module.applicationModuleXrefId)){
						moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox' class='modules transactionmodules' name='modules' id='"+module.applicationModuleXrefId+"' value='" + module.applicationModuleXrefId +"' checked>" + module.moduleName + " </td>";
					}else{
						moduleIdHtml = moduleIdHtml + "<td style='width:20%;' class='input'><input type='checkbox' class='modules transactionmodules' name='modules' id='"+module.applicationModuleXrefId+"' value='" + module.applicationModuleXrefId +"'>" + module.moduleName + " </td>";
					}
					moduleIdHtml = moduleIdHtml + "<td><ul type='disc'>";
					 for(var modKey in module.transactionTestCaseList){
						var modValue = module.transactionTestCaseList[modKey];
						moduleIdHtml = moduleIdHtml + "<li style='font-style:italic;'>" + modValue.transactionName + "</li> ";
					} 
					 moduleIdHtml = moduleIdHtml + "</ul></td></tr>";
				<#else>	
					moduleIdHtml = moduleIdHtml + "<tr><td style='text-align: left; width:20%;' class='input'><input type='checkbox' class='modules transactionmodules' name='modules' id='"+module.applicationModuleXrefId+"' value='" + module.applicationModuleXrefId +"'>" + module.moduleName + " </td>";
					moduleIdHtml = moduleIdHtml + "<td><ul type='disc'>";
					 for(var modKey in module.transactionTestCaseList){
						var modValue = module.transactionTestCaseList[modKey];
						moduleIdHtml = moduleIdHtml + "<li style='font-style:italic;'>" + modValue.transactionName + "</li> ";
					} 
					 moduleIdHtml = moduleIdHtml + "</ul></td></tr>";
				</#if> 
				if(columnCount == 5) {
					moduleIdHtml = moduleIdHtml + "</tr>"
				}
			}	
		});
	
	return moduleIdHtml;
}

function enableDisableTransactionModules() {
   if($(".entireTransactionModules").prop("checked")) {
   	$(".transactionmodules").prop("checked",true);
	} else {
		$(".transactionmodules").prop("checked",false);
	}
}

function enableDisableCrawlerModules() {
	 if($(".entireCrawlerModules").prop("checked")) {
	    	$(".CrawlerModules").prop("checked",true);
	    	$("#0").prop("checked",false);
	    	$(".modules").attr("disabled",false);
		} else {
			$(".CrawlerModules").prop("checked",false);
		}
}
function setModuleType(){
	if($("#transactional").prop("checked")){
		
		$("#transactionOrCrawlerType").val("1");
	} else if($("#transactionalWithCrawler").prop("checked")) {
		
		$("#transactionOrCrawlerType").val("0");
	}
	loadApplicationModules();
}
function isContains(moduleArray,key){
	for(var i=0;i < moduleArray.length;i++) {
		if(moduleArray[i] == key) {
			return true;
		}
	}
	return false;
}
function getEnvironmentCategoryDetails() {
	$("#loginUrlDiv").hide();
	var applicationId = $("#applicationId").val();
	if ($('#environmentCategoryId option').length > 0){
		document.getElementById("environmentCategoryId").options.length=0;
		document.getElementById("loginId").options.length=0;
		$('#checkSecureLbl').show();
		$('#checkSecureInput').show();
		$("#secureSite").val("ON");
	}
	loadEnvironmentCategoryDropdown(applicationId,"environmentCategoryId");
	getEnvironmentDetails();
}
$(window).scroll(function () {
    document.cookie='ypos=' + $(window).scrollTop();
});

function validateChooseModule(){
	var boxes = document.getElementsByTagName('input');
	var moduleCheckboxCount = 0;

	for ( var i = 0; i < boxes.length; i++) {
		if (boxes[i] != null) {
			if (boxes[i].type == "checkbox" && (boxes[i].name=="appmodule" || boxes[i].name=="modules" || boxes[i].name=="entireApplication")) {
				if (boxes[i].checked) {
				moduleCheckboxCount++;
				}
			}

		}
	}
	if (moduleCheckboxCount == 0) {
		alert('Please Select atleast one module');
		return false;
	}
	return true;
}

function validateAndsave() {
	if(isEmpty(document.aform.suitName) || document.aform.suitName.value < 0 || document.aform.suitName.value == "") {
		alert('Suit Name should not be blank');
		document.aform.suitName.focus();
		return false;
	}
	
	if(isEmpty(document.aform.applicationId) || document.aform.applicationId.value < 0 || document.aform.applicationId.value == "") {
		alert('Please Select Application');
		document.aform.applicationId.focus();
		return false;
	}
	if(isEmpty(document.aform.environmentCategoryId) || document.aform.environmentCategoryId.value < 0 || document.aform.environmentCategoryId.value == "") {
		alert('Please Select Environment Category');
		document.aform.environmentCategoryId.focus();
		return false;
	}
	if((isEmpty(document.aform.loginId) || document.aform.loginId.value < 0 || document.aform.loginId.value == "") && document.aform.secureSite.value =="ON") {
		alert('Please Select Login Id');
		document.aform.loginId.focus();
		return false;
	}
	
	if(!validateChooseModule()){
		return false;
	}
	
	if(isEmpty(document.aform.browserType) || document.aform.browserType.value < 0 || document.aform.browserType.value == "") {
		alert('Please Select Browser Type');
		document.aform.browserType.focus();
		return false;
	}
	
	if(isEmpty(document.aform.urlLevel) || document.aform.urlLevel.value == "") {
		document.aform.urlLevel.value = 0 ;
	}
	
	if(document.aform.suitId.value != "" && document.aform.suitId.value > 0) {
		document.getElementById("brokenLinkTestingId").setAttribute('action',"<@spring.url '/UpdateBrokenLinkTesting.ftl' />");
	} else {
		document.getElementById("brokenLinkTestingId").setAttribute('action',"<@spring.url '/SaveBrokenLinkTesting.ftl' />");
	}

	document.getElementById("brokenLinkTestingId").submit();
}
function loadEnvironmentForTestingPages(applicationId, environmentCategoryId,target){

	 var dataToSend = {
			 'applicationId' : applicationId, 
			 'environmentCategoryId' : environmentCategoryId
		      };  
	 var environmentId = 0;
	 var request =$.ajax({
		        url: "GetEnvironmentDropdown.ftl",
		        type: "POST", 
		        cache: false,
		        data: dataToSend,
		        dataType: "json"
		        
		      });   
	 request.done(function( msg ) {    
	       var Outmsg= JSON.stringify(msg);
	       Outmsg = JSON.parse(Outmsg);
	       $.each(Outmsg,function(key, val) {
	           $('<option />', {value: key, text: val}).appendTo("#"+target);
	           environmentId = key;
	   	});
	    appendLoginUrl(environmentId, target);   
	});
		
	request.fail(function( jqXHR, textStatus ){
	});

}  
function appendLoginUrl(environmentId, target) {
	 var dataToSend = {
			 'environmentId' : environmentId
		      };  
	 var request =$.ajax({
		        url: "GetEnvironmentLoginUrl.ftl",
		        type: "POST", 
		        cache: false,
		        data: dataToSend,
		        dataType: "json"
		        
		      });   
	 request.done(function( msg ) {    
	       var Outmsg= JSON.stringify(msg);
	       Outmsg = JSON.parse(Outmsg);
	       $.each(Outmsg,function(key, val) {
	    	   $("#loginOrHomeUrl").val(val);
	    	   $("#loginUrlDiv").show();
	   	});
	});
		
	request.fail(function( jqXHR, textStatus ){  
	});
}
function getEnvironmentDetails() {
	$("#loginUrlDiv").hide();
	var environmentCategoryId =$("#environmentCategoryId").val();
	var applicationId = $("#applicationId").val();
	if ($('#environmentId option').length > 0){
		document.getElementById("environmentId").options.length=0;
	}
	loadEnvironmentForTestingPages(applicationId, environmentCategoryId,"environmentId");
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
					<span class="icon" title="Information"></span>${error}
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
					<a id="tcId100" class="tipLink rel" name="tcId100">Help</a><b><@spring.message
						"mint.help.brokenlink"/></b><a href="<@spring.message 'mint.help.mailTo'/>"
						target="_top"> <@spring.message "mint.help.mailId"/></a>
				</div>
			</div>
			<hr style="margin-top: 0px;">
		<!--  table view one for application and category -->	

        <form modelAttribute="testingForm"  action='SaveBrokenLinkTesting.ftl' id="brokenLinkTestingId" name="aform" target="_top" method="POST">
        <input type="hidden" id="secureSite" name="secureSite" value="ON">
        
		<#if testingForm?exists && testingForm.suitId?exists>
			<input type="hidden" id="suitId" name="suitId" value="${testingForm.suitId!}">
			<input type="hidden" id="transactionOrCrawlerType" name="transactionOrCrawlerType" value="${testingForm.transactionOrCrawlerType!}">
		<#else>
			<input type="hidden" id="suitId" name="suitId" value="">
			<input type="hidden" id="transactionOrCrawlerType" name="transactionOrCrawlerType" value="0">
		</#if>
		
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Provide Suit Name</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:2px;">
							<table class="bd rowheight35">
								<tr class="lblFieldPair">
									<td class="lbl">Suite Name</td>
									<td class="input">
										<#if testingForm?exists && testingForm.suitName?exists>
											<input type="text" id="suitName" name="suitName" value="${testingForm.suitName!}">
										<#else>
											<input type="text" id="suitName" name="suitName" value="">
										</#if>
										<font color="red" size=-1>&nbsp;*</font>
									</td>
								</tr>
								
								<tr class="lblFieldPair">
									<td class="lbl">Private Suit</td>
									<td class="input">
										<#if testingForm?exists && testingForm.privateSuit?exists && testingForm.privateSuit == true>
											<input type="checkbox" id="privateSuit" name="privateSuit" checked>
										<#else>
											<input type="checkbox" id="privateSuit" name="privateSuit" >
										</#if>	
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
							<h6>Choose Application and Environment category</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<input type="hidden" id="setupTabNumber" name="setupTabNumber" value="1">
								<table class="bd rowheight35">
									<br />
									<tr class="lblFieldPair">
										<td class="lbl">Application Name</td>
										<td class="input">
											<select id="applicationId" name="applicationId" onchange="getEnvironmentCategoryDetails()">
												<option value="-1" selected="true">----------Select-------------</option>
												<#list applications! as application> 
													<#if testingForm?exists && testingForm.applicationId?exists && testingForm.applicationId==application.applicationId>
														<option value="${application.applicationId!}" selected="true">${application.applicationName!}</option>
													<#else>
														<option value="${application.applicationId!}">${application.applicationName!}</option>
													</#if> 
												</#list>
											</select><font color="red" size=-1>&nbsp;*</font>
										</tr>
									</tr>
									<tr class="lblFieldPair">
										<td class="lbl">Environment Category</td>
										<td class="input">
											<select id="environmentCategoryId" name="environmentCategoryId" onchange="getEnvironmentDetails();loadLoginUserDetails();loadApplicationModules();">
													<option value="-1" selected="true">----------Select-------------</option>
													<#list environmentCategoryList! as environmentCategory>
														<#if testingForm?exists && testingForm.environmentCategoryId?exists && testingForm.environmentCategoryId==environmentCategory.environmentCategoryId>
															<option value="${environmentCategory.environmentCategoryId!}" selected="true">${environmentCategory.environmentCategoryName!}</option>
														<#else>
															<option value="${environmentCategory.environmentCategoryId!}">${environmentCategory.environmentCategoryName!}</option>
														</#if> 
													</#list>
											</select><font color="red" size=-1>&nbsp;*</font>
										</td>
									</tr>
									<tr class="lblFieldPair">
										<td class="lbl">Environment Name</td>
										<td class="input">
											<table>
											 <tr>
											 	<td>
												 	<select id="environmentId" name="environmentId" disabled> 
													<option value="-1" selected="true">---------Select----------</option>
													<#list environmentList! as environment>
														<#if testingForm.environmentId?exists && testingForm.environmentId == environment.environmentId>
															<option value="${environment.environmentId!}" selected="true">${environment.environmentName!}</option>
														<#else>
														<option value="${environment.environmentId!}" >${environment.environmentName!}</option>
														</#if> 
													</#list>
													</select>
											 	</td>
											 	<td>
											 		<div id="loginUrlDiv">
														&nbsp;&nbsp; Login/Home Url &nbsp;&nbsp;&nbsp;<input type="text" name="loginOrHomeUrl" id="loginOrHomeUrl" value="" disabled="true">
													</div>
												</td>
											 </tr>
											 </table>
										</td>
									</tr>
									<tr class="lblFieldPair">
										<td class="lbl"><div id="checkSecureLbl" >Login Id</div></td>
										<td class="input"><div id="checkSecureInput" >
											<select id="loginId" name="loginId">
												<#list loginList! as login>
													<#if testingForm?exists && testingForm.loginId?exists && testingForm.loginId==login.loginUserDetailId>
															<option value="${login.loginUserDetailId!}" selected="true">${login.loginId!}</option>
													</#if>
												</#list>
											</select><font color="red" size=-1>&nbsp;&nbsp;&nbsp;*</font></div>
										</td>
									</tr>
									<tr class="lblFieldPair">
										<td class="lbl">Process External URL 1-Level</td>
										<td class="input">
											<#if testingForm?exists && testingForm.processUrl?exists && testingForm.processUrl== true>
												<input type="checkbox" id="processUrl" name="processUrl" checked="checked">
											<#else>
												<input type="checkbox" id="processUrl" name="processUrl">
											</#if>	
										</td>
									</tr>
								</table>
						</td>
					</tr>
				</table>
			</div>
		</section>
		<!-- table view for report selection -->
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Report</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<table class="bd rowheight35">
								
								<tr class="lblFieldPair">
									<td style='width:20%;padding:5' class='input'>
										<input type="checkbox" id="brokenLinks" name="brokenLinks" >Broken Links
									</td>
									<td style='width:20%;padding:5' class='input'>
										<input type="checkbox" id="brokenLinksResources" name="brokenLinksResources">Broken links with resources
									</td>
									<td style='width:20%;padding:5' class='input'>
										<input type="checkbox" id="loadAttributes" name="loadAttributes">Load Time attributes 
									</td>
								</tr>
								</table>
						</td>
					</tr>
						
				</table>
			</div>
		</section>
		<!--  table view for Choose Crawling Depth -->
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Choose Crawling Depth</h6>
						</td>
					</tr>
					
					<tr class="bd">
						<td style="padding:10px">
								<table class="bd rowheight35" id="crawlurlLevel">
									<tr class="lblFieldPair">
										<td class="lbl"><label for="crawlurlLevel">Crawl URL Level</label></td>
										<td class="input">
											<#if testingForm?exists && testingForm.urlLevel?exists>
												<input type="text" min="1" id="urlLevel" name="urlLevel" onkeypress="return isNumberKey(event)" value="${testingForm.urlLevel!}">
											<#else>
												<input type="text" min="1" id="urlLevel" name="urlLevel" onkeypress="return isNumberKey(event)" value="">
											</#if>
										</td>
									</tr>
								</table>
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
							<font style="font-weight: bold;">Choose Modules</font>
							<span style="float: right;">
								<input type="radio" name="scriptType" id="transactional"  onclick="setModuleType()">&nbsp;&nbsp;&nbsp;Transactional
								<input type="radio" name="scriptType" id="transactionalWithCrawler" checked="checked" onclick="setModuleType()">&nbsp;&nbsp;&nbsp;Non-Transactional
							</span>
							&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<table class="bd rowheight35" id="moduleId">
						<tr class='lblFieldPair'>
								<td style='width:20%;' class='input'>
									<!-- <#if testingForm?exists && testingForm.modules[0]?exists && testingForm.modules[0]== "0">
										<input class="modules" type="checkbox" name="appmodule" checked="checked">All
									<#else>
										<input class="modules" type="checkbox" name="appmodule" onclick="enableDisableModules()">All 
									</#if> -->
								</td>
							</tr>
								</table>
						</td>
					</tr>
				</table>
			</div>
		</section>

		<!-- Browser type selection-->
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Choose Browser Type</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<table class="bd rowheight35">
								<tr class="lblFieldPair">
									<td class="lbl">Browser Type</td>
									<td class="input">
										<select id="browserType" name="browserType">
												<#list browserTypeList! as browserType>
													<#if testingForm?exists && testingForm.browserType?exists && testingForm.browserType==browserType.browserTypeId>
														<option value="${browserType.browserTypeId!}" selected="true">${browserType.browserName!}</option>
													<#else>
														<option value="${browserType.browserTypeId!}">${browserType.browserName!}</option>
													</#if> 
												</#list>
										</select><font color="red" size=-1>&nbsp;*</font>
									</td>
								</tr>
								</table>
								<center><font color="red" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* mandatory</font></center>
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
					<#if testingForm?exists && testingForm.editOrViewMode?exists && testingForm.editOrViewMode == 'ViewMode'>
						<a href="#" class="btn disabled"><span>Save</span></a>
					<#else>
						<a href="#" onClick="validateAndsave()" class="btn"><span>Save</span></a>
					</#if>	
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
