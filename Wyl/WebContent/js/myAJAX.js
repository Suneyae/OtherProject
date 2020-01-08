/**
 * 定义触发ajax的事件
 * 
 * message:本次ajax请求的名称，可选。
 * 
 * success:处理成功后触发的函数，原型是function(data)。
 * 
 * error:处理是否时触发的函数，原型是function(XMLHttpRequest, textStatus, errorThrown);
 * async：同步或是异步，同步为false、异步是true
 */
function WylAjax() {
	this.sendAjax = function(message, success, error, async) {
		// if(this.services.length==0){
		// alert('请至少添加一个服务');
		// return;
		// }
		if (!this.services) {
			alert('请至少添加一个服务');
			// return;//return后就不会往下执行了，即下面的js代码就不会执行了，直接跳出
		}
		var t_async = true;
		var t_message = message;
		var t_success = success;
		var t_error = error;
		if (jQuery.isFunction(message)) {
			t_message = '正在请求服务，请稍候...';
			t_success = message;
			t_error = success;
		} else if (typeof message != 'string') {
			alert('入参错误，请检查程序！');
			return;
		}
		if (async != null && typeof async == 'boolean') {
			if (!async) {
				t_async = false;
			}
		}
		var thisrequest = {};
		$.ajax({
			url : contextPath + '/ajax.do',
			data : {
				parameters : JSON.stringify(this.services),
				shareArguments : JSON.stringify(this.shareParameters)
			},
			dataType : 'json',
			cache : false,
			async : t_async,
			type : 'post',
			error : function(request, textStatus, errorThrown) {
				if (!t_error) {
					alert('数据请求错误！');
				} else {
					t_error(request, textStatus, errorThrown);
				}
			},
			success : function(data, textStatus) {
				// 校验业务处理是否正确执行
				if ("1" != data.FHZ) {// 出错了，弹出错误提醒
					if ("loginTimeout" == data.FHZ) {
						if (window.confirm(data.MSG || '')) {
							window.top.location.href = _selfUrl;
						}
					} else {
						if (t_error) {
							t_error(data.MSG || '', 'serviceErr', data.MSG);
						} else {
							alert(data.MSG || '');
						}
					}
				} else if (!t_success) {
				} else {
					t_success(data.RTN);
				}
			},
			beforeSend : function() {
				$.data(thisrequest, 'msg', showMsg(t_message, -1));
				// createProgressBar();
			},
			complete : function() {
				hideMsg($.data(thisrequest, 'msg'));
			}
		});
	};
	/**
	 * 入参：url, 默认为post方法，根据传入的url访问web资源
	 */
	this.ajaxSend = function(url) {
		$.ajax({
			type : 'POST',
			url : url,
			dataType : 'html',// 指定获取到的数据的解析格式，可以不设定这个属性
			success : function(msg) {
				console.log('myAJAX.js的ajaxSend()方法 成功获取资源:' + msg);
			},
			error : function(msg2) {
				console.log('myAJAX.js的ajaxSend()方法获取失败:=======' + msg2
						+ '=========');
			}
		});
	}
}
/**
 * 检验传入的对象是否为json对象或者json字符串， 符合条件则返回json对象，否则返回false
 * 不能够传入字符串"weiyl","abcd"非json字符串，否则会报错。
 * 
 * @param json
 * @returns
 */
function toJson(json) {
	var type = typeof json;
	/**
	 * 防止传入的字符串是null,undefined或者''空字符串，所以判断的时候 用type=="string"与上json本身
	 */
	if (type == "string" && json) {
		return JSON.parse(json);
	} else if (type == "object") {
		return json;
	} else {
		alert('你传入的参数' + json + '，不是json对象或者json格式的字符串');
		return false;
	}
}
/**
 * 使用typeof操作符 对一个值使用typeof操作符可能返回下列某个字符串:
 * 
 * 1):undefined——如果这个值未定义
 * 
 * 2):boolean——如果这个值是布尔值
 * 
 * 3):string——如果这个值是字符串
 * 
 * 4):number——如果这个值是数值
 * 
 * 5):object——如果这个值是对象或null
 * 
 * 6):function——如果这个值是函数 传入一个json对象，或者json格式 的字符串，返回这个json对象的长度, 否则，返回 -1
 * 
 * @param jsonObj
 * @returns {Number}
 */
function getJsonLen(jsonObj) {
	jsonObj = toJson(jsonObj);
	var type = typeof jsonObj;
	if (type == "boolean" || type == undefined || type == "number"
			|| type == "function") {
		return -1;
	}
	var len = 0;
	for ( var item in jsonObj) {
		len++;
	}
	return len;
}

/**
 * /** 传入一个json字符串或者json对象， 获取所有key值，并且 以数组形式返回, 如果传入的不是json字符串或者json对象， 就返回-1
 * 
 * @param JsonObj
 * @returns {Array}
 */
function getJsonKey(JsonObj) {
	var len = getJsonLen(JsonObj);
	if (len == -1) {
		return -1;
	}
	var array = [];
	/*
	 * 因为在getJsonLen()方法中对传入的对象做了 判断，所以这里 就不用再判断是否为json或者json字符串了
	 */
	for ( var item in JsonObj) {
		array.push(item);
	}
	return array;
}
/**
 * 传入一个json字符串或者json对象， 获取所有value值，并且以 数组形式返回, 如果传入的不是json字符串或者json对象， 就返回-1
 * 
 * @param JsonObj
 * @returns {Array}
 */
function getJsonValue(JsonObj) {
	var len = getJsonLen(JsonObj);
	if (len == -1) {
		return -1;
	}
	var array = [];
	/*
	 * 因为在getJsonLen()方法中对传入的对象做了 判断，所以 这里就不用再判断是否为json或者json字符串了
	 */
	for ( var item in JsonObRRj) {
		array.push(JsonObj[item]);
	}
	return array;
}
/**
 * 双击多记录表数据，调用自定义的function FunName,
 * 
 * @param ListID
 * @param FunName
 * @returns {Boolean}
 */
function onDblClickListRow(ListID, FunName) {
	var target = eval('document.all.' + ListID);
	var opts = jQuery.data(target, 'fwdatagrid').options;
	var fun = window[FunName];
	opts.onDblClickRow = fun;
	return true;
}