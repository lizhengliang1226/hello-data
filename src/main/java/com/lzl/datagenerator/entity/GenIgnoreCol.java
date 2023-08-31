package com.lzl.datagenerator.entity;

import java.io.Serial;
import java.io.Serializable;

/**
 * 忽略的列(GenIgnoreCol)实体类
 *
 * @author makejava
 * @since 2023-08-31 10:56:42
 */
public class GenIgnoreCol implements Serializable {
    @Serial
    private static final long serialVersionUID = -97644639823017813L;
    /**
     * 数据源ID
     */
    private String datasourceId;
    /**
     * 列名
     */
    private String columnName;


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

}