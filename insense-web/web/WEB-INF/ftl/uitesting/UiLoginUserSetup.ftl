<script type="text/javascript">
$(document).ready(function() {
	$("#environment").attr('disabled','disabled');
});

$(document).ready(function(){
	$('#rsaEnabled').change(function () {
			document.aform.securityAnswer.value = "";
			$('#securityAnswer').prop('disabled', this.checked);
	});
	if($('#rsaEnabled').prop('checked')) {
		$('#securityAnswer').prop('disabled', true);
	}
});

function addUIElement() 
{
    $('#containertbl').append("<tr>\
    		<td colspan='2'>UI Element Identifier</td>\
    		<td><input class='input_small required' type='text' name='uitype'><font face='verdana,arial' size='-1' color='red'>*</font></td>\
    		<td colspan='2'>UI Element</td>\
    		<td><input class='input_small required' type='text' name='uielem'><font face='verdana,arial' size='-1' color='red'>*</font></td>\
    		<td colspan='2'>Value</td>\
    		<td><input class='input_small required' type='text' name='uival'><font face='verdana,arial' size='-1' color='red'>*</font></td>\
    		<td colspan='3' style='text-align:left;'><a class='deleteIcon np' onClick='removeElements(this)' width='20' height='20'><span class='icon nmt plxs' ></span>\
    		<td style='text-align:right;'>Secure &nbsp;&nbsp;</td>\
    		<td style='text-align:left;'><input style='width: 10px;' type='checkbox' name='isSecure' onClick='maskInput(this)'></td>\
    	</tr>");
 }
function getUIDetails() {
	document.aform.applicationId.value = document.aform.application.options[document.aform.application.selectedIndex].value;
	document.getElementById("loginUserDetails").setAttribute('action',"<@spring.url '/SaveLoginUserDetails.ftl' />");
	document.getElementById("loginUserDetails").setAttribute('method',"POST");
	document.getElementById("loginUserDetails").submit();
}
function removeElements(btnelem)
{
	 $(btnelem).closest('tr').remove();  
} 

function validateInputValues(){
	/**
	var elementArrayValue = [];							
	$('#containertbl tr').each(function () {
	  	if($(this).find("[name='uitype']").val() == "")
	    {
	    	alert("Please provide UI Element Names");
	    	return false;
	    }
	   if($(this).find("[name='uielem']").val() == "")
	    {
	    	alert("Please provide UI Element Names");
	    	return false;
	    }
	   if($(this).find("[name='uival']").val() == "")
	    {
	    	alert("Please provide UI Element Values");
	    	return false;
	    }	   
	elementArrayValue.push($(this).find("[name='uitype']").val()+"~"+$(this).find("[name='uielem']").val()+"~"+$(this).find("[name='uival']").val()+"~"+$(this).find("[name='isSecure']").prop('checked'));
	});
	**/
	document.aform.applicationId.value = document.aform.application.options[document.aform.application.selectedIndex].value;
	document.aform.environmentId.value = document.aform.environment.options[document.aform.environment.selectedIndex].value;
	document.aform.categoryId.value = document.aform.environmentCategoryId.options[document.aform.environmentCategoryId.selectedIndex].value;
	/**var strappid = application.options[application.selectedIndex].value;
	var strenvid = environment.options[environment.selectedIndex].value;
	document.getElementById("elementsArray").value=elementArrayValue;**/
	
	
	if( document.aform.loginUserId.value != "" 
			&& document.aform.loginUserId.value > 0) {
		document.getElementById("loginUserDetails").setAttribute('action',"<@spring.url '/UpdateLoginUserDetails.ftl' />");
	} else {
		document.getElementById("loginUserDetails").setAttribute('action',"<@spring.url '/SaveLoginUserDetails.ftl' />");
	}
	document.getElementById("loginUserDetails").submit();
}
function maskInput(btnelem1) {
	if($(btnelem1).prop('checked')){
		var secureinput = $(btnelem1).closest('tr').find("[name='uival']");
		var txt = secureinput.val();
		secureinput.replaceWith("<input class='input_small required' type='password' name='uival' value='"+txt+"'>");
	} else {
		var secureinput = $(btnelem1).closest('tr').find("[name='uival']");
		var txt = secureinput.val();
		secureinput.replaceWith("<input class='input_small required' type='text' name='uival' value='"+txt+"'>");
	}
}
function deleteRow(loginUserId) {
	if(confirm("Confirm delete of application configs"))
	{
		clearFieldsApplication();
		document.getElementById("application").value ="-1";
		document.getElementById("applicationId").value ="";
		document.getElementById("loginUserId").value = loginUserId;
		document.getElementById("loginUserDetails").setAttribute('action',"<@spring.url '/deleteLoginUserDetails.ftl' />");
		document.getElementById("loginUserDetails").setAttribute('method',"POST");
		document.getElementById("loginUserDetails").submit();
	}
}
function editRow(loginUserId) {
	document.getElementById("loginUserId").value = loginUserId;
	document.getElementById("loginUserDetails").setAttribute('action',"<@spring.url '/editLoginUserDetails.ftl' />");
	document.getElementById("loginUserDetails").setAttribute('method',"POST");
	document.getElementById("loginUserDetails").submit();
}

function clearFieldsApplication(){
	document.aform.environmentId.value = "";
	document.aform.environment.value = "";
	document.aform.environmentCategoryId.value = "-1";
	document.aform.loginId.value = "";
	document.aform.password.value = "";
	document.aform.securityAnswer.value = "";
	
	//clearLoginUielements();
}


function getEnvironmentCategoryDetails() {
	clearFieldsApplication();
	var applicationId = document.aform.application.options[document.aform.application.selectedIndex].value;
	if ($('#environmentCategoryId option').length > 0){
		document.getElementById("environmentCategoryId").options.length=0;
	}
	loadEnvironmentCategoryDropdown(applicationId,"environmentCategoryId");
	
}
function getSecureEnvironmentDetails() {
	//clearLoginUielements();
	document.aform.loginId.value = "";
	document.aform.password.value = "";
	document.aform.securityAnswer.value = "";
	document.aform.securityAnswer.disabled = false;
	document.aform.rsaEnabled.checked = false;
	
	if ($('#environment option').length > 0){
		document.getElementById("environment").options.length=0;
	}
	loadSecureEnvironmentDropdown($("#environmentCategoryId").val(),$("#application").val(),"environment")
}


function clearLoginUielements(){
	document.aform.uitype.value = "";
	document.aform.uielem.value = "";
	document.aform.uival.value = "";
}
function clearAll() {
	$("#applicationId").val(-1);
	$("#application").val(-1);
	$("#environmentId").val(-1);
	$("#environment").val(-1);
	$("#environmentCategoryId").val(-1);
	$("#loginUserId").val("");
	$("#loginId").val("");
	$("#password").val("");
	$("#securityAnswer").val("");
	$("#rsaEnabled").prop("checked",false);

}
</script>
<body>
<section class="drop mtlg">
	<div class="cm">
		<table cellpadding='0' cellspacing='0' border='0' width='100%'>
			<tr>
				<td class="hd">
					<h3>Add login user details (For Secure Sites)</h3>
				</td>
			</tr>
			<tr class="bd">
				<td>
					<form modelAttribute="UiTestingSetupForm" action='SaveLoginUserDetails.ftl' id="loginUserDetails" 
						  name="aform" target="_top" method="POST" onSubmit="return validateInputValues()">
						
					<input type="hidden" id="isPublic" name="isPublic" value="${isPublic!}" /> 
					<input type="hidden" id="applicationId" name="applicationId" value="${uiTestingSetupForm.applicationId!}">
					<input type="hidden" id="environmentId" name="environmentId" value="${uiTestingSetupForm.environmentId!}">
					<input type="hidden" id="categoryId" name="categoryId" value="${environmentCategoryId!}">
					<input type="hidden" id="loginUserId" name="loginUserId" value="${uiTestingSetupForm.loginUserId!}" /> 
					<input type="hidden" id="elementsArray" name="elementsArray" value=" ">
					<table class="bd rowheight35"><br/>	
 						<tr class="lblFieldPair">
							<td class="lbl">Application Name</td>
							<td class="input">
								<select  id="application" name="application" onchange="getEnvironmentCategoryDetails()">
									<option value="-1">----------Select-------------</option>
										<#list applications! as application>
											<#if uiTestingSetupForm.applicationId?exists && uiTestingSetupForm.applicationId == application.applicationId>
												<option value="${application.applicationId}" selected="true">${application.applicationName}</option>
											<#else>
												<option value="${application.applicationId}">${application.applicationName}</option>
											</#if> 
										</#list>
								</select> <span class="required" style="margin-left:0px;"></span>	
							</td>
						</tr>
						<tr class="lblFieldPair">
								<td class="lbl">Environment Category</td>
								<td class="input">
									<select id="environmentCategoryId" name="environmentCategoryId" onchange="getSecureEnvironmentDetails()">
										<option value="-1" selected="true">----------Select-------------</option>
											<#list environmentCategoryList! as environmentCategory>
												<#if environmentCategory.environmentCategoryId != 1>
													<#if uiTestingSetupForm.environmentCategoryId?exists && uiTestingSetupForm.environmentCategoryId == environmentCategory.environmentCategoryId>
														<option value="${environmentCategory.environmentCategoryId!}" selected="true">${environmentCategory.environmentCategory.environmentCategoryName!}</option>
													<#else>
														<option value="${environmentCategory.environmentCategoryId!}">${environmentCategory.environmentCategory.environmentCategoryName!}</option>
													</#if>
												</#if>
											</#list>
									</select>
								</td>
						</tr>
						<tr class="lblFieldPair">
							<td class="lbl">Environment	Name</td>
							<td  class="input">
								<select id="environment" name="environment" onchange="">
									<option value="-1">----------Select-------------</option>
										<#list environmentList! as environment> 
											<#if environment.secureSite>
												<#if uiTestingSetupForm.environmentId?exists && uiTestingSetupForm.environmentId == environment.environmentId>
													<option value="${environment.environmentId}" selected="true">${environment.environmentName}</option>
												</#if> 
											</#if> 
										</#list>
								</select><span class="required" style="margin-left:0px;"></span>	
							</td>
						</tr>
						<tr class="lblFieldPair">
							<td class="lbl">Login Id</td>
							<td  class="input">
								<input type="text" name="loginId" id="loginId" value="${uiTestingSetupForm.loginId!}">
							</td>
						</tr>
						<tr class="lblFieldPair">
							<td class="lbl">Password</td>
							<td  class="input">
								<input type="password" id="password"  name="password" value="${uiTestingSetupForm.password!}">
							</td>
						</tr>
						<tr class="lblFieldPair">
							<td class="lbl">RSA Enabled</td>
							<td  class="input">
								<#if uiTestingSetupForm.rsaEnabled>
									<input type="checkbox" id="rsaEnabled" name="rsaEnabled" checked="true">
								<#else>
								    <input type="checkbox" id="rsaEnabled" name="rsaEnabled">
								</#if>
								
							</td>
						</tr>
						<tr class="lblFieldPair">
							<td class="lbl">Security Answer</td>
							<td  class="input">
								<input type="text" id="securityAnswer" name="securityAnswer" value="${uiTestingSetupForm.securityAnswer!}">
							</td>
						</tr>
						
						<!-- <tr class="lblFieldPair">
							<td colspan="10">
							<hr/>	
								<table class="nobordertable sfhtTable" id="containertbl">
								<#if addLoginUserList?has_content && addLoginUserList?exists>
									<#assign rowNum = 0> 
										<#list addLoginUserList.loginUiElements! as elementList>
												<tr>
													<td colspan="2">UI Element Identifier</td>
													<td><input class="input_small required" type='text'
														name="uitype" value="${elementList.elementIdentifier!}">
													</td>
													<td colspan="2">UI Element</td>
													<td><input class="input_small required" type="text"
														name="uielem" value="${elementList.uiElementName!}">
													</td>
													<td colspan="2">Value</td>
													<#if elementList.secured == 1>
													<td><input class="input_small required" type="password"
														name="uival" value="${elementList.uiElementValue!}">
													</td>
													<#else>
													<td><input class="input_small required" type="text"
														name="uival" value="${elementList.uiElementValue!}">
													</td> 
													</#if>
													<#if rowNum == 0 >
													<td colspan="3"
														style="text-align: left;"><a class="editLink np"
														name="addbtn" onClick="addUIElement()" width="20" height="20"><span class="icon nmt plxs" ></span>Add
													</td>
													<#else>
													<td colspan="3"
														style="text-align: left;"><a class="deleteIcon np"
														name="addbtn" onClick="removeElements(this)" width="20" height="20"><span class="icon nmt plxs" ></span>
													</td>
													</#if> 
													<#assign rowNum = 1>
													<td style="text-align: right;"><font
														face="verdana,arial">Secure&nbsp;&nbsp;</td> <#if
													elementList.secured == 1>
													<td style="text-align: left;"><input
														style="width: 10px;" type="checkbox" checked="true"
														name="isSecure" onClick="maskInput(this)"></td> <#else>
													<td style="text-align: left;"><input
														style="width: 10px;" type="checkbox" name="isSecure"
														onClick="maskInput(this)"></td> </#if>
												</tr>
												</#list>
									<#else>		
										<tr>
											<td colspan="2">UI Element Identifier</td>
											<td><input class="input_small required" type='text'
												name="uitype"></td>
											<td colspan="2">UI Element</td>
											<td><input class="input_small required" type="text"
												name="uielem"></td>
											<td colspan="2">Value</td>
											<td><input class="input_small required" type="text"
												name="uival"></td>
											<td colspan="3"
												style="text-align: left;"><a class="editLink np"
												name="addbtn" onClick="addUIElement()" width="20" height="20"><span class="icon nmt plxs" ></span>Add
											</td>
											<td style="text-align: right;">Secure&nbsp;&nbsp;</td>
											<td style="text-align: left;">
												<input style="width: 10px;" type="checkbox" name="isSecure" onClick="maskInput(this)">
											</td>
										</tr>
									</#if>
								</table>
							</td>
						</tr> -->
					</table>
					</form>
					<center><font color="red" size=-1>&nbsp;* mandatory</font></center>
				</td>
			</tr>
			<tr class="bd">
				<td class="btnBar">
					<a href="#" onClick="validateInputValues()" class="btn"><span>Save</span></a>
					<a href="#" onClick="clearAll()" class="btn"><span>Clear</span></a>
					<a href="#" onClick="loadTabData(3)" class="btn"><span>Previous</span></a> 
					<a href="#" onClick="loadTabData(5)" class="btn"><span>Next</span></a>
				</td>
			</tr>
		</table>
			
	</div>
</section>
	

<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="UI_TESTLOGIN_content">
  	<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
			<table id="ui_testLogin_table" class="styleA fullwidth sfhtTable" summary="">
				<caption></caption>
				<thead>
					<tr>
						<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype1" tabindex="0" data-sortpath="none">Application Name</th>
						<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype2" tabindex="0" data-sortpath="none">Environment Name</th>
						<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype3" tabindex="0" data-sortpath="none">Environment Category</th>
						<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype4" tabindex="0" data-sortpath="none">Login Id</th>
						<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype5" tabindex="0" data-sortpath="none">Security Answer</th>
						<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype6" tabindex="0" data-sortpath="none">Created/Modified Date</th>
						<!-- <th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue1" tabindex="0" data-sortpath="none">Ui Element Identifier</th>
						<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue2" tabindex="0" data-sortpath="none">Ui Element</th>
						<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue3" tabindex="0" data-sortpath="none">Ui Element Value</th> -->
						<th class="txtl header w7">Edit</th>
						<th class="txtl header w7">Remove</th>
					</tr>
				</thead>
				<tbody>
					<#if loginList?has_content> 
						<#list loginList as loginuser>
							<tr data-sortfiltered="false">
								<td scope="row" data-sortvalue="${loginuser.application.applicationName!} ">
									<#if loginuser.application?has_content>
										${loginuser.application.applicationName!}
									</#if>
								</td>
							
								<td scope="row" data-sortvalue="${loginuser.environment.environmentName!}">
									<#if loginuser.environment?has_content>
										${loginuser.environment.environmentName!}
									</#if>
								</td>
							
								<td scope="row" data-sortvalue="${loginuser.environment.environmentCategory.environmentCategoryName!}">
								<#if loginuser.environment.environmentCategory?has_content> 
									 	${loginuser.environment.environmentCategory.environmentCategoryName!} 
									</#if>
								</td>
								<td scope="row" data-sortvalue="${loginuser.loginId!}">${loginuser.loginId!}</td>
								<td scope="row" data-sortvalue="${loginuser.securityAnswer!}">${loginuser.securityAnswer!}</td>
								<td scope="row" data-sortvalue="${loginuser.dateTimeModified!}">${loginuser.dateTimeModified!}</td>
								
								<td scope="row"><a href="#" class="editLink np" title="Edit the application" onClick="editRow('${loginuser.loginUserDetailId!}')"><span class="icon nmt plxs" ></span>Edit</a></td>
								<td scope="row"><a title="Remove the application" class="deleteIcon np" href="#" onClick="deleteRow('${loginuser.loginUserDetailId!}')"><span class="icon nmt plxs" ></span>Remove</a></td>
							</tr>
					</#list>
				<#else>
					<tr><td colspan='7'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
				</#if>
	           </tbody>
			</table>
		</div>
		<@mint.tablesort id="#ui_testLogin_table" casesensitive="false" jsvar="ui_testLogin_table__js"/>
		<@mint.paginate table="#ui_testLogin_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
	</div>
</div>		
