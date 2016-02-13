
 <script type="text/javascript" src="https://www.google.com/jsapi"></script>

<style>
.lblFieldPair .lbl {
 padding-right: 3px;
 width: auto;
}
@-moz-document url-prefix() { 
  .lblFieldPair .lbl {
     float:none;
  }
}
.lblFieldPair .input {
	display: table-cell;
}
.imgicon:HOVER {
	border: 1px solid #5ec2eb;
}
</style>
<script type="text/javascript">

	var dataArray =[]; var criteriaId =0;
	google.load("visualization", "1", {packages:["corechart"]});

  
    $(document).ready(function(){
    	// $('#xmlInput').prop('checked', true);
    	/* generateDataOnLoad(1, "groupDiv", "By Group");
    	generateDataOnLoad(2, "envDiv", "By Environment");
    	generateDataOnLoad(3, "funcDiv", "By Functionality"); */
    	
	 }); 
    
    function drawChartLoad(divName, title){
    	
    	$('#overallChart1').show();
    	$('#overallChart2').show();
    	var data = google.visualization.arrayToDataTable(dataArray);
    	 var options = {'title':title,
                 'width':500,
                 'height':500,
                 titleTextStyle: {color: 'black', fontName: 'Arial', fontSize: '12', fontWidth: 'normal'},};
		var chart = new google.visualization.PieChart(document.getElementById(divName));
		/*  function selectHandler() {
			 
			 var selectedItem = chart.getSelection()[0];
			
		       if (selectedItem) 
		       {
		    	 var criteriaType = data.getValue(selectedItem.row, 0);
		         var topping = data.getValue(selectedItem.row, 2); // to get the third arg from the passed array
		       //  var topping = data.getValue(selectedItem.row, 1); // to get the second arg from the passed array
		       
		         generateDataForBarChart(criteriaId, topping);
		       }
		     }
		google.visualization.events.addListener(chart, 'select', selectHandler); */
		chart.draw(data, options);   
		dataArray =[];
    }
    
    function drawBarChart(position, title) {
    	var lege = "";
    	var criteriaId = $('#criteriaId').val();
    	if(criteriaId == 1 && position==1) {
    		lege = "Functionality";
    	} else if(criteriaId == 1 && position==2) {
    		lege = "Environment";
    	}else if(criteriaId == 2 && position==1) {
    		lege = "Functionality";
    	} else if(criteriaId == 2 && position==2) {
    		lege = "Group";
    	} else if(criteriaId == 3 && position==1) {
    		lege = "Environment";
    	}  else if(criteriaId == 3 && position==2) {
    		lege = "Group";
    	}
    	    	
    	$('#overallChart1').hide();
    	$('#overallChart2').hide();
    	 var applndata = new google.visualization.DataTable(); 
         applndata.addColumn('string', 'Criteria'); 
         applndata.addColumn('number', lege); 
         applndata.addColumn({ type: 'string', role: 'annotation' });
         applndata.addRows(dataArray);
         var chart;
         if(position ==1) {
         	chart = new google.visualization.ColumnChart (document.getElementById('barChart1'));
   		 } else {
   			chart = new google.visualization.ColumnChart (document.getElementById('barChart2'));
   		 }
       //define options for visualization
         var options;
       
       		if(position ==1){
       			options = {width: 500, height: 300, is3D: true, title: title,
               		 titleTextStyle: {color: 'black', fontName: 'Arial', fontSize: '18', fontWidth: 'normal'},};
       		} else {
       			options = {width: 500, height: 300, is3D: true, 
                  		 titleTextStyle: {color: 'black', fontName: 'Arial', fontSize: '18', fontWidth: 'normal'},};
       		}
        //draw our chart
         chart.draw(applndata, options);
         dataArray =[];
         }
	
    function drawChart(){
    	$('#overallChart1').hide();
    	$('#overallChart2').hide();
    	var data = google.visualization.arrayToDataTable(dataArray);
    	 var options = {'title':'Usage Reports',
                 'width':550,
                 'height':550,
                 pieSliceText: 'value',
                 titleTextStyle: {color: 'black', fontName: 'Arial', fontSize: '18', fontWidth: 'normal'},};
		var chart = new google.visualization.PieChart(document.getElementById('pieChart'));
		 function selectHandler() {
			 
			 var selectedItem = chart.getSelection()[0];
			
		       if (selectedItem) 
		       {
		    	 var criteriaType = data.getValue(selectedItem.row, 0);
		         var topping = data.getValue(selectedItem.row, 2); // to get the third arg from the passed array
		       //  var topping = data.getValue(selectedItem.row, 1); // to get the second arg from the passed array
		      
		        // generateDataForBarChart(criteriaId, topping);
		         generateDataForBarChart(criteriaId, topping, 1, criteriaType);
		         generateDataForBarChart(criteriaId, topping, 2, criteriaType);
		       }
		     }
		google.visualization.events.addListener(chart, 'select', selectHandler);
		chart.draw(data, options);   
		dataArray =[];
    }
    
   /*  function generateDataForBarChart(criteriaId, id) {
    	generateDataForBarChartFirst(criteriaId, id);
    	generateDataForBarChartSecond(criteriaId, id);
    }
     */
    function generateDataForBarChart(criteriaId, id, pos,criteriaType ) {
    	
 	   dataArray =[];
 	   var data = {
 				 'criteriaId' : criteriaId,
 				 'id' : id,
 				 'fromDate' : $('#fromDateReport').val(),
 				 'toDate' : $('#toDateReport').val(),
 				 'position' : pos
 			      };  
 	  
 	   var request = $.ajax({  url: 
 			"GenerateBarChart.ftl",
 			type: "POST", 
 			data: data,
 			cache: false,
 			dataType: "json"}); 
 	   
 	   request.done(function( msg ) {
 			var Outmsg= JSON.stringify(msg);
 			Outmsg = JSON.parse(Outmsg);
 			var dataItem = [];
 			 $.each(Outmsg,function(key, val) {
 				 dataItem = [key, parseInt(val),String(val) ];
 				 dataArray.push(dataItem);
 	    	 });
 			 drawBarChart(pos, criteriaType);
 				
 	    	 });
 			// drawChart();
    }
       
   function generateDataOnLoad(criteriaId, divName, title){
	   $('#film td').hide();

		dataArray =[];
		 $('#pieChart').html("");
		 $('#barChart').html("");
		 var dataToSend = {
				 'criteriaId' : criteriaId,
				 'fromDate' : $('#fromDateReport').val(),
				 'toDate' : $('#toDateReport').val()
			      };  
	
	/* 	var dataToSend = $('#usageReportsFormId').serialize();
			      alert(dataToSend); */
		var request = $.ajax({  url: 
			"GeneratePieChart.ftl",
			type: "POST", 
			data: dataToSend,
			cache: false,
			dataType: "json"}); 
			request.done(function( msg ) {
				var Outmsg= JSON.stringify(msg);
				Outmsg = JSON.parse(Outmsg);
				 var dataItem = ['Category', 'Value', 'Id'];
				 dataArray.push(dataItem);
				 $.each(Outmsg,function(key, val) {
					 var str = key.split(':');
					 dataItem = [str[1], parseInt(val), str[0]];
					 dataArray.push(dataItem);
					
		    	 });
				 drawChartLoad(divName, title);
				
		 }); 
		 	request.fail(
		  	function( jqXHR, textStatus){
		  		disableLoader();
		  	}); 
	}
   
     
   
	function generateData(){
		
		if($('#criteriaId').val() > 0){
		dataArray =[];
		 $('#pieChart').html("");
		 $('#barChart1').html("");
		 $('#barChart2').html("");
		criteriaId = $('#criteriaId').val();
		 var dataToSend = {
				 'criteriaId' : $('#criteriaId').val(),
				 'fromDate' : $('#fromDateReport').val(),
				 'toDate' : $('#toDateReport').val()
			      };  
	
	/* 	var dataToSend = $('#usageReportsFormId').serialize();
			      alert(dataToSend); */
		var request = $.ajax({  url: 
			"GeneratePieChart.ftl",
			type: "POST", 
			data: dataToSend,
			cache: false,
			dataType: "json"}); 
			request.done(function( msg ) {
				var Outmsg= JSON.stringify(msg);
				Outmsg = JSON.parse(Outmsg);
				 var dataItem = ['Category', 'Value', 'Id'];
				 dataArray.push(dataItem);
				 $.each(Outmsg,function(key, val) {
					 var str = key.split(':');
					 dataItem = [str[1], parseInt(val), str[0]];
					 dataArray.push(dataItem);
					
		    	 });
				 drawChart();
				
		 }); 
		 	request.fail(
		  	function( jqXHR, textStatus){
		  		disableLoader();
		  	}); 
		} else {
			alert("Please select the report criteria");
		}
	}
</script>
<section class="drop mtlg">
					<div class="cm">
						<table cellpadding='0' cellspacing='0' border='0' width='100%'>
							<tr>
								<td class="hd">
									<h6>Usage Reports</h6>
								</td>
							</tr>
							<tr class="bd">
								<td>
										<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This charts shows the Mint Usage reports based on <b>Group, Environment and Functionalities.</b>
				Please select the criterias and submit to view the report chart in detail.
				
					<form modelAttribute="usageReportForm"
						action="/GenerateUsageReports.ftl" id="usageReportsFormId"
						name="usageReportsFormId" target="_top" method="POST"
						onSubmit="return validateAndsave();">
						<input type="hidden" id="tab" name="tab"
							value="${reportForm.tab!}">
						<div style="margin-left:25px">
						<table>
						</br>
										
							<tr class="lblFieldPair">
								<td class="lbl">&nbsp;&nbsp;&nbsp;&nbsp;Report Criteria</td> 
								<td class="input">&nbsp;&nbsp;<select id="criteriaId" name="criteriaId">
									<option value="0">----------Select----------</option> 
										<option value="1">Group</option>
										<option value="2">Environment</option>
										<option value="3">Functionality</option>
								</select><font color="red" size=-1>&nbsp;*</font></td>
								<td>&nbsp;&nbsp;&nbsp;</td>
								<td class="lbl" >From Date</td>
								<td class="input">
														<div id="recurrencedatvalue">
															<input type="text" class="input_small" id="fromDateReport"
																name="fromDateReport" value="${reportForm.fromDate!}" >
															<script type="text/javascript">
																$('#fromDateReport').datetimepicker({
																	 format:'Y/m/d',
																	timepicker:false
																
																		});
															</script>
														</div>
													</td>
													<td class="lbl">To Date</td>
													<td class="input">
														<div id="recurrencedatvalue">
															<input type="text" class="input_small" id="toDateReport"
																name="toDateReport" value="${reportForm.toDate!}">
															<script type="text/javascript">
																$('#toDateReport').datetimepicker({
																	 format:'Y/m/d',
																	timepicker:false
																	
																	});
															</script>
														</div>
													</td>		
							
								
								</tr>
											
						</table>
						</div>
						<center>
							</td>
							</tr>
							<tr class="bd">
								<td class="btnBar">
									
								<a href="#" onClick="clearData()" class="btn"><span>Clear</span></a>
								<a href="#" onClick="generateData()" class="btn"><span>View Report</span></a></td>
							</tr>
						</table>
					</div>
				</section>

<section class="drop mtlg">
	<div class="cm">
		<table cellpadding='0' cellspacing='0' border='0' width='100%'>
		
			<tr class="bd">
				<td>
				<br>
				
						<center>
					
							<table>
								<tr>
							
								<td id="pieChart">
								 </td>
								</tr>
								</table>
								<table cellspacing="200px">
								<tr>
								  <td id="barChart1">
								  </td>
								  <td id="barChart2">
								  </td>
								  </tr>
							</table>
							<table id="overallChart1">
								  <tr>
								  <td id="groupDiv">
								  </td> 
								  
								 <td id="envDiv">
								 </td>
								 </tr>
								</table >
								<table id="overallChart2">
								 <tr>
								 <td id="funcDiv">
								 </td>
								  </tr>
							  </table>
							 
						    </center>
					
				</td>
			</tr>
		</table>
	</div>
</section>