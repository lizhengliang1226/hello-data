package com.lzl.datagenerator.config;

import cn.hutool.db.meta.Table;
import lombok.Data;

import java.util.Set;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/8/24-18:24
 */
@Data
public class TableConfig {
    private String tableName;
    private Integer genNum;
    private Set<String> pkInfo;
    private Table tableMetaInfo;
}