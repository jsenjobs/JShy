package com.chaos.jim.db.template.annotations;

import java.lang.annotation.*;

/**
 * Created by jsen on 2016/8/2.
 * 标记事务处理，被标记的方法作为事务处理，使用该注解要注册
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Transactional {
}
