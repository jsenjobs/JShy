package com.chaos.jim.db.template.annotations;

import java.lang.annotation.*;

/**
 * Created by jsen on 2016/8/1.
 * 定义一个数据表,也可以只是一个视图表，
 * 只要表属性注解正确且用到的地方sql操作正确
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Table {
    //表格名称
    String table();
    //表的类型 单表，组合表  single  combine
    String type() default "single";
}
