package com.chaos.jim.db.template.dbpool;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by jsen on 2016/8/1.
 * dbcp数据库连接池
 */
public class DBPoolDefault implements DBPool {

    private ThreadLocal<Connection> tl=new ThreadLocal<>();
    private PoolProperties properties = new PoolProperties();

    private PreparedStatement ps =null;

    private ResultSet rs = null;

    // 创建数据库连接对象 ( 数据源 )
    private BasicDataSource dataSource = new BasicDataSource();


    /**
     * 读取配置文件，并设置数据库连接池的属性
     */
    public void parserProperty() {
        // 创建配置文件对象
        Properties props = new Properties();
        // 获得配置文件输入流对象
        InputStream input = DBPoolDefault.class.getResourceAsStream("/dbcp.properties");
        try {

            // 将文件读取到内存中
            props.load(input);
            // 根据键获得值
            properties.setDRIVER_CLASS(props.getProperty("driver"));
            properties.setURL(props.getProperty("url"));
            properties.setUSER_NAME(props.getProperty("userName"));
            properties.setPASSWORD(props.getProperty("password"));
            properties.setInitSize(Integer.parseInt(props.getProperty("initsize")));
            properties.setMaxActive(Integer.parseInt(props.getProperty("maxactive")));
            properties.setMinIdle(Integer.parseInt(props.getProperty("minidle")));
            properties.setMaxIdle(Integer.parseInt(props.getProperty("maxidle")));
            properties.setMaxWait(Integer.parseInt(props.getProperty("maxwait")));
            properties.setRemoveAbandoned(Boolean.parseBoolean(props.getProperty("removeabandoned")));
            properties.setRemeoveAbandonedTimeout(Integer.parseInt(props.getProperty("removeabandonedtimeout")));
            properties.setDefaultAutocommit(Boolean.parseBoolean(props.getProperty("defaultautocommit")));
            properties.setDefaultReadonly(Boolean.parseBoolean(props.getProperty("defaultreadonly")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(DBPoolDefault.class.getResource("").getPath());
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 设置 dataSource各属性值
     */
    public void DataSourceConfig() {
        // 设置数据库驱动
        dataSource.setDriverClassName(properties.getDRIVER_CLASS());
        // 设置 URL地址
        dataSource.setUrl(properties.getURL());
        // 设置用户名
        dataSource.setUsername(properties.getUSER_NAME());
        // 设置用户口令
        dataSource.setPassword(properties.getPASSWORD());
        // 初始化连接
        dataSource.setInitialSize(properties.getInitSize());
        // 设置最大连接数
        // dataSource.setMaxActive(maxActive);
        // 设置最小空闲连接
        dataSource.setMinIdle(properties.getMinIdle());
        // 设置最大空闲连接
        dataSource.setMaxIdle(properties.getMaxIdle());
        // 设置最大等待时间
        // dataSource.setMaxWait(maxWait);
        // 设置是否自动回收
        // dataSource.setRemoveAbandoned(removeAbandoned);
        // 设置超时时间
        dataSource.setRemoveAbandonedTimeout(properties.getRemeoveAbandonedTimeout());
        // 设置是否事物提交值
        dataSource.setDefaultAutoCommit(properties.isDefaultAutocommit());
        // 设置对于数据库是否只读
        dataSource.setDefaultReadOnly(properties.isDefaultReadonly());

    }

    public DBPoolDefault() {
        parserProperty();
        DataSourceConfig();
    }

    /**
     * 获得连接对象
     * @return
     */
    public Connection getConn() {
        try{
            //得到当前线程上绑定的连接
            Connection conn = tl.get();
            if(conn==null){  //代表线程上没有绑定连接
                conn = dataSource.getConnection();
                tl.set(conn);
            }
            return conn;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void executeQuery(String sql,Object[] params, QueryCall queryCall) throws Exception {
        if (queryCall!=null) {
            ps = getConn().prepareStatement(sql);
            if (params != null && params.length> 0) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            rs = ps.executeQuery();
            queryCall.query(rs);
            release(rs, ps);
        }
    }

    public void startTransactional() {
        try{
            //得到当前线程上绑定连接开启事务
            Connection conn = tl.get();
            if(conn==null){  //代表线程上没有绑定连接
                conn = dataSource.getConnection();
                tl.set(conn);
            }
            conn.setAutoCommit(false);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void commitTransaction(){
        Connection conn;
        try{
            conn = tl.get();
            if(conn!=null){
                conn.commit();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            tl.remove();
        }
    }

    public void rollbackTransaction() {
        Connection conn;
        try{
            conn = tl.get();
            if(conn!=null){
                conn.rollback();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            tl.remove();
        }
    }

    public interface QueryCall {
        void query(ResultSet resultSet);
    }

    // 更新数据的操作增,删,改要用到的封装方法
    public int update(String sql, Object[] obj) throws SQLException {
        // 准备语句的创建,带有sql命令的对象
        ps = getConn().prepareStatement(sql);
        for (int i = 1; i <= obj.length; i++) {
            ps.setObject(i, obj[i - 1]);
        }
        int i = ps.executeUpdate();
        release(null, ps);
        return i;
    }

    public void release(ResultSet rs, PreparedStatement pstmt) {
        // 释放结果集
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // 释放准备语句
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void release() {
        tl.remove();
    }

    /*
    public static void main(String[] args) throws Exception {
        DBCPTest test = new DBCPTest();
        String[] params = {"2"};
        ResultSet resultSet = test.executeQuery("select * from user where id=?", params);
        while(resultSet.next()){
            System.out.println(resultSet.getString("nickname"));
        }
    }
    */
}