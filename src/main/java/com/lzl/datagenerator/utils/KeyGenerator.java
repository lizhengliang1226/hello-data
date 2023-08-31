package com.lzl.datagenerator.utils;

/**
 * key构建器
 *
 * @author makejava
 * @version 1.0
 * @since 2023-08-31 10:29:01
 */
public class KeyGenerator {
    private static final String SEP="_";
    public static final String DICT_CACHE_PREFIX = "DICT_CACHE";
    public static String genDictCacheKey(String datasourceId){
        return DICT_CACHE_PREFIX+SEP+datasourceId;
    }
}