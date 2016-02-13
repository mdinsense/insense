<script type="text/javascript">
$(document).ready(function() {
	if(document.aform.applicationId.value <=0 || document.aform.applicationName.value =="") {
		clearFields();
	}
});
function getApplicationDetailsForEdit(applicationId)
{
	document.aform.applicationId.value=applicationId;
	document.getElementById("configureSignature").setAttribute('action',"<@spring.url '/EditSignature.ftl' />");
	document.getElementById("configureSignature").setAttribute('method',"POST");
	document.getElementById("configureSignature").submit();
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
		document.getElementById("configureSignature").setAttribute('action',"<@spring.url '/UpdateSignature.ftl' />");
	} else {
		document.getElementById("configureSignature").setAttribute('action',"<@spring.url '/SaveSignature.ftl' />");
	}
		document.getElementById("configureSignature").submit();
}	
function deleteSignature(signatureId)
{
	if(confirm("Confirm delete of Signature details setup"))
	{
		document.aform.applicationId.value=applicationId;
		document.getElementById("configureSignature").setAttribute('action',"<@spring.url '/DeleteSignature.ftl' />");
		document.getElementById("configureSignature").setAttribute('method',"POST");
		document.getElementById("configureSignature").submit();
	}
}
function clearFields() {
	document.aform.applicationId.value="";
	document.aform.applicationName.value= "";
	document.aform.applicationDescription.value="";
}
function checkAll() {
	if($("#signatureAll").prop("checked")) {
		$(".signatureChk").prop("checked",true);
	} else {
		$(".signatureChk").prop("checked",false);
	}
}
function checkThis(id) {
	$("#signatureAll").prop("checked",false);
	if($("#"+id).prop("checked")) {
		
	} else {
		
	}
}
</script>
<section class="drop mtlg">
	<div class="cm">
		<table cellpadding='0' cellspacing='0' width="100%;">
			<tr>
				<td class="hd">
					<h3>Configure FCE Monitoring</h3>
				</td>
			</tr>
			<tr class="bd">
				<td style="padding:5">
					<form modelAttribute="guardianForm" action='SetupApplication.ftl' id="configureSignature" name="aform" target="_top" method="POST">
						<input type="hidden" id="tabNumber" name="tabNumber" value="1">
						<table class="bd rowheight35">
							<br />
							<tr class="lblFieldPair">
								<td class="lbl">Application</td>
								<td class="input">
											<select multiple id="application" name="application">
												<option value="IWC">IWC</option>
												<option value="IFA">IFA</option>
												<option value="PlanFocus">PlanFocus</option>
											</select>
								</td>
							</tr>
							<tr class="lblFieldPair">
								<td class="lbl">Environment</td>
								<td class="input">
											<select multiple id="environment" name="environment">
												<option value="Prod">Prod</option>
												<option value="ProdFix">ProdFix</option>
												<option value="AT">AT</option>
											</select>
								</td>
							</tr>
							<tr class="lblFieldPair">
								<td class="lbl">Tier</td>
								<td class="input">
											<select multiple id="tier" name="tier">
												<option value="Tier-1">Tier-1</option>
												<option value="Tier-1">Tier-2</option>
												<option value="Tier-1">Tier-3</option>
											</select>
								</td>
							</tr>
							<tr class="lblFieldPair">
								<td class="lbl">Signatures</td>
								<td class="input">
										<select multiple id="signature" name="signature">
												<option value="signature-1">signature-1</option>
												<option value="signature-2">signature-2</option>
												<option value="signature-3">signature-3</option>
										</select>
								</td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
			<tr class="bd">
				<td class="btnBar"><a href="#" onClick="validateAndsave()"
					class="btn"><span>Add Monitor</span></a>
				</td>
			</tr>
		</table>
	</div>
</section>
<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="signature_table_content">
  	<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
		<table id="signature_table" class="styleA fullwidth sfhtTable" summary="">
			<caption></caption>
			<thead>
				<tr>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Application</th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Environment</th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Signature</th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Tier</th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Date</th>
					<th class="txtl header w15">edit</th>
					<th class="txtl header w10">Remove</th>
				</tr>
			</thead>
			<tbody>
			<#list monitorList! as monitor>
				<tr data-sortfiltered="false">
					<td scope="row" data-sortvalue="${monitor.application!}">${monitor.application!}</td>
					<td scope="row" data-sortvalue="${monitor.environment!}">${monitor.environment!}</td>
					<td scope="row" data-sortvalue="${monitor.signature!}">${monitor.signature!}</td>
					<td scope="row" data-sortvalue="${monitor.tier!}">${monitor.tier!}</td>
					<td scope="row" data-sortvalue="${monitor.date!}">${monitor.date!}</td>
					<td class="row"><a href="#" class="editLink np" title="Edit the application" onClick="editSignature('${monitor.signatureId}')"><span class="icon nmt plxs" ></span>Edit</a></td>
					<td class="row"><a title="Remove the application" class="deleteIcon np" href="#" onClick="deleteSignature('${monitor.signatureId}')"><span class="icon nmt plxs" ></span>Remove</a></td>
				</tr>
			</#list>
			</tdoby>
		</table>
		</div>
		<@mint.tablesort id="#signature_table" casesensitive="false" jsvar="signature_table__js"/>
		<@mint.paginate table="#signature_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
	</div>
</div>