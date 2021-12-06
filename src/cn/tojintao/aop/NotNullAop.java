package cn.tojintao.aop;

import cn.tojintao.aop.annotation.NotNull;
import cn.tojintao.common.CodeEnum;
import cn.tojintao.model.dto.ResultInfo;
import cn.tojintao.util.LoggerUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;

/**
 * @author cjt
 * @date 2021/7/3 10:37
 * @description: 字符串判空切面
 */
public class NotNullAop implements InvocationHandler {

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
        //获取方法参数所有注解
        Annotation[][] annotations = method.getParameterAnnotations();
        if(annotations.length == 0){
            return method.invoke(target, args);
        }
        //获取方法参数对象
        Parameter[] parameters = method.getParameters();
        //获取方法参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                //参数非空校验
                if(annotations[i][j] != null && annotations[i][j] instanceof NotNull && ((NotNull)annotations[i][j]).notNull()){
                    boolean check = checkString(parameters[i].getName(), parameterTypes[i].getName(), args[i]);
                    if(!check){
                        return ResultInfo.error(CodeEnum.NULL_PARAM);
                    }
                    break;
                }
            }
        }
        return method.invoke(target, args);
    }

    /**
     * 参数非空校验
     * @param paramName
     * @param paramType
     * @param value
     */
    public boolean checkString(String paramName, String paramType, Object value){
        //将value转换为String并使用trim去掉空格
        if(value == null || "".equals(value.toString().trim())){
            logger.danger("参数名:" + paramName + "字符串值为空");
            return false;
        }
        return true;
    }

}
