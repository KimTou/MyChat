package cn.tojintao.dao;

import cn.tojintao.model.entity.User;

import java.util.List;

/**
 * @author cjt
 * @date 2021/6/21 0:15
 */
public interface FriendDao {

    List<User> findAllFriend(Integer userId);

    void beFriend(Integer userId, Integer addId);

    void insertRequest(Integer userId, Integer addId);

    List<Integer> getRequest(Integer userId);

    boolean requestIsExist(Integer userId, Integer addId);

    boolean isFriend(Integer userId, Integer addId);

    void deleteRequest(Integer userId, Integer addId);

    void deleteFriend(Integer userId, Integer friendId);

}
