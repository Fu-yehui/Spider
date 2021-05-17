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
<center>
<button id="start_button" type="button" class="btn btn-success">启动爬虫</button>
<button id="stop_button" type="button" class="btn btn-warning" disabled="true" >停止爬虫</button>
</center>

<div class="modal fade" id="msgModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    Spider Message
                </h4>
            </div>
            <div  class="modal-body">
                <span id="msg" class="glyphicon"> </span>
            </div>

        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

</body>
<script src="/resources/script/spider.js" type="text/javascript"></script>

<script type="text/javascript">
    $(function () {

        spider.spdierManagment.init({

        });
    })
</script>
</html>
