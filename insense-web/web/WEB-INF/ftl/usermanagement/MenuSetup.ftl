
<style type="text/css">
#rgihtSide {
	vertical-align:text-top;
}
</style>
<script>
$(document).ready(function(){
	if($("#groupId").val() > 0) {
		checkSelectedMenus();
	}
	checkAllAccess();
}); 
$(function(){
	   $('#select_all').click(function(event) {  
	          if(this.checked) {
	        	$(".checkbox1").prop("disabled",true);
	        	$(".checkbox2").prop("disabled",true);
	            $('.checkbox1').each(function() {
	                this.checked = true;                        
	            });
	            $('.checkbox2').each(function() {
	                this.checked = false;                        
	            });
	        }
	        if(!this.checked) {
	        	$(".checkbox1").prop("disabled",false);
	        	$(".checkbox2").prop("disabled",false);
	            $('.checkbox1').each(function() {
	                this.checked = false;                        
	            });
	            $('.checkbox2').each(function() {
	                this.checked = false;                        
	            });
	        }
	   });
});
function submitAccessDetails() {
	$(".checkbox1").prop("disabled",false);
	$(".checkbox2").prop("disabled",false);
	if($("#groupId").val() == -1) {
		alert("Please select any user group to proceed");
		return false;
	}
	var items = new Array();
	$('#sampleList li input:checkbox:checked').each(function() {
	    items.push($(this).val());
	});
	$('#userAccessForm').attr('action', 'SaveAccessControl.ftl');
	$('#userAccessForm').submit()
}

function checkSelectedMenus(){
	 $('.checkbox1').each(function() {
         this.checked = false;                        
     });
	var userId="0";
	var groupId=document.userAccessForm.groupId.value;
	 $('.checkbox1').each(function() {
		 var menuId=this.value;
		 var menuName=this.name;
		 var isFunctionality=false;
		 if(menuName=='functionalityIdList') {
			 isFunctionality = true;
		 }
		 var request = $.ajax({
				url: "CheckSelectedMenu.ftl",  
				type: "POST",  
				cache: false,
				data: { 'userId' : userId, 'groupId' : groupId, 'menuId': menuId, 'isFunctionality' : isFunctionality},  
				dataType: "json"}); 
					request.done(function( msg ) {				
						var Outmsg= JSON.stringify(msg);
					       Outmsg = JSON.parse(Outmsg);
					       $.each(Outmsg,function(key, val) {
					    	   if (val=="M"){
					           		document.getElementById(key).checked = true;   
					    	   } else if(val=="F"){
					    		   document.getElementById("func"+key).checked = true;  
							    } else {
							    	 document.getElementById(menuId).checked = false;
							    }
					   		});
					       checkAllAccess();
		 		 }); 
		  	request.fail(
		 	 function( jqXHR, textStatus ){  
		  	});   
	  });
}
function checkAllAccess() {
	 var allChecked = 0;
	 $('.checkbox1').each(function() {
         if(!this.checked) {
        	 allChecked = 1;
         }                        
     });
     $('.checkbox2').each(function() {
    	 if(!this.checked) {
        	 allChecked = 1;
         }                   
     });
     if(allChecked == 0) {
    	  $('#select_all').prop("checked",true);
    	  $(".checkbox1").prop("disabled",true);
    	  $(".checkbox2").prop("disabled",true);
     } else {
    	  $('#select_all').prop("checked",false);
    	  $(".checkbox1").prop("disabled",false);
    	  $(".checkbox2").prop("disabled",false);
     }
}
</script>
<form modelAttribute="UserManagementForm" action='SaveGroupSetup.ftl'
	id="userAccessForm" name="userAccessForm" target="_top" method="POST">

	<section class="drop mtlg">
	<div class="cm">
		<table cellpadding='0' cellspacing='0' border='0' width='100%'>
			<tr>
				<td class="hd">
					<h3>Menu Access</h3>
				</td>
			</tr>
			<tr class="bd">
				<td style="padding: 5">
					<center>
						<div>
							<input type="hidden" id="setupTabNumber" name="setupTabNumber"
								value="${userManagementForm.setupTabNumber!}">
							<table class="Datatable mt mtlg" style="width: 80%;">
								<tr id="groupRow">
									<td class="txtl hd" width="50%">
										<span>
											<div
												style="float: left; margin-left: 10px; margin-top: 5px; vertical-align: middle;">
												<font face="verdana,arial" size=-1><div id="roleLabel">Group
														Name</div> </font>
											</div>
										</span>
										<span>
											<div style="float: left; margin-left: 10px; margin-top: 0px;">
												<select id="groupId" name="groupId" tabindex="3" onchange="checkSelectedMenus()">
													<option value="-1">Select</option>
													<#list groupList! as group> <#if
													userManagementForm.groupId?exists &&
													userManagementForm.groupId == group.groupId>
													<option value="${group.groupId}" selected="true">${group.groupName}</option>
													<#else>
													<option value="${group.groupId}">${group.groupName}</option>
													</#if> </#list>
												</select>
	

											</div>
										</span>
										
									</td>
									<td class="hd">
										<input type="checkbox" name="select_all" id="select_all">
										<b>All Access</b>
									</td>
								</tr>
							</table>
						</div>
						<table id="megatable" class="Datatable mt" colspan="1" colspace="1" style="width:80%; margin-top: 20px;">
							<tr></tr>
							<tr>
								<td width="50%">
									<table class="Datatable mt" colspan="1" colspace="1"  id="menuTable" width="40%" name="menus">
										<#list Session.menuList! as menu>
										<#if (menu_index < menuPaginationCount) && (menu.menu.menuId >= 0) >
										<tr>
											<td style="text-align: left;font-size:16"><b>${menu.menu.menuName}</b></td>
										</tr>
										<#list menu.menu.functionality! as funct>
												<tr>
													<td style="text-align: left;font-style:italic;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
														type="checkbox" id="func${funct.functionalityId}" value="${funct.functionalityId}" 
														name="functionalityIdList" onClick="#" class="checkbox1 functionalityIdList"> &nbsp;&nbsp; ${funct.functionalityName}</td>
												</tr> 
										</#list>
											<#list menu.childMenus! as childMenu>
												<tr>
													<td style="text-align: left;font-size:14">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
														type="checkbox" id="${childMenu.menuId}" name="menuIdList" value="${childMenu.menuId}"
														onClick="#" class="checkbox1"><b> &nbsp;&nbsp; ${childMenu.menuName}</b></td>
												</tr> 
												<#list childMenu.functionality! as funct>
												<tr>
													<td style="text-align: left;font-style:italic;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
														type="checkbox" id="func${funct.functionalityId}" value="${funct.functionalityId}" 
														name="functionalityIdList" onClick="#" class="checkbox1"> &nbsp;&nbsp; ${funct.functionalityName}</td>
												</tr> 
												</#list>
											</#list> 
										</#if>
										</#list>
									</table>
								</td>
								<td width="50%" id="rgihtSide">
									<table class="Datatable mt" colspan="1" colspace="1" width="40%" id="menuTable" name="menus">
										<#list Session.menuList! as menu>
										<#if (menu_index >= menuPaginationCount) && (menu.menu.menuId >= 0) >
										<tr>
											<td style="text-align: left;font-size:16"><b>${menu.menu.menuName}</b></td>
										</tr>
											<#list menu.childMenus! as childMenu>
												<tr>
													<td style="text-align: left;font-size:14">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
														type="checkbox" id="${childMenu.menuId}" value="${childMenu.menuId}" 
														name="menuIdList" onClick="#" class="checkbox1"><b>  &nbsp;&nbsp; ${childMenu.menuName}</b></td>
												</tr> 
												<#list childMenu.functionality! as funct>
												<tr>
													<td style="text-align: left;font-style:italic;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
														type="checkbox" id="func${funct.functionalityId}" value="${funct.functionalityId}" 
														name="functionalityIdList" onClick="#" class="checkbox1">  &nbsp;&nbsp; ${funct.functionalityName}</td>
												</tr> 
												</#list>
											</#list> 
										</#if>
										</#list>
									</table>
								</td>
							</tr>
						</table>
					</center>
				</td>
			</tr>
			<tr class="bd">
			<td class="btnBar">
				<a href="#" onClick="submitAccessDetails()" class="btn"><span>Submit</span></a>
			</td>
		</tr>
		</table>
	</div>
	</section>
</form>