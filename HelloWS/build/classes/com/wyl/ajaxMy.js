/**
 * 
 * @param mode GET或者POST,二选一，如果不选，那么默认为GET
 * @param url
 * @param funcSuceess
 * @param funcFailde
 */
function ajaxWd(mode,url,funcSucc,funcFail){
	if(window.XMLHttpResquest){
		var oAjax = new XMLHttpRequest();
	}else{
		var oAjax = new ActiveXObject('MicroSoft.XMLHTTP');
	}
	if(!mode){
		mode = 'GET';
	}
	//连接服务器
	oAjax.open(mode,url,true);
	
	//发送请求
	oAjax.send();
	
	//获取响应
	oAjax.onreadystatechange=function(){
		if(oAjax.readState==4){
			if(oAjax.status==200){
				if(funcSucc){
					funcSucc(oAjax.responseText);
				}else{
					alert('没有入参funcSucc');
				}
				if(funcFail){
					funcFail(oAjax.responseText);
				}else{
					alert('没有入参funcFail');
				}
			}
		}
	}
	
	
}