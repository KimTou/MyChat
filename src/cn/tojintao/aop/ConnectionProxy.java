package cn.tojintao.aop;

import cn.tojintao.common.ConnectionPool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @author cjt
 * @date 2021/7/3 0:24
 * @description: 数据库连接代理对象
 */
public class ConnectionProxy implements InvocationHandler {

    /**
     * 数据库连接对象（被代理）
     */
    private Connection target;
    /**
     * 数据库连接池
     */
    private ConnectionPool pool;

    public ConnectionProxy(ConnectionPool pool){
        this.pool = pool;
    }

    /**
     * 生产代理对象
     * @param target
     * @return
     */
    public Connection getProxy(Connection target){
        this.target = target;
        return (Connection) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if("close".equals(method.getName())){
            //释放数据库连接，将连接放回连接池
            pool.releaseConnection(target);
            return null;
        }else{
            return method.invoke(target, args);
        }
    }

}
