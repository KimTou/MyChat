package cn.tojintao.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author cjt
 * @date 2021/7/2 23:07
 */
public class LoggerUtil {

    /**
     * 饿汉模式
     */
    private static LoggerUtil logger = new LoggerUtil();

    public static LoggerUtil getLogger(){
        return logger;
    }

    /**
     * 日志记录
     * @param method
     */
    public void info(Object target, Method method, Object[] args){
        System.out.println("======================日志记录============================");
        System.out.println("当前时刻:" + DateUtil.getTimeStr());
        System.out.println("请求方法类名:" + target.getClass().getName());
        System.out.println("方法名:" + method.getName());
        System.out.println("参数列表:");
        //获取方法参数对象
        Parameter[] parameters = method.getParameters();
        //获取方法参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < args.length; i++) {
            System.out.print("参数名:" + parameters[i].getName());
            System.out.print("\t参数类型:" + parameterTypes[i]);
            System.out.print("\t参数值:" + args[i]);
            System.out.println();
        }
        System.out.println();
    }

    /**
     * 日志记录
     * @param msg
     */
    public void info(String msg){
        System.out.println();
        System.out.println(DateUtil.getTimeStr() + " ------ 日志[info]：" + msg);
        System.out.println();
    }

    /**
     * 日志记录
     * @param msg
     */
    public void danger(String msg){
        System.out.println();
        System.out.println(DateUtil.getDate() + " ****** 日志[danger]警告：" + msg);
        System.out.println();
    }

}
