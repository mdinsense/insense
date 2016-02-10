<#macro paginate table id ="" perpage="" jsvar="" defer="false" style="normal" startpage="" autocount="" multisort="" 
	showperpagedropdown="" datasource="" pagelistinglimit="" selectpage="true">
	
	<#local initialValues = componentInitialize(id, defer, jsvar)>
	<#local tagId = initialValues.tagId>
	<#local defer = initialValues.defer>
	<#local jsvar = initialValues.jsvar>
	<#local functionJsVar = initialValues.functionJsVar>

	<#local autocount = boolean(autocount, "false")>	
	<#local multisort = boolean(multisort, "false")>	
	<#local selectpage = boolean(selectpage, "true")>	
	<#local showperpagedropdown = boolean(showperpagedropdown, "true")>

	<#if perpage == "">
		<#local perpage = "20">
	</#if>	
	<#if startpage == "">
		<#local startpage = "1">
	</#if>
	<#if datasource == "">
		<#local datasource="''">
	<#elseif datasource?starts_with("js:")>	
		<#local datasource="${datasource?substring(3)}">
	<#else>
		<#local datasource="'${datasource}'">
	</#if>
	
	<#if pagelistinglimit == "">
		<#local pagelistinglimit = "null">
	</#if>
	<#if style != "new">
	<div id="${tagId}" class="pagination"></div>
	<#else>
	<div id="${tagId}" class="pagenavi clearfix">
		<div id="paginglabel${tagId}" class="screenReader">Pagination</div>
		<div class="fll">
			<label for="pageselect">Results Per Page</label>
			<select id="pageselect${tagId}" name="pageselect"></select>
		</div>
		<ul role="navigation" aria-labelledby="paginglabel${tagId}" class="pagingList">
			<li>
				<a class="backLink" href="#" rel="prev">Previous <span class="icon"></span></a>
			</li>
			<li class="pageNav">
				<span id="pageLabel${tagId}" class="hidden" aria-hidden="true">Page <span>5</span></span>
				<label id="gotoPagelabel${tagId}" for="gotoPage${tagId}">Page</label>
				<input type="text" value="5" id="gotoPage${tagId}" size="5" aria-labelledby="gotoPagelabel${tagId} ofPages${tagId}" />
					of <span id="ofPages${tagId}">100</span>
				<input type="button" value="Go" class="btn3 btnSmall ml" title="Submit" />
			</li>
			<li class="last">
				<a href="#" class="nextLink" rel="next">Next <span class="icon"></span></a>
			</li>
		</ul>
	</div>
	</#if>
	<script>
		<@wrapComponent jsvar="${jsvar}" defer="${defer}">
		${functionJsVar} new tiaacref.paginate({
			id: '#${tagId}',
			table: '${table}',
			perPage: ${perpage},
			startPage: ${startpage},
			autoCount: ${autocount},
			multiSort: ${multisort},
			showPerPageDropDown: ${showperpagedropdown},
			dataSource: ${datasource},
			style: '${style}',
			selectPage: ${selectpage},
			pageListingLimit: ${pagelistinglimit}
		});
		</@wrapComponent>
	</script>
</#macro>
