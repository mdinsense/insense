<#import "../common/spring.ftl" as spring />
<#import "../common/MacroTemplates.ftl" as mint />
<div id="FeedBack Form">
   <#include "../common/Header.ftl">
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
    <script type="text/javascript">  
 $(document).ready(function() {  
 
  });
  
 function validateAndsave(){
	 if( $('#menuId').val() == -1) {
			alert('Please Select Menu Name');
			return false;
		}
		
		document.getElementById("feedBackId").setAttribute('action',"<@spring.url '/SaveFeedBack.ftl' />");
		document.getElementById("feedBackId").submit();
 }
 
</script>
</head>
<body onLoad="window.scrollTo(0,readCookie('ypos'))">
<div style="width:80%;margin-top:18px;" class="content twoCol">
<div class="page-content" id="pagecontent" role="main" tabindex="-1">
			<#if success?exists>
			<div class="infoModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${success}
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
<!--  table view one for application and category -->	

        <form modelAttribute="feedBackForm"  action='SaveFeedBack.ftl' id="feedBackId" name="aform" target="_top" method="POST">
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>FeedBack Form</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<table class="bd rowheight35">
									<br />
									<tr class="lblFieldPair">
										<td class="lbl">Choose Menu Name</td>
										<td class="input">
											<select id="menuId" name="menuId"
												onChange="">
												<option value="0">----------------Select--------------</option>
												<#list Session.menuList! as menu>
													<#if menu.menu.menuId != -1>
														<option value="${menu.menu.menuId!}">${menu.menu.menuName!}</option>
													</#if>	
												</#list>
											</select>
										</td>
									</tr>
									<tr class="lblFieldPair">
										<td class="lbl">Comments</td>
										<td class="input">
											<textarea name="comments" id ="comments" rows="6" cols="56" maxlength="1000" class=""  style="width: 400px;"></textarea>
										</td>
									</tr>

								</table>
						</td>
					</tr>
					<tr class="bd">
					<td class="btnBar">
						<a href="#" onClick="validateAndsave()" class="btn"><span>Submit</span></a>
						<a href="#" onClick="" class="btn"><span>Cancel</span></a>
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
