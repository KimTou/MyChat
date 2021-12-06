package cn.tojintao.util;

import cn.tojintao.common.ConnectionPool;

import java.sql.*;

/**
 * @author cjt
 * @date 2021/6/19 11:14
 */
public class DbUtil {

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    /**
     * 获取数据库连接
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        /*Class.forName(jdbcName);
        Connection con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
        return con;*/
        return connectionPool.getConnection();
    }

    /**
     * 关闭连接
     * @param con
     * @throws Exception
     */
    public static void close(ResultSet rs, PreparedStatement st, Connection con) throws SQLException {
        if (rs != null) {
            rs.close();
            if (st != null) {
                st.close();
                if (con != null) {
                    //close释放连接，放回连接池
                    con.close();
                }
            }
        }
    }
}
