<#import "spring.ftl" as spring />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title>MINT</title>
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
	<script type="text/javascript" src="js/script.js"></script>
	<script type="text/javascript">  
	String.prototype.contains = function(it) { return this.indexOf(it) != -1; };
	$(document).ready(function() {
		disableLoader();
		$(".requiredSelect").each(function(){
			 $(this).after("<font face='verdana,arial' size='-1' color='red'>*</font>");
		});
	});
	function submitSelecterUser()
	{
	 	var aliasName = document.getElementById("aliases").value;
		document.headerForm.aliasField.value = aliasName;
		document.headerForm.action='Home.ftl';
		document.headerForm.method = 'POST';
		document.headerForm.submit();
		
	}
	function addLoaderToDiv(target) {
		var loader 	= 		"<div id='ajaxSpinnerContainer'>";
		loader 		+= 		"<div><a data-demo='logoHref' href='' title=''>";
		loader 		+= 		"<img data-demo='logo' src='images/progress_bar.gif'></a></div></div>";
		$('#'+target).empty();
 		$('#'+target).html(loader);
	}
	function unselectUser() {
	
		document.headerForm.aliasField.value = null;
		document.headerForm.unsetAliasField.value= 'true';
		document.headerForm.action='Home.ftl';
		document.headerForm.method = 'POST';
		document.headerForm.submit();
	}
	function openLogoUrl() {
		var url = "<@spring.message 'mint.home.logo.url'/>";
		window.location.assign("http://"+url);
	}
	function readCookie(name){
		return(document.cookie.match('(^|; )'+name+'=([^;]*)')||0)[2]
	}
	$(window).scroll(function () {
	    document.cookie='ypos=' + $(window).scrollTop();
	});
	function logout(){
		window.open('', '_self', ''); 
		window.close();
	}
	</script>
</head>
<body data-errorcheck="on" id="dashboard" data-behavior="" data-demo="body" onLoad="window.scrollTo(0,readCookie('ypos'))">
<form name="headerForm">
	<input type="hidden" name="aliasField">
	<input type="hidden" name="unsetAliasField">
</form>

<div id="container" class="mainbody" style="width:80%">
	<header data-demo="header">
		<div class="headercontent" style="width:100%">
			<table width="100%">
				<tr>
					<td  width="15%"> 
						<div class="logo">
							<a data-demo="logoHref" href="#" onClick="openLogoUrl()" title="">
			        			<img data-demo="logo" src="images/logo_tiaa.png" alt="MINT">
		      				</a> 
    					</div>  
    				</td>
    				<td  align="right" width="10%">
	    				<div data-demo="headertitle" class="headertitle" style="font-weight: bolder;">
							MinT
	   					</div>
   					</td>
					<#if Session.userGroupName?? && Session.userGroupName?lower_case == "admin">
   					<td  align="left" width="34%">
					<#if !Session.aliasName?? || Session.aliasName == "null">
						<div class="flr mtxlg">
							<select id="aliases" name="aliases" style="width: 120px;">
							<#list Session.mintUsers as user> 
								<option value="${user.userName!}">${user.userName!}&nbsp;(${user.groupName!})</option>
							</#list>
							</select>
							<a class="btn btnSmall" style="vertical-align: top;" href="#" onclick="submitSelecterUser()"><span>Set (as Alias)</span></a> 
						</div>
					<#else>
					<div class="flr mtxlg">
						<span></span>
						<span style="width: 160px;height:17px;">
							<font color="blue" ><b>Alias: </b>${Session.aliasName}</font>
						</span>
							<a class="btn btnSmall" style="vertical-align: top;" href="#" onclick="unselectUser()"><span>Unset(Alias)</span></a> 
					</div>
					</#if>
			
					</td>
					<#else>
					<td>&nbsp;&nbsp;</td>
					</#if>
				</tr>
			</table>
			<div style="float:right;margin-top:10px;margin-right:15px;font-weight: bolder;border-left: 1px dotted gray">
				&nbsp;&nbsp;&nbsp;
				<span id='logout' style="  margin:auto;  width: 20%"><a href='#' onClick='logout()'>
					<font color="#0558b1" style="width: 160px;font-weight:bold;">
					Sign Out
					</font>
					</a>
				</span>
			</div>
			<div style="float:right;margin-top:10px;margin-right: 20px;font-weight: bolder;border-left: 1px dotted gray">
				&nbsp;&nbsp;&nbsp;
				<span style="margin:auto;width:20%">
		       		<font color="#0558b1" style="width: 160px;font-weight:bold;">
						Welcome 
							${Session.loginUserName!}&nbsp;(${Session.userGroupName!})
					</font>
				</span>
			</div>
			<div style="float:right;margin-top:10px;margin-right: 20px;font-weight: bolder;">
				<a href='FeedBack.ftl'>
					<font color="#0558b1" style="width: 160px;font-weight:bold;">
						<span>Feedback</span>
					</font>
				</a>
			</div>
		</div>
	</header>
	<div role="application" style="width:100%">
		<nav>
			<div class="navcontent" role="navigation" >
				<span class="screenReader">Use arrow keys to access sub-menus and sub-menu links, this may require a mode change</span>
				<ul class="l1nav drop3" aria-label="Primary Navigation" role="menubar" tabindex="-1" style="height: 45px;width:100%" >

 				<#list Session.menuList as menu>
 	 			<li style="width:10%">
 	 				<a href='${menu.menu.menuAction}'> ${menu.menu.menuName}
 	 				
 	 				<#if menu.childMenus?? && (menu.childMenus?size > 0) >
 	 				<span class="nav-icon"></span>
 	 				</#if>
 	 				</a>
 	 				<#if menu.childMenus?? && (menu.childMenus?size > 0) >
             			<div class="megamenu mm-1col" >
            				<!-- Row 01 -->
                			<div class="mm_row clearfix">
                  				<!-- Column 01 -->
                  				<div class="mm_col">
                    				<section class="mm_callout">
                      				<div class="bd">
                        				<ul class="mm_list">
								 		<#list menu.childMenus as submenu> 
					      					<li style="width:100%;">
												<a style="width:100%;" href='${submenu.menuAction}'>${submenu.menuName}</a>
					      			 		</li>
					      	 			</#list>	
					     

										</ul>
					
				    				</div>
				   					</section>
				  				</div>
			    			</div>
			   			</div>
		
					</#if>
				</li>	
				</#list>

    			</ul>
			</div>
		</nav>
	</div>
<div id="ajaxSpinnerContainer" class="ajaxSpinnerContainer">
  <div class="loader">
      <a data-demo="logoHref" href="#" title="">
        <!-- <img data-demo="logo" src="images/ajax-loader.gif"> -->
         <img data-demo="logo" src="images/progress_bar.gif">
      </a> 
   </div>
</div>  
<script type="text/javascript" src="js/menu.js"></script>
   </div><!-- close container -->           
<script>
function enableLoader() {
	$(".ajaxSpinnerContainer").show();
	$(".ajaxSpinnerContainer").addClass("modal-backdrop");
	$(".ajaxSpinnerContainer").addClass("fade");
	$(".ajaxSpinnerContainer").addClass("in");
}
function disableLoader() {
	$(".ajaxSpinnerContainer").hide();
	$(".ajaxSpinnerContainer").removeClass("modal-backdrop");
	$(".ajaxSpinnerContainer").removeClass("fade");
	$(".ajaxSpinnerContainer").removeClass("in");
}
$(document).submit(function(){
	enableLoader();
});
function openWin(url)
{
var myWindow = window.open(url,"Help","width=800,height=600,resizable=yes,scrollbars=yes");
}
$( document ).ajaxStart(function( event, xhr, settings ) {
	enableLoader();
});
$(document).ajaxStart($.blockUI).ajaxStop($.unblockUI);
$( document ).ajaxComplete(function( event, xhr, settings ) {
	disableLoader(); 
	if(xhr.status == '405' || xhr.status == '403' || xhr.status == '500'){
        window.location = '/mint/login.ftl';
	}
});
$(document).ready(function() {
	disableLoader();
	$(".required").each(function(){
		 $(this).after("<font face='verdana,arial' size='-1' color='red'>*</font>");
	});
});
$(document).ready(function() {
	  if(document.getElementById('pv_glance_fixed_table11')){
			$('#participant_glance_table11, #participant_glance_table_header11').css('width','962px');
				$(".gsfhtData").scroll(function(){
					$(".gsfhtHeader")
					.scrollLeft($(".gsfhtData").scrollLeft());
				});
				$(".gsfhtHeader").scroll(function(){
					$(".gsfhtData")
					.scrollLeft($(".gsfhtHeader").scrollLeft());
				});	
		}
			
		$('#participant_glance_table11').find('th').each(function(i) {
			$($('#participant_glance_table_header11').find('th')[i]).css('width',$(this).css('width')); 
		});
		
		$('.sfhtHeader').css('width','967px');
		$('.sfhtData').css({
			width: '980px'
		});
		$(".input").each(function(){
		      $(this).prop('align', 'left');
		  }); 
		disableLoader();
});
</script>
</script>
