<script type="text/javascript">

$(document).ready(function() {
	$("#environmentId").attr('disabled','disabled');
});

function deleteApplicationConfig(configId) {
	if(confirm("Confirm delete of application configs"))
	{
		document.getElementById("configId").value = configId;
		document.getElementById("applicationConfigForm").setAttribute('action',"<@spring.url '/DeleteApplicationConfig.ftl' />");
		document.getElementById("applicationConfigForm").setAttribute('method',"POST");
		document.getElementById("applicationConfigForm").submit();
	}
}

function editApplicationConfig(configId) {
	document.getElementById("configId").value = configId;
	document.getElementById("applicationConfigForm").setAttribute('action',"<@spring.url '/EditApplicationConfig.ftl' />");
	document.getElementById("applicationConfigForm").setAttribute('method',"POST");
	document.getElementById("applicationConfigForm").submit();
}

function validateAndsave() {
	$("#environmentId").attr('disabled',false);
	var bCount = document.applicationConfigForm.browserRestart;
	var threads = document.applicationConfigForm.noOfBrowsers;
	var maxtime = document.applicationConfigForm.browserTimeout;
	
	if( $('#applicationId').val() == -1) {
		alert('Please Select Application');
		return false;
	}
	
	if( $('#environmentId').val() == -1) {
		alert('Please Select environment');
		return false;
	}
	
	if(isEmpty(bCount) || isEmpty(threads) || isEmpty(maxtime))	{
		alert('Browser Time Out / Number Of Browsers / Browser Restart should not be empty');
		return false;
	}
	
	if(!textNumeric(bCount.value) || !textNumeric(threads.value) || !textNumeric(maxtime.value)) {
		alert('Only Numeric values allowed for Browser Time Out / Number Of Browsers / Browser Restart');
		return false;
	}
	
	if(!isInNumericRange(bCount.value,50,1000)) {
		  alert("Allowed range for browser restart count is 50 to 1000");
		  bCount.focus();
		  return false;
	}
	
	if(!isInNumericRange(threads.value,1,15)) {
	  alert("Allowed range for number of browsers is 1 to 15");
	  threads.focus();
	  return false;
	}
	
	if(!isInNumericRange(maxtime.value,0,180)) {
		alert("Allowed range for browser Timeout is 0 to 180");
		maxtime.focus();
	    return false;
	}
	document.getElementById("applicationConfigForm").setAttribute('action',"<@spring.url '/SaveApplicationConfig.ftl' />");
	if( $('#configId').val() > 0) {
		document.getElementById("applicationConfigForm").setAttribute('action',"<@spring.url '/UpdateApplicationConfig.ftl' />");	
	}
	document.getElementById("applicationConfigForm").setAttribute('method',"POST");
	document.getElementById("applicationConfigForm").submit();
}

function clearFieldsApplication(){
	document.applicationConfigForm.environmentId.value = "";
	document.applicationConfigForm.environmentCategoryId.value = "";
	document.applicationConfigForm.browserTimeout.value = 60;
	document.applicationConfigForm.noOfBrowsers.value = 1;
	document.applicationConfigForm.browserRestart.value = 200;
}

function clearFieldsEnvironment(){
	document.applicationConfigForm.browserTimeout.value = 60;
	document.applicationConfigForm.noOfBrowsers.value = 1;
	document.applicationConfigForm.browserRestart.value = 200;
}

function getEnvironmentCategoryDetails() {
	clearFieldsApplication();
	if ($('#environmentCategoryId option').length > 0){
		document.getElementById("environmentCategoryId").options.length=0;
	}
	loadEnvironmentCategoryDropdown($("#applicationId").val(),"environmentCategoryId");
	
}

function getEnvironmentDetails() {
	clearFieldsEnvironment();
	if ($('#environmentId option').length > 0){
		document.getElementById("environmentId").options.length=0;
	}
	loadEnvironmentDropdown($("#applicationId").val(),$("#environmentCategoryId").val(),"environmentId");
}
function clearAll() {
	$("#applicationId").val(-1);
	$("#environmentId").val(-1);
	$("#environmentCategoryId").val(-1);
	$("#configId").val("");
	$("#browserTimeout").val(60);
	$("#noOfBrowsers").val(1);
	$("#browserRestart").val(200);

}
</script>
<section class="drop mtlg">
	<div class="cm">
	<table cellpadding='0' cellspacing='0' width="100%;">
		<tr>
			<td class="hd">
				<h3>Application Config</h3>
			</td>
		</tr>
		<tr class="bd">
			<td>
				<form modelAttribute="UiTestingSetupForm"
					action='ApplicationConfig.ftl' id="applicationConfigForm" name="applicationConfigForm"
					target="_top" method="POST">
					<table class="bd rowheight35">
					  <br/>
						<input type="hidden" id="configId" name="configId" value="${uiTestingSetupForm.configId!}">
						<input type="hidden" id="setupTabNumber" name="setupTabNumber" value="5">
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
												<#if uiTestingSetupForm.environmentCategoryId?exists && uiTestingSetupForm.environmentCategoryId == environmentCategory.environmentCategoryId>
													<option value="${environmentCategory.environmentCategoryId}" selected="true">${environmentCategory.environmentCategory.environmentCategoryName!}</option>
												<#else>
													<option value="${environmentCategory.environmentCategoryId}">${environmentCategory.environmentCategory.environmentCategoryName!}</option>
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
							<td class="lbl">Browser Timeout(in secs)</td>
							<td class="input"">
								<input type="text" class="required" name="browserTimeout" value="${uiTestingSetupForm.browserTimeout!}" maxlength="3" />
							</td>
						</tr>
						<tr class="lblFieldPair">
							<td class="lbl">Run on No. of Browsers</td>
							<td class="input"">
								<input type="text" class="required"	name="noOfBrowsers" id="threads" value="${uiTestingSetupForm.noOfBrowsers!}" maxlength="2" />
							</td>
						</tr>
						<tr class="lblFieldPair">
							<td class="lbl">Browser Restart</td>
							<td class="input"">
								<input type="text" class="required" name="browserRestart" id="browserRestart" value="${uiTestingSetupForm.browserRestart!}" maxlength="4" />
							</td>
						</tr>
					</table>
				</form>
					<center><font color="red" size=-1>&nbsp;* mandatory &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ** conditional mandatory</font></center>
				</td>
				</tr>
				<tr class="bd">
					<td class="btnBar">
						<a href="#" onClick="validateAndsave()" class="btn"><span>Save</span></a>
						<a href="#" onClick="clearAll()" class="btn"><span>Clear</span></a>
						<a href="#" onClick="loadTabData(4)" class="btn"><span>Previous</span></a> 
						<a href="#" onClick="loadTabData(6)" class="btn"><span>Next</span></a>
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
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Application Name</th>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Application Description</th>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Browser Timeout(in secs)</th>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Run on No. of Browsers</th>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Browser Restart</th>
					<th class="txtl header w5">edit</th>
					<th class="txtl header w8">Remove</th>
				</tr>
			</thead>
			<tbody>
			<#if applicationConfigList?has_content> 
				<#list applicationConfigList! as applicationConfig>
					<tr data-sortfiltered="false">
						<td scope="row" data-sortvalue="${applicationConfig.application.applicationName!!}">${applicationConfig.application.applicationName!}</td>
						<td scope="row" data-sortvalue="${applicationConfig.environment.environmentName!}">${applicationConfig.environment.environmentName!}</td>
						<td scope="row" data-sortvalue="${applicationConfig.browserTimeout!}">${applicationConfig.browserTimeout!}</td>
						<td scope="row" data-sortvalue="${applicationConfig.noOfBrowsers!}">${applicationConfig.noOfBrowsers!}</td>
						<td scope="row" data-sortvalue="${applicationConfig.browserRestart!}">${applicationConfig.browserRestart!}</td>
						<td class="row"><a href="#" class="editLink np" title="Edit the application" onClick="editApplicationConfig('${applicationConfig.configId!}')"><span class="icon nmt plxs" ></span>Edit</a></td>
						<td class="row"><a title="Remove the application" class="deleteIcon np" href="#" onClick="deleteApplicationConfig('${applicationConfig.configId!}')"><span class="icon nmt plxs" ></span>Remove</a></td>
					</tr>
				</#list>
			<#else>
					<tr><td colspan='7'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
			</#if>
			</tbody>
		</table>
		</div>
		<@mint.tablesort id="#mint_home_table" casesensitive="false" jsvar="mint_home_table__js"/>
		<@mint.paginate table="#mint_home_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
	</div>
	</div>