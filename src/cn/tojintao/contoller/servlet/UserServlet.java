package cn.tojintao.contoller.servlet;

import cn.tojintao.aop.LoggerAop;
import cn.tojintao.aop.NotNullAop;
import cn.tojintao.model.entity.User;
import cn.tojintao.model.dto.ResultInfo;
import cn.tojintao.service.UserService;
import cn.tojintao.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static cn.tojintao.util.JsonUtil.getJsonString;

/**
 * @author cjt
 * @date 2021/6/19 16:29
 */
@WebServlet("/user/*")
public class UserServlet extends BaseServlet{

    UserService userService = (UserService) new NotNullAop().getProxy(new UserServiceImpl());

    /**
     * 用户登录
     * @param request
     * @param response
     * @return
     */
    public ResultInfo<User> login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取json字符串
        String json = getJsonString(request);
        ObjectMapper objectMapper = new ObjectMapper();
        //将json字符串转为User对象
        User user = objectMapper.readValue(json,User.class);
        ResultInfo<User> resultInfo = userService.login(user);
        if(user.getUserId() == null){
            return resultInfo;
        }
        HttpSession session = request.getSession();
        session.setAttribute(String.valueOf(user.getUserId()),user);
        //将登陆用户的id存储到客户端
        Cookie cookie = new Cookie("userId", Integer.toString(user.getUserId()));
        //使得cookie在服务器下的资源都有效，解决跨域问题
        cookie.setPath(request.getContextPath());
        //发送cookie
        response.addCookie(cookie);
        //如果想获取用户的详细信息，也可以以用户id或用户名作为key，将登陆的用户的对象作为value存入session域中
        return resultInfo;
    }

    /**
     * 根据用户id查找用户
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ResultInfo<User> findUserById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getJsonString(request);
        //获取json字符串键值对
        JSONObject jsonObject = JSONObject.fromObject(json);
        int userId = jsonObject.getInt("userId");
        return userService.findUserById(userId);
    }


}
