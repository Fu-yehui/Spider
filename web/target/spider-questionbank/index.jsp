<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--引入jstl -->
<%@include file="/WEB-INF/jsp/common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>${question.title}</title>
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
            <h2>${question.title}</h2>
        </div>
        <div class="panel-body">

        </div>
    </div>
</div>




</body>
</html>
