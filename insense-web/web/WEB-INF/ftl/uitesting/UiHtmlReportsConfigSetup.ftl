<script type="text/javascript">
$(document).ready(function() {
	$("#environmentId").attr('disabled','disabled');
	<#if uiTestingSetupForm.removeTagOrSplitTag?has_content && uiTestingSetupForm.removeTagOrSplitTag == 2>
		$("#removeTagOrSplitTag").val(2);
	<#elseif uiTestingSetupForm.removeTextContent?has_content && uiTestingSetupForm.removeTagOrSplitTag == 3>
		$("#removeTagOrSplitTag").val(3);
	<#else>
	$("#removeTagOrSplitTag").val(1);
	</#if>
	showElements();
});
function validateAndsave() {
	$("#environmentId").attr('disabled',false);
	$("#environmentCategoryId").attr('disabled',false);
	if( $('#applicationId').val() == -1) {
		alert('Please Select Application');
		return false;
	}
	
	if( $('#environmentId').val() == -1) {
		alert('Please Select environment');
		return false;
	}
	
	if($("#removeTagOrSplitTag").val() == 1) {
		if($("#removeTagName").val() == "") {
			alert('Remove Tag Name should not be empty');
			return false;
		}
	}
	
	if($("#removeTagOrSplitTag").val() == 2) {
		if($("#splitTagName").val() == "" || $("#splitContentName").val() == "") {
			alert('Split Tag Name and content name should not be empty');
			return false;
		}
	}
	
	document.getElementById("HtmlReportsConfigId").setAttribute('action',"<@spring.url '/SaveHtmlReportsConfig.ftl' />");
	
	if($("#configId").val() > 0) {
		document.getElementById("HtmlReportsConfigId").setAttribute('action',"<@spring.url '/UpdateHtmlReportsConfig.ftl' />");
	} 	
	
	document.getElementById("HtmlReportsConfigId").setAttribute('method',"POST");
	document.getElementById("HtmlReportsConfigId").submit();
}
function showElements() {
	if($("#removeTagOrSplitTag").val() == 1) {
		$('#removeTag').show();
		$('.splitTag').hide();
		$('#removeText').hide();
	}
	if($("#removeTagOrSplitTag").val() == 2) {
		$('#removeTag').hide();
		$('.splitTag').show();
		$('#removeText').hide();
	}
	if($("#removeTagOrSplitTag").val() == 3) {
		$('#removeTag').hide();
		$('.splitTag').hide();
		$('#removeText').show();
	}
}
function getEnvironmentCategoryDetails() {
	var applicationId = $("#applicationId").val();
	if ($('#environmentCategoryId option').length > 0){
		document.getElementById("environmentCategoryId").options.length=0;
	}
	loadEnvironmentCategoryDropdown(applicationId,"environmentCategoryId");
	
}
function getEnvironmentDetails() {
	var environmentCategoryId =$("#environmentCategoryId").val();
	var applicationId = $("#applicationId").val();
	if ($('#environmentId option').length > 0){
		document.getElementById("environmentId").options.length=0;
	}
	loadEnvironmentDropdown(applicationId, environmentCategoryId,"environmentId");
}
function editHtmlReportConfig(configId) {
	document.getElementById("configId").value = configId;
	document.getElementById("HtmlReportsConfigId").setAttribute('action',"<@spring.url '/EditHtmlReportsConfig.ftl' />");
	document.getElementById("HtmlReportsConfigId").setAttribute('method',"POST");
	document.getElementById("HtmlReportsConfigId").submit();
}
function deleteHtmlReportConfig(configId) {
	clearAll();
	document.getElementById("configId").value = configId;
	document.getElementById("HtmlReportsConfigId").setAttribute('action',"<@spring.url '/DeleteHtmlReportsConfig.ftl' />");
	document.getElementById("HtmlReportsConfigId").setAttribute('method',"POST");
	document.getElementById("HtmlReportsConfigId").submit();
}
function clearAll() {
	$("#applicationId").val(-1);
	$("#environmentId").val(-1);
	$("#environmentCategoryId").val(-1);
	$("#configId").val("");
	$("#removeTagOrSplitTag").val(1);
	$("#removeTagName").val("");
	$("#splitTagName").val("");
	$("#splitContentName").val("");
	$("#removeTextContent").val("");
	showElements();
}
</script>
<section class="drop mtlg">
	<div class="cm">
	
	<table cellpadding='0' cellspacing='0' width="100%;">
		<tr>
					<td class="hd">
						<h3>HTML Reports Config</h3>
					</td>
		</tr>
		<tr class="bd">
			<td>
				<form modelAttribute="UiTestingSetupForm" action='saveHtmlReportsConfig.ftl' id="HtmlReportsConfigId"
					name="aform" target="_top" method="POST">
					<table class="bd rowheight35"><br/>
						<input type="hidden" id="configId" name="configId" value="${uiTestingSetupForm.configId!}">
						<tr class="lblFieldPair">
							<td class="lbl">Application	Name</td>
							<td class="input"">
								<select	id="applicationId" name="applicationId"	onchange="getEnvironmentCategoryDetails()"> 
										<option value="-1" selected="true">----------Select-------------</option>
									<#list applications! as application> 
										<#if uiTestingSetupForm.applicationId?has_content && application.applicationId == uiTestingSetupForm.applicationId>
											<option value="${application.applicationId}" selected="true">${application.applicationName!}</option>
										<#else>
											<option value="${application.applicationId}">${application.applicationName!}</option>
										</#if> 
									</#list>
								</select>
							</td>
						</tr>
						<tr class="lblFieldPair">
								<td class="lbl">Environment Category</td>
								<td class="input">
									<select id="environmentCategoryId" name="environmentCategoryId" onchange="getEnvironmentDetails()">
										<option value="-1" selected="true">----------Select-------------</option>
											<#list environmentCategoryList! as environmentCategory>
												<#if uiTestingSetupForm.environmentCategoryId?has_content && uiTestingSetupForm.environmentCategoryId == environmentCategory.environmentCategoryId>
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
							<td class="input"">
								<select id="environmentId" name="environmentId" onchange=""> 
									<option value="-1" selected="true">----------Select-------------</option>
								<#list environmentList! as environment> 
									<#if uiTestingSetupForm.environmentId?has_content && environment.environmentId == uiTestingSetupForm.environmentId>
										<option value="${environment.environmentId}" selected="true">${environment.environmentName!}</option>
									</#if> 
								</#list>
								</select>
							</td>
						</tr>
						<tr class="lblFieldPair">
							<td class="lbl">Removed/Split Content
							</td>
							<td class="input">
								<select	id="removeTagOrSplitTag" name="removeTagOrSplitTag" onchange="showElements()">
									<option value="1" selected="true">Remove tag</option>
									<option value="2">Split By Content</option>
									<option value="3">Remove Text</option>
								</select>
							</td>
						</tr>
						<tr class="lblFieldPair" id="removeTag">
							<td class="lbl">Remove Tag Name
							</td>
							<td class="input"><input type="text"	
								name="removeTagName" class="required" id="removeTagName"
								value="${uiTestingSetupForm.removeTagName!}" /></td>
						</tr>
						<tr class="lblFieldPair splitTag">
							<td class="lbl">HTML Tag Path</td>
							<td class="input">
								<input type="text" name="splitTagName" id="splitTagName" value="${uiTestingSetupForm.splitTagName!}"/>
							</td>
						</tr>
						<tr class="lblFieldPair splitTag">
							<td class="lbl">Split Content Name</td>
							<td class="input">
								<input type="text" name="splitContentName" id="splitContentName" value="${uiTestingSetupForm.splitContentName!}" />
							</td>
						</tr>
						<tr class="lblFieldPair" id="removeText">
							<td class="lbl">Remove Text
							</td>
							<td class="input"><input type="text"	
								name="removeTextContent" class="required" id="removeTextContent"
								value="${uiTestingSetupForm.removeTextContent!}" /></td>
						</tr>
					</table>
				</form>
					<center><font color="red" size=-1>&nbsp;* mandatory</font></center>
				</td>
				</tr>
				<tr class="bd">
								<td class="btnBar">
									<a href="#" onClick="validateAndsave()" class="btn"><span>Save</span></a>
									<a href="#" onClick="clearAll()" class="btn"><span>Clear</span></a>
									<a href="#" onClick="loadTabData(5)" class="btn"><span>Previous</span></a> 
									<a href="#" onClick="loadTabData(7)" class="btn"><span>Next</span></a>
								</td>
				</tr>
	</table>
</div>
</section>
<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="mint_home_table_content">
  	<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
		<table id="mint_home_table" class="styleA fullwidth sfhtTable" summary="">
			<caption></caption>
			<thead>
				<tr>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Application Name</th>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Environment Name</th>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Remove Tag Name</th>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Split Tag Name</th>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Split Content Name</th>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Remove Text Content</th>
					<th class="txtl header w35">Remove By Tag/Split By Content/Remove Text</th>
					<th class="txtl header w8">Edit</th>
					<th class="txtl header w10">Remove</th>
				</tr>
			</thead>
			<tbody>
			<#if htmlReportConfigList?has_content> 
				<#list htmlReportConfigList! as htmlReportConfig>
					<tr data-sortfiltered="false">
						<td scope="row" data-sortvalue="${htmlReportConfig.application.applicationName!!}">${htmlReportConfig.application.applicationName!}</td>
						<td scope="row" data-sortvalue="${htmlReportConfig.environment.environmentName!}">${htmlReportConfig.environment.environmentName!}</td>
						<td scope="row" data-sortvalue="${htmlReportConfig.removeTagName!}">${htmlReportConfig.removeTagName!}</td>
						<td scope="row" data-sortvalue="${htmlReportConfig.splitTagName!}">${htmlReportConfig.splitTagName!}</td>
						<td scope="row" data-sortvalue="${htmlReportConfig.splitContentName!}">${htmlReportConfig.splitContentName!}</td>
						<td scope="row" data-sortvalue="${htmlReportConfig.removeTextContent!}">${htmlReportConfig.removeTextContent!}</td>
						<td>
						<#if htmlReportConfig.removeTag>
								Remove By Tag
						<#elseif htmlReportConfig.removeText>
								Remove Text
						<#else>
								Split By Content
						</#if>
						</td>
						<td class="row"><a href="#" class="editLink np" title="Edit the application" onClick="editHtmlReportConfig('${htmlReportConfig.htmlReportsConfigId!}')"><span class="icon nmt plxs" ></span>Edit</a></td>
						<td class="row"><a title="Remove the application" class="deleteIcon np" href="#" onClick="deleteHtmlReportConfig('${htmlReportConfig.htmlReportsConfigId!}')"><span class="icon nmt plxs" ></span>Remove</a></td>
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