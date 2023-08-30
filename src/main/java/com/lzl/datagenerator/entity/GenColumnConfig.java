package com.lzl.datagenerator.entity;

import java.io.Serializable;

/**
 * 列配置表(GenColumnConfig)实体类
 *
 * @author makejava
 * @since 2023-08-29 18:55:51
 */
public class GenColumnConfig implements Serializable {
    private static final long serialVersionUID = 114025035607878283L;
    /**
     * 数据源ID
     */
    private String datasourceId;
    /**
     * 列名
     */
    private String columnName;
    /**
     * 策略代码
     */
    private String strategyCode;
    /**
     * 默认值
     */
    private String defaultVal;
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
    private Integer step;
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

    public String getStrategyCode() {
        return strategyCode;
    }

    public void setStrategyCode(String strategyCode) {
        this.strategyCode = strategyCode;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public Long getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(Long baseValue) {
        this.baseValue = baseValue;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public String getQueryCol() {
        return queryCol;
    }

    public void setQueryCol(String queryCol) {
        this.queryCol = queryCol;
    }

    public String getRandomEle() {
        return randomEle;
    }

    public void setRandomEle(String randomEle) {
        this.randomEle = randomEle;
    }

    public String getDictColName() {
        return dictColName;
    }

    public void setDictColName(String dictColName) {
        this.dictColName = dictColName;
    }

}