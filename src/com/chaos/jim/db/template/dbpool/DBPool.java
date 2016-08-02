package com.chaos.jim.db.template.dbpool;

import com.chaos.jim.db.template.dbpool.DBPoolDefault;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jsen on 2016/8/2.
 */
public interface DBPool {
    void parserProperty();
    void DataSourceConfig();
    Connection getConn();
    void executeQuery(String sql,Object[] params, DBPoolDefault.QueryCall queryCall) throws Exception;
    void startTransactional();
    void commitTransaction();
    void rollbackTransaction();
    int update(String sql, Object[] obj) throws SQLException;
    void release(ResultSet rs, PreparedStatement pstmt);
    void release();
}
