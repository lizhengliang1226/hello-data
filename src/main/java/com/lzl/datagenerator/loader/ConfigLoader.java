package com.lzl.datagenerator.loader;


import cn.hutool.core.lang.Pair;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import cn.hutool.setting.Setting;
import cn.hutool.setting.yaml.YamlUtil;
import com.lzl.datagenerator.config.CacheManager;
import com.lzl.datagenerator.entity.GenColumnConfig;
import com.lzl.datagenerator.entity.GenSystemConfig;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配置加载器
 * 负责加载系统所有的配置并封装成实体类对外提供
 *
 * @author LZL
 * @version 1.0
 * @since 2023/8/30
 */
public class ConfigLoader implements Loader {
    private final String CONFIG_FILE_PATH = "classpath:config/generate.yml";
    private final String DATASOURCE_URL_PROP = "url";
    private final String DATASOURCE_USER_PROP = "username";
    private final String DATASOURCE_PASSWORD_PROP = "password";
    private final String DICT_CACHE_PREFIX = "DICT_CACHE";
    private Map<String, List<String>> genConfig;
    private DSFactory globalDsFactory;

    @Override
    public void load() {
        readYmlConfig();
        loadDatabaseConfig();
    }

    public static void main(String[] args) {
        ConfigLoader configLoader = new ConfigLoader();
        configLoader.load();
        configLoader.destory();
    }

    private void readYmlConfig() {
        genConfig = YamlUtil.loadByPath(CONFIG_FILE_PATH, JSONObject.class).getJSONArray("generate-datasource").stream().map(
                                    json -> (JSONObject) json).map(j -> Pair.of(j.get("id").toString(), Arrays.asList(String.valueOf(j.get("table-list")).split(","))))
                            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    @Override
    public void destory() {
        globalDsFactory.destroy();
    }

    public void loadDatabaseConfig() {
        try {
            List<GenColumnConfig> columnConfigList = Db.use().findAll(Entity.create("GEN_COLUMN_CONFIG"), GenColumnConfig.class);
            List<GenColumnConfig> tableConfigList = Db.use().findAll(Entity.create("GEN_TABLE_CONFIG"), GenColumnConfig.class);
            loadSystemConfig();
        } catch (SQLException e) {
            Log.get().error("系统配置加载失败！");
            throw new RuntimeException(e);
        }
    }

    private void loadSystemConfig() throws SQLException {
        List<GenSystemConfig> systemConfigList = Db.use().findAll(Entity.create("GEN_SYSTEM_CONFIG"), GenSystemConfig.class);
        Setting sysSetting = systemConfigList.parallelStream().reduce(Setting.create(), (setting, sysConfig) -> {
            Setting curSetting = setting.setByGroup(DATASOURCE_URL_PROP, sysConfig.getDatasourceId(), sysConfig.getDatabaseUrl()).setByGroup(
                    DATASOURCE_USER_PROP, sysConfig.getDatasourceId(), sysConfig.getDatabaseUser()).setByGroup(DATASOURCE_PASSWORD_PROP,
                                                                                                               sysConfig.getDatasourceId(),
                                                                                                               sysConfig.getDatabasePassword());
            if (sysConfig.getLoadDictCache() == 1) {
                loadDictCache(sysConfig, curSetting);
            }
            return curSetting;
        }, Setting::addSetting);
        globalDsFactory = DSFactory.create(sysSetting);
    }

    private void loadDictCache(GenSystemConfig sysConfig, Setting curSetting) {
        DSFactory dsFactory = DSFactory.create(curSetting);
        DataSource dataSource = dsFactory.getDataSource(sysConfig.getDatasourceId());
        Map<Object, List<Object>> dictCache = null;
        try {
            dictCache = Db.use(sysConfig.getDatasourceId()).findAll(sysConfig.getDictTableName()).stream().collect(
                    Collectors.groupingBy(entity -> entity.get(sysConfig.getDictCodeColName()),
                                          Collectors.mapping(entity -> entity.get(sysConfig.getDictItemColName()), Collectors.toList())));
        } catch (SQLException e) {
            Log.get().error("数据源ID[{}]的字典缓存加载失败，异常信息：{}", sysConfig.getDatasourceId(), e.getMessage());
            throw new RuntimeException(e);
        }
        CacheManager.getInstance().put(DICT_CACHE_PREFIX + "_" + sysConfig.getDatasourceId(), dictCache);
        dsFactory.destroy();
    }
}