<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chat.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/layui.js"></script>
</head>
<body>
<div class="layui-layout layui-layout-admin">
<div class="layui-header">
    <div class="layui-logo">MyChat</div>
    <!-- 头部区域（可配合layui 已有的水平导航） -->
    <ul class="layui-nav layui-layout-left">
        <li class="layui-nav-item"><a href="${pageContext.request.contextPath}/friend.jsp">好友管理</a></li>
        <li class="layui-nav-item"><a href="">nav 2</a></li>
        <li class="layui-nav-item"><a href="">nav 3</a></li>
        <li class="layui-nav-item">
            <a href="javascript:;">nav groups</a>
        </li>
    </ul>
    <ul class="layui-nav layui-layout-right">
        <li class="layui-nav-item">
            <a href="javascript:;">
                <img src="${pageContext.request.contextPath}/img/1.jpg" class="layui-nav-img">
                小伙子
            </a>
        </li>
        <li class="layui-nav-item"><a href="">注销</a></li>
    </ul>
</div>

<div class="layui-side layui-bg-black">
    <div class="layui-side-scroll">
        <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
        <ul class="layui-nav layui-nav-tree"  lay-filter="test">
            <li class="layui-nav-item layui-nav-itemed">
                <a class="" href="@{/admin/function}">资料管理</a>
            </li>
            <li class="layui-nav-item">
                <a href="@{/admin/addModule}">模块管理</a>
            </li>
        </ul>
    </div>
</div>
</div>

<div class="layui-body">

    <table border="1" class="table table-bordered table-hover" style="word-break: break-all;word-wrap: break-word;table-layout: fixed">

        <tr class="bg-warning">
            <th>评论</th>
        </tr>
        <tbody id="t_body2">

        </tbody>

    </table>

    <div id="right-page" data-window="-1">

        <div id="message"></div>

    </div>

    <form class="layui-form layui-form-pane" action="">
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">文本域</label>
            <div class="layui-input-block">
                <textarea placeholder="请输入内容" class="layui-textarea"></textarea>
                <button class="btn btn-default btn-lg" onclick="sendOne()" style="float: right">发送</button>
            </div>
        </div>
    </form>
<%--
    <div class="area">
        <textarea class="layui-textarea" id="text" autofocus="autofocus" cols="100" required="required" maxlength="300" ></textarea>
        <button class="btn btn-default btn-lg" onclick="sendOne()" style="float: right">发送</button>
    </div>--%>



</div>

</body>

<script type="text/javascript">
    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        //携带用户id
        websocket = new WebSocket("ws://localhost:8080/QGfish/websocket/"+$.cookie('userId'));
    }
    else {
        alert('当前浏览器 Not support websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function () {
        setMessageInnerHTML("聊天室连接发生错误");
    };

    //连接成功建立的回调方法
    websocket.onopen = function () {
        setMessageInnerHTML("进入聊天室");
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        setMessageInnerHTML(event.data);
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        setMessageInnerHTML("聊天室连接关闭");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //关闭WebSocket连接
    function closeWebSocket() {
        websocket.close();
    }

    //发送消息
    function send() {
        var message = document.getElementById('text').value;
        var userId= $.cookie('userId');

        let data={
            from:userId,
            message:message,
            type:1
        }
        websocket.send(JSON.stringify(data));
    }

    //发送私信
    function sendOne() {
        var to;
        //第一个参数是提示文字，第二个参数是文本框中默认的内容
        to =prompt("请输入私信人的id","");
        if(isNaN(to))
        {
            alert("输入的不是正整数");
            return false;
        }
        if (!(/(^[0-9]*[1-9][0-9]*$)/.test(to))){
            alert("输入的不是正整数");
            return false;
        }

        var message = document.getElementById('text').value;
        var userId=$.cookie('userId');

        let data={
            from:userId,
            to:to,
            message:message,
            type:2
        }
        websocket.send(JSON.stringify(data));
    }
</script>


</body>
</html>
