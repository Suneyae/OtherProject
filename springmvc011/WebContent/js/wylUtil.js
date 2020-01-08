//w作为window的形参，就表示window
(function(w) {

	// 定义一个全局的window.wyl变量，就类似于jquery里的$，Jquery对象
	w.wyl;
	/**
	 * 模仿$(domObj)的形式，通过wyl(domObj)的方式把一个dom转成wyl对象
	 * 
	 * @param para
	 * @returns {wyl} 这里的selector暂时只能是dom对象
	 */
	wyl = function(selector) {
		this._selector = selector;
		if (this == window) {
			// 我自己原来写的
			// return this.wyl;
			return new wyl(selector);
		} else {
			// 用来保存选中的元素
			this.elements = [];

			// 判断selector的类型，因为jquery(xxx)中的xxx总共有三种类型，1 function ,2 字符串 ,如
			// '#aa','.my'等等 ; 3 dom对象也就是object

			switch (typeof selector) {
			case 'function':
				this.wylAddEvent(w, 'load', selector);
				break;
			// 模拟 #xx选择器
			case 'string':
				switch (selector.charAt(0)) {
				case '#':// 按照id选择器
					var obj = document.getElementById(selector.substring(1));
					this.elements.push(obj);
					break;
				case '.':// 按照class选择器
					this.elements = getEleByClas(document, selector
							.substring(1));
					break;
				default: // 按照tagName
					this.elements = document.getElementsByTagName(selector);
				}
				break;
			case 'object':
				this.elements.push(selector);
				break;
			default:
				break;
			}

			// 根据name获取dom
			/* var dom_ = document.getElementsByName('' + selector); */
			/**
			 * 根据 document.getElementsByName('' +selector);的形式获取到的dom对象
			 * 有length属性，而通过getElementById获取的dom对象没有length属性
			 * 
			 */
			/*
			 * if (dom_.length) { alert(dom_.length); } else {
			 * alert(dom_.innerHTML + '目前这个框架只能通过传入name属性来使用，无法通过id来解析dom对象'); }
			 */

			// 作用：返回当前对象，即返回window.wyl这个对象
			// return this;
			var this_ = [].push.apply(this, this.elements);
			// 注意这里返回的不是this_而是this
			return this;
		}

	}

	// 添加click事件

	wyl.prototype.click = function(fn) {
		for (var i = 0; i < this.elements.length; i++) {
			this.wylAddEvent(this.elements[i], 'click', fn);
		}
		return this;
	}

	// 根据class进行选择，即模拟class选择器
	wyl.getEleByClas = function(oParent, sClass) {
		var aEle = document.getElementsByTagName("*");
		var aResults = [];
		for (i = 0; i < aEle.length; i++) {
			if (aEle[i].className == sClass) {
				aResults.push(aEle[i]);
			}
		}
		return aResults;
	}
	// 模仿jquery的bind()方法，
	wyl.prototype.wylAddEvent = wyl.wylAddEvent = function(obj, sEv, fn) {
		// 去除绑定的事件,暂时没有成功解绑定 20160812
		this.wylDelEvent(obj, sEv, fn);

		if (obj.attachEvent) {
			// IE
			obj.attachEvent('on' + sEv, fn);
		} else {
			// 非IE; 适用firefox,chrome等
			obj.addEventListener(sEv, fn, false);
		}
	}

	// 暂时发现 wylDelEvent 无效，没有成功解绑定事件
	wyl.prototype.wylDelEvent = wyl.wylDelEvent = function(obj, sEv, fn) {
		if (obj.attachEvent) {
			// IE
			obj.attachEvent('on' + sEv, function() {
			});
		} else {
			/*
			 * event 必须。字符串，指定事件名。
			 * 
			 * 注意: 不要使用 "on" 前缀。 例如，使用 "click" ,而不是使用 "onclick"。
			 * 
			 * 提示： 所有 HTML DOM 事件，可以查看我们完整的 HTML DOM Event 对象参考手册。 function
			 * 必须。指定要事件触发时执行的函数。
			 * 
			 * 当事件对象会作为第一个参数传入函数。 事件对象的类型取决于特定的事件。例如， "click" 事件属于
			 * MouseEvent(鼠标事件) 对象。 useCapture 可选。布尔值，指定事件是否在捕获或冒泡阶段执行。
			 * 
			 * 可能值: true - 事件句柄在捕获阶段执行 false- false- 默认。事件句柄在冒泡阶段执行
			 */

			// 非IE; 适用firefox,chrome等
			// x.removeEventListener("mousemove", myFunction);
			var flag = obj.removeEventListener;
			if (flag) {
				obj.removeEventListener(sEv, fn);
				// obj.removeEventListener(sEv);
				// obj.removeEventListener(sEv, fn, false);
			}

		}
	}

	// wyl.prototype.isArr的作用：在所有的wyl对象上加上isArr(para)的function，
	wyl.prototype.isArr = wyl.isArr = function(para) {
		/**
		 * 在wyl对象上添加工具方法isArr(obj) 实例：var flag = wyl.isArr(obj); 如果为true那么就说明是数组
		 */
		if (typeof para == 'object' && para instanceof Array) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 实现类似框架中的linkbutton()功能的方法 框架中的使用实例： $obj.linkbutton({ disabled:true });
	 * 
	 */
	wyl.prototype.print = wyl.print = function(para) {
		alert(para);
		return this;
	}

	wyl.prototype.linkbutton = wyl.linkbutton = function(para) {
		var _type = typeof para;
		if (_type != 'object') {
			return this.print('传入的参数必须是object');
		}
		// 存放传入的obejct类型的参数
		var para_ = para || {};
		var f = para_.disabled;
		if (f == true) {
			$(this).removeClass();
			$(this).addClass('l-btn-disabled');
			// 通过jquery设置onclick属性为null,相当于移除onclick属性
			$(this[0]).attr('onclick', null);

			// delete (this[0].onclick);//无效
			return this;
		} else {
			$(this).removeClass();
			$(this).addClass('l-btn-abled');
			return this;
		}
	}

	// 封装 模态窗口，
	function motai(html, parameter, size) {
		// document.open(html,parameter,size);
		var d = 123;// 主要是调试改网页的时候在此处设置断点用
		// 此处做能力检测，进行兼容
		if (window.showModalDialog) {
			// IE是支持的是 window.showModalDialog
			console.log('showModalDialog 方式');
			window.showModalDialog(html, parameter, size);
		} else {
			// Chrome，Edge等都是支持 document.open();的方式
			console.log('open 方式');
			document.open(html, parameter, size);
		}
	}
	function myEncodeStr(str) {
		str = typeof str == 'string' ? str : '';
		if (!str) {
			alert('无法编码，传入的对象必须是字符串');
			return false;
		}
		return encodeURIComponent(encodeURIComponent(str));
	}

	/**
	 * 传入字符串
	 */
	function openMotai(str) {
		// var thePara =
		// encodeURIComponent(encodeURIComponent(document.getElementById('id_text').value));
		var thePara = encodeURIComponent(encodeURIComponent(str));
		var thePara = myEncodeStr(str);
		// 转为
		// 设置弹出窗口的标题栏，需要使用encodeURIComponent进行编码设置，否则弹窗口会乱码
		// var title = encodeURIComponent("增员信息页面"+g_blankDouble) ;
		thePara = motai("open?thePara=" + thePara + "&_t="
				+ new Date().getTime(), window.a,
				"dialogWidth:400px;dialogHeight:500px;");
	}
	
	//隐藏 一个表单或者 标签域 直接使用 jquery的hide()方法就行。
	// 或者 使用jquery 的 css("visibility","hidden");//隐藏输入框
	// 显示 用 css("visibility","visible");//隐藏输入框
	// 显示 用 jquery的show();

	/**
	 * 模拟双击后调用的函数， added by weiyongle 20160911
	 */

	// 传入window对象
})(window)
