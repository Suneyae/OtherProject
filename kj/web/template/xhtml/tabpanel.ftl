<div id="${parameters.id}" <#rt/>
	<#list parameters.dynamicAttributes?keys as key>
	${key} = "${parameters.dynamicAttributes[key]}";
	</#list>
>
<#--Tab头处理 -->
	<ul>
	<#list parameters.tabPanel.tabs![] as tab>
		<li><a href="${tab.url!("#"+tab.id)}"><span>${tab.title!""}</span></a></li>
	</#list>
	</ul>
<#--Tab内容处理 -->
	<#list parameters.tabPanel.tabs![] as tab>
	<#if tab.content??>
	<div id="${tab.id}" myclass="fwtab">${tab.content}</div>
	</#if>
	</#list>
<#--Tab脚本处理 -->
	<script>
	$(function(){
	var options=jQuery.extend({disabled:[
<#--提取禁用的tab标签页 -->
	<#list parameters.tabPanel.tabs![] as tab><#rt>
	<#if tab.disabled><#rt>
		${tab_index},<#rt>
	</#if>
	</#list>
	],show:function(event, ui) {$(document).trigger('gridlazyInit');}}
	,${parameters.tabPanel.init!"{}"});
	jQuery("#${parameters.id}").tabs(options);
	});
	</script>
</div>