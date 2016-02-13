<script type="text/javascript">
$(document).ready(function() {
	$(".createGroupName").hide();
	$(".createGroupDescription").hide();
	$(".selectGroupName").show();

	if($("#editMode").val() == "EditMode"){
		document.getElementById("addNewGroup").disabled = true;
		$("#addNewGroup").addClass("btnOff");
	}else{
		document.getElementById("addNewGroup").disabled = false;
		$("#addNewGroup").removeClass("btnOff");
	}
	
	});
	
function showCreateGroup(){
	$(".createGroupName").show();
	$(".createGroupDescription").show();
	$(".selectGroupName").hide();
}

function editUserDetails(userId){
	document.getElementById("userId").value = userId;
	document.getElementById("editMode").value = "EditMode";
	document.getElementById("configureUserSetup").setAttribute('action',"<@spring.url '/EditUserModule.ftl' />");
	document.getElementById("configureUserSetup").setAttribute('method',"POST");
	document.getElementById("configureUserSetup").submit();
}

function deleteUserDetails(userId){
	if(confirm("Confirm delete of User details"))
	{
		document.userform.userName.value="";
		document.userform.groupId.value="-1";
		document.userform.groupName.value="";
		document.userform.groupDescription.value="";
		document.userform.emailId.value="";
		$(".createGroupName").hide();
		$(".createGroupDescription").hide();
		$(".selectGroupName").show();
		
		document.getElementById("configureUserSetup").setAttribute('action',"<@spring.url '/DeleteUserModule.ftl' />");
		document.getElementById("userId").value = userId;
		document.getElementById("configureUserSetup").setAttribute('method',"POST");
		document.getElementById("configureUserSetup").submit();
	}	
}


function clearFields(){
	document.userform.userId.value="";
	document.userform.userName.value="";
	document.userform.groupId.value="-1";
	document.userform.groupName.value="";
	document.userform.groupDescription.value="";
	document.userform.emailId.value="";
	$(".createGroupName").hide();
	$(".createGroupDescription").hide();
	$(".selectGroupName").show();
}

function validateAndsave(){
	var userName=document.userform.userName.value;
	var emailId=document.userform.emailId.value;
	if(userName==""){
		alert('User Name should not be blank');
		document.userform.userName.focus();
		return false;
	}else if(!specialCharValidate(userName)){
		alert('Special Characters are not allowed');
		document.userform.applicationName.focus();
		return false;
	}else if(!textAlphanumeric(userName)){
		alert('User Name is not alphanumeric');
		document.userform.applicationName.focus();
		return false;
	}	
	if(emailId== "")
	{
		alert('Please Enter Description');
		document.userform.emailAddr.focus();
		return false;
	}
	if(document.userform.userId.value != "" && document.userform.userId.value > 0) {
		document.getElementById("configureUserSetup").setAttribute('action',"<@spring.url '/UpdateUserSetup.ftl' />");
	} else {
		document.getElementById("configureUserSetup").setAttribute('action',"<@spring.url '/SaveUserSetup.ftl' />");
	}
		document.getElementById("configureUserSetup").submit();
}	

</script>
<section class="drop mtlg">
	<div class="cm">
		<table cellpadding='0' cellspacing='0' width="100%;">
			<tr>
				<td class="hd">
					<h3>User Setup</h3>
				</td>
			</tr>
			<tr class="bd">
				<td style="padding:5">
					<form modelAttribute="UserManagementForm" action='SaveUserSetup.ftl' id="configureUserSetup" name="userform" target="_top" method="POST">
						<input type="hidden" id="setupTabNumber" name="setupTabNumber" value="1">
						<input type="hidden" id="userId" name="userId" value="${userManagementForm.userId!}">
						<input type="hidden" id="editMode" name="editMode" value="${userManagementForm.editMode!}">
						<table class="bd rowheight35">
							<br />
							<tr class="lblFieldPair">
								<td class="lbl">User Name</td>
								<td class="input">
									<input class="input_small required" type="text" name="userName" maxlength="25" value="${userManagementForm.userName!}">
								</td>
							</tr>
							<tr class="lblFieldPair selectGroupName">
								<td class="lbl">Group Name</td>
								<td class="input"><select id="groupId" name="groupId" onchange="">
										<option value="-1" selected="true">----------Select-------------</option>
										<#list groupsDetails! as group> 
											<#if userManagementForm.groupId?exists && userManagementForm.groupId == group.groupId>
												<option value="${group.groupId!}" selected="true">${group.groupName!}</option>
											<#else>
												<option value="${group.groupId!}">${group.groupName!}</option>
											</#if> 
										</#list>
								</select> &nbsp;&nbsp; <input type="button" id="addNewGroup" value="Add New Group"
									onclick="showCreateGroup();"></td>
								</td>
							</tr>
							<tr class="lblFieldPair createGroupName">
								<td class="lbl">Group Name</td>
								<td class="input">
									<input class="input_small required" type="text" id="groupName" name="groupName" value="${userManagementForm.groupName!}" maxlength="25">&nbsp;&nbsp;
									<input type="button" value="Select Group" onclick="loadTabData(1);"></td>
								</td>
							</tr>
							
							<tr class="lblFieldPair createGroupDescription">
								<td class="lbl">Group Description</td>
								<td class="input">
									<input class="input_small required" type="text" id="groupDescription" name="groupDescription" value="${userManagementForm.groupDescription!}" maxlength="25">
								</td>
							</tr>
							
							<tr  class="lblFieldPair createGroupDescription">
							<td class="lbl">Group - Admin rights</td>
							<td class="input">
								<input type="checkbox" name="groupAdminRights" id="groupAdminRights" >
							</td>
							</tr>
							
							<tr class="lblFieldPair">
								<td class="lbl">Email Id</td>
								<td>
									<input class="required" style="width:200px" type="text" width="100" length="100" name="emailId" id="emailId" maxlength="75" value="${userManagementForm.emailId!}">
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
<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="user_setup_table_content">
  	<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
		<table id="user_setup_table" class="styleA fullwidth sfhtTable" summary="">
			<caption></caption>
			<thead>
				<tr>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype1" tabindex="0" data-sortpath="none">User Name</th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype2" tabindex="0" data-sortpath="none">Group Name</th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype3" tabindex="0" data-sortpath="none">Group Description</th>
					<th class="txtl header w25" scope="col" data-sortfilter="false" data-sortdatatype="string" data-sort="true" data-sortname="ftype3" tabindex="0" data-sortpath="none">Admin Rights</th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Email Id</th>
					<th class="txtl header w15">edit</th>
					<th class="txtl header w10">Remove</th>
				</tr>
			</thead>
			<tbody>
			<#if usersDetails?has_content>
				<#list usersDetails! as userSetup>
					<tr data-sortfiltered="false">
						<td scope="row" data-sortvalue="${userSetup.userName!}">${userSetup.userName!}</td>
						<td scope="row" data-sortvalue="${userSetup.group.groupName!}">${userSetup.group.groupName!}</td>
						<td scope="row" data-sortvalue="${userSetup.group.groupDescription!}">${userSetup.group.groupDescription!}</td>
						<#if userSetup.group.groupAdminRights == true>
						<td scope="row" >Yes</td>
						<#else>
						<td scope="row" >No</td>
						</#if>
						
						<td scope="row" data-sortvalue="${userSetup.emailId!}">${userSetup.emailId!}</td>
						<td class="row"><a href="#" class="editLink np" title="Edit the application" onClick="editUserDetails('${userSetup.userId!}')"><span class="icon nmt plxs" ></span>Edit</a></td>
						<td class="row"><a title="Remove the application" class="deleteIcon np" href="#" onClick="deleteUserDetails('${userSetup.userId!}')"><span class="icon nmt plxs" ></span>Remove</a></td>
					</tr>
				</#list>
			<#else>
					<tr><td colspan='7'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
			</#if>
			</tbody>
		</table>
		</div>
		<@mint.tablesort id="#user_setup_table" casesensitive="false" jsvar="user_setup_table__js"/>
		<@mint.paginate table="#user_setup_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
	</div>
</div>
