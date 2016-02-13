<#import "spring.ftl" as spring />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title>MINT Property Configuration Page</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=1800">	
	<meta name="description">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/css/bootstrap-combined.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" media="screen" href="http://tarruda.github.com/bootstrap-datetimepicker/assets/css/bootstrap-datetimepicker.min.css">
	<link rel='stylesheet' type='text/css' href='css/global_web.css'/>
	<link rel='stylesheet' type='text/css' href='css/mint.css'/>
	<script type="text/javascript" src="js/modernizr.js"></script>
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/jquery-ui.js"></script>
	<script type="text/javascript" src="js/menu.js"></script>
	<script src="js/jquery-1.9.1.js"></script>	
</head>
<body data-errorcheck="on" id="dashboard" data-behavior="" data-demo="body" class="">
<script type="text/javascript">
function validateAndSave() {
	var propertyName = [];	
	var propertyDisplayName = [];
	var propertyValue = [];
	$('.mintProperty').each(function () {
		propertyName.push($(this).attr('id'));
		propertyDisplayName.push($(this).attr('name'));
		propertyValue.push($(this).val());
	});
	$("#propertyName").val(propertyName);
	$("#propertyDisplayName").val(propertyDisplayName);
	$("#propertyValue").val(propertyValue);
	document.getElementById("configPropertyId").setAttribute('action',"<@spring.url '/SaveConfig.ftl' />");
	document.getElementById("configPropertyId").submit();
}
</script>
<div id="container" style="width:80%">
	<header data-demo="header">
		<div class="headercontent" style="width:100%">
			<table width="100%">
				<tr>
					<td  width="15%"> 
						<div class="logo">
							<a data-demo="logoHref" href="#" title="">
			        		<img data-demo="logo" src="images/logo_tiaa.png" alt="MINT">
		      				</a> 
    					</div>  
    				</td>
    				<td  align="right" width="10%">
	    				<div data-demo="headertitle" class="headertitle" style="font-weight: bolder;">
							MinT
	   					</div>
   					</td>
   					<td align="right" width="40%">
					</td>
   					<td  align="left" width="34%">
					</td>
				</tr>
			</table>
		</div>
	</header>
	<div role="application" style="width:100%">
		<nav>
			<div class="navcontent" role="navigation" >
				<ul class="l1nav drop3" aria-label="Primary Navigation" role="menubar" tabindex="-1" style="height: 45px;width:100%" >
    			</ul>
			</div>
		</nav>
	</div>
</div>	
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
<section class="drop mtlg">
	<div class="cm">
	<table cellpadding='0' cellspacing='0' width="100%;">
		<tr>
			<td class="hd">
				<h3>Mint Properties Configuration</h3>
			</td>
		</tr>
		<tr class="bd">
			<td>
				<form modelAttribute="ConfigProperty"
					action='SaveConfig.ftl' id="configPropertyId" name="configPropertyId"
					target="_top" method="POST">
					<table class="bd rowheight35">
					<input type="hidden" id="propertyName" name="propertyName" value="" /> 
					<input type="hidden" id="propertyDisplayName" name="propertyDisplayName" value="">
					<input type="hidden" id="propertyValue" name="propertyValue" value="">
					  <br/>
						<#list mintPropertiesList! as mintProperty> 
						<tr class="lblFieldPair">
							<td class="lbl">${mintProperty.propertyDisplayName!}</td>
							<td class="input">
								<input class="mintProperty required" id="${mintProperty.propertyName!}" type="text" name="${mintProperty.propertyDisplayName!}" maxlength="100" value="${mintProperty.propertyValue!}">
							</td>
						</tr>
					   </#list>
					</table>
				</form>
					<center><font color="red" size=-1>&nbsp;* mandatory</font></center>
				</td>
				</tr>
				<tr class="bd">
					<td class="btnBar">
						<a href="#" onClick="validateAndSave()" class="btn"><span>Save</span></a>
					</td>
				</tr>
	</table>
	</div>
</section>
</div>
</div>
</body>
</html>
</div>
<div>