package com.lzl.datagenerator.loader;


import cn.hutool.aop.ProxyUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.SafeConcurrentHashMap;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.db.Db;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.meta.*;
import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import cn.hutool.setting.Setting;
import cn.hutool.setting.yaml.YamlUtil;
import com.lzl.datagenerator.config.CacheManager;
import com.lzl.datagenerator.config.ColumnConfig;
import com.lzl.datagenerator.config.TableConfig;
import com.lzl.datagenerator.entity.GenIgnoreCol;
import com.lzl.datagenerator.entity.GenJdbcTypeDefaultVal;
import com.lzl.datagenerator.entity.GenSystemConfig;
import com.lzl.datagenerator.entity.GenTableConfig;
import com.lzl.datagenerator.entity.vo.GenColumnConfigVo;
import com.lzl.datagenerator.entity.vo.GenColumnDefaultConfigVo;
import com.lzl.datagenerator.proxy.ColDataProvider;
import com.lzl.datagenerator.proxy.ColDataProviderProxyImpl;
import com.lzl.datagenerator.strategy.DataStrategy;
import com.lzl.datagenerator.strategy.DataStrategyFactory;
import com.lzl.datagenerator.utils.KeyGenerator;
import lombok.Getter;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 配置加载器
 * 负责加载系统所有的配置并封装成实体类对外提供
 *
 * @author LZL
 * @version 1.0
 * @since 2023/8/30
 */
public class ConfigLoader implements Loader {
    private static final String CONFIG_FILE_PATH = "classpath:config/generate.yml";
    private final String DATASOURCE_URL_PROP = "url";
    private final String DATASOURCE_USER_PROP = "username";
    private final String DATASOURCE_DRIVER_CLASS_NAME_PROP = "driver";
    private final String DATASOURCE_PASSWORD_PROP = "password";
    private Map<String, List<String>> genConfig;
    @Getter
    private DSFactory globalDsFactory;
    private static final byte[] encryptKey = new byte[]{-115, -35, -109, 40, -22, 108, 123, -92, -52, -28, -59, -21, -78, 54, -101, 8};
    private static final SymmetricCrypto AES = new SymmetricCrypto(SymmetricAlgorithm.AES, encryptKey);
    @Getter
    private final Map<String, Object> globalColumnDefaultValMap = new SafeConcurrentHashMap<>();
    @Getter
    private Map<String, List<TableConfig>> tableConfigMap = new HashMap<>();
    @Getter
    private Map<JdbcType, Object> jdbcTypeDefaultValMap = new HashMap<>();
    @Getter
    private Map<String, List<String>> genIgnoreColMap = new HashMap<>();

    @Override
    public void load() {
        readYmlConfig();
        loadDatabaseConfig();
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
            loadSystemConfig();
            loadTableConfig();
            loadJdbcDefaultConfig();
            loadIgnoreColConfig();
        } catch (SQLException e) {
            Log.get().error("系统配置加载失败！");
            throw new RuntimeException(e);
        }
    }

    private void loadIgnoreColConfig() {
        try {
            genIgnoreColMap = Db.use().findAll(Entity.create("GEN_IGNORE_COL"), GenIgnoreCol.class).stream().collect(
                    Collectors.groupingBy(GenIgnoreCol::getDatasourceId, Collectors.mapping(GenIgnoreCol::getColumnName, Collectors.toList())));
        } catch (SQLException e) {
            Log.get().error("加载系统忽略的列配置失败！");
            throw new RuntimeException(e);
        }
    }

    private void loadJdbcDefaultConfig() {
        try {
            List<GenJdbcTypeDefaultVal> genJdbcTypeDefaultValList = Db.use().findAll(Entity.create("GEN_JDBC_TYPE_DEFAULT_VAL"),
                                                                                     GenJdbcTypeDefaultVal.class);
            jdbcTypeDefaultValMap = genJdbcTypeDefaultValList.stream().map(c -> {
                try {
                    JdbcType jdbcType = JdbcType.valueOf(c.getJdbcType());
                    return Pair.of(jdbcType, transDefaultVal(c.getDefaultVal()));
                } catch (Exception e) {
                    Log.get().error("JDBC类型默认值配置错误，[{}]不是一个JDBC类型，请检查！", c.getJdbcType());
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        } catch (SQLException e) {
            Log.get().error("JDBC默认值配置加载失败！");
            throw new RuntimeException(e);
        }
    }

    private void loadTableConfig() {
        tableConfigMap = genConfig.entrySet().parallelStream().map(genConfigEntity -> {
            String datasourceId = genConfigEntity.getKey();
            List<String> tableCodeList = genConfigEntity.getValue();
            try {
                List<TableConfig> tableConfigList = Db.use().findAll(Entity.create("GEN_TABLE_CONFIG").set("DATASOURCE_ID", datasourceId)
                                                                           .set("TABLE_CODE", tableCodeList), GenTableConfig.class).parallelStream()
                                                      .map(tc -> {
                                                          Integer genNum = tc.getGenNum();
                                                          String tableCode = tc.getTableCode();
                                                          TableConfig tableConfig = new TableConfig();
                                                          Table tableMetaInfo = MetaUtil.getTableMeta(globalDsFactory.getDataSource(datasourceId),
                                                                                                      tableCode);
                                                          // 唯一索引和主键去重后的列名集合，包含在里面的就要自己定义生成器生产数据
                                                          Set<String> uniqueIndexColAndPkSet = getUniqueIndexCol(tableMetaInfo);
                                                          Collection<Column> columnsMetaData = tableMetaInfo.getColumns();
                                                          Map<String, ColDataProvider> colDataProviderMap = createColDataProviderMap(columnsMetaData,
                                                                                                                                     datasourceId,
                                                                                                                                     tableCode);
                                                          tableConfig.setTableName(tableCode);
                                                          tableConfig.setGenNum(genNum);
                                                          tableConfig.setDatasourceId(datasourceId);
                                                          tableConfig.setPkInfo(uniqueIndexColAndPkSet);
                                                          tableConfig.setColumns(columnsMetaData);
                                                          tableConfig.setColDataProvider(colDataProviderMap);
                                                          return tableConfig;
                                                      }).toList();
                return Pair.of(datasourceId, tableConfigList);
            } catch (SQLException e) {
                Log.get().error("数据源ID[{}]表[{}]数据加载失败", datasourceId, String.join(",", tableCodeList));
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    /**
     * 根据表的元数据信息获取表的唯一索引和主键的列集合
     *
     * @param tableInfo 表的元数据信息
     * @return 主键和唯一索引列集合
     */
    private Set<String> getUniqueIndexCol(Table tableInfo) {
        return Stream.concat(tableInfo.getIndexInfoList().parallelStream().filter(index -> !index.isNonUnique()).flatMap(
                                              index -> index.getColumnIndexInfoList().parallelStream()).map(ColumnIndexInfo::getColumnName).collect(Collectors.toSet())
                                      .parallelStream(), tableInfo.getPkNames().parallelStream()).collect(Collectors.toSet());
    }

    private Map<String, ColDataProvider> createColDataProviderMap(Collection<Column> columnsMetaData, String datasourceId, String tableCode) {
        try {
            String sql = """
                    select DATASOURCE_ID,
                           TABLE_CODE,
                           COLUMN_NAME,
                           a.STRATEGY_TMPL_ID,
                           STRATEGY_CODE,
                           BASE_VALUE,
                           PREFIX,
                           SUFFIX,
                           STEP,
                           QUERY_SQL,
                           QUERY_COL,
                           RANDOM_ELE,
                           DICT_COL_NAME
                    from GEN_COLUMN_CONFIG a
                             left join GEN_STRATEGY_TEMPLATE b on a.STRATEGY_TMPL_ID = b.STRATEGY_TMPL_ID where a.DATASOURCE_ID=? and a.TABLE_CODE = ?
                              and a.COLUMN_NAME in(%s)
                    """;
            Map<String, GenColumnConfigVo> colConfigVoMap = DbUtil.use(globalDsFactory.getDataSource(datasourceId)).query(String.format(sql,  getColNameListStr(
                                                                                                                                  columnsMetaData)),
                                                                                                                          GenColumnConfigVo.class,
                                                                                                                          datasourceId, tableCode
                                                                                                                        )
                                                                  .stream().collect(Collectors.toMap(GenColumnConfigVo::getColumnName, c -> c));
            String sql1 = """
                    select DATASOURCE_ID,
                           COLUMN_NAME,
                           a.STRATEGY_TMPL_ID,
                           DEFAULT_VAL,
                           STRATEGY_CODE,
                           BASE_VALUE,
                           PREFIX,
                           SUFFIX,
                           STEP,
                           QUERY_SQL,
                           QUERY_COL,
                           RANDOM_ELE,
                           DICT_COL_NAME
                    from GEN_COLUMN_DEFAULT_CONFIG a
                             left join GEN_STRATEGY_TEMPLATE b on a.STRATEGY_TMPL_ID = b.STRATEGY_TMPL_ID
                    where a.DATASOURCE_ID = ?
                      and a.COLUMN_NAME in (%s)
                                        """;
            Map<String, GenColumnDefaultConfigVo> colDefaultConfigVoMap = DbUtil.use(globalDsFactory.getDataSource(datasourceId)).query(String.format(sql1, getColNameListStr(
                                                                                                                                                columnsMetaData)),
                                                                                                                                        GenColumnDefaultConfigVo.class,
                                                                                                                                        datasourceId)
                                                                                .parallelStream().filter(cg -> {
                        if ("@".equals(cg.getStrategyTmplId())) {
                            globalColumnDefaultValMap.put(cg.getDatasourceId() + "_" + cg.getColumnName(), transDefaultVal(cg.getDefaultVal()));
                            return false;
                        }
                        return true;
                    }).collect(Collectors.toMap(GenColumnDefaultConfigVo::getColumnName, c -> c));

            return columnsMetaData.parallelStream().map(column -> {
                ColumnConfig columnConfig = new ColumnConfig();
                String colName = column.getName();
                if (colConfigVoMap.containsKey(colName)) {
                    GenColumnConfigVo genColumnConfigVo = colConfigVoMap.get(colName);
                    copyGenColumnConfigVoProperties(columnConfig, genColumnConfigVo);
                    return columnConfig;
                }
                if (colDefaultConfigVoMap.containsKey(colName)) {
                    GenColumnDefaultConfigVo genColumnConfigVo = colDefaultConfigVoMap.get(colName);
                    copyGenColumnDefaultConfigVoProperties(columnConfig, genColumnConfigVo);
                    return columnConfig;
                }
                return null;
            }).filter(Objects::nonNull).map(this::createColDataProvider).collect(
                    Collectors.toMap(ColDataProvider::getName, colDataProvider -> colDataProvider));
        } catch (SQLException e) {
            Log.get().error("数据源ID[{}]表代码[{}]列配置查询失败！", datasourceId, tableCode);
            throw new RuntimeException(e);
        }
    }

    private static void copyGenColumnDefaultConfigVoProperties(ColumnConfig columnConfig, GenColumnDefaultConfigVo genColumnConfigVo) {
        BeanUtil.copyProperties(genColumnConfigVo, columnConfig, CopyOptions.create().setFieldValueEditor((fieldName, val) -> {
            List<String> val1 = randomEleFieldTrans(fieldName, val);
            if (val1 != null) {
                return val1;
            }
            return val;
        }));
    }

    private static void copyGenColumnConfigVoProperties(ColumnConfig columnConfig, GenColumnConfigVo genColumnConfigVo) {
        BeanUtil.copyProperties(genColumnConfigVo, columnConfig, CopyOptions.create().setFieldValueEditor((fieldName, val) -> {
            List<String> val1 = randomEleFieldTrans(fieldName, val);
            if (val1 != null) {
                return val1;
            }
            return val;
        }));
    }

    private static List<String> randomEleFieldTrans(String fieldName, Object val) {
        if ("randomEle".equals(fieldName)) {
            if (val != null && !"".equals(val.toString())) {
                return Arrays.asList(val.toString().split(","));
            }
        }
        return null;
    }

    private Object transDefaultVal(String defaultValue) {
        if ("$sysdate".equals(defaultValue)) {
            return LocalDateTime.now();
        }
        return defaultValue;
    }

    private String getColNameListStr(Collection<Column> columnsMetaData) {
        return columnsMetaData.stream().map(column -> "'" + column.getName() + "'").collect(Collectors.joining(","));
    }

    /**
     * 根据列配置创建列数据生成器
     *
     * @param columnConfig 列配置
     * @return 列数据生成器
     */
    private ColDataProvider createColDataProvider(ColumnConfig columnConfig) {
        DataStrategy dataStrategy = DataStrategyFactory.createDataStrategy(columnConfig);
        return createColDataProxy(columnConfig.getColumnName(), dataStrategy);

    }

    /**
     * 获取列数据生成器的代理实现
     *
     * @param colName  列名
     * @param strategy 策略名
     * @return 根据配置生成的代理实现
     */
    private ColDataProvider createColDataProxy(String colName, DataStrategy strategy) {
        return ProxyUtil.newProxyInstance(new ColDataProviderProxyImpl(colName, strategy), ColDataProvider.class);
    }

    private void loadSystemConfig() throws SQLException {
        List<GenSystemConfig> systemConfigList = Db.use().findAll(Entity.create("GEN_SYSTEM_CONFIG").set("DATASOURCE_ID", genConfig.keySet()),
                                                                  GenSystemConfig.class);
        Setting sysSetting = systemConfigList.parallelStream().map(this::decryptSysConfig).reduce(Setting.create(), (setting, sysConfig) -> {
            Setting curSetting = setting.setByGroup(DATASOURCE_URL_PROP, sysConfig.getDatasourceId(), sysConfig.getDatabaseUrl()).setByGroup(
                    DATASOURCE_USER_PROP, sysConfig.getDatasourceId(), sysConfig.getDatabaseUser()).setByGroup(DATASOURCE_PASSWORD_PROP,
                                                                                                               sysConfig.getDatasourceId(),
                                                                                                               sysConfig.getDatabasePassword()).setByGroup(DATASOURCE_DRIVER_CLASS_NAME_PROP,
                                                                                                                                                           sysConfig.getDatasourceId(),sysConfig.getDatabaseDriverClassName());
            if (sysConfig.getLoadDictCache() == 1) {
                loadDictCache(sysConfig, curSetting);
            }
            return curSetting;
        }, Setting::addSetting);
        globalDsFactory = DSFactory.create(sysSetting);
    }

    private GenSystemConfig decryptSysConfig(GenSystemConfig genSystemConfig) {
        String databasePassword = genSystemConfig.getDatabasePassword();
        String databaseUrl = genSystemConfig.getDatabaseUrl();
        String databaseUser = genSystemConfig.getDatabaseUser();
        genSystemConfig.setDatabaseUrl(AES.decryptStr(genSystemConfig.getDatabaseUrl()));
        genSystemConfig.setDatabaseUser(AES.decryptStr(genSystemConfig.getDatabaseUser()));
        genSystemConfig.setDatabasePassword(AES.decryptStr(genSystemConfig.getDatabasePassword()));
        return genSystemConfig;
    }


    private void loadDictCache(GenSystemConfig sysConfig, Setting curSetting) {
        DSFactory dsFactory = DSFactory.create(curSetting);
        DataSource dataSource = dsFactory.getDataSource(sysConfig.getDatasourceId());
        Map<Object, List<Object>> dictCache;
        try {
            dictCache = Db.use(dataSource).findAll(sysConfig.getDictTableName()).stream().collect(
                    Collectors.groupingBy(entity -> entity.get(sysConfig.getDictCodeColName()),
                                          Collectors.mapping(entity -> entity.get(sysConfig.getDictItemColName()), Collectors.toList())));
        } catch (SQLException e) {
            Log.get().error("数据源ID[{}]的字典缓存加载失败，异常信息：{}", sysConfig.getDatasourceId(), e.getMessage());
            throw new RuntimeException(e);
        }
        CacheManager.getInstance().put(KeyGenerator.genDictCacheKey(sysConfig.getDatasourceId()), dictCache);
        dsFactory.destroy();
    }
}