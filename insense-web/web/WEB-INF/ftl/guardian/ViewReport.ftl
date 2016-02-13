<link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css"/>
<script src="js/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript" src="js/formValidation.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
<style type="text/css">
.dialogdiv {
  height:90%;
  width:32%;
  overflow:auto;
  display:inline-block;
  margin-left:1%;
}
</style>
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#inprogress_div").hide();
				$("#exceptionSummary").hide();
				$("#signatureDetails").hide();
				$("#solutionDetails").hide();
				
			});
	function validateAndsave() {
		
	}
	function deleteSignature(signatureId) {
		
	}
	function checkMe(elem) {
		$("#inprogress_div").hide();
		$(".rowValue").val("");
		if($("#"+elem).prop('checked')) {
			$(".gReportChk").prop('checked',false);
			$(".rowValue").prop('disabled',true);
			$("#"+elem).prop('checked',true);
			$("#criticality"+elem).attr('disabled',false);
			$("#tier"+elem).attr('disabled',false);
			$("#fromdate"+elem).attr('disabled',false);
			$("#todate"+elem).attr('disabled',false);
			showReportTable()
		} else {
			$(".gReportChk").prop('checked',false);
			$(".rowValue").prop('disabled',true);
		}
	}
	function showReportTable(id) {
		var heading = $("#channel"+id).html() + " " + $("#application"+id).html() + " " + $("#environment"+id).html() + " " + $("#criticality"+id).val() + " For " + $("#tier"+id).val() + " from " + $("#fromdate"+id).val() + " To " + $("#todate"+id).val();
		$("#text2").html("");
		if($("#criticality"+id).val() && $("#tier"+id).val() && $("#fromdate"+id).val() && $("#todate"+id).val()) {
			$("#text2").html(heading);
			$("#inprogress_div").show();
		}
	}
	function showExceptionDialog() {
		//This code to make it 100% X 70% of window size
		 var wWidth = $(window).width();
	     var dWidth = wWidth * .4;
	     var wHeight = $(window).height();
	     var dHeight = wHeight * 0.7;
	     
		$("#exceptionSummary").dialog({
			width: dWidth,
		    minHeight: "auto",
		    maxHeight: dHeight,
		    show: "slide",
			title : 'Reports summary',
			resizable : false,
			position : [ 'center', 'top' ],
			create: function (event) { $(event.target).parent().css('position', 'fixed');}
		});
		$("#exceptionSummary").show();
	}
	function showSignatureDialog() {
		//This code to make it 100% X 70% of window size
		 var wWidth = $(window).width();
	     var dWidth = wWidth * .4;
	     var wHeight = $(window).height();
	     var dHeight = wHeight * 0.7;
	     
		$("#signatureDetails").dialog({
			width: dWidth,
		    minHeight: "auto",
		    maxHeight: dHeight,
		    show: "slide",
			title : 'Reports summary',
			resizable : false,
			position : [ 'center', 'top' ],
			create: function (event) { $(event.target).parent().css('position', 'fixed');}
		});
		
		$("#signatureDetails").show();
		window.scrollTo(0, 1000);
	}
	function showSolutionDialog() {
		//This code to make it 100% X 70% of window size
		 var wWidth = $(window).width();
	     var dWidth = wWidth * .4;
	     var wHeight = $(window).height();
	     var dHeight = wHeight * 0.7;
	     
		$("#solutionDetails").dialog({
			width: dWidth,
		    minHeight: "auto",
		    maxHeight: dHeight,
		    show: "slide",
			title : 'Reports summary',
			resizable : false,
			position : [ 'center', 'top' ],
			create: function (event) { $(event.target).parent().css('position', 'fixed');}
		});
		
		$("#solutionDetails").show();
		window.scrollTo(0, 1000);
	}
</script>
<section class="drop mtlg">
	<div class="cm">
		<table cellpadding='0' cellspacing='0' width="100%;">
			<tr>
				<td class="hd">
					<h3>Reports</h3>
				</td>
			</tr>
			<tr class="bd">
				<td style="padding: 5">
					<table class="bd rowheight35">
						<tr>
							<td colspan="3">
								<div style="width: 100%;" class="content" tabindex="0"
									style="display: block;" id="completed_table_content">
									<div class="bd statusTables" id="completed_div">
										<div id="onlyactive"
											style="overflow: auto; overflow-y: hidden; padding-bottom: 10px;">
											<span id="text1" class="statusTables"
												style="font-weight: bolder; margin: 10px">Guardian
												Reports</span>
											<table id="completed_table"
												class="styleA fullwidth sfhtTable"
												style="margin-top: 10px; margin-bottom: 10px;" summary="">
												<caption></caption>
												<thead>
													<tr>
														<th class="txtl header w4" scope="col" tabindex="0"
															data-sortpath="none">Select</th>
														<th class="txtl header w9" scope="col" tabindex="0"
															data-sortpath="none">Channel Name</th>
														<th class="txtl header w11" scope="col" tabindex="0"
															data-sortpath="none">Application Name</th>
														<th class="txtl header w12" scope="col" tabindex="0"
															data-sortpath="none">Environment</th>
														<th class="txtl header w10" scope="col" tabindex="0"
															data-sortpath="none">Criticality</th>
														<th class="txtl header w10" scope="col" tabindex="0"
															data-sortpath="none">Tier</th>
														<th class="txtl header w10" scope="col" tabindex="0"
															data-sortpath="none">From Date</th>
														<th class="txtl header w10" scope="col" tabindex="0"
															data-sortpath="none">To Date</th>
													</tr>
												</thead>
												<tbody>
												<#list guardianReportList! as report>
													<tr>
														<td class="row"><input class="gReportChk" type="checkbox"
															id="${report.reprotId}" onClick="checkMe(this.id)"></td>
														<td id="channel${report.reprotId}" scope="row" data-sortvalue="${report.channel!}">${report.channel!}</td>
														<td id="application${report.reprotId}" scope="row" data-sortvalue="${report.application!}">${report.application!}</td>
														<td id="environment${report.reprotId}" scope="row" data-sortvalue="${report.environment!}">${report.environment!}</td>
														<td class="row">
															<select multiple id="criticality${report.reprotId}" class="width100 rowValue" name="criticality" onchange="showReportTable('${report.reprotId}')" disabled>
																	<option value="INFO">INFO</option>
																	<option value="WARNING">WARNING</option>
																	<option value="ERROR">ERROR</option>
																	<option value="FATAL">FATAL</option>
															</select>
														</td>
														<td class="row">
															<select multiple id="tier${report.reprotId}" class="width100 rowValue" name="tier" onchange="showReportTable('${report.reprotId}')" disabled>
																	<option value="Tier-1">Tier-1</option>
																	<option value="Tier-2">Tier-2</option>
																	<option value="Tier-3">Tier-3</option>
															</select>
														</td>
														<td class="row"><div id="dateVal1">
																<input type="text" class="input_small rowValue" id="fromdate${report.reprotId}"
																	name="fromdate${report.reprotId}" value="" onchange="showReportTable('${report.reprotId}')" disabled>
																<script type="text/javascript">
																	$('#fromdate${report.reprotId}').datetimepicker(
																					{
																		timepicker : false
																	});
																</script>
															</div>
														</td>
														<td scope="row"">
														<div id="dateval2">
																<input type="text" class="input_small rowValue" id="todate${report.reprotId}"
																	name="todate${report.reprotId}" value="" onchange="showReportTable('${report.reprotId}')" disabled>
																<script type="text/javascript">
																	$(
																			'#todate${report.reprotId}')
																			.datetimepicker(
																					{
																						timepicker : false
																					});
																</script>
															</div>
														</td>
													</tr>
													</#list>
												</tbody>
											</table>
										</div>
									</div>
								</div>
								<div class="bd statusTables" id="inprogress_div">
									<div id="onlyactive"
										style="overflow: auto; overflow-y: hidden; padding-bottom: 10px;">
										<span id="text2" class="statusTables"
											style="font-weight: bolder; margin: 10px"></span>
										<table id="inprogress_table"
											class="styleA fullwidth sfhtTable"
											style="margin-top: 10px; margin-bottom: 10px;" summary="">
											<br />
											<caption></caption>
											<thead>
												<tr>
													<th id="scheduleIdRow" class="txtl header w5" scope="col"
														tabindex="0" data-sortpath="none">Date</th>
													<th id="startDateTimeRow" class="txtl header w10"
														scope="col" tabindex="0" data-sortpath="none">No Of
														Times Occured</th>
													<th id="lastUpdatedtimeRow" class="txtl header w10"
														scope="col" tabindex="0" data-sortpath="none">Exception
														Details (Splunk)</th>
													<th id="processedUrlRow" class="txtl header w10"
														scope="col" tabindex="0" data-sortpath="none">Signature</th>
													<th id="pendingUrlCountRow" class="txtl header w10"
														scope="col" tabindex="0" data-sortpath="none">Solution</th>
													<th id="reportsStartTimeRow" class="txtl header w10"
														scope="col" tabindex="0" data-sortpath="none">Provided
														By</th>
													<th id="reportsEndTimeRow" class="txtl header w10"
														scope="col" tabindex="0" data-sortpath="none">Date</th>
												</tr>
											</thead>
											<tbody>
												<tr>
														<td scope="row"">
															2015/09/21
														</td>
														<td scope="row"">
															15
														</td>
														<td scope="row"">
															<a href="#" class="editLink np" title="Edit the application" onClick="showExceptionDialog()">
															<span class="icon nmt plxs"></span>View Exception</a>
														</td>
														<td scope="row"">
															<a href="#" class="editLink np" title="Edit the application" onClick="showSignatureDialog()">
															<span class="icon nmt plxs"></span>View Signature</a>
														</td>
														<td scope="row"">
															<a href="#" class="editLink np" title="Edit the application" onClick="showSolutionDialog()">
															<span class="icon nmt plxs"></span>view Solution</a>
														</td>
														<td scope="row"">
															Admin
														</td>
														<td scope="row"">
															2015/09/28
														</td>
													</tr>
													<tr>
														<td scope="row"">
															2015/06/28
														</td>
														<td scope="row"">
															30
														</td>
														<td scope="row"">
															<a href="#" class="editLink np" title="Edit the application" onClick="showExceptionDialog()">
															<span class="icon nmt plxs"></span>View Exception</a>
														</td>
														<td scope="row"">
															<a href="#" class="editLink np" title="Edit the application" onClick="showSignatureDialog()">
															<span class="icon nmt plxs"></span>View Signature</a>
														</td>
														<td scope="row"">
															<a href="#" class="editLink np" title="Edit the application" onClick="showSolutionDialog()">
															<span class="icon nmt plxs"></span>view Solution</a>
														</td>
														<td scope="row"">
															Prodsupport
														</td>
														<td scope="row"">
															2015/07/28
														</td>
													</tr>
											</tbody>
										</table>
									</div>
								</div>
							</td>
						</tr
					</table>
					</div>
					</section>
	<center>				
	<div id="exceptionSummary" class="dialogdiv" title="Exception summary">
		<p>20:08:12.738 ["http-bio-8080"-exec-3] ERROR org.hibernate.engine.jdbc.spi.SqlExceptionHelper - Table 'mintdbv3.applicationmodulexref' doesn't exist
2015-09-01 20:08:12 ERROR ApplicationModuleDAOImpl:36 - org.hibernate.exception.SQLGrammarException: could not extract ResultSet20:08:12.738 ["http-bio-8080"-exec-3] ERROR org.hibernate.engine.jdbc.spi.SqlExceptionHelper - Table 'mintdbv3.applicationmodulexref' doesn't exist
2015-09-01 20:08:12 ERROR ApplicationModuleDAOImpl:36 - org.hibernate.exception.SQLGrammarException: could not extract ResultSet20:08:12.738 ["http-bio-8080"-exec-3] ERROR org.hibernate.engine.jdbc.spi.SqlExceptionHelper - Table 'mintdbv3.applicationmodulexref' doesn't exist
2015-09-01 20:08:12 ERROR ApplicationModuleDAOImpl:36 - org.hibernate.exception.SQLGrammarException: could not extract ResultSet</p>
	</div>
	<div id="signatureDetails" class="dialogdiv" title="Signature summary">
		<p>Internal Server Error</p>
	</div>
	<div id="solutionDetails" class="dialogdiv" title="Solution summary">
	<p>Link is passing invalid argument. Anchor tag is changed with correct argument value.</p>
	</div>
	</center>