package com.lzl.datagenerator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 策略模板(GenStrategyTemplate)实体类
 *
 * @author makejava
 * @since 2023-08-30 23:25:20
 */
@TableName
public class GenStrategyTemplate implements Serializable {
    private static final long serialVersionUID = -46524329231763615L;
    /**
     * 策略模板ID，当为@时无意义
     */
    @TableId
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


    public String getStrategyTmplId() {
        return strategyTmplId;
    }

    public void setStrategyTmplId(String strategyTmplId) {
        this.strategyTmplId = strategyTmplId;
    }

    public String getStrategyCode() {
        return strategyCode;
    }

    public void setStrategyCode(String strategyCode) {
        this.strategyCode = strategyCode;
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

    public Long getStep() {
        return step;
    }

    public void setStep(Long step) {
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

