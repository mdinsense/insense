<#macro tablesort id jsvar="" defer="false" type="" value="" name="" multisort="" onbeforesort="" onaftersort="" casesensitive="" initialsort="">

	<#local initialValues = componentInitialize(id, defer, jsvar)>
	<#local tagId = initialValues.tagId>
	<#local defer = initialValues.defer>
	<#local jsvar = initialValues.jsvar>
	<#local functionJsVar = initialValues.functionJsVar>

	<#local multisort = boolean(multisort, "false")>
	<#local casesensitive = boolean(casesensitive, "true")>
	<#if onbeforesort == "">
		<#local onbeforesort = "''">
	</#if>
	<#if onaftersort == "">
		<#local onaftersort = "''">
	</#if>
	<script>
		<@wrapComponent jsvar="${jsvar}" defer="${defer}">
			${functionJsVar} new tiaacref.tableSort({
			id: '${tagId}',
			multiSort: ${multisort},
			onBeforeSort: function(e){${onbeforesort}},
			onAfterSort: function(e){${onaftersort}},
			caseSensitive: ${casesensitive},
			initialSort: '${initialsort}'
			});
		</@wrapComponent>
	</script>
</#macro>
