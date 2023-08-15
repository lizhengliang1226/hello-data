package com.lzl.datagenerator.strategy;

import com.lzl.datagenerator.config.ColumnConfig;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author LZL
 * @version v1.0
 * @date 2023/7/31-22:24
 */
@ToString
public class AutoIncDataStrategy implements DataStrategy {
    private final AtomicLong baseVal;
    private final String prefix;
    private final String suffix;

    @Override
    public Object getNextVal() {
        return String.format("%s%s%s", prefix == null ? "" : prefix, baseVal.getAndIncrement(), suffix == null ? "" : suffix);
    }

    @Override
    public String getName() {
        return "auto-inc";
    }

    public AutoIncDataStrategy(ColumnConfig columnConfig) {
        this.baseVal = new AtomicLong(columnConfig.getBaseValue().longValue());
        this.prefix = columnConfig.getPrefix();
        this.suffix = columnConfig.getSuffix();
    }
}