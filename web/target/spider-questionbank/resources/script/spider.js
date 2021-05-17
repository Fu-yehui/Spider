//存放主要交互逻辑js代码
//javascript 模块化

var spider = {
    //封装秒杀相关ajax的url
    URL: {
        start: function () {
            return '/spider/fetch';
        },
        stop: function (seckillId) {
            return '/spider/stop';
        }
    },



    //
    spdierManagment: {
        //初始化
        init: function () {

            //绑定一次点击事件
            $('#start_button').one('click',function () {
                //1.禁用开启按钮
                // $(this).addClass('disabled');
                $(this).attr("disabled",true);


                //2.发送开启爬虫请求
                $.get(spider.URL.start(), {}, function (result) {
                    //开启停止按钮
                    $('#stop_button').attr("disabled",false);

                    var msgModal = $('#msgModal');
                    if (result && result['success']) {
                        var stringDto = result['data'];
                        var successMsg=stringDto['message'];
                        msgModal.modal({
                            show: true,
                            backdrop: 'static',
                            keyboard: false
                        });
                        $('#msg').hide().html('<span class="label label-success">'+successMsg+'</span>').show(300);
                    }else{
                        var errorMsg = result['error'];
                        msgModal.modal({
                            show: true,
                            backdrop: 'static',
                            keyboard: false
                        });
                        $('#msg').hide().html('<span class="label label-danger">'+errorMsg+'</span>').show(300);
                    }
                });
            });




            $('#stop_button').one('click',function () {
                //1.禁用停止按钮
                $(this).addClass('disabled');


                //2.发送停止爬虫请求
                $.get(spider.URL.stop(), {}, function (result) {
                    //开启启动按钮
                    $('#start_button').attr("disabled",false);

                    var msgModal = $('#msgModal');
                    if (result && result['success']) {
                        var counter = result['data'];
                        var total=counter['total'];
                        var newData=counter['newData'];
                        msgModal.modal({
                            show: true,
                            backdrop: 'static',
                            keyboard: false
                        });
                        $('#msg').hide().html('<span class="label label-success">本次爬虫总获取题目数量：'+total+'</span> <br><span class="label label-success">其中新的题目数量为：'+newData+'</span>').show(300);
                    }else{
                        var errorMsg = result['error'];
                        msgModal.modal({
                            show: true,
                            backdrop: 'static',
                            keyboard: false
                        });
                        $('#msg').hide().html('<span class="label label-danger">'+errorMsg+'</span>').show(300);
                    }
                });
            });
        }
    }
}