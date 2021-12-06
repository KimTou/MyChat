<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>聊天界面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container" style="text-align: center">

    <h2>登录</h2>

    <br><br><br>

    <form class="form-horizontal" method="post">

        <div class="form-group">
            <label for="userName" class="col-sm-3 control-label">用户名</label>
            <div class="col-xs-6">
            <input type="text" class="form-control" id="userName" name="userName" placeholder="用户名">
            </div>
        </div>
        <br><br>
        <div class="form-group">
            <label for="password" class="col-sm-3 control-label">密码</label>
            <div class="col-xs-6">
            <input type="password" class="form-control" id="password" name="password" placeholder="密码">
            </div>
        </div>
        <br><br>
        <button id="login_btn" type="button" class="btn btn-success btn-lg">提交</button>
    </form>
</div>

<script>
    let serverUrl = 'http://localhost:8080/MyChat/'

    let lg_btn = document.getElementById('login_btn')

    lg_btn.onclick = function () {
        //获取登陆各个输入框的值，存放在data对象中
        let data = {
            userName: document.getElementById('userName').value,
            password: document.getElementById('password').value,
        }
        $.ajax({
            url: serverUrl + "user/login",
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            data: JSON.stringify(data),
            async: true,
            success: function (data) {
                if (data.code == 200) {
                    alert(data.msg + "\n欢迎进入MyChat:" + data.data.userName);
                    $(window).attr("location", serverUrl + "function.jsp?"+"type=0&boxId=0") ;
                }
                else {
                    alert(data.msg);
                }
            }
        })
    }

</script>

</body>
</html>
