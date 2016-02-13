<#import "../common/spring.ftl" as spring />
<#import "../common/MacroTemplates.ftl" as mint />
<div id="User Access Setup">
   <#include "../common/Header.ftl">
</div>
<style type="text/css">
  .tab-box {
  	margin-top: 1%;
  }
</style>
<script type="text/javascript" src="js/formValidation.js"></script>	
<script type="text/javascript" src="js/script.js"></script>
<script language="JavaScript">
var currentMenu = ${userManagementForm.setupTabNumber};
$(document).ready(function(){
	 $(".tabLink").each(function(){
	      $(this).click(function(){
	        tabeId = $(this).attr('id');
	        $(".tabLink").removeClass("activeLink");
	        $(this).addClass("activeLink"); 
	        return false;	  
	      });
	});
	$(".tabLink").removeClass("activeLink");
	$("#cont-${userManagementForm.setupTabNumber}").addClass("activeLink");
}); 

function loadTabData(setupTabNumber) {
	var form = $(document.createElement('form'));
	$('body').append(form);
	$(form).attr("action", "<@spring.url '/UserAccessSetup.ftl' />");
	$(form).attr("method", "POST");
	
	var input1 = $("<input>").attr("type", "hidden").attr("name", "setupTabNumber").val(setupTabNumber);
	$(form).append($(input1));
	$(form).submit();
}

function loadApplicationDropdown(groupId,applicationList){
	
	 var dataToSend = {
			 'groupId' : groupId
		      }; 
	 
	var request = $.ajax({  
		url: "GetAppliactionListByGroupName.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 

	request.done(function( msg ) {	
		var Outmsg= JSON.stringify(msg);
		Outmsg = JSON.parse(Outmsg);
		
				$.each(Outmsg,function(key, val) {
					var requestReference = $.ajax({  
						url: "GetAppliactionReferenceForGroupName.ftl",
						type: "POST", 
						cache: false,
						data: {'groupId' : groupId},
						dataType: "json"
					});
					requestReference.done(function( msg ) {	
						var OutmsgRef= JSON.stringify(msg);
						OutmsgRef = JSON.parse(OutmsgRef);
						 $.each(OutmsgRef,function(key, val) {
				                $('#'+applicationList+'> option').each(function() {
				                    if(($(this).val() == key)) {
				                       $(this).attr("selected","true");
				                    }
				                });
				            });
					});
					requestReference.fail(function( jqXHR, textStatus ){ 
					});
				
		     $('<option />', {value: key, text: val}).appendTo("#"+applicationList);
		});
	});
	
	request.fail(function( jqXHR, textStatus ){  
	}); 
} 

function loadApplicationDropdownSecond(groupId,applicationList,applicationListSecond){
	 var dataToSend = {
			 'groupId' : groupId
		      }; 
	 
	var request = $.ajax({  
		url: "GetAllOrPendingApplicationForGroup.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 

	request.done(function( msg ) {	
		var Outmsg= JSON.stringify(msg);
		Outmsg = JSON.parse(Outmsg);

		$.each(Outmsg,function(key, val) {
			 $('<option />', {value: key, text: val}).appendTo("#"+applicationList);
		});
			
	});
	
	request.fail(function( jqXHR, textStatus ){  
	}); 
	
	var request = $.ajax({  
		url: "GetAppliactionReferenceForGroupName.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 

	request.done(function( msg ) {	
		var Outmsg= JSON.stringify(msg);
		Outmsg = JSON.parse(Outmsg);

		$.each(Outmsg,function(key, val) {
			 $('<option />', {value: key, text: val}).appendTo("#"+applicationListSecond);
		});
			
	});
	
	request.fail(function( jqXHR, textStatus ){  
	});
}

function loadEnvironmentCategoryDropdown(groupId,environmentCategoryList){
	
	 var dataToSend = {
			 'groupId' : groupId
		      }; 
	 
	var request = $.ajax({  
		url: "GetEnvironmentCategoryForGroup.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 

	request.done(function( msg ) {	
		var Outmsg= JSON.stringify(msg);
		Outmsg = JSON.parse(Outmsg);

		$.each(Outmsg,function(key, val) {
				var requestReference = $.ajax({  
					url: "GetEnvironmentCategoryReferenceForGroup.ftl",
					type: "POST", 
					cache: false,
					data: {'groupId' : groupId},
					dataType: "json"
				});
				requestReference.done(function( msg ) {	
					var OutmsgRef= JSON.stringify(msg);
					OutmsgRef = JSON.parse(OutmsgRef);
					 $.each(OutmsgRef,function(key, val) {
			                $('#'+environmentCategoryList+'> option').each(function() {
			                    if(($(this).val() == key)) {
			                       $(this).attr("selected","true");
			                    }
			                });
			            });
				});
				requestReference.fail(function( jqXHR, textStatus ){  
				});
			
         $('<option />', {value: key, text: val}).appendTo("#"+environmentCategoryList);
    });
	});
	
	request.fail(function( jqXHR, textStatus ){  
	}); 
} 
function loadEnvironmentCategoryDropdownSecond(groupId,environmentCategoryId,environmentCategoryIdSecond){
	 var dataToSend = {
			 'groupId' : groupId
		      }; 
	 
	var request = $.ajax({  
		url: "GetAllOrPendingEnvironmentCategoryForGroup.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 

	request.done(function( msg ) {	
		var Outmsg= JSON.stringify(msg);
		Outmsg = JSON.parse(Outmsg);

		$.each(Outmsg,function(key, val) {
			 $('<option />', {value: key, text: val}).appendTo("#"+environmentCategoryId);
		});
			
	});
	
	request.fail(function( jqXHR, textStatus ){  
	}); 
	
	var request = $.ajax({  
		url: "GetEnvironmentCategoryReferenceForGroup.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 

	request.done(function( msg ) {	
		var Outmsg= JSON.stringify(msg);
		Outmsg = JSON.parse(Outmsg);

		$.each(Outmsg,function(key, val) {
			 $('<option />', {value: key, text: val}).appendTo("#"+environmentCategoryIdSecond);
		});
			
	});
	
	request.fail(function( jqXHR, textStatus ){  
	});
}

function loadFunctionalityDropdown(functionalityId,functionalityTable){
	var dataToSend = {
			 'functionalityId' : functionalityId
		      }; 
	 
	var request = $.ajax({  
		url: "GetFunctionalityListByPageName.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 

	request.done(function( msg ) {	
		var Outmsg= JSON.stringify(msg);
		Outmsg = JSON.parse(Outmsg);
		
		$tbody = $('#'+functionalityTable);
	    $tbody.append('<tr><th>Functionality Id</th><th> Functionality Value </th></tr>');
	    
		$.each(Outmsg, function (key, val) {
	    	$tbody.append('<tr><td>'+key+'</td><td>'+val+'</td></tr>');
		});
	
	});
	
	request.fail(function( jqXHR, textStatus ){  
	});
}
</script>
<head>	

</script>
</head>
<body onLoad="window.scrollTo(0,readCookie('ypos'))">

<div style="width:80%;" class="content twoCol">
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
			<#if NotValidFormat?exists>
			<div class="alertModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${NotValidFormat}
				</div>
			</div>
			</#if>
			<#if CompileIssues?exists>
			<div class="alertModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${CompileIssues}
				</div>
			</div>
			</#if>
			<#if skipError?exists>
			<div class="alertModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${skipError}
				</div>
			</div>
			</#if>
			<#if crawlError?exists>
			<div class="alertModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${crawlError}
				</div>
			</div>
			</#if>
			<#if Deleted?exists>
			<div class="infoModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${Deleted}
				</div>
			</div>		
			</#if>
			<#if Updated?exists>
			<div class="infoModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${Updated}
				</div>
			</div>		
			</#if>
			<!-- help contents -->
			<hr style="margin-top: 10px;">
			<div class="mblg">
				<div class="mtxs">
					<a id="tcId100" class="tipLink rel" name="tcId100">Help</a>
						<b>
						<#if userManagementForm.setupTabNumber == 1>
							<@spring.message "mint.help.usersetup"/>
						<#elseif userManagementForm.setupTabNumber == 2>
							<@spring.message "mint.help.groupsetup"/>
						<#elseif userManagementForm.setupTabNumber == 3>
							<@spring.message "mint.help.accesss"/>
						<#elseif userManagementForm.setupTabNumber == 4>
							<@spring.message "mint.help.addmenu"/>
						<#elseif userManagementForm.setupTabNumber == 5>
							<@spring.message "mint.help.addfunctionality"/>
						<#else>
							<@spring.message "mint.help.usersetup"/>
				 		</#if>
				 		</b>
					<a href="<@spring.message 'mint.help.mailTo'/>" target="_top"> <@spring.message "mint.help.mailId"/></a>
				</div>
			</div>
			<hr style="margin-top: 0px;">
			<div class="tab-box"> 
			    <a href="javascript:;" class="tabLink activeLink" id="cont-1" onClick="loadTabData(1)">User Setup</a>
			    <a href="javascript:;" class="tabLink" id="cont-2" onClick="loadTabData(2)">Assign App/Env</a>
    			<a href="javascript:;" class="tabLink " id="cont-3"  onClick="loadTabData(3);">Menu Access Setup</a>
    			<a href="javascript:;" class="tabLink " id="cont-4"  onClick="loadTabData(4);">Add New Menu</a>
    			<a href="javascript:;" class="tabLink " id="cont-5"  onClick="loadTabData(5);">Add Functionality</a> 
			</div>
			<div class="tabcontent paddingAll" id="cont-2-1" />
				<#if userManagementForm.setupTabNumber == 1>
					<#include "UserSetup.ftl">
				<#elseif userManagementForm.setupTabNumber == 2>
					<#include "GroupSetup.ftl">
				<#elseif userManagementForm.setupTabNumber == 3>
					<#include "MenuSetup.ftl">
				<#elseif userManagementForm.setupTabNumber == 4>
					<#include "AddNewMenu.ftl">
				<#elseif userManagementForm.setupTabNumber == 5>
				 	 <#include "AddNewFunctionality.ftl">
				 <#else>
				 	<#include "UserSetup.ftl">
				 </#if>
			 </div>
</div>
</div>
</body>
</html>
