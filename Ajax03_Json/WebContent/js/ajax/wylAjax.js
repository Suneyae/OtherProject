(function(w, undefined) {
	window.wyl;
	wyl = function(services){
		this.services = [];
	}
	wyl.prototype.appendServices = function(serviceObj){
		var tmp_serviceObj = serviceObj;
		var elements = [];
		var _type = typeof serviceObj;
		if(_type!='object'){
			alert('传入的参数必须为对象类型，而不能是类型：'+_type);
			return false;
		}
		//检查传入参数serviceObj是否有serviceId 
		if(!serviceObj.serviceId){
			alert('传入的参数必须有serviceId属性!');
			return false;
		}
		//检查传入参数serviceObj是否有method
		if(!serviceObj.method){
			alert('传入的参数必须有method属性!');
			return false;
		}
		//检查传入参数serviceObj是否有parameters
		if(!serviceObj.parameters){
			alert('传入的参数必须有parameters属性!');
			return false;
		}else{
			var _type2 = typeof serviceObj.parameters;
			if(_type2!='object'){
				alert('传入的参数的属性parameters属性值必须是object类型!');
				return false;
			}
		}
		if(!$.isArray(tmp_serviceObj)){
			tmp_serviceObj = [tmp_serviceObj];
		}
		//把传入的参数加到wyl.services属性上，然后返回wyl对象
		this.services = this.services.concat(tmp_serviceObj);
		return this;
		
	}
	
	
	wyl.prototype.sendAjax = function(message,success,error,async){
		// message 是用来存储ajax发送请求的时候浏览器右上角的提示信息的，暂不处理
		var _message = message;
		var _success = success;
		var _error = error;
		var _async = async;
		
		
		
		$.ajax({
			// getSearch
			/*url:contextPath+'/ajax.do'*/
			url:contextPath+'/getSearch.do'
			,data:{parameters:JSON.stringify(this.services)}
			,dataType :'json'
			,cache:false
			,async:_async
			,type:'post'
			,error:function (request, textStatus, errorThrown) {
				if(!_error){
					FWalert('数据请求错误！');	
				}else{
					_error(request, textStatus, errorThrown);
				}
				}
			,success:function (data, textStatus) {
				//校验业务处理是否正确执行
				if("1"!=data.FHZ){//出错了，弹出错误提醒
					if ("loginTimeout" == data.FHZ) {
						if(window.confirm(data.MSG||'')){
							window.top.location.href=_selfUrl;
						}
					} else {
						if(_error){
							_error(data.MSG||'', 'serviceErr', data.MSG);
						}else{
							FWalert(data.MSG||'');
						}
					}
				}else if(!_success){
				}else{
					_success(data.RTN);
				}
			}
			/*,beforeSend:function( ){
				$.data(thisrequest,'msg',showMsg(t_message,-1));
				//createProgressBar();
			}
			,complete:function( ){
				hideMsg($.data(thisrequest,'msg'));
			}*/
		 });
		
		
		
	}
})(window)