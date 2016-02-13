
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
   <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
    <script src="js/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script type="text/javascript" src="js/formValidation.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
    <script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
     <style>  
     #accordion {   
      padding: 20px;    
      width: 350px;   
      height: 100px;  
      }  
      </style>
<script> 
var lastRowId; 
var wsSuiteArray=[];var jsonString="";var suiteObj;
$(document).ready(function(){
	lastRowId = $('#operTable tr:last').attr('id');
	
		  if('${webserviceSetupForm.xmlInput!}' == "true"){
			 $('#xmlInput').prop('checked', true);
			 $('#rawInput').prop('checked', false);
		 } else {
			 $('#xmlInput').prop('checked', false);
			 $('#rawInput').prop('checked', true);
		 } 
		 
		 
		
	 }); 
	 
$(function() {   
	$( "#accordion" ).accordion({   
		heightStyle: "content",collapsible: true ,event: "click hoverintent", autoHeight: true, active: 'none'
		});  

	
$("#accordion").click(function() {
    		var accIndex =  	$('#accordion').accordion('option', 'active');
		//	var serviceId = $(".ui-accordion-content-active").attr('id');
			var serviceId = $('.serviceClass')[accIndex].id;
			//alert(serviceId);
			document.getElementById("serviceId").value = parseInt(serviceId); 
			getOperations($("#serviceId").attr('value'));
	});  
	

	
});  

function cancelSubmission() {
	document.getElementById("WsAddTestcaseForm").setAttribute('action',"<@spring.url '/Home.ftl' />");
	document.getElementById("WsAddTestcaseForm").setAttribute('method',"POST");
	document.getElementById("WsAddTestcaseForm").submit();
}

function updateParameters(serviceId, operationId, environmentId ,paramset1 ){
	var wsSuite = {"serviceId":serviceId, "operationId":operationId, "environmentId":environmentId, "paramset":paramset1}; 
	wsSuiteArray.push(wsSuite);
}
function showParams(elem,operationId, paramset1,paramset2, parameterSets) {
	
		lastRowId = $('#operTable tr:last').attr('id');
		if(lastRowId == 'headerRow') {
			lastRowId = "row0";
		}
	
	var rownum = elem.value;//alert(elem.checked);

	if(elem.checked == true){
		
		 var aa = lastRowId.substring(3);
	
		 var currentRow = parseInt(aa, 10) +1;
		 		
	
	 	var active = $('#accordion').accordion('option', 'active');
		var serviceName= $("#accordion h3").eq(active).text();
		var serviceId = $('.serviceClass')[active].id;
		
		var oprId = operationId+"oprName";
		var operationName = $("#"+oprId).attr('value')
		var environmentId = document.getElementById("environmentId").value;
		var environmentName = $("#environmentId option:selected").text();
		var inputs= document.getElementById(parameterSets).value;
		
		 var operations = "<tr id='row"+ currentRow +"'><td>"+serviceName+"</td>";
			 operations += '<td>'+operationName+'<b>-</b>'+environmentName+'</td>';
			 var count = 1;
			operations+= "<td><a href='#' onclick=\"displayValue('i"+ currentRow +"')\">"+paramset2+"</a>";
			operations+="<input type='hidden' name='parametersets' id='parametersets' value='";
			operations+=paramset1;
			operations+= "'>";
			operations+="<input type='hidden' name='i"+currentRow+"' id='dataset"+ paramset1 +"' value='";
			operations+=inputs;
			operations+= "'></td>"; 
			$('#operTable').append(operations); 
			var wsSuite = {"serviceId":serviceId, "operationId":operationId, "environmentId":environmentId, "paramset":paramset1}; 
			wsSuiteArray.push(wsSuite);
	   
	} else {
 	
		for(var i=0; i<wsSuiteArray.length; i++){
			console.log(wsSuiteArray[i].paramset);
		   if(wsSuiteArray[i].paramset == paramset1){
	        	wsSuiteArray.splice(i, 1);  //removes 1 element at position i 
	            break;
	        }
	    }

		  var aa = lastRowId.substring(3);
		 lastRowId = "row" +  (parseInt(aa, 10) -1);
		
		 var row = document.getElementById("dataset"+rownum);
		// alert(row.parentNode);
		 //row.parentNode.removeChild(row); 
		  row.parentNode.parentNode.preventDefault();
		 row.parentNode.parentNode.remove();
	}
	

}

function showParamsXml(elem,operationId, paramset1,paramset2, parameterSets) {
	
		lastRowId = $('#operTable tr:last').attr('id');
		if(lastRowId == 'headerRow') {
			lastRowId = "row0";
		}

	var rownum = elem.value;//alert(elem.checked);

	if(elem.checked == true){
		
		 var aa = lastRowId.substring(3);

		 var currentRow = parseInt(aa, 10) +1;
		 		
	
	 	var active = $('#accordion').accordion('option', 'active');
		var serviceName= $("#accordion h3").eq(active).text();
		var serviceId = $('.serviceClass')[active].id;
		
		var oprId = operationId+"oprName";
		var operationName = $("#"+oprId).attr('value')
		var environmentId = document.getElementById("environmentId").value;
		var environmentName = $("#environmentId option:selected").text();
		var inputs= document.getElementById(parameterSets).value;
		
		 var operations = "<tr id='row"+ currentRow +"'><td>"+serviceName+"</td>";
			 operations += '<td>'+operationName+'<b>-</b>'+environmentName+'</td>';
			 var count = 1;
			operations+= "<td><a href='#' onclick=\"displayValue('i"+ currentRow +"')\">"+paramset2+"</a>";
			operations+="<input type='hidden' name='parametersets' id='parametersets' value='";
			operations+=paramset1;
			operations+= "'>";
			operations+="<input type='hidden' name='i"+currentRow+"' id='dataset"+ paramset1 +"' value='";
			operations+=inputs;
			
			operations+= "'></td>"; 
			$('#operTable').append(operations); 
			var wsSuite = {"serviceId":serviceId, "operationId":operationId, "environmentId":environmentId, "paramset":paramset1}; 
		
			wsSuiteArray.push(wsSuite);
	
	} else {
 	
		for(var i=0; i<wsSuiteArray.length; i++){
			console.log(wsSuiteArray[i].paramset);
		   if(wsSuiteArray[i].paramset == paramset1){
	        	wsSuiteArray.splice(i, 1);  //removes 1 element at position i 
	            break;
	        }
	    }

		  var aa = lastRowId.substring(3);
		//  alert("aa:"+aa);
		 lastRowId = "row" +  (parseInt(aa, 10) -1);
		// alert("lastRowId:"+lastRowId);
		 var row = document.getElementById("dataset"+rownum);
		// alert("row:"+row);
		// alert("Row "+row +" is to deleted ");
		  row.parentNode.parentNode.preventDefault();
		  row.parentNode.parentNode.remove();
		 
		 
	}
	

}
function submitSelectedServices(){

	var rawInput ='';
	var xmlInput='';
	
	
	var wsSuiteName = document.getElementById("wsSuiteName").value;
	var privateSuit = document.getElementById("privateSuit").checked;
	var wsSuiteId = document.getElementById("wsSuiteId").value;
	var environmentId = document.getElementById("environmentId").value;
	if($('#xmlInput').prop('checked')) {
		reqInputType ="xmlInput";
	} else {
		reqInputType ="rawInput";
	}
	
	suiteObj = {
			  "wsSuiteId" :wsSuiteId,
			  "wsSuiteArray" : wsSuiteArray,
		      "wsSuiteName": wsSuiteName,
		      "privateSuit":privateSuit,
		      "reqInputType":reqInputType,
		      "wsSuiteId":wsSuiteId,
		      "environmentId":environmentId
		   }
	document.getElementById("wsSuiteArray").value = wsSuiteArray;
	console.log(JSON.stringify(suiteObj));
	var data = $('#WsAddTestcaseForm').serialize();
	
	if(validateInputForAdd()&&validateCheckbox()) {
	var request = $.ajax({  url: 	
		"submitWsSuite.ftl", 
		
		headers: {
	        "Accept": "application/json",
	        "Content-Type": "application/json"
	    },
			type: "POST",
			data: JSON.stringify(suiteObj),
			dataType: "json"
			}); 
			request.done(function( msg ) {
				var Outmsg= JSON.stringify(msg);
				Outmsg = JSON.parse(Outmsg);
				if(Outmsg.toString()=="true"){
						alert("Webservice Suite saved successfully");
						suiteObj.wsSuiteId = 0;
					} else {
						alert("Some issues while saving testsuite");
					}
				  $("#operTable").find("tr:gt(0)").remove();
				  $("#wsSuiteName").val("");
				  clearDataSets();
				  $('#wsSuiteName').removeProp('disabled');
			
}); 
	  	request.fail(
	 	 function( jqXHR, textStatus ) 
	  		{ 
	  		});  
	} else {
		return false;
	}
	
}



/* function showOperations(serviceId) {
	alert(serviceId);
	if ($("#"+serviceId).attr('aria-expanded') == 'true') { 
	document.getElementById("serviceId").value=parseInt(serviceId); 
	getOperations($(".ui-accordion-content-active").attr('id'));
	}
} */
</script>
<script>



function getOperations(serviceId) {
	enableLoader();
//	$("#accordion").accordion({ active: 1 });	 
	 $(".operationIdDiv").html("");
	 $('#testParamSetsHeading').html("");
	var environmentId = document.getElementById("environmentId").value;
	var request = $.ajax({  url: 	
	"GetOperationsFromServiceEnv.ftl",  
		type: "POST",  
		cache: false,
		data: { serviceId : serviceId, environmentId: environmentId},  
		dataType: "json"}); 
			request.done(function( msg ) {
				disableLoader();
				var Outmsg= JSON.stringify(msg);
				Outmsg = JSON.parse(Outmsg);
				var operations ="";
				 var oprFlag =0;
				 $.each(Outmsg,function(key, val) {
					 oprFlag =1;
					 operations += '<tr><td><label for="' + val + '">' + val + '</label></td>';
					 operations += '<td><input type="hidden" id="'+key+'oprName" value="' + val + '"><input class="radio_opr" onclick="oprCheck(this)" type="radio" name="radio_opr" id="' + key + '" value="' + key + '" /></td></tr>';
		    	 });
				if(oprFlag==0){
					 operations += 'No operations Configured';
					 $('#operationIdDiv'+serviceId).html(operations);
				} else {
				 	 $('#operationIdDiv'+serviceId).append(operations);
				}
 		 }); 
  	request.fail(
 	 function( jqXHR, textStatus ){  
 		disableLoader();
			if(jqXHR.status == '403' || jqXHR.status == '500'){
	            // inactive session so redirect to login page
            window.location = '/mint/login.ftl';
		    }
 	 }); 
}

function oprCheck(item){
	 $('#testParamSetsHeading').html("");
	document.getElementById("operationId").value= item.id;
	var r =document.getElementById("operationId").value;
	if(document.getElementById("xmlInput").checked){
		getTestSoapParameterSets(item.id);
	} else {
		getTestParameterSets(item.id);
	}
		//document.getElementById("buttonsTable").style.display='block';
		document.getElementById("testParamSetsHeading").style.display='block';
}
function displayValue(key) 
{
	var key = document.getElementById(key).value;
	if(key.contains("requestXML") || key.contains("ReqXML")) {
		var startString = "<td class=crop>";
		var startpos = key.indexOf(startString) + startString.length;
		var endpos = key.indexOf("</tr>",startpos); 
		newstr = key.substring(startpos,endpos-5);
		var key= key.replace(newstr, "<textarea style='width:100%; height:100%;' cols='70' rows='15'>"+newstr+"</textarea>");
	}
	showReqParametersPopUp(key);
}
function displayValuePopup(key) 
{
	var key = document.getElementById(key).value;
	if(key.contains("requestXML") || key.contains("ReqXML")) {
		var startString = "<td class='crop'>";
		var startpos = key.indexOf(startString) + startString.length;
		var endpos = key.indexOf("</tr>",startpos); 
		newstr = key.substring(startpos,endpos-5);
		var key= key.replace(newstr, "<textarea style='width:100%; height:100%;' cols='70' rows='15'>"+newstr+"</textarea>");
	}
	showReqParametersPopUp(key);
}
function showReqParametersPopUp(key) {
	$("#ReqParameters").dialog({
		width : 700,
		height : 650,
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

function getTestParameterSets(operationId){
	enableLoader();
 	$('#testParamSetsHeading').html(''); 
	var operationId = document.getElementById(operationId).value;
	var serviceId = document.getElementById("serviceId").value;	
	var environmentId = document.getElementById("environmentId").value;	
	var dataAvail = false;
	var parameterSets="<tr><td class='hd'>" +
	 "<font face='verdana,arial' size=-1>DataSet</font>" +
	 "</td><td class='hd'></td></tr>";
	 
/* 	 var parameterSets="<tr><td class='hd'>" +
	 "<font face='verdana,arial' size=-1>DataSet</font>" +
	 "</td><td class='hd'><input type='checkbox' name='selectAll' id='select_all' onchange='checkAll()'" + 
	 "value='setCheckBoxVal' style='width:20px;'/></td></tr>"; */
	 
	var request = $.ajax({  url: 	
	"GetTestParamSets.ftl", 
		type: "POST",  
		cache: false,
		data: {serviceId : serviceId, environmentId: environmentId, operationId : operationId},  dataType: "json"});  
			request.done(function( msg ) {
				disableLoader();
				var Outmsg= JSON.stringify(msg);
				Outmsg = JSON.parse(Outmsg);
				var count = 1;
				 $.each(Outmsg,function(key, val) {
						var paramset = key.split(":");
						
						// paramset[0] -> ParamsetID
						// paramset[1] -> datasetName
						
					 parameterSets+= "<tr><td><a href='#' onclick=\"displayValue('i"+ count +"')\">"+paramset[1]+"</a>";
					 parameterSets+="<input type='hidden' name='i"+paramset[0]+"' id='i"+ count +"' value='";
					 var p="";
					 for (var paramSetKey in val){
						 dataAvail = true;
						 	var paramName  = paramSetKey;
						 	var paramValue = val[paramSetKey];
						 	p+= '<tr><td>' + paramName + '</td>';
							p+= '<td class=' + 'crop' + '>' + paramValue +'</td></tr>';
						}
					
					 parameterSets+=p;
					 parameterSets+= "'></td>";
					 parameterSets+="<td>"; 
					 parameterSets+='<input type="hidden" value="'+p+'" id="ij'+count+'" >'  ;
					// parameterSets+='<input type="checkbox" name="params" id ="params" value="' +key + '" style="width:20px;"></input></td></tr>' ;
					var idd = "ij"+ count;
					var myElem = document.getElementById('dataset'+paramset[0]);
					
					if (myElem == null) {
				 	   parameterSets+='<input type="checkbox" name="params" id ="params" value="' +paramset[0] + '" style="width:20px;" onclick=showParams(this,"'+operationId+'","'+paramset[0]+'","'+paramset[1]+'","'+idd+'")></input></td></tr>' ;
				 	} else {
				 		parameterSets+='<input type="checkbox" name="params" checked id ="params" value="' +paramset[0] + '" style="width:20px;" onclick=showParams(this,"'+operationId+'","'+paramset[0]+'","'+paramset[1]+'","'+idd+'")></input></td></tr>' ;
				 	}
					 count++;
		    	});
				 if(dataAvail == true){
				 $('#testParamSetsHeading').append(parameterSets);
				 }
				 document.getElementById(operationId).checked = true;
				// ('#operationId').attr('checked',true);
			 });
  	request.fail(
 	 function( jqXHR, textStatus ) 
  		{ 
 		disableLoader();
  		}); 

}

function getTestSoapParameterSets(operationId){
	enableLoader();
 	$('#testParamSetsHeading').html(''); 
	var serviceId = document.getElementById("serviceId").value;	
	var operationId = document.getElementById(operationId).value;
	var environmentId = document.getElementById("environmentId").value;	
	var dataAvail = false;var count = 1;
	var parameterSets="<tr><td class='hd'>" +
	 "<font face='verdana,arial' size=-1>DataSet</font>" +
	 "</td><td class='hd'></td></tr>";
	 
		/* var parameterSets="<tr><td class='hd'>" +
		 "<font face='verdana,arial' size=-1>DataSet</font>" +
		 "</td><td class='hd'><input type='checkbox' name='selectAll' id='select_all' onchange='checkAll()'" + 
		 "value='setCheckBoxVal' style='width:20px;'/></td></tr>";
		  */
	var request = $.ajax({  url: 	
	"GetTestSoapParamSets.ftl", 
		type: "POST",  
		cache: false,
		data: { serviceId : serviceId, environmentId: environmentId, operationId : operationId}, dataType: "json"}); 
			request.done(function( msg ) {
				disableLoader();
				var Outmsg= JSON.stringify(msg);
				Outmsg = JSON.parse(Outmsg);
				var count = 1;
				 $.each(Outmsg,function(key, val) {
					 var paramset = key.split(":");
				
					 parameterSets+= "<tr><td><a href='#' onclick=\"displayValue('i"+ count +"')\">"+paramset[1]+"</a>";
					 parameterSets+="<input type='hidden' id='i"+count+"' value='";
					 var p="";
					 for (var paramSetKey in val){
						 dataAvail = true;
						 	var paramName  = paramSetKey;
						 	var paramValue = val[paramSetKey];
						 	p+= '<tr><td>' + paramName + '</td>';
							p+= '<td class=' + 'crop' + '>' + paramValue +'</td></tr>';
						}
					 parameterSets+=p;
					 parameterSets+= "'></td>";
					 parameterSets+="<td>"; 
					 
					 parameterSets+='<input type="hidden" value="'+p+'" id="ij'+count+'" >'  ;
						// parameterSets+='<input type="checkbox" name="params" id ="params" value="' +key + '" style="width:20px;"></input></td></tr>' ;
						var idd = "ij"+ count;
						var myElem = document.getElementById('dataset'+paramset[0]);
						if (myElem == null) {
					 parameterSets+='<input onclick=showParamsXml(this,"'+operationId+'","'+paramset[0]+'","'+paramset[1]+'","'+idd+'") type="checkbox" name="params" id ="params" value="' +paramset[0] + '" style="width:20px;"></input></td></tr>' ;
						} else {
					 parameterSets+='<input onclick=showParamsXml(this,"'+operationId+'","'+paramset[0]+'","'+paramset[1]+'","'+idd+'") type="checkbox" checked name="params" id ="params" value="' +paramset[0] + '" style="width:20px;"></input></td></tr>' ;	
						}
					 count++;
		    	});
				 if(dataAvail == true){
				 $('#testParamSetsHeading').append(parameterSets);
				 }	
				 document.getElementById(operationId).checked = true;
 		 }); 
  	request.fail(
 	 function( jqXHR, textStatus ) 
  		{ 
 		disableLoader();
  		}); 

}


function checkAll(){
	   $('#select_all').click(function(event) {   
	        if(this.checked) {
	            // Iterate each checkbox
	            $(':checkbox').each(function() {
	                this.checked = true;                        
	            });
	        }
	        if(!this.checked) {
	            // Iterate each checkbox
	            $(':checkbox').each(function() {
	                this.checked = false;                        
	            });
	        }
	    });
	}



/* function addForAnotherService() {
	if(validateInputForAdd()&&validateCheckbox() && document.getElementById("rawInput").checked){
			document.getElementById("WsAddTestcaseForm").action="<@spring.url "/WSAddAnotherTestCase.ftl" />";	  			
			document.getElementById("WsAddTestcaseForm").submit();	
						
	} else if(validateInputForAdd()&&validateCheckbox() && document.getElementById("xmlInput").checked){
			document.getElementById("WsAddTestcaseForm").action="<@spring.url "/WSAddAnotherTestCaseV2.ftl" />";	  			
			document.getElementById("WsAddTestcaseForm").submit();	
	}
	else{
		return false;
	}
} */

function validateCommonInputFields(){
	var suiteName=document.getElementById("wsSuiteName").value;
	var environmentId=document.getElementById("environmentId").value;		
	if(suiteName == ""){
		alert('Please Enter the Suit Name');
		document.WsAddTestcaseForm.suiteName.focus();
		return false;
	}else if(environmentId == ""){
		alert('Please Select the Environment');
		document.WsAddTestcaseForm.environmentId.focus();
		return false;
	}
}

function validateInputForAdd(){
	var suiteName=document.getElementById("wsSuiteName").value;
	var environmentId=document.getElementById("environmentId").value;							

	var serviceId=document.getElementById("serviceId").value;
	var operationId=document.getElementById("operationId").value;
	if(wsSuiteArray.length > 0 && serviceId=="" && suiteName!="") {
		alert("Oops!! You did not make any chanegs");
	} else {
	
	if(suiteName==""){
			alert('Please Enter the Suite Name');
			return false;
		} else if(environmentId==""){
			alert('Please Select the environment');
			return false;
		} else if(serviceId==""){
			alert('Please Select a Service');
			return false;
		} else if(operationId==""){
				alert('Please Select an Operation');
				return false;
		}
		else {
		return true;
		}
	}
	
}

function validateCheckbox(){
	/* var selected=0;
	var onlySelectAll=0;
 	$(':checkbox').each(function() {
         if(this.id!="select_all" && this.checked == true)  {
             selected=1;
          }     
                                      
    });
    if(selected==1){
    	return true;
    }
    else{	
    	alert("Please select atleast 1 parameter set. If there are no parameter sets, please go to Web Service Testing page and add parameter Sets.");	
		return false;
	} */
	
	if(wsSuiteArray.length <= 0) {
		alert("Please add datasets");
		return false;
	} else {
		return true;
	}
}


function clearDataSets(){
	/* document.getElementById("operationId").value="0";
	var request = $.ajax({  url: 	
		"clearSavedDataset.ftl", 
			type: "POST",  
			cache: false
			}); 
			request.done(function( msg ) {
				//$( "#operTable" ).html('');
				  $("operTable").find("tr:gt(0)").remove();

	 		 }); 
	  	request.fail(
	 	 function( jqXHR, textStatus ) 
	  		{ 
	  		});  */
	  		$("#operTable").find("tr:not(:first)").remove();
	  		$("#params").prop("checked", false);
	  		$('#testParamSetsHeading').html("");
	  		$("#accordion").accordion({ active: "none" });
	  		wsSuiteArray=[];

	 // $("operTable").find("tr:gt(0)").remove();
}
function expandServiceSection(){
	$("#accordion").accordion({ active: "none" });	
}
function clearData() {
	  	    	var res = confirm("Added datasets will be cleared. Are you sure to change the input type?");
				 if (res == true) {
					 clearDataSets();
				 }
	  	  
}

function isEmpty( el ){
	if (e1.children().length == 0){
	    return true;
	}
	return false;
}





/* function submitSelectedServices(){
	document.getElementById("WsAddTestcaseForm").action="<@spring.url "/wSSubmitTestCases.ftl" />";
	document.getElementById("WsAddTestcaseForm").submit();
} */

</script>
<script type="text/javascript">
        var specialKeys = new Array();
        specialKeys.push(8); //Backspace
        specialKeys.push(9); //Tab
        specialKeys.push(46); //Delete
        specialKeys.push(36); //Home
        specialKeys.push(35); //End
        specialKeys.push(37); //Left
        specialKeys.push(39); //Right
	specialKeys.push(46); //Period
	specialKeys.push(47); //Slash
	specialKeys.push(46); //Period
	specialKeys.push(95);
        function IsAlphaNumeric(e) {
            var keyCode = e.keyCode == 0 ? e.charCode : e.keyCode;
            var ret = false;
            if(keyCode == 95) {
            	ret = true;
            } else {
            ret = (
            		(keyCode >= 48 && keyCode <= 57) || 
            		(keyCode >= 65 && keyCode <= 90) ||
            		(keyCode >= 97 && keyCode <= 122) || 
            		(specialKeys.indexOf(e.keyCode) !=-1 && e.charCode != e.keyCode)
            		);
       		 }
            document.getElementById("error").style.display = ret ? "none" : "inline"; 
            return ret;
        }
    </script>
<body onLoad="window.scrollTo(0,readCookie('ypos'))">
<div id="ReqParameters" title="Request Parameters"></div>

<section class="drop mtlg">
<form modelAttribute="webserviceSetupForm" method="POST" id="WsAddTestcaseForm" name="WsAddTestcaseForm" method="POST" >
<input type="hidden" id="wsSuiteId" name="wsSuiteId" value="${wsSuiteId!}">
<input type="hidden" id="editOrViewMode"  name="editOrViewMode" value="${editOrViewMode!}">
<input type="hidden" id="environmentName"  name="environmentName">
<input type="hidden" id=wsSuite" name="wsSuite">
<input type ="hidden" id="wsSuiteArray" name="wsSuiteArray">
<div class="cm">
<table cellpadding='0' cellspacing='0' border='0' width='100%'>
	<tr>
		<td class="hd"><h3>Add TestCases for Web Services</h3></td>
	</tr>
	<tr class="bd">
		<td style="padding:5"><br>

		<input type="hidden" id="operationId" name="operationId" value="${operationId!}">
		<input type="hidden" id="serviceId" name="serviceId" value="${serviceId!}">
		<table class="bd rowheight35">
			<tr class="lblFieldPair">
				<td class="lbl">Web Service Suite Name</td>
				<td class="input">
				<#if wsSuiteName?exists>
				<input type="text" name="wsSuiteName" id="wsSuiteName" value="${wsSuiteName!}" disabled onkeypress="return IsAlphaNumeric(event);" ondrop="return false;"
        onpaste="return false;">
				<#else>
				<input type="text" name="wsSuiteName" id="wsSuiteName" value="${wsSuiteName!}" onkeypress="return IsAlphaNumeric(event);" ondrop="return false;"
        onpaste="return false;">
				</#if>
				 <span id="error" style="color: Red; display: none">* Special Characters not allowed</span>
				<font color="red" size=-1>&nbsp;*</font>
				</td>
			</tr>
			
			<tr class="lblFieldPair">
				<td class="lbl">Environment</td>
				<td class="input">
				<select id="environmentId" name="environmentId"  onChange='expandServiceSection()'>
					<option value="">Select</option>
					<#list environmentCategoryList as environment> 
						<#if environmentId?exists>
						<#if environment.environmentCategoryId = environmentId>
							<option value="${environment.environmentCategoryId}" selected >	
							${environment.environmentCategory.environmentCategoryName}
							</option>
						<#else>
							<option value="${environment.environmentCategoryId}">	
							${environment.environmentCategory.environmentCategoryName}
							</option>
						</#if>
						</#if>
						
						<#if !environmentId?exists>
										<option value="${environment.environmentCategoryId}">		 
										${environment.environmentCategory.environmentCategoryName}</option>
						</#if>
					</#list>
				</select><font color="red" size=-1>&nbsp;*</font>
				</td>
			</tr>
			<!-- <tr class="lblFieldPair">
				<td class="lbl">Visible to Other Users</td>
				<td class="input">
					<input type="radio" id="visibleToOtherUsersYes" name="visibleToOtherUsers" value="${visibleToOtherUsers}" checked = "${visibleToOtherUsers}" >&nbsp;&nbsp;Yes</br> 
					<input type="radio" id="visibleToOtherUsersNo" name="visibleToOtherUsers" value="${visibleToOtherUsers}">&nbsp;&nbsp;No
				</td>
			</tr> -->	
			
			<tr class="lblFieldPair">
					<td class="lbl">Private Suit</td>
					<td class="input">
						<#if webserviceSetupForm?exists && webserviceSetupForm.privateSuit?exists && webserviceSetupForm.privateSuit == true>
							<input type="checkbox" id="privateSuit" name="privateSuit" checked>
						<#else>
							<input type="checkbox" id="privateSuit" name="privateSuit" >
						</#if>	
					</td>
			</tr>
			
			<tr class="lblFieldPair">
			<td class="lbl">Input Type</td>
			<td class="input"> 
				<input type="radio" id="xmlInput" value="xmlInput" name="reqInputType" onclick="clearData()" checked=${xmlInput!}>&nbsp;&nbsp;Input By XML</br>
			 	<input type="radio" id="rawInput" value="rawInput"  name="reqInputType" onclick="clearData()" checked=${rawInput!}>&nbsp;&nbsp;Input By Field
			</td>
			</tr>
					
		</table>

</td>
</tr>
</table>
<table cellpadding='0' cellspacing='0' border='0' width='100%' >
<tr>
<td>

	<div id="accordion" style="width:800px;height:100%">
							<#list servicesList as service> 
							<h3>${service.serviceName}</h3>
							<div id="${service.serviceId}" class="serviceClass">
								<div id="operationIdDiv${service.serviceId}" class="operationIdDiv">
								
								</div>
							</div>	
							</#list>
	</div>
					</td>
					<td></br></br>
					<div id="datasetDiv" style="overflow:auto ; max-height:175px; padding-bottom:10px;">
					<table class="styleA fullwidth sfhtTable" >
					<tr class="lblFieldPair" id="testParamSetsHeading">
			
					</tr>
					</table>
					</div>
					</td>
</tr>
<tr><td><center><font color="red" size=-1>&nbsp;* mandatory &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></center></td></tr>

</td>
</tr>
</table>

<table class="bd rowheight35" style="margin-left: 2%">
<tr>
<td>



<table class="styleA drop2 sfhtTable" id="operTable" cellpadding='0' cellspacing='0' border='0' width='70%' >
	<tr id="headerRow">
		<td class='hd' style="font-weight:bold; width:200px;">Selected Service</td>
		<td class='hd' style="font-weight:bold; width:600px;">Selected Operation - Environment</td>
		<td class='hd' style="font-weight:bold; width:600px;">Dataset</td>
	</tr>
	
	 <#if serviceMapUI?exists>
	<#list serviceMapUI as wsSuiteParams> 
	<script>
		updateParameters('${wsSuiteParams.serviceId}', '${wsSuiteParams.operationId}', '${wsSuiteParams.environmentId}' ,'${wsSuiteParams.parameterSetId}');
	</script>
	<tr id='row${wsSuiteParams.parameterSetId}' >
			<td>${wsSuiteParams.serviceName}</td>
		<td>
			${wsSuiteParams.operationName}
			<b>-</b>  
			${wsSuiteParams.environmentName}
		</td>	
		<#assign parmString="">
		<#list wsSuiteParams.wsDataset.parameterValuesMap?keys as key>
			<#assign parmString=parmString +"<tr><td>" + key +"</td><td class='crop'>" +wsSuiteParams.wsDataset.parameterValuesMap[key] + "</td></tr>">
		</#list>
		<td>
		    <textarea id="${wsSuiteParams.wsDataset.dataSet}" style="display:none;">${parmString}</textarea> 
			<a href='#' onclick="displayValuePopup('${wsSuiteParams.wsDataset.dataSet}')">${wsSuiteParams.wsDataset.dataSet}</a>
			<input type='hidden' name="i${wsSuiteParams.parameterSetId}" id="dataset${wsSuiteParams.parameterSetId}">
		</td>	
	</tr>	
	</#list>
	<#else>
	
	</#if> 
	
</table>

</td>
<tr>
</table>

<table  width='100%'>

<tr class="bd">
	<td class="btnBar" >
		<#if webserviceSetupForm?exists && webserviceSetupForm.editOrViewMode?exists && webserviceSetupForm.editOrViewMode == 'ViewMode'>
				<a href="#" class="btn disabled" id="SubmitSelectedServices" onclick="submitSelectedServices()"><span>Submit</span></a>
		<#else>
				<a href="#" class="btn" id="SubmitSelectedServices" onclick="submitSelectedServices()"><span>Submit</span></a>
		</#if>	
			<a href="#" class="btn" id="cancelButton" onclick="cancelSubmission()"><span>Cancel</span></a>
	
	
</table>



</div></form>
</section>
</body>