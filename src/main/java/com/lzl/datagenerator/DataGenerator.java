package com.lzl.datagenerator;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.db.Db;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.meta.Column;
import cn.hutool.db.meta.JdbcType;
import cn.hutool.log.Log;
import com.google.common.collect.Lists;
import com.lzl.datagenerator.config.CacheManager;
import com.lzl.datagenerator.config.Configuration;
import com.lzl.datagenerator.config.DataConfigBean;
import com.lzl.datagenerator.config.TableConfig;
import com.lzl.datagenerator.proxy.ColDataProvider;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;


/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
public class DataGenerator {
    private final Configuration configuration;
    private static final String DEL_TABLE_TMPL = "TRUNCATE TABLE %s";

    public DataGenerator(Configuration configuration) {
        this.configuration = configuration;
    }

    public void generate() {
        configuration.getDataConfigList()
                     .parallelStream()
                     .filter(config -> "ALL".equals(configuration.getGenerate()) || configuration.getGenerate().contains(config.getDataSourceId()))
                     .forEach(dataConfigBean -> dataConfigBean.getTableConfigMap()
                                                              .values()
                                                              .parallelStream()
                                                              .map(tableConfig -> generateDataList(tableConfig, dataConfigBean))
                                                              .forEach(dataPair -> saveData(dataConfigBean, dataPair)));

    }

    private void saveData(DataConfigBean dataConfigBean, Pair<String, List<Entity>> dataPair) {
        List<Entity> dataList = dataPair.getValue();
        String tableName = dataPair.getKey();
        TableConfig tableConfig = dataConfigBean.getTableConfigMap().get(tableName);
        Set<String> pkInfo = tableConfig.getPkInfo();
        if (CollectionUtil.isNotEmpty(dataList)) {
            Log.get().info("开始删除表{}数据.", tableName);
            try {
//                 deleteByBatchSql(dataConfigBean, dataList, tableName, pkInfo);
                deleteByPkInfo(dataConfigBean, dataList, tableName, pkInfo);
            } catch (Exception e) {
                Log.get().error("删除表[{}]数据失败", tableName);
                throw new RuntimeException(e);
            }
            Log.get().info("开始保存表{}数据，预计保存数据{}条.", tableName, dataList.size());
            Lists.partition(dataList, 5000).parallelStream().forEach(list -> {
                try {
                    Db.use(dataConfigBean.getDataSourceId()).insert(list);
                } catch (SQLException e) {
                    Log.get().error("保存表[{}]数据失败，原因[{}]", tableName, e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        } else {
            Log.get().info("表{}生成的数据量为0，不生成数据", tableName);
        }
    }

    /**
     * 通过主键并行的一个个删除数据
     *
     * @param dataConfigBean 数据配置
     * @param dataList       数据集合
     * @param tableName      表名
     * @param pkInfo         主键列
     */
    private void deleteByPkInfo(DataConfigBean dataConfigBean, List<Entity> dataList, String tableName, Set<String> pkInfo) {
        dataList.parallelStream().map(data -> {
            Entity entity = Entity.create(tableName);
            Map<String, Object> pkDataMap = pkInfo.parallelStream()
                                                  .map(pk -> Pair.of(pk, data.get(pk)))
                                                  .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
            entity.putAll(pkDataMap);
            return entity;
        }).forEach(e -> {
            try {
                DbUtil.use(DbUtil.getDs(dataConfigBean.getDataSourceId())).del(e);
            } catch (Exception ex) {
                Log.get()
                   .error("数据库ID[{}]，表[{}]的数据删除失败，删除主键：{}", dataConfigBean.getDataSourceId(), tableName, generatePkStr(e, pkInfo));
                throw new RuntimeException(ex);
            }
        });
    }

    private String generatePkStr(Entity e, Set<String> pkInfo) {
        return pkInfo.stream().map(pk -> String.valueOf(e.get(pk))).collect(Collectors.joining("_"));
    }

    /**
     * 通过构建删除语句，批量执行，完成数据删除，使用的条件是根据主键得到in查询条件
     *
     * @param dataConfigBean 数据配置
     * @param dataList       数据列表
     * @param tableName      表名
     * @param pkInfo         主键列
     */
    private void deleteByBatchSql(DataConfigBean dataConfigBean, List<Entity> dataList, String tableName, Set<String> pkInfo) {
        List<String> deleteSqlList = Lists.partition(dataList, 10)
                                          .parallelStream()
                                          .map(list -> list.parallelStream()
                                                           .flatMap(data -> pkInfo.parallelStream()
                                                                                  .map(pk -> Pair.of(pk, "'" + data.get(pk) + "'"))
                                                                                  .toList()
                                                                                  .stream())
                                                           .collect(Collectors.groupingBy(Pair::getKey,
                                                                                          Collectors.mapping(Pair::getValue, Collectors.toList())))
                                                           .entrySet()
                                                           .parallelStream()
                                                           .map(pkDataMap -> pkDataMap.getValue()
                                                                                      .parallelStream()
                                                                                      .map(String::valueOf)
                                                                                      .collect(Collectors.joining(",", pkDataMap.getKey() + " in (",
                                                                                                                  ")")))
                                                           .collect(Collectors.joining(" and ", "delete from " + tableName + " where ", "")))
                                          .toList();
        try {
            Db.use(dataConfigBean.getDataSourceId()).executeBatch(deleteSqlList);
        } catch (SQLException e) {
            Log.get().error("数据源{}删除数据失败，异常信息：{}", dataConfigBean.getDataSourceId(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建数据集合
     *
     * @param dataConfig 数据源id
     * @return 生成的数据集合
     */
    private Pair<String, List<Entity>> generateDataList(TableConfig tableConfig, DataConfigBean dataConfig) {
        String tableCode = tableConfig.getTableName();
        Collection<Column> columnMetaInfoList = tableConfig.getColumns();
        // 唯一索引和主键去重后的列名集合，包含在里面的就要自己定义生成器生产数据
        List<Entity> res = LongStream.range(0L, tableConfig.getGenNum())
                                     .parallel()
                                     .mapToObj(index -> Entity.create(tableCode))
                                     .peek(dataEntity -> columnMetaInfoList.forEach(
                                             column -> setColumnValue(dataConfig, tableConfig, tableConfig.getPkInfo(), dataEntity, column)))
                                     .toList();
        return Pair.of(tableCode, res);
    }

    private void setColumnValue(DataConfigBean dataConfig,
                                TableConfig tableConfig,
                                Set<String> uniqueIndexColAndPkSet,
                                Entity entity,
                                Column column) {
        if (Optional.ofNullable(dataConfig.getIgnoreCol()).orElse("").contains(column.getName())) {
            return;
        }
        // 类型
        JdbcType typeEnum = column.getTypeEnum();
        // 列名
        String colName = column.getName();
        // 从上往下取值，优先级从高到低，数据生成策略>默认值>字典值>类型默认值
        // 数据生成策略器取值
        Object nextVal = getNextVal(tableConfig.getColDataProvider(), colName);
        // 字段默认值
        Object colDefaultVal = dataConfig.getColDefaultValue().get(colName);
        // 取字典值
        Object dictDefaultVal = getDictValByColName(colName, dataConfig.getDataSourceId());
        // 类型默认值
        Object typeDefaultVal = getDefaultValByJdbcType(typeEnum);
        if (uniqueIndexColAndPkSet.contains(colName) && nextVal == null && colDefaultVal == null && dictDefaultVal == null) {
            Log.get().error("表[{}]主键列或唯一索引列[{}]没有配置数据生成策略或默认值，请检查!", tableConfig.getTableName(), colName);
            throw new RuntimeException();
        }
        // 根据优先级取值
        // 生成器 > 字段默认值 > 字典值 > 类型默认值
        entity.set(colName,
                   nextVal == null ? colDefaultVal == null ? dictDefaultVal == null ? typeDefaultVal : dictDefaultVal : colDefaultVal : nextVal);
    }


    /**
     * 获取某一列的下一个值
     *
     * @param colName 列名
     * @return 下一个值
     */
    public Object getNextVal(Map<String, ColDataProvider> tableColDataProviderMap, String colName) {
        ColDataProvider colDataProvider = tableColDataProviderMap.get(colName);
        if (colDataProvider == null) {
            return null;
        }
        return colDataProvider.getNextVal();
    }

    /**
     * 获取默认值通过JDBC类型
     *
     * @param jdbcType jdbc类型
     * @return JDBC类型的默认值
     */
    public Object getDefaultValByJdbcType(JdbcType jdbcType) {
        Object defaultVal = configuration.getTypeDefaultValMap().get(jdbcType);
        if (defaultVal == null) {
            Log.get().error("数据类型{}没有默认值设置，请检查!", jdbcType.name());
            throw new RuntimeException();
        }
        return defaultVal;
    }

    /**
     * 尝试从字典缓存获取值，获取不到返回空，不报错
     *
     * @param colName      列名
     * @param dataSourceId 数据源ID
     * @return 获取到的字典值
     */
    public Object getDictValByColName(String colName, String dataSourceId) {
        try {
            Map<String, List<Object>> dictCache = CacheManager.getInstance().get(dataSourceId);
            List<Object> dictItems = dictCache.get(colName);
            if (dictItems != null) {
                return RandomUtil.randomEle(dictItems);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}