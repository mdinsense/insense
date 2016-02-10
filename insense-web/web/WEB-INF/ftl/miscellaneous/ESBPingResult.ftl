<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
   <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
    <script src="js/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script type="text/javascript" src="js/formValidation.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
    <script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
	<script type="text/javascript">
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
		$("#ServiceResponse").html('<table id="Saved_Request_Parameters" class="styleA fullwidth sfhtTable" summary=""></table></div></div></div>');
		$('#Saved_Request_Parameters').empty();
		document.getElementById('Saved_Request_Parameters').appendChild(
			document.createTextNode(key));
		$("#ServiceResponse").show();
		window.scrollTo(0, 1000);
	}
	
	function validateCommonInputFields(){
		var environment=document.getElementById("environment").value;
		var resultDate=document.getElementById("resultDate").value;
		if(environment == "0"){
			alert('Please Select the Environment');
			document.esbFileUploadForm.environment.focus();
			return false;
		}else if(resultDate == "0"){
			alert('Please Select the result Date');
			document.esbFileUploadForm.resultDate.focus();
			return false;
		}
		return true;
	}
	
	function submitEsbPingPage(){
		if(validateCommonInputFields()){
			document.esbFileUploadForm.action ="ESBPingReports.ftl";
			document.esbFileUploadForm.submit();
		}
	}
	function getESBPingDates(){
		enableLoader();
		$("#resultDate").html("");
		var env = document.getElementById("environment").value;
		var request = $.ajax({  url: 	
		"getESBPingDates.ftl",  
			type: "POST",  
			data: { environment: env},  
			dataType: "json"}); 
				request.done(function( msg ) {
					disableLoader();
					var Outmsg= JSON.stringify(msg);
					Outmsg = JSON.parse(Outmsg);
					 $('<option />', {value: "0", text: "----------Select-------------"}).appendTo("#resultDate");
					 $.each(Outmsg,function(key, val) {
						 $('<option />', {value: key, text: val}).appendTo("#resultDate");
			    	});
	 		 }); 
	  	request.fail(
	 	 function( jqXHR, textStatus ){  
	 		disableLoader();
  		}); 
	
	}
	
</script>

<body onLoad="window.scrollTo(0,readCookie('ypos'))">

<div id="ServiceResponse" title="Service Response">
</div>

<form modelAttribute="webserviceSetupForm" method="POST"  name="esbFileUploadForm" id="esbFileUploadForm"
								action='<@spring.url "/SaveESBPingTestDetails.ftl"/>' enctype="multipart/form-data">
	<section class="drop mtlg">
		<div class="cm">
			<table cellpadding='0' cellspacing='0' width="100%;">
				<tr>
					<td class="hd">
						<h3>ESB Ping Results</h3>
					</td>
				</tr>
				<tr class="bd">
					<td align="center">
							<br>
								
								
								<table class="bd rowheight35">
							<br />
							<tr class="lblFieldPair">
								<td class="lbl">Environment Name</td>
								<td class="input">
										<select class="requiredSelect" id="environment" name="environment" onChange='getESBPingDates();'>
											<option value="0">----------------Select------------------</option>
												<option value="PROD-A">PROD-A</option>
												<option value="PROD-B">PROD-B</option>
												<option value="DR">DR</option>	
										</select>
								</td>
							</tr>
							<tr class="lblFieldPair">
								<td class="lbl">Ping Result Date</td>
								<td class="input">
									<select class="requiredSelect" id="resultDate" name="resultDate">
											<option value="0">----------------Select------------------</option>
									</select>
								</td>
							</tr>
							
						</table>
					<center><font color="red" size=-1>&nbsp;* mandatory &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></center>	
					</td>
					</tr>
								
					<tr><td>&nbsp;</td></tr>
					<tr class="bd">
						<td class="btnBar"><a href="#" onClick="submitEsbPingPage()"
											class="btn"><span>View Reports</span></a> 
						</td>
					</tr>
				</table>
				</div>
		</section>
		
		
<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="esb_pingResult_table_content">
  	<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
		<table id="esb_pingResult_table" class="styleA fullwidth sfhtTable" summary="">
			<caption></caption>
			<thead>
				<tr>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Service</th>
					<th class="txtl header w15">Ping Status</th>
					<th class="txtl header w10">Ping Response</th>
				</tr>
			</thead>
			<tbody>
			<#if serviceStatusList?has_content>
				<#list serviceStatusList as service> 
					<tr data-sortfiltered="false">
						<td>${service.serviceName!}</td>
						<td>${service.status!}</td>
						<td class="row">
							<#if service.htmlSource?? >
								<#assign resp=service.htmlSource> 	
								<input type="hidden" id="resp${service_index + 1}" value="${resp}">			
								<a href="javascript:displayXML('resp${service_index + 1}');">View</a>
							</#if>
						</td>
					</tr>
				</#list>
			<#else>
					<tr><td colspan='4'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
			</#if>
			</tbody>
		</table>
		</div>
		<@mint.tablesort id="#esb_pingResult_table" casesensitive="false" jsvar="esb_pingResult_table__js"/>
		<@mint.paginate table="#esb_pingResult_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
		
	</div></div>
	</form>

	</body>