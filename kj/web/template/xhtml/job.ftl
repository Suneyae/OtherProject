<#assign hnisi=JspTaglibs["/WEB-INF/hnisi.tld"] />
<#assign  jobId=parameters.id>
<#assign  winId="win_job_"+jobId>
<#assign  isMulti=parameters.isMulti!false>
<#assign  width=parameters.width!724>
<#assign  height=parameters.height!400>
<#macro M_JOB >
    <script type="text/javascript">
        job_${parameters.id}_refresh = ${parameters.refresh};
        $(function(){
            $("#${winId}").fwwindow({
                title: "任务:${jobId}",
                width: ${width},
                modal: true,
                closed: true,
                maximizable:false,
                minimizable:false,
                collapsible:false,
                closable :true,
                height: ${height}
            });
            
            FwJob.prototype.__jobWin.push("#${winId}");
            <#if !isMulti>
                FwJob.prototype.${winId}_refresh();
                var t= setInterval("FwJob.prototype.${winId}_refresh()",${parameters.refresh});
                FwJob.prototype.__jobRefresh[${jobId}]=t;
            </#if>
        });
        
        function FwJob.prototype.${winId}_refresh() {
            var whereCls = "1=2";
            var key = $("#${jobId}_key").val();
            var jobId = "${jobId}";
            whereCls = " TRANSID = '"+jobId +"' and KEY = '"+key+"'";
            for(i = 0;i< FwJob.prototype.__jobWin.length; i++){
                 $(FwJob.prototype.__jobWin[i]).fwwindow('close');
            }

            $('#${winId}').fwwindow('open');
            
            getListData('fw_job_state',whereCls);
            FwJob.prototype.isComplete("${jobId}",key);
        }
       
       
        
        
    </script>
    
    <form name="job${jobId}Form" method="post">
        <input type="hidden" name="key" id="${jobId}_key" value="${parameters.key!''}"/>
        <div id="${winId}" class="window" style="display:none" >
            <@hnisi.glt id="fw_job_state"  height="${height-50}" width="${width-40}" whereCls="${whereCls!'1=2'}" showContent=true hasTitle=false hasPage=false expBtns=""/>
        </div>
    </form>
</#macro>

<@M_JOB />