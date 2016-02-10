<#import "../common/spring.ftl" as spring />
<#import "../common/MacroTemplates.ftl" as mint />
<div id="Ui Testing Setup">
   <#include "../common/Header.ftl">
</div>
<style type="text/css">
  .tab-box {
  	margin-top: 1.5%;
  }
</style>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
function submitSuitType(){
	
		document.getElementById("suitTypeForm").setAttribute('action',"<@spring.url '/ChooseSuitTypeSubmit.ftl' />");
		document.getElementById("suitTypeForm").setAttribute('method',"POST");
		document.getElementById("suitTypeForm").submit();	
}
</script>
</head>
<body>

<div style="width:80%;margin-top:18px;" class="content twoCol">
<hr>
		<div class="mblg">
			<div class="mtxs">
		 		<a id="tcId100" class="tipLink rel" name="tcId100" >Help</a><b>Choose suit type to create suit</b><a href="<@spring.message 'mint.help.mailTo'/>" target="_top">
				<@spring.message "mint.help.mailId"/></a>
			</div>
		</div>
<hr style="margin-top: 0px;">
<div class="page-content" id="pagecontent" role="main" tabindex="-1">
			
<!--  table view one for application and category -->	

        <form modelAttribute="testingForm"  action='ChooseSuitTypeSubmit.ftl' id="suitTypeForm" name="aform" target="_top" method="POST">
        <#if testingForm?exists && testingForm.suitId?exists>
			<input type="hidden" id="suitId" name="suitId" value="${testingForm.suitId!}">
		<#else>
			<input type="hidden" id="suitId" name="suitId" value="">
		</#if>
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Choose Suit Type</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:5">
								<input type="hidden" id="setupTabNumber" name="setupTabNumber" value="1">
								<table class="bd rowheight35">
									<br />
									<tr class="lblFieldPair">
										<td class="lbl">Suit Types</td>
										<td class="input">
											<select id="suitType" name="suitType">
												<#list suitTypes! as suitType> 
														<option value="${suitType.menuId!}">${suitType.menuName!}</option>
												</#list>
											</select>
											&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" style="vertical-align: top;" onclick="submitSuitType()" value="Submit"/>
										</tr>
									</tr>
									

								</table>
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
