<script type="text/javascript">
$(document).ready(function() {
	if(document.aform.applicationId.value <=0 || document.aform.applicationName.value =="") {
		clearFields();
	}
});
function getApplicationDetailsForEdit(applicationId)
{
	document.aform.applicationId.value=applicationId;
	document.getElementById("configureApplication").setAttribute('action',"<@spring.url '/EditApplicationDetails.ftl' />");
	document.getElementById("configureApplication").setAttribute('method',"POST");
	document.getElementById("configureApplication").submit();
}
function validateAndsave()
{
	var applicationName=document.aform.applicationName.value;
	var applicationDescription=document.aform.applicationDescription.value;
	if(applicationName==""){
		alert('Application Name should not be blank');
		document.aform.applicationName.focus();
		return false;
	}else if(!specialCharValidate(applicationName)){
		alert('Special Characters are not allowed');
		document.aform.applicationName.focus();
		return false;
	}else if(!textAlphanumeric(applicationName)){
		alert('Application Name is not alphanumeric');
		document.aform.applicationName.focus();
		return false;
	}	
	if(applicationDescription== "")
	{
		alert('Please Enter Description');
		document.aform.applicationDescription.focus();
		return false;
	}
	if(document.aform.applicationId.value != "" && document.aform.applicationId.value > 0) {
		document.getElementById("configureApplication").setAttribute('action',"<@spring.url '/UpdateApplication.ftl' />");
	} else {
		document.getElementById("configureApplication").setAttribute('action',"<@spring.url '/SaveApplication.ftl' />");
	}
		document.getElementById("configureApplication").submit();
}	
function deleteApplication(applicationId)
{
	if(confirm("Confirm delete of application setup"))
	{
		document.aform.applicationId.value=applicationId;
		document.getElementById("configureApplication").setAttribute('action',"<@spring.url '/DeleteApplication.ftl' />");
		document.getElementById("configureApplication").setAttribute('method',"POST");
		document.getElementById("configureApplication").submit();
	}
}
function clearFields() {
	document.aform.applicationId.value="";
	document.aform.applicationName.value= "";
	document.aform.applicationDescription.value="";
}
</script>
<section class="drop mtlg">
	<div class="cm">
		<table cellpadding='0' cellspacing='0' width="100%;">
			<tr>
				<td class="hd">
					<h3>Application Setup</h3>
				</td>
			</tr>
			<tr class="bd">
				<td style="padding:5">
					<form modelAttribute="UiTestingSetupForm" action='SetupApplication.ftl' id="configureApplication" name="aform" target="_top" method="POST">
						<input type="hidden" id="setupTabNumber" name="setupTabNumber" value="1">
						<table class="bd rowheight35">
							<br />
							<tr class="lblFieldPair">
								<td class="lbl">Application Name</td>
								<td class="input">
									<input type="hidden" id="applicationId" name="applicationId" value="${uiTestingSetupForm.applicationId!}">
									<input class="input_small required" type="text" name="applicationName" maxlength="25"
									value="${uiTestingSetupForm.applicationName!}">
								</td>
							</tr>
							<tr class="lblFieldPair">
								<td class="lbl">Application Description</td>
								<td class="input">
									<input class="required" type="text" name="applicationDescription" maxlength="100" value="${uiTestingSetupForm.applicationDescription!}">
								</td>
							</tr>
							<tr>
								<td width="100"></td>
								<td width="100" align="left"><font color="red" size=-1>* mandatory</font></td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
			<tr class="bd">
				<td class="btnBar"><a href="#" onClick="validateAndsave()"
					class="btn"><span>Save</span></a> <a href="#"
					onClick="clearFields()" class="btn"><span>Clear</span></a> <a
					href="#" onClick="loadTabData(2);" class="btn"><span>Next</span></a>
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
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Application Name</th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Application Description</th>
					<th class="txtl header w15">edit</th>
					<th class="txtl header w10">Remove</th>
				</tr>
			</thead>
			<tbody>
			<#if applications?has_content>
				<#list applications! as application>
					<tr data-sortfiltered="false">
						<td scope="row" data-sortvalue="${application.applicationName!}">${application.applicationName!}</td>
						<td scope="row" data-sortvalue="${application.applicationDescription!}">${application.applicationDescription!}</td>
						<td class="row"><a href="#" class="editLink np" title="Edit the application" onClick="getApplicationDetailsForEdit('${application.applicationId}')"><span class="icon nmt plxs" ></span>Edit</a></td>
						<td class="row"><a title="Remove the application" class="deleteIcon np" href="#" onClick="deleteApplication('${application.applicationId}')"><span class="icon nmt plxs" ></span>Remove</a></td>
					</tr>
				</#list>
			<#else>
					<tr><td colspan='4'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
			</#if>
			</tbody>
		</table>
		</div>
		<@mint.tablesort id="#mint_home_table" casesensitive="false" jsvar="mint_home_table__js"/>
		<@mint.paginate table="#mint_home_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
	</div>
</div>