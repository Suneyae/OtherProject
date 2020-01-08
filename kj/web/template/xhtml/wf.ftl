<#assign hnisi=JspTaglibs["/WEB-INF/hnisi.tld"] />
<#assign serviceObject=parameters.serviceObject!"workflowProxyService.doWork">
<#assign isShowBtnSave=parameters.isShowSavaBtn!false>
<#assign isShowUploadFile=parameters.isShowUploadFile!false>
<#assign isDownFile=parameters.isDownFile!false>
<#assign isDisabled=parameters.isDisabled!false>
<#assign isDialog=parameters.isDialog!true>
<#assign toApplyOpr=parameters.toApplyOpr!"false">
<#assign getItemOpr=parameters.getItemOpr!"false">
<#assign isUploadFile=parameters.isUploadFile!false>
<#assign btnUploadFile = {"id":"wf_btn_upload","name":"上传影像","keycomb":"","desc":"上传影像文件"} >
<#assign btnExit = {"id":"wf_btn_exit","name":"退单","keycomb":"","desc":"回退本次办理的业务"} >
<#assign btnSave = {"id":"wf_btn_save","name":"保存","keycomb":"","desc":"保存修改,返回当前页面"} >
<#assign btnNext = {"id":"wf_btn_next","name":"提交","keycomb":"","desc":"将任务提交给下一个操作员"} >
<#assign btnBack = {"id":"wf_btn_back","name":"回退","keycomb":"","desc":"将业务回退给前任务操作员"} >
<#assign btnEnd =  {"id":"wf_btn_end","name":"结束","keycomb":"","desc":"本流程结束，数据生效"} >
<#-- 提取java枚举类型-->
<#assign ShowType= enum["cn.sinobest.framework.web.tags.WfTag$ShowType"] >

<#macro M_SHOWBOTTON showType formname >
    
    <#switch showType!"">
        
        <#case ShowType.START_END >   <#--只有一个环节 --> 
                <#if isShowUploadFile >
                        <a class="buttonlink wf_btn" name="${btnUploadFile.id}" keycomb="${btnUploadFile.keycomb}" onclick="_openYxzl(this,'${formname}');return false;" href="javascript:void(0);" title="${btnUploadFile.desc}" >${btnUploadFile.name}</a>
                </#if>
                <#if  parameters.pid?default('') != '' > 
                    <a class="buttonlink wf_btn" name="${btnExit.id}" keycomb="${btnExit.keycomb}" onclick="backOrder(this,'${formname}');return false;" href="javascript:void(0);" title="${btnExit.desc}" >${btnExit.name}</a>
                </#if>
                <#if isShowBtnSave >
                    <a class="buttonlink wf_btn" name="${btnSave.id}" keycomb="${btnSave.keycomb}" onclick="wfSave(this,'${formname}');return false;" href="javascript:void(0);" title="${btnSave.desc}" >${btnSave.name}</a>
                </#if>
                <a class="buttonlink wf_btn" name="${btnEnd.id}" keycomb="${btnEnd.keycomb}" onclick="btnEnd(this,'${formname}');return false;"  href="javascript:void(0);" title="${btnEnd.desc}">${btnEnd.name}</a>              
            <#break>
            
        <#case ShowType.START >   <#--开始环节 -->
                <#if isShowUploadFile >
                        <a class="buttonlink wf_btn" name="${btnUploadFile.id}" keycomb="${btnUploadFile.keycomb}" onclick="_openYxzl(this,'${formname}');return false;" href="javascript:void(0);" title="${btnUploadFile.desc}" >${btnUploadFile.name}</a>
                </#if> 
                <#if  parameters.pid?default('') != '' >
                    <a class="buttonlink wf_btn" name="${btnExit.id}" keycomb="${btnExit.keycomb}" onclick="backOrder(this,'${formname}');return false;" href="javascript:void(0);" title="${btnExit.desc}" >${btnExit.name}</a>
                </#if>
                <#if isShowBtnSave >
                    <a class="buttonlink wf_btn" name="${btnSave.id}" keycomb="${btnSave.keycomb}" onclick="wfSave(this,'${formname}');return false;" href="javascript:void(0);" title="${btnSave.desc}" >${btnSave.name}</a>
                </#if>
                <a class="buttonlink wf_btn" name="${btnNext.id}"  keycomb="${btnNext.keycomb}" onclick="wfNext('${formname}',${isDialog?string});return false;" href="javascript:void(0);" title="${btnNext.desc}">${btnNext.name}</a>
            
            <#break>
        <#case ShowType.MIDDLE >  <#--中间环节 -->
               <#if isShowUploadFile >
                    <a class="buttonlink wf_btn" name="${btnUploadFile.id}" keycomb="${btnUploadFile.keycomb}" onclick="_openYxzl(this,'${formname}');return false;" href="javascript:void(0);" title="${btnUploadFile.desc}" >${btnUploadFile.name}</a>
                </#if>
                <a class="buttonlink wf_btn" name="${btnBack.id}" keycomb="${btnBack.keycomb}" onclick="wfBack('${formname}');return false;" href="javascript:void(0);" title="${btnBack.desc}">${btnBack.name}</a>
                <#if isShowBtnSave >
                    <a class="buttonlink wf_btn" name="${btnSave.id}" keycomb="${btnSave.keycomb}" onclick="wfSave(this,'${formname}');return false;" href="javascript:void(0);" title="${btnSave.desc}">${btnSave.name}</a>
                </#if>
                <a class="buttonlink wf_btn" name="${btnNext.id}" keycomb="${btnNext.keycomb}" onclick="wfNext('${formname}',${isDialog?string});return false;" href="javascript:void(0);" title="${btnNext.desc}" >${btnNext.name}</a>
            <#break>
            
        <#case ShowType.END >     <#-- 结束环节 -->
                <#if isShowUploadFile >
                    <a class="buttonlink wf_btn" name="${btnUploadFile.id}" keycomb="${btnUploadFile.keycomb}" onclick="_openYxzl(this,'${formname}');return false;" href="javascript:void(0);" title="${btnUploadFile.desc}" >${btnUploadFile.name}</a>
                </#if>
                <a class="buttonlink wf_btn" name="${btnBack.id}" keycomb="${btnBack.keycomb}" onclick="wfBack('${formname}');return false;" href="javascript:void(0);" title="${btnBack.desc}">${btnBack.name}</a>
                <#if isShowBtnSave >
                    <a class="buttonlink wf_btn" name="${btnSave.id}" keycomb="${btnSave.keycomb}" onclick="wfSave(this,'${formname}');return false;" href="javascript:void(0);" title="${btnSave.desc}">${btnSave.name}</a>
                </#if>
                <a class="buttonlink wf_btn" name="${btnEnd.id}" keycomb="${btnEnd.keycomb}" onclick="btnEnd(this,'${formname}');return false;"  href="javascript:void(0);" title="${btnEnd.desc}">${btnEnd.name}</a>
                
            <#break>
            
        <#case ShowType.MULTI >     <#-- 混合 -->
               <#if isShowUploadFile >
                    <a class="buttonlink wf_btn" name="${btnUploadFile.id}" keycomb="${btnUploadFile.keycomb}" onclick="_openYxzl(this,'${formname}');return false;" href="javascript:void(0);" title="${btnUploadFile.desc}" >${btnUploadFile.name}</a>
                </#if>
                <a class="buttonlink wf_btn" name="${btnBack.id}" keycomb="${btnBack.keycomb}" onclick="wfBack('${formname}');return false;"  href="javascript:void(0);" title="${btnBack.desc}">${btnBack.name}</a>
                <#if isShowBtnSave >
                    <a class="buttonlink wf_btn" name="${btnSave.id}" keycomb="${btnSave.keycomb}" onclick="wfSave(this,'${formname}');return false;"  href="javascript:void(0);" title="${btnSave.desc}">${btnSave.name}</a>
                </#if>
                <a class="buttonlink wf_btn" name="${btnEnd.id}" keycomb="${btnEnd.keycomb}" onclick="btnEnd(this,'${formname}');return false;"  href="javascript:void(0);" title="${btnEnd.desc}">${btnEnd.name}</a>
                <a class="buttonlink wf_btn" name="${btnNext.id}" keycomb="${btnNext.keycomb}" onclick="wfNext('${formname}',${isDialog?string});return false;"  href="javascript:void(0);" title="${btnNext.desc}" >${btnNext.name}</a>
            
            <#break>
        <#default>
            null    
    </#switch>
</#macro>

<#macro M_HISTORY_WF whereCls>
      <@hnisi.glt id="fw_history_wf"  height="${parameters.height!'150'}" width="${parameters.width!'90%'}" whereCls="${whereCls!'1=2'}" showContent=false  hasPage=false expBtns=""/>
</#macro>
<#macro M_DOWNFILE_WF whereCls>
      <@hnisi.glt id="fw_wf_yxzllist"  height="${parameters.height!'150'}"  whereCls="${whereCls!'1=2'}" showContent=true  hasPage=false expBtns=""/>
</#macro>

<#macro M_WFFORM fname>
    <form name="${fname!''}" method="post" action="${parameters.ctx}/<#if isUploadFile >CommUpload.do<#else>CommSave.do</#if>"  <#if isUploadFile >enctype="multipart/form-data"</#if>/>
        <!-- 流程参数 -->
        <input type="hidden" name="_commDo" value="${serviceObject!""}" />
        <input type="hidden" id="${fname!''}_rtnURL" name="_rtnURL" value="${parameters._rtnURL!""}" />
        <input type="hidden" id="${fname!''}_pid" name="pid" value="${parameters.pid!""}" />
        <input type="hidden" id="${fname!''}_wid" name="wid" value="${parameters.wid!""}" />
        <input type="hidden" id="${fname!''}_wfState" name="_wfState" value="${parameters._wfState!"data"}" />
        <input type="hidden" id="${fname!''}_isWfStart" name="_isWfStart" value="${parameters._isWfStart!""}" />
        <input type="hidden" id="${fname!''}_processDefId" name="_processDefId" value="${parameters._processDefId!""}" title="流程定义ID"/>
        <input type="hidden" id="${fname!''}_curActDefId" name="_curActDefId" value="${parameters._curActDefId!""}"  title="当前环节定义ID"/>
        <input type="hidden" id="${fname!''}_curActDefName" name="_curActDefName" value="${parameters._curActDefName!""}"  title="当前环节定义名称"/>
        <input type="hidden" id="${fname!''}_nextActDefId" name="_nextActDefId" value="${parameters._nextActDefId!""}"  title="下一环节定义ID"/>
        <input type="hidden" id="${fname!''}_nextActDefName" name="_nextActDefName" value="${parameters._nextActDefName!""}"  title="下一环节定义名称"/>
        <input type="hidden" id="${fname!''}_wfData" name="_wfData" value="{'_nextActDefId':'${parameters._nextActDefId!""}','_nextActDefName':'${parameters._nextActDefName!""}','_unitId':'${parameters._unitId!""}','_bae006':'${parameters._bae006!""}'}" />
        <input type="hidden" id="${fname!''}_keyData" name="_keyData" value=""  title="关键信息" />
        <input type="hidden" id="${fname!''}_archiveId" name="_archiveId" value=""  title="档案编号" />
        <input type="hidden" id="${fname!''}_toApplyOpr" name="_toApplyOpr" value="${toApplyOpr}"  title="回退给原提交人" />
        <input type="hidden" id="${fname!''}_operId" name="_operId" value="${parameters._operId!""}" title="经办人" />
        <input type="hidden" id="${fname!''}_unitId" name="_unitId" value="${parameters._unitId!""}" title="经办单位" />
        <input type="hidden" id="${fname!''}_accepterId" name="_accepterId" value="" title="接收人" />
        <input type="hidden" id="${fname!''}_aab001" name="_aab001" value="${parameters._aab001!""}" title="单位顺序号"  />
        <input type="hidden" id="${fname!''}_aac001" name="_aac001" value="${parameters._aac001!""}" title="个人顺序号" />
        <input type="hidden" id="${fname!''}_bae006" name="_bae006" value="${parameters._bae006!""}" title="创建机构" />
        <input type="hidden" id="${fname!''}_cascade" name="_cascade" value="${parameters.isCascadeReceiver!""}" title="是否显示下级机构人员" />
        <input type="hidden" id="${fname!''}_toUnitOpr" name="_toUnitOpr" value="${parameters.toUnitOpr!""}" title="是否是提交给单位" />
        <input type="hidden" id="${fname!''}_getItemOpr" name="_getItemOpr" value="${parameters.getItemOpr!""}" title="提交给定环节经办人" />
        <!-- 业务数据 -->
        ${parameters.body!""}
        <@M_SHOWBOTTON parameters.btnType fname />
    
       <#if parameters.isShowBZ || parameters.isShowHistory >
           <@hnisi.ft header="流程信息" id="ft_${fname!''}">
                <#if parameters.isShowBZ>
                    <table width="100%"><tr><td width="80" align="right">流程备注：</td><td width="90%"><textarea id="${fname!''}_comment" class="wftextarea" rows="2"  style="width:100%" name="_comment" maxlength="200" onblur="commentValidate(this)" >${parameters._comment!""}</textarea></td></tr></table>
                </#if>
                
                <#if parameters.isShowHistory>
                    <#assign whereCls=parameters.pid!"">
                     <#if whereCls == "">
                         <#assign whereCls="1=2">
                     <#else>
                         <#assign whereCls=("BAE007='${whereCls}'")>
                     </#if>
                     <@M_HISTORY_WF whereCls />
                </#if>
            </@hnisi.ft>
        </#if>
        <#if isDownFile>
            <#assign whereCls=parameters.pid!"">
                 <#if whereCls == "">
                     <#assign whereCls="1=2">
                 <#else>
                     <#assign whereCls=("BAE007='${whereCls}'")>
                 </#if>
             <@M_DOWNFILE_WF whereCls />
         </#if>
    </form>
    
    
    
    
    <#if parameters.isDisabled>
        <script type="text/javascript" >
            try{
                toDisabled(${fname!''},"_comment",-1);
            }catch(ex){
                FWalert(ex.message);
            }
        </script>
    </#if>
    
</#macro>


<script src="${parameters.ctx}/js/fw/wf.js" type="text/javascript" ></script>

</head>

<div id="mainWindow" class="window" style="display:none" >
    <div id="div_acts">
    
    </div>
    
    <div id="wfDialogBtn" class="wfDialogBtn" style="display:none">
        <a class="buttonlink" keycomb="" href="javascript:void(0);" id="btn_cancel">取消</a>&nbsp;&nbsp;
        <a class="buttonlink" keycomb="" href="javascript:void(0);" id="btn_ok">确定</a>&nbsp;&nbsp;
    </div>
</div>
<div id="_msgWindow" class="window" style="display:none" >
    
    <div id="div_msg_context" style="width:100%;height:92%;">
                     
    </div>
    <div id="divMsgDialogBtn" class="wfDialogBtn" >
        <a class="buttonlink" keycomb="" href="javascript:void(0);" id="btn_msg_cancel">关闭</a>&nbsp;&nbsp;
    </div>
    <div id="div_wait" style="clear:both;position:absolute;padding:10px;left:50px;top:100px;z-index:9021">
                     正在处理中，请稍等......
        <br/>             
        <img width="95%" height="15px" src="${parameters.ctx}/themes/default/images/wait.gif" />
     </div>
</div>
<div id="div_wf" class="div_wf">
<@M_WFFORM parameters.formName />
</div>
<script type="text/javascript" >
    $(function(){
        _initTd('${parameters.formName}');
    });
</script>
