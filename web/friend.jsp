<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>好友管理</title>
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

    <h2>添加好友</h2><br>
    <form class="navbar-form navbar-left" role="search">
        <div class="form-group">
            <label for="userName">用户名</label>
            <input type="text" id="userName" class="form-control" placeholder="请输入用户名" autocomplete="off">
        </div>
        <%--        查询按钮不能为submit，应该为button！！！--%>
        <button type="button" class="btn btn-default" onclick="addFriend()">添加好友</button>
    </form>

    <br><hr><br>

    <h2>好友列表</h2><br>

    <table border="1" class="table table-bordered table-hover">

        <tr class="info">
            <th>用户名</th>
            <th>当前状态</th>
            <th>进入聊天</th>
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
            url: serverUrl + "friend/findAllFriend",
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
                            "<td>" + list[i].user.userName + "</td>";
                            if(list[i].status){
                                table += "<td>在线状态</td>";
                            }else{
                                table += "<td>离线状态</td>";
                            }
                            table +=
                            "<td><button class='btn btn-default btn-primary' onclick='tochat(id)' id='" + list[i].user.userId + "'>进入聊天</button>&nbsp;" +
                            "<td><button class='btn btn-default btn-danger' onclick='deleteFriend(id)' id='" + list[i].user.userId + "'>删除好友</button>&nbsp;" +
                            "</tr>";
                        $("#t_body").html(table);
                        $("#lis").html(li);
                    })
                }
            }
        })
    }

    function back(){
        $(window).attr("location", serverUrl + "function.jsp?"+"type=0&boxId=0") ;
    }

    //与好友聊天
    function tochat(boxId){
        $(window).attr("location", serverUrl + "function.jsp?"+"type=2&boxId="+boxId) ;
    }

    //删除好友
    function deleteFriend(friendId){
        let data = {
            userId:$.cookie('userId'),
            friendId: friendId
        }

        $.ajax({
            url: serverUrl + "friend/deleteFriend",
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

    //添加好友
    function addFriend(){
        let data = {
            userId:$.cookie('userId'),
            addUserName: $('#userName').val()
        }

        $.ajax({
            url: serverUrl + "friend/addFriend",
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            data: JSON.stringify(data),
            async: true,
            success: function (data) {
                if(data.code==200){
                    alert("已发送好友请求，请等待对方同意")
                }else{
                    alert(data.data)
                }
            }
        })
    }


</script>

</body>
</html>
