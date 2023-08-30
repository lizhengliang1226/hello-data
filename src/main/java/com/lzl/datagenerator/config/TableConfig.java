package com.lzl.datagenerator.config;

import cn.hutool.db.meta.Column;
import com.lzl.datagenerator.proxy.ColDataProvider;
import lombok.Data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/8/24-18:24
 */
@Data
public class TableConfig {
    private String datasourceId;
    private String tableName;
    private Integer genNum;
    private Set<String> pkInfo;
    private Collection<Column> columns;
    private Map<String, ColDataProvider> colDataProvider;
}