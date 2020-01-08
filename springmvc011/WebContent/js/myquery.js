
//实现自己的MyQuery框架
var MyQuery = function(selector){
    if ( window == this ) return new MyQuery(selector);
    //这里只实现dom类型的简单查找,嘿嘿
    var doms = document.getElementsByTagName(selector);
    var arr = [];
    for(var i=0; i<doms .length; i++){
        arr.push(doms.item(i));
    }
    return this.setArray(arr);
}
MyQuery.prototype.setArray = function( arr ) {
        this.length = 0;
        [].push.apply( this, arr );
        return this;
}
MyQuery.fn = MyQuery.prototype;
var $ = MyQuery;
//插件扩展 1)each
MyQuery.fn.each = function(method){
    for(var i=0,l=this.length; i<l; i++){
        method.call(this[i],i);
    }
}
//插件扩展 2)show
MyQuery.fn.show = function(){
    this.each(function(i){
    	/*alert("第 "+i+" 个div : id= "+this.id+" : 内容："+this.innerHTML);*/
        console.log("第 "+i+" 个div : id= "+this.id+" : 内容："+this.innerHTML);
    });
}
//debugger
$("div").show();
//第 0 个div : id= d : 内容：divvv
//第 1 个div : id= fsd : 内容：fdsf
