package cn.tojintao.service.impl;

import cn.tojintao.aop.LoggerAop;
import cn.tojintao.aop.annotation.NotNull;
import cn.tojintao.common.CodeEnum;
import cn.tojintao.contoller.socket.ChatSocket;
import cn.tojintao.dao.ChatDao;
import cn.tojintao.dao.FriendDao;
import cn.tojintao.dao.UserDao;
import cn.tojintao.dao.impl.ChatDaoImpl;
import cn.tojintao.dao.impl.FriendDaoImpl;
import cn.tojintao.dao.impl.UserDaoImpl;
import cn.tojintao.model.entity.Group;
import cn.tojintao.model.entity.GroupMessage;
import cn.tojintao.model.entity.Message;
import cn.tojintao.model.entity.User;
import cn.tojintao.model.dto.ResultInfo;
import cn.tojintao.model.vo.BoxVo;
import cn.tojintao.model.vo.MessageVo;
import cn.tojintao.model.vo.UserVo;
import cn.tojintao.service.ChatService;

import java.util.LinkedList;
import java.util.List;

/**
 * @author cjt
 * @date 2021/6/20 20:20
 */
public class ChatServiceImpl implements ChatService {

    /**
     * 增强 Dao（打印日志）
     */
    private final ChatDao chatDao = (ChatDao) new LoggerAop().getProxy(new ChatDaoImpl());

    private final UserDao userDao = (UserDao) new LoggerAop().getProxy(new UserDaoImpl());

    private final FriendDao friendDao = (FriendDao) new LoggerAop().getProxy(new FriendDaoImpl());

    /**
     * 获取聊天记录
     * @param userId
     * @param friendId
     * @return
     */
    @Override
    public ResultInfo<List<MessageVo>> getChatById(Integer userId, Integer friendId) {
        List<MessageVo> chats = chatDao.getChatById(userId, friendId);
        return ResultInfo.success(CodeEnum.SUCCESS, chats);
    }

    /**
     * 获取群聊天记录
     * @param groupId
     * @return
     */
    @Override
    public ResultInfo<List<GroupMessage>> getGroupChatById(Integer groupId) {
        List<GroupMessage> groupChatMes = chatDao.getGroupChatById(groupId);
        return ResultInfo.success(CodeEnum.SUCCESS, groupChatMes);
    }

    /**
     * 保存聊天记录
     * @param message
     * @return
     */
    @Override
    public ResultInfo<Message> saveMessage(Message message) {
        chatDao.saveMessage(message);
        return ResultInfo.success(CodeEnum.SUCCESS, message);
    }

    /**
     * 保存群聊
     * @param groupMessage
     * @return
     */
    @Override
    public ResultInfo<GroupMessage> saveGroupMessage(GroupMessage groupMessage) {
        chatDao.saveGroupMessage(groupMessage);
        return ResultInfo.success(CodeEnum.SUCCESS, groupMessage);
    }

    /**
     * 删除聊天记录
     * @param userId
     * @param friendId
     * @return
     */
    @Override
    public ResultInfo<?> deleteChat(Integer userId, Integer friendId) {
        chatDao.deleteChat(userId, friendId);
        return ResultInfo.success(CodeEnum.SUCCESS);
    }

    /**
     * 获取所有聊天框
     * @param userId
     * @return
     */
    @Override
    public ResultInfo<List<BoxVo>> getAllChatBox(Integer userId) {
        List<BoxVo> boxVoList = new LinkedList<>();
        //获取私聊
        List<User> allFriend = friendDao.findAllFriend(userId);
        for(User user : allFriend){
            BoxVo boxVo = new BoxVo();
            UserVo userVo = new UserVo();
            userVo.setUser(user);
            //查询用户在线状态
            userVo.setStatus(ChatSocket.getUserStatus(user.getUserId()));
            boxVo.setUserVo(userVo);
            boxVo.setType(false);
            boxVoList.add(boxVo);
        }
        //获取群聊
        List<Group> allGroup = userDao.getAllGroup(userId);
        for (Group group : allGroup){
            BoxVo boxVo = new BoxVo();
            boxVo.setGroup(group);
            boxVo.setType(true);
            boxVoList.add(boxVo);
        }
        return ResultInfo.success(CodeEnum.SUCCESS, boxVoList);
    }

    /**
     * 获取所有群聊
     * @param userId
     * @return
     */
    @Override
    public ResultInfo<List<Group>> getAllGroup(Integer userId) {
        List<Group> allGroup = chatDao.getAllGroup(userId);
        return ResultInfo.success(CodeEnum.SUCCESS, allGroup);
    }

    /**
     * 新建群聊
     * @param userId
     * @param groupName
     * @return
     */
    @Override
    public ResultInfo<?> addGroup(Integer userId, @NotNull String groupName) {
/*        if(groupName == null || groupName.equals("") || groupName.length() == 0){
            return ResultInfo.error(CodeEnum.NULL_PARAM);
        }*/
        //群聊不能同名
        if(chatDao.findGroupByName(groupName) != null){
            return ResultInfo.error(CodeEnum.BAD_REQUEST);
        }
        chatDao.insertGroup(userId, groupName);
        return ResultInfo.success(CodeEnum.SUCCESS);
    }

    /**
     * 退出群聊
     * @param userId
     * @param groupId
     * @return
     */
    @Override
    public ResultInfo<?> outGroup(Integer userId, Integer groupId) {
        chatDao.outGroup(userId, groupId);
        return ResultInfo.success(CodeEnum.SUCCESS);
    }

    /**
     * 加入群聊
     * @param userId
     * @param groupName
     * @return
     */
    @Override
    public ResultInfo<?> intoGroup(Integer userId, @NotNull String groupName) {
        Group group = chatDao.findGroupByName(groupName);
        if(group == null){
            return ResultInfo.error(CodeEnum.BAD_REQUEST);
        }
        chatDao.intoGroup(userId, group.getGroupId());
        return ResultInfo.success(CodeEnum.SUCCESS);
    }
}
