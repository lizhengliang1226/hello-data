package com.lzl.datagenerator.strategy;

import com.lzl.datagenerator.annotations.Strategy;
import com.lzl.datagenerator.config.ColumnConfig;
import lombok.ToString;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
@Strategy(name = "fixed-value")
@ToString
public  class FixedValueDataStrategy implements DataStrategy {
    private Object fixedValue;

    @Override
    public Object getNextVal() {
        return fixedValue;
    }

    @Override
    public String getName() {
        return "fixed-value";
    }

    public FixedValueDataStrategy(ColumnConfig columnConfig) {
        this.fixedValue = columnConfig.getFixedValue();
    }

    public FixedValueDataStrategy() {
    }
}