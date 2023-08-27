package entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 列生成配置表(ColumnConfig)实体类
 *
 * @author makejava
 * @since 2023-08-27 14:33:26
 */
@Data
public class ColumnConfig implements Serializable {
    private static final long serialVersionUID = 328547616334169117L;
    /**
     * 数据源ID
     */
    private String datasourceId;
    /**
     * 列名
     */
    private String columnName;
    /**
     * 默认值
     */
    private String defaultVal;
    /**
     * 策略名
     */
    private String strategyName;
    /**
     * 基础值
     */
    private Long baseValue;
    /**
     * 前缀
     */
    private String prefix;
    /**
     * 后缀
     */
    private String suffix;
    /**
     * 步进
     */
    private Long step;
    /**
     * 查询SQL
     */
    private String querySql;
    /**
     * 查询的列
     */
    private String queryCol;
    /**
     * 随机元素列表
     */
    private String randomEle;
    /**
     * 字典列名
     */
    private String dictColName;
    /**
     * 固定值
     */
    private String fixedValue;

}

