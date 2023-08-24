package com.lzl.datagenerator.proxy;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.lzl.datagenerator.strategy.DataStrategy;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
@Data
public class ColDataProviderProxyImpl implements InvocationHandler {
    /**
     * 列名
     */
    private final String colName;
    /**
     * 数据生成策略
     */
    private final DataStrategy strategy;
    /**
     * 当前已经生成的数据Set集合
     */
    private final Set<Object> curGenDataSet;

    public ColDataProviderProxyImpl(String colName, DataStrategy strategy) {
        this.colName = colName;
        this.strategy = strategy;
        this.curGenDataSet = new ConcurrentHashSet<>();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        String name = method.getName();
        return switch (name) {
            case "getName" -> colName;
            case "getNextVal" -> {
                Object nextVal = strategy.getNextVal();
                curGenDataSet.add(nextVal);
                yield nextVal;
            }
            case "toString" -> toString();
            case "getCurGenSet" -> getCurGenSet();
            default -> throw new RuntimeException(String.format("没有方法%s的代理实现", name));
        };
    }

    private Set<Object> getCurGenSet() {
        return curGenDataSet;
    }
}