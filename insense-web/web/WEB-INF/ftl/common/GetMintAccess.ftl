<html>
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
	<div id="top-bar" align="Center">
		<br><font size=25 Color ="#ffffff">MinT</font>
	</div>
		<br><p align="center">
		<div align="center"><h2>Either Your session is timed out or You don't have access to MINT.</h2>If you have access to MINT, Please close the broser and login again. 
		<BR>To get the access please email to DL_Mint_Support@tiaa-cref.org.
<#include "Footer.ftl">	
</body>
</html>
