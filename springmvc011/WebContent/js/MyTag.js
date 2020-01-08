/**
 * theForm:表单名
 * 如<form name="aForm" />
 * 那么这个入参就是 aForm 或者 "aForm"
 */
function myClick(theForm){
	if(window[""+theForm]){
		console.log('true');
	}
	console.log('theForm：'+window[""+theForm]);
	
	if(typeof theForm == 'string'){
		//判断入参是否为string
		var formStr = " var theForm = document."+theForm+";";
		eval(formStr);
	}
	
	//循环表单的方式
	var tagObjs = theForm.elements;
	for(var i=0;i<tagObjs.length;i++){
		//控制台打印输出值
		console.log("tagObjs["+i+"]:"+tagObjs[i].value);
	}
	console.log('length:'+tagObjs.length);
	return true;

}