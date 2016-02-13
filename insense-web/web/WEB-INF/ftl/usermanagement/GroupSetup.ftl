<script type="text/javascript">

$(document).ready(function() {
	$('.add-all').click(function(){
	    $('#applicationId option:selected').appendTo('#applicationIdSecond'); 
	});
	$('.remove-all').click(function(){
	    $('#applicationIdSecond option:selected').appendTo('#applicationId'); 
	});
	
	$('.category-add-all').click(function(){
	    $('#environmentCategoryId option:selected').appendTo('#environmentCategoryIdSecond'); 
	});
	$('.category-remove-all').click(function(){
	    $('#environmentCategoryIdSecond option:selected').appendTo('#environmentCategoryId'); 
	});
	
	if($("#groupId").val() != null) {
		getApplicationList();
	}
	
	});
	
function getApplicationList() {
	var groupId = document.groupform.groupId.options[document.groupform.groupId.selectedIndex].value;
	document.getElementById("applicationId").options.length=0;
	document.getElementById("environmentCategoryId").options.length=0;
	document.getElementById("applicationIdSecond").options.length=0;
	document.getElementById("environmentCategoryIdSecond").options.length=0;
	if(groupId > 0){
		//loadApplicationDropdown(groupId,"applicationId");
		//loadEnvironmentCategoryDropdown(groupId,"environmentCategoryId");
		loadApplicationDropdownSecond(groupId,"applicationId","applicationIdSecond");
		loadEnvironmentCategoryDropdownSecond(groupId,"environmentCategoryId","environmentCategoryIdSecond");
	}
}

	function validateAndsave() {
		
		if( $('#groupId').val() == -1) {
			alert('Please Select Group Name');
			return false;
		}
		
		$('#applicationIdSecond option').prop('selected', true); 
		$('#environmentCategoryIdSecond option').prop('selected', true); 
		
		document.getElementById("configureGroupSetup").setAttribute('action',"<@spring.url '/SaveGroupSetup.ftl' />");
		document.getElementById("configureGroupSetup").submit();
	}
	
	function clearAll(){
		document.getElementById("groupId").value = -1;
		document.getElementById("applicationId").options.length=0;
		document.getElementById("environmentCategoryId").options.length=0;
		document.getElementById("applicationIdSecond").options.length=0;
		document.getElementById("environmentCategoryIdSecond").options.length=0;
	}

</script>
<section class="drop mtlg">
	<div class="cm">
	
	<table cellpadding='0' cellspacing='0' border='0' width='100%'>
		<tr>
			<td class="hd">
				<h3>Group Access</h3>
			</td>
		</tr>
		<tr class="bd">
			<td style="padding: 5">
				<body onload="isSecureSite()">
					<form modelAttribute="UserManagementForm"
						action='/SaveGroupSetup.ftl' id="configureGroupSetup" name="groupform"
						enctype="multipart/form-data" target="_top" method="POST"
						onSubmit="return validateAndsave();">
						<input type="hidden" id="setupTabNumber" name="setupTabNumber" value="2">
						<table class="bd rowheight35">
						    <br/>
							<tr class="lblFieldPair selectGroupName">
								<td class="lbl">Group Name</td>
								<td class="input"><select id="groupId" name="groupId" onchange="getApplicationList();">
										<option value="-1" selected="true">----------Select-------------</option>
										<#list groupsDetails! as group> 
											<#if userManagementForm.groupId?exists && userManagementForm.groupId == group.groupId>
												<option value="${group.groupId!}" selected="true">${group.groupName!}</option>
											<#else>
												<option value="${group.groupId!}">${group.groupName!}</option>
											</#if> 
										</#list>
								</select><span class="required"></span> </td>
								</td>
							</tr>
							
							<tr class="lblFieldPair">
								<td class="lbl">Application Name</td>
								<td class="input">
									<select multiple id="applicationId" name="applicationId"></select>
								    <a href="#" class="btn add-all"><span> >>> </span></a>
								    <a href="#" class="btn remove-all"><span> <<< </span></a>&nbsp;&nbsp;
								    <select multiple id="applicationIdSecond" name="applicationIdSecond"></select>
								</tr>
							</tr>
							<tr class="lblFieldPair">
								<td class="lbl">Environment Category</td>
								<td class="input">
									<select multiple id="environmentCategoryId" name="environmentCategoryId"></select> 
								    <a href="#" class="btn category-add-all"><span> >>> </span></a>
								    <a href="#" class="btn category-remove-all"><span> <<< </span></a>&nbsp;&nbsp;
								    <select multiple id="environmentCategoryIdSecond" name="environmentCategoryIdSecond"></select>
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
