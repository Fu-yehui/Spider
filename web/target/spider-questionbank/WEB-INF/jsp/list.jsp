<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--引入jstl -->
<%@include file="common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>题库列表</title>
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
            <h2>题库</h2>
        </div>
        <div class="panel-body">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>题名</th>
                    <th>标签</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="question" items="${list}">
                    <tr>
                        <td>
                            <span class="left">
                                <a  href="/question/${question.id}/detail" target="_blank">${question.title}</a>
                            </span>
                        </td>
                        <td>
                            <span class="label label-primary">${question.label}</span>
                        </td>

                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>




</body>
</html>
