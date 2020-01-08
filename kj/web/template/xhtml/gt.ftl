<#-- 提取java枚举类型-->
<#assign GtType= enum["cn.sinobest.framework.service.tags.Gt$GtType"] >
<#-- 准备用户右对齐的类型，目前知道的是 文本、单选和多选-->
<#assign rightAlign= [GtType.LABEL,GtType.CHECKBOX,GtType.RADIOBOX] >
<#-- 准备动态字典-->
<#assign gtDicts= parameters.gtDicts >
<#-- 准备值-->
<#assign gtValues= parameters.gtValues >
<#-- 查询条件集合-->
<#assign whereClsMap= parameters.whereClsMap!{} >
<#-- 多记录表配置集合-->
<#assign gltMap= parameters.gltMap!{} >
<#-- 快速字典默认表头定义-->
<#assign COMBOBOX_P_HEADER =
    [{"field":"AAA102","title":"字典代码","width":60},
    {"field":"AAA103","title":"字典值","width":100},
    {"field":"AAA101","title":"分类名称","width":120},
    {"field":"AAA100","title":"分类代码","width":100}]
>
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
    "q":["text","searchgrid-text 05q",1000],<#-- 不能粘贴的输入框-->
    "y":["text","bankaccount",23],<#-- 银行账号每4位隔开 -->
    " ":["text","textbox",50]<#-- 默认-->
    }>
<#assign multipleNames = []>
<#--解析单记录表的控件内容 -->
<#macro GT_COMPOMENT v_comp>
<#--控件只读判断 -->
<#assign v_readOnly=((parameters.readOnly!false)||((v_comp.readOnly!"")=="true"))?string("disabled","") />
<#--分控件类型处理 -->
<#switch v_comp.type!"">
    <#case GtType.LABEL ><#--处理文本 -->
        ${v_comp.label!"&nbsp;"}
        <#if v_comp.fdj??><#--处理放大镜 -->
            <span style="padding-left:2px" title="${v_comp.fdjhint!"单击可以打开查询窗口"
            }"><a href="javascript:void(0)" class="fdj" tabindex="10000" onclick="${v_comp.fdj!"void(0)"}"></a></span>
        </#if>
        <#break>
    <#case GtType.INPUT ><#--处理单行输入控件-->
        <#assign inputTmp=inputSets[v_comp.subtype!" "]>
        <#assign compValue=gtValues[v_comp.pname]!"">
        <#if (compValue?is_sequence)>
            <#assign compValue=(compValue[0]?string)!"">
        </#if>
        <#switch v_comp.subtype!"">
            <#case "q">
        <input title="${v_comp.tip!""}" type="${inputTmp[0]}" class="${inputTmp[1]}" name="${v_comp.pname!""
        }"  vldStr="${v_comp.vld!""}" ${v_comp.cus!""} ${v_readOnly} value=${compValue} 
        >
            <script language="javascript">
            $(function(){
                var arr=[];
                var columnheaders=${(tojson(gltMap[v_comp.glt][0].colHead))!"{}"};
                <#-- 转化列定义，用列表展现-->
                jQuery.each(columnheaders,function(indexInArray, valueOfElement){
                    jQuery.each(valueOfElement,function(indexInArray, valueOfElement2){
                        if(typeof valueOfElement2['field']=='undefined'){
                            valueOfElement2['field']=(1+indexInArray).toString();
                        }
                    });
                });
                $('table#${parameters.id} .05q[name="${v_comp.pname!""}"]').filter(function(index){
                    //避免重复初始化
                    return $(this).data('_hasinit')==null;
                }).data('_hasinit',true).searchgrid({
                    confid:${tojson(v_comp.glt)!"null"},
                    whereCls:${(tojson(whereClsMap[v_comp.glt]))!"null"},
                    url:"${request.contextPath!""}/gltPage.do",
                    localdb:${v_comp.localdb!"false"},
                    remotedb:${v_comp.remotedb!"false"},
                    onselect:${v_comp.onselect!"null"},
                    width:"${v_comp.ddWidth!"auto"}",
                    height:"${v_comp.ddHeight!"auto"}",
                    fieldIndices:[0,0],
                    textFieldOnly:true,
                    columns:columnheaders
                });
            });
            </script>
                <#break>
            <#default>
        <input title="${v_comp.tip!""}" type="${inputTmp[0]}" class="${inputTmp[1]}" maxlength="${v_comp.maxlength!inputTmp[2]}" name="${v_comp.pname!""
        }" value="${compValue}" vldStr="${v_comp.vld!""}" ${v_comp.cus!""} ${v_readOnly}/>
                <#break>
        </#switch>
     <#break>
    <#case GtType.SFZ ><#--处理身份证 -->
        <#assign compValue=gtValues[v_comp.pname]!"">
        <#if (compValue?is_sequence)>
            <#assign compValue=(compValue[0]?string)!"">
        </#if>
        <input type="text" title="${v_comp.tip!""}" class="idcardbox" maxlength="${v_comp.maxlength!19}" name="${v_comp.pname!""}" value="${
        compValue}" vldStr="${v_comp.vld!""}" ${v_comp.cus!""} ${v_readOnly}/>
        <#break>
    <#case GtType.TEXTAREA ><#--处理多行输入控件 -->
        <#assign compValue=gtValues[v_comp.pname]!"">
        <#if (compValue?is_sequence)>
            <#assign compValue=(compValue[0]?string)!"">
        </#if>
        <#switch v_comp.subtype!""><#case "q">
        <textarea class="textareabox 03q" title="${v_comp.tip!""}" rows="${v_comp.rows!3}" name="${v_comp.pname}" vldStr="${
        v_comp.vld!((v_comp.label!"")+"="+"l(0-150)")}" ${v_comp.cus!""} ${v_readOnly}>${compValue}</textarea>
            <script language="javascript">
            $(function(){
                var arr=[];
                var columnheaders=${(tojson(gltMap[v_comp.glt][0].colHead))!"{}"};
                <#-- 转化列定义，用列表展现-->
                jQuery.each(columnheaders,function(indexInArray, valueOfElement){
                    jQuery.each(valueOfElement,function(indexInArray, valueOfElement2){
                        if(typeof valueOfElement2['field']=='undefined'){
                            valueOfElement2['field']=(1+indexInArray).toString();
                        }
                    });
                });
                $('table#${parameters.id} .03q[name="${v_comp.pname!""}"]').filter(function(index){
                    //避免重复初始化
                    return $(this).data('_hasinit')==null;
                }).data('_hasinit',true).searchgrid({
                    confid:${tojson(v_comp.glt)!"null"},
                    whereCls:${(tojson(whereClsMap[v_comp.glt]))!"null"},
                    url:"${request.contextPath!""}/gltPage.do",
                    localdb:${v_comp.localdb!"false"},
                    remotedb:${v_comp.remotedb!"false"},
                    onselect:${v_comp.onselect!"null"},
                    width:"${v_comp.ddWidth!"auto"}",
                    height:"${v_comp.ddHeight!"auto"}",
                    fieldIndices:[0,0],
                    textFieldOnly:true,
                    columns:columnheaders
                });
            });
            </script>
            <#break>
            <#default>
        <textarea class="textareabox" title="${v_comp.tip!""}" rows="${v_comp.rows!3}" name="${v_comp.pname}" vldStr="${
        v_comp.vld!((v_comp.label!"")+"="+"l(0-150)")}" ${v_comp.cus!""} ${v_readOnly}>${compValue}</textarea>
                <#break>
        </#switch>
        <#break>
    <#case GtType.COMBOBOX ><#--处理字典控件 -->
        <#assign compValue=gtValues[v_comp.pname]!"">
        <#if (compValue?is_sequence)>
            <#assign compValue=(compValue[0]?string)!"">
        </#if>
        <#switch v_comp.subtype!"">
            <#case "p">
                <input type="text" title="${v_comp.tip!""}" class="searchgridbox"  subtype="${v_comp.subtype}" name="${v_comp.pname!""
                }" textfield="${(gtDicts[v_comp.dname][compValue].AAA103)!""}" value="${
                compValue!"10"}" ${v_comp.cus!""} vldStr="${v_comp.vld!""}" ${v_readOnly}>
                <script language="javascript">
                $(function(){
                    <#if v_comp.colHeads??>
                    var columnheaders=${tojson(v_comp.colHeads)};
                    <#else>
                    var columnheaders=
    [{"field":"AAA102","title":"字典代码","width":60},
    {"field":"AAA103","title":"字典值","width":100},
    {"field":"AAA101","title":"分类名称","width":120},
    {"field":"AAA100","title":"分类代码","width":100}];
                    </#if>
                    var dicts=${tojson(gtDicts[v_comp.dname!""])!"null"};
                    var arr=[];
                    <#-- 转化字典，用列表展现-->
                    for(key in dicts){
                        arr.push(dicts[key]);
                    }
                    $('table#${parameters.id} .searchgridbox[name="${v_comp.pname!""}"]').filter(function(index){
                        //避免重复初始化
                        return $(this).data('_hasinit')==null;
                    }).data('_hasinit',true).searchgrid({
                        width:"${v_comp.ddWidth!"auto"}",
                        height:"${v_comp.ddHeight!"auto"}", 
                        idField:'AAA102',
                        textField:'AAA103',
                        initdata:{"rows":arr},
                        columns:[columnheaders]
                    });
                });
                </script>
                <#break>
            <#case "q">
                <#assign disaplayValue=gtValues['_DIC_'+v_comp.pname]!"">
                <#if (disaplayValue?is_sequence)>
                    <#assign disaplayValue=(disaplayValue[0]?string)!"">
                </#if>
                <input type="text" title="${v_comp.tip!""}"  class="searchgridbox"  subtype="${v_comp.subtype}" name="${v_comp.pname!""
                }" textfield="${(gtDicts[v_comp.dname][compValue].AAA103)!""}" value="${
                compValue}" ${v_comp.cus!""} vldStr="${v_comp.vld!""}" ${v_readOnly} displayValue="${disaplayValue}">
                <script language="javascript">
                $(function(){
                    var arr=[];
                    var columnheaders=${(tojson(gltMap[v_comp.glt][0].colHead))!{}};
                    <#-- 转化列定义，用列表展现-->
                    jQuery.each(columnheaders,function(indexInArray, valueOfElement){
                        jQuery.each(valueOfElement,function(indexInArray, valueOfElement2){
                            if(typeof valueOfElement2['field']=='undefined'){
                                valueOfElement2['field']=(1+indexInArray).toString();
                            }
                        });
                    });
                    $('table#${parameters.id} .searchgridbox[name="${v_comp.pname!""}"]').filter(function(index){
                        //避免重复初始化
                        return $(this).data('_hasinit')==null;
                    }).data('_hasinit',true).searchgrid({
                        confid:${tojson(v_comp.glt)!"null"},
                        whereCls:${(tojson(whereClsMap[v_comp.glt]))!"null"},
                        url:"${request.contextPath!""}/gltPage.do",
                        localdb:${v_comp.localdb!"false"},
                        remotedb:${v_comp.remotedb!"false"},
                        onselect:${v_comp.onselect!"null"},
                        width:"${v_comp.ddWidth!"auto"}",
                        height:"${v_comp.ddHeight!"auto"}",
                        fieldIndices:[0,1],
                        columns:columnheaders
                    });
                });
                </script>
                <#break>
<#-- 下面的子类型暂时不用
            <#case "c">
                <#if (v_comp.comtype!"")=="parent">
                <select class="ldcombox" title="${v_comp.tip!""}"  comtype="parent" childname="${v_comp.childname!""}" name="${v_comp.pname!""
                }" vldStr="${v_comp.vld!""}" ${v_comp.cus!""} ${v_readOnly}>
                <#else>
                <select class="ldcombox" title="${v_comp.tip!""}"  comtype="child" parentname="${v_comp.parentname!""}" name="${v_comp.pname!""
                }" vldStr="${v_comp.vld!""}" ${v_comp.cus!""} ${v_readOnly}>
                </#if>
                    <#list (gtDicts[v_comp.dname]!{})?values as dict>
                    <option value="${dict.AAA102!""}" ${(dict.AAA102==(compValue?string))?string("selected","")
                    }>${dict.AAA103!""}</option>
                    </#list>
                    <option value="" ${(""==(compValue?string))?string("selected","")}>&nbsp;</option>
                </select>
                <#break>
-->
            <#case "b">
                <select class="dbcombox" title="${v_comp.tip!""}" dname="${v_comp.dname!""}" url="${request.contextPath!""}/dictSearch.do" 
                name="${v_comp.pname!""}" vldStr="${v_comp.vld!""}" ${v_comp.cus!""} ${v_readOnly}
                fwpanelWidth="${v_comp.ddWidth!"auto"}" <#rt>
                fwpanelHeight="${v_comp.ddHeight!"auto"}" <#rt>
                onselect="${v_comp.onselect!""}"<#rt>>
                </select>
                <#break>
            <#case "s">
                <select class="combobox" title="${v_comp.tip!""}" name="${v_comp.pname!""}" dname="${v_comp.dname!""}" vldStr="${v_comp.vld!""}" ${v_comp.cus!""} ${v_readOnly}
                fwpanelWidth="${v_comp.ddWidth!"auto"}" <#rt>
                fwpanelHeight="${v_comp.ddHeight!"auto"}"  <#rt>
                onselect="${v_comp.onselect!""}"<#rt> 
                style="display:none;" dictType="s" >
                    <#list (gtDicts[v_comp.dname]!{})?values as dict>
                    <option value="${dict.AAA102!""}" ${(dict.AAA102==(compValue?string))?string("selected","")
                    }>${dict.AAA103!""}</option>
                    </#list>
                    <option value="" ${(""==(compValue?string))?string("selected","")}>&nbsp;</option>
                </select>
                <SPAN  class=fwcombox >
                    <INPUT  class="fwcombox-text" autocomplete="off" ><SPAN class=fwcombox-arrow ></SPAN>
                </SPAN>
                <#break>
            <#default>
                <select class="incombox" title="${v_comp.tip!""}" name="${v_comp.pname!""}" dname="${v_comp.dname!""}"  vldStr="${v_comp.vld!""}" 
                ${v_comp.cus!""} ${v_readOnly} displayCol="${v_comp.displayCol!"AAA103"}" <#rt>
                fwpanelWidth="${v_comp.ddWidth!"auto"}" <#rt>
                fwpanelHeight="${v_comp.ddHeight!"auto"}" <#rt>
                onselect="${v_comp.onselect!""}" whereCls=${(tojson(whereClsMap[v_comp.dname]))!""} <#rt>
                style="display:none;" dictType="${v_comp.dictType!"default"}" >
                  <#list (gtDicts[v_comp.dname]!{})?values as dict>
                        <#if dict.AAA102==(compValue?string)>
                            <option value="${dict.AAA102!""}" selected >${dict.AAA102!""}-${dict.AAA103!""}</option>
                            <#break>
                        </#if>
                  </#list>
                  <option value=""></option>
                </select>
                <SPAN  class=fwcombox >
                    <INPUT  class="fwcombox-text" autocomplete="off" ><SPAN class=fwcombox-arrow ></SPAN>
                </SPAN>
                <#break>
        </#switch>
        <#break>
    <#case GtType.DATEPICKER ><#--处理日期控件 -->
        <#assign compValue=gtValues[v_comp.pname]!"">
        <#if (compValue?is_sequence)>
            <#assign compValue=(compValue[0]?string)!"">
        </#if>
        <input type="text" class="datebox" title="${v_comp.tip!""}" name="${v_comp.pname!""}" vldStr="${v_comp.vld!""}" maxlength="${
        v_comp.maxlength!50}" value="${compValue}" ${v_comp.cus!""} ${v_readOnly}/> 
        <#break>
    <#case GtType.FILE ><#--处理上传控件 -->
        <#assign compValue=gtValues[v_comp.pname]!"">
        <#if (compValue?is_sequence)>
            <#assign compValue=(compValue[0]?string)!"">
        </#if>
        <input type="file" class="filebox" title="${v_comp.tip!""}" name="${v_comp.pname!""}" vldStr="${v_comp.vld!""}" maxlength="${
        v_comp.maxlength!50}" value="${compValue}" ${v_comp.cus!""} ${v_readOnly}/> 
        <#break>
    <#case GtType.LINK ><#--处理超级链接 -->
        <a href="${v_comp.href!("javascript:void(0)")}" ${v_comp.cus!""} ${v_readOnly} title="${v_comp.tip!""}" >${v_comp.label}</a>
        <#break>
    <#case GtType.BUTTON ><#--处理按钮-->
        <a class="buttonlink" href="${v_comp.href!("javascript:void(0)")}" ${v_readOnly} ${v_comp.cus!""} title="${v_comp.tip!""}" >${v_comp.label}</a>
        <#break>
    <#case GtType.IMG ><#--处理图片-->
        <img title="${v_comp.tip!""}" src="${request.contextPath}${v_comp.src!""}" ${v_comp.cus!""} alt="${v_comp.label}" ${v_readOnly}></img>
        <#break>
    <#case GtType.CHECKBOX >
        <#-- 标记该控件多值-->
        <#if !multipleNames?seq_contains(v_comp.pname)>
        <#assign multipleNames=multipleNames+[v_comp.pname]>
        </#if>
        <#-- 取控件的值-->
        <#assign compValue=gtValues[v_comp.pname]![]>
        <#if !(compValue?is_sequence)>
            <#assign compValue=[compValue?string]>
        </#if>
        <#if v_comp.dname??>
            <table class="inputSet">
            <#list (gtDicts[v_comp.dname]!{})?values?chunk(((v_comp.colNum)?number)!10) as dicts>
              <tr>
              <#list dicts as dict>
                <td align="right">
                <label>${dict.AAA103!""}<input title="${v_comp.tip!""}" type="checkbox" class="checkbox" ${v_comp.cus!""} name="${v_comp.pname!""}" value="${dict.AAA102!""}" ${
                (compValue?seq_contains(dict.AAA102))?string("checked","")} ${v_readOnly}/></label>
                </td>
              </#list>
              </tr>
            </#list>
            </table>
        <#else>
            <label>${v_comp.text}<input title="${v_comp.tip!""}" type="checkbox" class="checkbox" ${v_comp.cus!""} name="${v_comp.pname!""}" value="${v_comp.value!""}" vldStr="${v_comp.vld!""}" ${
        (compValue?seq_contains(v_comp.value!""))?string("checked","")} ${v_readOnly}/></label>
        </#if>
        <#break>
    <#case GtType.RADIOBOX >
        <#assign compValue=gtValues[v_comp.pname]![]>
        <#if !(compValue?is_sequence)>
            <#assign compValue=[compValue?string]>
        </#if>
        <#if v_comp.dname??>
            <table class="inputSet">
            <#list (gtDicts[v_comp.dname]!{})?values?chunk(((v_comp.colNum)?number)!10) as dicts>
            <tr>
            <#list dicts as dict>
                <td><label>${dict.AAA103!""}<input type="radio" title="${v_comp.tip!""}" class="checkbox" ${v_comp.cus!""} name="${v_comp.pname!""}" value="${dict.AAA102!""}" ${
        (compValue?seq_contains(dict.AAA102))?string("checked","")} ${v_readOnly}/></label></td>
            </#list>
            </tr>
            </#list>
            </table>
        <#else>
            <label>${v_comp.text}<input type="radio" title="${v_comp.tip!""}" class="checkbox" ${v_comp.cus!""} name="${v_comp.pname!""}" value="${v_comp.value!""}" vldStr="${v_comp.vld!""}" ${
        (compValue?seq_contains(v_comp.value!""))?string("checked","")} ${v_readOnly}/></label>
        </#if>
        <#break>
    <#case GtType.NESTTABLE >
        <@GT_TABLE_CONTENT v_comp.gt true/>
        <#break>
    <#default>
</#switch>
</#macro>
<#--解析单记录表的表单内容模板-->
<#macro GT_TABLE_CONTENT v_gt v_nested>
    <#if !v_nested>
    <table class="datatable" cellpadding="0" cellspacing="0"  border=0 align="center" name="${v_gt.id}1"
        id="${v_gt.id}" width=98%>
    <#else>
    <table class="datatable" cellpadding="0" cellspacing="0"  border=0 align="center" >
    </#if>
    <#-- 计算单记录表中一个td的默认宽度 -->
    <#assign def_td_width= ((v_gt.colNum)/100)?string("percent") >
    <#list v_gt.renders as row><#--行数据生成 -->
        <tr>
        <#list row as col><#--列数据生成 -->
            <td width="${col[0].width!def_td_width}" rowspan="${col[0].rowspan!1}" colspan="${col[0].colspan!1}" class="${
            rightAlign?seq_contains(col[0].type!"")?string("tdprompt","tdinput")} ${col[0].nClass!""}" <#rt>
            <#if col[0].align??>style="text-align:${col[0].align};"</#if> >
            <#list col as comp>
                <@GT_COMPOMENT comp />
            </#list>
            </td>
        </#list>
        </tr>
    </#list>
    </table>
    <#-- 生成单记录表HIDDENT区域-->
    <#if v_gt.hiddens??>
        <#list v_gt.hiddens as hidden>
        <#assign compValue=gtValues[hidden]!"">
        <#if (compValue?is_sequence)>
            <#assign compValue=compValue[0]?string!"">
        </#if>
        <input type="hidden" name="${hidden}" value="${(compValue)}"/>
        </#list>
    </#if>
</#macro>
<#-- 开始生成单记录表脚本-->

<#--若没有标题也没有边框，就不需要DIV也不需要fieldset-->
<#if (parameters.hasTitle!true)||(parameters.hasBorder!true) >

    <#--只要标题不要边框-->
    <#if parameters.hasBorder!true >
      <fieldset class="fieldset" align="center"  style="cursor:default;width:95%" >
    <#else>
       <fieldset  align="center"  style="border:none;cursor:default;width:95%" >
    </#if>

    <#if parameters.hasTitle!true ><#-- 生成标题-->
        <legend class="legend" style="cursor:hand;" >
            <span onclick="FWshowTable('img_${parameters.id}1','div_${parameters.id}')" >
                <img id="img_${parameters.id}1" src="${request.contextPath}/themes/default/images/query_icon_right.gif"></span>
                <span title="单击展开或收缩查询条件" onclick="FWshowTable('img_${parameters.id}1','div_${parameters.id}')">
                ${parameters.title!(parameters.gt.description!'')}
            </span>
        </legend>
    
    </#if>
    
     <#if parameters.hasBorder!true ><#-- 生成边框-->
        <div id="div_${parameters.id}" class="tablediv">
    <#else>
         <div id="div_${parameters.id}"> 
    </#if>
<#else>
 <div> <#-- 没有边框和标题时需要加一个DIV否则 排在它后面的标签能居中-->
</#if>


<@GT_TABLE_CONTENT parameters.gt false/>
<#-- 标识哪那些是可以多值的-->
<input type="hidden" name="_multiple" value="<#rt/>
<#list multipleNames as name>${name}<#if name_has_next>,</#if></#list>">
<#--若没有标题也没有边框，就不需要DIV-->
 <#if (parameters.hasTitle!true)||(parameters.hasBorder!true) >
    </div>
 </#if>

<#--若没有标题也没有边框，就不需要fieldset-->
 <#if (parameters.hasTitle!true)||(parameters.hasBorder!true) >
  </fieldset>
 <#else>
 </div>
 </#if>
<#--根据配置显示归档查询按钮-->
<#if (parameters.hasArchive) && (parameters.permitArchive) >
   	<#--<div id="${parameters.id}_archive_div" style="position:absolute;z-index:0;left:0px;top:0px;width:100px;height:100px;display:block;">
   		<div style="position:absolute;">
			<a class="buttonlink" href="javascript:void(0)" name="btnQuery" onclick="queryArchive_${parameters.id}(${parameters.id},GetForm($('#${parameters.id}').closest('form')));return false;">归档查询</a>
		</div>-->
	</div>
	<script>
		<#-- 动态加载单点登陆所需JS -->
		function queryArchive_${parameters.id}(id,data){
			loadScript({
				url:contextPath + "/js/fw/encrypt.js",
				beforeFunc: function(){return null==document.getElementById("fwencrypt");},
				afterFunc: function(script){
					if(null != script){
						script.id = "fwencrypt";
					}
					loadScript({
						url:contextPath + "/js/fw/encrypt2.js",
						beforeFunc: function(){return null==document.getElementById("fwencrypt2");},
						afterFunc: function(script){
							if(null != script){
								script.id = "fwencrypt2";
							}
							<#--回调单点登陆 -->
							queryArchive_${parameters.id}_login(id,data);
						}
					});
				}
			});
		}
		<#--单点登陆 -->
		function queryArchive_${parameters.id}_login(id,data){
			ssoLogin("${parameters.archiveUrl}",readCookie("LOGINID"),getSsoUid(),
				function(jsonpdata){
					if(data.FHZ<0){
						alert(jsonpdata.MSG);
						return;
					}else{
						<#--回调归档查询 -->
						_queryArchive_${parameters.id}(id,data);			
					}
				}
			);
		}
		<#--真正的归档查询的代码,主要是拼接url参数字符串，然后在新窗口打开-->
		function _queryArchive_${parameters.id}(id,data){
				var querystr = "";
				for(var key in data){
					querystr += "&"+key+"="+encodeURIComponent(data[key]);
				}
				querystr += "&archiveGtId=" + encodeURIComponent(id);
				<#--检查gt是否在一个Tab里面，如果是在一个Tab里面，则获取Tab的顺序号，为了让归档页面自动打开相应的Tab-->
				var $tabPanel = $('#${parameters.id}').closest('div.ui-tabs-panel');
				if(0 != $tabPanel.size()){
					var tabPanelId = $tabPanel.attr("id");
					var number = parseInt(tabPanelId.substring(tabPanelId.lastIndexOf("-")+1),10)-1;
					querystr += "&selectedTabNo=" + number;
				}
				var href = document.location.href;
				if(href.indexOf('?')==-1 && querystr.length>2){
					querystr = '?' + querystr.substring(1);
				}
				querystr += "&t="+new Date().getTime();
				var destHref = "";
				for(var i=0,count=0,len=href.length;i<len;i++){
					if('/'==href.charAt(i)){
						count++;
					}
					if(4==count){
						destHref = "${parameters.archiveUrl}"+href.substring(i)+querystr;
						break;
					}
				}
				var url = destHref;
				var position = "resizable:1;status:0;help:0;scroll:1;center:1;dialogWidth:1024px;dialogHeight:600px";
				window.showModelessDialog(url,window,position);
			}
	</script>
</#if>
<#-- 生成单记录表对应js脚本的区域-->
<script language="javascript">
$(function(){
    if($(${parameters.id}).data('_init')){return;}
    //标志已经初始化，无需重新初始化
    $(${parameters.id}).data('_init',true);
    <#if !(parameters.showContent!true)>
    FWshowTable('img_${parameters.id}1','div_${parameters.id}')
    </#if>
    FWinitObject('${parameters.id}');
    //响应窗体的重新排列事件
    $(window).resize(function(){FWresizeObject(${parameters.id});});
    //最后再重新排列下，原字典控件的宽度可能会变化，而导致不对齐
    FWresizeObject(${parameters.id});
	<#--归档查询按钮定位-->
    <#if (parameters.hasArchive) && (parameters.permitArchive) >
    	<#--在收缩或者展开fieldset的时候是否需要同时调整归档查询按钮的位置-->
    	var needMoveArchiveBtn = "true";
    	<#if !(parameters.hasTitle!true)&&!(parameters.hasBorder!true) >
    		var _archiveTop  = $("#${parameters.id}").offset().top + $("#${parameters.id}").outerHeight()+5	;
		  	var _archiveLeft = $("#${parameters.id}").offset().left + $("#${parameters.id}").outerWidth()-84;
		  	${parameters.id}_archive_div.style.top  = _archiveTop;
		  	${parameters.id}_archive_div.style.left = _archiveLeft;
		  	needMoveArchiveBtn = "false";<#--由于gt没有配置fieldset，所以归档按钮的层是位于开发人员自定义的fieldset里面，所以不需要重新定位归档按钮-->
		<#else>
			var _archiveTop  = $("#${parameters.id}").offset().top + $("#${parameters.id}").outerHeight()+8	;
		  	var _archiveLeft = $("#${parameters.id}").offset().left + $("#${parameters.id}").outerWidth()-74;
		  	${parameters.id}_archive_div.style.top  = _archiveTop;
		  	${parameters.id}_archive_div.style.left = _archiveLeft;
    	</#if>
	  	<#--在包围框fieldset设置单记录表的ID-->
	  	var $parent = $("#${parameters.id}");
	  	while($parent != null && $parent.get(0).tagName != null){
	  		if("fieldset" == $parent.get(0).tagName.toLowerCase()){
	  			$parent.get(0).fwgtid = "${parameters.id}";
	  			$parent.get(0).needMoveArchiveBtn = needMoveArchiveBtn;
	  			break;
	  		}else{
	  			$parent = $parent.parent();
	  		}
	  	}
  	</#if>
});
</script> 
