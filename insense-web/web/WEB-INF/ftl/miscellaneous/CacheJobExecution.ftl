<#import "../common/spring.ftl" as spring />
<#import "../common/MacroTemplates.ftl" as mint />
<div id="Ui Testing Setup">
   <#include "../common/Header.ftl">
</div>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#scriptFile').hide();
	$("#scriptFileButton").click(function() {
		$('#scriptFile').click();
	});
});
function validateInputValues(){
	
		if($("#applicationName").val()== ""){
			alert('Appplication name should not be blank');
			return false;
		}
		if($("#environmentName").val()== ""){
			alert('Environment name should not be blank');
			return false;
		}
		
		if(document.aform.scriptFile.value == ""){
			alert('Please select script');
			document.aform.scriptFile.focus();
			return false;
		}
		$('#scriptFile').show();
		document.getElementById("CacheJobExecutionId").setAttribute('action',"<@spring.url '/SaveClearCacheDetails.ftl' />");
		document.getElementById("CacheJobExecutionId").submit();
}
function CopyMe(oFileInput, sTargetID) {
	var arrTemp = oFileInput.value.split('\\');
	document.aform.scriptPath.value = arrTemp[arrTemp.length - 1];
	document.aform.scriptPath.title = arrTemp[arrTemp.length - 1];
}
function selectAllCheckBoxes() {
	if(document.getElementById("clearCacheselectAll").checked) {
		$(".cacheCheckBox").prop("checked",true);
	} else {
		$(".cacheCheckBox").prop("checked",false);
	}
}
function selectThis(id) {
	$("#clearCacheselectAll").prop("checked",false);
	if($("#"+id).prop("checked")) {
		$("#"+id).prop("checked",true);
	} else {
		$("#"+id).prop("checked",false);
	}
}
function runSelected(){
	var cacheIdsToRun = [];
	var canRun = 0;
	$('.cacheCheckBoxChild').each(function() {
		if ($(this).prop("checked")) {
			cacheIdsToRun.push($(this).attr('id'));
			canRun = 1;
		}
	});
	var data =  cacheIdsToRun + "";
	if(canRun == 0) {
		alert("There are no clear cache test case selected to run");
	} else {
		var dataToSend = {'clearCacheIdsToRun' : data}; 
		var request = $.ajax({  
			url: "RunClearCacheTestCases.ftl",
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
	    	  refreshPage();
	     });
		});
		request.fail(function( jqXHR, textStatus ){  
			$("#ajaxSpinnerContainer").show();
			refreshPage();
		}); 
	}
}
function refreshPage() {
	document.getElementById("CacheJobExecutionId").setAttribute('action',"<@spring.url '/ClearCacheHomePost.ftl' />");
	document.getElementById("CacheJobExecutionId").setAttribute('method','POST');
	document.getElementById("CacheJobExecutionId").submit();
}
function deleteCacheJob(clearCacheId) {
	$("#clearCacheId").val(clearCacheId);
	document.getElementById("CacheJobExecutionId").setAttribute('action',"<@spring.url '/DeleteClearCacheDetails.ftl' />");
	document.getElementById("CacheJobExecutionId").setAttribute('method','POST');
	document.getElementById("CacheJobExecutionId").submit();
}
</script>
<br/>
<font face="verdana,arial" size=-1>
<div style="width:80%" class="content twoCol">
<div class="page-content" id="pagecontent" role="main" tabindex="-1">
			<#if Success?exists>
			<div class="infoModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${Success}
				</div>
			</div>
			</#if> 
			<#if error?exists>
			<div class="alertModule visible" id="tcId1" style="margin-top:1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${error}
				</div>
			</div>
			</#if>
			<!-- help contents -->
			<hr style="margin-top: 10px;">
			<div class="mblg">
				<div class="mtxs">
					<a id="tcId100" class="tipLink rel" name="tcId100">Help</a><b><@spring.message
						"mint.help.clearcache"/></b><a href="<@spring.message 'mint.help.mailTo'/>"
						target="_top"> <@spring.message "mint.help.mailId"/></a>
				</div>
			</div>
			<hr style="margin-top: 0px;">
<section class="drop mtlg">
<div class="cm">
<table cellpadding='0' cellspacing='0' border='0' width='100%'>
	<tr>
		<td class="hd">
			<h3>Cache Job Execution</h3>
		</td>
	</tr>
	<tr class="bd">
		<td>
		<form modelAttribute="MiscellaneousToolForm" action='SaveClearCacheDetails.ftl'  id="CacheJobExecutionId" name="aform" target="_top" method="POST" enctype="multipart/form-data">
			<input type="hidden" id="clearCacheId"  name="clearCacheId" value="">
			<input type="hidden" id="clearCacheIdsToRun" name="clearCacheIdsToRun" value="" /> 
			<table class="bd rowheight35">
				    <tr></tr>
					<tr class="lblFieldPair">
						<td class="lbl">Application Name</td>
						<td class="input">
							<input type="text"  id="applicationName" name="applicationName">
						</td> 
					</tr>
					<tr class="lblFieldPair">
					     <td class="lbl">Environment Name</td>
					     <td class="input">
							<input type="text"  id="environmentName" name="environmentName">
						</td>
					</tr>
					<tr class="lblFieldPair">
							<td class="lbl">Login Script</td>	
							<td class="input">
							<input type="text" id="scriptPath" name="scriptPath"> 
							<input type="file" name="scriptFile" id="scriptFile" onchange="CopyMe(this, 'txtFileName')"> 
							<input type="button" class="uploadbutton" id="scriptFileButton" value="Upload"></td>
					</tr>
			</table>
	</td>
	</tr>
	<tr class="bd">
		<td class="btnBar">
			<a href="#"  onClick="validateInputValues()" class="btn"><span>Submit</span></a>
		</td>
	</tr>
  </form>
</table>
</div>
</section>
</center>

<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="mint_home_table_content">
  	<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
		<table id="mint_home_table" class="styleA fullwidth sfhtTable" summary="">
			<caption></caption>
			<thead>
				<tr>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype" tabindex="0" data-sortpath="none">Application Name</th>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Environment Name</th>
					<th class="txtl header w25" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Last Run Date & Time</th>
					<th class="txtl header w15" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="fvalue" tabindex="0" data-sortpath="none">Status</th>
					<th class="txtl header w8">Remove</th>
					<th class="txtl header w20">Select <input style='width: 10px;' type='checkbox' class="cacheCheckBox" id='clearCacheselectAll' onClick='selectAllCheckBoxes()'></th>
				</tr>
			</thead>
			<tbody>
			<#list clearCacheList! as clearCacheData>
				<tr data-sortfiltered="false">
					<td scope="row" data-sortvalue="${clearCacheData.clearCache.applicationName!}">${clearCacheData.clearCache.applicationName!}</td>
					<td scope="row" data-sortvalue="${clearCacheData.clearCache.environmentName!}">${clearCacheData.clearCache.environmentName!}</td>
					<td scope="row" data-sortvalue="${clearCacheData.clearCacheExecutionStatus.runDate!}">${clearCacheData.clearCacheExecutionStatus.runDate!}</td>
					<td scope="row" data-sortvalue="${clearCacheData.clearCacheExecutionStatus.executionStatus!}">${clearCacheData.clearCacheExecutionStatus.executionStatus!}</td>
					<td class="row"><a title="Remove the application" class="deleteIcon np" href="#" onClick="deleteCacheJob('${clearCacheData.clearCache.clearCacheId!}')"><span class="icon nmt plxs" ></span>Remove</a></td>
					<td class="row"><input style='width: 10px;' type='checkbox' class="cacheCheckBox cacheCheckBoxChild" id="${clearCacheData.clearCache.clearCacheId!}" onClick='selectThis(this.id)'></td>
				</tr>
			</#list>
			</tdoby>
		</table>
		</div>
		<@mint.tablesort id="#mint_home_table" casesensitive="false" jsvar="mint_home_table__js"/>
		<@mint.paginate table="#mint_home_table" style="new" autocount="true" perpage="10" startpage="1" selectpage="false" showperpagedropdown="true"/>
</div>
<section class=" drop mtlg">
			<div class="cm">
				<table cellpadding='0' cellspacing='0' width="100%;">
				<tr class="bd">
				<td class="btnBar">
					<a href="#" onClick="runSelected()" class="btn"><span>Run</span></a>
				</td>
				</tr>
			</table>
			</div>
</section>
</div>
</div>
<#include "../common/footer.ftl">
</body>
</html>