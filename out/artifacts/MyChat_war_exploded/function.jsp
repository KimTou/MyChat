<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>MyChat</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chat.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.cookie.min.js"></script>

</head>
<body onload="refresh(${param.type},${param.boxId})">

<div class="main">
    <div class="top">
        <div class="top-left">
            <div class="header"></div>
            <div class="search">
                <input type="text" id="mine">
                <%--<button id="delete" value="" onclick="deleteChat()">清除聊天记录</button>--%>
                <i class="icon-sear"></i>
            </div>
        </div>
        <div class="top-type">
            <a href="${pageContext.request.contextPath}/friend.jsp" class="news icon-site"></a>
            <a href="${pageContext.request.contextPath}/request.jsp" class="friend icon-site"></a>
            <a href="${pageContext.request.contextPath}/group.jsp" class="file icon-site"></a>
        </div>
    </div>

    <div class="box">
        <%--左部好友--%>
        <div class="chat-list" id="chat-list">
            <%--<div class="list-box">
                <img class="chat-head" src="${sessionScope.user.avatar}" alt="">
                <div class="chat-rig">
                    <p class="title">公众号</p>
                    <p class="text">紧急！！！</p>
                </div>
            </div>

            <div class="list-box">
                <img class="chat-head" src="../static/img/img-header2.jpg" alt="">
                <div class="chat-rig"><p class="title">安安安</p>
                    <p class="text">你好，我这里有个任务帮我做一下</p></div>
            </div>--%>
        </div>

        <div class="box-right" id="chat-content">
           <%-- <div class="recvfrom">
                <div class="nav-top">
                    <p>公众号</p>
                </div>
                <div class="news-top">
                    <ul>
                        <li class="other">
                            <div class="avatar"><img src="../static/img/img-header2.jpg" alt=""></div>
                            <div class="msg">
                                <p class="msg-name">宋温暖</p>
                                <p class="msg-text">请接下这个活 <emoji class="pizza"></emoji></p>
                                <time>20:18</time>
                            </div>
                        </li>
                        <li class="self">
                            <div class="avatar"><img src="../static/img/img-header2.jpg" alt=""></div>
                            <div class="msg">
                                <p class="msg-name">安安安</p>
                                <p class="msg-text">请接下这个下这个活请接下接下这个活请接下接下这个活请接下下这个活请接下这个活<emoji class="pizza"></emoji></p>
                                <time>20:18</time>
                            </div>
                        </li>
                        <li class="other">
                            <div class="avatar"><img src="../static/img/img-header2.jpg" alt=""></div>
                            <div class="msg">
                                <p class="msg-name">Name</p>
                                <p class="msg-text">请接下这个活 <emoji class="pizza"></emoji></p>
                                <time>20:18</time>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>--%>
            <%--<div class="sendto">
                <div class="but-text">
                    <textarea name="" id="message" cols="110" rows="6"></textarea>
                    <a class="button" onclick="sendOne(3)">发送</a>
                </div>
            </div>--%>
        </div>

    </div>

</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/function.js"></script>

</body>
</html>