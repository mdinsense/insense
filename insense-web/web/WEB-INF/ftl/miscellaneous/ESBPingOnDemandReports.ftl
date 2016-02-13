<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
   <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
    <script src="js/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script type="text/javascript" src="js/formValidation.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
    <script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
		<script>
		function displayXML(getValue){
				var xmlContent="";
				xmlContent = document.getElementById(getValue).value;
				showServiceResponsePopUp(xmlContent)
				
			}
		function showServiceResponsePopUp(key) {
			$("#ServiceResponse").dialog({
				width : 700,
				height : 650,
				title : 'Service Response',
				resizable : false,
				position : [ 170, 0 ]
			});
			$("#ServiceResponse").html('<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="completion_summary_content">');
			$("#ServiceResponse").html('<div class="bd">');
			$("#ServiceResponse").html('<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">');
			$("#ServiceResponse").html('<table id="Service_Response" class="styleA fullwidth sfhtTable" summary=""></table></div></div></div>');
			$('#Service_Response').empty();
			//	$('#Service_Response').html(key);
			document.getElementById('Service_Response').appendChild(
			document.createTextNode(key));
			$("#ServiceResponse").show();
			window.scrollTo(0, 1000);
		}
		
		</script>

<body onLoad="window.scrollTo(0,readCookie('ypos'))">

	<div id="ServiceResponse" title="Service Response">
	</div>
<section class="drop mtlg">
<form modelAttribute="webserviceSetupForm" method="POST"  name="esbFileUploadForm" id="esbFileUploadForm"
								action='<@spring.url "/SaveESBPingTestDetails.ftl"/>' enctype="multipart/form-data">
		<div class="cm">
			<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h3>ESB Ping Results</h3>
						</td>
					</tr>
					
				<tr class="bd">
					<td style="padding:5">
					
						<section class="drop mtlg" style='margin-left: 2cm'>
						<div class="cm" style='width:600px;margin-left: 2cm'>
						<table cellpadding='0' cellspacing='0' id = "servicesTobeAdded" width="100%" >
 	 						<tr>        	
						   		<td class="hd">S.No</font></td> 	       	
						   		<td class="hd">Service Name</font></td>   	
						   		<td class="hd">Ping Status</font></td>
						   		<td class="hd">Ping Response</font></td>
							</tr>
							<#list serviceUrlList as service>
							<#if service.status>
									<tr>
										<td>${service_index + 1}</td>
										<td>${service.serviceName!}</td>
										<td>
											<#if service.status>
												SUCCESS
											<#else>
												<#if service.serviceFound>
													FAILED
												<#else>
													SERVICE Not Found
												</#if>
											</#if>
										</td>
										<td>
										<#if service.htmlSource?? >
											<div id="responseXML" name="responseXML">
											<#assign resp=service.htmlSource> 
											<input type="hidden" id="resp${service_index + 1}" value="${resp}">		
											<a href="javascript:displayXML('resp${service_index + 1}');">View Ping Response</a>
											</div>
										</#if>
										</td>
									</tr>
								<#else>
									<tr>
										<td><font size=3 Color="Red">${service_index + 1}</font></td>
										<td><font size=3 Color="Red">${service.serviceName!}</font></td>
										<td><font size=3 Color="Red">
											<#if service.serviceFound>
												FAILED
											<#else>
												SERVICE Not Found
											</#if>
											</font>
										</td>
										<td>
										<#if service.htmlSource?? >
											<div id="responseXML" name="responseXML">
											<#assign resp=service.htmlSource> 
											<input type="hidden" id="resp${service_index + 1}" value="${resp}">		
											<a href="javascript:displayXML('resp${service_index + 1}');"><font size=3 Color="Red">View Ping Response</font></a>
											</div>
										</#if>
										</td>
									</tr>
								</#if>
								</#list>
 						
						</table>
						</div>
						</section>
					
					</td>
				</tr>
				</table>
				
			</div>
			</form>
		</section>
	</body>