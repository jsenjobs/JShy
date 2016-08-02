package com.chaos.jim.db.template.annotations;

/**
 * Created by jsen on 2016/8/1.
 * sql操作类型
 */
public enum Actions {
    // 返回单个数据表模型
    SingleModel,
    // 返回一个数据列表
    ModelList,
    // 返回操作影响数
    EffectValue
}
