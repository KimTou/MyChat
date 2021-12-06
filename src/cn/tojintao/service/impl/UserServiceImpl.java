package cn.tojintao.service.impl;

import cn.tojintao.aop.LoggerAop;
import cn.tojintao.common.CodeEnum;
import cn.tojintao.dao.UserDao;
import cn.tojintao.dao.impl.UserDaoImpl;
import cn.tojintao.model.entity.User;
import cn.tojintao.model.dto.ResultInfo;
import cn.tojintao.service.UserService;

/**
 * @author cjt
 * @date 2021/6/19 16:48
 */
public class UserServiceImpl implements UserService {

    /**
     * 增强 Dao（打印日志）
     */
    private final UserDao userDao = (UserDao) new LoggerAop().getProxy(new UserDaoImpl());

    /**
     * 登录
     * @param user
     * @return
     */
    @Override
    public ResultInfo<User> login(User user) {
        if(user.getUserName().length() == 0 || user.getPassword().length() == 0){
            return ResultInfo.error(400, "请填写完整信息");
        }else{
            if(userDao.login(user) == null){
                return ResultInfo.error(400, "用户名或密码错误");
            }
            return ResultInfo.success(CodeEnum.SUCCESS, user);
        }
    }

    @Override
    public ResultInfo<User> findUserById(Integer userId) {
        User user = userDao.getUserById(userId);
        return ResultInfo.success(CodeEnum.SUCCESS, user);
    }

}
