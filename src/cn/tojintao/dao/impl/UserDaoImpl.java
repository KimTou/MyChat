package cn.tojintao.dao.impl;

import cn.tojintao.dao.UserDao;
import cn.tojintao.model.entity.Group;
import cn.tojintao.model.entity.User;
import cn.tojintao.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author cjt
 * @date 2021/6/19 16:48
 */
public class UserDaoImpl implements UserDao {

    /**
     * 头像路径前缀
     */
    private static final String imgPath = "http://localhost:8080/MyChat/img/";

    /**
     * 连接数据库
     */
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;

    /**
     * 登录
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        try{
            con= DbUtil.getConnection();
            //寻找是否有与输入的用户名密码一致的用户
            String sql="select * from user where user_name=? and password=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            rs = stmt.executeQuery();
            //若返回结果集不为空，证明输入正确
            if(rs.next()) {
                //返回用户id
                user.setUserId(rs.getInt("user_id"));
                user.setAvatar(imgPath + rs.getString("avatar"));
                return user;
            }
            else {
                return null;
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            try{
                DbUtil.close(rs,stmt, con);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public User getUserById(Integer userId) {
        try{
            con= DbUtil.getConnection();
            String sql="select * from user where user_id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            //若返回结果集不为空，封装结果
            if (rs.next()) {
                //返回用户id
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setAvatar(imgPath + rs.getString("avatar"));
                return user;
            }
            else {
                return null;
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            try{
                DbUtil.close(rs,stmt, con);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public User getUserByName(String userName) {
        try{
            con= DbUtil.getConnection();
            String sql="select * from user where user_name = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            //若返回结果集不为空，证明输入正确
            if(rs.next()) {
                //返回用户id
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setAvatar(imgPath + rs.getString("avatar"));
                return user;
            }
            else {
                return null;
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            try{
                DbUtil.close(rs,stmt, con);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取所有群聊
     * @param userId
     * @return
     */
    @Override
    public List<Group> getAllGroup(Integer userId) {
        try{
            con= DbUtil.getConnection();
            String sql="select * from `group` where group_id in (select group_id from group_user where user_id=?)";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            List<Group> groupList = new LinkedList<>();
            while(rs.next()) {
                Group group = new Group();
                group.setGroupId(rs.getInt("group_id"));
                group.setGroupName(rs.getString("group_name"));
                groupList.add(group);
            }
            return groupList;
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            try{
                DbUtil.close(rs,stmt, con);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取群聊中的所有用户id
     * @param groupId
     * @return
     */
    @Override
    public List<Integer> getGroupUser(Integer groupId) {
        try{
            con= DbUtil.getConnection();
            String sql="select user_id from `group_user` where group_id=?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, groupId);
            rs = stmt.executeQuery();
            List<Integer> userIdList = new LinkedList<>();
            while(rs.next()) {
                Integer userId = rs.getInt("user_id");
                userIdList.add(userId);
            }
            return userIdList;
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            try{
                DbUtil.close(rs,stmt, con);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }


}
