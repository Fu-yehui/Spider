<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--引入jstl -->
<%@include file="/WEB-INF/jsp/common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>Spider Management</title>
    <%@include file="/WEB-INF/jsp/common/head.jsp"%>


    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<!-- 页面显示部分 -->
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading text-center">
            <h2>Spider Management</h2>
        </div>
        <div class="panel-body">

            <form class="form-horizontal" role="form">
                <div class="form-group">
                    <label for="threadCount" class="col-sm-2 control-label">线程数量</label>
                    <div class="col-sm-10">
                        <input type="number" class="form-control" id="threadCount"
                               name="threadCount"
                               placeholder="请输入爬虫线程数量, >= 1">
                    </div>
                </div>
                <div class="form-group">
                    <label for="retryCount" class="col-sm-2 control-label">重试次数</label>
                    <div class="col-sm-10">
                        <input type="number" class="form-control" id="retryCount"
                               name="retryCount"
                               placeholder="爬取url失败后的重试次数, >= 0">
                    </div>
                </div>
                <div class="form-group">
                    <label for="sleepTime" class="col-sm-2 control-label">间隔时间</label>
                    <div class="col-sm-10">
                        <input type="number" class="form-control" id="sleepTime"
                               name="sleepTime"
                               placeholder="每个线程执行一次爬取后的间隔时间, >= 0">
                    </div>
                </div>

                <div class="form-group">
                    <label  class="col-sm-2 control-label">代理</label>
                    <div class="col-sm-10">
                        <label class="radio-inline">
                            <input type="radio" name="useProxy" id="optionsRadios1" value="true" checked>  开启
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="useProxy" id="optionsRadios2"  value="false"> 关闭
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <label for="url" class="col-sm-2 control-label">URL</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="url"
                               name="url"
                               placeholder="设定预爬取的URL"
                                value="https://www.dotcpp.com/oj/problemset.php?page=1">
                    </div>
                </div>


                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-default">登录</button>
                    </div>
                </div>
            </form>



        </div>
    </div>
</div>




</body>
<script src="/resources/script/spider.js" type="text/javascript"></script>

<script type="text/javascript">
    $(function () {
        //使用EL表达式传入参数
        seckill.detail.init({
            seckillId:${seckill.seckillId},
            startTime:${seckill.startTime.time},//毫秒
            endTime:${seckill.endTime.time}
        });
    })
</script>
</html>
