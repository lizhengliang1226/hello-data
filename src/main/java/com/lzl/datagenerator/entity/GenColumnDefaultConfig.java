package com.lzl.datagenerator.entity;

import java.io.Serializable;

/**
 * 列配置表,不依赖于某个表的全局列配置(GenColumnDefaultConfig)实体类
 *
 * @author makejava
 * @since 2023-08-30 23:25:20
 */
public class GenColumnDefaultConfig implements Serializable {
    private static final long serialVersionUID = -75834189477926156L;
    /**
     * 数据源ID
     */
    private String datasourceId;
    /**
     * 列名
     */
    private String columnName;
    /**
     * 策略模板ID，当为@时无意义
     */
    private String strategyTmplId;
    /**
     * 默认值
     */
    private String defaultVal;


    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getStrategyTmplId() {
        return strategyTmplId;
    }

    public void setStrategyTmplId(String strategyTmplId) {
        this.strategyTmplId = strategyTmplId;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

}

