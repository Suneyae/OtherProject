/**
 * 
 * @param mode GET或者POST,二选一，如果不选，那么默认为GET
 * @param url
 * @param funcSuceess
 * @param funcFailde
 */
function ajaxWd(mode,url,funcSucc,funcFail){
	var oAjax = null;
	// window.XMLHttpRequest
	if(window.XMLHttpRequest){
		oAjax = new XMLHttpRequest();
//		alert('本浏览器支持xmlhttprequest');
	}else{
//		alert('本浏览器不支持xmlhttprequest');
		oAjax = new ActiveXObject("MicroSoft.XMLHTTP");
	}
	
	if(!mode){
		mode = 'GET';
	}
	
	alert(mode);
	//连接服务器
	oAjax.open(mode,url,true);
	
	//发送请求
	oAjax.send();
	
	//获取响应
	oAjax.onreadystatechange=function(){
		
		if(oAjax.readState==4){
			if(oAjax.status==200){
//				if(funcSucc){
//					funcSucc(oAjax.responseText);
//					alert('有入参funcSucc');
//				}else{
//					alert('没有入参funcSucc');
//				}
//				if(funcFail){
//					funcFail(oAjax.responseText);
//					alert('有入参funcFail');
//				}else{
//					alert('没有入参funcFail');
//				}
				alert(oAjax.responseText);
			}
			
		}
	}
	
	
}