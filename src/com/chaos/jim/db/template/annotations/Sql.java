package com.chaos.jim.db.template.annotations;

import java.lang.annotation.*;

/**
 * Created by jsen on 2016/8/1.
 * 一个sql执行语句
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Sql {
    //sql语句
    String sql();
    Actions action();
    //返回类型
    Class returnType();
}
