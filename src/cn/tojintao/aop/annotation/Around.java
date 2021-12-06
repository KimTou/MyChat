package cn.tojintao.aop.annotation;

import java.lang.annotation.*;

/**
 * @author cjt
 * @date 2021/6/19 11:51
 * @description: 环绕通知
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Around {

    Class<? extends Annotation> targetClass();

}
