<div id="Guardian Reports">
   <#include "header.ftl">
</div>
<style type="text/css">
  .tab-box {
  	margin-top: 1.5%;
  }
</style>
<script type="text/javascript" src="js/script.js"></script>
<script language="JavaScript">
function readCookie(name){
	return(document.cookie.match('(^|; )'+name+'=([^;]*)')||0)[2]
}
</script>
<head>	
<script type="text/javascript" src="js/formValidation.js"></script>		
 <script type="text/javascript">
    var currentMenu = ${guardianForm.tabNumber};
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
    		$("#cont-${guardianForm.tabNumber}").addClass("activeLink");
     });  	
 function loadTabData(setupTabNumberVal) {
    		var form = $(document.createElement('form'));
    		$('body').append(form);
    		$(form).attr("name", "GuardianForm");
    		$(form).attr("action", "<@spring.url '/GuardianHome.ftl' />");
    		$(form).attr("method", "POST");
    		var input1 = $("<input>").attr("type", "hidden").attr("name", "tabNumber").val(setupTabNumberVal);
    		$(form).append($(input1));
    		$(form).submit();
}
$(window).scroll(function () {
    document.cookie='ypos=' + $(window).scrollTop();
});
</script>
</head>
<body onLoad="window.scrollTo(0,readCookie('ypos'))">
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
			
			<hr style="margin-top: 0px;">
			<div class="tab-box">
				<span>
			    <a href="javascript:;" class="tabLink activeLink" id="cont-1" onClick="loadTabData(1)">Add Signature</a>
			    </span>
			    <span>
			    <a href="javascript:;" class="tabLink" id="cont-2" onClick="loadTabData(2)">Signature Monitor</a>
			    </span>
			    <span>
    			<a href="javascript:;" class="tabLink " id="cont-3"  onClick="loadTabData(3);">View Report</a>
    			</span>
			</div>
			<div class="tabcontent paddingAll" id="cont-2-1" />
				<#if guardianForm.tabNumber == 1>
					<#include "AddSignature.ftl">
				<#elseif guardianForm.tabNumber == 2>
					<#include "MonitorSignature.ftl">
				<#elseif guardianForm.tabNumber == 3>
					<#include "ViewReport.ftl">
				<#else>
				 	<#include "AddSignature.ftl">
				 </#if>
			 </div>
</div>
</div>
<div id="footer">
<#include "../common/Footer.ftl">
</div>
</body>
</html>
