package com.lzl.datagenerator.config;

import cn.hutool.db.DbUtil;
import cn.hutool.db.meta.JdbcType;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.Data;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
                    CONFIGURATION = new Configuration();
                    CONFIGURATION = YamlUtil.loadByPath("classpath:/config/generate.yml", Configuration.class);
                    CONFIGURATION.getDataConfigList()
                                 .parallelStream()
                                 .filter(config -> "ALL".equals(CONFIGURATION.getGenerate()) || CONFIGURATION.getGenerate()
                                                                                                             .contains(config.getDataSourceId()))
                                 .forEach(DataConfigBean::init);
                    CONFIGURATION.initJdbcTypeDefaultValue();
                    CONFIGURATION.writeConfigToDb();
                }
            }
        }
        return CONFIGURATION;
    }

    private void writeConfigToDb() {
        dataConfigList.stream().forEach(dc -> {
            Map<String, String> colDefaultValue = dc.getColDefaultValue();
            dc.getColumnConfigMap().forEach((k, v) -> {
//                ColumnConfig columnConfig = new ColumnConfig();
//                columnConfig.setDatasourceId(v.getDataSourceId());
//                columnConfig.setColumnName(v.getColName());
//                columnConfig.setDefaultVal(colDefaultValue.getOrDefault(k, " "));
//                columnConfig.setStrategyName(v.getStrategy());
//                Number baseValue = v.getBaseValue();
//                if (baseValue == null) {
//                    columnConfig.setBaseValue(0L);
//                } else {
//                    columnConfig.setBaseValue(baseValue.longValue());
//                }
//                columnConfig.setPrefix(v.getPrefix());
//                columnConfig.setSuffix(v.getSuffix());
//                columnConfig.setStep(v.getStep());
//                columnConfig.setQuerySql(v.getQuerySql());
//                columnConfig.setQueryCol(v.getQueryCol());
//                columnConfig.setRandomEle(v.getRandomEle().stream().map(o->String.valueOf(o)).collect(Collectors.joining(",")));
//                columnConfig.setDictColName(v.getDictColName());
//                columnConfig.setFixedValue(Objects.toString(v.getFixedValue(),""));
                String a="insert into column_config (DATASOURCE_ID, COLUMN_NAME, STRATEGY_NAME)\n" + "values ('%s','%s','%s')";
                try {
                    DbUtil.use(DbUtil.getDs("mysql"))
                          .execute(String.format(a,v.getDataSourceId(),v.getColName(),v.getStrategy()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    public String getValue(Object o, Object def) {
        return String.valueOf(Optional.ofNullable(o).orElse(def));
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