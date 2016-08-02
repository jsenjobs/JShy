package com.chaos.jim.db.template;

import com.chaos.jim.db.template.dbpool.DBPool;
import com.chaos.jim.db.template.dbpool.DBPoolDefault;

/**
 * Created by jsen on 2016/8/2.
 */
class PoolManager {
    private static DBPool pool = null;
    static DBPool getPool() {
        if (pool == null) {
            pool = new DBPoolDefault();
        }
        return pool;
    }

    static void setPool(DBPool pool) {
        if (pool == null){
            PoolManager.pool = pool;
        }
    }
}
