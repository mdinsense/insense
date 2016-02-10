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
</script>
<head>	
<script type="text/javascript" src="js/formValidation.js"></script>		
<script type="text/javascript"> 
$(document).ready(function() {  
		$(".fileupload").hide();
		enableSection("findImageId",1);
		enableSection("includeExcludeId",2);
		enableSection("removeTagId",3);
		document.getElementById("rsaSecured").checked = true;
		document.getElementById("staticFlag").checked = true;
		document.getElementById("secureFlag").checked = true;
 });
function CopyMe(oFileInput, sTargetID) {
		var arrTemp = oFileInput.value.split('\\');
		$("#"+sTargetID).val(arrTemp[arrTemp.length - 1]);
}
function onUploadClick(fileTarget) {
		$("#"+fileTarget).click();
}
function CheckSecureSite() {
	if ($('#secureFlag').prop('checked')) {
		document.getElementById("secureFlag").checked = true;
		$(".loginScriptUpload").show();
	} else{
		document.getElementById("secureFlag").checked = false;
		$("#loginScriptFile").val("");
		$("#fileUrlPath").val("");
		$(".loginScriptUpload").hide();
	}
}
function CheckStaticUrl() {
	$(".staticUrlList").hide();
	if($("#staticFlag").prop('checked')) {
		$(".staticUrlList").show();
	}
}
function addRow(rowTypeId, targetId) {
	if(isAddAllowed) {
		switch (rowTypeId) {
	    case 1:
	    	row = getTextRow();
	        break;
	    case 2:
	    	row = getImageRow();
	        break;
	    case 3:
	    	row = getIncludeUrlRow();
	        break;
	    case 4:
	    	row = getExcludeUrlRow();
	        break;
	    case 5:
	    	row = getRemoveTagRow();
	        break;
	    case 6:
	    	row = getRemoveTextRow();
	        break;
		}
		$("#"+targetId).append(row);
	}
}
function isAddAllowed(rowTypeId) {
	switch (rowTypeId) {
	    case 1:
	    	return $("#findText").prop("chekced");
	        break;
	    case 2:
	    	return $("#findImage").prop("chekced");
	        break;
	    case 3:
	    	return $("#include").prop("chekced");
	        break;
	    case 4:
	    	return $("#exclude").prop("chekced");
	        break;
	    case 5:
	    	return $("#tag").prop("chekced");
	        break;
	    case 6:
	    	return $("#text").prop("chekced");
	        break;
	}
}
function getTextRow() {
	var row = "<tr class='transactional textClass'><td class='lbl'>Image &nbsp;</td><td class='input'>";
		row += "<input class='input_small required ' type='text' id='textName' style='width:930px;' name='textName' value=''>";
		row += "<a class='editLink np' name='addbtn' onClick='removeElements(this)' maxlength='250' width='20' height='20'><span class='icon nmt plxs' ></span>x"
		row += "</td></tr>"
	return 	row;
} 
function getImageRow() {
	var row = "<tr class='transactional imageClass'><td class='lbl'>Text &nbsp;</td><td class='input'>";
	row += "<input class='input_small required ' type='text' id='textName' style='width:930px;' name='textName' value=''>";
	row += "<a class='editLink np' name='addbtn' onClick='removeElements(this)' maxlength='250' width='20' height='20'><span class='icon nmt plxs' ></span>x"
	row += "</td></tr>"
	return 	row;
}
function getRemoveTagRow() {
	var row = "<tr class='transactional removeTagClass'><td class='lbl'>Tag &nbsp;</td><td class='input'>";
	row += "<input class='input_small required ' type='text' id='removeTag' style='width:930px;' name='removeTag' value=''>";
	row += "<a class='editLink np' name='addbtn' onClick='removeElements(this)' maxlength='250' width='20' height='20'><span class='icon nmt plxs' ></span>x"
	row += "</td></tr>"
	return 	row;
}
function getRemoveTextRow() {
	var row = "<tr class='transactional removeTextClass'><td class='lbl'>Text &nbsp;</td><td class='input'>";
	row += "<input class='input_small required ' type='text' id='removeText' style='width:930px;' name='removeText' value=''>";
	row += "<a class='editLink np' name='addbtn' onClick='removeElements(this)' maxlength='250' width='20' height='20'><span class='icon nmt plxs' ></span>x"
	row += "</td></tr>"
	return 	row;
}
function getIncludeUrlRow() {
	var row = "<tr class='transactional includeClass'><td class='lbl'>Include Url</td><td class='input'>";
	row += "<input class='input_small required' type='text' id='includeUrl' style='width:930px;' name='includeUrl' value=''>";
	row += "<a class='editLink np' name='addbtn' onClick='removeElements(this)' maxlength='250' width='20' height='20'><span class='icon nmt plxs' ></span>x"
	row += "</td></tr>"
	return 	row;
}
function getExcludeUrlRow() {
	var row = "<tr class='transactional excludeClass'><td class='lbl'>Exclude Url</td><td class='input'>";
	row += "<input class='input_small required ' type='text' id='excludeUrl' style='width:930px;' name='excludeUrl' value=''>";
	row += "<a class='editLink np' name='addbtn' onClick='removeElements(this)' maxlength='250' width='20' height='20'><span class='icon nmt plxs' ></span>x"
	row += "</td></tr>"
	return 	row;
}
function removeElements(btnelem) {
	 $(btnelem).closest('tr').remove();  
}
function enableOrDisable(elem,target,link,targetClass){
	if($("#"+elem).prop("checked")) {
		document.getElementById(target).disabled = false; 
		$("#"+link).removeClass("disabled");
	} else {
		$("."+targetClass).each(function() {
			 $(this).closest('tr').remove(); 
		 });
		document.getElementById(target).value = ""; 
		document.getElementById(target).disabled = true;
		$("#"+link).addClass("disabled");
	}
}
function enableRsa() {
	$("#securityAnswer").prop("disabled",true);
	if($("#rsaSecured").prop("checked")) {
		document.getElementById("rsaSecured").checked = true;
		$("#securityAnswer").prop("disabled",false)
	}
} 
function enableSection(elem,target) {
	if($("#"+elem).prop("checked")) {
		$("."+target).show();
	} else {
		$("."+target).hide();
	}
}
function setCompareType(){
	if($("#quickTest").prop("checked")) 
	{
		document.getElementById("textCompare").checked = true;
		document.getElementById("htmlCompare").checked = false;
		document.getElementById("screenCompare").checked = false;
	} else if($("#comprehensiveTest").prop("checked")) {
		document.getElementById("textCompare").checked = true;
		document.getElementById("htmlCompare").checked = true;
		document.getElementById("screenCompare").checked = true;
	}
}
function validateAndsave() {
	if($("#suitName").val() == "") {
		alert("Suit name cannot be blank");
		return false;
	}
	if($("#applicationName").val() == "") {
		alert("Application name cannot be blank");
		return false;
	}
	if($("#environmentName").val() == "") {
		alert("environmentName name cannot be blank");
		return false;
	}
	if($("#loginOrHomeUrl").val() == "") {
		alert("loginOrHomeUrl cannot be blank");
		return false;
	}
	if($("#staticFlag").prop("checked")) {
		if($("#staticUrlFile").val() == "") {
			alert("Please choose staticUrl File to continue");
			return false;
		}
		if($("#columnPosition").val() == "") {
			alert("Please provide column Position to continue");
			return false;
		}
	}
	if($("#secureFlag").prop("checked")) {
		if($("#loginScriptFile").val() == "") {
			alert("Please choose loginScript File to continue");
			return false;
		}
		if($("#loginId").val() == "") {
			alert("loginId cannot be blank");
			return false;
		}
		if($("#password").val() == "") {
			alert("password cannot be blank");
			return false;
		}
		if($("#rsaSecured").prop("checked")) {
			if($("#securityAnswer").val() == "") {
				alert("Please provide securityAnswer to continue");
				return false;
			}
		}
	} 
	document.getElementById("testSuitHomeForm").setAttribute('action',"<@spring.url '/SaveSuit.ftl' />");
	document.getElementById("testSuitHomeForm").submit();
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
						"mint.help.functional"/></b><a href="<@spring.message 'mint.help.mailTo'/>"
						target="_top"> <@spring.message "mint.help.mailId"/></a>
				</div>
			</div>
			<hr style="margin-top: 0px;">
			<!--  table view one for application and category -->	

        <form modelAttribute="TestSuitForm" action='/SaveSuit.ftl'  id="testSuitHomeForm" name="testSuitHomeForm" enctype="multipart/form-data" target="_top" method="POST">
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
										<input class="required" type="text" id="suitName" name="suitName" value="">
									</td>
								</tr>
								<tr class="lblFieldPair">
										<td class="lbl">Application Name</td>
										<td class="input">
											<input class="required" type="text" id="applicationName" name="applicationName" value="">
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
							<h6>Environment Details</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<table cellpadding='0' cellspacing='0' border='0' width='100%'>
									<tr class="bd">
										<td style="padding: 5">
											<body onload="CheckSecureSite()">
													<table class="bd rowheight35">
													    <br/>
														
														<tr class="lblFieldPair">
															<td class="lbl">Environment Name</td>
															<td class="input">
																<input class="required" type="text" id="environmentName" name="environmentName" value="" maxlength="100">
															</td>
														</tr>
														<tr class="lblFieldPair">
															<td class="lbl">Login/Home Url</td>
															<td class="input">
																<input class="required urlinput" type="text"	id="loginOrHomeUrl"  name="loginOrHomeUrl" value="" maxlength="150">
															</td>
														</tr>
														<tr class="lblFieldPair">
															<td class="lbl">Is Secure Site</td>
															<td class="input">
																<input type="checkbox" id="secureFlag" name="secureFlag" onclick="CheckSecureSite()">
															</td>
														</tr>
														<tr class="lblFieldPair loginScriptUpload">
															<td class="lbl">Login Script</td>
															<td class="input">
																<input class="input_small" type="text" id="fileUrlPath" name="fileUrlPath" value=""> 
																<input class="fileupload" type="file" id="loginScriptFile" onchange="CopyMe(this, 'fileUrlPath')" name="loginScriptFile"> 
																<input type="button" class="uploadbutton" id="loginScriptButton" onClick="onUploadClick('loginScriptFile')" value="Upload"></td>
															</td> 
														</tr>
														<tr class="lblFieldPair">
															<td class="lbl">Is Static Url</td>
															<td class="input">
																<input type="checkbox" id="staticFlag" name="staticFlag" onclick="CheckStaticUrl()">
															</td>
														</tr>
														<tr class="lblFieldPair staticUrlList">
															<td class="lbl">Upload Static Url List</td>
															<td class="input">
																<input class="input_small" type="text" class="required" id="staticfileUrlPath" name="staticfileUrlPath" value="""> 
																<input type="file" class="file fileupload" id="staticUrlFile" onchange="CopyMe(this, 'staticfileUrlPath')" name="staticUrlFile"> 
																<input type="button" class="uploadbutton" id="staticUrlButton" onClick="onUploadClick('staticUrlFile')" value="Upload"></td>
															</td> 
														</tr>
														<tr class="lblFieldPair staticUrlList">
															<td class="lbl">Url Column Position</td>
															<td class="input">
																<input class="input_small" type="text" class="required" name="columnPosition" id="columnPosition" value="">
															</td>
														</tr>
												</table>
										</td>
									</tr>
								</table>
						</td>
					</tr>
				</table>
			</div>
		</section>

		<section class="mtlg loginScriptUpload">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Login details</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<table cellpadding='0' cellspacing='0' border='0' width='100%'>
									<tr class="bd">
										<td style="padding: 5">
										<body>
													<table class="bd rowheight35 sfhtTable" id="containertbl">
														<tr class="lblFieldPair">
															<td class="lbl">Login id</td>
															<td class="input">
																<input type="text" class="required" value="" name="loginId" id="loginId">
															</td>
														</tr>
														<tr class="lblFieldPair">
															<td class="lbl">Password</td>
															<td class="input">
																<input type="password" class="required" value="" name="password" id="password">
															</td>
														</tr>
														<tr class="lblFieldPair">
															<td class="lbl">RSA Enabled</td>
															<td class="input">
																<input type="checkbox" id="rsaSecured" name="rsaSecured" onclick="enableRsa()">
															</td>
														</tr>
														<tr class="lblFieldPair">
															<td class="lbl">Security Answer</td>
															<td class="input">
																<input type="text" value="" name="securityAnswer" id="securityAnswer">
															</td>
														</tr>
													</table>
													</form>
											</body>
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
							<h6><input type="checkbox" id="findImageId" onclick="enableSection(this.id,1)">&nbsp; Find Image/text</h6>
						</td>
					</tr>
					<tr class="bd 1">
						<td style="padding:10px">
								<table cellpadding='0' cellspacing='0' width="100%;">				
					<tr class="bd">
						<td style="padding:10px">
								<table class="bd rowheight35 sfhtTable" id="containertbl">
								<tr class="lblFieldPair">
									<td class="lbl">
										<input type="checkbox" id="findText" name="findText" onclick="enableOrDisable(this.id,'textName','textLink','textClass')">
										&nbsp;&nbsp;&nbsp;Find Text<font color="red" size=-1>&nbsp;*</font>&nbsp;&nbsp;&nbsp;
									</td>
									<td class="input">
										<input type="checkbox" class="required" id="findImage" name="findImage" onclick="enableOrDisable(this.id,'imageName','imageLink','imageClass')">Find Image
									</td>
								</tr>
								</table>
								<table class="bd rowheight35 sfhtTable" id="txtTable">
								<tr><td colspan='50'><div style='float:left; width: 45%;'><hr/></div><div style='float:right; width: 45%;'><hr/></div><b>Image Section</b></td></tr>
									<tr class=" ">
										<td class="lbl">
											<span id="txtlblTable">Image  &nbsp;</span>
										</td>
										<td class="input">
										<input class="input_small textNameCls " type="text" id="textName" name="textName" value="" maxlength="250" disabled="disabled" style="width:930px;">
										<a class="editLink np disabled" name="textLink" id="textLink" width="20" height="20" onClick="addRow(1, 'txtTable')">
										<span class="icon nmt plxs "></span>+</td>
									</tr>
								</table>
								<table class="bd rowheight35 sfhtTable" id="imgTable">
								<tr><td colspan='50'><div style='float:left; width: 45%;'><hr/></div><div style='float:right; width: 45%;'><hr/></div><b>Text Section</b></td></tr>
										<tr class=" ">
											<td class="lbl">
												<span id="txtlblTable">Text &nbsp;</span>
											</td>
											<td class="input">
											<input class="input_small" type="text" id="imageName" name="imageName" value="" maxlength="250" style="width:930px;" disabled="disabled">
											<a class="editLink np" name="addbtn" id="imageLink" width="20" height="20" onClick="addRow(2, 'imgTable')">
											<span class="icon nmt plxs "></span>+</td>
										</tr>
									</table>
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
							<h6><input type="checkbox" id="includeExcludeId" onclick="enableSection(this.id,2)">&nbsp;IncludeExcludeUrl Setup</h6>
						</td>
					</tr>
					<tr class="bd 2">
					<td style="padding:10px">
					<table cellpadding='0' cellspacing='0' width="100%;">				
					<tr class="bd">
						<td style="padding:10px">
								<table class="bd rowheight35 sfhtTable" id="containertbl">
								<tr class="lblFieldPair">
									<td class="lbl">
										<input type="checkbox" id="include" name="include" onclick="enableOrDisable(this.id,'includeUrl','includeLink','includeClass')">
										&nbsp;&nbsp;&nbsp;Include<font color="red" size=-1>&nbsp;*</font>&nbsp;&nbsp;&nbsp;
									</td>
									<td class="input">
										<input type="checkbox" id="exclude" name="exclude" onclick="enableOrDisable(this.id,'excludeUrl','excludeLink','excludeClass')">Exclude<font color="red" size=-1>&nbsp;*</font>
									</td>
								</tr>
								</table>
								<table class="bd rowheight35 sfhtTable" id="incTable">
								<tr><td colspan='50'><div style='float:left; width: 45%;'><hr/></div><div style='float:right; width: 45%;'><hr/></div><b>Include Section</b></td></tr>
									<tr class=" ">
										<td class="lbl">
											<span id="txtlblTable">Include Url  &nbsp;</span>
										</td>
										<td class="input">
										<input class="input_small textNameCls " type="text" id="includeUrl" name="includeUrl" value="" maxlength="250" disabled="disabled" style="width:930px;">
										<a class="editLink np disabled" name="textLink" id="includeLink" width="20" height="20" onClick="addRow(3, 'incTable')">
										<span class="icon nmt plxs "></span>+</td>
									</tr>
								</table>
								<table class="bd rowheight35 sfhtTable" id="excTable">
								<tr><td colspan='50'><div style='float:left; width: 45%;'><hr/></div><div style='float:right; width: 45%;'><hr/></div><b>Exclude Url</b></td></tr>
										<tr class=" ">
											<td class="lbl">
												<span id="txtlblTable">Exclude Url &nbsp;</span>
											</td>
											<td class="input">
											<input class="input_small" type="text" id="excludeUrl" name="excludeUrl" value="" maxlength="250" style="width:930px;" disabled="disabled">
											<a class="editLink np" name="addbtn" id="excludeLink" width="20" height="20" onClick="addRow(4, 'excTable')">
											<span class="icon nmt plxs "></span>+</td>
										</tr>
									</table>
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
							<h6><input type="checkbox" id="removeTagId" onclick="enableSection(this.id,3)">&nbsp; RemoveTag/Text</h6>
						</td>
					</tr>
					<tr class="bd 3">
						<td style="padding:10px">
								<table cellpadding='0' cellspacing='0' width="100%;">				
					<tr class="bd">
						<td style="padding:10px">
								<table class="bd rowheight35 sfhtTable" id="containertbl">
								<tr class="lblFieldPair">
									<td class="lbl">
										<input type="checkbox" id="tag" name="tag" onclick="enableOrDisable(this.id,'removeTag','tagLink','removeTagClass')">
										&nbsp;&nbsp;&nbsp;RemoveTag<font color="red" size=-1>&nbsp;*</font>&nbsp;&nbsp;&nbsp;
									</td>
									<td class="input">
										<input type="checkbox" id="text" name="text" onclick="enableOrDisable(this.id,'removeText','textLink','removeTextClass')">RemoveText<font color="red" size=-1>&nbsp;*</font>
									</td>
								</tr>
								</table>
								<table class="bd rowheight35 sfhtTable" id="tagTbl">
								<tr><td colspan='50'><div style='float:left; width: 45%;'><hr/></div><div style='float:right; width: 45%;'><hr/></div><b>Tag Section</b></td></tr>
									<tr class=" ">
										<td class="lbl">
											<span id="txtlblTable">Tag  &nbsp;</span>
										</td>
										<td class="input">
										<input class="input_small textNameCls " type="text" id="removeTag" name="removeTag" value="" maxlength="250" disabled="disabled" style="width:930px;">
										<a class="editLink np disabled" name="tagLink" id="tagLink" width="20" height="20" onClick="addRow(5, 'tagTbl')">
										<span class="icon nmt plxs "></span>+</td>
									</tr>
								</table>
								<table class="bd rowheight35 sfhtTable" id="rtextTbl">
								<tr><td colspan='50'><div style='float:left; width: 45%;'><hr/></div><div style='float:right; width: 45%;'><hr/></div><b>Text Section</b></td></tr>
										<tr class=" ">
											<td class="lbl">
												<span id="txtlblTable">Text &nbsp;</span>
											</td>
											<td class="input">
											<input class="input_small" type="text" id="removeText" name="removeText" value="" maxlength="250" style="width:930px;" disabled="disabled">
											<a class="editLink np" name="addbtn" id="textLink" width="20" height="20" onClick="addRow(6, 'rtextTbl')">
											<span class="icon nmt plxs "></span>+</td>
										</tr>
									</table>
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
										<select id="browserTypeId" name="browserTypeId">
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

		<!-- table view 3 for comparison selection -->
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Choose Comparison Strategy</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<table class="bd rowheight35">
								<tr class="lblFieldPair ">
									<td class="lbl">
										<input type="radio" name="CompareType" id="quickTest" checked="checked" onclick="setCompareType()">
										&nbsp;&nbsp;&nbsp;Quick Test<font color="red" size=-1>&nbsp;*</font>&nbsp;&nbsp;&nbsp;
									</td>
									<td class="input">
										<input type="radio" name="CompareType" id="comprehensiveTest" onclick="setCompareType()">Comprehensive Test<font color="red" size=-1>&nbsp;*</font>
									</td>
								</tr>
								<tr class="lblFieldPair">
									<td style='width:20%;padding:5' class='input'>
										<input type="checkbox" id="textCompare" name="textCompare" checked="checked" disabled>Text Compare (View Screen shot)
									</td>
									<td style='width:20%;padding:5' class='input'>
										<input type="checkbox" id="htmlCompare" name="htmlCompare" disabled>Html Compare
									</td>
									<td style='width:20%;padding:5' class='input'>
										<input type="checkbox" id="screenCompare" name="screenCompare" disabled>Image Compare
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
						<a href="#" onClick="validateAndsave()" class="btn"><span>Save</span></a>
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
