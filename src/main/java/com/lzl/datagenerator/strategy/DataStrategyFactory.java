package com.lzl.datagenerator.strategy;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.log.Log;
import com.lzl.datagenerator.annotations.Strategy;
import com.lzl.datagenerator.config.ColumnConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LZL
 * @version v1.0
 * @since 2023/7/31-22:24
 */
public class DataStrategyFactory {
    private static Map<String, Class<DataStrategy>> dataStrategyClassMap;
    static {
        init();
    }

    private static void init() {
        Set<Class<?>> classes = ClassUtil.scanPackage("", strategyClass -> strategyClass.isAnnotationPresent(
                Strategy.class) && DataStrategy.class.isAssignableFrom(strategyClass));
        dataStrategyClassMap = classes.stream()
                                      .map(aClass -> Pair.of(aClass.getAnnotation(Strategy.class).name(), (Class<DataStrategy>) aClass))
                                      .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    /**
     * 根据列配置创建列的数据生成策略实例
     *
     * @param columnConfig 列配置
     * @return 数据策略实例
     */
    public static DataStrategy createDataStrategy(ColumnConfig columnConfig) {
        Class<DataStrategy> dataStrategyClass = dataStrategyClassMap.get(columnConfig.getStrategy());
        if (dataStrategyClass == null) {
            Log.get().error("没有名为{}的列策略实现，请检查！", columnConfig.getStrategy());
            throw new RuntimeException();
        }
        Constructor<DataStrategy> constructor = null;
        try {
            constructor = dataStrategyClass.getConstructor(ColumnConfig.class);
            return constructor.newInstance(columnConfig);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            Log.get()
               .error("创建数据源[{}]-列名[{}]-策略名[{}]的数据生成器时发生了异常，异常信息：", columnConfig.getDataSourceId(),
                      columnConfig.getColName(), columnConfig.getStrategy());
            throw new RuntimeException(e);
        }
    }
}