package com.lzl.datagenerator.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.db.Db;
import cn.hutool.log.Log;
import com.lzl.datagenerator.annotations.Strategy;
import com.lzl.datagenerator.config.CacheManager;
import com.lzl.datagenerator.config.ColumnConfig;
import lombok.ToString;

import java.sql.SQLException;
import java.util.List;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
@Strategy(name = "rand-table-ele")
@ToString
public  class RandomTableEleDataStrategy implements DataStrategy {
    private List<Object> randomList;
    private String dataSourceId;
    private String querySql;
    private String queryCol;

    @Override
    public Object getNextVal() {
        randomList = CacheManager.getInstance().get(dataSourceId + "_" + queryCol);
        if (CollUtil.isEmpty(randomList)) {
            try {
                randomList = Db.use(dataSourceId).query(querySql).stream().map(e -> e.get(queryCol)).distinct().toList();
                CacheManager.getInstance().put(dataSourceId + "_" + queryCol, randomList);
            } catch (SQLException e) {
                Log.get().error("构建rand-table-ele策略异常，数据源ID[{}]查询SQL[{}]查询字段[{}]", dataSourceId, querySql, queryCol);
                throw new RuntimeException(e);
            }
        }
        return RandomUtil.randomEle(randomList);
    }

    @Override
    public String getName() {
        return "rand-table-ele";
    }

    public RandomTableEleDataStrategy(ColumnConfig columnConfig) {
        dataSourceId = columnConfig.getDatasourceId();
        querySql = columnConfig.getQuerySql();
        queryCol = columnConfig.getQueryCol();
    }

    public RandomTableEleDataStrategy() {
    }
}