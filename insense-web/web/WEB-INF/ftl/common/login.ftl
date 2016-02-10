<html>
<#import "spring.ftl" as spring />

  <head>
  	<link rel="stylesheet" type="text/css" href="./css/style.css" />
	<script type="text/javascript" src="./js/jquery.js"></script>
<script type="text/javascript">
		$(document).ready(function(){
		   $("#zone-bar li em").click(function() {
		   		var hidden = $(this).parents("li").children("ul").is(":hidden");
		   		
				$("#zone-bar>ul>li>ul").hide()        
			   	$("#zone-bar>ul>li>a").removeClass();
			   		
			   	if (hidden) {
			   		$(this)
				   		.parents("li").children("ul").toggle()
				   		.parents("li").children("a").addClass("zoneCur");
				   	} 
			   });
		});
</script>
	</head>
<body>

 <div class="logo">
      <a data-demo="logoHref" href="https://www.tiaa-cref.org/" title="TIAA-CREF Financial Services website">
        <img data-demo="logo" src="images/logo_tiaa.png" alt="TIAA-CREF - Financial Services">
      </a> 
    </div>  
    
	<div id="top-bar" align="Center">
		<br><font size=25 Color ="#ffffff">MinT</font>
	</div>
<div style="width: 60%; float:left;">
		<br><p align="center">
						<div class="hd" align="center"><h2>Welcome to MinT Home Login Screen</h2></div>
</div>

<div style="width: 40%; float:right">
		<form action='Home.ftl' method="POST">
		<font size=2 Color ="Red">
		<#if model.invalidUserName?exists>
			${model.invalidUserName}
		</#if>
		</font>
		<table align="center" cellspacing="5" cellpadding="10" style="width: 378px; "> 
			<tr><td>Login Id</td><td><input type="text" maxlength="8" name="loginUserName"></td></tr>
			<!--<tr><td>Password</td><td><input type="password" maxlength="15" name="pass" ></td></tr>-->
			<tr/>
			<tr><td align="center"></td><td><input type="submit" value="Login" ></td></tr>
		</table>
		</form>
</div>
		<#include "Footer.ftl">
		</body>

</html>