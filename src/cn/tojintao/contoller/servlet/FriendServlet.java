package cn.tojintao.contoller.servlet;

import cn.tojintao.common.CodeEnum;
import cn.tojintao.dao.UserDao;
import cn.tojintao.dao.impl.UserDaoImpl;
import cn.tojintao.model.entity.User;
import cn.tojintao.model.dto.ResultInfo;
import cn.tojintao.model.vo.UserVo;
import cn.tojintao.service.FriendService;
import cn.tojintao.service.impl.FriendServiceImpl;
import net.sf.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static cn.tojintao.util.JsonUtil.getJsonString;

/**
 * @author cjt
 * @date 2021/6/21 0:20
 */
@WebServlet("/friend/*")
public class FriendServlet extends BaseServlet{

    FriendService friendService = new FriendServiceImpl();

    UserDao userDao = new UserDaoImpl();

    public ResultInfo<User> findUserByName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        String userName = jsonObject.getString("userName");
        User user = userDao.getUserByName(userName);
        request.setAttribute("findUserId", user.getUserId());
        request.setAttribute("findUserName", user.getUserName());
        return ResultInfo.success(CodeEnum.SUCCESS, user);
    }

    /**
     * 查找所有好友
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ResultInfo<List<UserVo>> findAllFriend(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer userId = jsonObject.getInt("userId");
        return friendService.findAllFriend(userId);
    }

    /**
     * 发送添加好友请求
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ResultInfo<?> addFriend(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer userId = jsonObject.getInt("userId");
        String addUserName = jsonObject.getString("addUserName");
        return friendService.addFriend(userId, addUserName);
    }

    /**
     * 发送添加好友请求
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ResultInfo<List<User>> getRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer userId = jsonObject.getInt("userId");
        return friendService.getRequest(userId);
    }

    /**
     * 同意添加好友请求
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ResultInfo<?> agreeRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer userId = jsonObject.getInt("userId");
        Integer addId = jsonObject.getInt("addId");
        return friendService.agreeRequest(userId, addId);
    }

    /**
     * 拒绝添加好友请求
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ResultInfo<?> refuseRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer userId = jsonObject.getInt("userId");
        Integer addId = jsonObject.getInt("addId");
        return friendService.refuseRequest(userId, addId);
    }

    /**
     * 删除好友
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ResultInfo<?> deleteFriend(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        Integer userId = jsonObject.getInt("userId");
        Integer friendId = jsonObject.getInt("friendId");
        return friendService.deleteFriend(userId, friendId);
    }

}
