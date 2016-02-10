<#import "spring.ftl" as spring />
<#import "MacroTemplates.ftl" as mint />
<div id="Mint Home">
   <#include "Header.ftl">
</div>
<style type="text/css">
.tab-box {
	margin-top: 1.5%;
}
/*  .ui-widget-header  {
  background-image: -webkit-linear-gradient(#97CAE0, #E6EAED);
 } */
</style>
<script type="text/javascript">
$(document).ready(function() {
	if($('#Comparison_report_table tr').length < 2) {
		var innerHtml = "<tbody><tr><td colspan='11'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr></tbody>";
		$('#Comparison_report_table').append(innerHtml);
	}
});
function updateAction(scheduleAction, scheduleExecutionId) {
	var scheduleActionTemp = $('#'+scheduleAction).val();
	$('#scheduleAction').val(scheduleActionTemp);
	$("#scheduleExecutionId").val(scheduleExecutionId);
	document.getElementById("aform").setAttribute('action',"<@spring.url '/UiMonitorJob.ftl' />");
	document.getElementById("aform").setAttribute('method',"POST");
	document.getElementById("aform").submit();
}
</script>
</head>
<div style="width:80%;margin-top:18px;" class="content twoCol">
<div class="page-content" id="pagecontent" role="main" tabindex="-1">
		<hr style="margin-top: 10px;">
		<div class="mblg">
			<div class="mtxs">
		 		<a id="tcId100" class="tipLink rel" name="tcId100" >Help</a><b><@spring.message "mint.help.monitorJobs"/></b><a href="<@spring.message 'mint.help.mailTo'/>" target="_top">
				<@spring.message "mint.help.mailId"/></a>
			</div>
		</div>
		<hr style="margin-top: 0px;">
<body>
	<section class="drop mtlg">
		<div class="cm">
		<form modelAttribute="RegressionTestExecutionForm"
								id="aform" name="aform" target="_top"
								method="POST" onSubmit="">
		<input type="hidden" id="scheduleExecutionId" name="scheduleExecutionId" value="${regressionTestExecutionForm.scheduleExecutionId!}">
		<input type="hidden" id="scheduleAction"  name="scheduleAction" value="0">
			<table cellpadding='0' cellspacing='0' width="100%;">
				<tr>
					<td class="hd">
						<h3>Job Status</h3>
					</td>
				</tr>
				<tr class="bd">
					<td style="padding: 5">
						<table id="Comparison_report_table"
							class="styleA fullwidth sfhtTable" summary="">
							<caption></caption>
							<thead>
								<tr>
									<th class="txtl header w10" scope="col" data-sortfilter="true"
										data-sortdatatype="number" data-sort="true"
										data-sortname="ftype1" tabindex="0" data-sortpath="none">Schedule Id</th>
									<th class="txtl header w10" scope="col" data-sortfilter="true"
										data-sortdatatype="string" data-sort="true"
										data-sortname="ftype2" tabindex="0" data-sortpath="none">Status</th>
									<th class="txtl header w15" scope="col" data-sortfilter="true"
										data-sortdatatype="date" data-sort="true"
										data-sortname="ftype3" tabindex="0" data-sortpath="none">Start Time</th>
									<th class="txtl header w10" scope="col" data-sortfilter="true"
										data-sortdatatype="string" data-sort="true"
										data-sortname="ftype6" tabindex="0" data-sortpath="none">Application Name</th>
									<th class="txtl header w15" scope="col" data-sortfilter="true"
										data-sortdatatype="string" data-sort="true"
										data-sortname="ftype6" tabindex="0" data-sortpath="none">Environment Category</th>
									<th class="txtl header w10" scope="col" data-sortfilter="true"
										data-sortdatatype="number" data-sort="true"
										data-sortname="ftype4" tabindex="0" data-sortpath="none">Approximate End Time</th>
									<th class="txtl header w10" scope="col" data-sortfilter="true"
										data-sortdatatype="number" data-sort="true"
										data-sortname="ftype5" tabindex="0" data-sortpath="none">Schedule By</th>
									<th class="txtl header w10" scope="col" data-sortfilter="true"
										data-sortdatatype="string" data-sort="true"
										data-sortname="ftype6" tabindex="0" data-sortpath="none">Group</th>
									<th class="txtl header w5" scope="col" data-sortfilter="true"
										data-sortdatatype="string" data-sort="true"
										data-sortname="ftype7" tabindex="0" data-sortpath="none">Action</th>
									<th class="txtl header w5" scope="col" data-sortfilter="true"
										data-sortdatatype="string" data-sort="true"
										data-sortname="ftype8" tabindex="0" data-sortpath="none">Update Status</th>
								</tr>
							</thead>
							<tbody>
								<#list scheduleStatusList! as schedule>
									<tr>
										<td id="${schedule.scheduleExecutionId!}_td_scheduleExecutionId" scope="row" data-sortvalue="${schedule.scheduleExecutionId!}">
										${schedule.scheduleExecutionId!}</td>
										<td id="${schedule.executionStatus!}" scope="row" data-sortvalue="${schedule.executionStatus!}">
										${schedule.executionStatus!}</td>
										<td id="${schedule.startTime!}" scope="row" data-sortvalue="${schedule.startTime!}">
										${schedule.startTime!}</td>
										<td id="${schedule.applicationName!}" scope="row" data-sortvalue="${schedule.applicationName!}">
										${schedule.applicationName!}</td>
										<td id="${schedule.environmentCategory!}" scope="row" data-sortvalue="${schedule.environmentCategory!}">
										${schedule.environmentCategory!}</td>
										<td id="${schedule.runningTime!}" scope="row" data-sortvalue="${schedule.runningTime!}">
										${schedule.runningTime!}</td>
										<td id="${schedule.scheduledBy!}" scope="row" data-sortvalue="${schedule.scheduledBy!}">
										${schedule.scheduledBy!}</td>
										<td id="${schedule.group!}" scope="row" data-sortvalue="${schedule.group!}">
										${schedule.group!}</td>
										<#if isShowUpdateJobStatus>
										<td id="${schedule.scheduleExecutionId!}_td" scope="row" data-sortvalue="">
										<select id="${schedule.scheduleExecutionId!}_Select" name="${schedule.scheduleExecutionId!}_Select" >
											<option value="0">--Choose Action--</option>
											<#list schedule.action! as action>
												<option value="${action.statusCode!}">${action.action!}</option>
											</#list>
										</select>
									
										</td>
										<td scope="row" data-sortvalue="">
											<input type="button" value="Update Status" onClick="updateAction('${schedule.scheduleExecutionId!}_Select', '${schedule.scheduleExecutionId!}')">
										</td>
										<#else>
											<td id="${schedule.scheduleExecutionId!}_td" scope="row" data-sortvalue="">
											<select id="${schedule.scheduleExecutionId!}_Select" name="${schedule.scheduleExecutionId!}_Select">
												<option value="0">--Choose Action--</option>
												<#list schedule.action! as action>
													<option value="${action.statusCode!}">${action.action!}</option>
												</#list>
											</select>
											</td>
											<td scope="row" data-sortvalue="">
												<input type="button" class="disable" value="Update Status" onClick="updateAction('${schedule.scheduleExecutionId!}_Select', '${schedule.scheduleExecutionId!}')">
											</td>
										</#if>
									</tr>
								</#list>
							</tbody>
						</table>
					</td>
				</tr>
			</table>
			</form>
		</div>
	</section>
</body>
</div>
</div>
</html>
