
<script src="//cdnjs.cloudflare.com/ajax/libs/json3/3.3.2/json3.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#environment").attr('disabled','disabled');
	});
function loadSetupLogin() {
	if(document.getElementById("isPublic").value == 1) {
		document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('isPageLoad',"ON");
		document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('action',"<@spring.url '/ApplicationConfig.ftl' />");
	} else {
		document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('action',"<@spring.url '/SetupEnvironmentLoginUser.ftl' />");
	}
	document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('method',"POST");
	document.getElementById("saveAnalyticsExcludeLinkId").submit();
}

function loadSetUpenvironment() {
	document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('applicationId',document.aform.applicationId.value);
	document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('environmentId',document.aform.environmentId.value);
	document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('action',"<@spring.url '/SetupEnvironment.ftl' />");
	document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('method',"POST");
	document.getElementById("saveAnalyticsExcludeLinkId").submit();
}

function deleteAnalyticsExcludeLink(analyticsExcludeId) {
	if(	confirm("Please Confirm to Delete Analytics Exclude Link Setup details from the system")){
		clearFields();
		document.getElementById("analyticsExcludeLinkId").value = analyticsExcludeId;
		document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('action',"<@spring.url '/DeleteAnalyticsExcludeSetup.ftl' />");
		document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('method',"POST");
		document.getElementById("saveAnalyticsExcludeLinkId").submit();
  	}
}

function editAnalyticsExcludeLink(analyticsExcludeId) {
	document.getElementById("analyticsExcludeLinkId").value = analyticsExcludeId;
	document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('action',"<@spring.url '/EditAnalyticsExcludeSetupDetails.ftl' />");
	document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('method',"POST");
	document.getElementById("saveAnalyticsExcludeLinkId").submit();
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
	document.aform.analyticsExcludeLinkId.value = "";
	document.aform.applicationId.value = "";
	document.aform.environmentId.value = "";
	document.aform.application.value = "";
	document.aform.environment.value = "";
	document.aform.environmentCategoryId.value = "";
	document.aform.excludeLink.value = "";
	document.aform.excludeLinkType.value = "";
}

function clearExcludeTypeAndLink(){
	document.aform.excludeLink.value = "";
	document.aform.excludeLinkType.value = "";
}

function clearFieldsApplication(){
	document.aform.environmentId.value = "";
	document.aform.environment.value = "";
	document.aform.environmentCategoryId.value = "";
	document.aform.excludeLink.value = "";
	document.aform.excludeLinkType.value = "";
}

function getEnvironmentCategoryDetails() {
	clearFieldsApplication();
	var applicationId = document.aform.application.options[document.aform.application.selectedIndex].value;
	if ($('#environmentCategoryId option').length > 0){
		document.getElementById("environmentCategoryId").options.length=0;
	}
	loadEnvironmentCategoryDropdown(applicationId,"environmentCategoryId");
	
}
function getEnvironmentDetails() {
	
	clearExcludeTypeAndLink();
	var environmentCategoryId = document.aform.environmentCategoryId.options[document.aform.environmentCategoryId.selectedIndex].value;
	var applicationId = document.aform.application.value;
	if ($('#environment option').length > 0){
		document.getElementById("environment").options.length=0;
	}
	loadEnvironmentDropdown(applicationId, environmentCategoryId,"environment");
}

function validateAndsave(){
	var excludeLink=document.aform.excludeLink;

	document.aform.applicationId.value = document.aform.application.options[document.aform.application.selectedIndex].value;
	document.aform.environmentId.value = document.aform.environment.options[document.aform.environment.selectedIndex].value;
	document.aform.categoryId.value = document.aform.environmentCategoryId.options[document.aform.environmentCategoryId.selectedIndex].value;
	document.aform.excludeLinkType.value = document.aform.excludeLinkType.options[document.aform.excludeLinkType.selectedIndex].value;
	
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
	if(isEmpty(document.aform.excludeLinkType) || document.aform.excludeLinkType.value < 0 || document.aform.excludeLinkType.value == "") {
		alert('Please Select Exclude Link Type');
		document.aform.environment.focus();
		return false;
	}
	if(isEmpty(excludeLink)){
		alert('Please provide Exclude Link to proceed');								
		return false;
	}
	
	if(!isEmpty(excludeLink) && !isInNumericRange(excludeLink.value.length,1,250)){
		alert('Exclude Link length should be below 250 characters.');
		excludeUrlId.focus();
		return false;
	}

	if( document.aform.analyticsExcludeLinkId.value != "" 
			&& document.aform.analyticsExcludeLinkId.value > 0) {
		document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('action',"<@spring.url '/UpdateAnalyticsExcludeSetup.ftl' />");
	} else {
		document.getElementById("saveAnalyticsExcludeLinkId").setAttribute('action',"<@spring.url '/SaveAnalyticsExcludeSetup.ftl' />");
	}
	document.getElementById("saveAnalyticsExcludeLinkId").submit();
}
</script>
<section class="drop mtlg">
	<div class="cm">
		<table cellpadding='0' cellspacing='0' border='0' width='100%'>
			<tr>
				<td class="hd">
					<h3>Analytics Exclude Setup</h3>
				</td>
			</tr>
			<tr class="bd">
				<td>
					<form modelAttribute="UiTestingSetupForm" action='SaveAnalyticsExcludeSetup.ftl' id="saveAnalyticsExcludeLinkId" name="aform" 
						  target="_top" method="POST" onLoad=disable_Env() onSubmit="return validateInputValues()">
					<input type="hidden" id="analyticsExcludeLinkId" name="analyticsExcludeLinkId" value="${uiTestingSetupForm.analyticsExcludeLinkId!}" /> 
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
							<select id="environment" name="environment"	onchange="clearExcludeTypeAndLink()"> 
								<option value="-1" selected="true">----------Select-------------</option>
									<#list environmentList! as environment>
										<#if uiTestingSetupForm.environmentId?exists && uiTestingSetupForm.environmentId == environment.environmentId>
											<option value="${environment.environmentId!}" selected="true">${environment.environmentName!}</option>
										</#if> 
									</#list>
							</select> <font color="red" size=-1>&nbsp;*</font></td>
						</tr>
						
						<tr class="lblFieldPair">
							<td class="lbl">Exclude Link Type</td>
							<td class="input">
								<select id="excludeLinkType" name="excludeLinkType" onchange="">
										<option value="-1" selected="true">----------Select-------------</option>
											<#list excludeLinkTypeList! as excludeLinkType>
												<#if uiTestingSetupForm.excludeLinkType?exists && uiTestingSetupForm.excludeLinkType == excludeLinkType.excludeLinktypeId>
													<option value="${excludeLinkType.excludeLinktypeId!}" selected="true">${excludeLinkType.excludeLinktype!}</option>
												<#else>
													<option value="${excludeLinkType.excludeLinktypeId!}">${excludeLinkType.excludeLinktype!}</option>
												</#if>
											</#list>
									</select> 
								<font color="red" size=-1>&nbsp;*</font>
							</td>
						</tr>
						<tr class="lblFieldPair">
							<td class="lbl">Exclude Link</td>
							<td class="input">
								<input class ="" type="text" id="excludeLink" name="excludeLink"  value="${uiTestingSetupForm.excludeLink!}" maxlength="250"> 
								<font color="red" size=-1>&nbsp;*</font>
								<font color="red" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* mandatory</font>
							</td>
						</tr>
				
					</table>
					</td>
					<td>
					<table class="bd rowheight35"><br/>
					<tr class="lblFieldPair">
							<td class="lbl">System Exclude Link</td>
							<td class="input">
								<select id="systemExcludeLink" name="systemExcludeLink" multiple> 
									<#list systemExcludeLinkList! as systemExcludeUrl>
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
					<a href="#" onClick="clearFields()" class="btn"><span>Clear</span></a>
					<a href="#" onClick="loadTabData(7)" class="btn"><span>Previous</span></a> 
				</td>
			</tr>
		</table>
	</div>
</section>

<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="UI_ANAEXC_LINK_content">
  	<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
			<table id="ui_analyticsExcludeLink_table" class="styleA fullwidth sfhtTable" summary="">
				<caption></caption>
				<thead>
					<tr>
						<th class="txtl header w20" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype1" tabindex="0" data-sortpath="none">Application Name</th>
						<th class="txtl header w20" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype2" tabindex="0" data-sortpath="none">Environment Name</th>
						<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype3" tabindex="0" data-sortpath="none">Environment Category</th>
						<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue1" tabindex="0" data-sortpath="none">Analytics Exclude Link Type</th>
						<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue2" tabindex="0" data-sortpath="none">Analytics Exclude Link</th>
						<th class="txtl header w15">Edit</th>
						<th class="txtl header w10">Remove</th>
					</tr>
				</thead>
				<tbody>
					<#if analyticsExcludeLinkDetail?has_content> 
						<#list analyticsExcludeLinkDetail as analyticsExcludeLink>
							<tr data-sortfiltered="false">
								<td scope="row" data-sortvalue="${analyticsExcludeLink.application.applicationName!}">
									<#if analyticsExcludeLink.application?has_content> 
									 	${analyticsExcludeLink.application.applicationName!} 
									 </#if>
								</td>
								<td scope="row" data-sortvalue="${analyticsExcludeLink.environment.environmentName!}">
									 <#if analyticsExcludeLink.environment?has_content> 
									 	${analyticsExcludeLink.environment.environmentName!}
									 </#if>
								</td>
								<td scope="row" data-sortvalue="${analyticsExcludeLink.environment.environmentCategory.environmentCategoryName!}">
								 	${analyticsExcludeLink.environment.environmentCategory.environmentCategoryName!} 
								</td>
								<td scope="row" data-sortvalue="${analyticsExcludeLink.excludeLinkTypevalue.excludeLinktype!} ">${analyticsExcludeLink.excludeLinkTypevalue.excludeLinktype!}</td>
								<td scope="row" data-sortvalue="${analyticsExcludeLink.excludeLink!} ">${analyticsExcludeLink.excludeLink!}</td>
								<td scope="row"><a href="#" class="editLink np" title="Edit the application" onClick="editAnalyticsExcludeLink('${analyticsExcludeLink.analyticsExcludeLinkId}')"><span class="icon nmt plxs" ></span>Edit</a></td>
								<td scope="row"><a title="Remove the application" class="deleteIcon np" href="#" onClick="deleteAnalyticsExcludeLink('${analyticsExcludeLink.analyticsExcludeLinkId}')"><span class="icon nmt plxs" ></span>Remove</a></td>
							</tr>
		               </#list> 
	               <#else>
						<tr><td colspan='7'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
		           </#if>
	           </tbody>
			</table>
		</div>
		<@mint.tablesort id="#ui_analyticsExcludeLink_table" casesensitive="false" jsvar="ui_analyticsExcludeLink_table__js"/>
		<@mint.paginate table="#ui_analyticsExcludeLink_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
	</div>
</div>