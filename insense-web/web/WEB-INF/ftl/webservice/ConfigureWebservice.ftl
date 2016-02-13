<script type="text/javascript">

$(function(){
	   $('#select_all').click(function(event) {  
	          if(this.checked) {
	            $('.checkbox1').each(function() {
	                this.checked = true;                        
	            });
	            $('.checkbox2').each(function() {
	                this.checked = false;                        
	            });
	        }
	        if(!this.checked) {
	            $('.checkbox1').each(function() {
	                this.checked = false;                        
	            });
	            $('.checkbox2').each(function() {
	                this.checked = false;                        
	            });
	        }
	    });
	})
	

function selectAll() {
	if(document.getElementById("select_all_opr").checked) {
		$('.checkbox_opr').each(function(){ this.checked = true; });
	} else {
		$('.checkbox_opr').each(function(){ this.checked = false; });
	}
}
	
function clearEnv() {
document.getElementById("environmentId").value =-1;
$('#checkboxes').empty();
}
//Onboarding the new services to MinT
function validateAndsave()
{

	if(!validateInput()){
		alert('Please select service(s)');
		return false;
	} else {
		enableLoader();
		document.getElementById("webserviceSetupForm").setAttribute('action',"<@spring.url '/AddWebservices.ftl' />");
		document.getElementById("webserviceSetupForm").submit();
	}
}	


//Submitting the endpoint url and selected operations
function submitEndpoint(){
	if(validateInputValues()){
		
		document.WSOperationForm.action="ConfigureWebservice.ftl";
		document.WSOperationForm.submit();
	/* 	$("#WSOperationForm").action="ConfigureWebservice.ftl";
		$("#WSOperationForm").submit(); */
	}
}

//Loading operations based the selected service and environment
function getOperations(){
	var serviceId = document.getElementById("serviceId").value;	
	    var request =$.ajax({
		        url: "GetOperationsByService.ftl",
		        type: "POST", 
		        cache: false,
		        data: { serviceId : serviceId},  
		        dataType: "json"
		      });   
		 
	 request.done(function( msg ) {
		 $('#checkboxes').html("");
			var Outmsg= JSON.stringify(msg);
			Outmsg = JSON.parse(Outmsg);
			 var operations = "<tr><td class='hd' style='font-weight:bold;'>" + 
				"<font face='verdana,arial' size=-1>Operation</font>" + 
				"</td><td class='hd' align='left'><input type='checkbox' class='checkbox1' name='select_all_opr' id='select_all_opr' onclick='selectAll(this)'></td></tr>"; 
			//	var operations = "";
			 $.each(Outmsg,function(key, val) {
				 var operationName = key.split(":")[0];
				 var methodType = key.split(":")[1];
				 operations += '<tr><td>' + operationName + '</td>';
				 operations += '<td>&nbsp;<input class="checkbox_opr" type="checkbox" name="' + key + '" id="' + val + '" value="' + val + '" /></td></tr>';
				 operations += '<input type="hidden" name="'+ val+'methodType" value = "'+methodType+'">';	
	    		});
			
			   $('#checkboxes').append(operations);
			   checkOperations();
		});
		
		request.fail(function( jqXHR, textStatus ){  
		}); 
	 
}


// Check operations if exist
function checkOperations(){
	var serviceId=document.getElementById("serviceId").value;
	var environmentId=document.getElementById("environmentId").value;
			
		$('.checkbox_opr').each(function() {
		 var operId=this.value;
		 var operName=this.name;
		 var request = $.ajax({  url: 	
			"GetSelectedOperations.ftl",  
				type: "POST",  
				cache: false,
				data: {serviceId : serviceId, environmentId : environmentId, operationId : operId},  
				dataType: "json"}); 
					request.done(function( msg ) {			
						var Outmsg= JSON.stringify(msg);
						Outmsg = JSON.parse(Outmsg);
						 $.each(Outmsg,function(key, val) {
							 var oprcheckbox=  document.getElementById(operId).value;
							 if (val== true){
								 document.getElementById(operId).checked = true;   
							 }
							 else{
							  document.getElementById(operId).checked = false;
							 }
				    		});
				
		 		 }); 
		  	request.fail(
		 	 function( jqXHR, textStatus ){ 
			  		
		 	 });                     
		});

}


//Validating input before submitting the endpoint Url and Operations
function validateInputValues(){
	if($("#serviceId").val() <= 0) {
		alert('Please Select a Service');
		return false;
	}	
	if($("#environmentId").val() <= 0) {
		alert('Please Select an Environment');
		return false;
	}	
	if(!$("#endPoint").val()) {
		alert('Please provide an EndPoint');
		return false;
	}	
	if(!validateInput()){
		alert('Please select an operation');
		return false;
	}
	return true;
}


//Validating the operations
function validateInput(){
	var selected=0;
 	$(':checkbox').each(function() {
         if(this.checked == true)  {
             selected=1;
          }                                    
    });
    if(selected==1){
    	return true;
    }
    else{	
		return false;
	}
}

function readCookie(name){
	return(document.cookie.match('(^|; )'+name+'=([^;]*)')||0)[2]
}
$(window).scroll(function () {
    document.cookie='ypos=' + $(window).scrollTop();
});

// Retrieve endpoint, if exists.
function checkEndpoint(){
		var serviceId=document.getElementById("serviceId").value;
		var environmentId=document.getElementById("environmentId").value;
		
		
	 var request = $.ajax({  url: 	
		"GetEndpoint.ftl",  
			type: "POST",  
			cache: false,
			data: {serviceId : serviceId, environmentId: environmentId},  
			dataType: "json"}); 
				request.done(function( msg ) {				
					var Outmsg= JSON.stringify(msg);
					Outmsg = JSON.parse(Outmsg);
					 $.each(Outmsg,function(key, val) {
						 document.getElementById(key).value = val;   	
			    	});
	 		 }); 
	  	request.fail(
	 	 function( jqXHR, textStatus )	{ 
	  	});                     

	
}

</script>
<body onLoad="window.scrollTo(0,readCookie('ypos'))">
<div class="page-content" id="pagecontent" role="main" tabindex="-1" style="margin-top: 10px;">
<form method="POST" modelAttribute="webserviceSetupForm" name="WebserviceForm"
					action=''  method="POST" id="webserviceSetupForm">

<section class="drop mtlg">
	<div class="cm">
		<table cellpadding='0' cellspacing='0' width="100%;">
			<tr>
				<td class="hd">
					<h3>Webservice Setup</h3>
				</td>
			</tr>
			<#if servicesToBeAdded?has_content>
			<tr class="bd">
				<td style="padding:5">
						
						<table id="megatable" class="styleA drop sfhtTable BoxBorder" colspan="1" colspace="1" style="width:80%; margin-top: 20px;">
						
							<tr >
								<td >
											<table cellpadding='0' cellspacing='0' id ="servicesTobeAdded" style="width:50%; margin-top: 10px;margin-left: 100px;">
			 	 						
												<#list servicesToBeAdded! as service> 
													<#if (service_index < servicePaginationCount) >
					  							<tr  >
					  								<td align="center" style="padding-top: 5px; padding-right: 5px; padding-bottom: 5px; padding-left: 5px;">		
													<div id="checkboxColumn" name="checkboxColumn">
															<input type="checkbox" name="${service}" id="checkbox1" class="checkbox1"/>
													</div>
													</td>
													<td style="padding-top: 5px; padding-right: 5px; padding-bottom: 5px; padding-left: 5px; margin-left: 2cm;"" >
												      	<div id="service" name="service"> 
																	${service}	
												 		</div>
													</td>
													
												</tr>
													</#if>
					 						 </#list>
									</table>
								</td>
								<td>
										<table cellpadding='0' cellspacing='0' id ="servicesTobeAdded" >
		 	 						
											<#list servicesToBeAdded! as service> 
												<#if (service_index >= servicePaginationCount) >
				  							<tr class="bd">
				  							<td align="center" style="padding-top: 5px; padding-right: 5px; padding-bottom: 5px; padding-left: 5px;">		
												<div id="checkboxColumn" name="checkboxColumn">
														<input type="checkbox" name="${service}" id="checkbox1" class="checkbox1"/>
												</div>
												</td>
												<td style="padding-top: 5px; padding-right: 5px; padding-bottom: 5px; padding-left: 5px; margin-left: 2cm;"" >
											      	<div id="service" name="service"> 
																${service}	
											 		</div>
												</td>
												
											</tr>
												</#if>
				 						 </#list>
										</table>
								
								</td>
							</tr>
							</table>
						
				</td>
				</tr>
			
	 				<tr class="bd" >
						<td class="btnBar"><a style="float: right" href="#" onClick="validateAndsave()"
						
							class="btn"><span>Load Service</span></a> 
						</td>
					</tr>

			</table>
		
			<#else>
			<tr><td>&nbsp;</td></tr>
						<tr>
							<td align="center">
									<div class="messageBodyContent"><span class="icon" title="Information"></span>${result}</div>
									</td>
						</tr>
				<tr><td>&nbsp;</td></tr>		
			</#if>
			
		</table>
						
	</div>
</section>

</form>
<form modelAttribute="webserviceSetupForm" method="POST" name="WSOperationForm" id="WSOperationForm">
<section class="drop mtlg">
		<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd" colspan="3">
							<h3>Configure Service</h3>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding: 5">
							
							<input type="hidden" id="submitDone" name="submitDone"/
								<table>
								<tr>
									<td width="50%">
										<table class="bd rowheight35"><br/>
											<tr class="lblFieldPair">
												<td class="lbl"><label>Service</label></td>
												<td class="input">
													<select id="serviceId" name="serviceId" tabindex="1" onchange='clearEnv()'> 
															<option value="-1">Select</option>
															<#list servicesList as service> 
																	<option value="${service.serviceId}">		 
																	${service.serviceName}</option>
															</#list>
												 		</select><font color="red" size=-1>&nbsp;*</font>
												</td>
											</tr>
											<tr class="lblFieldPair">
												<td class="lbl"><label>Environment</label></td>
												<td class="input">
													<select id="environmentId" name="environmentId"  onChange='getOperations();checkEndpoint()'>
														<option value="-1">Select</option>
														<#list environmentCategoryList as environment> 
																<option value="${environment.environmentCategoryId}">	
																${environment.environmentCategory.environmentCategoryName}
																</option>
														</#list>
													</select><font color="red" size=-1>&nbsp;*</font>
												</td>
											</tr>
											<tr class="lblFieldPair">
												<td class="lbl"><label>Endpoint</label></td>
												<td class="input">
													<input style="width:400px;" type="text" name="endPoint" id="endPoint" 
														value="${endPoint}"><font color="red" size=-1>&nbsp;*</font>
												</td>
											</tr>
										</table>
								
									</td>
									
									<td width="40%"></br>
										<table class="drop2 bd rowheight35 styleA sfhtTable" id="checkboxes">
										</table>
									</td>
									<td width="10%">
									</td>
								</tr>
								<tr class="rowheight35"><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>
								<tr class="bd">
									<td class="btnBar" colspan="3">
									<a class="btn" style="float: right" onclick="submitEndpoint();" href="#"><span>Submit</span></a>
									</td>
								</tr>
								</table>
							
						</td>
					</tr>
					
				</table>
			</div>
	</section>
	</form>



 
</form>
</div>
</body>
