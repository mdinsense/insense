<#import "spring.ftl" as spring /> 
<#import "MacroTemplates.ftl" as mint/>
<div id="Mint Home"><#include "header.ftl"></div>
<head>
<link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css" />
<script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
<style>
.lblFieldPair .lbl {
 padding-right: 3px;
 width: auto;
}
@-moz-document url-prefix() { 
  .lblFieldPair .lbl {
     float:none;
  }
}
.lblFieldPair .input {
	display: table-cell;
}
.imgicon:HOVER {
	border: 1px solid #5ec2eb;
}
</style>
	<script type="text/javascript">

	$(document).ready(function() {
		
		 $(".tabLink").each(function(){
		      $(this).click(function(){
		        tabeId = $(this).attr('id');
		        $(".tabLink").removeClass("activeLink");
		        $(this).addClass("activeLink"); 
		        return false;
		      });
		});
		$(".tabLink").removeClass("activeLink");
		$("#cont-${reportForm.tab}").addClass("activeLink");
	});
	
	function loadTabData(setupTabNumberVal) {
		//$("#tab").val(setupTabNumberVal);
		var form = $(document.createElement('form'));
		$('body').append(form);
		$(form).attr("name", "UsageReport");
		$(form).attr("action", "<@spring.url '/UsageReports.ftl' />");
		$(form).attr("method", "POST");
		var input1 = $("<input>").attr("type", "hidden").attr("name", "tab").val(setupTabNumberVal);
		
		$(form).append($(input1));
  		
		$(form).submit();
		/* 
		document.getElementById("usageReportsFormId").setAttribute('action',"<@spring.url '/UsageReports.ftl' />");
		document.getElementById("usageReportsFormId").submit(); */

	}
	
	 function clearData() {
		   document.getElementById("usageReportsFormId").reset();
	   }
</script>
</head>
<body>
	<div id=commonDv">
		<div class="content twoCol" style="width: 80%;">
			<div class="page-content" id="pagecontent" role="main" tabindex="-1">
				<#if Success?exists>
				<div class="infoModule visible" id="tcId1" style="margin-top: 1%;">
					<div class="messageBodyContent">
						<span class="icon" title="Information"></span>${Success}
					</div>
				</div>
				</#if> 
				<#if error?exists>
				<div class="alertModule visible" id="tcId1" style="margin-top: 1%;">
					<div class="messageBodyContent">
						<span class="icon" title="Information"></span>${error}
					</div>
				</div>
				</#if>
				<hr style="margin-top: 10px;">
				<div class="mblg">
					<div class="mtxs">
						<a id="tcId100" class="tipLink rel" name="tcId100">Help</a><b><@spring.message
							"mint.help.usagereports"/></b><a
							href="<@spring.message 'mint.help.mailTo'/>" target="_top">
							<@spring.message "mint.help.mailId"/></a>
					</div>
				</div>
				<hr style="margin-top: 0px;">
				
				<div class="tab-box">
				<span>
			    	<a href="javascript:;" class="tabLink activeLink" id="cont-1" onClick="loadTabData(1)">Report Chart</a>
			    </span>
			    	<span>
			    	<a href="javascript:;" class="tabLink" id="cont-2" onClick="loadTabData(2)">Detailed Report</a>
			    </span>
			    	<div id="downloadIcon" style="float:right;display:none;" class="drop2">Download Report:&nbsp;<img src="/mint/images/xlsdownload.jpg" class="imgicon" onClick="downloadReport()"/></div>
				</div>
				<div class="tabcontent paddingAll" id="cont-2-1" >
				      <#if reportForm.tab! ==1>
						<#include "UsageReportChart.ftl">
					  <#elseif reportForm.tab! ==2>
						<#include "UsageDetailedReport.ftl">
					  <#else>
					  	<#include "UsageReportChart.ftl">
					  </#if>	
				 </div>
				<section>
					<br>
				</section>
				
			</div>
		</div>
	</div>