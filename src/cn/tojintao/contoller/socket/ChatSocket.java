package cn.tojintao.contoller.socket;

import cn.tojintao.aop.LoggerAop;
import cn.tojintao.aop.NotNullAop;
import cn.tojintao.dao.UserDao;
import cn.tojintao.dao.impl.UserDaoImpl;
import cn.tojintao.model.entity.GroupMessage;
import cn.tojintao.model.entity.Message;
import cn.tojintao.model.entity.User;
import cn.tojintao.model.vo.MessageVo;
import cn.tojintao.service.ChatService;
import cn.tojintao.service.impl.ChatServiceImpl;
import cn.tojintao.util.DateUtil;
import cn.tojintao.util.LoggerUtil;
import net.sf.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cjt
 * @date 2021/6/20 9:25
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/chatSocket/{userId}")
public class ChatSocket {

    /**
     * 增强service（字符串判空）
     */
    private final ChatService chatService = (ChatService) new NotNullAop().getProxy(new ChatServiceImpl());

    /**
     * 增强 Dao（打印日志）
     */
    private final UserDao userDao = (UserDao) new LoggerAop().getProxy(new UserDaoImpl());

    /**
     * 日志工具
     */
    LoggerUtil logger = LoggerUtil.getLogger();

    /**
     * 静态变量，用来记录当前在线连接数。
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Map，用来存放每个在线客户端对应的ChatSocket对象。
     */
    private static final Map<Integer,ChatSocket> clients = new ConcurrentHashMap<Integer,ChatSocket>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     *  当前用户
     */
    private User user;

    /**
     * 连接建立成功调用的方法
     * @param userId  使用者的id
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     * @throws IOException
     */
    @OnOpen
    public void onOpen(@PathParam("userId")Integer userId, Session session) throws IOException {
        ChatSocket chatSocket = clients.get(userId);
        if(chatSocket != null && chatSocket.session != null && chatSocket.session.isOpen()){
            chatSocket.session.close();
        }
        chatSocket = new ChatSocket();
        chatSocket.session = session;
        chatSocket.user = userDao.getUserById(userId);
        clients.put(userId, chatSocket);
        //在线数加1
        addOnlineCount();
        logger.info("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param jsonMsg 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String jsonMsg, Session session) throws IOException {
        //解析页面传送过来的json字符串
        JSONObject jsonObject = JSONObject.fromObject(jsonMsg);
        //获取消息内容
        String message = jsonObject.getString("message");
        //获取发送者id
        int sender = jsonObject.getInt("sender");
        //type为1，代表群聊消息
        if(jsonObject.getInt("type") == 1) {
            int groupId = jsonObject.getInt("groupId");
            //发给每一个在线用户
            sendGroupMessage(sender, groupId, message);
        }
        //type为2，代表私信
        else {
            //获取私信人id
            int receiver = jsonObject.getInt("receiver");
            sendMessage(sender, receiver, message);
        }
        logger.info("来自客户端的消息:" + message);
    }

    /**
     * 群聊
     * @param mes
     * @throws IOException
     */
    public void sendGroupMessage(Integer sender, Integer groupId, String mes) throws IOException{
        User userById = userDao.getUserById(sender);
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setGroupId(groupId);
        groupMessage.setSender(sender);
        groupMessage.setSenderName(userById.getUserName());
        groupMessage.setSenderAvatar(userById.getAvatar());
        groupMessage.setContent(mes);
        groupMessage.setGmtCreate(DateUtil.getDate());
        chatService.saveGroupMessage(groupMessage);
        String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(groupMessage);
        List<Integer> groupUser = userDao.getGroupUser(groupId);
        for(Integer userId : groupUser){
            ChatSocket chatSocket = clients.get(userId);
            if(chatSocket!=null && chatSocket.session!=null && chatSocket.session.isOpen()){
                chatSocket.session.getBasicRemote().sendText(jsonString);
            }
        }
    }

    /**
     * 私聊
     * @param mes
     * @throws IOException
     */
    public void sendMessage(Integer sender, Integer receiver, String mes) throws IOException{
        ChatSocket userSocket = clients.get(sender);
        ChatSocket receive = clients.get(receiver);
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(mes);
        message.setGmtCreate(DateUtil.getDate());
        //将消息存入数据库
        chatService.saveMessage(message);
        //封装页面展示对象
        MessageVo messageVo = new MessageVo();
        messageVo.setId(message.getId());
        messageVo.setSender(message.getSender());
        messageVo.setReceiver(message.getReceiver());
        messageVo.setContent(message.getContent());
        messageVo.setGmtCreate(message.getGmtCreate());
        messageVo.setSenderName(userSocket.user.getUserName());
        messageVo.setSenderAvatar(userSocket.user.getAvatar());
        //转json字符串
        String jsonString = com.alibaba.fastjson.JSONObject.toJSONString(messageVo);
        if(userSocket!=null && userSocket.session!=null && userSocket.session.isOpen()){
            userSocket.session.getBasicRemote().sendText(jsonString);
        }
        if(receive!=null && receive.session!=null && receive.session.isOpen()){
            receive.session.getBasicRemote().sendText(jsonString);
        }
    }

    /**
     * 查询用户在线状态
     * @param userId
     * @return
     */
    public static boolean getUserStatus(Integer userId){
        ChatSocket socket = clients.get(userId);
        if(socket != null && socket.session != null && socket.session.isOpen()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        //在线数减1
        subOnlineCount();
        logger.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        logger.danger("发生错误");
        error.printStackTrace();
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        ChatSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        ChatSocket.onlineCount--;
    }

}
