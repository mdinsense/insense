<#import "spring.ftl" as spring />
<#import "MacroTemplates.ftl" as mint />
<div id="Ui Testing Setup"><#include "../common/Header.ftl"></div>
<head>
<style type="text/css">
#divAll {
  height:90%;
  width:100%;
  overflow:auto;
  display:inline-block;
  float:left;
}
#divImage {
  height:90%;
  width:32%;
  overflow:auto;
  display:inline-block;
  margin-left:1%;
}

#divhtml {
  height:100%;
  width:100%;
  overflow:auto;
  display:inline-block;
  float:left;
}
#divpage {
  height:100%;
  width:100%;
  overflow:auto;
  display:inline-block;
  float:left;
}
#divdisplay{
  height:100%;
  width:100%;
  overflow:auto;
  display:inline-block;
  margin-left:1%;
}
</style>

<link rel="stylesheet" type="text/css"
	href="css/jquery.datetimepicker.css" />
<script src="js/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript" src="js/formValidation.js"></script>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
</head>
<script type="text/javascript">
	$(document).ready(function() {
		$("#divAll").hide();
	});
	function loadImages(imagePath, target) {
		enableLoader();
		var dataToSend = {
			'imagePath' : imagePath
		};

		var request = $.ajax({
			url : "getImage.ftl",
			type : "POST",
			cache : false,
			data : dataToSend,
			dataType : "json"
		});

		request.done(function(msg) {
			disableLoader();
			var Outmsg = JSON.stringify(msg);
			Outmsg = JSON.parse(Outmsg);

			$.each(Outmsg, function(key, val) {
					document.getElementById(target).src = "data:image/png;base64,"
							+ val;
			});
		});
		request.fail(function(jqXHR, textStatus) {
			disableLoader();
			document.getElementById(target).src = "<@spring.url '' />" + "/images/notavailable.jpg";
			$("#"+target).css("height","1000px");
		});
	}
	
	function loadHtmlFile(htmlFilePath, target) {
		enableLoader();
		var dataToSend = {
			'htmlFilePath' : htmlFilePath
		};

		var request = $.ajax({
			url : "getHtmlFileSource.ftl",
			type : "POST",
			cache : false,
			data : dataToSend,
			dataType : "json"
		});

		request.done(function(msg) {
			disableLoader();
			var Outmsg = JSON.stringify(msg);
			Outmsg = JSON.parse(Outmsg);
			$("#textCompare").html('');
			$.each(Outmsg, function(key, val) {
				$("#textCompare").html(val);
			});
		});
		request.fail(function(jqXHR, textStatus) {
			disableLoader();
		});
	}
	
	function loadPageViewHtmlFile(htmlFilePath, target) {
		enableLoader();
		var dataToSend = {
			'htmlFilePath' : htmlFilePath
		};

		var request = $.ajax({
			url : "getHtmlFileSource.ftl",
			type : "POST",
			cache : false,
			data : dataToSend,
			dataType : "json"
		});

		request.done(function(msg) {
			disableLoader();
			var Outmsg = JSON.stringify(msg);
			Outmsg = JSON.parse(Outmsg);
			$("#divhtml").html('');
			$.each(Outmsg, function(key, val) {
				$("#divhtml").html(val);
			});
		});
		request.fail(function(jqXHR, textStatus) {
			disableLoader();
		});
	}
	
	function loadDomHtmlFile(htmlFilePath, target) {
		enableLoader();
		var dataToSend = {
			'htmlFilePath' : htmlFilePath
		};

		var request = $.ajax({
			url : "getHtmlFileSource.ftl",
			type : "POST",
			cache : false,
			data : dataToSend,
			dataType : "json"
		});

		request.done(function(msg) {
			disableLoader();
			var Outmsg = JSON.stringify(msg);
			Outmsg = JSON.parse(Outmsg);
			$("#divpage").html('');
			$.each(Outmsg, function(key, val) {
				$("#divpage").html(val);
			});
		});
		request.fail(function(jqXHR, textStatus) {
			disableLoader();
		});
	}
	
	function loadTabData() {
		var form = $(document.createElement('form'));
		$('body').append(form);
		$(form).attr("name", "UiScheduleTestcaseSetup");
		$(form).attr("modelAttribute", "RegressionTestExecutionForm");
		$(form).attr("action", "<@spring.url '/Home.ftl' />");
		$(form).attr("method", "POST");
		var input1 = $("<input>").attr("type", "hidden").attr("name", "setupTabNumber").val(${regressionTestExecutionForm.setupTabNumber!});
		var input2 = $("<input>").attr("type", "hidden").attr("name", "suitId").val(document.getElementById("suitId").value);
		var input3 = $("<input>").attr("type", "hidden").attr("name", "allSched").val(${regressionTestExecutionForm.allSched!});
		var input4 = $("<input>").attr("type", "hidden").attr("name", "completedSched").val(${regressionTestExecutionForm.completedSched!});
		var input5 = $("<input>").attr("type", "hidden").attr("name", "currentSched").val(${regressionTestExecutionForm.currentSched!});
		var input6 = $("<input>").attr("type", "hidden").attr("name", "futureSched").val(${regressionTestExecutionForm.futureSched!});
		var input7 = $("<input>").attr("type", "hidden").attr("name", "scheduleExecutionId").val(${regressionTestExecutionForm.scheduleExecutionId!});
		var input8 = $("<input>").attr("type", "hidden").attr("name", "suitType").val('${regressionTestExecutionForm.suitType!}');
		
		$(form).append($(input1));
		$(form).append($(input2));
		$(form).append($(input3));
		$(form).append($(input4));
		$(form).append($(input5));
		$(form).append($(input6));
		$(form).append($(input7));
		$(form).append($(input8));
		$(form).submit();

	}
	function showComparisonPopUp(baselineImageFilePath, currentImageFilePath, imageDifferenceFilePath) {
		//This code to make it 100% X 23% of window size
		 var wWidth = $(window).width();
	     var dWidth = wWidth * 1.0;
	     var wHeight = $(window).height();
	     var dHeight = wHeight * 0.23;
	     
		$("#divAll").dialog({
			width: dWidth,
		    minHeight: "auto",
		    maxHeight: 600,
		    show: "slide",
		    title : 'Comparison Details',
		    resizable: false,
		    position: ['left', 'top'],
		    create: function (event) { $(event.target).parent().css('position', 'fixed');}
		});
		$("#divAll").show();
		loadImages(baselineImageFilePath,
				"baseline");
		loadImages(currentImageFilePath,
				"current");
		loadImages(imageDifferenceFilePath,
				"compare");
		window.scrollTo(0, 1000);
		
	}
	
	
	function textComparisonPopUp(compareDifferenceTextFilePath) {
		//This code to make it 100% X 23% of window size
		 var wWidth = $(window).width();
	     var dWidth = wWidth * 1.0;
	     var wHeight = $(window).height();
	     var dHeight = wHeight * 0.23;
	        
		$("#textCompare").dialog({
		    width: dWidth,
		    minHeight: "auto",
		    maxHeight: 600,
		    show: "slide",
		    title : 'Text Comparison',
			resizable : true,
			position : [ 'left', 'top' ],
			create: function (event) { $(event.target).parent().css('position', 'fixed');}
		});
		$("#textCompare").show();
		loadHtmlFile(compareDifferenceTextFilePath, "textCompare");
		window.scrollTo(0, 1000);
	}
	
	function htmlComparisonPopUp(htmlPageViewDifferenceFilePath) {
		//This code to make it 100% X 23% of window size
		 var wWidth = $(window).width();
	     var dWidth = wWidth * .95;
	     var wHeight = $(window).height();
	     var dHeight = wHeight * 0.23;
	     
		$("#divhtml").dialog({
			width: dWidth,
		    minHeight: "auto",
		    maxHeight: 600,
		    show: "slide",
			title : 'Html Comparison',
			resizable : true,
			position : ['left', 'top' ],
			create: function (event) { $(event.target).parent().css('position', 'fixed');}
		});
		$("#divhtml").show();
		loadPageViewHtmlFile(htmlPageViewDifferenceFilePath, "divhtml");
		window.scrollTo(0, 1000);
	}
	function pageViewPopUp(htmlDomViewDifferenceFilePath) {
		//This code to make it 100% X 23% of window size
		 var wWidth = $(window).width();
	     var dWidth = wWidth * .95;
	     var wHeight = $(window).height();
	     var dHeight = wHeight * 0.23;
	     
		$("#divpage").dialog({
			width: dWidth,
		    minHeight: "auto",
		    maxHeight: 600,
		    show: "slide",
			title : 'Page View',
			resizable : true,
			position : [ 'left', 'top'],
			create: function (event) { $(event.target).parent().css('position', 'fixed');}
		});
		$("#divpage").show();
		loadDomHtmlFile(htmlDomViewDifferenceFilePath, "divpage");
		window.scrollTo(0, 1000);
	}
</script>
<body>
				
<br />
<font face="verdana,arial" size=-1>
	<div style="width: 80%" class="content twoCol">
		<div class="page-content" id="pagecontent" role="main" tabindex="-1">
			<#if Success?exists>
			<div class="infoModule visible" id="tcId1" style="margin-top: 1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${Success}
				</div>
			</div>
			</#if> <#if error?exists>
			<div class="alertModule visible" id="tcId1" style="margin-top: 1%;">
				<div class="messageBodyContent">
					<span class="icon" title="Information"></span>${error}
				</div>
			</div>
			</#if>
			<!-- help contents -->
			<hr style="margin-top: 10px;">
			<div class="mblg">
				<div class="mtxs">
		 		<a id="tcId100" class="tipLink rel" name="tcId100" >Help</a><b>
		 		<@spring.message "mint.help.comparescreenresults"/></b>
		 		<a href="<@spring.message 'mint.help.mailTo'/>" target="_top">
				<@spring.message "mint.help.mailId"/></a>
				</div>
			</div>
			<hr style="margin-top: 0px;">
			
					<table cellpadding='0' cellspacing='0' width="100%;">
						<tr class="bd">
						<td class="">
							<a href="#" onClick="loadTabData()" style="float: left;"><span>Back<<</span></a>
						</td>
						</tr>
						
					</table>
					<section class="mtlg">
						<div class="cm">
							<table cellpadding='0' cellspacing='0' width="100%;">
								<tr>
									<td class="hd">
										<h6>Comparison Details</h6>
									</td>
								</tr>
								
							</table>
						</div>
					</section>
				
					
					<form modelAttribute="regressionTestExecutionForm"
						action='getImage.ftl' id="showImageCompariosnResult"
						name="aform" target="_top" method="POST"
						enctype="multipart/form-data">
							<input type="hidden" id="suitId" name="suitId" value="${regressionTestExecutionForm.suitId!}">
							<input type="hidden" id="comparisonDates" name="comparisonDates" value="${regressionTestExecutionForm.comparisonDates!}">
							<div class="content" tabindex="0" style="display: block;width:100%;" id="comparison_details_table_content">
									<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:10px;">
							<table id="comparison_details_table" class="styleA fullwidth sfhtTable" summary="">
								<caption></caption>
								<thead>
									<tr>
										<th id="col1" data-sortfilter="true" data-sortdatatype="number" data-sort="true" data-sortname="ftype0" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Url No</th>
										<th id="col2" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype1" class="txtl header w25" scope="col" tabindex="0" data-sortpath="none">Url</th>
										<th id="col3" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype2" class="txtl header w10" scope="col" tabindex="0" data-sortpath="none">Status</th>
										<th id="col4" data-sortfilter="true" data-sortdatatype="number" data-sort="true" data-sortname="ftype3" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Percentage Content Match</th>
										<th id="col5" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Text Comparison</th>
										<th id="col6" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Image Comparison</th>
										<th id="col7" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Page View Comparison</th>
										<th id="col8" class="txtl header w5" scope="col" tabindex="0" data-sortpath="none">Html Comparison</th>
										
									</tr>
								</thead>
								<tbody>
									<#list compareResults! as results>
									<tr data-sortfiltered="false">
										<td class="row" data-sortvalue="">${results.urlNo!}</td>
										<td class="row" data-sortvalue="">${results.url!}</td>
				
										<#assign urlFoundStatus="">

										<#if results.textFileMatched! || results.htmlMatched! || results.imageMatched!>
											<#assign urlFoundStatus="PASSED">
										<#else>
											<#assign urlFoundStatus="FAILED">
										</#if>
										
										<td class="row" data-sortvalue="">${urlFoundStatus!}</td>
										<td class="row" data-sortvalue="">
											<#if results.urlFoundInCurrentRun! && results.urlFoundInBaseline!>
												${results.percentageMatch!}
											<#elseif results.urlFoundInBaseline!>
												URL NOT Found
											<#elseif results.urlFoundInCurrentRun!>
												NEW URL
											</#if>
											
										</td>
										<td class="row" data-sortvalue="">
											<#if results.compareDifferenceTextFilePath??>
												<a href="#" class="editLink np" title="Edit the application" onClick="textComparisonPopUp('${results.compareDifferenceTextFilePath!}')"> 
													<span class="icon nmt plxs"></span>view
												</a>
											<#else>
												No Difference
											</#if>
										</td>
										<td>
											<a href="#" class="editLink np" title="View Image Differences" onClick="showComparisonPopUp('${results.baselineImageFilePath!}', '${results.currentImageFilePath!}', '${results.imageDifferenceFilePath!}')"> 
												<span class="icon nmt plxs"></span>
												<#if results.imageMatched! >
													MATCHED
												<#else>
													<#if compareImage?? && compareImage == false>
														NOT APPLICABLE
													<#else>
														<#if results.imageDifferenceFilePath??>
															View
														<#else>
															IN PROGRESS
														</#if>
													</#if>	
												</#if>
											</a>
										</td>
										<td class="row" data-sortvalue="">
											<#if results.htmlMatched!  >
												MATCHED
											<#else>
												<#if results.htmlDomFilePath??>
													<a href="#" class="editLink np" title="View DOM Differences" onClick="pageViewPopUp('${results.htmlDomFilePath!}')"> 
														<span class="icon nmt plxs"></span>view
													</a>
												<#else>
													<#if compareHtml?? && compareHtml == true>
														IN PROGRESS
													<#else>
														NOT APPLICABLE
													</#if>
												</#if>
											</#if>
										</td>
	
										<td class="row" data-sortvalue="">
											<#if results.htmlMatched!  >
												MATCHED
											<#else>
												<#if results.htmlPageViewFilePath??>
													<a href="#" class="editLink np" title="View Tag Differences" onClick="htmlComparisonPopUp('${results.htmlPageViewFilePath!}')"> 
														<span class="icon nmt plxs"></span>view
													</a>
												<#else>
													<#if compareHtml?? && compareHtml == true>
														IN PROGRESS
													<#else>
														NOT APPLICABLE
													</#if>
												</#if>
											</#if>
														
										</td>
									</tr>
									</#list>
								</tbody>
							</table>
							</div>
							<@mint.tablesort id="#comparison_details_table" casesensitive="false" jsvar="comparison_details_table__js"/>
							<@mint.paginate table="#comparison_details_table" style="new" autocount="true" perpage="25" startpage="1" selectpage="false" showperpagedropdown="true"/>
							</div>
					</form>
								
				
			<center>
				<div id="divAll" title="Basic dialog">
					<div id="divImage" >
						<h3>Base Line Screen</h3>
						<div>
							<img id="baseline" src="images/progress_bar.gif" style="overflow-x: scroll;overflow-y: scroll;"/>
						</div>
					</div>
					<div id="divImage" >
						<h3>Screen Differences</h3>
						<div>
							<img id="compare" src="images/progress_bar.gif" style="overflow-x: scroll;overflow-y: scroll; "/>
						</div>
					</div>
					<div id="divImage" >
						<h3>Current Screen</h3>
						<div>
							<img id="current" src="images/progress_bar.gif" style="overflow-x: scroll;overflow-y: scroll;" />
						</div>
					</div>
				</div>
				<div id="divdisplay" style="overflow-x:auto;overflow-y: auto;">
					<div id="textCompare" title="Text Difference" style="overflow-x: auto;overflow-y: auto;">
					</div>
				</div>
				<div id="divdisplay" style="overflow-x:auto;overflow-y: auto;">
					<div id="divhtml" title="Page view" style="overflow-x: auto;overflow-y: auto;">
					</div>
				</div>
				<div id="divdisplay" style="overflow-x:auto;overflow-y: auto;">
					<div id="divpage" title="Html Comparison" style="overflow-x: auto;overflow-y: auto;">
					</div>
				</div>
			</center>
		</div>
	</div> <#include "../common/footer.ftl">
	</body>
	</html>