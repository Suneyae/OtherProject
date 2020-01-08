<#--多记录表的固定表头和扩展表头的输出方式基本一样，所以先先定义个表头宏，简化代码 -->

<#-- 输出列标题 函数--> 
<#macro GLT_COL v_head_row isFixed> 
			<#list v_head_row as col><#--列数据生成 -->
				 <th nowrap="nowrap" rowspan=${col.rowspan!1} colspan=${col.colspan!1} <#if col.align??> _al=${col.align}</#if> sortable=${col.sortable!'true'} 
				 <#if col.width??>style="width=${col.width}"</#if> <#if (col.lastRowIndex??)>lastRowIndex="${col.lastRowIndex}" </#if>
				  <#if isFixed>class=fwdatagrid-th-fixed<#else>class=fwdatagrid-th</#if>
				  <#if col.checkbox??> style="text-align:left;padding-left:2px"</#if> > 
				   
						<#if !(col.lastRowIndex??)><#--标题组生成方式 -->
						    <#if col.checkbox??><#--生成checkbox --> 
							  <input type=checkbox class=fwdatagrid-header-check> 
							<#else> 
								${col.title!""}
							</#if> 
						<#else><#--非标题组生成方式 -->  
						    <#if col.checkbox??><#--生成checkbox -->
								  <input type=checkbox class=fwdatagrid-header-check> 
							<#else>  
							<div class=fwdatagrid-div onMouseDown="FWMouseDownToResize(this,'${parameters.id}_table');" onMouseMove="FWMouseMoveToResize(this,'${parameters.id}_table');" onMouseUp="FWMouseUpToResize(this);">  
							 ${col.title!""}
							 <span class=fwdatagrid-sort-icon>&nbsp;</span> 
							 </div>  
							</#if> 
					    </#if> 
			     
				 </th>
			 </#list>  
</#macro>

<#macro GLT_HEADER v_head_fixed,v_head_colHead>

       <#if ((v_head_fixed?size) > 1 )>
         <#-- 含有分组标题的情形先要拼第一行 -->
          <tr class=fwdatagrid-header-group >
            <th nowrap="nowrap" rowspan=2 class=fwdatagrid-th-rownum>&nbsp;</th>	
            <#list v_head_fixed as row>
              <@GLT_COL row true />
              <#break>
             </#list> 
             
             <#list v_head_colHead as row>
              <@GLT_COL row false/>
              <#break>
             </#list> 
          </tr>
          
          <#-- 拼第二行 -->
            <tr class=fwdatagrid-header-tr>
            <#list v_head_fixed as row>
               <#if (row_index >0) >  
                   <@GLT_COL row true/> 
               </#if>
             </#list> 
             
             <#list v_head_colHead as row>
                 <#if (row_index >0) >  
                  <@GLT_COL row false />
                </#if> 
             </#list> 
          </tr>
          
       <#else>
            <tr class=fwdatagrid-header-tr style="height:20px">
              <th nowrap="nowrap" class=fwdatagrid-th-rownum>&nbsp;</th>	
            <#list v_head_fixed as row>
              <@GLT_COL row true/>
              <#break>
             </#list> 
             
             <#list v_head_colHead as row>
              <@GLT_COL row false/>
              <#break>
             </#list> 
          </tr>
       </#if> 
     
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
		<div class="fwdatagrid" style="height:${parameters.options.height!'220'}px;" >   <!-- 整个grid的外层 -->  
			<div class="fwdatagrid-wrap"  >   <!-- 整个grid的覆盖区 -->
				<#assign buildPage=(parameters.options.hasPage||(parameters.options.expbtn?length!"")>0) >
				<#if (["TOP","BOTH"]?seq_contains(parameters.pageAlign!"BOTTOM"))&&buildPage >
				<div class="fwdatagrid-pager"></div><!-- 整个grid的分页区 -->
				</#if>
				<div class="fwdatagrid-view" > <!-- 整个grid的数据区 --> 
				    <table  border=0 cellspacing=0 cellpadding=0  width=100% id="${parameters.id}_table"> 
			          <thead>
			  	       <@GLT_HEADER parameters.glt.fixHead![] parameters.glt.colHead />
			          </thead>
			        <tbody> 
				    </tbody>
		            </table> 
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