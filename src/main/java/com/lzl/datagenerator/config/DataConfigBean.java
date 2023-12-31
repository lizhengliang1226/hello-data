package com.lzl.datagenerator.config;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Db;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.meta.Column;
import cn.hutool.db.meta.ColumnIndexInfo;
import cn.hutool.db.meta.MetaUtil;
import cn.hutool.db.meta.Table;
import cn.hutool.log.Log;
import com.lzl.datagenerator.proxy.ColDataProvider;
import com.lzl.datagenerator.proxy.ColDataProviderProxyImpl;
import com.lzl.datagenerator.strategy.DataStrategy;
import com.lzl.datagenerator.strategy.DataStrategyFactory;
import lombok.Data;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
@Data
public class DataConfigBean {
    private String dataSourceId;
    private List<ColumnConfig> columnConfig;
    private List<TableConfig> tableConfig;
    private Map<String, TableConfig> tableConfigMap;
    private DictConfig dictConfig;
    private String loadDictCache;
    private Map<String, String> colDefaultValue = new HashMap<>(16);
    private volatile static DataConfigBean DATA_CONFIG_BEAN;
    private Map<String, ColumnConfig> columnConfigMap = new HashMap<>(16);
    private String ignoreCol;

    private void transColumnConfig() {
        if (CollectionUtil.isNotEmpty(columnConfig)) {
            try {
                columnConfigMap = columnConfig.parallelStream().flatMap(columnConfig -> {
                    String colName = columnConfig.getColumnName();
                    return Arrays.stream(colName.split(",")).flatMap(col -> {
                        ColumnConfig clone = ObjectUtil.clone(columnConfig);
                        clone.setColumnName(col);
                        clone.setDataSourceId(dataSourceId);
                        return Stream.of(Pair.of(col, clone));
                    });
                }).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
            } catch (IllegalStateException e) {
                Log.get().error("数据源{}的某个列策略配置了多条，列配置策略必须唯一，请根据异常信息检查列配置！", dataSourceId, e.getMessage());
                throw e;
            } catch (Exception e) {
                Log.get().error("转换数据源{}的列配置出现了异常，异常信息：{}", dataSourceId, e.getMessage());
                throw e;
            }

        }

    }

    private DataConfigBean() {

    }

    private void loadDictCache() {
        try {
            CacheManager.getInstance()
                        .put(dataSourceId, Db.use(dataSourceId)
                                             .findAll(dictConfig.getDictTableName())
                                             .stream()
                                             .collect(Collectors.groupingBy(entity -> entity.get(dictConfig.getDictCodeColName()),
                                                                            Collectors.mapping(entity -> entity.get(dictConfig.getDictItemColName()),
                                                                                               Collectors.toList()))));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("数据库ID为[%s]的字典缓存加载失败，异常信息：%s", dataSourceId, e.getMessage()));
        }
    }

    public void init() {
        if (Boolean.TRUE.toString().equals(loadDictCache)) {
            // 加载字典缓存
            loadDictCache();
        }
        // 转换列配置
        transColumnConfig();
        // 加载表的元数据信息和主键信息
        transTableConfig();
    }

    private void transTableConfig() {
        tableConfigMap = tableConfig.parallelStream().filter(t -> t.getGenNum() > 0).peek(tc -> {
            String tableCode = tc.getTableName();
            Table tableMetaInfo = MetaUtil.getTableMeta(DSFactory.get(dataSourceId), tableCode);
            // 唯一索引和主键去重后的列名集合，包含在里面的就要自己定义生成器生产数据
            Set<String> uniqueIndexColAndPkSet = getUniqueIndexCol(tableMetaInfo);
            Collection<Column> columnsMetaData = tableMetaInfo.getColumns();
            Map<String, ColDataProvider> colDataProviderMap = createColDataProviderMap(columnsMetaData);
            tc.setPkInfo(uniqueIndexColAndPkSet);
            tc.setColumns(columnsMetaData);
            tc.setColDataProvider(colDataProviderMap);
        }).collect(Collectors.toMap(TableConfig::getTableName, tc -> tc));
    }

    private Map<String, ColDataProvider> createColDataProviderMap(Collection<Column> columnsMetaData) {
        return columnsMetaData.parallelStream()
                              .filter(column -> columnConfigMap.containsKey(column.getName()))
                              .map(column -> createColDataProvider(columnConfigMap.get(column.getName())))
                              .collect(Collectors.toMap(ColDataProvider::getName, colDataProvider -> colDataProvider));
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

    /**
     * 根据表的元数据信息获取表的唯一索引和主键的列集合
     *
     * @param tableInfo 表的元数据信息
     * @return 主键和唯一索引列集合
     */
    private Set<String> getUniqueIndexCol(Table tableInfo) {
        return Stream.concat(tableInfo.getIndexInfoList()
                                      .parallelStream()
                                      .filter(index -> !index.isNonUnique())
                                      .flatMap(index -> index.getColumnIndexInfoList().parallelStream())
                                      .map(ColumnIndexInfo::getColumnName)
                                      .collect(Collectors.toSet())
                                      .parallelStream(), tableInfo.getPkNames().parallelStream()).collect(Collectors.toSet());
    }

}