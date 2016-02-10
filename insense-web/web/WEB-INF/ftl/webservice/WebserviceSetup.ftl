<#import "../common/spring.ftl" as spring />
<#import "../common/MacroTemplates.ftl" as mint />
<div id="Webservice Testing Setup">
   <#include "../common/Header.ftl">
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
    var currentMenu = ${webserviceSetupForm.setupTabNumber};
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
    		$("#cont-${webserviceSetupForm.setupTabNumber}").addClass("activeLink");
     });  	
 function loadTabData(setupTabNumberVal) {
	
    		var form = $(document.createElement('form'));
    		$('body').append(form);
    		$(form).attr("name", "WebserviceSetup");
    		$(form).attr("action", "<@spring.url '/WebserviceSetup.ftl' />");
    		$(form).attr("method", "POST");
    		var input1 = $("<input>").attr("type", "hidden").attr("name", "setupTabNumber").val(setupTabNumberVal);
    		var input2 = $("<input>").attr("type", "hidden").attr("name", "applicationId").val("${webserviceSetupForm.applicationId!}");
    		var input3 = $("<input>").attr("type", "hidden").attr("name", "environmentId").val("${webserviceSetupForm.environmentId!}");
    		var input4 = $("<input>").attr("type", "hidden").attr("name", "environmentCategoryId").val("${webserviceSetupForm.environmentCategoryId!}");
    		$(form).append($(input1));
    		$(form).append($(input2));
    		$(form).append($(input3));
    		$(form).append($(input4));
  		
    		$(form).submit();
}

 function getWSTestSuites(){
		$('#wsSuiteId').html('');
		$('<option />', {value: "-1", text: "Select"}).appendTo("#wsSuiteId");
		var environmentId = document.getElementById("environmentId").value;
		enableLoader();
		var request = $.ajax({  url: 	
		"GetWSTestSuites.ftl",  
			type: "POST",  
			cache: false,
			data: { environmentId: environmentId},  
			dataType: "json"}); 
				request.done(function( msg ) {
					var Outmsg= JSON.stringify(msg);
					
					 $.each(Outmsg,function(key, val) {
						  $('<option />', {value: key, text: val}).appendTo("#wsSuiteId");
			    		});
					// $( "#wsSuiteName" ).html( msg );
					 if('${webserviceSetupForm.wsSuiteId!}' > 0){
						 $('#wsSuiteId').val('${webserviceSetupForm.wsSuiteId!}');
					}
	 		 }); 
	  	request.fail(
	 	 function( jqXHR, textStatus ){  
	 		disableLoader();
	  		}); 
	  	disableLoader();
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
						<#if webserviceSetupForm.setupTabNumber == 1>
							<@spring.message "mint.help.webservice"/>
						<#elseif webserviceSetupForm.setupTabNumber == 2>
							<@spring.message "mint.help.saveparameter"/>
						<#elseif webserviceSetupForm.setupTabNumber == 3>
							<@spring.message "mint.help.createParamter"/>
						<#elseif webserviceSetupForm.setupTabNumber == 4>
							<@spring.message "mint.help.reloadWebservices"/>
						<#elseif webserviceSetupForm.setupTabNumber == 5>
							<@spring.message "mint.help.esbPingTest"/>
						<#elseif webserviceSetupForm.setupTabNumber == 6>
							<@spring.message "mint.help.esbPingResults"/>
						<#elseif webserviceSetupForm.setupTabNumber == 7>
							<@spring.message "mint.help.webservicesReports"/>
						<#elseif webserviceSetupForm.setupTabNumber == 8>
							<@spring.message "mint.help.scheduleWebservice"/>							
						<#else>
							<@spring.message "mint.help.webservice"/>
				 		</#if>
				 		</b>
					<a href="<@spring.message 'mint.help.mailTo'/>" target="_top"> <@spring.message "mint.help.mailId"/></a>
				</div>
			</div>	
			<hr style="margin-top: 0px;">
			<div class="tab-box">
				<span>
			    <a href="javascript:;" class="tabLink activeLink" id="cont-1" onClick="loadTabData(1)">Webservice Setup</a>
			    </span>
			   <!--   <span>
			    <a href="javascript:;" class="tabLink" id="cont-2" onClick="loadTabData(2)">Test/Save Request</a>
			    </span>
			    <span>
			    <a href="javascript:;" class="tabLink" id="cont-3" onClick="loadTabData(3)">Create Testsuite</a>
			    </span> -->
			    <span>
			    <a href="javascript:;" class="tabLink" id="cont-2" onClick="loadTabData(2)">Reload Webservices</a>
			    </span>
			   <!--  <span>
			    <a href="javascript:;" class="tabLink" id="cont-2" onClick="loadTabData(5)">ESB Ping Test</a>
			    </span>
			    <span>
			    <a href="javascript:;" class="tabLink" id="cont-2" onClick="loadTabData(6)">ESB Ping Results</a>
			    </span> -->
			    <!--  <span>
			    	<a href="javascript:;" class="tabLink" id="cont-8" onClick="loadTabData(8)">Schedule Webservice</a>
			    </span>
			    <span>
			    <a href="javascript:;" class="tabLink" id="cont-7" onClick="loadTabData(7)">Webservice Reports</a>
			    </span> -->

			</div>
			<div class="tabcontent paddingAll" id="cont-2-1" />
				<#if webserviceSetupForm.setupTabNumber == 1>
					<#include "ConfigureWebservice.ftl">
			
				 <#else>
				 	<#include "ReloadWebservices.ftl">
				</#if>
				
			 </div>
</div>
</div>
</body>
</html>
