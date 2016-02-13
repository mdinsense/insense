<#import "../common/spring.ftl" as spring />
<#import "../common/MacroTemplates.ftl" as mint />
<div id="Ui Testing Setup">
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
$(document).ready(function() {
});

function addText() {
	var test;
	
    $('#txtTable').append("<tr class='lblFieldPair transactional removeText'>\
			<td class='lbl'>Text</td>\
			<td class='input'>\
				<input class='input_small required urlinputTextOrImage' type='text' id='textName' name='textName' value=''>\
				<a class='editLink np' name='addbtn' onClick='removeElements(this)' maxlength='250' width='20' height='20'><span class='icon nmt plxs' ></span>Remove\
			</td>\
		</tr>");
 }
 
function addImage() {
	var test;
	
    $('#imgTable').append("<tr class='lblFieldPair transactional removeImage'>\
			<td class='lbl'>Image</td>\
			<td class='input'>\
				<input class='input_small required urlinputTextOrImage' type='text' id='imageName' name='imageName' value=''>\
				<a class='editLink np' name='addbtn' onClick='removeElements(this)' maxlength='250' width='20' height='20'><span class='icon nmt plxs' ></span>Remove\
			</td>\
		</tr>");
 }

 
function removeElements(btnelem)
{
	 $(btnelem).closest('tr').remove();  
} 



function enableOrDisableText(){
	
	if($("#findText").prop("checked")) {
		
		document.getElementById("textName").disabled = false; 
		$("#textLink").removeClass("disabled");
		$("#textLink").attr('onClick','addText()');
	} else {
		$(".removeText").each(function() {
			 $(this).closest('tr').remove(); 
		 });
		document.getElementById("textName").value = ""; 
		document.getElementById("textName").disabled = true;
		$("#textLink").addClass("disabled");
		$("#textLink").removeAttr("onClick");
	}
}

function enableOrDisableImage(){
	
	if($("#findImage").prop("checked")) {
		
		document.getElementById("imageName").disabled = false; 
		$("#imageLink").removeClass("disabled");
		$("#imageLink").attr('onClick','addImage()');
	} else {
		$(".removeImage").each(function() {
			 $(this).closest('tr').remove(); 
		 });
		document.getElementById("imageName").value = ""; 
		document.getElementById("imageName").disabled = true;
		$("#imageLink").addClass("disabled");
		$("#imageLink").removeAttr("onClick");
	}
}
 
</script>
<head>	
    <script type="text/javascript" src="js/formValidation.js"></script>		
    <script type="text/javascript"> 
    
 $(document).ready(function() {  
		 $("#loginUrlDiv").hide();

    	if($("#environmentCategoryId").val() != null && $("#applicationId").val() != null) {
    		getEnvironmentDetails();
    		if($("#loginId").val() == null){
    			$('#checkSecureLbl').hide();
	   			$('#checkSecureInput').hide();
	   			$("#secureSite").val("OFF");
    		}
    		
    	}
  });
 
  function isNumberKey(evt) {
	    var charCode = (evt.which) ? evt.which : event.keyCode
	    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))
	       return false;

	    return true;
	 }
  
  function clearFormDetailsForEdit(applicationId)
  {
  	document.aform.applicationId.value=applicationId;
  	document.getElementById("TextOrImageSuitId").setAttribute('action',"<@spring.url '/EditApplicationDetails.ftl' />");
  	document.getElementById("TextOrImageSuitId").setAttribute('method',"POST");
  	document.getElementById("TextOrImageSuitId").submit();
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

function isContains(moduleArray,key){
	for(var i=0;i < moduleArray.length;i++) {
		if(moduleArray[i] == key) {
			return true;
		}
	}
	return false;
}
function getEnvironmentCategoryDetails() {
	$('#moduleId').html('');
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
function getEnvironmentDetails() {
	$("#loginUrlDiv").hide();
	var environmentCategoryId =$("#environmentCategoryId").val();
	var applicationId = $("#applicationId").val();
	if ($('#environmentId option').length > 0){
		document.getElementById("environmentId").options.length=0;
	}
	loadEnvironmentForTestingPages(applicationId, environmentCategoryId,"environmentId");
}
$(window).scroll(function () {
    document.cookie='ypos=' + $(window).scrollTop();
});

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
	if((isEmpty(document.aform.loginId) || document.aform.loginId.value < 0 || document.aform.loginId.value == "") && document.aform.secureSite.value =="ON" ) {
		alert('Please Select Login Id');
		document.aform.loginId.focus();
		return false;
	}
	
	if($("#findText").prop("checked") || $("#findImage").prop("checked")) {
		if($("#findText").prop("checked")) {
			var isValid = 1;
			$("#textName").each(function() {
				if($(this).val() == null || $(this).val() == "") {
					alert('TextName Field Missing');
					isValid = 0;
				}
			});

			if(isValid == 0) {
				return false;
			}
		}
		
		if($("#findImage").prop("checked")) {
			var isValid1 = 1;
			$("#imageName").each(function() {
				if($(this).val() == null || $(this).val() == "") {
					alert('ImageName Field Missing');
					isValid1 = 0;
				}
			});

			if(isValid1 == 0) {
				return false;
			}
		}
	}else{
		alert('Please Select Atleast One from FindText or FindImage');
		return false;
	}
		
	
		
	
	if(document.aform.suitId.value != "" && document.aform.suitId.value > 0) {
		document.getElementById("TextOrImageSuitId").setAttribute('action',"<@spring.url '/UpdateTextOrImageSuit.ftl' />");
	} else {
		document.getElementById("TextOrImageSuitId").setAttribute('action',"<@spring.url '/SaveTextOrImageSuit.ftl' />");
	}
	
	document.getElementById("TextOrImageSuitId").submit();
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
	$("#loginUrlDiv").html('');
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
	    	   $("#loginUrlDiv").html("&nbsp;&nbsp;<b>Login/Home Url : </b>"+val);
	    	   $("#loginUrlDiv").show();
	   	});
	});
		
	request.fail(function( jqXHR, textStatus ){  
	});
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
						"mint.help.FindTextOrImage"/></b><a href="<@spring.message 'mint.help.mailTo'/>"
						target="_top"> <@spring.message "mint.help.mailId"/></a>
				</div>
			</div>
			<hr style="margin-top: 0px;">
			<!--  table view one for application and category -->	

        <form modelAttribute="testingForm"  action='SaveTextOrImageSuit' id="TextOrImageSuitId" name="aform" target="_top" method="POST">
        <input type="hidden" id="secureSite" name="secureSite" value="ON">
        <input type="hidden" name="checkText" id="checkText" value="ON"> 
        <input type="hidden" name="checkImage" id="checkImage" value="OFF">
        
        <#if testingForm?exists && testingForm.suitId?exists>
			<input type="hidden" id="suitId" name="suitId" value="${testingForm.suitId!}">
		<#else>
			<input type="hidden" id="suitId" name="suitId" value="">
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
						<td style="padding:10px">
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
											<select id="environmentCategoryId" name="environmentCategoryId" onchange="getEnvironmentDetails();loadLoginUserDetails();">
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
							<h6>Find Image/Text</h6>
						</td>
					</tr>
					
					<tr class="bd">
						<td style="padding:10px">
								<table class="bd rowheight35 sfhtTable" id="containertbl">
								<tr class="lblFieldPair ">
									<td class="lbl">
										<input type="checkbox" id="findText" name="findText" onclick="enableOrDisableText()">
										&nbsp;&nbsp;&nbsp;Find Text<font color="red" size=-1>&nbsp;*</font>&nbsp;&nbsp;&nbsp;
									</td>
									<td class="input">
										<input type="checkbox" id="findImage" name="findImage" onclick="enableOrDisableImage()">Find Image<font color="red" size=-1>&nbsp;*</font>
									</td>
								</tr>
								</table>
								<table class="bd rowheight35 sfhtTable" id="txtTable">
								<tr><td colspan='50'><div style='float:left; width: 45%;'><hr/></div><div style='float:right; width: 45%;'><hr/></div><b>Text Section</b></td></tr>
								<#if suitTextImageXrefList??>
								<#assign xrefCount = 1>
								<#assign isTextPresent = 0>
									<#list suitTextImageXrefList! as suitTextImageXref>
										<#if suitTextImageXref.text>
										<#assign isTextPresent = 1>
										<script>document.getElementById("findText").checked = true; </script>
											<#if xrefCount ==1>
											<tr class="lblFieldPair">
											<#else>
											<tr class="lblFieldPair removeText">
											</#if>
												<td class="lbl">
													<span id="txtlblTable">Text</span>
												</td>
												<td class="input">
												<#if xrefCount ==1>
													<input class="input_small textNameCls urlinputTextOrImage" type="text" id="textName" name="textName" maxlength="250" value="${suitTextImageXref.textOrImageName!}" style="width:1200px;">
													<a class="editLink np" name="textLink" id="textLink" onClick="addText()" width="20" height="20">
													<span class="icon nmt plxs "></span>Add</td>
													<#assign xrefCount = 0>
												<#else>
													<input class="input_small textNameCls urlinputTextOrImage" type="text" id="textName" name="textName" maxlength="250" value="${suitTextImageXref.textOrImageName!}" style="width:1200px;">
													<a class="editLink np" name="addbtn" onClick="removeElements(this)" width="20" height="20">
													<span class="icon nmt plxs "></span>Remove</td>
												</#if>
												
											</tr>
										</#if>
									</#list>
									<#if isTextPresent ==0>
										<tr class="lblFieldPair ">
											<td class="lbl">
												<span id="txtlblTable">Text</span>
											</td>
											<td class="input">
											<input class="input_small textNameCls urlinputTextOrImage" type="text" id="textName" name="textName" value="" maxlength="250" disabled="disabled" style="width:1200px;">
											<a class="editLink np disabled" name="textLink" id="textLink" width="20" height="20" >
											<span class="icon nmt plxs "></span>Add</td>
										</tr>
									</#if>
								<#else>
									<tr class="lblFieldPair ">
										<td class="lbl">
											<span id="txtlblTable">Text</span>
										</td>
										<td class="input">
										<input class="input_small textNameCls urlinputTextOrImage" type="text" id="textName" name="textName" value="" maxlength="250" disabled="disabled" style="width:1200px;">
										<a class="editLink np disabled" name="textLink" id="textLink" width="20" height="20" >
										<span class="icon nmt plxs "></span>Add</td>
									</tr>
								</#if>
								</table>
								<table class="bd rowheight35 sfhtTable" id="imgTable">
								<tr><td colspan='50'><div style='float:left; width: 45%;'><hr/></div><div style='float:right; width: 45%;'><hr/></div><b>Image Section</b></td></tr>
								<#if suitTextImageXrefList??>
								<#assign xrefCount = 1>
								<#assign isImagePresent = 0>
									<#list suitTextImageXrefList! as suitTextImageXref>
										<#if suitTextImageXref.image>
										<#assign isImagePresent = 1>
										<script>document.getElementById("findImage").checked = true; </script>
											<#if xrefCount ==1>
											<tr class="lblFieldPair">
											<#else>
											<tr class="lblFieldPair removeImage">
											</#if>
												<td class="lbl">
													<span id="txtlblTable">Image</span>
												</td>
												<td class="input">
												
												<#if xrefCount ==1>
													<input class="input_small imageNameCls urlinputTextOrImage" type="text" id="imageName" name="imageName" maxlength="250" value="${suitTextImageXref.textOrImageName!}" style="width:1200px;">
													<a class="editLink np" name="addbtn" id="imageLink"  onClick="addImage()" width="20" height="20">
													<span class="icon nmt plxs "></span>Add</td>
													<#assign xrefCount = 0>
												<#else>
													<input class="input_small imageNameCls urlinputTextOrImage" type="text" id="imageName" name="imageName" maxlength="250" value="${suitTextImageXref.textOrImageName!}" style="width:1200px;">
													<a class="editLink np" name="addbtn" onClick="removeElements(this)" width="20" height="20">
													<span class="icon nmt plxs "></span>Remove</td>
												</#if>
												
											</tr>
										</#if>
										
									</#list>
									<#if isImagePresent ==0>
											<tr class="lblFieldPair ">
												<td class="lbl">
													<span id="txtlblTable">Image</span>
												</td>
												<td class="input">
												<input class="input_small imageNameCls urlinputTextOrImage" type="text" id="imageName" name="imageName" value="" maxlength="250" style="width:1200px;" disabled="disabled">
												<a class="editLink np disabled" name="addbtn" id="imageLink" width="20" height="20" >
												<span class="icon nmt plxs "></span>Add</td>
											</tr>
									</#if>
								<#else>
									<tr class="lblFieldPair ">
										<td class="lbl">
											<span id="txtlblTable">Image</span>
										</td>
										<td class="input">
										<input class="input_small imageNameCls urlinputTextOrImage" type="text" id="imageName" name="imageName" value="" maxlength="250" style="width:1200px;" disabled="disabled">
										<a class="editLink np disabled" name="addbtn" id="imageLink" width="20" height="20" >
										<span class="icon nmt plxs "></span>Add</td>
									</tr>
								</#if>
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
