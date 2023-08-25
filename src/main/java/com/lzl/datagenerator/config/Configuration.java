package com.lzl.datagenerator.config;

import cn.hutool.db.meta.JdbcType;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配置类
 *
 * @author LZL
 * @version v1.0
 * @since 2023/8/4-21:18
 */
@Data
public class Configuration {
    private String generate;
    private Map<String, String> jdbcTypeDefaultValue;
    private List<DataConfigBean> dataConfigList;
    private static volatile Configuration CONFIGURATION;
    private Map<JdbcType, Object> typeDefaultValMap;
    private Map<String, Object> placholderMap = new HashMap<>() {{
        put("$sysdate", LocalDateTime.now());
    }};

    public static Configuration getInstance() {
        // 第一次检查，避免不必要的同步
        if (CONFIGURATION == null) {
            synchronized (DataConfigBean.class) {
                // 第二次检查，保证只有一个实例被创建
                if (CONFIGURATION == null) {
                    CONFIGURATION = YamlUtil.loadByPath("classpath:/config/generate.yml", Configuration.class);
                    CONFIGURATION.getDataConfigList().parallelStream().filter(
                                         config -> "ALL".equals(CONFIGURATION.getGenerate()) || CONFIGURATION.getGenerate().contains(config.getDataSourceId()))
                                 .forEach(DataConfigBean::init);
                    CONFIGURATION.initJdbcTypeDefaultValue();
                }
            }
        }
        return CONFIGURATION;
    }

    private void initJdbcTypeDefaultValue() {
        typeDefaultValMap = jdbcTypeDefaultValue.entrySet().parallelStream().collect(Collectors.toMap(e -> JdbcType.valueOf(e.getKey()), e -> {
            if (placholderMap.containsKey(e.getValue())) {
                return placholderMap.get(e.getValue());
            }
            return e.getValue();
        }));
    }

    private Configuration() {}
}