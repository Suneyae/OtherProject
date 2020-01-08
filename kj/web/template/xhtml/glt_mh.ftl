<#--多记录表的固定表头和扩展表头的输出方式基本一样，所以先先定义个表头宏，简化代码 -->
<#macro GLT_HEADER v_head_arr isFixed>
<div class=fwdatagrid-body>
<table  border=0 cellspacing=0 cellpadding=0>
    <thead class="fwdatagrid-header">
    <#if ((v_head_arr?size) > 0 )>
        <#if isFixed ><#--固定表头前才需要先输出一个空列 -->
        <#assign td_rownumber>
            <td rowspan=${v_head_arr?size}><div class=fwdatagrid-header-rownumber></div></td>
        </#assign>
        </#if>
        <#list v_head_arr as row>
        <tr>
            ${td_rownumber!""}<#assign td_rownumber=""><#--只在第一行生成，其他行输出空 -->
            <#list row as col><#--列数据生成 -->
                <#if !(col.lastRowIndex??)><#--标题组生成方式 -->
                <td rowspan=${col.rowspan!1} colspan=${col.colspan!1} width="${col.width!""}">
                   <#if col.checkbox??><#--生成checkbox --> 
                        <input type=checkbox class=fwdatagrid-header-check>
                    <#else>
                        ${col.title!""}
                    </#if>
                <#else><#--非标题组生成方式 -->
                <td rowspan=${col.rowspan!1} colspan=${col.colspan!1} lastRowIndex="${col.lastRowIndex}"
                 width="${col.width!""}" _al="${col.colspan!"center"}" sortable=${col.sortable!'true'}>
                    <#if col.checkbox??><#--生成checkbox -->
                        <input type=checkbox class=fwdatagrid-header-check>${col.title!""}
                    <#else>
                        <span>${col.title!""}</span>
                        <span class=fwdatagrid-sort-icon>&nbsp;</span>
                    </#if>
                    <#--<#assign col_style=col_style+[(col.align!"left")]> -->
                </#if>
                </td>
            </#list>
        </tr>
        </#list>
    <#elseif isFixed>
        <tr>
        <td rowspan=1><div class=fwdatagrid-header-rownumber></div></td>
        </tr>
    </#if>
    </thead>
</table>
</div>
</#macro>
<#-- 开始生成多记录表脚本-->
<#if parameters.hasTitle!true >
<fieldset class="fieldset2"  align="center" style="cursor:default;width:${parameters.width!"100%"}" >
    <legend class="legend" style="cursor:hand;" >
        <span onclick="FWshowTable('img_${parameters.id}_grid','${parameters.id}')" >
            <img id="img_${parameters.id}_grid" src="${request.contextPath}/themes/default/images/query_icon_right.gif">
        </span>
        <span title="单击展开或收缩" onclick="FWshowTable('img_${parameters.id}_grid','${parameters.id}')">
           ${parameters.title!(parameters.glt.title!'')}
        </span>
    </legend>
</#if>
    <div align="left" style="width:100%" id="${parameters.id}" >  
        <div class="fwdatagrid"  style="height:${parameters.options.height!'220'}px;" >   <!-- 整个grid的外层 -->  
            <div class="fwdatagrid-wrap"  >   <!-- 整个grid的覆盖区 -->
                <#assign buildPage=(parameters.options.hasPage||(parameters.options.expbtn?length!"")>0) >
                <#if (["TOP","BOTH"]?seq_contains(parameters.pageAlign!"BOTTOM"))&&buildPage >
                <div class="fwdatagrid-pager"></div><!-- 整个grid的分页区 -->
                </#if>
                <div class="fwdatagrid-view" >           <!-- 整个grid的数据区 -->
                    <@GLT_HEADER parameters.mergeHeader true/>
                    <div class="fwdatagrid-resize-proxy"> </div>
                </div>
                <#if (["BOTTOM","BOTH"]?seq_contains(parameters.pageAlign!"BOTTOM"))&&buildPage >
                <div class="fwdatagrid-pager"></div><!-- 整个grid的分页区 -->
                </#if>
            </div>
        </div>
        <input type="hidden" name="_multiple" />
    </div>
 <#if parameters.hasTitle!true >
</fieldset>
</#if>
<#-- 生成多记录表对应js脚本的区域-->
<script language="javascript">
$(function(){
    if($(${parameters.id}).data('_init')){return;}
    //标志已经初始化，无需重新初始化
    $(${parameters.id}).data('_init',true);
    <#if !(parameters.showContent!true)>
    FWshowTable('img_${parameters.id}_grid','${parameters.id}');
    </#if>
    $("#${parameters.id}").fwdatagrid(${tojson(parameters.options)});
});
</script> 