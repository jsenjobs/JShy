package com.chaos.jim.db.template;

import com.chaos.jim.db.template.annotations.AutoBody;
import com.chaos.jim.db.template.annotations.AutoParameter;
import com.chaos.jim.db.template.dbpool.DBPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;

/**
 * Created by jsen on 2016/8/1.
 * 注入数据库操作
 * 注入事务处理
 */
public class Configer {
    private static int length;
    private static final Logger logger = LogManager.getLogger(Configer.class);

    /**
     * 注入sql操作
     * @param packageRoot 包含要注入对象的包路径，建议使用含有注入对象的类的包,
     *                    要注入的类必须有AutoBody注解,
     *                    被注解的参数要有AutoParameter注解，且为static类型
     * @param loop 是否递归遍历
     */
    public static void register(String packageRoot, boolean loop) {
        URL root = Configer.class.getClassLoader().getResource("");
        if (root!=null) {
            String path = root.getPath();
            if (!path.endsWith(File.separator)) {
                path+=File.separator;
            }
            length = path.length();
            try {
                parserPath(new File(path+packageRoot.replaceAll(".", File.separator)), loop);
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
                logger.error("ClassNotFound when autoWried the dbm handler field.");
            }
        } else {
            logger.error("Init DB proxy error, url root is empty.");
        }
    }

    public static void register(String packageRoot) {
        register(packageRoot, true);
    }

    @Deprecated
    public static void register(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field:fields) {
            if (field.getAnnotation(AutoParameter.class)!=null) {
                try {
                    field.set(obj, DPQuery.autoWried(field.getType()));
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage());
                    logger.error("some error happened when autoWried the dbm handler field.");
                }
            }
        }
    }

    /**
     * 注册事务处理,建议在有事务注解时才注册事务
     * @param obj 要注册事务的实体类，必须继承于接口,必须标注AutoBody注解
     * @param <T> 接口类型，事务注解在接口中的方法进行标注
     * @return 注入后的对象
     */
    public static <T> T registerTransactional(T obj) {
        if (obj.getClass().getAnnotation(AutoBody.class)!=null) {
            return DPTransactional.autoWried(obj,  obj.getClass().getInterfaces());
        }
        return obj;
    }

    @Deprecated
    public static <T> T registerField(Class<T> clazz) {
        return DPQuery.autoWried(clazz);
    }




    public static void setDBPool(DBPool dbPool) {
        PoolManager.setPool(dbPool);
    }



    private static void parserPath(File dir, boolean loop) throws ClassNotFoundException {
        File[] files = dir.listFiles();
        for (File file:files) {
            if (file.isFile()) {
                if (file.getName().endsWith(".class")) {
                    String abPath = file.getAbsolutePath();
                    abPath = abPath.substring(length, abPath.length()-6);
                    abPath = abPath.replaceAll(File.separator, ".");
                    Class cla = Class.forName(abPath);
                    if (cla.getAnnotation(AutoBody.class)!=null) {
                        Field[] fields = cla.getDeclaredFields();
                        for (Field field:fields) {
                            if (field.getAnnotation(AutoParameter.class)!=null) {
                                try {
                                    field.set(null, DPQuery.autoWried(field.getType()));
                                } catch (IllegalAccessException e) {
                                    logger.error(e.getMessage());
                                    logger.error("some error happened when autoWried the dbm handler.");
                                }
                            }
                        }
                    }
                }
            } else if (loop && file.isDirectory()) {
                parserPath(file, loop);
            }
        }
    }
}
