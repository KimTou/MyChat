package cn.tojintao.contoller.servlet;

import cn.tojintao.aop.NotNullAop;
import cn.tojintao.model.entity.Group;
import cn.tojintao.model.entity.GroupMessage;
import cn.tojintao.model.dto.ResultInfo;
import cn.tojintao.model.vo.BoxVo;
import cn.tojintao.model.vo.MessageVo;
import cn.tojintao.service.ChatService;
import cn.tojintao.service.impl.ChatServiceImpl;
import net.sf.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static cn.tojintao.util.JsonUtil.getJsonString;

/**
 * @author cjt
 * @date 2021/6/20 10:18
 */
@WebServlet("/chat/*")
public class ChatServlet extends BaseServlet{

    ChatService chatService = (ChatService) new NotNullAop().getProxy(new ChatServiceImpl());

    /**
     * 获取聊天记录
     * @param request
     * @return
     * @throws IOException
     */
    public ResultInfo<List<MessageVo>> getChatById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer userId = jsonObject.getInt("userId");
        Integer friendId = jsonObject.getInt("friendId");
        return chatService.getChatById(userId, friendId);
    }

    /**
     * 获取群聊天记录
     * @param request
     * @return
     * @throws IOException
     */
    public ResultInfo<List<GroupMessage>> getGroupChatById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer groupId = jsonObject.getInt("groupId");
        return chatService.getGroupChatById(groupId);
    }

    /**
     * 删除聊天记录
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ResultInfo<?> deleteChat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer userId = jsonObject.getInt("userId");
        Integer friendId = jsonObject.getInt("friendId");
        return chatService.deleteChat(userId, friendId);
    }

    /**
     * 获取所有聊天框
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ResultInfo<List<BoxVo>> getAllChatBox(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer userId = jsonObject.getInt("userId");
        return chatService.getAllChatBox(userId);
    }

    /**
     * 获取所有群聊
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ResultInfo<List<Group>> getAllGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer userId = jsonObject.getInt("userId");
        return chatService.getAllGroup(userId);
    }

    /**
     * 新建群聊
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ResultInfo<?> addGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer userId = jsonObject.getInt("userId");
        String groupName = jsonObject.getString("groupName");
        return chatService.addGroup(userId, groupName);
    }

    /**
     * 新建群聊
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ResultInfo<?> outGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer userId = jsonObject.getInt("userId");
        Integer groupId = jsonObject.getInt("groupId");
        return chatService.outGroup(userId, groupId);
    }

    /**
     * 加入群聊
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ResultInfo<?> intoGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer userId = jsonObject.getInt("userId");
        String groupName = jsonObject.getString("groupName");
        return chatService.intoGroup(userId, groupName);
    }

}
