<div style="width:100%;" class="content" tabindex="0" style="display: block;" id="mint_home_table_content">
  	<div class="bd">
  		<div id="onlyactive" style="overflow:auto ; overflow-y: hidden; padding-bottom:2px;">
		<table id="mint_analytics_summary_table" class="styleA fullwidth sfhtTable" summary="">
			<caption></caption>
			<thead>
				<tr>
					<th class="txtl header w5" scope="col" data-sortfilter="true" data-sortdatatype="string" data-sort="true" data-sortname="ftype1" tabindex="0" data-sortpath="none">Tag Name</th>
					<th class="txtl header w5" scope="col" data-sortfilter="false" data-sortdatatype="number" data-sort="true" data-sortname="ftype2" tabindex="0" data-sortpath="none">Total Url Count</th>
					<th class="txtl header w5" scope="col" data-sortfilter="false" data-sortdatatype="number" data-sort="true" data-sortname="ftype3" tabindex="0" data-sortpath="none">Tag Found URL Count</th>
					<th class="txtl header w5" scope="col" data-sortfilter="false" data-sortdatatype="number" data-sort="true" data-sortname="ftype4" tabindex="0" data-sortpath="none">Tag Not Found URL Count</th>
					<th class="txtl header w5" scope="col" data-sortfilter="false" data-sortdatatype="number" data-sort="true" data-sortname="ftype5" tabindex="0" data-sortpath="none">Tag has Error Url count</th>
				</tr>
			</thead>
			<tbody>
			<#if analyticsSummaryList?has_content>
				<#list analyticsSummaryList! as analytics>
					<tr data-sortfiltered="false">
						<td class="row" data-sortvalue="${analytics.tagName!}">${analytics.tagName!}</td>
						<td class="row" data-sortvalue="">${analytics.totalUrl!}</td>
						<td class="row" data-sortvalue="">${analytics.tagPresentUrlCount!}</td>
						<td class="row" data-sortvalue="">${analytics.tagNotPresentUrlCount!}</td>
						<td class="row" data-sortvalue="">${analytics.tagHasErrorUrlCount!}</td>
					</tr>
				</#list>
			<#else>
					<tr><td colspan='5'><font face='verdana,arial' size='-1'>No Records Found.</font></td></tr>
			</#if>
			</tbody>
		</table>
		</div>
		<@mint.tablesort id="#mint_analytics_summary_table" casesensitive="false" jsvar="mint_analytics_summary_table__js"/>
		<@mint.paginate table="#mint_analytics_summary_table" style="new" autocount="true" perpage="15" startpage="1" selectpage="false" showperpagedropdown="true"/>
</div>
</div>




