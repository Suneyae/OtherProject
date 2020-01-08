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
	"SFZ":"18",
	"READONLY":"read"
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

<#--准备 生成TD所需的表头定义列（过滤多列表头，仅保留最后有效的一列）-->
<#if (parameters.options.frozenColumns > 0) >
<#assign v_fixHead = parameters.lastHeaderRow[0..(parameters.options.frozenColumns-1)] >
<#assign v_colHead = parameters.lastHeaderRow[(parameters.options.frozenColumns)..]![] >
<#else>
<#assign v_fixHead = [] >
<#assign v_colHead = parameters.lastHeaderRow >
</#if>
<#assign colNames=(parameters.options.data.headers![])>
 
<#--macro 生成TD-->
<#macro GT_TD v_input_comp v_data_one v_row_pos v_col_pos>
<#assign v_type="">
<#assign v_subtype=" ">
<#assign v_readOnly=((v_input_comp.readOnly!"")=="true")?string("readonly=\"readonly\"","") />
<#assign v_addClass=(v_col_pos==0)?string("nupfirstcollum","")>
 <#if !(v_input_comp.type??)>
   <#assign v_type="02"> 
 <#else>
     <#assign v_type=v_input_comp.type?substring(0, 2)>

   <#if (v_input_comp.type?length>2)>
     <#assign v_subtype=v_input_comp.type?substring(2, 3)>
    </#if>
 </#if>
  <#--设置为不可编辑-->
  <#if (v_readOnly!="")>
    <#assign v_type="read">
  </#if>
 <#assign v_comp={
	"type":v_type,
	"subtype":v_subtype!" ",
	"tip":v_input_comp.tip!"", 
	"pname":colNames[v_col_pos]!"",
	"rows":v_input_comp.rows!"",
	"cus":v_input_comp.cus!"",
	"vld":v_input_comp.vld!"",
	"label":v_input_comp.label!"",
	"value":v_input_comp.value!"",
    "text":v_input_comp.text!"",
    "colNum":v_input_comp.colNum!"10",
    "align":v_input_comp.align!"center",
    "onselect":v_input_comp.onselect!""
	}>
	<#if (v_input_comp.maxlength??)>
	  <#assign  v_comp = v_comp + {"maxlength":v_input_comp.maxlength}>
	</#if>
 <td field="${colNames[v_col_pos]}" align="${v_comp.align}" class=fwnupgrid-td> 
 <#assign compValue = v_data_one["_DIC_"+colNames[v_col_pos]]!"">
 <#assign hiddenValue = v_data_one[colNames[v_col_pos]]!"">
 <#if compValue=="">
 <#assign compValue = v_data_one[colNames[v_col_pos]]!"">
 </#if>
 
 <#--全部的控件都不用NAME 用rw来表示行 用nd来表示列名,目的为了减少submit时提交的数据量-->
 <#--myclass 用来新增一行的时候 重新将class=myclass 目的是为了初始化控件-->
 <#switch v_comp.type!"">  
    <#case GtType.READONLY>
    <#case GtType.LABEL>
        <#assign inputTmp=inputSets[v_comp.subtype!" "]>  
        <label class="${v_addClass}" rw="${v_row_pos}" nd="${v_comp.pname}" value="${hiddenValue}">${compValue}</label>
        <input title="${v_comp.tip!""}" type="hidden" class="${inputTmp[1]} ${v_addClass}"  myclass="${inputTmp[1]} ${v_addClass}" maxlength="${v_comp.maxlength!inputTmp[2]}" rw="${v_row_pos}" nd="${v_comp.pname}" value="${hiddenValue}" vldStr="${v_comp.vld!""}"  ${v_comp.cus!""} ${v_readOnly}/>
         <#break>
	<#case GtType.INPUT ><#--处理单行输入控件--> 
		<#assign inputTmp=inputSets[v_comp.subtype!" "]> 
		<#if (compValue?is_sequence)>
			<#assign compValue=(compValue[0]?string)!"">
		</#if>
		<input title="${v_comp.tip!""}" type="${inputTmp[0]}" class="${inputTmp[1]} ${v_addClass}" myclass="${inputTmp[1]} ${v_addClass}" maxlength="${v_comp.maxlength!inputTmp[2]}" rw="${v_row_pos}" nd="${v_comp.pname}" value="${compValue}" vldStr="${v_comp.vld!""}"  ${v_comp.cus!""} ${v_readOnly}/>
     <#break> 
	<#case GtType.SFZ ><#--处理身份证 --> 
		<#if (compValue?is_sequence)>
			<#assign compValue=(compValue[0]?string)!"">
		</#if>
		<input type="text" title="${v_comp.tip!""}" class="idcardbox ${v_addClass}" myclass="idcardbox ${v_addClass}"  maxlength="${v_comp.maxlength!19}" rw="${v_row_pos}" nd="${v_comp.pname}" value="${compValue}" vldStr="${v_comp.vld!""}" ${v_comp.cus!""} ${v_readOnly}/>
		<#break>
	<#case GtType.TEXTAREA ><#--处理多行输入控件 --> 
		<#if (compValue?is_sequence)>
			<#assign compValue=(compValue[0]?string)!"">
		</#if>
		<textarea class="textareabox ${v_addClass}"  myclass="textareabox ${v_addClass}" title="${v_comp.tip!""}" rows="${v_comp.rows!3}" rw="${v_row_pos}" nd="${v_comp.pname}" vldStr="${v_comp.vld!((v_comp.label!"")+"="+"l(0-150)")}" ${v_comp.cus!""} ${v_readOnly}>${compValue}</textarea>
		<#break>
	<#case GtType.DATEPICKER ><#--处理日期控件 --> 
		<#if (compValue?is_sequence)>
			<#assign compValue=(compValue[0]?string)!"">
		</#if>
		<input type="text" class="datebox ${v_addClass}" myclass="datebox ${v_addClass}" title="${v_comp.tip!""}" rw="${v_row_pos}" nd="${v_comp.pname}" vldStr="${v_comp.vld!""}" maxlength="${v_comp.maxlength!50}" value="${compValue}" ${v_comp.cus!""} ${v_readOnly}/> 
		<#break>
	<#case GtType.BUTTON ><#--处理按钮-->
        <#if (compValue?is_sequence)>
            <#assign compValue=(compValue[0]?string)!"">
        </#if>
		<a class="buttonlink ${v_addClass}" myclass="buttonlink ${v_addClass}"  ${v_readOnly} ${v_comp.cus!""} rw="${v_row_pos}" nd="${v_comp.pname}" title="${v_comp.tip!""}" >${compValue}</a>
		<#break>
	<#case GtType.CHECKBOX >
        <#-- 取控件的值-->
        <#if (hiddenValue?is_sequence)>
            <#assign hiddenValue=(hiddenValue[0]?string)!"">
        </#if>
        <#-- 单checkbox处理 -->
        <input title="${v_comp.tip!""}" type="checkbox" class="checkbox ${v_addClass}" ${v_comp.cus!""} name="${v_comp.pname}"
        myclass="checkbox ${v_addClass}" rw="${v_row_pos}" nd="${v_comp.pname!""}" value="${v_comp.value!""}" 
        vldStr="${v_comp.vld!""}" ${(hiddenValue=(v_comp.value!""))?string("checked","")} ${v_readOnly}/>
		<#break>
	<#case GtType.RADIOBOX >
        <#if (hiddenValue?is_sequence)>
            <#assign hiddenValue=(hiddenValue[0]?string)!"">
        </#if>
        <#if (parameters.glt.dict[v_comp.pname])??>
        <#-- 字典项处理 -->
            <#assign dicts=parameters.options.data.dicts[v_comp.pname]!{}>
            <table border=0 cellspacing=0 cellpadding=0 class="inputSet">
            <#list dicts?keys?chunk(((v_comp.colNum)?number)!10) as subkeys>
              <tr>
              <#list subkeys as key>
                <td align="right"  style="border:0">
            <label>${dicts[key]!""}<input title="${v_comp.tip!""}" type="radio" class="radio ${v_addClass}" ${v_comp.cus!""} 
            myclass="radio ${v_addClass}" rw="${v_row_pos}" nd="${v_comp.pname!""}" name="${v_row_pos+(v_comp.pname!"")}" value="${key!""}" 
            ${(hiddenValue=(key!""))?string("checked","")} ${v_readOnly}/></label>
                </td>
              </#list>
              </tr>
            </#list>
            </table>
       <#else>
    	   <label>${v_comp.text}<input type="radio" title="${v_comp.tip!""}" class="radio ${v_addClass}" myclass="radio ${v_addClass}" 
    	   ${v_comp.cus!""} rw="${v_row_pos}" nd="${v_comp.pname}" name="${v_comp.pname}" value="${compValue!""}" 
    	   vldStr="${v_comp.vld!""}" ${(hiddenValue=(v_comp.value!""))?string("checked","")} ${v_readOnly} /></label>
	   </#if>
	   <#break> 
	<#case GtType.COMBOBOX>
	    <#if v_comp.subtype!='b'>
	       <#assign v_readOnly='readOnly=true'>
	    </#if>
	    <#assign inputTmp=inputSets[v_comp.subtype!" "]> 
	    <input type="text"   class="${inputTmp[1]} ${v_addClass}"  myclass="${inputTmp[1]} ${v_addClass}" title="${v_comp.tip!""}" cmb="true" rw="${v_row_pos}" nd="${v_comp.pname}_CMB"  onfocus="${parameters.id}_combox(this,'${v_comp.pname}');" maxlength="${v_comp.maxlength!50}" value="${compValue}" ${v_comp.cus!""} ${v_readOnly} doselect="${v_comp.onselect!""}"<#rt>/> 
	    <input type="hidden" class="gltcom" myclass="gltcom" rw="${v_row_pos}" nd="${v_comp.pname}"  vldStr="${v_comp.vld!""}" value="${hiddenValue}" /> 
	  <#break>
 	<#default>
</#switch>
 
 </td>
</#macro>

<#--生成单行记录的函数 -->          
<#macro GLT_TR tr_rows_arr v_data_one irow>
   <tr class="gltDataTableTr"  fwnupgrid-row-index=${irow}> 
   <#list 0.. (parameters.options.nupRow-1) as repeatRowCnt >
   <#if ((tr_rows_arr?size)>0)> 
      <#list tr_rows_arr as cols>   
	      <@GT_TD v_input_comp=cols 
         	v_data_one=v_data_one[repeatRowCnt]!{}
         	v_row_pos=(irow+repeatRowCnt_index)
         	v_col_pos=cols_index/>
	   </#list> 
   </#if>
</#list>
</tr>
</#macro>



<#assign ifixcol=0>
<#--生成整个表单记录的函数 --> 
<#macro GLT_DATA v_data_arr >
	<#assign emptyComp={}> 
	
    <#if ((v_data_arr?size)>0)> 
         <#list v_data_arr?chunk((parameters.options.nupRow),emptyComp) as nupRows> 
            <#assign nupRows2=nupRows>  
            <@GLT_TR tr_rows_arr=v_colHead
					 v_data_one=nupRows2
					 irow=(nupRows_index*parameters.options.nupRow)
            />
        </#list> 
    <#else>
          <@GLT_TR v_colHead {} 0 /> 
    </#if> 
</#macro>



<#-- 输出列标题 函数--> 
<#macro GLT_COL v_head_row isFixed> 
			<#list v_head_row as col><#--列数据生成 -->
				 <th nowrap="nowrap" rowspan=${col.rowspan!1} colspan=${col.colspan!1} <#if col.align??> _al=${col.align}</#if> sortable=${col.sortable!'true'} 
				 <#if col.width??>style="width=${col.width}"</#if> <#if (col.lastRowIndex??)>lastRowIndex="${col.lastRowIndex}" </#if>
				  <#if isFixed>class=fwnupgrid-th-fixed<#else>class=fwdatagrid-th</#if> > 
				   
						<#if !(col.lastRowIndex??)><#--标题组生成方式 -->
						    <#if col.checkbox??><#--生成checkbox --> 
							  <input type=checkbox class=fwnupgrid-header-check> 
							<#else> 
								${col.title!""}
							</#if> 
						<#else><#--非标题组生成方式 -->  
						    <#if col.checkbox??><#--生成checkbox -->
								  <input type=checkbox class=fwnupgrid-header-check> 
							<#else>
								<span>${col.title!""}</span>
								<span class=fwnupgrid-sort-icon>&nbsp;</span>
							</#if> 
					    </#if> 
			     
				 </th>
			 </#list>  
</#macro>

<#macro GLT_HEADER v_head_colHead>
     <tr class=fwnupgrid-header-tr> 
          <#list 1.. parameters.options.nupRow as repeatRowCnt >
	             <#list v_head_colHead as row>
	               <@GLT_COL row false/>
	               <#break>
	             </#list> 
           </#list> 
      </tr>  
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
        <div class="fwnupgrid">  <!-- 整个grid的外层 -->  
            <div class="fwnupgrid-wrap">   <!-- 整个grid的覆盖区 --> 
                <#assign buildPage=(parameters.options.hasPage||(parameters.options.expbtn?length!"")>0) >
                <#if (["TOP","BOTH"]?seq_contains(parameters.pageAlign!"BOTTOM"))&&buildPage >
                <div class="fwdatagrid-pager"></div><!-- 整个grid的分页区 -->
                </#if>
                <div class="fwnupgrid-view"> <!-- 整个grid的数据区 -->

                     <table  border=0 cellspacing=0 cellpadding=0  width=100%> 
			          <thead>
			  	       <@GLT_HEADER parameters.glt.colHead />
			          </thead>
			         <tbody> 
					   <@GLT_DATA parameters.options.data.rows />
				     </tbody>
		            </table> 
                 
                </div>
                <#if (["BOTTOM","BOTH"]?seq_contains(parameters.pageAlign!"BOTTOM"))&&buildPage >
                <div class="fwnupgrid-pager"></div><!-- 整个grid的分页区 -->
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
	$("#${parameters.id}").fwnupgrid(${tojson(parameters.options)});
	
	FWinitObject('${parameters.id}');
});

function ${parameters.id}_combox(input,combox){ 
    //只读的不处理
    if(input.className.indexOf('readOnly')!=-1)return;
  $("#${parameters.id}").fwnupgrid("showComBox",input,"${parameters.id}_"+combox);
}
	
</script> 