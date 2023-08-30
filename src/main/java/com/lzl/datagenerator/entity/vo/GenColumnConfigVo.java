package com.lzl.datagenerator.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 列配置vo
 *
 * @author LZL
 * @version 1.0
 * @since 2023/8/31
 */
@Data
public class GenColumnConfigVo implements Serializable {
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

    /**
     * 策略代码
     */
    private String strategyCode;
    /**
     * 基础值
     */
    private Long baseValue;
    /**
     * 前缀
     */
    private String prefix;
    /**
     * 后缀
     */
    private String suffix;
    /**
     * 步进
     */
    private Long step;
    /**
     * 查询SQL
     */
    private String querySql;
    /**
     * 查询的列
     */
    private String queryCol;
    /**
     * 随机元素列表
     */
    private String randomEle;
    /**
     * 字典列名
     */
    private String dictColName;
}
