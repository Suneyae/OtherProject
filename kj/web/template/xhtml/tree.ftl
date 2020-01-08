<#-- 准备树的类型 -->
<#assign v_type= (parameters.type?string)!"2" >
<#-- 准备机构树的 通办业务ID -->
<#assign v_bussFuncId = parameters.bussFuncId!"" >

<div style="text-align:center;">
	<a href="#" id="${parameters.id!""}_zk">全部展开</a> | <a href="#" id="${parameters.id!""}_gb">全部关闭</a>
</div>
<hr/>
<div id="${parameters.id!""}">
<script type="text/javascript">
jQuery(function($){
var vid='#${parameters.id!""}';
//避免重复初始化
if($(vid).data('_init')){return};
$(vid).data('_init',true);
var initDepth = ${parameters.initDepth!2};
var expandDepth = 2;
if(initDepth>0){
	expandDepth = Math.min(initDepth,2);
}
var selected = ${tojson(parameters.selected![])};
var defOptions=${parameters.options!"{}"};
var defAjax={key:'${parameters.root!""}',depth:1,whereCls:${tojson(parameters.whereCls!"")}
	,view:'${(v_type=="2")?string("v_org_tree","v_right_tree")}'
	,orderSql:'${(v_type=="2")?string("TREEKEY asc","TREEKEY asc,sortno asc")}'
	,bussFuncId:'${(v_type=="2")?string(v_bussFuncId,"")}'};
var url="${request.contextPath}/tree.do?"+$.param({selected:selected},true);
$(vid).dynatree(jQuery.extend({
	minExpandLevel:2,checkbox: false,selectMode: 1
	,initAjax:{url: url,data:jQuery.extend({},defAjax,{depth:initDepth,showRoot:true})}
	,onLazyRead: function(dtnode){
        dtnode.appendAjax({url: url
        	,data: jQuery.extend({},defAjax,{"key": dtnode.data.key})
        	});
        }
},defOptions));
$(vid+'_zk').click(function(){//全部展开
	var root = $(vid).dynatree("getRoot");
	root.appendAjax({url: url
        	,data: jQuery.extend({},defAjax,{depth:-1,showRoot:true})
        	,success: function(dtnode) {
        		//展开所有节点
				root.visit(function(dtnode){dtnode.expand(true);});
	           }
        	});
$(vid+'_gb').click(function(){//全部关闭
	$(vid).dynatree("getRoot").visit(function(dtnode){dtnode.expand(false);});});
	});
});
</script>
</div>