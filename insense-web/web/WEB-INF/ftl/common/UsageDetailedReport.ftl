
<script type="text/javascript">
	var isLastSearchResult = 1; 
	$(document).ready(function() {
		setSelectionAll("environmentCategoryId",1);
		<#if searchResult?exists && searchResult>
			$(".multiSelection").find("option").attr("selected", false);
			setLastSearchCriteria();
		</#if>
		getUsersList();
		 $(".tabLink").each(function(){
		      $(this).click(function(){
		        tabeId = $(this).attr('id');
		        $(".tabLink").removeClass("activeLink");
		        $(this).addClass("activeLink"); 
		        return false;
		      });
		});
		$(".tabLink").removeClass("activeLink");
		$("#cont-${reportForm.tab}").addClass("activeLink");
	});
	function setLastSearchCriteria() {
		<#list reportForm.groupName! as groupid>
			   $("#groupName option").each(function(){ 
				   if ($(this).val() == ${groupid}) { 
					 	 $(this).attr("selected",true); 
					 } 
				 });
		</#list>
		<#list reportForm.solutionType! as groupid>
		   $("#solutionType option").each(function(){ 
			   if ($(this).val() == ${groupid}) { 
				 	 $(this).attr("selected",true); 
				 } 
			 }); 
		</#list>
		<#list reportForm.environmentCategoryId! as groupid>
			   $("#environmentCategoryId option").each(function(){ 
				   if ($(this).val() == ${groupid}) { 
					 	 $(this).attr("selected",true); 
					 } 
				 }); 
		</#list>
		<#list reportForm.functionality! as groupid>
		   $("#functionality option").each(function(){ 
			   if ($(this).val() == ${groupid}) { 
				 	 $(this).attr("selected",true); 
				 } 
			 }); 
		</#list>
	}
	function setSelectionAll(target,selected) {
		var allValue = 0;
		if(target == "environmentCategoryId") {
			allValue = 1;
		}
		if(selected == allValue) {
			$("#"+target+" option:selected").removeAttr("selected");
			$("#"+target).val(allValue);
		}
	}
	function getReport() {
		enableLoader();
		document.getElementById("usageReportsFormId").setAttribute('action',"<@spring.url '/GetReportList.ftl' />");
		document.getElementById("usageReportsFormId").submit();
	}
	
	function getUsersList() {
		document.getElementById("users").options.length=0;
		var groups = $("#groupName").val();
		loadUsersList(groups,"users");
	}
	
	function loadUsersList(groupArr,users){
		var isSelected = true;
		// replaces the groupNames as selected group from last search criteria
		<#if reportForm.groupName?? && reportForm.groupName?has_content>
		 	if(isLastSearchResult == 1) {	
		 		groupArr = "";
		 		document.getElementById("users").options.length=0;
				<#list reportForm.groupName! as groupid>
				  groupArr += "${groupid},";
				</#list>
				groupArr = removeLastComma(groupArr);
				$("#users").find("option").attr("selected", false);
				isSelected = false;
		 	}
		</#if>
		 var dataToSend = {
				 'groupName' : ""+groupArr
			      }; 
		
		var request = $.ajax({  
			url: "GetUsersDetailForUsageReports.ftl",
			type: "POST", 
			cache: false,
			data: dataToSend,
			dataType: "json"
		}); 

		request.done(function( msg ) {	
			var Outmsg= JSON.stringify(msg);
			Outmsg = JSON.parse(Outmsg);
			$('<option />', {value: 0, text: "ALL",selected: isSelected}).appendTo("#"+users);
			$.each(Outmsg,function(key, val) {
				 $('<option />', {value: key, text: val}).appendTo("#"+users);
			});
			 if(isLastSearchResult == 1) {
			<#list reportForm.users! as groupid>
			   $("#users option").each(function(){ 
				   if ($(this).val() == ${groupid}) { 
					 	 $(this).attr("selected",true); 
					 } 
				 }); 
			</#list>
			 isLastSearchResult = 0;
			 }
		});
		
		request.fail(function( jqXHR, textStatus ){  
		}); 
		
	}
	function getFunctionality() {
		document.getElementById("functionality").options.length=0;
		var sol = $("#solutionType").val();
		getFunctionalityForSolutionType(sol);
	}
	function removeLastComma(strng){        
	    var n=strng.lastIndexOf(",");
	    var a=strng.substring(0,n) 
	    return a;
	}
	function getFunctionalityForSolutionType(solutionTypeId){
		enableLoader();
		var dataToSend = {
				 'solutionTypeId' : ""+solutionTypeId
			      }; 
		var request = $.ajax({  url: 
			"GetFunctionalityForSolutionType.ftl",
			type: "POST", 
			data: dataToSend,
			cache: false,
			dataType: "text"});
			request.done(function( functionalityList ) {
			disableLoader();
			if ($.parseJSON(functionalityList) != ''){
				 $('<option />', {value: 0, text: "ALL",selected: true}).appendTo("#functionality");
	  			$.each($.parseJSON(functionalityList), function(idx, functionality) {
	  				 $('<option />', {value: functionality.id, text: functionality.name}).appendTo("#functionality");
	 			});
			}
			}); 
		 	request.fail(
		  	function( jqXHR, textStatus){
		  		disableLoader();
		  	}); 
	}
	function downloadReport() {
		document.getElementById("usageReportsFormId").setAttribute('action',"<@spring.url '/DownloadUsageReport.ftl' />");
		document.getElementById("usageReportsFormId").submit();
	}
	

</script>
				<section class="drop mtlg">
					<div class="cm">
						<table cellpadding='0' cellspacing='0' border='0' width='100%'>
							<tr>
								<td class="hd">
									<h3>Usage Reports</h3>
								</td>
							</tr>
							<tr class="bd">
								<td>
										<form modelAttribute="UsageReportsForm"	action="/GenerateUsageReports.ftl" id="usageReportsFormId" name="usageReportsFormId" target="_top" method="POST" onSubmit="return validateAndsave();">
							 			<input type="hidden" id="tab"  name="tab" value="${reportForm.tab!}"> 
											<table><br>
												<tr class="lblFieldPair">
													<td class="lbl">&nbsp;&nbsp;Group Name</td>
													<td class="input">
													<select multiple id="groupName"	class="multiSelection" name="groupName" onchange="setSelectionAll(this.id,this.value);getUsersList();">
															<option value="0" selected="true">ALL</option> 
															<#list groupsDetails! as group>
																<option value="${group.groupId!}">${group.groupName!}</option>
															</#list>
													</select>
													</td>
													<td class="lbl">Users</td>
													<td class="input"><select multiple id="users" class="multiSelection" onchange="setSelectionAll(this.id,this.value);" name="users"></select></td>
													<td class="lbl">Solution Type</td>
													<td class="input">
														<select multiple class="multiSelection" id="solutionType" onchange="setSelectionAll(this.id,this.value);getFunctionality();" name="solutionType">
															<option value="0" selected="true">ALL</option>
															<#list solutionTypes! as solutionType>
																<option value="${solutionType.solutionTypeId!}">${solutionType.solutionTypeName!}</option>
															</#list>
														</select>
													</td>
													<td class="lbl">Functionality</td>
													<td class="input">
														<select multiple class="multiSelection" id="functionality" onchange="setSelectionAll(this.id,this.value);" name="functionality">
															<option value="0" selected="true">ALL</option>
														 	<#list functionalities! as functionality>
																<option value="${functionality.functionalityTypeId!}">${functionality.functionalityName!}</option>
															</#list>
														</select>
													</td>
												</tr>
												<tr></tr>
												<tr class="lblFieldPair">
													<td class="lbl">&nbsp;&nbsp;Environment</td>
													<td class="input">
													<select multiple class="multiSelection" onchange="setSelectionAll(this.id,this.value);" id="environmentCategoryId" name="environmentCategoryId">
														 	<#list environmentList! as environmentCategory>
																<option value="${environmentCategory.environmentCategoryId!}">${environmentCategory.environmentCategoryName!}</option>
															</#list>
													</select>
													</td>
													<td class="lbl">From Date</td>
													<td class="input">
														<div id="recurrencedatvalue">
															<input type="text" class="input_small" id="fromDate"
																name="fromDate" value="${reportForm.fromDate!}">
															<script type="text/javascript">
																$('#fromDate').datetimepicker({
																	 format:'Y/m/d',
																		timepicker:false
																		});
															</script>
														</div>
													</td>
													<td class="lbl">To Date</td>
													<td class="input">
														<div id="recurrencedatvalue">
															<input type="text" class="input_small" id="toDate"
																name="toDate" value="${reportForm.toDate!}">
															<script type="text/javascript">
																$('#toDate').datetimepicker({
																	 format:'Y/m/d',
																		timepicker:false
																	});
															</script>
														</div>
													</td>
													<td></td>
													<td></td>
												</tr>
											</table>
										</form>
								</td>
							</tr>
							<tr class="bd">
								<td class="btnBar">
								<!-- <a href="#" onClick="downloadReport()" class="btn disabled"><span>Download Report</span></a> -->
								<a href="#" onClick="clearData()" class="btn"><span>Clear</span></a>
								<a href="#" onClick="getReport()" class="btn"><span>View Report</span></a></td>
							</tr>
						</table>
					</div>
				</section>
	<section>
					<div style="width: 100%;" class="content" tabindex="0" style="display: block;" id="usageReports_table_content">
						<div class="bd statusTables" id="completed_div">
							<div id="onlyactive" style="overflow: auto; overflow-y: hidden; padding-bottom: 10px;">
								<table id="usageReports_table" class="styleA fullwidth sfhtTable drop2" style="margin-top: 10px; margin-bottom: 10px;" summary="">
									<caption></caption>
									<thead>
										<tr>
											<th class="txtl header w5" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype1" tabindex="0" data-sortpath="none">S.No</th>
											<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype2" tabindex="0" data-sortpath="none">Application Name</th>
											<th class="txtl header w10"
												scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype3" tabindex="0" data-sortpath="none">Environment
												Name</th>
											<th class="txtl header w10"
												scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype4" tabindex="0" data-sortpath="none">MINT User
												id</th>
											<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype5" tabindex="0" data-sortpath="none">Group Name</th>
											<th id="reportsStartTimeRow" class="txtl header w10"
												scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype6" tabindex="0" data-sortpath="none">Solution
												type</th>
											<th  class="txtl header w10"
												scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype7" tabindex="0" data-sortpath="none">Functionality
												Type</th>
											<th class="txtl header w10"
												scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype8" tabindex="0" data-sortpath="none">Run Start
												Date Time</th>
											<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype9" tabindex="0" data-sortpath="none">Run Complete Date
												Time</th>
											<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype10" tabindex="0" data-sortpath="none">Notes
												Time</th>
										</tr>
									</thead>
									<tbody>
										<#if reportList?exists && reportList?has_content>
										<#list reportList! as report>
										    <script>$("#downloadIcon").show();</script>
											<tr data-sortfiltered="false">
												<td scope="row" data-sortvalue="${report.slno!}">${report.slno!}</td>
												<td scope="row" data-sortvalue="${report.applicationName!}">${report.applicationName!}</td>
												<td scope="row" data-sortvalue="${report.environmentCategoryName!}">${report.environmentCategoryName!}</td>
												<td scope="row" data-sortvalue="${report.groupName!}">${report.groupName!}</td>
												<td scope="row" data-sortvalue="${report.userName!}">${report.userName!}</td>
												<td scope="row" data-sortvalue="${report.solutionTypeName!}">${report.solutionTypeName!}</td>
												<td scope="row" data-sortvalue="${report.functionalityName!}">${report.functionalityName!}</td>
												<td scope="row" data-sortvalue="${report.startDate!}">${report.startDate!}</td>
												<td scope="row" data-sortvalue="${report.endDate!}">${report.endDate!}</td>
												<td scope="row" data-sortvalue="${report.notes!}">${report.notes!}</td>
											</tr>
										</#list>
										<#else>
												<tr><td colspan='4'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
										</#if>
									</tbody>
								</table>
							</div>
							<@mint.tablesort id="#usageReports_table" casesensitive="false" jsvar="usageReports_table__js"/>
							<@mint.paginate table="#usageReports_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
						</div>
					</div>
				</section>