package com.lzl.datagenerator.loader;

/**
 * 配置加载器接口
 * 定义需要加载的数据
 * @author LZL
 * @version 1.0
 * @since 2023/8/30
 */
public interface Loader {
    void load();
    void destory();
}