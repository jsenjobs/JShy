package com.chaos.jim.db.template;

import com.chaos.jim.db.template.annotations.Transactional;
import com.chaos.jim.db.template.dbpool.DBPool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by jsen on 2016/8/2.
 * 数据库事务处理相关的动态注入
 */
class DPTransactional implements InvocationHandler {
    static DBPool dbcpProxy = PoolManager.getPool();

    private Object proxied;

    private DPTransactional(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if (method.getAnnotation(Transactional.class) != null) {
            dbcpProxy.startTransactional();
            try {
                result = method.invoke(proxied, args);
                dbcpProxy.commitTransaction();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                dbcpProxy.rollbackTransaction();
                return result;
            }
        }
        result = method.invoke(proxied, args);
        dbcpProxy.release();
        return result;
    }

    @SuppressWarnings("unchecked")
    static <T> T autoWried(T obj, Class<?>[] clazzes) {
        return  (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                clazzes, new DPTransactional(obj));
    }
}
