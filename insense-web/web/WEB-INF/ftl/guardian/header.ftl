<#import "../common/spring.ftl" as spring />
<#import "../common/MacroTemplates.ftl" as mint />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title>Guardian</title>
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
							Guardian
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