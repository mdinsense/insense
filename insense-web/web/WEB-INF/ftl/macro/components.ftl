<#assign tagIdCounter = 0>

<#function componentInitialize id="" defer="false" jsvar="" inline="true" placement="">
	<#local functionJsVar = "">
	<#if id == "">
		<#local tagId = "tcId${tagIdCounter}">
		<#assign tagIdCounter = tagIdCounter + 1>
	<#else>
		<#local tagId = "${id}">
	</#if>
	<#local defer = boolean(defer, "false")>
	<#local inline = boolean(inline, "true")>
	<#if placement =="">
		<#if defer == "true">
			<#local placement = "domReady">
		</#if>
	<#else>
		<#if placement == "domReady">
			<#local defer = "true">
		<#else>
			<#local defer = "false">
		</#if>
	</#if>
	<#if jsvar != "">
		<#if defer == "true" || placement == "windowLoad">
			<#local functionJsVar = "${jsvar} = ">
			<#local jsvar = "var ${jsvar} =">
		<#else>
			<#local functionJsVar = "var ${jsvar} =">
		</#if>
	</#if>	
	<#return {
		"tagId": "${tagId}", 
		"defer": "${defer}", 
		"jsvar": "${jsvar}", 
		"functionJsVar": "${functionJsVar}",
		"originalId": "${id}",
		"inline": "${inline}",
		"placement": "${placement}"
		}>
</#function>


<#function boolean value="" default="false">
	<#local retval = value>
	<#if default != "true" && default != "false">
		<#local default = "false">
	</#if>
	<#if retval != "true" && retval != "false">
		<#local retval = default>
	</#if>
	<#return retval>
</#function>

<#macro wrapComponent jsvar defer>
	<#if defer == "true">
		<#if jsvar != "">
			${jsvar} null;
		</#if>
		$(document).ready(function(){
	</#if>
	<#nested>
	<#if defer == "true">
	});
	</#if>
</#macro>

