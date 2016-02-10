<script type="text/javascript">
	$(document).ready(function() {
		$('.createEnvironment').hide()
		$("#environmentCategoryId").attr('disabled','disabled');
		$('#loginScriptFile').hide();
		$("#loginScriptButton").click(function() {
			$('#loginScriptFile').click();
		});
		// for environment edit
		if($("#isEnvironmentEdit").val() == 1) {
			$("#applicationId").attr('disabled','disabled');
			$('.createEnvironment').show();
			$('.selectEnvironment').hide();
		}
		// to show secure site or public site
		<#if uiTestingSetupForm.secureSite == false>
			document.SetupEnvironmentForm.iSecureSite.checked = false;
			$(".fileupload").hide();
		</#if>
	});
	function CopyMe(oFileInput, sTargetID) {
		var arrTemp = oFileInput.value.split('\\');
		document.SetupEnvironmentForm.fileUrlPath.value = arrTemp[arrTemp.length - 1];
		document.SetupEnvironmentForm.fileUrlPath.title = arrTemp[arrTemp.length - 1];
	}
	function isSecureSite() {
		if ($('#iSecureSite').prop('checked')) {
			$(".fileupload").show();
		} else{
			document.SetupEnvironmentForm.loginScriptFile.value = "";
			document.SetupEnvironmentForm.fileUrlPath.value = "";
			$(".fileupload").hide();
		}
	}
	function validateAndsave() {
		
		$("#environmentCategoryId").attr('disabled',false);
		
		if($('#isEnvironmentEdit').val() == 1) {
			return updateEnvironment();
		}
		
		if( $('#applicationId').val() == -1) {
			alert('Please Select Application');
			return false;
		}
		
		if($("#saveEnvironment").val() == 'true') {
			if(!validateSaveOrUpdateEnvironment()) {
				return false
			}
			if( $('#environmentCategoryId').val() == -1) {
				alert('Please Select Environment Category');
				return false;
			}
		} else {
			if( $('#environmentId').val() == -1) {
				alert('Please Select Environment');
				return false;
			}
		}
		
		enableLoader();
		document.getElementById("SetupEnvironmentForm").setAttribute('action',"<@spring.url '/SaveEnvironment.ftl' />");
		submitForm();
	}
	
	function submitForm() {
		try {
			 document.getElementById("SetupEnvironmentForm").submit();
		    } catch (e) {
		        try {
		        	document.getElementById("SetupEnvironmentForm").submit();
		        } catch (e) {
		            try {
		            	document.getElementById("SetupEnvironmentForm").submit();
		            } catch (e) {
		                alert(e);
		            }
		        }
		    }
	}

	function deleteEnvironment(environmentId, applicationId)
	{
		if(confirm("Confirm delete of environment mapping details"))
		{
			document.getElementById("SetupEnvironmentForm").setAttribute('action',"<@spring.url '/DeleteEnvironment.ftl' />");
			document.getElementById("environmentId").value = environmentId;
			document.getElementById("applicationId").value = applicationId;
			document.getElementById("SetupEnvironmentForm").setAttribute('method',"POST");
			document.getElementById("SetupEnvironmentForm").submit();
		}
	}
	function editEnvironment(environmentId,applicationId,environmentCategoryId)
	{
		$("#applicationId").attr('disabled',false);
		$("#environmentCategoryId").attr('disabled',false);
		document.SetupEnvironmentForm.environmentId.value=environmentId;
		document.SetupEnvironmentForm.applicationId.value=applicationId;
		document.SetupEnvironmentForm.environmentCategoryId.value=environmentCategoryId;
		document.getElementById("SetupEnvironmentForm").setAttribute('action',"<@spring.url '/EditEnvironmentDetails.ftl' />");
		document.getElementById("SetupEnvironmentForm").setAttribute('method',"POST");
		document.getElementById("SetupEnvironmentForm").submit();
	}
	function showCreateEnvironment(){
		$(".createEnvironment").show();
		$(".selectEnvironment").hide();
		$("#environmentCategoryId").attr('disabled',false);
		document.SetupEnvironmentForm.environmentId.value='';
		document.SetupEnvironmentForm.iSecureSite.checked = true;
		document.SetupEnvironmentForm.saveEnvironment.value = true;
	}
	function updateEnvironment() {
		if(!validateSaveOrUpdateEnvironment()) {
			return false
		}
		$("#applicationId").attr('disabled',false);
		enableLoader();
		document.getElementById("SetupEnvironmentForm").setAttribute('action',"<@spring.url '/UpdateEnvironment.ftl' />");
		submitForm();
	}
	function validateSaveOrUpdateEnvironment() {

		if ( $('#environmentName').val() == '') {
			alert('Please Enter environment name');
			return false;
		}
		if(!specialCharValidate(document.SetupEnvironmentForm.environmentName.value)){
			alert('Special Characters are not allowed');
			document.SetupEnvironmentForm.environmentName.focus();
			return false;
		}else if(!textAlphanumeric(document.SetupEnvironmentForm.environmentName.value)){
			alert('Environment Name is not alphanumeric');
			document.SetupEnvironmentForm.environmentName.focus();
			return false;
		}
	
		if ( $('#loginOrHomeUrl').val() == '') {
			alert('Please Enter Login/Home URL');
			return false;
		}
		
		if ($('#iSecureSite').prop('checked')) {
			document.SetupEnvironmentForm.secureSite.value = true;
				/*if(document.SetupEnvironmentForm.loginScriptFile.value == "") {
					alert('Please select login script');
					document.SetupEnvironmentForm.loginScriptFile.focus();
					return false;
				}*/
		} else{
			document.SetupEnvironmentForm.secureSite.value = false;
		}
		
		return true;
	}
	function getEnvironmentCategoryDetails() {
		var environmentId = $('#environmentId').val();
		getEnvironmentCategory(environmentId,"environmentCategoryId");
	}
	function clearAll() {
		document.SetupEnvironmentForm.applicationId.value=-1;
		document.SetupEnvironmentForm.environmentId.value=-1;
		document.SetupEnvironmentForm.environmentCategoryId.value=-1;
		document.SetupEnvironmentForm.environmentName.value="";
		document.SetupEnvironmentForm.loginOrHomeUrl.value="";
		document.SetupEnvironmentForm.fileUrlPath.value="";
		document.SetupEnvironmentForm.secureSite.value=false;
		$("#applicationId").attr('disabled',false);
		$("#environmentCategoryId").attr('disabled',true);
		$('.createEnvironment').hide();
		$('.selectEnvironment').show();
	}
</script>
<section class="drop mtlg">
	<div class="cm">
	
	<table cellpadding='0' cellspacing='0' border='0' width='100%'>
		<tr>
			<td class="hd">
				<h3>Setup Environment</h3>
			</td>
		</tr>
		<tr class="bd">
			<td style="padding: 5">
				<body onload="isSecureSite()">
					<form modelAttribute="envirornmentForm"
						action='/SaveEnvironment.ftl' id="SetupEnvironmentForm" name="SetupEnvironmentForm"
						enctype="multipart/form-data" target="_top" method="POST"
						onSubmit="return validateAndsave();">
						<input type="hidden" id="setupTabNumber" name="setupTabNumber" value="2">
						<input type="hidden" id=secureSite name="secureSite" value="false">
						<input type="hidden" id="saveEnvironment" name="saveEnvironment" value="false">
						<input type="hidden" id="isEnvironmentEdit" name="isEnvironmentEdit" value=${uiTestingSetupForm.isEnvironmentEdit}>
						<table class="bd rowheight35">
						    <br/>
							<tr class="lblFieldPair">
								<td class="lbl">Application Name</td>
								<td class="input">
									<select id="applicationId" name="applicationId">
										<option value="-1">----------Select-------------</option>
										<#list applications! as application> 
											<#if uiTestingSetupForm.applicationId?exists && uiTestingSetupForm.applicationId == application.applicationId>
												<option value="${application.applicationId}" selected="true">${application.applicationName}</option>
											<#else>
												<option value="${application.applicationId}">${application.applicationName}</option>
											</#if> 
										</#list>
									</select>
								</tr>
							</tr>
							<tr class="lblFieldPair selectEnvironment">
								<td class="lbl">Environment Name</td>
								<td class="input"><select id="environmentId" name="environmentId" onchange="getEnvironmentCategoryDetails();">
										<option value="-1" selected="true">----------Select-------------</option>
										<#list environmentList! as environment>
												<#if uiTestingSetupForm.environmentId?exists && uiTestingSetupForm.environmentId == environment.environmentId>
													<option value="${environment.environmentId!}" selected="true">${environment.environmentName!}</option>
													<script type="text/javascript">getEnvironmentCategoryDetails();</script>
												<#else>
													<option value="${environment.environmentId!}">${environment.environmentName!}</option>
												</#if> 
										</#list>
								</select> &nbsp;&nbsp; <input type="button" value="Add New Environment"
									onclick="showCreateEnvironment();"></td>
								</td>
							</tr>
							
							<tr class="lblFieldPair createEnvironment">
								<td class="lbl">Environment Name</td>
								<td class="input">
									<input class="required" type="text" id="environmentName" name="environmentName" value="${uiTestingSetupForm.environmentName!}" maxlength="100">&nbsp;&nbsp;
									<input type="button" value="Select Environment" onclick="loadTabData(2);"></td>
								</td>
							</tr>
							<tr class="lblFieldPair">
								<td class="lbl">Environment Category</td>
								<td class="input">
									<select id="environmentCategoryId" name="environmentCategoryId">
											<option value="-1" selected="true">----------Select-------------</option>
										<#list environmentCategoryList! as environmentCategory>
											<#if uiTestingSetupForm.environmentCategoryId?exists && uiTestingSetupForm.environmentCategoryId == environmentCategory.environmentCategoryId>
												<option value="${environmentCategory.environmentCategoryId}" selected="true">${environmentCategory.environmentCategory.environmentCategoryName!}</option>
											<#else>
												<option value="${environmentCategory.environmentCategoryId}">${environmentCategory.environmentCategory.environmentCategoryName!}</option>
											</#if>
										</#list>
									</select>
								</td>
							</tr>

							<tr class="lblFieldPair createEnvironment">
								<td class="lbl">Login/Home Url</td>
								<td class="input">
									<input class="required urlinput" type="text"	id="loginOrHomeUrl"  name="loginOrHomeUrl" value="${uiTestingSetupForm.loginOrHomeUrl!}" maxlength="150">
								</td>
							</tr>
							<tr class="lblFieldPair createEnvironment">
								<td class="lbl">Is Secure Site</td>
								<td class="input">
									<input type="checkbox" id="iSecureSite" name="iSecureSite" checked="true" onClick="isSecureSite()">
								</td>
							</tr>
							<tr class="lblFieldPair createEnvironment fileupload">
								<td class="lbl">Login Script</td>
								<td class="input">
									<input class="input_small" type="text" id="fileUrlPath" name="fileUrlPath" value="${uiTestingSetupForm.fileUrlPath!}""> 
									<input type="file" id="loginScriptFile" onchange="CopyMe(this, 'txtFileName')" name="loginScriptFile"> 
									<input type="button" class="uploadbutton" id="loginScriptButton" value="Upload"></td>
								</td> 
							</tr>
					</table>
				</form>
				<center><font color="red" size=-1>&nbsp;* mandatory &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></center>
			</td>
		</tr>
		<tr class="bd">
			<td class="btnBar">
				<a href="#" onClick="validateAndsave()" class="btn"><span>Save</span></a>
				<a href="#" onClick="clearAll()" class="btn"><span>Clear</span></a>
				<a href="#" onClick="loadTabData(1)" class="btn"><span>Previous</span></a> 
				<a href="#" onClick="loadTabData(3)" class="btn"><span>Next</span></a>
			</td>
		</tr>
	</table>
</div>
</section>
	<div class="content" tabindex="0" style="display: block;width:100%;" id="mint_home_table_content">
  		<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
		<table id="mint_home_table" class="styleA fullwidth sfhtTable" summary="">
			<caption></caption>
			<thead>
				<tr>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Application Name</th>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Environment Name</th>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Environment Category</th>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">login/Home Url</th>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Login Script File Name</th>
					<th class="txtl header w10">Secure Site</th>
					<th class="txtl header w10">edit</th>
					<th class="txtl header w10">Remove</th>
				</tr>
			</thead>
			<tbody>
			<#if applicationEnvironmentCategoryList?has_content>
				<#list applicationEnvironmentCategoryList! as applicationEnvironmentCategory>
						<tr data-sortfiltered="false">
							<td scope="row" data-sortvalue="${applicationEnvironmentCategory.application.applicationName!}">
							${applicationEnvironmentCategory.application.applicationName!}
							</td>
							<td scope="row" data-sortvalue="${applicationEnvironmentCategory.environment.environmentName!}">
							${applicationEnvironmentCategory.environment.environmentName!}
							</td>
							<td scope="row" data-sortvalue="${applicationEnvironmentCategory.environment.environmentCategory.environmentCategoryName!}">
								${applicationEnvironmentCategory.environment.environmentCategory.environmentCategoryName!}
							</td>
							<td scope="row" data-sortvalue="${applicationEnvironmentCategory.environment.loginOrHomeUrl!}">
							${applicationEnvironmentCategory.environment.loginOrHomeUrl!}
							</td>
							
							<#assign environmentLoginScript = applicationEnvironmentCategory.environment.environmentLoginScript!>
							<td scope="row" data-sortvalue="${environmentLoginScript.sourceFileName!}">${environmentLoginScript.sourceFileName!}</td>
								
							<td class="row">
							<#if applicationEnvironmentCategory.environment.secureSite>
								Yes
							<#else>
								No
							</#if>
							</td>
							<td class="row">
								<a href="#" class="editLink np" title="Edit the application" onClick="editEnvironment('${applicationEnvironmentCategory.environment.environmentId!}','${applicationEnvironmentCategory.application.applicationId!}','${applicationEnvironmentCategory.environment.environmentCategory.environmentCategoryId!}')">
									<span class="icon nmt plxs" ></span>Edit
								</a>
							</td>
							<td class="row">
								<a title="Remove the Environment" class="deleteIcon np" href="#" onClick="deleteEnvironment('${applicationEnvironmentCategory.environment.environmentId!}','${applicationEnvironmentCategory.application.applicationId!}')">
									<span class="icon nmt plxs" ></span>Remove
								</a>
							</td>
						</tr>
				</#list>
			<#else>
					<tr><td colspan='8'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
			</#if>	
			</tbody>
		</table>
		</div>
		<@mint.tablesort id="#mint_home_table" casesensitive="false" jsvar="mint_home_table__js"/>
		<@mint.paginate table="#mint_home_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
		</div>
</div>