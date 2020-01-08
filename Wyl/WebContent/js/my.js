(function(j) { // 这里的j是一个形参，表示传入的jQuery对象，j可以任意填写
    j.extend({ // 相当于给jQuery对象加上了一个属性readName，而这个属性是一个方法
        // 通过传入的jQuery对象，
        // 然后再用jQuery.exetend(functionName:function(){});
        // 这种方式扩展jQuery的方法,
        // 使用方法或者说是调用方式：$.readName('I am 你大爷 ');
        this_: this,
        timeId: null,
        flagWyl: null,
        readName: function(name) {
            // alert(typeof this);
            // alert(typeof window);
            // alert(typeof this.name);
            console.log(typeof this.name);
            console.log(typeof this);
            if (name == null || name == undefined || name == '') {
                // alert('没有入参name！');
                console.log('没有入参name！')

            } else {
                // alert('入参name：'+name);
                console.log('入参name：' + name);
            }
        },
        //封装提示的内容：
        Walert: function(msg) {
            alert('系统异常：' + msg);
        },
        /**
		 * toId:需要设置时间的表单的Id,
		 * flag:true的时候，设置本地时间格式，false,设置成通用格式
		 */
        WsetTime: function(toId, flag) {
            var d = new Date();
            //			window.setInterval('$.WsetTime('+toId+','+flag+')',10000);
            if (flag) {
                $('#' + toId + '').attr('value', d.toLocaleString());
            } else {
                $('#' + toId + '').attr('value', d.toTimeString());
            }
            //			$.this_ = this;//把当前对象传给 存储临时变量的对象，当前对象即WsetTime()这个函数
            $.timeId = toId;
            $.flagWyl = flag;
        },
        /**
		 * 设置时间间隔
		 */
        WsetGap: function(intervalTime) {
            if ($.timeId) {
                window.setInterval('$.WsetTime(' + $.timeId + ',' + $.flagWyl + ')', intervalTime);
            }
        },
        /**
		 * 封装console.log()方法
		 */
        logW: function(paras) {
            console.log('logW:' + paras);
        },
        /**
		 * targetId:input标签的id
		 * flag:设置时间间隔，如果不设置，那么就间隔默认为1000毫秒
		 * desc:如果设置了那么在时间前面就会加描述
		 * 用例：
		 * <input id="xxx" />
		 * 1 $.myClock('xxx');
		 * 2 $.myClock('xxx',true,900);
		 * 3 $.myClock('xxx',true,900,2333);
		 * 
		 *  document.getElementById("clock").value = t;
		 */
        myClock: function(targetId, flag, interval, desc) {
            if (!flag) {
                interval = 1000;
            }
            var $target = $('#' + targetId + '');
            var desc_ = '';
            if (desc) {
                desc_ = desc;
            }
            var theTimer = window.setInterval(function() {
                var time = new Date().toLocaleTimeString();
                $target.attr('value', desc_ + time.toString());
            },
            interval);
            return theTimer;
        }

    })
})(jQuery) // 这个jQuer一定要这样写，表示传入jQuery对象
