package com.lzl.datagenerator.entity;

import java.io.Serializable;

/**
 * 列配置表(GenColumnConfig)实体类
 *
 * @author makejava
 * @since 2023-08-30 23:25:15
 */
public class GenColumnConfig implements Serializable {
    private static final long serialVersionUID = -57165160056002991L;
    /**
     * 数据源ID
     */
    private String datasourceId;
    /**
     * 生成表代码
     */
    private String tableCode;
    /**
     * 列名
     */
    private String columnName;
    /**
     * 策略模板ID，当为@时无意义
     */
    private String strategyTmplId;


    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
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

}

