/**通用ajax服务的定义对象
 * services可以是单个服务对象，也可以是service服务数组
 * 具体服务的定义请参考appendServices成员函数
 */
function Service(services){
	this.services=[];
	this.shareParameters={};
	/**添加共享参数，在这里统一设置共享参数
	 */
	this.addShareParameters=function(shareParameters){
		this.shareParameters=shareParameters;
		return this;
	};
	/**
	 * 批量调用一般服务
	 * 入参可以是一个调用服务的数组或者单个调用服务
	 * 每个调用服务有下面的属性
	 * serviceId 服务Id，不可为空
	 * method 服务方法名，不可为空
	 * parameters 服务参数，必须是object，object的属性名代表参数名，属性值代表参数值。
	 * transform：结果集的转换规则（返回结果集合List<Map<String,Object>>时才会用到）：null（无转换）、'firstRow'（取首行）、'breakdown'（分列），值不区分大小写 
	 * shareResults 共享服务的返回结果，默认不共享false，如果后续服务需要使用到结果里面的参数，
	 *   那么就需要明确指定。该参数可以是 boolean,array或者object，为true时表示全部共享；
	 *   数组可以指定返回值中需要共享的值的名，这样返回值的键值对应关系共享；
	 *   object可以额外指定共享名的别名，当要求返回值以新的字段名共享、
	 *   或者一个以多个新别名共享时就需要用到这种类型，object的属性名表示结果集中需要共享的字段名，
	 *   属性值表示新的字段名；
	 *   参数共享处理列表结果集时，整个结果集的数据存放在别名为空串的特殊key上。如果本业务没有返回结果，那么参数共享无效。
	 * useShare 使用共享参数标志，定义方式与shareResults一样，意义正好想对应。该参数指明如果从共享参数中获取要的值。
	 * shareNotNull 校验共享参数是否为空，作为useShare的附属属性。
	 *   有时希望如果获取到的共享参数是空的，那么不继续执行，那么可以在这里指定哪些一定不能为空。
	 *   目前只允许三种取值，null,true和别名数组，null表示不校验，true表示全校验，别名数组表示只校验指定的值（别名是指useShare中新的别名，并非共享池里面的共享别名）。
	 */
	this.appendServices=function(services){
		if(!services){
			return this;
		}
		//默认按批量形式添加服务，如果是单个，那么用数组包装
		var tmp_services=services;
		if(!$.isArray(tmp_services)){
			tmp_services = [tmp_services];
		}
		//每个service必须有serviceId，method
		for(index in tmp_services){
			//检查必录项
			if(!tmp_services[index].serviceId||!tmp_services[index].method){
				FWalert('服务定义的serviceId和method不可为空');
				return this;
			}
			//检查可选项
			if(tmp_services[index].parameters){
				if(typeof tmp_services[index].parameters !='object'
					||jQuery.isArray(tmp_services[index].parameters)){
					FWalert('服务定义的参数必须是map！');
					return;
				}
			}
			//如果指定了transform，那么值只能是规定的
			if(tmp_services[index].transform){
				if('FIRSTROW'!=tmp_services[index].transform.toUpperCase()
						&&'BREAKDOWN'!=tmp_services[index].transform.toUpperCase()){
					FWalert('transform属性不正确');
					return this;
				}
			}
			//shareResults
			//转换shareResults，统一转换成map，或者boolean
			shareResults = tmp_services[index].shareResults;
			if(shareResults){
				if(typeof shareResults =='boolean'){
					if(!shareResults){
						shareResults =null;
					}
				}else if(jQuery.isArray(shareResults)){
					//转化为map
					shareResults={};
					$.each(tmp_services[index].shareResults,function(indexInArray, valueOfElement){
						shareResults[valueOfElement]=valueOfElement;
					});
					tmp_services[index].shareResults =shareResults;
				}
			}
			//useShare
			useShare = tmp_services[index].useShare;
			if(useShare){
				if(typeof useShare =='boolean'){
					if(!useShare){
						tmp_services[index].useShare =null;
					}
				}else if(jQuery.isArray(useShare)){
					//转化为map
					useShare={};
					$.each(tmp_services[index].useShare,function(indexInArray, valueOfElement){
						useShare[valueOfElement]=valueOfElement;
					});
					tmp_services[index].useShare =useShare;
				}
			}
			//shareNotNull，只允许true和字符串数组
			shareNotNull = tmp_services[index].shareNotNull;
			if(shareNotNull){
				if(typeof shareNotNull !=='boolean' && !jQuery.isArray(shareNotNull)){
					FWalert('参数[shareNotNull]的取值必须是true或者字符串数组！');
					return this;
				}else if(shareNotNull ===false){
					tmp_services[index].shareNotNull = null;
				}
			}
		}
		this.services=this.services.concat(tmp_services);
		return this;
	};
	/**定义添加直接调用存储过程的服务
	 * 可以批量添加存储过程。每个存储过程服务的有以下属性：
	 * procName、parameters、shareResults、useShare，
	 * 其中procName指明存储过程的名称，其他的请参考appendServices；parameters中的参数名不能是procName
	 * 批量添加存储过程时，用数组存储各个存储过程作为参数传入即可。
	 * ...
	 */
	this.appendProc=function(procedures){
		if(!procedures){
			return this;
		}
		//默认按批量形式添加服务，如果是单个，那么用数组包装
		tmp_procedures=procedures;
		if(!$.isArray(tmp_procedures)){
			tmp_procedures = [tmp_procedures];
		}
		//遍历，一个一个的处理
		procedure_services =[];
		for (index in tmp_procedures){
			//必须有configId属性
			procedure = tmp_procedures[index];
			if(!procedure.procName){
				FWalert('存储过程服务必须指定procName属性');
				return this;
			}
			procedure = $.extend(true,{},procedure,
					{serviceId:'directJdbcService',method:'savePointProcedure'
						,parameters:{'':procedure.procName}});
			//去掉存储过程名称
			delete procedure.procName;
			//添加到服务列表
			procedure_services.push(procedure);
		}
		return this.appendServices(procedure_services);
	};
	/**定义添加调用预定义查询语句的服务
	 * 可以批量添加查询语句。每个查询服务的包括以下属性：
	 * configId、transform、indices，parameters、shareResults、useShare。
	 * configId是Mapper.xml中的配置ID(注意写上空间名)
	 * parameters是传递给configId的参数
	 * transform：结果集的转换规则：null（无转换）、'firstRow'（取首行）、'breakdown'（分列），值不区分大小写
	 * ，该参数要求传入字符串数组类型，元素值指明参数Map中的一个参数名。
	 * 其它属性将作为查询语句的备用参数。其他的请参考appendServices；
	 * 批量添加查询服务时，用数组存储各个查询服务作为参数传入即可。
	 * ...
	 */
	this.appendQuery=function(querys){
		if(!querys){
			return this;
		}
		//默认按批量形式添加服务，如果是单个，那么用数组包装
		tmp_querys=querys;
		if(!$.isArray(tmp_querys)){
			tmp_querys = [tmp_querys];
		}
		//遍历，一个一个的处理
		query_services = [];
		for (index in tmp_querys){
			//必须有configId属性
			var query = tmp_querys[index];
			if(!query.configId){
				FWalert('查询服务必须指定configId属性');
				return this;
			}
			//参数索引放入参数串中
			query = $.extend(true,{},query,
					{serviceId:'commService',method:'query'
						,parameters:{'_commDo':query.configId}});
			
			//去掉存储过程名称，和参数索引
			delete query.configId;
			
			//添加到服务列表
			query_services.push(query);
		}
		return this.appendServices(query_services);
	};
	/**定义触发ajax的事件
	 * message:本次ajax请求的名称，可选。
	 * success:处理成功后触发的函数，原型是function(data)。
	 * error:处理是否时触发的函数，原型是function(XMLHttpRequest, textStatus, errorThrown);
	 * async：同步或是异步，同步为false、异步是true
	 */
	this.sentAjax=function(message,success,error,async){
		if(this.services.length==0){
			FWalert('请至少添加一个服务');
			return;
		}
		var t_async = true;
		var t_message = message;
		var t_success = success;
		var t_error = error;
		if(jQuery.isFunction(message)){
			t_message = '正在请求服务，请稍候...';
			t_success =message;
			t_error = success;
		}else if (typeof message != 'string'){
			FWalert('入参错误，请检查程序！');
			return ;
		}
		if(async!=null&&typeof async=='boolean'){
			if(!async){
				t_async = false;
			}
		}
		var thisrequest={};
		 $.ajax({
			url:contextPath+'/ajax.do'
			,data:{parameters:JSON.stringify(this.services),shareArguments:JSON.stringify(this.shareParameters)}
			,dataType :'json'
			,cache:false
			,async:t_async
			,type:'post'
			,error:function (request, textStatus, errorThrown) {
				if(!t_error){
					FWalert('数据请求错误！');	
				}else{
					t_error(request, textStatus, errorThrown);
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
						if(t_error){
							t_error(data.MSG||'', 'serviceErr', data.MSG);
						}else{
							FWalert(data.MSG||'');
						}
					}
				}else if(!t_success){
				}else{
					t_success(data.RTN);
				}
			}
			,beforeSend:function( ){
				$.data(thisrequest,'msg',showMsg(t_message,-1));
				//createProgressBar();
			}
			,complete:function( ){
				hideMsg($.data(thisrequest,'msg'));
			}
		 });
	};
	//添加参数
	if(services){
		this.appendServices(services);
	}
}
/**
* 在页面的左上角显示错误消息
* @param msg 消息内容
* @param timeout 秒为单位,0或者负数表示不自动隐藏
* @author 吴英德
**/
var framework_message_layer;
var clearIntervalID;
function showMsg(msg,delay){

	var recurrectLocation=function(){
		if(framework_message_layer==null)
				{clearInterval(clearIntervalID);return;}
			var posX,posY;
	    if (window.innerHeight) {
	        posX = window.pageXOffset;
	        posY = window.pageYOffset;
	    }
	    else if (document.documentElement && document.documentElement.scrollTop) {
	        posX = document.documentElement.scrollLeft;
	        posY = document.documentElement.scrollTop;
	    }
	    else if (document.body) {
	        posX = document.body.scrollLeft;
	        posY = document.body.scrollTop;
	    }
		framework_message_layer.style.top=String(posY+10)+'px';
		framework_message_layer.style.right=String(posX+10)+'px';
	};
	if(framework_message_layer == null){
		framework_message_layer=document.createElement('div');
		framework_message_layer.className='err_message_blank_board';
		document.body.appendChild(framework_message_layer);
		clearIntervalID=window.setInterval(recurrectLocation,100);
		recurrectLocation();
	}
	var my_div = document.createElement('div');
	my_div.className='err_message';
	//Element.extend(my_div);
	my_div.innerHTML=msg;
	framework_message_layer.appendChild(my_div);
	recurrectLocation();
	if(delay>0){
		setTimeout(function(){
			jQuery(my_div).remove();
			if(jQuery(framework_message_layer).is(':empty')){
				jQuery(framework_message_layer).remove();
				window.clearInterval(clearIntervalID);
				framework_message_layer=null;
			}
		},delay*1000);
	}else{
		return my_div;
	}
}

/**隐藏右上角对应的消息
 * @param object 某消息对象，来自showMsg的返回值
 */
function hideMsg(object){
	jQuery(object).remove();
}
