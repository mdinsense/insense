
<script src="//cdnjs.cloudflare.com/ajax/libs/json3/3.3.2/json3.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#environment").attr('disabled','disabled');
	});
function loadSetupLogin() {
	if(document.getElementById("isPublic").value == 1) {
		document.getElementById("saveIncExcURLId").setAttribute('isPageLoad',"ON");
		document.getElementById("saveIncExcURLId").setAttribute('action',"<@spring.url '/ApplicationConfig.ftl' />");
	} else {
		document.getElementById("saveIncExcURLId").setAttribute('action',"<@spring.url '/SetupEnvironmentLoginUser.ftl' />");
	}
	document.getElementById("saveIncExcURLId").setAttribute('method',"POST");
	document.getElementById("saveIncExcURLId").submit();
}

function loadSetUpenvironment() {
	document.getElementById("saveIncExcURLId").setAttribute('applicationId',document.aform.applicationId.value);
	document.getElementById("saveIncExcURLId").setAttribute('environmentId',document.aform.environmentId.value);
	document.getElementById("saveIncExcURLId").setAttribute('action',"<@spring.url '/SetupEnvironment.ftl' />");
	document.getElementById("saveIncExcURLId").setAttribute('method',"POST");
	document.getElementById("saveIncExcURLId").submit();
}

function updateForm(includeId) {
	document.getElementById("includeUrlId").value=includeId;
	document.getElementById("saveIncExcURLId").setAttribute('action',"<@spring.url '/DeleteIncludeExcludeUrl.ftl' />");
	document.getElementById("saveIncExcURLId").submit();
}

function deleteIncludeUrl(includeId) {
	if(	confirm("Please Confirm to Delete Include Url details from the system")){
		document.getElementById("includeUrlId").value=includeId;
		document.getElementById("excludeUrlId").value="";
		clearFields();
		document.getElementById("saveIncExcURLId").setAttribute('action',"<@spring.url '/DeleteIncludeExcludeUrl.ftl' />");
		document.getElementById("saveIncExcURLId").setAttribute('method',"POST");
		document.getElementById("saveIncExcURLId").submit();
  	}
}

function deleteExcludeUrl(excludeId) {
	if(	confirm("Please Confirm to Delete Exclude Url details from the system")){
		document.getElementById("includeUrlId").value="";
		document.getElementById("excludeUrlId").value=excludeId;
		clearFields();
		document.getElementById("saveIncExcURLId").setAttribute('action',"<@spring.url '/DeleteIncludeExcludeUrl.ftl' />");
		document.getElementById("saveIncExcURLId").setAttribute('method',"POST");
		document.getElementById("saveIncExcURLId").submit();
  	}
}

function editIncludeUrl(includeId,categoryId) {
	document.getElementById("includeUrlId").value = includeId;
	document.getElementById("environmentCategoryId").value = categoryId;
	document.getElementById("excludeUrlId").value="";
	document.getElementById("saveIncExcURLId").setAttribute('action',"<@spring.url '/EditIncludeExcludeDetails.ftl' />");
	document.getElementById("saveIncExcURLId").setAttribute('method',"POST");
	document.getElementById("saveIncExcURLId").submit();
}

function editExcludeUrl(excludeId,categoryId) {
	document.getElementById("includeUrlId").value="";
	document.getElementById("excludeUrlId").value=excludeId;
	document.getElementById("environmentCategoryId").value = categoryId;
	document.getElementById("saveIncExcURLId").setAttribute('action',"<@spring.url '/EditIncludeExcludeDetails.ftl' />");
	document.getElementById("saveIncExcURLId").setAttribute('method',"POST");
	document.getElementById("saveIncExcURLId").submit();
}

function disable_Env() {
	if(document.getElementById("environment").disabled) {
		document.getElementById("environment").disabled = false;		
	} else {
		document.getElementById("environment").disabled = true;
		alert('Select All is selected, so the Crawl URL will be applicable for all Environments');
	}
}

function clearFields(){
	document.aform.applicationId.value = "";
	document.aform.environmentId.value = "";
	document.aform.application.value = "";
	document.aform.environment.value = "";
	document.aform.environmentCategoryId.value = "";
	document.aform.includeUrl.value = "";
	document.aform.excludeUrl.value = "";
	$('select option').removeAttr('selected');
	document.aform.includeUrl.disabled = false;
	document.aform.excludeUrl.disabled = false;
}

function clearIncludeExclude(){
	document.aform.includeUrl.value = "";
	document.aform.excludeUrl.value = "";
}

function clearAllFields(){
	document.aform.includeUrlId.value = "";
	document.aform.excludeUrlId.value = "";	
	document.aform.applicationId.value = "";
	document.aform.environmentId.value = "";
	document.aform.application.value = "-1";
	document.aform.environment.value = "-1";
	document.aform.environmentCategoryId.value = "-1";
	document.aform.includeUrl.value = "";
	document.aform.excludeUrl.value = "";
	$('select option').removeAttr('selected');
	document.aform.includeUrl.disabled = false;
	document.aform.excludeUrl.disabled = false;
}

function clearFieldsApplication(){
	document.aform.environmentId.value = "";
	document.aform.environment.value = "";
	document.aform.environmentCategoryId.value = "";
	document.aform.includeUrl.value = "";
	document.aform.excludeUrl.value = "";
}

/****
 * 
 function getEnvironmentDetails() {
	document.aform.environmentId.value = "";
	document.aform.includeUrl.value = "";
	document.aform.excludeUrl.value = "";
	document.aform.includeUrlId.value = "";
	document.aform.excludeUrlId.value = "";
	document.aform.environmentCategoryId.value = "";
	
	var applicationId = document.aform.application.options[document.aform.application.selectedIndex].value;
	if ($('#environment option').length > 0){
		document.getElementById("environment").options.length=0;
	}
	loadEnvironmentDropdown(applicationId,"environment");
}

function getEnvironmentCategoryDetails() {
	document.aform.includeUrlId.value = "";
	document.aform.excludeUrlId.value = "";
	var environmentId = document.aform.environment.options[document.aform.environment.selectedIndex].value;
	loadEnvironmentCategoryDropdown(environmentId,"environmentCategoryId");
}
*/
function getEnvironmentCategoryDetails() {
	clearFieldsApplication();
	var applicationId = document.aform.application.options[document.aform.application.selectedIndex].value;
	if ($('#environmentCategoryId option').length > 0){
		document.getElementById("environmentCategoryId").options.length=0;
	}
	loadEnvironmentCategoryDropdown(applicationId,"environmentCategoryId");
	//loadEnvironmentCategoryDropdownAppendALL(applicationId,"environmentCategoryId");
}

function getEnvironmentDetails() {
	
	clearIncludeExclude();
	var environmentCategoryId = document.aform.environmentCategoryId.options[document.aform.environmentCategoryId.selectedIndex].value;
	var applicationId = document.aform.application.value;
	if ($('#environment option').length > 0){
		document.getElementById("environment").options.length=0;
	}
	loadEnvironmentDropdown(applicationId, environmentCategoryId,"environment");	
}

function validateAndsave(){
	var includeUrl=document.aform.includeUrl;
	var excludeUrl=document.aform.excludeUrl;
	
	document.aform.applicationId.value = document.aform.application.options[document.aform.application.selectedIndex].value;
	document.aform.environmentId.value = document.aform.environment.options[document.aform.environment.selectedIndex].value;
	document.aform.categoryId.value = document.aform.environmentCategoryId.options[document.aform.environmentCategoryId.selectedIndex].value;
	
	if(isEmpty(document.aform.application) || document.aform.application.value < 0 || document.aform.application.value == "") {
		alert('Please Select Application');
		document.aform.application.focus();
		return false;
	}
	if(isEmpty(document.aform.environment) || document.aform.environment.value < 0 || document.aform.environment.value == "") {
		alert('Please Select Environment');
		document.aform.environment.focus();
		return false;
	}
	if(isEmpty(includeUrl) && isEmpty(excludeUrl)){
		alert('Please provide ExcludeUrl or IncludeUrl to proceed');								
		return false;
	}
	if(!isEmpty(includeUrl) && !isInNumericRange(includeUrl.value.length,1,250)){
		alert('Include Url length should be below 250 characters.');
		includeUrl.focus();
		return false;
	}
	if(!isEmpty(excludeUrl) && !isInNumericRange(excludeUrl.value.length,1,250)){
		alert('Exclude Url length should be below 250 characters.');
		excludeUrlId.focus();
		return false;
	}

	if( document.aform.includeUrlId.value != "" && document.aform.includeUrlId.value > 0
		|| document.aform.excludeUrlId.value != "" && document.aform.excludeUrlId.value > 0	) {
		document.getElementById("saveIncExcURLId").setAttribute('action',"<@spring.url '/UpdateIncludeExcludeURL.ftl' />");
	} else {
		document.getElementById("saveIncExcURLId").setAttribute('action',"<@spring.url '/SaveIncludeExcludeURL.ftl' />");
	}
	document.getElementById("saveIncExcURLId").submit();
}
</script>
<section class="drop mtlg">
	<div class="cm">
		<table cellpadding='0' cellspacing='0' border='0' width='100%'>
			<tr>
				<td class="hd">
					<h3>Include/Exclude URLs</h3>
				</td>
			</tr>
			<tr class="bd">
				<td>
					<form modelAttribute="UiTestingSetupForm" action='SaveIncludeExcludeURL.ftl' id="saveIncExcURLId" name="aform" 
						  target="_top" method="POST" onLoad=disable_Env() onSubmit="return validateInputValues()">
					<input type="hidden" id="includeUrlId" name="includeUrlId" value="${uiTestingSetupForm.includeUrlId!}" /> 
					<input type="hidden" id="excludeUrlId" name="excludeUrlId" value="${uiTestingSetupForm.excludeUrlId!}" /> 
					<input type="hidden" id="isPublic" name="isPublic" value="${isPublic!}" /> 
					<input type="hidden" id="applicationId" name="applicationId" value="${applicationId!}">
					<input type="hidden" id="environmentId" name="environmentId" value="${environmentId!}"> 
					<input type="hidden" id="categoryId" name="categoryId" value="${environmentCategoryId!}"> 
					<input type="hidden" id="isPageLoad" name="isPageLoad" value="ON">
					
					<table>
					
					<tr><td>
					<table class="bd rowheight35"><br/>
					
						<tr class="lblFieldPair">
							<td class="lbl">Application Name</td>
							<td class="input">
							<select id="application" name="application" 	onchange="getEnvironmentCategoryDetails()"> 
								<option value="-1">----------Select-------------</option>
								<#list applications as application> 
									<#if uiTestingSetupForm.applicationId?exists && uiTestingSetupForm.applicationId==application.applicationId>
										<option value="${application.applicationId}" selected="true">${application.applicationName}</option>
									<#else>
										<option value="${application.applicationId}">${application.applicationName}</option>
									</#if> 
								</#list>
							</select> <font color="red" size=-1>&nbsp;*</font></td>
						</tr>
						<tr class="lblFieldPair">
								<td class="lbl">Environment Category</td>
								<td class="input">
									<select id="environmentCategoryId" name="environmentCategoryId" onchange="getEnvironmentDetails()">
										<option value="-1" selected="true">----------Select-------------</option>
											<#list environmentCategoryList! as environmentCategory>
												<#if uiTestingSetupForm.environmentCategoryId?exists && uiTestingSetupForm.environmentCategoryId == environmentCategory.environmentCategoryId>
													<option value="${environmentCategory.environmentCategoryId!}" selected="true">${environmentCategory.environmentCategory.environmentCategoryName!}</option>
												<#else>
													<option value="${environmentCategory.environmentCategoryId!}">${environmentCategory.environmentCategory.environmentCategoryName!}</option>
												</#if>
											</#list>
									</select>
								</td>
						</tr>
						<tr class="lblFieldPair">
							<td class="lbl">Environment Name</td>
							<td class="input">
							<select id="environment" name="environment"	onchange="clearIncludeExclude()"> 
								<option value="-1" selected="true">----------Select-------------</option>
									<#list environmentList! as environment>
										<#if uiTestingSetupForm.environmentId?exists && uiTestingSetupForm.environmentId == environment.environmentId>
											<option value="${environment.environmentId!}" selected="true">${environment.environmentName!}</option>
										</#if> 
									</#list>
							</select> <font color="red" size=-1>&nbsp;*</font></td>
						</tr>
						<#assign enableIncludeUrl = ""/>
						<#if uiTestingSetupForm.includeUrlId??>
							<#assign enableIncludeUrl = "disabled"/>
						</#if>
						
						<#assign enableExcludeUrl = ""/>
						<#if uiTestingSetupForm.excludeUrlId??>
							<#assign enableExcludeUrl = "disabled"/>
						</#if>
						
						<tr class="lblFieldPair">
							<td class="lbl">Include URL</td>
							<td class="input">
								<input class ="" type="text" id="includeUrl" name="includeUrl" ${enableExcludeUrl} value="${uiTestingSetupForm.includeUrl!}" maxlength="250"> 
								<font color="red" size=-1>&nbsp;*</font>
							</td>
						</tr>
						<tr class="lblFieldPair">
							<td class="lbl">Exclude URL</td>
							<td class="input">
								<input class ="" type="text" id="excludeUrl" name="excludeUrl"  ${enableIncludeUrl} value="${uiTestingSetupForm.excludeUrl!}" maxlength="250"> 
								<font color="red" size=-1>&nbsp;*</font>
								<font color="red" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* mandatory</font>
							</td>
						</tr>
				
					</table>
					</td>
					<td>
					<table class="bd rowheight35"><br/>
					<tr class="lblFieldPair">
							<td class="lbl">System Include URL</td>
							<td class="input">
								<select id="systemIncludeUrls" name="systemIncludeUrls" multiple> 
									<#list systemIncludeUrlList! as systemIncludeUrl>
											<option value="${systemIncludeUrl!}">${systemIncludeUrl!}</option>
									</#list>
								</select>
							</td>
					</tr>
						
					<tr class="lblFieldPair">
							<td class="lbl">System Exclude URL</td>
							<td class="input">
								<select id="systemExcludeUrls" name="systemExcludeUrls" multiple> 
									<#list systemExcludeUrlList! as systemExcludeUrl>
											<option value="${systemExcludeUrl!}">${systemExcludeUrl!}</option>
									</#list>
								</select>
							</td>
					</tr>
					</table>
					</td>
				</table>
					</form>
						
				</td>
			</tr>
			<tr class="bd">
				<td class="btnBar">
					<a href="#" onClick="validateAndsave()" class="btn"><span>Save</span></a>
					<a href="#" onClick="clearAllFields()" class="btn"><span>Clear</span></a>
					<a href="#" onClick="loadTabData(2)" class="btn"><span>Previous</span></a> 
					<a href="#" onClick="loadTabData(4)" class="btn"><span>Next</span></a>
				</td>
			</tr>
		</table>
	</div>
</section>
	
<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="UI_INCEXC_URL_content">
  	<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
			<table id="ui_includeExclude_table" class="styleA fullwidth sfhtTable" summary="">
				<caption></caption>
				<thead>
					<tr>
						<th class="txtl header w20" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype1" tabindex="0" data-sortpath="none">Application Name</th>
						<th class="txtl header w20" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype2" tabindex="0" data-sortpath="none">Environment Name</th>
						<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype3" tabindex="0" data-sortpath="none">Environment Category</th>
						<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue1" tabindex="0" data-sortpath="none">Include URL</th>
						<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue2" tabindex="0" data-sortpath="none">Exclude URL</th>
						<th class="txtl header w15">Edit</th>
						<th class="txtl header w10">Remove</th>
					</tr>
				</thead>
				<tbody>
				<#if includeURLList?has_content || excludeURLList?has_content>
					<#if includeURLList?has_content> 
						<#list includeURLList as includeListValue> 
							<tr data-sortfiltered="false">
								<td scope="row" data-sortvalue="${includeListValue.application.applicationName!} ">
									<#if includeListValue.application?has_content> 
									 	${includeListValue.application.applicationName!} 
									</#if>
								</td>
								<td scope="row" data-sortvalue="${includeListValue.environment.environmentName!}">
									<#if includeListValue.environment?has_content> 
									 	${includeListValue.environment.environmentName!} 
									</#if>
								</td>
								<td scope="row" data-sortvalue="${includeListValue.environment.environmentCategory.environmentCategoryName!}">
								 	${includeListValue.environment.environmentCategory.environmentCategoryName!} 
								</td>
								<td scope="row" data-sortvalue="${includeListValue.includeUrl!}">${includeListValue.includeUrl!}</td>
								<td></td>
								<td scope="row"><a href="#" class="editLink np" title="Edit the application" onClick="editIncludeUrl('${includeListValue.includeUrlId}','${includeListValue.environment.environmentCategory.environmentCategoryId}')"><span class="icon nmt plxs" ></span>Edit</a></td>
								<td scope="row"><a title="Remove the application" class="deleteIcon np" href="#" onClick="deleteIncludeUrl('${includeListValue.includeUrlId}')"><span class="icon nmt plxs" ></span>Remove</a></td>
							</tr>
						</#list> 
					</#if>
					
					<#if excludeURLList?has_content> 
						<#list excludeURLList as excludeListValue>
							<tr data-sortfiltered="false">
								<td scope="row" data-sortvalue="${excludeListValue.application.applicationName!}">
									<#if excludeListValue.application?has_content> 
									 	${excludeListValue.application.applicationName!} 
									 </#if>
								</td>
								<td scope="row" data-sortvalue="${excludeListValue.environment.environmentName!}">
									 <#if excludeListValue.environment?has_content> 
									 	${excludeListValue.environment.environmentName!}
									 </#if>
								</td>
								<td scope="row" data-sortvalue="${excludeListValue.environment.environmentCategory.environmentCategoryName!}">
								 	${excludeListValue.environment.environmentCategory.environmentCategoryName!} 
								</td>
								<td></td>
								<td scope="row" data-sortvalue="${excludeListValue.excludeUrl!} ">${excludeListValue.excludeUrl!}</td>
								<td scope="row"><a href="#" class="editLink np" title="Edit the application" onClick="editExcludeUrl('${excludeListValue.excludeUrlId}','${excludeListValue.environment.environmentCategory.environmentCategoryId}')"><span class="icon nmt plxs" ></span>Edit</a></td>
								<td scope="row"><a title="Remove the application" class="deleteIcon np" href="#" onClick="deleteExcludeUrl('${excludeListValue.excludeUrlId}')"><span class="icon nmt plxs" ></span>Remove</a></td>
							</tr>
		               </#list> 
		           </#if>
		       	<#else>
					<tr><td colspan='7'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
				</#if>    
	           </tbody>
			</table>
		</div>
		<@mint.tablesort id="#ui_includeExclude_table" casesensitive="false" jsvar="ui_includeExclude_table__js"/>
		<@mint.paginate table="#ui_includeExclude_table" style="new" autocount="true" perpage="5" startpage="1" selectpage="false" showperpagedropdown="true"/>
	</div>
</div>		
