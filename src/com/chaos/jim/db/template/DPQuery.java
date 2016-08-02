package com.chaos.jim.db.template;

import com.chaos.jim.db.template.annotations.Column;
import com.chaos.jim.db.template.annotations.Sql;
import com.chaos.jim.db.template.dbpool.DBPool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jsen on 2016/8/1.
 * sql执行的动态注入
 */
class DPQuery implements InvocationHandler {
    static DBPool dbcpProxy = PoolManager.getPool();
    private DPQuery() {
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Sql sql = method.getAnnotation(Sql.class);
        final Exception[] exceptions = new Exception[1];
        switch (sql.action()) {
            case SingleModel:
                final Object[] result = new Object[1];
                dbcpProxy.executeQuery(sql.sql(), args, resultSet -> {
                    try {
                        if (resultSet.next()) {
                            Class returnType = sql.returnType();
                            result[0] = returnType.newInstance();
                            Method[] setMethods = returnType.getMethods();
                            Column column;
                            for (Method setMethod:setMethods) {
                                column = setMethod.getAnnotation(Column.class);
                                if (column !=null) {
                                    Method dataMethod = resultSet.getClass().getMethod("get"+ column.type(), String.class);
                                    try {
                                        Object dv = dataMethod.invoke(resultSet, column.name());
                                        setMethod.invoke(result[0], dv);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        exceptions[0] = e;
                    }
                });
                if (exceptions[0]!=null) {
                    throw exceptions[0];
                }
                return result[0];
            case ModelList:
                List<Object> results = new ArrayList<>();
                dbcpProxy.executeQuery(sql.sql(), args, resultSet -> {
                    Object result1;
                    Class returnType = sql.returnType();
                    Method[] objMethods = returnType.getMethods();
                    Column column;
                    Map<Method, Column> setMethods = new HashMap<>();
                    for (Method objMethod:objMethods) {
                        column = objMethod.getAnnotation(Column.class);
                        if (column !=null) {
                            setMethods.put(objMethod, column);
                        }
                    }
                    try {
                        while (resultSet.next()) {
                            result1 = returnType.newInstance();
                            for (Map.Entry<Method, Column> entry:setMethods.entrySet()) {
                                column = entry.getValue();
                                Method dataMethod = resultSet.getClass().getMethod("get"+ column.type(), String.class);
                                try {
                                    Object dv = dataMethod.invoke(resultSet, column.name());
                                    entry.getKey().invoke(result1, dv);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            results.add(result1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        exceptions[0] = e;
                    }
                });
                if (exceptions[0]!=null) {
                    throw exceptions[0];
                }
                return results;
            case EffectValue:
                return dbcpProxy.update(sql.sql(), args);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    static <T> T autoWried(Class<T> mapperInterface) {
        ClassLoader classLoader = mapperInterface.getClassLoader();
        Class<?>[] interfaces = new Class[]{mapperInterface};
        DPQuery proxy = new DPQuery();
        try {
            return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }
}
