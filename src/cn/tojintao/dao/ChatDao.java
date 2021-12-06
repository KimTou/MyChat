package cn.tojintao.dao;

import cn.tojintao.model.entity.Group;
import cn.tojintao.model.entity.GroupMessage;
import cn.tojintao.model.entity.Message;
import cn.tojintao.model.vo.MessageVo;

import java.util.List;

/**
 * @author cjt
 * @date 2021/6/20 20:09
 */
public interface ChatDao {

    List<MessageVo> getChatById(Integer userId, Integer friendId);

    List<GroupMessage> getGroupChatById(Integer groupId);

    void saveMessage(Message message);

    void deleteChat(Integer userId, Integer friendId);

    void saveGroupMessage(GroupMessage groupMessage);

    List<Group> getAllGroup(Integer userId);

    void insertGroup(Integer userId, String groupName);

    void outGroup(Integer userId, Integer groupId);

    void intoGroup(Integer userId, Integer groupId);

    Group findGroupByName(String groupName);

}
