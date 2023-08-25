package com.lzl.datagenerator.strategy;

import com.lzl.datagenerator.config.ColumnConfig;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
@ToString
public non-sealed class AutoIncDataStrategy implements DataStrategy {
    private final AtomicLong baseVal;
    private final String prefix;
    private final String suffix;
    private final long step;

    @Override
    public Object getNextVal() {
        return String.format("%s%s%s", prefix == null ? "" : prefix, baseVal.getAndAdd(step), suffix == null ? "" : suffix);
    }

    @Override
    public String getName() {
        return "auto-inc";
    }

    public AutoIncDataStrategy(ColumnConfig columnConfig) {
        this.baseVal = new AtomicLong(columnConfig.getBaseValue().longValue());
        this.prefix = columnConfig.getPrefix();
        this.suffix = columnConfig.getSuffix();
        this.step = columnConfig.getStep() == 0 ? 1 : columnConfig.getStep();
    }
}