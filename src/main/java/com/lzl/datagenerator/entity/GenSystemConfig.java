package com.lzl.datagenerator.entity;

import java.io.Serializable;

/**
 * 系统配置表(GenSystemConfig)实体类
 *
 * @author makejava
 * @since 2023-08-29 18:56:19
 */
public class GenSystemConfig implements Serializable {
    private static final long serialVersionUID = -50615623394342689L;
    /**
     * 数据源ID
     */
    private String datasourceId;
    /**
     * 数据源URL
     */
    private String databaseUrl;
    /**
     * 数据源用户名
     */
    private String databaseUser;
    /**
     * 数据源密码
     */
    private String databasePassword;
    /**
     * 是否加载字典缓存 1-加载 0-不加载
     */
    private Integer loadDictCache;
    /**
     * 字典表名
     */
    private String dictTableName;
    /**
     * 字典表的字典代码列名
     */
    private String dictCodeColName;
    /**
     * 字典表的字典项列名
     */
    private String dictItemColName;


    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public Integer getLoadDictCache() {
        return loadDictCache;
    }

    public void setLoadDictCache(Integer loadDictCache) {
        this.loadDictCache = loadDictCache;
    }

    public String getDictTableName() {
        return dictTableName;
    }

    public void setDictTableName(String dictTableName) {
        this.dictTableName = dictTableName;
    }

    public String getDictCodeColName() {
        return dictCodeColName;
    }

    public void setDictCodeColName(String dictCodeColName) {
        this.dictCodeColName = dictCodeColName;
    }

    public String getDictItemColName() {
        return dictItemColName;
    }

    public void setDictItemColName(String dictItemColName) {
        this.dictItemColName = dictItemColName;
    }

}