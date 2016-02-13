<#import "../common/spring.ftl" as spring />
<#import "../common/MacroTemplates.ftl" as mint />
<div id="Ui Testing Setup">
   <#include "../common/header.ftl">
</div>
<style type="text/css">
  .tab-box {
  	margin-top: 1.5%;
  }
</style>
<script type="text/javascript" src="js/script.js"></script>
<head>	
    <script type="text/javascript" src="js/formValidation.js"></script>		
    <script type="text/javascript">
    var currentMenu = ${uiTestingSetupForm.setupTabNumber};
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
    		$("#cont-${uiTestingSetupForm.setupTabNumber}").addClass("activeLink");
     });  	
 function loadTabData(setupTabNumberVal) {
    		var form = $(document.createElement('form'));
    		$('body').append(form);
    		$(form).attr("name", "UiTestingSetup");
    		$(form).attr("action", "<@spring.url '/UiTestingSetup.ftl' />");
    		$(form).attr("method", "POST");
    		var input1 = $("<input>").attr("type", "hidden").attr("name", "setupTabNumber").val(setupTabNumberVal);
    		var input2 = $("<input>").attr("type", "hidden").attr("name", "applicationId").val("${uiTestingSetupForm.applicationId!}");
    		var input3 = $("<input>").attr("type", "hidden").attr("name", "environmentId").val("${uiTestingSetupForm.environmentId!}");
    		var input4 = $("<input>").attr("type", "hidden").attr("name", "environmentCategoryId").val("${uiTestingSetupForm.environmentCategoryId!}");
    		$(form).append($(input1));
    		$(form).append($(input2));
    		$(form).append($(input3));
    		$(form).append($(input4));
  		
    		$(form).submit();
}
function loadEnvironmentCategoryDropdown(applicationId,environment){
	
	 var dataToSend = {
			 'applicationId' : applicationId
		      }; 
	 
	var request = $.ajax({  
		url: "GetEnvironmentCategoryDropdown.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 

	request.done(function( msg ) {	
		var Outmsg= JSON.stringify(msg);
		Outmsg = JSON.parse(Outmsg);
		 if(!document.getElementById(environment).hasAttribute("multiple")) {
	  	 	 $('<option />', {value: "-1", text: "----------Select-------------"}).appendTo("#"+environment);
			}
        $.each(Outmsg,function(key, val) {
            $('<option />', {value: key, text: val}).appendTo("#"+environment);
       });
	});
	
	request.fail(function( jqXHR, textStatus ){  
	}); 
} 
function loadEnvironmentCategoryDropdownAppendALL(applicationId,environment){
	
	 var dataToSend = {
			 'applicationId' : applicationId
		      }; 
	 
	var request = $.ajax({  
		url: "GetEnvironmentCategoryDropdown.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 

	request.done(function( msg ) {	
		var Outmsg= JSON.stringify(msg);
		Outmsg = JSON.parse(Outmsg);
		 if(!document.getElementById(environment).hasAttribute("multiple")) {
	  	 	 $('<option />', {value: "-1", text: "----------Select-------------"}).appendTo("#"+environment);
	  	 	$('<option />', {value: "1", text: "ALL"}).appendTo("#"+environment);
			}
       $.each(Outmsg,function(key, val) {
           $('<option />', {value: key, text: val}).appendTo("#"+environment);
      });
	});
	
	request.fail(function( jqXHR, textStatus ){  
	}); 
} 
function getEnvironmentCategory(environmentId,target){
	var request = $.ajax({  
		url: "GetEnvironmentCategory.ftl",
		type: "POST", 
		cache: false,
		data: { environmentId : environmentId },
		dataType: "json"
	}); 
	 request.done(function( msg ) {    
         var Outmsg= JSON.stringify(msg);
         Outmsg = JSON.parse(Outmsg);
            $.each(Outmsg,function(key, val) {
                $('#'+target+'> option').each(function() {
                       if(($(this).val() == -1)) {
                       $(this).attr("selected","true");
                    }
                    if(($(this).val() == key)) {
                       $(this).attr("selected","true");
                    }
                });
            });
         });
	request.fail(function( jqXHR, textStatus ){  
	}); 
}
function loadEnvironmentDropdown(applicationId, environmentCategoryId,environmentId){

	 var dataToSend = {
			 'applicationId' : applicationId, 
			 'environmentCategoryId' : environmentCategoryId
		      };  

	 var request =$.ajax({
		        url: "GetEnvironmentDropdown.ftl",
		        type: "POST", 
		        cache: false,
		        data: dataToSend,
		        dataType: "json"
		        
		      });   
	 request.done(function( msg ) {    
	       var Outmsg= JSON.stringify(msg);
	       Outmsg = JSON.parse(Outmsg);
	       $.each(Outmsg,function(key, val) {
	           $('<option />', {value: key, text: val}).appendTo("#"+environmentId);
	   	});
	});
		
	request.fail(function( jqXHR, textStatus ){  
	});

} 

function loadSecureEnvironmentDropdown(environmentCategoryId,applicationId,environmentId){
	var dataToSend = {
			 'applicationId' : applicationId, 
			 'environmentCategoryId' : environmentCategoryId
		      };
	
	var request = $.ajax({  
		url: "GetSecureEnvironmentDropdown.ftl",
		type: "POST", 
		cache: false,
		data: dataToSend,
		dataType: "json"
	}); 
	
	request.done(function( msg ) {    
	       var Outmsg= JSON.stringify(msg);
	       Outmsg = JSON.parse(Outmsg);
	     
	       $.each(Outmsg,function(key, val) {
	           $('<option />', {value: key, text: val}).appendTo("#"+environmentId);
	   	});
	});
	request.fail(function( jqXHR, textStatus ){  
	});
} 
</script>
</head>
<body>
<div style="width:80%;margin-top:18px;" class="content twoCol">
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
						<#if uiTestingSetupForm.setupTabNumber == 1>
							<@spring.message "mint.help.application"/>
						<#elseif uiTestingSetupForm.setupTabNumber == 2>
							<@spring.message "mint.help.environment"/>
						<#elseif uiTestingSetupForm.setupTabNumber == 3>
							<@spring.message "mint.help.includeexculeurl"/>
						<#elseif uiTestingSetupForm.setupTabNumber == 4>
							<@spring.message "mint.help.loginuserdetails"/>
						<#elseif uiTestingSetupForm.setupTabNumber == 5>
							<@spring.message "mint.help.applicationConfig"/>
						<#elseif uiTestingSetupForm.setupTabNumber == 6>
							<@spring.message "mint.help.htmlreportsconfig"/>
						<#elseif uiTestingSetupForm.setupTabNumber == 7>
							<@spring.message "mint.help.modulesetup"/>
						<#elseif uiTestingSetupForm.setupTabNumber == 8>
							<@spring.message "mint.help.analyticsexcludesetup"/>							
						<#else>
							<@spring.message "mint.help.application"/>
				 		</#if>
				 		</b>
					<a href="<@spring.message 'mint.help.mailTo'/>" target="_top"> <@spring.message "mint.help.mailId"/></a>
				</div>
			</div>
			<hr style="margin-top: 0px;">
			<div class="tab-box">
				<span>
			    <a href="javascript:;" class="tabLink activeLink" id="cont-1" onClick="loadTabData(1)">Application</a>
			    </span>
			    <span>
			    <a href="javascript:;" class="tabLink" id="cont-2" onClick="loadTabData(2)">Environment</a>
			    </span>
			    <span>
    			<a href="javascript:;" class="tabLink " id="cont-3"  onClick="loadTabData(3);">Include/Exclude Url</a>
    			</span>
    			<span>
    			<a href="javascript:;" class="tabLink " id="cont-4"  onClick="loadTabData(4);">Login User</a>
    			</span>
    			<span>
    			<a href="javascript:;" class="tabLink " id="cont-5"  onClick="loadTabData(5);">Testing Config</a>
    			</span>
    			<span>
    			<a href="javascript:;" class="tabLink " id="cont-6"  onClick="loadTabData(6);">Partial Compare Config</a>
    			</span>
    			<span>
    			<a href="javascript:;" class="tabLink " id="cont-7"  onClick="loadTabData(7);">Module</a>
    			</span>
    			<span>
    			<a href="javascript:;" class="tabLink " id="cont-8"  onClick="loadTabData(8);">Analytics Exclude Link</a>
    			</span>
			</div>
			<div class="tabcontent paddingAll" id="cont-2-1" />
				<#if uiTestingSetupForm.setupTabNumber == 1>
					<#include "UiApplicationSetup.ftl">
				<#elseif uiTestingSetupForm.setupTabNumber == 2>
					<#include "UiEnvironmentSetup.ftl">
				<#elseif uiTestingSetupForm.setupTabNumber == 3>
					<#include "UiIncludeExcludeUrl.ftl">
				<#elseif uiTestingSetupForm.setupTabNumber == 4>
					<#include "UiLoginUserSetup.ftl">
				<#elseif uiTestingSetupForm.setupTabNumber == 5>
				 	 <#include "UiApplicationConfigSetup.ftl">
				<#elseif uiTestingSetupForm.setupTabNumber == 6>
				 	<#include "UiHtmlReportsConfigSetup.ftl">
				<#elseif uiTestingSetupForm.setupTabNumber == 7>
				 	<#include "UiModuleSetup.ftl">
				<#elseif uiTestingSetupForm.setupTabNumber == 8>
				 	<#include "UiAnalyticsExcludeSetup.ftl">
				<#else>
				 	<#include "UiApplicationSetup.ftl">
				 </#if>
			 </div>
</div>
</div>
</body>
</html>
