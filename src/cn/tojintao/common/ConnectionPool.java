package cn.tojintao.common;

import cn.tojintao.aop.ConnectionProxy;
import cn.tojintao.util.LoggerUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

/**
 * @author cjt
 * @date 2021/7/2 22:47
 * @description: 自定义数据库连接池
 */
public class ConnectionPool{

    LoggerUtil logger = LoggerUtil.getLogger();

    private static Properties prop = new Properties();

    static{
        //读取配置文件
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("pool.properties");
        try {
            prop.load(in);
            Class.forName(prop.getProperty("driver-name"));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据库驱动名称
     */
    private static String DRIVER_NAME = prop.getProperty("driver-name");
    /**
     * 数据库连接地址
     */
    private static String URL = prop.getProperty("url");
    /**
     * 数据库用户名
     */
    private static String USERNAME = prop.getProperty("username");
    /**
     * 数据库密码
     */
    private static String PASSWORD = prop.getProperty("password");
    /**
     * 最小连接数
     */
    private static Integer MIN_IDLE = Integer.valueOf(prop.getProperty("minIdle"));
    /**
     * 最大连接数
     */
    private static Integer MAX_ACTIVE = Integer.valueOf(prop.getProperty("maxActive"));
    /**
     * 当前创建连接数
     */
    private static int currentCon = 0;

    /**
     * 饿汉模式（线程安全）
     */
    private static ConnectionPool instance = new ConnectionPool();

    /**
     * 连接队列（static会为null）
     */
    private final LinkedList<Connection> connectionList = new LinkedList<Connection>();

    /**
     * 初始化连接池
     */
    public ConnectionPool() {
        for (int i = 0; i < MIN_IDLE; i++) {
            connectionList.add(this.createConnection());
        }
    }

    /**
     * 获取连接池对象
     * @return
     */
    public static ConnectionPool getInstance(){
        if(instance == null){
            return new ConnectionPool();
        }
        return instance;
    }

    /**
     * 获取数据库连接对象
     * @return
     */
    public Connection getConnection() {
        logger.info("当前数据库连接数：" + currentCon + "\t剩余空闲数据库连接数：" + connectionList.size());
        synchronized (connectionList){
            if(connectionList.size() > 0){
                Connection connection = connectionList.removeFirst();
                return new ConnectionProxy(this).getProxy(connection);
            }else if(currentCon < MAX_ACTIVE){
                return new ConnectionProxy(this).getProxy(createConnection());
            }else{
                logger.danger("数据库连接数已达上限");
                throw new RuntimeException("数据库连接数已达上限");
            }
        }
    }

    /**
     * 创建连接对象
     * @return
     * @throws SQLException
     */
    public Connection createConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.danger("创建数据库连接失败");
            throw new RuntimeException("创建数据库连接失败");
        }finally {
            currentCon++;
        }
    }

    /**
     * 释放连接（放回连接队列）
     * @param connection
     */
    public void releaseConnection(Connection connection){
        connectionList.add(connection);
    }

}
