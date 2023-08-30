package com.lzl.datagenerator.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
@Data
public class ColumnConfig implements Serializable {
    private String columnName;
    private String strategyCode;
    private String defaultValue;
    private Number baseValue;
    private List<Object> randomEle;
    private String querySql;
    private String queryCol;
    private String dictColName;
    private String dataSourceId;
    private String prefix;
    private String suffix;
    private long step;
}