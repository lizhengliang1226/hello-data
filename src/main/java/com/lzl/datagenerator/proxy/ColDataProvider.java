package com.lzl.datagenerator.proxy;

import java.util.Set;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
public interface ColDataProvider extends Cloneable {
    /**
     * 返回当前数据支持器的列名
     * @return 列名
     */
    String getName();

    /**
     * 返回默认值
     * @return 默认值
     */
    Object getDefaultVal();

    /**
     * 返回下一个值
     * @return 下一个值
     */
    Object getNextVal();

    /**
     * 重置方法，将数据重置到开始状态，在每张表数据生成完后调用
     */
    void reset();

    /**
     * 获取当前已经生成的数据集合
     * @return 数据集合
     */
    Set<Object> getCurGenSet();
}