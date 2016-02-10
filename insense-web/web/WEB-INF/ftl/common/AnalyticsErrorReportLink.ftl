  <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
   <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
    <script src="js/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script type="text/javascript" src="js/formValidation.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
    <script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
<script type="text/javascript">

function showSiteCatalystTagDataPopUp(scheduleExecutionId, baseLinePageUrl, pageTitle, tagName, detailsOrError){
	//This code to make it 100% X 70% of window size
	 var wWidth = $(window).width();
     var dWidth = wWidth * 1.0;
     var wHeight = $(window).height();
     var dHeight = wHeight * 0.3;
     
	$("#siteCatalystTagDataErrorSummary").dialog({
		width: wWidth,
	    minHeight: "auto",
	    maxHeight: dHeight,
	    show: "slide",
		title : 'Download Reports',
		resizable : false,
		position : [ 'center', 'top' ],
		create: function (event) { $(event.target).parent().css('position', 'fixed');}
	});
	addLoaderToDiv("siteCatalystTagDataErrorSummary");
	loadSiteCatalystTagDataSummary(scheduleExecutionId, baseLinePageUrl, pageTitle, tagName, detailsOrError);
	$("#siteCatalystTagDataErrorSummary").show();
	window.scrollTo(0, 1000);
}

function loadSiteCatalystTagDataSummary(scheduleExecutionId, baseLinePageUrl, pageTitle, tagName, detailsOrError){
	var newLine="**";
	var request = $.ajax({  url: 
		"getSiteCatalystTagDataMapValues.ftl",
		data: {scheduleExecutionId:scheduleExecutionId , baseLinePageUrl:baseLinePageUrl, pageTitle:pageTitle, tagName:tagName, detailsOrError:detailsOrError  },
		type: "POST", 
		cache: false,
		dataType: "text"});
		
		request.done(function( uiSiteCatalystTagDataSummary ) {
			disableLoader();
			var innerHtml = siteCatalystTagDataTableHeader();
			if ($.parseJSON(uiSiteCatalystTagDataSummary) != ''){
				innerHtml = innerHtml + '<tbody>';
				$.each($.parseJSON(uiSiteCatalystTagDataSummary), function(idx, siteCatalyst) {
					if(siteCatalyst.first == newLine){
						innerHtml = innerHtml + '<tr data-sortfiltered="false"><td colspan="3"><hr></td></tr>';
					}else{
						innerHtml = innerHtml + '<tr data-sortfiltered="false">';
			 			innerHtml = innerHtml + '<td class="row">' + siteCatalyst.first + '</td>';
						innerHtml = innerHtml + '<td class="row">' + siteCatalyst.second + '</td>';
						innerHtml = innerHtml + '<td class="row">' + siteCatalyst.third + '</td>';
						innerHtml = innerHtml + '</tr>';
					}
				});
				innerHtml = innerHtml + '</tbody>';
			}else{
				innerHtml = innerHtml + "<tbody><tr><td colspan='3'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr></tbody>";
			}
	 			$('#siteCatalystTagDataErrorSummary').empty().append(innerHtml);
		}); 	
		
	 	request.fail(
	  		function( jqXHR, textStatus ){  
	  			$("#siteCatalystTagDataErrorSummary").html("<div>Failed to load reports</div");
	  		}); 
}

function siteCatalystTagDataTableHeader(){
	var innerHtml = '<thead><tr><th id="scheduleIdRow" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Data Name</th>';
	innerHtml = innerHtml + '<th id="startDateTimeRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Expected Data Value</th>';
	innerHtml = innerHtml + '<th id="startDateTimeRow" class="txtl header w10" scope="col"  tabindex="0" data-sortpath="none">Actual Data Value</th>';
	return innerHtml;
}

</script>   
<section class="mtlg">
	<div class="cm">
		<table cellpadding='0' cellspacing='0' width="100%;">
			<tr>
				<td class="hd">
					<h6>Choose Tag Name</h6>
				</td>
			</tr>
			<tr class="bd">
				<td style="padding:10px">
						<table class="bd rowheight35">
						<tr class="lblFieldPair">
							<td class="lbl">Tag Name</td>
							<td class="input">
								<select id="analyticTagName" name="analyticTagName" onchange="getAnalyticsDetailsOrErrorList(this.value,'Error');">
								<option value="-1" selected="true">----------Select-------------</option>
									<#if specificTagName == "ALL">
										<option value="ALL" selected="true">ALL</option>
									<#else>
										<option value="ALL">ALL</option>
									</#if>
									<#list regressionTestExecutionForm.tagNameDataList! as tagList> 
										<#if tagList?exists && tagList == specificTagName>
											<option value="${tagList!}" selected="true">${tagList!}</option>
										<#else>
											<option value="${tagList!}">${tagList!}</option>
										</#if> 
									</#list>
										
								</select><font color="red" size=-1>&nbsp;*</font>
							</td>
						</tr>
						</table>
						<center><font color="red" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* mandatory</font></center>
				</td>
			</tr>		
		</table>
	</div>
</section>

<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="mint_analytics_error_table_content">
  	<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
		<table id="mint_analytics_error_table" class="styleA fullwidth sfhtTable" summary="">
			<caption></caption>
			<thead>
				<tr>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype5" tabindex="0" data-sortpath="none">PageUrl</th>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype3" tabindex="0" data-sortpath="none">PageTitle</th>
					<th class="txtl header w10" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype7" tabindex="0" data-sortpath="none">TagName</th>
					<th class="txtl header w10" >View Tag Data</th>
				</tr>
			</thead>
			<tbody>
			<#if analyticsErrorList?has_content>
				<#list analyticsErrorList! as analytics>
					<tr data-sortfiltered="false">
						<td class="row" data-sortvalue="${analytics.currentRunPageUrl!}">${analytics.currentRunPageUrl!}</td>
						<td class="row" data-sortvalue="${analytics.pageTitle!}">${analytics.pageTitle!}</td>
						<#if specificTagName == "ALL">
							<td class="row" data-sortvalue="${analytics.actualTagName!}">${analytics.actualTagName!}</td>						
							<td class="row" ><a href="#" class="editLink np" title="Site Catalyst tagDate" onClick="showSiteCatalystTagDataPopUp(${analytics.scheduleExecutionId!},'${analytics.baseLinePageUrl!}','${analytics.pageTitle!}','${analytics.actualTagName!}','Error')"><span class="icon nmt plxs" ></span>Click to View</a></td>
						<#else>
							<td class="row" data-sortvalue="${specificTagName!}">${specificTagName!}</td>
							<td class="row" ><a href="#" class="editLink np" title="Site Catalyst tagDate" onClick="showSiteCatalystTagDataPopUp(${analytics.scheduleExecutionId!},'${analytics.baseLinePageUrl!}','${analytics.pageTitle!}','${specificTagName!}','Error')"><span class="icon nmt plxs" ></span>Click to View</a></td>
						</#if>	
						
					</tr>
				</#list>
			<#else>
					<tr><td colspan='11'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
			</#if>
			</tbody>
		</table>
		</div>
		<@mint.tablesort id="#mint_analytics_error_table" casesensitive="false" jsvar="mint_analytics_error_table__js"/>
		<@mint.paginate table="#mint_analytics_error_table" style="new" autocount="true" perpage="15" startpage="1" selectpage="false" showperpagedropdown="true"/>
	</div>
</div>
<center>
	<div id="siteCatalystTagDataErrorSummary" title="view SiteCatalyst TagData">
	</div>
</center>


