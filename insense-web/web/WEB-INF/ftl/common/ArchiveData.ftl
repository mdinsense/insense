<#import "../common/spring.ftl" as spring />
<#import "../common/MacroTemplates.ftl" as mint />
<div id="Ui Testing Setup">
   <#include "../common/Header.ftl">
</div>
<head>
<link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css" />
<script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
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
	var isLastSearchResult = 1; 
	$(document).ready(function() {
		
		if(document.getElementById("key").value == "" ){
			 $("#success").hide();
			 $("#fail").hide();
		} else {
			if(document.getElementById("key").value == "success") {
				 $("#success").show();
				 $("#fail").hide();
			 } else {
				 $("#success").hide();
				 $("#fail").show();
			 }
		}
		setSelectionAll("environmentCategoryId",1);
		<#if searchResult?exists && searchResult>
			$(".multiSelection").find("option").attr("selected", false);
			setLastSearchCriteria();
		</#if>
		getUsersList();
		getFunctionality();
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
	
	function selectThis(id) {
		$("#archiveDataSelectAll").prop("checked",false);
		if($("#"+id).prop("checked")) {
			$("#"+id).prop("checked",true);
		} else {
			$("#"+id).prop("checked",false);
		}
	}
	
	function selectAllCheckBoxes() {
		if(document.getElementById("archiveDataSelectAll").checked) {
			$(".archiveDataCheckBox").prop("checked",true);
		} else {
			$(".archiveDataCheckBox").prop("checked",false);
		}
	}
	
	function getReport() {
		enableLoader();
		document.getElementById("key").value = "";
		document.getElementById("usageReportsFormId").setAttribute('action',"<@spring.url '/ArchiveGetReportList.ftl' />");
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
	

	function runSelected(){
		var schedules = [];
		var canRun = 0;
		$('.archiveDataCheckBoxChild').each(function() {
			if ($(this).prop("checked")) {
				schedules.push($(this).attr('id'));
				canRun = 1;
			}
		});
		var data =  schedules + "";
		if(canRun == 0) {
			alert("There are no schedules selected to run");
		} else {
			var dataToSend = {'schedules' : data}; 
			var request = $.ajax({  
				url: "ArchiveSelectedSchedules.ftl",
				type: "POST", 
				cache: false,
				data: dataToSend,
				dataType: "json"
			}); 
			request.done(function( msg ) {
				var Outmsg= JSON.stringify(msg);
				Outmsg = JSON.parse(Outmsg);
		      $.each(Outmsg,function(key, val) {
		    	  $("#ajaxSpinnerContainer").show();
		    	 if( key == "success"){
		    		 $("#success").show();
		    		 $("#fail").hide();
		    	 } else {
		    		 $("#success").hide();
		    		 $("#fail").show();
		    	 }
		    	 
		    	 refreshPage(key);
		     });
		      
		     
			});
			request.fail(function( jqXHR, textStatus ){  
				$("#ajaxSpinnerContainer").show();
				
			}); 
		}
	}
	function refreshPage(key) {
		enableLoader();
		document.getElementById("key").value = key;
		document.getElementById("usageReportsFormId").setAttribute('action',"<@spring.url '/ArchiveGetReportList.ftl' />");
		document.getElementById("usageReportsFormId").submit();
	
	}

	 function clearData() {
		   document.getElementById("usageReportsFormId").reset();
		   $("#environmentCategoryId").get(0).selectedIndex = 0;
	   }
</script>
<div style="width:80%" class="content twoCol">
<div class="page-content" id="pagecontent" role="main" tabindex="-1">
<SCRIPT>
alert(key);
</SCRIPT>

		<div id ="success">
			<div class="infoModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>Selected data has been archived successfully
				</div>
			</div>
		</div>	
		
		<div id ="fail">	
			<div class="alertModule visible" id="tcId2" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>Data archive has been failed to complete.
				</div>
			</div>
		</div>	
		<section class="drop mtlg">
					<div class="cm">
						<table cellpadding='0' cellspacing='0' border='0' width='100%'>
							<tr>
								<td class="hd">
									<h3>Archive Data</h3>
								</td>
							</tr>
							<tr class="bd">
								<td>
										<form modelAttribute="UsageReportsForm"	action="/GenerateUsageReports.ftl" id="usageReportsFormId" name="usageReportsFormId" target="_top" method="POST" onSubmit="return validateAndsave();">
							 			<input type="hidden" id="tab"  name="tab" value="${reportForm.tab!}"> 
							 			<input type="hidden" id="key"  name="key" value="${reportForm.key!}"> 
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
														<select class="multiSelection" id="solutionType" onchange="setSelectionAll(this.id,this.value);getFunctionality();" name="solutionType">
															<!-- <option value="0" selected="true">---------------Select---------------</option>  -->
															<#list solutionTypes! as solutionType>
																<option value="${solutionType.solutionTypeId!}">${solutionType.solutionTypeName!}</option>
															</#list>
														</select>
													</td>
													<td class="lbl">Functionality</td>
													<td class="input">
														<select multiple class="multiSelection" id="functionality" onchange="setSelectionAll(this.id,this.value);" name="functionality">
														<!-- 	<option value="0" selected="true">ALL</option> -->
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
								<a href="#" onClick="getReport()" class="btn"><span>Submit</span></a></td>
							</tr>
						</table>
					</div>
				</section>
	<section>
					<div style="width: 100%;" class="content" tabindex="0" style="display: block;" id="usageReports_table_content">
						<div class="bd statusTables" id="completed_div">
							<div id="onlyactive" style="overflow: auto; overflow-y: hidden; padding-bottom: 10px;">
								<table id="archivedata_table" class="styleA fullwidth sfhtTable drop2" style="margin-top: 10px; margin-bottom: 10px;" summary="">
									<caption></caption>
									<thead>
										<tr>
											<th class="txtl header w5" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype1" tabindex="0" data-sortpath="none">Schedule Execution Id</th>
											<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype2" tabindex="0" data-sortpath="none">Start Date</th>
											<th class="txtl header w10"
												scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype3" tabindex="0" data-sortpath="none">End
												Date</th>
											<th class="txtl header w10"
												scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype4" tabindex="0" data-sortpath="none">Scheduled By</th>
											<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype5" tabindex="0" data-sortpath="none">Group Name</th>
											<th class="txtl header w10" scope="col"> Select <input style='width: 10px;' type='checkbox' class="archiveDataCheckBox" id="archiveDataSelectAll" onClick='selectAllCheckBoxes()'></th>
										</tr>
									</thead>
									<tbody>
										<#if archiveDataList?exists && archiveDataList?has_content>
										<#list archiveDataList! as archiveData>
										    <script>$("#downloadIcon").show();</script>
											<tr data-sortfiltered="false">
												<td scope="row" data-sortvalue="${archiveData.scheduleExecutionId!}">${archiveData.scheduleExecutionId!}</td>
												<td scope="row" data-sortvalue="${archiveData.startDate!}">${archiveData.startDate!}</td>
												<td scope="row" data-sortvalue="${archiveData.endDate!}">${archiveData.endDate!}</td>
												<td scope="row" data-sortvalue="${archiveData.scheduledBy!}">${archiveData.scheduledBy!}</td>
												<td scope="row" data-sortvalue="${archiveData.groupName!}">${archiveData.groupName!}</td>
												<td><input type="checkbox" class="archiveDataCheckBox archiveDataCheckBoxChild"  id="${archiveData.scheduleExecutionId!}" onClick='selectThis(this.id)'></td>
											</tr>
										</#list>
										<#else>
												<tr><td colspan='4'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
										</#if>
										
										
									</tbody>
								</table>
							</div>
							<@mint.tablesort id="#archivedata_table" casesensitive="false" jsvar="archivedata_table__js"/>
							<@mint.paginate table="#archivedata_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
						</div>
						
					</div>
						<table class="styleA fullwidth sfhtTable drop2" style="margin-top: 10px; margin-bottom: 10px;" summary="">
							<tr class="bd">
								<td class="btnBar">
								<a href="#" onClick="runSelected()" class="btn"><span>Archive</span></a></td>
							</tr>
						</table>
				</section>
				</div>
				</div>
				<#include "../common/footer.ftl">
</body>
</html>