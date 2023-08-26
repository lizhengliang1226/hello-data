package com.lzl.datagenerator.strategy;

import com.lzl.datagenerator.annotations.Strategy;
import com.lzl.datagenerator.config.ColumnConfig;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
@Setter
@ToString
@Strategy(name = "auto-inc")
public  class AutoIncDataStrategy implements DataStrategy {
    private AtomicLong baseVal;
    private String prefix;
    private String suffix;
    private long step;

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

    public AutoIncDataStrategy() {
    }
}