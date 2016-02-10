<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
   <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
    <script src="js/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script type="text/javascript" src="js/formValidation.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
    <script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
<script>


$(document).ready(function(){
	 $('#customHeaders').hide();
	 $('#responseDiv').hide();
	 $('#savedDataset').hide();
	 $('#environmentId').val('${webserviceSetupForm.environmentId}');
	 getServices();
		var serviceId =  '${webserviceSetupForm.serviceId}';
		var environmentId = document.getElementById("environmentId").value;
		if(serviceId > 0){
			 $('#serviceId').val(serviceId);
		}
		 if('${webserviceSetupForm.xmlInput!}' == "true"){
			 $('#xmlInput').prop('checked', true);
		 } else {
			 $('#rawInput').prop('checked', true);
		 }
		 getOperations();
		 enableLoader();
		 var  myVar = setTimeout(getServiceType, 1000);
	 
	 }); 
	 
function getOperations() {
	document.getElementById("requestParamstable").style.display = 'none';
	document.getElementById("reqParamsHeading").style.display = 'none';
	 $('#responseDiv').hide();
	 $('#operationId').html("");
	 var serviceId = document.getElementById("serviceId").value;
	if(serviceId <=0){
		serviceId =  '${webserviceSetupForm.serviceId}';
		 $('#serviceId').val(serviceId);
	}

	var environmentId = document.getElementById("environmentId").value;
	var request = $.ajax({  url: 	
	"GetOperationsFromServiceEnv.ftl",  
		type: "POST",  
		cache: false,
		data: { serviceId : serviceId, environmentId: environmentId},  
		dataType: "json"}); 
			request.done(function( msg ) {
				var Outmsg= JSON.stringify(msg);
				Outmsg = JSON.parse(Outmsg);
				 $('<option />', {value: "0", text: "Select"}).appendTo("#operationId");
				 $.each(Outmsg,function(key, val) {
					  $('<option />', {value: key, text: val}).appendTo("#operationId");
		    	 });
				 $('#operationId').val('${webserviceSetupForm.operationId}');
 		 }); 
  	request.fail(
 	 function( jqXHR, textStatus ){
	}); 
}

function oprCheck(item){
	 $('#responseDiv').hide();
	if(document.getElementById("xmlInput").checked){
		getTestSoapParameterSets(item.id);
	} else {
		getTestParameterSets(item.id);
	}
		//document.getElementById("buttonsTable").style.display='block';
		document.getElementById("testParamSetsHeading").style.display='block';
}
function displayValue(key) {

	var key = document.getElementById(key).value;
	var startpos = key.indexOf("ReqXML</td><td>"); 
	var key= key.replace("ReqXML</td><td>", "ReqXML</td><td><textarea cols='100' rows='20'>"); 
	var endpos = key.indexOf("</tr>",startpos); 
	var newstr = key.substring(startpos,endpos-5);
	key = key.replace(newstr, newstr+"</textarea>"); 
//	var w = window.open('', "", "width=600, height=400, scrollbars=yes");
//	var pos = key.indexOf("ReqXML"); 
	showReqParametersPopUp(key)
	/* w.document.write("<html><head></head><body><form><table border='1' width=60% style='border-collapse:collapse'><tr>")
	w.document.write(key);
	w.document.write("</tr></table></form></body></html>"); */
}

function showReqParametersPopUp(key) {
	$("#ReqParameters").dialog({
		width : 900,
		height : 450,
		title : 'Saved Request Parameters',
		resizable : false,
		position : [ 170, 0 ]
	});
	$("#ReqParameters").html('<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="completion_summary_content">');
	$("#ReqParameters").html('<div class="bd">');
	$("#ReqParameters").html('<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">');
	$("#ReqParameters").html('<table id="Saved_Request_Parameters" class="styleA fullwidth sfhtTable" summary=""></table></div></div></div>');
	$('#Saved_Request_Parameters').empty();
	$('#Saved_Request_Parameters').html(key);
	$("#ReqParameters").show();
	window.scrollTo(0, 1000);
}
	//If input type is individual request params, It will retrieve the saved input parameters 
	function getTestParameterSets() {
		enableLoader();
		$('#savedDataset').hide();
		$('#testParamSetsHeading').html('');
	
		var environmentId = document.getElementById("environmentId").value;
		var serviceId =  $("#serviceId").val();
		if(serviceId == 0) {
			serviceId = '${webserviceSetupForm.serviceId}';
			$("#serviceId").val(serviceId);
		}
	
		
		var operationId = $("#operationId").val();
		if(operationId == 0) {
		 	operationId = '${webserviceSetupForm.operationId}';
			$("#operationId").val(operationId);
		} 
		
		var dataAvail = false;
		var parameterSets = "";
		var request = $.ajax({
			url : "GetTestParamSets.ftl",
			type : "POST",
			cache : false,
			data : {
				serviceId : serviceId,
				environmentId : environmentId,
				operationId : operationId
			},
			dataType : "json"
		});
		request
				.done(function(msg) {
					disableLoader();
					var Outmsg = JSON.stringify(msg);
					Outmsg = JSON.parse(Outmsg);
					var count = 1;
					$
							.each(
									Outmsg,
									function(key, val) {
										
										var paramset = key.split(":");
										parameterSets += "<tr><td class='input'>";
										parameterSets += '<input type="radio" name="paramSetId" id ="paramSetId" value="' +paramset[0] + '" style="width:20px;"></input></td>';
										parameterSets += "<td class='lbl'><a href='#' onclick=\"displayValue('i"
												+ count
												+ "')\">"+paramset[1]+"</a>";
										parameterSets += "<input type='hidden' id='i"
												+ count + "' value='";
										for ( var paramSetKey in val) {
											 $('#savedDataset').show();
											dataAvail = true;
											var paramName = paramSetKey;
											var paramValue = val[paramSetKey];
											parameterSets += '<tr><td>'
													+ paramName + '</td>';
											parameterSets += '<td>'
													+ paramValue + '</td></tr>';
										}
										parameterSets += "'></td></tr>";
										count++;
									});
					if (dataAvail == true) {
						$('#testParamSetsHeading').append(parameterSets);
					}
				});
		request.fail(function(jqXHR, textStatus) {
			disableLoader();
		});

	}

	// If input type is XML, it will retrieve saved XML input parameters
	function getTestSoapParameterSets() {
		enableLoader();
		$('#savedDataset').hide();
		$('#testParamSetsHeading').html('');
	
		var environmentId = document.getElementById("environmentId").value;
		var serviceId =  $("#serviceId").val();
		if(serviceId == 0) {
			serviceId = '${webserviceSetupForm.serviceId}';
			$("#serviceId").val(serviceId);
		}
	
		
		var operationId = $("#operationId").val();
		if(operationId == 0) {
		 	operationId = '${webserviceSetupForm.operationId}';
			$("#operationId").val(operationId);
		} 
		var dataAvail = false;
		var parameterSets = "";

		var request = $.ajax({
			url : "GetTestSoapParamSets.ftl",
			type : "POST",
			cache : false,
			data : {
				serviceId : serviceId,
				environmentId : environmentId,
				operationId : operationId
			},
			dataType : "json"
		});
		request
				.done(function(msg) {
					disableLoader();
					var count = 1;
					var Outmsg = JSON.stringify(msg);
					Outmsg = JSON.parse(Outmsg);
					$
							.each(
									Outmsg,
									function(key, val) {
										
										var paramset = key.split(":");
										parameterSets += "<tr><td class='input'>";
										parameterSets += '<input type="radio" name="paramSetId" id ="paramSetId" value="' +paramset[0] + '" style="width:20px;"></input></td>';
										parameterSets += "<td class='lbl'><a href='#' onclick=\"displayValue('i"
												+ count
												+ "')\">"+paramset[1]+"</a>";
										parameterSets += "<input type='hidden' id='i"
												+ count + "' value='";
										for ( var paramSetKey in val) {
											 $('#savedDataset').show();
											dataAvail = true;
											var paramName = paramSetKey;
											var paramValue = val[paramSetKey];
										
											parameterSets+= '<tr><td>' + paramName + '</td>';
											if(paramName == "requestXML") {
										 		parameterSets+= '<td><textarea name="'+paramName
														+ '" id="'+paramName+'" style="width:100%; height:100%" rows=100 cols=100>'+paramValue+'</textarea></td></tr>';
											} else {
												parameterSets+= '<td>'+paramValue+'</td></tr>';
											}

										}
										parameterSets += "'></td></tr>";
										count++;
									});
					if (dataAvail == true) {
						$('#testParamSetsHeading').append(parameterSets);
					}
				});
		request.fail(function(jqXHR, textStatus) {
			disableLoader();
		});

	}

	// Retrieve the services, mapped to the selected environment
	function getServices() {
		document.getElementById("requestParamstable").style.display = 'none';
		document.getElementById("reqParamsHeading").style.display = 'none';
		 $('#responseDiv').hide();
		enableLoader();
		$('#serviceId').html('');
		/*document.getElementById("testParamSetsTable").style.display = 'none';
		document.getElementById("testParamSetsHeading").style.display = 'none';
		document.getElementById("requestParamstable").style.display = 'none';
		document.getElementById("submitReqParams").style.display = 'none';
		document.getElementById("reqParamsHeading").style.display = 'none';
		document.getElementById("operation").value = "";*/

		var environmentId = document.getElementById("environmentId").value;
		
		var request = $.ajax({
			url : "GetServiceByEnvId.ftl",
			type : "POST",
			cache : false,
			data : {
				environmentId : environmentId
			},
			dataType : "json"
		});
		
		 $('#serviceId').val('${webserviceSetupForm.serviceId}');
		 
		request.done(function(msg) {
			disableLoader();
			var Outmsg = JSON.stringify(msg);
			Outmsg = JSON.parse(Outmsg);
			 $('<option />', {value: "0", text: "Select"}).appendTo("#serviceId");
			$.each(Outmsg, function(key, val) {
				$('<option />', {
					value : key,
					text : val
				}).appendTo("#serviceId");
			});
			
			if('${webserviceSetupForm.serviceId}' > 0){
				var serviceId = ${webserviceSetupForm.serviceId};
				 $('#serviceId').val(serviceId);
			}
			
		});
		request.fail(function(jqXHR, textStatus) {
			disableLoader();
		});
	
	}

	function getServiceType() {
		 $('#responseDiv').hide();
		enableLoader();
		var operationId =  $("#operationId").val();
		

		/* if(operationId == 0) {
			operationId = '${webserviceSetupForm.operationId}';
			$("#operationId").val(operationId);
		} */
		
		var serviceId =  $("#serviceId").val();
		if(serviceId == 0) {
			serviceId = '${webserviceSetupForm.serviceId}';
			$("#serviceId").val(serviceId);
		}
		
		
		var request = $.ajax({
			url : "GetServiceType.ftl",
			type : "POST",
			cache : false,
			data : {
				serviceId : serviceId
			},
			dataType : "json"
		});
		request
				.done(function(msg) {
					disableLoader();
					var Outmsg = JSON.stringify(msg);
					Outmsg = JSON.parse(Outmsg);
					$
							.each(
									Outmsg,
									function(key, val) {
										if (val == "wadl") {
										
											document.getElementById("xmlInput").disabled = true;
											document.getElementById("rawInput").disabled = false;
											document.getElementById("rawInput").checked = true;
											getParameters();
											getTestParameterSets();
										} else if (val == "wsdl") {
											document.getElementById("xmlInput").disabled = false;
											document.getElementById("rawInput").disabled = false;
											if (document
													.getElementById("xmlInput").checked == true) {

												getXMLParameters();
												getTestSoapParameterSets();
											} else if (document
													.getElementById("rawInput").checked == true) {

												getParameters();
												getTestParameterSets()
											}
										}
									});
					disableLoader();
				});
		request.fail(function(jqXHR, textStatus) {
			disableLoader();
		});

	}

	function getParameters() {
		 $('#responseDiv').hide();
		enableLoader();
		document.getElementById("requestParamstable").style.display = 'block';
		document.getElementById("reqParamsHeading").style.display = 'block';
		document.getElementById("submitReqParams").style.display = 'block';
		$("immediateResponse").show();
		document.getElementById("customHeaders").style.display = 'block';

		// To reset the link (Click here to view the response)


		var serviceId =  $("#serviceId").val();
		if(serviceId == 0) {
			serviceId = '${webserviceSetupForm.serviceId}';
			$("#serviceId").val(serviceId);
		}
		var operationId = $("#operationId").val();
		if(operationId == 0) {
		 	operationId = '${webserviceSetupForm.operationId}';
			$("#operationId").val(operationId);
		} 
		
		if (operationId != 0 && operationId != null && operationId != "") {
			var request = $.ajax({
				url : "GetReqParameterList.ftl",
				type : "POST",
				cache : false,
				data : {
					serviceId : serviceId,
					operationId : operationId
				},
				dataType : "json"
			});
			request
					.done(function(msg) {
						disableLoader();
						var Outmsg = JSON.stringify(msg);
						Outmsg = JSON.parse(Outmsg);
						var parameters = "";
						$
								.each(
										Outmsg,
										function(paramName, paramType) {
											
											var str = paramType.split("^");
											paramType = str[0];
											var reqXML = str[1];
											
											parameters += "<tr><td>"
													+ paramName + "</td><td>"
											if (paramName != null
													&& (paramType == "x" || paramType == "X")) {
												parameters += '<textarea style="width:100%; height:100%" name="'
														+ paramName
														+ '" id="'+paramName+'" rows=20 cols=100>'+reqXML+'</textarea></td></tr>';

											} else {
												parameters += '<input type="text" id="'+paramName+'" name="'+paramName
									+ '"></td></tr>';
											}
											parameters += '<tr><td>&nbsp;</td><td>&nbsp;</td></tr>'
										});
						$("#requestParamstable").html(parameters);
					});
			request.fail(function(jqXHR, textStatus) {
				disableLoader();
			});

		} else {
			disableLoader();
			document.getElementById("requestParamstable").style.display = 'none';
			document.getElementById("reqParamsHeading").style.display = 'none';
			document.getElementById("submitReqParams").style.display = 'none';
			// To reset the link (Click here to view the response)
			var newlink = document.getElementById("newlink");
			if (newlink != null) {
				newlink.parentNode.removeChild(newlink);
			}
		}
	
	}

	function getXMLParameters() {
		 $('#responseDiv').hide();
		document.getElementById("requestParamstable").style.display = 'block';
		document.getElementById("reqParamsHeading").style.display = 'block';
		document.getElementById("submitReqParams").style.display = 'block';
		$("immediateResponse").show();
		document.getElementById("customHeaders").style.display = 'block';

		var parameters = "<tr style='white-space:nowrap; padding: 0px; margin: 0px;'><td> <textarea id='requestXML'  style='width:100%; height:100%' name='requestXML' cols='100' rows='15' id='reqXml'>";
		
		var serviceId =  $("#serviceId").val();
		if(serviceId == 0) {
			serviceId = '${webserviceSetupForm.serviceId}';
			$("#serviceId").val(serviceId);
		}
	
		
		var operationId = $("#operationId").val();
		if(operationId == 0) {
		 	operationId = '${webserviceSetupForm.operationId}';
			$("#operationId").val(operationId);
		} 
		if (operationId != 0 && operationId != null && operationId != "") {

			var request = $.ajax({
				url : "GetXMLParameter.ftl",
				type : "POST",
				cache : false,
				data : {
					serviceId : serviceId,
					operationId : operationId
				},
				dataType : "json"
			});
			request.done(function(msg) {
				var Outmsg = JSON.stringify(msg);
				Outmsg = JSON.parse(Outmsg);
				$.each(Outmsg, function(key, val) {
					parameters += val;
				});
				parameters+="</textarea></td></tr>";
				$("#requestParamstable").html(parameters);
			});
			request.fail(function(jqXHR, textStatus) {
			});

		} else {
			document.getElementById("requestParamstable").style.display = 'none';
			document.getElementById("reqParamsHeading").style.display = 'none';
			document.getElementById("submitReqParams").style.display = 'none';

			// To reset the link (Click here to view the response)
			var newlink = document.getElementById("newlink");

			if (newlink != null) {
				newlink.parentNode.removeChild(newlink);
			}
		}
	}

	function addHttpHeaders() {
		 $('#customHeaders').show();
		 $('#customHeaders').focus();
		 
		$('#customHttpHeaders').append("<tr>\
    		<td><label>Name</label></td>\
    		<td><input type='text' name='headerName' id='headerName' value=''></td>\
    		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\
    		<td><label>Value</label></td>\
    		<td><input type='text' name='headerValue' id='headerValue' value=''>\
    		<a class='deleteIcon np' onClick='removeElements(this)' width='20' height='20'><span class='icon nmt plxs' ></span></td>\
    	</tr><tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
	/* 	var rowCount = $('#customHttpHeaders tr').length;
		alert(rowCount); */
	}

	function removeElements(btnelem) {
		$(btnelem).closest('tr').next().remove();
		$(btnelem).closest('tr').remove();
	/* 	var rowCount = $('#customHttpHeaders tr').length;
		alert(rowCount/2); */
	}
	function submitParams() {
		enableLoader();
		var serviceId = $("#serviceId").val();
		var operationId = $("#operationId").val();
		var environmentId = $("#environmentId").val();
		var count = 0;
		var elemntArray = [];
		$('#customHttpHeaders tr')
				.each(function() {
					if(count == 0){
							elemntArray.push($(this)
									.find("[name='headerName']").val()
									+ "~"
									+ $(this).find("[name='headerValue']")
											.val());
							count =1;
					} else {
						count =0;
					}
						
				});
		document.getElementById("elementsArray").value = elemntArray;
		if(environmentId == "0"){
			disableLoader();
			alert('Please Select the Environment');
		} else if (serviceId == "0") {
			disableLoader();
			alert('Please Select a Service');
		} else if (operationId == ""  || operationId == "0") {
			disableLoader();
			alert('Please Select an Operation');
		} else {
			var boxes = document.getElementsByTagName('input');
			var count1 = count*2;
			var txtBoxCount = 0;
			var datasetFlag =0;
			for ( var i = 0; i < boxes.length; i++) {
				if (boxes[i] != null) {
					if (boxes[i].id == "datasetName") {
						if (boxes[i].value == "" || boxes[i].value == null) {
							datasetFlag =1;
						}
					}

				}
			}
			
			for ( var i = 0; i < boxes.length; i++) {
				if (boxes[i] != null) {
					if (boxes[i].type == "text") {
						txtBoxCount++;
						if (boxes[i].value == "" || boxes[i].value == null) {
							count1++;
						}
					}

				}
			}
			txtBoxCount = txtBoxCount-1;
			if (datasetFlag ==1){
				disableLoader();
				alert("Enter Dataset Name");
			} else if (count1 == txtBoxCount && !(count1 == 0 && txtBoxCount == 0)) {
				disableLoader();
				alert("Enter required parameters");
			} else if (document.getElementById("xmlInput").checked) {
				document.DatasetForm.action = 'SaveXmlInputs.ftl';
				document.DatasetForm.submit();
			} else if (document.getElementById("rawInput").checked) {
				document.DatasetForm.action = 'SaveRawInputs.ftl';
				document.DatasetForm.submit();
			}
		}

	}

	function getServiceResponse() {
		enableLoader();
		 $('#responseDiv').show();
		$('#newlink').html('');
		var paramSetId = "";
			var serviceId =  '${webserviceSetupForm.serviceId}';
		if(serviceId == 0) {
			serviceId = $("#serviceId").val();
		}else {
			$("#serviceId").val(serviceId);
		}
		
		var operationId = '${webserviceSetupForm.operationId}';
		if(operationId == 0) {
		 	operationId = $("#operationId").val();
		} else {
			$("#operationId").val(operationId);
		}
		var environmentId = document.DatasetForm.environmentId.value;
		var len = document.DatasetForm.paramSetId.length;

		var i;
		for (i = 0; i < len; i++) {
			if (document.DatasetForm.paramSetId[i].checked) {
				paramSetId = document.DatasetForm.paramSetId[i].value;

				break;
			}
		}
		if (paramSetId == "") {
			paramSetId = document.DatasetForm.paramSetId.value;
		}

		var url = "";
		if (document.getElementById("xmlInput").checked) {
			url = "WSTestingSoapOperationDataSet.ftl";
		} else {
			url = "WSTestingOperationDataSet.ftl";
		}

		var request = $.ajax({
			url : url,
			type : "POST",
			cache : false,
			data : {
				serviceId : serviceId,
				operationId : operationId,
				paramSetId : paramSetId,
				environmentId : environmentId
			},
			dataType : "text"
		});
		request.done(function(msg) {
			disableLoader();
			$("#responseXml").html(msg);
			var newlink = document.getElementById("newlink");
			if (newlink != null) {
				newlink.parentNode.removeChild(newlink);
			}
			var a = document.createElement('a');
			var linkText = document
					.createTextNode("Click to View Response");
			a.appendChild(linkText);
			a.title = "View Response";
			a.href = "#";
			a.id = "newlink";
			a.onclick = function() {
				showServiceResponsePopUp(msg);
			};
			document.getElementById("linkCol").appendChild(a);

		});
		request.fail(function(jqXHR, textStatus) {
			disableLoader();
		});
	}

	function showServiceResponsePopUp(key) {
		$("#ServiceResponse").dialog({
			width : 700,
			height : 650,
			title : 'Service Response',
			resizable : false,
			position : [ 170, 0 ]
		});
		$("#ServiceResponse")
				.html(
						'<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="completion_summary_content">');
		$("#ServiceResponse").html('<div class="bd">');
		$("#ServiceResponse")
				.html(
						'<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">');
		$("#ServiceResponse")
				.html(
						'<table id="Service_Response" class="styleA fullwidth sfhtTable" summary=""><textarea row="100" cols="150" style="width:100%; height:100%">'+key+'</textarea></table></div></div></div>');
		$('#Service_Response').empty();
		//	$('#Service_Response').html(key);
		/* document.getElementById('Service_Response').appendChild(
				document.createTextNode(key)); */
		$("#ReqParameters").show();
		window.scrollTo(0, 1000);
	}
	function testOperation() {
		enableLoader();
		 $('#responseDiv').show();
		var serviceId = document.DatasetForm.serviceId.value;
		var operationId = document.DatasetForm.operationId.value;
		var environmentId = document.DatasetForm.environmentId.value;
		if(environmentId == "0"){
			disableLoader();
			alert('Please Select the Environment');
		} else if (serviceId == "0") {
			disableLoader();
			alert('Please Select a Service');
		} else if (operationId == ""  || operationId == "0") {
			disableLoader();
			alert('Please Select an Operation');
		}

		var boxes = document.getElementsByTagName('input');
		var count1 = 0;
		var txtBoxCount = 0;
		var requestParamters = '';
		var elemntArray = [];
		$('#customHttpHeaders tr')
				.each(
						function() {
							elemntArray.push($(this)
									.find("[name='headerName']").val()
									+ "~"
									+ $(this).find("[name='headerValue']")
											.val());
						});

		document.getElementById("elementsArray").value = elemntArray;
		$('#newlink').html('');
		
		
		
		for ( var i = 0; i < boxes.length; i++) {
			if (boxes[i] != null) {
				if (boxes[i].type == "text" && boxes[i].id != "datasetName") {
					txtBoxCount++;

					if (boxes[i].value == "" || boxes[i].value == null) {
						count1++;
					}
				}

			}
		}
		
		
	 if (count1 == txtBoxCount && !(count1 == 0 && txtBoxCount == 0)) {
		 	disableLoader();
			alert("Enter required parameters");
		} else {

			var data = $('#DatasetForm').serialize();
			var url = "";
			if (document.getElementById("xmlInput").checked) {
				url = "WSSoapTestingOperation.ftl";
			} else if (document.getElementById("rawInput").checked) {
				url = "WSTestingOperation.ftl";
			}

			var request = $.ajax({
				url : url,
				type : "POST",
				cache : false,
				data : data,
				dataType : "text"
			});
			request.done(function(msg) {
				disableLoader();
				$("#responseXml").html(msg);
				var newlink = document.getElementById("newlink");
				if (newlink != null) {
					newlink.parentNode.removeChild(newlink);
				}
				var a = document.createElement('a');
				var linkText = document
						.createTextNode("Click to View Response");
				a.appendChild(linkText);
				a.title = "View Response";
				a.href = "#";
				a.id = "newlink";
				a.onclick = function() {
					showServiceResponsePopUp(msg);
				};
				document.getElementById("linkCol").appendChild(a);

			});

			request.fail(function(jqXHR, textStatus) {
				disableLoader();
			});
		}

	}
</script>

<body onLoad="window.scrollTo(0,readCookie('ypos'))">
	<div id="ReqParameters" title="Request Parameters">
	</div>
	<div id="ServiceResponse" title="Service Response">
	</div>


<div class="page-content" id="pagecontent" role="main" tabindex="-1">
<form id="DatasetForm" modelAttribute="webserviceSetupForm" method="POST" name="DatasetForm">
<input type="hidden" id="elementsArray" name="elementsArray" value="">
<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Webservice Testing</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
							<table class="bd rowheight35" id="tblParams" cellpadding='0' cellspacing='0' border='0' align='left' width='100%' height='100%' >
					<tr class="lblFieldPair">
						<td class="lbl">
							<font face="verdana,arial" size=-1>Environment</font>
						</td>
						<td class="input">
							<select id="environmentId" name="environmentId" onChange="getServices();">
										<option value="0">Select</option>
										<#list environmentCategoryList as environment> 
												<option value="${environment.environmentCategoryId}">		 
												${environment.environmentCategory.environmentCategoryName}</option>
										</#list>
							</select><font color="red" size=-1>&nbsp;*</font>
						</td>
					</tr>
				
					<tr class="lblFieldPair">
						<td class="lbl">
							<font face="verdana,arial" size=-1>Service</font>
						</td>
						<td class="input">
							<select id="serviceId" name="serviceId" onChange='getOperations();' tabindex="1"> 
								<option value="0">Select</option>
							</select><font color="red" size=-1>&nbsp;*</font>
						</td>
					</tr>
				
					
				
				
					<tr class="lblFieldPair">
						<td class="lbl">
							<font face="verdana,arial" size=-1>Operation</font>
						</td>
						<td class="input">
								<select id="operationId" name="operationId" tabindex="2" onchange="getServiceType()" >
								  	<option value="0">Select</option>
								</select><font color="red" size=-1>&nbsp;*</font>
						</td>
					</tr>
					<tr class="lblFieldPair">
						<td class="lbl">
							<font face="verdana,arial" size=-1>Enter Request Parameters As</font>
						</td>
						<td class="input">
								<input type="radio" id="xmlInput" style="width:20px" value="xmlInput" name="reqInputType" onclick="getXMLParameters();getTestSoapParameterSets()">XML </br>
							  	<input type="radio" id="rawInput" style="width:20px" value="rawInput"  name="reqInputType" onclick="getParameters();getTestParameterSets()">Request Params</td>
						</td>
					</tr>
				</table>
					
							
						</td>
					</tr>
				</table>
			</div>
		</section>
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Input Parameters</h6>
						</td>
					</tr>
					<tr class="bd">
						<td style="padding:10px">
								<div id="reqParamsHeading" style='width:100%; text-align:center; '>
									</div>
					
									<br>
									<table id="requestParamstable" style="margin-left:125px;width:80%;">
									</table>
						</td>
					</tr>
				</table>
			</div>
		</section>
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Custom Headers</h6>
						</td>
					</tr>
						<tr class="bd">
						<td class="btnBar" >
							<a href="#" class="btn btnSmall" id="SubmitSelectedServices" onclick="addHttpHeaders()"><span>Add custom headers</span></a>
						</td>
					</tr>
					<tr class="bd">
					
						<td style="padding:10px">
								<div id="customHeaders" style="width:100%; text-align:right;"> 
									<center>
										<table id="customHttpHeaders" class="sfhtTable" style="width:80%;">
										</table>
									</center>
															
								</div>
						</td>
					</tr>
				</table>
			</div>
		</section>
		
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					
					<tr class="bd">
					
						<td style="padding:10px">
							<table class="fw">
								<tr class="lblFieldPair">
									<td class="lbl">
										<font face="verdana,arial" size=-1>Dataset Name</font>
									</td>
									<td class="input">
											<input type="text" id="datasetName" name="datasetName"><font color="red" size=-1>&nbsp;*</font>
											<div id="submitReqParams"></div>
											<div style="float: right;">
												<a href="#" class="btn btn2" id="SubmitSelectedServices" onclick="testOperation()"><span>Test Operation</span></a>
												&nbsp;&nbsp;<a href="#" class="btn btnSmall" id="SubmitSelectedServices" onclick="submitParams()"><span>Save Dataset</span></a>
											</div>
									</td>
									<tr>
									<td colspan="2">
										<div id='loadingmessage' style='display:none'>
								  			<img src='./images/ajax-loader.gif'/>
										</div>
									</td>
									<td> </td>
								</tr>
									
								</tr>
								
							</table>
						
						</td>
					</tr>
				</table>
			</div>
		
		<div id="savedDataset">
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Available datasets</h6>
						</td>
					</tr>
					<tr class="bd">
					
						<td style="padding:10px">
							<div id="content">
	 
								<table id="testParamSetsHeading" style="table-layout:fixed;" cellpadding="1" cellspacing="1">
								</table>
								
								<table border="0" cellpadding="0" cellspacing="0">
								
								</table>
								
								
									<table  width='100%'>
											
												<tr class="bd">
													<td class="btnBar" >
														<a href="#" class="btn" id="btnSubmit" onclick="getServiceResponse()"><span>Execute Dataset</span></a>
													</td>
												</tr>
												
											<tr>
											</tr>
									</table>	
							</div>
								
							</table>
						
						</td>
					</tr>
				</table>
			</div>
		</section>
		</div>
		<div id="responseDiv">
		<section class="mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
					<tr>
						<td class="hd">
							<h6>Response</h6>
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
					</tr>
					<tr class="bd">
						<td>
							<table class="fw">
								<tr class="lblFieldPair">
									
									<td style="width:145" id="linkCol" class="lbl">
									</td>
									<td class="input">
											
									</td>
									<tr>
									
									
								</tr>
									
								</tr>
								
							</table>
						</td>
						
					</tr>
					
				</table>
			</div>
		</section>
		</div>
		
</div>

</body>