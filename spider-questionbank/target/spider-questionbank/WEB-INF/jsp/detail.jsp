<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--引入jstl -->
<%@include file="common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>${question.title}</title>
    <%@include file="common/head.jsp"%>


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
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">题目描述</h3>
                </div>
                <div class="panel-body">
                    ${question.description}
                </div>
            </div>

            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">输入</h3>
                </div>
                <div class="panel-body">
                    ${question.input}
                </div>
            </div>

            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">输出</h3>
                </div>
                <div class="panel-body">
                    ${question.output}
                </div>
            </div>

            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">样例输入</h3>
                </div>
                <div class="panel-body">
                    <pre >${question.sampleInput}</pre>
                </div>
            </div>

            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">样例输出</h3>
                </div>
                <div class="panel-body">
                    <pre >${question.sampleOutput}</pre>
                </div>
            </div>

            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">提示</h3>
                </div>
                <div class="panel-body">
                    ${question.prompt}
                </div>
            </div>

            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">标签</h3>
                </div>
                <div class="panel-body">
                    ${question.label}
                </div>
            </div>



        </div>
    </div>
</div>




</body>
</html>
