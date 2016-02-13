<script type="text/javascript">
$(document).ready(function() {
		
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
function checkAll() {
	if($("#signatureAll").prop('checked')) {
		$(".signatureChk").prop('checked',true);
	} else {
		$(".signatureChk").prop('checked',false);
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
					<h3>Add Signature</h3>
				</td>
			</tr>
			<tr class="bd">
				<td style="padding:5">
					<form modelAttribute="guardianForm" action='SetupApplication.ftl' id="configureSignature" name="aform" target="_top" method="POST">
						<input type="hidden" id="tabNumber" name="tabNumber" value="1">
						<table class="bd rowheight35">
							<br />
							<tr class="lblFieldPair">
								<td class="lbl">Criticality</td>
								<td class="input">
											<select multiple id="criticality" name="criticality">
												<option value="INFO">INFO</option>
												<option value="WARNING">WARNING</option>
												<option value="ERROR">ERROR</option>
												<option value="FATAL">FATAL</option>
											</select>
								</td>
							</tr>
							<tr class="lblFieldPair">
								<td class="lbl">Signature</td>
								<td class="input">
									<textarea name="signature" value=""></textarea>
								</td>
							</tr>
							<tr class="lblFieldPair">
								<td class="lbl">Solution</td>
								<td class="input">
									<textarea name="solution" value=""></textarea>
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
					class="btn"><span>Add Signature</span></a>
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
					<th class="txtl header w15"><input class="suit" type="checkbox" id="signatureAll" onClick="checkAll()"></th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue1" tabindex="0" data-sortpath="none">Criticality</th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue2" tabindex="0" data-sortpath="none">Signature</th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue3" tabindex="0" data-sortpath="none">solution</th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue4" tabindex="0" data-sortpath="none">solution Provided By</th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue5" tabindex="0" data-sortpath="none">Date</th>
					<th class="txtl header w15">edit</th>
					<th class="txtl header w10">Remove</th>
				</tr>
			</thead>
			<tbody>
			<#list signatureList as sig>
				<tr data-sortfiltered="false">
					<td class="row"><input type="checkbox" class="signatureChk" id="${sig.signatureId!}"></td>
					<td scope="row" data-sortvalue="${sig.criticality!}">${sig.criticality!}</td>
					<td scope="row" data-sortvalue="${sig.signature!}">${sig.signature!}</td>
					<td scope="row" data-sortvalue="${sig.solution!}">${sig.solution!}</td>
					<td scope="row" data-sortvalue="${sig.providedby!}">${sig.providedby!}</td>
					<td scope="row" data-sortvalue="${sig.date!}">${sig.date!}</td>
					<td class="row"><a href="#" class="editLink np" title="Edit the application" onClick="editSignature('${sig.signatureId!}')"><span class="icon nmt plxs" ></span>Edit</a></td>
					<td class="row"><a title="Remove the application" class="deleteIcon np" href="#" onClick="deleteSignature('${sig.signatureId!}')"><span class="icon nmt plxs" ></span>Remove</a></td>
				</tr>
			</#list>
			</tbody>
		</table>
		</div>
		<@mint.tablesort id="#signature_table" casesensitive="false" jsvar="signature_table__js"/>
		<@mint.paginate table="#signature_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
	</div>
</div>