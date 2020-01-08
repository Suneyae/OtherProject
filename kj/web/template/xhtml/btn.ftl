<#assign rd=parameters.readonly>
<#assign  hf=parameters.href>
<#assign  delay=parameters.delay>
<#assign  isDelay=parameters.isDelay>
<#assign disabled="">
<#if rd=="true">
<#assign disabled="disabled">
</#if>
<#if isDelay>
    <#assign  eventFun="__btnClick(this,${parameters.onclick?replace('();','')},${delay});return false;">
<#else>
    <#assign  eventFun="${parameters.onclick};return false;">
</#if>

<#if hf=="javascript:void(0)">
 <a class="buttonlink"  ${disabled}  plain="false" name="${parameters.name}" id="${parameters.id}" title="${parameters.title}" 　 keycomb="${parameters.keycomb}" href="${hf}" onclick="${eventFun}" >${parameters.value}</a> 
 <#else>
 <a class="buttonlink"  ${disabled}  plain="false"  name="${parameters.name}" id="${parameters.id}" title="${parameters.title}" 　 keycomb="${parameters.keycomb}" href="${hf}" >${parameters.value}</a>
 </#if>
 
 