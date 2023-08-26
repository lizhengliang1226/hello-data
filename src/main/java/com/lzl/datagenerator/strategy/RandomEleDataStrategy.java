package com.lzl.datagenerator.strategy;

import cn.hutool.core.util.RandomUtil;
import com.lzl.datagenerator.annotations.Strategy;
import com.lzl.datagenerator.config.ColumnConfig;
import lombok.ToString;

import java.util.List;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
@Strategy(name = "rand-ele")
@ToString
public  class RandomEleDataStrategy implements DataStrategy {
    private List<Object> randomList;

    @Override
    public Object getNextVal() {
        return RandomUtil.randomEle(randomList);
    }

    @Override
    public String getName() {
        return "rand-ele";
    }

    public RandomEleDataStrategy() {
    }

    public RandomEleDataStrategy(ColumnConfig columnConfig) {
        this.randomList = columnConfig.getRandomEle();
    }
}