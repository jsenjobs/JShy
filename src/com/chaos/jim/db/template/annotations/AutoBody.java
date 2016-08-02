package com.chaos.jim.db.template.annotations;

import java.lang.annotation.*;

/**
 * Created by jsen on 2016/8/1.
 * 自动注入类标记
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface AutoBody {
}
