package cn.tojintao.dao.impl;

import cn.tojintao.dao.ChatDao;
import cn.tojintao.dao.UserDao;
import cn.tojintao.model.entity.Group;
import cn.tojintao.model.entity.GroupMessage;
import cn.tojintao.model.entity.Message;
import cn.tojintao.model.entity.User;
import cn.tojintao.model.vo.MessageVo;
import cn.tojintao.util.DbUtil;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author cjt
 * @date 2021/6/20 20:09
 */
public class ChatDaoImpl implements ChatDao {

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

    UserDao userDao = new UserDaoImpl();

    /**
     * 获取私信
     * @param userId
     * @param friendId
     * @return
     */
    @Override
    public List<MessageVo> getChatById(Integer userId, Integer friendId) {
        try{
            //获取两者的详细信息
            User user = userDao.getUserById(userId);
            User friend = userDao.getUserById(friendId);
            con = DbUtil.getConnection();
            //查询两人之间的聊天记录
            String sql="select * from message where sender=? and receiver=? or sender=? and receiver=? order by gmt_create asc";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.setInt(3, friendId);
            stmt.setInt(4, userId);
            rs = stmt.executeQuery();
            List<MessageVo> messageList = new LinkedList<>();
            while(rs.next()) {
                MessageVo message = new MessageVo();
                message.setId(rs.getInt("id"));
                message.setSender(rs.getInt("sender"));
                message.setReceiver(rs.getInt("receiver"));
                message.setContent(rs.getString("content"));
                message.setGmtCreate(rs.getTimestamp("gmt_create"));
                if(message.getSender().equals(userId)){
                    message.setSenderName(user.getUserName());
                    message.setSenderAvatar(user.getAvatar());
                }else{
                    message.setSenderName(friend.getUserName());
                    message.setSenderAvatar(friend.getAvatar());
                }
                messageList.add(message);
            }
            return messageList;
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
     * 群聊天记录
     * @param groupId
     * @return
     */
    @Override
    public List<GroupMessage> getGroupChatById(Integer groupId) {
        try{
            con= DbUtil.getConnection();
            //查询群聊记录
            String sql="select g.id, g.group_id, g.sender, g.content, g.gmt_create, u.user_name, u.avatar " +
                    "from group_message as g left join `user` as u on sender = user_id where group_id = ? order by gmt_create asc";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, groupId);
            rs = stmt.executeQuery();
            List<GroupMessage> messageList = new LinkedList<>();
            while(rs.next()) {
                GroupMessage message = new GroupMessage();
                message.setId(rs.getInt("id"));
                message.setGroupId(groupId);
                message.setSender(rs.getInt("sender"));
                message.setSenderName(rs.getString("user_name"));
                message.setSenderAvatar(imgPath + rs.getString("avatar"));
                message.setContent(rs.getString("content"));
                message.setGmtCreate(rs.getTimestamp("gmt_create"));
                messageList.add(message);
            }
            return messageList;
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
     * 保存私聊信息
     * @param message
     */
    @Override
    public void saveMessage(Message message) {
        try{
            con= DbUtil.getConnection();
            String sql="insert into message(sender,receiver,content,gmt_create) values (?,?,?,?)";
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, message.getSender());
            stmt.setInt(2, message.getReceiver());
            stmt.setString(3, message.getContent());
            stmt.setTimestamp(4, new Timestamp(message.getGmtCreate().getTime()));
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            List<Message> messageList = new LinkedList<>();
            if(rs.next()) {
                //获得主键
                message.setId(rs.getInt(1));
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
    }

    /**
     * 删除私信聊天记录
     * @param userId
     * @param friendId
     */
    @Override
    public void deleteChat(Integer userId, Integer friendId) {
        try{
            con= DbUtil.getConnection();
            String sql="delete from message where sender=? and receiver=? or sender=? and receiver=?";
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

    /**
     * 存储群聊记录
     * @param groupMessage
     */
    @Override
    public void saveGroupMessage(GroupMessage groupMessage) {
        try{
            con= DbUtil.getConnection();
            //查询两人之间的聊天记录
            String sql="insert into group_message(group_id,sender,content,gmt_create) values (?,?,?,?)";
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, groupMessage.getGroupId());
            stmt.setInt(2, groupMessage.getSender());
            stmt.setString(3, groupMessage.getContent());
            stmt.setTimestamp(4, new Timestamp(groupMessage.getGmtCreate().getTime()));
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                //获得主键
                groupMessage.setId(rs.getInt(1));
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
            String sql="select gu.group_id as group_id, g.group_name as group_name from group_user as gu " +
                    "left join `group` as g on gu.group_id = g.group_id where gu.user_id =?";
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
     * 新建群聊
     * @param userId
     * @param groupName
     */
    @Override
    public void insertGroup(Integer userId, String groupName) {
        try{
            con= DbUtil.getConnection();
            String sql="insert into `group`(group_name) values (?)";
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, groupName);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                //获得主键
                int groupId = rs.getInt(1);
                String add ="insert into `group_user`(group_id, user_id) values (?,?)";
                stmt = con.prepareStatement(add);
                stmt.setInt(1, groupId);
                stmt.setInt(2, userId);
                stmt.executeUpdate();
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
    }

    /**
     * 退出群聊
     * @param userId
     * @param groupId
     */
    @Override
    public void outGroup(Integer userId, Integer groupId) {
        try{
            con= DbUtil.getConnection();
            String sql="delete from group_user where group_id=? and user_id=?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, groupId);
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
     * 加入群聊
     * @param userId
     * @param groupId
     */
    @Override
    public void intoGroup(Integer userId, Integer groupId) {
        try{
            con= DbUtil.getConnection();
            String sql="insert into `group_user`(group_id, user_id) values (?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, groupId);
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
     * 查找群聊
     * @param groupName
     * @return
     */
    @Override
    public Group findGroupByName(String groupName) {
        try{
            con= DbUtil.getConnection();
            String sql="select * from `group` where group_name=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, groupName);
            rs = stmt.executeQuery();
            if(rs.next()) {
                Group group = new Group();
                group.setGroupId(rs.getInt("group_id"));
                group.setGroupName(rs.getString("group_name"));
                return group;
            }else{
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

}
