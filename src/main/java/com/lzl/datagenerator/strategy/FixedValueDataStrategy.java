package com.lzl.datagenerator.strategy;

import com.lzl.datagenerator.config.ColumnConfig;
import lombok.ToString;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
@ToString
public non-sealed class FixedValueDataStrategy implements DataStrategy {
    private final Object fixedValue;

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
}