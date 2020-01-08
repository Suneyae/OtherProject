 
(function($){
	
	function removeNode(node){
		node.each(function(){
			$(this).remove();
			if ($.browser.msie){
				this.outerHTML = '';
			}
		});
	}
	
	function setSize(target, param){
 
		var opts = $.data(target, 'fwwindow').options;
		var panel = $.data(target, 'fwwindow').fwpanel;
		var pheader = panel.find('>div.panel-header');
		var pbody = panel.find('>div.panel-body');
		
		if (param){
			if (param.width) opts.width = param.width;
			if (param.height) opts.height = param.height;
			if (param.left != null) opts.left = param.left;
			if (param.top != null) opts.top = param.top;
		}
		
		if (opts.fit == true){
			var p = panel.parent(); 
		    if(p.attr("tagName")=="FORM"){
		     var pp = p.parent();
		 	  opts.width = pp.width()-20;
			  opts.height = pp.height();
		    }else{
			opts.width = p.width()-20;
			opts.height = p.height();
		    }
		}
		panel.css({
			left: opts.left,
			top: opts.top
		});
		panel.css(opts.style);
		panel.addClass(opts.cls);
		pheader.addClass(opts.headerCls);
		pbody.addClass(opts.bodyCls);
		
		if (!isNaN(opts.width)){
			if ($.boxModel == true){
				panel.width(opts.width - (panel.outerWidth() - panel.width()));
				pheader.width(panel.width() - (pheader.outerWidth() - pheader.width()));
				pbody.width(panel.width() - (pbody.outerWidth() - pbody.width()));
			} else {
				panel.width(opts.width);
				pheader.width(panel.width());
				pbody.width(panel.width());
			}
		} else {
			panel.width('auto');
			pbody.width('auto');
		}
		if (!isNaN(opts.height)){
 
			
			if ($.boxModel == true){
				panel.height(opts.height - (panel.outerHeight() - panel.height()));
				pbody.height(panel.height() - pheader.outerHeight() - (pbody.outerHeight() - pbody.height()));
			} else { 
				panel.height(opts.height);
				pbody.height(panel.height() - pheader.outerHeight());
			}
		} else {
			pbody.height('auto');
		}
		panel.css('height', null);
		FWresizeObject(target);
		//panel.find('>div.panel-body>div').triggerHandler('_resize');
	}
	
	/**
	 * create and initialize window, the window is created based on panel component 
	 */
	function init(target, options){
		
		var state = $.data(target, 'fwwindow');
		var opts;
		if (state){
			opts = $.extend(state.opts, options);
		} else {
		 
			var t = $(target);
			opts = $.extend({}, $.fn.fwwindow.defaults, {
				title: t.attr('title'),
				collapsible: (t.attr('collapsible') ? t.attr('collapsible') == 'true' : undefined),
				minimizable: (t.attr('minimizable') ? t.attr('minimizable') == 'true' : undefined),
				maximizable: (t.attr('maximizable') ? t.attr('maximizable') == 'true' : undefined),
				closable: (t.attr('closable') ? t.attr('closable') == 'true' : undefined),
				closed: (t.attr('closed') ? t.attr('closed') == 'true' : undefined),
				shadow: (t.attr('shadow') ? t.attr('shadow') == 'true' : undefined),
				modal: (t.attr('modal') ? t.attr('modal') == 'true' : undefined),  
				width: (parseInt(t.css('width')) || undefined),
				height: (parseInt(t.css('height')) || undefined),
			//	left: (parseInt(t.css('left')) || undefined),
			//	top: (parseInt(t.css('top')) || undefined),

				iconCls: t.attr('icon'),
				cls: 'fwwindow',
				headerCls: 'fwwindow-header',
				bodyCls: 'fwwindow-body',
				href: t.attr('href'),
				cache: (t.attr('cache') ? t.attr('cache') == 'true' : undefined),
				fit: (t.attr('fit') ? t.attr('fit') == 'true' : undefined),
				border: (t.attr('border') ? t.attr('border') == 'true' : undefined),
				noheader: (t.attr('noheader') ? t.attr('noheader') == 'true' : undefined), 
				collapsed: (t.attr('collapsed') ? t.attr('collapsed') == 'true' : undefined),
				minimized: (t.attr('minimized') ? t.attr('minimized') == 'true' : undefined),
				maximized: (t.attr('maximized') ? t.attr('maximized') == 'true' : undefined) 
				
			}, options);
			$(target).attr('title', '');
			state = $.data(target, 'fwwindow', {options:opts});
		};
		 
		 
         var panel = $(target).addClass('panel-body').wrap('<div class="panel"></div>').parent();
             panel.bind('_resize', function(){ 
            	if (opts.fit == true){
            		setSize(target);
            	}
            	return false;
            });
          
        panel.hide();
		state.options = opts;
		state.fwpanel = panel;
		
    	addHeader(target);
		setBorder(target); 
		setSize(target);
		
		// create mask
		if (state.mask) state.mask.remove();
		if (opts.modal == true){
			state.mask = $('<div class="fwwindow-mask"></div>').appendTo('body');
			state.mask.css({
				//zIndex: $.fn.fwwindow.defaults.zIndex--,
				width: getPageArea().width,
				height: getPageArea().height,
				display: 'none'
			});
		}
		
		// create shadow
		if (state.shadow) state.shadow.remove();
		if (opts.shadow == true){
			state.shadow = $('<div class="fwwindow-shadow"></div>').insertAfter(state.fwpanel);
			state.shadow.css({
				display: 'none'
			});
		}
 
		// if require center the fwwindow
		initPostion(target);
 
		movePanel(target);
		
		if (state.options.closed == false){
			openPanel(target);	// open the fwwindow
		}
	}
	
	function initPostion(target){
		var state = $.data(target, 'fwwindow'); 
		
		//if (state.options.left == null){
			var width = state.options.width;
			if (isNaN(width)){
				width = state.fwpanel.outerWidth();
			}
			state.options.left = ($(window).width() - width) / 2 + $(document).scrollLeft();
	//	}
	 
	//	if (state.options.top == null){
			var height = state.options.height;
			if (isNaN(height)){
				height = state.fwpanel.outerHeight();
			}
			state.options.top = ($(window).height() - height) / 2 + $(document).scrollTop();
	//	}
		
	}
	
	function addHeader(target){
		
		var opts = $.data(target, 'fwwindow').options;
		var panel = $.data(target, 'fwwindow').fwpanel;
		
		removeNode(panel.find('>div.panel-header'));
		
		if (opts.title && !opts.noheader){
			var header = $('<div class="panel-header"><div class="panel-title">'+opts.title+'</div></div>').prependTo(panel);
			if (opts.iconCls){
				header.find('.panel-title').addClass('panel-with-icon');
				$('<div class="panel-icon"></div>').addClass(opts.iconCls).appendTo(header);
			}
			var tool = $('<div class="panel-tool"></div>').appendTo(header);
			if (opts.closable){
				$('<div class="panel-tool-close"></div>').appendTo(tool).bind('click', onClose);
			}
			if (opts.maximizable){
				$('<div class="panel-tool-max"></div>').appendTo(tool).bind('click', onMax);
			}
		//	if (opts.minimizable){
		//		$('<div class="panel-tool-min"></div>').appendTo(tool).bind('click', onMin);
		//	}
			if (opts.collapsible){
				$('<div class="panel-tool-collapse"></div>').appendTo(tool).bind('click', onToggle);
			}
			if (opts.tools){
				for(var i=opts.tools.length-1; i>=0; i--){
					var t = $('<div></div>').addClass(opts.tools[i].iconCls).appendTo(tool);
					if (opts.tools[i].handler){
						t.bind('click', eval(opts.tools[i].handler));
					}
				}
			}
			tool.find('div').hover(
				function(){$(this).addClass('panel-tool-over');},
				function(){$(this).removeClass('panel-tool-over');}
			);
			panel.find('>div.panel-body').removeClass('panel-body-noheader');
		} else {
			panel.find('>div.panel-body').addClass('panel-body-noheader');
		}
		
		function onToggle(){
			if ($(this).hasClass('panel-tool-expand')){
				expandPanel(target, true);
			} else {
				collapsePanel(target, true);
			}
			return false;
		}
		
		function onMin(){
			minimizePanel(target);
			return false;
		}
		
		function onMax(){
			if ($(this).hasClass('panel-tool-restore')){
				restorePanel(target);
			} else {
				maximizePanel(target);
			}
			return false;
		}
		
		function onClose(){
			closePanel(target);
			var opts = $.data(target, 'fwwindow').options;
		     if (opts.onClose) {
	              opts.onClose.apply();
	          } 
			return false;
		}
	}
	

	/**
	 * load content from remote site if the href attribute is defined
	 */
	function loadData(target){
		var state = $.data(target, 'fwwindow');
		if (state.options.href && (!state.isLoaded || !state.options.cache)){
			state.isLoaded = false;
			var pbody = state.panel.find('>div.panel-body');
			pbody.html($('<div class="panel-loading"></div>').html(state.options.loadingMessage));
			pbody.load(state.options.href, null, function(){
				if ($.parser){
					$.parser.parse(pbody);
				}
				state.options.onLoad.apply(target, arguments);
				state.isLoaded = true;
			});
		}
	}
	
	function openPanel(target, forceOpen){
		var opts  = $.data(target, 'fwwindow').options;
		var panel = $.data(target, 'fwwindow').fwpanel;
		var mask  = $.data(target, 'fwwindow').mask;
   
		//fn.fwwindow.defaults.zIndex
		//为全部的深度
		var deIndex = $.fn.fwwindow.defaults.zIndex; 
	 
		   //重新获取窗口位置
		    initPostion(target);
		 
		   if(opts.modal==true){ 
			   mask.css({
					display:'block',
					zIndex: opts.zIndex++
				});
			   panel.css({zIndex: opts.zIndex++}); 
		   } 
		   
		   panel.css({top:opts.top});
		   
		if(opts.zIndex<deIndex){
			opts.zIndex = deIndex;
			panel.css({zIndex: opts.zIndex});
		}else{
			$.fn.fwwindow.defaults.zIndex = opts.zIndex + 1;	
		}   
		  
		$(target).css({display:'block'});
		panel.show();
		opts.closed = false;
		
		setCombox(target);
		//触发其它控件的后续初始化步骤
		$(document).trigger('gridlazyInit');
		if (opts.maximized == true) maximizePanel(target);
		if (opts.minimized == true) minimizePanel(target);
		if (opts.collapsed == true) collapsePanel(target);
		
		if (!opts.collapsed){
			loadData(target);
		}
	 
	}
	
	function closePanel(target, forceClose){
		var opts  = $.data(target, 'fwwindow').options;
		var panel = $.data(target, 'fwwindow').fwpanel;
		var mask  = $.data(target, 'fwwindow').mask;
 
		  if(opts.modal==true){
			  mask.hide();
		   }
		panel.hide();
		opts.closed = true; 
	}
	

	function destroyPanel(target, forceDestroy){
		var opts = $.data(target, 'fwwindow').options;
		var panel = $.data(target, 'fwwindow').fwpanel;
		
		//if (forceDestroy != true){
		//	if (opts.onBeforeDestroy.call(target) == false) return;
	//	}
		removeNode(panel);
	//	opts.onDestroy.call(target);
	}
	
	function collapsePanel(target, animate){
		var opts = $.data(target, 'fwwindow').options;
		var panel = $.data(target, 'fwwindow').fwpanel;
		var body = panel.find('>div.panel-body');
		var tool = panel.find('>div.panel-header .panel-tool-collapse');
		
		if (tool.hasClass('panel-tool-expand')) return;
		
		body.stop(true, true);	// stop animation
	//	if (opts.onBeforeCollapse.call(target) == false) return;
		
		tool.addClass('panel-tool-expand');
		if (animate == true){
			body.slideUp('normal', function(){
				opts.collapsed = true;
				//opts.onCollapse.call(target);
			});
		} else {
			body.hide();
			opts.collapsed = true;
			//opts.onCollapse.call(target);
		}
	}
	
	function movePanel(target, param){
		var opts = $.data(target, 'fwwindow').options;
		var panel = $.data(target, 'fwwindow').fwpanel;
		if (param){
			if (param.left != null) opts.left = param.left;
			if (param.top != null) opts.top = param.top;
		}
		panel.css({
			left: opts.left,
			top: opts.top
		});
		//opts.onMove.apply(target, [opts.left, opts.top]);
	}

	function expandPanel(target, animate){
		var opts = $.data(target, 'fwwindow').options;
		var panel = $.data(target, 'fwwindow').fwpanel;
		var body = panel.find('>div.panel-body');
		var tool = panel.find('>div.panel-header .panel-tool-collapse');
		
		if (!tool.hasClass('panel-tool-expand')) return;
		
		body.stop(true, true);	// stop animation
		//if (opts.onBeforeExpand.call(target) == false) return;
		
		tool.removeClass('panel-tool-expand');
		if (animate == true){
			body.slideDown('normal', function(){
				opts.collapsed = false;
				//opts.onExpand.call(target);
				loadData(target);
			});
		} else {
			body.show();
			opts.collapsed = false;
			//opts.onExpand.call(target);
			loadData(target);
		}
	}
	
	function maximizePanel(target){
		var opts = $.data(target, 'fwwindow').options;
		var panel = $.data(target, 'fwwindow').fwpanel;
		var tool = panel.find('>div.panel-header .panel-tool-max');
		
		if (tool.hasClass('panel-tool-restore')) return;
		
		setComboxDef(target);
		
		tool.addClass('panel-tool-restore');
		
		$.data(target, 'fwwindow').original = {
			width: opts.width,
			height: opts.height,
			left: opts.left,
			top: opts.top,
			fit: opts.fit
		};
		opts.left = 0;
		opts.top = 0;
		opts.fit = true;
		setSize(target);
		opts.minimized = false;
		opts.maximized = true;
		setCombox(target);
	}
	

	function minimizePanel(target){ 
		var opts = $.data(target, 'fwwindow').options;
		var panel = $.data(target, 'fwwindow').fwpanel;
		panel.hide();
		opts.minimized = true;
		opts.maximized = false; 
	}
	
	function restorePanel(target){
		var opts = $.data(target, 'fwwindow').options;
		var panel = $.data(target, 'fwwindow').fwpanel;
		var tool = panel.find('>div.panel-header .panel-tool-max');
		
		if (!tool.hasClass('panel-tool-restore')) return;
	 
		setComboxDef(target);
		
		panel.show();
		tool.removeClass('panel-tool-restore');
		var original = $.data(target, 'fwwindow').original;
		opts.width = original.width;
		opts.height = original.height;
		opts.left = original.left;
		opts.top = original.top;
		opts.fit = original.fit;
		setSize(target);
		opts.minimized = false;
		opts.maximized = false;
	 	setCombox(target);
	}
	
	function setBorder(target){
		var opts = $.data(target, 'fwwindow').options;
		var panel = $.data(target, 'fwwindow').fwpanel;
		if (opts.border == true){
			panel.find('>div.panel-header').removeClass('panel-header-noborder');
			panel.find('>div.panel-body').removeClass('panel-body-noborder');
		} else {
			panel.find('>div.panel-header').addClass('panel-header-noborder');
			panel.find('>div.panel-body').addClass('panel-body-noborder');
		}
	}
	
	function setTitle(target, title){
		$.data(target, 'fwwindow').options.title = title;
		$(target).panel('header').find('div.panel-title').html(title);
	}
	
	
	//设置下拉框的大小以及显示层

	function setCombox(target){
		//触发事件调整窗口内的控件
		FWresizeObject(target);
	}
	
	//最小化下拉框的宽度
	//因为不设置的化，窗口内的下拉框无法自动收缩

	function setComboxDef(target){}
	
	$(window).unbind('.panel').bind('resize.panel', function(){
		var layout = $('body.layout');
		if (layout.length){
			layout.layout('resize');
		} else {
			$('body>div.panel').triggerHandler('_resize');
		}
	});
	/**
	 * set fwwindow drag and resize property
	 */
	function setProperties(target){
		var state = $.data(target, 'fwwindow');
		
		state.fwpanel.draggable({
			handle: '>div.panel-header>div.panel-title',
			disabled: state.options.draggable == false,
			onStartDrag: function(e){
				if (state.mask) state.mask.css('z-index', $.fn.fwwindow.defaults.zIndex++);
				if (state.shadow) state.shadow.css('z-index', $.fn.fwwindow.defaults.zIndex++);
				var zIndex = $.fn.fwwindow.defaults.zIndex++;
				state.fwpanel.css('z-index', zIndex);
				state.options.zIndex = zIndex;
				setCombox(target);				
				
				if (!state.proxy){
					state.proxy = $('<div class="fwwindow-proxy"></div>').insertAfter(state.fwpanel);
				}
				state.proxy.css({
					display:'none',
					zIndex: $.fn.fwwindow.defaults.zIndex++,
					left: e.data.left,
					top: e.data.top,
					width: ($.boxModel==true ? (state.fwpanel.outerWidth()-(state.proxy.outerWidth()-state.proxy.width())) : state.fwpanel.outerWidth()),
					height: ($.boxModel==true ? (state.fwpanel.outerHeight()-(state.proxy.outerHeight()-state.proxy.height())) : state.fwpanel.outerHeight())
				});
				setTimeout(function(){
					if (state.proxy) state.proxy.show();
				}, 500);
			},
			onDrag: function(e){
				state.proxy.css({
					display:'block',
					left: e.data.left,
					top: e.data.top
				});
				return false;
			},
			onStopDrag: function(e){
				state.options.left = e.data.left;
				if(e.data.top<20){
				  state.options.top = 20;
				}else{
				  state.options.top = e.data.top; 
				}
				movePanel(target);
				state.proxy.remove();
				state.proxy = null;
			}
		});
		
		state.fwpanel.resizable({
			disabled: state.options.resizable == false,
			onStartResize:function(e){
				if (!state.proxy){
					state.proxy = $('<div class="fwwindow-proxy"></div>').insertAfter(state.fwpanel);
				}
				state.proxy.css({
					zIndex: $.fn.fwwindow.defaults.zIndex++,
					left: e.data.left,
					top: e.data.top,
					width: ($.boxModel==true ? (e.data.width-(state.proxy.outerWidth()-state.proxy.width())) : e.data.width),
					height: ($.boxModel==true ? (e.data.height-(state.proxy.outerHeight()-state.proxy.height())) : e.data.height)
				});
			},
			onResize: function(e){
				state.proxy.css({
					left: e.data.left,
					top: e.data.top,
					width: ($.boxModel==true ? (e.data.width-(state.proxy.outerWidth()-state.proxy.width())) : e.data.width),
					height: ($.boxModel==true ? (e.data.height-(state.proxy.outerHeight()-state.proxy.height())) : e.data.height)
				});
				return false;
			},
			onStopResize: function(e){
				state.options.left = e.data.left;
				state.options.top = e.data.top;
				state.options.width = e.data.width;
				state.options.height = e.data.height;
				setSize(target);
				state.proxy.remove();
				state.proxy = null;
			}
		});
	}
	
	function getPageArea() {
		if (document.compatMode == 'BackCompat') {
			return {
				width: Math.max(document.body.scrollWidth, document.body.clientWidth),
				height: Math.max(document.body.scrollHeight, document.body.clientHeight)
			}
		} else {
			return {
				width: Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth),
				height: Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight)
			}
		}
	}
	
	// when fwwindow resize, reset the width and height of the fwwindow's mask
	$(window).resize(function(){
		$('.fwwindow-mask').css({
			width: $(window).width(),
			height: $(window).height()
		});
		setTimeout(function(){
			$('.fwwindow-mask').css({
				width: getPageArea().width,
				height: getPageArea().height
			});
		}, 50);
	});
	
	$.fn.fwwindow = function(options, param){
	 
		if (typeof options == 'string'){
			switch(options){
				case 'open':
					return this.each(function(){
						  openPanel(this);
					});
				case 'close':
					return this.each(function(){
						 closePanel(this);
			   });
			   case 'toggle':
					return this.each(function(){	
					if($(this).hasClass('panel-tool-expand')){
						expandPanel(this, true);
					} else {
						collapsePanel(this, true);
					}		
			  });
		}
			return true;
		}
		
		options = options || {};
		return this.each(function(){
			init(this, options);
			setProperties(this);
		});
	};
	
	$.fn.fwwindow.defaults = {
		zIndex: 9000,
		draggable: true,
		resizable: true,
		shadow: true,
		modal: false, 
		title: null,
		iconCls: null,
		width: 'auto',
		height: 'auto',
		left: null,
		top: null,
		cls: null,
		headerCls: null,
		bodyCls: null,
		style: {},
		cache: true,
		fit: false,
		border: true,
		doSize: true,	 
		noheader: false,
		content: null,	 
	    tools: [],	 
		href: null,
		loadingMessage: 'Loading...',
	 
		title: '新建窗口',
		collapsible: true,
		minimizable: true,
		maximizable: true,
		closable: true, 
		collapsed: false,
		minimized: false,
		maximized: false,
		closed: true,
		onClose : function() {}
	};
})(jQuery);