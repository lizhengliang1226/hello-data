package com.lzl.datagenerator.utils;

import cn.hutool.db.Db;
import cn.hutool.db.ds.DSFactory;

import javax.sql.DataSource;

/**
 * 数据源工具类
 *
 * @author LZL
 * @version 1.0
 * @since 2023/8/31
 */
public class DbUtils {
    private static DSFactory globalDsFactory;

    public static void setGlobalDsFactory(DSFactory dsFactory) {
        globalDsFactory = dsFactory;
    }

    public static void destroy() {
        globalDsFactory.destroy();
    }

    public static DataSource getDataSource(String datasourceId) {
        return globalDsFactory.getDataSource(datasourceId);
    }

    public static Db use(String datasourceId) {
        return Db.use(getDataSource(datasourceId));
    }
}
