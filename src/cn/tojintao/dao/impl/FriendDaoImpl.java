package cn.tojintao.dao.impl;

import cn.tojintao.dao.FriendDao;
import cn.tojintao.dao.UserDao;
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
 * @date 2021/6/21 0:30
 */
public class FriendDaoImpl implements FriendDao {
    /**
     * 连接数据库
     */
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;

    UserDao userDao = new UserDaoImpl();

    @Override
    public List<User> findAllFriend(Integer userId) {
        try{
            con= DbUtil.getConnection();
            //查询两人之间的聊天记录
            String sql="select friend_id as id from friend where user_id=? union all " +
                    "select user_id as id from friend where friend_id=?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            rs = stmt.executeQuery();
            List<User> userList = new LinkedList<>();
            while(rs.next()) {
                Integer id = rs.getInt(1);
                User user = userDao.getUserById(id);
                userList.add(user);
            }
            return userList;
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
     * 成为好友
     * @param userId
     * @param addId
     */
    @Override
    public void beFriend(Integer userId, Integer addId) {
        try{
            con= DbUtil.getConnection();
            String sql="insert into friend(user_id,friend_id) values (?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, addId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            try{
                DbUtil.close(rs,stmt, con);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 新增添加好友请求
     * @param userId
     * @param addId
     */
    @Override
    public void insertRequest(Integer userId, Integer addId) {
        try{
            con= DbUtil.getConnection();
            String sql="insert into request(user_id,add_id) values (?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, addId);
            stmt.executeUpdate();
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            try{
                DbUtil.close(rs,stmt, con);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取好友请求
     * @param userId
     * @return
     */
    @Override
    public List<Integer> getRequest(Integer userId) {
        try{
            con= DbUtil.getConnection();
            //查询两人之间的聊天记录
            String sql="select user_id from request where add_id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            List<Integer> userList = new LinkedList<>();
            while(rs.next()) {
                Integer id = rs.getInt(1);
                userList.add(id);
            }
            return userList;
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
     * 判断请求是否存在
     * @param userId
     * @param addId
     * @return
     */
    @Override
    public boolean requestIsExist(Integer userId, Integer addId) {
        try{
            con= DbUtil.getConnection();
            //查询两人之间的聊天记录
            String sql="select user_id from request where user_id = ? and add_id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, addId);
            rs = stmt.executeQuery();
            if(rs.next()) {
                return true;
            }else{
                return false;
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
        return false;
    }

    /**
     * 判断是否是好友
     * @param userId
     * @param addId
     * @return
     */
    @Override
    public boolean isFriend(Integer userId, Integer addId) {
        try{
            con= DbUtil.getConnection();
            String sql="select user_id from friend where user_id = ? and friend_id = ? or user_id=? and friend_id=?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, addId);
            stmt.setInt(3, addId);
            stmt.setInt(4, userId);
            rs = stmt.executeQuery();
            if(rs.next()) {
                return true;
            }else{
                return false;
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
        return false;
    }

    /**
     * 清除好友请求
     * @param userId
     * @param addId
     */
    @Override
    public void deleteRequest(Integer userId, Integer addId) {
        try{
            con= DbUtil.getConnection();
            String sql="delete from request where user_id=? and add_id=? or user_id=? and add_id=?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, addId);
            stmt.setInt(3, addId);
            stmt.setInt(4, userId);
            stmt.executeUpdate();
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            try{
                DbUtil.close(rs,stmt, con);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除好友
     * @param userId
     * @param friendId
     */
    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        try{
            con= DbUtil.getConnection();
            String sql="delete from friend where user_id=? and friend_id=? or user_id=? and friend_id=?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.setInt(3, friendId);
            stmt.setInt(4, userId);
            stmt.executeUpdate();
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            try{
                DbUtil.close(rs,stmt, con);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}
