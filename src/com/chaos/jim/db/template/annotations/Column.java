package com.chaos.jim.db.template.annotations;

import java.lang.annotation.*;

/**
 * Created by jsen on 2016/8/1.
 * 用于set方法表示数据库的一个table属性
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Column {
    //属性名 the true name in the database
    String name();
    //属性类型 the type value will be called in ResultSet.get$value$() method.
    String type();
}
