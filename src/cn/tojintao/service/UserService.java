package cn.tojintao.service;

import cn.tojintao.model.entity.User;
import cn.tojintao.model.dto.ResultInfo;

/**
 * @author cjt
 * @date 2021/6/19 16:47
 */
public interface UserService {

    ResultInfo<User> login(User user);

    ResultInfo<User> findUserById(Integer userId);

}
