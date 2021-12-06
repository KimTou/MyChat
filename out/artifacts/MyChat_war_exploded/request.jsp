<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>好友请求</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.cookie.min.js"></script>

</head>
<body onload="refresh()">

<!-- 反色导航条组件  -->
<nav class="navbar navbar-inverse" style="margin-top: 0px;">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">MyChat</a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li>
                    <a href="javascript:void(0)" onclick="back()">返回聊天界面</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/friend.jsp" >好友管理</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/request.jsp" >好友请求</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/group.jsp" >群聊管理</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">

    <h2>请求列表</h2><br>

    <table border="1" class="table table-bordered table-hover">

        <tr class="info">
            <th>用户id</th>
            <th>用户名</th>
            <th>操作</th>
        </tr>
        <tbody id="t_body">

        </tbody>

    </table>
</div>

<script>
    let serverUrl = 'http://localhost:8080/MyChat/'

    function refresh(){
        let data = {
            userId:$.cookie('userId'),
        }

        $.ajax({
            url: serverUrl + "friend/getRequest",
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            data: JSON.stringify(data),
            async: true,
            success: function (data) {
                if (data.data != null) {
                    var table = "";
                    var li = "";
                    var list = data.data;
                    $.each(list,function (i,rs) {
                        table += "<tr>" +
                            "<td>" + list[i].userId + "</td>" +
                            "<td>" + list[i].userName + "</td>" +
                            "<td><button class='btn btn-success' onclick='agree(id)' id='"+ list[i].userId +"'>同意</button>&nbsp;" +
                            "<button class='btn btn-danger' onclick='refuse(id)' id="+list[i].userId+ ">拒绝</button></td>"+
                            "</tr>";

                    })
                    $("#t_body").html(table);
                    $("#lis").html(li);
                }
            }
        })
    }

    function back(){
        $(window).attr("location", serverUrl + "function.jsp?"+"type=0&boxId=0") ;
    }

    //添加好友
    function agree(addId){
        let data = {
            userId:$.cookie('userId'),
            addId: addId
        }

        $.ajax({
            url: serverUrl + "friend/agreeRequest",
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            data: JSON.stringify(data),
            async: true,
            success: function (data) {
                refresh();
            }
        })
    }

    function refuse(addId){
        let data = {
            userId:$.cookie('userId'),
            addId: addId
        }

        $.ajax({
            url: serverUrl + "friend/refuseRequest",
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            data: JSON.stringify(data),
            async: true,
            success: function (data) {
                refresh();
            }
        })
    }

</script>

</body>
</html>
