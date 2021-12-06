package cn.tojintao.dao;

import cn.tojintao.model.entity.Group;
import cn.tojintao.model.entity.User;

import java.util.List;

/**
 * @author cjt
 * @date 2021/6/19 16:48
 */
public interface UserDao {

    User login(User user);

    User getUserById(Integer userId);

    User getUserByName(String userName);

    List<Group> getAllGroup(Integer userId);

    List<Integer> getGroupUser(Integer groupId);

}
