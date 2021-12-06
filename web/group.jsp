<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>群聊管理</title>
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

    <h2>添加群聊</h2><br>
    <form class="navbar-form navbar-left" role="search">
        <div class="form-group">
            <label for="groupName">群聊名称</label>
            <input type="text" id="groupName" class="form-control" placeholder="请输入群聊名称" autocomplete="off">
        </div>
        <button type="button" class="btn btn-default" onclick="intoGroup()">加入群聊</button>
    </form>

    <br><hr><br>

    <h2>新建群聊</h2>
    <form class="navbar-form navbar-left" role="search">
        <button type="button" class="btn btn-success btn-lg" onclick="addGroup()">新建群聊</button>
    </form>

    <br><hr><br>

    <h2>我的群聊</h2><br>

    <table border="1" class="table table-bordered table-hover">

        <tr class="info">
            <th>群聊名称</th>
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
            url: serverUrl + "chat/getAllGroup",
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
                            "<td>" + list[i].groupName + "</td>";
                        table +=
                            "<td><button class='btn btn-primary ' onclick='tochat(id)' id='" + list[i].groupId + "'>进入群聊</button>&nbsp;" +
                            "<td><button class='btn btn-danger ' onclick='outGroup(id)' id='" + list[i].groupId + "'>退出群聊</button>&nbsp;" +
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

    //进入群聊
    function tochat(boxId){
        $(window).attr("location", serverUrl + "function.jsp?"+"type=1&boxId="+boxId) ;
    }

    //退出群聊
    function outGroup(groupId){
        let data = {
            userId:$.cookie('userId'),
            groupId: groupId
        }

        $.ajax({
            url: serverUrl + "chat/outGroup",
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

    //加入群聊
    function intoGroup(){
        let data = {
            userId:$.cookie('userId'),
            groupName: $('#groupName').val()
        }

        $.ajax({
            url: serverUrl + "chat/intoGroup",
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

    function addGroup(){
        //第一个参数是提示文字，第二个参数是文本框中默认的内容
        let groupName =prompt("请输入群聊名称","");

        if(groupName === "null"){
            return;
        }

        let data = {
            userId:$.cookie('userId'),
            groupName: groupName
        }

        $.ajax({
            url: serverUrl + "chat/addGroup",
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
