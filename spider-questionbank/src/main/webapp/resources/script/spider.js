//存放主要交互逻辑js代码
//javascript 模块化

var seckill = {
    //封装秒杀相关ajax的url
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/'+seckillId+"/exposer";
        },
        execution:function(seckillId,md5){
            return '/seckill/'+seckillId+'/'+md5+'/execution';
        }
    },

    //验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;//直接判断对象会看对象是否为空,空就是undefine就是false; isNaN 非数字返回true
        } else {
            return false;
        }
    },

    handlerSeckill:function(seckillId,node){
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        console.log("exposerUrl: "+seckill.URL.exposer(seckillId));
        $.get(seckill.URL.exposer(seckillId),{},function(result){
            if(result && result['success']){
                var exposer=result['data'];
                if(exposer['exposed']){
                    var md5=exposer['md5'];
                    var killUrl=seckill.URL.execution(seckillId,md5);
                    console.log("killUrl: "+killUrl);

                    //绑定一次点击事件
                    $('#killBtn').one('click',function () {
                        //1.禁用按钮
                        $(this).addClass('disabled');
                        //2.发送秒杀请求
                        $.post(killUrl,{},function(result){
                            if(result && result['success']){
                                var seckillExecution=result['data'];
                                var state=seckillExecution['state'];
                                var stateInfo=seckillExecution['stateInfo'];
                                if(seckillExecution['successKilled']) {
                                    var successKilled = seckillExecution['successKilled'];
                                    var userPhone = successKilled['userPhone'];
                                    var createTime = new Date(successKilled['createTime']);
                                    console.log("state: " + state + " stateInfo: " + stateInfo + " userPhone: " + userPhone + " createTime: " + createTime);
                                }

                                //3.显示秒杀结果
                                node.html('<span class="label label-success">'+stateInfo+'</span>')
                            }
                        });
                    });

                    node.show();


                }else{
                    //还未开启秒杀，客户机时间不一致
                    var now=exposer['now'];
                    var start=exposer['startTime'];
                    var end=exposer['endTime'];
                    //重新计算计时逻辑
                    seckill.countdown(seckillId,now,start,end);
                }
            }else{
                console.log('result: '+result);
            }
        });

    },


    countdown: function (seckillId, nowTime, startTime, endTime) {
        console.log(seckillId + '_' + nowTime + '_' + startTime + '_' + endTime);
        var seckillBox = $('#seckill-box');
        if (nowTime > endTime) {
            //秒杀结束
            seckillBox.html('秒杀结束!');
        } else if (nowTime < startTime) {
            //秒杀未开始,计时事件绑定
            var killTime = new Date(startTime + 1000);//todo 防止时间偏移
            seckillBox.countdown(killTime, function (event) {
                //时间格式
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒 ');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                //时间完成后回调事件
                //获取秒杀地址,控制现实逻辑,执行秒杀
                console.log('______fininsh.countdown');
                seckill.handlerSeckill(seckillId, seckillBox);
            });
        }else {
            //秒杀开始
            seckill.handlerSeckill(seckillId, seckillBox);
        }
    },


    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证和登入，计时交互
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                var killPhoneModel = $('#killPhoneModal');
                //显示弹出层
                killPhoneModel.modal({
                    show: true,
                    backdrop: 'static',
                    keyboard: false
                });

                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    console.log('inputPhone: ' + inputPhone);
                    if (seckill.validatePhone(inputPhone)) {
                        //电话写入cookie
                        $.cookie('killPhone', inputPhone, {expires: 1, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误</label>').show(300);
                    }
                });
            }
            //已经登入
            //显示计时
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];

            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];

                    //时间判断
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result: ' + result);
                }

            });


        }
    }
}