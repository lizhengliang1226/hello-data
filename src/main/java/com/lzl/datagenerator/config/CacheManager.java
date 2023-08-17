package com.lzl.datagenerator.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存持有者
 *
 * @author LZL
 * @version v1.0
 * @since 2023/8/4-23:46
 */
public class CacheManager {
    private static CacheManager instance;
    private final Map<String, Object> cacheData;

    /**
     * 私有构造函数，防止外部实例化
     */
    private CacheManager() {
        cacheData = new HashMap<>();
    }

    /**
     * 获取缓存管理器实例
     * @return 缓存管理器的实例
     */
    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    /**
     * 添加数据到缓存
     * @param key 缓存的key
     * @param value 缓存的value
     * @param <T> 缓存的值类型
     */
    public <T> void put(String key, T value) {
        cacheData.put(key, value);
    }

    /**
     * 从缓存中获取数据
     * @param key 缓存的key
     * @return 缓存的值
     * @param <T> 缓存的值类型
     */
    public <T> T get(String key) {
        return (T) cacheData.get(key);
    }

    /**
     * 清除缓存中的数据
     */
    public void clear() {
        cacheData.clear();
    }
}