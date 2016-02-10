<#import "spring.ftl" as spring />
<#import "MacroTemplates.ftl" as mint />
<div id="ManageSuits">
   <#include "Header.ftl">
</div>
<head>
   <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>

    <script src="js/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script type="text/javascript" src="js/formValidation.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
    <script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
	
 <script>
 $(document).ready(function(){
	 $("#privatesuitId").hide();
	 document.getElementById("groupsId").disabled=false;
	 document.getElementById("assignGroupId").disabled=false;
	 
	 $(".groupdetails").hide();
	 $('.manageSuits-add-all').click(function(){
		    $('#groupsId option:selected').appendTo('#assignGroupId'); 
		});
		$('.manageSuits-remove-all').click(function(){
		    $('#assignGroupId option:selected').appendTo('#groupsId'); 
		});
		<#if regressionTestSuit?has_content>
			if($("#suitId").val() != null) {
				var suitId = $("#suitId").val();
				if(isSuitExists(suitId) == true) {
					loadGroupsandAssignGroupsDropdown(document.getElementById("suitId").value,"groupsId","assignGroupId");
					$("#"+suitId).prop('checked', true);
					$(".groupdetails").show();
				}
			}
		</#if>
});
function isSuitExists(suitId) {
	var isExists = false;
	$(".suit").each(function() {
		  if($( this ).attr('id') == suitId) {
			  isExists = true;
		  }
	});
	return isExists;
}
function checkSuitname(elem, suitId, privateSuit){
	$(".groupdetails").show();
	$(".suit").each(function() {
		  $( this ).prop('checked', false);
	});
	$( elem ).prop('checked', true);
	if(elem.checked==true) {
		if(privateSuit == true){
			$("#privatesuitId").show();
			document.getElementById("groupsId").disabled=true;
			document.getElementById("assignGroupId").disabled=true;
		}else{
			$("#privatesuitId").hide();
			document.getElementById("groupsId").disabled=false;
			document.getElementById("assignGroupId").disabled=false;
		}
		if(suitId > 0){
			document.getElementById("groupsId").options.length=0;
			document.getElementById("assignGroupId").options.length=0;
			document.getElementById("suitId").value = suitId;
			loadGroupsandAssignGroupsDropdown(suitId,"groupsId","assignGroupId");
		}
	} 
}

function loadGroupsandAssignGroupsDropdown(suitId,groupsId,assignGroupId){
	 var dataToSend = {
			 'suitId' : suitId
		      }; 
	 
	var request = $.ajax({  
		url: "GetAllorPendingGroupsForManageSuits.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 

	request.done(function( msg ) {	
		var Outmsg= JSON.stringify(msg);
		Outmsg = JSON.parse(Outmsg);

		$.each(Outmsg,function(key, val) {
			 $('<option />', {value: key, text: val}).appendTo("#"+groupsId);
		});
			
	});
	
	request.fail(function( jqXHR, textStatus ){  
	}); 
	
	var request = $.ajax({  
		url: "GetGroupDetailsForManageSuits.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 

	request.done(function( msg ) {	
		var Outmsg= JSON.stringify(msg);
		Outmsg = JSON.parse(Outmsg);

		$.each(Outmsg,function(key, val) {
			 $('<option />', {value: key, text: val}).appendTo("#"+assignGroupId);
		});
			
	});
	
	request.fail(function( jqXHR, textStatus ){  
		
	});
}

function editSuitDetails(suitId,editOrViewMode) {
	document.SuitForm.suitId.value = suitId;
	document.SuitForm.editOrViewMode.value = editOrViewMode;
	document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/ChoosingOnEditSuitDetails.ftl' />");
	document.getElementById("SuitForm").setAttribute('method',"POST");
	document.getElementById("SuitForm").submit();	
}

function removeSuite(suitId) {
	
	if(	confirm("Please Confirm to remove this suit")){
		document.SuitForm.suitId.value = suitId;
		document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/removeSuitDetails.ftl' />");
		document.getElementById("SuitForm").setAttribute('method',"POST");
		document.getElementById("SuitForm").submit();
  	}
}

function listSuits(){
	document.getElementById("suitId").value = "";
	document.getElementById("groupsId").options.length=0;
	document.getElementById("assignGroupId").options.length=0;
	document.getElementById("groupId").value = document.SuitForm.uiSuitsCategory.options[document.SuitForm.uiSuitsCategory.selectedIndex].value;
 	document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/RetrieveManageSuits.ftl' />");
	document.getElementById("SuitForm").setAttribute('method',"POST");
	document.getElementById("SuitForm").submit(); 
}

function refreshValue(){
	window.location.reload(true);
}

function validateAndsave(){
	if(document.getElementById("suitId") > 0){
		alert('Please Select the Suit');
		return false;
	}
	
	$('#assignGroupId option').prop('selected', true); 
	$('#groupsId option').prop('selected', true); 
	
	document.getElementById("SuitForm").setAttribute('action',"<@spring.url '/SaveManageSuits.ftl' />");
	document.getElementById("SuitForm").submit();
}

</script>
</head>
  
 <body>
<div id=commonDv">

<div class="content twoCol" style="width:80%;">
<form modelAttribute="RegressionTestExecutionForm"  id="SuitForm" name="SuitForm" target="_top" method="POST" >
<input type="hidden" id="editOrViewMode"  name="editOrViewMode">
<input type="hidden" id="suitId"  name="suitId" value="${regressionTestExecutionForm.suitId!}">
<input type="hidden" id="groupId"  name="groupId" value="${regressionTestExecutionForm.groupId!}">

	<div class="page-content" id="pagecontent" role="main" tabindex="-1">
		<#if Success?exists>
			<div class="infoModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${Success}
				</div>
			</div>
			</#if> 
			<#if error?exists>
			<div class="alertModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${error}
				</div>
			</div>
		</#if>
		<hr style="margin-top: 10px;">
		<div class="mblg">
			<div class="mtxs">
		 		<a id="tcId100" class="tipLink rel" name="tcId100" >Help</a><b><@spring.message "mint.help.managesuit"/></b><a href="<@spring.message 'mint.help.mailTo'/>" target="_top">
		 		<@spring.message "mint.help.mailId"/></a>
			</div>
		</div>
			
			<hr style="margin-top: 0px;">
			
	 		
			<table cellspacing="0" cellpadding="0" width="100%" >
				<tr class="bd">
					<td valign="middle" style="padding:10;width:8.5%;font-weight: bold;">Show Suits :</td>
					<td class="input" style="width:15%;">
						<select id="uiSuitsCategory" name="uiSuitsCategory" onchange = "listSuits()" >
					        <#list uiManageSuitsList!?keys as key>
					         	<#if selectedShowSuits! == "${key!}">
									<option value="${key!}" selected>${uiManageSuitsList[key]!}</option>
								<#else>
									<option value="${key!}">${uiManageSuitsList[key]!}</option>
								</#if>
					        </#list>
   
						</select>
					</td>
			
					<td valign="middle" style="padding:10;width:10%;font-weight: bold;">&nbsp;&nbsp;Testing Type :</td>
					<td class="input" style="width:15%;">
						<select id="solutionType" name="solutionType">
							<#list solutionTypeList! as solutionType>
								<option value="${solutionType.solutionTypeId}">${solutionType.solutionTypeName!}</option>
							</#list>
						</select>
					</td>
					
				</tr>
			</table>
			
			<hr>
		</div>
		<div class="content" tabindex="0" style="display: block;width:100%;" id="manage_suits_table_content">
  			<div class="bd">
  			<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
				<table id="manage_suits_table" class="styleA fullwidth sfhtTable" summary="">
					<caption></caption>
					<thead>
						<tr>
							<th class="txtl header w10">Select Suit Name</th>
							<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype1" tabindex="0" data-sortpath="none">Test Suit Name</th>
							<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype2" tabindex="0" data-sortpath="none">Application Name</th>
							<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype3" tabindex="0" data-sortpath="none">Environment Category</th>
							<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype4" tabindex="0" data-sortpath="none">Created By Group</th>
							<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype5" tabindex="0" data-sortpath="none">Type</th>
							<th class="txtl header w10">Edit / View</th>
							<th class="txtl header w10">Remove</th>
						</tr>
					</thead>
					<tbody>
					<#if regressionTestSuit?has_content>
						<#list regressionTestSuit! as suit>
							<tr data-sortfiltered="false">
								<td class="row">
								<input class="suit" type="checkbox" name="suitname" id="${suit.suitId}"
									onClick="<#if suit.privateSuit>checkSuitname(this,${suit.suitId},true);<#else>checkSuitname(this,${suit.suitId},false);</#if>">
								</td>
								
								<#if suit.privateSuit>
									<td scope="row" data-sortvalue="${suit.suitName!}">${suit.suitName!} (Private)</td>
								<#else>
									<td class="row" data-sortvalue="${suit.suitName!}">${suit.suitName!}</td>
								</#if>
									
								<#assign applicationNameForSuit="">
								<#list applicationList as application>
									<#if application.application.applicationId ==suit.applicationId>
										<#assign applicationNameForSuit="${application.application.applicationName!}">
									</#if>
								</#list>
								
								<td class="row" data-sortvalue="${applicationNameForSuit}">
									${applicationNameForSuit}  
								</td>
								
								<#assign environmentCategoryForSuit="">
								<#list environmentCategoryList as environmentCategory>
									<#if environmentCategory.environmentCategoryId == suit.environmentCategoryId>
										<#assign environmentCategoryForSuit="${environmentCategory.environmentCategoryName}">
									</#if>
								</#list>
								<td class="row" data-sortvalue="${environmentCategoryForSuit}">
									${environmentCategoryForSuit}
								</td>
								<td scope="row" data-sortvalue="${suit.users.group.groupName}">
									${suit.users.group.groupName}
								</td>
								<td scope="row" data-sortvalue="${suit.type!}">
									${suit.type!}
								</td>
								<td scope="row">
									<a href="#" class="editLink np" id="edit_${suit.suitId!}" title="Edit the application" onClick="editSuitDetails(${suit.suitId!},'EditMode')"><span class="icon nmt plxs" ></span>Edit</a>
								</td>
								<td class="row">
									<a title="Delete Suit" class="deleteIcon np" href="#" id="delete_${suit.suitId!}" onClick="removeSuite(${suit.suitId!})"><span class="icon nmt plxs" ></span>Remove</a>
								</td>
							</tr>
						</#list>
					<#else>
						<tr><td colspan='8'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
					</#if>
					</tdoby>
				</table>
			</div>
			<@mint.tablesort id="#manage_suits_table" casesensitive="false" jsvar="manage_suits_table__js"/>
			<@mint.paginate table="#manage_suits_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
			</div>
		</div>
		
			<div class="alertModule" id="privatesuitId" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information" style="color: red;"></span>Private Suit. Can't be assigned
				</div>
			</div>
		
		<section class="mtlg groupdetails">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Assign Suit</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<table class="bd rowheight35" id="moduleId">
									<tr class='lblFieldPair'>
											<td class="lbl">Groups</td>
											<td class="input">
												<select multiple id="groupsId" name="groupsId"></select> 
											    <a href="#" id="addall" class="btn manageSuits-add-all"><span> >>> </span></a>
											    <a href="#" id="removeall" class="btn manageSuits-remove-all"><span> <<< </span></a>&nbsp;&nbsp;
											    <select multiple id="assignGroupId" name="assignGroupId"></select>
											</td>
										</tr>
								</table>
						</td>
					</tr>
				</table>
			</div>
		</section>
		<section class="mtlg groupdetails">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
				<tr class="bd">
				<td class="btnBar">
					<a href="#" onClick="validateAndsave()" class="btn"><span>Submit</span></a>
				</td>
				</tr>
			</table>
			</div>
		</section>
		</form>

 		</div>
</div>

	

</body>
</html>
   