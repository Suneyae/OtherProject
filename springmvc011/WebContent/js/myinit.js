var wyl_01 = {
    tip    : function(msg){
        alert('i am a tip,msg:'+msg);
    },
    tip2 :function(){
        alert('i am the second');
    }
}

/**
	 * 
	 */
	function wylOnDbClick(objId,FunName){
		if(!typeof FunName=='string'){
			FunName = ""+FunName;
		}
		if(!w[FunName]){
			alert('你还没有定义回调函数:'+FunName);
		}
		
		
		$('#'+objId).click(w[FunName]);
	}
//初始化js，只要网页中引入了改js，即引入了myinit.js，那么页面就会立刻执行wyl_01.tip()这个function
wyl_01.tip('hhhh');
wyl_01.tip2();