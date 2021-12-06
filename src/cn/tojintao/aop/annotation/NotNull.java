package cn.tojintao.aop.annotation;

import java.lang.annotation.*;

/**
 * @author cjt
 * @date 2021/7/3 10:35
 * @description: 自定义字符串判空注解
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {

    boolean notNull() default true;

}
