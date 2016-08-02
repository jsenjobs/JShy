package com.chaos.jim.db.template.dbpool;

/**
 * Created by jsen on 2016/8/2.
 */
public class PoolProperties {
    // 连接驱动
    private String DRIVER_CLASS =null;

    // 连接地址
    private String URL = null;

    // 数据库用户名
    private String USER_NAME =null;

    // 数据库口令
    private String PASSWORD =null;

    // 初始化连接
    private int initSize;

    // 最大连接数
    private int maxActive;

    // 最小空闲连接
    private int minIdle;

    // 最大空闲连接
    private int maxIdle;

    // 最大等待时间
    private int maxWait;

    // 等待超时是否自动回收超时连接
    private boolean removeAbandoned;

    // 超时时间
    private int remeoveAbandonedTimeout;

    // 是否事物 提交
    private boolean defaultAutocommit;

    // 对于数据库是否只能读取
    private boolean defaultReadonly;

    public String getDRIVER_CLASS() {
        return DRIVER_CLASS;
    }

    public void setDRIVER_CLASS(String DRIVER_CLASS) {
        this.DRIVER_CLASS = DRIVER_CLASS;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public int getInitSize() {
        return initSize;
    }

    public void setInitSize(int initSize) {
        this.initSize = initSize;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public boolean isRemoveAbandoned() {
        return removeAbandoned;
    }

    public void setRemoveAbandoned(boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    public int getRemeoveAbandonedTimeout() {
        return remeoveAbandonedTimeout;
    }

    public void setRemeoveAbandonedTimeout(int remeoveAbandonedTimeout) {
        this.remeoveAbandonedTimeout = remeoveAbandonedTimeout;
    }

    public boolean isDefaultAutocommit() {
        return defaultAutocommit;
    }

    public void setDefaultAutocommit(boolean defaultAutocommit) {
        this.defaultAutocommit = defaultAutocommit;
    }

    public boolean isDefaultReadonly() {
        return defaultReadonly;
    }

    public void setDefaultReadonly(boolean defaultReadonly) {
        this.defaultReadonly = defaultReadonly;
    }
}
