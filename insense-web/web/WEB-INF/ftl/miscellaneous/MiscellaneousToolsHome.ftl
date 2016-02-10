<#import "../common/spring.ftl" as spring />
<#import "../common/MacroTemplates.ftl" as mint />
<div id="Ui Testing Setup">
   <#include "../common/Header.ftl">
</div>
<head>	
<style type="text/css">
  .tab-box {
  	margin-top: 1.5%;
  }
</style>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/formValidation.js"></script>		
<script type="text/javascript">
function readCookie(name){
	return(document.cookie.match('(^|; )'+name+'=([^;]*)')||0)[2]
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
			<!-- help contents -->
			<hr style="margin-top: 10px;">
			<div class="mblg">
				<div class="mtxs">
					<a id="tcId100" class="tipLink rel" name="tcId100">Help</a><b><@spring.message
						"mint.help.mischellaneoustools"/></b><a href="<@spring.message 'mint.help.mailTo'/>"
						target="_top"> <@spring.message "mint.help.mailId"/></a>
				</div>
			</div>
			<hr style="margin-top: 0px;">
			<form method="POST" name="MiscellaneousTool" id="MiscellaneousTool">
				<section class="drop mtlg">
					<div class="cm">
						<table cellpadding='0' cellspacing='0' width="100%;">
							<tr>
								<td class="hd">
									<h3>Miscellaneous Tools</h3>
								</td>
							</tr>
							<tr class="bd">
								<td style="padding: 5"><input type="hidden" id="submitDone"
									name="submitDone" />
									<center>
										<div>
											<table class="Datatable mt"
												style="width: 90%; margin-top: 50px;">
												<tr class="hd">
													<td class="txtl"><h6>Application</h6></td>
													<td class="txtl"><h6>Application Description</h6></td>
												</tr>
												<#list toolsList! as tools> 
													<#if tools.hasAccess>
													<tr>
														<td class="txtl"><a href="${tools.toolActionUrl!}">${tools.toolName!}</a></td>
														<td class="txtl">${tools.toolDescription!}</td>
													</tr>
													<#else>
													<tr>
														<td class="txtl"><font face='verdana,arial' size='-1' color='red'>${tools.toolName!} (Don't have access)</font></a></td>
														<td class="txtl">${tools.toolDescription!}</td>
													</tr>
													</#if>
												</#list>
											</table>
										</div>
									</center></td>
							</tr>
							<tr class="bd rowheight35"><td>&nbsp;</td></tr>
						</table>
					</div>
				</section>
			</form>
		</div>
</div>
<div id="footer">
<#include "../common/Footer.ftl">
</div>
</body>
</html>
