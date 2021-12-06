package cn.tojintao.aop;

import cn.tojintao.util.LoggerUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author cjt
 * @date 2021/7/2 21:02
 * 日志记录切面
 */
public class LoggerAop implements InvocationHandler {

    LoggerUtil logger = LoggerUtil.getLogger();

    /**
     * 目标对象（被代理）
     */
    private Object target;

    /**
     * 生产代理对象
     * @param target
     * @return
     */
    public Object getProxy(Object target){
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info(target, method, args);
        return method.invoke(target, args);
    }
}
