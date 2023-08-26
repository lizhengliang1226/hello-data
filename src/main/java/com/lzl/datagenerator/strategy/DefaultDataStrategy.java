package com.lzl.datagenerator.strategy;

import com.lzl.datagenerator.annotations.Strategy;
import com.lzl.datagenerator.config.ColumnConfig;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
@Strategy(name = "default")
@ToString
public  class DefaultDataStrategy implements DataStrategy {
    private final AtomicLong baseVal = new AtomicLong(0L);

    public DefaultDataStrategy(ColumnConfig columnConfig) {
    }

    public DefaultDataStrategy() {
    }

    @Override
    public Object getNextVal() {
        return baseVal.getAndIncrement();
    }

    @Override
    public String getName() {
        return "default";
    }
}