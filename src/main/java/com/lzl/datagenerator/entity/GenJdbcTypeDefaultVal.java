package com.lzl.datagenerator.entity;

import java.io.Serial;
import java.io.Serializable;

/**
 * jdbc类型默认值表(GenJdbcTypeDefaultVal)实体类
 *
 * @author makejava
 * @since 2023-08-31 10:29:01
 */
public class GenJdbcTypeDefaultVal implements Serializable {
    @Serial
    private static final long serialVersionUID = 174073768341525601L;
    /**
     * jdbc类型
     */
    private String jdbcType;
    /**
     * 默认值
     */
    private String defaultVal;


    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

}