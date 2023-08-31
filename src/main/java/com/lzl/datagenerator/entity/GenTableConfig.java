package com.lzl.datagenerator.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 表生成配置表(GenTableConfig)实体类
 *
 * @author makejava
 * @since 2023-08-30 23:25:20
 */
@Data
public class GenTableConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = -76704557516081700L;
    /**
     * 数据源ID
     */
    private String datasourceId;
    /**
     * 生成表代码
     */
    private String tableCode;
    /**
     * 生成数量
     */
    private Integer genNum;
}

