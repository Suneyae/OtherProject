<#setting url_escaping_charset='UTF-8'> 
<#-- 提取java枚举类型-->
<#assign GtType={ 
    "LABEL":"01",
    "INPUT":"02", 
    "TEXTAREA":"03",
    "COMBOBOX":"05",
    "DATEPICKER":"06",
    "RADIOBOX":"08",
    "CHECKBOX":"09",
    "BUTTON":"10",
    "LINK":"11",
    "IMG":"12",
    "SFZ":"18"
 }>
 <#-- 准备单选控件的值-->
<#assign inputSets= {
    <#--子类型：[type类型，class名称，最大长度] -->
    "p":["text","postbox",6],<#-- 邮件编码-->
    "n":["text","numberbox",50],<#-- 只能输入数字-->
    "c":["text","chinessbox",50],<#-- 只能输入中文-->
    "f":["text","floatbox",50],<#-- 只能输入浮点数-->
    "d":["text","yearmonthbox",6],<#-- 只能输入YYYYMM格式的日期-->
    "t":["text","timebox",8],<#-- 只能输入时间格式-->
    "s":["text","datetime",19],<#-- 只能输入日期+时间 格式-->
    "m":["password","textbox",50],<#-- 密码输入框-->
    "x":["text","notpastebox",50],<#-- 不能粘贴的输入框-->
    " ":["text","textbox",50],<#-- 默认-->
    "b":["text","textbox",50]<#-- 默认-->
    }>
<#assign colNames=(parameters.options.data.headers![])>
<#--准备 生成TD所需的表头定义列（过滤多列表头，仅保留最后有效的一列）-->
<#if (parameters.options.frozenColumns > 0) >
<#assign v_fixHead = parameters.lastHeaderRow[0..(parameters.options.frozenColumns-1)] >
<#assign v_colHead = parameters.lastHeaderRow[(parameters.options.frozenColumns)..]![] >
<#else>
<#assign v_fixHead = [] >
<#assign v_colHead = parameters.lastHeaderRow >
</#if>
 
<#--macro 生成TD-->
<#macro GT_TD v_input_comp v_data_one v_row_pos v_col_pos bFixed>
 <#assign v_pname=colNames[v_col_pos]!"" >
<td field="${v_pname}" align="${v_input_comp.align!"center"}"  <#if bFixed>class=fweditgrid-td-fixed<#else>class=fweditgrid-td</#if>> 
 <#assign compValue = v_data_one[("_DIC_"+v_pname)]!""
  hiddenValue = v_data_one[v_pname]!"">
<#if compValue=="">
  <#assign compValue = hiddenValue>
</#if> 

<#--全部的控件都不用NAME 用rw来表示行 用nd来表示列名,目的为了减少submit时提交的数据量-->
 <#switch v_input_comp.type>
    <#case GtType.LABEL>
        <label rw="${v_row_pos}" nd="${v_pname}" style="width:100%;" value="${hiddenValue}">${compValue}</label>
        <#break>
    <#case GtType.LINK> <#--用于配置超链接的情况 不能用VALUE来保存因为双引号重复-->
         <label rw="${v_row_pos}" nd="${v_pname}" style="width:100%;" >${compValue}</label>
         <#break>
    <#case GtType.INPUT ><#--处理单行输入控件--> 
        <#assign inputTmp=inputSets[v_input_comp.subtype]> 
        <#if (compValue?is_sequence)>
            <#assign compValue=(compValue[0]?string)!"">
        </#if>
        <input title="${v_input_comp.tip}" type="${inputTmp[0]}" class="${inputTmp[1]}" maxlength="${v_input_comp.maxlength!inputTmp[2]}" rw="${v_row_pos}" nd="${v_pname}" value="${compValue}" vldStr="${v_input_comp.vld}"  ${v_input_comp.cus} />
     <#break> 
    <#case GtType.SFZ ><#--处理身份证 --> 
        <#if (compValue?is_sequence)>
            <#assign compValue=(compValue[0]?string)!"">
        </#if>
        <input type="text" title="${v_input_comp.tip}" class="idcardbox" maxlength="${v_input_comp.maxlength!19}" rw="${v_row_pos}" nd="${v_pname}" value="${compValue}" vldStr="${v_input_comp.vld}" ${v_input_comp.cus} />
        <#break>
    <#case GtType.TEXTAREA ><#--处理多行输入控件 --> 
        <#if (compValue?is_sequence)>
            <#assign compValue=(compValue[0]?string)!"">
        </#if>
        <textarea class="textareabox" title="${v_input_comp.tip}" rows="${v_input_comp.rows!3}" rw="${v_row_pos}" nd="${v_pname}" vldStr="${v_input_comp.vld}" ${v_input_comp.cus} >${compValue}</textarea>
        <#break>
    <#case GtType.DATEPICKER ><#--处理日期控件 --> 
        <#if (compValue?is_sequence)>
            <#assign compValue=(compValue[0]?string)!"">
        </#if>
        <input type="text" class="datebox" title="${v_input_comp.tip}" rw="${v_row_pos}" nd="${v_pname}" vldStr="${v_input_comp.vld}" maxlength="${v_input_comp.maxlength!50}" value="${compValue}" ${v_input_comp.cus} /> 
        <#break>
    <#case GtType.BUTTON ><#--处理按钮-->
        <#if (compValue?is_sequence)>
            <#assign compValue=(compValue[0]?string)!"">
        </#if>
        <a class="buttonlink"  ${v_input_comp.cus} rw="${v_row_pos}" nd="${v_pname}" title="${v_input_comp.tip}" >${compValue}</a>
        <#break>
    <#case GtType.CHECKBOX >
        <#-- 取控件的值-->
        <#if (hiddenValue?is_sequence)>
            <#assign hiddenValue=(hiddenValue[0]?string)!"">
        </#if>
        <#assign ck=""  value=(v_input_comp.value)!""  chk=v_data_one[v_input_comp.chk]!"" > 
        <#if chk=="1">
            <#assign ck="checked">
        </#if>

        <#-- 单checkbox处理 -->
        <input title="${v_input_comp.tip}" type="checkbox" class="checkbox" ${v_input_comp.cus} name="${v_pname}"
        rw="${v_row_pos}" nd="${v_pname}" value="${compValue}" vldStr="${v_input_comp.vld}"  ${ck} />
        <#break>
    <#case GtType.RADIOBOX >
        <#if (hiddenValue?is_sequence)>
            <#assign hiddenValue=(hiddenValue[0]?string)!"">
        </#if>
        <#if (parameters.glt.dict[v_pname])??>
        <#-- 字典项处理 -->
            <#assign dicts=parameters.options.data.dicts[v_pname]!{}
            v_colNum=(v_input_comp.colNum?number)!10 >
            <table border=0 cellspacing=0 cellpadding=0 class="inputSet">
            <#list dicts?keys?chunk(v_colNum) as subkeys>
              <tr>
              <#list subkeys as key>
                <td align="right"  style="border:0">
                   <label>${dicts[key]!""}<input title="${v_input_comp.tip}" type="radio" class="radio" ${v_input_comp.cus} 
            rw="${v_row_pos}" nd="${v_pname}" name="${v_row_pos+(v_pname)}" value="${key}" 
            ${(hiddenValue=(key))?string("checked","")} /></label>
                </td>
              </#list>
              </tr>
            </#list>
            </table>
       <#else>
           <label>${v_input_comp.text!""}<input type="radio" title="${v_input_comp.tip}" class="radio"
           ${v_input_comp.cus} rw="${v_row_pos}" nd="${v_pname}" name="${v_pname}" value="${compValue!""}" 
           vldStr="${v_input_comp.vld}" ${(hiddenValue=(v_input_comp.value!""))?string("checked","")}  /></label>
       </#if>
       <#break> 
    <#case GtType.COMBOBOX>
        <#if v_input_comp.subtype!='b'>
           <#assign v_readOnly='readOnly=true'>
        </#if>
        <#assign inputTmp=inputSets[v_input_comp.subtype]> 
        <input type="text"   class="${inputTmp[1]}" title="${v_input_comp.tip}" cmb="true" rw="${v_row_pos}" nd="${v_pname}_CMB"  onfocus="${parameters.id}_combox(this,'${v_pname}');" maxlength="${v_input_comp.maxlength!50}" value="${compValue}" ${v_input_comp.cus}   doselect="${v_input_comp.onselect!""}"<#rt>/> 
        <input type="hidden" class="gltcom" rw="${v_row_pos}" nd="${v_pname}"  vldStr="${v_input_comp.vld}" value="${hiddenValue}" />
      <#break>
    <#default>
</#switch> 
 </td>
</#macro>

<#--生成单行记录的函数 -->
                    
<#macro GLT_TR v_fix_arr v_col_arr v_data_one irow icolOffset>
<#if ((v_col_arr?size)>0)>
    <tr fweditgrid-row-index=${irow} > 
      
         <td class=fweditgrid-td-rownumber>${irow+1}</td>
        <#if parameters.showDelete=="true" >
          <td class=fweditgrid-td-opr><a class="gltlink"  href="javascript:void(0)" onclick="${parameters.id}_delRow(this);return false;" >删除</a></td>
        </#if>
    
       <#list v_fix_arr as cols> 
         <@GT_TD cols v_data_one irow cols_index true/>
       </#list> 

       <#list v_col_arr as cols> 
         <@GT_TD cols v_data_one irow icolOffset+cols_index false/>
       </#list> 

     </tr>
<#else>
 <tr fweditgrid-row-index=${irow} > 
  <#if bFixed>
    <td class=fweditgrid-td-rownumber>${irow+1}</td>
    <#if parameters.showDelete=="true" >
    <td class=fweditgrid-td-opr><div class=fweditgrid-cell-opr><a class="gltlink"  href="javascript:void(0)" onclick="${parameters.id}_delRow(this);return false;" >删除</a></div></td>
    </#if>
  </#if>
 </tr>
</#if>
</#macro>

<#--生成整个表单记录的函数 -->
<#macro GLT_DATA v_data_arr >
    <#if ((v_data_arr?size)>0)> 
        <#list v_data_arr as rows>
          <@GLT_TR v_fixHead v_colHead rows rows_index parameters.options.frozenColumns/>
        </#list> 
    <#else>
          <@GLT_TR v_fixHead v_colHead {} 0 parameters.options.frozenColumns/> 
    </#if> 
</#macro>


<#-- 输出列标题 函数--> 
<#macro GLT_COL v_head_row isFixed> 
			<#list v_head_row as col><#--列数据生成 -->
				 <th nowrap="nowrap" rowspan=${col.rowspan!1} colspan=${col.colspan!1} <#if col.align??> _al=${col.align}</#if> sortable=${col.sortable!'true'} 
				 <#if col.width??>style="width=${col.width}"</#if> <#if (col.lastRowIndex??)>lastRowIndex="${col.lastRowIndex}" </#if>
				  <#if isFixed>class=fweditgrid-th-fixed<#else>class=fweditgrid-th</#if> > 
				   
						<#if !(col.lastRowIndex??)><#--标题组生成方式 -->
						    <#if col.checkbox??><#--生成checkbox --> 
							  <input type=checkbox class=fweditgrid-header-check> 
							<#else> 
								${col.title!""}
							</#if> 
						<#else><#--非标题组生成方式 -->  
						    <#if col.checkbox??><#--生成checkbox -->
								  <input type=checkbox class=fweditgrid-header-check> 
							<#else> 
							 <div class=fweditgrid-div onMouseDown="FWMouseDownToResize(this,'${parameters.id}_table');" onMouseMove="FWMouseMoveToResize(this,'${parameters.id}_table');" onMouseUp="FWMouseUpToResize(this);">  
							 ${col.title!""}
							 <span class=fweditgrid-sort-icon>&nbsp;</span> 
							 </div>  
							</#if> 
					    </#if> 
			     
				 </th>
			 </#list>  
</#macro>



<#macro GLT_HEADER v_head_fixed,v_head_colHead>

       <#if ((v_head_fixed?size) > 1 )>
         <#-- 含有分组标题的情形先要拼第一行 -->
          <tr class=fweditgrid-header-group >
              <th nowrap="nowrap" rowspan=2 class=fweditgrid-th-rownum>&nbsp;</th>	
		    <#if parameters.showDelete=="true" >
              <th nowrap="nowrap" rowspan=2  class=fweditgrid-th-opr>操作</th> 
            </#if>

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
            <tr class=fweditgrid-header-tr>
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
            <tr class=fweditgrid-header-tr>
              <th nowrap="nowrap" class=fweditgrid-th-rownum>&nbsp;</th>	
		    <#if parameters.showDelete=="true" >
              <th nowrap="nowrap" class=fweditgrid-th-opr>操作</th> 
            </#if>

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
        <div class="fweditgrid">  <!-- 整个grid的外层 -->  
            <div class="fweditgrid-wrap">   <!-- 整个grid的覆盖区 --> 
                <#assign buildPage=(parameters.options.hasPage||(parameters.options.expbtn?length!"")>0) >
                <#if (["TOP","BOTH"]?seq_contains(parameters.pageAlign!"BOTTOM"))&&buildPage >
                <div class="fweditgrid-pager"></div><!-- 整个grid的分页区 -->
                </#if>
                <div class="fweditgrid-view"> <!-- 整个grid的数据区 -->

                     <table  border=0 cellspacing=0 cellpadding=0  width=100% id="${parameters.id}_table"> 
			          <thead>
			  	       <@GLT_HEADER parameters.glt.fixHead![] parameters.glt.colHead />
			          </thead>
			         <tbody> 
					   <@GLT_DATA parameters.options.data.rows />
				     </tbody>
		            </table> 
                 
                </div>
                <#if (["BOTTOM","BOTH"]?seq_contains(parameters.pageAlign!"BOTTOM"))&&buildPage >
                <div class="fweditgrid-pager"></div><!-- 整个grid的分页区 -->
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
    $("#${parameters.id}").fweditgrid(${tojson(parameters.options)});

});

function ${parameters.id}_combox(input,combox){ 
    //只读的不处理
    if(input.className.indexOf('readOnly')!=-1)return;
  $("#${parameters.id}").fweditgrid("showComBox",input,"${parameters.id}_"+combox);
}

function ${parameters.id}_delRow(obj){
    var flag = true;
    var tr = $(obj).closest('TR');
    var iRow = tr.attr("fweditgrid-row-index");

    try {
        var callback = eval("${parameters.id}_delRowCallback ;");

       //调用自定义回调方法
        if (typeof (callback) == 'function'){
            try{
                flag = callback(iRow);
            }catch(ex){
                flag = false;
                FWalert(ex.message);
            }
        }
    
    } catch (e) {
    }
 if(flag){
    $("#${parameters.id}").fweditgrid("removeRows",iRow);
 }
}
    
</script> 
<#compress></#compress>  
