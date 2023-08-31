package com.lzl.datagenerator;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.meta.Column;
import cn.hutool.db.meta.JdbcType;
import cn.hutool.log.Log;
import com.google.common.collect.Lists;
import com.lzl.datagenerator.config.CacheManager;
import com.lzl.datagenerator.config.TableConfig;
import com.lzl.datagenerator.loader.ConfigLoader;
import com.lzl.datagenerator.proxy.ColDataProvider;
import com.lzl.datagenerator.utils.KeyGenerator;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;


/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
public class DataGenerator {
    private final ConfigLoader configuration;
    private static final String DEL_TABLE_TMPL = "TRUNCATE TABLE %s";

    public DataGenerator(ConfigLoader configuration) {
        this.configuration = configuration;
    }

    public void generate() {
        configuration.getTableConfigMap().entrySet().parallelStream().forEach(
                dataConfigBean -> dataConfigBean.getValue().parallelStream().map(this::generateDataList).forEach(this::saveData));

    }

    private void saveData(Pair<TableConfig, List<Entity>> dataPair) {
        List<Entity> dataList = dataPair.getValue();
        TableConfig tableConfig = dataPair.getKey();
        String tableName = tableConfig.getTableName();
        Set<String> pkInfo = tableConfig.getPkInfo();
        String datasourceId = tableConfig.getDatasourceId();
        if (CollectionUtil.isNotEmpty(dataList)) {
            Log.get().info("开始删除表{}数据.", tableName);
            try {
                deleteByBatchSql(datasourceId, dataList, tableName, pkInfo);
                // deleteByPkInfo(dataConfigBean, dataList, tableName, pkInfo);
            } catch (Exception e) {
                Log.get().error("删除表[{}]数据失败", tableName);
                throw new RuntimeException(e);
            }
            Log.get().info("开始保存表{}数据，预计保存数据{}条.", tableName, dataList.size());
            Lists.partition(dataList, 5000).parallelStream().forEach(list -> {
                try {
                    DbUtil.use(configuration.getGlobalDsFactory().getDataSource(datasourceId)).insert(list);
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
     * @param dataList  数据集合
     * @param tableName 表名
     * @param pkInfo    主键列
     */
    private void deleteByPkInfo(String datasourceId, List<Entity> dataList, String tableName, Set<String> pkInfo) {
        dataList.parallelStream().map(data -> {
            Entity entity = Entity.create(tableName);
            Map<String, Object> pkDataMap = pkInfo.parallelStream().map(pk -> Pair.of(pk, data.get(pk))).collect(
                    Collectors.toMap(Pair::getKey, Pair::getValue));
            entity.putAll(pkDataMap);
            return entity;
        }).forEach(e -> {
            try {
                DbUtil.use(configuration.getGlobalDsFactory().getDataSource(datasourceId)).del(e);
            } catch (Exception ex) {
                Log.get().error("数据库ID[{}]，表[{}]的数据删除失败，删除主键：{}", datasourceId, tableName, generatePkStr(e, pkInfo));
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
     * @param dataList  数据列表
     * @param tableName 表名
     * @param pkInfo    主键列
     */
    private void deleteByBatchSql(String datasourceId, List<Entity> dataList, String tableName, Set<String> pkInfo) {
        List<String> deleteSqlList = Lists.partition(dataList, 10).parallelStream().map(list -> list.parallelStream().flatMap(
                                                                                                            data -> pkInfo.parallelStream().map(pk -> Pair.of(pk, "'" + data.get(pk) + "'")).toList().stream()).collect(
                                                                                                            Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList()))).entrySet().parallelStream()
                                                                                                    .map(pkDataMap -> pkDataMap.getValue()
                                                                                                                               .parallelStream()
                                                                                                                               .map(String::valueOf)
                                                                                                                               .collect(
                                                                                                                                       Collectors.joining(
                                                                                                                                               ",",
                                                                                                                                               pkDataMap.getKey() + " in (",
                                                                                                                                               ")")))
                                                                                                    .collect(Collectors.joining(" and ",
                                                                                                                                "delete from " + tableName + " where ",
                                                                                                                                ""))).toList();
        try {
            DataSource dataSource = configuration.getGlobalDsFactory().getDataSource(datasourceId);
            DbUtil.use(dataSource).executeBatch(deleteSqlList);
        } catch (SQLException e) {
            Log.get().error("数据源[{}]删除数据失败，异常信息：{}", datasourceId, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建数据集合
     *
     * @return 生成的数据集合
     */
    private Pair<TableConfig, List<Entity>> generateDataList(TableConfig tableConfig) {
        String tableCode = tableConfig.getTableName();
        Collection<Column> columnMetaInfoList = tableConfig.getColumns();
        // 唯一索引和主键去重后的列名集合，包含在里面的就要自己定义生成器生产数据
        List<Entity> res = LongStream.range(0L, tableConfig.getGenNum()).parallel().mapToObj(index -> Entity.create(tableCode)).peek(
                                             dataEntity -> columnMetaInfoList.forEach(column -> setColumnValue(tableConfig, tableConfig.getPkInfo(), dataEntity, column)))
                                     .toList();
        return Pair.of(tableConfig, res);
    }

    private void setColumnValue(TableConfig tableConfig, Set<String> uniqueIndexColAndPkSet, Entity entity, Column column) {
        Map<String, List<String>> genIgnoreColMap = configuration.getGenIgnoreColMap();
        String datasourceId = tableConfig.getDatasourceId();
        if (genIgnoreColMap.containsKey(datasourceId) && genIgnoreColMap.get(datasourceId).contains(column.getName())) {
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
        Object colDefaultVal = configuration.getGlobalColumnDefaultValMap().get(colName);
        // 取字典值
        Object dictDefaultVal = getDictValByColName(colName, datasourceId);
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
        Object defaultVal = configuration.getJdbcTypeDefaultValMap().get(jdbcType);
        if (defaultVal == null) {
            Log.get().error("JDBC数据类型{}没有默认值设置，请检查!", jdbcType.name());
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
            Map<String, List<Object>> dictCache = CacheManager.getInstance().get(KeyGenerator.genDictCacheKey(dataSourceId));
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