package cn.tojintao.service.impl;

import cn.tojintao.aop.LoggerAop;
import cn.tojintao.common.CodeEnum;
import cn.tojintao.contoller.socket.ChatSocket;
import cn.tojintao.dao.FriendDao;
import cn.tojintao.dao.UserDao;
import cn.tojintao.dao.impl.FriendDaoImpl;
import cn.tojintao.dao.impl.UserDaoImpl;
import cn.tojintao.model.entity.User;
import cn.tojintao.model.dto.ResultInfo;
import cn.tojintao.model.vo.UserVo;
import cn.tojintao.service.FriendService;

import java.util.LinkedList;
import java.util.List;

/**
 * @author cjt
 * @date 2021/6/21 0:15
 */
public class FriendServiceImpl implements FriendService {

    /**
     * 增强 Dao（打印日志）
     */
    private final FriendDao friendDao = (FriendDao) new LoggerAop().getProxy(new FriendDaoImpl());

    private final UserDao userDao = (UserDao) new LoggerAop().getProxy(new UserDaoImpl());

    /**
     * 获取所有好友
     * @param userId
     * @return
     */
    @Override
    public ResultInfo<List<UserVo>> findAllFriend(Integer userId) {
        List<User> allFriend = friendDao.findAllFriend(userId);
        List<UserVo> all = new LinkedList<>();
        for(User user: allFriend){
            UserVo userVo = new UserVo();
            userVo.setUser(user);
            userVo.setStatus(ChatSocket.getUserStatus(user.getUserId()));
            all.add(userVo);
        }
        return ResultInfo.success(CodeEnum.SUCCESS, all);
    }

    /**
     * 发送好友添加请求
     * @param userId
     * @param addUserName
     * @return
     */
    @Override
    public ResultInfo<?> addFriend(Integer userId, String addUserName) {
        User addUser = userDao.getUserByName(addUserName);
        if(addUser == null){
            return ResultInfo.error(CodeEnum.PARAM_NOT_IDEAL, "未查找到该用户");
        }else{
            //判断是否已发送添加好友请求
            if(friendDao.requestIsExist(userId, addUser.getUserId())){
                return ResultInfo.error(CodeEnum.BAD_REQUEST, "你已发送过添加请求，请等待对方回应");
            }else{
                friendDao.insertRequest(userId, addUser.getUserId());
                return ResultInfo.success(CodeEnum.SUCCESS);
            }
        }
    }

    /**
     * 获取好友请求
     * @param userId
     * @return
     */
    @Override
    public ResultInfo<List<User>> getRequest(Integer userId) {
        //获取请求用户id
        List<Integer> requestIdList = friendDao.getRequest(userId);
        List<User> requestUserList = new LinkedList<>();
        for(Integer requestId : requestIdList){
            //获取请求用户对象详细信息
            User requestUser = userDao.getUserById(requestId);
            requestUserList.add(requestUser);
        }
        return ResultInfo.success(CodeEnum.SUCCESS, requestUserList);
    }

    /**
     * 同意添加好友请求
     * @param userId
     * @param addId
     * @return
     */
    @Override
    public ResultInfo<?> agreeRequest(Integer userId, Integer addId) {
        if(friendDao.isFriend(userId, addId)){
            //判断是否已经是好友关系，是则不再添加
            friendDao.deleteRequest(userId, addId);
            return ResultInfo.success(CodeEnum.SUCCESS);
        }
        friendDao.beFriend(userId, addId);
        friendDao.deleteRequest(userId, addId);
        return ResultInfo.success(CodeEnum.SUCCESS);
    }

    /**
     * 拒绝添加好友
     * @param userId
     * @param addId
     * @return
     */
    @Override
    public ResultInfo<?> refuseRequest(Integer userId, Integer addId) {
        friendDao.deleteRequest(userId, addId);
        return ResultInfo.success(CodeEnum.SUCCESS);
    }

    /**
     * 删除好友
     * @param userId
     * @param friendId
     * @return
     */
    @Override
    public ResultInfo<?> deleteFriend(Integer userId, Integer friendId) {
        friendDao.deleteFriend(userId, friendId);
        return ResultInfo.success(CodeEnum.SUCCESS);
    }
}
